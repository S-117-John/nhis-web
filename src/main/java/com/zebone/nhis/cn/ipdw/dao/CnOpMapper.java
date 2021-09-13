package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.vo.OpPressVo;
import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpInfect;
import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.sch.appt.SchApptOp;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnOpMapper {
	/**
	 * 获取手术申请对应的医嘱
	 * @param pkCnord
	 * @return
	 */
    CnOrder selectOrder(String pkCnord);
    /**
     * 得到手术申请单界面需要初始化的字典
     * @return
     */
    List<BdDefdoc> getOpDictList();
    /**
     * 获取单个患者的手术申请列表
     * @param pkPv
     * @return
     */
    //List<CnOpApply> getOpApplyList(@Param("pkPv")String pkPv, @Param("pkOrg")String pkOrg);
    List<CnOpApply> getOpApplyList(Map<String,Object> paramMap);

    /**
     * 获取单个患者的手术申请列表 --复写
     * @param pkPv
     * @return
     */
    //List<CnOpApply> getOpApplyList(@Param("pkPv")String pkPv, @Param("pkOrg")String pkOrg);
    List<OpPressVo> getOpApplyListNew(Map<String,Object> paramMap);
    /**
     * 撤销手术申请
     * @param pkOpList
     * @param empSn
     * @param empName
     */
    void cancelOpApply(@Param("pkOpList") List<String> pkOpList, @Param("empSn")String empSn, @Param("empName")String empName);
    /**
     * 签署手术申请
     * @param pkOpList
     */
    void signOpApply(@Param("pkOpList")List<String> pkOpList,@Param("pkEmpOrd")String pkEmpOrd,@Param("nameEmpOrd")String nameEmpOrd);
    /**
     * 获取手术申请预约情况
     * @param codeApply
     * @return
     */
    List<SchApptOp> getOpApptList(String codeApply);
    
    /**
     * 删除申请对应的附加申请
     * @param applyNo
     */
    void removeChildApply(String pkOrdop);
    /**
     * 获取附加申请列表
     * @param pkOpList
     * @return
     */
    List<CnOpSubjoin> getChildApplyList(@Param("pkOpList") List<String> pkOpList);
    /**
     * 删除手术申请
     * @param pkOrdop
     */
    void removeOpApply(String pkOrdop);
    /**
     * 删除手术医嘱
     * @param pkCnord
     */
    void removeOpOrder(String pkCnord);
    
    /**
     * 科主任确认查询手术申请单
     */
    public List<Map<String,Object>> qryHeadSignApply(Map<String,Object> qryMap);
    
    /**
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> qryOpApply(Map<String,Object>map);
    
    /**
     * 查询医嘱信息（list）
     * @param pkOpList
     * @return
     */
    public List<CnOrder> qryOpOrderMsg (List<String> pkOpList);
    
    
    /**手术感染列表
     * @param pkOpList
     * @return
     */
    List<CnOpInfect> getInfectList(@Param("pkCnordList") List<String> pkCnordList);
    
    /**
     * 删除手术感染
     * @param pkCnord
     */
    void removeOpInfect(String pkCnord);
    
    /**
     * 查询感染类型
     * @param pkCnord
     * @return
     */
    List<Map<String,Object>> qryCnOpInfect(String pkCnord);
}