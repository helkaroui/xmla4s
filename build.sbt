import Dependencies._

ThisBuild / scalaVersion := "2.13.5"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "dev.sharek"
ThisBuild / organizationName := "Sharek"



lazy val root = (project in file("."))
  .settings(
    name := "xmla4s",
    dependencies
  )

lazy val Http4sVersion = "1.0.0-M23"

lazy val dependencies = depends(
  scalaTest % Test,
  "org.typelevel" %% "cats-effect" % "3.1.1" withSources() withJavadoc(),
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "org.scala-lang.modules" %% "scala-xml" % "2.0.0"

)

def depends(modules: ModuleID*): Seq[Def.Setting[Seq[ModuleID]]] = Seq(libraryDependencies ++= modules)