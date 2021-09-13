package com.zebone.nhis.pi.pub.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.acc.PiAccCredit;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pi.acc.PiAccShare;
import com.zebone.nhis.common.module.pi.acc.PiCardDetail;
import com.zebone.nhis.common.module.pi.acc.PiCardIss;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.pi.pub.dao.PiPubMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 患者卡信息处理，院内就诊卡需要收费并生成相应的账户费用信息
 * 患者账户在发卡之前就已经有其账户存在
 * @author L
 *
 */
@Service
public class PiPubCarddealService {
	
	@Autowired
	private PiPubMapper piPubMapper;
	/**
	 * 获取患者账户信息
	 * 要求：如果被查询患者有授权人信息，显示授权人信息，如果没有，显示自己的账户信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PiAcc> getPiAcc(String param,IUser user){
		List<PiAcc> retuO=new ArrayList<PiAcc>();
		String pkPi=JSON.parseObject(param).getString("pkPi");
		String sql="select * from pi_acc_share where pk_pi_use=? and del_flag='0'";
		List<PiAccShare> shre=DataBaseHelper.queryForList(sql, PiAccShare.class,new Object[]{pkPi});
		if(shre.size()>0){
			String ss="select * from pi_acc where pk_pi=?";
			List<PiAcc> pkcLst=new ArrayList<PiAcc>();
			for(PiAccShare pks:shre){
				PiAcc pkc=DataBaseHelper.queryForBean(ss, PiAcc.class,new Object[]{pks.getPkPi()});
				pkcLst.add(pkc);
			}
			retuO=pkcLst;
		}else{
			PiAcc pkc=DataBaseHelper.queryForBean("select * from pi_acc where pk_pi=?", PiAcc.class, pkPi);
			retuO.add(pkc);
		}
		return retuO;
	}
/**
 * 交易接口号：002004001006
 * 保存患者授权信息
 * 要求：被授权人能够授权给其他人，但是不能被授权（被授权人唯一）,不能授权给自己
 * @param param
 * @param zl
 */
	public void saveSharedAcc(String param,IUser user){
		Date time = new Date();
		PiAccShare piShare=JsonUtil.readValue(param, PiAccShare.class);
		String getPkpi="select * from pi_acc where pk_pi=?";
		String getSharLimt="select * from pi_acc_share where pk_pi_use=? and del_flag='0'";
		List<PiAccShare> i=DataBaseHelper.queryForList(getSharLimt,PiAccShare.class, piShare.getPkPiUse());
		if(i.size()>0){
			throw new BusException("被授权账户已被授权给其他账户，请重新选择。");
		}else if(piShare.getPkPi().equals(piShare.getPkPiUse())){
			throw new BusException("授权人不能授权给自己！");
		}
		PiAcc piPkacc=DataBaseHelper.queryForBean(getPkpi, PiAcc.class, piShare.getPkPi());
		piShare.setPkPiacc(piPkacc.getPkPiacc());
		piShare.setDateOpera(time);
		piShare.setPkEmpOpera(((User)user).getPkEmp());
		piShare.setNameEmpOpera(((User)user).getNameEmp());
		piShare.setModifier(((User)user).getPkEmp());
		piShare.setModityTime(time);
		DataBaseHelper.insertBean(piShare);
	}
	/**
	 * 交易接口号：002004001008
	 * 发卡数据处理
	 * 另外，如果是发卡类型为01 ，医院就诊卡的时候，需要交纳押金，做处理
	 * 冻结账户不可操作
	 * 查询病人的卡，设置卡的序号
	 * 
	 * 校验卡号：
	 * 1.对于本账户信息，要求非作废的卡号不可重复，作废状态的卡号可重复
	 * 2.不是本账户的人，要求不可重复卡号
	 * @param param
	 * @author zl
	 */
	
	public void saveCardInfosCoreMethod(IUser user, String pkpi, PiCard picard) {
		String ckCardNOInScop="select * from pi_card_iss where flag_use=1 and "
				+ "flag_active=1 and eu_status=1 and pk_org=?";
		List<PiCardIss> piCardIss=DataBaseHelper.queryForList(ckCardNOInScop,PiCardIss.class, ((User)user).getPkOrg());
		if(IDictCodeConst.DT_CARDTYPE_HOS.equals(picard.getDtCardtype())){
			if(piCardIss.size()!=1){
				throw new BusException("领卡表维护错误，请修改。");
			}else{//唯一一条
				if(piCardIss.get(0).getCntUse()<=0)
					{throw new BusException("剩余卡不足。");}
				//校验是否在范围内
				if(piCardIss.get(0).getBeginNo()==null){
					piCardIss.get(0).setBeginNo(0l);
				}
				if(!(Integer.valueOf(picard.getCardNo())>=piCardIss.get(0).getBeginNo()&&
						Integer.valueOf(picard.getCardNo())<=piCardIss.get(0).getEndNo())){
					throw new BusException("不在卡号范围内，无法发卡。");
				}
			}
		}
		
		/*String yz="select * from pi_card where card_no=? and pk_org=?";
		List<PiCard> yzLst=DataBaseHelper.queryForList(yz, PiCard.class,picard.getCardNo(),((User)user).getPkOrg());
		if(yzLst.size()>0){//数据库中存在卡号，校验，不存在的话可执行发卡操作
			boolean accountCon=true;
			List<String> lStats=new ArrayList<String>();
			String onez=yzLst.get(0).getPkPi();
			for(int i=0;i<yzLst.size();i++){
				if(!onez.equals(yzLst.get(i).getPkPi())){
					accountCon=false;//数据库存在不同账户
					//数据库已存在不同账户不执行发卡
				}else{
					lStats.add(yzLst.get(i).getEuStatus());
				}
			}
			if(accountCon){//同一账户,
				if(!pkpi.equals(yzLst.get(0).getPkPi())){//校验当前账户是否相同
					throw new BusException("已有其他账户拥有该卡！");//否相同
				}else{//相同，校验作废卡号（非作废不可重复）
					if(lStats.contains(EnumerateParameter.ZERO)||lStats.contains(EnumerateParameter.ONE)||lStats.contains(EnumerateParameter.TWO)){
						throw new BusException("已存在有效的该卡信息，不可再次添加！");
					}
				}
			}else{
				throw new BusException("已有多个账户拥有该卡，数据错误！");
			}
		}*/
		//以上都以抛出异常信息为不执行一下操作，若要求不抛出异常信息，需要增加变量加以判断
		String getXh="select * from pi_card where pk_pi=? and pk_org=?";
		List<PiCard> xhLst=DataBaseHelper.queryForList(getXh, PiCard.class,pkpi,((User)user).getPkOrg() );
		int maxNo=0;
		for(PiCard no:xhLst){
			if(no.getSortNo()>=maxNo){
				maxNo=no.getSortNo();
			}
		}
		maxNo++;
		picard.setSortNo(maxNo);//序号
		picard.setFlagActive(EnumerateParameter.ONE);
		
		//picard.setEuStatus(EnumerateParameter.ZERO);
		//DataBaseHelper.insertBean(picard);
		DataBaseHelper.updateBeanByPk(picard);
		PiCardDetail piCardDetail=new PiCardDetail();
		piCardDetail.setPkEmpOpera(((User)user).getPkEmp());
		piCardDetail.setNameEmpOpera(((User)user).getNameEmp());
		piCardDetail.setDateHap(new Date());
		piCardDetail.setModifier(((User)user).getPkEmp());
		piCardDetail.setPkPicard(picard.getPkPicard());
		piCardDetail.setPkPi(picard.getPkPi());
		piCardDetail.setCardNo(picard.getCardNo());
		piCardDetail.setEuOptype(EnumerateParameter.ZERO);
		DataBaseHelper.insertBean(piCardDetail);
		//医院就诊卡，插入交款记录信息
		/*if(IDictCodeConst.DT_CARDTYPE_HOS.equals(picard.getDtCardtype())){
			//获取押金金额
			String jeVal=ApplicationUtils.getSysparam("PI0002",false);
			String piAccInfo="select * from pi_acc where pk_pi=? ";
			PiAcc  pia=DataBaseHelper.queryForBean(piAccInfo, PiAcc.class, pkpi);
			if(EnumerateParameter.ONE.equals(pia.getEuStatus())){
				double ye=pia.getAmtAcc()+Double.valueOf(jeVal);
				pia.setAmtAcc(ye);
				DataBaseHelper.updateBeanByPk(pia, false);
				//插入交付款记录
				BlDepositPi jkjl=new BlDepositPi();
				jkjl.setEuDirect(EnumerateParameter.ONE);
				jkjl.setPkPi(pkpi);
				jkjl.setAmount(Double.valueOf(jeVal));
				jkjl.setDatePay(new Date());
				jkjl.setPkDept(((User)user).getPkDept());
				jkjl.setPkEmpPay(((User)user).getPkEmp());
				jkjl.setNameEmpPay(((User)user).getNameEmp());
				jkjl.setDtPaymode(EnumerateParameter.ONE);
				DataBaseHelper.insertBean(jkjl);
				piAccDetailVal(pia,jkjl);	
			}else if(EnumerateParameter.TWO.equals(pia.getEuStatus())){
				throw new BusException("该账户已被冻结，院内就诊卡不可发放！");
			}else{
				throw new BusException("账户数据错误");
			}
		}*/
		if(IDictCodeConst.DT_CARDTYPE_HOS.equals(picard.getDtCardtype())&&piCardIss.size()>0){
		//更新领卡信息表
		piCardIss.get(0).setCntUse(piCardIss.get(0).getCntUse()-1);//更新剩余卡数
		if(piCardIss.get(0).getCntUse()==0){
			piCardIss.get(0).setFlagUse(EnumerateParameter.ZERO);
			piCardIss.get(0).setFlagActive(EnumerateParameter.ZERO);
			piCardIss.get(0).setEuStatus(EnumerateParameter.TWO);
		}else{
			piCardIss.get(0).setCurNo(Long.valueOf(picard.getCardNo())+1);
		}
		DataBaseHelper.updateBeanByPk(piCardIss.get(0),false);}
	}
	/**
	 * 交易号：002004001011
	 * 患者退卡
	 * 	1.只有卡片状态不是"作废"状态的才可以退卡
		2.将卡片状态改成退卡
		3.如果卡片类型 为"01" 院内就诊卡时,需要退给他押金,押金金额取PI0002参数
		4.冻结账户不可操作
		注：如果发卡后一段时间后改了押金金额，再次退卡的时候金额就不一样了
	 */
	public void backCard(String param , IUser user){
		User uu=(User)user;
		String pkpiCard=JSON.parseObject(param).getString("pkPiCard");
		String sqlForcard="select * from pi_card where pk_picard=? and pk_org=?";
		PiCard pic=DataBaseHelper.queryForBean(sqlForcard, PiCard.class, pkpiCard,uu.getPkOrg());
		if(pic!=null&&!EnumerateParameter.EIGHT.equals(pic.getEuStatus())&&!EnumerateParameter.NINE.equals(pic.getEuStatus())){
			pic.setEuStatus(EnumerateParameter.EIGHT);//退卡
			if(IDictCodeConst.DT_CARDTYPE_HOS.equals(pic.getDtCardtype())){
				String yj=ApplicationUtils.getSysparam("PI0002",false);
				String piAccInfo="select * from pi_acc where pk_pi=? ";
				PiAcc  pia=DataBaseHelper.queryForBean(piAccInfo, PiAcc.class, pic.getPkPi());
				if(pia.getEuStatus()==null||EnumerateParameter.ONE.equals(pia.getEuStatus())){
					BigDecimal nowac=BigDecimal.ZERO;
					BigDecimal s=pia.getAmtAcc();
					BigDecimal yajin = new BigDecimal(yj);
					if(s.compareTo(yajin)<0){
						throw new BusException("账户余额小于押金金额");
					}else{
						nowac=s.add(yajin);
					}
					pia.setAmtAcc(nowac);//账户
					BlDepositPi jkjl=new BlDepositPi();
					jkjl.setEuDirect(EnumerateParameter.NEGA);
					jkjl.setPkPi(pic.getPkPi());
					jkjl.setAmount(yajin);
					jkjl.setDatePay(new Date());
					jkjl.setPkDept(((User)user).getPkDept());
					jkjl.setPkEmpPay(((User)user).getPkEmp());
					jkjl.setNameEmpPay(((User)user).getNameEmp());
					jkjl.setDtPaymode(EnumerateParameter.ONE);
					DataBaseHelper.insertBean(jkjl);
					DataBaseHelper.updateBeanByPk(pia, false);
					piAccDetailVal(pia,jkjl);
				}else{
					throw new BusException("该病人账户已冻结，无法退卡！");
				}
			}
			DataBaseHelper.updateBeanByPk(pic, false);
			//更新卡操作记录-退卡
			DataBaseHelper.update("update pi_card_detail set eu_optype='8' where pk_picard=?", new Object[]{pic.getPkPicard()});
		}
	}
	/**
	 *  交易号：002004001012
	 *  1.注意判断当前卡状态
		2.卡操作流水
	 * @param pkpicard，status
	 * @param user
	 */
	public void changeCardStatus(String param , IUser user){
		User uu=(User)user;
		String pkpiCard=JSON.parseObject(param).getString("pkPiCard");
		String status=JSON.parseObject(param).getString("status");
		//查询当前卡状态
		
		String cardsql="select * from pi_card where pk_picard=? and pk_org=?";
		PiCard card= DataBaseHelper.queryForBean(cardsql, PiCard.class, pkpiCard,uu.getPkOrg());
		if(EnumerateParameter.ZERO.equals(card.getEuStatus())){
			if(EnumerateParameter.NINE.equals(status)||EnumerateParameter.ONE.equals(status)||EnumerateParameter.TWO.equals(status)||EnumerateParameter.EIGHT.equals(status)){
				card.setEuStatus(status);	
			}
		}else if(EnumerateParameter.ONE.equals(card.getEuStatus())){
			if(EnumerateParameter.NINE.equals(status)||EnumerateParameter.ZERO.equals(status)||EnumerateParameter.TWO.equals(status)){
				card.setEuStatus(status);	
			}else{
				throw new BusException("该卡处于挂失状态，不可退卡！");
			}
		}else if(EnumerateParameter.TWO.equals(card.getEuStatus())){
			if(EnumerateParameter.EIGHT.equals(status)||EnumerateParameter.NINE.equals(status)){
				card.setEuStatus(status);	
			}
			else if(EnumerateParameter.ZERO.equals(card.getEuStatus())){
				throw new BusException("该卡已到期，不可使用该卡！");
			}else{
				throw new BusException("该卡已到期，不可挂失！");
			}
		}
		DataBaseHelper.updateBeanByPk(card, false);
		//DataBaseHelper.update("update pi_card_detail set eu_optype='"+status+"' where pk_picard=?", new Object[]{card.getPkPicard()});
		PiCardDetail pcd = new PiCardDetail();
		pcd.setPkPicard(card.getPkPicard());
		pcd.setPkPi(card.getPkPi());
		pcd.setCardNo(card.getCardNo());
		//如果参数 status 为0,说明是启用状态（该处不存在发卡状态）
		if(PiCardDetail.CARD_EU_OPTYPE_0.equals(status)){
			status = PiCardDetail.CARD_EU_OPTYPE_2;
		}
		pcd.setEuOptype(status);
		pcd.setPkEmpOpera(uu.getPkEmp());
		pcd.setNameEmpOpera(uu.getNameEmp());
		pcd.setDateHap(new Date());
		DataBaseHelper.insertBean(pcd);
	}
	/**
	 * 交易号：002004001013
	 * 获取当前收据号
	 * @param param
	 * @param user
	 * @return
	 */
	public String getNowSjh(String param , IUser user){
		return ApplicationUtils.getCode("0603");//收据号
	}
	/**
	 * 交易号：002004001014
	 * 新增信用额度
	 * 冻结账户不可操作
	 * @param param
	 * @param user
	 */
	public void saveNeCredit(String param , IUser user){
		PiAccCredit piAccd=JsonUtil.readValue(param, PiAccCredit.class);
		//获取账户id
		String getaccId="select * from pi_acc where pk_pi=?";
		PiAcc pk_acc=DataBaseHelper.queryForBean(getaccId,PiAcc.class ,piAccd.getPkPi());
		if(pk_acc.getEuStatus()==null||EnumerateParameter.ONE.equals(pk_acc.getEuStatus())){
			BigDecimal leftamt=BigDecimal.ZERO;
			if(pk_acc.getCreditAcc()==null||"".equals(pk_acc.getCreditAcc())){
				pk_acc.setCreditAcc(BigDecimal.ZERO);
			}
			leftamt=pk_acc.getCreditAcc().add(piAccd.getAmtCredit());
			if(leftamt.compareTo(BigDecimal.ZERO)<0){
				throw new BusException("信用额度小于0！");
			}else{
				pk_acc.setCreditAcc(leftamt);
			}
			DataBaseHelper.updateBeanByPk(pk_acc,false);
			piAccd.setPkPiacc(pk_acc.getPkPiacc().toString());
			piAccd.setPkEmpOpera(((User)user).getPkEmp());
			piAccd.setNameEmpOpera(((User)user).getNameEmp());
			piAccd.setDateOpera(new Date());
			DataBaseHelper.insertBean(piAccd);
		}else{
			throw new BusException("该账户已冻结，不可新增信用");
		}
	}
	
	/**
	 * 交易接口号：002004001015
	 * 新增收退款
	 * 冻结账户不可操作
	 * 往pi_acc,pi_acc_detail,bl_deposit_pi表里面插入数据
	 * @param param
	 * @param user
	 */
	public void saveMonOperation(String param,IUser user){
		String euopType=JsonUtil.getFieldValue(param, "euOptype");
		String dtPaymode=JsonUtil.getFieldValue(param, "dtPaymode");
		BlDepositPi dp=JsonUtil.readValue(param, BlDepositPi.class);
		BalAccoutService jsservice=new BalAccoutService();
		jsservice.saveMonOperation(dp,user,null,euopType,dtPaymode);
	}
	/**
	 * 交易号：002004001017
	 * 患者账户冻结和解冻
	 * 如果eu_status字段为空，默认设置为1 ： 有效
	 * @param param
	 * @param user
	 */
	public void changeAccStatus(String param,IUser user){
		PiAcc piac=JsonUtil.readValue(param, PiAcc.class);
		String etAcc="select * from pi_acc where pk_pi=?";
		PiAcc dbAcc=DataBaseHelper.queryForBean(etAcc, PiAcc.class, piac.getPkPi());
		if(dbAcc.getEuStatus()==null||"".equals(dbAcc.getEuStatus()))dbAcc.setEuStatus(EnumerateParameter.ONE);
		if(EnumerateParameter.NINE.equals(dbAcc.getEuStatus())){//作废
			throw new BusException("该账户已作废，不可做解冻，冻结操作！");
		}else if(dbAcc.getEuStatus().equals(piac.getEuStatus())){
			if(EnumerateParameter.ONE.equals(dbAcc.getEuStatus())){
				throw new BusException("该账户已为有效状态，无需解冻！");
			}else if(EnumerateParameter.TWO.equals(dbAcc.getEuStatus())){
				throw new BusException("该账户已为冻结状态，无需再次冻结！");
			}
		}
		dbAcc.setEuStatus(piac.getEuStatus());
		DataBaseHelper.updateBeanByPk(dbAcc,false);
	}
	/**插入卡详细信息押金值
	 * @param pia
	 * @param jkjl
	 */
	public static void piAccDetailVal(PiAcc pia ,BlDepositPi jkjl){
		PiAccDetail pad=new PiAccDetail();
		pad.setPkPiacc(pia.getPkPiacc());
		pad.setPkPi(pia.getPkPi());
		pad.setDateHap(new Date());
		pad.setEuOptype(EnumerateParameter.ONE);
		pad.setEuDirect(jkjl.getEuDirect());
		pad.setAmount(jkjl.getAmount());
		pad.setPkDepopi(jkjl.getPkDepopi());
		pad.setAmtBalance(pia.getAmtAcc());
		pad.setPkEmpOpera(jkjl.getPkEmpPay());
		pad.setNameEmpOpera(jkjl.getNameEmpPay());
		DataBaseHelper.insertBean(pad);
		}
	/**查询可用的领卡信息*/
	public List<PiCardIss> getPiCardIss(String param, IUser user){
		PiCardIss cardiss = JsonUtil.readValue(param, PiCardIss.class);
		List<PiCardIss> list = piPubMapper.getPiCardIss(cardiss);
		return list;
	}
}















