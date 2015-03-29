package com.wwluk.pidio

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.server.Directives._
import akka.http.server.Route
import akka.pattern.ask
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Sink
import akka.util.Timeout

import scala.concurrent.duration._

trait RestServer {
  implicit val system = ActorSystem("sample")
  implicit val materializer = ActorFlowMaterializer()
  implicit val timeout = Timeout(5 seconds)

  def api: PlayerApi

  val actor = system.actorOf(PlayerActor.props(api), "api")

  import system.dispatcher

  val route: Route =
    path("status") {
      get {
        complete {
          val future = actor ? GetStatus
          future.mapTo[String]
        }
      }
    }

  val serverBinding = Http(system).bind(interface = "0.0.0.0", port = 8081)

  serverBinding.to(Sink.foreach(conn => {
    conn handleWith Route.handlerFlow(route)
  })).run()
}


object RestApp extends App with RestServer {
  override def api: PlayerApi = new MockApi
}
