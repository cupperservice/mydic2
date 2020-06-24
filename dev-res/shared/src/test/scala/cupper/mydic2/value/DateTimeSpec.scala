package cupper.mydic2.value

import org.scalatest.funsuite.AnyFunSuite

import DateTimeFormatter._

class DateTimeSpec extends AnyFunSuite {
  test("String to DateTime") {
    def str2DateTime(datetime: DateTime): Unit = {
      assert(datetime == DateTime(1577804400000L))
    }
    str2DateTime("2020/01/01 00:00:00")
  }

  test("DateTime to String") {
    def dateTime2Str(datetime: String): Unit = {
      assert(datetime == "2020/01/01 00:00:00")
    }
    dateTime2Str(DateTime(1577804400000L))
  }

  test("Long to DateTime") {
    def toDateTime(datetime: DateTime): Unit = {
      assert(DateTime(1577804400000L) == datetime)
    }

    toDateTime(1577804400000L)
  }
}
