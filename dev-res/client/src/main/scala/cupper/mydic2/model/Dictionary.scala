package cupper.mydic2.model

import cupper.mydic2.repository.Repository
import cupper.mydic2.value.Word
import cupper.mydic2.value.Example

import scala.concurrent.Future
import scalajs.concurrent.JSExecutionContext.Implicits.queue

trait RepositoryComponent {
  val repository: Repository
}

trait DictionaryComponent {
  this: RepositoryComponent =>
  val dictionary: Dictionary

  class Dictionary {
    def getInformation(): Future[DictionaryInformation] = repository.getInformation()

    def findWord(text: String): Future[Option[RecordedWord]] = Option(text) match {
      case Some(v) => {
        implicit val repo: Repository = repository
        repository.findWord(v).map(word => word match {
          case Some(word) => Some(RecordedWord(word))
          case None => None
        })
      }
      case None => Future(None)
    }

    def getWords(): Future[List[Word]] = repository.getWords()
  }
}
