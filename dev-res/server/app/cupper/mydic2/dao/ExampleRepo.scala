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
      db.withConnection(connection => {
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
      })
    }(databaseExecutionContext)
  }

  override def create(word: Word, example: String): Option[Example] = {
    db.withConnection { connection => {
      {
        val stmt = connection.prepareStatement("insert into example(content, word_id) values(?,?)")
        stmt.setString(1, example)
        stmt.setInt(2, word.id)
        stmt.executeUpdate()
      }

      {
        val stmt = connection.prepareStatement("select id, content, word_id from example where id = last_insert_id()")
        val rs = stmt.executeQuery()
        if(rs.next())
          Some(Example(rs.getInt(1), rs.getString(2), word))
        else None
      }
    }}
  }

  override def get(id: Int, word: Word): Option[Example] =
    db.withConnection { connection => {
      val stmt = connection.prepareStatement("select id, content, word_id from example where id = ?")
      stmt.setInt(1, id)
      val rs = stmt.executeQuery()
      if(rs.next()) Some(Example(rs.getInt(1), rs.getString(2), word))
      else None
    }
  }

  override def update(id: Int, text: String, word: Word): Option[Example] =
    db.withConnection { connection => {
      val stmt = connection.prepareStatement("update example set content = ? where id = ?")
      stmt.setString(1, text)
      stmt.setInt(2, id)
      if(stmt.executeUpdate() == 1)
        Some(Example(id, text, word))
      else
        None
    }
  }
}
