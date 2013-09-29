name := "appd"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.clapper" % "markwrap_2.10" % "1.0.1"
)     

play.Project.playScalaSettings
