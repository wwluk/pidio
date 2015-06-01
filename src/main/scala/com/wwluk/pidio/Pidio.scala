package com.wwluk.pidio

object Pidio extends App with RestServer{
  override val api: PlayerApi = new MPCApi
}
