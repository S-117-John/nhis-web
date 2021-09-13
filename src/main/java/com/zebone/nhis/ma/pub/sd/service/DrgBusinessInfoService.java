package com.zebone.nhis.ma.pub.sd.service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.sd.dao.DrgInfoMapper;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * drg系统业务处理服务(有事物)
 * @author yangxue
 *
 */
@Service
public class DrgBusinessInfoService {
	
	private static Logger log = LoggerFactory.getLogger("nhis.SdDrgPlatFormLog");
	@Autowired
	private DrgInfoMapper drgInfoMapper;
	
	public String url=ApplicationUtils.getPropertyValue("DRG.HOST_URL", "");
	/**
	 * 属性名map
	 */
	private static Map<String, String> fieldMap = new HashMap<>();
	/**
	 * 最小数据集
	 * @param args
	 * @return
	 */
	public Object minDataInfoUpload(Object...args){
		try {
			log.info("****************开始调用最小数据集*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE", orgCode);
			Map<String, Object> map=new HashMap<String, Object>();
			String [] pkpvS=args[0].toString().split(",");
			String result=null;
			for (int i = 0; i < pkpvS.length; i++) {
				map.put("pkPv", pkpvS[i]);
				List<Map<String, Object>> minDatalist=this.createParam(map);
				reqParam.put("MAIN_DATA", JsonUtil.writeValueAsString(minDatalist));
				log.info("调用最小数据集入参为:"+reqParam);
				//String req = JsonUtil.writeValueAsString(minDatalist);
				result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.MINDATA.URL", ""), reqParam);
				log.info("调用最小数据集返回结果为:"+result);
				log.info("****************结束调用最小数据集*****************");
				JSONObject json =JSONObject.parseObject(result.toString());
				Integer stuCode= (Integer)json.get("CODE");
				if(300==stuCode.intValue()){
					JSONObject jsonFaildata=(JSONObject) json.get("FAILDATA");
					String memo=jsonFaildata.getString("MEMO");
					if(!StringUtils.isEmpty(memo)){
						String msg=getFieldName(memo);
						msg=msg+"。"+jsonFaildata.getString("MSG");
						throw new BusException("失败原因:"+msg);
					}
				}
				if(200!=stuCode.intValue()){
					throw new BusException("调用最小数据集失败，失败信息:"+json.get("MESSAGE")+"\n"+json.get("FAILDATA"));
				}
			}
			return result;
		} catch (Exception e) {
			log.info("最小数据集接口异常:"+e.getMessage());
			throw new BusException("最小数据集接口异常:"+e.getMessage());
		}
	}
	/**
	 * 最小数据集冲销
	 * @param args
	 * @return
	 */
	public Object minDataOffInfoUpdUpload(Object...args){
		try {
			log.info("****************开始调用最小数据集冲销*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("pkPv", args[0]);
			List<Map<String, Object>> orglist=drgInfoMapper.queryMinDataOff(map);
			if(orglist == null || orglist.size() == 0 ){
				throw new BusException("没有查询到可以冲销得数据，请检查！");
			}
			Map<String, Object> mapParam=orglist.get(0);
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE",orgCode);
			reqParam.put("YBLSH", mapParam.get("yblsh").toString());
			reqParam.put("ZYCS", mapParam.get("zycs").toString());
			reqParam.put("BAH", mapParam.get("bah").toString());
			log.info("调用最小数据集冲销入参为:"+reqParam);
			//String req = JsonUtil.writeValueAsString(reqParam);
			String result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.MINDATAOFF.URL", ""), reqParam);
			log.info("调用最小数据集冲销返回结果为:"+result);
			log.info("****************结束调用最小数据集冲销*****************");
			JSONObject json =JSONObject.parseObject(result.toString());
			Integer stuCode= (Integer)json.get("CODE");
			if(200!=stuCode.intValue()){
				throw new BusException("病历冲销失败，失败信息:"+json.get("MESSAGE"));
			}
			return result;
		} catch (Exception e) {
			log.info("最小数据集冲销接口异常"+e.getMessage());
			throw new BusException("最小数据集冲销接口异常"+e.getMessage());
		}
	}
	/**
	 * 病人服务明细
	 * @param args
	 * @return
	 */
	public Object serviceDetailUpload(Object...args){
		try {
			log.info("****************开始调用病人服务明细*****************");
			//Map<String,String> reqParam = new HashMap<>();
			Map<String,String> reqParam =new LinkedHashMap <>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE", orgCode);
			Map<String, Object> map=new HashMap<String, Object>();
			
			String [] pkpvS=args[0].toString().split(",");
			String result=null;
			for (int i = 0; i < pkpvS.length; i++) {
				map.put("pkPv", pkpvS[i]);
				List<Map<String, Object>> orglist=drgInfoMapper.queryServiceDetail(map);
				
				if(orglist == null || orglist.size() == 0 ){
					throw new BusException("没有查询到病人服务明细，请检查！");
				}
				Map<String, Object> mapBah=orglist.get(0);
				reqParam.put("YBLSH", mapBah.get("yblsh").toString());
				reqParam.put("JZLSH", mapBah.get("jzlsh").toString());
				reqParam.put("ZYCS", mapBah.get("zycs").toString());
				reqParam.put("BAH", mapBah.get("bah").toString());
				
				List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
				Map<String, Object> mapData=new LinkedHashMap<String, Object>();
				for (Map<String, Object> map2 : orglist) {
					mapData=new LinkedHashMap<String, Object>();
					mapData.put("CYSJ", map2.get("cysj"));
					mapData.put("HOS_DETAIL_CODE", map2.get("hosDetailCode"));
					mapData.put("HOS_DETAIL_NAME", map2.get("hosDetailName"));
					mapData.put("CENTER_DETAIL_CODE", map2.get("centerDetailCode"));
					mapData.put("CENTER_DETAIL_NAME", map2.get("centerDetailName"));
					mapData.put("DRG_DETAIL_CODE", map2.get("drgDetailCode"));
					mapData.put("TYPE", map2.get("type"));				
					mapData.put("IS_SPECIAL", map2.get("isSpecial"));
					mapData.put("DOSAGE", map2.get("dosage"));
					mapData.put("SPECIFICATION", map2.get("specification"));
					mapData.put("CONTRACTOR", map2.get("contractor"));
					mapData.put("UNIT", map2.get("unit"));
					mapData.put("D_PRICE", map2.get("dPrice"));
					mapData.put("D_NUMBER", map2.get("dNumber"));
					mapData.put("PRICE", map2.get("price"));
					mapData.put("PROPORITION", map2.get("proporition"));
					mapData.put("PRICE_TIME", map2.get("priceTime"));
					list.add(mapData);
				}
				reqParam.put("DETAILS", JsonUtil.writeValueAsString(list));
				//String req = JsonUtil.writeValueAsString(reqParam);
				log.info("调用病人服务明细入参为:"+reqParam);
				result = HttpClientUtil.sendHttpPostDrg(url+ApplicationUtils.getPropertyValue("DRG.SERVICEDETAIL.URL", ""), reqParam);
				//String result =this.post(url+ApplicationUtils.getPropertyValue("DRG.SERVICEDETAIL.URL", ""), reqParam);
				log.info("调用病人服务明细返回结果为:"+result);
				log.info("****************结束调用病人服务明细*****************");
				JSONObject json =JSONObject.parseObject(result.toString());
				Integer stuCode= (Integer)json.get("CODE");
				if(200!=stuCode.intValue()){
					throw new BusException("病人服务明细失败，失败信息:"+json.get("MESSAGE")+"\n"+json.get("FAILDATA"));
				}
			}
			return result;
		} catch (Exception e) {
			log.info("病人服务明细接口异常"+e.getMessage());
			throw new BusException("病人服务明细接口异常"+e.getMessage());
		}
	}
	
	/**
	 * 病案质控	
	 * 时间没有处理
	 * @param args
	 * @return
	 */
	public Object controlUpload(Object...args){
		try {
			log.info("****************开始调用病案质控*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			reqParam.put("APP_ID", appId);
			//reqParam.put("ORG_CODE", orgCode);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("pkPv", args[0]);
			List<Map<String, Object>> orglist=drgInfoMapper.queryServiceDetail(map);
			if(orglist == null || orglist.size() == 0 ){
				throw new BusException("没有查询到数据，请检查！");
			}
			Map<String, Object> mapParam=orglist.get(0);
			reqParam.put("ORG_CODE", orgCode);
			//reqParam.put("YBLSH", mapParam.get("yblsh").toString());
			reqParam.put("ZYCS", mapParam.get("zycs").toString());
			reqParam.put("BAH", mapParam.get("bah").toString());
			reqParam.put("RULE",null);
			reqParam.put("MSG",null);
			reqParam.put("START_DATE",getDate(1)+" 00:00:00");
			reqParam.put("END_DATE",getDate(2)+" 23:59:59");
			//String req = JsonUtil.writeValueAsString(reqParam);
			log.info("调用病案质控入参为:"+reqParam);
			String result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.CONTROL.URL", ""), reqParam);
			log.info("调用病案质控返回结果为:"+result);
			log.info("****************结束调用病案质控*****************");
			JSONObject json =JSONObject.parseObject(result.toString());
			Integer stuCode= (Integer)json.get("CODE");
			if(200!=stuCode.intValue()){
				throw new BusException("查询病案质控反馈失败，失败信息:"+json.get("MESSAGE"));
			}
			return result;
		} catch (Exception e) {
			log.info("病案质控反馈接口异常"+e.getMessage());
			throw new BusException("病案质控反馈接口异常"+e.getMessage());
		}
	}
	/**
	 * 分组结果查询
	 * 时间没有处理
	 * @param args
	 * @return
	 */
	public Object groupQuery(Object...args){
		try {
			log.info("****************开始调用分组结果查询*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE", orgCode);
			reqParam.put("START_DATE",getDate(1)+" 00:00:00");
			reqParam.put("END_DATE",getDate(2)+" 23:59:59");

			if(args[0]!=null && !StringUtils.isBlank(args[0].toString())){//获取个人

				Map<String, Object> map=new HashMap<String, Object>();
				map.put("pkPv", args[0]);
				List<Map<String, Object>> orglist=drgInfoMapper.queryControl(map);
				if(orglist == null || orglist.size() == 0 ){
					throw new BusException("没有查询到数据，请检查！");
				}
				Map<String, Object> mapParam=orglist.get(0);
				reqParam.put("ZYCS", mapParam.get("zycs").toString());
				reqParam.put("BAH", mapParam.get("bah").toString());
			}else{//获取全院
				if(args[1]!=null && !StringUtils.isBlank(args[1].toString())){
					reqParam.put("START_DATE",args[1]+" 00:00:00");
				}
				if(args[2]!=null && !StringUtils.isBlank(args[2].toString())){
					reqParam.put("END_DATE",args[2]+" 23:59:59");
				}
			}

			//reqParam.put("YBLSH", mapParam.get("yblsh").toString());
			//String req = JsonUtil.writeValueAsString(reqParam);
			log.info("调用分组结果入参为:"+reqParam);
			String result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.GROUPQUERY.URL", ""), reqParam);
			log.info("调用分组结果查询返回结果为:"+result);
			log.info("****************结束调用分组结果查询*****************");
			JSONObject json =JSONObject.parseObject(result.toString());
			Integer stuCode= (Integer)json.get("CODE");
			if(200!=stuCode.intValue()){
				throw new BusException("分组结果查询失败，失败信息:"+json.get("MESSAGE"));
			}
			return result;
		} catch (Exception e) {
			log.info("分组结果查询接口异常"+e.getMessage());
			throw new BusException("分组结果查询接口异常"+e.getMessage());
		}
	}
	/**
	 * 定时任务分组结果查询
	 * 时间没有处理
	 * @param args
	 * @return
	 */
	public Object groupQueryTask(Object...args){
		try {
			log.info("****************开始调用分组结果查询*****************");
			Map<String,String> reqParam = new HashMap<>();
			String appId=ApplicationUtils.getPropertyValue("DRG.APP_ID", "");
			String orgCode=ApplicationUtils.getPropertyValue("DRG.ORG_CODE", "");
			reqParam.put("APP_ID", appId);
			reqParam.put("ORG_CODE", orgCode);
			String date=DateUtils.formatDate(DateUtils.getPreDay(),"yyyy-MM-dd");
			
			reqParam.put("START_DATE",date+" 00:00:00");
			reqParam.put("END_DATE",date+" 23:59:59");
			//String req = JsonUtil.writeValueAsString(reqParam);
			log.info("调用分组结果入参为:"+reqParam);
			String result = HttpClientUtil.sendHttpPost(url+ApplicationUtils.getPropertyValue("DRG.GROUPQUERY.URL", ""), reqParam);
			log.info("调用分组结果查询返回结果为:"+result);
			log.info("****************结束调用分组结果查询*****************");
			JSONObject json =JSONObject.parseObject(result.toString());
			Integer stuCode= (Integer)json.get("CODE");
			if(200!=stuCode.intValue()){
				throw new BusException("分组结果查询失败，失败信息:"+json.get("MESSAGE"));
			}
			return result;
		} catch (Exception e) {
			log.info("分组结果查询接口异常"+e.getMessage());
			throw new BusException("分组结果查询接口异常"+e.getMessage());
		}
	}
	/**
	 * 获取最小数据集接口参数
	 * @return
	 */
	public List<Map<String, Object>> createParam(Map<String, Object> map){
		//1.先查询基本信息
		List<Map<String, Object>> orglist=drgInfoMapper.queryBasicInfo(map);
		if(orglist == null || orglist.size() == 0 ){
			throw new BusException("没有查询到基本信息，请检查！");
		}
		Map<String, Object> mapBasicParam=orglist.get(0);
		//String pkPage=(String) mapBasicParam.get("pkPage");
		///最小数据集map
		List<Map<String, Object>> minDatalist=new ArrayList<Map<String,Object>>();
		Map<String,Object> minData = new HashMap<>();
		for (Map<String, Object> map2 : orglist) {
			minData=new HashMap<String, Object>();
			minData.put("HOS_NAME", map2.get("hosName"));
			minData.put("HOS_ID", map2.get("hosId"));
			minData.put("YLFKFS", Integer.parseInt(map2.get("ylfkfs").toString()));
			minData.put("YBKH", map2.get("ybkh"));
			minData.put("YBLSH", map2.get("yblsh"));
			minData.put("JZLSH", map2.get("jzlsh"));
			minData.put("BAH", map2.get("bah"));
			minData.put("ZYCS",  Integer.parseInt(map2.get("zycs").toString()));
			if(null==map2.get("xb")){
				throw new BusException("患者性别为空，请检查！");
			}
			String sex=(String) map2.get("xb");
			int xb=0;
			if("0".equals(sex)){
				xb=0;
			}else if("1".equals(sex)){
				xb=1;
			}else if("2".equals(sex)){
				xb=2;
			}else if("9".equals(sex)){
				xb=9;
			}
			minData.put("XB", xb);
			minData.put("CSRQ", map2.get("csrq"));
			minData.put("NL", map2.get("nl"));
			minData.put("BZYZSNL", map2.get("bzyzsnl"));
			if(null==map2.get("nl") && null==map2.get("bzyzsnl")){
				String txt=map2.get("ageTxt").toString().split("D")[1];
				if(StringUtils.isNotEmpty(txt)){
					BigDecimal  d = new BigDecimal(txt);  
					minData.put("BZYZSNL", d.divide(new BigDecimal("30"),4, BigDecimal.ROUND_HALF_UP));
				}
			}
			minData.put("XSECSTZ", map2.get("xsecstz"));
			minData.put("XSERYTZ", map2.get("xserytz"));
			if(null==map2.get("rytj")){
				throw new BusException("入院途径为空，请检查！");
			}
			String rytjStr=(String) map2.get("rytj");
			int rytj=9;
			if("1".equals(rytjStr)){//急诊  
				rytj=1;
			}else if("2".equals(rytjStr)){//门诊
				rytj=2;
			}else if("3".equals(rytjStr)){//其他医疗机构转入
				rytj=3;
			}else if("9".equals(rytjStr)){//其他
				rytj=9;
			}
			minData.put("RYTJ",rytj);
			minData.put("RYSJ", map2.get("rysj"));
			minData.put("RYKB", map2.get("rykb"));
			minData.put("RYKBBM", map2.get("rykbbm"));
			
			
			Map<String, Object> transferMap=new HashMap<String, Object>();
			transferMap.put("pkPage", mapBasicParam.get("pkPage"));
			//2.查询转科信息
			minData.put("ZKSJ1", null);
			minData.put("ZKSJS1", null);
			minData.put("ZKKB1", null);
			minData.put("ZKSJ2", null);
			minData.put("ZKSJS2", null);
			minData.put("ZKKB2", null);
			minData.put("ZKSJ3", null);
			minData.put("ZKSJS3", null);
			minData.put("ZKKB3", null);
			List<Map<String, Object>> transferlist=drgInfoMapper.queryTransferInfo(transferMap);
			for (int i = 0; i < transferlist.size(); i++) {
				if(i<3){
					Map<String, Object> mapfor=transferlist.get(i);
					int a=i+1;
					minData.put("ZKSJ"+a,mapfor.get("zksj"));
					minData.put("ZKSJS"+a,mapfor.get("zjsjs"));
					minData.put("ZKKB"+a,mapfor.get("zkkb"));
				}
			}
			
			
			minData.put("CYSJ", map2.get("cysj").toString().substring(0,19));
			minData.put("CYKB", map2.get("cykb"));
			minData.put("CYKBBM", map2.get("cykbbm"));
			minData.put("CYZZYSGH", map2.get("czzzysgh"));
			minData.put("CYZZYSXM", map2.get("cyzzysxm"));
			minData.put("CYGCYSGH", map2.get("cygcysgh"));
			minData.put("CYGCYSXM", map2.get("cygcysxm"));
			minData.put("CYZGYSGH", map2.get("cyzgysgh"));
			minData.put("CYZGYSXM", map2.get("cyzgysxm"));
			minData.put("SJZYTS", map2.get("sjzyts"));
			minData.put("SJHL", map2.get("sjhl"));
			minData.put("EJHL", map2.get("ejhl"));
			minData.put("YJHL", map2.get("yjhl"));
			minData.put("TJHLT", map2.get("tjhlt"));
			minData.put("TJHLS", map2.get("tjhls"));
			
			
			//3.查询重症
			minData.put("RZZJHS1",null);
			minData.put("RZZJHSLX1",null);
			minData.put("RZZJHSJ1",null);
			minData.put("CZZJHSJ1",null);
			minData.put("RZZJHS2",null);
			minData.put("RZZJHSLX2",null);
			minData.put("RZZJHSJ2",null);
			minData.put("CZZJHSJ2",null);
			minData.put("RZZJHS3",null);
			minData.put("RZZJHSLX3",null);
			minData.put("RZZJHSJ3",null);
			minData.put("CZZJHSJ3",null);
			List<Map<String, Object>> iculist=drgInfoMapper.queryICUInfo(transferMap);
			for (int i = 0; i < iculist.size(); i++) {
				if(i<3){
					Map<String, Object> mapfor=transferlist.get(i);
					int a=i+1;
					minData.put("RZZJHS"+a,mapfor.get("rzzjhs"));
					minData.put("RZZJHSLX"+a,mapfor.get("rzzjhslx"));
					minData.put("RZZJHSJ"+a,mapfor.get("rzzjhsj"));
					minData.put("CZZJHSJ"+a,mapfor.get("czzjhsj"));
				}
			}
			
			minData.put("QJCS",map2.get("qjcs"));
			minData.put("QJCGCS",map2.get("qjcgcs"));
			minData.put("MJZSYMC",map2.get("mjzsymc"));
			minData.put("MJZSYBM",map2.get("mjzsybm"));
			minData.put("MJZSYBW",map2.get("mjzsybw"));
			minData.put("MJZSYSX",map2.get("mjzsysx"));
			minData.put("MJZZDMS",map2.get("mjzzsms"));
			
			
			//4.查询主诊断
			Map<String, Object> diagParamMap=new HashMap<String, Object>();
			diagParamMap.put("pkPage", mapBasicParam.get("pkPage"));
			diagParamMap.put("flagPrimary", "1");//查询主诊断
			List<Map<String, Object>> mainDiaglist=drgInfoMapper.queryDiagInfo(diagParamMap);//查询主诊断
			if(mainDiaglist == null || mainDiaglist.size() == 0 ){
				throw new BusException("没有查询到主诊断信息，请检查！");
			}
			Map<String, Object> mainDiagMap=mainDiaglist.get(0);
			minData.put("SYMC",mainDiagMap.get("symc").toString().replaceAll("#", ""));
			minData.put("SYBM",mainDiagMap.get("sybm"));
			minData.put("SYBW",mainDiagMap.get("sybw"));
			minData.put("SYSX",mainDiagMap.get("sysx"));
			minData.put("ZDMS",mainDiagMap.get("zdms"));
			
			//4.查询诊断15条
			//生成15个默认值
			for (int i = 0; i < 14; i++) {
				int a=i+1;
				minData.put("SYMC"+a,null);
				minData.put("SYBM"+a,null);
				minData.put("SYBW"+a,null);
				minData.put("SYSX"+a,null);
				minData.put("ZDMS"+a,null);
				minData.put("SFZY"+a,null);
			}
			diagParamMap.put("flagPrimary", "0");//查询诊断
			List<Map<String, Object>> diaglist=drgInfoMapper.queryDiagInfo(diagParamMap);//查询诊断
			List<Map<String, Object>> diaglistNew=new ArrayList<Map<String,Object>>();
			Map<String,Object> m=new HashMap<String, Object>();
			for (int i = 0; i < diaglist.size(); i++) {
				if(!m.containsKey(diaglist.get(i).get("sybm"))){
					diaglistNew.add(diaglist.get(i));
					m.put(diaglist.get(i).get("sybm").toString(), diaglist.get(i).get("sybm"));
				}
			}
			for (int i = 0; i < diaglistNew.size(); i++) {
				if(i<15){
					Map<String, Object> mapfor=diaglistNew.get(i);
					int a=i+1;
					if(!mapfor.get("symc").toString().replaceAll("#", "").equals(minData.get("SYMC"))){
						minData.put("SYMC"+a,mapfor.get("symc").toString().replaceAll("#", ""));
						minData.put("SYBM"+a,mapfor.get("sybm"));
						minData.put("SYBW"+a,mapfor.get("sybw"));
						minData.put("SYSX"+a,mapfor.get("sysx"));
						minData.put("ZDMS"+a,mapfor.get("symc"));
						minData.put("SFZY"+a,mapfor.get("sfzy"));
					}
				}
			}
			/*******************************上面为诊断信息*************************************/
			//5.CCHI信息
			//生成8个默认值
			for (int i = 0; i < 8; i++) {
				int a=i+1;
				minData.put("CCHIBM"+a,null);
				minData.put("CCHIMC"+a,null);
				minData.put("CCHIXSF"+a,null);
				minData.put("SFZYSS"+a,null);
				minData.put("CCHIZDYSGH"+a,null);
				minData.put("CCHIZDYSXM"+a,null);
				minData.put("CCHIZDYSKB"+a,null);
				minData.put("CCHIZDYSKBBM"+a,null);
			}
			List<Map<String, Object>> cchilist=drgInfoMapper.queryCCHIInfo(map);//查询CCHI信息
			for (int i = 0; i < cchilist.size(); i++) {
				if(i<8){
					Map<String, Object> mapfor=cchilist.get(i);
					int a=i+1;
					minData.put("CCHIBM"+a,mapfor.get("cchibm"));
					minData.put("CCHIMC"+a,mapfor.get("cchimc").toString().replaceAll("\r|\n", ""));
					minData.put("CCHIXSF"+a,mapfor.get("cchixsf"));
					minData.put("SFZYSS"+a,mapfor.get("sfzyss"));
					minData.put("CCHIZDYSGH"+a,mapfor.get("cchizdysgh"));
					minData.put("CCHIZDYSXM"+a,mapfor.get("cchizdysxm"));
					minData.put("CCHIZDYSKB"+a,mapfor.get("cchizdyskb"));
					minData.put("CCHIZDYSKBBM"+a,mapfor.get("cchizdyskbbm"));
				}
			}
			/*******************************上面为CCHI信息*************************************/
			
			if(null==map2.get("lyfs")){
				throw new BusException("离院方式为空，请检查！");
			}
			String lyfsStr=(String) map2.get("lyfs");
			int lyfs=9;
			if("1".equals(lyfsStr)){//医嘱离院
				lyfs=1;
			}else if("2".equals(lyfsStr)){//医嘱转院
				lyfs=2;
			}else if("3".equals(lyfsStr)){//医嘱转社区卫生服务机构/乡镇卫生院
				lyfs=3;
			}else if("4".equals(lyfsStr)){// 非医嘱离院
				lyfs=4;
			}else if("5".equals(lyfsStr)){//死亡
				lyfs=5;
			}else if("9".equals(lyfsStr)){// 其他
				lyfs=9;
			}
			
			minData.put("LYFS",lyfs);
			minData.put("HXJSYSJS",map2.get("hxjsysjs"));
			minData.put("HXJSYSJF",map2.get("hxjsysjf"));
			minData.put("ZFY",map2.get("zfy"));
			minData.put("ZFJE",map2.get("zfje"));
			//查询收费项目
			minData.put("ZLCZF", 0);
			minData.put("HLF", 0);
			minData.put("QTFY", 0);
			minData.put("BLZDF", 0);
			minData.put("SYSZDF", 0);
			minData.put("YXXZDF", 0);
			minData.put("LCZDXMF", 0);
			minData.put("FSSZLXMF", 0);
			minData.put("WLZLF", 0);
			minData.put("SSZLF", 0);
			minData.put("MZF", 0);
			minData.put("SSF", 0);
			minData.put("KFF", 0);
			minData.put("ZYZLF", 0);
			minData.put("XYF", 0);
			minData.put("KJYWF", 0);
			minData.put("ZCYF", 0);
			minData.put("ZCYF1", 0);
			minData.put("XF", 0);
			minData.put("BDBLZPF", 0);
			minData.put("QDBLZPF", 0);
			minData.put("NXYZLZPF", 0);
			minData.put("XBYZLZPF", 0);
			minData.put("HCYYCLF", 0);
			minData.put("YYCLF", 0);
			minData.put("YCXYYCLF", 0);
			minData.put("QTF", 0);
			List<Map<String, Object>> itemlist=drgInfoMapper.queryItemInfo(transferMap);
			BigDecimal ZCF=BigDecimal.ZERO,PTCWF=BigDecimal.ZERO,ZZJHCWF=BigDecimal.ZERO;
			for (Map<String, Object> map3 : itemlist) {
				if(map3.get("itemcode").equals("0103")){//诊查费
					ZCF=(BigDecimal) map3.get("amount");
				}else if(map3.get("itemcode").equals("0104")){//普通床位费
					PTCWF=(BigDecimal) map3.get("amount");
				}else if(map3.get("itemcode").equals("0105")){//重症监护床位费
					ZZJHCWF=(BigDecimal) map3.get("amount");
				}else if(map3.get("itemcode").equals("02")){//一般治疗操作费
					minData.put("ZLCZF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("03")){//护理费
					minData.put("HLF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("04")){//其他费用
					minData.put("QTFY", map3.get("amount"));
				}else if(map3.get("itemcode").equals("05")){//病理诊断费
					minData.put("BLZDF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("06")){//实验室诊断费
					minData.put("SYSZDF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("07")){//影像学诊断费
					minData.put("YXXZDF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("08")){//临床诊断项目费
					minData.put("LCZDXMF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("09")){//非手术治疗项目费
					minData.put("FSSZLXMF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("0901")){//临床物理治疗费
					minData.put("WLZLF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("10")){//手术治疗费
					minData.put("SSZLF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("1001")){//麻醉费
					minData.put("MZF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("1002")){//手术费
					minData.put("SSF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("11")){//康复费
					minData.put("KFF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("12")){//中医治疗费
					minData.put("ZYZLF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("13")){//西药费
					minData.put("XYF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("1301")){//抗菌药物费
					minData.put("KJYWF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("14")){//中成药费
					minData.put("ZCYF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("15")){//中草药费
					minData.put("ZCYF1", map3.get("amount"));
				}else if(map3.get("itemcode").equals("16")){//血费
					minData.put("XF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("17")){//白蛋白类制品费
					minData.put("BDBLZPF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("18")){//球蛋白类制品费
					minData.put("QDBLZPF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("19")){//凝血因子类制品费
					minData.put("NXYZLZPF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("20")){//细胞因子类制品费
					minData.put("XBYZLZPF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("21")){//检查用一次性医用材料费
					minData.put("HCYYCLF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("22")){//治疗用一次性医用材料费
					minData.put("YYCLF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("23")){//手术用一次性医用材料费
					minData.put("YCXYYCLF", map3.get("amount"));
				}else if(map3.get("itemcode").equals("24")){//其他费
					minData.put("QTF", map3.get("amount"));
				}
				
			}
			minData.put("ZCF",ZCF);
			minData.put("PTCWF",PTCWF);
			minData.put("ZZJHCWF",ZZJHCWF);
			minData.put("YLFUF",ZCF.add(PTCWF).add(ZZJHCWF));//一般医疗服务费
			
			minData.put("CBCWF",map2.get("cbcwf"));
			minData.put("CWHCF",map2.get("cwhcf"));
			minData.put("CWYPF",map2.get("cwypf"));
			minData.put("CWXMF",map2.get("cwxmf"));
			minData.put("CBLX_CODE",map2.get("cblxCode"));
			minData.put("BA_TYPE",map2.get("dtDrgcasetype"));
			
			minDatalist.add(minData);
		}
		return minDatalist;
	}
	/**
	 * 获取当前月第一天或最后一天
	 * @param type 1是第一天，2是最后一天
	 * @return
	 */
	public String getDate(int type){
		String str=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		if(1==type){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH,1);//1:本月第一天
			str= format.format(c.getTime());
		}else{
			Calendar ca = Calendar.getInstance();
			ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
			str= format.format(ca.getTime());
		}
		return str;
	}
	
	public static String post(String url, Map<String,String> params){
		BufferedReader in = null;
		try {
			HttpClient client = HttpClientUtil.getHttpClient();
			HttpPost request = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext();) {
				String key =iter.next();
				String value =params.get(key);
				nvps.add(new BasicNameValuePair(key, value));
			}
			request.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
			HttpResponse response = client.execute(request);
			int code = response.getStatusLine().getStatusCode();
			if(code == HttpStatus.SC_OK) {
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				return sb.toString();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取属性汉字名
	 * @param memo
	 * @return
	 */
	private String getFieldName(String memo){
		String str="";
		String [] fields=memo.split(",");
		for (String field : fields) {
			String strAttr=field;
			if(isNumber(field)){
				strAttr=field.substring(0,field.length()-1);
			}
			if(StringUtils.isEmpty(str)){
				str=fieldMap.get(strAttr);
			}else{
				str=str+","+fieldMap.get(strAttr);
			}
		}
		return str;
	}
	/**
	 * 判断字符是否为数字
	 * @param str
	 * @return
	 */
	private static boolean isNumber(String str){
		str=str.substring(str.length()-1, str.length());
		if(str.matches("[0-9]+")){
			return true;
		}else{
			return false;
		}
	}
	static {
		fieldMap.put("HOS_NAME", "医疗机构名称");
		fieldMap.put("HOS_ID", "医疗服务网点代码");
		fieldMap.put("YLFKFS", "医疗付费方式");
		fieldMap.put("YBKH", "医保卡号");
		fieldMap.put("YBLSH", "医保流水号");
		fieldMap.put("JZLSH", "就诊流水号");
		fieldMap.put("BAH", "病案号");
		fieldMap.put("ZYCS", "住院次数");
		fieldMap.put("XM", "姓名");
		fieldMap.put("XB", "性别");
		fieldMap.put("CSRQ", "出生日期");
		fieldMap.put("NL", "年龄");
		fieldMap.put("BZYZSNL", "(年龄不足1周岁的)年龄(月)");
		fieldMap.put("XSECSTZ", "新生儿出生体重(克)");
		fieldMap.put("XSERYTZ", "新生儿入院体重(克）");
		fieldMap.put("RYTJ", "入院途径");
		fieldMap.put("RYSJ", "入院时间");
		fieldMap.put("RYKB", "入院科别");
		fieldMap.put("RYKBBM", "入院科别编码");
		/*******************共3组********************/
		fieldMap.put("ZKSJ", "转科时间");
		fieldMap.put("ZKSJS", "转科时间-时");
		fieldMap.put("ZKKB", "转科科别");
		/***********/
		fieldMap.put("CYSJ", "出院时间");
		fieldMap.put("CYKB", "出院科别");
		fieldMap.put("CYKBBM", "出院科别编码");
		fieldMap.put("CYZZYSGH", "出院主诊医生工号");
		fieldMap.put("CYZZYSXM", "出院主诊医生姓名");
		fieldMap.put("CYGCYSGH", "出院管床医生工号");
		fieldMap.put("CYGCYSXM", "出院管床医生姓名");
		fieldMap.put("CYZGYSGH", "出院主管医生工号");
		fieldMap.put("CYZGYSXM", "出院主管医生姓名");
		fieldMap.put("SJZYTS", "实际住院(天)");
		fieldMap.put("SJHL", "三级护理（天）");
		fieldMap.put("EJHL", "二级护理（天）");
		fieldMap.put("YJHL", "一级护理（天）");
		fieldMap.put("TJHLT", "特级护理（天）");
		fieldMap.put("TJHLS", "特级护理（小时）");
		/*******************共3组********************/
		fieldMap.put("RZZJHS", "是否入住重症监护室");
		fieldMap.put("RZZJHSLX", "入住重症监护室类型");
		fieldMap.put("RZZJHSJ", "进重症监护病房时间");
		fieldMap.put("CZZJHSJ", "出重症监护病房时间");
		/***********/
		fieldMap.put("QJCS", "抢救（次）");
		fieldMap.put("QJCGCS", "抢救成功（次）");
		fieldMap.put("MJZSYMC", "门（急）诊地区临床诊断术语");
		fieldMap.put("MJZSYBM", "门（急）诊地区临床诊断术语编码");
		fieldMap.put("MJZSYBW", "门（急）诊地区临床诊断术语部位");
		fieldMap.put("MJZSYSX", "门（急）诊地区临床诊断术语属性");
		fieldMap.put("MJZZDMS", "门（急）诊地区临床诊断术语诊断描述");
		fieldMap.put("SYMC", "地区临床诊断术语(主要诊断术语)");
		fieldMap.put("SYBM", "地区临床诊断术语(主要诊断术语)编码");
		fieldMap.put("SYBW", "地区临床诊断术语(主要诊断术语)部位");
		fieldMap.put("SYSX", "地区临床诊断术语(主要诊断术语)属性");
		fieldMap.put("ZDMS", "地区临床诊断术语(主要诊断术语)诊断描述");
		/*******************共15组********************/
		fieldMap.put("SYMC", "地区临床诊断术语");
		fieldMap.put("SYBM", "地区临床诊断术语编码");
		fieldMap.put("SYBW", "地区临床诊断术语部位");
		fieldMap.put("SYSX", "地区临床诊断术语属性");
		fieldMap.put("ZDMS", "诊断描述");
		fieldMap.put("SFZY", "是否治疗");
		/***********/
		/*******************共8组********************/
		fieldMap.put("CCHIBM", "CCHI编码");
		fieldMap.put("CCHIMC", "CCHI名称");
		fieldMap.put("CCHIXSF", "CCHI修饰符");
		fieldMap.put("SFZYSS", "是否为主要手术/操作/内科治疗方式");
		fieldMap.put("CCHIZDYSGH", "CCHI主刀医生工号");
		fieldMap.put("CCHIZDYSXM", "CCHI主刀医生姓名");
		fieldMap.put("CCHIZDYSKB", "CCHI主刀医生科别");
		fieldMap.put("CCHIZDYSKBBM", "CCHI主刀医生科别编码");
		/***********/
		fieldMap.put("LYFS", "离院方式");
		fieldMap.put("HXJSYSJS", "呼吸机使用时间（小时）");
		fieldMap.put("HXJSYSJF", "呼吸机使用时间（分）");
		fieldMap.put("ZFY", "住院总费用");
		fieldMap.put("ZFJE", "自付金额");
		fieldMap.put("YLFUF", "一般医疗服务费");
		fieldMap.put("ZCF", "诊察费");
		fieldMap.put("PTCWF", "普通床位费");
		fieldMap.put("ZZJHCWF", "重症监护床位费");
		fieldMap.put("ZLCZF", "一般治疗操作费");
		fieldMap.put("HLF", "护理费");
		fieldMap.put("QTFY", "其他费用");
		fieldMap.put("BLZDF", "病理诊断费");
		fieldMap.put("SYSZDF", "实验室诊断费");
		fieldMap.put("YXXZDF", "影像学诊断费");
		fieldMap.put("LCZDXMF", "临床诊断项目费");
		fieldMap.put("FSSZLXMF", "非手术治疗项目费");
		fieldMap.put("WLZLF", "临床物理治疗费");
		fieldMap.put("SSZLF", "手术治疗费");
		fieldMap.put("MZF", "麻醉费");
		fieldMap.put("SSF", "手术费");
		fieldMap.put("KFF", "康复费");
		fieldMap.put("ZYZLF", "中医治疗费");
		fieldMap.put("XYF", "西药费");
		fieldMap.put("KJYWF", "抗菌药物费");
		fieldMap.put("ZCYF", "中成药费");
		//fieldMap.put("ZCYF1", "中草药费");
		fieldMap.put("XF", "血费");
		fieldMap.put("BDBLZPF", "白蛋白类制品费");
		fieldMap.put("QDBLZPF", "球蛋白类制品费");
		fieldMap.put("NXYZLZPF", "凝血因子类制品费");
		fieldMap.put("XBYZLZPF", "细胞因子类制品费");
		fieldMap.put("HCYYCLF", "检查用一次性医用材料费");
		fieldMap.put("YYCLF", "治疗用一次性医用材料费");
		fieldMap.put("YCXYYCLF", "手术用一次性医用材料费");
		fieldMap.put("QTF", "其他费");
		fieldMap.put("CBCWF", "超标床位费");
		fieldMap.put("CWHCF", "除外耗材费");
		fieldMap.put("CWYPF", "除外药品费");
		fieldMap.put("CWXMF", "除外项目费");
		fieldMap.put("CBLX_CODE", "参保类型");
		fieldMap.put("BA_TYPE", "病例类型");
    }
}
