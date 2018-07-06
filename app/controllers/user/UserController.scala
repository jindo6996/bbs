package controllers

import controllers.BaseController
import controllers.exception.{ FormErrorException, UserNotFoundException }
import controllers.form.login.LoginForm
import javax.inject._
import play.api.mvc.{ AbstractController, ControllerComponents, Request, Result }
import controllers.form.login.LoginForm._
import models.user.UserDao

import scala.util.{ Failure, Success, Try }

@Singleton
class UserController @Inject() (userDao: UserDao, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport with BaseController {
  def login = Action { implicit request =>
    if (getMailInSession.isDefined) {
      Redirect(routes.HomeController.index())
    } else {
      Ok(views.html.user.login(loginForm))
    }
  }
  //--------
  def validate = Action { implicit request =>
    validateForm(loginForm) match {
      case Success(form)                                   => checkToLogin(form)
      case Failure(formErr: FormErrorException[LoginInfo]) => BadRequest(views.html.user.login(formErr.formError))
    }
  }
  //-----------
  def checkToLogin(userInfo: LoginInfo)(implicit request: Request[Any]): Result = {
    val t = userDao.getUserByUsernamePassword(userInfo)
    userDao.getUserByUsernamePassword(userInfo) match {
      case Success(user)                     => Redirect(routes.HomeController.index()).withSession("mail" -> user.mail)
      case Failure(e: UserNotFoundException) => BadRequest(views.html.user.login(LoginForm.loginForm.fill(userInfo).withGlobalError(e.msg)))
    }
  }
  //----
  def getMailInSession(implicit request: Request[Any]) = request.session.get("mail")

  //-------
  def logout = Action {
    Redirect(routes.UserController.login).withNewSession
  }
}
