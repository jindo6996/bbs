package controllers.exception

case class EntityNotFoundException[T](msg: String, entity: T) extends Exception(msg)
