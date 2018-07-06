package setup
import scalikejdbc.ConnectionPool

trait DBSetting {
  def loadJDBC() {
    val url = "jdbc:mysql://localhost:3306/bbs_test"
    val user = "root"
    val password = "123123"
    ConnectionPool.singleton(url, user, password)
    ConnectionPool.add('bbs_test, url, user, password)
  }

  loadJDBC()
}