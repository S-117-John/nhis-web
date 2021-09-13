package com.zebone.nhis.pro.zsba.compay.pub.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pi.pub.dao.PiPubMapper;
import com.zebone.nhis.pi.pub.vo.CommonParam;
import com.zebone.nhis.pi.pub.vo.PibaseVo;
import com.zebone.nhis.pi.pub.vo.PvDiagVo;
import com.zebone.nhis.pro.zsba.compay.up.vo.AmountBean;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;



@Service
public class PiInfoService {

	@Resource
	private PiPubMapper piPubMapper;
	
	
	
	/**
	 * 
	 * 精确查询住院患者就诊信息(包含预交金以及费用总额)<br>
	 * 022003002012
	 * <pre>
	 * 1.从患者基本表pi_master中查询基本信息;
	 * 2.从就诊记录表pv_encounter查询就诊信息,查询就诊状态为0、1、3的记录;
	 * 3.从过敏史表pi_allergic查询过敏信息，多笔过敏信息之间以逗号分隔;
	 * 4.从临床综合诊断表pv_diag中获取诊断名称，多个诊断之间使用逗号分隔;
	 * 5.从医保计划表pi_insurance查询医保名称;
	 * 6.从收费结算-交款记录表bl_deposit中统计eu_dptype=9的交款金额作为预交金金额;
	 * 7.从收费结算-住院收费明细包bl_ip_dt表中统计金额字段做为费用总额;
	 * 8.pi_allergic、pv_diag、bl_deposit、bl_ip_dt表使用pk_pv字段与pv_encounter表关联;
	 * 9.参数就诊状态为数组，为空时查询就诊状态为1的记录;
	 * 10.查询字段包括：患者编码、就诊卡号、证件号码、医保卡号、就诊号码、当前床位、就诊主键
	 * </pre>
	 * 
	 * @param param
	 * @return user
	 * @throws
	 *
	 */
	public PibaseVo getPibaseAndAmountVo(String param, IUser user) {
		CommonParam cparam = JsonUtil.readValue(param, CommonParam.class);
		PibaseVo vo = getPibaseAndAmountVo(cparam);
		return vo;
	}

	public PibaseVo getPibaseAndAmountVo(CommonParam cparam) {
		String fieldName = cparam.getFieldName();
		String fieldValue = cparam.getFieldValue();

		PibaseVo vo = new PibaseVo();
		PiMaster master = new PiMaster();
		PvEncounter encounter = new PvEncounter();
		// 参数在不同的表中，分开查表
		boolean flag = false; // 默认false 查患者基本表 pi_master
		if ("code_pi".equals(fieldName)) { // 患者编码
			master.setCodePi(fieldValue);
		} else if ("code_ip".equals(fieldName)) { // 住院号
			master.setCodeIp(fieldValue);
			encounter.setEuPvtype("3");//如果按照住院号查询，默认查询住院患者，否则会查出门诊就诊信息
		} else if ("id_no".equals(fieldName)) {// 证件号码
			master.setIdNo(fieldValue);
		} else if ("insur_no".equals(fieldName)) {// 医保卡号
			master.setInsurNo(fieldValue);
		} else if ("code_pv".equals(fieldName)) { // 就诊号码
			encounter.setCodePv(fieldValue);
			flag = true;
		} else if ("bed_no".equals(fieldName)) { // 当前床位
			//新增查询条件按，查询本科室下的患者
			encounter.setPkDeptNs(UserContext.getUser().getPkDept());
			encounter.setBedNo(fieldValue);
			flag = true;
		} else if ("pk_pv".equals(fieldName)) { // 就诊主键
			encounter.setPkPv(fieldValue);
			flag = true;
		} else if ("card_no".equals(fieldName)) { // 就诊卡号
			PiCard card = piPubMapper.getPiCardByCardNo(fieldValue);
			if (card != null) {
				master.setPkPi(card.getPkPi());
			} else {
				return null;
			}
		} else if ("code_st".equals(fieldName)) { // 就诊卡号
			BlSettle blSettle = piPubMapper.qryBlSettleByCodeSt(fieldValue);
			if (blSettle != null) {
				master.setPkPi(blSettle.getPkPi());
				encounter.setPkPi(blSettle.getPkPi());
				encounter.setPkPv(blSettle.getPkPv());
			} else {
				return null;
			}
		} else {
			throw new BusException("参数错误！");
		}

		// 参数flagStatus（默认1）：0-不判断就诊状态；1-判断就诊状态（如果参数euStatuss为空，默认查询状态为1的记录）
		if ("1".equals(cparam.getFlagStatus())) {
			// 查询就诊记录表pv_encounter，默认查就诊状态为1的记录
			String[] euStatuss = cparam.getEuStatuss();
			if (euStatuss == null) {
				euStatuss = new String[] { "1" };
			}
			encounter.setEuStatuss(euStatuss);
		}
		if (flag) {
			List<PvEncounter> list = piPubMapper.getPvEncounterList(encounter);
			if (CollectionUtils.isNotEmpty(list)) {
				encounter = list.get(0);
				master.setPkPi(encounter.getPkPi());
				List<PiMaster> listP = piPubMapper.getPiMaster(master);
				if (CollectionUtils.isNotEmpty(listP)) {
					master = listP.get(0);
				}
			} else {
				return null;
			}
		} else {
			List<PiMaster> listP = piPubMapper.getPiMaster(master);
			if (CollectionUtils.isNotEmpty(listP)) {
				master = listP.get(0);
			}
			if (master == null) {
				throw new BusException("无法获取患者信息！");
			}else if(CommonUtils.isEmptyString(master.getPkPi())){
				return null;
			}
			
			encounter.setPkPi(master.getPkPi());
			List<PvEncounter> list = piPubMapper.getPvEncounterList(encounter);
			if (CollectionUtils.isNotEmpty(list)) {
				encounter = list.get(0);
			} else {
				return null;
			}
		}
		vo.setPkPi(master.getPkPi());
		vo.setPkPv(encounter.getPkPv());
		vo.setPkInsu(encounter.getPkInsu());//医保主计划
		vo.setCodePi(master.getCodePi());
		vo.setCodeIp(master.getCodeIp());//添加住院号
		vo.setCodeOp(master.getCodeOp());//添加门诊号
		vo.setNamePi(master.getNamePi());
		vo.setDtSex(master.getDtSex());
		vo.setBirthDate(master.getBirthDate());
		vo.setDtIdtype(master.getDtIdtype());
		vo.setIdNo(master.getIdNo());
		vo.setMobile(master.getMobile());
		vo.setInsurNo(master.getInsurNo());
		vo.setNamePicate(master.getNamePicate());
		vo.setNameRel(master.getNameRel());//联系人
		vo.setFlagIn(encounter.getFlagIn());
		vo.setCodePv(encounter.getCodePv());
		vo.setPkEmpPhy(encounter.getPkEmpPhy());
		vo.setNameEmpPhy(encounter.getNameEmpPhy());
		vo.setPkEmpNs(encounter.getPkEmpNs());
		vo.setNameEmpNs(encounter.getNameEmpNs());
		vo.setBedNo(encounter.getBedNo());
		vo.setPkDept(encounter.getPkDept());
		vo.setPkDeptNs(encounter.getPkDeptNs());
		vo.setEuStatus(encounter.getEuStatus());
		vo.setDateBegin(encounter.getDateBegin());
		vo.setDateEnd(encounter.getDateEnd());
		vo.setDateClinic(encounter.getDateClinic());
		vo.setEuPvtype(encounter.getEuPvtype());
		vo.setPkPicate(encounter.getPkPicate());
		vo.setAddress(master.getAddrCur()+master.getAddrCurDt());
		vo.setFlagSpec(encounter.getFlagSpec());
		vo.setNote(encounter.getNote());
		vo.setFlagMi(encounter.getFlagMi());
		vo.setNameSpouse(encounter.getNameSpouse());
		vo.setIdnoSpouse(encounter.getIdnoSpouse());
		// 住院天数 -- 出院则用出院时间计算，未出院用当前日期
		if( encounter.getDateBegin() != null && encounter.getDateEnd()!=null ){
			vo.setIpDays(DateUtils.getDateSpace(encounter.getDateBegin(), encounter.getDateEnd())+1);
		}else if (encounter.getDateBegin() != null) {
			vo.setIpDays(DateUtils.getDateSpace(encounter.getDateBegin(), new Date()));
		} else {
			vo.setIpDays(0);
		}
		
		//婴儿标志--pv_infant存在 pk_pv_infant = pk_pv，则表示为婴儿
		int cnt = DataBaseHelper.queryForScalar("select count(1) from pv_infant where pk_pv_infant = ? and del_flag='0'"
    			, Integer.class, new Object[]{encounter.getPkPv()});
    	if(cnt > 0)
    		vo.setFlagInfant("1");

		// 过敏信息
		String nameAl = "";
		List<PiAllergic> allergicLsit = piPubMapper .getPiAllergicListByPkPi(master.getPkPi());
		if (CollectionUtils.isNotEmpty(allergicLsit)) {
			int i = 0;
			for (PiAllergic allergic : allergicLsit) {
				if (i == 0) {
					nameAl += allergic.getNameAl();
				} else {
					nameAl += "," + allergic.getNameAl();
					i++;
				}
			}
		}
		vo.setNameAl(nameAl);

		// 诊断信息
		String nameDiag = "";
		String pkDiag = "";
		List<PvDiagVo> diagList = piPubMapper.getPvDiagListByPkPv(encounter.getPkPv());
		if (CollectionUtils.isNotEmpty(diagList)) {
			int i = 0;
			for (PvDiagVo diag : diagList) {
				if (i == 0) {
					nameDiag += diag.getDiagText();
					pkDiag += diag.getPkDiag();
				} else {
					nameDiag += "," + diag.getDiagText();
					pkDiag += "," + diag.getPkDiag();
					i++;
				}
			}
		}
		vo.setPkDiag(pkDiag);
		vo.setNameDiag(nameDiag);

		// 保险 2018-12-13 左连接拓展属性，获取该医保是否为广州医保
		Map<String, Object> mapInsu = DataBaseHelper.queryForMap( "select hp.bedquota,hp.name,attr.val_attr flag_gz_hp from bd_hp hp"
				+ " left join bd_dictattr attr on attr.pk_dict = hp.pk_hp and attr.del_flag = '0'"
				+ " left join bd_dictattr_temp attrtemp on attrtemp.pk_dictattrtemp = attr.pk_dictattrtemp and attrtemp.del_flag = '0' and attrtemp.code_attr = '0307'"
				+ " where hp.del_flag = '0' and hp.pk_hp = ?", encounter.getPkInsu());
		if (mapInsu != null) {
			vo.setNameInsu(mapInsu.get("name").toString());
			vo.setBedquota(CommonUtils.getDoubleObject(mapInsu.get("bedquota"))); 
			if(null != mapInsu.get("flagGzHp") && !CommonUtils.isEmptyString(mapInsu.get("flagGzHp").toString()))
			vo.setFlagGzHp(mapInsu.get("flagGzHp").toString());//是否为广州医保
		} else {
			vo.setNameInsu("");
			vo.setFlagGzHp("0");
		}

		// 预交金金额
		String preSql = "select nvl(sum(amount),0) as amount from BL_DEPOSIT where del_flag='0' and eu_dptype='9' and pk_pv=?";
		AmountBean preAmount = DataBaseHelper.queryForBean(preSql, AmountBean.class, new Object[]{encounter.getPkPv()});
		vo.setPrepayAmount(preAmount.getAmount().doubleValue());

		// 费用总额
		String totalSql = "select nvl(sum(amount),0) as amount from BL_IP_DT where del_flag='0' and pk_pv=?";
		AmountBean totalAmount = DataBaseHelper.queryForBean(totalSql, AmountBean.class, new Object[]{encounter.getPkPv()});
		vo.setTotalAmount(totalAmount.getAmount().doubleValue());
		
		// 自费总额
		String piSql = "select nvl(sum(amount),0) as amount from BL_IP_DT where del_flag='0' and ratio_self=1 and pk_pv=? ";
		AmountBean piAmount = DataBaseHelper.queryForBean(piSql, AmountBean.class, new Object[]{encounter.getPkPv()});
		vo.setAmountPi(piAmount.getAmount().doubleValue());

		return vo;
	}
	
	
}
