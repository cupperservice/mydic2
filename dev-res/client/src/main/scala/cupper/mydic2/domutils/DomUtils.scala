package cupper.mydic2.domutils

import org.scalajs.dom

trait DomUtils {
  def setTextContent(node: dom.Node, content: String): Unit = {
    val textNode = if(node.childNodes.length == 0) {
      node.appendChild(dom.document.createTextNode(""))
    } else {
      node.childNodes.item(0)
    }
    textNode.textContent = content
  }

  def addItem(node: dom.Node, content: String): Unit = {
    val item = dom.document.createElement("li")
    setTextContent(item, content)
    node.appendChild(item)
  }

  def clearList(node: dom.Node): Unit = {
    while(node.hasChildNodes()) node.removeChild(node.firstChild)
  }
}
