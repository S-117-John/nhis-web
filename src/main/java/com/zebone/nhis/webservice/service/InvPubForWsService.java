package com.zebone.nhis.webservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 *  管理票据域webservcie专用公共服务
 *
 */
@Service
public class InvPubForWsService {
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
	public Map<String, Object> getCanUsedEmpinv(String euType, String nameMachine, String pkEmp, String valBL, String pkOrg) {

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
			
			queryForMap = DataBaseHelper.queryForMap(sql, pkOrg, euType, nameMachine);
		}else{//无参数控制的票据，共享私有均可
			sql += "and (inv.pk_emp_opera = ? or inv.name_machine = ?)";
			queryForMap = DataBaseHelper.queryForMap(sql, pkOrg, euType, pkEmp,nameMachine);
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
	 * 获取住院发票共享的计算机名称
	 * @param nameMachine
	 * @return
	 */
	private String qryShareInvComName(String nameMachine){
		String sql = "select argu.arguval from bd_res_pc pc"
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
	public String flushLeft(String c, long length, String content){             
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
	
	/**
	 * 灵璧自助机web接口
	 * 确认使用票据服务
	 * @param param
	 *            { "pkEmpinv":"领用主键", "cnt":"使用张数" }
	 */
	public void LbconfirmUseEmpInv(String pkEmpinv, Long cnt, User user) {

		String pkOrg = user.getPkOrg();

		Map<String, Object> empinv = DataBaseHelper.queryForMap("select cnt_use,pk_emp_opera from bl_emp_invoice where del_flag = '0' and pk_empinv = ?", pkEmpinv);
		Long cntUse = Long.parseLong(empinv.get("cntUse").toString());
		String pkEmpOpera = empinv.get("pkEmpOpera").toString();
		if (cntUse - cnt == 0) {
			DataBaseHelper.update("update bl_emp_invoice set cnt_use = cnt_use - ?,cur_no = cur_no + ?,flag_active = '0',flag_use = '0' where pk_empinv = ?",
					cnt, cnt, pkEmpinv);
			//启用下一条(按领用时间升序排序)未使用的票据记录，更新新纪录字段flag_use='1'
			List<Map<String, Object>> nextempinv = DataBaseHelper.queryForList("select * from bl_emp_invoice inv where inv.del_flag = '0' and inv.pk_org = ? and inv.pk_emp_opera = ? and inv.flag_active = '1' and inv.flag_use = '0' order by inv.date_opera",
					new Object[]{pkOrg, pkEmpOpera});
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
	
	public void LbUpdateBlDmpInvoice(long cntUse,long cnt,String pkOrg,String pkEmpinv,String pkEmpOpera){
		if (cntUse - cnt == 0) {
			DataBaseHelper.update("update bl_emp_invoice set cnt_use = cnt_use - ?,cur_no = cur_no + ?,flag_active = '0',flag_use = '0' where pk_empinv = ?",
					cnt, cnt, pkEmpinv);
			//启用下一条(按领用时间升序排序)未使用的票据记录，更新新纪录字段flag_use='1'
			List<Map<String, Object>> nextempinv = DataBaseHelper.queryForList("select * from bl_emp_invoice inv where inv.del_flag = '0' and inv.pk_org = ? and inv.pk_emp_opera = ? and inv.flag_active = '1' and inv.flag_use = '0' order by inv.date_opera",
					new Object[]{pkOrg, pkEmpOpera});
			if (nextempinv == null || nextempinv.size() == 0) {
				//如果不存在下一条，不处理
				//throw new BusException("没有可使用的票据了！");
			}else{				
				String pkinv = nextempinv.get(0).get("pkEmpinv").toString();
				DataBaseHelper.update("update bl_emp_invoice set flag_use = '1' where pk_empinv = ?", new Object[] { pkinv });
			}
		} else if (cntUse - cnt > 0) {
			DataBaseHelper.update("update bl_emp_invoice  set cnt_use = cnt_use - "+cnt+",cur_no = cur_no + "+cnt+" where pk_empinv = ?",new Object[] {pkEmpinv});
		}
	}
	
	/**
	 * 确认使用票据服务
	 * @param param
	 *            { "pkEmpinv":"领用主键", "cnt":"使用张数" }
	 */
	public void confirmUseEmpInv(String pkEmpinv, Long cnt) {

		String pkOrg = UserContext.getUser().getPkOrg();

		Map<String, Object> empinv = DataBaseHelper.queryForMap("select cnt_use,pk_emp_opera from bl_emp_invoice where del_flag = '0' and pk_empinv = ?", pkEmpinv);
		Long cntUse = Long.parseLong(empinv.get("cntUse").toString());
		String pkEmpOpera = empinv.get("pkEmpOpera").toString();
		if (cntUse - cnt == 0) {
			DataBaseHelper.update("update bl_emp_invoice set cnt_use = cnt_use - ?,cur_no = cur_no + ?,flag_active = '0',flag_use = '0' where pk_empinv = ?",
					cnt, cnt, pkEmpinv);
			//启用下一条(按领用时间升序排序)未使用的票据记录，更新新纪录字段flag_use='1'
			List<Map<String, Object>> nextempinv = DataBaseHelper.queryForList("select * from bl_emp_invoice inv where inv.del_flag = '0' and inv.pk_org = ? and inv.pk_emp_opera = ? and inv.flag_active = '1' and inv.flag_use = '0' order by inv.date_opera",
					new Object[]{pkOrg, pkEmpOpera});
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
		
}
