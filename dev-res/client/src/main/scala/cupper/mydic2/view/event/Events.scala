package cupper.mydic2.view.event

import org.scalajs.dom
import org.scalajs.dom.raw.{CustomEvent, CustomEventInit}

import scala.scalajs.js

import cupper.mydic2.value.Word
import cupper.mydic2.value.Example

sealed trait Event
case class GoHome() extends Event
case class FindForWord(text: String) extends Event
case class EditExample(id: Int, text: String, word: Word) extends Event
case class ApplyEditExample(data: Example) extends Event
case class CancelEditExample() extends Event

object Event {
  val goHome = "goHome"
  val findForWord = "findForWord"
  val editExample = "editExample"
  val applyEditExample = "applyEditExample"
  val cancelEditExample = "cancelEditExample"

  def dispatch(event: Event): Unit = {
    val eventType = event match {
      case e: GoHome => goHome
      case e: FindForWord => findForWord
      case e: EditExample => editExample
      case e: ApplyEditExample => applyEditExample
      case e: CancelEditExample => cancelEditExample
    }

    dom.document.dispatchEvent(new CustomEvent(eventType, new CustomEventInit {
      val detail: js.UndefOr[Event] = event
    }))
  }

  def addEventListener(eventType: String, listener: dom.CustomEvent => Unit): Unit =
    dom.document.addEventListener(eventType, listener)
}
