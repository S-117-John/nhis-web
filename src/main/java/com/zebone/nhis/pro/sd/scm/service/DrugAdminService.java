package com.zebone.nhis.pro.sd.scm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.sd.scm.dao.DrugAdminMapper;
import com.zebone.nhis.pro.sd.scm.vo.RegSzyjPd;
import com.zebone.nhis.pro.sd.scm.vo.RegSzyjPdcompany;
import com.zebone.nhis.pro.sd.scm.vo.RegSzyjPdcontract;
import com.zebone.nhis.pro.sd.scm.vo.RegSzyjPddelivery;
import com.zebone.nhis.pro.sd.scm.vo.RegSzyjPddeliveryDt;
import com.zebone.nhis.pro.sd.scm.vo.RegSzyjPdinvoice;
import com.zebone.nhis.pro.sd.scm.vo.RegSzyjPdinvoicedt;
import com.zebone.nhis.pro.sd.scm.vo.RegSzyjPdmap;
import com.zebone.nhis.pro.sd.scm.vo.RegSzyjPdprice;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 深大医院药监目录对照服务
 * @author jd
 *
 */
@Service
public class DrugAdminService {
	
	@Resource
	private DrugAdminMapper drugAdminMapper;
	
	/**
	 * 022004001001
	 * 药监药品信息保存
	 * @param param
	 * @param user
	 */
	public void saveDrugAdmin(String param,IUser user)
	{
		List<RegSzyjPd> drugList=JsonUtil.readValue(param, new TypeReference<List<RegSzyjPd>>(){} );
		if(drugList==null||drugList.size()==0)return;
		Set<String> codeList=new HashSet<String>();
		for (RegSzyjPd drug : drugList) {
			drug.setPkRegpd(NHISUUID.getKeyId());
			ApplicationUtils.setDefaultValue(drug, true);
			codeList.add(drug.getYpbm());
		}
		//删除表中已存在本次下载的数据集数据，重新保存
		String delWhere="ypbm in ("+CommonUtils.convertSetToSqlInPart(codeList, "ypbm")+")";
		DataBaseHelper.deleteBeanByWhere(new RegSzyjPd(), delWhere);
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(RegSzyjPd.class),drugList);
	}
	
	/**
	 * 022004001002
	 * 获取医院药品目录信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getHospDrugList(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)paramMap=new HashMap<String, Object>();
		//paramMap.put("pkOrg", ((User)user).getPkOrg());
		List<Map<String,Object>> resList=drugAdminMapper.getHospDrugList(paramMap);
		//获取对照信息
		String sql = "SELECT bp.code,rsp.ypbm FROM reg_szyj_pdmap rsp INNER JOIN bd_pd bp ON bp.pk_pd = rsp.pk_pd where bp.del_flag='0'";
		List<Map<String, Object>> mapYpbmList = DataBaseHelper.queryForList(sql, new Object[]{});
		Map<String, String> mapYpbm = new HashMap<String, String>();
		if(mapYpbmList != null && mapYpbmList.size() > 0)
		{
			//list转map,方便通过code找到ypbm
	        for (Map<String, Object> itemMap : mapYpbmList) {
	        	String key = (String)itemMap.get("code");
	        	if(key != null && !"".equals(key))
	        	{
	        		if(!mapYpbm.containsKey(key))
	        		{
	        			mapYpbm.put(key, (String)itemMap.get("ypbm"));
	        		}
	        	}
	        }			
		}
		//补充值
		if(resList != null &&  resList.size() > 0)
		{
			String ypbm = "";
			for (Map<String, Object> item : resList) 
			{    
	        	ypbm = "";
				if(mapYpbm.containsKey((String)item.get("codePd")))
				{
					ypbm = mapYpbm.get((String)item.get("codePd"));
				}
				item.put("ypbm", ypbm);
	        }			
		}
		
		return resList;
	}
	
	/**
	 * 022004001003
	 * 获取药监药品目录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getDrugAdminList(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> resList=drugAdminMapper.getDrugAdminList(paramMap);
		return resList;
	}
	
	/**
	 * 保存药监药品价格信息
	 * @param param
	 * @param user
	 */
	public void saveDrugAdminPrice(String param,IUser user){
		JSONArray array = JSONArray.parseArray(param);
		List<RegSzyjPdprice> pdPriceList=JSONObject.parseArray(array.toJSONString(), RegSzyjPdprice.class);
		if(pdPriceList==null ||pdPriceList.size()==0)return;
		
		Set<String> codeList=new HashSet<String>();
		for (RegSzyjPdprice drug : pdPriceList) {
			drug.setPkPdprice(NHISUUID.getKeyId());
			ApplicationUtils.setDefaultValue(drug, true);
			drug.setBcgxsj(new Date());
			codeList.add(drug.getYpbm());
		}
		//删除表中已存在本次下载的数据集数据，重新保存
		String delWhere="ypbm in ("+CommonUtils.convertSetToSqlInPart(codeList, "ypbm")+")";
		DataBaseHelper.deleteBeanByWhere(new RegSzyjPdprice(), delWhere);
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(RegSzyjPdprice.class),pdPriceList);
	}
	
	/**
	 * 022004001005
	 * 获取药监药品价格信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getDrugAdminPrice(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> resList=drugAdminMapper.getDrugAdminPrice(paramMap);
		return resList;
	}
	
	/**
	 * 
	 * 022004001006
	 * 保存药监企业信息下载数据
	 * @param param
	 * @param user
	 */
	public void saveDrugAdminCompany(String param,IUser user){
		List<RegSzyjPdcompany> companyList=JsonUtil.readValue(param, new TypeReference<List<RegSzyjPdcompany>>() {});
		if(companyList==null || companyList.size()==0)return;
		Set<String> codeList=new HashSet<String>();
		for (RegSzyjPdcompany drug : companyList) {
			drug.setPkPdcomp(NHISUUID.getKeyId());
			ApplicationUtils.setDefaultValue(drug, true);
			drug.setQylx("2");
			codeList.add(drug.getQybm());
		}
		//删除表中已存在本次下载的数据集数据，重新保存
		String delWhere="qybm in ("+CommonUtils.convertSetToSqlInPart(codeList, "qybm")+")";
		DataBaseHelper.deleteBeanByWhere(new RegSzyjPdcompany(), delWhere);
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(RegSzyjPdcompany.class),companyList);
		
	}
	
	/**
	 * 022004001007
	 * 发票下载
	 * @param param
	 * @param user
	 */
	public void saveDrugAdminInvData(String param,IUser user){
		JSONArray array = JSONArray.parseArray(param);
		List<RegSzyjPdinvoice> invDataList=JSONObject.parseArray(array.toJSONString(), RegSzyjPdinvoice.class);
		if(invDataList==null||invDataList.size()==0)return;
		Set<String> codeList=new HashSet<String>();
		List<RegSzyjPdinvoicedt> invDtList=new ArrayList<RegSzyjPdinvoicedt>();
		for (RegSzyjPdinvoice inv : invDataList) {
			inv.setPkPdinv(NHISUUID.getKeyId());
			ApplicationUtils.setDefaultValue(inv, true);
			codeList.add(inv.getFpbh());
			for (RegSzyjPdinvoicedt invdt : inv.getMx()) {
				invdt.setPkPdinvdt(NHISUUID.getKeyId());
				invdt.setPkPdinv(inv.getPkPdinv());
				invdt.setFpbh(inv.getFpbh());
				ApplicationUtils.setDefaultValue(invdt, true);
			}
			invDtList.addAll(inv.getMx());
		}
		
		//删除子表
		String delDtWhere="exists(select 1 from reg_szyj_pdinvoice where reg_szyj_pdinvoice.pk_pdinv=reg_szyj_pdinvoicedt.pk_pdinv and reg_szyj_pdinvoice.fpbh in("+CommonUtils.convertSetToSqlInPart(codeList, "fpbh")+")) ";
		DataBaseHelper.deleteBeanByWhere(new RegSzyjPdinvoicedt(), delDtWhere);
		
		//删除表中已存在本次下载的数据集数据，重新保存
		String delWhere="fpbh in ("+CommonUtils.convertSetToSqlInPart(codeList, "fpbh")+")";
		DataBaseHelper.deleteBeanByWhere(new RegSzyjPdinvoice(), delWhere);
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(RegSzyjPdinvoice.class),invDataList);
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(RegSzyjPdinvoicedt.class),invDtList);
		
	}

	/**
	 * 022004001008
	 * 获取发票汇总信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getInvSumDataList(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		return drugAdminMapper.getInvSumDataList(paramMap);
	}
	
	/**
	 * 022004001009
	 * 获取发票明细数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getInvDtDataList(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		List<Map<String,Object>> resList=drugAdminMapper.getInvDataDtList(paramMap);
		return resList;
	}
	
	/**
	 * 022004001010
	 * @param param
	 * @param user
	 */
	public void saveDrugRefHosp(String param,IUser user){
		RegSzyjPdmap pdMap=JsonUtil.readValue(param, RegSzyjPdmap.class);
		if(pdMap==null)return;
		String delWhere="pk_pd ='"+pdMap.getPkPd()+"' and ypbm='"+pdMap.getYpbm()+"'";
		DataBaseHelper.deleteBeanByWhere(new RegSzyjPdmap(), delWhere);
		
		ApplicationUtils.setDefaultValue(pdMap, true);
		DataBaseHelper.insertBean(pdMap);
	}
	
	/**
	 * 022004001011
	 * 获取药品对照数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getDrugRefMapper(String param,IUser user){
		String searchTxt=JsonUtil.getFieldValue(param, "searchTxt");
		List<Map<String,Object>> resList=drugAdminMapper.getDrugRefMapper(searchTxt);
		return resList;
		
	}
	
	/**
	 * 022004001012
	 * 三方合同剩余量下载
	 * @param param
	 * @param user
	 */
	public void saveDrugAdminContract(String param,IUser user){
		JSONArray array = JSONArray.parseArray(param);
		List<RegSzyjPdcontract> contList=JSONObject.parseArray(array.toJSONString(), RegSzyjPdcontract.class);
		
		if(contList==null||contList.size()==0)return;
		Set<String> codeList=new HashSet<String>();
		for (RegSzyjPdcontract con : contList) {
			con.setPkPdcomp(NHISUUID.getKeyId());
			ApplicationUtils.setDefaultValue(con, true);
			codeList.add(con.getHtbh());
		}
		
		String delWhere="htbh in ("+CommonUtils.convertSetToSqlInPart(codeList, "htbh")+")";
		DataBaseHelper.deleteBeanByWhere(new RegSzyjPdcontract(), delWhere);
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(RegSzyjPdcontract.class),contList);
		
	}
	
	/**
	 * 022004001013
	 * 获取三方合同剩余量
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getDrugAdminContract(String param,IUser user){
		return drugAdminMapper.getDrugAdminContract();
	}
	
	/**
	 * 022004001014
	 * 保存配送单下载数据
	 * @param param
	 * @param user
	 */
	public void saveDrugAdminPrep(String param,IUser user){
		RegSzyjPddelivery pddelive=JSONObject.parseObject(param, RegSzyjPddelivery.class);
		if(pddelive==null)return;
		String delSqldt="exists (select 1 from reg_szyj_pddelivery where reg_szyj_pddelivery_dt.pk_pddiv=reg_szyj_pddelivery.pk_pddiv and reg_szyj_pddelivery.cgjhbh='"+pddelive.getCgjhbh()+"')";
		DataBaseHelper.deleteBeanByWhere(new RegSzyjPddeliveryDt(), delSqldt);
		String delSql=" reg_szyj_pddelivery.cgjhbh='"+pddelive.getCgjhbh()+"'";
		DataBaseHelper.deleteBeanByWhere(new RegSzyjPddelivery(), delSql);
		
		ApplicationUtils.setDefaultValue(pddelive, true);
		pddelive.setEuStatus("0");
		DataBaseHelper.insertBean(pddelive);
		
		for (RegSzyjPddeliveryDt pddelivedt : pddelive.getMx()) {
			pddelivedt.setPkPddivdt(NHISUUID.getKeyId());
			pddelivedt.setPkPddiv(pddelive.getPkPddiv());
			ApplicationUtils.setDefaultValue(pddelivedt, true);
		}
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(RegSzyjPddeliveryDt.class), pddelive.getMx());
	}
	
	/**
	 * 022004001015
	 * 获取配送单数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getDrugAdminPrepOrder(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> resList=drugAdminMapper.getDrugAdminPrepOrder(paramMap);
		return resList;
	}
	
	/**
	 * 022004001016
	 * 获取配送单数据明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getDrugAdminPrepOrderDt(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		List<Map<String,Object>> resList=drugAdminMapper.getDrugAdminPrepOrderDt(paramMap);
		return resList;
	}
	
	/**
	 * 022004001017
	 * 获取上次更新时间
	 * @param param
	 * @param user
	 * @return
	 */
	public String getUpdateDate(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		return drugAdminMapper.getUpdateDate(paramMap);
	}
	
	/**
	 * 022004001018
	 * 变更配送单状态
	 * @param param
	 * @param user
	 */
	public void updateDrugAdminStatus(String param,IUser user){
		String pkPddiv=JsonUtil.getFieldValue(param, "pkPddiv");
		String pkPdst=JsonUtil.getFieldValue(param, "pkPdst");
		String sql="update reg_szyj_pddelivery set eu_status='1',pk_pdst=? where pk_pddiv=?";
		DataBaseHelper.execute(sql, new Object[]{pkPdst,pkPddiv});
		
	}
	
	/**
	 * 入库上传数据返回成功业务处理
	 * @param param
	 * @param user
	 */
	public void updateUpInstkStatus(String param,IUser user){
		String codePlan=JsonUtil.getFieldValue(param, "codePlan");
		String codeSt=JsonUtil.getFieldValue(param, "codeSt");
		String codeRtn=JsonUtil.getFieldValue(param, "codeRtn");
		String plan="update pd_plan set eu_status='3' where code_plan=?";
		DataBaseHelper.execute(plan, new Object[]{codePlan});
		
		String st="update pd_st set code_rtn=? where code_st=?";
		DataBaseHelper.execute(st, new Object[]{codeRtn,codeSt});
	}
	
	/**
	 * 退货上传数据返回成功业务处理
	 * @param param
	 * @param user
	 */
	public void updateRetGoodsStUpData(String param,IUser user){
		String codePlan=JsonUtil.getFieldValue(param, "codePlan");
		String codeSt=JsonUtil.getFieldValue(param, "codeSt");
		String codeRtn=JsonUtil.getFieldValue(param, "codeRtn");
		String plan="update pd_plan set eu_status='4' where code_plan=?";
		DataBaseHelper.execute(plan, new Object[]{codePlan});
		
		String st="update pd_st set code_rtn=? where code_st=?";
		DataBaseHelper.execute(st, new Object[]{codeRtn,codeSt});
	}
	
	/**
	 * 022004001020
	 * 获取药监药品编码
	 * @param param
	 * @param user
	 * @return
	 */
	public String getDrugadminBypkPd(String param,IUser user){
		String pkPd=JsonUtil.getFieldValue(param, "pkPd");
		String name=JsonUtil.getFieldValue(param, "name");
		String sql="select ypbm from REG_SZYJ_PDMAP where PK_PD=?";
		
		List<Map<String,Object>> resList=DataBaseHelper.queryForList(sql, new Object[]{pkPd});
		if(resList==null ||resList.size()>1 ||resList.get(0).get("ypbm")==null){
			throw new BusException("医院药品目录:药品【"+name+"】对照有误，请重新处理！");
		}
		return resList.get(0).get("ypbm").toString();
	}
}
