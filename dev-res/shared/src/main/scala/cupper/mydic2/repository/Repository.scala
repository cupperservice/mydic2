package cupper.mydic2.repository

import cupper.mydic2.value.Word

trait Repository {
  def countWords(): Int
  def findWord(textWord: String): Option[Word]
}
