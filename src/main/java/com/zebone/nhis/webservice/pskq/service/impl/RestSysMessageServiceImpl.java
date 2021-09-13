package com.zebone.nhis.webservice.pskq.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;
import org.apache.commons.collections.MapUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.dao.SysMsgRecDao;
import com.zebone.nhis.webservice.pskq.model.message.MessageLog;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.service.SysMessageService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service("pskqSysMessageServiceImpl")
public class RestSysMessageServiceImpl implements SysMessageService {

    @Resource(name = "pskqSysMsgRecDao")
    private SysMsgRecDao sysMsgRecDao;

    @Override
    public void save(String param) {
        MessageLog messageLog = JsonUtil.readValue(param, MessageLog.class);
        SysMsgRec rec = new SysMsgRec();
        rec.setTransType(messageLog.getTransType());
        rec.setMsgType(messageLog.getMsgType());
        rec.setMsgId(messageLog.getMsgId());
        rec.setTransDate(new Date());
        rec.setMsgContent(messageLog.getMsgContent());
        rec.setSysCode(messageLog.getSysCode());
        rec.setMsgStatus(messageLog.getMsgStatus());
        DataBaseHelper.insertBean(rec);
    }

    /**
     * 001006001010
     * @param param
     * @return
     */
    @Override
    public List<SysMsgRec> getMessageList(String param, IUser user) {
        Map<String,Object> map = JsonUtil.readValue(param,Map.class);
        List<SysMsgRec> result = sysMsgRecDao.selectMessage(map);
        return result;
    }

    @Override
    public void resend(String param) {

    }

    /**
     * 保存平台发来的参数
     * @param param
     */
    @Override
    public void saveReceiveMessage(String param) {
        Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param,RequestBody.class);
        SysMsgRec rec = new SysMsgRec();
        rec.setTransType("receive");
        rec.setMsgType(requestBody.getEvent().getEventCode());
        rec.setMsgId(requestBody.getId().replace("-",""));
        rec.setTransDate(new Date());
        rec.setMsgContent(param);
        rec.setSysCode("ESB");
        rec.setMsgStatus("RECEIVE");
        DataBaseHelper.insertBean(rec);
    }

    public void save(String param,String result){
    	//修改为手动事物 , 关闭事务自动提交
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
        try {
        	Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
            ResponseBody responseBody = gson.fromJson(result,ResponseBody.class);
            Map<String,Object> ack = responseBody.getAck();

            SysMsgRec rec = new SysMsgRec();
            rec.setTransType("receive");
            rec.setMsgType(responseBody.getEvent().getEventCode());
            rec.setMsgId(MapUtils.getString(ack,"targetMessageId").replace("-",""));
            rec.setTransDate(new Date()); 
            rec.setErrTxt(result);
            rec.setSysCode("NHIS");
            rec.setMsgStatus(MapUtils.getString(ack,"ackCode"));
            rec.setMsgContent(param);
            DataBaseHelper.insertBean(rec);
            dataSourceTransactionManager.commit(transStatus); // 添加失败 回滚事务；
		} catch (Exception e) {
			dataSourceTransactionManager.rollback(transStatus); // 添加失败 回滚事务；
			System.out.println("保存消息信息失败：" + e.getMessage() + "==========================");
		}
        

    }
    
    public void save(String param,Object result,String msgType,String error){
    	//修改为手动事物 , 关闭事务自动提交
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
        try {
        	SysMsgRec rec = new SysMsgRec();
            rec.setTransType("send");
            rec.setMsgType(msgType);
            rec.setMsgId(NHISUUID.getKeyId());
            rec.setTransDate(new Date());
            rec.setErrTxt(String.valueOf(result) + "====" + error);
            rec.setSysCode("NHIS");
            rec.setMsgStatus("AE");
            rec.setMsgContent(param);
            DataBaseHelper.insertBean(rec);
            dataSourceTransactionManager.commit(transStatus); // 添加失败 回滚事务；
		} catch (Exception e) {
			dataSourceTransactionManager.rollback(transStatus); // 添加失败 回滚事务；
			System.out.println("保存消息信息失败：" + e.getMessage() + "==========================");
		}
        
    }
}
