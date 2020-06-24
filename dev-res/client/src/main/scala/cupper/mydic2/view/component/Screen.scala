package cupper.mydic2.view.component

import org.scalajs.dom.html.Element

abstract class Screen(val top: Element) {
  def show(): Unit = top.style.display = "block"
  def disable(): Unit = top.style.display = "none"
}
