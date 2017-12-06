package fullgc.samples.errorReporting

/**
  * Created by dani on 23/11/17.
  */
abstract class ServingError extends ServingErrorSender{
  val code: Int
  val description: String
  val content: String
}
object ServingError{
  def modifyContent(error: ServingError, content: String): ServingError = {
    error match {
      case f: FatalError => FatalError(exceptionCause = f.exceptionCause, content = content)
      case i: InvalidRequestError => InvalidRequestError(i.paramName, i.paramValue, content = content)
    }
  }
}

trait ServingErrorSender extends Sender{
  this: ServingError =>
  def send(): Unit = send(this)
  override def send(event: ServingError): Unit = ""
}

case class FatalError(override val code: Int = 1, exceptionCause: String, content: String = "") extends ServingError with S3_Backup with CsVTransformer with Kafka with JsonTransformer with Timestamp with Metric with Log{
  override val description: String = s"Fatal Error code $code accrued"
}
case class InvalidRequestError(paramName: String, paramValue: String, override val code: Int = 2, content: String = "") extends ServingError with Kafka with JsonTransformer with Log{
  override val description: String = s"Invalid Request. Bad  parameter: $paramName" + " " + s"with value: $paramValue"
}