package cupper.mydic2.model

import org.scalatest.funsuite.AnyFunSuite
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

import cupper.mydic2.repository.Repository
import cupper.mydic2.model.RecordedWord
import cupper.mydic2.value.{NumOfAllWords, Example, Word}
import cupper.mydic2.value.DateTimeFormatter._

class RecordedWordSpec extends AnyFunSuite {
  test("found examples") {
    val examples = List(
      Example(1, "This is examples"),
      Example(2, "That example is nice"))
    implicit val repo = new RepositoryMock {
      override def getExamples(word: Word): Future[List[Example]] = Future(examples)
    }

    val recordedWord = RecordedWord(Word(1, "example", 2, "2020/01/01 12:00:00"))
    for(res <- recordedWord.getExamples()) {
      assert(res.examples == examples)
    }
  }

  test("edit word") {
    val beforeEdit = Word(1, "before", 2, "2020/06/24 15:40:59")
    val afterEdit = Word(1, "after", 2, "2020/06/24 15:40:59")
    implicit val repo = new RepositoryMock {
      override def updateWord(id: Int, text: String): Future[Option[Word]] = Future(Some(afterEdit))
    }
    val recordedWord = RecordedWord(beforeEdit)

    for(res <- recordedWord.update(afterEdit.text)) {
      assert(res.get.word == afterEdit)
    }
  }
}
