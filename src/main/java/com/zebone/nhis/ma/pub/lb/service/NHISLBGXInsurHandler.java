package com.zebone.nhis.ma.pub.lb.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.ma.pub.lb.dao.LbGXarchMapper;
import com.zebone.nhis.ma.pub.lb.vo.archInfoVo.*;
import com.zebone.nhis.pro.zsba.common.support.MD5Util;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 灵璧国新web服务实现接口
 */
@Component
public class NHISLBGXInsurHandler{
    private static final Logger logger = LoggerFactory.getLogger("nhis.lbWebServiceLog");
    @Autowired
    private LbGXarchMapper lbGXarchMapper;

    @Value("#{applicationProperties['ext.ArchInfo.token']}")
    private  String TOKEN;
    @Value("#{applicationProperties['ext.ArchInfo.userId']}")
    private  String USERID;
    @Value("#{applicationProperties['ext.ArchInfo.URL']}")
    private  String URL;


    public Object invokeMethod(String methodName, Object[] args) {
        Object result;
        try {
            Method method = ReflectUtils.findDeclaredMethod(this.getClass(), methodName, new Class[]{args==null?Object[].class:args.getClass()});
            result = method.invoke(this, new Object[]{args});
        } catch (Exception e) {
            logger.error("中公网中心服务-其他异常：",e);
            throw new BusException("其他异常："+ ExceptionUtils.getRootCauseMessage(e));
        }
        return result;
    }
    //~~~~~~~~~~~~~~对接接口开始~~~~~~~~~~~~~~~~

    /**
     * 查询病案上传信息
     * @param args
     * @return
     */
    public  List<PiArchInfoVo> qryPiArchInfo(Object[] args){
        List<PiArchInfoVo> piArchInfoVos = null;
        Map<String,Object> param=new HashMap<String,Object>();
        QryVo qryVo = (QryVo) args[0];
        param =JSON.parseObject(JSON.toJSONString(qryVo), Map.class);
        String dateSt = MapUtils.getString(param, "dateSt");
        String dateEnd = MapUtils.getString(param, "dateEnd");
        if (StringUtils.isBlank(dateSt) && StringUtils.isBlank(dateEnd)){
            throw new BusException("请输入时间范围");
        }
        if (param.get("flagUpload")==null || param.get("flagUpload").equals("CIS_R_00") || param.get("flagUpload").equals("")){
            //已上传或全部
            piArchInfoVos = lbGXarchMapper.qryUpLoadByDate(param);
            return piArchInfoVos;
        }
        //未上传
        piArchInfoVos = lbGXarchMapper.qryNoUpLoadByDate(param);
        return piArchInfoVos;
    }


    /**
     * 批量上传
     * @param args
     */
    public Map<String, Object> batchUpload(Object[] args){
        //TODO 1.解析入参
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("dateSt",args[0]);
        param.put("dateEnd",args[1]);
        String dateSt = MapUtils.getString(param, "dateSt");
        String dateEnd = MapUtils.getString(param, "dateEnd");
        if (StringUtils.isBlank(dateSt) && StringUtils.isBlank(dateEnd)){
            throw new BusException("请输入时间范围");
        }
        List<PiArchInfoVo> piArchInfoVoList = lbGXarchMapper.qryArchInfo(param);
        Map<String,Object> resMap = new HashMap<>();
        if (piArchInfoVoList != null && piArchInfoVoList.size()>0){
            for (PiArchInfoVo piArchInfoVo : piArchInfoVoList) {
                resMap.put(piArchInfoVo.getXm(),uploadArchInfo(piArchInfoVo.getPkpv()));
            }
        }
//        Map<String, Object> resMap = (Map<String, Object>) JSONObject.parse(resp);
        return resMap;
    }

    /**
     * 单个上传
     * @param args
     */
    public Map<String, Object> oneUpload(Object[] args){
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("pkpv",args[0]);
        String pkpv = MapUtils.getString(param, "pkpv");
        return uploadArchInfo(pkpv);
    }

    /**
     * 病案上传
     * @param pkpv
     * @return
     */
    public Map<String, Object> uploadArchInfo(String pkpv) {

        if (StringUtils.isBlank(pkpv)){
            throw new BusException("就诊号为空");
        }
        //TODO 1.组装数据
        //病案主页数据组装
        Medical medical = lbGXarchMapper.qryArchMainInfo(pkpv);
        if (medical == null){
            throw new BusException("未查询到病案信息");
        }
        //病案附页
        MedicalAttach medicalAttach = lbGXarchMapper.qryArchAttach(pkpv);
        //暂时给null
        LeaveHospital leaveHospital = new LeaveHospital();
        List<ListCheck> listChecks = new ArrayList<>();
//        ListCheck listCheck = new ListCheck();
//        listChecks.add(listCheck);
        List<ListICU> listICUs = new ArrayList<ListICU>();
        //手术
        List<ListOperation> listOperations = new ArrayList<>();
        listOperations = lbGXarchMapper.qryListOperation(pkpv);
        //手术明细
        Map<String,Object> map = new HashMap<>();
        map.put("pkpv",pkpv);
        if (listOperations != null && listOperations.size()>0){
            for (ListOperation listOperation : listOperations) {
                if (listOperation.getOperationRecordNo() != null && !listOperation.getOperationRecordNo().isEmpty()){
                    map.put("operationRecordNo",listOperation.getOperationRecordNo());
                }
                List<ListOperationDetail> listOperationDetails = lbGXarchMapper.qryListOpeDetail(map);
                listOperation.setListOperationDetail(listOperationDetails);
            }
        }

        //入参data组装
        ArchInfoDataVo archInfoDataVo = new ArchInfoDataVo();
        archInfoDataVo.setMedical(medical);
        archInfoDataVo.setMedicalAttach(medicalAttach);
        archInfoDataVo.setLeaveHospital(leaveHospital);
        archInfoDataVo.setListCheck(listChecks);
        archInfoDataVo.setListOperation(listOperations);
        archInfoDataVo.setListICU(listICUs);

        //接口入参组装
        LbGXArchSendMsgVo archSendMsg = new LbGXArchSendMsgVo();
        archSendMsg.setServiceId("5001");
        archSendMsg.setUserId(USERID);
        archSendMsg.setNonce("");
        //转JSON 过滤转译,空值-->"", ""-->''
        String tmp = StringEscapeUtils.unescapeJavaScript(JSONObject.toJSONString(archInfoDataVo, (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        })).replace("\"","\'");
        logger.info(StringEscapeUtils.unescapeJavaScript(JSONObject.toJSONString(archInfoDataVo, (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        })));
        archSendMsg.setData(tmp);
        archSendMsg.setSiginMethod("1");
        String strText = TOKEN + USERID;
        archSendMsg.setSignData(MD5Util.getMD5Code(strText).toUpperCase());
        //TODO 2.发送数据
        String sendMsg = StringEscapeUtils.unescapeJavaScript(JSONObject.toJSONString(archSendMsg));
        logger.info(sendMsg);

        String resp = HttpClientUtil.sendHttpPostJson(URL, sendMsg);
        logger.info(resp);
        Map<String, Object> respMap = (Map<String, Object>) JSONObject.parse(resp);
        if (MapUtils.getString(respMap,"Code").equals("CIS_R_00"))
        {
            DataBaseHelper.execute("update ins_szyb_js set qdidgx = 'CIS_R_00' where PK_PV = ?",pkpv);
        }
        //返回信息
        return respMap;
    }
    /**
     * 作废病案
     * @param args
     */
    public Map<String,Object> invalid(Object[] args){
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("pkpv",args[0]);
        String pkpv = MapUtils.getString(param, "pkpv");
        if (StringUtils.isBlank(pkpv)){
            throw new BusException("就诊号为空");
        }
        Medical medical = lbGXarchMapper.qryArchMainInfo(pkpv);
        if (medical == null) throw new BusException("未查询到病案信息");
        if (medical.getAdmissionNo() == null || "".equals(medical.getAdmissionNo()))
            throw new BusException("住院号为空");
        if (medical.getHospitalId() == null || "".equals(medical.getHospitalId()))
            throw new BusException("机构号为空");
        param.put("AdmissionNo",medical.getAdmissionNo());
        param.put("HospitalId",medical.getHospitalId());
        //接口入参组装
        LbGXArchSendMsgVo archSendMsg = new LbGXArchSendMsgVo();
        archSendMsg.setServiceId("5003");
        archSendMsg.setUserId(USERID);
        archSendMsg.setNonce("");
        //转JSON 过滤转译,空值-->"", ""-->''
        String tmp = JSONObject.toJSONString(param).replace("\"","\'");
        archSendMsg.setData(tmp);
        archSendMsg.setSiginMethod("1");
        String strText = TOKEN + USERID;
        archSendMsg.setSignData(MD5Util.getMD5Code(strText).toUpperCase());
        //TODO 2.发送数据
        String sendMsg = StringEscapeUtils.unescapeJavaScript(JSONObject.toJSONString(archSendMsg));
        logger.info(sendMsg);

        String resp = HttpClientUtil.sendHttpPostJson(URL, sendMsg);
        logger.info(resp);
        Map<String, Object> respMap = (Map<String, Object>) JSONObject.parse(resp);
        if (MapUtils.getString(respMap,"Code").equals("CIS_R_00"))
        {
            DataBaseHelper.execute("update ins_szyb_js set qdidgx = null where PK_PV = ?",pkpv);
        }
        //返回信息
        return respMap;
    }
    //查询
    public List<LbGXArchSendMsgVo> queryArchInfo(Object[] args) {
        return null;
    }

}
