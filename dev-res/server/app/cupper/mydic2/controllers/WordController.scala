package cupper.mydic2.controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import cupper.mydic2.models.Values.DateTimeFormatter._

@Singleton
class WordController @Inject()(
    cc: ControllerComponents, usecase: cupper.mydic2.models.WordModel) extends AbstractController(cc) {

  def getInformation = Action.async { implicit request =>
    Future {
      Ok(usecase.getInformation().toString)
    }
  }

  def createIfNotExist = Action.async { implicit request =>
    request.body.asJson match {
      case Some(w) =>
        usecase.createIfNotExist((w \ "word").as[JsString].value).map(w => {
          val json = Json.obj(
            "id" -> w.id,
            "word" -> w.word,
            "ref_count" -> w.ref_count,
            "last_ref_time" -> dateTime2String(w.last_ref_time)
          )
          Ok(json.toString())
        })
      case None =>
        Future(BadRequest("aho"))
    }
  }

  def createIfNotExist2 = Action { implicit request =>
    case class Person(firstName: String, lastName: String, age: Int)

    implicit val personWrites = new Writes[Person] {
      def writes(person: Person) = Json.obj(
        "firstName" -> person.firstName,
        "lastName"  -> person.lastName,
        "age"       -> person.age
      )
    }

    implicit val personReads: Reads[Person] = (
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "age").read[Int]
    )(Person.apply _)

    request.body.asJson.flatMap(json => json.validate[Person].fold(
      valid = { res => Some(res) },
      invalid = { e => None }
    )) match {
      case Some(p) =>
        Ok(Json.toJson(p))
      case None =>
        BadRequest
    }

    // val person = Person("kazu", "kawa", 99)
    // val json = Json.toJson(person)
    // Ok(json)
  }
}
