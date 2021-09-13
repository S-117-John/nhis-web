package com.zebone.nhis.webservice.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.*;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.model.v24.segment.OBR;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdPayer;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlPi;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pi.acc.PiCardDetail;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvEr;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.webservice.dao.BdPubForWsMapper;
import com.zebone.nhis.webservice.dao.BlPubForWsMapper;
import com.zebone.nhis.webservice.dao.CnPubForWsMapper;
import com.zebone.nhis.webservice.dao.PvPubForWsMapper;
import com.zebone.nhis.webservice.dao.SchPubForWsMapper;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.vo.ItemPriceVo;
import com.zebone.nhis.webservice.vo.LbPiMasterRegVo;
import com.zebone.nhis.webservice.vo.LbSHRequestVo;
import com.zebone.nhis.webservice.vo.LbSHResponseVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *灵璧自助机据域webservcie专用公共服务
 *
 */
@Service
public class LbPubForWsService {
	@Resource
	private BlPubForWsService blPubForWsService;
	@Resource
	private InvPubForWsService invPubForWsService;
	@Autowired
	private SchPubForWsMapper schMapper;
	@Resource
	private SchPubForWsMapper schPubForWsMapper;
	@Resource
	private BdPubForWsMapper bdPubForWsMapper;
	@Autowired
	private PvPubForWsMapper pvPubForWsMapper;
	@Autowired
	private CnPubForWsMapper cnPubForWsMapper;
	@Resource
	private BlPubForWsMapper blPubForWsMapper;

	private Logger logger = LoggerFactory.getLogger("nhis.lbWebServiceLog");
	ApplicationUtils apputil = new ApplicationUtils();
	/**
	 * 灵璧患者信息校验更新/注册
	 * @throws ParseException
	 */
	public PiMaster lbSavePiMaster(PiMaster pi, LbSHRequestVo requ,User user) throws ParseException{
		if(null != requ){
			// 校验是否已经注册
			PiMaster temp_pi = DataBaseHelper.queryForBean("select code_pi,name_pi,pk_pi from pi_master where id_no = ? and dt_idtype='01' ",PiMaster.class, requ.getCardNo());
			if (temp_pi != null) {
				temp_pi.setMobile(requ.getPhoneno());// 手机号
				temp_pi.setAddrCur(requ.getAddress());// 地址
				temp_pi.setNamePi(requ.getName());// 患者名称
				temp_pi.setMobile(requ.getPhoneno());// 手机号
				temp_pi.setAddrCur(requ.getAddress());// 地址
				temp_pi.setHicNo(requ.getHicNo());//健康卡号
				temp_pi.setModifier(user.getPkEmp());// 修改人

				temp_pi.setPkOrg(user.getPkOrg());
				temp_pi.setCreator(user.getPkEmp());
				temp_pi.setCreateTime(new Date());
				temp_pi.setDelFlag("0");
				temp_pi.setTs(new Date());
				temp_pi.setModifier(user.getPkEmp());

				DataBaseHelper.updateBeanByPk(temp_pi, false);
				return temp_pi;
			}

			pi.setCodePi(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
			pi.setCodeOp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZBL));
			pi.setCodeIp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL));
			pi.setCodeEr(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_JZ));//急诊号

			pi.setPkPicate("24d7bf6ec4034d248d1d0db083bcf7d1");// 患者分类，默认无优惠
			pi.setNamePi(requ.getName());// 患者名称
			pi.setDtIdtype(requ.getCardType());// 证件号类型
			pi.setIdNo(requ.getCardNo());// 证件号
			if(!("男").equals(requ.getSex()) || !("女").equals(requ.getSex())){
				pi.setDtSex(requ.getSex());// 性别编码
			}
			pi.setBirthDate(DateUtils.parseDate(requ.getBirthday(),"yyyy-MM-dd"));// 出生日期
			if(("04").equals(requ.getNation())){
				pi.setDtNation("4");// 民族
			}
			pi.setMobile(requ.getPhoneno());// 手机号
			pi.setAddrCur(requ.getAddress());// 地址
			pi.setHicNo(requ.getHicNo());//健康卡号

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
			insu.setCreateTime(new Date());
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
		}else if(StringUtils.isNotBlank(pi.getIdNo())){
			// 校验是否已经注册
			PiMaster temp_pi = DataBaseHelper.queryForBean("select code_pi,name_pi,pk_pi from pi_master where id_no = ? and dt_idtype='01' ",PiMaster.class,pi.getIdNo());
			if (temp_pi != null) {
				temp_pi.setMobile(pi.getMobile());// 手机号
				temp_pi.setNamePi(pi.getNamePi());// 患者名称
				temp_pi.setModifier(user.getPkEmp());// 修改人
				temp_pi.setPkOrg(user.getPkOrg());
				temp_pi.setCreator(user.getPkEmp());
				temp_pi.setCreateTime(new Date());
				temp_pi.setDelFlag("0");
				temp_pi.setTs(new Date());
				temp_pi.setModifier(user.getPkEmp());

				DataBaseHelper.updateBeanByPk(temp_pi, false);
				return temp_pi;
			}

			pi.setCodePi(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
			pi.setCodeOp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZBL));
			pi.setCodeIp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL));
			pi.setCodeEr(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_JZ));//急诊号

			pi.setPkPicate("24d7bf6ec4034d248d1d0db083bcf7d1");// 患者分类，默认无优惠
			//根据身份证获取年龄、性别、出生日期
			Map<String, Object> sexMap = LbSelfUtil.getBirAgeSex(pi.getIdNo());

			pi.setDtSex(LbSelfUtil.getPropValueStr(sexMap,"sexCode"));// 性别编码
			pi.setBirthDate(DateUtils.parseDate(LbSelfUtil.getPropValueStr(sexMap,"birthday"),"yyyy-MM-dd"));// 出生日期

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
			insu.setCreateTime(new Date());
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
		}


		return pi;
	}

	//患者信息更新
	public PiMaster lbSavePiMaster(PiMaster pi,User user) throws ParseException {
		PiMaster temp_pi = DataBaseHelper.queryForBean("select code_pi,name_pi,pk_pi from pi_master where id_no = ? and dt_idtype='01' ",PiMaster.class,pi.getIdNo());
		if (StringUtils.isNotBlank(pi.getPkPi())) {
			pi.setPkOrg(user.getPkOrg());
			pi.setCreator(user.getPkEmp());
			pi.setCreateTime(new Date());
			pi.setDelFlag("0");
			pi.setTs(new Date());
			pi.setModifier(user.getPkEmp());
			DataBaseHelper.updateBeanByPk(pi, false);
		}else if (temp_pi != null) {
			temp_pi.setMobile(pi.getMobile());// 手机号
			temp_pi.setNamePi(pi.getNamePi());// 患者名称
			temp_pi.setModifier(user.getPkEmp());// 修改人
			temp_pi.setPkOrg(user.getPkOrg());
			temp_pi.setCreator(user.getPkEmp());
			temp_pi.setCreateTime(new Date());
			temp_pi.setDelFlag("0");
			temp_pi.setTs(new Date());
			temp_pi.setModifier(user.getPkEmp());

			DataBaseHelper.updateBeanByPk(temp_pi, false);
			return temp_pi;
		}else{
			pi.setCodePi(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
			pi.setCodeOp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZBL));
			pi.setCodeIp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL));
			pi.setCodeEr(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_JZ));//急诊号
			pi.setPkPicate("24d7bf6ec4034d248d1d0db083bcf7d1");// 患者分类，默认无优惠
			if(StringUtils.isNotBlank(pi.getIdNo())){
				//根据身份证获取年龄、性别、出生日期
				Map<String, Object> sexMap = LbSelfUtil.getBirAgeSex(pi.getIdNo());
				pi.setDtSex(LbSelfUtil.getPropValueStr(sexMap,"sexCode"));// 性别编码
				pi.setBirthDate(DateUtils.parseDate(LbSelfUtil.getPropValueStr(sexMap,"birthday"),"yyyy-MM-dd"));// 出生日期
			}
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
			insu.setCreateTime(new Date());
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
		}
		return pi;
	}
	/**
	 * 灵璧自助机患者发卡数据保存
	 * @throws ParseException
	 */
	public void lbSavePiCard(PiCard picard,PiCardDetail piCardDetail,User user,LbSHRequestVo requ){
		String getXh = "select * from pi_card where pk_pi=? and pk_org=?";
		List<PiCard> xhLst = DataBaseHelper.queryForList(getXh,PiCard.class, requ.getPatientId(), user.getPkOrg());

		int maxNo = 0;
		for (PiCard no : xhLst) {
			if (no.getSortNo() >= maxNo) {
				maxNo = no.getSortNo();
			}
		}
		maxNo++;
		picard.setSortNo(maxNo);
		picard.setFlagActive(EnumerateParameter.ONE);
		picard.setEuStatus(EnumerateParameter.ZERO);
		picard.setPkOrg(user.getPkOrg());
		DataBaseHelper.insertBean(picard);


		piCardDetail.setPkEmpOpera(user.getPkEmp());
		piCardDetail.setNameEmpOpera(user.getNameEmp());
		piCardDetail.setDateHap(new Date());
		piCardDetail.setModifier(user.getPkEmp());
		piCardDetail.setPkPicard(picard.getPkPicard());
		piCardDetail.setPkPi(picard.getPkPi());
		piCardDetail.setCardNo(picard.getCardNo());
		piCardDetail.setPkOrg(user.getPkOrg());
		piCardDetail.setEuOptype(EnumerateParameter.ZERO);
		DataBaseHelper.insertBean(piCardDetail);
	}

	/**
	 * 灵璧修改卡状态，进行重新发卡
	 * @throws ParseException
	 */
	public void lbupdatePiCard(PiCard piCards){
		piCards.setDelFlag("");
	}

	/**
	 * 灵璧自助机发医院就诊卡，插入交款记录信息
	 * @throws ParseException
	 */
	public void lbSaveBlDepositPi(PiAcc pia,PiCardDetail piCardDetail,User user,PiCard picard,LbSHRequestVo requ){
		int num = DataBaseHelper.queryForScalar("select count(1) from pi_card  where pk_pi= ? and eu_status<'2' ",Integer.class, requ.getPatientId());
		// 获取押金金额
		String jeVal = LbSelfUtil.getSysparam("PI0002", false,user);
		// 获取制卡费
		String fee = LbSelfUtil.getSysparam("PI0004", false,user);
		BigDecimal ye = pia.getAmtAcc();// 发卡时账户未加入押金
		pia.setAmtAcc(ye);
		DataBaseHelper.updateBeanByPk(pia, false);
		// 收取制卡费模式为1或2,插入交付款记录
		if (EnumerateParameter.ONE.equals(LbSelfUtil.getSysparam("PI0005", false,user))
				|| EnumerateParameter.TWO.equals(LbSelfUtil.getSysparam("PI0005", false,user))
				|| EnumerateParameter.THREE.equals(LbSelfUtil.getSysparam("PI0005", false,user))) {
			BlDepositPi jkjl = new BlDepositPi();
			BlPi blPi = new BlPi();
			if (EnumerateParameter.THREE.equals(LbSelfUtil.getSysparam("PI0005", false,user))) {
				pia.setAmtAcc(new BigDecimal(jeVal));
				DataBaseHelper.updateBeanByPk(pia, false);
				picard.setDeposit(new BigDecimal(jeVal));
				DataBaseHelper.updateBeanByPk(picard, false);
				jkjl.setAmount(new BigDecimal(jeVal));// 押金
				jkjl.setEuDirect(EnumerateParameter.ONE);
				jkjl.setPkPi(requ.getPatientId());
				jkjl.setDatePay(new Date());
				jkjl.setPkDept(user.getPkDept());
				jkjl.setPkEmpPay(user.getPkEmp());
				jkjl.setNameEmpPay(user.getNameEmp());
				jkjl.setDtPaymode(EnumerateParameter.ONE);
				DataBaseHelper.insertBean(jkjl);
				piAccDetailVal(pia, jkjl);
			} else if (num > 0|| EnumerateParameter.ONE.equals(LbSelfUtil.getSysparam("PI0005", false,user))) { // 补卡操作
				jkjl.setAmount(new BigDecimal(fee));// 工本费
				jkjl.setEuDirect(EnumerateParameter.ONE);
				jkjl.setPkPi(requ.getPatientId());
				jkjl.setDatePay(new Date());
				jkjl.setPkDept(user.getPkDept());
				jkjl.setPkEmpPay(user.getPkEmp());
				jkjl.setNameEmpPay(user.getNameEmp());
				jkjl.setDtPaymode(EnumerateParameter.ONE);
				DataBaseHelper.insertBean(jkjl);
				piAccDetailVal(pia, jkjl);
				// 非就诊计费明细
				blPi.setPkPi(requ.getPatientId());
				blPi.setNameBl("患者就诊卡制卡费");
				blPi.setPrice(Double.valueOf(fee));
				blPi.setQuan(Double.valueOf(EnumerateParameter.ONE));
				blPi.setAmount(Double.valueOf(fee)* Double.valueOf(EnumerateParameter.ONE));
				blPi.setFlagPd(EnumerateParameter.ONE);
				blPi.setDtPaymode(EnumerateParameter.ONE);
				blPi.setEuButype(EnumerateParameter.ZERO);
				blPi.setPkBu(piCardDetail.getPkPicarddt());
				blPi.setFlagCc(EnumerateParameter.ZERO);
				nonTreatment(blPi, user);
			}
		}
	}


	/**
	 * 插入卡详细信息押金值
	 *
	 * @param pia
	 * @param jkjl
	 */
	public static void piAccDetailVal(PiAcc pia, BlDepositPi jkjl) {
		PiAccDetail pad = new PiAccDetail();
		pad.setPkPiacc(pia.getPkPiacc());
		pad.setPkPi(pia.getPkPi());
		pad.setDateHap(new Date());
		pad.setEuOptype(EnumerateParameter.NINE);
		pad.setEuDirect(jkjl.getEuDirect());
		pad.setAmount(jkjl.getAmount());
		pad.setPkDepopi(jkjl.getPkDepopi());
		pad.setAmtBalance(pia.getAmtAcc());
		pad.setPkEmpOpera(jkjl.getPkEmpPay());
		pad.setNameEmpOpera(jkjl.getNameEmpPay());
		DataBaseHelper.insertBean(pad);
	}



	/**
	 * 保存挂号信息(有事物)
	 */
	public LbPiMasterRegVo savePvRegInfo(boolean Iffeel,LbPiMasterRegVo regvo,User u){
		String pkPv = NHISUUID.getKeyId();
		regvo.setPkPv(pkPv);
		regvo.setPkOrg(u.getPkOrg());
		// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkSchres", regvo.getPkSchres());
		Map<String,Object> schres = schMapper.querySchResInfo(paramMap);
		//2.保存就诊记录
		PvEncounter pv = savePvEncounter(regvo, pkPv,u);
		regvo.setCodePv(pv.getCodePv());
		//3.生成门诊就诊记录，写表pv_op；
		if("1".equals(pv.getEuPvtype())){
			savePvOp(regvo,pv,schres);//门诊就诊属性
		}else{
			//急诊就诊属性
			savePvEr(regvo, pv);
		}
		//判断是否有挂号费用
		if(Iffeel){
			/**
			 * 3.生成记费信息，写表bl_ip_dt；
			 * 4.生成结算信息，写表bl_settle；
			 * 5.生成结算明细，写表bl_settle_detail；
			 * 6.生成支付记录，写表bl_deposit；
			 * 如果挂号打印发票：
			 * 7.生成发票信息，写表bl_invoice；
			 * 8.生成发票和结算关系，写表bl_st_inv；
			 * 9.生成发票明细，bl_invoice_dt；
			 * 10.更新发票登记表，更新bl_emp_invoice。
			 */
			regvo = saveSettle(regvo, pv,u);
		}
		//有号表方式，更新排班已使用号数
		//DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[] { regvo.getPkSch() });
		schMapper.updateSchCntUsed(regvo.getPkSch());

		return regvo;
	}


	/**
	 * 保存就诊记录门诊属性
	 * @return
	 */
	private PvOp savePvOp(LbPiMasterRegVo master,PvEncounter pv,Map<String,Object> schres){
		// 保存门诊属性
		PvOp pvOp = new PvOp();
		pvOp.setPkPv(pv.getPkPv());
		Integer opTimes = pvPubForWsMapper.LbgetMaxOpTimes(master.getPkPi());
		pvOp.setOpTimes(new Long(opTimes+1));
		pvOp.setPkSchsrv(master.getPkSchsrv());
		pvOp.setPkRes(master.getPkSchres());
		pvOp.setPkDateslot(master.getPkDateslot());
		pvOp.setPkDeptPv(master.getPkDept());
		if(schres!=null){
			pvOp.setPkEmpPv(CommonUtils.getString(schres.get("pkEmp")));
			pvOp.setNameEmpPv(CommonUtils.getString(schres.get("nameEmp")));
		}
		pvOp.setTicketno(CommonUtils.isEmptyString(master.getTicketNo())?0:Long.parseLong(master.getTicketNo()));
		pvOp.setPkSch(master.getPkSch());
		pvOp.setFlagFirst("1"); // 初诊
		pvOp.setPkAppo(master.getPkAppt()); // 字段重复
		pvOp.setPkSchappt(master.getPkAppt());// 对应预约
		if(CommonUtils.isNull(master.getPkAppt())){//挂号方式
			pvOp.setEuRegtype("0");
		}else{
			pvOp.setEuRegtype("1");
		}
		pvOp.setDtApptype("0");
		// 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
		// ( 参数-1) || '23:59:59'
		pvOp.setDateBegin(pv.getDateBegin());

		if(!"9".equals(master.getEuSrvtype())){
			pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pv.getDateBegin()));
		}else{//急诊号24小时有效
			pvOp.setDateEnd(DateUtils.strToDate(DateUtils.addDate(pv.getDateBegin(), 24, 4, "yyyyMMddHHmmss")));
		}
		pvOp.setFlagNorelease("0");
		DataBaseHelper.insertBean(pvOp);
		return pvOp;
	}

	/**
	 * 保存就诊记录急诊属性
	 * @return
	 */
	private PvEr savePvEr(LbPiMasterRegVo master,PvEncounter pv){
		// 保存急诊属性
		PvEr pvEr = new PvEr();
		pvEr.setPkPv(pv.getPkPv());
		pvEr.setPkSchsrv(master.getPkSchsrv());
		pvEr.setPkRes(master.getPkSchres());
		pvEr.setPkDateslot(master.getPkDateslot());
		pvEr.setPkDeptPv(master.getPkDept());
		// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkSchres", master.getPkSchres());
		Map<String,Object> schres =schPubForWsMapper.querySchResInfo(paramMap);
		if(schres!=null){
			pvEr.setPkEmpPv(CommonUtils.getString(schres.get("pkEmp")));
			pvEr.setNameEmpPv(CommonUtils.getString(schres.get("nameEmp")));
		}
		pvEr.setTicketno(CommonUtils.isEmptyString(master.getTicketNo())?0:Long.parseLong(master.getTicketNo()));
		pvEr.setPkSch(master.getPkSch());

		// 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
		// ( 参数-1) || '23:59:59'
		pvEr.setDateBegin(pv.getDateBegin());
		pvEr.setDateEnd(ApplicationUtils.getPvDateEndEr(pv.getDateBegin()));
		pvEr.setDateArv(new Date());
		DataBaseHelper.insertBean(pvEr);
		return pvEr;
	}

	/**
	 * 保存就诊记录
	 * @param master
	 * @param pkPv
	 * @return
	 */
	private PvEncounter savePvEncounter(LbPiMasterRegVo master,String pkPv,User user){

		// 保存就诊记录
		boolean jz = "2".equals(master.getEuPvtype());
		PvEncounter pvEncounter = new PvEncounter();
		pvEncounter.setPkPv(pkPv);
		pvEncounter.setPkOrg(user.getPkOrg());
		pvEncounter.setPkPi(master.getPkPi());
		pvEncounter.setPkDept(master.getPkDept());//就诊科室
		pvEncounter.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
		pvEncounter.setEuPvtype(jz ? PvConstant.ENCOUNTER_EU_PVTYPE_2 : PvConstant.ENCOUNTER_EU_PVTYPE_1 ); // 急诊|门诊
		pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0); // 登记
		pvEncounter.setNamePi(master.getNamePi());
		pvEncounter.setDtSex(master.getDtSex());
		pvEncounter.setAgePv(DateUtils.getAgeByBirthday(master.getBirthDate(),pvEncounter.getDateBegin()));
		pvEncounter.setAddress(master.getAddress());
		pvEncounter.setFlagIn("0");
		pvEncounter.setFlagSettle("1");
		pvEncounter.setDtMarry(master.getDtMarry());
		pvEncounter.setPkInsu(master.getPkHp());
		pvEncounter.setPkPicate(master.getPkPicate());
		pvEncounter.setPkEmpReg(user.getPkEmp());
		pvEncounter.setNameEmpReg(user.getNameEmp());
		pvEncounter.setDateReg(new Date());
		if(master.getDateAppt()!=null){//如果是预约挂号，开始日期为预约日期
			pvEncounter.setDateBegin(master.getDateAppt());
		}else{
			pvEncounter.setDateBegin(master.getDateReg());//挂号的排班日期
		}
		//只保存pv_op表
		//if(schres!=null){
		//	pvEncounter.setPkEmpPhy(CommonUtils.getString(schres.get("pkEmp")));
		//	pvEncounter.setNameEmpPhy(CommonUtils.getString(schres.get("nameEmp")));
		//}
		pvEncounter.setFlagCancel("0");
		pvEncounter.setDtIdtypeRel("01");
		pvEncounter.setDtPvsource(master.getDtSource());
		pvEncounter.setNameRel(master.getNameRel());
		pvEncounter.setIdnoRel(master.getIdnoRel());
		pvEncounter.setTelRel(master.getTelRel());
		pvEncounter.setEuPvmode("0");
		pvEncounter.setFlagSpec(isSpec(master.getPkSrv())?"1":"0");
		pvEncounter.setEuStatusFp("0");
		pvEncounter.setEuLocked("0");
		pvEncounter.setEuDisetype("0");

		pvEncounter.setCreator(user.getPkEmp());
		pvEncounter.setCreateTime(new Date());
		pvEncounter.setDelFlag("0");
		pvEncounter.setTs(new Date());
		DataBaseHelper.insertBean(pvEncounter);

		return pvEncounter;
	}

	/**
	 * 保存结算信息（包含门诊收费明细，门诊结算，门诊结算明细，交易记录，发票信息，发票明细）
	 * @return
	 * @throws BusException
	 */
	private LbPiMasterRegVo saveSettle(LbPiMasterRegVo master,PvEncounter pv,User user) throws BusException {
		//没有收费项时不做收费处理
		if(master.getItemList()==null||master.getItemList().size()<=0)
			return master;

		String pkOrg = master.getPkOrg();
		String pkPi = master.getPkPi();
		String pkPv = master.getPkPv();

		String pkCurDept = user.getPkDept();// 当前科室
		String pkOpDoctor = user.getPkEmp();// 当前用户主键
		String nameUser = user.getNameEmp();// 当前用户名

		//1.保存门诊收费明细
		List<BlOpDt> blOpDts = constructBlOpDt(master, pv,user);
		if(blOpDts!=null&&blOpDts.size()>0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), blOpDts);
		//2.生成结算
		BigDecimal amountSt = BigDecimal.ZERO;// 结算金额
		BigDecimal amountPi = BigDecimal.ZERO;// 患者自付金额
		BigDecimal amountInsu = BigDecimal.ZERO;// 医保支付金额
		BigDecimal discAmount = BigDecimal.ZERO;// 患者优惠金额
		BigDecimal accountPrepaid = BigDecimal.ZERO;// 账户已付
		String pkDisc = null;
		for (BlOpDt bpt : blOpDts) {
			amountSt = amountSt.add(new BigDecimal(bpt.getAmount()));
			amountPi = amountPi.add(new BigDecimal(bpt.getAmount()));
			//医保优惠计费部分
			amountInsu = amountInsu.add(new BigDecimal(0.00));
			pkDisc = bpt.getPkDisc();// 优惠类型
			if (pkDisc != null) {
				//患者优惠
				discAmount = discAmount.add(new BigDecimal(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan())));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				accountPrepaid = accountPrepaid.add(new BigDecimal(bpt.getAmountPi()));
			}
		}


		//第三方医保支付金额
		BigDecimal amtInsuThird = master.getAmtInsuThird()==null?BigDecimal.ZERO:master.getAmtInsuThird();
		BlSettle bs = new BlSettle();
		bs.setPkOrg(pkOrg);
		bs.setPkPi(pkPi);
		bs.setPkPv(pkPv);
		bs.setPkInsurance(pv.getPkInsu());
		bs.setDtSttype("00");// 结算类型
		bs.setEuStresult(EnumerateParameter.ZERO);// 结算结果分类
		bs.setAmountSt(amountSt.setScale(2,BigDecimal.ROUND_HALF_UP));
		//此处患者支付还没有排除结算医保分摊的金额，同样医保支付还没有包含结算时医保分摊的金额
		bs.setAmountPi(amountPi.setScale(2,BigDecimal.ROUND_HALF_UP));
		bs.setAmountInsu(amountInsu.add(discAmount).add(amtInsuThird).setScale(2,BigDecimal.ROUND_HALF_UP));
		bs.setDateSt(new Date());
		bs.setPkOrgSt(pkOrg);
		bs.setPkDeptSt(pkCurDept);
		bs.setPkEmpSt(pkOpDoctor);
		bs.setNameEmpSt(nameUser);
		bs.setFlagCc(EnumerateParameter.ZERO);
		bs.setFlagCanc(EnumerateParameter.ZERO);
		bs.setFlagArclare(EnumerateParameter.ZERO);
		bs.setAmountPrep(BigDecimal.ZERO);
		bs.setAmountAdd(0d);
		bs.setAmountDisc(0d);
		bs.setCodeSt(ApplicationUtils.getCode("0604"));
		bs.setReceiptNo(master.getReceiptNo());
		bs.setDateReceipt(master.getDateReceipt());
		bs.setPkEmpReceipt(master.getPkEmpReceipt());
		bs.setNameEmpReceipt(master.getNameEmpReceipt());
		bs.setEuPvtype(pv.getEuPvtype());

		bs.setPkOrg(pkOrg);
		bs.setCreator(pkOpDoctor);
		bs.setCreateTime(new Date());
		bs.setDelFlag("0");
		bs.setTs(new Date());
		bs.setModifier(pkOpDoctor);

		DataBaseHelper.insertBean(bs);
		String pkSettle = bs.getPkSettle();
		// 将结算主键反写到记费细目表
		List<BlOpDt> blOpDtNews = new ArrayList<BlOpDt>();
		StringBuilder pkBlOpDts = new StringBuilder("");
		for (BlOpDt bpt : blOpDts) {
			BlOpDt bodNew = new BlOpDt();
			bodNew.setPkSettle(pkSettle);
			bodNew.setFlagSettle(EnumerateParameter.ONE);
			bodNew.setTs(new Date());
			bodNew.setPkCgop(bpt.getPkCgop());
			double cc =amountPi.doubleValue();
			bodNew.setAmountPi(cc);
			bodNew.setAmountHppi(cc);
			blOpDtNews.add(bodNew);
			pkBlOpDts = pkBlOpDts.append("'").append(bpt.getPkCgop()).append("',");
		}
		//批量更新blOpDtNews
		blPubForWsMapper.updateBlOpDtList(blOpDtNews);
		//DataBaseHelper.batchUpdate("update bl_op_dt set amount_hppi=:amountHppi,amount_pi=:amountPi,pk_settle=:pkSettle,flag_settle=:flagSettle,ts=:ts where pk_cgop=:pkCgop ", blOpDtNews);

		//3、生成结算明细
		/*
		 * 结算明细组成说明：
		 * （1）记费表bl_op_dt内部医保支付金额;
		 * （2）记费表bl_op_dt优惠比例金额；
		 * （3）结算时第三方医保支付金额；
		 * （4）结算时每次调动医保返回的报销金额；
		 * （5）患者自付金额。
		 */
		Map<String,Object> mapParamTemp = new HashMap<String,Object>();
		List<BlSettleDetail> blSettleDetails = new ArrayList<BlSettleDetail>();
		BdHp bdHp = null;
		// 3.1、内部主医保项目和种类支付金额
		if (amountInsu.compareTo(BigDecimal.ZERO) == 1) {
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkSettle(bs.getPkSettle());
			mapParamTemp.clear();
			mapParamTemp.put("pkHp", pv.getPkInsu());
			mapParamTemp.put("pkOrg", pkOrg);
			bdHp = blPubForWsService.qryBdHpInfo(mapParamTemp);
			blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
			blSettleDetail.setPkInsurance(pv.getPkInsu());// 主医保计划
			blSettleDetail.setAmount(amountInsu.doubleValue());

			blSettleDetail.setCreator(pkOpDoctor);
			blSettleDetail.setCreateTime(new Date());
			blSettleDetail.setDelFlag("0");
			blSettleDetail.setTs(new Date());
			blSettleDetail.setModifier(pkOpDoctor);

			blSettleDetails.add(blSettleDetail);
		}
		//3.2 外部医保支付金额
		if(amtInsuThird.compareTo(BigDecimal.ZERO)==1){
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkSettle(bs.getPkSettle());
			mapParamTemp.clear();
			mapParamTemp.put("pkHp", pv.getPkInsu());
			mapParamTemp.put("pkOrg", pkOrg);
			bdHp = blPubForWsService.qryBdHpInfo(mapParamTemp);
			blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
			blSettleDetail.setPkInsurance(pv.getPkInsu());// 主医保计划
			blSettleDetail.setAmount(amountInsu.doubleValue());

			blSettleDetail.setCreator(pkOpDoctor);
			blSettleDetail.setCreateTime(new Date());
			blSettleDetail.setDelFlag("0");
			blSettleDetail.setTs(new Date());
			blSettleDetail.setModifier(pkOpDoctor);

			blSettleDetails.add(blSettleDetail);
		}
		// 3.3、患者的优惠比例金额
		if (discAmount.compareTo(BigDecimal.ZERO) == 1) {
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkSettle(bs.getPkSettle());
			mapParamTemp.clear();
			mapParamTemp.put("pkHp", pkDisc);
			mapParamTemp.put("pkOrg", pkOrg);
			bdHp = blPubForWsService.qryBdHpInfo(mapParamTemp);
			blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
			blSettleDetail.setPkInsurance(pkDisc);// 优惠类型主键
			blSettleDetail.setAmount(discAmount.doubleValue());

			blSettleDetail.setCreator(pkOpDoctor);
			blSettleDetail.setCreateTime(new Date());
			blSettleDetail.setDelFlag("0");
			blSettleDetail.setTs(new Date());
			blSettleDetail.setModifier(pkOpDoctor);

			blSettleDetails.add(blSettleDetail);
		}
		//不计算总额分摊的情况
		//3.4、患者支付金额
		if (amountPi.compareTo(BigDecimal.ZERO) == 1) {
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkStdt(NHISUUID.getKeyId());
			blSettleDetail.setPkSettle(bs.getPkSettle());
			mapParamTemp.put("pkOrg", "~                               ");
			BdPayer bdPayer = blPubForWsService.qryBdPayerByEuType(mapParamTemp);
			if (bdPayer == null) {
				throw new BusException("未维护支付方为本人的医保计划");
			}
			//查询付款方式为本人的医保主键
			BdHp hp = DataBaseHelper.queryForBean(
					"select * from bd_hp where PK_PAYER = ? and EU_HPTYPE = '0'",
					BdHp.class, new Object[]{bdPayer.getPkPayer()});
			blSettleDetail.setPkPayer(bdPayer.getPkPayer());// 支付方(本人)
			blSettleDetail.setPkInsurance(hp.getPkHp());//自费部分，全自费主键
			blSettleDetail.setAmount(amountPi.doubleValue());// 患者自付金额

			blSettleDetail.setCreator(pkOpDoctor);
			blSettleDetail.setCreateTime(new Date());
			blSettleDetail.setDelFlag("0");
			blSettleDetail.setTs(new Date());
			blSettleDetail.setModifier(pkOpDoctor);

			blSettleDetails.add(blSettleDetail);
		}
		// 批量统一插入结算明细表
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), blSettleDetails);

		// 4、写结算交款记录表
		List<BlDeposit> depositListDao = new ArrayList<BlDeposit>();
		List<BlDeposit> depositList = master.getDepositList();// 支付方式

		for (BlDeposit blDeposit : depositList) {
			BlDeposit blDepositDao = new BlDeposit();
			blDepositDao.setPkDepo(NHISUUID.getKeyId());
			blDepositDao.setCreator(pkOpDoctor);
			blDepositDao.setCreateTime(new Date());
			blDepositDao.setDelFlag("0");
			blDepositDao.setTs(new Date());
			blDepositDao.setModifier(pkOpDoctor);

			blDepositDao.setPkOrg(pkOrg);
			blDepositDao.setEuDptype(EnumerateParameter.ZERO);
			blDepositDao.setEuDirect(EnumerateParameter.ONE);
			blDepositDao.setPkPi(pkPi);
			blDepositDao.setPkPv(pkPv);
			blDepositDao.setEuPvtype(pv.getEuPvtype());
			// 交易金额
			blDepositDao.setAmount(blDeposit.getAmount());
			blDepositDao.setDtPaymode(master.getDtPaymode());
			blDepositDao.setPayInfo(blDeposit.getPayInfo()); //收付款方式信息 对应支票号，银行交易号码
			blDepositDao.setDatePay(new Date());
			blDepositDao.setPkDept(pkCurDept);
			blDepositDao.setPkEmpPay(pkOpDoctor);
			blDepositDao.setNameEmpPay(nameUser);
			blDepositDao.setFlagAcc(blDepositDao.getFlagAcc() == null ? EnumerateParameter.ZERO : EnumerateParameter.ONE);
			blDepositDao.setFlagSettle(EnumerateParameter.ONE);
			blDepositDao.setPkSettle(bs.getPkSettle());
			blDepositDao.setFlagCc(EnumerateParameter.ZERO);// 操作员结账标志
			depositListDao.add(blDepositDao);
		}
		master.setDepositList(depositListDao);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), depositListDao);

		master.setPkSettle(pkSettle);
		return master;
	}

	/**
	 * 构建门诊记费参数
	 * @param master
	 * @param pv
	 * @param u
	 * @return
	 */
	private List<BlOpDt> constructBlOpDt(LbPiMasterRegVo master,PvEncounter pv,User user){
		if(master.getItemList()==null||master.getItemList().size()<=0)
			return null;
		String codeCg = ApplicationUtils.getCode("0601");
		List<BlOpDt> blopdts = new ArrayList<BlOpDt>();
		List<ItemPriceVo> itemList = new ArrayList<>();
		itemList = (List<ItemPriceVo>)master.getItemList();
		String jsonBean =JsonUtil.writeValueAsString(itemList);
		List<ItemPriceVo> items =new ArrayList<>();
		items = (List<ItemPriceVo>)JsonUtil.readValue(jsonBean, new TypeReference<List<ItemPriceVo>>(){});
		for(ItemPriceVo item:items){
			item.setFlagPd("0");
			BlOpDt vo = new BlOpDt();
			String pkCgop = NHISUUID.getKeyId();
			vo.setPkCgop(pkCgop);
			vo.setSpec(item.getSpec());
			vo.setPkOrg(user.getPkOrg());
			vo.setPkPv(master.getPkPv());
			vo.setPkPi(master.getPkPi());
			vo.setPkOrgApp(user.getPkOrg());
			vo.setPkDeptApp(pv.getPkDept());
			vo.setPkOrgEx(user.getPkOrg());
			vo.setPkSchAppt(master.getPkAppt());
			//如果序号为空，从1开始递增
			vo.setSortno(1);
			vo.setPkEmpApp(CommonUtils.isEmptyString(item.getPkEmpApp())?pv.getPkEmpPhy():item.getPkEmpApp());
			vo.setNameEmpApp(CommonUtils.isEmptyString(item.getNameEmpApp())?pv.getNameEmpPhy():item.getNameEmpApp());
			vo.setPkDeptEx(CommonUtils.isEmptyString(item.getPkDeptEx())?pv.getPkDept():item.getPkDeptEx());
			//医嘱自动记费不写记费部门、记费人员、记费人员姓名字段。
			if(!LbSelfUtil.converToTrueOrFalse(item.getFlagSign())){
				vo.setPkDeptCg(CommonUtils.isEmptyString(item.getPkDeptCg())?user.getPkDept():item.getPkDeptCg());
				vo.setPkEmpCg(CommonUtils.isEmptyString(item.getPkEmpCg())?user.getPkEmp() : item.getPkEmpCg());
				vo.setNameEmpCg(CommonUtils.isEmptyString(item.getNameEmpCg())?user.getNameEmp() : item.getNameEmpCg());//当前用户姓名；
			}
			if(CommonUtils.isEmptyString(vo.getPkEmpApp()))
				vo.setPkEmpApp(user.getPkEmp());
			if(CommonUtils.isEmptyString(vo.getNameCg()))
				vo.setNameEmpApp(user.getNameEmp());
			//校验dateCg是否为null
			if(item.getDateCg()!=null)
				vo.setDateCg(item.getDateCg());
			else
				vo.setDateCg(new Date());
			//获取当前时间yyyy-MM-dd
			Date dateHap = null;
			if(item.getDateHap()!=null)
				dateHap = DateUtils.strToDate(DateUtils.formatDate(item.getDateHap(), "yyyy-MM-dd"),"yyyy-MM-dd");
			else
				dateHap = DateUtils.strToDate(DateUtils.getDateTime(),"yyyy-MM-dd");
			vo.setDateHap(dateHap);
			vo.setPkUnit(item.getPkUnit());
			vo.setPkUnitPd(item.getPkUnitPd());
			vo.setSpec(item.getSpec());
			vo.setPackSize(1);
			//设置药品相关属性
			if("1".equals(item.getFlagPd())){//药品
				vo.setPkPd(item.getPkOrdOld());
				vo.setPkUnit(item.getPkUnitPd());
				vo.setNameCg(item.getName());
			}
			vo.setBatchNo(item.getBatchNo());
			vo.setPrice(item.getPrice());
			vo.setFlagPv("1");//挂号结算标志
			vo.setPriceCost(item.getPriceCost());
			vo.setDateExpire(item.getDateExpire());
			vo.setPkItem(item.getPkItem());
			vo.setNameCg(item.getName());
			vo.setPkItemcate(item.getPkItemcate());
			vo.setFlagInsu("0");
			vo.setFlagPd(item.getFlagPd());
			vo.setPkPres(item.getPkPres());
			vo.setPkCnord(item.getPkCnord());
			vo.setPkCgopBack(null);
			vo.setFlagSettle("0");
			vo.setCodeCg(codeCg);
			vo.setFlagAcc("0");

			// 设置单价，数量，金额，比例等信息
			// price_org,price,quan,amount,pk_disc,ratio_disc,ratio_self,amount_pi
			vo.setQuan(item.getQuan());
			vo.setPriceOrg(item.getPrice());
			vo.setPkDisc(item.getPkDisc());
			vo.setRatioDisc(item.getRatioDisc()==null?1D:item.getRatioDisc());
			vo.setRatioSelf(item.getRatioSelf()==null?1D:item.getRatioSelf());

			vo.setRatioAdd(item.getRatioSpec()!=null && LbSelfUtil.converToTrueOrFalse(pv.getFlagSpec())?item.getRatioSpec():0D);	//特诊加收比例

			vo.setFlagRecharge("0");
			//amount，金额，price_org*quan+amount_add
			vo.setAmount(MathUtils.mul(vo.getQuan(), vo.getPriceOrg()));

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("pkItem", vo.getPkItem());
			param.put("pkOrg", vo.getPkOrg());
			param.put("euType", Constant.OPINV);
			param.put("flagPd", vo.getFlagPd());
			Map<String, Object> resBill =  blPubForWsService.qryBillCodeByPkItem(param);
			Map<String, Object> resAccount = blPubForWsService.qryAccountCodeByPkItem(param);

			if(resBill!=null){
				vo.setCodeBill(resBill.get("code")==null?"":(String)resBill.get("code"));
			}
			if(resAccount!=null){
				vo.setCodeAudit(resAccount.get("code")==null?"":(String) resAccount.get("code"));
			}

			vo.setPkOrg(user.getPkOrg());
			vo.setCreator(user.getPkEmp());
			vo.setCreateTime(new Date());
			vo.setDelFlag("0");
			vo.setTs(new Date());
			vo.setModifier(user.getPkEmp());

			//组装最终集合之前过滤amount=0的记费信息，保存amount=0的信息在结算时上传记费明细到医保端可能会出问题，所以记费时过滤费用为0的项目。
			if(!MathUtils.equ(vo.getAmount(), 0.0)
					&& !MathUtils.equ(vo.getQuan(), 0.0))
			{
				blopdts.add(vo);
			}
		}
		return blopdts;
	}

	/**
	 * 依据服务主键，判断当前服务的服务类型 是否为 特诊
	 * @param pkSrv
	 * @return
	 */
	private boolean isSpec(String pkSrv){
		if(StringUtils.isNotBlank(pkSrv)){
			SchSrv srv = DataBaseHelper.queryForBean("select eu_srvtype from sch_srv where pk_schsrv=?", SchSrv.class, pkSrv);
			return srv!=null && "2".equals(srv.getEuSrvtype());
		}
		return false;
	}

	/**
	 * @param user
	 * @param blPi
	 *            非就诊计费接口
	 */
	public void nonTreatment(BlPi blPi, User user) {
		blPi.setCodeBl(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
		blPi.setDateBl(new Date());
		blPi.setPkDeptBl(((User) user).getPkDept());
		blPi.setPkEmpBl(((User) user).getPkEmp());
		blPi.setNameEmpBl(((User) user).getNameEmp());

		blPi.setPkOrg(user.getPkOrg());
		blPi.setCreator(user.getPkEmp());
		blPi.setCreateTime(new Date());
		blPi.setDelFlag("0");
		blPi.setTs(new Date());
		blPi.setModifier(user.getPkEmp());

		DataBaseHelper.insertBean(blPi);
	}

	/**
	 * 灵璧锁号操作
	 * @param requ
	 * @param ticket
	 * @return
	 */
	public LbSHResponseVo lbLockReg(LbSHRequestVo requ,SchTicket ticket) {
		LbSHResponseVo responseVo =new LbSHResponseVo();
		String pkSch = requ.getRegId();
		String sql = "select * from sch_sch where del_flag = '0' and pk_sch = ?";
		SchSch schSch = DataBaseHelper.queryForBean(sql,SchSch.class, pkSch);
		// 1.判断是否还有可约号
		if (schSch == null || "1".equals(schSch.getFlagStop())){
			responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESFAIL, "您所选排班不存在或已停用！"));
			return responseVo;
		}
		if(schSch.getCntTotal().intValue()<=schSch.getCntUsed().intValue()){
			responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESFAIL, "您所选排班已挂满号！"));
			return responseVo;
		}
		//构造排班计划
		//中二 日排班 是没有计划的，将要使用的值传入即可
		boolean flagTicket = DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where pk_sch = ?",
				Integer.class, new Object[]{pkSch})>0;
		//2.占用号源
		if(StringUtils.isNotBlank(requ.getTicketNo())){
			List<SchTicket> tickets = DataBaseHelper.queryForList(
					"select * from sch_ticket where pk_sch = ? and ticketno=? and DEL_FLAG = '0' and FLAG_USED = '1'",
					SchTicket.class, pkSch,requ.getTicketNo());
			ticket = tickets.get(0);
		}else if(flagTicket){
			List<SchTicket> tickets = new ArrayList<>();
			//判断时段编码是否为空
			if(CommonUtils.isNotNull(requ.getPhaseCode())){
				Map<String, Object> slotSecMap = DataBaseHelper.queryForMap("select * from bd_code_dateslot_sec where pk_dateslotsec=?", requ.getPhaseCode());
				//判断map是否为空
				if(BeanUtils.isNotNull(slotSecMap)){
					tickets = DataBaseHelper.queryForList(
							"select * from sch_ticket where pk_sch = ? and TO_CHAR (begin_time, 'hh24:mi:ss') >=? and TO_CHAR (end_time, 'hh24:mi:ss') <=?  and DEL_FLAG = '0'  and flag_stop='0' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
							SchTicket.class, pkSch,slotSecMap.get("timeBegin"),slotSecMap.get("timeEnd"));
				}else{
					tickets = DataBaseHelper.queryForList(
							"select * from sch_ticket where pk_sch = ? and DEL_FLAG = '0'  and flag_stop='0' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
							SchTicket.class, pkSch);
				}

			}else{
				tickets = DataBaseHelper.queryForList(
						"select * from sch_ticket where pk_sch = ? and DEL_FLAG = '0'  and flag_stop='0' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
						SchTicket.class, pkSch);
			}

			if(tickets==null||tickets.size()<=0){
				responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESFAIL, "您所选排班已挂满号！"));
				return responseVo;
			}
			ticket = tickets.get(0);
			Map<String, Object> schTicketMap = new HashMap<String, Object>();
			//好号主键
			schTicketMap.put("pkSchticket", ticket.getPkSchticket());
			//需要更新的状态
			schTicketMap.put("flagUsed", "1");
			//更新的条件
			schTicketMap.put("flagUseds", "0");
			//占用号表数据
			int cnt = schMapper.updateSchTicketFlagUsed(schTicketMap);
			//DataBaseHelper.update("update sch_ticket set FLAG_USED ='1' where pk_schticket =? and FLAG_USED='0'", new Object[]{ticket.getPkSchticket()});
			if(cnt<=0){
				responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESFAIL, "您所选的挂号号码已被占用，请重试！"));
				return responseVo;
			}

		}else{//无号表方式 ，锁定排班表
			//先更新号表，后查询更新的票号返回
			int cnt = schMapper.updateSchSchTicketNo(pkSch);
			//DataBaseHelper.update("update sch_sch  set TICKET_NO = nvl(TICKET_NO,0)+1,cnt_used=cnt_used+1 where PK_SCH = ? and cnt_used<cnt_total", new Object[]{pkSch});
			if(cnt<=0){
				responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESFAIL, "您所选的排班资源已挂满！"));
				return responseVo;
			}
			ticket =  new SchTicket();
			SchSch sch= DataBaseHelper.queryForBean("select ticket_no,pk_sch from sch_sch where pk_sch = ?", SchSch.class, new Object[]{pkSch});
			ticket.setTicketno(sch.getTicketNo());
			ticket.setPkSch(pkSch);
		}

		//获取收费项目
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkSchticket", ticket.getPkSchticket());
		List<Map<String, Object>> listMap = schPubForWsMapper.querySchInfo(map);

		BigDecimal amtAcc = BigDecimal.ZERO;
		if(0 != listMap.size()){
			for (int i = 0; i < listMap.size(); i++) {
				if(null != listMap.get(i).get("price")){
					Double pric = Double.parseDouble(listMap.get(i).get("price").toString());
					BigDecimal amt=BigDecimal.valueOf(pric);
					amtAcc=amtAcc.add(amt);
				}
			}
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "操作成功！"));
		responseVo.setTotalFee(amtAcc.toString());//挂号总费用
		responseVo.setPayFee(amtAcc.toString());//支付总分用
		responseVo.setWaitNo(ticket.getTicketno());//候诊号
		responseVo.setRegId(ticket.getPkSchticket());//号源编码
		return responseVo;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String lockTicket(Map<String, Object> queryMap){
		List<Map<String,Object>> list = schPubForWsMapper.queryTicketsBySchAndTimeList(queryMap);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		//占用号表数据
		String pkSchticket = MapUtils.getString(list.get(0),"pkSchticket");
		int cnt = DataBaseHelper.update("update sch_ticket set FLAG_USED ='1' where pk_schticket =? and FLAG_USED='0'"
				, new Object[]{pkSchticket});
		if(cnt<=0)
			throw new BusException("您所选的挂号号码已被占用，请重试！");
		return pkSchticket;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void unLockTicket(String pkSchticket){
		DataBaseHelper.update("update sch_ticket set FLAG_USED ='0' where pk_schticket = ?", new Object[]{pkSchticket});
	}

	public LbSHResponseVo registerWechat(String param,LbSHRequestVo requ,User user,LbPiMasterRegVo regvo) throws ParseException{
		LbPubForWsService service = ServiceLocator.getInstance().getBean(LbPubForWsService.class);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pkSch", requ.getRegId());
		String registerDateStr = requ.getRegDate();
		String registerTimeStr = (String) requ.getRegisterTime();
		String[] timeArgs = registerTimeStr.split("-");
		queryMap.put("beginDate", registerDateStr + " " + timeArgs[0]);
		queryMap.put("endDate", registerDateStr + " " + timeArgs[1]);
		String pkSchticket = service.lockTicket(queryMap);
		if(pkSchticket == null){
			throw new BusException("预约失败，当前时段预约已满！");
		}
		try{
			requ.setRegId(pkSchticket);
			return service.register(param, requ, user, regvo);
		} catch (Exception e){
			service.unLockTicket(pkSchticket);
			logger.error("微信挂号异常：",e);
			throw new BusException("挂号失败,其他异常！");
		}
	}

	/**
	 * 灵璧挂号操作
	 * @param pkSch
	 * @param WaitNo
	 * @throws ParseException
	 */
	public LbSHResponseVo register(String param,LbSHRequestVo requ,User user,LbPiMasterRegVo regvo) throws ParseException{
		//响应结果
		LbSHResponseVo responseVo = new LbSHResponseVo();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkSchticket", requ.getRegId());
		//根据号源主键查新排班信息
		List<Map<String, Object>> schPlanlist = schMapper.LbgetSchPlanInfo(paramMap);
		if(schPlanlist.size()<=0){
			logger.info("号源信息有误："+param);
			responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESFAIL, "号源信息有误，请重新获取"));
			return responseVo;
		}
		if(schPlanlist.get(0)==null){
			logger.info("号源信息有误："+param);
			responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESFAIL, "号源信息有误，请重新获取"));
			return responseVo;
		}
		boolean Iffeel = false;

		regvo.setDateReg(new Date());//挂号日期--排班日期
		regvo.setPkDept(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkDept"));//挂号科室
		if(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"euSrvtype").equals("9")){
			regvo.setEuPvtype("2");//就诊类型
		}
		regvo.setDtPaymode(requ.getPayType());//支付类型
		regvo.setPkSchres(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkSchres"));
		regvo.setPkDateslot(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkDateslot"));//日期分组
		regvo.setTicketNo(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"ticketno"));//预约票号
		regvo.setPkSch(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkSch"));//排班主键
		regvo.setPkSchsrv(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkSchsrv"));//排班服务主键

		BigDecimal amt=BigDecimal.ZERO;
		if(!CommonUtils.isEmptyString(requ.getPayAmt()) && !("0.0").equals(requ.getPayAmt()) && !("0").equals(requ.getPayAmt())&& !("0.00").equals(requ.getPayAmt())){
			BlDeposit dep =new BlDeposit();
			Double pric = Double.parseDouble(requ.getPayAmt());
			amt=BigDecimal.valueOf(pric);

			//账户支付可用余额校验
			if(("4").equals(requ.getPayType())){
				logger.info("挂号暂不支持账户支付："+param);
				responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESFAIL, "挂号暂不支持账户支付"));
				return responseVo;
			}else if(!("1").equals(requ.getPayType()) && !("4").equals(requ.getPayType())){
				dep.setPayInfo(requ.getQrCodeInfoVo().get(0).getFlowno());
			}

			dep.setAmount(amt);
			dep.setDtPaymode(requ.getPayType());//支付方式
			List<BlDeposit> depList =new ArrayList<>();
			depList.add(dep);
			regvo.setDepositList(depList);
			Iffeel = true;
			//门诊诊察费明细
			List<ItemPriceVo>  itemList = bdPubForWsMapper.LbgetBdItemInfo(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkSchsrv"));
			regvo.setItemList(itemList);
		}

		//判断是否允许挂号
		String date = regvo.getDateAppt()!=null?DateUtils.formatDate(regvo.getDateAppt(), "yyyyMMdd"): (regvo.getDateReg()!=null?DateUtils.formatDate(regvo.getDateReg(), "yyyyMMdd"):null);
		if(StringUtils.isNotBlank(date)) {
			if(DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.PK_DEPT=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
					Integer.class, new Object[]{regvo.getPkPi(),date,regvo.getPkDept()}) >0){
				responseVo.setCommonVo(LbSelfUtil.commonVo("0", "该患者已经存在当前日期和科室的挂号记录！"));
				return responseVo;
			}
		}

		//保存挂号信息（含保存患者信息）
		regvo =savePvRegInfo(Iffeel, regvo, user);
		responseVo.setCodeNo(regvo.getCodePv());//门诊流水号
		Map<String, Object> PatMap = new HashMap<String, Object>();
		PatMap.put("pkPi", regvo.getPkPi());
		PatMap.put("pkPv", regvo.getPkPv());
		PatMap.put("pkEmp", regvo.getPkEmp());

		if(Iffeel){
			if(("7").equals(requ.getPayType()) || ("8").equals(requ.getPayType())){
				/**
				 * 支付信息写入外部支付接口记录表bl_ext_pay
				 */
				BlDeposit vo =regvo.getDepositList().get(0);
				blPubForWsService.LbPayment(vo,requ,PatMap,user,null);
			}
		}
		Map<String, Object> piCardMap = DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where FLAG_ACTIVE = '1' AND EU_STATUS ='0' and DT_CARDTYPE ='01' and pk_pi=? ORDER BY create_time DESC", new Object[]{requ.getPatientId()});
		if(piCardMap != null){
			responseVo.setDtcardNo(LbSelfUtil.getPropValueStr(piCardMap,"cardNo"));
		}else{
			responseVo.setDtcardNo(regvo.getCodeOp());
		}
		if(("2").equals(requ.getBookType())){
			Map<String, Object> schMap = new HashMap<String, Object>();
			schMap.put("euStatus", "1");
			schMap.put("pkpi", regvo.getPkPi());
			schMap.put("pkSch", LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkSch"));
			schMapper.updateSchApptEuStatus(schMap);
			//DataBaseHelper.execute("update sch_appt set eu_status ='1' where pk_pi=? and pk_sch=? and eu_status='0'",regvo.getPkPi(),LbSelfUtil.getPropValueStr(schPlanlist.get(0),"pkSch"));
		}
		responseVo.setTotalFee(requ.getPayAmt());
		responseVo.setDeptName(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"name"));//挂后科室名称
		responseVo.setLocation(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"namePlace"));//候诊地点
		responseVo.setWaitNo(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"ticketno"));
		responseVo.setDoctName(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"nameEmp"));//医生姓名
		responseVo.setTypeName(LbSelfUtil.getPropValueStr(schPlanlist.get(0),"stvName"));
		responseVo.setCodeOp(regvo.getCodeOp());
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "操作成功！"));

		//发送消息到平台
		Map<String,Object> msgParam =  new HashMap<String,Object>();
		msgParam.put("pkEmp", user.getPkEmp());
		msgParam.put("nameEmp", user.getNameEmp());
		msgParam.put("codeEmp",user.getCodeEmp());
		msgParam.put("pkPv", regvo.getPkPv());
		msgParam.put("isAdd", "0");
		PlatFormSendUtils.sendPvOpRegMsg(msgParam);
		return responseVo;
	}
	/**
	 * 灵璧解号
	 * @param pkSch
	 * @param WaitNo
	 */
	public void lbUnLockReg(String pkSchticket){
		Map<String, Object> schTicketMap = new HashMap<String, Object>();
		//好号主键
		schTicketMap.put("pkSchticket", pkSchticket);
		//需要更新的状态
		schTicketMap.put("flagUsed", "0");
		//更新的条件
		schTicketMap.put("flagUseds", "1");
		//占用号表数据
		schMapper.updateSchTicketFlagUsed(schTicketMap);

		//String sql = "update sch_ticket set FLAG_USED ='0' where FLAG_USED='1' and pk_schticket =? ";
		//DataBaseHelper.update(sql, new Object[]{pkSchticket});
	}

	/**
	 * 灵璧解号
	 * @param pkSch
	 * @param WaitNo
	 */
	public int lbCancelOrder(Map<String, Object> schApptMap, User user){
		Map<String, Object> schAppMap = new HashMap<String, Object>();
		schAppMap.put("euStatus", "9");
		schAppMap.put("flagCancel", "1");
		schAppMap.put("dateCancel", new Date());
		schAppMap.put("pkEmpCancel", user.getPkEmp());
		schAppMap.put("flagCancel", "0");
		schAppMap.put("euStatuss", "0");
		schAppMap.put("pkSchappt", schApptMap.get("pkSchappt"));
		//根据PkSchappt更新状态
		int up=schMapper.updateSchApptPkSchappt(schAppMap);
		//DataBaseHelper.execute("update SCH_APPt set eu_status ='9',flag_cancel='1',date_cancel=?,pk_emp_cancel=? where flag_cancel='0' and eu_status='0' and pk_schappt=?",new Date(),user.getPkEmp(),schApptMap.get("pkSchappt"));
		if(up>0){
			Map<String, Object> tickMap = new HashMap<String, Object>();
			tickMap.put("flagUsed", "0");
			tickMap.put("flagUseds", "1");
			tickMap.put("pkSch", schApptMap.get("pkSch"));
			tickMap.put("ticketNo", schApptMap.get("ticketNo"));
			//无号表主键时根据pksch、ticketno条件进行更新
			schMapper.updateSchTicketAppt(tickMap);
			//DataBaseHelper.execute("update sch_ticket set FLAG_USED ='0' where FLAG_USED='1' and pk_sch=? and ticketno=?",schApptMap.get("pkSch"),schApptMap.get("ticketNo"));
			return up;
		}else{
			return 0;
		}
	}
}
