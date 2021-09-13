package com.zebone.nhis.ma.pub.platform.zsrm.support;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.nhis.common.module.ex.nis.ns.*;
import com.zebone.nhis.common.module.scm.pub.BdFactory;
import com.zebone.nhis.common.module.scm.pub.BdPdAtt;
import com.zebone.nhis.common.module.scm.pub.BdPdAttDefine;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class QueryUtils {
	/**
	 * 根据codeOp查询患者信息
	 * @param codeOp
	 * @return
	 */
	public static List<Map<String ,Object>> queryPiByCodeOp(String codeOp) {
		if(codeOp ==null || "".equals(codeOp)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select * from PI_MASTER where CODE_OP=?");
		List<Map<String, Object>> queryPiByCodeOp = DataBaseHelper.queryForList(sql.toString(),codeOp);
		return queryPiByCodeOp;
	}

	/**
	 * 通过单位名称获取单位pk
	 * @param nameUnit
	 * @return
	 */
	public static  Map<String,Object> queryUnitByNameUnit(String nameUnit){
		if(nameUnit ==null || "".equals(nameUnit)){
			return null;
		}
		String sql = "SELECT pk_unit from bd_unit where name = ?";
		List<Map<String, Object>> queryUnitByNameUnit = DataBaseHelper.queryForList(sql.toString(),nameUnit);
		if(queryUnitByNameUnit == null || queryUnitByNameUnit.size() == 0){
			return null;
		}
		return queryUnitByNameUnit.get(0);
	}


	public static Map<String ,Object> queryPkEmpByCode(String code) {
		if(code ==null || "".equals(code)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select pk_emp,name_emp,code_emp,pk_org from BD_OU_EMPLOYEE where CODE_EMP=?");
		Map<String, Object> queryPkEmpByCode = DataBaseHelper.queryForMap(sql.toString(),code);
		return queryPkEmpByCode;
	}

	/**
	 * 通过设备名称获取设备id
	 * @param mspName
	 * @return
	 */
	public static  Map<String,Object> queryPkMspByMspName(String mspName){
		if(mspName ==null || "".equals(mspName)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select pk_msp from bd_res_msp where name=?");
		Map<String, Object> queryPkMspByMspName = DataBaseHelper.queryForMap(sql.toString(),mspName);
		return queryPkMspByMspName;
	}

	/**
	 * 通过单位名称获取单位id
	 * @param nameUnit
	 * @return
	 */
	public static  Map<String,Object> queryPkUnitByNameUnit(String nameUnit){
		if(nameUnit ==null || "".equals(nameUnit)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select pk_unit from bd_unit where name=?");
		Map<String, Object> queryPkUnitByNameUnit = DataBaseHelper.queryForMap(sql.toString(),nameUnit);
		return queryPkUnitByNameUnit;
	}

	/**
	 * 保存检验结果
	 * @param pkCnord
	 * @param codeApply
	 * @param exLabOccList
	 */
	public static int saveLisRptList(String pkCnord,String codeApply ,List<ExLabOcc> exLabOccList){
		if(exLabOccList==null || exLabOccList.size()==0) {
			return 0;
		}
		//作废原有记录
		DataBaseHelper.execute("delete ex_lab_occ where pk_cnord =? ", pkCnord);
		//插入新数据
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExLabOcc.class), exLabOccList);
		/*for (ExLabOcc exLabOcc : exLabOccList) {
			DataBaseHelper.insertBean(exLabOcc);
		}*/
		//更新报告状态
		//DataBaseHelper.update("update cn_lab_apply set eu_status ='4' where pk_cnord in (select pk_cnord from cn_order where cn_order.pk_pv = ? and code_apply in "+codeApplyStr+")",new Object[] { pkPv });
		int execute = DataBaseHelper.execute("update cn_lab_apply set eu_status ='4' where pk_cnord =? ",pkCnord);
		return execute;

	}

	/**
	 * 回收检验结果
	 * @param pkCnord
	 * @param codeApply
	 */
	public static int deleteLisRptList(String pkCnord,String codeApply){
		//作废原有记录
		DataBaseHelper.execute("delete ex_lab_occ where pk_cnord =? ", pkCnord);
		//更新报告状态
		int execute = DataBaseHelper.execute("update cn_lab_apply set eu_status ='3' where pk_cnord =? ",pkCnord);
		return execute;

	}

	/**
	 * 保存细菌结果
	 * @param exLabOccBactList
	 * @param exLabOccBactAlList
	 */
	public static void saveBactRptList(String codeApply, List<ExLabOccBact> exLabOccBactList, List<ExLabOccBactAl> exLabOccBactAlList) {
		//作废原有记录
		String sql = "delete EX_LAB_OCC_BACT where PK_LABOCC in (select EX_LAB_OCC.PK_LABOCC from EX_LAB_OCC where CODE_APPLY=?)";
		DataBaseHelper.execute(sql,codeApply);

		//插入新数据
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExLabOccBact.class), exLabOccBactList);
		/*for (ExLabOccBact exLabOccBact : exLabOccBactList) {
			DataBaseHelper.insertBean(exLabOccBact);
		}*/

	}


	/**
	 * 回收微生物检验结果
	 * @param pkCnord
	 * @param codeApply
	 */
	public static int deleteBactReportList(String pkCnord,String codeApply){
		//作废原有记录
		DataBaseHelper.execute("delete ex_lab_occ where pk_cnord =? ", pkCnord);
		String sql = "delete EX_LAB_OCC_BACT where PK_LABOCC in (select EX_LAB_OCC.PK_LABOCC from EX_LAB_OCC where CODE_APPLY=?)";
		DataBaseHelper.execute(sql,codeApply);
		//更新报告状态
		int execute = DataBaseHelper.execute("update cn_lab_apply set eu_status ='3' where pk_cnord =? ",pkCnord);
		return execute;

	}

	/**
	 * 新增检查执行，更新检查报告状态
	 * @param codeApply
	 * @param list
	 */
	public  static int saveRisRptList(String pkCnord,String codeApply,List<ExRisOcc> list){
		if(list==null||list.size()==0) return 0;
		//更新检查报告状态 （作废原有记录）
		DataBaseHelper.execute("delete ex_ris_occ where pk_cnord = ?", pkCnord);
		int count = DataBaseHelper.execute("update cn_ris_apply set eu_status ='4' where pk_cnord = ?", pkCnord);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExRisOcc.class), list);

		/*for (ExRisOcc exRisOcc : list) {
			DataBaseHelper.insertBean(exRisOcc);
		}*/

		//查询医辅执行表数据
		/*ExAssistOcc exAssistOcc = DataBaseHelper.queryForBean("select * from EX_ASSIST_OCC where PK_CNORD=?",ExAssistOcc.class, pkCnord);
		//更新门诊执行表数据
		if(exAssistOcc!=null){
			exAssistOcc.setDateOcc(list.get(0).getDateOcc());
			exAssistOcc.setPkOrgOcc(list.get(0).getPkOrg());
			exAssistOcc.setPkEmpOcc(list.get(0).getPkEmpOcc());
			exAssistOcc.setNameEmpOcc(list.get(0).getNameEmpOcc());
			exAssistOcc.setPkDeptOcc(list.get(0).getPkDeptOcc());
			exAssistOcc.setEuStatus("1");
			DataBaseHelper.updateBeanByPk(exAssistOcc);
		}*/
		return count;
	}

	/**
	 * 回收检查结果
	 * @param pkCnord
	 * @param codeApply
	 */
	public static int deleteRisRptList(String pkCnord,String codeApply){
		//作废原有记录
		DataBaseHelper.execute("delete ex_ris_occ where pk_cnord =? ", pkCnord);
		//更新报告状态
		int execute = DataBaseHelper.execute("update cn_ris_apply set eu_status ='3' where pk_cnord =? ",pkCnord);
		//查询医辅执行表数据
		/*ExAssistOcc exAssistOcc = DataBaseHelper.queryForBean("select * from EX_ASSIST_OCC where PK_CNORD=?",ExAssistOcc.class, pkCnord);
		//更新门诊执行表数据
		if(exAssistOcc!=null){
			exAssistOcc.setDateOcc(null);
			exAssistOcc.setEuStatus("0");
			DataBaseHelper.updateBeanByPk(exAssistOcc);
		}*/
		return execute;

	}




	/**
	 * 获取公共字典
	 * @param code
	 * @return
	 */
	public static BdDefdoc getbdDefdocInfo(String codeDefdoclist, String code){
		return DataBaseHelper.queryForBean("SELECT name from bd_defdoc where code_defdoclist = ? and code = ?", BdDefdoc.class,codeDefdoclist,code);
	}

	/**
	 * 获取药品附件属性
	 * @param code
	 * @return
	 */
	public static BdPdAttDefine getBdPdAttInfo(String pkPd, String code){
		return DataBaseHelper.queryForBean("SELECT def.* from bd_pd_att_define def left join  bd_pd_att att on att.pk_pdattdef = def.pk_pdattdef where att.pk_pd = ? and def.code_att = ?", BdPdAttDefine.class,pkPd,code);
	}

	/**
	 * 获取厂商信息
	 * @param pkFactory
	 * @return
	 */
	public static BdFactory getBdFactoryInfo(String pkFactory){
		return DataBaseHelper.queryForBean("SELECT name,code from bd_factory where pk_factory = ? ", BdFactory.class,pkFactory);
	}

	/**
	 * 获取药品等级（2自费，0甲类，1乙类）
	 * @param pkPd
	 * @return
	 */
	public static BdItemHp getBdItemHpInfo(String pkPd){
		return DataBaseHelper.queryForBean("SELECT eu_level from bd_item_hp where PK_ITEM = ? ", BdItemHp.class,pkPd);
	}

	/**
	 * 通过单位id获取单位名称
	 * @param id
	 * @return
	 */
	public static BdUnit queryUnitById(String id){
		return DataBaseHelper.queryForBean("select name from bd_unit where pk_unit=?",BdUnit.class,id);
	}

	/**
	 * 通过code获取频次
	 * @param code
	 * @return
	 */
	public static BdTermFreq queryBdTermFreqByCode(String code){
		return DataBaseHelper.queryForBean("select name from bd_term_freq where code=?",BdTermFreq.class,code);
	}

	public static User getDefaultUser(String deviceId) {
		User user = new User();
		Map<String, Object> bdOuMap = DataBaseHelper
				.queryForMap(
						"SELECT * FROM bd_ou_employee emp JOIN bd_ou_empjob empjob  ON empjob.pk_emp = emp.pk_emp WHERE empjob.del_flag = '0' and emp.del_flag = '0' and emp.CODE_EMP = ?",
						deviceId);
		if (bdOuMap != null) {
			user.setPkOrg(CommonUtils.getPropValueStr(bdOuMap, "pkOrg"));
			user.setNameEmp(CommonUtils.getPropValueStr(bdOuMap, "nameEmp"));
			user.setPkOrg(CommonUtils.getPropValueStr(bdOuMap, "pkOrg"));
			user.setNameEmp(CommonUtils.getPropValueStr(bdOuMap, "nameEmp"));
			user.setPkEmp(CommonUtils.getPropValueStr(bdOuMap, "pkEmp"));
			user.setPkDept(CommonUtils.getPropValueStr(bdOuMap, "pkDept"));
			user.setCodeEmp(CommonUtils.getPropValueStr(bdOuMap, "codeEmp"));
		}else {
			user = null;
		}
		return user;
	}

	/**
	 * 根据codeOp查询患者信息
	 * @param codeEmp
	 * @return
	 */
	public static List<Map<String ,Object>> queryOccDeptByCodeEmp(String codeEmp) {
		if(codeEmp ==null || "".equals(codeEmp)){
			return null;
		}
//		String sql = "select busa.pk_dept pk_dept_area, " +
//				"       job.pk_dept pk_dept_job\n" +
//				"from bd_dept_bus bus\n" +
//				"         inner join bd_dept_bu bu\n" +
//				"                    on bus.pk_deptbu = bu.pk_deptbu\n" +
//				"         inner join bd_dept_bus busa\n" +
//				"                    on bus.pk_deptbu = busa.pk_deptbu\n" +
//				"         inner join bd_ou_empjob job\n" +
//				"                    on job.pk_dept = bus.pk_dept\n" +
//				"         inner join bd_ou_employee emp\n" +
//				"                    on emp.pk_emp = job.pk_emp\n" +
//				"         inner join bd_ou_dept dept\n" +
//				"                    on busa.pk_dept = dept.pk_dept\n" +
//				"where busa.dt_depttype = '16'\n" +
//				"  and bu.dt_butype = '12'\n" +
//				"  and busa.del_flag = 0\n" +
//				"  and job.is_main = '1'\n" +
//				"  and emp.code_emp = ?";

		String sql = "select pk_dept pk_dept_area,pk_dept pk_dept_job from bd_ou_empjob\n" +
				"where is_main='1' and del_flag = '0' and code_emp= ?";
		List<Map<String, Object>> queryOccDeptByCodeEmp = DataBaseHelper.queryForList(sql,codeEmp);
		return queryOccDeptByCodeEmp;
	}
	/**
	 * 获取科室信息
	 * @param codeDept
	 * @return
	 */
	public static BdOuDept getDeptInfo(String codeDept){
		String sql="select * from bd_ou_dept where code_dept=?";
		BdOuDept dept=DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{codeDept});
		return dept;
	}
}
