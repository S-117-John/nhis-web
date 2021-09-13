package com.zebone.nhis.scm.pub.service;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.module.scm.st.PdStock;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.dao.PdStPubMapper;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 单个物品确认批次模式出库服务
 * @author yangxue
 * 
 */
@Service
public class PdStOutPubService {

	@Resource
	private PdStOutHandler stOutHandler;

	@Resource
	private PdStPubMapper pdstPubMapper;
	
	

	/**
	 * 药房发药执行出库
	 * @param pdList
	 * @param pk_dept
	 * @param pk_store
	 * @param eu_outtype
	 * @return PdOutDtParamVo:出库明细
	 */
	public List<PdOutDtParamVo> execStOut(List<PdOutParamVo> pdList, String pk_dept, String pk_store, String eu_outtype) {

		boolean flag_pd = pdList == null || pdList.size() <= 0;
		if (flag_pd)
			throw new BusException("未获取到需要出库的数据！");
		if (CommonUtils.isEmptyString(pk_store))
			throw new BusException("未获取到出库仓库主键！");
		// 确定出库方式
		if (CommonUtils.isEmptyString(eu_outtype)) {
			eu_outtype = stOutHandler.queryEuOutType(pk_store);
		}
		List<PdOutDtParamVo> dtlist = new ArrayList<PdOutDtParamVo>();// 出库明细
		List<String> pkPds = new ArrayList<String>();
		for(int i = 0 , len = pdList.size() ; i < len ; i++){
			pkPds.add(pdList.get(i).getPkPd());
		}
		stOutHandler.lockConfirmPdSt(pkPds, pk_store);

		for (PdOutParamVo paramVo : pdList) {
			String pk_pd = paramVo.getPkPd();
			if(paramVo.getQuanMin()<=0)
				throw new BusException("药品"+ stOutHandler.getPdNameByPk(pk_pd) +"的基本单位申请数量小于或等于0，无法进行出库！");
			// 校验库存是否够,不够取下一条物品
			double quan = getStoreNum(pk_pd, pk_store);
			if (quan < paramVo.getQuanMin())
				throw new BusException("物品" + stOutHandler.getPdNameByPk(pk_pd) + "库存量不足，现库存数量为"
						+ MathUtils.div(quan, CommonUtils.getDouble(paramVo.getPackSize())));
			List<PdOutDtParamVo> list = stOutHandler.confirmPdSt(pk_pd, pk_dept, pk_store, paramVo.getQuanMin(), eu_outtype, paramVo.getPkPv(),
					paramVo.getPkCnOrd(), paramVo.getPkPdapdt());
			dtlist.addAll(list);
		}

		if (dtlist != null && dtlist.size() > 0) {
			// 创建出库单，并更新库存
			List<PdOutDtParamVo> list = processStoreOut(dtlist, pk_store, IDictCodeConst.DT_STTYPE_ORDOUT);// 医嘱出库
			return list;
		}
		return null;
	}

	/**
	 * 处理库存 1）写表pd_st,pd_st_detail，生成出库记录
	 * 2）更新pd_st_detail对应的入库记录；更新已出库数量quan_outstore=quan_outstore+出库数量；
	 * 如果入库数量quan_min=quan_outstore，更新完成出库flag_finish=1； 3） 写表pd_stock；
	 * 按照“仓库+物品+批号+失效日期+成本单价+零售单价”查询，查询到匹配记录后，
	 * 更新库存数量quan_min=quan_min-对应批次数量，如果quan_min=0，且quan_prep=0，删除该库存记录。
	 * @param dtlist
	 *            ：出库明细
	 * @param pk_store
	 *            ：出库仓库
	 * @param dttype
	 *            :出库类型
	 * @return
	 */
	public List<PdOutDtParamVo> processStoreOut(List<PdOutDtParamVo> dtlist, String pk_store, String dttype) {

		User user = UserContext.getUser();
		// 查询是否当天生成过的发药出库单
		String pk_pdst = stOutHandler.getPdStIn(null,null,dttype, user, pk_store, "-1");
		if (CommonUtils.isEmptyString(pk_pdst)) {
			// 创建出入库单
			PdSt st = stOutHandler.createPdst(null,null,null, user, dttype, "-1");
			DataBaseHelper.insertBean(st);
			pk_pdst = st.getPkPdst();
		}

		List<PdStDetail> insert_list = new ArrayList<PdStDetail>();
		List<String> update_list = new ArrayList<String>();
		int i = 1;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (PdOutDtParamVo dt : dtlist) {
			// 更新库存表
			Map<String, Object> stmap = verfiyExistPD(dt, pk_store, "0");
			if (stmap == null)
				continue;
			String pk_pdstock = CommonUtils.getString(stmap.get("pkPdstock"));
			if (CommonUtils.isNull(pk_pdstock)) {
				// msg_list.add("未查询到物品主键为"+dt.getPkPd()+"匹配的库存记录!");
				continue;
			}
			PdStDetail stdt = stOutHandler.createPdstdt(dt, null, user, pk_pdst, i);
			i++;
			
			// 如果出库数量和库存量相等，并且quan_prep=0，删除该条库存记录，否则更新库存量
			Double quan_min = CommonUtils.getDouble(stmap.get("quanMin"));
			Double quan_prep = CommonUtils.getDouble(stmap.get("quanPrep"));
			if (MathUtils.equ(MathUtils.sub(quan_min, dt.getQuanOutMin()), 0.00) && MathUtils.equ(quan_prep, 0.00)) {
				DataBaseHelper.execute("delete from pd_stock where pk_pdstock = ? ", new Object[] { pk_pdstock });
			} else {
				paramMap.put("quan", dt.getQuanOutMin());
				paramMap.put("pkPdstock", pk_pdstock);
				paramMap.put("packSize", stOutHandler.getPackSize(dt.getPkPd()));
				paramMap.put("dateNow", new Date());
				DataBaseHelper.update("update pd_stock set quan_min =  quan_min - :quan " + ",amount =  (quan_min - :quan)/:packSize*price "
						+ ",amount_cost =  (quan_min - :quan)/:packSize*price_cost " + ",ts = :dateNow " + "where pk_pdstock = :pkPdstock ",
						paramMap);
			}
			// 更新原入库单的库存数量
			String pk_stdt = dt.getPkPdstdt();// 原入库单主键
			String update_sql = "update pd_st_detail set quan_outstore = nvl(quan_outstore,0) + " + dt.getQuanOutMin().doubleValue() + ",ts = to_date('"
					+ DateUtils.getDefaultDateFormat().format(new Date()) + "','YYYYMMDDHH24MISS')"
					+ ",flag_finish=(case when (nvl(quan_outstore,0)+"+dt.getQuanOutMin()+")>=quan_min then '1' else '0' end) where pk_pdstdt = '" + pk_stdt + "' and quan_min>=nvl(quan_outstore,0)+"+ dt.getQuanOutMin();
			update_list.add(update_sql);
			dt.setPkPdstdt(stdt.getPkPdstdt());// 更新为出库明细主键返回发药明细
			insert_list.add(stdt);
		}
		if (insert_list != null && insert_list.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), insert_list);
		}
		if (update_list != null && update_list.size() > 0) {
			DataBaseHelper.batchUpdate(update_list.toArray(new String[0]));
		}
		return dtlist;
	}

	/**
	 * 获取物品的库存量
	 * @param pk_pd
	 * @param pk_store
	 * @return
	 */
	public Double getStoreNum(String pk_pd, String pk_store) {

		String sql = "select sum(quan_min-quan_prep) as quan_min from pd_stock  where pk_pd = ? and pk_store= ? and flag_stop = '0' ";
		Map<String, Object> map = DataBaseHelper.queryForMap(sql, new Object[] { pk_pd, pk_store });
		double quan = 0;
		if (map != null) {
			quan = CommonUtils.getDouble(map.get("quanMin"));
		}
		return quan;
	}

	/**
	 * 预留物品
	 * @param pk_pd
	 *            -- 物品列表
	 * @param pk_dept
	 *            -- 物品所属部门
	 * @param pk_store
	 *            -- 物品所属仓库
	 * @param useType
	 *            -- 预留类型：0住院，1门诊，2急诊
	 */
	public void setPdPrepNum(List<PdOutParamVo> outList,String pk_store,String pk_dept,String useType) {

		if(outList == null || outList.size()<=0)
			throw new BusException("未获取到需要设置预留量的药品列表!");
		if(CommonUtils.isEmptyString(pk_store)){
			if(CommonUtils.isEmptyString(pk_dept))
				throw new BusException("未获取到物品存放的科室和仓库，无法设置预留！");
			else
				pk_store = stOutHandler.getStoreByDept(pk_dept);
		}
		if(CommonUtils.isEmptyString(pk_store))
			throw new BusException("未获取到适合的仓库信息，无法设置预留！");
		List<String> pkPds = new ArrayList<String>();
		for(PdOutParamVo vo:outList){
			if(vo.getQuanMin().doubleValue()>0)
			   pkPds.add(vo.getPkPd());
			else 
			  throw new BusException("物品" + stOutHandler.getPdNameByPk(vo.getPkPd())+"需要设置的预留数量必须大于0！");
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkPds", pkPds);
		paramMap.put("pkStore", pk_store);
		//查询库存量sql
		StringBuilder sqlstore = new StringBuilder("select sum(quan_min-quan_prep) as total,pk_pd from pd_stock where pk_pd in (:pkPds) and pk_store = :pkStore   ");
		//查询库存批次sql
		StringBuilder sql = new StringBuilder("select pk_pdstock,quan_min-quan_prep as quan_min,pk_pd from pd_stock where pk_pd in (:pkPds) and pk_store = :pkStore  and quan_min - quan_prep >0"); 
		if("0".equals(useType)){//住院预留
			sqlstore.append(" and flag_stop = '0' ");
			sql.append(" and flag_stop = '0' ");
		}else if("1".equals(useType)){//门诊预留
			sqlstore.append(" and flag_stop_op = '0' ");
			sql.append(" and flag_stop_op = '0' ");
		}else if("2".equals(useType)){//急诊预留
			sqlstore.append(" and flag_stop_er = '0' ");
			sql.append(" and flag_stop_op = '0' ");
		}
		sqlstore.append(" group by pk_pd"); 
		List<Map<String,Object>> totallist = DataBaseHelper.queryForList(sqlstore.toString(),paramMap);
		if(totallist == null || totallist.size()<=0){
			String msg="";
			for(PdOutParamVo vo:outList){
				msg+=stOutHandler.getPdNameByPk(vo.getPkPd())+",";
			}
			throw new BusException("物品" + msg+"可用库存量不足!");
		}
		//校验库存
		String message="";
		for(Map<String,Object> totalMap : totallist){
			Double sum = CommonUtils.getDouble(totalMap.get("total"));
			String pk_pd = CommonUtils.getString(totalMap.get("pkPd"));
			for(PdOutParamVo outvo:outList){
				if(outvo.getPkPd().equals(pk_pd)&&(sum.doubleValue()<=0||outvo.getQuanMin().doubleValue()>sum.doubleValue()))
					message+="物品【" + stOutHandler.getPdNameByPk(pk_pd) + "】可用库存量不足!\n";
			}
		}
		if(message.length()>0){
			throw new BusException(message);
		}
		//锁定预留量字段
		stOutHandler.lockStorePrepNum(paramMap," quan_min - quan_prep >0 ");
		
		//确认批次设置除预留量
		sql.append(" order by pk_pd,quan_min-quan_prep desc");
		List<PdStock> result = DataBaseHelper.queryForList(sql.toString(),PdStock.class,paramMap);
		if (result != null && result.size() > 0) {
			List<String> sqllist = new ArrayList<String>();
			for(PdOutParamVo outvo:outList){
				for (PdStock stk : result) {
					String pk_pd = stk.getPkPd();
					if(outvo.getPkPd().equals(pk_pd)&&outvo.getQuanMin().doubleValue()>0){//需要设置的预留量大于0的情况才设置预留
						Double quan_min = stk.getQuanMin();
						String pk_pdstock = stk.getPkPdstock();
						if (quan_min.doubleValue() >= outvo.getQuanMin().doubleValue()) {
							String update_sql = "update pd_stock set quan_prep = quan_prep + " + outvo.getQuanMin() + " where pk_pdstock = ? ";
							DataBaseHelper.update(update_sql, new Object[]{pk_pdstock});
							outvo.setQuanMin(new Double(0));//全部设置完毕
							break;
						} else {
							sqllist.add("update pd_stock set quan_prep = quan_prep + " + quan_min + " where pk_pdstock = '" + pk_pdstock + "' ");
							outvo.setQuanMin(new Double(outvo.getQuanMin().doubleValue() - quan_min.doubleValue()));
						}
					}
				}
			}

			if (sqllist.size() > 0) {
				DataBaseHelper.batchUpdate(sqllist.toArray(new String[0]));
			}
		} else {
			throw new BusException("该批物品在仓库中无可用库存，无法设置预留!");
		}
	}

	/**
	 * 解除预留物品
	 * @param pk_pd
	 *            -- 物品主键
	 * @param pk_dept
	 *            -- 物品所属部门
	 * @param quan
	 *            -- 基本单位下需要解除的预留数量
	 * @param useType
	 *            -- 预留类型：0住院，1门诊，2急诊
	 */
	public void setPdUnPrepNum(List<PdOutParamVo> outList,String pk_store,String pk_dept,String useType) {

		if(outList == null || outList.size()<=0)
			throw new BusException("未获取到需要解除预留量的药品列表!");
		if(CommonUtils.isEmptyString(pk_store)){
			if(CommonUtils.isEmptyString(pk_dept))
				throw new BusException("未获取到物品存放的科室和仓库，无法解除预留！");
			else
				pk_store = stOutHandler.getStoreByDept(pk_dept);
		}
		if(CommonUtils.isEmptyString(pk_store))
			throw new BusException("未获取到适合的仓库信息，无法解除预留！");
		List<String> pkPds = new ArrayList<String>();
		for(PdOutParamVo vo:outList){
				if(vo.getQuanMin().doubleValue()>0)
				   pkPds.add(vo.getPkPd());
				else 
				  throw new BusException("物品" + stOutHandler.getPdNameByPk(vo.getPkPd())+"需要解除的预留数量必须大于0！");
		}
		//锁定预留量字段
		Map<String,Object> paramMap = new HashMap<String,Object>();
		String a="";
		paramMap.put("pkPds", pkPds);
		paramMap.put("pkStore", pk_store);
		stOutHandler.lockStorePrepNum(paramMap," quan_prep >0 ");
		//确认批次并扣除预留量
		StringBuilder sql = new StringBuilder("select pk_pdstock,quan_prep,pk_pd from pd_stock where pk_pd  in (:pkPds) and pk_store = :pkStore  and  quan_prep > 0 ");
		if("0".equals(useType)){//住院预留
			sql.append(" and flag_stop = '0' ");
		}else if("1".equals(useType)){//门诊预留
			sql.append(" and flag_stop_op = '0' ");
		}else if("2".equals(useType)){//急诊预留
			sql.append(" and flag_stop_op = '0' ");
		}
		sql.append(" order by pk_pd,quan_prep desc");
		List<PdStock> result = DataBaseHelper.queryForList(sql.toString(),PdStock.class,paramMap);
		if (result != null && result.size() > 0) {
			List<String> sqllist = new ArrayList<String>();
			for(PdOutParamVo outvo:outList){
				for (PdStock stk : result) {
					String pk_pd = stk.getPkPd();
					if(outvo.getPkPd().equals(pk_pd)&&outvo.getQuanMin().doubleValue()>0){//需要设置的预留量大于0的情况才设置预留
						Double quan_prep = stk.getQuanPrep();
						String pk_pdstock = stk.getPkPdstock();
						if (quan_prep.doubleValue() >= outvo.getQuanMin().doubleValue()) {
							String update_sql = "update pd_stock set quan_prep = quan_prep - " + outvo.getQuanMin() + " where pk_pdstock = ? ";
							DataBaseHelper.update(update_sql, new Object[]{pk_pdstock});
							outvo.setQuanMin(new Double(0));//全部设置完毕
							break;
						} else {
							sqllist.add("update pd_stock set quan_prep = quan_prep - " + quan_prep + " where pk_pdstock = '" + pk_pdstock + "' ");
							outvo.setQuanMin(new Double(outvo.getQuanMin().doubleValue() - quan_prep));
						}
					}
				}
			}

			if (sqllist.size() > 0) {
				DataBaseHelper.batchUpdate(sqllist.toArray(new String[0]));
			}
		} else {
			//throw new BusException("该批物品在仓库中无可用预留量，无法解除预留!");
		}
		
	}

	/**
	 * 门诊医生站处方保存用批量询价
	 * @param storePriceMap
	 * key:pkStore, value:执行科室对应的仓库，尽量传，减少查询
	 * key:pkDeptExec,value:执行科室，只能是一个执行科室,必须传
	 * key:quanMap,value:map<药品主键，基本单位下的用量>，必须传
	 * key:pkPd,value:map<pkPd,List<String>>，必须传
	 * key:packSize,value:仓库单位下的包装量，不必须
	 * @param isBase,是否是基数药询价，必须传
	 */
	public List<PdOutDtParamVo> getPdStorePrice(Map<String,Object> storePriceMap,boolean isBase){
		//查询仓库，可以传参，减少查询
		String pkStore = storePriceMap.get("pkStore").toString();
		if(CommonUtils.isEmptyString(pkStore)){
			pkStore = stOutHandler.getStoreByDept(storePriceMap.get("pkDeptExec").toString());
			storePriceMap.put("pkStore", pkStore);
		}
		//判断库存----//基数药只判断是否有库存，不判断用量
		List<Map<String,Object>> storeNumList= pdstPubMapper.qryStockNum(storePriceMap);
		if(storeNumList.size()==0){
			throw new BusException("询价时未获取到药品库存信息");
		}
		Map<String,Object> quanMap = (Map<String, Object>) storePriceMap.get("quanMap");
		Optional<Map<String, Object>> opValid = storeNumList.parallelStream().filter(m -> isBase?MapUtils.getDoubleValue(m, "quanMin")<=0
				:(MapUtils.getDoubleValue(m, "quanMin") < MapUtils.getDoubleValue(quanMap, MapUtils.getString(m, "pkPd")))).findFirst();
		if(opValid.isPresent()){
			throw new BusException("物品 "+stOutHandler.getPdNameByPk(MapUtils.getString(opValid.get(),"pkPd"))
					+ (isBase?"暂时无库存，无法获取价格！":" 库存量不足，现库存数量为"+MathUtils.div(MapUtils.getDoubleValue(opValid.get(), "quanMin"), MapUtils.getDoubleValue(opValid.get(), "packSize"))));
		}
		storePriceMap.put("euOuttype", MapUtils.getString(storeNumList.get(0),"euOuttype"));
        //询价
		List<PdOutDtParamVo> list = stOutHandler.confirmPdSt(storePriceMap); 
	    return list;
	}
	/**
	 *询价
	 * @param pk_dept
	 * @param pk_store
	 * @param pk_pd
	 * @param quan --基本单位下的数量
	 * @param pack_size --仓库单位下的包装量
	 * @param isBase -- 是否是基数药询价
	 * @return
	 */
	public List<PdOutDtParamVo> getPdStorePrice(String pk_dept,String pk_store,String pk_pd,Double quan,Integer pack_size,boolean isBase){
		if(CommonUtils.isEmptyString(pk_store)){
			pk_store = stOutHandler.getStoreByDept(pk_dept);
		}
		double quanSt = getStoreNum(pk_pd,pk_store);
		if(quanSt<quan&&!isBase){
			String sql = " select cvt.pack_size  from bd_pd_store  store "+
			 " inner join  bd_pd_convert cvt on cvt.pk_pdconvert = store.pk_pdconvert  where store.pk_pd = '"+pk_pd+"' and store.pk_store = '"+pk_store+"'";
			Integer packSize = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{});
			throw new BusException("物品 "+stOutHandler.getPdNameByPk(pk_pd)+" 库存量不足，现库存数量为"+MathUtils.div(quanSt, CommonUtils.getDouble(packSize)));
		}else if(quanSt<=0&&isBase){//基数药只判断是否有库存，不判断用量
			throw new BusException("物品 "+stOutHandler.getPdNameByPk(pk_pd)+" 暂时无库存，无法获取价格！");
		}
		List<PdOutDtParamVo> list = stOutHandler.confirmPdSt(pk_pd,pk_dept,pk_store,quan,"","","","");
	    return list;
	}
	/**
	 * 校验库存表是否存在匹配的库存记录
	 * @param 匹配条件
	 *            paramMap{pkPd，batchNo，dateExpire，priceCost，price，pkStore,
	 *            flagStop}
	 * @param pk_store
	 * @return
	 */
	private Map<String, Object> verfiyExistPD(PdOutDtParamVo dt, String pk_store, String flagStop) {

		Map<String, Object> verfyMap = new HashMap<String, Object>();
		verfyMap.put("pkPd", dt.getPkPd());
		verfyMap.put("batchNo", dt.getBatchNo());
		if (dt.getDateExpire() != null) {
			verfyMap.put("dateExpire", DateUtils.getDefaultDateFormat().format(dt.getDateExpire()).substring(0, 8));
		}
		verfyMap.put("priceCost", dt.getPriceCost());
		verfyMap.put("price", dt.getPrice());
		verfyMap.put("pkStore", pk_store);
		//verfyMap.put("flagStop", flagStop); 停用标志全部去掉
		List<Map<String, Object>> stmap = pdstPubMapper.queryPkPdStoreByCon(verfyMap);
		if(stmap!=null&&stmap.size()>0)
			return stmap.get(0);
		return null;
	}

}
