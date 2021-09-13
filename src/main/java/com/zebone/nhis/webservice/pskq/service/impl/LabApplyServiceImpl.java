package com.zebone.nhis.webservice.pskq.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.LabApply;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.service.LabApplyService;
import com.zebone.nhis.webservice.service.PskqPubForWsService;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 检验申请服务
 */
public class LabApplyServiceImpl implements LabApplyService {
	
    @SuppressWarnings("unchecked")
	@Override
    public void editLabApply(String param, ResultListener listener) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
            RequestBody requestBody = gson.fromJson(param, RequestBody.class);
            Map<String, Object> query = requestBody.getMessage();
            if (query.containsKey("LAB_APPLY") && query.get("LAB_APPLY") != null) {
                List<DataElement> dataElement = (List<DataElement>) query.get("LAB_APPLY");
                LabApply labApply = (LabApply) MessageFactory.deserialization(dataElement, new LabApply());
                if (CommonUtils.isEmptyString(labApply.getLabApplyNo())) {
                	listener.error("申请单号不能为空");
                    return;
                }
                CnOrder cnOrder = DataBaseHelper.queryForBean("select * from cn_order where CODE_APPLY = ? and del_flag = '0' and  code_ordtype like '03%' ",
                		CnOrder.class, labApply.getLabApplyNo());
                if (cnOrder == null) {
                    listener.error("没有查询到医嘱信息");
                    return;
                }
                CnLabApply cnLabApply = DataBaseHelper.queryForBean("select * from CN_LAB_APPLY where PK_CNORD = ? and del_flag = '0'",CnLabApply.class, cnOrder.getPkCnord());
                if (cnLabApply == null) {
                    listener.error("没有查询到申请单信息");
                    return;
                }
                //更新申请单状态
                StrategyFactory.creator(requestBody.getEvent().getEventCode()).doOperate(labApply,cnOrder,cnLabApply);
                listener.success("消息处理成功");
            } else {
                listener.error("没有查询到申请单信息");
            }
        } catch (Exception e) {
            listener.exception(e.getMessage());
        }
    }



    public interface Strategy {

        int doOperate(LabApply labApply,CnOrder cnOrder,CnLabApply cnLabApply) throws Exception;
    }

    /**
     * 条码打印
     */
    public static class BarcodePrintStrategy implements Strategy {

        @Override
        public int doOperate(LabApply labApply,CnOrder cnOrder,CnLabApply cnLabApply) throws Exception {
            DataBaseHelper.update("");
            return 1;
        }
    }

    /**
     * 取消打印
     */
    public static class CancelPrintStrategy implements Strategy {

        @Override
        public int doOperate(LabApply labApply,CnOrder cnOrder,CnLabApply cnLabApply)  throws Exception {
            //
            return 1;
        }
    }

    /**
     * 样本采集
     */
    public static class SampleCollectionStrategy implements Strategy {

        @Override
        public int doOperate(LabApply labApply,CnOrder cnOrder,CnLabApply cnLabApply) throws Exception{
            if (CommonUtils.isEmptyString(labApply.getCollectOperaId())) {
            	throw new BusException("标本采集人不能为空");
            }
            if(!"1".equals(cnLabApply.getEuStatus())){
            	 throw new BusException("当前申请单状态为非提交状态，不允许采集");
            }
            User user = LbSelfUtil.getDefaultUser(labApply.getCollectOperaId());
			PvEncounter pvEncounter = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where pk_pv = ? and del_flag = '0'",PvEncounter.class,cnOrder.getPkPv());
			ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
			PskqPubForWsService pskqPubForWsService = applicationContext.getBean("pskqPubForWsService", PskqPubForWsService.class);
			pskqPubForWsService.lisCollection(cnOrder, pvEncounter, labApply, cnLabApply, user);
            return 1;
        }
    }

    /**
     * 样本送检
     */
    public static class SampleSubmissionStrategy implements Strategy {

        @Override
        public int doOperate(LabApply labApply,CnOrder cnOrder,CnLabApply cnLabApply) {
            //
            return 1;
        }
    }

    /**
     * 样本送达
     */
    public static class SampleDeliveryStrategy implements Strategy {

        @Override
        public int doOperate(LabApply labApply,CnOrder cnOrder,CnLabApply cnLabApply) {
            //
            return 1;
        }
    }

    /**
     * 样本接收
     */
    public static class SampleReceptionStrategy implements Strategy {

        @Override
        public int doOperate(LabApply labApply,CnOrder cnOrder,CnLabApply cnLabApply) throws Exception  {
            if (CommonUtils.isEmptyString(labApply.getReceiveOperaId())) {
            	throw new BusException("标本接收人不能为空");
            }
            if(!"2".equals(cnLabApply.getEuStatus())){
            	 throw new BusException("当前申请单状态为非采集状态，不允许接收");
            }
            User user = LbSelfUtil.getDefaultUser(labApply.getReceiveOperaId());
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
			PskqPubForWsService pskqPubForWsService = applicationContext.getBean("pskqPubForWsService", PskqPubForWsService.class);
			pskqPubForWsService.lisReceive(cnOrder, labApply, cnLabApply, user);
            return 1;
        }
    }

    /**
     * 样本退回
     */
    public static class SampleReturnStrategy implements Strategy {

        @Override
        public int doOperate(LabApply labApply,CnOrder cnOrder,CnLabApply cnLabApply) throws Exception  {
            if (CommonUtils.isEmptyString(labApply.getCancelOperaId())) {
            	throw new BusException("标本取消人不能为空");
            }
            if (CommonUtils.isEmptyString(labApply.getCancelDateTime())) {
            	throw new BusException("取消时间不能为空");
            }
            if(!"3".equals(cnLabApply.getEuStatus())){
            	 throw new BusException("当前申请单状态为非接收状态，不允许取消接收");
            }
            User user = LbSelfUtil.getDefaultUser(labApply.getCancelOperaId());
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
			PskqPubForWsService pskqPubForWsService = applicationContext.getBean("pskqPubForWsService", PskqPubForWsService.class);
			pskqPubForWsService.lisReturn(cnOrder, labApply, cnLabApply, user);
            return 1;
        }
    }

    /**
     * 样本检测
     */
    public static class SampleTestingStrategy implements Strategy {

        @Override
        public int doOperate(LabApply labApply,CnOrder cnOrder,CnLabApply cnLabApply)  throws Exception {
            //
            return 1;
        }
    }

    /**
     * 检验报告
     */
    public static class InspectionReportStrategy implements Strategy {

        @Override
        public int doOperate(LabApply labApply,CnOrder cnOrder,CnLabApply cnLabApply) throws Exception  {
            if (CommonUtils.isEmptyString(labApply.getExecOperaId())) {
            	throw new BusException("报告执行人不能为空");
            }
            if (CommonUtils.isEmptyString(labApply.getExecDateTime())) {
            	throw new BusException("报告时间不能为空");
            }
            if(!"3".equals(cnLabApply.getEuStatus())){
            	 throw new BusException("当前申请单状态为非接收状态，不允许接收");
            }
            User user = LbSelfUtil.getDefaultUser(labApply.getExecOperaId());
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
			PskqPubForWsService pskqPubForWsService = applicationContext.getBean("pskqPubForWsService", PskqPubForWsService.class);
			pskqPubForWsService.lisReport(cnOrder, labApply, cnLabApply, user);
            return 1;
        }
    }

    public static class StrategyFactory {
        public static final String BARCODE_PRINT_EVENT = "E006301";
        public static final String CANCEL_PRINT_EVENT = "E006302";
        public static final String SAMPLE_COLLECTION_EVENT = "E006303";
        public static final String SAMPLE_SUBMISSION_EVENT = "E006304";
        public static final String SAMPLE_DELIVERY_EVENT = "E006305";
        public static final String SAMPLE_RECEPTION_EVENT = "E006306";
        public static final String SAMPLE_RETURN_EVENT = "E006307";
        public static final String SAMPLE_TESTING_EVENT = "E006308";
        public static final String INSPECTION_REPORT_EVENT = "E006309";

        private static Map<String, Strategy> strategyMap = new HashMap<>();

        static {
            strategyMap.put(BARCODE_PRINT_EVENT, new BarcodePrintStrategy());
            strategyMap.put(CANCEL_PRINT_EVENT, new CancelPrintStrategy());
            strategyMap.put(SAMPLE_COLLECTION_EVENT, new SampleCollectionStrategy());
            strategyMap.put(SAMPLE_SUBMISSION_EVENT, new SampleSubmissionStrategy());
            strategyMap.put(SAMPLE_DELIVERY_EVENT, new SampleDeliveryStrategy());
            strategyMap.put(SAMPLE_RECEPTION_EVENT, new SampleReceptionStrategy());
            strategyMap.put(SAMPLE_RETURN_EVENT, new SampleReturnStrategy());
            strategyMap.put(SAMPLE_TESTING_EVENT, new SampleTestingStrategy());
            strategyMap.put(INSPECTION_REPORT_EVENT, new InspectionReportStrategy());
        }

        public static Strategy creator(String event) {
            return strategyMap.get(event);
        }


    }

}

