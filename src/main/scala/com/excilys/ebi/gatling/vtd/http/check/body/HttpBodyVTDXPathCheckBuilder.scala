package com.excilys.ebi.gatling.vtd.http.check.body
import com.excilys.ebi.gatling.core.check.strategy.{NonExistenceCheckStrategy, NonEqualityCheckStrategy, ExistenceCheckStrategy, EqualityCheckStrategy, CheckStrategy}
import com.excilys.ebi.gatling.core.context.Context
import com.excilys.ebi.gatling.core.util.StringHelper.EMPTY
import com.excilys.ebi.gatling.http.check.body.HttpBodyXPathCheckBuilder
import com.excilys.ebi.gatling.http.check.HttpCheckBuilder
import com.excilys.ebi.gatling.http.request.HttpPhase.{HttpPhase, CompletePageReceived}


object HttpBodyVTDXPathCheckBuilder {

		/**
	 * Will check if the value extracted via an XPath expression is equal to a specified value
	 *
	 * @param what a function returning the XPath expression
	 * @param occurrence the occurrence of the XPath expression that should be extracted
	 * @param expected the value expected
	 */
	def vtdXPathEquals(what: Context => String, occurrence: Int, expected: String) = new HttpBodyXPathCheckBuilder((what, occurrence), Some(EMPTY), EqualityCheckStrategy, Some(expected))
	/**
	 * Will check if the value extracted via an XPath expression is equal to a specified value
	 *
	 * The first occurrence of the XPath expression will be extracted
	 *
	 * @param what a function returning the XPath expression
	 * @param expected the value expected
	 */
	def vtdXPathEquals(what: Context => String, expected: String): HttpBodyXPathCheckBuilder = vtdXPathEquals(what, 0, expected)
	/**
	 * Will check if the value extracted via an XPath expression is equal to a specified value
	 *
	 * @param expression the XPath expression
	 * @param occurrence the occurrence of the XPath expression that should be extracted
	 * @param expected the value expected
	 */
	def vtdXPathEquals(expression: String, occurrence: Int, expected: String): HttpBodyXPathCheckBuilder = vtdXPathEquals(((c: Context) => expression), occurrence, expected)
	/**
	 * Will check if the value extracted via an XPath expression is equal to a specified value
	 *
	 * The first occurrence of the XPath expression will be extracted
	 *
	 * @param expression the XPath expression
	 * @param expected the value expected
	 */
	def vtdXPathEquals(expression: String, expected: String): HttpBodyXPathCheckBuilder = vtdXPathEquals((c: Context) => expression, expected)

	/**
	 * Will check if the value extracted via an XPath expression is different from a specified value
	 *
	 * @param what a function returning the XPath expression
	 * @param occurrence the occurrence of the XPath expression that should be extracted
	 * @param expected the value expected
	 */
	def vtdxpathNotEquals(what: Context => String, occurrence: Int, expected: String) = new HttpBodyXPathCheckBuilder((what, occurrence), Some(EMPTY), NonEqualityCheckStrategy, Some(expected))
	/**
	 * Will check if the value extracted via an XPath expression is different from a specified value
	 *
	 * The first occurrence of the XPath expression will be extracted
	 *
	 * @param what a function returning the XPath expression
	 * @param expected the value expected
	 */
	def vtdxpathNotEquals(what: Context => String, expected: String): HttpBodyXPathCheckBuilder = vtdxpathNotEquals(what, 0, expected)
	/**
	 * Will check if the value extracted via an XPath expression is different from a specified value
	 *
	 * @param expression the XPath expression
	 * @param occurrence the occurrence of the XPath expression that should be extracted
	 * @param expected the value expected
	 */
	def vtdxpathNotEquals(expression: String, occurrence: Int, expected: String): HttpBodyXPathCheckBuilder = vtdxpathNotEquals(((c: Context) => expression), occurrence, expected)
	/**
	 * Will check if the value extracted via an XPath expression is different from a specified value
	 *
	 * The first occurrence of the XPath expression will be extracted
	 *
	 * @param expression the XPath expression
	 * @param expected the value expected
	 */
	def vtdxpathNotEquals(expression: String, expected: String): HttpBodyXPathCheckBuilder = vtdxpathNotEquals((c: Context) => expression, expected)

	/**
	 * Will check if the XPath expression result exists at least occurrence times
	 *
	 * @param what a function returning the XPath expression
	 * @param occurrence the occurrence of the XPath expression that should be extracted
	 */
	def vtdxpathExists(what: Context => String, occurrence: Int) = new HttpBodyXPathCheckBuilder((what, occurrence), Some(EMPTY), ExistenceCheckStrategy, None)
	/**
	 * Will check if the XPath expression result exists
	 *
	 * @param what a function returning the XPath expression
	 */
	def vtdxpathExists(what: Context => String): HttpBodyXPathCheckBuilder = vtdxpathExists(what, 0)
	/**
	 * Will check if the XPath expression result exists at least occurrence times
	 *
	 * @param expression the XPath expression
	 * @param occurrence the occurrence of the XPath expression that should be extracted
	 */
	def vtdxpathExists(expression: String, occurrence: Int): HttpBodyXPathCheckBuilder = vtdxpathExists((c: Context) => expression, occurrence)
	/**
	 * Will check if the XPath expression result exists
	 *
	 * The first occurrence of the XPath expression will be extracted
	 *
	 * @param expression the XPath expression
	 */
	def vtdxpathExists(expression: String): HttpBodyXPathCheckBuilder = vtdxpathExists((c: Context) => expression)

	/**
	 * Will check if the XPath expression result does not exist more than occurrence times
	 *
	 * @param what a function returning the XPath expression
	 * @param occurrence the occurrence of the XPath expression that should be extracted
	 */
	def vtdxpathNotExists(what: Context => String, occurrence: Int) = new HttpBodyXPathCheckBuilder((what, occurrence), Some(EMPTY), NonExistenceCheckStrategy, None)
	/**
	 * Will check if the XPath expression result does not exist
	 *
	 * @param what a function returning the XPath expression
	 */
	def vtdxpathNotExists(what: Context => String): HttpBodyXPathCheckBuilder = vtdxpathNotExists(what, 0)
	/**
	 * Will check if the XPath expression result does not exist more than occurrence times
	 *
	 * @param expression the XPath expression
	 * @param occurrence the occurrence of the XPath expression that should be extracted
	 */
	def vtdxpathNotExists(expression: String, occurrence: Int): HttpBodyXPathCheckBuilder = vtdxpathNotExists((c: Context) => expression, occurrence)
	/**
	 * Will check if the XPath expression result does not exist
	 *
	 * @param expression the XPath expression
	 */
	def vtdxpathNotExists(expression: String): HttpBodyXPathCheckBuilder = vtdxpathNotExists((c: Context) => expression)

	/**
	 * Will capture the occurrence-th result of the XPath expression
	 *
	 * @param what the function returning the XPath expression
	 */
	def vtdxpath(what: Context => String, occurrence: Int) = vtdxpathExists(what, occurrence)
	/**
	 * Will capture the result of the XPath expression
	 *
	 * @param what the function returning the XPath expression
	 */
	def vtdxpath(what: Context => String) = vtdxpathExists(what)
	/**
	 * Will capture the occurrence-th result of the XPath expression
	 *
	 * @param expression the XPath expression
	 */
	def vtdxpath(expression: String, occurrence: Int) = vtdxpathExists(expression, occurrence)
	/**
	 * Will capture the result of the XPath expression
	 *
	 * @param expression the XPath expression
	 */
	def vtdxpath(expression: String) = vtdxpathExists(expression)
}

/**
 * This class builds a response body check based on XPath expressions
 *
 * @param what the function returning the expression representing what is to be checked
 * @param to the optional context key in which the extracted value will be stored
 * @param strategy the strategy used to check
 * @param expected the expected value against which the extracted value will be checked
 */
class HttpBodyVTDXPathCheckBuilder(what: (Context => String, Int), to: Option[String], strategy: CheckStrategy, expected: Option[String])
		extends HttpCheckBuilder[HttpBodyXPathCheckBuilder](what._1, to, strategy, expected, CompletePageReceived) {

	def newInstance(what: Context => String, to: Option[String], strategy: CheckStrategy, expected: Option[String], when: HttpPhase) = new HttpBodyXPathCheckBuilder((what, this.what._2), to, strategy, expected)

	def build = new HttpBodyVTDXPathCheck(what, to, strategy, expected)
}