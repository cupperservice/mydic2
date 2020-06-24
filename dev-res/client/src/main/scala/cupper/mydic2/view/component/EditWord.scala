package cupper.mydic2.view.component

import org.scalajs.dom.document
import org.scalajs.dom.html.Element
import org.scalajs.dom.html.Input

import cupper.mydic2.value.Word
import cupper.mydic2.view.event.Event
import cupper.mydic2.view.event.ApplyEditWord
import cupper.mydic2.view.event.CancelEditWord

class EditWord(val _top: Element) extends Screen(_top) {
  var word: Option[Word] = None
  val text = document.getElementById("edit-word-text").asInstanceOf[Input]

  document.getElementById("edit-word-ok").asInstanceOf[Element].onclick = (event) =>
    Event.dispatch(ApplyEditWord(this.word.get.copy(text = text.value)))

  document.getElementById("edit-word-cancel").asInstanceOf[Element].onclick = (event) =>
    Event.dispatch(CancelEditWord())

  def show(word: Word): Unit = {
    this.word = Some(word)
    text.value = word.text
    super.show()
  }
}

object EditWord {
  def apply(top: Element): EditWord = new EditWord(top)
}
