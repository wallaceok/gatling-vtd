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
package com.excilys.ebi.gatling.vtd.http.check.body
import com.excilys.ebi.gatling.core.check.CheckContext.{ setAndReturnCheckContextAttribute, getCheckContextAttribute }
import com.excilys.ebi.gatling.core.check.extractor.ExtractorFactory
import com.excilys.ebi.gatling.core.check.{ CheckOneBuilder, CheckMultipleBuilder }
import com.excilys.ebi.gatling.core.session.Session
import com.excilys.ebi.gatling.http.check.HttpMultipleCheckBuilder
import com.excilys.ebi.gatling.http.request.HttpPhase.CompletePageReceived
import com.excilys.ebi.gatling.vtd.check.extractor.{ VTDXPathExtractor, MultiVTDXPathExtractor }
import com.ning.http.client.Response
import com.ximpleware.{ VTDNav, CustomVTDGen, AutoPilot }

import HttpBodyVTDXPathCheckBuilder.HTTP_RESPONSE_BODY_VTD_CHECK_CONTEXT_KEY

object HttpBodyVTDXPathCheckBuilder {

	val HTTP_RESPONSE_BODY_VTD_CHECK_CONTEXT_KEY = "httpResponseBodyVtd"

	def vtdXpath(what: Session => String) = new HttpBodyVTDXPathCheckBuilder(what)
}

/**
 * This class builds a response body check based on regular expressions
 *
 * @param what the function returning the expression representing what is to be checked
 * @param strategy the strategy used to check
 * @param expected the expected value against which the extracted value will be checked
 * @param saveAs the optional session key in which the extracted value will be stored
 */
class HttpBodyVTDXPathCheckBuilder(what: Session => String) extends HttpMultipleCheckBuilder[String](what, CompletePageReceived) {

	def find = find(0)

	def getVtdResources(response: Response) = getCheckContextAttribute(HTTP_RESPONSE_BODY_VTD_CHECK_CONTEXT_KEY).getOrElse {
		val vtdEngine = new CustomVTDGen
		vtdEngine.setDoc(response.getResponseBodyAsBytes)
		vtdEngine.parse(false)
		val vn: VTDNav = vtdEngine.getNav
		val ap: AutoPilot = new AutoPilot(vn)
		setAndReturnCheckContextAttribute(HTTP_RESPONSE_BODY_VTD_CHECK_CONTEXT_KEY, (vn, ap))
	}

	def find(occurence: Int) = new CheckOneBuilder(checkBuildFunction[String], new ExtractorFactory[Response, String] {
		def getExtractor(response: Response) = {
			val (vn, ap) = getVtdResources(response)
			new VTDXPathExtractor(vn, ap, occurence)
		}
	})

	def findAll = new CheckMultipleBuilder(checkBuildFunction[List[String]], new ExtractorFactory[Response, List[String]] {
		def getExtractor(response: Response) = {
			val (vn, ap) = getVtdResources(response)
			new MultiVTDXPathExtractor(vn, ap)
		}
	})
}