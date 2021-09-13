package com.zebone.nhis.ma.pub.sd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.sd.dao.CnAegerMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.vo.OrdCaVo;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.sd.vo.CnAeger;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

import javax.annotation.Resource;

/**
 * 疾病诊断说明书服务
 * @author sun
 *
 */
@Service
public class CnAegerService {

	@Resource
	private CnAegerMapper cnAegerMapper;

	/**
	 * 查询疾病诊断证明书信息
	 * 010005003007
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> qryCnAeger(String param, IUser user){
		Map<String, Object> res = null;
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		User loginUser = (User) user ;
		//获取当前就诊患者
//		List<Map<String, Object>> pvList = DataBaseHelper.queryForList("select pv.pk_pv from pv_encounter pv where pv.flag_cancel != '1' and pv.eu_pvtype = ? and pv.eu_status ='1' and pv.del_flag='0'   and pv.pk_emp_phy = ?",map.get("euPvtype") ,loginUser.getPkEmp());
		String pkPv = (String) map.get("pkPv");
		if(StringUtils.isNoneBlank(pkPv)){
			StringBuffer sql = new StringBuffer();
			sql.append("Select pv.name_pi,pv.dt_sex,pv.age_pv,pv.pk_dept,case when pi.tel_no is not null then pi.tel_no else pi.mobile end tel_no,case when pv.date_admit is null then pv.date_begin else pv.date_admit end cn_begin,pi.code_ip,pi.code_op,pv.pk_pv,aeger.* ");
			sql.append(" From pv_encounter pv Inner Join pi_master pi On pv.pk_pi=pi.pk_pi ");
			sql.append(" Inner Join cn_aeger aeger On pv.pk_pv=aeger.pk_pv ");
			sql.append(" Where pv.pk_pv=? ");
//			res= DataBaseHelper.queryForMap(sql.toString(), pvList.get(0).get("pkPv"));
			res= DataBaseHelper.queryForMap(sql.toString(), pkPv);
			if(res==null){
				sql = new StringBuffer();
				sql.append("Select pv.name_pi,pv.dt_sex,pv.age_pv,pv.pk_dept,case when pi.tel_no is not null then pi.tel_no else pi.mobile end tel_no,pi.unit_work address,case when pv.date_admit is null then pv.date_begin else pv.date_admit end cn_begin,pv.date_end date_out,pi.code_ip,pi.code_op,pv.pk_pv,diag.name_diag ");
				sql.append(" From pv_encounter pv Inner Join pi_master pi On pv.pk_pi=pi.pk_pi ");
				sql.append(" Left Join pv_diag diag On pv.pk_pv=diag.pk_pv  And diag.flag_maj='1'  ");
				sql.append(" Where pv.pk_pv=? ");
//				res= DataBaseHelper.queryForMap(sql.toString(), pvList.get(0).get("pkPv"));
				res= DataBaseHelper.queryForMap(sql.toString(), pkPv);
			}

		}
		
		return res;
	}
	/**
	 *保存疾病诊断证明书信息
	 *010005003008
	 * @param param
	 * @param user
	 */
	public String saveAeger(String param, IUser user){
		CnAeger cnAeger = JsonUtil.readValue(param, CnAeger.class);
		User loginUser = (User) user ;
		String res = null;
		if(cnAeger!=null){
			if(StringUtils.isBlank(cnAeger.getPkCnaeger())){
				//保存
//				cnAeger.setPkEmp(loginUser.getPkEmp());
//				cnAeger.setNameEmp(loginUser.getNameEmp());
				DataBaseHelper.insertBean(cnAeger);
				res = cnAeger.getPkCnaeger();
			}else{
				//修改
//				cnAeger.setModifier(loginUser.getPkEmp());
//				cnAeger.setModityTime(new Date());
				DataBaseHelper.updateBeanByPk(cnAeger, false);
			}
		}
		return res;
		
	}
	
	/**
	 * 根据pk_emp获取人员图片签名
	 * 交易码：010005003010
	 * @param param
	 * @param user
	 * @return
	 */
	public OrdCaVo qryCaImgByPkEmp(String param,IUser user){
		
		String pkEmp = JsonUtil.getFieldValue(param, "pkEmp");
		
		OrdCaVo retVo = new OrdCaVo();
		if(!CommonUtils.isEmptyString(pkEmp)){
			retVo = DataBaseHelper.queryForBean(
					"select img_sign from BD_OU_EMPLOYEE where pk_emp = ? ",
					OrdCaVo.class, new Object[]{pkEmp});
		}
		
		return retVo;
	}

	/**
	 * 交易号：010005003012
	 * 根据pkpv查询患者药品处方信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOrdInfoByPkPv(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);

		List<Map<String,Object>> rtnList = new ArrayList<>();
		if(paramMap!=null && paramMap.size()>0){
			rtnList = cnAegerMapper.qryOrdInfoByPkPv(paramMap);
		}

		return rtnList;
	}
	
	
}
