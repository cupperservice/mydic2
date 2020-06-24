package cupper.mydic2.repository

import cupper.mydic2.model.{DictionaryInformation}
import cupper.mydic2.value.Word
import cupper.mydic2.value.Example

import scala.concurrent.Future

trait Repository {
  def getInformation(): Future[DictionaryInformation]
  def findWord(text: String): Future[Option[Word]]
  def getExamples(word: Word): Future[List[Example]]
  def createExample(example: Example, word: Word): Future[Example]
  def updateExample(example: Example, word: Word): Future[Example]
  def getWords(): Future[List[Word]]
  def updateWord(id: Int, text: String): Future[Option[Word]]
}
