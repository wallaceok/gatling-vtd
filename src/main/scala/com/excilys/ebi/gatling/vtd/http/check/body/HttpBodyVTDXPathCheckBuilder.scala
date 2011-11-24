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

import com.excilys.ebi.gatling.core.check.strategy.{ExistenceCheckStrategy, CheckStrategy}
import com.excilys.ebi.gatling.core.context.Context
import com.excilys.ebi.gatling.core.util.StringHelper.interpolate
import com.excilys.ebi.gatling.http.check.HttpCheckBuilder
import com.excilys.ebi.gatling.http.request.HttpPhase.{HttpPhase, CompletePageReceived}
import com.excilys.ebi.gatling.core.check.CheckBuilderSave
import com.excilys.ebi.gatling.core.check.CheckBuilderVerify
import com.excilys.ebi.gatling.core.check.CheckBuilderFind
import com.excilys.ebi.gatling.core.check.CheckBuilderVerifyAll
import com.excilys.ebi.gatling.core.check.CheckBuilderVerifyOne

/**
 * @author <a href="mailto:slandelle@excilys.com">Stephane Landelle</a>
 */
object HttpBodyVTDXPathCheckBuilder {
	/**
	 *
	 */
	def vtdXpath(what: Context => String) = new HttpBodyVTDXPathCheckBuilder(what, Some(0), ExistenceCheckStrategy, Nil, None) with CheckBuilderFind[HttpCheckBuilder[HttpBodyVTDXPathCheckBuilder]]
	/**
	 *
	 */
	def vtdXpath(expression: String): HttpBodyVTDXPathCheckBuilder with CheckBuilderFind[HttpCheckBuilder[HttpBodyVTDXPathCheckBuilder]] = vtdXpath(interpolate(expression))
}

/**
 * This class builds a response body check based on XPath expressions
 *
 * @param what the function returning the expression representing what is to be checked
 * @param to the optional context key in which the extracted value will be stored
 * @param strategy the strategy used to check
 * @param expected the expected value against which the extracted value will be checked
 */
class HttpBodyVTDXPathCheckBuilder(what: Context => String, occurrence: Option[Int], strategy: CheckStrategy, expected: List[String], saveAs: Option[String])
		extends HttpCheckBuilder[HttpBodyVTDXPathCheckBuilder](what, occurrence, strategy, expected, saveAs, CompletePageReceived) {

	def newInstance(what: Context => String, occurrence: Option[Int], strategy: CheckStrategy, expected: List[String], saveAs: Option[String], when: HttpPhase) =
		new HttpBodyVTDXPathCheckBuilder(what, occurrence, strategy, expected, saveAs)

	def newInstanceWithFindOne(occurrence: Int) =
		new HttpBodyVTDXPathCheckBuilder(what, Some(occurrence), strategy, expected, saveAs) with CheckBuilderVerifyOne[HttpCheckBuilder[HttpBodyVTDXPathCheckBuilder]]

	def newInstanceWithFindAll =
		new HttpBodyVTDXPathCheckBuilder(what, None, strategy, expected, saveAs) with CheckBuilderVerifyAll[HttpCheckBuilder[HttpBodyVTDXPathCheckBuilder]]

	def newInstanceWithVerify(strategy: CheckStrategy, expected: List[String] = Nil) =
		new HttpBodyVTDXPathCheckBuilder(what, occurrence, strategy, expected, saveAs) with CheckBuilderSave[HttpCheckBuilder[HttpBodyVTDXPathCheckBuilder]]

	def build = new HttpBodyVTDXPathCheck(what, occurrence, strategy, expected, saveAs)
}
