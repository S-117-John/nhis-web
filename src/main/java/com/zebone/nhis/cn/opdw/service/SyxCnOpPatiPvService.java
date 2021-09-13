package com.zebone.nhis.cn.opdw.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.cn.opdw.dao.SyxCnOpPatiPvMapper;
import com.zebone.nhis.cn.opdw.vo.*;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.*;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptPv;
import com.zebone.nhis.common.module.sch.plan.SchPlan;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pi.pub.service.PiPubService;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.nhis.pv.pub.dao.RegPubMapper;
import com.zebone.nhis.pv.pub.service.RegPubService;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.nhis.pv.pub.vo.PvOpParam;
import com.zebone.nhis.sch.pub.support.SchEuStatus;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class SyxCnOpPatiPvService {

    private Logger logger = LoggerFactory.getLogger("nhis.op");
    static Integer ADDCONST = 9000;
    @Autowired
    public SyxCnOpPatiPvMapper patiPvDao;

    @Autowired
    private RegPubService regPubService;

    @Autowired
    private RegPubMapper regPubMapper;

    @Autowired
    private OpCgPubService opCgPubService;

    @Resource
    private PiPubService piPubService;
    
    @Resource
    private PvInfoPubService pvInfoPubService;

    public List<Map<String, Object>> qrySchPC(String param, IUser user) {
        //已存在pkEmp参数
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Date d = new Date();
        User u = (User) user;
        String dateStr = DateUtils.getDateStr(d);
        paramMap.put("pkEmp", u.getPkEmp());
        paramMap.put("pkDept", u.getPkDept());
        paramMap.put("dateWorkBegin", dateStr + "000000");
        paramMap.put("dateWorkEnd", dateStr + "235959");
        paramMap.put("dateNow", DateUtils.getDate("yyyy-MM-dd"));
        paramMap.put("timeNow", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        List<Map<String, Object>> schPclist = patiPvDao.qrySchPC(paramMap);
        return schPclist;
    }

    public List<Map<String, Object>> qryPiList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Date d = new Date();
        User u=(User)user;
        List<Map<String, Object>> pilist = new ArrayList<Map<String, Object>>();
        String pvState = JSON.parseObject(param).getString("state"); //待确认前台是传state  未接诊：0   已接诊 ：1   接诊中 ：2
        String vof = JSON.parseObject(param).getString("vof"); //查询所有状态得患者
        String isRefreshOp = CommonUtils.getString(paramMap.get("isRefreshOp"));

        //有效期按照系统参数取
        if ("0".equals(isRefreshOp)) { //急诊
            paramMap.put("dateNow", DateUtils.getDateStr(d));
            paramMap.put("dateBegin", DateUtils.getDateTimeStr(ApplicationUtils.getPvDateBeginEr(d)));
            paramMap.put("dateEnd", DateUtils.getDateTimeStr(d));
        }else{
            paramMap.put("dateNow", DateUtils.getDateStr(d));
            paramMap.put("dateBegin", DateUtils.getDateTimeStr(ApplicationUtils.getPvDateBegin(d)));
            paramMap.put("dateEnd", DateUtils.getDateTimeStr(d));
        }

        if(StringUtils.isNotEmpty(vof)){
            paramMap.put("vof", vof);
        }

        String type = JSON.parseObject(param).getString("type");//服务类型 0普通 1专家 2特诊 9急诊
        type = "1".equals(type) ? "1" : "0";//专家只显示挂自己号的病人，非专家显示挂自己号和挂科室的患者
        paramMap.put("type", type);

        //bug 35114 医生站加载的预约病人列表需要依据所登录的接诊资源进行加载   
        String pkSchres = JSON.parseObject(param).getString("pkSchres");//门诊医生站弹出框选择的资源主键 (非人医的pkSchres为null)
        String appt=JSON.parseObject(param).getString("appt");//待诊列表类型 0挂号信息 1预约信息
        appt = "1".equals(appt) ? "1" : "0";
        if("1".equals(appt)){
            Map<String,Object> schMap=new HashMap<String, Object>();
            schMap.put("pkOrg",u.getPkOrg());
            schMap.put("pkDeptEx",u.getPkDept());
            schMap.put("dateAppt",DateUtils.getDateStr(d));
            schMap.put("pkEmp",u.getPkEmp());
            if(StringUtils.isNotBlank(pkSchres)) {
               schMap.put("pkSchres",pkSchres);
            }
            pilist=patiPvDao.qryPiSchList(schMap); //未接诊
        }else{
            if ("0".equals(isRefreshOp)) {
                if ("0".equals(pvState)) {
                    pilist = patiPvDao.qryErPiList(paramMap); //未接诊
                }
            }else {
                if ("0".equals(pvState)) {
                    pilist = patiPvDao.qryPiList(paramMap); //未接诊
                }
            }
        }
        if ("1".equals(pvState) || "2".equals(pvState)){
            pilist = patiPvDao.qryPvList(paramMap);//已接诊合并查询
        }
        return pilist;
    }

    public Map<String, Object> qryPvMode(String param, IUser user) {
        //pkPv
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> pvMode = getHpAttr(paramMap);
        return pvMode;
    }

    private Map<String, Object> getHpAttr(Map<String, Object> paramMap) {
        Map<String, Object> pvMode = new HashMap<String, Object>();
        List<Map<String, Object>> pvModelist = patiPvDao.qryPvMode(paramMap);
        if (pvModelist != null && pvModelist.size() == 1) {
            pvMode = pvModelist.get(0);
        } else {
            throw new BusException("查询患者就诊记录失败，请联系管理员！");
        }
        String pkHp = pvMode.get("pkInsu") != null ? pvMode.get("pkInsu").toString() : "";
        if (StringUtils.isNotBlank(pkHp)) {
            List<Map<String, Object>> attresList = DataBaseHelper.queryForList("select   attrtmp.code_attr, attr.val_attr from bd_hp hp  inner join bd_dictattr attr on hp.pk_hp = attr.pk_dict inner join bd_dictattr_temp attrtmp on attrtmp.pk_dictattrtemp = attr.pk_dictattrtemp  where hp.pk_hp=? ", new Object[]{pkHp});
            if (attresList != null && attresList.size() > 0) {
                Map<String, Object> hpAttr = new HashMap<String, Object>();
                for (Map<String, Object> attrM : attresList) {
                    String codeAttr = CommonUtils.getString(attrM.get("codeAttr"));
                    String valAttr = CommonUtils.getString(attrM.get("valAttr"));
                    if (StringUtils.isNotEmpty(valAttr)) {
                        hpAttr.put(codeAttr, valAttr);
                    }
                }
                String nameHp = CommonUtils.getString(pvMode.get("insuName"));
                if (!hpAttr.containsKey("0313")) {
                    throw new BusException("没有维护【" + nameHp + "】扩展属性【0313】一般疾病处方日天数上限！");
                }
                if (!hpAttr.containsKey("0314")) {
                    throw new BusException("没有维护【" + nameHp + "】扩展属性【0314】慢性病处方日天数上限！");
                }
                if (!hpAttr.containsKey("0315")) {
                    throw new BusException("没有维护【" + nameHp + "】扩展属性【0315】门诊慢病处方日天数上限！");
                }
                if (!hpAttr.containsKey("0316")) {
                    throw new BusException("没有维护【" + nameHp + "】扩展属性【0316】门诊特病处方日天数上限！");
                }
                if (!hpAttr.containsKey("0317")) {
                    throw new BusException("没有维护【" + nameHp + "】扩展属性【0317】处方日天数累计上限！");
                }
                pvMode.put("Attres", attresList);
            } else {
                throw new BusException("查询医保拓展属性为空，请联系管理员！");
            }
        } else {
            throw new BusException("查询患者就诊记录的医保计划为空，请联系管理员！");
        }
        return pvMode;
    }

    //更新就诊记录的待遇类型与诊断,诊断文档不详？
    public void updatePvMode(String param, IUser user) {
        //pkPv/euPvMode/euPvtype
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("更新就诊模式eu_pvmode时，传参pkPv为空！");
        }
        int settleCount = DataBaseHelper.queryForScalar("select count(1) from bl_settle where pk_pv=? and dt_sttype='01' ", Integer.class, new Object[]{pkPv});
        if (settleCount > 0) {
            throw new BusException("此次就诊已存在缴费记录！");
        }
        String euPvmode = paramMap.get("euPvMode") != null ? paramMap.get("euPvMode").toString() : "";
        if (StringUtils.isBlank(euPvmode)) {
            throw new BusException("更新就诊模式eu_pvmode时，传参euPvmode为空！");
        }
        DataBaseHelper.update("update pv_encounter set eu_pvmode = ? where pk_pv = ?", new Object[]{euPvmode, pkPv});
        User u = (User) user;
        DataBaseHelper.update("delete from pv_diag where pk_pv=? and eu_sptype in ('2','3')", new Object[]{pkPv});
        if ("3".equals(euPvmode) || "4".equals(euPvmode)) {
            String pkDiag = CommonUtils.getString(paramMap.get("pkDiag"));
            if (StringUtils.isBlank(pkDiag)) {
                throw new BusException("传pkDiag为空！");
            }
            String codeDiag = CommonUtils.getString(paramMap.get("codeDiag"));
            String nameDiag = CommonUtils.getString(paramMap.get("nameDiag"));
            PvDiag pvDiag = new PvDiag();
            pvDiag.setPkPv(pkPv);
            pvDiag.setSortNo(1);
            pvDiag.setDtDiagtype("0000");//门诊诊断
            pvDiag.setNameDiag(nameDiag);
            pvDiag.setCodeIcd(codeDiag);
            pvDiag.setDescDiag(nameDiag);
            //pvDiag.setFlagMaj("1");
            pvDiag.setFlagMaj("0");
            pvDiag.setEuSptype("3".equals(euPvmode) ? "2" : "3");//门慢、门特
            pvDiag.setFlagSusp("0");
            pvDiag.setFlagContagion("0");
            pvDiag.setFlagFinally("0");
            pvDiag.setFlagCure("0");
            pvDiag.setDateDiag(new Date());
            pvDiag.setPkEmpDiag(u.getPkEmp());
            pvDiag.setNameEmpDiag(u.getNameEmp());
            pvDiag.setPkDiag(pkDiag);
            DataBaseHelper.insertBean(pvDiag);
            DataBaseHelper.update("update ins_gzgy_pv set eu_pvmode_hp=? where pk_pv=?", new Object[]{("3".equals(euPvmode) ? "1" : "2"), pkPv});
        } else {
            if ("0".equals(euPvmode)) {
                DataBaseHelper.update("update ins_gzgy_pv set eu_pvmode_hp='0' where pk_pv=?", new Object[]{pkPv});
            }
        }
    }

    // 更新就诊记录的疾病类型
    public void updatePvDise(String param, IUser user) {

        //pkPv/euDisetype
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = paramMap.get("pkPv") != null ? paramMap.get("pkPv").toString() : "";
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("更新疾病类型时，传参pkPv为空！");
        }
        String euDisetype = paramMap.get("euDisetype") != null ? paramMap.get("euDisetype").toString() : "";
        if (StringUtils.isBlank(euDisetype)) {
            throw new BusException("更新疾病类型时时，传参euDisetype为空！");
        }
        DataBaseHelper.update("update pv_encounter set eu_disetype = ? where pk_pv = ?", new Object[]{euDisetype, pkPv});
    }

    //判断挂号资源是否和人员排班资源一致
    public String isRegSameAsSch(String param, IUser user) {
        //pkPv/pkOrgarea/pkEmp
        //dateWork/timeNow
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        User u = (User) user;
        Map<String, Object> clinicMap = clinicVaild(paramMap, u);
        Boolean isSameEmpPhy = (Boolean) clinicMap.get("opIsSamePhy");
        if (isSameEmpPhy) {
            return "1";//针对门诊反复接诊情况
        }

        SyxPiPv piPvInfo = (SyxPiPv) clinicMap.get("piPvInfo");
        String euPvtype = piPvInfo.getEuPvtype();
        if ("2".equals(euPvtype)) {
            return "1";//急诊直接接诊
        }
        //科室资源，相同科室直接返回
        if(EnumerateParameter.ZERO.equals(piPvInfo.getEuRestype())
                && StringUtils.equals(piPvInfo.getPkDeptBelong(),u.getPkDept())){
            return "1";
        }
        String opPvpkSchsrv = piPvInfo.getOpPkSchsrv();
        String opPvpkSchres = piPvInfo.getOpPkRes();
        if(StringUtils.isNotEmpty(opPvpkSchsrv) && StringUtils.isNotEmpty(opPvpkSchres)){
            List<Map<String, Object>> empSchList = (List<Map<String, Object>>) clinicMap.get("empSchList");
            for (Map<String, Object> empSchMap : empSchList) {
                String pkSchresEmp = CommonUtils.getString(empSchMap.get("pkSchresEmp"));
                String pkSchsrvEmp = CommonUtils.getString(empSchMap.get("pkSchsrvEmp"));
                String pkSchresDept = CommonUtils.getString(empSchMap.get("pkSchresDept"));
                String pkSchsrvDept = CommonUtils.getString(empSchMap.get("pkSchsrvDept"));
                if (opPvpkSchsrv.equals(pkSchsrvEmp) && opPvpkSchres.equals(pkSchresEmp) || opPvpkSchsrv.equals(pkSchsrvDept) && opPvpkSchres.equals(pkSchresDept)) {
                    return "1";
                }
            }
        }
        return "0";
    }

    public String qryEmpSch(String param, IUser user) {
        //dateWork/timeNow/pkDept
        Map<String, Object> paramMap = new HashMap<String, Object>();
        boolean hasSch = false;
        Date d = new Date();
        User u = (User) user;
        String dateStr = DateUtils.getDateStr(d);
        paramMap.put("dateWork", dateStr);
        paramMap.put("timeNow", DateUtils.getDate("HH:mm:ss"));
        paramMap.put("pkDept", u.getPkDept());
        paramMap.put("pkEmp", u.getPkEmp());
        List<Map<String, Object>> opPvEmpsrvtypeList = patiPvDao.qrySchEmpsrvtypeDateslot(paramMap);
        if (opPvEmpsrvtypeList != null && opPvEmpsrvtypeList.size() > 0) {
            for (Map<String, Object> map : opPvEmpsrvtypeList) {
                String cntTotal = CommonUtils.getString(map.get("cntTotal"));
                if (StringUtils.isNotBlank(cntTotal) && Double.parseDouble(cntTotal) > 0) {
                    hasSch = true;
                    break;
                }
            }
        }
        return hasSch ? "1" : "0";
    }

    //判断是否有接诊当前患者的权限/急诊到不了此方法
    public void isHasClinicRight(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        User u = (User) user;
        Map<String, Object> clinicMap = clinicVaild(paramMap, u);
        Boolean isSameEmpPhy = (Boolean) clinicMap.get("opIsSamePhy");
        if (isSameEmpPhy) {
            return;//针对门诊反复接诊情况
        }
        SyxPiPv piPvInfo = (SyxPiPv) clinicMap.get("piPvInfo");
        Map<String, Object> schEmp = DataBaseHelper.queryForMap(" select max(res.dt_cnlevel) code  from sch_resource res where res.eu_restype='1' and res.pk_emp=?  ", new Object[]{u.getPkEmp()});
        String empSrvCode = MapUtils.getString(schEmp, "code");
        //没资源、或者挂号的资源、医生的资源有一个出诊级别是空的，就默认可以接诊。只比较都有值的情况，接诊的时候也做了处理
        if (StringUtils.isNoneBlank(piPvInfo.getOpSrvCode(), empSrvCode) &&
                piPvInfo.getOpSrvCode().compareTo(empSrvCode) > 0) {
            throw new BusException("没有权限接诊该患者，请确认患者的挂号资源出诊级别");
        }
    }

    //接诊患者
    public void clinic(String param, IUser user) {
        //pkPv/pkOrgarea/pkEmp
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        User u = (User) user;
        Map<String, Object> clinicMap = clinicVaild(paramMap, u);
        Boolean isSameEmpPhy = (Boolean) clinicMap.get("opIsSamePhy");
        String pkDeptArea = MapUtils.getString(paramMap,"pkDeptArea");
        String pkPv = MapUtils.getString(paramMap,"pkPv");
        Boolean flagSch = "1".equals(MapUtils.getString(paramMap,"flagSch"));//是否从前台确认资源，及无排班情况正常接诊

        //写log日志
        logger.info("接诊初始参数{}",param);
        //更新预约记录
        try{
            String sql = "select * from PV_OP where PK_PV = ?";
            PvOp pvOp = DataBaseHelper.queryForBean(sql,PvOp.class,new Object[]{pkPv});

            sql = "update SCH_APPT set EU_STATUS = '1' where PK_SCHAPPT = ?";
            DataBaseHelper.execute(sql,new Object[]{pvOp.getPkSchappt()});
            //todo
            sql = "update SCH_APPT_PV set PK_PV = ? where PK_SCHAPPT = ?";
            DataBaseHelper.execute(sql,new Object[]{pkPv,pvOp.getPkSchappt()});
        }catch (Exception e){

        }

        PvEncounter pvInfo=DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?",PvEncounter.class,new Object[]{pkPv});

        if(flagSch){
            logger.info("资源接诊挂号信息：{}",JsonUtil.writeValueAsString(clinicMap));
            logger.info("资源接诊患者信息：{}",JsonUtil.writeValueAsString(pvInfo));
            clincZy(paramMap,clinicMap,pvInfo,u);
        }else{
            logger.info("排班接诊挂号信息：{}",JsonUtil.writeValueAsString(clinicMap));
            logger.info("排班接诊患者信息：{}",JsonUtil.writeValueAsString(pvInfo));
            clincPb(paramMap,clinicMap,pvInfo,u);
        }
    }
    //有排班接诊
    private void clincPb(Map<String, Object> paramMap,Map<String, Object> clinicMap,PvEncounter pvInfo,User u){

        Boolean isSameEmpPhy = (Boolean) clinicMap.get("opIsSamePhy");
        String pkDeptArea = MapUtils.getString(paramMap,"pkDeptArea");
        String pkPv = MapUtils.getString(paramMap,"pkPv");
        boolean isNotUpdateBeginDate = "1".equals(MapUtils.getString(paramMap,"isNotUpdateBeginDate"));

        if (isSameEmpPhy || isNotUpdateBeginDate && pvInfo.getDateBegin()!=null) {//针对门诊反复接诊情况//就诊中再次接诊,按update条件是不用更新pv_encounter?
            //完成接诊后再次接诊
            DataBaseHelper.update("update pv_encounter set eu_status = '1', eu_locked='1',pk_dept_area=nvl(?,pk_dept_area) where pk_pv = ? and eu_locked = '0'  ", new Object[]{pkDeptArea,pkPv});
            return;
        }

        String pkResCn = "";
        String pkSchsrvCn = "";
        String pkSchsrv="";
        Date dateBegin=new Date();
        Date dateEnd=new Date();
        //--使用当前医生排班的资源和服务更新数据
        List<Map<String, Object>> empSchList = (List<Map<String, Object>>) clinicMap.get("empSchList");
        SyxPiPv piPvInfo = (SyxPiPv) clinicMap.get("piPvInfo");
        String euPvtype = piPvInfo.getEuPvtype();
        String pkEmpPhy = piPvInfo.getPkEmpPhy();

        boolean isMz = "1".equals(euPvtype) || "4".equals(euPvtype);
        //没有医生排班，且已经通过了前置验证（前端按照配置调用接诊权限那些），找一个合适的资源信息赋值即可。
        if(CollectionUtils.isEmpty(empSchList)){
            Map<String,Object> empQryParam = new HashMap<>();
            empQryParam.put("pkEmp",u.getPkEmp());
            empQryParam.put("dtCnlevel",piPvInfo.getOpSrvCode());
            empQryParam.put("dept",u.getPkDept());
            empSchList = DataBaseHelper.queryForList("select res.PK_SCHRES pk_schres_emp,res.PK_SCHSRV pk_schsrv_emp,res.PK_DEPT pk_schres_dept," +
                    " '' pk_schsrv_dept from sch_resource res where res.eu_restype='1' and res.pk_emp=:pkEmp" +
                    (isMz?" and nvl(res.dt_cnlevel,'~')>=nvl(:dtCnlevel,'0') and res.pk_dept_belong=:dept":"") + " order by res.dt_cnlevel", empQryParam);
            if(isMz && CollectionUtils.isEmpty(empSchList)){
                throw new BusException("医生在当前科室下没有符合条件的排班资源！");
            }
        }
        if(CollectionUtils.isEmpty(empSchList)){
            throw new BusException("医生没有符合条件的排班信息！");
        }

        if (isMz) { //门诊选择最合适的资源接诊
            String opPvpkSchsrv = piPvInfo.getOpPkSchsrv();
            String opPvpkSchres = piPvInfo.getOpPkRes();
            boolean findResEmp = false;
            for (Map<String, Object> empSchMap : empSchList) {
                String pkSchresEmp = CommonUtils.getString(empSchMap.get("pkSchresEmp"));
                String pkSchsrvEmp = CommonUtils.getString(empSchMap.get("pkSchsrvEmp"));
                if (opPvpkSchsrv.equals(pkSchsrvEmp) && opPvpkSchres.equals(pkSchresEmp)) {
                    pkResCn = pkSchresEmp;
                    pkSchsrvCn = pkSchsrvEmp;
                    findResEmp = true;
                    break;
                }
            }
            if (!findResEmp) {
                for (Map<String, Object> empSchMap : empSchList) {
                    String pkSchresDept = CommonUtils.getString(empSchMap.get("pkSchresDept"));
                    String pkSchsrvDept = CommonUtils.getString(empSchMap.get("pkSchsrvDept"));
                    if (opPvpkSchsrv.equals(pkSchsrvDept) && opPvpkSchres.equals(pkSchresDept)) {
                        String pkSchresEmp = CommonUtils.getString(empSchMap.get("pkSchresEmp"));
                        String pkSchsrvEmp = CommonUtils.getString(empSchMap.get("pkSchsrvEmp"));
                        pkResCn = pkSchresEmp;
                        pkSchsrvCn = pkSchsrvEmp;
                        findResEmp = true;
                        break;
                    }
                }
            }
            if (!findResEmp) {
                Map<String, Object> schEmpMap = empSchList.get(0);
                pkResCn = CommonUtils.getString(schEmpMap.get("pkSchresEmp"));
                pkSchsrvCn = CommonUtils.getString(schEmpMap.get("pkSchsrvEmp"));
            }
        } else {  //急诊只要有当前科室当天的排班就可以接诊
            Map<String, Object> schEmpMap = empSchList.get(0);
            pkResCn = CommonUtils.getString(schEmpMap.get("pkSchresEmp"));
            pkSchsrvCn = CommonUtils.getString(schEmpMap.get("pkSchsrvEmp"));
            pkSchsrv= CommonUtils.getString(schEmpMap.get("pkSchsrv"));
            dateBegin=pvInfo.getDateBegin();
            dateEnd=ApplicationUtils.getPvDateEndEr(dateBegin);
        }

        if ("2".equals(euPvtype)) { //急诊
            DataBaseHelper.update("update pv_er  set pk_res_cn  = ?, pk_schsrv_cn =?,PK_SCHSRV=?,date_begin=?,date_end=? where pk_pv = ?", new Object[]{pkResCn, pkSchsrvCn,pkSchsrv,dateBegin,dateEnd, pkPv});
            if (!u.getPkEmp().equals(pkEmpPhy)) {
                PvStaff pvStaff = new PvStaff();
                pvStaff.setPkPv(CommonUtils.getString(pkPv));
                pvStaff.setPkOrg(u.getPkOrg());
                pvStaff.setPkDept(u.getPkDept());
                pvStaff.setPkEmp(u.getPkEmp());
                pvStaff.setNameEmp(u.getNameEmp());
                pvStaff.setDateBegin(new Date());
                DataBaseHelper.insertBean(pvStaff);
            }
        } else {
            DataBaseHelper.update("update pv_op  set pk_res_cn  = ?, pk_schsrv_cn =? where pk_pv = ?", new Object[]{pkResCn, pkSchsrvCn, pkPv});
        }
        //更新分诊队列状态（根据配置（PV0008）确定是否启用分诊叫号，启用则更新，未启用不操作）
        //String isTriage = ApplicationUtils.getSysparam("PV0008", false) ;
        //if("1".equals(isTriage))
        DataBaseHelper.update("update pv_que  set eu_status='1' where pk_pv=? and eu_status='0' ", new Object[]{pkPv});
        //门诊初次接诊/急诊多次接诊
        //20201120-接诊其他医生时不更新首诊医生的处理逻辑
        Boolean isOpNoUpPhy = (Boolean) clinicMap.get("isOpNoUpPhy");
        StringBuilder updateSql = new StringBuilder("update pv_encounter set eu_status = '1',eu_locked='1',pk_dept_area=nvl(?,pk_dept_area) ")
                .append(!isOpNoUpPhy?", pk_dept=?, date_clinic=?,pk_emp_phy= ?,name_emp_phy =?":"")
                .append(" where pk_pv = ? and eu_locked = '0' ");
        Object[] objects = isOpNoUpPhy?new Object[]{pkDeptArea,pkPv}:
                new Object[]{pkDeptArea,u.getPkDept(), new Date(), u.getPkEmp(), u.getNameEmp(), pkPv};
        DataBaseHelper.update(updateSql.toString(), objects);

        //发送门诊到诊消息ADT^A10
        PlatFormSendUtils.sendOpArriveMsg(paramMap);
    }

    private void clincZy(Map<String, Object> paramMap,Map<String, Object> clinicMap,PvEncounter pvInfo,User u){
        String euPvtype = pvInfo.getEuPvtype();
        boolean isMz = !"2".equals(euPvtype);
        Boolean isSameEmpPhy = (Boolean) clinicMap.get("opIsSamePhy");

        String pkDeptArea = MapUtils.getString(paramMap,"pkDeptArea");
        String pkPv = MapUtils.getString(paramMap,"pkPv");
        String pkSchsrvcn = MapUtils.getString(paramMap,"pkSchsrv");//接诊资源
        String pkSchrescn=MapUtils.getString(paramMap,"pkSchres");//接诊服务
        boolean isNotUpdateBeginDate="1".equals(MapUtils.getString(paramMap,"pkSchres"));
        String pkSchsrv=null;
        String pkSchres=null;
        String pkDateslot=null;
        String pkSch=null;

        //查询当前患者预约资源
        String sql="select APPT.PK_DATESLOT,APPT.PK_SCHRES,APPT.PK_SCHSRV,APPT.PK_SCH\n" +
                "from SCH_APPT_PV apptpv\n" +
                "INNER JOIN SCH_APPT appt on APPTPV.PK_SCHAPPT=appt.PK_SCHAPPT\n" +
                "where APPTPV.PK_PV =? ";
        List<Map<String,Object>> empQryParam = DataBaseHelper.queryForList(sql,new Object[]{pkPv});
        if(empQryParam!=null && empQryParam.size()>0){
            pkSchsrv=MapUtils.getString(empQryParam.get(0),"pkSchsrv");
            pkSchres=MapUtils.getString(empQryParam.get(0),"pkSchres");
            pkDateslot=MapUtils.getString(empQryParam.get(0),"pkDateslot");
            pkSch=MapUtils.getString(empQryParam.get(0),"pkSch");
        }

        Date dateBegin=new Date();
        Date dateEnd=new Date();
        if(isNotUpdateBeginDate){
            //更正就诊信息
            if(pvInfo.getDateBegin()!=null){ //反复就诊
                //完成接诊后再次接诊
                DataBaseHelper.update("update pv_encounter set eu_status = '1', eu_locked='1',pk_dept_area=nvl(?,pk_dept_area) where pk_pv = ? and eu_locked = '0'  ", new Object[]{pkDeptArea,pkPv});
                return;
            }
        }else{
            //更正就诊信息
            if("2".equals(pvInfo.getEuStatus())){ //反复就诊
                //完成接诊后再次接诊
                DataBaseHelper.update("update pv_encounter set eu_status = '1', eu_locked='1',pk_dept_area=nvl(?,pk_dept_area),date_clinic=? where pk_pv = ? and eu_locked = '0'  ", new Object[]{pkDeptArea,new Date(),pkPv});
                return;
            }
        }


        if(isMz){
            logger.info("资源接诊前：{}",JsonUtil.writeValueAsString(getOpOp(pkPv)));
            dateBegin=pvInfo.getDateBegin();
            dateEnd=ApplicationUtils.getPvDateEnd(dateBegin);
            DataBaseHelper.update("update pv_op  set pk_schsrv=?,pk_res=?,pk_dateslot=?,pk_sch=?,date_begin=?,date_end=?,pk_res_cn=?, pk_schsrv_cn=? where pk_pv = ?",
                    new Object[]{ pkSchsrv,pkSchres,pkDateslot,pkSch,dateBegin,dateEnd,pkSchrescn,pkSchsrvcn,pkPv});
        }else{
            logger.info("资源接诊前：{}",JsonUtil.writeValueAsString(getOpEr(pkPv)));
            dateBegin=pvInfo.getDateBegin();
            dateEnd=ApplicationUtils.getPvDateEndEr(dateBegin);
            DataBaseHelper.update("update pv_er  set pk_schsrv=?,pk_res=?,pk_dateslot=?,pk_sch=?,date_begin=?,date_end=?,pk_res_cn=?, pk_schsrv_cn=? where pk_pv = ?",
                    new Object[]{ pkSchsrv,pkSchres,pkDateslot,pkSch,dateBegin,dateEnd,pkSchrescn,pkSchsrvcn,pkPv});
            if (!u.getPkEmp().equals(pvInfo.getPkEmpPhy())) {
                PvStaff pvStaff = new PvStaff();
                pvStaff.setPkPv(CommonUtils.getString(pkPv));
                pvStaff.setPkOrg(u.getPkOrg());
                pvStaff.setPkDept(u.getPkDept());
                pvStaff.setPkEmp(u.getPkEmp());
                pvStaff.setNameEmp(u.getNameEmp());
                pvStaff.setDateBegin(new Date());
                DataBaseHelper.insertBean(pvStaff);
            }
        }

        Boolean isOpNoUpPhy = (Boolean) clinicMap.get("isOpNoUpPhy");
        StringBuilder updateSql = new StringBuilder("update pv_encounter set eu_status = '1',eu_locked='1',pk_dept_area=nvl(?,pk_dept_area) ")
                .append(!isOpNoUpPhy?", pk_dept=?, date_clinic=?,pk_emp_phy= ?,name_emp_phy =?":"")
                .append(" where pk_pv = ? and eu_locked = '0' ");
        Object[] objects = isOpNoUpPhy?new Object[]{pkDeptArea,pkPv}:
                new Object[]{pkDeptArea,u.getPkDept(), new Date(), u.getPkEmp(), u.getNameEmp(), pkPv};
        DataBaseHelper.update(updateSql.toString(), objects);
        logger.info("资源接诊后：{}",JsonUtil.writeValueAsString(getOpOp(pkPv)));
        logger.info("资源接诊后：{}",JsonUtil.writeValueAsString(getOpEr(pkPv)));
    }

    private PvEr getOpEr(String pkPv){
        PvEr pvEr=DataBaseHelper.queryForBean("select * from pv_er where pk_pv=?" ,PvEr.class,pkPv);
        return pvEr;
    }
    private PvOp getOpOp(String pkPv){
        PvOp pvOp=DataBaseHelper.queryForBean("select * from pv_op where pk_pv=?" ,PvOp.class,pkPv);
        return pvOp;
    }
    //判断待接诊患者是否被锁定，以及挂号时间是否有效
    public Map<String, Object> clinicVaild(Map<String, Object> paramMap, User u) {
        String pkPv = paramMap.get("pkPv") != null ? paramMap.get("pkPv").toString() : "";
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("传参pkPv为空！");
        }
        List<SyxPiPv> pvEncounters = patiPvDao.qryPiPvInfo(paramMap);
        if (pvEncounters == null || pvEncounters.size() != 1) {
            throw new BusException("查询患者就诊记录有误，请联系管理员！");
        }
        Map<String, Object> clinicMap = new HashMap<String, Object>();
        SyxPiPv pvEncounter = pvEncounters.get(0);
        String euLocked = pvEncounter.getEuLocked();
        String pkEmpPhy = pvEncounter.getPkEmpPhy();
        String euPvtype = pvEncounter.getEuPvtype();//1 门诊 2 急诊

        String pkEmp = u.getPkEmp();
        String flagCancel = pvEncounter.getFlagCancel();
        boolean isOpNoUpPhy = false;
        if ("1".equals(flagCancel)) {
            throw new BusException("该患者已退号！");
        }
        String pv0057 = ApplicationUtils.getSysparam("PV0057", false);
        if(!"0".equals(pv0057)){
            pv0057 = "1";
        }
        if ("0".equals(euLocked)) { //门诊中只要一个医生接诊了，那么完成接诊以后只能这个医生接诊,急诊不是，急诊换医生接诊要写pv_staff
            String pv0035 = ApplicationUtils.getSysparam("PV0035", false, "请维护好系统参数PV0035！");
            if ("0".equals(pv0035)) {
                if ("1".equals(euPvtype) && StringUtils.isNotBlank(pkEmpPhy) && !pkEmp.equals(pkEmpPhy)) {
                    throw new BusException("该患者已经被" + pvEncounter.getNameEmpPhy() + "接诊！");
                }
            }
            //20201120-接诊其他医生时不更新首诊医生的处理逻辑
            if("2".equals(pv0035)) {
            	//【门诊医生站】急诊患者首诊医生处理 任务4612 begin
            	isOpNoUpPhy = (("1".equals(euPvtype) || "2".equals(euPvtype)) && StringUtils.isNotBlank(pkEmpPhy) && !pkEmp.equals(pkEmpPhy));
            	//【门诊医生站】急诊患者首诊医生处理 任务4612 end
            }
        } else if ("1".equals(euLocked)) { //门诊中接诊后允许已经接诊的医师再次接诊，但不用再次更新pv表，只加锁
            if (!pkEmp.equals(pkEmpPhy) && "1".equals(pv0057)) {
                throw new BusException("该患者正在被" + pvEncounter.getNameEmpPhy() + "接诊！");
            }
        } else if ("2".equals(euLocked)) {
            if("1".equals(pv0057)){
                throw new BusException("该患者正在做收费处理，不能被接诊！");
            }

        }

        Date d = new Date();
        Date dateEnd = pvEncounter.getDateEnd()==null? pvEncounter.getEuPvtype().equals(("2"))?ApplicationUtils.getPvDateEndEr(pvEncounter.getPvDateBegin()): ApplicationUtils.getPvDateEnd(pvEncounter.getPvDateBegin()):pvEncounter.getDateEnd();//人民医院急诊调用，不判断有效日期
        if (d.after(dateEnd)) { //
            throw new BusException(pvEncounter.getNamePi() + "挂号有效期为" + DateUtils.formatDate(dateEnd,"yyyy-MM-dd HH:mm")+ "！\n挂号已过期！");
        }
        String dateStr = DateUtils.getDateStr(d);
        paramMap.put("dateWork", dateStr);
        paramMap.put("timeNow", DateUtils.getDate("HH:mm:ss"));
        paramMap.put("pkDept", u.getPkDept());
        paramMap.put("pkEmp", u.getPkEmp());
        List<Map<String, Object>> empSchList = patiPvDao.qrySchEmpsrvtype(paramMap);
        String type=qryPb(paramMap);
        //取参数PV0045（接诊是否判断当前医生是否有排班），0 科室和人员资源都判断，1 科室资源不判断，2 科室和人员资源都不判断
        String validDsch =ApplicationUtils.getSysparam("PV0045", false);
        validDsch = StringUtils.isBlank(validDsch)?EnumerateParameter.ZERO:validDsch;

        boolean isDeptSch = EnumerateParameter.ZERO.equals(pvEncounter.getEuRestype());
        String errTip = "[挂号资源："+(isDeptSch?"科室":"人员")+"]医生在当前科室当天的排班为空！";

        //0：科室和人医必须都有；1：医生必须有，科室无所谓；2：有排班就行不区分
        if(EnumerateParameter.ZERO.equals(validDsch) && (StringUtils.isEmpty(type) || !EnumerateParameter.TWO.equals(type) )){
            throw new BusException(errTip);
        }
        if(EnumerateParameter.ONE.equals(validDsch) && (StringUtils.isEmpty(type) || EnumerateParameter.ZERO.equals(type) )){
            throw new BusException(errTip);
        }

        clinicMap.put("piPvInfo", pvEncounter);
        clinicMap.put("opIsSamePhy", ("1".equals(euPvtype) && StringUtils.isNotBlank(pkEmpPhy) && pkEmp.equals(pkEmpPhy)));
        clinicMap.put("isOpNoUpPhy", isOpNoUpPhy);
        clinicMap.put("empSchList", empSchList);
        return clinicMap;
    }

    /**
     * 判断当前医生是否有排班 type=1 医生；type=0 科室；type=null表示无排班;type=2表示都有
     * @param paramMap
     * @return
     */
    public String qryPb(Map<String, Object> paramMap){
        List<Map<String, Object>> empSchList = patiPvDao.qrySchDateslot(paramMap);
        String type=null;
        if(empSchList!=null && empSchList.size()>0){
            List<Map<String, Object>> empList=new ArrayList<>();
            for(Map vo :empSchList){
                if(StringUtils.isNotEmpty(MapUtils.getString(vo,"pkEmp"))){
                    empList.add(vo);
                }
            }
            if (empList.size() > 0 && empList.size() == empSchList.size())
            {
                type = "1";
                return type;//当前医生排班资源
            }
            if (empList.size() == 0)
            {
                type = "0";
                return type;//当前科室排班资源
            }
            type = "2";//同时存在
        }

        return type;
    }

    //CancelClinic 取消接诊:就诊1/结束2——>登记0
    public void cancelClinic(String param, IUser user) {
        //pkPv
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = paramMap.get("pkPv") != null ? paramMap.get("pkPv").toString() : "";
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("取消接诊当前患者时，传参pkPv为空！");
        }
        String stopCallNum = paramMap.get("stopCallNum") != null ? paramMap.get("stopCallNum").toString() : "";
        if (StringUtils.isBlank(stopCallNum)) {
            throw new BusException("取消接诊当前患者时，传参stopCallNum为空！");
        }

//        try{
//            //4664 【门诊医生站】取消就诊时物理删除患者对应的医嘱、病历、诊断等，删除对应的就诊记录
//            //：医嘱，病历，诊断，收费明细，检查/检验申请单 物理删除,其他逻辑保持不变
//            String sql = "select *\n" +
//                    "from pv_encounter pv\n" +
//                    "         inner join bl_settle st on pv.pk_pv = st.pk_pv\n" +
//                    "         inner join bd_hp hp on st.pk_insurance = hp.pk_hp\n" +
//                    "where st.dt_sttype = '01'\n" +
//                    "  and st.eu_stresult = '0'\n" +
//                    "  and pv.eu_pvtype in (1, 2, 4)\n" +
//                    "and pv.PK_PV = ?\n" +
//                    "order by st.date_st desc";
//            List<Map<String,Object>> result = DataBaseHelper.queryForList(sql,new Object[]{pkPv});
//            List<String> orderNo = new ArrayList<>();
//            if(result.size()<=0){
//                //获取检查检验申请单号
//                sql = "select PK_CNORD from CN_ORDER where PK_PV = ?";
//                orderNo = DataBaseHelper.queryForList(sql,String.class,new Object[]{pkPv});
//                //更新就诊记录状态为9
//                sql = "update PV_ENCOUNTER set EU_STATUS = '9' where PK_PV = ?";
//                DataBaseHelper.execute(sql,new Object[]{pkPv});
//                //逻辑删除医嘱
//                sql = "delete from CN_ORDER where PK_PV = ?";
//                DataBaseHelper.execute(sql,new Object[]{pkPv});
//                //删除病例，删除就诊文书
//                //改前端调用交易码
//                sql = "delete from pv_doc where pk_pv=?";
//                DataBaseHelper.execute(sql,new Object[]{pkPv});
////                sql = "delete from cn_emr_op where pk_pv=?";
////                DataBaseHelper.execute(sql,new Object[]{pkPv});
//                //逻辑删除诊断
//                sql = "delete from PV_DIAG where PK_PV = ?";
//                DataBaseHelper.execute(sql,new Object[]{pkPv});
//                //清空预约记录就诊主键
//                sql = "update SCH_APPT_PV set DEL_FLAG = '1' where PK_PV = ?";
//                DataBaseHelper.execute(sql,new Object[]{pkPv});
//                //删除收费明细
//                sql = "delete from bl_op_dt where PK_PV = ?";
//                DataBaseHelper.execute(sql,new Object[]{pkPv});
//                //记录系统应用日志
//                //todo
//                //删除检查检验申请单
//
//                orderNo.forEach(a->{
//                    String tempsql = "delete from CN_LAB_APPLY where PK_CNORD = ?";
//                    DataBaseHelper.execute(tempsql,new Object[]{a});
//                    tempsql = "delete from CN_RIS_APPLY where PK_CNORD = ?";
//                    DataBaseHelper.execute(tempsql,new Object[]{a});
//                });
//
//
//
//            }
//        }catch (Exception e){
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        }


        Integer ordSize = DataBaseHelper.queryForScalar("select count(1) from cn_order where pk_pv=? and DEL_FLAG = '0'", Integer.class, new Object[]{pkPv});
        if (ordSize > 0) {
            throw new BusException("已开立医嘱，不能取消接诊！");
        }
        Integer docSize = DataBaseHelper.queryForScalar("select count(1) from  pv_doc  where pk_pv=? and del_flag='0' ", Integer.class, new Object[]{pkPv});
        if (docSize > 0) {
            throw new BusException("已有病历，不能取消接诊！");
        }
        //User u = (User)user;
        //更新就诊状态，解锁
        int upCount = DataBaseHelper.update("update pv_encounter  set eu_status='0', pk_emp_phy=null, name_emp_phy=null, eu_locked='0' where pk_pv=? and eu_locked='1' ", new Object[]{pkPv});
        if (upCount > 0) {
            List<Map<String, Object>> opMap = DataBaseHelper.queryForList("select * from pv_op where pk_pv=?", new Object[]{pkPv});
            List<Map<String, Object>> erMap = DataBaseHelper.queryForList("select * from pv_er where pk_pv=?", new Object[]{pkPv});
            if (opMap.size() > 0) {
                DataBaseHelper.update("update pv_encounter  set pk_dept=?,pk_emp_phy = null,name_emp_phy = null where pk_pv=?", new Object[]{opMap.get(0).get("pkDeptPv"), pkPv});
            } else if (erMap.size() > 0) {
                DataBaseHelper.update("update pv_encounter  set pk_dept=?,pk_emp_phy = null,name_emp_phy = null where pk_pv=?", new Object[]{erMap.get(0).get("pkDeptPv"),  pkPv});
            }
            DataBaseHelper.update("update pv_op  set pk_res_cn=null, pk_schsrv_cn=null where pk_pv=?", new Object[]{pkPv});
            DataBaseHelper.update("update pv_er  set pk_res_cn=null, pk_schsrv_cn=null where pk_pv=?", new Object[]{pkPv});
            //更新分诊队列状态（根据配置（PV0008）确定是否启用分诊叫号，启用则更新，未启用不操作）
            //String isTriage = ApplicationUtils.getSysparam("PV0008", false) ;
            //if("1".equals(isTriage))
            if ("1".equals(stopCallNum)) {
                DataBaseHelper.update("update pv_que set eu_status='8', date_over=?, cnt_over=cnt_over+1  where pk_pv=? and eu_status='1' ", new Object[]{new Date(), pkPv});
            } else {
                DataBaseHelper.update("update pv_que  set eu_status='0' where pk_pv=? and eu_status > '0	' ", new Object[]{pkPv});
            }

        } else {
            throw new BusException("取消接诊失败！");
        }



        //发送完成接诊消息ADT^A03
        PlatFormSendUtils.sendCancelClinicMsg(paramMap);
    }

    //完成接诊
    public void finishClinic(String param, IUser user) {
        //pkPv/euPvtype
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = paramMap.get("pkPv") != null ? paramMap.get("pkPv").toString() : "";
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("完成接诊当前患者时，传参pkPv为空！");
        }
        //更新患者就诊状态为“结束”//医生就诊解锁；
        int upPv = DataBaseHelper.update("update  pv_encounter  set eu_status='2', date_end = ? , eu_locked = case eu_locked when '2' then eu_locked else '0' end  where pk_pv=? ", new Object[]{new Date(),pkPv});
//        if (upPv == 0) {
//            throw new BusException("完成接诊失败！");//返回列表重新进入后在尝试！
//        }

        //清除多余数据，此处为收费发现存在无医嘱，无处方的费用信息，无法复现问题来源，在最后诊毕时进行删除
        String sql="select a.* from BL_OP_DT a Left JOIN CN_ORDER b on a.PK_CNORD=b.PK_CNORD " +
                "where a.FLAG_SETTLE='0' and a.PK_CNORD is not null and b.PK_CNORD is NULL and a.PK_PV=?";
        List<BlOpDt> opDtList=DataBaseHelper.queryForList(sql,BlOpDt.class,new Object[]{pkPv});
        if(opDtList!=null && opDtList.size()>0){
            DataBaseHelper.batchUpdate("delete from bl_op_dt where PK_CGOP=:pkCgop",opDtList);
        }
        //删除患者无医嘱处方数据
        sql ="select a.* from CN_PRESCRIPTION a Left JOIN CN_ORDER b on a.PK_PRES=b.PK_PRES " +
                "where b.PK_PRES is NULL and a.PK_PV=?";
        List<CnPrescription> cnPrescriptionList=DataBaseHelper.queryForList(sql,CnPrescription.class,new Object[]{pkPv});
        if(cnPrescriptionList!=null && cnPrescriptionList.size()>0){
            DataBaseHelper.batchUpdate("delete from CN_PRESCRIPTION where PK_PRES=:pkPres",cnPrescriptionList);
        }

        completePvQue(pkPv);
        //发送完成接诊消息ADT^A03
        PlatFormSendUtils.sendFinishClinicMsg(paramMap);
        ExtSystemProcessUtils.processExtMethod("FollowUp", "uploadOrder", pkPv);

        //发送急的检验检查信息
        Map<String,Object> paramVo=new HashMap<String, Object>();
        paramVo.put("pkPv", pkPv);
        PlatFormSendUtils.sendOpEmeOrdMag(paramVo);
    }

    public void completePvQue(String pkPv){
        //更新分诊队列状态（根据配置（PV0008）确定是否启用分诊叫号，启用则更新，未启用不操作）
        String isTriage = ApplicationUtils.getSysparam("PV0008", false) ;
        if("1".equals(isTriage)){
            DataBaseHelper.update("update pv_que  set eu_status='2' where pk_pv=? and eu_status='1' ", new Object[]{pkPv});
        }
    }

    //预约患者查询（按照排班资源改为按照人员查询） old:004003003010
    public List<Map<String, Object>> qryApptPatis(String param, IUser user) {
        //pkSchres/pkDeptEx/codePi/namePi/dtSex/idNo/mobile/cardNo
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String dateStr = DateUtils.getDateStr(new Date());
        paramMap.put("dateNow", dateStr);
        List<Map<String, Object>> apptPatisList = patiPvDao.qryApptPatis(paramMap);
        return apptPatisList;
    }

    // 预约签到   old:004003003011  逻辑待确认
    public List<Map<String, Object>> apptPatient(String param, IUser user) {
        ApptPatient pati = JsonUtil.readValue(param, new TypeReference<ApptPatient>() {
        });
        String pkPicate = pati.getPkPicate();
        if (StringUtils.isNoneBlank(pkPicate)) {
            int count = DataBaseHelper.queryForScalar("select count(1) from pi_cate where pk_picate = ? and del_flag = '0'", Integer.class, pkPicate);
            //如果根据患者分类参数无法获取记录，说明该值是错误的，置为空，重新查询默认的
            if (count == 0) {
                pkPicate = null;
            }
        }
        if (StringUtils.isBlank(pkPicate)) {
            PiCate piCate = DataBaseHelper.queryForBean("select * from pi_cate where flag_def = '1' and del_flag = '0'", PiCate.class);
            pkPicate = piCate.getPkPicate();
        }
        List<PvOpParam> paramList = new ArrayList<PvOpParam>();
        PvOpParam vo = new PvOpParam();
        vo.setOrderNo("1");
        vo.setPkPi(pati.getPkPi());
        vo.setPkPicate(pkPicate);
        SchAppt appt = DataBaseHelper.queryForBean("select * from sch_appt where pk_schappt = ?", SchAppt.class, pati.getPkSchappt());
        vo.setPkSchsrv(pati.getPkSchappt());
        vo.setPkSchsrv(appt.getPkSchsrv());
        vo.setPkRes(appt.getPkSchres());
        vo.setPkDateslot(appt.getPkDateslot());
        vo.setPkSch(appt.getPkSch());
        vo.setPkAppo(appt.getPkSchappt());
        String pkHp = "";
        PiInsurance insu = DataBaseHelper.queryForBean("select * from pi_insurance where pk_pi = ? and flag_def = '1'", PiInsurance.class, pati.getPkPi());
        if (insu != null) {
            int count = DataBaseHelper.queryForScalar("select count(1) from bd_hp where pk_hp = ? and del_flag = '0'", Integer.class, insu.getPkHp());
            if (count == 0) { //错误的医保信息

            } else {
                pkHp = insu.getPkHp();
            }
        }
        if ("".equals(pkHp)) { //如果无法获取患者医保，取医保类型为全自费的第一条
            List<PiInsurance> insuList = DataBaseHelper.queryForList("select pk_hp from bd_hp where EU_HPTYPE = '0' and FLAG_OP = '1' and del_flag = '0'", PiInsurance.class);
            if (CollectionUtils.isNotEmpty(insuList)) {
                pkHp = insuList.get(0).getPkHp();
            } else {
                throw new BusException("无法获取患者医保信息 ！");
            }
        }
        vo.setPkInsu(pkHp);
        vo.setPkDeptPv(UserContext.getUser().getPkDept());
        vo.setPkEmpPv(UserContext.getUser().getPkEmp());
        vo.setNameEmpPv(UserContext.getUser().getNameEmp());
        paramList.add(vo);
        regPubService.saveAppoPvEncounterAndOp(paramList, user);

        DataBaseHelper.update("update sch_appt set eu_status = '1' where pk_schappt = ?", new Object[]{pati.getPkSchappt()});
        return null;

    }

    //  加号处理   old :004003003007
    public void addRegPatient(String param, IUser user) {

        Map<String, Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {
        });
        String pkPi = CommonUtils.getString(paramMap.get("pkPi"));
        if (StringUtils.isBlank(pkPi)) {
            throw new BusException("加号传参pkPi为空！");
        }
        //1.有排班资源，校验当前是否有可用的排班
        User u = (User) user;
        String pkEmp = u.getPkEmp();
        String pkDept = u.getPkDept();
        Date d = new Date();
        String dateStr = DateUtils.getDateStr(d);
        Date dateNow = DateUtils.getDateMorning(d, 0);
        paramMap.put("dateWork", dateStr);
        paramMap.put("timeNow", DateUtils.getDate("HH:mm:ss"));
        paramMap.put("pkDept", pkDept);
        paramMap.put("pkEmp", pkEmp);
        List<Map<String, Object>> empSchList = patiPvDao.qrySchEmpsrvtype(paramMap);
        if (empSchList == null || empSchList.size() <= 0) {//查询医生当前的排班服务和排班资源可能有多条
            throw new BusException("查询医生当前的排班为空，请联系管理员！");
        }
        //4.校验该患者在该科室是否已加过号
        String sql = "select count(1) from sch_appt_pv sap inner join sch_appt sa on sa.pk_schappt=sap.pk_schappt where sap.del_flag='0' and sap.eu_apptmode='1' and sap.pk_emp_phy=?  and sa.pk_pi=? and sa.date_appt=to_date(?,'yyyyMMdd') ";
        int count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkEmp, pkPi, dateStr});
        if (count > 0) {
            throw new BusException("已对当前患者加过号！");
        }
        Map<String, Object> sch = null;
        if (empSchList != null && empSchList.size() > 0) {
            for (int i = 0; i < empSchList.size(); i++) {
                sch = empSchList.get(i);
                String cntHas = CommonUtils.getString(sch.get("cntHas"));
                if (StringUtils.isNotBlank(cntHas) && Double.parseDouble(cntHas) <= 0) {
                    if (i == empSchList.size() - 1) {
                        throw new BusException("没有可加号数，加号失败！");
                    }
                } else {
                    break;
                }
            }
        }
        //2.加号处理
        //2.1写表sch_appt
        SchAppt schAppt = new SchAppt();
        schAppt.setPkSchappt(NHISUUID.getKeyId());
        schAppt.setPkOrg(u.getPkOrg());
        schAppt.setEuSchclass("0");
        schAppt.setPkSch(CommonUtils.getString(sch.get("pkSch")));
        schAppt.setDateAppt(dateNow);
        schAppt.setPkDateslot(CommonUtils.getString(sch.get("pkDateslot")));
        schAppt.setPkSchres(CommonUtils.getString(sch.get("pkSchresEmp")));
        schAppt.setPkSchsrv(CommonUtils.getString(sch.get("pkSchsrvEmp")));
        schAppt.setPkPi(pkPi);
        schAppt.setPkOrgEx(u.getPkOrg());
        schAppt.setPkDeptEx(pkDept);
        schAppt.setDateReg(d);
        schAppt.setPkDeptReg(pkDept);
        schAppt.setPkEmpReg(pkEmp);
        schAppt.setNameEmpReg(u.getNameEmp());
        schAppt.setEuStatus("0");
        schAppt.setFlagPay("0");
        schAppt.setFlagNotice("0");
        schAppt.setFlagCancel("0");
        schAppt.setFlagNoticeCanc("0");
        schAppt.setTicketNo("0");
        ApplicationUtils.setDefaultValue(schAppt, true);
        DataBaseHelper.insertBean(schAppt);

        //2.2写表sch_appt_pv
        SchApptPv apptPv = new SchApptPv();
        apptPv.setPkOrg(u.getPkOrg());
        apptPv.setPkSchappt(schAppt.getPkSchappt());
        apptPv.setEuApptmode("1");
        apptPv.setPkEmpPhy(u.getPkEmp());
        apptPv.setNameEmpPhy(u.getNameEmp());
        apptPv.setFlagPv("0");
        ApplicationUtils.setDefaultValue(apptPv, true);
        DataBaseHelper.insertBean(apptPv);
        //2.3更新表sch_sch；
        DataBaseHelper.execute(" update sch_sch  set cnt_over=cnt_over+1 where pk_sch=? ", schAppt.getPkSch());
    }

    //  查询患者信息列表  old: 004003011049
    public List<Map<String, Object>> qryPatients(String param, IUser user) {
        //pkSchres/pkDeptEx/codePi/namePi/dtSex/idNo/mobile/cardNo
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> apptPatisList = patiPvDao.qryPatients(paramMap);
        return apptPatisList;
    }

    //  初始化入院通知单 old:  004003003012
    public OpPatiInfo initForIp(String param, IUser user) {
        //pkPv
        Map<String, Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {
        });
        String pkPv = (String) paramMap.get("pkPv");
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("传参pkPv为空！");
        }
        OpPatiInfo opPatiInfo = patiPvDao.qryPiOpInfo(paramMap);
        int count = DataBaseHelper.queryForScalar("select count(1) from pv_ip_notice where pk_pv_op=? and eu_status='2' ",
                Integer.class, pkPv);
        opPatiInfo.setIsExists(Integer.valueOf(count));
        return opPatiInfo;
    }

    //   住院预约 old: 004003003013
    public PvIpNotice toIp(String param, IUser user) {
        //pkPvOp/pkDeptIp/dateValid
        PvIpNotice notice = JsonUtil.readValue(param, new TypeReference<PvIpNotice>() {
        });
        String pkInNotice = notice.getPkInNotice();
        if (StringUtils.isBlank(pkInNotice)) {
            //2.写表pv_ip_notice；
            notice.setPkInNotice(NHISUUID.getKeyId());
            notice.setEuStatus("0");
            ApplicationUtils.setDefaultValue(notice, true);
            DataBaseHelper.insertBean(notice);
        } else {
            if(notice.getEuStatus()!=null && notice.getEuStatus()!="9"){
                //3.更新表pv_ip_notice；
                DataBaseHelper.updateBeanByPk(notice, false);
                String Sql=" update PV_IP_NOTICE set PK_DIAG_MAJ=:pkDiagMaj,DATE_COVID=:dateCovid,eu_result_covid=:euResultCovid,pk_hp=:pkHp where PK_IN_NOTICE=:pkInNotice ";
                DataBaseHelper.update(Sql,notice);
            }else{
                //取消入院
                String Sql=" update PV_IP_NOTICE set EU_STATUS='9' where PK_IN_NOTICE=:pkInNotice ";
                DataBaseHelper.update(Sql,notice);
            }
        }
        ExtSystemProcessUtils.processExtMethod("OldHis","ipNotice",notice);
        //门诊转住院消息ADT^A06
        Map<String, Object> pvInfoMap = JsonUtil.readValue(param, Map.class);
        PlatFormSendUtils.sendOpToIpMsg(pvInfoMap);
        return notice;
    }

    /**
     * 取消入院申请
     * 004003003022
     * @param param
     * @param user
     */
    public void cancleToIp(String param, IUser user) {
        //pkPvOp/pkDeptIp/dateValid
        PvIpNotice notice = JsonUtil.readValue(param, new TypeReference<PvIpNotice>() {
        });
        if(StringUtils.isEmpty(notice.getPkInNotice())){
            return;
        }
        String Sql=" delete from  PV_IP_NOTICE where PK_IN_NOTICE='"+notice.getPkInNotice()+"' ";
        DataBaseHelper.execute(Sql);
        //门诊转住院消息ADT^A06
        Map<String, Object> pvInfoMap = JsonUtil.readValue(param, Map.class);
        pvInfoMap.put("state","9");//表示取消
        PlatFormSendUtils.sendOpToIpMsg(pvInfoMap);
    }


    public List<Map<String, Object>> getOpLinesBusiness(String param, IUser user) {

        //pkDept
        Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
        return getLinesBusiness(mapParam);

    }

    public List<Map<String, Object>> getLinesBusiness(Map<String, Object> mapParam) {

        if (mapParam.get("pkDept") == null) {
            throw new BusException("科室参数不正确！");
        }
        String sql = "select case bu.dt_butype when '06' then '1'  when '07' then '2' else '' end as  key,busa.pk_dept,busa.pk_org from bd_dept_bus bus inner join bd_dept_bu bu on bus.pk_deptbu=bu.pk_deptbu  inner join bd_dept_bus busa on bus.pk_deptbu=busa.pk_deptbu  where busa.dt_depttype='0402' and bu.dt_butype in ('06','07')  and  bus.pk_dept=?  and busa.del_flag = '0' and bu.del_flag = '0' and bus.del_flag = '0'";
        List<Map<String, Object>> result = DataBaseHelper.queryForList(sql, new Object[]{mapParam.get("pkDept")});
        if (result == null || result.size() <= 0) {
            throw new BusException("此科室未在业务线维护其对应的科室");
        }
        return result;
    }

    public Map<String, Object> addSchPatient(String param, IUser user) {
        PvEncounter pv = getSchAndCreateReg(param, user);
        //pkPv
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkPv", pv.getPkPv());
        Map<String, Object> pvMode = getHpAttr(paramMap);
        return pvMode;
    }

    public PvEncounter getSchAndCreateReg(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {
        });

        if (MapUtils.isEmpty(paramMap)) {
            throw new BusException("未传入参数");
        }
        String pkSchres = MapUtils.getString(paramMap, "pkSchres");
        String pkSchsrv = MapUtils.getString(paramMap, "pkSchsrv");

        String pkPi = MapUtils.getString(paramMap, "pkPi");
        String pkInsu = MapUtils.getString(paramMap, "pkHp");
        String pkOrgarea = MapUtils.getString(paramMap, "pkOrgarea");
        String pkSch = MapUtils.getString(paramMap, "pkSch");
        PiMaster piVo = null;
        if (StringUtils.isEmpty(pkPi)) {
            Map<String, Object> piMap = MapUtils.getMap(paramMap, "piMaster");
            piVo = new PiMaster();
            try {
                String birthStr = piMap.get("birthDate").toString();
                piMap.remove("birthDate");
                BeanUtils.copyProperties(piVo, piMap);
                piVo.setBirthDate(DateUtils.strToDate(birthStr));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        checkParamOfSch(pkSchres, pkSchsrv, pkPi);

        String sql = "select count(1) from pv_encounter where pk_dept = ? and pk_pi=? and eu_status != '9'  and date_begin>=to_date(?,'yyyyMMdd')";
        int count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{UserContext.getUser().getPkDept(), pkPi, DateUtils.getDateStr(new Date())});
        if (count > 0) {
            throw new BusException("已对当前患者临时挂过号！");
        }

        //获取医保信息
        if (StringUtils.isBlank(pkInsu)) {
            Map<String, Object> mapHp = DataBaseHelper.queryForMap("select pk_hp,name from bd_hp where eu_hptype=0 and del_flag=0");
            if (MapUtils.isEmpty(mapHp)) {
                throw new BusException("没有全自费医保类型");
            }
            pkInsu = MapUtils.getString(mapHp, "pkHp");
        }
        //获取排班服务
        SchSrv schSrv = DataBaseHelper.queryForBean("select * from sch_srv where pk_schsrv=?", SchSrv.class, new Object[]{pkSchsrv});
        if (schSrv == null) {
            throw new BusException("依据排班服务主键未获取到对应服务信息");
        }

        //根据选择的资源和服务类别，生成排班数据，写表sch_sch
        SchSch schSch = null;
        if (StringUtils.isBlank(pkSch)) {
            schSch = saveSch(pkSchres, pkSchsrv, pkOrgarea);
        } else {
            schSch = DataBaseHelper.queryForBean("select * from sch_sch where pk_sch=?", SchSch.class, new Object[]{pkSch});
        }
        //新病人注册写表Pi_Master
        if (piVo != null) {
            PiMaster piMaster = savePiMaster(piVo, pkInsu);
            pkPi = piMaster.getPkPi();
        }
        //根据患者基本信息，生成就诊记录，写表pv_encounter
        PvEncounter pvEncount = savePvEncount(pkPi, pkInsu, schSrv.getEuSrvtype());
        
        if(null != schSch) {
        	pvInfoPubService.addPkDeptArea(pkSchres,pvEncount);
        }

        if ("9".equals(schSrv.getEuSrvtype())) { //急诊
            //生成门诊就诊，写表pv_op ,急诊无临时挂号
            savePvEr(pvEncount, schSch);//门诊就诊属性
        } else {
            //生成门诊就诊，写表pv_op ,急诊无临时挂号
            savePvOp(pvEncount, schSch);//门诊就诊属性
        }

        //保存就诊保险信息
        savePvInsurance(pvEncount);

        //判断当前患者是否为在职的本院职工（离职日期小于当前日期表示已离职）；(---上面的逻辑在产品文档中已经废弃2.3.1.5.9）
        boolean isCountStaff = StringUtils.isNotBlank(pkPi) &&
                DataBaseHelper.queryForScalar("select count(1) from bd_ou_empjob job  INNER JOIN PI_MASTER pi on pi.PK_EMP=job.pk_emp  where pi.PK_PI =? and (job.date_left is null or job.date_left>to_date(?,'YYYYMMDDHH24MISS'))"
                        ,Integer.class,new Object[]{pkPi, DateUtils.getDate("yyyyMMddHHmmss")})>0;

        //对应服务类型，生成诊查费记费记录，写表bl_op_dt
        saveConstructBlOpDt(schSch, pvEncount,isCountStaff);

        return pvEncount;
    }

    private PvEr savePvEr(PvEncounter pvEncount, SchSch schSch) {
        PvEr er = new PvEr();
        er.setPkPv(pvEncount.getPkPv());
        er.setPkSchsrv(schSch.getPkSchsrv());
        er.setPkRes(schSch.getPkSchres());
        er.setPkDateslot(schSch.getPkDateslot());
        er.setPkDeptPv(pvEncount.getPkDept());
        er.setPkEmpPv(pvEncount.getPkEmpPhy());
        er.setNameEmpPv(pvEncount.getNameEmpPhy());
        er.setPkResCn(schSch.getPkSchres());
        er.setPkSchsrvCn(schSch.getPkSchsrv());
        er.setTicketno(CommonUtils.isEmptyString(schSch.getTicketNo()) ? 0 : Long.parseLong(schSch.getTicketNo()));
        er.setPkSch(schSch.getPkSch());
        er.setDateBegin(pvEncount.getDateBegin());
        er.setDateEnd(ApplicationUtils.getPvDateEndEr(pvEncount.getDateBegin()));
        DataBaseHelper.insertBean(er);
        return er;
    }

    private SchSch saveSch(String pkSchres, String pkSchsrv, String pkOrgarea) {
        SchResource schResource = DataBaseHelper.queryForBean("select * from sch_resource where pk_schres=?", SchResource.class, new Object[]{pkSchres});
        if (schResource == null) {
            throw new BusException("依据排班资源主键未获取到对应资源信息");
        }
        Map<String, Object> mapDateslot = DataBaseHelper.queryForMap("select ds.pk_dateslot from sch_resource res inner join bd_code_dateslot ds on res.dt_dateslottype=ds.dt_dateslottype"
                        + " where res.pk_schres=? and to_date(?,'HH24:MI:SS') between to_date(ds.TIME_BEGIN,'HH24:MI:SS') and to_date(ds.TIME_END,'HH24:MI:SS')"
                , new Object[]{pkSchres, DateUtils.getDate("HH:mm:ss")});
        if (MapUtils.isEmpty(mapDateslot)) {
            throw new BusException("当前时间非工作时间，不能挂号！");
        }

        User u = UserContext.getUser();
        Date d = new Date();
        //如果已有排班，不在生成
        String sql = "select sch.* from  sch_sch sch inner join bd_code_dateslot lot on lot.pk_dateslot=sch.pk_dateslot  where sch.pk_dept=?  and sch.pk_schres=? and sch.pk_schsrv=? "
                + "and sch.date_work=to_date(?,'yyyyMMdd') and sch.eu_status = '8' and sch.cnt_total = 0 and to_date(?,'HH24:MI:SS') between to_date(lot.TIME_BEGIN,'HH24:MI:SS') and to_date(lot.TIME_END,'HH24:MI:SS') ";
        List<SchSch> schList = DataBaseHelper.queryForList(sql, SchSch.class, new Object[]{schResource.getPkDeptBelong(), pkSchres, pkSchsrv, DateUtils.getDateStr(d), DateUtils.getDate("HH:mm:ss")});
        if (schList != null && schList.size() > 0) {
            return schList.get(0);
        }

        String pkDateslot = MapUtils.getString(mapDateslot, "pkDateslot");
        SchSch schSch = new SchSch();
        schSch.setPkSch(NHISUUID.getKeyId());
        schSch.setPkDateslot(pkDateslot);
        schSch.setPkSchres(pkSchres);
        schSch.setPkSchsrv(pkSchsrv);
        schSch.setMinutePer(schResource.getMinutePer().intValue());
        schSch.setPkOrg(u.getPkOrg());
        schSch.setPkDept(schResource.getPkDeptBelong());
        schSch.setEuSchclass(schResource.getEuSchclass());
        schSch.setDateWork(DateUtils.getDateMorning(d, 0));
        schSch.setPkPlanweek(null);
        schSch.setPkSchplan(null);
        schSch.setCntTotal(0);
        schSch.setCntAdd(0);
        schSch.setCntAppt(0);
        schSch.setCntOver(0);
        schSch.setCntUsed(0);
        schSch.setFlagStop("0");
        schSch.setFlagModi("1");
        schSch.setEuStatus(SchEuStatus.AUDIT.getStatus());
        schSch.setEuAppttype("0");
        schSch.setPkOrgarea(pkOrgarea);
        schSch.setPkEmpSch(u.getPkEmp());
        schSch.setNameEmpSch(u.getNameEmp());
        schSch.setDateSch(d);
        schSch.setPkEmpChk(u.getPkEmp());
        schSch.setNameEmpChk(u.getNameEmp());
        schSch.setDateChk(d);
        schSch.setNote("临时挂号");//(u.getUserName() + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "生成排班！");
        DataBaseHelper.insertBean(schSch);
        return schSch;
    }

    private PvOp savePvOp(PvEncounter pv, SchSch schSch) {
        // 保存门诊属性
        PvOp pvOp = new PvOp();
        pvOp.setPkPv(pv.getPkPv());
        Integer opTimes = regPubMapper.getMaxOpTimes(pv.getPkPi());
        pvOp.setOpTimes(new Long(opTimes + 1));
        pvOp.setPkSchsrv(schSch.getPkSchsrv());
        pvOp.setPkRes(schSch.getPkSchres());
        pvOp.setPkDateslot(schSch.getPkDateslot());
        pvOp.setPkDeptPv(pv.getPkDept());
        pvOp.setPkEmpPv(pv.getPkEmpPhy());
        pvOp.setNameEmpPv(pv.getNameEmpPhy());
        pvOp.setPkResCn(schSch.getPkSchres());
        pvOp.setPkSchsrvCn(schSch.getPkSchsrv());
        pvOp.setTicketno(CommonUtils.isEmptyString(schSch.getTicketNo()) ? 0 : Long.parseLong(schSch.getTicketNo()));
        pvOp.setPkSch(schSch.getPkSch());
        pvOp.setFlagFirst("1"); // 初诊
        pvOp.setEuRegtype("0"); //现场挂号

        // 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
        // ( 参数-1) || '23:59:59'
        pvOp.setDateBegin(pv.getDateBegin());
        pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pv.getDateBegin()));

        DataBaseHelper.insertBean(pvOp);
        return pvOp;
    }

    private PvEncounter savePvEncount(String pkPi, String pkInsu, String euSrvtype) {
        PiMaster master = DataBaseHelper.queryForBean("select * from pi_master where pk_pi=?", PiMaster.class, new Object[]{pkPi});
        if (master == null) {
            throw new BusException("依据患者主键未获取到对应患者信息");
        }
        User u = UserContext.getUser();
        Date d = new Date();
        // 保存就诊记录
        PvEncounter pvEncounter = new PvEncounter();
        pvEncounter.setPkPv(NHISUUID.getKeyId());
        pvEncounter.setPkPi(master.getPkPi());
        pvEncounter.setPkInsu(pkInsu);
        pvEncounter.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
        if ("9".equals(euSrvtype)) { //急诊
            pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_2); //设置就诊属性
        } else {
            pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_1); //设置就诊属性
            pvEncounter.setFlagSpec("0");
            if ("2".equals(euSrvtype)) //特诊
            {
                pvEncounter.setFlagSpec("1");
            }
        }
        pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_1); // 就诊
        pvEncounter.setEuLocked("1");
        pvEncounter.setDateClinic(d);
        pvEncounter.setPkDept(u.getPkDept());//就诊科室
        pvEncounter.setPkEmpPhy(u.getPkEmp());
        pvEncounter.setNameEmpPhy(u.getNameEmp());
        pvEncounter.setDateBegin(d);
        pvEncounter.setDateReg(d);
        pvEncounter.setPkEmpReg(u.getPkEmp());
        pvEncounter.setNameEmpReg(u.getNameEmp());
        pvEncounter.setNamePi(master.getNamePi());
        pvEncounter.setDtSex(master.getDtSex());
        pvEncounter.setAgePv(DateUtils.getAgeByBirthday(master.getBirthDate(),pvEncounter.getDateBegin()));
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
     * 保存就诊保险信息
     * @param pv
     */
    private PvInsurance savePvInsurance(PvEncounter pv) {

        if (CommonUtils.isNotNull(pv.getPkInsu())) {
            PvInsurance insu = new PvInsurance();
            insu.setPkPvhp(NHISUUID.getKeyId());
            insu.setPkOrg(UserContext.getUser().getPkOrg());
            insu.setCreator(UserContext.getUser().getPkEmp());
            insu.setCreateTime(new Date());
            insu.setModifier(UserContext.getUser().getPkEmp());
            insu.setTs(new Date());
            insu.setPkPv(pv.getPkPv());
            insu.setPkHp(pv.getPkInsu());
            DataBaseHelper.insertBean(insu);
            return insu;
        }
        return null;
    }

    private void saveConstructBlOpDt(SchSch schSch, PvEncounter pv,boolean isCountStaff) {
        //根据挂号服务，获取挂号服务收费项目
        String euType=isCountStaff?"1":"0";
        List<Map<String, Object>> srvordlist = DataBaseHelper.queryForList("select srv.*,ord.pk_ord   from  sch_srv srv inner join sch_srv_ord ord on ord.pk_schsrv = srv.pk_schsrv  and ord.del_flag='0' where srv.del_flag='0' and srv.flag_stop='0' and srv.pk_schsrv =? and (case when ord.EU_TYPE is null then '0' else ord.EU_TYPE end ) = ? ", new Object[]{schSch.getPkSchsrv(),euType});
        if (srvordlist == null || srvordlist.size() <= 0) {
            return;
        }
        List<BlPubParamVo> blOpItem = new ArrayList<BlPubParamVo>();
        for (Map<String, Object> ordmap : srvordlist) {
            User u = UserContext.getUser();
            String pkOrg = u.getPkOrg();
            BlPubParamVo bpb = new BlPubParamVo();
            bpb.setFlagPv("1");
            bpb.setPkOrg(pkOrg);
            bpb.setEuPvType(pv.getEuPvtype());
            bpb.setPkPv(pv.getPkPv());
            bpb.setPkPi(pv.getPkPi());
            bpb.setPkDeptAreaapp(pv.getPkDeptArea());
            bpb.setPkOrd(CommonUtils.getString(ordmap.get("pkOrd")));
            bpb.setQuanCg(1.0);
            bpb.setFlagPd("0");
            bpb.setEuAdditem("0"); //非附加费
            blOpItem.add(bpb);
        }
        if (blOpItem.size() > 0) {
            opCgPubService.blOpCgBatch(blOpItem);
        }
    }

    private void checkParamOfSch(String pkSchres, String pkSchsrv, String pkPi) {
        if (StringUtils.isBlank(pkSchres)) {
            throw new BusException("未传入排班资源主键");
        }
        if (StringUtils.isBlank(pkSchsrv)) {
            throw new BusException("未传入排班服务主键");
        }
		/*if(StringUtils.isBlank(pkPi)) {
			throw new BusException("未传入患者主键");
		}*/
    }

    public List<Map<String, Object>> qryUnSettleCnOrd(String param, IUser user) {
        //pkEmp/
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("查询指引单传参pkPv为空！");
        }
        Date d = new Date();
        String dateStr = DateUtils.getDateStr(d);
        paramMap.put("dateNow", dateStr + "000000");
        List<Map<String, Object>> list = patiPvDao.qryUnSettleCnOrd(paramMap);
        return list;
    }

    /**
     * 查询患者未结算的医嘱附加费用，交易号：004003011046
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryPatiOrdAddInfo(String param, IUser user) {
        List<SyxCnOrderVo> paramList = JsonUtil.readValue(param, new TypeReference<List<SyxCnOrderVo>>() {
        });
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Date d = new Date();
        String dateStr = DateUtils.getDateStr(d);
        paramMap.put("dateNow", dateStr + "000000");
        paramMap.put("list", paramList);
        List<Map<String, Object>> list = patiPvDao.queryPatiOrdAddInfo(paramMap);
        return list;
    }

    /**
     * 查询患者科室费用信息 交易号：004003011047
     *
     * @param param
     * @param user
     * @return 未结费用/总费用
     */
    public Map<String, Object> queryPatiDeptChargeInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("传参pkPv为空！");
        }
        Date d = new Date();
        String dateStr = DateUtils.getDateStr(d);
        paramMap.put("dateNow", dateStr + "000000");
        Map<String, Object> map = patiPvDao.queryPatiDeptChargeInfo(paramMap);
        return map;
    }


    /**
     * 诊间挂号
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> addSchRoomPatient(String param, IUser user) {

        Map<String, Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {
        });
        if (MapUtils.isEmpty(paramMap)) {
            throw new BusException("未传入参数");
        }
        String pkSchres = MapUtils.getString(paramMap, "pkSchres");
        String pkSchsrv = MapUtils.getString(paramMap, "pkSchsrv");
        String pkPi = MapUtils.getString(paramMap, "pkPi");
        String pkInsu = MapUtils.getString(paramMap, "pkHp");
        String pkSch = MapUtils.getString(paramMap, "pkSch");
        String valAttr = null;//离休费用特殊处理
        PiMaster piVo = null;
        if (StringUtils.isEmpty(pkPi)) {
            Map<String, Object> piMap = MapUtils.getMap(paramMap, "piMaster");
            piVo = new PiMaster();
            try {
                String birthStr = piMap.get("birthDate").toString();
                piMap.remove("birthDate");
                BeanUtils.copyProperties(piVo, piMap);
                piVo.setBirthDate(DateUtils.strToDate(birthStr));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        //参数校验
        checkParamOfSchRoom(pkSchres, pkSchsrv, pkPi, pkSch);
        User u = (User) user;

        //验证是否存在当日科室挂号信息
        if (DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.PK_DEPT=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS < 9 ",
                Integer.class, new Object[]{pkPi, DateUtils.formatDate(new Date(), "yyyyMMdd"), u.getPkDept()}) > 0) {
            throw new BusException("该患者已经存在当前日期和科室的挂号记录！");
        }

        //获取医保信息
        if (StringUtils.isBlank(pkInsu)) {
            Map<String, Object> mapPkHp = DataBaseHelper.queryForMap("select pk_hp from pi_insurance where pk_pi=?", new Object[]{pkPi});
            if (MapUtils.isEmpty(mapPkHp)) {
                Map<String, Object> mapHp = DataBaseHelper.queryForMap("select pk_hp,name from bd_hp where eu_hptype=0 and del_flag=0");
                if (MapUtils.isEmpty(mapHp)) {
                    throw new BusException("没有全自费医保类型");
                }
                pkInsu = MapUtils.getString(mapHp, "pkHp");
            } else {
                pkInsu = MapUtils.getString(mapPkHp, "pkHp");
            }
        }
        //离休特殊处理
        Map<String, Object> mapValAttr = DataBaseHelper.queryForMap("select attr.val_attr from bd_dictattr attr where attr.pk_dict=? and attr.code_attr='0326'", new Object[]{pkInsu});
        if (!MapUtils.isEmpty(mapValAttr)) {
            valAttr = MapUtils.getString(mapValAttr, "valAttr");
        }

        //获取排班信息
        SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
                SchSch.class, pkSch);

        //判断是否还有可挂号
        if (schSch == null || "1".equals(schSch.getFlagStop())) {
            throw new BusException("您所选排班不存在或已停用！");
        }
        if (schSch.getCntTotal().intValue() <= schSch.getCntUsed().intValue()) {
            throw new BusException("您所选排班已约满！");
        }
        SchTicket ticket = null;

        //获取排班服务
        SchSrv schSrv = DataBaseHelper.queryForBean("select * from sch_srv where pk_schsrv=?", SchSrv.class, new Object[]{pkSchsrv});
        if (schSrv == null) {
            throw new BusException("依据排班服务主键未获取到对应服务信息");
        }
        //占用号源
        boolean haveTicket = DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where pk_sch = ?",
                Integer.class, new Object[]{pkSch})>0;
        if (haveTicket) {
            // 处理号表
            ticket = getUnusedTicket(pkSch);
        } else {//无号表方式 ，锁定排班表
            ticket = getUnusedTicketFromSch(pkSch);
        }

        PvEncounter pvEncount = null;
        Map<String, Object> pvMode = null;
        //保存挂号信息（含保存患者信息）
        try {
            //新病人注册写表Pi_Master
            if (piVo != null) {
                PiMaster piMaster = savePiMaster(piVo, pkInsu);
                pkPi = piMaster.getPkPi();
            }
            if (ticket != null) {
                schSch.setTicketNo(ticket.getTicketno());
            }
            pvEncount = savePvRegInfo(pkPi, pkInsu, schSrv, schSch, valAttr);
            //有号表方式，更新排班已使用号数
            if (haveTicket) {
                DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[]{schSch.getPkSch()});
            }
            Map<String, Object> pvparam = new HashMap<String, Object>();
            pvparam.put("pkPv", pvEncount.getPkPv());
            pvMode = getHpAttr(pvparam);
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            //还原占用的挂号资源
            if ("1".equals(haveTicket)) {
                setTicketUnused(ticket);
            } else {
                setTicketUnusedFromSch(ticket);
            }
            throw new BusException(e.getMessage());
        }
        return pvMode;
    }

    //参数校验
    private void checkParamOfSchRoom(String pkSchres, String pkSchsrv, String pkPi, String pkSch) {
        if (StringUtils.isBlank(pkSchres)) {
            throw new BusException("未传入排班资源主键");
        }
        if (StringUtils.isBlank(pkSchsrv)) {
            throw new BusException("未传入排班服务主键");
        }
		/*if(StringUtils.isBlank(pkPi)) {
			throw new BusException("未传入患者主键");
		}*/
        if (StringUtils.isBlank(pkSch)) {
            throw new BusException("未传入排班资源！");
        }
    }

    /**
     * 获取可用挂号
     *
     * @param pkSch
     * @return SchTicket
     */
    public SchTicket getUnusedTicket(String pkSch) {
        // 处理号表and eu_pvtype = ?,euPvtype
        List<SchTicket> tickets = DataBaseHelper.queryForList(
                "select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0'  and flag_stop='0' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
                SchTicket.class, pkSch);
        if (tickets == null || tickets.size() <= 0) {
            throw new BusException("您所选的排班资源已挂满！");
        }
        SchTicket ticket = tickets.get(0);
        //占用号表数据
        int cnt = DataBaseHelper.update("update sch_ticket set FLAG_USED ='1' where pk_schticket =? and FLAG_USED='0'", new Object[]{ticket.getPkSchticket()});
        if (cnt <= 0) {
            throw new BusException("您所选的挂号号码已被占用，请重试！");
        }
        return ticket;
    }

    /**
     * 无号表方式下获取可用票号
     *
     * @param pkSch
     * @return
     */
    public SchTicket getUnusedTicketFromSch(String pkSch) {
        //先更新号表，后查询更新的票号返回
        int cnt = DataBaseHelper.update("update sch_sch  set TICKET_NO = nvl(TICKET_NO,0)+1,cnt_used=cnt_used+1 where PK_SCH = ? and cnt_used<cnt_total", new Object[]{pkSch});
        if (cnt <= 0) {
            throw new BusException("您所选的排班资源已挂满！");
        }
        SchTicket ticket = new SchTicket();
        SchSch sch = DataBaseHelper.queryForBean("select ticket_no,pk_sch from sch_sch where pk_sch = ?", SchSch.class, new Object[]{pkSch});
        ticket.setTicketno(sch.getTicketNo());
        ticket.setPkSch(pkSch);
        return ticket;
    }

    /**
     * 构造排班计划<br>
     * 中二 日排班 是没有计划的，将要使用的值传入即可
     *
     * @param pkPlan
     * @return
     */
    private SchPlan getSchPlan(String pkPlan, String pkSch) {
        SchPlan schplan = null;
        if (StringUtils.isNotBlank(pkPlan)) {
            schplan = DataBaseHelper.queryForBean("select * from sch_plan where del_flag = '0' and pk_schplan = ?",
                    SchPlan.class, pkPlan);
            if (schplan == null || "1".equals(schplan.getFlagStop())) {
                throw new BusException("您所选排班，对应的排班计划不存在或已停用！");
            }
        } else {
            //还有那种一键生成的，没有号表的；日排班的是有号表的
            schplan = new SchPlan();
            schplan.setFlagTicket("0");
            if (StringUtils.isNotBlank(pkSch)
                    && DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where PK_SCH = ?", Integer.class, new Object[]{pkSch}) > 0) {
                schplan.setFlagTicket("1");
            }
            schplan.setFlagStop("0");
        }

        return schplan;
    }

    /**
     * 设置预约号可用
     *
     * @param ticket
     */
    public void setTicketUnused(SchTicket ticket) {
        if (ticket == null) {
            return;
        }
        DataBaseHelper.update("update sch_ticket set FLAG_USED ='0' where pk_schticket = ?", new Object[]{ticket.getPkSchticket()});
    }

    /**
     * 无票号方式设置可用号数未使用
     *
     * @param ticket
     */
    public void setTicketUnusedFromSch(SchTicket ticket) {
        if (ticket == null || CommonUtils.isNull(ticket.getPkSch())) {
            return;
        }
        DataBaseHelper.update("update sch_sch set cnt_used=cnt_used-1 where pk_sch = ?", new Object[]{ticket.getPkSch()});
    }

    //保存患者挂号信息
    public PvEncounter savePvRegInfo(String pkPi, String pkInsu, SchSrv schSrv, SchSch schSch, String valAttr) {
        //根据患者基本信息，生成就诊记录，写表pv_encounter
        PvEncounter pvEncount = savePvEncount(pkPi, pkInsu, schSrv.getEuSrvtype());
        
        if(null != schSch) {
        	pvInfoPubService.addPkDeptArea(schSch.getPkSchres(),pvEncount);
        }
        
        if ("9".equals(schSrv.getEuSrvtype())) { //急诊
            //生成门诊就诊，写表pv_op ,急诊无临时挂号
            savePvEr(pvEncount, schSch);//门诊就诊属性
        } else {
            //生成门诊就诊，写表pv_op ,急诊无临时挂号
            savePvOp(pvEncount, schSch);//门诊就诊属性
        }
        //判断当前患者是否为在职的本院职工（离职日期小于当前日期表示已离职）；(---上面的逻辑在产品文档中已经废弃2.3.1.5.9）
        boolean isCountStaff = StringUtils.isNotBlank(pkPi) &&
                DataBaseHelper.queryForScalar("select count(1) from bd_ou_empjob job  INNER JOIN PI_MASTER pi on pi.PK_EMP=job.pk_emp  where pi.PK_PI =? and (job.date_left is null or job.date_left>to_date(?,'YYYYMMDDHH24MISS'))"
                        ,Integer.class,new Object[]{pkPi, DateUtils.getDate("yyyyMMddHHmmss")})>0;

        //保存就诊保险信息
        savePvInsurance(pvEncount);
        //对应服务类型，生成诊查费记费记录，写表bl_op_dt
        if (StringUtils.isNotBlank(valAttr) && "1".equals(valAttr)) {
            //离休特殊处理
            saveConstructBlOpDtAttr(schSch, pvEncount,isCountStaff);
        } else {
            saveConstructBlOpDt(schSch, pvEncount,isCountStaff);
        }
        return pvEncount;
    }

    /**
     * 查询排班信息（包含科室排班信息）
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qrySch(String param, IUser user) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Date d = new Date();
        User u = (User) user;
        String dateStr = DateUtils.getDateStr(d);
        paramMap.put("dateWork", dateStr);
        paramMap.put("timeNow", DateUtils.getDate("HH:mm:ss"));
        paramMap.put("pkDept", u.getPkDept());
        paramMap.put("pkEmp", u.getPkEmp());
        return patiPvDao.qrySchDateslot(paramMap);
    }

    //离休计费
    private void saveConstructBlOpDtAttr(SchSch schSch, PvEncounter pv,boolean isCountStaff) {
        //根据挂号服务，获取挂号服务收费项目
        String bl0050 = ApplicationUtils.getSysparam("BL0050", false);
        List<Map<String, Object>> pkItem = DataBaseHelper.queryForList("select pk_item from bd_item item where item.code=?", new Object[]{bl0050});
        if (pkItem == null || pkItem.size() <= 0) {
            String euType=isCountStaff?"1":"0";
            List<Map<String, Object>> srvordlist = DataBaseHelper.queryForList("select srv.*,ord.pk_ord   from  sch_srv srv inner join sch_srv_ord ord on ord.pk_schsrv = srv.pk_schsrv  and ord.del_flag='0' where srv.del_flag='0' and srv.flag_stop='0' and srv.pk_schsrv =? and (case when ord.EU_TYPE is null then '0' else ord.EU_TYPE end ) = ? ", new Object[]{schSch.getPkSchsrv(),euType});
            if (srvordlist == null || srvordlist.size() <= 0) {
                return;
            }
            List<BlPubParamVo> blOpItem = new ArrayList<BlPubParamVo>();
            for (Map<String, Object> ordmap : srvordlist) {
                User u = UserContext.getUser();
                String pkOrg = u.getPkOrg();
                BlPubParamVo bpb = new BlPubParamVo();
                bpb.setFlagPv("1");
                bpb.setPkOrg(pkOrg);
                bpb.setEuPvType(pv.getEuPvtype());
                bpb.setPkPv(pv.getPkPv());
                bpb.setPkPi(pv.getPkPi());
                bpb.setPkDeptAreaapp(pv.getPkDeptArea());
                bpb.setPkOrd(CommonUtils.getString(ordmap.get("pkOrd")));
                bpb.setQuanCg(1.0);
                bpb.setFlagPd("0");
                bpb.setEuAdditem("0"); //非附加费
                blOpItem.add(bpb);
            }
            if (blOpItem.size() > 0) {
                opCgPubService.blOpCgBatch(blOpItem);
            }
        } else {
            List<BlPubParamVo> blOpItem = new ArrayList<BlPubParamVo>();
            for (Map<String, Object> ordmap : pkItem) {
                User u = UserContext.getUser();
                String pkOrg = u.getPkOrg();
                BlPubParamVo bpb = new BlPubParamVo();
                bpb.setFlagPv("1");
                bpb.setPkOrg(pkOrg);
                bpb.setEuPvType(pv.getEuPvtype());
                bpb.setPkPv(pv.getPkPv());
                bpb.setPkPi(pv.getPkPi());
                bpb.setPkDeptAreaapp(pv.getPkDeptArea());
                bpb.setPkItem(CommonUtils.getString(ordmap.get("pkItem")));
                bpb.setQuanCg(1.0);
                bpb.setFlagPd("0");
                bpb.setEuAdditem("0"); //非附加费
                blOpItem.add(bpb);
            }
            if (blOpItem.size() > 0) {
                opCgPubService.blOpCgBatch(blOpItem);
            }
        }
    }

    /**
     * 保存或更新患者信息piMaster
     *
     * @param piMaster
     * @return
     */
    public PiMaster savePiMaster(PiMaster piMaster, String pkInsu) {
        PiMasterParam piParam = new PiMasterParam();

        //新增患者，先保存患者信息
        verifyMaster(piMaster, true);

        if (CommonUtils.isNotNull(pkInsu)) {
            piParam.setInsuranceList(transformPiInsurance(piMaster.getPkPi(), pkInsu));
        }
        piParam.setMaster(piMaster);
        PiMaster piNew = piPubService.savePiMasterParam(piParam, new String[0]);

        return piNew;

    }


    /**
     * 中二新加的两字段的唯一性校验
     *@param piMaster
     * @param isAdd
     */
    private void verifyMaster(PiMaster piMaster, boolean isAdd) {
        Object[] objParam = isAdd ? new Object[]{null} : new Object[]{null, piMaster.getPkPi()};
        String pkPistr = isAdd ? "" : " and pk_pi != ? ";
        //校验身份证是否重复
        objParam[0] = piMaster.getIdNo();
        if (StringUtils.isNotBlank(piMaster.getIdNo())
                && "01".equals(piMaster.getDtIdtype())
                && (DataBaseHelper.queryForScalar("select count(1) from pi_master "
                + "where del_flag = '0' and dt_idtype = '01' and id_no = ?" + pkPistr, Integer.class, objParam)) != 0) {
            throw new BusException("身份证号重复，无法更新！");
        }
    }

    private List<PiInsurance> transformPiInsurance(String pkPi, String pkHp) {
        List<PiInsurance> insulist = new ArrayList<PiInsurance>();
        PiInsurance insu = DataBaseHelper.queryForBean("select pk_insurance from pi_insurance where del_flag=0 and pk_pi=? and pk_hp=?", PiInsurance.class, new Object[]{pkPi, pkHp});
        //目前调用的方法是全部删了做插入的，那么这里直接设置为默认，序号为0
        if (insu == null) {
            insu = new PiInsurance();
            insu.setFlagDef("1");
            insu.setSortNo(0L);
            insu.setPkPi(pkPi);
            insu.setPkHp(pkHp);
        }
        insulist.add(insu);
        return insulist;
    }

    //查询锁状态
    public String getPvEuLocked(String pkPv) {
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("传参pkPv为空！");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkPv", pkPv);
        List<SyxPiPv> pvEncounters = patiPvDao.qryPiPvInfo(paramMap);
        if (pvEncounters == null || pvEncounters.size() != 1) {
            throw new BusException("查询患者就诊记录有误，请联系管理员！");
        }
        String euLocked = pvEncounters.get(0).getEuLocked();
        return euLocked;
    }

    /**
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Description 查询患者流行病史
     * @auther wuqiang
     * @Date 2020-02-02
     * @Param [param, user]
     */
    public List<Map<String, Object>> queryPvEpidemicInf(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        List<Map<String, Object>> mapList = patiPvDao.queryPvEpidemicInf(pkPv);
        return mapList;

    }

    /***
     * @Description 保存患者流行病史信息
     * @auther wuqiang
     * @Date 2020-02-02
     * @Param [param, user]
     * @return void
     */
    public void savePvEpidemicInf(String param, IUser user) {
        EpidemicInfVo epi = JsonUtil.readValue(param, EpidemicInfVo.class);
        String sql = "update  PV_ENCOUNTER set DESC_SYMP=?,DESC_EPID=?,DESC_TREAT=?,PK_DEPT_DIST=? where PK_PV=?";
        DataBaseHelper.execute(sql, new Object[]{epi.getDescSymp(), epi.getDescEpid(), epi.getDescTreat(), epi.getPkDeptDist(), epi.getPkPv()});
        sql="update  PV_OP set FLAG_FIRST=? where PK_PV=?";
        DataBaseHelper.execute(sql,new Object[]{ epi.getFlagFirst(),epi.getPkPv()});
        sql="update  PV_DIAG set DATE_DIAG=? where FLAG_MAJ='1' and PK_PV=? ";
        DataBaseHelper.execute(sql,new Object[]{ epi.getDateOnset(),epi.getPkPv()});

    }

    /**
     * 转诊中
     * @param param
     * @param user
     */
    public void modToInTreatment(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = MapUtils.getString(paramMap,"pkPv");
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("传入患者不能为空！");
        }
        int count = DataBaseHelper.update("update  pv_encounter  set eu_status='1', eu_locked='0'  where pk_pv=?  and  eu_locked='1' ", new Object[]{pkPv});
        if(count ==0){
            throw new BusException("转入诊中状态失败，请确认患者是否正在收费！");
        }
        completePvQue(pkPv);

        //发送平台消息
        PlatFormSendUtils.sendFinishClinicMsg(paramMap);
    }



}
