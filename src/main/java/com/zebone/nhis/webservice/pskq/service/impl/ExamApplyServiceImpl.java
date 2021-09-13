package com.zebone.nhis.webservice.pskq.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.ExamApply;
import com.zebone.nhis.webservice.pskq.model.LabApply;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.service.ExamApplyService;
import com.zebone.nhis.webservice.pskq.service.LabApplyService;
import com.zebone.nhis.webservice.service.PskqPubForWsService;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 检查申请服务
 */
public class ExamApplyServiceImpl implements ExamApplyService {
	
    @SuppressWarnings("unchecked")
	@Override
    public void editExamApply(String param, ResultListener listener) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
            RequestBody requestBody = gson.fromJson(param, RequestBody.class);
            Map<String, Object> query = requestBody.getMessage();
            List<DataElement> dataElement =new ArrayList<DataElement>();
            if (query.containsKey("EXAM_RESULT") && query.get("EXAM_RESULT") != null) {
                dataElement = (List<DataElement>) query.get("EXAM_RESULT");
            }
            if (query.containsKey("EXAM_APPLY") && query.get("EXAM_APPLY") != null) {
                dataElement = (List<DataElement>) query.get("EXAM_APPLY");
            }
            if(null==dataElement||dataElement.size()==0){
            	listener.error("没有查询到申请单信息");
            }
            ExamApply examApply = (ExamApply) MessageFactory.deserialization(dataElement, new ExamApply());
            if (CommonUtils.isEmptyString(examApply.getExamApplyNo())) {
            	listener.error("申请单号不能为空");
                return;
            }
            CnOrder cnOrder = DataBaseHelper.queryForBean("select * from cn_order where CODE_APPLY = ? and del_flag = '0' and  code_ordtype like '02%' ",
            		CnOrder.class, examApply.getExamApplyNo());
            if (cnOrder == null) {
                listener.error("没有查询到医嘱信息");
                return;
            }
            CnRisApply cnRisApply = DataBaseHelper.queryForBean("select * from cn_ris_apply where PK_CNORD = ? and del_flag = '0'",CnRisApply.class, cnOrder.getPkCnord());
            if (cnRisApply == null) {
                listener.error("没有查询到申请单信息");
                return;
            }
            //更新申请单状态
            StrategyFactory.creator(requestBody.getEvent().getEventCode()).doOperate(examApply,cnOrder,cnRisApply);
            listener.success("消息处理成功");
        } catch (Exception e) {
            listener.exception(e.getMessage());
        }
    }



    public interface Strategy {

        int doOperate(ExamApply examApply,CnOrder cnOrder,CnRisApply cnRisApply) throws Exception;
    }

    /**
     * 回传检查状态
     */
    public static class SampleCollectionStrategy implements Strategy {

        @Override
        public int doOperate(ExamApply examApply,CnOrder cnOrder,CnRisApply cnRisApply) throws Exception{
			PvEncounter pvEncounter = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where pk_pv = ? and del_flag = '0'",PvEncounter.class,cnOrder.getPkPv());
			ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
			PskqPubForWsService pskqPubForWsService = applicationContext.getBean("pskqPubForWsService", PskqPubForWsService.class);
			pskqPubForWsService.risStatus(cnOrder, examApply, cnRisApply);
            return 1;
        }
    }

    /**
     * 检查报告
     */
    public static class InspectionReportStrategy implements Strategy {

        @Override
        public int doOperate(ExamApply examApply,CnOrder cnOrder,CnRisApply cnRisApply) throws Exception  {
            if (CommonUtils.isEmptyString(examApply.getExecOperaId())) {
            	throw new BusException("报告执行人不能为空");
            }
            if (CommonUtils.isEmptyString(examApply.getExecDateTime())) {
            	throw new BusException("报告时间不能为空");
            }
            User user = LbSelfUtil.getDefaultUser(examApply.getExecOperaId());
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
			PskqPubForWsService pskqPubForWsService = applicationContext.getBean("pskqPubForWsService", PskqPubForWsService.class);
			pskqPubForWsService.risReport(cnOrder, examApply, cnRisApply, user);
            return 1;
        }
    }

    public static class StrategyFactory {
        public static final String SAMPLE_COLLECTION_EVENT1 = "E007101";
        public static final String SAMPLE_COLLECTION_EVENT2 = "E007102";
        public static final String SAMPLE_COLLECTION_EVENT3 = "E007103";
        public static final String SAMPLE_COLLECTION_EVENT4 = "E007104";
        public static final String SAMPLE_COLLECTION_EVENT5 = "E007105";
        
        public static final String SAMPLE_STATUS_EVENT = "E006119";

        private static Map<String, Strategy> strategyMap = new HashMap<>();

        static {
            strategyMap.put(SAMPLE_COLLECTION_EVENT1, new InspectionReportStrategy());
            strategyMap.put(SAMPLE_COLLECTION_EVENT2, new InspectionReportStrategy());
            strategyMap.put(SAMPLE_COLLECTION_EVENT3, new InspectionReportStrategy());
            strategyMap.put(SAMPLE_COLLECTION_EVENT4, new InspectionReportStrategy());
            strategyMap.put(SAMPLE_COLLECTION_EVENT5, new InspectionReportStrategy());
            strategyMap.put(SAMPLE_STATUS_EVENT, new SampleCollectionStrategy());
        }

        public static Strategy creator(String event) {
            return strategyMap.get(event);
        }


    }

}

