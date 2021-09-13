package com.zebone.nhis.cn.opdw.service;

import java.util.*;

import com.zebone.nhis.cn.opdw.vo.BdOrdSetShare;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Lis;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.opdw.dao.OpOrderMapper;
import com.zebone.nhis.cn.opdw.dao.OpTemplatesMapper;
import com.zebone.nhis.cn.opdw.vo.OpFavoritesVo;
import com.zebone.nhis.cn.opdw.vo.OpTemplatesVo;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdDept;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdLab;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdRis;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSet;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSetDt;
import com.zebone.nhis.common.module.cn.opdw.PiOrdTemp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OpOrderTemplatesService {
	
	@Autowired
	private OpTemplatesMapper OpTemplates;
	@Autowired
	private OpOrderMapper OpOrder;
	/**
	 * 获得医嘱模板或模板明细列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getOpTemplatesAndDetail(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map.get("mode")!=null){
			if	(map.get("mode").toString().equals("0"))  // 处方模板
				return OpTemplates.getTemplates(map);
			else if (map.get("mode").toString().equals("1"))  //处方模板明细
				return OpTemplates.getTemplatesDetail(map);
			else if	(map.get("mode").toString().equals("2"))  //治疗医嘱模板
				return OpTemplates.getTemplatesCure(map);  
			else if	(map.get("mode").toString().equals("3"))//检查治疗明细
				return OpTemplates.getTemplatesCureDetail(map);
			else if (map.get("mode").toString().equals("4"))//草药模板
				return OpTemplates.getHerbTemplates(map);
			else if (map.get("mode").toString().equals("5"))//草药明细
				return OpTemplates.getHerbTemplatesDetail(map);
		}
		 //获取模板明细 不区分类型
	   return OpTemplates.getAllDetails(map);
	}
	/**
	 * 保存医嘱模板
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveOpTemplates(String param,IUser user){
		OpTemplatesVo opTemplates = JsonUtil.readValue(param, new TypeReference<OpTemplatesVo>(){} );
		List<BdOrdSet> TemplatesList = opTemplates.getBdOrderSet();
		List<BdOrdSetDt> TempDetailList = opTemplates.getBdOrderSetDt();
		//if(TempDetailList.size()==0) return;
		for(int i=0; i<TemplatesList.size(); i++){
			BdOrdSet vTemp = TemplatesList.get(i);
			if(Constants.RT_NEW.equals(vTemp.getRowStatus()))
			{
				int year = vTemp.getCreateTime().getYear();
				if(year <= 0) vTemp.setCreateTime(new Date());
				year = vTemp.getTs().getYear();
				if(year <= 0) vTemp.setTs(new Date());
				
				if (!ValidateTemplates(vTemp, 0))
					throw new BusException("存在相同编码或名称的模板，请重新输入！");
				DataBaseHelper.insertBean(vTemp);
				if(TempDetailList!=null && TempDetailList.size()>0){
					if	(!(TempDetailList.size() == 1 && Constants.RT_LOAD.equals(TempDetailList.get(0).getRowStatus())))
					{
						if (!InsertDetail(TempDetailList))
							throw new BusException("新增NEW模板明细失败！");
					}
				}
			}
			else if(Constants.RT_UPDATE.equals(vTemp.getRowStatus()))
			{
				if (!ValidateTemplates(vTemp, 1))
					throw new BusException("存在相同编码或名称的模板，请重新输入！");
				if(TempDetailList!=null && TempDetailList.size()>0){
					if	(!(TempDetailList.size() == 1 && Constants.RT_LOAD.equals(TempDetailList.get(0).getRowStatus())))
					{
						if (!DeleteDetail(vTemp.getPkOrdset().toString()))
							throw new BusException("删除UPDATE模板明细失败！");
						if (!InsertDetail(TempDetailList))
							throw new BusException("新增UPDATE模板明细失败！");
					}
				}
				
				DataBaseHelper.updateBeanByPk(vTemp);
			}
			else if(Constants.RT_LOAD.equals(vTemp.getRowStatus()))  //追加模板明细
			{
				if(TempDetailList!=null && TempDetailList.size()>0){
					if (!InsertDetail(TempDetailList))
					    throw new BusException("新增UPDATE模板明细失败！");
				}
			}			
			else if(Constants.RT_REMOVE.equals(vTemp.getRowStatus()))
			{
				if (!DeleteDetail(vTemp.getPkOrdset().toString()))
					throw new BusException("删除REMOVE模板明细失败！");
				DataBaseHelper.deleteBeanByPk(vTemp);	
			}		
		}
	}
	
	private boolean ValidateTemplates(BdOrdSet vTemp, int mode) {
		User user = UserContext.getUser();
		String sql = "";
		int ret =0;
		String euRight = vTemp.getEuRight();
		String sqlWhere="";
		if("1".equals(euRight)) sqlWhere =" and eu_right='1' and pk_dept='"+vTemp.getPkDept()+"'" ;
		else if("2".equals(euRight)) sqlWhere = " and eu_right='2' and pk_emp='"+vTemp.getPkEmp()+"'";
		else sqlWhere = " and eu_right='0' ";
		if	(mode == 0)  //新增模板
		{
			sql = "select count(*) from bd_ord_set where (code = ? or name = ?) and pk_org = ? and del_flag = '0'";
			ret = DataBaseHelper.queryForScalar(sql+sqlWhere, Integer.class, vTemp.getCode(), vTemp.getName(), user.getPkOrg());
		}
		else  //修改模板
		{
			sql = "select count(*) from bd_ord_set where (code = ? or name = ?) and pk_org = ? and pk_ordset <> ? and del_flag = '0'";
			ret = DataBaseHelper.queryForScalar(sql+sqlWhere, Integer.class, vTemp.getCode(), vTemp.getName(), user.getPkOrg(), vTemp.getPkOrdset());
		}
		return ret == 0;
	}
	private boolean DeleteDetail(String pkOrdset) {
		String sql_del = "delete from bd_ord_set_dt where pk_ordset=?";
		DataBaseHelper.execute(sql_del, pkOrdset);	
		return true;
	}
	
	private boolean InsertDetail(List<BdOrdSetDt> DetailList) {
		for(int i=0; i<DetailList.size(); i++){
			BdOrdSetDt vDetail = DetailList.get(i);
			int year = vDetail.getTs().getYear();
			if(year <= 0) vDetail.setTs(new Date());
			
			year = vDetail.getCreateTime().getYear();
			if(year <= 0) vDetail.setCreateTime(new Date());
			DataBaseHelper.insertBean(vDetail);
		}
		return true;
	}	
	
	/**
	 * 获得患者常用药品列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getOpFavorites(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
    	return OpTemplates.getFavorites(map);
    }	
	
	/**
	 * 保存患者常用
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveOpFavorites(String param,IUser user){
		OpFavoritesVo opFavorites = JsonUtil.readValue(param, new TypeReference<OpFavoritesVo>(){} );
		List<PiOrdTemp> favList = opFavorites.getFavList();
		List<PiOrdTemp> favlListDel = opFavorites.getFavListDel();	
		for(int i=0; i<favList.size(); i++){
			PiOrdTemp fav = favList.get(i);
			if(Constants.RT_NEW.equals(fav.getRowStatus()))
			{
				ApplicationUtils.setDefaultValue(fav, true);
				fav.setModityTime(null);
				DataBaseHelper.insertBean(fav);
			}
			else if(Constants.RT_UPDATE.equals(fav.getRowStatus()))
			{
				
				DataBaseHelper.updateBeanByPk(fav, false);
			}
			else if(Constants.RT_REMOVE.equals(fav.getRowStatus()))
				DataBaseHelper.deleteBeanByPk(fav);	
		}		
		for(int i=0; i<favlListDel.size(); i++){
			PiOrdTemp fav = favlListDel.get(i);
			DataBaseHelper.deleteBeanByPk(fav);	
		}
	}
	/**
	 * 获得复制处方及明细列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getOpOrderCopy(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		map.put("systemTime", Calendar.getInstance().getTime());
		return OpTemplates.getOpOrderCopy(map);
    }	
	
	/**
	 * 校验编码和名称是否唯一
	 * 004003007030
	 * @param param
	 * @param user
	 * @return
	 */
	public int isSole(String param,IUser user){
		BdOrdSet bdOrdSet = JsonUtil.readValue(param, BdOrdSet.class);
		//@todo区分是新增校验还是修改校验
		int mode=StringUtils.isBlank(bdOrdSet.getPkOrdset())?0:1;//0新增 1修改
		boolean noRepeat = ValidateTemplates(bdOrdSet, mode);
//		Map<String, String> params = new HashMap<String,String>();
//		params.put("pkOrg", bdOrdSet.getPkOrg());
//		params.put("pkDept", bdOrdSet.getPkDept());
//		params.put("pkEmp", bdOrdSet.getPkEmp());
//		params.put("code", bdOrdSet.getCode());
//		params.put("name", bdOrdSet.getName());
		int count = noRepeat? 0:1;
//		count = OpTemplates.ischeckoutSoleCodeOrName(params);
		return count;
		
	}
	
	/**
	 * 根据医嘱主键查询默认执行机构和科室
	 * 004003007031
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOrdDept> queryOrdDept(String param,IUser user){
		BdOrdDept bdOrdDept = JsonUtil.readValue(param, BdOrdDept.class);
		Map<String, String> params = new HashMap<String,String>();
		params.put("pkOrd", bdOrdDept.getPkOrd());
		List<BdOrdDept> org = OpTemplates.getBdOrdDepts(params);
		return org;
		
	}
	
	/**
	 * 根据主键查询检查项目定义
	 * 004003007032
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOrdRis> queryRisDescAtt(String param,IUser user){
		BdOrd bdOrd = JsonUtil.readValue(param, BdOrd.class);
		Map<String, String> params = new HashMap<String,String>();
		params.put("pkOrd", bdOrd.getPkOrd());
		List<BdOrdRis> ris = OpTemplates.getRisDescAtt(params);
		return ris;		
	}
	
	/**
	 * 根据主键查询检验项目定义
	 * 004003007033
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOrdLab> queryLabNote(String param,IUser user){
		BdOrd bdOrd = JsonUtil.readValue(param, BdOrd.class);
		Map<String, String> params = new HashMap<String,String>();
		params.put("pkOrd", bdOrd.getPkOrd());
		List<BdOrdLab> lab = OpTemplates.getLabNote(params);
		return lab;
		
	}


	/**
	 * 根据模板主键查询共享科室
	 * 004003007035
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryOrdSetShare(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map.get("pkOrdset") == null){
			throw new BusException("模板主键为空！");
		}
		List<Map<String,Object>> ordSetShare = OpTemplates.getOrdSetShare(map);
		return ordSetShare;
	}

	/**
	 * 保存模板的共享科室
	 * 004003007037
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveOrdsetShare(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map.get("pkOrdset") == null){
			throw new BusException("模板主键为空！");
		}
		//List<BdOrdSetShare> ordSetShareList = JsonUtil.readValue(param, new TypeReference<List<BdOrdSetShare>>(){} );
		List<BdOrdSetShare> ordSetShareList = JsonUtil.readValue(JsonUtil.writeValueAsString(map.get("saveList")), new TypeReference<List<BdOrdSetShare>>(){} );


		//if(ordSetShareList == null || ordSetShareList.size() <= 0){
		//	throw new BusException("模板共享参数为空！");
		//}
		String pkOrdSet = map.get("pkOrdset").toString();
		//根据模板主键删除所有，再批量插入
		String delSql = "delete from BD_ORD_SET_SHARE where pk_ordset=?";
		int n = DataBaseHelper.execute(delSql, pkOrdSet);
		if(ordSetShareList != null && ordSetShareList.size() > 0){
			ordSetShareList.forEach(m->{
				ApplicationUtils.setDefaultValue(m,true);
			});
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdOrdSetShare.class), ordSetShareList);
		}
	}

	/**
	 * 根据模板主键查询未共享科室
	 * 004003007036
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryOrdSetUnShare(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map.get("pkOrdset") == null){
			throw new BusException("模板主键为空！");
		}
		List<Map<String,Object>> ordSetShare = OpTemplates.getOrdSetUnShare(map);
		return ordSetShare;
	}

	
	
}
