package com.zebone.nhis.labor.nis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.labor.nis.PvLaborGram;
import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.common.module.nd.record.NdRecordRow;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.labor.nis.dao.PvLaborGramMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 产程图业务处理类
 * @author yangxue
 *
 */
@Service
public class PvLaborGramService {
	@Resource
	private PvLaborGramMapper pvLaborGramMapper;
	/***
	 * 保存产程图内容
	 * @param param
	 * @param user
	 */
     public void saveLaborGram(String param,IUser user){
    	 List<PvLaborGram> gramlist = JsonUtil.readValue(param, new TypeReference<List<PvLaborGram>>(){});
    	 if(gramlist == null || gramlist.size()<=0) return;
    	 for(PvLaborGram gram:gramlist){
    		 if(CommonUtils.isEmptyString(gram.getPkLaborgram())){
    			 DataBaseHelper.insertBean(gram);
    		 }else{
    			 gram.setModifier(((User)user).getPkEmp());
    			 gram.setTs(new Date());
    			 gram.setPkEmp(((User)user).getPkEmp());
    			 gram.setNameEmp(((User)user).getNameEmp());
    			 DataBaseHelper.updateBeanByPk(gram, false);
    		 }
    	 }
    	 
     }
     /**
      * 查询产程图记录内容
      * @param param{pkPv,pkLaborrec}
      * @param user
      * @return
      */
     public List<PvLaborGram> queryGramInfo(String param,IUser user){
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 if(paramMap == null || paramMap.get("pkPv") == null)
    		 throw new BusException("未获取到就诊相关参数！");
    	 return pvLaborGramMapper.queryPvLaborGram(paramMap);
     }
     /**
      * 删除产程图中的记录
      */
     public void deleteGramInfo(String param,IUser user){
    	 List<PvLaborGram> gramlist = JsonUtil.readValue(param, new TypeReference<List<PvLaborGram>>(){});
    	 if(gramlist == null||gramlist.size()<=0) return;
    	 for(PvLaborGram gram : gramlist){
    		 DataBaseHelper.deleteBeanByPk(gram);
    	 }
     }
     /**
      * 设置临产时间，并删除小于临产时间的数据{pkLaborgram,dateEntry}
      */
     public void deleteGramInfoByTime(String param,IUser user){
    	 Map<String,String> paramMap = JsonUtil.readValue(param,Map.class);
    	 if(paramMap == null || CommonUtils.isEmptyString(paramMap.get("dateEntry")))
    		 throw new BusException("未获取到设置临产时间相关参数！");
    	 String sql = "delete from PV_LABOR_GRAM where DATE_ENTRY < to_date('"+paramMap.get("dateEntry")+"','YYYYMMDDHH24MISS')";
    	 DataBaseHelper.execute(sql, new Object[]{});
    	 if(paramMap.get("pkLaborgram")!=null&&!CommonUtils.isEmptyString(paramMap.get("pkLaborgram"))){
    		 DataBaseHelper.execute("update PV_LABOR_GRAM set flag_lc='1',note='临产时间' where PK_LABORGRAM = ? ", new Object[]{paramMap.get("pkLaborgram")});
    	 }
     }
     /**
      * 查询一般护理记录单内容
      * @param param{pkRecord,pkPv,pkLaborrec}
      * @param user
      * @return
      */
     public List<PvLaborGram> queryNdRecord(String param,IUser user){
    	 Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
    	 if(CommonUtils.isNull(paramMap.get("pkRecord")))
    		 throw new BusException("未获取到一般护理记录单主键！");
    	 List<NdRecordRow> list  = pvLaborGramMapper.queryNdRecordRow(paramMap.get("pkRecord"));
 		 if (list == null || list.size() <= 0)
 			return null;
 		List<PvLaborGram> gramlist = new ArrayList<PvLaborGram>();
 		String pkPv = paramMap.get("pkPv");
 		String pkLaborrec = paramMap.get("pkLaborrec");
 		String pkOrg = ((User)user).getPkOrg();
 		//查询分娩方式
	    List<Map<String,Object>> outlist = pvLaborGramMapper.queryLaborMode(pkLaborrec);
 		for (NdRecordRow row : list) {
 			PvLaborGram gram = new PvLaborGram();
 			//查询列记录
 			List<NdRecordDt> dtlist = pvLaborGramMapper.queryNdRecordDt(row.getPkRecordrow());
 			if(dtlist!=null&&dtlist.size()>0){
 				for(NdRecordDt dt:dtlist){
 					if(CommonUtils.isEmptyString(dt.getVal()))
 						continue;
 					if("05".equals(dt.getDtNdcoltype())){//舒张压
 						gram.setValDbp(dt.getVal());
 					}else if("06".equals(dt.getDtNdcoltype())){//收缩压
 						gram.setValSbp(dt.getVal());
 					}else if("07".equals(dt.getDtNdcoltype())){//宫缩间歇
 						gram.setValJx(dt.getVal());
 					}else if("08".equals(dt.getDtNdcoltype())){//宫缩持续秒数
 						gram.setValCx(dt.getVal());
 					}else if("09".equals(dt.getDtNdcoltype())){//宫口扩张
 						gram.setValDfc(dt.getVal());
 					}else if("10".equals(dt.getDtNdcoltype())){//胎心率
 						gram.setValFhr(dt.getVal());
 					}else if("11".equals(dt.getDtNdcoltype())){//先露高低
 						gram.setValSo(dt.getVal());
 					}else if("13".equals(dt.getDtNdcoltype())){//备注
 						gram.setNote(dt.getVal());
 					}
 					
 					gram.setTs(new Date());//用来标识是否有值
 				}
 			}
 			if(gram.getTs()!=null){
 				gram.setDateEntry(row.getDateEntry());
 	 			gram.setPkEmp(row.getPkEmp());
 	 			gram.setNameEmp(row.getNameEmp());
 	 			gram.setPkLaborrec(pkLaborrec);
 	 			gram.setPkPv(pkPv);
 	 			gram.setPkOrg(pkOrg);
 				if(outlist!=null&&outlist.size()>0){
 	 				for(Map<String,Object> map:outlist){
 	 					//分娩时间=护理记录时间时，设置分娩方式
 	 					if(map.get("dateOut")!=null&&(Date)map.get("dateOut")==row.getDateEntry()){
 	 						gram.setDtOutMode(CommonUtils.getString(map.get("dtOutMode")));
 	 					}
 	 				}
 	 			}
 				gramlist.add(gram);
 			}
 			   
 		}
    	 return gramlist;
     }
     
     
}
