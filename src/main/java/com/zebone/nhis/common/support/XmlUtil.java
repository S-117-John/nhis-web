package com.zebone.nhis.common.support;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.beanutils.BeanUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.zebone.platform.modules.exception.BusException;

public class XmlUtil {
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
			e.printStackTrace();
			throw new BusException(load.getName() + "转成bean对象时解析错误");
		}
		return object;
	}
	 /**
     *将xml对象转换为javaBean
     * @param clazz
     * @param xml
     * @return
     */
    public static Object XmlToBean(Class clazz,String xml){
        if(xml!=null&&xml!=""){
            Field[] fields = clazz.getDeclaredFields();
            List<Field> fieldList = new ArrayList<Field>();
            for (Field fie : fields) {
                if (fie.isAnnotationPresent(XmlElementAnno.class)) {
                    fieldList.add(fie);
                }
            }
            try {
                StringReader read = new StringReader(xml);
                InputSource source = new InputSource(read);
                //创建一个新的SAXBuilder
                SAXBuilder sb = new SAXBuilder();
                Document doc = sb.build(source);
                //取的根元素
                Element root = doc.getRootElement();
                Object object = clazz.newInstance();
                if(!fieldList.isEmpty()){
                    for (Field field : fieldList) {
                        Element child = root.getChild(field.getName());
                        if(child!=null){
                            BeanUtils.setProperty(object, field.getName(), child.getValue());
                        }
                    }
                }
                return object;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	
	/**
	 * bean对象转xml
	 * 
	 * @param obj
	 * @param load
	 * @return
	 */
	public static String beanToXml(Object obj, Class<?> load) {
		return beanToXml(obj,load,null);
	}

	public static String beanToXml(Object obj, Class<?> load, Map<String,Object> propertys) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(load);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			if(propertys!=null && propertys.size()>0){
				for (Map.Entry<String, Object> et : propertys.entrySet()) {
					marshaller.setProperty(et.getKey(),et.getValue());
				}
			}
			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			return writer.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new BusException(load.getName() + "转成xml时解析错误");
		}
	}
	/**
	 * bean对象转xml
	 * 
	 * @param obj
	 * @param load
	 * @return
	 */
	public static String beanToXml(Object obj, Class<?> load,boolean isCreateHead) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(load);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, !isCreateHead);//为true不创建，为false创建
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new BusException(load.getName() + "转成xml时解析错误"+e.getMessage());
		}
	}

	/**
	 * 时间格式工具
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date parseDate(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			throw new BusException("时间格式转换错误");
		}
	}

	/**截取xml，因为这个XmlUtil不能解析<MCCI_IN000002UV01 xmlns="urn:hl7-org:v3" xmlns:xsi="http://www.w3.org/2001/XMLSchema
	 * -instance" ITSVersion="XML_1.0" xsi:schemaLocation="urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd">这样的xml头
	 * 所以我先截取
	 * create by: gao shiheng
	 *
	 * @Param: null
	 * @return
	 */
	public static String interceptionXml(String xml, String beginString, String endString){
		try {
			int beginIndex = 0;
			int endIndex = 0;
			beginIndex = xml.indexOf(beginString);
			endIndex = xml.indexOf(endString);
			return xml.substring(beginIndex, endIndex);

		}catch (Exception e){
			throw new RuntimeException();
		}
	}
}
