package com.zebone.nhis.compay.pub.service;

import com.google.common.collect.Maps;
import com.zebone.nhis.compay.pub.support.NationalTool;
import com.zebone.nhis.compay.pub.vo.HisInsuResVo;
import com.zebone.nhis.compay.pub.vo.InsChkAccSumVo;
import com.zebone.nhis.compay.pub.vo.InsFileResponse;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 全国医保-对账-对接外接接口部分
 */
@Service
public class NationalInsuranceChkHandler {
    private static final Logger logger = LoggerFactory.getLogger("nhis.ZsrmQGLog");

    @Resource
    private NationalInsuranceService insuranceService;


    /**
     * 【3201】医药机构费用结算对总账
     * @param insChkAccSumVo
     * @return {setlOptins:结算经办机构,stmtRslt:对账结果,stmtRsltDscr:对账结果说明}
     */
    public Map<String,Object> checkAccSum(InsChkAccSumVo insChkAccSumVo){
        Map<String, Object> insurMap = new HashMap<>();
        insurMap.put("insutype", insChkAccSumVo.getInsuType());//险种
        insurMap.put("clr_type", insChkAccSumVo.getClrType());//清算类别
        insurMap.put("setl_optins","442000");//结算经办机构
        insurMap.put("stmt_begndate",insChkAccSumVo.getDateBegin());//对账开始日期
        insurMap.put("stmt_enddate",insChkAccSumVo.getDateEnd());//对账结束日期
        insurMap.put("medfee_sumamt",insChkAccSumVo.getAmountSt());//医疗费总额
        insurMap.put("fund_pay_sumamt",insChkAccSumVo.getAmountInsu());//基金支付总额
        insurMap.put("acct_pay",insChkAccSumVo.getAmountPi());//个人账户支付金额
        insurMap.put("fixmedins_setl_cnt", insChkAccSumVo.getDataNum());//定点医药机构结算笔数
        Map<String, Object> inputParam = Maps.newHashMap();
        inputParam.put("data",insurMap);
        HisInsuResVo insuResVo = insuranceService.sendHttpPost("3201",inputParam);
        Map<String, Object> dynaBean = JsonUtil.readValue((String) insuResVo.getResVo().getOutput(),Map.class);
        Map stmtinfo = MapUtils.getMap(dynaBean, "stmtinfo");
        stmtinfo.put("stmtRsltDes", NationalTool.getRsltDes(MapUtils.getString(stmtinfo,"stmtRslt")));
        return stmtinfo;
    }

    /**
     * 【3202】医药机构费用结算对明细账---总账对不平时，进行对明细账
     *
     * @return <pre>
     * fileQuryNo	文件查询号--用于下载明细对账结果文件
     * filename	文件名称
     * dldEndtime	下载截止时间--yyyy-MM-dd HH:mm:ss
     * </pre>
     */
    public InsFileResponse checkAccDetail(InsChkAccSumVo sumVo,String fileNo) {
        Map<String, Object> insurMap = new HashMap<>();
        insurMap.put("setl_optins","442000");
        insurMap.put("file_qury_no",fileNo);
        insurMap.put("stmt_begndate",sumVo.getDateBegin());
        insurMap.put("stmt_enddate",sumVo.getDateEnd());
        insurMap.put("medfee_sumamt",sumVo.getAmountSt());
        insurMap.put("fund_pay_sumamt",sumVo.getAmountInsu());
        insurMap.put("cash_payamt", sumVo.getAmountCash());
        insurMap.put("fixmedins_setl_cnt", sumVo.getDataNum());
        insurMap.put("clr_type", sumVo.getClrType());
        Map<String, Object> inputParam = Maps.newHashMap();
        inputParam.put("data",insurMap);
        HisInsuResVo insuResVo = insuranceService.sendHttpPost("3202",inputParam);
        Map<String,Object> map = JsonUtil.readValue((String) insuResVo.getResVo().getOutput(),Map.class);
        map = MapUtils.getMap(map,"fileinfo");
        map.put("stmtRsltDes", NationalTool.getRsltDes(MapUtils.getString(map,"stmtRslt")));
        InsFileResponse insFileResponse = new InsFileResponse();
        try {
            BeanUtils.copyProperties(insFileResponse,map);
        } catch (Exception e) {
            logger.error("属性赋值异常:", e);
            throw new BusException("属性赋值异常");
        }
        return insFileResponse ;
    }

    /***
     * 冲正交易
     * @param psnNo 人员编号
     * @param omsgid 原发送方报文ID
     * @param oinfno 原交易编号
     *               <pre>
     *               可被冲正的交易包括：【2102】药店结算、【2103】药店结算撤销、【2207】门诊结算、【2208】门诊结算撤销、【2304】住院结算、【2207】住院结算撤销、【2401】入院办理。
     *               </pre>
     */
    public void reverse(String psnNo,String omsgid,String oinfno){
        Map<String, Object> insurMap = Maps.newHashMap();
        insurMap.put("psn_no",psnNo);
        insurMap.put("omsgid",omsgid);
        insurMap.put("oinfno",oinfno);
        Map<String, Object> inputParam = Maps.newHashMap();
        inputParam.put("data",insurMap);
        insuranceService.sendHttpPost("2601",inputParam);
    }

}
