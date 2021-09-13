package com.zebone.nhis.compay.pub.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zebone.nhis.ma.pub.platform.syx.vo.PiMasterEmpiInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybDict;
import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybDicttype;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.compay.pub.dao.MedicalInsuranceMapper;
import com.zebone.nhis.compay.pub.vo.DictTypesData;
import com.zebone.nhis.compay.pub.vo.InsCommonMedical;
import com.zebone.nhis.compay.pub.vo.InsCommonTableParam;
import com.zebone.nhis.compay.pub.vo.InsItemResult;
import com.zebone.nhis.compay.pub.vo.InsItemsBySchsrvData;
import com.zebone.nhis.compay.pub.vo.MedicalInsuranceVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 医保管理
 *
 * @author Administrator
 */
@Service
public class MedicalInsuranceService {

    @Autowired
    private MedicalInsuranceMapper medicalInsuranceMapper;

    @Autowired
    private CgQryMaintainService cgQryMaintainService;

    private Logger logger = LoggerFactory.getLogger("com.zebone");

    /**
     * 医保字典保存
     *
     * @param param
     * @param user
     */
    public void saveMedicalInsurance(String param, IUser user) {
        MedicalInsuranceVo medicalInsuranceVo = JsonUtil.readValue(param,
                MedicalInsuranceVo.class);

        String pkHp = medicalInsuranceVo.getPkHp();
        List<DictTypesData> dictTypes = medicalInsuranceVo.getDictTypes();
        List<InsZsybDicttype> insDicttypes = new ArrayList<>();
        List<InsZsybDict> insDicts = new ArrayList<>();

        if (dictTypes == null || dictTypes.size() == 0) {
            throw new BusException("保存医保下载字典参数为空");
        }

        for (DictTypesData dictTypesData : dictTypes) {
            // 保存医保数据字典类别
            InsZsybDicttype insDicttype = new InsZsybDicttype();
            ApplicationUtils.setDefaultValue(insDicttype, true);
            insDicttype.setPkHp(pkHp);
            insDicttype.setCodeType(dictTypesData.getCodeType());
            insDicttype.setNameType(dictTypesData.getNameType());
            insDicttype.setSpcode(dictTypesData.getSpcode());
            insDicttype.setdCode(dictTypesData.getdCode());
            insDicttypes.add(insDicttype);

            for (InsZsybDict dictsList : dictTypesData.getDicts()) {
                // 保存医保数据字典数据
                InsZsybDict insDict = new InsZsybDict();
                ApplicationUtils.setDefaultValue(insDict, true);
                insDict.setPkInsdicttype(insDicttype.getPkInsdicttype());
                insDict.setName(dictsList.getName());
                insDict.setCode(dictsList.getCode());
                insDict.setSpcode(dictsList.getSpcode());
                insDict.setdCode(dictsList.getdCode());
                insDicts.add(insDict);
            }
        }
        if (insDicttypes.size() > 0) {
            DataBaseHelper.batchUpdate(
                    DataBaseHelper.getInsertSql(InsZsybDicttype.class),
                    insDicttypes);
        }
        if (insDicts.size() > 0) {
            DataBaseHelper.batchUpdate(
                    DataBaseHelper.getInsertSql(InsZsybDict.class), insDicts);
        }
    }

    /**
     * 通用医保表保存
     *
     * @param param
     * @param user
     */
    public void saveCommonMedical(String param, IUser user) {
        InsCommonTableParam insCommonParam = JsonUtil.readValue(param,
                InsCommonTableParam.class);
        String tableName = CommonUtils.getString(insCommonParam.getTableName());
        String primaryKey = CommonUtils.getString(insCommonParam
                .getPrimaryKey());
        String uniqueKey = CommonUtils.getString(insCommonParam.getUniqueKey());
        List<String> dataColumn = insCommonParam.getDataColumn();
        List<List<String>> dataValue = insCommonParam.getDataValue();

        List<Map<String, Object>> valueList = new ArrayList<>();
        Set<String> primaryStr = new HashSet<>();
        StringBuffer keyBuffer = new StringBuffer();
        keyBuffer.append(":pk,");
        for (int i = 0; i < dataColumn.size(); i++) {
            keyBuffer.append(":").append(dataColumn.get(i)).append(",");
        }
        keyBuffer.append(":pkOrg,:creator,:createTime,:delFlag,:ts,:modifier");

        // 遍历值和列，构造批量查询的value list
        for (List<String> list : dataValue) {
            if (list.size() != dataColumn.size()) {
                throw new BusException("传入的列名和列值的数量不匹配");
            }
            Map<String, Object> colValueMap = new LinkedHashMap<>();

            colValueMap.put("pk", NHISUUID.getKeyId());
            for (int i = 0; i < dataColumn.size(); i++) {
                // 查询关键列在column的第几个字段
                if (dataColumn.get(i).equalsIgnoreCase(uniqueKey)) {
                    primaryStr.add(list.get(i));
                }
                colValueMap.put(dataColumn.get(i), list.get(i));
            }

            colValueMap.put("pkOrg", UserContext.getUser().getPkOrg());
            colValueMap.put("creator", UserContext.getUser().getPkEmp());
            colValueMap.put("createTime", new Date());
            colValueMap.put("delFlag", "0");
            colValueMap.put("ts", new Date());
            colValueMap.put("modifier", UserContext.getUser().getPkEmp());
            valueList.add(colValueMap);
        }
        // 将数据库中存在primaryKey的列删除掉 ----这块要重新处理，做联合主键

        String primarySql = "delete from " + tableName + " where " + uniqueKey
                + " = ?";
        Iterator<String> it = primaryStr.iterator();
        while (it.hasNext()) {
            DataBaseHelper.execute(primarySql, new Object[]{it.next()});
        }

        // 方法1 集合类的通用遍历方式, 从很早的版本就有, 用迭代器迭代
        StringBuffer insertBuffer = new StringBuffer();
        insertBuffer
                .append("insert into ")
                .append(tableName)
                .append("(")
                .append(primaryKey)
                .append(",")
                .append(dataColumn.toString().substring(1,
                        dataColumn.toString().length() - 1))
                .append(", PK_ORG, CREATOR, CREATE_TIME, DEL_FlAG, TS, MODIFIER)")
                .append(" values(").append(keyBuffer.toString()).append(")");

        DataBaseHelper.batchUpdate(CommonUtils.getString(insertBuffer),
                valueList);

    }

    /**
     * 查询通用医保表数据,分页查询
     *
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public Page<List<Object>> selectCommonMedical(String param, IUser user) {
        InsCommonMedical insCommonMedical = JsonUtil.readValue(param,
                InsCommonMedical.class);
        List<String> dataColumn = insCommonMedical.getDataColumn();
        int pageIndex = CommonUtils.getInteger(insCommonMedical.getPageIndex());
        int pageSize = CommonUtils.getInteger(insCommonMedical.getPageSize());
        // 分页操作
        MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> queryForList = medicalInsuranceMapper
                .selectCommonMedical(insCommonMedical);

        List<List<Object>> returnList = new ArrayList<>();
        // 根据mapper查询出的结果和传递的列顺序 返回值list
        for (int i = 0; i < queryForList.size(); i++) {
            List<Object> mapList = new LinkedList<>();
            Map<String, Object> itemMap = queryForList.get(i);
            for (String dataItem : dataColumn) {
                mapList.add(itemMap.get(dataItem));
            }
            returnList.add(mapList);
        }

        Page<List<Object>> page = MyBatisPage.getPage();
        page.setRows(returnList);
        return page;
    }

    /**
     * 删除医保匹配
     *
     * @param param
     * @param user
     */
    public void deleteInsItem(String param, IUser user) {
        // 用医保匹配主键
        String pkInsItemMap = JsonUtil.readValue(param, String.class);
        DataBaseHelper
                .update("update INS_XIAN_ITEM_MAP set del_flag='1' where PK_INSITEMMAP=? and del_flag = '0'",
                        new Object[]{pkInsItemMap});
    }

    /**
     * 保存匹配审批结果
     *
     * @param param
     * @param user
     */
    public void saveInsitemResult(String param, IUser user) {
        @SuppressWarnings("unchecked")
        List<InsItemResult> insItems = JsonUtil.readValue(param, List.class);

        String sql = "update INS_XIAN_ITEM_MAP set eu_examine=:euExamine and examine_note=:examineNote"
                + " where pk_hp=:pkHp and pk_item=:pkItem and code_insitem=:codeInsitem and del_flag='0'";

        DataBaseHelper.batchUpdate(sql, insItems);

        // for (InsItemResult insItem : insItems) {
        // String pkHp = CommonUtils.getString(insItem.getPkHp());
        // String pkItem = CommonUtils.getString(insItem.getPkItem());
        // String codeInsitem = CommonUtils.getString(insItem.getCodeInsitem());
        // String euExamine = CommonUtils.getString(insItem.getEuExamine());
        // String examineNote = CommonUtils.getString(insItem.getExamineNote());
        // Map<String, String> map = new HashMap<>();
        // map.put("pkHp", pkHp);
        // map.put("pkItem", pkItem);
        // map.put("codeInsitem", codeInsitem);
        // map.put("euExamine", euExamine);
        // map.put("examineNote", examineNote);
        //
        // String usql =
        // "update INS_XIAN_ITEM_MAP set eu_examine=:euExamine and examine_note=:examineNote "
        // +
        // " where pk_hp=:pkHp and pk_item=:pkItem and code_insitem=:codeInsitem and del_flag = '0'";
        // DataBaseHelper.update(usql, InsXaybItemMap.class, map);
        // }
    }

    /**
     * 根据医保卡信息新建患者
     *
     * @param param
     * @param user
     * @return
     */
    public PiMaster createPatientByInsCard(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        PiMaster master = JsonUtil.readValue(param, PiMaster.class);
        String ybInfo = null;
        if (JsonUtil.getJsonNode(param, "yBPatientInfo") != null) {
            ybInfo = JsonUtil.getJsonNode(param, "yBPatientInfo").toString();
        }
        String idNo = CommonUtils.getString(paramMap.get("idNo"));// 身份证号码
        String insurNo = CommonUtils.getString(paramMap.get("insurNo"));// 医保卡号
        String pkHp = CommonUtils.getString(paramMap.get("pkHp"));// 医保计划
        String namePi = CommonUtils.getString(paramMap.get("namePi"));// 患者姓名
        Date birthDate = DateUtils.strToDate(paramMap.get("birthDate")
                .toString());
        String mobile = CommonUtils.getString(paramMap.get("mobile"));// 手机号码
        String dtSex = CommonUtils.getString(paramMap.get("dtSex"));// 患者性别
        String pkInsu = CommonUtils.getString(paramMap.get("pkInsu"));// 住院号

        if (StringUtils.isBlank(idNo) && StringUtils.isBlank(insurNo)) {
            throw new BusException("身份证号码和医保证号同时为空！");
        }

        String insCode = null;
        if (paramMap.get("insCode") != null) {
            insCode = CommonUtils.getString(paramMap.get("insCode"));// 第三方医保标识号
        }
        Map<String, Object> queryForMap = null;
        if (!org.springframework.util.StringUtils.isEmpty(idNo)) { // 优先身份证查找
            queryForMap = DataBaseHelper
                    .queryForMap(
                            "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                    + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.id_no=? and pm.del_flag='0'",
                            new Object[]{idNo.toUpperCase()});
        }

		if (queryForMap != null) {
			master.setNamePi(namePi);
		}
        if (queryForMap == null) { // 医保卡查找
            if (insCode != null
                    && (insCode.equals("04") || insCode.equals("03")) 
                    && !CommonUtils.isEmptyString(insurNo) ) // 灵璧离休医保,宿州市医保
            {
                queryForMap = DataBaseHelper
                        .queryForMap(
                                "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                        + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.insur_no=? and pm.del_flag='0'",
                                new Object[]{insurNo.toUpperCase()});
            }

        }

        // 为空-新增保存
        if (queryForMap == null) {
            // PiMaster master = new PiMaster();
            // 患者是第一次来医院
//            String codeIp = CommonUtils.getString(paramMap.get("codeIp"));// 住院号
//            if (CommonUtils.isEmptyString(codeIp)) {
//                codeIp = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL);
//                master.setCodeIp(codeIp);//获取住院号
//                logger.info("==============【" + master.getNamePi() + "】医保读卡新增患者获取住院号:" + master.getCodeIp() + "============");
//            }
            Map<String, Object> picateMap = DataBaseHelper
                    .queryForMap(
                            "select pk_picate from pi_cate where flag_def='1' and del_flag='0'",
                            new Object[]{});

            if (picateMap != null && picateMap.get("pkPicate") != null) {
                master.setPkPicate(picateMap.get("pkPicate").toString());// 默认是普通患者分类
            }

            String codePi = ApplicationUtils
                    .getCode(SysConstant.ENCODERULE_CODE_HZ);
            master.setCodePi(codePi);// 患者编码
            master.setIdNo(idNo);// 身份证号
            master.setDtIdtype("01");// 证件类型 --01
            master.setInsurNo(insurNo);// 医保卡号
            master.setNamePi(namePi);// 姓名
            master.setDtSex(dtSex);// 患者性别
            master.setBirthDate(birthDate);// 出生日期
            master.setMobile(mobile);// 手机号码
            //master.setCodeIp(codeIp);
            master.setCodeOp(ApplicationUtils
                    .getCode(SysConstant.ENCODERULE_CODE_MZBL)); // 门诊号
            master.setCodeEr(ApplicationUtils.getCode("0303"));

            ApplicationUtils.setDefaultValue(master, true);
            DataBaseHelper.insertBean(master);
            // 设置医保计划
            PiInsurance insurance = new PiInsurance();
            insurance.setPkPi(master.getPkPi());
            insurance.setPkHp(pkHp);
            insurance.setDelFlag("0");
            insurance.setCreateTime(new Date());
            DataBaseHelper.insertBean(insurance);
            // 新增时，插入一条PiAcc记录
            PiAcc acc = new PiAcc();
            acc.setPkPi(master.getPkPi());
            //acc.setCodeAcc(codeIp);
            acc.setAmtAcc(BigDecimal.ZERO);
            acc.setCreditAcc(BigDecimal.ZERO);
            acc.setEuStatus(PiConstant.ACC_EU_STATUS_1);
            DataBaseHelper.insertBean(acc);
        } else {// 更新保存
            //当住院号为空时生成新的
//            String codeIp = CommonUtils.getString(queryForMap.get("codeIp"));// 住院号
//            if (CommonUtils.isEmptyString(codeIp)) {
//                codeIp = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL);
//                master.setCodeIp(codeIp);//获取住院号
//                DataBaseHelper.update("update pi_master set code_ip=? where pk_pi=?", new Object[]{codeIp, master.getPkPi()});
//                logger.info("==============【" + master.getNamePi() + "】医保患者读卡更新获取住院号:" + master.getCodeIp() + "============");
//            }
            // 把当前传递的医保卡号设为医保卡号
            master.setPkPi(queryForMap.get("pkPi").toString());
            master.setInsurNo(insurNo);// 医保卡号
            // if (picateMap != null && picateMap.get("pkPicate") != null) {
            // master.setPkPicate(picateMap.get("pkPicate").toString());//
            // 默认是普通患者分类
            // }

            //根据pk_pi查询患者基本信息addrcode_birth、addrcode_origin、addrcode_regi、addrcode_cur地址字段，如果有值则不更新该信息
            PiMaster piAddrInfo = DataBaseHelper.queryForBean(
                    "select addrcode_birth,addrcode_origin,addrcode_regi,addrcode_cur from PI_MASTER where pk_pi = ?",
                    PiMaster.class,
                    new Object[]{queryForMap.get("pkPi").toString()}
                    );

            if(piAddrInfo!=null){
                if(!CommonUtils.isEmptyString(piAddrInfo.getAddrcodeBirth())){
                    master.setAddrcodeBirth(null);
                    master.setAddrBirth(null);
                }

                if(!CommonUtils.isEmptyString(piAddrInfo.getAddrcodeOrigin())){
                    master.setAddrcodeOrigin(null);
                    master.setAddrOrigin(null);
                }

                if(!CommonUtils.isEmptyString(piAddrInfo.getAddrcodeRegi())){
                    master.setAddrcodeRegi(null);
                    master.setAddrRegi(null);
                }

                if(!CommonUtils.isEmptyString(piAddrInfo.getAddrcodeCur())){
                    master.setAddrcodeCur(null);
                    master.setAddrCur(null);
                }
            }

            //修改lb项目bug[27158],修改更新语句并放入mapper的xml中并进行字段判空，被注代码后期无问题可以进行清理
            medicalInsuranceMapper.updatrPiMaster(master);
           /* String insurSql = "update pi_master set INSUR_NO = ? ,"
                    + " ADDRCODE_BIRTH = ? ,ADDR_BIRTH = ? ,ADDRCODE_ORIGIN"
                    + " = ? ,ADDR_ORIGIN = ?,ADDRCODE_REGI = ? ,ADDR_REGI = ?,ADDR_REGI_DT = ?,POSTCODE_REGI = ?,ADDRCODE_CUR = ?,"
                    + "ADDR_CUR = ? ,ADDR_CUR_DT = ? ,POSTCODE_CUR = ?,POSTCODE_WORK = ?"
                    + " where del_flag = '0' and pk_pi = ?";
            DataBaseHelper.update(
                    insurSql,
                    new Object[]{insurNo, master.getAddrcodeBirth(),
                            master.getAddrBirth(), master.getAddrcodeOrigin(),
                            master.getAddrOrigin(), master.getAddrcodeRegi(),
                            master.getAddrRegi(), master.getAddrRegiDt(),
                            master.getPostcodeRegi(), master.getAddrcodeCur(),
                            master.getAddrCur(), master.getAddrCurDt(),
                            master.getPostcodeCur(), master.getPostcodeWork(),
                            queryForMap.get("pkPi").toString()});*/
            
            // 先把之前存在的医保计划设成非默认的
            DataBaseHelper
                    .update("update pi_insurance set flag_def='0' where pk_pi=? and del_flag='0'",
                            new Object[]{queryForMap.get("pkPi").toString()});

            Map<String, Object> map = new HashMap<>();
            map.put("pkHp", pkHp);
            map.put("pkPi", queryForMap.get("pkPi").toString());
            // 设置医保计划
            String insuranceSql = "update pi_insurance set PK_HP=:pkHp, FLAG_DEF='1' "
                    + " where del_flag='0' and pk_pi=:pkPi ";
            DataBaseHelper.update(insuranceSql, map);

            Date date = new Date();
            Calendar no = Calendar.getInstance();
            no.setTime(date);
            no.set(Calendar.DATE, no.get(Calendar.DATE) - 3);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String begin = sdf.format(no.getTime()).concat("000000");
            String end = sdf.format(date).concat("235959");

            // 门诊收费时更新PV_ENCOUNTER的医保计划
            Map<String, Object> pvMap = DataBaseHelper
                    .queryForMap(
                            "select * from "
                                    + "PV_ENCOUNTER where pk_pi = ? and del_flag = '0'"
                                    + " and eu_pvtype in ('1','2') and create_time < to_date(?, 'yyyymmddhh24miss')  and"
                                    + " create_time > to_date(?, 'yyyymmddhh24miss')",
                            new Object[]{queryForMap.get("pkPi").toString(),
                                    end, begin});
            // 存在就诊记录
            if (pvMap != null && pvMap.get("pkPi") != null) {
                DataBaseHelper
                        .update("update PV_ENCOUNTER set pk_insu = ? where pk_pi= ? and del_flag='0' and eu_pvtype='3' "
                                        + " and eu_pvtype in ('1','2') and create_time < to_date(?, 'yyyymmddhh24miss')  and"
                                        + " create_time > to_date(?, 'yyyymmddhh24miss')",
                                new Object[]{pkHp,
                                        pvMap.get("pkPi").toString(), end,
                                        begin});

            }
        }
        logger.info("==========读医保卡创建患者【" + master.getNamePi() + "】，获取住院号为:" + master.getCodeIp() + "=========");
        PiMaster piMaster = null;

        if (insCode != null && insCode.equals("04") || (StringUtils.isBlank(idNo))) // 灵璧离休医保
        {
            piMaster = DataBaseHelper
                    .queryForBean(
                            "select * from pi_master where del_flag ='0' and insur_no=?",
                            PiMaster.class, insurNo);
        }
        if(piMaster==null){
            piMaster = DataBaseHelper.queryForBean(
                    "select * from pi_master where del_flag ='0' and id_no=? order by CREATE_TIME desc ",
                    PiMaster.class, idNo);
        }
        return piMaster;
    }

    //获取住院号   获取规则：1）首先从被退院的患者信息中获取； 2）如果获取的返回值为空，从系统编码规则接口中获取。
    private String getCodeIp(String codeIp) {
        if (!CommonUtils.isEmptyString(codeIp)) {
            codeIp = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL);
        }
        return codeIp;
    }

    /**
     * 根据排班资源信息 和附加费信息获取详细计费项目明细----只考虑挂一个号的情况
     *
     * @param param
     * @param user
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public List<InsItemsBySchsrvData> getItemsBySchsrv(String param, IUser user) {
        Map<String, Object> mapListParam = JsonUtil.readValue(param, Map.class);
        List<InsItemsBySchsrvData> mapResultList = new ArrayList<>();
        InsItemsBySchsrvData qryThisServicePrice = null;
        List<Map<String, Object>> opDtLists = (List<Map<String, Object>>) mapListParam
                .get("opDtList");// 附加费用主键(包括对应的数量)
        // Map<String, Object> mapSchsrv =
        // cgQryMaintainService.qrySchSrvByPkSchsrv(mapListParam);
        // 根据排班服务主键查询对应的收费项目主键
        List<String> pkItems = cgQryMaintainService
                .qrySchSrvOrdsByPkSchsrv(mapListParam);
        // 将前台传过来的（勾选的附加费的pkitem加入进去），将list转换成map
        Map<String, Double> mapOpDtLists = new HashMap<String, Double>();
        for (Map<String, Object> mapTemp : opDtLists) {
            pkItems.add(mapTemp.get("pkItem").toString());
            mapOpDtLists.put(mapTemp.get("pkItem").toString(),
                    Double.parseDouble(mapTemp.get("quan").toString()));
        }
        for (String pkItem : pkItems) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("pkItem", pkItem);
            itemMap.put("pkOrg", ((User) user).getPkOrg());
            itemMap.put("pkHp", mapListParam.get("pkInsu").toString());// 医保服务主键
            itemMap.put("dateHap", Calendar.getInstance().getTime()); // 取当前时间
            Map<String, Object> bdItem = cgQryMaintainService
                    .qryBdItemByPk(itemMap);
            BdHp bdHap = cgQryMaintainService.qryBdHpInfo(itemMap);
            // 定价模式是本服务定价
            if (bdItem == null) {
                throw new BusException("【" + pkItem + "】" + ":未维护此收费项目");
            }
            if (bdItem.get("flagActive") == null
                    || "0".equals(bdItem.get("flagActive").toString())) {
                throw new BusException("【" + bdItem.get("name") + "】"
                        + ":此收费项目未启用");
            }
            if (bdItem.get("delFlag") == null
                    || "1".equals(bdItem.get("delFlag").toString())) {
                throw new BusException("【" + bdItem.get("name") + "】"
                        + ":此收费项目已删除");
            }
            qryThisServicePrice = queryOraginalMessage(itemMap);
            // 医保下没有此收费项目
            if (qryThisServicePrice == null) {
                throw new BusException("【" + bdItem.get("name") + "】 未在患者的医保【"
                        + bdHap.getName() + "】下维护价格");
            }
            // 如果是排班项目默认只有1个，前台传入的附加项目有几个取几个。
            if (mapOpDtLists.containsKey(pkItem)) {
                qryThisServicePrice.setQuan(mapOpDtLists.get(pkItem)
                        .doubleValue());
            } else {
                qryThisServicePrice.setQuan(1.0D);
            }
            qryThisServicePrice.setAmount(qryThisServicePrice.getPrice()
                    * qryThisServicePrice.getQuan());

            mapResultList.add(qryThisServicePrice);
        }
        return mapResultList;
    }

    // 查询详细计费项目明细
    private InsItemsBySchsrvData queryOraginalMessage(
            Map<String, Object> mapParam) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select item.pk_item,item.name as itemName,price.PRICE,item.spec,unit.name as unitName,item.code ");
        sql.append(" from bd_item item ");
        sql.append("inner join bd_item_price price on item.pk_item=price.pk_item and price.pk_org = ? and price.flag_stop = 0 and nvl(price.del_flag,'0') = '0'");
        sql.append(" left join BD_UNIT unit on item.PK_UNIT = unit.pk_unit ");
        sql.append(" inner join bd_pricetype_cfg cfg on price.eu_pricetype=cfg.eu_pricetype and cfg.pk_hp=? and cfg.pk_org=? and nvl(cfg.del_flag,'0') = '0' ");
        sql.append(" where item.pk_item = ? and price.date_begin <= ? and price.date_end > ? and nvl(item.del_flag,'0') = '0'  ");

        InsItemsBySchsrvData qryThisServicePrice = DataBaseHelper.queryForBean(
                sql.toString(),
                InsItemsBySchsrvData.class,
                new Object[]{mapParam.get("pkOrg"), mapParam.get("pkHp"),
                        mapParam.get("pkOrg"), mapParam.get("pkItem"),
                        mapParam.get("dateHap"), mapParam.get("dateHap")});
        // new Object[] {mapParam.get("pkHp"), mapParam.get("pkItem")});
        return qryThisServicePrice;
    }

    // 当是药品查询的是bd_pd时，构造sql
    private String buildPdSql(String joinType) {
        /**
         * bd_pd中的pk_pd是INS_XAYB_ITEM_MAP中间表的外键pk_item
         * INS_XAYB_ITEM_MAP和INS_XAYB_ITEM通过联合主键关联 map中的 code_Insitem; // 医保项目编号
         * ------item中的 ybtyxmbm; //医保通用项目编码 map中的 ybxxzch; // 医保信息注册号
         * ------item中的 zcxxxlh; //注册信息序列号
         */
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("select pd.pk_pd as pkItems,")
                .append("pd.name as hisName,")
                .append("pd.short_name as shortName,")
                .append("pd.pk_factory as factoryName,")
                .append("pd.spcode as hisspcode,")
                .append("pd.spec as hisSpec,")
                .append("pd.dt_dosage as dosageName,")
                .append("pd.pk_unit_pack as unitPackName,")
                .append("pd.price as hisPrice, ")
                .append("pd.dt_abrd as abordFlag,")
                .append("map.EU_EXAMINE,")
                .append("map.EXAMINE_NOTE from bd_pd pd ")
                .append(joinType)
                .append(" join INS_XAYB_ITEM_MAP map on map.pk_item = pd.PK_PD ")
                .append(joinType)
                .append(" join INS_XAYB_ITEM xayb on xayb.ybtyxmbm = map.code_Insitem and xayb.zcxxxlh = map.ybxxzch");

        return sBuffer.toString();
    }

    // 当是药品查询的是bd_item时，构造sql
    private String buildItemSql(String joinType) {
        /**
         * bd_item中的pk_item是INS_XAYB_ITEM_MAP中间表的外键pk_item
         * INS_XAYB_ITEM_MAP和INS_XAYB_ITEM通过联合主键关联 map中的 code_Insitem; // 医保项目编号
         * ------item中的 ybtyxmbm; //医保通用项目编码 map中的 ybxxzch; // 医保信息注册号
         * ------item中的 zcxxxlh; //注册信息序列号
         */
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("select item.pk_item as pkItems,")
                .append("item.name as hisName,")
                .append("item.name_prt as shortName,")
                .append("null as factoryName,")
                .append("item.spcode as hisspcode,")
                .append("item.spec as hisSpec,")
                .append("null as dosageName,")
                .append("item.pk_unit as unitPackName,")
                .append("item.price as hisPrice,")
                .append("null as abordFlag,")
                .append("map.EU_EXAMINE,")
                .append("map.EXAMINE_NOTE from bd_item item ")
                .append(joinType)
                .append(" join INS_XAYB_ITEM_MAP map on map.pk_item = item.pk_item ")
                .append(joinType)
                .append(" join INS_XAYB_ITEM xayb on xayb.ybtyxmbm = map.code_Insitem and xayb.zcxxxlh = map.ybxxzch");

        return sBuffer.toString();
    }

    /**
     * 根据医保卡信息新建患者
     *
     * @param param
     * @param user
     * @return
     */
    public PiMaster updatePatientByInsCard(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        PiMaster master = JsonUtil.readValue(param, PiMaster.class);
        String idNo = CommonUtils.getString(paramMap.get("idNo"));// 身份证号码
        String insurNo = CommonUtils.getString(paramMap.get("insurNo"));// 医保卡号
        String pkHp = CommonUtils.getString(paramMap.get("pkHp"));// 医保计划
        String insCode = null;
        Map<String, Object> queryForMap = null;
        if (paramMap.get("insCode") != null) {
            insCode = CommonUtils.getString(paramMap.get("insCode"));// 第三方医保标识号
            if (!org.springframework.util.StringUtils.isEmpty(insCode)) {
                if (insCode.equals("07")) { // 农合
                    queryForMap = DataBaseHelper
                            .queryForMap(
                                    "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                            + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.id_no=? and pm.del_flag='0'",
                                    new Object[]{idNo.toUpperCase()});
                } else if (insCode.equals("04")) { // 离休
                    queryForMap = DataBaseHelper
                            .queryForMap(
                                    "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                            + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.insur_no=? and pm.del_flag='0'",
                                    new Object[]{insurNo.toUpperCase()});
                } else if (insCode.equals("03")) { // 宿州市
                    queryForMap = DataBaseHelper
                            .queryForMap(
                                    "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                            + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.id_no=? and pm.del_flag='0'",
                                    new Object[]{idNo.toUpperCase()});

                    if (queryForMap == null) {
                        queryForMap = DataBaseHelper
                                .queryForMap(
                                        "select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
                                                + "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.insur_no=? and pm.del_flag='0'",
                                        new Object[]{insurNo.toUpperCase()});
                    }

                }

            }
        }

        if (queryForMap != null) {

            String insurSql = "update pi_master set INSUR_NO = ?  where del_flag = '0' and pk_pi = ?";
            DataBaseHelper.update(insurSql, new Object[]{insurNo,
                    queryForMap.get("pkPi").toString()});

            DataBaseHelper
                    .update("update pi_insurance set flag_def='0' where pk_pi=? and del_flag='0'",
                            new Object[]{queryForMap.get("pkPi").toString()});
            Map<String, Object> map = new HashMap<>();
            map.put("pkHp", pkHp);
            map.put("pkPi", queryForMap.get("pkPi").toString());
            String insuranceSql = "update pi_insurance set PK_HP=:pkHp, FLAG_DEF='1' "
                    + " where del_flag='0' and pk_pi=:pkPi ";
            DataBaseHelper.update(insuranceSql, map);
            Date date = new Date();
            Calendar no = Calendar.getInstance();
            no.setTime(date);
            no.set(Calendar.DATE, no.get(Calendar.DATE));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String begin = sdf.format(no.getTime()).concat("000000");
            String end = sdf.format(date).concat("235959");
            Map<String, Object> pvMap = DataBaseHelper
                    .queryForMap(
                            "select * from "
                                    + "PV_ENCOUNTER where pk_pi = ? and del_flag = '0'"
                                    + " and eu_pvtype in ('1','2') and create_time < to_date(?, 'yyyymmddhh24miss')  and"
                                    + " create_time > to_date(?, 'yyyymmddhh24miss')",
                            new Object[]{queryForMap.get("pkPi").toString(),
                                    end, begin});
            // 存在就诊记录
            if (pvMap != null && pvMap.get("pkPi") != null) {
                DataBaseHelper
                        .update("update PV_ENCOUNTER set pk_insu = ? where pk_pi= ? and del_flag='0'"
                                        + " and eu_pvtype in ('1','2') and create_time < to_date(?, 'yyyymmddhh24miss')  and"
                                        + " create_time > to_date(?, 'yyyymmddhh24miss')",
                                new Object[]{pkHp,
                                        pvMap.get("pkPi").toString(), end,
                                        begin});

            }

            PiMaster piMaster = null;
            if (insCode.equals("04")) // 灵璧离休医保
            {
                piMaster = DataBaseHelper
                        .queryForBean(
                                "select * from pi_master where del_flag ='0' and insur_no=?",
                                PiMaster.class, insurNo);
            } else if (insCode.equals("03")) {
                piMaster = DataBaseHelper.queryForBean(
                        "select * from pi_master where del_flag ='0' and id_no=?",
                        PiMaster.class, idNo);
                if (piMaster == null) {
                    piMaster = DataBaseHelper
                            .queryForBean(
                                    "select * from pi_master where del_flag ='0' and insur_no=?",
                                    PiMaster.class, insurNo);
                }
            } else {
                piMaster = DataBaseHelper.queryForBean(
                        "select * from pi_master where del_flag ='0' and id_no=?",
                        PiMaster.class, idNo);
            }
            logger.info("==========读医保卡更新患者【" + piMaster.getNamePi() + "】，住院号为:" + piMaster.getCodeIp() + "=========");
            return piMaster;
        } else {
            return null;
        }


    }

}
