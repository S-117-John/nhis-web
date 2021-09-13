package com.zebone.nhis.scm.st.service;

import java.util.*;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.vo.PdRepriceHistVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.st.dao.StoreManagerMapper;
import com.zebone.nhis.scm.st.vo.PdStoreInfoVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 库存管理
 * @author yangxue
 *
 */
@Service
public class StoreManagerService {
	@Resource
	private StoreManagerMapper storeManagerMapper;
	/**
	 * 查看物品库存信息
	 * @param param{code,name,dtDosage,dtAnti,dtPois,dateBegin,dateEnd,price,flagUp,flagDown}
	 * @param user
	 * @return
	 */
    public List<PdStoreInfoVo> queryPdStoreInfoList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	if(map == null)
    		map = new HashMap<String,Object>();
    	map.put("pkStore", ((User)user).getPkStore());
    	if(CommonUtils.isNotNull(map.get("dateBegin"))){
			map.put("dateBegin", CommonUtils.getString(map.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
    	return storeManagerMapper.queryPdStoreInfoList(map);
    }
    
    /**
     * 008006009009
     * 库存药品汇总查询
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> queryPdStockSum(String param,IUser user){
    	Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
    	if(paramMap == null)
    		paramMap = new HashMap<String,Object>();
    	paramMap.put("pkStore", ((User)user).getPkStore());
    	if(CommonUtils.isNotNull(paramMap.get("dateBegin"))){
    		paramMap.put("dateBegin", CommonUtils.getString(paramMap.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(paramMap.get("dateEnd"))){
			paramMap.put("dateEnd", CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8)+"235959");
		}
		List<Map<String,Object>> resList=storeManagerMapper.qryPdStockSum(paramMap);
		return resList;
    }
    /**
     * 查看可用批次
     * @param param{pkStore,pkPd}
     * @param user
     * @return
     */
    public List<PdStDtVo> queryStDtList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	if(map == null || CommonUtils.isNull(map.get("pkStore"))||CommonUtils.isNull(map.get("pkPd")))
    		throw new BusException("未获取到当前操作的仓库和物品信息！");
    	if(map.get("dateExpire")!=null){
    		map.put("dateExpire", CommonUtils.getString(map.get("dateExpire")).substring(0, 8)+"000000");
    	}
    	//当前取的单价均为零售单价，根据不同的包装取不同的包装单价
    	List<PdStDtVo> resList=storeManagerMapper.queryStDtList(map);
    	for (int i = 0; i < resList.size(); i++) {
    		PdStDtVo dtVo=resList.get(i);
    		dtVo.setPrice(MathUtils.mul(MathUtils.div(dtVo.getPrice(),CommonUtils.getDouble(dtVo.getPackSizePd())),CommonUtils.getDouble(dtVo.getPackSize())));
    		dtVo.setPriceCost(MathUtils.mul(MathUtils.div(dtVo.getPriceCost(),CommonUtils.getDouble(dtVo.getPackSizePd())),CommonUtils.getDouble(dtVo.getPackSize())));
		}
    	return resList;
    }
    /**
     * 设置物品停用
     * @param param{pkPdStore,stopType[0:住院,1:门诊,2:急诊]}
     * @param user
     */
    public void setPdStop(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
    	if(paramMap==null||paramMap.get("pkPdStore")==null||paramMap.get("stopType")==null) throw new BusException("未获取到需要设置停用的物品信息!");
    	StringBuilder sql = new StringBuilder("");
    	if("0".equals(paramMap.get("stopType").toString())){
    	    sql = new StringBuilder(" update  pd_stock set flag_stop='1' ")
    			 .append(",ts = to_date('")
    			 .append(DateUtils.getDefaultDateFormat().format(new Date()))
    			 .append("','YYYYMMDDHH24MISS') ")
    			 .append(" where pk_pdstock=:pkPdStore ");
    	}else if("1".equals(paramMap.get("stopType").toString())){
    		sql = new StringBuilder("  update  pd_stock set flag_stop_op='1' ")
    		      .append(",ts = to_date('")
        		  .append(DateUtils.getDefaultDateFormat().format(new Date()))
        		  .append("','YYYYMMDDHH24MISS') ")
        		  .append(" where pk_pdstock=:pkPdStore ");
    	}else if("2".equals(paramMap.get("stopType").toString())){
    		sql =  new StringBuilder("  update  pd_stock set flag_stop_er='1' ")
        		  .append(",ts = to_date('")
        		  .append(DateUtils.getDefaultDateFormat().format(new Date()))
        		  .append("','YYYYMMDDHH24MISS') ")
        		  .append(" where pk_pdstock=:pkPdStore ");
    	}
    	DataBaseHelper.update(sql.toString(), paramMap);
    }
    /**
     * 设置物品启用
     * @param param{pkPdStore,stopType[0:住院,1:门诊,2:急诊]}
     * @param user
     */
    public void setPdEnable(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
    	if(paramMap==null||paramMap.get("pkPdStore")==null||paramMap.get("stopType")==null) throw new BusException("未获取到需要设置停用的物品信息!");
    	StringBuilder sql = new StringBuilder("");
    	if("0".equals(paramMap.get("stopType").toString())){
    	    sql = new StringBuilder(" update  pd_stock set flag_stop='0' ")
    	    		.append(",ts = to_date('").append(DateUtils.getDefaultDateFormat().format(new Date()))
    	    		.append("','YYYYMMDDHH24MISS') ")
    	    		.append("  where pk_pdstock=:pkPdStore and flag_stop='1'");
    	}else if("1".equals(paramMap.get("stopType").toString())){
    		sql = new StringBuilder(" update  pd_stock set flag_stop_op='0' ")
        		.append(",ts = to_date('")
        		.append(DateUtils.getDefaultDateFormat().format(new Date()))
        		.append("','YYYYMMDDHH24MISS') ")
        		.append(" where pk_pdstock=:pkPdStore and flag_stop_op='1'");
    	}else if("2".equals(paramMap.get("stopType").toString())){
    		sql = new StringBuilder(" update  pd_stock set flag_stop_er='0' ")
        		 .append(",ts = to_date('")
        		 .append(DateUtils.getDefaultDateFormat().format(new Date()))
        		 .append("','YYYYMMDDHH24MISS') ")
        		 .append(" where pk_pdstock=:pkPdStore and flag_stop_er='1'");
    	}
    	DataBaseHelper.update(sql.toString(), paramMap);
    }
    /**
     * 解除预留
     * @param param{type:（0,全部解除；1，单条解除）pkPdStock:库存主键}
     * @param user
     */
    public void unLockPrep(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	if(map == null)throw new BusException("未获取到解除预留方式！");
    	String type = CommonUtils.getString(map.get("type"));
    	if("0".equals(type)){//全部解除
    		String sql = "update pd_stock set quan_prep=0 "
    				+",ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') "
    				+ " where pk_store=? and quan_prep>0 ";
    		DataBaseHelper.update(sql, new Object[]{((User)user).getPkStore()});
    	}else if("1".equals(type)){//单条解除
    		String pk_pdstock = CommonUtils.getString(map.get("pkPdStock"));
    		String sql = "update pd_stock  set quan_prep=0 "
    				+",ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') "
    				+ " where pk_pdstock=? and quan_prep>0 ";
    		DataBaseHelper.update(sql, new Object[]{pk_pdstock});
    	}
    }
    /**
     * 查看调价历史
     * @param param{pkPd,dateBegin,dateEnd}
     * @param user
     * @return
     */
    public List<PdRepriceHistVo> queryPriceHist(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	if(map == null)
    		map = new HashMap<String,Object>();
    	map.put("pkStore", ((User)user).getPkStore());
    	if(CommonUtils.isNotNull(map.get("dateBegin"))){
			map.put("dateBegin", CommonUtils.getString(map.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
    	return storeManagerMapper.queryHistList(map);
    }
    /**
     * 校验用户密码
     * @param param{usercode,pwd}
     * @param user
     * @return
     */
    public String verfyPwd(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	if(map == null||map.get("pwd")==null)
    		throw new BusException("未获取到录入的密码");
    	String sql = "select count(*) from bd_ou_user where code_user = ? and pwd = ? ";
    	Integer u = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{map.get("usercode"),new SimpleHash("md5",map.get("pwd")).toHex()});
    	if(u>0)
    		return "1";
    	else 
    		return "密码错误！";
    }
    
    /**
     * 008006009008
     * 查询当前选中药品的所有库存
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryStockAll(String param,IUser user){
    	String pkPd=JsonUtil.getFieldValue(param, "pkPd");
    	if(pkPd==null ||"".equals(pkPd))return null;
    	return storeManagerMapper.qryStockAll(pkPd);
    	
    }

	/**
	 * 设置物品启用和停止
	 * @param param{pkPdStore,stopType[0:住院,1:门诊,2:急诊]}
	 * @param user
	 */
	public void setPdEnableAndStop(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		if(paramMap==null||paramMap.get("pkPdStores")==null){
			throw new BusException("未获取到需要设置的物品信息!");
		}
		//List<String> list= Arrays.asList(paramMap.get("pkPdStores").toString());
		List<String> list = new ArrayList<String>();
		if (paramMap.get("pkPdStores") instanceof ArrayList<?>) {
			for (Object o : (List<?>) paramMap.get("pkPdStores")) {
				list.add(String.class.cast(o));
			}
		}
        Set<String> set = new HashSet<>(list);
		StringBuilder sql = new StringBuilder("");
		
		sql = new StringBuilder(" update  pd_stock set ");
		sql.append(" ts = to_date('").append(DateUtils.getDefaultDateFormat().format(new Date()));
		sql.append("','YYYYMMDDHH24MISS') ");
		if(paramMap.containsKey("flagStop") && paramMap.get("flagStop")!=null){
			sql.append(" ,flag_stop=:flagStop ");
		}
		if(paramMap.containsKey("flagStopEr")&&paramMap.get("flagStopEr")!=null){
			sql.append(" ,flag_stop_er=:flagStopEr");
		}
		if(paramMap.containsKey("flagStopOp") && paramMap.get("flagStopOp")!=null){
			sql.append(",flag_stop_op=:flagStopOp");
		}
		sql.append("  where pk_pdstock in ( ");
		sql.append(CommonUtils.convertSetToSqlInPart(set, "pk_pdstock")+")");
		DataBaseHelper.update(sql.toString(), paramMap);
	}
}
