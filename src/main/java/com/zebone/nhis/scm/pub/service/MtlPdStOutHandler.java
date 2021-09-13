package com.zebone.nhis.scm.pub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.pub.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 物品出库
 * @author wj
 *
 */
@Service
public class MtlPdStOutHandler{
	/**
	 * 锁确认发药物品批次（并发控制）
	 * @param pk_store
	 * @return
	 */
	public void lockConfirmPdSt(List<String> pkPds,String pk_store) {
		
		String sql = "select quan_outstore from pd_st_detail dt  where "
				   + "exists( select 1 from pd_st st where st.pk_store_st=:pkStore and st.del_flag='0' and st.eu_direct = 1 and dt.pk_pdst=st.pk_pdst) "
				   + " and dt.pk_pd in(:pkPds) and dt.del_flag='0' and dt.flag_finish = '0' and dt.quan_min - dt.quan_outstore > 0  for update of quan_outstore"   ;   //对quan_outstore字段进行加锁
		if(Application.isSqlServer()){
			sql = "select quan_outstore from pd_st_detail dt  with (rowlock,xlock) where "
					   + "exists( select 1 from pd_st st where st.pk_store_st=:pkStore and st.del_flag='0' and st.eu_direct = 1 and dt.pk_pdst=st.pk_pdst) "
					   + " and dt.pk_pd in(:pkPds) and dt.del_flag='0' and dt.flag_finish = '0' and dt.quan_min - dt.quan_outstore > 0 "   ;   
		}
		Map<String,Object> pMap = new HashMap<String,Object>();
		pMap.put("pkStore", pk_store);
		pMap.put("pkPds", pkPds);
		DataBaseHelper.queryForList(sql, pMap);
		
	}

	
	public List<PdOutDtParamVo> confirmPdSt(String pk_pd,String pk_dept,String pk_store,Double quan_min,String eu_outtype) {
		//根据物品，出库模式获取入库明细
		return confirmPdStQuan(pk_pd, quan_min, queryInStDt(eu_outtype,pk_dept,pk_store, pk_pd));
	}

	public List<PdOutDtParamVo> confirmPdSt(String pk_store,String eu_outtype,Double quanMin, MtlPdBatchVo batchVo) {
		//根据物品，出库模式获取入库明细
		return confirmPdStQuan(batchVo.getPkPd(), quanMin, queryInStDt(eu_outtype,pk_store,batchVo));
	}

	private List<PdOutDtParamVo> confirmPdStQuan(String pk_pd, Double quan_min, List<PdOutDtParamVo> dtlist) {
		if(dtlist==null||dtlist.size()<=0)
			throw new BusException("未获取到与物品"+getPdNameByPk(pk_pd)+"相匹配的可用入库记录，无法完成出库操作！");
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
	 * 发药查询对应入库记录
	 * @param eu_outtype
	 * @param pk_dept
	 * @param pk_store
	 * @param pk_pd
	 * @return
	 */
	public List<PdOutDtParamVo> queryInStDt(String eu_outtype, String pk_dept,String pk_store,String pk_pd){
		return queryInStDtPrivate(eu_outtype,pk_dept,pk_store,pk_pd,null);
	}

	public List<PdOutDtParamVo> queryInStDt(String eu_outtype,String pkStore, MtlPdBatchVo batchVo){
		return queryInStDtPrivate(eu_outtype,null,pkStore,batchVo.getPkPd(),batchVo);
	}

	private List<PdOutDtParamVo> queryInStDtPrivate(String eu_outtype, String pk_dept,String pk_store,String pk_pd, MtlPdBatchVo batchVo){
		if(CommonUtils.isEmptyString(eu_outtype)){
			eu_outtype = queryOutTypeByPdAndDept(pk_pd,pk_dept);
			if(CommonUtils.isEmptyString(eu_outtype))
				throw new BusException("未获取到仓库的出库模式！");
		}
		//如果有传入批次，就按照指定批次查询
		StringBuffer sqlBatch = null;
		if(batchVo != null){
			sqlBatch = new StringBuffer();
			sqlBatch.append(" and dt.price=").append(batchVo.getPrice())
					.append(" and dt.price_cost=").append(batchVo.getPriceCost());
			if(batchVo.getDateExpire() != null) {
				sqlBatch.append(" and nvl(to_char(dt.date_expire,'YYYYMMDD'),'~') ='")
						.append(DateUtils.dateToStr("yyyyMMdd",batchVo.getDateExpire())).append("'");
			}
			if(StringUtils.isNotBlank(batchVo.getBatchNo())){
				sqlBatch.append(" and dt.batch_no='").append(batchVo.getBatchNo()).append("'");
			}
			pk_pd = StringUtils.isBlank(pk_pd)?batchVo.getPkPd():pk_pd;
		}
		//2019-05-14 这里改为使用子查询关联库存表，避免库存有多条记录时，导致同批次多条库存记录，会导致调用此方法生成出库明细有多条。
		StringBuffer sql = new StringBuffer("");
		sql.append("select dt.pk_pdstdt,dt.pk_pd,st.date_chk, dt.date_fac,store.pack_size,pd.pack_size pack_size_pd,pd.pack_size_max,store.pk_unit as pk_unit_pack,store.posi_no,");
		sql.append("dt.date_expire, dt.batch_no, dt.quan_min,dt.quan_outstore,dt.price_cost,dt.price,dt.price/pd.pack_size*store.pack_size as price_store,pd.flag_single");
		sql.append(" ,unit.name as unit_name from pd_st_detail dt ");
		sql.append(" inner join bd_pd pd on pd.pk_pd = dt.pk_pd ");
		sql.append(" inner join bd_pd_store store on store.pk_pd = pd.pk_pd and store.flag_stop='0' inner join bd_unit unit on store.PK_UNIT = unit.pk_unit");
		sql.append(" inner join pd_st st on dt.pk_pdst=st.pk_pdst ");
		sql.append(" where dt.pk_pd=? and st.eu_direct = 1 and  dt.quan_min -dt.quan_outstore > 0 and dt.flag_finish = '0' ");
		if(!CommonUtils.isEmptyString(pk_store)){
			sql.append(" and st.pk_store_st = '").append(pk_store).append("' and store.pk_store='").append(pk_store).append("' ");
		}else if(!CommonUtils.isEmptyString(pk_dept)){
			sql.append(" and st.pk_dept_st = '").append(pk_dept).append("' and store.pk_dept = '").append(pk_dept).append("' ");
		}
		if(sqlBatch != null) {
			sql.append(sqlBatch);
		}
		sql.append(" and exists(select 1 from pd_stock stk where stk.pk_pd = dt.pk_pd and stk.batch_no = dt.batch_no and round(cast(stk.price as numeric),4) = round(cast(dt.price as numeric),4) ");
		sql.append(" and round(cast(stk.price_cost as numeric),4) = round(cast(dt.price_cost as numeric),4) ");
		sql.append(" and nvl(to_char(stk.date_expire,'YYYYMMDD'),'~') = nvl(to_char(dt.date_expire,'YYYYMMDD'),'~')");
		if(!CommonUtils.isEmptyString(pk_store)){
			sql.append(" and stk.pk_store = '").append(pk_store).append("'");
		}else if(!CommonUtils.isEmptyString(pk_dept)){
			sql.append(" and stk.pk_dept = '").append(pk_dept).append("'");
		}
		sql.append(")");
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
	 * 批量确认发药物品批次
	 * @param pk_pds
	 * @param pk_dept
	 * @param pk_store
	 * @param eu_outtype
	 * @param outlist
	 * @param flag_ip
	 * @return
	 */
	public List<PdOutDtParamVo> confirmBatchPdSt(String pk_pds,String pk_dept,String pk_store,String eu_outtype,List<PdOutParamVo> outlist,boolean flag_ip) {
		//根据物品，出库模式获取入库明细
		List<PdOutDtParamVo> dtlist = queryBatchInStDt(eu_outtype,pk_dept,pk_store,pk_pds,flag_ip);
		if(dtlist==null||dtlist.size()<=0) throw new BusException("未获取到可用入库记录，无法完成出库操作！");
		List<PdOutDtParamVo> result = new ArrayList<PdOutDtParamVo>();
		//取出库物品列表
		for(PdOutParamVo outvo:outlist){
			//对同一个物品进行计算批次
			double total_min = 0;
			List<PdOutDtParamVo> pdlist = new ArrayList<PdOutDtParamVo>();
			for(PdOutDtParamVo dtvo:dtlist){
				if(outvo.getPkPd().equals(dtvo.getPkPd())){
					dtvo.setPkCnord(outvo.getPkCnOrd());
					dtvo.setPkPv(outvo.getPkPv());
					dtvo.setPkPdapdt(outvo.getPkPdapdt());
					//按顺序累加，若数量
					double use_min = MathUtils.sub(dtvo.getQuanMin(), dtvo.getQuanOutstore());//可用数量
					total_min = MathUtils.add(total_min, use_min);
					//还不足够出库数量,添加到出库列表
					if(total_min <= outvo.getQuanMin() ){
						dtvo.setQuanOutMin(use_min);
						dtvo.setQuanOutPack(MathUtils.div(use_min, CommonUtils.getDouble(dtvo.getPackSize())));
						pdlist.add(dtvo);
					}else{//已经满足需要出库的数量
						if(MathUtils.compareTo(MathUtils.sub(MathUtils.sub(total_min, use_min),outvo.getQuanMin()),0.00) >= 0){  
							break;
						}else {
							dtvo.setQuanOutMin(MathUtils.sub(outvo.getQuanMin(), MathUtils.sub(total_min, use_min)));
							dtvo.setQuanOutPack(MathUtils.div(dtvo.getQuanOutMin(), CommonUtils.getDouble(dtvo.getPackSize())));
							pdlist.add(dtvo);
						}
					}
				}
			}
			result.addAll(pdlist);
		}
		return result;
	}

	/**
	 * 根据仓库出库模式，批量获取物品入库明细列表
	 * @param eu_outtype
	 * @param pk_store
	 * @param pk_dept
	 * @return
	 */
	public List<PdOutDtParamVo> queryBatchInStDt(String eu_outtype, String pk_dept,String pk_store,String pk_pds,boolean flag_ip){
	   if(CommonUtils.isEmptyString(eu_outtype)) 
		   throw new BusException("未获取到仓库的出库模式！");
		
	   StringBuffer sql = new StringBuffer(""); 
	   sql.append("select dt.pk_pdstdt,dt.pk_pd,st.date_chk, dt.date_fac,pd.pack_size,pd.pack_size pack_size_pd,pd.pk_unit_pack,");  
	   sql.append("dt.date_expire, dt.batch_no, dt.quan_min,dt.quan_outstore,dt.price_cost,dt.price from pd_st_detail dt ");
	   sql.append(" inner join bd_pd pd on pd.pk_pd = dt.pk_pd ");
	   sql.append(" inner join bd_pd_store store on store.pk_pd = pd.pk_pd and store.flag_stop='0' ");
	   sql.append(" inner join pd_st st on dt.pk_pdst=st.pk_pdst ");
	   sql.append(" inner join pd_stock stk on stk.pk_pd = dt.pk_pd and stk.batch_no = dt.batch_no and stk.price = dt.price ");
	   sql.append(" and stk.price_cost = dt.price_cost "); 
	   sql.append(" and nvl(to_char(stk.date_expire,'YYYYMMDD'),'~') = nvl(to_char(dt.date_expire,'YYYYMMDD'),'~') ");
		
		
       if(!CommonUtils.isEmptyString(pk_store)){
    	   sql.append(" and stk.pk_store = '"+pk_store+"' where st.pk_store_st = '"+pk_store+"' and store.pk_store='"+pk_store+"'");
       }else if(!CommonUtils.isEmptyString(pk_dept)){
    	   sql.append(" and stk.pk_dept = '"+pk_dept+"' where st.pk_dept_st = '"+pk_dept+"' and store.pk_dept = '"+pk_dept+"' ");
       }
       sql.append(" and dt.pk_pd in ( ");
       sql.append(pk_pds);
       sql.append(" ) and st.eu_direct = 1 and  dt.quan_min -dt.quan_outstore > 0 and dt.flag_finish = '0' ");
       if ("0".equals(eu_outtype)){//按入库日期正序
    	   sql.append(" order by st.date_chk,dt.pk_pd");
       }else if("1".equals(eu_outtype)||"3".equals(eu_outtype)){//按失效日期正序
    	   sql.append(" order by dt.pk_pd,dt.date_expire");
       }else if("2".equals(eu_outtype)){//按入库日期倒序
    	   sql.append(" order by st.date_chk desc,dt.pk_pd ");
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
	 * 根据仓库确定仓库出库模式
	 * @param pk_store
	 * @return
	 */
	public String queryOutTypeByPdAndDept(String pk_store){
		String sql = "select pk_store,eu_outtype from bd_store where pk_store=? " ;
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sql, new Object[]{pk_store});
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
		if("1".equals(direct)){
			st.setCodeSt(ScmPubUtils.getInStoreCode());
		}else{
			st.setCodeSt(ScmPubUtils.getOutStoreCode());
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
		st.setPkOrgLk(stvo.getPkOrgLk());
		st.setPkDeptLk(stvo.getPkDeptLk());
		st.setPkStoreLk(stvo.getPkStoreLk());
		st.setNote(stvo.getNote());
		if(stvo!=null){
			st.setPkPdstSr(stvo.getPkPdst());
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
			pddt.setQuanPack(MathUtils.div(MathUtils.sub(stdt.getQuanMin(), (stdt.getQuanOutstore()==null?Double.valueOf("1"):stdt.getQuanOutstore())), CommonUtils.getDouble(stdt.getPackSize())));
	        pddt.setQuanMin(MathUtils.sub(stdt.getQuanMin(), (stdt.getQuanOutstore()==null?Double.valueOf("1"):stdt.getQuanOutstore())));
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
	
	/**
	 * 根据科室获取仓库主键
	 * @param pk_dept
	 * @return
	 */
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
