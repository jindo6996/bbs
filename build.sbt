name := "bbs_project1"
 
version := "1.0" 
      
lazy val `bbs_project1` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

libraryDependencies += evolutions

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.34"
libraryDependencies ++= Seq(
  "com.h2database"  %  "h2"                           % "1.4.196", // your jdbc driver here
  "org.scalikejdbc" %% "scalikejdbc"                  % "3.2.0",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "3.2.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.2",
  "org.scalikejdbc" %% "scalikejdbc-test"   % "3.2.2"   % "test",
  "org.webjars" % "bootstrap" % "3.3.7",
  "junit"          % "junit"     % "4.9"   withSources()
)
libraryDependencies += specs2 % Test