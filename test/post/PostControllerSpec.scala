package controllers

import models.post._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import scalikejdbc.config.DBs

import scala.util.Success

class PostControllerSpec extends PlaySpecification with Mockito {
  val mockPostDAO: PostDao = mock[PostDao]
  val controller = new PostController(mockPostDAO, stubControllerComponents())

  "PostsController" should {
    "getAll" in {
      "show all post" in {
        val post = List(Post(1, "Test"))
        mockPostDAO.getAll returns Success(post)
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
  }
}