package cupper.mydic2.component.view

import cupper.mydic2.component.Data
import cupper.mydic2.component.event.Event
import cupper.mydic2.utils.DomUtils._
import cupper.mydic2.utils.{ListItemId, SpeechSynthesis, SpeechSynthesisUtterance}
import cupper.mydic2.value.DateTimeFormatter._
import cupper.mydic2.value._
import org.scalajs.dom.document
import org.scalajs.dom.html.{Button, Element}

class DetailView(val _top: Element) extends Screen(_top) {
  val wordText = document.getElementById("detail-word")
  val wordId = document.getElementById("detail-word-id")
  val refCount = document.getElementById("ref_count")
  val lastRefTime = document.getElementById("last_ref_time")
  val examples = document.getElementById("examples")
  val speakWordBtn = document.getElementById("btn-speak").asInstanceOf[Button]

  override def show(data: Data): Unit = {
    val detailData = data.asInstanceOf[DetailData]
    setTextContent(this.wordId, detailData.word.id.toString)
    setTextContent(this.wordText, detailData.word.text)
    setIntContent(this.refCount, detailData.word.refCount)
    setTextContent(this.lastRefTime, dateTime2String(detailData.word.lastRefTime))

    // Speak word
    speakWordBtn.onclick = (event) =>
      SpeechSynthesis.speak(new SpeechSynthesisUtterance(detailData.word.text))

    // Edit word
    wordText.asInstanceOf[Element].onclick = (event) =>
      Event.dispatch(Event.EditWord(detailData.word))

    // Clear example list
    while(examples.childNodes.length > 2) examples.removeChild(examples.lastChild)

    // TODO タイトルはリストの先頭ではなく独立して定義させる
    document.getElementsByName("[0]").item(0).asInstanceOf[Element].onclick = (event) =>
      Event.dispatch(Event.EditExample(0, getTextContent(event.target.asInstanceOf[Element]), detailData.word))

    detailData.examples.foreach(example => {
      val el = addItem(this.examples, example.text, s"[${example.id}]").asInstanceOf[Element]
      el.onclick = (event) => {
        val target = event.target.asInstanceOf[Element]

        val id = ListItemId(target.getAttribute("name")).value match {
          case Some(i) => i
          case None => -1
        }
        Event.dispatch(Event.EditExample(id, getTextContent(target), detailData.word))
      }
    })

    super.show()
  }
}
object DetailView {
  def apply(element: Element): DetailView = new DetailView(element)
}

class DetailData(val word: Word, val examples: List[Example], _view: Screen) extends Data {
  override val view: Screen = _view
}
object DetailData {
  def apply(word: Word, examples: List[Example], view: Screen): DetailData = new DetailData(word, examples, view)
}
