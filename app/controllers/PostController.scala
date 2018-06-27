package controllers
import javax.inject._
import play.api.mvc._
import models.{PostDao, Posts}


@Singleton
class PostController @Inject()(postDao: PostDao, cc: ControllerComponents) extends AbstractController(cc) {
  def listPost= Action {
    Ok(views.html.listPost(postDao.all))
  }
}
