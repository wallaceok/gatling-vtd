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
import com.excilys.ebi.gatling.http.check.body.HttpBodyCheckBuilder
import com.excilys.ebi.gatling.vtd.check.extractor.VtdXPathExtractor
import com.ning.http.client.Response
object HttpBodyVtdXPathCheckBuilder {

	def vtdXpath(expression: EvaluatableString, namespaces: List[(String, String)]) = new HttpBodyCheckBuilder(findExtractorFactory(namespaces), findAllExtractorFactory(namespaces), countExtractorFactory(namespaces), expression)

	private val HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY = "HttpBodyVtdXPathExtractor"

	private def getCachedExtractor(response: Response) = getOrUpdateCheckContextAttribute(HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY, new VtdXPathExtractor(response.getResponseBodyAsBytes))

	private def findExtractorFactory(namespaces: List[(String, String)])(occurrence: Int): ExtractorFactory[Response, String] = (response: Response) => getCachedExtractor(response).extractOne(occurrence, namespaces)

	private def findAllExtractorFactory(namespaces: List[(String, String)]): ExtractorFactory[Response, Seq[String]] = (response: Response) => getCachedExtractor(response).extractMultiple(namespaces)

	private def countExtractorFactory(namespaces: List[(String, String)]): ExtractorFactory[Response, Int] = (response: Response) => getCachedExtractor(response).count(namespaces)
}