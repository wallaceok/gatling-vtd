import sbt._

object Dependencies {

	val vtd = "com.ximpleware" % "vtd-xml" % "2.11"

	val core: String => ModuleID = "io.gatling" % "gatling-core" % _
	val http: String => ModuleID = "io.gatling" % "gatling-http" % _

	def gatlingVtdDeps(version: String) = vtd +: Seq(core, http).map(_(version))
}