package com.zebone.nhis.cn.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.pub.vo.BdDefdocVo;
import com.zebone.nhis.cn.pub.vo.CnOrderPrintVo;
import com.zebone.nhis.cn.pub.vo.PdStrockVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.PvDiagDt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnPubMapper {

	/**
	 * 获取手术申请对应的医嘱
	 * @param pkCnord
	 * @return
	 */
    CnOrder selectOrder(String pkCnord);
    
    /**
     * 删除申请对应的附加申请
     */
    void removeChildApply(String pkOrdop);
    
    /**
     * 查询参照医保目录
     */
    public List<Map<String,Object>> queryHpdicttype(Map<String,Object> paramMap);
    
    /**
     * 查询公医标示（记账部分内容）
     */
    public List<Map<String,Object>> queryGyType(Map<String,Object> paramMap);
    
    /**
     * 查询收费项目基本信息
     */
    public List<Map<String,Object>> queryItemBaseInfo(Map<String,Object> paramMap);
    
    /**
     * 查询频次执行时间
     * 
     */
    public List<Map<String,Object>> qryFreqTime(Map<String,Object> paramMap);
    
    /**
     * 判断两个科室是不是在同一个大专科
     * 
     */
    public int qryTheSameSpecialty(Map<String,Object> paramMap);
    
    /**
     * 获取编码表默认值的数据
     */
    public BdDefdocVo qryBdDefdocFlagDef(String codeDefdoclist);
    
    /**
     * 患者诊断转换
     * 
     */
    public List<Map<String,Object>> qryDiag(Map<String,Object> paramMap);

    /**
     * 查询医嘱项目信息
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> qryOrd(Map<String,Object> paramMap);

    //查询单个病人信息---修改病人信息时使用
    public List<Map<String,Object>> queryPati(Map<String,Object> map) ;

    /**
     * 查询已打印数据
     * @param param
     * @return
     */
    public List<Map<String,Object>> qryPrint(Map<String, Object> param);

    //查询业务下科室对应仓库
    public List<Map<String,Object>> qryStoreByDept(Map<String,Object> map);

    //查询对应医嘱执行单是否取消
    public List<Map<String,Object>> qryOrdOcc(Map<String,Object> map);
    
  //查询对应医嘱执行单是否已经取消
    public List<Map<String,Object>> qryOrdCan(Map<String,Object> map);

    //查询药品库存，批量
    public List<Map<String,Object>> qryPdQuanByStork(PdStrockVo map);

    //查询药品库存，批量
    public List<Map<String,Object>> qryPdStorkByDept(PdStrockVo map);

    //查询患者诊断
    public List<Map<String,Object>> qryPiDiag(Map<String, Object> param);
    //查询诊断明细
    public List<PvDiagDt> qryPvdiagDt(String pkPvdiag);

    //查询药品库存，单个
    public Map<String,Object> qryPdQuanByStorkOne(PdStrockVo map);

    List<Map<String, Object>> queryHpRateFormat(Map<String,Object> paramMap);

    /**
     * 查询收费项目基本信息
     */
    public List<Map<String,Object>> queryOrdBaseInfo(Map<String,Object> paramMap);

}
