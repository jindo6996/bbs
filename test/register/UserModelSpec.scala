package register

import controllers.form.user.RegisterForm.RegisterInfo
import models.user.UserDao
import play.api.test.PlaySpecification
import setup.DBSetting
import scalikejdbc._
import scala.util.Success

class UserModelSpec extends PlaySpecification with DBSetting {
  val conn: java.sql.Connection = ConnectionPool('bbs_test).borrow()
  val userModels: UserDao = new UserDao()
  val mail = "test@gmail.com"
  val password = "123123"
  "User model" should {
    "insert" in {
      "Store user success" in {
        using(ConnectionPool.borrow()) { conn =>
          implicit val session: DBSession = AutoSession
          sql"DELETE FROM users".update.apply()
          sql"ALTER TABLE users AUTO_INCREMENT = 1".update.apply()
          val registerInfo = RegisterInfo("test@gmail.com", "123123", "123123")
          userModels.insert(registerInfo) match {
            case Success(id) => id mustEqual (1)
          }
        }
      }
    }
  }
}
