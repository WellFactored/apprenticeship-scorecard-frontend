
name := "apprenticeship-scorecard-frontend"

scalaVersion := "2.11.8"

enablePlugins(PlayScala)
disablePlugins(PlayLayoutPlugin)

enablePlugins(GitVersioning)
enablePlugins(GitBranchPrompt)

git.useGitDescribe := true

PlayKeys.devSettings := Seq("play.server.http.port" -> "9005")

routesImport ++= Seq()

libraryDependencies ++= Seq(
  cache,
  ws,
  "joda-time" % "joda-time" % "2.7",
  "org.joda" % "joda-convert" % "1.7",
  "org.typelevel" %% "cats" % "0.4.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0-RC1" % Test
)

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")
