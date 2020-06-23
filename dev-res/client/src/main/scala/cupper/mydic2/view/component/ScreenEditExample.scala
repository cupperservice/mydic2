package cupper.mydic2.view.component

import org.scalajs.dom.document
import org.scalajs.dom.html.{Button, Element, Input, TextArea}
import cupper.mydic2.view.event.{ApplyEditExample, EditExample, CancelEditExample}
import cupper.mydic2.utils.DomUtils._
import cupper.mydic2.view.event.Event
import cupper.mydic2.value.Example

class ScreenEditExample(val element: Element) {
  document.getElementById("screen-edit-example-create").asInstanceOf[Button].onclick = (event) =>
    Event.dispatch(ApplyEditExample(Example(id.value.toInt, content.value)))

  document.getElementById("screen-edit-example-cancel").asInstanceOf[Button].onclick = (event) =>
    Event.dispatch(CancelEditExample())

  val content = document.getElementById("screen-edit-example-content").asInstanceOf[TextArea]
  val id = document.getElementById("screen-edit-example-id").asInstanceOf[Input]
  val wordText = document.getElementById("screen-edit-example-word").asInstanceOf[Element]

  def show(example: EditExample): Unit = {
    content.value = example.text
    id.value = example.id.toString
    setTextContent(wordText, example.word.text)

    element.style.display = "block"
  }

  def disable(): Unit = element.style.display = "none"
}

object ScreenEditExample {
  def apply(element: Element): ScreenEditExample = new ScreenEditExample(element)
}
