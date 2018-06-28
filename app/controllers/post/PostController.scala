package controllers
import javax.inject._
import play.api.mvc._
import models.post.{ PostDao }
import scala.util.{ Success, Failure }

@Singleton
class PostController @Inject() (postDao: PostDao, cc: ControllerComponents) extends AbstractController(cc) {
  def listPost = Action {
    //    Ok((postDao.getAll))
    postDao.getAll match {
      case Success(posts) => Ok(views.html.post.listPost(posts))
      case Failure(ex)    => InternalServerError("A server error occurred: " + ex.getMessage)
    }
  }
}
