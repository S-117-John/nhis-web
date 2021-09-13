package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphConstant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.ZsphMsgService;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.ProcessData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.ResponseData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * 解析器，不通结果可以自行实现deserialize即可
 */
public class ResponseResolver {
    protected static Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");

    protected final String STATUS_ADD = "SAVE";
    protected final String STATUS_ACK = "ACK";
    protected final String STATUS_ERROR = "ERROR";

    protected ZsphMsgService msgService;

    public ResponseResolver(){
        msgService = ServiceLocator.getInstance().getBean(ZsphMsgService.class);
    }

    /**
     *  执行解析并按照返回，保存数据，不允许重写
     * @param processorData
     * @return
     */
    public final Object process(ProcessData processorData) {
        Object rs = null;
        if(processorData != null
                && processorData.getRequestData()!=null && processorData.getResponseData()!=null){
            RequestData requestData = processorData.getRequestData();
            ResponseData responseData = processorData.getResponseData();
            //异常情况下，就不要再去解析，直接插入异常
            String error = "",status=STATUS_ACK;
            if(responseData.isSuccess()){
                try{
                    error = "返回数据："+responseData.getData();
                    rs = deserialize(responseData);
                } catch (Exception e){
                    logger.error("解析异常：",e);
                    status = STATUS_ERROR;
                    error += "|解析异常："+e.getMessage();
                }
            } else {
                status = STATUS_ERROR;
                error = responseData.getData();
            }
            msgService.addMsg(requestData,status,error);
        }
        return rs;
    }

    /**
     * 解析数据，成功后:去掉头部信息 返回成功的具体内容，失败的异常
     * @param responseData
     * @return
     */
    public Object deserialize(ResponseData responseData){
        BusinessBase businessBase = ZsphMsgUtils.fromJson(responseData.getData(), BusinessBase.class);
        List<Entry> entryList = null;
        List<Issue> issueList = null;
        if(businessBase !=null){
            //只要有error就认为失败
            if((CollectionUtils.isNotEmpty(entryList=businessBase.getEntry()))){
                Response response = entryList.get(0).getResponse();
                Outcome outcome = TransfTool.mapToBean(Outcome.class,response.getOutcome());
                issueList=(response !=null && response.getOutcome()!=null)?outcome.getIssue():null;
            } else {
                issueList=businessBase.getIssue();
            }
        }
        if(CollectionUtils.isEmpty(issueList)){
            throw new BusException("返回结果中未解析到issue");
        }
        return getSusObject(issueList);
    }

    private Object getSusObject(List<Issue> issueList) {
        Optional<Issue> issueErr = issueList.stream().filter(x -> ZsphConstant.RES_ERR_CODE.equalsIgnoreCase(x.getSeverity())).findFirst();
        if(issueErr.isPresent()){
            throw new BusException(issueErr.get().getDiagnostics());
        }
        return issueList.get(0);
    }
}
