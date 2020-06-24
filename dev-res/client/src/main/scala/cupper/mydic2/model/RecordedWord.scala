package cupper.mydic2.model

import cupper.mydic2.value.Word
import cupper.mydic2.repository.Repository

import scala.concurrent.Future
import scalajs.concurrent.JSExecutionContext.Implicits.queue

trait RecordedWord {
  val word: Word
  val repo: Repository

  def getExamples(): Future[RecordedExamples] = {
    implicit val _repo = repo
    for(res <- repo.getExamples(word)) yield RecordedExamples(res, word)
  }

  def updateText(text: String): Future[Option[RecordedWord]] = {
    implicit val _repo = repo
    for(res <- repo.updateWord(word.id, text)) yield {
      res match {
        case Some(word) => Some(RecordedWord(word))
        case None => None
      }
    }
  }
}

object RecordedWord {
  def apply(w: Word)(implicit r: Repository): RecordedWord =
    new RecordedWord {
      override val word: Word = w
      override val repo: Repository = r
    }
}
