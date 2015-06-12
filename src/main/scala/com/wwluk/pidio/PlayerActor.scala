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

    case Play(position) =>
      sender ! api.play(position)

    case Stop =>
      sender ! api.stop

    case GetVolume =>
      sender ! api.volume

    case SetVolume(volume) =>
      sender ! api.setVolume(volume)

    case GetPlaylist =>
      sender ! api.playlist

    case Remove(pos) => {
      api.removeSong(pos)
      sender ! "removed"
    }

    case Add(uri) => {
      api.addSong(uri)
      sender ! "added"
    }
  }
}

case object GetStatus

case object PlayCurrent

case class Play(position: Int)

case object Stop

case object GetVolume

case class SetVolume(vol: Int) {
  require(vol >= 0)
  require(vol <= 100)
}

case object GetPlaylist

case class Remove(position: Int)

case class Add(uri: String)