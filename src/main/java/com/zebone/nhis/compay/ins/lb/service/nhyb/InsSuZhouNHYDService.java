package com.zebone.nhis.compay.ins.lb.service.nhyb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsFyfjmx;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhydPi;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhydReginfo;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhydSettle;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhydSettleFee;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhydSettleGrade;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.compay.ins.lb.dao.nhyb.InsSuZhouNHYDMapper;
import com.zebone.nhis.pv.pub.vo.PvOpParam;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsSuZhouNHYDService {

	@Autowired
	private InsSuZhouNHYDMapper insSuZhouYDMapper;
	
	/**
	 * 交易号：015001007001
	 * 新农合异地医保患者信息保存(废弃)
	 * @param param
	 * @param user
	 */
	public PiMaster savePatientInfo(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String ybInfo=null;
		if(JsonUtil.getJsonNode(param, "yBPatientInfo") != null ){
			ybInfo = JsonUtil.getJsonNode(param, "yBPatientInfo").toString();
		}
		String idNo = CommonUtils.getString(paramMap.get("idNo"));//身份证号码
		String insurNo = CommonUtils.getString(paramMap.get("insurNo"));//医保卡号
		String pkHp = CommonUtils.getString(paramMap.get("pkHp"));//医保计划
		String namePi = CommonUtils.getString(paramMap.get("namePi"));//患者姓名
		Date birthDate = DateUtils.strToDate(paramMap.get("birthDate").toString());
		String mobile = CommonUtils.getString(paramMap.get("mobile"));//手机号码
		String dtSex = CommonUtils.getString(paramMap.get("dtSex"));//患者性别
		String codeIp = CommonUtils.getString(paramMap.get("codeIp"));//住院号
		String insCode = null;
		if(paramMap.get("insCode") != null){
			insCode = CommonUtils.getString(paramMap.get("insCode"));//第三方医保标识号
		}

		if(insurNo == null || StringUtils.isEmpty(insurNo)){
			throw new BusException("医保卡号为空");
		}
		Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select pm.*,bh.dt_exthp from bd_hp bh left join  pi_insurance pi on bh.pk_hp = pi.pk_hp "
				+ "left join pi_master pm on pi.pk_pi=pm.pk_pi where pm.insur_no=? and pm.del_flag='0'" , new Object[]{insurNo});
		//为空-新增保存
		if(queryForMap == null){
			PiMaster master = new PiMaster();
			//患者是第一次来医院    
			String pkPicate = DataBaseHelper.queryForScalar("select pk_picate from pi_cate where name ='普通'",String.class,new Object[]{});
			master.setPkPicate(pkPicate);//默认是普通患者分类
			String codePi = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ);
//			String codePi = "1";
			master.setCodePi(codePi);//患者编码
			master.setIdNo(idNo);//身份证号
			master.setDtIdtype("01");//证件类型 --01
			master.setInsurNo(insurNo);//医保卡号
			master.setNamePi(namePi);//姓名
			master.setDtSex(dtSex);//患者性别
			master.setBirthDate(birthDate);//出生日期
			master.setMobile(mobile);//手机号码
			master.setCodeIp(codeIp);
			ApplicationUtils.setDefaultValue(master, true);
			DataBaseHelper.insertBean(master);
			//设置医保计划
			PiInsurance insurance = new PiInsurance();
			insurance.setPkPi(master.getPkPi());
			insurance.setPkHp(pkHp);
			insurance.setDelFlag("0");
			insurance.setCreateTime(new Date());
			DataBaseHelper.insertBean(insurance);
			//picard 保存卡信息
			// 身份证
			PiCard idCard = new PiCard();
			idCard.setPkPi(master.getPkPi());
			idCard.setDtCardtype("04");//身份证
			idCard.setCardNo(idNo);
			idCard.setDelFlag("0");
			idCard.setEuStatus("0");
			idCard.setFlagActive("1");
			ApplicationUtils.setDefaultValue(idCard, true);
			DataBaseHelper.insertBean(idCard);
			// 医保卡
			PiCard insureCard = new PiCard();
			insureCard.setPkPi(master.getPkPi());
			insureCard.setDtCardtype("02");//医保卡
			insureCard.setCardNo(insurNo);
			insureCard.setDelFlag("0");
			insureCard.setEuStatus("0");
			insureCard.setFlagActive("1");
			ApplicationUtils.setDefaultValue(insureCard, true);
			DataBaseHelper.insertBean(insureCard);
			String pkPi = DataBaseHelper.queryForScalar("select pk_pi from pi_master pm where pm.insur_no=? and pm.del_flag='0'" ,String.class, new Object[]{insurNo});
			if(insCode != null && insCode.equals("06")){
				InsSuzhounhydPi insSuzhounhydPi = JsonUtil.readValue(ybInfo, InsSuzhounhydPi.class);
				insSuzhounhydPi.setPkPi(pkPi);
				DataBaseHelper.insertBean(insSuzhounhydPi);
			}
		}else{//更新保存
			//INSUR_NO 医保卡号是空的
			if(queryForMap.get("insurNo").toString() == null || queryForMap.get("insurNo").toString().equals("")){
				// 把当前传递的医保卡号设为医保卡号
				String insurSql = "update pi_master set INSUR_NO = ? where del_flag = '0' and pk_pi = ?";
				DataBaseHelper.update(insurSql, new Object[]{insurNo, queryForMap.get("pkPi").toString()});
				// 医保卡
				PiCard insureCard = new PiCard();
				insureCard.setPkPi(queryForMap.get("pkPi").toString());
				insureCard.setDtCardtype("02");//医保卡
				insureCard.setCardNo(insurNo);
				insureCard.setDelFlag("0");
				insureCard.setEuStatus("0");
				insureCard.setFlagActive("1");
				ApplicationUtils.setDefaultValue(insureCard, true);
				DataBaseHelper.insertBean(insureCard);
			}
			//先把之前存在的医保计划设成非默认的
			DataBaseHelper.update("update pi_insurance set flag_def='0' where pk_pi=? and del_flag='0'",
					new Object[] {queryForMap.get("pkPi").toString()});
			
			Map<String, Object> map = new HashMap<>();
			map.put("pkHp", pkHp);
			map.put("pkPi", queryForMap.get("pkPi").toString());
			//设置医保计划
			String insuranceSql = "update pi_insurance set PK_HP=:pkHp, FLAG_DEF='1' "
					+ " where del_flag='0' and pk_pi=:pkPi ";
			DataBaseHelper.update(insuranceSql, map);
			
			//灵璧农合异地医保
			//如果pi_master表关联的主医保中的第三方医保字段 ，如果是农保异地06，就保存 INS_SUZHOUNHYD_PI 患者的医保信息。
			if(queryForMap.get("dtExthp") != null){
				String pkPi = DataBaseHelper.queryForScalar("select pk_pi from pi_master pm where pm.insur_no=? and pm.del_flag='0'" ,String.class, new Object[]{insurNo});
				if(insCode != null && insCode.equals("06") && queryForMap.get("dtExthp").equals(insCode)){
					InsSuzhounhydPi insSuzhounhydPi = JsonUtil.readValue(ybInfo, InsSuzhounhydPi.class);
					insSuzhounhydPi.setPkPi(pkPi);
					DataBaseHelper.updateBeanByPk(insSuzhounhydPi,false);
				}
			}
		}
		
		PiMaster piMaster = DataBaseHelper.queryForBean(
				"select * from pi_master where del_flag ='0' and insur_no=?",PiMaster.class, insurNo);
		
		
		return piMaster;
	}
	
    /**
     * 交易号：015001007002
     * 查询患者信息
     * @param param
     * @param user
     * @return
     */
	@SuppressWarnings("unused")
	public InsSuzhounhydPi queryPatiInfo(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//身份证cardNo
		String cardNo = paramMap.get("cardno").toString();
		//centerNo
		String centerNo = paramMap.get("centerno").toString();
		//year
		String year = paramMap.get("year").toString();
 
		InsSuzhounhydPi insSuzhounhydPi = new InsSuzhounhydPi();
		if (paramMap != null ) {
			insSuzhounhydPi = insSuZhouYDMapper.queryPatiInfo(paramMap);
		}
		
		return insSuzhounhydPi;
	}
	
	/**
	 * 交易号：015001007003
	 * 保存医保登记信息
	 * @param param
	 * @param user
	 * @return
	 */
	public String saveYbRegitInfo(String param, IUser user){
		InsSuzhounhydReginfo insParam = JsonUtil.readValue(param, InsSuzhounhydReginfo.class);
		String inpatientsn = insParam.getInpatientsn();
		
		InsSuzhounhydReginfo bean = DataBaseHelper.queryForBean("select * from INS_SUZHOUNHYD_REGINFO where inpatientsn=? and del_flag='0'", InsSuzhounhydReginfo.class, new Object[]{inpatientsn});
		if(bean==null){
			//保存登记信息
			DataBaseHelper.insertBean(insParam);
		}else{
			//更新登记信息
			if (!StringUtils.isBlank(bean.getId())) {
				DataBaseHelper.updateBeanByPk(bean, false);
			}
		}
		bean = DataBaseHelper.queryForBean("select * from INS_SUZHOUNHYD_REGINFO where inpatientsn=? and del_flag='0'", InsSuzhounhydReginfo.class, new Object[]{inpatientsn});
		return bean.getId();
	}
	
	/**
	 * 交易号：015001007004
	 * 医保登记删除
	 * @param param
	 * @param user
	 * @return
	 */
	public void deleteYbRegit(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap!=null){
			//根据PK_PV删除医保登记信息
			String updateSql = "update INS_SUZHOUNHYD_REGINFO set del_flag = '1' where pk_pv = ?";
			DataBaseHelper.update(updateSql, new Object[]{paramMap.get("pkPv").toString()});
		}
	}
	
	/**
	 * 交易号：015001007005
	 * 查询医保登记信息
	 * @param param
	 * @param user
	 * @return
	 */
	public InsSuzhounhydReginfo queryYbRegitInfo(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		InsSuzhounhydReginfo regitInfo = new InsSuzhounhydReginfo();
		if (paramMap != null ) {
			 regitInfo = insSuZhouYDMapper.queryYbRegitInfo(paramMap);
		}
		return regitInfo;
	}
	
	/**
	 * 交易号：015001007006
	 * 批量保存分解明细接口
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveINS_FYFJMX(String param, IUser user){
		//接收：INS_FYFJMX集合
		List<InsFyfjmx> paramList = JsonUtil.readValue(param, new TypeReference<List<InsFyfjmx>>() {});
		if(paramList != null && paramList.size() > 0){
			for (InsFyfjmx insFyfjmx : paramList) {
				if(insFyfjmx.getPkPv() == null || insFyfjmx.getPkPv().equals("")){
					throw new BusException("患者就诊主键为空!");
				}
				if(insFyfjmx.getPkSettle() == null || insFyfjmx.getPkSettle().equals("")){
					throw new BusException("患者结算主键为空!");
				}
				DataBaseHelper.update("update bl_ip_dt set flag_insu='1' where pk_pv=? and pk_settle=? and del_flag='0'", 
						new Object[]{insFyfjmx.getPkPv(),insFyfjmx.getPkSettle()});
			}
		}
		else{
			throw new BusException("数据为空!");
		}
	}
	
	/**
	 * 交易号：015001007007
	 * 撤销医保上传
	 * @param param
	 * @param user
	 * @return
	 */
	public void cancleYbUpload(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap != null){
			String pkPv = paramMap.get("pkPv").toString();
			String pkSettle = paramMap.get("pkSettle").toString();
			if(pkPv == null || pkPv.equals("")){
				throw new BusException("患者就诊主键为空!");
			}
			if(pkSettle == null || pkSettle.equals("")){
				throw new BusException("患者结算主键为空!");
			}
			//逻辑删除 INS_FYFJMX 信息
			DataBaseHelper.update("update INS_FYFJMX set del_flag='1' where pk_pv=? and pk_settle=? ", new Object[]{pkPv,pkSettle});
			//更新BL_IP_DT 的 上传标记为0
			DataBaseHelper.update("update BL_IP_DT set flag_insu='0' where pk_pv=? and pk_settle=? ", new Object[]{pkPv,pkSettle});
		}
	}
	
	/**
	 * 交易号：015001007008
	 * 医保结算保存
	 * @param param
	 * @param user
	 * @return
	 */
	public String saveYbSettle(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//医保结算对象
		InsSuzhounhydSettle insSuzhouNhYdSettle = JsonUtil.readValue(param, InsSuzhounhydSettle.class);

		//医保分类汇总（暂时没传）
		//List<InsSuzhounhydSettleFee> szNhYdSettleFeeList = (List<InsSuzhounhydSettleFee>) paramMap.get("szNhYdSettleFeeList");
		//医保分段信息（暂时没传）
		//List<InsSuzhounhydSettleGrade> szNhYdSettleGradeList = (List<InsSuzhounhydSettleGrade>) paramMap.get("szNhYdSettleGradeList");

		if(insSuzhouNhYdSettle != null){
			DataBaseHelper.insertBean(insSuzhouNhYdSettle);
		}
		//保存INS_SUZHOUNHYD_SETTLE_FEE，INS_SUZHOUNHYD_SETTLE 的主键主键也要写进去
		/*if(szNhYdSettleFeeList != null && szNhYdSettleFeeList.size() > 0){
			for (InsSuzhounhydSettleFee settleFee : szNhYdSettleFeeList) {
				settleFee.setPkInsSettle(queryBean.getId());
				DataBaseHelper.insertBean(szNhYdSettleFeeList);
			}
		}*/
		//保存INS_SUZHOUNHYD_SETTLE_GRADE，INS_SUZHOUNHYD_SETTLE 的主键主键也要写进去
		/*if(szNhYdSettleFeeList != null && szNhYdSettleFeeList.size() > 0){
			for (InsSuzhounhydSettleGrade settleGrade : szNhYdSettleGradeList) {
				settleGrade.setPkInsSettle(queryBean.getId());
				DataBaseHelper.insertBean(szNhYdSettleFeeList);
			}
		}*/
		return insSuzhouNhYdSettle.getId();
	}
	
	/**
	 * 交易号：015001007009
	 * 医保结算撤销
	 * @param param
	 * @param user
	 * @return
	 */
	public void cancleYbSettle(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkSettle = paramMap.get("pkSettle").toString();
		if(pkSettle != null){
			//逻辑删除INS_SUZHOUNHYD_SETTLE
			DataBaseHelper.update("update INS_SUZHOUNHYD_SETTLE set del_flag='1' where  pk_settle=? ", new Object[]{pkSettle});
		}
	}
	
	/**
	 * 交易号：015001007010
	 * 根据农合异地登记主键更改pkPv 建立与pv_encounter的关系
	 * @param param
	 * @param user
	 */
	public void updateNhYdRePkPv(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.size()==0)return ;
		String sql="update INS_SUZHOUNHYD_REGINFO set pk_pv=? where del_flag='0' and id=?";
		DataBaseHelper.update(sql,new Object[]{paramMap.get("pkPv"),paramMap.get("id")});
	}
	
	/**
	 * 交易号：015001007011
	 * 根据农合异地结算主键更新pk_settle,建立与bl_settle的关系
	 * @param param
	 * @param user
	 */
	public void updateNhYdJsPkSettle(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return ;
		String sql="update INS_SUZHOUNHYD_SETTLE set pk_settle=? where del_flag='0' and id=? ";
		DataBaseHelper.update(sql, new Object[]{paramMap.get("pkSettle"),paramMap.get("id")});
	}
}
