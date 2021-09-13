package com.zebone.nhis.emr.scires.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.zebone.nhis.common.module.emr.qc.EmrEventRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeItem;
import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgItem;
import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeRule;
import com.zebone.nhis.common.module.emr.qc.EmrGradeStandard;
import com.zebone.nhis.common.module.emr.qc.EmrGradeType;
import com.zebone.nhis.common.module.emr.qc.EmrMedRecTask;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;
import com.zebone.nhis.common.module.emr.scires.EmrRptCnd;
import com.zebone.nhis.common.module.emr.scires.EmrRptExp;
import com.zebone.nhis.common.module.emr.scires.EmrRptList;
import com.zebone.nhis.emr.common.EmrConstants;
import com.zebone.nhis.emr.common.EmrUtils;
import com.zebone.nhis.emr.qc.dao.EmrQCMapper;
import com.zebone.nhis.emr.rec.rec.dao.EmrRecMapper;
import com.zebone.nhis.emr.scires.dao.EmrSciResMapper;
import com.zebone.nhis.emr.scires.vo.RptRsltItem;
import com.zebone.nhis.emr.scires.vo.RptRsltRec;
import com.zebone.nhis.emr.scires.vo.SciResAnaQry;
import com.zebone.nhis.emr.scires.vo.SciResAnaRpt;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 病历科研分析服务
 * @author chengjia
 *
 */
@Service
public class SciResService {
	

	static Logger log = LogManager.getLogger(SciResService.class);

	@Resource
	private	EmrSciResMapper sciResMapper;
	@Resource
	private	EmrRecMapper recMapper;
	
	/**
	 * 查询科研分析报表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrRptList> queryRptList(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<EmrRptList> rtnList=sciResMapper.queryRptList(map);
		return rtnList;
	}
	
	/**
	 * 查询科研分析报表信息
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public EmrRptList queryRptInfo(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		//String pkRpt=map.get("pkRpt").toString();
		Map cndMap=new HashMap();
		cndMap.put("pkRpt", map.get("pkRpt").toString());
		String orderBy=" order by sort_num,pk_cnd";
		cndMap.put("orderBy", orderBy);
		List<EmrRptList> rptList=sciResMapper.queryRptList(map);
		if(rptList!=null&&rptList.size()>0){
			orderBy=" order by exp.exp_num,exp.pk_exp";
			map.put("orderBy", orderBy);
			EmrRptList rpt=rptList.get(0);
			List<EmrRptCnd> cndList=sciResMapper.queryRptCndList(cndMap);
			rpt.setCndList(cndList);
			List<EmrRptExp> expList=sciResMapper.queryRptExpList(map);
			rpt.setExpList(expList);
			return rpt;
		}

		return null;
	}
	
	
	/**
	 * 保存科研分析报表
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveRptList(String param , IUser user){
		EmrRptList rpt=JsonUtil.readValue(param, EmrRptList.class);
		String status="";
		if(StringUtils.isNoneEmpty(rpt.getStatus())) status= rpt.getStatus();
		
		if(status.equals(EmrConstants.STATUS_NEW)){
			sciResMapper.saveEmrRptList(rpt);
		}else if(status.equals(EmrConstants.STATUS_UPD)){
			sciResMapper.updateEmrRptList(rpt);
		}else if(status.equals(EmrConstants.STATUS_DEL)){
			sciResMapper.deleteEmrRptList(rpt.getPkRpt());
		}
		EmrRptCnd cnd=null;
		EmrRptExp exp=null;
		int i=0;
		for (i = 0; rpt.getCndList()!=null&&i < rpt.getCndList().size(); i++) {
			cnd=rpt.getCndList().get(i);
			status="";
			if(StringUtils.isNoneEmpty(cnd.getStatus())) status= cnd.getStatus();
			
			if(status.equals(EmrConstants.STATUS_NEW)){
				sciResMapper.saveEmrRptCnd(cnd);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				sciResMapper.updateEmrRptCnd(cnd);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				sciResMapper.deleteEmrRptCnd(cnd.getPkCnd());
			}
		}
	
		for (i = 0; rpt.getExpList()!=null&&i < rpt.getExpList().size(); i++) {
			exp=rpt.getExpList().get(i);
			status="";
			if(StringUtils.isNoneEmpty(exp.getStatus())) status= exp.getStatus();
			
			if(status.equals(EmrConstants.STATUS_NEW)){
				sciResMapper.saveEmrRptExp(exp);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				sciResMapper.updateEmrRptExp(exp);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				sciResMapper.deleteEmrRptExp(exp.getPkExp());
			}
		}
	}  
    
	/**
	 * 查询科研分析报表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SciResAnaRpt queryRptAnaly(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		SciResAnaQry qry=JsonUtil.readValue(param, SciResAnaQry.class);
		SciResAnaRpt rpt=new SciResAnaRpt();
		rpt.setRetCode(0);
		rpt.setRetMsg("");
		rpt.setRecList(new ArrayList<RptRsltRec>());
		List<EmrRptCnd> cndList=sciResMapper.queryRptCndList(map);
		if(cndList==null||cndList.size()==0){
			rpt.setRetCode(-1);
			rpt.setRetMsg("科研分析条件未设置!");
			return rpt;
		}
		
		List<EmrRptCnd> cndListAya=new ArrayList<EmrRptCnd>();
		for (EmrRptCnd emrRptCnd : cndList) {
			if(emrRptCnd.getEuGrade()==null) continue;
			int grade=emrRptCnd.getEuGrade().intValue();
			String cndType=emrRptCnd.getEuCndtype();
			if(cndType==null) continue;
			if(grade==1){
				if(cndType.equals("2")){
					//组合条件
					//查找子条件
					List<EmrRptCnd> childs=getCndChilds(emrRptCnd,cndList);
					emrRptCnd.setChilds(childs);
				}else{
					//简单条件
				}
				cndListAya.add(emrRptCnd);
			}
		}
		
		List<EmrRptExp> expList=sciResMapper.queryRptExpList(map);
		if(expList==null||expList.size()==0){
			rpt.setRetCode(-1);
			rpt.setRetMsg("科研分析输出项目未设置!");
			//return rpt;
		}

		List<EmrPatList> list=recMapper.queryPatListByConds(map);
		if(list==null||list.size()==0){
			rpt.setRetCode(0);
			rpt.setRetMsg("未检索到符合条件的就诊记录...");
			return rpt;
		}
		System.out.println("list:"+list.size());
		List<String> pkPvs=new ArrayList<String>();
		for (EmrPatList pat : list) {
			pkPvs.add(pat.getPkPv());
		}
		List<EmrMedRec> recList=recMapper.queryEmrDocListByPvs(pkPvs);
		if(recList==null||recList.size()==0){
			rpt.setRetCode(0);
			rpt.setRetMsg("未检索到符合条件的病历记录...");
			return rpt;
		}
		for (EmrPatList pat : list) {
			List<EmrMedRec> patRecList=new ArrayList<EmrMedRec>();
			for (EmrMedRec rec : recList) {
				if(pat.getPkPv().equals(rec.getPkPv())){
					patRecList.add(rec);
				}
			}
			if(patRecList==null||patRecList.size()==0) continue;
			
			List<RptRsltRec> rsltRecList=patAnalyHandle(pat,cndListAya,expList,patRecList);
			if(rsltRecList!=null&&rsltRecList.size()>0){
				rpt.getRecList().addAll(rsltRecList);
			}
		}
		
		return rpt;
	}

	private List<EmrRptCnd> getCndChilds(EmrRptCnd cnd,List<EmrRptCnd> cndList){
		if(cnd==null||cnd.getEuCndtype()==null||!cnd.getEuCndtype().equals("2")) return null;
		List<EmrRptCnd> cndListChild=new ArrayList<EmrRptCnd>();
		for (EmrRptCnd emrRptCnd : cndList) {
			String pkCndUp=emrRptCnd.getPkCndUp();
			if(pkCndUp!=null&&pkCndUp.equals(cnd.getPkCnd())){
				cndListChild.add(emrRptCnd);
				if(emrRptCnd.getEuCndtype()!=null&&emrRptCnd.getEuCndtype().equals("2")){
					emrRptCnd.setChilds(getCndChilds(emrRptCnd,cndList));
				}
			}
			
	
		}
		return cndListChild;
	}
	//单病人处理科研分析
	@SuppressWarnings("rawtypes")
	private List<RptRsltRec> patAnalyHandle(EmrPatList pat, List<EmrRptCnd> cndList,List<EmrRptExp> expList,List<EmrMedRec> recList) {
		if(pat==null||cndList==null||expList==null) return null;
		List<RptRsltRec> rtnList=new ArrayList<RptRsltRec>();
		Map map=EmrUtils.beanToMap(pat);
		for (EmrRptCnd cnd : cndList) {
			boolean match=analyHandle(map,cnd,expList,recList);
			cnd.setMatch(match);
		}
		boolean rtn=calculateResult(cndList, cndList.size()-1);
		if(rtn){
			RptRsltRec report=new RptRsltRec();
			report.setPkPi(map.get("pkPi").toString());
			report.setPkPv(map.get("pkPv").toString());
			report.setName(map.get("name").toString());
			report.setAgeTxt(map.get("ageTxt").toString());
			report.setSexName(map.get("sexName").toString());
			report.setTimes(Integer.parseInt(map.get("ipTimes").toString()));
			report.setUserName(map.get("name").toString());
			report.setValue("");
			report.setPatNo(map.get("patNo").toString());
			String beginStr=map.get("dateBegin").toString();
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.US);
				Date beginDate = sdf.parse(beginStr);
				sdf = new SimpleDateFormat("yyyy-MM-dd");

				beginStr=sdf.format(beginDate);
			} catch (ParseException e1) {
				
			}
			
			report.setBeginDate(beginStr);
			List<RptRsltItem> itemList=new ArrayList<RptRsltItem>();
			for (EmrRptExp exp : expList) {
				String nodeType=exp.getNodeType();
				RptRsltItem item=new RptRsltItem();
				item.setNodeName(exp.getNodeName());
				item.setNodeCode(exp.getNodeCode());
				item.setNodeType(nodeType);
				item.setValue("");
				for (EmrMedRec rec : recList) {
					if(exp.getDocType()!=null&&rec.getTypeCode()!=null&&exp.getDocType().equals(rec.getTypeCode())){
						String xml=rec.getDocXml();
						if(xml!=null){
							try {
								Document doc=EmrUtils.string2doc(xml);
								String xpath="";
								String nodeCode=exp.getNodeCode();
								//chengjia 111
								if(nodeType.equals("para")){
									xpath="//Region[@para_code='"+nodeCode+"']/Content_Text/text()";
								}else if(nodeType.equals("sec")){
									xpath="//Section[@sec_code='"+nodeCode+"']/Content_Text/text()";
									/*//限制段落
									if(paraCode!=null&&!paraCode.equals("")&&!paraCode.equals("*")){
										xpath="/DocObjContent/Region[@para_code='"+paraCode+"']//Section[@sec_code='"+code+"']/Content_Text/text()";
									}else{
										xpath="//Section[@sec_code='"+code+"']/Content_Text/text()";
									}*/
								}else if(nodeType.equals("element")){
									xpath="//NewCtrl[@de_code='"+nodeCode+"']/Content_Text/text()";
									
									/*if(paraCode!=null&&!paraCode.equals("")&&!paraCode.equals("*")){
										xpath="/DocObjContent/Region[@para_code='"+paraCode+"']//NewCtrl[@de_code='"+code+"']/Content_Text/text()";
									}else{
										xpath="//NewCtrl[@de_code='"+code+"']/Content_Text/text()";
									}*/

								}
								Object tmp = EmrUtils.getXPathElement(doc, xpath);
								if(tmp!=null){
									//item.setValue(item.getValue()+"#("+rec.getPkRec()+")"+tmp.toString()+"\n");
									item.setValue(tmp.toString());
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
				}
				itemList.add(item);
			}
			if(itemList.size()>0){
				RptRsltItem[] arrayItems=new RptRsltItem[itemList.size()];
				arrayItems=itemList.toArray(arrayItems);
				//report.setArrayItems(arrayItems);
				report.setItems(itemList);
			}
			rtnList.add(report);
		}
		return rtnList;
	}

	//科研分析处理（单条件）
	@SuppressWarnings("rawtypes")
	private boolean analyHandle(Map map, EmrRptCnd cnd,List<EmrRptExp> expList, List<EmrMedRec> recList) {
		String cndType=cnd.getEuCndtype();
		if(cndType==null) return false;
		if(cndType.equals("2")){
			//组合条件
			List<EmrRptCnd> childs=cnd.getChilds();
			if(childs!=null&&childs.size()>0){
				for (EmrRptCnd childCnd : childs) {
					childCnd.setMatch(analyHandle(map,childCnd,expList,recList));
				}
				return calculateResult(childs,childs.size()-1);
			}
		}else if(cndType.equals("1")){
			//简单条件
			boolean match = false;
			for (EmrMedRec rec : recList) {
				match=analyRecHandle(map,cnd,rec);
				if(match) break;
			}
			return match;
		}
		
		return false;
		
	}

	@SuppressWarnings("rawtypes")
	private boolean analyRecHandle(Map map, EmrRptCnd cnd, EmrMedRec rec) {
		boolean ret = false ;
		if(cnd==null) return false;
		String objClass=cnd.getEuObjClass();
		String objCode=cnd.getObjCode();
		String logic=(cnd.getLogic()==null||cnd.getLogic().equals(""))?"=":cnd.getLogic();
		String valueCode=cnd.getValueCode();
		String valueName=cnd.getValueName();
		String dataType=cnd.getEuDataType();
		String objType=cnd.getEuObjType();
		String docType=rec.getTypeCode();
		String docXml=rec.getDocXml();
		String docTypeCnd="",paraCode="",code="";
		
		if(objType==null) objType="element";
		if(valueName==null) valueName="";
		//0基本信息1文档
		if(objClass==null) objClass="1";
		String xpath="";
		if(objClass.equals("1")){
			//病历文档chengjia
			try {
				String[] paths = objCode.split("\\|");//doctype/
				if(paths!=null){
					int len=paths.length;
					if(len>0){
						docTypeCnd=paths[0];
						if(len>1){
							paraCode=paths[1];
						}
						if(len>2){
							code=paths[2];
						}
					}
					if(docType.equals(docTypeCnd)&&docXml!=null){
						Document document=EmrUtils.string2doc(docXml);
						StringBuffer bf = new StringBuffer();
						//chengjia
						bf.append("/DocObjContent");
						xpath="";
						if(objType.equals("para")){
							//段落
							///DocObjContent/Region[@para_code='01']/Content_Text/text()
							bf.append("/Region[@para_code='"+paraCode+"']/Content_Text/text()");
							xpath="//Region[@para_code='"+paraCode+"']/Content_Text/text()";
						}else if(objType.equals("sec")){
							//组合元素
							///DocObjContent/Region/Section[@sec_code='0002634']/Content_Text/text()
							//bf.append("/Region/Section[@sec_code='"+code+"']/Content_Text/text()");
							xpath="//Section[@sec_code='"+code+"']/Content_Text/text()";
							//限制段落
							if(paraCode!=null&&!paraCode.equals("")&&!paraCode.equals("*")){
								xpath="/DocObjContent/Region[@para_code='"+paraCode+"']//Section[@sec_code='"+code+"']/Content_Text/text()";
							}else{
								xpath="//Section[@sec_code='"+code+"']/Content_Text/text()";
							}
						}else if(objType.equals("element")){
							//元素
							///DocObjContent/Region/Section/NewCtrl[@de_code='0001023']/Content_Text/text()
							//bf.append("/Region/Section/NewCtrl[@sec_code='"+code+"']/Content_Text/text()");
							//限制段落
							if(paraCode!=null&&!paraCode.equals("")&&!paraCode.equals("*")){
								xpath="/DocObjContent/Region[@para_code='"+paraCode+"']//NewCtrl[@de_code='"+code+"']/Content_Text/text()";
							}else{
								xpath="//NewCtrl[@de_code='"+code+"']/Content_Text/text()";
							}
						}
						if(!xpath.equals("")){
							Object obj = EmrUtils.getXPathElement(document , xpath);
							if(obj!=null){
								String objStr=obj.toString();
								if(logic.equals("=")){
									if(valueName.indexOf("，")>=0&&objStr.indexOf(valueName)>=0){
										ret=true;
									}else if(valueName.indexOf("，")>=0){
										String[] strs = valueName.split("，");
										for ( String str :strs){
											if ( objStr.indexOf(str) >= 0 ){
												ret = true;
												break;
											}
										}
									}else{
										if(objStr.equals(valueName)){
											ret=true;
										}
									}
								}else if( logic.equals("!=")){
									if (valueName.indexOf("，") >= 0 && !(objStr.indexOf(valueName) >= 0) ){
										ret = true ;
									}else if (valueName.indexOf("，") >= 0 ){
										String[] strs = valueName.split("，");
										for ( String str :strs){
											if ( objStr.indexOf(str) >= 0 ){
												ret = false;
												return ret;
											}
										}
										ret=true;
									}else{
										if(!objStr.equals(valueName)){
											ret=true;
										}
									}
										
								}else if( logic.equals("in")){
									if (valueName.indexOf("，") >= 0 && objStr.indexOf(valueName) >= 0 ){
										ret = true ;
									}else if (valueName.indexOf("，") >= 0 ){
										String[] strs = valueName.split("，");
										for ( String str :strs){
											if ( objStr.indexOf(str) >= 0 ){
												ret = true;
												break;
											}
										}
									}
								}else if(logic.equals("like")){
									if(objStr.indexOf(valueName)>=0){
										ret=true;
									}
								}
								
							}
						} 
						
						
					}
				}
			} catch (Exception e) {
				return false;
			}
			
		}else if(objClass.equals("0")){
			//患者信息
			if(objCode==null) objCode="";
			if(dataType==null||dataType.equals("")) dataType="3";
			if(valueCode==null) valueCode="";
			if(logic.equals("=")){
				ret=objCode.equals(valueCode);
			}else if(logic.equals("!=")){
				ret=!objCode.equals(valueCode);
			}else if(logic.equals("like")){
				ret=objCode.indexOf(valueCode)>=0;
			}else if(logic.equals("in")){
				ret=objCode.indexOf(valueCode)>=0;
			}else if(dataType.equals("1")){
				//chengjia 0 数字1 日期 3文本text 2input（文本选择）
				SimpleDateFormat format=new SimpleDateFormat();
				try {
					Date date1=format.parse(valueCode);
					Date date2=format.parse(map.get(objCode).toString());
					if(logic.equals(">")){
						ret=date2.getTime()>date1.getTime();
					}else if(logic.equals(">=")){
						ret=date2.getTime()>=date1.getTime();
					}
					if(logic.equals("<")){
						ret=date2.getTime()<date1.getTime();
					}
					if(logic.equals("<=")){
						ret=date2.getTime()<=date1.getTime();
					}
				} catch (Exception e) {
					ret=false;
				}
			}else if(dataType.equals("0")){
				try {
					float num1 = Float.parseFloat(valueCode);
					float num2 = Float.parseFloat(map.get(objCode).toString());
					if (logic.equals(">")){
						ret =  num2 > num1 ;
					}else if ( logic.equals(">=")){
						ret =  num2 >= num1 ;
					}else if ( logic.equals("<")){
						ret =  num2 < num1 ;
					}else if ( logic.equals("<=")){
						ret =  num2 <= num1 ;
					}
				} catch (Exception e) {
					ret = false;
				}
			}
			
		}
		return ret;
	}

	private boolean calculateResult(List<EmrRptCnd> childs,int row) {
		boolean bRtn=false;
		boolean bMatchPrev=false;
		boolean bMatchCurr=false;
		
		if(row<0||row>=childs.size()){
			return false;
		}else if(row==0){
			return childs.get(0).isMatch();
		}else if(row>=1){
			EmrRptCnd child=childs.get(row-1);
			if(child==null) return false;
			String rttype=child.getEuRttype();
			rttype=rttype==null?"1":(rttype.equals("2")?"2":"1");
			if(rttype.equals("1")){
				//并且
				bMatchPrev=calculateResult(childs,row-1);
				bMatchCurr=childs.get(row).isMatch();
				bRtn=(bMatchPrev&&bMatchCurr);
				return bRtn;
			}else{
				//或者
				bMatchPrev=calculateResult(childs,row-1);
				bMatchCurr=childs.get(row).isMatch();
				bRtn=(bMatchPrev||bMatchCurr);
				//bRtn=(calculateResult(childs,row-1)||childs.get(row).isMatch());
				return bRtn;
			}
		}
		
		return false;
		
	}
	
}

