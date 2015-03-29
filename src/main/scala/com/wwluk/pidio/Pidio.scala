package com.wwluk.pidio

object Pidio extends App with RestServer{
  override def api: PlayerApi = new MPCApi
}
