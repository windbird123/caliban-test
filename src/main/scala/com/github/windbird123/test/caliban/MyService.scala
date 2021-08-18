package com.github.windbird123.test.caliban

import com.github.windbird123.test.caliban.MyData.{ Origin, _ }
import zio._
import zio.magic._
import zio.stream.ZStream

trait MyService {
  def getCharacters(origin: Option[Origin]): UIO[List[Character]]
  def findCharacter(name: String): UIO[Option[Character]]
  def deleteCharacter(name: String): UIO[Boolean]
  def deleteEvents: ZStream[Any, Nothing, String]
}

object MyService {
  def getCharacters(origin: Option[Origin]): ZIO[Has[MyService], Nothing, List[Character]] =
    ZIO.serviceWith[MyService](_.getCharacters(origin))

  def findCharacter(name: String): ZIO[Has[MyService], Nothing, Option[Character]] =
    ZIO.serviceWith[MyService](_.findCharacter(name))

  def deleteCharacter(name: String): ZIO[Has[MyService], Nothing, Boolean] =
    ZIO.serviceWith[MyService](_.deleteCharacter(name))

  def deleteEvents: ZStream[Has[MyService], Nothing, String] = ZStream.accessStream(_.get.deleteEvents)

  val live: ZLayer[Any, Nothing, Has[MyService]] = ZLayer.wire(
    Ref.make(MyData.sampleCharacters).toLayer,
    Hub.unbounded[String].toLayer,
    MyServiceLive.toLayer[MyService]
  )

}

case class MyServiceLive(characters: Ref[List[Character]], subscribers: Hub[String]) extends MyService {
  override def getCharacters(origin: Option[Origin]): UIO[List[Character]] =
    characters.get.map(_.filter(c => origin.forall(c.origin == _)))

  override def findCharacter(name: String): UIO[Option[Character]] = characters.get.map(_.find(c => c.name == name))

  override def deleteCharacter(name: String): UIO[Boolean] =
    characters
      .modify(list =>
        if (list.exists(_.name == name)) (true, list.filterNot(_.name == name))
        else (false, list)
      )
      .tap(deleted => UIO.when(deleted)(subscribers.publish(name)))

  override def deleteEvents: ZStream[Any, Nothing, String] =
    ZStream.unwrapManaged(subscribers.subscribe.map(x => ZStream.fromQueue(x)))
}
