package com.zebone.nhis.common.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.zebone.nhis.common.support.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.base.bd.code.BdEncoderule;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class ApplicationHandler {
	


	/**
	 * 根据编码规则生成编码 批量生产
	 * @param code
	 * @return 注编码生成的事务和业务处理中的事务相互独立。
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String[] operEncod(String code, int size) {

		String encoderuleSql = "select pk_encoderule,code,  length_code, flag_prefix, prefix, flag_date, format_date,flag_suffix, suffix,"
				+ " eu_snrule, val_init, val, ts from bd_encoderule where code = ?  and del_flag ='0' for update";

		if(Application.isSqlServer()){
			encoderuleSql = "select pk_encoderule,code,  length_code, flag_prefix, prefix, flag_date, format_date,flag_suffix, suffix,"
					+ " eu_snrule, val_init, val, ts from bd_encoderule with (rowlock,xlock) where code = ?  and del_flag ='0'";
			
		}
		
		BdEncoderule rule = DataBaseHelper.queryForBean(encoderuleSql, BdEncoderule.class, code);

		String[] bhs = new String[size];

		if (rule == null) {
			throw new BusException("编码生成失败：编码规则【" + code + "】没有维护！");
		}

		StringBuffer bh = new StringBuffer();
		Date nowDate = new Date();
		String suffix="";
		if (EnumerateParameter.ONE.equals(rule.getFlagPrefix()) && rule.getPrefix() != null) {
			bh.append(rule.getPrefix());
		}
		if (EnumerateParameter.ONE.equals(rule.getFlagSuffix()) && rule.getSuffix() != null) {
			suffix=rule.getSuffix();
		}
		if (EnumerateParameter.ONE.equals(rule.getFlagDate())) {
			try {
				SimpleDateFormat dateformat = new SimpleDateFormat(rule.getFormatDate());
				bh.append(dateformat.format(nowDate));
			} catch (Exception e) {
				throw new BusException("编码生成失败【" + code + "】：日期格式不正确！");
			}
		}

		long val = rule.getVal();
		if (val == 0) {
			val = rule.getValInit();
		} else {
			val = val + 1;
			if (EnumerateParameter.THREE.equals(rule.getEuSnrule())) { // 按年归零
				if (!DateUtils.isSameYear(rule.getTs(), nowDate)) {
					val = rule.getValInit();
				}
			} else if (EnumerateParameter.TWO.equals(rule.getEuSnrule())) { // 按年归零
				if (!DateUtils.isSameMonth(rule.getTs(), nowDate)) {
					val = rule.getValInit();
				}
			} else if (EnumerateParameter.ONE.equals(rule.getEuSnrule())) {// 按日归零
				if (!DateUtils.isSameDay(rule.getTs(), nowDate)) {
					val = rule.getValInit();
				}
			}
		}

		int bhLen = bh.length();
		int suffixLen=suffix.length();
		long valNew = val;
				
		for (int j = 0; j < size; j++) {
		    valNew = val + j;

			int addCodeSize = rule.getLengthCode() - bhLen - String.valueOf(valNew).length()-suffixLen;
			if (addCodeSize < 0) {
				throw new BusException("编码生成失败【" + code + "】：编码长度定义不正确！");
			}

			bhs[j] = bh.toString();

			for (int i = 0; i < addCodeSize; i++) {
				bhs[j] = bhs[j] + EnumerateParameter.ZERO;
			}

			bhs[j] = bhs[j] + valNew;
			
			bhs[j] = bhs[j] + suffix;
			
		}

		rule.setTs(nowDate);
		rule.setVal(valNew);

	    rule.setModifier(UserContext.getUser().getPkEmp());
		
		
		DataBaseHelper.updateBeanByPk(rule, false);

		return bhs;
	}
	
	
	/**
	 * 根据编码规则生成编码
	 * @param code
	 * @return 注编码生成的事务和业务处理中的事务相互独立。
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String operEncod(String code) {

		// 不用过滤当前机构
		String encoderuleSql = "select pk_encoderule,code,  length_code, flag_prefix, prefix, flag_date, format_date,flag_suffix, suffix,"
				+ " eu_snrule, val_init, val, ts from bd_encoderule where code = ?  and del_flag ='0' for update";
		
		if(Application.isSqlServer()){
			encoderuleSql = "select pk_encoderule,code,  length_code, flag_prefix, prefix, flag_date, format_date,flag_suffix, suffix,"
					+ " eu_snrule, val_init, val, ts from bd_encoderule with (rowlock,xlock) where code = ?  and del_flag ='0'";
			
		}

		BdEncoderule rule = DataBaseHelper.queryForBean(encoderuleSql, BdEncoderule.class, code);

		if (rule == null) {
			throw new BusException("编码生成失败：编码规则【" + code + "】没有维护！");
		}

		StringBuffer bh = new StringBuffer();
		Date nowDate = new Date();
		String suffix="";
		if (EnumerateParameter.ONE.equals(rule.getFlagPrefix()) && rule.getPrefix() != null) {
			bh.append(rule.getPrefix());
		}
		if (EnumerateParameter.ONE.equals(rule.getFlagSuffix()) && rule.getSuffix() != null) {
			suffix=rule.getSuffix();
		}

		if (EnumerateParameter.ONE.equals(rule.getFlagDate())) {
			try {
				SimpleDateFormat dateformat = new SimpleDateFormat(rule.getFormatDate());
				bh.append(dateformat.format(nowDate));
			} catch (Exception e) {
				throw new BusException("编码生成失败【" + code + "】：日期格式不正确！");
			}
		}

		long val = rule.getVal();
		if (val == 0) {
			val = rule.getValInit();
		} else {
			val = val + 1;
			if (EnumerateParameter.THREE.equals(rule.getEuSnrule())) { // 按年归零
				if (!DateUtils.isSameYear(rule.getTs(), nowDate)) {
					val = rule.getValInit();
				}
			} else if (EnumerateParameter.TWO.equals(rule.getEuSnrule())) { // 按年归零
				if (!DateUtils.isSameMonth(rule.getTs(), nowDate)) {
					val = rule.getValInit();
				}
			} else if (EnumerateParameter.ONE.equals(rule.getEuSnrule())) {// 按日归零
				if (!DateUtils.isSameDay(rule.getTs(), nowDate)) {
					val = rule.getValInit();
				}
			}
		}

		int bhLen = bh.length();
		int suffixLen=suffix.length();
		int addCodeSize = rule.getLengthCode() - bhLen - String.valueOf(val).length()-suffixLen;

		if (addCodeSize < 0) {
			throw new BusException("编码生成失败【" + code + "】：编码长度定义不正确！");
		}

		for (int i = 0; i < addCodeSize; i++) {
			bh.append("0");
		}

		bh.append(val);
		bh.append(suffix);
		rule.setTs(nowDate);
		rule.setVal(val);
		if(UserContext.getUser()!=null){
		rule.setModifier(UserContext.getUser().getPkEmp());
		}
		DataBaseHelper.updateBeanByPk(rule, false);

		return bh.toString();
	}

	/**
	 * 根据表名字段名获取序号
	 * @param nameFd
	 * @param nameTb
	 * @param count
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int operSerialno(String nameTb,String nameFd,int count) {
      if(CommonUtils.isEmptyString(nameTb)) {
		  throw new BusException("获取序号时name_tb字段不能为空！");
	  }
      if(CommonUtils.isEmptyString(nameFd)) {
		  throw new BusException("获取序号时name_fd字段不能为空！");
	  }
      nameFd = nameFd.toUpperCase();
      nameTb = nameTb.toUpperCase();
      int result = -1;
      //默认当前序号+1
      if(count==0)
      	count = 1;
      String updateSql = "update BD_SERIALNO set val = val + ? where name_tb = ? and name_fd = ?";
      int ret = DataBaseHelper.update(updateSql,count,nameTb,nameFd);
      //更新成功，取号
      if(ret==1){
      	String selectSql = "select val from BD_SERIALNO where name_tb = ? and name_fd = ? ";
      	result = DataBaseHelper.queryForScalar(selectSql,Integer.class,nameTb,nameFd) - count;
	  }
       return result;
	}


}
