package com.zebone.nhis.pro.zsrm.bl.service;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.module.bl.BlDepoRemove;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pi.PiLockDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.pro.zsrm.bl.dao.ZsrmOpCgSettleMapper;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 中山市人民医院门诊结算客户化服务
 */
@Service
public class ZsrmOpCgSettleService {

    @Resource
    private ZsrmOpCgSettleMapper zsrmOpCgSettleMapper;
    
    /**
     * 交易码：
     * 解锁已完成缴费患者
     * @param param
     * @param user  操作用户
     */
    public void unlockPi(String param, IUser user){
    	Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPi = CommonUtils.getPropValueStr(paramMap, "pkPi");
        if(CommonUtils.isEmptyString(pkPi)) {
        	return;
        }
        User u = (User)user;
        int cnt = zsrmOpCgSettleMapper.getUnSettleRecordNum(pkPi);
        //不存在48小时内未缴费记录，则解锁
        if(cnt<=0){
            int count = DataBaseHelper.execute("delete from pi_lock where pk_pi = ? And eu_locktype = '3'",pkPi);
            //解锁成功后写入解锁明细
            if(count > 0) {
            	//写入解锁明细
                PiLockDt lockDt = new PiLockDt();
                lockDt.setDateLock(new Date());
                lockDt.setPkPi(pkPi);
                lockDt.setEuDirect("-1");
                lockDt.setEuLocktype("3");
                lockDt.setPkEmpOpera(u.getPkEmp());
                lockDt.setNameEmpOpera(u.getNameEmp());
                lockDt.setPkOrg(u.getPkOrg());
                DataBaseHelper.insertBean(lockDt);
            }   
        }

    }

    /**
     * 交易号：022006006004 
     * 查询患者的退费结算记录
     * @return
     */
    public List<Map<String, Object>> qryPatiRefSettle(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPi = CommonUtils.getPropValueStr(paramMap, "pkPi");
        if(CommonUtils.isEmptyString(pkPi)) {
        	return null;
        }
        return zsrmOpCgSettleMapper.qryPatiRefSettle(pkPi);
    }
    
    /**
     * 交易号：022006006005
     * 查询患者的退费结算明细
     * @return
     */
    public List<Map<String, Object>> qryPatiRefSettleDt(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkSettle = CommonUtils.getPropValueStr(paramMap, "pkSettle");
        if(CommonUtils.isEmptyString(pkSettle)) {
        	return null;
        }
        return zsrmOpCgSettleMapper.qryPatiRefSettleDt(pkSettle);
    }
    
    /**
	 * 交易号：022006006006
	 * 复制退费信息
	 * @param param
	 * @param user
	 */
	public void copyBlOpDtInfo(String param,IUser user){
		List<String> pkCgopList =JsonUtil.readValue(param, new TypeReference<List<String>>(){});
		if(pkCgopList == null || pkCgopList.size() < 1){
            return;
        }
		
		//1、根据记费主键查询记费信息
		List<BlOpDt> blOpDtList = zsrmOpCgSettleMapper.qryblOpDtList(pkCgopList);

		List<BlOpDt> blOpDtNews = new ArrayList<>();
		if(blOpDtList != null && blOpDtList.size() > 0) {
			//2、根据原始记费记录，生成新的记费记录
			for(BlOpDt oriOpDt : blOpDtList){
				BlOpDt blOpDtNew = new BlOpDt();
				ApplicationUtils.copyProperties(blOpDtNew, oriOpDt);
				blOpDtNew.setQuan(oriOpDt.getQuan());
				blOpDtNew.setAmountPi(Math.abs(oriOpDt.getAmountPi()));
				blOpDtNew.setAmount(Math.abs(oriOpDt.getAmount()));
				blOpDtNew.setAmountHppi(Math.abs(oriOpDt.getAmountHppi()));
				blOpDtNew.setFlagSettle(EnumerateParameter.ZERO);
				blOpDtNew.setPkSettle(null);
				blOpDtNew.setFlagRecharge("1");
				blOpDtNew.setPkInvoice(null);
				blOpDtNew.setPkCgopBack(null);
				blOpDtNew.setPkCgopOld(oriOpDt.getPkCgop());
				blOpDtNew.setDateCg(new Date());
				ApplicationUtils.setDefaultValue(blOpDtNew, true);
				blOpDtNews.add(blOpDtNew);
			}
		}
		
		//3、插入新的记费记录
		if(blOpDtNews!=null && blOpDtNews.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), blOpDtNews);
		}
	}
	
	/**
	 * 交易号：022006006016
	 * 查询交款记录
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> getPaymentRecords(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String  dateBegin = CommonUtils.getPropValueStr(paramMap, "dateBegin");
		if(CommonUtils.isNotNull(dateBegin)){
			paramMap.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		}
		String  dateEnd = CommonUtils.getPropValueStr(paramMap, "dateEnd");
		if(CommonUtils.isNotNull(dateEnd)){
			paramMap.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		}
		if(EnumerateParameter.NINE.equals(CommonUtils.getPropValueStr(paramMap, "euRemove"))) {
			paramMap.remove("euRemove");
		}
        return zsrmOpCgSettleMapper.getPaymentRecords(paramMap);
	}
	
	 /**
	 * 交易号：022006006017
	 * 保存挂账销账
	 * @param param
	 * @param user
	 */
	public void saveWriteOffAccount(String param,IUser user) {
		//解析入参  获取核销金额 付款方式  以及挂账信息
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String amount = CommonUtils.getPropValueStr(map, "amount");
		String Paymode = CommonUtils.getPropValueStr(map, "dtPaymode");
		Map<String, Object> paramMap = (Map<String, Object>) map.get("row");
		String pkDepo = CommonUtils.getPropValueStr(paramMap, "pkDepo");
		String pkEmpRemove = CommonUtils.getPropValueStr(paramMap, "pkEmpRemove");
		String dateRemove = CommonUtils.getPropValueStr(paramMap, "dateRemove");
		String noteRemove = CommonUtils.getPropValueStr(paramMap, "noteRemove");
		Date strToDate = DateUtils.strToDate(dateRemove);
		//如果入参没问题  进行数据校验  录入核销金额不得大于剩余未核销金额；
		if (StringUtils.isBlank(amount)) {
			throw new BusException("核销金额不可为空");
		}
		BigDecimal amountRemove = new BigDecimal(amount);
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("pkDepo", pkDepo);
		BigDecimal compareTheAmount = zsrmOpCgSettleMapper.compareTheAmount(params);
		if (compareTheAmount != null && amountRemove.compareTo(compareTheAmount) == 1) {
			throw new BusException("核销金额不得大于剩余未核销金额");
		}
		//拼装实体进行持久化
		BlDepoRemove blDepoRemove = new BlDepoRemove();
		blDepoRemove.setDateRemove(strToDate);
		blDepoRemove.setPkDepo(pkDepo);
		blDepoRemove.setEuDirect("1");
		blDepoRemove.setAmountRemove(amountRemove);
		blDepoRemove.setDtPaymode(Paymode);
		blDepoRemove.setPkEmpRemove(pkEmpRemove);
		blDepoRemove.setNoteRemove(noteRemove);
		ApplicationUtils.setDefaultValue(blDepoRemove, true);
		DataBaseHelper.insertBean(blDepoRemove);
		//持久化之后 当未核销金额为0时，更新核销完成标志；
		if (compareTheAmount != null && amountRemove.subtract(compareTheAmount).compareTo(new BigDecimal("0")) == 0) {
			String sql = "update bl_deposit  set eu_remove='1', pk_emp_remove=?,date_remove=?  where pk_depo=? ";
			DataBaseHelper.update(sql, new Object[]{pkEmpRemove, strToDate, pkDepo});
		} else {
			String sql = "update bl_deposit  set pk_emp_remove=?,date_remove=?  where pk_depo=? ";
			DataBaseHelper.update(sql, new Object[]{pkEmpRemove, strToDate, pkDepo});
		}
	}
	
	/**
	 * 交易号：022006006023
	 * 根据患者主键查询患者就诊信息
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryPatiPvInfo(String param, IUser user) {
		Map<String, Object> mapParam = JsonUtil.readValue(param, HashMap.class);
		mapParam.put("pkOrg", ((User) user).getPkOrg());
		StringBuffer sql = new StringBuffer();
		sql.append("select pv.pk_pv,pv.eu_pvtype,pv.pk_dept,pv.name_emp_tre,dept.name_dept,pv.date_reg ");
		sql.append(" from pv_encounter pv inner join bd_ou_dept dept on dept.pk_dept=pv.pk_dept ");
		sql.append(" where pv.pk_pi=? and pv.eu_status <> '9' and pv.pk_org=? and pv.eu_pvtype in ('1','2','4')  ");
		sql.append(" order by pv.date_reg desc");
		// 就诊记录列表
		List<Map<String, Object>> mapResult = DataBaseHelper.queryForList(sql.toString(), new Object[]{mapParam.get("pkPi"), mapParam.get("pkOrg")});
		return mapResult;
	}

	/**
	 * 根据pkDepo获取核销记录
	 */
	public List<Map<String, Object>> queryBlDepoRemove(String param, IUser user) {
		return zsrmOpCgSettleMapper.getBlDepoRemove(JSONObject.parseObject(param, Map.class));
	}
}
