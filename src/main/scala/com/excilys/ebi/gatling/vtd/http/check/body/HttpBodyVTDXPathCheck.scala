package com.excilys.ebi.gatling.vtd.http.check.body

import com.excilys.ebi.gatling.core.check.strategy.CheckStrategy
import com.excilys.ebi.gatling.core.context.Context
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
 */
class HttpBodyVTDXPathCheck(what: (Context => String, Int), to: Option[String], strategy: CheckStrategy, expected: Option[String])
		extends HttpCheck(what._1, new HttpBodyVTDXPathExtractorFactory(what._2), to, strategy, expected, CompletePageReceived) {
}