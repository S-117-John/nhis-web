package com.zebone.nhis.webservice.client.zhongshan.tpi.rhip;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * <p>
 * An example of how this class may be used:
 * 
 * <pre>
 * WebServiceEntryService service = new WebServiceEntryService();
 * WebServiceEntry portType = service.getWebServiceEntryPort();
 * portType.invoke(...);
 * </pre>
 * 
 * </p>
 * 
 */
@WebServiceClient(name = "WebServiceEntryService", targetNamespace = "http://ws.adapter.bsoft.com/", wsdlLocation = "http://192.168.0.75:8203/WebServiceEntry?wsdl")
public class WebServiceEntryService extends Service {

	private final static URL WEBSERVICEENTRYSERVICE_WSDL_LOCATION;
	private final static Logger logger = Logger
			.getLogger(com.zebone.nhis.webservice.client.zhongshan.tpi.rhip.WebServiceEntryService.class
					.getName());

	static {
		URL url = null;
		try {
			URL baseUrl;
			baseUrl = com.zebone.nhis.webservice.client.zhongshan.tpi.rhip.WebServiceEntryService.class
					.getResource(".");
			url = new URL(baseUrl,
					"http://192.168.0.75:8203/WebServiceEntry?wsdl");
		} catch (MalformedURLException e) {
			logger.warning("Failed to create URL for the wsdl Location: 'http://192.168.0.75:8203/WebServiceEntry?wsdl', retrying as a local file");
			logger.warning(e.getMessage());
		}
		WEBSERVICEENTRYSERVICE_WSDL_LOCATION = url;
	}

	public WebServiceEntryService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public WebServiceEntryService() {
		super(WEBSERVICEENTRYSERVICE_WSDL_LOCATION, new QName(
				"http://ws.adapter.bsoft.com/", "WebServiceEntryService"));
	}

	/**
	 * 
	 * @return returns WebServiceEntry
	 */
	@WebEndpoint(name = "WebServiceEntryPort")
	public WebServiceEntry getWebServiceEntryPort() {
		return super.getPort(new QName("http://ws.adapter.bsoft.com/",
				"WebServiceEntryPort"), WebServiceEntry.class);
	}

}
