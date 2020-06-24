package cupper.mydic2.value

case class NumOfAllWords(num: Int)
case class Word(id: Int, text: String, refCount: Int, lastRefTime: DateTime)
case class Example(id: Int, text: String)
case class DateTime(time: Long) {
  override def toString: String = DateTimeFormatter.dateTime2String(this)
}

// The following code is different JVM and JS
object DateTimeFormatter {
  implicit def dateTime2String(time: DateTime): String = ???
  implicit def string2DateTime(time: String): DateTime = ???
  implicit def long2DateTime(time: Long): DateTime = ???
}
