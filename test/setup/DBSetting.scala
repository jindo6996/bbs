package setup
import scalikejdbc.ConnectionPool

trait DBSetting {
  def loadJDBC() {
    val url = "jdbc:mysql://localhost/bbs_test"
    val user = "root"
    val password = "123123"
    ConnectionPool.singleton(url, user, password)
    ConnectionPool.add('bbs_test, url, user, password)
  }

  loadJDBC()
}