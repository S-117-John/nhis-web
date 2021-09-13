package com.zebone.nhis.pi.fix.service;

import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.*;
import com.zebone.nhis.common.module.pi.acc.PiCardIss;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.labor.pub.dao.PiLaborPubMapper;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pi.fix.dao.PibaseMapper;
import com.zebone.nhis.pi.fix.vo.OldSysPi;
import com.zebone.nhis.pi.fix.vo.PagePiListVo;
import com.zebone.nhis.pi.fix.vo.PageQryPiParam;
import com.zebone.nhis.pi.fix.vo.PiParamsDto;
import com.zebone.nhis.pi.pub.dao.PiPubMapper;
import com.zebone.nhis.pi.pub.vo.*;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 患者基本信息（包含患者信息：基本、地址、家庭关系、保险计划）
 *
 * @author wangpeng
 * @date 2016年9月9日
 */
@Service
public class PibaseService {
    /**
     * 患者基本信息mapper
     */
    @Resource
    private PibaseMapper pibaseMapper;

    @Resource
    private PiPubMapper piPubMapper;

    @Resource
    private PiLaborPubMapper piLaborPubMapper;

    @Autowired
    private OpcgPubHelperService opcgPubHelperService;

    /**
     * 根据字段查询患者信息
     *
     * @param
     * @return PiMasterVo
     * @throws
     * @author wangpeng
     * @date 2016年9月18日
     */
    public PiMasterVo getPiMaster(String param, IUser user) {
        CommonParam cParam = JsonUtil.readValue(param, CommonParam.class);
        String fieldName = cParam.getFieldName();
        String fieldValue = cParam.getFieldValue();
        String photoFlag = cParam.getPhotoFlag(); // 为1时需要照片
        PiMasterVo master = null;
        StringBuffer sqlBuf = new StringBuffer("select ");
        if ("1".equals(photoFlag)) { // 需要照片
            sqlBuf.append(" m.* " + " from pi_master m ");
        } else {
            sqlBuf.append(" m.pk_pi,m.pk_org,m.code_pi,m.code_op,m.code_ip,m.barcode,m.pk_picate,m.name_pi,m.dt_idtype,m.id_no,m.hic_no,m.insur_no,"
                    + "m.mpi,m.flag_ehr,m.dt_sex,m.birth_date,m.place_birth,m.dt_marry,m.dt_occu,m.dt_edu,m.dt_country,m.dt_nation,m.tel_no,m.mobile,"
                    + "m.wechat_no,m.email,m.name_rel,m.tel_rel,m.dt_ralation,m.addr_rel,m.dt_blood_abo,m.dt_blood_rh,m.creator,m.create_time,m.modifier,m.del_flag,m.ts,"
                    + "m.addrcode_birth,m.addr_birth,m.addrcode_origin,m.addr_origin,"
                    + "m.addrcode_regi ,m.addr_regi ,m.addr_regi_dt,m.postcode_regi,"
                    + "m.addrcode_cur ,m.addr_cur ,m.addr_cur_dt,m.postcode_cur ,"
                    + "m.unit_work,m.tel_work,m.address,m.postcode_work,GETPVAGE(m.BIRTH_DATE, null) as age_format"
                    + " from pi_master m ");
        }
        if (StringUtils.isNotBlank(fieldName) && !StringUtils.equals("card_no", fieldName)) {
            sqlBuf.append(" where m.del_flag = '0' and m.").append(fieldName).append(" = ?");
        } else if ("card_no".equals(fieldName)) { // 查状态为：0-在用,
            sqlBuf.append(" inner join pi_card c on c.del_flag = '0' and c.pk_pi = m.pk_pi and c.eu_status = '0' and c.card_no = ? "
                    + " where m.del_flag = '0'");
        } else if ("hic_no".equals(fieldName)) {
            sqlBuf.append(" where m.del_flag = '0' and m.").append(fieldName).append(" = ?");
        } else {
            throw new BusException("参数错误！");
        }
        List<PiMasterVo> masterList = DataBaseHelper.queryForList(sqlBuf.toString(), PiMasterVo.class,
                new Object[]{fieldValue});
        if (masterList != null && masterList.size() > 1) {
            throw new BusException("根据" + fieldName + "检索患者信息时，获取到多条患者记录，请核对数据是否正确！");
        }
        master = masterList.isEmpty() ? null : masterList.get(0);
        return master;
    }

    /**
     * 根据字段查询患者信息 + 地址全部名称
     *
     * @param
     * @return PiMaster
     * @throws
     * @author wangjie
     * @date 2018年4月23日
     */
    public PiMasterAndAddr getPiMasterAndAddr(String param, IUser user) {
        CommonParam cParam = JsonUtil.readValue(param, CommonParam.class);
        String fieldName = cParam.getFieldName();
        String fieldValue = cParam.getFieldValue();
        String photoFlag = cParam.getPhotoFlag(); // 为1时需要照片
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("photoFlag", photoFlag);
        if (CommonUtils.isEmptyString(fieldValue)) {
            throw new BusException("入参【fieldValue】为空！");
        }
        if ("pk_pi".equals(fieldName)) {
            map.put("pk_pi", fieldValue);
        } else if ("code_pi".equals(fieldName)) {
            map.put("code_pi", fieldValue);
        } else if ("code_ip".equals(fieldName)) {
            map.put("code_ip", fieldValue);
        } else if ("id_no".equals(fieldName)) {
            map.put("id_no", fieldValue);
        } else if ("insur_no".equals(fieldName)) {
            map.put("insur_no", fieldValue);
        } else if ("card_no".equals(fieldName)) { // 查状态为：0-在用,
            map.put("card_no", fieldValue);
        } else {
            throw new BusException("参数错误！");
        }
        List<PiMasterAndAddr> piList = pibaseMapper.getPiMasterAndAddr(map);
        PiMasterAndAddr master = null;
        if (piList != null && piList.size() > 0)
            master = piList.get(0);
        return master;
    }

    /**
     * 精确查询患者信息<br>
     *
     * <pre>
     * 1.从患者基本表pi_master中查询基本信息;
     * 2.从就诊记录表pv_encounter查询就诊信息,查询就诊状态为0、1、3的记录;
     * 3.从过敏史表pi_allergic查询过敏信息，多笔过敏信息之间以逗号分隔;
     * 4.从临床综合诊断表pv_diag中获取诊断名称，多个诊断之间使用逗号分隔;
     * 5.从医保计划表pi_insurance查询医保名称;
     * 6.pi_allergic、pv_diag表使用pk_pv字段与pv_encounter表关联;
     * 7.查询字段包括：患者编码、就诊卡号、就诊主键、证件号码、医保卡号、就诊号码、当前床位、就诊类型，就诊状态
     * </pre>
     *
     * @param param
     * @return user
     * @throws
     * @author wangpeng
     * @date 2016年9月10日
     */
    public PibaseVo getPibaseVo(String param, IUser user) {
        CommonParam cparam = JsonUtil.readValue(param, CommonParam.class);
        String fieldName = cparam.getFieldName();
        String fieldValue = cparam.getFieldValue();
        PibaseVo vo = new PibaseVo();
        PiMaster master = new PiMaster();
        PvEncounter encounter = new PvEncounter();
        String[] valueSplit = null;
        // 参数在不同的表中，分开查表
        boolean flag = false; // 默认false 查患者基本表 pi_master
        String strName = "";
        //先判断
        if (CommonUtils.isNull(fieldValue)) {
            throw new BusException("查询患者信息，传入参数为空！");
        }
        if ("code_pi".equals(fieldName)) { // 患者编码
            strName = "患者编码";
            master.setCodePi(fieldValue);
        } else if ("id_no".equals(fieldName)) {// 证件号码
            strName = "证件号码";
            master.setIdNo(fieldValue);
        } else if ("insur_no".equals(fieldName)) {// 医保卡号
            strName = "医保号码";
            master.setInsurNo(fieldValue);
        } else if ("code_pv".equals(fieldName)) { // 就诊号码
            strName = "就诊号码";
            encounter.setCodePv(fieldValue);
            flag = true;
        } else if ("code_ip".equals(fieldName)) {// 住院号
            strName = "住院号";
            master.setCodeIp(fieldValue);
        } else if ("code_op".equals(fieldName)) {
            strName = "门诊号";
            master.setCodeOp(fieldValue);
        } else if ("hic_no".equals(fieldName)) {
            strName = "健康卡号";
            master.setHicNo(fieldValue);
        } else if ("pk_pv".equals(fieldName)) { // 就诊主键
            strName = "就诊主键";
            valueSplit = fieldValue != null ? fieldValue.split(",")
                    : new String[]{"null"};
            encounter.setPkPv(valueSplit[0]);
            flag = true;
        } else if ("bed_no".equals(fieldName)) { // 当前床位
            strName = "当前床位";  //过滤床位需要根据当前科室查询
            encounter.setBedNo(fieldValue);
            encounter.setPkDept(cparam.getPkDept());
            flag = true;
        } else if ("card_no".equals(fieldName)) { // 就诊卡号
            strName = "就诊卡号";
            PiCard card = piPubMapper.getPiCardByCardNo(fieldValue);
            if (card != null) {
                master.setPkPi(card.getPkPi());
            } else {
                throw new BusException("未获取到【" + strName + " : " + fieldValue
                        + "】相关患者卡信息！");
            }
        } else {
            throw new BusException("参数错误！");
        }
        if ("1".equals(cparam.getFlagStatus())) {
            String[] euStatuss = cparam.getEuStatuss();
            if (euStatuss == null && !"pk_pv".equals(fieldName)) {// 处理门诊医生站已完成就诊在有效期内可以继续接诊情况
                euStatuss = new String[]{"0", "1", "3"};
            }
            if (valueSplit != null && valueSplit.length == 2)
                euStatuss = new String[]{"0", "1", "2", "3"};
            encounter.setEuStatuss(euStatuss);
        }
        if (chkIsOpOrIp(cparam.getEuPvtypes())) {
            encounter.setDateEnd(new Date());// 设置门诊查询时，添加有效就诊日期过来
            encounter.setPkEmpReg(UserContext.getUser().getPkEmp());// 添加门诊就诊资源为
            // 当前操作人
            encounter.setPkWg(UserContext.getUser().getPkDept());// 添加门诊就诊资源为
            // 当前科室
        }
        if (flag) {
            List<PvEncounter> list = piPubMapper.getPvEncounterList(encounter);
            if (CollectionUtils.isNotEmpty(list)) {
                encounter = list.get(0);
                master.setPkPi(encounter.getPkPi());
                List<PiMaster> listP = piPubMapper.getPiMaster(master);
                if (CollectionUtils.isNotEmpty(listP)) {
                    master = listP.get(0);
                }
            } else {
                throw new BusException("该患者【" + strName + " : " + fieldValue
                        + "】无就诊记录或已被取消接诊，如需继续请直接录入ID号接诊！+");
            }
        } else {
            List<PiMaster> listP = piPubMapper.getPiMaster(master);
            if (CollectionUtils.isNotEmpty(listP)) {
                master = listP.get(0);
                if (master != null) {
                    encounter.setPkPi(master.getPkPi());
                    List<PvEncounter> list = piPubMapper.getPvEncounterList(encounter);
                    if (CollectionUtils.isNotEmpty(list)) {
                        if (list.size() > 1 && "1".equals(cparam.getIsBackMorePv())) return null;
                        if(cparam.getPkDept() != null && !"".equals(cparam.getPkDept()))
                        {
                        	int nPatchCount = 0;
                        	Date d = new Date();
                        	StringBuffer sb = new StringBuffer();
                        	for(PvEncounter pvEncounter : list)
                        	{
                        		Date dateEnd = pvEncounter.getEuPvtype().equals(("2"))?ApplicationUtils.getPvDateEndEr(pvEncounter.getDateBegin()):ApplicationUtils.getPvDateEnd(pvEncounter.getDateBegin());//人民医院急诊调用，不判断有效日期
                                if(cparam.getPkDept().equals(pvEncounter.getPkDept()) && !d.after(dateEnd))
                        		{
                        			encounter = pvEncounter;
                        			sb.append(",");
                        			sb.append(pvEncounter.getPkPv());
                        			sb.append(",");
                        			nPatchCount++;
                        		}
                        	}
                        	if(nPatchCount == 0)
                        	{
                        		return null;
                        	}
                        	else if(nPatchCount > 1)
                        	{
                        		vo.setPopUpWindowsSpecialFlag("1");
                        		vo.setMatchPkPvs(sb.toString());
                        	}
                        }
                        else
                        {
                            encounter = list.get(0);                        	
                        }
                    } else {
                        throw new BusException("该患者【" + strName + " : "
                                + fieldValue + "】无就诊记录或已被取消接诊，如需继续请直接录入ID号接诊！");
                    }
                } else {
                    throw new BusException("未获取到【" + strName + " : "
                            + fieldValue + "】的患者信息！");
                }
            } else {
                throw new BusException("未获取到【" + strName + " : " + fieldValue
                        + "】的患者信息！");
            }
        }
        vo.setPkPi(master.getPkPi());
        vo.setPkPv(encounter.getPkPv());
        vo.setCodePv(encounter.getCodePv());
        vo.setCodePi(master.getCodePi());
        vo.setCodeIp(master.getCodeIp());
        vo.setCodeOp(master.getCodeOp());
        vo.setNamePi(master.getNamePi());
        vo.setNameRel(master.getNameRel());//联系人
        vo.setDtSex(master.getDtSex());
        vo.setBirthDate(master.getBirthDate());
        vo.setDtIdtype(master.getDtIdtype());
        vo.setIdNo(master.getIdNo());
        vo.setMobile(master.getMobile());
        vo.setInsurNo(master.getInsurNo());
        vo.setNamePicate(master.getNamePicate());
        vo.setBedNo(encounter.getBedNo());
        vo.setPkDept(encounter.getPkDept());
        vo.setPkDeptNs(encounter.getPkDeptNs());
        vo.setEuStatus(encounter.getEuStatus());
        vo.setDateBegin(encounter.getDateBegin());
        vo.setDateEnd(encounter.getDateEnd());
        vo.setDateClinic(encounter.getDateClinic());
        vo.setEuPvtype(encounter.getEuPvtype());
        vo.setEuPvmode(encounter.getEuPvmode());//设置就诊模式
        vo.setEmpName(encounter.getEmpName());
        vo.setPkInsu(encounter.getPkInsu());// 返回添加医保主键
        vo.setFlagSpec(encounter.getFlagSpec());//设置特诊标志
        vo.setPkEmpPhy(encounter.getPkEmpPhy());
        vo.setNameEmpPhy(encounter.getNameEmpPhy());//接诊医生
        vo.setDtBedtype(master.getDtBedtype());//床位类型
        vo.setAdtType(encounter.getAdtType());
        vo.setAgeFormat(ApplicationUtils.getAgeFormat(master.getBirthDate(), encounter.getDateBegin()));
        vo.setWeight(encounter.getWeight());
        vo.setSbp(encounter.getSbp());
        vo.setDbp(encounter.getDbp());
        vo.setDescEpid(encounter.getDescEpid());
        vo.setPkDeptArea(encounter.getPkDeptArea());
        vo.setDtJurisdiction(encounter.getDtJurisdiction());
        vo.setPkContraception(encounter.getPkContraception());
        vo.setUnitWork(master.getUnitWork());
        vo.setPkEmp(master.getPkEmp());
        // 过敏信息
        List<PiAllergic> allergicLsit = piPubMapper
                .getPiAllergicListByPkPi(master.getPkPi());
        if (CollectionUtils.isNotEmpty(allergicLsit)) {
            String strPiAl = allergicLsit.get(allergicLsit.size() - 1).getNameAl();
            vo.setNameAl(strPiAl);
        }
        // 诊断信息
        String nameDiag = "";
        // 只显示主诊断 2016-12-23 by wangpeng
        List<PvDiagVo> diagList = piPubMapper.getPvDiagListByPkPv(encounter.getPkPv());
        if (CollectionUtils.isNotEmpty(diagList)) {
            int i = 0;
            for (PvDiagVo diag : diagList) {
                if (i == 0) {
                    nameDiag += diag.getDiagText();
                } else {
                    nameDiag += "," + diag.getDiagText();
                    i++;
                }
            }
            vo.setPkDiag(diagList.get(0).getPkDiag()); // 主诊断只能有一个
            vo.setDiagCode(diagList.get(0).getCodeIcd());
        }
        vo.setNameDiag(nameDiag);
        // 保险
        Map<String, Object> mapInsu = DataBaseHelper.queryForMap(
                "select name from bd_hp "
                        + "where del_flag = '0' and pk_hp = ?",
                encounter.getPkInsu());
        if (mapInsu != null) {
            vo.setNameInsu(mapInsu.get("name").toString());
        } else {
            vo.setNameInsu("");
        }
        //全国医保
        return vo;
    }

    /**
     * 查询产房banner相关信息
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getPibaseLabVo(String param, IUser user) {
        CommonParam cparam = JsonUtil.readValue(param, CommonParam.class);
        if (cparam == null)
            throw new BusException("入参格式有误！");
        String fieldName = cparam.getFieldName();
        if (CommonUtils.isEmptyString(fieldName))
            throw new BusException("入参【fieldName】为空！");
        String fieldValue = cparam.getFieldValue();
//		if (CommonUtils.isEmptyString(fieldValue))
//			throw new BusException("入参【fieldValue】为空！");
        if (CommonUtils.isEmptyString(cparam.getFlagLabor()))
            throw new BusException("入参【flagLabor】为空！");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("flagLabor", cparam.getFlagLabor());// 产房 |病区
        paramMap.put("status", cparam.getStatus());// 当前产房就诊记录状态
        if ("code_pi".equals(fieldName)) {
            paramMap.put("codePi", fieldValue);// 患者编码
        } else if ("id_no".equals(fieldName)) {
            paramMap.put("idNo", fieldValue);// 证件号码
        } else if ("insur_no".equals(fieldName)) {
            paramMap.put("insurNo", fieldValue);// 医保卡号
        } else if ("code_pv".equals(fieldName)) {
            paramMap.put("codePv", fieldValue);// 就诊号码
        } else if ("code_ip".equals(fieldName)) {
            paramMap.put("codeIp", fieldValue);// 住院号
        } else if ("pk_pv".equals(fieldName)) {
            paramMap.put("pkPv", fieldValue);// 就诊主键
        } else if ("bed_no".equals(fieldName)) {
            if (!CommonUtils.isEmptyString(cparam.getFlagLabor())
                    && "1".equals(cparam.getFlagLabor()))
                paramMap.put("codeBed", fieldValue);// 当前床位 （产房codeBed）
            else
                paramMap.put("bedNo", fieldValue);// 当前床位 （病区bedNo）
        } else if ("card_no".equals(fieldName)) {
            PiCard card = piPubMapper.getPiCardByCardNo(fieldValue);
            if (card != null) {
                paramMap.put("pkPi", card.getPkPi());// 就诊卡号 =》 就诊主键
            } else {
                throw new BusException("未获取到【就诊卡号: " + fieldValue + "】相关患者卡信息！");
            }
        } else {
            throw new BusException("参数错误！");
        }
        // 查询孕妇相关信息 + 孕妇建档信息
        List<Map<String, Object>> pv_labors = piLaborPubMapper.queryPatiInfo(paramMap);
        if (pv_labors != null && pv_labors.size() > 0)
        {Optional<Map<String, Object>> lamp = pv_labors.stream().
                    filter(s -> "1".equals(s.getOrDefault("euStatus", "0"))).findAny();
        if (lamp.isPresent()){
            return lamp.get();
        } else {
            return pv_labors.get(0);
        }
        }
        return null;
    }

    /**
     * 查询患者列表
     *
     * @param param
     * @return user
     * @throws
     * @author wangpeng
     * @date 2016年9月18日
     */
    public List<PiMasterVo> getPiMasterList(String param, IUser user) {
        //2018-12-18 中山二院需求，新老系统并行使用期间保持患者住院号、患者编码同步
        List<OldSysPi> piList = (List<OldSysPi>) ExtSystemProcessUtils.processExtMethod("PiInfo", "piInfo", param);
        PageQryPiParam master = JsonUtil.readValue(param, PageQryPiParam.class);
        List<PiMasterVo> list = pibaseMapper.getPiMasterListNoPhoto(master);
        if (piList != null) {
            list.addAll(piList);
        }
        return list;
    }

    /**
     * 查询患者列表[分页]
     *
     * @param param
     * @return user
     * @throws
     * @author wangjie
     * @date 2018年5月4日
     */
    public PagePiListVo getPiMasterListPaging(String param, IUser user) {
        PagePiListVo piListAndTotal = new PagePiListVo();
        PageQryPiParam qryparam = JsonUtil.readValue(param,
                PageQryPiParam.class);
        if (qryparam == null)
            throw new BusException("查询参数有问题！");
        if(CommonUtils.isNull(qryparam.getCodePi())
                &&CommonUtils.isNull(qryparam.getCodeOp())
                &&CommonUtils.isNull(qryparam.getNamePi())
                &&CommonUtils.isNull(qryparam.getCodeOp())
                &&CommonUtils.isNull(qryparam.getCodeIp())
                &&CommonUtils.isNull(qryparam.getIdNo())
                &&CommonUtils.isNull(qryparam.getMobile())
                &&CommonUtils.isNull(qryparam.getInsurNo())
                &&CommonUtils.isNull(qryparam.getBirthBegin())
                &&CommonUtils.isNull(qryparam.getBirthEnd())
        ){
            throw new BusException("请置少输入一个有效查询条件，例如：姓名，身份证号等！");
        }

        int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
        int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
        // 如果要查年龄函数，先屏蔽xml中的
        boolean exeAgeFormat = qryparam.getAgeFormatFlag() == null || "1".equals(qryparam.getAgeFormatFlag());
        if (exeAgeFormat) {
            qryparam.setAgeFormatFlag("0");
        }
        // 分页操作
        MyBatisPage.startPage(pageIndex, pageSize);
        List<PiMasterVo> list = pibaseMapper.getPiMasterListNoPhoto(qryparam);
        // 分页完再查询年龄函数
        if (exeAgeFormat && list.size() > 0) {
            Set<String> pkPiSet = list.parallelStream().map(PiMaster::getPkPi).collect(Collectors.toSet());
            List<PiMasterVo> masterVos = DataBaseHelper.queryForList("select PK_PI,GETPVAGE(BIRTH_DATE,null) as age_format from PI_MASTER where PK_PI in (" + CommonUtils.convertSetToSqlInPart(pkPiSet, "pk_pi") + ") and BIRTH_DATE is not null"
                    , PiMasterVo.class);
            Map<String, String> piMasterVoMap = masterVos.parallelStream().collect(Collectors.toMap(PiMasterVo::getPkPi, PiMasterVo::getAgeFormat));
            list.stream().forEach(vo -> {
                vo.setAgeFormat(piMasterVoMap.get(vo.getPkPi()));
            });
        }
        Page<List<PiMasterVo>> page = MyBatisPage.getPage();
        piListAndTotal.setPatiList(list);
        piListAndTotal.setTotalCount(page.getTotalCount());
        return piListAndTotal;
    }

    /**
     * 查询患者就诊信息列表<br>
     *
     * <pre>
     * 该方法不需要返回过敏源、诊断信息
     * </pre>
     *
     * @param param
     * @return user
     * @throws
     * @author wangpeng
     * @date 2016年9月12日
     */
    public List<PibaseVo> getPibaseVoList(String param, IUser user) {
        PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
        String pkOrg = UserContext.getUser().getPkOrg();
        encounter.setPkOrg(pkOrg);
        // 当就诊类型为3时，使用date_begin字段匹配参数dateClinic；其余情况下，使用date_clinic字段匹配参数dateClinic
        if ("3".equals(encounter.getEuPvtype())) {
            if (encounter.getDateClinic() != null) {
                encounter.setDateBegin(encounter.getDateClinic());
                encounter.setDateClinic(null);
            }
        } else if ("1".equals(encounter.getEuPvtype())
                || "2".equals(encounter.getEuPvtype())
                || chkIsOpOrIp(encounter.getEuPvtypes())) {
            //yangxue 注释
            //encounter.setDateEnd(new Date());
        }
        List<PibaseVo> voList = pibaseMapper.getPibaseVoList(encounter);
        Set<String> setString = new HashSet<>();
        for(PibaseVo vo:voList){
            setString.add(vo.getPkPv());
        }
        //not_settle
        Map<String, Map<String, Object>> blMap = DataBaseHelper.queryListToMap("select dt.PK_PV key_,sum(dt.amount_pi) amount_pi from BL_OP_DT dt" +
                " where dt.flag_settle='0' and dt.flag_acc='0' and dt.pk_pv  in ("+ CommonUtils.convertSetToSqlInPart(setString, "pk_pv")+") group by dt.PK_PV ");
        for(PibaseVo vo:voList){
            vo.setNotSettle(MapUtils.getDouble(MapUtils.getMap(blMap,vo.getPkPv()),"amountPi",0.0));
        }
        return voList;
    }

    /**
     * 检验 就诊类型中是否包含门诊或急诊
     *
     * @param pvTypes
     * @return
     */
    private boolean chkIsOpOrIp(String[] pvTypes) {
        boolean isOp = false;
        if (null == pvTypes || pvTypes.length < 1)
            return isOp;
        else {
            for (String str : pvTypes) {
                if ("1".equals(str)) {
                    isOp = true;
                    break;
                } else if ("2".equals(str)) {
                    isOp = true;
                    break;
                } else
                    continue;
            }
        }
        return isOp;
    }

    /**
     * 获取患者编码
     *
     * @param param
     * @return user
     * @throws
     * @author wangpeng
     * @date 2016年9月14日
     */
    public String getPiMasterCode(String param, IUser user) {
        // 患者编码 code="001"
        return ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ);
    }

    /**
     * 保存患者分类
     *
     * @param param
     * @param user
     */
    public void savePiCateList(String param, IUser user) {
        List<PiCate> piCateList = JsonUtil.readValue(param,
                new TypeReference<List<PiCate>>() {
                });
        Map<String, String> codemap = new HashMap<String, String>();
        Map<String, String> namemap = new HashMap<String, String>();
        /** 校验---1.校验前台所传的list的每一条编码和名称的唯一性 */
        if (piCateList != null && piCateList.size() > 0) {
            int len = piCateList.size();
            for (int i = 0; i < len; i++) {
                String code = piCateList.get(i).getCode();
                String name = piCateList.get(i).getName();
                if (codemap.containsKey(code)) {
                    throw new BusException("患者分类编码重复！");
                }
                if (namemap.containsKey(name)) {
                    throw new BusException("患者分类名称重复！");
                }
                codemap.put(code, piCateList.get(i).getPkPicate());
                namemap.put(name, piCateList.get(i).getPkPicate());
            }
            /** 查询数据库中所有 */
            List<PiCate> allist = this.pibaseMapper.findAllCates();
            /** 校验---2.校验前台所传的list的每一条编码和名称是否和数据库重复 */
            for (PiCate dataCate : allist) {
                if (codemap.containsKey(dataCate.getCode())) {
                    String pkPicate = codemap.get(dataCate.getCode());
                    if (pkPicate == null) { // 新增情况
                        throw new BusException("患者分类编码在数据库中已存在！");
                    } else { // 修改情况
                        if (!dataCate.getPkPicate().equals(pkPicate)) {
                            throw new BusException("患者分类编码在数据库中已存在！");
                        }
                    }
                }
                if (namemap.containsKey(dataCate.getName())) {
                    String pkPicate = namemap.get(dataCate.getName());
                    if (pkPicate == null) { // 新增情况
                        throw new BusException("患者分类名称在数据库中已存在！");
                    } else { // 修改情况
                        if (!dataCate.getPkPicate().equals(pkPicate)) {
                            throw new BusException("患者分类名称在数据库中已存在！");
                        }
                    }
                }
            }
            /** 新增或更新到数据库 */
            for (PiCate cate : piCateList) {
                if (cate.getPkPicate() == null) {
                    DataBaseHelper.insertBean(cate);
                } else {
                    DataBaseHelper.updateBeanByPk(cate, false);
                }
            }
        }
    }

    /**
     * 删除患者分类
     *
     * @param param
     * @param user
     */
    public void delPiCate(String param, IUser user) {
        String pkPicate = JsonUtil.getFieldValue(param, "pkPicate");
        int count = DataBaseHelper
                .queryForScalar(
                        "select count(*) from pi_master where del_flag='0' and pk_picate = ?",
                        Integer.class, pkPicate);
        if (count == 0) {
            DataBaseHelper.update(
                    "update pi_cate set del_flag='1' where pk_picate = ?",
                    new Object[]{pkPicate});
        } else {
            throw new BusException("该患者分类被患者基本信息引用！");
        }
    }

    /**
     * 查询可用的卡登记记录
     */
    public List<PiCardIss> getPiCardIssList(String param, IUser user) {
        PiCardIss cardiss = JsonUtil.readValue(param, PiCardIss.class);
        User u = UserContext.getUser();
        String pkOrg = u.getPkOrg();
        cardiss.setPkOrg(pkOrg);
        List<PiCardIss> list = pibaseMapper.getPiCardIssList(cardiss);
        return list;
    }

    /*
     * public PiCardIss savePiCardIssList(String param, IUser user){ PiCardIss
     * cardiss = JsonUtil.readValue(param, PiCardIss.class);
     * if(cardiss.getPkCardiss() == null){ DataBaseHelper.insertBean(cardiss);
     * }else{ DataBaseHelper.updateBeanByPk(cardiss, false); } return cardiss; }
     */
    /*
     * public void savePiCardIssList(String param , IUser user){ List<PiCardIss>
     * cardiss = JsonUtil.readValue(param, new TypeReference<List<PiCardIss>>()
     * {}); if(cardiss != null && cardiss.size() != 0){ //先全删再恢复的方式（软删除） String
     * pkitem = cardiss.get(0).getPkCardiss(); DataBaseHelper.update(
     * "update pi_card_iss set del_flag = '1' where pk_cardiss = ?",new
     * Object[]{pkitem}); for(PiCardIss pv : cardiss){ if(pv.getPkCardiss() !=
     * null){ pv.setDelFlag("0");//恢复 DataBaseHelper.updateBeanByPk(pv, false);
     * }else{ DataBaseHelper.insertBean(pv); } } }else{ String pkitem =
     * cardiss.get(0).getPkCardiss(); DataBaseHelper.update(
     * "update pi_card_iss set del_flag = '1' where pk_cardiss = ?",new
     * Object[]{pkitem}); } }
     */

    /**
     * 保存卡记录
     */
    /*
     * public PiCardIss savePiCardIssList(String param, IUser user) {
     * List<PiCardIss> cardiss = JsonUtil.readValue(param,new
     * TypeReference<List<PiCardIss>>() {}); List<PiCardIss> list =
     * pibaseMapper.getPiCardIssLists();
     *
     * Long begin = cardiss.get(0).getBeginNo(); Long endno =
     * cardiss.get(0).getEndNo(); boolean flag = false; // 判断卡号是否在数据库中有重复 for
     * (int x = 0; x < cardiss.size(); x++) {
     *
     * for (int i = 0; i < list.size(); i++) { long max =
     * cardiss.get(x).getBeginNo() - list.get(i).getBeginNo(); long min =
     * cardiss.get(x).getEndNo() - list.get(i).getEndNo(); if (max > 0) { max =
     * cardiss.get(x).getBeginNo(); } else { max = list.get(i).getBeginNo(); }
     * if (min < 0) { min = cardiss.get(x).getEndNo(); } else { min =
     * list.get(i).getEndNo(); } if (min - max >= 0) { flag = true; break; } }
     * if (flag) break; } if (flag) { throw new BusException("卡号已被占用，请重新选择"); }
     * else { if (cardiss != null && cardiss.size() != 0) { // 先全删再恢复的方式（软删除）
     * String pkOrg = cardiss.get(0).getPkOrg(); DataBaseHelper
     * .update("update pi_card_iss set del_flag = '1' where pk_cardiss = ?", new
     * Object[] { pkOrg }); for (PiCardIss pv : cardiss) { if (pv.getPkCardiss()
     * != null) { pv.setDelFlag("0");// 恢复 DataBaseHelper.updateBeanByPk(pv,
     * false); } else { DataBaseHelper.insertBean(pv); } } } else { // String
     * pkitem = cardiss.get(0).getPkCardiss(); // DataBaseHelper.update(
     * "update pi_card_iss set del_flag = '1' where pk_cardiss = ?",new //
     * Object[]{pkitem}); } } return null; }
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public PiCardIss savePiCardIssList(String param, IUser user) {
        User iuser = (User) user;
        List<PiCardIss> cardiss = JsonUtil.readValue(param,
                new TypeReference<List<PiCardIss>>() {
                });
        List<PiCardIss> list = pibaseMapper.getPiCardIssLists(null);
        List<PiCardIss> pList = null;
        int num = 0;
        for (PiCardIss piCardIss : cardiss) {
            if (piCardIss.getPkCardiss() == null) {
                num = num + 1;
            }
        }
        boolean flag = false;
        if (num == 0) {
            for (PiCardIss piCardIss : cardiss) {
                pList = new ArrayList<>();
                list = pibaseMapper.getPiCardIssLists(piCardIss.getPkCardiss());
                pList.add(piCardIss);
                flag = cardFlag(pList, list, flag);
                if (flag) {
                    throw new BusException("卡号已被占用，请重新选择");
                }
            }
        } else {
            flag = cardFlag(cardiss, list, flag);
            if (flag) {
                throw new BusException("卡号已被占用，请重新选择");
            }
        }
        // 判断卡号是否在数据库中有重复
        if (cardiss != null && cardiss.size() != 0) {
            // 先全删再恢复的方式（软删除）
            String pkOrg = iuser.getPkOrg();
            DataBaseHelper.update(
                    "update pi_card_iss set del_flag = '1' where pk_org = ?",
                    new Object[]{pkOrg});
            for (PiCardIss pv : cardiss) {
                if (pv.getPkCardiss() != null) {
                    pv.setDelFlag("0");// 恢复
                    DataBaseHelper.updateBeanByPk(pv, false);
                } else {
                    DataBaseHelper.insertBean(pv);
                    DataBaseHelper
                            .update("update pi_card_iss set del_flag = '0' where pk_org = ?",
                                    new Object[]{pkOrg});
                }
            }
        } else {
            // String pkitem = cardiss.get(0).getPkCardiss();
            // DataBaseHelper.update("update pi_card_iss set del_flag = '1' where pk_cardiss = ?",new
            // Object[]{pkitem});
        }
        return null;
    }

    private boolean cardFlag(List<PiCardIss> cardiss, List<PiCardIss> list,
                             boolean flag) {
        Long begin = cardiss.get(0).getBeginNo();
        Long endno = cardiss.get(0).getEndNo();
        for (PiCardIss beno : list) {
            Long beginNotmp = beno.getBeginNo();
            Long endNotmp = beno.getEndNo();
            if (!(endno < beginNotmp || begin > endNotmp)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除卡领用记录
     *
     * @param param
     * @param user
     */

    public void deletePiCardIss(String param, IUser user) {
        PiCardIss ca = JsonUtil.readValue(param, PiCardIss.class);

        /*
         * DataBaseHelper.execute("delete from pi_card_iss where pk_cardiss = ?",
         * new Object[] { ca.getPkCardiss() });
         */
        DataBaseHelper
                .update("update pi_card_iss set eu_status = 9,flag_active=0 where pk_cardiss = ?",
                        new Object[]{ca.getPkCardiss()});
    }

    /*
     * public void backPiCardIss(String param, IUser user){
     *
     * String pkpiCard=JSON.parseObject(param).getString("pkPiCard"); String
     * sqlForcard="select * from pi_card_iss where pk_cardiss=?"; PiCardIss
     * pia=DataBaseHelper.queryForBean(sqlForcard, PiCardIss.class);
     * if(pia.getEuStatus
     * ()==null||"".equals(pia.getEuStatus()))pia.setEuStatus(EnumerateParameter
     * .ONE); if(EnumerateParameter.TWO.equals(pia.getEuStatus())){
     * DataBaseHelper
     * .update("update pi_card_iss set eu_status=9,flag_active=0 where pk_cardiss=?"
     * ); }else{ throw new BusException("无法查询"); } }
     */

    /**
     * 退卡记录
     *
     * @param param
     * @param user
     */
    public Map<String, Object> backPiCardIss(String param, IUser user) {
        String piCard = JsonUtil.getFieldValue(param, "piCard");
        Map<String, Object> temp = DataBaseHelper
                .queryForMapFj(
                        "select * from pi_card_iss where eu_status='2' and flag_active='0' and pk_cardiss=?",
                        piCard);
        return temp;
    }

    /**
     * 查询领卡历史记录
     *
     * @param param
     * @param user
     * @return
     */
    public List<PiCardIss> queryPiCardIssList(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        User u = UserContext.getUser();
        String pkOrg = u.getPkOrg();
        map.put("pkOrg", pkOrg);
        map.get(pkOrg);
        List<PiCardIss> rtnList = pibaseMapper.queryPiCardIssList(map);
        return rtnList;
    }

    /**
     * 修改患者分类
     *
     * @param param{pkPicate,codePv,flagOp,flagIp}
     * @param user
     */
    public void updatePvEncounter(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null)
            throw new BusException("未获取到修改患者分类所需参数！");
        //查询优惠信息
        PiCate cate = DataBaseHelper.queryForBean(
                "select * from pi_cate where pk_picate = ?",
                PiCate.class, new Object[]{paramMap.get("pkPicate")});
        int num = DataBaseHelper.update(
                " update pv_encounter set pk_picate = ?,flag_spec = ? where code_pv = ? ",
                new Object[]{paramMap.get("pkPicate"),
                        cate != null && "1".equals(cate.getFlagSpec()) ? "1" : "0",
                        paramMap.get("codePv")
                });
        if (num <= 0) {
            throw new BusException("修改失败");
        }
        //如果是门诊就诊标志，同时修改当前就诊记录下，所有未结算信息
        if ("1".equals(CommonUtils.getString(paramMap.get("flagOp")))) {
            paramMap.put("pkOrg", ((User) user).getPkOrg());
            opcgPubHelperService.updateOpCgNoSettleDtByPicate(paramMap);
        }
    }

    /**
     * 获取患者所有信息
     *
     * @param param
     * @param user
     * @return
     */
    public PiMasterParam getPiMasterParam(String param, IUser user) {
        CommonParam cParam = JsonUtil.readValue(param, CommonParam.class);
        PiMasterParam piMasterParam = new PiMasterParam();
        String fieldValue = cParam.getFieldValue();
        String pkPi = fieldValue;
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        PiMaster piMaster = getPiMaster(param, user);
        List<PiFamily> pList = DataBaseHelper.queryForList(
                "select * from pi_family where del_flag = '0' and pk_pi = ? ",
                PiFamily.class, pkPi);
        List<PiAddress> pAddresses = DataBaseHelper
                .queryForList(
                        "select * from pi_address where del_flag = '0' and pk_pi = ? order by sort_no",
                        PiAddress.class, pkPi);
        List<PiInsurance> piInsurances = DataBaseHelper
                .queryForList(
                        "select * from pi_insurance where del_flag = '0' and pk_pi = ? order by sort_no",
                        PiInsurance.class, pkPi);
        List<PiCard> pCards = DataBaseHelper
                .queryForList(
                        "select * from pi_card where	pk_pi = ? and (	del_flag = '0'	or del_flag is null) and (	date_begin < ? 	and date_end > ?	 or date_begin is null	and date_end is null)",
                        PiCard.class, pkPi, date, date);
        piMasterParam.setMaster(piMaster);
        piMasterParam.setFamilyList(pList);
        piMasterParam.setAddressList(pAddresses);
        piMasterParam.setCardList(pCards);
        piMasterParam.setInsuranceList(piInsurances);
        return piMasterParam;
    }
    
    public PibaseVo getPibaseVoEMR(String param, IUser user) {
        CommonParam cparam = JsonUtil.readValue(param, CommonParam.class);
        String fieldName = cparam.getFieldName();
        String fieldValue = cparam.getFieldValue();
        PibaseVo vo = new PibaseVo();
        PiMaster master = new PiMaster();
        PvEncounter encounter = new PvEncounter();
        String[] valueSplit = null;
        // 参数在不同的表中，分开查表
        boolean flag = false; // 默认false 查患者基本表 pi_master
        String strName = "";
        //先判断
        if (CommonUtils.isNull(fieldValue)) {
            throw new BusException("查询患者信息，传入参数为空！");
        }
        if ("code_pi".equals(fieldName)) { // 患者编码
            strName = "患者编码";
            master.setCodePi(fieldValue);
        } else if ("id_no".equals(fieldName)) {// 证件号码
            strName = "证件号码";
            master.setIdNo(fieldValue);
        } else if ("insur_no".equals(fieldName)) {// 医保卡号
            strName = "医保号码";
            master.setInsurNo(fieldValue);
        } else if ("code_pv".equals(fieldName)) { // 就诊号码
            strName = "就诊号码";
            encounter.setCodePv(fieldValue);
            flag = true;
        } else if ("code_ip".equals(fieldName)) {// 住院号
            strName = "住院号";
            master.setCodeIp(fieldValue);
        } else if ("code_op".equals(fieldName)) {
            strName = "门诊号";
            master.setCodeOp(fieldValue);
        } else if ("hic_no".equals(fieldName)) {
            strName = "健康卡号";
            master.setHicNo(fieldValue);
        } else if ("pk_pv".equals(fieldName)) { // 就诊主键
            strName = "就诊主键";
            valueSplit = fieldValue != null ? fieldValue.split(",")
                    : new String[]{"null"};
            encounter.setPkPv(valueSplit[0]);
            flag = true;
        } else if ("bed_no".equals(fieldName)) { // 当前床位
            strName = "当前床位";  //过滤床位需要根据当前科室查询
            encounter.setBedNo(fieldValue);
            encounter.setPkDept(cparam.getPkDept());
            flag = true;
        } else if ("card_no".equals(fieldName)) { // 就诊卡号
            strName = "就诊卡号";
            PiCard card = piPubMapper.getPiCardByCardNo(fieldValue);
            if (card != null) {
                master.setPkPi(card.getPkPi());
            } else {
                throw new BusException("未获取到【" + strName + " : " + fieldValue
                        + "】相关患者卡信息！");
            }
        } else {
            throw new BusException("参数错误！");
        }
            List<PiMaster> listP = piPubMapper.getPiMaster(master);
            if (CollectionUtils.isNotEmpty(listP)) {
                master = listP.get(0);
                
            } else {
                throw new BusException("未获取到【" + strName + " : " + fieldValue
                        + "】的患者信息！");
            }
        
        vo.setPkPi(master.getPkPi());
        vo.setPkPv(encounter.getPkPv());
        vo.setCodePv(encounter.getCodePv());
        vo.setCodePi(master.getCodePi());
        vo.setCodeIp(master.getCodeIp());
        vo.setCodeOp(master.getCodeOp());
        vo.setNamePi(master.getNamePi());
        vo.setNameRel(master.getNameRel());//联系人
        vo.setDtSex(master.getDtSex());
        vo.setBirthDate(master.getBirthDate());
        vo.setDtIdtype(master.getDtIdtype());
        vo.setIdNo(master.getIdNo());
        vo.setMobile(master.getMobile());
        vo.setInsurNo(master.getInsurNo());
        vo.setNamePicate(master.getNamePicate());
        vo.setBedNo(encounter.getBedNo());
        vo.setPkDept(encounter.getPkDept());
        vo.setPkDeptNs(encounter.getPkDeptNs());
        vo.setEuStatus(encounter.getEuStatus());
        vo.setDateBegin(encounter.getDateBegin());
        vo.setDateEnd(encounter.getDateEnd());
        vo.setDateClinic(encounter.getDateClinic());
        vo.setEuPvtype(encounter.getEuPvtype());
        vo.setEuPvmode(encounter.getEuPvmode());//设置就诊模式
        vo.setEmpName(encounter.getEmpName());
        vo.setPkInsu(encounter.getPkInsu());// 返回添加医保主键
        vo.setFlagSpec(encounter.getFlagSpec());//设置特诊标志
        vo.setPkEmpPhy(encounter.getPkEmpPhy());
        vo.setNameEmpPhy(encounter.getNameEmpPhy());//接诊医生
        vo.setDtBedtype(master.getDtBedtype());//床位类型
        vo.setAdtType(encounter.getAdtType());
        vo.setAgeFormat(ApplicationUtils.getAgeFormat(master.getBirthDate(), encounter.getDateBegin()));
        vo.setWeight(encounter.getWeight());
        vo.setDescEpid(encounter.getDescEpid());
        vo.setPkDeptArea(encounter.getPkDeptArea());
        vo.setDtJurisdiction(encounter.getDtJurisdiction());
        vo.setPkContraception(encounter.getPkContraception());
        vo.setUnitWork(master.getUnitWork());
        // 过敏信息
        List<PiAllergic> allergicLsit = piPubMapper
                .getPiAllergicListByPkPi(master.getPkPi());
        if (CollectionUtils.isNotEmpty(allergicLsit)) {
            String strPiAl = allergicLsit.get(allergicLsit.size() - 1).getNameAl();
            vo.setNameAl(strPiAl);
        }
        // 诊断信息
        String nameDiag = "";
        // 只显示主诊断 2016-12-23 by wangpeng
        List<PvDiagVo> diagList = piPubMapper.getPvDiagListByPkPv(encounter.getPkPv());
        if (CollectionUtils.isNotEmpty(diagList)) {
            int i = 0;
            for (PvDiagVo diag : diagList) {
                if (i == 0) {
                    nameDiag += diag.getDiagText();
                } else {
                    nameDiag += "," + diag.getDiagText();
                    i++;
                }
            }
            vo.setPkDiag(diagList.get(0).getPkDiag()); // 主诊断只能有一个
            vo.setDiagCode(diagList.get(0).getCodeIcd());
        }
        vo.setNameDiag(nameDiag);
        // 保险
        Map<String, Object> mapInsu = DataBaseHelper.queryForMap(
                "select name from bd_hp "
                        + "where del_flag = '0' and pk_hp = ?",
                encounter.getPkInsu());
        if (mapInsu != null) {
            vo.setNameInsu(mapInsu.get("name").toString());
        } else {
            vo.setNameInsu("");
        }
        //全国医保
        return vo;
    }


    /**
     * 根据条件查询患者信息
     * @param param
     * @param user
     * @return
     */
    public List<PiMasterVo> queryPiMasterList(String param, IUser user) {
        PiParamsDto params = JsonUtil.readValue(param, PiParamsDto.class);
        List<PiMasterVo> list = pibaseMapper.queryPiMasterList(params);
        if (CollectionUtils.isEmpty(list) || list.size()>0 ){
            // list中包含参数中的任意两个即可返回
            String teCardNo = params.getTeCardNo();
            String teMobile = params.getTeMobile();
            String teNamePi = params.getTeNamePi();
            List<PiMasterVo> collect1 =new ArrayList<>();
            List<PiMasterVo> collect2 =new ArrayList<>();
            List<PiMasterVo> collect3 =new ArrayList<>();
            List<PiMasterVo> collect4 =new ArrayList<>();
            List<PiMasterVo> collect5 =new ArrayList<>();
            if (StringUtils.isNotBlank(teNamePi)){
                collect1=  list.stream().filter(p ->StringUtils.isNotBlank(p.getNamePi())&& p.getNamePi().contains(teNamePi)).collect(Collectors.toList());
            }


            if (StringUtils.isNotBlank(teMobile)){
                if (CollectionUtils.isNotEmpty(collect1)){
                    collect3=   collect1.stream().filter(p ->StringUtils.isNotBlank(p.getMobile())&& p.getMobile().contains(teMobile)).collect(Collectors.toList());
                    collect4=   list.stream().filter(p ->StringUtils.isNotBlank(p.getMobile()) && p.getMobile().contains(teMobile)).collect(Collectors.toList());

                }else {
                    collect3=   list.stream().filter(p ->StringUtils.isNotBlank(p.getMobile()) && p.getMobile().contains(teMobile)).collect(Collectors.toList());

                }
            }


            if (StringUtils.isNotBlank(teCardNo)){
                if (CollectionUtils.isNotEmpty(collect1)){
                    collect2=   collect1.stream().filter(p ->StringUtils.isNotBlank(p.getIdNo()) && p.getIdNo().contains(teCardNo)).collect(Collectors.toList());
                    collect5=   list.stream().filter(p ->StringUtils.isNotBlank(p.getIdNo()) && p.getIdNo().contains(teCardNo)).collect(Collectors.toList());

                }else {
                    collect2=   list.stream().filter(p ->StringUtils.isNotBlank(p.getIdNo()) && p.getIdNo().contains(teCardNo)).collect(Collectors.toList());

                }
            }
            if (CollectionUtils.isNotEmpty(collect1)){
                // 名字重复了，取并集
                Set<PiMasterVo> result = new HashSet<>();
                result.addAll(collect2);
                result.addAll(collect3);
                // 实际上有名字，但是名字不同，身份证和电话相同
                collect4.retainAll(collect5);
                result.addAll(collect4);
                return new ArrayList<>(result);
            }else {
                // 名字没有一样的  取交集
                if (CollectionUtils.isEmpty(collect2)|| CollectionUtils.isEmpty(collect3))
                    return new ArrayList<>();
                collect2.retainAll(collect3);

                return collect2;
            }


        }
        // 没有重复的返回空，前端不做处理即可
        return new ArrayList<>();
    }
}
