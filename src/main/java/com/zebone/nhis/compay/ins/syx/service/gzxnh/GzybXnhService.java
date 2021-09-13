package com.zebone.nhis.compay.ins.syx.service.gzxnh;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybSt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybStXnh;
import com.zebone.nhis.common.module.compay.ins.syx.xnh.InsGzybStXnhDt;
import com.zebone.nhis.common.module.compay.ins.syx.xnh.InsXnhRefdoc;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.compay.ins.syx.dao.gzxnh.GzxnhMapper;
import com.zebone.nhis.compay.ins.syx.vo.gzxnh.DiagVo;
import com.zebone.nhis.compay.ins.syx.vo.gzxnh.SettlePageDtVo;
import com.zebone.nhis.compay.ins.syx.vo.gzxnh.SettlePageHeaderVo;
import com.zebone.nhis.compay.ins.syx.vo.gzxnh.XnhBlipdtVo;
import com.zebone.nhis.compay.ins.syx.vo.gzxnh.XnhDeptVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.StVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 广州新农合
 * 
 * @author Administrator
 * 
 */
@Service
public class GzybXnhService {

	@Resource
	private GzxnhMapper gzxnhMapper;

	/**
	 * 015001008001 入院登记：2.保存转诊单
	 * 
	 * @param param
	 *            {"insXnhRefdoc":"转诊单记录"}
	 * @param user
	 * @return {"insXnhRefdoc":"转诊单记录（有主键）"}
	 */
	public InsXnhRefdoc saveXnhRefdoc(String param, IUser user) {
		InsXnhRefdoc xnhRefdoc = JsonUtil.readValue(param, InsXnhRefdoc.class);
		if (xnhRefdoc == null || xnhRefdoc.getCodeDoc() == null)
			throw new BusException("传入数据失败！");
		User u = (User) user;
		xnhRefdoc.setPkOrg(u.getPkOrg());
		// 判断当前数据库中是否存在有效的转诊数据
		String sql = "select count(1) from ins_xnh_refdoc where code_doc=? and del_flag='0'";
		int count = DataBaseHelper.queryForScalar(sql, Integer.class,
				xnhRefdoc.getCodeDoc());

		if (count == 1) {
			/**
			 * String uSql =
			 * "update ins_xnh_refdoc set pk_pi=?, pk_hp=? where code_doc=?";
			 * DataBaseHelper.update(uSql, new Object[] { xnhRefdoc.getPkPi(),
			 * xnhRefdoc.getPkHp(), xnhRefdoc.getCodeDoc() });
			 */
			DataBaseHelper.updateBeanByPk(xnhRefdoc, false);
		} else {
			DataBaseHelper.insertBean(xnhRefdoc);
		}
		return xnhRefdoc;
	}

	/**
	 * 015001008002 入院登记：3. 关联转诊单与患者就诊-就诊记录pv_encounter的pk_pv更新转诊单就诊主键pk_pv
	 * 
	 * @param param
	 *            {"pkPi":"患者主键","pkPv":"就诊主键"}
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void createPvRefdocVisitRef(String param, IUser user) {
		String pkvisit = JsonUtil.getFieldValue(param, "pkvisit");
		String pkPi = JsonUtil.getFieldValue(param, "pkPi");
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkvisit == null || "".equals(pkvisit))
			throw new BusException("HIS医保登记信息为空！");
		if (pkPi == null || "".equals(pkPi))
			throw new BusException("患者信息为空！");
		if (pkPv == null || "".equals(pkPv))
			throw new BusException("就诊信息为空！");

		String codeDoc = "";
		String codeSql = "select code_doc from ins_gzyb_visit_xnh where pk_visit=? and del_flag='0' ";
		Map<String, Object> codeDocMap = DataBaseHelper.queryForMap(codeSql,
				pkvisit);
		if (codeDocMap != null && codeDocMap.get("codeDoc") != null) {
			codeDoc = codeDocMap.get("codeDoc").toString();// 转诊单号
		}
		if (codeDoc != null && codeDoc != "") {
			DataBaseHelper
					.execute(
							"update ins_xnh_refdoc set PK_PV=?,PK_PI=? where code_doc=?",
							pkPv, pkPi, codeDoc);
		}
		DataBaseHelper.execute(
				"update INS_GZYB_VISIT set PK_PV=?,PK_PI=? where pk_visit=?",
				pkPv, pkPi, pkvisit);
	}

	/**
	 * 015001008003 取消住院登记：1.删除就诊单
	 * 
	 * @param param
	 *            {"pkPv":"就诊主键"}
	 * @param user
	 */
	public void deleteRefdocByPv(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv))
			throw new BusException("未获得就诊信息！");
		String sql = "UPDATE INS_XNH_REFDOC SET DEL_FLAG='1' WHERE pk_pv=?";
		DataBaseHelper.update(sql, new Object[] { pkPv });
	}

	/**
	 * 015001008004 取消住院登记：2.删除就诊登记表
	 * 
	 * @param param
	 *            {"pkPv":"就诊主键"}
	 * @param user
	 */
	public void deleteVisitByPkPv(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv))
			throw new BusException("未获得就诊信息！");
		String pkVisit = "";
		String sql = "SELECT PK_VISIT FROM INS_GZYB_VISIT WHERE PK_PV=? AND DEL_FLAG='0'";
		Map<String, Object> visitMap = DataBaseHelper.queryForMap(sql,
				new Object[] { pkPv });
		if (visitMap != null && visitMap.get("pkVisit") != null) {
			pkVisit = visitMap.get("pkVisit").toString();
		}
		String visitSql = "UPDATE INS_GZYB_VISIT SET DEL_FLAG='1' WHERE pk_pv=?";
		DataBaseHelper.update(visitSql, new Object[] { pkPv });
		String visitDtSql = "UPDATE INS_GZYB_VISIT_XNH SET DEL_FLAG='1' WHERE PK_VISIT=?";
		DataBaseHelper.update(visitDtSql, new Object[] { pkVisit });
	}

	/**
	 * 015001008005 出院结算：保存结算结果
	 * 
	 * @param param
	 *            {["InsGzybStXnhDt":"农合结算明细"]}
	 * @param user
	 * @return
	 */
	public void saveXnhStDt(String param, IUser user) {
		User u = (User) user;
		List<InsGzybStXnhDt> xnhStDts = JsonUtil.readValue(param,
				new TypeReference<List<InsGzybStXnhDt>>() {
				});
		if (xnhStDts != null && xnhStDts.size() > 0) {
			for (InsGzybStXnhDt xnhStDt : xnhStDts) {
				xnhStDt.setPkOrg(u.getPkOrg());
				DataBaseHelper.insertBean(xnhStDt);
			}
		}
	}

	/***
	 * 015001008006 取消出院结算
	 * 
	 * @param param
	 *            {"pkPv":"就诊主键"}
	 * @param user
	 */
	public void deleteXnJsInfo(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv))
			throw new BusException("未获得就诊信息！");

		String pkInsst = "";
		String pkInsstxnh = "";
		// 1.通过pkPv查询ins_gzyb_st中主键信息
		String stSql = "select pk_insst  from ins_gzyb_st  where pk_pv=? and (del_flag='0' or del_flag is null)";
		Map<String, Object> pkInsstMap = DataBaseHelper
				.queryForMap(stSql, pkPv);
		if (pkInsstMap != null && pkInsstMap.get("pkInsst") != null) {
			pkInsst = pkInsstMap.get("pkInsst").toString();
		}

		// 2.通过pkInsst查询ins_gzyb_st_xnh中主键信息
		String xnhSql = "select pk_insstxnh as pkInsstxnh from ins_gzyb_st_xnh where pk_insst =? and del_flag='0'";
		Map<String, Object> pkInsstxnhMap = DataBaseHelper.queryForMap(xnhSql,
				pkInsst);
		if (pkInsstxnhMap != null && pkInsstxnhMap.get("pkinsstxnh") != null) {
			pkInsstxnh = pkInsstxnhMap.get("pkinsstxnh").toString();
		}

		// 3.分别依次更新ins_gzyb_st,ins_gzyb_st_xnh,ins_gzyb_st_xnh_d;
		String sql = "update ins_gzyb_st set del_flag='1' where pk_pv=?";
		DataBaseHelper.update(sql, new Object[] { pkPv });
		sql = "update ins_gzyb_st_xnh set del_flag='1' where pk_insst=?";
		DataBaseHelper.update(sql, new Object[] { pkInsst });
		sql = "update ins_gzyb_st_xnh_dt set del_flag='1' where pk_insstxnh=?";
		DataBaseHelper.update(sql, new Object[] { pkInsstxnh });
		sql = "update INS_GZYB_VISIT set EU_STATUS_ST='0' where PK_PV=?";
		DataBaseHelper.update(sql, new Object[] { pkPv });
	}

	/**
	 * 015001008012 根据转诊单号查询转诊单信息
	 * 
	 * @param param
	 *            {"codeDoc":"转诊单号"}
	 * @param user
	 *            {"insXnhRefdoc":"转诊单vo"}
	 * @return
	 */
	public InsXnhRefdoc qryXnhRefdocByPk(String param, IUser user) {
		String codeDoc = JsonUtil.getFieldValue(param, "codeDoc");
		if (codeDoc == null || "".equals(codeDoc))
			throw new BusException("转诊单号传入为空！");
		String sql = "select * from ins_xnh_refdoc where del_flag='0' and code_doc=?";
		InsXnhRefdoc xnhRefdoc = DataBaseHelper.queryForBean(sql,
				InsXnhRefdoc.class, new Object[] { codeDoc });
		return xnhRefdoc;

	}

	/**
	 * 015001008017 根据pkPv查询新农合登记信息和转诊单信息
	 * 
	 * @param param
	 *            {"pkPv":"就诊主键"}
	 * @param user
	 * @return
	 */
	public Map<String, Object> qryXnhPiInfoByPkPv(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		String type = JsonUtil.getFieldValue(param, "tyPe");
		if (pkPv == null || "".equals(pkPv)) {
			throw new BusException("未获得就诊信息！");
		}
		return gzxnhMapper.qryXnhPiInfoByPkPv(pkPv,type);
	}

	/**
	 * 015001008018 根据患者住院登记流水号查询患者结算信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> qryXnhJsInfo(String param, IUser user) {
		String pkVisit = JsonUtil.getFieldValue(param, "pkVisit");
		if (pkVisit == null || "".equals(pkVisit))
			throw new BusException("未获取患者住院登记信息！");

		return gzxnhMapper.qryXnhJsInfo(pkVisit);

	}

	/**
	 * 015001008019通过患者主键跟就诊主键获取已上传未结算的收费明细表-患者自费总额
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String queryShareAmount(String param, IUser user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv))
			throw new BusException("就诊信息为空！");
		String amtPi = gzxnhMapper.queryShareAmount(paramMap);
		if (amtPi == null || "".equals(amtPi)) {
			return "0";
		} else {
			return amtPi;
		}
	}

	@SuppressWarnings("unused")
	private BigDecimal amtTrans(Object amt) {
		if (amt == null) {
			return BigDecimal.ZERO;
		} else {
			return (BigDecimal) amt;
		}
	}

	/**
	 * 015001008020 新农合结算信息保存处理
	 * 
	 * @param param
	 * @param user
	 */
	public void saveXnhStInfo(String param, IUser user) {
		// 1.1接收参数（ins_gzyb_st,ins_gzyb_st_city）
		StVo vo = JsonUtil.readValue(param, StVo.class);
		GzybSt st = vo.getSt();
		GzybStXnh stXnh = vo.getStXnh();
		User u = (User) user;

		// 1.2接收参数做验证
		if (st == null)
			throw new BusException("未获得结算基本信息！");
		if (stXnh == null)
			throw new BusException("未获得市医保结算信息！");
		// 2.2根据登记信息查询结算主键
		String qrySql = "select pk_insst from ins_gzyb_st where pk_visit=? and del_flag='0' ";
		Map<String, Object> insstMap = DataBaseHelper.queryForMap(qrySql,
				new Object[] { st.getPkvisit() });

		// 2.1逻辑处理：更新结算记录-通过登记主键更新del_flag='1'
		String sql = "delete from ins_gzyb_st where pk_visit=?";
		DataBaseHelper.execute(sql, new Object[] { st.getPkvisit() });

		// 2.3根据pk_insst更新结算新农合del_flag='1'
		if (CommonUtils.isNotNull(insstMap)
				&& CommonUtils.isNotNull(insstMap.get("pkInsst"))) {// 判断是否为空
			String xnhSql = "delete from ins_gzyb_st_xnh where pk_insst=?";
			DataBaseHelper.execute(xnhSql,
					new Object[] { insstMap.get("pkInsst") });
		}
		// 2.4保存结算记录
		st.setPkOrg(u.getPkOrg());
		st.setDelFlag("0");
		st.setCreator(u.getPkUser());
		st.setTs(new Date());
		DataBaseHelper.insertBean(st);
		// 2.5保存跨省新农合医保结算信息
		stXnh.setPkInsst(st.getPkInsst());
		stXnh.setPkOrg(u.getPkOrg());
		stXnh.setDelFlag("0");
		stXnh.setCreator(u.getPkUser());
		stXnh.setTs(new Date());
		DataBaseHelper.insertBean(stXnh);
	}

	/**
	 * 根据就诊主键pk_pv查询住院收费明细bl_ip_dt的医保上传标志flag_insu为0的记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<XnhBlipdtVo> qryBdItemAndOrderByPkPv(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv)) {
			throw new BusException("入参患者住院登记主键为空！");
		}
		List<XnhBlipdtVo> list = gzxnhMapper.qryBdItemAndOrderByPkPv(pkPv);
		return list;
	}

	/**
	 * 根据pkPv查询患者出入院状态及关联平台科室
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> qryPreInpTempInfo(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv)) {
			throw new BusException("入参患者住院登记主键为空！");
		}
		return gzxnhMapper.qryPreInpTempInfo(pkPv);

	}

	/**
	 * 015001008023 获取结算单打印数据（基本数据和明细数据）
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> qrySettlePageData(String param, IUser user) {
		// 1.接收数据，做初步校验
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv)) {
			throw new BusException("入参患者住院登记主键为空！");
		}
		// 2.查询结算基本信息和明细信息
		SettlePageHeaderVo pageVo = gzxnhMapper.qryPageHeaderVo(pkPv);
		List<SettlePageDtVo> dtVos = gzxnhMapper.qryPageDtVos(pkPv);
		// 3.组合基本信息
		if (pageVo != null) {
			// 3.1年龄
			if (pageVo.getBirthday() != null
					&& !"".equals(pageVo.getBirthday())) {
				String age = DateUtils.getAgeByBirthday(DateUtils
						.strToDate(pageVo.getBirthday()),DateUtils
						.strToDate(pageVo.getDateInt()));
				pageVo.setAge(age);
			}
			// 3.2出院日期，住院天数，结账日期
			// pageVo.setDateOut(DateUtils.getDate("yyyy-MM-dd hh:mm:ss"));
			if (pageVo.getDateInt() != null && !"".equals(pageVo.getDateInt())) {
				Date dateInt = DateUtils.strToDate(pageVo.getDateInt(),
						"yyyy-MM-dd hh:mm:ss");
				Date dateOut = DateUtils.strToDate(pageVo.getDateOut(),
						"yyyy-MM-dd hh:mm:ss");
				Integer days = DateUtils.getDateSpace(dateInt, dateOut);
				pageVo.setDays(days.toString());
			}
			// pageVo.setDateCC(DateUtils.getDate("yyyy-MM-dd hh:mm:ss"));
			// 3.3 直报金额大写 ,自付金额大写 --交由前台公共方法进行处理 ；后台方法类AmountConversionUtil；
			// 3.4患者地址拼接
			String patientAddress = pageVo.getNameChs() + pageVo.getNameChsh()
					+ pageVo.getNameChqx();
			pageVo.setPatientAddress(patientAddress);
		}
		// 4.组装返回数据
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("settleInfo", pageVo);
		resMap.put("settleDt", dtVos);
		return resMap;
	}

	/**
	 * 015001008009 取消住院登记
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String cancelInpRegister(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv)) {
			throw new BusException("入参患者住院登记主键为空！");
		}
		String inpNo = gzxnhMapper.qryXnhCodePv(pkPv);
		return inpNo;
	}

	/**
	 * 015001008024获取科室对照信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public XnhDeptVo qryXNhDeptVo(String param, IUser user) {
		String pkeptadmit = JsonUtil.getFieldValue(param, "pkeptadmit");
		XnhDeptVo xnhDeptVo = gzxnhMapper.qryXNhDeptVo(pkeptadmit);
		return xnhDeptVo;
	}

	/**
	 * 015001008025通过pkpv获取诊断信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public DiagVo qryDiagVo(String param, IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkpv == null || "".equals(pkpv)) {
			throw new BusException("入参患者住院登记主键为空！");
		}
		DiagVo diagVo = gzxnhMapper.qryDiagVo(pkpv);
		return diagVo;
	}

	/**
	 * 015001008026通过pkpv更新门诊或住院的flag_insu标记
	 * 
	 * @param param
	 * @param user
	 */
	public void updateOPorIpForInsByPkpv(String param, IUser user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if (map == null || map.size() <= 0)
			throw new BusException("未获得相关信息");
		if ("i".equalsIgnoreCase(map.get("type").toString())) {
			String sql = "update bl_ip_dt set FLAG_INSU='0' where pk_pv=? ";
			DataBaseHelper.execute(sql, map.get("pkPv"));
		} else if ("o".equalsIgnoreCase(map.get("type").toString())) {
			String sql = "update bl_op_dt set FLAG_INSU='0' where PK_PV=? ";
			DataBaseHelper.execute(sql, map.get("pkPv"));
		}
	}

	/**
	 * 015001008027
	 * 通过医保结算登记主键PK_INSST将PkSettle结算主键更新到INS_GZYB_ST表同时更新INS_GZYB_VISIT表
	 * 
	 * @param param
	 * @param user
	 */
	public void updatePkSettle(String param, IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
		if (pkpv != null && pkSettle != null) {
			if ("1".equals(pkSettle)) {
				DataBaseHelper
						.execute(
								"update INS_GZYB_ST set PK_SETTLE='' where PK_PV=? and del_flag='0'",
								pkpv);
				DataBaseHelper
						.execute(
								"update INS_GZYB_VISIT set EU_STATUS_ST='0' where PK_PV=? and del_flag='0'",
								pkpv);
			} else {
				DataBaseHelper
						.execute(
								"update INS_GZYB_ST set PK_SETTLE=? where PK_PV=? and del_flag='0'",
								pkSettle, pkpv);
				DataBaseHelper
						.execute(
								"update INS_GZYB_VISIT set EU_STATUS_ST='1' where PK_PV=? and del_flag='0'",
								pkpv);
				DataBaseHelper
						.execute(
								"update bl_ip_dt set FLAG_INSU='1' where pk_pv=? and PK_SETTLE=?",
								pkpv, pkSettle);
			}
		} else {
			throw new BusException("传入参数有空值");
		}
	}
}
