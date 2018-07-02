package postTest
import controllers.PostController
import models.post._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import scalikejdbc.config.DBs

import scala.util.Success

class PostControllerSpec extends PlaySpecification with Mockito {
  val mockPostDAO: PostDao = mock[PostDao]
  val controller = new PostController(mockPostDAO, stubControllerComponents())
  val post = Post(1, "Test", "Content", "jindo@gmail.com")

  "PostsController" should {
    //--------
    "getAll" in {
      "show all post" in {
        mockPostDAO.getAll returns Success(List(post))
        val result = controller.listPost.apply(FakeRequest())
        status(result) must equalTo(OK)
        contentAsString(result) must contain("Test")

      }
      "get post empty" in {
        val post: List[Post] = List()
        mockPostDAO.getAll returns Success(post)
        val result = controller.listPost.apply(FakeRequest())
        status(result) must equalTo(OK)
        contentAsString(result) must contain("Not have any post")
      }
    }
    //--------------
    "getPostByID" in {
      "found" in {
        mockPostDAO.getByID(1) returns Success(Option(post))
        val result = controller.getPostByID(1).apply(FakeRequest())
        status(result) must equalTo(OK)
        contentAsString(result) must contain("Content")
      }
      "Not found" in {
        val post: Option[Post] = None
        mockPostDAO.getByID(0) returns Success(post)
        val result = controller.getPostByID(0).apply(FakeRequest())
        status(result) must equalTo(OK)
        contentAsString(result) must contain("Post Not Found")
      }
    }
  }
}