import sbt._
import sbt.Keys._

import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import net.virtualvoid.sbt.graph.Plugin.graphSettings

import Resolvers._

object BuildSettings {

	lazy val basicSettings = Seq(
		homepage             := Some(new URL("http://gatling.io")),
		organization         := "io.gatling.vtd",
		organizationHomepage := Some(new URL("http://gatling.io")),
		startYear            := Some(2011),
		licenses             := Seq("GPLv2" -> new URL("http://www.gnu.org/licenses/gpl-2.0.html")),
		resolvers            := Seq(if(isSnapshot.value) sonatypeSnapshots else sonatypeReleases),
		scalaVersion         := "2.10.4",
		scalacOptions        := Seq(
			"-encoding",
			"UTF-8",
			"-target:jvm-1.6",
			"-deprecation",
			"-feature",
			"-unchecked",
			"-language:implicitConversions",
			"-language:postfixOps"
		)
	)

	lazy val gatlingVtdSettings = 
		basicSettings ++ formattingSettings ++ graphSettings ++ 
		Publish.settings ++ Release.settings

	/*************************/
	/** Formatting settings **/
	/*************************/

	lazy val formattingSettings = SbtScalariform.scalariformSettings ++ Seq(
		ScalariformKeys.preferences := formattingPreferences
	)

	import scalariform.formatter.preferences._

	def formattingPreferences = 
	FormattingPreferences()
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(IndentLocalDefs, true)

}
