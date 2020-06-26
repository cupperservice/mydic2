package cupper.mydic2.component

import cupper.mydic2.component.view.Screen

trait Data {
  val view: Screen

  def show(): Unit = view.show(this)
}
