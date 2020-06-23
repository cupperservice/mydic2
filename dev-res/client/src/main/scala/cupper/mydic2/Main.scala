package cupper.mydic2

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.window
import cupper.mydic2.view.component.{Header, ScreenDetail, ScreenEditExample, ScreenWord}
import cupper.mydic2.view.event.{ApplyEditExample, CancelEditExample, EditExample, Event, FindForWord}
import cupper.mydic2.model.{Dictionary, RecordedExamples, RecordedWord}
import cupper.mydic2.repository.Repository
import cupper.mydic2.repository.http.RepositoryImpl
import cupper.mydic2.value.Example
import cupper.mydic2.value.Word

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object Main {
  lazy val header = Header(document.getElementById("header").asInstanceOf[dom.html.Element])
  lazy val screenWord = ScreenWord(document.getElementById("screen-word").asInstanceOf[dom.html.Element])
  lazy val screenDetail = ScreenDetail(document.getElementById("screen-example").asInstanceOf[dom.html.Element])
  lazy val screenEditExample = ScreenEditExample(document.getElementById("screen-edit-example").asInstanceOf[dom.html.Element])

  val dic = new Dictionary {
    override val repo: Repository = new RepositoryImpl {}
  }

  var recordedWord: Option[RecordedWord] = None
  var recordedExamples: Option[RecordedExamples] = None

  def main(args: Array[String]): Unit = {
    window.onload = (e) => setupUI(e)
  }

  def setupUI(e: dom.Event): Unit = {
    goHome()
    for(info <- dic.getInformation()) {
      header.refresh(info.numOfAllWords)
    }

    Event.addEventListener(Event.goHome, (event: dom.CustomEvent) => goHome())
    Event.addEventListener(Event.findForWord, (event: dom.CustomEvent) => findForWord(event))
    Event.addEventListener(Event.editExample, (event: dom.CustomEvent) => editExample(event))
    Event.addEventListener(Event.applyEditExample, (event: dom.CustomEvent) => applyEditExample(event))
    Event.addEventListener(Event.cancelEditExample, (event: dom.CustomEvent) => cancelEditExample(event))
  }

  def goHome(): Unit = {
    for(list <- dic.getWords()) {
      window.console.log("==================")
      screenWord.show(list)
    }
    screenDetail.disable()
    screenEditExample.disable()
  }

  def findForWord(event: dom.CustomEvent): Unit = {
    val text = event.detail.asInstanceOf[FindForWord]
    for {
      recordedWord <- dic.getWord(text.text)
      recordedExamples <- recordedWord.getExamples()
    } {
      this.recordedWord = Some(recordedWord)
      this.recordedExamples = Some(recordedExamples)

      screenWord.disable()
      screenEditExample.disable()
      screenDetail.show(recordedWord.word, recordedExamples.examples.toList)
    }
  }

  def editExample(event: dom.CustomEvent): Unit = {
    screenWord.disable()
    screenDetail.disable()

    val detail = event.detail.asInstanceOf[EditExample]
    screenEditExample.show(detail)
  }

  def applyEditExample(event: dom.CustomEvent): Unit = {
    val detail = event.detail.asInstanceOf[ApplyEditExample]
    for(res <- recordedExamples.get.updateExample(detail.data)) {
      this.recordedExamples = Some(res)
      screenWord.disable()
      screenEditExample.disable()
      screenDetail.show(recordedWord.get.word, recordedExamples.get.examples.toList)
    }
  }

  def cancelEditExample(event: dom.CustomEvent): Unit = {
    val detail = event.detail.asInstanceOf[CancelEditExample]

    screenWord.disable()
    screenEditExample.disable()
    screenDetail.show(recordedWord.get.word, recordedExamples.get.examples.toList)
  }
}
