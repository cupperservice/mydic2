package cupper.mydic2

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.window
import cupper.mydic2.component.view.{HomeView, Header, EditWordView, DetailView, EditExampleView}
import cupper.mydic2.component.view.{HomeData, DetailData, EditExampleData, EditWordData}
import cupper.mydic2.component.event.Event
import cupper.mydic2.model.{RecordedExamples, RecordedWord}
import cupper.mydic2.ComponentRegistry._
import cupper.mydic2.value.Example

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object Main {
  lazy val header = Header(document.getElementById("header").asInstanceOf[dom.html.Element])
  lazy val homeView = HomeView(document.getElementById("home-tab").asInstanceOf[dom.html.Element])
  lazy val editWordView = EditWordView(document.getElementById("edit-word-tab").asInstanceOf[dom.html.Element])
  lazy val detailView = DetailView(document.getElementById("detail-tab").asInstanceOf[dom.html.Element])
  lazy val editExampleView = EditExampleView(document.getElementById("edit-example-tab").asInstanceOf[dom.html.Element])

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
      val detail = event.detail.asInstanceOf[Event.EditWord]
      homeView.disable()
      editExampleView.disable()
      detailView.disable()

      val data = EditWordData(detail.word, editWordView)
      data.show()
    })

    Event.addEventListener(Event.applyEditWord, (event) => {
      for(res <- recordedWord.get.updateText(event.detail.asInstanceOf[Event.ApplyEditWord].word.text)) {
        recordedWord = res

        val data = DetailData(recordedWord.get.word, recordedExamples.get.examples, detailView)
        data.show()

        homeView.disable()
        editExampleView.disable()
        editWordView.disable()
      }
    })

    Event.addEventListener(Event.cancelEditWord, (event) => {
      val data = DetailData(recordedWord.get.word, recordedExamples.get.examples, detailView)
      data.show()

      homeView.disable()
      editExampleView.disable()
      editWordView.disable()
    })
  }

  def goHome(): Unit = {
    for(histories <- dictionary.getWords()) {
      val home = HomeData(histories, homeView)
      home.show()
    }
    editWordView.disable()
    detailView.disable()
    editExampleView.disable()
  }

  def findForWord(event: dom.CustomEvent): Unit = {
    val text = event.detail.asInstanceOf[Event.FindForWord]
    for {
      recordedWord <- dictionary.findWord(text.text)
      res <- recordedWord
      recordedExamples <- res.getExamples()
    } {
      this.recordedWord = Some(res)
      this.recordedExamples = Some(recordedExamples)

      val data = DetailData(res.word, recordedExamples.examples, detailView)
      data.show()

      homeView.disable()
      editWordView.disable()
      editExampleView.disable()
    }
  }

  def editExample(event: dom.CustomEvent): Unit = {
    homeView.disable()
    editWordView.disable()
    detailView.disable()

    val editExampleData = event.detail match {
      case d: Event.CreateNewExample =>
        EditExampleData(d.word, Example(-1, ""), editExampleView)
      case d: Event.EditExample =>
        EditExampleData(d.word, Example(d.id, d.text), editExampleView)
    }
    editExampleData.show()
  }

  def applyEditExample(event: dom.CustomEvent): Unit = {
    val detail = event.detail.asInstanceOf[Event.ApplyEditExample]
    for(res <- recordedExamples.get.updateExample(detail.data)) {
      this.recordedExamples = Some(res)

      val data = DetailData(res.word, res.examples, detailView)
      data.show()

      homeView.disable()
      editWordView.disable()
      editExampleView.disable()
    }
  }

  def cancelEditExample(event: dom.CustomEvent): Unit = {
    val detail = event.detail.asInstanceOf[Event.CancelEditExample]

    val data = DetailData(recordedWord.get.word, recordedExamples.get.examples, detailView)
    data.show()

    homeView.disable()
    editWordView.disable()
    editExampleView.disable()
  }
}
