package cupper.mydic2.models

import scala.concurrent.Future

import Values._

trait ExampleRepo {
  def getExamples(word: Word): Future[Seq[Example]]
  def create(wordId: Int, example: String): Boolean
}
