# Motivation
gatling-vtd is an extension for the [Gatling](https://github.com/excilys/gatling) stress tool that provides XPath extraction based on [VTD-XML](http://vtd-xml.sourceforge.net).

VTD-XML is a XPath 1.0 implementation that is much more faster than the standard one.

# Usage

## Step 1 : adding the dependency
### When using the Gatling bundle

Just drop the gatling-vtd-&lt;version&gt;.jar into the lib directory.
You can find the suitable jar in the [download section](https://github.com/excilys/gatling-vtd/downloads).

### When using maven Maven

Use the following in you pom.xml :

``` xml
<repository>
	<id>excilys</id>
	<name>Excilys Repository</name>
	<url>http://repository.excilys.com/content/groups/public</url>
</repository>
<dependencies>
	<dependency>
		<groupId>com.excilys.ebi.gatling.vtd</groupId>
		<artifactId>gatling-vtd</artifactId>
		<version>${gatling-vtd.version}</version>
	</dependency>
</dependencies>
```

## Step 2 : using in the scripts

If you use the txt format, the required imports are automatically registered.
If you use the scala format, add the following import :

    import com.excilys.ebi.gatling.vtd.http.check.body.HttpBodyVTDXPathCheckBuilder._

This will let you use the new extractor builtins. The syntax is similar to the standard XPath one, just replace the prefix "xpath" with "vtdXPath". For example :

    capture(vtdXPath("//input[@id='text1']/@value") saveAs "aaaa_value")

# Important notice : licence

VTD-XML use a dual GPLv2/commercial licence.

This is the reason why gatling-vtd is a separate project licenced under GPLv2 instead of being integrated into core Gatling, that is Apache 2 licenced.

So please be aware that if you use this module :

* either your whole Gatling based project become GPLv2,
* or you have to purchase a commercial licence from [Ximpleware](http://www.ximpleware.com).