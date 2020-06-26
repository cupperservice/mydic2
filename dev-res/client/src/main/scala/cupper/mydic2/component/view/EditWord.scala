package cupper.mydic2.component.view

import cupper.mydic2.component.event.Event
import cupper.mydic2.value.Word
import cupper.mydic2.component.Data
import org.scalajs.dom.document
import org.scalajs.dom.html.{Button, Element, Input}

class EditWordView(val _top: Element) extends Screen(_top) {
  val text = document.getElementById("edit-word-text").asInstanceOf[Input]
  val okBtn = document.getElementById("edit-word-ok").asInstanceOf[Button]
  val cancelBtn = document.getElementById("edit-word-cancel").asInstanceOf[Button]

  override def show(data: Data): Unit = {
    val editWord = data.asInstanceOf[EditWordData]
    text.value = editWord.word.text

    okBtn.onclick = (event) =>
      Event.dispatch(Event.ApplyEditWord(editWord.word.copy(text = text.value)))

    cancelBtn.onclick = (event) =>
      Event.dispatch(Event.CancelEditWord())

    super.show()
  }
}
object EditWordView {
  def apply(top: Element): EditWordView = new EditWordView(top)
}

class EditWordData(val word: Word, _view: Screen) extends Data {
  override val view: Screen = _view
}
object EditWordData {
  def apply(word: Word, view: Screen): EditWordData = new EditWordData(word, view)
}
