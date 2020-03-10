package cupper.mydic2.dao

import java.sql.ResultSet

import play.api.db.Database
import javax.inject.Inject

import scala.concurrent.Future

import cupper.mydic2.models.Values._
import cupper.mydic2.models.{Values, ExampleRepo => ExampleRepoIF}

class ExampleRepo @Inject() (db: Database, databaseExecutionContext: DatabaseExecutionContext) extends ExampleRepoIF {
  override def getExamples(word: Values.Word): Future[Seq[Example]] = {
    Future[Seq[Example]] {
      val connection = db.getConnection()
      val stmt = connection.prepareStatement("select * from example where word_id=?")
      stmt.setInt(1, word.id)
      val rs = stmt.executeQuery()

      def createResult(rs: ResultSet, result: Seq[Example]): Seq[Example] = {
        if (rs.next()) {
          val example = Example(
            rs.getInt(1),
            rs.getString(2),
            word
          )
          createResult(rs, example +: result)
        } else {
          result
        }
      }

      createResult(rs, Seq.empty[Example])
    }(databaseExecutionContext)
  }

  override def create(wordId: Int, example: String): Boolean = {
    db.withConnection { connection => {
      val stmt = connection.prepareStatement("insert into example(content, word_id) values(?,?)")
      stmt.setString(1, example)
      stmt.setInt(2, wordId)
      stmt.executeUpdate()
      true
    }}
  }
}
