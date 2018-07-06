package login

import controllers.AuthController
import controllers.exception.EntityNotFoundException
import controllers.form.login.LoginForm.LoginInfo
import models.user.{ User, UserDao }
import org.specs2.mock.Mockito
import play.api.test.Helpers.stubControllerComponents
import play.api.test.{ FakeRequest, PlaySpecification }
import play.api.test.CSRFTokenHelper._
import scalikejdbc.DBSession

import scala.util.{ Failure, Success }

class AuthControllerSpec extends PlaySpecification with Mockito {
  val loginInfo = LoginInfo("test@gmail.com", "123123")
  var user = User(1, "test@gmail.com", "123123")
  "User Model" should {
    "Login" in {
      "Go to page login" in {
        val mockUserDAO: UserDao = mock[UserDao]
        val controller = new AuthController(mockUserDAO, stubControllerComponents())
        val result = controller.login.apply(FakeRequest(GET, "/login").withCSRFToken)
        contentAsString(result) must contain("Login")
      }
    }
    "validate" in {
      "Login sucess redirect Home" in {
        val mockUserDAO: UserDao = mock[UserDao]
        val controller = new AuthController(mockUserDAO, stubControllerComponents())
        mockUserDAO.getUserByUsernamePassword(loginInfo) returns Success(user)
        val result = controller.processLogin.apply(FakeRequest(POST, "/login").withFormUrlEncodedBody("mail" -> "test@gmail.com", "password" -> "123123").withCSRFToken)
        redirectLocation(result) mustEqual (Some("/"))
        session(result).get("mail") mustEqual (Some("test@gmail.com"))
      }
      "Login unsuccess because mail is empty" in {
        val mockUserDAO: UserDao = mock[UserDao]
        val controller = new AuthController(mockUserDAO, stubControllerComponents())
        mockUserDAO.getUserByUsernamePassword(loginInfo) returns Success(user)
        val result = controller.processLogin.apply(FakeRequest(POST, "/login").withFormUrlEncodedBody("mail" -> "", "password" -> "123123").withCSRFToken)
        contentAsString(result) must contain("error.email")
      }
      "Login unsuccess because password is empty" in {
        val mockUserDAO: UserDao = mock[UserDao]
        val controller = new AuthController(mockUserDAO, stubControllerComponents())
        mockUserDAO.getUserByUsernamePassword(loginInfo) returns Success(user)
        val result = controller.processLogin.apply(FakeRequest(POST, "/login").withFormUrlEncodedBody("mail" -> "test@gmail.com", "password" -> "").withCSRFToken)
        contentAsString(result) must contain("error.required")
      }
      "since info wrong, login unsuccess then return user not found" in {
        val mockUserDAO: UserDao = mock[UserDao]
        val controller = new AuthController(mockUserDAO, stubControllerComponents())
        mockUserDAO.getUserByUsernamePassword(loginInfo) returns Failure(EntityNotFoundException("User not found", loginInfo))
        val result = controller.processLogin.apply(FakeRequest(POST, "/login").withFormUrlEncodedBody("mail" -> "test@gmail.com", "password" -> "123123").withCSRFToken)
        contentAsString(result) must contain("User not found")
      }
      "login unsuccess because system error" in {
        val mockUserDAO: UserDao = mock[UserDao]
        val controller = new AuthController(mockUserDAO, stubControllerComponents())
        mockUserDAO.getUserByUsernamePassword(loginInfo) returns Failure(new Exception)
        println(mockUserDAO.getUserByUsernamePassword(loginInfo))
        controller.processLogin.apply(FakeRequest(POST, "/login").withFormUrlEncodedBody("mail" -> "test@mail.com", "password" -> "123123").withCSRFToken) must throwA[Exception]
      }
    }
  }
}
