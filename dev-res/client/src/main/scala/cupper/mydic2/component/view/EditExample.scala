package cupper.mydic2.component.view

import cupper.mydic2.component.event.Event
import cupper.mydic2.utils.DomUtils._
import cupper.mydic2.value.{Word, Example}
import cupper.mydic2.component.Data
import org.scalajs.dom.document
import org.scalajs.dom.html.{Button, Element, Input, TextArea}

class EditExampleView(_top: Element) extends Screen(_top) {
  document.getElementById("edit-example-create").asInstanceOf[Button].onclick = (event) =>
    Event.dispatch(Event.ApplyEditExample(Example(exampleId.value.toInt, exampleText.value)))

  document.getElementById("edit-example-cancel").asInstanceOf[Button].onclick = (event) =>
    Event.dispatch(Event.CancelEditExample())

  val exampleText = document.getElementById("edit-example-content").asInstanceOf[TextArea]
  val exampleId = document.getElementById("edit-example-id").asInstanceOf[Input]
  val wordText = document.getElementById("edit-example-word").asInstanceOf[Element]

  override def show(data: Data): Unit = {
    val editExample = data.asInstanceOf[EditExampleData]

    exampleText.value = editExample.example.text
    exampleId.value = editExample.example.id.toString
    setTextContent(wordText, editExample.word.text)

    super.show()
  }
}
object EditExampleView {
  def apply(element: Element): EditExampleView = new EditExampleView(element)
}

class EditExampleData(val word: Word, val example: Example, _view: Screen) extends Data {
  override val view: Screen = _view
}
object EditExampleData {
  def apply(word: Word, example: Example, view: Screen) = new EditExampleData(word, example, view)
}
