package register

import controllers.UserController
import models.user.UserDao
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification
import play.api.test.Helpers._

class UserControllerSpec extends PlaySpecification with Mockito{
  val mockUserDao : UserDao = mock[UserDao]
  val controller = new UserController(mockUserDao, stubControllerComponents())
}
