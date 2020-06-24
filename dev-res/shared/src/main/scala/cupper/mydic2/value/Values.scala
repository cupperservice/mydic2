package cupper.mydic2.value

case class NumOfAllWords(num: Int)
case class DictionaryInformation(numOfWords: Int, history: List[Word])
case class Word(id: Int, text: String, refCount: Int, lastRefTime: DateTime)
case class Example(id: Int, text: String)
case class DateTime(time: Long)
