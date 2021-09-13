package com.zebone.nhis.compay.ins.qgyb.service;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.compay.ins.qgyb.anno.FileMapping;
import com.zebone.nhis.compay.ins.qgyb.dao.NationalInsuBusMapper;
import com.zebone.nhis.compay.ins.qgyb.vo.CancerFighting;
import com.zebone.nhis.compay.ins.qgyb.vo.InsQgybItem;
import com.zebone.nhis.compay.ins.qgyb.vo.TableOfContents;
import com.zebone.nhis.compay.pub.service.NationalInsuranceService;
import com.zebone.nhis.compay.pub.service.NationalInsuranceUpDownService;
import com.zebone.nhis.compay.pub.vo.HisInsuResVo;
import com.zebone.nhis.compay.pub.vo.InsDownFileParamVo;
import com.zebone.nhis.compay.pub.vo.InsQgybStclear;
import com.zebone.nhis.compay.pub.vo.TransferToHospitalForRecordParam;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 全国医保业务服务类
 */
@Service
public class NationalInsuBusService {
	
	@Resource
    private NationalInsuBusMapper nationalInsuBusMapper;

    @Autowired
    private NationalInsuranceService service;

    @Autowired
    private NationalInsuranceUpDownService upDownService;

    
    /**
     * 根据人员就诊信息获取该笔结算的明细信息
     *
     * 020001001021
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo expenseDetailsQuery(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

        String psnNo = MapUtils.getString(paramMap,"psnNo");
        if(StringUtils.isEmpty(psnNo)){
            throw new BusException("人员编码【psn_no】不能为空");
        }
//        查询费用明细时,结算id可以为空,去除判断
        String setlId = MapUtils.getString(paramMap,"setlId");
//        if(StringUtils.isEmpty(setlId)){
//            throw new BusException("结算ID[setl_id]不能为空");
//        }
        String mdtrtId = MapUtils.getString(paramMap,"mdtrtId");
        if(StringUtils.isEmpty(mdtrtId)){
            throw new BusException("结算ID[mdtrt_id]不能为空");
        }
        HisInsuResVo result = service.expenseDetailsQuery(psnNo,setlId,mdtrtId,user);
        return result;
    }

    /**
     * 结算信息查询
     * 020001001020
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo settlementInformationQuery(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);



        String psnNo = MapUtils.getString(paramMap,"psnNo");
        if(StringUtils.isEmpty(psnNo)){
            throw new BusException("人员编码【psn_no】不能为空");
        }
        String setlId = MapUtils.getString(paramMap,"setlId");
        if(StringUtils.isEmpty(setlId)){
            throw new BusException("结算ID[setl_id]不能为空");
        }
        String mdtrtId = MapUtils.getString(paramMap,"mdtrtId");
        if(StringUtils.isEmpty(mdtrtId)){
            throw new BusException("结算ID[mdtrt_id]不能为空");
        }
        HisInsuResVo result = service.settlementInformationQuery(psnNo,setlId,mdtrtId,user);
        return result;
    }

    /**
     * 就诊信息查询
     *
     * 020001001022
     * 根据个人信息获取该人员在本机构一段时间内的就诊信息
     * 交易输入为单行数据，交易输出为多行数据
     * @return
     */
    public HisInsuResVo medicalInformationQuery(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);

        String beginTime = MapUtils.getString(paramMap,"begntime");
        String endTime = MapUtils.getString(paramMap,"endtime");
        Date begin = null;
        Date end = null;
        try {
            begin = DateUtils.parseDate(beginTime,new String[]{"yyyy-MM-dd HH:mm:ss"});
            end = DateUtils.parseDate(endTime,new String[]{"yyyy-MM-dd HH:mm:ss"});
        } catch (Exception e) {
            throw new BusException("时间格式错误");
        }

        String psnNo = MapUtils.getString(paramMap,"psnNo");
        if(StringUtils.isEmpty(psnNo)){
            throw new BusException("人员编码【psn_no】不能为空");
        }
        String medType = MapUtils.getString(paramMap,"medType");
        if(StringUtils.isEmpty(medType)){
            throw new BusException("医疗类别[medType]不能为空");
        }
        String mdtrtId = MapUtils.getString(paramMap,"mdtrtId");
        if(StringUtils.isEmpty(mdtrtId)){
//            throw new BusException("结算ID[mdtrt_id]不能为空");
        }
        HisInsuResVo result = service.medicalInformationQuery(psnNo,begin,end,medType,mdtrtId,user);
        return result;
    }

    /**
     * 转院备案撤销
     * 020001001023
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo transferToHospitalForRecordCancellation(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String psnNo = MapUtils.getString(paramMap,"psnNo");
        if(StringUtils.isEmpty(psnNo)){
            throw new BusException("psnNo不能为空");
        }

        String memo = MapUtils.getString(paramMap,"memo");


        String sql = "select TRT_DCLA_DETL_SN  from ins_qgyb_visit where PSN_NO = ?";
        Map<String,Object> insMap = DataBaseHelper.queryForMap(sql,psnNo);
        String trtDclaDetlSn = MapUtils.getString(insMap,"TRT_DCLA_DETL_SN");
        if(StringUtils.isEmpty(trtDclaDetlSn)){
            throw new BusException("待遇申报明细流水号[TRT_DCLA_DETL_SN]不能为空");
        }

        HisInsuResVo result = service.transferToHospitalForRecordCancellation(trtDclaDetlSn,psnNo,memo,user);
        return result;
    }

    /**
     * 转院备案
     * 020001001024
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo transferToHospitalForRecord(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        TransferToHospitalForRecordParam param = new TransferToHospitalForRecordParam();




        String mdtrtId = MapUtils.getString(paramMap,"mdtrtId");
        if(StringUtils.isEmpty(mdtrtId)){
            throw new BusException("mdtrtId不能为空");
        }

        String memo = MapUtils.getString(paramMap,"memo","");


        String psnNo = MapUtils.getString(paramMap,"psnNo");
        if(StringUtils.isEmpty(psnNo)){
            throw new BusException("人员编码【psn_no】不能为空");
        }

        String insutype = MapUtils.getString(paramMap,"insutype");
        String tel = MapUtils.getString(paramMap,"tel");
        String addr = MapUtils.getString(paramMap,"addr");
        String reflinMedinsNo = MapUtils.getString(paramMap,"reflinMedinsNo");
        String reflinMedinsName = MapUtils.getString(paramMap,"reflinMedinsName");
        String mdtrtareaAdmdvs = MapUtils.getString(paramMap,"mdtrtareaAdmdvs");
        String diagName = MapUtils.getString(paramMap,"diagName");
        String diagCode = MapUtils.getString(paramMap,"diagCode");
        String diseCondDscr = MapUtils.getString(paramMap,"diseCondDscr");
        //医院同意转院标志
        //0:否
        //1：是
        String hospAgreReflFlag = MapUtils.getString(paramMap,"hospAgreReflFlag");

        //1：市内转院
        //2:转往省内异地
        //3：转往省外异地
        String reflType = MapUtils.getString(paramMap,"reflType");
        String reflDateStr = MapUtils.getString(paramMap,"reflDateStr");
        String begndateStr = MapUtils.getString(paramMap,"begndate");
        String enddateStr = MapUtils.getString(paramMap,"enddate");
        Date reflDate = null;
        Date begndate = null;
        Date enddate = null;
        try {
            reflDate = DateUtils.parseDate(reflDateStr,new String[]{"yyyy-MM-dd"});
        } catch (Exception e) {
            throw new BusException("转院日期格式错误");
        }
        try {
            begndate = DateUtils.parseDate(begndateStr,new String[]{"yyyy-MM-dd"});
            enddate = DateUtils.parseDate(enddateStr,new String[]{"yyyy-MM-dd"});
        } catch (Exception e) {

        }
        String reflRea = MapUtils.getString(paramMap,"reflRea");
        String reflOpnn = MapUtils.getString(paramMap,"reflOpnn");
        String reflUsedFlag = MapUtils.getString(paramMap,"reflUsedFlag");

        param.setPsnNo(psnNo);
        param.setInsutype(insutype);
        param.setTel(tel);
        param.setAddr(addr);
        param.setInsuOptins("");
        param.setDiagName(diagName);
        param.setDiagCode(diagCode);
        param.setDiseCondDscr(diseCondDscr);
        param.setReflinMedinsNo(reflinMedinsNo);
        param.setReflinMedinsName(reflinMedinsName);
        param.setHospAgreReflFlag(hospAgreReflFlag);
        param.setReflType(reflType);
        param.setReflDate(reflDate);
        param.setReflRea(reflRea);
        param.setReflOpnn(reflOpnn);
        param.setBegndate(begndate);
        param.setEnddate(enddate);
        param.setReflUsedFlag(reflUsedFlag);
        param.setMdtrtareaAdmdvs(mdtrtareaAdmdvs);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<TransferToHospitalForRecordParam>> constraintViolationSet = validator.validate(param);
        StringBuffer stringBuffer = new StringBuffer();
        for (ConstraintViolation<TransferToHospitalForRecordParam> constraintViolation : constraintViolationSet) {
            stringBuffer.append("【"+constraintViolation.getMessage()+"】");
        }

        if(constraintViolationSet.size()>0){
            String errorMsg = stringBuffer.toString();
            throw new BusException(errorMsg);
        }
        HisInsuResVo result = service.transferToHospitalForRecord(param,user);
        String resultJson = (String) result.getResVo().getOutput();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(resultJson,JsonObject.class);
        JsonElement element =  jsonObject.get("reslut");
        jsonObject = element.getAsJsonObject();
        element = jsonObject.get("trtDclaDetlSn");
        String sn = element.getAsString();
        String sql = "update ins_qgyb_visit set TRT_DCLA_DETL_SN = ? where MDTRT_ID = ?";
        if(!StringUtils.isEmpty(sn)){
            DataBaseHelper.execute(sql,sn,mdtrtId);
        }

        System.out.println(sn);
        return result;
    }

    /**
     * 中医证候目录下载
     * 020001001025
     * @param params
     * @param user
     * @return
     */
    public HisInsuResVo tcmSyndromeCatalogDownload(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String ver = MapUtils.getString(paramMap,"ver");
        if(StringUtils.isEmpty(ver)){
            throw new BusException("最大版本号不能为空");
        }
        HisInsuResVo result = service.tcmSyndromeCatalogDownload(ver,user);
        return result;
    }


    private final String[] codeList = {"1301","1302","1303","1305","1306"};

    private static Map<String,Object> dictMap = new HashMap<>(16);
    private static Map<String,Object> dictListMap = new HashMap<>(16);
    static {
        dictMap.put("1301","1");
        dictMap.put("1302","1");
        dictMap.put("1303","1");
        dictMap.put("1304","1");
        dictMap.put("1305","2");
        dictMap.put("1306","3");
        dictListMap.put("1301","101");
        dictListMap.put("1302","102");
        dictListMap.put("1303","103");
        dictListMap.put("1304","104");
        dictListMap.put("1305","201");
        dictListMap.put("1306","301");
    }

    /**
     * 医用耗材目录下载 1306
     * 医疗服务项目目录 1305
     * 020001001026
     * @param params
     * @param user
     */
    public HisInsuResVo medicalConsumablesCatalogDownload(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String code = MapUtils.getString(paramMap,"code");
        String ver = MapUtils.getString(paramMap,"ver");
        String updateTime = MapUtils.getString(paramMap,"updateTime");
        String pageNum = MapUtils.getString(paramMap,"pageNum");
        String pageSize = MapUtils.getString(paramMap,"pageSize");

        String  listType= MapUtils.getString(dictListMap,code,"");
        String typeSql = "select MAX(VERSION) as VERSION from INS_QGYB_ITEM where LIST_TYPE=?";
        InsQgybItem insTiem = DataBaseHelper.queryForBean(typeSql,InsQgybItem.class,listType);
        if(insTiem!=null) {
            if(StringUtils.isNotBlank(insTiem.getVersion()))
            {
                ver=insTiem.getVersion();
            }
        }
        if(StringUtils.isEmpty(code)){
            throw new BusException("未传入接口编码【code】");
        }
        if(StringUtils.isEmpty(ver)){
            throw new BusException("未传入接口版本号【ver】");
        }
        HisInsuResVo result = new HisInsuResVo();
        if("1304".equals(code)){
            try{
                result = service.catalogDownload(updateTime,Integer.valueOf(pageNum),Integer.valueOf(pageSize),user);
            }catch (Exception e){
                throw new BusException(e.getMessage());
            }
        }else {
            try{
                result = service.catalogDownload(code,ver,user);
            }catch (Exception e){
                throw new BusException(e.getMessage());
            }
        }

        String resultJson = (String) result.getResVo().getOutput();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(resultJson,JsonObject.class);
        JsonElement jsonElement = jsonObject.get("filename");
        if(jsonElement==null)
        {
            return  result;
        }
        String fileName = jsonElement.getAsString();
        jsonElement = jsonObject.get("fileQuryNo");
        String fileQuryNo = jsonElement.getAsString();
        InsDownFileParamVo param = new InsDownFileParamVo();
        param.setFileName(fileName);
        param.setFileNo(fileQuryNo);
        List<Object[]> objects = upDownService.downAndGet(code,param);
        List<TableOfContents> tableList = fileToTable(objects);
        User user1 = (User)user;

        if(Arrays.stream(codeList).noneMatch(a->a.equals(code))){
            throw new BusException("请维护dictListMap集合");
        }

        try {
            List<InsQgybItem> updatelist=new ArrayList<>() ;
            List<InsQgybItem> insetlist=new ArrayList<>() ;

            List<InsQgybItem> itemList = tableToItem(tableList,code);
            for (InsQgybItem item : itemList) {
                item.setPkOrg(user1.getPkOrg());
                item.setDelFlag("0");
                item.setInsType("01");
                item.setItemType(MapUtils.getString(dictMap,code,""));
                item.setListType(MapUtils.getString(dictListMap,code,""));
                item.setEuHpdicttype("01");
                String sql = "select * from INS_QGYB_ITEM where INS_ITEM_CODE = ?";
                InsQgybItem resultItem = DataBaseHelper.queryForBean(sql,InsQgybItem.class,item.getInsItemCode());
                if(resultItem==null){
                    ApplicationUtils.setDefaultValue(item,true);
                    insetlist.add(item);
                }else {
                    BeanUtils.copyProperties(item,resultItem,getNullPropertyNames(item));
                    updatelist.add(resultItem);
                }
            }

            if(updatelist!=null&&updatelist.size()>0)
            {
                DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(InsQgybItem.class),updatelist);
                System.out.println("更新条数："+updatelist.size());
            }
            if(insetlist!=null&&insetlist.size()>0)
            {
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybItem.class),insetlist);
                System.out.println("新增条数："+insetlist.size());
            }
        } catch  (Exception e) {
            throw new BusException(e.getMessage());
        }
        return result;
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }




    private List<InsQgybItem> tableToItem(List<TableOfContents> tableList,String code) throws IllegalAccessException {
        List<InsQgybItem> itemList = new ArrayList<>();
        for (TableOfContents table : tableList) {
            Field[] fields = table.getClass().getDeclaredFields();
            InsQgybItem item = new InsQgybItem();
            for (Field field : fields) {
                field.setAccessible(true);
                FileMapping fileMapping = field.getAnnotation(FileMapping.class);
                if(fileMapping==null){
                    continue;
                }
                String column  = "";
                switch (code){
                    case "1301":
                        column = fileMapping.Table1301();
                        break;
                    case "1302":
                        column = fileMapping.Table1302();
                        break;
                    case "1303":
                        column = fileMapping.Table1303();
                        break;
                    case "1304":
                        column = fileMapping.Table1304();
                        break;
                    case "1305":
                        column = fileMapping.Table1305();
                        break;
                    case "1306":
                        column = fileMapping.Table1306();
                    break;
                }
                if(StringUtils.isEmpty(column)){
                   continue;
                }
                Object result =  field.get(table);

                Field[] itemFields = item.getClass().getDeclaredFields();
                for (Field itemField : itemFields) {
                    itemField.setAccessible(true);
                    com.zebone.platform.modules.dao.build.au.Field columnName = itemField.getAnnotation(com.zebone.platform.modules.dao.build.au.Field.class);
                    String value = columnName.value();
                    if(column.equals(value)){
                        if (("start_time").equals(column) || ("end_time").equals(column)|| ("drugReg_StartTime").equals(column)|| ("drugReg_EndTime").equals(column)|| ("appr_StartTime").equals(column)|| ("appr_EndTime").equals(column)){
                            if (Objects.nonNull(result)){
                                String dateString = result.toString();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
                                try {
                                    Date date = simpleDateFormat.parse(dateString);
                                    itemField.set(item,date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else {
                            itemField.set(item,result);
                        }

                    }
                }


            }
            itemList.add(item);
        }
        return itemList;
    }

    private List<TableOfContents> fileToTable(List<Object[]> objects){

        List<TableOfContents> tableList = new ArrayList<>();

        for (Object[] object : objects) {
            try {
                TableOfContents table = new TableOfContents();
                Field[] fields = table.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    FileMapping fileMapping = field.getAnnotation(FileMapping.class);
                    if(fileMapping==null){
                        continue;
                    }
                    int sort = fileMapping.serialNo();
                    if(sort<=object.length){
                        String value = (String) object[sort-1];
                        field.set(table,value);
                    }

                }
                tableList.add(table);
            } catch (IllegalAccessException e) {

            }
        }


        return tableList;
    }

    //************卡卡西***********


    /**
     * 人员定点记录查询(入参格式：{psn_no:“”})
     *020001001033
     * @param params
     * @param user
     * @return 医保返回list
     */
    public HisInsuResVo qryRecordDd(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String psnNo = MapUtils.getString(paramMap,"psn_no");
        if (StringUtils.isEmpty(psnNo)) {
            throw new BusException("功能号5302，人员定点信息查询失败：未获取到人员编号");
        }
        HisInsuResVo ret = service.qryRecordDd(psnNo,"03",user);
        return ret;
    }

    /**
     * 人员累计信息查询
     *020001001032
     * @param params
     * @param user
     * @return 医保返回list
     */
    public HisInsuResVo qryPersonnelAcc(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String psnNo = MapUtils.getString(paramMap,"psn_no");
        String cumYm = MapUtils.getString(paramMap,"cum_ym");
        if (StringUtils.isEmpty(psnNo)) {
            throw new BusException("功能号5206，人员累计信息查询失败：未获取到人员编号");
        }
        HisInsuResVo ret = service.qryPersonnelAcc(psnNo,cumYm,user);
        return ret;
    }

    /**
     * 在院信息查询
     *020001001027
     * @param params(begntime,endtime,psn_no)
     * @param user
     * @return 医保返回list
     */
    public HisInsuResVo qryInHospitalInfo(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String psnNo = MapUtils.getString(paramMap,"psn_no");
        String begntime = MapUtils.getString(paramMap,"begntime");
        String endtime = MapUtils.getString(paramMap,"endtime");
        if (StringUtils.isEmpty(begntime)) {
            throw new BusException("begntime:未获取到开始时间");
        }
        if (StringUtils.isEmpty(endtime)) {
            throw new BusException("endtime:未获取到结束时间");
        }
        HisInsuResVo ret = service.qryInHospitalInfo(psnNo,begntime,endtime,user);
        return ret;
    }

    /**
     * 转院信息查询
     *020001001028
     * @param params(begntime,endtime,psn_no)
     * @param user
     * @return
     */
    public HisInsuResVo qryTurnHospitalInfo(String params, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        String psnNo = MapUtils.getString(paramMap,"psn_no");
        String begntime = MapUtils.getString(paramMap,"begntime");
        String endtime = MapUtils.getString(paramMap,"endtime");
        Date begin = null;
        Date end = null;
        if (StringUtils.isEmpty(psnNo)) {
            throw new BusException("psn_no:未获取到人员编号");
        }
        if (StringUtils.isEmpty(begntime)) {
            throw new BusException("begntime:未获取到开始时间");
        }
        try {
            begin = DateUtils.parseDate(begntime,new String[]{"yyyy-MM-dd HH:mm:ss"});
            if(StringUtils.isNotBlank(endtime)){
                end = DateUtils.parseDate(endtime,new String[]{"yyyy-MM-dd HH:mm:ss"});
            }
        } catch (ParseException e) {
            throw new BusException("时间格式错误");
        }
        HisInsuResVo ret = service.qryTurnHospitalInfo(psnNo,begin,end,user);
        return ret;

    }

    /**
     * 医药机构信息获取
     *020001001031
     * @param params  fixmedins_type 1	定点医疗机构	2	定点零售药店
     * @param user
     * @return
     */
    public HisInsuResVo qryOrgInfo(String params, IUser user){
        //Map<String, Object> paramMap = JsonUtil.readValue(params, Map.class);
        HisInsuResVo ret = service.qryOrgInfo(user);
        return ret;
    }	
    
    /**
     * 查询患者抗癌药登记信息
     * 015001015025
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryNationalList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		User u = (User)user;
		paramMap.put("pk_org",u.getPkOrg());
		List<Map<String, Object>> list = nationalInsuBusMapper.queryNationalList(paramMap);
    	return list;
    }    
    
    /**
     * 保存患者医保谈判药
     * 015001015026
     * @param param[pk_pi(患者主键),code_pi(患者编码),med_list_code(药品医保编码),pk_emp(申请医生)]
     * @param code
     */
    public void savecancerFighting(String param,IUser user){
    	List<Map<String, Object>> paramMap = JsonUtil.readValue(param, List.class);
    	User u = (User)user;
    	List<CancerFighting> cancerFightingList = new ArrayList<CancerFighting>();
    	for (int i=0;i<paramMap.size();i++) {
	    	if(paramMap.get(i).get("pkInspd") == null){
	    	CancerFighting cancerFighting = new CancerFighting();
	    	ApplicationUtils.setDefaultValue(cancerFighting, true);
	    	cancerFighting.setPkPi((String)paramMap.get(i).get("pkPi"));
	    	cancerFighting.setCodePi((String) paramMap.get(i).get("codePi"));
	    	cancerFighting.setMedListCode((String) paramMap.get(i).get("medListCode"));
	    	cancerFighting.setPkEmp((String) paramMap.get(i).get("pkEmp"));
	    	cancerFightingList.add(cancerFighting);
	    	}
    	}
    	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CancerFighting.class), cancerFightingList);
    }
    
    /**
     * 删除患者医保谈判药
     * 015001015027
     * @param param
     * @param code
     */
    public void deletecancerFighting(String param,IUser user){
    	Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pk_inspd = MapUtils.getString(paramMap,"pkInspd");
    	if (pk_inspd != null) {
    	DataBaseHelper.execute("update ins_qgyb_pd set del_flag='1' where pk_inspd = ?", pk_inspd);
    	} else {
			throw new BusException("传入的主键为空值");
		}
    }

    /**
     *020001001042
     * 保存全国医保异地清分结算数据
     * @param param
     * @param user
     */
    public void saveQGYBYdStclear(String param, IUser user){
        InsQgybStclear[] list = JsonUtil.readValue(param, InsQgybStclear[].class);
        for (InsQgybStclear insQgYbStclear : list) {
            DataBaseHelper.insertBean(insQgYbStclear);
        }
    }

}
