package com.wwluk.pidio

import akka.actor.{Actor, ActorLogging, Props}

object PlayerActor {
  def props(api: PlayerApi): Props = Props(new PlayerActor(api))
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
  }
}

case object GetStatus

case object PlayCurrent

case object Stop