package fullgc.samples.errorReporting

import akka.actor.{ActorSystem, Props}
import fullgc.samples.actorMetrics.Actors.MyMonitoredActor
import fullgc.samples.actorMetrics.Messages.RecordableMessage

/**
  * Created by dani on 23/11/17.
  */
object Main extends App{
  FatalError(exceptionCause = new IllegalArgumentException().getClass.getSimpleName).send()
  InvalidRequestError("type", "banner").send()
  println("---------------------------------------------------------------------------------------")
  val actorSystem = ActorSystem("system")
  val myMonitoredActor = actorSystem.actorOf(Props[MyMonitoredActor])
  val message = SomeRecordableMessage()
  myMonitoredActor ! message
}
case class SomeRecordableMessage() extends RecordableMessage {
  override val messageName: String = this.getClass.getSimpleName
}
