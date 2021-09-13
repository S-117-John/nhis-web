package com.zebone.nhis.scm.pub.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdRepriceHist;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.module.scm.st.PdStock;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.dao.PdStPubMapper;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdInParamVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 * 入库公共服务
 * @author yangxue
 *
 */
@Service
public class PdStInPubService {
	@Resource
	private PdStOutHandler stOutHandler;
	@Resource
	private PdStPubMapper pdstPubMapper;
	/**
	 * 入库
	 * @param pdList
	 * @param pk_store
	 * @return List<Map<String,Object>>
	 */
	public Map<String,Object> execStIn(String pkOrgAp,String pkDeptAp,List<PdInParamVo> pdList,String pk_store){
		boolean flag_pd = pdList==null||pdList.size()<=0;
		if(flag_pd)
			throw new BusException("未获取到需要入库的数据！");
		if(CommonUtils.isEmptyString(pk_store))
			throw new BusException("未获取到入库仓库主键！");
		User user = UserContext.getUser();
		//查询是否当天生成过的退药入库单
		String pk_pdst = stOutHandler.getPdStIn(pkOrgAp,pkDeptAp,IDictCodeConst.DT_STTYPE_ORDIN, user, pk_store, "1");
		if(CommonUtils.isEmptyString(pk_pdst)){
			//创建入库单
			PdSt st = stOutHandler.createPdst(pkOrgAp,pkDeptAp,null, user, IDictCodeConst.DT_STTYPE_ORDIN, "1");	
			DataBaseHelper.insertBean(st);
			pk_pdst = st.getPkPdst();
		}
		List<PdStDetail> dtlist = new ArrayList<PdStDetail>();
		Map<String,Object> rtnMap = new  HashMap<String,Object>();
		int i = 1;
		Map<String,PdStock> newStkMap = new HashMap<String,PdStock>();//插入库存的物品集合(key:pkPd||batchNo||priceCost||pkStore||dateExpire(不为空时))
		Map<String,PdStockParam> updateStkMap = new HashMap<String,PdStockParam>();//更新库存的物品集合(key:pkPdStock)
		for(PdInParamVo paramVo:pdList){
			String pk_pd = paramVo.getPkPd();
			if(pk_pd == null||CommonUtils.isEmptyString(pk_pd))
				throw new BusException("此退药单包含物品主键为空的数据，无法完成入库！");
			Map<String,Object> pdMap = getPdInfo(pk_pd);
			if(pdMap == null){
				throw new BusException("未获取到物品的零售包装量，无法完成入库！");
			}
			double quan_pack = MathUtils.div(paramVo.getQuanMin(), CommonUtils.getDouble(pdMap.get("packSize")));
			//转换零售单价与成本单价为零售单位的对应单价
			paramVo.setPriceCost(MathUtils.mul(MathUtils.div(paramVo.getPriceCost(), paramVo.getPackSize().doubleValue()), CommonUtils.getDouble(pdMap.get("packSize"))));
			paramVo.setPrice(MathUtils.mul(MathUtils.div(paramVo.getPrice(), paramVo.getPackSize().doubleValue()), CommonUtils.getDouble(pdMap.get("packSize"))));
			//四舍五入保留六位小数--yangxue 2019/6/13
			paramVo.setPrice(new BigDecimal(paramVo.getPrice()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			paramVo.setPriceCost(new BigDecimal(paramVo.getPriceCost()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			//是否同一批次判断标准 
			Map<String,Object> pdParam = verfiyExistPD(paramVo,pk_store,null);
			if(pdParam == null){//库存中不存在
				//pkPd||batchNo||priceCost||pkStore||dateExpire(不为空时)
				StringBuilder key = new StringBuilder(pk_pd);
				key.append("||");
				key.append(paramVo.getBatchNo());
				key.append("||");
				//key.append(paramVo.getPriceCost());
				//key.append("||");
				key.append(pk_store);
				key.append("||");
				if(paramVo.getDateExpire()!=null){
					key.append(DateUtils.getDateTimeStr(paramVo.getDateExpire()));
				}else{
					key.append("null");
				}
			   boolean flagHas = false;
			   for (Map.Entry<String,PdStock> entry : newStkMap.entrySet()) {
				 if(key.toString().equals(entry.getKey())
						 && MathUtils.abs(MathUtils.sub(paramVo.getPriceCost(),entry.getValue().getPriceCost())).doubleValue()<0.01
						 && MathUtils.abs(MathUtils.sub(paramVo.getPrice(),entry.getValue().getPrice())).doubleValue()<0.01
				 ){//存在，追加数量及金额
					 flagHas = true;
					 PdStock stk = entry.getValue();
					 stk.setAmount(MathUtils.add(stk.getAmount(), MathUtils.mul(quan_pack, paramVo.getPrice())));
					 stk.setAmountCost(MathUtils.add(stk.getAmountCost(), MathUtils.mul(quan_pack, paramVo.getPriceCost())));
					 stk.setQuanMin(MathUtils.add(stk.getQuanMin(),paramVo.getQuanMin()));
					 break;
				 }   
			   }
			  if(!flagHas){//不存在相同批次的，生成新的库存记录
				 newStkMap.put(key.toString(), createStock(paramVo,pk_store,user,pdMap));
			  }
			}else{//库存中存在
				double price = CommonUtils.getDouble(pdParam.get("price"));
				if(!MathUtils.equ(price, paramVo.getPrice())){//已调价
					createPriceHist(paramVo,price,user,pk_store,pdMap);
					paramVo.setPrice(price);//按新价格入库
					paramVo.setPriceCost(CommonUtils.getDouble(pdParam.get("priceCost")));
				}
				String pkPdstock = CommonUtils.getString(pdParam.get("pkPdstock")); 
				boolean flagHas = false;
				   for (Map.Entry<String,PdStockParam> entry : updateStkMap.entrySet()) {
					 if(pkPdstock.equals(entry.getKey()) ){//存在，追加数量及金额
						 flagHas = true;
						 PdStockParam stkParam = entry.getValue();
						 stkParam.setAmount(MathUtils.add(stkParam.getAmount(), MathUtils.mul(quan_pack, paramVo.getPrice())));
						 stkParam.setAmountCost(MathUtils.add(stkParam.getAmountCost(), MathUtils.mul(quan_pack, paramVo.getPriceCost())));
						 stkParam.setQuanMin(MathUtils.add(stkParam.getQuanMin(),paramVo.getQuanMin()));
						 break;
					 }   
				   }
				  if(!flagHas){//不存在相同批次的，生成新的库存记录
					  PdStockParam stkParam = new PdStockParam();
					  stkParam.setAmount(MathUtils.mul(quan_pack, paramVo.getPrice()));
					  stkParam.setAmountCost(MathUtils.mul(quan_pack, paramVo.getPriceCost()));
					  stkParam.setQuanMin(paramVo.getQuanMin());
					  stkParam.setPkPdStock(pkPdstock);
					  updateStkMap.put(pkPdstock, stkParam);
				  }
			}
			PdStDetail dt = stOutHandler.createPdstdt(null, null, user, pk_pdst, i);
			setDtProperties(dt,paramVo,pdMap);
			dtlist.add(dt);
			rtnMap.put(paramVo.getPkPdapdt(), dt.getPkPdstdt());
		}
		//批量更新库存
		for (Map.Entry<String,PdStockParam> entry : updateStkMap.entrySet()) {
		    updateStock(entry.getValue());
	    }
		List<PdStock> sklist = new ArrayList<PdStock>();
		for (Map.Entry<String,PdStock> entry : newStkMap.entrySet()) {
			sklist.add(entry.getValue());
	    }
		if(sklist!=null&&sklist.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStock.class), sklist);
		}
		if(dtlist!=null&&dtlist.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), dtlist);
		}
		return rtnMap;
	}
	
	public void setDtProperties(PdStDetail dt,PdInParamVo paramVo,Map<String,Object> pdMap){
		int pack_size = CommonUtils.getInteger(pdMap.get("packSize"));
		double quan_pack = MathUtils.div(paramVo.getQuanMin(), CommonUtils.getDouble(pack_size));
		dt.setAmount(MathUtils.mul(quan_pack, paramVo.getPrice()));
		dt.setAmountCost(MathUtils.mul(quan_pack, paramVo.getPriceCost()));
		dt.setBatchNo(paramVo.getBatchNo());
		dt.setDateExpire(paramVo.getDateExpire());
		dt.setQuanPack(paramVo.getQuanPack());
		//dt.setQuanPack(quan_pack); 原始代码强制转换为了零售包装对应数量，改为退药入库时入参传入的包装对应数量
		dt.setQuanMin(paramVo.getQuanMin());
		dt.setQuanOutstore(0d);
		dt.setPkUnitPack(paramVo.getPkUnitPack());
		//dt.setPkUnitPack(CommonUtils.getString(pdMap.get("pkUnitPack")));  原始代码强制转换为了零售包装，改为退药入库时入参传入的包装
		dt.setPrice(paramVo.getPrice());
		dt.setPriceCost(paramVo.getPriceCost());
		dt.setPackSize(paramVo.getPackSize());
		//dt.setPackSize(pack_size); 原始代码强制转换为了零售包装量，改为退药入库时入参传入的包装量
		dt.setPkPd(paramVo.getPkPd());
	}
	
	/**
	 * 创建调价记录
	 * @param vo
	 * @param price
	 * @param u
	 * @param pk_store
	 */
	private void createPriceHist(PdInParamVo vo ,double price,User u,String pk_store,Map<String,Object> pdMap){
		PdRepriceHist hist = new PdRepriceHist();
		double pack_size = CommonUtils.getDouble(pdMap.get("packSize"));
		double quan_pack = MathUtils.div(vo.getQuanMin(), pack_size);
		hist.setAmount(MathUtils.mul(quan_pack, price));
		hist.setAmountOrg(MathUtils.mul(quan_pack, vo.getPrice()));
		hist.setAmountRep(MathUtils.sub(hist.getAmountOrg(), hist.getAmount()));
		//hist.setApproval(approval);
		hist.setCodeRep(ScmPubUtils.getRepCode());
		hist.setDateReprice(new Date());
		hist.setDelFlag("0");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkStore", pk_store);
		paramMap.put("pkPd", vo.getPkPd());
		//调价类型取最近一次调价类型
		List<Map<String,String>> repriceList = pdstPubMapper.queryRepriceHist(paramMap);
		if(repriceList!=null&&repriceList.size()>0){
			hist.setDtReptype(repriceList.get(0).get("dtReptype"));
		}else{
			hist.setDtReptype("03");
		}
		
		hist.setPackSize(vo.getPackSize());//仓库单位对应包装量
		hist.setPkDept(u.getPkDept());
		hist.setPkOrg(u.getPkOrg());
		hist.setPkPd(vo.getPkPd());
		hist.setPkStore(pk_store);
		hist.setPkUnitPack(vo.getPkUnitPack());//仓库单位
		hist.setPrice(MathUtils.mul(MathUtils.div(price, pack_size), vo.getPackSize().doubleValue()));//仓库单位对应价格
		hist.setPriceOrg(MathUtils.mul(MathUtils.div(vo.getPrice(), pack_size), vo.getPackSize().doubleValue()));
		hist.setQuanRep(MathUtils.div(vo.getQuanMin(), vo.getPackSize().doubleValue()));//仓库单位对应数量
		DataBaseHelper.insertBean(hist);
		
	}
	/**
	 * 校验库存表是否存在匹配的库存记录
	 * @param  {pkPd，batchNo，dateExpire，priceCost，price，pkStore}
	 * @param pk_store
	 * @return
	 */
	private Map<String,Object> verfiyExistPD(PdInParamVo dt,String pk_store,String flagStop){
		Map<String,Object> verfyMap = new HashMap<String,Object>();
		verfyMap.put("pkPd", dt.getPkPd());
		verfyMap.put("batchNo", dt.getBatchNo());
		if(dt.getDateExpire()!=null){
			verfyMap.put("dateExpire",DateUtils.getDefaultDateFormat().format(dt.getDateExpire()).substring(0, 8));
		}
		verfyMap.put("priceCost", dt.getPriceCost());
		verfyMap.put("pkStore", pk_store);
		//verfyMap.put("flagStop", flagStop);
		List<Map<String,Object>> stmap = pdstPubMapper.queryPkPdStoreByCon(verfyMap);
		if(stmap!=null&&stmap.size()>0)
			return stmap.get(0);
		return null;
	}
	/**
	 * 更新库存
	 * @param stockMap
	 * @param paramVo
	 */
	public void updateStock(Map<String,Object> stockMap,PdInParamVo paramVo,Map<String,Object> pdMap){
		String pk_pdstock = CommonUtils.getString(stockMap.get("pkPdstock"));
		double quan_pack = MathUtils.div(paramVo.getQuanMin(), CommonUtils.getDouble(pdMap.get("packSize")));
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("quan", paramVo.getQuanMin());
		paramMap.put("pkPdstock", pk_pdstock);
		paramMap.put("dateNow", new Date());
		paramMap.put("amountP", MathUtils.mul(quan_pack, paramVo.getPrice()));
		paramMap.put("amountCostP", MathUtils.mul(quan_pack, paramVo.getPriceCost()));
		DataBaseHelper.update("update pd_stock set quan_min =  quan_min + :quan, "
				+ "amount =amount + :amountP,amount_cost = amount_cost + :amountCostP ,ts = :dateNow where pk_pdstock = :pkPdstock  ", paramMap);
				
	}
	/**
	 * 更新库存量
	 * @param param
	 */
	public void updateStock(PdStockParam param){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("quan", param.getQuanMin());
		paramMap.put("pkPdstock", param.getPkPdStock());
		paramMap.put("dateNow", new Date());
		paramMap.put("amountP", param.getAmount());
		paramMap.put("amountCostP", param.getAmountCost());
		DataBaseHelper.update("update pd_stock set quan_min =  quan_min + :quan, "
				+ "amount =amount + :amountP,amount_cost = amount_cost + :amountCostP ,ts = :dateNow where pk_pdstock = :pkPdstock  ", paramMap);
	
	}
	/**
	 * 创建新的库存
	 * @param paramVo
	 * @param pk_store
	 */
	public PdStock createStock(PdInParamVo paramVo,String pk_store,User user,Map<String,Object> pdMap){
		PdStock st = new PdStock();
		double quan_pack = MathUtils.div(paramVo.getQuanMin(), CommonUtils.getDouble(pdMap.get("packSize")));
    	st.setAmount(MathUtils.mul(paramVo.getPrice(), quan_pack));
    	st.setAmountCost(MathUtils.mul(paramVo.getPriceCost(),quan_pack));
    	st.setBatchNo(paramVo.getBatchNo());
    	st.setDateExpire(paramVo.getDateExpire());
    	st.setFlagStop("0");
    	st.setFlagStopEr("0");
    	st.setFlagStopOp("0");
    	st.setPkDept(user.getPkDept());//当前部门
    	st.setPkOrg(user.getPkOrg());
    	st.setPkPd(paramVo.getPkPd());
    	st.setPkStore(pk_store);
    	st.setPrice(paramVo.getPrice());
    	st.setPriceCost(paramVo.getPriceCost());
    	st.setQuanMin(paramVo.getQuanMin());
    	st.setQuanPrep(0.00);
    	st.setPkPdstock(NHISUUID.getKeyId());
    	ApplicationUtils.setBeanComProperty(st, true);
    	return st;
	}
	/**
	 * 获取物品的零售单位，零售包装量
	 * @param pk_pd
	 * @return
	 */
	public Map<String,Object> getPdInfo(String pk_pd){
		String sql = "select pack_size,pk_unit_pack,name from bd_pd where pk_pd = ?";
		return DataBaseHelper.queryForMap(sql, new Object[]{pk_pd});
	}
	
	/**
	 * 更新库存使用的参数对象
	 * @author yangxue
	 *
	 */
	class PdStockParam{
		private Double quanMin;//更新数量
		private String pkPdStock;
		private Double amount;//更新金额
		private Double amountCost;//更新成本金额
		public Double getQuanMin() {
			return quanMin;
		}
		public void setQuanMin(Double quanMin) {
			this.quanMin = quanMin;
		}
		public String getPkPdStock() {
			return pkPdStock;
		}
		public void setPkPdStock(String pkPdStock) {
			this.pkPdStock = pkPdStock;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public Double getAmountCost() {
			return amountCost;
		}
		public void setAmountCost(Double amountCost) {
			this.amountCost = amountCost;
		}
		
		
	}
	
}
