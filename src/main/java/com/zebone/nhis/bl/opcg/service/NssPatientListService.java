package com.zebone.nhis.bl.opcg.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.dao.NssPatientListMapper;
import com.zebone.nhis.bl.pub.vo.NssBlInvoiceListVo;
import com.zebone.nhis.bl.pub.vo.NssPatientVo;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊清单
 * 
 * @author Administrator
 */
@Service
public class NssPatientListService {

	@Autowired
	private NssPatientListMapper nssPatientListMapper;
	/**
	 * 交易号：018004002001 查询患者半年的有效的收费日期集合
	 * 
	 * @param param
	 * @param user
	 * @return List<String>
	 */
	public List<NssPatientVo> getHalfYearPatiList(String param, IUser user) {

		List<NssPatientVo> paDateList = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = JsonUtil.readValue(param, Map.class);
		String pkPi = (String) params.get("pkPi");
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -6);
		Date date = calendar.getTime();

		String sql = "select date_st,pk_settle,pk_org,pk_pi,pk_pv"
				 + "   from BL_SETTLE" 
				 + "   where DATE_ST > ?"
				 + "   and PK_PI = ?" 
				 + "   and DEL_FLAG = '0'";
		
		paDateList = DataBaseHelper.queryForList(sql, NssPatientVo.class, new Object[]{date,pkPi});
		
		return paDateList;
	}

	/**
	 * 交易号：018004002002 查询患者半年的有效的收费日期集合
	 * 
	 * @param param
	 * @param user
	 * @return List<String>
	 * @throws ParseException 
	 */
	public List<Map<String, Object>> getCashDate(String param, IUser user) throws ParseException {

		@SuppressWarnings("unchecked")
		Map<String, Object> params = JsonUtil.readValue(param, Map.class);
		//截取日期字符串
		String strDate = params.get("dateSt").toString();
		StringBuffer stBuffer = new StringBuffer();
		stBuffer.append(strDate.substring(0,4))
				.append('-')
				.append(strDate.substring(4,6))
				.append('-')
				.append(strDate.substring(6,8));
		
		List<Map<String, Object>> stMap = DataBaseHelper.queryForList(
				"select st.pk_settle, "
						+ "to_char(st.date_st,'HH24:MI:SS') as date_st,"
		                + "nvl(st.amount_st,0) as amount_st,"
						+ "nvl(st.amount_pi,0) as amount_pi" + " from bl_settle st" + " where st.del_flag = '0'"
						+ "and PK_PI = ? "
						+ "and to_char(st.date_st,'YYYY-MM-DD') = ?",
//						+ "and DATEDIFF(day, st.date_st, ?) = 0",
				new Object[] { params.get("pkPi"), stBuffer.toString()});

		return stMap;
	}

	/**
	 * 交易号：018004002003 查询该收费记录的收费明细记录
	 * 
	 * @param param
	 * @param user
	 * @return List<BlInvoiceDt>
	 */
	public Page<NssBlInvoiceListVo> getChargeSchedule(String param, IUser user) {

		@SuppressWarnings("unchecked")
		Map<String, Object> params = JsonUtil.readValue(param, Map.class);
		int pageNum = Integer.valueOf(params.get("pageNum").toString());
		int pageSize = Integer.valueOf(params.get("pageSize").toString());
		String pkSettle = params.get("pkSettle").toString();
		
		MyBatisPage.startPage(pageNum,pageSize);	

		// 结算明细
		/**
		 * code_bill 账单项目编号 name_bill 账单项目名称 amount 全额
		 */
		List<NssBlInvoiceListVo> blIncoiecDt = nssPatientListMapper.getChargeSchedule(pkSettle);
		
		@SuppressWarnings("unchecked")
		Page<NssBlInvoiceListVo> page = MyBatisPage.getPage();
		page.setRows(blIncoiecDt);
		return page;

	}
}
