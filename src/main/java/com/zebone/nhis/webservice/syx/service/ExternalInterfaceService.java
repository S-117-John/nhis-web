package com.zebone.nhis.webservice.syx.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pi.PiAddress;
import com.zebone.nhis.webservice.syx.dao.ExternalInterfaceMapper;
import com.zebone.nhis.webservice.syx.vo.EffectiveDate;
import com.zebone.nhis.webservice.syx.vo.ExtAddres;
import com.zebone.nhis.webservice.syx.vo.ExtRequest;
import com.zebone.nhis.webservice.syx.vo.ExtReturn;
import com.zebone.nhis.webservice.syx.vo.ExtSubject;
import com.zebone.nhis.webservice.syx.vo.ViewNisEncounter;
import com.zebone.nhis.webservice.syx.vo.ViewNisEncountersEvent;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class ExternalInterfaceService {
	
	@Autowired
	private ExternalInterfaceMapper externalInterfaceMapper;
	
	public ExtReturn extManageMethod(ExtRequest req) {
		ExtReturn extReturn = crearReturn(req);
		switch (req.getActionId()) {
		case "UserAddressInfo":extReturn = addPiAddres(req,extReturn);
		break;
		case "UserAddressInfoDelete":extReturn = deletePiAddres(req,extReturn);
		break;
		case "UserAddressInfoSave":extReturn = savePiAddres(req,extReturn);
		break;
		case "NisEncounterEventQuery":extReturn = qryNisEncounterEvent(req,extReturn);
		break;
		case "NisEncounterQuery":extReturn = qryNisEncounter(req,extReturn);
		break;

		default:{
					ExtReturn returnErr = new ExtReturn();
					returnErr.setCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
					returnErr.getResult().setText("请求失败！未找到对应的服务方法");
					returnErr.getResult().setId("AE");
					return returnErr;
				}
		}
			
		return extReturn;
	}



	/**
	 * 新建返回报文
	 * @param req
	 * @return
	 */
	private ExtReturn crearReturn(ExtRequest req){
		ExtReturn extReturn = new ExtReturn();
		extReturn.setiTSVersion("1.0");
		extReturn.setId(NHISUUID.getKeyId());//消息流水号
		extReturn.setCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//创建时间
		extReturn.setActionId(req.getActionId());//服务名称(英文)
		extReturn.setActionName(req.getActionName());//服务名称(中文)
		extReturn.getResult().setRequestId(req.getId());//返回请求消息id
		extReturn.getResult().setRequestTime(req.getCreateTime());//请求时间
		return extReturn;
	}
	
	/**
	 * 增加患者常用地址信息
	 * @param req
	 * @param ret
	 * @return
	 */
	private ExtReturn addPiAddres(ExtRequest req,ExtReturn ret){
		ExtSubject subject = req.getSubject();
		if(subject == null ){
			return setErr(ret,"未获取到数据！");
		}
		if(StringUtils.isEmpty(subject.getAddress().getUsername())){
			return setErr(ret,"未获取到患者姓名！");
		}
		if(StringUtils.isEmpty(subject.getAddress().getTreatCard())){
			return setErr(ret,"未获取到患者编码！");
		}
		ExtAddres addrReq = subject.getAddress();
		ArrayList<Object[]> objs = new ArrayList<Object[]>();
		String sql = "insert into pi_address(dt_addrtype,pk_pi,pk_addr,name_prov,name_city,name_dist,addr,name_rel,tel,amt_fee,ts,create_time)" + 
				"select (select code from bd_defdoc where code_defdoclist='000014' and flag_def='1'),pi.pk_pi," 
				+ "?,?,?,?,?,?,?,?,?,? from pi_master pi where pi.code_pi=? and pi.name_pi=?";
		String id = NHISUUID.getKeyId();	
		Object[] obj = { id, addrReq.getProvinces(), addrReq.getCity(), addrReq.getZone(), addrReq.getAddrDetail(), addrReq.getConsignee(), addrReq.getConTel(), addrReq.getFee(), new Date(), new Date(), addrReq.getTreatCard(), addrReq.getUsername()};
			objs.add(obj);
		
			try {
				int i = DataBaseHelper.execute(sql, obj);
				if(i<1)
					return setErr(ret, "数据插入失败，请核对数据！");
				ret.getResult().setPkAddr(id);
				return ret;
			} catch (Exception e) {
				return  setErr(ret, "数据异常，保存失败！"); 
			}
	}
	
	private ExtReturn setErr(ExtReturn ret,String err){
		ret.getResult().setText(err);
		ret.getResult().setId("AE");
		return ret;
	}
	/**
	 * 删除患者常用地址服务
	 * @param req
	 * @param extReturn
	 * @return
	 */
	private ExtReturn deletePiAddres(ExtRequest req, ExtReturn extReturn) {
		ExtSubject subject = req.getSubject();
		String pkAddr = subject.getPkAddr();// 获得患者常用地址主键
		if (subject == null || "".equals(pkAddr) || pkAddr == null) {
			return setErr(extReturn, "未获取到数据");
		}
		try {
			int execute = DataBaseHelper.execute("delete from PI_ADDRESS where pk_addr = ?", pkAddr);
			if (execute != 0) {
				//extReturn.getResult().setRequestId(pkAddr);
				return extReturn;
			} else {
				
				return setErr(extReturn, "处理失败");
			}
		} catch (Exception e) {
			return setErr(extReturn, "处理失败");
		}
	}
	
	/**
	 * 保存患者常用地址服务
	 * @param req
	 * @param extReturn
	 * @return
	 */
	private ExtReturn savePiAddres(ExtRequest req, ExtReturn ret) {
		ExtSubject subject = req.getSubject();
		if(subject == null || subject.getAddress() == null){
			return setErr(ret,"请求失败,未获取到数据！");
		}
		ExtAddres address = subject.getAddress();
		PiAddress piAddress = new PiAddress();
		piAddress.setPkAddr(address.getPkAddr());
		try {
			Integer piNum = externalInterfaceMapper.countPiByPk(address.getPkPi());
			if(piNum<1)
				return setErr(ret, "请求失败，没有找到该患者！");
		} catch (Exception e1) {
			return setErr(ret, "请求失败！");
		}
		piAddress.setPkPi(address.getPkPi());
		
		try {
			piAddress.setSortNo(new BigDecimal(address.getSortNo()));
		} catch (Exception e) {
			e.printStackTrace();
			return setErr(ret, "请求失败,排序号非法！");
		}
		
		piAddress.setDtAddrtype(address.getDtAddrtype());
		piAddress.setDtRegionProv(address.getDtRegionProv());
		piAddress.setNameProv(address.getNameProv());
		piAddress.setDtRegionCity(address.getDtRegionCity());
		piAddress.setNameCity(address.getNameCity());
		piAddress.setDtRegionDist(address.getDtRegionDist());
		piAddress.setNameDist(address.getNameDist());
		
		piAddress.setAddr(address.getAddr());
		piAddress.setTel(address.getTel());
		piAddress.setFlagUse(address.getFlagUse());
		if(!"0".equals(address.getFlagUse())&&!"1".equals(address.getFlagUse()))
			return setErr(ret, "请求失败,常用地址标志取值应为0或1!");
		
		try {
			int i = DataBaseHelper.updateBeanByPk(piAddress, false);
			if (i<1)
				return setErr(ret, "请求失败，请检查数据的正确性！");
			ret.getResult().setPkAddr(piAddress.getPkAddr());
			return ret;
		} catch (Exception e) {
			return setErr(ret, "请求失败，请检查数据的正确性！");
		}
		
	}

	
	private ExtReturn qryNisEncounterEvent(ExtRequest req, ExtReturn extReturn) {
		List<ViewNisEncountersEvent> pvEncounterEvent = null;
		ExtReturn errReturn = new ExtReturn();		
		try {
			pvEncounterEvent = externalInterfaceMapper.qryPvEncounterEvent(req.getSubject());
		} catch (Exception e) {
			setErr(errReturn, "查询失败！");
			return errReturn;
		}
		if(pvEncounterEvent != null && pvEncounterEvent.size()>0){
			extReturn = crearReturn(req);
			extReturn.setViewNisEncountersEvent(pvEncounterEvent);
			return extReturn;
		}
		return setErr(errReturn, "没有找到就诊信息！");
	}

	
	private ExtReturn qryNisEncounter(ExtRequest req, ExtReturn extReturn) {
		List<ViewNisEncounter> pvEncounter = null;
		ExtReturn errReturn = new ExtReturn();
		try {
			pvEncounter = externalInterfaceMapper.qryPvEncounter(req.getSubject());
		} catch (Exception e) {
			setErr(errReturn, "查询失败！");
			return errReturn;
		}
		
		if(pvEncounter != null && pvEncounter.size()>0){
			extReturn = crearReturn(req);
			extReturn.setViewNisEncounters(pvEncounter);
			return extReturn;
		}
		
		return setErr(errReturn, "没有找到就诊信息！");
	}


}
