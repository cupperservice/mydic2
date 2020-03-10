package cupper.mydic2.dao

import java.sql.Connection
import javax.inject.Inject

import play.api.db.Database

import scala.concurrent.Future

import cupper.mydic2.models.{WordRepo => WordRepoIF}
import cupper.mydic2.models.Values.Word
import cupper.mydic2.models.Values.DateTime
import cupper.mydic2.models.Values.DateTimeFormatter._

class WordRepo @Inject() (db: Database, databaseExecutionContext: DatabaseExecutionContext) extends WordRepoIF {
  override def count(): Future[Int] = {
    Future[Int] {
      db.withConnection { conn =>
        val connection = db.getConnection()
        val stmt = connection.prepareStatement("select count(*) from word")
        val rs = stmt.executeQuery()
        rs.next()
        rs.getInt(1)
      }
    }(databaseExecutionContext)
  }

  override def get(id: Int): Future[Option[Word]] = {
    Future[Option[Word]] {
      db.withConnection { conn =>
        val connection = db.getConnection()
        val stmt = connection.prepareStatement("select * from word where id=?")
        stmt.setInt(1, id)
        val rs = stmt.executeQuery()
        if(rs.next()) {
          Some(Word(
            rs.getInt(1),
            rs.getString(2),
            rs.getInt(3),
            string2DateTime(rs.getString(4)))
          )
        } else {
          None
        }
      }
    }(databaseExecutionContext)
  }

  override def find(word: String): Future[Option[Word]] = {
    Future[Option[Word]] {
      db.withConnection { connection =>
        _find(word, connection)
      }
    }(databaseExecutionContext)
  }

  override def create(word: String): Future[Word] = {
    Future[Word] {
      db.withConnection { connection =>
        _insert(word, connection)
        _find(word, connection).get
      }
    }(databaseExecutionContext)
  }


  override def updateReference(id: Int): Future[Int] = {
    Future[Int] {
      db.withConnection { connection =>
        val stmt = connection.prepareStatement("update word set ref_count = ref_count + 1, last_ref_time = ? where id = ?")
        stmt.setString(1, dateTime2String(DateTime(System.currentTimeMillis())))
        stmt.setInt(2, id)
        stmt.executeUpdate()
     }
    }(databaseExecutionContext)
  }

  def _find(word: String, connection: Connection): Option[Word] = {
    val stmt = connection.prepareStatement("select * from word where word=?")
    stmt.setString(1, word)
    val rs = stmt.executeQuery()
    if(rs.next()) {
      Some(Word(
        rs.getInt(1),
        rs.getString(2),
        rs.getInt(3),
        string2DateTime(rs.getString(4)))
      )
    } else {
      None
    }
  }

  def _insert(word: String, connection: Connection): Int = {
    val stmt = connection.prepareStatement("insert into word(word, ref_count, last_ref_time) values(?, ?, ?)")
    stmt.setString(1, word)
    stmt.setInt(2, 1)
    stmt.setString(3, dateTime2String(long2DateTime(System.currentTimeMillis())))
    stmt.executeUpdate()
  }
}
