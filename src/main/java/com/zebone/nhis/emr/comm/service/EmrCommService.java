package com.zebone.nhis.emr.comm.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.jasig.cas.client.util.ReflectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.base.bd.code.BdWorkcalendardate;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.emr.comm.dao.EmrCommMapper;
import com.zebone.nhis.emr.comm.vo.BdSerialnoVo;
import com.zebone.nhis.emr.comm.vo.EmrMedRecPatVo;
import com.zebone.nhis.emr.comm.vo.EmrMedRecTaskVo;
import com.zebone.nhis.emr.common.EmrUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.build.BuildSql;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 电子病历-公共服务
 * @author chengjia
 *
 */
@Service
public class EmrCommService {
	
	@Resource
	private	EmrCommMapper commMapper;
	
	private boolean isInit = false;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public int getSerialNo(String tableName, String fieldName, int count, IUser user){
		if(tableName==null) return 0;

		if(!isInit){ 
			User u = (User)user;
			Double sn = commMapper.selectSn(tableName.toUpperCase(), fieldName.toUpperCase()); 
			if(sn==null){
				BdSerialnoVo initSn = new BdSerialnoVo();
				initSn.setPkSerialno(NHISUUID.getKeyId());
				initSn.setPkOrg(CommonUtils.getGlobalOrg());
				initSn.setNameTb(tableName.toUpperCase());
				initSn.setNameFd(fieldName.toUpperCase());
				initSn.setValInit((short)1000);
				initSn.setVal((short)1000);
				commMapper.initSn(initSn);
				isInit = true;
			}
		}
		int ret =  ApplicationUtils.getSerialNo(tableName,fieldName,count);
		return ret;
	}
	
	public int getSerialNo(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		return this.getSerialNo(paramMap.get("tableName"), paramMap.get("fieldName"), 
					Integer.parseInt(paramMap.get("count")), user);
	}
	
    /**
     * 查询医师病历书写任务
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public List<EmrMedRecTaskVo> queryEmpTaskList(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrMedRecTaskVo> rtnList=new ArrayList<EmrMedRecTaskVo>();
    	if(Application.isSqlServer()){
    		rtnList=(List<EmrMedRecTaskVo>)commMapper.queryEmpTaskListSql(map);
    	}else{
    		commMapper.queryEmpTaskList(map);
    		rtnList=(List<EmrMedRecTaskVo>)map.get("result");
    	}
    	//System.out.println("queryEmpTaskList:"+rtnList.size());
    	return rtnList;
    }
    
	/**
	 * 根据条件查询患者就诊记录
	 * @param pkPv/orderBy
	 * @param user
	 * @return
	 */
	public List<EmrPatList> queryEmrPatList(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		List<EmrPatList> list=commMapper.queryEmrPatList(map);
		
		return list;
	}	
	
	/**
	 *  保存CA签名记录
	 * @param list
	 */
	public void saveCnSignCas(String param , IUser user) {
		List<CnSignCa> list = JsonUtil.readValue(param, new TypeReference<List<CnSignCa>>(){});
		if(list==null||list.size()==0) return;
		
		 DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnSignCa.class),list);
	}
	
    /**
     * 查询医生病历任务患者列表
     * @param param
     * @param user
     * @return
     * @throws ParseException 
     */
    @SuppressWarnings({ "rawtypes","unused" })
	public List<EmrPatList> queryEmpPatTaskList(String param , IUser user) throws ParseException{
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<EmrPatList> rtnList=new ArrayList<EmrPatList>();
    	
    	List<BdWorkcalendardate> dateList=new ArrayList<BdWorkcalendardate>();
    	Map dateMap=new HashMap<Object,String>();
    	//dateMap.put("calendarDef", "1");
    	//dateMap.put("datetype", "1");
//    	Date today=new Date();
//    	Calendar calendar = Calendar.getInstance();  
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String dateStr = sdf.format(new Date());
//		Date begin =  sdf.parse(dateStr);
//		calendar.setTime(begin);
//		calendar.add(Calendar.DAY_OF_MONTH, 30);
//		Date end = calendar.getTime();
//    	dateMap.put("beginDate", begin);
//    	dateMap.put("endDate", end);
//    	dateList = commMapper.queryEmrWeekdays(dateMap);
//    	if(dateList==null||dateList.size()<3){
//    		calendar.setTime(begin);
//    		calendar.add(Calendar.DAY_OF_MONTH, 3);
//    		end = calendar.getTime();
//    	}else{
//    		BdWorkcalendardate endDate = dateList.get(2);
//    		end = endDate.getCalendardate();
//    	}
    	//map.put("beginDate", begin);
    	//map.put("endDate", end);
    	int days=3;
    	if(map.containsKey("days")&&map.get("days")!=null){
    		days=Integer.parseInt(map.get("days").toString());
    	}
    	map.put("days", days+1);
    	if (Application.isSqlServer()) {
        	rtnList = commMapper.queryEmpPatTaskListSql(map);
    	}else{
        	rtnList = commMapper.queryEmpPatTaskList(map);
    	}
    	return rtnList;
    }
    
    /**
     * 查询病历用工作日
     * @param params
     * @return
     */
    public List<BdWorkcalendardate> queryEmrWeekdays(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	List<BdWorkcalendardate> rtnList=new ArrayList<BdWorkcalendardate>();
    	rtnList = commMapper.queryEmrWeekdays(map);
    	return rtnList;
    	
    }
    
    /**
     * 保存病历扩展存储表信息
     * @param list
     */
    public void saveEmrExtData(List<Map<String,Object>> list,IUser user){
    	if(list==null||list.size()==0) return;
    	for(int i=0;i<list.size();i++){
    		Map<String,Object> tabMap=list.get(i);
//    		//表名//    		if(!tabMap.containsKey("clsName")) continue;
//    		if(!tabMap.containsKey("pkName")) continue;
//    		if(!tabMap.containsKey("pkValue")) continue;
//    		String tableName=tabMap.get("clsName").toString();
//    		if(tableName==null||tableName.equals("")) continue;
//    		String pkName=tabMap.get("pkName").toString();
//    		if(pkName==null||pkName.equals("")) continue;
//    		String pkValue=tabMap.get("pkValue").toString();
//    		if(pkValue==null||pkValue.equals("")) continue;
//
//    		//字段列表
//    		if(!tabMap.containsKey("columnMap")) continue;
//    		Map<String,Object> columnMap=(Map<String, Object>) tabMap.get("columnMap");
//    		columnMap.put(pkName, pkValue);
//    		String status="";
//    		if(tabMap.containsKey("status")) status=tabMap.get("status").toString();
//    		if(status==null) status="";
//    		
//			String sql="";
//			//先删后插
//			sql=" delete from "+tableName+" where "+pkName+" = '"+pkValue+"'";
//			DataBaseHelper.update(sql);
//    		if(!status.equals("del")){
//        		//非删除/插入
//    			updateData(tableName,columnMap,user);
//    		}
    		
    		String[] strs = saveExtDataExecute(tabMap,user);
    		if(tabMap.get("columnMap")!=null) {
    			Map<String,Object> columnMap=(Map<String, Object>) tabMap.get("columnMap");
	    		if(columnMap.get("items")!=null&&strs!=null&&strs.length==2) {
	        		List<Map<String,Object>> items=(List<Map<String, Object>>) columnMap.get("items");
	        		
	        		for(int j=0;j<items.size();j++){
	            		Map<String,Object> itemMap=items.get(j);
	            		if(itemMap.containsKey("columnMap")) {
	            			Map<String,Object> childMap = (Map<String, Object>) itemMap.get("columnMap");
	            			childMap.put(strs[0], strs[1]);
	            		}
	            		
	            		saveExtDataExecute(itemMap,user);
	            	}
	    		}
    		}

    	}
    }
    
    /**
     * 单表处理
     * @param tabMap
     * @param user
     * @return
     */
    private String[] saveExtDataExecute(Map<String,Object> tabMap,IUser user){
    	//表名
    	String[] pks=new String[2]; 
		if(!tabMap.containsKey("clsName")) return pks;
		if(!tabMap.containsKey("pkName")) return pks;
		if(!tabMap.containsKey("pkValue")) return pks;
		String tableName=tabMap.get("clsName").toString();
		if(tableName==null||tableName.equals("")) return pks;
		String pkName=tabMap.get("pkName").toString();
		if(pkName==null||pkName.equals("")) return pks;
		String pkValue=tabMap.get("pkValue").toString();
		if(pkValue==null||pkValue.equals("")) return pks;

		//字段列表
		if(!tabMap.containsKey("columnMap")) return pks;
		Map<String,Object> columnMap=(Map<String, Object>) tabMap.get("columnMap");
		//columnMap.put(pkName, pkValue);
		String status="";
		if(tabMap.containsKey("status")) status=tabMap.get("status").toString();
		if(status==null) status="";
		Map<String,Object> columnMapNew=new HashMap<String,Object>();
		pkName=EmrUtils.toLowerCaseFirstOne(pkName);
		columnMap.put(pkName, pkValue);
		for(String key : columnMap.keySet()){
        	if(key==null||key.equals("pkName")||key.equals("pkValue")||key.equals("clsName")
        			||key.equals("items")||key.equals("fatherClsName")||key.equals("status")) continue;
        	
        	columnMapNew.put(key, columnMap.get(key));
        }
		String sql="";
		//先删后插
		sql=" delete from "+tableName+" where "+EmrUtils.humpToUnderline(pkName)+" = '"+pkValue+"'";
		DataBaseHelper.update(sql);
		if(!status.equals("del")){
    		//非删除/插入
			updateData(tableName,columnMapNew,user);//columnMap
		}
		pks[0] = pkName;
		pks[1] = pkValue;
		
		return pks;
    }
    

	
    /**
     * 更新数据
     * @param tableName
     * @param columnMap
     * @param user
     */
    private void updateData(String tableName,Map<String,Object> columnMap,IUser user){
    	User u = (User)user;
    	columnMap.put("creator", u.getPkEmp());
    	Date now = new Date();
    	columnMap.put("create_time", now);
    	columnMap.put("ts", now);
    	StringBuffer names = new StringBuffer();
        StringBuffer values = new StringBuffer();
        //List<Object> paramList=new ArrayList<>();
        for(String key : columnMap.keySet()){
        	String propName=EmrUtils.humpToUnderline(key);
        	names.append(propName).append(",");
        	values.append(":").append(key).append(",");
        }
        
        names.deleteCharAt(names.length()-1);
        values.deleteCharAt(values.length()-1);
        
    	StringBuffer resultSql = new StringBuffer();
        resultSql.append("insert into ");
        resultSql.append(tableName);
        resultSql.append("(");
        resultSql.append(names);
        resultSql.append(") values (");
        resultSql.append(values);
        resultSql.append(")");
        
        String result = resultSql.toString();
        try {
        	DataBaseHelper.update(result, columnMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
        
        
    }
    
	/**
	 * 查询病历文档段落内容
	 * 说明：1、病程记录：typeCode[1000]paraCode[null] 2、主诉：typeCode[0900]paraCode[01]...
	 * @param pkPv就诊主键
	 * @param typeCode文档分类编码
	 * @param paraCode文档段落编码
	 * @return
	 */
	public List<EmrMedRecPatVo> getEmrRecByType(HashMap<String,Object> map){
		EmrMedRec rec=null;

		List<EmrMedRecPatVo> rtnList=commMapper.queryPatMedRecDoc(map);
		if(rtnList!=null&&rtnList.size()>0){
			rec=rtnList.get(0);
			EmrMedDoc doc =commMapper.getEmrMedDocById(rec.getPkDoc());
			rec.setMedDoc(doc);
		}

		return rtnList;
	}
}

