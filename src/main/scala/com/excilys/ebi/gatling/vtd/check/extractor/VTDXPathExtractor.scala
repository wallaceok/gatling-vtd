/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
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

import com.excilys.ebi.gatling.core.check.extractor.Extractor.{ toOption, seqToOption }
import com.excilys.ebi.gatling.core.util.StringHelper.EMPTY
import com.ximpleware.VTDNav.{ TOKEN_STARTING_TAG, TOKEN_PI_VAL, TOKEN_PI_NAME, TOKEN_ATTR_NAME }
import com.ximpleware.{ VTDNav, CustomVTDGen, AutoPilot }

/**
 * VTD-XML based XPath Extractor.
 *
 * Byte Array parsing is optimized as it only happens once so that the Extractor can be reused for multiple XPath expressions on the same target.
 *
 * @author <a href="mailto:slandelle@excilys.com">Stephane Landelle</a>
 */
class VTDXPathExtractor(bytes: Array[Byte]) {

	val vtdEngine = new CustomVTDGen
	vtdEngine.setDoc(bytes)
	vtdEngine.parse(false)
	val vn: VTDNav = vtdEngine.getNav
	val ap: AutoPilot = new AutoPilot(vn)

	private def getTextIndex(index: Int, vn: VTDNav) = {
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

	private def xpath[X](expression: String)(f: () => X) : X = {
		ap.selectXPath(expression)
		val values = f()
		ap.resetXPath
		values
	}

	def extractOne(occurrence: Int)(expression: String): Option[String] = {
		xpath(expression) { () =>
			var count = 0
			var index = -1
			do {
				index = ap.evalXPath
				count += 1

				if (index != -1) {
					index = getTextIndex(index, vn)
				}
			} while (count < occurrence && index != -1)

			if (count < occurrence || index == -1) {
				None
			} else {
				vn.toString(index)
			}
		}
	}

	def extractMultiple(expression: String): Option[Seq[String]] = {
		xpath(expression) { () =>
			var index = -1
			var values: List[String] = Nil
			do {
				index = ap.evalXPath

				if (index != -1) {
					index = getTextIndex(index, vn)

					val result = vn.toString(index)
					if (!result.equals(EMPTY))
						values = result :: values
				}
			} while (index != -1)
			values
		}
	}

	def count(expression: String): Option[Int] = extractMultiple(expression).getOrElse(Nil).size
}