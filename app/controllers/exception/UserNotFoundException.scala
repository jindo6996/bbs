package controllers.exception

import controllers.form.login.LoginForm.LoginInfo

case class UserNotFoundException[T](msg: String, user: T) extends Exception(msg)
