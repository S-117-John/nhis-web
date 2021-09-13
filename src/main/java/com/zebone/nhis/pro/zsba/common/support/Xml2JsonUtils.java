package com.zebone.nhis.pro.zsba.common.support;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * XML字符串转Json
 * @author zim
 *
 */
public class Xml2JsonUtils {

	/** 
     * 转换一个xml格式的字符串到json格式 
     * @param xml  xml格式的字符串 
     * @return 成功返回json 格式的字符串;失败反回null 
	 * @throws IOException 
	 * @throws JDOMException 
     */  
    public static  String xml2JSON(String xml) throws JDOMException, IOException {  
        JSONObject obj = new JSONObject();  
        InputStream is = new ByteArrayInputStream(xml.getBytes("GBK"));  
        SAXBuilder sb = new SAXBuilder();  
        Document doc = sb.build(is);  
        Element root = doc.getRootElement();  
        obj.put(root.getName(), iterateElement(root));  
        return obj.toString();  
    }  
  
    /** 
     * 转换一个xml格式的字符串到json格式 
     * @param file  java.io.File实例是一个有效的xml文件 
     * @return 成功反回json 格式的字符串;失败反回null 
     */  
    public static String xml2JSON(File file) {  
        JSONObject obj = new JSONObject();  
        try {  
            SAXBuilder sb = new SAXBuilder();  
            Document doc = sb.build(file);  
            Element root = doc.getRootElement();  
            obj.put(root.getName(), iterateElement(root));  
            return obj.toString();  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    /** 
     * 一个迭代方法 
     * @param element: org.jdom.Element的文档对象
     * @return java.util.Map 实例 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    private static Map  iterateElement(Element element) {  
        List jiedian = element.getChildren();  
        Element et = null;  
        Map obj = new HashMap();  
        List list = null;  
        for (int i = 0; i < jiedian.size(); i++) {  
            list = new LinkedList();  
            et = (Element) jiedian.get(i);  
            if (et.getTextTrim().equals("")) {  
                if (et.getChildren().size() == 0)  
                    continue;  
                if (obj.containsKey(et.getName())) {  
                    list = (List) obj.get(et.getName());  
                }
                list.add(iterateElement(et));  
                obj.put(et.getName(), list);  
            } else {  
                if (obj.containsKey(et.getName())) {  
                    list = (List) obj.get(et.getName());  
                    list.add(et.getTextTrim());  
                    obj.put(et.getName(), list);  
                }else{
                    obj.put(et.getName(), et.getTextTrim());  
                }
            }  
        }  
        return obj;  
    }  
  
    // 测试  
    public static void main(String[] args) {  
       try {
    	   System.out.println(Xml2JsonUtils.xml2JSON("<MapSet>"  
                   + "<MapGroup id='Sheboygan'>" + "<Map>"  
                   + "<Type>MapGuideddddddd</Type>"  
                   + "<SingleTile>true</SingleTile>" + "<Extension>"  
                   + "<ResourceId>ddd</ResourceId>" + "</Extension>" + "</Map>"  
                   + "<Map>" + "<Type>ccc</Type>" + "<SingleTile>ggg</SingleTile>"  
                   + "<Extension>" + "<ResourceId>aaa</ResourceId>"  
                   + "</Extension>" + "</Map>" + "<Extension />" + "</MapGroup>"  
                   + "<ddd>" + "33333333" + "</ddd>" + "<ddd>" + "444" + "</ddd>"  
                   + "</MapSet>"));  
		} catch (Exception e) {
			e.printStackTrace();
		}
    } 
	
}
