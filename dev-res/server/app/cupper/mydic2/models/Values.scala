package cupper.mydic2.models

import java.text.SimpleDateFormat
import java.util.Calendar

object Values {
  case class Word(id: Int, word: String, ref_count: Int, last_ref_time: DateTime)
  case class WordInfo()
  case class Example(id: Int, content: String, word: Word)
  case class DateTime(time: Long)
  object DateTimeFormatter {
    def dateTime2String(time: DateTime): String = {
      val formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
      val calendar = Calendar.getInstance()
      calendar.setTimeInMillis(time.time)
      formatter.format(calendar.getTime)
    }

    def string2DateTime(time: String): DateTime = {
      val formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
      DateTime(formatter.parse(time).getTime)
    }

    def long2DateTime(time: Long): DateTime = {
      DateTime(time)
    }
  }
}
