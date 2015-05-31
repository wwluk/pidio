package com.wwluk.pidio

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.model.headers._
import akka.http.server.Directives._
import akka.http.server.{PathMatchers, Route}
import akka.pattern.ask
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Sink
import akka.util.Timeout
import spray.json.DefaultJsonProtocol

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


case class TestClass(a: String, b: Int)

trait Protocols extends DefaultJsonProtocol {
  implicit val testClassFormat = jsonFormat2(TestClass.apply)
  //  implicit val statusFormat = jsonFormat(Status, "artist", "volume")
  implicit val statusFormat = jsonFormat3(Status.apply)
  //  implicit val stateFormat = jsonFormat[State] {


}

trait RestServer extends Protocols {
  implicit val system = ActorSystem("sample")
  implicit val materializer = ActorFlowMaterializer()
  implicit val timeout = Timeout(5 seconds)

  def api: PlayerApi

  val actor = system.actorOf(PlayerActor.props(api), "api")

  implicit val executor = system.dispatcher
  val route: Route = {
    path("status") {
      get {
        complete {
          val future = actor ? GetStatus
          future.mapTo[String]
        }
      }
    } ~
      path("play") {
        get {
          complete {
            val future = actor ? PlayCurrent
            future.mapTo[String]
          }
        }
      } ~
      path("stop") {
        get {
          complete {
            val future = actor ? Stop
            future.mapTo[String]
          }
        }
      } ~
      path("volume") {
        get {
          complete {
            val f = actor ? GetVolume
            f.mapTo[Option[Int]].collect({ case Some(v) => v.toString
            case _ => throw new Exception("Cannot read volume")
            })
          }
        }
      } ~
      path("volume" / PathMatchers.IntNumber) { vol => {
        post {
          complete {
            val f = actor ? SetVolume(vol)
            f.map(_.toString)
          }
        }
      }
      } ~
      path("test") {
        get {
          complete {
            TestClass("a", 1)
          }
        }
      } ~ path("s") {
      get {
        complete {
          api.s
        }
      }
    }
  }

  val serverBinding = Http(system).bind(interface = "0.0.0.0", port = 8081)

  serverBinding.to(Sink.foreach(conn => {
    import akka.http.model.HttpMethods._

    val corsHeaders = List(`Access-Control-Allow-Origin`(HttpOriginRange.*),
      `Access-Control-Allow-Methods`(GET, POST, OPTIONS, DELETE),
      `Access-Control-Allow-Headers`("Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent"))
    val routeWithHeaders = respondWithHeaders(corsHeaders) {
      route
    }

    conn handleWith Route.handlerFlow(routeWithHeaders)
  })).run()
}


object RestApp extends App with RestServer {
  override def api: PlayerApi = new MockApi
}
