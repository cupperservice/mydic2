package cupper.mydic2.model

import cupper.mydic2.repository.Repository
import cupper.mydic2.value.Word
import scala.concurrent.Future

trait Dictionary {
  val repo: Repository

  def getInformation(): Future[DictionaryInformation] = repo.getInformation()
  def getWord(text: String): Future[RecordedWord] = repo.getWord(text)
  def getWords(): Future[List[Word]] = repo.getWords()
}
