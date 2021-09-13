package com.zebone.nhis.cn.opdw.dao;

import com.zebone.nhis.cn.opdw.vo.OpPatiInfo;
import com.zebone.nhis.cn.opdw.vo.SyxPiPv;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SyxCnOpPatiPvMapper {

	//获取当前医生的排班计算机
	public List<Map<String,Object>> qrySchPC(Map<String,Object> map) ;
	// 查询门诊当前医生的未接诊患者
	public List<Map<String,Object>> qryPiList(Map<String,Object> map);

	// 查询门诊当前医生的预约登记患者
	List<Map<String,Object>> qryPiSchList(Map<String,Object> map);

	// 查询门诊当前医生的已接诊患者
	public List<Map<String,Object>> qryPiPvList(Map<String,Object> map);
	// 查询急诊当前医生的未接诊患者
	public List<Map<String,Object>> qryErPiList(Map<String,Object> map);
	// 查询急诊当前医生的已接诊患者
	public List<Map<String,Object>> qryErPiPvList(Map<String,Object> map);
	//查询就诊记录的待遇类型
	public List<Map<String,Object>> qryPvMode(Map<String,Object> map);
	//查询预约患者信息
	public List<Map<String,Object>> qryApptPatis(Map<String,Object>map);
	//对应资源的排班是否还有可加号数,暂取原来的接口查询
	public List<SchSch>  qrySchCntAddByWorktime(Map<String,Object> param);
	//查询患者信息
	public List<Map<String,Object>> qryPatients(Map<String,Object> map);
	//查询入院通知单初始化信息，暂取原来的接口查询
	public OpPatiInfo qryPiOpInfo(Map<String,Object> map);
	//查询挂号信息
	public List<SyxPiPv> qryPiPvInfo(Map<String,Object>map);
	//查询医生当前的排班服务和排班资源
	public List<Map<String,Object>> qrySchEmpsrvtype(Map<String,Object>map);
	//查询指引单
	public List<Map<String,Object>> qryUnSettleCnOrd(Map<String,Object>map);
	//查询指引单
	public List<Map<String,Object>> queryPatiOrdAddInfo(Map<String,Object>map);
	//查询患者科室费用信息
	public Map<String, Object> queryPatiDeptChargeInfo(Map<String, Object> paramMap);
	public List<Map<String, Object>> qrySchEmpsrvtypeDateslot(Map<String, Object> paramMap);
	
	public List<Map<String, Object>> qryPvList(Map<String, Object> paramMap);
	//查询医生排班（含科室）
	public List<Map<String, Object>> qrySchDateslot(Map<String, Object> paramMap);
    /**  查询患者疾病史信息*/
    public  List<Map<String, Object>> queryPvEpidemicInf(String pkPv);
}
