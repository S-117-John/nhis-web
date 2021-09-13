package com.zebone.nhis.pro.zsba.cn.opdw.service;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.cn.opdw.dao.CnOrderOpMapper;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.MchVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CnOrderOpService {

    @Resource
    private CnOrderOpMapper opOrderDao;

    /**
     * 获取患者处方明细列表
     * 022003026004
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> getPrescriptionDetail(String param, IUser user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String sPkPres = map.get("pkPres") == null ? "" : map.get("pkPres").toString();
        if (StringUtils.isBlank(sPkPres)) throw new BusException("处方主键为空！");
        String pkDeptExec = map.get("pkDeptExec") == null ? "" : map.get("pkDeptExec").toString();
        if (StringUtils.isBlank(pkDeptExec)) throw new BusException("西药房主键为空！");
        List<Map<String, Object>> prescriptionDetails = opOrderDao.getPrescriptionDetail(sPkPres, pkDeptExec, null);
        return prescriptionDetails;
    }

    /**
     * 患者历史就诊医嘱信息
     * 022003026005
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryHistoryOrders(String param, IUser user) {
        //pkPv
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = paramMap.get("pkPv") != null ? paramMap.get("pkPv").toString() : "";
        if (StringUtils.isBlank(pkPv)) throw new BusException("传参pkPv为空！");
        List<Map<String, Object>> histOrderlist = opOrderDao.qryHistoryOrders(paramMap);
        return histOrderlist;
    }

    /**
     * 查询医嘱模板明细
     * trans code 022003026006
     *
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<Map<String, Object>> getTempDtList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pid = MapUtils.getString(paramMap, "pid");
        String codeSet = MapUtils.getString(paramMap, "codeSet");
        if (StringUtils.isEmpty(pid) && StringUtils.isEmpty(codeSet)) {
            throw new BusException("请传入模板主键！！！");
        }
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        //查询非药品
        List<Map<String, Object>> ll = opOrderDao.qryBdOrd(paramMap);
        ret.addAll(ll);
        //查询药品
        List<Map<String, Object>> mm = opOrderDao.qryBdOrdDrug(paramMap);
        ret.addAll(mm);
        return ret;
    }

    /**
     * 删除计费信息
     *
     * @param param
     * @param user
     */
    public void delBlFree(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || CommonUtils.isNull(paramMap.get("pkCgop"))) {
            throw new BusException("请传入计费主键！");
        }
        List<BlOpDt> blOpDts = DataBaseHelper.queryForList(" select * from bl_op_dt where pk_cgop=? and del_flag='0' ", BlOpDt.class, paramMap.get("pkCgop").toString());
        if (blOpDts == null || blOpDts.size() == 0) {
            return;
        }
        for (BlOpDt vo : blOpDts) {
            if ("1".equals(vo.getFlagSettle())) {
                throw new BusException("费用已收，不能能删除!");
            }
            vo.setDelFlag("1");//删除标志
        }
        String pkCnord= String.valueOf(paramMap.get("pkCnord"));
        if ( StringUtils.isNotBlank(pkCnord)){
            opOrderDao.deleteExAssistOcc(pkCnord);
            opOrderDao.deleteExAssistOccDt(pkCnord);
        }

        DataBaseHelper.batchUpdate("delete from BL_OP_DT where PK_CGOP=:pkCgop ", blOpDts);
    }

    /**
     * 查询患者本次就诊未收费项目
     * trans code 022003026006
     *
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<BlOpDt> getFeeList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = MapUtils.getString(paramMap, "pkPv");
        if (StringUtils.isEmpty(pkPv)) {
            throw new BusException("请传入就诊主键！！！");
        }
        //费用
        List<BlOpDt>  blOpDts=opOrderDao.queryBlOpDts(pkPv);
        return blOpDts;
    }

    public void updateBDOrdSet() {
        String sql = "select distinct BOSD.PK_ORDSET, " +
                "                case when BO1.PK_PARENT is null or BO1.PK_PARENT = '' then BO1.CODE else BO2.CODE end as code " +
                "from BD_ORD_SET_DT BOSD " +
                "         left join BD_ORD BO on BO.PK_ORD = BOSD.PK_ORD " +
                "         left join BD_ORDTYPE BO1 on BO.CODE_ORDTYPE = BO1.CODE " +
                "         left join BD_ORDTYPE BO2 on BO2.PK_ORDTYPE = BO1.PK_PARENT " +
                "where BOSD.DEL_FLAG = '0'  " +
                "order by PK_ORDSET";
        List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sql);
        if (CollectionUtils.isEmpty(mapList)) {
            return;
        }
        List<String> sqls = new ArrayList<>(16);
        String pd = "";
        String lis = "";
        String ris = "";
        String zl = "";
        String pkOrdset = "";
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map = mapList.get(i);
            String code = MapUtils.getString(map, "code");
            String pkOrdSet1 = MapUtils.getString(map, "pkOrdset");
            if (StringUtils.isWhitespace(pkOrdset)) {
                pkOrdset = pkOrdSet1;
            }
            if (!pkOrdset.equals(pkOrdSet1)) {
                String codeOrdType = (pd + ris + lis + zl).trim();
                if (!StringUtils.isWhitespace(codeOrdType)) {
                    sqls.add("update BD_ORD_SET set CODE_ORD_TYPE='" + codeOrdType + "' where PK_ORDSET='" + pkOrdset + "'");
                }
                pkOrdset = pkOrdSet1;
                pd = lis = ris = zl = "";
            }
            //药品
            if (StringUtils.isBlank(code) || "01".equals(code)) {
                pd = "01";
            }
            //检验
            else if ("03".equals(code)) {
                lis = "03";
            }
            //检查
            else if ("02".equals(code)) {
                ris = "02";
            }
            //治疗
            else {
                zl = "05";
            }
            if (i == mapList.size() - 1) {
                String codeOrdType = (pd + ris + lis + zl).trim();
                if (!StringUtils.isWhitespace(codeOrdType)) {
                    sqls.add("update BD_ORD_SET set CODE_ORD_TYPE='" + codeOrdType + "' where PK_ORDSET='" + pkOrdset + "'");
                }
            }
        }
        if (!CollectionUtils.isEmpty(sqls)) {
            DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));
        }
    }

    /**
     * 根据医嘱主键查询检查申请详情
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryRisApplyInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<String> pkCnords = (List<String>) MapUtils.getObject(paramMap, "pkCnords");
        List<Map<String, Object>> rtn = opOrderDao.qryRisApplyInfo(pkCnords);
        return rtn;
    }

    /**
     * 根据医嘱主键查询检验申请详情
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryLabApplyInfo(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<String> pkCnords = (List<String>) MapUtils.getObject(paramMap, "pkCnords");
        List<Map<String, Object>> rtn = opOrderDao.qryLabApplyInfo(pkCnords);
        return rtn;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateMch(List<MchVo> mchVo) {
        List<String> sqls = new ArrayList<>(mchVo.size());
        mchVo.forEach(m -> {
            StringBuilder stringBuilder = new StringBuilder("update Woman_Subsidies  ");
            if (m.getIsCheck() == 1) {
                stringBuilder.append("  set IsCheck='1', ChkOrganCode='44200101', Src='NHIS', ChkOrgan='中山市博爱医院',ChkDate=getdate(),Ext='" + m.getOrderSn() + "'");
            } else {
                stringBuilder.append("  set IsCheck='0', ChkOrganCode=null, Src=null, ChkOrgan=null,ChkDate= null,Ext=null ");
            }
            stringBuilder.append(" where HealthNo='" + m.getHealthNo() + "'");
            stringBuilder.append(" and ItemCode='" + m.getMchCode() + "'");
            sqls.add(stringBuilder.toString());
        });
        DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveHealthNo(MchVo mchVo) {
        String sql = "select PM.PK_PI,PC.CARD_NO,PC.PK_PICARD,PC.DT_CARDTYPE " +
                "from PI_MASTER PM left join PI_CARD PC on PM.PK_PI=PC.PK_PI where PM.CODE_OP=?";
        List<PiCard> piCards = DataBaseHelper.queryForList(sql, PiCard.class, new Object[]{mchVo.getCodeOp()});
        List<String> pkPicard = piCards.stream().filter(m -> "06".equals(m.getDtCardtype())).map(m -> m.getPkPicard()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(pkPicard)&&pkPicard.size()>0) {
            if (piCards.size() == 1) {
                sql = "update PI_CARD set CARD_NO='" + mchVo.getHealthNo()
                        + "' where PK_PICARD in(" + CommonUtils.convertListToSqlInPart(pkPicard) + ")";
                DataBaseHelper.update(sql);
                return;
            } else {
                sql = "delete PI_CARD where PK_PICARD in(" + CommonUtils.convertListToSqlInPart(pkPicard) + ")";
                DataBaseHelper.update(sql);
            }
        }
        PiCard piCard = new PiCard();
        piCard.setPkPi(piCards.get(0).getPkPi());
        piCard.setSortNo(6);
        piCard.setDtCardtype("06");
        piCard.setInnerNo(mchVo.getHealthNo());
        piCard.setCardNo(mchVo.getHealthNo());
        piCard.setDateBegin(new Date());
        piCard.setDateEnd(new Date());
        piCard.setFlagActive("1");
        piCard.setEuStatus("0");
        piCard.setDeposit(new BigDecimal("0"));
        ApplicationUtils.setDefaultValue(piCard, true);
        DataBaseHelper.insertBean(piCard);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PiCard getPicard(String codeOp) {
        String sql = "select CARD_NO from PI_CARD PC where DT_CARDTYPE = '06' and PK_PI in ( " +
                "        select PI_MASTER.PK_PI from PI_MASTER where  CODE_OP = ?  )";
        return DataBaseHelper.queryForBean(sql, PiCard.class, new Object[]{codeOp});
    }
    
    /**
     * 博爱-欠费管理费用信息查询重构
     * 需求-8246
     * 交易号：022003026058
     * @param param
     * @param user
     * @return
     */
	public Map<String, Object> getOrdItemInfoFee(String param, IUser user) {
    	Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
    	Map<String, Object> OrdItem = new HashMap<>();
    	if(StringUtils.isNotEmpty(MapUtils.getString(paramMap, "pkPv"))) {
    		List<Map<String, Object>>  blOpDtList=opOrderDao.queryPkPvBlOpDtPts(paramMap);
    		List<Map<String, Object>>  cnOrderList=opOrderDao.queryPkCnOrderInfro(paramMap);
    		OrdItem.put("OrderList", cnOrderList);
    		OrdItem.put("ItemOpCgList", blOpDtList);
    	} 
    	return OrdItem;
    }
    
    /**
     * 根据pkCnord删除计费信息
     * 交易号：022003026059
     * @param param
     * @param user
     */
    public void delPkCnordBlFree(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (StringUtils.isEmpty(MapUtils.getString(paramMap, "pkCnord"))) {
            throw new BusException("传入医嘱项目主键无效！");
        }
        List<BlOpDt> blOpDts = DataBaseHelper.queryForList(" select * from bl_op_dt where pk_cnord=? and del_flag='0' ", BlOpDt.class, MapUtils.getString(paramMap, "pkCnord"));
        if (blOpDts == null || blOpDts.size() == 0) {
            return;
        }
        for (BlOpDt vo : blOpDts) {
            if ("1".equals(vo.getFlagSettle())) {
                throw new BusException("费用已收，不能能删除!");
            }
            vo.setDelFlag("1");//删除标志
        }
        
        if (StringUtils.isNotEmpty(MapUtils.getString(paramMap, "pkCnord"))){
            opOrderDao.deleteExAssistOcc(MapUtils.getString(paramMap, "pkCnord"));
            opOrderDao.deleteExAssistOccDt(MapUtils.getString(paramMap, "pkCnord"));
        }

        DataBaseHelper.batchUpdate("delete from BL_OP_DT where PK_CGOP=:pkCgop ", blOpDts);
    }

}
