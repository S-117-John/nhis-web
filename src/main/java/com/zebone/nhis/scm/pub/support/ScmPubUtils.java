package com.zebone.nhis.scm.pub.support;

import org.apache.commons.lang3.StringUtils;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

import java.util.*;

public class ScmPubUtils {
	/**
	 * 出库单号
	 *
	 * @return
	 */
	public static String getOutStoreCode() {
		String appcode = ApplicationUtils.getCode("0708");
		if (appcode == null || "".equals(appcode)) {
			appcode = UUID.randomUUID().toString().substring(0, 16);
		}
		return appcode;
	}

	/**
	 * 调价单号
	 *
	 * @return
	 */
	public static String getRepCode() {
		String appcode = ApplicationUtils.getCode("0706");
		if (appcode == null || "".equals(appcode)) {
			appcode = UUID.randomUUID().toString().substring(0, 16);
		}
		return appcode;
	}

	/**
	 * 入库单号
	 *
	 * @return
	 */
	public static String getInStoreCode() {
		String appcode = ApplicationUtils.getCode("0702");
		if (appcode == null || "".equals(appcode)) {
			appcode = UUID.randomUUID().toString().substring(0, 16);
		}
		return appcode;
	}

	/**
	 * 药袋单号
	 *
	 * @return
	 */
	public static String getBagCode() {
		String appcode = ApplicationUtils.getCode("0507");
		if (appcode == null || "".equals(appcode)) {
			appcode = UUID.randomUUID().toString().substring(0, 16);
		}
		return appcode;
	}

	/**
	 * 是否存在包药机参数
	 *
	 * @return
	 */
	public static boolean HasPackParam(String pk_dept) {
		if ("1".equals(getDeptParam("EX0027", pk_dept))) {
			return true;
		}
		return false;
	}

	/**
	 * @return java.lang.String
	 * @Description 获取包药机参数
	 * @auther wuqiang
	 * @Date 2020-06-01
	 * @Param [pk_dept:当前科室]
	 */
	public static String getPackParam(String pk_dept) {
		if ("1".equals(getDeptParam("EX0027", pk_dept))) {
			return "1";
		} else if ("2".equals(getDeptParam("EX0027", pk_dept))) {
			return "2";
		}
		return "0";
	}


	/**
	 * 是否存在发放分类主键
	 *
	 * @param pk_dept
	 * @return
	 */
	public static String getPdDecate(String pk_dept) {
		return getDeptParam("EX0028", pk_dept);
	}

	/**
	 * 获取包药机设备编号
	 *
	 * @param pk_dept
	 * @return
	 */
	public static String getMachineNo(String pk_dept) {
		return getDeptParam("EX0031", pk_dept);
	}

	/**
	 * 获取部门级参数
	 *
	 * @param code
	 * @param pk_dept
	 * @return
	 */
	private static String getDeptParam(String code, String pk_dept) {
		Map<String, Object> map = DataBaseHelper.queryForMap("select arguval from BD_RES_PC_ARGU where pk_dept = ? and eu_range ='0' and code_argu = ? and  del_flag ='0' ", new Object[]{pk_dept, code});
		if (map == null || map.get("arguval") == null || "".equals(map.get("arguval").toString()))
			return null;
		return map.get("arguval").toString();
	}

	/**
	 * 药袋对应收费编码
	 *
	 * @return
	 */
	public static String getPdBagBlCode() {
		return ApplicationUtils.getSysparam("BL0019", false);
	}

	/**
	 * 中山二院需求：草药处方收煎药费用
	 *
	 * @param exPdList
	 * @param user
	 * @return
	 */
	public static List<BlPubParamVo> buildBoilBl(List<ExPdApplyDetail> exPdList, User user) {
		List<BlPubParamVo> resList = new ArrayList<BlPubParamVo>();
		String itemCode = ApplicationUtils.getSysparam("BL0032", false);
		if (StringUtils.isBlank(itemCode)) return resList;
		//组合处方数据
		String pkPreses = "";
		for (ExPdApplyDetail exPd : exPdList) {
			if (exPd.getPkPres() != null && !pkPreses.contains(exPd.getPkPres())) {
				pkPreses += "'" + exPd.getPkPres() + "',";
			}
		}
		if (pkPreses.length() <= 0) return resList;
		pkPreses = pkPreses.substring(0, pkPreses.length() - 1) + ")";
		StringBuffer resSql = new StringBuffer("SELECT ord.pk_cnord,ord.pk_wg pk_wg_ex,ord.pk_wg_org pk_wg_org, pres.pk_pres, ord.pk_pv, ord.pk_pi," +
				" ord.eu_pvtype euPvType, ord.pk_org       pk_org_app, ord.pk_dept  " +
				"    pk_dept_app,ord.pk_dept pk_dept_ex,ord.pk_emp_ord   pk_emp_ex, ord.name_emp_ord name_emp_ex, ord.pk_emp_ord   pk_emp_app, ord.name_emp_ord name_emp_app, " +
				" case when fried_num = '0' or fried_num is null then ORDS * 1 else ORDS * fried_num end  quan_cg");
		resSql.append(" FROM cn_order ord INNER JOIN cn_prescription pres ON pres.pk_pres = ord.pk_pres");
		resSql.append(" WHERE pres.eu_boil > 0 AND FRIED_NUM > 0  AND pres.pk_pres in (");
		resSql.append(pkPreses);
		resList = DataBaseHelper.queryForList(resSql.toString(), BlPubParamVo.class, new Object[]{});
		if (resList.size() <= 0) return resList;
		//根据系统参数保存的收费项目编码查询pk_item和价格
		String sql = "select pk_item,price from bd_item where code=?";
		Map<String, Object> boilData = DataBaseHelper.queryForMap(sql, itemCode);
		if (boilData == null) {
			throw new BusException("未查询到系统参数【BL0032】对应编码【" + itemCode + "]的收费项目！");
		}
		double price = 0;
		//组装公共记费参数
		for (BlPubParamVo blPubVo : resList) {
			blPubVo.setPkOrg(user.getPkOrg());
			blPubVo.setPkItem(boilData.get("pkItem").toString());
			if (boilData.get("price") != null)
				price = Double.parseDouble(boilData.get("price").toString());
			blPubVo.setPrice(price);
			blPubVo.setPkOrgEx(user.getPkOrg());
			blPubVo.setPkDeptEx(user.getPkDept());
			blPubVo.setFlagPd("0");
			blPubVo.setDateHap(new Date());
			blPubVo.setPkDeptCg(user.getPkDept());
			blPubVo.setPkEmpCg(user.getPkEmp());
			blPubVo.setNameEmpCg(user.getNameEmp());
			blPubVo.setEuBltype("1");
		}
		return resList;
	}
}
