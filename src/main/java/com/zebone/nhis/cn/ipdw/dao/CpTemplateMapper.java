package com.zebone.nhis.cn.ipdw.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.cn.cp.BdCpCateord;
import com.zebone.nhis.common.module.cn.cp.BdCpCateordDt;
import com.zebone.nhis.common.module.cn.cp.CpTemp;
import com.zebone.nhis.common.module.cn.cp.CpTempDept;
import com.zebone.nhis.common.module.cn.cp.CpTempDiag;
import com.zebone.nhis.common.module.cn.cp.CpTempOrd;
import com.zebone.nhis.common.module.cn.cp.CpTempPhase;
import com.zebone.nhis.common.module.cn.cp.CpTempReason;
import com.zebone.nhis.common.module.cn.cp.CpTempWork;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CpTemplateMapper {
	
	public List<CpTemp> getTemplateList(@Param("pkOrg")String pkOrg, @Param("pkDept")String pkDept, 
			@Param("pkDiag")String pkDiag, @Param("cpName")String cpName );
	
	/**
	 * 获取路径基本信息、阶段及阶段明细
	 * @param pkTemp
	 * @return
	 */
	public List<CpTemp> getTemplate(String pkTemp);
	public List<CpTempPhase> getTemplatePhases(String pkTemp);
	public List<CpTempOrd> getOrders(String pkTemp);
	public List<CpTempWork> getWorks(String pkTemp);
	
	public List<CpTempReason> getReasons(String pkTemp);
	public List<CpTempDept> getDepts(String pkTemp);
	public List<CpTempDiag> getDiags(String pkTemp);
	
	//public List<CpTempPhase> getRecPhases(String pkRec);
	public void removeTemplate(String pkTemp);
	public void auditTemplate(@Param("pkTemp")String pkTemp, @Param("euStatus")String euStatus,  
					@Param("empSn")String empSn, @Param("empName")String empName,@Param("dateChk")Date dateChk);
	public Double getMaxVersion(String cpName);
	
	//路径字典维护
	public List<BdCpCateord> queryCateOrdList(@Param("nameOrd") String nameOrd, @Param("typeOrd") String typeOrd);
	public BdCpCateord getCateord(String pkOrd);
	public List<BdCpCateordDt> getCateordDetails(@Param("pkOrd")String pkOrd, @Param("orderType")String orderType);
	
}
