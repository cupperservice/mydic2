package cupper.mydic2.model

import cupper.mydic2.repository.Repository
import cupper.mydic2.value.{Example, Word}

import scala.concurrent.Future

abstract class RepositoryMock extends Repository {
  override def getInformation(): Future[DictionaryInformation] = ???
  override def findWord(text: String): Future[Option[Word]] = ???
  override def getExamples(word: Word): Future[List[Example]] = ???
  override def createExample(example: Example, word: Word): Future[Example] = ???
  override def updateExample(example: Example, word: Word): Future[Example] = ???
  override def getWords(): Future[List[Word]] = ???
  override def updateWord(id: Int, text: String): Future[Option[Word]] = ???
}
