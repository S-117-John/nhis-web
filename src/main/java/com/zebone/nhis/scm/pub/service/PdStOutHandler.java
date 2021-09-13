package com.zebone.nhis.scm.pub.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.dao.PdStPubMapper;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物品出库 此类中所有方法变动要特别慎重：
 * 其中包含住院发药药品寻批次，
 * 出库审核确认批次，
 * 待出库寻批次，
 * 门诊处方询价
 * 以及门诊发药确认批次
 * @author yangxue
 *
 */
@Service
public class PdStOutHandler{
	
	@Resource
	private PdStPubMapper pdStPubMapper;
	/**
	 * 锁确认发药物品批次（并发控制）
	 * @param pk_store
	 * @return
	 */
	public void lockConfirmPdSt(List<String> pkPds,String pk_store) {
		
		String sql = "select nvl(dt.quan_outstore,0) from pd_st_detail dt  where "
				   + "exists( select 1 from pd_st st where st.pk_store_st=:pkStore and st.del_flag='0' and st.eu_direct = 1 and dt.pk_pdst=st.pk_pdst) "
				   + " and dt.pk_pd in(:pkPds) and dt.del_flag='0' and dt.flag_finish = '0' and dt.quan_min - nvl(dt.quan_outstore,0) > 0  for update of quan_outstore"   ;   //对quan_outstore字段进行加锁
		if(Application.isSqlServer()){
			sql = "select isnull(dt.quan_outstore,0) from pd_st_detail dt  with (nolock) where "
					   + "exists( select 1 from pd_st st  with(nolock) where st.pk_store_st=:pkStore and st.del_flag='0' and st.eu_direct = 1 and dt.pk_pdst=st.pk_pdst) "
					   + " and dt.pk_pd in(:pkPds) and dt.del_flag='0' and dt.flag_finish = '0' and dt.quan_min - isnull(dt.quan_outstore,0) > 0 "   ;
		}
		Map<String,Object> pMap = new HashMap<String,Object>();
		pMap.put("pkStore", pk_store);
		pMap.put("pkPds", pkPds);
		DataBaseHelper.queryForList(sql, pMap);
		
	}
	
	/**
	 * 门诊医生站处方保存用确认发药物品批次
	 * 特别说明：门诊医生站处方保存用，其他不一定适用。因为只取第一个批次，而且调用这个之前已经确定了是否有库存
	 * @return
	 */
	public List<PdOutDtParamVo> confirmPdSt(Map<String,Object> storePriceMap) {
		String eu_outtype=storePriceMap.get("euOuttype").toString();
		String pk_dept=storePriceMap.get("pkDeptExec").toString();
		String pk_store=storePriceMap.get("pkStore").toString();
		List<String> pkPdList = (List<String>)storePriceMap.get("pkPd");
		//根据物品，出库模式获取入库明细
		List<PdOutDtParamVo> dtlist = queryBatchInStDtByOp(eu_outtype, pk_dept,pk_store,pkPdList);
		Map<String, PdOutDtParamVo> collect = dtlist.parallelStream().collect(Collectors.toMap(PdOutDtParamVo::getPkPd, vo -> vo, (k1, k2) -> k1));
		Optional<String> optional = pkPdList.parallelStream().filter(t -> !collect.containsKey(t)).findFirst();
		if(optional.isPresent()){
			throw new BusException("未获取到"+getPdNameByPk(optional.get())+"可用入库记录，无法完成出库操作！");
		}
		//按照方法说明，多个批次情况下获取第一个
		return Lists.newArrayList(collect.values());
	}
	
	/**
	 *确认发药物品批次
	 * @param pk_pd
	 * @param pk_store
	 * @param quan_min
	 * @param eu_outtype
	 * @param pk_pv
	 * @param pk_cnord
	 * @param pk_pdapdt
	 * @return
	 */
	public List<PdOutDtParamVo> confirmPdSt(String pk_pd,String pk_dept,String pk_store,Double quan_min,String eu_outtype,String pk_pv,String pk_cnord,String pk_pdapdt) {
		//根据物品，出库模式获取入库明细
		List<PdOutDtParamVo> dtlist = queryInStDt(eu_outtype,pk_dept,pk_store, pk_pd,pk_pv,pk_cnord,pk_pdapdt);
		if(dtlist==null||dtlist.size()<=0) throw new BusException("未获取到与物品"+getPdNameByPk(pk_pd)+"相匹配的可用入库记录，无法完成出库操作！");
		double total_min = 0;
		List<PdOutDtParamVo> result = new ArrayList<PdOutDtParamVo>();
		for(PdOutDtParamVo dtvo:dtlist){
			//按顺序累加，若数量
			double use_min = MathUtils.sub(dtvo.getQuanMin(), dtvo.getQuanOutstore());//可用数量
			total_min = MathUtils.add(total_min, use_min);
			//还不足够出库数量,添加到出库列表
			if(total_min <= quan_min ){
				dtvo.setQuanOutMin(use_min);
				dtvo.setQuanOutPack(MathUtils.div(use_min, CommonUtils.getDouble(dtvo.getPackSize())));
				result.add(dtvo);
			}else{//已经满足需要出库的数量
				if(MathUtils.sub(total_min, use_min) - quan_min >= 0){  
					break;
				}else {
					dtvo.setQuanOutMin(MathUtils.sub(quan_min, MathUtils.sub(total_min, use_min)));
					dtvo.setQuanOutPack(MathUtils.div(dtvo.getQuanOutMin(), CommonUtils.getDouble(dtvo.getPackSize())));
					result.add(dtvo);
				}
			}
		}
		return result;
	}
	
	/**
	 * 出库处理-待出库页签，根据仓库规则确定出库批次
	 * @param pk_pd
	 * @param pk_store
	 * @param quan_min
	 * @param eu_outtype
	 * @return
	 */
	public List<PdOutDtParamVo> confirmPdSt(String pk_pd,String pk_store,Double quan_min,String eu_outtype) {
		//根据物品，出库模式获取入库明细
		List<PdOutDtParamVo> dtlist = queryInStDt(eu_outtype,pk_store, pk_pd);
		if(dtlist==null||dtlist.size()<=0) throw new BusException("未获取到与物品"+getPdNameByPk(pk_pd)+"相匹配的可用入库记录，无法完成出库操作！");
		double total_min = 0;
		List<PdOutDtParamVo> result = new ArrayList<PdOutDtParamVo>();
		for(PdOutDtParamVo dtvo:dtlist){
			//按顺序累加，若数量
			double use_min = MathUtils.sub(dtvo.getQuanMin(), dtvo.getQuanOutstore());//可用数量
			total_min = MathUtils.add(total_min, use_min);
			//还不足够出库数量,添加到出库列表
			if(total_min <= quan_min ){
				dtvo.setQuanOutMin(use_min);
				dtvo.setQuanOutPack(MathUtils.div(use_min, CommonUtils.getDouble(dtvo.getPackSize())));
				result.add(dtvo);
			}else{//已经满足需要出库的数量
				if(MathUtils.sub(total_min, use_min) - quan_min >= 0){  
					break;
				}else {
					dtvo.setQuanOutMin(MathUtils.sub(quan_min, MathUtils.sub(total_min, use_min)));
					dtvo.setQuanOutPack(MathUtils.div(dtvo.getQuanOutMin(), CommonUtils.getDouble(dtvo.getPackSize())));
					result.add(dtvo);
				}
			}
		}
		
		result=distinctStdtData(result);
		return result;
	}
	
	private List<PdOutDtParamVo> distinctStdtData(List<PdOutDtParamVo> dtlist){
		List<PdOutDtParamVo> resdtList=new ArrayList<PdOutDtParamVo>();
		Map<String,PdOutDtParamVo> keyPddts=new HashMap<String,PdOutDtParamVo>();
		for (PdOutDtParamVo outstDtvo : dtlist) {
			String keyPddt=outstDtvo.getPkPd()+"-"+outstDtvo.getBatchNo()+"-"+outstDtvo.getPrice()+"-"+outstDtvo.getPriceCost()+"-"+outstDtvo.getDateExpire();
			if(keyPddts.keySet().contains(keyPddt)){
				//quanMin
				double quanMinBef=keyPddts.get(keyPddt).getQuanMin();
				double quanMin=MathUtils.add(quanMinBef,outstDtvo.getQuanMin());
				//quanMinOut 
				double quanMinOutBef=keyPddts.get(keyPddt).getQuanOutMin();
				double quanMinOut=MathUtils.add(quanMinOutBef,outstDtvo.getQuanOutMin());
				//quanOutPack
				double quanOutPackBef=keyPddts.get(keyPddt).getQuanOutPack();
				double quanOutPack=MathUtils.add(quanOutPackBef,outstDtvo.getQuanOutPack());
				//QuanOutstore
				double quanOutstoreBef=keyPddts.get(keyPddt).getQuanOutstore();
				double quanOutstore=MathUtils.add(quanOutstoreBef,outstDtvo.getQuanOutstore());
				keyPddts.get(keyPddt).setQuanMin(quanMin);
				keyPddts.get(keyPddt).setQuanOutMin(quanMinOut);
				keyPddts.get(keyPddt).setQuanOutPack(quanOutPack);
				keyPddts.get(keyPddt).setQuanOutstore(quanOutstore);
			}else{
				keyPddts.put(keyPddt, outstDtvo);
			}
		}
		for (PdOutDtParamVo pdStDtVo : keyPddts.values()) {
			resdtList.add(pdStDtVo);
		}
		return resdtList;
	}
	
	/**
	 * 确认药品出库批次-出库审核使用
	 * @param pk_pd 药品主键
	 * @param pk_store 对应出库仓库
	 * @param quan_min 
	 * @param eu_outtype 出库方式
	 * @param price 零售单价
	 * @param priceCost 成本单价
	 * @param batchNo 批号
	 * @param dateExpire 失效时间
	 * @return
	 */
	public Map<String,Object> confirmPdSt(String pk_pd,String pk_dept,String pk_store,Double quan_min,String eu_outtype,Double price,Double priceCost,String batchNo,Date dateExpire) {
		//根据物品，出库模式获取入库明细
		List<PdOutDtParamVo> dtlist = queryInStDt(eu_outtype,pk_dept,pk_store, pk_pd,price,priceCost,batchNo,dateExpire);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(dtlist==null||dtlist.size()<=0){
			//throw new BusException("未获取到与物品"+getPdNameByPk(pk_pd)+"-["+batchNo+"]相匹配的可用入库记录，无法完成出库操作！");
			resultMap.put("errpd",getPdNameByPk(pk_pd));
			return resultMap;
		}
		double total_min = 0;
		List<PdOutDtParamVo> result = new ArrayList<PdOutDtParamVo>();
		for(PdOutDtParamVo dtvo:dtlist){
			//按顺序累加，若数量
			double use_min = MathUtils.sub(dtvo.getQuanMin(), dtvo.getQuanOutstore());//可用数量
			total_min = MathUtils.add(total_min, use_min);
			//还不足够出库数量,添加到出库列表
			if(total_min <= quan_min ){
				dtvo.setQuanOutMin(use_min);
				dtvo.setQuanOutPack(MathUtils.div(use_min, CommonUtils.getDouble(dtvo.getPackSize())));
				result.add(dtvo);
			}else{//已经满足需要出库的数量
				if(MathUtils.sub(total_min, use_min) - quan_min >= 0){  
					break;
				}else {
					dtvo.setQuanOutMin(MathUtils.sub(quan_min, MathUtils.sub(total_min, use_min)));
					dtvo.setQuanOutPack(MathUtils.div(dtvo.getQuanOutMin(), CommonUtils.getDouble(dtvo.getPackSize())));
					result.add(dtvo);
				}
			}
		}
		resultMap.put("dtlist",result);
		return resultMap;
	}
	
	/**
	 * 根据仓库出库模式，获取物品入库明细列表
	 * @param eu_outtype
	 * @param pk_store
	 * @param pk_dept
	 * @param pk_pd
	 * @return
	 */
	public List<PdOutDtParamVo> queryInStDt(String eu_outtype, String pk_dept,String pk_store,String pk_pd,String pk_pv,String pk_cnord,String pk_pdapdt){
		if(CommonUtils.isEmptyString(eu_outtype)){
			eu_outtype = queryOutTypeByPdAndDept(pk_pd,pk_dept);
			if(CommonUtils.isEmptyString(eu_outtype))
				throw new BusException("未获取到仓库的出库模式！");
		}
	   StringBuffer sql = new StringBuffer(""); 
	   sql.append("select dt.pk_pdstdt,dt.pk_pd,st.date_chk, dt.date_fac,store.pack_size,pd.pack_size pack_size_pd,pd.pack_size_max,store.pk_unit as pk_unit_pack,store.posi_no,");  
	   sql.append("dt.date_expire, dt.batch_no, dt.quan_min,dt.quan_outstore,dt.price_cost,dt.price,dt.price/pd.pack_size*store.pack_size as price_store  from pd_st_detail dt ");
	   sql.append(" inner join bd_pd pd on pd.pk_pd = dt.pk_pd ");
	   sql.append(" inner join bd_pd_store store on store.pk_pd = pd.pk_pd and store.flag_stop='0' ");
	  // sql.append(" inner join bd_pd_convert cvt on cvt.pk_pd = store.pk_pd and cvt.pk_pdconvert = store.pk_pdconvert ");
	   sql.append(" inner join pd_st st on dt.pk_pdst=st.pk_pdst ");
	   sql.append(" inner join pd_stock stk on stk.pk_pd = dt.pk_pd and stk.batch_no = dt.batch_no and stk.price = dt.price ");
	   sql.append(" and stk.price_cost = dt.price_cost "); 
	   sql.append(" and nvl(to_char(stk.date_expire,'YYYYMMDD'),'~') = nvl(to_char(dt.date_expire,'YYYYMMDD'),'~') ");
       if(!CommonUtils.isEmptyString(pk_store)){
    	   sql.append(" and stk.pk_store = '"+pk_store+"' where st.pk_store_st = '"+pk_store+"' and store.pk_store='"+pk_store+"'");
       }else if(!CommonUtils.isEmptyString(pk_dept)){
    	   sql.append(" and stk.pk_dept = '"+pk_dept+"' where st.pk_dept_st = '"+pk_dept+"' and store.pk_dept = '"+pk_dept+"' ");
       }
	       
       sql.append(" and dt.pk_pd=? and st.eu_direct = 1 and  dt.quan_min -nvl(dt.quan_outstore,0) > 0 and dt.flag_finish = '0' ");
       if ("0".equals(eu_outtype)){//按入库日期正序
    	   sql.append(" order by st.date_chk ");
       }else if("1".equals(eu_outtype)||"3".equals(eu_outtype)){//按失效日期正序
    	   sql.append(" order by dt.date_expire  ");
       }else if("2".equals(eu_outtype)){//按入库日期倒序
    	   sql.append(" order by st.date_chk  desc ");
       }
		return DataBaseHelper.queryForList(sql.toString(), PdOutDtParamVo.class, new Object[]{pk_pd});
	}
	
	/**
	 * 根据仓库出库模式，获取物品入库明细列表，出库处理，待入库使用
	 * 原因：满足部分药品申请时有库存，出库时库存出尽，无法加载的情况
	 * @param eu_outtype
	 * @param pkStore
	 * @param pkPd
	 * @return
	 */
	public List<PdOutDtParamVo> queryInStDt(String eu_outtype,String pkStore,String pkPd){
		if(CommonUtils.isEmptyString(eu_outtype)){
				throw new BusException("未获取到仓库的出库模式！");
		}
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("pkStore", pkStore);
		paramMap.put("pkPd", pkPd);
		paramMap.put("euOuttype", eu_outtype);
		List<PdOutDtParamVo> resList=pdStPubMapper.getWaitInStdtDataList(paramMap);
		if(resList==null ||resList.size()==0){
			resList=pdStPubMapper.getUnHaveInStdtList(paramMap);
		}
		return resList;
	}
	
	/**
	 * 根据仓库出库模式，成本单价，零售单价，批号，失效时间，获取物品入库明细列表
	 * @param eu_outtype
	 * @param pk_store
	 * @param pk_dept
	 * @param pk_pd
	 * @return
	 */
	public List<PdOutDtParamVo> queryInStDt(String eu_outtype, String pk_dept,String pk_store,String pk_pd,Double price,Double priceCost,String batchNo,Date dateExpire){
		if(CommonUtils.isEmptyString(eu_outtype)){
			eu_outtype = queryOutTypeByPdAndDept(pk_pd,pk_dept);
			if(CommonUtils.isEmptyString(eu_outtype))
				throw new BusException("未获取到仓库的出库模式！");
		}
	   StringBuffer sql = new StringBuffer(""); 
	   sql.append("select dt.pk_pdstdt,dt.pk_pd,st.date_chk, dt.date_fac,store.pack_size,pd.pack_size pack_size_pd,pd.pack_size_max,store.pk_unit as pk_unit_pack,store.posi_no,");  
	   sql.append("dt.date_expire, dt.batch_no, dt.quan_min,dt.quan_outstore,dt.price_cost,dt.price,dt.price/pd.pack_size*store.pack_size as price_store  from pd_st_detail dt ");
	   sql.append(" inner join bd_pd pd on pd.pk_pd = dt.pk_pd ");
	   sql.append(" inner join bd_pd_store store on store.pk_pd = pd.pk_pd and store.flag_stop='0' ");
	   sql.append(" inner join pd_st st on dt.pk_pdst=st.pk_pdst ");
	   sql.append(" inner join pd_stock stk on stk.pk_pd = dt.pk_pd and nvl(stk.batch_no,'~') = nvl(dt.batch_no,'~') and stk.price = dt.price ");
	   sql.append(" and stk.price_cost = dt.price_cost "); 
	   sql.append(" and nvl(to_char(stk.date_expire,'YYYYMMDD'),'~') = nvl(to_char(dt.date_expire,'YYYYMMDD'),'~') ");
       if(!CommonUtils.isEmptyString(pk_store)){
    	   sql.append(" and stk.pk_store = '"+pk_store+"' where st.pk_store_st = '"+pk_store+"' and store.pk_store='"+pk_store+"'");
       }else if(!CommonUtils.isEmptyString(pk_dept)){
    	   sql.append(" and stk.pk_dept = '"+pk_dept+"' where st.pk_dept_st = '"+pk_dept+"' and store.pk_dept = '"+pk_dept+"' ");
       }
	       
       sql.append(" and dt.pk_pd=? and st.eu_direct = 1 and  dt.quan_min -nvl(dt.quan_outstore,0) > 0 and dt.flag_finish = '0' ");
       if(CommonUtils.isNotNull(batchNo)) {
		   sql.append(" and dt.price='"+price+"'");
		   sql.append(" and dt.price_cost='"+priceCost+"'");
		   sql.append(" and dt.batch_no='" + batchNo + "'");
	   }
       if(dateExpire!=null){
    	   String date=DateUtils.dateToStr("yyyy-MM-dd", dateExpire);
    	   sql.append(" and to_char(dt.date_expire,'YYYY-MM-dd')=to_char(to_date('"+date+"','yyyy-MM-dd'),'YYYY-MM-dd')");    
       }
       if ("0".equals(eu_outtype)){//按入库日期正序
    	   sql.append(" order by st.date_chk ");
       }else if("1".equals(eu_outtype)||"3".equals(eu_outtype)){//按失效日期正序
    	   sql.append(" order by dt.date_expire  ");
       }else if("2".equals(eu_outtype)){//按入库日期倒序
    	   sql.append(" order by st.date_chk  desc ");
       }
		return DataBaseHelper.queryForList(sql.toString(), PdOutDtParamVo.class, new Object[]{pk_pd});
	}
	
	/**
	 * 此方法仅住院/门诊发药使用
	 * 批量确认发药物品批次
	 * @return
	 */
	public List<PdOutDtParamVo> confirmBatchPdSt(String pk_pds,String pk_dept,String pk_store,String eu_outtype,List<PdOutParamVo> outlist,boolean flag_ip) {
		//根据物品，出库模式获取入库明细
		List<PdOutDtParamVo> dtlist = queryBatchInStDt(eu_outtype,pk_dept,pk_store,pk_pds,flag_ip);
		if(dtlist==null||dtlist.size()<=0) throw new BusException("未获取到可用入库记录，无法完成出库操作！");
		Map<String, List<PdOutDtParamVo>> dtMap = dtlist.stream().collect(Collectors.groupingBy(PdOutDtParamVo::getPkPd));

		List<PdOutDtParamVo> result = new ArrayList<>();
		//取出库物品列表
		for(PdOutParamVo outVo:outlist){
			List<PdOutDtParamVo> list = dtMap.get(outVo.getPkPd());
			if(CollectionUtils.isEmpty(list)){
				throw new BusException(getPdNameByPk(outVo.getPkPd()) + "：未获取到可用入库批次");
			}
			//对同一个物品进行计算批次
			double total_min = 0;
			List<PdOutDtParamVo> pdlist = new ArrayList<>();
			for(PdOutDtParamVo dtVo:list){
				dtVo.setPkCnord(outVo.getPkCnOrd());
				dtVo.setPkPv(outVo.getPkPv());
				dtVo.setPkPdapdt(outVo.getPkPdapdt());
				//按顺序累加，若数量
				double use_min = MathUtils.sub(dtVo.getQuanMin(), dtVo.getQuanOutstore());//可用数量
				total_min = MathUtils.add(total_min, use_min);
				//还不足够出库数量,添加到出库列表
				if(total_min <= outVo.getQuanMin() ){
					dtVo.setQuanOutMin(use_min);//需要出库数量
					dtVo.setQuanOutPack(MathUtils.div(use_min, CommonUtils.getDouble(dtVo.getPackSize())));
					pdlist.add(dtVo);
				}else{//已经满足需要出库的数量
					if(MathUtils.compareTo(MathUtils.sub(MathUtils.sub(total_min, use_min),outVo.getQuanMin()),0.00) >= 0){
						break;
					}else {
						dtVo.setQuanOutMin(MathUtils.sub(outVo.getQuanMin(), MathUtils.sub(total_min, use_min)));
						dtVo.setQuanOutPack(MathUtils.div(dtVo.getQuanOutMin(), CommonUtils.getDouble(dtVo.getPackSize())));
						pdlist.add(dtVo);
					}
				}
			}
			//如果某个药品寻批完成后，批次数量<需要数量，异常提示
			if(pdlist.stream().collect(Collectors.summingDouble(PdOutDtParamVo::getQuanOutMin)).compareTo(outVo.getQuanMin())<0){
				throw new BusException(getPdNameByPk(outVo.getPkPd()) + "：本次获取可用批次数量不足");
			}
			result.addAll(pdlist);
		}
		return result;
	}
	
	/**
	 * 根据仓库出库模式，批量获取物品入库明细列表——门诊处方保存调用
	 * @param euOuttype
	 * @param pkDept
	 * @param pkStore
	 * @return
	 */
	public List<PdOutDtParamVo> queryBatchInStDtByOp(String euOuttype, String pkDept,String pkStore,List<String> pkPds){
		if (CommonUtils.isEmptyString(euOuttype)) {
			throw new BusException("未获取到仓库的出库模式！");
		}
		Map<String,Object> paramMap = Maps.newHashMap();
		paramMap.put("euOuttype", euOuttype);
		paramMap.put("pkDept", pkDept);
		paramMap.put("pkStore", pkStore);
		paramMap.put("pkPds", pkPds);
	   return pdStPubMapper.queryBatchInStDtByOp(paramMap);
	}
	
	/**
	 * 根据仓库出库模式，批量获取物品入库明细列表
	 * @return
	 */
	public List<PdOutDtParamVo> queryBatchInStDt(String eu_outtype, String pk_dept,String pk_store,String pk_pds,boolean flag_ip){
	   if(CommonUtils.isEmptyString(eu_outtype)) throw new BusException("未获取到仓库的出库模式！");
	   StringBuffer sql = new StringBuffer("");
	   sql.append("select dt.pk_pdstdt,dt.pk_pd,st.date_chk, dt.date_fac,store.pack_size,pd.pack_size pack_size_pd,store.pk_unit as pk_unit_pack,");  
	   sql.append("dt.date_expire, dt.batch_no, dt.quan_min,dt.quan_outstore,dt.price_cost,dt.price,pd.name from pd_st_detail dt ");
	   sql.append(" inner join bd_pd pd on pd.pk_pd = dt.pk_pd ");
	   sql.append(" inner join bd_pd_store store on store.pk_pd = pd.pk_pd  ");//不控制仓库停用防止漏费and store.flag_stop='0'
	   //sql.append(" inner join bd_pd_convert cvt on cvt.pk_pd = store.pk_pd and cvt.pk_pdconvert = store.pk_pdconvert ");
	   sql.append(" inner join pd_st st on dt.pk_pdst=st.pk_pdst ");
	   sql.append(" inner join pd_stock stk on stk.pk_pd = dt.pk_pd and nvl(stk.batch_no,'~') = nvl(dt.batch_no,'~') and abs(dt.price-stk.price)<0.01 ");
	   if(flag_ip){//住院
		   sql.append(" and stk.flag_stop='0'");
	   }else if(flag_ip==false){//门诊
		   sql.append(" and stk.flag_stop_op='0'");
	   }else{//急诊
		   sql.append(" and stk.flag_stop_er='0'");
	   }
	   sql.append(" and abs(dt.price_cost-stk.price_cost)<0.01 ");
	   sql.append(" and nvl(to_char(stk.date_expire,'YYYYMMDD'),'~') = nvl(to_char(dt.date_expire,'YYYYMMDD'),'~') ");
		
		
       if(!CommonUtils.isEmptyString(pk_store)){
    	   sql.append(" and stk.pk_store = '"+pk_store+"' where st.pk_store_st = '"+pk_store+"' and store.pk_store='"+pk_store+"'");
       }else if(!CommonUtils.isEmptyString(pk_dept)){
    	   sql.append(" and stk.pk_dept = '"+pk_dept+"' where st.pk_dept_st = '"+pk_dept+"' and store.pk_dept = '"+pk_dept+"' ");
       }
       //if(flag_ip){//取住院默认单位
    	//   sql.append(" and cvt.flag_ip='1' ");
       //}else{//取门诊默认单位
    	//   sql.append(" and cvt.flag_op='1' ");
       //}
       sql.append(" and dt.pk_pd in ( ");
       sql.append(pk_pds);
       sql.append(" ) and st.eu_direct = 1 and  dt.quan_min -nvl(dt.quan_outstore,0) > 0 and dt.flag_finish = '0' ");
       sql.append(" group by dt.pk_pdstdt,dt.pk_pd,st.date_chk, dt.date_fac,store.pack_size,pd.pack_size ,store.pk_unit ,");
       sql.append(" dt.date_expire, dt.batch_no, dt.quan_min,dt.quan_outstore,dt.price_cost,dt.price,pd.name");
       if ("0".equals(eu_outtype)){//按入库日期正序
    	   sql.append(" order by dt.pk_pd,st.date_chk");
       }else if("1".equals(eu_outtype)||"3".equals(eu_outtype)){//按失效日期正序
    	   sql.append(" order by dt.pk_pd,dt.date_expire");
       }else if("2".equals(eu_outtype)){//按入库日期倒序
    	   sql.append(" order by dt.pk_pd,st.date_chk desc ");
       }
		List<PdOutDtParamVo>  list =  DataBaseHelper.queryForList(sql.toString(), PdOutDtParamVo.class, new Object[]{});
		return list;
	}
	
	/**
	 * 根据部门确定仓库出库方式
	 * @param pk_Store
	 * @return
	 */
	public String queryEuOutType(String pk_Store){
		if(CommonUtils.isEmptyString(pk_Store))
			throw new BusException("未获取到仓库主键");
		Map<String,Object> map = DataBaseHelper.queryForMap("select eu_outtype from bd_store where pk_store = ? ", new Object[]{pk_Store});
		if(map!=null){
			return CommonUtils.getString(map.get("euOuttype"));
		}
		return null;
	}
	
	/**
	 * 根据物品确定仓库出库模式
	 * @param pk_pd
	 * @param pk_dept
	 * @return
	 */
	public String queryOutTypeByPdAndDept(String pk_pd,String pk_dept){
		String sql = "select sto.pk_store,sto.eu_outtype from bd_store sto "+
				" inner join bd_pd_store pdsto on pdsto.pk_store = sto.pk_store "+
				" where pdsto.pk_pd = ? and pdsto.pk_dept = ? ";
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sql, new Object[]{pk_pd,pk_dept});
		if(list!=null&&list.size()>0){
			return CommonUtils.getString(list.get(0).get("euOuttype"));
		}
		return null;
	}

	
	/**
	 * 获取当日医嘱退药单
	 * @return
	 */
	public String getPdStIn(String pkOrgAp,String pkDeptAp,String sttype,User user,String pk_store,String direct){
		String date = DateUtils.getDateStr(new Date());
		String sql = "select pk_pdst from pd_st where to_char(date_st,'YYYYMMDD') = ? and "
				+ "dt_sttype = ? and pk_dept_st = ? and pk_store_st = ? and pk_org= ? and eu_direct = ?";
		if(!CommonUtils.isEmptyString(pkOrgAp)){
			sql = sql + " and pk_org_lk = '"+pkOrgAp+"' ";
		}
		if(!CommonUtils.isEmptyString(pkDeptAp)){
			sql = sql + " and pk_dept_lk = '"+pkDeptAp+"' ";
		}
		PdSt st = DataBaseHelper.queryForBean(sql, PdSt.class, new Object[]{date,sttype,user.getPkDept(),pk_store,user.getPkOrg(),direct});
		if(st!=null){
			return st.getPkPdst();
		}
		return null;
	}
	
	/**
	 * 构建出库单
	 * @param stvo
	 * @param u
	 * @return
	 */
	public PdSt createPdst(String pkOrgAp,String pkDeptAp,PdStVo stvo,User u,String dttype,String direct){
		PdSt st = new PdSt();
		if(stvo==null||stvo.getCodeSt()==null||"".equals(stvo.getCodeSt())){
			if("1".equals(direct)){
				st.setCodeSt(ScmPubUtils.getInStoreCode());
			}else{
				st.setCodeSt(ScmPubUtils.getOutStoreCode());
			}
		}else{
			st.setCodeSt(stvo.getCodeSt());
		}
		st.setDateChk(new Date());
		st.setDateSt(new Date());
		st.setDelFlag("0");
		st.setDtSttype(dttype);
		st.setEuDirect(direct);
		st.setEuStatus("1");
		st.setFlagChk("1");
		st.setFlagPay("0");
		st.setFlagPu("0");
		st.setNameEmpChk(u.getNameEmp());
		st.setNameEmpOp(u.getNameEmp());
		st.setNameSt(u.getNameEmp());
		st.setPkDeptSt(u.getPkDept());
		st.setPkEmpChk(u.getPkEmp());
		st.setPkEmpOp(u.getPkEmp());
		st.setPkOrg(u.getPkOrg());
		st.setPkDeptLk(pkDeptAp);
		st.setPkOrgLk(pkOrgAp);
		st.setPkStoreSt(u.getPkStore());
		if(stvo!=null){
			st.setNote(stvo.getNote());
			st.setFlagPu(stvo.getFlagPu());
			st.setPkPdstSr(stvo.getPkPdstSr());
			st.setPkSupplyer(stvo.getPkSupplyer());
		}
		return st;
	}
	
	/**
	 * 构建出库明细
	 * @param dt
	 * @param u
	 * @return
	 */
	public PdStDetail createPdstdt(PdOutDtParamVo dt,PdStDtVo stdt,User u,String pk_pdst,int i){
		PdStDetail pddt = new PdStDetail();
		pddt.setDelFlag("0");
		pddt.setDisc(1.00);
		pddt.setFlagChkRpt("0");
		pddt.setFlagFinish("0");
		pddt.setFlagPay("0");
		pddt.setPkOrg(u.getPkOrg());
		pddt.setPkPdstdt(NHISUUID.getKeyId());
		pddt.setPkPdst(pk_pdst);
        pddt.setSortNo(i);
		ApplicationUtils.setBeanComProperty(pddt, true);
		//根据不同对象设置不同的属性值
		setDiffProperties(pddt,dt,stdt);
		return pddt;
	}
	
	private void setDiffProperties(PdStDetail pddt,PdOutDtParamVo dt,PdStDtVo stdt){
		if(stdt!=null){
			pddt.setBatchNo(stdt.getBatchNo());
			pddt.setDateExpire(stdt.getDateExpire());
			pddt.setDateFac(stdt.getDateFac());
			pddt.setPackSize(stdt.getPackSize());
			pddt.setPkPd(stdt.getPkPd());
			pddt.setPkUnitPack(stdt.getPkUnitPack());
			pddt.setPrice(stdt.getPrice());
			pddt.setPriceCost(stdt.getPriceCost());
			pddt.setQuanPack(MathUtils.div(MathUtils.sub(stdt.getQuanMin(), stdt.getQuanOutstore()), CommonUtils.getDouble(stdt.getPackSize())));
	        pddt.setQuanMin(MathUtils.sub(stdt.getQuanMin(), stdt.getQuanOutstore()));
	        double packsize = CommonUtils.getDouble(getPackSize(stdt.getPkPd()));
			pddt.setAmount(MathUtils.mul(MathUtils.div(stdt.getPrice(), packsize), pddt.getQuanMin()));
			pddt.setAmountCost(MathUtils.mul(MathUtils.div(stdt.getPriceCost(), packsize), pddt.getQuanMin()));
		}else if(dt!=null){
			pddt.setBatchNo(dt.getBatchNo());
			pddt.setDateExpire(dt.getDateExpire());
			pddt.setDateFac(dt.getDateFac());
			pddt.setPackSize(dt.getPackSize());
			pddt.setPkPd(dt.getPkPd());
			pddt.setPkUnitPack(dt.getPkUnitPack());
			pddt.setPrice(dt.getPrice());
			pddt.setPriceCost(dt.getPriceCost());
			pddt.setQuanPack(MathUtils.div(dt.getQuanOutMin(),CommonUtils.getDouble(dt.getPackSize())));
			pddt.setQuanMin(dt.getQuanOutMin());
			double packsize = CommonUtils.getDouble(getPackSize(dt.getPkPd()));
			pddt.setAmount(MathUtils.mul(MathUtils.div(dt.getPrice(), packsize), pddt.getQuanMin()));
			pddt.setAmountCost(MathUtils.mul(MathUtils.div(dt.getPriceCost(), packsize), pddt.getQuanMin()));
		}
		
	}
	
	/**
	 * 获取物品名称
	 * @param pk_pd
	 * @return
	 */
	public String getPdNameByPk(String pk_pd){
		BdPd pd =  DataBaseHelper.queryForBean("select name from bd_pd where pk_pd = ?", BdPd.class, new Object[]{pk_pd});
	    if(pd==null)
	    	return "";
	    return pd.getName();
	}

	/**
	 * 获取零售单位的包装量
	 * @param pk_pd
	 * @return
	 */
	public Integer getPackSize(String pk_pd){
		BdPd pd = DataBaseHelper.queryForBean("select pack_size from bd_pd where pk_pd = ?", BdPd.class, new Object[]{pk_pd});
		if(pd == null)
			return 1;
		return pd.getPackSize();
	}
	
	public String getStoreByDept(String pk_dept){
		String sql = "select  pk_store from bd_store  where pk_dept = ? ";
		Map<String,Object> map = DataBaseHelper.queryForMap(sql, new Object[]{pk_dept});
		String pk_store = "";
		if(map!=null){
			pk_store =  CommonUtils.getString(map.get("pkStore"));
		}
		if(CommonUtils.isEmptyString(pk_store)){
			throw new BusException("根据科室主键"+pk_dept+"未获取到对应的仓库！");
		}
		return pk_store;
	}
	
	/**
	 * 锁定预留量
	 */
	public void lockStorePrepNum(Map<String,Object> paramMap,String wheresql){
		String sql = "select quan_prep from pd_stock where "+wheresql
				     +" and pk_pd in (:pkPds) and pk_store = :pkStore for update of quan_prep"   ;   //对quan_prep字段进行加锁
		if(Application.isSqlServer()){
			sql = "select quan_prep from pd_stock  with (rowlock,xlock) where "+wheresql
					   + " and pk_pd in (:pkPds) and pk_store = :pkStore "   ;   
		}
		DataBaseHelper.queryForList(sql, paramMap);
	}
	
	
	
}
