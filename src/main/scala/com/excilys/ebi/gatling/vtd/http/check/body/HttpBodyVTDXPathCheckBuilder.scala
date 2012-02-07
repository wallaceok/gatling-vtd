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
import com.excilys.ebi.gatling.core.check.{ CheckOneBuilder, CheckMultipleBuilder }
import com.excilys.ebi.gatling.core.session.Session
import com.excilys.ebi.gatling.http.check.HttpMultipleCheckBuilder
import com.excilys.ebi.gatling.http.request.HttpPhase.CompletePageReceived
import com.excilys.ebi.gatling.vtd.check.extractor.VTDXPathExtractor
import com.ning.http.client.Response

import HttpBodyVTDXPathCheckBuilder.HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY

object HttpBodyVTDXPathCheckBuilder {

	val HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY = "HttpBodyVTDXPathExtractor"

	def vtdXpath(expression: Session => String) = new HttpBodyVTDXPathCheckBuilder(expression)
}

/**
 * This class builds a response body check based on regular expressions
 *
 * @param expression the function returning the expression representing expression is to be checked
 */
class HttpBodyVTDXPathCheckBuilder(expression: Session => String) extends HttpMultipleCheckBuilder[String](expression, CompletePageReceived) {

	def find = find(0)

	def getCachedExtractor(response: Response) = getCheckContextAttribute(HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY).getOrElse {
		setAndReturnCheckContextAttribute(HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY, new VTDXPathExtractor(response.getResponseBodyAsBytes))
	}

	def find(occurrence: Int) = new CheckOneBuilder(checkBuildFunction, (response: Response) => getCachedExtractor(response).extractOne(occurrence))

	def findAll = new CheckMultipleBuilder(checkBuildFunction, (response: Response) => getCachedExtractor(response).extractMultiple)

	def count = new CheckOneBuilder(checkBuildFunction, (response: Response) => getCachedExtractor(response).count)
}