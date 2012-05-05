package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.JsValue
import actors.ClockActor
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.libs.Akka
import akka.actor.Props
import akka.util.Duration

object Application extends Controller {

  def main = Action { implicit request =>
    //implicit val headers = request.headers
    Ok(views.html.main("Your new application is ready.")) //, request))
  }

  def connect = {
    val ws = WebSocket.using[String] { request =>

      // Log events to the console
      val consumer = Iteratee.foreach[String](println).mapDone { _ =>
        println("Disconnected")
      }

      // Send a single 'Hello!' message
      val producer = Enumerator("{\"a\":2}")
      //val producer = Enumerator.imperative[JsValue](onStart = clientWebSocketHandler ! NotifyJoin(username))

      val clientWebSocketHandler = Akka.system.actorOf(Props(new ClockActor(consumer, producer)), name = "client-" + Math.random)
      Akka.system.scheduler.schedule(Duration.parse("0 seconds"), Duration.parse("5 seconds"), clientWebSocketHandler, "tick")

      (consumer, producer)
    }

    (ws)
  }

}