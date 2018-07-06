package models.user

import controllers.exception.{ UserNotFoundException, FormErrorException }
import controllers.form.login.LoginForm.LoginInfo
import scalikejdbc._
import javax.inject.Singleton
import untils.EncryptPassword._

import scala.util.Try

@Singleton
class UserDao() {
  def getUserByUsernamePassword(loginInfo: LoginInfo)(implicit session: DBSession = AutoSession): Try[User] = Try {
    val password = encryptPassword(loginInfo.password)
    sql"SELECT * FROM users WHERE mail=${loginInfo.mail} AND password=${password}".map(extract).single().apply()
      .getOrElse(throw UserNotFoundException("User not found", loginInfo))
  }
  private def extract(rs: WrappedResultSet): User = {
    User(rs.int("id"), rs.string("mail"), rs.string("password"))
  }
}
