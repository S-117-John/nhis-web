package com.zebone.nhis.compay.ins.shenzhen.service.city;


import com.google.common.collect.Lists;
import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleBudget;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.opdw.CnOpEmrRecord;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybPv;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybSt;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybStDt;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybVisit;
import com.zebone.nhis.common.module.compay.ins.shenzhen.*;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.InsQgybPV;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.compay.ins.shenzhen.dao.city.ShenzhenCityMapper;
import com.zebone.nhis.compay.ins.shenzhen.vo.city.*;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShenzhenCityService {

	private static Logger logger = LogManager.getLogger(ShenzhenCityService.class);
	@Autowired
	private  ShenzhenCityMapper shenzhenCityMapper;
	@Autowired
	private IpCgPubService ipCgPubService;

	/**
	 * 015001011001 ????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public String saveVisit(String param, IUser user) {
		User u = (User) user;
		VisitInfoMore visitInfoMore= JsonUtil.readValue(param, VisitInfoMore.class);
		InsSzybVisit visitVo = visitInfoMore.getInsSzybVisit();
		InsSzybVisitCityVo insSzybVisitCity=visitInfoMore.getInsSzybVisitCity();
		String sqlStr="";
		// ????????????  1    
		if (visitVo != null ) {
			visitVo.setPkOrg(u.getPkOrg());
			visitVo.setTs(new Date());
			if (visitVo.getPkVisit() != null && StringUtils.isNotBlank(visitVo.getPkVisit() )) {
				DataBaseHelper.updateBeanByPk(visitVo, false);
			} else {
				DataBaseHelper.execute("delete from ins_szyb_visit where (PK_PV=? or PVCODE_INS=?) and code_insst is null",
						new Object[] { visitVo.getPkPv(),visitVo.getPvcodeIns() });

				visitVo.setDelFlag("0");
				visitVo.setCreateTime(new Date());
				visitVo.setCreator(u.getPkEmp());
				DataBaseHelper.insertBean(visitVo);
			}
		}


		if("1".equals(visitVo.getDelFlag())){
			DataBaseHelper.execute("update ins_szyb_visit_city set del_flag='1' where pk_visit=?",
					new Object[] { visitVo.getPkVisit()});
		}

		//????????????_????????????   
		if (insSzybVisitCity != null) {
			insSzybVisitCity.setPkOrg(u.getPkOrg());
			insSzybVisitCity.setTs(new Date());
			insSzybVisitCity.setPkVisit(visitVo.getPkVisit());
			if (insSzybVisitCity.getPkVisitcity() != null && StringUtils.isNotBlank(insSzybVisitCity.getPkVisitcity())) {
				DataBaseHelper.updateBeanByPk(insSzybVisitCity, false);
			} else {
				DataBaseHelper.execute("delete from ins_szyb_visit_city city where city.PK_PV=? and exists(select 1 from ins_szyb_visit visit where (visit.PK_PV=? or visit.PVCODE_INS=?) and visit.code_insst is null and visit.pk_visit = city.pk_visit)",
						new Object[] { insSzybVisitCity.getPkPv(),visitVo.getPkPv(),visitVo.getPvcodeIns()});
				
				insSzybVisitCity.setDelFlag("0");
				insSzybVisitCity.setCreateTime(new Date());
				insSzybVisitCity.setCreator(u.getPkEmp());
				
				InsSzybVisitCity newCity=new InsSzybVisitCity();
				ApplicationUtils.copyProperties(newCity, insSzybVisitCity);
				DataBaseHelper.insertBean(newCity);
				
				
			}
		}
		
		
		//???????????????????????????????????????????????????(??????F00000000013??????F00000105411)
		if(visitVo!=null && StringUtils.isNotBlank(visitVo.getPkPv()) && insSzybVisitCity!=null && "310".equals(insSzybVisitCity.getAae140())){
			//??????
			List<RefundVo> refundList = new ArrayList<RefundVo>();
			//??????
			List<BlPubParamVo> chargeList = new ArrayList<BlPubParamVo>();
			
			BdItem itemF00000105411=DataBaseHelper.queryForBean("select * from bd_item where del_flag='0' and code='F00000105411'", BdItem.class,new Object[]{});
			Map<String,Object>  mapParam=new HashMap<String,Object> ();
			mapParam.put("pkPv", visitVo.getPkPv());
			List<BlIpDt> f00000000013List = shenzhenCityMapper.qryF00000000013(mapParam);
			String f00000000013PkItem="";
			if(f00000000013List!=null && f00000000013List.size()>0){
				for(BlIpDt blIpDt : f00000000013List){
					f00000000013PkItem=blIpDt.getPkItem();
					
					RefundVo refVo =  new RefundVo();
					refVo.setPkCgip(blIpDt.getPkCgip());
					refVo.setQuanRe(blIpDt.getQuan());
					refundList.add(refVo);
					
					BlPubParamVo chargeVo = new BlPubParamVo();
					chargeVo.setPkOrg(blIpDt.getPkOrg());
					chargeVo.setPkOrgApp(blIpDt.getPkOrgApp());
					chargeVo.setPkOrgEx(blIpDt.getPkOrgEx());
					chargeVo.setPkDeptApp(blIpDt.getPkDeptApp());
					chargeVo.setPkDeptCg(blIpDt.getPkDeptCg());
					chargeVo.setPkDeptEx(blIpDt.getPkDeptEx());
					chargeVo.setPkEmpApp(blIpDt.getPkEmpApp());
					chargeVo.setNameEmpApp(blIpDt.getNameEmpApp());
					chargeVo.setPkEmpCg(blIpDt.getPkEmpCg());
					chargeVo.setNameEmpCg(blIpDt.getNameEmpCg());
					chargeVo.setDateHap(blIpDt.getDateHap());
					chargeVo.setPkPv(blIpDt.getPkPv());
					chargeVo.setPkPi(blIpDt.getPkPi());
					chargeVo.setQuanCg(blIpDt.getQuan());
					chargeVo.setEuBltype(blIpDt.getEuBltype());
					chargeVo.setFlagPd("0");
					chargeVo.setEuPvType(insSzybVisitCity.getEuPvType());
					chargeVo.setPkItem(itemF00000105411.getPkItem());
					chargeVo.setPrice(itemF00000105411.getPrice());
					
					chargeList.add(chargeVo);
				}
				
				BlPubReturnVo resRefundInBatch = ipCgPubService.refundInBatch(refundList);
				ipCgPubService.chargeIpBatch(chargeList,false);
				
			}else{
				BdItem itemF00000000013=DataBaseHelper.queryForBean("select * from bd_item where del_flag='0' and code='F00000000013'", BdItem.class,new Object[]{});
				f00000000013PkItem=itemF00000000013.getPkItem();
			}	
			
			sqlStr="update pv_daycg_detail set pk_item=?  where del_flag='0' and pk_item=? and pk_daycg in (select pk_daycg from pv_daycg where pk_pv=? and del_flag='0')";
			DataBaseHelper.update(sqlStr,new Object[]{itemF00000105411.getPkItem(),f00000000013PkItem,visitVo.getPkPv()});
		}

		return visitVo.getPkVisit();
	}
	
	
	/**
	 * 015001011093 ??????????????????????????????HIS???????????????????????????????????????
	 * 
	 * @return
	 */
	public void reNewalVisit(String param, IUser user) {
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if (StringUtils.isNotBlank(paramMap.get("pkPv").toString())  && StringUtils.isNotBlank(paramMap.get("pkPi").toString())   ) {
			DataBaseHelper.update("update ins_szyb_visit set PK_PV=:pkPv,PK_PI=:pkPi where pk_visit=:pkVisit",paramMap);
			DataBaseHelper.update("update ins_szyb_visit_city set PK_PV=:pkPv where pk_visit=:pkVisit",paramMap);
		} else {
			throw new BusException("?????????????????????");
		}
	}
	

	/**
	 * 015001011049 -- ????????????????????????
	 * ????????????????????????pv_encounter????????????????????????????????????SaveZyHpRegRelationship??????
	 * ??????????????????????????????????????????pv_encounter??????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public void delPvEncounter(String param, IUser user) {
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		DataBaseHelper.execute("update pv_encounter set eu_status='9',del_flag='1' where PK_PV=? ",
				new Object[] { paramMap.get("pkPv")});
	}

	/**
	 * 015001011002 ????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryVisit(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = shenzhenCityMapper.qryVisit(paramMap);
		if(list==null || list.size()<=0){
			throw new BusException("?????????????????????????????????");
		}

		return list.get(0);
	}

	/**
	 * 015001011050 ??????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryVisitCity(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = shenzhenCityMapper.qryVisitCity(paramMap);
		if((list==null) || (list!=null && list.size()<1)){
			throw new BusException("????????????????????????????????????????????????");
		}

		return list.get(0);
	}

	/**
	 * 015001011006 ??????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryUploadDetail(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnList=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> allList = shenzhenCityMapper.qryUploadDetail(paramMap);
		List<String> zeroAmoutList = shenzhenCityMapper.qryUploadDetailZeroAmount(paramMap);

		for(Map<String,Object> item : allList){
			Boolean flagZero=false;
			for(String pkitem:zeroAmoutList){
				if(pkitem.equals(item.get("pkItem").toString())){
					flagZero=true;
				}
			}
			if(!flagZero){
				rtnList.add(item);
			}
		}

		return rtnList;
	}

	/**
	 * 015001011083 ??????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryUploadPresDetail(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> allList = shenzhenCityMapper.qryUploadPresDetail(paramMap);

		return allList;
	}

	/**
	 * 015001011007 ??????????????????-???????????????????????????????????????bl_ip_dt???flag_insu??????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public void updateFlagInsuByPk(String param , IUser user){
		//Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		IntegrateParam integrateParam = JsonUtil.readValue(param,IntegrateParam.class);
		List<InsSzybCitycg> insSzybCitycgList = integrateParam.getInsSzybCitycgList();
		Map<String,Object>  paramMap=integrateParam.getUnListParamMap();
		Date dateNow=new Date();
		User userInfo = (User) user;

		shenzhenCityMapper.updateFlagInsuByPk(paramMap);

		String codeInsst=paramMap.get("codeInsst").toString();
		String sql = "update ins_szyb_visit set code_insst=? where pk_pv=?";
		DataBaseHelper.update(sql, new Object[] { codeInsst,paramMap.get("pkPv").toString() });


		//???????????????INS_SZYB_CITYCG
		if(insSzybCitycgList!=null && insSzybCitycgList.size()>0){
			for(InsSzybCitycg insSzybCitycg :insSzybCitycgList){
				insSzybCitycg.setPkInscg(NHISUUID.getKeyId());
				insSzybCitycg.setCreator(userInfo.getPkEmp());
				insSzybCitycg.setTs(dateNow);
				insSzybCitycg.setPkOrg(userInfo.getPkOrg());
				insSzybCitycg.setCreateTime(dateNow);
				insSzybCitycg.setDelFlag("0");
			}

			String sqlStr="delete from INS_SZYB_CITYCG where pk_cgip=:pkCgip";
			DataBaseHelper.batchUpdate(sqlStr ,insSzybCitycgList);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybCitycg.class), insSzybCitycgList);
		}

	}

	/**
	 * 015001011082  	????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public void updatePres(String param , IUser user){
		//Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		IntegrateParam integrateParam = JsonUtil.readValue(param,IntegrateParam.class);
		List<InsSzybCitycg> insSzybCitycgList = integrateParam.getInsSzybCitycgList();
		Map<String,Object>  paramMap=integrateParam.getUnListParamMap();
		Date dateNow=new Date();
		User userInfo = (User) user;

		String sql = "update cn_prescription set flag_insu=:flagInsu,code_insu=:codeInsu where pk_pres=:pkPres";
		DataBaseHelper.update(sql, paramMap);

		//???????????????INS_SZYB_CITYCG
		if(insSzybCitycgList!=null && insSzybCitycgList.size()>0){
			for(InsSzybCitycg insSzybCitycg :insSzybCitycgList){
				insSzybCitycg.setPkInscg(NHISUUID.getKeyId());
				insSzybCitycg.setCreator(userInfo.getPkEmp());
				insSzybCitycg.setTs(dateNow);
				insSzybCitycg.setCreateTime(dateNow);
				insSzybCitycg.setPkOrg(userInfo.getPkOrg());
				insSzybCitycg.setDelFlag("0");
			}

			String sqlStr="delete from INS_SZYB_CITYCG where pk_cgip=:pkCgip";
			DataBaseHelper.batchUpdate(sqlStr ,insSzybCitycgList);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybCitycg.class), insSzybCitycgList);
		}
	}

	/**
	 * 015001011008 ??????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>>  qryPvDiag(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = shenzhenCityMapper.qryPvDiag(paramMap);
		return list;
	}

	/**
	 * 015001011010 ???????????????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryBlStatistical(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> presNoList = shenzhenCityMapper.qryPresNo(paramMap);
		if((presNoList==null) || (presNoList!=null && presNoList.size()!=1)){
			throw new BusException("?????????????????????????????????????????????????????????????????????");
		}

		Map<String,Object> map = shenzhenCityMapper.qryBlStatistical(paramMap);
		map.put("presNo", presNoList.get(0).get("presno").toString());

		return map;
	}

	/**
	 * 015001011011 ??????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public String addInsSzybSt(String param, IUser user) {
		SettleInfoMore settleInfoMore = JsonUtil.readValue(param, SettleInfoMore.class);
		InsSzybSt st = settleInfoMore.getInsSzybSt();
		InsSzybStCity stCity = settleInfoMore.getInsSzybStCity();
		List<InsSzybStCitydt> insSzybStCitydtList=settleInfoMore.getInsSzybStCitydtList();

		Date dt=new Date();
		User u = (User) user;
		if (st == null)
			throw new BusException("??????????????????????????????");
		if (stCity == null)
			throw new BusException("?????????????????????????????????");

		//????????????
		String sql = "update ins_szyb_st set del_flag='1' where pk_visit=? and code_hpst is null";
		DataBaseHelper.update(sql, new Object[] { st.getPkVisit() });

		//????????????--???????????????
		sql = "update ins_szyb_st_city set del_flag='1' where pk_insst in (select pk_insst from ins_szyb_st where pk_visit=?  and code_hpst is null)";
		DataBaseHelper.update(sql, new Object[] { st.getPkVisit() });

		//????????????--???????????????--????????????
		sql = "select * from ins_szyb_st_city where pk_insst in (select pk_insst from ins_szyb_st where pk_visit=? and code_hpst is null)";
		List<InsSzybStCity> insSzybStCityList = DataBaseHelper.queryForList(sql, InsSzybStCity.class, new Object[] { st.getPkVisit() });
		if(insSzybStCityList!=null && insSzybStCityList.size()>0){
			sql = "update ins_szyb_st_citydt set del_flag='1' where pk_insstcity = :pkInsstcity ";
			DataBaseHelper.batchUpdate(sql, insSzybStCityList);
		}
		

//		sql = "update ins_szyb_visit set code_insst=null  where pk_visit=?";
//		DataBaseHelper.update(sql, new Object[] { st.getPkVisit() });


		//??????ins_szyb_st
		st.setPkOrg(u.getPkOrg());
		st.setDelFlag("0");
		st.setCreateTime(dt);
		st.setCreator(u.getPkEmp());
		st.setTs(dt);
		DataBaseHelper.insertBean(st);

		//??????ins_szyb_st_city
		stCity.setPkInsst(st.getPkInsst());
		stCity.setPkOrg(u.getPkOrg());
		stCity.setDelFlag("0");
		stCity.setCreateTime(dt);
		stCity.setCreator(u.getPkEmp());
		stCity.setTs(dt);
		DataBaseHelper.insertBean(stCity);

		//??????ins_szyb_st_citydt
		if(insSzybStCitydtList!=null && insSzybStCitydtList.size()>0){
			for(InsSzybStCitydt insSzybStCitydt:insSzybStCitydtList){
				insSzybStCitydt.setPkInsstcitydt(NHISUUID.getKeyId());
				insSzybStCitydt.setPkInsstcity(stCity.getPkInsstcity());
				insSzybStCitydt.setDelFlag("0");
				insSzybStCitydt.setCreateTime(dt);
				insSzybStCitydt.setCreator(u.getPkEmp());
				insSzybStCitydt.setTs(dt);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybStCitydt.class), insSzybStCitydtList);
		}
		
		return st.getPkInsst();
	}


	/**
	 * 015001011012
	 * ????????????????????????????????????PK_PV???PkSettle?????????????????????INS_GZYB_ST???????????????INS_GZYB_VISIT???
	 *
	 * @param param
	 * @param user
	 */
	public void updatePkSettle(String param, IUser user) {
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String pkPv = paramMap.get("pkPv").toString();
		
		String pkSettle = null;
		String ywlx = null;//????????????
		String pkInsst = null;//??????????????????
		String pkVisit = null;
		
		if(paramMap.containsKey("pkSettle") && paramMap.get("pkSettle")!=null){
			pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		}
		if(paramMap.containsKey("ywlx") && paramMap.get("ywlx")!=null){
			ywlx = CommonUtils.getString(paramMap.get("ywlx"));
		}
		if(paramMap.containsKey("pkInsst") && paramMap.get("pkInsst")!=null){
			pkInsst = CommonUtils.getString(paramMap.get("pkInsst"));
		}
		//?????????????????????????????????????????????????????????
		if(!CommonUtils.isEmptyString(ywlx) && "0".equals(ywlx)){
			if(paramMap.containsKey("pkVisit") && paramMap.get("pkVisit")!=null){
				pkVisit = CommonUtils.getString(paramMap.get("pkVisit"));
			}
			
			BlSettle stInfo = new BlSettle();
			if(!CommonUtils.isEmptyString(pkSettle)){
				//??????????????????
				stInfo = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle = ?", BlSettle.class, new Object[]{pkSettle});
			}
			//????????????????????????
			PvEncounter pvInfo = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where pk_pv = ?", PvEncounter.class, new Object[]{pkPv});
			//????????????????????????
			PiMaster master = DataBaseHelper.queryForBean("select * from pi_master where pk_pi = ?", PiMaster.class, new Object[]{pvInfo.getPkPi()});
			//????????????????????????
			BdHp hpInfo = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp = ?", BdHp.class, new Object[]{pvInfo.getPkInsu()});
			
			//????????????????????????
			if(!CommonUtils.isEmptyString(pkInsst)){
				InsSzybSt insuStInfo = DataBaseHelper.queryForBean("select * from ins_szyb_st where PK_insst = ?", InsSzybSt.class, new Object[]{pkInsst});
				insuStInfo.setPkHp(pvInfo.getPkInsu());
				insuStInfo.setPkPv(pvInfo.getPkPv());
				insuStInfo.setPkPi(pvInfo.getPkPi());
				insuStInfo.setPkSettle(pkSettle);
				insuStInfo.setDateSt(stInfo!=null && stInfo.getDateSt()!=null?stInfo.getDateSt():new Date());
				DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsSzybSt.class), insuStInfo);
			}
			
			//????????????????????????
			InsSzybVisit visitInfo = DataBaseHelper.queryForBean("select * from ins_szyb_visit where pk_visit = ?", InsSzybVisit.class, new Object[]{pkVisit});
			visitInfo.setPkHp(pvInfo.getPkInsu());
			visitInfo.setPkPv(pvInfo.getPkPv());
			visitInfo.setPkPi(pvInfo.getPkPi());
			visitInfo.setBirthDate(master.getBirthDate());
			visitInfo.setEuStatusSt("0");
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsSzybVisit.class), visitInfo);
			
			//??????????????????????????????
			DataBaseHelper.execute("update ins_szyb_visit_city set bcc334=?,pk_pv=? where pk_visit = ?",hpInfo.getCode(),pvInfo.getPkPv(),pkVisit);
		}else if(!CommonUtils.isEmptyString(ywlx) && "1".equals(ywlx)){//????????????
			if(!CommonUtils.isEmptyString(pkInsst)){
				DataBaseHelper.execute("update ins_szyb_st set PK_SETTLE=? where del_flag='0' and PK_PV=? and pk_insst = ?",pkSettle, pkPv,pkInsst);
				DataBaseHelper.execute("update ins_szyb_visit set EU_STATUS_ST='1' where del_flag='0' and PK_VISIT in (select PK_VISIT from ins_szyb_st where pk_insst = ?)",pkInsst);
			}
		}else{
			//?????????????????????????????????????????????????????????
			DataBaseHelper.execute("update ins_szyb_st_city set del_flag = '1' where pk_insst in ( select PK_INSST from ins_szyb_st where pk_pv = ? and code_hpst is null )", pkPv);
			DataBaseHelper.execute("update ins_szyb_st set del_flag = '1' where pk_pv = ? and code_hpst is null ", pkPv);
			
			List<InsSzybSt> stList = DataBaseHelper.queryForList(
					"select * from ins_szyb_st where pk_pv = ? and pk_settle is null and del_flag='0' order by date_st desc",
					InsSzybSt.class, new Object[]{pkPv});
			
			if(stList!=null && stList.size()>0){
				InsSzybSt stVo = stList.get(0);
				stVo.setPkSettle(pkSettle);
				
				DataBaseHelper.updateBeanByPk(stVo);
			}
			
			//DataBaseHelper.execute("update ins_szyb_st set PK_SETTLE=? where del_flag='0' and PK_PV=? and pk_settle is null",pkSettle, pkPv);
			DataBaseHelper.execute("update ins_szyb_visit set EU_STATUS_ST='1' where del_flag='0' and PK_PV=? ",pkPv);
		}
	}

	/**
	 * 015001011015
	 * ????????????????????????
	 *
	 * @param param
	 * @param user
	 */
	public void emptyPkSettle(String param, IUser user) {
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
		String pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		String pkVisit = CommonUtils.getString(paramMap.get("pkVisit"));
		String pkInsst = null;
		if(paramMap.containsKey("pkInsst")){
			pkInsst = CommonUtils.getString(paramMap.get("pkInsst"));
		}
		String codeHpstCanc = "";
		if(paramMap.containsKey("codeHpstCanc")){
			Object obj=paramMap.get("codeHpstCanc");
			codeHpstCanc=obj==null?"":obj.toString();
		}
		Date dateNow = new Date();
		
		if(!CommonUtils.isEmptyString(pkSettle)){
			//????????????????????????
			String sql = "update ins_szyb_citycg set del_flag='1' where pk_cgip in (select pk_cgip from bl_ip_dt where pk_pv=? and pk_settle=?)";
			DataBaseHelper.update(sql, new Object[] { pkPv,pkSettle });

			//????????????--???????????????
			sql = "update ins_szyb_st_citydt set del_flag='1' where pk_insstcity in( select pk_insstcity from ins_szyb_st_city where pk_insst in (select pk_insst from ins_szyb_st where pk_pv=? and pk_settle=?))";
			DataBaseHelper.update(sql, new Object[] { pkPv,pkSettle });

			//??????--???????????????
			sql = "update ins_szyb_st_city set del_flag='1' where pk_insst in (select pk_insst from ins_szyb_st where pk_pv=? and pk_settle=?)";
			DataBaseHelper.update(sql, new Object[] { pkPv,pkSettle });

			//??????????????????
			sql = "update ins_szyb_st set del_flag='1',code_hpst_canc=?, canc_time=? where pk_pv=? and pk_settle=?";
			DataBaseHelper.update(sql, new Object[] {codeHpstCanc, dateNow,pkPv,pkSettle });

			//??????????????????--???????????????
			sql = "update ins_szyb_visit set EU_STATUS_ST='0' where pk_visit=?";//,code_insst=null
			DataBaseHelper.update(sql, new Object[] { pkVisit });

			sql = "update bl_ip_dt set flag_insu= '0',name_itemset=null where pk_pv=? and pk_settle=?";
			DataBaseHelper.update(sql, new Object[] { pkPv,pkSettle });
		}else if(!CommonUtils.isEmptyString(pkInsst)){
			String sql = null;
			
			//????????????--???????????????
			sql = "update ins_szyb_st_citydt set del_flag='1' where pk_insstcity in( select pk_insstcity from ins_szyb_st_city where pk_insst = ?)";
			DataBaseHelper.update(sql, new Object[] { pkInsst });
			
			//??????--???????????????
			sql = "update ins_szyb_st_city set del_flag='1' where pk_insst = ?";
			DataBaseHelper.update(sql, new Object[] { pkInsst });
			
			//??????????????????
			sql = "update ins_szyb_st set del_flag='1',code_hpst_canc=?, canc_time=? where pk_insst = ?";
			DataBaseHelper.update(sql, new Object[] {codeHpstCanc, dateNow,pkInsst });
			
			//??????????????????--???????????????
			sql = "update ins_szyb_visit set EU_STATUS_ST='0' where pk_visit in (select pk_insst from ins_szyb_st where pk_insst=?)";
			DataBaseHelper.update(sql, new Object[] { pkInsst });
		}
	}
		

	/**
	 * 015001011014  ??????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>>  qryInsSzybSt(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = shenzhenCityMapper.qryInsSzybSt(paramMap);
		return list;
	}





	/**
	 * 015001011017
	 *
	 * ??????????????????????????????????????????ins_szyb_item??????
	 *
	 * @param szybDrugInfo
	 *
	 */
	public void saveDrugInfo(String param, IUser user){
		SzYbDrugInfo szYbDrugInfoParam=JsonUtil.readValue(param, SzYbDrugInfo.class);

		int drugInfoSize = szYbDrugInfoParam.getOutputlist().size();

		List<InsSzybItem> insSzybItemList = new ArrayList<InsSzybItem>();
		for(int i = 0 ; i < drugInfoSize; i++ ){
			InsSzybItem insSzybItem = new InsSzybItem();
			SzYbDrugInfoList szYbDrugInfoList = szYbDrugInfoParam.getOutputlist().get(i);
			insSzybItem.setPkInsitem(NHISUUID.getKeyId());
			insSzybItem.setEuHpdicttype("01");
			insSzybItem.setAae013(szYbDrugInfoList.getAae013());
			insSzybItem.setAae396(stringTranInt(szYbDrugInfoList.getAae396()));
			insSzybItem.setAka020(szYbDrugInfoList.getAka020());
			insSzybItem.setAka022(szYbDrugInfoList.getAka022());
			insSzybItem.setAka031(szYbDrugInfoList.getAka031());
			insSzybItem.setAka036(szYbDrugInfoList.getAka036());
			insSzybItem.setAka061(szYbDrugInfoList.getAka061());
			insSzybItem.setAka062(szYbDrugInfoList.getAka062());
			insSzybItem.setAka064(szYbDrugInfoList.getAka064());
			insSzybItem.setAka065(szYbDrugInfoList.getAka065());
			insSzybItem.setAka067(szYbDrugInfoList.getAka067());
			insSzybItem.setAka068(stringTranDouble(szYbDrugInfoList.getAka068()));
			insSzybItem.setAka070(szYbDrugInfoList.getAka070());
			insSzybItem.setAka074(szYbDrugInfoList.getAka074());
			insSzybItem.setAka111(szYbDrugInfoList.getAka111());
			insSzybItem.setAkb020(szYbDrugInfoList.getAkb020());
			insSzybItem.setAkc222(szYbDrugInfoList.getAkc222());
			insSzybItem.setAke001(szYbDrugInfoList.getAke001());
			insSzybItem.setAke002(szYbDrugInfoList.getAke002());
			insSzybItem.setAke003(szYbDrugInfoList.getAke003());
			insSzybItem.setBke111(szYbDrugInfoList.getBke111());
			insSzybItem.setBkm007(szYbDrugInfoList.getBkm007());
			insSzybItem.setBkm008(szYbDrugInfoList.getBkm008());
			insSzybItem.setAma011(szYbDrugInfoList.getAma011());
			insSzybItem.setAla011(szYbDrugInfoList.getAla011());
			insSzybItem.setBka053(szYbDrugInfoList.getBka053());
			insSzybItem.setBka640(szYbDrugInfoList.getBka640());
			insSzybItem.setBkm009(stringTranInt(szYbDrugInfoList.getBkm009()));
			insSzybItem.setBkm017(szYbDrugInfoList.getBkm017());
			insSzybItem.setBkm030(szYbDrugInfoList.getBkm030());
			insSzybItem.setBkm032(szYbDrugInfoList.getBkm032());
			insSzybItem.setBkm033(stringTranInt(szYbDrugInfoList.getBkm033()));
			insSzybItem.setBkm034(stringTranInt(szYbDrugInfoList.getBkm034()));
			insSzybItem.setBkm035(szYbDrugInfoList.getBkm035());
			insSzybItem.setBkm036(stringTranInt(szYbDrugInfoList.getBkm036()));
			insSzybItem.setBkm037(stringTranInt(szYbDrugInfoList.getBkm037()));
			insSzybItem.setBkm038(szYbDrugInfoList.getBkm038());
			insSzybItem.setBkm039(stringTranInt(szYbDrugInfoList.getBkm039()));
			insSzybItem.setBkm040(stringTranInt(szYbDrugInfoList.getBkm040()));
			insSzybItem.setBkm041(szYbDrugInfoList.getBkm041());
			insSzybItem.setBkm042(stringTranInt(szYbDrugInfoList.getBkm042()));
			insSzybItem.setBkm043(stringTranInt(szYbDrugInfoList.getBkm043()));
			insSzybItem.setBkm044(szYbDrugInfoList.getBkm044());
			insSzybItem.setBkm045(stringTranInt(szYbDrugInfoList.getBkm045()));
			insSzybItem.setBkm046(stringTranInt(szYbDrugInfoList.getBkm046()));
			insSzybItem.setBkm047(szYbDrugInfoList.getBkm047());
			insSzybItem.setBkm048(stringTranInt(szYbDrugInfoList.getBkm048()));
			insSzybItem.setBkm049(stringTranInt(szYbDrugInfoList.getBkm049()));
			insSzybItem.setBkm054(szYbDrugInfoList.getBkm054());
			insSzybItem.setBkm081(stringTranInt(szYbDrugInfoList.getBkm081()));
			insSzybItem.setBkm082(stringTranInt(szYbDrugInfoList.getBkm082()));
			insSzybItem.setBkm083(stringTranInt(szYbDrugInfoList.getBkm083()));
			insSzybItem.setBkm090(stringTranDouble(szYbDrugInfoList.getBkm080()));
			insSzybItem.setBkm090(stringTranDouble(szYbDrugInfoList.getBkm090()));
			insSzybItem.setBkm095(stringTranDouble(szYbDrugInfoList.getBkm095()));
			insSzybItem.setBkm100(szYbDrugInfoList.getBkm100());
			insSzybItem.setBkm101(szYbDrugInfoList.getBkm101());
			insSzybItem.setBkm102(szYbDrugInfoList.getBkm102());
			insSzybItem.setBla050(stringTranInt(szYbDrugInfoList.getBla050()));
			insSzybItem.setBla052(szYbDrugInfoList.getBla052());
			insSzybItem.setBla053(stringTranInt(szYbDrugInfoList.getBla053()));
			insSzybItem.setBla054(stringTranInt(szYbDrugInfoList.getBla054()));
			insSzybItem.setBma030(stringTranInt(szYbDrugInfoList.getBma030()));
			insSzybItem.setBma031(stringTranInt(szYbDrugInfoList.getBma031()));
			insSzybItem.setCke900(szYbDrugInfoList.getCke900());
			insSzybItem.setCke901(szYbDrugInfoList.getCke901());
			insSzybItem.setCke902(szYbDrugInfoList.getCke902());
			insSzybItem.setCke903(szYbDrugInfoList.getCke903());
			insSzybItem.setCke904(szYbDrugInfoList.getCke904());
			insSzybItem.setCkm097(szYbDrugInfoList.getCkm097());
			insSzybItem.setCkm098(szYbDrugInfoList.getCkm098());
			insSzybItem.setCkm099(szYbDrugInfoList.getCkm099());
			insSzybItem.setCkm102(szYbDrugInfoList.getCkm102());
			insSzybItem.setCkm104(szYbDrugInfoList.getCkm104());
			insSzybItem.setCkm105(szYbDrugInfoList.getCkm105());
			insSzybItem.setCkm106(szYbDrugInfoList.getCkm106());
			insSzybItem.setCkm107(szYbDrugInfoList.getCkm107());
			insSzybItem.setDelFlag("0");
			insSzybItem.setCreator(user.getUserName());
			insSzybItem.setTs(new Date());

			insSzybItemList.add(insSzybItem);
		}

		if(insSzybItemList!=null && insSzybItemList.size()>0){
			DataBaseHelper.batchUpdate("update ins_szyb_item set del_flag = '1' where eu_hpdicttype = '01' and ake003 ='1' and ake001 = :ake001 ", insSzybItemList);//??????????????????????????????????????????????????????????????????
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybItem.class), insSzybItemList);
		}

	}

	/**
	 * ????????????015001011088
	 * ??????????????????
	 * ??????ins_szyb_dise???
	 * @param param
	 * @param user
	 */
	public void saveYBDiseInfos(String param,IUser user){
		SzYbDiseInfo szYbDiseInfo = JsonUtil.readValue(param, SzYbDiseInfo.class);
		if(szYbDiseInfo!=null &&
				szYbDiseInfo.getOutputlist()!=null && szYbDiseInfo.getOutputlist().size()>0){
			List<InsSzybDise> diseList = szYbDiseInfo.getOutputlist();

			//?????????????????????????????????
			List<String> codeList = new ArrayList<>();
			for(int i=0; i<diseList.size(); i++){
				codeList.add(diseList.get(i).getAka120());
				diseList.get(i).setAka020(PinyinUtils.getPinYinHeadChar(diseList.get(i).getAka121()).toUpperCase());
				ApplicationUtils.setDefaultValue(diseList.get(i), true);
			}

			List<InsSzybDise> diseDbList = shenzhenCityMapper.qryDiseListByCode(codeList);
			if(diseDbList!=null && diseDbList.size()>0){
				//????????????
				List<InsSzybDise> updateDise = new ArrayList<>();
				//????????????
				List<InsSzybDise> insertDise = new ArrayList<>();

				boolean flag;
				for(InsSzybDise dise : diseList){
					flag = false;
					for(InsSzybDise dbDise : diseDbList){
						//???????????????????????????
						if(dise.getAka120().equals(dbDise.getAka120())){
							updateDise.add(copyDiseInfo(dise,dbDise));
							flag = true;
							break;
						}
					}
					//??????
					if(!flag){

						insertDise.add(dise);
					}
				}


				if(updateDise!=null && updateDise.size()>0){
					DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(InsSzybDise.class), updateDise);
				}

				if(insertDise!=null && insertDise.size()>0){
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybDise.class), insertDise);
				}

			}else{
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybDise.class), diseList);
			}

		}
	}


	private InsSzybDise copyDiseInfo(InsSzybDise newDise,InsSzybDise oldDise){
		newDise.setPkInsdise(oldDise.getPkInsdise());
		newDise.setPkOrg(oldDise.getPkOrg());
		newDise.setCreator(oldDise.getCreator());
		newDise.setCreateTime(oldDise.getCreateTime());
		newDise.setModifier(oldDise.getModifier());
		newDise.setTs(new Date());
		newDise.setDelFlag(oldDise.getDelFlag());

		return newDise;
	}


	/**
	 * 015001011018
	 *
	 *	??????????????????????????????????????????????????????       ??????????????????????????????????????????
	 *
	 * @param szybDrugInfo
	 * @throws ParseException
	 *
	 */

	public String getLastDownDate(String param, IUser user) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String catalogueClass = paramMap.get("catalogueClass").toString();
		String time = "";
		if("3".equals(catalogueClass)){
			Map<String,Object> timeMap = DataBaseHelper.queryForMap("select time from (select AAE396 as time from ins_szyb_item where  eu_hpdicttype = '01'  and del_flag = '0'  and (ake003 = '3' or ake003 = '5')  ORDER BY AAE396 desc) where rownum =1");
			if(timeMap!= null && !"".equals(timeMap)){
				time = String.valueOf(timeMap.get("time"));
				DataBaseHelper.execute("UPDATE ins_szyb_item SET DEL_FLAG = '1'  where  eu_hpdicttype = '01' and ( ake003 = '3' or ake003 = '5') and eu_hpdicttype = '01' and  AAE396 = ? ", time);
			}else{
				time = "";
			}
		}else if("4".equals(catalogueClass)){//??????????????????
			Map<String,Object> timeMap = DataBaseHelper.queryForMap("select time from (select to_char(create_time,'yyyyMMdd') as time from ins_szyb_dise where del_flag = '0' ORDER BY create_time desc) where rownum =1");
			if(timeMap!=null && timeMap.size()>0){
				time = CommonUtils.getString(timeMap.get("time"));
			}
		}else{
			Map<String,Object> timeMap = DataBaseHelper.queryForMap("select time from (select AAE396 as time from ins_szyb_item where  eu_hpdicttype = '01' and AKE003 = ?  and del_flag = '0'  ORDER BY AAE396 desc) where rownum =1",catalogueClass);
			if(timeMap!= null && !"".equals(timeMap)){
				time = String.valueOf(timeMap.get("time"));
				DataBaseHelper.execute("UPDATE ins_szyb_item SET DEL_FLAG = '1'  where eu_hpdicttype = '01' and  AAE396 = ?  and ake003 = ? ", time ,catalogueClass);
			}else{
				time = "";
			}
		}



		return time;

	}




	/**
	 * 015001011025
	 *
	 * ??????????????????????????????????????????ins_szyb_item??????
	 *
	 * @param szYbDiagnosisInfo
	 *
	 */

	public void saveDiagnosisInfo(String param, IUser user){
		SzYbDiagnosisInfo szYbDiagnosisInfo=JsonUtil.readValue(param, SzYbDiagnosisInfo.class);
		int drugInfoSize = szYbDiagnosisInfo.getOutputlist().size();
		List<InsSzybItem> insSzybItemList = new ArrayList<InsSzybItem>();

		for(int i = 0 ; i < drugInfoSize; i++ ){
			InsSzybItem insSzybItem = new InsSzybItem();
			SzYbDiagnosisInfoList szYbDiagnosisInfoList = szYbDiagnosisInfo.getOutputlist().get(i);
			//DataBaseHelper.update("update ins_szyb_item set del_flag = '1' where eu_hpdicttype = '01' and ake003 ='2' and ake001 =  '"+ szYbDiagnosisInfoList.getAke001()+"'");
			insSzybItem.setPkInsitem(NHISUUID.getKeyId());
			insSzybItem.setEuHpdicttype("01");
			insSzybItem.setAae013(szYbDiagnosisInfoList.getAae013());
			insSzybItem.setAae396(stringTranInt(szYbDiagnosisInfoList.getAae396()));
			insSzybItem.setAka022(szYbDiagnosisInfoList.getAka022());
			insSzybItem.setAka036(szYbDiagnosisInfoList.getAka036());
			insSzybItem.setAka067(szYbDiagnosisInfoList.getAka067());
			insSzybItem.setAka111(szYbDiagnosisInfoList.getAka111());
			insSzybItem.setAkc222(szYbDiagnosisInfoList.getAkc222());
			insSzybItem.setAke001(szYbDiagnosisInfoList.getAke001());
			insSzybItem.setAke002(szYbDiagnosisInfoList.getAke002());
			insSzybItem.setAke003(szYbDiagnosisInfoList.getAke003());
			insSzybItem.setAma011(szYbDiagnosisInfoList.getAma011());
			insSzybItem.setAla011(szYbDiagnosisInfoList.getAla011());
			insSzybItem.setBka640(szYbDiagnosisInfoList.getBka640());
			insSzybItem.setBka956(szYbDiagnosisInfoList.getBka956());
			insSzybItem.setBke111(szYbDiagnosisInfoList.getBke111());
			insSzybItem.setBkm010(szYbDiagnosisInfoList.getBkm010());
			insSzybItem.setBkm011(szYbDiagnosisInfoList.getBkm011());
			insSzybItem.setBkm014(stringTranDouble(szYbDiagnosisInfoList.getBkm014()));
			insSzybItem.setBkm015(stringTranDouble(szYbDiagnosisInfoList.getBkm015()));
			insSzybItem.setBkm016(stringTranDouble(szYbDiagnosisInfoList.getBkm016()));
			insSzybItem.setBkm031(szYbDiagnosisInfoList.getBkm031());
			insSzybItem.setBkm032(szYbDiagnosisInfoList.getBkm032());
			insSzybItem.setBkm033(stringTranInt(szYbDiagnosisInfoList.getBkm033()));
			insSzybItem.setBkm034(stringTranInt(szYbDiagnosisInfoList.getBkm034()));
			insSzybItem.setBkm035(szYbDiagnosisInfoList.getBkm035());
			insSzybItem.setBkm036(stringTranInt(szYbDiagnosisInfoList.getBkm036()));
			insSzybItem.setBkm037(stringTranInt(szYbDiagnosisInfoList.getBkm037()));
			insSzybItem.setBkm038(szYbDiagnosisInfoList.getBkm038());
			insSzybItem.setBkm039(stringTranInt(szYbDiagnosisInfoList.getBkm039()));
			insSzybItem.setBkm040(stringTranInt(szYbDiagnosisInfoList.getBkm040()));
			insSzybItem.setBkm044(szYbDiagnosisInfoList.getBkm044());
			insSzybItem.setBkm045(stringTranInt(szYbDiagnosisInfoList.getBkm045()));
			insSzybItem.setBkm046(stringTranInt(szYbDiagnosisInfoList.getBkm046()));
			insSzybItem.setBkm047(szYbDiagnosisInfoList.getBkm047());
			insSzybItem.setBkm048(stringTranInt(szYbDiagnosisInfoList.getBkm048()));
			insSzybItem.setBkm049(stringTranInt(szYbDiagnosisInfoList.getBkm049()));
			insSzybItem.setBkm055(stringTranDouble(szYbDiagnosisInfoList.getBkm055()));
			insSzybItem.setBkm056(stringTranDouble(szYbDiagnosisInfoList.getBkm056()));
			insSzybItem.setBkm057(stringTranDouble(szYbDiagnosisInfoList.getBkm057()));
			insSzybItem.setBkm058(stringTranDouble(szYbDiagnosisInfoList.getBkm058()));
			insSzybItem.setBkm059(stringTranDouble(szYbDiagnosisInfoList.getBkm059()));
			insSzybItem.setBkm091(stringTranDouble(szYbDiagnosisInfoList.getBkm091()));
			insSzybItem.setBkm092(stringTranDouble(szYbDiagnosisInfoList.getBkm092()));
			insSzybItem.setBkm093(stringTranDouble(szYbDiagnosisInfoList.getBkm093()));
			insSzybItem.setBkm094(stringTranDouble(szYbDiagnosisInfoList.getBkm094()));
			insSzybItem.setBkm096(stringTranDouble(szYbDiagnosisInfoList.getBkm096()));
			insSzybItem.setBkm097(stringTranDouble(szYbDiagnosisInfoList.getBkm097()));
			insSzybItem.setBkm098(stringTranDouble(szYbDiagnosisInfoList.getBkm098()));
			insSzybItem.setBkm099(stringTranDouble(szYbDiagnosisInfoList.getBkm099()));
			insSzybItem.setBkm100(szYbDiagnosisInfoList.getBkm100());
			insSzybItem.setBkm101(szYbDiagnosisInfoList.getBkm101());
			insSzybItem.setBkm102(szYbDiagnosisInfoList.getBkm102());
			insSzybItem.setBla050(stringTranInt(szYbDiagnosisInfoList.getBla050()));
			insSzybItem.setBla051(stringTranInt(szYbDiagnosisInfoList.getBla051()));
			insSzybItem.setBla052(szYbDiagnosisInfoList.getBla052());
			insSzybItem.setBla053(stringTranInt(szYbDiagnosisInfoList.getBla053()));
			insSzybItem.setBla054(stringTranInt(szYbDiagnosisInfoList.getBla054()));
			insSzybItem.setBma030(stringTranInt(szYbDiagnosisInfoList.getBma030()));
			insSzybItem.setBma031(stringTranInt(szYbDiagnosisInfoList.getBma031()));
			insSzybItem.setCke900(szYbDiagnosisInfoList.getCke900());
			insSzybItem.setCke901(szYbDiagnosisInfoList.getCke901());
			insSzybItem.setCke902(szYbDiagnosisInfoList.getCke902());
			insSzybItem.setCke903(szYbDiagnosisInfoList.getCke903());
			insSzybItem.setCke904(szYbDiagnosisInfoList.getCke904());
			insSzybItem.setCkm099(szYbDiagnosisInfoList.getCkm099());
			insSzybItem.setCkm102(szYbDiagnosisInfoList.getCkm102());
			insSzybItem.setCkm107(szYbDiagnosisInfoList.getCkm107());
			insSzybItem.setCkm108(szYbDiagnosisInfoList.getCkm108());
			insSzybItem.setCkm109(szYbDiagnosisInfoList.getCkm109());
			insSzybItem.setTs(new Date());
			insSzybItem.setDelFlag("0");
			insSzybItem.setCreator(user.getUserName());
			//DataBaseHelper.insertBean(insSzybItem);

			insSzybItemList.add(insSzybItem);
		}

		if(insSzybItemList!=null && insSzybItemList.size()>0){
			DataBaseHelper.batchUpdate("update ins_szyb_item set del_flag = '1' where eu_hpdicttype = '01' and ake003 ='2' and ake001 = :ake001 ", insSzybItemList);//??????????????????????????????????????????????????????????????????
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybItem.class), insSzybItemList);
		}

	}



	/**
	 * 015001011026
	 *
	 * ??????????????????????????????????????????ins_szyb_item??????
	 *
	 * @param szYbDiagnosisInfo
	 *
	 */
	public void saveMaterialsInfo(String param, IUser user){
		SzYbMaterialsInfo szYbMaterialsInfo=JsonUtil.readValue(param, SzYbMaterialsInfo.class);

		List<InsSzybItem> insSzybItemList = new ArrayList<InsSzybItem>();
		int drugInfoSize = szYbMaterialsInfo.getOutputlist().size();
		for(int i = 0 ; i < drugInfoSize; i++ ){
			InsSzybItem insSzybItem = new InsSzybItem();
			SzYbMaterialsInfoList szYbMaterialsInfoList = szYbMaterialsInfo.getOutputlist().get(i);
			//DataBaseHelper.update("update ins_szyb_item set del_flag = '1' where eu_hpdicttype = '01' and  (ake003 ='3' or ake003 = '5') and ake001 =  '"+ szYbMaterialsInfoList.getAke001()+"'");
			insSzybItem.setPkInsitem(NHISUUID.getKeyId());
			insSzybItem.setEuHpdicttype("01");
			insSzybItem.setAkc222(szYbMaterialsInfoList.getAkc222());
			insSzybItem.setAae013(szYbMaterialsInfoList.getAae013());
			insSzybItem.setAae396(stringTranInt(szYbMaterialsInfoList.getAae396()));
			insSzybItem.setAka022(szYbMaterialsInfoList.getAka022());
			insSzybItem.setAka036(szYbMaterialsInfoList.getAka036());
			insSzybItem.setAka068(stringTranDouble(szYbMaterialsInfoList.getAka068()));
			insSzybItem.setAka111(szYbMaterialsInfoList.getAka111());
			insSzybItem.setAke001(szYbMaterialsInfoList.getAke001());
			insSzybItem.setAke002(szYbMaterialsInfoList.getAke002());
			insSzybItem.setAke003(szYbMaterialsInfoList.getAke003());
			insSzybItem.setAke004(szYbMaterialsInfoList.getAke004());
			insSzybItem.setAla011(szYbMaterialsInfoList.getAla011());
			insSzybItem.setAla026(szYbMaterialsInfoList.getAla026());
			insSzybItem.setAma011(szYbMaterialsInfoList.getAma011());
			insSzybItem.setBka053(szYbMaterialsInfoList.getBka053());
			insSzybItem.setBke111(szYbMaterialsInfoList.getBke111());
			insSzybItem.setBkm032(szYbMaterialsInfoList.getBkm032());
			insSzybItem.setBkm033(stringTranInt(szYbMaterialsInfoList.getBkm033()));
			insSzybItem.setBkm034(stringTranInt(szYbMaterialsInfoList.getBkm034()));
			insSzybItem.setBkm035(szYbMaterialsInfoList.getBkm035());
			insSzybItem.setBkm036(stringTranInt(szYbMaterialsInfoList.getBkm036()));
			insSzybItem.setBkm037(stringTranInt(szYbMaterialsInfoList.getBkm037()));
			insSzybItem.setBkm038(szYbMaterialsInfoList.getBkm038());
			insSzybItem.setBkm039(stringTranInt(szYbMaterialsInfoList.getBkm039()));
			insSzybItem.setBkm040(stringTranInt(szYbMaterialsInfoList.getBkm040()));
			insSzybItem.setBkm044(szYbMaterialsInfoList.getBkm044());
			insSzybItem.setBkm045(stringTranInt(szYbMaterialsInfoList.getBkm045()));
			insSzybItem.setBkm046(stringTranInt(szYbMaterialsInfoList.getBkm046()));
			insSzybItem.setBkm047(szYbMaterialsInfoList.getBkm047());
			insSzybItem.setBkm048(stringTranInt(szYbMaterialsInfoList.getBkm048()));
			insSzybItem.setBkm049(stringTranInt(szYbMaterialsInfoList.getBkm049()));
			insSzybItem.setBkm090(stringTranDouble(szYbMaterialsInfoList.getBkm090()));
			insSzybItem.setBkm095(stringTranDouble(szYbMaterialsInfoList.getBkm095()));
			insSzybItem.setBkm100(szYbMaterialsInfoList.getBkm100());
			insSzybItem.setBkm101(szYbMaterialsInfoList.getBkm101());
			insSzybItem.setBkm102(szYbMaterialsInfoList.getBkm102());
			insSzybItem.setBla050(stringTranInt(szYbMaterialsInfoList.getBla050()));
			insSzybItem.setBla051(stringTranInt(szYbMaterialsInfoList.getBla051()));
			insSzybItem.setBla052(szYbMaterialsInfoList.getBla052());
			insSzybItem.setBla053(stringTranInt(szYbMaterialsInfoList.getBla053()));
			insSzybItem.setBla054(stringTranInt(szYbMaterialsInfoList.getBla054()));
			insSzybItem.setBla055(szYbMaterialsInfoList.getBla055());
			insSzybItem.setBla056(stringTranInt(szYbMaterialsInfoList.getBla056()));
			insSzybItem.setBla057(stringTranInt(szYbMaterialsInfoList.getBla057()));
			insSzybItem.setBma030(stringTranInt(szYbMaterialsInfoList.getBma030()));
			insSzybItem.setBma031(stringTranInt(szYbMaterialsInfoList.getBma031()));
			insSzybItem.setCke900(szYbMaterialsInfoList.getCke900());
			insSzybItem.setCke901(szYbMaterialsInfoList.getCke901());
			insSzybItem.setCke902(szYbMaterialsInfoList.getCke902());
			insSzybItem.setCke903(szYbMaterialsInfoList.getCke903());
			insSzybItem.setCke904(szYbMaterialsInfoList.getCke904());
			insSzybItem.setCkm099(szYbMaterialsInfoList.getCkm099());
			insSzybItem.setCkm102(szYbMaterialsInfoList.getCkm102());
			insSzybItem.setCkm107(szYbMaterialsInfoList.getCkm107());

			insSzybItem.setCreator(user.getUserName());
			insSzybItem.setDelFlag("0");
			insSzybItem.setTs(new Date());
			//DataBaseHelper.insertBean(insSzybItem);
			insSzybItemList.add(insSzybItem);
		}

		if(insSzybItemList!=null && insSzybItemList.size()>0){
			DataBaseHelper.batchUpdate("update ins_szyb_item set del_flag = '1' where eu_hpdicttype = '01' and  (ake003 ='3' or ake003 = '5') and ake001 =  :ake001 ", insSzybItemList);//??????????????????????????????????????????????????????????????????
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybItem.class), insSzybItemList);
		}

	}







	/**
	 * 015001011027
	 * ???????????????????????????????????????????????? --????????????
	 * @param para
	 * @return
	 */
	public  BdPdAndCount DrugPageQueryHospital(String param, IUser user){


		BdPdAndCount bdpdAndCount = new BdPdAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("????????????????????????");
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		//????????????
		List<BdPd> listBdPd = shenzhenCityMapper.qryBdPdInfo(qryparam);
		Page<List<BdPd>> page = MyBatisPage.getPage();
		bdpdAndCount.setBdPdInfoList(listBdPd);
		bdpdAndCount.setTotalCount(page.getTotalCount());
		return bdpdAndCount;
	}


	/**
	 * 015001011028
	 * ???????????????????????????????????????????????? -- ????????????
	 * @param para
	 * @return
	 */
	public  InsSzybItemAndCount DrugPageQueryCentre(String param, IUser user){
		InsSzybItemAndCount InsSzybItemAndConut = new InsSzybItemAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("????????????????????????");
		if("02".equals(qryparam.getEunmMedicalInsurance())){
			qryparam.setProjectCatalogue("");
		}
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		//????????????
		List<InsSzybItem> listInsSzybItem = shenzhenCityMapper.qryDrugInsSzybItemInfo(qryparam);
		Page<List<InsSzybItem>> page = MyBatisPage.getPage();
		InsSzybItemAndConut.setInsSzybItem(listInsSzybItem);
		InsSzybItemAndConut.setTotalCount(page.getTotalCount());
		return InsSzybItemAndConut;
	}

	/**
	 * 015001011035
	 * ????????????????????????????????????????????????--????????????
	 * @param para
	 * @return
	 */
	public  SzYbDrugContrastInfoAndCount  queryDrugSzybItemMap(String param, IUser user){

		SzYbDrugContrastInfoAndCount szYbDrugContrastInfoAndCount = new SzYbDrugContrastInfoAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("????????????????????????");
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String,Object>> SzYbDrugContrastList = shenzhenCityMapper.qryDrugMapInfo(qryparam);
		Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
		szYbDrugContrastInfoAndCount.setSzYbDrugContrastInfo(SzYbDrugContrastList);
		szYbDrugContrastInfoAndCount.setTotalCount(page.getTotalCount());
		return szYbDrugContrastInfoAndCount;
	}

	/**
	 * 015001011029
	 * ???????????????????????????????????????????????????--????????????
	 * @param para
	 * @return
	 */
	public  BdItemAndCount DiagnosisPageQueryHospital(String param, IUser user){
		BdItemAndCount bdItemAndCount = new BdItemAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("????????????????????????");
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		String proCata = qryparam.getProjectCatalogue();
		if("2".equals(proCata)){
			qryparam.setProjectCatalogue("01");
		}else if("3".equals(proCata)){
			qryparam.setProjectCatalogue("07");
		}
		//????????????
		List<Map<String,Object>> bdItemInfoList = shenzhenCityMapper.qryBdItemInfo(qryparam);
		Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
		bdItemAndCount.setBdItemInfoList(bdItemInfoList);
		bdItemAndCount.setTotalCount(page.getTotalCount());
		return bdItemAndCount;
	}


	/**
	 * 015001011030
	 * ??????????????????????????????????????????????????? -- ????????????
	 * @param para
	 * @return
	 */
	public  InsSzybItemAndCount DiagnosisQueryCentre(String param, IUser user){
		InsSzybItemAndCount InsSzybItemAndConut = new InsSzybItemAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("????????????????????????");
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());

		MyBatisPage.startPage(pageIndex, pageSize);
		//????????????
		List<InsSzybItem> listInsSzybItem = shenzhenCityMapper.qryNoDrugInsSzybItemInfo(qryparam);
		Page<List<InsSzybItem>> page = MyBatisPage.getPage();
		InsSzybItemAndConut.setInsSzybItem(listInsSzybItem);
		InsSzybItemAndConut.setTotalCount(page.getTotalCount());
		return InsSzybItemAndConut;
	}

	/**
	 * 015001011032
	 * ???????????????????????????????????????????????????--????????????
	 * @param para
	 * @return
	 */
	public  SzYbDiagnosisContrastInfoAndCount  queryNoDrugSzybItemMap(String param, IUser user){

		SzYbDiagnosisContrastInfoAndCount szYbDiagnosisContrastInfoAndCount = new SzYbDiagnosisContrastInfoAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("????????????????????????");
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String,Object>> SzYbDiagnosisContrastList = shenzhenCityMapper.qryNoDrugMapInfo(qryparam);
		Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
		szYbDiagnosisContrastInfoAndCount.setSzYbDiagnosisContrastInfo(SzYbDiagnosisContrastList);
		szYbDiagnosisContrastInfoAndCount.setTotalCount(page.getTotalCount());
		return szYbDiagnosisContrastInfoAndCount;
	}



		/**
		 * 015001011086
		 * ??????????????????
		 * @param param
		 * @param user
		 * @return
		 */
		public 	Map<String,Object> upSzybItemMemo(String param, IUser user){
			Map<String,Object> ret = new HashMap<>();//????????????????????????????????????????????????
			Map<String, Object> qryparam = JsonUtil.readValue(param,Map.class);
			InsSzybItem listInsSzybItem = shenzhenCityMapper.qrySzybItem(qryparam);
			String ake003 =listInsSzybItem.getAke003();
			String euHpdicttype =listInsSzybItem.getEuHpdicttype();
			BdPd bdPd = null;
			BdItem bdItem = null;
			if("1".equals(ake003)){
				bdPd = shenzhenCityMapper.qryBdPd(qryparam);
				if(bdPd==null){
					throw new BusException("?????????????????????"+qryparam.get("hosCode")+"????????????,??????????????????");
				}
			}else {
				bdItem = shenzhenCityMapper.qryBdItem(qryparam);
				if(bdItem==null){
					throw new BusException("?????????????????????"+qryparam.get("hosCode")+"????????????,???????????????");
				}
			}
			if("01".equals(euHpdicttype)){
				memoEntity(listInsSzybItem,bdPd,bdItem,ret);
				ret.put("memo", "memo");
			}else{
				ret.put("memo", "0");
			}
			return ret;
		}



	/**
	 * 015001011031
	 * ????????????????????????????????????????????????????????????
	 * @param para
	 * @return
	 */
	public Map<String,Object> querySaveSzybItemMap(String param, IUser user){
		Map<String,Object> ret = new HashMap<>();
		Map<String, Object> qryparam = JsonUtil.readValue(param,Map.class);
		int retInsSzybItemMap = 0;
		int retBdItemHp = 0;
		BdPd bdPd = null;
		BdItem bdItem = null;
		if (qryparam == null )
			throw new BusException("????????????????????????");

		String proClass = (String)qryparam.get("proClass");
		InsSzybItem listInsSzybItem = shenzhenCityMapper.qrySzybItem(qryparam);
		String ake003 =listInsSzybItem.getAke003();
		if("1".equals(ake003)){
			bdPd = shenzhenCityMapper.qryBdPd(qryparam);
			if(bdPd==null){
				throw new BusException("?????????????????????"+qryparam.get("hosCode")+"????????????,??????????????????");
			}
		}else {
			bdItem = shenzhenCityMapper.qryBdItem(qryparam);
			if(bdItem==null){
				throw new BusException("?????????????????????"+qryparam.get("hosCode")+"????????????,???????????????");
			}
		}

		InsSzybItemMap insSzybItemMap = new InsSzybItemMap();
		insSzybItemMap.setPkinsitem(listInsSzybItem.getPkInsitem());
		insSzybItemMap.setEuhpdicttype(listInsSzybItem.getEuHpdicttype());
		insSzybItemMap.setFlagState((String)qryparam.get("flagState"));
		insSzybItemMap.setAke001(listInsSzybItem.getAke001());
		insSzybItemMap.setAke002(listInsSzybItem.getAke002());
		insSzybItemMap.setAke003(listInsSzybItem.getAke003());
		insSzybItemMap.setAka031(listInsSzybItem.getAka031());
		insSzybItemMap.setBkm031(listInsSzybItem.getBkm031());
		insSzybItemMap.setBkm032(listInsSzybItem.getBkm032());
		insSzybItemMap.setAka036(listInsSzybItem.getAka036());
		insSzybItemMap.setCkm099(listInsSzybItem.getCkm099());
		if(bdPd!=null){
			DataBaseHelper.execute("update ins_szyb_itemmap set del_flag='1' where pk_item=? and EU_HPDICTTYPE = ? and ake001=?",bdPd.getPkPd(),insSzybItemMap.getEuhpdicttype(),listInsSzybItem.getAke001());
			insSzybItemMap.setCodehosp(bdPd.getCode());
			insSzybItemMap.setPkitem(bdPd.getPkPd());
		}else if(bdItem!=null){
			DataBaseHelper.execute("update ins_szyb_itemmap set del_flag='1' where pk_item=? and EU_HPDICTTYPE = ? and ake001=?",bdItem.getPkItem(),insSzybItemMap.getEuhpdicttype(),listInsSzybItem.getAke001());
			insSzybItemMap.setCodehosp(bdItem.getCode());
			insSzybItemMap.setPkitem(bdItem.getPkItem());
		}
		insSzybItemMap.setFlagaudit("0");
		insSzybItemMap.setCreator(user.getUserName());
		insSzybItemMap.setCreatetime(new Date());
		insSzybItemMap.setDelflag("0");
		insSzybItemMap.setTs(new Date());
		insSzybItemMap.setDateBegin(new Date());
		try {
			insSzybItemMap.setDateEnd(DateUtils.parseDate(DateUtils.addDate(new Date(), 2, 1, "yyyy-MM-dd HH:mm:ss")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		retInsSzybItemMap = DataBaseHelper.insertBean(insSzybItemMap);
		ret.put("retInsSzybItemMap",retInsSzybItemMap);

		BdItemHp bdItemHp = new BdItemHp();
		bdItemHp.setEuItemType(proClass);
		if(bdPd!=null){
			bdItemHp.setPkItem(bdPd.getPkItem());
		}else if(bdItem!=null){
			bdItemHp.setPkItem(bdItem.getPkItem());
		}
		bdItemHp.setPkHp(listInsSzybItem.getPkInsitem());//??????????????????
		bdItemHp.setDtHpdicttype(listInsSzybItem.getAke003());//????????????
		bdItemHp.setCodeHp(listInsSzybItem.getAke001());//????????????
		bdItemHp.setEuLevel(listInsSzybItem.getBkm032());//????????????   --- ???????????????????????????
		bdItemHp.setRatioSelf("");//????????????
		bdItemHp.setNote("");
		bdItemHp.setCreator(user.getUserName());
		bdItemHp.setCreateTime(new Date());
		bdItemHp.setDelFlag("0");
		bdItemHp.setTs(new Date());
		bdItemHp.setHpTime(new Date());
		bdItemHp.setNameHp(listInsSzybItem.getAke002());//????????????
		retBdItemHp = DataBaseHelper.insertBean(bdItemHp);
		ret.put("retBdItemHp",retBdItemHp);
		return ret ;

	}


	/**
	 * ??????????????????ML009 ???ML008???ML007??????????????????                   ???????????????????????????  ?????????
	 * @return
	 * @throws ParseException
	 */
	private Map<String, Object> memoEntity(InsSzybItem listInsSzybItem,BdPd bdPd,BdItem bdItem,Map<String, Object> ret) {
		String ake003 =listInsSzybItem.getAke003();
		if("1".equals(ake003)){//??????
		ret.put("ake001", listInsSzybItem.getAke001());//??????????????????
			ret.put("bkm017",listInsSzybItem.getBkm017());//???????????????
			ret.put("aka070",listInsSzybItem.getAka070());//????????
			ret.put("aka064",listInsSzybItem.getAka064());//???????????????
			ret.put("aae013",listInsSzybItem.getAae013());//????????????
			ret.put("ake005",bdPd.getCode());//	??????????????????????????????
			ret.put("ake006",bdPd.getName());//	??????????????????????????????
			ret.put("aka074",bdPd.getVol()+bdPd.getPkUnitVol());//	??????
			Map<String, Object>  UnitMap = DataBaseHelper.queryForMap("Select name From bd_unit Where pk_unit = ? ",bdPd.getPkUnitMin());
			if(UnitMap!=null){
				ret.put("aka067",UnitMap.get("name"));//	????????????
			}else{
				ret.put("aka067","???");
			}
			Map<String, Object>  factoryMap = DataBaseHelper.queryForMap("Select name From bd_factory Where pk_factory = ?",bdPd.getPkFactory());
			if(factoryMap!=null){
				ret.put("bka053",factoryMap.get("name"));//	??????
			}else{
				ret.put("bka053","???");
			}

//			//????????????????????????
//			String sql = "select count(1) from bd_Pd pd " +
//					"inner join bd_pd_att att on pd.pk_pd=att.pk_pd " +
//					"inner join bd_pd_att_define def on att.pk_pdattdef=def.pk_pdattdef " +
//					"where def.code_att='0510' " +
//					"and att.val_att='1' " +
//					"and pd.pk_pd = ? ";
//			Integer cnt = DataBaseHelper.queryForScalar(sql,Integer.class,new Object[]{bdPd.getPkPd()});
//			if(cnt!=null && cnt>=1){
			bdPd.setPrice(MathUtils.div(bdPd.getPrice(),bdPd.getPackSize().doubleValue()));

//			}

			ret.put("bka505",bdPd.getPrice());//	??????????????????
			ret.put("bka506",bdPd.getPrice());//	??????????????????
			ret.put("aae030",new SimpleDateFormat("yyyyMMdd").format(new Date()));//	????????????

		}else if ("2".equals(ake003)) {//??????
			ret.put("ake001", listInsSzybItem.getAke001());//??????????????????
			ret.put("ake005", bdItem.getCode());//??????????????????????????????
			ret.put("ake006", bdItem.getName());//??????????????????????????????
			ret.put("bka506", bdItem.getPrice());//????????????????????????????????????
			ret.put("bkf131", "1");//????????????????????????
			ret.put("bkm062", "0");//????????????????????????
			ret.put("aae030",new SimpleDateFormat("yyyyMMdd").format(new Date()));//	????????????

		}else if ("3".equals(ake003)||"5".equals(ake003)) {//??????
			ret.put("ake001",listInsSzybItem.getAke001());//	??????????????????
			ret.put("ake005",bdItem.getCode());//	??????????????????????????????
			ret.put("ake006",bdItem.getName());//	??????????????????????????????
			Map<String, Object>  factoryMap = DataBaseHelper.queryForMap("Select name From bd_factory Where pk_factory = ?",bdItem.getPkFactory());
			if(factoryMap!=null){
				ret.put("bka053",factoryMap.get("name"));//	????????????
			}else{
				ret.put("bka053","???");//	????????????
			}
			String ake004 = listInsSzybItem.getAke004();
			if("".equals(ake004)){
				ake004 = "1";
			}
			ret.put("ake004",ake004);//	???????????????
			ret.put("ckf261","0");	//????????????????????????
			Map<String, Object>  DefDocMap = DataBaseHelper.queryForMap("Select name From bd_unit Where pk_unit = ? ",bdItem.getPkUnit());
			if(DefDocMap!=null){
				ret.put("aka067",DefDocMap.get("name"));//	????????????
			}else{
				ret.put("aka067","???");//	????????????
			}
			ret.put("aka074",bdItem.getSpec());//	??????
			ret.put("bka505",bdItem.getPrice());//	????????????
			ret.put("bka506",bdItem.getPrice());//	????????????
			ret.put("aae013",bdItem.getNote());//	??????

		}
		ret.put("ake003", ake003);

		return ret;
	}

	/**
	 * ????????????015001011068
	 * ????????????????????????????????????????????????????????????
	 * @param codeHosp
	 */
	public  void updateInsSzybMapInfo(String param, IUser user){
		Map<String, Object> qryparam = JsonUtil.readValue(param,Map.class);
    	DataBaseHelper.update("update ins_szyb_itemmap set eu_apply ='0' ,date_apply=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss')  where code_hosp = ? and del_flag = '0' ",new Object[] { qryparam.get("status") });
	}

	/**
	 * ????????????015001011069
	 * ????????????????????????????????????????????? ????????????????????? ??????????????????????????????
	 * @param codeHosp
	 */
	public  int updateMapStatus(String param, IUser user){
		int stu = 0;
		Map<String, Object> qryparam = JsonUtil.readValue(param,Map.class);
    	stu = DataBaseHelper.update("update ins_szyb_itemmap set eu_apply =? ,date_audit=to_date(?,'yyyy/mm/dd'),flag_audit =?  where code_hosp = ? and del_flag = '0' ",new Object[] { qryparam.get("applyStuts"),qryparam.get("memoDate"),qryparam.get("fishStuts"),qryparam.get("code") });
    	return stu ;
	}



	/**
	 * ????????????015001011070
	 *
	 */
	public void delSzybMapInfo(String param, IUser user){
		Map<String, Object> qryparam = JsonUtil.readValue(param,Map.class);
		if(qryparam!=null && qryparam.size()>0){
			if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(qryparam,"ake001"))){
				DataBaseHelper.update("update ins_szyb_itemmap set del_flag = '1' where code_hosp =? and ake003 =? and eu_hpdicttype =? and ake001 = ?",new Object[] { qryparam.get("ake005"), qryparam.get("ake003"),qryparam.get("euHpdicttype"),qryparam.get("ake001") });
			}else{
				DataBaseHelper.update("update ins_szyb_itemmap set del_flag = '1' where code_hosp =? and ake003 =? and eu_hpdicttype =? ",new Object[] { qryparam.get("ake005"), qryparam.get("ake003"),qryparam.get("euHpdicttype") });
			}
		}

	}


	/**
	 * ?????????  ?????? int ??????
	 * @param para
	 * @return
	 */
	public static int stringTranInt(String para){
		int str = 0;
		if(!"".equals(para) && null!=para){
			str = Integer.parseInt(para);
		}
		return str;
	}




	/**
	 * ?????????  ?????? Double ?????? ????????????
	 * @param para
	 * @return
	 */
	public static Double stringTranDouble(String para){
		Double str = 0.00;
		if(!"".equals(para) && null!=para){


			BigDecimal temp = BigDecimal.valueOf(Double.valueOf(para));
			// ???temp??????100
			temp = temp.multiply(BigDecimal.valueOf(100));
			temp = BigDecimal.valueOf(Double.valueOf(Math.floor(temp.doubleValue())));
			temp = temp.divide(BigDecimal.valueOf(100));
			str = temp.doubleValue();

		}
		return str;
	}



	/**
	 * 015001011046 ????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryInsDictmap(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		Map<String,Object> dataMap = shenzhenCityMapper.qryInsDictmap(paramMap);
		return dataMap;
	}


	/**
	 * 015001011051 ???????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public void updateVisitCity(String param , IUser user){
		InsSzybVisitCity insSzybVisitCity =JsonUtil.readValue(param, InsSzybVisitCity.class);

		String sql = "update pv_encounter set pk_insu=? Where pk_pv=?";
		DataBaseHelper.update(sql, new Object[] { insSzybVisitCity.getPkInsu(),insSzybVisitCity.getPkPv() });

		sql = "update ins_szyb_visit set persontype=? ,pk_hp=? Where pk_pv=?";
		DataBaseHelper.update(sql, new Object[] { insSzybVisitCity.getPersontype(),insSzybVisitCity.getPkInsu(),insSzybVisitCity.getPkPv() });
		
		if(!CommonUtils.isEmptyString(insSzybVisitCity.getPassword())){
			sql = "update ins_szyb_visit set password=? Where pk_pv=?";
			DataBaseHelper.update(sql, new Object[] { insSzybVisitCity.getPassword(),insSzybVisitCity.getPkPv() });
		}

		//DataBaseHelper.updateBeanByPk(insSzybVisitCity, false);

		sql = "update ins_szyb_visit_city set cme331=:cme331,cka303=:cka303,cka304=:Cka304,cme320=:cme320,amc021=:amc021,alc005=:alc005,aka120=:aka120,akc196=:akc196,aae140=:aae140,bkc378=:bkc378 Where pk_pv=:pkPv ";
		DataBaseHelper.update(sql,insSzybVisitCity);


	}


	/**
	 * 015001011064  ??????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPreSettlePat(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnList= shenzhenCityMapper.queryPreSettlePat(paramMap);
		return rtnList;
	}

	/**
	 * 015001011065  ????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public void dealPreSettle(String param , IUser user){
		User userInfo=(User)user;
		Date d = new Date();
		BlSettleBudget blSettleBudget=JsonUtil.readValue(param, BlSettleBudget.class);
		blSettleBudget.setTs(d);
		blSettleBudget.setDelFlag("0");
		blSettleBudget.setDateBud(d);

		BlSettleBudget oriBudget=DataBaseHelper.queryForBean("select * from bl_settle_budget where pk_pv=?", BlSettleBudget.class,new Object[]{blSettleBudget.getPkPv()});
		if(oriBudget!=null){
			blSettleBudget.setPkBudget(oriBudget.getPkBudget());
			blSettleBudget.setCreator(oriBudget.getCreator());
			blSettleBudget.setCreateTime(oriBudget.getCreateTime());
			blSettleBudget.setModifier(userInfo.getPkEmp());
			blSettleBudget.setModityTime(d);
			DataBaseHelper.updateBeanByPk(blSettleBudget, false);
		}else{
			blSettleBudget.setCreator(userInfo.getPkEmp());
			blSettleBudget.setCreateTime(d);
			DataBaseHelper.insertBean(blSettleBudget);
		}

	}

	/**
	 * 015001011066  ??????????????????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryInsItem(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnList= shenzhenCityMapper.queryInsItem(paramMap);
		return rtnList;
	}

	public List<Map<String,Object>> queryInsEmp(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> maps = DataBaseHelper.queryForList("select CODE_TYPE code_type_ins,CODE_INSUR from INS_SZYB_DICTMAP where CODE_TYPE in (?,?) and PK_HIS in (?,?) and EU_HPDICTTYPE='01'",
				new Object[]{paramMap.get("codeTypeEmp"), paramMap.get("codeTypeDept"),paramMap.get("pkNhisEmp"), paramMap.get("pkNhisDept")});
		return maps;
	}
	/**
	 * 015001011067  ??????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryInsOrd(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnList= shenzhenCityMapper.queryInsOrd(paramMap);
		return rtnList;
	}

	/**
	 * ????????????015001011089
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public DiseAndConut disePageHospitalQuery(String param , IUser user){


		DiseAndConut bdItemAndCount = new DiseAndConut();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("????????????????????????");
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		if(!CommonUtils.isEmptyString(qryparam.getSearchParam())){
			qryparam.setSearchParam(qryparam.getSearchParam().toUpperCase());
		}
		//????????????
		List<Map<String,Object>> bdItemInfoList = shenzhenCityMapper.disePageHospitalQuery(qryparam);
		Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
		bdItemAndCount.setDiseList(bdItemInfoList);
		bdItemAndCount.setTotalCount(page.getTotalCount());
		return bdItemAndCount;
	}

	/**
	 * 015001011081  ??????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> queryPresInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		Map<String,Object> rtnMap= shenzhenCityMapper.queryPresInfo(paramMap);
		return rtnMap;
	}



	/**
	 * 015001011084  ?????????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryFitInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnList= shenzhenCityMapper.queryFitInfo(paramMap);
		return rtnList;
	}

	/**
	 * 015001011085  ?????????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CnOrder> modifyFitInfo(String param , IUser user){
		List<InsSzybOrder> insSzybOrderList = JsonUtil.readValue(param,new TypeReference<List<InsSzybOrder>>() {});
		User loginUser = (User)user ;
		Date dateNow = new Date();
		List<InsSzybOrder> addList=new ArrayList<InsSzybOrder>();
		List<InsSzybOrder> updateList=new ArrayList<InsSzybOrder>();
		List<CnOrder> updateCnordList=new ArrayList<CnOrder>();
		
		Set<String> setPkInsOrds = new HashSet<String>();
		if(insSzybOrderList!=null && insSzybOrderList.size()>0){
			for(InsSzybOrder insSzybOrder:insSzybOrderList){


				if("1".equals(insSzybOrder.getFlagValid()))
				{
					CnOrder ord=new CnOrder();
					ord.setOrdsn(insSzybOrder.getOrdsn());
					ord.setFlagFit(insSzybOrder.getFlagFit());
					ord.setDescFit(insSzybOrder.getDescFit());
					ord.setPkCnord(insSzybOrder.getPkCnord());
					updateCnordList.add(ord);
				}
				insSzybOrder.setDelFlag("0");
				insSzybOrder.setTs(dateNow);
				insSzybOrder.setPkOrg(loginUser.getPkOrg());
				if(StringUtils.isBlank(insSzybOrder.getPkInsord())){
					insSzybOrder.setPkInsord(NHISUUID.getKeyId());
					insSzybOrder.setCreator(loginUser.getPkEmp());
					insSzybOrder.setCreateTime(dateNow);
					addList.add(insSzybOrder);
					setPkInsOrds.add(insSzybOrder.getPkInsord());
				}else{
					insSzybOrder.setModifier(loginUser.getPkEmp());
					insSzybOrder.setModityTime(dateNow);
					updateList.add(insSzybOrder);
					setPkInsOrds.add(insSzybOrder.getPkInsord());
				}
			}
		}

		if(addList.size()>0){
			String sqlStr="delete from ins_szyb_order where ordsn=:ordsn";
			DataBaseHelper.batchUpdate(sqlStr ,addList);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybOrder.class), addList);
		}
		if(updateList.size()>0){
			String sqlStr="update ins_szyb_order set flag_valid=:flagValid,modifier=:modifier,modity_time=:modityTime where pk_insord=:pkInsord";
			DataBaseHelper.batchUpdate(sqlStr ,updateList);
		}
		if(updateCnordList.size()>0){
			String sqlStr="update cn_order set flag_fit=:flagFit,desc_fit=:descFit where ordsn=:ordsn ";
			DataBaseHelper.batchUpdate(sqlStr ,updateCnordList);
		}
		/*
		if (setPkInsOrds.size() > 0) 
		{
			DataBaseHelper.execute("update ins_szyb_order a set a.pk_cnord = (select pk_cnord from cn_order b where a.ordsn = b.ordsn ) where a.pk_cnord is null and (a.pk_insord IN ( " + CommonUtils.convertSetToSqlInPart(setPkInsOrds, "a.pk_insord") + "))",new Object[] { });
		}*/
		if(Application.isSqlServer())
		{
			//sql server
			//????????????????????????????????????????????????
			DataBaseHelper.execute("update ins_szyb_order a set a.pk_cnord = (select pk_cnord from cn_order b where a.ordsn = b.ordsn ) where a.pk_cnord is null and a.create_time >= (getdate() - 1)",new Object[] { });			
		}
		else
		{
			//oracle
			//????????????????????????????????????????????????
			DataBaseHelper.execute("update ins_szyb_order a set a.pk_cnord = (select pk_cnord from cn_order b where a.ordsn = b.ordsn ) where a.pk_cnord is null and a.create_time >= (sysdate - 1)",new Object[] { });		
		}
		return updateCnordList;
	}
	/**
	 * ????????????015001011087
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 */
	public void delSzybItemMapInfo(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null){
			throw new BusException("????????????????????????");
		}
		String sql="update ins_szyb_itemmap set del_flag='1' where PK_ITEMMAP=?";
		DataBaseHelper.execute(sql,map.get("pkItemMap"));
	}

	/**
	 * ????????????015001011090
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 */
	public int qrySettleTimes(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null){
			throw new BusException("????????????????????????");
		}
		Integer cnt = DataBaseHelper.queryForScalar("select count(1) cnt from ins_szyb_st where del_flag='0' and pk_pv= ?", Integer.class, map.get("pkPv").toString());
	    return cnt;
	}
	/**
	 * ?????????015001011092
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPresClassInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnList= shenzhenCityMapper.queryPresClassInfo(paramMap);
		return rtnList;
	}
	/**????????????????????????
	 * ?????????015001011095
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPvHistory(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnList= shenzhenCityMapper.queryPvHistory(paramMap);
		return rtnList;
	}
	
	/**
	 * ????????????015001011096
	 * ??????pk_item??????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryUploadInfoByPkItem(String param , IUser user){
		List<String> pkList = JsonUtil.readValue(
				param,
				new TypeReference<List<String>>() {});
		List<Map<String,Object>> rtnList= shenzhenCityMapper.qryUploadInfoByPkItem(pkList);
		return rtnList;
	}
	
	/**
	 * ????????????015001011097
	 * ????????????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryInsStInfoByPk(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		
		Map<String,Object> retMap = new HashMap<>(16);
		
		if(paramMap!=null && paramMap.size()>0){
			if(paramMap.containsKey("pkInsst") && !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.containsKey("pkInsst")))){
				retMap = DataBaseHelper.queryForMap(
						"select * from ins_szyb_st where pk_insst = ?",
						new Object[]{CommonUtils.getString(paramMap.get("pkInsst"))});
			}
		}
		
		return retMap;
	}
	
	/**
	 * ????????????015001011098
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryOpVisitInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		
		List<Map<String,Object>> list = new ArrayList<>();
		if(paramMap!=null && paramMap.size()>0 
				&& paramMap.containsKey("pkPv") && paramMap.get("pkPv")!=null){
			list = shenzhenCityMapper.qryOpVisitInfo(paramMap);
		}
		
		if(list==null || list.size()<=0){
			throw new BusException("?????????????????????????????????");
		}

		//????????????????????????????????????(?????????),???????????????????????????????????????????????????????????????????????????????????????????????????
		if(list.get(0)!=null && list.get(0).size()>0){
			if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(list.get(0), "codeMedino"))
			&& CommonUtils.getPropValueStr(list.get(0), "codeMedino").length()<=10){
				//?????????????????????????????????????????????????????????
				List<Map<String,Object>> medNoList = DataBaseHelper.queryForList(
						"select DISTINCT code_medino from INS_SZYB_VISIT where pk_pi = ? and length(code_medino)>10 order by length(code_medino) desc",
						new Object[]{CommonUtils.getPropValueStr(list.get(0), "pkPi")}
						);

				if(medNoList!=null && medNoList.size()>0){
					list.get(0).remove("codeMedino");
					list.get(0).put("code_Medino",medNoList.get(0).get("codeMedino"));
				}
			}
		}

		return list.get(0);
	}
	
	/**
	 * 015001011099 ??????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOpStUploadDetail(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnList=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> allList = shenzhenCityMapper.qryOpStUploadDetail(paramMap);
		List<String> zeroAmoutList = shenzhenCityMapper.qryOpDtUpDtlsZeroAmount(paramMap);

		for(Map<String,Object> item : allList){
			Boolean flagZero=false;
			for(String pkitem:zeroAmoutList){
				if(pkitem.equals(item.get("pkItem").toString())){
					flagZero=true;
				}
			}
			if(!flagZero){
				rtnList.add(item);
			}
		}

		return rtnList;
	}
	
	/**
	 * 015001011100  ??????????????????-???????????????????????????????????????bl_op_dt???flag_insu??????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public void upOpdtFlagInsuByPk(String param , IUser user){
		//Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		IntegrateParam integrateParam = JsonUtil.readValue(param,IntegrateParam.class);
		List<InsSzybCitycg> insSzybCitycgList = integrateParam.getInsSzybCitycgList();
		Map<String,Object>  paramMap=integrateParam.getUnListParamMap();
		Date dateNow=new Date();
		User userInfo = (User) user;

		if(paramMap.containsKey("flagInsu") && paramMap.get("flagInsu")!=null
				&& "0".equals(CommonUtils.getString(paramMap.get("flagInsu")))){
			DataBaseHelper.execute(
					"delete from INS_SZYB_CITYCG city where exists (select 1 from bl_op_dt dt where dt.pk_pv = ? and dt.flag_insu='1' and (dt.flag_settle = '0' OR dt.PK_SETTLE IS NULL) and dt.pk_cgop = city.pk_cgip)"
					, CommonUtils.getString(paramMap.get("pkPv")));
		}
		
		shenzhenCityMapper.updateOpdtFlagInsuByPk(paramMap);

		if(paramMap.containsKey("codeInsst") && paramMap.get("codeInsst")!=null)
		{
//			String codeInsst=paramMap.get("codeInsst").toString();
//			String sql = "update ins_szyb_visit set code_insst=? where pk_pv=?";
//			DataBaseHelper.update(sql, new Object[] { codeInsst,paramMap.get("pkPv").toString() });	
		}
		
		//???????????????INS_SZYB_CITYCG
		if(insSzybCitycgList!=null && insSzybCitycgList.size()>0){
			for(InsSzybCitycg insSzybCitycg :insSzybCitycgList){
				insSzybCitycg.setPkInscg(NHISUUID.getKeyId());
				insSzybCitycg.setCreator(userInfo.getPkEmp());
				insSzybCitycg.setTs(dateNow);
				insSzybCitycg.setPkOrg(userInfo.getPkOrg());
				insSzybCitycg.setCreateTime(dateNow);
				insSzybCitycg.setDelFlag("0");
			}

			String sqlStr="delete from INS_SZYB_CITYCG where pk_cgip=:pkCgip";
			DataBaseHelper.batchUpdate(sqlStr ,insSzybCitycgList);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybCitycg.class), insSzybCitycgList);
		}

	}
	
	/**
	 *  ?????????????????????????????????????????????
	 *????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryOpdtBlStatistical(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> presNoList = shenzhenCityMapper.qryOpdtPresNo(paramMap);
		if((presNoList==null) || (presNoList!=null && presNoList.size()!=1)){
			throw new BusException("?????????????????????????????????????????????????????????????????????");
		}

		Map<String,Object> map = shenzhenCityMapper.qryOpdtBlStatistical(paramMap);
		map.put("presNo", presNoList.get(0).get("presno").toString());
		

		return map;
	}
	
	/**
	 * ????????????015001011102
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public String qrySrvDictInfo(String param , IUser user){
		String pkSchSrv = JsonUtil.getFieldValue(param, "pkSchSrv");
		
		String codeSrv = null;
		
		if(!CommonUtils.isEmptyString(pkSchSrv)){
			codeSrv = shenzhenCityMapper.qrySrvDictInfo(pkSchSrv);
		}
		
		return codeSrv;
	}
	
	/**
	 * ????????????015001011103
	 * ??????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOpListByPkSettle(String param , IUser user){
		String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
		
		List<Map<String,Object>> retList = new ArrayList<>();
		
		if(!CommonUtils.isEmptyString(pkSettle)){
			retList = shenzhenCityMapper.qryOpListByPkSettle(pkSettle);
		}
		
		return retList;
	}
	
	/**
	 * ????????????015001011104
	 * ??????pkSettle??????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryStInfoByPkSettle(String param , IUser user){
		String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
		
		Map<String,Object> stInfo = new HashMap<>(16);
		
		if(!CommonUtils.isEmptyString(pkSettle)){
			stInfo = DataBaseHelper.queryForMap("select * from bl_settle where pk_settle = ?", new Object[]{pkSettle});
		}
		
		return stInfo;
	}
	
	/**
	 * ????????????015001011106
	 * ????????????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryBackOpStUploadDetail(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> allList = shenzhenCityMapper.qryBackOpStUploadDetail(paramMap);

		return allList;
	}
	
	/**
	 * ????????????015001011105
	 * ??????pkPv??????????????????????????????
	 * @param param
	 * @param user
	 */
	public void updateInsuByPkPv(String param , IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		
		if(!CommonUtils.isEmptyString(pkPv)){
			List<Map<String,Object>> pkList = DataBaseHelper.queryForList(
					"select pk_hp from bd_hp where EU_HPTYPE = '0' and del_flag = '0'"
					,new Object[]{});
			
			if(pkList!=null && pkList.size()>0){
				Map<String,Object> hpMap = pkList.get(0);
				if(hpMap!=null && hpMap.size()>0 
						&& hpMap.containsKey("pkHp") &&  !CommonUtils.isEmptyString(CommonUtils.getString(hpMap.get("pkHp"))))
				{
					DataBaseHelper.execute("update PV_ENCOUNTER set pk_insu = ? where pk_pv = ?", new Object[]{CommonUtils.getString(hpMap.get("pkHp")),pkPv});
				}
			}
		}
	}
	
	/**
	 * ????????????015001011109
	 * ????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public void upOpVisitInfo(String param, IUser user) {
		VisitInfoMore visitInfoMore= JsonUtil.readValue(param, VisitInfoMore.class);
		InsSzybVisit visitVo = visitInfoMore.getInsSzybVisit();
		InsSzybVisitCityVo insSzybVisitCity=visitInfoMore.getInsSzybVisitCity();
		
		if(visitVo!=null && !CommonUtils.isEmptyString(visitVo.getPkVisit())){
			shenzhenCityMapper.updateOpVisitInfo(visitVo);
			
			//??????????????????????????????
			if(!CommonUtils.isEmptyString(visitVo.getPkHp()) && !CommonUtils.isEmptyString(visitVo.getPkPv())){
				DataBaseHelper.execute("Update pv_encounter Set pk_insu=? Where pk_pv=?", new Object[]{visitVo.getPkHp(),visitVo.getPkPv()});
			}
		}
		
		if(insSzybVisitCity!=null && !CommonUtils.isEmptyString(insSzybVisitCity.getPkVisit())){
			shenzhenCityMapper.updateOpVisitCityInfo(insSzybVisitCity);
		}
	}

	public Map<String,Object> qryQuePvInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> mapList = shenzhenCityMapper.qryQuePvInfo(paramMap);
		return CollectionUtils.isNotEmpty(mapList)?mapList.get(0):null;
	}

	public void saveQuePvInfo(String param , IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,new TypeReference<Map<String,Object>>(){});
		if(MapUtils.isEmpty(paramMap)){
			throw new BusException("???????????????");
		}
		if(StringUtils.isBlank(MapUtils.getString(paramMap,"pkPv"))){
			throw new BusException("???????????????");
		}
		InsSzybVisitCityVo ibc = JsonUtil.readValue(param, InsSzybVisitCityVo.class);
		List<String> svList = Lists.newArrayList();
		if(StringUtils.isNotEmpty(ibc.getCka303())){
			svList.add("cka303=:cka303");
		}
		if(StringUtils.isNotEmpty(ibc.getCka304())){
			svList.add("cka304=:cka304");
		}
		if(ibc.getCme320()!=null && ibc.getCme320().intValue()>0){
			svList.add("cme320=:cme320");
		}
		if(StringUtils.isNotEmpty(ibc.getAmc021())){
			svList.add("amc021=:amc021");
		}
		if(ibc.getCme331()!=null && ibc.getCme331().intValue()>0){
			svList.add("cme331=:cme331");
		}
		if(StringUtils.isNotEmpty(ibc.getCka305())){
			svList.add("cka305=:cka305");
		}
		if(StringUtils.isNotEmpty(ibc.getAlc005())){
			svList.add("alc005=:alc005");
		}
		if(svList.size()>0){
			DataBaseHelper.update("update INS_SZYB_VISIT_CITY set "+ StringUtils.join(svList,",") + " where pk_pv=:pkPv",ibc);
		}
		//??????pv_encounter
		svList.clear();
		List<String> infoList = Lists.newArrayList();
		//??????
		if(MapUtils.getObject(paramMap,"height")!=null){
			svList.add("height=:height");
			infoList.add("height=:height");
		}
		//??????
		if(MapUtils.getObject(paramMap,"weight")!=null){
			svList.add("weight=:weight");
			infoList.add("weight=:weight");
		}
		//??????
		if(MapUtils.getObject(paramMap,"breathe")!=null){
			infoList.add("breathe=:breathe");
		}
		//??????
		if(MapUtils.getObject(paramMap,"pulse")!=null){
			infoList.add("pulse=:pulse");
		}
		//??????
		if(MapUtils.getObject(paramMap,"temperature")!=null){
			infoList.add("temperature=:temperature");
		}
		//?????????/?????????
		if(MapUtils.getObject(paramMap,"sbp")!=null){
			infoList.add("sbp=:sbp");
		}
		if(MapUtils.getObject(paramMap,"dbp")!=null){
			infoList.add("dbp=:dbp");
		}
		//??????
		if(MapUtils.getObject(paramMap,"heartRate")!=null){
			infoList.add("HEART_RATE=:heartRate");
		}
		if(svList.size()>0){
			DataBaseHelper.update("update pv_encounter set "+ StringUtils.join(svList,",") + " where pk_pv=:pkPv",paramMap);
		}
		CnOpEmrRecord cnOpEmrRecord = JsonUtil.readValue(param, CnOpEmrRecord.class);
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		int cnemropCount = DataBaseHelper.queryForScalar("select count(1)  from cn_emr_op where PK_PV='"+pkPv+"'", Integer.class, new Object[]{});

		if(cnemropCount==0){
			DataBaseHelper.insertBean(cnOpEmrRecord);
		}else
		{
			if(infoList.size()>0){
				DataBaseHelper.update("update cn_emr_op set "+ StringUtils.join(infoList,",") + " where pk_pv=:pkPv",paramMap);
			}
		}
		/*
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkPv", MapUtils.getString(paramMap, "pkPv"));
		//PlatFormSendUtils.sendPatientSiMsg(map);//??????????????????ZPI^PSI
		PlatFormSendUtils.sendVitalSigns(paramMap);*/
	}
	
	/**
	 * ????????????015001011110
	 * ????????????????????????????????????????????????????????????????????????
	 * @param param
	 * @param user
	 */
	public void updateOpRegInfo(String param , IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,new TypeReference<Map<String,Object>>(){});
		if(paramMap!=null && paramMap.size()>0){
			if(paramMap.containsKey("pkPv") && paramMap.get("pkPv")!=null
					&& paramMap.containsKey("pkVisit") && paramMap.get("pkVisit")!=null){
				DataBaseHelper.execute("update ins_szyb_visit set pk_pv = ? where pk_visit = ?", new Object[]{paramMap.get("pkPv"),paramMap.get("pkVisit")});
			
				DataBaseHelper.execute("update ins_szyb_visit_city set pk_pv = ? where pk_visit = ?", new Object[]{paramMap.get("pkPv"),paramMap.get("pkVisit")});
			
				DataBaseHelper.execute("update PV_ENCOUNTER set pk_insu = ? where pk_pv = ?", new Object[]{paramMap.get("pkHp"),paramMap.get("pkPv")});
			}

			//1.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			//2.???????????????????????????????????????????????????
			if(paramMap.containsKey("flagSelfMac") && !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"flagSelfMac"))){
				//???????????????????????????????????????
				InsSzybVisit visit = DataBaseHelper.queryForBean(
						"select * from INS_SZYB_VISIT where PK_VISIT = ?"
						,InsSzybVisit.class,new Object[]{paramMap.get("pkVisit")});

				//??????????????????????????????????????????
				InsSzybVisitCity cityVo = DataBaseHelper.queryForBean(
						"select * from INS_SZYB_VISIT_city where pk_visit = ?"
						,InsSzybVisitCity.class,new Object[]{paramMap.get("pkVisit")});

				paramMap.put("pkPi",visit.getPkPi());

				//??????????????????????????????
				if(!CommonUtils.isEmptyString(visit.getCodeMedino())
					&& visit.getCodeMedino().length()<15){
					InsSzybVisit upVisit = shenzhenCityMapper.qryVisitInfoByPkPi(paramMap);
					if(upVisit!=null){
						visit.setCodeMedino(upVisit.getCodeMedino());

						DataBaseHelper.updateBeanByPk(visit,false);
					}
				}

				List<InsSzybVisitCity> cityList = shenzhenCityMapper.qryVisitCityByPkPi(paramMap);
				if(cityList!=null && cityList.size()>1){
					List<InsSzybVisitCity> filterList = null;

					filterList = cityList.stream()
							.filter(city-> !CommonUtils.isEmptyString(city.getCka303())).collect(Collectors.toList());
					if(filterList!=null && filterList.size()>0){
						cityVo.setCka303(filterList.get(0).getCka303());
					}

					filterList = cityList.stream()
							.filter(city-> !CommonUtils.isEmptyString(city.getCka305())).collect(Collectors.toList());
					if(filterList!=null && filterList.size()>0){
						cityVo.setCka305(filterList.get(0).getCka305());
					}

					filterList = cityList.stream()
							.filter(city-> city.getCme320()!=null && city.getCme320()!=0).collect(Collectors.toList());
					if(filterList!=null && filterList.size()>0){
						cityVo.setCme320(filterList.get(0).getCme320());
					}

					filterList = cityList.stream()
							.filter(city-> !CommonUtils.isEmptyString(city.getAmc021())).collect(Collectors.toList());
					if(filterList!=null && filterList.size()>0){
						cityVo.setAmc021(filterList.get(0).getAmc021());
					}

					filterList = cityList.stream()
							.filter(city-> city.getCme331()!=null && city.getCme331()!=0).collect(Collectors.toList());
					if(filterList!=null && filterList.size()>0){
						cityVo.setCme331(filterList.get(0).getCme331());
					}

					//????????????????????????
					DataBaseHelper.updateBeanByPk(cityVo,false);
				}
			}
		}
	}

	/**
	 * ????????????015001011111
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 */
	public void updateBlDtFlagInsu(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {});

		if (CommonUtils.isNotNull(paramMap.get("pkPv")) && CommonUtils.isNotNull(paramMap.get("flagInsu"))) {
			shenzhenCityMapper.updateOpdtFlagInsuByPk(paramMap);
		}
		
	}
	
	/**
	 * ????????????015001011113
	 * ????????????????????????????????????(???????????????????????????)
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryFirstOpVisitInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		
		List<Map<String,Object>> list = new ArrayList<>();
		if(paramMap!=null && paramMap.size()>0 
				&& paramMap.containsKey("pkPv") && paramMap.get("pkPv")!=null){
			list = shenzhenCityMapper.qryOpVisitInfo(paramMap);
		}
		
		if(list==null || list.size()<=0){
			throw new BusException("?????????????????????????????????");
		}

		return list.get(list.size()-1);
	}
	/**
	 * 015001011112 ??????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryVisitsOp(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> list = shenzhenCityMapper.qryOpVisitInfo(paramMap);
		if (list == null || list.size() <= 0) {
			throw new BusException("?????????????????????????????????");
		}else{
			//??????????????????????????????
			if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(list.get(0),"codeMedino"))
					&& CommonUtils.getPropValueStr(list.get(0),"codeMedino").length()<15){
				paramMap.put("pkPi",list.get(0).get("pkPi"));
				InsSzybVisit upVisit = shenzhenCityMapper.qryVisitInfoByPkPi(paramMap);
				if(upVisit!=null){
					//???????????????????????????????????????
					InsSzybVisit visit = DataBaseHelper.queryForBean(
							"select * from INS_SZYB_VISIT where PK_VISIT = ?"
							,InsSzybVisit.class,new Object[]{list.get(0).get("pkVisit")});

					visit.setCodeMedino(upVisit.getCodeMedino());

					DataBaseHelper.updateBeanByPk(visit,false);

					list.get(0).remove("codeMedino");
					list.get(0).put("code_Medino",upVisit.getCodeMedino());
				}
			}
		}

		return list;
	}
	
	/**
	 * ????????????015001011114
	 * ????????????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public String chkInsuCbReg(String param, IUser user){
		Boolean flagReg = chkInsuCbRegImpl(param);
		if(flagReg!=null){
			if(flagReg){
				return "1";
			}else{
				return "0";
			}
		}else{
			return "0";
		}
	}
	
	private Boolean chkInsuCbRegImpl(String param){
		Boolean flagReg = false;
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap!=null && paramMap.size()>0 
				&& paramMap.containsKey("pkVisit") && !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkVisit")))
				&& paramMap.containsKey("persontype") && !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("persontype")))){
			//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			String oldPersontype = DataBaseHelper.queryForScalar(
					"select persontype from ins_szyb_visit where pk_visit = ?",
					String.class, paramMap.get("pkVisit"));
			if(!CommonUtils.isEmptyString(oldPersontype)
					&& oldPersontype.equals(CommonUtils.getString(paramMap.get("persontype")))){
				return flagReg;
			}
			
			
			Integer oldCnt = DataBaseHelper.queryForScalar(
					"select count(1) from ins_szyb_dict where eu_hpdicttype='01' and flag_chd='1' and del_flag='0' and code_type = 'AKA130' and note like '%R%' and code = ?",
					Integer.class, new Object[]{oldPersontype});
			
			Integer newCnt = DataBaseHelper.queryForScalar(
					"select count(1) from ins_szyb_dict where eu_hpdicttype='01' and flag_chd='1' and del_flag='0' and code_type = 'AKA130' and note like '%R%' and code = ?",
					Integer.class, new Object[]{paramMap.get("persontype")});
			
			//?????????????????????????????????????????????????????????????????????????????????????????????0??????
			Integer allCnt = DataBaseHelper.queryForScalar(
					"select count(1) from ins_szyb_dict where eu_hpdicttype='01' and flag_chd='1' and del_flag='0' and code_type = 'AKA130' and note like '%R%' and (code = ? or code = ?)",
					Integer.class, new Object[]{paramMap.get("persontype"),oldPersontype});
			
			if((oldCnt==1 || newCnt==1) && allCnt!=2){
				flagReg = true;
				return flagReg;
			}
		}
		return flagReg;
	}
	
	/**
	 * ????????????015001011115
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryBirthInsuRegInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		List<Map<String,Object>> retMap = new ArrayList<>();
		if(paramMap!=null && paramMap.size()>0 
				&& paramMap.containsKey("idNo") && !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("idNo")))){
			
			//???????????????????????????????????????????????????
			retMap = DataBaseHelper.queryForList(
					"select * from ins_szyb_visit_city where aac147 = ? and del_flag = '0' and amc021 is not null order by CREATE_TIME desc",
					new Object[]{paramMap.get("idNo")});
			
		}
		
		if(retMap==null || retMap.size()<=0){
			return null;
		}
			
		return retMap.get(0);
	}

	/**
	 * ????????????015001011116
	 * ?????????????????????????????????????????????
	 * @param param
	 * @param user
	 */
	public void updateInsVisitCityCka(String param , IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap != null && paramMap.size() > 0) {
			if (paramMap.containsKey("pkPv") && paramMap.get("pkPv") != null && paramMap.containsKey("cka303")) {
				String sql = "update ins_szyb_visit_city set ";
				if (paramMap.get("cka303") == null || paramMap.get("cka303").toString() == "") {
					sql += "cka303 = null ";
				} else {
					sql += "cka303 = '" + paramMap.get("cka303") + "'";
				}
				sql += " where pk_pv='" + paramMap.get("pkPv") + "'";
				DataBaseHelper.execute(sql);
			}
		}
	}

	/**
	 * ????????????015001011118
	 * ??????????????????????????????0205????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<String> qryOrdAttrByPkCgop(String param , IUser user){
		List<String> rtnList = new ArrayList<>();

		List<String> pkList = JsonUtil.readValue(
				param,
				new TypeReference<List<String>>() {
				});

		if(pkList!=null && pkList.size()>0){
			//????????????????????????
			rtnList = shenzhenCityMapper.qryOrdAttrByPkCgop(pkList);
		}

		return rtnList;
	}

	/**
	 * ????????????015001011119
	 * ???????????????????????????????????????????????????
	 * @param param
	 * @param user
	 */
	public void upVisitSpSurv(String param , IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap!=null && paramMap.size()>0
				&& !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"pkPv"))){
			//????????????????????????????????????
			List<String> valList = shenzhenCityMapper.qrySpecSuByPkPv(paramMap);

			if(valList!=null && valList.size()>0){
				List<Map<String,Object>> pkList = DataBaseHelper.queryForList(
						"select pk_visit from INS_SZYB_VISIT where pk_pv = ? order by date_reg desc"
				,new Object[]{paramMap.get("pkPv")});

				if(pkList!=null && pkList.size()>0){
					DataBaseHelper.execute(
							"update ins_szyb_visit_city set cka304 = ? where pk_visit = ?",
							new Object[]{valList.get(0),CommonUtils.getPropValueStr(pkList.get(0),"pkVisit") });
				}
			}
		}
	}

	/**
	 * ????????????015001011120
	 * ???????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryCodeOpByAaz500(String param , IUser user){
		List<String> insuNoList = JsonUtil.readValue(param,ArrayList.class);

		List<Map<String,Object>> rtnList = new ArrayList<>();
		if(insuNoList!=null && insuNoList.size()>0){
			rtnList = shenzhenCityMapper.qryCodeOpByAaz500(insuNoList);
		}

		return rtnList;
	}

	/**
	 * ????????????015001011121
	 * ?????????????????????????????????HIS??????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryHisStInfoByBkc384(String param , IUser user){
		List<String> bkc384List = JsonUtil.readValue(param,ArrayList.class);
		List<Map<String,Object>> rtnList = new ArrayList<>();
		if(bkc384List!=null && bkc384List.size()>0){
			rtnList = shenzhenCityMapper.qryHisStInfoByBkc384(bkc384List);
		}
		return rtnList;
	}

	/**
	 * ????????????015001011122
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public String saveQgybOpVisit(String param , IUser user){
		User u = (User) user;
		InsQgybVisit visitVo= JsonUtil.readValue(JsonUtil.getJsonNode(param, "insQgybVisit"), InsQgybVisit.class);

		// ????????????  1
		if (visitVo != null ) {
			if(CommonUtils.isEmptyString(visitVo.getPkVisit())){
				visitVo.setTs(new Date());
				ApplicationUtils.setDefaultValue(visitVo,true);
				DataBaseHelper.insertBean(visitVo);

				String visitJsonNode=JsonUtil.getJsonNode(param, "insQgybVisit").toString();
				InsQgybPv ybPvVo= JsonUtil.readValue(JsonUtil.getJsonNode(visitJsonNode, "insQgybPv"), InsQgybPv.class);
				if(ybPvVo!=null){
					ybPvVo.setTs(new Date());
					ApplicationUtils.setDefaultValue(ybPvVo,true);
					DataBaseHelper.insertBean(ybPvVo);
				}
			}else{
				if(EnumerateParameter.ONE.equals(visitVo.getDelFlag())){
					//???????????????????????????
					DataBaseHelper.execute("update ins_qgyb_visit set DEL_FLAG = '1' where pk_pv = ?",new Object[]{visitVo.getPkPv()});
					DataBaseHelper.execute("update ins_qgyb_pv set DEL_FLAG = '1' where pk_pv = ?",new Object[]{visitVo.getPkPv()});
				}else{
					//??????????????????????????????
					String visitJsonNode=JsonUtil.getJsonNode(param, "insQgybVisit").toString();
					InsQgybPv newYbPvVo= JsonUtil.readValue(JsonUtil.getJsonNode(visitJsonNode, "insQgybPv"), InsQgybPv.class);

					//???????????????????????????
					InsQgybPv oldYbPvInfo = DataBaseHelper.queryForBean(
							"select * from ( select * from ins_qgyb_pv where MDTRT_ID = ? and del_flag = '0' order by CREATE_TIME desc ) where rownum = 1",
							InsQgybPv.class,new Object[]{visitVo.getMdtrtId()});

					if(oldYbPvInfo!=null){
						//??????
						oldYbPvInfo.setMedType(newYbPvVo.getMedType());
						oldYbPvInfo.setMatnType(newYbPvVo.getMatnType());
						oldYbPvInfo.setGesoVal(newYbPvVo.getGesoVal());
						oldYbPvInfo.setFpscNo(newYbPvVo.getFpscNo());
						oldYbPvInfo.setDiseTypeCode(newYbPvVo.getDiseTypeCode());
						oldYbPvInfo.setLatechbFlag(newYbPvVo.getLatechbFlag());
						oldYbPvInfo.setPretFlag(newYbPvVo.getPretFlag());
						oldYbPvInfo.setBirctrlType(newYbPvVo.getBirctrlType());
						oldYbPvInfo.setFetts(newYbPvVo.getFetts());
						oldYbPvInfo.setFetusCnt(newYbPvVo.getFetusCnt());
						oldYbPvInfo.setBirctrlMatnDate(newYbPvVo.getBirctrlMatnDate());
						oldYbPvInfo.setOprnOprtCode(newYbPvVo.getOprnOprtCode());

						DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybPv.class),oldYbPvInfo);
					}else{
						//??????
						newYbPvVo.setTs(new Date());
						ApplicationUtils.setDefaultValue(newYbPvVo,true);
						newYbPvVo.setPkPv(visitVo.getPkPv());
						newYbPvVo.setPkPi(visitVo.getPkPi());
						newYbPvVo.setPkHp(visitVo.getPkHp());
						newYbPvVo.setNamePi(visitVo.getNamePi());

						DataBaseHelper.insertBean(newYbPvVo);
					}
				}
			}
		}

		return visitVo.getPkVisit();
	}

	/***
	 * ????????????015001011123
	 *???????????????????????????????????????
	 * @param param
	 * @param user
	 */
	public void saveHisYbRelationship(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String pkPv = paramMap.get("pkPv").toString();

		String pkSettle = null;
		String ywlx = null;//????????????
		String pkInsst = null;//??????????????????
		String pkVisit = null;

		if(paramMap.containsKey("pkSettle") && paramMap.get("pkSettle")!=null){
			pkSettle = CommonUtils.getString(paramMap.get("pkSettle"));
		}
		if(paramMap.containsKey("ywlx") && paramMap.get("ywlx")!=null){
			ywlx = CommonUtils.getString(paramMap.get("ywlx"));
		}
		if(paramMap.containsKey("pkInsst") && paramMap.get("pkInsst")!=null){
			pkInsst = CommonUtils.getString(paramMap.get("pkInsst"));
		}
		//?????????????????????????????????????????????????????????
		if(!CommonUtils.isEmptyString(ywlx) && "0".equals(ywlx)){
			if(paramMap.containsKey("pkVisit") && paramMap.get("pkVisit")!=null){
				pkVisit = CommonUtils.getString(paramMap.get("pkVisit"));
			}

			BlSettle stInfo = new BlSettle();
			if(!CommonUtils.isEmptyString(pkSettle)){
				//??????????????????
				stInfo = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle = ?", BlSettle.class, new Object[]{pkSettle});
			}

			//????????????????????????
			PvEncounter pvInfo = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where pk_pv = ?", PvEncounter.class, new Object[]{pkPv});
			//????????????????????????
			PiMaster master = DataBaseHelper.queryForBean("select * from pi_master where pk_pi = ?", PiMaster.class, new Object[]{pvInfo.getPkPi()});

			//????????????????????????
			InsQgybVisit visitInfo = DataBaseHelper.queryForBean("select * from ins_qgyb_visit where pk_visit = ?", InsQgybVisit.class, new Object[]{pkVisit});
			visitInfo.setPkHp(pvInfo.getPkInsu());
			visitInfo.setPkPv(pvInfo.getPkPv());
			visitInfo.setPkPi(pvInfo.getPkPi());
			visitInfo.setNamePi(pvInfo.getNamePi());
			visitInfo.setIdno(master.getIdNo());
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybVisit.class), visitInfo);

			//????????????????????????
			if(!CommonUtils.isEmptyString(pkInsst)){
				InsQgybSt insuStInfo = DataBaseHelper.queryForBean("select * from ins_qgyb_st where setl_id = ?", InsQgybSt.class, new Object[]{pkInsst});
				insuStInfo.setPkHp(pvInfo.getPkInsu());
				insuStInfo.setPkPv(pvInfo.getPkPv());
				insuStInfo.setPkPi(pvInfo.getPkPi());
				insuStInfo.setPkSettle(pkSettle);
				insuStInfo.setDateSt(stInfo!=null && stInfo.getDateSt()!=null?stInfo.getDateSt():new Date());
				DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybSt.class), insuStInfo);
			}
		}else if(!CommonUtils.isEmptyString(ywlx) && "1".equals(ywlx)){//????????????
			if(!CommonUtils.isEmptyString(pkInsst)){
				DataBaseHelper.execute("update ins_qgyb_st set PK_SETTLE=? where del_flag='0' and PK_PV=? and setl_id = ?",pkSettle, pkPv,pkInsst);
				DataBaseHelper.execute("update INS_QGYB_VISIT set EU_STATUS_ST='1' where del_flag='0' and PK_VISIT in (select PK_VISIT from ins_qgyb_st where setl_id = ?)",pkInsst);
			}
		}else{
			List<InsQgybSt> stList = DataBaseHelper.queryForList(
					"select * from ins_qgyb_st where pk_pv = ? and setl_id = ? and pk_settle is null and del_flag='0' order by date_st desc",
					InsQgybSt.class, new Object[]{pkPv,pkInsst});

			if(stList!=null && stList.size()>0){
				InsQgybSt stVo = stList.get(0);
				stVo.setPkSettle(pkSettle);

				DataBaseHelper.updateBeanByPk(stVo);
			}

			//DataBaseHelper.execute("update ins_szyb_st set PK_SETTLE=? where del_flag='0' and PK_PV=? and pk_settle is null",pkSettle, pkPv);
			DataBaseHelper.execute("update ins_qgyb_visit set EU_STATUS_ST='1' where del_flag='0' and PK_PV=? ",pkPv);
		}
	}

	/**
	 * ????????????015001011124
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryOpQgybVisitInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);

		List<Map<String,Object>> list = new ArrayList<>();
		if(paramMap!=null && paramMap.size()>0
				&& paramMap.containsKey("pkPv") && paramMap.get("pkPv")!=null){
			list = shenzhenCityMapper.qryQgybOpVisitInfo(paramMap);
		}

		if(list==null || list.size()<=0){
			return null;
		}

		return list.get(0);
	}

	/**
	 * 015001011125 ??????????????????????????????HIS???????????????????????????????????????
	 *
	 * @return
	 */
	public void reQgybNewalVisit(String param, IUser user) {
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if (StringUtils.isNotBlank(paramMap.get("pkPv").toString())  && StringUtils.isNotBlank(paramMap.get("pkPi").toString())   ) {
			InsQgybVisit visitInfo = DataBaseHelper.queryForBean(
					"select * from ins_qgyb_visit where pk_visit = ?"
					,InsQgybVisit.class,new Object[]{paramMap.get("pkVisit")});

			if(visitInfo!=null && !CommonUtils.isEmptyString(visitInfo.getPkVisit())){
				visitInfo.setPkPi(CommonUtils.getPropValueStr(paramMap,"pkPi"));
				visitInfo.setPkPv(CommonUtils.getPropValueStr(paramMap,"pkPv"));

				DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybVisit.class),visitInfo);
			}

			//???????????????????????????????????????
			InsQgybPv visitPvInfo = DataBaseHelper.queryForBean(
					"select * from ins_qgyb_pv where mdtrt_id = ?"
					,InsQgybPv.class,new Object[]{visitInfo.getMdtrtId()});

			if(visitPvInfo!=null && !CommonUtils.isEmptyString(visitPvInfo.getPkInspv())){
				visitPvInfo.setPkPi(visitInfo.getPkPi());
				visitPvInfo.setPkPv(visitInfo.getPkPv());

				DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybPv.class),visitPvInfo);
			}

		} else {
			throw new BusException("?????????????????????");
		}
	}

	/**
	 * ????????????015001011126
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryQgybVisitInfo(String param, IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		Map<String,Object> visitMap = null;

		visitMap = shenzhenCityMapper.qryQgybVisitInfo(paramMap);

		return visitMap;
	}

	/**
	 * ????????????015001011127
	 * ??????????????????????????????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryInsuPvInfo(String param, IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);

		List<Map<String,Object>> rtnList = shenzhenCityMapper.qryInsuPvInfo(paramMap);

		return rtnList;
	}

	/**
	 * ????????????015001011128
	 * ????????????????????????
	 * @param param
	 * @param user
	 */
	public void updateInsuPvInfo(String param, IUser user){
		InsQgybPV insuPvInfo=JsonUtil.readValue(param, InsQgybPV.class);

		if(insuPvInfo!=null){
			ApplicationUtils.setDefaultValue(insuPvInfo,false);
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybPV.class),insuPvInfo);
		}
	}

	/**
	 * ????????????015001011129
	 *??????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryInsuStInfo(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);

		Map<String,Object> rtnMap = new HashMap<>(16);

		//????????????????????????
		InsQgybSt ybstInfo = DataBaseHelper.queryForBean(
				"select * from ins_qgyb_st where PK_SETTLE = ? order by CREATE_TIME desc "
				,InsQgybSt.class,new Object[]{paramMap.get("pkSettle")});

		//???????????????????????????????????????
		if(ybstInfo!=null && !CommonUtils.isEmptyString(ybstInfo.getPkInspv())){
			paramMap.clear();
			paramMap.put("pkInspv",ybstInfo.getPkInspv());
			List<Map<String,Object>> ybstPv = shenzhenCityMapper.qryInsuPvInfo(paramMap);
			rtnMap.put("ybstPv",ybstPv!=null && ybstPv.size()>0?ybstPv.get(0):null);

			paramMap.put("pkVisit",ybstInfo.getPkVisit());
			paramMap.put("pkPv",ybstInfo.getPkPv());
			rtnMap.put("ybstVisit",shenzhenCityMapper.qryQgybVisitInfo(paramMap));
		}

		rtnMap.put("ybstInfo",ybstInfo);

		return rtnMap;
	}

	/**
	 * ????????????015001011130
	 * ????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public InsQgybPV qryHistoryInsuPvInfo(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);

		InsQgybPV cityVo = new InsQgybPV();

		List<InsQgybPV> qgybPvList = shenzhenCityMapper.qryInsuPvByPkPi(paramMap);
		if(qgybPvList!=null && qgybPvList.size()>0){
			List<InsQgybPV> filterList = null;

			filterList = qgybPvList.stream()
					.filter(city-> !CommonUtils.isEmptyString(city.getDiseCodg())).collect(Collectors.toList());
			if(filterList!=null && filterList.size()>0){
				cityVo.setDiseCodg(filterList.get(0).getDiseCodg());
			}

			filterList = qgybPvList.stream()
					.filter(city-> !CommonUtils.isEmptyString(city.getDiseName())).collect(Collectors.toList());
			if(filterList!=null && filterList.size()>0){
				cityVo.setDiseName(filterList.get(0).getDiseName());
			}

			filterList = qgybPvList.stream()
					.filter(city-> !CommonUtils.isEmptyString(city.getBirctrlType())).collect(Collectors.toList());
			if(filterList!=null && filterList.size()>0){
				cityVo.setBirctrlType(filterList.get(0).getBirctrlType());
			}

			filterList = qgybPvList.stream()
					.filter(city-> city.getBirctrlMatnDate()!=null).collect(Collectors.toList());
			if(filterList!=null && filterList.size()>0){
				cityVo.setBirctrlMatnDate(filterList.get(0).getBirctrlMatnDate());
			}

			filterList = qgybPvList.stream()
					.filter(city-> !CommonUtils.isEmptyString(city.getMatnType())).collect(Collectors.toList());
			if(filterList!=null && filterList.size()>0){
				cityVo.setMatnType(filterList.get(0).getMatnType());
			}

			filterList = qgybPvList.stream()
					.filter(city-> !CommonUtils.isEmptyString(city.getGesoVal())).collect(Collectors.toList());
			if(filterList!=null && filterList.size()>0){
				cityVo.setGesoVal(filterList.get(0).getGesoVal());
			}
		}

		return cityVo;
	}

	/**
	 * 015001011131  ?????????????????????????????????
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryQgFitInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnList = new ArrayList<>();
		if(paramMap.containsKey("pkCnord") &&
			!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"pkCnord"))
		){
			rtnList= shenzhenCityMapper.querySavedQgFitInfo(paramMap);
		}else{
			rtnList= shenzhenCityMapper.queryQgFitInfo(paramMap);
		}

		return rtnList;
	}

	/**
	 * ????????????015001011132
	 * ?????????????????????????????????
	 */
	public void saveQgFitInfo(String param , IUser user){
		List<InsSzybOrder> insSzybOrderList = JsonUtil.readValue(param,new TypeReference<List<InsSzybOrder>>() {});

		if(insSzybOrderList!=null && insSzybOrderList.size()>0){
			List<CnOrder> updateCnordList=new ArrayList<CnOrder>();

			insSzybOrderList.stream().filter(m-> !CommonUtils.isEmptyString(m.getPkCnord())).forEach(vo->{
				CnOrder ord = new CnOrder();
				ord.setFlagFit(vo.getFlagFit());
				ord.setDescFit(vo.getDescFit());
				ord.setPkCnord(vo.getPkCnord());

				updateCnordList.add(ord);
			});

			if(updateCnordList!=null && updateCnordList.size()>0){
				String sqlStr="update cn_order set flag_fit=:flagFit,desc_fit=:descFit where pk_cnord=:pkCnord ";
				DataBaseHelper.batchUpdate(sqlStr ,updateCnordList);
			}
		}
	}

	/**
	 * ????????????015001011133
	 * ??????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryQgybVisitRegInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);

		Map<String,Object> rtnMap = new HashMap<>();

		List<Map<String,Object>> regList = shenzhenCityMapper.qryQgybVisitRegInfo(paramMap);

		if(regList!=null && regList.size()>0){
			rtnMap = regList.get(0);
		}

		return rtnMap;
	}

}
