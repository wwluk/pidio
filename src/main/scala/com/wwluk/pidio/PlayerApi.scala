package com.wwluk.pidio

import sys.process._

trait PlayerApi {
  def status:String

  def playCurrent: String

  def play(position: Int): String

  def stop: String

  def s: Status

  def volume: Option[Int]

  def setVolume(v: Int): String

  def playlist: Playlist

  def addSong(uri: String): Unit

  def removeSong(position: Int): Unit
}

class MPCApi extends PlayerApi {
  override def status: String = "mpc status" !!

  override def playCurrent: String = "mpc play" !!

  override def stop: String = "mpc stop" !!

  override def s = stringToStatus(status)

  implicit def stringToStatus(s: String) = Status("a", "b", """volume: (\d+)%""".r.findFirstMatchIn(s).map(_.group(1).toInt))

  override def volume: Option[Int] = {
    """volume: (\d+)%""".r.findFirstMatchIn("mpc volume" !!).map(_.group(1).toInt)
  }

  override def setVolume(v: Int): String = s"mpc volume $v" !!

  override def playlist: Playlist = Playlist("mpc playlist" lineStream)

  override def addSong(uri: String): Unit = s"mpc add $uri" !

  override def removeSong(position: Int): Unit = s"mpc del $position" !

  override def play(position: Int): String = {
    println("playing"); s"mpc play $position" !!
  }
}