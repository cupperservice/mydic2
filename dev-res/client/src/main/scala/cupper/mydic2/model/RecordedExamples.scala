package cupper.mydic2.model

import cupper.mydic2.value.Word
import cupper.mydic2.value.Example
import cupper.mydic2.repository.Repository

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

trait RecordedExamples {
  val word: Word
  val examples: List[Example]
  val repo: Repository

  def updateExample(example: Example): Future[RecordedExamples] = {
    val f = examples.find(e => e == example) match {
      case Some(e) =>
        repo.updateExample(example, word)
      case None =>
        repo.createExample(example, word)
    }

    for(e <- f) yield {
      val _word = this.word
      val _examples = e :: this.examples.filter(x => x.id != e.id)
      val _repo = this.repo

      new RecordedExamples {
        override val word: Word = _word
        override val examples: List[Example] = _examples
        override val repo: Repository = _repo
      }
    }
  }
}

object RecordedExamples {
  def apply(_examples: List[Example], _word: Word)(implicit _repo: Repository): RecordedExamples = {
    new RecordedExamples {
      override val word: Word = _word
      override val examples: List[Example] = _examples
      override val repo: Repository = _repo
    }
  }
}