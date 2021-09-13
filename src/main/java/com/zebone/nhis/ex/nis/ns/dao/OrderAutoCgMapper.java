package com.zebone.nhis.ex.nis.ns.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.wf.BdOrdAutoexec;
import com.zebone.nhis.ex.nis.ns.vo.AutoExOrdItemVo;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OrderAutoCgMapper {
	/**
	 * 查询自动执行设置内容
	 * @param paramMap{pkOrg:机构主键}
	 * @return
	 */
   public List<BdOrdAutoexec> queryAutoExec(Map<String,Object> paramMap);
   /**
    * 根据医嘱查询对应执行单
    * @param paramMap{pkOrds:医嘱主键集合}
    * @return
    */
   public List<ExlistPubVo> queryExecListByOrd(Map<String,Object> paramMap);
   
   /**
    * 根据医嘱查询对应收费项目
    * @param paramMap{pkOrds:医嘱主键集合}
    * @return
    */
   public List<AutoExOrdItemVo> queryOrdItem(Map<String,Object> paramMap);
}
