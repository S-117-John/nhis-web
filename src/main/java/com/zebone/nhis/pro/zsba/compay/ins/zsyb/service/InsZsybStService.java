package com.zebone.nhis.pro.zsba.compay.ins.zsyb.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybStMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.ChargeDetailsData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.DetailsData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbPv;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbSt;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbStItemcate;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbUndoSingle;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.YbSettlementData;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsZsybStService {
	
	@Autowired
	private InsZsybStMapper insStMapper;
	
	@Autowired
	private BlIpPubMapper blIpPubMapper; 

	/**
	 * 判断是否可以进行医保结算
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> ifYbState(String param , IUser user){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try{
			//回参status：1：可以进行医保结算 2：不能进行医保结算 3：不需要医保结算
			JSONObject jo = JSONObject.fromObject(param);
			String pkPv = jo.getString("pkPv");
			InsZsBaYbPv pv = DataBaseHelper.queryForBean("select gmsfhm,status from ins_pv where pk_pv = ? and del_flag = '0'", InsZsBaYbPv.class, pkPv);
			if(pv!=null&&pv.getStatus()!=null){
				//未出院登记的不能进行结算，需要提醒，1、2、3、4、6、7、9、10；已结算的不需要进行医保结算了，11、14、16；已出院登记或取消结算成功的可以进行医保计算。5、8、12、13、15
				if(pv.getStatus().equals("5")||pv.getStatus().equals("8")||pv.getStatus().equals("12")||pv.getStatus().equals("13")||pv.getStatus().equals("15")){
					//5:出院登记成功;8:取消出院登记失败;12:结算失败;13:取消结算成功;15:跨月取消结算成功
					returnMap.put("status", "1");
				}else if(pv.getStatus().equals("11")||pv.getStatus().equals("14")||pv.getStatus().equals("16")){
					returnMap.put("status", "3");
				}else{
					returnMap.put("status", "2");
				}
			}else{
				returnMap.put("status", "3");
			}
			if(pv!=null){
				returnMap.put("gmsfhm", pv.getGmsfhm());
			}else{
				returnMap.put("gmsfhm", null);
			}
		}catch (Exception e) {
			throw new BusException(e.getMessage());
		}
		return returnMap;
	}
	
	/**
	 * 获取住院费用明细，医保上传明细用的
	 * @param param
	 * @param user
	 * @return
	 */
	public DetailsData getChargeDetails(String param , IUser user){
		
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		String dateBegin = jo.getString("dateBegin");
		String dateEnd = jo.getString("dateEnd");

		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		param_h.put("dateBegin", dateBegin);
		param_h.put("dateEnd", dateEnd);
		
		DetailsData detailsData = new DetailsData();
		BigDecimal totalCost  = new BigDecimal(0);
		
		List<Map<String,Object>> basicDataList = insStMapper.getChargeDetails(param_h);//项目
		List<Map<String,Object>> YpBasicDataList = insStMapper.getChargeDetailsYp(param_h);//药品
		/*String xmTip = "";
		for(int i=0; i<basicDataList.size(); i++){
			if(basicDataList.get(i).get("xmbh")==null || StringUtils.isEmpty(basicDataList.get(i).get("xmbh").toString())){
				xmTip += basicDataList.get(i).get("name") + "("+basicDataList.get(i).get("code")+")、";
			}
		}
		if(xmTip.length()>0){
			xmTip = xmTip.substring(0, xmTip.length()-1);
			xmTip += "以上诊疗项目未进行医保目录对照，请联系医保科进行对照;";
		}
		String ypTip = "";
		for(int i=0; i<YpBasicDataList.size(); i++){
			if(YpBasicDataList.get(i).get("xmbh")==null || StringUtils.isEmpty(YpBasicDataList.get(i).get("xmbh").toString())){
				ypTip += YpBasicDataList.get(i).get("name") + "("+YpBasicDataList.get(i).get("code")+")、";
			}
		}
		if(ypTip.length()>0){
			ypTip = xmTip.substring(0, xmTip.length()-1);
			ypTip += "以上药品未进行医保目录对照，请联系药剂科进行对照;";
		}
		if(xmTip.length()>0 || ypTip.length()>0){
			detailsData.setErrorMsg(xmTip+xmTip);
		}else{*/
			basicDataList.addAll(YpBasicDataList);
			
			PvEncounter pe = DataBaseHelper.queryForBean("select * from pv_encounter where del_flag = '0' and pk_pv = ?", PvEncounter.class, pkPv);
			
			List<ChargeDetailsData> cddList = new ArrayList<ChargeDetailsData>();
			for(int i=0; i<basicDataList.size(); i++){
				if(i==0){
					detailsData.setCodeIp(basicDataList.get(i).get("code_ip")==null?null:basicDataList.get(i).get("code_ip").toString());//住院号	
					detailsData.setJzjlh(basicDataList.get(i).get("JZJLH")==null?null:basicDataList.get(i).get("JZJLH").toString());    //医保就诊登记号
				}
				ChargeDetailsData cdd = new ChargeDetailsData();
				cdd.setJzjlh(basicDataList.get(i).get("JZJLH")==null?null:basicDataList.get(i).get("JZJLH").toString());    //医保就诊登记号
				cdd.setXmxh(String.valueOf(i+1));  //项目序号
				cdd.setKzrq(basicDataList.get(i).get("date_cg")==null?null:dateFormat(basicDataList.get(i).get("date_cg").toString()));  //费用开始时间
				if(basicDataList.get(i).get("flag_pd")!=null&&basicDataList.get(i).get("flag_pd").equals("0")){
					cdd.setYyxmbh(basicDataList.get(i).get("code")==null?null:basicDataList.get(i).get("code").toString());  //收费编码 （医院项目编号）
					cdd.setYyxmmc(basicDataList.get(i).get("name_cg")==null?null:basicDataList.get(i).get("name_cg").toString()); //医院项目名称
				}else{
					cdd.setYyxmbh(basicDataList.get(i).get("code_wp")==null?null:basicDataList.get(i).get("code_wp").toString());  //收费编码 （医院物品编号）
					cdd.setYyxmmc(basicDataList.get(i).get("name_wp")==null?null:basicDataList.get(i).get("name_wp").toString()); //医院物品名称
				}

				cdd.setSpmc(basicDataList.get(i).get("name_wp")==null?null:basicDataList.get(i).get("name_wp").toString()); //物品(商品名称)
				cdd.setYpgg(basicDataList.get(i).get("spec")==null?null:basicDataList.get(i).get("spec").toString());  //规格   
				cdd.setYpyf(basicDataList.get(i).get("code_supply")==null?null:basicDataList.get(i).get("code_supply").toString()); //用法 
				cdd.setYpjl(basicDataList.get(i).get("dosage")==null?null:basicDataList.get(i).get("dosage").toString()); //剂量   
				//医嘱类别
				if(basicDataList.get(i).get("eu_always")==null||basicDataList.get(i).get("eu_always").equals("1")){
					cdd.setYzlb("2");
				}else if(basicDataList.get(i).get("eu_always").equals("0")){
					cdd.setYzlb("1");
				}
				cdd.setJg(basicDataList.get(i).get("price")==null?null:basicDataList.get(i).get("price").toString()); //单价
				cdd.setMcyl(basicDataList.get(i).get("quan")==null?null:basicDataList.get(i).get("quan").toString()); //数量
	/*			//金额*优惠比例等于上传给医保的金额(已废)
				Double total = Double.parseDouble(basicDataList.get(i).get("amount").toString())*Double.parseDouble(basicDataList.get(i).get("ratio_disc").toString());
				cdd.setJe(String.valueOf(total));  //金额
	*/			//全额上传医保
				cdd.setJe(basicDataList.get(i).get("amount").toString());
				//适应症标志(限制用药标志)
				if(basicDataList.get(i).get("flag_fit")==null||basicDataList.get(i).get("flag_fit").equals("0")){  
					cdd.setXzyybz("2");
				}else if(basicDataList.get(i).get("flag_fit").equals("1")){
					cdd.setXzyybz("3");
				}
				cdd.setDlgg(basicDataList.get(i).get("quan_cg")==null?null:basicDataList.get(i).get("quan_cg").toString());  //单量规格   
				cdd.setDwgg(basicDataList.get(i).get("name")==null?null:basicDataList.get(i).get("name").toString());  //单位规格  
				//cdd.setSyts(basicDataList.get(i).get("days")==null?null:basicDataList.get(i).get("days").toString());  //使用天数
				cddList.add(cdd);
				
				totalCost = totalCost.add(new BigDecimal(basicDataList.get(i).get("amount").toString()));
			}
			
			detailsData.setChargeDetailsDataList(cddList);
			detailsData.setJzrq(DateUtils.dateToStr("yyyyMMdd", pe.getDateBegin())); //就诊时间
			detailsData.setTotalCost(totalCost.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			
			String sql = "select * from ins_pv where pk_pv = ?";
			InsZsBaYbPv pv = DataBaseHelper.queryForBean(sql, InsZsBaYbPv.class, pkPv);
			if(pv!=null){
				detailsData.setGrsxh(pv.getGrsxh());
				detailsData.setJzrq(DateUtils.dateToStr("yyyyMMdd", pv.getRyrq())); //就诊时间
			}
		//}
		return detailsData;
	}
	
	/**
	 * 保存医保结算数据(这个方法目前没有用到)
	 * @param param
	 * @param user
	 */
	public String saveYbSettlementData(String param , IUser user){
		YbSettlementData ybSettlementData = JsonUtil.readValue(param, YbSettlementData.class);
		InsZsBaYbSt insSt = ybSettlementData.getInsSt();
		List<InsZsBaYbStItemcate> isList = ybSettlementData.getInsStItemcateList();
		
		//保存医保结算表
		InsZsBaYbPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and pk_pv = ?", InsZsBaYbPv.class, insSt.getPkPv());
		if(insSt.getFhz().equals("1")){
			insPv.setStatus("11");
			insSt.setStatus("11");
		}else{
			insPv.setStatus("12");
			insSt.setStatus("12");
		}
		insSt.setPkInspv(insPv.getPkInspv());
		insSt.setPkHp(insPv.getPkPv());
		insSt.setPkPi(insPv.getPkPi());
		insSt.setYybh("H003");
		insSt.setEuPvtype("3");  //1 门诊，2 急诊，3 住院，4 体检
		
		DataBaseHelper.insertBean(insSt);
		//保存医保结算项目
		for(int i=0; i<isList.size(); i++){
			InsZsBaYbStItemcate is = isList.get(i);
			BdItem bdItem = DataBaseHelper.queryForBean("select * from bd_item where del_flag = '0' and code = ?", BdItem.class, is.getJsxm());//根据项目编号查询项目名称
			is.setPkPi(insSt.getPkPi());
			is.setPkPv(insSt.getPkPv());
			is.setEuPvtype("3");
			//is.setPkSettle(insSt.getPkSettle()); 这个时候是没有结算主键的
			if(bdItem!=null){
				is.setJsxmmc(bdItem.getName());
			}
			is.setJzjlh(insSt.getJzjlh());
			is.setJsywh(insSt.getJsywh());
			is.setPkInsst(insSt.getPkInsst());
			DataBaseHelper.insertBean(is);
		}
		//修改入院登记表的状态
		//InsPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and jzjlh = ?", InsPv.class, insSt.getJzjlh());

		DataBaseHelper.updateBeanByPk(insPv, false);
		return insSt.getPkInsst();
	}
	

	
	/**
	 * 获取取消医保结算的入参
	 * @param param
	 * @param user
	 */
	public Map<String,String> getCancelYbSettleParam(String param , IUser user){
		Map<String,String> returnMap = new HashMap<String,String>();
		
		JSONObject jo = JSONObject.fromObject(param);
		String pkSettle = jo.getString("pkSettle");
		String dtDepttype = jo.getString("dtDepttype");
		
		String sql = "select t2.code_dept, t1.date_st from bl_settle t1 inner join bd_ou_dept t2 on t1.pk_dept_st = t2.pk_dept  where t1.pk_settle = ? and t1.del_flag = '0'";
		Map<String,Object> blSettleMap = DataBaseHelper.queryForMap(sql, pkSettle);
		
		String stSql = "select * from ins_st where pk_settle = ? and del_flag = '0'";
		InsZsBaYbSt insSt = DataBaseHelper.queryForBean(stSql, InsZsBaYbSt.class, pkSettle);
		
		String deptSql = "select dt_depttype from bd_ou_dept where code_dept = ?";
		Map<String,Object> deptMap = DataBaseHelper.queryForMap(deptSql, blSettleMap.get("codeDept"));
		
		if (insSt != null && !insSt.getStatus().equals("13") && !insSt.getStatus().equals("15")) {
			returnMap.put("pkPi", insSt.getPkPi());  
			returnMap.put("pkPv", insSt.getPkPv());  
			returnMap.put("isCencel", "true");  //需要进行医保的取消结算
			/*
			 * 护士站医保费用只能撤销当天的,不是当天的提示去收费处，收费处当天可以撤销，不是当天需要填单
			 */
			//是否跨月
			if((insSt.getJjywh()!=null&&insSt.getJjywh().length()>0) || 
					!DateUtils.dateToStr("yyyyMMdd", insSt.getJsrq()).equals(DateUtils.dateToStr("yyyyMMdd", new Date()))){
				if(insSt.getPosSn()!=null){
					if(dtDepttype.equals("08")){// 收费处
						String insPvSql = "select * from ins_pv where pk_inspv = ?";
						InsZsBaYbPv insPv = DataBaseHelper.queryForBean(insPvSql, InsZsBaYbPv.class, insSt.getPkInspv());
						
						returnMap.put("ndk", insPv.getNdk());
						returnMap.put("kh", insSt.getKh());
						returnMap.put("grsxh", insPv.getGrsxh());
						returnMap.put("gz", (insSt.getGzzfeje().add(insSt.getGzzfuje())).toString());
						returnMap.put("yybh", insSt.getYybh());
						
						returnMap.put("fillSingle", "true"); 
					}else{
						throw new BusException("护士站只能撤销当天的个账，不是当天的请到收费窗口退费");
					}
					
				}else{
					returnMap.put("fillSingle", "false");	//用来提示是否需要填单，true：需要；false：不需要
				}
				returnMap.put("artificialRevoke", "false"); //收费窗口是否提示扣个账  true：提示；false：不提示
			}else{
				//不是跨月并且是当天的走这边
				//判断是否是在收费处，是的话判断是否是在收费处结算的，在收费处的结算的，撤销需要提醒人工撤销，
				//不是在收费处结算的,需要填单
				if(insSt.getPosSn()!=null){
					String insPvSql = "select * from ins_pv where pk_inspv = ?";
					InsZsBaYbPv insPv = DataBaseHelper.queryForBean(insPvSql, InsZsBaYbPv.class, insSt.getPkInspv());
/*					if(insPv.getNdk()!=null && insPv.getNdk().equals("3")){
						returnMap.put("ndk", insPv.getNdk());
						returnMap.put("kh", insSt.getKh());
						returnMap.put("grsxh", insPv.getGrsxh());
						returnMap.put("gz", (insSt.getGzzfeje().add(insSt.getGzzfuje())).toString());
						returnMap.put("yybh", insSt.getYybh());
						
						returnMap.put("fillSingle", "false"); 
						returnMap.put("artificialRevoke", "false"); 
					}else{*/
						if(dtDepttype.equals("08")){//判断在哪取消结算的
							if(!deptMap.get("dtDepttype").equals("08")){//判断在哪进行结算的
								returnMap.put("ndk", insPv.getNdk());
								returnMap.put("kh", insSt.getKh());
								returnMap.put("grsxh", insPv.getGrsxh());
								returnMap.put("gz", (insSt.getGzzfeje().add(insSt.getGzzfuje())).toString());
								returnMap.put("yybh", insSt.getYybh());
								
								returnMap.put("fillSingle", "true"); 
								returnMap.put("artificialRevoke", "false"); 
							}else{
								returnMap.put("ndk", insPv.getNdk());
								returnMap.put("fillSingle", "false"); 		//用来提示是否需要填单，true：需要；false：不需要
								returnMap.put("artificialRevoke", "true"); 	//收费窗口是否提示撤销个账， true：提示；false：不提示
							}
						}else{
							if(!deptMap.get("dtDepttype").equals("08")){
								returnMap.put("artificialRevoke", "false"); 
								returnMap.put("fillSingle", "false"); 
							}else{
								throw new BusException("护士站只能撤销护士站扣的个账，收费处的请到收费窗口退费");
							}
						}
					//}
				}else{
					returnMap.put("artificialRevoke", "false"); 
					returnMap.put("fillSingle", "false"); 
				}
			}
			returnMap.put("jzjlh", insSt.getJzjlh());
			returnMap.put("jsywh", insSt.getJsywh());
			returnMap.put("posSn", insSt.getPosSn());
			//判断是否是跨月退费
			if(insSt.getJjywh()!=null){
				returnMap.put("jjywh", insSt.getJjywh());
			}else{
				returnMap.put("jjywh", "");
			}

			//判断是否是银行卡缴费
			BlDeposit blDeposit = DataBaseHelper.queryForBean("select * from bl_deposit where pk_settle = ?",BlDeposit.class, pkSettle);
			if(blDeposit==null || !blDeposit.getDtPaymode().equals("3")){
				returnMap.put("sureCencel", "true"); 
				
				if(blDeposit!=null && blDeposit.getDtPaymode().equals("1")){//现金结算
					if(!dtDepttype.equals("08")){
						throw new BusException("护士站不允许现金退费，请到收费窗口退费");
					}
				}
				
				BlSettle stVo = blIpPubMapper.QryBlSettleByPk(pkSettle);
/*				if("10".equals(stVo.getDtSttype()) && EnumerateParameter.TWO.equals(stVo.getEuStresult())){
			    	 //如果被取消的结算为出院结算，结算结果为存款结算（eu_stresult=2），要将存入患者账户的存款冲销回原状，冲销前要调用查询患者账户服务，确认患者账户金额是否足以冲销，如果患者账户金额不足，将不允许取消结算。
			    	 List<BlDepositPi> deposRe = blIpPubMapper.QryBlDepositPiBySt(pkSettle);
			    	 BigDecimal amtAcc = BigDecimal.ZERO;
			    	 for(BlDepositPi depo : deposRe){				
							amtAcc = amtAcc.add(depo.getAmount());
					}
			    	Map<String,Object> amtAccMap = DataBaseHelper.queryForMap("select  Amt_acc from pi_acc piacc  inner join pv_encounter pv on piacc.pk_pi = pv.pk_pi where  pv.pk_pv = ?", stVo.getPkPv());
			  		BigDecimal amtAccNow = (BigDecimal)amtAccMap.get("amtAcc");
			  		if(amtAccNow.compareTo(amtAcc)>=0){
			  			returnMap.put("sureCencel", "false"); //是否可以进行医保的取消结算
			  			returnMap.put("message", "账户余额:"+amtAccNow.doubleValue()+"不足，小于冲销所需:"+amtAcc+",取消结算失败!"); //
			  		}
				}*/
			}else{
				//银行卡缴费
				if(dtDepttype.equals("08")){
					returnMap.put("sureCencel", "true");
				}else{
					//银联pos机如果开通退货功能，可支持不是当天的退费，如果没开通，只有当天的才能撤销
					//目前以没开通的处理
					if(blSettleMap.get("dateSt").toString().substring(0, 8).equals(DateUtils.dateToStr("yyyyMMdd", new Date()))){
						returnMap.put("sureCencel", "true");
					}else{
			  			returnMap.put("sureCencel", "false"); //不可以进行医保的取消结算
			  			returnMap.put("message", "银行卡缴费的需要去人工窗口撤销费用!"); 
					}
				}
			}

		}else{
			returnMap.put("isCencel", "false");  //不需要进行医保的取消结算
		}
		return returnMap;
	}
	
	/**
	 * 取消医保结算(包括跨月的)
	 * @param param
	 * @param user
	 */
	public void cancelYbSettlement(String param , IUser user){
		InsZsBaYbSt insSt = JsonUtil.readValue(param, InsZsBaYbSt.class);
		
		//修改入院登记表的状态
		InsZsBaYbPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and jzjlh = ?", InsZsBaYbPv.class, insSt.getJzjlh());
		if(insSt.getJjywh()!=null&&insSt.getJjywh().length()>0){
			insPv.setDelFlag("1");
		}
		insPv.setStatus(insSt.getStatus());
		
		InsZsBaYbSt insSt2 = DataBaseHelper.queryForBean("select * from ins_st where del_flag = '0' and jsywh = ?", InsZsBaYbSt.class, insSt.getJsywh());
		insSt.setPkInsst(insSt2.getPkInsst());
		insSt.setDelFlag("1");
		DataBaseHelper.updateBeanByPk(insSt, false);
		DataBaseHelper.updateBeanByPk(insPv, false);
		if((insSt.getJjywh()!=null&&insSt.getJjywh().length()>0)||!DateUtils.dateToStr("yyyyMMdd", insSt2.getJsrq()).equals(DateUtils.dateToStr("yyyyMMdd", new Date()))){
			//跨月退单或者不是当天的，需保存填单信息
			InsZsBaYbUndoSingle us = new InsZsBaYbUndoSingle();
			us.setPkSettle(insSt2.getPkSettle());
			us.setPkInsst(insSt2.getPkInsst());
			us.setPkPi(insSt2.getPkPi());
			us.setPosSn(insSt2.getPosSn());
			us.setStatus("0");
			DataBaseHelper.insertBean(us);
		}
	}
	
	/**
	 * 修改住院收费明细的医保上传明细状态
	 * @param param
	 * @param user
	 */
	public void updateDetailStatus(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		String dateBegin = jo.getString("dateBegin");
		String dateEnd = jo.getString("dateEnd");
		String flagInsu =  jo.getString("flagInsu");//医保上次标志  修改为已上次传1，修改为未上次传0
		
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		param_h.put("dateBegin", dateBegin);
		param_h.put("dateEnd", dateEnd);
		
		List<Map<String,Object>> basicDataList = insStMapper.getChargeDetailsUpdate(param_h);//项目
		basicDataList.addAll(insStMapper.getChargeDetailsYpUpdate(param_h));//药品
		
		for(int i=0; i<basicDataList.size(); i++){
			BlIpDt bid = new BlIpDt();
			bid.setPkCgip(basicDataList.get(i).get("pk_cgip").toString());
			bid.setFlagInsu(flagInsu);
			DataBaseHelper.updateBeanByPk(bid, false);
		}
		
		
/*		StringBuffer sqlSb = new StringBuffer("update bl_ip_dt set flag_insu = ? ");
		sqlSb.append(" where pk_pv = ? and flag_settle = 0");
		sqlSb.append(" and date_cg  >= ? and date_cg <= ? ");
		DataBaseHelper.execute(sqlSb.toString(),flagInsu,pkPv, dateBegin, dateEnd);	*/
	}
	
	/**
	 * 重新结算时修改住院收费明细的医保上传明细状态
	 * @param param
	 * @param user
	 */
	public void updateDetailStatus2(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String jsywh = jo.getString("jsywh");
		String flagInsu =  jo.getString("flagInsu");//医保上次标志  修改为已上次传1，修改为未上次传0
		
		InsZsBaYbSt st = DataBaseHelper.queryForBean("select * from ins_st where del_flag = '0' and jsywh = ?", InsZsBaYbSt.class, jsywh);
		
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkSettle", st.getPkSettle());
		
		List<Map<String,Object>> basicDataList = insStMapper.getChargeDetails2(param_h);
		basicDataList.addAll(insStMapper.getChargeDetailsYp2(param_h));
		
		for(int i=0; i<basicDataList.size(); i++){
			BlIpDt bid = new BlIpDt();
			bid.setPkCgip(basicDataList.get(i).get("pk_cgip").toString());
			bid.setFlagInsu(flagInsu);
			DataBaseHelper.updateBeanByPk(bid, false);
		}
	}
	
	/**
	 * 重新结算时获取上次数据
	 * @param param
	 * @param user
	 */
	public DetailsData getAfreshSettlePanam(String param , IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String jsywh = jo.getString("jsywh");
		
		InsZsBaYbSt st = DataBaseHelper.queryForBean("select * from ins_st where del_flag = '0' and jsywh = ?", InsZsBaYbSt.class, jsywh);
		InsZsBaYbPv pv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and pk_inspv = ?", InsZsBaYbPv.class, st.getPkInspv());
		PvEncounter pe = DataBaseHelper.queryForBean("select * from pv_encounter where del_flag = '0' and pk_pv = ?", PvEncounter.class, st.getPkPv());
		
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkSettle", st.getPkSettle());
		
		DetailsData detailsData = new DetailsData();
		
		detailsData.setGrsxh(pv.getGrsxh());
		detailsData.setJym(st.getJym());
		detailsData.setKh(st.getKh());
		detailsData.setNd(st.getNd());
		detailsData.setPsam(st.getPsam());
		detailsData.setRylb(st.getRylb());
		detailsData.setYbzhye(st.getYbzhye().toString());
		
		BigDecimal totalCost = new BigDecimal(0);
		List<Map<String,Object>> basicDataList = insStMapper.getChargeDetails2(param_h);
		basicDataList.addAll(insStMapper.getChargeDetailsYp2(param_h));
		
		List<ChargeDetailsData> cddList = new ArrayList<ChargeDetailsData>();
		for(int i=0; i<basicDataList.size(); i++){
			if(i==0){
				detailsData.setCodeIp(basicDataList.get(i).get("code_ip")==null?null:basicDataList.get(i).get("code_ip").toString());//住院号	
				detailsData.setJzjlh(basicDataList.get(i).get("JZJLH")==null?null:basicDataList.get(i).get("JZJLH").toString());    //医保就诊登记号
			}
			ChargeDetailsData cdd = new ChargeDetailsData();
			cdd.setJzjlh(basicDataList.get(i).get("JZJLH")==null?null:basicDataList.get(i).get("JZJLH").toString());    //医保就诊登记号
			cdd.setXmxh(String.valueOf(i+1));  //项目序号
			cdd.setKzrq(basicDataList.get(i).get("date_cg")==null?null:dateFormat(basicDataList.get(i).get("date_cg").toString()));  //费用开始时间
			if(basicDataList.get(i).get("flag_pd")!=null&&basicDataList.get(i).get("flag_pd").equals("0")){
				cdd.setYyxmbh(basicDataList.get(i).get("code")==null?null:basicDataList.get(i).get("code").toString());  //收费编码 （医院项目编号）
				cdd.setYyxmmc(basicDataList.get(i).get("name_cg")==null?null:basicDataList.get(i).get("name_cg").toString()); //医院项目名称
			}else{
				cdd.setYyxmbh(basicDataList.get(i).get("code_wp")==null?null:basicDataList.get(i).get("code_wp").toString());  //收费编码 （医院物品编号）
				cdd.setYyxmmc(basicDataList.get(i).get("name_wp")==null?null:basicDataList.get(i).get("name_wp").toString()); //医院物品名称
			}

			cdd.setSpmc(basicDataList.get(i).get("name_wp")==null?null:basicDataList.get(i).get("name_wp").toString()); //物品(商品名称)
			cdd.setYpgg(basicDataList.get(i).get("spec")==null?null:basicDataList.get(i).get("spec").toString());  //规格   
			cdd.setYpyf(basicDataList.get(i).get("code_supply")==null?null:basicDataList.get(i).get("code_supply").toString()); //用法 
			cdd.setYpjl(basicDataList.get(i).get("dosage")==null?null:basicDataList.get(i).get("dosage").toString()); //剂量   
			//医嘱类别
			if(basicDataList.get(i).get("eu_always")==null||basicDataList.get(i).get("eu_always").equals("1")){
				cdd.setYzlb("2");
			}else if(basicDataList.get(i).get("eu_always").equals("0")){
				cdd.setYzlb("1");
			}
			cdd.setJg(basicDataList.get(i).get("price")==null?null:basicDataList.get(i).get("price").toString()); //单价
			cdd.setMcyl(basicDataList.get(i).get("quan")==null?null:basicDataList.get(i).get("quan").toString()); //数量
/*			//金额*优惠比例等于上传给医保的金额
			Double total = Double.parseDouble(basicDataList.get(i).get("amount").toString())*Double.parseDouble(basicDataList.get(i).get("ratio_disc").toString());
			cdd.setJe(String.valueOf(total));  //金额
*/			//全额上传医保
			cdd.setJe(basicDataList.get(i).get("amount").toString());
			//适应症标志(限制用药标志)
			if(basicDataList.get(i).get("flag_fit")==null||basicDataList.get(i).get("flag_fit").equals("0")){  
				cdd.setXzyybz("2");
			}else if(basicDataList.get(i).get("flag_fit").equals("1")){
				cdd.setXzyybz("3");
			}
			cdd.setDlgg(basicDataList.get(i).get("quan_cg")==null?null:basicDataList.get(i).get("quan_cg").toString());  //单量规格   
			cdd.setDwgg(basicDataList.get(i).get("name")==null?null:basicDataList.get(i).get("name").toString());  //单位规格  
			cdd.setSyts(basicDataList.get(i).get("days")==null?null:basicDataList.get(i).get("days").toString());  //使用天数
			cddList.add(cdd);
			
			totalCost = totalCost.add(new BigDecimal(basicDataList.get(i).get("amount").toString()));
		}
		detailsData.setChargeDetailsDataList(cddList);
		detailsData.setJzrq(DateUtils.dateToStr("yyyyMMdd", pe.getDateBegin())); //就诊时间
		detailsData.setTotalCost(totalCost.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		return detailsData;
	}
	
	/**
	 * 保存医保重新结算的数据
	 * @param param
	 * @param user
	 */
	public void saveAfreshYbSettlementData(String param , IUser user){
		YbSettlementData ybSettlementData = JsonUtil.readValue(param, YbSettlementData.class);
		InsZsBaYbSt insSt = ybSettlementData.getInsSt();
		List<InsZsBaYbStItemcate> isList = ybSettlementData.getInsStItemcateList();
		String jsywh = ybSettlementData.getJsywh();
		
		//将新的结算数据保存到旧的那条记录
		InsZsBaYbSt st = DataBaseHelper.queryForBean("select * from ins_st where del_flag = '0' and jsywh = ?", InsZsBaYbSt.class, jsywh);
        st.setJsywh(insSt.getJsywh());
        st.setXzlx(insSt.getXzlx());
        st.setYlfyze(insSt.getYlfyze());
        st.setGzzfuje(insSt.getGzzfuje());
        st.setGzzfeje(insSt.getGzzfeje());
        st.setTczfje(insSt.getTczfje());
        st.setGwytczf(insSt.getGwytczf());
        st.setXjzfuje(insSt.getXjzfuje());
        st.setXjzfeje(insSt.getXjzfeje());
        st.setDejsbz(insSt.getDejsbz());
        st.setJsqzytced(insSt.getJsqzytced());
        st.setJsrq(insSt.getJsrq());
        st.setBcyltczf(insSt.getBcyltczf());
        st.setJbylbz(insSt.getJbylbz());
        st.setBcxsbz(insSt.getBcxsbz());
        st.setGwyxsbz(insSt.getGwyxsbz());
        st.setDbtczf(insSt.getDbtczf());
        st.setDbdxlb(insSt.getDbdxlb());
        st.setMzdbtczf(insSt.getMzdbtczf());
        st.setMzdbljje(insSt.getMzdbljje());
        st.setYfdxlb(insSt.getYfdxlb());
        st.setMzyftczf(insSt.getMzyftczf());
        st.setMzyfljje(insSt.getMzyfljje());
        st.setJzlb(insSt.getJzlb());
        st.setFhz(insSt.getFhz());
        st.setMsg(insSt.getMsg());
		
        DataBaseHelper.updateBeanByPk(st, false);
        
        //删除旧的数据
        String sql = "delete from ins_st_itemcate where pk_insst = ?";
        DataBaseHelper.execute(sql, st.getPkInsst());
        
		//保存新的医保结算项目
		for(int i=0; i<isList.size(); i++){
			InsZsBaYbStItemcate is = isList.get(i);
			BdItem bdItem = DataBaseHelper.queryForBean("select * from bd_item where del_flag = '0' and code = ?", BdItem.class, is.getJsxm());//根据项目编号查询项目名称
			is.setPkPi(insSt.getPkPi());
			is.setPkPv(insSt.getPkPv());
			is.setEuPvtype("3");
			is.setPkSettle(insSt.getPkSettle()); 
			if(bdItem!=null){
				is.setJsxmmc(bdItem.getName());
			}
			is.setJzjlh(insSt.getJzjlh());
			is.setJsywh(insSt.getJsywh());
			is.setPkInsst(insSt.getPkInsst());
			DataBaseHelper.insertBean(is);
		}
	}
	
	/**
	 * 去除日期中的'-','/',':',' '等字符
	 * @param date
	 * @return
	 */
	private String dateFormat(String date){
		date=date.replace("-",""); 
		date=date.replace("/",""); 
		date=date.replace(":",""); 
		date=date.replace(" ",""); 
		date=date.replace(".",""); 
		date=date.substring(0, 14);
		return date;
	}
}