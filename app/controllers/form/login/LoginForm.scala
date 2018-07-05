package controllers.form.login

import play.api.data.Form
import play.api.data.Forms._

object LoginForm {
  case class LoginInfo(mail: String, password: String)
  val loginForm = Form(
    mapping(
      "mail" -> email,
      "password" -> nonEmptyText
    )(LoginInfo.apply)(LoginInfo.unapply)
  )
}
