package com.zebone.nhis.common.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.dao.EmrPubMapper;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.platform.modules.dao.DataBaseHelper;


/**
 * 电子病历病历-公共服务
 * @author chengjia
 *
 */
@Service
public class EmrPubService {
	
	@Resource
	private	EmrPubMapper recMapper;
	
	/**
	 * 查询病历文档段落内容
	 * 说明：1、病程记录：typeCode[1000]paraCode[null] 2、主诉：typeCode[090001]paraCode[01]...
	 * @param pkPv就诊主键
	 * @param typeCode文档分类编码
	 * @param paraCode文档段落编码
	 * @return
	 */
	public String getPatEmrParaText(String pkPv , String typeCode,String paraCode){
		String text="";
		Map<String,String> map=new HashMap<String,String>();
		map.put("pkPv", pkPv);
		map.put("typeCode", typeCode+"%");
		String orderBy=" order by rec.rec_date desc,rec.seq_no desc,rec.pk_rec";
		map.put("orderBy", orderBy);
		
		List<EmrMedRec> rtnList=recMapper.queryPatMedRecDoc(map);
		Iterator<Element> it = null;
		Iterator<Element> itC = null;
		Element node=null;
		Element nodeC=null;
		String nodeName="";
		String paraCodeElement="";
		String paraCodeElementC="";
		Element nodeText=null;
		String recDateStr="";
		Date recDate=null;
		String pkRec="";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		if(rtnList!=null&&rtnList.size()>0){
			EmrMedRec rec=rtnList.get(0);
			String flagCourseStr=rec.getDocType().getFlagCourse();
			
			Boolean flagCourse=flagCourseStr==null?false:(flagCourseStr.equals("1")?true:false);
			if(rec.getPkDoc()!=null){
				EmrMedDoc doc =recMapper.getEmrMedDocById(rec.getPkDoc());
				if(doc==null) return "";
				String docXml=doc.getDocXml();
				if(docXml!=null&&!docXml.equals("")){
					try {
						//docXml=docXml.replace("\\<DocObjContent[^>]*\\", "");
						Pattern pattern = Pattern.compile("<DocObjContent(.*?)>");
						Matcher matcher = pattern.matcher(docXml);
						if(matcher.find()){
							String str=matcher.group(1);
							docXml=docXml.replace(str,"");
						}
						
						SAXReader reader = new SAXReader();       
						Document document = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8"))); 
						Element root = document.getRootElement();  
						it = root.elementIterator();  
						while (it.hasNext()) {  
				            // 获取某个子节点对象  
				            node = it.next();  
				            nodeName=node.getName();
				            if(nodeName==null||!nodeName.equals("Region")) continue;
							//System.out.println(node.getName());
//							List<Attribute> list = node.attributes();
//							for (Attribute attr : list) {  
//					            System.out.println(attr.getText() + "-----" + attr.getName()  
//					                    + "---" + attr.getValue());  
//					        } 
				            if(flagCourse==false){
				            	//非病程
								if(node.attribute("para_code")==null) continue;
								paraCodeElement=node.attribute("para_code").getText();
								//if(node.attribute("para_name")==null) continue;
								//String paraNameElement=node.attribute("para_name").getText();
								if(paraCodeElement!=null){
									if(paraCodeElement.equals(paraCode)||paraCode==null||paraCode.equals("")){
										nodeText= node.element("Content_Text");
										if(nodeText!=null){
											text=nodeText.getText();
										}
										break;
									}
								}
				            	
				            }else{
				            	//病程
								if(node.attribute("pk_rec")==null) continue;
								if(node.attribute("rec_date")==null) continue;
								recDateStr=node.attribute("rec_date").getText();
								pkRec=node.attribute("pk_rec").getText();
								if(recDate==null||recDate.compareTo(format.parse(recDateStr))<0){
									recDate=format.parse(recDateStr);
									nodeText= node.element("Content_Text");
									if(nodeText!=null){
										text = nodeText.getText();
									}
									
//									itC = node.elementIterator();  
//									while (itC.hasNext()) {  
//							            nodeC = itC.next();  
//							            if(nodeC.attribute("para_code")==null) continue;
//							            paraCodeElementC=nodeC.attribute("para_code").getText();
//										if(paraCodeElementC!=null){
//											if(paraCodeElementC.equals(pkRec)){
//												nodeText= nodeC.element("Content_Text");
//												if(nodeText!=null){
//													text=nodeText.getText();
//												}
//												break;
//											}
//										}
//									}
									
								}							
				            	
				            }
				        }  
						document.toString();
						

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						return "";
					}

				}
			}
			
		}
		if(text!=null){
			text=text.trim();
			//text=text.replaceAll("[\\t]", "");//text.replaceAll("[\\t\\n\\r]", ""); \t为制表符 \n为换行 \r为回车
			text= text.replaceAll("(\r?\n()+)", "\r\n");
			text=text.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");
		}
		//System.out.println("text:"+text);
		return text;
	}
	
	/**
	 * 更新患者病历记录
	 * @param Map<String, Object> map
	 * 		  pkPv(必填),pkEmpRefer住院医师,pkEmpConsult主治医师,pkEmpNsCharge主管护士,pkEmpNsHead护士长,pkEmpQcDis质控医师
	 */
	public void updateEmrPatRec(Map<String, Object> map){
		String sql="update emr_pat_rec set ";
		String sqlAdd="";
		if(!map.containsKey("ts")){
			map.put("ts", new Date());
		}
		//住院医师pk_emp_refer
		if(map.containsKey("pkEmpRefer")){
			sqlAdd=sqlAdd+"pk_emp_refer = :pkEmpRefer,";
		}
		//主治医师pk_emp_consult
		if(map.containsKey("pkEmpConsult")){
			sqlAdd=sqlAdd+"pk_emp_consult = :pkEmpConsult,";
		}
		//主任医师pk_emp_director
		if(map.containsKey("pkEmpDirector")){
			sqlAdd=sqlAdd+"pk_emp_director = :pkEmpDirector,";
		}
		//主管护士pk_emp_ns_charge
		if(map.containsKey("pkEmpNsCharge")){
			sqlAdd=sqlAdd+"pk_emp_ns_charge = :pkEmpNsCharge,";
		}
		//护士长pk_emp_ns_head
		if(map.containsKey("pkEmpNsHead")){
			sqlAdd=sqlAdd+"pk_emp_ns_head = :pkEmpNsHead,";
		}
		//质控医师pk_emp_qc_dis
		if(map.containsKey("pkEmpQcDis")){
			sqlAdd=sqlAdd+"pk_emp_qc_dis = :pkEmpQcDis,";
		}
		
		String sqlWhere = "ts=:ts where pk_pv =:pkPv";
		DataBaseHelper.update(sql+sqlAdd+sqlWhere, map);
	}

	/**
	 * 查询病历文档段落内容
	 * 说明：1、病程记录：typeCode[1000]paraCode[null] 2、主诉：typeCode[0900]paraCode[01]...
	 * @param pkPv就诊主键
	 * @param typeCode文档分类编码
	 * @param paraCode文档段落编码
	 * @return
	 */
	public EmrMedRec getEmrRecByType(String pkPv , String typeCode){
		EmrMedRec rec=null;
		String text="";
		Map<String,String> map=new HashMap<String,String>();
		map.put("pkPv", pkPv);
		map.put("typeCode", typeCode+"%");
		String orderBy=" order by rec.rec_date desc,rec.seq_no desc,rec.pk_rec";
		map.put("orderBy", orderBy);

		List<EmrMedRec> rtnList=recMapper.queryPatMedRecDoc(map);
		if(rtnList!=null&&rtnList.size()>0){
			rec=rtnList.get(0);
			EmrMedDoc doc =recMapper.getEmrMedDocById(rec.getPkDoc());
			rec.setMedDoc(doc);
		}

		return rec;
	}
}

