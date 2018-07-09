package models.user

import controllers.exception.{ EntityNotFoundException, FormErrorException }
import controllers.form.login.LoginForm.LoginInfo
import controllers.form.user.RegisterForm.RegisterInfo
import scalikejdbc._
import javax.inject.Singleton
import untils.EncryptPassword._

import scala.util.Try

@Singleton
class UserDao() {
  def getUserByUsernamePassword(loginInfo: LoginInfo)(implicit session: DBSession = AutoSession): Try[User] = Try {
    val password = encryptPassword(loginInfo.password)
    sql"SELECT * FROM users WHERE mail=${loginInfo.mail} AND password=${password}".map(extract).single().apply()
      .getOrElse(throw EntityNotFoundException("User not found", loginInfo))
  }
  def insert(registerInfo: RegisterInfo)(implicit session: DBSession = AutoSession): Try[Long] = Try {
    val password = encryptPassword(registerInfo.password)
    sql"insert into users(mail,password) values (${registerInfo.mail}, ${password})".updateAndReturnGeneratedKey().apply()
  }
  private def extract(rs: WrappedResultSet): User = {
    User(rs.int("id"), rs.string("mail"), rs.string("password"))
  }
}
