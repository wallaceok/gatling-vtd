package com.excilys.ebi.gatling.vtd.check.extractor

import com.excilys.ebi.gatling.core.check.extractor.Extractor
import com.ximpleware.VTDNav.{ TOKEN_STARTING_TAG, TOKEN_PI_VAL, TOKEN_PI_NAME, TOKEN_ATTR_NAME }
import com.ximpleware.{ VTDNav, CustomVTDGen, AutoPilot }

abstract class AbstractVTDXPathExtractor(xmlContent: Array[Byte]) extends Extractor {

	val vtdEngine = new CustomVTDGen
	vtdEngine.setDoc(xmlContent)
	vtdEngine.parse(false)
	val vn = vtdEngine.getNav
	val ap = new AutoPilot(vn)

	def getTextIndex(index: Int, vn: VTDNav) = {
		vn.getTokenType(index) match {
			case TOKEN_ATTR_NAME => index + 1
			case TOKEN_STARTING_TAG => vn.getText
			case TOKEN_PI_NAME => {
				if (index + 1 < vn.getTokenCount && vn.getTokenType(index + 1) == TOKEN_PI_VAL)
					index + 1;
				else
					index - 1;
			}
			case x => throw new IllegalArgumentException("Unknown token type " + x)
		}
	}

	def extract(expression: String): List[String] = {

		ap.selectXPath(expression)

		val values = doExtract

		ap.resetXPath

		logger.debug("XPATH CAPTURE: {}", values)

		values
	}

	def doExtract: List[String]
}