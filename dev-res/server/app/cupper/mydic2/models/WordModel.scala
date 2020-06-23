package cupper.mydic2.models

import javax.inject.Inject
import play.api.libs.json._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class WordModel @Inject() (repo: WordRepo) {
  def getInformation(): Int = {
    Await.result(repo.count(), Duration.Inf)
  }

  def createIfNotExist(word: String): Future[Values.Word] = {
    repo.find(word).map(r => r match {
      case Some(w) =>
        Await.result(repo.updateReference(w.id), Duration.Inf)
        w
      case None =>
        Await.result(repo.create(word), Duration.Inf)
    })
  }

  def getAll(): List[Values.Word] = {
    repo.getAll()
  }
}
