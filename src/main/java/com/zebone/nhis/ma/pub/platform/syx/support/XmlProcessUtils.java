package com.zebone.nhis.ma.pub.platform.syx.support;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.thoughtworks.xstream.XStream;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;
import com.zebone.nhis.ma.pub.platform.syx.vo.Response;
import com.zebone.platform.modules.exception.BusException;

public class XmlProcessUtils {
	
	
	/**
	 * xml转换成请求Response对象
	 * @param xmlInput 平台响应xml信息
	 * @param resHeader  接收某种消息响应信息的消息模型，
	 * 即<MCCI_IN000002UV01 ITSVersion="XML_1.0" 
	 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	 * xmlns="urn:hl7-org:v3" 
	 * xsi:schemaLocation="urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd">
	 * </MCCI_IN000002UV01>
	 * 中的MCCI_IN000002UV01 内容
	 * @return
	 */
	 public static Response toResponseEntity(String xmlInput,String resHeader){
		  XStream xs = new XStream();
		  xs.setMode(XStream.NO_REFERENCES);
		  xs.processAnnotations(new Class[]{Response.class});
		  String st="<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Body>";
		  String end="</soapenv:Body></soapenv:Envelope>";
		  xmlInput = xmlInput.substring(st.length(), xmlInput.length()-end.length());
		  xmlInput = xmlInput.replaceAll(resHeader, "PRVS_IN000004UV01");
		  return (Response)xs.fromXML(xmlInput);
	  }
	  /**
	   * Requset对象转换成request格式的xml
	   * @param r
	   * @return
	   */
	  public static String toRequestXml(Request r,String reqHeader){
		  XStream xs = new XStream();
		  xs.setMode(XStream.NO_REFERENCES);
		  xs.processAnnotations(new Class[]{Request.class});
		  String str = xs.toXML(r);
		  str = str.replaceAll("PRPA__IN201311UV02", reqHeader);
		  str = str.replaceAll("XMLNS__XSI", "xmlns:xsi");
		  str = str.replaceAll("XSI__SCHEMALOCATION", "xsi:schemaLocation");
		  str = str.replaceAll("XSI__TYPE", "xsi:type");
		  str = str.replaceAll("XSI__NIL", "xsi:nil");
		  return str;
	  }
	  
		public static String beanToXml(Object obj, Class<?> load) {
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(load);
				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			return writer.toString();
			} catch (JAXBException e) {
				throw new BusException(load.getName() + "转成xml时解析错误");
			}
		}
	  
	  /**
		 * xml转bean对象
		 * 
		 * @param xmlPath
		 * @param load
		 * @return
		 */
		public static Object XmlToBean(String xmlPath, Class<?> load) {
			JAXBContext jContext = null;
			Object object = null;
			try {
				jContext = JAXBContext.newInstance(load);
				Unmarshaller unmarshaller = jContext.createUnmarshaller();
				object = unmarshaller.unmarshal(new StringReader(xmlPath));
			} catch (JAXBException e) {
				throw new BusException(load.getName() + "转成bean对象时解析错误");
			}
			return object;
		}
}
