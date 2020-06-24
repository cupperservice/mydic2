package cupper.mydic2.repository.http

import scala.scalajs.js

@js.native
trait Result extends js.Any {
  val code: Int = js.native
  val message: String = js.native
}

@js.native
trait CreateIfNotExistResponse extends js.Any {
  val id: Int = js.native
  val text: String = js.native
  val ref_count: Int = js.native
  val last_ref_time: String = js.native
}

@js.native
trait GetWordResponse extends js.Any {
  val id: Int = js.native
  val text: String = js.native
  val ref_count: Int = js.native
  val last_ref_time: String = js.native
}

@js.native
trait UpdateWordResponse extends js.Any {
  val id: Int = js.native
  val text: String = js.native
  val ref_count: Int = js.native
  val last_ref_time: String = js.native
}

@js.native
trait GetExampleResponse extends js.Any {
  val id: Int = js.native
  val text: String = js.native
}

@js.native
trait CreateExampleResponse extends js.Any {
  val result: Result
  val id: Int = js.native
  val text: String = js.native
}

@js.native
trait UpdateExampleResponse extends js.Any {
  val result: Result
  val id: Int = js.native
  val text: String = js.native
}
