package cupper.mydic2.models

import javax.inject.Inject

import Values._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ExampleModel @Inject() (exampleRepo: ExampleRepo, wordRepo: WordRepo) {
  def getExamples(wordId: Int): Seq[Example] = {
    val word = Await.result(wordRepo.get(wordId), Duration.Inf)
    word match {
      case Some(word) =>
        Await.result(exampleRepo.getExamples(word), Duration.Inf)
      case None =>
        Seq.empty[Example]
    }
  }

  def create(wordId: Int, content: String): Boolean = {
    exampleRepo.create(wordId, content)
  }
}
