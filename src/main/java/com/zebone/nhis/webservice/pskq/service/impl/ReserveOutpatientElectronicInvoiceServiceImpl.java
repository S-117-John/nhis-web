package com.zebone.nhis.webservice.pskq.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.sd.service.EnoteService;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.model.ElectronicInvoice;
import com.zebone.nhis.webservice.pskq.service.ElectronicInvoiceService;
import com.zebone.nhis.webservice.pskq.service.PskqInitService;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 挂号缴费电子票据
 */
public class ReserveOutpatientElectronicInvoiceServiceImpl implements ElectronicInvoiceService {

    public static ElectronicInvoiceService getInstance(){
        return new ReserveOutpatientElectronicInvoiceServiceImpl();
    }

    @Override
    public Object getElectronicInvoice(ElectronicInvoice electronicInvoice) {


        String sql = "select argu.arguval from bd_res_pc pc "
                +" left join bd_res_pc_argu argu on pc.PK_PC = argu.pk_pc and argu.flag_stop = '0' and argu.DEL_FLAG = '0'"
                +" where pc.flag_active = '1' and pc.del_flag = '0' and pc.eu_addrtype = '0' and argu.code_argu = 'BL0031' and pc.addr = ?";

        Map<String,Object> qryMap = DataBaseHelper.queryForMap(sql, electronicInvoice.getAddress());
        
        String rtnName = null;

        if(qryMap!=null && qryMap.size()>0){
            rtnName = CommonUtils.getString(qryMap.get("arguval"));
        }

        if(CommonUtils.isEmptyString(rtnName)){
            rtnName = "0";
        }
        Object retInv = null;
        if("1".equals(rtnName)){
            try{
                Map<String,Object> paramMap = new HashMap<>(16);
                paramMap.put("pkPv", electronicInvoice.getPkPv());
                paramMap.put("pkSettle", electronicInvoice.getPkSettle());
                paramMap.put("flagPrint", "0");//打印纸质票据
                //调用开立票据接口生成票据信息
                ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                EnoteService enoteService = applicationContext.getBean("enoteService", EnoteService.class);
                retInv = enoteService.eBillRegistration(new Object[]{paramMap, electronicInvoice.getUser()});

            }catch(Exception e){
                throw new BusException(e.getMessage());
            }
        }

        return retInv;
    }
}
