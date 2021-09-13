package com.zebone.nhis.cn.ipdw.dao;


import com.zebone.nhis.bl.pub.vo.BlIpDtSptVo;
import com.zebone.nhis.cn.ipdw.vo.CnOpApplyVo;
import com.zebone.nhis.cn.ipdw.vo.ExOpOccVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;
import com.zebone.platform.modules.mybatis.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface ExOpOccMapper {

    /**
     * 手术执行记录查询
     * @param paramMap
     * @return
     */
    public List<ExOpOccVo> queryExOpOccList(Map<String, Object> paramMap);


    /**
     * 手术执行记录查询
     * @param paramMap
     * @return
     */
    public List<CnOpApplyVo> queryCnOpApplyList(Map<String, Object> paramMap);


    /**
     * 根据医嘱号或手术申请主键获取附加手术集合
     * @param paramMap
     * @return
     */
    public List<CnOpSubjoin> queSubOpListByPk(Map<String, Object> paramMap);


    /**
     * 耗材登记列表查询
     * @param paramMap
     * @return
     */
    public List<BlIpDtSptVo> queryBlIpDtSptList(Map<String, Object> paramMap);
}
