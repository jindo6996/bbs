package models.post

import controllers.post.PostForm.PostInfo
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
  def createPost(postResult: PostInfo)(implicit session: DBSession = AutoSession): Try[Long] = Try {
    sql"insert into posts(title,content,mail) values (${postResult.title}, ${postResult.content}, ${postResult.email})".updateAndReturnGeneratedKey().apply()
  }
  private def extract(rs: WrappedResultSet): Post =
    Post(rs.int("id"), rs.string("title"), rs.string("content"), rs.string("mail"))
}
