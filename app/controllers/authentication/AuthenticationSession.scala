package controllers.authentication

import play.api.mvc.Request

trait AuthenticationSession {
  def getMailInSession(implicit request: Request[Any]) = request.session.get("mail")
}
