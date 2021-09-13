package com.zebone.nhis.cn.opdw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.opdw.dao.SyxOpTempMapper;
import com.zebone.nhis.cn.opdw.vo.SyxOpTemplatesVo;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSet;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSetDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class SyxOpTempService {

	@Autowired
	private SyxOpTempMapper OpTemplates;
	/**
	 * 保存医嘱模板
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveOpTemplates(String param,IUser user){
		SyxOpTemplatesVo opTemplates = JsonUtil.readValue(param, new TypeReference<SyxOpTemplatesVo>(){} );
		List<BdOrdSet> addingList = opTemplates.getAddingList();
		List<BdOrdSet> editingList = opTemplates.getEditingList(); 
		List<BdOrdSet> removingList = opTemplates.getRemovingList();
		List<BdOrdSetDt> bdOrderSetDt = opTemplates.getBdOrderSetDt();
		List<BdOrdSetDt> deleteSetDtList = opTemplates.getDeleteSetDtList();
		Date d = new Date();
	
		if(addingList!=null && addingList.size()>0){
			for(BdOrdSet vTemp : addingList){
				if (!ValidateTemplates(vTemp, 0,opTemplates.getCheckName())) throw new BusException("存在相同编码或名称的模板，请重新输入！");
				vTemp.setFlagIp("0");
				vTemp.setFlagOp("1");
				DataBaseHelper.insertBean(vTemp);
			}
		}
		if(editingList!=null && editingList.size()>0){
			for(BdOrdSet vTemp : editingList){
				if (!ValidateTemplates(vTemp, 1,opTemplates.getCheckName())) throw new BusException("存在相同编码或名称的模板，请重新输入！");
				DataBaseHelper.updateBeanByPk(vTemp, false);
			}
		}
		if(bdOrderSetDt!=null && bdOrderSetDt.size()>0){
			List<BdOrdSetDt> insetDt = new ArrayList<BdOrdSetDt>();
			List<BdOrdSetDt> updateDt = new ArrayList<BdOrdSetDt>();
			for(BdOrdSetDt vTempDt :bdOrderSetDt){
				if(Constants.RT_NEW.equals(vTempDt.getRowStatus())){
					vTempDt.setPkOrdsetdt(NHISUUID.getKeyId());
					vTempDt.setTs(d);
					vTempDt.setDelFlag("0");
					insetDt.add(vTempDt);
				}else if(Constants.RT_UPDATE.equals(vTempDt.getRowStatus())){
					updateDt.add(vTempDt);
				}
			}
			if(insetDt.size()>0) DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdOrdSetDt.class),insetDt);
			if(updateDt.size()>0) DataBaseHelper.batchUpdate("update bd_ord_set_dt set pk_ord=:pkOrd,name_ord=:nameOrd,code_freq=:codeFreq,code_supply=:codeSupply,dosage=:dosage,pk_unit_dos=:pkUnitDos,quan=:quan,pk_dept_exec=:pkDeptExec,note=:note,days=:days,parent_no=:parentNo,FLAG_DISP=:flagDisp  where pk_ordsetdt=:pkOrdsetdt", updateDt);
		}
		if(removingList!=null && removingList.size()>0){
			DataBaseHelper.batchUpdate("delete from bd_ord_set_dt where exists (select * from bd_ord_set b where bd_ord_set_dt.pk_ordset = b.pk_ordset and b.pk_ordset=:pkOrdset) ", removingList);
			DataBaseHelper.batchUpdate(" delete from bd_ord_set where pk_ordset=:pkOrdset ", removingList);
		}
		if(deleteSetDtList!=null && deleteSetDtList.size()>0){
			DataBaseHelper.batchUpdate("delete from bd_ord_set_dt  where pk_ordsetdt=:pkOrdsetdt ", deleteSetDtList);
		}
		
	}
	public List<Map<String,Object>> getOpTemplatesAndDetail(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(map.get("mode")!=null){
			String dbType = MultiDataSource.getCurDbType();
			map.put("dbType", dbType);
			if	(map.get("mode").toString().equals("0"))  // 处方模板
				return OpTemplates.getTemplates(map);
			else if (map.get("mode").toString().equals("1"))  //处方模板明细
				return OpTemplates.getTemplatesDetail(map);
			else if	(map.get("mode").toString().equals("2") || map.get("mode").toString().equals("6"))  //治疗医嘱模板
				return OpTemplates.getTemplatesCure(map);  
			else if	(map.get("mode").toString().equals("3") || map.get("mode").toString().equals("7"))//检查治疗明细
				return OpTemplates.getTemplatesCureDetail(map);
			else if (map.get("mode").toString().equals("4"))//草药模板
				return OpTemplates.getHerbTemplates(map);
			else if (map.get("mode").toString().equals("5"))//草药明细
				return OpTemplates.getHerbTemplatesDetail(map);
			else if (map.get("mode").toString().equals("8"))//协定处方模板
				return OpTemplates.getHerbPresTemplates(map);
			else if (map.get("mode").toString().equals("10"))//全部模板
				return OpTemplates.getAll(map);
		}
		 //获取模板明细 不区分类型
	   return OpTemplates.getAllDetails(map);
	}
	private boolean ValidateTemplates(BdOrdSet vTemp, int mode,String checkName) {
		User user = UserContext.getUser();
		String sql = "";
		int ret =0;
		String euRight = vTemp.getEuRight();
		String sqlWhere="";
		if("1".equals(euRight)) sqlWhere =" and eu_right='1' and pk_dept='"+vTemp.getPkDept()+"'" ;
		else if("2".equals(euRight)) sqlWhere = " and eu_right='2' and pk_emp='"+user.getPkEmp()+"'";
		else sqlWhere = " and eu_right='0' ";
		if("1".equals(checkName)){ //个人模板校验名称是否重复
			if	(mode == 0)  //新增模板
			{
				sql = "select count(*) from bd_ord_set where code = ? and pk_org = ? and del_flag = '0'";
				ret = DataBaseHelper.queryForScalar(sql+sqlWhere, Integer.class, vTemp.getCode(), user.getPkOrg());
			}
			else  //修改模板
			{
				sql = "select count(*) from bd_ord_set where code = ? and pk_org = ? and pk_ordset <> ? and del_flag = '0'";
				ret = DataBaseHelper.queryForScalar(sql+sqlWhere, Integer.class, vTemp.getCode(), user.getPkOrg(), vTemp.getPkOrdset());
			}
		}else{ //只校验编码是否重复，名称可以重复
			if	(mode == 0)  //新增模板
			{
				if("2".equals(euRight)) {
					sql = "select count(*) from bd_ord_set where (code = ? or name = ?) and pk_org = ? and del_flag = '0'";
					ret = DataBaseHelper.queryForScalar(sql+sqlWhere, Integer.class, vTemp.getCode(), vTemp.getName(), user.getPkOrg());
				}else {
					sql = "select count(*) from bd_ord_set where (code = ?) and pk_org = ? and del_flag = '0'";
					ret = DataBaseHelper.queryForScalar(sql+sqlWhere, Integer.class, vTemp.getCode(), user.getPkOrg());
				}
			}
			else  //修改模板
			{
				if("2".equals(euRight)) {
					sql = "select count(*) from bd_ord_set where (code = ? or name = ?) and pk_org = ? and pk_ordset <> ? and del_flag = '0'";
					ret = DataBaseHelper.queryForScalar(sql+sqlWhere, Integer.class, vTemp.getCode(), vTemp.getName(), user.getPkOrg(), vTemp.getPkOrdset());
				}else {
					sql = "select count(*) from bd_ord_set where (code = ?) and pk_org = ? and pk_ordset <> ? and del_flag = '0'";
					ret = DataBaseHelper.queryForScalar(sql+sqlWhere, Integer.class, vTemp.getCode(), user.getPkOrg(), vTemp.getPkOrdset());
				}
			}
		}

		return ret == 0;
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
		String paramCn0042 = ApplicationUtils.getSysparam("CN0042", false);
		List<Map<String,Object>> ret = null;
		if("1".equals(paramCn0042))
		{
			//门诊西药和成药合并开立
			ret = OpTemplates.getFavoritesIgnoreDrugType(map);
		}
		else
		{
			ret = OpTemplates.getFavorites(map);
		}
    	return ret;
    }	
}
