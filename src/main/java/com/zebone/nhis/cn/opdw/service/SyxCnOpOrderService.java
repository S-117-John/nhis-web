package com.zebone.nhis.cn.opdw.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.cn.ipdw.vo.SaveDiagParam;
import com.zebone.nhis.cn.opdw.dao.SyxCnOpOrderMapper;
import com.zebone.nhis.cn.opdw.vo.PdZeroVo;
import com.zebone.nhis.cn.opdw.vo.SyxBlOpDt;
import com.zebone.nhis.cn.opdw.vo.SyxCnOrderVo;
import com.zebone.nhis.cn.opdw.vo.SyxOpOrderVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnDiag;
import com.zebone.nhis.common.module.cn.ipdw.CnDiagDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.pi.PatInfo;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderOutpat;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendCnMapper;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SyxCnOpOrderService {
    private Logger logger = LoggerFactory.getLogger("nhis.cnOrder");
    @Autowired
    public SyxCnOpOrderMapper opOrderDao;
    @Autowired
    private CnPubService cnPubService;

    @Autowired
    private OpCgPubService opCgPubService;
    @Autowired
    private BdSnService bdSnService;
    @Autowired
    public SyxCnOpPatiPvService syxCnOpPatiPvService;

    /***
     * ??????--????????????--??????????????????
     * @param param
     * @param user
     * @return
     */
    public List<SyxCnOrderVo> qryPdOrders(String param, IUser user) {
        //pkPv
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = paramMap.get("pkPv") != null ? paramMap.get("pkPv").toString() : "";
        if (StringUtils.isBlank(pkPv)) throw new BusException("??????pkPv?????????");
        List<SyxCnOrderVo> opOrderlist = opOrderDao.qryPdOrders(paramMap);
        if (opOrderlist != null && opOrderlist.size() > 0) {
            setOrdSupplyBl(pkPv, opOrderlist);
            //setOrdZero(pkPv,opOrderlist);
        }
        return opOrderlist;
    }

    private void setOrdSupplyBl(String pkPv, List<SyxCnOrderVo> opOrderlist) {
        List<SyxBlOpDt> dt = DataBaseHelper.queryForList("select dt.eu_additem, dt.pk_pres ,dt.pk_cnord,dt.pk_cgop,dt.pk_item,dt.name_cg as item_name,dt.quan as quan_cg,dt.price,dt.amount,dt.creator from bl_op_dt dt where  dt.pk_pv=? and dt.eu_additem='1' and dt.pk_cnord is not null  ", SyxBlOpDt.class, new Object[]{pkPv});
        if (dt != null && dt.size() > 0) {
            List<List<SyxBlOpDt>> cnOrdGroup = dividerList(dt, new Comparator<SyxBlOpDt>() {
                @Override
                public int compare(SyxBlOpDt o1, SyxBlOpDt o2) {
                    return o1.getPkCnord().equals(o2.getPkCnord()) ? 0 : -1;
                }
            });
            for (List<SyxBlOpDt> group : cnOrdGroup) {
                for (SyxCnOrderVo ord : opOrderlist) {
                    if (ord.getPkCnord().equals(group.get(0).getPkCnord())) {
                        ord.setAddItems(group);
                        break;
                    }
                }
            }
        }
        List<SyxBlOpDt> dtSupplyAdd = DataBaseHelper.queryForList("select dt.eu_additem, dt.pk_pres ,dt.pk_cnord,dt.pk_cgop,dt.pk_item,dt.name_cg as item_name,dt.quan as quan_cg,dt.price,dt.amount,dt.creator from bl_op_dt dt where  dt.pk_pv=? and dt.eu_additem='2' and dt.pk_cnord is not null  ", SyxBlOpDt.class, new Object[]{pkPv});
        if (dtSupplyAdd != null && dtSupplyAdd.size() > 0) {
            List<List<SyxBlOpDt>> cnOrdGroup = dividerList(dtSupplyAdd, new Comparator<SyxBlOpDt>() {
                @Override
                public int compare(SyxBlOpDt o1, SyxBlOpDt o2) {
                    return o1.getPkCnord().equals(o2.getPkCnord()) ? 0 : -1;
                }
            });
            for (List<SyxBlOpDt> group : cnOrdGroup) {
                for (SyxCnOrderVo ord : opOrderlist) {
                    if (ord.getPkCnord().equals(group.get(0).getPkCnord())) {
                        ord.setAddSupplyItems(group);
                        break;
                    }
                }
            }
        }
    }

    //?????????????????????????????????
    private void setOrdZero(String pkPv, List<SyxCnOrderVo> opOrderlist) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pkOrg", opOrderlist.get(0).getPkOrg());
        map.put("pkPi", opOrderlist.get(0).getPkPi());
        for (SyxCnOrderVo ord : opOrderlist) {
            map.put("pkPd", null);
            map.put("pkItem", null);
            map.put("size", 0);
            map.put("quan", 0);
            if ("1".equals(ord.getFlagDurg())) { //??????
                List<Map<String, Object>> zero = opOrderDao.qryPdZero(ord);
                if (zero != null && zero.size() > 0) {
                    if ("1".equals(MapUtils.getString(zero.get(0), "valAtt"))) {
                        map.put("pkPd", ord.getPkOrd());
                        map.put("pkItem", ord.getPkOrd());
                        map = qryZero(map);
                        ord.setFlagZero("1");
                        if (StringUtils.isNotEmpty(MapUtils.getString(map, "size")) && StringUtils.isNotEmpty(MapUtils.getString(map, "size"))) {
                            Double max = Double.parseDouble(MapUtils.getString(map, "size")) - Double.parseDouble(MapUtils.getString(map, "quan"));
                            ord.setQuanMax(max);
                            ord.setZeroMax(Double.parseDouble(MapUtils.getString(map, "size")));
                        }
                    }
                }
            }
        }
    }

    //?????????????????????????????? old:004003007006
    public List<Map<String, Object>> qryPresAddItems(String param, IUser user) {
        return null;
    }

    /**
     * ??????--????????????--??????????????????
     * @param param
     * @param user
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Map<String, Object> savePdOrders(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        SyxOpOrderVo opOrderParam = JsonUtil.readValue(param, SyxOpOrderVo.class);
        Date d = new Date();
        User u = (User) user;
        PatInfo patInfo = opOrderParam.getPatInfo();
        if (patInfo == null || StringUtils.isEmpty(patInfo.getPkPv())) {
            //throw new BusException("???????????????????????????");
            return null;
        }
        //??????????????????????????????????????????????????????
        String euLocked = syxCnOpPatiPvService.getPvEuLocked(patInfo.getPkPv());
        if ("2".equals(euLocked)) throw new BusException("???????????????????????????????????????????????????????????????????????????");
        //??????????????????????????????
        BdOuEmpjob emp = DataBaseHelper.queryForBean(
                "SELECT * FROM bd_ou_empjob WHERE pk_emp =? AND is_main = '1' ", BdOuEmpjob.class, new Object[]{u.getPkEmp()});
        String pkDeptJob = emp != null && StringUtils.isNotBlank(emp.getPkDept()) ? emp.getPkDept() : null;
        //??????????????????????????????
        String pkDeptArea = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept_area From pv_encounter where pk_pv=?", patInfo.getPkPv()), "pkDeptArea");

        List<SyxCnOrderVo> addingList = opOrderParam.getAddingList();
        List<SyxCnOrderVo> deletingList = opOrderParam.getDeletingList();
        List<SyxCnOrderVo> editingList = opOrderParam.getEditingList();
        List<BlOpDt> addItems = new ArrayList<BlOpDt>();  //???????????????????????????
        List<BlPubParamVo> blOpItem = new ArrayList<BlPubParamVo>();
        List<CnPrescription> addPresPos = new ArrayList<CnPrescription>();
        List<String> presNoList = new ArrayList<String>();
        List<SyxCnOrderVo> addCnOrdList = new ArrayList<SyxCnOrderVo>();
        List<String> delItemsByCnord = new ArrayList<String>();
        List<SyxCnOrderVo> editCnOrdList = new ArrayList<SyxCnOrderVo>();
        Set<String> pkPresList = Sets.newHashSet();
        //???????????????????????????
        checkZeroPd(opOrderParam, u);
        Map<String, String> presDtHpprop = new HashMap<>();
        //????????????
        if (addingList != null && addingList.size() > 0) {
            int rNum = 0;
            String pkPres = "";
            Map<Integer, Integer> ordNumMap = new HashMap<Integer, Integer>();
            int ordNumMin = bdSnService.getSerialNo("CN_ORDER", "ORDSN", addingList.size(), u);
            for (SyxCnOrderVo ord : addingList) {
                ordNumMap.put(ord.getOrdNum(), ordNumMin);
                ordNumMin += 1;
            }
            for (SyxCnOrderVo ord : addingList) {
                int ordRNum = ord.getrNum(); //???1??????
                if (ordRNum != rNum) {
                    rNum = ordRNum;
                    if (StringUtils.isBlank(ord.getPresNo()) && StringUtils.isBlank(ord.getPkPres())) { //????????????????????????,????????????????????????????????????pkPres??????
                        CnPrescription addPresPo = new CnPrescription();
                        BeanUtils.copyProperties(addPresPo, ord);
                        addPresPo.setPresNo(ApplicationUtils.getCode("0406"));
                        addPresPo.setDatePres(d);
                        addPresPo.setPkPres(NHISUUID.getKeyId());
                        ord.setPkPres(addPresPo.getPkPres());
                        pkPres = addPresPo.getPkPres();
                        addPresPo.setTs(d);
                        addPresPo.setDelFlag("0");
                        addPresPo.setCreateTime(new Date());
                        addPresPo.setModityTime(null);
                        addPresPo.setNote(ord.getNotePres());
                        addPresPos.add(addPresPo);
                    }
                }
                ord.setPkCnord(NHISUUID.getKeyId());
                ord.setPkPres(StringUtils.isBlank(ord.getPkPres()) ? pkPres : ord.getPkPres());
                ord.setOrdsn(ord.getOrdsn() == 0 ? ordNumMap.get(ord.getOrdNum()) : ord.getOrdsn());
                ord.setOrdsnParent(ord.getOrdsnParent() == 0 ? (ordNumMap.containsKey(ord.getOrdParentNum()) ? ordNumMap.get(ord.getOrdParentNum()) : ord.getOrdsn()) : ord.getOrdsnParent());
                ord.setOrdsnChk(ord.getOrdsn());
                ord.setEuAlways("1"); //??????
                ord.setDateEnter(d);
                ord.setDateStart(d);
                ord.setDescOrd(null);
                ord.setTs(d);
                ord.setDelFlag("0");
                ord.setFlagDoctor("1");
                ord.setCreateTime(d);
                ord.setModityTime(null);
                ord.setCreator(u.getPkEmp());
                ord.setDateEffe(getDateEffec(ord.getEuPvtype()));// ????????????
                ord.setGroupno(ord.getgNum());//???????????????
                ord.setPkDeptJob(pkDeptJob);//????????????
                ord.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                opOrdToOpCg(ord, blOpItem);
                addCnOrdList.add(ord);
                pkPresList.add(ord.getPkPres());
                if (StringUtils.isNotBlank(ord.getPkPres()) &&
                        StringUtils.isBlank(presDtHpprop.get(ord.getPkPres()))) {
                    presDtHpprop.put(ord.getPkPres(), ord.getDtHpprop());
                }
                List<SyxBlOpDt> ordAddItems = ord.getAddItems();
                if (ordAddItems != null && ordAddItems.size() > 0) {
                    for (SyxBlOpDt opDt : ordAddItems) {
                        opDt.setPkPv(ord.getPkPv());
                        opDt.setPkPi(ord.getPkPi());
                        opDt.setPkPres(ord.getPkPres());
                        opDt.setEuAdditem("1");
                        opDt.setPkCnord(ord.getPkCnord());
                        opDt.setPkOrgEx(ord.getPkOrgExec());
                        opDt.setPkDeptEx(ord.getPkDeptExec());
                        opDt.setPkOrgApp(ord.getPkOrg());
                        opDt.setPkDeptApp(ord.getPkDept());
                        opDt.setPkEmpApp(ord.getPkEmpOrd());
                        opDt.setNameEmpApp(ord.getNameEmpOrd());
                        opDt.setPkDeptJob(pkDeptJob);
                        opDt.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                        addItems.add(opDt);
                    }
                }
                List<SyxBlOpDt> ordAddSupplyItems = ord.getAddSupplyItems();
                if (ordAddSupplyItems != null && ordAddSupplyItems.size() > 0) {
                    for (SyxBlOpDt opDt : ordAddSupplyItems) {
                        opDt.setPkPv(ord.getPkPv());
                        opDt.setPkPi(ord.getPkPi());
                        opDt.setEuAdditem("2");
                        opDt.setPkPres(ord.getPkPres());
                        opDt.setPkCnord(ord.getPkCnord());
                        opDt.setPkOrgEx(ord.getPkOrgExec());
                        opDt.setPkDeptEx(ord.getPkDeptExec());
                        opDt.setPkOrgApp(ord.getPkOrg());
                        opDt.setPkDeptApp(ord.getPkDept());
                        opDt.setPkEmpApp(ord.getPkEmpOrd());
                        opDt.setNameEmpApp(ord.getNameEmpOrd());
                        opDt.setPkDeptJob(pkDeptJob);
                        opDt.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                        addItems.add(opDt);
                    }
                }
            }
        }
        if (deletingList != null && deletingList.size() > 0) {
            for (CnOrder ord : deletingList) {
                String presNo = ord.getPresNo();
                if (!presNoList.contains(presNo)) {
                    presNoList.add(presNo);
                }
                delItemsByCnord.add(ord.getPkCnord());
            }
            //cnPubService.vaildUpdateCnOrdts(deletingList);
        }
        if (editingList != null && editingList.size() > 0) {
            //cnPubService.vaildUpdateCnOrdts(editingList);
            for (SyxCnOrderVo upCnOrder : editingList) {
                if (StringUtils.isEmpty(upCnOrder.getPkCnord())) continue;
                delItemsByCnord.add(upCnOrder.getPkCnord()); //??????????????????????????????????????????????????????
                List<SyxBlOpDt> ordAddItems = upCnOrder.getAddItems();
                if (ordAddItems != null && ordAddItems.size() > 0) {
                    for (SyxBlOpDt opDt : ordAddItems) {
                        opDt.setPkPv(upCnOrder.getPkPv());
                        opDt.setPkPi(upCnOrder.getPkPi());
                        opDt.setEuAdditem("1");
                        opDt.setPkPres(upCnOrder.getPkPres());
                        opDt.setPkCnord(upCnOrder.getPkCnord());
                        opDt.setPkOrgEx(upCnOrder.getPkOrgExec());
                        opDt.setPkDeptEx(upCnOrder.getPkDeptExec());
                        opDt.setPkOrgApp(upCnOrder.getPkOrg());
                        opDt.setPkDeptApp(upCnOrder.getPkDept());
                        opDt.setPkEmpApp(upCnOrder.getPkEmpOrd());
                        opDt.setNameEmpApp(upCnOrder.getNameEmpOrd());
                        opDt.setPkDeptJob(pkDeptJob);
                        opDt.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                        addItems.add(opDt);
                    }
                }
                List<SyxBlOpDt> ordAddSupplyItems = upCnOrder.getAddSupplyItems();
                if (ordAddSupplyItems != null && ordAddSupplyItems.size() > 0) {
                    for (SyxBlOpDt opDt : ordAddSupplyItems) {
                        opDt.setPkPv(upCnOrder.getPkPv());
                        opDt.setPkPi(upCnOrder.getPkPi());
                        opDt.setEuAdditem("2");
                        opDt.setPkPres(upCnOrder.getPkPres());
                        opDt.setPkCnord(upCnOrder.getPkCnord());
                        opDt.setPkOrgEx(upCnOrder.getPkOrgExec());
                        opDt.setPkDeptEx(upCnOrder.getPkDeptExec());
                        opDt.setPkOrgApp(upCnOrder.getPkOrg());
                        opDt.setPkDeptApp(upCnOrder.getPkDept());
                        opDt.setPkEmpApp(upCnOrder.getPkEmpOrd());
                        opDt.setNameEmpApp(upCnOrder.getNameEmpOrd());
                        opDt.setPkDeptJob(pkDeptJob);
                        opDt.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                        addItems.add(opDt);
                    }
                }
                //?????????????????????1??? ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                String presNo = upCnOrder.getPresNo();
                if (!presNoList.contains(presNo)) {
                    presNoList.add(presNo);
                }
                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                //?????????????????????????????????2??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????.???????????????????????????pkPres
                String pkPres = upCnOrder.getPkPres();
                if (StringUtils.isBlank(pkPres)) { //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    int toRNum = upCnOrder.getrNum(); //?????????????????????
                    boolean findNewPres = false;
                    for (SyxCnOrderVo add : addingList) {
                        if (add.getrNum() != 0 && toRNum == add.getrNum()) {
                            upCnOrder.setPkPres(add.getPkPres()); //??????????????????????????????
                            findNewPres = true;
                        }
                    }
                    if (!findNewPres) { //?????????????????????2??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        CnPrescription addPresPo = new CnPrescription();
                        BeanUtils.copyProperties(addPresPo, upCnOrder);
                        addPresPo.setPresNo(ApplicationUtils.getCode("0406"));
                        addPresPo.setDatePres(d);
                        addPresPo.setPkPres(NHISUUID.getKeyId());
                        addPresPo.setTs(d);
                        addPresPo.setModityTime(new Date());
                        addPresPo.setDelFlag("0");
                        addPresPo.setCreateTime(new Date());
                        addPresPo.setNote(upCnOrder.getNotePres());
                        addPresPos.add(addPresPo);
                        upCnOrder.setPkPres(addPresPo.getPkPres());
                        upCnOrder.setPresNo(addPresPo.getPresNo());
                    }
                }
                upCnOrder.setTs(d);
                upCnOrder.setDateEnter(d);
                upCnOrder.setPkEmpOrd(u.getPkEmp());
                upCnOrder.setNameEmpOrd(u.getNameEmp());
                upCnOrder.setPkDeptJob(pkDeptJob);//????????????
                upCnOrder.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                upCnOrder.setGroupno(upCnOrder.getgNum());
                opOrdToOpCg(upCnOrder, blOpItem);
                editCnOrdList.add(upCnOrder);
                pkPresList.add(upCnOrder.getPkPres());
                if (StringUtils.isNotBlank(upCnOrder.getPkPres()) &&
                        StringUtils.isBlank(presDtHpprop.get(upCnOrder.getPkPres()))) {
                    presDtHpprop.put(upCnOrder.getPkPres(), upCnOrder.getDtHpprop());
                }
            }
        }
        //??????????????????????????????????????????????????????
        if (addItems != null && addItems.size() > 0) {
            for (BlOpDt opDtItem : addItems) {
                supplyItemToOpCg(opDtItem, blOpItem);
            }
        }
        //3.?????????????????????
        if (addPresPos != null && addPresPos.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnPrescription.class), addPresPos);
        }
        //??????????????????
        if (editCnOrdList != null && editCnOrdList.size() > 0) {
            int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord in (" + getPkCnordStr(editCnOrdList) + " ) and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{});
            if (hadSettleCgCount > 0) {
                throw new BusException("??????????????????????????????????????????????????????????????????????????????");
            }
            logger.error("SyxCnOpOrderService.savePdOrders:{}",JSON.toJSONString(editCnOrdList));
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord=:pkCnord ",editCnOrdList);
            DataBaseHelper.batchUpdate("update cn_order set code_supply_add=:codeSupplyAdd, days=:days, pk_pres=:pkPres, ts=:ts,ordsn=:ordsn,ordsn_parent=:ordsnParent,pk_ord=:pkOrd,code_ord=:codeOrd,name_ord = :nameOrd,dosage=:dosage,pk_unit_dos=:pkUnitDos,code_supply=:codeSupply,code_freq=:codeFreq,pk_dept_exec=:pkDeptExec,pk_org_exec=:pkOrgExec,note_supply=:noteSupply,note_ord=:noteOrd,code_ordtype=:codeOrdtype,spec=:spec,pk_unit=:pkUnit,eu_st=:euSt,desc_ord=:descOrd,quan_cg=:quanCg,pk_unit_cg=:pkUnitCg,flag_pivas=:flagPivas,name_emp_ord=:nameEmpOrd,pk_emp_ord=:pkEmpOrd,date_enter=:dateEnter,DT_HPPROP=:dtHpprop,price_cg=:priceCg,groupno=:groupno,pk_dept_job=:pkDeptJob,PK_DEPT_AREAAPP=:pkDeptAreaapp,flag_disp=:flagDisp,flag_fit=:flagFit,desc_fit=:descFit,FLAG_SELF=:flagSelf,eu_injury=:euInjury,EU_EASON_DIS=:euEasonDis,AMOUNT_DISC=:amountDisc,FLAG_DISCST=:flagDiscst,FLAG_PLAN=:flagPlan,pack_size=:packSize  where pk_cnord = :pkCnord ", editCnOrdList);
            DataBaseHelper.batchUpdate("update CN_PRESCRIPTION set DT_PRESTYPE=:dtPrestype,note=:notePres where pk_pres=:pkPres", editCnOrdList);

        }
        //??????????????????
        if (addCnOrdList != null && addCnOrdList.size() > 0) {
            //?????????????????????
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord in ( select pk_cnord from cn_order ord where ord.ordsn=:ordsn)", addCnOrdList);
            logger.error("SyxCnOpOrderService.savePdOrders:{}",JSON.toJSONString(addCnOrdList));
            DataBaseHelper.batchUpdate("delete from cn_order where ordsn=:ordsn and pk_pv=:pkPv ", addCnOrdList);
            //????????????
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), addCnOrdList);
            List<CnOrder> ordNewAdds=new ArrayList<>();
            for (CnOrder ord : addCnOrdList){
                ordNewAdds.add(ord);
            }
            //??????CA????????????
            cnPubService.caRecordByOrd(false, ordNewAdds);
        }
        List<Map<String, Object>> delMapList = null;
        //??????????????????????????????
        String processClass = ApplicationUtils.getPropertyValue("msg.processClass", "");
        if ("SDPlatFormSendService".equals(processClass)) {//???????????????????????????????????????
            String delPkCnords = "";
            if (deletingList != null && deletingList.size() > 0) {
                String sql = "SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always, "
                        + " dt.PK_SETTLE,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer, "
                        + " co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input FROM cn_order co "
                        + " LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where 1=1 "
                        + "and co.pk_cnord  in ( ";
                int pkCnOrdSize = deletingList.size();
                for (int i = 0; i < pkCnOrdSize; i++) {
                    if (i == pkCnOrdSize - 1) {
                        if (StringUtils.isNotBlank(deletingList.get(i).getPkCnord())) {
                            delPkCnords += "'" + deletingList.get(i).getPkCnord() + "')";
                        }
                    } else {
                        if (StringUtils.isNotBlank(deletingList.get(i).getPkCnord())) {
                            delPkCnords += "'" + deletingList.get(i).getPkCnord() + "',";
                        }
                    }
                }
                delMapList = DataBaseHelper.queryForList(sql + delPkCnords);
            }
        }
        //????????????????????????????????????
        if ("PskqPlatFormSendService".equals(processClass)) {//???????????????????????????????????????
            List<Map<String, Object>> delList = new ArrayList<Map<String, Object>>();
            if (deletingList != null && deletingList.size() > 0) {
                List<String> pkCnords = new ArrayList<>();
                for (int i = 0; i < deletingList.size(); i++) {
                    if (StringUtils.isNotBlank(deletingList.get(i).getPkCnord())) {
                        pkCnords.add(deletingList.get(i).getPkCnord());
                    }
                }
                if (pkCnords.size() > 0) {
                    ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                    PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper = applicationContext.getBean("pskqPlatFormSendCnMapper", PskqPlatFormSendCnMapper.class);
                    List<OrderOutpat> OrderOutpatList = pskqPlatFormSendCnMapper.queryOpCnorderDrugs(pkCnords);
                    OrderOutpatList.addAll(pskqPlatFormSendCnMapper.queryOpCnorderProject(pkCnords));
                    for (OrderOutpat orderOutpat : OrderOutpatList) {
                        orderOutpat.setCancelFlag("1");
                        orderOutpat.setCancelOperaId(u.getCodeEmp());
                        orderOutpat.setCancelOperaName(u.getUserName());
                        String stringBean = JsonUtil.writeValueAsString(orderOutpat);
                        delList.add(JsonUtil.readValue(stringBean, Map.class));
                    }
                    delMapList = delList;
                }
            }
        }
        if (deletingList != null && deletingList.size() > 0) {
            int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord in (" + getPkCnordStr(deletingList) + " ) and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{});
            if (hadSettleCgCount > 0) {
                throw new BusException("??????????????????????????????????????????????????????????????????????????????");
            }
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord = :pkCnord and flag_settle='0' ",deletingList);
            logger.error("SyxCnOpOrderService.savePdOrders:{}",JSON.toJSONString(deletingList));
            DataBaseHelper.batchUpdate("delete from cn_order where pk_cnord = :pkCnord and pk_pv=:pkPv ", deletingList);
        }
        if (delItemsByCnord.size() > 0) {
            List<BlOpDt> blOpDts = new ArrayList<BlOpDt>();
            for (String pk : delItemsByCnord) {
                BlOpDt dt = new BlOpDt();
                dt.setPkCnord(pk);
                blOpDts.add(dt);
            }
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord=:pkCnord and flag_settle='0'", blOpDts);
        }
        if (blOpItem.size() > 0) {
            logger.error("SyxCnOpOrderService.savePdOrders??????????????????????????????:{}",JSON.toJSONString(blOpItem));
            opCgPubService.blOpCgBatch(blOpItem);
        }
        //???????????????cn_prescription
        if (presNoList.size() > 0) {
            Map<String, Object> presMap = new HashMap<String, Object>();
            presMap.put("presNo", presNoList);
            List<String> delPres = new ArrayList<String>();
            //??????????????????????????????????????????????????????
            List<Map<String, Object>> ordPres = DataBaseHelper.queryForList("select pres.pres_no,count(cn.pk_pres) ord_num from cn_prescription pres left join  cn_order cn on cn.pk_pres = pres.pk_pres and cn.eu_pvtype!='3' where pres.pres_no in(:presNo) and pres.pres_no is not null    group by pres.pres_no ", presMap);
            if (ordPres != null && ordPres.size() > 0) {
                for (Map<String, Object> map : ordPres) {
                    Integer ordNum = map.get("ordNum") != null ? Integer.valueOf(map.get("ordNum").toString()) : 0;
                    if (ordNum == 0) {
                        delPres.add(map.get("presNo").toString());
                    }
                }
            }
            if (delPres.size() > 0) {
                presMap = new HashMap<String, Object>();
                presMap.put("presNo", delPres);
                DataBaseHelper.update("delete from cn_prescription where pres_no in (:presNo)", presMap);
            }
        }
        //??????????????????????????????????????????,??????????????????
        if (!"true".equals(ApplicationUtils.getPropertyValue("cn.press.enable", ""))) {
            //1.???????????????????????????????????????DtHpprop???2.???????????????????????????
            List<String> presDtModSql = new ArrayList<>();
            if (presDtHpprop.size() > 0) {
                Iterator<String> iterator = presDtHpprop.keySet().iterator();
                String tempSql = "update %s set DT_HPPROP='%s' where PK_PRES='%s' and not exists(select 1 from BL_OP_DT dt where dt.PK_PRES='%s' and dt.FLAG_SETTLE='1')";
                while (iterator.hasNext()) {
                    String pkPres = iterator.next();
                    String dtHpprop = presDtHpprop.get(pkPres);
                    if (StringUtils.isBlank(dtHpprop)) {
                        dtHpprop = MapUtils.getString(DataBaseHelper.queryForMap("select DT_HPPROP from CN_PRESCRIPTION where PK_PRES=?", new Object[]{pkPres}), "dtHpprop");
                    } else {
                        presDtModSql.add(String.format(tempSql, "CN_PRESCRIPTION", dtHpprop, pkPres, pkPres));
                    }
                    if (StringUtils.isNotBlank(dtHpprop)) {
                        presDtModSql.add(String.format(tempSql, "CN_ORDER", dtHpprop, pkPres, pkPres));
                    }
                }
                DataBaseHelper.batchUpdate(presDtModSql.toArray(new String[0]));
            }
        }
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("control", "OK");
        mapParam.put("OMPtype", "WesternMedicine");
        mapParam.put("addCnOrdList", addCnOrdList);
        mapParam.put("editCnOrdList", editCnOrdList);
        mapParam.put("delMapList", delMapList);
        PlatFormSendUtils.sendOpO09Msg(mapParam);
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("pkPresList", pkPresList);
        return resultMap;
    }

    //??????????????????--pkCnord
    private String getPkCnordStr(List<? extends CnOrder> ordList) {
        StringBuffer sbfSql = new StringBuffer();
        String rtnStr = "";
        if (ordList != null && ordList.size() > 0) {
            for (CnOrder order : ordList) {
                sbfSql.append("'" + order.getPkCnord() + "',");
            }
            sbfSql.deleteCharAt(sbfSql.length() - 1);
            rtnStr = sbfSql.toString();
        }
        return rtnStr;
    }

    private void supplyItemToOpCg(BlOpDt addItems,
                                  List<BlPubParamVo> blOpItem) throws IllegalAccessException, InvocationTargetException {
        User u = UserContext.getUser();
        String pkOrg = u.getPkOrg();
        String pkDept = u.getPkDept();
        BlPubParamVo bpb = new BlPubParamVo();
        BeanUtils.copyProperties(bpb, addItems);
        bpb.setPkOrg(pkOrg);
        bpb.setFlagPd("0");
        bpb.setFlagPv("0");
        bpb.setDateHap(new Date());
        bpb.setPkDeptCg(pkDept);
        bpb.setPkEmpCg(u.getPkEmp());
        bpb.setNameEmpCg(u.getNameEmp());
        //		bpb.setEuAdditem("1");//????????????????????????
        blOpItem.add(bpb);
    }

    private void opOrdToOpCg(SyxCnOrderVo opOrd, List<BlPubParamVo> blOpItem) {
        User u = UserContext.getUser();
        boolean flagChangQuan = "1".equals(opOrd.getFlagChangQuan());
        String pkOrg = u.getPkOrg();
        String pkDept = u.getPkDept();
        BlPubParamVo bpb = new BlPubParamVo();
        bpb.setPkOrg(pkOrg);
        bpb.setEuPvType(opOrd.getEuPvtype());
        bpb.setPkPv(opOrd.getPkPv());
        bpb.setPkPi(opOrd.getPkPi());
        bpb.setPkOrd(opOrd.getPkOrd());
        bpb.setPkCnord(opOrd.getPkCnord());
        bpb.setPkPres(opOrd.getPkPres());
        bpb.setPkItem(opOrd.getPkOrd());
        bpb.setQuanCg(opOrd.getQuanCg());
        bpb.setPkUnitPd(opOrd.getPkUnitCg());
        if (flagChangQuan) {
            bpb.setQuanCg(opOrd.getQuan());
            bpb.setPkUnitPd(opOrd.getPkUnit());
        }
        bpb.setPkOrgEx(opOrd.getPkOrgExec());
        bpb.setPkOrgApp(opOrd.getPkOrg());
        bpb.setPkDeptEx(opOrd.getPkDeptExec());
        bpb.setPkDeptApp(opOrd.getPkDept());
        bpb.setPkEmpApp(opOrd.getPkEmpOrd());
        bpb.setNameEmpApp(opOrd.getNameEmpOrd());
        bpb.setFlagPd("1");
        bpb.setNamePd(opOrd.getNameOrd());
        bpb.setFlagPv("0");
        //bpb.setDateExpire(opOrd.getDateExpire());
        bpb.setPackSize(opOrd.getPackSize().intValue());
        if("1".equals(opOrd.getFlagSelf())){
            bpb.setPrice(0d);
        }else {
            bpb.setPrice(opOrd.getPriceCg());
        }

        //bpb.setPriceCost(opOrd.getPriceCost());
        bpb.setDateHap(new Date());
        bpb.setPkDeptCg(pkDept);
        bpb.setPkEmpCg(u.getPkEmp());
        bpb.setNameEmpCg(u.getNameEmp());
        bpb.setEuAdditem("0"); //????????????
        bpb.setPkDeptJob(opOrd.getPkDeptJob());
        bpb.setPkDeptAreaapp(opOrd.getPkDeptAreaapp());
        bpb.setFlagSelf(opOrd.getFlagSelf());
        blOpItem.add(bpb);
    }

    public List<Map<String, Object>> qryPiCurrMonthPdDays(String param, IUser user) {
        //pkPi/pkPd
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPi = paramMap.get("pkPi") != null ? paramMap.get("pkPi").toString() : "";
        if (StringUtils.isBlank(pkPi)) throw new BusException("??????pkPi?????????");
        String pkPd = paramMap.get("pkPd") != null ? paramMap.get("pkPd").toString() : "";
        if (StringUtils.isBlank(pkPd)) throw new BusException("??????pkPd?????????");
        return null;
    }

    public static <T> List<List<T>> dividerList(List<T> list, Comparator<? super T> comparator) {
        List<List<T>> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            boolean isContain = false;
            for (int j = 0; j < lists.size(); j++) {
                if (lists.get(j).size() == 0 || comparator.compare(lists.get(j).get(0), list.get(i)) == 0) {
                    lists.get(j).add(list.get(i));
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                List<T> newList = new ArrayList<>();
                newList.add(list.get(i));
                lists.add(newList);
            }
        }
        return lists;
    }

    public Map<String, String> saveOpDiag(String param, IUser user) {
        SaveDiagParam diagParam = JsonUtil.readValue(param, SaveDiagParam.class);
        PatInfo patInfo = diagParam.getPatInfo();
        //??????????????????
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        map.put("pkPv", patInfo.getPkPv());
        String sql = "select * from pv_diag where pk_pv=?";
        List<Map<String, Object>> paramList = DataBaseHelper.queryForList(sql, patInfo.getPkPv());
        map.put("paramList", paramList);//????????????????????????
        if (StringUtils.isEmpty(patInfo.getPkPv())) throw new BusException("????????????????????????!");
        Map<String, String> rtnMap = new HashMap<String, String>();
        String flagToCp = "0";
        String flagDiagDiv = "0";
        if ("1".equals(diagParam.getFlagUpPvmode())) {
            DataBaseHelper.update("update pv_encounter  set eu_pvmode='2' where pk_pv=? and  del_flag='0' and nvl(eu_pvmode,'0')='0' ", new Object[]{patInfo.getPkPv()});
            return rtnMap;
        }
        List<PvDiag> list = diagParam.getPvDiagList();
        User u = (User) user;
        DataBaseHelper.update("update pv_diag set del_flag='1' where pk_pv=?", new Object[]{patInfo.getPkPv()});
        for (PvDiag pvDiag : list) {
            pvDiag.setDelFlag("0");
            if (StringUtils.isEmpty(pvDiag.getPkPvdiag())) {
                DataBaseHelper.insertBean(pvDiag);
            } else {
                DataBaseHelper.updateBeanByPk(pvDiag, false);
            }
        }
        //?????????--????????????2020.05.14
        DataBaseHelper.update("delete from pv_diag  where pk_pv=? and del_flag='1'", new Object[]{patInfo.getPkPv()});
        List<PvDiag> qryList = DataBaseHelper.queryForList("select * from pv_diag where del_flag='0' and pk_pv=? order by ts desc", PvDiag.class, new Object[]{patInfo.getPkPv()});
        if (qryList.size() == 0) {
            rtnMap.put("flagToCp", flagToCp);
            rtnMap.put("flagDiagDiv", flagDiagDiv);
            //??????????????????
            PlatFormSendUtils.sendCnDiagMsg(map);
            return rtnMap;
        }
        CnDiag cnDiag = new CnDiag();
        cnDiag.setPkCndiag(NHISUUID.getKeyId());
        cnDiag.setEuPvtype(patInfo.getEuPvtype());
        cnDiag.setPkPv(patInfo.getPkPv());
        cnDiag.setDateDiag(new Date());
        cnDiag.setPkEmpDiag(u.getPkEmp());
        cnDiag.setNameEmpDiag(u.getNameEmp());
        String desc_diags = "";
        List<CnDiagDt> cnDiagDtList = new ArrayList<CnDiagDt>();
        List<String> pkDiags = new ArrayList<String>();
        for (PvDiag pvDiag : qryList) {
            pkDiags.add(pvDiag.getPkDiag());
            if (StringUtils.isEmpty(desc_diags)) {
                desc_diags += pvDiag.getDescDiag();
            } else {
                desc_diags += "," + pvDiag.getDescDiag();
            }
            CnDiagDt cnDiagDt = new CnDiagDt();
            cnDiagDt.setPkOrg(u.getPkOrg());
            cnDiagDt.setPkCndiagdt(NHISUUID.getKeyId());
            cnDiagDt.setPkCndiag(cnDiag.getPkCndiag());
            cnDiagDt.setSortNo(pvDiag.getSortNo());
            cnDiagDt.setDtTreatway(pvDiag.getDtTreatway());
            cnDiagDt.setDtDiagtype(pvDiag.getDtDiagtype());
            cnDiagDt.setPkDiag(pvDiag.getPkDiag());
            cnDiagDt.setDescDiag(pvDiag.getDescDiag());
            cnDiagDt.setFlagMaj(pvDiag.getFlagMaj());
            cnDiagDt.setFlagSusp(pvDiag.getFlagSusp());
            cnDiagDt.setFlagInfect(pvDiag.getFlagContagion());
            cnDiagDt.setFlagFinally(pvDiag.getFlagFinally());
            cnDiagDt.setFlagCure(pvDiag.getFlagCure());
            cnDiagDt.setTs(new Date());
            cnDiagDtList.add(cnDiagDt);
        }
        cnDiag.setDescDiags(desc_diags);
        DataBaseHelper.insertBean(cnDiag);
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnDiagDt.class), cnDiagDtList);
        String flagQry = diagParam.getFlagQryHp();
        if ("1".equals(flagQry)) {
            int diagDiv = DataBaseHelper.queryForScalar("select count(1) from bd_hp_diagdiv div inner join bd_hp_diagdiv_itemcate cate on div.pk_totaldiv=cate.pk_totaldiv and cate.del_flag='0'  where div.pk_hp=? and div.pk_diag=?  and div.del_flag='0'", Integer.class, new Object[]{diagParam.getPkHp(), diagParam.getPkDiag()});
            if (diagDiv > 0) flagDiagDiv = "1";
        }
        rtnMap.put("flagToCp", flagToCp);
        rtnMap.put("flagDiagDiv", flagDiagDiv);
        //??????????????????
        PlatFormSendUtils.sendCnDiagMsg(map);
        //??????????????????
        //ordProcService.syncDiagInfo(qryList,u);
        return rtnMap;
    }

    /**
     * ??????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> getPrescriptionDetail(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String sPkPres = map.get("pkPres") == null ? "" : map.get("pkPres").toString();
        if (StringUtils.isBlank(sPkPres)) throw new BusException("?????????????????????");
        String pkDeptExec = map.get("pkDeptExec") == null ? "" : map.get("pkDeptExec").toString();
        if (StringUtils.isBlank(pkDeptExec)) throw new BusException("????????????????????????");
        //  String pkDeptExChineseDrug = map.get("pkDeptExChineseDrug") == null?"":map.get("pkDeptExChineseDrug").toString();
        //   if(StringUtils.isBlank(pkDeptExChineseDrug)) throw new BusException("????????????????????????");
        List<Map<String, Object>> prescriptionDetails = opOrderDao.getPrescriptionDetail(sPkPres, pkDeptExec, null);
        return prescriptionDetails;
    }

    private Date getDateEffec(String euPvType) {

        String val = ApplicationUtils.getSysparam("CN0004", false);
        if (org.apache.commons.lang3.StringUtils.isEmpty(val)) {
            if (EnumerateParameter.TWO.equals(euPvType))// ??????
            {
                val = EnumerateParameter.TWO;
            } else {// ??????
                val = EnumerateParameter.THREE;
            }

        }
        Date dateEffec = DateUtils.getSpecifiedDay(new Date(), Integer.parseInt(val));
        return dateEffec;
    }
    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getDaysAndQuan(String param, IUser user) throws Exception {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> days = opOrderDao.queryDays(map);
        if (days.size() > 0) {
            int day = Integer.parseInt(days.get(0).get("valAttr").toString());
            String date = DateUtils.addDate(new Date(), -day, 3, "yyyyMMdd");
            map.put("date", date + "000000");
        }
        List<Map<String, Object>> paramMap = opOrderDao.queryDaysAndQuan(map);
        return paramMap;
    }

    /**
     * ???????????????????????????
     *
     * @param param
     * @param user
     */
    public void deteteOrd(String param, IUser user) {
        SyxOpOrderVo opOrderParam = JsonUtil.readValue(param, SyxOpOrderVo.class);
        if (opOrderParam == null || opOrderParam.getDeletingList() == null || opOrderParam.getDeletingList().size() == 0) {
            return;
        }
        List<SyxCnOrderVo> deletingList = opOrderParam.getDeletingList();
        //??????????????????
        if (deletingList != null && deletingList.size() > 0) {
            int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord in (" + getPkCnordStr(deletingList) + " ) and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{});
            if (hadSettleCgCount > 0) {
                throw new BusException("???????????????????????????????????????????????????");
            }
            logger.error("SyxCnOpOrderService.deteteOrd:{}",JSON.toJSONString(deletingList));
            //???????????????????????????
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord = :pkCnord ", deletingList);
            //????????????
            DataBaseHelper.batchUpdate("delete from cn_order where pk_cnord = :pkCnord ", deletingList);

            List<String> presNoList = new ArrayList<String>();
            if (deletingList != null && deletingList.size() > 0) {
                for (CnOrder ord : deletingList) {
                    String presNo = ord.getPresNo();
                    if (!presNoList.contains(presNo)) {
                        presNoList.add(presNo);
                    }
                }
            }
            //???????????????cn_prescription
            if (presNoList.size() > 0) {
                Map<String, Object> presMap = new HashMap<String, Object>();
                presMap.put("presNo", presNoList);
                List<String> delPres = new ArrayList<String>();
                //??????????????????????????????????????????????????????
                List<Map<String, Object>> ordPres = DataBaseHelper.queryForList("select pres.pres_no,count(cn.pk_pres) ord_num from cn_prescription pres left join  cn_order cn on cn.pk_pres = pres.pk_pres and cn.eu_pvtype!='3' where pres.pres_no in(:presNo) and pres.pres_no is not null    group by pres.pres_no ", presMap);
                if (ordPres != null && ordPres.size() > 0) {
                    for (Map<String, Object> map : ordPres) {
                        Integer ordNum = map.get("ordNum") != null ? Integer.valueOf(map.get("ordNum").toString()) : 0;
                        if (ordNum == 0) {
                            delPres.add(map.get("presNo").toString());
                        }
                    }
                }
                if (delPres.size() > 0) {
                    presMap = new HashMap<String, Object>();
                    presMap.put("presNo", delPres);
                    DataBaseHelper.update("delete from cn_prescription where pres_no in (:presNo)", presMap);
                }
            }
        }
    }
    /************************************???????????? start***********************************/
    /**
     * ????????????????????????????????????????????????
     * ??????????????????????????????????????????
     *
     * @param param
     * @param user
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void savePdOrdersNew(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        SyxOpOrderVo opOrderParam = JsonUtil.readValue(param, SyxOpOrderVo.class);
        Date d = new Date();
        User u = (User) user;
        PatInfo patInfo = opOrderParam.getPatInfo();
        //??????????????????????????????????????????????????????
        int eulocked = DataBaseHelper.queryForScalar("select count(*) " +
                "from PV_ENCOUNTER PE where EU_LOCKED='2'and  PK_PV=?", Integer.class, patInfo.getPkPv());
        String pv0057 = ApplicationUtils.getSysparam("PV0057", false);
        if(!"0".equals(pv0057)){
            pv0057 = "1";
        }
        if (eulocked > 0 && "1".equals(pv0057)) {
            throw new BusException("???????????????????????????????????????????????????????????????????????????");
        }
        //?????????????????????????????? -??????????????????????????????
        BdOuEmpjob emp = JsonUtil.readValue(String.valueOf(
                RedisUtils.getCacheHashObj("cnop:bdouempjob", u.getPkEmp())), BdOuEmpjob.class);
        if (emp == null) {
            emp = DataBaseHelper.queryForBean(
                    "SELECT pk_dept FROM bd_ou_empjob WHERE pk_emp =? AND is_main = '1' ", BdOuEmpjob.class, new Object[]{u.getPkEmp()});
            RedisUtils.setCacheHashObj("cnop:bdouempjob", u.getPkEmp(), emp);
        }
        String pkDeptJob = emp.getPkDept();
        //??????????????????????????????
        String pkDeptArea = patInfo.getPkDeptArea();
        //????????????????????????
        String blCode = ApplicationUtils.getSysparam("BL0025", false);
        BdItem ydBl = null;
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(blCode)) {
            ydBl = DataBaseHelper.queryForBean("select * from BD_ITEM where CODE =? ", BdItem.class, new Object[]{blCode});
        }
        //??????????????????????????????
        List<BdSupply> supplyList = JsonUtil.readValue(String.valueOf(
                RedisUtils.getCacheObj("cnop:bdsupply:allinfo")), new TypeReference<List<BdSupply>>() {
        });
        if (CollectionUtils.isEmpty(supplyList)) {
            String supplySql = "SELECT  BD_SUPPLY.* \n" +
                    "from BD_SUPPLY\n" +
                    "left join BD_SUPPLY_CLASS on BD_SUPPLY.PK_SUPPLYCATE = BD_SUPPLY_CLASS.PK_SUPPLYCATE \n" +
                    "where BD_SUPPLY.del_flag='0' and BD_SUPPLY_CLASS.del_flag='0' and BD_SUPPLY_CLASS.code='01' ";
            supplyList = DataBaseHelper.queryForList(supplySql, BdSupply.class);
            RedisUtils.setCacheObj("cnop:bdsupply:allinfo", supplyList, 360);
        }
        List<SyxCnOrderVo> addingList = opOrderParam.getAddingList();
        List<SyxCnOrderVo> deletingList = opOrderParam.getDeletingList();
        List<SyxCnOrderVo> editingList = opOrderParam.getEditingList();
        List<BlOpDt> addItems = new ArrayList<BlOpDt>();  //???????????????????????????
        List<BlPubParamVo> blOpItem = new ArrayList<BlPubParamVo>();
        List<CnPrescription> addPresPos = new ArrayList<CnPrescription>();
        List<String> presNoList = new ArrayList<String>();
        List<SyxCnOrderVo> addCnOrdList = new ArrayList<SyxCnOrderVo>();
        List<String> delItemsByCnord = new ArrayList<String>();
        List<SyxCnOrderVo> editCnOrdList = new ArrayList<SyxCnOrderVo>();
        Map<String, String> presDtHpprop = new HashMap<>();

        //??????????????????
        List<String> pkCnords=new ArrayList<String>();


        //????????????
        if (addingList != null && addingList.size() > 0) {
            int rNum = 0;
            String pkPres = "";
            Map<Integer, Integer> ordNumMap = new HashMap<Integer, Integer>();
            int ordNumMin = bdSnService.getSerialNo("CN_ORDER", "ORDSN", addingList.size(), u);
            for (SyxCnOrderVo ord : addingList) {
                ordNumMap.put(ord.getOrdNum(), ordNumMin);
                ordNumMin += 1;
            }
            for (SyxCnOrderVo ord : addingList) {
                int ordRNum = ord.getrNum(); //???1??????
                /************????????????  start ******************/
                if (ordRNum != rNum) {
                    rNum = ordRNum;
                    CnPrescription addPresPo = new CnPrescription();
                    BeanUtils.copyProperties(addPresPo, ord);
                    addPresPo.setDatePres(d);
                    if (StringUtils.isBlank(ord.getPkPres())) {
                        addPresPo.setPkPres(NHISUUID.getKeyId());
                        ord.setPkPres(addPresPo.getPkPres());
                    } else {
                        addPresPo.setPkPres(ord.getPkPres());
                    }
                    if (StringUtils.isBlank(ord.getPresNo())) {
                        addPresPo.setPresNo(ApplicationUtils.getCode("0406"));
                    } else {
                        addPresPo.setPresNo(ord.getPresNo());
                    }
                    pkPres = addPresPo.getPkPres();
                    addPresPo.setTs(d);
                    addPresPo.setDelFlag("0");
                    addPresPo.setCreateTime(new Date());
                    addPresPo.setModityTime(null);
                    addPresPos.add(addPresPo);
                }
                /************????????????  end ******************/
                ord.setPkCnord(NHISUUID.getKeyId());
                ord.setPkPres(StringUtils.isBlank(ord.getPkPres()) ? pkPres : ord.getPkPres());
                ord.setOrdsn(ord.getOrdsn() == 0 ? ordNumMap.get(ord.getOrdNum()) : ord.getOrdsn());
                ord.setOrdsnParent(ord.getOrdsnParent() == 0 ? (ordNumMap.containsKey(ord.getOrdParentNum()) ? ordNumMap.get(ord.getOrdParentNum()) : ord.getOrdsn()) : ord.getOrdsnParent());
                ord.setOrdsnChk(ord.getOrdsn());
                ord.setEuAlways("1"); //??????
                ord.setDateEnter(d);
                ord.setDateStart(d);
                ord.setDescOrd(null);
                ord.setTs(d);
                ord.setDelFlag("0");
                ord.setFlagDoctor("1");
                ord.setCreateTime(d);
                ord.setModityTime(null);
                ord.setCreator(u.getPkEmp());
                ord.setDateEffe(getDateEffec(ord.getEuPvtype()));// ????????????
                ord.setGroupno(ord.getgNum());//???????????????
                ord.setPkDeptJob(pkDeptJob);//????????????
                ord.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                opOrdToOpCg(ord, blOpItem);
                addCnOrdList.add(ord);
                if (StringUtils.isNotBlank(ord.getPkPres()) &&
                        StringUtils.isBlank(presDtHpprop.get(ord.getPkPres()))) {
                    presDtHpprop.put(ord.getPkPres(), ord.getDtHpprop());
                }

                /***************???????????? start***************/
                List<SyxBlOpDt> ordAddItems = ord.getAddItems();
                if (ordAddItems != null && ordAddItems.size() > 0) {
                    for (SyxBlOpDt opDt : ordAddItems) {
                        opDt.setPkPv(ord.getPkPv());
                        opDt.setPkPi(ord.getPkPi());
                        opDt.setPkPres(ord.getPkPres());
                        opDt.setEuAdditem("1");
                        opDt.setPkCnord(ord.getPkCnord());
                        opDt.setPkOrgEx(ord.getPkOrgExec());
                        opDt.setPkDeptEx(ord.getPkDeptExec());
                        opDt.setPkOrgApp(ord.getPkOrg());
                        opDt.setPkDeptApp(ord.getPkDept());
                        opDt.setPkEmpApp(ord.getPkEmpOrd());
                        opDt.setNameEmpApp(ord.getNameEmpOrd());
                        opDt.setPkDeptJob(pkDeptJob);
                        opDt.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                        addItems.add(opDt);
                    }
                }
                List<SyxBlOpDt> ordAddSupplyItems = ord.getAddSupplyItems();
                if (ordAddSupplyItems != null && ordAddSupplyItems.size() > 0) {
                    for (SyxBlOpDt opDt : ordAddSupplyItems) {
                        opDt.setPkPv(ord.getPkPv());
                        opDt.setPkPi(ord.getPkPi());
                        opDt.setEuAdditem("2");
                        opDt.setPkPres(ord.getPkPres());
                        opDt.setPkCnord(ord.getPkCnord());
                        opDt.setPkOrgEx(ord.getPkOrgExec());
                        opDt.setPkDeptEx(ord.getPkDeptExec());
                        opDt.setPkOrgApp(ord.getPkOrg());
                        opDt.setPkDeptApp(ord.getPkDept());
                        opDt.setPkEmpApp(ord.getPkEmpOrd());
                        opDt.setNameEmpApp(ord.getNameEmpOrd());
                        opDt.setPkDeptJob(pkDeptJob);
                        opDt.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                        addItems.add(opDt);
                    }
                }
                /***************???????????? end***************/

                //????????????????????????
                SyxBlOpDt yb = qryYdBl(ord, supplyList, ydBl);
                if (yb != null) {
                    yb.setPkDeptJob(pkDeptJob);
                    yb.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                    addItems.add(yb);
                }
            }
        }
        if (deletingList != null && deletingList.size() > 0) {
            for (CnOrder ord : deletingList) {
                String presNo = ord.getPresNo();
                if (!presNoList.contains(presNo)) {
                    presNoList.add(presNo);
                }
                delItemsByCnord.add(ord.getPkCnord());
            }
            //cnPubService.vaildUpdateCnOrdts(deletingList);
        }
        if (editingList != null && editingList.size() > 0) {
            //cnPubService.vaildUpdateCnOrdts(editingList);
            for (SyxCnOrderVo upCnOrder : editingList) {
                if (StringUtils.isEmpty(upCnOrder.getPkCnord())) continue;
                delItemsByCnord.add(upCnOrder.getPkCnord()); //??????????????????????????????????????????????????????
                List<SyxBlOpDt> ordAddItems = upCnOrder.getAddItems();
                if (ordAddItems != null && ordAddItems.size() > 0) {
                    for (SyxBlOpDt opDt : ordAddItems) {
                        opDt.setPkPv(upCnOrder.getPkPv());
                        opDt.setPkPi(upCnOrder.getPkPi());
                        opDt.setEuAdditem("1");
                        opDt.setPkPres(upCnOrder.getPkPres());
                        opDt.setPkCnord(upCnOrder.getPkCnord());
                        opDt.setPkOrgEx(upCnOrder.getPkOrgExec());
                        opDt.setPkDeptEx(upCnOrder.getPkDeptExec());
                        opDt.setPkOrgApp(upCnOrder.getPkOrg());
                        opDt.setPkDeptApp(upCnOrder.getPkDept());
                        opDt.setPkEmpApp(upCnOrder.getPkEmpOrd());
                        opDt.setNameEmpApp(upCnOrder.getNameEmpOrd());
                        opDt.setPkDeptJob(pkDeptJob);
                        opDt.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                        addItems.add(opDt);
                    }
                }
                List<SyxBlOpDt> ordAddSupplyItems = upCnOrder.getAddSupplyItems();
                if (ordAddSupplyItems != null && ordAddSupplyItems.size() > 0) {
                    for (SyxBlOpDt opDt : ordAddSupplyItems) {
                        if (ydBl != null && ydBl.getPkItem().equals(opDt.getPkItem())) {
                            continue;
                        }
                        opDt.setPkPv(upCnOrder.getPkPv());
                        opDt.setPkPi(upCnOrder.getPkPi());
                        opDt.setEuAdditem("2");
                        opDt.setPkPres(upCnOrder.getPkPres());
                        opDt.setPkCnord(upCnOrder.getPkCnord());
                        opDt.setPkOrgEx(upCnOrder.getPkOrgExec());
                        opDt.setPkDeptEx(upCnOrder.getPkDeptExec());
                        opDt.setPkOrgApp(upCnOrder.getPkOrg());
                        opDt.setPkDeptApp(upCnOrder.getPkDept());
                        opDt.setPkEmpApp(upCnOrder.getPkEmpOrd());
                        opDt.setNameEmpApp(upCnOrder.getNameEmpOrd());
                        opDt.setPkDeptJob(pkDeptJob);
                        opDt.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                        addItems.add(opDt);
                    }
                }
                //????????????????????????
                SyxBlOpDt yb = qryYdBl(upCnOrder, supplyList, ydBl);
                if (yb != null) {
                    yb.setPkDeptJob(pkDeptJob);
                    yb.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                    addItems.add(yb);
                }
                upCnOrder.setTs(d);
                upCnOrder.setDateEnter(d);
                upCnOrder.setPkEmpOrd(u.getPkEmp());
                upCnOrder.setNameEmpOrd(u.getNameEmp());
                upCnOrder.setPkDeptJob(pkDeptJob);//????????????
                upCnOrder.setPkDeptAreaapp(pkDeptArea);//??????????????????????????????????????????
                upCnOrder.setGroupno(upCnOrder.getgNum());
                opOrdToOpCg(upCnOrder, blOpItem);
                editCnOrdList.add(upCnOrder);
                if (StringUtils.isNotBlank(upCnOrder.getPkPres()) &&
                        StringUtils.isBlank(presDtHpprop.get(upCnOrder.getPkPres()))) {
                    presDtHpprop.put(upCnOrder.getPkPres(), upCnOrder.getDtHpprop());
                }
            }
        }
        //??????????????????????????????????????????????????????
        if (addItems != null && addItems.size() > 0) {
            for (BlOpDt opDtItem : addItems) {
                supplyItemToOpCg(opDtItem, blOpItem);
            }
        }
        //3.?????????????????????
        if (addPresPos != null && addPresPos.size() > 0) {
            //?????????????????????????????????
            List<CnPrescription> addPresPos1 = new ArrayList<CnPrescription>();
            for (CnPrescription item : addPresPos) {
                int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from CN_PRESCRIPTION where PK_PRES=? and del_flag='0'", Integer.class, new Object[]{item.getPkPres()});
                if (hadSettleCgCount > 0) {
                    continue;
                }
                addPresPos1.add(item);
            }
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnPrescription.class), addPresPos1);
        }
        //??????????????????
        if (editCnOrdList != null && editCnOrdList.size() > 0) {
            int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord in (" + getPkCnordStr(editCnOrdList) + " ) and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{});
            if (hadSettleCgCount > 0) {
                throw new BusException("??????????????????????????????????????????????????????????????????????????????");
            }
            logger.error("SyxCnOpOrderService.savePdOrdersNew:{}",JSON.toJSONString(editCnOrdList));
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord=:pkCnord ",editCnOrdList);
            DataBaseHelper.batchUpdate("update cn_order set code_supply_add=:codeSupplyAdd, days=:days, pk_pres=:pkPres, ts=:ts,ordsn=:ordsn,ordsn_parent=:ordsnParent,pk_ord=:pkOrd,code_ord=:codeOrd,name_ord = :nameOrd,dosage=:dosage,pk_unit_dos=:pkUnitDos,code_supply=:codeSupply,code_freq=:codeFreq,pk_dept_exec=:pkDeptExec,pk_org_exec=:pkOrgExec,note_supply=:noteSupply,note_ord=:noteOrd,code_ordtype=:codeOrdtype,spec=:spec,pk_unit=:pkUnit,eu_st=:euSt,desc_ord=:descOrd,quan=:quan,quan_cg=:quanCg,pk_unit_cg=:pkUnitCg,flag_pivas=:flagPivas,name_emp_ord=:nameEmpOrd,pk_emp_ord=:pkEmpOrd,date_enter=:dateEnter,DT_HPPROP=:dtHpprop,price_cg=:priceCg,groupno=:groupno,pk_dept_job=:pkDeptJob,PK_DEPT_AREAAPP=:pkDeptAreaapp,flag_disp=:flagDisp,flag_fit=:flagFit,desc_fit=:descFit,flag_self=:flagSelf,plan_occ_num=:planOccNum,pack_size=:packSize,flag_sp_unit=:flagSpUnit,eu_injury=:euInjury,EU_EASON_DIS=:euEasonDis where pk_cnord = :pkCnord ", editCnOrdList);
            DataBaseHelper.batchUpdate("update CN_PRESCRIPTION set DT_PRESTYPE=:dtPrestype,note=:notePres where pk_pres=:pkPres", editCnOrdList);
            for (CnOrder ord : editCnOrdList){
                pkCnords.add(ord.getPkCnord());
            }

        }
        //??????????????????
        if (addCnOrdList != null && addCnOrdList.size() > 0) {
            //?????????????????????
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord in ( select pk_cnord from cn_order ord where ord.ordsn=:ordsn)", addCnOrdList);
            logger.error("SyxCnOpOrderService.savePdOrdersNew:{}",JSON.toJSONString(addCnOrdList));
            DataBaseHelper.batchUpdate("delete from cn_order where ordsn=:ordsn ", addCnOrdList);
            //????????????
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), addCnOrdList);
            List<CnOrder> ordNewAdds=new ArrayList<>();
            for (CnOrder ord : addCnOrdList){
                pkCnords.add(ord.getPkCnord());
                ordNewAdds.add(ord);
            }
            //??????CA????????????
            cnPubService.caRecordByOrd(false, ordNewAdds);
        }
        List<Map<String, Object>> delMapList = null;
        if (deletingList != null && deletingList.size() > 0) {
            int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord in (" + getPkCnordStr(deletingList) + " ) and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{});
            if (hadSettleCgCount > 0) {
                throw new BusException("??????????????????????????????????????????????????????????????????????????????");
            }
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord = :pkCnord ", deletingList);
            logger.error("SyxCnOpOrderService.savePdOrdersNew:{}",JSON.toJSONString(deletingList));
            DataBaseHelper.batchUpdate("delete from cn_order where pk_cnord = :pkCnord ", deletingList);
        }
        if (delItemsByCnord.size() > 0) {
            List<BlOpDt> blOpDts = new ArrayList<BlOpDt>();
            for (String pk : delItemsByCnord) {
                BlOpDt dt = new BlOpDt();
                dt.setPkCnord(pk);
                blOpDts.add(dt);
            }
            DataBaseHelper.batchUpdate("delete from bl_op_dt where pk_cnord=:pkCnord and flag_settle='0'", blOpDts);
        }
        if (blOpItem.size() > 0) {
            List<BlPubParamVo> blOpItemNew=new ArrayList<BlPubParamVo>();
            for (BlPubParamVo vo : blOpItem){
                if(pkCnords.contains(vo.getPkCnord())){
                    blOpItemNew.add(vo);
                }
            }
            logger.info("?????????????????????{}",JSON.toJSONString(blOpItemNew));
            opCgPubService.blOpCgBatch(blOpItemNew);
        }
        //???????????????cn_prescription
        if (presNoList.size() > 0) {
            Map<String, Object> presMap = new HashMap<String, Object>();
            presMap.put("presNo", presNoList);
            //??????????????????????????????????????????????????????
            List<Map<String, Object>> ordPres = DataBaseHelper.queryForList("select pres.pres_no,count(cn.pk_pres) ord_num from cn_prescription pres left join  cn_order cn on cn.pk_pres = pres.pk_pres and cn.eu_pvtype!='3' where pres.pres_no in(:presNo) and pres.pres_no is not null    group by pres.pres_no ", presMap);
            DataBaseHelper.batchUpdate("delete from cn_prescription where pres_no in (:presNo)", ordPres.stream().filter(vo -> 0==MapUtils.getIntValue(vo,"ordNum")).collect(Collectors.toList()));
        }
        //??????????????????????????????????????????,??????????????????
        /*?????????????????????????????????
        if (!"true".equals(ApplicationUtils.getPropertyValue("cn.press.enable", ""))) {
            //1.???????????????????????????????????????DtHpprop???2.???????????????????????????
            List<String> presDtModSql = new ArrayList<>();
            if (presDtHpprop.size() > 0) {
                Iterator<String> iterator = presDtHpprop.keySet().iterator();
                String tempSql = "update %s set DT_HPPROP='%s' where PK_PRES='%s' and not exists(select 1 from BL_OP_DT dt where dt.PK_PRES='%s' and dt.FLAG_SETTLE='1')";
                while (iterator.hasNext()) {
                    String pkPres = iterator.next();
                    String dtHpprop = presDtHpprop.get(pkPres);
                    if (StringUtils.isBlank(dtHpprop)) {
                        dtHpprop = MapUtils.getString(DataBaseHelper.queryForMap("select DT_HPPROP from CN_PRESCRIPTION where PK_PRES=?", new Object[]{pkPres}), "dtHpprop");
                    } else {
                        presDtModSql.add(String.format(tempSql, "CN_PRESCRIPTION", dtHpprop, pkPres, pkPres));
                    }
                    if (StringUtils.isNotBlank(dtHpprop)) {
                        presDtModSql.add(String.format(tempSql, "CN_ORDER", dtHpprop, pkPres, pkPres));
                    }
                }
                DataBaseHelper.batchUpdate(presDtModSql.toArray(new String[0]));
            }
        }
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("control", "OK");
        mapParam.put("OMPtype", "WesternMedicine");
        mapParam.put("addCnOrdList", addCnOrdList);
        mapParam.put("editCnOrdList", editCnOrdList);
        mapParam.put("delMapList", delMapList);
        PlatFormSendUtils.sendOpO09Msg(mapParam);*/
    }

    private SyxBlOpDt qryYdBl(SyxCnOrderVo ord, List<BdSupply> supplyList, BdItem ydBl) {
        if (ydBl == null || ord == null) return null;
        if (supplyList == null || supplyList.size() == 0) return null;
        boolean ifKf = false;
        for (BdSupply supply : supplyList) {
            if (supply.getCode().equals(ord.getCodeSupply())) {
                ifKf = true;
                break;
            }
        }
        if (!ifKf) return null;
        SyxBlOpDt opDt = new SyxBlOpDt();
        opDt.setPkCgop(NHISUUID.getKeyId());
        opDt.setPkItem(ydBl.getPkItem());
        opDt.setPkUnit(ydBl.getPkUnit());
        opDt.setNameCg(ydBl.getName());
        opDt.setSpec(ydBl.getSpec());
        opDt.setPkPv(ord.getPkPv());
        opDt.setPkPi(ord.getPkPi());
        opDt.setQuanCg(1.00);
        opDt.setEuAdditem("4");
        opDt.setPkPres(ord.getPkPres());
        opDt.setPkCnord(ord.getPkCnord());
        opDt.setPkOrgEx(ord.getPkOrgExec());
        opDt.setPkDeptEx(ord.getPkDeptExec());
        opDt.setPkOrgApp(ord.getPkOrg());
        opDt.setPkDeptApp(ord.getPkDept());
        opDt.setPkEmpApp(ord.getPkEmpOrd());
        opDt.setNameEmpApp(ord.getNameEmpOrd());
        opDt.setPkDeptJob(ord.getPkDeptJob());
        opDt.setPkDeptAreaapp(ord.getPkDeptAreaapp());//??????????????????????????????????????????
        return opDt;
    }
    /************************************???????????? end***********************************/
    /***********************************???????????? start***********************************/
    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> qryZeroPd(String param, IUser user) {
        Map<String, Object> opOrdVo = JsonUtil.readValue(param, Map.class);
        if (opOrdVo == null || StringUtils.isEmpty(MapUtils.getString(opOrdVo, "pkPi"))) {
            throw new BusException("??????????????????????????????????????????");
        }
        if (StringUtils.isEmpty(MapUtils.getString(opOrdVo, "pkPd"))) {
            throw new BusException("?????????????????????????????????????????????");
        }
        User u = (User) user;
        //????????????
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("pkOrg", u.getPkOrg());
        rtnMap.put("pkPi", MapUtils.getString(opOrdVo, "pkPi"));
        rtnMap.put("pkPd", MapUtils.getString(opOrdVo, "pkPd"));
        rtnMap.put("pkItem", MapUtils.getString(opOrdVo, "pkPd"));
        rtnMap = qryZero(rtnMap);
        return rtnMap;
    }

    private Map<String, Object> qryZero(Map<String, Object> rtnMap) {
        //????????????????????????????????????
        rtnMap.put("ifOpen", "0");
        SyxCnOrderVo vo = new SyxCnOrderVo();
        vo.setPkOrd(MapUtils.getString(rtnMap, "pkPd"));
        List<Map<String, Object>> zero = opOrderDao.qryPdZero(vo);
        if (zero != null && zero.size() > 0) {
            if ("1".equals(MapUtils.getString(zero.get(0), "valAtt"))) {
                rtnMap.put("ifOpen", "1");
            }
        }
        if ("1".equals(MapUtils.getString(rtnMap, "ifOpen"))) {
            //?????????????????????????????????????????????
            List<Map<String, Object>> pdSizes = opOrderDao.qryPdMaxSize(rtnMap);
            if (pdSizes != null && pdSizes.size() > 0) {
                rtnMap.put("size", org.apache.commons.collections.MapUtils.getString(pdSizes.get(0), "amount"));
            }
            //???????????????????????????
            List<Map<String, Object>> piSizes = opOrderDao.qryPiSize(rtnMap);
            if (piSizes != null && piSizes.size() > 0) {
                rtnMap.put("quan", org.apache.commons.collections.MapUtils.getString(piSizes.get(0), "quan"));
            }
        }
        return rtnMap;
    }

    private void checkZeroPd(SyxOpOrderVo opOrderParam, User u) {
        PatInfo patInfo = opOrderParam.getPatInfo();
        List<SyxCnOrderVo> addingList = opOrderParam.getAddingList();
        List<SyxCnOrderVo> deletingList = opOrderParam.getDeletingList();
        List<SyxCnOrderVo> editingList = opOrderParam.getEditingList();
        //?????????????????????
        List<String> pdList = new ArrayList<String>();
        Map<String, Object> zeroPdMap = new HashMap<String, Object>();//?????????????????????
        Map<String, Object> zeroPdMap1 = new HashMap<String, Object>();//???????????????
        /*********?????????????????????************/
        if (addingList != null && addingList.size() > 0) {
            for (SyxCnOrderVo vo : addingList) {
                if (!pdList.contains(vo.getPkOrd())) {
                    pdList.add(vo.getPkOrd());
                }
            }
        }
        if (editingList != null && editingList.size() > 0) {
            for (SyxCnOrderVo vo : editingList) {
                if (!pdList.contains(vo.getPkOrd())) {
                    pdList.add(vo.getPkOrd());
                }
            }
        }
        if (pdList.size() == 0) return;
        //????????????
        zeroPdMap.put("pkPds", pdList);
        zeroPdMap.put("pkOrg", u.getPkOrg());
        zeroPdMap.put("pkPi", patInfo.getPkPi());
        //?????????????????????
        List<Map<String, Object>> zero = opOrderDao.qryPdsZero(zeroPdMap);
        if (zero == null && zero.size() == 0) return;
        /*********???????????????????????????????????????????????????************/
        List<PdZeroVo> pdSizes = opOrderDao.qryPdsMaxSize(zeroPdMap);
        /*********??????????????????******************************/
        if (addingList != null && addingList.size() > 0) {
            for (SyxCnOrderVo vo : addingList) {
                boolean ifKl = false;//???????????????
                boolean ifQx = true;//???????????????
                for (Map<String, Object> vo1 : zero) {
                    if ("1".equals(vo.getFlagDurg()) && StringUtils.isNotEmpty(vo.getPkOrd()) && vo.getPkOrd().equals(MapUtils.getString(vo1, "pkPd"))) {
                        ifKl = true;
                        break;
                    }
                }
                if (ifKl) {
                    if (pdSizes != null && pdSizes.size() > 0) {
                        for (PdZeroVo vo1 : pdSizes) {
                            if (vo1.getPkPd().equals(vo.getPkOrd())) {
                                ifQx = false;
                                break;
                            }
                        }
                    }
                    if (ifQx) {
                        throw new BusException("????????????" + vo.getNameOrd() + "????????????????????????????????????????????????");
                    }
                }
            }
        }
        if (editingList != null && editingList.size() > 0) {
            for (SyxCnOrderVo vo : editingList) {
                boolean ifKl = false;//???????????????
                boolean ifQx = true;//???????????????
                for (Map<String, Object> vo1 : zero) {
                    if ("1".equals(vo.getFlagDurg()) && StringUtils.isNotEmpty(vo.getPkOrd()) && vo.getPkOrd().equals(MapUtils.getString(vo1, "pkPd"))) {
                        ifKl = true;
                        break;
                    }
                }
                if (ifKl) {
                    if (pdSizes != null && pdSizes.size() > 0) {
                        for (PdZeroVo vo1 : pdSizes) {
                            if (vo1.getPkPd().equals(vo.getPkOrd())) {
                                ifQx = false;
                                break;
                            }
                        }
                    }
                    if (ifQx) {
                        throw new BusException("????????????" + vo.getNameOrd() + "????????????????????????????????????????????????");
                    }
                }
            }
        }
        /*********??????????????????????????????????????????************/
        List<Map<String, Object>> pdSizes1 = opOrderDao.qryPiZeroOrd(zeroPdMap);
        //????????????????????????????????????
        if (pdSizes1 != null && pdSizes1.size() > 0) {
            //??????????????????????????????????????????????????????????????????
            if (editingList != null && editingList.size() > 0) {
                for (SyxCnOrderVo vo : editingList) {
                    if (vo != null && StringUtils.isNotEmpty(vo.getPkCnord())) {
                        qryPkCnordByList(pdSizes1, vo.getPkCnord());
                    }
                }
            }
            //????????????????????????????????????????????????????????????????????????
            if (deletingList != null && deletingList.size() > 0) {
                for (SyxCnOrderVo vo : deletingList) {
                    if (vo != null && StringUtils.isNotEmpty(vo.getPkCnord())) {
                        qryPkCnordByList(pdSizes1, vo.getPkCnord());
                    }
                }
            }
        }
        //????????????
        if (pdSizes != null && pdSizes.size() > 0) {
            if (pdSizes1 != null && pdSizes1.size() > 0) {
                sumPdSize(pdSizes, pdSizes1, null, false); //??????????????????
            }
            if (addingList != null && addingList.size() > 0) {
                sumPdSize(pdSizes, null, addingList, true); //????????????
            }
            if (editingList != null && editingList.size() > 0) {
                sumPdSize(pdSizes, null, editingList, true); //?????????????????????
            }
            //??????
            for (PdZeroVo vo : pdSizes) {
                if (vo.getAmount() < vo.getSumQuan()) {
                    throw new BusException("????????????" + vo.getName() + "????????????????????????" + vo.getAmount() + "??????????????????" + vo.getSumQuan() + "????????????????????????");
                }
            }
        }
    }

    //???????????????list????????????????????????pkCnord
    private void qryPkCnordByList(List<Map<String, Object>> list, String pkCnord) {
        if (list == null || list.size() == 0) return;
        if (StringUtils.isEmpty(pkCnord)) return;
        for (Map<String, Object> vo : list) {
            if (vo == null || StringUtils.isEmpty(MapUtils.getString(vo, "pkCnord")))
                continue;
            if (pkCnord.equals(MapUtils.getString(vo, "pkCnord"))) {
                vo.put("del", "1");//???????????????
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param pdZeroVos ??????
     * @param pdSizes1  ?????????/????????????
     * @param list      ??????/????????????
     * @param ifAdd     ??????/??????
     */
    private void sumPdSize(List<PdZeroVo> pdZeroVos, List<Map<String, Object>> pdSizes1, List<SyxCnOrderVo> list, boolean ifAdd) {
        if (pdZeroVos == null || pdZeroVos.size() == 0) return;
        if (pdSizes1 != null && pdSizes1.size() > 0) {
            for (PdZeroVo vo : pdZeroVos) {
                for (Map<String, Object> item : pdSizes1) {
                    String pkPd = MapUtils.getString(item, "pkPd");
                    String del = MapUtils.getString(item, "del");
                    if (vo.getPkPd().equals(pkPd) && "0".equals(del)) {
                        Double sumQuan = vo.getSumQuan() + Double.parseDouble(MapUtils.getString(item, "quan"));
                        vo.setSumQuan(sumQuan);
                    }
                }
            }
        }
        if (list != null && list.size() > 0) {
            for (PdZeroVo vo : pdZeroVos) {
                for (SyxCnOrderVo item : list) {
                    String pkPd = item.getPkOrd();
                    if (vo.getPkPd().equals(pkPd)) {
                        Double sumQuan = vo.getSumQuan();
                        if (ifAdd) {
                            sumQuan = sumQuan + item.getQuanCg();
                        } else {
                            sumQuan = sumQuan - item.getQuanCg();
                        }
                        vo.setSumQuan(sumQuan);
                    }
                }
            }
        }
    }
    /***********************************???????????? end***********************************/


    /**
     * ??????????????????????????????????????????
     * @param param
     * @return string 0 ??? 1???
     */
    public String getFlagSettle(String param, IUser user){
        String presNo = JsonUtil.getFieldValue(param,"presNo");
        if (StringUtils.isBlank(presNo)){
            return "0";
        }
        List<Map<String, String>> flagSettle = opOrderDao.getFlagSettle(presNo);
        if (CollectionUtils.isNotEmpty(flagSettle)){
        if (flagSettle.stream().map(v -> v.get("FLAGSETTLE")).anyMatch(e -> e.contains("1"))){
            return "1";
        }
        }
        return "0";
    }

}
