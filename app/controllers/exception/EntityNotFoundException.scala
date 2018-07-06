package controllers.exception

import controllers.form.login.LoginForm.LoginInfo

case class EntityNotFoundException[T](msg: String, user: T) extends Exception(msg)
