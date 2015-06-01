package com.wwluk.pidio

import akka.actor.{Actor, ActorLogging, Props}

object PlayerActor {
  def props(api: PlayerApi): Props = {
    require(api != null)
    Props(new PlayerActor(api))
  }
}

class PlayerActor(api: PlayerApi) extends Actor with ActorLogging{
  override def receive: Receive = {
    case GetStatus =>
      val status = api.status
      sender ! status

    case PlayCurrent =>
      sender ! api.playCurrent

    case Stop =>
      sender ! api.stop

    case GetVolume =>
      sender ! api.volume

    case SetVolume(volume) =>
      sender ! api.volume(volume)
  }
}

case object GetStatus

case object PlayCurrent

case object Stop

case object GetVolume

case class SetVolume(vol: Int) {
  require(vol >= 0)
  require(vol <= 100)
}