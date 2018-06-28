import controllers.PostController
import models.post.{ Post, PostDao }
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import scala.util.{ Success, Failure }

class SystemError extends PlaySpecification with Mockito {
  val mockPostDAO: PostDao = mock[PostDao]
  val controller = new PostController(mockPostDAO, stubControllerComponents())
  val titleTest = "test"
  "Request Client" should {
    "Get fail" in {
      mockPostDAO.getAll returns Failure(new Exception("System error"))
      val result = controller.listPost.apply(FakeRequest())
      status(result) must equalTo(500)
    }
  }
}