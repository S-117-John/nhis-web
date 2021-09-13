package com.zebone.nhis.ma.pub.zsrm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;
import com.zebone.nhis.common.module.scm.st.*;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.syx.vo.Data;
import com.zebone.nhis.ma.pub.zsrm.dao.ZsrmHrpPdPlanMapper;
import com.zebone.nhis.ma.pub.zsrm.handler.ZsrmHrpPostHandler;
import com.zebone.nhis.ma.pub.zsrm.support.ZsrmHrpResult;
import com.zebone.nhis.ma.pub.zsrm.vo.*;
import com.zebone.nhis.ma.pub.zsrm.vo.PdReprice;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * 此类做HRP具体业务实现
 */
@Service
public class ZsrmHrpPostService {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ZsrmHrpPostService.class);
    @Resource
    ZsrmHrpPdPlanMapper pdPlanMapper;

    /**
     *  药房退药
     * @param param
     * @return
     */
    public HrpPubReceiveVo sureReturnBackDrug(String param){
        List<ApplyVo> appList = pdPlanMapper.pharmacyDrugWithdrawal(param);
        HrpPubReceiveVo receiveVo = builParameter(appList,"HISYFTY");
        return receiveVo;
    }

    /**
     * 药库确认药房退药申请
     */
    public String saveDrugStorageConfirmReturnBackDrug(String param){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String note = paramMap.get("status").toString();
        String pkPdst = paramMap.get("sourceId").toString();
        StringBuilder sb= new StringBuilder();
        sb.append("Update pd_st set note=note+? where pk_pdst=?");
        int result =  DataBaseHelper.update(sb.toString(),new Object[] {note, pkPdst});
        return String.valueOf(result);
    }

    /**
     * 查询出库信息（出库处理）
     */
    public ZsrmHrpResult getHrpOutStorePd(String param){
        return ZsrmHrpResult.resultMessage(null);
    }


    /**
     * 获取药库出库所需的数据
     * @param param
     */
    public ZsrmHrpPdSt selectByCondition(HrpPubReceiveVo<List<HrpAllocaReqVo>> param,String orderId){
        List<HrpAllocaReqVo> pdList = (List<HrpAllocaReqVo>)param.getItems();
        String locationFromSql = "select PK_PDPLAN pkPdplan,PK_DEPT pkDeptLk,PK_ORG pkOrgLk,PK_STORE pkStoreLk from pd_plan where PK_PDPLAN= ?";
        String codeDeptSql = "select PK_STORE pkStoreSt,PK_DEPT pkDeptSt, PK_ORG pkOrgLk from BD_STORE where pk_dept in (select pk_dept from bd_ou_dept where CODE_DEPT= ?)";
        ZsrmHrpPdSt zsrmHrpPdSt = new ZsrmHrpPdSt();
        if(!orderId.equals("false")){
            zsrmHrpPdSt = DataBaseHelper.queryForBean(locationFromSql,ZsrmHrpPdSt.class, orderId);
            if(zsrmHrpPdSt == null){
                throw new BusException("不存在的orderId : " + orderId);
            }
        }else{
            String locationtosql = "select pk_store pkStoreLk, pk_dept pkDeptLk from bd_store where pk_dept in ( select pk_dept from bd_ou_dept where code_dept=?)";
            zsrmHrpPdSt = DataBaseHelper.queryForBean(locationtosql,ZsrmHrpPdSt.class, param.getLocationTo());
            if(zsrmHrpPdSt == null) throw new BusException("目标库房" + param.getLocationTo() + "不存在");
        }
        ZsrmHrpPdSt pdst2 = DataBaseHelper.queryForBean(codeDeptSql,ZsrmHrpPdSt.class,param.getLocationFrom());
        if(pdst2 == null) throw  new BusException("来源库房" + param.getLocationFrom() + "不存在");
        ZsrmHrpPdSt pdst3 = DataBaseHelper.queryForBean("select PK_EMP pkEmpChk, NAME_EMP nameEmpChk from bd_ou_employee where del_flag = '0' and CODE_EMP= ? ",ZsrmHrpPdSt.class, param.getHandleUser());
        if(pdst3 == null) throw new BusException("人员" + param.getHandleUser() + "不存在");
        zsrmHrpPdSt.setPkEmpChk(pdst3.getPkEmpChk());
        zsrmHrpPdSt.setNameEmpChk(pdst3.getNameEmpChk());
        if(zsrmHrpPdSt.getPkOrgLk() == null||zsrmHrpPdSt.getPkOrgLk()==""){
            zsrmHrpPdSt.setPkOrgLk(pdst2.getPkOrgLk());
        }
        zsrmHrpPdSt.setPkStoreSt(pdst2.getPkStoreSt());
        zsrmHrpPdSt.setPkDeptSt(pdst2.getPkDeptSt());
        return zsrmHrpPdSt;
    }

    /**
     * 药库出库
     * @param param
     * @return
     */
    public PdSt saveHrpOutStorePd(HrpPubReceiveVo<List<HrpAllocaReqVo>> param) throws ParseException {
        List<HrpAllocaReqVo> pdList = (List<HrpAllocaReqVo>)param.getItems();
        List<PdSt> pdStList = new ArrayList<>();
        List<PdStDetail> detailList = new ArrayList<>();
        // 筛选重复的orderId
        TreeSet<String> ts = new TreeSet<>();
        for(HrpAllocaReqVo vo : pdList){
            ts.add(vo.getOrderId());
        }
        // 按申领单主键区分入库
        for(String orderId : ts) {
            PdSt pdSt = new PdSt();
            ZsrmHrpPdSt zsrmHrpPdSt = selectByCondition(param, orderId);
            pdSt.setPkPdst(NHISUUID.getKeyId());
            pdSt.setPkOrg(zsrmHrpPdSt.getPkOrgLk());
            pdSt.setPkDeptSt(zsrmHrpPdSt.getPkDeptSt());
            pdSt.setPkStoreSt(zsrmHrpPdSt.getPkStoreSt());
            pdSt.setDtSttype("0201");
            pdSt.setCodeSt(param.getOrderNo());
            pdSt.setNameSt("HRP调拨入库单");
            pdSt.setEuDirect("-1");
            pdSt.setDateSt(new Date());
            pdSt.setEuStatus("1");
            pdSt.setPkOrgLk(zsrmHrpPdSt.getPkOrgLk());
            pdSt.setPkDeptLk(zsrmHrpPdSt.getPkDeptLk());
            pdSt.setPkStoreLk(zsrmHrpPdSt.getPkStoreLk());
            pdSt.setPkPdplan(orderId);
            pdSt.setFlagPay("0");
            pdSt.setPkEmpOp(zsrmHrpPdSt.getPkEmpChk());
            pdSt.setNameEmpOp(zsrmHrpPdSt.getNameEmpChk());
            pdSt.setDateChk(DateUtils.strToDate(param.getOrderDate()));
            pdSt.setFlagChk("0");
            pdSt.setPkEmpChk(zsrmHrpPdSt.getPkEmpChk());
            pdSt.setNameEmpChk(zsrmHrpPdSt.getNameEmpChk());
            pdSt.setNote(param.getRemarks());
            pdSt.setFlagPu("0");
            pdSt.setCreator(zsrmHrpPdSt.getPkEmpChk());
            pdSt.setCreateTime(new Date());
            pdSt.setDelFlag("0");
            pdSt.setTs(new Date());
            pdStList.add(pdSt);
            for (HrpAllocaReqVo detailVo : pdList) {
                // 相同主键时执行
                if(detailVo.getOrderId().equals(orderId)) {
                    PdPlanDetail pdPlanDetail = pdPlanMapper.getPdplanDetailByPkPdplandt(detailVo.getOrderlineId());
                    if (pdPlanDetail == null) {
                        pdPlanDetail = pdPlanMapper.getPdInfoByKeyCode(detailVo.getMedicineCode());
                        if (pdPlanDetail == null) throw new BusException("药品" + detailVo.getMedicineCode() + "不存在");
                    }
                    PdStDetail detail = new PdStDetail();
                    // 主键
                    detail.setPkPdstdt(NHISUUID.getKeyId());
                    detail.setPkOrg(pdPlanDetail.getPkOrg());
                    detail.setPkPdst(pdSt.getPkPdst());
                    detail.setSortNo(pdPlanDetail.getSortNo());
                    detail.setPkPd(pdPlanDetail.getPkPd());
                    detail.setPkUnitPack(pdPlanDetail.getPkUnit());
                    detail.setPackSize(pdPlanDetail.getPackSize());
                    detail.setQuanMin(detailVo.getQuantity());
                    detail.setQuanPack(detailVo.getQuantity());
                    detail.setQuanOutstore(0.0);
                    detail.setPriceCost(detailVo.getPrice());
                    detail.setAmountCost(detailVo.getPrice() / pdPlanDetail.getPackSize() * detailVo.getQuantity());
                    detail.setPrice(detailVo.getPriceS());
                    detail.setAmount(detailVo.getPriceS() / pdPlanDetail.getPackSize() * detailVo.getQuantity());
                    detail.setBatchNo(detailVo.getLot());
                    detail.setDateExpire(DateUtils.parseDate(detailVo.getLotDate(), "yyyy-MM-dd"));
                    detail.setDisc(1.0);
                    detail.setFlagChkRpt("0");
                    detail.setFlagPay("0");
                    detail.setFlagFinish("0");
                    detail.setCreator(zsrmHrpPdSt.getPkEmpChk());
                    detail.setCreateTime(new Date());
                    detail.setDelFlag("0");
                    detail.setTs(new Date());
                    detail.setNote(detailVo.getLineId());
                    detailList.add(detail);
                }
            }
            // 修改申领单执行状态
            DataBaseHelper.batchUpdate("update pd_plan set eu_status='2' where pk_pdplan='" + orderId + "'");
        }
        // 数据插入库存表
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdSt.class), pdStList);
        // 数据插入库存详细表
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), detailList);
        return pdStList.get(0);
    }

    /**
     * 调拨申请发送
     * @param pkPdplan
     * @return
     */
    public HrpPubReceiveVo sendAllocaMethod(String pkPdplan){
        //TODO 通过pkPdplan 查询sql ,Mapper 方式；
        List<ApplyVo> applyVoList = pdPlanMapper.getPdplanByPkPdplan(pkPdplan);
        if(applyVoList.size() == 0){
            throw new BusException("没有任何可以提交的数据！");
        }
        HrpPubReceiveVo receiveVo = builParameter(applyVoList,"HISYFSL");
        return receiveVo;
    }

    /**
     * 调拨申请参数构建
     * @param param
     * @return
     */
    public HrpPubReceiveVo builParameter(List<ApplyVo> param, String operation){
        ApplyVo  applyVo = param.get(0);
        List list = new ArrayList();
        HrpPubReceiveVo pubReceiveVo = new HrpPubReceiveVo();
        pubReceiveVo.setOperation(operation);
        pubReceiveVo.setHospital(applyVo.getHospital());
        pubReceiveVo.setSourceId(applyVo.getSourceId());
        pubReceiveVo.setOrderNo(applyVo.getOrderNo());
        pubReceiveVo.setStatus(applyVo.getStatus());
        pubReceiveVo.setUserCode(applyVo.getUserCode());
        pubReceiveVo.setDeptCode(applyVo.getDeptCode());
        pubReceiveVo.setOrderDate(applyVo.getOrderDate());
        pubReceiveVo.setLocationTo(applyVo.getLocationTo());
        if(operation.equals("HISYFTY")){
            pubReceiveVo.setLocationFrom(applyVo.getLocationFrom());
        }else{
            pubReceiveVo.setAddress(applyVo.getAddress());
        }
        pubReceiveVo.setRemarks(applyVo.getRemarks()==null? "" : applyVo.getRemarks());
        for(ApplyVo vo : param){
            HrpAllocaReqVo allocaReqVo = new HrpAllocaReqVo();
            allocaReqVo.setLineId(vo.getLineId());
            allocaReqVo.setLineNumber(vo.getLineNumber());
            allocaReqVo.setLocationFrom(vo.getLocationFrom());
            allocaReqVo.setLocationTo(vo.getLocationTo());
            allocaReqVo.setQuantity(vo.getQuantity());
            allocaReqVo.setUnit(vo.getUnit());
            if(operation.equals("HISYFTY")){
                allocaReqVo.setPrice(vo.getPrice());
                allocaReqVo.setLot(vo.getLot());
                allocaReqVo.setLotDate(vo.getLotDate()==null? "" : vo.getLotDate());
            }
            allocaReqVo.setMedicineCode(vo.getMedicineCode());

            list.add(allocaReqVo);
        }
        pubReceiveVo.setItems(list);
        return pubReceiveVo;
    }

    /**
     * 药品调价单
     * @param param
     * @return
     */
    public PdReprice savePriceHisStore(HrpPubReceiveVo<List<HrpAllocaReqVo>> param){
        List<HrpAllocaReqVo> allocaReqVo = (List<HrpAllocaReqVo>)param.getItems();
        List<PdRepriceDetail> list = new ArrayList<PdRepriceDetail>();
        List<PdRepriceHist> pdRepriceHists = new ArrayList<>();
        String dept = null;
        PdReprice pdReprice = new PdReprice();
        ApplicationUtils.setDefaultValue(pdReprice, true);
        if (param.getSourceId().length() != 32){
            pdReprice.setPkPdrep(NHISUUID.getKeyId());
            pdReprice.setNote(param.getSourceId());
        }else{
            pdReprice.setPkPdrep(param.getSourceId());
        }
        BdOuEmployee emp = DataBaseHelper.queryForBean(" select pk_emp pkEmp, name_emp nameEmp from BD_OU_EMPLOYEE where pk_emp= ?", BdOuEmployee.class, pdReprice.getCreator());
        PdReprice pkStore = DataBaseHelper.queryForBean("select PK_STORE pkStore from BD_STORE where PK_DEPT in(select pk_dept from BD_OU_DEPT where code_dept = ?)", PdReprice.class, param.getLocationTo());
        if(pkStore == null){
            throw new BusException("申请调价部门： "+param.getLocationTo()+" 不存在");
        }
        pdReprice.setDtReptype("03");
        pdReprice.setCodeRep(param.getOrderNo());
        pdReprice.setDateEffe(DateUtils.strToDate(param.getAdjustTime()));
        pdReprice.setDateInput(new Date());
        pdReprice.setPkEmpInput(emp.getPkEmp());
        pdReprice.setNameEmpInput(emp.getNameEmp());
        pdReprice.setDateChk(new Date());
        pdReprice.setPkEmpChk(emp.getPkEmp());
        pdReprice.setNameEmpChk(emp.getNameEmp());
        pdReprice.setPkStore(pkStore.getPkStore());
        pdReprice.setDateEffe(new Date());
        pdReprice.setFlagChk("1");
        pdReprice.setEuStatus("2");
        pdReprice.setFlagCanc("0");
        pdReprice.setEuRepmode("0");
        pdReprice.setCreateTime(param.getCreateDate());
        for(HrpAllocaReqVo reqVo : allocaReqVo){
            PdRepriceDetail packSize = DataBaseHelper.queryForBean("select pack_size packSize, pk_pd pkPd , PK_UNIT_PACK pkUnitPack from bd_pd where code = ?", PdRepriceDetail.class, reqVo.getMedicineCode());
            DataBaseHelper.update("update bd_pd pd set pd.price= ? where pd.pk_pd= ? ", reqVo.getNewPrice(),packSize.getPkPd());
            StringBuilder sb = new StringBuilder();
            sb.append("update pd_st_detail dt set dt.price=?");
            sb.append("where exists");
            sb.append("(select 1 from pd_st st where dt.pk_pdst=st.pk_pdst and st.eu_direct=1 and dt.flag_finish=0 and dt.pk_pd=? and st.pk_store_st in (?))");
            DataBaseHelper.update(sb.toString(), reqVo.getNewPrice(),packSize.getPkPd(), pkStore.getPkStore());
            PdStock pdStock = DataBaseHelper.queryForBean("select quan_min, amount from pd_stock where pk_pd=? and pk_store=?", PdStock.class, packSize.getPkPd(), pkStore.getPkStore());
            double amount = reqVo.getNewPrice() * pdStock.getQuanMin()/packSize.getPackSize();
            DataBaseHelper.update("update pd_stock stk set stk.price=? , stk.amount=?  where stk.pk_pd=? and stk.pk_store=?", reqVo.getNewPrice(), amount, packSize.getPkPd(),pkStore.getPkStore());
            PdRepriceDetail pdRepriceDetail = new PdRepriceDetail();
            if(reqVo.getLineId().length() != 32){
                pdRepriceDetail.setPkPdrepdt(NHISUUID.getKeyId());
                pdRepriceDetail.setNote(reqVo.getLineId());
            }else{
                pdRepriceDetail.setPkPdrepdt(reqVo.getLineId());
            }
            ApplicationUtils.setDefaultValue(pdRepriceDetail, true);
            pdRepriceDetail.setPkPdrep(pdReprice.getPkPdrep());
            pdRepriceDetail.setPkPd(packSize.getPkPd());
            pdRepriceDetail.setPkUnitPack(packSize.getPkUnitPack());
            pdRepriceDetail.setPackSize(packSize.getPackSize());
            pdRepriceDetail.setPriceOrg(Double.valueOf(reqVo.getOriginPrice()));
            pdRepriceDetail.setPrice(Double.valueOf(reqVo.getNewPrice()));
            List<String> histList = (List<String>)reqVo.getLocationFrom();
            int index = histList.size();
            if(index != 0){
                index = index - 1;
            }
            for(int i = 0; i<= index; i++){
                PdRepriceHist pdRepriceHist = new PdRepriceHist();
                pdRepriceHist.setPkPdrephist(NHISUUID.getKeyId());
                pdRepriceHist.setPkPdrepdt(pdRepriceDetail.getPkPdrepdt());
                pdRepriceHist .setPkOrg(pdRepriceDetail.getPkOrg());
                pdRepriceHist.setDtReptype(pdReprice.getDtReptype());
                pdRepriceHist.setCodeRep(pdReprice.getCodeRep());
                pdRepriceHist.setDateReprice(param.getCreateDate());
                pdRepriceHist.setAmountOrg(pdStock.getAmount());
                pdRepriceHist.setQuanRep(pdStock.getQuanMin());
                pdRepriceHist.setAmount(amount);
                pdRepriceHist.setAmountRep(amount-pdStock.getAmount());
                if(histList.size() != 0){
                    dept = DataBaseHelper.queryForBean(" select pk_dept from bd_ou_dept where CODE_DEPT= ?", BdOuDept.class, histList.get(i)).getPkDept();
                    if(dept == null){
                        throw new BusException("调价药房" + histList.get(i) + "不存在");
                    }
                }
                pdRepriceHist.setPkDept(dept);
                pdRepriceHist.setPkStore(pkStore.getPkStore());
                pdRepriceHist.setPkPd(packSize.getPkPd());
                pdRepriceHist.setPkUnitPack(packSize.getPkUnitPack());
                pdRepriceHist.setPackSize(packSize.getPackSize());
                pdRepriceHist.setPriceOrg(pdRepriceDetail.getPriceOrg());
                pdRepriceHist.setPrice(pdRepriceDetail.getPrice());
                pdRepriceHist.setCreator(pdReprice.getCreator());
                pdRepriceHist.setCreateTime(param.getCreateDate());
                pdRepriceHist.setDelFlag("0");
                pdRepriceHist.setTs(new Date());
                pdRepriceHists.add(pdRepriceHist);
            }
            list.add(pdRepriceDetail);
        }
        DataBaseHelper.insertBean(pdReprice);
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdRepriceHist.class),pdRepriceHists);
       int [] result =  DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdRepriceDetail.class), list);
        return pdReprice;
    }

    /**
     * HRP->His
     * 药库库存
     * @return
     */
    public Object qryHisStock(ZsrmHrpStock zsrmHrpStock){
        List<ApplyVo> applyVoList = pdPlanMapper.querylistToHrp(zsrmHrpStock);
        return applyVoList;
    }

    /**
     * HIS-->HRP
     * @return
     */
    public Map<String,Object> getHrpStock(String param){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        return paramMap;
    }
}
