package cupper.mydic2.utils

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal}

@js.native
trait SpeechSynthesisVoice extends js.Object {
  val voiceURI: String = js.native
}

@js.native
@JSGlobal
class SpeechSynthesisUtterance(var text: String) extends js.Object {
  var voice: SpeechSynthesisVoice = js.native
}

@js.native
@JSGlobal("speechSynthesis")
object SpeechSynthesis extends js.Object {
  def speak(msg: SpeechSynthesisUtterance): js.Any = js.native
  def getVoices(): js.Array[SpeechSynthesisVoice] = js.native
}
