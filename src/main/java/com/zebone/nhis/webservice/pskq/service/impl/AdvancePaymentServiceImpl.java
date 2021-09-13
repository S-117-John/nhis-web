package com.zebone.nhis.webservice.pskq.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.InAppPay;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;
import com.zebone.nhis.webservice.pskq.service.AdvancePaymentService;
import com.zebone.nhis.webservice.pskq.utils.PskqMesUtils;
import com.zebone.nhis.webservice.service.BlPubForWsService;
import com.zebone.nhis.webservice.service.InvPubForWsService;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

public class AdvancePaymentServiceImpl implements AdvancePaymentService {
    @Override
    public void save(String param, ResultListener listener) {

        try{
            ApplicationUtils apputil = new ApplicationUtils();
            Map<String,Object> responseMap = new HashMap<String,Object>(16);
            //String result = "";
            Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HH24mmss").create();
            RequestBody requestBody = gson.fromJson(param,RequestBody.class);
            ResponseBody responseBody = new ResponseBody();
            responseBody.setService(requestBody.getService());
            responseBody.setEvent(requestBody.getEvent());
            responseBody.setId(requestBody.getId());
            responseBody.setCreationTime(requestBody.getCreationTime());
            responseBody.setSender(requestBody.getSender());
            responseBody.setReceiver(requestBody.getReceiver());
            String reqID = requestBody.getId();
            
            User user = PskqMesUtils.getUserExt(requestBody.getSender().getId());
    		if(user == null) {
    			listener.error("发送方：【"+requestBody.getSender().getSoftwareName().getName()+ "】；未在his注册工号，请先联系his注册工号！");
    			return;
    		}
    		UserContext.setUser(user);
            
            List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("ADVANCE_PAYMENT");
            Map<String, Object> paramMap = new HashMap<>(16);
            InAppPay inAppPay = (InAppPay) MessageFactory.deserialization(list, new InAppPay());
            //患者主键
            if(StringUtils.isNotEmpty(inAppPay.getPkPatient())){
                paramMap.put("pkPi", inAppPay.getPkPatient());
            } else{
                listener.error("未导入患者信息主建");
                return;
            }


            //付款方式
            if(StringUtils.isNotEmpty(inAppPay.getPatientTypeCode())){
                paramMap.put("dtPaymode", inAppPay.getPatientTypeCode());
            } else{
                listener.error("未导入付款方式代码");
                return;
            }


            //充值类型代码
            if(StringUtils.isNotEmpty(inAppPay.getAdvanceTypeCode())){
                paramMap.put("payType", inAppPay.getAdvanceTypeCode());
            } else{
                listener.error("未导入充值类型代码");
                return;
            }


            //充值总费用
            if(StringUtils.isNotEmpty(inAppPay.getTotalFee())){
                paramMap.put("payAmt", inAppPay.getTotalFee());
            } else{
                listener.error("未导入充值总费用");
                return;
            }

            //充值订单号
            if(StringUtils.isNotEmpty(inAppPay.getPaymentOrderNo())){
                paramMap.put("orderno", inAppPay.getPaymentOrderNo());
            } else{
                listener.error("未导入充值订单号");
                return;
            }


            //充值交易流水号
            if(StringUtils.isNotEmpty(inAppPay.getTransactionSerialNo())){
                paramMap.put("flowno", inAppPay.getTransactionSerialNo());
            } else{
                listener.error("未导入充值交易流水号");
                return;
            }


            //充值日期时间
            if(StringUtils.isNotEmpty(inAppPay.getAdvanceDateTime())){
                paramMap.put("paytime", inAppPay.getAdvanceDateTime());
            } else{
                listener.error("未导入充值日期时间");
                return;
            }

            //查询患者就诊信息   待定 字段流程未明确
            Map<String, Object> PatMap = DataBaseHelper.queryForMap("select * from pv_encounter pv left join pi_master pi on pv.pk_pi=pi.pk_pi and pi.del_flag='0' where pv.eu_pvtype= '3' and (pv.eu_status = '0' or pv.eu_status = '1') and pi.pk_pi= ?", paramMap.get("pkPi").toString());
            if(!BeanUtils.isNotNull(PatMap)){
                listener.error("未查询患者就诊信息");
                return;
            }
            
            /**
             * 获取当前可用票据
             */
            String sql = "select inv.inv_prefix,inv.cur_no,cate.length,inv.cnt_use,inv.pk_empinv,inv.pk_invcate, cate.prefix from bl_emp_invoice inv "
                    + "inner join bd_invcate cate on inv.pk_invcate=cate.pk_invcate where inv.del_flag = '0' and inv.pk_org = ? "
                    + "and inv.flag_use ='1' and inv.flag_active ='1' and cate.eu_type = ? and inv.pk_emp_opera = ?";
            Map<String, Object> queryForMap = new HashMap<String, Object>();

            queryForMap = DataBaseHelper.queryForMap(sql, user.getPkOrg(), Constant.HOSPREPAY, user.getPkEmp());

            if(queryForMap!=null){
                String curNo = null;
                if(queryForMap.get("curNo") == null){

                    listener.error("当前存在可用票据，但是当前号为空");
                    return;
                }else{
                    curNo = queryForMap.get("curNo").toString();
                }
                //票据号=票据分类前缀+票据前缀+号段组成
                String prefix = queryForMap.get("prefix") == null?"":queryForMap.get("prefix").toString(); //票据分类前缀
                String invPrefix = queryForMap.get("invPrefix") == null?"":queryForMap.get("invPrefix").toString();
                if(queryForMap.get("length") != null){
                    long length = Long.parseLong(queryForMap.get("length").toString());
                    ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                    InvPubForWsService invPubForWsService = applicationContext.getBean("invPubForWsService", InvPubForWsService.class);
                    curNo = invPubForWsService.flushLeft("0", length, curNo);
                    String curCodeInv = prefix + invPrefix + curNo;
                    queryForMap.put("curCodeInv", curCodeInv);
                }else{
                    String curCodeInv = prefix + invPrefix + curNo;
                    queryForMap.put("curCodeInv", curCodeInv);
                }
            }else{
                listener.error("已无可用票据！，请联系管理员维护发票信息！");
                return;
            }
            Long cnt = (long)1;

            Map<String, Object> empinv = DataBaseHelper.queryForMap("select cnt_use,pk_emp_opera from bl_emp_invoice where del_flag = '0' and pk_empinv = ?", queryForMap.get("pkEmpinv").toString());
            Long cntUse = Long.parseLong(empinv.get("cntUse").toString());
            if (cntUse - cnt < 0) {
                listener.error("更新后的票据可用张数为" + (cntUse - cnt) + "，小于0！发票不足！");
                return;
            }

            /**
             * 住院预交金写表BL_DEPOSIT
             */
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            BlPubForWsService blPubForWsService = applicationContext.getBean("blPubForWsService", BlPubForWsService.class);
            BlDeposit vo = new BlDeposit();
            vo = blPubForWsService.PskqHealthInsertBlDeposit(Constant.OTHERINV,vo,queryForMap,paramMap,PatMap,user);
            ResponseJson result = apputil.execService("BL", "BlPrePayService", "saveDeposit", vo,user);
            if(result.getStatus()== Constant.SUC){
                BlDeposit bldVo = JsonUtil.readValue(JsonUtil.writeValueAsString(result.getData()), BlDeposit.class);
                /**
                 * 支付信息写入外部支付接口记录表bl_ext_pay
                 */
                blPubForWsService.PskqHealthPayment(bldVo,paramMap,PatMap,user,null);
                responseMap.put("codeDepo", bldVo.getCodeDepo());
                responseMap.put("reptNo", bldVo.getReptNo());
                List<Map<String, Object>> blDepoList = DataBaseHelper.queryForList("SELECT * FROM BL_DEPOSIT WHERE EU_DIRECT=1 and PK_PV=?", bldVo.getPkPv());
                BigDecimal amtAcc = BigDecimal.ZERO;
                for(Map<String, Object> map : blDepoList){
                    Double pric = Double.parseDouble(map.get("amount").toString());
                    BigDecimal amt=BigDecimal.valueOf(pric);
                    amtAcc=amtAcc.add(amt);
                }
                responseMap.put("balance", amtAcc);

            }else{
                listener.error(result.getDesc());
                return;
            }

            listener.success("success");
        } catch (Exception e) {
            listener.error(e.getMessage());
        }
    }
}
