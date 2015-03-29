package com.wwluk.pidio

trait PlayerApi {
  def status:String
}

class MPCApi extends PlayerApi {
  import sys.process._
  override def status: String = "mpc status" !!
}

class MockApi extends PlayerApi {
  override def status: String = "Artist - song"
}