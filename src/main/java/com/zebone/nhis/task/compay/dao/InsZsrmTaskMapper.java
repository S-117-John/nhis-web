package com.zebone.nhis.task.compay.dao;

import com.zebone.nhis.common.module.scm.pub.BdPdIndpd;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;

@Mapper
public interface InsZsrmTaskMapper {
    /**
     * 查询限制用药信息
     * @return
     */
    List<BdPdIndpd> qryRestrictInfo();

    /**
     * 批量新增
     * @param list
     */
    void batchInsertBdPdIndpd(List<BdPdIndpd> list);
}
