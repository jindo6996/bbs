package controllers

import models._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import play.mvc.Results

import scala.util.{Failure, Success, Try}


class PostControllerSpec extends PlaySpecification with Mockito {

//  DBs.setupAll()
  val mockPostDAO: PostDao = mock[PostDao]
  val controller = new PostController(mockPostDAO,stubControllerComponents())

  "PostsController" should {
    "index" in {
      "show all post" in {
        val post = List(Posts(1,"Test"))
        val a = mockPostDAO.all returns post
        val result = controller.listPost.apply(FakeRequest())
        status(result) must equalTo(OK)
        contentAsString(result) must contain("Test")
      }
    }
  }
}