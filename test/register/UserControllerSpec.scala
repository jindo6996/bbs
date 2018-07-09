package register

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import controllers.form.user.RegisterForm.RegisterInfo
import controllers.{ UserController, routes }
import models.user.UserDao
import org.specs2.mock.Mockito
import play.api.test.{ FakeRequest, PlaySpecification }
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._

import scala.util.{ Failure, Success }

class UserControllerSpec extends PlaySpecification with Mockito {
  val mockUserDao: UserDao = mock[UserDao]
  val controller = new UserController(mockUserDao, stubControllerComponents())
  val mail = "test@gmail.com"
  "User controller" should {
    "addUser" in {
      "Go to Home if logged in" in {
        val result = controller.addUser().apply(FakeRequest(GET, "/users/register").withSession("mail" -> mail).withCSRFToken)
        status(result) mustEqual (303)
        redirectLocation(result) must some("/")
      }
      "Go to register page if not login" in {
        val result = controller.addUser().apply(FakeRequest(GET, "/users/register").withCSRFToken)
        contentAsString(result) must contain("Register")
      }
    }
    "storeUser" in {
      "store success then redirect Home" in {
        val registerInfo = RegisterInfo("test@gmail.com", "123123", "123123")
        mockUserDao.insert(registerInfo) returns Success(5)
        val result = controller.storeUser.apply(FakeRequest(POST, "/users/register").withFormUrlEncodedBody("mail" -> "test@gmail.com", "password" -> "123123", "rePassword" -> "123123").withCSRFToken)
        status(result) mustEqual (303)
        redirectLocation(result) must some("/")
      }
      "store unsuccess because email is empty" in {
        val registerInfo = RegisterInfo("test@gmail.com", "123123", "123123")
        mockUserDao.insert(registerInfo) returns Success(5)
        val result = controller.storeUser.apply(FakeRequest(POST, "/users/register").withFormUrlEncodedBody("mail" -> "", "password" -> "123123", "rePassword" -> "123123").withCSRFToken)
        contentAsString(result) must contain("error.email")
      }
      "store unsuccess because password does not match the confirm password" in {
        val registerInfo = RegisterInfo("test@gmail.com", "123123", "123123")
        mockUserDao.insert(registerInfo) returns Success(5)
        val result = controller.storeUser.apply(FakeRequest(POST, "/users/register").withFormUrlEncodedBody("mail" -> "test@gmail.com", "password" -> "123123", "rePassword" -> "a123123").withCSRFToken)
        contentAsString(result) must contain("Password does not match the confirm password")
      }
      "store unsuccess because Email is already registered" in {
        val registerInfo = RegisterInfo("test@gmail.com", "123123", "123123")
        mockUserDao.insert(registerInfo) returns Failure(new MySQLIntegrityConstraintViolationException("Duplicate entry"))
        val result = controller.storeUser.apply(FakeRequest(POST, "/users/register").withFormUrlEncodedBody("mail" -> "test@gmail.com", "password" -> "123123", "rePassword" -> "123123").withCSRFToken)
        contentAsString(result) must contain("Email is already registered")
      }
      "store unsuccess because system error" in {
        val registerInfo = RegisterInfo("test@gmail.com", "123123", "123123")
        mockUserDao.insert(registerInfo) returns Failure(new Exception)
        val result = controller.storeUser.apply(FakeRequest(POST, "/users/register").withFormUrlEncodedBody(
          "mail" -> "test@gmail.com",
          "password" -> "123123",
          "rePassword" -> "123123"
        ).withCSRFToken)
        status(result) mustEqual (500)
      }
    }
  }
}
