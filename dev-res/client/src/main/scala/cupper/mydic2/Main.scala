package cupper.mydic2

import org.scalajs.dom
import org.scalajs.dom.ext._
import org.scalajs.dom.document
import org.scalajs.dom.window

import scala.scalajs.js.Dynamic.global
import scala.scalajs.js
import cupper.mydic2.domutils.DomUtils
import org.scalajs.dom.raw.UIEvent

case class Point[T](val x: T, val y: T)

@js.native
trait Resultx extends js.Any {
  val text: String = js.native
  val version: String = js.native
}

object Main extends DomUtils {
  def main(args: Array[String]): Unit = {
    window.onload = (e) => setupUI(e)
  }

  lazy val result: dom.html.Input = document.getElementById("result").asInstanceOf[dom.html.Input]
  lazy val refCount = document.getElementById("ref_count")
  lazy val lastRefTime = document.getElementById("last_ref_time")
  lazy val examples = document.getElementById("examples")
  lazy val newExample = document.getElementById("new-example").asInstanceOf[dom.html.Button]
  lazy val createExampleDialog = document.getElementById("create-example-dialog").asInstanceOf[dom.html.Div]
  lazy val createExampleBtn = document.getElementById("create-example-btn").asInstanceOf[dom.html.Button]

  def setupUI(e: dom.Event): Unit = {
    setCanvas()

    val checkBtn = document.getElementById("check-button").asInstanceOf[dom.html.Button]
    val registedWordCount = document.getElementById("word_count")
    getInformation(registedWordCount)

    checkBtn.onclick = (event) => {
      import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
      import cupper.mydic2.http.CreateIfNotExist

      for {
        res <- CreateIfNotExist(result.value)
      } {
        setTextContent(refCount, res.ref_count.toString)
        setTextContent(lastRefTime, res.last_ref_time)
        val event = document.createEvent("UIEvent")
        event.asInstanceOf[UIEvent].initUIEvent("modified", false, true, window, res.id)
        document.dispatchEvent(event)
      }
    }

    newExample.onclick = (event) => {
      val myXs = document.body.scrollLeft
      val myYs = document.body.scrollTop

      createExampleDialog.style.left = "500"
      createExampleDialog.style.top = "500"
      createExampleDialog.style.visibility = "visible"
    }

    createExampleBtn.onclick = (event) => {
      createExampleDialog.style.visibility = "hidden"
    }

    document.addEventListener("modified", (event: dom.Event) => {
      import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
      import cupper.mydic2.http.GetExamples

      for(res <- GetExamples(event.asInstanceOf[UIEvent].detail)) {
        clearList(examples)
        for(item <- res) {
          addItem(examples, item.content)
        }
      }
    })
  }

  def getInformation(node: dom.Node): Unit = {
    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
    import cupper.mydic2.http.GetInformation

    for {
      res <- GetInformation()
    } setTextContent(node, res.responseText)
  }

  def recognize(image: dom.raw.ImageData): Unit = {
    val x = global.Tesseract.recognize(
      image, js.Dynamic.literal(lang = "eng"), js.Dynamic.literal(logger = (m: js.Any) => dom.console.log(m)))

    x.then((r: js.Any) => {
      dom.console.log(r)
      val rr = r.asInstanceOf[Resultx]
      dom.console.log(rr.text)
      result.value = rr.text
    }, (e: js.Any) => {
      dom.console.log("error")
      dom.console.log(e)
    })
  }

  def setCanvas(): Unit = {
    val canvas = document.getElementById("canvas").asInstanceOf[dom.html.Canvas]
    val rect = canvas.getBoundingClientRect()
    val context = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    canvas.width = canvas.parentElement.clientWidth
    canvas.height = canvas.parentElement.clientHeight

    var start: Point[Double] = Point(0, 0)
    var touchstart: Boolean = false

    def drawLine(start: Point[Double], end: Point[Double]): Unit = {
      context.beginPath()
      context.lineWidth = 3
      context.moveTo(start.x - rect.left, start.y - rect.top)
      context.lineTo(end.x - rect.left, end.y - rect.top)
      context.stroke()
    }


    def point(e: dom.TouchEvent): Option[Point[Double]] = {
      val touches = e.changedTouches
      if(touches.length == 1) {
        val touch = touches.head
        Some(Point(touch.clientX, touch.clientY))
      } else {
        None
      }
    }

    canvas.addEventListener("touchstart", (event: dom.Event) => {
      Recognize.cancel()
      event.preventDefault()
      start = point(event.asInstanceOf[dom.TouchEvent]).get
      touchstart = true
    })

    canvas.addEventListener("touchmove", (event: dom.Event) => {
      if(touchstart) {
        event.preventDefault()
        val end = point(event.asInstanceOf[dom.TouchEvent]).get
        drawLine(start, end)
        start = end
      }
    })

    canvas.addEventListener("touchend", (event: dom.Event) => {
      touchstart = false
      Recognize.start(context.getImageData(0, 0, canvas.width, canvas.height), recognize)
    })

    canvas.onmousedown = (event) => {
      Recognize.cancel()
      start = Point(event.clientX, event.clientY)
      touchstart = true
    }

    canvas.onmousemove = (event) => {
      if(touchstart) {
        val end = Point(event.clientX, event.clientY)
        drawLine(start, end)
        start = end
      }
    }

    canvas.onmouseup = (event) => {
      touchstart = false
      Recognize.start(context.getImageData(0, 0, canvas.width, canvas.height), recognize)
    }
  }
}

object Recognize {
  val timer = new java.util.Timer()
  var task: RecognizeTask = _

  def start(img: dom.raw.ImageData, fn: (dom.raw.ImageData) => Unit): Unit = {
    if(task != null) {
      task.cancel()
    }
    task = new RecognizeTask(img, fn)
    timer.schedule(task , 2000)
  }

  def cancel(): Unit = {
    if(task != null) {
      task.cancel()
    }
  }
}

class RecognizeTask(val img: dom.raw.ImageData, val fn: (dom.raw.ImageData) => Unit) extends java.util.TimerTask {
  override def run(): Unit = {
    dom.console.log("waiting...")
    fn(img)
  }
}
