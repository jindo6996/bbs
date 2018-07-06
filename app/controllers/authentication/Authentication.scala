package controllers.authentication

import play.api.mvc.Request

trait Authentication {
  def getMailInSession(implicit request: Request[Any]) = request.session.get("mail")
}
