package cupper.mydic2.repository

import cupper.mydic2.model.{DictionaryInformation, RecordedExamples, RecordedWord}
import cupper.mydic2.value.Word
import cupper.mydic2.value.Example

import scala.concurrent.Future

trait Repository {
  def getInformation(): Future[DictionaryInformation]
  def getWord(text: String): Future[RecordedWord]
  def getExamples(word: Word): Future[RecordedExamples]
  def createExample(example: Example, word: Word): Future[Example]
  def updateExample(example: Example, word: Word): Future[Example]
  def getWords(): Future[List[Word]]
}
