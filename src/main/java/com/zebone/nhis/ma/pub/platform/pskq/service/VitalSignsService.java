package com.zebone.nhis.ma.pub.platform.pskq.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOccDt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.ma.pub.platform.pskq.model.VitalSigns;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 卡卡西
 */
@Service
public class VitalSignsService {

    private static Map<String,Object> fieldMap = new HashMap<>(16);

    static {
        fieldMap.put("euStooltype","排便类型");
        fieldMap.put("valStool","大便次数");
        fieldMap.put("valStoolColo","灌肠后大便次数");
        fieldMap.put("valUrine","小便排出量");
        fieldMap.put("hourUrine","小便排出量小时数");
        fieldMap.put("valGast","胃液排出量");
        fieldMap.put("valGall","胆汁排出量");
        fieldMap.put("valDrainage","引流排出量");
        fieldMap.put("valInput","液体入量");
        fieldMap.put("hourInput","液体入量小时数");
        fieldMap.put("valWeight","体重");
        fieldMap.put("valSbp","收缩压(上午)");
        fieldMap.put("valDbp","舒张压(上午)");
        fieldMap.put("valSbpAdd","收缩压(下午)");
        fieldMap.put("valDbpAdd","舒张压(下午)");
        fieldMap.put("valAl","药物过敏");
        fieldMap.put("valSt","皮试");
        fieldMap.put("flagUroc","尿失禁");
        fieldMap.put("euTemptype","体温类型");
        fieldMap.put("valTemp","体温数值");
        fieldMap.put("valDrop","物理降温");
        fieldMap.put("valPulse","脉搏数值");
        fieldMap.put("euHrtype","心率类型");
        fieldMap.put("valHr","心率数值");
        fieldMap.put("euBrtype","呼吸类型");
        fieldMap.put("valBre","呼吸数值");
        fieldMap.put("valOutputTotal","总出量");
        fieldMap.put("hourOutputTotal","总出量小时");
        fieldMap.put("dtOutputtype","其它出量类型");
        fieldMap.put("valOutput","其它出量ml");
        fieldMap.put("hourOutput","其它出量小时");
        fieldMap.put("valOth","其它");
        fieldMap.put("note","备注");
    }

    public List<String> findVitalSigns(Map<String,Object> param) throws Exception {
        List<String> resultList = new ArrayList<>();
        String pkPv = MapUtils.getString(param,"pkPv");
        String sql = "select * from ex_vts_occ where pk_pv = ? and del_flag = '0' order by CREATE_TIME desc";
        List<ExVtsOcc> exVtsOccList = DataBaseHelper.queryForList(sql,ExVtsOcc.class,pkPv);
        sql = "select * from PV_ENCOUNTER where PK_PV = ?";
        PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,pkPv);
        sql = "select * from PI_MASTER where PK_PI = ?";
        PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,pvEncounter.getPkPi());
        sql = "select * from PV_IP where PK_PV = ?";
        List<PvIp> pvIpList = DataBaseHelper.queryForList(sql,PvIp.class,pkPv);
        PvIp pvIp = pvIpList.stream().findFirst().orElse(null);
        //vitalSigns.setEncounterId(pvEncounter.getCodePv());
        ExVtsOcc exVtsOcc = exVtsOccList.stream().findFirst().orElseThrow(()->new Exception("为查询到体征数据"));
        Field[] fields = exVtsOcc.getClass().getDeclaredFields();

        for (Field field : fields) {
            VitalSigns vitalSigns = new VitalSigns();
            field.setAccessible(true);
            String fieldName = field.getName();
            String typeName = MapUtils.getString(fieldMap,fieldName);
            if(StringUtils.isEmpty(typeName)){
                continue;
            }
            vitalSigns.setSignName(typeName);
            if(field.get(exVtsOcc)==null){
                continue;
            }
            vitalSigns.setMeasureValue(field.get(exVtsOcc)+"");
            String ipTimes = pvIp!=null?pvIp.getIpTimes()+"":"";
            vitalSigns.setVitalSignsId(piMaster.getCodeIp()+"_"+ipTimes+"_"+exVtsOcc.getPkVtsocc());
            vitalSigns.setEncounterId("10_3_2000_"+pvEncounter.getCodePv());
            vitalSigns.setPkPatient(piMaster.getCodePi());
            vitalSigns.setPatientName(piMaster.getNamePi());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //vitalSigns.setVisitDateTime(simpleDateFormat.format(exVtsOcc.getDateVts()));
            //S1802 护理记录信息更新服务
            //E180201 更新生命体征数据
            vitalSigns.setMeasureDateTime(simpleDateFormat.format(exVtsOcc.getDateInput()));
            sql = "select * from BD_OU_EMPLOYEE where PK_EMP = ?";
            if(!StringUtils.isEmpty(exVtsOcc.getPkEmpInput())){
                BdOuEmployee employee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,exVtsOcc.getPkEmpInput());
                if(employee!=null){
                    vitalSigns.setMeasureOperaId(employee.getCodeEmp());
                }

            }
            vitalSigns.setMeasureOperaName(exVtsOcc.getNameEmpInput());
            RequestBody requestBody = new RequestBody.Builder()
                    .service("S2701","护理记录信息新增服务")
                    .event("E270102","新增生命体征数据")
                    .sender()
                    .receiver()
                    .build();
            Map<String,Object> map = new HashMap<>(16);
            List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(vitalSigns);
            map.put("VITAL_SIGNS",dataElementList);
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
            resultList.add(requestString);
        }

        sql = "select * from EX_VTS_OCC_DT where PK_VTSOCC = ? order by CREATE_TIME desc ";
        List<ExVtsOccDt> exVtsOccDtList = DataBaseHelper.queryForList(sql,ExVtsOccDt.class,exVtsOcc.getPkVtsocc());
        for (ExVtsOccDt exVtsOccDt : exVtsOccDtList) {
            //体温类型
            if("0".equals(exVtsOccDt.getEuTemptype())){
                exVtsOccDt.setEuTemptype("口温");
            }else if("2".equals(exVtsOccDt.getEuTemptype())){
                exVtsOccDt.setEuTemptype("腋温");
            }else if("3".equals(exVtsOccDt.getEuTemptype())){
                exVtsOccDt.setEuTemptype("肛温");
            }

            //心率类型
            if("0".equals(exVtsOccDt.getEuHrtype())){
                exVtsOccDt.setEuHrtype("自然");
            }else if("1".equals(exVtsOccDt.getEuHrtype())){
                exVtsOccDt.setEuHrtype("起搏器");
            }

            //呼吸类型
            if("0".equals(exVtsOccDt.getEuBrtype())){
                exVtsOccDt.setEuBrtype("自然");
            }else if("1".equals(exVtsOccDt.getEuBrtype())){
                exVtsOccDt.setEuBrtype("呼吸机");
            }

            Field[] fieldsDt = exVtsOccDt.getClass().getDeclaredFields();
            for (Field field : fieldsDt) {
                VitalSigns vitalSigns = new VitalSigns();
                field.setAccessible(true);
                String fieldName = field.getName();
                String typeName = MapUtils.getString(fieldMap,fieldName);
                if(StringUtils.isEmpty(typeName)){
                    continue;
                }
                vitalSigns.setSignName(typeName);
                if(field.get(exVtsOccDt)==null){
                    continue;
                }
                vitalSigns.setMeasureValue(field.get(exVtsOccDt)+"");
                String ipTimes = pvIp!=null?pvIp.getIpTimes()+"":"";
                vitalSigns.setVitalSignsId(piMaster.getCodeIp()+"_"+ipTimes+"_"+exVtsOccDt.getPkVtsocc());
                vitalSigns.setEncounterId("10_3_2000_"+pvEncounter.getCodePv());
                vitalSigns.setPkPatient(piMaster.getCodePi());
                vitalSigns.setPatientName(piMaster.getNamePi());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //vitalSigns.setVisitDateTime(simpleDateFormat.format(exVtsOcc.getDateVts()));
                //S1802 护理记录信息更新服务
                //E180201 更新生命体征数据
                vitalSigns.setMeasureDateTime(simpleDateFormat.format(exVtsOccDt.getDateVts()));
                sql = "select * from BD_OU_EMPLOYEE where PK_EMP = ?";
                if(!StringUtils.isEmpty(exVtsOcc.getPkEmpInput())){
                    BdOuEmployee employee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,exVtsOcc.getPkEmpInput());
                    if(employee!=null){
                        vitalSigns.setMeasureOperaId(employee.getCodeEmp());
                    }

                }

                vitalSigns.setMeasureOperaName(exVtsOcc.getNameEmpInput());
                RequestBody requestBody = new RequestBody.Builder()
                        .service("S2701","护理记录信息新增服务")
                        .event("E270102","新增生命体征数据")
                        .sender()
                        .receiver()
                        .build();
                Map<String,Object> map = new HashMap<>(16);
                List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(vitalSigns);
                map.put("VITAL_SIGNS",dataElementList);
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
                resultList.add(requestString);
            }
        }

        return resultList;
    }

    public static void main(String[] args) {
        int cap = 16;
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        int r = (n < 0) ? 1 : (n >= 1 << 30) ? 1 << 30 : n + 1;
        System.out.println(r);
    }
}
