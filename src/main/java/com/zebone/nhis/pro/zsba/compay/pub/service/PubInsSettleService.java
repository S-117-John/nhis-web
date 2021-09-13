package com.zebone.nhis.pro.zsba.compay.pub.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo.InsZsKsybStKsyb;
import com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo.InsZsKsybStKsybJjfx;
import com.zebone.nhis.pro.zsba.compay.ins.pub.utils.HttpClient4;
import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsZsPubPvOut;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaPvQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaStQg;
import com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo.InsStWi;
import com.zebone.nhis.pro.zsba.compay.ins.ydyb.vo.InsZsYdybStSnyb;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbPv;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbSt;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbStItemcate;
import com.zebone.nhis.pro.zsba.compay.pub.dao.PubInsSettleMapper;
import com.zebone.nhis.pro.zsba.compay.pub.vo.PayPosTr;
import com.zebone.nhis.pro.zsba.compay.pub.vo.SettleCallBack;
import com.zebone.nhis.pro.zsba.compay.pub.vo.SettleCallBackData;
import com.zebone.nhis.pro.zsba.compay.pub.vo.SettleCallBackExtra;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaSettleInfo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class PubInsSettleService {
	
	@Autowired
	private PubInsSettleMapper pubInsSettleMapper;
	
	public List<Map<String,Object>> getBasicDataList(Map<String,Object> param){
		return pubInsSettleMapper.getChargeDetails(param);
	}
	
	/**
	 * 保存中山医保结算数据
	 * @param insSt
	 * @param isList
	 * @param pkSettle
	 */
 	public void saveYbSettlementData(InsZsBaYbSt insSt, List<InsZsBaYbStItemcate> isList, String pkSettle, String pkPosTr){
		if(insSt==null){
			return;
		}
		//保存医保结算表
		InsZsBaYbPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and pk_pv = ?", InsZsBaYbPv.class, insSt.getPkPv());
		insPv.setNdk(insSt.getNdk());
		insPv.setStatus("11");
		insSt.setStatus("11");
		insSt.setPkInspv(insPv.getPkInspv());
		insSt.setPkHp(insPv.getPkHp());
		insSt.setPkPi(insPv.getPkPi());
		insSt.setYybh("H003");
		insSt.setEuPvtype("3");  //1 门诊，2 急诊，3 住院，4 体检
		insSt.setPkSettle(pkSettle);
		
		DataBaseHelper.insertBean(insSt);
		//保存医保结算项目
		for(int i=0; i<isList.size(); i++){
			InsZsBaYbStItemcate is = isList.get(i);
			//根据项目编号查询项目名称
			String sql = "select * from ins_dict where DEL_FLAG=? and PK_INSDICTTYPE = (select PK_INSDICTTYPE from INS_DICTTYPE where CODE_TYPE=?) and CODE=?";
			BdItem bdItem = DataBaseHelper.queryForBean(sql, BdItem.class, new Object[]{"0", "0018", is.getJsxm()});// CODE_TYPE 为医保字典类型 ins_dicttype表中定义的 医保结算项目(18项分类代码)
			if(bdItem!=null){
				is.setJsxmmc(bdItem.getName());
			}
			is.setPkPi(insSt.getPkPi());
			is.setPkPv(insSt.getPkPv());
			is.setEuPvtype("3");
			is.setPkSettle(pkSettle); 
			is.setJzjlh(insSt.getJzjlh());
			is.setJsywh(insSt.getJsywh());
			is.setPkInsst(insSt.getPkInsst());
			DataBaseHelper.insertBean(is);
		}
		//修改入院登记表的状态
		//InsPv insPv = DataBaseHelper.queryForBean("select * from ins_pv where del_flag = '0' and jzjlh = ?", InsPv.class, insSt.getJzjlh());

		DataBaseHelper.updateBeanByPk(insPv, false);
		
		if(pkPosTr!=null){
			//保存三代卡个账交易记录
			PayPosTr tr = new PayPosTr();
			tr.setPkSettle(pkSettle);
			tr.setPkPosTr(pkPosTr);
			tr.setPkInsst(insSt.getPkInsst());
			DataBaseHelper.updateBeanByPk(tr, false);
		}
	}

	/**
	 * 保存省内异地医保结算数据
	 * @param st
	 * @param pkSettle
	 */
	public void saveInsStSnyb(InsZsYdybStSnyb st, String pkSettle){
		if(st==null){
			return;
		}
		InsZsPubPvOut pv = DataBaseHelper.queryForBean("select * from ins_pv_out where del_flag = '0' and pk_pv = ?", InsZsPubPvOut.class, st.getPkPv());
		if(pv!=null){
			pv.setStatus("11");
			DataBaseHelper.updateBeanByPk(pv, false);
			
			st.setPkPi(pv.getPkPi());
			st.setPkHp("21");
			st.setPkSettle(pkSettle);
			st.setStatus("11");
			DataBaseHelper.insertBean(st);
		}
	}

	/**
	 * 保存跨省医保结算数据
	 * @param st
	 * @param jjfxList
	 * @param pkSettle
	 */
	public void saveInsStKsyb(InsZsKsybStKsyb st, List<InsZsKsybStKsybJjfx> jjfxList, String pkSettle){
		if(st==null){
			return;
		}
		//修改医保登记表的状态
		InsZsPubPvOut pv = DataBaseHelper.queryForBean("select * from ins_pv_out where del_flag = '0' and pk_pv = ?", InsZsPubPvOut.class, st.getPkPv());
		pv.setStatus("11");
		DataBaseHelper.updateBeanByPk(pv, false);
		
		//保存结算数据
		st.setPkPi(pv.getPkPi());
		st.setPkHp("21");
		st.setPkSettle(pkSettle);
		st.setStatus("11");
		DataBaseHelper.insertBean(st);
		
		//保存基金分项数据
		for(InsZsKsybStKsybJjfx jjfx : jjfxList){
			jjfx.setYybh(st.getYybh());
			jjfx.setPkHp(st.getPkHp());
			jjfx.setPkPi(st.getPkPi());
			jjfx.setPkPv(st.getPkPv());
			jjfx.setPkSettle(pkSettle);
			jjfx.setEuPvtype(st.getEuPvtype());
			jjfx.setStatus("11");
			DataBaseHelper.insertBean(jjfx);
		}
	}
	
	/**
	 * 省内工商医保
	 * @param st
	 * @param pkSettle
	 */
	public void saveInsStWi(String pkPv, String pkSettle){
		if(StringUtils.isEmpty(pkPv) || StringUtils.isEmpty(pkSettle)){
			return;
		}
		//修改工商医保结算数据关联院内结算主键
		InsStWi wi = DataBaseHelper.queryForBean("select * from ins_st_wi where del_flag = '0' and pk_pv = ?", InsStWi.class, pkPv);
		if(wi!=null){
			wi.setPkSettle(pkSettle);
			DataBaseHelper.updateBeanByPk(wi, false);
		}
	}
	
	/**
	 * 保存全国医保结算数据
	 * @param st his结算主键
	 * @param pkSettle his结算主键
	 * @param pkPosTr pos交易，个账主键
	 * @param posTr电子凭证信息
	 */
	public void saveInsStQgyb(InsZsbaStQg st, String pkSettle, String pkPosTr, PayPosTr posTr){
		if(st==null){
			return;
		}
		st = DataBaseHelper.queryForBean("select * from ins_st_qg where del_flag = '0' and pk_insstqg = ?", InsZsbaStQg.class, st.getPkInsstqg());
		st.setPkSettle(pkSettle);
		DataBaseHelper.updateBeanByPk(st, false);
		
		if(pkPosTr!=null){
			//保存三代卡个账交易记录
			PayPosTr tr = new PayPosTr();
			tr.setPkSettle(pkSettle);
			tr.setPkPosTr(pkPosTr);
			tr.setPkInsst(st.getPkInsstqg());
			DataBaseHelper.updateBeanByPk(tr, false);
		}
		if(posTr!=null&&pkPosTr!=null){
			if(posTr.getSjly().equals("02")){
				InsZsbaPvQg insPv = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_inspvqg = ?", InsZsbaPvQg.class, st.getPkInspvqg());
				//电子凭证结算回流
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("orgId",  posTr.getShh());
				data.put("orgName",  "中山市博爱医院");
				data.put("orgType", "01");
				//21普通住院  22外伤住院(工伤不走全国医保) 52生育住院 53计划生育手术费
				BdHp bdhp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp = ?", BdHp.class, insPv.getPkInsu());
				if(bdhp.equals("00051") || bdhp.equals("00058") || bdhp.equals("00059")){
					data.put("medType", "21");
				}else if(bdhp.equals("00052")){
					data.put("medType", "52");
				}else if(bdhp.equals("00053")){
					data.put("medType", "53");
				}else{
					data.put("medType", "21");
				}
				
				data.put("cityCode", "442000");
				data.put("ecToken", posTr.getToken());
				data.put("idNo", posTr.getSidentity()); 
				data.put("idType", "01");//01居民身份证
				data.put("userName", posTr.getSname());
				data.put("clientId", posTr.getZdh());
				data.put("status", "SUCC");
				data.put("message", "");
				data.put("optType", "SETTLE");
				data.put("memo", "");
				BigDecimal gz = st.getAcctPay();
				BigDecimal xj = st.getPsnCashPay();
				data.put("feeSumamt", st.getMedfeeSumamt().toString());
				data.put("psnAcctPay", gz.toString());
				data.put("ownpayAmt", xj.toString());
				data.put("fundPay", (st.getMedfeeSumamt().subtract(gz).subtract(xj)).toString());
				data.put("insuranceAmount", "0.00");
				data.put("accountBlance", (new BigDecimal(posTr.getGzye()).subtract(gz)).toString());//因为posTr是扫描电子凭证时返回的，所以要减去实际扣款的个账，剩下的就是余额
				data.put("payType ", "");
				data.put("bizTraceNo", posTr.getPzh());
				data.put("oriBizTraceNo", "");
				data.put("bizTraceTime", DateUtils.getDate("yyyyMMddHHmmss"));
				Map<String, Object> extraMap = new HashMap<String, Object>();
				extraMap.put("cId", posTr.getZdh());
				extraMap.put("fixmedins_code", "H44200100009");
				extraMap.put("fixmedins_name", "中山市博爱医院");
				data.put("extra", extraMap);
				
				Map<String, Object>  paramMap = new HashMap<String, Object>();
				paramMap.put("data", data);
				paramMap.put("encType", "plain");
				paramMap.put("signType", "plain");
				paramMap.put("timestamp", "");
				paramMap.put("version", "");
				paramMap.put("appId", "1");
				String hc = HttpClient4.doPost("http://192.168.0.22:5566/zsdzybpz/jshl", paramMap);
				Map<String,Object> hcMap = JsonUtil.readValue(hc, Map.class); 
				if(hcMap.get("code")!=null&&hcMap.get("code").toString().equals("0")){
					
				}else{
					throw new BusException("调用电子凭证结算回流接口失败,"+hcMap.get("message"));
				}
			}

		}
	}
	
	/**
	 * 用于判断此次结算是否是省工伤医保，此方法用于pk_settle结算数据保存之前
	 * @param st
	 */
	public boolean checkInsStWi(String pkPv){
		InsStWi wi = DataBaseHelper.queryForBean("select * from ins_st_wi where del_flag = '0' and pk_pv = ?", InsStWi.class, pkPv);
		if(wi!=null){
			if(wi.getPkSettle()!=null){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 电子凭证结算回流
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> electronicVoucherJshl(String param, IUser user) {
		Map<String, Object> reutrnMap = new HashMap<String, Object>();
		try{
			PayPosTr posTr = JsonUtil.readValue(param, PayPosTr.class);	
			//JSONObject jo = JSONObject.fromObject(param);
			if(posTr!=null){
				if(posTr.getSjly().equals("02")){
					BlSettle st = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle = ?", BlSettle.class, posTr.getPkSettle());
					InsZsbaStQg stQg = DataBaseHelper.queryForBean("select * from ins_st_qg where pk_settle = ?", InsZsbaStQg.class, posTr.getPkSettle());
					//电子凭证结算回流
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("orgId",  posTr.getShh());
					data.put("orgName",  "中山市博爱医院");
					data.put("orgType", "1");
					//21普通住院  22外伤住院(工伤不走全国医保) 52生育住院 53计划生育手术费
					BdHp bdhp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp = ?", BdHp.class, st.getPkInsurance());
					if(bdhp.equals("00051") || bdhp.equals("00058") || bdhp.equals("00059")){
						data.put("medType", "21");
					}else if(bdhp.equals("00052")){
						data.put("medType", "52");
					}else if(bdhp.equals("00053")){
						data.put("medType", "53");
					}else{
						data.put("medType", "21");
					}
					
					data.put("cityCode", "442000");
					data.put("ecToken", posTr.getToken());
					data.put("idNo", posTr.getSidentity().replaceAll("\u0000","")); 
					data.put("idType", "01");//01居民身份证
					data.put("userName", posTr.getSname());
					data.put("clientId", posTr.getZdh());
					data.put("status", "SUCC");
					data.put("message", "");
					data.put("optType", "SETTLE");
					data.put("memo", "");
					data.put("feeSumamt",  stQg.getMedfeeSumamt());
					data.put("psnAcctPay", stQg.getAcctPay());
					data.put("ownpayAmt", stQg.getPsnCashPay());
					data.put("fundPay",  stQg.getMedfeeSumamt().subtract(stQg.getPsnPartAmt()));
					data.put("insuranceAmount", "0.00");
					data.put("accountBlance",  posTr.getGzye());
					data.put("payType ", "");
					data.put("bizTraceNo",  posTr.getXtckh()+posTr.getPzh());
					data.put("oriBizTraceNo", "");
					data.put("bizTraceTime", DateUtils.getDate("yyyyMMddHHmmss"));
					Map<String, Object> extraMap = new HashMap<String, Object>();
					extraMap.put("cId", posTr.getZdh());
					extraMap.put("fixmedins_code", "H44200100009");
					extraMap.put("fixmedins_name", "中山市博爱医院");
					data.put("extra", extraMap);
					
					Map<String, Object>  paramMap = new HashMap<String, Object>();
					paramMap.put("data", data);
					paramMap.put("encType", "plain");
					paramMap.put("signType", "plain");
					paramMap.put("timestamp", "");
					paramMap.put("version", "");
					paramMap.put("appId", "1");
					String hc = HttpClient4.doPost("http://192.168.0.22:5566/zsdzybpz/jshl", paramMap);
					Map<String,Object> hcMap = JsonUtil.readValue(hc, Map.class); 
					if(hcMap.get("code")!=null&&hcMap.get("code").toString().equals("0")){
						reutrnMap.put("code", "0");
					}else{
						reutrnMap.put("code", "-1");
						reutrnMap.put("msg", "调用电子凭证结算回流接口失败,"+hcMap.get("message"));
					}
				}
			}
		}catch (Exception e) {
			reutrnMap.put("code", "-1");
			reutrnMap.put("msg", "调用电子凭证结算回流接口失败,"+getExceptionDetail(e));
        } 
		return reutrnMap;
	}
	
	/**
	 * 电子凭证结算回流撤销
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> electronicVoucherJshlCx(String param, IUser user) {
		Map<String, Object> reutrnMap = new HashMap<String, Object>();
		try{
			//PayPosTr posTr = JsonUtil.readValue(param, PayPosTr.class);	
			JSONObject jo = JSONObject.fromObject(param);
			BigDecimal bignum1 = new BigDecimal("-1"); 
			String pkSettle = jo.getString("pkSettle");
			if(pkSettle!=null){
				BlSettle st = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle = ?", BlSettle.class, pkSettle);
				InsZsbaStQg stQg = DataBaseHelper.queryForBean("select * from ins_st_qg where pk_settle = ?", InsZsbaStQg.class, pkSettle);
				//查询三代卡或电子凭证记录
				String trSql = "select * from pay_pos_tr where pk_settle = ? and del_flag = '0'";
				PayPosTr tr = DataBaseHelper.queryForBean(trSql, PayPosTr.class, pkSettle);
				
				//电子凭证结算回流撤销
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("orgId",  tr.getShh());
				data.put("orgName",  "中山市博爱医院");
				data.put("orgType", "1");
				//21普通住院  22外伤住院(工伤不走全国医保) 52生育住院 53计划生育手术费
				BdHp bdhp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp = ?", BdHp.class, st.getPkInsurance());
				if(bdhp.equals("00051") || bdhp.equals("00058") || bdhp.equals("00059")){
					data.put("medType", "21");
				}else if(bdhp.equals("00052")){
					data.put("medType", "52");
				}else if(bdhp.equals("00053")){
					data.put("medType", "53");
				}else{
					data.put("medType", "21");
				}
				
				data.put("cityCode", "442000");
				data.put("ecToken", tr.getToken());
				data.put("idNo", tr.getIdNo().replaceAll("\u0000","")); 
				data.put("idType", "01");//01居民身份证
				data.put("userName", tr.getSname());
				data.put("clientId", tr.getZdh());
				data.put("status", "SUCC");
				data.put("message", "");
				data.put("optType", "REFUND");
				data.put("memo", "");
				data.put("feeSumamt",  stQg.getMedfeeSumamt().multiply(bignum1));
				data.put("psnAcctPay", stQg.getAcctPay().multiply(bignum1));
				data.put("ownpayAmt", stQg.getPsnCashPay().multiply(bignum1));
				data.put("fundPay",  (stQg.getMedfeeSumamt().subtract(stQg.getPsnPartAmt())).multiply(bignum1));
				data.put("insuranceAmount", "0.00");
				data.put("accountBlance",  tr.getGzye());
				data.put("payType ", "");
				data.put("bizTraceNo",  tr.getXtckh()+tr.getPzh());
				data.put("oriBizTraceNo", tr.getXtckh()+tr.getPzh());
				data.put("bizTraceTime", DateUtils.getDate("yyyyMMddHHmmss"));
				Map<String, Object> extraMap = new HashMap<String, Object>();
				extraMap.put("cId", tr.getZdh());
				extraMap.put("fixmedins_code", "H44200100009");
				extraMap.put("fixmedins_name", "中山市博爱医院");
				data.put("extra", extraMap);
				
				Map<String, Object>  paramMap = new HashMap<String, Object>();
				paramMap.put("data", data);
				paramMap.put("encType", "plain");
				paramMap.put("signType", "plain");
				paramMap.put("timestamp", "");
				paramMap.put("version", "");
				paramMap.put("appId", "1");
				String hc = HttpClient4.doPost("http://192.168.0.22:5566/zsdzybpz/jshl", paramMap);
				Map<String,Object> hcMap = JsonUtil.readValue(hc, Map.class); 
				if(hcMap.get("code")!=null&&hcMap.get("code").toString().equals("0")){
					reutrnMap.put("code", "0");
				}else{
					reutrnMap.put("code", "-1");
					reutrnMap.put("msg", "调用电子凭证结算回流撤销接口失败,"+hcMap.get("message"));
				}
			}else{
				reutrnMap.put("code", "-1");
				reutrnMap.put("msg", "调用电子凭证结算回流接口失败,入参的pkSettle不能为空！");
			}
		}catch (Exception e) {
			reutrnMap.put("code", "-1");
			reutrnMap.put("msg", "调用电子凭证结算回流接口失败,"+getExceptionDetail(e));
        } 
		return reutrnMap;
	}
	
	
	public static void main(String[] args) {
/*		SettleCallBack settleCallBack = new SettleCallBack();
		SettleCallBackData data = new SettleCallBackData();
		data.setOrgId("301442080620011");
		data.setOrgName("中山市博爱医院");
		data.setOrgType("01");
		data.setMedType("21");
		data.setCityCode("442000");
		data.setEcToken("4400001eu0c22tmo4cda4610ad000078c2b3b1");
		data.setIdNo("43292419791028561X");
		data.setIdType("01");
		data.setUserName("王成礼");
		data.setClientId("53458482");
		data.setStatus("SUCC");
		data.setMessage("");
		data.setOptType("SETTLE");
		data.setMemo("");
		data.setFeeSumAmt("251.63");
		data.setPsnAcctPay("251.63");
		data.setOwnPayAmt("0.00");
		data.setFundPay("0.00");
		data.setInsuranceAmount("0.00");
		data.setAccountBlance("507.69");
		data.setPayType("");
		data.setBizTraceNo("162844907216000138");
		data.setOriBizTraceNo("");
		data.setBizTraceTime("20210208162856");
		SettleCallBackExtra extra = new SettleCallBackExtra();
		extra.setcId("53458482");
		extra.setFixmedins_code("H44200100009");
		extra.setFixmedins_name("中山市博爱医院");
		data.setExtra(extra);
		settleCallBack.setData(data);
		settleCallBack.setAppId("1");*/
		/*Map<String, Object> data = new HashMap<String, Object>();
		data.put("orgId",  "301442080620011");
		data.put("orgName",  "中山市博爱医院");
		data.put("orgType", "01");
		data.put("medType", null);
		data.put("cityCode", "442000");
		data.put("ecToken", "4400001eu0c22tmo4cda4610ad000078c2b3b1");
		data.put("idNo", "43292419791028561X"); 
		data.put("idType", "01");//01居民身份证
		data.put("userName", "王成礼");
		data.put("clientId", "53458482");
		data.put("status", "SUCC");
		data.put("message", null);
		data.put("optType", "SETTLE");
		data.put("memo", null);
		data.put("feeSumamt",  "251.63");
		data.put("psnAcctPay", "251.63");
		data.put("ownpayAmt", "0.00");
		data.put("fundPay",  "0.00");
		data.put("insuranceAmount", "0");
		data.put("accountBlance",  "507.69");
		data.put("payType ", null);
		data.put("bizTraceNo",  "162844907216000138");
		data.put("oriBizTraceNo", "");
		data.put("bizTraceTime", "20210208162856");
		Map<String, Object> extraMap = new HashMap<String, Object>();
		extraMap.put("cId", "53458482");
		extraMap.put("fixmedins_code", "H44200100009");
		extraMap.put("fixmedins_name", "中山市博爱医院");
		data.put("extra", extraMap);
		
		JSONObject paramMap = new JSONObject();
		paramMap.put("data", data);
		paramMap.put("encType", "plain");
		paramMap.put("signType", "plain");
		paramMap.put("timestamp", "");
		paramMap.put("version", "");
		paramMap.put("appId", "1");*/
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orgId",  "301442080620011");
		data.put("orgName",  "中山市博爱医院");
		data.put("orgType", "01");
		data.put("medType", null);
		data.put("cityCode", "442000");
		data.put("ecToken", "4400001eu0c22tmo4cda4610ad000078c2b3b1");
		//data.put("idNo", "43292419791028561X"); 
		data.put("idNo", "F37606D68D08635D2BD811B6DE1E9F1CE1B70CD76BB13B94"); 
		data.put("idType", "01");//01居民身份证
		data.put("userName", "王成礼");
		data.put("clientId", "53458482");
		data.put("status", "SUCC");
		data.put("message", null);
		data.put("optType", "REFUND");
		data.put("memo", null);
		data.put("feeSumamt",  "251.63");
		data.put("psnAcctPay", "251.63");
		data.put("ownpayAmt", "0.00");
		data.put("fundPay",  "0.00");
		data.put("insuranceAmount", "0");
		data.put("accountBlance",  "507.69");
		data.put("payType ", null);
		data.put("bizTraceNo",  "162844907216000138");
		data.put("oriBizTraceNo", "162844907216000138");
		data.put("bizTraceTime", "20210208162856");
		Map<String, Object> extraMap = new HashMap<String, Object>();
		extraMap.put("cId", "53458482");
		extraMap.put("fixmedins_code", "H44200100009");
		extraMap.put("fixmedins_name", "中山市博爱医院");
		data.put("extra", extraMap);
		
		JSONObject paramMap = new JSONObject();
		paramMap.put("data", data);
		paramMap.put("encType", "plain");
		paramMap.put("signType", "plain");
		paramMap.put("timestamp", "");
		paramMap.put("version", "");
		paramMap.put("appId", "1");
		
		String hc = HttpClient4.doPost2("http://192.168.0.22:5566/zsdzybpz/jshl", paramMap);
	}

    /**
     * 获取异常详细信息，知道出了什么错，错在哪个类的第几行 .
     * 
     * @param ex
     * @return
     */
    public static String getExceptionDetail(Exception ex) {
        String ret = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            ex.printStackTrace(pout);
            ret = new String(out.toByteArray());
            pout.close();
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }
}