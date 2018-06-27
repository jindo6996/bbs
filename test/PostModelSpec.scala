import models.{PostDao, Posts}
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.test.PlaySpecification
import scalikejdbc._
import scalikejdbc.config._

@RunWith(classOf[JUnitRunner])
class PostSpecTest extends PlaySpecification {
  DBs.setupAll()
  val postModels = PostDao()
  val titleTest="test"
  "Posts Model" should {
    "all" in {
      "return list of all posts" in {
        implicit val session: DBSession = AutoSession
        sql"DELETE FROM BBS_Post".update.apply()
        sql"INSERT INTO BBS_Post(title) VALUES(${titleTest})".update.apply()
        sql"INSERT INTO BBS_Post(title) VALUES(${titleTest})".update.apply()
        sql"INSERT INTO BBS_Post(title) VALUES(${titleTest})".update.apply()
        val posts = postModels.all
        posts.size must beEqualTo(3)
        posts.head.title must beEqualTo("test")
        posts.last.title must beEqualTo("test")
      }
    }
  }
}