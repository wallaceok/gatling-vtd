/**
 * Copyright 2011-2014 eBusiness Information, Groupe Excilys (www.excilys.com)
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
package io.gatling.vtd.http.check.body

import com.typesafe.scalalogging.slf4j.StrictLogging
import com.ximpleware.{ AutoPilot, VTDNav }

import io.gatling.core.check.Preparer
import io.gatling.core.session.{ Expression, RichExpression }
import io.gatling.core.validation.{ FailureWrapper, SuccessWrapper }
import io.gatling.http.check.{ HttpCheckBuilders, HttpMultipleCheckBuilder }
import io.gatling.http.response.Response
import io.gatling.vtd.check.extractor.{ CountVtdXPathExtractor, MultipleVtdXPathExtractor, SingleVtdXPathExtractor, VtdXPathExtractor }

object HttpBodyVtdXPathCheckBuilder extends StrictLogging {

	private val preparer: Preparer[Response, Option[(VTDNav, AutoPilot)]] = (response: Response) =>
		try {
			val bytes = response.bodyBytes
			Some(VtdXPathExtractor.parse(bytes)).success

		} catch {
			case e: Exception =>
				val message = s"Could not parse response with VTD-XML: ${e.getMessage}"
				logger.info(message, e)
				message.failure
		}

	def vtdXpath(expression: Expression[String], namespaces: List[(String, String)]) =
		new HttpMultipleCheckBuilder[Option[(VTDNav, AutoPilot)], String](HttpCheckBuilders.bodyCheckFactory, preparer) {
			def findExtractor(occurrence: Int) = expression.map(new SingleVtdXPathExtractor(_, namespaces, occurrence))
			def findAllExtractor = expression.map(new MultipleVtdXPathExtractor(_, namespaces))
			def countExtractor = expression.map(new CountVtdXPathExtractor(_, namespaces))
		}
}
