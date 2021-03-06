package com.wwluk

package object pidio {

  case class Status(/*state: State.State, */ artist: String, song: String, volume: Option[Int])

  object State extends Enumeration {
    type State = Value
    val Playing, Paused, Stopped = Value
  }

  case class Playlist(playlist: Seq[PlaylistEntry])

  case class PlaylistEntry(position: Int, value: String)

}

