package com.github.windbird123.test.caliban.starwars

import caliban.GraphQL.graphQL
import caliban.schema.Annotations.{ GQLDeprecated, GQLDescription }
import caliban.schema.{ GenericSchema, Schema }
import caliban.wrappers.Wrappers.{ printErrors, timeout }
import caliban.{ GraphQL, RootResolver }
import com.github.windbird123.test.caliban.starwars.MyData.{ CharacterArgs, CharactersArgs, Role, _ }
import zio.clock.Clock
import zio.console.Console
import zio.duration.durationInt
import zio.stream.ZStream
import zio.{ Has, URIO }

object MyApi extends GenericSchema[Has[MyService]] {
  case class Queries(
    @GQLDescription("Return all characters from a given origin")
    characters: CharactersArgs => URIO[Has[MyService], List[Character]],
    @GQLDeprecated("Use `characters`")
    character: CharacterArgs => URIO[Has[MyService], Option[Character]]
  )

  case class Mutations(deleteCharacter: CharacterArgs => URIO[Has[MyService], Boolean])
  case class Subscriptions(characterDeleted: ZStream[Has[MyService], Nothing, String])

  // schema 정의 순서 중요!
  implicit val roleSchema: Schema[Has[MyService], Role]                     = gen[Role]
  implicit val characterSchema: Schema[Has[MyService], Character]           = gen[Character]
  implicit val characterArgsSchema: Schema[Has[MyService], CharacterArgs]   = gen[CharacterArgs]
  implicit val charactersArgsSchema: Schema[Has[MyService], CharactersArgs] = gen[CharactersArgs]

  val api: GraphQL[Console with Clock with Has[MyService]] = graphQL(
    RootResolver(
      Queries(args => MyService.getCharacters(args.origin), args => MyService.findCharacter(args.name)),
      Mutations(args => MyService.deleteCharacter(args.name)),
      Subscriptions(MyService.deleteEvents)
    )
  ) @@ timeout(3.seconds) @@ printErrors
}
