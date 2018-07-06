package controllers

import controllers.authentication.Authentication
import controllers.exception.{ FormErrorException, EntityNotFoundException }
import controllers.form.login.LoginForm
import controllers.form.login.LoginForm._
import javax.inject._
import models.user.UserDao
import play.api.mvc.{ AbstractController, ControllerComponents }

import scala.util.{ Failure, Success }

@Singleton
class AuthController @Inject() (userDao: UserDao, cc: ControllerComponents) extends AbstractController(cc)
  with play.api.i18n.I18nSupport with BaseController with Authentication {
  def login = Action { implicit request =>
    if (getMailInSession.isDefined) {
      Redirect(routes.HomeController.index())
    } else {
      Ok(views.html.user.login(loginForm))
    }
  }
  //--------
  def processLogin = Action { implicit request =>
    val result = for {
      loginInfo <- validateForm(loginForm)
      userInfo <- userDao.getUserByUsernamePassword(loginInfo)
    } yield userInfo
    result match {
      case Success(user) => Redirect(routes.HomeController.index()).withSession("mail" -> user.mail)
      case Failure(e: Exception) => e match {
        case formErr: FormErrorException[LoginInfo]      => BadRequest(views.html.user.login(formErr.formError))
        case userErr: EntityNotFoundException[LoginInfo] => BadRequest(views.html.user.login(LoginForm.loginForm.fill(userErr.user).withGlobalError(userErr.msg)))
      }
    }
  }
  //-------
  def logout = Action {
    Redirect(routes.AuthController.login).withNewSession
  }
}
