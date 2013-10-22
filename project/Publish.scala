import sbt._
import sbt.Keys._

import Resolvers._

object Publish {

	lazy val settings = Seq(
		crossPaths           := false,
		pomExtra             := scm ++ developersXml(developers),
		publishMavenStyle    := true,
		pomIncludeRepository := { _ => false },
		publishTo            := Some(if(isSnapshot.value) sonatypeSnapshots else sonatypeReleases),
		credentials          += Credentials(Path.userHome / ".sbt" / ".credentials")
	)

	/************************/
	/** POM extra metadata **/
	/************************/

	private val scm = {
		<scm>
			<connection>scm:git:git@github.com:excilys/gatling-vtd.git</connection>
			<developerConnection>scm:git:git@github.com:excilys/gatling-vtd.git</developerConnection>
			<url>https://github.com/excilys/gatling-vtd</url>
			<tag>HEAD</tag>
		</scm>
	}

	private case class GatlingDeveloper(emailAddress: String, name: String, isEbiz: Boolean)

	private val developers = Seq(
		GatlingDeveloper("slandelle@excilys.com", "Stephane Landelle", true),
		GatlingDeveloper("rsertelon@excilys.com", "Romain Sertelon", true)
	)

	private def developersXml(devs: Seq[GatlingDeveloper]) = {
		<developers>
		{
			for(dev <- devs)
			yield {
				<developer>
					<id>{dev.emailAddress}</id>
					<name>{dev.name}</name>
					{ if (dev.isEbiz) <organization>eBusiness Information, Excilys Group</organization> }
				</developer>
			}
		}
		</developers>
	}
}