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
  }
}

case object GetStatus