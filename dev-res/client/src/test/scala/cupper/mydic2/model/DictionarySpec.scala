package cupper.mydic2.model

import org.scalatest.funsuite.AnyFunSuite
import cupper.mydic2.repository.Repository
import cupper.mydic2.value.{NumOfAllWords, Example, Word}
import cupper.mydic2.value.DateTimeFormatter._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

class DictionarySpec extends AnyFunSuite {
  test("get information") {
    val info = new DictionaryInformation(NumOfAllWords(99))

    object Registry extends DictionaryComponent with RepositoryComponent {
      override val repository: Repository = new RepositoryMock {
        override def getInformation(): Future[DictionaryInformation] = Future {info}
      }
      override val dictionary: Dictionary = new Dictionary
    }

    for(result <- Registry.dictionary.getInformation()) yield assert(result == info)
  }

  test("found word at findWord") {
    val wordText = "annotation"
    val word = Word(1, wordText, 99, "2020/01/01 00:00:00")

    object Registry extends DictionaryComponent with RepositoryComponent {
      override val repository: Repository = new RepositoryMock {
        override def findWord(text: String): Future[Option[Word]] = Future {Some(word)}
      }
      override val dictionary: Dictionary = new Dictionary
    }

    for(result <- Registry.dictionary.findWord(wordText)) yield assert(result == Some(word))
  }

  test("did not find word at findWord") {
    val wordText = "annotation"
//    val word = Word(1, wordText, 99, "2020/01/01 00:00:00")

    object Registry extends DictionaryComponent with RepositoryComponent {
      override val repository: Repository = new RepositoryMock {
        override def findWord(text: String): Future[Option[Word]] = Future {None}
      }
      override val dictionary: Dictionary = new Dictionary
    }

    for(result <- Registry.dictionary.findWord(wordText)) yield assert(result == None)
  }

  test("set null at findWord") {
    val wordText = "annotation"
    val word = Word(1, wordText, 99, "2020/01/01 00:00:00")

    object Registry extends DictionaryComponent with RepositoryComponent {
      override val repository: Repository = new RepositoryMock {
        override def findWord(text: String): Future[Option[Word]] = Future {Some(word)}
      }
      override val dictionary: Dictionary = new Dictionary
    }

    for(result <- Registry.dictionary.findWord(null)) yield assert(result == None)
  }
}
