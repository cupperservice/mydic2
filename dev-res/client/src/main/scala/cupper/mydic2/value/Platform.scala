package cupper.mydic2.value

import scalajs.js

object DateTimeFormatter {
  implicit def dateTime2String(time: DateTime): String =
    new js.Date(time.time).toISOString()

  implicit def string2DateTime(time: String): DateTime =
    DateTime(new js.Date(time).getTime().toLong)

  implicit def long2DateTime(time: Long): DateTime = DateTime(time)
}
