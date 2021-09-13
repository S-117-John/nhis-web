package com.zebone.nhis.ex.pub.support;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 护士站获取系统参数工具类
 * @author yangxue
 *
 */
public class ExSysParamUtil {
	
	/**
	 * 获取生成预执行单天数 
	 * @param 
	 * @return
	 */
	public static String getExListDays() {
		String days = ApplicationUtils.getSysparam("EX0020",true);
		if(days == null ||days.equals("")){
			days = "1";
		}
	    return days;
	}
	
	/**
	 * 每毫升滴数
	 * @param isPub 是否全部参数
	 * @return
	 */
	public static String getSpeeds() {
		return ApplicationUtils.getSysparam("EX0004",false);
	}
	
	/**
	 * 获取生成执行单具体截止时间 --已使用
	 * @param 
	 * @return
	 */
	public static String getExListEndTime(){
		//默认235959
		String endtime = ApplicationUtils.getSysparam("EX0021",true);
		if(endtime == null ||endtime.equals("")){
			endtime = "235959";
		}else{
			endtime = endtime.replaceAll(":", "");
		}
		return endtime;
	}
	/**
	 * 获取医嘱核对时停止医嘱的截止时间
	 * @return
	 */
     public static String getCheckStopEndTime(){
	   String endtime = ApplicationUtils.getSysparam("EX0022",true);
		if(endtime == null ||endtime.equals("")){
			endtime = DateUtils.getSpecifiedDateStr(new Date(), 1)+"235959";//默认明天晚上
		}else{
			endtime = DateUtils.getSpecifiedDateStr(new Date(), 1)+endtime.replaceAll(":", "");
		}
		return endtime;
	}
	
     /**
 	 * 获取医嘱核对时医嘱的排序方式，0降序，1升序
 	 * @return
 	 */
    public static String getOrdByOrder(){
    	String ordOrder = ApplicationUtils.getSysparam("EX0069",false);
    	if(ordOrder == null || "".equals(ordOrder)){
    		ordOrder = "0";
    	}
 		return ordOrder;
 	}
	
	/**
	 * 获取护理等级
	 * @param isPub 是否全部参数
	 * @return
	 */
	public static String[] getNurGradeParam(){
		return new String[] {ApplicationUtils.getSysparam("EX0011", true),
				ApplicationUtils.getSysparam("EX0012", true),ApplicationUtils.getSysparam("EX0013", true),
				ApplicationUtils.getSysparam("EX0010", true)};
	}
	
	/**
	 * 获取病情等级
	 * @param isPub 是否全部参数
	 * @return
	 */
	public static String[] getConditionGradeParam(){
		return new String[] {ApplicationUtils.getSysparam("EX0014",true),
				ApplicationUtils.getSysparam("EX0015", true)};
	}
	
	
	/**
	 * 获取住院药品绑定模式 
	 * @param 
	 * @return
	 */
	public static String getBindModeParam(){
		return ApplicationUtils.getSysparam("EX0005",false);
	}
	
	
	/**
	 * 获取请领生成自动解锁时间-- 23已占用
	 * @param
	 * @return
	 */
	public static String getUnlockTime(){
		String time= ApplicationUtils.getSysparam("EX0023",true);
		if(time == null ||time.equals("")) time = "10";
		return time;
	}
	
	
	
	/**
	 * 获取儿科科室编码 --未使用
	 * @param isPub 是否全部参数
	 * @return
	 */
	public static String getPedCodes(){
		return ApplicationUtils.getSysparam("CN001",false);
	}
	
	/**
	 * 获取儿科第一瓶主键
	 * @param isPub 是否全部参数
	 * @return
	 */
	public static String getPivasFrist(){
		return ApplicationUtils.getSysparam("BL0012",false);
	}
	
	/**
	 * 获取儿科每加一瓶主键
	 * @param isPub 是否全部参数
	 * @return
	 */
	public static String getPivasOther(){
		return ApplicationUtils.getSysparam("BL0013",false);
	}
	/** 
	 * 请领单编号
	 * @return
	 */
	public static String getAppCode(){
	    String num = ApplicationUtils.getCode("0501");
	    if(num == null ||"".equals(num)){
	    	num = UUID.randomUUID().toString().substring(0, 16);  
	    }
	    return num;
	}
	/**
	 * 获取婴儿住院号
	 * 生成规则：母亲住院号+“B”+婴儿序号
	 * @return
	 */
	public static String getInfantCodeIp(String pkPv_mother){
		//1、获取系统参数中，住院号的婴儿分割符
		String codeIpSpc = ApplicationUtils.getSysparam("PI0017", false);
		if(CommonUtils.isEmptyString(codeIpSpc)) 
			throw new BusException("请维护系统参数【PI0017】- 婴儿分隔符！");
		String sql = " select pi.code_ip from pi_master pi inner join pv_encounter pv "
				+ " on pv.pk_pi = pi.pk_pi where pv.pk_pv = ?";
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sql, new Object[]{pkPv_mother});
		if(list!=null&&list.size()>0&&list.get(0).get("codeIp")!=null)
			return CommonUtils.getString(list.get(0).get("codeIp"))+codeIpSpc+getInfantSortNo(pkPv_mother);
		//无住院号的情况，为婴儿生成新的住院号
		return ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZY)+codeIpSpc;
	}
	
	/**
	 * 获取婴儿姓名
	 * 生成规则：母亲姓名+“B”+婴儿序号
	 * @return
	 */
	public static String getInfantNamePi(String pkPv_mother){
		//1、获取系统参数中，住院号的婴儿分割符
		String codeIpSpc = ApplicationUtils.getSysparam("PI0017", false);
		if(CommonUtils.isEmptyString(codeIpSpc)) 
			throw new BusException("请维护系统参数【PI0017】- 婴儿分隔符！");
		String sql = " select pi.name_pi from pi_master pi inner join pv_encounter pv "
				+ " on pv.pk_pi = pi.pk_pi where pv.pk_pv = ?";
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sql, new Object[]{pkPv_mother});
		if(list!=null&&list.size()>0&&list.get(0).get("namePi")!=null){
			return CommonUtils.getString(list.get(0).get("namePi"))+codeIpSpc+getInfantSortNo(pkPv_mother);
		}
			
		return ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZY)+codeIpSpc;
	}
	
	/**
	 * 获取婴儿序号
	 * 规则：按系统中某一母亲的所有婴儿全局排序，顺序递增
	 * @param pkPv_mother
	 * @return
	 */
	public static Integer getInfantSortNo(String pkPv_mother){
		String sql = "select max(inf.sort_no) from pv_infant inf "
				+ " inner join pv_encounter pv on pv.pk_pi = inf.pk_pi where pv.pk_pv = ? ";
		Integer sortno = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkPv_mother});
		if(sortno == null) 
			sortno = 1;
		else 
			sortno = sortno + 1;
		return sortno;
	}

	/**
	 * 获取可维护婴儿的科室编码
	 * @param isPub 是否全部参数
	 * @return
	 */
	public static String getCodeDeptOfAddInf(){
		return ApplicationUtils.getSysparam("EX0025",false);
	}
	
	/**
	 * 根据参数【EX0025】判断当前科室是否允许添加婴儿
	 * @param pkDeptNs
	 * @param pkOrg
	 * @return
	 */
	public static boolean getFlagAddInfByDeptNs(String pkDeptNs,String pkOrg){
		String deptCodeOfAddInf = ApplicationUtils.getSysparam("EX0025",false);;//获取可维护婴儿的科室编码
		if(CommonUtils.isEmptyString(deptCodeOfAddInf)) return false;
		deptCodeOfAddInf  = deptCodeOfAddInf.replace(",", "','");
		int cnt =DataBaseHelper.queryForScalar("select count(1) cnt from bd_ou_dept dept "
				+ " where dept.del_flag = '0' and dept.pk_org = ? "
				+ " and dept.pk_dept = ? and dept.code_dept in('"+deptCodeOfAddInf+"')", Integer.class, new Object[]{pkOrg,pkDeptNs});
		return cnt > 0;
	}
	
	/**
	 * 获取婴儿床位分割符
	 * @param isPub 是否全部参数
	 * @return
	 */
	public static String getSpcOfCodeBed(){
		return ApplicationUtils.getSysparam("BD0007",false);
	}
	
	/**
	 * 婴儿床位收费项目
	 *
	 * @param isPub 是否全部参数
	 * @return
	 */
	public static String getPkItemOfInfBed(String pkDeptNs) {
		String codeBedItem = ApplicationUtils.getDeptSysparam("BD0010", pkDeptNs);//获取婴儿收费项目编码
		if (!CommonUtils.isEmptyString(codeBedItem)) {
			List<BdItem> items = DataBaseHelper.queryForList("select item.* from bd_item item "
					+ "where item.code=? and item.del_flag = '0' and item.flag_active = '1' ", BdItem.class, new Object[]{codeBedItem});
			if (null != items && items.size() > 0)
				return items.get(0).getPkItem();
		}
		return "";
	}
}
