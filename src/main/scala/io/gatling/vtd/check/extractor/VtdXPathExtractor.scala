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
package io.gatling.vtd.check.extractor

import scala.annotation.tailrec

import com.ximpleware.{ AutoPilot, CustomVTDGen, VTDNav }
import com.ximpleware.VTDNav.{ TOKEN_ATTR_NAME, TOKEN_PI_NAME, TOKEN_PI_VAL, TOKEN_STARTING_TAG }

import io.gatling.core.check.extractor.{ CriterionExtractor, LiftedSeqOption }
import io.gatling.core.validation.{ SuccessWrapper, Validation }

object VtdXPathExtractor {

  def parse(bytes: Array[Byte]): (VTDNav, AutoPilot) = {
    val vtdEngine = new CustomVTDGen
    vtdEngine.setDoc(bytes)
    vtdEngine.parse(false)
    val vn = vtdEngine.getNav
    (vn, new AutoPilot(vn))
  }

  def useXpath[X](ap: AutoPilot, expression: String, namespaces: List[(String, String)])(f: => X): X = {
    namespaces.foreach {
      case (prefix, uri) => ap.declareXPathNameSpace(prefix, uri)
    }
    ap.selectXPath(expression)
    val values = f
    ap.resetXPath()
    values
  }

  def getTextIndex(index: Int, vn: VTDNav) = {
    vn.getTokenType(index) match {
      case TOKEN_ATTR_NAME    => index + 1
      case TOKEN_STARTING_TAG => vn.getText
      case TOKEN_PI_NAME =>
        if (index + 1 < vn.getTokenCount && vn.getTokenType(index + 1) == TOKEN_PI_VAL)
          index + 1
        else
          index - 1
      case x => throw new IllegalArgumentException("Unknown token type " + x)
    }
  }

  def extractAll(criterion: String, namespaces: List[(String, String)], vn: VTDNav, ap: AutoPilot): Seq[String] = {

      @tailrec
      def extractAllRec(vn: VTDNav, ap: AutoPilot, results: List[String]): List[String] = {
        val index = ap.evalXPath
        if (index == -1)
          results
        else {
          val textIndex = VtdXPathExtractor.getTextIndex(index, vn)
          val result = if (textIndex != -1) vn.toString(textIndex) else ""
          val newResults = if (!result.isEmpty) result :: results else results
          extractAllRec(vn, ap, newResults)
        }
      }

    useXpath(ap, criterion, namespaces) { extractAllRec(vn, ap, Nil) }
  }
}

abstract class VtdXPathExtractor[X] extends CriterionExtractor[Option[(VTDNav, AutoPilot)], String, X] { val criterionName = "vtd" }

class SingleVtdXPathExtractor(val criterion: String, namespaces: List[(String, String)], occurrence: Int) extends VtdXPathExtractor[String] {

  def extract(prepared: Option[(VTDNav, AutoPilot)]): Validation[Option[String]] = {

      @tailrec
      def extractOneRec(vn: VTDNav, ap: AutoPilot, occurrence: Int): Option[String] = {
        val index = ap.evalXPath
        if (index == -1)
          None
        else if (occurrence == 0) {
          val textIndex = VtdXPathExtractor.getTextIndex(index, vn)
          if (textIndex != -1) Some(vn.toString(textIndex)) else None
        } else
          extractOneRec(vn, ap, occurrence - 1)
      }

    val result = for {
      (vn, ap) <- prepared
      result <- VtdXPathExtractor.useXpath(ap, criterion, namespaces) { extractOneRec(vn, ap, occurrence) }
    } yield result

    result.success
  }
}

class MultipleVtdXPathExtractor(val criterion: String, namespaces: List[(String, String)]) extends VtdXPathExtractor[Seq[String]] {

  def extract(prepared: Option[(VTDNav, AutoPilot)]): Validation[Option[Seq[String]]] = {

    val result = for {
      (vn, ap) <- prepared
      result <- VtdXPathExtractor.extractAll(criterion, namespaces, vn, ap).liftSeqOption
    } yield result

    result.success
  }
}

class CountVtdXPathExtractor(val criterion: String, namespaces: List[(String, String)]) extends VtdXPathExtractor[Int] {

  def extract(prepared: Option[(VTDNav, AutoPilot)]): Validation[Option[Int]] = {
    val count = prepared.map { case (vn, ap) => VtdXPathExtractor.extractAll(criterion, namespaces, vn, ap).size }.getOrElse(0)
    Some(count).success
  }
}
