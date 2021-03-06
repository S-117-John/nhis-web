package com.zebone.nhis.webservice.lbzy.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.bl.pub.service.PareAccoutService;
import com.zebone.nhis.bl.pub.vo.BlPubSettleVo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.nhis.webservice.lbzy.dao.SelfServiceMapper;
import com.zebone.nhis.webservice.lbzy.model.QueryToPayDetailItem;
import com.zebone.nhis.webservice.lbzy.model.QueryToPayItem;
import com.zebone.nhis.webservice.lbzy.model.QueryToPayList;
import com.zebone.nhis.webservice.lbzy.model.QueryToPayResult;
import com.zebone.nhis.webservice.lbzy.model.ipin.BlOpDtFeeVo;
import com.zebone.nhis.webservice.lbzy.model.paydt.QueryToPayPres;
import com.zebone.nhis.webservice.lbzy.model.paydt.QueryToPresList;
import com.zebone.nhis.webservice.lbzy.model.paydt.QueryToPresResult;
import com.zebone.nhis.webservice.lbzy.model.reg.RegApptVo;
import com.zebone.nhis.webservice.lbzy.support.LbzySelfTools;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LbzySelfService {

    @Resource
    private SelfServiceMapper selfServiceMapper;

    @Resource
    private PareAccoutService pareAccoutService;

    /**
     * ?????????????????????????????????
     * @param codePi
     * @return
     */
    public QueryToPayResult queryToPayRegList(String codePi){
        List<QueryToPayItem> items = selfServiceMapper.qryPayPresRegInfo(codePi);
        if(CollectionUtils.isEmpty(items)){
            throw new BusException("??????????????????");
        }
        QueryToPayList queryToPayList = new QueryToPayList();
        queryToPayList.setObjectList(Lists.newArrayList());
        //??????????????????????????????
        items.stream().collect(Collectors.groupingBy(QueryToPayItem::getRegFlow))
                .forEach((k,v) ->{
                    Optional<QueryToPayItem> merge = v.stream().reduce((v1,v2)->{
                        v1.setTotalCost(MathUtils.add(v1.getTotalCost(), v2.getTotalCost()));
                        v1.setRecipeSEQS(Optional.ofNullable(v1.getRecipeSEQS()).orElse(new HashSet<>()));
                        v1.getRecipeSEQS().add(v1.getRecipeSEQ());
                        v1.getRecipeSEQS().add(v2.getRecipeSEQ());
                        return v1;
                    });
                    merge.ifPresent(queryToPayList.getObjectList()::add);
                });
        queryToPayList.setObjectList(queryToPayList.getObjectList().stream().sorted(Comparator.comparing(QueryToPayItem::getRegFlow).thenComparing(QueryToPayItem::getSeeTime,Comparator.reverseOrder())).collect(Collectors.toList()));
        queryToPayList.getObjectList().stream()
                .forEach(vo -> {
                    if(CollectionUtils.isNotEmpty(vo.getRecipeSEQS())){
                        vo.setRecipeSEQ(StringUtils.join(vo.getRecipeSEQS(),","));
                    }
                });
        QueryToPayResult queryToPayResult = new QueryToPayResult();
        queryToPayResult.list = queryToPayList;
        queryToPayResult.resultCode = "0";
        return queryToPayResult;
    }

    public QueryToPresResult queryToPayList(String codePi, String codePv){
        List<QueryToPayPres> items = selfServiceMapper.qryPayPresInfo(codePi);
        if(CollectionUtils.isEmpty(items)){
            throw new BusException("??????????????????");
        }
        QueryToPresList queryToPayList = new QueryToPresList();
        queryToPayList.setObjectList(Lists.newArrayList());
        //???????????????+?????????????????????
        items.stream().filter(vo -> StringUtils.isBlank(codePv)?true:codePv.equals(vo.getRegFlow())).collect(Collectors.groupingBy(vo -> vo.getRegFlow()+ "_"+vo.getRecipeSEQ()))
                .forEach((k,v) ->{
                    Optional<QueryToPayPres> merge = v.stream().reduce((v1,v2)->{
                        v1.setFee(MathUtils.add(v1.getFee(),v2.getFee()));
                        return v1;
                    });
                    merge.ifPresent(queryToPayList.getObjectList()::add);
                });
        queryToPayList.setObjectList(queryToPayList.getObjectList().stream()
                .sorted(Comparator.comparing(QueryToPayPres::getRegFlow).thenComparing(QueryToPayPres::getRecipeSEQ, Comparator.reverseOrder()))
                .collect(Collectors.toList()));
        QueryToPresResult queryToPayResult = new QueryToPresResult();
        queryToPayResult.list = queryToPayList;
        queryToPayResult.resultCode = "0";
        return queryToPayResult;
    }
    /**
     * ?????????????????????????????????
     * @return
     */
    public List<QueryToPayDetailItem> queryToPayDetail(Map<String,Object> paramMap){
        List<QueryToPayDetailItem> items = selfServiceMapper.qryPayPresDetail(paramMap);
        //?????? +-????????? ?????????,???????????????????????????????????????,
        List<QueryToPayDetailItem> pvCollect = Lists.newArrayList();
        //?????????????????????
        Set<String> pkCgops = new HashSet<>();
        items.stream().filter(v -> pkCgops.add(v.getPkCgop())).filter(vo -> "1".equals(vo.getFlagPv()))
        .collect(Collectors.groupingBy(QueryToPayDetailItem::getPkItem))
        .forEach((k,v) ->{
            Optional<QueryToPayDetailItem> sum = v.stream().reduce((v1, v2)->{
                v1.setQuantity(MathUtils.add(v1.getQuantity(), v2.getQuantity()));
                v1.setCost(MathUtils.add(v1.getCost(), v2.getCost()));
                return v1;
            });
            if(sum.get().getQuantity()>0){
                pvCollect.add(sum.get());
            }
        });
        items.removeIf(vo -> "1".equals(vo.getFlagPv()));
        items.addAll(pvCollect);
        return items;
    }

    /**
     * ????????????,,??????????????????????????????????????????????????????
     * @param paramMap
     * @return
     */
    public Map<String,Object> opPay(Map<String,Object> paramMap){
        String codePi = MapUtils.getString(paramMap,"PatientID");
        String codePv = MapUtils.getString(paramMap,"RegisterNO");
        String presCode = MapUtils.getString(paramMap,"RecipeSEQ");
        Map<String,Object> insInfo = (Map<String, Object>) MapUtils.getObject(paramMap,"SITransInfo");
        //TODO ????????????????????????????????????????????????????????????????????????Src\Zebone.His.InterfaceServices\Zebone.His.Medicare.SuZhou\SuZhouNhMedicareService.cs 1558
        // InsSzybService.saveSettlementReturnData
        // ??????opCgTransforVo.setAmtInsuThird ??????????????????
        Map<String,Object> resultMap = Maps.newHashMap();
        List<Map<String,Object>> list = getListNodes(paramMap);
        if(CollectionUtils.isEmpty(list)){
            throw new BusException("??????????????????????????????");
        }
        List<String> codePvs = Lists.newArrayList(StringUtils.split(codePv,","));
        if(codePvs.stream().filter(vo -> StringUtils.isBlank(vo)).count() >0){
            throw new BusException("RegisterNO??????????????????");
        }
        if(list.stream().filter(vo -> Double.compare(0,MapUtils.getDoubleValue(vo,"Amount",0D))>=0
                || StringUtils.isBlank(MapUtils.getString(vo,"POSTransNO"))).count()>0){
            throw new BusException("????????????????????????????????????????????????????????????0?????????POSTransNO????????????");
        }
        double inAmount = list.stream().mapToDouble(vo -> MapUtils.getDouble(vo, "Amount")).sum();
        List<BlOpDtFeeVo> blOpDtFeeVos = selfServiceMapper.qryPayBlOpDt(codePi, codePvs);
        if(blOpDtFeeVos.size() ==0){
            throw new BusException("?????????????????????????????????");
        }
        //????????????????????????????????????????????????
        List<String> filterCodes = Lists.newArrayList();
        if(StringUtils.isNotBlank(presCode)){
            filterCodes.addAll(Arrays.asList(presCode.split(",")));
        }
        List<BlOpDt> blOpDtList = blOpDtFeeVos.stream()
                .filter(vo -> filterCodes.size()==0 || filterCodes.contains(vo.getPresNo()) || filterCodes.contains(vo.getComNo()))
                .map(vo -> {
            BlOpDt blOpDt = new BlOpDt();
            BeanUtils.copyPropertiesIgnoreNull(vo, blOpDt);
            return blOpDt;
        }).collect(Collectors.toList());
        if(blOpDtList.size() ==0){
            throw new BusException("?????????????????????????????????");
        }
        double totalAmount = blOpDtList.stream().mapToDouble(BlOpDt::getAmount).sum();
        if(!MathUtils.equ(MathUtils.upRound(totalAmount,2), MathUtils.upRound(inAmount,2))){
            throw new BusException("?????????????????????????????????????????????????????????");
        }
        Map<String, List<BlOpDt>> collect = blOpDtList.stream().collect(Collectors.groupingBy(BlOpDt::getPkPv));
        final String delKey = "1Del$";
        collect.forEach((pkPv,dtList) ->{
            String pkPi = dtList.get(0).getPkPi();
            OpCgTransforVo opCgTransforVo =new OpCgTransforVo();
            opCgTransforVo.setAmtInsuThird(BigDecimal.valueOf(0D));//??????????????????
            List<BlDeposit> depositList = new ArrayList<>();
            //??????????????????????????????????????????????????????????????????????????????????????????-???????????????????????????????????????????????????
            list.removeIf(vo -> MapUtils.getBooleanValue(vo,delKey));
            double pvAmount = dtList.stream().mapToDouble(BlOpDt::getAmount).sum();
            for (Map<String, Object> map : list) {
                double pvAmountIn = MapUtils.getDoubleValue(map, "Amount", 0D);
                double sub;
                if((sub = MathUtils.sub(pvAmount, pvAmountIn))>=0){
                    pvAmount = sub;
                    map.put(delKey,true);
                }else {
                    pvAmountIn = pvAmount;
                    double comp = MathUtils.sub(pvAmountIn, pvAmount);
                    map.put(delKey,MathUtils.equ(comp, 0D));
                    map.put("Amount", comp);
                }
                BlDeposit blDeposit = new BlDeposit();
                blDeposit.setDtPaymode(LbzySelfTools.getPayCode(1,MapUtils.getString(map, "PayTypeID")));//????????????
                blDeposit.setPaymodeName(MapUtils.getString(map, "PayModeName"));
                blDeposit.setAmount(BigDecimal.valueOf(pvAmountIn));
                blDeposit.setPkPv(pkPv);
                blDeposit.setPkPi(pkPi);
                if(("2").equals(blDeposit.getDtPaymode())){
                    Map<String, BigDecimal> pamMap = pareAccoutService.getPatiAccountAvailableBalance(pkPi);
                    if(pamMap.isEmpty()){
                        throw new BusException("??????????????????");
                    }
                    //???????????????????????????
                    String sql=" select nvl(sum(DEPOSIT) ,0) as deposit from PI_CARD where FLAG_ACTIVE in ('0','1') and EU_STATUS in ('0','1','2') and PK_PI= ?";
                    Map<String,Object > cardMap= DataBaseHelper.queryForMap(sql,pkPi);
                    BigDecimal nAmout=BigDecimal.valueOf(Double.valueOf(String.valueOf(cardMap.get("deposit"))));
                    BigDecimal BalanceAmout =BigDecimal.valueOf(MapUtils.getDoubleValue(pamMap,"acc", 0D));
                    double amount = MapUtils.getDouble(map, "Amount",0d);
                    if(Double.compare(amount,BalanceAmout.subtract(nAmout).doubleValue()) >0){
                        throw new BusException("????????????????????????????????????????????????");
                    }
                } else if(!("1").equals(blDeposit.getDtPaymode())){
                    //POS?????????|?????????|?????????|POS????????????|?????????|??????|??????
                    String pos = MapUtils.getString(map, "POSTransNO");
                    String mgStr[] = pos.split("\\|");
                    blDeposit.setPayInfo(mgStr[0]);
                    blDeposit.setSerialNum(mgStr[0]);
                    blDeposit.setNote(pos);
                    blDeposit.setDtBank(MapUtils.getString(map, "OpenBank"));
                    addBlExtPay(blDeposit);
                }
                depositList.add(blDeposit);
            }
            BlPubSettleVo blPubSettleVo = new BlPubSettleVo();
            blPubSettleVo.setPkPi(pkPi);
            blPubSettleVo.setPkPv(pkPv);
            blPubSettleVo.setBlOpDts(blOpDtList);
            blPubSettleVo.setDepositList(depositList);
            blPubSettleVo.setPkBlOpDtInSql(CommonUtils.convertSetToSqlInPart(depositList.stream().map(BlDeposit::getPkDepo).collect(Collectors.toSet()), "pk_cgop"));//??????????????????
            opCgTransforVo.setPkPi(pkPi);
            opCgTransforVo.setPkPv(pkPv);
            opCgTransforVo.setBlOpDts(blOpDtList);
            opCgTransforVo.setBlDeposits(depositList);
            opCgTransforVo.setInvStatus("-2");//???????????????

            ResponseJson  result =  ApplicationUtils.execService("BL", "opCgSettlementService", "mergeOpcgAccountedSettlement", opCgTransforVo, UserContext.getUser());
            if(result.getStatus()== Constant.SUC){
                OpCgTransforVo rsOp = (OpCgTransforVo)result.getData();
                //????????????????????????
                List<BlDeposit> qryDepos = DataBaseHelper.queryForList("select PK_DEPO,PAY_INFO from BL_DEPOSIT where PK_SETTLE=? and PK_PV=?"
                        , BlDeposit.class, new Object[]{rsOp.getPkSettle(), pkPv});
                if(CollectionUtils.isNotEmpty(qryDepos)){
                    qryDepos.forEach(vo -> DataBaseHelper.update("update BL_EXT_PAY set PK_DEPO=?,PK_SETTLE=? where PK_PV=? and SERIAL_NO=?",new Object[]{vo.getPkDepo(),rsOp.getPkSettle(),pkPv,vo.getPayInfo()}));
                }
                List<Map<String,Object>> settleList = DataBaseHelper.queryForList("select CODE_ST,to_char(DATE_ST,'yyyy-MM-dd HH24:mi:ss') date_st,nvl(occ.WINNO_CONF,'???') wino from bl_settle st left join EX_PRES_OCC occ on st.PK_SETTLE=occ.PK_SETTLE where st.pk_settle=? and st.FLAG_CANC='0'"
                        , new Object[]{rsOp.getPkSettle()});
                if(settleList.size() ==0){
                    throw new BusException("????????????????????????");
                }
                resultMap.put("TranSerialNO",MapUtils.getString(DataBaseHelper.queryForMap("select code_op from pi_master where pk_pi = ?",new Object[]{pkPi}), "codeOp"));
                resultMap.put("TranTime",MapUtils.getString(settleList.get(0), "dateSt"));
                resultMap.put("SendWin",MapUtils.getString(settleList.get(0), "wino"));
            } else {
                throw new BusException(StringUtils.trimToEmpty(result.getErrorMessage())+" "+ StringUtils.trimToEmpty(result.getDesc()));
            }
        });
        resultMap.put("ResultCode","0");
        return resultMap;
    }

    public Map<String,Object>  prePayInHospital(Map<String,Object> paramMap){
        List<Map<String,Object>> list = getListNodes(paramMap);
        if(CollectionUtils.isEmpty(list)){
            throw new BusException("??????????????????????????????");
        }

        String sql = "select pv.PK_PI,pv.PK_PV,pv.EU_PVTYPE from PV_ENCOUNTER pv inner join pi_master pi on pv.PK_PI=pi.PK_PI where pi.CODE_IP=? and pi.NAME_PI=? and pv.EU_PVTYPE='3' and pv.EU_STATUS<3";
        List<Map<String, Object>> piList = DataBaseHelper.queryForList(sql, new Object[]{MapUtils.getString(paramMap, "InPatientID"), MapUtils.getString(paramMap, "InPatientName")});
        if(piList.size()==0){
            throw new BusException("????????????????????????????????????,??????????????????????????????");
        }
        if(piList.size()>1){
            throw new BusException("????????????????????????????????????,???????????????????????????");
        }
        User user = UserContext.getUser();
        StringBuilder tradeCode = new StringBuilder();
        Map<String, Object> piMap = piList.get(0);
        Map<String, Object> piAccMap = DataBaseHelper.queryForMap("select pk_piacc from pi_acc where pk_pi= ?", MapUtils.getString(piMap,"pkPi"));
        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????~~
        for (Map<String, Object> chargeMap : list) {
            Map<String,Object> invQryMap = Maps.newHashMap();
            invQryMap.put("euType", EnumerateParameter.FIVE);
            ResponseJson result = ApplicationUtils.execService("RMA","InvService","searchCanUsedEmpInvoices", invQryMap, user);
            if(result.getStatus()!= Constant.SUC){
                throw new BusException(StringUtils.trimToEmpty(result.getErrorMessage())+" "+ StringUtils.trimToEmpty(result.getDesc()));
            }
            Map<String, Object> invData = (Map<String, Object>)result.getData();

            String pos = MapUtils.getString(chargeMap, "POSTransNO");
            String mgStr[] = pos.split("\\|");
            BlDeposit vo = new BlDeposit();
            vo.setDtPaymode(LbzySelfTools.getPayCode(1,MapUtils.getString(chargeMap,"PayTypeID")));
            vo.setEuDptype(EnumerateParameter.NINE);
            vo.setEuDirect(EnumerateParameter.ONE);
            vo.setPkPi(MapUtils.getString(piMap,"pkPi"));
            vo.setPkPv(MapUtils.getString(piMap,"pkPv"));
            vo.setAmount(BigDecimal.valueOf(MapUtils.getDouble(chargeMap, "Amount")));
            vo.setPayInfo(pos);
            vo.setNote(pos);
            vo.setDatePay(new Date());
            vo.setPkEmpPay(user.getPkEmp());
            vo.setNameEmpPay("?????????:"+MapUtils.getString(paramMap,"UserID"));
            vo.setFlagSettle("0");
            vo.setFlagAcc("0");
            vo.setEuPvtype(MapUtils.getString(piMap,"euPvtype"));
            vo.setPkDept(user.getPkDept());
            vo.setPkAcc(MapUtils.getString(piAccMap,"pkPiacc"));
            vo.setPkEmpinvoice(LbSelfUtil.getPropValueStr(invData,"pkEmpinv"));
            vo.setReptNo(LbSelfUtil.getPropValueStr(invData,"curCodeInv"));
            ApplicationUtils.setDefaultValue(vo, true);
            result = ApplicationUtils.execService("BL", "BlPrePayService", "saveDeposit", vo, user);
            if(result.getStatus()!= Constant.SUC){
                throw new BusException(StringUtils.trimToEmpty(result.getErrorMessage())+" "+ StringUtils.trimToEmpty(result.getDesc()));
            }
            BlDeposit resultDepo = (BlDeposit) result.getData();
            addBlExtPay(resultDepo);
            tradeCode.append(resultDepo.getCodeDepo()).append("|");
        }
        tradeCode.deleteCharAt(tradeCode.length()-1);
        Map<String,Object> mapAmount = DataBaseHelper.queryForMap("select sum(AMOUNT) balance from BL_DEPOSIT where PK_PV=? and FLAG_SETTLE='0' and EU_PVTYPE='3' and eu_dptype='9'",
                new Object[]{MapUtils.getString(piMap,"pkPv")});
        Map<String,Object> resultMap = Maps.newHashMap();
        resultMap.put("ResultCode", "0");
        resultMap.put("TranSerialNO", tradeCode.toString());
        resultMap.put("InBalance", MapUtils.getDoubleValue(mapAmount, "balance",0D));
        return resultMap;
    }

    public void addBlExtPay(BlDeposit blDeposit){
        BlExtPay extPay = new BlExtPay();
        ApplicationUtils.setDefaultValue(extPay,true);
        extPay.setAmount(blDeposit.getAmount());
        extPay.setNameBank(blDeposit.getDtBank());
        extPay.setEuPaytype(blDeposit.getDtPaymode());
        extPay.setFlagPay("1");
        extPay.setSerialNo(blDeposit.getPayInfo());//?????????
        extPay.setTradeNo(blDeposit.getPayInfo());//???????????????
        extPay.setPkPi(blDeposit.getPkPi());
        extPay.setPkPv(blDeposit.getPkPv());
        extPay.setDateRefund(null);
        extPay.setEuBill("0");
        extPay.setDateBill(null);
        extPay.setDescPay("?????????");
        extPay.setDatePay(new Date());
        extPay.setDateAp(new Date());
        extPay.setPkSettle(blDeposit.getPkSettle());
        extPay.setPkDepo(blDeposit.getPkDepo());
        DataBaseHelper.insertBean(extPay);
    }

    public List<Map<String,Object>> getListNodes(Map<String,Object> paramMap){
        Map<String,Object> itemMap = (Map<String,Object>)MapUtils.getObject(paramMap, "List");
        Object object = MapUtils.getObject(itemMap, "Item");
        return (object instanceof List)?(List<Map<String,Object>>)object: Lists.newArrayList((Map<String,Object>)object);
    }

    /**
     * ??????-?????????
     * @param paramMap
     * @return
     */
    public RegApptVo doReg(Map<String,Object> paramMap) {
        List<Map<String,Object>> payList = getListNodes(paramMap);
        if(CollectionUtils.isEmpty(payList)){
            throw new BusException("????????????????????????");
        }
        PiMasterRegVo regVo = DataBaseHelper.queryForBean("select ps.pk_hp,pi.PK_PI,pi.NAME_PI,pi.DT_SEX,pi.BIRTH_DATE,pi.PK_PICATE,pi.NAME_REL,pi.IDNO_REL,pi.TEL_REL" +
                " from PI_MASTER pi left join pi_insurance ps on pi.pk_pi=ps.pk_pi and nvl(ps.flag_def,'1')='1' left join PI_CATE ct on pi.PK_PICATE=ct.PK_PICATE where pi.CODE_PI=?"
                ,PiMasterRegVo.class, new Object[]{MapUtils.getString(paramMap, "PatientID")});
        if(regVo == null) {
            throw new BusException("????????????????????????");
        }

        paramMap.put("now", DateUtils.getDate("yyyy-MM-dd"));
        PiMasterRegVo schMaster = selfServiceMapper.qryRegSch(paramMap);
        if(schMaster == null){
            throw new BusException("????????????????????????????????????????????????????????????");
        }
        BeanUtils.copyPropertiesIgnoreNull(schMaster, regVo);
        constructItemList(regVo);
        validFee(payList, regVo);
        constructDeposit(payList, regVo);

        regVo.setDateReg(new Date());
        regVo.setInvStatus("-2");
        ResponseJson  responseJson =  ApplicationUtils.execService("WPV", "RegSyxHandler", "savePvRegInfo", regVo, UserContext.getUser());
        if(responseJson.getStatus() != Constant.SUC){
            throw new BusException(StringUtils.trimToEmpty(responseJson.getErrorMessage())+" "+ StringUtils.trimToEmpty(responseJson.getDesc()));
        }
        regVo = (PiMasterRegVo) responseJson.getData();
        regVo.getDepositList().forEach(this::addBlExtPay);
        modPvInsu(regVo.getPkPv(), regVo.getPkPi());
        return selfServiceMapper.qryRegInfo(regVo.getPkPv());
    }

    private void modPvInsu(String pkPv,String pkPi) {
        ResponseJson responseJson;//????????????????????????????????????????????????????????????????????????????????????
        Integer exist = DataBaseHelper.queryForScalar("select count(1) from pv_encounter where pk_pv=? and pk_insu is null", Integer.class, new Object[]{pkPv});
        if(exist>0){
            Map<String,Object> mapParam = Maps.newHashMap();
            mapParam.put("pkPi", pkPi);
            responseJson =  ApplicationUtils.execService("WPV", "RegSyxHandler", "getPiInsurance", mapParam, UserContext.getUser());
            if(responseJson.getStatus() != Constant.SUC){
                throw new BusException(StringUtils.trimToEmpty(responseJson.getErrorMessage())+" "+ StringUtils.trimToEmpty(responseJson.getDesc()));
            }
            PiInsurance piInsurance = (PiInsurance) responseJson.getData();
            DataBaseHelper.update("update pv_encounter set pk_insu=? where pk_pv=?",piInsurance.getPkHp(),pkPv);
        }
    }

    public RegApptVo confirmApptReg(Map<String,Object> paramMap){
        List<Map<String,Object>> payList = getListNodes(paramMap);
        if(CollectionUtils.isEmpty(payList)){
            throw new BusException("????????????????????????");
        }
        PiMasterRegVo regVo = selfServiceMapper.qryConfirmReg(paramMap);
        if(regVo == null){
            throw new BusException("??????????????????????????????");
        }
        constructItemList(regVo);
        validFee(payList, regVo);
        constructDeposit(payList, regVo);

        ResponseJson result = ApplicationUtils.execService("WPV", "RegSyxService", "confirmApptRegInfo", regVo, UserContext.getUser());
        if(result.getStatus()!= Constant.SUC){
            throw new BusException(StringUtils.trimToEmpty(result.getErrorMessage())+" "+ StringUtils.trimToEmpty(result.getDesc()));
        }
        regVo = (PiMasterRegVo)result.getData();
        regVo.getDepositList().forEach(this::addBlExtPay);
        RegApptVo regApptVo = selfServiceMapper.qryApptInfo(regVo.getPkAppt(), regVo.getPkPi());
        modPvInsu(regVo.getPkPv(), regVo.getPkPi());
        regApptVo.setResultCode("0");
        return regApptVo;
    }

    public void cancelAppt(SchAppt schAppt){
        //???????????????????????????
        User u  = UserContext.getUser();
        if(u!=null){
            schAppt.setPkEmpCancel(u.getPkEmp());
            schAppt.setNameEmpCancel(u.getNameEmp());
        }
        schAppt.setNote("???????????????");
        schAppt.setFlagCancel("1");
        schAppt.setEuStatus("9");
        schAppt.setDateCancel(new Date());
        DataBaseHelper.updateBeanByPk(schAppt, false);
        DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = ?", new Object[] { schAppt.getPkSch() });
        DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = ?",
                new Object[] { schAppt.getPkSch() });
        DataBaseHelper.update("update sch_ticket set flag_used = '0' where pk_sch = ? and ticketno = ?",
                new Object[] { schAppt.getPkSch(), schAppt.getTicketNo() });
    }

    private void constructItemList(PiMasterRegVo regVo) {
        Map<String, Object> paramFee = Maps.newHashMap();
        paramFee.put("pkSchsrv", regVo.getPkSchsrv());
        paramFee.put("pkPres", regVo.getPkSchres());
        regVo.setItemList(selfServiceMapper.qryCofirmRegFee(paramFee));
        if (CollectionUtils.isEmpty(regVo.getItemList())) {
            throw new BusException("??????????????????????????????");
        }
    }

    private void constructDeposit(List<Map<String, Object>> payList, PiMasterRegVo regVo) {
        List<BlDeposit> depositList = payList.stream().map(vo ->{
            BlDeposit deposit = new BlDeposit();
            deposit.setAmount(BigDecimal.valueOf(MapUtils.getDoubleValue(vo, "Amount",0D)));
            deposit.setDtPaymode(LbzySelfTools.getPayCode(1,MapUtils.getString(vo,"PayTypeID")));
            deposit.setPaymodeName(MapUtils.getString(vo,"PayModeName"));
            deposit.setPayInfo(MapUtils.getString(vo,"POSTransNO"));
            deposit.setDtBank(MapUtils.getString(vo,"OpenBank"));
            deposit.setTradeNo(MapUtils.getString(vo,"POSTransNO"));
            return deposit;
        }).collect(Collectors.toList());
        regVo.setDepositList(depositList);
    }

    private void validFee(List<Map<String, Object>> payList, PiMasterRegVo regVo) {
        double totalIn = payList.stream().mapToDouble(vo-> MapUtils.getDoubleValue(vo,"Amount",0D)).sum();
        double totalPre = regVo.getItemList().stream().mapToDouble(vo -> MathUtils.mul(vo.getPrice(),vo.getQuan())).sum();
        if(!MathUtils.equ(MathUtils.upRound(totalIn,2), MathUtils.upRound(totalPre,2))){
            throw new BusException("????????????????????????????????????????????????");
        }
    }

}
