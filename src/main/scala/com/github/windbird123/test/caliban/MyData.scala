package com.github.windbird123.test.caliban

import com.github.windbird123.test.caliban.MyData.Origin.{ BELT, EARTH, MARS }
import com.github.windbird123.test.caliban.MyData.Role.{ Captain, Engineer }

object MyData {
  sealed trait Origin

  object Origin {
    case object EARTH extends Origin
    case object MARS  extends Origin
    case object BELT  extends Origin
  }

  sealed trait Role
  object Role {
    case class Captain(shipName: String)  extends Role
    case class Pilot(shipName: String)    extends Role
    case class Engineer(shipName: String) extends Role
  }

  case class Character(name: String, nicknames: List[String], origin: Origin, role: Option[Role])
  case class CharactersArgs(origin: Option[Origin])
  case class CharacterArgs(name: String)

  val sampleCharacters = List(
    Character("James Holden", List("Jim", "Hoss"), EARTH, Some(Captain("Rocinante"))),
    Character("Naomi Nagata", Nil, BELT, Some(Engineer("Rocinante"))),
    Character("Josephus Miller", List("Joe"), BELT, None),
    Character("Roberta Draper", List("Bobbie", "Gunny"), MARS, None)
  )
}
