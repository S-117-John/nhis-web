package com.zebone.nhis.ma.pub.platform.zsrm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.TransfTool;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphConstant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchDoctorVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch.SchDoctorOutcome;
import com.zebone.nhis.ma.pub.platform.zsrm.dao.ZsrmCnMapper;
import com.zebone.nhis.ma.pub.platform.zsrm.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.zsrm.service.ZsrmBlService;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.util.*;

@Service
public class ZsrmBlServiceImpl implements ZsrmBlService {


    @Resource
    private ZsrmCnMapper zsrmCnMapper;
    /**
     * ????????????????????????
     * @param param
     * @return
     */
    @Override
    public Response saveSupplementPrice(String param) {
        //???????????????
        JSONObject jsonObject = JSONObject.parseObject(param);
        String entryParam = jsonObject.getString("entry");
        List<BlEntry> blEntryList = JsonUtil.readValue(entryParam, new TypeReference<List<BlEntry>>() {});
        for(BlEntry blEntry:blEntryList){
            if(StringUtils.isNotEmpty(blEntry.getResource().getEpisodeID())){
                OperationFee requestBody=blEntry.getResource();
                List<BlPubParamVo> voList = new ArrayList<BlPubParamVo>();
                User user = new User();
                String pkOrg=null,pkPi=null,pkPv=null,euPvType=null,pkOrdexdt=null;
                Map<String, Object> pvMap = DataBaseHelper.queryForMap("select pk_pi,pk_pv,eu_pvtype,pk_org from pv_encounter where code_pv = ?",requestBody.getEpisodeID());
                if(MapUtils.isNotEmpty(pvMap)){
                    pkOrg = MapUtils.getString(pvMap, "pkOrg");
                    pkPi = MapUtils.getString(pvMap, "pkPi");
                    pkPv = MapUtils.getString(pvMap, "pkPv");
                    euPvType = MapUtils.getString(pvMap, "euPvtype");
                }else{
                    throw new BusException("??????????????????????????????");
                }


                //????????????????????????
                for(OrderDetail OrderDetail:requestBody.getOrderDetailList()){
                    //?????????????????????????????????
                    BlPubParamVo vo = new BlPubParamVo();
                    //??????????????????????????????????????????????????????????????????
                    if(StringUtils.isNotEmpty(OrderDetail.getOrderDetailID())){
                        pkOrdexdt=OrderDetail.getOrderDetailID();
                        Map<String, Object> bdItemMaps = DataBaseHelper.queryForMap("select amount,pk_dept_ex,pk_cgip from bl_ip_dt where pk_ordexdt=? and pk_pv=?",new Object[]{pkOrdexdt,pkPv});
                        if(MapUtils.isNotEmpty(bdItemMaps)){
                            //??????????????????
                            continue;
                        }
                    }else{
                        throw new BusException("????????????????????????");
                    }

                    if(StringUtils.isEmpty(OrderDetail.getOrdItemCode())){
                        throw new BusException("????????????????????????");
                    }
                    //??????????????????????????????????????????????????????
                    BigDecimal price = BigDecimal.ZERO;
                    //????????????????????????
                    Map<String, Object> bdItemMap =DataBaseHelper.queryForMap("select pk_item from bd_item where flag_active='1' and code = ?",OrderDetail.getOrdItemCode());
                    //??????????????????
                    //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    Map<String, Object> bdPdMap = DataBaseHelper.queryForMap("select pk_unit_min as pk_unit,pk_pd,price,pack_size from bd_pd where flag_stop!='1' and code = ?", OrderDetail.getOrdItemCode());
                    if(MapUtils.isNotEmpty(bdPdMap)){
                        String bdPdprice = LbSelfUtil.getPropValueStr(bdPdMap,"price");//????????????
                        String packSize = LbSelfUtil.getPropValueStr(bdPdMap,"packSize");//?????????
                        BigDecimal bdPdPackSize = BigDecimal.ZERO;
                        if(StringUtils.isNotEmpty(bdPdprice)){
                            price=BigDecimal.valueOf(Double.valueOf(bdPdprice));
                        }
                        if(StringUtils.isNotEmpty(packSize)){
                            bdPdPackSize=BigDecimal.valueOf(Double.valueOf(packSize));
                        }
                        //????????????????????????????????????
                        price=price.divide(bdPdPackSize);

                        //??????????????????
                        vo.setPkOrd(LbSelfUtil.getPropValueStr(bdPdMap,"pkPd"));
                        //???????????????????????????????????????
                        vo.setBatchNo("~");//?????????
                        vo.setPriceCost(price.doubleValue());//??????????????????
                        vo.setPkUnitPd(MapUtils.getString(bdPdMap,"pkUnit"));//????????????
                        vo.setPackSize(Integer.valueOf("1"));//???????????????
                        vo.setFlagPd(EnumerateParameter.ONE);//???????????? 1:?????????0?????????
                        vo.setFlagHasPdPrice("1");
                        vo.setPrice(price.doubleValue());//????????????
                    }else if(MapUtils.isNotEmpty(bdItemMap)){
                        //??????????????????
                        vo.setPkItem(MapUtils.getString(bdItemMap, "pkItem"));
                        vo.setFlagPd(EnumerateParameter.ZERO);//???????????? 1:?????????0?????????
                    }else{
                        throw new BusException("?????????"+OrderDetail.getOrdItemCode()+"??????????????????,???????????????????????????");
                    }

                    //?????????????????????????????????---?????????????????????????????????
                    String pkDeptApp = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ? and pk_org =?",OrderDetail.getOrdLocCode(), pkOrg), "pkDept");
                    if(StringUtils.isEmpty(pkDeptApp)){
                        throw new BusException("????????????"+OrderDetail.getOrdLocCode()+"????????????????????????");
                    }
                    String pkDeptEx = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ? and pk_org =?",OrderDetail.getExcLocCode(), pkOrg), "pkDept");
                    if(StringUtils.isEmpty(pkDeptEx)){
                        throw new BusException("????????????"+OrderDetail.getExcLocCode()+"????????????????????????");
                    }
                    String pkDeptCg = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ? and pk_org =?",OrderDetail.getFeeLocCode(), pkOrg), "pkDept");
                    if(StringUtils.isEmpty(pkDeptCg)){
                        throw new BusException("????????????"+OrderDetail.getFeeLocCode()+"????????????????????????");
                    }
                    //????????????????????????
                    Map<String, Object> EmpAppMap = DataBaseHelper.queryForMap("select pk_emp,name_emp from bd_ou_employee where code_emp = ?",OrderDetail.getOrdAuthCode());
                    if(MapUtils.isEmpty(EmpAppMap)){
                        throw new BusException("????????????"+OrderDetail.getOrdAuthCode()+"????????????????????????");
                    }
                    //?????????
                    Map<String, Object> empMap = DataBaseHelper.queryForMap("select pk_emp,name_emp from bd_ou_employee where code_emp = ?",OrderDetail.getOrdVeriCode());
                    if(MapUtils.isEmpty(empMap)){
                        throw new BusException("????????????"+OrderDetail.getOrdVeriCode()+"??????????????????");
                    }

                    vo.setEuBltype("9");//???????????????9???????????????
                    vo.setPkOrdexdt(pkOrdexdt);//?????????????????????????????????????????????????????????????????????????????????
                    vo.setPkOrg(pkOrg);//????????????
                    vo.setEuPvType(euPvType);//????????????
                    vo.setPkPv(pkPv);
                    vo.setPkPi(pkPi);
                    vo.setPkOrgEx(pkOrg);////????????????
                    vo.setPkOrgApp(pkOrg);//??????????????????
                    //vo.setPkCnord(pkCnord);//??????????????????
                    vo.setFlagPv(EnumerateParameter.ZERO);//?????????????????? false ??????????????????????????????1 ????????????0
                    vo.setDateHap(OrderDetail.getOrdDate());//??????????????????-????????????
                    vo.setQuanCg(Double.parseDouble(OrderDetail.getAmount()));//????????????
                    //vo.setBarcode(barCode);//????????????????????????
                    vo.setPkDeptApp(pkDeptApp);//??????????????????
                    vo.setPkDeptEx(pkDeptEx);//????????????
                    vo.setPkDeptCg(pkDeptCg);//????????????
                    vo.setPkEmpApp(MapUtils.getString(EmpAppMap, "pkEmp"));//??????????????????
                    vo.setNameEmpApp(MapUtils.getString(EmpAppMap, "nameEmp"));//??????????????????
                    vo.setPkEmpCg(MapUtils.getString(empMap, "pkEmp"));//??????????????????
                    vo.setNameEmpCg(MapUtils.getString(empMap, "nameEmp"));//??????????????????
                    voList.add(vo);
                }

                user.setPkOrg(pkOrg);
                UserContext.setUser(user);
                ApplicationUtils aUtils = new ApplicationUtils();
                //????????????????????????
                String serviceName;
                //????????????????????????
                String methodName;
                //???????????????????????????????????????????????????????????????????????????????????????
                if(EnumerateParameter.THREE.equals(euPvType)){
                    serviceName="IpCgPubService";
                    methodName ="savePatiCgInfo";
                }else{
                    serviceName="OtherAccountService";
                    methodName ="saveOpPatiCgInfo";
                }

                ResponseJson ret = aUtils.execService("bl", serviceName, methodName,voList, user);
                if(0 != ret.getStatus()){
                    throw new BusException(ret.getDesc());
                }
            }
        }
        //??????????????????
        Entry entry = new Entry(new Response());
        Response response = entry.getResponse();
        Outcome outcome = new Outcome();
        outcome.setResourceType("OperationOutcome");
        response.setStaus(ZsphConstant.RES_ERR_OTHER);
        Issue issue = new Issue();
        issue.setCode("informational");
        issue.setDiagnostics("??????");
        issue.setSeverity("informational");
        outcome.setIssue(Arrays.asList(issue));
        response.setOutcome(BeanMap.create(outcome));
        return response;
    }


    /**
     * ??????????????????
     * @param param
     * @return
     */
    @Override
    public List<Entry> getEBillByDate(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        //List<Parameter> parameterList = ZsphMsgUtils.createParameterList(requestBody);
        List<Parameter> parameterList =requestBody.getEntry()
                .stream().filter(et ->{return et!=null && et.getResource()!=null && "ServiceRequest".equals(et.getResource().getResourceType());})
                .findFirst().get().getResource().getParameter();
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("?????????parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"patientId"))){
            throw new BusException("??????Id???patientid????????????");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"billStartDate"))){
            throw new BusException("???????????????billStartDate????????????");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"billEndDate"))){
            throw new BusException("???????????????billEndDate????????????");
        }
        Map<String,Object> pMap = new HashMap();
        pMap.put("codeOp",MapUtils.getString(paramMap,"patientId"));
        pMap.put("dateStart",MapUtils.getString(paramMap,"billStartDate"));
        pMap.put("dateEnd",MapUtils.getString(paramMap,"billEndDate"));
        List<Map<String,Object>> billList = zsrmCnMapper.getEBillByDate(pMap);
        if(CollectionUtils.isEmpty(billList)){
            throw new BusException("????????????");
        }
        List<Entry> entryList = Lists.newArrayList();
        Entry entry = new Entry();
        Response response = new Response();
        entry.setResponse(response);
        response.setStaus(ZsphConstant.RES_SUS_OTHER);
        InvoiceOutcome outcome = new InvoiceOutcome();
        outcome.setResourceType("ServiceRequest");
        outcome.setId(NHISUUID.getKeyId());
        outcome.setImplicitRules("DZPJLBCX");
        List<DataTable> dataTableList = Lists.newArrayList();
        for (Map<String,Object> map : billList) {
            DataTable dataTable = new DataTable();
            dataTable.setPatientId(MapUtils.getString(map,"codeOp",""));
            dataTable.setPatientName(MapUtils.getString(map,"namePi",""));
            dataTable.setBillBatchCode(MapUtils.getString(map,"ebillbatchcode",""));
            dataTable.setBusNo(MapUtils.getString(map,"ebillno",""));
            dataTable.setRandom(MapUtils.getString(map,"checkcode",""));
            dataTable.setBillNo(MapUtils.getString(map,"ebillno",""));
            dataTable.setChargeDate(MapUtils.getString(map,"dateInv",""));
            dataTable.setChargeTotal(MapUtils.getString(map,"amountInv",""));
            dataTable.setPrintTimes(MapUtils.getString(map,"printTimes",""));
            dataTable.setBillUrl(MapUtils.getString(map,"urlNetebill",""));
            dataTableList.add(dataTable);
        }
        outcome.setDataTable(dataTableList);
        response.setOutcome(BeanMap.create(outcome));
        entryList.add(entry);
        return entryList;

    }

    /**
     * ??????????????????????????????
     * @param param
     * @return
     */
    @Override
    public void updateEBillPrintTimes(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList =requestBody.getEntry()
                .stream().filter(et ->{return et!=null && et.getResource()!=null && "ServiceRequest".equals(et.getResource().getResourceType());})
                .findFirst().get().getResource().getParameter();
        Destination destination= requestBody.getEntry()
                .stream().filter(et ->{return et!=null && et.getResource()!=null && "MessageHeader".equals(et.getResource().getResourceType());})
                .findFirst().get().getResource().getSource();
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("?????????parameter");
        }
        if(destination == null){
            throw new BusException("?????????????????????");

        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"busNo"))){
            throw new BusException("?????????????????????busNo????????????");
        }
        int count = DataBaseHelper.update("update BL_INVOICE set print_times = print_times+1,note=note+'"+destination.getEndpoint()+"''|' where ebillno = '"+MapUtils.getString(paramMap,"busNo")+"'");
        if(count<1){
            throw new BusException("????????????????????????");
        }

    }

    /**
     * 3.43.??????????????????????????????
     * @param param
     * @return
     */
    @Override
    public List<Entry> queryOutpfeeMasterInfo(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList =requestBody.getEntry()
                .stream().filter(et ->{return et!=null && et.getResource()!=null && "Parameters".equals(et.getResource().getResourceType());})
                .findFirst().get().getResource().getParameter();
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("?????????parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"patientId"))){
            throw new BusException("??????Id???patientid????????????");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"inDate"))){
            throw new BusException("???????????????inDate????????????");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"outDate"))){
            throw new BusException("???????????????outDate????????????");
        }
        List<Outfee> outfeeList = zsrmCnMapper.queryOutpfeeMasterInfo(paramMap);
        if(CollectionUtils.isEmpty(outfeeList)){
            throw new BusException("????????????");
        }
        List<Entry> entryList = Lists.newArrayList();
        Entry entry = new Entry();
        Response response = new Response();
        entry.setResponse(response);
        response.setStaus(ZsphConstant.RES_SUS_OTHER);
        FeeOutcome outcome = new FeeOutcome();
        outcome.setResourceType("OperationOutcome");
        //outcome.setId(NHISUUID.getKeyId());
        //outcome.setImplicitRules("DZPJLBCX");
        //List<Outfee> outfeeList = Lists.newArrayList();
        outcome.setOutfee(outfeeList);
        response.setOutcome(BeanMap.create(outcome));
        entryList.add(entry);
        return entryList;

    }

    /**
     * 3.44.??????????????????????????????
     * @param param
     * @return
     */
    @Override
    public List<Entry> queryoutpfeedetailinfo(String param) {
        RequestBody requestBody = ZsphMsgUtils.fromJson(param, RequestBody.class);
        List<Parameter> parameterList =requestBody.getEntry()
                .stream().filter(et ->{return et!=null && et.getResource()!=null && "Parameters".equals(et.getResource().getResourceType());})
                .findFirst().get().getResource().getParameter();
        if(CollectionUtils.isEmpty(parameterList)){
            throw new BusException("?????????parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"patientId"))){
            throw new BusException("??????Id???patientId????????????");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"codeSt"))){
            throw new BusException("????????????codeSt????????????");
        }

        List<OutFeedeTail> outfeeList = zsrmCnMapper.queryoutpfeedetailinfo(paramMap);
        if(CollectionUtils.isEmpty(outfeeList)){
            throw new BusException("????????????");
        }
        List<Entry> entryList = Lists.newArrayList();
        Entry entry = new Entry();
        Response response = new Response();
        entry.setResponse(response);
        response.setStaus(ZsphConstant.RES_SUS_OTHER);
        FeeOutcome outcome = new FeeOutcome();
        outcome.setResourceType("OperationOutcome");
        //outcome.setId(NHISUUID.getKeyId());
        //outcome.setImplicitRules("DZPJLBCX");
        //List<Outfee> outfeeList = Lists.newArrayList();
        outcome.setOutFeedeTail(outfeeList);
        response.setOutcome(BeanMap.create(outcome));
        entryList.add(entry);
        return entryList;

    }
}
