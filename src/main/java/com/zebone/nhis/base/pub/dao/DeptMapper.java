package com.zebone.nhis.base.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeptMapper {
    /**
     * 查询科室信息
     *
     * @param paramMap
     * @return
     */
    public List<BdOuDept> getDeptInfos(Map<String, Object> paramMap);

    /**
     * 查询科室及科室类型子表信息
     *
     * @param paramMap
     * @return
     */
    public List<BdOuDept> getDeptAndTypeInfos(Map<String, Object> paramMap);

    public int getCountDept(Map<String, Object> paramMap);

    /**
     * 查询机构下护理单元
     *
     * @param paramMap
     * @return
     */
    public List<BdOuDept> queryDeptNsList(Map<String, Object> paramMap);

    /**
     * 根据主键查询科室
     *
     * @param pkDept 主键
     * @return com.zebone.nhis.common.module.base.ou.BdOuDept
     * @author jesse
     */
    BdOuDept qryByPK(@Param("pkDept") String pkDept);

}
