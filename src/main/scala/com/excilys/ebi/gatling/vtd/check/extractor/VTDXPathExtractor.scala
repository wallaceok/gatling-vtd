package com.excilys.ebi.gatling.vtd.check.extractor
import com.excilys.ebi.gatling.core.check.extractor.Extractor
import com.excilys.ebi.gatling.core.util.StringHelper.EMPTY
import com.ximpleware.{CustomVTDGen, AutoPilot}

class VTDXPathExtractor(xmlContent: Array[Byte], occurrence: Int) extends Extractor {

	val vtdEngine = new CustomVTDGen
	vtdEngine.setDoc(xmlContent)
	vtdEngine.parse(false)
	var vn = vtdEngine.getNav()
	var ap = new AutoPilot(vn)

	def extract(expression: String): Option[String] = {

		ap.selectXPath(expression)
		val result = ap.evalXPathToString
		// FIXME return correct occurence
		val value = if (result.equals(EMPTY))
			None
		else
			Some(ap.evalXPathToString)

		logger.debug("XPATH CAPTURE: {}", value)
		value
	}
}