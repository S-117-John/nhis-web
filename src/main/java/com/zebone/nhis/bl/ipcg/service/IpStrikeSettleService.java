package com.zebone.nhis.bl.ipcg.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.bl.pub.vo.SettleInfo;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class IpStrikeSettleService {
	
	@Autowired
	private IpSettleService ipSettleService;
	@Autowired
	private InvSettltService invSettltService;
	@Autowired
	private BlIpPubMapper blIpPubMapper;
	@Autowired
	private CommonService commonService;
	
	
	@SuppressWarnings("unchecked")
	public List<BlIpDt> qryRefunData(String param,IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		List<String> pkSettles = (List<String>)paraMap.get("pkSettles");
		String pkPv = (String)paraMap.get("pkPv");
		List<BlIpDt> list = new ArrayList<BlIpDt>();
		if(StringUtils.hasText(pkPv) && pkSettles!=null && pkSettles.size()>0){
			list = blIpPubMapper.getRefundData(paraMap);
		}				
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Map<String,Object>> qryStrikePatis(String param,IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		Collection<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(paraMap !=null){
			list = blIpPubMapper.qryStrikePatis(paraMap);
			Map<String,Map<String,Object>> temp =  new HashMap<String,Map<String,Object>>();
			for(Map<String,Object> map : list){
				String pkPv = Application.isSqlServer()?(String)map.get("pkPv"):(String)map.get("PKPV");				
				if(temp.get(pkPv)!=null){
					Timestamp dateSt1 =  Application.isSqlServer()?(Timestamp)map.get("dateSt"):(Timestamp)map.get("DATEST");
					Timestamp dateSt2 = Application.isSqlServer()?(Timestamp)temp.get(pkPv).get("dateSt"):(Timestamp)temp.get(pkPv).get("DATEST");
					if(!valiAfter(dateSt1,dateSt2)){
						temp.put(pkPv, map);
					}
					
				}else{
					temp.put(pkPv, map);
				}			
			}
			list = temp.values();
		}
		return list;
	}
	
	private boolean valiAfter(Timestamp date1,Timestamp date2){
		if(date1 !=null && date2!=null){
			return date2.after(date1);
		}else{
			return false;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public String strike(String param,IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkSettle = (String)paraMap.get("pkSettle");
		
		//1.参数接收		
		User userifo = (User)user;
		SettleInfo allData = JsonUtil.readValue(param, SettleInfo.class);		
		BlDeposit fromSettle = allData.getDeposit();
		String pkPv = allData.getPkPv();//
		String euSttype = allData.getEuSttype();
		String euStresult = allData.getEuStresult();
		String dateEnd = allData.getDateEnd();
		String flagHbPrint = allData.getFlagHbPrint();//是否合并特诊发票
		
		List<InvInfoVo> invoInfos = allData.getInvos();
		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, pkPv);
		String dateBegin =  ipSettleService.dateTrans(pvvo.getDateBegin());

		//1	基于新的记费和退费信息生成新的结算记录和结算明细，写表bl_settle（记录原结算主键）和bl_settle_ddetail；
		Map<String,Object> stDataMap = ipSettleService.settleData(allData, pkPv, euSttype, dateBegin, dateEnd, user, euStresult,null);
		List<BlSettleDetail> stDtVos = (List<BlSettleDetail>) stDataMap.get("detail");
		BlSettle stVo = (BlSettle)stDataMap.get("settle");
		stVo.setPkSettleRev(pkSettle);
		
		for(BlSettleDetail vo : stDtVos){
			vo.setPkStdt(NHISUUID.getKeyId());
		}
		DataBaseHelper.insertBean(stVo);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), stDtVos);

		//2	计算新增记费和退费金额合计，生成新的结算（eu_dptype=2）收付款记录，写表bl_deposit；
		if(fromSettle!=null){	
			fromSettle.setEuDptype("2");
			ApplicationUtils.setDefaultValue(fromSettle, true);
			fromSettle.setPkDepo(NHISUUID.getKeyId());
			fromSettle.setFlagSettle("1");
			fromSettle.setPkSettle(stVo.getPkSettle());
			fromSettle.setDateReptBack(null);
			
			DataBaseHelper.insertBean(fromSettle);				
			}
		//3	更新费用明细表bl_ip_st中新增记费和退费记录字段flag_settle=1和pk_settle=新结算主键；
		DataBaseHelper.execute("update bl_ip_dt set flag_settle = '1', pk_settle = ? where  flag_settle = '0' and pk_pv = ?",stVo.getPkSettle(),pkPv);	    
	    //4	作废被冲账结算关联的发票；	    
		List<BlInvoice> invs =
	    		DataBaseHelper.queryForList("select * from bl_invoice invo  "
	    		+ "where invo.pk_invoice in (select stInv.Pk_Invoice from bl_st_inv stInv where stInv.Pk_Settle = ? )",BlInvoice.class, pkSettle);
	    for(BlInvoice vo : invs){
	    	vo.setFlagCancel("1");
	    	vo.setDateCancel(new Date());
	    	vo.setNameEmpCancel(userifo.getNameEmp());
	    	vo.setPkEmpCancel(userifo.getPkEmp());
	    	DataBaseHelper.updateBeanByPk(vo,false);
	    }	    
	    //5	计算被冲账结算和本次结算费用，重新获取发票并生成发票信息和发票明细，写表bl_invoice和bl_invcoice_dt；
	    Map<String,Object> invMap = invSettltService.invoData(pkPv,pvvo.getFlagSpec(),invoInfos,user,dateBegin,dateEnd,stVo.getPkSettle(),flagHbPrint);	
		List<BlInvoice> invo = (List<BlInvoice>) invMap.get("inv");
		List<BlInvoiceDt> invoDt = (List<BlInvoiceDt>)invMap.get("invDt");
		ipSettleService.insertInvo(invo,invoDt);
		//6.写发票和结算关系表bl_st_inv；
		ipSettleService.insertInvoSt(invo,stVo);
		//7.发票确认使用
		commonService.confirmUseEmpInv(invo.get(0).getPkEmpinvoice(), new Long(invo.size()));
       return stVo.getPkSettle();
		}
	
	
	public Date dateTrans(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date temp = null;
		if(StringUtils.hasText(date)){
			try {
				temp = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return temp;
	}
	
	@SuppressWarnings("unchecked")
	public List<BlSettle> QryByPkSettles(String param,IUser user){
		List<BlSettle> res = new ArrayList<BlSettle>();
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		List<String> pks = (List<String>) paraMap.get("pkSettles");		
		if(pks!=null && pks.size()>0){
			for(String pk : pks){
				res.add(blIpPubMapper.QryBlSettleByPk(pk));	
			}		
		}		
		return res;
	}
   
}


