package actors
import akka.actor.Actor
import play.api.mvc.WebSocket
import akka.event.Logging
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.JsValue
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.PushEnumerator
import play.libs.Akka

case class Send(val text: String)
case class Tick
case class Read(val text: String)

class ClockActor(producer: PushEnumerator[String]) extends Actor {

  val log = Logging(context.system, this)
  def receive = {
    case Tick =>
      producer.push("Server : " + Math.random)
    case Send(t) =>
      producer.push(t)
    case Read(t) =>
      sender ! broadcast(t) //not working
  }
}