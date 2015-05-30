package com.wwluk.pidio

trait PlayerApi {
  def status:String

  def playCurrent: String

  def stop: String

  def s: Status
}

class MPCApi extends PlayerApi {
  import sys.process._
  override def status: String = "mpc status" !!

  override def playCurrent: String = "mpc play" !!

  override def stop: String = "mpc stop" !!

  override def s: Status = ???
}

class MockApi extends PlayerApi {
  override def status: String = """>>> POLSKASTACJA .PL >>> - HOT 100 - Goraca Setka Nowych HITOW (Polskie Radio): Aronchupa - I'm An Albatraoz
[playing] #3/3   0:50/0:00 (0%)
volume: 90%   repeat: off   random: off   single: off   consume: off"""

  override def playCurrent: String = "playing"

  override def stop: String = "stopped"

  override def s = stringToStatus(status)


  implicit def stringToStatus(s: String) = Status("a", "b", """volume: (\d+)%""".r.findFirstMatchIn(s).map(_.group(1).toInt))
}