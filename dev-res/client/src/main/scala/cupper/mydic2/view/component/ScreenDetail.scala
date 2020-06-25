package cupper.mydic2.view.component

import org.scalajs.dom.{CustomEvent, Node, document}
import org.scalajs.dom.html.Element
import org.scalajs.dom.html.Button
import cupper.mydic2.utils.DomUtils._
import cupper.mydic2.utils.{ListItemId, SpeechSynthesis, SpeechSynthesisUtterance}
import cupper.mydic2.value._
import cupper.mydic2.value.DateTimeFormatter._
import cupper.mydic2.view.event.Event

class ScreenDetail(val element: Element) {
  var word: Option[Word] = None
  val wordText = document.getElementById("screen-example-word")
  val wordId = document.getElementById("screen-example-word-id")
  val refCount = document.getElementById("ref_count")
  val lastRefTime = document.getElementById("last_ref_time")
  val examples = document.getElementById("examples")

  document.getElementById("btn-speak").asInstanceOf[Button].onclick = (event) =>
    SpeechSynthesis.speak(new SpeechSynthesisUtterance(getTextContent(wordText)))

  document.getElementsByName("[0]").item(0).asInstanceOf[Element].onclick = (event) => {
    Event.dispatch(Event.EditExample(0, getTextContent(event.target.asInstanceOf[Element]), this.word.get))
  }

  def show(word: Word, examples: List[Example]): Unit = {
    clearExamples()
    this.word = Some(word)
    setTextContent(this.wordText, word.text)
    setTextContent(this.wordId, word.id.toString)
    setIntContent(this.refCount, word.refCount)
    setTextContent(this.lastRefTime, dateTime2String(word.lastRefTime))

    examples.foreach(example => {
      val el = addItem(this.examples, example.text, s"[${example.id}]").asInstanceOf[Element]
      el.onclick = (event) => {
        val target = event.target.asInstanceOf[Element]

        val id = ListItemId(target.getAttribute("name")).value match {
          case Some(i) => i
          case None => -1
        }
        Event.dispatch(Event.EditExample(id, getTextContent(target), this.word.get))
      }
    })

    element.style.display = "block"
  }

  // dispatch edit word
  wordText.asInstanceOf[Element].onclick = (event) => Event.dispatch(Event.EditWord(this.word.get))

  def disable(): Unit = element.style.display = "none"

  def clearExamples(): Unit = {
    while(examples.childNodes.length > 2) examples.removeChild(examples.lastChild)
  }
}

object ScreenDetail {
  def apply(element: Element): ScreenDetail = new ScreenDetail(element)
}
