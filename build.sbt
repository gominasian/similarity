import sbtassembly.AssemblyPlugin.autoImport._

ThisBuild / version := "0.1.0-SNAPSHOT"

name              := "DocumentSimilarityApp"
version           := "0.1"
scalaVersion      := "2.13.8"
javacOptions ++= Seq("--release", "11")
scalafmtOnCompile := true

lazy val root = (project in file("."))
  .settings(
    name                 := "DocumentSimilarityApp",
    assembly / mainClass := Some("Server"),
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core"          % "3.5.1",
      "org.apache.spark" %% "spark-sql"           % "3.5.1",
      "org.apache.spark" %% "spark-mllib"         % "3.5.1",
      "org.typelevel"    %% "cats-core"           % "2.12.0",
      "org.typelevel"    %% "cats-kernel"         % "2.12.0",
      "org.typelevel"    %% "cats-effect"         % "3.3.14",
      "org.slf4j"         % "slf4j-api"           % "2.0.12",
      "ch.qos.logback"    % "logback-classic"     % "1.5.6",
      "org.http4s"       %% "http4s-dsl"          % "0.23.16",
      "org.http4s"       %% "http4s-blaze-server" % "0.23.16",
      "org.http4s"       %% "http4s-circe"        % "0.23.16",
      "io.circe"         %% "circe-core"          % "0.14.9",
      "io.circe"         %% "circe-generic"       % "0.14.9",
      "io.circe"         %% "circe-parser"        % "0.14.9"
    )
  )

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
  case PathList("META-INF", xs @ _*)                        => MergeStrategy.discard
  case x                                                    => MergeStrategy.defaultMergeStrategy(x)
}

addCommandAlias("cc", "clean; compile")
