import models.post.{ Post, PostDao }
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.test.PlaySpecification
import scalikejdbc._
import scalikejdbc.config._

import scala.util.{ Failure, Success }

@RunWith(classOf[JUnitRunner])
class PostSpecTest extends PlaySpecification {
  DBs.setup()
  val postModels: PostDao = new PostDao()
  val titleTest = "test"
  "Posts Model" should {
    "all" in {
      "return list of all posts" in {
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
}