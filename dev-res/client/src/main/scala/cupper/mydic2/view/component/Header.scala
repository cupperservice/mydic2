package cupper.mydic2.view.component

import org.scalajs.dom.document
import org.scalajs.dom.html.Element
import cupper.mydic2.utils.DomUtils._
import cupper.mydic2.value.NumOfAllWords
import cupper.mydic2.view.event.GoHome
import cupper.mydic2.view.event.Event

class Header(val element: Element) {
  val wordCount =  document.getElementById("word_count")
  document.getElementById("home").asInstanceOf[Element].onclick =
    (event) => Event.dispatch(GoHome())

  def refresh(numOfWords: NumOfAllWords): Unit = {
    setIntContent(wordCount, numOfWords.num)
  }
}

object Header {
  def apply(element: Element): Header = new Header(element)
}
