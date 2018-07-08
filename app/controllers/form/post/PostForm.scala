package controllers.form.post

import play.api.data.Form
import play.api.data.Forms._

object PostForm {
  case class PostInfo(title: String, content: String, email: String)
  val postForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "content" -> nonEmptyText,
      "email" -> email
    )(PostInfo.apply)(PostInfo.unapply)
  )
}
