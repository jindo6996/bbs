package postTest
import controllers.PostController
import controllers.form.post.PostForm.PostInfo
import models.post._
import play.api.test._
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import scalikejdbc.config.DBs

import scala.util.{ Failure, Success }
import play.api.test.CSRFTokenHelper._
import scalikejdbc.DBSession

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
    //----------------
    "addPost" in {
      "Go to Add post page" in {
        val result = controller.addPost().apply(FakeRequest(GET, "/create/post").withCSRFToken)
        status(result) must equalTo(OK)
        contentAsString(result) must contain("Post Submission")
      }
      "Save post success then show this post " in {
        val post1 = Post(5, "Post Success", "have all requirement", "test@gmail.com")
        mockPostDAO.insert(any[PostInfo])(any[DBSession]) returns Success(5)
        mockPostDAO.getByID(5) returns Success(Option(post1))
        val result = controller.savePost().apply(FakeRequest(POST, "/posts/store").withFormUrlEncodedBody("title" -> "Post Success", "content" -> "have all requirement", "email" -> "test@gmail.com").withCSRFToken)
        status(result) must equalTo(200)
      }
      "Unsuccess: email error" in {
        val result = controller.savePost().apply(FakeRequest(POST, "/posts/store").withFormUrlEncodedBody("title" -> "Post Success", "content" -> "have all requirement", "email" -> "taaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaestgmail.com").withCSRFToken)
        contentAsString(result) must contain("error.email")
      }
      "Unsuccess: Title is null" in {
        val result = controller.savePost().apply(FakeRequest(POST, "/posts/store").withFormUrlEncodedBody("title" -> "", "content" -> "have all requirement", "email" -> "test@gmail.com").withCSRFToken)
        contentAsString(result) must contain("error.required")
      }
      "Create Post fail" in {
        val postInfo = PostInfo("test", "test", "t@gmail.com")
        mockPostDAO.insert(postInfo) returns Failure(new Exception)
        val result = controller.savePost().apply(FakeRequest().withCSRFToken)
        status(result) mustEqual (400)
      }
      // user story 5
      "go to add page with success with email filled if already logged in" in {
        val result = controller.addPost().apply(FakeRequest(GET, "/posts/add").withSession("mail" -> "test@gmail.com").withCSRFToken)
        status(result) mustEqual (200)
        contentAsString(result) must contain("test@gmail.com")
      }
    }
    "savePost when logged in" in {
      "when request's mail not match user'mail (user is logging), save with user'mail not request's mail" in {
        val post1 = Post(5, "Post Success", "have all requirement", "test@gmail.com")
        val postInfo = PostInfo("Post Success", "have all requirement", "test@gmail.com")
        mockPostDAO.insert(postInfo) returns Success(5)
        mockPostDAO.getByID(5) returns Success(Option(post1))
        val result = controller.savePost().apply(FakeRequest(POST, "/posts/store").withFormUrlEncodedBody(
          "title" -> "Post Success",
          "content" -> "have all requirement",
          "email" -> "testmailfail@gmail.com"
        ).withSession("mail" -> "test@gmail.com").withCSRFToken)
        contentAsString(result) must not contain ("testmailfail@gmail.com")
        contentAsString(result) must contain("test@gmail.com")
      }

      "Unsuccess by system error" in {
        val postInfo = PostInfo("Post Success", "have all requirement", "test@gmail.com")
        mockPostDAO.insert(postInfo) returns Failure(new Exception)
        val result = controller.savePost().apply(FakeRequest(POST, "/posts/store").withFormUrlEncodedBody(
          "title" -> "Post Success",
          "content" -> "have all requirement",
          "email" -> "test@gmail.com"
        ).withSession("mail" -> "test@gmail.com").withCSRFToken)
        status(result) mustEqual (500)
      }
    }

  }
}