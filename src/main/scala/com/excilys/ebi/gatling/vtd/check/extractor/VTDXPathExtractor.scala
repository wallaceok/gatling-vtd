/**
 * Copyright 2011 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package com.excilys.ebi.gatling.vtd.check.extractor
import com.excilys.ebi.gatling.core.check.extractor.Extractor
import com.excilys.ebi.gatling.core.util.StringHelper.EMPTY
import com.ximpleware.VTDNav.{TOKEN_STARTING_TAG, TOKEN_PI_VAL, TOKEN_PI_NAME, TOKEN_ATTR_NAME}
import com.ximpleware.{CustomVTDGen, AutoPilot}

/**
 * VTD-XML based XPath Extractor.
 *
 * Byte Array parsing is optimized as it only happens once so that the Extractor can be reused for multiple XPath expressions on the same targe.
 *
 * @author <a href="mailto:slandelle@excilys.com">Stephane Landelle</a>
 */
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