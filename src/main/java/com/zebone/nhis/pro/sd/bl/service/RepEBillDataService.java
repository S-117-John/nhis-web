package com.zebone.nhis.pro.sd.bl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.sd.bl.dao.RepEBillDataMapper;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.vo.BillInfo;
import com.zebone.nhis.common.module.bl.BlEmpInvoice;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RepEBillDataService {

	@Autowired
	private RepEBillDataMapper repEBillDataMapper;

	@Autowired
	private InvSettltService invSettltService;

	/**
	 * 交易号：007003008015
	 * 查询结算发票信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryStInvInfo(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> res = repEBillDataMapper.qryStInvInfo(params);
		Integer pageNum = CommonUtils.getInteger(MapUtils.getString(params,"pageIndex"));
        Integer pageSize = CommonUtils.getInteger(MapUtils.getString(params,"pageSize"));
        Map<String,Object> resMap = new HashMap<String, Object>();
        resMap.put("paramList", CommonUtils.startPage(res,pageNum,pageSize));
        resMap.put("total", res.size());
		return resMap;
	}

	/**
	 * 交易号：007003008016
	 * 批量开立电子票据
	 * @param param
	 * @param user
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String drawEbill(String param,IUser user){
		Map<String, Object> params = getParam(param);

		//查询票据详细信息
		List<Map<String,Object>> res = repEBillDataMapper.qryStInvInfo(params);

		StringBuffer msg = new StringBuffer("");

		List<BlInvoice> upInvList = new ArrayList<>();

		for(Map<String,Object> invMap : res){
			if(invMap!=null && invMap.size()>0){
//				if("1".equals(CommonUtils.getString(invMap.get("flagCancel")))){
//					msg.append("姓名："+CommonUtils.getString(invMap.get("namePi"))+",发票号码："+CommonUtils.getString(invMap.get("codeInv"))+",票据已作废，无法补传电子票据信息!\r\n");
//					continue;
//				}

				try{
					Map<String, Object> invRet = null;
					if("00".equals(CommonUtils.getPropValueStr(invMap, "dtSttype"))) {
						invRet = invSettltService.repEBillRegistration( CommonUtils.getString(invMap.get("pkPv")), user, CommonUtils.getString(invMap.get("pkSettle")));
					}else {
						if("3".equals(CommonUtils.getPropValueStr(invMap, "euPvtype"))) {
							invRet = invSettltService.repEBillOutpatient( CommonUtils.getString(invMap.get("pkPv")), user, CommonUtils.getString(invMap.get("pkSettle")));
						}else {
							invRet = invSettltService.repEBillMzOutpatient(CommonUtils.getString(invMap.get("pkPv")), user, CommonUtils.getString(invMap.get("pkSettle")));
						}

					}


					if(invRet!=null && invRet.size()>0 && invRet.containsKey("inv")){
						List<BlInvoice> invo = (List<BlInvoice>) invRet.get("inv");

						//组织电子票据信息
						if(invo!=null && invo.size()>0){
							//查询该票据详细信息
							BlInvoice invInfo = DataBaseHelper.queryForBean(
									"select * from bl_invoice where pk_invoice = ?",
									BlInvoice.class, new Object[]{invMap.get("pkInvoice")});

							invInfo.setEbillbatchcode(invo.get(0).getEbillbatchcode());
							invInfo.setEbillno(invo.get(0).getEbillno());
							invInfo.setCheckcode(invo.get(0).getCheckcode());
							invInfo.setDateEbill(invo.get(0).getDateEbill());
							invInfo.setUrlEbill(invo.get(0).getUrlEbill());
							invInfo.setUrlNetebill(invo.get(0).getUrlNetebill());
							invInfo.setPkEmpEbill(invo.get(0).getPkEmpEbill());
							invInfo.setNameEmpEbill(invo.get(0).getNameEmpEbill());
							invInfo.setQrcodeEbill(invo.get(0).getQrcodeEbill());
							invInfo.setFlagCcEbill("0");
							invInfo.setFlagCcCancelEbill("0");
							invInfo.setFlagCancelEbill("0");

							upInvList.add(invInfo);
						}

					}
				}catch(Exception e){
					msg.append("姓名："+CommonUtils.getString(invMap.get("namePi"))+",发票号码："+CommonUtils.getString(invMap.get("codeInv"))+","+e.getMessage()+"!\r\n");
				}
			}
		}

		//集合不为空时批量修改票据信息
		if(upInvList!=null && upInvList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlInvoice.class), upInvList);
		}

		return msg.toString();
	}

	/**
	 * 交易号：007003008017
	 * 批量换开纸质票据
	 * @param param
	 * @param user
	 * @return
	 */
	public String drawPaperBill(String param,IUser user){
		List<String> pkList = JsonUtil.readValue(param,ArrayList.class);

		if(pkList==null || pkList.size()<=0){
			throw new BusException("请勾选发票信息！");
		}

		//查询票据详细信息
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("pkInvList", pkList);
		List<Map<String,Object>> res = repEBillDataMapper.qryStInvInfo(params);

		StringBuffer msg = new StringBuffer("");

		for(Map<String,Object> invMap : res){
			if(invMap!=null && invMap.size()>0){
//				if("1".equals(CommonUtils.getString(invMap.get("flagCancel")))){
//					msg.append("姓名："+CommonUtils.getString(invMap.get("namePi"))+",发票号码："+CommonUtils.getString(invMap.get("codeInv"))+",票据已作废，无法换开票据!\r\n");
//					continue;
//				}

				try{
					List<BillInfo> billList = new ArrayList<>();
					//查询该纸质票的票据信息，查找前缀等信息
					BlEmpInvoice empInvInfo = DataBaseHelper.queryForBean(
							"select * from BL_EMP_INVOICE where PK_EMPINV = ?",
							BlEmpInvoice.class, new Object[]{invMap.get("pkEmpinvoice")});
					if(empInvInfo!=null){
						String curNo = CommonUtils.getString(invMap.get("codeInv"));
						if(!CommonUtils.isEmptyString(empInvInfo.getInvPrefix())){
							curNo = CommonUtils.getString(invMap.get("codeInv")).substring(empInvInfo.getInvPrefix().length());
						}

						//纸质票信息有包含前缀等信息，此处需要将前缀删除，只保留数字部分号码
						BillInfo bill = new BillInfo();
						bill.setCurNo(CommonUtils.getInteger(curNo));
						billList.add(bill);

						invSettltService.drawRePaperBill(billList,CommonUtils.getString(invMap.get("pkSettle")), user);
					}
				}catch(Exception e){
					e.printStackTrace();
					msg.append("姓名："+CommonUtils.getString(invMap.get("namePi"))+",发票号码："+CommonUtils.getString(invMap.get("codeInv"))+","+e.getMessage()+"!\r\n");
				}
			}
		}

		return msg.toString();
	}

	/**
	 *批量冲红票据信息
	 * 交易号：007003008019
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String drawCancelBill(String param,IUser user){
		Map<String, Object> params = getParam(param);
		List<Map<String,Object>> res = repEBillDataMapper.qryStInvInfo(params);

		StringBuffer msg = new StringBuffer("");

		for(Map<String,Object> invMap : res){
			if(invMap!=null && invMap.size()>0){
				if("0".equals(CommonUtils.getString(invMap.get("flagCancel")))){
					msg.append("姓名："+CommonUtils.getString(invMap.get("namePi"))+",发票号码："+CommonUtils.getString(invMap.get("codeInv"))+",票据没有作废，无法冲红票据!\r\n");
					continue;
				}

				try{
					invSettltService.billCancel(CommonUtils.getString(invMap.get("pkSettle")), user);
				}catch(Exception e){
					e.printStackTrace();
					msg.append("姓名："+CommonUtils.getString(invMap.get("namePi"))+",发票号码："+CommonUtils.getString(invMap.get("codeInv"))+","+e.getMessage()+"!\r\n");
				}
			}
		}

		return msg.toString();
	}

	private Map<String, Object> getParam(String param) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (param.startsWith("[")) {
			List<String> pkList = JsonUtil.readValue(param, ArrayList.class);
			if (pkList == null || pkList.size() <= 0) {
				throw new BusException("请勾选发票信息！");
			}
			params.put("pkInvList", pkList);
		} else {
			params = JsonUtil.readValue(param, Map.class);
		}
		return params;
	}

}