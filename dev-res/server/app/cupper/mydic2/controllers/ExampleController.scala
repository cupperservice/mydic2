package cupper.mydic2.controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global

import cupper.mydic2.http.Messages

@Singleton
class ExampleController @Inject() (
    cc: ControllerComponents,
    example: cupper.mydic2.models.ExampleModel) extends AbstractController(cc) with Messages {

  def get(wordId: Int) = Action.async {implicit request =>
    Future {
      val result = for(e <- example.getExamples(wordId)) yield Json.obj (
        "id" -> e.id,
        "content" -> e.content
      ).toString()

      Ok(result.mkString("[", ",", "]"))
    }
  }

  def create(wordId: Int) = Action.async {implicit  request =>
    Future {
      request.body.asJson match {
        case Some(body) =>
          if(example.create(wordId, (body \ "content").as[JsString].value)) {
            Ok(Json.obj(
              fields = "result" -> Json.obj(
                fields = "code" -> 0,
                "message" -> M000
              )).toString())
          } else {
            Ok(Json.obj(
              fields = "result" -> Json.obj(
                fields = "code" -> 1,
                "message" -> M001
              )
            ))
          }
        case None =>
          BadRequest
      }
    }
  }
}
