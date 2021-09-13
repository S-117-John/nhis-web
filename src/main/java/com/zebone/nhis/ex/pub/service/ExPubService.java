package com.zebone.nhis.ex.pub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.dao.ExPubMapper;
import com.zebone.nhis.ex.pub.vo.PatiInfoVo;
import com.zebone.nhis.ex.pub.vo.PdPriceVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 护士站对外接口
 * @author yangxue
 *
 */
@Service
public class ExPubService {
	@Resource
	private ExPubMapper exPubMapper;
	/**
	 * 查询病区患者床位列表
	 * @param param{pkOrg,pkDept}
	 * @param user
	 * @return
	 */
	public List<PatiInfoVo> queryPatiBedList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<PatiInfoVo> cardlist = exPubMapper.getPatiInfo(paramMap);
		return cardlist;
	}
	/**
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public BdResBed getBedInfoByPk(String param,IUser user){
  	  if(param == null ) return null;
  	  return DataBaseHelper.queryForBean("select * from bd_res_bed where pk_bed = ?", BdResBed.class, param);
    }
	/**
	 * 获取基数药品价格信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PdPriceVo> getPdBasePrice(Map<String,Object> paramMap){
		if(paramMap==null||CommonUtils.isNotNull(paramMap.get("pkDeptAp")))
			return null;
		return exPubMapper.queryPdBasePrice(paramMap);
	}
	/**
	 * 根据临床科室，查询住院静配药房
	 * @param param
	 * @param user
	 * @return{pkDept,nameDept}
	 */
	public Map<String,Object> getPivasDept(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||paramMap.get("pkDept")==null)
			throw new BusException("请传入临床科室主键pkDept！");
		return exPubMapper.queryPivasDept(paramMap);
	}
	 
    /**
     * 查询未完成记录
     * @param param
     */
    public List<Map<String, Object>> queryUnExTaskList(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	String unDeEx = ApplicationUtils.getSysparam("EX0032", false);
    	if("1".equals(unDeEx))
    		paramMap.put("flagAp", "1");
    	List<Map<String, Object>> list = new ArrayList<>();
    	//博爱判断是否为职能科室（血透、产房）
		String flagFunDept = paramMap.containsKey("flagFunDept") ? paramMap.get("flagFunDept") != null ? paramMap.get("flagFunDept").toString() : "0" : "0";
		if(CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("isLabor"))) && "0".equals(flagFunDept)){
			list = exPubMapper.queryUnExTaskList(paramMap);
		}else if(CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("isLabor"))) && "1".equals(flagFunDept)){
			list = exPubMapper.queryUnExTaskListForFunDept(paramMap);
		}else if("1".equals(CommonUtils.getString(paramMap.get("isLabor"))) && "1".equals(flagFunDept)){
			list = exPubMapper.queryUnExTaskListForFunLabor(paramMap);
		}else{
			list = exPubMapper.queryUnExTaskListForLabor(paramMap);
		}
    	return list;
    }
    
	/**
	 * 根据科室主键获取【临床科室与护理单元】业务线的相关科室
	 * @param param
	 * @param user
	 * @return{pkDept,nameDept}
	 */
	public List<Map<String,Object>> queryCnOrNsDept(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(null == paramMap) return null;
		if(CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkDept"))))
			throw new BusException("请传入临床科室主键pkDept！");
		if( CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkDept"))))
			throw new BusException("请传入待获取的科室类型dtDepttype！");
		List<Map<String,Object>> list = exPubMapper.queryCnOrNsDept(paramMap);
		return list;
	}
	 
	/**
	 * 根据医保计划，获取医保计划的附加属性
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryDictAttrByType(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = exPubMapper.queryDictAttrByType(paramMap);
		return list;
	}

	/**
	 * 根据医保计划，获取医保计划的附加属性
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryDictAttrByTypes(String param,IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
		if(mapParam==null)throw new BusException("未传入有效参数");
		List<Map<String,Object>> list = new ArrayList<>();
		if(mapParam.get("pkPds")==null) {
			throw new BusException("未传入要查询的医嘱主键");
		}else {
			List<String> pkPdList = (List<String>)mapParam.get("pkPds");
			if(null != pkPdList && !pkPdList.isEmpty()) {
				list = exPubMapper.queryDictAttrByTypes(mapParam);
			}
		} 
		return list;
	}
}
