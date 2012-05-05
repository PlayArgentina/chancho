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
import actors.Read
import actors.Tick
import akka.actor.ActorPath
import actors.ClockActor
import java.util.ArrayList
import actors.ClockActor
import scala.collection.mutable.MutableList
import actors.Send
import actors.Send
import akka.actor.ActorRef
import akka.actor.Actor
import akka.routing.Broadcast

object Application extends Controller with Actor{

  def actors = MutableList[ActorRef]()

  def main = Action { implicit request =>
    Ok(views.html.main("Your new application is ready."))
  }

  def connect = {
    val ws = WebSocket.using[String] { request =>

      val producer = Enumerator.imperative[String]()
      val clientWebSocketHandler = Akka.system.actorOf(Props(new ClockActor(producer)), name = "client-" + Math.random)
      this.actors += clientWebSocketHandler
      val consumer = Iteratee.foreach[String] { event =>
        clientWebSocketHandler ! Read(event)
      }

      Akka.system.scheduler.schedule(Duration.parse("0 seconds"), Duration.parse("5 seconds"), clientWebSocketHandler, Tick)

      (consumer, producer)
    }

    (ws)
  }
  
  def receive = {
    case Broadcast(msg) =>
      this.actors.foreach { a =>
      	a ! Send("lala")
      }
  }

}