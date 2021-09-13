package com.zebone.nhis.ex.nis.ns.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.PvOperTrans;
import com.zebone.nhis.common.module.ex.nis.ns.PvOperTransItem;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.dao.PvOperTransMapper;
import com.zebone.nhis.ex.nis.ns.vo.OperTransVo;
import com.zebone.nhis.ex.nis.ns.vo.PvOperTransItemVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class PvOperTransService {
	
	@Resource
    private PvOperTransMapper pvOperTransMapper;
	
	/**
     * 根据手术单查询手术交接单
     * @param param(pkOrdop)
     * @param user
     * @return
     */
    public PvOperTrans queryOperTrans(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	if(CommonUtils.isNull(paramMap.get("pkOrdop"))){
    		throw new BusException("未获取到查询参数：手术单主键！");
    	}
    	if(CommonUtils.isNull(paramMap.get("pkOrg"))){
    		paramMap.put("pkOrg", ((User)user).getPkOrg());
    	}
    	return pvOperTransMapper.queryPvOperTrans(paramMap);
    }
	
	/**
     * 根据手术交接单 - 查询术前准备项目内容
     * @param param(pkOperTrans)
     * @param user
     * @return
     */
    public List<PvOperTransItemVo> queryTranItem(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	if(CommonUtils.isNull(paramMap.get("pkOpertrans"))){
    		throw new BusException("未获取到查询参数：手术交接单单主键！");
    	}
    	return pvOperTransMapper.queryPvOperTransItem(paramMap);
    }
    
    /**
	 * 保存手术交接单
	 * @param param(PvOperTransVo)
	 * @param user
	 */
	public void saveOperTrans(String param,IUser user){
		OperTransVo trans = JsonUtil.readValue(param, OperTransVo.class);
		if(trans == null){
			throw new BusException("未获取到要保存的内容！");
		}
		
		PvOperTrans operTrans = new PvOperTrans(); // 手术交接单
		ApplicationUtils.copyProperties(operTrans, trans);
		List<PvOperTransItem> list = trans.getListTransItem(); // 术前准备项目
		
		if (CommonUtils.isEmptyString(trans.getPkOpertrans())) {//新增
			
			//1、写入 pv_oper_trans 表，其中eu_status = '0'
			String pk_operTrans = NHISUUID.getKeyId();
			operTrans.setPkOrg(((User)user).getPkOrg());
			operTrans.setPkOpertrans(pk_operTrans);
			operTrans.setEuStatus("0");
			DataBaseHelper.insertBean(operTrans);
			
			// 2、写入pv_oper_trans_item 表，其中pk_opertrans = pk_operTrans(刚刚获取的)
			if(list != null && list.size() > 0){
				PvOperTransItem transItem = new PvOperTransItem();
				for(int i = 0 ; i < list.size() ; i++){
					transItem = list.get(i);
					transItem.setPkOpertrans(pk_operTrans);
					transItem.setPkTransitem(NHISUUID.getKeyId());
					transItem.setPkOrg(((User)user).getPkOrg());
				}
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvOperTransItem.class), list);
			}
		}else{//修改
			
			// 1、更新 pv_oper_trans 表
			DataBaseHelper.updateBeanByPk(operTrans,false);
			
			// 2、更新 pv_oper_trans_item 表 (如果为空则清空，否则保存新增、修改的，删除不存在的)
			if(list != null &&  list.size() > 0){
				List<PvOperTransItem> listAdd = new ArrayList<PvOperTransItem>();
				List<PvOperTransItem> listUpdate = new ArrayList<PvOperTransItem>();
				String pkTransItem = "" ; 
				String delStr = "";
				for(int i = 0 ; i < list.size() ; i++){
					if(CommonUtils.isEmptyString(list.get(i).getPkTransitem())){
						pkTransItem = NHISUUID.getKeyId();
						list.get(i).setPkOpertrans(operTrans.getPkOpertrans());
						list.get(i).setPkTransitem(pkTransItem);
						listAdd.add(list.get(i));
					}else{
						listUpdate.add(list.get(i));
					}
					delStr += "'" +list.get(i).getPkTransitem() +"',";
				}
				// 1)更新修改的 ; 2)插入新增的 ; 3)删除 删行的.
				if(listAdd.size() > 0){
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvOperTransItem.class), listAdd);
				}
				if(listUpdate.size() > 0){
					DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(PvOperTransItem.class), listUpdate);
				}
				String delSql = "delete from pv_oper_trans_item where pk_opertrans = ? and pk_transitem not in ("+delStr.subSequence(0, delStr.length() - 1)+") ";
				if(delStr.split(",").length > 0){
					DataBaseHelper.execute(delSql, new Object[] {operTrans.getPkOpertrans()});
				}
			}else{
				DataBaseHelper.execute(" delete from pv_oper_trans_item where pk_opertrans = ?", new Object[] {operTrans.getPkOpertrans()});
			}
		}
	}
	
	/**
	 * 删除手术交接单
	 * @param param(PvOperTransVo)
	 * @param user
	 */
	public void delOperTrans(String param,IUser user){
		OperTransVo trans = JsonUtil.readValue(param, OperTransVo.class);
		if(trans == null){
			throw new BusException("未获取到要删除的内容！");
		}
		// 1、删除手术交接单
		DataBaseHelper.execute("delete from pv_oper_trans where pk_opertrans = ? ", new Object[]{trans.getPkOpertrans()});
		
		// 2、删除术前准备项目
		DataBaseHelper.execute("delete from pv_oper_trans_item where pk_opertrans = ? ", new Object[]{trans.getPkOpertrans()});
	}
	
	/**
	 * 完成交接
	 * @param param
	 * @param user
	 */
	public void transComfirm(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkOpertrans = (String) paramMap.get("pkOpertrans");
    	if(CommonUtils.isNull(pkOpertrans)){
    		throw new BusException("未获取到手术交接单单主键！");
    	}
    	String UpdateOperTrans = "update pv_oper_trans set eu_status = '1' where pk_opertrans = '"+ pkOpertrans+"' ";
    	DataBaseHelper.execute(UpdateOperTrans);
	}
}
