package com.zebone.nhis.compay.ins.lb.service.lxyb;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.compay.ins.lb.dao.lxyb.SzlxIpOrOpMapper;
import com.zebone.nhis.compay.ins.lb.service.pub.dao.LbYbPubMapper;
import com.zebone.nhis.compay.ins.lb.vo.lxyb.InsSzlxFymx;
import com.zebone.nhis.compay.ins.lb.vo.lxyb.InsSzlxJs;
import com.zebone.nhis.compay.ins.lb.vo.lxyb.InsSzlxMzdj;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class SzlxIpOrOpService {
	@Resource
	private SzlxIpOrOpMapper szlxIpOrOpMapper;
	@Resource
	private LbYbPubMapper lbYbPubMapper;
	/**
	 * 交易号【015001006001】
	 * 保存门诊/住院登记数据
	 * @param param
	 * @param user
	 */
	public InsSzlxMzdj saveHpAdmit(String param,IUser user){
		InsSzlxMzdj insSzlxMzdj=JsonUtil.readValue(param, InsSzlxMzdj.class);
		if(insSzlxMzdj==null)return null;
		if(insSzlxMzdj.getId()!=null){
		 	DataBaseHelper.updateBeanByPk(insSzlxMzdj, false);
		}else{
		  	DataBaseHelper.insertBean(insSzlxMzdj);
		}
		return insSzlxMzdj;
	}
	/**
	 * 交易号【015001006002】
	 * 根据pkPv删除门诊/住院登记数据
	 * @param param
	 * @param user
	 */
	public void delHpAdmit(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) throw new BusException("未获得就诊主键！");
		DataBaseHelper.update("update ins_szlx_mzdj set del_flag='1' where pk_pv=?", new Object[]{paramMap.get("pkPv")});
	}
	
	/**
	 * 交易号【015001006017】
	 * 根据pkPv删除门诊/住院登记数据
	 * @param param
	 * @param user
	 */
	public void delHpAdmitByDjdm(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) 
			throw new BusException("未获得就诊主键！");
		DataBaseHelper.update("update ins_szlx_mzdj set del_flag='1' where DJDM=?", new Object[]{paramMap.get("djdm")});
	}
	/**
	 * 交易号【015001006003】
	 * 根据就诊主键,去查询住院/门诊登记数据
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> getHpAdmit(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null||map.size()<=0)throw new BusException("未获得就诊主键！");
		List<Map<String, Object>> hpAdmit = szlxIpOrOpMapper.getHpAdmit(map);
		return hpAdmit.get(0);
	}
	
	/**
	 * 交易号【015001006016】
	 * 根据就诊主键,去查询住院/门诊登记数据
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> getHpAdmitByDjdm(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null||map.size()<=0)throw new BusException("未获得就诊主键！");
		List<Map<String, Object>> hpAdmit = szlxIpOrOpMapper.getHpAdmitByDjdm(map);
		return hpAdmit.get(0);
	}
	/**
	 * 根据his编码获取中心对照编码
	 * @param param
	 * @param user
	 * @return
	 *//*
	public List<Map<String,Object>> searchYbCompare(String param,IUser user){
		return null;
	}*/
	
	/**
	 * 交易号【015001006004】
	 * 保存处方明细分解数据,INS_SZlx_FYMX
	 * @param param
	 * @param user
	 */
	public List<InsSzlxFymx> saveCostAnalysisDetail(String param,IUser user){
		List<InsSzlxFymx> list=JsonUtil.readValue(param, new TypeReference<List<InsSzlxFymx>>() {});
		if(list==null|| list.size()<=0)throw new BusException("未获取相关信息！");
		for (InsSzlxFymx insSzlxFymx : list) {
			if(insSzlxFymx.getId()!=null){
				 DataBaseHelper.updateBeanByPk(insSzlxFymx, false);
			 }else{
				 DataBaseHelper.insertBean(insSzlxFymx);
			 }
		}
		return list;
	}	
	
	/**
	 * 交易号【015001006005】
	 * 保存费用结算返回数据,INS_SZLX_JS
	 * @param param
	 * @param user
	 */
	public InsSzlxJs saveSettlementReturnData(String param,IUser user){
		 InsSzlxJs insSzybJs=JsonUtil.readValue(param, InsSzlxJs.class);
		 if(insSzybJs==null)throw new BusException("未获得相关信息！");
		 if(insSzybJs.getId()!=null){
			 DataBaseHelper.updateBeanByPk(insSzybJs, false);
		 }else{ 
			 DataBaseHelper.insertBean(insSzybJs);
		 }
		 return insSzybJs;
	}
	
	/**
	 *交易号【015001006006】
	 * 根据登记代码, 删除ins_szlx_fymx数据
	 * @param param
	 * @param user
	 */
	public void delFymxData(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map!=null &&map.get("djdm")!=null){
			DataBaseHelper.update("update ins_szlx_fymx set del_flag='1' where djdm=?",new Object[]{ map.get("djdm")});
		}
	}
	
	/**
	 * 交易号【015001006007】
	 * 根据pkSettle 查询结算返回信息 INS_SZLX_JS
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> getYbJsData(String param,IUser user){
		Map<String, Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null|| map.size()<=0)throw new BusException("未获得相关信息！");
		return szlxIpOrOpMapper.getYbJsData(map);
	}
	
	/**
	 * 交易号【015001006008】
	 * 更新结算状态数据,DEL_FLAG置1 INS_SZLX_JS
	 * @param param
	 * @param user
	 */
	public void delYbJsData(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)throw new BusException("未获得相关信息！");
		if(paramMap.get("pkSettle") != null){
		     DataBaseHelper.update("update ins_szlx_js set del_flag='1' where pk_settle=?", new Object[]{paramMap.get("pkSettle")});
		}
		if(paramMap.get("id") != null){
		     DataBaseHelper.update("update ins_szlx_js set del_flag='1' where id=?", new Object[]{paramMap.get("id")});
		}
	}
	
	/**
	 * 交易号【015001006009】】
	 * 根据门诊登记主键跟新离休中的就诊主键
	 * @param param
	 * @param user
	 */
	public void updateMzdjPkPv(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return ;
		String sql="update ins_szlx_mzdj set pk_pv=? where id=?";
		DataBaseHelper.update(sql, new Object[]{paramMap.get("pkPv"),paramMap.get("ybRegPk")});
		
	}
	
	/**
	 * 015001006010
	 * 对照离休收费项目
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryLxYbItemInfo(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		if(map==null)return null;
		if(map.get("euMatch")!=null&&"1".equals(map.get("euMatch"))){
			return szlxIpOrOpMapper.qryLxItemWithInfo(map);
		}else if(map.get("euMatch")!=null && "2".equals(map.get("euMatch"))){
			return lbYbPubMapper.qryYbItemDicNoWithInfo(map);
		}else{
			return null;
		}
	}
	/**
	 * 015001006011
	 * 对照离休药品字典
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryLxPdWithInfo(String param,IUser user){
		Map<String,Object> map=JsonUtil.readValue(param, Map.class);
		String euActive = map.get("euActive").toString();
		if(map==null)return null;
		if(map.get("euMatch")!=null&&"1".equals(map.get("euMatch"))){
			if(euActive.equals("1")){//启用
				map.put("euActive", 0);
			}else{//停用
				map.put("euActive", 1);
			}
			return szlxIpOrOpMapper.qryLxPdWithInfo(map);
		}else if(map.get("euMatch")!=null && "2".equals(map.get("euMatch"))){
			if(euActive.equals("1")){//启用
				map.put("euActive", 0);
			}else{//停用
				map.put("euActive", 1);
			}
			return lbYbPubMapper.qryYbPdDicNoWithInfo(map);
		}else{
			return null;
		}
	}
	
	/**
	 * 根据pk_pv更新离休结算pk_settle
	 * @param param
	 * @param user
	 */
	public void updateLxJsPkSettle(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return;
		String sql="update INS_SZLX_JS set PK_SETTLE=? where pk_pv=? and del_flag='0'";
		DataBaseHelper.update(sql, new Object[]{paramMap.get("pkSettle"),paramMap.get("pkPv")});
	}
	/**
	 * 015001006014
	 * 根据pk_pv 查询离休费用明细：用于撤销部分上传的费用明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryLxFymx(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.get("pkPv")==null) throw new BusException("未获得就诊信息！");
		
		return szlxIpOrOpMapper.qryLxFymx(paramMap);
	}
	
	/**
	 * 015001006015
	 * 根据id将费用明细置为删除状态，再根据pkCgip/pkCgop将上传标记置为0（未上传）
	 * @param param{"id":"离休费用明细主键","pkCgip":"住院明细","pkCgop":"门诊明细"}
	 * @param user
	 */
	public void updateLxUploadInit(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap!=null){
			String fymxSql="update ins_szlx_fymx set del_flag='1' where id=?";
			DataBaseHelper.update(fymxSql, paramMap.get("id"));
			if(paramMap.get("pkCgip")!=null&& "".equals(paramMap.get("pkCgip"))){
				String ipSql="update bl_ip_dt set flag_insu='0' where pk_cgip=? and del_flag='0'";
				DataBaseHelper.update(ipSql, paramMap.get("pkCgip"));
			}else if(paramMap.get("pkCgop")!=null&& "".equals(paramMap.get("pkCgop"))){
				String opSql="update bl_op_dt set flag_insu='0' where pk_cgop=? and del_flag='0'";
				DataBaseHelper.update(opSql, paramMap.get("pkCgop"));
			}
		}
	}
	
	/**
	 * 015001006016
	 * 更新宿州离休医保结算表-费用明细表-登记信息表的关系
	 * @param param{"yBRegID":"登记主键","jsId":"结算主键（医保）","fymxIdList":"费用明细主键集合","pkPv":"就诊主键","pkSettle":"结算主键"}
	 * @param user
	 */
	public void updateJsAndFymxAndMzdjInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return;
		/*更新登记关系*/
		String djSql="update ins_szlx_mzdj set pk_pv=? where id=?";
		DataBaseHelper.update(djSql, new Object[]{paramMap.get("pkPv"),paramMap.get("yBRegID")});
		
		/*更新费用明细*/
		List<String> fymxIds=(List<String>)paramMap.get("fymxIdList");
		for (String id : fymxIds) {
			String fySql="update ins_szlx_fymx set pk_pv=? where id=?";
			DataBaseHelper.update(fySql, new Object[]{paramMap.get("pkPv"),id});
		}
		/*更新医保结算*/
		String jsSql="update ins_szlx_js set pk_pv=? ,pk_settle=? where id=?";
		DataBaseHelper.update(jsSql, new Object[]{paramMap.get("pkPv"),paramMap.get("pkSettle"),paramMap.get("jsId")});
	}
}
