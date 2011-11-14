package com.excilys.ebi.gatling.vtd.http.check.body.extractor
import com.excilys.ebi.gatling.core.check.extractor.ExtractorFactory
import com.excilys.ebi.gatling.vtd.check.extractor.VTDXPathExtractor
import com.ning.http.client.Response

class HttpBodyVTDXPathExtractorFactory(occurence: Int) extends ExtractorFactory[Response] {

	def getExtractor(response: Response) = {
		logger.debug("Instantiation of VTDXPathExtractor")
		new VTDXPathExtractor(response.getResponseBodyAsBytes, occurence)
	}
}