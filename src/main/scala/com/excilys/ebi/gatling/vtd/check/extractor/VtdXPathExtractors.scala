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

import scala.annotation.tailrec

import com.excilys.ebi.gatling.core.check.Extractor
import com.excilys.ebi.gatling.core.validation.{ SuccessWrapper, Validation }
import com.ximpleware.{ AutoPilot, CustomVTDGen, VTDNav }
import com.ximpleware.VTDNav.{ TOKEN_ATTR_NAME, TOKEN_PI_NAME, TOKEN_PI_VAL, TOKEN_STARTING_TAG }

object VtdXPathExtractors {

	def parse(bytes: Array[Byte]): (VTDNav, AutoPilot) = {
		val vtdEngine = new CustomVTDGen
		vtdEngine.setDoc(bytes)
		vtdEngine.parse(false)
		val vn = vtdEngine.getNav
		(vn, new AutoPilot(vn))
	}

	abstract class VtdXPathExtractor[X] extends Extractor[Option[(VTDNav, AutoPilot)], String, X] {
		val name = "vtd"
	}

	private def useXpath[X](ap: AutoPilot, expression: String, namespaces: List[(String, String)])(f: => X): X = {
		namespaces.foreach {
			case (prefix, uri) => ap.declareXPathNameSpace(prefix, uri)
		}
		ap.selectXPath(expression)
		val values = f
		ap.resetXPath
		values
	}

	private def getTextIndex(index: Int, vn: VTDNav) = {
		vn.getTokenType(index) match {
			case TOKEN_ATTR_NAME => index + 1
			case TOKEN_STARTING_TAG => vn.getText
			case TOKEN_PI_NAME => {
				if (index + 1 < vn.getTokenCount && vn.getTokenType(index + 1) == TOKEN_PI_VAL)
					index + 1
				else
					index - 1
			}
			case x => throw new IllegalArgumentException("Unknown token type " + x)
		}
	}

	val extractOne = (namespaces: List[(String, String)]) => (occurrence: Int) => new VtdXPathExtractor[String] {

		def apply(prepared: Option[(VTDNav, AutoPilot)], criterion: String): Validation[Option[String]] = {

			@tailrec
			def extractOneRec(vn: VTDNav, ap: AutoPilot, occurrence: Int): Option[String] = {
				val index = ap.evalXPath
				if (index == -1)
					None
				else if (occurrence == 0) {
					val textIndex = getTextIndex(index, vn)
					if (textIndex != -1) Some(vn.toString(textIndex)) else None
				} else
					extractOneRec(vn, ap, occurrence - 1)
			}

			val result = for {
				(vn, ap) <- prepared
				result <- useXpath(ap, criterion, namespaces) { extractOneRec(vn, ap, occurrence) }
			} yield result

			result.success
		}
	}

	val extractMultiple = (namespaces: List[(String, String)]) => new VtdXPathExtractor[Seq[String]] {

		def apply(prepared: Option[(VTDNav, AutoPilot)], criterion: String): Validation[Option[Seq[String]]] = {

			@tailrec
			def extractMultipleRec(vn: VTDNav, ap: AutoPilot, results: List[String]): List[String] = {
				val index = ap.evalXPath
				if (index == -1)
					results
				else {
					val textIndex = getTextIndex(index, vn)
					val result = if (textIndex != -1) vn.toString(textIndex) else ""
					val newResults = if (!result.isEmpty) result :: results else results
					extractMultipleRec(vn, ap, newResults)
				}
			}

			val result = for {
				(vn, ap) <- prepared
				result = useXpath(ap, criterion, namespaces) { extractMultipleRec(vn, ap, Nil) }
			} yield result

			result.success
		}
	}

	val count = (namespaces: List[(String, String)]) => new VtdXPathExtractor[Int] {

		def apply(prepared: Option[(VTDNav, AutoPilot)], criterion: String): Validation[Option[Int]] =
			extractMultiple(namespaces)(prepared, criterion).map(_.map(_.size))
	}
}