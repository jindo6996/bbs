package controllers
import controllers.form.post.PostForm.postForm
import javax.inject._
import play.api.mvc._
import models.post.{ Post, PostDao }
import controllers.BaseController

import scala.util.{ Failure, Success, Try }

@Singleton
class PostController @Inject() (postDao: PostDao, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport with BaseController with CustomException {
  def listPost = Action {
    Ok(views.html.post.listPost(postDao.getAll.get))
  }
  //--------------
  def getPostByID(id: Int) = Action {
    Ok(views.html.post.viewPost(postDao.getByID(id).get))
  }
  //--------------
  def addPost() = Action { implicit request =>
    Ok(views.html.post.addPost(postForm))
  }
  //------------
  def savePost() = Action { implicit request =>
    val result = for {
      post <- validateForm(postForm)
      id <- postDao.createPost(post)
    } yield (id)
    result match {
      case Success(id) => Ok(views.html.post.viewPost(postDao.getByID(id.toInt).get))
      case Failure(e: Exception) => validateException(postForm, e) match {
        case Success(form) => BadRequest(views.html.post.addPost(form))
      }
    }
  }
}
