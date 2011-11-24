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
package com.excilys.ebi.gatling.vtd.http.check.body.extractor
import com.excilys.ebi.gatling.core.check.extractor.ExtractorFactory
import com.excilys.ebi.gatling.vtd.check.extractor.VTDXPathExtractor
import com.excilys.ebi.gatling.vtd.check.extractor.MultiVTDXPathExtractor
import com.ning.http.client.Response

class HttpBodyVTDXPathExtractorFactory(occurence: Option[Int]) extends ExtractorFactory[Response] {

	def getExtractor(response: Response) = {
		occurence.map { value =>
			new VTDXPathExtractor(response.getResponseBodyAsBytes, value)
		}.getOrElse {
			new MultiVTDXPathExtractor(response.getResponseBodyAsBytes)
		}
	}
}