package com.zebone.nhis.labor.nis.service;

import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.module.labor.nis.PvLaborInstru;
import com.zebone.nhis.common.module.labor.nis.PvLaborRec;
import com.zebone.nhis.common.module.labor.nis.PvLaborRecDt;
import com.zebone.nhis.common.module.pv.PvInfant;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ex.nis.pi.vo.PvIpVo;
import com.zebone.nhis.ex.pub.service.PvInfantPubService;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.BabyBedVO;
import com.zebone.nhis.ex.pub.vo.PvInfantVo;
import com.zebone.nhis.labor.nis.dao.PvLaborRecMapper;
import com.zebone.nhis.labor.nis.vo.PvLaborRecVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PvLaborRecService {

    @Resource
    private PvLaborRecMapper pvLaborRecMapper;
    @Resource
    private PvInfantPubService pvInfantPubService;

    /**
     * 根据就诊主键查询分娩记录及明细
     *
     * @param param{pkOrg,pkPv,pkPvlaborrec}
     * @param user
     * @return
     */
    public PvLaborRecVo queryPvLaborRec(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (CommonUtils.isNull(paramMap.get("pkOrg"))) {
            paramMap.put("pkOrg", ((User) user).getPkOrg());
        }
        List<PvLaborRecVo> reclist = pvLaborRecMapper.queryPvLaborRec(paramMap);
        if (reclist == null || reclist.size() == 0)
            return null;
        if (reclist.size() > 1)
            throw new BusException("该患者存在多条可用产房就诊记录，请检查！");
        PvLaborRecVo vo = reclist.get(0);
        if (vo == null) return null;
        paramMap.put("pkLaborrec", vo.getPkLaborrec());
        List<PvLaborRecDt> dtlist = pvLaborRecMapper.queryPvLaborRecDt(paramMap);
        List<PvInfantVo> inflist = pvLaborRecMapper.queryPvInfant(paramMap);
        //2020-03-13 手动分床模式查询分床记录
//    	String BD0013 = ApplicationUtils.getSysparam("BD0013",false);
//    	if("0".equals(BD0013)){
        String sql = "select pv.pk_pv,pv.pk_dept,pv.pk_dept_ns,bed.pk_bed,bed.code as codeBed,staff.pk_emp as pk_Ip_Psn_Main,"
                + "wg.pk_wg as pk_ip_wg,staffns.pk_emp as pk_Ip_Psn_Ns,pv.date_admit from PV_ENCOUNTER pv "
                + "inner join pv_infant inf on pv.pk_pv=inf.pk_pv_infant "
                + "left join bd_res_bed bed on pv.pk_pi=bed.pk_pi "
                + "left join PV_STAFF staff on staff.pk_pv=pv.pk_pv and staff.dt_role='0001' "
                + "left join PV_STAFF staffns on staffns.pk_pv=pv.pk_pv and staffns.dt_role='0100' "
                + "left join PV_CLINIC_GROUP wg on wg.pk_pv=pv.pk_pv "
                + "where inf.pk_laborrec = ?  and bed.del_flag = '0' and bed.flag_active = '1' and inf.del_flag = '0'  "
                + "and pv.eu_status = '1' and pv.eu_pvtype = '3' ";
        List<BabyBedVO> babyBed = DataBaseHelper.queryForList(sql, BabyBedVO.class, new Object[]{vo.getPkLaborrec()});
        if (babyBed.size() > 0) {
            for (PvInfantVo pvInfant : inflist) {
                for (BabyBedVO babyBedVO : babyBed) {
                    if (babyBedVO != null && babyBedVO.getPkPv().equals(pvInfant.getPkPvInfant())) {
                        pvInfant.setBabyBed(babyBedVO);
                    }
                }
            }
        }
//		}
        vo.setDtlist(dtlist);
        vo.setInflist(inflist);
        return vo;
    }

    /**
     * 根据就诊主键查询分娩记录不含明细
     *
     * @param param{pkOrg,pkPv,pkPvlaborrec}
     * @param user
     * @return
     */
    public PvLaborRecVo queryPvLaborRecInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (CommonUtils.isNull(paramMap.get("pkOrg"))) {
            paramMap.put("pkOrg", ((User) user).getPkOrg());
        }
        List<PvLaborRecVo> reclist = pvLaborRecMapper.queryPvLaborRec(paramMap);
        if (reclist == null)
            return null;
        if (reclist.size() > 1)
            throw new BusException("该患者存在多条可用产房就诊记录，请检查！");
        PvLaborRecVo vo = reclist.get(0);
        return vo;
    }

    /**
     * 保存婴儿分娩记录-一次只能保存一个婴儿的信息
     *
     * @param param{PvLaborRecVo}
     * @param user
     */
    public String savePvLaborRec(String param, IUser user) {
        PvLaborRecVo pvLaborRec = JsonUtil.readValue(param, PvLaborRecVo.class);
        if (pvLaborRec == null) {
            throw new BusException("未获取到要保存的内容！");
        }
        String pkInf = "";
        PvLaborRec labor = new PvLaborRec();
        ApplicationUtils.copyProperties(labor, pvLaborRec);
        PvInfantVo infvo = new PvInfantVo();
        if (CommonUtils.isEmptyString(labor.getPkLaborrec())) {// 新增
            String pk_pvLaborrec = NHISUUID.getKeyId();
            labor.setPkLaborrec(pk_pvLaborrec);
            //防止重复插入，先校验是否已经存在分娩记录，存在则删除
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("pkPv", pvLaborRec.getPkPv());
            paramMap.put("pkOrg", ((User) user).getPkOrg());
            List<PvLaborRecVo> reclist = pvLaborRecMapper.queryPvLaborRec(paramMap);
            if (reclist == null)
                return null;
            if (reclist != null && reclist.size() > 0) {//存在则删除
                DataBaseHelper.execute("delete from pv_labor_rec where pk_pv = ? and pk_org = ? ", new Object[]{pvLaborRec.getPkPv(), ((User) user).getPkOrg()});
            }
            DataBaseHelper.insertBean(labor);
            if (pvLaborRec.getDtlist() != null && pvLaborRec.getDtlist().size() > 0) {
                PvLaborRecDt dt = pvLaborRec.getDtlist().get(0);//每次保存只有一个婴儿信息
                dt.setPkLaborrecdt(NHISUUID.getKeyId());
                dt.setPkLaborrec(pk_pvLaborrec);
                dt.setSortNo(ExSysParamUtil.getInfantSortNo(dt.getPkPv()));//设置婴儿序号
                DataBaseHelper.insertBean(dt);
                PvInfantVo pvInfantVo = generateInfant(dt, pvLaborRec.getInflist(), true);
                if ("死产".equals(pvInfantVo.getEuBirth()) || "死胎".equals(pvInfantVo.getEuBirth()) ||
                    "流产".equals(pvInfantVo.getEuBirth()) || "治疗性引产".equals(pvInfantVo.getEuBirth()) ||
                    "出生缺陷性引产".equals(pvInfantVo.getEuBirth())
                ) {
                    //死产，死胎患者只保存婴儿信息记录，不生成就诊数据
                    infvo = savePvInfant(pvInfantVo, (User) user);
                } else {
                    //同时增加婴儿信息
                    infvo = pvInfantPubService.saveInfant(pvInfantVo, (User) user);
                }
                //返回婴儿序号 + 婴儿主键
                pkInf = infvo.getSortNo() + "," + infvo.getPkInfant();
            }
        } else {// 修改
            DataBaseHelper.updateBeanByPk(labor, false);
            if(labor.getFlagDab() != null && "1".equals(labor.getFlagDab()) && labor.getPkLaborrec() != null && !"".equals(labor.getPkLaborrec())){
                DataBaseHelper.update("update pv_labor_rec set date_allbreak = null where pk_laborrec = ?",new Object[]{labor.getPkLaborrec()});
            }
            if (pvLaborRec.getDtlist() != null && pvLaborRec.getDtlist().size() > 0) {
                for (PvLaborRecDt dt : pvLaborRec.getDtlist()) {
                    if (CommonUtils.isEmptyString(dt.getPkLaborrecdt())) {//新增的明细
                        dt.setPkLaborrec(labor.getPkLaborrec());
                        dt.setPkLaborrecdt(NHISUUID.getKeyId());
                        dt.setSortNo(ExSysParamUtil.getInfantSortNo(dt.getPkPv()));//设置婴儿序号
                        DataBaseHelper.insertBean(dt);
                        PvInfantVo pvInfantVo = generateInfant(dt, pvLaborRec.getInflist(), true);
                        if ("死产".equals(pvInfantVo.getEuBirth()) || "死胎".equals(pvInfantVo.getEuBirth())||
                            "流产".equals(pvInfantVo.getEuBirth()) || "治疗性引产".equals(pvInfantVo.getEuBirth()) ||
                            "出生缺陷性引产".equals(pvInfantVo.getEuBirth())) {
                            //死产，死胎患者只保存婴儿信息记录，不生成就诊数据
                            infvo = savePvInfant(pvInfantVo, (User) user);
                        } else {
                            //同时增加婴儿信息
                            infvo = pvInfantPubService.saveInfant(pvInfantVo, (User) user);
                        }
                        //返回婴儿序号 + 婴儿主键
                        pkInf = infvo.getSortNo() + "," + infvo.getPkInfant();
                    } else {//修改的明细
                        DataBaseHelper.updateBeanByPk(dt, false);
                        //同时修改婴儿信息
                        PvInfantVo pvInfantVo = generateInfant(dt, pvLaborRec.getInflist(), false);
                        PvInfant infant = DataBaseHelper.queryForBean(
                                "select EU_BIRTH  from PV_INFANT PI  where PK_INFANT=?",
                                PvInfant.class, pvInfantVo.getPkInfant());
                        //只允许死胎，死产修改为活胎活产，不允许活胎改为死胎
                        String [] deathEuBirth=new String[]{"死产","死胎","流产","治疗性引产","出生缺陷性引产"};
                        Boolean euBirthChang = infant != null &&infant.getEuBirth()!=null
                                && Arrays.stream(deathEuBirth).allMatch(infant.getEuBirth()::equals)
                                && !Arrays.stream(deathEuBirth).allMatch(pvInfantVo.getEuBirth()::equals);
                        if (euBirthChang) {
                            DataBaseHelper.execute(
                                    "delete  from  PV_INFANT   where PK_INFANT=?", pvInfantVo.getPkInfant());
                            infvo = pvInfantPubService.saveInfant(generateInfant(dt, pvLaborRec.getInflist(), true), (User) user);
                            //返回婴儿序号 + 婴儿主键
                            pkInf = infvo.getSortNo() + "," + infvo.getPkInfant();
                        } else {
                            pvInfantPubService.updateInfant(pvInfantVo, (User) user);
                            //返回婴儿序号 + 婴儿主键
                            pkInf = (pvLaborRec.getInflist() == null || pvLaborRec.getInflist().size() <= 0) ? "" : pvLaborRec.getInflist().get(0).getSortNo() + "," + pvLaborRec.getInflist().get(0).getPkInfant();
                        }
                    }
                }
            }
        }
        //保存产程记录时患者如提前添加器械清点单，修改对应数据的分娩主键信息
        PvLaborInstru pvLaborInstru = DataBaseHelper.queryForBean("select * from pv_labor_instru where pk_pv = ?", PvLaborInstru.class, pvLaborRec.getPkPv());
        if (pvLaborInstru != null) {
            DataBaseHelper.update("update pv_labor_instru set pk_laborrec = ? where pk_pv = ?", new Object[]{labor.getPkLaborrec(), pvLaborRec.getPkPv()});
        }
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("adtType", "新出生");
        msgMap.put("codeIp", infvo.getCode());
        PlatFormSendUtils.sendDeptInMsg(msgMap);
        return pkInf;
    }

    /**
     * @return void
     * @Description 保存新生儿信息，只保存PV_INFANT表，其余信息不保存
     * @auther wuqiang
     * @Date 2020-10-10
     * @Param [vo, user]
     */
    private PvInfantVo savePvInfant(PvInfantVo vo, User user) {
        if (CommonUtils.isEmptyString(vo.getPkInfant())) {
            vo.setPkInfant(NHISUUID.getKeyId());//设置婴儿主键
        }
        vo.setEuStatusAdt("0");//设置婴儿转科状态
        if (vo.getSortNo() == 0 || vo.getSortNo() == null) {//未设置婴儿序号，取最大婴儿序号加1
            vo.setSortNo(ExSysParamUtil.getInfantSortNo(vo.getPkPv()));
        }
        //插入婴儿信息
        PvInfant infant = new PvInfant();
        ApplicationUtils.copyProperties(infant, vo);
        DataBaseHelper.insertBean(infant);
        return vo;
    }


    /**
     * 删除婴儿分娩记录
     *
     * @param param{pkLaborrec，pkLaborrecdt}
     * @param user
     */
    public void delPvLaborRec(String param, IUser user) {
        Map<String, String> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) {
            throw new BusException("未获取到要删除的内容！");
        }
        String pkLaborrec = paramMap.get("pkLaborrec");
        String pkLaborrecdt = paramMap.get("pkLaborrecdt");
        //分娩记录主键为空，删除所有分娩信息,并删除婴儿信息
        if (!CommonUtils.isEmptyString(pkLaborrec)) {
            DataBaseHelper.execute("delete from pv_labor_rec where pk_laborrec = ?", new Object[]{pkLaborrec});
            DataBaseHelper.execute("delete from pv_labor_rec_dt where pk_laborrec = ?", new Object[]{pkLaborrec});
            delInfant(pkLaborrec, null);
        }
        //分娩明细主键不为空，删除分娩明细，不删除分娩主记录，同时删除婴儿信息
        if (!CommonUtils.isEmptyString(pkLaborrecdt)) {
            DataBaseHelper.execute("delete from pv_labor_rec_dt where pk_laborrecdt = ?", new Object[]{pkLaborrecdt});
            delInfant(null, pkLaborrecdt);
        }
    }

    /**
     * 根据分娩记录删除婴儿信息
     *
     * @param pkLaborrec
     * @param pkLaborrecdt
     */
    private void delInfant(String pkLaborrec, String pkLaborrecdt) {
        //批量删除婴儿
        if (!CommonUtils.isEmptyString(pkLaborrec)) {
            List<PvInfant> list = DataBaseHelper.queryForList("select pk_infant,pk_pv_infant,pk_pv,pk_pi_infant from pv_infant where pk_laborrec = ? ", PvInfant.class, new Object[]{pkLaborrec});
            if (list == null || list.size() <= 0) return;
            for (PvInfant pv : list) {
                pvInfantPubService.deleteInfantByPk(pv.getPkInfant(), pv.getPkPv(), pv.getPkPvInfant(), pv.getPkPiInfant());
                //pvInfantPubService.deleteInfAndRecByPk(pv.getPkInfant(),pv.getPkPv(),pv.getPkPvInfant(),pv.getPkPiInfant(),null);
            }
        }
        if (!CommonUtils.isEmptyString(pkLaborrecdt)) {
            PvInfant infant = DataBaseHelper.queryForBean("select pk_infant,pk_pv_infant,pk_pv,pk_pi_infant from pv_infant where pk_laborrecdt = ? ", PvInfant.class, new Object[]{pkLaborrecdt});
            if (infant == null) return;
            pvInfantPubService.deleteInfantByPk(infant.getPkInfant(), infant.getPkPv(), infant.getPkPvInfant(), infant.getPkPiInfant());
            //pvInfantPubService.deleteInfAndRecByPk(infant.getPkInfant(),infant.getPkPv(),infant.getPkPvInfant(),infant.getPkPiInfant(),null);
        }
    }

    /**
     * 根据分娩明细生成婴儿信息
     *
     * @param dt
     * @return
     */
    private PvInfantVo generateInfant(PvLaborRecDt dt, List<PvInfantVo> inflist, boolean flagNew) {
        PvInfantVo inf = new PvInfantVo();
        if (inflist != null && inflist.size() > 0) {
            ApplicationUtils.copyProperties(inf, inflist.get(0));
        }
        //新增时设置如下信息
        if (flagNew) {
            inf.setPkLaborrec(dt.getPkLaborrec());
            inf.setPkLaborrecdt(dt.getPkLaborrecdt());
            inf.setCode(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
            inf.setPkPv(dt.getPkPv());
            inf.setSortNo(dt.getSortNo());//序号
        }
        //根据母亲就诊主键获取母亲姓名
        Map<String, Object> name_mother = DataBaseHelper.queryForMap("select name_pi from pv_encounter where pk_pv = ? ", new Object[]{dt.getPkPv()});
        String str_name = "婴";
        if (name_mother != null) str_name = CommonUtils.getString(name_mother.get("namePi"));
//		Map<String,Object> sex = DataBaseHelper.queryForMap("select name from BD_DEFDOC where CODE_DEFDOCLIST ='000000' and code = ? ", new Object[]{dt.getDtSexInf()});
//		String str_sex = "未知";
//		if(sex!=null)str_sex = CommonUtils.getString(sex.get("name"));
//		if("男".equals(str_sex)) str_sex = "子";
//		inf.setName(str_name+"之"+str_sex);//默认姓名为母亲名字+“之”+性别
        inf.setName(str_name + "B" + dt.getSortNo());//默认姓名为母亲名字+“B”+序号
        inf.setDateBirth(dt.getDateOut());//出生日期
        inf.setDtSex(dt.getDtSexInf());//性别
        inf.setLen(dt.getInfLen());//身长
        inf.setWeight(dt.getInfWight());//体重
        return inf;
    }


    /**
     * 根据选中床位主键获取对应的产程记录
     *
     * @param param
     * @param user
     */
    public String quePvInfantByPkBed(String param, IUser user) {
        String pkBed = JsonUtil.getFieldValue(param, "pkBed");
        String pkPv = "";
        String queSql = " select pit.* from PV_INFANT pit" +
                " left join PV_ENCOUNTER pv on pv.pk_pv = pit.PK_PV_INFANT" +
                " left join pv_ip  pi on pi.PK_PV = pit.PK_PV_INFANT" +
                " left join BD_RES_BED bed on bed.PK_BED = pi.PK_BED_AN" +
                " where bed.PK_BED = ? and bed.FLAG_OCUPY = '0'" +
                " and bed.FLAG_ACTIVE = '1' and pv.EU_STATUS = '1'";
        if (pkBed != null && pkBed != "") {
            PvInfant pvInfant = DataBaseHelper.queryForBean(queSql, PvInfant.class, pkBed);
            if (pvInfant != null && pvInfant.getPkPv() != null && pvInfant.getPkPv() != "") {
                pkPv = pvInfant.getPkPv();
            }
        }
        return pkPv;
    }


    /**
     * 根据患者主键获取本病区患者婴儿的陪护床位主键(产科)
     *
     * @param param
     * @param user
     */
    public Map<String, Object> quePkBedAnByPkPv(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        String pkDept = JsonUtil.getFieldValue(param, "pkDept");
        Map<String, Object> result = new HashMap<>();
        String pkBedAn = "";
        String pkBedType = "";
        //产妇入科时根据pvpv获取婴儿陪护床位主键
        String queSql = " select bed.pk_ward,pi.* " +
                " from   pv_ip pi " +
                " inner  join PV_INFANT inf on pi.PK_PV = inf.PK_PV_INFANT " +
                " inner  join bd_res_bed bed on bed.pk_bed = pi.pk_bed_an " +
                " where  inf.pk_pv = ? ";
        if (pkPv != null && pkPv != "" && pkDept != null && pkDept != "") {
            List<PvIpVo> pkIpList = DataBaseHelper.queryForList(queSql, PvIpVo.class, pkPv);
            if (pkIpList.size() > 0) {
                PvIpVo pvip = pkIpList.get(0);
                if (StringUtils.isNotBlank(pvip.getPkWard()) && StringUtils.isNotBlank(pvip.getPkBedAn()) && pkDept.equals(pvip.getPkWard())) {
                    pkBedAn = pvip.getPkBedAn();
                    pkBedType = "0";
                }
            }
        }
        //婴儿入科时母亲不在本病区，根据pkpv查询母亲是否有其他婴儿在本病区有陪护床位
        if ("".equals(pkBedAn)) {
            queSql = " select bed.pk_ward,ip.* " +
                    " from   pv_ip ip " +
                    " inner  join PV_INFANT inf on inf.PK_PV_INFANT = ip.pk_pv " +
                    " inner  join (select * from PV_INFANT where PK_PV_INFANT = ?) oth on oth.pk_pv = inf.pk_pv" +
                    " inner  join bd_res_bed bed on bed.pk_bed = ip.pk_bed_an " +
                    " where  inf.PK_PV_INFANT != ?";
            List<PvIpVo> pkIpList = DataBaseHelper.queryForList(queSql, PvIpVo.class, pkPv, pkPv);
            if (pkIpList.size() > 0) {
                PvIpVo pvip = pkIpList.get(0);
                if (StringUtils.isNotBlank(pvip.getPkWard()) && StringUtils.isNotBlank(pvip.getPkBedAn()) && pkDept.equals(pvip.getPkWard())) {
                    pkBedAn = pvip.getPkBedAn();
                    pkBedType = "0";
                }
            }
        }
        //婴儿入科时母亲在本病区，根据pkpv查询母亲的床位自动生成婴儿床位
        if ("".equals(pkBedAn)) {
            queSql = " select bed.* " +
                    " from bd_res_bed bed " +
                    " inner join PV_BED pv on pv.PK_BED_WARD = bed.pk_ward and pv.bedno = bed.code " +
                    " inner join PV_INFANT inf on inf.pk_pv = pv.pk_pv " +
                    " where inf.PK_PV_INFANT = ? and bed.pk_ward = ? and pv.date_end is null ";
            BdResBed bdResBed = DataBaseHelper.queryForBean(queSql, BdResBed.class, pkPv, pkDept);
            if (bdResBed != null) {
                pkBedAn = bdResBed.getPkBed();
                pkBedType = "1";
            }
        }
        result.put("pkBedAn", pkBedAn);
        result.put("pkBedType", pkBedType);
        return result;
    }
}
