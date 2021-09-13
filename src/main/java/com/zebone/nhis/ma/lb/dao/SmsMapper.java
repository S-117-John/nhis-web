package com.zebone.nhis.ma.lb.dao;


import com.zebone.nhis.common.module.ma.sms.SmsTemp;
import com.zebone.nhis.ma.lb.vo.SmsTempVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmsMapper {
    //查询短信模板
    List<SmsTempVo> QrySmsTemp(Map<String, Object> paramMap);
    //查询人员信息
    List<Map<String,Object>> SearchMobile(Map<String, Object> paramMap);
    //查询患者信息
    List<Map<String,Object>> SearchMaster(Map<String, Object> paramMap);
    //查询短信发送记录
    List<Map<String,Object>> SearchSmsSend(Map<String, Object> paramMap);


    Map<String, Object> querySumIpMessage(Map<String, Object> paramMap);

    Map<String, Object> querySumOpMessage(Map<String, Object> paramMap);

}
