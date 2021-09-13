package com.zebone.nhis.ma.pub.lb.dao;


import com.zebone.nhis.ma.pub.lb.vo.archInfoVo.*;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LbGXarchMapper {
    /**
     * 根据时间查询病案信息
     * @param param
     * @return
     */
    List<PiArchInfoVo> qryArchInfo(Map<String, Object> param);
    /**
     * 查询病案主页信息
     * @param param
     * @return
     */
    Medical qryArchMainInfo(String param);

    /**
     * 查询病案附页信息
     * @param param
     * @return
     */
//    Map<String, Object> qryArchAttach(String param);
    MedicalAttach qryArchAttach(String param);
    /**
     * 查询手术记录
     * @param param
     * @return
     */
    List<ListOperation> qryListOperation(String param);

    /**
     * 查询手术记录明细
     * @param param
     * @return
     */
    List<ListOperationDetail> qryListOpeDetail(Map<String, Object> param);

    /**
     * 查询全部或已上传
     * @param param
     * @return
     */
    List<PiArchInfoVo> qryUpLoadByDate(Map<String,Object> param);

    /**
     * 查询未上传
     * @param param
     * @return
     */
    List<PiArchInfoVo> qryNoUpLoadByDate(Map<String,Object> param);



















}
