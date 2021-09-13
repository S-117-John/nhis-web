package com.zebone.nhis.base.bd.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.FeeControlSetMapper;
import com.zebone.nhis.common.module.base.bd.srv.BdBlCtrl;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

//记费控制设置
@Service
public class FeeControlSetService {
	
	@Autowired
	private FeeControlSetMapper feeControlSetMapper;

	//组查询：001002007087
	public List<Map<String, Object>> qryGroup(String param, IUser user){
		return feeControlSetMapper.qryGroup();
	}
	
	//明细查询：001002007088
	public List<Map<String, Object>> qryDetail(String param, IUser user){
		
		String sortNo = JsonUtil.getFieldValue(param, "sortNo");
		
		return feeControlSetMapper.qryDetail(sortNo);
	}
	
	//保存组：001002007089
	public void saveGroup(String param, IUser user){
		
		BdBlCtrl bdBlCtrl = JsonUtil.readValue(param, BdBlCtrl.class);
		Integer sortno = bdBlCtrl.getSortno();
		
		if(bdBlCtrl == null || sortno == null)throw new BusException("数据异常！");
		
		//新增时需要校验序号是否已存在
		if("1".equals(bdBlCtrl.getIsAdd())){
			String sql = "select count(1) from bd_bl_ctrl where sortno = ?";
			Integer count = DataBaseHelper.queryForScalar(sql, Integer.class, sortno);
			if(count > 0)throw new BusException("该序号已存在！");
			//记录类型0 父记录(添加组，1子记录添加明细
			bdBlCtrl.setFlagChd("0");
			DataBaseHelper.insertBean(bdBlCtrl);
		}else{
			DataBaseHelper.updateBeanByPk(bdBlCtrl,false);
		}
	}
	
	//保存组：001002007090
	public void saveDetail(String param, IUser user){
		
		List<BdBlCtrl> list = JsonUtil.readValue(param,new TypeReference<List<BdBlCtrl>>() {});
		
		if(list != null && list.size() > 0){
			for (BdBlCtrl bdBlCtrl : list) {
				if(StringUtils.isNotBlank(bdBlCtrl.getPkBlctrl())){
					DataBaseHelper.updateBeanByPk(bdBlCtrl,false);
				}else{
					DataBaseHelper.insertBean(bdBlCtrl);
				}
			}
		}
	}
	
	//删除组：001002007091
	public void delGroup(String param, IUser user){
		
		BdBlCtrl bdBlCtrl = JsonUtil.readValue(param, BdBlCtrl.class);
		
		String sql = "delete from BD_BL_CTRL where pk_blctrl = ? or sortno = ?";
		DataBaseHelper.execute(sql, bdBlCtrl.getPkBlctrl(),bdBlCtrl.getSortno());
	}
	
	//删除组：001002007092
	public void delDetail(String param, IUser user){
		
		List<BdBlCtrl> list = JsonUtil.readValue(param,new TypeReference<List<BdBlCtrl>>() {});
		
		if(list != null && list.size() > 0){
			for (BdBlCtrl bdBlCtrl : list) {
				String sql = "delete from BD_BL_CTRL where pk_blctrl = ?";
				DataBaseHelper.execute(sql, bdBlCtrl.getPkBlctrl());
			}
		}
	}
}
