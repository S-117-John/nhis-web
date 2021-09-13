package com.zebone.nhis.emr.common;

import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 电子病历公共工具类
 * @author chengjia
 *
 */
public  class EmrUtils {
	private static DocumentBuilder db = null;
	private static XPathFactory factory = XPathFactory.newInstance();
	
	private final static String UNDERLINE = "_";
	
	//将javabean实体类转为map类型，然后返回一个map类型的值
    @SuppressWarnings("unused")
	public static Map<String, Object> beanToMap(Object obj) { 
            Map<String, Object> params = new HashMap<String, Object>(0); 
            try { 
                PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean(); 
                PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj); 
                for (int i = 0; i < descriptors.length; i++) { 
                    String name = descriptors[i].getName(); 
                    
                    if (!"class".equals(name)) { 
                    	Object object=propertyUtilsBean.getNestedProperty(obj, name);
                        params.put(name, object); 
                    } 
                } 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
            return params; 
    }
	public static Document string2doc(String xml) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException{
		if (db == null) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		}
		Document doc = db.parse(new java.io.ByteArrayInputStream(xml.getBytes("utf-8")));
		doc.normalize();
		return doc;
	}
	
	public static Object getXPathElement(Document doc , String path){
		try{
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(path);
			return expr.evaluate(doc,XPathConstants.STRING );

		}catch(Exception ex){

		}
		return null;
	}

    public static NodeList getXPathElementList(Document doc , String path){
        try{
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile(path);
            return (NodeList)expr.evaluate(doc,XPathConstants.NODESET);

        }catch(Exception ex){

        }
        return null;
    }

	
    public static String getWmTcmType(String hosWmTcmType){
        String wmTcmType = null;
        if (hosWmTcmType == "0"){
            //不区分
        }else if (hosWmTcmType == "2"){
            //中医
            wmTcmType = "TCM";
        }else{
            //西医
            wmTcmType = "WM";
        }
        return wmTcmType;
    }
    
	// 图片到byte数组
	public static byte[] image2byte(String path) {
		byte[] data = null;
		FileImageInputStream input = null;
		try {
			input = new FileImageInputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
			output.close();
			input.close();
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
		return data;
	}

	// byte数组到图片
	public static void byte2image(byte[] data, String path) {
		if (data.length < 3 || path.equals(""))
			return;
		try {
			FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
			imageOutput.write(data, 0, data.length);
			imageOutput.close();
			//System.out.println("Make Picture success,Please find image in "	+ path);
		} catch (Exception ex) {
			System.out.println("Exception: " + ex);
			ex.printStackTrace();
		}
	}

	// byte数组到16进制字符串
	public static String byte2string(byte[] data) {
		if (data == null || data.length <= 1)
			return "0x";
		if (data.length > 200000)
			return "0x";
		StringBuffer sb = new StringBuffer();
		int buf[] = new int[data.length];
		// byte数组转化成十进制
		for (int k = 0; k < data.length; k++) {
			buf[k] = data[k] < 0 ? (data[k] + 256) : (data[k]);
		}
		// 十进制转化成十六进制
		for (int k = 0; k < buf.length; k++) {
			if (buf[k] < 16)
				sb.append("0" + Integer.toHexString(buf[k]));
			else
				sb.append(Integer.toHexString(buf[k]));
		}
		return "0x" + sb.toString().toUpperCase();
	}

	// 判断文件是否存在/存在删除
	public static void removeExistFiles(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			//System.out.println("file exists");
			file.delete();
		}

	}

	// 判断文件是否存在
	public static void judeFileExists(File file) {

		if (file.exists()) {
			//System.out.println("file exists");
		} else {
			//System.out.println("file not exists, create it ...");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// 判断文件夹是否存在/不存在创建
	public static void checkDirExists(String filePath) {
		File dir = new File(filePath);
		judeDirExists(dir);

	}
	
	
	/**
	 * 文件夹不存在自动创建
	 * @param filePath
	 */
	public static void checkDirsExists(String filePath) {
		File fp = new File(filePath);  
        // 创建目录  
//        if (!fp.exists()) {  
//            fp.mkdirs();// 目录不存在的情况下，创建目录。  
//        }  
        File file = new File(filePath);
		if(!file.exists()){
			file.getParentFile().mkdirs();
		}
	}
	
	// 判断文件夹是否存在
	public static void judeDirExists(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				//System.out.println("dir exists");
			} else {
				//System.out.println("the same name file exists, can not create dir");
			}
		} else {
			//System.out.println("dir not exists, create it ...");
			file.mkdirs();
		}

	}
	

	/**
	 * 查询病历文档段落及元素内容
	 * @param EmrMedRecVo
	 * @param EmrMedDoc
	 * @param typeCode文档分类编码
	 * @param qryCode 段落或元素编码
	 * @param qryTypeStr 1段落 2元素
	 * @return
	 */
	public static String[] getPatEmrParaText(EmrMedRec medRec,EmrMedDoc medDoc,String typeCode,String qryCode,String qryTypeStr){
		String texts[]=new String[4];
		texts[0] = "";
		String docXml=medDoc.getDocXml();
		if(docXml==null) return texts;
		if(qryCode==null||qryCode.equals("")) return texts;
		if(qryTypeStr==null||qryTypeStr.equals("")) return texts;
		//String flagCourseStr=medRec.getDocType().getFlagCourse();
		String qryType="para";
		if(qryTypeStr==null||qryTypeStr.equals("")||"012".indexOf(qryTypeStr)<=0){
			qryTypeStr="1";
		}
		if(qryTypeStr.equals("0")||qryTypeStr.equals("1")){
			qryType="para";//段落
		}else{
			qryType="element";//元素
		}
//		Boolean flagCourse=qryTypeStr.equals("1")?true:false;
		try {
			Pattern pattern = Pattern.compile("<DocObjContent(.*?)>");
			Matcher matcher = pattern.matcher(docXml);
			if(matcher.find()){
				String str=matcher.group(1);
				docXml=docXml.replace(str,"");
			}
			
			SAXReader reader = new SAXReader();       
			org.dom4j.Document document = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8"))); 
			Element root = document.getRootElement();
			if(qryType.equals("para")){
				if(qryCode.indexOf("de")>=0){
					//段落模式下取元素数据
					String deCode=qryCode.replace("de", "");
					texts = getNodes(root,"element",deCode);
				}else{
					texts = getNodes(root,"para",qryCode);
				}
			}else{
            	//元素
				texts = getNodes(root,"element",qryCode);
			}

			document.toString();
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return texts;
		}

		if(texts!=null&&texts.length>0){
			texts[0]=texts[0].trim();
			texts[0]= texts[0].replaceAll("\\{", "").replaceAll("\\}", "");
			texts[0]= texts[0].replaceAll("(\r?\n()+)", "\r\n");
			texts[0]=texts[0].replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");
		}
		return texts;
	}
	
    /**
     * 从指定节点开始,递归遍历所有子节点
     */
	public static String[] getNodes(Element node,String ctrlType,String qryCode){
		String[] rtns=new String[4];
		rtns[0] = "";
		if(node==null) return rtns;
		String text="";
		String deCodeValue="";
		String valueCode="";
		
		String textCode="";
		
		Element textNode;
		if(ctrlType==null||ctrlType.equals("")) return rtns;
		if(qryCode==null||qryCode.equals("")) return rtns;
		
		String nodeName=node.getName();
//		String nodeValue=node.getTextTrim();
		boolean find=false;
		if(nodeName!=null&&!nodeName.equals("")){
			//当前节点的名称、文本内容和属性
			//System.out.println("当前节点名称："+node.getName());//当前节点名称
			//System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称
			if((ctrlType.equals("para")&&nodeName.equals("Region"))||(ctrlType.equals("element")&&nodeName.equals("Section"))||(ctrlType.equals("element")&&nodeName.equals("NewCtrl"))){
				List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
				for(Attribute attr:listAttr){//遍历当前节点的所有属性
					String name=attr.getName();//属性名称
					String value=attr.getValue();//属性的值
					//System.out.println("属性名称："+name+"属性值："+value);
					if(ctrlType.equals("para")){
						if(name!=null&&!name.equals("")&&value!=null&&!value.equals("")){
							if(name.equals("para_code")&&value.equals(qryCode)){
								find=true;
								break;
							}
						}

					}else{
						if(name!=null&&!name.equals("")&&value!=null&&!value.equals("")){
							//if((name.equals("sec_code")||name.equals("de_code"))&&value.equals(qryCode)){
							if(name.equals("sec_code")){
								if(value.equals(qryCode)||value.equals("SB"+qryCode)){
									find=true;
									break;
								}
							}else if(name.equals("de_code")){
								if(value.equals(qryCode)||value.equals("EB"+qryCode)){
									find=true;
									break;
								}
							}
						}
					}
				}
				if(find){
					textNode=node.element("Content_Text");
					if(node.attribute("de_code_value")!=null){
						deCodeValue=node.attribute("de_code_value").getValue();
						valueCode=node.attribute("value_code").getValue();
					}
					if(node.attribute("droplist_code")!=null){
						textCode=node.attribute("droplist_code").getValue();
					}
					
					if(textNode!=null){
						text=textNode.getTextTrim();
						rtns[0] = text;
						rtns[1] = deCodeValue;
						rtns[2] = valueCode;
						rtns[3] = textCode;
						return rtns;
					}
				}
				
			}
			
		}
		
		//递归遍历当前节点所有的子节点
		List<Element> listElement=node.elements();//所有一级子节点的list
		for(Element e:listElement){//遍历所有一级子节点
			String[] eTexts=getNodes(e,ctrlType,qryCode);//递归
			if(eTexts!=null&&eTexts.length>0&&!eTexts[0].equals("")) return eTexts;
		}
		return rtns;
	}
	
	public static String getDocTxt(String docXml){
		try {
			Pattern pattern = Pattern.compile("<DocObjContent(.*?)>");
			Matcher matcher = pattern.matcher(docXml);
			if(matcher.find()){
				String str=matcher.group(1);
				docXml=docXml.replace(str,"");
			}
			StringBuffer buffer=new StringBuffer();
			
			SAXReader reader = new SAXReader();       
			org.dom4j.Document document = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8"))); 
			Element root = document.getRootElement();
			
			List<Element> listElement=root.elements();//所有一级子节点的list
			for(Element e:listElement){//遍历所有一级子节点
				String nodeName=e.getName();
				if(nodeName!=null&&nodeName.equals("Region")){
					Element textNode=e.element("Content_Text");
					String paraName=e.attributeValue("para_name");
					String text = textNode.getTextTrim();
					if(buffer!=null&&buffer.length()>0){
						if(text!=null&&!text.equals("")){
							buffer.append("\r\n");
							if(paraName!=null&&!paraName.equals("")){
								if(text.indexOf(paraName)>=0){
									buffer.append(text);
								}else{
									buffer.append(paraName+":"+text);
								}
							}
							
						}
						
						
					}else{
						buffer.append(text);
					}
				}
			}
			String docTxt=buffer.toString();
			if(docTxt!=null){
				docTxt=docTxt.replaceAll("\\{", "").replaceAll("\\}", "");
				docTxt = docTxt.replaceAll("(\r?\n()+)", "\r\n");
				docTxt = docTxt.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");
			}
			return docTxt;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}

	}
	
	/**
	 * 病历文档全文（目前应用于门诊）
	 * @param docXml
	 * @return
	 */
	public static String getDocFullTxt(String docXml){
		try {
			SAXReader reader = new SAXReader();       
			org.dom4j.Document document = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8"))); 
			Element root = document.getRootElement();
			String docTxt="";
			List<Element> listElement=root.elements();//所有一级子节点的list
			for(Element e:listElement){//遍历所有一级子节点
				String nodeName=e.getName();
				if(nodeName!=null&&nodeName.equals("Body_Text")){
					docTxt = e.getTextTrim();
					if(docTxt!=null&&!docTxt.equals("")) {
						break;
					}
				}
			}
			return docTxt;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}

	}
	
	/**
     * 从指定节点开始,递归遍历所有子节点
     */
	public static String getNodeText(Element node,String qryCode){
		String text="";
		if(node==null) return text;

		Element textNode;
		if(qryCode==null||qryCode.equals("")) return text;
		
		String nodeName=node.getName();
		boolean find=false;
		if(nodeName!=null&&!nodeName.equals("")){
			//当前节点的名称、文本内容和属性
			List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
			for(Attribute attr:listAttr){//遍历当前节点的所有属性
				String name=attr.getName();//属性名称
				String value=attr.getValue();//属性的值
				//System.out.println("属性名称："+name+"属性值："+value);

				if(name!=null&&!name.equals("")&&value!=null&&!value.equals("")){
					if(name.equals("sec_code")){
						if(value.equals(qryCode)||value.equals("SB"+qryCode)){
							find=true;
							break;
						}
					}else if(name.equals("de_code")){
						if(value.equals(qryCode)||value.equals("EB"+qryCode)){
							find=true;
							break;
						}
					}else if(name.equals("para_code")){
						if(value.equals(qryCode)){
							find=true;
							break;
						}
					}
				}
			}
			if(find){
				textNode=node.element("Content_Text");
				
				
				if(textNode!=null){
					text=textNode.getTextTrim();
					return text;
				}
			}
			
		}

		
		//递归遍历当前节点所有的子节点
		List<Element> listElement=node.elements();//所有一级子节点的list
		for(Element e:listElement){//遍历所有一级子节点
			String eText=getNodeText(e,qryCode);//递归
			if(!StringUtils.isEmpty(eText)) return eText;
		}
		return text;
	}
	
	
	/**
	 * 查询病历文档节点内容
	 * @param docXml
	 * @param code 编码
	 * @return
	 */
	public static String getEmrNoteText(String docXml,String code){
		String text="";
		if(docXml==null) return text;

		try {
			Pattern pattern = Pattern.compile("<DocObjContent(.*?)>");
			Matcher matcher = pattern.matcher(docXml);
			if(matcher.find()){
				String str=matcher.group(1);
				docXml=docXml.replace(str,"");
			}
			
			SAXReader reader = new SAXReader();       
			org.dom4j.Document document = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8"))); 
			Element root = document.getRootElement();
			
			text = getNodeText(root,code);


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return text;
		}

		if(!StringUtils.isEmpty(text)){
			text=text.trim();
			text= text.replaceAll("\\{", "").replaceAll("\\}", "");
			text= text.replaceAll("(\r?\n()+)", "\r\n");
			text=text.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");
		}

		return text;
	}
	
	 /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split(UNDERLINE);
        for (String s : a) {
            if (!para.contains(UNDERLINE)) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        if (!para.contains(UNDERLINE)) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, UNDERLINE);
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }
    
  //首字母转小写
    public static String toLowerCaseFirstOne(String s){
      if(Character.isLowerCase(s.charAt(0)))
        return s;
      else
        return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }


    //首字母转大写
    public static String toUpperCaseFirstOne(String s){
      if(Character.isUpperCase(s.charAt(0)))
        return s;
      else
        return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}

