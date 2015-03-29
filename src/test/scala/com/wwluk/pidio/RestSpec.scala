package com.wwluk.pidio

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model._
import akka.http.unmarshalling._
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.scalatest._
import org.scalatest.concurrent._
import org.scalatest.time.{Millis, Seconds, Span}

import scala.concurrent.Future


class RestSpec extends FlatSpec with Matchers with ScalaFutures {
  implicit val testSystem = ActorSystem("test-system")

  import testSystem.dispatcher

  implicit val fm = ActorFlowMaterializer()
  implicit val defaultPatience =
    PatienceConfig(timeout = Span(2, Seconds), interval = Span(5, Millis))

  val server = new RestServer {
    override val api: PlayerApi = new MockApi
  }

  def sendRequest(req: HttpRequest) =
    Source.single(req).via(
      Http(testSystem).outgoingConnection(host = "127.0.0.1",
        port = 8081)
    ).runWith(Sink.head)


  "Rest" should "return mock status" in {
    val request: Future[HttpResponse] = sendRequest(HttpRequest(uri = "/status"))
    whenReady(request) { response =>
      val stringFuture = Unmarshal(response.entity).to[String]
      whenReady(stringFuture) { str =>
        str should include("Artist")
      }
    }
  }

}
