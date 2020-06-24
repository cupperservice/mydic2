package cupper.mydic2.models

import javax.inject.Inject

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import cupper.mydic2.value.Example

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

  def create(wordId: Int, text: String): Option[Example] = {
    val word = Await.result(wordRepo.get(wordId), Duration.Inf)
    word match {
      case Some(word) =>
        exampleRepo.create(word, text)
      case None =>
        None
    }
  }

  def get(wordId: Int, exampleId: Int): Option[Example] = {
    val word = Await.result(wordRepo.get(wordId), Duration.Inf)
    word match {
      case Some(word) =>
        exampleRepo.get(exampleId, word)
      case None =>
        None
    }
  }

  def update(wordId: Int, exampleId: Int, text: String): Option[Example] = {
    val example = get(wordId, exampleId)
    example match {
      case Some(e) =>
        exampleRepo.update(exampleId, text)
      case None =>
        None
    }
  }
}
