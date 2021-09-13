package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zebone.nhis.cn.opdw.vo.ApptPatient;
import com.zebone.nhis.cn.opdw.vo.OpPatiInfo;
import com.zebone.nhis.cn.opdw.vo.OpPatiOvervieVo;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 门诊医生站--患者信息总览(持久层)
 * @author Roger
 *
 */
@Mapper
public interface CnOpPatiOverviewMapper {
	
	@Select("SELECT * FROM pv_op WHERE pk_pv =  #{pkPv}")
	PvOp qryPvOpByPkPv (@Param("pkPv") String pkPv);
	
	@Select("SELECT res.pk_schres, res.code,res.name,res.eu_restype FROM sch_resource res WHERE res.flag_stop = 0 and ((res.eu_restype=1 AND res.pk_emp= #{pkEmp}  and res.pk_dept_belong=#{pkDept}) or (res.eu_restype=0 and res.pk_dept=#{pkDept}))")
	List<SchResource> qrySchResByCon(@Param("pkEmp") String pkEmp,@Param("pkDept") String pkDept); 
	
//	@Select("SELECT emr.pk_pv, emr.problem, emr.present,emr.history, emr.allergy,emr.height,emr.weight,emr.temperature,emr.sbp,emr.dbp,emr.exam_phy,emr.exam_aux,emr.note FROM cn_emr_op emr WHERE emr.pk_pv = #{pkPv}")
//	OpPatiOvervieVo qryEmrDataByPkpv(@Param("pkPv") String pkPv);
	
	@Select("SELECT emr.pk_pv, emr.problem, emr.present,emr.history, emr.allergy,emr.height,emr.weight,emr.temperature,emr.sbp,emr.dbp,emr.exam_phy,emr.exam_aux,emr.note FROM cn_emr_op emr WHERE emr.pk_pv = #{pkPv}")
	List<OpPatiOvervieVo> qryEmrDataByPkpv(@Param("pkPv") String pkPv);
	
	@Select("SELECT lab.date_rpt,lab.name_index,lab.val,lab.unit,lab.eu_result FROM ex_lab_occ lab WHERE lab.eu_result <> 0 AND lab.pk_pv=#{pkPv}")
	List<ExLabOcc> qryLabDataByPkpv(@Param("pkPv") String pkPv);

	@Select("SELECT occ.date_rpt,ord.name_ord,occ.eu_result FROM ex_ris_occ occ  INNER JOIN cn_order ord ON occ.pk_cnord=ord.pk_cnord WHERE occ.pk_pv=#{pkPv}")
	List<ExRisOcc> qryRisDataByPkpv(@Param("pkPv") String pkPv);
	
	@Select("SELECT * FROM cn_order ord WHERE ord.pk_pv = #{pkPv} order by ord.date_start desc")
	List<CnOrder> qryOrdDataByPkpv(@Param("pkPv") String pkPv);
	
    @Select("SELECT * FROM pv_diag WHERE pk_pv = #{pkPv} ORDER BY sort_no " )
	List<PvDiag> qryPvDiagByPkPv(@Param("pkPv") String pkPv);
    
    @Select("SELECT * FROM cn_order ord WHERE ord.flag_erase=0 and (ord.code_ordtype like '02%' or ord.code_ordtype like '03%') and ord.pk_pv = #{pkPv} order by code_apply")
	List<CnOrder> qryRptDataByPkpv(String pkPv);

    @Select("SELECT * FROM bd_code_dateslot WHERE dt_dateslottype = '01' ")
    List<BdCodeDateslot> getCurDateSlot();
    
	List<PiMaster> getPiMasterList(PiMaster master);
	
	List<SchSch>  getAddSch(Map<String,Object> param);
	
	List<SchSch>  getAddSchByDateTime(Map<String,Object> param);

	List<ApptPatient> getApptPatientList(Map<String, Object> para);

	//入院申请单
	OpPatiInfo qryPatiOpInfo(@Param("pkPv") String pkPv);

	//住院诊断编码字典中山
	List<Map<String, Object>> qryCnDaigByPk(Map<String, String> paramMap);

	/**
	 * 查询患者就诊记录
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qryVisitingInfo(Map<String, String> paramMap);

	/**
	 * 查询门诊就诊费用明细
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qryVisitingCharges(Map<String, String> paramMap);

	/**
	 * 查询门诊就诊诊断记录
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qryVisitingDiag(Map<String, String> paramMap);
    
}