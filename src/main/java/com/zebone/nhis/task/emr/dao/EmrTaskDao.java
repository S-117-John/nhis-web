package com.zebone.nhis.task.emr.dao;

import com.zebone.nhis.task.emr.vo.EmrLuceneInfo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmrTaskDao {

    /**
     * 自费金额为空的人
     * @return
     */
    List<String> listPkPv();

    /**
     * 更新自费
     * @param pkPv
     * @param selfCost
     * @return
     */
    int update(@Param("pkPv") String pkPv,@Param("selfCost") Double selfCost);

    /**
     * 获取自费金额
     * @param pkPv
     * @return
     */
    List<Double> getCost(String pkPv);

    /**
     * 根据部门编号字符串, 获取该部门下的住院病人Pkpv集合
     * @param deptCode
     * @return
     */
    List<String> getPkPvByDeptCode(@Param("deptCode") String deptCode);

    /**
     * 根据pkpv 搜索docxml
     * @param pkPv
     * @return
     */
    List<String> getDocXmlByPkpv(@Param("pkPv") String pkPv);

    /**
     * 保存
     */
    public int saveEmrLuceneInfo(EmrLuceneInfo emrLuceneInfo);
}
