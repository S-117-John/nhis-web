package com.zebone.nhis.ex.pub.service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ex.pub.dao.ExPubMapper;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.ex.pub.vo.PdPriceVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;

/**
 * 计费公共服务
 *
 * @author yangxue
 */
@Service
public class BlCgExPubService {

    @Resource
    private IpCgPubService ipCgPubService;
    @Resource
    private PdStOutPubService pdStOutPubService;
    @Resource
    private ExPubMapper exPubMapper;


    /**
     * 住院静脉输液收费项目 -首日首次 - bd_dictattr.code_attr='0106'
     */
    private static final String INFUSION_ITEM_FIRST = "0106";
    /**
     * 住院连续输液收费项目 -第二组起 - bd_dictattr.code_attr='0107'
     */
    private static final String INFUSION_ITEM_CONTINUITY = "0107";
    /**
     * 住院小儿静脉输液收费项目 -首日首次 - bd_dictattr.code_attr='0108'
     */
    private static final String INFUSION_ITEM_FIRST_SON = "0108";
    /**
     * 住院小儿连续输液收费项目 -第二组起 - bd_dictattr.code_attr='0109'
     */
    private static final String INFUSION_ITEM_CONTINUITY_SON = "0109";
    /**
     * 附加费标志  0-不是附加费
     */
    private static final String EU_ADDUPDATE_NO = "0";
    /**
     * 附加费标志  1-当天第一次
     */
    private static final String EU_ADDUPDATE_FIRST = "1";
    /**
     * 附加费标志  2-当天第二次起
     */
    private static final String EU_ADDUPDATE_CONTINUITY = "2";

    /**
     * 医嘱执行确认并计费
     *
     * @param exList
     * @param u
     */
    public List<BlPubParamVo> execAndCg(List<ExlistPubVo> exList, User u) {
        //user = u;
        List<BlPubParamVo> cgList = new ArrayList<BlPubParamVo>();
        //住院输液附加费自动处理 -EX0064
        String infusionAutoProcess = ApplicationUtils.getSysparam("EX0064", false);
        //住院会诊记费方式 --BL0058 住院会诊计费方式： 0护士执行计费  1.医生应答计费
        String hospConsultChargeMode = ApplicationUtils.getSysparam("BL0058", false);
        //医嘱更新List
        List<String> updateOrderList = new ArrayList<String>();
        //输液类用法对象
        List<String> bdSupplyList = new ArrayList<>();
        //自动收取的输液附加费用List
        List<Map<String, Object>> mapInfusionItem = new ArrayList<Map<String, Object>>();
        //是否是儿科
        boolean isSonDept = false;
        //查询是否已收费
        String queryFeeSql = "select sum(AMOUNT) amount_sum from bl_ip_dt dt where dt.PK_ORDEXDT = ?";
        List<Map<String, Object>> mapOrderOcc = new ArrayList<Map<String, Object>>();
        if ("1".equals(infusionAutoProcess)) {
            //获取输液类用法
            bdSupplyList = exPubMapper.queryBdSupplyPivas();
            // 获取自动收取的输液附加费用
            StringBuilder sbItem = new StringBuilder("");
            sbItem.append("select item.pk_item, item.code, item.name, attr.code_attr, attr.name_attr,item.pk_unit,1 as quan ");
            sbItem.append(" from bd_item item inner join bd_dictattr attr on item.pk_item=attr.pk_dict ");
            sbItem.append(" where attr.pk_org=? and attr.code_attr in ('0106','0107','0108','0109') and attr.val_attr='1' ");
            mapInfusionItem = (List<Map<String, Object>>) DataBaseHelper.queryForList(sbItem.toString(), new Object[]{u.getPkOrg()});
            //当前科室是否为儿科
            Map<String, Object> dtMedicaltypeMap = DataBaseHelper.queryForMap("select dt_medicaltype from bd_ou_dept where pk_dept=?", u.getPkDept());
            if (dtMedicaltypeMap != null) {
                isSonDept = "05".equals(CommonUtils.getString(dtMedicaltypeMap.get("dtMedicaltype"))) ? true : false;
            }
            //判断当前患者，在当前科室，当天的输液类医嘱是否收取过第一组输液费
            StringBuilder sbOcc = new StringBuilder("");
            sbOcc.append("select ord.pk_pv, occ.date_plan from cn_order ord ");
            sbOcc.append(" inner join pv_encounter pv on ord.pk_pv=pv.pk_pv inner join ex_order_occ occ on ord.pk_cnord=occ.pk_cnord ");
            sbOcc.append(" inner join bd_supply sup on ord.code_supply=sup.code ");
            sbOcc.append(" where pv.eu_status='1' and  ord.ordsn=ord.ordsn_parent and occ.eu_status='1' ");
            sbOcc.append(" and occ.eu_addcharge='1' and  sup.flag_pivas='1'  and ord.pk_dept_ns = ?  ");
            mapOrderOcc = (List<Map<String, Object>>) DataBaseHelper.queryForList(sbOcc.toString(), new Object[]{u.getPkDept()});
        }
        List<String> updateExOccSql = new ArrayList<String>();
        for (ExlistPubVo exMap : exList) {
            String eu_status = exMap.getEuStatus();
            if (eu_status.equals("9") || eu_status.equals("1")) {
                continue;// 如果已经执行或取消执行，不再进行确认
            }
            //当天是否已收取过第一组费用
            boolean isExistence = false;
            //false-用法附加费；true-住院输液附加费
            boolean isInfusionAuto = false;
            //附加费标志
            exMap.setEuAddcharge(EU_ADDUPDATE_NO);
            String pkEmp = exList.get(0).getPkEmpOcc();//获取前台传入的执行人
            String nameEmp = exList.get(0).getNameEmpOcc();//获取前台传入的执行人名称
            Date dateOcc = exMap.getDateOcc();//前台获取的执行时间
            if (dateOcc == null) {
                dateOcc = new Date();
            }
            if (exMap.getDatePlanEx() == null) {
                exMap.setDatePlanEx(new Date());
            }
            //临时医嘱填充临时临嘱执行人和临嘱执行时间
            if (!"Y".equals(exMap.getIsskt()) && exMap.getDatePlanEx() != null && "1".equals(exMap.getEuAlways()) && exMap.getCntFreq() <= 1) {
                exMap.setPkEmpEx(CommonUtils.isEmptyString(exMap.getPkEmpEx()) ? u.getPkEmp() : exMap.getPkEmpEx());
                exMap.setNameEmpEx(CommonUtils.isEmptyString(exMap.getNameEmpEx()) ? u.getNameEmp() : exMap.getNameEmpEx());
                StringBuilder updateSql = new StringBuilder("update cn_order set ");
                updateSql.append("modifier = '" + u.getPkEmp() + "' ");
                updateSql.append(",modity_time = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + "','YYYYMMDDHH24MISS') ");
                updateSql.append(",ts = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + "','YYYYMMDDHH24MISS') ");
                updateSql.append(",date_plan_ex = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", exMap.getDatePlanEx()) + "','YYYYMMDDHH24MISS') ");
                updateSql.append(" ,pk_emp_ex ='" + exMap.getPkEmpEx() + "'");
                updateSql.append(" ,name_emp_ex ='" + exMap.getNameEmpEx() + "'");
                updateSql.append(" where pk_cnord = '" + exMap.getPkCnord() + "'");
                updateOrderList.add(updateSql.toString());
            } else {//皮试的情况按组更新
                StringBuilder updateSql = new StringBuilder("update cn_order set ");
                updateSql.append("modifier = '" + u.getPkEmp() + "' ");
                updateSql.append(",modity_time = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + "','YYYYMMDDHH24MISS') ");
                updateSql.append(",ts = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + "','YYYYMMDDHH24MISS') ");
                if (CommonUtils.isNotNull(exMap.getDatePlanEx())) {
                    updateSql.append(",date_plan_ex = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", exMap.getDatePlanEx()) + "','YYYYMMDDHH24MISS') ");
                }
                updateSql.append(" ,pk_emp_ex ='" + exMap.getPkEmpEx() + "'");
                updateSql.append(" ,name_emp_ex ='" + exMap.getNameEmpEx() + "'");
                updateSql.append(" where ordsn_parent = '" + exMap.getOrdsnParent() + "'");
                updateOrderList.add(updateSql.toString());
            }
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("dateOcc", dateOcc);
            paramMap.put("pkOrg", u.getPkOrg());
            paramMap.put("pkDept", u.getPkDept());
            pkEmp = CommonUtils.isEmptyString(pkEmp) ? u.getPkEmp() : pkEmp;
            nameEmp = CommonUtils.isEmptyString(nameEmp) ? u.getNameEmp() : nameEmp;
            paramMap.put("pkEmp", pkEmp);
            paramMap.put("nameEmp", nameEmp);
            String sql = "update ex_order_occ  set ex_order_occ.date_occ = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", dateOcc) + "','YYYYMMDDHH24MISS'),ex_order_occ.eu_status='1',ex_order_occ.pk_emp_occ='" + pkEmp + "',ex_order_occ.name_emp_occ='" + nameEmp + "'  ";
            if (!"Y".equals(exMap.getIsskt())) {// 如果不是皮试
                String pk_exocc = CommonUtils.getString(exMap.getPkExocc());
                sql = sql + " where ex_order_occ.pk_exocc = '" + pk_exocc + "' and ex_order_occ.eu_status='0' and (ex_order_occ.pk_cg is  null or ex_order_occ.pk_cg ='')";//未记过费的
                paramMap.put("pkExocc", pk_exocc);
                updateExOccSql.add(sql);
                // 更新执行状态。
//                int cnt = DataBaseHelper.update(sql, paramMap);
//                if (cnt < 1)
//                    throw new BusException("【" + exMap.getNameOrd() + "】可能已被他人记费，请刷新后重试！");
            } else {
                sql = sql
                        + " where exists (select pk_cnord from cn_order ord where ord.pk_cnord = ex_order_occ.pk_cnord "
                        + " and ord.ordsn_parent in (select o.ordsn_parent from cn_order o where o.pk_cnord = '" + exMap.getPkCnord() + "' )) and ex_order_occ.eu_status='0' ";
                paramMap.put("pkCnord", exMap.getPkCnord());
                updateExOccSql.add(sql);
//                int cnt = DataBaseHelper.update(sql, paramMap);
//                if (cnt < 1)
//                    throw new BusException("【" + exMap.getNameOrd() + "】可能已被他人记费，请刷新后重试！");
            }
            //会诊医嘱-根据参数BL0058判断是否记费; 0护士执行计费  1.医生应答计费
            if ("1".equals(hospConsultChargeMode) && exMap.getCodeOrdtype() != null && exMap.getCodeOrdtype().startsWith(IOrdTypeCodeConst.DT_ORDTYPE_CLINICSRV)) {
                continue;
            }
            //如果参数启动护士站住院输液附加费自动处理，并且是输液类医嘱，忽略用法附加费，启动输液附加费自动处理
            if ("1".equals(infusionAutoProcess) && bdSupplyList.contains(exMap.getCodeSupply())) {
                ExlistPubVo exInfusionMap = new ExlistPubVo();
                BeanUtils.copyProperties(exMap, exInfusionMap);
                //false-用法附加费；true-住院输液附加费
                isInfusionAuto = true;
                //验证当前患者当天是已经收取过第一组费用
                if (mapOrderOcc != null && mapOrderOcc.size() > 0) {
                    for (Map<String, Object> mapOcc : mapOrderOcc) {
                        String datePlan = CommonUtils.getString(mapOcc.get("datePlan"));
                        String pkPv = CommonUtils.getString(mapOcc.get("pkPv"));
                        int dateSpace = DateUtils.getDateSpace(exMap.getDatePlan(), DateUtils.strToDate(datePlan, "yyyy-MM-dd HH:mm:ss"));
                        if (pkPv.equals(exMap.getPkPv()) && dateSpace == 0) {
                            isExistence = true;
                            break;
                        }
                    }
                }
                //isExistence --> false-如果未收取，该患者本次执行的第一组输液医嘱自动收取第一组附加费; true-如果已收取，该患者本次执行的所有输液医嘱自动收取第二组起附加费；
                //isSonDept --> 如果当前科室专业类型为儿科（dept.dt_medicaltype=05）,附加费获取儿科专用的收费项目
                List<BlPubParamVo> setInfusionSupplyVo = null;
                if (isExistence) {
                    //生成当天第二次起附加费vo
                    //是否是儿科
                    if (isSonDept) {
                        setInfusionSupplyVo = setInfusionSupplyVo(mapInfusionItem, exInfusionMap, INFUSION_ITEM_CONTINUITY_SON, EU_ADDUPDATE_CONTINUITY, u);
                    } else {
                        setInfusionSupplyVo = setInfusionSupplyVo(mapInfusionItem, exInfusionMap, INFUSION_ITEM_CONTINUITY, EU_ADDUPDATE_CONTINUITY, u);
                    }
                } else {
                    //生成当天第一次附加费vo
                    //是否是儿科
                    if (isSonDept) {
                        setInfusionSupplyVo = setInfusionSupplyVo(mapInfusionItem, exInfusionMap, INFUSION_ITEM_FIRST_SON, EU_ADDUPDATE_FIRST, u);
                    } else {
                        setInfusionSupplyVo = setInfusionSupplyVo(mapInfusionItem, exInfusionMap, INFUSION_ITEM_FIRST, EU_ADDUPDATE_FIRST, u);
                    }
                    if (setInfusionSupplyVo != null && setInfusionSupplyVo.size() > 0) {
                        //当前患者当天只收取一次第一组费用
                        Map<String, Object> mapOcc = new HashMap<String, Object>();
                        mapOcc.put("pkPv", exMap.getPkPv());
                        mapOcc.put("datePlan", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", exMap.getDatePlan()));
                        mapOrderOcc.add(mapOcc);
                    }
                }
                if (setInfusionSupplyVo != null && setInfusionSupplyVo.size() > 0) {
                    cgList.addAll(setInfusionSupplyVo);
                }
            }
            // 对计费医嘱进行计费
            if ("1".equals(CommonUtils.getString(exMap.getFlagBl()))) {
                if (exMap.getFlagDurg() == null
                        || "".equals(exMap.getFlagDurg()))
                    throw new BusException("该条医嘱的flag_drug字段错误");
                if (exMap.getFlagBase() == null
                        || "".equals(exMap.getFlagBase()))
                    throw new BusException("该条执行记录的flag_base字段错误");
                //判断是否非本科室医嘱且已计费，若是，则略过防止重复计费
                boolean bHaveCharged = false;
                if("1".equals(exMap.getOtherDeptOrderAndFeeCheckFlag()))
                {                	
        			Map<String,Object> feeMap = DataBaseHelper.queryForMap(queryFeeSql, exMap.getPkExocc());
        			BigDecimal feeOfExocc = feeMap.get("amountSum") == null ? BigDecimal.ZERO : (BigDecimal)feeMap.get("amountSum");
        			if(feeOfExocc.compareTo(BigDecimal.ZERO) > 0)
        			{
        				bHaveCharged = true;
        			}
                }
                if(!"1".equals(exMap.getOtherDeptOrderAndFeeCheckFlag()) || !bHaveCharged)
                {
	                List<BlPubParamVo> blvos = setBlCgVo(exMap, exList, isInfusionAuto, u);
	                if (blvos != null && blvos.size() > 0) {
	                    cgList.addAll(blvos);
	                }
                }
            } else {//非记费医嘱，记用法附加费
                //判断是收取用法附加费还是输液自动附加费
                if (!isInfusionAuto) {
                    List<Map<String, Object>> mapPk_Item = getSupplyItem(exMap);
                    if (mapPk_Item != null) {
                        for (Map<String, Object> mapTemp : mapPk_Item) {
                            exMap.setFlagDurg("0");
                            exMap.setEuAddcharge(EU_ADDUPDATE_FIRST);
                            cgList.addAll(constructBlVo(exMap, mapTemp, true, u));
                        }
                    }
                }
            }
        }
        //批量更新执行单状态
        if (updateExOccSql != null && updateExOccSql.size() > 0) {
            int[] cnt = DataBaseHelper.batchUpdate(updateExOccSql.toArray(new String[0]));
            int count = 0;
            for (int i : cnt) {
                if (i > 0) {
                    count += 1;
                }
            }
            if (count != updateExOccSql.size()) {
                throw new BusException("部分执行单可能已被他人记费，请刷新后重试！");
            }
        }
        if (cgList != null && cgList.size() > 0) {
            boolean isAllowQF = false;
            //根据记费明细中的taskAutocgFlag标志此处为 自动执行由本病区执行的医嘱 这个定时任务触发的执行确认操作，
            //系统自动滚动的项目不受患者欠费权限的控制，即使患者欠费也可正常滚费
            String taskAutocgFlag = cgList.get(0).getTaskAutocgFlag();
            if (taskAutocgFlag != null && "1".equals(taskAutocgFlag)) {
                isAllowQF = true;
            }
            BlPubReturnVo vo = ipCgPubService.chargeIpBatch(cgList, isAllowQF);//调用计费接口
            if (vo != null && vo.getBids() != null && vo.getBids().size() > 0) {
                List<String> update_list = new ArrayList<String>();
                for (BlIpDt dt : vo.getBids()) {
                    //更新附加费标志
                    String euAddcharge = "";
                    for (BlPubParamVo cg : cgList) {
                        if (dt.getPkOrdexdt().equals(cg.getPkOrdexdt()) && !"0".equals(cg.getEuAddcharge())) {
                            euAddcharge = ",eu_addcharge = '" + cg.getEuAddcharge() + "' ";
                        }
                    }
                    String sql = "update ex_order_occ set pk_cg = '" + dt.getPkCgip() + "' " + euAddcharge + "  where pk_exocc = '" + dt.getPkOrdexdt() + "'";
                    update_list.add(sql);
                }
                if (update_list != null && update_list.size() > 0) {
                    DataBaseHelper.batchUpdate(update_list.toArray(new String[0]));
                }
            }
        }
        //更新医嘱信息
        if (updateOrderList != null && updateOrderList.size() > 0) {
            DataBaseHelper.batchUpdate(updateOrderList.toArray(new String[0]));
        }
        // 根据医嘱类型更新申请单表
        /** 监测代码永远不会执行 先注释
         List<String> pkCnordList = Lists.newArrayList();// 医嘱主键集合
         for (int i = 0; i < cgList.size(); i++) {
         if (cgList.get(i).getCodeOrdtype() != null) {
         String codeOrdtype = cgList.get(i).getCodeOrdtype().toString().substring(0, 2);
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
         }
         */
        Map<String, Object> paramListMap = new HashMap<String, Object>();
        List<Map<String, Object>> pkCnordLis = new ArrayList<>();
        for (int j = 0; j < cgList.size(); j++) {
            Map<String, Object> pkCnordMap = new HashMap<>();
            try {
                pkCnordMap = MsgUtils.objectToMap(cgList.get(j));
            } catch (Exception e) {
            }
            pkCnordLis.add(pkCnordMap);
        }
        paramListMap.put("dtlist", pkCnordLis);
        paramListMap.put("type", "I");
        paramListMap.put("Control", "OK");
        paramListMap.put("execAndCg", "");//方法标志
        PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
        paramListMap = null;
        return cgList;
    }

    /**
     * 构造计费vo
     *
     * @param map
     * @return
     */
    private List<BlPubParamVo> setBlCgVo(ExlistPubVo vo, List<ExlistPubVo> exList, boolean isInfusionAuto, User u) {
        List<BlPubParamVo> vos = new ArrayList<BlPubParamVo>();
        // 附加费
        List<Map<String, Object>> mapPk_Item = new ArrayList<Map<String, Object>>();
//        String item_Sql = "";
        if ("1".equals(vo.getFlagBase())) {// 基数药品记费
            vos.addAll(constructBlVo(vo, null, true, u));
        }
        if ("1".equals(vo.getFlagDurg())) {// 药品的情况，只收皮试附加费
            //isInfusionAuto = true 说明启用了EX0064参数，不收取用法附加费
            if (!isInfusionAuto) {
//              if (vo.getFlagSt() == null) {
                if ((vo.getOrdsn() == (vo.getOrdsnParent()))) {
                    mapPk_Item = getSupplyItem(vo);
                } else {
                    //如果是皮试--处理bug-26078 【住院护士】医嘱执行确认，试敏医嘱执行，如果同组皮试医嘱选择了子医嘱，皮试附加费会漏记
                    if ("Y".equals(vo.getIsskt())) {
                        mapPk_Item = getSupplyItem(vo);
                    } else {
                        //如果子医嘱设置的用法和父医嘱不一致，单独处理附加费用
                        for (ExlistPubVo ex : exList) {
                            if (ex.getOrdsn() == vo.getOrdsnParent() && !ex.getCodeSupply().equals(vo.getCodeSupply())) {
                                mapPk_Item = getSupplyItem(vo);
                            }
                        }
                    }
                }
                if (mapPk_Item != null) {
                    for (Map<String, Object> mapTemp : mapPk_Item) {
                        vo.setFlagDurg("0");
                        vo.setEuAddcharge(EU_ADDUPDATE_FIRST);
                        vos.addAll(constructBlVo(vo, mapTemp, true, u));
                    }
                }
//            }
            }
        } else {// 非药品
//            item_Sql = " select orditem.pk_item,orditem.quan,item.pk_unit "
//                    + " from bd_ord_item orditem inner join bd_item item on orditem.pk_item=item.pk_item  "
//                    + " where orditem.pk_ord=? and orditem.del_flag = '0' ";
//            mapPk_Item = (List<Map<String, Object>>) DataBaseHelper
//                    .queryForList(item_Sql, new Object[]{vo.getPkOrd()});
//            if (mapPk_Item == null)
//                return null;
            //for (Map<String, Object> mapTemp : mapPk_Item) {
            // blVo.setPk_ordpddt(map.get("pk_exlist"));//如果是非药品存放医嘱执行主键
            //    vos.addAll(constructBlVo(vo, mapTemp, false));
            //}
            vos.addAll(constructBlVo(vo, null, false, u));
        }
        return vos;
    }

    private List<Map<String, Object>> getSupplyItem(ExlistPubVo vo) {
        // 查询用法附加费
        StringBuilder sb = new StringBuilder("");
        sb.append("select item.pk_item,bditem.pk_unit,item.quan from bd_supply_item item ");
        sb.append(" inner join bd_supply sp on sp.pk_supply = item.pk_supply ");
        sb.append(" inner join bd_item bditem on bditem.pk_item = item.pk_item ");
        if (Application.isSqlServer()) {
            sb.append(" where (item.eu_pvtype = '3' or len(item.eu_pvtype) = 0 or item.eu_pvtype is null ) and sp.code = ?");
        } else {
            sb.append(" where (item.eu_pvtype = '3' or item.eu_pvtype is null ) and sp.code = ?");
        }
        List<Map<String, Object>> mapPk_Item = (List<Map<String, Object>>) DataBaseHelper
                .queryForList(sb.toString(), new Object[]{vo.getCodeSupply()});
        return mapPk_Item;
    }

    /**
     * 通过执行单转计费
     *
     * @param exvo
     * @param mapTemp 其他费用
     * @return
     */
    private List<BlPubParamVo> constructBlVo(ExlistPubVo exvo, Map<String, Object> mapTemp, boolean flagDrug, User u) {
        CnOrder cnorder = DataBaseHelper.queryForBean("select pk_wg,pk_wg_org,code_ordtype from cn_order where pk_cnord = ?",
                CnOrder.class, exvo.getPkCnord());
        BlPubParamVo vo = new BlPubParamVo();
        if(cnorder!=null){
            vo.setPkWgEx(cnorder.getPkWg());
            vo.setPkWg(cnorder.getPkWg());
            vo.setPkWgOrg(cnorder.getPkWgOrg());
            vo.setCodeOrdtype(cnorder.getCodeOrdtype());
        }
        vo.setEuPvType(exvo.getEuPvtype());
        vo.setPkPres(exvo.getPkPres());
        vo.setPkPv(exvo.getPkPv());
        vo.setPkCnord(exvo.getPkCnord());
        vo.setFlagPd(exvo.getFlagDurg());
        vo.setFlagPv("0");
        vo.setPkEmpEx(u.getPkEmp());
        vo.setNameEmpEx(u.getNameEmp());
        if (!"99".equals(exvo.getEuBltype())) {
            vo.setEuBltype("0");//病区记费
            vo.setNameEmpCg(u.getNameEmp());
            vo.setPkEmpCg(u.getPkEmp());
        } else {
            vo.setNameEmpCg("自动");
            vo.setEuBltype("99");
        }
        vo.setFlagFit(exvo.getFlagFit());
        vo.setDescFit(exvo.getDescFit());//增加适应症描述
        vo.setNameEmpApp(exvo.getNameEmpOrd());
        vo.setPkOrg(exvo.getPkOrg());
        vo.setPkEmpApp(exvo.getPkEmpOrd());
        vo.setPkOrgApp(exvo.getPkOrg());
        vo.setPkDeptNsApp(exvo.getPkDeptNs());//开立病区
        vo.setDateHap(exvo.getDatePlan());
        vo.setPkDeptApp(exvo.getPkDept());//开立科室，如果不是病区应该赋值为执行科室
        vo.setPkDeptEx(exvo.getPkDeptExec());
        vo.setEuAddcharge(exvo.getEuAddcharge());
        if (mapTemp != null && CommonUtils.isNotNull(mapTemp.get("pkItem"))) {
            vo.setPkItem(CommonUtils.getString(mapTemp.get("pkItem")));
            vo.setPkUnitPd(CommonUtils.getString(mapTemp.get("pkUnit")));
            vo.setPkOrd(null);
            if (!flagDrug) {
                vo.setQuanCg(MathUtils.mul(CommonUtils.getDouble(mapTemp.get("quan")), exvo.getQuanOcc()));
            } else {
                vo.setPkDeptEx(exvo.getPkDeptNs());
                vo.setQuanCg(CommonUtils.getDouble(mapTemp.get("quan")));
            }
        } else {
            vo.setPkItem(null);
            vo.setPkOrd(exvo.getPkOrd());
            vo.setPkUnitPd(exvo.getPkUnit());
            vo.setQuanCg(exvo.getQuanOcc());
        }
        vo.setPkOrdexdt(exvo.getPkExocc());
        vo.setPkOrgEx(exvo.getPkOrgOcc());
        vo.setPkPi(exvo.getPkPi());
        vo.setInfantNo(exvo.getInfantNo());
        vo.setPkDeptCg(u.getPkDept());
        vo.setTaskAutocgFlag(exvo.getTaskAutocgFlag()); //定时任务自动执行记费标志
        //公共赋值部分--end
        List<BlPubParamVo> bllist = new ArrayList<BlPubParamVo>();
        Map<String, Object> map = null;
        //如果是药品记费，先询价
        if (flagDrug && "1".equals(exvo.getFlagBase()) && mapTemp == null) {
            if (exvo.isFlagLabor()) {//产房执行确认，药品取零售价
                BdPd pdvo = DataBaseHelper.queryForBean("select pd.pack_size,pd.price from bd_pd pd where pd.pk_pd = ?", BdPd.class, new Object[]{exvo.getPkOrd()});
                vo.setBatchNo("~");
                vo.setPackSize(exvo.getPackSize());
                vo.setPrice(MathUtils.mul(MathUtils.div(pdvo.getPrice(), pdvo.getPackSize().doubleValue()), vo.getPackSize().doubleValue()));
                vo.setPriceCost(vo.getPrice());
                vo.setDateExpire(null);
                vo.setQuanCg(MathUtils.upRound(exvo.getQuanOcc()));
                vo.setPkUnitPd(exvo.getPkUnit());
                bllist.add(vo);
            } else {
                map = new HashMap<String, Object>();
                List<String> pkPds = new ArrayList<String>();
                pkPds.add(exvo.getPkOrd());
                map.put("pkPds", pkPds);
                map.put("pkDeptAp", exvo.getPkDeptExec());
                //修改为从基数药出库记录中获取记费信息
                List<PdPriceVo> pricelist = exPubMapper.queryPdBasePrice(map);
                if (pricelist == null || pricelist.size() <= 0) {
                    //throw new BusException("基数药品【"+exvo.getNameOrd()+"】无科室领药记录，获取不到价格信息，无法完成记费！");
                    BdPd pdvo = DataBaseHelper.queryForBean("select pd.pack_size,pd.price from bd_pd pd where pd.pk_pd = ?", BdPd.class, new Object[]{exvo.getPkOrd()});
                    vo.setBatchNo("~");
                    vo.setPackSize(exvo.getPackSize());
                    vo.setPrice(MathUtils.mul(MathUtils.div(pdvo.getPrice(), pdvo.getPackSize().doubleValue()), vo.getPackSize().doubleValue()));
                    vo.setPriceCost(vo.getPrice());
                    vo.setDateExpire(null);
                    vo.setQuanCg(MathUtils.upRound(exvo.getQuanOcc()));
                    vo.setPkUnitPd(exvo.getPkUnit());
                    bllist.add(vo);
                } else {
                    for (PdPriceVo pdvo : pricelist) {
                        if (exvo.getPkOrd().equals(pdvo.getPkPd())) {
                            vo.setBatchNo(pdvo.getBatchNo());
                            vo.setPackSize(exvo.getPackSize());
                            vo.setPrice(MathUtils.mul(MathUtils.div(pdvo.getPrice(), CommonUtils.getDouble(pdvo.getPackSize())), CommonUtils.getDouble(exvo.getPackSize())));
                            vo.setPriceCost(MathUtils.mul(MathUtils.div(pdvo.getPriceCost(), CommonUtils.getDouble(pdvo.getPackSize())), CommonUtils.getDouble(exvo.getPackSize())));
                            vo.setDateExpire(pdvo.getDateExpire());
                            vo.setQuanCg(MathUtils.upRound(exvo.getQuanOcc()));
                            vo.setPkUnitPd(exvo.getPkUnit());
                            bllist.add(vo);
                            break;
                        }
                    }
                }
            }
        } else {
            bllist.add(vo);
        }
        return bllist;
    }


    /**
     * 取消执行并退费
     */
    public void cancelExAndRtnCg(List<ExlistPubVo> exList, User u) {
        if (exList == null || exList.size() <= 0) return;
        //退费
        BlPubReturnVo cgvo = rtnCg(exList, u);
        //if(cgvo == null || cgvo.getBids() == null||cgvo.getBids().size() <=0)
        //	throw new BusException("退费后未获取到退费记录！");
        String pkEmpCanc = u.getPkEmp();
        String nameEmpCanc = u.getNameEmp();
        List<String> consultSqlList = new ArrayList<String>();//会诊申请单更新sql集合--取消医嘱时同时取消会诊申请单的状态
        for (ExlistPubVo exMap : exList) {
            pkEmpCanc = exMap.getPkEmpCanc();
            nameEmpCanc = exMap.getNameEmpCanc();
            String eu_status = exMap.getEuStatus();
            if (eu_status.equals("9")) {
                continue;// 如果已经取消执行，不再进行取消
            }
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("dateCanc", new Date());
            paramMap.put("pkDept", u.getPkDept());
			/*paramMap.put("pkEmp", u.getPkEmp());
			paramMap.put("nameEmp", u.getNameEmp());*/
            paramMap.put("pkEmp", pkEmpCanc);
            paramMap.put("nameEmp", nameEmpCanc);
            paramMap.put("pkCgRtn", "");
            String pk_exocc = exMap.getPkExocc();
            if (cgvo != null && cgvo.getBids() != null && cgvo.getBids().size() > 0) {
                for (BlIpDt dt : cgvo.getBids()) {
                    if (pk_exocc.equals(dt.getPkOrdexdt())) {
                        paramMap.put("pkCgRtn", dt.getPkCgip());
                    }
                }
            }
            String sql = "update ex_order_occ set flag_canc='1',pk_cg_cancel=:pkCgRtn,date_canc = :dateCanc,eu_status='9',"
                    + "pk_dept_canc=:pkDept,pk_emp_canc=:pkEmp,name_emp_canc=:nameEmp where pk_exocc = :pkExocc and (pk_cg_cancel is null or pk_cg_cancel='')";
            paramMap.put("pkExocc", pk_exocc);
            // 更新执行状态。
            int cnt = DataBaseHelper.update(sql, paramMap);
            if (cnt < 1) {
                throw new BusException("【" + exMap.getNameOrd() + "】可能已被他人取消，请刷新后重试！");
            }
            consultSqlList.add("update cn_consult_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//更新会诊申请单
            consultSqlList.add("update cn_trans_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//更新输血申请单
            consultSqlList.add("update cn_ris_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//更新检查申请单
            consultSqlList.add("update cn_lab_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//更新检验申请单
            String upExSql = "update EX_ASSIST_OCC set FLAG_CANC='1', DATE_CANC=to_date('" + DateUtils.getDefaultDateFormat().format(new Date()) + "','YYYYMMDDHH24MISS') " +
                    ", PK_EMP_CANC='" + pkEmpCanc + "',NAME_EMP_CANC='" + nameEmpCanc + "'" +
                    "    where PK_ASSOCC in ( " +
                    "    select EAOD.PK_ASSOCC " +
                    "    from EX_ASSIST_OCC_DT EAOD " +
                    "    where EAOD.PK_EXOCC = '" + pk_exocc +
                    "')";
            consultSqlList.add(upExSql);//更新辅佐执行表
        }
        //批量更新会诊、输血、检查 申请单表状态为0
        if (null != consultSqlList && consultSqlList.size() > 0) {
            DataBaseHelper.batchUpdate(consultSqlList.toArray(new String[0]));
        }
        consultSqlList = null;
    }

    /**
     * 对取消的执行单退费
     *
     * @param exList
     * @param u
     */
    private BlPubReturnVo rtnCg(List<ExlistPubVo> exList, User u) {
        if (exList == null || exList.size() <= 0) return null;
        List<RefundVo> rtnList = new ArrayList<RefundVo>();
        for (ExlistPubVo exvo : exList) {
            //对基数药与非药品的执行单进行退费
            String flagDurg = exvo.getFlagDurg();
            String flagBase = exvo.getFlagBase();
            String euStatus = exvo.getEuStatus();
            //如果是基数药或者是执行过的非药品，退费
            if ((flagDurg.equals("1") && euStatus.equals("1")) || 
            		(!flagDurg.equals("1") && euStatus.equals("1") && "1".equals(exvo.getFlagBl()))) {
                String cgsql = " select cg.pk_cgip,cg.quan,pk_ordexdt from bl_ip_dt cg "
                        + "inner join ex_order_occ exlist on exlist.pk_exocc = cg.pk_ordexdt  where " +
                        " exlist.pk_exocc = ?  and  cg.quan > 0 and cg.flag_settle = '0'" +
                        " and not exists (select 1 from bl_ip_dt b where cg.pk_cgip=b.pk_cgip_back)";
                List<Map<String, Object>> mapPk_CgList = (List<Map<String, Object>>) DataBaseHelper.queryForList(cgsql, new Object[]{exvo.getPkExocc()});
                if (mapPk_CgList == null) {
                    return null;
                }
                List<RefundVo> cgList = new ArrayList<RefundVo>();
                for (Map<String, Object> mapPk_Cg : mapPk_CgList) {
                    cgList.add(constructRtnCgVo(mapPk_Cg, u));
                }
                rtnList.addAll(cgList);
            }
        }
        BlPubReturnVo vo = null;
        try {
            if (rtnList != null && rtnList.size() > 0) {
                vo = ipCgPubService.refundInBatch(rtnList);
            }
        } catch (BusException e) {
            throw new BusException("住院退费出错:" + e.getMessage());
        }
        return vo;
    }

    /**
     * 构造退费参数
     */
    private RefundVo constructRtnCgVo(Map<String, Object> map, User u) {
        if (map == null) throw new BusException("调用退费接口时构造退费参数错误！未获取到转换的数据");
        RefundVo vo = new RefundVo();
        vo.setNameEmp(u.getNameEmp());
        vo.setPkCgip(CommonUtils.getString(map.get("pkCgip")));
        vo.setPkDept(u.getPkDept());
        vo.setPkOrg(u.getPkOrg());
        vo.setPkEmp(u.getPkEmp());
        vo.setQuanRe(CommonUtils.getDouble(map.get("quan")));
        return vo;
    }

    /**
     * 住院输液自动记费附加费vo
     *
     * @param map
     * @return
     */
    private List<BlPubParamVo> setInfusionSupplyVo(List<Map<String, Object>> mapInfusionItem, ExlistPubVo exMap, String codeAttr, String euAddcharge, User u) {
        List<BlPubParamVo> vos = new ArrayList<BlPubParamVo>();
        if ((exMap.getOrdsn() == (exMap.getOrdsnParent()))) {
            for (Map<String, Object> map : mapInfusionItem) {
                if (codeAttr.equals(CommonUtils.getString(map.get("codeAttr")))) {
                    exMap.setFlagDurg("0");
                    exMap.setEuAddcharge(euAddcharge);
                    vos.addAll(constructBlVo(exMap, map, true, u));
                }
            }
        }
        return vos;
    }

    /**
     * 恢复执行 - 将已执行的执行单恢复成未执行状态
     *
     * @param exList
     * @param user
     */
    public void recoveryExAndRtnCg(List<ExlistPubVo> exList, User u) {
        if (exList == null || exList.size() <= 0) return;
        //退费
        BlPubReturnVo cgvo = rtnCg(exList, u);
        //会诊申请单更新sql集合--取消医嘱时同时取消会诊申请单的状态
        List<String> consultSqlList = new ArrayList<String>();
        //执行单更新集合
        List<String> updateExOccSql = new ArrayList<String>();
        //医嘱更新集合
        Set<String> pkCnords = new HashSet<String>();
        //住院检查执行确认或恢复是否更新申请单状态 -EX0077  0-不更新 否则更新
        String isUpApplyStatus = ApplicationUtils.getSysparam("EX0077", false);
        for (ExlistPubVo exMap : exList) {
            String euStatus = exMap.getEuStatus();
            if (euStatus.equals("0")) {
                continue;// 如果已经恢复执行，不再进行恢复
            }
            String sql = "update ex_order_occ set date_occ = null ,pk_emp_occ= null ,name_emp_occ = null,eu_status='0',pk_cg = null "
                    + " where pk_exocc = '" + exMap.getPkExocc() + "' and (date_occ is null or eu_status != '0')";
            updateExOccSql.add(sql);
            //如果是临时医嘱，清空cn_order表的date_plan_ex,pk_emp_ex,name_emp_ex
            if ("1".equals(exMap.getEuAlways()) && exMap.getCntFreq() <= 1) {
                pkCnords.add(exMap.getPkCnord());
            }
            consultSqlList.add("update cn_consult_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//更新会诊申请单
            consultSqlList.add("update cn_trans_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//更新输血申请单
            //考虑参数null的情况，这里取反
            if (!"0".equals(isUpApplyStatus)) {
                consultSqlList.add("update cn_ris_apply set eu_status='0' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//更新检查申请单
            }
            consultSqlList.add("update cn_lab_apply set eu_status='1' where pk_cnord='" + exMap.getPkCnord() + "' and eu_status > '0'");//更新检验申请单
        }
        //批量更新执行单状态
        if (updateExOccSql != null && updateExOccSql.size() > 0) {
            int[] cnt = DataBaseHelper.batchUpdate(updateExOccSql.toArray(new String[0]));
            int count = 0;
            for (int i : cnt) {
                if (i > 0) {
                    count += 1;
                }
            }
            if (count != updateExOccSql.size()) {
                throw new BusException("部分执行单可能已被他人取消，请刷新后重试！");
            }
        }
        //批量更新医嘱信息
        if (pkCnords != null && pkCnords.size() > 0) {
            StringBuffer str = new StringBuffer("update cn_order set ");
            str.append(" date_plan_ex = null ,pk_emp_ex= null ,name_emp_ex = null ");
            str.append(",modifier = '" + u.getPkEmp() + "' ");
            str.append(",modity_time = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + "','YYYYMMDDHH24MISS') ");
            str.append(",ts = to_date('" + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + "','YYYYMMDDHH24MISS') ");
            str.append(" where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")");
            DataBaseHelper.update(str.toString());
        }
        //批量更新会诊、输血、检查 申请单表状态为0
        if (null != consultSqlList && consultSqlList.size() > 0) {
            DataBaseHelper.batchUpdate(consultSqlList.toArray(new String[0]));
        }
        consultSqlList = null;
    }
}
