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
     * 收费费用补录接口
     * @param param
     * @return
     */
    @Override
    public Response saveSupplementPrice(String param) {
        //解析请参数
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
                    throw new BusException("未获取到有效患者信息");
                }


                //遍历收费项目信息
                for(OrderDetail OrderDetail:requestBody.getOrderDetailList()){
                    //创建记费实体并填充数据
                    BlPubParamVo vo = new BlPubParamVo();
                    //根据记费信息唯一主键判断记费信息是否重复记录
                    if(StringUtils.isNotEmpty(OrderDetail.getOrderDetailID())){
                        pkOrdexdt=OrderDetail.getOrderDetailID();
                        Map<String, Object> bdItemMaps = DataBaseHelper.queryForMap("select amount,pk_dept_ex,pk_cgip from bl_ip_dt where pk_ordexdt=? and pk_pv=?",new Object[]{pkOrdexdt,pkPv});
                        if(MapUtils.isNotEmpty(bdItemMaps)){
                            //跳出本次循环
                            continue;
                        }
                    }else{
                        throw new BusException("未获取到记费主键");
                    }

                    if(StringUtils.isEmpty(OrderDetail.getOrdItemCode())){
                        throw new BusException("收费项目编码为空");
                    }
                    //创建药品零售价，当记费是药品时使用。
                    BigDecimal price = BigDecimal.ZERO;
                    //收费项目信息查询
                    Map<String, Object> bdItemMap =DataBaseHelper.queryForMap("select pk_item from bd_item where flag_active='1' and code = ?",OrderDetail.getOrdItemCode());
                    //药品信息查询
                    //药品记费是需要药品单价，根据药品编码查询药品字典信息并及时药品单价。未与库存相关联。
                    Map<String, Object> bdPdMap = DataBaseHelper.queryForMap("select pk_unit_min as pk_unit,pk_pd,price,pack_size from bd_pd where flag_stop!='1' and code = ?", OrderDetail.getOrdItemCode());
                    if(MapUtils.isNotEmpty(bdPdMap)){
                        String bdPdprice = LbSelfUtil.getPropValueStr(bdPdMap,"price");//零售价格
                        String packSize = LbSelfUtil.getPropValueStr(bdPdMap,"packSize");//包装量
                        BigDecimal bdPdPackSize = BigDecimal.ZERO;
                        if(StringUtils.isNotEmpty(bdPdprice)){
                            price=BigDecimal.valueOf(Double.valueOf(bdPdprice));
                        }
                        if(StringUtils.isNotEmpty(packSize)){
                            bdPdPackSize=BigDecimal.valueOf(Double.valueOf(packSize));
                        }
                        //药品零售价除以单位包装量
                        price=price.divide(bdPdPackSize);

                        //医嘱项目主键
                        vo.setPkOrd(LbSelfUtil.getPropValueStr(bdPdMap,"pkPd"));
                        //收费项目为药品时设置批次号
                        vo.setBatchNo("~");//批次号
                        vo.setPriceCost(price.doubleValue());//药品成本单价
                        vo.setPkUnitPd(MapUtils.getString(bdPdMap,"pkUnit"));//零售单位
                        vo.setPackSize(Integer.valueOf("1"));//药品包装量
                        vo.setFlagPd(EnumerateParameter.ONE);//物品标志 1:药品，0：物品
                        vo.setFlagHasPdPrice("1");
                        vo.setPrice(price.doubleValue());//零售单价
                    }else if(MapUtils.isNotEmpty(bdItemMap)){
                        //收费项目主键
                        vo.setPkItem(MapUtils.getString(bdItemMap, "pkItem"));
                        vo.setFlagPd(EnumerateParameter.ZERO);//物品标志 1:药品，0：物品
                    }else{
                        throw new BusException("未查询"+OrderDetail.getOrdItemCode()+"收费项目信息,或改收费项目已停用");
                    }

                    //查询执行科室、开立科室---暂使用一个、后期待确定
                    String pkDeptApp = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ? and pk_org =?",OrderDetail.getOrdLocCode(), pkOrg), "pkDept");
                    if(StringUtils.isEmpty(pkDeptApp)){
                        throw new BusException("未查询到"+OrderDetail.getOrdLocCode()+"相关开立科室信息");
                    }
                    String pkDeptEx = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ? and pk_org =?",OrderDetail.getExcLocCode(), pkOrg), "pkDept");
                    if(StringUtils.isEmpty(pkDeptEx)){
                        throw new BusException("未查询到"+OrderDetail.getExcLocCode()+"相关执行科室信息");
                    }
                    String pkDeptCg = MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ? and pk_org =?",OrderDetail.getFeeLocCode(), pkOrg), "pkDept");
                    if(StringUtils.isEmpty(pkDeptCg)){
                        throw new BusException("未查询到"+OrderDetail.getFeeLocCode()+"相关记费科室信息");
                    }
                    //查询开立医生信息
                    Map<String, Object> EmpAppMap = DataBaseHelper.queryForMap("select pk_emp,name_emp from bd_ou_employee where code_emp = ?",OrderDetail.getOrdAuthCode());
                    if(MapUtils.isEmpty(EmpAppMap)){
                        throw new BusException("未查询到"+OrderDetail.getOrdAuthCode()+"相关开立医生信息");
                    }
                    //记费人
                    Map<String, Object> empMap = DataBaseHelper.queryForMap("select pk_emp,name_emp from bd_ou_employee where code_emp = ?",OrderDetail.getOrdVeriCode());
                    if(MapUtils.isEmpty(empMap)){
                        throw new BusException("未查询到"+OrderDetail.getOrdVeriCode()+"相关人员信息");
                    }

                    vo.setEuBltype("9");//记费类型：9：其他记账
                    vo.setPkOrdexdt(pkOrdexdt);//记费主键。用于效验三方费用记费唯一，避免重复记费问题。
                    vo.setPkOrg(pkOrg);//机构主键
                    vo.setEuPvType(euPvType);//就诊类型
                    vo.setPkPv(pkPv);
                    vo.setPkPi(pkPi);
                    vo.setPkOrgEx(pkOrg);////执行机构
                    vo.setPkOrgApp(pkOrg);//开立机构主键
                    //vo.setPkCnord(pkCnord);//医嘱项目编码
                    vo.setFlagPv(EnumerateParameter.ZERO);//挂号费用标志 false 如果是挂号费用必须为1 否则全为0
                    vo.setDateHap(OrderDetail.getOrdDate());//服务发生时间-开立时间
                    vo.setQuanCg(Double.parseDouble(OrderDetail.getAmount()));//记费数量
                    //vo.setBarcode(barCode);//高值耗材材料编码
                    vo.setPkDeptApp(pkDeptApp);//开立科室主键
                    vo.setPkDeptEx(pkDeptEx);//执行科室
                    vo.setPkDeptCg(pkDeptCg);//记费科室
                    vo.setPkEmpApp(MapUtils.getString(EmpAppMap, "pkEmp"));//开立医生主键
                    vo.setNameEmpApp(MapUtils.getString(EmpAppMap, "nameEmp"));//开立医生名称
                    vo.setPkEmpCg(MapUtils.getString(empMap, "pkEmp"));//记费人员主键
                    vo.setNameEmpCg(MapUtils.getString(empMap, "nameEmp"));//记费人员姓名
                    voList.add(vo);
                }

                user.setPkOrg(pkOrg);
                UserContext.setUser(user);
                ApplicationUtils aUtils = new ApplicationUtils();
                //其他记账服务名称
                String serviceName;
                //其他记账接口名称
                String methodName;
                //根据就诊类型判断是否调用住院其他记账接口、门诊其他记账接口
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
        //创建响应数据
        Entry entry = new Entry(new Response());
        Response response = entry.getResponse();
        Outcome outcome = new Outcome();
        outcome.setResourceType("OperationOutcome");
        response.setStaus(ZsphConstant.RES_ERR_OTHER);
        Issue issue = new Issue();
        issue.setCode("informational");
        issue.setDiagnostics("成功");
        issue.setSeverity("informational");
        outcome.setIssue(Arrays.asList(issue));
        response.setOutcome(BeanMap.create(outcome));
        return response;
    }


    /**
     * 电子发票查询
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
            throw new BusException("未传入parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"patientId"))){
            throw new BusException("患者Id：patientid不能为空");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"billStartDate"))){
            throw new BusException("开始日期：billStartDate不能为空");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"billEndDate"))){
            throw new BusException("结束日期：billEndDate不能为空");
        }
        Map<String,Object> pMap = new HashMap();
        pMap.put("codeOp",MapUtils.getString(paramMap,"patientId"));
        pMap.put("dateStart",MapUtils.getString(paramMap,"billStartDate"));
        pMap.put("dateEnd",MapUtils.getString(paramMap,"billEndDate"));
        List<Map<String,Object>> billList = zsrmCnMapper.getEBillByDate(pMap);
        if(CollectionUtils.isEmpty(billList)){
            throw new BusException("没有数据");
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
     * 更新电子发票打印次数
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
            throw new BusException("未传入parameter");
        }
        if(destination == null){
            throw new BusException("发送方不能为空");

        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"busNo"))){
            throw new BusException("电子发票号码：busNo不能为空");
        }
        int count = DataBaseHelper.update("update BL_INVOICE set print_times = print_times+1,note=note+'"+destination.getEndpoint()+"''|' where ebillno = '"+MapUtils.getString(paramMap,"busNo")+"'");
        if(count<1){
            throw new BusException("未获取到发票信息");
        }

    }

    /**
     * 3.43.获取门诊费用主表服务
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
            throw new BusException("未传入parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"patientId"))){
            throw new BusException("患者Id：patientid不能为空");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"inDate"))){
            throw new BusException("开始日期：inDate不能为空");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"outDate"))){
            throw new BusException("结束日期：outDate不能为空");
        }
        List<Outfee> outfeeList = zsrmCnMapper.queryOutpfeeMasterInfo(paramMap);
        if(CollectionUtils.isEmpty(outfeeList)){
            throw new BusException("没有数据");
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
     * 3.44.获取门诊费用明细服务
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
            throw new BusException("未传入parameter");
        }
        Map<String, Object> paramMap = ZsphMsgUtils.parameterListToMap(parameterList);
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"patientId"))){
            throw new BusException("患者Id：patientId不能为空");
        }
        if(StringUtils.isBlank(MapUtils.getString(paramMap,"codeSt"))){
            throw new BusException("结算号：codeSt不能为空");
        }

        List<OutFeedeTail> outfeeList = zsrmCnMapper.queryoutpfeedetailinfo(paramMap);
        if(CollectionUtils.isEmpty(outfeeList)){
            throw new BusException("没有数据");
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
