package com.zebone.nhis.base.bd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.OrdAutorunMapper;
import com.zebone.nhis.bl.pub.syx.service.IpCgPubSyxService;
import com.zebone.nhis.common.module.base.bd.code.BdAdminDivision;
import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivHvitem;
import com.zebone.nhis.common.module.base.bd.wf.BdOrdAutoexec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OrdAutorunService {
	
	@Autowired
	private OrdAutorunMapper ordAutorunMapper;
	
	@Autowired
	private IpCgPubSyxService ipCgPubSyxService;
	
	/**
	 * 交易号：001002006017
	 * 查询医嘱自动执行设置
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOrdAutoexec> qryOrdAutoexec(String param,IUser user){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		
		List<BdOrdAutoexec> ordList = ordAutorunMapper.qryOrdAutoexec(paramMap);
		
		return ordList;
	}
	
	/**
	 * 交易号：001002006018
	 * 删除医嘱自动执行
	 * @param param
	 * @param user
	 */
	public void delOrdAutoexec(String param,IUser user){
		String pkOrdautoexec = JsonUtil.getFieldValue(param, "pkOrdautoexec");
		DataBaseHelper.execute("delete from bd_ord_autoexec where pk_ordautoexec=?", pkOrdautoexec);
	}
	
	/**
	 * 交易号：001002006019
	 * 保存医嘱自动执行
	 * @param param
	 * @param user
	 */
	public void saveOrdAutoexec(String param,IUser user){
		List<BdOrdAutoexec> ordList = JsonUtil.readValue(param, new TypeReference<List<BdOrdAutoexec>>() {
		});
		
		/**新增或更新到数据库*/
		List<BdOrdAutoexec> insertList = new ArrayList<>();
		List<BdOrdAutoexec> updateList = new ArrayList<>();
		for(BdOrdAutoexec saveInfo : ordList){
			if(CommonUtils.isEmptyString(saveInfo.getPkOrdautoexec())){
				ApplicationUtils.setDefaultValue(saveInfo, true);
				insertList.add(saveInfo);
			}else{
				updateList.add(saveInfo);
			}
		}
		
		if(insertList!=null && insertList.size()>0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdOrdAutoexec.class), insertList);
		if(updateList!=null && updateList.size()>0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BdOrdAutoexec.class), updateList);
	}
	
	/**
	 * 交易号：001002006020
	 * 执行自动记费
	 * @param param
	 * @param user
	 */
	public void ordAutoexecCharge(String param,IUser user){
		//调用记费接口，执行自动记费
		ipCgPubSyxService.ordAutoexecCharge();
	}
}
