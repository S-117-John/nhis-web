package com.zebone.nhis.ma.pub.platform.send.impl.yh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.ma.pub.platform.send.PlatFormSendHandlerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.mq.msg.service.EwellMqHelper;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.ma.pub.platform.send.IPlatFormSendHandler;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.dao.SendMsgMapper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 发送ADT领域消息
 */
@Service("YHPlantFormSendService")
public class YHPlatFormSendHandler extends PlatFormSendHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(YHPlatFormSendHandler.class);
    @Resource
    private YHPlatFormAdtHandler yhPlatFormAdtHandler;

    @Resource
    private EwellMqHelper ewellMqHelper;

    @Resource
    private SendMsgMapper sendMsgMapper;

    /**
     * 患者转科
     *
     * @param param
     */
    @Override
    public void sendDeptInMsg(Map<String, Object> param) {

        try {
            yhPlatFormAdtHandler.sendDeptInMsg(param);
            logger.info("success");
        } catch (Exception e) {
            //记录发送消息失败日志
            logger.error("出现了异常", e);
        }
    }

    /**
     * 患者出院
     *
     * @param param
     */
    @Override
    public void sendPvOutMsg(Map<String, Object> param) {

        try {
            yhPlatFormAdtHandler.sendPvOutMsg(param);
            logger.info("success");
        } catch (Exception e) {
            //记录发送消息失败日志
            logger.error("出现了异常", e);
        }

    }

    /**
     * 医嘱作废
     *
     * @param param
     */
    public void sendCancleOrder(Map<String, Object> param) {
        try {
            yhPlatFormAdtHandler.sendCancleOrder(param);
            logger.info("success");
        } catch (Exception e) {
            //记录发送消息失败日志
            logger.error("出现了异常", e);
        }
    }

    /**
     * 发送检查检验申请单信息
     */
    @Override
    public void sendCnMedApplyMsg(Map<String, Object> param) throws BusException {

    }

    /**
     * 发送检查检验费用信息
     */
    public void sendCnMedApplyBlMsg(Map<String, Object> param)
            throws BusException {
        // TODO Auto-generated method stub
    }

    @Override
    public void sendBedChange(Map<String, Object> paramMap) throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdDefDocMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdTermDiagMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdTermFreqMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdSupplyMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdResBedMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdOrdMsg(Map<String, Object> paramMap) throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdItemMsg(Map<String, Object> paramMap) throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdItemSetMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdOuDeptMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdOuEmpMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdOuUserMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBedChangeMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBedPackMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBedRtnPackMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendCancelDeptInMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendDoctorChangeMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendPiMasterMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendPvInfoMsg(Map<String, Object> paramMap) throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendPvInMsg(Map<String, Object> paramMap) throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendPvCancelInMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendPvCancelOutMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendPvOpRegMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendPvOpCancelRegMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlCancelSettleMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlSettleMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlOpSettleMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlOpCancelSettleMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlMedApplyMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendCnDiagMsg(Map<String, Object> paramMap) throws BusException {
        // TODO Auto-generated method stub

    }


    @Override
    public void sendCnPresOpMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendExConfirmMsg(Map<String, Object> paramMap)
            throws BusException {
        // TODO Auto-generated method stub

    }

    /**
     * 医嘱核对时触发
     *
     * @param paramMap{ordlist:List<Map<String,Object>>(OrderCheckVo)}
     * @throws BusException
     */
    @Override
    public void sendExOrderCheckMsg(Map<String, Object> paramMap) throws BusException {
//        List<Map<String, Object>> checkList = (List<Map<String, Object>>) paramMap.get("ordlistvo");
        List<CnOrder> checkList= (List<CnOrder>)paramMap.get("ordlistvo");
        List<String> orderList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        for (CnOrder vo : checkList) {
            orderList.add( vo.getPkCnord());
        }
        map.put("orderList", orderList);
        try {
            //发送检查检验申请单信息
            yhPlatFormAdtHandler.sendCnLisOrrRisApplyMsg(map);
            //费用信息发送
            yhPlatFormAdtHandler.sendCnMedApplyBlMsg(map);
            logger.info("success");
        } catch (Exception e) {
            //记录发送消息失败日志
            logger.error("出现了异常", e);
        }

    }

    @Override
    public void sendBdPdMsg(Map<String, Object> paramMap) {
        // TODO Auto-generated method stub


    }

    @Override
    public void sendEmrMsg(Map<String, Object> paramMap) {
        //todo
    }

    @Override
    public void sendBdFactoryMsg(Map<String, Object> paramMap) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBdOuOrgMsg(Map<String, Object> paramMap) {

    }

    @Override
    public void sendMsgLisRis(Map<String, Object> paramMap) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendSchInfo(Map<String, Object> paramMap) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendSchAppt(Map<String, Object> paramMap) {
        // TODO Auto-generated method stub
    }

    /**
     * 临床数据提取
     *
     * @param paramMap
     * @return
     */
    @Override
    public List<Map<String, Object>> getEMRClinicalData(Map<String, Object> paramMap) {
        String qryType = (String) paramMap.get("qryType");
        String smpNo = (String) paramMap.get("smpNo");
        List<Map<String, Object>> listmap = new ArrayList<>();
        //检验
        if ("lis".equals(qryType)) {
            //检验明细
            if (StringUtils.isNotBlank(smpNo)) {
                List<Map<String, Object>> emrLisResults = yhPlatFormAdtHandler.queryPatLisResult(paramMap);
                listmap.addAll(emrLisResults);

            } else {
                List<Map<String, Object>> emrLisResults = yhPlatFormAdtHandler.queryPatLisResult(paramMap);
                listmap.addAll(emrLisResults);
                }

            }
            if ("ris".equals(qryType)) {
                //检查
                List<Map<String, Object>> emrRisResults = yhPlatFormAdtHandler.queryPatRisResult(paramMap);

                    listmap.addAll(emrRisResults);
                }



        return listmap;
    }

    @Override
    public void sendScmIpDeDrug(Map<String, Object> paramMap) {
        // TODO Auto-generated method stub
        try {
            yhPlatFormAdtHandler.sendDrugMsg(paramMap);
            logger.info("success");
        } catch (Exception e) {
            //记录发送消息失败日志
            logger.error("出现了异常", e);
        }
    }

	@Override
	public void sendPvOpNoMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public String sendCnOpCall(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void sendReceiptMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendDepositMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOpApplyMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendRegLevelMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendPactMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOpConfirmMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCallPayMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendQueryIpMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendQueryPiMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendBlWeiXinSQMZQ1Msg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBlWeiXinQBPZZLMsgDetails(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBlWeiXinQBPZZLMsgTheme(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendTotalExpensesMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendFeedbackDepositMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendOpCompleteMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendDepositStatusMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendAddExOrderOccMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendDelExOrderOccMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendUpDateExOrderOccMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendBedCgMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendAllExOrderOccMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

    /**
     * 发送费用短信提醒信息
     * zsrm-门诊欠费短信提醒
     * @param paramMap
     */
    @Override
    public void sendOpFeeReminderMsg(Map<String, Object> paramMap) {

    }

	@Override
	public void sendRelationshipMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOperaOrderCancelMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCancelSignPresMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendSignPresMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCancelHerbOrderMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendUpdateSignMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendSignHerbOrder2Msg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCancleLisApplyListMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCancleRisApplyListMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendSaveDeptBuAndBusesMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendHighValueConSumIp(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendHighValueConSumIpBack(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

    @Override
    public void sendBdMaterMsg(Map<String, Object> paramMap) {

    }

    @Override
	public void sendOpArriveMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOpToIpMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendFinishClinicMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCancelClinicMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendUpPiInfoMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendConsultMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendConsultResponeMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCnOpAppMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOpO09Msg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendDeptChangeMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBdDeptUnitMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void sendShortMsgPhoneChk(Map<String, Object> paramMap) {

    }

    @Override
    public void sendDstributeCardMsg(Map<String, Object> paramMap) {

    }

}
