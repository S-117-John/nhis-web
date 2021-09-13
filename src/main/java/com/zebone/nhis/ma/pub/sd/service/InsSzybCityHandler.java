package com.zebone.nhis.ma.pub.sd.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.compay.ins.shenzhen.XtGzrz;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.compay.pub.syx.vo.CostForecastVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.dao.jdbc.LoadDataSource;
import com.zebone.platform.modules.dao.jdbc.entity.DataBaseEntity;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.FileUtils;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 深圳市医保业务处理服务(无事物)
 * @author huanghaisheng
 *
 */
@Service
public class InsSzybCityHandler {
	@Autowired
	private InsSzybCityService insSzybCityService;

	/**
	 * author:huanghaisheng
	 * 业务处理方法转换器
	 * @param methodName
	 * @param args
	 */
    public Object invokeMethod(String methodName,Object...args){
    	Object result = null;
    	switch(methodName){
    	case "loginUserQry":
    		insSzybCityService.loginUserQry(args);
    		break;
    	case "qryCostForecast":
			this.qryCostForecast(args);
			break;
    	}
    	return result;
    }
    
    /**
	 * 医保预测
	 * @param args
	 */
  	private void qryCostForecast(Object[] args) {
  		if (args != null) {
  			String param =(String)args[0];
  			IUser user =(IUser)args[1];
  			Map<String,Object> rtnM =(Map<String,Object>)args[2];
  			Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
  			String pkPv = (String) paramMap.get("pkPv");
  			
  			//未结费用
			Map<String, Object> amtNoSettle= DataBaseHelper.queryForMap("select sum(amount) amt from bl_ip_dt where flag_settle='0' and del_flag='0' and pk_pv=?",pkPv);
			if (amtNoSettle != null) {
				rtnM.put("AmtNosettle", amtNoSettle.get("amt")!=null ?amtNoSettle.get("amt").toString():"0");
			}
			//医保预测费用
			Map<String, Object> amtHp= DataBaseHelper.queryForMap("select sum(amount_insu+amount_pidiv) from bl_settle_budget  where del_flag='0' and pk_pv=?",pkPv);
			if (amtHp != null) {
				rtnM.put("AmtHp", amtHp.get("amt")!=null ?amtHp.get("amt").toString():"0");
			}
			//预交金
			Map<String, Object> amtPrepmap = DataBaseHelper.queryForMap("select sum(amount) amt from bl_deposit where eu_dptype='9' and flag_settle='0' and pk_pv=?",pkPv);
			if (amtPrepmap != null) {
				rtnM.put("AmtPrep", amtPrepmap.get("amt")!=null ?amtPrepmap.get("amt").toString():"0");
			}
			
			//绿色担保金
			Map<String, Object> amtCredit = DataBaseHelper.queryForMap("select sum(amt_credit) amt from PV_IP_ACC where  flag_canc='0' and del_flag='0' and pk_pv=?",pkPv);
			if (amtCredit != null) {		
				rtnM.put("amtIpAcc", amtCredit.get("amt")!=null ?amtCredit.get("amt").toString():"0"); 
			}
			
			
			//可用余额=预交金+医保预测+绿色担保金-未结费用
			Double dAmtPrep = CommonUtils.getDouble(rtnM.get("AmtPrep"));
			Double dAmtHp =CommonUtils.getDouble(rtnM.get("AmtHp"));
			Double dAmtCredit =CommonUtils.getDouble(rtnM.get("amtIpAcc")); 
			Double dAmtNosettle =CommonUtils.getDouble(rtnM.get("AmtNosettle")); 	
			Double dAmtAcc1 =   MathUtils.sub(MathUtils.add(MathUtils.add(dAmtPrep, dAmtHp),dAmtCredit),dAmtNosettle); 
			Double dAmtAcc =   MathUtils.sub(MathUtils.add(dAmtPrep, dAmtHp),dAmtNosettle); 
			rtnM.put("AmtAcc", dAmtAcc.toString());//可用余额--界面上可用余额不变

			//是否欠费
			String IsArrearage=ApplicationUtils.getSysparam("BL0004", false);
			if("1".equals(IsArrearage)){
				rtnM.put("IsArrearage", dAmtAcc1<0);
			}else{
				rtnM.put("IsArrearage", false);
			}
  		}
  	}
    
    /**
	 * 保存院方日志 015001011087
	 * 此方法是提供给直接通过交易码使用(因为切换了数据源,无事务)
	 */
	public void saveSbLog(String param,IUser user) throws Exception{
		List<String> listName=getDataSoureList();
		try {
			if (listName.contains("sughChisSb")){
				//切换数据源
				DataSourceRoute.putAppId("sughChisSb");
				// 数据插入
				List<XtGzrz> logList = JsonUtil.readValue(param,new TypeReference<List<XtGzrz>>() {});
				insSzybCityService.saveSbLog(logList);
				DataSourceRoute.putAppId("default");
			}else {
				//throw new BusException("未找到10.0.3.81的数据源配置");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusException("写入院方日志表xt_gzrz失败"+e.getMessage());
		} finally {
			DataSourceRoute.putAppId("default");
		}
	}
	
	
	private List<String>  getDataSoureList(){
		List<String> listName = new ArrayList();
		try {
			String path = LoadDataSource.class.getClassLoader().getResource("config/datasource.xml").getFile().toString();
			File file = new File(path.replace("%20", " "));
			Document doc = FileUtils.getDocument(file);
			Element rootElement = doc.getRootElement();

			if(rootElement != null){
				List<Element> list = rootElement.elements("datasource");

				for(int i=0;i<list.size();i++){
					DataBaseEntity dataBaseEntity = new DataBaseEntity();
					Element datasources = list.get(i);
					if(datasources != null){
						String dsName = datasources.attributeValue("name");
						listName.add(dsName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listName;
	}
}
