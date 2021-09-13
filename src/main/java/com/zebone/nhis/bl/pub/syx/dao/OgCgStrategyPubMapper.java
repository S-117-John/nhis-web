package com.zebone.nhis.bl.pub.syx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.bl.pub.syx.vo.BlOpDtVo;
import com.zebone.nhis.bl.pub.syx.vo.HpVo;
import com.zebone.nhis.bl.pub.syx.vo.ItemCgNumVo;
import com.zebone.nhis.bl.pub.vo.GyHpDivInfo;
import com.zebone.nhis.bl.pub.vo.GyStItemVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.PvDiag;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OgCgStrategyPubMapper {
	
	/**获取患者主医保是否是广州公医*/
	public Map<String,Object> qryPvInsuAttrVal(Map<String,Object> paramMap);
	
	/**查询医保相关信息*/
    public HpVo queryHp(Map<String,Object> paramMap);
    
    /**查询患者医保就诊模式*/
    public String qryHpStatusByPv(Map<String,Object> paramMap);
    
    /**查询公医普通门诊待结算药费*/
    public Map<String,Object> qrygyGeneralOpDrugAmt(Map<String,Object> paramMap);
    
    /**查询患者不同自付比例的总数量*/
    public List<Map<String,Object>> qryRatioCountByPv(Map<String,Object> paramMap);
    
    /**获取患者当日诊次记录以及诊次药品费用*/
    public List<Map<String,Object>> qryTodayDrugAmtByPi(Map<String,Object> paramMap);
    
    /**查询医保计划扩展属性“0302”（广州公医普通门诊报销限制包含药费的诊次数）值*/
    public String qryOpVisitCount(Map<String,Object> paramMap);
    
    /**查询非药品待结算金额*/
    public Map<String,Object> qrygyGeneralOpItemAmt(Map<String,Object> paramMap);
    
    /**查询患者本次就诊待结算记费明细*/
    public List<BlOpDt> qryOpcgsByPv(Map<String,Object> paramMap);
    
    /**根据诊断查询是否属于慢病类型*/
    public Integer qryChronicTypeByDiag(Map<String,Object> paramMap);
    
    /**查询患者主诊断名称*/
    public List<PvDiag> qryNamediagByPv(Map<String,Object> paramMap);
    
    /**查询公医慢病门诊待结算金额*/
    public Map<String,Object> qrygyChronicOpAmt(Map<String,Object> paramMap);
    
    /**查询患者门诊特殊病信息*/
    public Map<String,Object> qrySpdisInfoByPv(Map<String,Object> paramMap);
    
    /**查询患者医保类型下的记费明细*/
    public List<BlOpDtVo> qryOpListByPv(Map<String,Object> paramMap);
    
    /**查询公医特病门诊待结算金额*/
    public Map<String,Object> qrySpdisOpAmt(Map<String,Object> paramMap);
    
    /**获取本月已结算信息*/
    public Map<String,Object> qrySpdisStOpAmt(Map<String,Object> paramMap);
    
    /**获取患者指定日期下的已记费数量*/
	public String qryOpCgNumByPv(@Param("pkPv")String pkPv,@Param("dateHap")String dateHap,@Param("pkItem")String pkItem);
	
	/**查询收费项目及其数量限制属性*/
	public List<ItemCgNumVo> qryItemCgNum(@Param("pkList") List<String> pkList);
	
	/**查询患者有无结算信息(过滤取消结算)*/
	public Integer qryStInfoByPv(Map<String,Object> paramMap);
	
	/**查询患者就诊诊查费集合*/
	public Map<String,Object> qryExamFeeInfo(Map<String,Object> paramMap);
	
	/**查询待结算诊查费主键*/
	public List<String> qryExamPkList(Map<String,Object> paramMap);
	
	/**查询公医非药品待结算信息*/
	public List<GyStItemVo> qryGyItemList(Map<String,Object> paramMap);
	
	/**查询材料费策略信息*/
	public List<GyHpDivInfo> qryMaterialsInfo(Map<String,Object> paramMap);
	
	/**查询医疗机构信息*/
	public Map<String,Object> qryMedicalInfo(Map<String,Object> paramMap);
	
	/**查询患者医疗证号*/
	public String qryMcnoByPv(Map<String,Object> paramMap);
	
	/**查询是否是定点医院*/
	public Integer checkDesHos(Map<String,Object> paramMap);
}
