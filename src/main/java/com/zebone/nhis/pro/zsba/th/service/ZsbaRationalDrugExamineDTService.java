package com.zebone.nhis.pro.zsba.th.service;


import com.zebone.nhis.pro.zsba.th.service.vo.PrescriptionQu;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname RationalDrugExamineDTService
 * @Description 大通处方审核接口
 * @Date 2021-02-20 16:27
 * @Created by wuqiang
 */

@Service
public class ZsbaRationalDrugExamineDTService {
    
    
   /**
    * @Description 保存门诊处方审核结果
    * @auther wuqiang
    * @Date 2021-02-20 
    * @Param [param, user]
    * @return void
    */
    public void  saveAuditResult(String param, IUser user) throws ParseException {
        List<PrescriptionQu> preses = JsonUtil.readValue(param,new TypeReference<List<PrescriptionQu>>() {});
        if(CollectionUtils.isEmpty(preses)){
            return ;
        }
        List<String> updateEuSqls=new ArrayList<>(5);
        for (PrescriptionQu pr:preses ){
            updateEuSqls.add("update CN_PRESCRIPTION set eu_pass ='"+ pr.getText()+"' where PRES_NO='"+pr.getId()+"'");
        }
        DataBaseHelper.batchUpdate(updateEuSqls.toArray(new String[0]));
    }

}
