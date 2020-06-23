import sbtcrossproject.{crossProject, CrossType}

lazy val server = (project in file("server")).settings(commonSettings).settings(
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.vmunier" %% "scalajs-scripts" % "1.1.4",
    "mysql" % "mysql-connector-java" % "5.1.41",
    "com.google.cloud" % "google-cloud-translate" % "1.94.5",
    // "com.typesafe.play" %% "play-slick" % "5.0.0",
    // "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
    jdbc,
    evolutions,
    guice,
    specs2 % Test
  ),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
  EclipseKeys.preTasks := Seq(compile in Compile)
)
.enablePlugins(PlayScala)
.dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "1.0.0",
    "org.scalatest" %%% "scalatest" % "3.1.2" % Test
  ),
  // skip in packageJSDependencies := false,
  // jsDependencies ++= Seq(
  //   "org.webjars" % "jquery" % "2.2.1" / "jquery.js" minified "jquery.min.js",
  //   ProvidedJS / "tesseract.v1.js" commonJSName "Tesseract",
  //   ProvidedJS / "worker.v1.js" commonJSName "Worker",
  // )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb)
.dependsOn(sharedJs)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(commonSettings)
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val commonSettings = Seq(
  scalaVersion := "2.13.1",
  organization := "cupper"
)
