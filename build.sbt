ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

name              := "DocumentSimilarityApp"
version           := "0.1"
scalaVersion      := "2.13.8"
javacOptions ++= Seq("--release", "11")
scalafmtOnCompile := true

lazy val root = (project in file("."))
  .settings(
    name := "DocumentSimilarityApp",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core"      % "3.5.1",
      "org.apache.spark" %% "spark-sql"       % "3.5.1",
      "org.apache.spark" %% "spark-mllib"     % "3.5.1",
      "org.slf4j"         % "slf4j-api"       % "2.0.12",
      "ch.qos.logback"    % "logback-classic" % "1.5.6",

    )
  )

addCommandAlias("cc", "clean; compile")
