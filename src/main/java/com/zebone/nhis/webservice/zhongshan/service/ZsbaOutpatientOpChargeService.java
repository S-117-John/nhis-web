package com.zebone.nhis.webservice.zhongshan.service;

import com.zebone.nhis.bl.pub.service.ExAssistPubService;
import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnTransApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOccDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.pv.pub.dao.RegPubMapper;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.zhongshan.dao.ThiIpCnWebMapper;
import com.zebone.nhis.webservice.zhongshan.vo.*;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname ZsbaOutpatientOpChargeService
 * @Description ????????????????????????-???????????????
 * @Date 2021-03-04 19:09
 * @Created by wuqiang
 */
@Service
public class ZsbaOutpatientOpChargeService {
    private Logger logger = LoggerFactory.getLogger("nhis.zsbaWebSrv");
    @Autowired
    private RegPubMapper regMapper;
    @Autowired
    private OpCgPubService opCgPubService;
    @Resource
    private BdSnService bdSnService;
    @Resource
    private ThiIpCnWebMapper thiIpCnWebMapper;
    @Resource
    private IpCgPubService ipCgPubService;
    @Autowired
    private ExAssistPubService settlePdOutService; // ?????????????????????
    public String opCharge(String inf) {
        String ret;
        OutpatientOpChargeVo outpatientOpChargeVo = JsonUtil.readValue(inf, OutpatientOpChargeVo.class);
        //????????????
        if (outpatientOpChargeVo == null) {
            ret = " ZsbaOutpatientOpChargeService-OpCharge: -1|???????????? " + " ";
            return new RespJson(ret, false).toString();
        }
        Date date = new Date();
        try {
            ret = vilaFiled(outpatientOpChargeVo);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|???????????? " + outpatientOpChargeVo.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
            date = DateUtils.parseDate(outpatientOpChargeVo.getDate_hap());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|?????????????????? " + outpatientOpChargeVo.toString());
            return new RespJson("-1|??????????????????", false).toString();
        } catch (ParseException e) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|?????????????????? " + outpatientOpChargeVo.toString());
        }
        if (Integer.valueOf(outpatientOpChargeVo.getQuan()) <= 0) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|??????????????????0 " + outpatientOpChargeVo.toString());
            return new RespJson("-1|???????????????????????????0", false).toString();
        }
        PiMaster piMaster = getPiMaster(outpatientOpChargeVo.getCode_op());

        if (piMaster == null) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|??????????????????????????? " + outpatientOpChargeVo.toString());
            return new RespJson("-1|???????????????????????????", false).toString();
        }
        ThiBdOrdLab thiBdOrdLab = getThiBdOrdLab(outpatientOpChargeVo.getCode_item());
        if (thiBdOrdLab == null) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|??????????????????????????? " + outpatientOpChargeVo.toString());
            return new RespJson("-1|??????????????????,??????????????????-" + outpatientOpChargeVo.getCode_item(), false).toString();
        }
       String sql = "select B.PK_ORG, PK_DEPT, CODE_DEPT, NAME_DEPT " +
                "from BD_OU_DEPT BOD " +
                "         inner join BD_OU_ORG B ON B.PK_ORG = BOD.PK_ORG " +
                "where B.CODE_ORG = ? and CODE_DEPT = ?";
        BdOuDept bdOuDeptOpen = DataBaseHelper.queryForBean(sql, BdOuDept.class,
                new Object[]{outpatientOpChargeVo.getCode_org_app(), outpatientOpChargeVo.getCode_dept_app()});
        if (bdOuDeptOpen == null) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|????????????????????? " + outpatientOpChargeVo.toString());
            return new RespJson("-1|?????????????????????,?????????????????????/??????????????????", false).toString();
        }
        BdOuDept bdOuDeptEx = DataBaseHelper.queryForBean(sql, BdOuDept.class,
                new Object[]{outpatientOpChargeVo.getCode_org_ex(), outpatientOpChargeVo.getCode_dept_ex()});
        if (bdOuDeptEx == null) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|????????????????????? " + outpatientOpChargeVo.toString());
            return new RespJson("-1|?????????????????????,?????????????????????/??????????????????", false).toString();
        }
        sql = "select PK_DEPT,CODE_DEPT ,NAME_DEPT " +
                "from BD_OU_DEPT BOD where  CODE_DEPT=?";
        BdOuDept bdOuDeptJob = DataBaseHelper.queryForBean(sql, BdOuDept.class,
                new Object[]{outpatientOpChargeVo.getCode_dept_job()});
        if (bdOuDeptJob == null) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|??????????????????????????? " + outpatientOpChargeVo.toString());
            return new RespJson("-1|???????????????????????????,???????????????????????????", false).toString();
        }
        BdOuDept bdOuDeptCg = DataBaseHelper.queryForBean(sql, BdOuDept.class,
                new Object[]{outpatientOpChargeVo.getCode_dept_cg()});
        if (bdOuDeptCg == null) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|????????????????????? " + outpatientOpChargeVo.toString());
            return new RespJson("-1|?????????????????????,???????????????????????????", false).toString();
        }
        sql = "select BOE.PK_EMP,BOE.NAME_EMP,BOE.CODE_EMP " +
                "from BD_OU_USER BOU " +
                "         inner join BD_OU_EMPLOYEE BOE ON BOU.PK_EMP=BOE.PK_EMP  " +
                "WHERE CODE_USER=?";
        BdOuEmployee bdOuEmployeeDoc = DataBaseHelper.queryForBean(sql, BdOuEmployee.class, outpatientOpChargeVo.getCode_emp_app());
        if (bdOuEmployeeDoc == null) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|????????????????????? " + outpatientOpChargeVo.toString());
            return new RespJson("-1|?????????????????????,???????????????????????????", false).toString();
        }
        BdOuEmployee bdOuEmployeeDocEx = DataBaseHelper.queryForBean(sql, BdOuEmployee.class, outpatientOpChargeVo.getCode_emp_ex());
        if (bdOuEmployeeDocEx == null) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|????????????????????? " + outpatientOpChargeVo.toString());
            return new RespJson("-1|?????????????????????,???????????????????????????", false).toString();
        }
        BdOuEmployee bdOuEmployeeDocCg = DataBaseHelper.queryForBean(sql, BdOuEmployee.class, outpatientOpChargeVo.getCode_emp_cg());
        if (bdOuEmployeeDocCg == null) {
            logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|????????????????????? " + outpatientOpChargeVo.toString());
            return new RespJson("-1|?????????????????????,???????????????????????????", false).toString();
        }
        sql = "select top 1 PK_HP, NAME, CODE from BD_HP BH " +
                "where DEL_FLAG = '0' and (CODE = ? or EU_HPTYPE = '0') " +
                "order by EU_HPTYPE desc";
        BdHp bdHp = DataBaseHelper.queryForBean(sql, BdHp.class, outpatientOpChargeVo.getCode_hp());
        User user = new User();
        user.setPkOrg(bdOuDeptOpen.getPkOrg());
        user.setPkEmp(bdOuEmployeeDoc.getPkEmp());
        user.setNameEmp(bdOuEmployeeDoc.getNameEmp());
        UserContext.setUser(user);
        //????????????????????????
        PvEncounter pvEncount = savePvEncount(1,piMaster, bdHp, bdOuDeptOpen, bdOuEmployeeDoc, date);
        //??????????????????????????????
        savePvOp(pvEncount);//??????????????????
        //????????????????????????
        savePvInsurance(pvEncount);
        CnOrder cnOrder = saveCnorder(outpatientOpChargeVo, pvEncount, thiBdOrdLab, bdOuDeptEx, bdOuDeptCg, bdOuEmployeeDocEx, bdOuEmployeeDocCg, bdOuDeptJob);
        CnLabApply cnLabApply = saveCnLabApply(cnOrder, thiBdOrdLab);
        //??????????????????
        saveBlOpDt(Double.valueOf(outpatientOpChargeVo.getQuan() ),pvEncount, null, cnOrder, bdOuDeptEx, bdOuDeptCg, bdOuEmployeeDocEx, bdOuEmployeeDocCg, bdOuDeptJob);
        Map<String, String> map = new HashMap<>(1);
        map.put("codePv", pvEncount.getCodePv());
        return new RespJson("0|??????|" + JsonUtil.writeValueAsString(map), true).toString();
    }

    public PiMaster getPiMaster(String codeOp) {
        String sql = "select PK_PI,CODE_OP,NAME_PI,DT_SEX,BIRTH_DATE,DT_MARRY," +
                "       DT_SOURCE,ADDRESS,PK_PICATE,NAME_REL,IDNO_REL, " +
                "       TEL_REL from PI_MASTER PM where CODE_OP = ?";
        PiMaster piMaster = DataBaseHelper.queryForBean(sql, PiMaster.class, codeOp);
        return piMaster;
    }

    private CnLabApply saveCnLabApply(CnOrder cnOrder, ThiBdOrdLab thiBdOrdLab) {
        CnLabApply cnLabApply = new CnLabApply();
        cnLabApply.setPkCnord(cnOrder.getPkCnord());
        cnLabApply.setDtColtype(thiBdOrdLab.getDtColltype()); //????????????
        cnLabApply.setDtSamptype(thiBdOrdLab.getDtSamptype());
        cnLabApply.setDtTubetype(thiBdOrdLab.getDtContype());
        cnLabApply.setDelFlag("0");
        cnLabApply.setEuStatus("0");//???????????????
        cnLabApply.setNote("??????????????????");//??????
        cnLabApply.setPurpose(cnOrder.getPurpose());//????????????
        cnLabApply.setPkOrg(cnOrder.getPkOrg());
        ApplicationUtils.setDefaultValue(cnLabApply, true);
        DataBaseHelper.insertBean(cnLabApply);
        return cnLabApply;
    }


    private CnOrder saveCnorder(OutpatientOpChargeVo outpatientOpChargeVo, PvEncounter pvEncount, ThiBdOrdLab thiBdOrdLab, BdOuDept bdOuDeptEx, BdOuDept bdOuDeptCg, BdOuEmployee bdOuEmployeeDocEx, BdOuEmployee bdOuEmployeeDocCg, BdOuDept bdOuDeptJob) {
        CnOrder cnOrder = new CnOrder();
        Date date = new Date();
        int ordSn = bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, UserContext.getUser());
        cnOrder.setOrdsnChk(ordSn);
        cnOrder.setDescFit("0");
        cnOrder.setEuPvtype(pvEncount.getEuPvtype());
        cnOrder.setPkPv(pvEncount.getPkPv());
        cnOrder.setPkPi(pvEncount.getPkPi());
        cnOrder.setDateEffe(date);
        cnOrder.setCodeOrdtype(thiBdOrdLab.getCodeordtype());
        cnOrder.setEuAlways("1");
        cnOrder.setOrdsn(ordSn);
        cnOrder.setOrdsnParent(ordSn);
        cnOrder.setPkOrd(thiBdOrdLab.getPkOrd());
        cnOrder.setCodeOrd(thiBdOrdLab.getCodeord());
        cnOrder.setQuanDisp(0.0D);
        cnOrder.setNameOrd(thiBdOrdLab.getName());
        cnOrder.setCodeApply(ApplicationUtils.getCode("0402"));
        cnOrder.setCodeFreq(StringUtils.isBlank(thiBdOrdLab.getCodeFreq()) ? "QD" : thiBdOrdLab.getCodeFreq());
        cnOrder.setDosage(0.0D);
        Double quan = StringUtils.isBlank(outpatientOpChargeVo.getQuan()) ? 1.0D : Double.valueOf(outpatientOpChargeVo.getQuan());
        cnOrder.setQuan(quan);
        cnOrder.setQuanCg(quan);
        cnOrder.setPackSize(0.0D);
        cnOrder.setPriceCg(thiBdOrdLab.getPrice());
        cnOrder.setDays(1L);
        cnOrder.setOrds(1L);
        cnOrder.setPkOrgExec(bdOuDeptEx.getPkOrg());
        cnOrder.setPkDeptExec(bdOuDeptEx.getPkDept());
        cnOrder.setEuStatusOrd("0");
        cnOrder.setDateEnter(date);
        cnOrder.setDateStart(date);
        cnOrder.setFlagBl("1");
        cnOrder.setPkDept(pvEncount.getPkDept());
        cnOrder.setPkEmpInput(pvEncount.getPkEmpPhy());
        cnOrder.setNameEmpInput(pvEncount.getEmpName());
        cnOrder.setPkEmpOrd(pvEncount.getPkEmpPhy());
        cnOrder.setNameEmpOrd(pvEncount.getEmpName());
        cnOrder.setDateSign(date);
        cnOrder.setFlagDoctor("1");
        cnOrder.setPkEmpEntry(pvEncount.getPkEmpPhy());
        cnOrder.setNameEmpEntry(pvEncount.getNameEmpPhy());
        cnOrder.setDateEntry(date);
        cnOrder.setGroupno(1);
        cnOrder.setFlagOcc("0");
        ApplicationUtils.setDefaultValue(cnOrder, true);
        DataBaseHelper.insertBean(cnOrder);
        return cnOrder;
    }

    /**
     * @return com.zebone.nhis.webservice.zhongshan.vo.ThiBdOrdLab
     * @Description ????????????????????????
     * @auther wuqiang
     * @Date 2021-06-09
     * @Param [codeItem]
     */
    private ThiBdOrdLab getThiBdOrdLab(String codeItem) {
        return thiIpCnWebMapper.getThiBdOrdLab(codeItem);
    }

    public String deleteOpCharge(String inf) {
        Map<String, Object> map = JsonUtil.readValue(inf, Map.class);
        String codePv = MapUtils.getString(map, "codePv");
        if (StringUtils.isBlank(codePv)) {
            logger.error(" ZsbaOutpatientOpChargeService-DeleteOpCharge: -1|???????????????????????????codePv ");
            return new RespJson("-1|???????????????????????????codePv", false).toString();
        }
        PvEncounter pvEncounter = getPvconter(codePv,null,"1");
        if (pvEncounter == null) {
            logger.error("ZsbaOutpatientOpChargeService-DeleteOpCharge: -1|??????????????????????????????????????????????????????????????? {}", codePv);
            return new RespJson("-1|?????????????????????????????????", false).toString();
        }
        thiIpCnWebMapper.deleteCnorder(pvEncounter.getPkPv());
        thiIpCnWebMapper.deleteCnLabApply(pvEncounter.getPkPv());
        thiIpCnWebMapper.deleteExAssistOcc(pvEncounter.getPkPv());
        thiIpCnWebMapper.deleteExAssistOccDt(pvEncounter.getPkPv());
        String sql = "update PV_ENCOUNTER set EU_STATUS = '9' where PK_PV = ?";
        DataBaseHelper.execute(sql,new Object[]{pvEncounter.getPkPv()});
        int cou = DataBaseHelper.execute("DELETE BL_OP_DT where PK_PV=? and FLAG_SETTLE='0' ", new Object[]{pvEncounter.getPkPv()});
        if (cou > 0) {
            return new RespJson("0|??????", false).toString();
        }
        return new RespJson("-1|????????????????????????????????????", false).toString();
    }

    /**
     * @return void
     * @Description ??????????????????
     * @auther wuqiang
     * @Date 2021-03-06
     * @Param [outpatientOpChargeVo, pvEncount, bdItem, bdOuDeptEx, bdOuDeptCg, bdOuEmployeeDocEx, bdOuEmployeeDocCg, bdOuDeptJob]
     */
    public void saveBlOpDt(Double quan, PvEncounter pvEncount, BdItem bdItem, CnOrder cnOrder, BdOuDept bdOuDeptEx, BdOuDept bdOuDeptCg, BdOuEmployee bdOuEmployeeDocEx, BdOuEmployee bdOuEmployeeDocCg, BdOuDept bdOuDeptJob) {
        List<BlPubParamVo> blOpItem = new ArrayList<BlPubParamVo>();
        BlPubParamVo bpb = new BlPubParamVo();
        bpb.setFlagPv("0");
        bpb.setPkOrg(pvEncount.getPkOrg());
        bpb.setEuPvType(pvEncount.getEuPvtype());
        bpb.setPkPv(pvEncount.getPkPv());
        bpb.setPkPi(pvEncount.getPkPi());
        if (cnOrder != null) {
            bpb.setPkCnord(cnOrder.getPkCnord());
            bpb.setDtSamptype(cnOrder.getDtSamptype());
            bpb.setCodeOrdtype(cnOrder.getCodeOrdtype());
            bpb.setPkOrd(cnOrder.getPkOrd());
            bpb.setOrdsn(cnOrder.getOrdsn());
            bpb.setOrdsnParent(cnOrder.getOrdsnParent());
            bpb.setQuanCg(cnOrder.getQuanCg());
        }
        if (bdItem != null) {
            bpb.setPkItem(bdItem.getPkItem());
            bpb.setQuanCg(quan);
        }
        bpb.setFlagPd("0");
        bpb.setEuAdditem("0");
        bpb.setPkOrgEx(bdOuDeptEx.getPkDept());
        bpb.setPkOrgEx(bdOuDeptEx.getPkOrg());
        bpb.setPkOrgApp(pvEncount.getPkOrg());
        bpb.setPkDeptApp(pvEncount.getPkDept());
        bpb.setPkDeptEx(bdOuDeptEx.getPkDept());
        bpb.setPkDeptCg(bdOuDeptCg.getPkDept());
        bpb.setDateHap(pvEncount.getDateBegin());
        bpb.setDateCg(new Date());
        bpb.setPkEmpCg(bdOuEmployeeDocCg.getPkEmp());
        bpb.setNameEmpCg(bdOuEmployeeDocCg.getNameEmp());
        bpb.setPkEmpEx(bdOuEmployeeDocEx.getPkEmp());
        bpb.setNameEmpEx(bdOuEmployeeDocEx.getNameEmp());
        bpb.setPkDeptJob(bdOuDeptJob.getPkDept());
        blOpItem.add(bpb);
        if (blOpItem.size() > 0) {
            BlPubReturnVo vo = opCgPubService.blOpCgBatch(blOpItem);
            List<BlOpDt> exDtList = vo.getBods();
            if (exDtList != null && exDtList.size() > 0) {
                exDtList.forEach(m -> {
                    m.setFlagPd("0");
                });
                settlePdOutService.generateExAssistOcc(exDtList);
            }
        }
    }

    /**
     * @return void
     * @Description ??????????????????
     * @auther wuqiang
     * @Date 2021-03-06
     * @Param [pv]
     */
    public void savePvInsurance(PvEncounter pv) {
        PvInsurance insu = new PvInsurance();
        insu.setPkPvhp(NHISUUID.getKeyId());
        insu.setPkOrg(pv.getPkOrg());
        insu.setCreator(pv.getPkEmpPhy());
        insu.setCreateTime(new Date());
        insu.setTs(new Date());
        insu.setPkPv(pv.getPkPv());
        insu.setPkHp(pv.getPkInsu());
        DataBaseHelper.insertBean(insu);
    }

    /**
     * @return void
     * @Description ??????????????????
     * @auther wuqiang
     * @Date 2021-03-06
     * @Param [pvEncount ]
     */
    public PvOp savePvOp(PvEncounter pv) {
        PvOp pvOp = new PvOp();
        pvOp.setPkPv(pv.getPkPv());
        String val = ApplicationUtils.getSysparam("PV0056", false);
        Integer opTimes;
        if ("2".equals(val)) {
            opTimes = regMapper.getMaxOpTimesFromPiMaster(pv.getPkPi());
        } else {
            opTimes = regMapper.getMaxOpTimes(pv.getPkPi());
        }
        pvOp.setOpTimes(new Long(opTimes + 1));
        pvOp.setPkSchsrv(null);
        pvOp.setPkRes(null);
        pvOp.setPkDateslot(null);
        pvOp.setPkDeptPv(pv.getPkDept());
        pvOp.setPkEmpPv(pv.getPkEmpPhy());
        pvOp.setNameEmpPv(pv.getNameEmpPhy());
        pvOp.setPkResCn(null);
        pvOp.setPkSchsrvCn(null);
        pvOp.setTicketno(null);
        pvOp.setPkSch(null);
        pvOp.setFlagFirst("1"); // ??????
        pvOp.setEuRegtype("1"); //????????????
        // ?????????????????????????????????????????????????????????????????????:date_end=to_date(date_begin,'yyyymmdd') +
        // ( ??????-1) || '23:59:59'
        pvOp.setDateBegin(pv.getDateBegin());
        pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pv.getDateBegin()));
        DataBaseHelper.insertBean(pvOp);
        DataBaseHelper.update("update PI_MASTER set CNT_OP=? where PK_PI=?", new Object[]{opTimes + 1, pv.getPkPi()});
        return pvOp;
    }

    /***
     * @Description ??????????????????
     * @auther wuqiang
     * @Date 2021-03-06
     * @Param [master, bdHp]
     * @return com.zebone.nhis.common.module.pv.PvEncounter
     */
    public PvEncounter savePvEncount(int serNum,PiMaster master, BdHp bdHp, BdOuDept bdOuDeptOpen, BdOuEmployee bdOuEmployeeDoc, Date d) {
        // ??????????????????
        PvEncounter pvEncounter = new PvEncounter();
        pvEncounter.setPkOrg(bdOuDeptOpen.getPkOrg());
        pvEncounter.setPkPv(NHISUUID.getKeyId());
        pvEncounter.setPkPi(master.getPkPi());
        pvEncounter.setPkInsu(bdHp.getPkHp());
        pvEncounter.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
        if (serNum==14){
            pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_4); //??????????????????
            pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_1); // ??????
        } else {
            pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_1); //??????????????????
            pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_2); // ??????
        }
        pvEncounter.setFlagSpec("0");
        pvEncounter.setEuLocked("0");
        pvEncounter.setDateClinic(d);
        pvEncounter.setPkDept(bdOuDeptOpen.getPkDept());//????????????
        pvEncounter.setPkEmpPhy(bdOuEmployeeDoc.getPkEmp());
        pvEncounter.setNameEmpPhy(bdOuEmployeeDoc.getNameEmp());
        pvEncounter.setDateBegin(d);
        pvEncounter.setDateEnd(d);
        pvEncounter.setDateReg(d);
        pvEncounter.setPkEmpReg(bdOuEmployeeDoc.getPkEmp());
        pvEncounter.setNameEmpReg(bdOuEmployeeDoc.getNameEmp());
        pvEncounter.setNamePi(master.getNamePi());
        pvEncounter.setDtSex(master.getDtSex());
        pvEncounter.setAgePv(DateUtils.getAgeByBirthday(master.getBirthDate(), pvEncounter.getDateBegin()));
        pvEncounter.setAddress(master.getAddress());
        pvEncounter.setFlagIn("0");
        pvEncounter.setFlagSettle("0");
        pvEncounter.setDtMarry(master.getDtMarry());
        pvEncounter.setPkPicate(master.getPkPicate());
        pvEncounter.setFlagCancel("0");
        pvEncounter.setDtIdtypeRel("01");
        pvEncounter.setDtPvsource(master.getDtSource());
        pvEncounter.setNameRel(master.getNameRel());
        pvEncounter.setIdnoRel(master.getIdnoRel());
        pvEncounter.setTelRel(master.getTelRel());
        pvEncounter.setEuPvmode("0");
        pvEncounter.setEuStatusFp("0");
        pvEncounter.setEuDisetype("0");
        DataBaseHelper.insertBean(pvEncounter);
        return pvEncounter;
    }


    /**
     * @return java.lang.String
     * @Description ??????????????????????????????
     * @auther wuqiang
     * @Date 2021-03-06
     * @Param [t]
     */
    public static <T> String vilaFiled(T t) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        if (null == t) {
            stringBuilder.append("????????????????????????");
            return stringBuilder.toString();
        }
        Class<?> aClass = t.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        //??????????????????
        for (Field field : declaredFields) {
            //??????????????????
            field.setAccessible(true);
            Annotation annotation = field.getDeclaredAnnotation(NotBlank.class);
            //?????????????????????????????? ????????????
            if (annotation == null) {
                continue;
            }
            Object o = field.get(t);
            //????????????????????????
            NotBlank notNull = (NotBlank) annotation;
            if (o == null || (field.getType() == String.class && StringUtils.isBlank((String) o))) {
                if (StringUtils.isNotBlank(notNull.message())) {
                    stringBuilder.append(field.getName());
                    stringBuilder.append(notNull.message());
                    stringBuilder.append(",");
                } else {
                    stringBuilder.append(field.getName());
                    stringBuilder.append("??????");
                    stringBuilder.append(",");
                }
            } else if (field.getType() == java.util.List.class) {
                List<?> objects = (List<?>) o;
                for (Object object : objects) {
                    stringBuilder.append(vilaFiled(object));
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * @return java.lang.String
     * @Description 02????????????????????????
     * @auther wuqiang
     * @Date 2021-04-09
     * @Param [inf]
     */
    public String saveCnOrder(String inf) {
        String ret;
        ThiCnOrder thiCnOrder = JsonUtil.readValue(getIniParam(inf), ThiCnOrder.class);
        //????????????
        if (thiCnOrder == null) {
            ret = " ZsbaOutpatientOpChargeService-SaveCnOrDer: -1|???????????? " + " ";
            return new RespJson(ret, false).toString();
        }
        Date dateStart = new Date();
        try {
            ret = vilaFiled(thiCnOrder);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" ZsbaOutpatientOpChargeService-SaveCnOrDer: -1|???????????? " + thiCnOrder.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
            dateStart = DateUtils.parseDate(thiCnOrder.getDateStart());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" ZsbaOutpatientOpChargeService-SaveCnOrDer: -1|?????????????????? " + thiCnOrder.toString());
            return new RespJson("-1|??????????????????", false).toString();
        } catch (ParseException e) {
            logger.error(" ZsbaOutpatientOpChargeService-SaveCnOrDer: -1|?????????????????? " + thiCnOrder.toString());
        }
        PvEncounter pvEncounter = getPvconter(null, thiCnOrder.getPkPv(), "3");
        if (pvEncounter == null) {
            logger.error(" ZsbaOutpatientOpChargeService-SaveCnOrDer: -1|??????????????????????????????????????????????????????????????? " + thiCnOrder.toString());
            return new RespJson("-1|???????????????????????????????????????????????????????????????", false).toString();
        }
        BdOrd bdOrd = getBdOrd(thiCnOrder.getPkOrd());
        if(null == bdOrd) {
        	logger.error(" ZsbaOutpatientOpChargeService-SaveCnOrDer: -1| ???????????????????????????" + thiCnOrder.toString());
            return new RespJson("-1|???????????????????????????,???????????????????????????", false).toString();
        }
        BdOuDept bdOuDeptEex = getBdOuDept(pvEncounter.getPkOrg(),null, thiCnOrder.getPkDeptExec());
        if (bdOuDeptEex == null) {
            logger.error(" ZsbaOutpatientOpChargeService-SaveCnOrDer: -1| ?????????????????????" + thiCnOrder.toString());
            return new RespJson("-1|?????????????????????,???????????????????????????", false).toString();
        }
        
        BdOuDept bdOuDept = getBdOuDept(pvEncounter.getPkOrg(),null, thiCnOrder.getPkDept());
        if (bdOuDept == null) {
            logger.error(" ZsbaOutpatientOpChargeService-SaveCnOrDer: -1| ?????????????????????" + thiCnOrder.toString());
            return new RespJson("-1|?????????????????????,???????????????????????????", false).toString();
        }
        BdOuEmployee bdOuEmployeeDoctor = getBdOuEmployee(null,thiCnOrder.getPkEmpOrd());
        if (bdOuEmployeeDoctor == null) {
            logger.error(" ZsbaOutpatientOpChargeService-SaveCnOrDer: -1| ?????????????????????" + thiCnOrder.toString());
            return new RespJson("-1|?????????????????????,?????????????????????????????????", false).toString();
        }
        String pkUnitBt = getPkUnit(thiCnOrder.getCodeUnitBt());
        if (StringUtils.isEmpty(pkUnitBt)) {
            logger.error(" ZsbaOutpatientOpChargeService-SaveCnOrDer: -1| ???????????????" + thiCnOrder.toString());
            return new RespJson("-1|????????????????????????,????????????????????????????????????", false).toString();
        }else {
        	thiCnOrder.setPkUnitBt(pkUnitBt);
        }
        User user = getDefaultUser(null,thiCnOrder.getPkEmpOrd());
        if(!BeanUtils.isNotNull(user)){
            return new RespJson("-1|???????????????????????????", false).toString();
        }
        UserContext.setUser(user);
        CnOrder cnOrder = creatCnOrder(thiCnOrder, pvEncounter, bdOuDept, bdOuDeptEex, bdOuEmployeeDoctor, dateStart,bdOrd);
        try {
            int result = DataBaseHelper.insertBean(cnOrder);

            if(result > 0) {
                // ??????????????????????????????
                saveExAssOccAndExAssOccDt(cnOrder, thiCnOrder);
            }
            
            CnTransApply cnt = new CnTransApply();
            cnt.setBtContent(thiCnOrder.getBtContent());//????????????
            cnt.setDatePlan(DateUtils.parseDate(thiCnOrder.getDatePlan(),"yyyy-MM-dd"));
            cnt.setDtBtAbo(thiCnOrder.getDtBtAbo());
            cnt.setDtBtRh(thiCnOrder.getDtBtRh());
            cnt.setDtBttype(thiCnOrder.getDtBttype());
            cnt.setEuPreAborh(thiCnOrder.getEuPreAborh());
            cnt.setFlagAl(thiCnOrder.getFlagAl());
            cnt.setFlagLab(thiCnOrder.getFlagLab());
            cnt.setFlagPreg(thiCnOrder.getFlagPreg());
            cnt.setFlagBthis(thiCnOrder.getFlagBthis());
            cnt.setNote(thiCnOrder.getNote());
            cnt.setQuanBt(Double.valueOf(thiCnOrder.getQuanBt()));
            cnt.setPkUnitBt(thiCnOrder.getPkUnitBt());
            cnt.setPkCnord(cnOrder.getPkCnord());
			cnt.setEuStatus("0");
            cnt.setCodeApply(cnOrder.getCodeApply());
            cnt.setDelFlag("0");
            cnt.setPkOrg(cnOrder.getPkOrg());
            cnt.setCreator(bdOuEmployeeDoctor.getPkEmp());
            cnt.setCreateTime(dateStart);
            cnt.setModifier(bdOuEmployeeDoctor.getPkEmp());
            DataBaseHelper.insertBean(cnt);

            //?????????????????????--??????
    		if(null != thiCnOrder.getLabOcc() && !thiCnOrder.getLabOcc().isEmpty()){
    			String ordSql="delete from ex_lab_occ where code_apply=?";
    			DataBaseHelper.execute(ordSql, cnOrder.getCodeApply());
    			
    			thiCnOrder.getLabOcc().forEach(occ->{
    				occ.setPkLabocc(NHISUUID.getKeyId());
    				occ.setCodeApply(cnOrder.getCodeApply());//????????????
    				occ.setPkPi(cnOrder.getPkPi());
    				occ.setPkPv(cnOrder.getPkPv());
    				occ.setPkOrg(cnOrder.getPkOrg());
    				occ.setPkOrgOcc(cnOrder.getPkOrg());
    				occ.setEuType("0");
    				occ.setDelFlag("0");
    				occ.setCreator(bdOuEmployeeDoctor.getPkEmp());
    				occ.setCreateTime(new Date());
    				occ.setTs(new Date());
    			});
    			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExLabOcc.class), thiCnOrder.getLabOcc());
    		}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(" ZsbaOutpatientOpChargeService-SaveCnOrDer: -1| ??????????????????" + e.getMessage());
            return new RespJson("-1|??????:"+ e.getMessage(), false).toString();
		}
        
        Map<String, String> map = new HashMap<>(1);
        //map.put("ordsn", String.valueOf(cnOrder.getOrdsn()));
        map.put("pkCnord",cnOrder.getPkCnord());
        map.put("nameOrd",cnOrder.getNameOrd());
        return new RespJson("0|??????|" + JsonUtil.writeValueAsString(map), true).toString();
    }

    /**
     * ??????????????????????????????
     * @param cnOrder ??????
     * @param thiCnOrder
     */
    private void saveExAssOccAndExAssOccDt(CnOrder cnOrder, ThiCnOrder thiCnOrder) throws ParseException {
        // ???????????????
        ExAssistOcc exAssistOcc = new ExAssistOcc();
        exAssistOcc.setPkOrg(cnOrder.getPkOrg());
        exAssistOcc.setPkCnord(cnOrder.getPkCnord());
        exAssistOcc.setPkPv(cnOrder.getPkPv());
        exAssistOcc.setPkPi(cnOrder.getPkPi());
        exAssistOcc.setEuPvtype(cnOrder.getEuPvtype());
        String codeOcc = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_OPASEX);// ?????????
        exAssistOcc.setCodeOcc(codeOcc);
        exAssistOcc.setPkDept(cnOrder.getPkDept());
        exAssistOcc.setPkEmpOrd(cnOrder.getPkEmpOrd());
        exAssistOcc.setNameEmpOrd(cnOrder.getNameEmpOrd());
        exAssistOcc.setDateOrd(cnOrder.getDateStart());
        exAssistOcc.setDatePlan(DateUtils.parseDate(thiCnOrder.getDatePlan(),"yyyy-MM-dd"));
        exAssistOcc.setQuanOcc(1.0);
        exAssistOcc.setTimesOcc(1);
        exAssistOcc.setTimesTotal(1);
        exAssistOcc.setPkOrgOcc(cnOrder.getPkOrgExec());
        exAssistOcc.setPkDeptOcc(cnOrder.getPkDeptExec());
        exAssistOcc.setFlagCanc(EnumerateParameter.ZERO);
        exAssistOcc.setFlagOcc(EnumerateParameter.ZERO);
        exAssistOcc.setFlagPrt(EnumerateParameter.ZERO);
        exAssistOcc.setInfantNo(EnumerateParameter.ZERO);
        exAssistOcc.setEuStatus(EnumerateParameter.ZERO);
        exAssistOcc.setFlagRefund(EnumerateParameter.ZERO);
        //2021.1.29-tjq-zsrm??????[5217]-??????????????????
        exAssistOcc.setPkDeptArea(cnOrder.getPkDeptArea());
        ApplicationUtils.setDefaultValue(exAssistOcc, true);

        // ?????????????????????
        ExAssistOccDt exAssistOccDt = new ExAssistOccDt();
        exAssistOccDt.setPkAssocc(exAssistOcc.getPkAssocc());
        exAssistOccDt.setPkCnord(exAssistOcc.getPkCnord());
        exAssistOccDt.setPkOrg(exAssistOcc.getPkOrg());
        //???????????????
        if(cnOrder.getOrdsn().equals(cnOrder.getOrdsnParent()))
            exAssistOccDt.setFlagMaj("1");
        else
            exAssistOccDt.setFlagMaj("0");
        ApplicationUtils.setDefaultValue(exAssistOccDt, true);
        int exOcc = DataBaseHelper.insertBean(exAssistOcc);
        if(exOcc > 0){
            DataBaseHelper.insertBean(exAssistOccDt);
        }
    }

    /**
     * @return com.zebone.nhis.common.module.cn.ipdw.CnOrder
     * @Description ??????CnOrder
     * @auther wuqiang
     * @Date 2021-04-10
     * @Param 
     */
    private CnOrder creatCnOrder(ThiCnOrder thiCnOrder, PvEncounter pvEncounter, BdOuDept bdOuDept, BdOuDept bdOuDeptEex, BdOuEmployee bdOuEmployeeDoctor, Date dateStart,BdOrd bdOrd) {
        CnOrder cnOrder = new CnOrder();
        int ordSn = bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, UserContext.getUser());
        cnOrder.setCodeApply(ApplicationUtils.getCode("0404"));
        cnOrder.setPkCnord(NHISUUID.getKeyId());
        cnOrder.setPkOrd(bdOrd.getPkOrd());
        cnOrder.setPkOrg(bdOuDept.getPkOrg());
        cnOrder.setPkPv(pvEncounter.getPkPv());
        cnOrder.setPkPi(pvEncounter.getPkPi());
        cnOrder.setPkOrgExec(bdOuDept.getPkOrg());
        cnOrder.setPkDeptExec(bdOuDeptEex.getPkDept());
        cnOrder.setPkDept(bdOuDept.getPkDept());
        cnOrder.setPkDeptNs(pvEncounter.getPkDeptNs());
        cnOrder.setPkEmpInput(bdOuEmployeeDoctor.getPkEmp());
        cnOrder.setPkEmpOrd(bdOuEmployeeDoctor.getPkEmp());
        //cnOrder.setOrdsnChk(ordSn);
        cnOrder.setEuIntern("9");
        cnOrder.setEuPvtype("3");
        cnOrder.setCodeOrdtype("12");
        cnOrder.setEuAlways("1");
        cnOrder.setOrdsn(ordSn);
        cnOrder.setOrdsnParent(ordSn);
        cnOrder.setCodeOrd(bdOrd.getCode());
        cnOrder.setNameOrd(bdOrd.getName());
        cnOrder.setDescOrd(bdOrd.getName());
        cnOrder.setCodeFreq("once");
        cnOrder.setDosage(Double.valueOf(thiCnOrder.getQuanBt()));
        cnOrder.setPkUnitDos(thiCnOrder.getPkUnitBt());
        cnOrder.setQuan(1D);
        cnOrder.setQuanCg(0D);
        cnOrder.setPackSize(1.00);
        cnOrder.setPriceCg(getItemPrice(bdOrd.getPkOrd()));
        cnOrder.setDays(0l);
        //cnOrder.setOrds(1l);
        cnOrder.setFlagFirst("0");
        cnOrder.setFlagItc("1");
        cnOrder.setFlagSign("1");
        cnOrder.setFlagDurg("0");
        cnOrder.setFlagSelf("0");
        cnOrder.setFlagNote("0");
        cnOrder.setFlagBase("0");
        cnOrder.setFlagBl("0");
        cnOrder.setFlagEraseChk("0");
        cnOrder.setFlagStop("0");
        cnOrder.setFlagStopChk("0");
        cnOrder.setFlagErase("0");
        cnOrder.setFlagCp("0");
        cnOrder.setFlagDoctor("1");
        cnOrder.setFlagPrint("0");
        cnOrder.setFlagMedout("0");
        cnOrder.setFlagEmer("0");
        cnOrder.setFlagThera("0");
        cnOrder.setFlagPrev("0");
        cnOrder.setFlagFit("0");
        //cnOrder.setFlagItc("0");
        cnOrder.setFlagPivas("0");
        cnOrder.setEuStatusOrd("1");
        cnOrder.setDateSign(dateStart);
        cnOrder.setDateEnter(dateStart);
        cnOrder.setDateStart(dateStart);
        cnOrder.setDateEffe(dateStart);
        cnOrder.setNameEmpInput(bdOuEmployeeDoctor.getNameEmp());
        cnOrder.setNameEmpOrd(bdOuEmployeeDoctor.getNameEmp());
        //cnOrder.setInfantNo(0);
        cnOrder.setQuanBed(0D);
        cnOrder.setEuIntern("0");
        cnOrder.setGroupno(0);
        cnOrder.setQuanDisp(0D);
        cnOrder.setCreator(bdOuEmployeeDoctor.getPkEmp());
        cnOrder.setCreateTime(new Date());
        cnOrder.setTs(new Date());
        cnOrder.setDelFlag("0");
        
        return cnOrder;
    }

    /**
     * @Descirption ???????????????????????????
     * @param pkOrd ??????????????????
     * @return price
     */
    public Double getItemPrice(String pkOrd){
        String sql = "select PRICE price_cg from bd_item  where pk_item in(select pk_item from bd_ord_item where pk_ord =?)";
        CnOrder cnOrder = DataBaseHelper.queryForBean(sql, CnOrder.class, pkOrd);
        Double price = cnOrder.getPriceCg();
        return price;
    }
    /**
     * @return com.zebone.nhis.common.module.base.ou.BdOuEmployee
     * @Description ??????????????????
     * @auther wuqiang
     * @Date 2021-04-10
     * @Param [codeEmpInput]
     */
    public BdOuEmployee getBdOuEmployee(String codeEmpInput,String pkEmp ) {
        String sql = "select BOE.PK_EMP,BOE.NAME_EMP,BOE.CODE_EMP ,BOU.PK_ORG " +
                "from BD_OU_USER BOU " +
                "         inner join BD_OU_EMPLOYEE BOE ON BOU.PK_EMP=BOE.PK_EMP  " +
                "WHERE CODE_USER=? or BOE.PK_EMP=?";
        return DataBaseHelper.queryForBean(sql, BdOuEmployee.class, codeEmpInput,pkEmp);
    }

    /**
     * @return com.zebone.nhis.common.module.base.ou.BdOuDept
     * @Description ??????????????????
     * @auther wuqiang
     * @Date 2021-04-10
     * @Param [pkOrg, codeDeptExec]
     */
    public BdOuDept getBdOuDept(String pkOrg, String codeDeptExec,String pkDept) {
        String sql = "select B.PK_ORG, PK_DEPT, CODE_DEPT, NAME_DEPT " +
                "from BD_OU_DEPT BOD " +
                "         inner join BD_OU_ORG B ON B.PK_ORG = BOD.PK_ORG " +
                "where B.PK_ORG = ? and (CODE_DEPT = ? or BOD.PK_DEPT=? )  ";
        return DataBaseHelper.queryForBean(sql, BdOuDept.class,
                new Object[]{pkOrg, codeDeptExec,pkDept});
    }
    
    /**
     * ???????????????????????????????????????
     * @param codeDeptExec
     * @param pkDept
     * @return
     */
    public BdOuDept getBdOuDeptNoWithOrg( String codeDeptExec,String pkDept) {
        String sql = "select B.PK_ORG, PK_DEPT, CODE_DEPT, NAME_DEPT " +
                "from BD_OU_DEPT BOD " +
                "         inner join BD_OU_ORG B ON B.PK_ORG = BOD.PK_ORG " +
                "where  CODE_DEPT = ? or BOD.PK_DEPT=?   ";
        return DataBaseHelper.queryForBean(sql, BdOuDept.class,
                new Object[]{codeDeptExec,pkDept});
    }

    /**
     * ??????????????????
     * @param pkOrd
     * @return
     */
    public BdOrd getBdOrd(String pkOrd) {
        String sql = "select * from bd_ord where pk_ord= ? ";
        return DataBaseHelper.queryForBean(sql, BdOrd.class, new Object[]{pkOrd});
    }

    /**
     * @return com.zebone.nhis.common.module.pv.PvEncounter
     * @Description ????????????????????????
     * @auther wuqiang
     * @Date 2021-04-10
     * @Param [codePv]
     */
    public PvEncounter getPvconter(String codePv,String pkPv, String euPvtype) {
        String sql = " select * " +
                "from PV_ENCOUNTER PE where EU_STATUS='1'and (CODE_PV=?  or  PK_PV=?) and eu_pvtype=?";
        if ("1".equals(euPvtype)){
            sql = " select * " +
                    "from PV_ENCOUNTER PE where EU_STATUS in ('1','2')and (CODE_PV=?  or  PK_PV=?) and eu_pvtype=?";
        }
        return DataBaseHelper.queryForBean(sql, PvEncounter.class, new Object[]{codePv,pkPv,euPvtype});
    }

    /**
     * @return java.lang.String
     * @Description ????????????
     * @auther wuqiang
     * @Date 2021-04-13
     * @Param [inf]
     */
    public String cancelCnOrd(String inf) {
        inf= getIniParam(inf);
        String ret;
        ThiCancelCnOrd thiCancelCnOrd = JsonUtil.readValue(inf, ThiCancelCnOrd.class);
        //????????????
        if (thiCancelCnOrd == null) {
            ret = " ZsbaOutpatientOpChargeService-CancelCnOrd: -1|???????????? " + " ";
            return new RespJson(ret, false).toString();
        }
        try {
            ret = vilaFiled(thiCancelCnOrd);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" ZsbaOutpatientOpChargeService-CancelCnOrd: -1|???????????? " + thiCancelCnOrd.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" ZsbaOutpatientOpChargeService-CancelCnOrd: -1|?????????????????? " + thiCancelCnOrd.toString());
            return new RespJson("-1|??????????????????", false).toString();
        }
        BdOuEmployee bdOuEmployeeDoctor = getBdOuEmployee(null,thiCancelCnOrd.getPkEmpErase());
        if (bdOuEmployeeDoctor == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelCnOrd: -1| ???????????????????????????" + thiCancelCnOrd.toString());
            return new RespJson("-1|???????????????????????????,?????????????????????????????????", false).toString();
        }
        User user = new User();
        user.setPkOrg(bdOuEmployeeDoctor.getPkOrg());
        user.setPkEmp(bdOuEmployeeDoctor.getPkEmp());
        user.setNameEmp(bdOuEmployeeDoctor.getNameEmp());
        UserContext.setUser(user);
        CnOrder cnOrder = thiIpCnWebMapper.getCnOrderPkCnord
                (thiCancelCnOrd.getPkCnord(), " AND PE.EU_STATUS = '1' and CO.EU_STATUS_ORD in ('1','2','3')");
        if (cnOrder == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelCnOrd: -1| ???????????????????????????" + thiCancelCnOrd.toString());
            return new RespJson("-1|???????????????????????????,???????????????????????????????????????????????????/?????????/?????????", false).toString();
        }
        Integer cout = DataBaseHelper.queryForScalar("select count(*) " +
                "from EX_ORDER_OCC EOO where PK_CNORD=? and EU_STATUS='1'", Integer.class, cnOrder.getPkCnord());
        if (cout >= 1) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelCnOrd: -1|?????????????????????????????????" + thiCancelCnOrd.toString());
            return new RespJson("-1|?????????????????????????????????????????????????????????", false).toString();
        }
        //??????????????????
        if (!cnOrder.getPkEmpOrd().equals(bdOuEmployeeDoctor.getPkEmp())) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelCnOrd: -1| ???????????????????????????????????????" + thiCancelCnOrd.toString());
            return new RespJson("-1|???????????????????????????????????????", false).toString();
        }
        Date date = new Date();
        if ("0".equals(ApplicationUtils.getSysparam("CN0028", false))) {
            cnOrder.setFlagEraseChk("1");
            cnOrder.setDateEraseChk(date);
            cnOrder.setPkEmpEraseChk(bdOuEmployeeDoctor.getPkEmp());
            cnOrder.setNameEraseChk(bdOuEmployeeDoctor.getNameEmp());
        }
        cnOrder.setFlagErase("1");
        cnOrder.setEuStatusOrd("9");
        cnOrder.setPkEmpErase(bdOuEmployeeDoctor.getPkEmp());
        cnOrder.setNameEmpErase(bdOuEmployeeDoctor.getNameEmp());
        cnOrder.setDateErase(date);
        thiIpCnWebMapper.updateCancleCnorder(cnOrder);
        thiIpCnWebMapper.updateCancleExOrdExcc(cnOrder);
        return new RespJson("0|??????", false).toString();
    }

    /**
     * @return java.lang.String
     * @Description 04????????????????????????
     * @auther wuqiang
     * @Date 2021-04-14
     * @Param [inf]
     */
    public String exeCnOrd(String inf) {
        inf= getIniParam(inf);
        String ret;
        ThiExeCnOrd thiExeCnOrd = JsonUtil.readValue(inf, ThiExeCnOrd.class);
        //????????????
        if (thiExeCnOrd == null) {
            ret = " ZsbaOutpatientOpChargeService- ExeCnOrd: -1|???????????? " + " ";
            return new RespJson(ret, false).toString();
        }
        try {
            ret = vilaFiled(thiExeCnOrd);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" ZsbaOutpatientOpChargeService-ExeCnOrd: -1|???????????? " + thiExeCnOrd.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" ZsbaOutpatientOpChargeService-ExeCnOrd: -1|?????????????????? " + thiExeCnOrd.toString());
            return new RespJson("-1|??????????????????", false).toString();
        }
        BdOuEmployee bdOuEmployee = getBdOuEmployee(null,thiExeCnOrd.getPkEmpOcc());
        if (bdOuEmployee == null) {
            logger.error(" ZsbaOutpatientOpChargeService--ExeCnOrd: -1| ????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|????????????????????????,??????????????????????????????", false).toString();
        }
        CnOrder cnOrder = thiIpCnWebMapper.getCnOrderPkCnord(thiExeCnOrd.getPkCnord(), " AND PE.EU_STATUS = '1' and CO.EU_STATUS_ORD = '3'");
        if (cnOrder == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1| ???????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|???????????????????????????", false).toString();
        }
        BdOuDept bdOuDept = getBdOuDept(cnOrder.getPkOrg(), null, thiExeCnOrd.getPkDeptOcc());
        if (bdOuDept == null) {
            logger.error(" ZsbaOutpatientOpChargeService--ExeCnOrd: -1| ???????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|?????????????????????,???????????????????????????", false).toString();
        }
        if (!cnOrder.getPkDeptExec().equals(bdOuDept.getPkDept())) {
            logger.error(" ZsbaOutpatientOpChargeService--ExeCnOrd: -1| ??????????????????????????????????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|??????????????????????????????????????????????????????,???????????????", false).toString();
        }
        String sqlTrans ="select pk_ordbt from CN_TRANS_APPLY where PK_CNORD=? and EU_STATUS='1' ";
        CnTransApply cnt = DataBaseHelper.queryForBean(sqlTrans, CnTransApply.class, cnOrder.getPkCnord());
        if (cnt == null) {
         logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1| ????????????????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|????????????????????????????????????", false).toString();
        }
        String sqlOrder = "select PK_EXOCC from EX_ORDER_OCC EOO  where PK_CNORD=? and EU_STATUS='0'";
        ExOrderOcc exOrderOcc = DataBaseHelper.queryForBean(sqlOrder, ExOrderOcc.class, cnOrder.getPkCnord());
        if (exOrderOcc == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1| ???????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|???????????????????????????", false).toString();
        }
        if (exeCnOrd(cnOrder, bdOuDept, bdOuEmployee, exOrderOcc,cnt)) {
            return new RespJson("0|??????", false).toString();
        }
        
        logger.error(" ZsbaOutpatientOpChargeService--ExeCnOrd: -1| ????????????" + thiExeCnOrd.toString());
        return new RespJson("-1|????????????", false).toString();
    }

    /**
     * @return java.lang.String
     * @Description ????????????
     * @auther wuqiang
     * @Date 2021-04-14
     * @Param [cnOrder]
     */
    private boolean exeCnOrd(CnOrder cnOrder, BdOuDept bdOuDept, BdOuEmployee bdOuEmployee, ExOrderOcc exOrderOcc,CnTransApply cnt) {
        Date date = new Date();
        StringBuilder updateSql = new StringBuilder("update cn_order set ");
        updateSql.append(" EU_STATUS_ORD = '3' ");
        updateSql.append(",modifier = '" + bdOuEmployee.getPkEmp() + "' ");
        updateSql.append(",modity_time = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", date) + "','YYYYMMDDHH24MISS') ");
        updateSql.append(",ts = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", date) + "','YYYYMMDDHH24MISS') ");
        updateSql.append(",date_plan_ex = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", date) + "','YYYYMMDDHH24MISS') ");
        updateSql.append(" ,pk_emp_ex ='" + bdOuEmployee.getPkEmp() + "'");
        updateSql.append(" ,name_emp_ex ='" + bdOuEmployee.getNameEmp() + "'");
        updateSql.append(" where pk_cnord = '" + cnOrder.getPkCnord() + "'");
        int upc = DataBaseHelper.update(updateSql.toString());
        exOrderOcc.setDateOcc(date);
        exOrderOcc.setPkEmpOcc(bdOuEmployee.getPkEmp());
        exOrderOcc.setNameEmpOcc(bdOuEmployee.getNameEmp());
        exOrderOcc.setPkDeptOcc(bdOuDept.getPkDept());
        String sql = "update ex_order_occ  set  date_occ =:dateOcc," +
                " eu_status='1' ,pk_emp_occ=:pkEmpOcc, " +
                " name_emp_occ=:nameEmpOcc,PK_DEPT_OCC=:pkDeptOcc where PK_EXOCC=:pkExocc ";
        int upc1 = DataBaseHelper.update(sql, exOrderOcc);
        cnt.setModityTime(date);
        cnt.setModifier(bdOuEmployee.getPkEmp());
        cnt.setPkEmpBp(bdOuEmployee.getPkEmp());
        cnt.setNameEmpBp(bdOuEmployee.getNameEmp());
        cnt.setEuStatus("3");
        String sqls = "update cn_trans_apply set  modifier =:modifier,eu_status=:euStatus,modity_time=:modityTime, " +
                " pk_emp_bp=:pkEmpBp,name_emp_bp=:nameEmpBp where pk_ordbt=:pkOrdbt ";
        DataBaseHelper.update(sqls, cnt);
        return upc * upc1 > 0;
    }

    /**
     * @return java.lang.String
     * @Description 05????????? ????????????
     * @auther wuqiang
     * @Date 2021-04-20
     * @Param [inf]
     */
    public String cancelExeCnord(String inf) {
        inf= getIniParam(inf);
        String ret;
        ThiCancelExeCnord thiExeCnOrd = JsonUtil.readValue(inf, ThiCancelExeCnord.class);
        //????????????
        if (thiExeCnOrd == null) {
            ret = " ZsbaOutpatientOpChargeService- CancelExeCnord: -1|???????????? " + " ";
            return new RespJson(ret, false).toString();
        }
        try {
            ret = vilaFiled(thiExeCnOrd);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1|???????????? " + thiExeCnOrd.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1|?????????????????? " + thiExeCnOrd.toString());
            return new RespJson("-1|??????????????????", false).toString();
        }
        BdOuEmployee bdOuEmployee = getBdOuEmployee(null, thiExeCnOrd.getPkEmpCanc());
        if (bdOuEmployee == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1| ????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|????????????????????????,??????????????????????????????", false).toString();
        }
        CnOrder cnOrder = thiIpCnWebMapper.getCnOrderPkCnord(thiExeCnOrd.getPkCnord(), "AND PE.EU_STATUS = '1' and CO.EU_STATUS_ORD = '3'");
        if (cnOrder == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1| ???????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|???????????????????????????", false).toString();
        }
        BdOuDept bdOuDept = getBdOuDept(cnOrder.getPkOrg(), null, thiExeCnOrd.getPkDeptCanc());
        if (bdOuDept == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1| ???????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|?????????????????????,?????????????????????????????????", false).toString();
        }
        
        String sqlTrans ="select pk_ordbt from CN_TRANS_APPLY where PK_CNORD=?";
        CnTransApply cnt = DataBaseHelper.queryForBean(sqlTrans, CnTransApply.class, cnOrder.getPkCnord());
        if (cnt == null) {
         logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1| ????????????????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|????????????????????????????????????", false).toString();
        }
        ExOrderOcc exOrderOcc = DataBaseHelper.queryForBean("select PK_EXOCC,PK_DEPT_OCC " +
                "from EX_ORDER_OCC EOO  where PK_CNORD=? and EU_STATUS='1'", ExOrderOcc.class, cnOrder.getPkCnord());
        if (exOrderOcc == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1| ???????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|???????????????????????????", false).toString();
        }
        if (!exOrderOcc.getPkDeptOcc().equals(bdOuDept.getPkDept())) {
            logger.error(" ZsbaOutpatientOpChargeService--ExeCnOrd: -1| ?????????????????????????????????????????????" + thiExeCnOrd.toString());
            return new RespJson("-1|?????????????????????????????????????????????,???????????????", false).toString();
        }
        if (cancelExeCnord(cnOrder, bdOuDept, bdOuEmployee, exOrderOcc,cnt)) {
            return new RespJson("0|??????", false).toString();
        }
        
        logger.error(" ZsbaOutpatientOpChargeService-CancelExeCnord: -1| ??????????????????" + thiExeCnOrd.toString());
        return new RespJson("-1|??????????????????", false).toString();
    }

    private boolean cancelExeCnord(CnOrder cnOrder, BdOuDept bdOuDept, BdOuEmployee bdOuEmployee, ExOrderOcc exOrderOcc,CnTransApply cnt) {
        Date date = new Date();
        exOrderOcc.setDateCanc(new Date());
        exOrderOcc.setPkDeptCanc(bdOuDept.getPkDept());
        exOrderOcc.setPkEmpCanc(bdOuEmployee.getPkEmp());
        exOrderOcc.setNameEmpCanc(bdOuEmployee.getNameEmp());
        String sql = "update ex_order_occ set flag_canc='1',date_canc =:dateCanc,eu_status='9',"
                + "pk_dept_canc=:pkDeptCanc,pk_emp_canc=:pkEmpCanc,name_emp_canc=:nameEmpCanc where pk_exocc =:pkExocc";
        int upc1 = DataBaseHelper.update(sql, exOrderOcc);
        cnt.setModityTime(date);
        cnt.setModifier(bdOuEmployee.getPkEmp());
        cnt.setEuStatus("0");
        String sqls = "update cn_trans_apply set modifier =:modifier,eu_status=:euStatus,modity_time=:modityTime where pk_ordbt=:pkOrdbt";
        DataBaseHelper.update(sqls, cnt);
        
        return upc1 > 0;
    }

    /**
     * @return java.lang.String
     * @Description 06?????????????????????
     * @auther wuqiang
     * @Date 2021-04-20
     * @Param [inf]
     */
    public String IpCharge(String inf) {
        inf= getIniParam(inf);
        String ret;
        ThiIpCharge thiIpCharge = JsonUtil.readValue(inf, ThiIpCharge.class);
        //????????????
        if (thiIpCharge == null) {
            ret = "-1|???????????? " + " ";
            return new RespJson(ret, false).toString();
        }
        Date date = new Date();
        try {
            ret = vilaFiled(thiIpCharge);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|???????????? " + thiIpCharge.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|?????????????????? " + thiIpCharge.toString());
            return new RespJson("-1|??????????????????", false).toString();
        }
        BdOuDept bdOuDeptCg = getBdOuDept("89ace0e12aba439991c0eb001aaf02f7", null, thiIpCharge.getPkDeptCg());
        if (bdOuDeptCg == null) {
            logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|?????????????????????????????? " + thiIpCharge.toString());
            return new RespJson("-1|??????????????????????????????????????????????????????????????????", false).toString();
        }
        BdOuEmployee bdOuEmployeeCg = getBdOuEmployee(null, thiIpCharge.getPkEmpCg());
        if (bdOuEmployeeCg == null) {
            logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|?????????????????????????????? " + thiIpCharge.toString());
            return new RespJson("-1|??????????????????????????????????????????????????????????????????", false).toString();
        }
        User user = getDefaultUser(null,thiIpCharge.getPkEmpCg());
        if(!BeanUtils.isNotNull(user)){
            return new RespJson("-1|???????????????????????????", false).toString();
        }
        UserContext.setUser(user);
        List<BlPubParamVo> blPubParamVos = new ArrayList<>(thiIpCharge.getCgItems().size());
        Map<String, String> map = new HashMap<>(5);
        for (ThiCgItem thiCgItem : thiIpCharge.getCgItems()) {
            PvEncounter pvEncounter = getPvconter(null, thiCgItem.getPkPv(), "3");
            map.put(pvEncounter.getPkPv(), thiCgItem.getPkPv());
            if (pvEncounter == null) {
                logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|??????????????????????????? " + thiIpCharge.toString());
                return new RespJson("-1|???????????????????????????,???????????????????????????", false).toString();
            }
            if (Double.valueOf(thiCgItem.getQuan()) <= 0) {
                logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|??????????????????0 " + thiIpCharge.toString());
                return new RespJson("-1|???????????????????????????0", false).toString();
            }
            BdItem bdItem = getBdItem(null, thiCgItem.getPkItem());
            if (bdItem == null) {
                logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|??????????????????????????? " + thiIpCharge.toString());
                return new RespJson("-1|?????????????????????????????????????????????????????????", false).toString();
            }
            BdOuDept bdOuDept = getBdOuDept(pvEncounter.getPkOrg(), null, thiCgItem.getPkDeptApp());
            if (bdOuDept == null) {
                logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|??????????????????????????? " + thiIpCharge.toString());
                return new RespJson("-1|?????????????????????????????????????????????????????????", false).toString();
            }
            BdOuEmployee bdOuEmployee = getBdOuEmployee(null, thiCgItem.getPkEmpApp());
            if (bdOuEmployee == null) {
                logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|???????????????????????? " + thiIpCharge.toString());
                return new RespJson("-1|?????????????????????????????????????????????????????????", false).toString();
            }
            BdOuDept bdOuDeptExe = getBdOuDept(pvEncounter.getPkOrg(), null, thiCgItem.getPkDeptEx());
            if (bdOuDeptExe == null) {
                logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|???????????????????????? " + thiIpCharge.toString());
                return new RespJson("-1|?????????????????????????????????????????????????????????", false).toString();
            }
            CnOrder  cnOrder = null;
            if(StringUtils.isNotBlank(thiCgItem.getPkCnord())){
                cnOrder = thiIpCnWebMapper.getCnOrderPkCnord(thiCgItem.getPkCnord(), "AND PE.EU_STATUS = '1' and CO.EU_STATUS_ORD in( '3','2','1')");
            }

            if(cnOrder == null){
                logger.error(" ZsbaOutpatientOpChargeService-IpCharge: -1|???????????????????????? " + thiCgItem.toString());
                return new RespJson("-1|?????????????????????????????????????????????????????????", false).toString();
            }
            BlPubParamVo blPubParamVo = createBlPubParamVo(pvEncounter, cnOrder, bdItem, thiCgItem, bdOuDept, bdOuDeptExe, bdOuDeptCg, bdOuEmployee, bdOuEmployeeCg);
            blPubParamVos.add(blPubParamVo);
        }
        BlPubReturnVo blPubReturnVo = ipCgPubService.chargeIpBatch(blPubParamVos, false);
        List<ThiIpChargeRet> thiIpChargeRets = new ArrayList<>(blPubReturnVo.getBids().size());
        blPubReturnVo.getBids().forEach(m -> {
                    ThiIpChargeRet thiIpChargeRet = new ThiIpChargeRet();
                    thiIpChargeRet.setAmountPi(String.valueOf(m.getAmountPi()));
                    thiIpChargeRet.setAmount(String.valueOf(m.getAmount()));
                    thiIpChargeRet.setQuan(String.valueOf(m.getQuan()));
                    thiIpChargeRet.setPrice(String.valueOf(m.getPrice()));
                    thiIpChargeRet.setNameCg(m.getNameCg());
                    thiIpChargeRet.setPkCgIp(m.getPkCgip());
                    thiIpChargeRet.setPkPv(MapUtils.getString(map, m.getPkPv()));
                    thiIpChargeRet.setDataCg(DateUtils.formatDate(m.getDateCg(), "yyyy-MM-dd HH:mm:ss"));
                    thiIpChargeRets.add(thiIpChargeRet);
                }
        );
        return new RespJson("0|??????|" + JsonUtil.writeValueAsString(thiIpChargeRets), true).toString();
    }

    private BlPubParamVo createBlPubParamVo(PvEncounter pvEncounter, CnOrder cnOrder, BdItem bdItem, ThiCgItem thiCgItem, BdOuDept bdOuDept, BdOuDept bdOuDeptExe, BdOuDept bdOuDeptCg, BdOuEmployee bdOuEmployee, BdOuEmployee bdOuEmployeeCg) {
        BlPubParamVo blPubParamVo = new BlPubParamVo();
        BlPubParamVo vo = new BlPubParamVo();
        blPubParamVo.setEuPvType("3");
        blPubParamVo.setPkPv(pvEncounter.getPkPv());
        blPubParamVo.setFlagPv("0");
        blPubParamVo.setFlagPd("0");
        blPubParamVo.setEuBltype(thiCgItem.getEuBlType());
        blPubParamVo.setNameEmpApp(bdOuEmployee.getNameEmp());
        blPubParamVo.setPkOrg(pvEncounter.getPkOrg());
        blPubParamVo.setPkEmpApp(bdOuEmployee.getPkEmp());
        blPubParamVo.setPkOrgApp(pvEncounter.getPkOrg());
        blPubParamVo.setPkCnord(cnOrder.getPkCnord());
        //????????????
        blPubParamVo.setPkDeptNsApp(pvEncounter.getPkDeptNs());
        blPubParamVo.setDateHap(new Date());
        //????????????
        blPubParamVo.setPkDeptApp(bdOuDept.getPkDept());
        blPubParamVo.setPkDeptEx(bdOuDeptExe.getPkDept());
        blPubParamVo.setPkOrgEx(pvEncounter.getPkOrg());
        blPubParamVo.setPkPi(pvEncounter.getPkPi());
        blPubParamVo.setInfantNo("0");
        blPubParamVo.setPkDeptCg(bdOuDeptCg.getPkDept());
        blPubParamVo.setPkEmpCg(bdOuDeptCg.getNameDept());
        blPubParamVo.setPkItem(bdItem.getPkItem());
        blPubParamVo.setPkUnitPd(bdItem.getPkUnit());
        blPubParamVo.setQuanCg(Double.valueOf(thiCgItem.getQuan()));
        return blPubParamVo;
    }

    private BdItem getBdItem(String codeItem,String pkItem) {
        String sql = "select  pk_item from BD_ITEM BI where (CODE=? or PK_ITEM=? ) and DEL_FLAG='0' and FLAG_ACTIVE='1'";
        return DataBaseHelper.queryForBean(sql, BdItem.class, codeItem,pkItem);
    }

    /**
     * @return java.lang.String
     * @Description 07?????????????????????
     * @auther wuqiang
     * @Date 2021-04-20
     * @Param [inf]
     */
    public String cancelIpCharge(String inf) {
        inf= getIniParam(inf);
        String ret;
        ThiCancelIpCharge thiCancelIpCharge = JsonUtil.readValue(inf, ThiCancelIpCharge.class);
        //????????????
        if (thiCancelIpCharge == null) {
            ret = "-1|???????????? " + " ";
            return new RespJson(ret, false).toString();
        }
        try {
            ret = vilaFiled(thiCancelIpCharge);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" ZsbaOutpatientOpChargeService-CancelIpCharge: -1|???????????? " + thiCancelIpCharge.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" ZsbaOutpatientOpChargeService-CancelIpCharge: -1|?????????????????? " + thiCancelIpCharge.toString());
            return new RespJson("-1|??????????????????", false).toString();
        }
        List<RefundVo> refundVos = new ArrayList<>(16);
        List<BlIpDt> blIpDtList = thiIpCnWebMapper.getBlIpDts(thiCancelIpCharge.getDataCgs().stream().map(ThiIpChargeRet::getPkCgIp).collect(Collectors.toList()));
        if (blIpDtList.size() == 0) {
            blIpDtList = thiIpCnWebMapper.getBlIpDtsByPkCgipBack(thiCancelIpCharge.getDataCgs().stream().map(ThiIpChargeRet::getPkCgIp).collect(Collectors.toList()));
            if(blIpDtList.size() == 0) {
                logger.error(" ZsbaOutpatientOpChargeService-CancelIpCharge: -1|?????????????????? " + thiCancelIpCharge.toString());
                return new RespJson("-1|??????????????????", false).toString();
            }
        }
        BdOuDept bdOuDept = getBdOuDept(blIpDtList.get(0).getPkOrg(), null, thiCancelIpCharge.getPkDeptCg());
        if (bdOuDept == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelIpCharge: -1|??????????????????????????? " + thiCancelIpCharge.toString());
            return new RespJson("-1|?????????????????????????????????????????????????????????", false).toString();
        }
        BdOuEmployee bdOuEmployee = getBdOuEmployee(null, thiCancelIpCharge.getPkEmpCg());
        if (bdOuEmployee == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelIpCharge: -1|??????????????????????????? " + thiCancelIpCharge.toString());
            return new RespJson("-1|???????????????????????????????????????????????????????????????", false).toString();
        }
        User user = getDefaultUser(null,thiCancelIpCharge.getPkEmpCg());
        if(!BeanUtils.isNotNull(user)){
            return new RespJson("-1|???????????????????????????", false).toString();
        }
        UserContext.setUser(user);
        thiCancelIpCharge.getDataCgs().forEach(m -> {
            RefundVo refundVo = new RefundVo();
            refundVo.setQuanRe(Double.valueOf(m.getQuan()));
            refundVo.setNameEmp(bdOuEmployee.getNameEmp());
            refundVo.setPkOrg(bdOuDept.getPkOrg());
            refundVo.setPkDept(bdOuDept.getPkDept());
            refundVo.setPkEmp(bdOuEmployee.getPkEmp());
            refundVo.setPkCgip(m.getPkCgIp());
            refundVo.setNoteCg(m.getNoteCg());
            refundVos.add(refundVo);
        });
        BlPubReturnVo ipcgvo = ipCgPubService.refundInBatch(refundVos);
        return new RespJson("0|??????|", false).toString();
    }

    /**
     * @return java.lang.String
     * @Description 08?????????????????????
     * @auther wuqiang
     * @Date 2021-04-20
     * @Param [inf]
     */
    public String login(String inf) {
        inf= getIniParam(inf);
        String ret;
        ThiAccount thiAccount = JsonUtil.readValue(inf, ThiAccount.class);
        //????????????
        if (thiAccount == null) {
            ret = "-1|???????????? " + " ";
            return new RespJson(ret, false).toString();
        }
        try {
            ret = vilaFiled(thiAccount);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" ZsbaOutpatientOpChargeService-Login: -1|???????????? " + thiAccount.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" ZsbaOutpatientOpChargeService-Login: -1|?????????????????? " + thiAccount.toString());
            return new RespJson("-1|??????????????????", false).toString();
        }
        //?????????????????????base64????????????
        org.apache.commons.codec.binary.Base64 base64 = new Base64();
        String password = null;
        try {
            password = new String(base64.decode(thiAccount.getLogPwd()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String encryptPassword = new SimpleHash("md5", password).toHex();
        Integer cout = DataBaseHelper.queryForScalar("select count(*) " +
                " from BD_OU_USER BOU " +
                " where CODE_USER = ? " +
                "  and PWD = ? " +
                "  and IS_LOCK = '0' " +
                "  and FLAG_ACTIVE = '1' ", Integer.class, new Object[]{thiAccount.getLogCode(), encryptPassword});
        if (cout != null && cout >= 1) {
            return new RespJson("0|??????", false).toString();
        }
        return new RespJson("-1|??????", false).toString();
    }

    /**
     * @return java.lang.String
     * @Description 09???????????????????????????
     * @auther wuqiang
     * @Date 2021-04-20
     * @Param [inf]
     */
    public String getPvInfo(String inf) {
        inf= getIniParam(inf);
        String ret;
        ThiQueryPiInf thiQueryPiInf = JsonUtil.readValue(inf, ThiQueryPiInf.class);
        //????????????
        if (thiQueryPiInf == null) {
            ret = "-1|???????????? " + " ";
            return new RespJson(ret, false).toString();
        }
        try {
            ret = vilaFiled(thiQueryPiInf);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" ZsbaOutpatientOpChargeService-getPvInfo: -1|???????????? " + thiQueryPiInf.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" ZsbaOutpatientOpChargeService-getPvInfo: -1|?????????????????? " + thiQueryPiInf.toString());
            return new RespJson("-1|??????????????????", false).toString();
        }
        System.out.println(thiQueryPiInf.toString());
        List<Map<String, Object>> maps = thiIpCnWebMapper.getPvInfo(thiQueryPiInf);
        return new RespJson("0|??????|" + JsonUtil.writeValueAsString(maps), true).toString();
    }

    public String getIniParam(String inf) {
        if (inf != null) {
            if (inf.contains("datalist")){
                Map<String,Object> map = JsonUtil.readValue(inf,Map.class );
                return JsonUtil.writeValueAsString(map.get("datalist"));
            }           
        }
        return inf;
    }
    
    //??????????????????????????????????????????
    public String getOpPkDeptNs(String pkDept) {
    	StringBuffer sql = new StringBuffer("");
    	sql.append("select bus.pk_dept from bd_dept_bus bus ");
    	sql.append(" LEFT JOIN bd_dept_bus busa on bus.PK_DEPTBU = busa.pk_deptbu ");
    	sql.append(" where bus.DT_DEPTTYPE = '02' and busa.pk_dept=? ");
    	List<Map<String, Object>> pkDeptList = DataBaseHelper.queryForList(sql.toString(), new Object[]{pkDept});
    	if(null != pkDeptList && !pkDeptList.isEmpty()) {
    		return MapUtils.getString(pkDeptList.get(0), "pkDept");
    	}else {
    		return null;
    	}
    }
    
    //??????????????????????????????
    public String getPkUnit(String code) {
    	String sql = "select PK_UNIT from bd_unit where code=? ";
    	List<Map<String, Object>> pkUnitList = DataBaseHelper.queryForList(sql.toString(), new Object[]{code});
     
    	if(null != pkUnitList && !pkUnitList.isEmpty()) {
    		return MapUtils.getString(pkUnitList.get(0), "pkUnit");
    	}else {
    		return null;
    	}
    }

    /**
     * ????????????????????????????????????
     * @param codeEmp
     * @return
     */
    private static User getDefaultUser(String codeEmp,String pkEmp) {
        User user=null;
        String sql = "";
        if(StringUtils.isNotEmpty(codeEmp)) {
            sql = "SELECT * FROM bd_ou_employee emp JOIN bd_ou_empjob empjob  ON empjob.pk_emp = emp.pk_emp WHERE empjob.del_flag = '0' and emp.del_flag = '0' and emp.CODE_EMP = ?";
        }
        if(StringUtils.isNotEmpty(pkEmp)) {
            sql = "SELECT * FROM bd_ou_employee emp JOIN bd_ou_empjob empjob  ON empjob.pk_emp = emp.pk_emp WHERE empjob.del_flag = '0' and emp.del_flag = '0' and emp.pk_emp = ?";
            codeEmp = pkEmp;
        }
        Map<String, Object> bdOuMap = DataBaseHelper.queryForMap(sql ,codeEmp);
        if (bdOuMap != null) {
            user = new User();
            user.setPkOrg(MapUtils.getString(bdOuMap, "pkOrg"));
            user.setNameEmp(MapUtils.getString(bdOuMap, "nameEmp"));
            user.setPkOrg(MapUtils.getString(bdOuMap, "pkOrg"));
            user.setNameEmp(MapUtils.getString(bdOuMap, "nameEmp"));
            user.setPkEmp(MapUtils.getString(bdOuMap, "pkEmp"));
            user.setPkDept(MapUtils.getString(bdOuMap, "pkDept"));
            user.setCodeEmp(MapUtils.getString(bdOuMap, "codeEmp"));
        }
        return user;
    }

    /**
     * 19?????????????????????????????????
     * @param param
     * @return
     */
    public String  invalidSaveLisApplyList(String param){
        param = getIniParam(param);
        ThiCancelZyCnOrd thiCancelCnOrd = JsonUtil.readValue(param, ThiCancelZyCnOrd.class);
        String ret;
        //????????????
        if (thiCancelCnOrd == null) {
            ret = " ZsbaOutpatientOpChargeService-CancelCnOrd: -1|???????????? " + " ";
            return new RespJson(ret, false).toString();
        }
        try {
            ret = vilaFiled(thiCancelCnOrd);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" ZsbaOutpatientOpChargeService-CancelCnOrd: -1|???????????? " + thiCancelCnOrd.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" ZsbaOutpatientOpChargeService-CancelCnOrd: -1|?????????????????? " + thiCancelCnOrd.toString());
            return new RespJson("-1|??????????????????", false).toString();
        }
        BdOuEmployee bdOuEmployeeDoctor = getBdOuEmployee(null, thiCancelCnOrd.getPkEmpErase());
        if (bdOuEmployeeDoctor == null) {
            logger.error(" ZsbaOutpatientOpChargeService-CancelCnOrd: -1| ???????????????????????????" + thiCancelCnOrd.getPkEmpErase());
            return new RespJson("-1|???????????????????????????,?????????????????????????????????", false).toString();
        }
        User user = new User();
        user.setPkOrg(bdOuEmployeeDoctor.getPkOrg());
        user.setPkEmp(bdOuEmployeeDoctor.getPkEmp());
        user.setNameEmp(bdOuEmployeeDoctor.getNameEmp());
        UserContext.setUser(user);

        List<String> pkCnordlist = thiCancelCnOrd.getPkCnord();
        for(String pkCnord: pkCnordlist){
            CnOrder cnOrder = thiIpCnWebMapper.getCnOrderPkCnord
                    (pkCnord, " AND PE.EU_STATUS = '1' and CO.EU_STATUS_ORD in ('1','2','3')");
            if (cnOrder == null) {
                logger.error(" ZsbaOutpatientOpChargeService-CancelCnOrd: -1| ???????????????????????????" + pkCnord);
                return new RespJson("-1|???????????????????????????,???????????????????????????????????????????????????/?????????/?????????", false).toString();
            }
            Date date = new Date();
            if ("0".equals(ApplicationUtils.getSysparam("CN0028", false))) {
                cnOrder.setFlagEraseChk("1");
                cnOrder.setDateEraseChk(date);
                cnOrder.setPkEmpEraseChk(bdOuEmployeeDoctor.getPkEmp());
                cnOrder.setNameEraseChk(bdOuEmployeeDoctor.getNameEmp());
            }
            cnOrder.setFlagErase("1");
            cnOrder.setEuStatusOrd("9");
            cnOrder.setPkEmpErase(bdOuEmployeeDoctor.getPkEmp());
            cnOrder.setNameEmpErase(bdOuEmployeeDoctor.getNameEmp());
            cnOrder.setDateErase(date);
            thiIpCnWebMapper.updateCancleCnorder(cnOrder);
            thiIpCnWebMapper.updateCancleExOrdExcc(cnOrder);
        }
        return new RespJson("0|??????", false).toString();
    }
}

