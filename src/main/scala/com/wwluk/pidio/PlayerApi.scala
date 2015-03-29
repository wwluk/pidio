package com.wwluk.pidio

trait PlayerApi {
  def status:String

  def playCurrent: String

  def stop: String
}

class MPCApi extends PlayerApi {
  import sys.process._
  override def status: String = "mpc status" !!

  override def playCurrent: String = "mpc play" !!

  override def stop: String = "mpc stop" !!
}

class MockApi extends PlayerApi {
  override def status: String = "Artist - song"

  override def playCurrent: String = "playing"

  override def stop: String = "stopped"
}