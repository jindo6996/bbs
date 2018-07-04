package controllers

import play.api.data.Form
import play.api.mvc.Request

import scala.util.{ Failure, Success, Try }

trait CustomException {
  def validateException[T](form: Form[T], ex: Exception)(implicit resquest: Request[Any]): Try[Form[T]] = {
    if (ex.getMessage == "Error Form") return Success(form.bindFromRequest)
    else return Failure(new Exception("Unknown"))
  }
}
