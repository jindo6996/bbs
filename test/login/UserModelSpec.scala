package login

import controllers.form.login.LoginForm
import controllers.form.login.LoginForm.LoginInfo
import controllers.form.post.PostForm.PostInfo
import scala.util.{ Failure, Success }
import models.user.UserDao
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.test.PlaySpecification
import scalikejdbc._
import scalikejdbc.config._
import setup._
import untils.EncryptPassword

import scala.util.{ Failure, Success }
class UserModelSpec extends PlaySpecification with DBSetting with EncryptPassword {
  val conn: java.sql.Connection = ConnectionPool('bbs_test).borrow()
  val userModels: UserDao = new UserDao()
  val passTest = encryptPassword("123123")
  val mailTest = "test@gmail.com"
  //  val formUser=LoginForm.loginForm.fill(userInfo)
  //  val titleTest = "test"
  "User Model" should {
    "getUserByUsernamePassword" in {
      "Login success" in {
        using(ConnectionPool.borrow()) { conn =>
          implicit val session: DBSession = AutoSession
          val userInfo = LoginInfo(mailTest, "123123")
          sql"DELETE FROM users".update().apply()
          sql"INSERT INTO users(mail,password) VALUES (${mailTest}, ${passTest})".update().apply()
          val user = userModels.getUserByUsernamePassword(userInfo) match {
            case Success(userGet) => userGet
          }
          user.mail mustEqual (mailTest)
          user.password mustEqual (passTest)
        }
      }
      "Login unsuccess" in {
        using(ConnectionPool.borrow()) { conn =>
          implicit val session: DBSession = AutoSession
          // pass wrong
          val userInfo1 = LoginInfo(mailTest, "abc")
          sql"DELETE FROM users".update().apply()
          sql"INSERT INTO users(mail,password) VALUES (${mailTest}, ${passTest})".update().apply()
          val userException1 = userModels.getUserByUsernamePassword(userInfo1) match {
            case Failure(exception) => exception
          }
          userException1.getMessage mustEqual ("User not found")
          val userInfo2 = LoginInfo(mailTest + "abc", "123123")
          sql"DELETE FROM users".update().apply()
          sql"INSERT INTO users(mail,password) VALUES (${mailTest}, ${passTest})".update().apply()
          val userException2 = userModels.getUserByUsernamePassword(userInfo2) match {
            case Failure(exception) => exception
          }
          userException2.getMessage mustEqual ("User not found")
        }
      }
    }
  }
}
