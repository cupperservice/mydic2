package cupper.mydic2.utils

import org.scalajs.dom
import cupper.mydic2.component.event.Event
import org.scalajs.dom.raw.{CustomEvent, CustomEventInit}

import scalajs.js

object DomUtils {
  def setTextContent(node: dom.Node, content: String): Unit = {
    val textNode = if(node.childNodes.length == 0) {
      node.appendChild(dom.document.createTextNode(""))
    } else {
      node.childNodes.item(0)
    }
    textNode.textContent = content
  }

  def setIntContent(node: dom.Node, content: Int): Unit = setTextContent(node, content.toString)

  def getTextContent(node: dom.Node): String = {
    node.childNodes.item(0).textContent
  }

  def addItem(node: dom.Node, content: String, name: String): dom.Node = {
    val item = dom.document.createElement("li")
    item.setAttribute("name", name)
    setTextContent(item, content)
    node.appendChild(item)
  }

  def clearList(node: dom.Node): Unit = {
    while(node.hasChildNodes()) node.removeChild(node.firstChild)
  }
}

case class ListItemId(id: String) {
  val p = "\\[([0-9]+)\\]".r
  lazy val value = id match {
    case p(v) => Some(v.toInt)
    case _ => None
  }
}
