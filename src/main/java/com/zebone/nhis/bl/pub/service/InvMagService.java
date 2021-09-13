package com.zebone.nhis.bl.pub.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InvMagService {
	
	
	/**
	 * 查询当前可用票据服务
	 * @param param
	 *            { "nameMachine":"机器名", "euType":"票据类型: 0 门诊发票，1 住院发票" }
	 * 
	 * @return 
	 * {
     * "invPrefix":"号码前缀",
     * "curNo":"当前票据号码",
     * "length":"票据号长度"(可为null),
     * "curCodeInv":"当前发票号值",
     * "cntUse":"剩余张数",
     * "pkEmpinv":"领用主键",
     * "pkInvcate":"票据分类"
     * }
	 */
	public Map<String, Object> searchCanUsedEmpInvoices(Map<String,Object> paramMap) {
       if(paramMap==null)
    	   throw new BusException("未获取到机器名及要使用的票据类型！");
       if(paramMap.get("nameMachine")==null)
    	   throw new BusException("未获取到机器名！");
       if(paramMap.get("euType")==null)
    	   throw new BusException("未获取到要使用的票据类型！");
		/** 获取前台所传参数 */
		String nameMachine = paramMap.get("nameMachine").toString();
		String euType = paramMap.get("euType").toString();

		/** 获取当前系统参数 */
		String valBL = null;
		if("0".equals(euType)){//门诊发票
			valBL = ApplicationUtils.getDeptSysparam("BL0036", UserContext.getUser().getPkDept());//使用科室级别系统参数
			if(valBL==null||"".equals(valBL)){
				throw new BusException("请维护当前科室门诊发票使用方式参数BL0036");
			}
		}else if("1".equals(euType)){
			valBL = ApplicationUtils.getDeptSysparam("BL0006", UserContext.getUser().getPkDept());//使用科室级别系统参数
			if(valBL==null||"".equals(valBL)){
				throw new BusException("请维护当前科室住院发票使用方式参数BL0006");
			}
		}else if("5".equals(euType)){
			valBL = ApplicationUtils.getDeptSysparam("BL0043", UserContext.getUser().getPkDept());//使用科室级别系统参数
			if(valBL==null||"".equals(valBL)){
				throw new BusException("请维护当前科室住院预交金使用方式参数BL0043");
			}
		}
		//String valBL = ApplicationUtils.getSysparam("BL0006", false);// 发票使用方式（0私用,1公用）
		//String valBL = ApplicationUtils.getDeptSysparam("BL0006", UserContext.getUser().getPkDept());//使用科室级别系统参数
		String pkOrg = UserContext.getUser().getPkOrg(); 
		String pkEmp = UserContext.getUser().getPkEmp();
		/** 获取当前可用票据 */
		Map<String, Object> queryForMap = getCanUsedEmpinv(euType, nameMachine, pkEmp, valBL, pkOrg);
		return queryForMap;
	}
	
	/**
	 * 获取当前可用票据
	 * @param euType
	 * @param nameMachine
	 * @param pkEmp
	 * @param valBL
	 * @param pkOrg
	 * @return
	 * {
     * "invPrefix":"号码前缀",
     * "curNo":"当前票据号码",
     * "length":"票据号长度"(可为null),
     * "curCodeInv":"当前发票号值",
     * "cntUse":"剩余张数",
     * "pkEmpinv":"领用主键",
     * "pkInvcate":"票据分类"
     * }
	 */
	private Map<String, Object> getCanUsedEmpinv(String euType, String nameMachine, String pkEmp, String valBL, String pkOrg) {

		String sql = "select inv.inv_prefix,inv.cur_no,cate.length,inv.cnt_use,inv.pk_empinv,inv.pk_invcate, cate.prefix from bl_emp_invoice inv "
				+ "inner join bd_invcate cate on inv.pk_invcate=cate.pk_invcate where inv.del_flag = '0' and inv.pk_org = ? "
				+ "and inv.flag_use ='1' and inv.flag_active ='1' and cate.eu_type = ? ";
		Map<String, Object> queryForMap = new HashMap<String, Object>();
		if ("0".equals(valBL)) {
			sql += "and inv.pk_emp_opera = ?";
			queryForMap = DataBaseHelper.queryForMap(sql, pkOrg, euType, pkEmp);
		} else if ("1".equals(valBL)) {
			sql += "and inv.name_machine = ?";
			//获取住院发票共享的计算机名称
			String newNameMachine = qryShareInvComName(nameMachine);
			//如果未获取到BL0037参数，以当前工作站名称为参数获取可用发票号
			nameMachine = !CommonUtils.isEmptyString(newNameMachine)?newNameMachine:nameMachine;
			List<Map<String,Object>> listMap = DataBaseHelper.queryForList(sql, pkOrg, euType, nameMachine.toUpperCase());
			//由于票据管理目前存在相同票据分类可能有2条及以上同时启用的问题，所以在获取发票时加入校验，如果存在此情况，提醒用户自行设置。
			if(listMap==null || listMap.size()<0)
				throw new BusException("已无可用票据！");
			else if(listMap.size()!=1)
				throw new BusException("存在启用多条票据分类相同的发票信息，请到【票据管理】进行重新设置！");
			queryForMap = listMap.get(0);
		}
		if(queryForMap!=null){
			String curNo = null;
			if(queryForMap.get("curNo") == null){
				throw new BusException("当前存在可用票据，但是当前号为空！");
			}else{
				curNo = queryForMap.get("curNo").toString();
			}
			//票据号=票据分类前缀+票据前缀+号段组成
			String prefix = queryForMap.get("prefix") == null?"":queryForMap.get("prefix").toString(); //票据分类前缀
			String invPrefix = queryForMap.get("invPrefix") == null?"":queryForMap.get("invPrefix").toString();
			if(queryForMap.get("length") != null){
				long length = Long.parseLong(queryForMap.get("length").toString());
				curNo = this.flushLeft("0", length, curNo);
				String curCodeInv = prefix + invPrefix + curNo;
				queryForMap.put("curCodeInv", curCodeInv);
			}else{
				String curCodeInv = prefix + invPrefix + curNo;
				queryForMap.put("curCodeInv", curCodeInv);
			}
		}else{
			throw new BusException("已无可用票据！");
		}
		return queryForMap;
	}
	
	/**
	 * 查询当前可用票据服务
	 * @param param
	 *            { "nameMachine":"机器名", "euType":"票据类型: 0 门诊发票，1 住院发票" }
	 * 
	 * @return 
	 * {
     * "invPrefix":"号码前缀",
     * "curNo":"当前票据号码",
     * "length":"票据号长度"(可为null),
     * "curCodeInv":"当前发票号值",
     * "cntUse":"剩余张数",
     * "pkEmpinv":"领用主键",
     * "pkInvcate":"票据分类"
     * }
	 */
	public Map<String, Object> searchCanUsedEmpInvoices(String param, IUser user) {

		/** 获取前台所传参数 */
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String nameMachine = "";
		if(paramMap.get("nameMachine")!=null)
			nameMachine = paramMap.get("nameMachine").toString();
		String euType = paramMap.get("euType").toString();

		/** 获取当前系统参数 */
		//String valBL = ApplicationUtils.getSysparam("BL0006", false);// 发票使用方式（0私用,1公用）
		String valBL = null;
		String pkOrg = ((User) user).getPkOrg();
		String pkEmp = ((User) user).getPkEmp();
		if("0".equals(euType)){//门诊发票
			valBL = ApplicationUtils.getDeptSysparam("BL0036", UserContext.getUser().getPkDept());//使用科室级别系统参数
			if(valBL==null||"".equals(valBL)){
				throw new BusException("请维护当前科室门诊发票使用方式参数BL0036");
			}
		}else if("1".equals(euType)){
			valBL = ApplicationUtils.getDeptSysparam("BL0006", UserContext.getUser().getPkDept());//使用科室级别系统参数
			if(valBL==null||"".equals(valBL)){
				throw new BusException("请维护当前科室住院发票使用方式参数BL0006");
			}
		}else if("5".equals(euType)){
			valBL = ApplicationUtils.getDeptSysparam("BL0043", UserContext.getUser().getPkDept());//使用科室级别系统参数
			if(valBL==null||"".equals(valBL)){
				throw new BusException("请维护当前科室住院预交金使用方式参数BL0043");
			}
		}
		
		/** 获取当前可用票据 */
		Map<String, Object> queryForMap = getCanUsedEmpinv(euType, nameMachine, pkEmp, valBL, pkOrg);
		return queryForMap;
	}
	
	/**
	 * 获取住院发票共享的计算机名称
	 * @param nameMachine
	 * @return
	 */
	private String qryShareInvComName(String nameMachine){
		String sql = "select argu.arguval from bd_res_pc pc "
				+" left join bd_res_pc_argu argu on pc.PK_PC = argu.pk_pc and argu.flag_stop = '0' and argu.DEL_FLAG = '0'"
				+" where pc.flag_active = '1' and pc.del_flag = '0' and pc.eu_addrtype = '0' and argu.code_argu = 'BL0037' and pc.addr = ?";
	
		Map<String,Object> qryMap = DataBaseHelper.queryForMap(sql, nameMachine);
		
		String rtnName = "";
		
		if(qryMap!=null && qryMap.size()>0)
			rtnName = CommonUtils.getString(qryMap.get("arguval"));
		
		return rtnName;
	}
	
	/**
	 * 左边补齐 
	 * c 要填充的字符    
     * length 填充后字符串的总长度    
     * content 要格式化的字符串   
     */  
	private String flushLeft(String c, long length, String content){             
		String str = "";     
		String cs = "";     
		if (content.length() > length){     
			throw new BusException("异常：当前号码长度与指定长度不一致！");     
		}else{    
			for (int i = 0; i < length - content.length(); i++){     
				cs = cs + c;     
			}  
		}  
		str = cs + content;      
		return str;      
	}    
	
}
