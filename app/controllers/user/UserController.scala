package controllers

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import controllers.authentication.Authentication
import controllers.exception.FormErrorException
import controllers.form.user.RegisterForm
import controllers.form.user.RegisterForm._
import javax.inject.{ Inject, Singleton }
import models.user.UserDao
import play.api.mvc.{ AbstractController, ControllerComponents }

import scala.util.{ Failure, Success }

@Singleton
class UserController @Inject() (userDao: UserDao, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport with BaseController with Authentication {
  def addUser = Action { implicit request =>
    getMailInSession.map(mail =>
      Redirect(routes.HomeController.index())).getOrElse(Ok(views.html.user.register(registerForm)))
  }
  def storeUser = Action { implicit request =>
    (for {
      registerForm <- validateForm(registerForm)
      if registerForm.rePassword == registerForm.password
      id <- userDao.insert(registerForm)
    } yield {
      Redirect(routes.HomeController.index()).withSession("mail" -> registerForm.mail)
    }).recover {
      case formErr: FormErrorException[RegisterInfo] => BadRequest(views.html.user.register(formErr.formError))
      case emailExist: MySQLIntegrityConstraintViolationException if emailExist.getMessage.contains("Duplicate entry") => BadRequest(views.html.user.register(registerForm.bindFromRequest().withGlobalError("Email is already registered")))
      case passwordNotMatch: NoSuchElementException => BadRequest(views.html.user.register(registerForm.bindFromRequest().withGlobalError("Password does not match the confirm password")))
    }.get
  }
}
