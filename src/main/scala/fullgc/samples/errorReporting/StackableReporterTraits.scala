package fullgc.samples.errorReporting

/**
  * Created by dani on 23/11/17.
  */

import java.util.Date

import fullgc.samples.errorReporting.Clients._

import scala.util.Try

trait Sender {
  def send(event: ServingError): Unit
}

trait Log extends Sender {
  abstract override def send(event: ServingError): Unit = {
    Logger.log(event.description)
    super.send(event)
  }
}
trait Metric extends Sender {
  abstract override def send(event: ServingError): Unit = {
    Monitor.incrementCounter(event.getClass.getSimpleName/*this is not recommended here...*/, "errorCode" -> event.code.toString)
    super.send(event)
  }
}
trait S3_Backup extends Sender {
  abstract override def send(event: ServingError): Unit = {
    S3_Client.upload(event.content)
    super.send(event)
  }
}
trait Kafka extends Sender {
  abstract override def send(event: ServingError): Unit = {
      Try(KafkaProducer.send(event.content)).getOrElse(super.send(event))
  }
}

trait JsonTransformer extends Sender {
  import ServingError._
  abstract override def send(event: ServingError): Unit = {
    super.send(modifyContent(event, {
      val date = event match {
        case d: Timestamp => "\"date\":" + d.date
        case _ => ""
      }
      val cause = event match {
        case f: FatalError => "\"cause\":" + f.exceptionCause
        case i: InvalidRequestError => "\"cause\":" + i.paramName +":" +i.paramValue
      }
      s"""
           {
            "code:"${event.code},
            $date
            $cause
           }
       """
    }))
  }
}

trait CsVTransformer extends Sender {
  import ServingError._
  abstract override def send(event: ServingError): Unit = {
    super.send(modifyContent(event, {
      val date = event match {
        case d: Timestamp => d.date
        case _ => ""
      }
      val cause = event match {
        case f: FatalError => f.exceptionCause
        case i: InvalidRequestError => i.paramName +"," +i.paramValue
      }
      s"""${event.code},$date$cause"""
    }))
  }
}

trait Timestamp {
  val date: Date = new Date()
}