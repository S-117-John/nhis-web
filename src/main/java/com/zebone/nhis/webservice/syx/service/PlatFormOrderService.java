package com.zebone.nhis.webservice.syx.service;

import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.plan.SchPlan;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormOrderMapper;
import com.zebone.nhis.webservice.syx.support.SetUserUtils;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.*;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;

@Service
public class PlatFormOrderService {

    @Resource
    PlatFormOrderMapper platFormOrderMapper;


    /**
     * 医院信息查询接口
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String getHospitalInfo(String content) throws Exception {

        HospitalInfoReqSubject reqData = (HospitalInfoReqSubject) XmlUtil.XmlToBean(content, HospitalInfoReqSubject.class);
        SetUserUtils.setUserByOldId(reqData.getSender().getSenderId());

        //res
        HospitalInfo hospitalInfo = new HospitalInfo();        
       
		List<DistrictInfo> areaInfo = platFormOrderMapper.getAreaInfo(UserContext.getUser().getPkOrg());
		hospitalInfo.setDistrictInfo(areaInfo);
		ApplicationUtils.copyProperties(hospitalInfo,areaInfo.get(0));
		hospitalInfo.setMaxRegDays(ApplicationUtils.getSysparam("SCH0008", false, "请维护系统参数SCH0008！"));
		if(areaInfo.size() != 0){
			hospitalInfo.setResultCode("0");
			hospitalInfo.setResultDesc("处理成功");        	
		}else{
			hospitalInfo.setResultCode("0");
			hospitalInfo.setResultDesc("未查询到数据");
		}		   
        //subject
        HospitalInfoResSubject hospitalInfoResSubject = new HospitalInfoResSubject();
        hospitalInfoResSubject.setRes(hospitalInfo);

        //result
        HospitalInfoResResult hospitalInfoResResult = new HospitalInfoResResult();
        hospitalInfoResResult.setId("AA");
        hospitalInfoResResult.setRequestTime(reqData.getCreateTime());
        hospitalInfoResResult.setRequestId(reqData.getId());
        hospitalInfoResResult.setText("处理成功!");
        hospitalInfoResResult.setSubject(hospitalInfoResSubject);

        //response
        HospitalInfoResExd hospitalInfoResExd = new HospitalInfoResExd();
        hospitalInfoResExd.setResult(hospitalInfoResResult);

        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, hospitalInfoResExd), HospitalInfoResExd.class);
        return hospXml;
    }

    /**
     * 号源锁定接口
     *
     * @param content
     * @return
     */
    public String lockReg(String content) throws Exception {
        RegLockReqSubject reqData = (RegLockReqSubject) XmlUtil.XmlToBean(content, RegLockReqSubject.class);
        SetUserUtils.setUserByEmpCode(reqData.getSender().getSenderId());
        Integer isApp = DataBaseHelper.queryForScalar("select count(1) from sch_ticket where dt_apptype=? and id_lock=? and flag_lock='1' ", Integer.class, new Object[]{reqData.getSubject().get(0).getOrderType(), reqData.getSubject().get(0).getLockId()});
        String resXml = "";
        if (isApp != null && isApp > 0) {
            resXml = XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "号源锁定成功", "0"), PlatFormResSucc.class);
            return resXml;
        }

        //如果入参中“出诊日期”大于HISKey“中二外部预约系统接口的相关参数设置”中“最大预约天数”的设置值对应的“最大可预约日期”，则返回失败。
        // 处理结果描述为“预约日期（20-5-10）大于最大可预约日期（2011-5-9）。”
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int sch0008 = Integer.parseInt(ApplicationUtils.getSysparam("SCH0008", false, "请维护系统参数SCH0008！"));
        calendar.add(Calendar.DAY_OF_MONTH, sch0008);
        if (calendar.getTime().compareTo(DateUtils.strToDate(reqData.getSubject().get(0).getRegDate(), "yyyy-MM-dd")) < 0) {
            resXml = XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "预约日期（" + reqData.getSubject().get(0).getRegDate() + "）大于最大可预约日期（" + DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", calendar.getTime()) + "）", "1"), PlatFormResSucc.class);
            return resXml;
        }

        //如果入参中“分时开始时间”和“分时结束时间”没有对应的时段明细记录，则返回失败，
        // 处理结果描述为“分时开始时间和分时结束时间没有对应的时段明细。”
        Map<String, Object> qryParm = Maps.newHashMap();
        qryParm.put("dateWork", reqData.getSubject().get(0).getRegDate() + " 00:00:00");
        //qryParm.put("codeDateslot", reqData.getSubject().get(0).getTimeRegInfo().getTimeId());
        qryParm.put("beginTime", reqData.getSubject().get(0).getRegDate() + " " + reqData.getSubject().get(0).getStartTime() + ":00");//分时开始时间
        qryParm.put("endTime", reqData.getSubject().get(0).getRegDate() + " " + reqData.getSubject().get(0).getEndTime() + ":00");//分时结束时间
        Integer tickets = platFormOrderMapper.qryDateLot(qryParm);
        if (tickets == null || tickets <= 0) {
            resXml = XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "分时开始时间和分时结束时间没有对应的时段明细。", "1"), PlatFormResSucc.class);
            return resXml;
        }

        //	如果指定出诊日期和时段属于预约时段，且指定门诊专科、指定医生、指定排班日期、指定明细时段的预约剩余号源等于0，
        //	则返回失败，处理结果描述为“专科A、医生B、2011-5-5、8:00至9:00的预约剩余号源为0”
        qryParm.put("deptId", reqData.getSubject().get(0).getDeptId());
        qryParm.put("empId", reqData.getSubject().get(0).getDoctorId());
        List<SchTicket> ticketNum = platFormOrderMapper.qryIsApp(qryParm);
        if (ticketNum == null || ticketNum.size() <= 0) {
            Map<String, Object> nameDept = DataBaseHelper.queryForMap("select name_dept from bd_ou_dept where code_dept = ?", reqData.getSubject().get(0).getDeptId());
            Map<String, Object> nameEmp = DataBaseHelper.queryForMap("select name_emp from bd_ou_employee where code_emp = ?", reqData.getSubject().get(0).getDoctorId());
            resXml = XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData,
                    nameDept.get("nameDept") + "，" + nameEmp.get("nameEmp") + "，" + reqData.getSubject().get(0).getRegDate() + "，" + reqData.getSubject().get(0).getStartTime() + "至" + reqData.getSubject().get(0).getEndTime() + "的剩余号源为0。", "1"), PlatFormResSucc.class);
            return resXml;
        } else {
            DataBaseHelper.update("update sch_ticket set flag_lock='1' ,flag_used='1',dt_apptype=?,id_lock=? where pk_schticket=? ",
                    new Object[]{reqData.getSubject().get(0).getOrderType(), reqData.getSubject().get(0).getLockId(), ticketNum.get(0).getPkSchticket()});
            DataBaseHelper.update("update sch_sch set cnt_used=cnt_used+1 where pk_sch=?", new Object[]{ticketNum.get(0).getPkSch()});
        }

        return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "锁定号源成功。", "0"), PlatFormResSucc.class);
    }

    /**
     * 解除锁定接口
     *
     * @param content
     * @return
     */
    public String unlockReg(String content) throws Exception {
        RegLockReqSubject reqData = (RegLockReqSubject) XmlUtil.XmlToBean(content, RegLockReqSubject.class);
        // 如果入参中“预约来源（orderType）”+“锁定流水号（LockID）”没有对应的号源记录（包括：预约号源和当前号源），则返回失败，
        // 处理结果描述为“预约来源（健康之路）的锁定流水号（123）没有对应的号源记录。”
        SchTicket hasApp = DataBaseHelper.queryForBean("select * from sch_ticket where dt_apptype=? and id_lock=?", SchTicket.class, new Object[]{reqData.getSubject().get(0).getOrderType(), reqData.getSubject().get(0).getLockId()});
        String resXml = "";
        if (hasApp == null) {
            resXml = XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "预约来源（" + reqData.getSubject().get(0).getOrderType() + "）的锁定流水号（" + reqData.getSubject().get(0).getLockId() + "）没有对应的号源记录。", "1"), PlatFormResSucc.class);
            return resXml;
        }

        // 如果入参中“预约来源（orderType）”+ “锁定流水号（LockID）”已不存在对应的号源记录，则返回“0-成功”。（相当于允许第三方重复调用当前接口）
        if ("0".equals(hasApp.getFlagLock())) {
            resXml = XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "解除号源锁定成功", "0"), PlatFormResSucc.class);
            return resXml;
        }

        //	如果入参中“预约来源（orderType）”+ “锁定流水号（LockID）”存在对应的号源记录（包括：预约号源和当前号源），但已挂号，
        //	则返回失败，处理结果描述为“预约来源（健康之路）的锁定流水号（123）对应的号源记录已挂号。”


        // 将“预约来源（orderType）”+ “锁定流水号（LockID）”对应的号源记录（包括：预约号源和当前号源）解除锁定：
        DataBaseHelper.update("update sch_ticket set flag_lock='0' ,flag_used='0',dt_apptype=null,id_lock=null where pk_schticket=? ", new Object[]{hasApp.getPkSchticket()});
        DataBaseHelper.update("update sch_sch set cnt_used=cnt_used-1 where pk_sch=?", new Object[]{hasApp.getPkSch()});

        return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "解除号源锁定成功", "0"), PlatFormResSucc.class);
    }

    /**
     * 挂号接口
     *
     * @param content
     * @return
     */
    public String addOrder(String content) throws Exception { 
        RegLockReqSubject reqData = (RegLockReqSubject) XmlUtil.XmlToBean(content, RegLockReqSubject.class);
        SetUserUtils.setUserByEmpCode(reqData.getSender().getSenderId());

        //	如果入参中“患者农行卡号”或“身份证号”或“姓名”内容为空，则返回失败，处理结果描述为“患者的农行卡号（或身份证号或姓名）不允许为空”。
        // （农行网点挂号必须上传“患者农行卡号”、“身份证号”、“姓名”）
        if ("7".equals(reqData.getSubject().get(0).getOrderType())) {
            if (StringUtils.isEmpty(reqData.getSubject().get(0).getUserNhCard()) || StringUtils.isEmpty(reqData.getSubject().get(0).getUserIdCard()) || StringUtils.isEmpty(reqData.getSubject().get(0).getUserName())) {
                return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "患者的农行卡号（或身份证号或姓名）不允许为空", "1"), PlatFormResSucc.class);
            }
        }

        //根据患者标识信息（身份证+患者姓名）获取待挂号患者信息；
        //如果不存在患者信息，提示“未找到匹配的患者信息，请先注册！”；
        //如果存在多条患者信息，获取最近注册的一条记录；
        List<RegPiMasterVo> piMasters = DataBaseHelper.queryForList("select * from pi_master where name_pi=? and id_no=? order by create_time desc", RegPiMasterVo.class, new Object[]{reqData.getSubject().get(0).getUserName(), reqData.getSubject().get(0).getUserIdCard()});
        if (piMasters == null || piMasters.size() <= 0) {
            return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "未找到匹配的患者信息，请先注册！", "1"), PlatFormResSucc.class);
        }

        //允许患者同一日期重复挂号的科室编码
        String sch0009 = ApplicationUtils.getSysparam("SCH0009", false);

        //对于参数SCH0009中设置的科室，检索指定日期下是否已达到允许重复挂号的次数（SCH0010参数值），达到时提示“患者xxxx年xx月xx日在xx科已存在x次挂号！”，否则继续挂号；
        //对于参数SCH0009中未设置的科室，检索指定日期下是否存在挂号记录，如果存在，提示“患者xxxx年xx月xx日在xx科已存在挂号！”，否则继续挂号；
        String apptSql = "select appt.*,dept.name_dept from SCH_APPT appt inner join PI_MASTER pi on pi.PK_PI = appt.PK_PI " +
                "inner join BD_OU_DEPT dept on dept.PK_DEPT = appt.PK_DEPT_EX where appt.DATE_APPT = ? and dept.CODE_DEPT=? and pi.PK_PI=? and appt.flag_cancel='0' ";
        List<Map<String, Object>> appCount = DataBaseHelper.queryForList(apptSql, new Object[]{DateUtils.strToDate(DateUtils.dateToStr("yyyyMMdd", new Date()) + "000000"), reqData.getSubject().get(0).getDeptId(), piMasters.get(0).getPkPi()});
        if (sch0009 != null) {
            //同一科室下允许患者重复挂号次数
            String sch0010 = ApplicationUtils.getSysparam("SCH0010", false, "请联系管理员维护系统参数SCH0010！");
            List<String> sch0009s = Arrays.asList(sch0009.split(",|，"));
            if (sch0009s.contains(reqData.getSubject().get(0).getDeptId())) {
                if (appCount != null && appCount.size() >= Integer.parseInt(sch0010)) {
                    return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData,
                            "患者" + reqData.getSubject().get(0).getUserName() + appCount.get(0).get("dateAppt") + "在" + appCount.get(0).get("nameDept") + "已存在" + sch0010 + "次挂号！", "1"), PlatFormResSucc.class);
                }
            }
        }
        if (appCount != null && appCount.size() > 0) {
            return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData,
                    "患者" + reqData.getSubject().get(0).getUserName() + appCount.get(0).get("dateAppt") + "在" + appCount.get(0).get("nameDept") + "已存在挂号！", "1"), PlatFormResSucc.class);
        }

        //调用预约挂号服务，生成挂号记录；
        //更新排班表sch_sch，排班号表sch_ticket;
        //生成预约记录sch_appt（订单号ordid存入orderid_ext），sch_appt_pv
        ApplicationUtils apputil = new ApplicationUtils();

        //	根据号码锁定lockid获取锁定号表明细：
        SchTicket schTicket = null;
        if (!StringUtils.isEmpty(reqData.getSubject().get(0).getLockId())) {
            schTicket = DataBaseHelper.queryForBean("select * from sch_ticket where id_lock=?", SchTicket.class, reqData.getSubject().get(0).getLockId());
        }
        //未锁定号源直接预约挂号
        if (schTicket == null) {
            List<SchTicket> schTickets = platFormOrderMapper.qryRegPam(reqData.getSubject().get(0));
            if (schTickets == null || schTickets.size() <= 0) {
                //String desc = appCount.get(0).get("nameDept") + "、" + reqData.getSubject().get(0).getDoctorId() + "、" + reqData.getSubject().get(0).getRegDate() + "、" + reqData.getSubject().get(0).getStartTime() + "至" + reqData.getSubject().get(0).getEndTime() + "的剩余号源为0。";
                //专科A、医生B、2011-5-5、8:00至9:00的剩余号源为0。
            	String sql = "select * from sch_resource where code=?";
        		List<Map<String, Object>> deptName = DataBaseHelper.queryForList(sql, new Object[]{reqData.getSubject().get(0).getDeptId()});
        		String desc = deptName.get(0).get("name")+"、"+reqData.getSubject().get(0).getDoctorId() + "医生  、" + reqData.getSubject().get(0).getRegDate() + "、" + reqData.getSubject().get(0).getStartTime() + "至" + reqData.getSubject().get(0).getEndTime() + "的剩余号源为0。";
                return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, desc, "1"), PlatFormResSucc.class);
            }
            DataBaseHelper.update("update sch_ticket set flag_used='1',dt_apptype=? where pk_schticket=? ",new Object[]{reqData.getSubject().get(0).getOrderType(),schTickets.get(0).getPkSchticket()});
            schTicket=schTickets.get(0);
        }

        SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where pk_sch=?", SchSch.class, schTicket.getPkSch());
        SchPlan schPlan = DataBaseHelper.queryForBean("select * from sch_plan where pk_schplan=?", SchPlan.class, schSch.getPkSchplan());
        piMasters.get(0).setEuSchclass(schSch.getEuSchclass());
        piMasters.get(0).setPkSch(schSch.getPkSch());
        piMasters.get(0).setOutsideOrderId(reqData.getSubject().get(0).getOrderId());
        piMasters.get(0).setOrderSource(reqData.getSubject().get(0).getOrderType());
        piMasters.get(0).setDateAppt(DateUtils.strToDate(reqData.getSubject().get(0).getRegDate(), "yyyy-MM-dd"));
        RegApptVo regApptVo = new RegApptVo();
        regApptVo.setRegvo(piMasters.get(0));
        regApptVo.setSchSch(schSch);
        regApptVo.setSchplan(schPlan);
        regApptVo.setU(UserContext.getUser());
        regApptVo.setTicket(schTicket);
        regApptVo.setGh(false);
        ResponseJson responseJson = apputil.execService("PV", "RegSyxHandler", "saveApptSchRegInfoExt", regApptVo, UserContext.getUser());
        Map<String, Object> regPiMasterMap = JsonUtil.readValue(JsonUtil.getJsonNode((String) responseJson.getData(), "regvo"), Map.class);

        RegResExd regResExd = getRegResExd(reqData, regPiMasterMap);

        return XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, regResExd), RegResExd.class);
    }

    /**
     * 组装返回SQL
     *
     * @param reqData
     * @param regPiMasterVo
     * @return
     */
    private RegResExd getRegResExd(RegLockReqSubject reqData, RegPiMasterVo regPiMasterVo) {
        //组装返回XML
        //res
        RegLockVo regLockVo = new RegLockVo();
        regLockVo.setResultCode("0");
        regLockVo.setResultDesc("预约挂号成功！");
        regLockVo.setOrderIdHis(regPiMasterVo.getApptCode());
        regLockVo.setUserFlag("1");
        regLockVo.setWaitTime(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", regPiMasterVo.getDateBegin()));
        if (!StringUtils.isEmpty(regPiMasterVo.getPkDeptunit())) {
            Map<String, Object> stringObjectMap = DataBaseHelper.queryForMap("select name from bd_dept_unit where pk_deptunit=?", regPiMasterVo.getPkDeptunit());
            String name = DataBaseHelper.queryForMap("select name from bd_dept_unit where pk_deptunit=?", regPiMasterVo.getPkDeptunit()).get("name").toString();
            regLockVo.setDiagnoseRoomName(name);
        }

        //subject
        RegResSubject regResSubject = new RegResSubject();
        regResSubject.setRegLockVo(regLockVo);

        //result
        RegResResult regResResult = new RegResResult();
        regResResult.setId("AA");
        regResResult.setRequestId(reqData.getId());
        regResResult.setRequestTime(reqData.getCreateTime());
        regResResult.setText("处理成功！");
        regResResult.setRegResSubject(regResSubject);

        //response
        RegResExd regResExd = new RegResExd();
        regResExd.setResult(regResResult);
        return regResExd;
    }

    /**
     * 组装返回SQL
     *
     * @param reqData
     * @param regPiMasterVo
     * @return
     */
    private RegResExd getRegResExd(RegLockReqSubject reqData, Map<String, Object> regPiMasterVo) {
        //组装返回XML
        //res
        RegLockVo regLockVo = new RegLockVo();
        regLockVo.setResultCode("0");
        regLockVo.setResultDesc("预约挂号成功！");
        regLockVo.setOrderIdHis(regPiMasterVo.get("apptCode").toString());
        regLockVo.setUserFlag("1");
        regLockVo.setWaitTime(regPiMasterVo.get("dateBegin").toString());
        if (regPiMasterVo.get("pkDeptunit") != null && !"".equals(regPiMasterVo.get("pkDeptunit").toString())) {
            String name = DataBaseHelper.queryForMap("select name from bd_dept_unit where pk_deptunit=?", regPiMasterVo.get("pkDeptunit").toString()).get("name").toString();
            regLockVo.setDiagnoseRoomName(name);
        }

        //subject
        RegResSubject regResSubject = new RegResSubject();
        regResSubject.setRegLockVo(regLockVo);

        //result
        RegResResult regResResult = new RegResResult();
        regResResult.setId("AA");
        regResResult.setRequestId(reqData.getId());
        regResResult.setRequestTime(reqData.getCreateTime());
        regResResult.setText("处理成功！");
        regResResult.setRegResSubject(regResSubject);

        //response
        RegResExd regResExd = new RegResExd();
        regResExd.setResult(regResResult);
        return regResExd;
    }

    public String getDeptInfo(String content) throws Exception {

        DeptInfoReqSubject reqData = (DeptInfoReqSubject) XmlUtil.XmlToBean(content, DeptInfoReqSubject.class);
        SetUserUtils.setUserByEmpCode(reqData.getSender().getSenderId());
        //res
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
        if (reqData.getSubject() != null && reqData.getSubject().size() > 0) {
            paramMap.put("deptId", reqData.getSubject().get(0).getDeptId());
        }
        List<DeptInfo> deptInfoList = platFormOrderMapper.getDeptInfo(paramMap);                
        

        ResultDesc resultDesc = new ResultDesc();        
        //subject
        DeptInfoResSubject deptInfoResSubject = new DeptInfoResSubject();
        deptInfoResSubject.setRes(deptInfoList); 

        //result
        DeptInfoResResult deptInfoResResult = new DeptInfoResResult();
        deptInfoResResult.setId("AA");
        deptInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        deptInfoResResult.setRequestId(reqData.getId());
        deptInfoResResult.setText("处理成功!");
        deptInfoResResult.setSubject(deptInfoResSubject);

        //response
        DeptlInfoResExd deptlInfoResExd = new DeptlInfoResExd();
        deptlInfoResExd.setResult(deptInfoResResult);

        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, deptlInfoResExd), DeptlInfoResExd.class);
        if(deptInfoList.size() != 0){
        	return PfWsUtils.setResStatus("0","处理成功！",hospXml);
        }else{
        	return PfWsUtils.setResStatus("0","未查询到数据!",hospXml);        	
        }
        
    }

    /**
     * 取消预约接口
     *
     * @param content
     * @return
     */
    public String cancelOrder(String content) throws Exception {
        RegLockReqSubject reqData = (RegLockReqSubject) XmlUtil.XmlToBean(content, RegLockReqSubject.class);
        SchAppt schAppt = DataBaseHelper.queryForBean("select * from sch_appt where orderid_ext=? and dt_apptype=?", SchAppt.class, reqData.getSubject().get(0).getOrderId(), reqData.getSubject().get(0).getOrderType());
        if (schAppt == null) {
            return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "未找到订单号！", "1"), PlatFormResSucc.class);
        }
        if(schAppt.getFlagCancel().equals("0")){
        	DataBaseHelper.update("update sch_appt set eu_status='9', flag_cancel='1',date_cancel=?, note=? where pk_schappt=?",
        			new Object[]{DateUtils.strToDate(reqData.getSubject().get(0).getCancelTime(), "yyyy-MM-dd HH:mm:ss"), reqData.getSubject().get(0).getCancelReason(), schAppt.getPkSchappt()});
        	DataBaseHelper.update("update sch_sch set cnt_used=cnt_used-1 where pk_sch=?", new Object[]{schAppt.getPkSch()});
        	DataBaseHelper.update("update sch_ticket set flag_lock='0',dt_apptype=null,id_lock=null,flag_used='0' where pk_sch=? and ticketno=?",
        			new Object[]{schAppt.getPkSch(), schAppt.getTicketNo()});        	
        }

        return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "取消预约成功！", "0"), PlatFormResSucc.class);
    }

    /**
     * 取号接口
     *
     * @param content
     * @return
     */
    public String infoOrder(String content) throws Exception {
        RegLockReqSubject reqData = (RegLockReqSubject) XmlUtil.XmlToBean(content, RegLockReqSubject.class);
        SetUserUtils.setUserByOldId(reqData.getSender().getSenderId());
        Map<String, Object> pvTime = platFormOrderMapper.qryApptInfo(reqData.getSubject().get(0).getOrderType(), reqData.getSubject().get(0).getOrderId());
        if (pvTime == null) {
            return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "预约来源（" + reqData.getSubject().get(0).getOrderType() + "）的订单号（" + reqData.getSubject().get(0).getOrderId() + "）没有对应的挂号记录！", "1"), PlatFormResSucc.class);
        }
        String pv0023 = ApplicationUtils.getSysparam("PV0023", false, "请联系管理员维护系统参数PV0023");
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) pvTime.get("beginTime"));
        calendar.add(Calendar.MINUTE, -Integer.parseInt(pv0023));
        if (nowDate.compareTo(calendar.getTime()) < 0) {
            int dateSpace = DateUtils.getDateSpace(nowDate, calendar.getTime()) * 60;
            return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "距离可报道时间还有" + dateSpace + "分钟！", "1"), PlatFormResSucc.class);
        }
        DataBaseHelper.update("update pv_op set date_sign=? where pk_pv=?", new Object[]{nowDate, pvTime.get("pkPv")});
        String PV0008 = ApplicationUtils.getSysparam("PV0008", false, "请联系管理员维护系统参数PV0008");

//      读取参数PV0008，参数值为1时，调用分诊签到接口，生成排队记录，并返回排队顺序号
        if ("1".equals(PV0008)) {

        }
        return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "取号成功！", "0"), PlatFormResSucc.class);
    }

    /**
     * 通知到预约患者接口
     *
     * @param content
     * @return
     */
    public String notifyPatient(String content) throws Exception {
        RegLockReqSubject reqData = (RegLockReqSubject) XmlUtil.XmlToBean(content, RegLockReqSubject.class);
        SchAppt schAppt = DataBaseHelper.queryForBean("select * from sch_appt where orderid_ext=? and dt_apptype=?", SchAppt.class,
                new Object[]{reqData.getSubject().get(0).getOrderId(), reqData.getSubject().get(0).getOrderType()});
        if (schAppt == null) {
            return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "没有对应的挂号记录！", "1"), PlatFormResSucc.class);
        }
        DataBaseHelper.update("update sch_appt set flag_notice_canc='1' where pk_schappt=?", new Object[]{schAppt.getPkSchappt()});
        return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(reqData, "处理成功！", "0"), PlatFormResSucc.class);
    }

	public String getRegRecords(String content) {
		//TimeRegInfoReqSubject reqData = (TimeRegInfoReqSubject) XmlUtil.XmlToBean(content, TimeRegInfoReqSubject.class);
		RegRecordsInfoReqSubject reqData = (RegRecordsInfoReqSubject) XmlUtil.XmlToBean(content, RegRecordsInfoReqSubject.class);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (reqData.getSubject() != null && reqData.getSubject().size() > 0) {
            paramMap.put("orderType", reqData.getSubject().get(0).getOrderType());
            paramMap.put("orderId", reqData.getSubject().get(0).getOrderId());            

        }
        
        List<RegListInfo> regListInfoList = platFormOrderMapper.getRegListInfo(paramMap);
        for (RegListInfo regListInfo : regListInfoList) {
			if(regListInfo.getStatus().equals("1")){
				regListInfo.setStatus("已挂号");
			}else if(regListInfo.getStatus().equals("2")){
				regListInfo.setStatus("已支付");
			}else if(regListInfo.getStatus().equals("3")){
				regListInfo.setStatus("已取号");
			}else if(regListInfo.getStatus().equals("4")){
				regListInfo.setStatus("已退费");
			}
		}
        if(regListInfoList.size() == 0){
        	RegListInfo regListInfo = new RegListInfo();
        	regListInfo.setStatus("不存在对应的挂号记录");
        	regListInfoList.add(regListInfo);
        }
        
        //subject  
        RegListInfoResSubject regListInfoResSubject = new RegListInfoResSubject();
        regListInfoResSubject.setRes(regListInfoList);

        //result        
        RegListInfoResResult regListInfoResResult = new RegListInfoResResult();
        regListInfoResResult.setId("AA");
        regListInfoResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        regListInfoResResult.setRequestId(reqData.getId());
        regListInfoResResult.setText("处理成功!");
        regListInfoResResult.setSubject(regListInfoResSubject);

        //response
        RegListInfoResExd regListInfoResExd = new RegListInfoResExd();
        
        regListInfoResExd.setResult(regListInfoResResult);

        String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(reqData, regListInfoResExd), RegListInfoResExd.class);
        if(regListInfoList.size() != 0){
        	return PfWsUtils.setResStatus("0","处理成功！",hospXml);
        }else{
        	return PfWsUtils.setResStatus("0","未查询到数据！",hospXml);
        } 
	}
}
