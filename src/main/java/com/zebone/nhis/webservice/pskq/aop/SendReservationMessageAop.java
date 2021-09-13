package com.zebone.nhis.webservice.pskq.aop;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.webservice.pskq.factory.Message;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.factory.MessageFactoryBean;
import com.zebone.nhis.webservice.pskq.model.Outpatient;
import com.zebone.nhis.webservice.pskq.model.ReserveOutpatient;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;
import com.zebone.nhis.webservice.pskq.utils.RestTemplateUtil;
import com.zebone.platform.modules.utils.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Aspect
public class SendReservationMessageAop {

    @AfterReturning(value="@annotation(com.zebone.nhis.webservice.pskq.annotation.SendReservationMessage)",returning = "object")
    public void after(JoinPoint joinPoint, Object object){

       try{

           if(object==null&& !(object instanceof String)){
               return;
           }
           Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
           ResponseBody responseBody = gson.fromJson((String) object,ResponseBody.class);
           if(responseBody==null){
               return;
           }
           if(responseBody.getAck()==null){
               return;
           }
           if(!responseBody.getAck().containsKey("ackCode")||StringUtils.isEmpty(responseBody.getAck().get("ackCode"))){
                return;
           }
           if(!"AA".equals(responseBody.getAck().get("ackCode"))){
               return;
           }
           Object[] args = joinPoint.getArgs();
           if(args.length!=1){
               return;
           }
           String param = (String) args[0];
           RequestBody requestBody = gson.fromJson(param,RequestBody.class);
           List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("RESERVE_OUTPATIENT");
           //预约信息
           ReserveOutpatient reserveOutpatient = (ReserveOutpatient) MessageFactory.deserialization(list, new ReserveOutpatient());
           if(reserveOutpatient==null){
               return;
           }
           if(StringUtils.isEmpty(reserveOutpatient.getPayState())){
                return;
           }
           //已支付，发送hsi挂号成功消息
           if("1".equals(reserveOutpatient.getPayState())){
               Outpatient outpatient = new Outpatient();
               Message message = MessageFactoryBean.getInstance().getMessage(outpatient);
               Map<String,Object> map = new HashMap<>(16);
               RequestBody requestBodyMessage = message.getRequestBody(map);
               String result = JSON.toJSONString(requestBodyMessage);
               result = RestTemplateUtil.getInstance().post(result);
               ResponseBody responseBodyMessage = JsonUtil.readValue(result,ResponseBody.class);
               if(responseBodyMessage.getAck()!=null&&
                       responseBodyMessage.getAck().containsKey("ackCode")&&
                       "AA".equals(responseBodyMessage.getAck())){
                    //消息发送成功
               }else {
                   //消息发送失败
               }

           }
       }catch (Exception exception){

       }

    }
}
