package com.zebone.nhis.scm.material.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdCc;
import com.zebone.nhis.common.module.scm.st.PdCcDetail;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.scm.material.dao.MtlPdCcMapper;
import com.zebone.nhis.scm.material.vo.MtlPdBaseParam;
import com.zebone.nhis.scm.material.vo.MtlPdCcVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 库存结账
 * 
 * @author wj
 * @date 2018年08月17日
 * 
 */
@Service
public class MtlPdCcService {

	/***
	 * 库存结账mapper
	 */
	@Resource
	private MtlPdCcMapper mtlPdCcMapper;

	/**
	 * 交易号：008007011001
	 * 查询库存结账明细
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPdCcList(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param,Map.class);
		map.put("pkOrg", UserContext.getUser().getPkOrg());
		map.put("pkStore", UserContext.getUser().getPkStore());
		List<Map<String, Object>> dList = mtlPdCcMapper.getPdCcList(map);
		return dList;
	}
	
	/**
	 * 交易号：008007011002
	 * 查询库存结账明细
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public MtlPdCcVo getPdCcVo(String param, IUser user) {
		PdCc cc = JsonUtil.readValue(param, PdCc.class);
		MtlPdCcVo vo = new MtlPdCcVo();
		PdCc pdCc = mtlPdCcMapper.getPdCcById(cc.getPkPdcc());
		List<PdCcDetail> dList = mtlPdCcMapper.getPdCcDetailListById(cc .getPkPdcc());
		vo.setPdCc(pdCc);
		vo.setPdCcDetailList(dList);
		return vo;
	}

	/**
	 * 交易号：008007011003
	 * 保存库存结账
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public MtlPdCcVo savePdCcAndDetail(String param, IUser user) {
		PdCc cc = JsonUtil.readValue(param, PdCc.class);
		User u = UserContext.getUser();
		MtlPdCcVo vo = new MtlPdCcVo();
		List<PdCcDetail> dList = new ArrayList<PdCcDetail>();

		String dateBegin = DateUtils.dateToStr("yyyyMMddHHmmss", cc.getDateBegin());
		String dateEnd = DateUtils.dateToStr("yyyyMMddHHmmss", cc.getDateEnd());
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkStore", u.getPkStore());
		mapParam.put("monthFin", cc.getMonthFin() + "");
		mapParam.put("dateBeginStr", dateBegin);
		mapParam.put("dateEndStr", dateEnd);

		// 需要查询财务月份的上个月
		Integer monthFin = cc.getMonthFin();
		if (monthFin == null) {
			throw new BusException("财务月份不能为空！");
		}
		String mFin = cc.getMonthFin().toString();
		Date mFinD = DateUtils.strToDate(mFin, "yyyyMM");
		String monthFinLast=DateUtils.addDate(mFinD, -1, 2, "yyyyMM");
		mapParam.put("monthFinLast", monthFinLast);

		// 首先获取物品基本信息（pk_pd、pk_unit_pack、pack_size确定一个物品）
		List<MtlPdBaseParam> pdBaseList = mtlPdCcMapper.getPdBaseParamList(mapParam);

		// 查询上月结存数
		Map<String, Map<String, Object>> lastCcMaps = DataBaseHelper .queryListToMap(
				"select dt.pk_pd || '~' || dt.pk_unit as key_,dt.quan_min,dt.amount_cost,dt.amount "
								+ "  from pd_cc_detail dt "
								+ " inner join bd_pd pd on pd.pk_pd = dt.pk_pd and pd.del_flag = '0' "
								+ " inner join pd_cc cc on cc.pk_pdcc = dt.pk_pdcc and cc.del_flag = '0'"
								+ " where dt.del_flag = '0' "
								+ "   and pd.dt_pdtype like '1%'"
								+ "   and cc.pk_store=? "
								+ "   and cc.month_fin= ?",
						new Object[] { u.getPkStore(), monthFinLast + "" });

		// 查询本月入库数
		Map<String, Map<String, Object>> inStMaps = DataBaseHelper.queryListToMap(
						"select dt.pk_pd || '~' || dt.pk_unit_pack as key_,nvl(sum(dt.quan_min),0) quan_min"
						   + " ,nvl(sum(dt.amount_cost),0) amt_cost,nvl(sum(dt.amount),0) amt "
						   + "  from pd_st_detail dt "
						   + " inner join pd_st st on dt.pk_pdst=st.pk_pdst and st.del_flag = '0' "
						   + " inner join bd_pd pd on pd.pk_pd = dt.pk_pd and pd.del_flag = '0' "
						   + " where dt.del_flag = '0' and st.eu_direct = '1' and pd.dt_pdtype like '1%' "
						   + "	 and st.pk_store_st = ? "
						   + "	 and st.date_chk >= to_date(?, 'yyyymmddhh24miss') "
						   + "	 and st.date_chk < to_date(?, 'yyyymmddhh24miss') "
						   + "group by dt.pk_pd,dt.pk_unit_pack",
						new Object[] { u.getPkStore(), dateBegin, dateEnd });

		// 询本月出库数
		Map<String, Map<String, Object>> outStMaps = DataBaseHelper.queryListToMap(
						"select dt.pk_pd || '~' || dt.pk_unit_pack as key_,nvl(sum(dt.quan_min),0) quan_min"
								+ ",nvl(sum(dt.amount_cost),0) amt_cost,nvl(sum(dt.amount),0) amt "
								+ "from pd_st_detail dt "
								+ " inner join pd_st st on dt.pk_pdst=st.pk_pdst and st.del_flag = '0' "
								+ " inner join bd_pd pd on pd.pk_pd = dt.pk_pd and pd.del_flag = '0' "
								+ " where dt.del_flag = '0' and st.eu_direct = '-1' and pd.dt_pdtype like '1%' "
								+ "	  and st.pk_store_st = ? "
								+ "	  and st.date_chk >= to_date(?, 'yyyymmddhh24miss') "
								+ "	  and st.date_chk < to_date(?, 'yyyymmddhh24miss') "
								+ "group by dt.pk_pd,dt.pk_unit_pack",
						new Object[] { u.getPkStore(), dateBegin, dateEnd });

		// 查询本月调价数据
		Map<String, Map<String, Object>> rePriceMaps = DataBaseHelper.queryListToMap(
						"select dt.pk_pd || '~' || dt.pk_unit_pack as key_,nvl(sum(dt.amount_rep),0) amt "
								+ " from pd_reprice_hist dt "
								+ " inner join bd_pd pd on pd.pk_pd = dt.pk_pd and pd.del_flag = '0' "
								+ " where dt.del_flag = '0' and pd.dt_pdtype like '1%' "
								+ "   and dt.pk_store = ? "
								+ "   and dt.date_reprice >= to_date(?, 'yyyymmddhh24miss') "
								+ "   and dt.date_reprice < to_date(?, 'yyyymmddhh24miss') "
								+ " group by dt.pk_pd, dt.pk_unit_pack",
						new Object[] { u.getPkStore(), dateBegin, dateEnd });

		// 保存
		String pkPdcc = NHISUUID.getKeyId();

		Date ts = new Date();
		if (CollectionUtils.isNotEmpty(pdBaseList)) {
			for (int i = 0; i < pdBaseList.size(); i++) {
				Double amountCost = new Double(0); // 成本金额
				Double amount = new Double(0); // 零售金额
				Double quanMin = new Double(0); // 数量
				MtlPdBaseParam p = pdBaseList.get(i);
				String key = p.getPkPd() + "~" + p.getPkUnitPack();
				Map<String, Object> lastCcMap = lastCcMaps.get(key);
				Map<String, Object> inStMap = inStMaps.get(key);
				Map<String, Object> outStMap = outStMaps.get(key);
				Map<String, Object> rePriceMap = rePriceMaps.get(key);
				// 数量=期初+入库-出库
				// 成本金额=期初+入库-出库
				// 零售金额=期初+入库-出库+调价
				if (lastCcMap != null) {
					quanMin = quanMin + Double.valueOf(lastCcMap.get("quanMin") .toString());
					amountCost = amountCost + Double.valueOf(lastCcMap.get("amountCost") .toString());
					amount = amount + Double.valueOf(lastCcMap.get("amount").toString());
				}
				if (inStMap != null) {
					quanMin = quanMin + Double.valueOf(inStMap.get("quanMin").toString());
					amountCost = amountCost + Double.valueOf(inStMap.get("amtCost").toString());
					amount = amount + Double.valueOf(inStMap.get("amt").toString());
				}
				if (outStMap != null) {
					quanMin = quanMin - Double.valueOf(outStMap.get("quanMin").toString());
					amountCost = amountCost - Double.valueOf(outStMap.get("amtCost").toString());
					amount = amount - Double.valueOf(outStMap.get("amt").toString());
				}
				if (rePriceMap != null) {
					amount = amount + Double.valueOf(rePriceMap.get("amt").toString());
				}

				PdCcDetail detail = new PdCcDetail();
				detail.setPkPdccdt(NHISUUID.getKeyId());
				detail.setPkOrg(u.getPkOrg());
				detail.setPkPdcc(pkPdcc);
				detail.setPkPd(p.getPkPd());
				detail.setPkUnit(p.getPkUnitPack());
				detail.setPackSize(p.getPackSize());
				detail.setQuanMin(quanMin);
				detail.setAmountCost(amountCost);
				detail.setAmount(amount);
				detail.setCreator(u.getPkEmp());
				detail.setCreateTime(ts);
				detail.setModifier(u.getPkEmp());
				detail.setDelFlag("0");
				detail.setTs(ts);
				dList.add(detail);
			}

			// 保存pd_cc
			cc.setPkPdcc(pkPdcc);
			cc.setPkDept(u.getPkDept());
			cc.setPkStore(u.getPkStore());
			cc.setPkEmpCc(u.getPkEmp());
			cc.setNameEmpCc(u.getNameEmp());
			cc.setDateCc(ts);
			DataBaseHelper.insertBean(cc);

			// 保存pd_cc_detail
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(PdCcDetail.class), dList);
		} else {
			throw new BusException("该时间段内没有未结账物品！");
		}
		return vo;
	}

	/**
	 * 交易号：008007011004
	 * 取消库存结账
	 * 
	 * @param param
	 * @param user
	 */
	public void deletePdCc(String param, IUser user) {
		PdCc cc = JsonUtil.readValue(param, PdCc.class);
		String pkPdcc = cc.getPkPdcc();
		// 先删除明细
		DataBaseHelper.execute("delete from pd_cc_detail where pk_pdcc = ?", new Object[] { pkPdcc });
		DataBaseHelper.execute("delete from pd_cc where pk_pdcc = ? ", new Object[] { pkPdcc });
	}

	/**
	 * 交易号：008007011005
	 * 获取财务月份
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getPdCcMouth(String param, IUser user) {
		Map<String, Object> map = new HashMap<String, Object>();
		User u = UserContext.getUser();
		PdCc pdCc = new PdCc();
		if (CommonUtils.isEmptyString(u.getPkDept())) {
			throw new BusException("请维护部门！");
		}
		if (CommonUtils.isEmptyString(u.getPkOrg())) {
			throw new BusException("请维护机构！");
		}
		if (CommonUtils.isEmptyString(u.getPkStore())) {
			throw new BusException("请维护仓库！");
		}
		if (Application.isSqlServer()) {
			pdCc = DataBaseHelper.queryForBean( "select top 1 * from PD_CC "
					+ "where pk_org = ? and pk_store = ? and pk_dept= ? "
					+ "  and del_flag = '0' "
					+ "  and exists (select 1 from bd_pd pd "
					+ "             inner join pd_cc_detail dt on dt.pk_pd = pd.pk_pd "
					+ "             where dt.pk_pdcc = PD_CC.pk_pdcc and pd.dt_pdtype like '1%') "
					+ "order by PD_CC.month_fin desc", PdCc.class, new Object[] { u.getPkOrg(), u.getPkStore(), u.getPkDept() });
		} else {
			List<PdCc> cclist = DataBaseHelper.queryForList( "select * from PD_CC where pk_org = ? and pk_store = ? "
					+ " and pk_dept= ? "
					+ " and del_flag = '0' "
					+ " and exists (select 1 from bd_pd pd "
					+ "             inner join pd_cc_detail dt on dt.pk_pd = pd.pk_pd "
					+ "             where dt.pk_pdcc = PD_CC.pk_pdcc and pd.dt_pdtype like '1%') "
					+ "order by PD_CC.month_fin desc", PdCc.class, new Object[] { u.getPkOrg(), u.getPkStore(), u.getPkDept() });
			if (cclist != null && cclist.size() > 0)
				pdCc = cclist.get(0);
			else
				pdCc = null;
		}
		if (pdCc == null) {
			SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
			String time = format.format(Calendar.getInstance().getTime());
			map.put("monthFin", time);
		} else {
			String obligate;
			Integer Monthtime = pdCc.getMonthFin();
			String monthtime = Monthtime.toString();
			obligate = monthtime.substring(4, 6);
			if (obligate.equals("12")) {
				try {
					Integer a = Integer.parseInt(monthtime);
					a += 89;
					String sa = a.toString();
					sa += "25000000";
					String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
					sa = sa.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
					map.put("monthFin", sa);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			} else {
				Monthtime = Monthtime + 1;
				monthtime = Monthtime.toString();
				monthtime += "25000000";
				String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
				monthtime = monthtime.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
				map.put("monthFin", monthtime);
			}
		}
		return map;
	}

	/**
	 * 交易号：008007011006
	 * 获取开始时间<br>
	 * 如果第一次月结时，取当前仓库业务类型为“0110（初始化入库）”的最小审核日期，否则为当前仓库上次月结终止日期+1秒
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getPdCcDateBegin(String param, IUser user) {
		User u = UserContext.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		Date dateBegin = null;
		// 查询上次月结
		PdCc pdCc = new PdCc();
		if (Application.isSqlServer()) {
			StringBuffer sql=new StringBuffer();
			sql.append("select top 1 * from pd_cc where del_flag = '0' and pk_org = ? and pk_store = ? ");
			sql.append(" and pk_dept = ?  and exists (select 1 from bd_pd pd");
			sql.append(" inner join pd_cc_detail dt on dt.pk_pd = pd.pk_pd");
			sql.append("  where dt.pk_pdcc = pd_cc.pk_pdcc and pd.dt_pdtype like '1%') order by month_fin desc");
			pdCc = DataBaseHelper .queryForBean(sql.toString(),PdCc.class,new Object[] { u.getPkOrg(), u.getPkStore(),u.getPkDept() });
		} else {
			StringBuffer sql=new StringBuffer();
			sql.append(" select t.* from (select * from pd_cc where del_flag = '0' and pk_org = ? ");
			sql.append(" and exists (select 1 from bd_pd pd inner join pd_cc_detail dt on dt.pk_pd = pd.pk_pd ");
			sql.append(" where dt.pk_pdcc = pd_cc.pk_pdcc and pd.dt_pdtype like '1%')");
			sql.append(" and pk_store = ? and pk_dept = ? order by month_fin desc) t ");
			sql.append(" where rownum = 1");
			pdCc = DataBaseHelper .queryForBean(sql.toString(), PdCc.class, new Object[] { u.getPkOrg(), u.getPkStore(), u.getPkDept() });
		}
		if (pdCc != null) {
			// 为当前仓库上次月结终止日期+1秒
			Date begin = pdCc.getDateEnd();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(begin);
			calendar.add(Calendar.SECOND, 1);
			dateBegin = calendar.getTime();
		} else {
			// 没有月结时取当前仓库业务类型为“0110（初始化入库）”的最小审核日期
			dateBegin = DataBaseHelper.queryForScalar("select min(date_chk) from pd_st"
					+ " where dt_sttype = '0110' and del_flag = '0' and pk_org = ? and pk_store_st = ? "
					+ "and exists (select 1 from bd_pd pd "
					+ "             inner join pd_st_detail dt on dt.pk_pd = pd.pk_pd "
					+ "             where dt.pk_pdst = pd_st.pk_pdst and pd.dt_pdtype like '1%')",
							Date.class, new Object[] { u.getPkOrg(), u.getPkStore() });
		}
		map.put("dateBegin", dateBegin);
		return map;
	}

}
