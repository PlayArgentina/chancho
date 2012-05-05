package actors
import akka.actor.Actor
import play.api.mvc.WebSocket
import akka.event.Logging
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.JsValue
import play.api.libs.iteratee.Enumerator

case object tick

class ClockActor(consumer: Iteratee[String, _], producer: Enumerator[String]) extends Actor {

  val log = Logging(context.system, this)
  def receive = {
    case "tick" =>

  }
}