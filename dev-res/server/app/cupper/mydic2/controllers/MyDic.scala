package cupper.mydic2.controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

@Singleton
class MyDic2 @Inject()(
    cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index())
  }
}
