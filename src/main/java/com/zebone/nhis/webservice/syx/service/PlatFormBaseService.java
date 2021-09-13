package com.zebone.nhis.webservice.syx.service;

import com.zebone.nhis.common.module.pi.PiAddress;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormBaseMapper;
import com.zebone.nhis.webservice.syx.support.SetUserUtils;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.*;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PlatFormBaseService {

    @Resource
    PlatFormBaseMapper platFormBaseMapper;

    /**
     * 医生信息查询接口
     *
     * @param content
     * @return
     */
    public String getDoctorInfo(String content) throws Exception {

        DoctorInfoReqSubject reqData = (DoctorInfoReqSubject) XmlUtil.XmlToBean(content, DoctorInfoReqSubject.class);
        //res
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (reqData.getSubject() != null && reqData.getSubject().size() > 0) {
            String deptId = reqData.getSubject().get(0).getDeptId();
            if (!"-2".equals(deptId)) {
                paramMap.put("deptId", reqData.getSubject().get(0).getDeptId());
            }
            paramMap.put("doctorId", reqData.getSubject().get(0).getDoctorId());
        }
        List<DoctorInfo> doctorInfoList = platFormBaseMapper.getDoctorInfo(paramMap);

        //subject
        DoctorInfoResSubject doctorInfoResSubject = new DoctorInfoResSubject();
        doctorInfoResSubject.setRes(doctorInfoList);

        //result
        DoctorInfoResResult doctorInfoResResult = new DoctorInfoResResult();
        doctorInfoResResult.setId("AA");
        doctorInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        doctorInfoResResult.setRequestId(reqData.getId());
        doctorInfoResResult.setText("处理成功!");
        doctorInfoResResult.setSubject(doctorInfoResSubject);

        //response
        DoctorlInfoResExd doctorlInfoResExd = new DoctorlInfoResExd();
        doctorlInfoResExd.setResult(doctorInfoResResult);

        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, doctorlInfoResExd), DoctorlInfoResExd.class);
        if(doctorInfoList.size() != 0){
        	return PfWsUtils.setResStatus("0","处理成功！",hospXml);       	
        }else{
        	return PfWsUtils.setResStatus("0","未查询到数据!",hospXml);   
        }     
    }

    /**
     * 医生号源信息查询接口
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String getRegInfo(String content) throws Exception {
        RegInfoReqSubject reqData = (RegInfoReqSubject) XmlUtil.XmlToBean(content, RegInfoReqSubject.class);
        SetUserUtils.setUserByEmpCode(reqData.getSender().getSenderId());
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (reqData.getSubject() != null && reqData.getSubject().size() > 0) {
            paramMap.put("deptId", reqData.getSubject().get(0).getDeptId());
            paramMap.put("doctorId", reqData.getSubject().get(0).getDoctorId());
            paramMap.put("startDate", DateUtils.dateToStr("yyyy-MM-dd", reqData.getSubject().get(0).getStartDate()) + " 00:00:00");
            paramMap.put("endDate", DateUtils.dateToStr("yyyy-MM-dd", reqData.getSubject().get(0).getEndDate()) + " 23:59:59");
            Date startDate = reqData.getSubject().get(0).getStartDate();
            Date endDate = reqData.getSubject().get(0).getEndDate();
            /**
             * 1）如果开始日期=当前日期，结束日期=开始日期+“23:59:59”；
             * 2）如果开始日期>当前日期，如果结束日期>开始日期+7，结束日期=开始日期+7，否则不做处理。
             */
            if (startDate != null && startDate.compareTo(new Date()) == 0) {
                paramMap.put("endDate", startDate);
            }
            if (startDate.compareTo(new Date()) > 0) {
                if (differentDays(startDate, endDate) - 7 > 0) {
                    int dateTime = Integer.parseInt(ApplicationUtils.getSysparam("SCH0008", false, "请维护系统参数SCH0008！"));
                    Date date = plusDay(dateTime - 1, endDate);
                    paramMap.put("endDate", DateUtils.dateToStr("yyyy-MM-dd", date) + " 23:59:59");
                }
    			/*if(startDate != null && endDate != null ){
    				int number = endDate.compareTo(startDate);
    				if(number<1){
    					int dateTime = Integer.parseInt(ApplicationUtils.getSysparam("SCH0008", false, "请维护系统参数SCH0008！"));
    					Date date = plusDay(dateTime,endDate);
    					paramMap.put("endDate", date);
    				}   			
    			}   */
            }
        }
        List<Map<String, Object>> regInfoList = platFormBaseMapper.getRegInfo(paramMap);

        List<RegInfo> regInfos = new ArrayList<>();
        RegInfo regInfo = new RegInfo();
        List<TimeRegInfoList> timeRegInfoLists = new ArrayList<>();
        if (regInfoList.size() > 0) {
            regInfo.setDoctorId(regInfoList.get(0).get("doctorid") == null ? null : regInfoList.get(0).get("doctorid").toString());
            regInfo.setDoctorName(regInfoList.get(0).get("doctorname") == null ? null : regInfoList.get(0).get("doctorname").toString());
            regInfo.setDoctorTitle(regInfoList.get(0).get("doctortitle") == null ? null : regInfoList.get(0).get("doctortitle").toString());
            regInfo.setDeptId(regInfoList.get(0).get("deptid") == null ? null : regInfoList.get(0).get("deptid").toString());
            regInfo.setDeptName(regInfoList.get(0).get("deptname") == null ? null : regInfoList.get(0).get("deptname").toString());


            for (Map<String, Object> map : regInfoList) {
                HashMap ht = new HashMap();
                if (ht.containsValue(map.get("regdate"))) {
                    continue;
                } else {
                    ht.put("regdate", map.get("regdate"));
                    TimeRegInfoList timeRegInfoList = new TimeRegInfoList();
                    timeRegInfoList.setRegDate(map.get("regdate") == null ? null : StringUtils.substringBefore(map.get("regdate").toString(), " "));
                    timeRegInfoList.setRegWeekDay(map.get("regweekday") == null ? null : map.get("regweekday").toString());

                    List<TimeRegInfo> timeInfos = new ArrayList<>();
                    for (Map<String, Object> maps : regInfoList) {
                        if (map.get("regdate") == maps.get("regdate")) {
                            TimeRegInfo timeRegInfo = new TimeRegInfo();
                            timeRegInfo.setTimeName(map.get("timename") == null ? null : map.get("timename").toString());
                            timeRegInfo.setTimeId(map.get("timeid") == null ? null : map.get("timeid").toString());
                            timeRegInfo.setStatusType(map.get("statustype") == null ? null : map.get("statustype").toString());
                            timeRegInfo.setRegTotalCount(map.get("regtotalcount") == null ? null : map.get("regtotalcount").toString());
                            timeRegInfo.setRegLeaveCount(map.get("regleavecount") == null ? null : map.get("regleavecount").toString());
                            timeRegInfo.setRegFee(map.get("regfee") == null ? null : map.get("regfee").toString());
                            timeRegInfo.setTreatFee(map.get("treatfee") == null ? null : map.get("treatfee").toString());
                            timeRegInfo.setIsTimeReg(map.get("istimereg") == null ? null : map.get("istimereg").toString());
                            timeInfos.add(timeRegInfo);
                        }
                    }
                    timeRegInfoList.setTimeRegInfo(timeInfos);
                    timeRegInfoLists.add(timeRegInfoList);
                }
            }
        }
        regInfo.setTimeRegInfoList(timeRegInfoLists);
        regInfos.add(regInfo);
        //subject
        RegInfoResSubject regInfoResSubject = new RegInfoResSubject();

        regInfoResSubject.setRes(regInfos);
        //result
        RegInfoResResult regInfoResResult = new RegInfoResResult();
        regInfoResResult.setId("AA");
        regInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));

        regInfoResResult.setRequestId(reqData.getId());
        regInfoResResult.setText("处理成功!");
        regInfoResResult.setSubject(regInfoResSubject);

        //response
        RegInfoResExd regInfoResExd = new RegInfoResExd();
        regInfoResExd.setResult(regInfoResResult);

        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, regInfoResExd), RegInfoResExd.class);
		if(regInfoList.size() != 0){
			return PfWsUtils.setResStatus("0","处理成功！",hospXml);			
		}else{
			return PfWsUtils.setResStatus("0","未查询到数据！",hospXml);	
		}
    }

    /**
     * 医生号源分时信息查询接口
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String getTimeRegInfo(String content) throws Exception {
        TimeRegInfoReqSubject reqData = (TimeRegInfoReqSubject) XmlUtil.XmlToBean(content, TimeRegInfoReqSubject.class);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (reqData.getSubject() != null && reqData.getSubject().size() > 0) {
            paramMap.put("deptId", reqData.getSubject().get(0).getDeptId());
            paramMap.put("doctorId", reqData.getSubject().get(0).getDoctorId());
            paramMap.put("regDate", reqData.getSubject().get(0).getRegDate());
            paramMap.put("timeId", reqData.getSubject().get(0).getTimeId());

        }
        List<TimeRegInfo> timeRegInfoList = platFormBaseMapper.getTimeRegInfo(paramMap);

        //subject
        TimeRegInfoResSubject timeRegInfoResSubject = new TimeRegInfoResSubject();
        timeRegInfoResSubject.setRes(timeRegInfoList);

        //result
        TimeRegInfoResResult timeRegInfoResResult = new TimeRegInfoResResult();
        timeRegInfoResResult.setId("AA");
        timeRegInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        timeRegInfoResResult.setRequestId(reqData.getId());
        timeRegInfoResResult.setText("处理成功!");
        timeRegInfoResResult.setSubject(timeRegInfoResSubject);


        //response
        TimeRegInfoResExd timeRegInfoResExd = new TimeRegInfoResExd();
        timeRegInfoResExd.setResult(timeRegInfoResResult);

        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, timeRegInfoResExd), TimeRegInfoResExd.class);
        if(timeRegInfoList.size() != 0){
        	return PfWsUtils.setResStatus("0","处理成功！",hospXml);
        }else{
        	return PfWsUtils.setResStatus("0","未查询到数据！",hospXml);
        }       
    }

    /**
     * 科室号源信息查询接口
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String getDeptRegInfo(String content) throws Exception {
        DeptRegInfoReqSubject reqData = (DeptRegInfoReqSubject) XmlUtil.XmlToBean(content, DeptRegInfoReqSubject.class);
        SetUserUtils.setUserByOldId(reqData.getSender().getSenderId());
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (reqData.getSubject() != null && reqData.getSubject().size() > 0) {
            paramMap.put("deptId", reqData.getSubject().get(0).getDeptId());
            paramMap.put("startDate", reqData.getSubject().get(0).getStartDate());
            paramMap.put("endDate", reqData.getSubject().get(0).getEndDate());
            Date startDate = reqData.getSubject().get(0).getStartDate();
            Date endDate = reqData.getSubject().get(0).getEndDate();

            if (startDate != null && endDate != null) {
                int number = endDate.compareTo(startDate);
                if (number < 1) {
                    int dateTime = Integer.valueOf(ApplicationUtils.getSysparam("SCH0008", false));
                    Date date = plusDay(dateTime, endDate);
                    paramMap.put("endDate", date);
                }
            }

        }
        List<DeptRegInfo> deptRegInfoList = platFormBaseMapper.getDeptRegInfo(paramMap);
        //subject
        DeptRegInfoResSubject deptRegInfoResSubject = new DeptRegInfoResSubject();
        deptRegInfoResSubject.setRes(deptRegInfoList);

        //result
        DeptRegInfoResResult deptRegInfoResResult = new DeptRegInfoResResult();
        deptRegInfoResResult.setId("AA");
        deptRegInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        deptRegInfoResResult.setRequestId(reqData.getId());
        deptRegInfoResResult.setText("处理成功!");
        deptRegInfoResResult.setSubject(deptRegInfoResSubject);

        //response
        DeptRegInfoResExd deptRegInfoResExd = new DeptRegInfoResExd();
        deptRegInfoResExd.setResult(deptRegInfoResResult);

        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, deptRegInfoResExd), DeptRegInfoResExd.class);
        if(deptRegInfoList.size() != 0){
        	return PfWsUtils.setResStatus("0","处理成功！",hospXml);
        }else{       	
        	return PfWsUtils.setResStatus("0","未查询到数据！",hospXml);
        }
    }

    /**
     * 患者信息查询
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String getUserInfo(String content) throws Exception {
        UserInfoReqSubject reqData = (UserInfoReqSubject) XmlUtil.XmlToBean(content, UserInfoReqSubject.class);

        //res
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (reqData.getSubject().get(0).getUserIdCard() == null || reqData.getSubject().get(0).getUserIdCard().length() == 0) {
            throw new BusException("参数患者身份证件号码为必填项，不允许为空");
        }
        if (reqData.getSubject() != null && reqData.getSubject().size() > 0) {

            paramMap.put("userCardType", reqData.getSubject().get(0).getUserCardType());
            paramMap.put("userCardId", reqData.getSubject().get(0).getUserCardId());
            paramMap.put("userIdCard", reqData.getSubject().get(0).getUserIdCard());
            paramMap.put("userName", reqData.getSubject().get(0).getUserName());
        }
        List<UserInfo> userInfoList = platFormBaseMapper.getUserInfo(paramMap);

        //subject
        UserInfoResSubject userInfoResSubject = new UserInfoResSubject();
        userInfoResSubject.setRes(userInfoList);

        //result
        UserInfoResResult userInfoResResult = new UserInfoResResult();
        userInfoResResult.setId("AA");
        userInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        userInfoResResult.setRequestId(reqData.getId());
        userInfoResResult.setText("处理成功!");
        userInfoResResult.setSubject(userInfoResSubject);

        //response
        UserInfoResExd userInfoResExd = new UserInfoResExd();
        userInfoResExd.setResult(userInfoResResult);

        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, userInfoResExd), UserInfoResExd.class);
        if(userInfoList.size() != 0){
        	return PfWsUtils.setResStatus("0","处理成功！",hospXml);
        }else{
        	return PfWsUtils.setResStatus("0","未查询到数据！",hospXml);
        }
       

    }

    /**
     * 给指定日期加上天数
     *
     * @param num
     * @param newDate
     * @return
     * @throws Exception
     */
    public Date plusDay(int num, Date newDate) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date  currdate = format.parse(newDate);
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        newDate = ca.getTime();
        return newDate;
    }


    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }


    /**
     * 查询患者常用地址
     *
     * @param content
     * @return
     */
    public String getPatientAddress(String content) {
        UserInfoReqSubject reqData = (UserInfoReqSubject) XmlUtil.XmlToBean(content, UserInfoReqSubject.class);
        List<PiAddrInfo> piAddrInfos = platFormBaseMapper.getPatientAddress(reqData.getSubject().get(0).getUserCardType(), reqData.getSubject().get(0).getUserCardId());

        //res
        PiAddrInfoRes piAddrInfoRes = new PiAddrInfoRes();
        piAddrInfoRes.setAddrList(piAddrInfos);

        //subject
        PiAddrInfoResSubject piAddrInfoResSubject = new PiAddrInfoResSubject();
        piAddrInfoResSubject.setRes(piAddrInfoRes);

        //result
        PiAddrInfoResResult piAddrInfoResResult = new PiAddrInfoResResult();
        piAddrInfoResResult.setId("AA");
        piAddrInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        piAddrInfoResResult.setRequestId(reqData.getId());
        piAddrInfoResResult.setText("处理成功!");
        piAddrInfoResResult.setSubject(piAddrInfoResSubject);

        //response
        PiAddrInfoResExd piAddrInfoResExd = new PiAddrInfoResExd();
        piAddrInfoResExd.setResult(piAddrInfoResResult);

        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, piAddrInfoResExd), PiAddrInfoResExd.class);
        return hospXml;
    }

    /**
     * 3.64	注册或修改患者常用地址
     *
     * @param content
     * @return
     */
    public String setPatientAddress(String content) {
        PiAddrInfoReqSubject reqData = (PiAddrInfoReqSubject) XmlUtil.XmlToBean(content, PiAddrInfoReqSubject.class);
        if (!StringUtils.isBlank(reqData.getSubject().get(0).getAddrId())) {
            setPiAddress(reqData.getSubject().get(0));
            DataBaseHelper.updateBeanByPk(setPiAddress(reqData.getSubject().get(0)));
        } else {
            String sqlCon = "";
            switch (reqData.getSubject().get(0).getUserCardType()) {
                case "0":
                    sqlCon = " and pi.id_no = ?";
                    break;
                case "1":
                    sqlCon = " and pi.code_op = ?";
                    break;
                case "2":
                    sqlCon = " and pi.citizen_no = ?";
                    break;
                case "3":
                    sqlCon = " and pi.insur_no = ?";
                    break;
                case "4":
                    sqlCon = " and pi.idno_rel = ?";
                    break;
                case "5":
                    sqlCon = " and pi.mobile = ?";
                    break;
                default:
                    break;
            }
            PiMaster piMaster = DataBaseHelper.queryForBean("select pi.pk_pi from pi_master pi where 1=1" + sqlCon, PiMaster.class, reqData.getSubject().get(0).getUserCardId());
            PiAddress addr = setPiAddress(reqData.getSubject().get(0));
            addr.setPkPi(piMaster.getPkPi());
            addr.setDtAddrtype("04");
            addr.setAmtFee("0");
            addr.setPkAddr(NHISUUID.getKeyId());
            DataBaseHelper.insertBean(addr);
        }
        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "处理成功！", "0"), PlatFormResSucc.class);
        return hospXml;
    }

    private PiAddress setPiAddress(PiAddrInfo piAddrInfo) {
        PiAddress piAddress = new PiAddress();
        piAddress.setPkAddr(piAddrInfo.getAddrId());
        piAddress.setNameProv(piAddrInfo.getProvince());
        piAddress.setNameCity(piAddrInfo.getCity());
        piAddress.setNameDist(piAddrInfo.getDistrict());
        piAddress.setAddr(piAddrInfo.getAddress());
        piAddress.setNameRel(piAddrInfo.getContact());
        piAddress.setTel(piAddrInfo.getPhone());
        piAddress.setFlagUse(piAddrInfo.getFlagDef());
        return piAddress;
    }
}
