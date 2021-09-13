package com.zebone.nhis.bl.ipcg.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.ipcg.vo.MedicalAppVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 医技医嘱
 * @author yangxue
 *
 */
@Mapper
public interface MedicalOrdMapper {
	/**
	 * 医技申请单查询
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
   public List<MedicalAppVo> qryAppInfo(Map<String,Object> paramMap)throws BusException;
   /**
    * 查询医技科室医嘱模板
    * @param paramMap
    * @return
    * @throws BusException
    */
   public List<CnOrder> queryMedicalOrdList(Map<String,Object> paramMap)throws BusException;
}
