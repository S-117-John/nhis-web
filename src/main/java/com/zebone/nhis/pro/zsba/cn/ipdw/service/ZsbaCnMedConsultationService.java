package com.zebone.nhis.pro.zsba.cn.ipdw.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.cn.ipdw.dao.ZsbaCnMedConsultationMapper;
import com.zebone.nhis.pro.zsba.cn.ipdw.vo.CnOrdAntiExpre;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Classname ZsbaCnMedConsultationService
 * @Description 抗菌药会诊申请业务类
 * @Date 2021-01-19 9:38
 * @Created by wuqiang
 */
@Service
public class ZsbaCnMedConsultationService {
   @Autowired
    private ZsbaCnMedConsultationMapper zsbaCnMedConsultationMapper;
    /**
     * @Description 保存临床-医嘱-特殊级抗菌药物专家库信息
     * @auther wuqiang
     * @Date 2021-01-19
     * @Param [param, user]
     * @return void
     */
    public void saveCnOrdAntiExpre(String param, IUser user){
        List< CnOrdAntiExpre> cnOrdAntiExpreList = JsonUtil.readValue(param, new TypeReference<List< CnOrdAntiExpre>>() {
        });

        if (CollectionUtils.isEmpty(cnOrdAntiExpreList)){
            return;
        }
        String sql="delete  from CN_ORD_ANTI_EXPR";
        for (CnOrdAntiExpre cnOrdAntiExpre:cnOrdAntiExpreList){
            ApplicationUtils.setDefaultValue(cnOrdAntiExpre,true);
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrdAntiExpre.class),cnOrdAntiExpreList);
    }
  /**
   * @Description 查询临床-医嘱-特殊级抗菌药物专家库信息
   * @auther wuqiang
   * @Date 2021-01-19
   * @Param [param, user]
   * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
   */
    public List<Map<String,Object>> getCnOrdAntiExpre(String param, IUser user){
       return zsbaCnMedConsultationMapper.getCnOrdAntiExpre();

    }
    /**
     * @Description 特殊抗菌药申请单查询
     * @auther wuqiang
     * @Date 2021-01-20
     * @Param [param, user]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public List<Map<String,Object>> getCnOrdAntiApply(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (MapUtils.isEmpty(paramMap)){
            return null ;
        }
         return  zsbaCnMedConsultationMapper.getCnOrdAntiApply(paramMap);
    }
    
}
