package controllers

import models.post.Post
import play.api.data.Form
import play.api.mvc.Request
import scala.util.{ Failure, Success }
import scala.util.Try
import exception._

trait BaseController {
  def validateForm[T](form: Form[T])(implicit resquest: Request[Any]): Try[T] = {
    form.bindFromRequest().fold(
      formWithErrors => {
        Failure(new FormErrorException("Error Form", formWithErrors))
      },
      result => {
        Success(result)
      }
    )

  }

}
