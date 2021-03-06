package com.zebone.nhis.webservice.client.zhongshan.pacs;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by Apache CXF 3.0.13 2017-05-18T15:17:19.474+08:00
 * Generated source version: 3.0.13
 * 
 */
@WebServiceClient(name = "MathService", targetNamespace = "http://www.gdpacs.com/")
public class MathService extends javax.xml.ws.Service {

	public final static URL WSDL_LOCATION = null;

	public final static QName SERVICE = new QName("http://www.gdpacs.com/", "MathService");

	public final static QName MathServiceSoap12 = new QName("http://www.gdpacs.com/", "MathServiceSoap12");

	public final static QName MathServiceSoap = new QName("http://www.gdpacs.com/", "MathServiceSoap");

	public MathService(URL wsdlLocation) {

		super(wsdlLocation, SERVICE);
	}

	public MathService(URL wsdlLocation, QName serviceName) {

		super(wsdlLocation, serviceName);
	}

	public MathService() {

		super(WSDL_LOCATION, SERVICE);
	}

	// This constructor requires JAX-WS API 2.2. You will need to endorse the
	// 2.2
	// API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS
	// 2.1
	// compliant code instead.
	public MathService(WebServiceFeature... features) {

		super(WSDL_LOCATION, SERVICE, features);
	}

	// This constructor requires JAX-WS API 2.2. You will need to endorse the
	// 2.2
	// API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS
	// 2.1
	// compliant code instead.
	public MathService(URL wsdlLocation, WebServiceFeature... features) {

		super(wsdlLocation, SERVICE, features);
	}

	// This constructor requires JAX-WS API 2.2. You will need to endorse the
	// 2.2
	// API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS
	// 2.1
	// compliant code instead.
	public MathService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {

		super(wsdlLocation, serviceName, features);
	}

	/**
	 * 
	 * @return returns MathServiceSoap
	 */
	@WebEndpoint(name = "MathServiceSoap12")
	public MathServiceSoap getMathServiceSoap12() {

		return super.getPort(MathServiceSoap12, MathServiceSoap.class);
	}

	/**
	 * 
	 * @param features
	 *            A list of {@link javax.xml.ws.WebServiceFeature} to configure
	 *            on the proxy. Supported features not in the
	 *            <code>features</code> parameter will have their default
	 *            values.
	 * @return returns MathServiceSoap
	 */
	@WebEndpoint(name = "MathServiceSoap12")
	public MathServiceSoap getMathServiceSoap12(WebServiceFeature... features) {

		return super.getPort(MathServiceSoap12, MathServiceSoap.class, features);
	}

	/**
	 * 
	 * @return returns MathServiceSoap
	 */
	@WebEndpoint(name = "MathServiceSoap")
	public MathServiceSoap getMathServiceSoap() {

		return super.getPort(MathServiceSoap, MathServiceSoap.class);
	}

	/**
	 * 
	 * @param features
	 *            A list of {@link javax.xml.ws.WebServiceFeature} to configure
	 *            on the proxy. Supported features not in the
	 *            <code>features</code> parameter will have their default
	 *            values.
	 * @return returns MathServiceSoap
	 */
	@WebEndpoint(name = "MathServiceSoap")
	public MathServiceSoap getMathServiceSoap(WebServiceFeature... features) {

		return super.getPort(MathServiceSoap, MathServiceSoap.class, features);
	}

}
