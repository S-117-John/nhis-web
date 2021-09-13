package com.zebone.nhis.compay.ins.lb.service.szyb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.compay.ins.lb.szyb.*;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.compay.ins.lb.vo.szyb.StReceVo;
import com.zebone.nhis.pro.zsba.common.support.Pinyin4jUtils;
import com.zebone.platform.common.support.UserContext;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.compay.ins.lb.dao.szyb.InsSzybMapper;
import com.zebone.nhis.compay.ins.lb.vo.szyb.ParamLoginInSave;
import com.zebone.nhis.compay.ins.lb.vo.szyb.SaveRegisterInfoParam;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 宿州市医保服务接口
 * 
 * @author yangxue
 * 
 */
@Service
public class InsSzybService {
	@Resource
	private InsSzybMapper insSzybMapper;

	/**
	 * 保存费用结算返回数据 015001005001
	 * 
	 * @param param
	 * @param user
	 * @throws ParseException
	 */
	public InsSzybJs saveSettlementReturnData(String param, IUser user) {
		InsSzybJs szybjs = JsonUtil.readValue(param, InsSzybJs.class);
		if (szybjs != null) {
			ApplicationUtils.setDefaultValue(szybjs, true);
			DataBaseHelper.insertBean(szybjs);
		}
		return szybjs;
	}

	/**
	 * 查询宿州市医保结算信息
	 * 
	 * @param param
	 *            {ywzqh:业务周期号}
	 * @param user
	 * @return
	 */
	public List<InsSzybJs> queryInsSzybJsDetail(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			throw new BusException("未获取到查询宿州市医保结算信息所需的任何查询条件！");
		paramMap.put("pkOrg", ((User) user).getPkOrg());
		return insSzybMapper.querySzybJsList(paramMap);
	}

	/**
	 * 根据业务周期号查询宿州市医保结算汇总信息
	 * 
	 * @param param
	 *            {ywzqh:业务周期号}
	 * @param user
	 * @return
	 */
	public Map<String, Object> queryInsSzybJsSummary(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			throw new BusException("未获取到查询宿州市医保结算汇总信息所需的任何查询条件！");
		paramMap.put("pkOrg", ((User) user).getPkOrg());
		return insSzybMapper.querySzybJsSummary(paramMap);
	}

	/**
	 * 保存宿州市医保处方明细分解数据
	 * 
	 * @param param
	 * @param user
	 * @throws ParseException
	 */
	public List<InsSzybFymx> saveCostAnalysisDetail(String param, IUser user) {
		List<InsSzybFymx> fymxList = JsonUtil.readValue(param,
				new TypeReference<List<InsSzybFymx>>() {
				});

		for (InsSzybFymx fymx : fymxList) {
			ApplicationUtils.setDefaultValue(fymx, true);
		}
		DataBaseHelper.batchUpdate(
				DataBaseHelper.getInsertSql(InsSzybFymx.class), fymxList);

		// for (InsSzybFymx fymx : fymxList) {
		// DataBaseHelper.insertBean(fymx);
		// }
		return fymxList;
	}

	/**
	 * 医保签到记录保存 020001002011 变更为015001005004
	 * 
	 * @param param
	 * @param user
	 * @throws ParseException
	 */

	public void saveLoginInfo(String param, IUser user) throws ParseException {
		ParamLoginInSave pmiInfo = JsonUtil.readValue(param,
				ParamLoginInSave.class);
		InsSzybLoginrecord insSzybLoginrecord = new InsSzybLoginrecord();
		if (pmiInfo != null) {
			if ("1".equals(pmiInfo.getQdzt())) {
				String sqls = "select count(1) from ins_szyb_loginrecord where del_flag='0' and ry=? and qdywzqh=?";
				int res = DataBaseHelper
						.queryForScalar(sqls, Integer.class, new Object[] {
								pmiInfo.getQdr(), pmiInfo.getQdywzqh() });
				if (res > 0)
					return;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 小写的mm表示的是分钟
				String dstr = pmiInfo.getQdsj();
				java.util.Date date = sdf.parse(dstr);
				insSzybLoginrecord.setId(NHISUUID.getKeyId());
				insSzybLoginrecord.setQdsj(date);
				insSzybLoginrecord.setRy(pmiInfo.getQdr());
				insSzybLoginrecord.setQdyblx(pmiInfo.getYblx());
				insSzybLoginrecord.setQdywzqh(pmiInfo.getQdywzqh());
				insSzybLoginrecord.setQdzt(pmiInfo.getQdzt());
				DataBaseHelper.insertBean(insSzybLoginrecord);
			} else if ("2".equals(pmiInfo.getQdzt())) {// 签退
				pmiInfo.setQtsj(DateUtils.dateToStr("yyyyMMddHHmmss",
						new Date()));
				insSzybMapper.updateLoginrecord(pmiInfo);
			}
		}
	}

	/**
	 * 015001005022 查询签到信息
	 * 
	 * @param param
	 *            {"qdr":"签到人","beginDate":"开始时间","endDate":"结束时间"}
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qrySzybLoginreCord(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || paramMap.size() <= 0)
			return null;
		String beginDate = paramMap.get("beginDate").toString().substring(0, 8)
				+ "000000";
		String endDate = paramMap.get("endDate").toString().substring(0, 8)
				+ "235959";
		paramMap.put("beginDate", beginDate);
		paramMap.put("endDate", endDate);
		return insSzybMapper.qryLoginreCord(paramMap);
	}

	/**
	 * 保存门诊住院登记信息数据 020001002012 变更为 015001005005
	 * 
	 * @param param
	 * @param user
	 * @throws ParseException
	 */
	public String saveRegisterInfo(String param, IUser user) {
		SaveRegisterInfoParam saveRegisterInfoParam = JsonUtil.readValue(param,
				SaveRegisterInfoParam.class);
		InsSzybMzdj insSzybMzdj = new InsSzybMzdj();
		//判断读卡方式是否为空
		if(StringUtils.isBlank(saveRegisterInfoParam.getDisType())) {
			insSzybMzdj.setDisType(saveRegisterInfoParam.getDisType());
		}	
		if (saveRegisterInfoParam != null) {

			if (StringUtils.isBlank(saveRegisterInfoParam.getId())) {
				ApplicationUtils.copyProperties(insSzybMzdj,
						saveRegisterInfoParam);
				ApplicationUtils.setDefaultValue(insSzybMzdj, true);
				insSzybMzdj.setId(NHISUUID.getKeyId());
				insSzybMzdj.setRydsfzdbm4(saveRegisterInfoParam
						.getRydsfzdbm_four());
				insSzybMzdj.setRydwfzdbm5(saveRegisterInfoParam.getRydwfzdbm());
				insSzybMzdj.setFsflsh(saveRegisterInfoParam.getFsflsh());
				DataBaseHelper.insertBean(insSzybMzdj);
			} else {
				ApplicationUtils.copyProperties(insSzybMzdj,
						saveRegisterInfoParam);
				DataBaseHelper.updateBeanByPk(insSzybMzdj, false);
			}
		}
		return insSzybMzdj.getId();
	}

	/**
	 * 
	 * 查询门诊或住院登记数据
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public InsSzybMzdj qrySzybMzdj(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			return null;
		String sql = "select * from INS_SZYB_MZDJ where del_flag='0' and pk_pv=?";
		InsSzybMzdj ism = DataBaseHelper.queryForBean(sql, InsSzybMzdj.class,
				new Object[] { paramMap.get("pkPv") });

		if (ism == null) { // 通过pkpv未查询到
			String sql1 = "select * from INS_SZYB_MZDJ where del_flag='0' and LSH=?";
			InsSzybMzdj ismEntity = DataBaseHelper.queryForBean(sql1,
					InsSzybMzdj.class, new Object[] { paramMap.get("pkPv") });
			return ismEntity;
		} else {
			return ism;
		}
	}

	/**
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryInsSzybMzds(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			throw new BusException("相关信息未获得！");
		//String pkHp = paramMap.get("pkHp").toString();
		//List<String> pkItems = (List<String>) paramMap.get("pkItems");
		//Set<String> str = new HashSet<String>(pkItems);

//		String sql = "select * from INS_SZYB_ITEM_MAP where del_flag = '0' and pk_hp='"
//				+ pkHp
//				+ "' and pk_item in ("
//				+ CommonUtils.convertSetToSqlInPart(str, "pk_item") + ")";
		List<Map<String, Object>> resList = insSzybMapper.qryItemMapInfo(paramMap);
		return resList;
	}

	/**
	 * 015001005021 根据业务流水号查询费用明细
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<InsSzybFymx> qryFymxInfo(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			return null;
		List<InsSzybFymx> resMap = insSzybMapper.qryFymxInfo(paramMap);
		return resMap;
	}

	/**
	 * 015001005023 根据业务周期号查询结算中各个费用总和
	 * 
	 * @return
	 */
	public Map<String, Object> qrySumSzybJsKind(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || paramMap.size() == 0)
			return null;
		return insSzybMapper.qrySumSzybJsKind(paramMap);
	}

	/**
	 * 015001005028 更新宿州医保结算表-费用明细表-登记信息表的关系
	 * 
	 * @param param
	 *            {"yBRegID":"登记主键","jsId":"结算主键（医保）","fymxIdList":"费用明细主键集合",
	 *            "pkPv":"就诊主键","pkSettle":"结算主键"}
	 * @param user
	 */
	public void updateJsAndFymxAndMzdjInfo(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			return;
		if (paramMap.get("yBRegID") != null) {
			String djSql = "update ins_szyb_mzdj set pk_pv=? where id=?";
			DataBaseHelper.update(djSql, new Object[] { paramMap.get("pkPv"),
					paramMap.get("yBRegID") });
		}

//		if (paramMap.get("fymxIdList") != null) {
//			List<String> pkList = (List<String>) paramMap.get("fymxIdList");
//			Set<String> fymxIds = new HashSet<String>(pkList);
//			
//			String fySql = "update ins_szyb_fymx set pk_pv='"+paramMap.get("pkPv")+"' where id in( "+CommonUtils.convertSetToSqlInPart(fymxIds, "id")+")";
//			DataBaseHelper.update(fySql,new Object[]{});
//			
////			if (fymxIds.size() > 0) {
////				for (String id : fymxIds) {
////					String fySql = "update ins_szyb_fymx set pk_pv=? where id=?";
////					DataBaseHelper.update(fySql,
////							new Object[] { paramMap.get("pkPv"), id });
////				}
////			}
//		}
		/* 更新医保结算 */
		String jsSql = "update ins_szyb_js set pk_pv=? ,pk_settle=? where id=?";
		DataBaseHelper.update(jsSql, new Object[] { paramMap.get("pkPv"),
				paramMap.get("pkSettle"), paramMap.get("jsId") });
	}

	/**
	 * 015001005029 根据pkPv删除登记表和费用明细记录
	 * 
	 * @param param
	 *            {"pkPv":"就诊记录"}
	 * @param user
	 */
	public void deleteFymxAndMzdj(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String mzSql = "update ins_szyb_mzdj set del_flag='1' where pk_pv=?";
		DataBaseHelper.update(mzSql, new Object[] { paramMap.get("pkPv") });
		String flag = (String) paramMap.get("flag");
		if (!StringUtils.isEmpty(flag) && flag.equals("1")) {
			String fySql = "update ins_szyb_fymx set del_flag='1' where pk_pv=?";
			DataBaseHelper.update(fySql, new Object[] { paramMap.get("pkPv") });
		}

	}

	/**
	 * 015001005044 根据pkSettle删除登记表和费用明细记录
	 *
	 * @param param
	 *
	 * @param user
	 */
	public void delRegRelMz(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		if(paramMap!=null && paramMap.size()>0){
			//根据pkSettle查询对应的结算记录
			if(paramMap.containsKey("pkSettle")){
				//删除登记信息，软删除
				DataBaseHelper.update(
						"update INS_SZYB_MZDJ set DEL_FLAG = '1' where lsh in (select ywlsh from ins_szyb_js where PK_SETTLE=?)",
						new Object[]{paramMap.get("pkSettle")});
			}

			String flag = (String) paramMap.get("flag");
			if (!StringUtils.isEmpty(flag) && flag.equals("1")) {
				String fySql = "update ins_szyb_fymx set del_flag='1' where pk_pv=?";
				DataBaseHelper.update(fySql, new Object[] { paramMap.get("pkPv") });
			}
		}
	}

	/**
	 * 015001005030 根据pk_pv查询医保结信息
	 *
	 * @param param
	 *            {"pkPv":"就诊主键"}
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryJsInfoByPkPv(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			return null;
		String sql = "select * from ins_szyb_js where pk_pv=?";
		return DataBaseHelper.queryForList(sql,
				new Object[] { paramMap.get("pkPv") });
	}

	/**
	 *
	 *
	 * 015001005041 根据pk_pv查询宿州医保单病种
	 *
	 * @param param
	 *            {"pkPv":"就诊主键"}
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryPatiDrgs(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null)
			return null;
		String sql = "select * from ins_szyb_drgs where pk_pv=?";
		return DataBaseHelper.queryForList(sql,
				new Object[] { paramMap.get("pkPv") });
	}

	/**
	 * 015001005042 根据pk_pv查询宿州医保单病种
	 *
	 * @param param
	 *            {"InsSzybDrgs":"宿州医保单病种"}
	 * @param user
	 * @return
	 */
	public InsSzybDrgs savePatiDrgs(String param, IUser user) {
		InsSzybDrgs drgs = JsonUtil.readValue(param, InsSzybDrgs.class);
		User u=(User) user;
		if (drgs == null)
			return null;
		if(drgs.getPkInsszybdrgs()==null){
			drgs.setTs(new Date());
			drgs.setCreator(u.getPkEmp());
			drgs.setCreateTime(new Date());
			DataBaseHelper.insertBean(drgs);
		}else{
			drgs.setModifier(u.getPkEmp());
			drgs.setTs(new Date());
			DataBaseHelper.updateBeanByPk(drgs,false);
		}
		return drgs;
	}
	
	/**
	 * 交易号：015001005043
	 * 根据pkpv查询医疗分类，疾病分组，治疗方式信息
	 * @param param
	 * @param user
	 * @return
	 */
	public InsSzybDrgs qryDrgsByPkPv(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || !paramMap.containsKey("pkPv"))
			return null;
		
		List<InsSzybDrgs> drgsList = DataBaseHelper.queryForList(
				"select * from ins_szyb_drgs where pk_pv = ? and del_flag = '0'",
				InsSzybDrgs.class,new Object[]{paramMap.get("pkPv")});
		
		if(drgsList!=null && drgsList.size()>0){
			return drgsList.get(0);
		}else {
			return null;
		}
	}

	/***
	 * 交易号：015001005045
	 * 根据科室主键查询标准部门编码信息
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> qryDtSttypeByPkdept(String param,IUser user){
		List<Map<String,Object>> rtnList = new ArrayList<>();
		List<String> pkList = JsonUtil.readValue(
				param,
				new TypeReference<List<String>>() {
				});

		if(pkList!=null && pkList.size()>0){
			rtnList = insSzybMapper.qryDtSttypeByPkdept(pkList);
		}

		return rtnList;
	}

	/**
	 * 交易号：015001005046
	 * 查询人员备案信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryInsuEmpInfo(String param,IUser user){
		List<Map<String,Object>> rtnList = new ArrayList<>();
		rtnList = insSzybMapper.qryInsuEmpInfo();
		return rtnList;
	}


	/**
	 * 交易号：015001005047
	 * 保存人员备案信息
	 * @param param
	 * @param user
	 */
	public void insertInsuEmpInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		Integer cnt = DataBaseHelper.queryForScalar(
				"select count(1) from INS_DICT dict inner join INS_DICTTYPE ty on ty.PK_INSDICTTYPE = dict.PK_INSDICTTYPE where ty.CODE_TYPE = 'YS001' and code = ?",
				Integer.class,new Object[]{paramMap.get("codeEmp")});

		if(cnt!=null && cnt<=0){
			//查询人员字典类别主键信息
			String pkInsdicttype = DataBaseHelper.queryForScalar(
					"select PK_INSDICTTYPE from INS_DICTTYPE where CODE_TYPE = 'YS001' and del_flag='0'",
					String.class,new Object[]{});

			InsDict dictVo = new InsDict();
			dictVo.setPkInsdict(NHISUUID.getKeyId());
			dictVo.setCode(CommonUtils.getPropValueStr(paramMap,"codeEmp"));
			dictVo.setName(CommonUtils.getPropValueStr(paramMap,"nameEmp"));
			dictVo.setPkInsdicttype(pkInsdicttype);
			dictVo.setSpcode(Pinyin4jUtils.toFirstSpell(dictVo.getName()));
			dictVo.setDcode(Pinyin4jUtils.toFirstSpell(dictVo.getName()));
			ApplicationUtils.setDefaultValue(dictVo,true);
			DataBaseHelper.insertBean(dictVo);

			InsSzybEmployee empInfo = new InsSzybEmployee();
			empInfo.setPkId(NHISUUID.getKeyId());
			empInfo.setAcaPost(CommonUtils.getPropValueStr(paramMap,"acaPost"));
			empInfo.setCategorv(CommonUtils.getPropValueStr(paramMap,"categorv"));
			empInfo.setEmpCerNo(CommonUtils.getPropValueStr(paramMap,"empCerNo"));
			empInfo.setEmpJobName(CommonUtils.getPropValueStr(paramMap,"empJobName"));
			empInfo.setEmpLevel(CommonUtils.getPropValueStr(paramMap,"empLevel"));
			empInfo.setEmpSchool(CommonUtils.getPropValueStr(paramMap,"empSchool"));
			empInfo.setExPost(CommonUtils.getPropValueStr(paramMap,"exPost"));
			empInfo.setIdNo(CommonUtils.getPropValueStr(paramMap,"idNo"));
			empInfo.setInsuPrescription(CommonUtils.getPropValueStr(paramMap,"insuPrescription"));
			empInfo.setOper(CommonUtils.getPropValueStr(paramMap,"oper"));
			empInfo.setPkEmp(CommonUtils.getPropValueStr(paramMap,"pkEmp"));
			empInfo.setPraArea(CommonUtils.getPropValueStr(paramMap,"praArea"));
			empInfo.setRemark(CommonUtils.getPropValueStr(paramMap,"remark"));
			empInfo.setWorkPhone(CommonUtils.getPropValueStr(paramMap,"workPhone"));
			empInfo.setCodeEmp(CommonUtils.getPropValueStr(paramMap,"codeEmp"));

			ApplicationUtils.setDefaultValue(empInfo,true);
			DataBaseHelper.insertBean(empInfo);
		} else {
			List<InsSzybEmployee> empInfoList = DataBaseHelper.queryForList(
					"select * from INS_SZYB_EMPLOYEE where code_emp = ? and del_flag='0'",
					InsSzybEmployee.class,new Object[]{CommonUtils.getPropValueStr(paramMap,"codeEmp")});

			if(empInfoList!=null){
				InsSzybEmployee empInfo = empInfoList.get(0);
				empInfo.setAcaPost(CommonUtils.getPropValueStr(paramMap,"acaPost"));
				empInfo.setCategorv(CommonUtils.getPropValueStr(paramMap,"categorv"));
				empInfo.setEmpCerNo(CommonUtils.getPropValueStr(paramMap,"empCerNo"));
				empInfo.setEmpJobName(CommonUtils.getPropValueStr(paramMap,"empJobName"));
				empInfo.setEmpLevel(CommonUtils.getPropValueStr(paramMap,"empLevel"));
				empInfo.setEmpSchool(CommonUtils.getPropValueStr(paramMap,"empSchool"));
				empInfo.setExPost(CommonUtils.getPropValueStr(paramMap,"exPost"));
				empInfo.setIdNo(CommonUtils.getPropValueStr(paramMap,"idNo"));
				empInfo.setInsuPrescription(CommonUtils.getPropValueStr(paramMap,"insuPrescription"));
				empInfo.setOper(CommonUtils.getPropValueStr(paramMap,"oper"));
				empInfo.setPkEmp(CommonUtils.getPropValueStr(paramMap,"pkEmp"));
				empInfo.setPraArea(CommonUtils.getPropValueStr(paramMap,"praArea"));
				empInfo.setRemark(CommonUtils.getPropValueStr(paramMap,"remark"));
				empInfo.setWorkPhone(CommonUtils.getPropValueStr(paramMap,"workPhone"));

				DataBaseHelper.updateBeanByPk(empInfo,false);
			}
		}

	}

	/**
	 * 交易号：015001005048
	 * 删除人员备案信息
	 * @param param
	 * @param user
	 */
	public void delInsuEmpInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		//查询人员字典类别主键信息
		String pkInsdicttype = DataBaseHelper.queryForScalar(
				"select PK_INSDICTTYPE from INS_DICTTYPE where CODE_TYPE = 'YS001' and del_flag='0'",
				String.class,new Object[]{});

		DataBaseHelper.execute(
				"delete from ins_dict where PK_INSDICTTYPE = ? and code = ? "
				,new Object[]{pkInsdicttype,paramMap.get("codeEmp")});

		DataBaseHelper.execute(
				"delete from INS_SZYB_EMPLOYEE where code_emp = ? "
				,new Object[]{paramMap.get("codeEmp")});
	}

	/**
	 * 交易号：015001005049
	 * 查询上传结算单信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryStUploadInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		Map<String, Object> rtnMap = new HashMap<>(16);

		//查询就诊信息
		PvEncounter pvInfo = insSzybMapper.qryPvInfoByPkSettle(paramMap);
		//结算信息
		List<StReceVo> stList = new ArrayList<>();
		//医保支付明细信息
		List<Map<String,Object>> insuPayoutList = new ArrayList<>();
		List<Map<String,Object>> insuPayList = new ArrayList<>();

		paramMap.put("pkpv",pvInfo.getPkPv());

		if(EnumerateParameter.THREE.equals(pvInfo.getEuPvtype())){
			//查询结算信息
			stList = insSzybMapper.qryStInfo(paramMap);

			//查询医保支付明细信息
			insuPayList = insSzybMapper.qryInsuPayDtls(paramMap);

			//查询住院诊断信息
			rtnMap.put("stDiagList",insSzybMapper.qryIpDiagList(paramMap));

			//查询收费项目信息
			rtnMap.put("stChargeList",insSzybMapper.qryChargeDtls(paramMap));

			//查询手术信息
			rtnMap.put("stOperTeList",insSzybMapper.qryIclDtls(paramMap));
		}else{
			//查询结算信息
			stList = insSzybMapper.qryOpStInfo(paramMap);

			//查询医保支付明细信息
			insuPayList = insSzybMapper.qryOpInsuPayDtls(paramMap);

			//查询门诊慢特病诊断信息

			//查询住院诊断信息
			rtnMap.put("stSlowDiagDtls",insSzybMapper.qryOpDiagList(paramMap));

			//查询收费项目信息
			rtnMap.put("stChargeList",insSzybMapper.qryOpChargeDtls(paramMap));
		}

		if(stList!=null && stList.size()>0){
			stList.get(0).setBmy(UserContext.getUser().getCodeEmp());
			stList.get(0).setYljgtbr(UserContext.getUser().getCodeEmp());
			if(!CommonUtils.isEmptyString(UserContext.getUser().getPkDept())){
				BdOuDept deptVo = DataBaseHelper.queryForBean(
						"select * from bd_ou_dept where pk_dept = ?"
						, BdOuDept.class,new Object[]{UserContext.getUser().getPkDept()});

				stList.get(0).setYljgtbbm(deptVo!=null?deptVo.getNameDept():"");
			}

			//查询结算信息
			List<Map<String, Object>> stMapList = insSzybMapper.qryStInfoByPkSettle(paramMap);
			if(stMapList!=null && stMapList.size()>0){
				stList.get(0).setYwlsh(CommonUtils.getPropValueStr(stMapList.get(0),"ywlsh"));
				stList.get(0).setJsksrq(CommonUtils.getPropValueStr(stMapList.get(0),"jsksrq"));
				stList.get(0).setJsjsrq(CommonUtils.getPropValueStr(stMapList.get(0),"jsjsrq"));
				stList.get(0).setPjdm(CommonUtils.getPropValueStr(stMapList.get(0),"pjdm"));
				stList.get(0).setPjhm(CommonUtils.getPropValueStr(stMapList.get(0),"pjhm"));
			}

			rtnMap.put("stInfo",ApplicationUtils.beanToMap(stList.get(0)));
		}

		if(insuPayList!= null && insuPayList.size()>0){
			Map<String,Object> payMap = insuPayList.get(0);

			//医疗救助基金
			Map<String,Object> insuPayoutMap1 = new HashMap<>();
			insuPayoutMap1.put("jjzflx","610100");
			insuPayoutMap1.put("jjzfje",payMap.get("ljjzjj"));
			insuPayoutList.add(insuPayoutMap1);

			//公务员医疗补助基金
			Map<String,Object> insuPayoutMap2 = new HashMap<>();
			insuPayoutMap2.put("jjzflx","320100");
			insuPayoutMap2.put("jjzfje",payMap.get("gwyylbzjj"));
			insuPayoutList.add(insuPayoutMap2);

			//职工基本医疗保险统筹基金
			Map<String,Object> insuPayoutMap3 = new HashMap<>();
			insuPayoutMap3.put("jjzflx","310100");
			insuPayoutMap3.put("jjzfje",payMap.get("zgjbylbxtcjj"));
			insuPayoutList.add(insuPayoutMap3);

			//城乡居民基本医疗保险基金
			Map<String,Object> insuPayoutMap4 = new HashMap<>();
			insuPayoutMap4.put("jjzflx","390100");
			insuPayoutMap4.put("jjzfje",payMap.get("cxjmjbylbxjj"));
			insuPayoutList.add(insuPayoutMap4);

			//城乡居民大病医疗保险资金
			Map<String,Object> insuPayoutMap5 = new HashMap<>();
			insuPayoutMap5.put("jjzflx","391100");
			insuPayoutMap5.put("jjzfje",payMap.get("cxjmdbylbxzj"));
			insuPayoutList.add(insuPayoutMap5);

			//职工基本医疗保险个人账户基金
			Map<String,Object> insuPayoutMap6 = new HashMap<>();
			insuPayoutMap5.put("jjzflx","310200");
			insuPayoutMap5.put("jjzfje",payMap.get("grzhzf"));
			insuPayoutList.add(insuPayoutMap5);

			//职工大额医疗费用补助基金
			Map<String,Object> insuPayoutMap7 = new HashMap<>();
			insuPayoutMap7.put("jjzflx","330100");
			insuPayoutMap7.put("jjzfje",payMap.get("bcdbzcje"));
			insuPayoutList.add(insuPayoutMap7);

			//离休人员医疗保障基金
			Map<String,Object> insuPayoutMap8 = new HashMap<>();
			insuPayoutMap8.put("jjzflx","340100");
			insuPayoutMap8.put("jjzfje",0);
			insuPayoutList.add(insuPayoutMap8);

			Map<String,Object> insuPayoutMap9 = new HashMap<>();
			insuPayoutMap9.put("jjzflx","501000");
			insuPayoutMap9.put("jjzfje",0);
			insuPayoutList.add(insuPayoutMap9);

			Map<String,Object> insuPayoutMap10 = new HashMap<>();
			insuPayoutMap10.put("jjzflx","370100");
			insuPayoutMap10.put("jjzfje",0);
			insuPayoutList.add(insuPayoutMap10);

			Map<String,Object> insuPayoutMap11 = new HashMap<>();
			insuPayoutMap11.put("jjzflx","999999");
			insuPayoutMap11.put("jjzfje",0);
			insuPayoutList.add(insuPayoutMap11);

			Map<String,Object> insuPayoutMap12 = new HashMap<>();
			insuPayoutMap12.put("jjzflx","610200");
			insuPayoutMap12.put("jjzfje",0);
			insuPayoutList.add(insuPayoutMap12);

			Map<String,Object> insuPayoutMap13 = new HashMap<>();
			insuPayoutMap13.put("jjzflx","610300");
			insuPayoutMap13.put("jjzfje",0);
			insuPayoutList.add(insuPayoutMap13);

			Map<String,Object> insuPayoutMap14 = new HashMap<>();
			insuPayoutMap14.put("jjzflx","610700");
			insuPayoutMap14.put("jjzfje",0);
			insuPayoutList.add(insuPayoutMap14);

			insuPayoutList.stream()
					.forEach(pay->{
						pay.put("qdid",payMap.get("qdid"));
						pay.put("jsid",payMap.get("jsid"));
						pay.put("djlsh",payMap.get("djlsh"));
					});
		}

		if(insuPayoutList!=null && insuPayoutList.size()>0){
			rtnMap.put("stPayDtlsList",insuPayoutList);
		}

		return rtnMap;
	}

	/**
	 * 交易号：015001005050
	 *修改医保结算信息，清单ID
	 * @param param
	 * @param user
	 */
	public void upateYbjsInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		DataBaseHelper.execute("update ins_szyb_js set qdid = ? where pk_settle = ?",new Object[]{paramMap.get("qdid"),paramMap.get("pkSettle")});
	}

	/**
	 * 交易号：
	 * 查询医保结算信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryInsuStInfo(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		Map<String, Object> rtnMap = new HashMap<>(16);

		List<Map<String, Object>> stList = insSzybMapper.qryInsuStInfo(paramMap);
		if(stList!=null && stList.size()>0){
			rtnMap = stList.get(0);
		}

		return rtnMap;
	}



}
