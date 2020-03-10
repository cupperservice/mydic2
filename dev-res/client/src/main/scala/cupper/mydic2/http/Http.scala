package cupper.mydic2.http

import scala.concurrent.{Future, Promise}
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.{Event, XMLHttpRequest}

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import scala.util.Success
import scala.util.Failure

case class AjaxException(xhr: XMLHttpRequest) extends Exception

object Http {
  def get(url: String): Future[XMLHttpRequest] = apply("GET", url)
  def post(url: String): Future[XMLHttpRequest] = apply("POST", url)

  def apply(method: String, url: String, data: Option[String] = None): Future[XMLHttpRequest] = {
    val req = new XMLHttpRequest()
    val promise = Promise[XMLHttpRequest]

    req.withCredentials = true

    req.onreadystatechange = (e: Event) => {
      if(req.readyState.toInt == 4) {
        if(req.status >= 200 && req.status < 300) {
          promise.success(req)
        } else {
          promise.failure(AjaxException(req))
        }
      }
    }

    req.open(method, url, true)

    req.setRequestHeader("content-type", "application/xml")

    data match {
      case Some(d) => req.send(d)
      case None => req.send()
    }

    promise.future
  }
}

@js.native
trait Result extends js.Any {
  val code: Int = js.native
  val message: String = js.native
}

@js.native
trait CreateIfNotExistResponse extends js.Any {
  val id: Int = js.native
  val word: String = js.native
  val ref_count: Int = js.native
  val last_ref_time: String = js.native
}
class CreateIfNotExist(content: String)
object CreateIfNotExist {
  def apply(content: String): Future[CreateIfNotExistResponse] = {
    val headers = Map("content-type" -> "application/json")
    val data = js.JSON.stringify(js.Dictionary("word" -> content))

    Ajax.post("./word", data, 10000, headers).map { res =>
      js.JSON.parse(res.responseText).asInstanceOf[CreateIfNotExistResponse]
    }
  }
}

class GetInformation
object GetInformation {
  def apply(): Future[XMLHttpRequest] = {
    Ajax.get("./word")
  }
}

@js.native
trait GetExampleResponse extends js.Any {
  val id: Int = js.native
  val content: String = js.native
}

class GetExamples(wordId: Int)
object GetExamples {
  def apply(wordId: Int): Future[js.Array[GetExampleResponse]] = {
    val headers = Map("content-type" -> "application/json")
    val data = js.JSON.stringify(js.Dictionary("wordId" -> wordId))

    Ajax.get(s"./word/${wordId}/example", data, 10000, headers).map {res =>
      js.JSON.parse(res.responseText).asInstanceOf[js.Array[GetExampleResponse]]
    }
  }
}

@js.native
trait CreateExampleResponse extends js.Any {
  val result: Result
}

class CreateExample(wordId: Int, content: String)
object CreateExample {
  def apply(wordId: Int, content: String): Future[CreateExampleResponse] = {
    val headers = Map("content-type" -> "application/json")
    val data = js.JSON.stringify((js.Dictionary("content" -> content)))

    Ajax.post(s"./word/${wordId}", data, 10000, headers).map {res =>
      js.JSON.parse(res.responseText).asInstanceOf[CreateExampleResponse]
    }
  }
}
