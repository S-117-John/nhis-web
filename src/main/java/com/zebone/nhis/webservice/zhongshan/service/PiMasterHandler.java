package com.zebone.nhis.webservice.zhongshan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.adt.service.ZsbaAdtHandler;
import com.zebone.nhis.pro.zsba.adt.service.ZsbaAdtService;
import com.zebone.nhis.pro.zsba.adt.vo.PiMasterBa;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.zhongshan.vo.PiMasterChkParam;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class PiMasterHandler {

	@Resource
	private ZsbaAdtHandler zsbaAdtHandler;
	
	@Resource
	private ZsbaAdtService zsbaAdtService;
	
	/**
	 * 1、人工办卡（LIS01）
	 *  调用方：体检系统(在体检系统 导入集体体检患者时，调用该服务，告知获取并同步患者主索引信息)
	 * @param func_id 		功能编号  	非空 【PiMaster01】
	 * @param ope_time	 	操作时间 	非空	【yyyy-MM-dd HH:mm:ss】
	 * @param ope_code 		操作人工号 	非空
	 * @param namePi 		患者姓名 	非空
	 * @param dtSex	 	患者性别 	非空【1-男，2-女，9-未知】
	 * @param dtIdtype 	证件类型 	非空【00-身份证，】
	 * @param idNo		 	证件号码 	非空
	 * @param birthDate	出生日期 	非空 【yyyy-MM-dd】
	 * @param telNo 		联系电话
	 * @param address 		现居住地址
	 * @param unitWork 		公司
	 * {"func_id":"PiMaster01","ope_time":"2021-06-22 15:50","ope_code":"03617","namePi":"namePi1","dtSex":"1","dtIdtype":"00","idNo":"1254845224521451245","birthDate":"2021-01-05","telNo":"12578458956","address":"居住地址","company":"公司地址"}
	 * @return
	 */
	public String qryOrSavePiInfo(String input_info) {
		PiMasterChkParam chkParam = JsonUtil.readValue(input_info,PiMasterChkParam.class);

		DataSourceRoute.putAppId("default");
		// 1、校验传参
		if (!"PiMaster01".equals(chkParam.getFunc_id()))
			throw new BusException( "功能编号不为PiMaster01!");
		String msg = chkParam.toChkString();
		if (msg != null)
			throw new BusException( msg);
		
		//1.2 校验操作人是否存在
 		BdOuEmployee empOper = DataBaseHelper.queryForBean("select * from bd_ou_employee where del_flag = '0' "
				+ " and code_emp = ? ", BdOuEmployee.class, chkParam.getOpe_code());
		if(empOper == null)
			throw new BusException( "操作人：不存在编码为【"+ chkParam.getOpe_code() +"】 的人员！"); 
		User user = new User();
		user.setPkOrg(empOper.getPkOrg());
		user.setNameEmp(empOper.getNameEmp());
		user.setPkEmp(empOper.getPkEmp());
		user.setCodeEmp(empOper.getCodeEmp());
		UserContext.setUser(user);
		
		// 2、校验【新系统】是否存在该患者
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("idNo", chkParam.getIdNo());
		param.put("dtSex", chkParam.getDtSex());
		param.put("namePi", chkParam.getNamePi());
		param.put("birthDate",chkParam.getBirthDateTxt());
		List<Map<String,Object>> listNhisPi = zsbaAdtService.getPiListFromNhis(param);
		String sex = "";
		if(null != listNhisPi && listNhisPi.size() > 0){
			for (Map<String, Object> map : listNhisPi) {
				// 1-男【02】，2-女【03】, 9-未知【】转换性别
				sex = CommonUtils.getString(map.get("dtSex"));
				sex = "02".equals(sex) ? "1" : ("03".equals(sex) ? "2":"9");
				map.put("dtSex", sex);
			}
			String mes = JSONUtils.toJSONString(listNhisPi);
			return new RespJson("0|成功|" +  mes, true).toString();
		}
		
//		//4、校验【旧系统】是否存在患者
//		DataSourceRoute.putAppId("HIS_bayy");
//		listNhisPi = zsbaAdtService.getPiListFromHis(param);
//		PiMasterBa savePi = null;
//		if(null != listNhisPi && listNhisPi.size() > 0){
//			try {
//				savePi = ApplicationUtils.mapToBean(listNhisPi.get(0), PiMasterBa.class) ;
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new BusException( "从旧系统获取患者信息失败！"); 
//			}
//		}
//		else{
//			savePi = new PiMasterBa();
//			ApplicationUtils.copyProperties(savePi, chkParam);
//			savePi.setCreateTime(new Date());
//			savePi.setMobile(savePi.getTelNo());
//			savePi.setAddrCurDt(chkParam.getAddress());
//		}
//		DataSourceRoute.putAppId("default");

		//5、保存患者基本信息
		PiMasterBa savePi = new PiMasterBa();
		ApplicationUtils.copyProperties(savePi, chkParam);
		savePi.setCreateTime(new Date());
		savePi.setMobile(savePi.getTelNo());	
		savePi.setAddrCurDt(chkParam.getAddress());
		savePi.setCreator("体检接口导入");
		savePi.setModifier(empOper.getPkEmp());//创建人名字写入修改人
		savePi.setTs(new Date());
		PiMaster pi = new PiMaster();
		ApplicationUtils.copyProperties(pi, chkParam);
		Map<String,Object> pa = (Map<String, Object>) ApplicationUtils.beanToMap(chkParam);
		listNhisPi = zsbaAdtHandler.savePiinfoDetail(pa,savePi,pi,false);
		if(null != listNhisPi && listNhisPi.size() > 0){
			for (Map<String, Object> map : listNhisPi) {
				// 1-男【02】，2-女【03】, 9-未知【】转换性别
				sex = CommonUtils.getString(map.get("dtSex"));
				sex = "02".equals(sex) ? "1" : ("03".equals(sex) ? "2":"9");
				map.put("dtSex", sex);
			}
			String mes = JSONUtils.toJSONString(listNhisPi);
			return new RespJson("0|成功|" +  mes, true).toString();
		}
		
		return new RespJson("0|成功", true).toString();
	}

	/**
	 * 返回之前，调整患者性别
	 * @param map
	 * @return
	 */
	public Map<String, Object> transRtnPiInfo(Map<String, Object> map){
		String sex = CommonUtils.getString(map.get("dtSex"));
		// 1-男【02】，2-女【03】, 9-未知【】转换性别
		if("02".endsWith(sex))
			sex = "1";
		else if("03".endsWith(sex))
			sex = "2";
		else
			sex = "9";
		map.remove("dtSex");
		map.put("dtSex", sex);
		return map;
	}
	
}
