package com.zebone.nhis.compay.ins.zsrm.dao.base;

import com.zebone.nhis.compay.ins.zsrm.vo.base.HisInfo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YbSpecialOperateMapper {
    /**
     * 查询全国医保,his信息
     * @param ybPkReg
     * @return
     */
    List<HisInfo> queryHisInfoByQgyb(@Param("ybPkReg") String ybPkReg);

    /**
     * 查询工伤医保,his信息
     * @param ybPkReg
     * @return
     */
    List<HisInfo> queryHisInfoByGsyb(@Param("ybPkReg") String ybPkReg);
}
