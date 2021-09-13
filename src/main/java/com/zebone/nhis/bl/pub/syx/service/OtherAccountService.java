package com.zebone.nhis.bl.pub.syx.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.syx.dao.OtherAccountMapper;
import com.zebone.nhis.bl.pub.syx.vo.OtherChargeRecordVo;
import com.zebone.nhis.bl.pub.syx.vo.OtherReferencePdAndItemVo;
import com.zebone.nhis.bl.pub.syx.vo.OtherRelatedOrdersVo;
import com.zebone.nhis.bl.pub.syx.vo.OtherVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OtherAccountService {

	@Resource
	private OtherAccountMapper otherAccountMapper;
	@Autowired
	private OpCgPubService opCgPubService;
	/**
	 * 查询关联医嘱
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<OtherRelatedOrdersVo> qryRelatedOrdersList(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkpv");
		String pkdept = JsonUtil.getFieldValue(param, "pkdept");
		if (pkPv == null || "".equals(pkPv)) {
			return null;
		}
		if (pkdept == null || "".equals(pkdept)) {
			return null;
		}
		List<OtherRelatedOrdersVo> list = otherAccountMapper.qryRelatedOrdersList(pkPv, pkdept);
		return list;
	}

	/**
	 * 查询记费组套
	 */

	/**
	 * 获取药品和收费项目参照
	 */
	public List<OtherReferencePdAndItemVo> qryReferencePdAndItemList(String param, IUser user) {
		User u = (User) user;
		String pkhp = JsonUtil.getFieldValue(param, "pkhp");
		String pkOrg = u.getPkOrg();
		String keyword = JsonUtil.getFieldValue(param, "keyword");
		List<OtherReferencePdAndItemVo> list = otherAccountMapper.qryReferencePdAndItemList(pkhp, pkOrg, keyword);
		return list;
	}

	/**
	 * 查询收费记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<OtherChargeRecordVo> qryOtherChargeRecordList(String param, IUser user) {
		OtherVo otherVo = JsonUtil.readValue(param, OtherVo.class);

		List<OtherChargeRecordVo> list = otherAccountMapper.qryOtherChargeRecordList(otherVo);
		return list;
	}

	public List<OtherChargeRecordVo> qryOpOtherChargeRecordList(String param, IUser user) {
		OtherVo otherVo = JsonUtil.readValue(param, OtherVo.class);

		List<OtherChargeRecordVo> list = otherAccountMapper.qryOpOtherChargeRecordList(otherVo);
		return list;
	}

	/**
	 * 判断传入的项目是否被退费
	 * 
	 * @param param
	 * @param user
	 * @return
	 * @throws BusException
	 */
	public boolean isbeRefund(String param, IUser user) throws BusException {
		Integer codeCount = 0;
		String pkcgip = JsonUtil.getFieldValue(param, "pkcgip");
		if (pkcgip == null || "".equals(pkcgip)) {
			throw new BusException("传入参数有空值！");
		} else {
			codeCount = DataBaseHelper.queryForScalar("select count(1)  from bl_ip_dt  where pk_cgip_back=? ", Integer.class, pkcgip);
		}
		if (codeCount > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 交易号：007004002049 查询科室常用项目
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryComItemList(String param, IUser user){
		
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		List<Map<String,Object>> retList = new ArrayList<>();
		
		if(paramMap!=null && paramMap.size()>0 
				&& paramMap.containsKey("pkDept")){
			retList = otherAccountMapper.qryComItemList(paramMap);
		}
		
		return retList;
		
	}

	public void saveOpPatiCgInfo(String param, IUser user) {
		List<BlPubParamVo> vos = JsonUtil.readValue(param, new TypeReference<List<BlPubParamVo>>() {});
		BlPubReturnVo cgOpret= opCgPubService.blOpCgBatch(vos);
		ExtSystemProcessUtils.processExtMethod("CONSUMABLE", "saveOpConsumable", param,cgOpret);
	}
	
	public void deleteItem(String param, IUser user) {
		List<BlOpDt> dts = JsonUtil.readValue(param,
				new TypeReference<List<BlOpDt>>() {
				});

		Set<String> cgs = new HashSet<String>();// 费用明细主键集
		for (BlOpDt dt : dts) {
			if (dt.getPkCgop() != null)
			cgs.add(dt.getPkCgop());
		}

		if (cgs.size() > 0) {
			int num = DataBaseHelper.queryForScalar(
					"select count(1) from bl_op_dt where pk_cgop in ("
							+ CommonUtils.convertSetToSqlInPart(cgs, "pk_Cgop")
							+ ") and flag_settle='1'",
					Integer.class);
			if (num == 0) {
				DataBaseHelper.execute(
						"delete from bl_op_dt where pk_cgop in ("
								+ CommonUtils.convertSetToSqlInPart(cgs,
										"pk_Cgop")
								+ ")");
			} else {
				throw new BusException("项目已缴费，请刷新！");
			}

		}
	}
	
	/**
     * 查询毒麻药柜接口中间表待计费的数据
     * 交易号：007004002052
     *
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String, Object>> qryOtherOpChargeRecordList(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null)
		{
			throw new BusException("入参非法");
		}
		String pkPv = (String)paramMap.get("pkPv");
		if(pkPv == null)
		{
			throw new BusException("入参未包含就诊主键");
		}
		//查询此患者来自毒麻药柜的待计费的记录	
		String sql = "select pk_mid_bl_op,pk_pv,pk_cnord,code_item,name_item,quan,quan_unit,date_appt from MID_BL_OP where pk_pv = ? and FLAG_ITEM='1' and FEE_FLAG='0' and DEL_FLAG = '0'";
		List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sql, new Object[]{pkPv});		

		Set<String> setCodes = new HashSet<String>();
		if(mapList != null && mapList.size() > 0)
		{
			for (Map<String, Object> item : mapList) 
			{
				setCodes.add((String)item.get("codeItem"));
	        }
			//查询单位字典数据		
			String sqlBdUnit = "select pk_unit,name from bd_unit where del_flag='0'";
			List<Map<String, Object>> mapUnitList = DataBaseHelper.queryForList(sqlBdUnit, new Object[]{});
			if(mapUnitList == null || mapUnitList.size() == 0)
			{
				throw new BusException("字典表bd_unit未查询到数据");
			}
			//list转map,方便通过单位名称找到pk_unit
			Map<String, String> mapUnit = new HashMap<String, String>();
            for (Map<String, Object> itemMap : mapUnitList) {
            	String key = (String)itemMap.get("name");
            	if(key != null && !"".equals(key))
            	{
            		if(!mapUnit.containsKey(key))
            		{
            			mapUnit.put(key, (String)itemMap.get("pkUnit"));
            		}
            	}
            }
            if (setCodes.size() > 0) {
            	sql = "select pk_pd,pk_unit_min,pack_size,code from bd_pd where (code in (" + CommonUtils.convertSetToSqlInPart(setCodes, "code") +  ")) and del_flag='0' and flag_stop='0'";
            	List<Map<String, Object>> mapPdList = DataBaseHelper.queryForList(sql, new Object[]{});
    			//list转map,方便通过code找到相关的其它值
    			Map<String, Object> mapFeeItem = new HashMap<String, Object>();
                for (Map<String, Object> itemMap : mapPdList) {
                	String key = (String)itemMap.get("code");
                	if(key != null && !"".equals(key))
                	{
                		if(!mapFeeItem.containsKey(key))
                		{
                			mapFeeItem.put(key, itemMap);
                		}
                	}
                }
                //补充值
                for (Map<String, Object> item : mapList) 
    			{
    				if(mapFeeItem.containsKey((String)item.get("codeItem")))
    				{
    					Map<String, Object> feeMap = (Map<String, Object>)mapFeeItem.get((String)item.get("codeItem"));
    					item.put("pkItem", feeMap.get("pkPd"));
    					String pkUnit = "";
    					if(mapUnit.containsKey((String)item.get("quanUnit")))
    					{
    						pkUnit = mapUnit.get((String)item.get("quanUnit"));
    					}
    					String pkUnitMin = (String)feeMap.get("pkUnitMin");
    					if(pkUnit.equals(pkUnitMin))
    					{
    						//如果 pk_unit == pk_unit_min, 表示是最小单位,则数量维持不变
    						item.put("quanCg", item.get("quan"));
    					}
    					else
    					{
    						//否则，数量乘以pack_size 为换算后数量(最小单位的数量)
    						BigDecimal quan = (BigDecimal)item.get("quan");
    						BigDecimal packSize = (BigDecimal)feeMap.get("packSize");
    						BigDecimal quanCg = quan.multiply(packSize);
    						item.put("quanCg", quanCg);
    					}
    					item.put("pkUnit", pkUnit);
    				}
    	        }
            }
		}

		return mapList;
	}
}
