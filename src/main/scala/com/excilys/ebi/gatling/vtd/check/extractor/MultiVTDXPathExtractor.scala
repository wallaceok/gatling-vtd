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
import com.ximpleware.{ CustomVTDGen, AutoPilot }
import com.excilys.ebi.gatling.core.check.extractor.MultiValuedExtractor

/**
 * VTD-XML based XPath Extractor.
 *
 * Byte Array parsing is optimized as it only happens once so that the Extractor can be reused for multiple XPath expressions on the same targe.
 *
 * @author <a href="mailto:slandelle@excilys.com">Stephane Landelle</a>
 */
class MultiVTDXPathExtractor(xmlContent: Array[Byte]) extends AbstractVTDXPathExtractor(xmlContent) with MultiValuedExtractor {

	def doExtract = {
		var index = -1;
		var values: List[String] = Nil
		do {
			index = ap.evalXPath();

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