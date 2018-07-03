package controllers.form.post

import play.api.data.Form
import play.api.data.Forms.{ mapping, text }
import play.api.data.validation.Constraints

// PostForm object: This is the form of the create post page: having 3 fields to fill (email, title and content of post)
object PostForm {
  case class PostInfo(title: String, content: String, email: String)
  val postForm = Form(
    mapping(
      "title" -> text.verifying(Constraints.nonEmpty),
      "content" -> text.verifying(Constraints.nonEmpty),
      "email" -> text.verifying(Constraints.emailAddress, Constraints.maxLength(100))
    )(PostInfo.apply)(PostInfo.unapply)
  )
}
