package cupper.mydic2.component.view

import cupper.mydic2.component.Data
import cupper.mydic2.component.event.Event
import cupper.mydic2.utils.DomUtils._
import cupper.mydic2.utils.ListItemId
import cupper.mydic2.value.Word
import org.scalajs.dom.document
import org.scalajs.dom.html.{Button, Element, Input}

class HomeView(val _top: Element) extends Screen(_top) {
  val textWord = document.getElementById("text-word").asInstanceOf[Input]
  val wordList = document.getElementById("word_list").asInstanceOf[Element]

  val btnCheck = document.getElementById("btn-check").asInstanceOf[Button].onclick = (event) =>
    Event.dispatch(Event.FindForWord(textWord.value))

  override def show(data: Data): Unit = {
    val homeData = data.asInstanceOf[HomeData]
    clearList(wordList)
    homeData.histories.foreach(w => {
      addItem(wordList, w.text, s"[${w.id}]").asInstanceOf[Element].onclick = (event) => {
        val wordId = ListItemId(event.target.asInstanceOf[Element].getAttribute("name")).value match {
          case Some(id) => id
          case None => -1
        }
        Event.dispatch(Event.FindForWord(homeData.histories.find(w => w.id == wordId).get.text))
      }
    })
    super.show()
  }
}
object HomeView {
  def apply(element: Element): HomeView = new HomeView(element)
}

class HomeData(val histories: List[Word], _view: Screen) extends Data {
  override val view: Screen = _view
}
object HomeData {
  def apply(histories: List[Word], view: Screen): HomeData = new HomeData(histories, view)
}
