package cupper.mydic2.component.view

import org.scalajs.dom.html.Element
import cupper.mydic2.component.Data

abstract class Screen(val top: Element) {
  def show(): Unit = top.style.display = "block"
  def disable(): Unit = top.style.display = "none"

  def show(data: Data): Unit = {

  }
}
