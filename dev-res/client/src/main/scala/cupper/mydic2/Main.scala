package cupper.mydic2

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.window
import cupper.mydic2.view.component.{Header, ScreenDetail, ScreenEditExample, ScreenWord, EditWord}
import cupper.mydic2.view.event.{ApplyEditExample, CancelEditExample, EditExample, Event, FindForWord, ApplyEditWord}
import cupper.mydic2.model.{RecordedExamples, RecordedWord}
import cupper.mydic2.ComponentRegistry._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object Main {
  lazy val header = Header(document.getElementById("header").asInstanceOf[dom.html.Element])
  lazy val screenWord = ScreenWord(document.getElementById("screen-word").asInstanceOf[dom.html.Element])
  lazy val screenEditWord = EditWord(document.getElementById("edit-word").asInstanceOf[dom.html.Element])
  lazy val screenDetail = ScreenDetail(document.getElementById("screen-example").asInstanceOf[dom.html.Element])
  lazy val screenEditExample = ScreenEditExample(document.getElementById("screen-edit-example").asInstanceOf[dom.html.Element])

  var recordedWord: Option[RecordedWord] = None
  var recordedExamples: Option[RecordedExamples] = None

  def main(args: Array[String]): Unit = {
    window.onload = (e) => setupUI(e)
  }

  def setupUI(e: dom.Event): Unit = {
    goHome()
    for(info <- dictionary.getInformation()) {
      header.refresh(info.numOfAllWords)
    }

    Event.addEventListener(Event.goHome, (event: dom.CustomEvent) => goHome())
    Event.addEventListener(Event.findForWord, (event: dom.CustomEvent) => findForWord(event))
    Event.addEventListener(Event.editExample, (event: dom.CustomEvent) => editExample(event))
    Event.addEventListener(Event.applyEditExample, (event: dom.CustomEvent) => applyEditExample(event))
    Event.addEventListener(Event.cancelEditExample, (event: dom.CustomEvent) => cancelEditExample(event))

    Event.addEventListener(Event.editWord, (event: dom.CustomEvent) => {
      screenWord.disable()
      screenEditExample.disable()
      screenDetail.disable()
      screenEditWord.show(recordedWord.get.word)
    })

    Event.addEventListener(Event.applyEditWord, (event) => {
      for(res <- recordedWord.get.updateText(event.detail.asInstanceOf[ApplyEditWord].word.text)) {
        recordedWord = res

        screenWord.disable()
        screenEditExample.disable()
        screenDetail.show(recordedWord.get.word, recordedExamples.get.examples)
        screenEditWord.disable()
      }
    })

    Event.addEventListener(Event.cancelEditWord, (event) => {
      screenWord.disable()
      screenEditExample.disable()
      screenDetail.show(recordedWord.get.word, recordedExamples.get.examples)
      screenEditWord.disable()
    })
  }

  def goHome(): Unit = {
    for(list <- dictionary.getWords()) {
      screenWord.show(list)
    }
    screenEditWord.disable()
    screenDetail.disable()
    screenEditExample.disable()
  }

  def findForWord(event: dom.CustomEvent): Unit = {
    val text = event.detail.asInstanceOf[FindForWord]
    for {
      recordedWord <- dictionary.findWord(text.text)
      res <- recordedWord
      recordedExamples <- res.getExamples()
    } {
      this.recordedWord = Some(res)
      this.recordedExamples = Some(recordedExamples)

      screenWord.disable()
      screenEditWord.disable()
      screenEditExample.disable()
      screenDetail.show(res.word, recordedExamples.examples)
    }
  }

  def editExample(event: dom.CustomEvent): Unit = {
    screenWord.disable()
    screenEditWord.disable()
    screenDetail.disable()

    val detail = event.detail.asInstanceOf[EditExample]
    screenEditExample.show(detail)
  }

  def applyEditExample(event: dom.CustomEvent): Unit = {
    val detail = event.detail.asInstanceOf[ApplyEditExample]
    for(res <- recordedExamples.get.updateExample(detail.data)) {
      this.recordedExamples = Some(res)
      screenWord.disable()
      screenEditWord.disable()
      screenEditExample.disable()
      screenDetail.show(recordedWord.get.word, recordedExamples.get.examples.toList)
    }
  }

  def cancelEditExample(event: dom.CustomEvent): Unit = {
    val detail = event.detail.asInstanceOf[CancelEditExample]

    screenWord.disable()
    screenEditWord.disable()
    screenEditExample.disable()
    screenDetail.show(recordedWord.get.word, recordedExamples.get.examples.toList)
  }
}
