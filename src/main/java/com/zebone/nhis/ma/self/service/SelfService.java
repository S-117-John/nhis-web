package com.zebone.nhis.ma.self.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.common.module.base.bd.ques.BdTopicOpt;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.ma.dp.DpInvResult;
import com.zebone.nhis.common.module.ma.dp.DpInvResultDt;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.acc.PiAccShare;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.ma.self.dao.SelfMapper;
import com.zebone.nhis.ma.self.vo.CardAndPageInfo;
import com.zebone.nhis.ma.self.vo.CardOrAccVo;
import com.zebone.nhis.ma.self.vo.SelfPatInfo;
import com.zebone.nhis.pi.pub.service.PiPubCarddealService;
import com.zebone.nhis.pi.pub.vo.PiMasterVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 自助服务
 * 
 * @author L
 * 
 */
@Service
public class SelfService {

	@Resource
	private SelfMapper selfMapper;
	
	@Autowired
	private OpcgPubHelperService opcgPubHelperService;
	
	@Autowired
	private PiPubCarddealService piPubCarddealService;
	
	/**
	 * 一卡通账户充值 1.后台生成票据号 2.允许正数,只能增加
	 * 3.给出患者主键，新增、修改表pi_acc,pi_acc_detail,bl_deposit_pi
	 * 
	 * 自助机暂时默认无转存
	 * @param param
	 * @param user
	 */
	public void cardRecharge(String param, IUser user) {
	
		BlDepositPi depi = JsonUtil.readValue(param, BlDepositPi.class);
		String dtPayMode=JsonUtil.getFieldValue(param, "dtPayMode");
		if (depi.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new BusException("充值金额小于0，请确认！");
		}
		if ("-1".equals(depi.getEuDirect())) {
			throw new BusException("该功能只允许充值，请确认！");
		}
		// 读取患者账户信息,账户和患者信息1对1
		String getPiaccInfo = "select * from pi_acc where pk_pi=? and (del_flag='0' or del_flag is null)";
		PiAcc pa = DataBaseHelper.queryForBean(getPiaccInfo, PiAcc.class, new Object[] { depi.getPkPi() });
		if (pa == null) {
			throw new BusException("患者账户已被删除，请确认。");
		}
		// 生成收据号
		String pjh = ApplicationUtils.getCode("0603");
		depi.setDatePay(new Date());
		depi.setReptNo(pjh);
		depi.setPkDept(((User) user).getPkDept());
		depi.setPkEmpPay(((User) user).getPkEmp());
		depi.setNameEmpPay(((User) user).getNameEmp());
		depi.setNote("一卡通账户充值-自助");
		BalAccoutService jsservice = new BalAccoutService();
		jsservice.saveMonOperation(depi, user,null,null,dtPayMode);
		/*
		 * DataBaseHelper.insertBean(depi); // 账户操作流水记录 PiAccDetail pad = new
		 * PiAccDetail(); pad.setAmount(depi.getAmount());
		 * pad.setPkPiacc(pa.getPkPiacc()); pad.setDateHap(depi.getDatePay());
		 * pad.setEuOptype("1"); pad.setEuDirect(depi.getEuDirect());
		 * pad.setNote("一卡通账户充值-自助"); pad.setAmtBalance(pa.getAmtAcc() +
		 * depi.getAmount()); pad.setPkDepopi(depi.getPkDepopi());
		 * DataBaseHelper.insertBean(pad);
		 */
	}
	/**
	 * 自助开户 1.根据身份证号,医保卡号，手机号码判断是否已存在改患者 2.自动获取患者编号,创建患者 3.创建一卡通账户
	 * 4.根据dtCardtype,将身份证或者医保卡发卡
	 * @param param
	 * @param user
	 * @return
	 */
	public PiMaster creatAccountBySelf(String param, IUser user) {

		User userorg = (User) user;
		// 1
		String cardType = JSON.parseObject(param).getString("dtCardtype");
		PiMaster piMas = JsonUtil.readValue(param, PiMaster.class);
		List<PiMaster> sqlpi = selfMapper.getpiMasterByCredent(piMas.getDtIdtype(), piMas.getIdNo(), piMas.getInsurNo(), piMas.getMobile(), userorg.getPkOrg());
		if (sqlpi.size() > 0) {// 已存在
			
			throw new BusException("已存在该患者，自助开户不可更新。");
		} else {// 不存在
			String codePi = ApplicationUtils.getCode("0201");// 患者編碼
			piMas.setCodePi(codePi);
			DataBaseHelper.insertBean(piMas);
			// 创建账户,以及一卡通，pi_acc
			PiAcc acc = new PiAcc();
			acc.setPkPi(piMas.getPkPi());
			acc.setCodeAcc(piMas.getCodeIp());
			acc.setAmtAcc(BigDecimal.ZERO);
			acc.setCreditAcc(BigDecimal.ZERO);
			acc.setEuStatus("1");
			DataBaseHelper.insertBean(acc);
			// 创建一卡通pi_card
			PiCard piCard = new PiCard();
			piCard.setPkPi(piMas.getPkPi());
			piCard.setSortNo(1);//序号
			piCard.setDtCardtype(cardType);
			// 院内就诊卡不行
			String saveCard = "";
			if (IDictCodeConst.DT_CARDTYPE_YBK.equals(cardType)) {
				saveCard = piMas.getInsurNo();
			} else {
				saveCard = piMas.getIdNo();
			}
			piCard.setInnerNo(saveCard);
			piCard.setCardNo(saveCard);
			piCard.setDelFlag("0");
			piCard.setEuStatus("0");
			piCard.setFlagActive("1");
			DataBaseHelper.insertBean(piCard);
		}
		return piMas;
	}
	/**
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public SelfPatInfo getSelfpatino(String param, IUser user){
		String cardNo=JsonUtil.getFieldValue(param, "cardNo");
		User userAll=(User)user;
		String sql="";
		if(Application.isSqlServer()){
			sql="select top 1 pm.pk_pi,pm.name_pi,pe.code_pv,pe.pk_dept,pe.pk_dept_ns,pe.date_reg,pe.pk_pv,pa.pk_piacc,pa.amt_acc, pa.credit_acc, pa.eu_status ,AmtDepo.amount as AmtDepo"
					+ " from pi_card pc inner join pi_master pm on pm.pk_pi = pc.pk_pi inner join pi_acc pa on pa.pk_pi = pm.pk_pi inner join pv_encounter pe left JOIN ( SELECT SUM(AMOUNT) as amount, PK_PV  FROM BL_DEPOSIT"
					+ " WHERE  BL_DEPOSIT.EU_DPTYPE = '9' GROUP BY PK_PV) AmtDepo ON pe.PK_PV = AmtDepo.pk_pv on pe.pk_pi = pm.pk_pi"
					+ "and pe.eu_pvtype = '3' and pe.eu_status in ('0', '1')"
					+ "and pe.del_flag = '0'  where pc.eu_status in ('0', '1', '2')"
					+ "and pc.del_flag = '0' and pc.pk_org=? and pc.card_no=? order by pe.date_reg DESC";
		}else{
			sql="select A.*,(SELECT SUM(AMOUNT) FROM BL_DEPOSIT WHERE BL_DEPOSIT.PK_PV = A.PK_PV AND BL_DEPOSIT.EU_DPTYPE = '9') AmtDepo "
				+ "from (select pm.pk_pi,pm.name_pi,pe.code_pv,pe.pk_dept,pe.pk_dept_ns,pe.date_reg,pe.pk_pv,pa.pk_piacc,pa.amt_acc,pa.credit_acc,pa.eu_status"
				+"from pi_card pc inner join pi_master pm on pm.pk_pi=pc.pk_pi "+"inner join pi_acc pa on pa.pk_pi = pm.pk_pi "
				+"inner join pv_encounter pe on pe.pk_pi=pm.pk_pi and pe.eu_pvtype = '3' and pe.eu_status in ('0','1') and pe.del_flag = '0' "
				+"where pc.pk_org=? and pc.card_no=? and pc.eu_status in ('0','1','2') and pc.del_flag = '0' order by pe.date_reg desc ) "
				+ "A where rownum=1 ";}
		SelfPatInfo slef=DataBaseHelper.queryForBean(sql, SelfPatInfo.class, userAll.getPkOrg(),cardNo);
		return slef;
	} 
	/**
	 * 根据卡号获取卡信息 1.因为卡号在一定程度上是允许重复的,如果取出多条记录,就取状态正常的那条数据
	 * 2.返回卡信息和卡账户信息,优先考虑卡的授权账户 3.如果没有找到卡信息,卡类型是身份证或医保卡,就返回账户信息 4.返回加上授权人信息
	 * 
	 * 注：若授权账户被冻结，返回本账户信息
	 * @param param
	 *            cardNo，dtCardtype
	 * @param user
	 * @return
	 */
	public CardOrAccVo getCardInfos(String param, IUser user) {

		User uu = (User) user;
		CardOrAccVo ac = new CardOrAccVo();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String date=sdf.format(new Date());
		PiCard card = JsonUtil.readValue(param, PiCard.class);
		String getCard = "select * from pi_card where card_no=? and dt_cardtype=? "
				+ "and (del_flag='0' or del_flag is null) and "
				+ "(date_begin<to_date('"+date+"','YYYYMMDDHH24MISS') and "
						+ "date_end >to_date('"+date+"','YYYYMMDDHH24MISS') or date_begin is null and date_end is null) and pk_org='"
				+ uu.getPkOrg() + "'";
		List<Map<String, Object>> carlst = DataBaseHelper.queryForList(getCard, card.getCardNo(), card.getDtCardtype());
		if (carlst.size() >= 1) {
			for (Map<String, Object> acard : carlst) {
				String acc = "select * from pi_acc where pk_pi=? and (del_flag='0' or del_flag is null)";
				String mast = "select * from pi_master where pk_pi=? and (del_flag='0' or del_flag is null) and pk_org='" + uu.getPkOrg() + "'";
				if ("0".equals(acard.get("euStatus"))) {// 在用
					// 获取卡账户信息，优先获取授权账户信息
					String getpkPi = "select pas.* from pi_acc  pi inner"
							+ " join pi_acc_share pas on pi.pk_piacc=pas.pk_piacc where pas.pk_pi_use=? and (pas.del_flag='0' or pas.del_flag is null) and pas.pk_org='"
							+ uu.getPkOrg() + "'";
					List<PiAccShare> sharLst = DataBaseHelper.queryForList(getpkPi, PiAccShare.class, acard.get("pkPi").toString());
					// 当前卡信息
					ac.setCardNo(card.getCardNo());
					ac.setPkPi(acard.get("pkPi").toString());
					ac.setDtCardtype(acard.get("dtCardtype").toString());
					ac.setPkPicard(acard.get("pkPicard").toString());
					if (acard.get("dateBegin") != null) {
						ac.setDateBegin(acard.get("dateBegin").toString());
					}
					if (acard.get("dateEnd") != null) {
						ac.setDateEnd(acard.get("dateEnd").toString());
					}
					ac.setFlagActive(acard.get("flagActive").toString());
					ac.setEuCardStatus(acard.get("euStatus").toString());
					if (sharLst.size() == 1) {// 被授权人账户唯一，返回授权人账户信息
						CardOrAccVo ac1 = new CardOrAccVo();
						PiAcc shardin = DataBaseHelper.queryForBean(acc, PiAcc.class, sharLst.get(0).getPkPi());
						PiMaster master = DataBaseHelper.queryForBean(mast, PiMaster.class, sharLst.get(0).getPkPi());
						if (shardin != null) {
							// 授权账户信息
							ac1.setCodePi(master.getCodePi());
							ac1.setPkPiacc(shardin.getPkPiacc());
							ac1.setAmtAcc(shardin.getAmtAcc().toString());
							ac1.setCreditAcc(shardin.getCreditAcc().toString());
							ac1.setEuAccStatus(shardin.getEuStatus());
							ac1.setNamePi(master.getNamePi());
							if (master.getDtSex() != null)
								ac1.setDtSex(master.getDtSex());
							if (master.getBirthDate() != null)
								ac1.setBirthDate(master.getBirthDate());
							if (master.getPkPicate() != null)
								ac1.setPkPicate(master.getPkPicate());
							ac.setSqzhxx(ac1);// 授权人信息
							break;
						} else {// 本账户信息(有授权，无账户)
							PiAcc locaAcc = DataBaseHelper.queryForBean(acc, PiAcc.class, acard.get("pkPi").toString());
							PiMaster localmast = DataBaseHelper.queryForBean(mast, PiMaster.class, acard.get("pkPi").toString());
							ac.setCodePi(localmast.getCodePi());
							ac.setPkPiacc(locaAcc.getPkPiacc());
							ac.setAmtAcc(locaAcc.getAmtAcc().toString());
							ac.setCreditAcc(locaAcc.getCreditAcc().toString());
							ac.setEuAccStatus(locaAcc.getEuStatus());
							ac.setNamePi(localmast.getNamePi());
							if (localmast.getDtSex() != null)
								ac.setDtSex(localmast.getDtSex());
							if (localmast.getDtSex() != null)
								ac.setBirthDate(localmast.getBirthDate());
							if (localmast.getDtSex() != null)
								ac.setPkPicate(localmast.getPkPicate());
							ac.setMobile(localmast.getMobile());
							if("01".equals(localmast.getDtIdtype())){
								ac.setIdNo(localmast.getIdNo());
							}
							break;
						}
					} else if (sharLst.size() > 1) {
						throw new BusException("被授权账户含有多个授权账户信息!");
					} else {// <1 无授权账户
						PiAcc locaAcc = DataBaseHelper.queryForBean(acc, PiAcc.class, acard.get("pkPi").toString());
						PiMaster localmast = DataBaseHelper.queryForBean(mast, PiMaster.class, acard.get("pkPi").toString());
						ac.setCodePi(localmast.getCodePi());
						ac.setPkPiacc(locaAcc.getPkPiacc());
						ac.setAmtAcc(locaAcc.getAmtAcc().toString());
						ac.setCreditAcc(locaAcc.getCreditAcc().toString());
						ac.setEuAccStatus(locaAcc.getEuStatus());
						ac.setNamePi(localmast.getNamePi());
						if (localmast.getDtSex() != null)
							ac.setDtSex(localmast.getDtSex());
						if (localmast.getDtSex() != null)
							ac.setBirthDate(localmast.getBirthDate());
						if (localmast.getDtSex() != null)
							ac.setPkPicate(localmast.getPkPicate());
						ac.setMobile(localmast.getMobile());
						if("01".equals(localmast.getDtIdtype())){
							ac.setIdNo(localmast.getIdNo());
						}
						break;
					}
				} else {// 无有效卡 身份证或者医保卡
					if ("02".equals(card.getDtCardtype()) || "04".equals(card.getDtCardtype())) {
						PiAcc locaAcc = DataBaseHelper.queryForBean(acc, PiAcc.class, acard.get("pkPi").toString());
						PiMaster localmast = DataBaseHelper.queryForBean(mast, PiMaster.class, acard.get("pkPi").toString());
						ac.setCodePi(localmast.getCodePi());
						ac.setPkPiacc(locaAcc.getPkPiacc());
						ac.setAmtAcc(locaAcc.getAmtAcc().toString());
						ac.setCreditAcc(locaAcc.getCreditAcc().toString());
						ac.setEuAccStatus(locaAcc.getEuStatus());
						ac.setNamePi(localmast.getNamePi());
						if (localmast.getDtSex() != null)
							ac.setDtSex(localmast.getDtSex());
						if (localmast.getDtSex() != null)
							ac.setBirthDate(localmast.getBirthDate());
						if (localmast.getDtSex() != null)
							ac.setPkPicate(localmast.getPkPicate());
						ac.setMobile(localmast.getMobile());
						if("01".equals(localmast.getDtIdtype())){
							ac.setIdNo(localmast.getIdNo());
						}
					}
				}
			}
		} else {// 没有相关卡信息
			throw new BusException("没有相关卡信息或超出使用时间或已被删除");
		}
		return ac;
	}

	/**
	 * 预交金交费后产生一笔交款记录 bl_deposit
	 * @param param
	 * @param user
	 */
	public void createPayRecord(String param, IUser user) {

		BlDeposit record = JsonUtil.readValue(param, BlDeposit.class);
		DataBaseHelper.insertBean(record);
	}

	/**
	 * 问卷调查-问卷列表 f分页ResponseJson
	 * 
	 * @param param
	 *            cardNo
	 * @param user
	 * @return
	 */
	public Page<?> queryQesLst(String param, IUser user) {

		Page<?> page = null;
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String date=sdf.format(new Date());
		int pageSize = JSON.parseObject(param).getIntValue("pageSize");
		int pageIndex = JSON.parseObject(param).getIntValue("pageIndex");
		User uu = (User) user;
		String cardNo = JSON.parseObject(param).getString("cardNo");// 判断是患者还是用户
		// 患者(是否验证卡的状态信息？)
		String hzInfo = "select * from pi_card where card_no=? and (del_flag='0' or del_flag is null)  and eu_status='0' "
				+ "and date_begin<to_date('"+date+"','YYYYMMDDHH24MISS') "
				+ "and date_end>to_date('"+date+"','YYYYMMDDHH24MISS') and pk_org='"
				+ uu.getPkOrg() + "'";
		// 用户
		String yhInfo = "select * from bd_ou_employee where code_emp=? and (del_flag='0' or del_flag is null) and flag_active='1' and pk_org='" + uu.getPkOrg()
				+ "'";
		BdOuEmployee yhcount = DataBaseHelper.queryForBean(yhInfo, BdOuEmployee.class, cardNo);

		PiCard hzcount = DataBaseHelper.queryForBean(hzInfo, PiCard.class, cardNo);
		if (hzcount != null) {// 患者存在
			page = queryQuesForHzOrYh("0", hzcount.getPkPi(), uu.getPkOrg(), pageSize, pageIndex);
		} else if (yhcount != null) {// 用户存在
			page = queryQuesForHzOrYh("1", yhcount.getCodeEmp(), uu.getPkOrg(), pageSize, pageIndex);
		}
		return page;
	}

	/**
	 * 处理问卷列表
	 * @param euType
	 *            患者类型
	 * @param id
	 *            pk_pi 或者pk_emp
	 * @return
	 */
	private Page<?> queryQuesForHzOrYh(String euType, String id, String pkOrg, int pageSize, int pageIndex) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String date=sdf.format(new Date());
		// 查看是否指定
		List<String> hdlx = new ArrayList<String>();
		if ("0".equals(euType)) {// 患者
			hdlx.add("00");
			hdlx.add("01");
		} else if ("1".equals(euType)) {// 用户
			hdlx.add("00");
			hdlx.add("02");
		}
		String pusql = "select bts.pk_topicset,di.name_inv,di.date_begin,di.date_end,bts.name_schm,"
				+ "(case when di.quan_max>(select count(*) as sx from dp_inv_result "
				+ "where pk_dpinv=di.pk_dpinv) then '1' else '0' end)as sfsx,di.pk_dpinv,'" + id + "' as pk_target "
				+ "from dp_inv di inner join bd_topic_set bts on " + "di.pk_topicset=bts.pk_topicset " + "where di.del_flag='0' and "
				+ "di.eu_status='1' and di.date_begin <to_date('"+date+"','YYYYMMDDHH24MISS') and di.date_end> " + "to_date('"+date+"','YYYYMMDDHH24MISS') and di.pk_org=? ";
		String isorder = "";
		List<String> invIdLst = new ArrayList<String>();
		if(Application.isSqlServer()){
			invIdLst = selfMapper.getSqlSerDpInvPkLstOnAppoint(euType, id, pkOrg,date);
		}else{
			invIdLst = selfMapper.getDpInvPkLstOnAppoint(euType, id, pkOrg,date);
		}
		if (invIdLst.size() > 0) {// 有指定活动
			String ids = "";
			isorder = "and di.flag_range='1'and di.pk_dpinv in (";
			for (String one : invIdLst) {
				ids += ("'" + one + "',");
			}
			ids = ids.substring(0, ids.length() - 1);
			isorder += ids + ")";
		} else {// 无指定,查询符合条件的全部问卷
			isorder = "and (di.flag_range='0' or di.flag_range is null)";
		}
		String lxs = "";
		String euTypeStr = "and di.eu_tatype=? and di.dt_invtype in (";
		for (String one : hdlx) {
			lxs += ("'" + one + "',");
		}
		lxs = lxs.substring(0, lxs.length() - 1);
		euTypeStr += lxs + ")";
		String sql = pusql + isorder + euTypeStr;
		Page<?> page = DataBaseHelper.queryForPage(sql, pageIndex, pageSize, new Object[] { pkOrg, euType });
		return page;
	}

	/**
	 * 问卷调查-问卷题目及选项
	 * @param param
	 *            问卷id
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryAnsAndDet(String param, IUser user) {

		User uu = (User) user;
		String quesId = JSON.parseObject(param).getString("pkTopicset");
		String sql = "select * from bd_topic where pk_schm=? and (del_flag='0' or del_flag is null) and pk_org='" + uu.getPkOrg() + "'";
		List<Map<String, Object>> bdtpLst = DataBaseHelper.queryForList(sql, quesId);
		String choose = "select * from bd_topic_opt where pk_topic=? and (del_flag='0' or del_flag is null) and pk_org='" + uu.getPkOrg() + "'";
		if (bdtpLst.size() > 0) {
			for (Map<String, Object> dpc : bdtpLst) {
				List<BdTopicOpt> optLst = DataBaseHelper.queryForList(choose, BdTopicOpt.class, dpc.get("pkTpbank").toString());
				dpc.put("choose", optLst);
			}
		} else {
			throw new BusException("没有该问卷信息。");
		}
		return bdtpLst;
	}

	/**
	 * 问卷调查-保存调查结果 新增或者更新，校验结果表中完成标志为1的不可更新，标志为0的可以更新 保存的时候直接保存为已完成1，患者或者用户要么是提交
	 * ，要么重写；提交就是已完成，重写就不会提交。
	 * @param param
	 * @param user
	 *            DpInvResult
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void saveResults(String param, IUser user) {

		Map<String, Object> res = JsonUtil.readValue(param, Map.class);
		Map<String, String> rest = (Map<String, String>) res.get("dpInvResult");

		SimpleDateFormat aa = new SimpleDateFormat("yyyyMMdd");
		JSONObject js = JSONObject.parseObject(param);
		JSONArray jas = JSON.parseArray(js.getString("dpInvResultDts"));
		List<DpInvResultDt> restDts = JSON.parseArray(jas.toString(), DpInvResultDt.class);
		DpInvResult resSv = new DpInvResult();
		resSv.setFlagFin("1");
		resSv.setPkDpinv(rest.get("pkDpinv").toString());
		resSv.setPkTarget(rest.get("pkTarget").toString());
		resSv.setDateFin(aa.format(new Date()));
		resSv.setDelFlag("0");
		DataBaseHelper.insertBean(resSv);
		for (DpInvResultDt resDt : restDts) {
			resDt.setPkInvresult(resSv.getPkInvresult());
			resDt.setDelFlag("0");
			DataBaseHelper.insertBean(resDt);
		}
	}

	/**
	 * 门诊自助缴费查询服务
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BlPatiCgInfoNotSettleVO> qryPatiFeesWithPkpi(String param, IUser user) {

		// 支持单个号退
		Map<String, Object> mapParam = JsonUtil.readValue(param, HashMap.class);
		mapParam.put("pkOrg", ((User) user).getPkOrg());
		java.util.Date utilDate = new Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		mapParam.put("curDate", sqlDate);
		return opcgPubHelperService.queryPatiCgInfoNotSettle(mapParam);
	}

	/**
	 * 获取专家列表 以及获取相应的人员工作关系信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getProfersLstInfo(String param, IUser user) {

		User uu = (User) user;
		String pkDept = JSON.parseObject(param).getString("pkDept");
		String sql = "select boemp.* from bd_ou_empjob boe left join BD_OU_EMPLOYEE boemp on boemp.pk_emp =boe.pk_emp where  boe.pk_dept=? and boemp.flag_spec='1' and boemp.del_flag='0' and boemp.pk_org=? and boe .pk_org=? order by boe.pk_emp";
		List<Map<String, Object>> re = DataBaseHelper.queryForListFj(sql, pkDept, uu.getPkOrg(), uu.getPkOrg());
		String sqlRelationship = "select * from bd_ou_empjob where pk_emp=?";
		// 获取相应的工作关系
		// 相同科室，相同人员不同职位过滤
		String id = "";
		for (int i = re.size(); i > 0; i--) {
			Map<String, Object> map = new HashMap<String, Object>();
			map = re.get(i - 1);
			if (id.equals(map.get("pkEmp").toString())) {
				re.remove(i - 1);
				continue;
			} else {
				id = map.get("pkEmp").toString();
				List<Map<String, Object>> relships = DataBaseHelper.queryForList(sqlRelationship, map.get("pkEmp"));
				map.put("relationShips", relships);
			}
		}
		return re;
	}
	
	/**
	 * 读卡、输入手机号、输入密码 、发卡---自助发卡
	 * @param param
	 * @param user
	 * @return
	 */
	
	public void selfIssuedCard(String param, IUser user) {
		User userorg = (User) user;
		String cardType = JSON.parseObject(param).getString("dtCardtype");
		PiMaster piMaster = JsonUtil.readValue(param, PiMaster.class);
		PiMasterVo piMas = JsonUtil.readValue(param, PiMasterVo.class);
		List<PiMaster> sqlpi = selfMapper.getpiMasterByCredent(piMas.getDtIdtype(), piMas.getIdNo(), piMas.getInsurNo(), piMas.getMobile(), userorg.getPkOrg());
		if (sqlpi.size() > 0) {// 已存在
			
			throw new BusException("已存在该患者，请进行补卡操作。");
		} else {// 不存在
			 
			String codePi = ApplicationUtils.getCode("0201");// 患者編碼
			piMaster.setCodePi(codePi);
			DataBaseHelper.insertBean(piMaster);
			// 创建账户pi_acc
			PiAcc acc = new PiAcc();
			acc.setPkPi(piMaster.getPkPi());
			acc.setCodeAcc(piMaster.getCodeIp());
			acc.setAmtAcc(BigDecimal.ZERO);
			acc.setCreditAcc(BigDecimal.ZERO);
			acc.setEuStatus("1");
			DataBaseHelper.insertBean(acc);
			// 创建用户关联医疗卡信息pi_card
		    PiCard piCard = new PiCard();
			piCard.setPkPi(piMaster.getPkPi());
			piCard.setSortNo(1);//卡序号
			piCard.setDtCardtype(cardType);//卡类型
			piCard.setInnerNo(piMas.getCardNumber());
			piCard.setCardNo(piMas.getCardNumber());
			piCard.setDelFlag("0");
			piCard.setEuStatus("0");
			piCard.setFlagActive("1");
			//piCard.setPassWord(piMas.getPassWord());
			DataBaseHelper.insertBean(piCard);					               
			piPubCarddealService.saveCardInfosCoreMethod(user, piMaster.getPkPi(), piCard);
		}
	}
	/**
	 * 补卡操作
	 * 读卡、输入手机号、输入密码、发卡
	 * @param param
	 * @param user
	 * @return
	 */
	public void complementCardOperation(String param,IUser user){
		User userorg = (User) user;
		String cardType = JSON.parseObject(param).getString("dtCardtype");
		PiMasterVo piMas = JsonUtil.readValue(param, PiMasterVo.class);
		List<PiMaster> sqlpi = selfMapper.getpiMasterByCredent(piMas.getDtIdtype(), piMas.getIdNo(), piMas.getInsurNo(), piMas.getMobile(), userorg.getPkOrg());
		if (sqlpi.size() > 0) {// 已存在用户,创建新的用户卡信息、账户
		//获取用户患者主键	
		PiMaster pMaster=sqlpi.get(0);			
		String pkpi=pMaster.getPkPi();
		// 创建账户pi_acc
		PiAcc acc = new PiAcc();
		acc.setPkPi(pkpi);
		acc.setCodeAcc(piMas.getCodeIp());
		acc.setAmtAcc(BigDecimal.ZERO);
		acc.setCreditAcc(BigDecimal.ZERO);
		acc.setEuStatus("1");
		DataBaseHelper.insertBean(acc);
		// 创建用户关联医疗卡信息pi_card
	    PiCard piCard = new PiCard();
		piCard.setPkPi(pkpi);
		piCard.setSortNo(1);//卡序号
		piCard.setDtCardtype(cardType);//卡类型
		piCard.setInnerNo(piMas.getCardNumber());
		piCard.setCardNo(piMas.getCardNumber());
		piCard.setDelFlag("0");
		piCard.setEuStatus("0");
		piCard.setFlagActive("1");
		//piCard.setPassWord(piMas.getPassWord());
		DataBaseHelper.insertBean(piCard);								
		piPubCarddealService.saveCardInfosCoreMethod(user, pkpi, piCard);
			
		}else {
			throw new BusException("不存在该用户，请新办诊疗卡。");
		}
	}
	/**
	 * 社保卡激活
	 * 读卡、输入手机号、激活
	 * @param param
	 * @param user
	 * @return
	 */
	public void socialeCurityCardActivation(String param,IUser user){
		
		String cardType = JSON.parseObject(param).getString("dtCardtype");
		PiMaster piMas = JsonUtil.readValue(param, PiMaster.class);
		
		String sql = "select * from pi_master where insur_no = ? ";
		PiMaster piMaster= DataBaseHelper.queryForBean(sql, PiMaster.class, piMas.getInsurNo());
		if (piMaster == null) {
			//根据身份证号码获取患者信息
			String sqlIdno = "select * from pi_master where id_no = ? ";
			PiMaster piMasterIdno= DataBaseHelper.queryForBean(sqlIdno, PiMaster.class, piMas.getIdNo());
			if (piMasterIdno == null) {//如果为空则说明该患者在系统中未创建账户信息，则根据医保卡新建				
				String codePi = ApplicationUtils.getCode("0201");// 患者編碼
				piMas.setCodePi(codePi);
				DataBaseHelper.insertBean(piMas);
				// 创建账户pi_acc
				PiAcc acc = new PiAcc();
				acc.setPkPi(piMas.getPkPi());
				acc.setCodeAcc(piMas.getCodeIp());
				acc.setAmtAcc(BigDecimal.ZERO);
				acc.setCreditAcc(BigDecimal.ZERO);
				acc.setEuStatus("1");
				DataBaseHelper.insertBean(acc);
				// 创建卡信息pi_card
				PiCard piCard = new PiCard();
				piCard.setPkPi(piMas.getPkPi());
				piCard.setSortNo(1);//序号
				piCard.setDtCardtype(cardType);
				String saveCard = "";
				if (IDictCodeConst.DT_CARDTYPE_YBK.equals(cardType)) {
					saveCard = piMas.getInsurNo();
				} else {
					saveCard = piMas.getIdNo();
				}
				piCard.setInnerNo(saveCard);
				piCard.setCardNo(saveCard);
				piCard.setDelFlag("0");
				piCard.setEuStatus("0");
				piCard.setFlagActive("1");
				DataBaseHelper.insertBean(piCard);
			}else {//该患者已存在账户信息则进行账户关联
				String insurNo = piMas.getInsurNo();//医保卡号
				String pkpi = piMasterIdno.getPkPi();
				String sqlIdNum = "update pi_master set insur_no = ? where pk_pi = ? ";
				DataBaseHelper.update(sqlIdNum, insurNo,pkpi);
				// 根据医保卡创建卡信息pi_card
				PiCard piCard = new PiCard();
				piCard.setPkPi(piMas.getPkPi());
				piCard.setSortNo(1);//序号
				piCard.setDtCardtype(cardType);
				String saveCard = "";
				if (IDictCodeConst.DT_CARDTYPE_YBK.equals(cardType)) {
					saveCard = piMas.getInsurNo();
				} else {
					saveCard = piMas.getIdNo();
				}
				piCard.setInnerNo(saveCard);
				piCard.setCardNo(saveCard);
				piCard.setDelFlag("0");
				piCard.setEuStatus("0");
				piCard.setFlagActive("1");
				DataBaseHelper.insertBean(piCard);
				
			} 
			
		}else {// 根据医保卡号查到患者信息，则说明医保卡与患者已关联。此卡已激活
			throw new BusException("社保卡已激活，请进行其他操作");			
		    }
		
	 }
	
	/**
	 *住院费用查询
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BlIpDt> queryBidParam(String param,IUser user){
		CardAndPageInfo  cardAndPageInfo=JsonUtil.readValue(param, CardAndPageInfo.class);
		List<BlIpDt> blIpDts=null;
		User u = (User) user;
		if (Application.isSqlServer()) {
			blIpDts = selfMapper.getBlIpDtsSqlSer(cardAndPageInfo.getCardNo(),cardAndPageInfo.getDtCardtype(),cardAndPageInfo.getEndDate(),cardAndPageInfo.getBeginDate(),u.getPkOrg());
		} else {
			blIpDts = selfMapper.getBlIpDtsOracle(cardAndPageInfo.getCardNo(),cardAndPageInfo.getDtCardtype(),cardAndPageInfo.getEndDate(),cardAndPageInfo.getBeginDate(),u.getPkOrg());
		}
		return blIpDts;
	}
}
