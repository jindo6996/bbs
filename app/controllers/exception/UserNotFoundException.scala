package controllers.exception

case class UserNotFoundException(msg: String) extends Exception(msg)
