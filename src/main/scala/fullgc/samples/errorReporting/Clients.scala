package fullgc.samples.errorReporting

import com.typesafe.scalalogging.LazyLogging

/**
  * Created by dani on 23/11/17.
  */
object Clients {

  object KafkaProducer extends LazyLogging{
    def send(eventContent: String): Unit = logger.info(s"sending to Kafka: $eventContent")//throw new IllegalArgumentException()//
  }
  object Monitor extends LazyLogging{
    def incrementCounter(name: String, tags: (String, String)): Unit = logger.info(s"incrementing counter $name")
    def record(name: String, actorName: String, messageName: String, startTime: Long): Unit = logger.info(s"$name latency for message $messageName in actor $actorName is ${(System.currentTimeMillis()- startTime).toInt}")
  }
  object Logger extends LazyLogging{
    def log(event: String): Unit = logger.info(s"error: $event")
  }
  object S3_Client extends LazyLogging{
    def upload(eventContent: String): Unit = logger.info(s"uploading event $eventContent to S3")
  }
}
