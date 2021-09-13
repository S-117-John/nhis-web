package com.zebone.nhis.cn.ipdw.dao;

import com.zebone.nhis.cn.ipdw.vo.CnOrderInputVO;
import com.zebone.nhis.cn.ipdw.vo.EmpVo;
import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.cn.opdw.CnOpEmrRecord;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnOrderMapper {
  //查询医嘱录入字典
	public List<Map<String,Object>> qryCnOrderRef(Map<String,Object> map) ;
  //查询医嘱
	public List<Map<String,Object>> qryCnOrder(Map<String,Object> map);
  //查询排斥医嘱
	public List<Map<String,Object>> qryOrdExcludeDt(Map<String, Object> map);
  //查询个人模板
	public List<Map<String,Object>> qryOrdSet(Map<String, Object> map);
  //查询个人模板明细
	public List<Map<String,Object>> qryOrdSetDt(Map<String,Object> map);
  //查询当前诊断
	public List<Map<String,Object>> qryPvDiag(String pk_pv);
	//查询历史诊断
	public List<Map<String,Object>> qryCnDiag(String pk_pv);
	//查询IV类医嘱
	public List<Map<String,Object>> qrySortIv(String pkPv);
   //查询带有患者信息的医嘱
   public List<Map<String,Object>> qryCnPatiOrder(String pkPv);
   //查询临床路径表单
   public List<Map<String,Object>> qryCpFormData(@Param("pkCprec")String pkCprec,@Param("pkCptemp")String pkCptemp);
   //查询临床路径表单头部信息
   public List<Map<String,Object>> qryCpFormTitleData(@Param("pkCprec")String pkCprec);
   //查询药品主键
   public BdPd getDrugCode(Map<String, String> params);
   //查询住院次数
   public List<PvIp> getIpTimes(String pkPv);
   //查询就诊次数
   public List<PvOp> getOpTimes(String pkPv);
   //查询患者信息
   public List<PiMaster> getMaBirth(String pkPi);
   //查询就诊信息
   public List<PvEncounter> getEncounters(String pkPv);
   //查询过敏信息
   public List<PiAllergic> getAllergics(String pkPi);
   //查询诊断信息
   public List<BdTermDiag> getDiags(String pkPv);
   //查询单位名称
   public List<BdUnit> getBdUnits(String pkUnit);
   //查询给药途径名称
   public List<BdSupply> getBdSupplies(String code);
   //查询科室名称
   public BdOuDept getBdOuDept(Map<String, String> params);
   //查询医生信息
   public List<EmpVo> getEmpInfo(String pkEmp);
   //根据当前医生，查询科室主任信息
   public List<EmpVo> getDocterInfo(Map<String, String> params);
   //查询检查信息
   public List<CnRisApply> getRisInfo(String pkPv);
   //查询检验信息
   public List<CnLabApply> getLabInfo(String pkPv);
   //查询医嘱信息
   public List<CnOrder> getOrdInfo(Map<String, String> params);
   //查询体征信息
   public List<CnOpEmrRecord> getBodyInfo(Map<String, String> params);
   //查询标准诊断编码信息
   //public List<BdTermDiag> getDiagICDTenInfo(Map<String, String> params);
   public List<Map<String, Object>> getDiagICDTenInfo(Map<String, String> params);
   //长期医嘱录入，医嘱单位为日，不允许一天1次以上频率，次用量也不允许大于1——总量不允许超过1
   public List<Map<String,Object>> checkFreqAndQuan(List<Integer> pkCnords);
   //查询患者适应症信息
   public List<Map<String,Object>> qryDescFit(Map<String,Object>map);
   //查询患者适应症信息--限定支付范围信息里只显示适应症目录里有的医嘱项目
   public List<Map<String,Object>> qryDescFitOlny(Map<String,Object>map);
   //查询医嘱及其抗菌药监测信息
   public List<Map<String, Object>> qryOrdAnti(Map<String, Object> params);
   //查询皮试药品的皮试剂
   public List<Map<String, Object>> qryOrdStReagent(Map<String, Object> params);
   //查询医嘱录入字典
   public List<Map<String,Object>> qryPdNoStore(Map<String,Object> paramMap) ;
   //查询用药信息
   public List<Map<String, Object>> qryDrugInfo(Map<String, Object> paramMap);
   //遍历查询医嘱信息
   public List<CnOrder> qryOrderMsg(List<CnOrder> pkCnords);
   //查询药品关联的溶媒
   public List<Map<String, Object>> qryPdMens(Map<String, Object> params);   

   //查询药品关联业务线的库存及是否停用等信息
   public List<Map<String, Object>> qryOrdInfoFromPd(Map<String, Object> params);

   List<Map<String,Object>> findOrdList(Map<String, Object> params);

   //医嘱录入字典查询--基准版本
   List<CnOrderInputVO> findOrdBaseList(Map<String, Object> params);
 /**
  * 获取非药品信息
  * @param params
  * @return
  */
 List<CnOrderInputVO> findUnPdList(Map<String, Object> params);

 List<CnOrderInputVO> findPdList(Map<String, Object> params);

 List<Double> findQuanMin(Map<String, Object> params);

 List<String> getFlagMens(String pkPd);

 /**
  * 是否是辅助用药
  * @return
  */
 List<String> findBdPdAtt(Map<String, Object> params);

 /**
  * 是否是辅助用药
  * @return
  */
 List<Map<String,Object>> findBdPdAttByPk(Map<String, Object> params);

 /**
  *科研标志
  * @param pkOrd
  * @return
  */
 List<String> getFlagKy(String pkOrd);

 /**
  * 是否是辅助用药
  * @return
  */
 List<Map<String, Object>> qryPdLimit(Map<String, Object> params);

    List<Map<String, Object>>  qryOrdInfo(List<String> pkOrds);
}
