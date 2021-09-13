package com.zebone.nhis.webservice.zhongshan.dao;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.webservice.zhongshan.vo.ThiBdOrdLab;
import com.zebone.nhis.webservice.zhongshan.vo.ThiPeBdOrd;
import com.zebone.nhis.webservice.zhongshan.vo.ThiQueryPiInf;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Classname ThiIpCnWebMapper
 * @Description 三方接口数据库交互层
 * @Date 2021-04-13 15:42
 * @Created by wuqiang
 */
@Mapper
public interface ThiIpCnWebMapper {
    CnOrder getCnOrder(@Param("ordsn") String ordsn, @Param("param") String param);
    CnOrder getCnOrderPkCnord(@Param("pkCnord") String pkCnord, @Param("param") String param);
    void updateCancleCnorder(CnOrder cnOrder);

    List<Map<String, Object>> getPvInfo(ThiQueryPiInf thiQueryPiInf);

    List<BlIpDt> getBlIpDts(List<String> collect);

    List<BlIpDt> getBlIpDtsByPkCgipBack(List<String> collect);

    void  updateCancleExOrdExcc(CnOrder cnOrder);

    ThiBdOrdLab  getThiBdOrdLab(String codeItem);

    void   deleteCnorder(String pkPv);

    void  deleteCnLabApply(String pkPv);

    void  deleteExAssistOcc(String pkPv);

    void  deleteExAssistOccDt(String pkPv);

    ThiPeBdOrd getThiPeBdOrd(@Param("pkOrd")String pkOrd, @Param("codeOrd")String codeOrd);
}
