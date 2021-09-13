package com.zebone.nhis.scm.pub.service;

import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.scm.st.PdSingle;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.module.scm.st.PdStock;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.scm.pub.dao.MtlPdStPubMapper;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 库存管理对外服务公共接口
 *
 * @author wj
 *
 */
@Service
public class MtlPdStPubService {

	@Resource
	private MtlPdStPubMapper pdstPubMapper;

	@Resource
	private MtlPdStOutHandler stOutHandler;


	/**
	 * 删除入库单
	 *
	 * @param param
	 *            {pkPdst}
	 * @param user
	 */
	public void deletePdst(String param, IUser user) {
		String pkPdst = JsonUtil.readValue(param, String.class);
		if (CommonUtils.isEmptyString(pkPdst))
			throw new BusException("未获取到待删除的入库单主键！");
		Object [] params = new Object[] { pkPdst };
		String del_sql_dt = "delete from pd_st_detail  where pd_st_detail.pk_pdst = ? and "
				+ " exists (select 1  from pd_st  "
				+ "   where pd_st_detail.pk_pdst=pd_st.pk_pdst and  pd_st.eu_status='0') ";
		DataBaseHelper.execute(del_sql_dt, params);
		String del_sql = " delete from pd_st where pk_pdst = ? and eu_status='0'";
		DataBaseHelper.execute(del_sql, params);
	}

	/**
	 * 更新入库单为审核状态
	 *
	 * @param paramMap
	 */
	public void updatePdSt(Map<String, Object> paramMap) {
		// 更新入库单状态
		if(StringUtils.isBlank(MapUtils.getString(paramMap,"euStatus")))
			paramMap.put("euStatus", EnumerateParameter.ONE);
		String update_sql = "update pd_st set eu_status=:euStatus,flag_chk='1',pk_emp_chk=:pkEmp,"
				+ "name_emp_chk=:nameEmp,date_chk=:dateChk,ts=:dateChk where pk_pdst = :pkPdst";
		DataBaseHelper.update(update_sql, paramMap);
	}

	/**
	 * 审核时更新入库库存
	 */
	public void updateInStore(List<PdStDtVo> dtlist, String pk_store,
			String pk_dept) {
		if (dtlist == null || dtlist.size() <= 0)
			throw new BusException("未获取到需要入库的物品明细信息！");
		List<PdStock> insertList = new ArrayList<PdStock>();
		List<PdStDtVo> resDtList=distinctStdtData(dtlist);
		for (PdStDtVo dt : resDtList) {// 更新
			Map<String, Object> stmap = verfiyExistPD(dt, pk_store, null);

			if (stmap != null && CommonUtils.isNotNull(stmap.get("pkPdstock"))) { // 更新
				Map<String, Object> updateMap = new HashMap<String, Object>();
				updateMap.put("quanMin", dt.getQuanMin());
				updateMap.put("pkPdstock", stmap.get("pkPdstock"));
				StringBuffer upSql=new StringBuffer();
				upSql.append("update pd_stock set quan_min = nvl(quan_min,0)+:quanMin");
				upSql.append(" ,amount_cost = amount_cost + ");
				upSql.append(dt.getAmountCost());
				upSql.append(" ,amount = amount +");
				upSql.append(dt.getAmount());
				upSql.append(" ,ts = to_date('");
				upSql.append(DateUtils.getDefaultDateFormat().format(new Date()));
				upSql.append("','YYYYMMDDHH24MISS')");
				upSql.append("  where pk_pdstock = :pkPdstock ");
				DataBaseHelper.update(upSql.toString(), updateMap);
			} else {
				PdStock st = new PdStock();
				st.setAmount(dt.getAmount());
				st.setAmountCost(dt.getAmountCost());
				st.setBatchNo(dt.getBatchNo());
				st.setDateExpire(dt.getDateExpire());
				st.setFlagStop("0");
				st.setFlagStopOp("0");
				st.setFlagStopEr("0");
				st.setPkDept(pk_dept);// 当前部门
				st.setPkOrg(dt.getPkOrg());
				st.setPkPd(dt.getPkPd());
				st.setPkStore(pk_store);
				st.setPrice(dt.getPrice());
				st.setPriceCost(dt.getPriceCost());
				st.setQuanMin(dt.getQuanMin());
				st.setQuanPrep(0.00);
				st.setPkPdstock(NHISUUID.getKeyId());
				ApplicationUtils.setBeanComProperty(st, true);
				insertList.add(st);
			}
		}
		if (insertList != null && insertList.size() > 0) {
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(PdStock.class), insertList);
		}
	}

	/**
	 * 汇总入库明细中药品信息中，成本价格，零售价格，批号，失效日期一致的药品信息
	 * @param dtlist
	 * @return
	 */
	private List<PdStDtVo> distinctStdtData(List<PdStDtVo> dtlist){
		List<PdStDtVo> resdtList=new ArrayList<PdStDtVo>();
		for (PdStDtVo instDtvo : dtlist) {
			boolean isAdd=true;
			for (PdStDtVo outstDt : resdtList) {
				if(CommonUtils.isNotNull(instDtvo.getPkPdstdt())&&!instDtvo.getPkPdstdt().equals(outstDt.getPkPdstdt())&&
						isSameBatch(instDtvo,outstDt)){
					mergreSameInfo(instDtvo,outstDt);
					isAdd=false;
				}else if(isSameBatch(instDtvo,outstDt)){
					mergreSameInfo(instDtvo,outstDt);
					isAdd=false;
				}
			}
			if(isAdd){
				resdtList.add(instDtvo);
			}
		}
		return resdtList;
	}

	/**
	 * 是否同一个批次
	 * @param instDtvo
	 * @param outstDt
	 * @return
	 */
	public Boolean isSameBatch(PdStDtVo instDtvo, PdStDtVo outstDt){
		int comDate=0;
		if(instDtvo.getDateExpire()!=null && outstDt.getDateExpire()!=null){
			comDate=instDtvo.getDateExpire().compareTo(outstDt.getDateExpire());
		}
		if(instDtvo.getPkPd().equals(outstDt.getPkPd())
				&&instDtvo.getBatchNo().equals(outstDt.getBatchNo())
				&&MathUtils.compareTo(instDtvo.getPrice(), outstDt.getPrice())==0
				&&MathUtils.compareTo(instDtvo.getPriceCost(), outstDt.getPriceCost())==0
				&&comDate==0){
			return true;
		}
		return false;
	}

	/**
	 * 合并同批次药品的金额/数量
	 * @param instDtvo
	 * @param outstDt
	 */
	public void mergreSameInfo(PdStDtVo instDtvo, PdStDtVo outstDt){
		double quanMin = MathUtils.add(outstDt.getQuanMin(),instDtvo.getQuanMin());
		outstDt.setQuanMin(quanMin);
		double amount = MathUtils.add(outstDt.getAmount(), instDtvo.getAmount());
		outstDt.setAmount(amount);
		double amountCost = MathUtils.add(outstDt.getAmountCost(), instDtvo.getAmountCost());
		outstDt.setAmountCost(amountCost);
	}

	/**
	 * 更新出库库存
	 *
	 * @param dtlist
	 * @param pk_store
	 */
	public void updateOutStore(List<PdStDtVo> dtlist, String pk_store) {
		List<String> update_list = new ArrayList<String>();
		for (PdStDtVo dt : dtlist) {
			// 更新库存表
			updateStock(dt, pk_store, dt.getQuanMin());
			// 更新原入库单的库存数量
			update_list.add(getOutUpdate(dt));
		}
		if (update_list != null && update_list.size() > 0) {
			DataBaseHelper.batchUpdate(update_list.toArray(new String[0]));
		}
	}

	private String getOutUpdate(PdStDtVo dt) {
		if (Application.isSqlServer()){
			return "update pd_st_detail set quan_outstore =isnull(quan_outstore,0)+"+
					dt.getQuanMin()+",flag_finish = (case when (isnull(quan_outstore,0)+"+
					dt.getQuanMin()+")=quan_min then '1' else '0' end) where pk_pdstdt='"+dt.getPkDtin()+"'";
		}

		return "update pd_st_detail set quan_outstore =nvl(quan_outstore,0)+"+
				dt.getQuanMin()+",flag_finish = (case when (nvl(quan_outstore,0)+"+
				dt.getQuanMin()+")=quan_min then '1' else '0' end) where pk_pdstdt='"+dt.getPkDtin()+"'";
	}

	/**
	 * 保存入库单
	 *
	 * @param user
	 * @param type
	 * @param euDirect
	 * @return
	 */
	public PdStVo savePdSt(PdStVo stvo, IUser user, String type, String euDirect) {
		if (stvo == null)
			throw new BusException("未获取到需要保存的单据！");
		PdSt st = new PdSt();
		ApplicationUtils.copyProperties(st, stvo);
		st.setDateChk(new Date());
		st.setPkStoreSt(((User)user).getPkStore());
		if (CommonUtils.isEmptyString(st.getPkPdst())) {// 新增
			if (!CommonUtils.isEmptyString(type)) {
				st.setDtSttype(type);
			}
			// st.setFlagChk("0");
			st.setFlagPay("0");
			DataBaseHelper.insertBean(st);
		} else {// 修改
			DataBaseHelper.updateBeanByPk(st, false);
		}

		List<PdStDtVo> dtlist = stvo.getDtlist();
		if (dtlist != null && dtlist.size() > 0) {
			List<PdStDetail> insert_list = new ArrayList<PdStDetail>();
			List<PdStDetail> update_list = new ArrayList<PdStDetail>();
			List<PdSingle> insert_Pdlist = new ArrayList<PdSingle>();
			for (PdStDtVo dt : dtlist) {
				//数据库存储零售单位对应的零售单价及成本单价
				if(dt.getPackSizePd() == null || dt.getPackSizePd().intValue() ==0){
					throw new BusException("没有传入物品包装量！");
				}
				if(dt.getPackSize() == null || dt.getPackSize().intValue() ==0){
					throw new BusException("没有传入仓库物品包装量！");
				}
				if(dt.getPackSizePd().intValue() != dt.getPackSize().intValue()){
					dt.setPrice(MathUtils.upRound(MathUtils.mul(MathUtils.div(dt.getPrice(), (double)dt.getPackSize()), (double)dt.getPackSizePd()),6));
					dt.setPriceCost(MathUtils.upRound(MathUtils.mul(MathUtils.div(dt.getPriceCost(), (double)dt.getPackSize()), (double)dt.getPackSizePd()),6));
				}
				// 判断是否是单品
				boolean isSingle = PdStDtVo.FLAG_SINGLE_T.equals(dt.getFlagSingle());
				if (CommonUtils.isEmptyString(dt.getPkPdstdt())) {// 新增
					dt.setPkPdst(st.getPkPdst());
					dt.setPkPdstdt(NHISUUID.getKeyId());
					dt.setFlagChkRpt("0");
					dt.setFlagFinish("0");
					dt.setFlagPay("0");
					dt.setDisc(Double.valueOf("1"));
					dt.setQuanOutstore(0.0);
					ApplicationUtils.setBeanComProperty(dt, true);
					insert_list.add(dt);
					// 如果物品为“单品”管理，基于入库明细记录更新表pd_single；字段pk_pdstdt_out对应的是出库明细。
					if (isSingle) {// 如果是单品
						insert_Pdlist.addAll(initPdSingle(stvo, user, euDirect, dt));
					}
				} else { // 修改
					update_list.add(dt);
					//单品有可能会变动，删了重新添加
					if(isSingle && CollectionUtils.isNotEmpty(dt.getSinList())){
						DataBaseHelper.execute("delete from pd_single where eu_status = '0' and " +
								(StringUtils.equals(euDirect,EnumerateParameter.NEGA)?"pk_pdstdt_out=?":"pk_pdstdt_in=?"), new Object[]{dt.getPkPdstdt()});
						insert_Pdlist.addAll(initPdSingle(stvo, user, euDirect, dt));
					}
				}
			}
			if (insert_list != null && insert_list.size() > 0) {
				DataBaseHelper.batchUpdate(
						DataBaseHelper.getInsertSql(PdStDetail.class),
						insert_list);
			}
			if (update_list != null && update_list.size() > 0) {
				DataBaseHelper.batchUpdate(
						DataBaseHelper.getUpdateSql(PdStDetail.class),
						update_list);
			}
			if (insert_Pdlist != null && insert_Pdlist.size() > 0) {
				if(StringUtils.equals(euDirect,EnumerateParameter.NEGA)){
					//出库修改单品记录
					for(PdSingle pdSingle:insert_Pdlist){
						DataBaseHelper.update("update pd_single set pk_pdstdt_out=?,flag_out='0' where pk_single=? and eu_status='1'"
								,new Object[]{pdSingle.getPkPdstdtOut(),pdSingle.getPkSingle()});
					}

				} else {
					DataBaseHelper.batchUpdate(
							DataBaseHelper.getInsertSql(PdSingle.class),
							insert_Pdlist);
				}
			}

			// 删除明细PD_ST_DETAIL
			List<Object[]> list = stvo.getDelDtList();
			if (CollectionUtils.isNotEmpty(list)) {
				DataBaseHelper.getJdbcTemplate().batchUpdate(
						"delete from pd_st_detail where pk_pdstdt = ? ",
						stvo.getDelDtList());
				//由于单品是和出入库明细对应的，那么在修改的时候，只需要处理对应关系
				DataBaseHelper.getJdbcTemplate().batchUpdate("delete from PD_SINGLE where eu_status = '0' and "+
						(StringUtils.equals(euDirect,EnumerateParameter.NEGA)?"pk_pdstdt_out=?":"pk_pdstdt_in=?"), stvo.getDelDtList());
			}
		}
		stvo.setPkPdst(st.getPkPdst());
		stvo.setDtlist(dtlist);
		return stvo;
	}

	/**
	 * 重构提取的初始化单品方法
	 * @param stvo
	 * @param user
	 * @param euDirect
	 * @param dt
	 */
	private List<PdSingle> initPdSingle(PdStVo stvo, IUser user, String euDirect, PdStDtVo dt) {
		List<PdSingle> pdSingleList = dt.getSinList();
		if(CollectionUtils.isEmpty(pdSingleList)){
			throw new BusException("明细中单品未选择单品记录！");
		}
		for (PdSingle psSingle : pdSingleList) {
			psSingle.setPkSingle(StringUtils.isNotBlank(psSingle.getPkSingle())?psSingle.getPkSingle():NHISUUID.getKeyId());
			psSingle.setPkOrg(dt.getPkOrg());
			psSingle.setPkDept(stvo.getPkDeptSt());// 所属科室
			psSingle.setPkStore(stvo.getPkStoreSt());// 所属仓库
			psSingle.setPkPd(dt.getPkPd());
			psSingle.setBarcode(psSingle.getBarcode());// 条码
			if (euDirect.equals("-1")) {// 出库
				psSingle.setFlagIn("0");// 入库标志
				psSingle.setFlagOut("0");// 出库标志
				psSingle.setPkPdstdtIn("");
				psSingle.setEuStatus("0");// 状态
				psSingle.setPkPdstdtOut(dt.getPkPdstdt());// 关联的出库明细
			}
			if (euDirect.equals("1")) {// 入库
				psSingle.setEuStatus("0");// 状态
				psSingle.setFlagIn("0");// 入库标志
				psSingle.setFlagOut("0");// 出库标志
				psSingle.setPkPdstdtOut("");
				psSingle.setPkPdstdtIn(dt.getPkPdstdt());// 关联的入库明细
			}
			psSingle.setCreator(user.getId());
			psSingle.setCreateTime(new Date());
			psSingle.setDelFlag("0");
			psSingle.setModifier(user.getId());
			psSingle.setModityTime(new Date());
		}

		return pdSingleList;
	}

	/**
	 * 退回入库单
	 *
	 * @param param
	 * @param user
	 */
	public void rtnPdst(String param, IUser user, String dttype) {
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		if (stvo == null)
			throw new BusException("未获取到需要退回的单据！");
		PdSt st = stOutHandler.createPdst(null, null, stvo, (User) user,
				dttype, "-1");
		DataBaseHelper.insertBean(st);
		// 生成明细
		Map<String, Double> mapQuan = transverterToMapQuan(stvo.getDtlist());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkPdst", stvo.getPkPdst());
		List<PdStDtVo> dtlist = pdstPubMapper.queryPdStDetailList(map);
		if (dtlist == null || dtlist.size() <= 0)
			return;
		List<PdStDetail> insert_list = new ArrayList<PdStDetail>();
		int i = 1;
		for (PdStDtVo dt : dtlist) {
			// 已退数量+要退数量 不能大于出库数量
			double quan = dt.getQuanMin() ==0?dt.getQuanPack():dt.getQuanMin();//原入库数量
			double rest = MathUtils.sub(quan, dt.getQuanOutstore() == null ? 0 : dt.getQuanOutstore());
			if(MapUtils.getDoubleValue(mapQuan, dt.getPkPdstdt(), 0) > rest){
				throw new BusException(String.format("物品：%s,最多只能再退%s", dt.getPdname(), rest));
			}

			// 退库只能修改数量，所以这里变更为传入的数量
			dt.setQuanMin(mapQuan.get(dt.getPkPdstdt()));
			PdStDetail stdt = stOutHandler.createPdstdt(null, dt, (User) user,
					st.getPkPdst(), i); // 退货单明细
			i++;

			// 更新库存表
			updateStock(dt, stvo.getPkStoreSt(), dt.getQuanMin());
			// 更新原入库单的库存数量
			dt.setQuanOutstore(MathUtils.add(
					dt.getQuanOutstore() == null ? 0 : dt.getQuanOutstore(),
							dt.getQuanMin() == null ? 0 : dt.getQuanMin()));
			if (MathUtils.equ(quan, dt.getQuanOutstore())) {// 出库数量==退回数量
				dt.setFlagFinish("1");
			}
			DataBaseHelper.update("UPDATE PD_ST_DETAIL SET quan_outstore =?,flag_finish=? WHERE PK_PDSTDT=?"
					,new Object[]{dt.getQuanOutstore(), dt.getFlagFinish(), dt.getPkPdstdt()});

			insert_list.add(stdt);
		}
		mapQuan.clear();
		if (insert_list != null && insert_list.size() > 0) {
			// 插入新的入库单
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(PdStDetail.class), insert_list);
		}
	}

	/**
	 * 根据入库单主键查询入库明细
	 *
	 * @param param
	 *            {pkPdst}
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PdStDtVo> queryPdStDtList(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if (map == null || CommonUtils.isNull(map.get("pkPdst")))
			throw new BusException("入库单主键为空，无法获取明细！");
		List<PdStDtVo> list = pdstPubMapper.queryPdStDetailList(map);
		return list;
	}

	/**
	 * 更新出库库存量
	 *
	 * @param dt
	 * @param pk_store
	 * @param quan_min_out
	 */
	private void updateStock(PdStDtVo dt, String pk_store, Double quan_min_out) {
		Map<String, Object> stmap = verfiyExistPD(dt, pk_store, "0");
		if (stmap == null
				|| CommonUtils.isNull(CommonUtils.getString(stmap
						.get("pkPdstock"))))
			throw new BusException("未查询到物品"+dt.getPdname()+"匹配的库存记录,请核对后重新操作！");

		String pk_pdstock = CommonUtils.getString(stmap.get("pkPdstock"));
		// 如果退货数量和库存量相等，并且quan_prep=0，删除该条库存记录，否则更新库存量
		Double quan_min = CommonUtils.getDouble(stmap.get("quanMin"));
		Double quan_prep = CommonUtils.getDouble(stmap.get("quanPrep"));
		double quanCurrent = MathUtils.add(quan_min, quan_prep);
		if(MathUtils.sub(quanCurrent, dt.getQuanMin()) <0) {
			throw new BusException(String.format("物品:%s,当前批次库存数量:%s,小于要出数量，不能完成操作！", dt.getPdname(), quanCurrent));
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (MathUtils.equ(MathUtils.sub(quan_min, quan_min_out), 0.00)
				&& MathUtils.equ(quan_prep, 0.00)) {
			DataBaseHelper.execute(
					"delete from pd_stock where pk_pdstock = ? ",
					new Object[] { pk_pdstock });
		} else {
			paramMap.put("quan", quan_min_out);
			paramMap.put("pkPdstock", pk_pdstock);
			paramMap.put("dateNow", new Date());
			paramMap.put("amountP", dt.getAmount());
			paramMap.put("amountCostP", dt.getAmountCost());
			DataBaseHelper
					.update("update pd_stock set quan_min =  quan_min - :quan, "
							+ "amount =amount - :amountP,amount_cost = amount_cost - :amountCostP ,ts = :dateNow where pk_pdstock = :pkPdstock ",
							paramMap);
		}
	}

	/**
	 * 校验库存表是否存在匹配的库存记录
	 *
	 * @param dt
	 * @param pk_store
	 * @return
	 */
	public Map<String, Object> verfiyExistPD(PdStDtVo dt, String pk_store,
			String flagStop) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPd", dt.getPkPd());
		paramMap.put("batchNo", dt.getBatchNo());
		if (dt.getDateExpire() != null) {
			paramMap.put("dateExpire",
					DateUtils.getDefaultDateFormat().format(dt.getDateExpire())
							.substring(0, 8));
		}
		//库里面存入时四舍五入6位，这里按照四舍五入4位去获取批次
		paramMap.put("priceCost", MathUtils.upRound(dt.getPriceCost(), 4));
		paramMap.put("price", MathUtils.upRound(dt.getPrice(),4));
		paramMap.put("pkStore", pk_store);// 当前仓库
		Map<String, Object> stmap = pdstPubMapper.queryPkPdStoreByCon(paramMap);
		return stmap;
	}


	private Map<String, Double> transverterToMapQuan(List<PdStDtVo> dtlist){
		Map<String, Double> map = new HashMap<String, Double>();;
		if(CollectionUtils.isNotEmpty(dtlist)){
			for(PdStDtVo vo:dtlist)
				map.put(vo.getPkPdstdt(), (vo.getQuanMin()==null||vo.getQuanMin()==0)?vo.getQuanPack():vo.getQuanMin());
		}
		return map;
	}

	/**
	 * 依据出入库单据主键，更新对应单品状态
	 * @param direct
	 * @param pkPdst
	 * @param euStatus
	 * @param InOut
	 */
	public void updatePdSingleStatus(String direct, String pkPdst,String euStatus,boolean InOut){
		StringBuilder sql = new StringBuilder("update pd_single set eu_status=:euStatus");
		String str = "";
		if(EnumerateParameter.ONE.equals(direct)){
			sql.append(",flag_in=:flagInOut");
			str = "pk_pdstdt_in";
		} else if(EnumerateParameter.NEGA.equals(direct)){
			str = "pk_pdstdt_out";
			sql.append(",flag_out=:flagInOut");
		} else{
			throw new BusException("修改单品状态时传入出入库方向不正确");
		}
		sql.append(" where ").append(str);
		sql.append(" in(select dt.PK_PDSTDT from PD_ST_DETAIL dt where dt.pk_pdst =:pkPdst) ");
		Map<String,Object> param = Maps.newHashMap();
		param.put("pkPdst", pkPdst);
		param.put("euStatus", euStatus);
		param.put("flagInOut", InOut?EnumerateParameter.ONE:EnumerateParameter.ZERO);
		DataBaseHelper.update(sql.toString(), param);
	}
}
