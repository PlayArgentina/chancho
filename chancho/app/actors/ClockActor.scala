package actors
import akka.actor.Actor
import play.api.mvc.WebSocket
import akka.event.Logging
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.JsValue
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.PushEnumerator

case class Tick
case class Read(val text: String)

class ClockActor(producer: PushEnumerator[String]) extends Actor {

  val log = Logging(context.system, this)
  def receive = {
    case Tick =>
      producer.push("Server: " + Math.random)
    case Read(t) =>
      producer.push("client: " + t)
  }
}