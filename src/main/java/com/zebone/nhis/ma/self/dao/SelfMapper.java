package com.zebone.nhis.ma.self.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.ma.self.BdOuSssOper;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.ma.self.vo.CardAndPageInfo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SelfMapper {
	/**
	 * 获取机器功能信息
	 * 
	 * @param ipAddress
	 * @return
	 */
	List<BdOuSssOper> findMachinOperInfo(@Param("opers")String opers);
	List<PiMaster> getpiMasterByCredent(@Param("idType")String idType,@Param("idNo")String idNo,@Param("insurNo")String insurNo,@Param("mobile")String mobile,@Param("pkOrg")String pkOrg);
	/**
	 * 查询指定人员的活动id
	 * @param euTatype 活动对象类型
	 * @param pkTarget pk_pi，或者pk_emp
	 * @return
	 */
	List<String> getDpInvPkLstOnAppoint(@Param("euTatype")String euTatype,@Param("pkTarget")String pkTarget,@Param("pkOrg")String pkOrg,@Param("curDate")String curDate);
	List<String> getSqlSerDpInvPkLstOnAppoint(@Param("euTatype")String euTatype,@Param("pkTarget")String pkTarget,@Param("pkOrg")String pkOrg,@Param("curDate")String curDate);
	
	/**
	 * 住院费用查询
	 * @return
	 */
	List<BlIpDt> getBlIpDtsSqlSer(@Param("cardNo")String cardNo,@Param("dtCardtype")String dtCardtype,@Param("endDate")String endDate,@Param("beginDate")String beginDate,@Param("pkOrg")String pkOrg);
	List<BlIpDt> getBlIpDtsOracle(@Param("cardNo")String cardNo,@Param("dtCardtype")String dtCardtype,@Param("endDate")String endDate,@Param("beginDate")String beginDate,@Param("pkOrg")String pkOrg);
	
	
}
