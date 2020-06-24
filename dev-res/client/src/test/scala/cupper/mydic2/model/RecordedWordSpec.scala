package cupper.mydic2.model

import org.scalatest.funsuite.AnyFunSuite
import org.scalamock.scalatest.MockFactory

import cupper.mydic2.repository.Repository
import cupper.mydic2.value.Word

class RecordedWordSpec extends AnyFunSuite with MockFactory {
  test("does not edit") {
    implicit val repo = mock[Repository]

    val word = Word(1, "word", 1, "")
    val recordedWord = RecordedWord()
  }
}
