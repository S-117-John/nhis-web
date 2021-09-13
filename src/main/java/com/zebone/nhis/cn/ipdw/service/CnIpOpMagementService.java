package com.zebone.nhis.cn.ipdw.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.cn.ipdw.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.cn.ipdw.dao.CnIpOpMagementMapper;
import com.zebone.nhis.cn.ipdw.vo.CnOpSureParam;
import com.zebone.nhis.cn.ipdw.vo.OpApplyQryParam;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.pub.service.BlCgExPubService;
import com.zebone.nhis.ex.pub.service.CreateExecListService;
import com.zebone.nhis.ex.pub.service.QueryRemainFeeService;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class CnIpOpMagementService {
    private Logger logger = LoggerFactory.getLogger("nhis.op");
    @Autowired
    private CnIpOpMagementMapper opDao;
    @Autowired
    private IpCgPubService cgDao;
    @Autowired
    private CreateExecListService createExecListService;
    @Autowired
    private QueryRemainFeeService exPubService;
    @Resource
    private BlCgExPubService blCgExPubService;

    public List<Map<String, Object>> qryOpApplyByParam(String param, IUser user) {
        OpApplyQryParam qryP = JsonUtil.readValue(param, OpApplyQryParam.class);
        Map<String, Object> m = new HashMap<String, Object>();
        User u = (User) user;
        String str = qryP.getPkPv();
        if (StringUtils.isNoneBlank(str)) {
            m.put("pkPv", str);
        }
        str = qryP.getCodeApply();
        if (StringUtils.isNoneBlank(str)) {
            m.put("codeApply", str);
        }
        str = qryP.getCodeIp();
        if (StringUtils.isNoneBlank(str)) {
            m.put("codeIp", str);
        }
        str = qryP.getCodePi();
        if (StringUtils.isNoneBlank(str)) {
            m.put("codePi", str);
        }
        str = qryP.getEuPvtype();
        if (StringUtils.isNoneBlank(str)) {
            m.put("euPvtype", str);
        }
        str = qryP.getEuStatus();
        if (StringUtils.isNoneBlank(str)) {
            m.put("euStatus", str);
        }
        str = qryP.getNamePi();
        if (StringUtils.isNoneBlank(str)) {
            m.put("namePi", str);
        }
		  /*str = qryP.getPkDept();
		if(StringUtils.isNoneBlank(str)){
			m.put("pkDept", str);
		}*/
        str = qryP.getPkDeptNs();
        if (StringUtils.isNoneBlank(str)) {
            m.put("pkDeptNs", str);
        }
        if (StringUtils.isNotBlank(qryP.getIsAnae())) {
            m.put("isAnae", qryP.getIsAnae());
        }
        if (StringUtils.isNoneBlank(qryP.getCodeOp())) {
            m.put("codeOp", qryP.getCodeOp());
        }
        str = qryP.getDeptOpAndAnae();
        if ("1".equals(str)) {
            m.put("deptOpAndAnae", "(ord.pk_dept_exec='" + qryP.getPkDept() + "' or opt.pk_dept_anae='" + qryP.getPkDept() + "')");
        } else {
            str = qryP.getIsAnae();
            if ("1".equals(str)) {
                m.put("pkDeptAnae", qryP.getPkDept());
                str = qryP.getDtAnae();
                if (StringUtils.isNoneBlank(str)) {
                    m.put("dtAnae", str);
                }
            } else {
                m.put("pkDeptExec", qryP.getPkDept());
            }
        }
        str = qryP.getReview();
        if (StringUtils.isNoneBlank(str)) {
            m.put("review", str);
        }
        Date date = qryP.getDateStartBegin();
        if (date != null) {
            m.put("dateStartBegin", DateUtils.dateToStr("yyyy-MM-dd", date) + " 00:00:00");
        }
        date = qryP.getDateStartEnd();
        if (date != null) {
            m.put("dateStartEnd", DateUtils.dateToStr("yyyy-MM-dd", date) + " 23:59:59");
        }
        List<Map<String, Object>> opApply = new ArrayList<>();
        //麻醉管理
        if ("1".equals(qryP.getIsAnae())) {
            opApply = opDao.qryAnaeApplyByParam(m);
        } else {
            opApply = opDao.qryOpApplyByParam(m);
        }
        str = qryP.getFlagDetial();
        if ("1".equals(str) && opApply != null && opApply.size() > 0) {
            Map<String, Object> mapPk = new HashMap();
            for (Map<String, Object> map : opApply) {
                String pkPv = (String) map.get("pkPv");
                //当开启一级缓存时，对象引用导致数据重复，故通过pk控制
                if (StringUtils.isNotBlank(pkPv) && !mapPk.containsKey(pkPv)) {
                    Map<String, Object> mo = new HashMap<String, Object>();
                    mo.put("pkPv", map.get("pkPv"));
                    //if(StringUtils.isNoneBlank(qryP.getOrdDeptByOp()))   mo.put("pkDept",u.getPkDept());
                    if (StringUtils.isNoneBlank(qryP.getOrdDeptNsByOp())) mo.put("pkDeptNs", u.getPkDept());
                    //未确认的
                    List<Map<String, Object>> cnOrds = opDao.qryPvCnOrder(mo);
                    //已确认的
                    List<Map<String, Object>> confirmCnOrds = opDao.qryConfirmPvCnOrder(mo);
                    cnOrds.addAll(confirmCnOrds);
                    mapPk.put(pkPv, cnOrds);
                    map.put("ordList", cnOrds);
                    //map.put("confirmOrdList", confirmCnOrds);
                } else {
                    if (mapPk.containsKey(pkPv)) map.put("ordList", mapPk.get(pkPv));
                }
            }
        }
        Map<String, Object> feeParamMap = new HashMap<String, Object>();
        for (Map<String, Object> map : opApply) {
            String pkPv = map.get("pkPv").toString();
            //查询医嘱录入的费用
            if("1".equals(qryP.getIsIncludeFee()))
    		{
            	 String pkCnord = map.get("pkCnord").toString();
            	 feeParamMap.put("pkPv", pkPv);
            	 feeParamMap.put("pkCnord", pkCnord);
            	 List<Map<String, Object>> listFee = opDao.qryOpApplyFeeByParam(feeParamMap);
            	 if(listFee != null && listFee.size() > 0)
            	 {
            		 Map<String, Object> mapItem = listFee.get(0);
            		 if(mapItem != null && mapItem.containsKey("sumam"))
    				 {
                    	 map.put("sumam", mapItem.get("sumam"));
    				 }
            	 }
    		}
            map.put("IsArrearage", "1".equals(ApplicationUtils.getSysparam("BL0004", false)) ? !exPubService.isArrearage(pkPv, "", BigDecimal.ZERO) : false);
        }
        return opApply;
    }
    
    /**
     * 查询出院患者手术申请
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryOpOutApply(String param, IUser user) {
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
       
    	String  dateBegin = CommonUtils.getString(map.get("dateBegin"));
		 if(dateBegin == null){
			 map.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
		 }else{
			 map.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		 }
		 
		 String  dateEnd = CommonUtils.getString(map.get("dateEnd"));
		 if(dateEnd == null){
			 map.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
		 }else{
			 map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		 }

        return opDao.qryOpOutApply(map);
    }
    
    //手术取消完成：
    public void finishCancel(String param, IUser user) {
        User u = (User) user;
        Map<String, Object> m = JsonUtil.readValue(param, Map.class);
        String isFinish = m.get("isFinish").toString();
        String pkCnord = m.get("pkCnord").toString();
        String pkPv = m.get("pkPv").toString();
        logger.info("手术取消完成初始参数{}",param);
        if ("1".equals(isFinish)) {
            String euStatus;
            //假如没有排班记录的话，申请单状态改成提交
            String sql = "SELECT count(1) FROM EX_OP_SCH WHERE EU_STATUS = '1' and PK_PV = ? and PK_CNORD = ?";
            Integer count = DataBaseHelper.queryForScalar(sql, Integer.class, pkPv, pkCnord);
            if (count == 0) {
                euStatus = "1";
            } else {
                euStatus = "2";
            }
            logger.info("此处开始修改手术申请单状态，修改前{}",JsonUtil.writeValueAsString(getOpApply(pkCnord)));
            DataBaseHelper.execute("update cn_op_apply set eu_status=? where eu_status='5' and pk_cnord=? ", euStatus, pkCnord);
            DataBaseHelper.execute("update ex_order_occ set eu_status='0',flag_canc='1',pk_dept_canc=?,"
                            + "date_canc=?,pk_emp_canc=?,name_emp_canc=?,modifier=?,modity_time=?,ts=? "
                            + "where pk_cnord=? and eu_status='1' ",
                    u.getPkDept(), new Date(), u.getPkEmp(), u.getNameEmp(), u.getPkEmp(), new Date(), new Date(), pkCnord);

            logger.info("此处开始修改手术申请单状态，修改后{}",JsonUtil.writeValueAsString(getOpApply(pkCnord)));
        }
    }

    public List<Map<String, Object>> qrySureOrd(String param, IUser user) {
        Map<String, Object> m = JsonUtil.readValue(param, Map.class);
        m.put("dept", ((User) user).getPkDept());
        String isFinish = m.get("isFinish").toString();
        String isAnaeFinish = m.get("isAnaeFinish").toString();
        String finish = m.get("finish").toString();
        logger.info("手术/麻醉完成初始参数{}",param);
        //手术完成
        if ("1".equals(isFinish)) finishOpOrd(m);
        //麻醉完成
        if ("1".equals(isAnaeFinish)) anaeFinish(m);
        //手术麻醉同时完成
        if ("1".equals(finish)) finishOpOrdTwo(m);
        String setPlanEx = m.get("setPlanEx").toString();
        if ("1".equals(setPlanEx)) setOrdExPlanTime((User) user, m);
        return null;
    }

    //麻醉完成/手术完成(住院费用查询独立)
    //004004011011
    public void finishOpAnae(String param, IUser user) {
        Map<String, Object> m = JsonUtil.readValue(param, Map.class);
        m.put("dept", m.get("pkDept").toString());
        String isFinish = m.get("isFinish").toString();
        String isAnaeFinish = m.get("isAnaeFinish").toString();
        String finish = m.get("finish").toString();
        //手术完成
        if ("1".equals(isFinish)) finishOpOrd(m);
        //麻醉完成
        if ("1".equals(isAnaeFinish)) anaeFinish(m);
    }

    //麻醉完成
    private void anaeFinish(Map<String, Object> m) {
        String pkCnord = m.get("pkCnord") == null ? "" : m.get("pkCnord").toString();
        if (StringUtils.isBlank(pkCnord)) throw new BusException("申请单医嘱主键为空！");
        String pkPv = m.get("pkPv") == null ? "" : m.get("pkPv").toString();
        if (StringUtils.isBlank(pkPv)) throw new BusException("申请单就诊主键为空！");
        //校验申请单下的签署医嘱是否执行：跟需求确认医嘱怎么跟申请单对应上。
        if (!VaildCnAnaeOrd(pkPv, pkCnord)) throw new BusException("麻醉科还有医嘱未执行确认！请确认！");
        DataBaseHelper.update("update CN_OP_APPLY set FLAG_FINISH_ANAE='1' where pk_cnord=? ", new Object[]{pkCnord});
    }

    private void finishOpOrdTwo(Map<String, Object> m) {
        String pkCnord = m.get("pkCnord") == null ? "" : m.get("pkCnord").toString();
        if (StringUtils.isBlank(pkCnord)) throw new BusException("申请单医嘱主键为空！");
        String pkPv = m.get("pkPv") == null ? "" : m.get("pkPv").toString();
        if (StringUtils.isBlank(pkPv)) throw new BusException("申请单就诊主键为空！");
        //校验申请单下的签署医嘱是否执行：跟需求确认医嘱怎么跟申请单对应上。
        if (!VaildCnOrd(pkPv, m.get("dept").toString())) throw new BusException("手术室还有医嘱未执行确认！请确认！");
        //校验是否未生成执行单
        Integer occ = DataBaseHelper.queryForScalar("select count(1) from ex_order_occ where pk_cnord=?", Integer.class, pkCnord);
        if (occ == 0) {
            throw new BusException("没有生成执行单，不允许操作手术完成！");
        }
        logger.info("此处开始修改手术申请单状态手术麻醉一起完成，修改前{}",JsonUtil.writeValueAsString(getOpApply(pkCnord)));

        User u = UserContext.getUser();
        DataBaseHelper.update("update cn_op_apply set eu_status='5',FLAG_FINISH_ANAE='1'  where pk_cnord=? ", new Object[]{pkCnord});
        DataBaseHelper.update("update ex_order_occ set date_occ=?,eu_status='1' ,pk_emp_occ=?,name_emp_occ=?  where pk_cnord=?  and eu_status='0' ", new Object[]{new Date(), u.getPkEmp(), u.getNameEmp(), pkCnord});
        logger.info("此处开始修改手术申请单状态手术麻醉一起完成，修改后{}",JsonUtil.writeValueAsString(getOpApply(pkCnord)));

    }

    private void finishOpOrd(Map<String, Object> m) {
        String pkCnord = m.get("pkCnord") == null ? "" : m.get("pkCnord").toString();
        if (StringUtils.isBlank(pkCnord)) throw new BusException("申请单医嘱主键为空！");
        String pkPv = m.get("pkPv") == null ? "" : m.get("pkPv").toString();
        if (StringUtils.isBlank(pkPv)) throw new BusException("申请单就诊主键为空！");
        //校验申请单下的签署医嘱是否执行：跟需求确认医嘱怎么跟申请单对应上。
        if (!VaildCnOrd(pkPv, m.get("dept").toString())) throw new BusException("手术室还有医嘱未执行确认！请确认！");
        //校验是否未生成执行单
        Integer occ = DataBaseHelper.queryForScalar("select count(1) from ex_order_occ where pk_cnord=?", Integer.class, pkCnord);
        if (occ == 0) {
            throw new BusException("没有生成执行单，不允许操作手术完成！");
        }
        updateOpStatus(pkCnord);
    }

    private void updateOpStatus(String pkCnord) {
        User u = UserContext.getUser();

        logger.info("此处开始修改手术申请单状态，修改前{}",JsonUtil.writeValueAsString(getOpApply(pkCnord)));

        DataBaseHelper.update("update cn_op_apply set eu_status='5' where pk_cnord=? ", new Object[]{pkCnord});
        DataBaseHelper.update("update ex_order_occ set date_occ=?,eu_status='1' ,pk_emp_occ=?,name_emp_occ=?  where pk_cnord=?  and eu_status='0' ", new Object[]{new Date(), u.getPkEmp(), u.getNameEmp(), pkCnord});

        logger.info("此处开始修改手术申请单状态，修改后{}",JsonUtil.writeValueAsString(getOpApply(pkCnord)));

    }

    private CnOpApply getOpApply(String pkCnord){
        CnOpApply cn=DataBaseHelper.queryForBean("select * from CN_OP_APPLY where PK_CNORD=?" ,CnOpApply.class,pkCnord);
        return cn;
    }

    //校验手术室是否有未确认医嘱
    private boolean VaildCnOrd(String pkPv, String pkDeptNs) {
        String sql = "select count(1) from cn_order ord where ord.pk_pv=? and ord.eu_status_ord<='1' and ord.pk_dept_ns=?";
        Integer count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkPv, pkDeptNs});
        if (count > 0) {
            return false;
        }
        return true;
    }

    //校验麻醉科是否有未确认医嘱
    private boolean VaildCnAnaeOrd(String pkPv, String pkCnord) {
        String sql = "select count(1) from cn_order ord where ord.pk_pv=? and ord.eu_status_ord<='1' and ord.pk_dept_ns = ( SELECT pk_dept_anae FROM cn_op_apply op WHERE op.pk_cnord =? ) ";
        Integer count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkPv, pkCnord});
        if (count > 0) {
            return false;
        }
        return true;
    }

    private void setOrdExPlanTime(User u, Map<String, Object> pM) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pkPvs", pM.get("pkpvs"));
        map.put("pkDept", u.getPkDept());
        map.put("pkDeptNs", u.getPkDept());
        String isSure = pM.get("isSure").toString();
        if ("1".equals(isSure)) map.put("isSure", "NotNull");
        List<CnOpSureParam> list = opDao.qrySureOrd(map);
        if (list == null || list.size() <= 0) return;
        List<CnOrder> cnOrds = new ArrayList<CnOrder>();
        for (CnOpSureParam cgVo : list) {
            CnOrder cn = new CnOrder();
            cn.setPkCnord(cgVo.getPkCnord());
            cnOrds.add(cn);
        }
        if (cnOrds == null || cnOrds.size() <= 0)
            throw new BusException("未获取到要设置时间的医嘱列表！");
        String update_sql = "";
        //设置的分钟大于0，按开始时间计算计划执行时间
        if (pM.get("val") != null && !"".equals(pM.get("val").toString()) && CommonUtils.getInteger(pM.get("val").toString()) > 0) {
            if (Application.isSqlServer()) {
                update_sql = "update cn_order set date_plan_ex = dateadd(mi," + CommonUtils.getInteger(pM.get("val")) + ",date_start) ";
            } else {//默认按oracle数据库
                update_sql = "update cn_order set date_plan_ex = date_start+numtodsinterval(" + CommonUtils.getInteger(pM.get("val")) + ",'minute') ";
            }
        } else {//按执行时间设置计划执行时间
            update_sql = "update cn_order set date_plan_ex = to_date(" + CommonUtils.getString(pM.get("dateEx")) + ",'YYYYMMDDHH24MISS') ";
        }
        update_sql = update_sql + " where pk_cnord=:pkCnord";
        DataBaseHelper.batchUpdate(update_sql, cnOrds);
        //DataBaseHelper.execute(update_sql, new Object[]{});
    }

    public void sureOrd(String param, IUser user) {
//		Map<String, Object> receiptMap = JsonUtil.readValue(param, Map.class);
//		List<String> pkpvs=(List<String>)receiptMap.get("pkPvList");
//		List<CnOpSureParam> cnOrdList=(ArrayList<CnOpSureParam>)receiptMap.get("cnOrdList");
        List<BlPubParamVo> cnOrdList = JsonUtil.readValue(param, new TypeReference<List<BlPubParamVo>>() {
        });
        String pkEmpEx = "";//执行人，可修改---20200415
        String NameEmpEx = "";
        List<String> pkpvs = new ArrayList<String>();
        List<String> ordsnList = new ArrayList<String>();
        Map<String, Object> batchNoMap = new HashMap<String, Object>();
        if (cnOrdList != null && cnOrdList.size() > 0) {
            for (BlPubParamVo ord : cnOrdList) {
                ordsnList.add(ord.getOrdsn().toString());
                batchNoMap.put(ord.getOrdsn().toString(), ord.getBatchNo());
                if (!pkpvs.contains(ord.getPkPv())) {
                    pkpvs.add(ord.getPkPv());
                }
            }
            pkEmpEx = cnOrdList.get(0).getPkEmpEx(); //得到传入的执行人
            NameEmpEx = cnOrdList.get(0).getNameEmpEx();
        }
        if (pkpvs == null || pkpvs.size() <= 0) return;
        User u = (User) user;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pkPvs", pkpvs);
        if (ordsnList != null && ordsnList.size() > 0)
            map.put("ordsnList", ordsnList);
        map.put("pkDeptNs", u.getPkDept());
        List<CnOpSureParam> list = opDao.qrySureOrd(map);
        if (list == null || list.size() <= 0) return;
        List<BlPubParamVo> blPubParamVos = new ArrayList<BlPubParamVo>();
        List<CnOrder> cnOrds = new ArrayList<CnOrder>();
        List<String> applyPkCnords = new ArrayList<String>();
        for (CnOpSureParam cgVo : list) {
            if (!"".equals(pkEmpEx)) cgVo.setPkEmpEx(pkEmpEx); //重新给执行人
            if (!"".equals(NameEmpEx)) cgVo.setNameEmpEx(NameEmpEx);
            setCgVO(cgVo, u, blPubParamVos, applyPkCnords);
            cnOrds.add(setCnVO(cgVo, u));
        }

        if (blPubParamVos.size() > 0) {
            for (BlPubParamVo blPubParam : blPubParamVos) {
                blPubParam.setEuBltype("3");//记费类型:手麻
                if (batchNoMap.containsKey(blPubParam.getOrdsn().toString()) && StringUtils.isNotBlank((String) batchNoMap.get(blPubParam.getOrdsn().toString()))) {
                    blPubParam.setBatchNo((String) batchNoMap.get(blPubParam.getOrdsn().toString()));
                }
            }
            cgDao.chargeIpBatch(blPubParamVos, false);
        }
        updateOrdSChecktatus(cnOrds);
        /*手麻开立医嘱确认后执行动作，原本模式：不生成， 如需生成，各项目实现各自所需
         * */
        ExtSystemProcessUtils.processExtMethod("handAnesthesiaExeRec", "handAnesthesiaExeRec", applyPkCnords);
        updateOrdStatus(cnOrds);
        //手术医嘱确认消息ORL^O22
        List<Map<String, Object>> listMap = JsonUtil.readValue(param, List.class);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dtlist", listMap);
        paramMap.put("Control", "OK");
        paramMap.put("type", "O");
        paramMap.put("sureOrd", "");
        PlatFormSendUtils.sendBlMedApplyMsg(paramMap);
    }

    private void updateApplyOrdStatus(List<String> applyPkCnords) {
        if (applyPkCnords == null || applyPkCnords.size() <= 0) return;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pkCnords", applyPkCnords);
        //更新各类申请单状态
        DataBaseHelper.update(getUpdateSql("cn_trans_apply"), map);
        DataBaseHelper.update(getUpdateSql("cn_op_apply"), map);
        DataBaseHelper.update(getUpdateSql("cn_pa_apply"), map);
        DataBaseHelper.update(getUpdateSql("cn_ris_apply"), map);
        DataBaseHelper.update(getUpdateSql("cn_lab_apply"), map);
    }

    private String getUpdateSql(String tableName) {
        return "update " + tableName + "  set eu_status = 1 where pk_cnord  in (:pkCnords) ";
    }

    private void updateOrdSChecktatus(List<CnOrder> cnOrds) {
        if (cnOrds != null && cnOrds.size() > 0)
            DataBaseHelper.batchUpdate("update cn_order set eu_status_ord='2'  where pk_cnord=:pkCnord and del_flag='0' ", cnOrds);
    }

    private void updateOrdStatus(List<CnOrder> cnOrds) {
        if (cnOrds != null && cnOrds.size() > 0)
            DataBaseHelper.batchUpdate("update cn_order set eu_status_ord=:euStatusOrd, pk_emp_chk=:pkEmpChk,name_emp_chk=:nameEmpChk,date_chk=:dateChk,date_last_ex=:dateLastEx where pk_cnord=:pkCnord and del_flag='0' ", cnOrds);
    }

    private CnOrder setCnVO(CnOpSureParam cgVo, User u) {
        CnOrder cn = new CnOrder();
        cn.setEuStatusOrd("3");
        cn.setPkEmpChk(u.getPkEmp());
        cn.setNameEmpChk(u.getNameEmp());
        cn.setDateChk(new Date());
        cn.setTs(cn.getDateChk());
        cn.setDateLastEx(cn.getDateChk());
        cn.setPkCnord(cgVo.getPkCnord());
        return cn;
    }

    private void setCgVO(CnOpSureParam cgVo, User u, List<BlPubParamVo> blPubParamVos, List<String> applyPkCnords) {
        String pkDeptExec = cgVo.getPkDeptEx();
        if (StringUtils.isBlank(pkDeptExec)) {
            throw new BusException(cgVo.getNamePd() + "执行科室不能为空!请检查!");
        }
        buildCgVO(u, cgVo);

        blPubParamVos.add(cgVo);
        applyPkCnords.add(cgVo.getPkCnord());
    }

    private void buildCgVO(User userOp, CnOpSureParam blPubParamVo) {
        String pkOrg = userOp.getPkOrg();
        String pkEmp = userOp.getPkEmp();
        String nameEmp = userOp.getNameEmp();
        blPubParamVo.setPkOrg(pkOrg);
        blPubParamVo.setEuPvType("3");// 住院
        blPubParamVo.setPkOrgApp(blPubParamVo.getPkOrg());// 开立机构
        blPubParamVo.setFlagPv("0"); // 挂号费用标志 false 如果是挂号费用必须为1 否则全为0
        blPubParamVo.setDateHap(new Date()); //服务发生日期
        blPubParamVo.setPkDeptCg(userOp.getPkDept()); //记费科室
        blPubParamVo.setPkEmpCg(pkEmp); //记费人员
        blPubParamVo.setNameEmpCg(nameEmp); //记费人员姓名
        //blPubParamVo.setPkPv(); //已经在VO里
        //blPubParamVo.setPkPi(); //已经在VO里
        //blPubParamVo.setPkOrd(); //已经在VO里
        if (StringUtils.isBlank(blPubParamVo.getPkOrgEx())) {
            blPubParamVo.setPkOrgEx(userOp.getPkOrg());// 执行机构已经在VO里
        }
        //blPubParamVo.setPkDeptEx();// 执行科室 已经在VO里
        //blPubParamVo.setFlagPd(); //已经在VO里
        //blPubParamVo.setQuanCg();// 实际发放数量
        //blPubParamVo.setPkPres();//处方主键 已经在VO里
        //blPubParamVo.setPkCnOrd();// 对应的医嘱主键 已经在VO里
        // blPubParamVo.setPkDeptApp();// 开立科室 已经在VO里
        //blPubParamVo.setPkEmpApp();// 开立医生 已经在VO里
        //blPubParamVo.setNameEmpApp();// 开立医生姓名 已经在VO里
        // blPubParamVo.setPkOrdexdt(); //pkOrdexdt 不存
        //blPubParamVo.setInfantNo(); //婴儿标志，已在VO里
        //blPubParamVo.setPkDeptNsApp(); //开立病区已在VO里
        //blPubParamVo.setPkEmpEx(blPubParamVo.getPkEmpApp()); //执行医生,同开立医生
        //blPubParamVo.setNameEmpEx(blPubParamVo.getNameEmpApp()); //执行医生，同开立医生
        Integer cnt = 1;
        //根据频次计算周期执行次数
        if (!CommonUtils.isEmptyString(blPubParamVo.getCodeFreq())) {
            cnt = DataBaseHelper.queryForScalar("select cnt from bd_term_freq where code = ? and del_flag = '0'", Integer.class, blPubParamVo.getCodeFreq());
        }
        if (cnt == null) cnt = 1;
        //如果是物品，取零售单位，重新计算数量
        if ("1".equals(blPubParamVo.getFlagPd())) {
            // blPubParamVo.setPkUnitPd();  //零售单位(pd的包装单位pk_unit_pack) 药品包装量(pack_size) 在已经在VO里
            Integer day = (blPubParamVo.getDays() == null || blPubParamVo.getDays() == 0) ? 1 : blPubParamVo.getDays();
            double quanCg = MathUtils.mul(blPubParamVo.getQuan(), MathUtils.mul(cnt.doubleValue(), day.doubleValue()));
            blPubParamVo.setQuanCg(Math.ceil(quanCg));
            blPubParamVo.setBatchNo("~");
            blPubParamVo.setDateExpire(new Date());
            double price = blPubParamVo.getPrice() == null ? 0 : blPubParamVo.getPrice();
            Integer packSize = blPubParamVo.getPackSize() == null ? 0 : blPubParamVo.getPackSize();
            Double priceMin = MathUtils.div(price, packSize.doubleValue());
            blPubParamVo.setPrice(priceMin);
            blPubParamVo.setPriceCost(priceMin); //成本单价 ,在这不影响患者记费，不为空就行 ，存零售单价
            blPubParamVo.setPkItem(blPubParamVo.getPkOrd()); // 药品的话此处传pkPd,非药品的话不用传，只要pk_ord有值就好
            blPubParamVo.setPackSize(1);//记费单位存为基本单位
        } else {
            blPubParamVo.setQuanCg(MathUtils.mul(blPubParamVo.getQuan(), cnt.doubleValue()));
        }
    }

    //按明细查询
    public List<Map<String, Object>> qryChargeDetail(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String itemcate = map.get("pkItemcate") == null ? "" : getItemcate(map.get("pkItemcate").toString());
        map.put("pkItemcate", itemcate);
        map.put("pkOrg", ((User) user).getPkOrg());
        if(Application.isSqlServer()){
            map.put("dbType", "sqlserver");
        }else{
            map.put("dbType", "oracle");
        }
        //对日期进行处理
        //map = setDate(map);
        return opDao.qryChargeDetail(map);
    }

    //对日期进行临界处理
    private Map<String, Object> setDate(Map<String, Object> map) {
        Object dateBegin = map.get("dateCgBegin");
        if (dateBegin != null) {
            String dateBeginStr = (String) dateBegin;
            map.put("dateCgBegin", dateBeginStr.substring(0, 8) + "000000");
        }
        Object dateEnd = map.get("dateCgEnd");
        if (dateEnd != null) {
            String dateEndStr = (String) dateEnd;
            map.put("dateCgEnd", dateEndStr.substring(0, 8) + "235959");
        }
        return map;
    }

    private String getItemcate(String itemcates) {
        String rtn = "";
        if (StringUtils.isNotBlank(itemcates)) {
            String[] s = itemcates.split(",");
            if (s != null && s.length > 0) {
                for (String cate : s) {
                    rtn += "'" + cate + "',";
                }
            }
            if (StringUtils.isNotBlank(rtn)) rtn = "(" + rtn.substring(0, rtn.length() - 1) + ")";
        }
        return rtn;
    }

    //按开立科室项目汇总
    public List<Map<String, Object>> qryChargeSum(String param, IUser user) throws Exception {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String itemcate = map.get("pkItemcate") == null ? "" : getItemcate(map.get("pkItemcate").toString());
        map.put("priceOrg", map.get("priceOrg") == null ? "" : map.get("priceOrg").toString());
        map.put("pkItemcate", itemcate);
        map.put("pkOrg", ((User) user).getPkOrg());
        //对日期进行处理
        //map = setDate(map);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        if (map.containsKey("dateCgBegin") && !"".equals(map.get("dateCgBegin"))) {
            Date dateCgBegin = sdf.parse((String) map.get("dateCgBegin"));
            map.put("dateCgBegin", dateCgBegin);
        }
        if (map.containsKey("dateCgEnd") && !"".equals(map.get("dateCgEnd"))) {
            Date dateCgEnd = sdf.parse((String) map.get("dateCgEnd"));
            map.put("dateCgEnd", dateCgEnd);
        }
        return opDao.qryChargeSum(map);
    }

    //按项目汇总
    public List<Map<String, Object>> qryItemSum(String param, IUser user) throws Exception {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String itemcate = map.get("pkItemcate") == null ? "" : getItemcate(map.get("pkItemcate").toString());
        map.put("priceOrg", map.get("priceOrg") == null ? "" : map.get("priceOrg").toString());
        map.put("pkItemcate", itemcate);
        map.put("pkOrg", ((User) user).getPkOrg());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        Date dateCgBegin = sdf.parse((String) map.get("dateCgBegin"));
        Date dateCgEnd = sdf.parse((String) map.get("dateCgEnd"));
        map.put("dateCgBegin", dateCgBegin);
        map.put("dateCgEnd", dateCgEnd);
        return opDao.qryItemSum(map);
    }

    public void ordCreatePres(String param, IUser user) {
        List<CnOrder> cnords = JsonUtil.readValue(param, new TypeReference<List<CnOrder>>() {
        });
        if (cnords == null || cnords.size() <= 0) return;
        List<CnPrescription> presList = new ArrayList<CnPrescription>();
        String pkDiag = qryPkDiag(cnords.get(0).getPkPv());
        for (CnOrder ord : cnords) {
            CnPrescription pres = new CnPrescription();
            try {
                BeanUtils.copyProperties(pres, ord);
            } catch (IllegalAccessException e) {
                throw new BusException(e.getMessage());
            } catch (InvocationTargetException e) {
                throw new BusException(e.getMessage());
            }
            pres.setPkPres(NHISUUID.getKeyId());
            ord.setPkPres(pres.getPkPres());
            pres.setDtProperties("00");
            pres.setDtPrestype(("01".equals(ord.getDtPois()) || "02".equals(ord.getDtPois())) ? "05" : "06"); //05麻醉精一处方 //06精二处方
            pres.setPresNo(ApplicationUtils.getCode("0406"));
            pres.setDatePres(ord.getDateStart());
            pres.setPkDiag(pkDiag);
            pres.setFlagPrt("0");
            presList.add(pres);
        }
        if (presList.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnPrescription.class), presList);
            DataBaseHelper.batchUpdate("update cn_order set pk_pres=:pkPres where ordsn=:ordsn and del_flag='0' ", cnords);
        }
    }

    private String qryPkDiag(String pkPv) {
        if (StringUtils.isBlank(pkPv)) return "";
        String sql = "SELECT pk_diag FROM PV_DIAG WHERE PK_PV=? AND FLAG_MAJ='1' and del_flag='0'";
        List<Map<String, Object>> diagMaps = DataBaseHelper.queryForList(sql, new Object[]{pkPv});
        if (diagMaps == null || diagMaps.size() <= 0) return "";
        return diagMaps.get(0).get("pkDiag").toString();
    }

    //撤销手术：004004011013
    public void backOut(String param, IUser user) {
        Map<String, Object> m = JsonUtil.readValue(param, Map.class);
        String pkCnord = m.get("pkCnord") == null ? "" : m.get("pkCnord").toString();
        if (StringUtils.isBlank(pkCnord)) throw new BusException("申请单医嘱主键为空！");
        String pkPv = m.get("pkPv") == null ? "" : m.get("pkPv").toString();
        if (StringUtils.isBlank(pkPv)) throw new BusException("申请单就诊主键为空！");
        User u = UserContext.getUser();
        String flagZf=MapUtils.getString(m,"flagZf");
        if(StringUtils.isNotEmpty(flagZf) && "1".equals(flagZf)){
            DataBaseHelper.update("update cn_order set eu_status_ord='9',pk_emp_erase =?, name_emp_erase =?, flag_erase = '1',date_erase=? where pk_cnord=? and flag_erase='0' ", new Object[]{u.getPkEmp(),u.getNameEmp(),new Date(), pkCnord});
        }else{
            DataBaseHelper.update("update cn_order set eu_status_ord='1',ts=? where pk_cnord=? ", new Object[]{new Date(), pkCnord});
        }

        DataBaseHelper.update("update cn_op_apply set eu_status='0',ts=? where pk_cnord=? ", new Object[]{new Date(), pkCnord});
        DataBaseHelper.update("update ex_order_occ set date_occ=?,eu_status='9',pk_emp_occ=?,name_emp_occ=?,ts=? where pk_cnord=? ", new Object[]{new Date(), u.getPkEmp(), u.getNameEmp(), new Date(), pkCnord});
    }

    /**
     * 手术医嘱执行确认列表
     * CnIpOpMagementService.medicalAdviceList
     * {"pkPv":"e4bdd4203e76440197b3a24ef92b9350","startDate":"2020/6/2 0:00:00","endDate":"2020/6/2 0:00:00"}
     * 004004011015
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> medicalAdviceList(String param, IUser user){
        Map<String, Object> m = JsonUtil.readValue(param, Map.class);
        String dbType = MultiDataSource.getCurDbType();
        m.put("dbType",dbType);
        List<Map<String,Object>> result = opDao.medicalAdviceList(m);
        return result;
    }

    /**
	 * 根据医嘱写入CA记录
	 */
	private void caRecrdByOrd(List<CnSignCa> caSignList) {
		if(caSignList!=null && caSignList.size()>0){
			 List<CnSignCa> cnSignCa = new ArrayList<CnSignCa>();
			 for(CnSignCa caVo : caSignList){
				 if(caVo!=null){
					 ApplicationUtils.setDefaultValue(caVo, true);
					 caVo.setEuButype("0");
					 cnSignCa.add(caVo);
				 }
			 }
			 if(cnSignCa.size()>0){
				 DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnSignCa.class), cnSignCa);
			 } 
		 }
	}
    
    /**
     * 执行确认
     * CnIpOpMagementService.executionConfirmation
     * 004004011016
     * @param param
     * [{"pkDeptNs":"e90efea76c6148f7aa934b131930d310","codeIp":"ZYBL000441","bedNo":"+13","namePi":"朱一一","pkExocc":"5bc1da69e3af44ddab4fa8f3e2e0c3ee","datePlan":"20200310203040","nameOrd":"一次性引流管","pkCnord":"c463932437e941ec877f2af7d2f6365d","nameFreq":"ONCE","nameSupply":null,"dateOcc":"20200310203040","quanOcc":1.0000,"nameEmpOcc":"pgm","pkEmpOcc":"f288f1380ea44426bda1c0e22cce6b68","euStatus":"0","nameEuStatus":"未执行","nameUnit":null,"nameDeptOcc":"内一科护理单元","flagAp":"0","flagBack":"0","flagDe":"0","flagSelf":"0","flagDrug":"0","noteOrd":null,"nameEmpOrd":"王洁","ordsn":"12629","checked":"1","dateEnter":"20200310203107","pkDeptExec":"e90efea76c6148f7aa934b131930d310"}]
     * @param user
     */
    public void executionConfirmation(String param, IUser user){

        List<Map<String,Object>> cnords = JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>() {});
        if(cnords==null || cnords.size()==0)
            throw new BusException("没有传入执行数据！");
        
        //ca签名认证集合信息
        List<CnSignCa> caList = new ArrayList<>();
        //整理执行单主键
        List<String> pkExoccList = new ArrayList<>();
        for (Map<String,Object> map: cnords)
        {
            if(map.containsKey("pkExocc") && map.get("pkExocc") !=null){
                pkExoccList.add(map.get("pkExocc").toString());
            }
            //CA签名信息
            if(map.containsKey("cnSignCa") && map.get("cnSignCa")!=null){
            	 try {
            		 CnSignCa caVo =JsonUtil.readValue(JsonUtil.writeValueAsString(map.get("cnSignCa")),CnSignCa.class);
            		 caList.add(caVo);
            	 } catch (Exception e) {
     				e.printStackTrace();
     			}
            }
        }
        //调用保存CA签名信息接口
        caRecrdByOrd(caList);

        if(pkExoccList.size()==0){
            throw new BusException("没有传入执行单主键！");
        }

        //参数配置
        Map<String,Object> map=new HashMap<>();
        map.put("pkExoccList",pkExoccList);
        List<ExlistPubVo> exList = opDao.qryExecList(map);
        if(exList == null) return;
        //如果是计费医嘱，调用计费接口
        User u = (User)user;
        Map<String,Object> firtsCnOrd = cnords.get(0);

        for (ExlistPubVo vo: exList)
        {
            vo.setPkEmpOcc(u.getPkEmp());
            vo.setNameEmpOcc(u.getNameEmp());
            if( firtsCnOrd!=null && firtsCnOrd.containsKey("flagBatch") && "1".equals(firtsCnOrd.get("flagBatch").toString())  ){
                vo.setDateOcc(DateUtils.strToDate(firtsCnOrd.get("dateOcc").toString(),"yyyyMMddHHmmss"));
            }else{
                vo.setDateOcc(vo.getDatePlan());
            }
        }

        //调用公共执行计费服务
        List<BlPubParamVo> gclist = blCgExPubService.execAndCg(exList, u);

        String sql ="update cn_order set date_plan_ex=:dateOcc,pk_emp_ex=:pkEmpOcc,name_emp_ex=:nameEmpOcc where pk_cnord=:pkCnord ";
        DataBaseHelper.batchUpdate(sql,exList);
    }
    
    /**
     * 查询当前病人医嘱中检查的执行状态
     * IOpMagementConfirmService.GetRisStatus
     * 004004011021
     * @param param
     * @param user
     */
    public List<Map<String,Object>> getRisStatus(String param, IUser user)
    {
		List<String> ords=(List<String>)JsonUtil.readValue(param, new TypeReference<List<String>>() {});
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("ords",ords);
        List<Map<String,Object>> rtList = opDao.GetRisStatus(paramMap);
		return rtList;

    }
    

    /**
     * 取消执行
     * @param param
     * @param user
     */
    public void cancelEx(String param, IUser user){
        List<Map<String,Object>> cnords = JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>() {});
        if(cnords==null || cnords.size()==0)
            throw new BusException("没有传入已执行数据！");

        //整理执行单主键
        List<String> pkExoccList = new ArrayList<>();
        List<String> consultSqlList = new ArrayList<String>();
        List<String> orderSqlList = new ArrayList<String>();
        for (Map<String,Object> map: cnords)
        {
            if(map.containsKey("pkExocc") && map.get("pkExocc") !=null){
                pkExoccList.add(map.get("pkExocc").toString());
                consultSqlList.add("update EX_ORDER_OCC set eu_status='0',flag_canc='0',DATE_OCC=null,PK_EMP_OCC=null,NAME_EMP_OCC=null,pk_cg=null,PK_CG_CANCEL=null,DATE_CANC=null,PK_DEPT_CANC=null where pk_exocc= '"+map.get("pkExocc").toString()+"'");
            }
            if(map.containsKey("pkCnord") && map.get("pkCnord") !=null){
                orderSqlList.add("update cn_order set date_plan_ex=null,pk_emp_ex=null,name_emp_ex=null where pk_cnord='"+map.get("pkCnord").toString()+"'");
            }
        }

        if(pkExoccList.size()==0){
            throw new BusException("没有传入执行单主键！");
        }
        //参数配置
        Map<String,Object> map=new HashMap<>();
        map.put("pkExoccList",pkExoccList);
        List<ExlistPubVo> exList = opDao.qryExecList(map);
        if(exList == null) return;
        blCgExPubService.cancelExAndRtnCg(exList, (User) user);

        //恢复状态
        if(null != consultSqlList && consultSqlList.size() > 0)
            DataBaseHelper.batchUpdate(consultSqlList.toArray(new String[0]));

        //恢复医嘱表数据
        if(null != orderSqlList && orderSqlList.size() > 0)
            DataBaseHelper.batchUpdate(orderSqlList.toArray(new String[0]));
    }
    
    /**
     * 保存退费
     * 004004011017
     * @param param
     * @param user
     */
    public void saveRefund(String param, IUser user) {
        List<Map<String,Object>> refundList = JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>() {});
        if(refundList==null || refundList.size()==0){
        	 throw new BusException("没有传入待退费数据！");
        }    
    	Set<String> pkCgips = new HashSet<String>();
    	Set<String> pkPvs = new HashSet<String>();
    	List<RefundVo> refundVos = new ArrayList<RefundVo>();
    	//1、组织退费数据
    	for (Map<String,Object> map: refundList)
        {
    		RefundVo vo = new RefundVo();
    		if(CommonUtils.isNull(map.get("pkCgip"))){
    			throw new BusException("没有传入退费主键！");
    		}
    		if(CommonUtils.isNull(map.get("pkPv"))){
    			throw new BusException("没有传入患者就诊主键！");
    		}
    		if(CommonUtils.isNull(map.get("quan"))){
    			throw new BusException("没有传入退费数量！");
    		}
            pkCgips.add(map.get("pkCgip").toString());
            pkPvs.add(map.get("pkPv").toString());
            vo.setPkCgip(map.get("pkCgip").toString());
            vo.setQuanRe(CommonUtils.getDouble(map.get("quan")));
            refundVos.add(vo);
        }
		//2、退费校验
    	String pvSql = "select count(1) from pv_encounter  where  flag_in = '0' "
				+ " and pk_pv in (" + CommonUtils.convertSetToSqlInPart(pkPvs, "pk_pv") + ")";
		Integer pvCount = DataBaseHelper.queryForScalar(pvSql, Integer.class);
		if(pvCount > 0){	
			throw new BusException("有患者已出院结算，无法退费！");
		}
		String cgReSql = "select count(1) from bl_ip_dt  where pk_cgip_back in (" + CommonUtils.convertSetToSqlInPart(pkCgips, "pk_cgip") + ")";
		Integer cgReCount = DataBaseHelper.queryForScalar(cgReSql, Integer.class);
		if(cgReCount > 0){	
			throw new BusException("退费列表存在已退费项目，无法退费！");
		}
		String cgSettleSql = "select count(1) from bl_ip_dt  where flag_settle='1' and pk_cgip in (" + CommonUtils.convertSetToSqlInPart(pkCgips, "pk_cgip") + ")";
		Integer cgSettleCount = DataBaseHelper.queryForScalar(cgSettleSql, Integer.class);
		if(cgSettleCount > 0){	
			throw new BusException("退费列表存在已结算项目，无法退费！");
		}
        //3、退费
        cgDao.refundInBatch(refundVos);
    }
    
}
