package com.zebone.nhis.ex.pivas.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimaps;
import com.google.common.collect.UnmodifiableIterator;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdPivas;
import com.zebone.nhis.common.module.ex.pivas.conf.BdPivasBatch;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pivas.dao.PivasOperMapper;
import com.zebone.nhis.ex.pivas.vo.ExPdApplyDetailVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class PivasOperService {

	@Resource
	private PivasOperMapper pivasOperMapper;
	
	/**
	 * 查询待复核药品信息
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public List<Map<String, Object>> queryWaitToBeAudittedPdInfos(String param, IUser user) throws ParseException{
		Map paramMap = JsonUtil.readValue(param, Map.class);
		//当前部门
		String pkDeptAdmix = ((User)user).getPkDept();
		paramMap.put("pkDeptAdmix", pkDeptAdmix);
		//前台传的日期字符串，格式："yyyyMMddHHmmss"
		String timeOccBegin = paramMap.get("timeOccBegin").toString();
		String timeOccEnd = paramMap.get("timeOccEnd").toString();
		Date timeoccbegin = DateUtils.getDefaultDateFormat().parse(timeOccBegin);
		Date timeoccend = DateUtils.getDefaultDateFormat().parse(timeOccEnd);
		paramMap.put("timeoccbegin", timeoccbegin);
		paramMap.put("timeoccend", timeoccend);
		List<Map<String, Object>> pdInfoList = this.pivasOperMapper.queryWaitToBeAudittedPdInfos(paramMap);
		return pdInfoList;
	}
	
	/**
	 * 复核处理
	 * 
	 * @param param
	 * @param user
	 */
	public void reAudit(String param, IUser user) {
		List<String> pkPdpivaList = JsonUtil.readValue(param, new TypeReference<List<String>>() {
		});
		String pkEmp = ((User) user).getPkEmp();
		String nameEmp = ((User) user).getNameEmp();
		
		if(pkPdpivaList!=null && pkPdpivaList.size()!=0){
			for(String pkPdpivas : pkPdpivaList){
				DataBaseHelper.update(
						"update ex_pd_pivas set eu_status = 8, flag_chk = 1, date_chk = ?, pk_emp_chk = ?, name_emp_chk = ? where pk_pdpivas = ?",
						new Object[] {new Date(), pkEmp, nameEmp, pkPdpivas });
			}
		}
	}

	/**
	 * 查询签收药品<br>
	 * 交易号：005003002014
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public List<Map<String, Object>> queryToBeConfirmedPdInfos(String param, IUser user) throws ParseException{
		Map paramMap = JsonUtil.readValue(param, Map.class);
		//当前病区(bd_ou_dept，dt_depttype=02的就是病区)
		String pkDeptApply = ((User)user).getPkDept();
		paramMap.put("pkDeptApply", pkDeptApply);
		//前台传的日期字符串，格式："yyyyMMddHHmmss"
		/**1.用药开始时间*/
		Date timeoccbegin = null;
		if(paramMap.get("timeOccBegin")!=null){
			String timeOccBegin = paramMap.get("timeOccBegin").toString();
			timeoccbegin = DateUtils.getDefaultDateFormat().parse(timeOccBegin);
		}
		paramMap.put("timeoccbegin", timeoccbegin);
		/**2.用药结束时间*/
		Date timeoccend = null;
		if(paramMap.get("timeOccEnd")!=null){
			String timeOccEnd = paramMap.get("timeOccEnd").toString();
			timeoccend = DateUtils.getDefaultDateFormat().parse(timeOccEnd);
		}
		paramMap.put("timeoccend", timeoccend);
		/**3.签收开始时间*/
		Date datesignbegin = null;
		if(paramMap.get("dateSignBegin")!=null){
			String dateSignBegin = paramMap.get("dateSignBegin").toString();
			datesignbegin = DateUtils.getDefaultDateFormat().parse(dateSignBegin);
		}
		paramMap.put("datesignbegin", datesignbegin);
		/**4.签收结束时间*/
		Date datesignend = null;
		if(paramMap.get("dateSignEnd")!=null){
			String dateSignEnd = paramMap.get("dateSignEnd").toString();
			datesignend = DateUtils.getDefaultDateFormat().parse(dateSignEnd);
		}
		paramMap.put("datesignend", datesignend);
		List<Map<String, Object>> pdInfoList = this.pivasOperMapper.queryToBeConfirmedPdInfos(paramMap);
		return pdInfoList;
	}
	
	/**
	 * 查询领药单明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExPdApplyDetailVo> queryPdapDt(String param, IUser user){
		Map paramMap = JsonUtil.readValue(param, Map.class);
		return pivasOperMapper.queryPdapDt(paramMap);
	}
	
	/**
	 * 查询静配记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPivas (String param, IUser user){
		User u = (User) user;
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkDeptAdmix", u.getPkDept());
		paramMap.put("curDate", DateUtils.getDateStr(new Date()));
		paramMap.put("nextDate", DateUtils.getDateStr(DateUtils.getSpecifiedDay(new Date(), 1)));
		return pivasOperMapper.queryPivas(paramMap);
	}
	
	/**
	 * 签收处理
	 * 
	 * @param param
	 * @param user
	 */
	public void confirm(String param, IUser user) {
		List<String> pkPdpivaList = JsonUtil.readValue(param, new TypeReference<List<String>>() {
		});
		String pkEmp = ((User) user).getPkEmp();
		String nameEmp = ((User) user).getNameEmp();
		if(pkPdpivaList!=null && pkPdpivaList.size()!=0){
			for(String pkPdpivas : pkPdpivaList){
				DataBaseHelper.update(
						"update ex_pd_pivas set eu_status = 9, flag_sign = 1, date_sign = ?, pk_emp_sign = ?, name_emp_sign = ? where pk_pdpivas = ?",
						new Object[] {new Date(), pkEmp, nameEmp, pkPdpivas });
			}
		}
	}

	/**
	 * 自动审核
	 * 
	 * @param param
	 * @param user
	 */
	public void autoCheck(String param, IUser user) {
		String pkCnord = JSON.parseObject(param).getString("pkCnord");

	}

	/**
	 * 人工审核
	 * 
	 * @param param
	 * @param user
	 */
	public void manualCheck(String param, IUser user) {
		User u = (User) user;
		List<Map<String, String>> paramMap = JsonUtil.readValue(param, List.class);
		for (Map<String, String> map : paramMap) {
			String pkCnord = map.get("pkCnord");
			String dtMedcheckitem = map.get("dtMedcheckitem");
			String euResultManu = map.get("euResultManu");
			String descManu = map.get("descManu");
			
			//住院医嘱发药，会一次发完
			Integer count_finish = DataBaseHelper.queryForScalar("select count(1) from ex_pd_apply_detail where flag_finish = '1' and eu_direct = '1' and pk_cnord = ?", Integer.class, new Object[]{pkCnord});
			if(count_finish != null && count_finish > 0){
				throw new BusException("该记录已发药，请选择未发药记录进行审核！");
			}
			
			
			int count = DataBaseHelper.queryForScalar("select count(*) from EX_PD_CHECK where pk_cnord = ?", Integer.class,
					new Object[] { pkCnord });
			if (count > 0) {
				Map<String, Object> mapUpdate = new HashMap<String, Object>();				
				mapUpdate.put("pkCnord", pkCnord);
				mapUpdate.put("pkDept", u.getPkDept());
				mapUpdate.put("pkEmp", u.getPkEmp());
				mapUpdate.put("nameEmp", u.getNameEmp());		
				StringBuffer sqlBuffer = new StringBuffer("update EX_PD_CHECK set ");
				if(StringUtils.isNotBlank(dtMedcheckitem)){
					mapUpdate.put("dtMedcheckitem", dtMedcheckitem);
					sqlBuffer.append("dt_medcheckitem =:dtMedcheckitem, ");
				}
				if(StringUtils.isNotBlank(euResultManu)){
					mapUpdate.put("euResultManu", euResultManu);
					sqlBuffer.append("eu_result_manu =:euResultManu, ");
				}
				if(StringUtils.isNotBlank(descManu)){
					mapUpdate.put("descManu", descManu);
					sqlBuffer.append("desc_manu =:descManu, ");
				}
				mapUpdate.put("nowTime", new Date());
				sqlBuffer.append("pk_dept_chk =:pkDept, date_chk =:nowTime, pk_emp_chk =:pkEmp, name_emp_chk =:nameEmp where pk_cnord =:pkCnord");
				
				DataBaseHelper.update(sqlBuffer.toString(), mapUpdate);
			} else {
				String pkPdchk = NHISUUID.getKeyId();
				DataBaseHelper.execute(
						"insert into EX_PD_CHECK(pk_pdchk,pk_org,pk_cnord,dt_medcheckitem,eu_result_manu,desc_manu,pk_dept_chk,date_chk,pk_emp_chk,name_emp_chk) values(?,?,?,?,?,?,?,?,?,?)",
						new Object[] { pkPdchk, u.getPkOrg(), pkCnord, dtMedcheckitem, euResultManu, descManu,
								u.getPkDept(), new Date(), u.getPkEmp(), u.getNameEmp() });
			}
		}
		
	}

	/**
	 * 医嘱停发(用药审核)
	 * 
	 * @param param
	 * @param user
	 */
	public void ordStop(String param, IUser user) {
		User u = (User) user;
		List<String> pkCnords = JSON.parseArray(param, String.class);
		StringBuilder sbd = new StringBuilder("pk_cnord in (");
		for (int i = 0; i < pkCnords.size(); i++) {
			if(i == 0){
				sbd.append("'").append(pkCnords.get(i)).append("'");
			}else{
				sbd.append(",'").append(pkCnords.get(i)).append("'");
			}
			
			//住院医嘱发药，会一次发完
			Integer count_finish = DataBaseHelper.queryForScalar("select count(1) from ex_pd_apply_detail where flag_finish = '1' and eu_direct = '1' and pk_cnord = ?", Integer.class, new Object[]{pkCnords.get(i)});
			if(count_finish != null && count_finish > 0){
				throw new BusException("该记录已发药，无法停发！");
			}
		}
		sbd.append(")");
		String pkCnordWhere = sbd.toString();
		int cnt = DataBaseHelper.queryForScalar("select count(*) from ex_pd_apply_detail where "
				+ pkCnordWhere, Integer.class, new Object[] {});
		if(cnt == 0){
			throw new BusException("医嘱无请领单！");
		}
		cnt = DataBaseHelper.queryForScalar("select count(*) from ex_pd_apply_detail where flag_stop = 0 "
				+ "and " + pkCnordWhere, Integer.class, new Object[] {});
		if(cnt == 0){
			throw new BusException("医嘱已停发！");
		}
		DataBaseHelper.update(
				"update ex_pd_apply_detail set flag_stop = 1, reason_stop = '用药审核不通过', "
				+ "pk_emp_stop = ?, name_emp_stop = ? where " + pkCnordWhere,
				new Object[] { u.getPkEmp(), u.getNameEmp() });
	}
	
	
	/**
	 * 医嘱停发(瓶签排批)
	 * 
	 * @param param
	 * @param user
	 */
	public void ordStop1(String param, IUser user) {
		User u = (User) user;
		List<String> pkCnords = JSON.parseArray(param, String.class);
		StringBuilder sbd = new StringBuilder("pk_cnord in (");
		for (int i = 0; i < pkCnords.size(); i++) {
			if(i == 0){
				sbd.append("'").append(pkCnords.get(i)).append("'");
			}else{
				sbd.append(",'").append(pkCnords.get(i)).append("'");
			}
		}
		sbd.append(")");
		String pkCnordWhere = sbd.toString();
		CnOrder cnOrder = DataBaseHelper.queryForBean("select * from cn_order where " + pkCnordWhere, CnOrder.class);
		String reasonStop = "";
		if("4".equals(cnOrder.getEuStatusOrd())){//停止
			reasonStop = "医嘱已停止";
		}
		if("9".equals(cnOrder.getEuStatusOrd())){//取消
			reasonStop = "医嘱已取消";
		}
		DataBaseHelper.update(
				"update ex_pd_apply_detail set flag_stop = '1', reason_stop = ?, "
						+ "pk_emp_stop = ?, name_emp_stop = ? where " + pkCnordWhere,
						new Object[] { reasonStop, u.getPkEmp(), u.getNameEmp() });
	}
	
	
	/**
	 * 生成瓶签信息
	 * @param param
	 * @param user
	 */
	public void generatePivas(String param, IUser user){
		User u = (User)user;
		List<ExPdApplyDetailVo> applyDetails = JsonUtil.readValue(param,new TypeReference<List<ExPdApplyDetailVo>>() {});
		/**list分组*/
		//根据ordsnParent 对list进行分组
		ImmutableList<ExPdApplyDetailVo> digits= ImmutableList.copyOf(applyDetails);
		//分组方法，ordsnParent及执行时间 一致的为一组
		Function<ExPdApplyDetailVo, String> group = new Function<ExPdApplyDetailVo, String>(){
			@Override
			public String apply(ExPdApplyDetailVo vo) {
				return vo.getOrdsnParent() + ":" + new DateTime(vo.getDatePlan()).toString("yyyyMMddHHmmss");
			}
		};
		//执行分组方法
		ImmutableListMultimap<String, ExPdApplyDetailVo> groupMap = Multimaps.index(digits,group);
		ImmutableSet<String> keys = groupMap.keySet();
		UnmodifiableIterator<String> it = keys.iterator();
		while(it.hasNext()){
			String key = it.next();
//			String[] arr = key.split(":");
//			String ordsnParent = arr[0];
//			String datePlanStr = arr[1];
			ImmutableList<ExPdApplyDetailVo> list = groupMap.get(key);
			String barcode = NHISUUID.getKeyId();
			for (ExPdApplyDetailVo applyDetail : list) {
				ExPdPivas exPdPivas = new ExPdPivas();
				exPdPivas.setPkPdpivas(NHISUUID.getKeyId());
				exPdPivas.setBarcode(barcode);
				exPdPivas.setPkOrg(u.getPkOrg());
				exPdPivas.setPkPv(applyDetail.getPkPv());
				exPdPivas.setPkPi(applyDetail.getPkPi());
				exPdPivas.setPkPdapdt(applyDetail.getPkPdapdt());
				exPdPivas.setPkExocc(applyDetail.getPkExocc());
				exPdPivas.setPkCnord(applyDetail.getPkCnord());
				exPdPivas.setEuAlways(applyDetail.getEuAlways());
				exPdPivas.setNameOrd(applyDetail.getNameOrd());
				exPdPivas.setSpec(applyDetail.getSpec());
				exPdPivas.setOrdsn(applyDetail.getOrdsn());
				exPdPivas.setOrdsnParent(applyDetail.getOrdsnParent());
				exPdPivas.setCodeFreq(applyDetail.getCodeFreq());
				exPdPivas.setDosage(applyDetail.getDosage());
				exPdPivas.setPkUnitDos(applyDetail.getPkUnitDos());
				exPdPivas.setDripSpeed(applyDetail.getDripSpeed());
				exPdPivas.setPkPd(applyDetail.getPkPd());
				exPdPivas.setPkUnit(applyDetail.getPkUnit());
				exPdPivas.setPackSize(applyDetail.getPackSize());
				exPdPivas.setQuanPack(applyDetail.getQuanPack());
				exPdPivas.setQuanMin(applyDetail.getQuanMin());
				exPdPivas.setPkDeptApply(applyDetail.getPkDeptAp());
				exPdPivas.setPkDeptAdmix(u.getPkDept());
				
				exPdPivas.setCodeSupply(applyDetail.getCodeSupply());
				//静配分类
				if(StringUtils.isBlank(applyDetail.getPkPivascate())){
					for (ExPdApplyDetailVo vo : list) {
						if(StringUtils.isNotBlank(vo.getPkPivascate())){
							exPdPivas.setPkPivascate(vo.getPkPivascate());
							break;
						}
					}
				}else{
					exPdPivas.setPkPivascate(applyDetail.getPkPivascate());
				}
				//执行时间
				exPdPivas.setTimePlan(applyDetail.getDatePlan());
				//用药时间
				Date timeOcc = null ;
				if(applyDetail.getSortIv().intValue() == 1){
					timeOcc = applyDetail.getDatePlan();
				}else{
					int cnt = Integer.parseInt(ApplicationUtils.getSysparam("EX0004", true));//每毫升滴数
					int i = 0;
					for (ExPdApplyDetailVo vo : groupMap.get(key)) {
						if(vo.getSortIv() == 1 || i == 0){
							timeOcc = vo.getDatePlan();
						}else{
							//int duration = (int) (vo.getDosage()/(vo.getDripSpeed()/cnt));
							int duration = 0;
							timeOcc = new DateTime(timeOcc).plusMinutes(duration).toDate();
							if(applyDetail.getSortIv().intValue() == vo.getSortIv().intValue()){
								break;
							}
						}
						i++;
					}
				}
				exPdPivas.setTimeOcc(timeOcc);
				//获取配药批次
				String timeOccStr = new DateTime(timeOcc).toString("HH:mm:ss");
				BdPivasBatch bdPivasBatch = DataBaseHelper.queryForBean("select * from bd_pivas_batch where del_flag = '0' and eu_type = '0' and pk_dept = ? and time_begin <= ? and time_end > ?", 
						BdPivasBatch.class, applyDetail.getPkDept(),timeOccStr,timeOccStr);
				//获取打包批次
				if(bdPivasBatch == null){
					bdPivasBatch = DataBaseHelper.queryForBean("select code from bd_pivas_batch where del_flag = '0' and eu_type = '1' and pk_dept = ?", BdPivasBatch.class, applyDetail.getPkDept());
					if(bdPivasBatch == null){
						throw new BusException("当前科室配液批次设置有误，请检查!");
					}
				}
				exPdPivas.setCodeBatch(bdPivasBatch.getCode());
				exPdPivas.setEuStatus("0");
				exPdPivas.setFlagPrt("0");
				exPdPivas.setFlagStop("0");
				exPdPivas.setFlagPack("0");
				exPdPivas.setFlagIn("0");
				exPdPivas.setFlagAdmix("0");
				exPdPivas.setFlagOut("0");
				exPdPivas.setFlagChk("0");
				exPdPivas.setFlagSign("0");
				DataBaseHelper.insertBean(exPdPivas);
			}
		}
	}
	
	/**
	 * 修改静配批次
	 * @param param
	 * @param user
	 */
	public void updateCodeBatch(String param, IUser user){
		Map paramMap = JsonUtil.readValue(param, Map.class);
		pivasOperMapper.updatePivas(paramMap);
	}

}
