package com.zebone.nhis.scm.pub.service;

import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.module.scm.st.PdStock;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.dao.PdStPubMapper;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 库存管理对外服务公共接口
 * @author yangxue
 *
 */
@Service
public class PdStPubService {
	@Resource
	private PdStPubMapper pdstPubMapper;
	@Resource
	private PdStOutHandler stOutHandler;
	/**
	 * 删除入库单
	 * @param param{pkPdst}
	 * @param user
	 */
	public void deletePdst(String param,IUser user){
		String pkPdst = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pkPdst)) return;
		String del_sql_dt = "delete from pd_st_detail  where pd_st_detail.pk_pdst=? and "+
				" exists (select 1  from pd_st  "+
                 "   where pd_st_detail.pk_pdst=pd_st.pk_pdst and  pd_st.eu_status='0') ";
		DataBaseHelper.execute(del_sql_dt, new Object[]{pkPdst});
		String del_sql = " delete from pd_st where pk_pdst = ? and eu_status='0'";
		DataBaseHelper.execute(del_sql, new Object[]{pkPdst});
 	}
	/**
	 * 更新入库单为审核状态
	 * @param paramMap
	 */
	public void updatePdSt(Map<String,Object> paramMap){
		//更新入库单状态
		String update_sql = "update pd_st set eu_status='1',flag_chk='1',pk_emp_chk=?,"
						+ "name_emp_chk=?,date_chk=?,ts=? where pk_pdst = ? and eu_status='0'";
		 int count=DataBaseHelper.execute(update_sql, new Object[]{paramMap.get("pkEmp"),paramMap.get("nameEmp"),paramMap.get("dateChk"),paramMap.get("dateChk"),paramMap.get("pkPdst")});
		 if(count!=1){
			 throw new BusException("您所提交的数据已发生改变，请刷新数据，或返回初始列表");
		 }
	}
	/**
	 * 审核时更新入库库存
	 */
	public void updateInStore(List<PdStDtVo> dtlist,String pk_store,String pk_dept){
		if(dtlist == null || dtlist.size() <= 0) throw new BusException("未获取到需要入库的物品明细信息！");
		List<PdStock> insert_list = new ArrayList<PdStock>();
		List<PdStDtVo> resDtList=distinctStdtData(dtlist);
		for(PdStDtVo dt:resDtList){//更新
			Map<String,Object> stmap = verfiyExistPD(dt,pk_store,null);
		    if(stmap!=null&&CommonUtils.isNotNull(stmap.get("pkPdstock"))){//插入
		    	Map<String,Object> update_map = new HashMap<String,Object>();
		    	update_map.put("quanMin", dt.getQuanMin());
		    	update_map.put("pkPdstock", stmap.get("pkPdstock"));
		    	String update_sql = "update pd_stock set quan_min = nvl(quan_min,0)+:quanMin,"
		    			+ "amount_cost = amount_cost + "+dt.getAmountCost()+","
		    			+ "ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS'),"
		    			+ "amount = amount +"+dt.getAmount()+" where pk_pdstock = :pkPdstock ";
		    	DataBaseHelper.update(update_sql, update_map);
		    }else{	
		    	PdStock st = new PdStock();
		    	st.setAmount(dt.getAmount());
		    	st.setAmountCost(dt.getAmountCost());
		    	st.setBatchNo(dt.getBatchNo());
		    	st.setDateExpire(dt.getDateExpire());
		    	st.setFlagStop("0");
		    	st.setFlagStopOp("0");
		    	st.setFlagStopEr("0");
		    	st.setPkDept(pk_dept);//当前部门
		    	st.setPkOrg(dt.getPkOrg());
		    	st.setPkPd(dt.getPkPd());
		    	st.setPkStore(pk_store);
		    	st.setPrice(dt.getPrice());
		    	st.setPriceCost(dt.getPriceCost());
		    	st.setQuanMin(dt.getQuanMin());
		    	st.setQuanPrep(0.00);
		    	st.setPkPdstock(NHISUUID.getKeyId());
		    	ApplicationUtils.setBeanComProperty(st, true);
		    	insert_list.add(st);
		    }
		}
		if(insert_list!=null&&insert_list.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStock.class), insert_list);
		}
	}
	
	/**
	 * 汇总入库明细中药品信息中，成本价格，零售价格，批号，失效日期一致的药品信息
	 * @param dtlist
	 * @return
	 */
	private List<PdStDtVo> distinctStdtData(List<PdStDtVo> dtlist){
		List<PdStDtVo> resdtList=new ArrayList<PdStDtVo>();
		Map<String,PdStDtVo> keyPddts=new HashMap<String,PdStDtVo>();
		for (PdStDtVo instDtvo : dtlist) {
			//将界面上的单价转换成零售单位对应的单价进行入库
			instDtvo.setPrice(MathUtils.mul(MathUtils.div(instDtvo.getPrice(),CommonUtils.getDouble(instDtvo.getPackSize())),CommonUtils.getDouble(instDtvo.getPackSizePd())));
			instDtvo.setPriceCost(MathUtils.mul(MathUtils.div(instDtvo.getPriceCost(),CommonUtils.getDouble(instDtvo.getPackSize())),CommonUtils.getDouble(instDtvo.getPackSizePd())));
			//四舍五入保留六位小数--yangxue 2019/6/13
			instDtvo.setPrice(new BigDecimal(instDtvo.getPrice()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			instDtvo.setPriceCost(new BigDecimal(instDtvo.getPriceCost()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			String keyPddt=instDtvo.getPkPd()+"-"+instDtvo.getBatchNo()+"-"+instDtvo.getPrice()+"-"+instDtvo.getPriceCost()+"-"+instDtvo.getDateExpire();
			if(keyPddts.keySet().contains(keyPddt)){
				//quanPack
				double quanPackBef=keyPddts.get(keyPddt).getQuanPack();
				double quanPack=MathUtils.add(quanPackBef,instDtvo.getQuanPack());
				//quanMin 
				double quanBef=keyPddts.get(keyPddt).getQuanMin();
				double quanMin=MathUtils.add(quanBef,instDtvo.getQuanMin());
				//成本金额
				double amountCostBef=keyPddts.get(keyPddt).getAmountCost();
				double amountCost=MathUtils.add(amountCostBef,instDtvo.getAmountCost());
				//零售金额
				double amountBef=keyPddts.get(keyPddt).getAmount();
				double amount=MathUtils.add(amountBef,instDtvo.getAmount());
				keyPddts.get(keyPddt).setQuanPack(quanPack);
				keyPddts.get(keyPddt).setQuanMin(quanMin);
				keyPddts.get(keyPddt).setAmount(amount);
				keyPddts.get(keyPddt).setAmountCost(amountCost);
			}else{
				keyPddts.put(keyPddt, instDtvo);
			}
		}
		for (PdStDtVo pdStDtVo : keyPddts.values()) {
			resdtList.add(pdStDtVo);
		}
		return resdtList;
	}
	/**
	 * 
	 * @param dtlist
	 * @param pk_store
	 * @param pk_store
	 */
	public void updateOutStore(List<PdStDtVo> dtlist,String pk_store){
		List<String> update_list = new ArrayList<String>();
		for(PdStDtVo dt:dtlist) {
			//更新库存表
			updateStock(dt, pk_store, dt.getQuanMin());
			//更新原入库单的库存数量
			String pk_stdt = dt.getPkDtin();
			String update_sql=null;
			if(Application.isSqlServer()){
				 update_sql = "update pd_st_detail set quan_outstore = isnull(quan_outstore,0) + " + dt.getQuanMin()
						+ ",flag_finish=(case when (isnull(quan_outstore,0)+"+dt.getQuanMin()+")>=quan_min then '1' else '0' end),ts = to_date('" + DateUtils.getDefaultDateFormat().format(new Date()) + "','YYYYMMDDHH24MISS') "
						+ " where pk_pdstdt = '" + pk_stdt + "' and quan_min>=isnull(quan_outstore,0)+"+ dt.getQuanMin();
			}else {
				 update_sql = "update pd_st_detail set quan_outstore = nvl(quan_outstore,0) + " + dt.getQuanMin()
						+ ",flag_finish=(case when (nvl(quan_outstore,0)+"+dt.getQuanMin()+")>=quan_min then '1' else '0' end),ts = to_date('" + DateUtils.getDefaultDateFormat().format(new Date()) + "','YYYYMMDDHH24MISS') "
						+ " where pk_pdstdt = '" + pk_stdt + "' and quan_min>=nvl(quan_outstore,0)+"+ dt.getQuanMin();
			}

			update_list.add(update_sql);
		}
		if(update_list!=null&&update_list.size()>0){
			DataBaseHelper.batchUpdate(update_list.toArray(new String[0]));
		}
	}
	
	
	/**
	 * 保存入库单
	 * @param stvo
	 * @param user
	 * @param type
	 * @return
	 */
	public PdStVo  savePdSt(PdStVo stvo,IUser user,String type){
		if(stvo == null) throw new BusException("未获取到需要保存的单据！");
		PdSt st = new PdSt();
		ApplicationUtils.copyProperties(st,stvo);
		if(CommonUtils.isEmptyString(st.getPkPdst())){//新增
			if(!CommonUtils.isEmptyString(type)){
				st.setDtSttype(type);
			}
			//st.setFlagChk("0");
			st.setFlagPay("0");
			DataBaseHelper.insertBean(st);
		}else{//修改
			DataBaseHelper.updateBeanByPk(st,false);
		}
		List<PdStDtVo> dtlist = stvo.getDtlist();
		if(dtlist!=null&&dtlist.size()>0){
			List<PdStDetail> insert_list = new ArrayList<PdStDetail>();
			List<PdStDetail> update_list = new ArrayList<PdStDetail>();
			for(PdStDtVo dt:dtlist){
				if(CommonUtils.isEmptyString(dt.getPkPdstdt())){//新增
					dt.setPkPdst(st.getPkPdst());
					dt.setPkPdstdt(NHISUUID.getKeyId());
					dt.setFlagChkRpt("0");
					dt.setFlagFinish("0");
					dt.setFlagPay("0");
					dt.setQuanOutstore(0.0);
					ApplicationUtils.setBeanComProperty(dt, true);
					insert_list.add(dt);
				}else{
					dt.setPkPdst(st.getPkPdst());
					ApplicationUtils.setBeanComProperty(dt, false);
	    			update_list.add(dt);
				}
			}
			if(insert_list!=null&&insert_list.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), insert_list);
			}
			if(update_list!=null&&update_list.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(PdStDetail.class), update_list);
			}
			//删除明细PD_ST_DETAIL
	    	if(stvo.getDelDtList()!=null&&stvo.getDelDtList().size()>0){
	    		DataBaseHelper.getJdbcTemplate().batchUpdate("delete from pd_st_detail where pk_pdstdt = ? ", stvo.getDelDtList());
	    	}
		}
		stvo.setPkPdst(st.getPkPdst());
		stvo.setDtlist(dtlist);
		return stvo;
	}
	/**
	 * 退回入库单
	 * @param param
	 * @param user
	 */
	public void rtnPdst(String param,IUser user,String dttype,String direct){
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		if(stvo == null) throw new BusException("未获取到需要退回的单据！");
		PdSt st = stOutHandler.createPdst(null,null,stvo, (User)user,dttype,direct);
		DataBaseHelper.insertBean(st);
		//生成明细
		Map<String,Object> map = new HashMap<String,Object>();
		//map.put("pkStore", stvo.getPkStoreSt());
		map.put("pkPdst", stvo.getPkPdst());
		List<PdStDtVo> dtlist = null;
		if(stvo.getDtlist()==null||stvo.getDtlist().size()<=0)
			dtlist = pdstPubMapper.queryPdStDetailList(map);
		else 
			dtlist = stvo.getDtlist();
		if(dtlist == null ||dtlist.size() <= 0) return;
		List<PdStDetail> insert_list = new ArrayList<PdStDetail>();
		int i = 1;
		for(PdStDtVo dt:dtlist){
			PdStDetail stdt = stOutHandler.createPdstdt(null,dt,(User)user,st.getPkPdst(),i);
			i++;
			//更新库存表
			updateStock(dt,stvo.getPkStoreSt(),dt.getQuanMin());
			//更新原入库单的库存数量
			stdt.setQuanOutstore(MathUtils.add(stdt.getQuanOutstore(),stdt.getQuanMin()));
			if(stdt.getQuanOutstore()==stdt.getQuanMin()){//出库数量==退回数量
				stdt.setFlagFinish("1");
			}
			insert_list.add(stdt);
		}
		if(insert_list!=null&&insert_list.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), insert_list);
		}
		
	}
	/**
	 * 根据入库单主键查询入库明细
	 * @param param{pkPdst}
	 * @param user
	 * @return
	 */
	public List<PdStDtVo> queryPdStDtList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null ||CommonUtils.isNull(map.get("pkPdst")))
           throw new BusException("入库单主键为空，无法获取明细！");
		return pdstPubMapper.queryPdStDetailList(map);
	}
	/**
	 * 更新出库库存量
	 * @param dt
	 * @param pk_store
	 * @param quan_min_out
	 */
	public void updateStock(PdStDtVo dt,String pk_store,Double quan_min_out){
		Map<String,Object> stmap = verfiyExistPD(dt,pk_store,"0");
		if(stmap == null ||CommonUtils.isNull(CommonUtils.getString(stmap.get("pkPdstock"))))
			throw new BusException("未查询到物品"+stOutHandler.getPdNameByPk(dt.getPkPd())+"匹配的库存记录,请核对后重新操作！");
		String pk_pdstock = CommonUtils.getString(stmap.get("pkPdstock"));
		//如果退货数量和库存量相等，并且quan_prep=0，删除该条库存记录，否则更新库存量
		Double quan_min = CommonUtils.getDouble(stmap.get("quanMin"));
		Double quan_prep = CommonUtils.getDouble(stmap.get("quanPrep"));
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(MathUtils.equ(MathUtils.sub(quan_min, quan_min_out), 0.00)&&MathUtils.equ(quan_prep,0.00)){
			DataBaseHelper.execute("delete from pd_stock where pk_pdstock = ? ",new Object[]{pk_pdstock});
		}else{
			paramMap.put("quan", quan_min_out);
			paramMap.put("pkPdstock", pk_pdstock);
			paramMap.put("dateNow", new Date());
			paramMap.put("amountP", dt.getAmount());
			paramMap.put("amountCostP", dt.getAmountCost());
			int count = DataBaseHelper.update("update pd_stock set quan_min =  quan_min - :quan, "
					+ "amount =amount - :amountP,amount_cost = amount_cost - :amountCostP ,ts = :dateNow where pk_pdstock = :pkPdstock and quan_min>=:quan", paramMap);
			if(count ==0){
				throw new BusException("物品"+stOutHandler.getPdNameByPk(dt.getPkPd())+"库存数量不足,请核对后重新操作！");
			}
		}
		DataBaseHelper.execute("delete from pd_stock where pk_pdstock = ? and quan_min<=0",new Object[]{pk_pdstock});
	}
	
	/**
	 * 校验库存表是否存在匹配的库存记录
	 * @param dt
	 * @param pk_store
	 * @return
	 */
	public Map<String,Object> verfiyExistPD(PdStDtVo dt,String pk_store,String flagStop){
		Map<String,Object> paramMap = new HashMap<String,Object>();

		paramMap.put("pkPd", dt.getPkPd());
		paramMap.put("batchNo", dt.getBatchNo());
		if(dt.getDateExpire()!=null){
			paramMap.put("dateExpire", DateUtils.getDefaultDateFormat().format(dt.getDateExpire()).substring(0, 8));
		}
		if(!CommonUtils.isEmptyString(dt.getPkPdPlandt())){
			double priceOrg= new BigDecimal(dt.getPriceOrg()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
			double pricCostOrg=new BigDecimal(dt.getPriceCostOrg()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
			paramMap.put("priceCost", pricCostOrg);
			paramMap.put("price", priceOrg);
		}else{
			double price= new BigDecimal(dt.getPrice()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
			double pricCost=new BigDecimal(dt.getPriceCost()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
			paramMap.put("priceCost",pricCost);
			paramMap.put("price", price);
		}
		//采购退回可以修改成本单价，未修改前的成本单价
		if(dt.getPriceCostOld()!= null && MathUtils.compareTo(dt.getPriceCostOld(),0d)>0){
			double pricCost=new BigDecimal(dt.getPriceCostOld()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
			paramMap.put("priceCost", pricCost);
		}
		
		paramMap.put("pkStore",pk_store);//当前仓库
		//paramMap.put("flagStop",flagStop);//当前仓库
		List<Map<String,Object>> stmap = pdstPubMapper.queryPkPdStoreByCon(paramMap);
		if(stmap!=null&&stmap.size()>0)
			return stmap.get(0);
		return null;
	}
	/**
	 * 校验指定物品是否在指定仓库中存在
	 * @param pkStore
	 * @param pkPds
	 */
	public List<Map<String,Object>> verfyPdIsInStore(String pkStore,String pkDept,List<String> pkPds){
		Map<String,Object> map = new HashMap<String,Object>();
    	map.put("pkStore", pkStore);
    	map.put("pkDept", pkDept);
    	map.put("pdlist", pkPds);
    	return pdstPubMapper.queryPdByStore(map);
	}

	/**
	 * 校验当前仓库是否初始建账
	 * @param pkStore
	 * @return
	 */
	public Integer verfyPdIsStRecord(String pkDept,String pkStore){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkStore",pkStore);

		map.put("pkDept",pkDept);
		Integer isInit = pdstPubMapper.verfyPdIsStRecord(map);
		if(isInit==null){
			return 0;
		}else{
			return isInit;
		}
	}
	
}
