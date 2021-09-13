package com.zebone.nhis.pro.pskq.health.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pro.pskq.health.dao.PskqHealthMapper;
import com.zebone.nhis.pro.pskq.health.vo.PskqHealthCancelResDataVo;
import com.zebone.nhis.pro.pskq.health.vo.PskqHealthCancelResultVo;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class PskqHealthService {
	
	@Resource
    private PskqHealthMapper pskqHealthMapper;
	/**
	 * 通过健康码获取的患者信息进行建档登记
	 * @param param
	 * @param user
	 * @return
	 */
	public String healthPiMasterReg(String param, IUser user) {		
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String hicNo = CommonUtils.getString(map.get("hicNo"));
		if(CommonUtils.isEmptyString(hicNo)){
			throw new BusException("获取二维码为空，请重新扫码！");
		}
		//调用EHC05服务验证健康码，并通过健康码获取到患者信息
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("hicNo",hicNo);
		Map<String, Object> retPi = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHC05", new Object[]{paramMap,user});
		PiMaster requPi = (PiMaster) retPi.get("piMaster");
		User u = (User)user;
		// 校验是否已经注册，如果已注册绑定健康码
		PiMaster temp_pi = DataBaseHelper.queryForBean("select code_pi,name_pi,pk_pi from pi_master where id_no = ? and dt_idtype = ?",PiMaster.class, new Object[]{requPi.getIdNo(),requPi.getDtIdtype()} );
		if (temp_pi != null) {
			temp_pi.setMobile(requPi.getMobile());// 手机号
			temp_pi.setAddrCurDt(requPi.getAddrCurDt());// 地址
			temp_pi.setNamePi(requPi.getNamePi() );// 患者名称
			temp_pi.setTelNo(requPi.getTelNo());//电话号码
			temp_pi.setUnitWork(requPi.getUnitWork());//工作地址
			temp_pi.setHicNo(requPi.getHicNo());//健康卡id
			temp_pi.setModifier(u.getPkEmp());// 修改人
			temp_pi.setPkOrg(u.getPkOrg());
			temp_pi.setCreator(u.getPkEmp());
			temp_pi.setCreateTime(new Date());
			temp_pi.setDelFlag("0");
			temp_pi.setTs(new Date());
			temp_pi.setModifier(u.getPkEmp());
			DataBaseHelper.updateBeanByPk(temp_pi, false);
			return temp_pi.getHicNo();
		}
		requPi.setCodePi(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
		requPi.setCodeOp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZBL));
		requPi.setCodeIp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL));
		requPi.setCodeEr(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_JZ));//急诊号
		
		PiCate piCate = DataBaseHelper.queryForBean("select * from PI_CATE where flag_def = '1' and del_flag = '0'", PiCate.class);
		requPi.setPkPicate(piCate.getPkPicate());// 患者分类
		
		requPi.setPkOrg(u.getPkOrg());
		requPi.setCreator(u.getPkEmp());
		requPi.setCreateTime(new Date());
		requPi.setDelFlag("0");
		requPi.setTs(new Date());
		DataBaseHelper.insertBean(requPi);

		//查询自费医保计划主键
		Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT * FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
		//保存患者医保计划 ---默认自费
		PiInsurance insu = new PiInsurance();
		insu.setPkHp(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp"));
		insu.setPkPi(requPi.getPkPi());
		insu.setSortNo(Long.valueOf("1"));

		insu.setDateBegin(new Date());//生效日期
		insu.setDateEnd(DateUtils.getTimeForOneYear(10));//失效日期

		insu.setFlagDef("1");//设置默认
		insu.setDelFlag("0");
		insu.setCreator(u.getPkEmp());//创建人
		insu.setCreateTime(new Date());
		insu.setTs(new Date());
		DataBaseHelper.insertBean(insu);

		//新增时，插入一条PiAcc记录
		PiAcc acc = new PiAcc();
		acc.setPkPi(requPi.getPkPi());
		acc.setCodeAcc(requPi.getCodeIp());
		acc.setAmtAcc(BigDecimal.ZERO);
		acc.setCreditAcc(BigDecimal.ZERO);
		acc.setEuStatus(PiConstant.ACC_EU_STATUS_1);
		DataBaseHelper.insertBean(acc);
		return requPi.getHicNo();
	}
	
	/**
	 * 电子健康网住院业务退款发起第三方退费通知
	 * @param param
	 * @param user
	 * @return
	 */
	public PskqHealthCancelResultVo healthPaymentIpRefund(String param, IUser user) {		
		Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
    	PskqHealthCancelResultVo ResultVo = new PskqHealthCancelResultVo();
		String pkDepo=paramMap.get("pkDepo").toString();
		String type =paramMap.get("type").toString();
		Map<String,Object> ehealthMap = new HashMap<>(16);
    	ehealthMap.put("pkDepo", pkDepo);
    	ehealthMap.put("type", type);
    	Map<String, Object> extPayMap = pskqHealthMapper.queryDepositDataByPkDepo(pkDepo);
		if(extPayMap == null){
			throw new RuntimeException("未查询到第三方关联的订单记录！");
		}
		extPayMap.put("type", type);
    	//调用PM020服务，发起退款通知
		Map<String, Object> retPi = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodePM020", new Object[]{extPayMap,user});	
    	PskqHealthCancelResDataVo  resDataVo = new PskqHealthCancelResDataVo();
		ResultVo.setStatuscode("1");
		ResultVo.setData(resDataVo);
		return ResultVo;
	}
	
	
	/**
	 * 电子健康网门诊业务退款发起第三方退费通知
	 * @param param
	 * @param user
	 * @return
	 */
	public PskqHealthCancelResultVo healthPaymentOpRefund(String param, IUser user) {		
		Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
    	PskqHealthCancelResultVo ResultVo = new PskqHealthCancelResultVo();
		String pkSettle=paramMap.get("pkSettle").toString();
		String type =paramMap.get("type").toString();
		Map<String,Object> ehealthMap = new HashMap<>(16);
    	ehealthMap.put("pkSettle", pkSettle);
    	ehealthMap.put("type", type);
    	Map<String, Object> extPayMap = pskqHealthMapper.querySettleDataByPkSettle(pkSettle);
		if(extPayMap == null){
			throw new RuntimeException("未查询到第三方关联的订单记录！");
		}
		extPayMap.put("type", type);
    	//调用PM020服务，发起退款通知
		Map<String, Object> retPi = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodePM020", new Object[]{extPayMap,user});	
    	PskqHealthCancelResDataVo  resDataVo = new PskqHealthCancelResDataVo();
		ResultVo.setStatuscode("1");
		ResultVo.setData(resDataVo);
		return ResultVo;
	}
	
	/**
	 * 022005001004
	 * @param param 
	 * @param user
	 * @return
	 */
	public Integer checkCancelAllSettleExt(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null){
			throw new BusException("未传入有效参数信息！");
		}
		//住院预交金传pkDepo
		String pkSettle = CommonUtils.getPropValueStr(paramMap, "pkSettle");
		String sql="select count(1) from bl_ext_pay where sysname in ('健康网') and pk_settle=?  ";
		Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkSettle});
		return count;
	}
	
	/**
	 * 022005001005
	 * @param param 
	 * @param user
	 * @return
	 */
	public Integer checkCancelAllDepoExt(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null){
			throw new BusException("未传入有效参数信息！");
		}
		//住院预交金传pkDepo
		String pkDepo = CommonUtils.getPropValueStr(paramMap, "pkDepo");
		String sql="select count(1) from bl_ext_pay where sysname in ('健康网') and  pk_depo = ? ";
		Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkDepo});
		return count;
	}
	
	/**
	 * 022005001007
	 * @param param 
	 * @param user
	 * @return
	 */
	public String getThirdOrderSource(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null){
			throw new BusException("未传入有效参数信息！");
		}
		String pkSettle = CommonUtils.getPropValueStr(paramMap, "pkSettle");
		String sql="select sysname from bl_ext_pay where pk_settle = ? ";
		List<Map<String,Object>> sysnameList=DataBaseHelper.queryForList(sql, new Object[]{pkSettle});
		return (sysnameList != null && sysnameList.size() > 0) ? CommonUtils.getPropValueStr(sysnameList.get(0), "sysname") : "" ;
	}
	
}
