package com.excilys.ebi.gatling.vtd.check.extractor
import com.excilys.ebi.gatling.core.check.extractor.Extractor
import com.excilys.ebi.gatling.core.util.StringHelper.EMPTY
import com.ximpleware.VTDNav.{ TOKEN_STARTING_TAG, TOKEN_PI_VAL, TOKEN_PI_NAME, TOKEN_ATTR_NAME }
import com.ximpleware.{ CustomVTDGen, AutoPilot }

class VTDXPathExtractor(xmlContent: Array[Byte], occurrence: Int) extends Extractor {

	val vtdEngine = new CustomVTDGen
	vtdEngine.setDoc(xmlContent)
	vtdEngine.parse(false)
	val vn = vtdEngine.getNav
	val ap = new AutoPilot(vn)

	def extract(expression: String): Option[String] = {

		ap.selectXPath(expression)

		var count = 0
		var index = -1;
		do {
			index = ap.evalXPath();
			count += 1

			if (index != -1) {
				index = vn.getTokenType(index) match {
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
		} while (count < occurrence && index != -1)

		val value = if (count < occurrence || index == -1) {
			None
		} else {
			val result = vn.toString(index)
			if (result.equals(EMPTY))
				None
			else
				Some(result)
		}

		ap.resetXPath

		logger.debug("XPATH CAPTURE: {}", value)

		value
	}
}