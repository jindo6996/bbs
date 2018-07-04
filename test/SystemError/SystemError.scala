package SystemError

import controllers.PostController
import controllers.form.post.PostForm.PostInfo
import models.post.{ Post, PostDao }
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import play.api.test.CSRFTokenHelper._

import scala.util.{ Failure, Success }

class SystemError extends PlaySpecification with Mockito {
  val mockPostDAO: PostDao = mock[PostDao]
  val controller = new PostController(mockPostDAO, stubControllerComponents())
  val titleTest = "test"
  "Server error" should {
    "Get All fail" in {
      mockPostDAO.getAll returns Failure(new Exception("System error"))
      controller.listPost.apply(FakeRequest()) must throwA[Exception]
    }
    "Get By ID fail" in {
      mockPostDAO.getByID(5) returns Failure(new Exception("System error"))
      controller.getPostByID(5).apply(FakeRequest()) must throwA[Exception]
    }
  }
}