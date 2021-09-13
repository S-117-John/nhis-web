package com.zebone.nhis.ma.pub.zsrm.service;

import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.zsrm.dao.ZsrmOpTransInHospitalMapper;
import com.zebone.nhis.ma.pub.zsrm.vo.ZsrmOpTransInHospitalVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 中山人民医院门急诊转入院
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ZsrmOpTransInHospitalHandler {

    @Resource
    private ZsrmOpTransInHospitalMapper opTransInHospitalMapper;

    @Resource
    private ZsrmOpTransInHospitalService opTransInHospitalService;

    public Object invokeMethod(String method,Object... params){
        Object obj=new Object();
        if (!"true".equals(ApplicationUtils.getPropertyValue("ext.oldhis.pinotice.enable", "")))
            return obj;
        switch (method){
            case "ipNotice":
               saveOpTransInhosptial(params);
                break;
            default:
                break;
        }
        return obj;
    }
    /**
     * 门急诊转入院
     * @param notice
     */
    public void saveOpTransInhosptial(Object... params) {
        try {
            if (params == null) return;
            PvIpNotice notice = (PvIpNotice) params[0];
            if (notice == null) return;

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("pkPv", notice.getPkPvOp());

            List<ZsrmOpTransInHospitalVo> transInHospitalVoList = opTransInHospitalMapper.getOpTransInHospInfo(paramMap);
            if (transInHospitalVoList == null || transInHospitalVoList.size() == 0) return;

            ZsrmOpTransInHospitalVo inHospitalVo = transInHospitalVoList.get(0);
            Date dateAp = notice.getDateAdmit();
            Date dateNow = DateUtils.strToDate(DateUtils.getDate("yyyy-MM-dd"), "yyyy-MM-dd");
            if (dateAp.compareTo(dateNow) > 0) {
                inHospitalVo.setApplyAdmissionType("2");
            } else {
                inHospitalVo.setApplyAdmissionType("1");
            }
            inHospitalVo.setApplyAdmissionTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", dateAp));
            inHospitalVo.setHospitalId("H0010");
            if (CommonUtils.isNull(inHospitalVo.getHisInPatientOn())) {
                inHospitalVo.setHisInPatientOn(inHospitalVo.getHisOutPatientId());
            }
            User user = UserContext.getUser();
            Map<String, Object> deptMap = getDept(user.getPkDept());
            inHospitalVo.setApplyDepartCode(MapUtils.getString(deptMap, "codeDept"));
            inHospitalVo.setApplyDepartName(MapUtils.getString(deptMap, "nameDept"));
            inHospitalVo.setApplyDoctorCode(user.getCodeEmp());
            inHospitalVo.setApplyDoctorName(user.getNameEmp());
            String now = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            inHospitalVo.setApplyTime(now);
            inHospitalVo.setTs(now);
            inHospitalVo.setRemark(notice.getNote());
            inHospitalVo.setRequestBedNum(notice.getBedNo());
            Map<String, Object> reqDept = getDept(notice.getPkDeptIp());
            inHospitalVo.setRequestDepartCode(MapUtils.getString(reqDept, "codeDept"));
            inHospitalVo.setRequestDepartName(MapUtils.getString(reqDept, "nameDept"));
            Map<String, Object> nsDept = getDept(notice.getPkDeptNsIp());
            inHospitalVo.setDepartCode(MapUtils.getString(nsDept, "codeDept"));
            inHospitalVo.setDepartName(MapUtils.getString(reqDept, "nameDept"));
            if (CommonUtils.isNull(inHospitalVo.getDiagnosisCode())) {
                List<Map<String, Object>> resList = opTransInHospitalMapper.getDiagInfo(notice.getPkDiagMaj());
                if (resList != null && resList.size() > 0) {
                    inHospitalVo.setDiagnosisCode(MapUtils.getString(resList.get(0), "diagcode"));
                    inHospitalVo.setDiagnosisName(MapUtils.getString(resList.get(0), "diagname"));
                }
            }
            String[] properStrs = new String[]
                    {
                            "applyAdmissionTime",
                            "applyAdmissionType",
                            "hisOrderId",
                            "hospitalId",
                            "patientLevelValue",
                            "patientName",
                            "patientTypeValue",
                            "processMethod",
                            "requestDepartCode",
                            "requestDepartName",
                            "departCode",
                            "departName",
                            "lendFlag",
                            "hisInPatientOn"
                    };
            String errmsg = validateProperty(inHospitalVo, properStrs);
            if (CommonUtils.isNotNull(errmsg)) {
                throw new BusException("发送入院申请单失败，请联系管理员对照数据！");
            }
            DataSourceRoute.putAppId("old_his");
            String all = DataSourceRoute.getAppId();
            opTransInHospitalService.saveOldHisinHospital(inHospitalVo);
        } catch (Exception e) {
            throw new BusException(e.getMessage());
        } finally {
            DataSourceRoute.putAppId("default");
        }


    }

    /**
     *
     * @param pkDept
     * @return
     */
    private Map<String,Object> getDept(String pkDept){
        String deptSql = "select old_code code_dept,name_dept from bd_ou_dept where pk_dept=?";
        Map<String, Object> deptMap = DataBaseHelper.queryForMap(deptSql, new Object[]{pkDept});
        return deptMap;
    }

    /**
     * 校验实体属性值是否为空
     * @param validateObj
     * @param ignoreProperties
     * @return
     */
    private static String validateProperty(Object validateObj,String... ignoreProperties) {
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(validateObj.getClass());
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
        String errMsg="";
        for (PropertyDescriptor targetPd : targetPds) {
            Method readMethod = targetPd.getReadMethod();
            if (readMethod != null && (ignoreList != null && ignoreList.contains(targetPd.getName()))) {
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(validateObj);
                    if (value instanceof String) {
                        if (StringUtils.isEmpty((String) value)) {
                            errMsg+="数据表【zy_SyncMzPatInfo】"+ "中的" + targetPd.getName() + "不能为空\n";
                            continue;
                        }
                    }
                    String proName=targetPd.getName();
                    if (value == null && ignoreList.contains(proName)) {
                        errMsg += "数据表【zy_SyncMzPatInfo】" + "中的" + targetPd.getName() + "不能为空\n";
                    }
                } catch (Exception ex) {
                    throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                }
            }
        }
        return errMsg;
    }
}
