package models.post

import scalikejdbc.DB
import scalikejdbc._
import javax.inject.Singleton
import scala.util.Try

@Singleton
class PostDao() {
  def getAll: Try[List[Post]] = Try {
    DB readOnly { implicit session =>
      sql"SELECT * FROM posts".map(extract).list.apply()
    }
  }
  def getByID(id: Int): Try[Option[Post]] = Try {
    DB readOnly { implicit session =>
      sql"SELECT * FROM posts WHERE id =$id".map(extract).single().apply()
    }
  }

  private def extract(rs: WrappedResultSet): Post =
    Post(rs.int("id"), rs.string("title"), rs.string("content"), rs.string("mail"))
}
