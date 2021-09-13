package com.zebone.nhis.arch.support;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.zebone.nhis.arch.vo.FileVo;
import com.zebone.nhis.arch.vo.WSArchiveManageIntfReturnVo;

public class ArchUtil {

	/**
	 * 将set<String>中的数据修改为： 'value1','value2','value3'
	 * @param setstr
	 *            放在in中的主键
	 * @param pkName
	 *            当上面的属性长度大于1000的时候做分割使用的数据
	 */
	public static String convertSetToSqlInPart(Set<String> setStr, String pkName) {

		StringBuffer sbf = new StringBuffer();
		String[] strArr = setStr.toArray(new String[0]);
		if (strArr.length > 999) {

			/*
			 * 自己使用不会出现这种状况 if(pkName == null || pkName.equals("")){ throw new
			 * BusinessException("传入的pkName为空请检查"); }
			 */

			int strArrLen = strArr.length;
			int fortimes = strArrLen / 1000;
			int subNums = strArrLen % 1000;
			// 循环每个1000的数据
			for (int x = 0; x < fortimes; x++) {
				for (int i = x * 1000, cnt = (2 * x + 1) * 500; i < cnt; i++) {
					sbf.append("'");
					sbf.append(strArr[i]);
					sbf.append("',");
				}
				sbf = new StringBuffer(sbf.substring(0, sbf.length() - 1));
				sbf.append(") or ");
				sbf.append(pkName);
				sbf.append(" in (");
				for (int i = (2 * x + 1) * 500, cnt = (x + 1) * 1000; i < cnt; i++) {
					sbf.append("'");
					sbf.append(strArr[i]);
					sbf.append("',");
				}
				sbf = new StringBuffer(sbf.substring(0, sbf.length() - 1));
			}
			if (subNums != 0) {
				sbf.append(") or ");
				sbf.append(pkName);
				sbf.append(" in (");
			}
			// 循环除1000后的余数
			for (int i = 1000 * fortimes; i < strArrLen; i++) {
				sbf.append("'");
				sbf.append(strArr[i]);
				sbf.append("',");
			}
			sbf = new StringBuffer(sbf.substring(0, sbf.length() - 1));
		} else {
			for (int i = 0, cnt = strArr.length; i < cnt; i++) {
				sbf.append("'");
				sbf.append(strArr[i]);
				sbf.append("',");
			}
			if (strArr.length > 0) {
				sbf = new StringBuffer(sbf.substring(0, sbf.length() - 1));
			} else {
				sbf = new StringBuffer("''");
			}
		}
		return sbf.toString();
	}

	public static WSArchiveManageIntfReturnVo parseToWSArchiveManageIntfReturnVo(String protocolXML) {

		WSArchiveManageIntfReturnVo returnVo = null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			SaxHandler handler = new SaxHandler("FileList");
			parser.parse(new InputSource(new StringReader(protocolXML)), handler);
			returnVo = handler.getReturnVo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVo;
	}

	/**
	 * 测试方法
	 */
	// public static void main(String[] args) {
	//
	// String ddd =
	// "<?xml version=\"1.0\" encoding=\"utf-8\"?><FileList total=\"4\"><patId>20160317334740</patId><patName>梁月琴</patName><inOrOut>住院</inOrOut><File><code>20160317334740</code><fileName>PACS_20160317334740</fileName><name>胎儿超声心动图_x2</name><arcYN>N</arcYN><repTime></repTime><desc></desc></File><File><code>20160318337377</code><fileName>PACS_20160318337377</fileName><name>产科I级超声+脐血流监测_c2</name><arcYN>N</arcYN><repTime></repTime><desc></desc></File><File><code>20160321343247</code><fileName>PACS_20160321343247</fileName><name>胎盘</name><arcYN>N</arcYN><repTime></repTime><desc></desc></File><File><code>20160315330535</code><fileName>PACS_20160315330535</fileName><name>心电图(十八导联)*</name><arcYN>N</arcYN><repTime></repTime><desc></desc></File></FileList>";
	// parseToWSArchiveManageIntfReturnVo(ddd);
	// }

}
/**
 * 将xml格式的字符串映射成自定义的VO
 * 
 * <FileList total=”此病人产生的病历文档总数量”> <patId>病人ID<patId> <patName>病人姓名</patName>
 * <inOrOut>门诊/住院/体检</inOrOut> <File> <code>病历文档各个系统内部唯一编码</code>
 * <fileName>文件名</fileName> <name>报告名称</name> <arcYN>Y:已经归档；N待归档</arcYN>
 * <repTime>报告单时间</repTime> <desc>报告的其他一些描述</desc> </File> . . . <File>
 * <code>病历文档各个系统内部唯一编码</code> <name>报告名称</name> <arcYN>Y:已经归档；N待归档</arcYN>
 * <repTime>报告单时间</repTime> <desc>报告的其他一些描述</desc> </File> </FileList>
 * 
 * 2017-5-19 10:50:06
 * 
 * @author gongxy
 * 
 */
class SaxHandler extends DefaultHandler {

	private List<FileVo> files = null;

	private FileVo file;

	private WSArchiveManageIntfReturnVo returnVo;

	private String currentTag = null;

	private String currentValue = null;

	private String nodeName = null;

	public SaxHandler(String nodeName) {

		this.nodeName = nodeName;
	}

	public List<FileVo> getFiles() {

		return files;
	}

	public WSArchiveManageIntfReturnVo getReturnVo() {

		return returnVo;
	}

	@Override
	public void startDocument() throws SAXException {

		// TODO 当遇到文档的开头的时候，调用这个方法
		super.startDocument();
		returnVo = new WSArchiveManageIntfReturnVo();
		files = new ArrayList<FileVo>();

	}

	@Override
	public void endDocument() throws SAXException {

		// TODO Auto-generated method stub
		super.endDocument();
		returnVo.setFiles(files);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		// TODO
		if (qName.equals("File")) {
			file = new FileVo();
		}
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals(nodeName))
			if (returnVo != null && attributes != null) {
				for (int i = 0; i < attributes.getLength(); i++) {
					if (attributes.getQName(i).equals("total")) {
						returnVo.setFileTotalCount(attributes.getValue(i));
					}
				}
			}
		currentTag = qName;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		// TODO 在遇到结束标签的时候，调用这个方法
		super.endElement(uri, localName, qName);
		if (qName.equals("File") && file != null) {
			files.add(file);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		// TODO 这个方法用来处理在XML文件中读到的内容
		super.characters(ch, start, length);
		if (currentTag != null && returnVo != null && files != null) {
			currentValue = new String(ch, start, length);
			if (currentValue != null && !currentValue.trim().equals("") && !currentValue.trim().equals("\n")) {
				if (currentTag.equals("patId")) {
					returnVo.setPatId(currentValue);
				}
				if (currentTag.equals("patName")) {
					returnVo.setPatName(currentValue);
				}
				if (currentTag.equals("inOrOut")) {
					returnVo.setInOrOut(currentValue);
				}
				if (file != null) {
					if (currentTag.equals("code")) {
						file.setCode(currentValue);
					}
					if (currentTag.equals("fileName")) {
						file.setFileName(currentValue);
					}
					if (currentTag.equals("name")) {
						file.setName(currentValue);
					}
					if (currentTag.equals("arcYN")) {
						file.setArcYN(currentValue);
					}
					if (currentTag.equals("repTime")) {
						file.setRepTime(currentValue);
					}
					if (currentTag.equals("desc")) {
						file.setDesc(currentValue);
					}
				}

			}
		}
		currentTag = null;
		currentValue = null;
	}

}
