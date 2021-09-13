package com.zebone.nhis.pro.zsba.ex.dao;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.base.bd.wf.BdOrdAutoexec;
import com.zebone.nhis.pro.zsba.ex.vo.ExAssistOccPar;
import com.zebone.nhis.pro.zsba.ex.vo.MedAppBaVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MedApplyBaMapper {
	
	/**
	 * 查询申请单列表
	 * @param map{pkPvs,apptype,codeApply}
	 * @return
	 */
	public List<MedAppBaVo> queryBaAppList(Map<String,Object> map);
	
	/**
	 * 查询产房的医技申请列表
	 */
	public List<MedAppBaVo> queryBaLabAppList(Map<String, Object> map);

	/**
	 * 查询申请单已执行列表
	 * @param map
	 * @return
	 */
	public String queryBaAppExList(Map<String,Object> map);
	
	/**
	 * 查询自动执行设置内容
	 * @param paramMap{pkOrg:机构主键}
	 * @return
	 */
   public List<BdOrdAutoexec> queryAutoExec(Map<String,Object> paramMap);

	/**
	 * 查询收费项目
	 * @param exAssistOccPars
	 * @return
	 */
    List<BlPubParamVo> getApplyBlItem(List<ExAssistOccPar> exAssistOccPars);
    
    /**
     * 查询医嘱科室信息-博爱门诊医技-耗材记费使用
     * @param map
     * @return
     */
    Map<String, Object> queryProBaOrderDept(Map<String, Object> map);
    /**
	 *查询耗材
	 * @param map
	 */
	void wzQuery(Map<String, Object> map);
	/**
	 *记耗材
	 * @param map
	 */
	void wzConsume(Map<String, Object> map);
	/**
	 *取消耗材
	 * @param map
	 */
	void wzCancelconsume(Map<String, Object> map);
}
