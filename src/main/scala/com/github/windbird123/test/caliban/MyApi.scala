package com.github.windbird123.test.caliban

import caliban.GraphQL.graphQL
import caliban.{ GraphQL, RootResolver }
import caliban.schema.Annotations.GQLDescription
import caliban.schema.{ GenericSchema, Schema }
import caliban.wrappers.Wrappers.{ printErrors, timeout }
import com.github.windbird123.test.caliban.MyData._
import zio._
import zio.clock.Clock
import zio.console.Console
import zio.duration.durationInt

object MyApi extends GenericSchema[Has[MyService]] {
  case class Queries(
    @GQLDescription("Return all characters from a given origin")
    characters: CharactersArgs => URIO[Has[MyService], List[Character]]
  )

  implicit val roleSchema: Schema[Has[MyService], Role]                     = gen[Role]
  implicit val characterSchema: Schema[Has[MyService], Character]           = gen[Character]
  implicit val characterArgsSchema: Schema[Has[MyService], CharacterArgs]   = gen[CharacterArgs]
  implicit val charactersArgsSchema: Schema[Has[MyService], CharactersArgs] = gen[CharactersArgs]

  val api: GraphQL[Console with Clock with Has[MyService]] = graphQL(
    RootResolver(
      Queries(args => MyService.getCharacters(args.origin))
    )
  ) @@ timeout(3.seconds) @@ printErrors

}
