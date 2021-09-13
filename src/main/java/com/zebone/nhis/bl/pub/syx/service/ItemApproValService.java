package com.zebone.nhis.bl.pub.syx.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.syx.dao.ItemApproValMapper;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyChk;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 公医限额项目审批服务
 * @author c
 *
 */
@Service
public class ItemApproValService {
	@Resource
	private ItemApproValMapper itemApproValMapper;
	@Resource
	private IpCgPubSyxService ipCgPubSyxService;
	
	/**
	 * 交易号：007003003023
	 * 查询审批信息数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsGzgyChk> qryApproValList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		return itemApproValMapper.qryApproValList(paramMap);
	}
	
	/**
	 * 交易号：007003003024
	 * 更新审批信息数据
	 * @param param
	 * @param user
	 */
	public void updateApproVal(String param,IUser user){
		InsGzgyChk gzgyChk = JsonUtil.readValue(param, InsGzgyChk.class);
		
		/**更新审批信息数据*/
		itemApproValMapper.updateApproVal(gzgyChk);
		
		//审批通过时更新计费表信息
		if(gzgyChk.getEuResult().equals("0")){
			/**根据eu_pvtype更新bl_op_dt表\bl_ip_dt表*/
			Integer euPvtype = Integer.valueOf(gzgyChk.getEuPvtype());
			if(euPvtype<3){			//更新bl_op_dt表
				
			} else if(euPvtype==3){	//更新bl_ip_dt表
				ipCgPubSyxService.itemApValCg(gzgyChk);
			}
		}
		
	}
	
	/**
	 * 交易号：007003003025
	 * 取消审批
	 * @param param
	 * @param user
	 */
	public void cancelApproVal(String param,IUser user){
		InsGzgyChk gzgyChk = JsonUtil.readValue(param, InsGzgyChk.class);
		
		/**取消审批*/
		String sql = "update ins_gzgy_chk set flag_chk='0',eu_result=null,date_chk=null,pk_emp_chk=null,name_emp_chk=null where pk_gzgychk=? and flag_chk='1'";
		DataBaseHelper.execute(sql, gzgyChk.getPkGzgychk());
		
		/**根据eu_pvtype更新bl_op_dt表\bl_ip_dt表*/
		Integer euPvtype = Integer.valueOf(gzgyChk.getEuPvtype());
		
		/**取消审批时只有审批结果为通过更新计费信息，未通过不更新计费信息*/
		if(gzgyChk.getEuResult().equals("0")){
			if(euPvtype<3){			//更新bl_op_dt表
				
			} else if(euPvtype==3){	//更新bl_ip_dt表
				ipCgPubSyxService.cancelItemApValCg(gzgyChk);
			}
		}
	}
	
}
