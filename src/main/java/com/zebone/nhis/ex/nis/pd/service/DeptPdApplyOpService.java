package com.zebone.nhis.ex.nis.pd.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.nis.pd.dao.DeptPdApplyOpMapper;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 科室领药-门诊
 * @author jd
 *
 */
@Service
public class DeptPdApplyOpService {

	   @Resource
	   private DeptPdApplyOpMapper deptPdApplyOpMapper;
	   /**
	    * 查询已计费未退费，可生成请领的执行单列表
	    * @param param{pkDept--开立科室,codeIp,dateBegin,dateEnd,pdname}
	    * @return
	    */
	   public List<GeneratePdApExListVo> queryExCgList(String param,IUser user){
		   Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		   if(paramMap == null||CommonUtils.isNull(paramMap.get("pkDept")))
			   throw new BusException("未获取到要生成请领单的当前科室！");
		   return deptPdApplyOpMapper.qryUnPdApExList(paramMap);
	   }
	  
	   public void generatePdAp(String param,IUser user){
		   Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		   List<GeneratePdApExListVo> exlist = JsonUtil.readValue(JsonUtil.getJsonNode(param, "exlist"),new TypeReference<List<GeneratePdApExListVo>>(){});
		   if(exlist == null || exlist.size()<=0)
			   throw new BusException("未获取到需要生成请领单的物品信息！");
		   User u = (User)user;
		   
		   //针对毒麻药品添加该字段设置备注区分
		   String pdType = null != paramMap.get("pdType") ? paramMap.get("pdType").toString() : "";
		   //生成请领单
		   ExPdApply pdap = createPdAp(exlist.get(0).getPkOrgOcc(),exlist.get(0).getPkDeptOcc(),u,pdType);
		   DataBaseHelper.insertBean(pdap);
		   //创建请领明细并执行更新操作，回写bl_ip_dt中得数据
		   createApplyDt(exlist,u,pdap,paramMap);
	   }
	   
	   /**
	    * 根据病区查询请领单列表
	    * @param param{pkDeptNs,dateEnd,dateBegin,pkDeptDe}
	    * @param user
	    * @return
	    */
		public List<Map<String,Object>> queryPdAppByDept(String param,IUser user){
			Map<String,Object> map = JsonUtil.readValue(param, Map.class);
			String dateEnd = CommonUtils.getString(map.get("dateEnd"));
			if(dateEnd!=null&&!dateEnd.equals("")){
				map.put("dateEnd", dateEnd.substring(0,8)+"235959");
			}
			return deptPdApplyOpMapper.queryPdApply(map);
		}
		/**
		 * 根据请领单查询请领明细
		 * @param param{pkPdap}
		 * @param user
		 * @return
		 */
		public List<Map<String,Object>> queryPdApDetail(String param,IUser user){
			Map<String,Object> map = JsonUtil.readValue(param, Map.class);
			return deptPdApplyOpMapper.queryPdApDetail(map);
		}
		/**
		 * 取消请领
		 * @param param{选中行所有数据}
		 * @param user
		 */
		public void cancelApply(String param,IUser user){
			Map<String,Object> map= JsonUtil.readValue(param, Map.class);
			if(map == null) throw new BusException("未获取到需要取消的请领单信息！");
			String direct = CommonUtils.getString(map.get("euDirect"));
			User u = (User)user;
			map.put("dateCanc", new Date());
			if("1".equals(direct)){//请领
				//校验请领单是否发药
				String sql = "select pk_pdap from ex_pd_apply where (flag_cancel = '1' or date_de is not null ) and pk_pdap = :pkPdap  ";
				Map<String,Object> result = DataBaseHelper.queryForMap(sql, map);
				if(result!=null&&!CommonUtils.isEmptyString(CommonUtils.getString(result.get("pkPdap")))){
					throw new BusException("该请领单已经发药或取消，不允许取消!");
				}
				
				//更新收费记录数据
				StringBuilder dtBl = new StringBuilder("update bl_op_dt set pk_pdstdt = null,ts=:dateCanc   ");
				dtBl.append("where pk_pdstdt in (select dt.pk_pdapdt from ex_pd_apply_detail dt ");
				dtBl.append("inner join ex_pd_apply ap on dt.pk_pdap = ap.pk_pdap ");
				dtBl.append("where ap.pk_pdap = :pkPdap) ");
				DataBaseHelper.update(dtBl.toString(), map);	
			}
			  //执行更新请领状态
			   StringBuilder upAp = new StringBuilder("update ex_pd_apply set flag_cancel = '1' , pk_emp_cancel = :pkEmp, ");
			   upAp.append("name_emp_cancel = :nameEmp , pk_dept_cancel = :pkDept , date_cancel = :dateCanc ,");
			   upAp.append("eu_status = '9',ts=:dateCanc  where pk_pdap = :pkPdap ");
				map.put("pkEmp", u.getPkEmp());
				map.put("nameEmp", u.getNameEmp());
				map.put("pkDept", u.getPkDept());
				DataBaseHelper.update(upAp.toString(), map);
			//更新请领单明细状态
				map.put("reason", "门诊-科室取消");
				StringBuilder updateSql = new StringBuilder("update ex_pd_apply_detail set flag_stop = '1' , reason_stop = :reason,");
				updateSql.append("pk_emp_stop = :pkEmp, name_emp_stop = :nameEmp,ts=:dateCanc  ");
				updateSql.append("where pk_pdap = :pkPdap and flag_de = '0' and flag_stop = '0'");
				DataBaseHelper.update(updateSql.toString(), map);
		}
		
	   /**
		 * 
		 * @param ordVO
		 * @param param
		 * @return
		 * @throws BusException
		 */
		private ExPdApply createPdAp(String pk_org_occ,String pk_dept_occ,User user,String pdType){
			ExPdApply vo = new ExPdApply();
			vo.setPkOrg(user.getPkOrg());
			String pk_pdap = NHISUUID.getKeyId();
			vo.setPkPdap(pk_pdap);
			vo.setEuDirect("1");
			String code = ExSysParamUtil.getAppCode();
			vo.setCodeApply(code);
			vo.setEuAptype("3");//门诊科室领药
			vo.setPkDeptAp(user.getPkDept());
			vo.setPkEmpAp(user.getPkEmp());
			vo.setNameEmpAp(user.getNameEmp());
			vo.setDateAp(new Date());
			vo.setPkOrgDe(pk_org_occ);
			vo.setPkDeptDe(pk_dept_occ);
			vo.setFlagCancel("0");
			vo.setFlagFinish("0");
			vo.setEuStatus("0");
			vo.setCreateTime(new Date());
			vo.setCreator(user.getPkEmp());
			if("1".equals(pdType))//毒麻药品需要设置备注
				vo.setNote("毒麻领用");
			vo.setTs(new Date());
			vo.setDelFlag("0");
			return vo;
		}
		
		
		private void createApplyDt(List<GeneratePdApExListVo> ordlist,User user,ExPdApply appVO,Map<String,Object> paramMap) {
			List<ExPdApplyDetail> dtList=new ArrayList<ExPdApplyDetail>();
			List<String> upSqls=new ArrayList<String>();
			for (GeneratePdApExListVo exVo : ordlist) {
				ExPdApplyDetail appDt=new ExPdApplyDetail();
				String pkPdapdt=NHISUUID.getKeyId();
				appDt.setPkPdapdt(pkPdapdt);
				appDt.setPkOrg(user.getPkOrg());
				appDt.setPkPdap(appVO.getPkPdap());
				appDt.setEuDirect(EnumerateParameter.ONE);
				appDt.setPkPv(exVo.getPkPv());
				appDt.setPkPd(exVo.getPkPd());
				appDt.setPkCnord(exVo.getPkCnord());
				appDt.setEuDetype(EnumerateParameter.ZERO);
				appDt.setFlagBase(EnumerateParameter.ZERO);
				appDt.setFlagMedout(EnumerateParameter.ZERO);
				appDt.setFlagSelf(EnumerateParameter.ZERO);
				appDt.setPkUnit(exVo.getPkUnit());
				appDt.setPackSize(exVo.getPackSizeStore());
				appDt.setQuanPack(exVo.getQuanOcc());
				appDt.setQuanMin(MathUtils.mul(exVo.getQuanOcc(),CommonUtils.getDouble(exVo.getPackSizeStore())));
				appDt.setOrds(1);
				appDt.setPrice(exVo.getPriceMin());//零售单价
				appDt.setPriceCost(exVo.getPriceMin());//成本价格
				appDt.setAmount(exVo.getAmount());
				appDt.setFlagDe(EnumerateParameter.ZERO);
				appDt.setFlagFinish(EnumerateParameter.ZERO);
				appDt.setFlagStop(EnumerateParameter.ZERO);
				appDt.setFlagPivas(exVo.getFlagPivas());
				appDt.setCreator(user.getPkEmp());
				appDt.setCreateTime(new Date());
				appDt.setTs(new Date());
				appDt.setFlagCanc("0");
				dtList.add(appDt);
				StringBuffer blSql=new StringBuffer("update bl_op_dt set pk_pdstdt='");
				blSql.append(pkPdapdt);
				blSql.append("',batch_no = '");
				blSql.append(exVo.getBatchNo());
				if(!CommonUtils.isEmptyString(exVo.getPkCnord())){
					blSql.append("' where pk_cnord ='");
					blSql.append(exVo.getPkCnord());
				}else{
					blSql.append("' where pk_cgop ='");
					blSql.append(exVo.getPkCgop());
				}
				blSql.append("' and pk_pdstdt is null and pk_dept_ex='");
				blSql.append(user.getPkDept());
				blSql.append("' and pk_item='");
				blSql.append(exVo.getPkPd());
				blSql.append("' and date_cg>=to_date('"+paramMap.get("dateStart")+"','YYYYMMDDHH24MISS')"); 
				blSql.append(" and date_cg<=to_date('"+paramMap.get("dateEnd")+"','YYYYMMDDHH24MISS')");
				upSqls.add(blSql.toString());
			}
			if(dtList.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdApplyDetail.class),dtList);
			}
			if(upSqls.size()>0){
				DataBaseHelper.batchUpdate(upSqls.toArray(new String[0]));
			}
		}
		/**
		 * 计算仓库单位下的发放数量
		 * @param quan_min -- 基本单位下的数量
		 * @param pack_size--仓库单位下的包装量
		 * @return
		 */
		private double getQuanPack(double quan_min,int pack_size){
			double num = MathUtils.upRound(MathUtils.div(quan_min, CommonUtils.getDouble(pack_size)));
			return num;
		}
}
