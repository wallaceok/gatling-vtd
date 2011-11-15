# Motivation
gatling-vtd is an extension for the [Gatling](https://github.com/excilys/gatling) stress tool that provides XPath extraction based on [VTD-XML](http://vtd-xml.sourceforge.net).

VTD-XML is a XPath 1.0 implementation that is much more faster than the standard one.

# Usage

## Step 1 : adding the dependency
### When using the Gatling bundle

Just drop the gatling-vtd-<version>.jar into the lib directory.

### When using maven Maven

Use the following in you pom.xml :
<repository>
	<id>excilys-thirdparty</id>
	<name>Excilys Third Party Repository</name>
	<url>http://repository.excilys.com/content/repositories/thirdparty</url>
</repository>
<dependencies>
	<dependency>
		<groupId>com.excilys.ebi.gatling</groupId>
		<artifactId>gatling-vtd</artifactId>
		<version>${gatling-vtd.version}</version>
	</dependency>
</dependencies>

## Step 2 : using in the scripts

Add the following line into your script :

    import com.excilys.ebi.gatling.vtd.http.check.body.HttpBodyVTDXPathCheckBuilder._

This will let you use the new extractor builtins. The syntax is similar to the standard XPath one, just replace the prefix "xpath" with "vtdXPath". For example :

    capture(vtdXPath("//input[@id='text1']/@value") in "aaaa_value")

# Important notice : licence

VTD-XML use a dual GPLv3/commercial licence.

This is the reason why gatling-vtd is a separate project licenced under GPLv2 instead of being integrated into core Gatling, that is Apache 2 licenced.

So please be aware that if you use this module :

* either your whole Gatling based project become GPLv2,
* or you have to purchase a commercial licence from [Ximpleware](http://www.ximpleware.com).