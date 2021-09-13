package com.zebone.nhis.bl.pub.service;

import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOccDt;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ex.pub.support.OrderFreqCalCountHandler;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 门诊医技执行公共服务
 */
@Service
public class ExAssistPubService {
    /**
     * 生成医技执行单
     * @param dtList
     */
    public void generateExAssistOcc(List<BlOpDt> dtList){
        // 主键是医嘱主键
        Map<String, List<BlOpDt>> mapAssit = new HashMap<String, List<BlOpDt>>();
        for (BlOpDt blOpDt : dtList) {
            if (!BlcgUtil.converToTrueOrFalse(blOpDt.getFlagPd())) {
                //如果是挂号费，不做任何处理
                if("1".equals(blOpDt.getFlagPv()) ||
                        (!CommonUtils.isEmptyString(blOpDt.getEuAdditem()) && "1".equals(blOpDt.getEuAdditem()))) //校验是否是附加费用(2019-08-13加入逻辑)
                    continue;
                if (mapAssit.get(blOpDt.getPkCnord()) == null) {
                    mapAssit.put(blOpDt.getPkCnord(), new ArrayList<>());
                 }
                mapAssit.get(blOpDt.getPkCnord()).add(blOpDt);
            }
        }
        if(mapAssit==null)
            return;
        //保存医技执行记录
        saveExAssistOcc(mapAssit);
    }
    /**
     * 保存医技执行记录
     * @param mapAssit{pkCnord,List<BlOpDt>}
     * @throws BusException
     */
    public void saveExAssistOcc(Map<String, List<BlOpDt>> mapAssit) throws BusException {

        User user = UserContext.getUser();
        Set<String> pkCnords = new HashSet<String>();
        // 一个处方生成一个请领单
        for (Map.Entry<String, List<BlOpDt>> entry : mapAssit.entrySet()) {
            pkCnords.add(entry.getKey());
        }
        if (pkCnords.size() == 0)
            return;
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from  cn_order ord  where pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnords, "pk_cnord") + ")");
        List<CnOrder> cnOrders = DataBaseHelper.queryForList(sql.toString(), CnOrder.class, new Object[] {});
        List<ExAssistOcc> exAssistOccDaos = new ArrayList<ExAssistOcc>();
        List<ExAssistOccDt> exAssistOccDtList = new ArrayList<ExAssistOccDt>();
        for (CnOrder cnOrder : cnOrders) {
            //校验是否是主医嘱，只有主医嘱生成ex_assist_occ信息
            if(!cnOrder.getOrdsn().equals(cnOrder.getOrdsnParent()))
                continue;

            //校验主医嘱是否生成过医技执行
            String chkSql = "select count(1) as ct from ex_assist_occ where pk_cnord=? and flag_refund = '0'";
            int mapCount = DataBaseHelper.queryForScalar(chkSql,Integer.class, cnOrder.getPkCnord());
            if(mapCount==1)
                continue;

            // 校验重要字段
            if (cnOrder.getDays() == null) {
                throw new BusException("【" + cnOrder.getNameOrd() + "】的days字段为空");
            }
            if (cnOrder.getCodeFreq() == null) {
                throw new BusException("【" + cnOrder.getNameOrd() + "】的code_freq字段为空");
            }
            if (cnOrder.getCodeOrdtype() == null) {
                throw new BusException("【" + cnOrder.getNameOrd() + "】的code_ordtype字段为空");
            }
//			if(cnOrder.getDateStart() == null){	//如果开始时间为空，取当前时间。
//				Date date = new Date();
//				cnOrder.setDateStart(date);
//			}
            // 调用计算执行次数相关信息方法
            OrderFreqCalCountHandler ofcch = new OrderFreqCalCountHandler();
            if(cnOrder.getDateStart() == null){
                throw new BusException("【" + cnOrder.getNameOrd() + "】的date_start字段为空");
            }
            // 根据医嘱的开始时间和天数计算医嘱的结束时间
            Date dateEnd = DateUtils.getSpecifiedDay(cnOrder.getDateStart(), cnOrder.getDays().intValue());
            OrderAppExecVo orderAppExecVo = ofcch.calCount(cnOrder.getCodeOrdtype(), cnOrder.getCodeFreq(), cnOrder.getDateStart(), dateEnd, cnOrder.getQuan(),
                    false);
            BlOpDt blOpDtFirst = mapAssit.get(pkCnords.iterator().next()).get(0);
            int timeOcc = 1;
            for (OrderExecVo orderExecVo : orderAppExecVo.getExceList()) {
                ExAssistOcc eao = new ExAssistOcc();
                eao.setPkOrg(user.getPkOrg());
                eao.setPkCnord(cnOrder.getPkCnord());
                eao.setPkPv(cnOrder.getPkPv());
                eao.setPkPi(cnOrder.getPkPi());
                eao.setEuPvtype(cnOrder.getEuPvtype());
                String codeOcc = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_OPASEX);// 条形码
                eao.setCodeOcc(codeOcc);
                eao.setPkDept(cnOrder.getPkDept());
                eao.setPkEmpOrd(cnOrder.getPkEmpOrd());
                eao.setNameEmpOrd(cnOrder.getNameEmpOrd());
                eao.setDateOrd(cnOrder.getDateStart());
                eao.setDatePlan(orderExecVo.getExceTime());
                eao.setQuanOcc(orderExecVo.getQuanCur());
                eao.setTimesOcc(timeOcc);
                eao.setTimesTotal((int) orderAppExecVo.getCount());
                eao.setPkOrgOcc(cnOrder.getPkOrgExec());
                eao.setPkDeptOcc(cnOrder.getPkDeptExec());
                eao.setFlagCanc(EnumerateParameter.ZERO);
                eao.setFlagOcc(EnumerateParameter.ZERO);
                eao.setFlagPrt(EnumerateParameter.ZERO);
                eao.setInfantNo(EnumerateParameter.ZERO);
                eao.setEuStatus(EnumerateParameter.ZERO);
                eao.setFlagRefund(EnumerateParameter.ZERO);
                eao.setPkSettle(blOpDtFirst.getPkSettle());
                //2021.1.29-tjq-zsrm任务[5217]-添加执行诊区
                eao.setPkDeptArea(cnOrder.getPkDeptArea());//执行诊区
                ApplicationUtils.setDefaultValue(eao, true);
                exAssistOccDaos.add(eao);
                timeOcc++;
            }

        }

        //生成ex_assist_occ_dt明细
        if(exAssistOccDaos!=null && exAssistOccDaos.size() > 0){
            for(ExAssistOcc exOcc : exAssistOccDaos){
                //获取pk_cnord对应的父医嘱号
                Integer ordParent = qryOrdsnByPk(cnOrders,exOcc.getPkCnord());
                for(CnOrder ord : cnOrders){
                    if(ord.getOrdsnParent().equals(ordParent)){
                        ExAssistOccDt occDt = new ExAssistOccDt();
                        occDt.setPkAssocc(exOcc.getPkAssocc());
                        //主医嘱标志
                        if(ord.getOrdsn().equals(ord.getOrdsnParent()))
                            occDt.setFlagMaj("1");
                        else
                            occDt.setFlagMaj("0");
                        occDt.setPkCnord(ord.getPkCnord());
                        occDt.setPkOrd(ord.getPkOrd());
                        ApplicationUtils.setDefaultValue(occDt, true);
                        exAssistOccDtList.add(occDt);
                    }
                }
            }
        }

        if (exAssistOccDaos.size() > 0) {
            // 批量一次性插入
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExAssistOcc.class), exAssistOccDaos);
        }

        if(exAssistOccDtList.size()>0){
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExAssistOccDt.class), exAssistOccDtList);
        }

        for (ExAssistOccDt exAssistOccDt : exAssistOccDtList) {
            String sqlEustatus = "select count(*) from cn_ris_apply  where pk_cnord=?";
            Integer count = DataBaseHelper.queryForScalar(sqlEustatus, Integer.class, exAssistOccDt.getPkCnord());
            if(count==0){
                sqlEustatus = "select count(*)  from cn_lab_apply  where pk_cnord=?";
                count = DataBaseHelper.queryForScalar(sqlEustatus, Integer.class, exAssistOccDt.getPkCnord());
                String sqlUpdate = "update cn_lab_apply set eu_status='1' where pk_cnord=?";
                DataBaseHelper.update(sqlUpdate, new Object[]{exAssistOccDt.getPkCnord()});
            }else {
                String sqlUpdate = "update cn_ris_apply set eu_status='1' where pk_cnord=?";
                DataBaseHelper.update(sqlUpdate, new Object[]{exAssistOccDt.getPkCnord()});
            }
        }

    }
    /**
     * 获取pk_cnord对应的父医嘱号
     * @param orderList
     * @param pkCnord
     * @return
     */
    private Integer qryOrdsnByPk(List<CnOrder> orderList,String pkCnord){
        Integer ordParent = null;
        for(CnOrder ord : orderList){
            if(ord.getPkCnord().equals(pkCnord)){
                ordParent = ord.getOrdsnParent();
                break;
            }
        }
        return ordParent;
    }
}
