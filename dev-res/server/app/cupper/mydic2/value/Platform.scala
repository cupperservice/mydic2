package cupper.mydic2.value

import java.text.SimpleDateFormat
import java.util.Calendar

object DateTimeFormatter {
  implicit def dateTime2String(time: DateTime): String = {
    val formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    val calendar = Calendar.getInstance()
    calendar.setTimeInMillis(time.time)
    formatter.format(calendar.getTime)
  }

  implicit def string2DateTime(time: String): DateTime = {
    val formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    DateTime(formatter.parse(time).getTime)
  }

  implicit def long2DateTime(time: Long): DateTime = {
    DateTime(time)
  }
}
