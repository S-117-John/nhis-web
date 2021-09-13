package com.zebone.nhis.common.service;

import com.zebone.nhis.common.module.base.bd.code.BdSysparam;
import com.zebone.nhis.common.module.base.bd.code.BdSysparamTemp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * His 基础业务公用服务
 * @author lingjun
 * 
 */
@Service
public class CommonService {

	private Logger logger = LoggerFactory.getLogger("com.zebone");

	/**
	 * 确认使用票据服务
	 * @param pkEmpinv
	 *            { "pkEmpinv":"领用主键", "cnt":"使用张数" }
	 */
	public void confirmUseEmpInv(String pkEmpinv, Long cnt) {

		String pkOrg = UserContext.getUser().getPkOrg();

		Map<String, Object> empinv = DataBaseHelper.queryForMap("select cnt_use,pk_emp_opera,pk_invcate from bl_emp_invoice where del_flag = '0' and pk_empinv = ?", pkEmpinv);
		Long cntUse = Long.parseLong(empinv.get("cntUse").toString());
		String pkEmpOpera = empinv.get("pkEmpOpera").toString();
		if (cntUse - cnt == 0) {
			DataBaseHelper.update("update bl_emp_invoice set cnt_use = cnt_use - ?,cur_no = cur_no + ?,flag_active = '0',flag_use = '0' where pk_empinv = ?",
					cnt, cnt, pkEmpinv);
			//启用下一条(按领用时间升序排序)未使用的票据记录，更新新纪录字段flag_use='1'
			List<Map<String, Object>> nextempinv = DataBaseHelper.queryForList("select * from bl_emp_invoice inv where inv.del_flag = '0' and inv.pk_org = ? and inv.pk_emp_opera = ? and pk_invcate = ? and inv.flag_active = '1' and inv.flag_use = '0' order by inv.date_opera",
					new Object[]{pkOrg, pkEmpOpera,empinv.get("pkInvcate").toString()});
			if (nextempinv == null || nextempinv.size() == 0) {
				//如果不存在下一条，不处理
				//throw new BusException("没有可使用的票据了！");
			}else{				
				String pkinv = nextempinv.get(0).get("pkEmpinv").toString();
				DataBaseHelper.update("update bl_emp_invoice set flag_use = '1' where pk_empinv = ?", new Object[] { pkinv });
			}
		} else if (cntUse - cnt > 0) {
			DataBaseHelper.update("update bl_emp_invoice  set cnt_use = cnt_use - ?,cur_no = cur_no + ? where pk_empinv = ?", cnt, cnt,
					pkEmpinv);
		} else {
			throw new BusException("更新后的可用张数为" + (cntUse - cnt) + "，小于0！");
		}
	}

	/**
	 * 业务线获取科室
	 * @param param
	 *            { "pkDept":"科室主键", "dtButype":"业务线类型编码","dtDepttype":"科室医疗类型"
	 *            }
	 * @return {"pkDept":"关联业务线科室"}
	 */
	public Map<String, Object> getLinesBusiness(String param, IUser user) {

		@SuppressWarnings("unchecked")
		Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
		return getLinesBusiness(mapParam);

	}

	/**
	 * 
	 * @param mapParam
	 *            { "pkDept":"科室主键", "dtButype":"业务线类型编码","dtDepttype":"科室医疗类型"
	 *            }
	 * @return {"pkDept":"关联业务线科室"}
	 */
	public Map<String, Object> getLinesBusiness(Map<String, Object> mapParam) {

		if (mapParam.get("pkDept") == null || mapParam.get("dtButype") == null || mapParam.get("dtDepttype") == null) {
			throw new BusException("参数不正确");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select busa.pk_dept,busa.pk_org,busa.time_begin,busa.time_end,busa.FLAG_DEF from bd_dept_bus bus inner join bd_dept_bu bu on bus.pk_deptbu=bu.pk_deptbu ") ;
		sql.append(" inner join bd_dept_bus busa on bus.pk_deptbu=busa.pk_deptbu  where busa.dt_depttype=? and bu.dt_butype= ? and  bus.pk_dept=? ");
		sql.append(" and busa.del_flag = '0' and bu.del_flag = '0' and bus.del_flag = '0' ");
		List<Map<String, Object>> resultList = DataBaseHelper.queryForList(sql.toString(),
				mapParam.get("dtDepttype"), mapParam.get("dtButype"), mapParam.get("pkDept"));
		if (resultList == null || resultList.size()==0) throw new BusException("此科室未在业务线维护其对应的科室");
		Map<String, Object> ret=new HashMap<String, Object>();
		boolean ifTrue=false;
		/** 获取药房增加执行时刻判断 */
		List<Map<String, Object>> res=new ArrayList<Map<String, Object>>();
		Date nowTime=new Date();
		for (Map<String, Object> result: resultList) {
			Date timeBegin = DateUtils.getDateMorning(nowTime,0);
			Date timeEnd = DateUtils.getDateMorning(nowTime,1);
			if(result.get("timeBegin")!=null ){
				timeBegin = DateUtils.strToDate(DateUtils.getDate() +" "+ result.get("timeBegin").toString(),"yyyy-MM-dd HH:mm:ss");
			}
			if(result.get("timeEnd")!=null){
				timeEnd = DateUtils.strToDate(DateUtils.getDate() +" "+ result.get("timeEnd").toString(),"yyyy-MM-dd HH:mm:ss") ;
			}
			if(DateUtils.getSecondBetween(timeEnd,timeBegin)>0 ){ //开始时间大于结束时间
				if(DateUtils.getHour(nowTime)>DateUtils.getHour(timeEnd))
					timeEnd=DateUtils.getSpecifiedDay(timeEnd,1);
				else
					timeBegin = DateUtils.getSpecifiedDay(timeBegin, -1);
			}

			if(DateUtils.getSecondBetween(timeBegin,nowTime)>=0 && DateUtils.getSecondBetween(nowTime,timeEnd)>=0){
				ret=result;
				res.add(result);
				if(result.get("flagDef")!=null && "1".equals(result.get("flagDef").toString())){
					ifTrue=true;
					break;
				}

			}
		}

		if(!ifTrue){
			if(res.size()==0) throw new BusException("此科室未在业务线维护当前时间其对应的科室");
			ret=res.get(0);
		}
		return ret;
	}
	
	/***
	 * 根据编码规则获取系统编码（流水号）
	 * 
	 * @param  {"code":"系统编码"}
	 * @return Map<String,String>
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2017年4月13日
	 */
	public Map<String, String> getBdEncode(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
		String val = ApplicationUtils.getCode(mapParam.get("code").toString());
		if(mapParam.get("code").toString().equals("0203")){
			logger.info("==============CommonService.getBdEncode获取住院号:"+val+"============");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("val", val);
		return map;
	}
	
	/**
	 * 获取计算机系统参数
	 * @param param
	 * @param user
	 * @return
	 * @author yuanxinan 
	 */
	public Map<String, Object> getSysParam(String param, IUser user) {
		Map<String, Object> qryMap = JsonUtil.readValue(param, Map.class);
		Map<String, Object> map = new HashMap<String, Object>();
		String pkOrg = ((User) user).getPkOrg();
		if(null != qryMap && null != qryMap.get("pkOrg") && !CommonUtils.isEmptyString(qryMap.get("pkOrg").toString()))
			pkOrg = qryMap.get("pkOrg").toString();
		List<BdSysparam> parameters=DataBaseHelper.queryForList("select * from bd_sysparam where del_flag = '0' and  (pk_org='~                               ' or pk_org= ?)",BdSysparam.class, new Object[] {pkOrg });
		List<Map<String, Object>> pcArgus=DataBaseHelper.queryForList("SELECT pc.addr, pc.eu_addrtype, argu.* FROM bd_res_pc_argu argu left JOIN bd_res_pc pc on  pc.pk_pc = argu.pk_pc WHERE argu.del_flag = '0' AND (argu.pk_org = '~                               ' or argu.pk_org= ?)", new Object[] {pkOrg });
		List<BdSysparamTemp> templates=DataBaseHelper.queryForList("select * from bd_sysparam_temp",BdSysparamTemp.class);
		map.put("parameters", parameters);
		map.put("templates", templates);
		map.put("pcArgus", pcArgus);
		return map;
	}
	
	/**
	 * 获取NHIS系统配置文件属性值
	 * @param param
	 * @param user
	 * @return
	 */
	public String getSysPropValue(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map.get("propName")==null) return "";
		String propName = map.get("propName").toString();
		String defaultVale="";
		if(map.get("defaultVale")!=null)  defaultVale=map.get("defaultVale").toString();
		
		String propValue=ApplicationUtils.getPropertyValue(propName, defaultVale);
		
		return propValue;
	}
	/**
	 * 根据指定字典编码获取对应主键
	 * @param param
	 * @param user
	 * @return
	 */
	//public String getDictPkByCode(String pkOrg,String tableName,String pkColName,String codeColName,String code,String delFlag){
	public String getDictPkByCode(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String pkOrg = map.get("pkOrg").toString();
		String tableName = map.get("tableName").toString();
		String pkColName = map.get("pkColName").toString();
		String codeColName = map.get("codeColName").toString();
		String code = map.get("code").toString();
		String delFlag = map.get("delFlag").toString();
		
		if(CommonUtils.isEmptyString(pkOrg))
			throw new BusException("未传入机构主键pkOrg");
		if(CommonUtils.isEmptyString(tableName))
			throw new BusException("未传入需要获取的字典表名tableName");
		if(CommonUtils.isEmptyString(pkColName))
			throw new BusException("未传入需要转换的主键列名pkColName");
		if(CommonUtils.isEmptyString(codeColName))
			throw new BusException("未传入需要转换的编码列名codeColName");
		if(CommonUtils.isEmptyString(code))
			throw new BusException("未传入需要转换的编码值code");
		//不传入delFlag 默认查询未删除的字典内容
		if(CommonUtils.isEmptyString(delFlag))
			delFlag = "0";
		StringBuilder sql = new StringBuilder("select ").append(pkColName.toLowerCase()).append(" from ").append(tableName)
				.append(" where ").append(codeColName).append("= :fValue and del_flag = :delFlag and ltrim(rtrim(PK_ORG)) = :pkOrg");
		//未防止存在垃圾数据查询产生重复记录，使用List返回
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("fValue", code);
		paramMap.put("delFlag", delFlag);
		paramMap.put("pkOrg", pkOrg.replaceAll("\\s*", ""));
		List<Map<String,Object>> result = DataBaseHelper.queryForList(sql.toString(), paramMap);
		if(result == null||result.size()<=0){
			return null;
		}else{
			pkColName = UnderlineToHump(pkColName);
			return 	result.get(0).get(pkColName) == null?null:result.get(0).get(pkColName).toString();	
		}
	}
	
	/***
	 * 下划线命名转为驼峰命名
	 * 
	 * @param para
	 *        下划线命名的字符串
	 */
    public String UnderlineToHump(String para){
        StringBuilder result=new StringBuilder();
        String a[]=para.split("_");
        for(String s:a){
            if(result.length()==0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

	/**
	 * 获取格式化年龄的服务
	 * @param param
	 * @param user
	 * @return
	 */
	public String getFormatAge(String param, IUser user) {
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		if(MapUtils.isEmpty(paraMap)){
			return null;
		}
		return ApplicationUtils.getAgeFormat(MapUtils.getString(paraMap,"birthDate"),
				MapUtils.getString(paraMap,"dateBegin"));
	}
}
