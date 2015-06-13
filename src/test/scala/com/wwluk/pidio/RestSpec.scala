package com.wwluk.pidio

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model._
import akka.http.model.japi.RequestEntity
import akka.http.unmarshalling._
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.scalamock.proxy.ProxyMockFactory
import org.scalatest._
import org.scalatest.concurrent._
import org.scalatest.time.{Millis, Seconds, Span}

import scala.concurrent.Future

import org.scalamock.scalatest.MockFactory


class RestSpec extends FlatSpec with Matchers with ScalaFutures with MockFactory {
  implicit val testSystem = ActorSystem("test-system")
  import testSystem.dispatcher
  implicit val fm = ActorFlowMaterializer()
  implicit val defaultPatience =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(100, Millis))

  val testApi = stub[PlayerApi]


  val server = new RestServer {
    val api: PlayerApi = testApi
  }

  def GETRequest(uri: String) = sendRequest(HttpRequest(uri = uri))

  def POSTRequest(uri: String) = sendRequest(HttpRequest(HttpMethods.POST, uri))

  def DELETERequest(uri: String) = sendRequest(HttpRequest(HttpMethods.DELETE, uri))

  def sendRequest(req: HttpRequest) =
    Source.single(req).via(
      Http(testSystem).outgoingConnection(host = "127.0.0.1",
        port = 8081)
    ).runWith(Sink.head)

  def validateResponse(responseFuture: Future[HttpResponse], responseContent: String): Unit = {
    validateResponse(responseFuture, Seq(responseContent))
  }

  def validateResponse(responseFuture: Future[HttpResponse], responseContents: Seq[String]): Unit = {
    whenReady(responseFuture) { response =>
      response.status shouldBe StatusCode.int2StatusCode(200)
      val stringFuture = Unmarshal(response.entity).to[String]
      whenReady(stringFuture) { str =>
        responseContents foreach { responseContent =>
          str should include(responseContent)
        }
      }
    }
  }


  "Rest" should "return mock status" in {
    (testApi.status _) when() returns """>>> POLSKASTACJA .PL >>> - HOT 100 - Goraca Setka Nowych HITOW (Polskie Radio): Aronchupa - I'm An Albatraoz
[playing] #3/3   0:50/0:00 (0%)
volume: 90%   repeat: off   random: off   single: off   consume: off"""

    validateResponse(GETRequest("/status"), "HOT")
  }

  it should "play" in {
    (testApi.playCurrent _) when() returns "playing"
    validateResponse(GETRequest("/play"), "playing")
  }

  it should "stop" in {
    (testApi.stop _) when() returns "stopped"
    validateResponse(GETRequest("/stop"), "stopped")
  }

  it should "return volume" in {
    (testApi.volume _) when() returns Some(90)
    validateResponse(GETRequest("/volume"), "90")
  }

  it should "set volume" in {
    (testApi.setVolume _) when (33) returns "33"
    validateResponse(POSTRequest("/volume/33"), "33")
  }

  it should "return playlist" in {
    (testApi.playlist _) when() returns Playlist(Seq("track 1", "track 2", "track 3").map(track => PlaylistEntry(0, track)))
    validateResponse(GETRequest("/playlist"), Seq("track 1", "track 2"))
  }

  it should "remove song from playlist" in {
    validateResponse(DELETERequest("/playlist/remove/4"), "")
    (testApi.removeSong _) verify 4
  }

  it should "add song to playlist" in {
    val song = "http://1.2.3.4:8080/test.mp3"
    val request = HttpRequest(HttpMethods.POST, "/playlist/add", entity = HttpEntity(song))
    validateResponse(sendRequest(request), "added")
    (testApi.addSong _) verify song
  }

  it should "play given song" in {
    (testApi.play _) when 3 returns "track 3"
    validateResponse(GETRequest("/play/3"), "track 3")
    (testApi.play _) verify 3
  }
}
