package controllers
import controllers.form.post.PostForm.postForm
import javax.inject._
import play.api.mvc._
import models.post.{ Post, PostDao }

import scala.util.{ Failure, Success }

@Singleton
class PostController @Inject() (postDao: PostDao, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {
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
    postForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.post.addPost(formWithErrors))
      },
      postResult => {
        postDao.createPost(postResult)
        val newPost = Option(Post(0, postResult.title, postResult.content, postResult.email))
        Ok(views.html.post.viewPost(newPost))
      }
    )
  }
}
