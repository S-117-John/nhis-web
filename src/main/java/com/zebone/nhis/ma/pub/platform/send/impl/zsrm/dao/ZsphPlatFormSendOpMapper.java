package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao;

import com.zebone.nhis.ma.pub.platform.zsrm.vo.PriceInquiryVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ZsphPlatFormSendOpMapper {

    /**
     * 物价查询接口返回数据
     * @param paramList
     * @return
     */
    public List<PriceInquiryVo> getPriceInquiry(Map<String,Object> param);
}
