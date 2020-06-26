package cupper.mydic2.component.view

import cupper.mydic2.component.event.Event
import cupper.mydic2.utils.DomUtils._
import cupper.mydic2.value.NumOfAllWords
import org.scalajs.dom.document
import org.scalajs.dom.html.Element

class Header(val element: Element) {
  val wordCount =  document.getElementById("word_count")
  document.getElementById("home").asInstanceOf[Element].onclick =
    (event) => Event.dispatch(Event.GoHome())

  def refresh(numOfWords: NumOfAllWords): Unit = {
    setIntContent(wordCount, numOfWords.num)
  }
}

object Header {
  def apply(element: Element): Header = new Header(element)
}
