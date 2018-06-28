package setup
import scalikejdbc.ConnectionPool

trait DBSetting {
  def loadJDBC() {
    val url = "jdbc:mysql://localhost/bbs_test?characterEncoding=UTF-8"
    val user = "root"
    val password = ""
    ConnectionPool.singleton(url, user, password)
  }

  loadJDBC()
}