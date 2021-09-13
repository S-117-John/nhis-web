package com.zebone.nhis.webservice.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptPv;
import com.zebone.nhis.common.module.sch.plan.SchPlan;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.webservice.dao.PvPubForWsMapper;
import com.zebone.nhis.webservice.dao.SchPubForWsMapper;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.vo.LbPiMasterRegVo;
import com.zebone.nhis.webservice.vo.LbSHRequestVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.web.support.ResponseJson;

/**
 * 就诊域webservcie专用公共服务
 * 
 * @author yangxue
 * 
 */
@Service
public class PvPubForWsService {
	@Resource
	private PvPubForWsMapper pvPubForWsMapper;
	@Resource
	private SchPubForWsMapper schPubForWsMapper;
	private User u = new User();

	/**
	 * 查询患者注册信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getPiMasterInfo(Map<String, Object> paramMap) {
		List<Map<String, Object>> piMasterList = pvPubForWsMapper
				.queryPiMaster(paramMap);
		Map<String, Object> piMaster = new HashMap<String, Object>();
		if (piMasterList.size() > 0)
			piMaster = piMasterList.get(0);
		return piMaster;
	}

	/**
	 * 查询患者分类信息
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getPiCateInfo() {
		return pvPubForWsMapper.getPiCateInfo();
	}

	/**
	 * 保存患者信息
	 * 
	 * @param paramMap
	 *            {namePi，idNo，mobile，dtSex}
	 * @return
	 */
	public Map<String, Object> savePiMaster(Map<String, Object> paramMap) {
		ApplicationUtils apputil = new ApplicationUtils();
		
		u.setPkEmp("extwebservice");
		u.setPkOrg(CommonUtils.getString(paramMap.get("pkOrg")));
		UserContext.setUser(u);
		ResponseJson rs = apputil.execService("SCH", "SchExtPubService",
				"savePiMaster", paramMap, u);
		return (Map<String, Object>) rs.getData();
	}

	/**
	 * 患者建档注册
	 * 
	 * @param paramMap 必填{pkOrg,dtIdtype,idNo,mobile,dtSex,birthDate}
	 * @return
	 */
	public Map<String, Object> piMasterRegister(Map<String, Object> paramMap,User user) {
		ApplicationUtils apputil = new ApplicationUtils();	
		String dtIdtype = CommonUtils.getPropValueStr(paramMap, "dtIdType");
		String idNo = CommonUtils.getPropValueStr(paramMap, "idNo");
    	Map<String, Object> result = new HashMap<>(16);
    	//验证患者是否已注册，只验证身份证
    	if("01".equals(dtIdtype)) {
    		PiMaster temp_pi = DataBaseHelper.queryForBean("select code_pi,name_pi,pk_pi from pi_master where id_no = ? and dt_idtype=? ",PiMaster.class, idNo,dtIdtype);
        	if(temp_pi != null){
        		//手机号
    			if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "mobile"))){
    				temp_pi.setHicNo(CommonUtils.getPropValueStr(paramMap, "mobile"));
    			}
    			//患者名称
    			if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "namePi"))){
    				temp_pi.setHicNo(CommonUtils.getPropValueStr(paramMap, "namePi"));
    			}
    			//地址
    			if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "dtSex"))){
    				temp_pi.setHicNo(CommonUtils.getPropValueStr(paramMap, "dtSex"));
    			}
    			//健康卡号
    			if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "hicNo"))){
    				temp_pi.setHicNo(CommonUtils.getPropValueStr(paramMap, "hicNo"));
    			}
    			//医保卡号
    			if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap, "insurNo"))){
    				temp_pi.setHicNo(CommonUtils.getPropValueStr(paramMap, "insurNo"));
    			}
    			temp_pi.setModifier(user.getPkEmp());// 修改人
    			temp_pi.setPkOrg(user.getPkOrg());
    			temp_pi.setDelFlag("0");
    			temp_pi.setTs(new Date());
    			temp_pi.setModifier(user.getPkEmp());
    			DataBaseHelper.updateBeanByPk(temp_pi, false);		
        		result.put("codePi", temp_pi.getCodePi());
        		result.put("namePi", temp_pi.getNamePi());
        		result.put("pkPi", temp_pi.getPkPi());
        		result.put("patientId", temp_pi.getPkPi());
        		return result;
        	}
    	}
		String codeInsu = CommonUtils.getPropValueStr(paramMap, "codeInsu");
    	List<PiInsurance> insuranceList = new ArrayList<>();
    	PiInsurance piInsurance = new PiInsurance();
    	String sql = " select pk_hp from bd_hp where  code = ? and del_flag = '0'";
    	if(codeInsu == null || codeInsu == ""){
    		codeInsu = "01";
    	}
    	Map<String, Object> hpMap = DataBaseHelper.queryForMap(sql, codeInsu);
    	if(CommonUtils.isNull(CommonUtils.getPropValueStr(hpMap, "pkHp"))){
    		hpMap = DataBaseHelper.queryForMap(sql, "01");
    	}
    	piInsurance.setPkHp(CommonUtils.getPropValueStr(hpMap, "pkHp"));
    	insuranceList.add(piInsurance);
    	paramMap.put("insuranceList", insuranceList);
    	PiMaster master = new PiMaster();
		master.setIdNo(idNo);
    	master.setDtIdtype(dtIdtype);
    	master.setNamePi(CommonUtils.getPropValueStr(paramMap, "namePi"));
    	master.setMobile(CommonUtils.getPropValueStr(paramMap, "mobile"));
    	master.setDtSex(CommonUtils.getPropValueStr(paramMap, "dtSex"));
    	master.setHicNo(CommonUtils.getPropValueStr(paramMap, "hicNo"));
    	master.setInsurNo(CommonUtils.getPropValueStr(paramMap, "insurNo"));//医保卡号
    	master.setBirthDate(DateUtils.strToDate(CommonUtils.getPropValueStr(paramMap, "birthDate"), "yyyy-MM-dd"));
		paramMap.put("master", master);
		ResponseJson rs = apputil.execService("PI", "PiPubService","savePiMasterParam", paramMap, user);
		master = (PiMaster) rs.getData();
		if(master == null){
			result.put("message", "建档失败，请联系his管理员!");
			result.put("result", "false");
			return result;
		}
		result.put("codePi", master.getCodePi());
		result.put("namePi", master.getNamePi());
		result.put("pkPi", master.getPkPi());
		result.put("patientId", master.getPkPi());
			
		return result;
	}
	
	/**
	 * 查询是否存在就诊卡
	 * 
	 * @param paramMap
	 * @return
	 */

	public List<Map<String, Object>> getPiCard(Map<String, Object> paramMap) {
		return pvPubForWsMapper.getPiCard(paramMap);
	}

	/**
	 * 查询患者当前就诊状态的住院就诊信息
	 * 
	 * @param paramMap
	 *            ：param pkPi必传 euStatus:就诊状态 codeIp:住院号pkDept:就诊科室
	 *            dateBegin:开始日期 dateEnd:截止日期
	 * @return
	 */
	public List<Map<String, Object>> getPvInfoByIp(Map<String, Object> paramMap) {
		String begin="";
		String end ="";
		if(paramMap.get("dateBegin")!=null && !"".equals(paramMap.get("dateBegin"))) begin = paramMap.get("dateBegin").toString();
		if(paramMap.get("dateEnd")!=null && !"".equals(paramMap.get("dateEnd"))) end = paramMap.get("dateEnd").toString();
        //时间处理
        if (!"".equals(begin)&& begin!=null &&!"".equals(end)&& end!=null){	
            String dateBegin = DateUtils.dateToStr("yyyyMMddHHmmss", DateUtils.getDateMorning(DateUtils.strToDate(begin), 0));
            String dateEnd =DateUtils.dateToStr("yyyyMMddHHmmss", DateUtils.getDateMorning(DateUtils.strToDate(end), 1));
            paramMap.put("dateBegin",dateBegin);
            paramMap.put("dateEnd",dateEnd);
        }	
		return pvPubForWsMapper.getPvInfoByIp(paramMap);
	}

	/**
	 * 查询当前患者就诊状态的门诊就诊信息
	 * 
	 * @param paramMap
	 *            ： parampkPi:患者唯一标识 codeOp:门诊号[可选] pkDept:就诊科室[可选]
	 * @return
	 */
	public List<Map<String, Object>> getPvInfoByOp(Map<String, Object> paramMap) {

		return pvPubForWsMapper.getPvInfoByOp(paramMap);
	}

	/**
	 * 挂号预结算，外部接口调用时必须添加参数校验
	 * 
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> preRegister(Map<String, Object> paramMap) {
		if (paramMap == null)
			return null;
		// 组装预结算参数
		List<Map<String, Object>> reglist = new ArrayList<Map<String, Object>>();
		// 医保信息
		Map<String, Object> insu = new HashMap<String, Object>();
		insu.put("sortNo", "0");
		insu.put("pkHp", paramMap.get("pkInsu"));
		insu.put("flagMaj", "1");
		List<Map<String, Object>> insuList = new ArrayList<Map<String, Object>>();
		insuList.add(insu);
		paramMap.put("insuList", insuList);
		paramMap.put("orderNo", "0");
		reglist.add(paramMap);
		// [{cardNo=, orderNo=0,
		// pkPi=11f7aebdeaa84eb9a63daf94e156011f,
		// pkPicate=58f82f7162c24140a00d5c2e3cb0d7d9,
		// pkSchsrv=1cac23a1d4cb417fa0e1ed42e41fdd95,
		// pkAppo=null,
		// pkInsu=6cc82cc6f52a43eaafa6a5464ba1118f,
		// insuList=[{sortNo=0, pkHp=6cc82cc6f52a43eaafa6a5464ba1118f,
		// nameHp=null, flagMaj=1}],
		// opDtList=[], ybzje=0.0, ybzf=0.0, xjzf=0.0}]

		// 调用挂号预结算，返回支付金额
		ApplicationUtils apputil = new ApplicationUtils();
		u.setPkEmp("extwebservice");
		u.setPkOrg(CommonUtils.getString(paramMap.get("pkOrg")));
		ResponseJson rs = apputil.execService("BL", "OpCgSettlementService",
				"countRegisteredAccountingSettlement", reglist, u);
		return (Map<String, Object>) rs.getData();
	}

	/**
	 * 保存挂号信息，外部接口调用时必须添加参数校验
	 * 
	 * @param paramMap
	 *            {depositList，insuList，pkDateSlot，pkInsu，pkPi，pkPicate，pkRes，
	 *            pkSch，pkSchsrv}
	 * @return
	 */
	public List<Map<String, Object>> saveRegister(Map<String, Object> paramMap) {
		if (paramMap == null)
			return null;
		// 组装结算参数
		List<Map<String, Object>> reglist = new ArrayList<Map<String, Object>>();
		// 医保信息
		Map<String, Object> insu = new HashMap<String, Object>();
		insu.put("sortNo", "0");
		insu.put("pkHp", paramMap.get("pkInsu"));
		insu.put("flagMaj", "1");
		List<Map<String, Object>> insuList = new ArrayList<Map<String, Object>>();
		insuList.add(insu);
		paramMap.put("insuList", insuList);
		paramMap.put("orderNo", "0");

		Map<String, Object> deposit = new HashMap<String, Object>();
		deposit.put("amount", paramMap.get("accountReceivable"));// 应收金额
		deposit.put("dtPaymode", paramMap.get("dtPaymode"));// 微信：7
		deposit.put("NamePaymode", paramMap.get("NamePaymode"));
		List<Map<String, Object>> depositList = new ArrayList<Map<String, Object>>();
		depositList.add(deposit);
		paramMap.put("depositList", depositList);// 增加支付方式
		reglist.add(paramMap);
		// 调用挂号结算
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson rs = apputil.execService("PV", "RegService",
				"savePvEncounterAndOp", reglist, u);
		return (List<Map<String, Object>>) rs.getData();
	}

	/**
	 * 退号预结算
	 * 
	 * @param paramMap
	 *            {pkPv,pkOrg}
	 * @return
	 */
	public Map<String, Object> preRtnRegister(Map<String, Object> paramMap) {
		if (paramMap == null)
			return null;
		u.setPkEmp("extwebservice");
		u.setNameEmp("extwebservice");
		u.setPkOrg(CommonUtils.getString(paramMap.get("pkOrg")));
		// 计算应退金额
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson rs = apputil.execService("BL", "OpCgSettlementService",
				"countRefoundRegisteredSettlement", paramMap, u);
		Map<String, Object> result = (Map<String, Object>) rs.getData();
		if (result == null)
			return null;
		BigDecimal zhzf = BigDecimal.ZERO;
		BigDecimal ytje = result.get("patientsPay") == null ? BigDecimal.ZERO
				: (BigDecimal) result.get("patientsPay");
		if (result.get("blDeposits") != null) {
			List<Map<String, Object>> bldepolist = (List<Map<String, Object>>) result
					.get("blDeposits");
			if (bldepolist.size() > 0) {
				for (Map<String, Object> dep : bldepolist) {
					if (dep != null
							&& "4".equals(CommonUtils.getString(dep
									.get("dtPaymode")))) {
						BigDecimal amount = dep == null ? BigDecimal.ZERO
								: (BigDecimal) dep.get("amount");
						zhzf = zhzf.add(amount);
					}
				}
			}
		}
		ytje = ytje.subtract(zhzf);
		if (ytje.compareTo(BigDecimal.ZERO) <= 0) {
			result.put("returnAmount", BigDecimal.ZERO);
		} else {
			result.put("returnAmount", ytje);
		}
		return result;
	}

	/**
	 * 保存退号结算信息
	 * 
	 * @param paramMap
	 *            {pkPv：就诊主键，blDeposits:第三方支付信息集合（内容同退号预结算的返回集合blDeposits）}
	 * @return
	 */
	public Map<String, Object> saveRtnRegister(Map<String, Object> paramMap) {
		if (paramMap == null)
			return null;
		if (paramMap.get("blDeposits") != null) {
			List<Map<String, Object>> extlist = (List<Map<String, Object>>) paramMap
					.get("blDeposits");
			if (extlist.size() > 0) {
				for (Map<String, Object> ext : extlist) {
					if (ext != null && ext.get("amount") != null) {
						ext.put("amount", ((BigDecimal) ext.get("amount"))
								.multiply(new BigDecimal(-1)));
					}
				}
			}
			paramMap.put("blExtPayBank", extlist);
		}

		// 调用退号接口
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson rs = apputil.execService("PV", "RegService", "cancelPv",
				paramMap, u);
		Map<String, Object> result = (Map<String, Object>) rs.getData();
		return result;
	}

	/**
	 * 保存预约登记信息(有事物部分)
	 * 
	 * @param regvo
	 * @param schSch
	 * @param ticket
	 * @param schplan
	 * @param u
	 * @return
	 */
	public Map<String, Object> saveApptSchRegInfo(Map<String, Object> paramMap) {
		ApplicationUtils apputil = new ApplicationUtils();
		u.setPkEmp("extwebservice");
		u.setPkOrg(CommonUtils.getString(paramMap.get("pkOrg")));
		ResponseJson rs = apputil.execService("SCH", "SchExtPubService",
				"savePiMaster", paramMap, u);
		return (Map<String, Object>) rs.getData();
	}

	public LbPiMasterRegVo saveApptSchRegInfos(LbPiMasterRegVo regvo, SchSch schSch,
			SchTicket ticket, SchPlan schplan, User u, boolean isGh) {
		SchAppt schAppt = new SchAppt();
		schAppt.setPkSchappt(NHISUUID.getKeyId());
		schAppt.setEuSchclass(regvo.getEuSchclass());
		schAppt.setPkSch(regvo.getPkSch());
		schAppt.setDateAppt(schSch.getDateWork());
		schAppt.setPkDateslot(schSch.getPkDateslot());
		schAppt.setPkSchres(schSch.getPkSchres());
		schAppt.setPkSchsrv(schSch.getPkSchsrv());
		schAppt.setOrderidExt(regvo.getOutsideOrderId());
		if ("1".equals(schplan.getFlagTicket())) {
			schAppt.setTicketNo(ticket.getTicketno());
		} else {
			schAppt.setTicketNo(String.valueOf(NumberUtils.toInt(
					schSch.getTicketNo(), 0) + 1));
		}
		if (ticket != null) {
			schAppt.setBeginTime(ticket.getBeginTime());
			schAppt.setEndTime(ticket.getEndTime());
		} else {
			schAppt.setBeginTime(schSch.getDateWork());
			schAppt.setEndTime(schSch.getDateWork());
		}
		schAppt.setPkPi(regvo.getPkPi());
		if (StringUtils.isEmpty(regvo.getOrderSource())) {
			String defAppType = MapUtils
					.getString(
							DataBaseHelper
									.queryForMap("select code from BD_DEFDOC where  DEL_FLAG = '0' and CODE_DEFDOCLIST = '020100' and DEL_FLAG='0' and FLAG_DEF='1' "),
							"code");
			schAppt.setDtApptype(defAppType);
		} else {
			schAppt.setDtApptype(regvo.getOrderSource());
		}
		schAppt.setPkDeptEx(schSch.getPkDept());
		schAppt.setPkOrgEx(schSch.getPkOrg());
		schAppt.setDateReg(new Date());
		schAppt.setPkDeptReg(u.getPkDept());
		schAppt.setPkEmpReg(u.getPkEmp());
		schAppt.setNameEmpReg(u.getNameEmp());
		schAppt.setEuStatus("0");// 0:登记,1:到达
		if (isGh) {
			schAppt.setFlagPay("1");
		} else {
			schAppt.setFlagPay("0");
		}
		schAppt.setFlagNotice("0");
		schAppt.setFlagCancel("0");
		schAppt.setFlagNoticeCanc("0");
		schAppt.setCode(ApplicationUtils.getCode("0101"));// 预约编码，不清楚,写主键//2019-07-19编码规则中预约单号为预约编码
		DataBaseHelper.insertBean(schAppt);

		// 无论资源是科室还是人员都要写
		SchResource schRes = DataBaseHelper
				.queryForBean(
						"select * from SCH_RESOURCE where del_flag = '0' and pk_schres = ? and eu_restype=1",
						SchResource.class, schSch.getPkSchres());
		SchApptPv schApptPv = new SchApptPv();
		schApptPv.setPkSchappt(schAppt.getPkSchappt());
		schApptPv.setEuApptmode("0");
		if (schRes != null) {
			schApptPv.setPkEmpPhy(schRes.getPkEmp());
			Map<String, Object> nameEmp = DataBaseHelper.queryForMap(
					"select name_emp from BD_OU_EMPLOYEE where pk_emp = ?",
					schRes.getPkEmp());
			schApptPv.setNameEmpPhy(nameEmp.get("nameEmp") == null ? ""
					: nameEmp.get("nameEmp").toString());
		}
		schApptPv.setFlagPv("0");
		DataBaseHelper.insertBean(schApptPv);

		if ("1".equals(schplan.getFlagTicket())) {
			schPubForWsMapper.updateSchCntUsed(regvo.getPkSch());
			//DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?",new Object[] { regvo.getPkSch() });
		}
		regvo.setPkAppt(schAppt.getPkSchappt());
		regvo.setApptCode(schAppt.getCode());
		regvo.setPkDeptunit(schSch.getPkDeptunit());
		regvo.setDateBegin(schAppt.getDateAppt());
		regvo.setDtApptype(schAppt.getDtApptype());
		return regvo;

	}


	/**
	 * 查询卡状态
	 */
	public PiCard getPiCard(String pkpi,String cardno) {
		String sql = "select * from Pi_card where pk_pi=? and Card_No=? and (del_flag = '0' or del_flag is null)";
		PiCard picard = DataBaseHelper.queryForBean(sql, PiCard.class, new Object[] { pkpi,cardno });
		return picard;
	}
	
	/**
	 * 查询患者当前就诊状态的住院就诊信息(床旁系统)
	 * @param paramMap
	 * @return
	 */
    public List<Map<String,Object>> getPvInfoByPv(Map<String,Object> paramMap){
    	return pvPubForWsMapper.getPvInfoByPv(paramMap);
    }
	
    /**
	 * 住院查询患者就诊信息(webservice)
	 * @param paramMap
	 * @return
	 */
    public List<Map<String,Object>> queryPiListInHosp(Map<String,Object> paramMap){
    	return pvPubForWsMapper.queryPiListInHosp(paramMap);
    }
    /**
	 * 查询患者信息输血
	 * @param param
	 * @return
	 */
    public List<Map<String,Object>> queryPiMasterByTmis(Map<String,Object> param){
    	return pvPubForWsMapper.queryPiMasterByTmis(param);
    }
    /**
	 * 查询科室血型分布（输血
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> queryMasterBlood(List<String> depts){
		return pvPubForWsMapper.queryMasterBlood(depts);
	}
	
	
	/**
	 * 口腔医院患者注册
	 * @param pi
	 * @param user
	 * @return
	 * @throws ParseException
	 */
	public PiMaster pskqSavePiMaster(PiMaster pi,User user){
		//健康卡是否需要读取外部接口
        String extHealth = ApplicationUtils.getSysparam("PI0019", false);
        if ("1".equals(extHealth)) {
            //如果不存在健康码进行健康码注册
            if (CommonUtils.isEmptyString(pi.getHicNo())) {
                Map<String, Object> ehealthMap = new HashMap<>(16);
                ehealthMap.put("piMaster", pi);
				//电子健康码注册
				Map<String, String> hicNo = (Map<String, String>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHC01", new Object[]{ehealthMap});
				if (hicNo != null) {
					pi.setHicNo(hicNo.get("hicNo"));
					pi.setNote(pi.getNote()+hicNo.get("note"));
				}
            }
        }
        // 校验是否已经注册
		if("01".equals(pi.getDtIdtype())){	
			PiMaster temp_pi = DataBaseHelper.queryForBean("select code_pi,name_pi,pk_pi from pi_master where id_no = ? and dt_idtype='01' ",PiMaster.class,pi.getIdNo());
			if (temp_pi != null) {
				temp_pi.setMobile(pi.getMobile());// 手机号
				temp_pi.setNamePi(pi.getNamePi());// 患者名称
				temp_pi.setModifier(user.getPkEmp());// 修改人
				temp_pi.setHicNo(pi.getHicNo());//电子健康码
				temp_pi.setPkOrg(user.getPkOrg());
				temp_pi.setCreator(user.getPkEmp());
				temp_pi.setCreateTime(new Date());
				temp_pi.setDelFlag("0");
				temp_pi.setTs(new Date());
				temp_pi.setModifier(user.getPkEmp());
				temp_pi.setMpi(pi.getMpi());
				DataBaseHelper.updateBeanByPk(temp_pi, false);
				return temp_pi;
			}
		}
		pi.setCodePi(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
		pi.setCodeOp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZBL));
		pi.setCodeIp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL));
		PiCate piCate = DataBaseHelper.queryForBean("SELECT * FROM pi_cate where flag_def='1'", PiCate.class);
		pi.setPkPicate(piCate.getPkPicate());// 患者分类，默认无优惠
		pi.setPkOrg(user.getPkOrg());
		pi.setCreator(user.getPkEmp());
		pi.setCreateTime(new Date());
		pi.setDelFlag("0");
		pi.setTs(new Date());
		pi.setModifier(user.getPkEmp());
		DataBaseHelper.insertBean(pi);

		//查询自费医保计划主键
		Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT * FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
		//保存患者医保计划 ---自助机默认自费
		PiInsurance insu = new PiInsurance();
		insu.setPkHp(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp"));
		insu.setPkPi(pi.getPkPi());
		insu.setSortNo(Long.valueOf("1"));

		insu.setDateBegin(new Date());//生效日期
		insu.setDateEnd(DateUtils.getTimeForOneYear(10));//失效日期

		insu.setFlagDef("1");//设置默认
		insu.setDelFlag("0");
		insu.setCreator(user.getPkEmp());//创建人
		insu.setTs(new Date());
		DataBaseHelper.insertBean(insu);

		//新增时，插入一条PiAcc记录
		PiAcc acc = new PiAcc();
		acc.setPkPi(pi.getPkPi());
		acc.setCodeAcc(pi.getCodeIp());
		acc.setAmtAcc(BigDecimal.ZERO);
		acc.setCreditAcc(BigDecimal.ZERO);
		acc.setEuStatus(PiConstant.ACC_EU_STATUS_1);
		DataBaseHelper.insertBean(acc);
		
		return pi;
	}
}
