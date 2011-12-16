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
import com.excilys.ebi.gatling.core.util.StringHelper.EMPTY

/**
 * VTD-XML based XPath Extractor.
 *
 * Byte Array parsing is optimized as it only happens once so that the Extractor can be reused for multiple XPath expressions on the same targe.
 *
 * @author <a href="mailto:slandelle@excilys.com">Stephane Landelle</a>
 */
class VTDXPathExtractor(xmlContent: Array[Byte], occurrence: Int) extends AbstractVTDXPathExtractor(xmlContent) {

	def doExtract = {

		var count = 0
		var index = -1;
		do {
			index = ap.evalXPath();
			count += 1

			if (index != -1) {
				index = getTextIndex(index, vn)
			}
		} while (count < occurrence && index != -1)

		if (count < occurrence || index == -1) {
			Nil
		} else {
			val result = vn.toString(index)
			if (result.equals(EMPTY))
				Nil
			else
				List(result)
		}
	}
}