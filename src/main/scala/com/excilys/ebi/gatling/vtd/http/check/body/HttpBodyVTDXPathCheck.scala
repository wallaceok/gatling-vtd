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
package com.excilys.ebi.gatling.vtd.http.check.body

import com.excilys.ebi.gatling.core.check.strategy.CheckStrategy
import com.excilys.ebi.gatling.core.session.Session
import com.excilys.ebi.gatling.http.check.HttpCheck
import com.excilys.ebi.gatling.http.request.HttpPhase.CompletePageReceived
import com.excilys.ebi.gatling.vtd.http.check.body.extractor.HttpBodyVTDXPathExtractorFactory

/**
 * This class represents a check made on the body of the response with XPath expressions
 *
 * @param what the function returning the XPath expression
 * @param to the optional context key in which the extracted value will be stored
 * @param strategy the strategy used to check
 * @param expected the expected value against which the extracted value will be checked
 *
 * @author <a href="mailto:slandelle@excilys.com">Stephane Landelle</a>
 */
class HttpBodyVTDXPathCheck(what: Session => String, occurrence: Option[Int], strategy: CheckStrategy, expected: List[String], saveAs: Option[String])
		extends HttpCheck(what, new HttpBodyVTDXPathExtractorFactory(occurrence), strategy, expected, saveAs, CompletePageReceived) {
}