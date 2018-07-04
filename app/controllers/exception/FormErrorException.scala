package controllers.exception

import play.api.data.Form

case class FormErrorException[T](msg: String, formError: Form[T]) extends Exception(msg)
