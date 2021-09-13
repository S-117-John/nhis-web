package com.zebone.nhis.cn.ipdw.service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.CnNoticeService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnIpPresMapper;
import com.zebone.nhis.cn.ipdw.vo.CnIpPressVO;
import com.zebone.nhis.cn.ipdw.vo.CnOrderVO;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnIpPresService {
	@Resource
	private CnIpPresMapper cnIpPresMapper;
	@Autowired
	private CnPubService cnPubService;

	@Autowired
	private CnNoticeService cnNoticeService;
	/**
	 * 查询患者处方
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CnIpPressVO> qryCnIpPres(String param,IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");	
		if(StringUtils.isBlank(pkPv)) throw new BusException("前台传的患者编码(pkpv)为空!");
		return cnIpPresMapper.qryCnIpPres(pkPv);		
	}
	
	/**
	 * 
	 * 查询一张处方下的明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CnOrderVO> qryCnIpPresOrd(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		return cnIpPresMapper.qryCnIpPresOrd(paramMap);
	}
	
	/**
	 * 删除处方
	 * @param param
	 * @param user
	 */
	public void delPres(String param,IUser user){
		String pkPress = JsonUtil.getFieldValue(param, "pkPress");
	    int count =  DataBaseHelper.queryForScalar("select count(1) from cn_order where pk_pres=? and flag_sign='1' ", Integer.class, new Object[]{pkPress});
		if(count>1) throw new BusException("处方已签署,请刷新！");
		CnPrescription cpv = new CnPrescription();
		cpv.setPkPres(pkPress);
		
		DataBaseHelper.execute("DELETE FROM CN_ORDER WHERE flag_sign='0' and ORDSN_PARENT IN (SELECT ORDSN_PARENT FROM CN_ORDER WHERE PK_PRES = ? )", new Object[]{pkPress});//删除护瞩
		DataBaseHelper.deleteBeanByPk(cpv);
		DataBaseHelper.deleteBeanByWhere(new CnOrder(), "pk_pres='"+cpv.getPkPres()+"'");	
	}
	
	/**
	 * 
	 * 保存处方及明细
	 * @param param
	 * @param user
	 * @return
	 */
	public CnIpPressVO savePresInfos(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		CnIpPressVO rtn=new CnIpPressVO();
		CnIpPressVO cnIpPressMore= JsonUtil.readValue(param, CnIpPressVO.class);	
		List<CnOrderVO> cnOrdList = cnIpPressMore.getOrds();
		List<CnOrder> cnOrdDelList = cnIpPressMore.getOrdsForDel();
		/**人医客户化逻辑**/
		boolean ifRY= "1".equals(cnIpPressMore.getIfry());
		Date dateNow = new Date();
		User userInfo = (User)user;
		String pkPv=null;
		String pkWgOrg=null;
		String pkWg=null;
		if(cnIpPressMore!=null && StringUtils.isNotEmpty(cnIpPressMore.getPkPv())){
			pkPv=cnIpPressMore.getPkPv();

			//判断是否符合开立条件
			if(checkPvInfo(pkPv)){
				throw  new BusException("前患者就诊的科室为日间病房,住院超过允许开立医嘱的天数！不允许开立新医嘱！");
			}

			PvEncounter pvInfo = DataBaseHelper.queryForBean("Select * From PV_ENCOUNTER Where pk_pv=? ",PvEncounter.class, new Object[]{pkPv});
			pkWgOrg=pvInfo.getPkWgOrg();
			pkWg=pvInfo.getPkWg();
		}

		//处理其他逻辑--人医
		if(ifRY){
			if(cnOrdList.size()>0) setOrdPres(cnOrdList);
		}


		CnPrescription pres = new CnPrescription();
		BeanUtils.copyProperties(pres, cnIpPressMore);
		if(StringUtils.isBlank(pres.getPkPres())){
			DataBaseHelper.insertBean(pres);
		}
		else DataBaseHelper.updateBeanByPk(pres,false);
		
		List<CnOrder> addOrdList = new ArrayList<CnOrder>();
		List<CnOrder> updateOrdList = new ArrayList<CnOrder>();
		List<CnOrder> allOrdList= new ArrayList<CnOrder>();
		if(cnOrdList != null)
		{
			for(CnOrderVO cnordVo: cnOrdList ){
				cnordVo.setPkPres(pres.getPkPres());
				CnOrder ord = new CnOrder();
				BeanUtils.copyProperties(ord, cnordVo);
				ord.setPkWgOrg(pkWgOrg);//原医疗组设置
				ord.setPkWg(pkWg);
				if(StringUtils.isBlank(ord.getPkCnord())){
					String pkCnord = NHISUUID.getKeyId();
					ord.setPkCnord(pkCnord);
					ord.setTs(dateNow);
					ord.setCreateTime(dateNow);
					ord.setCreator(userInfo.getPkEmp());
					addOrdList.add(ord);
				}else{
					updateOrdList.add(ord);
				}
				allOrdList.add(ord);
			}
		}
		
		
		if (cnOrdDelList != null && cnOrdDelList.size() > 0) {
			
			DataBaseHelper.batchUpdate("delete from cn_order where pk_cnord = :pkCnord  and eu_status_ord=0 ", cnOrdDelList);
			//如果所删医嘱是父医嘱，则删同组医嘱（包含护嘱）
		    List<CnOrder> delParentOrdList= new ArrayList<CnOrder>();
		    for(CnOrder ordVo:cnOrdDelList){
			   if(ordVo.getOrdsn().compareTo(ordVo.getOrdsnParent())==0){
				   delParentOrdList.add(ordVo);
			   }
			}
	    	if(delParentOrdList.size()>0){
	    		DataBaseHelper.batchUpdate("delete from cn_order where ordsn_parent =:ordsnParent ", delParentOrdList);
		    }
			   
		}
		if (addOrdList != null && addOrdList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), addOrdList);
		}
		if (updateOrdList != null && updateOrdList.size() > 0) {
			cnPubService.vaildUpdateCnOrdts(updateOrdList);
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOrder.class), updateOrdList);
		}
		
		List<CnOrderVO> rtnOrdList = DataBaseHelper.queryForList("select * from cn_order where del_flag='0' and pk_pres = ?", CnOrderVO.class, new Object[] { pres.getPkPres() });
		BeanUtils.copyProperties(rtn, cnIpPressMore);
		BeanUtils.copyProperties(rtn, pres);
		rtn.setOrds(rtnOrdList);
		rtn.setCpExpOrdList(allOrdList);
		
		return rtn;
		
	}
	
	/**
	 * 签署处方
	 * @param param
	 * @param user
	 */
	public CnIpPressVO signPres(String param,IUser user)throws IllegalAccessException, InvocationTargetException{
		User userInfo = (User)user;
		//签署前先保存
		CnIpPressVO cnIpPressMore=  this.savePresInfos(param,user);
		Date nowDate=new Date();
		List<CnOrderVO> cnOrdList = cnIpPressMore.getOrds();
		List<CnOrder> cpExpOrdList = cnIpPressMore.getCpExpOrdList();

		String pkPv=null;
		String pkWgOrg=null;
		String pkWg=null;
		if(cnIpPressMore!=null && StringUtils.isNotEmpty(cnIpPressMore.getPkPv())){
			pkPv=cnIpPressMore.getPkPv();
			PvEncounter pvInfo = DataBaseHelper.queryForBean("Select * From PV_ENCOUNTER Where pk_pv=? ",PvEncounter.class, new Object[]{pkPv});
			pkWgOrg=pvInfo.getPkWgOrg();
			pkWg=pvInfo.getPkWg();
		}

		List<CnOrder> ordList = new ArrayList<CnOrder>();
		for(CnOrderVO cnordVo: cnOrdList ){
			cnordVo.setDateSign(nowDate);
			CnOrder ord = new CnOrder();
			BeanUtils.copyProperties(ord, cnordVo);
			ord.setPkWgOrg(pkWgOrg);
			ord.setPkWg(pkWg);
			ord.setPkEmpOrd(userInfo.getPkEmp());
			ord.setNameEmpOrd(userInfo.getNameEmp());
			ordList.add(ord);
		}
		
		
		if (ordList != null && ordList.size() > 0) {
			DataBaseHelper.batchUpdate("update cn_order set eu_status_ord='1',flag_sign='1',date_sign=:dateSign,pk_emp_ord=:pkEmpOrd,name_emp_ord=:nameEmpOrd,pk_wg=:pkWg,PK_WG_ORG=:pkWgOrg where ordsn_parent =:ordsnParent  and flag_sign='0'", ordList);
			cnPubService.sendMessage("新医嘱",ordList,false);
		}
		
		//保存CA认证信息
		if(cnIpPressMore.getCnSignCa()!=null &&
				!CommonUtils.isEmptyString(cnIpPressMore.getCnSignCa().getEuOptype())){
			List<CnSignCa> caList = new ArrayList<>();
			for(CnOrder ord : ordList){
				CnSignCa caVo = new CnSignCa();
				BeanUtils.copyProperties(caVo, cnIpPressMore.getCnSignCa());
				caVo.setPkBu(ord.getPkCnord());//CA认证业务主键赋值
				caList.add(caVo);
			}
			cnPubService.caRecord(caList);
		}
		
		//路径变异处理
		if(cpExpOrdList!=null && cpExpOrdList.size()>0){
			cnPubService.recExpOrder(false, cpExpOrdList, userInfo);
		}

		//保存临床提醒消息
		cnNoticeService.saveCnNotice(ordList);

		//发送平台消息
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ordlist", ordList);
		PlatFormSendUtils.sendSignPresMsg(paramMap);
		
		return cnIpPressMore;
	}
	/**
	 * 取消签署处方
	 * @param param
	 * @param user
	 */
	public void cancelSignPres(String param,IUser user)throws IllegalAccessException, InvocationTargetException{
		CnIpPressVO cnIpPressMore= JsonUtil.readValue(param, CnIpPressVO.class);	
		List<CnOrderVO> cnOrdList = cnIpPressMore.getOrds();
		
		Map<String, Object> rtnMap = DataBaseHelper.queryForMap(" select count(1) rtn from cn_order ord where ord.pk_pres= ? and ord.eu_status_ord>1", cnIpPressMore.getPkPres());
		if(Integer.parseInt(rtnMap.get("rtn").toString())>0) throw new BusException("有医嘱被核对，不可取消签署！");
		
		Map<String,Object> mapParam = new HashMap<String,Object>();
		mapParam.put("pkPres", cnIpPressMore.getPkPres());
		DataBaseHelper.update("update cn_order set eu_status_ord='0',flag_sign='0',date_sign=null where flag_sign='1' and ordsn_parent in (select ordsn_parent from cn_order where pk_pres = :pkPres )  ", mapParam);
		
		List<CnOrder> ordList = new ArrayList<CnOrder>();
		for(CnOrderVO cnordVo: cnOrdList ){
			CnOrder ord = new CnOrder();
			BeanUtils.copyProperties(ord, cnordVo);
			ordList.add(ord);
		}
//取消签署不发消息
//		if (ordList != null && ordList.size() > 0) {
//			cnPubService.sendMessage("医嘱取消签署",ordList,false);
//		}
		
		//保存CA认证信息
		if(cnIpPressMore.getCnSignCa()!=null &&
				!CommonUtils.isEmptyString(cnIpPressMore.getCnSignCa().getEuOptype())){
			List<CnSignCa> caList = new ArrayList<>();
			cnIpPressMore.getCnSignCa().setPkBu(ordList.get(0).getPkCnord());//CA认证业务主键赋值
			caList.add(cnIpPressMore.getCnSignCa());
			cnPubService.caRecord(caList);
		}
		//发送平台消息
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("ordlist", ordList);
		PlatFormSendUtils.sendCancelSignPresMsg(paramMap);
	}
	/**
	 * 查询处方未退药记录行数
	 * @param param
	 * @param user
	 * @return
	 */
	public int QryUndoneDrugWithdraw(String param,IUser user){
		String pkPres = JsonUtil.getFieldValue(param, "pkPres");	
		if(StringUtils.isBlank(pkPres)) throw new BusException("前台传的处方主键为空!");
		Map<String, Object> rtnMap = DataBaseHelper.queryForMap(" select count(1) rtn from ex_order_occ occ inner join cn_order ord on occ.pk_cnord=ord.pk_cnord where ord.pk_pres=? and occ.pk_pdback is null", pkPres);
		return Integer.parseInt(rtnMap.get("rtn").toString()); 		
	}
	
	/**
	 * 查询模板明细（只查药品细目）
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPresOrdSetDt(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		return  cnIpPresMapper.qryPresOrdSetDt(paramMap);
	}
	
	/**
	 * 更改处方打印标志
	 * @param param
	 * @param user
	 * @return
	 */
	public void modifyPresFlagPrt(String param,IUser user){
		CnIpPressVO presVo= JsonUtil.readValue(param, CnIpPressVO.class);	
		Map<String,Object> mapParam = new HashMap<String,Object>();
		mapParam.put("pkPres", presVo.getPkPres());
		mapParam.put("flagPrt", presVo.getFlagPrt());
		String pkPres = JsonUtil.getFieldValue(param, "pkPres");	
		DataBaseHelper.update("update cn_prescription set flag_prt= :flagPrt where  pk_pres = :pkPres ", mapParam);
		DataBaseHelper.update("update cn_order set flag_print= :flagPrt where  pk_pres = :pkPres ", mapParam);
	}
	
	
	/**
	 * 查询患者处方及处方明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CnIpPressVO> qryCnIpPresAndOrds(String param,IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");	
		if(StringUtils.isBlank(pkPv)) throw new BusException("前台传的患者编码(pkpv)为空!");
		List<CnIpPressVO>  IpPresList= cnIpPresMapper.qryCnIpPres(pkPv);	
		if(IpPresList!=null && IpPresList.size()>0){
			for(CnIpPressVO ipPres: IpPresList ){
				Map<String,Object>  mapParam=new HashMap<String,Object> ();
				mapParam.put("pkPv", pkPv);
				mapParam.put("pkPres", ipPres.getPkPres());
				List<CnOrderVO>  presOrdList=cnIpPresMapper.qryCnIpPresOrd(mapParam);
				ipPres.setOrds(presOrdList);
			}
			
		}
		
		return IpPresList;
	}
	
	public List<Map<String,Object>> qryPresOrdCopy(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");	
		if(StringUtils.isBlank(pkPv)) throw new BusException("前台传的患者编码(pkpv)为空!");
		return  cnIpPresMapper.qryPresOrdCopy(paramMap);
	}
	
	/**
	 * 查询患者常用处方
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPresOrdUse(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		String pkEmpInput = JsonUtil.getFieldValue(param, "pkEmpInput");	
		if(StringUtils.isBlank(pkEmpInput)) throw new BusException("前台传的人员主键为空!");
		String dateEnter = JsonUtil.getFieldValue(param, "dateEnter");	
		if(StringUtils.isBlank(dateEnter)) throw new BusException("前台传的开始日期为空!");
		return  cnIpPresMapper.qryPresOrdUse(paramMap);
	}

   /***
    * @Description 查询药品的仓库默认单位
    * @auther wuqiang
    * @Date 2020-07-23 
    * @Param [param, user]
    * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    */
	public List<Map<String,Object>> getOrdStoreUnit(String param , IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param,Map.class);
		return cnIpPresMapper.getOrdStoreUnit(paramMap);
	}


	/*************************************医嘱属性 前端不需要处理 start**************************************/

	//处理日间病房医嘱开立期限的校验
	private boolean checkPvInfo(String pkPv){
		boolean ok=false;//默认通过校验
		String day=ApplicationUtils.getSysparam("CN0107", false);
		String sql="Select PV.* From pv_encounter pv \n" +
				"Inner Join bd_dictattr dict On pv.pk_dept=dict.pk_dict \n" +
				"Where  pv.pk_pv=? And dict.code_attr='0605' And dict.val_attr='1'";
		PvEncounter pvEncounter=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{pkPv});

		if(StringUtils.isNotEmpty(day) && pvEncounter!=null){
			int dayIp= DateUtils.getDateSpace(pvEncounter.getDateBegin(), new Date());
			int dayI= Integer.parseInt(day);
			//判断
			if(dayI>0 && dayI<dayIp) ok=true;
		}
		return ok;
	}

	//计免标志处理
	private void setOrdPres(List<CnOrderVO> list){
		if(list==null || list.size()==0) return;
		String sql="Select pdatt.pk_pd,pdatt.val_att flag_plan \n" +
				"From bd_pd_att pdatt \n" +
				"Left Join bd_pd_att_define def On pdatt.pk_pdattdef=def.pk_pdattdef \n" +
				"where pdatt.pk_pd=? and def.code_att='0523' ";
		List<Map<String,Object>> attrs=new ArrayList<>();
		for(CnOrderVO vo :list){
			if(!"1".equals(vo.getFlagDurg())) continue;

			attrs=DataBaseHelper.queryForList(sql,new Object[]{vo.getPkOrd()});
			if(attrs.size()>0 && "1".equals(MapUtils.getString(attrs.get(0),"flagPlan"))){
				vo.setFlagPlan("1");//计免标志
				vo.setFlagBase("1");//基数药标志
			}
			attrs=new ArrayList<>();
		}
	}
	/*************************************医嘱属性 前端不需要处理 end**************************************/


	
}
