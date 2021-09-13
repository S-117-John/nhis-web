package com.zebone.nhis.scm.pub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdReprice;
import com.zebone.nhis.common.module.scm.st.PdRepriceDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.vo.MtlPdRepriceDtVo;
import com.zebone.nhis.scm.pub.vo.MtlPdRepriceHistVo;
import com.zebone.nhis.scm.pub.vo.MtlPdRepriceVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 调价公共服务
 * @author Wj
 *
 */
@Service
public class MtlPdRepricePubService {
   
	/**
     * 保存调价单
     * @param param
     * @param user
     * @param vo
     */
	public MtlPdRepriceVo saveReprice(String param,User user,MtlPdRepriceVo vo){
		if(vo == null) return null;
		PdReprice pre = new PdReprice();
		ApplicationUtils.copyProperties(pre,vo);
		if(CommonUtils.isEmptyString(vo.getPkPdrep())){//新增
			DataBaseHelper.insertBean(pre);
		}else{//修改
			DataBaseHelper.updateBeanByPk(pre,false);
		}
		List<MtlPdRepriceDtVo> dtlist = vo.getDtlist();
		if(dtlist!=null&&dtlist.size()>0){
			List<PdRepriceDetail> insert_list = new ArrayList<PdRepriceDetail>();
			List<PdRepriceDetail> update_list = new ArrayList<PdRepriceDetail>();
			for(MtlPdRepriceDtVo dt:dtlist){
				if(CommonUtils.isEmptyString(dt.getPkPdrepdt())){//新增
					dt.setPkPdrep(pre.getPkPdrep());
					dt.setPkPdrepdt(NHISUUID.getKeyId());
					ApplicationUtils.setBeanComProperty(dt, true);
					insert_list.add(dt);
				}else{
	    			update_list.add(dt);
				}
			}
			if(insert_list!=null&&insert_list.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdRepriceDetail.class), insert_list);
			}
			if(update_list!=null&&update_list.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(PdRepriceDetail.class), update_list);
			}
			//删除明细pd_reprice_detail
	    	if(vo.getDelDtList()!=null&&vo.getDelDtList().size()>0){
	    		DataBaseHelper.getJdbcTemplate().batchUpdate("delete from pd_reprice_detail where pk_pdrepdt = ? ", vo.getDelDtList());
	    	}
		}
		vo.setPkPdrep(pre.getPkPdrep());
		return vo;
	}
	
	/**
	 * 定时调价
	 * @param pkOrg
	 */
	public void execFixedReprice(String pkOrg){
		//查询定时调价的调价单
		String sql = "select pk_pdrep,code_rep from pd_reprice where eu_repmode = '1' and eu_status ='1' and pk_org = ? ";
		List<Map<String,Object>> replist = DataBaseHelper.queryForList(sql, new Object[]{pkOrg});
		if(replist == null||replist.size() <=0)
			throw new BusException("未获取到需要定时调价的调价单");
		List<String> sqls = new ArrayList<String>();
		for(Map<String,Object> map :replist){
			String pk_pdrep = CommonUtils.getString(map.get("pkPdrep"));
			String code_rep = CommonUtils.getString(map.get("codeRep"));
			List<MtlPdRepriceHistVo> hslist = this.queryHistByCode(code_rep,pk_pdrep);
			execReprice(hslist);//执行仓库调价，3张表
			updatePrePriceHist(code_rep);//更新调价历史
			StringBuffer sqlup=new StringBuffer();
			sqlup.append("update pd_reprice set eu_status='2'");
			sqlup.append(" ,ts = to_date('"+DateUtils.getDate("yyyyMMddHHmmss")+"','YYYYMMDDHH24MISS')  ");
			sqlup.append(" where pk_pdrep='"+pk_pdrep+"'");
			sqls.add(sqlup.toString());//更新调价单状态
		}
		if(sqls!=null&&sqls.size()>0){
			DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));
		}
	}
	/**
	 * 查询调价单对应的调价记录
	 * @param pk_pdrep
	 * @return
	 */
	public List<MtlPdRepriceHistVo> queryHistByCode(String code_rep,String pk_pdrep){
		StringBuffer sqls=new StringBuffer();
		sqls.append(" select dt.price,dt.pk_pd,his.pk_store,pd.pack_size from pd_reprice_detail dt");
		sqls.append(" inner join pd_reprice_hist his on his.pk_pd=dt.pk_pd and dt.pk_pdrepdt=his.pk_pdrepdt");
		sqls.append(" inner join bd_pd pd on pd.pk_pd=dt.pk_pd");
		sqls.append(" where dt.pk_pdrep=?");
		List<MtlPdRepriceHistVo> reslist= DataBaseHelper.queryForList(sqls.toString(), MtlPdRepriceHistVo.class, new Object[]{pk_pdrep});
		return reslist;
	}
	
	/**
	 * 执行调价
	 * @param dtlist
	 */
	public void execReprice(List<MtlPdRepriceHistVo> dtlist){
		String pk_pdrep = dtlist.get(0).getPkPdrep();
		//更新物品价格
		updatePdPrice(pk_pdrep);
		//更新未完成出库的入库单
		updatePdst(dtlist);
		//更新库存表里的价格
		updateStock(dtlist);
	}
	
	
	/**
	 * 更新调价单状态
	 * @param eu_repmode 调价方式
	 * @param pk_pdrep 调价单主键
	 */
	public void updateRepriceApp(String eu_repmode,String pk_pdrep,User u){
		Map<String,Object> map = new HashMap<String,Object>();
		
		String sql = "update pd_reprice  set eu_status=:euStatus,date_chk=:dateChk"
				+ " ,pk_emp_chk=:pkEmp,name_emp_chk=:nameEmp,flag_chk='1',ts=:dateChk";
		if("0".equals(eu_repmode)){//实时调价
			sql = sql +",date_effe = :dateChk ";
			map.put("euStatus", "2");
		}else{
			map.put("euStatus", "1");
		}
		sql = sql + " where pk_pdrep=:pkPdrep";
		map.put("dateChk", new Date());
		map.put("pkEmp", u.getPkEmp());
		map.put("nameEmp", u.getNameEmp());
		map.put("pkPdrep", pk_pdrep);
		DataBaseHelper.update(sql, map);
	}
	
	/**
	 * 更新物品价格--根据调价明细（非调价历史）
	 * @param pk_pdrep
	 */
	public void updatePdPrice(String pk_pdrep){
		if(CommonUtils.isEmptyString(pk_pdrep)) return;
		String sql = "select pk_pd,price from pd_reprice_detail where pk_pdrep = ?";
		List<PdRepriceDetail> list = DataBaseHelper.queryForList(sql,PdRepriceDetail.class,new Object[]{pk_pdrep});
        List<String> sqls = new ArrayList<String>(); 
        for(PdRepriceDetail vo:list){
        	String sqldt = "update bd_pd  set price="+vo.getPrice()+""
        			+ " ,ts = to_date('"+DateUtils.getDate("yyyyMMddHHmmss")+"','YYYYMMDDHH24MISS') "
        			+ " where pk_pd='"+vo.getPkPd()+"'";
        	sqls.add(sqldt);
        }
        DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));
	}
	
	/**
	 * 更新未完成出库的入库单
	 * @param list
	 */
	public void updatePdst(List<MtlPdRepriceHistVo> list){
		if(list == null||list.size()<=0) return;
        List<String> sqls = new ArrayList<String>(); 
        for(MtlPdRepriceHistVo vo:list){
        	StringBuffer sql=new StringBuffer();
        	sql.append("update pd_st_detail  set pd_st_detail.price="+vo.getPrice()+"");
        	sql.append(" ,pd_st_detail.amount = pd_st_detail.quan_min*"+MathUtils.div(vo.getPrice(),CommonUtils.getDouble(vo.getPackSize())));
        	sql.append(" ,pd_st_detail.ts = to_date('"+DateUtils.getDate("yyyyMMddHHmmss")+"','YYYYMMDDHH24MISS')  ");
        	sql.append(" where exists (select 1  from pd_st where pd_st_detail.pk_pdst=pd_st.pk_pdst ");
        	sql.append( "   and pd_st.eu_direct=1 and pd_st_detail.flag_finish=0 ");
        	sql.append("   and pd_st_detail.pk_pd='"+vo.getPkPd()+"' ");
        	sql.append("   and pd_st.pk_store_st ='"+vo.getPkStore()+"')");
        	sqls.add(sql.toString());
        }
        DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));
	}
	
	/**
	 * 更新库存表里的价格
	 * @param list
	 */
	public void updateStock(List<MtlPdRepriceHistVo> list){
		if(list == null||list.size()<=0) return;
        List<String> sqls = new ArrayList<String>(); 
        for(MtlPdRepriceHistVo vo:list){
        	String sql = "update pd_stock  set price="+vo.getPrice()
        			+ " ,amount="+vo.getPrice()+"/"+vo.getPackSize()+ "*quan_min"
        		    + " ,ts = to_date('"+DateUtils.getDate("yyyyMMddHHmmss")+"','YYYYMMDDHH24MISS') "
        		    + " where pk_pd='"+vo.getPkPd()+"' and pk_store='"+vo.getPkStore()+"' ";
        	sqls.add(sql);
        }
        DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));
	}
	
	/**
	 * 更新调价记录的调价日期
	 * @param code_rep
	 */
	public void updatePrePriceHist(String code_rep){
	   if(CommonUtils.isEmptyString(code_rep)) return;
       String sql = "update pd_reprice_hist set date_reprice=:dateChk "
       		+ " ,ts = to_date('"+DateUtils.getDate("yyyyMMddHHmmss")+"','YYYYMMDDHH24MISS')  "
       		+ " where code_rep = '"+code_rep+"'";
       Map<String,Object> map = new HashMap<String,Object>();
       map.put("dateChk", new Date());
       DataBaseHelper.update(sql, map);
	}
	
}
