package com.zebone.nhis.ma.pub.platform.zsrm.service;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Response;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;

/**
 * 基础数据相关接口
 */
public interface ZsphBasicDataService {
    //机构信息查询接口
    Response getBdOuOrg(String param);
    //科室信息查询接口
    Response getBdOuDept(String param);
    //保存新增、修改科室信息接口
    Response saveBdOuDept(String param);
    //人员信息查询
    Response getPractitioner(String param);
    //人员信息新增、更新
    Response savePractitioner(String param);
    //诊断信息查询
    Response getBdTermDiag(String param);
    //诊断信息新增修改
    void saveBdTermDiag(String param);
    //收费项目信息查询
    Response getChargeBdIteminfo(String param);
    //诊疗项目信息查询
    Response getChargeBdOrdinfo(String param);
    //药品字典查询
    void getBdPd(String param, ResultListener listener);
}
