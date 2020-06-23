package cupper.mydic2.models

import scala.concurrent.Future

import Values._

trait ExampleRepo {
  def getExamples(word: Word): Future[Seq[Example]]
  def create(word: Word, example: String): Option[Example]
  def get(id: Int, word: Word): Option[Example]
  def update(id: Int, text: String, word: Word): Option[Example]
}
