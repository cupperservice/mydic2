package cupper.mydic2.value

case class NumOfAllWords(num: Int)
case class Word(id: Int, text: String, refCount: Int, lastRefTime: String)
case class Example(id: Int, text: String) {
  override def hashCode(): Int = id.hashCode()

  override def equals(obj: Any): Boolean = obj match {
    case v: Example => v.id == id
    case _ => false
  }
}
