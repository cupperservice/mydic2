package cupper.mydic2.repository.http

import org.scalajs.dom.ext.Ajax
import cupper.mydic2.value.{Example, NumOfAllWords, Word}
import cupper.mydic2.model.{DictionaryInformation, RecordedExamples, RecordedWord}
import cupper.mydic2.repository.Repository
import cupper.mydic2.value.DateTimeFormatter._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.concurrent.Future
import scala.scalajs.js

trait RepositoryImpl extends Repository {
  override def getInformation(): Future[DictionaryInformation] = {
    for(res <- Ajax.get("/word"))
      yield new DictionaryInformation(NumOfAllWords(res.responseText.toInt))
  }

  override def getWord(text: String): Future[RecordedWord] = {
    def apply(text: String): Future[CreateIfNotExistResponse] = {
      val headers = Map("content-type" -> "application/json")
      val data = js.JSON.stringify(js.Dictionary("word" -> text))

      Ajax.post("./word", data, 10000, headers).map { res =>
        js.JSON.parse(res.responseText).asInstanceOf[CreateIfNotExistResponse]
      }
    }

    implicit val repo: Repository = this
    for(res <- apply(text)) yield {
      RecordedWord(Word(res.id, res.word, res.ref_count, res.last_ref_time))
    }
  }

  override def getExamples(word: Word): Future[RecordedExamples] = {
    def apply(wordId: Int): Future[js.Array[GetExampleResponse]] = {
      val headers = Map("content-type" -> "application/json")
      val data = js.JSON.stringify(js.Dictionary("wordId" -> wordId))

      Ajax.get(s"./word/${wordId}/example", data, 10000, headers).map {res =>
        js.JSON.parse(res.responseText).asInstanceOf[js.Array[GetExampleResponse]]
      }
    }

    implicit val repo: Repository = this
    for(res <- apply(word.id)) yield {
      val ex = res.foldLeft(List[Example]())((list, e) => Example(e.id, e.content) :: list)
      RecordedExamples(ex, word)
    }
  }

  override def createExample(example: Example, word: Word): Future[Example] = {
    def apply(): Future[CreateExampleResponse] = {
      val headers = Map("content-type" -> "application/json")
      val data = js.JSON.stringify(js.Dictionary("text" -> example.text))

      Ajax.post(s"./word/${word.id}/example", data, 10000, headers).map {res =>
        js.JSON.parse(res.responseText).asInstanceOf[CreateExampleResponse]
      }
    }

    implicit val repo: Repository = this
    for(res <- apply()) yield {
      Example(res.id, res.text)
    }
  }

  override def updateExample(example: Example, word: Word): Future[Example] = {
    def apply(): Future[UpdateExampleResponse] = {
      val headers = Map("content-type" -> "application/json")
      val data = js.JSON.stringify(js.Dictionary("text" -> example.text))

      Ajax.put(s"./word/${word.id}/example/${example.id}", data, 10000, headers).map {res =>
        js.JSON.parse(res.responseText).asInstanceOf[UpdateExampleResponse]
      }
    }

    implicit val repo: Repository = this
    for(res <- apply()) yield {
      Example(res.id, res.text)
    }
  }

  override def getWords(): Future[List[Word]] = {
    def apply(): Future[js.Array[GetWordResponse]] = {
      val headers = Map("content-type" -> "application/json")

      Ajax.get(s"./word/all", "", 10000, headers).map {res =>
        js.JSON.parse(res.responseText).asInstanceOf[js.Array[GetWordResponse]]
      }
    }

    implicit val repo: Repository = this
    for(res <- apply()) yield
      res.foldLeft(List[Word]())((list, w) => Word(w.id, w.text, w.ref_count, w.last_ref_time) :: list)
  }
}
