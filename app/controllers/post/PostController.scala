package controllers
import controllers.authentication.Authentication
import controllers.form.post.PostForm.{ PostInfo, postForm }
import javax.inject._
import play.api.mvc._
import models.post.{ Post, PostDao }
import controllers.exception._

import scala.util.{ Failure, Success }

@Singleton
class PostController @Inject() (postDao: PostDao, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport with BaseController with Authentication {
  def listPost = Action { implicit request =>
    Ok(views.html.post.listPost(postDao.getAll.get))
  }
  //--------------
  def getPostByID(id: Int) = Action { implicit request =>
    Ok(views.html.post.viewPost(postDao.getByID(id).get))
  }
  //--------------
  def addPost() = Action { implicit request =>
    getMailInSession.map { mail =>
      val postInfo = PostInfo("", "", mail)
      val postFormLogged = postForm.fill(postInfo)
      Ok(views.html.post.addPost(postFormLogged))
    }.getOrElse(Ok(views.html.post.addPost(postForm)))

  }
  //------------
  def savePost() = Action { implicit request =>
    val result = for {
      post <- validateForm(postForm)
      postSolve = post.copy(email = getMailInSession.getOrElse(post.email))
      id <- postDao.insert(postSolve)
    } yield id
    result match {
      case Success(id) => Ok(views.html.post.viewPost(postDao.getByID(id.toInt).get))
      case Failure(e: Exception) => e match {
        case formErr: FormErrorException[PostInfo] => BadRequest(views.html.post.addPost(formErr.formError))
        case _                                     => InternalServerError(e.getMessage)
      }
    }
  }
}
