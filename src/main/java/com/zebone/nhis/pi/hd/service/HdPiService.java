package com.zebone.nhis.pi.hd.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pi.PiHd;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pi.hd.dao.HdPiMapper;
import com.zebone.nhis.pi.hd.vo.PiMasterHdVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 透析患者管理
 * @author IBM
 *
 */
@Service
public class HdPiService {
	
	@Resource
	private HdPiMapper hdPiMapper;
	
	/**
	 * 查询透析患者登记信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPiHd(String param, IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);		
		if(paramMap==null)
			throw new BusException("未获取到要查询的参数");
		List<Map<String,Object>> result=hdPiMapper.queryHdPiByCodePi(paramMap);
		return result;
	}

	/**
	 * 保存透析患者登记信息
	 * @param param
	 * @param user
	 */
	public void savePiHd(String param, IUser user){
		PiHd piHd=JsonUtil.readValue(param,PiHd.class);
		User u = (User) user;
		PiMaster master = JsonUtil.readValue(param, PiMaster.class);
		if(piHd==null) throw new BusException("未获取到需要保存的数据");
		//保存或更新患者信息_血液透析表
		if(piHd. getPkPihd()==null||"".equals(CommonUtils.getString(piHd.getPkPihd()))){
			piHd.setPkOrg(((User)user).getPkOrg());
			Date time= new java.sql.Date(new java.util.Date().getTime());
			piHd.setDateArch(time);
			piHd.setPkEmp(u.getPkEmp());
			DataBaseHelper.insertBean(piHd);
		}else{
			DataBaseHelper.update("update PI_HD set CODE_HD=:codeHd,DT_HDTYPE=:dtHdtype,DATE_FIRST=:dateFirst,DATE_DEATH=:dateDeath,CNT_WEEK=:cntWeek,eu_status=:euStatus where PK_PIHD=:pkPihd",piHd);
		}
		
		//更新患者信息表
		if(master.getPkPi()!=null && !"".equals(CommonUtils.getString(master.getPkPi()))){
			DataBaseHelper.update("update PI_MASTER set NAME_PI=:namePi,ID_NO=:idNo,DT_SEX=:dtSex,BIRTH_DATE=:birthDate,MOBILE=:mobile,NAME_REL=:nameRel,TEL_REL=:telRel,DT_BLOOD_ABO=:dtBloodAbo,DT_BLOOD_RH=:dtBloodRh,ADDR_REL=:addrRel where PK_PI=:pkPi",master);
		}
	}
	
	/**
	 * 查询透析治疗患者--透析业务
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryHdPi(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map==null )
			throw new BusException("未获取到要查询患者信息");
		List<Map<String,Object>> result=hdPiMapper.queryHdPi(map);
		return result;
	}
}
