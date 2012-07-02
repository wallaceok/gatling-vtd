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

import com.excilys.ebi.gatling.core.check.CheckContext.getOrUpdateCheckContextAttribute
import com.excilys.ebi.gatling.core.check.ExtractorFactory
import com.excilys.ebi.gatling.core.session.EvaluatableString
import com.excilys.ebi.gatling.http.check.HttpMultipleCheckBuilder
import com.excilys.ebi.gatling.http.request.HttpPhase.CompletePageReceived
import com.excilys.ebi.gatling.http.response.ExtendedResponse
import com.excilys.ebi.gatling.vtd.check.extractor.VtdXPathExtractor

object HttpBodyVtdXPathCheckBuilder {

	private val HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY = "HttpBodyVtdXPathExtractor"

	private def getCachedExtractor(ExtendedResponse: ExtendedResponse) = getOrUpdateCheckContextAttribute(HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY, new VtdXPathExtractor(ExtendedResponse.getResponseBodyAsBytes))

	private def findExtractorFactory(namespaces: List[(String, String)])(occurrence: Int): ExtractorFactory[ExtendedResponse, String, String] = (ExtendedResponse: ExtendedResponse) => getCachedExtractor(ExtendedResponse).extractOne(occurrence, namespaces)

	private def findAllExtractorFactory(namespaces: List[(String, String)]): ExtractorFactory[ExtendedResponse, String, Seq[String]] = (ExtendedResponse: ExtendedResponse) => getCachedExtractor(ExtendedResponse).extractMultiple(namespaces)

	private def countExtractorFactory(namespaces: List[(String, String)]): ExtractorFactory[ExtendedResponse, String, Int] = (ExtendedResponse: ExtendedResponse) => getCachedExtractor(ExtendedResponse).count(namespaces)

	def vtdXpath(expression: EvaluatableString, namespaces: List[(String, String)]) = new HttpMultipleCheckBuilder(findExtractorFactory(namespaces), findAllExtractorFactory(namespaces), countExtractorFactory(namespaces), expression, CompletePageReceived)
}