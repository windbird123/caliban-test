ThisBuild / scalaVersion := "2.12.11"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.windbird123.test.caliban"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "caliban-test",
    libraryDependencies ++= Seq(
      "dev.zio"               %% "zio"              % "1.0.10",
      "io.github.kitlangton"  %% "zio-magic"        % "0.3.7",
      "com.github.ghostdogpr" %% "caliban"          % "1.1.1",
      "com.github.ghostdogpr" %% "caliban-client"   % "1.1.1",
      "com.github.ghostdogpr" %% "caliban-zio-http" % "1.1.1",
      "dev.zio"               %% "zio-test"         % "1.0.10" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
  .enablePlugins(CalibanPlugin)
