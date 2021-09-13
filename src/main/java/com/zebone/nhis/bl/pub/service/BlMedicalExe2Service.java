package com.zebone.nhis.bl.pub.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.bl.pub.dao.BlMedicalExe2Mapper;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.RisPrintInfoVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOccDt;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pro.zsrm.bl.vo.PageMedListVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 医疗执行服务第二版（含门诊、住院）
 */
@Service
public class BlMedicalExe2Service {

    @Autowired
    private BlIpMedicalExeService blIpMedicalExeService;
    @Autowired
    private OpCgPubService opCgPubService;
    @Resource
    private BlMedicalExe2Mapper blMedicalExe2Mapper;
    @Autowired
    private IpCgPubService ipCgPubService;

    /**
     * 医技业务管理-住院页签-查询功能：查询住院医技申请单
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpMedicalApp(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (CommonUtils.isNotNull(paramMap.get("dateBegin"))) {
            paramMap.put("dateBegin", CommonUtils.getString(paramMap.get("dateBegin")).substring(0, 8) + "000000");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateEnd"))) {
            paramMap.put("dateEnd", CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8) + "235959");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateBeginPlan"))) {
            paramMap.put("dateBeginPlan", CommonUtils.getString(paramMap.get("dateBeginPlan")).substring(0, 8) + "000000");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateEndPlan"))) {
            paramMap.put("dateEndPlan", CommonUtils.getString(paramMap.get("dateEndPlan")).substring(0, 8) + "235959");
        }
        
        if(CommonUtils.isNotNull(paramMap.get("dateBegin")) && CommonUtils.isNotNull(paramMap.get("dateEnd"))) {
        	Date dateBegin = DateUtils.strToDate(MapUtils.getString(paramMap, "dateBegin"));
            Date dateEnd = DateUtils.strToDate(MapUtils.getString(paramMap, "dateEnd"));
            int day = DateUtils.getDateSpace(dateBegin, dateEnd);
            if (day > 31) {
                throw new BusException("按时间查询范围不能超过31天!");
            }
        }
        
        paramMap.put("recentDay", DateUtils.dateToStr("yyyyMMdd", DateUtils.getSpecifiedDay(new Date(), -3)) + "000000");
        List<Map<String, Object>> list = blMedicalExe2Mapper.qryIpMedAppInfo(paramMap);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Map<String, Object>> maps = new ArrayList<>(list.size());
            List<String> pkExoccs = list.stream().map(m -> MapUtils.getString(m, "pkExocc")).distinct().collect(Collectors.toList());
            List<Map<String, Object>> list1 = getMedBlIpdtInfo(pkExoccs);
            boolean notEm = CollectionUtils.isNotEmpty(list1);
            list.forEach(a -> {
                //全部
                if ("9".equals(MapUtils.getString(paramMap, "euCharge"))) {
                    Integer cnt = 0;
                    Integer itemNum = 0;
                    if (notEm) {
                        List<Map<String, Object>> list2 = list1.stream()
                                .filter(p -> MapUtils.getString(a, "pkExocc").equals(MapUtils.getString(p, "pkOrdexdt")))
                                .collect(Collectors.toList());
                        cnt = CollectionUtils.isNotEmpty(list2) ? Double.valueOf(MapUtils.getString(list2.get(0), "cnt")).intValue() : 0;
                        itemNum = CollectionUtils.isNotEmpty(list2) ? ((list2.get(0).get("itemNum") instanceof BigDecimal) ? ((BigDecimal) list2.get(0).get("itemNum")).intValue() : (Integer) list2.get(0).get("itemNum")) : 0;
                    }
                    a.put("cnt", cnt);
                    a.put("itemNum", itemNum);
                    maps.add(a);
                }
                //已收费
                if ("1".equals(MapUtils.getString(paramMap, "euCharge"))) {
                    Integer cnt = 0;
                    Integer itemNum = 0;
                    if (notEm) {
                        List<Map<String, Object>> list2 = list1.stream().filter(p -> MapUtils.getString(a, "pkExocc")
                                .equals(MapUtils.getString(p, "pkOrdexdt")))
                                .collect(Collectors.toList());
                        cnt = CollectionUtils.isNotEmpty(list2) ? Double.valueOf(MapUtils.getString(list2.get(0), "cnt")).intValue() : 0;
                        itemNum = CollectionUtils.isNotEmpty(list2) ? ((list2.get(0).get("itemNum") instanceof BigDecimal) ? ((BigDecimal) list2.get(0).get("itemNum")).intValue() : (Integer) list2.get(0).get("itemNum")) : 0;
                    }
                    boolean isFit = ("1".equals(MapUtils.getString(a, "euStatusOcc")) && cnt == 0) || cnt > 0;
                    if (isFit) {
                        a.put("cnt", cnt);
                        a.put("itemNum", itemNum);
                        maps.add(a);
                    }
                }
                // 未收费
                if ("0".equals(MapUtils.getString(paramMap, "euCharge"))) {
                    Integer cnt = 0;
                    Integer itemNum = 0;
                    if (notEm) {
                        List<Map<String, Object>> list2 = list1.stream().filter(p -> MapUtils.getString(a, "pkExocc")
                                .equals(MapUtils.getString(p, "pkOrdexdt")))
                                .collect(Collectors.toList());
                        cnt = CollectionUtils.isNotEmpty(list2) ? Double.valueOf(MapUtils.getString(list2.get(0), "cnt")).intValue() : 0;
                        itemNum = CollectionUtils.isNotEmpty(list2) ? ((list2.get(0).get("itemNum") instanceof BigDecimal) ? ((BigDecimal) list2.get(0).get("itemNum")).intValue() : (Integer) list2.get(0).get("itemNum")) : 0;
                    }
                    boolean isFit = ("0".equals(MapUtils.getString(a, "euStatusOcc")) && cnt == 0) && itemNum == 0;
                    if (isFit) {
                        a.put("cnt", cnt);
                        a.put("itemNum", itemNum);
                        maps.add(a);
                    }
                }
                //已退费
                if ("2".equals(MapUtils.getString(paramMap, "euCharge"))) {
                    Integer cnt = 0;
                    Integer itemNum = 0;
                    if (notEm) {
                        List<Map<String, Object>> list2 = list1.stream().filter(p -> MapUtils.getString(a, "pkExocc")
                                .equals(MapUtils.getString(p, "pkOrdexdt")))
                                .collect(Collectors.toList());
                        cnt = CollectionUtils.isNotEmpty(list2) ? Double.valueOf(MapUtils.getString(list2.get(0), "cnt")).intValue() : 0;
                        itemNum = CollectionUtils.isNotEmpty(list2) ? ((list2.get(0).get("itemNum") instanceof BigDecimal) ? ((BigDecimal) list2.get(0).get("itemNum")).intValue() : (Integer) list2.get(0).get("itemNum")) : 0;
                    }
                    boolean isFit = cnt == 0 && itemNum > 0;
                    if (isFit) {
                        a.put("cnt", cnt);
                        a.put("itemNum", itemNum);
                        maps.add(a);
                    }
                }
            });
            return maps;
        }
        return list;
    }

    /**
     * @Description sqlServer数据库查询不允许超过2100个
     * @auther wuqiang
     * @Date 2021-04-26
     * @Param [pkExoccs]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    private List<Map<String, Object>> getMedBlIpdtInfo(List<String> pkExoccs){
        List<Map<String, Object>> list1=new ArrayList<>(pkExoccs.size()*2);
       String sql="select nvl(sum(dt.AMOUNT) ,0) cnt, " +
               "        dt.pk_ordexdt, " +
               "        dt.pk_dept_ex, " +
               "        count(1) as item_num " +
               "        from bl_ip_dt dt " +
               "        where  " +
               "            dt.pk_ordexdt in  (" + CommonUtils.convertSetToSqlInPart(new HashSet<>(pkExoccs), "pk_ordexdt")+ ")"+
               "        group by dt.pk_ordexdt, dt.pk_dept_ex";
        list1=DataBaseHelper.queryForList(sql);
        return list1;
    }

    /**
     * 医技业务管理-门诊页签-查询功能：查询门急诊医技申请单
     *
     * @param param
     * @param user
     * @return
     */
    public PageMedListVo queryOpMedicalApp(String param, IUser user) {
        PageMedListVo pageMedListVo = new PageMedListVo();
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (CommonUtils.isNotNull(paramMap.get("dateBegin"))) {
            paramMap.put("dateBegin", CommonUtils.getString(paramMap.get("dateBegin")).substring(0, 8) + "000000");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateEnd"))) {
            paramMap.put("dateEnd", CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8) + "235959");
        }
        Date dateBegin =DateUtils.strToDate(CommonUtils.getString(paramMap.get("dateBegin")));
        Date dateEnd =DateUtils.strToDate(CommonUtils.getString(paramMap.get("dateEnd")));
        int day = DateUtils.getDateSpace(dateBegin,dateEnd);
        if(day>31){
            throw new BusException("按时间查询范围不能超过31天!");
        }
        int pageIndex = CommonUtils.getInteger(CommonUtils.getString(paramMap.get("pageIndex")));
        int pageSize = CommonUtils.getInteger(CommonUtils.getString(paramMap.get("pageSize")));
        // 分页操作
        String pkOrg = UserContext.getUser().getPkOrg();
        paramMap.put("pkOrg",pkOrg);
        MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> list = blMedicalExe2Mapper.qryOpMedAppInfo(paramMap);
        Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
        pageMedListVo.setMedList(list);
        pageMedListVo.setTotalCount(page.getTotalCount());
        return pageMedListVo;
    }

    /**
     * 医技业务管理-住院页签-点击每一行：查询住院医技申请对应费用及执行记录
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> queryIpBlDtAndOcc(String param, IUser user) {
        // Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> paramMaps = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        // //判断费用合计
        // Double totalAmount =
        // DataBaseHelper.queryForScalar("select case when sum(cg.amount) is null then 0 else sum(cg.amount) end totalAmount  from bl_ip_dt cg where cg.pk_cnord = ? and cg.pk_ordexdt = ? ",
        // Double.class, paramMap.get("pkCnord"),paramMap.get("pkExocc"));
        // 查询多条申请单费用合计
        List<String> pkExoccList = Lists.newArrayList();// 医嘱执行单主键集合
        List<String> pkCnordList = Lists.newArrayList();// 医嘱主键集合
        String pkCnordFather = null; // 主医嘱主键
        String pkExoccFather = null; // 医嘱执行单主医嘱
        for (Map<String, Object> temp : paramMaps) {
            pkExoccList.add(temp.get("pkExocc").toString());
            pkCnordList.add(temp.get("pkCnord").toString());
            if ("0".equals(temp.get("isFather").toString())) {// 主医嘱
                pkCnordFather = temp.get("pkCnord").toString();
                pkExoccFather = temp.get("pkExocc").toString();
            }
        }
        Double totalAmount = blMedicalExe2Mapper.getTotalAmount(pkExoccList);
        List<Map<String, Object>> dtlist = null;
        if (totalAmount <= 0D) {
            // 查询费用记录（未记费）
            // dtlist = blMedicalExe2Mapper.qryIpMedBlDtCharge(paramMap);
            // 查询费用记录（未记费）重构
            dtlist = blMedicalExe2Mapper.qryIpMedBlDtCharge_refactor(pkCnordList);
        } else {
            // 查询费用记录（已记费）
            // dtlist = blMedicalExe2Mapper.qryIpMedBlDtInfo(paramMap);
            // 查询费用记录（已记费） 重构
            Map<String, Object> pkListMap = Maps.newHashMap();
            // 查询申请单明细
            pkListMap.put("pkCnordList", pkCnordList);
            pkListMap.put("pkExoccList", pkExoccList);
            dtlist = blMedicalExe2Mapper.qryIpMedBlDtInfoRefactors(pkListMap);
            //查询可以部分退费的正负计费明细
            if (dtlist != null) {
                dtlist.addAll(blMedicalExe2Mapper.qryIpMedBlDtPartialRefundInfo(pkListMap));
            } else {
                dtlist = blMedicalExe2Mapper.qryIpMedBlDtPartialRefundInfo(pkListMap);
            }
        }
        // 查询执行单记录
        // List<Map<String, Object>> occlist =
        // blMedicalExe2Mapper.queryExAssistOccList(paramMap);
        // 查询执行单记录（重构）
        if (pkCnordFather == null || pkExoccFather == null) {
            throw new BusException("未找到该申请单的主申请单，请不要对该条申请单记费！\n请通知管理员检查数据维护和该组申请单的执行科室是否一致！");
        }
        List<Map<String, Object>> occlist = blMedicalExe2Mapper.queryExAssistOccList_refactor(pkCnordFather, pkExoccFather);
        for (Map<String, Object> map : occlist) {
            if (map.get("pkMsp") != null) {
                map.put("pkMsp", map.get("pkMsp").toString().trim());
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        // 费用明细列表
        result.put("dtlist", dtlist);
        // 执行明细列表
        result.put("occlist", occlist);
        return result;
    }

    /**
     * 医技业务管理-门诊页签-点击行：查询门诊医技申请对应费用及执行记录
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> queryOpBlDtAndOcc(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> dtlist = blMedicalExe2Mapper.qryOpMedBlDtInfo(paramMap);
        List<Map<String, Object>> occlist = blMedicalExe2Mapper.queryExAssistOccList(paramMap);
        for (Map<String, Object> map : occlist) {
            if (map.get("pkMsp") != null) {
                map.put("pkMsp", map.get("pkMsp").toString().trim());
            }
        }
        // 费用明细列表
        result.put("dtlist", dtlist);
        // 执行明细列表
        result.put("occlist", occlist);
        return result;
    }

    /**
     * 医疗记录查询-查询功能：查询医技申请单
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryMedicalApp(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> list = new ArrayList<>();
        String euPvtype = CommonUtils.getString(paramMap.get("euPvtype"));
        if (!CommonUtils.isEmptyString(euPvtype)) {
            if (EnumerateParameter.THREE.equals(euPvtype)) // 住院
                list = blMedicalExe2Mapper.qryMedAppInfoIp(paramMap);
            else
                // 门诊
                list = blMedicalExe2Mapper.qryMedAppInfoOp(paramMap);
        } else {
            list.addAll(blMedicalExe2Mapper.qryMedAppInfoIp(paramMap));
            list.addAll(blMedicalExe2Mapper.qryMedAppInfoOp(paramMap));
        }
        return list;
    }

    /**
     * 医疗记录查询-点击行：查询患者对应的医技执行记录
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryIpBlOcc(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> occlist = blMedicalExe2Mapper.queryExAssistOccList(paramMap);
        // 执行明细列表
        return occlist;
    }

    /**
     * 医疗记录查询-取消执行：取消医技执行 医技业务管理-取消执行：取消医技执行
     *
     * @param param
     * @param user
     * @return
     */
    public void cancleExocc(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Map<String, Object> updateMap = new HashMap<>();
        // 执行主键
        updateMap.put("pkAssOccs", (List<String>) paramMap.get("pkAssOccs"));
        updateMap.put("pkEmpCanc", UserContext.getUser().getPkEmp());
        updateMap.put("nameEmpCanc", UserContext.getUser().getNameEmp());
        updateMap.put("dateCanc", DateUtils.dateToStr("yyyyMMddHHssmm", new Date()));
        blMedicalExe2Mapper.cancleExocc(updateMap);
        // 更新医嘱状态为1（提交） 2019-01-04注释，需求文档变更，此需求在退费时处理
        // List<Map<String, Object>> qryMap =
        // DataBaseHelper.queryForList("SELECT a.PK_CNORD,substr(a.CODE_ORDTYPE,0,2) as type FROM CN_ORDER a WHERE a.ORDSN_PARENT IN (SELECT b.ORDSN_PARENT FROM CN_ORDER b WHERE b.PK_CNORD=?)",
        // paramMap.get("pkCnord").toString());
        // blMedicalExe2Mapper.updateApply(qryMap);
        // DataBaseHelper.execute("update cn_ris_apply set eu_status='1' where pk_cnord=? and eu_status='3'",
        // new Object[] { paramMap.get("pkCnord").toString() });
    }

    /**
     * 医疗记录查询-退费功能：住院退费
     *
     * @param param
     * @param user
     * @return
     */
    public void cancleCharge(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String PkExocc = (String) paramMap.get("pkExocc");
        Map<String, Object> retMap = DataBaseHelper.queryForMap("select sum(quan) quan from bl_ip_dt where pk_ordexdt=?", PkExocc);
        Double quan = retMap != null ? CommonUtils.getDouble(retMap.get("quan")) : 0D;
        if (quan <= 0)
            throw new BusException("暂无可退的费用信息!");
        // 查询对应的记费记录主键
        String PkCg = blMedicalExe2Mapper.queryCharge(PkExocc);
        // 根据记费主键，查询到具体的记费数据
        List<Map<String, Object>> blIpDt = blMedicalExe2Mapper.queryBlIpDt(PkCg);
        // 调用记退费接口
        Object[] args = new Object[4];
        blIpMedicalExeService.retCg(PkExocc, (User) user, args, "1");
        // 更新医嘱执行单：
        DataBaseHelper.execute("update ex_order_occ set date_occ= null, pk_emp_occ= null, name_emp_occ= null, pk_cg= null, eu_status='0'  where pk_exocc=? ", PkExocc);
        // 如果关联的医技执行单未执行，做删除处理
        DataBaseHelper.execute("delete from ex_assist_occ where pk_exocc=? and flag_occ='0'", PkExocc);
    }

    /**
     * 医疗记录查询-退费功能：住院退费,退单条收费项目(支持批量) 007004002035
     *
     * @param param
     * @param user
     * @return
     */
    public void refunedSingleItem(String param, IUser user) {
        List<Map<String, String>> chargeItems = JsonUtil.readValue(JsonUtil.getJsonNode(param, "dts"), new TypeReference<List<Map<String, String>>>() {
        });
        List<String> pkExoccs = JsonUtil.readValue(JsonUtil.getJsonNode(param, "pkExoccs"), new TypeReference<List<String>>() {
        });
        Set<String> pkPvList = new HashSet<>();
        List<String> PkCgList = Lists.newArrayList();
        for (Map<String, String> item : chargeItems) {
            if (item.get("pkCgip") == null)
                continue;
            PkCgList.add(item.get("pkCgip").toString());
            pkPvList.add(item.get("pkPv").toString());
        }
        if (PkCgList.size() <= 0)
            return;
        List<PvEncounter> pvEncounterList = DataBaseHelper.queryForList(
                "select * from PV_ENCOUNTER where pk_pv in ("
                        + CommonUtils.convertSetToSqlInPart(pkPvList, "pk_pv") + ")",
                PvEncounter.class, new Object[]{});
        for (PvEncounter pv : pvEncounterList) {
            if (!"1".equals(pv.getEuStatus())) {
                throw new BusException("患者《" + pv.getNamePi() + "》不是就诊状态，请核对该患者！");
            }
        }
        // 根据记费主键，查询到具体的记费数据
        List<BlIpDt> blIpDt = blMedicalExe2Mapper.queryBlIpDt_refactor(PkCgList);
        // 组装退费参数
        List<RefundVo> refundVoList = Lists.newArrayList();
        for (BlIpDt temp : blIpDt) {
            RefundVo refundVo = new RefundVo();
            refundVo.setNameEmp(UserContext.getUser().getNameEmp());
            refundVo.setPkCgip(temp.getPkCgip());
            refundVo.setPkOrdexdt(temp.getPkOrdexdt());
            refundVo.setPkDept(UserContext.getUser().getPkDept());
            refundVo.setPkEmp(UserContext.getUser().getPkEmp());
            refundVo.setPkOrg(UserContext.getUser().getPkOrg());
            for (Map<String, String> map : chargeItems) {
                boolean isUseDefQuan = temp.getPkCgip().equals(MapUtils.getString(map, "pkCgip")) &&
                        StringUtils.isNotBlank(MapUtils.getString(map, "quanRet"));
                if (isUseDefQuan) {
                    refundVo.setQuanRe(Double.parseDouble(chargeItems.get(0).get("quanRet")));
                    refundVo.setNoteCg(chargeItems.get(0).get("noteCg"));
                    break;
                }
                refundVo.setQuanRe(temp.getQuan());
                break;
            }
            refundVoList.add(refundVo);
        }
        // 调用记退费接口
        ipCgPubService.refundInBatch(refundVoList);
        // 查询该医嘱是否存在已计费的收费项目
        Integer itemCount = blMedicalExe2Mapper.isRefund(pkExoccs);
        // 当退的费用为最后一条费用时
        if (itemCount == null || itemCount <= 0) {
            // 更新医嘱执行单：
            blMedicalExe2Mapper.updateExOrdOcc(pkExoccs);
            // 更新对应医嘱申请单状态为1（提交）
            Set<String> pkExSet = new HashSet<String>(pkExoccs);
            List<Map<String, Object>> pkMap = DataBaseHelper.queryForList("select occ.pk_cnord,substr(ord.code_ordtype,0,2) as type from ex_order_occ occ inner join cn_order ord on ord.pk_cnord=occ.pk_cnord where pk_exocc in(" + CommonUtils.convertSetToSqlInPart(pkExSet, "pk_exocc") + ")", new Object[]{});
            for (Map<String, Object> temp : pkMap) {
                if ("02".equals(temp.get("type")))
                    DataBaseHelper.execute("update cn_ris_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
                if ("03".equals(temp.get("type")))
                    DataBaseHelper.execute("update cn_lab_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
                if ("12".equals(temp.get("type")))
                    DataBaseHelper.execute("update cn_trans_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
            }
            // 退费之后删除关联的医技执行单
            blMedicalExe2Mapper.deleteExOrdOccDt(pkExoccs);
            blMedicalExe2Mapper.deleteExOrdOcc(pkExoccs);
        }
        //发送检查检验记费信息至平台
        Map<String, Object> paramListMap = new HashMap<String, Object>();
        paramListMap.put("dtlist", chargeItems);
        paramListMap.put("type", "I");
        paramListMap.put("Control", "CR");
        Set<String> pkExSet = new HashSet<String>(pkExoccs);
        List<Map<String, Object>> pkMap = DataBaseHelper.queryForList("select occ.pk_cnord,substr(ord.code_ordtype,0,2) as type from ex_order_occ occ inner join cn_order ord on ord.pk_cnord=occ.pk_cnord where pk_exocc in(" + CommonUtils.convertSetToSqlInPart(pkExSet, "pk_exocc") + ")", new Object[]{});
        List<String> pkCnordList = new ArrayList<String>();
        for (Map<String, Object> temp : pkMap) {
            pkCnordList.add(temp.get("pkCnord").toString());
        }
        paramListMap.put("pkCnords", pkCnordList);
        PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
        paramListMap = null;
    }

    /**
     * 医疗记录查询-退费功能：住院退费,一次性退全部费用（重构）
     *
     * @param param
     * @param user
     * @return
     */
    public void cancleCharge_refactor(String param, IUser user) {
        List<String> pkExoccList = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        // 查询患者是否可退费（成组退成组收）
        Integer quan = blMedicalExe2Mapper.isRefund(pkExoccList);
        if (quan == null || quan <= 0)
            throw new BusException("暂无可退的费用信息!");
        // 查询对应的记费记录主键
        List<String> PkCgList = blMedicalExe2Mapper.queryCharge_refactor(pkExoccList);
        // 根据记费主键，查询到具体的记费数据
        List<BlIpDt> blIpDt = blMedicalExe2Mapper.queryBlIpDt_refactor(PkCgList);
        // 组装退费参数
        List<RefundVo> refundVoList = Lists.newArrayList();
        for (BlIpDt temp : blIpDt) {
            RefundVo refundVo = new RefundVo();
            refundVo.setNameEmp(UserContext.getUser().getNameEmp());
            refundVo.setPkCgip(temp.getPkCgip());
            refundVo.setPkDept(UserContext.getUser().getPkDept());
            refundVo.setPkEmp(UserContext.getUser().getPkEmp());
            refundVo.setPkOrg(UserContext.getUser().getPkOrg());
            refundVo.setQuanRe(temp.getQuan());
            refundVoList.add(refundVo);
        }
        // 调用记退费接口
        ipCgPubService.refundInBatch(refundVoList);
        // 更新医嘱执行单：
        blMedicalExe2Mapper.updateExOrdOcc(pkExoccList);
        // 更新对应医嘱申请单状态为1（提交）
        Set<String> pkExSet = new HashSet<String>(pkExoccList);
        List<Map<String, Object>> pkMap = DataBaseHelper.queryForList("select occ.pk_cnord,substr(ord.code_ordtype,0,2) as type from ex_order_occ occ inner join cn_order ord on ord.pk_cnord=occ.pk_cnord where pk_exocc in(" + CommonUtils.convertSetToSqlInPart(pkExSet, "pk_exocc") + ")", new Object[]{});
        for (Map<String, Object> temp : pkMap) {
            if ("02".equals(temp.get("type")))
                DataBaseHelper.execute("update cn_ris_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
            if ("03".equals(temp.get("type")))
                DataBaseHelper.execute("update cn_lab_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
            if ("12".equals(temp.get("type")))
                DataBaseHelper.execute("update cn_trans_apply set eu_status='1' where pk_cnord=? and eu_status='3' ", temp.get("pkCnord"));
        }
        // 如果关联的医技执行单未执行，做删除处理
        blMedicalExe2Mapper.deleteExOrdOccDt(pkExoccList);
        blMedicalExe2Mapper.deleteExOrdOcc(pkExoccList);
    }

    /**
     * 医技业务管理-门诊-保存功能:保存费用
     *
     * @param param 记费明细
     * @param user
     * @return list -已记费明细
     */
    public List<BlOpDt> saveDtList(String param, IUser user) {
        // 变更入参类型为门诊记费明细
        List<BlOpDt> dtlist = JsonUtil.readValue(param, new TypeReference<List<BlOpDt>>() {
        });
        if (dtlist == null || dtlist.size() <= 0)
            return null;
        List<BlPubParamVo> params = new ArrayList<BlPubParamVo>();
        List<BlOpDt> rtnlist = new ArrayList<BlOpDt>();
        //医嘱号存进set去重 最后拼接
        for (BlOpDt dt : dtlist) {
            if (CommonUtils.isEmptyString(dt.getPkCgop())) {
                params.add(constructBlParam(dt));
            } else if ("1".equals(dt.getEuAdditem()) && !CommonUtils.isEmptyString(dt.getPkCgop())) {
                // 先删除已经存在的附加记费项目,重新进行记费(会将用法附加费也同时删除重新记)
                DataBaseHelper.execute("delete from bl_op_dt where pk_cgop = ?", dt.getPkCgop());
                params.add(constructBlParam(dt));
            } else if ((dt.getEuAdditem() == null || "0".equals(dt.getEuAdditem())) && !CommonUtils.isEmptyString(dt.getPkCgop())) {
                // 非附加记费项目，不记费，直接返回界面
                rtnlist.add(dt);
            }
        }
        // 调用批量记费方法
        if (params.size() > 0) {
            BlPubReturnVo rtnvo = opCgPubService.blOpCgBatch(params);
            if (rtnvo != null && rtnvo.getBods() != null && rtnvo.getBods().size() > 0)
                rtnlist.addAll(rtnvo.getBods());
        }
        return rtnlist;
    }

    private BlPubParamVo constructBlParam(BlOpDt dt) {
        // 添加至需记费参数集合
        BlPubParamVo dtparam = new BlPubParamVo();
        dtparam.setBatchNo(dt.getBatchNo());
        dtparam.setDateExpire(dt.getDateExpire());
        dtparam.setDateHap(new Date());
        dtparam.setEuAdditem("1");
        dtparam.setEuPvType("1");// 就诊类型默认门诊
        dtparam.setFlagPd(dt.getFlagPd());
        dtparam.setFlagPv("0");// 非挂号费
        dtparam.setNameEmpApp(dt.getNameEmpApp());// 由前台传入，避免覆盖原始用法附加费的开立人
        dtparam.setNameEmpCg(dt.getNameEmpCg());
        dtparam.setPkOrgApp(dt.getPkOrgApp());
        dtparam.setPkEmpApp(dt.getPkEmpApp());
        dtparam.setPkEmpCg(dt.getPkEmpCg());
        dtparam.setNamePd(dt.getNameCg());
        dtparam.setPackSize(dt.getPackSize());
        dtparam.setPkCnord(dt.getPkCnord());
        dtparam.setPkDeptApp(dt.getPkDeptApp());
        dtparam.setPkDeptCg(dt.getPkDeptCg());
        dtparam.setPkDeptEx(dt.getPkDeptEx());
        dtparam.setPkEmpApp(dt.getPkEmpApp());
        dtparam.setPkEmpCg(dt.getPkEmpCg());
        if (BlcgUtil.converToTrueOrFalse(dt.getFlagPd())) // 药品
            dtparam.setPkOrd(dt.getPkItem());
        dtparam.setPkItem(dt.getPkItem());
        dtparam.setPkOrg(dt.getPkOrg());
        dtparam.setPkOrgApp(dt.getPkOrgApp());
        dtparam.setPkOrgEx(dt.getPkOrgEx());
        dtparam.setPkPi(dt.getPkPi());
        dtparam.setPkPres(dt.getPkPres());
        dtparam.setPkPv(dt.getPkPv());
        dtparam.setPkUnitPd(dt.getPkUnitPd());
        dtparam.setPrice(dt.getPrice());
        dtparam.setPriceCost(dt.getPriceCost());
        dtparam.setQuanCg(dt.getQuanCg());
        dtparam.setSpec(dt.getSpec());
        dtparam.setFlagHasPdPrice("1");
        dtparam.setPkDeptAreaapp(dt.getPkDeptAreaapp());
        dtparam.setPkDeptJob(dt.getPkDeptJob());
        return dtparam;
    }

    /**
     * 医技业务管理-执行功能：医技执行
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> excList(String param, IUser user) {
        List<Map<String, Object>> paramMapList = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        });
        for (Map<String, Object> paramMap : paramMapList) {
            paramMap.put("dateOcc", DateUtils.getDateTime());
            paramMap.put("pkEmpOcc", StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkEmpOcc")) ? MapUtils.getString(paramMap, "pkEmpOcc") : UserContext.getUser().getPkEmp());
            paramMap.put("nameEmpOcc", StringUtils.isNotBlank(MapUtils.getString(paramMap, "nameEmpOcc")) ? MapUtils.getString(paramMap, "nameEmpOcc") : UserContext.getUser().getNameEmp());
            paramMap.put("pkOrgOcc", StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkOrgOcc")) ? MapUtils.getString(paramMap, "pkOrgOcc") : UserContext.getUser().getPkOrg());
            paramMap.put("pkDeptOcc", StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkDeptOcc")) ? MapUtils.getString(paramMap, "pkDeptOcc") : UserContext.getUser().getPkDept());
            // 数据校验
            if (Integer.parseInt(paramMap.get("euPvtype").toString()) == 1) {
                // 门急诊：
                int count = blMedicalExe2Mapper.opDataCheck(paramMap.get("pkCnord").toString());
                if (count > 0) {
                    throw new BusException("有结算费用，不能执行!");
                }
            }
            if (Integer.parseInt(paramMap.get("euPvtype").toString()) == 3) {
                // 住院：
                List<Map<String, Object>> ipData = blMedicalExe2Mapper.ipDataCheck(paramMap.get("pkCnord").toString());
                for (Map<String, Object> map : ipData) {
                    if (map.get("pkCgip") == null) {
                        throw new BusException("有未记费项目，不能执行！");
                    }
                }
            }
            // 医技执行
            try {
                blMedicalExe2Mapper.medExeOcc(paramMap);
            } catch (Exception e) {
                throw new BusException("执行失败！");
            }
            // 如果该执行单关联了预约信息，同时更新预约记录状态为“到达”
            List<Map<String, Object>> occlist = blMedicalExe2Mapper.queryExAssistOccList(paramMap);
            for (Map<String, Object> map : occlist) {
                if (map.get("pkMsp") != null) {
                    map.put("pkMsp", map.get("pkMsp").toString().trim());
                }
                if (map.get("dateAppt") != null) {
                    // 如果该执行单关联了预约信息(预约时间不为空)，同时更新预约记录状态为“到达”
                    DataBaseHelper.execute("update sch_appt  set eu_status='1' where pk_schappt in(select pk_schappt from sch_appt_ord where pk_assocc=?) and eu_status='0'", map.get("pkAssocc"));
                    // 更新医嘱预约记录的执行标志；
                    DataBaseHelper.execute("update sch_appt_ord set flag_exec='1' where pk_assocc=? and flag_exec='0'", map.get("pkAssocc"));
                }
            }
            // 更新医嘱状态为3（检查）
            DataBaseHelper.execute("update cn_ris_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{paramMap.get("pkCnord").toString()});
            DataBaseHelper.execute("update cn_lab_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{paramMap.get("pkCnord").toString()});
            //发送检查检验记费信息至平台
            Map<String, Object> paramListMap = new HashMap<String, Object>();
            paramListMap.put("dtlist", paramMapList);
            paramListMap.put("type", "I");
            paramListMap.put("Control", "OK");
            PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
            paramListMap = null;
            // 执行成功之后，讲结果返回前台
            return occlist;
        }
        return null;
    }
    // /保存住院医技记费 ,调用原住院医技记费，注意新添加费用明细需要添加附加标志

    /**
     * 住院记费
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> savePatiCgInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<BlPubParamVo> dtlist = (List<BlPubParamVo>) paramMap.get("dtlist");
        String pkCnord = paramMap.get("pkCnord").toString();
        CnOrder cnord = DataBaseHelper.queryForBean("select pk_org,pk_ord from cn_order where pk_cnord = ?", CnOrder.class, pkCnord);
        String pkExocc = "";
        if (paramMap.get("pkExocc") != null) {
            pkExocc = paramMap.get("pkExocc").toString();
        }
        String dtListStr = JsonUtil.writeValueAsString(dtlist);
        dtlist = JsonUtil.readValue(dtListStr, new TypeReference<List<BlPubParamVo>>() {
        });
        for (BlPubParamVo blPubParamVo : dtlist) {
            blPubParamVo.setDateHap(new Date());
            // 申请机构：从医嘱表中获取
            blPubParamVo.setPkOrgApp(cnord.getPkOrg());
            blPubParamVo.setPkDeptEx(UserContext.getUser().getPkDept());
            // 执行单主键
            blPubParamVo.setPkOrdexdt(pkExocc);
            blPubParamVo.setPkCnord(pkCnord);
            // blPubParamVo.setPkOrd(cnord.getPkOrd());
            if (BlcgUtil.converToTrueOrFalse(blPubParamVo.getFlagPd()))
                blPubParamVo.setPkOrd(blPubParamVo.getPkItem());
        }
        if (dtlist == null || dtlist.size() <= 0)
            throw new BusException("没有需要记费的数据!");
        BlPubReturnVo chargeIpBatch = new BlPubReturnVo();
        if (paramMap.get("chargeCnt").toString().equals("已收费"))// 已经收费的:只需要写记费表
            chargeIpBatch = ipCgPubService.chargeIpBatch(dtlist, false);
        else
            // 没有收费的:都要写
            chargeIpBatch = ipCgPubService.chargeIpBatch(dtlist, false);
        List<BlIpDt> bids = chargeIpBatch.getBids();
        ExAssistOcc exAssistOcc = new ExAssistOcc();
        Date date = new Date();
        for (BlIpDt blIpDt : bids) {
            // 更新医嘱执行单，写表ex_order_occ new Date() date_occ=?,
            DataBaseHelper.execute("update ex_order_occ set date_occ=?, pk_emp_occ=?, name_emp_occ=?, pk_cg=?, eu_status='1',pk_org_occ=?, pk_dept_occ=?  where pk_exocc=? ", new Date(), ((User) user).getPkEmp(), ((User) user).getNameEmp(), blIpDt.getPkCgip(), ((User) user).getPkOrg(), ((User) user).getPkDept(), pkExocc);
            // 2018-10-15注释，和需求确认过，计费时不操作该表
            // 根据医嘱类型更新
            // DataBaseHelper.execute("update cn_ris_apply set eu_status='3' where pk_cnord=? and  eu_status<'3' ",
            // pkCnord);
            // 更新医嘱
            DataBaseHelper.execute("update cn_order set pk_org_exec=?, pk_dept_exec=? where pk_cnord=? ", ((User) user).getPkOrg(), ((User) user).getPkDept(), pkCnord);
        }
        int count = DataBaseHelper.queryForScalar("select count(1) from ex_assist_occ where pk_exocc=?", Integer.class, pkExocc);
        // 将生成的“检查”医技执行单存储起来传入检查系统接口
        List<ExAssistOcc> exAssistOccList = Lists.newArrayList();
        if (count <= 0) {
            // 根据频次和数量：生成医技执行记录(频次已经再前台分开显示)
            // 得到该条医嘱的数量
            CnOrder cnOrder = DataBaseHelper.queryForBean("select quan from cn_order where pk_cnord=? ", CnOrder.class, pkCnord);
            int len = (cnOrder.getQuan()).intValue();
            for (int i = 0; i < len; i++) {
                exAssistOcc = constructExAssistOcc(bids.get(0), pkExocc, user);
                DataBaseHelper.insertBean(exAssistOcc);
                if ("03".equals(cnOrder.getCodeOrdtype()))// 医嘱类型为检查时存入集合
                    exAssistOccList.add(exAssistOcc);
            }
        }
        List<Map<String, Object>> dtChargelist = blMedicalExe2Mapper.qryIpMedBlDtInfo(paramMap);
        // 调用外部系统接口
        // ExtSystemProcessUtils.processExtMethod("PACS", "savePacsEx",
        // pkExocc);
        return dtChargelist;
    }

    /**
     * 住院记费重构
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> savePatiCgInfo_refactor(String param, IUser user) {
        List<Map<String, Object>> paramMapList = JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {});
        // 一组申请单对应的所有收费项目明细
        List<BlPubParamVo> dtAllList = Lists.newArrayList();
        Map<String, Object> pExOrderOcc = null;// 主医嘱对应的医嘱执行单对象
        List<ExAssistOccDt> exAssistOccDtList = Lists.newArrayList();// 医技执行单明细
        List<String> pkExoccList = Lists.newArrayList();// 医嘱执行单主键集合
        List<String> pkCnordList = Lists.newArrayList();// 医嘱主键集合
        Set<String> pkPvList = new HashSet<>();
        //根据时间戳控制并发
        String dateTs = null;

        //为true时计费时不修改医嘱执行表的执行状态、执行人及执行时间
        boolean bFlagSpec = false;
        // 循环遍历前台传来的一组申请单
        for (Map<String, Object> occAndDt : paramMapList) {
            // 医嘱执行单
            Map<String, Object> exOrderOcc = (Map<String, Object>) occAndDt.get("exOrderOcc");
            if(!bFlagSpec && occAndDt.containsKey("flagSpec"))
            {
            	String flagSpec = (String)occAndDt.get("flagSpec");
            	if("1".equals(flagSpec))
            	{
            		bFlagSpec = true;
            	}
            }
            pkExoccList.add(exOrderOcc.get("pkExocc").toString());
            pkCnordList.add(exOrderOcc.get("pkCnord").toString());
            pkPvList.add(exOrderOcc.get("pkPv").toString());
            if (exOrderOcc.containsKey("ts")){
                dateTs=DateUtils.getDateTimeStr( DateUtils.strToDate(exOrderOcc.get("ts").toString()));
            }
            if (exOrderOcc.get("ordsnParent").toString().equals(exOrderOcc.get("ordsn").toString()))
                pExOrderOcc = exOrderOcc;
            // 计费明细
            List<BlPubParamVo> dtlist = (List<BlPubParamVo>) occAndDt.get("dtlist");
            if (dtlist == null || dtlist.size() <= 0)
                continue;
            // 这样写目的是将list里存的Map转换成对象
            String dtListStr = JsonUtil.writeValueAsString(dtlist);
            dtlist = JsonUtil.readValue(dtListStr, new TypeReference<List<BlPubParamVo>>() {
            });
            //构建去掉重复pkOrd的费用明细，有pkOrd的收费项目按医嘱项目进行记费，为了兼容科研医嘱记费设置 2020.05.18
            List<BlPubParamVo> newDtList = new ArrayList<BlPubParamVo>();
            boolean flagAdd = false;
            // 构建计费参数
            for (BlPubParamVo blPubParamVo : dtlist) {
                if (blPubParamVo.getDateHap() == null) {
                    blPubParamVo.setDateHap(new Date());
                }
                // 更新执行机构为当前机构；更新执行科室为当前科室
                blPubParamVo.setPkOrgApp(blPubParamVo.getPkOrg());
                blPubParamVo.setPkDeptEx(blPubParamVo.getPkDeptEx());
                blPubParamVo.setPkDeptNsApp(exOrderOcc.get("pkDeptNs").toString());
                blPubParamVo.setEuBltype("2");
                blPubParamVo.setPkDeptCg(UserContext.getUser().getPkDept());
                // 医嘱执行单主键、医嘱主键
                blPubParamVo.setPkOrdexdt(exOrderOcc.get("pkExocc").toString());
                // 子医嘱、父医嘱标志
                blPubParamVo.setOrdsnParent(Integer.parseInt(exOrderOcc.get("ordsnParent").toString()));
                blPubParamVo.setOrdsn(Integer.parseInt(exOrderOcc.get("ordsn").toString()));
                // 医嘱开始时间、医嘱类型
                blPubParamVo.setDateStart(DateUtils.strToDate(exOrderOcc.get("dateStart").toString()));
                blPubParamVo.setCodeOrdtype(exOrderOcc.get("codeOrdtype").toString());
                // 判断是否为新增的收费项目，该项目不包含在与该医嘱关联的收费项目中
                if ("1".equals(blPubParamVo.getIsNewItem())) {
                    blPubParamVo.setPkOrdexdt(pExOrderOcc.get("pkExocc").toString());
                    blPubParamVo.setPkDeptApp(pExOrderOcc.get("pkDeptApp").toString());
                }
                //pkOrd不为空，且不为新增记费项目
                if (CommonUtils.isNotNull(blPubParamVo.getPkOrd()) && "0".equals(blPubParamVo.getIsNewItem())) {
                    if (!flagAdd) {
                        // 若该项目为药品则将药品的pk_pd放在pkOrd中
                        if (BlcgUtil.converToTrueOrFalse(blPubParamVo.getFlagPd())) {
                            blPubParamVo.setPkOrd(blPubParamVo.getPkItem());
                        }
                        //清除收费项目主键
                        blPubParamVo.setPkItem(null);
                        blPubParamVo.setQuanCg(1.0D);
                        newDtList.add(blPubParamVo);
                        flagAdd = true;
                    }
                } else {
                    if (BlcgUtil.converToTrueOrFalse(blPubParamVo.getFlagPd())) {
                        blPubParamVo.setPkOrd(blPubParamVo.getPkItem());
                    }
                    newDtList.add(blPubParamVo);
                }
            }
            if (newDtList != null && newDtList.size() > 0) {
                dtAllList.addAll(newDtList);
            }
            // 组装医技执行单明细实体
            ExAssistOccDt exAssistOccDt = new ExAssistOccDt();
            if (pExOrderOcc != null) {
                exAssistOccDt.setFlagMaj("1"); // 主医嘱标志
            } else {
                exAssistOccDt.setFlagMaj("0");
            }
            exAssistOccDt.setPkCnord(exOrderOcc.get("pkCnord").toString());
            exAssistOccDt.setPkExocc(exOrderOcc.get("pkExocc").toString());
            exAssistOccDt.setPkOrd(exOrderOcc.get("pkOrd").toString());
            exAssistOccDtList.add(exAssistOccDt);
        }
        List<PvEncounter> pvEncounterList = DataBaseHelper.queryForList(
                "select * from PV_ENCOUNTER where pk_pv in ("
                        + CommonUtils.convertSetToSqlInPart(pkPvList, "pk_pv") + ")",
                PvEncounter.class, new Object[]{});
        for (PvEncounter pv : pvEncounterList) {
            if (!"1".equals(pv.getEuStatus())) {
                throw new BusException("患者《" + pv.getNamePi() + "》不是就诊状态，请核对该患者！");
            }
        }
        if (pExOrderOcc != null && pExOrderOcc.get("pkOrdris") != null && !"".equals(pExOrderOcc.get("pkOrdris").toString())) {
            Integer risCount = DataBaseHelper.queryForScalar("select count(1) from cn_ris_apply where pk_ordris=? and eu_status='0' ", Integer.class, pExOrderOcc.get("pkOrdris").toString());
            if (risCount != null && risCount > 0) {
                throw new BusException("该申请单已退回，请刷新页面！");
            }
        }
        if (StringUtils.isNotBlank(dateTs)){
            Integer count = DataBaseHelper.queryForScalar("select count(1) as count from ex_order_occ where pk_exocc=? and to_char(ts,'YYYYMMDDHH24MISS')='" + dateTs + "'", Integer.class, pExOrderOcc.get("pkExocc").toString());
            if (count <= 0)
                throw new BusException("记费失败，数据已经发生了变化，请刷新页面！");
        }
        if (pExOrderOcc != null && pExOrderOcc.get("pkExocc") != null && !"".equals(pExOrderOcc.get("pkExocc").toString())) {
            Integer count = DataBaseHelper.queryForScalar("select count(1) as count from ex_order_occ where pk_exocc=?", Integer.class, pExOrderOcc.get("pkExocc").toString());
            if (count <= 0)
                throw new BusException("该申请单已作废，请刷新页面！");
        } else {
            throw new BusException("未查询到该申请单的主申请单，请联系医生站确认该组申请单的执行科室是否一致！");
        }
        // 调用计费接口，得到返回数据
        BlPubReturnVo chargeIpBatch = ipCgPubService.chargeIpBatch(dtAllList, false);
        // 住院收费明细
        List<BlIpDt> bids = chargeIpBatch.getBids();
        if (bids == null || bids.size() <= 0) {
            throw new BusException("记费失败，未获取到记费返回数据！请刷新页面重新记费！");
        }
        // 更新医嘱执行单，写表ex_order_occ
        Map<String, Object> updateOccParam = Maps.newHashMap();
        updateOccParam.put("dateOcc", pExOrderOcc.get("dateOcc"));
        updateOccParam.put("pkEmpOcc", ((User) user).getPkEmp());
        updateOccParam.put("nameEmpOcc", ((User) user).getNameEmp());
        updateOccParam.put("pkOrgOcc", ((User) user).getPkOrg());
        updateOccParam.put("pkDeptOcc", ((User) user).getPkDept());
        updateOccParam.put("pkExoccList", pkExoccList);
        if(bFlagSpec)
        {
        	blMedicalExe2Mapper.updateOrdOccSpec(updateOccParam); 
        }
        else
        {
            blMedicalExe2Mapper.updateOrdOcc(updateOccParam);        	
        }
        // 更新医嘱 cn_order
        Map<String, Object> updateOrderParam = Maps.newHashMap();
        updateOrderParam.put("pkOrgExec", ((User) user).getPkOrg());
        updateOrderParam.put("pkDeptExec", ((User) user).getPkDept());
        updateOrderParam.put("pkCnordList", pkCnordList);
        blMedicalExe2Mapper.updateCnOrder(updateOrderParam);
        // 根据医嘱类型更新申请单表
        if (pExOrderOcc.get("codeOrdtype") != null) {
            String codeOrdtype = pExOrderOcc.get("codeOrdtype").toString().substring(0, 2);
            Set<String> pkCnordSet = new HashSet<String>(pkCnordList);
            switch (codeOrdtype) {
                case "02":
                    DataBaseHelper.execute("update cn_ris_apply set eu_status='3' where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ") and eu_status<'3'");
                    break;
                case "03":
                    DataBaseHelper.execute("update cn_lab_apply set eu_status='3' where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ") and eu_status<'3'");
                    break;
                case "12":
                    DataBaseHelper.execute("update cn_trans_apply set eu_status='3' where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ") and eu_status<'3'");
                    break;
                default:
                    break;
            }
        }
        // 判断是否有未执行的医嘱执行单，返回值大于0时返回，返回值为0时生成医技执行单
        int count = blMedicalExe2Mapper.isCharging(pkExoccList);
        if (count <= 0) {
            // 组装新增的医技执行单
            ExAssistOcc exAssistocc = new ExAssistOcc();
            exAssistocc.setPkOrg(((User) user).getPkOrg());
            exAssistocc.setPkCnord(pExOrderOcc.get("pkCnord").toString());
            exAssistocc.setPkPv(pExOrderOcc.get("pkPv").toString());
            exAssistocc.setPkPi(pExOrderOcc.get("pkPi").toString());
            exAssistocc.setEuPvtype("3");
            // 执行单号
            exAssistocc.setCodeOcc(ApplicationUtils.getCode("0503"));
            exAssistocc.setPkEmpOrd(((User) user).getPkEmp());// 开单医生主键
            exAssistocc.setNameEmpOrd(((User) user).getNameEmp());// 开单医生姓名
            exAssistocc.setPkDept(((User) user).getPkDept());
            exAssistocc.setDateOrd(new Date());
            exAssistocc.setQuanOcc(Double.parseDouble(pExOrderOcc.get("quan").toString()));
            exAssistocc.setTimesOcc(1);
            exAssistocc.setTimesTotal(1);
            exAssistocc.setPkOrgOcc(pExOrderOcc.get("pkOrgOcc").toString());
            exAssistocc.setPkDeptOcc(pExOrderOcc.get("pkDeptOcc").toString());
            exAssistocc.setFlagOcc("0");
            exAssistocc.setFlagCanc("0");
            exAssistocc.setPkExocc(pExOrderOcc.get("pkExocc").toString());
            exAssistocc.setInfantNo("0");
            exAssistocc.setEuStatus("0");
            exAssistocc.setFlagPrt("0");
            exAssistocc.setFlagRefund("0");
            DataBaseHelper.insertBean(exAssistocc);
            // 关联执行单与执行单明细
            for (ExAssistOccDt temp : exAssistOccDtList) {
                temp.setPkAssocc(exAssistocc.getPkAssocc());
                temp.setPkAssoccdt(NHISUUID.getKeyId());
            }
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExAssistOccDt.class), exAssistOccDtList);
        }
        Map<String, Object> pkListMap = Maps.newHashMap();
        pkListMap.put("pkExoccList", pkExoccList);
        pkListMap.put("pkCnordList", pkCnordList);
        List<Map<String, Object>> dtChargelist = blMedicalExe2Mapper.qryIpMedBlDtInfo_refactor(pkListMap);
        if (dtChargelist == null || dtChargelist.size() <= 0) {
            throw new BusException("记费失败，请刷新重新记费！");
        }
        // 调用外部系统接口
        if (pExOrderOcc != null) {
            List<String> PkExoccsFather = Lists.newArrayList();
            PkExoccsFather.add(pExOrderOcc.get("pkExocc").toString());
            ExtSystemProcessUtils.processExtMethod("PACS", "savePacsEx", pkExoccList);
        }
        //发送检查检验记费信息至平台
        Map<String, Object> paramListMap = new HashMap<String, Object>();
        paramListMap.put("dtlist", paramMapList);
        paramListMap.put("type", "I");
        paramListMap.put("Control", "OK");
        paramListMap.put("pkCnords", pkCnordList);
        PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
        paramListMap = null;
        return dtChargelist;
    }

    // 住院：向ex_assist_occ表中放数据
    private ExAssistOcc constructExAssistOcc(BlIpDt blIpDt, String pkExocc, IUser user) {
        // 添加至医技参数集合
        ExAssistOcc exAssietocc = new ExAssistOcc();
        exAssietocc.setPkOrg(blIpDt.getPkOrg());
        exAssietocc.setPkCnord(blIpDt.getPkCnord());
        exAssietocc.setPkPv(blIpDt.getPkPv());
        exAssietocc.setPkPi(blIpDt.getPkPi());
        exAssietocc.setEuPvtype("3");
        // 执行单号
        exAssietocc.setCodeOcc(ApplicationUtils.getCode("0503"));
        exAssietocc.setPkEmpOrd(((User) user).getPkEmp());// 开单医生主键
        exAssietocc.setNameEmpOrd(((User) user).getNameEmp());// 开单医生姓名
        exAssietocc.setPkDept(blIpDt.getPkDeptApp());
        exAssietocc.setDateOrd(new Date());
        exAssietocc.setQuanOcc(blIpDt.getQuan());
        exAssietocc.setTimesOcc(1);
        exAssietocc.setTimesTotal(1);
        exAssietocc.setPkOrgOcc(blIpDt.getPkOrgEx());
        exAssietocc.setPkDeptOcc(blIpDt.getPkDeptEx());
        exAssietocc.setFlagOcc("0");
        exAssietocc.setFlagCanc("0");
        exAssietocc.setPkExocc(pkExocc);
        exAssietocc.setInfantNo("0");
        exAssietocc.setEuStatus("0");
        exAssietocc.setFlagPrt("0");
        exAssietocc.setFlagRefund("0");
        return exAssietocc;
    }

    /**
     * 门诊删除收费项目
     *
     * @param param
     * @param user
     * @return
     */
    public void deleteOcc(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        DataBaseHelper.execute("delete from bl_op_dt where pk_cgop = ?", paramMap.get("pkCgop"));
    }

    /**
     * 交易号：007004001014 根据医嘱pk查询其对应的计费总额信息
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> qryTotalAmount(String param, IUser user) {
        String pkCnord = JsonUtil.getFieldValue(param, "pkCnord");
        Map<String, Object> amtInfo = DataBaseHelper.queryForMap("select sum(amount) amount,sum(amount_pi) amountPi,sum(amount-amount_pi) amountHppi from bl_op_dt where flag_settle='0' and pk_cnord = ?", pkCnord);
        return amtInfo;
    }

    /**
     * 交易号：007004001015 查询门诊患者是否有待结算的费用
     *
     * @param param
     * @param user
     * @return
     */
    public Integer qryOpIsCharge(String param, IUser user) {
        String pkCnord = JsonUtil.getFieldValue(param, "pkCnord");
        Integer count = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord=? and flag_settle='0'", Integer.class, pkCnord);
        return count;
    }

    /**
     * 查询收费组套
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qrycgSet(String param, IUser user) {
        String pkBlCgset = JsonUtil.getFieldValue(param, "pkBlCgset");
        List<Map<String, Object>> cgList = blMedicalExe2Mapper.qrycgSet(pkBlCgset);
        return cgList;
    }

    /**
     * 查询检查申请单打印信息
     *
     * @param param
     * @param user
     * @return
     */
    public List<RisPrintInfoVo> qryRisPrintInfo(String param, IUser user) {
        List<String> pkPvs = JsonUtil.readValue(JsonUtil.getJsonNode(param, "pkOrdris"), new TypeReference<List<String>>() {
        });
        String pkDeptExec = JsonUtil.getFieldValue(param, "pkDeptExec");
        List<RisPrintInfoVo> printInfo = blMedicalExe2Mapper.qryRisPrintInfo(pkPvs, pkDeptExec);
        return printInfo;
    }

    /**
     * 根据患者就诊主键查询该科室下的可打印申请单
     * 007004002048
     *
     * @param param
     * @param user
     * @return
     */
    public List<RisPrintInfoVo> qryAppPrtByPkPv(String param, IUser user) {
        List<String> pkPvs = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        Map<String, Object> qryPam = Maps.newHashMap();
        qryPam.put("pkPvs", pkPvs);
        qryPam.put("pkDept", UserContext.getUser().getPkDept());
        List<RisPrintInfoVo> risPrtInfos = blMedicalExe2Mapper.qryAppPrtByPkPv(qryPam);
        return risPrtInfos;
    }

    /**
     * 批量更新检查申请单状态 007004002036
     *
     * @param param
     * @param user
     */
    public void updateRisPrtFlag(String param, IUser user) {
        List<String> pkOrdriss = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        Set<String> PkOrdrisSet = new HashSet<String>(pkOrdriss);
        DataBaseHelper.update("update cn_ris_apply set flag_print2='1' where pk_ordris in (" + CommonUtils.convertSetToSqlInPart(PkOrdrisSet, "pk_ordris") + ")", new Object[]{});
    }

    /**
     * 查询常用的收费项目 007004002037
     *
     * @return
     */
    public List<Map<String, Object>> qryUsuItem(String param, IUser user) {
        Map<String, Object> qryParam = Maps.newHashMap();
        qryParam.put("dateBegin", DateUtils.addDate(new Date(),-1,2,"yyyy-MM-dd HH:mm:ss"));
        qryParam.put("dateEnd", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        qryParam.put("pkDeptEx", UserContext.getUser().getPkDept());
        List<Map<String, Object>> usuItem = blMedicalExe2Mapper.qryUsuItem(qryParam);
        return usuItem;
    }

    /**
     * 查询科室收费项目 007004002038
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryDeptItem(String param, IUser user) {
        String status = JsonUtil.getFieldValue(param, "status");
        Map<String, Object> qryParam = Maps.newHashMap();
        qryParam.put("status", status);
        qryParam.put("pkDept", UserContext.getUser().getPkDept());
        return blMedicalExe2Mapper.qryDeptItem(qryParam);
    }

    /**
     * 退回医技申请
     * 007004002039
     *
     * @param param
     * @param user
     */
    public void cancelApply(String param, IUser user) {
        List<Map<String, Object>> celApplyList = JsonUtil.readValue(param, List.class);
        if (celApplyList.size() > 0) {
            for (Map<String, Object> celMap : celApplyList) {
                if ("02".equals(MsgUtils.getPropValueStr(celMap, "codeOrdtype").substring(0, 2))) {
                    String pkOrdries = MsgUtils.getPropValueStr(celMap, "pkOrdris");
                    if (StringUtils.isNotBlank(pkOrdries)) {
                        String sql = "SELECT count(1) FROM CN_RIS_APPLY ris INNER JOIN EX_ORDER_OCC occ ON occ.PK_CNORD=ris.PK_CNORD WHERE occ.EU_STATUS='1' AND ris.PK_ORDRIS ='" + pkOrdries + "'";
                        Integer count = DataBaseHelper.queryForScalar(sql, Integer.class);
                        if (count != null && count > 0) {
                            throw new BusException("该申请单已记费，请先退费再退回申请！");
                        } else {
                            blMedicalExe2Mapper.cancelApply(pkOrdries);
                        }
                    }
            	   /* if (pkOrdries != null && pkOrdries.size() > 0) {
            	      blMedicalExe2Mapper.cancelApply(pkOrdries);
            	    }*/
                } else if ("03".equals(MsgUtils.getPropValueStr(celMap, "codeOrdtype").substring(0, 2))) {
                    String pkCnords = MsgUtils.getPropValueStr(celMap, "pkCnord");
                    if (StringUtils.isNotBlank(pkCnords)) {
                        String sql = "SELECT count(1) FROM cn_lab_apply lab INNER JOIN EX_ORDER_OCC occ ON occ.PK_CNORD=lab.PK_CNORD WHERE occ.EU_STATUS='1' AND occ.PK_CNORD ='" + pkCnords + "'";
                        Integer count = DataBaseHelper.queryForScalar(sql, Integer.class);
                        if (count != null && count > 0) {
                            throw new BusException("该申请单已记费，请先退费再退回申请！");
                        } else {
                            DataBaseHelper.update("update cn_lab_apply set eu_status ='0' where pk_cnord = ?", new Object[]{pkCnords});
                        }
                    }
                } else if ("12".equals(MsgUtils.getPropValueStr(celMap, "codeOrdtype").substring(0, 2))) {
                    String pkCnords = MsgUtils.getPropValueStr(celMap, "pkCnord");
                    if (StringUtils.isNotBlank(pkCnords)) {
                        String sql = "SELECT count(1) FROM cn_trans_apply apply INNER JOIN EX_ORDER_OCC occ ON occ.PK_CNORD=apply.PK_CNORD WHERE occ.EU_STATUS='1' AND occ.PK_CNORD ='" + pkCnords + "'";
                        Integer count = DataBaseHelper.queryForScalar(sql, Integer.class);
                        if (count != null && count > 0) {
                            throw new BusException("该申请单已记费，请先退费再退回申请！");
                        } else {
                            DataBaseHelper.update("update cn_trans_apply set eu_status ='0' where pk_cnord = ?", new Object[]{pkCnords});
                        }
                    }
                }
            }
        }
    }

    /**
     * 查询收退费明细
     * 007004002040
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryCgDt(String param, IUser user) {
        List<String> pkCnords = JsonUtil.readValue(param, new TypeReference<List<String>>() {
        });
        Set<String> pkCnordSet = new HashSet<String>(pkCnords);
        String sql = "SELECT * FROM (SELECT NAME_CG,SPEC,QUAN,PRICE,AMOUNT,PK_CNORD,DATE_CG,NAME_EMP_CG,flag_settle FROM BL_IP_DT UNION ALL SELECT NAME_CG,SPEC,QUAN,PRICE,AMOUNT,PK_CNORD,DATE_CG,NAME_EMP_CG,flag_settle FROM BL_OP_DT) cg WHERE cg.PK_CNORD in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "pk_cnord") + ")";
        List<Map<String, Object>> blIpDt = DataBaseHelper.queryForList(sql, new Object[]{});
        return blIpDt;
    }

    /**
     * 查询存在医技申请的住院患者
     * 007004002046
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPiInfo(String param, IUser user) {
        Map<String, Object> qryPam = JsonUtil.readValue(param, Map.class);
        qryPam.put("pkDeptExec", UserContext.getUser().getPkDept());
        List<Map<String, Object>> maps = blMedicalExe2Mapper.qryPiInfo(qryPam);
        return maps;
    }

    /**
     * 根据pkpv查询该患者在该科室下的所有申请单
     * 007004002047
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryApplyByPkPv(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        String euCharge = JsonUtil.getFieldValue(param, "euCharge");
        List<Map<String, Object>> applyInfos = blMedicalExe2Mapper.qryApplyByPkPv(pkPv, UserContext.getUser().getPkDept(), euCharge);
        return applyInfos;
    }

    /**
     * 医技业务管理-门诊页签-查询功能：查询门急诊医技申请单
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> queryOpBaMedicalApp(String param, IUser user) {
    	long date1 = System.currentTimeMillis();
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (CommonUtils.isNotNull(paramMap.get("dateBegin"))) {
            paramMap.put("dateBegin", CommonUtils.getString(paramMap.get("dateBegin")).substring(0, 8) + "000000");
        }
        if (CommonUtils.isNotNull(paramMap.get("dateEnd"))) {
            paramMap.put("dateEnd", CommonUtils.getString(paramMap.get("dateEnd")).substring(0, 8) + "235959");
        }
        // 分页操作
        int pageIndex = CommonUtils.getInteger(MapUtils.getString(paramMap, "pageIndex"));
        int pageSize = CommonUtils.getInteger(MapUtils.getString(paramMap, "pageSize"));
        MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> list = blMedicalExe2Mapper.queryOpBaMedicalApp(paramMap);
        Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
        if (CollectionUtils.isNotEmpty(list)){
            Set<String> pkCnords=list.stream().map(m->MapUtils.getString(m,"pkCnord")).collect(Collectors.toSet());
            StringBuilder sql=new StringBuilder();

            sql.append("select isnull(sum(dt.amount),0 )cnt, dt.pk_cnord ");
            sql.append  (" from bl_op_dt dt   where dt.PK_CNORD in ( " );
            sql.append (CommonUtils.convertSetToSqlInPart(pkCnords,"dt.PK_CNORD"));
               sql.append( " ) and dt.flag_settle = '1'    group by dt.pk_cnord");

            List<Map<String, Object>> blOplist=DataBaseHelper.queryForList(sql.toString());
            sql=new StringBuilder();
            sql.append("SELECT occ.pk_cnord,COUNT(*) num ,'1' EU_STATUS from ex_assist_occ occ where occ.pk_cnord  in ( ");
            sql.append(CommonUtils.convertSetToSqlInPart(pkCnords,"occ.pk_cnord") );
            sql.append("  )and  occ.EU_STATUS = '1' GROUP BY  occ.pk_cnord ");
            sql.append("union all");
            sql.append(" SELECT occ.pk_cnord,COUNT(1) numUn ,'0' EU_STATUS from ex_assist_occ occ where  occ.pk_cnord in( ");
            sql.append(CommonUtils.convertSetToSqlInPart(pkCnords,"occ.pk_cnord"));
            sql.append(" )and  occ.EU_STATUS = '0' and (occ.flag_refund ='0' or occ.flag_refund is null) GROUP BY occ.pk_cnord");
            List<Map<String, Object>> exOcclist=DataBaseHelper.queryForList(sql.toString());;
            list.forEach(m->{
                Integer cnt = 0;
                Integer num = 0;
                Integer numUn = 0;
                String euStatusName="取消执行";
                List<Map<String, Object>> list2 =  blOplist.stream()
                        .filter(p -> MapUtils.getString(m, "pkCnord").equals(MapUtils.getString(p, "pkCnord")))
                        .collect(Collectors.toList());
                cnt=CollectionUtils.isNotEmpty(list2)?((list2.get(0).get("cnt") instanceof BigDecimal) ?
                        ((BigDecimal)list2.get(0).get("cnt")).intValue() : (Integer)list2.get(0).get("cnt")) : 0;
               list2 =   exOcclist.stream()
                        .filter(p -> MapUtils.getString(m, "pkCnord").equals(MapUtils.getString(p, "pkCnord"))
                             && "1".equals(MapUtils.getString(p, "euStatus"))
                        )
                        .collect(Collectors.toList());
                num=CollectionUtils.isNotEmpty(list2)?((list2.get(0).get("num") instanceof BigDecimal) ?
                        ((BigDecimal)list2.get(0).get("num")).intValue() : (Integer)list2.get(0).get("num")) : 0;
                list2 =  exOcclist.stream()
                        .filter(p -> MapUtils.getString(m, "pkCnord").equals(MapUtils.getString(p, "pkCnord"))
                                && "0".equals(MapUtils.getString(p, "euStatus"))
                        )
                        .collect(Collectors.toList());
                numUn=CollectionUtils.isNotEmpty(list2)?((list2.get(0).get("num") instanceof BigDecimal) ?
                        ((BigDecimal)list2.get(0).get("num")).intValue() : (Integer)list2.get(0).get("num")) : 0;
                if (num==0 &&numUn>0){
                    euStatusName="未执行";
                } else if (num>0 &&numUn>0){
                    euStatusName="部分执行";
                }else if (num>0&&numUn==0){
                    euStatusName="已执行";

                }
               m.put("cnt",cnt);
                m.put("num",num);
                m.put("numUn",numUn);
                m.put("euStatusName",euStatusName);
            });
        }
        Map<String, Object> map = new HashMap<>();
        map.put("exAssistOccList", list);
        map.put("totalCount", page.getTotalCount());
        return map;
    }
}
