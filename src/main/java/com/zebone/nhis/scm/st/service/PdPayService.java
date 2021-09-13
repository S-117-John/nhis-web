package com.zebone.nhis.scm.st.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.pub.BdSupplyer;
import com.zebone.nhis.common.module.scm.st.PdPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.st.dao.PdPayMapper;
import com.zebone.nhis.scm.st.vo.PdPayVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 付款处理
 * @author yangxue
 *
 */
@Service
public class PdPayService {
	@Resource
	private PdPayMapper pdPayMapper;
    /**
     * 查询供应商列表
     * @param param
     * @param user
     * @return
     */
	public List<Map<String,Object>> querySupplyer(String param,IUser user){
		String pk_org = ((User)user).getPkOrg();
		return pdPayMapper.querySupplyerList(pk_org);
	}
	/**
	 * 查询待付款列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PdStDtVo> queryToPayList(String param,IUser user){
		String pk_supply = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pk_supply)) throw new BusException("未获取到供应商信息！");
		String pk_org = ((User)user).getPkOrg();
		return pdPayMapper.queryToPayList(pk_org, pk_supply);
	}
	/**
	 * 查询已付款列表
	 * @param param{payNo,pkSupplyer,dtPaymode,dtBank,certNo,dateBegin,dateEnd,pkEmpPay}
	 * @param user
	 * @return
	 */
	public List<PdPayVo> queryPayList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String pk_org = ((User)user).getPkOrg();
		if(map == null){
			map = new HashMap<String,Object>();
		}  
    	if(CommonUtils.isNotNull(map.get("dateBegin"))){
			map.put("dateBegin", CommonUtils.getString(map.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
		map.put("pkOrg", pk_org);
		return pdPayMapper.queryPayList(map);
	}
	/**
	 * 保存付款信息
	 * @param param
	 * @param user
	 * @return
	 */
	public PdPayVo savePayInfo(String param,IUser user){
		PdPayVo vo = JsonUtil.readValue(param, PdPayVo.class);
		if(vo == null || vo.getDtlist()==null ||vo.getDtlist().size()<=0) throw new BusException("未获取到付款明细信息！");
		PdPay pay = new PdPay();
		ApplicationUtils.copyProperties(pay, vo);
		ApplicationUtils.setBeanComProperty(pay, true);
		DataBaseHelper.insertBean(pay);
		String pk_pay = pay.getPkPdpay();

		for(PdStDtVo dt : vo.getDtlist()){
			String sql = " update pd_st_detail set pk_pdpay = '"+pk_pay+"',flag_pay = '1',amount_pay = amount_cost "
					+",ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') "
					+ " where pk_pdstdt = '"+dt.getPkPdstdt()+"'";
			DataBaseHelper.update(sql, new Object[]{});
			//更新入库单状态
			String select_sql = "select count(1) as num from pd_st_detail dt"
					+ " where dt.pk_pdst = '"+dt.getPkPdst()+"' and dt.flag_pay = '0'";
			int num = DataBaseHelper.queryForScalar(select_sql, Integer.class, new Object[]{});
	        if(num==0){
	        	DataBaseHelper.update("update pd_st  set flag_pay=1  "
	        			+",ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') "
	        			+ "where pk_pdst=? and flag_pay='0'",new Object[]{dt.getPkPdst()});
	        } 
		}
	  vo.setPkPdpay(pk_pay);
      return vo;
	}
	/**
	 * 获取供应商信息
	 * @param param{pkSupplyer}
	 * @param user
	 * @return
	 */
	public BdSupplyer getSupplyerInfo(String param,IUser user){
		String pkSupplyer = JsonUtil.readValue(param, String.class);
		if(pkSupplyer == null||pkSupplyer.equals("")) throw new BusException("未获取到供应商主键");
		return DataBaseHelper.queryForBean("select * from bd_supplyer where pk_supplyer = ? ", BdSupplyer.class, new Object[]{pkSupplyer});
	}
	/**
	 * 查询付款信息
	 * @param param{pkPdPay}
	 * @param user
	 * @return
	 */
	public PdPayVo queryPayInfo(String param,IUser user){
		PdPayVo payvo  = JsonUtil.readValue(param, PdPayVo.class);
		if(payvo == null) throw new BusException("未获取到需要查看的付款信息！");
		if(!CommonUtils.isEmptyString(payvo.getPkPdpay())){
			payvo.setDtlist(pdPayMapper.queryStDtList(payvo.getPkPdpay()));
		}
		return payvo;
	}
}
