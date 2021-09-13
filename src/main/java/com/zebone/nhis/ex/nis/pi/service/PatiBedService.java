package com.zebone.nhis.ex.nis.pi.service;

import com.zebone.nhis.base.pub.service.BdResPubService;
import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.module.pv.PvBed;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.pi.dao.PvInfantMapper;
import com.zebone.nhis.ex.nis.pi.vo.NsDeptPatiAmountVo;
import com.zebone.nhis.ex.nis.pub.dao.PatiBedMapper;
import com.zebone.nhis.ex.pub.service.ExPubService;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.support.PatiFeeHandler;
import com.zebone.nhis.ex.pub.vo.PatiCardVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DBHelper;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 床位卡服务（含床位卡列表，换床，包床）
 *
 * @author yangxue
 */
@Service
public class PatiBedService {
    @Resource
    private PatiBedMapper patiBedMapper;
    @Resource
    private PatiFeeHandler patiFeeHandler;
    @Resource
    private NsDeptPatiAmountService amountService;
    @Resource
    private ExPubService exPubService;
    @Resource
    private PvInfantMapper pvInfantMapper;
    @Resource
    private PvStaffManagerService pvStaffManagerService;
    @Resource
    private BdResPubService bdResPubService;
    @Resource
    private IpCgPubService ipCgPubService;

    /**
     * 查询病区床位及患者列表
     *
     * @param param{pkOrg,pkDeptNs}
     * @param user
     * @return
     */
    public List<PatiCardVo> queryPatiBedList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (Application.isSqlServer()) {
            paramMap.put("dbType", "sqlserver");
        } else {
            paramMap.put("dbType", "oracle");
        }
        //是否查询婴儿空床位列表
        paramMap.put("babyBed", ApplicationUtils.getSysparam("BD0013", false));

        //床位卡底部图标显示 -- 根据系统参数EX0068中是否配置了4:手术标志,5:引流管标志内容进行查询，未配置则不查询这两项内容。
        String bedCardIcon = ApplicationUtils.getSysparam("EX0068", false);
        if (bedCardIcon != null) {
            String[] iconArr = bedCardIcon.split(",");
            if (iconArr != null && iconArr.length > 0) {
                for (String icon : iconArr) {
                    if ("4".equals(icon)) {
                        //手术
                        paramMap.put("operation", icon);
                    } else if ("5".equals(icon)) {
                        //引流管
                        paramMap.put("drain", icon);
//                        paramMap.put("nowDate", DateUtils.dateToStr("yyyyMMddHHmmss", new Date()));
                    }
                }
            }
        }
        List<PatiCardVo> cardlist = patiBedMapper.getBedInfo(paramMap);

        //==================== 是否手术、是否引流管标识子查询拆分 = start ====================
        List<String> listPkPv = cardlist.stream().map(PatiCardVo::getPkPv).collect(Collectors.toList());
        List<Map<String, Object>> listChild = listPkPv.size() > 0 ? patiBedMapper.getBedInfoChildSearch(listPkPv) : null;
        List<Map<String, Object>> listOperation = null;
        List<Map<String, Object>> listDrain = null;


        //是否手术
        //op.eu_status < '5'
        if ("4".equals(paramMap.get("operation")) && listChild != null) {
            listOperation = listChild.stream().filter(map -> map.get("eu_status") != null && Integer.parseInt(map.get("eu_status").toString()) < 5)
                    .collect(Collectors.toList());
        }

        //是否引流管
        //ord.code_ordtype='0503'
        //and ord.eu_always='0'
        //and  (ord.flag_stop_chk='0' or ord.date_stop < dbo.to_date('20210427165231', 'YYYYMMDDHH24MISS'))
        if ("5".equals(paramMap.get("drain")) && listChild != null) {
            Calendar nowDate = Calendar.getInstance();
            listDrain = listChild.stream().filter(map -> map.get("code_ordtype") != null && map.get("code_ordtype").toString().equals("0503")
                    && map.get("ord.eu_always") != null && map.get("ord.eu_always").toString().equals("0")
                    && (map.get("flag_stop_chk") != null && map.get("flag_stop_chk").toString().equals("0") || nowDate.after((Date) map.get("date_stop"))))
                    .collect(Collectors.toList());
        }

        for (PatiCardVo vo : cardlist) {
            if ("4".equals(paramMap.get("operation"))) {
                if (listOperation == null) vo.setIsOperation("0");
                else {
                    List<Map<String, Object>> list = listOperation.stream()
                            .filter(map -> map.get("pk_pv").toString().equals(vo.getPkPv())).collect(Collectors.toList());
                    vo.setIsOperation(list.size() > 0 ? "1" : "0");
                }
            }

            if ("5".equals(paramMap.get("drain"))) {
                if(listDrain == null) vo.setIsDrain("0");
                else{
                    List<Map<String, Object>> list = listDrain.stream()
                            .filter(map -> map.get("pk_pv").toString().equals(vo.getPkPv())).collect(Collectors.toList());
                    vo.setIsDrain(list.size() > 0 ? "1" : "0");
                }
            }
        }
        //==================== 是否手术、是否引流管标识子查询拆分 = end ====================

        return cardlist;
    }

    /**
     * 选中床位卡，加载患者详细信息
     *
     * @param param{cardvo}
     * @param user
     * @return
     */
    public PatiCardVo queryPatiCardInfo(String param, IUser user) {
        PatiCardVo cardvo = JsonUtil.readValue(param, PatiCardVo.class);
        if (cardvo == null) return null;
        PatiCardVo newCard = patiFeeHandler.setPatiFee(cardvo);
        return newCard;
    }

    /**
     * 查询患者在途费用明细
     *
     * @param param{pkPv}
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryZtFeeListByPv(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        return patiBedMapper.getZtFeeDetailByPv(paramMap);
    }

    /**
     * 查询患者固定费用明细
     *
     * @param param{pkPv}
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryGdFeeListByPv(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        return patiBedMapper.getGdFeeDetailByPv(paramMap);
    }

    /**
     * 查询床位大卡统计信息栏内对应的各类患者人数
     *
     * @param param{pkDeptNs}
     * @param user
     * @return
     */
    public NsDeptPatiAmountVo queryNsDeptPatiAmount(String param, IUser user) {
        return amountService.queryNsDeptPatiAmount(param, user);
    }

    /**
     * 换床
     *
     * @param param{bednoSrc,bednoDes,pkPv,pkDeptNs,pkPi,pkDept,pkBedDes,pkBedSrc,pkPvDes,pkPiDes}
     * @param user
     * @return
     */
    public PatiCardVo exChangeBed(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || "".equals(CommonUtils.getString(paramMap.get("pkPv"))) || "".equals(CommonUtils.getString(paramMap.get("bednoDes"))) || "".equals(CommonUtils.getString(paramMap.get("bednoSrc")))) {
            throw new BusException("换床患者或目标床位号为空，请确认后重新操作！");
        }
        User u = (User) user;
        String bedno_des = CommonUtils.getString(paramMap.get("bednoDes"));
        String pk_dept_ns = CommonUtils.getString(paramMap.get("pkDeptNs"));
        String pkPi = CommonUtils.getString(paramMap.get("pkPi"));
        String pkPiDes = CommonUtils.getString(paramMap.get("pkPiDes"));
        //床位模式-0手动分床模式 1-自动分成床模式
        String BD0013 = ApplicationUtils.getSysparam("BD0013", false);
        String pk_org = u.getPkOrg();
        //换床:更新原床位及就诊记录
        paramMap.put("dateEnd", new Date());
        paramMap.put("pkOrg", pk_org);
        paramMap.put("pkEmp", u.getPkEmp());
        paramMap.put("nameEmp", u.getNameEmp());
        DataBaseHelper.update("update pv_bed set date_end =:dateEnd,pk_emp_end =:pkEmp,name_emp_end =:nameEmp where pk_dept =:pkDept and pk_dept_ns =:pkDeptNs and bedno =:bednoSrc and pk_pv =:pkPv and date_end is null", paramMap);
        DataBaseHelper.update("update pv_encounter set bed_no = :bednoDes where pk_pv = :pkPv", paramMap);
        Map<String, Object> flagOcupy = DataBaseHelper.queryForMap("select flag_ocupy from bd_res_bed where code = ? and pk_org = ? and pk_ward = ? ", new Object[]{bedno_des, pk_org, pk_dept_ns});

        String sql = "";
        //手动分床模式不处理婴儿床删除标志
        if (!"0".equals(BD0013)) {
            sql = " ,flag_active = (case when '09' = dt_bedtype then '1' else flag_active end) ,del_flag = (case when '09' = dt_bedtype then '0' else del_flag end) ";
        }
        int cnt = DataBaseHelper.update("update bd_res_bed set flag_ocupy = '1',pk_pi = :pkPi,pk_dept_used = :pkDeptNs,eu_status='02'"
                + sql
                + " where code = :bednoDes and pk_org =:pkOrg and pk_ward=:pkDeptNs and flag_active='1'"
                + (CommonUtils.isEmptyString(pkPiDes) ? " and pk_pi is null" : " and pk_pi = '" + pkPiDes + "'"), paramMap);
        if (cnt < 1)
            throw new BusException("目标床位信息已经发生变化，请先刷新！");

        //校验目标床位是否为空
        if (flagOcupy != null && "1".equals(CommonUtils.getString(flagOcupy.get("flagOcupy")))) {
            //被占用的情况下换床
            DataBaseHelper.update("update pv_bed set date_end =:dateEnd,pk_emp_end =:pkEmp,name_emp_end =:nameEmp where pk_dept =:pkDept and pk_dept_ns =:pkDeptNs and bedno =:bednoDes and pk_pv =:pkPvDes and date_end is null", paramMap);
            DataBaseHelper.update("update pv_encounter set bed_no = :bednoSrc where pk_pv = :pkPvDes", paramMap);

            cnt = DataBaseHelper.update("update bd_res_bed set flag_ocupy = '1',pk_pi = :pkPiDes,pk_dept_used = :pkDeptNs,eu_status='02'"
                    + sql
                    + " where code = :bednoSrc and pk_org =:pkOrg and pk_ward=:pkDeptNs"
                    + (CommonUtils.isEmptyString(pkPi) ? " and pk_pi is null" : " and pk_pi = '" + pkPi + "'"), paramMap);//互换|换空床皆可
            //针对换床只能换空床的操作
            //DataBaseHelper.update("update bd_res_bed set flag_ocupy = '1',pk_pi = :pkPiDes,pk_dept_used = :pkDeptNs,eu_status='02',flag_active = (case when '09' = dt_bedtype then '1' else flag_active end),del_flag = (case when '09' = dt_bedtype then '0' else del_flag end) where code = :bednoSrc and pk_org =:pkOrg and pk_ward=:pkDeptNs and flag_ocupy ='0'", paramMap);
            //插入新的源床位记录
            saveSrcBed(paramMap, u);
        } else {
            //未被占用的情况下换床
            cnt = DataBaseHelper.update("update bd_res_bed set flag_ocupy = '0',pk_pi = null,pk_dept_used = null,eu_status='01'"
                    + sql
                    + " where code = :bednoSrc and pk_org =:pkOrg and pk_ward=:pkDeptNs"
                    + (CommonUtils.isEmptyString(pkPi) ? " and pk_pi is null" : " and pk_pi = '" + pkPi + "'"), paramMap);
        }
        if (cnt < 1)
            throw new BusException("源床位信息已经发生变化，请先刷新！");

        //插入新目标床位记录
        saveBed(paramMap, u);

        //获取新的床位卡信息
//		List<PatiCardVo> cardlist = patiBedMapper.getBedInfo(paramMap);
//		if(cardlist!=null&&cardlist.size()>0){
//			return cardlist.get(0);
//		}

        //若存在婴儿的情况，母亲换床，同时修改婴儿床位编号，只判断EX0025参数中配置的科室，未配置的不考虑
        boolean isHaveInf = ExSysParamUtil.getFlagAddInfByDeptNs(pk_dept_ns, u.getPkOrg());
        if (isHaveInf && !"0".equals(BD0013)) {
            String bedSpec = ExSysParamUtil.getSpcOfCodeBed();
            if (CommonUtils.isEmptyString(bedSpec))
                throw new BusException("请维护系统参数【BD0007】婴儿的床位分隔符！");
            List<Map<String, Object>> inflist = hasInfantList(CommonUtils.getString(paramMap.get("pkPv")), pk_dept_ns);
            if (inflist.size() > 0) {
                //2019-07-06 原逻辑：校验母亲的目标床位是否配有婴儿床，且床位数量是否够婴儿数量，不够提示先维护婴儿床位
//				List<BdResBed> bedlist = DataBaseHelper.queryForList("select code from bd_res_bed where pk_ward = ? and flag_ocupy='0' and dt_bedtype = '09' and eu_status='01' and code like '"+bedno_des+"%' order by code ", BdResBed.class, new Object[]{pk_dept_ns});
//				if(bedlist==null||bedlist.size()<inflist.size()){
//					throw new BusException("该患者在当前病区有"+inflist.size()+"个婴儿,\n但所换床位未维护相应数量的空闲婴儿床，无法完成换床操作，\n请先在床位管理中维护对应的婴儿床位！");
//				}
                //2019-07-06 现逻辑：若当前床位不存在则自动补全，然后进行换床操作
                String bedNoList = "";
                List<BdResBed> bedNolist = new ArrayList<BdResBed>();

                for (Map<String, Object> map : inflist) {
                    BdResBed bedInf = new BdResBed();
                    bedNoList = bedNoList + (bedno_des + bedSpec + CommonUtils.getString(map.get("sortNo")) + ",");
                    bedInf.setCode(bedno_des + bedSpec + CommonUtils.getString(map.get("sortNo")));
                    bedNolist.add(bedInf);
                }
                if (!CommonUtils.isEmptyString(bedNoList))
                    bedNoList = bedNoList.substring(0, bedNoList.length() - 1);
                BdResBed bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where code=? and pk_ward =? ", BdResBed.class, new Object[]{bedno_des, pk_dept_ns});
                if (null == bedMa)
                    throw new BusException("未获取到床位【" + bedno_des + "】！");
                List<BdResBed> bedlist = bdResPubService.isHaveBedAndAdd(pk_dept_ns, bedMa, null, bedNoList, u);
                if (null == bedlist || bedlist.size() < 1)
                    updateInfantBed(inflist, bedNolist, pk_dept_ns);//更新床位
                else
                    updateInfantBed(inflist, bedlist, pk_dept_ns);//更新床位
            }
        }


        //2019-02-25 更新责任护士
        Map<String, Object> staMap = new HashMap<String, Object>();
        if (!CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkEmpNsSrc")))) {
            staMap.put("pkPv", CommonUtils.getString(paramMap.get("pkPv")));
            staMap.put("pkDept", CommonUtils.getString(paramMap.get("pkDept")));
            staMap.put("pkEmp", CommonUtils.getString(paramMap.get("pkEmpNsSrc")));
            staMap.put("nameEmp", CommonUtils.getString(paramMap.get("nameEmpNsSrc")));
            pvStaffManagerService.saveChangedStaff(staMap, u);
        }
        if (!CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkEmpNsDes")))) {
            staMap.put("pkPv", CommonUtils.getString(paramMap.get("pkPvDes")));
            staMap.put("pkDept", CommonUtils.getString(paramMap.get("pkDeptDes")));
            staMap.put("pkEmp", CommonUtils.getString(paramMap.get("pkEmpNsDes")));
            staMap.put("nameEmp", CommonUtils.getString(paramMap.get("nameEmpNsDes")));
            pvStaffManagerService.saveChangedStaff(staMap, u);
        }

        //发送换床消息至集成平台
        paramMap.put("bedOld", CommonUtils.getString(paramMap.get("bednoSrc")));
        paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
        paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        PlatFormSendUtils.sendBedChangeMsg(paramMap);

        return null;
    }


    /**
     * 判断当前就诊患者在当前病区是否有婴儿
     *
     * @param pkPv
     * @return
     */
    private List<Map<String, Object>> hasInfantList(String pkPv, String pkDeptNs) {
        return pvInfantMapper.getInfantListByMother(pkPv, pkDeptNs);
    }

    private void updateInfantBed(List<Map<String, Object>> inflist, List<BdResBed> bedlist, String pk_dept_ns) {
        for (int i = 0; i < inflist.size(); i++) {
            Map<String, Object> tempMap = inflist.get(i);
            tempMap.put("pkdeptns", pk_dept_ns);
            tempMap.put("codebed", bedlist.get(i).getCode());
            String update_pv = "update pv_encounter set bed_no = :codebed where pk_pv = :pkPv";
            DataBaseHelper.update(update_pv, tempMap);
            String update_pv_bed = "update pv_bed set bedno = :codebed where pk_pv=:pkPv and pk_dept_ns = :pkdeptns";
            DataBaseHelper.update(update_pv_bed, tempMap);
            String update_bed_des = "update bd_res_bed set pk_pi =:pkPi ,flag_ocupy='1',eu_status='02',code_fa=:codeFa"
                    + " ,flag_active = (case when '09' = dt_bedtype then '1' else flag_active end)"
                    + " ,del_flag = (case when '09' = dt_bedtype then '0' else del_flag end) where code=:codebed and pk_ward=:pkdeptns";
            DataBaseHelper.update(update_bed_des, tempMap);
            String update_bed_src = "update bd_res_bed set pk_pi = null,flag_ocupy='0',eu_status='01'"
                    + " ,flag_active = (case when '09' = dt_bedtype then '0' else flag_active end)"
                    + " ,del_flag = (case when '09' = dt_bedtype then '1' else del_flag end)  where code=:bedNo and pk_ward=:pkdeptns";
            DataBaseHelper.update(update_bed_src, tempMap);
        }
    }

    /**
     * 包床
     *
     * @param param{pkPv,pkDeptNs,pkPi,pkDept,List<BdResBed> bedlist}
     * @param user
     * @return
     */
    public List<PatiCardVo> packBed(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || "".equals(CommonUtils.getString(paramMap.get("pkPv")))) {
            throw new BusException("包床患者就诊主键为空，请确认后重新操作！");
        }
        //获取包床列表
        List<Map<String, Object>> bedlist = (List<Map<String, Object>>) paramMap.get("bedlist");
        if (bedlist == null || bedlist.size() <= 0) {
            throw new BusException("所包床位列表为空，请确认后重新操作！");
        }

        //插入新的床位记录
        User u = (User) user;
        paramMap.put("dateBegin",new Date());
        insertPvBed(paramMap, bedlist, u);
        //更新床位字典
        updateBedInfo(paramMap, bedlist);
        //获取新的床位卡信息
        paramMap.put("pkOrg", u.getPkOrg());
        List<PatiCardVo> cardlist = patiBedMapper.getBedInfo(paramMap);

        paramMap.put("pkEmp", u.getPkEmp());
        paramMap.put("codeEmp", u.getCodeEmp());
        paramMap.put("nameEmp", u.getNameEmp());
        //获取包床列表
        List<String> beds = new ArrayList<String>();
        for (Map<String, Object> bed : bedlist) {
            beds.add(bed.get("code").toString());
        }
        paramMap.put("bednos", beds);
        //发送包床信息至平台
        PlatFormSendUtils.sendBedPackMsg(paramMap);

        return cardlist;
    }

    /**
     * 退包床
     *
     * @param param{pkPv,bednos}
     * @param user
     */
    public void saveRtnBed(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
//		String pk_pv = CommonUtils.getString(paramMap.get("pkPv"));
//		List<String> bednos = (List)paramMap.get("bednos");
        paramMap.put("dateEnd", new Date());
        User u = (User) user;
        paramMap.put("pkEmp", u.getPkEmp());
        paramMap.put("nameEmp", u.getNameEmp());
        paramMap.put("pkDeptNs", u.getPkDept());
        String update_pv = "update pv_bed set date_end = :dateEnd,pk_emp_end = :pkEmp,name_emp_end = :nameEmp where bedno in (:bednos) and pk_pv = :pkPv ";
        //更新就诊床位
        DataBaseHelper.update(update_pv, paramMap);
        //更新床位
        String update_bed = "update bd_res_bed set pk_pi = null,flag_ocupy='0',eu_status='01',pk_dept_used=null where code in (:bednos) and pk_ward = :pkDeptNs";
        DataBaseHelper.update(update_bed, paramMap);

        //发送包床信息至平台
        paramMap.put("pkEmp", u.getPkEmp());
        paramMap.put("codeEmp", u.getCodeEmp());
        paramMap.put("nameEmp", u.getNameEmp());
        PlatFormSendUtils.sendBedRtnPackMsg(paramMap);
    }

    /**
     * 查询包床或退包床 床位列表
     *
     * @param param{type:1,包床，2：退包床；pkPv;pkDeptNs;pkPi}
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryPackBedList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        return patiBedMapper.queryPvPackBed(paramMap);
    }

    /**
     * 插入床位记录
     *
     * @param paramMap
     * @param user
     */
    private void saveSrcBed(Map<String, Object> paramMap, User user) {
        PvBed bed = new PvBed();
        bed.setBedno(CommonUtils.getString(paramMap.get("bednoSrc")));
        bed.setDateBegin(new Date());
        bed.setFlagMaj("1");
        bed.setNameEmpBegin(user.getNameEmp());
        bed.setPkEmpBegin(user.getPkEmp());
        BdResBed bedvo = exPubService.getBedInfoByPk(CommonUtils.getString(paramMap.get("pkBedSrc")), user);
        if (bedvo != null) {
            bed.setPkBedWard(bedvo.getPkWard());
        }
        bed.setPkPv(CommonUtils.getString(paramMap.get("pkPvDes")));
        bed.setPkDept(CommonUtils.getString(paramMap.get("pkDept")));
        bed.setPkDeptNs(CommonUtils.getString(paramMap.get("pkDeptNs")));
        bed.setEuHold("0");
        DataBaseHelper.insertBean(bed);
    }

    /**
     * 插入源床位就诊记录
     *
     * @param paramMap
     * @param user
     */
    private void saveBed(Map<String, Object> paramMap, User user) {
        PvBed bed = new PvBed();
        bed.setBedno(CommonUtils.getString(paramMap.get("bednoDes")));
        bed.setDateBegin(new Date());
        bed.setFlagMaj("1");
        bed.setNameEmpBegin(user.getNameEmp());
        bed.setPkEmpBegin(user.getPkEmp());
        BdResBed bedvo = exPubService.getBedInfoByPk(CommonUtils.getString(paramMap.get("pkBedDes")), user);
        if (bedvo != null) {
            bed.setPkBedWard(bedvo.getPkWard());
        }
        bed.setPkPv(CommonUtils.getString(paramMap.get("pkPv")));
        bed.setPkDept(CommonUtils.getString(paramMap.get("pkDept")));
        bed.setPkDeptNs(CommonUtils.getString(paramMap.get("pkDeptNs")));
        bed.setEuHold("0");
        DataBaseHelper.insertBean(bed);
    }

    /**
     * 批量插入包床信息
     *
     * @param paramMap
     * @param bedlist
     * @param u
     */
    private void insertPvBed(Map<String, Object> paramMap, List<Map<String, Object>> bedlist, User u) {
        List<PvBed> pvbedlist = new ArrayList<PvBed>();
        List<String> bednos = new ArrayList<String>();
        String pkWard = "";
        for (Map<String, Object> bedvo : bedlist) {
            PvBed bed = new PvBed();
            bed.setBedno(CommonUtils.getString(bedvo.get("code")));
            bednos.add(CommonUtils.getString(bedvo.get("code")));
            bed.setDateBegin((Date) MapUtils.getObject(paramMap,"dateBegin"));
            bed.setFlagMaj("0");
            bed.setNameEmpBegin(u.getNameEmp());
            bed.setPkEmpBegin(u.getPkEmp());
            bed.setPkBedWard(CommonUtils.getString(bedvo.get("pkWard")));
            pkWard = bed.getPkBedWard();
            bed.setPkPv(CommonUtils.getString(paramMap.get("pkPv")));
            bed.setPkDept(CommonUtils.getString(paramMap.get("pkDept")));
            bed.setPkDeptNs(CommonUtils.getString(paramMap.get("pkDeptNs")));
            bed.setEuHold("1");
            bed.setCreateTime(new Date());
            bed.setCreator(u.getPkEmp());
            bed.setTs(new Date());
            bed.setPkOrg(CommonUtils.getString(bedvo.get("pkOrg")));
            bed.setPkPvbed(NHISUUID.getKeyId());
            pvbedlist.add(bed);
        }
        //校验床位是否存在对应的附加项目
        boolean flagAdd = "1".equals(ApplicationUtils.getSysparam("BL0054", false)) ? true : false;
        if (flagAdd) {
            Map<String, Object> bedMap = new HashMap<String, Object>();
            bedMap.put("pkWard", pkWard);
            bedMap.put("bednos", bednos);
            //主表包床费用
            List<Map<String, Object>> beds = patiBedMapper.queryBedContainsAdditem(bedMap);
            //查询子表包床
            List<Map<String, Object>> itemBeds = patiBedMapper.queryItemBedContainsAdditem(bedMap);
            if ((beds == null || beds.size() < bedlist.size()) && (itemBeds == null || itemBeds.size() < bedlist.size())) {
                throw new BusException("使用系统参数[BL0054=1]模式下，所包床位必须维护附加收费项目!");
            }

        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvBed.class), pvbedlist);
    }

    /**
     * 批量更新床位字典的床位状态等信息
     *
     * @param paramMap
     * @param bedlist
     */
    private void updateBedInfo(Map<String, Object> paramMap, List<Map<String, Object>> bedlist) {
        List<String> pk_bed_str = new ArrayList<String>();

        //判断患者是否在院
        int count = DataBaseHelper.queryForScalar("select count(pk_pv) from pv_encounter where pk_pv = ?  and eu_pvtype = '3'  and eu_status ='1' ", Integer.class, paramMap.get("pkPv"));
        if (count < 1) {
            throw new BusException("数据已变化，请刷新后重试！");
        }
        for (Map<String, Object> bedvo : bedlist) {
            pk_bed_str.add(CommonUtils.getString(bedvo.get("code")));
        }
        String update_Sql = "update bd_res_bed set flag_ocupy='1',pk_pi = :pkPi,eu_status='02',pk_dept_used=:pkDeptNs  where code in (:codes) and pk_ward=:pkDeptNs and flag_ocupy='0' and pk_pi is null";
        paramMap.put("codes", pk_bed_str);
        int cnt = DataBaseHelper.update(update_Sql, paramMap);
        if (cnt <= 0 || cnt < pk_bed_str.size())
            throw new BusException("包床失败，" + pk_bed_str + " 床位已经被占用，请刷新后重试！");
    }

    /**
     * 2019-02-23 孙逸仙新增需求 - 更新患者分类
     *
     * @param param
     * @param user
     */
    public void updatePicateByPv(String param, IUser user) {
        PatiCardVo card = JsonUtil.readValue(param, PatiCardVo.class);
        if (null == card)
            throw new BusException("未获取到待更新患者分类的入参！");
        //查询患者就诊记录
        String querySql = "select * from PV_ENCOUNTER where pk_pv = ?";
        PvEncounter pvEncounter = DataBaseHelper.queryForBean(querySql, PvEncounter.class, card.getPkPv());
        User u = (User) user;
        String sql = "UPDATE PV_ENCOUNTER SET MODIFIER='" + u.getNameEmp()
                + "',MODITY_TIME=to_date('" + DateUtils.getDateTime() + "','YYYY-MM-DD HH24:MI:SS') "
                + " ,FLAG_SPEC = (SELECT FLAG_SPEC FROM PI_CATE WHERE PK_PICATE = ? AND DEL_FLAG='0')"
                + " ,PK_PICATE=? WHERE PK_PV=? AND FLAG_IN = '1' ";
        if (null == card.getPkPicate() || CommonUtils.isEmptyString(card.getPkPicate()))
            sql += "AND PK_PICATE IS NULL ";
        else
            sql += "AND PK_PICATE='" + card.getPkPicate() + "'";
        int cnt = DataBaseHelper.update(sql, new Object[]{card.getPkPicateNew(), card.getPkPicateNew(), card.getPkPv()});
        if (cnt != 1)
            throw new BusException("床位【" + card.getBedno() + "】患者" + card.getNamePi() + " 修改患者分类失败！");

        //根据患者分类更新患者费用明细
        String isUpdateValue = ApplicationUtils.getSysparam("PV0018", false);
        if ("1".equals(isUpdateValue) && pvEncounter != null) {
            ipCgPubService.updateBlIpDtCgRate(pvEncounter.getPkPv(), pvEncounter.getPkInsu(), card.getPkPicateNew(),
                    pvEncounter.getPkInsu(), pvEncounter.getPkPicate());
        }
    }

    /**
     * 查询床位责任护士
     *
     * @param param{pk_bed ： 床位主键}
     * @param user
     * @return
     */
    public List<Map<String, Object>> getEmpNsByBed(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = patiBedMapper.getEmpNsByBed(paramMap);
        return list;
    }

    /**
     * 查询患者是否被住院处冻结
     * 005002001062
     *
     * @param param
     * @param user
     */
    public void searchPiFrozen(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        if (StringUtils.isBlank(pkPv)) {
            return;
        }
        Integer integer = DataBaseHelper.queryForScalar("select count(1) from pv_ip where pk_pv=? and flag_frozen='1'", Integer.class, pkPv);
        if (integer != null && integer > 0) {
            throw new BusException("当前住院处冻结该患者，病区不可取消出院！");
        }
    }

    /**
     * 查询患者是否已有入院记录
     * 005002001063
     *
     * @param param
     * @param user
     */
    public void searchPiAmtRecord(String param, IUser user) {
        String pkPi = JsonUtil.getFieldValue(param, "pkPi");
        if (StringUtils.isBlank(pkPi)) {
            return;
        }
        List<Map<String, Object>> pvList = DataBaseHelper.queryForList("select pv.pk_pv,pv.pk_dept_ns,dept.name_dept from pv_encounter pv left join bd_ou_dept dept " +
                        " on pv.pk_dept_ns = dept.pk_dept where pv.pk_pi = ? and pv.eu_status='1' and pv.eu_pvtype = '3' "
                , new Object[]{pkPi});
        if (pvList != null && pvList.size() > 0) {
            throw new BusException("当前患者在'" + pvList.get(0).get("nameDept") + "'存未出院记录，不允许重复入院！");
        }
    }

    /**
     * 查询类型为0303的打印模板
     */
    public List<Map<String, Object>> getPrintList(String param, IUser user) {
        return patiBedMapper.getPrintList();
    }
}
