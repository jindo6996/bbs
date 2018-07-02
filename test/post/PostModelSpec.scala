package postTest

import models.post.{ Post, PostDao }
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.test.PlaySpecification
import scalikejdbc._
import scalikejdbc.config._
import setup._

import scala.util.{ Failure, Success }

@RunWith(classOf[JUnitRunner])
class PostSpecTest extends PlaySpecification with DBSetting {
  val conn: java.sql.Connection = ConnectionPool('bbs_test).borrow()
  val postModels: PostDao = new PostDao()
  val titleTest = "test"
  "Posts Model" should {
    "all" in {
      "return list of all posts" in {
        using(ConnectionPool.borrow()) { conn =>
          implicit val session: DBSession = AutoSession
          sql"DELETE FROM posts".update.apply()
          var postsEmpty = postModels.getAll match {
            case Success(posts) => posts
          }
          postsEmpty.size must beEqualTo(0)
          sql"INSERT INTO posts(id,title) VALUES(${1},${"Test"})".update.apply()
          sql"INSERT INTO posts(id,title) VALUES(${2},${"Test"})".update.apply()
          sql"INSERT INTO posts(id,title) VALUES(${3},${"Test"})".update.apply()
          val posts = postModels.getAll match {
            case Success(posts) => posts
          }
          posts.size must beEqualTo(3)
          posts.head.title must beEqualTo("Test")
          posts.last.title must beEqualTo("Test")
        }
      }
    }
    "Get by ID" in {
      "Return post" in {
        using(ConnectionPool.borrow()) { conn =>
          implicit val session: DBSession = AutoSession
          sql"DELETE FROM posts".update.apply()
          var postsEmpty = postModels.getByID(1) match {
            case Success(posts) => posts
          }
          postsEmpty.size must beEqualTo(0)
          sql"INSERT INTO posts(id,title,content,mail) VALUES(${1},${"Test"},${"Test"},${"jindo@gmail.com"})".update.apply()
          sql"INSERT INTO posts(id,title,content,mail) VALUES(${2},${"Test"},${"Test"},${"jindo@gmail.com"})".update.apply()
          sql"INSERT INTO posts(id,title,content,mail) VALUES(${3},${"Test"},${"Test"},${"jindo@gmail.com"})".update.apply()
          val posts = postModels.getAll match {
            case Success(posts) => posts
          }
          posts.size must beEqualTo(3)
          posts.head.content must beEqualTo("Test")
          posts.last.mail must beEqualTo("jindo@gmail.com")
        }
      }
    }
  }
}