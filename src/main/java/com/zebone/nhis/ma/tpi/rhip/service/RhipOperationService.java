package com.zebone.nhis.ma.tpi.rhip.service;

import com.zebone.nhis.common.module.ma.tpi.rhip.PacsRptInfo;
import com.zebone.nhis.ma.tpi.rhip.dao.LisResultInfoMapper;
import com.zebone.nhis.ma.tpi.rhip.dao.OperationInfoMapper;
import com.zebone.nhis.ma.tpi.rhip.support.RpDataUtils;
import com.zebone.nhis.ma.tpi.rhip.support.RpWsUtils;
import com.zebone.nhis.ma.tpi.rhip.support.XmlGenUtils;
import com.zebone.nhis.ma.tpi.rhip.vo.OperationRecordInfo;
import com.zebone.nhis.ma.tpi.rhip.vo.PatListVo;
import com.zebone.nhis.ma.tpi.rhip.vo.PtExamReport;
import com.zebone.nhis.ma.tpi.rhip.vo.PtOperation;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@Service
public class RhipOperationService {
    @Resource
    private OperationInfoMapper operationRecord;

    /**
     * 手术记录数据上传
     * @param param
     * @param user
     * @return
     * @throws Exception
     */
    public String rpDataTrans(String param , IUser user, PatListVo pat) throws Exception{
        Map map = JsonUtil.readValue(param,Map.class);
        String pkPv = (String) map.get("pkPv");
        String codePi=pat.getCodePi();
        String ipTimes = pat.getIpTimes().toString();
//        String ipTimes = "1";
//        String codePi = "000768210100";
        if (codePi == null ||ipTimes == null) {
            return "codePi或者住院次不能为空";
        }
        map.put("codePi",codePi);//
        map.put("ipTimes",ipTimes);//
        //手术记录(Pt_Operation)
        List<OperationRecordInfo> list = operationRecord.OperationRecordInfo(map);
        if(list!=null&&list.size()>0){
            for (OperationRecordInfo rpt : list) {
                PtOperation ptOperationReport = RpDataUtils.createPtOperationReport(user, pat, rpt);
                String xml= XmlGenUtils.create(user, pat, ptOperationReport);
                String rtnStr= RpWsUtils.invokeWS(xml);
                if(!CommonUtils.isEmpty(rtnStr)){
                    return "手术记录:"+rtnStr;
                }else{
                    if(rtnStr.indexOf("error")>=0){
                        throw new BusException(rtnStr);
                    }
                }
            }
        }
        return "";
    }
}
