package com.zebone.nhis.cn.pub.dao;

import com.zebone.nhis.cn.pub.vo.CnOrderPrintVo;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.cn.ipdw.CnOrderPrint;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrdPrintMapper {

    /**
     * 查询已打印数据
     * @param param
     * @return
     */
    public List<CnOrderPrintVo> qryPrint(Map<String, Object> param);

    /**
     * 查询医嘱数据
     * @param param
     * @return
     */
    public List<Map<String,Object>> qryOrd(Map<String, Object> param);

    /**
     * 查询人员签名信息，注意由于签名是blob类型，所以只返回了主键、签名两个字段
     * @param param
     * @return
     */
    public List<BdOuEmployee> qryEmpPic(Map<String, Object> param);
}
