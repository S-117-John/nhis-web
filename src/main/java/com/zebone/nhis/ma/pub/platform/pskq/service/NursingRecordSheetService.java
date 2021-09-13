package com.zebone.nhis.ma.pub.platform.pskq.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.common.module.nd.record.NdRecordRow;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.ma.pub.platform.pskq.dao.NursingRecordSheetDao;
import com.zebone.nhis.ma.pub.platform.pskq.model.NursingRecordSheet;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 护理记录
 */
@Service
public class NursingRecordSheetService {

    @Autowired
    private NursingRecordSheetDao dao;

    public static Map<String, Object> codeMap = new HashMap<>(16);

    static {
        codeMap.put("体温", "EB0002620");
        codeMap.put("心率", "EB0002401");
        codeMap.put("呼吸频率", "EB0002218");
        codeMap.put("血压", "EB0001201");
        codeMap.put("血氧饱和度", "EB0002665");
        codeMap.put("意识", "EBU001119");
        codeMap.put("入量", "EBU001254");
        codeMap.put("入量值", "field16");
        codeMap.put("出量", "EBU001255");
        codeMap.put("出量值", "field17");
        codeMap.put("伤口敷料", "EBU001136");
        codeMap.put("伤口", "EBU001138");
        codeMap.put("吸氧", "EBU001226");
        codeMap.put("呼吸", "EBU001211");
        codeMap.put("冰敷", "EBU001139");
        codeMap.put("管道", "head_4");
        codeMap.put("特殊形况记录", "EBU001052");

    }

    public String add(Map<String, Object> param) throws Exception {
        HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
        String pkRecord = MapUtils.getString(param, "pkRecord");
        if (StringUtils.isEmpty(pkRecord)) {
            throw new Exception("护理记录单主键为空");
        }
        String pkPv = MapUtils.getString(param,"pkPv");
        if (StringUtils.isEmpty(pkPv)) {
            throw new Exception("就诊主键为空");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "select * from nd_record_row where PK_RECORD = ?";
        List<NdRecordRow> ndRecordRowList = DataBaseHelper.queryForList(sql, NdRecordRow.class, pkRecord);
        for (NdRecordRow ndRecordRow : ndRecordRowList) {
            Optional.ofNullable(ndRecordRow).orElseThrow(() -> new Exception("未查询到护理记录"));
            param.put("pkRow", ndRecordRow.getPkRecordrow());
            List<NdRecordDt> result = dao.findRecord(param);
            NursingRecordSheet nursingRecordSheet = new NursingRecordSheet();
            sql = "select * from PV_ENCOUNTER where PK_PV = ?";
            PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,pkPv);

            Optional.ofNullable(pvEncounter).orElseThrow(()->new Exception("未查询到就诊记录"));
            sql = "select * from PI_MASTER where PK_PI = ?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,pvEncounter.getPkPi());
            nursingRecordSheet.setPkPatient(piMaster.getCodePi());
            nursingRecordSheet.setEncounterId("10_3_2000_"+pvEncounter.getCodePv());
            nursingRecordSheet.setRecordingTime(simpleDateFormat.format(ndRecordRow.getDateSign()));
            nursingRecordSheet.setRecordId(pkRecord);
            sql = "select * from BD_OU_EMPLOYEE where PK_EMP = ?";

            NdRecordDt ndRecordDt = result.stream().findFirst().orElse(null);
            ndRecordDt = Optional.ofNullable(ndRecordDt).orElse(new NdRecordDt());
            if(!StringUtils.isEmpty(ndRecordDt.getCreator())){
                BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,ndRecordDt.getCreator());
                nursingRecordSheet.setRecorder(bdOuEmployee.getNameEmp());
                nursingRecordSheet.setRecordId(bdOuEmployee.getPkEmp());
            }

            nursingRecordSheet.setRowId(ndRecordRow.getPkRecordrow());
            //体温
            nursingRecordSheet.setBodyTemperature(result.stream().filter(a -> MapUtils.getString(codeMap, "体温").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            //脉搏
            nursingRecordSheet.setPulse(result.stream().filter(a -> MapUtils.getString(codeMap, "心率").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            //呼吸
            nursingRecordSheet.setBreathingFrequency(result.stream().filter(a -> MapUtils.getString(codeMap, "呼吸频率").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setSystolicBloodPressure(result.stream().filter(a -> MapUtils.getString(codeMap, "血压").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setBloodOxygen(result.stream().filter(a -> MapUtils.getString(codeMap, "血氧饱和度").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setConsciousness(result.stream().filter(a -> MapUtils.getString(codeMap, "意识").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setInputAmountContent(result.stream().filter(a -> MapUtils.getString(codeMap, "入量").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setInputAmountValue(result.stream().filter(a -> MapUtils.getString(codeMap, "入量值").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setAmount(result.stream().filter(a -> MapUtils.getString(codeMap, "出量").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setOutputAmountValue(result.stream().filter(a -> MapUtils.getString(codeMap, "出量值").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setWound(result.stream().filter(a -> MapUtils.getString(codeMap, "伤口").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setWoundDressing(result.stream().filter(a -> MapUtils.getString(codeMap, "伤口敷料").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setOxygen(result.stream().filter(a -> MapUtils.getString(codeMap, "吸氧").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setBreathing(result.stream().filter(a -> MapUtils.getString(codeMap, "呼吸").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setIceCompress(result.stream().filter(a -> MapUtils.getString(codeMap, "冰敷").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
//            nursingRecordSheet.setIceCompress(result.stream().filter(a -> MapUtils.getString(codeMap, "管道").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            nursingRecordSheet.setRemarks(result.stream().filter(a -> MapUtils.getString(codeMap, "特殊形况记录").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining()));
            RequestBody requestBody = null;
            String status = MapUtils.getString(param,"status");
            if("add".equals(status)){
                requestBody = new RequestBody.Builder().service("S2701","护理记录信息新增服务").event("E270101","新增护理记录").sender().receiver().build();
            }else {
                requestBody = new RequestBody.Builder().service("S2702","护理记录信息更新服务").event("E270201","更新护理记录").sender().receiver().build();
            }
            Map<String,Object> map = new HashMap<>(16);
            List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(nursingRecordSheet);
            map.put("NURSING_RECORD_SHEET",dataElementList);
            requestBody.setMessage(map);
            SenderElement senderElement = new SenderElement(
                    "2000",
                    new SoftwareNameElement("HIS","医院信息管理系统"),
                    new SoftwareProviderElement("Zebone","江苏振邦智慧城市信息系统有限公司"),
                    new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
            );
            ReceiverElement receiverElement = new ReceiverElement(
                    "1200",
                    new SoftwareNameElement("ESB","集成平台"),
                    new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                    new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
            );
            requestBody.setSender(senderElement);
            requestBody.setReceiver(receiverElement);
            String requestString = JSON.toJSONString(requestBody);
            JSONObject jsonObject = JSONObject.parseObject(requestString);
            JSONArray jsonArray = jsonObject.getJSONObject("message").getJSONArray("NURSING_RECORD_SHEET");
            //管道
            String guandao = result.stream().filter(a -> MapUtils.getString(codeMap, "管道").equals(a.getColname())).map(a -> a.getVal()).collect(Collectors.joining());
            JSONObject obj1 = new JSONObject();
            JSONArray jsonArrayVal = new JSONArray();

            String sql2 = "SELECT b.colname,c.rowno,d.val,b.COLNO\n" +
                    "FROM ND_RECORD a\n" +
                    "         INNER JOIN nd_record_dc b ON a.pk_record = b.pk_record and a.pk_record=?\n" +
                    "         inner join nd_record_dt d on d.pk_record=a.pk_record and d.colname=b.colno\n" +
                    "         inner join nd_record_row c on c.pk_recordrow=d.pk_recordrow\n" +
                    " where c.ROWNO = ? " +
                    "order by c.rowno";

            List<Map<String,Object>> resultList = DataBaseHelper.queryForList(sql2,pkRecord,ndRecordRow.getRowno());
            for (Map<String, Object> stringObjectMap : resultList) {
//                if(result.stream().filter(a -> MapUtils.getString(stringObjectMap,"colno").equals(a.getColname())).count()>0){
//
//                }
                JSONObject object = new JSONObject();
                object.put("DATA_ELEMENT_ID","LHDE0055038");
                object.put("DATA_ELEMENT_NAME",MapUtils.getString(stringObjectMap,"colname",""));
                object.put("DATA_ELEMENT_EN_NAME","ZDYZD1");
                object.put("DATA_ELEMENT_VALUE",MapUtils.getString(stringObjectMap,"val",""));
                jsonArrayVal.add(object);

            }




            obj1.put("ZDYL_LIST",jsonArrayVal);
            jsonArray.add(obj1);

            String result1 = JSONObject.toJSONString(jsonObject);

            String responseString = httpRestTemplate.postForString(result1);
            ResponseBody paramVo = JsonUtil.readValue(responseString, ResponseBody.class);
        }

        return "";
    }
}
