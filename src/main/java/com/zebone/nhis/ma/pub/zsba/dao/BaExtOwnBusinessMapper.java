package com.zebone.nhis.ma.pub.zsba.dao;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;

/**
 * @Classname BaExtOwnBusinessMapper
 * @Description TODO
 * @Date 2020-05-25 14:32
 * @Created by wuqiang
 */
@Mapper
public interface BaExtOwnBusinessMapper {
    List<String>  queryRisAndLisPkCnords(List<String> pkCnords);

    List<ExOrderOcc>  queryExOrderOcc(List<String> pkCnords);

    List<BlIpDt> queryBlIpDt(List<String> pkCnords);


}
