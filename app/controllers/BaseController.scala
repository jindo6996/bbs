package controllers

import models.post.Post
import play.api.data.Form
import play.api.mvc.Request
import scala.util.{ Failure, Success }
import scala.util.Try

trait BaseController {
  def validateForm[T](form: Form[T])(implicit resquest: Request[Any]): Try[T] = {
    form.bindFromRequest().fold(
      formWithErrors => {
        Failure(new Exception("Error Form"))
      },
      result => {
        Success(result)
      }
    )

  }

}
