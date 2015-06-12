package com.wwluk.pidio

import sys.process._

trait PlayerApi {
  def status:String

  def playCurrent: String

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

  override def s: Status = ???

  override def volume: Option[Int] = {
    """volume: (\d+)%""".r.findFirstMatchIn("mpc volume" !!).map(_.group(1).toInt)
  }

  override def setVolume(v: Int): String = s"mpc volume $v" !!

  override def playlist: Playlist = Playlist("mpc playlist" lineStream)

  override def addSong(uri: String): Unit = s"mpc add $uri" !

  override def removeSong(position: Int): Unit = s"mpc del $position" !
}

class MockApi extends PlayerApi {
  override def status: String = """>>> POLSKASTACJA .PL >>> - HOT 100 - Goraca Setka Nowych HITOW (Polskie Radio): Aronchupa - I'm An Albatraoz
[playing] #3/3   0:50/0:00 (0%)
volume: 90%   repeat: off   random: off   single: off   consume: off"""

  override def playCurrent: String = "playing"

  override def stop: String = "stopped"

  override def s = stringToStatus(status)


  implicit def stringToStatus(s: String) = Status("a", "b", """volume: (\d+)%""".r.findFirstMatchIn(s).map(_.group(1).toInt))

  override def volume: Option[Int] = Some(90)

  override def setVolume(v: Int): String = s"volume: $v"

  override def playlist: Playlist = Playlist(Seq("track 1", "track 2", "track 3"))

  override def addSong(uri: String): Unit = println(s"song $uri added")

  override def removeSong(position: Int): Unit = println(s"song $position deleted")
}