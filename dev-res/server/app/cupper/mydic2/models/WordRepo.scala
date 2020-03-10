package cupper.mydic2.models

import scala.concurrent.Future

import Values._

trait WordRepo {
  def count(): Future[Int]
  def get(id: Int): Future[Option[Word]]
  def create(word: String): Future[Word]
  def find(word: String): Future[Option[Word]]
  def updateReference(id: Int): Future[Int]
}
