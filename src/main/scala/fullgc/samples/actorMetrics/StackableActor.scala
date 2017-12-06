package fullgc.samples.actorMetrics

import akka.actor.Actor
import com.typesafe.scalalogging.StrictLogging
import fullgc.samples.actorMetrics.Messages.RecordableMessage
import fullgc.samples.errorReporting.Clients.Monitor

/**
  * Created by dani on 03/03/16.
  */
object Actors {

  trait LoggerActor extends Actor with StrictLogging {
    abstract override def receive: Receive = {
      case recordableMessage: RecordableMessage =>
        logger.info(s"handling message: ${recordableMessage.messageName}")
        super.receive(recordableMessage)
        logger.info(s"done handling message: ${recordableMessage.messageName}")
      case message => super.receive(message)
    }
  }

  trait LatencyRecorderActor extends Actor with StrictLogging {
    val actorName: String = this.getClass.getSimpleName

    abstract override def receive: Receive = {
      case recordableMessage: RecordableMessage =>
        Monitor.record("time-in-mailbox", actorName, recordableMessage.messageName, recordableMessage.dispatchTime)
        val start = System.currentTimeMillis()
        super.receive(recordableMessage)
        Thread.sleep(200)
        Monitor.record("processing-time", actorName, recordableMessage.messageName, start)
      case message => super.receive(message)
    }
  }

  abstract class MyActor extends Actor with StrictLogging {
    override def receive: Receive = {
      case message => logger.info("performing some work...")
    }
  }
  class MyMonitoredActor extends MyActor with LatencyRecorderActor with LoggerActor
}

object Messages {
  trait RecordableMessage extends InnMessage {
    val dispatchTime: Long = System.currentTimeMillis()
  }
  trait InnMessage {
    val messageName: String
  }
}

