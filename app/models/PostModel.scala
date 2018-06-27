package models
import scalikejdbc._
import scalikejdbc.config._

case class Posts(id: Int, title: String)
case class PostDao(){
  DBs.setupAll()
  def all: List[Posts] = DB readOnly { implicit session =>
    sql"SELECT * FROM BBS_Post".map(a => (Posts(a.int("id"), a.string("title")))).list.apply()
  }
}
