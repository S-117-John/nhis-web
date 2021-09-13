package com.zebone.nhis.cn.ipdw.dao;

import com.zebone.nhis.cn.ipdw.vo.CnTransApprovalVo;
import com.zebone.nhis.common.module.cn.ipdw.CnTransApproval;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 输血医嘱
 */
@Mapper
public interface CnBloodMapper {

    public List<Map<String, Object>> qryMaxBl(Map<String, Object> paramMap);

    public List<CnTransApprovalVo> qryBlApp(Map<String, Object> paramMap);

    public List<Map<String, Object>> qryMaxOrd(Map<String, Object> paramMap);
}
