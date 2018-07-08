package controllers.form.user

import play.api.data.Form
import play.api.data.Forms._

object RegisterForm {
  case class RegisterInfo(mail: String, password: String, rePassword: String)
  val registerForm = Form(
    mapping(
      "mail" -> nonEmptyText,
      "password" -> nonEmptyText,
      "rePassword" -> nonEmptyText
    )(RegisterInfo.apply)(RegisterInfo.unapply)
  )
}
