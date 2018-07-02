package controllers
import javax.inject._
import play.api.mvc._
import models.post.PostDao

import scala.util.{ Failure, Success }

@Singleton
class PostController @Inject() (postDao: PostDao, cc: ControllerComponents) extends AbstractController(cc) {
  def listPost = Action {
    Ok(views.html.post.listPost(postDao.getAll.get))
  }
  def getPostByID(id: Int) = Action {
    Ok(views.html.post.viewPost(postDao.getByID(id).get))
  }
}
