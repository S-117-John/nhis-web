package com.zebone.nhis.pro.zsrm.cn.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.zebone.nhis.cn.opdw.dao.PvDocMapper;
import com.zebone.nhis.common.module.cn.opdw.CnOpEmrRecord;
import com.zebone.nhis.common.module.cn.opdw.PvDoc;
import com.zebone.nhis.common.module.pi.InsQgybPV;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvClinicGroup;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.emr.common.EmrSaveUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pi.pub.service.PiPubService;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.nhis.pro.zsrm.cn.dao.CnOpPiMapper;
import com.zebone.nhis.pro.zsrm.cn.vo.PiZsVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnOpPiService {

    @Autowired
    private PiPubService piPubService;

    @Autowired
    private CnOpPiMapper cnOpPiDao;

    @Resource
	private PvDocMapper recMapper;

    /**
     * 新患者--保存患者信息
     * @param param
     * @param user
     * @return
     */
    public PiMaster savePiMaster(String param, IUser user){
        PiZsVo regvo = JsonUtil.readValue(param, PiZsVo.class);
        PiMasterParam piParam = new PiMasterParam();
        if(regvo==null) {
            throw new BusException("当前患者信息为空！");
        }
        if(!StringUtils.isNotBlank(regvo.getIdNo())) {
            throw new BusException("当前患者id信息为空！");
        }
        PiMaster pi = new PiMaster();
        //中二的界面只有一个身份证号，默认证件类型就是身份证
        regvo.setDtIdtype((StringUtils.isNotBlank(regvo.getIdNo()) && StringUtils.isBlank(regvo.getDtIdtype()))?"01":regvo.getDtIdtype());
        ApplicationUtils.copyProperties(pi, regvo);
        if(CommonUtils.isNull(regvo.getPkPi())){//新增患者，先保存患者信息
            verifyMaster(regvo,true);
            if(CommonUtils.isNotNull(regvo.getPkHp())){
                piParam.setInsuranceList(transformPiInsurance(regvo));
            }
            piParam.setMaster(pi);
            PiMaster piNew = piPubService.savePiMasterParam(piParam, new String[0]);
            //由于像深大那种会使用触发器修改,统一从库中取一次即可
            piNew.setCodeOp(MapUtils.getString(DataBaseHelper.queryForMap("select code_op from PI_MASTER where PK_PI=?",new Object[]{piNew.getPkPi()}),"codeOp"));
            //保存后的患者信息赋值给界面传过来的患者信息
            ApplicationUtils.copyProperties(regvo, piNew);
        }else{//只更新界面录入的几个字段
            verifyMaster(regvo,false);
            //健康卡是否需要读取外部接口 (老系统导入的患者，没有健康码，更新的时候需要注册健康码)
            String extHealth = ApplicationUtils.getSysparam("PI0019", false);
            if ("1".equals(extHealth)) {
                //如果不存在健康码进行健康码注册
                if (CommonUtils.isEmptyString(pi.getHicNo())) {
                    Map<String, Object> ehealthMap = new HashMap<>(16);
                    ehealthMap.put("piMaster", pi);
                    //电子健康码注册
                    Map<String, String> hicNo = (Map<String, String>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHC01", new Object[]{ehealthMap});
                    if (hicNo != null) {
                        regvo.setHicNo(hicNo.get("hicNo"));
                        regvo.setNote(regvo.getNote()+hicNo.get("note"));
                    }
                }
            }
            cnOpPiDao.updatePiMaster(regvo);

            if(CommonUtils.isNotNull(regvo.getPkHp())){
                piPubService.savePiInsuranceList(transformPiInsurance(regvo), pi.getPkPi());
            }
        }
        return regvo;
    }

    /**
     * 唯一性校验
     * @param regvo
     * @param isAdd
     */
    private void verifyMaster(PiZsVo regvo,boolean isAdd){
        Object []objParam = isAdd?new Object[]{null}:new Object[]{null,regvo.getPkPi()};
        String pkPistr = isAdd?"":" and pk_pi != ? ";
        if(!isAdd){
            //新增的内部代码已经做了验证，这里只做修改时校验
            //1、校验编码是否重复
            objParam[0] = regvo.getCodePi();
            if (DataBaseHelper .queryForScalar( "select count(1) from pi_master "
                    + "where del_flag = '0' and code_pi = ?"+pkPistr, Integer.class, objParam) != 0) {
                throw new BusException("患者编码重复，无法更新！");
            }
            //3、校验住院号是否重复
            objParam[0] = regvo.getCodeIp();
            if(StringUtils.isNotBlank(regvo.getCodeIp())
                    && (DataBaseHelper.queryForScalar("select count(1) from pi_master "
                    + "where del_flag = '0' and code_ip = ?"+pkPistr,Integer.class, objParam))!=0){
                throw new BusException("住院号重复，无法更新！");
            }
            //4、校验身份证是否重复
            objParam[0] = regvo.getIdNo();
            if(StringUtils.isNotBlank(regvo.getIdNo())
                    && "01".equals(regvo.getDtIdtype())
                    && (DataBaseHelper.queryForScalar("select count(1) from pi_master "
                    + "where del_flag = '0' and dt_idtype = '01' and id_no = ?"+pkPistr,Integer.class, objParam))!= 0){
                throw new BusException("身份证号重复，无法更新！");
            }
        }
        objParam[0] = regvo.getSenNo();
        //5、校验老人证号是否重复
        if(StringUtils.isNotBlank(regvo.getSenNo())
                && (DataBaseHelper.queryForScalar("select count(1) from pi_master "
                + "where del_flag = '0'and sen_no = ?"+pkPistr,Integer.class, objParam))!= 0){
            throw new BusException("老人优待证号重复！");
        }
        //6	、优抚证号是否重复
        objParam[0] = regvo.getSpcaNo();
        if(StringUtils.isNotBlank(regvo.getSpcaNo())
                && (DataBaseHelper.queryForScalar("select count(1) from pi_master "
                + "where del_flag = '0'and spca_no = ?"+pkPistr,Integer.class, objParam))!= 0){
            throw new BusException("优抚证号重复！");
        }
        //6	、诊疗卡号是否重复
        objParam[0] = regvo.getCodeOp();
        if(StringUtils.isNotBlank(regvo.getCodeOp())
                && (DataBaseHelper.queryForScalar("select count(1) from pi_master "
                + "where del_flag = '0' and  code_op=?"+pkPistr,Integer.class, objParam))!= 0){
            throw new BusException("诊疗卡号重复！");
        }
        //7,医疗证号
        objParam[0] = regvo.getMcno();
        if(StringUtils.isNotBlank(regvo.getMcno())
                && (DataBaseHelper.queryForScalar("select count(1) from pi_master "
                + "where del_flag = '0'and mcno = ?"+pkPistr,Integer.class, objParam))!= 0){
            throw new BusException("医疗证号重复！");
        }
        //7,医保卡号
        objParam[0] = regvo.getInsurNo();
        if(StringUtils.isNotBlank(regvo.getInsurNo())
                && (DataBaseHelper.queryForScalar("select count(1) from pi_master "
                + "where del_flag = '0'and insur_no = ?"+pkPistr,Integer.class, objParam))!= 0){
            throw new BusException("医保卡号重复！");
        }
        //8,健康卡号
        objParam[0] = regvo.getHicNo();
        if(StringUtils.isNotBlank(regvo.getHicNo())
                && (DataBaseHelper.queryForScalar("select count(1) from pi_master "
                + "where del_flag = '0'and hic_no = ?"+pkPistr,Integer.class, objParam))!= 0){
            throw new BusException("健康卡号重复！");
        }
        //8,市民卡号
        objParam[0] = regvo.getCitizenNo();
        if(StringUtils.isNotBlank(regvo.getCitizenNo())
                && (DataBaseHelper.queryForScalar("select count(1) from pi_master "
                + "where del_flag = '0'and citizen_no = ?"+pkPistr,Integer.class, objParam))!= 0){
            throw new BusException("市民卡号重复！");
        }
    }

    /**
     * 患者医保信息
     * @param regvo
     * @return
     */
    private List<PiInsurance> transformPiInsurance(PiZsVo regvo){
        List<PiInsurance> insulist = new ArrayList<PiInsurance>();
        PiInsurance insu = DataBaseHelper.queryForBean("select pk_insurance from pi_insurance where del_flag=0 and pk_pi=? and pk_hp=?", PiInsurance.class, new Object[]{regvo.getPkPi(),regvo.getPkHp()});
        //目前调用的方法是全部删了做插入的，那么这里直接设置为默认，序号为0
        if(insu == null) {
            insu = new PiInsurance();
            insu.setFlagDef("1");
            insu.setSortNo(0L);
            insu.setPkPi(regvo.getPkPi());
            insu.setPkHp(regvo.getPkHp());
        }
        insulist.add(insu);
        return insulist;
    }

    /***
     * 更新患者就诊医保信息
     * @param param
     * @param user
     */
    public void savePvinfo(String param, IUser user){
        PvEncounter pvinfo=JsonUtil.readValue(param,PvEncounter.class);
        if(pvinfo==null|| CommonUtils.isEmptyString(pvinfo.getPkPv())) {
            throw new BusException("当前患者信息为空！");
        }

        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String upContraception = paramMap.get("upContraception") != null ? paramMap.get("upContraception").toString() : "";

        //更新避孕信息
        if(StringUtils.isNotEmpty(upContraception)){
            DataBaseHelper.update(" update PV_ENCOUNTER set pk_contraception =:pkContraception,dt_jurisdiction=:dtJurisdiction where PK_PV=:pkPv " ,pvinfo);
            return;
        }
        if(CommonUtils.isNotNull(pvinfo.getPkPicate())){
            DataBaseHelper.update(" update PV_ENCOUNTER set PK_INSU =:pkInsu,PK_PICATE=:pkPicate where PK_PV=:pkPv " ,pvinfo);
        }else{
            DataBaseHelper.update(" update PV_ENCOUNTER set PK_INSU =:pkInsu,note=:note where PK_PV=:pkPv " ,pvinfo);
        }
    }

    /**
     * 患者信息保存
     * @param param
     * @param user
     */
    public void PatiSave(String param , IUser user){
        PiMaster pait=JsonUtil.readValue(param, PiMaster.class);
        PvEncounter pven=JsonUtil.readValue(param, PvEncounter.class);
        InsQgybPV ins=JsonUtil.readValue(param,InsQgybPV.class);
        Map<String,Object> pvOp=JsonUtil.readValue(param, Map.class);
        User u=(User) user;
        if(pven.getPkPv()==null) {
            throw new BusException("请传入患者就诊主键！");
        }
        if(pait.getPkPi()==null) {
            throw new BusException("请传入患者主键！");
        }

        //更新患者信息
        DataBaseHelper.update("update pi_master set dt_idtype=:dtIdtype,id_no = :idNo,name_pi = :namePi,dt_sex=:dtSex,birth_date=:birthDate,mobile = :mobile,addrcode_cur=:addrcodeCur,addr_cur=:addrCur,modifier=:modifier,insur_no=:insurNo where pk_pi = :pkPi " , pait);
        //更新患者就诊信息
        DataBaseHelper.update("update pv_encounter set name_pi = :namePi,dt_sex=:dtSex,addrcode_cur=:addrcodeCur,addr_cur_dt=:addrCurDt,height=:height,weight=:weight,idno_agent=:idnoAgent,name_agent=:nameAgent,tel_agent=:telAgent,dt_spcdtype=:dtSpcdtype,flag_card_chk=:flagCardChk,modifier=:modifier,pk_insu=:pkInsu where pk_pv = :pkPv " , pven);
        //更新患者疾病信息
        DataBaseHelper.update("update cn_emr_op set sbp=:sbp,dbp=:dbp,modifier=:modifier where pk_pv = :pkPv " , pvOp);
        //更新患者医保相关信息
        DataBaseHelper.update("update ins_szyb_visit_city set cka303=:cka303,cka304=:cka304,cme320=:cme320,amc021=:amc021,cme331=:cme331,alc005=:alc005,cka305=:cka305 where pk_pv = :pkPv " , pvOp);

        String ybpvSql="select count(1) from ins_qgyb_pv where pk_pv=?";
        int countpv=DataBaseHelper.queryForScalar(ybpvSql,Integer.class,new Object[]{pven.getPkPv()});

        //医保信息
        if(StringUtils.isBlank(ins.getPkInspv()) && countpv==0){ //新增
            ins.setPkOrg(u.getPkOrg());
            ins.setPkPi(pven.getPkPi());
            ins.setPkPv(pven.getPkPv());
            ins.setPkHp(pven.getPkInsu());
            ins.setDelFlag("0");
            ins.setCreator(u.getPkEmp());
            ins.setCreateTime(new Date());
            ins.setTs(new Date());
            DataBaseHelper.insertBean(ins);
        }else{
            DataBaseHelper.update(" update ins_qgyb_pv set med_type=:medType,dise_codg=:diseCodg,dise_name=:diseName,birctrl_type=:birctrlType,birctrl_matn_date=:birctrlMatnDate,matn_type=:matnType,geso_val=:gesoVal where pk_inspv=:pkInspv",ins);
        }

        /*** 修改病情描述信息 start ***/
        String sql="";

        //是否有医嘱
        if(Application.isSqlServer()){
            sql = "select top 1 * from CN_EMR_OP where PK_PV = ? order by ts desc ";
        }else{
            sql = "select * from CN_EMR_OP where PK_PV = ? and rownum=1 order by ts desc ";
        }
        Map<String,Object> rtn=DataBaseHelper.queryForMap(sql,new Object[]{pven.getPkPv()});

        if(StringUtils.isNotEmpty(MapUtils.getString(rtn,"present")) ||  StringUtils.isNotEmpty(MapUtils.getString(rtn,"problem"))){
            sql="update CN_RIS_APPLY set note_dise=? where pk_ordris in (select b.pk_ordris from CN_ORDER a inner join CN_RIS_APPLY b on a.pk_cnord=b.pk_cnord where a.PK_PV=? and ( b.note_dise is null or   ltrim(rtrim(b.note_dise)) ='') )";
            String present=MapUtils.getString(rtn,"present");
            String problem = MapUtils.getString(rtn,"problem");
            if(StringUtils.isNotEmpty(present) && StringUtils.isNotEmpty(problem)){
                if(problem.contains(present)){
                    DataBaseHelper.update(sql,new Object[]{problem,pven.getPkPv()});
                }else {
                    DataBaseHelper.update(sql,new Object[]{problem+ "\r\n"+present,pven.getPkPv()});
                }
            }else if(StringUtils.isNotEmpty(present)){
                DataBaseHelper.update(sql,new Object[]{present,pven.getPkPv()});
            }else if(StringUtils.isNotEmpty(problem)){
                DataBaseHelper.update(sql,new Object[]{problem,pven.getPkPv()});
            }
        }

        /*** 修改病情描述信息 end ***/

        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        PlatFormSendUtils.sendUpPiInfoMsg(paramMap);
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

        //删除对应数据信息
        //4664 【门诊医生站】取消就诊时物理删除患者对应的医嘱、病历、诊断等，删除对应的就诊记录
        //查询患者是否有计费信息
        StringBuffer sql1=new StringBuffer();
        sql1.append(" select * from bl_op_dt where flag_settle='1' and pk_pv=?  ");
        List<Map<String,Object>> result = DataBaseHelper.queryForList(sql1.toString(),new Object[]{pkPv});

        if (result!=null && result.size()>0){
            throw new BusException("该患者已缴费，不能取消接诊！");
        }

        //查询当前患者是否有入院通知单信息
        List<Map<String,Object>> result1 = DataBaseHelper.queryForList(" select * from PV_IP_NOTICE where pk_pv_op=? ",new Object[]{pkPv});
        if (result1!=null && result1.size()>0){
            throw new BusException("该患者已申请住院，不能取消接诊！");
        }

        try{
            //4664 【门诊医生站】取消就诊时物理删除患者对应的医嘱、病历、诊断等，删除对应的就诊记录
            //：医嘱，病历，诊断，收费明细，检查/检验申请单 物理删除,其他逻辑保持不变
            String sql = "select *\n" +
                    "from pv_encounter pv\n" +
                    "         inner join bl_settle st on pv.pk_pv = st.pk_pv\n" +
                    "         inner join bd_hp hp on st.pk_insurance = hp.pk_hp\n" +
                    "where st.dt_sttype = '01'\n" +
                    "  and st.eu_stresult = '0'\n" +
                    "  and pv.eu_pvtype in (1, 2, 4)\n" +
                    "and pv.PK_PV = ?\n" +
                    "order by st.date_st desc";
            List<Map<String,Object>> resultBlSettle  = DataBaseHelper.queryForList(sql,new Object[]{pkPv});
            List<String> orderNo = new ArrayList<>();
            if(resultBlSettle.size()<=0){
                //获取检查检验申请单号
                sql = "select PK_CNORD from CN_ORDER where PK_PV = ?";
                orderNo = DataBaseHelper.queryForList(sql,String.class,new Object[]{pkPv});
                //更新就诊记录状态为9
                sql = "update PV_ENCOUNTER set EU_STATUS = '9' where PK_PV = ?";
                DataBaseHelper.execute(sql,new Object[]{pkPv});
                //物理删除收费明细
                sql = "delete from bl_op_dt where PK_PV = ?";
                DataBaseHelper.execute(sql,new Object[]{pkPv});
                //物理删除医嘱
                sql = "delete from CN_ORDER where PK_PV = ?";
                DataBaseHelper.execute(sql,new Object[]{pkPv});
                //删除病例，删除就诊文书
                //改前端调用交易码
                sql = "delete from pv_doc where pk_pv=?";
                DataBaseHelper.execute(sql,new Object[]{pkPv});
//                sql = "delete from cn_emr_op where pk_pv=?";
//                DataBaseHelper.execute(sql,new Object[]{pkPv});
                //物理删除诊断
                sql = "delete from PV_DIAG where PK_PV = ?";
                DataBaseHelper.execute(sql,new Object[]{pkPv});
                //清空预约记录就诊主键
                sql = "update SCH_APPT_PV set PK_PV=null where PK_PV = ?";
                DataBaseHelper.execute(sql,new Object[]{pkPv});

                //物理删除病历记录
                sql ="delete from cn_emr_op where pk_pv= ? ";
                DataBaseHelper.execute(sql,new Object[]{pkPv});
                sql ="delete from PV_DOC where pk_pv= ? ";
                DataBaseHelper.execute(sql,new Object[]{pkPv});

                //记录系统应用日志
                //todo
                //删除检查检验申请单

                orderNo.forEach(a->{
                    String tempsql = "delete from CN_LAB_APPLY where PK_CNORD = ?";
                    DataBaseHelper.execute(tempsql,new Object[]{a});
                    tempsql = "delete from CN_RIS_APPLY where PK_CNORD = ?";
                    DataBaseHelper.execute(tempsql,new Object[]{a});
                });



            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }


        //更新就诊状态，解锁
        int upCount = DataBaseHelper.update("update pv_encounter  set eu_status='9', pk_emp_phy=null, name_emp_phy=null, eu_locked='0' where pk_pv=? and eu_locked!='2'  ", new Object[]{pkPv});
        if (upCount > 0) {
            List<Map<String, Object>> opMap = DataBaseHelper.queryForList("select * from pv_op where pk_pv=?", new Object[]{pkPv});
            List<Map<String, Object>> erMap = DataBaseHelper.queryForList("select * from pv_er where pk_pv=?", new Object[]{pkPv});
            if (opMap.size() > 0) {
                DataBaseHelper.update("update pv_encounter  set pk_dept=?,pk_emp_phy=?,name_emp_phy=? where pk_pv=?", new Object[]{opMap.get(0).get("pkDeptPv"), opMap.get(0).get("pkEmpPv"), opMap.get(0).get("nameEmpPv"), pkPv});
            }else if (erMap.size() > 0) {
                DataBaseHelper.update("update pv_encounter  set pk_dept=?,pk_emp_phy=?,name_emp_phy=? where pk_pv=?", new Object[]{erMap.get(0).get("pkDeptPv"), erMap.get(0).get("pkEmpPv"), erMap.get(0).get("nameEmpPv"), pkPv});
            }
            //DataBaseHelper.update("update pv_op  set pk_res_cn=null, pk_schsrv_cn=null where pk_pv=?", new Object[]{pkPv});
            //DataBaseHelper.update("update pv_er  set pk_res_cn=null, pk_schsrv_cn=null where pk_pv=?", new Object[]{pkPv});
            if ("1".equals(stopCallNum)) {
                DataBaseHelper.update("update pv_que set eu_status='9', date_over=?, cnt_over=cnt_over+1  where pk_pv=? and eu_status='1' ", new Object[]{new Date(), pkPv});
            } else {
                DataBaseHelper.update("update pv_que  set eu_status='0' where pk_pv=? and eu_status > '0	' ", new Object[]{pkPv});
            }

        } else {
            throw new BusException("取消接诊失败！");
        }

        //发送完成接诊消息ADT^A03
        PlatFormSendUtils.sendCancelClinicMsg(paramMap);
    }

    /**
     * 查询就诊患者信息
     * @param param
     * @param user
     * @return
     */
    public Map<String,Object> qryPvInfo(String param , IUser user){
        Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
        int pageIndex = MapUtils.getIntValue(paramap, "pageIndex",1);
        int pageSize = MapUtils.getIntValue(paramap, "pageSize",50);

        //MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> searchPv = cnOpPiDao.searchPv(paramap);
        //Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
        Map<String, Object> result = new HashMap<>();
        result.put("list", searchPv);
        //result.put("totalCount", page.getTotalCount());

        return result;
//        if(searchPv != null && searchPv.size() > 0) {
//        	//查询医保信息
//        	Set<String> pkPvs = new HashSet<String>();
//        	for (Map<String, Object> map : searchPv) {
//        		pkPvs.add(map.get("pkPv").toString());
//			}
//        	StringBuffer str = new StringBuffer();
//        	str.append(" select pv.pk_inspv,pv.pk_pv,pv.med_type,dict.name as med_name,pv.dise_codg,pv.dise_name, ");
//        	str.append(" pv.birctrl_type,dict1.name as birctrl_name,pv.birctrl_matn_date,pv.matn_type,  ");
//        	str.append(" dict2.name as matn_name,pv.geso_val from ins_qgyb_pv pv");
//        	str.append(" left join ins_qgyb_dict dict on pv.med_type=dict.code and dict.code_type='med_type' ");
//        	str.append(" left join ins_qgyb_dict dict1 on pv.birctrl_type=dict1.code and dict1.code_type='birctrl_type' ");
//        	str.append(" left join ins_qgyb_dict dict2 on pv.matn_type=dict2.code and dict2.code_type='matn_type' ");
//        	str.append(" where pv.pk_pv in (");
//        	str.append(ArchUtil.convertSetToSqlInPart(pkPvs, "pk_pv"));
//        	str.append(") ");
//        	List<Map<String, Object>> qgybPvList = DataBaseHelper.queryForList(str.toString(), new Object[] {});
//
//        	//查询诊断
//            str = new StringBuffer();
//            str.append(" SELECT * from PV_DIAG diag ");
//            str.append(" where diag.pk_pv in (");
//            str.append(ArchUtil.convertSetToSqlInPart(pkPvs, "pk_pv"));
//            str.append(") ");
//            List<Map<String, Object>> diagPvList = DataBaseHelper.queryForList(str.toString(), new Object[] {});
//
//            for (Map<String, Object> searchPvMap : searchPv) {
//                String pkPv = searchPvMap.get("pkPv").toString();
//                //全国医保
//                if(qgybPvList != null && qgybPvList.size() > 0) {
//                    for (Map<String, Object> qgybPvMap : qgybPvList) {
//                        String qgybPv = qgybPvMap.get("pkPv").toString();
//                        if(pkPv.equals(qgybPv)) {
//                            searchPvMap.put("pkInspv", CommonUtils.getPropValueStr(qgybPvMap, "pkInspv"));
//                            searchPvMap.put("medType", CommonUtils.getPropValueStr(qgybPvMap, "medType"));
//                            searchPvMap.put("medName", CommonUtils.getPropValueStr(qgybPvMap, "medName"));
//                            searchPvMap.put("diseCodg", CommonUtils.getPropValueStr(qgybPvMap, "diseCodg"));
//                            searchPvMap.put("diseName", CommonUtils.getPropValueStr(qgybPvMap, "diseName"));
//                            searchPvMap.put("birctrlType", CommonUtils.getPropValueStr(qgybPvMap, "birctrlType"));
//                            searchPvMap.put("birctrlName", CommonUtils.getPropValueStr(qgybPvMap, "birctrlName"));
//                            searchPvMap.put("birctrlMatnDate", CommonUtils.getPropValueStr(qgybPvMap, "birctrlMatnDate"));
//                            searchPvMap.put("matnType", CommonUtils.getPropValueStr(qgybPvMap, "matnType"));
//                            searchPvMap.put("matnName", CommonUtils.getPropValueStr(qgybPvMap, "matnName"));
//                            searchPvMap.put("gesoVal", CommonUtils.getPropValueStr(qgybPvMap, "gesoVal"));
//                        }
//                    }
//                }
//
//
//                //诊断
//                if(diagPvList != null && diagPvList.size() > 0) {
//                    String namediag="";
//                    for (Map<String, Object> qdiagPvMap : diagPvList) {
//                        String qgybPv = qdiagPvMap.get("pkPv").toString();
//                        if (pkPv.equals(qgybPv)) {
//                            namediag=namediag+MapUtils.getString(qdiagPvMap,"nameDiag")+",";
//                        }
//                    }
//                    if(namediag.length()>1){
//                        namediag.substring(namediag.length()-1);
//                    }
//                    searchPvMap.put("nameDiag",namediag);
//                }
//
//            }
//        }
//        return searchPv;

    }

    /**
     * 统计数据
     * @param param
     * @param user
     * @return
     */
    public Map<String,Object> totalPvInfo(String param , IUser user){
        Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
        //查询数量
        Map<String, Object> pvSum=cnOpPiDao.searchPvCount(paramap);
        return pvSum;
    }
    /**
     * 查询患者详细信息
     * @param param
     * @param user
     * @return
     */
    public Map<String,Object> qryPvInfoDeti(String param , IUser user){
        Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
        String pkPv=MapUtils.getString(paramap,"pkPv");
        if(StringUtils.isEmpty(pkPv)){
            return null;
        }

        String pkPi=MapUtils.getString(paramap,"pkPi");
        if(StringUtils.isEmpty(pkPi)){
            return null;
        }
        //医保信息
        List<Map<String, Object>> rtnMap=cnOpPiDao.searchPvDeti(paramap);
        Map<String, Object> rtn=new HashMap<>();

        if(rtnMap!=null && rtnMap.size()>0){
            rtn=rtnMap.get(0);
        }else{
            rtn.put("pkInspv", null);
            rtn.put("medType", null);
            rtn.put("medName", null);
            rtn.put("diseCodg", null);
            rtn.put("diseName", null);
            rtn.put("birctrlType",null);
            rtn.put("birctrlName", null);
            rtn.put("birctrlMatnDate", null);
            rtn.put("matnType", null);
            rtn.put("matnName", null);
            rtn.put("gesoVal", null);
        }
        //住院申请
        int ipCount=DataBaseHelper.queryForScalar(" select count(1) from PV_IP_NOTICE where pk_pv_op=? and del_flag='0'", Integer.class,new Object[]{pkPv});
        if(ipCount>0){
            rtn.put("flagNotice","1");
        }else{
            rtn.put("flagNotice","0");
        }

        //过敏信息
        List<PiAllergic> allergics=DataBaseHelper.queryForList("select * from pi_allergic where del_flag = '0' and pk_pi = ? order by date_find",PiAllergic.class,new Object[]{pkPi});
        String allergName="";
        if(allergics!=null && allergics.size()>0){
            for (int i=0;i<allergics.size();){
                if(i!=allergics.size()-1)
                    allergName = allergName+allergics.get(i).getNameAl()+",";
                else
                    allergName = allergName+allergics.get(i).getNameAl();
                i=i+1;
            }
        }
        rtn.put("nameAl",allergName);

        return rtn;
    }


    /***
     * 查询当前患者已结算费用
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryPvBlInfo(String param , IUser user){
        Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
        String pkPv=MapUtils.getString(paramap,"pkPv");
        if(StringUtils.isEmpty(pkPv)){
            throw new BusException("请传入患者主键！！！");
        }
        String sql=" select * from BL_OP_DT where PK_PV= '"+pkPv+"' and DEL_FLAG !='1' and FLAG_SETTLE='1' ";
        List<Map<String,Object>> list=DataBaseHelper.queryForList(sql);
        return list;
    }

    /**
     * 拷贝患者病历信息  022006004039
     * @param param
     * @param user
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void saveEmr(String param , IUser user) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
        String pkPv=MapUtils.getString(paramap,"pkPv");//新pkPv
        String pkPvOld=MapUtils.getString(paramap,"pkPvOld");//旧pkPv
        if(StringUtils.isEmpty(pkPv) || StringUtils.isEmpty(pkPvOld)){
            throw new BusException("请传入患者2次就诊主键！！！");
        }

        User u=(User)user;

        //查询旧就诊主键病历信息
        List<CnOpEmrRecord> listEmrOld=DataBaseHelper.queryForList(" select * from CN_EMR_OP where PK_PV=? and DEL_FLAG='0'",CnOpEmrRecord.class,new Object[]{pkPvOld});

        //查询旧就诊主键病历信息
        List<PvDoc> listDocOld=DataBaseHelper.queryForList(" select doc.*,doc.name as temp_name from pv_doc doc where doc.pk_pv=? and doc.del_flag='0'",PvDoc.class,new Object[]{pkPvOld});

        if(listEmrOld!=null && listEmrOld.size()>0){
            List<CnOpEmrRecord> listEmrNew=new ArrayList<>();
            for (CnOpEmrRecord emrRecord : listEmrOld){
                CnOpEmrRecord vo =new CnOpEmrRecord();
                BeanUtils.copyProperties(vo,emrRecord);
                vo.setPkEmrop(NHISUUID.getKeyId());
                vo.setPkPv(pkPv);
                vo.setDateEmr(new Date());
                vo.setCreator(u.getPkEmp());
                vo.setCreateTime(new Date());
                listEmrNew.add(vo);
            }
            if(listEmrNew.size()>0){
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOpEmrRecord.class),listEmrNew);
            }
        }

        if(listDocOld!=null && listDocOld.size()>0){
            List<PvDoc> listDocNew=new ArrayList<>();
            for (PvDoc pvDoc : listDocOld){
                PvDoc vo = new PvDoc();
                BeanUtils.copyProperties(vo,pvDoc);
                vo.setPkPvdocBak(vo.getPkPvdoc());
                vo.setPkPvdoc(NHISUUID.getKeyId());
                vo.setPkPv(pkPv);
                vo.setDateDoc(new Date());
                vo.setPkEmpDoc(u.getPkEmp());
                vo.setNameEmpDoc(u.getNameEmp());
                vo.setCreator(u.getPkEmp());
                vo.setCreateTime(new Date());
                vo.setRemark("copy");
                
                listDocNew.add(vo);
            }
            if(listDocNew.size()>0){
                //DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvDoc.class),listDocNew);
            	
            	String saveDataMode = EmrSaveUtils.getSaveDataMode();
        		if(saveDataMode.equals("0")){
        			//本地存储
        			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvDoc.class),listDocNew);
        		 }else if(saveDataMode.equals("1")){
         			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvDoc.class),listDocNew);

         			String dbName = EmrSaveUtils.getDbName();
        			 //独立存储
        			 List<PvDoc> listDocData=DataBaseHelper.queryForList(" select doc.*,doc.name as temp_name from "+dbName+"pv_doc doc where doc.pk_pv=? and doc.del_flag='0'",PvDoc.class,new Object[]{pkPvOld});
        			 if(listDocData!=null && listDocData.size()>0){
        				 List<PvDoc> listDocDataNew=new ArrayList<>();
    		            for (PvDoc pvDoc : listDocData){
    		                PvDoc pvDocNew = new PvDoc();
    		                BeanUtils.copyProperties(pvDocNew,pvDoc);
    		                for (PvDoc pvDocH : listDocNew) {
    		                	if(pvDocNew.getPkPvdoc().equals(pvDocH.getPkPvdocBak())) {
    		                		pvDocNew.setPkPvdoc(pvDocH.getPkPvdoc());
    		                		break;
    		                	}
							}
    		                pvDocNew.setPkPv(pkPv);
    		                pvDocNew.setDateDoc(new Date());
    		                pvDocNew.setPkEmpDoc(u.getPkEmp());
    		                pvDocNew.setNameEmpDoc(u.getNameEmp());
    		                pvDocNew.setCreator(u.getPkEmp());
    		                pvDocNew.setCreateTime(new Date());
    		                pvDocNew.setRemark("copy");
    		                
    		                listDocDataNew.add(pvDocNew);
    		            }
    		            if(listDocDataNew.size()>0) {
    		            	for (PvDoc doc : listDocDataNew) {
    		            		doc.setDbName(dbName);
    		            		recMapper.savePvDocEmr(doc);
							}
    		            	
    		            }
    		            
        			 }
        		 }
            }
        }

    }

    public PvEncounter qryPVInfo(String param , IUser user){
        Map<String, Object> paramap = JsonUtil.readValue(param, Map.class);
        String pkPv=MapUtils.getString(paramap,"pkPv");//新pkPv
        if(StringUtils.isEmpty(pkPv)){
            throw new BusException("请传入患者主键");
        }
        PvEncounter pv=DataBaseHelper.queryForBean(" select * from PV_ENCOUNTER where pk_pv=? ",PvEncounter.class,new Object[]{pkPv});
        return pv;
    }

    /***
     *
     * @param param
     * @param user
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public  void savePvInfo(String param , IUser user) throws InvocationTargetException, IllegalAccessException {
        Map<String,Object> pv=JsonUtil.readValue(param, Map.class);
        String pkPv=MapUtils.getString(pv,"pkPv");
        if(pv==null || StringUtils.isEmpty(pkPv)){
            throw new BusException("请传入患者主键");
        }
        String pkWgOld=MapUtils.getString(pv,"pkWgOld");

        String pkWg=MapUtils.getString(pv,"pkWg");
        if(pv==null || StringUtils.isEmpty(pkWg)){
            throw new BusException("请传入患者新医疗组");
        }


        User user1=(User)user;
        PvEncounter pvEncounter=DataBaseHelper.queryForBean(" select * from PV_ENCOUNTER where pk_pv=? ",PvEncounter.class,new Object[]{pkPv});
        if(pvEncounter==null) return;
        PvClinicGroup pvClinicGroup=new PvClinicGroup();
        if(StringUtils.isNotEmpty(pkWgOld)){
            PvClinicGroup pvGroup= DataBaseHelper.queryForBean(" select * from pv_clinic_group where pk_pv =? AND pk_dept =? AND pk_wg =? ",PvClinicGroup.class,new Object[]{pkPv,pkWgOld,pvEncounter.getPkDept()});
            if(pvGroup!=null){
                pvGroup.setEuStatus("0");
                pvGroup.setDateEnd(new Date());
                pvGroup.setModifier(user1.getPkEmp());
                DataBaseHelper.updateBeanByPk(pvGroup);
            }
        }

        pvClinicGroup.setPkClinicgroup(NHISUUID.getKeyId());
        pvClinicGroup.setPkPv(pvEncounter.getPkPv());
        pvClinicGroup.setPkDept(pvEncounter.getPkDept());
        pvClinicGroup.setPkDeptNs(pvEncounter.getPkDeptNs());
        pvClinicGroup.setPkWg(pkWg);
        pvClinicGroup.setDateBegin(new Date());
        pvClinicGroup.setCreator(user1.getPkEmp());
        pvClinicGroup.setCreateTime(new Date());
        pvClinicGroup.setEuStatus("1");

        DataBaseHelper.update("update PV_ENCOUNTER set pk_wg =? where PK_PV=? " ,new Object[]{pkWg,pkPv});
        DataBaseHelper.insertBean(pvClinicGroup);

    }


    /********************************* 患者信息 start***************************************/

    /***
     *
     *022006004053
     * @param param
     * @param user
     */
    public void qryIpCheck(String param , IUser user){
        Map<String,Object> pvInof=JsonUtil.readValue(param, Map.class);
        //患者主键
        String pkPi=MapUtils.getString(pvInof,"pkPi");
        if(StringUtils.isEmpty(pkPi)){
            throw new BusException("请传入患者主键");
        }
        //科室主键
        String pkDept=MapUtils.getString(pvInof,"pkDept");
        if(StringUtils.isEmpty(pkDept)){
            throw new BusException("请传入科室主键");
        }

        //入院科室是否为日间病房
        String sql="Select * From bd_dictattr " +
                " Where code_attr='0605' And val_attr='1' and pk_dict='"+pkDept+"'";
        List<Map<String,Object>> listKS = DataBaseHelper.queryForList(sql);
        //非日间病房，不作任何判断
        if(listKS==null || listKS.size()==0){
            return;
        }

        String mes="当前患者非中山市参保的一类门特患者，不允许申请日间病房的入院！";
        //医疗类别
        String yllb=MapUtils.getString(pvInof,"yllb");
        if(StringUtils.isEmpty(yllb) || !"14".equals(yllb)){
            throw new BusException(mes);
        }
        //病种
        String bz=MapUtils.getString(pvInof,"bz");
        if(StringUtils.isEmpty(bz)){
            throw new BusException(mes);
        }
        sql="Select * From ins_qgyb_dict " +
                "Where code_type='disease_tb' And note='一类门特' and  Code='"+bz+"'";
        List<Map<String,Object>> listBZ=DataBaseHelper.queryForList(sql);
        if(listBZ==null || listBZ.size()==0){
            throw new BusException(mes);
        }

        //判断患者参保地区
        sql="select mas.pk_insupi,mas.psn_name  from INS_QGYB_MASTER mas " +
                "left join INS_QGYB_INSUTYPE insu on insu.PK_INSUPI=mas.PK_INSUPI " +
                "where insu.insuplc_admdvs='442000' and insu.psn_insu_stas = '1' and insu.del_flag='0' and mas.del_flag='0' " +
                "and mas.pk_pi='"+pkPi+"' ";
        List<Map<String,Object>> listYB=DataBaseHelper.queryForList(sql);
        if(listYB==null || listYB.size()==0){
            throw new BusException(mes);
        }

    }

    /**
     * 患者医保是否为中山市医保  022006004061
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> checkPiYb(String param , IUser user){
        Map<String,Object> pvInof=JsonUtil.readValue(param, Map.class);

        //患者主键
        String pkPi=MapUtils.getString(pvInof,"pkPi");
        if(StringUtils.isEmpty(pkPi)){
            throw new BusException("请传入患者主键");
        }
        //患者医保主键
        String pkInsupi=MapUtils.getString(pvInof,"pkInsupi");
        if(StringUtils.isEmpty(pkInsupi)){
            throw new BusException("请传入患者医保主键");
        }

        pvInof.put("zsyb","0");
        //判断患者参保地区
        String sql="select mas.pk_insupi,mas.psn_name  from INS_QGYB_MASTER mas " +
                "left join INS_QGYB_INSUTYPE insu on insu.PK_INSUPI=mas.PK_INSUPI " +
                "where insu.insuplc_admdvs='442000' and insu.psn_insu_stas = '1' and insu.del_flag='0' and mas.del_flag='0' " +
                "and mas.pk_pi='"+pkPi+"' and mas.pk_insupi='"+pkInsupi+"'";
        List<Map<String,Object>> listYB=DataBaseHelper.queryForList(sql);
        if(listYB!=null && listYB.size()>0){
            pvInof.put("zsyb","1");
        }

        return pvInof;
    }

    /**
     *022006004058
     * 2.2.51.6.2.判断一类门特病种
     * @return
     */
    public List<Map<String,Object>> checkYlmt(String param , IUser user){
        Map<String,Object> map=JsonUtil.readValue(param,Map.class);
        String code=MapUtils.getString(map,"code");
        if(StringUtils.isEmpty(code)){
            throw new BusException("请传入需要查询的医保编码！");
        }

        String sql="Select * From ins_qgyb_dict " +
                "Where code_type='disease_tb' And Code=?";

        List<Map<String,Object>> rtn = DataBaseHelper.queryForList(sql,new Object[]{code});
        return rtn;
    }

    /**
     *022006004059
     * 用于查询日间手术病房对应的手术病种
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryOpBz(String param , IUser user){
        Map<String,Object> map=JsonUtil.readValue(param,Map.class);
        String pkDept=MapUtils.getString(map,"pkDept");
        if(StringUtils.isEmpty(pkDept)){
            throw new BusException("请传入需要查询的科室主键！");
        }

        String sql="Select sur.* From bd_surdise sur " +
                "Where sur.pk_dept=? And sur.del_flag='0'";

        List<Map<String,Object>> rtn = DataBaseHelper.queryForList(sql,new Object[]{pkDept});
        return rtn;
    }

    /***
     *022006004060
     * 日间手术病种门诊可开立项目 \30天内开立入日间手术病房对应病种不可开立的项目
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryOpBzOrd(String param , IUser user){
        Map<String,Object> map=JsonUtil.readValue(param,Map.class);
        String pkSurdise=MapUtils.getString(map,"pkSurdise");
        if(StringUtils.isEmpty(pkSurdise)){
            throw new BusException("请传入需要查询的手术病种主键！");
        }
        List<Map<String,Object>> rtn =null;
        boolean allow="1".equals(MapUtils.getString(map,"allow"));
        String sql="";
        if(allow){
            //日间手术病种门诊可开立项目
            sql="Select surdt.pk_surdisedt,surdt.pk_ord,ord.code,ord.name\n" +
                    "From bd_surdise_dt surdt\n" +
                    "Inner Join bd_ord ord On surdt.pk_ord=ord.pk_ord\n" +
                    "Where surdt.pk_surdise=? And surdt.del_flag='0' And  surdt.eu_type='0'";
            rtn = DataBaseHelper.queryForList(sql,new Object[]{pkSurdise});
        }else{
            String pkPi=MapUtils.getString(map,"pkPi");
            if(StringUtils.isEmpty(pkPi)){
                throw new BusException("请传入需要查询的手术病种患者主键！");
            }
            //30天内开立入日间手术病房对应病种不可开立的项目
            sql="Select cnord.pk_ord,cnord.name_ord \n" +
                    "From cn_order cnord\n" +
                    "Inner Join bd_surdise_dt surdt On cnord.pk_ord=surdt.pk_ord\n" +
                    "Where cnord.pk_pi=? And cnord.date_start>Sysdate-30 And surdt.pk_surdise=? And surdt.eu_type='1' ";
            rtn = DataBaseHelper.queryForList(sql,new Object[]{pkPi,pkSurdise});
        }
        return rtn;

    }

    public Map<String,Object> qryPvBl(String param , IUser user){
        Map<String,Object> pvInof=JsonUtil.readValue(param, Map.class);

        //患者主键
        String pkPi=MapUtils.getString(pvInof,"pkPi");
        if(StringUtils.isEmpty(pkPi)){
            throw new BusException("请传入患者主键");
        }
        //医保类型
        String code=MapUtils.getString(pvInof,"code");
        if(StringUtils.isEmpty(code)){
            throw new BusException("请传入医保类型编码");
        }

        //默认非生育门诊
        pvInof.put("symz","0");

        //查询计算信息
        List<Map<String, Object>> pvSettle=cnOpPiDao.searchPvSettle(pvInof);

        if(pvSettle!=null && pvSettle.size()>0){
            String pkPv=MapUtils.getString(pvSettle.get(0),"pkPv");
            if(StringUtils.isNotEmpty(pkPv)){
                //医保信息
                List<Map<String, Object>> ybMapList=cnOpPiDao.searchPvDeti(pvSettle.get(0));
                if(ybMapList!=null && ybMapList.size()>0){
                    String medType=MapUtils.getString(ybMapList.get(0),"medType");
                    if(code.equals(medType)){
                        pvInof.put("symz","1");
                    }
                }
            }
        }

        return pvInof;
    }

    /********************************* 患者信息 end***************************************/
}
