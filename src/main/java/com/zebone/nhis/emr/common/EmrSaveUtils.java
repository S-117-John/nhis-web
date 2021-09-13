package com.zebone.nhis.emr.common;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.esotericsoftware.minlog.Log;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrTxtData;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.Application;
import com.zebone.platform.modules.dao.DataBaseHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 电子病历存储工具类
 * @author chengjia
 *
 */
public  class EmrSaveUtils {
	
	/**
	 * 获取存储方式
	      病历存储数据模式：0、本地存储（空或未设置）1、独立存储（数据文件分开存储）
	 * @return
	 */
	public static String getSaveDataMode(){
		String saveDataMode = ApplicationUtils.getSysparam("EmrSaveDataMode", true);
		if(saveDataMode!=null&&saveDataMode.equals("1")){
			saveDataMode="1";
		}else{
			saveDataMode="0";
		}
		
		return saveDataMode;
	}
	
	/**
	 * 获取存储数据库名称
	 * @return
	 */
	public static String getDbName(){
		String dbName = ApplicationUtils.getSysparam("EmrSaveDataDBName", true);
		if(dbName==null){
			if(Application.isSqlServer()){
				dbName="emr.dbo.";
			}else{
				dbName="emr.";
			}
		}
		return dbName;
	}
	
	/**
	 * 保存病历文本数据
	 * @param medRec
	 * @param medDoc
	 */
	public static void saveEmrExpData(EmrMedRec medRec, EmrMedDoc medDoc,String status) {
		//保存病历文档的同时，将特定文档特定段落的内容存储到表中
		String strs=ApplicationUtils.getPropertyValue("emr.save.data.paras", "");
		if(strs!=null&&!strs.equals("")){
			Date now = new Date();
			//先删除、再插入
			DataBaseHelper.execute("delete from emr_txt_data where pk_pv = ? and  pk_rec = ? ",new Object[] {medRec.getPkPv(),medRec.getPkRec()});
			
			if ((medRec.getDelFlag()==null||medRec.getDelFlag().equals("0"))&&(status.equals(EmrConstants.STATUS_NEW) || status.equals(EmrConstants.STATUS_UPD))) {
				
				//存储整份病历文本
				saveEmrTxtData(medRec, now, "00","00", EmrUtils.getDocTxt(medDoc.getDocXml()),null);
				
				List<String> listType = java.util.Arrays.asList(strs.split(";"));
				if(listType!=null&&listType.size()>0){
					for(int i=0;i<listType.size();i++){
						String str=listType.get(i);
						if(str==null||str.equals("")) continue;
						List<String> listDoc = java.util.Arrays.asList(str.split(":"));
						if(listDoc!=null&&listDoc.size()==3){
							String docType=listDoc.get(0);
							if(docType==null||docType.equals("")) continue;
							if(!docType.equals(medRec.getTypeCode())) continue;
							//病程记录
							String qryType=listDoc.get(1);
							if(qryType==null||qryType.equals("")) continue;
							if("012".indexOf(qryType)<=0) continue;
							
							String strDoc=listDoc.get(2);
							if(strDoc==null||strDoc.equals("")) continue;
							List<String> listPara = java.util.Arrays.asList(strDoc.split(","));
							if(listPara!=null&&listPara.size()>0){
								for(int j=0;j<listPara.size();j++){
									String strCode=listPara.get(j);
									if(strCode==null||strCode.equals("")) continue;
									List<String> listDe = java.util.Arrays.asList(strCode.split("-"));
									String paraCode="";
									if(listDe!=null&&listDe.size()==2){
										paraCode=listDe.get(0);
										strCode=listDe.get(1);
									}
									String remark=null;
									String[] docTxts=EmrUtils.getPatEmrParaText(medRec, medDoc,medRec.getTypeCode(), strCode,qryType);
									if(docTxts!=null&&docTxts.length>0&&docTxts[0]!=null&&!docTxts[0].equals("")){
										saveEmrTxtData(medRec, now, strCode.replace("de", ""),paraCode, docTxts[0],remark);	
									}
									//名称对应的编码字段
									if(docTxts!=null&&docTxts.length>2&&docTxts[1]!=null&&!docTxts[1].equals("")){
										if(docTxts[2]!=null&&!docTxts[2].equals("")){
											saveEmrTxtData(medRec, now, strCode.replace("de", ""),docTxts[1], docTxts[2],remark);	
										}
									}
									
								}
							}

						}
						
					}
				}
			}
			
		}
	}
	
	/**
	 * 保存病历图片
	 * @param status
	 * @param medDoc
	 * @return
	 */
	public static String saveEmrImageData(String status, EmrMedDoc medDoc) {
		String pathSave="";
		String fileSaveMode = ApplicationUtils.getPropertyValue("emr.fileSaveMode", "loc");
		//loc/ftp
		if(fileSaveMode==null||fileSaveMode.equals("")) fileSaveMode="loc";
		// 新增和修改文档保存pdf、pic或html
		if (status.equals(EmrConstants.STATUS_NEW) || status.equals(EmrConstants.STATUS_UPD)) {
			try {
				byte[] expData = medDoc.getDocExpData();
				if (expData != null && expData.length > 0) {
					String fileName = medDoc.getFileName();
					String filePath = medDoc.getFilePath();
					String fileType = medDoc.getFileType();
					String path = "";
					String lastStr = "";
					//String pathSave="";
					if(fileSaveMode.equals("loc")){
						String rootStr = ApplicationUtils.getPropertyValue("emr.filePath", "");
						if (rootStr != null && !rootStr.equals("")) {
							// 判断文件夹是否存在/不存在添加
							EmrUtils.checkDirExists(rootStr);

							lastStr = rootStr.substring(rootStr.length() - 1, rootStr.length());
							if (lastStr != null && !lastStr.equals("\\")) {
								rootStr = rootStr + "\\";
							}
							if (filePath != null && !filePath.equals("")) {
								lastStr = filePath.substring(filePath.length() - 1, filePath.length());
								if (lastStr != null && !lastStr.equals("\\")) {
									filePath = filePath + "\\";
								}
							}
							if (fileType == null || fileType.equals(""))
								fileType = "pdf";
							// 判断文件夹是否存在/不存在创建
							EmrUtils.checkDirExists(rootStr + filePath);

							path = rootStr + filePath + fileName + "." + fileType;
							// path=rootStr+NHISUUID.getKeyId()+".pdf";
							// 判断文件是否存在/存在删除
							// EmrUtils.removeExistFiles(path);
							// 生成图片（覆盖）
							EmrUtils.byte2image(expData, path);
							pathSave=path;
						}
					}else if (fileSaveMode.equals("ftp")){
						path=filePath;
						if (fileType == null || fileType.equals("")) fileType = "pdf";
						fileName=fileName+"."+fileType;
						pathSave = EmrFtpUtils.fileUpload(path, fileName, expData);
					}
					
					//DataBaseHelper.update("update emr_med_doc set file_path=? where pk_doc=?", new Object[]{pathSave,medDoc.getPkDoc()});
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}else if(status.equals(EmrConstants.STATUS_DEL)){
			if(fileSaveMode.equals("app")){
				
			}else if (fileSaveMode.equals("ftp")){
				
			}
		}
		return pathSave;
	}
	public static void saveEmrTxtData(EmrMedRec medRec, Date now, String strCode,
			String paraCode, String docTxt,String remark) {
		EmrTxtData txt = new EmrTxtData();
		
		txt.setPkPv(medRec.getPkPv());
		txt.setPkDoc(medRec.getPkDoc());
		txt.setPkRec(medRec.getPkRec());
		txt.setTypeCode(medRec.getTypeCode());
		txt.setParaCode(paraCode.equals("")?strCode:paraCode);
		txt.setDocTxt(docTxt);
		txt.setRemark(remark);
		txt.setDelFlag("0");
		txt.setCreator(medRec.getCreator());
		txt.setCreateTime(now);
		txt.setTs(now);
		
		DataBaseHelper.insertBean(txt);
	}
}
