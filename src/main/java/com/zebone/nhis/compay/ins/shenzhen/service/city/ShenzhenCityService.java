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
	 * 015001011001 就诊登记
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
		// 医保登记  1    
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

		//医保登记_深圳医保   
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
		
		
		//针对自费转基本医疗保险的诊查费处理(退掉F00000000013，收F00000105411)
		if(visitVo!=null && StringUtils.isNotBlank(visitVo.getPkPv()) && insSzybVisitCity!=null && "310".equals(insSzybVisitCity.getAae140())){
			//退费
			List<RefundVo> refundList = new ArrayList<RefundVo>();
			//计费
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
	 * 015001011093 通过医保登记主键修改HIS就诊登记就诊主键、患者主键
	 * 
	 * @return
	 */
	public void reNewalVisit(String param, IUser user) {
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if (StringUtils.isNotBlank(paramMap.get("pkPv").toString())  && StringUtils.isNotBlank(paramMap.get("pkPi").toString())   ) {
			DataBaseHelper.update("update ins_szyb_visit set PK_PV=:pkPv,PK_PI=:pkPi where pk_visit=:pkVisit",paramMap);
			DataBaseHelper.update("update ins_szyb_visit_city set PK_PV=:pkPv where pk_visit=:pkVisit",paramMap);
		} else {
			throw new BusException("传入参数有空值");
		}
	}
	

	/**
	 * 015001011049 -- 删除就诊登记记录
	 * 深圳医保是先保存pv_encounter表后再调用医保登记接口（SaveZyHpRegRelationship）。
	 * 在医保登记接口失败后，需要把pv_encounter的记录删除。
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
	 * 015001011002 返回就诊登记信息
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryVisit(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = shenzhenCityMapper.qryVisit(paramMap);
		if(list==null || list.size()<=0){
			throw new BusException("就诊登记信息查询错误！");
		}

		return list.get(0);
	}

	/**
	 * 015001011050 返回就诊登记附加信息
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryVisitCity(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = shenzhenCityMapper.qryVisitCity(paramMap);
		if((list==null) || (list!=null && list.size()<1)){
			throw new BusException("未查询到深圳市医保就诊登记信息！");
		}

		return list.get(0);
	}

	/**
	 * 015001011006 查询上传明细
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
	 * 015001011083 查询上传外购处方明细
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
	 * 015001011007 根据收费结算-住院收费明细主键的集合更新bl_ip_dt的flag_insu字段
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


		//插入表数据INS_SZYB_CITYCG
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
	 * 015001011082  	更改处方上传标示
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

		//插入表数据INS_SZYB_CITYCG
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
	 * 015001011008 查询病人诊断
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
	 * 015001011010 查询医疗费用总额等相关信息
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryBlStatistical(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> presNoList = shenzhenCityMapper.qryPresNo(paramMap);
		if((presNoList==null) || (presNoList!=null && presNoList.size()!=1)){
			throw new BusException("查询出【费用明细录入的结算业务序列号】不唯一！");
		}

		Map<String,Object> map = shenzhenCityMapper.qryBlStatistical(paramMap);
		map.put("presNo", presNoList.get(0).get("presno").toString());

		return map;
	}

	/**
	 * 015001011011 保存医保结算信息数据
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
			throw new BusException("未获得结算基本信息！");
		if (stCity == null)
			throw new BusException("未获得市医保结算信息！");

		//医保结算
		String sql = "update ins_szyb_st set del_flag='1' where pk_visit=? and code_hpst is null";
		DataBaseHelper.update(sql, new Object[] { st.getPkVisit() });

		//医保结算--深圳市医保
		sql = "update ins_szyb_st_city set del_flag='1' where pk_insst in (select pk_insst from ins_szyb_st where pk_visit=?  and code_hpst is null)";
		DataBaseHelper.update(sql, new Object[] { st.getPkVisit() });

		//医保结算--深圳市医保--结算明细
		sql = "select * from ins_szyb_st_city where pk_insst in (select pk_insst from ins_szyb_st where pk_visit=? and code_hpst is null)";
		List<InsSzybStCity> insSzybStCityList = DataBaseHelper.queryForList(sql, InsSzybStCity.class, new Object[] { st.getPkVisit() });
		if(insSzybStCityList!=null && insSzybStCityList.size()>0){
			sql = "update ins_szyb_st_citydt set del_flag='1' where pk_insstcity = :pkInsstcity ";
			DataBaseHelper.batchUpdate(sql, insSzybStCityList);
		}
		

//		sql = "update ins_szyb_visit set code_insst=null  where pk_visit=?";
//		DataBaseHelper.update(sql, new Object[] { st.getPkVisit() });


		//插表ins_szyb_st
		st.setPkOrg(u.getPkOrg());
		st.setDelFlag("0");
		st.setCreateTime(dt);
		st.setCreator(u.getPkEmp());
		st.setTs(dt);
		DataBaseHelper.insertBean(st);

		//插表ins_szyb_st_city
		stCity.setPkInsst(st.getPkInsst());
		stCity.setPkOrg(u.getPkOrg());
		stCity.setDelFlag("0");
		stCity.setCreateTime(dt);
		stCity.setCreator(u.getPkEmp());
		stCity.setTs(dt);
		DataBaseHelper.insertBean(stCity);

		//插表ins_szyb_st_citydt
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
	 * 通过医保结算病人就诊主键PK_PV将PkSettle结算主键更新到INS_GZYB_ST表同时更新INS_GZYB_VISIT表
	 *
	 * @param param
	 * @param user
	 */
	public void updatePkSettle(String param, IUser user) {
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String pkPv = paramMap.get("pkPv").toString();
		
		String pkSettle = null;
		String ywlx = null;//业务类型
		String pkInsst = null;//医保结算主键
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
		//挂号情况，保存患者就诊信息到医保结算表
		if(!CommonUtils.isEmptyString(ywlx) && "0".equals(ywlx)){
			if(paramMap.containsKey("pkVisit") && paramMap.get("pkVisit")!=null){
				pkVisit = CommonUtils.getString(paramMap.get("pkVisit"));
			}
			
			BlSettle stInfo = new BlSettle();
			if(!CommonUtils.isEmptyString(pkSettle)){
				//查询结算信息
				stInfo = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle = ?", BlSettle.class, new Object[]{pkSettle});
			}
			//查询患者就诊信息
			PvEncounter pvInfo = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where pk_pv = ?", PvEncounter.class, new Object[]{pkPv});
			//查询患者基本信息
			PiMaster master = DataBaseHelper.queryForBean("select * from pi_master where pk_pi = ?", PiMaster.class, new Object[]{pvInfo.getPkPi()});
			//查询就诊医保信息
			BdHp hpInfo = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp = ?", BdHp.class, new Object[]{pvInfo.getPkInsu()});
			
			//查询医保结算信息
			if(!CommonUtils.isEmptyString(pkInsst)){
				InsSzybSt insuStInfo = DataBaseHelper.queryForBean("select * from ins_szyb_st where PK_insst = ?", InsSzybSt.class, new Object[]{pkInsst});
				insuStInfo.setPkHp(pvInfo.getPkInsu());
				insuStInfo.setPkPv(pvInfo.getPkPv());
				insuStInfo.setPkPi(pvInfo.getPkPi());
				insuStInfo.setPkSettle(pkSettle);
				insuStInfo.setDateSt(stInfo!=null && stInfo.getDateSt()!=null?stInfo.getDateSt():new Date());
				DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsSzybSt.class), insuStInfo);
			}
			
			//查询医保登记信息
			InsSzybVisit visitInfo = DataBaseHelper.queryForBean("select * from ins_szyb_visit where pk_visit = ?", InsSzybVisit.class, new Object[]{pkVisit});
			visitInfo.setPkHp(pvInfo.getPkInsu());
			visitInfo.setPkPv(pvInfo.getPkPv());
			visitInfo.setPkPi(pvInfo.getPkPi());
			visitInfo.setBirthDate(master.getBirthDate());
			visitInfo.setEuStatusSt("0");
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsSzybVisit.class), visitInfo);
			
			//修改医保登记明细信息
			DataBaseHelper.execute("update ins_szyb_visit_city set bcc334=?,pk_pv=? where pk_visit = ?",hpInfo.getCode(),pvInfo.getPkPv(),pkVisit);
		}else if(!CommonUtils.isEmptyString(ywlx) && "1".equals(ywlx)){//门诊结算
			if(!CommonUtils.isEmptyString(pkInsst)){
				DataBaseHelper.execute("update ins_szyb_st set PK_SETTLE=? where del_flag='0' and PK_PV=? and pk_insst = ?",pkSettle, pkPv,pkInsst);
				DataBaseHelper.execute("update ins_szyb_visit set EU_STATUS_ST='1' where del_flag='0' and PK_VISIT in (select PK_VISIT from ins_szyb_st where pk_insst = ?)",pkInsst);
			}
		}else{
			//保存关联关系时删除预结算生成的垃圾数据
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
	 * 清空医保结算信息
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
			//明细上传返回记录
			String sql = "update ins_szyb_citycg set del_flag='1' where pk_cgip in (select pk_cgip from bl_ip_dt where pk_pv=? and pk_settle=?)";
			DataBaseHelper.update(sql, new Object[] { pkPv,pkSettle });

			//结算明细--深圳市医保
			sql = "update ins_szyb_st_citydt set del_flag='1' where pk_insstcity in( select pk_insstcity from ins_szyb_st_city where pk_insst in (select pk_insst from ins_szyb_st where pk_pv=? and pk_settle=?))";
			DataBaseHelper.update(sql, new Object[] { pkPv,pkSettle });

			//结算--深圳市医保
			sql = "update ins_szyb_st_city set del_flag='1' where pk_insst in (select pk_insst from ins_szyb_st where pk_pv=? and pk_settle=?)";
			DataBaseHelper.update(sql, new Object[] { pkPv,pkSettle });

			//深圳医保结算
			sql = "update ins_szyb_st set del_flag='1',code_hpst_canc=?, canc_time=? where pk_pv=? and pk_settle=?";
			DataBaseHelper.update(sql, new Object[] {codeHpstCanc, dateNow,pkPv,pkSettle });

			//就诊登记信息--深圳市医保
			sql = "update ins_szyb_visit set EU_STATUS_ST='0' where pk_visit=?";//,code_insst=null
			DataBaseHelper.update(sql, new Object[] { pkVisit });

			sql = "update bl_ip_dt set flag_insu= '0',name_itemset=null where pk_pv=? and pk_settle=?";
			DataBaseHelper.update(sql, new Object[] { pkPv,pkSettle });
		}else if(!CommonUtils.isEmptyString(pkInsst)){
			String sql = null;
			
			//结算明细--深圳市医保
			sql = "update ins_szyb_st_citydt set del_flag='1' where pk_insstcity in( select pk_insstcity from ins_szyb_st_city where pk_insst = ?)";
			DataBaseHelper.update(sql, new Object[] { pkInsst });
			
			//结算--深圳市医保
			sql = "update ins_szyb_st_city set del_flag='1' where pk_insst = ?";
			DataBaseHelper.update(sql, new Object[] { pkInsst });
			
			//深圳医保结算
			sql = "update ins_szyb_st set del_flag='1',code_hpst_canc=?, canc_time=? where pk_insst = ?";
			DataBaseHelper.update(sql, new Object[] {codeHpstCanc, dateNow,pkInsst });
			
			//就诊登记信息--深圳市医保
			sql = "update ins_szyb_visit set EU_STATUS_ST='0' where pk_visit in (select pk_insst from ins_szyb_st where pk_insst=?)";
			DataBaseHelper.update(sql, new Object[] { pkInsst });
		}
	}
		

	/**
	 * 015001011014  查询病人医保结算记录
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
	 * 下载医保局药品信息到医保目录ins_szyb_item里面
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
			DataBaseHelper.batchUpdate("update ins_szyb_item set del_flag = '1' where eu_hpdicttype = '01' and ake003 ='1' and ake001 = :ake001 ", insSzybItemList);//删除再重新导入的时候将此行代码注释将提升效率
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybItem.class), insSzybItemList);
		}

	}

	/**
	 * 交易码：015001011088
	 * 保存疾病信息
	 * 保存ins_szyb_dise表
	 * @param param
	 * @param user
	 */
	public void saveYBDiseInfos(String param,IUser user){
		SzYbDiseInfo szYbDiseInfo = JsonUtil.readValue(param, SzYbDiseInfo.class);
		if(szYbDiseInfo!=null &&
				szYbDiseInfo.getOutputlist()!=null && szYbDiseInfo.getOutputlist().size()>0){
			List<InsSzybDise> diseList = szYbDiseInfo.getOutputlist();

			//查询已经存在的疾病信息
			List<String> codeList = new ArrayList<>();
			for(int i=0; i<diseList.size(); i++){
				codeList.add(diseList.get(i).getAka120());
				diseList.get(i).setAka020(PinyinUtils.getPinYinHeadChar(diseList.get(i).getAka121()).toUpperCase());
				ApplicationUtils.setDefaultValue(diseList.get(i), true);
			}

			List<InsSzybDise> diseDbList = shenzhenCityMapper.qryDiseListByCode(codeList);
			if(diseDbList!=null && diseDbList.size()>0){
				//修改集合
				List<InsSzybDise> updateDise = new ArrayList<>();
				//新增集合
				List<InsSzybDise> insertDise = new ArrayList<>();

				boolean flag;
				for(InsSzybDise dise : diseList){
					flag = false;
					for(InsSzybDise dbDise : diseDbList){
						//存在相同编码则更新
						if(dise.getAka120().equals(dbDise.getAka120())){
							updateDise.add(copyDiseInfo(dise,dbDise));
							flag = true;
							break;
						}
					}
					//新增
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
	 *	获取三大目录的更新日期的前一天的时间       并根据日期删除当天的目录信息
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
		}else if("4".equals(catalogueClass)){//社保疾病目录
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
	 * 下载医保局诊疗信息到医保目录ins_szyb_item里面
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
			DataBaseHelper.batchUpdate("update ins_szyb_item set del_flag = '1' where eu_hpdicttype = '01' and ake003 ='2' and ake001 = :ake001 ", insSzybItemList);//删除再重新导入的时候将此行代码注释将提升效率
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybItem.class), insSzybItemList);
		}

	}



	/**
	 * 015001011026
	 *
	 * 下载医保局材料信息到医保目录ins_szyb_item里面
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
			DataBaseHelper.batchUpdate("update ins_szyb_item set del_flag = '1' where eu_hpdicttype = '01' and  (ake003 ='3' or ake003 = '5') and ake001 =  :ake001 ", insSzybItemList);//删除再重新导入的时候将此行代码注释将提升效率
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybItem.class), insSzybItemList);
		}

	}







	/**
	 * 015001011027
	 * 返回三大目录药品类的对象查询结果 --医院目录
	 * @param para
	 * @return
	 */
	public  BdPdAndCount DrugPageQueryHospital(String param, IUser user){


		BdPdAndCount bdpdAndCount = new BdPdAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		//药品信息
		List<BdPd> listBdPd = shenzhenCityMapper.qryBdPdInfo(qryparam);
		Page<List<BdPd>> page = MyBatisPage.getPage();
		bdpdAndCount.setBdPdInfoList(listBdPd);
		bdpdAndCount.setTotalCount(page.getTotalCount());
		return bdpdAndCount;
	}


	/**
	 * 015001011028
	 * 返回三大目录药品类的对象查询结果 -- 中心目录
	 * @param para
	 * @return
	 */
	public  InsSzybItemAndCount DrugPageQueryCentre(String param, IUser user){
		InsSzybItemAndCount InsSzybItemAndConut = new InsSzybItemAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		if("02".equals(qryparam.getEunmMedicalInsurance())){
			qryparam.setProjectCatalogue("");
		}
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		//中心目录
		List<InsSzybItem> listInsSzybItem = shenzhenCityMapper.qryDrugInsSzybItemInfo(qryparam);
		Page<List<InsSzybItem>> page = MyBatisPage.getPage();
		InsSzybItemAndConut.setInsSzybItem(listInsSzybItem);
		InsSzybItemAndConut.setTotalCount(page.getTotalCount());
		return InsSzybItemAndConut;
	}

	/**
	 * 015001011035
	 * 返回三大目录药品类的对象查询结果--对照目录
	 * @param para
	 * @return
	 */
	public  SzYbDrugContrastInfoAndCount  queryDrugSzybItemMap(String param, IUser user){

		SzYbDrugContrastInfoAndCount szYbDrugContrastInfoAndCount = new SzYbDrugContrastInfoAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
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
	 * 返回三大目录非药品类的对象查询结果--医院目录
	 * @param para
	 * @return
	 */
	public  BdItemAndCount DiagnosisPageQueryHospital(String param, IUser user){
		BdItemAndCount bdItemAndCount = new BdItemAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		String proCata = qryparam.getProjectCatalogue();
		if("2".equals(proCata)){
			qryparam.setProjectCatalogue("01");
		}else if("3".equals(proCata)){
			qryparam.setProjectCatalogue("07");
		}
		//医院目录
		List<Map<String,Object>> bdItemInfoList = shenzhenCityMapper.qryBdItemInfo(qryparam);
		Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
		bdItemAndCount.setBdItemInfoList(bdItemInfoList);
		bdItemAndCount.setTotalCount(page.getTotalCount());
		return bdItemAndCount;
	}


	/**
	 * 015001011030
	 * 返回三大目录非药品类的对象查询结果 -- 中心目录
	 * @param para
	 * @return
	 */
	public  InsSzybItemAndCount DiagnosisQueryCentre(String param, IUser user){
		InsSzybItemAndCount InsSzybItemAndConut = new InsSzybItemAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());

		MyBatisPage.startPage(pageIndex, pageSize);
		//中心目录
		List<InsSzybItem> listInsSzybItem = shenzhenCityMapper.qryNoDrugInsSzybItemInfo(qryparam);
		Page<List<InsSzybItem>> page = MyBatisPage.getPage();
		InsSzybItemAndConut.setInsSzybItem(listInsSzybItem);
		InsSzybItemAndConut.setTotalCount(page.getTotalCount());
		return InsSzybItemAndConut;
	}

	/**
	 * 015001011032
	 * 返回三大目录非药品类的对象查询结果--对照目录
	 * @param para
	 * @return
	 */
	public  SzYbDiagnosisContrastInfoAndCount  queryNoDrugSzybItemMap(String param, IUser user){

		SzYbDiagnosisContrastInfoAndCount szYbDiagnosisContrastInfoAndCount = new SzYbDiagnosisContrastInfoAndCount();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
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
		 * 医保备案上传
		 * @param param
		 * @param user
		 * @return
		 */
		public 	Map<String,Object> upSzybItemMemo(String param, IUser user){
			Map<String,Object> ret = new HashMap<>();//对照项目为深圳医保则调用备案接口
			Map<String, Object> qryparam = JsonUtil.readValue(param,Map.class);
			InsSzybItem listInsSzybItem = shenzhenCityMapper.qrySzybItem(qryparam);
			String ake003 =listInsSzybItem.getAke003();
			String euHpdicttype =listInsSzybItem.getEuHpdicttype();
			BdPd bdPd = null;
			BdItem bdItem = null;
			if("1".equals(ake003)){
				bdPd = shenzhenCityMapper.qryBdPd(qryparam);
				if(bdPd==null){
					throw new BusException("医院项目编码为"+qryparam.get("hosCode")+"备案有误,不属于药品类");
				}
			}else {
				bdItem = shenzhenCityMapper.qryBdItem(qryparam);
				if(bdItem==null){
					throw new BusException("医院项目编码为"+qryparam.get("hosCode")+"备案有误,属于药品类");
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
	 * 查询并保存对照信息到深圳医保目录对照表里
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
			throw new BusException("查询参数有问题！");

		String proClass = (String)qryparam.get("proClass");
		InsSzybItem listInsSzybItem = shenzhenCityMapper.qrySzybItem(qryparam);
		String ake003 =listInsSzybItem.getAke003();
		if("1".equals(ake003)){
			bdPd = shenzhenCityMapper.qryBdPd(qryparam);
			if(bdPd==null){
				throw new BusException("医院项目编码为"+qryparam.get("hosCode")+"对照有误,不属于药品类");
			}
		}else {
			bdItem = shenzhenCityMapper.qryBdItem(qryparam);
			if(bdItem==null){
				throw new BusException("医院项目编码为"+qryparam.get("hosCode")+"对照有误,属于药品类");
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
		bdItemHp.setPkHp(listInsSzybItem.getPkInsitem());//医保主建编码
		bdItemHp.setDtHpdicttype(listInsSzybItem.getAke003());//目录类别
		bdItemHp.setCodeHp(listInsSzybItem.getAke001());//医保编码
		bdItemHp.setEuLevel(listInsSzybItem.getBkm032());//医保等级   --- 取的是收费项目等级
		bdItemHp.setRatioSelf("");//自付比例
		bdItemHp.setNote("");
		bdItemHp.setCreator(user.getUserName());
		bdItemHp.setCreateTime(new Date());
		bdItemHp.setDelFlag("0");
		bdItemHp.setTs(new Date());
		bdItemHp.setHpTime(new Date());
		bdItemHp.setNameHp(listInsSzybItem.getAke002());//医保名称
		retBdItemHp = DataBaseHelper.insertBean(bdItemHp);
		ret.put("retBdItemHp",retBdItemHp);
		return ret ;

	}


	/**
	 * 拼接协议机构ML009 、ML008、ML007备案消息入参                   入参取值还存在问题  待确定
	 * @return
	 * @throws ParseException
	 */
	private Map<String, Object> memoEntity(InsSzybItem listInsSzybItem,BdPd bdPd,BdItem bdItem,Map<String, Object> ret) {
		String ake003 =listInsSzybItem.getAke003();
		if("1".equals(ake003)){//药品
		ret.put("ake001", listInsSzybItem.getAke001());//社保目录编码
			ret.put("bkm017",listInsSzybItem.getBkm017());//药品本位码
			ret.put("aka070",listInsSzybItem.getAka070());//剂型 
			ret.put("aka064",listInsSzybItem.getAka064());//处方药标志
			ret.put("aae013",listInsSzybItem.getAae013());//药品备注
			ret.put("ake005",bdPd.getCode());//	协议机构内部目录编码
			ret.put("ake006",bdPd.getName());//	医药机构内部目录名称
			ret.put("aka074",bdPd.getVol()+bdPd.getPkUnitVol());//	规格
			Map<String, Object>  UnitMap = DataBaseHelper.queryForMap("Select name From bd_unit Where pk_unit = ? ",bdPd.getPkUnitMin());
			if(UnitMap!=null){
				ret.put("aka067",UnitMap.get("name"));//	计价单位
			}else{
				ret.put("aka067","无");
			}
			Map<String, Object>  factoryMap = DataBaseHelper.queryForMap("Select name From bd_factory Where pk_factory = ?",bdPd.getPkFactory());
			if(factoryMap!=null){
				ret.put("bka053",factoryMap.get("name"));//	厂家
			}else{
				ret.put("bka053","无");
			}

//			//判断是否国谈药品
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

			ret.put("bka505",bdPd.getPrice());//	药品进货价格
			ret.put("bka506",bdPd.getPrice());//	药品收费价格
			ret.put("aae030",new SimpleDateFormat("yyyyMMdd").format(new Date()));//	备案日期

		}else if ("2".equals(ake003)) {//诊疗
			ret.put("ake001", listInsSzybItem.getAke001());//社保目录编码
			ret.put("ake005", bdItem.getCode());//协议机构内部目录编码
			ret.put("ake006", bdItem.getName());//协议机构内部目录名称
			ret.put("bka506", bdItem.getPrice());//协议机构内部项目收费价格
			ret.put("bkf131", "1");//协议机构项目类别
			ret.put("bkm062", "0");//门诊特检项目标识
			ret.put("aae030",new SimpleDateFormat("yyyyMMdd").format(new Date()));//	备案日期

		}else if ("3".equals(ake003)||"5".equals(ake003)) {//材料
			ret.put("ake001",listInsSzybItem.getAke001());//	社保目录编码
			ret.put("ake005",bdItem.getCode());//	协议机构内部目录编码
			ret.put("ake006",bdItem.getName());//	医药机构内部目录名称
			Map<String, Object>  factoryMap = DataBaseHelper.queryForMap("Select name From bd_factory Where pk_factory = ?",bdItem.getPkFactory());
			if(factoryMap!=null){
				ret.put("bka053",factoryMap.get("name"));//	生产厂家
			}else{
				ret.put("bka053","无");//	生产厂家
			}
			String ake004 = listInsSzybItem.getAke004();
			if("".equals(ake004)){
				ake004 = "1";
			}
			ret.put("ake004",ake004);//	生产地类别
			ret.put("ckf261","0");	//特殊医用材料标识
			Map<String, Object>  DefDocMap = DataBaseHelper.queryForMap("Select name From bd_unit Where pk_unit = ? ",bdItem.getPkUnit());
			if(DefDocMap!=null){
				ret.put("aka067",DefDocMap.get("name"));//	计价单位
			}else{
				ret.put("aka067","无");//	生产厂家
			}
			ret.put("aka074",bdItem.getSpec());//	规格
			ret.put("bka505",bdItem.getPrice());//	进货价格
			ret.put("bka506",bdItem.getPrice());//	收费价格
			ret.put("aae013",bdItem.getNote());//	备注

		}
		ret.put("ake003", ake003);

		return ret;
	}

	/**
	 * 交易号：015001011068
	 * 备案成功后，修改对照表备案状态和备案时间
	 * @param codeHosp
	 */
	public  void updateInsSzybMapInfo(String param, IUser user){
		Map<String, Object> qryparam = JsonUtil.readValue(param,Map.class);
    	DataBaseHelper.update("update ins_szyb_itemmap set eu_apply ='0' ,date_apply=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss')  where code_hosp = ? and del_flag = '0' ",new Object[] { qryparam.get("status") });
	}

	/**
	 * 交易号：015001011069
	 * 查询医保局备案信息，修改对照表 备案申请状态： 审批时间：办结状态：
	 * @param codeHosp
	 */
	public  int updateMapStatus(String param, IUser user){
		int stu = 0;
		Map<String, Object> qryparam = JsonUtil.readValue(param,Map.class);
    	stu = DataBaseHelper.update("update ins_szyb_itemmap set eu_apply =? ,date_audit=to_date(?,'yyyy/mm/dd'),flag_audit =?  where code_hosp = ? and del_flag = '0' ",new Object[] { qryparam.get("applyStuts"),qryparam.get("memoDate"),qryparam.get("fishStuts"),qryparam.get("code") });
    	return stu ;
	}



	/**
	 * 交易号：015001011070
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
	 * 字符串  转化 int 类型
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
	 * 字符串  转化 Double 类型 的双精度
	 * @param para
	 * @return
	 */
	public static Double stringTranDouble(String para){
		Double str = 0.00;
		if(!"".equals(para) && null!=para){


			BigDecimal temp = BigDecimal.valueOf(Double.valueOf(para));
			// 将temp乘以100
			temp = temp.multiply(BigDecimal.valueOf(100));
			temp = BigDecimal.valueOf(Double.valueOf(Math.floor(temp.doubleValue())));
			temp = temp.divide(BigDecimal.valueOf(100));
			str = temp.doubleValue();

		}
		return str;
	}



	/**
	 * 015001011046 查询医保对照信息
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
	 * 015001011051 修改深圳市医保信息
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
	 * 015001011064  查询需要预结算的病人
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
	 * 015001011065  预结算后数据处理
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
	 * 015001011066  查询医嘱收费项目的医保等信息
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
	 * 015001011067  查询医嘱的医保等信息
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
	 * 交易码：015001011089
	 * 查询医院医保疾病信息字典
	 * @param param
	 * @param user
	 * @return
	 */
	public DiseAndConut disePageHospitalQuery(String param , IUser user){


		DiseAndConut bdItemAndCount = new DiseAndConut();
		PageSzYbQryParam qryparam = JsonUtil.readValue(param,PageSzYbQryParam.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		if(!CommonUtils.isEmptyString(qryparam.getSearchParam())){
			qryparam.setSearchParam(qryparam.getSearchParam().toUpperCase());
		}
		//医院目录
		List<Map<String,Object>> bdItemInfoList = shenzhenCityMapper.disePageHospitalQuery(qryparam);
		Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
		bdItemAndCount.setDiseList(bdItemInfoList);
		bdItemAndCount.setTotalCount(page.getTotalCount());
		return bdItemAndCount;
	}

	/**
	 * 015001011081  查询处方信息
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
	 * 015001011084  查询医嘱的限制用药信息
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
	 * 015001011085  修改医嘱的限制用药信息
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
			//更新最近两天的数据，提高执行效率
			DataBaseHelper.execute("update ins_szyb_order a set a.pk_cnord = (select pk_cnord from cn_order b where a.ordsn = b.ordsn ) where a.pk_cnord is null and a.create_time >= (getdate() - 1)",new Object[] { });			
		}
		else
		{
			//oracle
			//更新最近两天的数据，提高执行效率
			DataBaseHelper.execute("update ins_szyb_order a set a.pk_cnord = (select pk_cnord from cn_order b where a.ordsn = b.ordsn ) where a.pk_cnord is null and a.create_time >= (sysdate - 1)",new Object[] { });		
		}
		return updateCnordList;
	}
	/**
	 * 交易号：015001011087
	 * 删除医保对照相关信息
	 * @param param
	 * @param user
	 */
	public void delSzybItemMapInfo(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null){
			throw new BusException("未得到相关信息！");
		}
		String sql="update ins_szyb_itemmap set del_flag='1' where PK_ITEMMAP=?";
		DataBaseHelper.execute(sql,map.get("pkItemMap"));
	}

	/**
	 * 交易号：015001011090
	 * 查询病人做了几次结算
	 * @param param
	 * @param user
	 */
	public int qrySettleTimes(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null){
			throw new BusException("未得到相关信息！");
		}
		Integer cnt = DataBaseHelper.queryForScalar("select count(1) cnt from ins_szyb_st where del_flag='0' and pk_pv= ?", Integer.class, map.get("pkPv").toString());
	    return cnt;
	}
	/**
	 * 交易号015001011092
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPresClassInfo(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> rtnList= shenzhenCityMapper.queryPresClassInfo(paramMap);
		return rtnList;
	}
	/**查询患者就诊历史
	 * 交易号015001011095
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
	 * 交易号：015001011096
	 * 根据pk_item查询上传费用明细信息
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
	 * 交易号：015001011097
	 * 根据医保结算主键查询医保结算信息
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
	 * 交易号：015001011098
	 * 查询门诊挂号医保登记信息
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
			throw new BusException("就诊登记信息查询错误！");
		}

		//校验医疗证号字段是否有效(加密串),如果是不是加密串格式则搜索患者历史记录，查询是否有有效医疗证号信息
		if(list.get(0)!=null && list.get(0).size()>0){
			if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(list.get(0), "codeMedino"))
			&& CommonUtils.getPropValueStr(list.get(0), "codeMedino").length()<=10){
				//获取患者历史就诊记录的有效医疗证号信息
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
	 * 015001011099 查询门诊医保上传明细
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
	 * 015001011100  根据收费结算-门诊收费明细主键的集合更新bl_op_dt的flag_insu字段
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
		
		//插入表数据INS_SZYB_CITYCG
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
	 *  查询门诊医疗费用总额等相关信息
	 *交易号：
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryOpdtBlStatistical(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> presNoList = shenzhenCityMapper.qryOpdtPresNo(paramMap);
		if((presNoList==null) || (presNoList!=null && presNoList.size()!=1)){
			throw new BusException("查询出【费用明细录入的结算业务序列号】不唯一！");
		}

		Map<String,Object> map = shenzhenCityMapper.qryOpdtBlStatistical(paramMap);
		map.put("presNo", presNoList.get(0).get("presno").toString());
		

		return map;
	}
	
	/**
	 * 交易号：015001011102
	 * 查询医保挂号类型字典
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
	 * 交易号：015001011103
	 * 根据结算主键查询结算明细信息
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
	 * 交易号：015001011104
	 * 根据pkSettle查询结算信息
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
	 * 交易号：015001011106
	 * 查询部分退费时待上传费用明细信息
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
	 * 交易号：015001011105
	 * 根据pkPv修改主医保计划为自费
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
	 * 交易号：015001011109
	 * 修改门诊登记信息
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
			
			//医保计划不为空则更新
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
			throw new BusException("请传入参数");
		}
		if(StringUtils.isBlank(MapUtils.getString(paramMap,"pkPv"))){
			throw new BusException("请传入参数");
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
		//修改pv_encounter
		svList.clear();
		List<String> infoList = Lists.newArrayList();
		//身高
		if(MapUtils.getObject(paramMap,"height")!=null){
			svList.add("height=:height");
			infoList.add("height=:height");
		}
		//体重
		if(MapUtils.getObject(paramMap,"weight")!=null){
			svList.add("weight=:weight");
			infoList.add("weight=:weight");
		}
		//呼吸
		if(MapUtils.getObject(paramMap,"breathe")!=null){
			infoList.add("breathe=:breathe");
		}
		//脉搏
		if(MapUtils.getObject(paramMap,"pulse")!=null){
			infoList.add("pulse=:pulse");
		}
		//体温
		if(MapUtils.getObject(paramMap,"temperature")!=null){
			infoList.add("temperature=:temperature");
		}
		//收缩压/舒张压
		if(MapUtils.getObject(paramMap,"sbp")!=null){
			infoList.add("sbp=:sbp");
		}
		if(MapUtils.getObject(paramMap,"dbp")!=null){
			infoList.add("dbp=:dbp");
		}
		//心率
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
		//PlatFormSendUtils.sendPatientSiMsg(map);//发送体征消息ZPI^PSI
		PlatFormSendUtils.sendVitalSigns(paramMap);*/
	}
	
	/**
	 * 交易号：015001011110
	 * 使用通道进行挂号时调用此接口保存医保登记关联关系
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

			//1.线上预结算挂号时需要把患者以往就诊的大病类别、门诊慢病类别、孕周、计生证号、建册日期更新到本次就诊信息中
			//2.上预结算挂号时需要更新患者医疗证号
			if(paramMap.containsKey("flagSelfMac") && !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"flagSelfMac"))){
				//获取本地的医保登记详细信息
				InsSzybVisit visit = DataBaseHelper.queryForBean(
						"select * from INS_SZYB_VISIT where PK_VISIT = ?"
						,InsSzybVisit.class,new Object[]{paramMap.get("pkVisit")});

				//获取本次就诊医保登记详细信息
				InsSzybVisitCity cityVo = DataBaseHelper.queryForBean(
						"select * from INS_SZYB_VISIT_city where pk_visit = ?"
						,InsSzybVisitCity.class,new Object[]{paramMap.get("pkVisit")});

				paramMap.put("pkPi",visit.getPkPi());

				//更新患者医疗证号信息
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

					//更新患者登记信息
					DataBaseHelper.updateBeanByPk(cityVo,false);
				}
			}
		}
	}

	/**
	 * 交易号：015001011111
	 * 更新费用明细医保上传标志
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
	 * 交易号：015001011113
	 * 查询门诊挂号医保登记信息(首次登记的医保信息)
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
			throw new BusException("就诊登记信息查询错误！");
		}

		return list.get(list.size()-1);
	}
	/**
	 * 015001011112 返回就诊登记信息集合
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryVisitsOp(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> list = shenzhenCityMapper.qryOpVisitInfo(paramMap);
		if (list == null || list.size() <= 0) {
			throw new BusException("就诊登记信息查询错误！");
		}else{
			//更新患者医疗证号信息
			if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(list.get(0),"codeMedino"))
					&& CommonUtils.getPropValueStr(list.get(0),"codeMedino").length()<15){
				paramMap.put("pkPi",list.get(0).get("pkPi"));
				InsSzybVisit upVisit = shenzhenCityMapper.qryVisitInfoByPkPi(paramMap);
				if(upVisit!=null){
					//获取本地的医保登记详细信息
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
	 * 交易号：015001011114
	 * 校验多通道结算时是否需要自动挂号
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
			//校验本次结算使用的医疗类别和挂号登记的医疗类别是否一致，如果一致则不需要挂号
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
			
			//如果和登记时医疗类别不一致，则校验本次选择的医疗类别是否需要挂0元号
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
	 * 交易号：015001011115
	 * 如果选择的是生育的医疗类别，查询改患者数据库之前是否已经有存入孕周、计生证号、建册日期信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryBirthInsuRegInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		List<Map<String,Object>> retMap = new ArrayList<>();
		if(paramMap!=null && paramMap.size()>0 
				&& paramMap.containsKey("idNo") && !CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("idNo")))){
			
			//根据身份证号查询最近登记的生育信息
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
	 * 交易号：015001011116
	 * 更新患者医保表中的大病类别字段
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
	 * 交易号：015001011118
	 * 根据记费主键查询医嘱0205扩展属性
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
			//查询扩展属性信息
			rtnList = shenzhenCityMapper.qryOrdAttrByPkCgop(pkList);
		}

		return rtnList;
	}

	/**
	 * 交易号：015001011119
	 * 检索特检类别并更新到患者登记信息表
	 * @param param
	 * @param user
	 */
	public void upVisitSpSurv(String param , IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap!=null && paramMap.size()>0
				&& !CommonUtils.isEmptyString(CommonUtils.getPropValueStr(paramMap,"pkPv"))){
			//查询该患者是否有特检医嘱
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
	 * 交易号：015001011120
	 * 根据医疗证号查询患者门诊号
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
	 * 交易号：015001011121
	 * 根据医保结算序列号查询HIS对应结算信息
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
	 * 交易号：015001011122
	 * 保存全国医保登记信息
	 * @param param
	 * @param user
	 * @return
	 */
	public String saveQgybOpVisit(String param , IUser user){
		User u = (User) user;
		InsQgybVisit visitVo= JsonUtil.readValue(JsonUtil.getJsonNode(param, "insQgybVisit"), InsQgybVisit.class);

		// 医保登记  1
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
					//软删除医保登记信息
					DataBaseHelper.execute("update ins_qgyb_visit set DEL_FLAG = '1' where pk_pv = ?",new Object[]{visitVo.getPkPv()});
					DataBaseHelper.execute("update ins_qgyb_pv set DEL_FLAG = '1' where pk_pv = ?",new Object[]{visitVo.getPkPv()});
				}else{
					//修改医保住院登记信息
					String visitJsonNode=JsonUtil.getJsonNode(param, "insQgybVisit").toString();
					InsQgybPv newYbPvVo= JsonUtil.readValue(JsonUtil.getJsonNode(visitJsonNode, "insQgybPv"), InsQgybPv.class);

					//查询原医保登记信息
					InsQgybPv oldYbPvInfo = DataBaseHelper.queryForBean(
							"select * from ( select * from ins_qgyb_pv where MDTRT_ID = ? and del_flag = '0' order by CREATE_TIME desc ) where rownum = 1",
							InsQgybPv.class,new Object[]{visitVo.getMdtrtId()});

					if(oldYbPvInfo!=null){
						//修改
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
						//新增
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
	 * 交易号：015001011123
	 *保存全国医保登记表关联关系
	 * @param param
	 * @param user
	 */
	public void saveHisYbRelationship(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String pkPv = paramMap.get("pkPv").toString();

		String pkSettle = null;
		String ywlx = null;//业务类型
		String pkInsst = null;//医保结算主键
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
		//挂号情况，保存患者就诊信息到医保结算表
		if(!CommonUtils.isEmptyString(ywlx) && "0".equals(ywlx)){
			if(paramMap.containsKey("pkVisit") && paramMap.get("pkVisit")!=null){
				pkVisit = CommonUtils.getString(paramMap.get("pkVisit"));
			}

			BlSettle stInfo = new BlSettle();
			if(!CommonUtils.isEmptyString(pkSettle)){
				//查询结算信息
				stInfo = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle = ?", BlSettle.class, new Object[]{pkSettle});
			}

			//查询患者就诊信息
			PvEncounter pvInfo = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where pk_pv = ?", PvEncounter.class, new Object[]{pkPv});
			//查询患者基本信息
			PiMaster master = DataBaseHelper.queryForBean("select * from pi_master where pk_pi = ?", PiMaster.class, new Object[]{pvInfo.getPkPi()});

			//查询医保登记信息
			InsQgybVisit visitInfo = DataBaseHelper.queryForBean("select * from ins_qgyb_visit where pk_visit = ?", InsQgybVisit.class, new Object[]{pkVisit});
			visitInfo.setPkHp(pvInfo.getPkInsu());
			visitInfo.setPkPv(pvInfo.getPkPv());
			visitInfo.setPkPi(pvInfo.getPkPi());
			visitInfo.setNamePi(pvInfo.getNamePi());
			visitInfo.setIdno(master.getIdNo());
			DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybVisit.class), visitInfo);

			//查询医保结算信息
			if(!CommonUtils.isEmptyString(pkInsst)){
				InsQgybSt insuStInfo = DataBaseHelper.queryForBean("select * from ins_qgyb_st where setl_id = ?", InsQgybSt.class, new Object[]{pkInsst});
				insuStInfo.setPkHp(pvInfo.getPkInsu());
				insuStInfo.setPkPv(pvInfo.getPkPv());
				insuStInfo.setPkPi(pvInfo.getPkPi());
				insuStInfo.setPkSettle(pkSettle);
				insuStInfo.setDateSt(stInfo!=null && stInfo.getDateSt()!=null?stInfo.getDateSt():new Date());
				DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybSt.class), insuStInfo);
			}
		}else if(!CommonUtils.isEmptyString(ywlx) && "1".equals(ywlx)){//门诊结算
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
	 * 交易号：015001011124
	 * 查询门诊挂号医保登记信息
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
	 * 015001011125 通过医保登记主键修改HIS就诊登记就诊主键、患者主键
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

			//查询医保登记关联的就诊信息
			InsQgybPv visitPvInfo = DataBaseHelper.queryForBean(
					"select * from ins_qgyb_pv where mdtrt_id = ?"
					,InsQgybPv.class,new Object[]{visitInfo.getMdtrtId()});

			if(visitPvInfo!=null && !CommonUtils.isEmptyString(visitPvInfo.getPkInspv())){
				visitPvInfo.setPkPi(visitInfo.getPkPi());
				visitPvInfo.setPkPv(visitInfo.getPkPv());

				DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybPv.class),visitPvInfo);
			}

		} else {
			throw new BusException("传入参数有空值");
		}
	}

	/**
	 * 交易号：015001011126
	 * 查询全国医保登记信息
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
	 * 交易号：015001011127
	 * 查询医保就诊信息，只查询未参与结算的就诊记录
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
	 * 交易号：015001011128
	 * 更新医保就诊信息
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
	 * 交易号：015001011129
	 *查询全国医保结算信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryInsuStInfo(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);

		Map<String,Object> rtnMap = new HashMap<>(16);

		//查询医保结算信息
		InsQgybSt ybstInfo = DataBaseHelper.queryForBean(
				"select * from ins_qgyb_st where PK_SETTLE = ? order by CREATE_TIME desc "
				,InsQgybSt.class,new Object[]{paramMap.get("pkSettle")});

		//查询医保结算关联的就诊信息
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
	 * 交易号：015001011130
	 * 查询患者历史医保就诊信息
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
	 * 015001011131  查询医嘱的限制用药信息
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
	 * 交易号：015001011132
	 * 保存选择的限制用药信息
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
	 * 交易号：015001011133
	 * 查询全国医保登记信息
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
