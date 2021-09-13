package com.zebone.nhis.cn.ipdw.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnSpecialDrugApplyMapper;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdAntiApply;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnSpecialDrugApplyService {
	
	@Autowired
	private CnSpecialDrugApplyMapper cnSpecialDrugApplyMapper;
	
	//查询特殊抗菌药申请记录：004004012001
	public List<Map<String, Object>> querySpecialDrugApply(String param,IUser user){
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map.get("dateBegin") != null){
			String dateBegin = (String)map.get("dateBegin");
			map.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		}
		if(map.get("dateEnd") != null){
			String dateEnd = (String)map.get("dateEnd");
			map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		}
		
		return cnSpecialDrugApplyMapper.querySpecialDrugApply(map);
	}
	
	//修改特殊抗菌药申请：004004012002
	public void editSpecialDrugApply(String param,IUser user){
		
		CnOrdAntiApply apply = JsonUtil.readValue(param, CnOrdAntiApply.class);
		//正在使用的抗菌药变'无'时,名称清空
		if("0".equals(apply.getEuUseanti())){
			apply.setAnti(null);
		}
		String pkOrdantiapply = apply.getPkOrdantiapply();
		if(StringUtils.isNotBlank(pkOrdantiapply)){
			//真删除，后插入
			int delete = DataBaseHelper.deleteBeanByPk(apply);
			if(delete != 0){
				apply.setPkOrdantiapply(null);
				DataBaseHelper.insertBean(apply);
			}
		}
	}
	
	//更新打印信息
	public void updateSpecialDrugApplyPrint(String param,IUser user){
		
		String value = JsonUtil.getFieldValue(param, "pkOrdantiapply");
		
		if(StringUtils.isNotBlank(value)){
			String sql = "update cn_ord_anti_apply set flag_print='1',pk_emp_print=?,name_emp_print=?,"
					+ "date_print=? where pk_ordantiapply in (" + value + ")";
			int update = DataBaseHelper.update(sql, user.getId(),user.getUserName(),new Date());
			if(update == 0){
				throw new BusException("打印失败");
			}
		}
	}
}
