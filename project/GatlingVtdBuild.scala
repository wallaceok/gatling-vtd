import sbt._
import sbt.Keys._

import BuildSettings._
import Dependencies._

object GatlingVtdBuild extends Build {

	override lazy val settings = super.settings ++ {
		shellPrompt := { state => Project.extract(state).currentProject.id + " > " }
	}

	lazy val vtd = Project("gatling-vtd", file("."))
		.settings(gatlingVtdSettings: _*)
		.settings(libraryDependencies ++= gatlingVtdDeps(version.value))

}