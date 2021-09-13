package com.zebone.nhis.pro.zsba.compay.ins.pub.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.dao.BlIpSettleQryMapper;
import com.zebone.nhis.bl.pub.service.InvMagService;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BillInfo;
import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.bl.BlAmtVo;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.InvItemVo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.pro.zsba.compay.ins.pub.dao.InsPubIpSettleQryMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.HosInitialData;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class InvPubInvSettltService {
	
	@Autowired
	private InsPubIpSettleQryMapper insPubIpSettleQryMapper; 
	
	@Autowired
	private BlIpSettleQryMapper blIpSettleQryMapper;
	
	private Map<String, Object> handleDate(List<InvInfoVo> invoInfos, User user, String pkPv,String dateBegin, String dateEnd) {
		
		List<BlInvoice> invs = new ArrayList<BlInvoice>();
		List<BlInvoiceDt> invDts = new ArrayList<BlInvoiceDt>();
		Map<String,Object> res = new HashMap<String,Object>();
		BlAmtVo amtRe = insPubIpSettleQryMapper.QryAmtAndPi(pkPv, dateBegin, dateEnd,null);
		double amt = 0.0;
		double amtPi = 0.0;
		if(amtRe !=null ){
			 amt  = amtRe.getAmtSum();
			  amtPi  = amtRe.getAmtPi();
		}
		
	    Double rate = 1.0;
		if(amt<=0){
			res.put("inv", invs);
			res.put("invDt", invDts);
			return res;
		}
		for(InvInfoVo vo : invoInfos){
			rate  = MathUtils.div(vo.getAmount().doubleValue(), amt);
			BlInvoice inv = new BlInvoice();
			inv.setPkInvoice(NHISUUID.getKeyId());
			inv.setPkOrg(user.getPkOrg());
			inv.setPkInvcate(vo.getInv().getPkInvcate());
			inv.setPkEmpinvoice(vo.getInv().getPkEmpinv());
			inv.setCodeInv(vo.getInv().getCurCodeInv());
			inv.setDateInv(new Date());
			
			inv.setAmountInv(vo.getAmount().doubleValue());
			double amt_pi = MathUtils.mul(rate, amtPi);
			inv.setAmountPi(amt_pi);
			inv.setNote("住院发票"+vo.getNote());
			inv.setPkEmpInv(user.getPkEmp());
			inv.setNameEmpInv(user.getNameEmp());
			inv.setPrintTimes(1);
			inv.setFlagCancel("0");
			inv.setDateCancel(null);
			inv.setFlagCc("0");
			inv.setFlagCcCancel("0");
			invs.add(inv);
			
			List<InvItemVo> itemVos = blIpSettleQryMapper.QryInvItemInfo(pkPv,UserContext.getUser().getPkOrg(), 
						DateUtils.dateToStr("yyyyMMddHHmmss", vo.getDateSplitBegin()),DateUtils.dateToStr("yyyyMMddHHmmss", vo.getDateSplitEnd()), null,null);
			
			for(InvItemVo invItem : itemVos){
				BlInvoiceDt invDt = new BlInvoiceDt();
				invDt.setPkInvoicedt(NHISUUID.getKeyId());
				invDt.setPkInvoice(inv.getPkInvoice());
				invDt.setPkBill(invItem.getPkInvcateitem());
				invDt.setNameBill(invItem.getName());
				invDt.setCodeBill(invItem.getCode());
				invDt.setAmount(invItem.getAmount());				
				invDts.add(invDt);
 			}
			
		}
		res.put("inv", invs);
		res.put("invDt", invDts);
		return res;
	}

	/**
	 * 判断患者该打印什么医保报表
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> ifInsReportFormType(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkSettle = jo.getString("pkSettle");
		String pkPv = jo.getString("pkPv");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("type", "0");//0不需要打印
		//查询患者医保主计划编码
		Map<String, Object> insuMap = DataBaseHelper.queryForMap("select hp.code from pv_encounter pe inner join bd_hp hp on pe.pk_insu = hp.pk_hp where pe.pk_pv = ?", pkPv);
		if(insuMap!=null && insuMap.get("code")!=null && insuMap.get("code").toString().trim().length()!=0){
			String code = insuMap.get("code").toString();
			if(code.equals("00021")||code.equals("00023")||code.equals("00024")||code.equals("00025")||code.equals("00022")){
				//中山医保判断是普通报表还是优抚双低报表
				StringBuffer sb = new StringBuffer("select st.dbdxlb, st.yfdxlb, pv.jzjlh from ins_pv pv inner join ins_st st on pv.pk_inspv = st.pk_inspv");
				sb.append(" where st.pk_settle = ?");
				Map<String, Object> ybMap = DataBaseHelper.queryForMap(sb.toString(), pkSettle);
				if(ybMap!=null && ybMap.get("jzjlh")!=null&&ybMap.get("jzjlh").toString().length()!=0){
					if((ybMap.get("dbdxlb")!=null && ybMap.get("dbdxlb").toString().length()!=0) ||
							(ybMap.get("yfdxlb")!=null && ybMap.get("yfdxlb").toString().length()!=0)){
						//优抚双低报表
						returnMap.put("type", "2");
					}else{
						//普通医保报表
						returnMap.put("type", "1");
					}
				}
			}else if(code.equals("00031")){
				//省内普通医保
				StringBuffer sb = new StringBuffer("select pk_insst from ins_st_snyb");
				sb.append(" where pk_settle = ?");
				Map<String, Object> ybMap = DataBaseHelper.queryForMap(sb.toString(), pkSettle);
				if(ybMap!=null && ybMap.get("pkInsst")!=null&&ybMap.get("pkInsst").toString().length()!=0){
					returnMap.put("type", "3");
				}
			}else if(code.equals("00032")){
				//省内工伤医保
				StringBuffer sb = new StringBuffer("select pk_insstwi from ins_st_wi");
				sb.append(" where pk_settle = ?");
				Map<String, Object> ybMap = DataBaseHelper.queryForMap(sb.toString(), pkSettle);
				if(ybMap!=null && ybMap.get("pkInsstwi")!=null&&ybMap.get("pkInsstwi").toString().length()!=0){
					returnMap.put("type", "4");
				}
			}else if(code.equals("0004")){
				//跨省医保
				StringBuffer sb = new StringBuffer("select pk_insst from ins_st_ksyb");
				sb.append(" where pk_settle = ?");
				Map<String, Object> ybMap = DataBaseHelper.queryForMap(sb.toString(), pkSettle);
				if(ybMap!=null && ybMap.get("pkInsst")!=null&&ybMap.get("pkInsst").toString().length()!=0){
					returnMap.put("type", "5");
				}
			}else if(code.equals("00051")||code.equals("00052")||code.equals("00053")||code.equals("00054")||code.equals("00058")||code.equals("00059")){
				//全国医保
				StringBuffer sb = new StringBuffer("select pk_insstqg from ins_st_qg");
				sb.append(" where pk_settle = ?");
				Map<String, Object> ybMap = DataBaseHelper.queryForMap(sb.toString(), pkSettle);
				if(ybMap!=null && ybMap.get("pkInsstqg")!=null&&ybMap.get("pkInsstqg").toString().length()!=0){
					returnMap.put("type", "6");
				}
			}
		}
		return returnMap;
	}

}
