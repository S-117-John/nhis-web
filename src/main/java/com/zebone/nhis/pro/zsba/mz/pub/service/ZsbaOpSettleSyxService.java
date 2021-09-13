package com.zebone.nhis.pro.zsba.mz.pub.service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybPv;
import com.zebone.nhis.pro.zsba.mz.pub.dao.ZsbaOpSettleSyxMapper;
import com.zebone.nhis.pro.zsba.mz.pub.vo.PiParamVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 门诊结算公共服务--syx客户化
 * @author yangxue
 */
@Service
public class ZsbaOpSettleSyxService {

	@Resource
	private ZsbaOpSettleSyxMapper zsbaOpSettleSyxMapper;
//	@Autowired
//	private ZsbaOpCgPubService zsbaOpcgPubService; //门诊计费
	
	/**
	 * 修改患者信息
	 * 交易号：007002004009->022003027133
	 * @throws ParseException  
     */
	@SuppressWarnings({ "unchecked", "unused" })
	public void modifyPiInfo(String param,IUser user) throws ParseException {
		PiParamVo pivo = JsonUtil.readValue(param,PiParamVo.class);
		if(pivo==null||CommonUtils.isEmptyString(pivo.getPkPi())||CommonUtils.isEmptyString(pivo.getPkPv()))
			throw new BusException("未传入患者唯一标识pkPi或pkPv");
		pivo.setTs(new Date());
		pivo.setModifier(((User)user).getPkEmp());
		//1.更新患者信息
		zsbaOpSettleSyxMapper.updatePiMaster(pivo);
		//校验医保计划是否有变更
		PvEncounter pvvo = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ? ", PvEncounter.class, new Object[]{pivo.getPkPv()});
		//2.更新就诊信息
		zsbaOpSettleSyxMapper.updatePvEncounter(pivo);
		//3.更新就诊主医保计划
		DataBaseHelper.update("update pv_insurance set pk_hp =:pkInsu, modifier =:modifier, ts =:ts where pk_pv =:pkPv and del_flag = '0' ", pivo);
		//4.如果存在病种代码和名称则更新医保就诊表
		if((CommonUtils.isNotNull(pivo.getDiseCodg()) && CommonUtils.isNotNull(pivo.getDiseName())) 
				|| CommonUtils.isNotNull(pivo.getInsutype()) || CommonUtils.isNotNull(pivo.getBirctrlType()) 
				|| CommonUtils.isNotNull(pivo.getBirctrlMatnDate())) {
			//计生手术或生育日期格式化
			if(CommonUtils.isNotNull(pivo.getBirctrlMatnDate()) && pivo.getBirctrlMatnDate().length()>=10) {
				pivo.setBirctrlMatnDate(pivo.getBirctrlMatnDate().substring(0, 10)+" 00:00:00");
			}
			//根据医保主计划查询对应医疗类别
			String medType = "11";//默认普通门诊
			StringBuffer hpDictSql = new StringBuffer();
			hpDictSql.append(" select top 1 att.val_attr from bd_dictattr att ");
			hpDictSql.append(" inner join bd_dictattr_temp tmp on att.pk_dictattrtemp=tmp.pk_dictattrtemp ");
			hpDictSql.append(" where att.pk_dict=? and tmp.code_attr='0330' ");
			Map<String,Object> hpDictMap = DataBaseHelper.queryForMap(hpDictSql.toString(), new Object[]{pivo.getPkInsu()});
			if(hpDictMap!=null && hpDictMap.get("valAttr")!=null && CommonUtils.isNotNull(String.valueOf(hpDictMap.get("valAttr")))){
				medType = String.valueOf(hpDictMap.get("valAttr"));
			}
			//查询医保就诊记录
			String insPvSql = "select top 1 * from ins_qgyb_pv where pk_pv=? and del_flag='0' ";
			InsQgybPv insQgybPv = DataBaseHelper.queryForBean(insPvSql, InsQgybPv.class, pivo.getPkPv());
			if(insQgybPv!=null) {
				String updatePvSql = "update ins_qgyb_pv set med_type=?,dise_codg=?,dise_name=?,insutype=?,birctrl_type=?,birctrl_matn_date=? where pk_pv=? and del_flag='0'";
				DataBaseHelper.execute(updatePvSql, new Object[]{medType,pivo.getDiseCodg(),pivo.getDiseName(),pivo.getInsutype(),pivo.getBirctrlType(),pivo.getBirctrlMatnDate(),pivo.getPkPv()});
			}else{
				//获取博爱机构信息
				BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select * from BD_OU_ORG where CODE_ORG=?",BdOuOrg.class,new Object[]{"202"});
				insQgybPv = new InsQgybPv();
				ApplicationUtils.setDefaultValue(insQgybPv, true);
				insQgybPv.setPkOrg(bdOuOrg.getPkOrg());
				insQgybPv.setPkHp(pivo.getPkInsu());
				insQgybPv.setPkPi(pivo.getPkPi());
				insQgybPv.setPkPv(pivo.getPkPv());
				insQgybPv.setNamePi(pivo.getNamePi());
				insQgybPv.setMedType(medType);
				insQgybPv.setDiseCodg(pivo.getDiseCodg());
				insQgybPv.setDiseName(pivo.getDiseName());
				insQgybPv.setInsutype(pivo.getInsutype());
				insQgybPv.setBirctrlType(pivo.getBirctrlType());
				if(StringUtils.isNotEmpty(pivo.getBirctrlMatnDate())) {
					insQgybPv.setBirctrlMatnDate(DateUtils.parseDate(pivo.getBirctrlMatnDate()));
				}
				DataBaseHelper.insertBean(insQgybPv);
			}
		}
		//5.医保计划变更的情况下，根据医保更新记费明细
		//if(StringUtils.isEmpty(pvvo.getPkInsu()) || StringUtils.isEmpty(pvvo.getPkPicate()) || !pvvo.getPkInsu().equals(pivo.getPkInsu()) || !pvvo.getPkPicate().equals(pivo.getPkPicate())){
			//TODO 医保变化费用重算
			//TODO 患者分类费用重算
			//zsbaOpcgPubService.updateBlOpDtCgRate(pivo.getPkPv(), pivo.getPkInsu(), pivo.getPkPicate(),pvvo.getPkInsu(),pvvo.getPkPicate());
		//}
		
        //发送患者信息修改ADT^A08
		Map<String, Object> paramMap = JsonUtil.readValue(param,Map.class);
        PlatFormSendUtils.sendUpPiInfoMsg(paramMap);
	}
}
