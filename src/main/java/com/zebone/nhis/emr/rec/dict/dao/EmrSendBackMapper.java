package com.zebone.nhis.emr.rec.dict.dao;


import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatRec;
import com.zebone.nhis.emr.rec.dict.vo.EmrDocListPrarm;
import com.zebone.nhis.emr.rec.dict.vo.EmrOpenEditRecParam;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * create by: gao shiheng
 *终末质控-病历退回-mapper
 * @Param: null
 * @return
 */
@Mapper
public interface EmrSendBackMapper {

    /**
     * create by: gao shiheng
     *根据患者就诊主键查询已科室质控提交未归档的病历
     * @return a
     * @Param: null
    */
    public List<EmrDocListPrarm> queryEmrDocList(String pkPv);

    /**
     * create by: gao shiheng
     *查询患者病历记录
     * @Param: null
     * @return
     */
    public EmrPatRec selectEmrPatRecByPkPv(String pkPv);

    public List<EmrMedRec> selectEmrMedRecByPkREc(List list);

    /**
     * create by: gao shiheng
     * create time: 18:03 2019/5/10
     *查询病历召回申请列表
     * @Param: null
     * @return
     */
    public List<EmrOpenEditRecParam> queryEmrRecallList(Map<String,Object> map);

    /**
     * create by: gao shiheng
     * create time: 18:41 2019/5/13
     *病历召回审批查询病历文档
     * @Param: null
     * @return
     */
    public List<EmrMedRec> queryEmrDoclList(Map<String,Object> map);
}
