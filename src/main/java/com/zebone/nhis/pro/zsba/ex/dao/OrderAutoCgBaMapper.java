package com.zebone.nhis.pro.zsba.ex.dao;

import com.zebone.nhis.common.module.base.bd.wf.BdOrdAutoexec;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.pro.zsba.ex.vo.AutoExOrdItemBaVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderAutoCgBaMapper {
    /**
     * 查询自动执行设置内容
     *
     * @param paramMap{pkOrg:机构主键}
     * @return
     */
    public List<BdOrdAutoexec> queryAutoExec(Map<String, Object> paramMap);

    /**
     * 根据医嘱查询对应执行单
     *
     * @param paramMap{pkOrds:医嘱主键集合}
     * @return
     */
    public List<ExlistPubVo> queryExecListByOrd(Map<String, Object> paramMap);

    /**
     * 根据医嘱查询对应收费项目
     *
     * @param paramMap{pkOrds:医嘱主键集合}
     * @return
     */
    public List<AutoExOrdItemBaVo> queryOrdItem(Map<String, Object> paramMap);
}
