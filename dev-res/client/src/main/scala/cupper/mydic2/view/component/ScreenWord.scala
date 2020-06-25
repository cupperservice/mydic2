package cupper.mydic2.view.component

import org.scalajs.dom.document
import org.scalajs.dom.html.{Button, Element, Input}
import cupper.mydic2.view.event.Event
import cupper.mydic2.value.Word
import cupper.mydic2.utils.DomUtils._
import cupper.mydic2.utils.ListItemId

class ScreenWord(val _top: Element) extends Screen(_top) {
  var histories = List[Word]()
  val textWord = document.getElementById("text-word").asInstanceOf[Input]
  val wordList = document.getElementById("word_list").asInstanceOf[Element]

  val btnCheck = document.getElementById("btn-check").asInstanceOf[Button].onclick = (event) =>
    Event.dispatch(Event.FindForWord(textWord.value))

  def show(words: List[Word]): Unit = {
    histories = words
    clearList(wordList)
    words.foreach(w => {
      addItem(wordList, w.text, s"[${w.id}]").asInstanceOf[Element].onclick = (event) => {
        val wordId = ListItemId(event.target.asInstanceOf[Element].getAttribute("name")).value match {
          case Some(id) => id
          case None => -1
        }
        org.scalajs.dom.window.console.log(s"+++++++ ${wordId}")
        Event.dispatch(Event.EditWord(histories.find(w => w.id == wordId).get))
      }
    })
    super.show()
  }
}

object ScreenWord {
  def apply(element: Element): ScreenWord = {
    new ScreenWord(element)
  }
}
