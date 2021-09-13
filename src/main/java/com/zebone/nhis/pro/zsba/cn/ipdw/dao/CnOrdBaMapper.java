package com.zebone.nhis.pro.zsba.cn.ipdw.dao;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.pro.zsba.cn.ipdw.vo.CanRisLabVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface CnOrdBaMapper {

    public List<CanRisLabVo> qryRisOrd(Map<String, Object> m);

    public List<CanRisLabVo> qryLabOrd(Map<String, Object> m);


}
