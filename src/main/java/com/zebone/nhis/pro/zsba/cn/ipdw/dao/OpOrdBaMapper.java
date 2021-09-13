package com.zebone.nhis.pro.zsba.cn.ipdw.dao;

import com.zebone.nhis.pro.zsba.cn.ipdw.vo.OpOrderVO;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OpOrdBaMapper {
    /**
     * 根据患者获取待核对医嘱列表
     * @param map
     * @return
     */
    public List<OpOrderVO> queryOrderCheckList(Map<String,Object> map);

    /**
     * 查询功能科室患者列表
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryPiFuncInfo(Map<String,Object> map);
    
    /**
     * 查询手术提示试图的手术信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryPtopOperatInfo(Map<String,Object> map);
    
    public List<Map<String,Object>> queryPkEmpInfo(List<String> codeEmpList);
}
