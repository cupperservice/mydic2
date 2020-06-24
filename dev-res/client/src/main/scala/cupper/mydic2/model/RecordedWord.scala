package cupper.mydic2.model

import cupper.mydic2.value.Word
import cupper.mydic2.repository.Repository

import scala.concurrent.Future

trait RecordedWord {
  val word: Word
  val repo: Repository

  def getExamples(): Future[RecordedExamples] = {
    repo.getExamples(word)
  }
}

object RecordedWord {
  def apply(w: Word)(implicit r: Repository): RecordedWord =
    new RecordedWord {
      override val word: Word = w
      override val repo: Repository = r
    }
}
