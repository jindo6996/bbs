package models.user

import controllers.exception.{ UserNotFoundException }
import controllers.form.login.LoginForm.LoginInfo
import scalikejdbc.DB
import scalikejdbc._
import javax.inject.Singleton
import untils.EncryptPassword

import scala.util.Try

@Singleton
class UserDao() extends EncryptPassword {
  def getUserByUsernamePassword(loginInfo: LoginInfo)(implicit session: DBSession = AutoSession): Try[User] = Try {
    val password = encryptPassword(loginInfo.password)
    sql"SELECT * FROM users WHERE mail=${loginInfo.mail} AND password=${password}".map(extractUser).single().apply()
      .getOrElse(throw UserNotFoundException("User not found"))
  }
  private def extractUser(rs: WrappedResultSet): User = {
    User(rs.int("id"), rs.string("mail"), rs.string("password"))
  }
}
