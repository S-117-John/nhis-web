package com.zebone.nhis.cn.opdw.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.zebone.nhis.bl.pub.syx.dao.IpSettleGzgyMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.opcg.vo.OpCgCnOrder;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.vo.BlOpDrugStorePriceInfo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.cn.opdw.dao.CnOpHerbPresMapper;
import com.zebone.nhis.cn.opdw.vo.CnPresDt;
import com.zebone.nhis.cn.opdw.vo.HerbOrdSetDt;
import com.zebone.nhis.cn.opdw.vo.HerbPresDt;
import com.zebone.nhis.cn.opdw.vo.PiPresInfo;
import com.zebone.nhis.cn.opdw.vo.PiPresInfoDt;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSet;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSetDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import javax.annotation.Resource;


/**
 * 门诊医生站--草药处方服务
 *
 * @author Roger
 */
@Service
public class CnOpHerbPresService {

    public static String NEW = "N";
    public static String UPDATE = "U";
    public static String DELETE = "R";
    public static String LOAD = "L";

    @Autowired
    private CnOpHerbPresMapper cnOpHerbPresMapper;
    @Autowired
    private BdSnService bdSnService;
    @Autowired
    private PdStOutPubService pdStOutPubService;
    @Autowired
    private OpCgPubService opCgPubService;
    @Autowired
    private PriceStrategyService priceStrategyService;
    @Resource
    private IpSettleGzgyMapper ipSettleGzgyMapper;
    DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    Date tsOld = null;

    /**
     * 草药处方查询
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnPrescription> qryPres(String param, IUser user) {
        Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String, String>>() {
        });
        String pkPv = paramMap.get("pkPv");

        List<CnPrescription> res = new ArrayList<CnPrescription>();

        if (!CommonUtils.isEmptyString(pkPv)) {
            res = cnOpHerbPresMapper.getHerbPres(pkPv);
        }
        return res;
    }

    /**
     * 草药处方查询
     *
     * @param param
     * @param user
     * @return
     */
    public List<CnPresDt> qryPresDt(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPres = paramMap.get("pkPres") != null && paramMap.get("pkPres") != "" ? paramMap.get("pkPres").toString() : null;
        List<String> pkPdList = paramMap.containsKey("pkPdList") ? (List<String>) paramMap.get("pkPdList") : null;

        List<CnPresDt> res = new ArrayList<CnPresDt>();

        if (!CommonUtils.isEmptyString(pkPres)) {
            res = cnOpHerbPresMapper.getHerbPresDt(pkPres, pkPdList);
        }
        return res;
    }

    /**
     * 查询草药医嘱模板
     *
     * @param param
     * @param user
     * @return
     */
    public List<BdOrdSet> qryOrdSet(String param, IUser user) {

        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<BdOrdSet> res = new ArrayList<BdOrdSet>();

        if (paramMap != null) {
            res = cnOpHerbPresMapper.getOrdSet(paramMap);
        }

        return res;
    }

    /**
     * 查询草药医嘱模板明细
     *
     * @param param
     * @param user
     * @return
     */
    public List<HerbOrdSetDt> qryOrdSetDt(String param, IUser user) {

        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<HerbOrdSetDt> res = new ArrayList<HerbOrdSetDt>();

        if (paramMap != null) {
            res = cnOpHerbPresMapper.getOrdSetDt(paramMap);
        }

        return res;
    }


    /**
     * 保存草药处方
     *
     * @param param
     * @param user
     * @return
     */
    public void saveHerbPres(String param, IUser user) {
        CnPrescription pres = JsonUtil.readValue(param, new TypeReference<CnPrescription>() {
        });
        if (pres != null) {
            if (StringUtils.isNotEmpty(pres.getPkPres())) {
                BlOpDt flagSettle = DataBaseHelper.queryForBean("select flag_settle from bl_op_dt where  pk_pres =? ",
                        BlOpDt.class, pres.getPkPres());
                if (flagSettle.getFlagSettle().equals("1")) {
                    throw new BusException("处方已收费，不允许修改！");
                }
            }
            if (NEW.equals(pres.getRowStatus())) {
                insertPres(pres, user);
            } else if (UPDATE.equals(pres.getRowStatus())) {
                updatePres(pres, user);
            } else {

            }
        }
    }

    private void updatePres(CnPrescription pres, IUser user) {

        //获取操作数据库前的Ts
        tsOld = pres.getTs();
        if (tsOld == null) {
            throw new BusException("未获取到时间戳Ts！");
        }
        //获取此刻处方Ts
        List<CnPrescription> queryForList = DataBaseHelper.queryForList("select * from CN_PRESCRIPTION where pk_pres=? and del_flag='0'", CnPrescription.class, pres.getPkPres());

        if (queryForList == null || queryForList.size() == 0 || queryForList.get(0).getTs() == null || !(format.format(queryForList.get(0).getTs()).equals(format.format(tsOld))))
            throw new BusException("数据被其他用户修改,刷新后请重新处理！");

        DataBaseHelper.updateBeanByPk(pres, false);
        List<HerbPresDt> dt = pres.getHerbDt();
        StringBuilder nameOrd = new StringBuilder("");
        int nameSize = dt.size() - 1;
        if (CollectionUtils.isNotEmpty(dt)) {
            for (int i = 0; i < dt.size() && i < 3; ++i) {
                HerbPresDt vo = dt.get(i);
                nameOrd.append(vo.getName());
                if (i == nameSize || i == 2) {
                    break;
                }
                nameOrd.append("+");
            }
        }
        DataBaseHelper.execute(" update cn_order set ords = ?,name_ord=? where pk_pres = ?", pres.getHerbords(), nameOrd.toString(), pres.getPkPres());

        List<HerbPresDt> news = new ArrayList<HerbPresDt>();
        List<CnOrder> ords = DataBaseHelper.queryForList("select * from cn_order where pk_pres = ?", CnOrder.class, pres.getPkPres());
        for (HerbPresDt vo : pres.getHerbDt()) {
            if (DELETE.equals(vo.getStatusData())) {
                delHerbPresDrug(pres.getPkPres(), vo);
            } else if (NEW.equals(vo.getStatusData())) {
                news.add(vo);
            } else if (LOAD.equals(vo.getStatusData())) {
                news.add(vo);
                delHerbPresDrug(pres.getPkPres(), vo);
            }
        }
        PvEncounter pv = DataBaseHelper.queryForBean(" Select * From pv_Encounter where pk_pv = ? ", PvEncounter.class, pres.getPkPv());
        insertDrug(pres.getPkPres(), news, ords.get(0), pv);
    }

    private void insertPres(CnPrescription pres, IUser user) {
        List<HerbPresDt> dt = pres.getHerbDt();

        //1.处方信息
        pres.setPkOrg(UserContext.getUser().getPkOrg());
        pres.setDtPrestype("02");
        pres.setDatePres(new Date());
        List<PvDiag> diag = cnOpHerbPresMapper.getPvDiagListByPkPv(pres.getPkPv());
        pres.setPkDiag(diag != null && diag.size() > 0 ? diag.get(0).getPkDiag() : null);
        ApplicationUtils.setDefaultValue(pres, true);
        pres.setModityTime(null);
        DataBaseHelper.insertBean(pres);

        //2.医嘱信息
        CnOrder ord = new CnOrder();
        ord.setPkOrg(UserContext.getUser().getPkOrg());
        PvEncounter pv = DataBaseHelper.queryForBean("Select * From pv_encounter where pk_pv =? ", PvEncounter.class, pres.getPkPv());
        ord.setEuPvtype(pv.getEuPvtype());
        ord.setPkPv(pv.getPkPv());
        ord.setPkPi(pv.getPkPi());
        String code_Cn00004 = ApplicationUtils.getSysparam("CN0004", false);
        if (code_Cn00004 == null)
            throw new BusException("当前机构的参数【CN0004】未维护，处方有效天数");
        ord.setDateStart(pres.getDatePres());
        ord.setDateEnter(pres.getDatePres());
        ord.setDateEffe(DateUtils.getSpecifiedDay(ord.getDateStart(), Integer.parseInt(code_Cn00004)));
        ord.setCodeOrdtype("0103");
        ord.setEuAlways("1");
        ord.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, user));
        ord.setOrdsnParent(ord.getOrdsn());
        ord.setPkOrd("~");
        ord.setCodeOrd("DEF99999");
        ord.setPkPres(pres.getPkPres());
        StringBuilder nameOrd = new StringBuilder("");
        if (CollectionUtils.isNotEmpty(dt)) {
            for (int i = 0; i < dt.size() && i < 3; ++i) {
                HerbPresDt vo = dt.get(i);
                nameOrd.append("+");
                nameOrd.append(vo.getName());
            }
        }
        ord.setNameOrd(nameOrd.toString());
        ord.setOrds(pres.getHerbords());
        ord.setFlagFirst("0");
        ord.setLastNum(0l);
        ord.setPkDeptExec(pres.getPkDeptExec());
        ord.setPkOrgExec(pres.getPkOrgExec());
        ord.setEuStatusOrd("1");
        ord.setFlagBase("0");
        ord.setFlagDurg("1");
        ord.setFlagSelf("0");
        ord.setFlagNote("0");
        ord.setFlagBl("1");
        ord.setPkDept(UserContext.getUser().getPkDept());
        ord.setPkWg(null);//医生所在医疗小组；
        ord.setPkEmpInput(UserContext.getUser().getPkEmp());
        ord.setNameEmpInput(UserContext.getUser().getNameEmp());
        ord.setPkEmpOrd(pres.getPkEmpOrd());
        ord.setNameEmpOrd(pres.getNameEmpOrd());
        ord.setFlagSign("1");
        ord.setDateSign(pres.getDatePres());
        ord.setFlagStop("0");
        ord.setFlagStopChk("0");
        ord.setFlagErase("0");
        ord.setFlagEraseChk("0");
        ord.setFlagCp("0");
        ord.setFlagDoctor("0");
        ord.setFlagPrint("0");
        ord.setInfantNo(0);
        ord.setFlagPrev("0");
        ord.setFlagMedout("0");
        ord.setFlagFit("0");
        ord.setFlagEmer("0");
        ord.setFlagThera("0");
        ord.setPkDept(pres.getPkDept());
        ord.setDays(1l);//天数默认设置为1
        ord.setDosage(0.0d);//用量默认为0
        ApplicationUtils.setDefaultValue(ord, true);
        ord.setModityTime(null);
        DataBaseHelper.insertBean(ord);
        insertDrug(pres.getPkPres(), dt, ord, pv);

    }

    private void insertDrug(String pkPres, List<HerbPresDt> dt,
                            CnOrder ord, PvEncounter pv) {
        //3.保存处方药品明细（多条记录），写表cn_ord_herb；
        List<CnOrdHerb> herbs = new ArrayList<CnOrdHerb>();
        List<OpCgCnOrder> cgCnOrders = new ArrayList<OpCgCnOrder>();
        if (CollectionUtils.isNotEmpty(dt)) {
            for (HerbPresDt vo : dt) {
                CnOrdHerb herb = new CnOrdHerb();
                herb.setPkCnord(ord.getPkCnord());
                herb.setSortNo(vo.getSortNo());
                herb.setPkPd(vo.getPkPd());
                herb.setPrice(vo.getPrice());
                herb.setPkUnit(vo.getPkUnit());
                herb.setQuan(vo.getQuan());
                herb.setNoteUse(vo.getNoteUse());
                herb.setEuHerbtype("1");
                ApplicationUtils.setDefaultValue(herb, true);
                herb.setModityTime(null);
                herbs.add(herb);

                OpCgCnOrder cgCnOrder = new OpCgCnOrder();
                cgCnOrder.setPkOrd(herb.getPkPd());
                cgCnOrder.setQuanCg(MathUtils.mul(herb.getQuan(), ord.getOrds().doubleValue() == 0.0 ? 1 : ord.getOrds().doubleValue()));
                cgCnOrder.setNameOrd(vo.getName());
                cgCnOrder.setPkUnitCg(herb.getPkUnit());
                cgCnOrder.setPackSize(1D);
                cgCnOrder.setOrds(ord.getOrds());
                cgCnOrders.add(cgCnOrder);
            }
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrdHerb.class), herbs);
        //4.生成记费明细
        for (OpCgCnOrder opCgCnOrder : cgCnOrders) {
            opCgCnOrder.setPkCnord(ord.getPkCnord());
            opCgCnOrder.setPkPres(pkPres);
            opCgCnOrder.setPkDeptExec(ord.getPkDeptExec());
            opCgCnOrder.setPkOrgExec(ord.getPkOrgExec());
            // 调用供应链询价服务
            BlOpDrugStorePriceInfo blOpDrugStorePriceInfo = new BlOpDrugStorePriceInfo();
            blOpDrugStorePriceInfo.setPkPd(opCgCnOrder.getPkOrd());
            blOpDrugStorePriceInfo.setPkDept(opCgCnOrder.getPkDeptExec());
            blOpDrugStorePriceInfo.setQuanAp(opCgCnOrder.getQuanCg());
            blOpDrugStorePriceInfo.setPkOrg(UserContext.getUser().getPkOrg());
            blOpDrugStorePriceInfo.setNamePd(opCgCnOrder.getNameOrd());
            blOpDrugStorePriceInfo = priceStrategyService.queryDrugStorePriceInfo(blOpDrugStorePriceInfo);
            opCgCnOrder.setDateExpire(blOpDrugStorePriceInfo.getDateExpire());
            opCgCnOrder.setPriceCg(blOpDrugStorePriceInfo.getPrice());
            opCgCnOrder.setPriceCost(blOpDrugStorePriceInfo.getPriceCost());
            opCgCnOrder.setBatchNo(blOpDrugStorePriceInfo.getBatchNo());
            opCgCnOrder.setPackSize(blOpDrugStorePriceInfo.getPackSize().doubleValue());
        }

        if (CollectionUtils.isNotEmpty(cgCnOrders)) {
            drugOpCg(cgCnOrders, pv);
        }
    }

    /**
     * 删除草药处方药品
     *
     * @param param
     * @param user
     */
    public void delHerbPresDrug(String pkPres, HerbPresDt dt) {


        // 1.删除关联记费明细
        int ctn = DataBaseHelper.execute("delete from bl_op_dt  where pk_pres=? and"
                + " pk_pd=? and flag_acc=0 and flag_settle=0 and    to_char(ts,'yyyy-mm-dd hh24:mi:ss')=?", pkPres, dt.getPkPd(), dt.getTstr());
        if (ctn < 1) {

            throw new BusException("数据已被其他用户修改，刷新后请重新处理！");
        }
        //2.删除医嘱
        DataBaseHelper.execute(" delete from cn_ord_herb  where pk_pd=?"
                        + "	  and exists (select 1 from cn_order ord  where cn_ord_herb.pk_cnord=ord.pk_cnord and ord.pk_pres=?)",
                dt.getPkPd(), pkPres);

    }

    /**
     * 查询数据是否已经被别的用户修改
     *
     * @param param
     * @param user
     */
    public List<CnPrescription> queryDataIsModified(String param, IUser user) {
        CnPrescription pres = JsonUtil.readValue(param, new TypeReference<CnPrescription>() {
        });
        String pkPres = pres.getPkPres();

        //获取操作数据库前的Ts
        tsOld = pres.getTs();
        if (tsOld == null) {
            throw new BusException("未获取到时间戳Ts！");
        }
        List<CnPrescription> list = new ArrayList<>();

        //获取此刻处方Ts
        list = DataBaseHelper.queryForList("select * from CN_PRESCRIPTION where pk_pres=? and del_flag='0'", CnPrescription.class, pkPres);

        return list;
    }

    /**
     * 删除草药处方
     *
     * @param param
     * @param user
     */
    public void delHerbPres(String param, IUser user) {

        CnPrescription pres = JsonUtil.readValue(param, new TypeReference<CnPrescription>() {
        });
        String pkPres = pres.getPkPres();
        List<HerbPresDt> dts = pres.getHerbDt();

        //获取操作数据库前的Ts
        tsOld = pres.getTs();
        if (tsOld == null) {
            throw new BusException("未获取到时间戳Ts！");
        }
        //获取此刻处方Ts
        List<CnPrescription> queryForList = DataBaseHelper.queryForList("select * from CN_PRESCRIPTION where pk_pres=? and del_flag='0'", CnPrescription.class, pkPres);

        if (queryForList == null || queryForList.size() == 0 || queryForList.get(0).getTs() == null || !(format.format(queryForList.get(0).getTs()).equals(format.format(tsOld))))
            throw new BusException("数据已被其他用户修改,刷新后请重新处理！");

        for (HerbPresDt vo : dts) {
            //1.删除关联记费明细
            int ctn = DataBaseHelper.execute("delete from bl_op_dt  where pk_item=? and "
                            + " flag_acc=0 and flag_settle=0 and  to_char(ts,'yyyy-mm-dd hh24:mi:ss')=? ",
                    vo.getPkPd(), vo.getTstr());
            if (ctn < 1) {
                throw new BusException("数据已被其他用户修改，刷新后请重新处理！");
            }
        }


        //2.删除处方下的药品明细
        DataBaseHelper.execute("delete from cn_ord_herb  "
                        + "	where exists(select 1  from cn_order ord where cn_ord_herb.pk_cnord=ord.pk_cnord and  ord.pk_pres=?)",
                pkPres);

        //3.删除医嘱
        DataBaseHelper.execute("delete from cn_order  where pk_pres=?", pkPres);

        //4.删除处方
        DataBaseHelper.execute(" delete from cn_prescription  where pk_pres=?", pkPres);

    }

    /**
     * 通用记费方法
     *
     * @param occo
     * @param pvInfo
     */
    private void drugOpCg(List<OpCgCnOrder> occo, PvEncounter pvInfo) {

        User u = UserContext.getUser();
        String pkOrg = u.getPkOrg();
        String pkDept = u.getPkDept();
        List<BlPubParamVo> bods = new ArrayList<BlPubParamVo>();
        for (OpCgCnOrder opCgCnOrder : occo) {
            BlPubParamVo bpb = new BlPubParamVo();
            bpb.setPkOrg(pkOrg);
            bpb.setEuPvType(pvInfo.getEuPvtype());
            bpb.setPkPv(pvInfo.getPkPv());
            bpb.setPkPi(pvInfo.getPkPi());
            bpb.setPkOrd(opCgCnOrder.getPkOrd());
            bpb.setPkCnord(opCgCnOrder.getPkCnord());
            bpb.setPkPres(opCgCnOrder.getPkPres());
            bpb.setPkItem(null);
            bpb.setQuanCg(opCgCnOrder.getQuanCg());
            bpb.setPkOrgEx(opCgCnOrder.getPkOrgExec());
            bpb.setPkOrgApp(pvInfo.getPkOrg());
            bpb.setPkDeptEx(opCgCnOrder.getPkDeptExec());
            bpb.setPkDeptApp(pvInfo.getPkDept());
            bpb.setPkEmpApp(opCgCnOrder.getPkEmpOrd());
            bpb.setNameEmpApp(opCgCnOrder.getNameEmpOrd());
            bpb.setFlagPd(opCgCnOrder.getPkPres() == null ? "0" : "1");
            bpb.setNamePd(opCgCnOrder.getNameOrd());
            bpb.setFlagPv("0");
            bpb.setDateExpire(opCgCnOrder.getDateExpire());
            bpb.setPkUnitPd(opCgCnOrder.getPkUnitCg());
            bpb.setPackSize(opCgCnOrder.getPackSize().intValue());
            bpb.setPrice(opCgCnOrder.getPriceCg());
            bpb.setPriceCost(opCgCnOrder.getPriceCost());
            bpb.setDateHap(new Date());
            bpb.setPkDeptCg(pkDept);
            bpb.setPkEmpCg(u.getPkEmp());
            bpb.setNameEmpCg(u.getNameEmp());
            bods.add(bpb);
        }
        // 批量记费
        //opcgPubService.blOpCg(bods);  lijipeng 注释，改为批量模式
        opCgPubService.blOpCgBatch(bods);
    }

    /**
     * 查询患者可复制的处方
     *
     * @param param
     * @param user
     * @return
     */
    public List<PiPresInfo> getCopyPres(String param, IUser user) {
        PiPresInfo para = JsonUtil.readValue(param, new TypeReference<PiPresInfo>() {
        });
        if (Application.isSqlServer()) {
            return para != null ? cnOpHerbPresMapper.getCopyPres(para) : null;
        } else {
            return para != null ? cnOpHerbPresMapper.getCopyPresOracle(para) : null;
        }

    }

    /**
     * 查询患者可复制的处方明细
     *
     * @param param
     * @param user
     * @return
     */
    public List<PiPresInfoDt> getCopyPresDt(String param, IUser user) {
        Map<String, Object> para = JsonUtil.readValue(param, Map.class);
        String pkPres = para.get("pkPres") == null ? null : (String) para.get("pkPres");
        String pkDept = para.get("pkDept") == null ? null : (String) para.get("pkDept");

        List<PiPresInfoDt> res = null;
        if (!CommonUtils.isEmptyString(pkDept) && !CommonUtils.isEmptyString(pkPres)) {
            res = cnOpHerbPresMapper.getCopyPresDt(pkPres, pkDept);
        }
        return res;
    }

    /**
     * 保存草药模板
     *
     * @param param
     * @param user
     * @return
     */
    public BdOrdSet saveOrdSet(String param, IUser user) {
        BdOrdSet vo = JsonUtil.readValue(param, new TypeReference<BdOrdSet>() {
        });
        return saveSet(vo);
    }

    public BdOrdSet saveSet(BdOrdSet vo) {
        if (vo == null || CommonUtils.isEmptyString(vo.getName()) || CommonUtils.isEmptyString(vo.getCode())) {
            return null;
        } else {
            String pk = vo.getPkOrdset();
            int count = 0;
            if ("N".equals(vo.getRowStatus())) {
                //效验编码和名称唯一性
                count = DataBaseHelper.queryForScalar("select count(1) from bd_ord_set where pk_org = ? and (code = ? or name = ?) and del_flag = '0' ", Integer.class, new Object[]{UserContext.getUser().getPkOrg(), vo.getCode(), vo.getName()});
                if (count > 0) {
                    throw new BusException("存在相同编码或名称的模板，请重新输入！");
                }
                ApplicationUtils.setDefaultValue(vo, true);
                pk = vo.getPkOrdset();
                DataBaseHelper.insertBean(vo);
            } else if ("U".equals(vo.getRowStatus())) {
                //效验编码和名称唯一性
                count = DataBaseHelper.queryForScalar("select count(1) from bd_ord_set where pk_org = ? and (code = ? or name = ?) and PK_ORDSET != ? and del_flag = '0'", Integer.class, new Object[]{UserContext.getUser().getPkOrg(), vo.getCode(), vo.getName(), vo.getPkOrdset()});
                if (count > 0) {
                    throw new BusException("存在相同编码或名称的模板，请重新输入！");
                }

                ApplicationUtils.setDefaultValue(vo, false);
                DataBaseHelper.updateBeanByPk(vo, false);
            } else if ("R".equals(vo.getRowStatus())) {
                DataBaseHelper.deleteBeanByPk(vo);
                pk = null;
            }
            return DataBaseHelper.queryForBean("Select * from bd_ord_set where pk_ordset = ?", BdOrdSet.class, pk);
        }
    }

    /**
     * 保存草药模板
     *
     * @param param
     * @param user
     * @return
     */
    public void saveOrdSetDt(String param, IUser user) {
        List<BdOrdSetDt> vos = JsonUtil.readValue(param, new TypeReference<List<BdOrdSetDt>>() {
        });
        saveSetDt(vos);
    }

    public void saveSetDt(List<BdOrdSetDt> vos) {
        if (vos == null || vos.size() < 1) {
            return;
        } else {
            for (BdOrdSetDt vo : vos) {
                if ("N".equals(vo.getRowStatus())) {
                    ApplicationUtils.setDefaultValue(vo, true);
                    DataBaseHelper.insertBean(vo);
                } else if ("R".equals(vo.getRowStatus())) {
                    DataBaseHelper.deleteBeanByPk(vo);
                } else {
                    ApplicationUtils.setDefaultValue(vo, false);
                    DataBaseHelper.updateBeanByPk(vo, false);
                }
            }
        }
    }

    /**
     * 根据医嘱模板主键查询医嘱模板明细是否存在，如果没有明细信息则删除该模板
     *
     * @param param
     * @param user
     * @return
     */
    public void queryOrdDtForPkOrdSet(String param, IUser user) {
        BdOrdSet vo = JsonUtil.readValue(param, BdOrdSet.class);
        int count = DataBaseHelper.queryForScalar("select count(1) from  bd_ord_set_dt where PK_ORDSET = ?", Integer.class, vo.getPkOrdset());
        if (count <= 0) {
            DataBaseHelper.deleteBeanByPk(vo);
        }
    }


    /**
     * 获取药品属性-是否限制等状态
     * 004003010014
     */
    public String queryHerbalAttr(String param, IUser user) {
        List<String> pkPds = JsonUtil.readValue(param, ArrayList.class);
        if (CollectionUtils.isEmpty(pkPds)) {
            throw new BusException("草药参数尚未传过来！");
        }
        List<Map<String, String>> herbaList = ipSettleGzgyMapper.queryHerbalAttr(pkPds);
        if (CollectionUtils.isEmpty(herbaList)) {
           return "01";
        }

        Set<String> limit = new HashSet<>();
        Set<String> yibao = new HashSet<>();
        Set<String> zifei = new HashSet<>();

        for (Map<String, String> map : herbaList) {
            //  不是限制的
            if (map.get("aka036").equals("0")) {
                // 自费
                if (map.get("bkm032").equals("99")) {
                    zifei.add(map.get("pkItem"));
                } else {
                    // 医保
                    yibao.add(map.get("pkItem"));
                }

            }
            // 限制
            if (map.get("aka036").equals("1")) {
                limit.add(map.get("pkItem"));
            }

        }

        // 各种自费的条件判断
         if (limit.size() == 0 && yibao.size() == 0 && zifei.size() > 1) {  // 只有自费的==自费
            return "01";
        } else if (limit.size() > 1 && zifei.size() == 0 && yibao.size() == 0) { // 多个限制==自费
            return "01";
        } else if (pkPds.size() == 1) {  // 只有一个时 ==自费
            return "01";
        }
        // 默认是医保
        return "11";

    }


}
