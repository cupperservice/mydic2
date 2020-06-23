package cupper.mydic2.view.component

import org.scalajs.dom.document
import org.scalajs.dom.html.{Button, Element, Input}

import cupper.mydic2.view.event.FindForWord
import cupper.mydic2.view.event.Event
import cupper.mydic2.value.Word
import cupper.mydic2.utils.DomUtils._

class ScreenWord(val element: Element) {
  val textWord = document.getElementById("text-word").asInstanceOf[Input]
  val wordList = document.getElementById("word_list").asInstanceOf[Element]

  val btnCheck = document.getElementById("btn-check").asInstanceOf[Button].onclick = (event) => {
    Event.dispatch(FindForWord(textWord.value))
  }

  def show(words: List[Word]): Unit = {
    clearList(wordList)
    words.foreach(w => {
      addItem(wordList, w.text, "")
    })
    element.style.display = "block"
  }

  def disable(): Unit = element.style.display = "none"
}

object ScreenWord {
  def apply(element: Element): ScreenWord = {
    new ScreenWord(element)
  }
}
