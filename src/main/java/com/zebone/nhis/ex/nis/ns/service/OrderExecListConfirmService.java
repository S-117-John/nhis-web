package com.zebone.nhis.ex.nis.ns.service;

import com.zebone.nhis.bl.pub.service.BlIpMedicalExeService;
import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.MedExeIpParam;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.base.transcode.SysApplog;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.module.ex.nis.ns.ExGluOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExStOcc;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.dao.OrderExecListMapper;
import com.zebone.nhis.ex.nis.ns.vo.ExStOccPv;
import com.zebone.nhis.ex.nis.ns.vo.ExStOccVo;
import com.zebone.nhis.ex.nis.ns.vo.LabColVo;
import com.zebone.nhis.ex.nis.pub.service.NsPubService;
import com.zebone.nhis.ex.pub.service.BlCgExPubService;
import com.zebone.nhis.ex.pub.service.FixedCostService;
import com.zebone.nhis.ex.pub.support.ExListSortByOrdUtil;
import com.zebone.nhis.ex.pub.support.SortByOrdMapUtil;
import com.zebone.nhis.ex.pub.vo.DeptCgItemVo;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.ex.pub.vo.ExlistPubVoRet;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pro.zsba.common.support.DateUtil;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 医嘱执行确认
 * @author yangxue
 */
@Service
public class OrderExecListConfirmService {
	 
	 @Resource
	 private OrderExecListMapper orderExecListMapper;
	 @Resource
	 private BlCgExPubService blCgExPubService;
	 @Resource
	 private FixedCostService fixedCostService;
	 @Resource
	 private IpCgPubService ipCgPubService;
	 @Resource
	 private BlIpMedicalExeService ipMedicalExeService;
	 @Resource
	 private NsPubService nsPubService;
	 
	 private String propertyValue = ApplicationUtils.getPropertyValue("msg.processClass", "");

	 /**
	  * 查询医嘱执行单列表
	  * @param param{dateBegin,dateEnd,confirmFlag,ordtype,euStatus,pkPvs,pkDeptNs}
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<ExlistPubVo> queryExlist(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 
		 String useUiTimeValueFlag = "0";//查询时是否使用前台传来的时间点值，1 是，其他 否
		 if(map.containsKey("useUiTimeValue"))
		 {
			 useUiTimeValueFlag = (String)map.get("useUiTimeValue");
		 }
		 String  dateBegin = CommonUtils.getString(map.get("dateBegin"));
		 if(dateBegin == null){
			 map.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
		 }else{
//			 map.put("dateBegin", dateBegin.substring(0, 12)+"00");
			 if("1".equals(useUiTimeValueFlag))
			 {
				 map.put("dateBegin", dateBegin);	
			 }
			 else
			 {
				 map.put("dateBegin", dateBegin.substring(0, 8)+"000000");				 
			 }
		 }
		 
		 String  dateEnd = CommonUtils.getString(map.get("dateEnd"));
		 if(dateEnd == null){
			 map.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
		 }else{
//			 map.put("dateEnd", dateEnd.substring(0, 12)+"59");			 
			 if("1".equals(useUiTimeValueFlag))
			 {
				 //map.put("dateEnd", dateEnd);
				 map.put("dateEnd", dateEnd.substring(0, 12)+"59");	
			 }
			 else
			 {
				 map.put("dateEnd", dateEnd.substring(0, 8)+"235959");				 
			 }
		 }
		 
		 //设置为医嘱执行确认功能点标志
		 map.put("confirmFlag", "1");
		 List<ExlistPubVo> result = new ArrayList<ExlistPubVo>(16);
		 //根据参数判断使用哪个查询接口 EX0032	不发药可执行药品执行单
		 if("1".equals(ApplicationUtils.getSysparam("EX0032", false))){
			List<ExlistPubVo > exlistPubVoList =  orderExecListMapper.queryExecListContainApDrug(map);
			/**
			 * 优化查询，把原来的查询条件写到代码里
			 *  and not exists ( select 1 from ex_order_occ occ1
			 *                		 inner join cn_order ord1 on occ1.pk_cnord=ord1.pk_cnord
			 *         			 where occ1.pk_pdapdt is null
			 *               		   and occ1.flag_base ='0' and ord1.flag_durg = '1' and occ1.flag_self = '0' and ord1.FLAG_NOTE ='0'
			 *               		   and ord.ordsn_parent = ord1.ordsn_parent
			 *               		   and exlist.date_plan = occ1.date_plan
			 *               		   and exlist.pk_pv = occ1.pk_pv
			 *               )
			 * */
			result=exlistPubVoList.stream().filter(m->
					!(StringUtils.isBlank(m.getPkPdapdt())
							&&"0".equals(m.getFlagBase())
							&&"1".equals(m.getFlagDurg())
							&&"0".equals(m.getFlagSelf())
							&&"0".equals(m.getFlagNote()))
					).collect(Collectors.toList());
		 }else{
			 result =  orderExecListMapper.queryExecListByCon(map);
		 }
		 //追加当查询的医嘱类型为药品时，追加显示同组的非药品医嘱执行单 yangxue 2021.5.13
		 if(result!=null&&result.size()>0&&"01".equals(CommonUtils.getString(map.get("ordtype")))){
            Set<Integer> ordSnParent =result.stream().map((exlistvo)->exlistvo.getOrdsnParent()).collect(Collectors.toSet());
            List<Integer> ordSnParents = new ArrayList<Integer>(ordSnParent);
            map.put("ordsnParents",ordSnParents);
            List<ExlistPubVo> subExlist = orderExecListMapper.queryExecListByParent(map);
            if(subExlist!=null&&subExlist.size()>0){
				result.addAll(subExlist);
				//重新排序
				result = result.stream().sorted(Comparator.comparing(ExlistPubVo::getBedNo)
						.thenComparing(ExlistPubVo::getOrdsnParent)
						.thenComparing(ExlistPubVo::getDatePlan)
						.thenComparing(ExlistPubVo::getOrdsn))
						.collect(Collectors.toList());
			}
		 }

		 new ExListSortByOrdUtil().ordGroup(result);
		 //是否过滤自动执行医嘱
		 if(map.containsKey("notShowAutoExexOrd"))
		 {
			 String notShowAutoExexOrdFlag = (String)map.get("notShowAutoExexOrd");
			 if("1".equals(notShowAutoExexOrdFlag) && result != null)
			 {
				 //查询是否是自动执行医嘱
				 Set<String> setPkOrds = new HashSet<String>();
		         for (ExlistPubVo item : result) {
		        	 if(item.getPkOrd() != null)
		        	 {
				         setPkOrds.add(item.getPkOrd());
		        	 }
		         }
		         List<Map<String, Object>> retAutoExecList = new ArrayList<Map<String, Object>>();
		         if (setPkOrds.size() > 0) {
		            StringBuilder sb = new StringBuilder();
		            sb.append("SELECT att.pk_dict FROM bd_dictattr att INNER JOIN bd_dictattr_temp tmp ");
		            sb.append("ON att.pk_dictattrtemp = tmp.pk_dictattrtemp WHERE tmp.code_attr = '0202' and att.val_attr = '1' ");
		            sb.append(" and (att.pk_dict IN ( ");
		            sb.append(CommonUtils.convertSetToSqlInPart(setPkOrds, "att.pk_dict"));
		            sb.append(")) ");
		            String sql = sb.toString();
		            retAutoExecList = DataBaseHelper.queryForList(sql, new Object[]{});
		            //list转map 
		            Map<String, String> autoExecMap = new HashMap<String, String>();
		            for (Map<String, Object> itemMap : retAutoExecList) {
		            	String key = (String)itemMap.get("pkDict");
		            	if(key != null && !"".equals(key))
		            	{
		            		if(!autoExecMap.containsKey(key))
		            		{
		            			autoExecMap.put(key, "1");
		            		}
		            	}
		            }
		            //比较，过滤
		            if(autoExecMap.size() > 0)
		            {
			            for(int i = 0 ; i < result.size() ; i++) 
			            {
			            	ExlistPubVo exlistPubVo = result.get(i);
			            	if(exlistPubVo.getPkOrd() != null && "0".equals(exlistPubVo.getEuAlways()))//只过滤自动执行的长嘱
			            	{
			            		if(autoExecMap.containsKey(exlistPubVo.getPkOrd()))
			            		{
			            			result.remove(i);
			            			i--;
			            		}
			            	}
			            }		            	
		            }
		        }
			 }
		 }

		 return result;
	 }
	 
	 /**
	  * 根据医嘱的pkCnord查询医嘱执行单列表
     * 交易号：005002002105
	  * @param param
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<ExlistPubVo> queryExlistByPkCnord(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		/*String pkConsrep = map.get("pkConrep").toString();
		String sql = "SELECT cca.pk_cnord as pkcnord FROM CN_CONSULT_RESPONSE cor INNER  JOIN cn_consult_apply cca ON cor.pk_cons = cca.pk_cons WHERE cor.pk_consrep = ?";
		List<Map<String, Object>> orderList = DataBaseHelper.queryForList(sql, new Object[]{pkConsrep});
		String pkCnord = "null";
		if(orderList == null || orderList.size() == 0)
		{
			//没找到对应的会诊申请医嘱,抛出异常
			throw new BusException("通过会诊应答表主键" + pkConsrep + "未找到对应的会诊申请医嘱!");
		}
		else
		{
			Map<String,Object> orderItem = orderList.get(0);
			pkCnord = orderItem.get("pkcnord").toString();
		}*/
		
		Map<String,Object> mapParam = new HashMap<String,Object>();
		
		String pkCnord = map.get("pkCnord").toString();
		mapParam.put("pkCnord", pkCnord); 
		List<ExlistPubVo> result = new ArrayList<ExlistPubVo>(16);
		
		result =  orderExecListMapper.queryExecListByPkCnord(mapParam);

		return result;
	 }
	 
	 /**
	  * 扫码执行确认
	  * @param param{codeIp,pkExocc}
	  * @param user
	  */
	 @SuppressWarnings("unchecked")
	public void confirmExByScan(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 if(map == null || map.get("codeIp") == null || CommonUtils.isEmptyString(map.get("codeIp").toString()) || 
				 map.get("pkExocc") == null || CommonUtils.isEmptyString(map.get("pkExocc").toString()))
			 throw new BusException("未获取到需要执行的患者住院号及执行单号！");
		 List<ExlistPubVo> result =  orderExecListMapper.queryExecListByPkAndCodeIp(map);
		 if(result == null||result.size()<=0)
			 throw new BusException("未获取到符合条件的执行单，无法完成执行确认！");
		 User u = (User)user;
		 //执行确认
		 blCgExPubService.execAndCg(result, u);
		 
		 
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 paramMap.put("exlist", JsonUtil.readValue(param,new TypeReference<List<Map<String,Object>>>(){}));
		 paramMap.put("typeStatus", "ADD");
		 PlatFormSendUtils.sendExConfirmMsg(paramMap);
		 paramMap = null;
		 
	 }
	/**
	 * 不执行
	 * @param param{List<ExlistPubVo>}
	 * @param user
	 */
	public void confirmUnEx(String param,IUser user) {
		User u = (User)user;
		List<ExlistPubVo> exList = JsonUtil.readValue(param, new TypeReference<List<ExlistPubVo>>() {
		});
		if (exList == null) return;
		Set<String> setList = new HashSet<>();
		for(ExlistPubVo vo:exList){
			setList.add(vo.getPkExocc());
		}
		StringBuilder updateSql = new StringBuilder("update ex_order_occ set ");
		String date = DateUtils.dateToStr("yyyyMMddHHmmss", new Date());
		updateSql.append("modifier = '" + u.getPkEmp() + "' ");
		updateSql.append(",modity_time = to_date('" + date + "','YYYYMMDDHH24MISS') ");
		updateSql.append(",ts = to_date('" + date + "','YYYYMMDDHH24MISS') ");
		updateSql.append(",flag_canc='1',eu_status ='2'");
		updateSql.append(" ,date_canc =to_date('" + date + "','YYYYMMDDHH24MISS')");
		updateSql.append(" ,pk_dept_canc ='" + u.getPkDept() + "'");
		updateSql.append(" ,pk_emp_canc ='" + u.getPkEmp() + "'");
		updateSql.append(" ,name_emp_canc ='" + u.getNameEmp() + "'");
		updateSql.append(" where pk_exocc in (" + CommonUtils.convertSetToSqlInPart(setList, "pk_exocc") + ")");
		DataBaseHelper.update(updateSql.toString());
	}
	 /**
	  * 执行确认
	  * @param param{List<ExlistPubVo>}
	  * @param user
	  */
	public void confirmEx(String param,IUser user){
		List<ExlistPubVo> exList = JsonUtil.readValue(param,new TypeReference<List<ExlistPubVo>>(){});
		if(exList == null) return;
		//如果是计费医嘱，调用计费接口
		//判断是否来自会诊应答后自动执行的医嘱
		boolean bConsultOrder = false;
		String pkPv = "";
		for (ExlistPubVo ex : exList) {
			if("1".equals(ex.getConsultFlag()))
			{
				bConsultOrder = true;
				pkPv = ex.getPkPv();
			}
			break;
		}
		User u = null;
		if(bConsultOrder)
		{
			//获取此医嘱对应患者此次就诊的责任护士信息，以此责任护士的身份自动执行
			u = new User();
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT pv.pk_org as pkorg, pv.pk_dept_ns as pkdeptns, ");
			sb.append("deptns.name_dept as namedeptns, ");
			sb.append("pv.name_emp_ns as nameempns, ");
			sb.append("pv.pk_emp_ns as pkempns ");
			sb.append("FROM   pv_encounter pv ");
			sb.append("LEFT   JOIN bd_ou_dept deptns ");
			sb.append("ON     pv.pk_dept_ns = deptns.pk_dept ");
			sb.append("WHERE  pv.pk_pv = ? ");
			String sql = sb.toString();
			List<Map<String, Object>> empList = DataBaseHelper.queryForList(sql, new Object[]{pkPv});
			for(Map<String, Object> mapItem : empList) 
			{
				if(mapItem.get("pkdeptns") != null)
				{
					//mapItem.get("namedeptns").toString()
					u.setPkOrg(mapItem.get("pkorg").toString());
					u.setPkDept(mapItem.get("pkdeptns").toString());
					u.setPkEmp(mapItem.get("pkempns").toString());
					u.setNameEmp(mapItem.get("nameempns").toString());
				}
				break;
			}
		}
		else
		{
			u = (User)user;
		}
		 //调用公共执行计费服务
		 List<BlPubParamVo> gclist = blCgExPubService.execAndCg(exList, u);
		 //orderExecListMapper.updateTransApply(exList);//记费时更新输血执行单状态cn_trans_apply.eu_status = '3'
		 //修改为支持超过1000条执行确认数据的情况
		 Set<String> ordSet = new HashSet<String>();
		 for(ExlistPubVo ex:exList){
			 ordSet.add(ex.getPkCnord());
		 }
		 DataBaseHelper.update("UPDATE CN_TRANS_APPLY SET eu_status='3'where eu_status < '3' and pk_cnord in ("+CommonUtils.convertSetToSqlInPart(ordSet, "pk_cnord")+")"); 
		 DataBaseHelper.update("UPDATE cn_lab_apply SET eu_status='3'where eu_status < '3' and pk_cnord in ("+CommonUtils.convertSetToSqlInPart(ordSet, "pk_cnord")+")"); 
		 //住院检查执行确认或恢复是否更新申请单状态 -EX0077  0-不更新 否则更新
	     String isUpApplyStatus = ApplicationUtils.getSysparam("EX0077", false);
	     //考虑参数null的情况，这里取反
		 if(!"0".equals(isUpApplyStatus)) {
			 DataBaseHelper.update("UPDATE cn_ris_apply SET eu_status='3'where eu_status < '3' and pk_cnord in ("+CommonUtils.convertSetToSqlInPart(ordSet, "pk_cnord")+")"); 
		 }

		 //2019-02-20 记费确认时更新临时医嘱的执行护士、计划执行时间
//		 List<String> upOrdPks = new ArrayList<String>();
//		 Map<String,Object> upMap = new HashMap<String,Object>();
//		 for (ExlistPubVo ex : exList) {
//			 if("1".equals(ex.getEuAlways())&& !CommonUtils.isEmptyString(ex.getPkEmpEx())){
//				 if(!upMap.containsKey("pkEmpEx") ){
//					 upMap.put("modifier", ex.getNameEmpEx());
//					 upMap.put("modifierTime", new Date());
//					 upMap.put("pkEmpEx", ex.getPkEmpEx());
//					 upMap.put("nameEmpEx", ex.getNameEmpEx());
//					 upMap.put("datePlanEx", ex.getDatePlanEx());
//				 }
//				 upOrdPks.add(ex.getPkCnord());
//			 }
//		 }
//		 if(upOrdPks.size() > 0){
//			 upMap.put("pkCnords", upOrdPks);
//			 orderExecListMapper.updateOrdEmpNsEx(upMap);
//		 }
		 
		//执行确认时保存CA认证信息
		 caRecrdByOrd(exList);
		 
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 paramMap.put("exlist", JsonUtil.readValue(param,new TypeReference<List<Map<String,Object>>>(){}));
		 paramMap.put("typeStatus", "ADD");
		 paramMap.put("confirmEx", "");//方法标志，深大使用
		 PlatFormSendUtils.sendExConfirmMsg(paramMap);
		 
	 }
	 /**
	  * 查询未执行试敏医嘱
	  * @param param{dateBegin,dateEnd,pkPvs}
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryStUnExlist(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
//		 map.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
//		 map.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
		 map.put("pkDeptNs", ((User)user).getPkDept());
		 String  dateBegin = CommonUtils.getString(map.get("dateBegin"));
		 if(dateBegin == null){
			 map.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
		 }else{
			 map.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		 }
		 String  dateEnd = CommonUtils.getString(map.get("dateEnd"));
		 if(dateEnd == null){
			 map.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
		 }else{
			 map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		 }
		 
		 List<Map<String, Object>> result = orderExecListMapper.queryStUnExecListByPv(map);
		 new SortByOrdMapUtil().ordGroup(result);
		 
		 return result;

	 }
	 /**
	  * 查询已执行试敏医嘱
	  * @param param{dateBegin,dateEnd,pkPvs}
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryStExlist(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
//		 map.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
//		 map.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
		 map.put("pkDeptNs", ((User)user).getPkDept());
		 String  dateBegin = CommonUtils.getString(map.get("dateBegin"));
		 if(dateBegin == null){
			 map.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
		 }else{
			 map.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		 }
		 String  dateEnd = CommonUtils.getString(map.get("dateEnd"));
		 if(dateEnd == null){
			 map.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
		 }else{
			 map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		 }
		 List<Map<String, Object>> result = orderExecListMapper.queryStExecListByPv(map);
		 new SortByOrdMapUtil().ordGroup(result);
		 return result;
	 }
	 
	 /**
	  * 查询全部试敏医嘱
	  * @param param{dateBegin,dateEnd,pkPvs}
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryAllStExlist(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 map.put("pkDeptNs", ((User)user).getPkDept());
		 String  dateBegin = CommonUtils.getString(map.get("dateBegin"));
		 if(dateBegin == null){
			 map.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
		 }else{
			 map.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		 }
		 String  dateEnd = CommonUtils.getString(map.get("dateEnd"));
		 if(dateEnd == null){
			 map.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
		 }else{
			 map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		 }
		 List<Map<String, Object>> result = orderExecListMapper.queryAllStExecListByPv(map);
		 new SortByOrdMapUtil().ordGroup(result);

		 return result;
	 }
	 
	 /**
	  * 保存试敏结果
	  * @param param
	  * @param user
	  */
	 public void saveStResult(String param,IUser user){
		 ExStOccVo stvo = JsonUtil.readValue(param, ExStOccVo.class);
		 ExStOcc vo = new ExStOcc();
		 //深大--判断执行时间是否取开始时间
		 Date dateEx = "1".equals(stvo.getIsExDateGetStartDate()) ? stvo.getDateBeginSt() : stvo.getDateEndSt();
		 if(stvo != null){
			 ApplicationUtils.copyProperties(vo, stvo);
			 if(CommonUtils.isEmptyString(vo.getPkStocc())){
				 vo.setDateOcc(dateEx);
				 DataBaseHelper.insertBean(vo);
			 }
			 else{
				 vo.setDateOcc(dateEx);
				 DataBaseHelper.updateBeanByPk(vo,false);
			 }
		 }
		 String pk_cnord = stvo.getPkCnord();
		 ExlistPubVo paramVO = new ExlistPubVo();
		
		//先根据 pk_cnord查询出对应的医嘱
		 CnOrder ordVO = DataBaseHelper.queryForBean("select * from cn_order where pk_cnord = ? ", CnOrder.class,pk_cnord);
		 List<ExlistPubVo> exListMaps=new ArrayList<ExlistPubVo>();
		 paramVO.setIsskt("Y");
		 paramVO.setPkCnord(pk_cnord);
		 paramVO.setFlagSt("st");
		 paramVO.setCodeSupply(ordVO.getCodeSupply());
		 paramVO.setPkExocc(stvo.getPkExocc());
		 paramVO.setFlagBase(ordVO.getFlagBase());
		 paramVO.setFlagBl(ordVO.getFlagBl());
		 paramVO.setFlagDurg(ordVO.getFlagDurg());
		 paramVO.setOrdsn(ordVO.getOrdsn());
		 paramVO.setOrdsnParent(ordVO.getOrdsnParent());
		 paramVO.setPkOrd(ordVO.getPkOrd());
		 paramVO.setEuStatus("0");
		 paramVO.setEuPvtype(ordVO.getEuPvtype());
		 paramVO.setPkOrg(ordVO.getPkOrg());
		 paramVO.setPkPv(ordVO.getPkPv());
		 paramVO.setPkEmpOrd(ordVO.getPkEmpOrd());
		 paramVO.setNameEmpOrd(ordVO.getNameEmpOrd());
		 paramVO.setDatePlan(stvo.getDateOcc());
		 paramVO.setPkDept(ordVO.getPkDept());
		 paramVO.setPkDeptPv(ordVO.getPkDept());
		 paramVO.setPkDeptNs(ordVO.getPkDeptNs());
		 paramVO.setPkDeptExec(ordVO.getPkDeptExec());
		 paramVO.setQuanOcc(stvo.getQuanOcc());
		 paramVO.setPackSize(stvo.getPackSize());
		 paramVO.setPkPi(ordVO.getPkPi());
		 paramVO.setPkUnit(stvo.getPkUnit());
		 paramVO.setPkOrgOcc(stvo.getPkOrgOcc());
		 paramVO.setInfantNo(ordVO.getInfantNo()+"");
		 paramVO.setPkEmpOcc(stvo.getPkEmpOcc());
		 paramVO.setNameEmpOcc(stvo.getNameEmpOcc());
		 paramVO.setPkEmpEx(stvo.getPkEmpOcc());
		 paramVO.setNameEmpEx(stvo.getNameEmpOcc());
		 paramVO.setDatePlanEx(dateEx);
		 paramVO.setDateOcc(dateEx);
	     exListMaps.add(paramVO);
	     User u = (User)user;
	     //更新执行单并计费
	     blCgExPubService.execAndCg(exListMaps, u);
		 //保存皮试结果变更日志
		 nsPubService.saveSysApplogInfo(stvo,u,ordVO.getEuSt());
		//设置过敏史及医嘱名称
		setPiAllergic(stvo,(User)user,vo.getPkStocc());
	 }

	 /**
	  * 更新试敏结果
	  * @param param
	  * @param user
	  */
	 public void updateStResult(String param,IUser user){
		 ExStOccVo stvo = JsonUtil.readValue(param, ExStOccVo.class);
		 User u = (User)user;
		 if(stvo == null||CommonUtils.isEmptyString(stvo.getPkStocc())){
			 throw new BusException("未获取到试敏结果内容！");
		 }
		 ExStOcc st = new ExStOcc();
		 ApplicationUtils.copyProperties(st, stvo);
		 DataBaseHelper.updateBeanByPk(st,false);
		 //修改皮试结果时修改执行人为页面上的操作人
		 CnOrder ordVO = DataBaseHelper.queryForBean("select * from cn_order where pk_cnord = ? ", CnOrder.class,stvo.getPkCnord());
		 StringBuilder updateSql = new StringBuilder("update cn_order set ");
		 updateSql.append("modifier = '"+u.getPkEmp()+"' ");
		 updateSql.append(",modity_time = to_date('"+DateUtils.dateToStr("yyyyMMddHHmmss",new Date())+"','YYYYMMDDHH24MISS') ");
		 updateSql.append(",ts = to_date('"+DateUtils.dateToStr("yyyyMMddHHmmss",new Date())+"','YYYYMMDDHH24MISS') ");
		 updateSql.append(" ,pk_emp_ex ='"+stvo.getPkEmpOcc()+"'");
		 updateSql.append(" ,name_emp_ex ='"+stvo.getNameEmpOcc()+"'");
		 updateSql.append(" where ordsn_parent = '"+ordVO.getOrdsnParent()+"'");
		 DataBaseHelper.update(updateSql.toString());
		 StringBuilder sql = new StringBuilder("update ex_order_occ  set ex_order_occ.pk_emp_occ='"+stvo.getPkEmpOcc()+"',ex_order_occ.name_emp_occ='"+stvo.getNameEmpOcc()+"'  ");
		 sql.append(" where exists (select pk_cnord from cn_order ord where ord.pk_cnord = ex_order_occ.pk_cnord ");
		 sql.append(" and ord.ordsn_parent in (select o.ordsn_parent from cn_order o where o.pk_cnord = '"+stvo.getPkCnord()+"' ))");
		 DataBaseHelper.update(sql.toString());
		 //保存皮试结果变更日志
		 nsPubService.saveSysApplogInfo(stvo,u,ordVO.getEuSt());
		 //设置过敏史及医嘱名称
		 setPiAllergic(stvo,(User)user,st.getPkStocc());
	 }


	 
	 /**
	  * 查询试敏结果
	  * param{pkCnord}
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public ExStOccPv queryStResult(String param,IUser user){
		 Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		 if(paramMap == null||CommonUtils.isEmptyString(paramMap.get("pkCnord")))
			 throw new BusException("未获取到医嘱主键！");
		 String pkCnord = paramMap.get("pkCnord").toString();
		 CnOrder ord= DataBaseHelper.queryForBean("select pk_pv from cn_order where pk_cnord=?", CnOrder.class, new Object[]{pkCnord});
		 String pkPv ="";
		 if(ord!=null)pkPv = ord.getPkPv();
		 ExStOccPv st = new ExStOccPv();
		 if(StringUtils.isNotBlank(pkPv)){
			 List<ExStOccPv> occ = orderExecListMapper.queryExStOccPv(pkCnord, pkPv);
			 if(occ!=null && occ.size()>0) st =occ.get(0); 
		 }
	     return st;
	 }
	 /**
	  * 保存或更新试敏结果时，更新过敏史及医嘱名称
	  * @param stvo
	  * @param user
	  */
	private void setPiAllergic(ExStOccVo stvo,User user,String pkStocc){
		//如果是阳性，则插入患者过敏史
		 if(!"-".equals(stvo.getResult())){
			   StringBuilder sql= new StringBuilder(" select pd.name,pi.dt_pharm,pd.dt_pharm as ph,pi.pk_bu from cn_order ord   ");
			   sql.append(" inner join bd_pd pd on pd.pk_pd=ord.pk_ord and ord.flag_durg='1'  ");
			   sql.append(" left join pi_allergic pi on pi.pk_pi = ord.pk_pi  ");
			   sql.append(" where ord.pk_cnord = ? ");
			     List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			     if(CommonUtils.isEmptyString(stvo.getPkStocc())){//新增
			    	 list = DataBaseHelper.queryForList(sql.toString() ,new Object[]{stvo.getPkCnord()});
			     }else{//更新
			    	 sql.append(" and pi.pk_bu = '"+pkStocc+"'");
			    	 list = DataBaseHelper.queryForList(sql.toString(),new Object[]{stvo.getPkCnord()});
			     }
				 if(list == null||list.size()<=0){//更新前被删掉了，重新按新增查询
					 list = DataBaseHelper.queryForList(sql.toString(),new Object[]{stvo.getPkCnord()});
				 }
				 if(list !=null&&list.size()>0){
			    	 String dt_pharm = CommonUtils.getString(list.get(0).get("dtPharm"));
			    	 String pk_bu = CommonUtils.getString(list.get(0).get("pkBu"));
			    	 if(CommonUtils.isEmptyString(dt_pharm)&&CommonUtils.isEmptyString(pk_bu)){//插入过敏史
			    			 PiAllergic al = new PiAllergic();
					    	 al.setPkPi(stvo.getPkPi());
					    	 al.setEuMcsrc("0");
					    	 al.setEuAltype("9");
					    	 al.setDateFind(new Date());
					    	 al.setNameEmpRec(user.getNameEmp());
					    	 al.setPkEmpRec(user.getPkEmp());
					    	 al.setDateRec(new Date());
					    	 al.setDtPharm(CommonUtils.getString(list.get(0).get("ph")));
					    	 al.setNameAl(CommonUtils.getString(list.get(0).get("name")));
					    	 al.setDelFlag("0");
					    	 al.setPkBu(pkStocc);
					    	 DataBaseHelper.insertBean(al);
			    	 }
			     }
		 }else{//如果是阳性，删掉原有的阴性记录
			 DataBaseHelper.execute("delete from pi_allergic where pk_bu = ? ", stvo.getPkStocc());
		 }
		String eu_st = "-".equals(stvo.getResult())?"2":"3";
		//将皮试结果写入医嘱表cn_order的name_ord
	 	String sql_t="update cn_order  set  eu_st='"+eu_st+"' where pk_cnord='"+stvo.getPkCnord()+"'";
	 	DataBaseHelper.update(sql_t, new Object());
	 	//更新关联的医嘱列表标志
	 	if(stvo.getOrdList()!=null&&stvo.getOrdList().size()>0){
	 		Map<String,Object> paramMap = new HashMap<String,Object>();
	 		paramMap.put("ordList", stvo.getOrdList());
	 		paramMap.put("result", stvo.getResult());
	 		paramMap.put("euSt", eu_st);
	 		orderExecListMapper.updateStOrd(paramMap);
	 	}
	}

	/**
	 * 查询未查的
	 * @param{pkPv,pkCnord}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryStOrderList(String param,IUser u){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null ||paramMap.get("pkPv")==null ||"".equals(paramMap.get("pkPv").toString())){
			throw new BusException("未获取到患者就诊主键！");
		}
		return orderExecListMapper.queryStOrdList(paramMap);
	}
	 /**
	  * 查询未计费固定费用列表
	  * @param param{pkPvs,pkDeptNs}
	  * @param u
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<DeptCgItemVo> queryFixedCgList(String param,IUser u){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 map.put("pkOrg", ((User)u).getPkOrg());
		 String dateOut =  CommonUtils.getString(map.get("dateOut"));//出院日期
		 Date cgEnd = null;
		 if(dateOut!=null&&!"".equals(dateOut)){
			 cgEnd = DateUtils.strToDate(dateOut);
		 }
		 List<DeptCgItemVo> list =  fixedCostService.execFixedCharge(map, true,(User)u,cgEnd);
		 if(cgEnd!=null){
			 for(DeptCgItemVo item:list){
				 item.setDateOut(cgEnd);//设置出院日期
			 }
		 }
			 
		 return list;
	 }
	 
	 /**
	  * 记固定费用
	  * @param param
	  * @param u
	  */
	 public void cgFixedCost(String param,IUser u){
		 List<DeptCgItemVo> list = JsonUtil.readValue(param,new TypeReference<List<DeptCgItemVo>>(){});
		 User user = (User)u;
		 String error = fixedCostService.saveFixedCost(user.getPkDept(),1, list,true,false,null);
		 if(!CommonUtils.isEmptyString(error)){
			 throw new BusException(error);
		 }
	 }
	 
	 /**
	  * 查询血糖执行单列表
	  * @param param
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryBloodList(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 String  dateS = CommonUtils.getString(map.get("dateS"));
		 if(dateS == null){
			 map.put("dateS", DateUtils.getDateStr(new Date())+"000000");
		 }else{
			 map.put("dateS", dateS.substring(0, 8)+"000000");
		 }
		 String  dateE = CommonUtils.getString(map.get("dateE"));
		 if(dateE == null){
			 map.put("dateE", DateUtils.getDateStr(new Date())+"235959");
		 }else{
			 map.put("dateE", dateE.substring(0, 8)+"235959");
		 }
		 List<Map<String,Object>> list=orderExecListMapper.queryBloodList(map);
		 return list;
	 }
	 
	 /**
	  * 保存血糖执行单列表
	  * @param param
	  * @param user
	  */
	 public void saveBlood(String param,IUser user){
		 ExGluOcc ex = JsonUtil.readValue(param, ExGluOcc.class);
		 if(CommonUtils.isEmptyString(ex.getPkGluocc())){
			 DataBaseHelper.insertBean(ex);
		 }else{
			 String sql = "update ex_glu_occ set dt_glupoint=:dtGlupoint, dt_glutype=:dtGlutype,"
			 		+ " eu_reason=:euReason, note=:note , val=:val , "
			 		+ " ts = to_date('"+DateUtils.getDate("yyyyMMddHHmmss")+"', 'YYYYMMDDHH24MISS')  "
			 		+ " where pk_gluocc =:pkGluocc and del_flag = '0' ";
			 Map<String,Object> map = new HashMap<String,Object>();
			 map.put("dtGlupoint", ex.getDtGlupoint());
			 map.put("dtGlutype", ex.getDtGlutype());
			 map.put("euReason", ex.getEuReason());
			 map.put("note", ex.getNote());
			 map.put("val", ex.getVal());
			 map.put("pkGluocc", ex.getPkGluocc());
			 DataBaseHelper.update(sql, map);
//			 DataBaseHelper.updateBeanByPk(ex, false);
		 }
	 }
	 
	 /**
	  * 更新血糖执行状态
	  * @param param
	  * @param user
	  */
	 public void updateExOrderOcc(String param,IUser user){
		 ExGluOcc ex = JsonUtil.readValue(param, ExGluOcc.class);
		 ExOrderOcc order=new ExOrderOcc();
		 order.setNameEmpOcc(ex.getNameEmpOcc());
		 order.setPkDeptOcc(ex.getPkDeptOcc());
		 order.setPkEmpOcc(ex.getPkEmpOcc());
		 order.setPkExocc(ex.getPkExocc());
		 order.setPkOrgOcc(ex.getPkOrgOcc());
		 order.setDateOcc(ex.getDateOcc());
		 order.setEuStatus("1");
		 BlPubParamVo bl=new BlPubParamVo(); 
		 bl.setPkOrg(ex.getPkOrgOcc());
		 bl.setPkPv(ex.getPkPv());
		 bl.setPkPi(ex.getPkPi());
		 bl.setDateHap(new Date());
		 bl.setPkOrgEx(order.getPkOrgOcc());
		 bl.setPkDeptEx(ex.getPkDeptOcc());
		 bl.setFlagPd("0");
		 bl.setPkDeptCg(ex.getPkDeptOcc());
		 bl.setPkEmpCg(ex.getPkEmpOcc());
		 bl.setNameEmpCg(ex.getNameEmpOcc());
		 String cnord=ex.getPkCnord();
		 CnOrder ordVO=DataBaseHelper.queryForBean("select * from cn_order where pk_cnord = ? ", CnOrder.class,cnord);
		 bl.setPkOrdexdt(ex.getPkExocc());
		 bl.setCodeOrdtype(ordVO.getCodeOrdtype());
		 bl.setDateStart(ordVO.getDateStart());
		 bl.setPkOrgApp(ordVO.getPkOrg());
		 bl.setPkDeptApp(ordVO.getPkDept());
		 bl.setPkDeptNsApp(ordVO.getPkDeptNs());
		 bl.setPkEmpApp(ordVO.getPkEmpOrd());
		 bl.setNameEmpApp(ordVO.getNameEmpOrd());
		 bl.setPkOrd(ordVO.getPkOrd());
		 bl.setQuanCg(ordVO.getQuan());
		 bl.setPkCnord(ordVO.getPkCnord());
		 bl.setEuPvType(ordVO.getEuPvtype());
		 bl.setFlagFit(ordVO.getFlagFit());
		 List<BlPubParamVo> list=new ArrayList<BlPubParamVo>();
		 list.add(bl);
		 //判断对应执行单是否已执行
		 String queStr = "select * from ex_order_occ where pk_exocc = ?";
		 ExOrderOcc occ = DataBaseHelper.queryForBean(queStr,ExOrderOcc.class,order.getPkExocc());
		 if("1".equals(occ.getEuStatus())){
			 throw new BusException("该执行单已被执行，请刷新数据后重新操作！");
		 }
		 if(CommonUtils.isEmptyString(ex.getPkGluocc())){
			 DataBaseHelper.insertBean(ex);
			 ipCgPubService.chargeIpBatch(list,false);
		 }else{
			 DataBaseHelper.updateBeanByPk(ex, false);
		 }
		 orderExecListMapper.updateExOrderOcc(order);
	 }

	 /**
	  * 根据扫描的住院号及条码查询执行单列表
	  * @param param
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<ExlistPubVo> queryExlistByScan(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 if(map == null || map.get("codeIp") == null || CommonUtils.isEmptyString(map.get("codeIp").toString()) || 
				 map.get("pkExocc") == null || CommonUtils.isEmptyString(map.get("pkExocc").toString()))
			 throw new BusException("未获取到需要执行的患者住院号及执行单号！");
		 List<ExlistPubVo> result =  orderExecListMapper.queryExecListByPkAndCodeIp(map);
		 return result;
	 }
	 
	 /**
	  * 第二次执行确认
	  * @param param{List<ExlistPubVo>}
	  * @param user
	  */
	 public void confirmExSecond(String param,IUser user){
		 List<ExlistPubVo> exList = JsonUtil.readValue(param,new TypeReference<List<ExlistPubVo>>(){});
		 if(exList == null) 
			 throw new BusException("未获取到需要二次执行的执行单！");
		 String pkEmp = exList.get(0).getPkEmpOcc2();
		 String nameEmp = exList.get(0).getNameEmpOcc2();
		 if(CommonUtils.isEmptyString(pkEmp))
			 throw new BusException("未获取到二次执行的执行人主键！");
		 if(CommonUtils.isEmptyString(nameEmp))
			 throw new BusException("未获取到二次执行的执行人名称！");
		 String pkExoccs = "";
		 for (ExlistPubVo ex : exList) {
			 pkExoccs += "'"+ex.getPkExocc()+"',";
		}
		 if(CommonUtils.isEmptyString(pkExoccs)) return;
		 pkExoccs = pkExoccs.substring(0,pkExoccs.length() - 1);

		 String sql = "update ex_order_occ set pk_emp_occ2 = ? , name_emp_occ2 = ?,"
		 		+ "ts = to_date('"+DateUtils.getDate("yyyyMMddHHmmss")+"','YYYYMMDDHH24MISS') where pk_exocc in ("+pkExoccs+")";
		 int cnt = DataBaseHelper.execute(sql, new Object[]{pkEmp,nameEmp});
		 if(cnt != exList.size())
			 throw new BusException("部分执行单未执行成功！");
		 
		 //2019-02-21 记费确认时更新临时医嘱的执行护士、计划执行时间
//		 List<String> upOrdPks = new ArrayList<String>();
//		 Map<String,Object> upMap = new HashMap<String,Object>();
//		 for (ExlistPubVo ex : exList) {
//			 if("1".equals(ex.getEuAlways())&& !CommonUtils.isEmptyString(ex.getPkEmpEx())){
//				 if(!upMap.containsKey("pkEmpEx") ){
//					 upMap.put("modifier", ex.getNameEmpEx());
//					 upMap.put("modifierTime", new Date());
//					 upMap.put("nameEmpEx", ex.getNameEmpEx());
//				 }
//				 upOrdPks.add(ex.getPkCnord());
//			 }
//		 }
//		 if(upOrdPks.size() > 0){
//			 upMap.put("pkCnords", upOrdPks);
//			 orderExecListMapper.updateOrdEmpNsEx(upMap);
//		 }
	 }
	 
	 /**
	  * 校验用户密码 - 并获取相关操作人员信息进行返回
	  * @param param{usercode,pwd}
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	 public BdOuEmployee verfyPwd(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param,Map.class);
		 if(map == null)
			 throw new BusException("未获取到待验证的用户信息！");
		 if(null == map.get("code") || CommonUtils.isEmptyString(map.get("code").toString()))
			 throw new BusException("未获取到录入的用户编码！");
		 if(null == map.get("pwd") || CommonUtils.isEmptyString(map.get("pwd").toString()))
			 throw new BusException("未获取到录入的密码！");
		 int cntCode = DataBaseHelper.queryForScalar("select count(*) from bd_ou_user where code_user = ? "
				 , Integer.class, new Object[]{map.get("code")});
		 if( cntCode < 1)
			 throw new BusException("用户不存在！");
		 
		 List<Map<String,Object>> pkEmps = DataBaseHelper.queryForList("select pk_emp from bd_ou_user where code_user = ? and pwd = ? "
				 ,new Object[]{map.get("code"),new SimpleHash("md5",map.get("pwd")).toHex()});
		 if(null == pkEmps || pkEmps.size() < 1)
			 throw new BusException("密码错误！");

		 BdOuEmployee emp = DataBaseHelper.queryForBean("select name_emp,pk_emp from bd_ou_employee where pk_emp = ?", BdOuEmployee.class, new Object[]{pkEmps.get(0).get("pkEmp")});
		 if(null == emp)
			 throw new BusException("该用户未维护相关人员信息！");
		 else 
			 return emp;
	 }
	 
	 /**
	  * 检验标本采集查询
	  * @param param
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryLabExlist(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param,Map.class);
		 //未执行时，合并容器
		 if("0".equals(map.get("flagCol"))){
			 
			 List<LabColVo> dtList=new ArrayList<LabColVo>();
			 if(Application.isSqlServer()){
				 dtList = orderExecListMapper.qryLabExlistForDifSsr(map);
			 }else{
				 dtList = orderExecListMapper.qryLabExlistForDif(map);
			 }
			 if(null != dtList && dtList.size() > 0)
				 getDiffLabList(dtList, true);
		 }
		 
		 List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		 if(Application.isSqlServer()){
			 list = orderExecListMapper.qryLabExlistSsr(map);
		 }else{
			 list = orderExecListMapper.qryLabExlist(map);
		 }
		 return list;
	 }
	 
	 /**
	  * 检验标本采集执行
	  * @param param
	  * @param User
	  * @return
	  */
	public void ConfirmLabEx(String param,IUser User){
		 List<LabColVo> dtList = JsonUtil.readValue(param, new TypeReference<List<LabColVo>>(){});
		 if(null == dtList || dtList.size() < 1)
			 throw new BusException("未获取到待执行的相关入参！");
		 
		 User u = (User)User;
		 
		 //1、更新申请单状态=采集，采集人，采集科室，采集时间
		 List<CnLabApply> labList = JsonUtil.readValue(param, new TypeReference<List<CnLabApply>>(){});
		 if(null == labList || labList.size() < 1)
			 throw new BusException("未获取到待执行的相关申请单！");
		 /*for (CnLabApply lab : labList) {
			 DataBaseHelper.updateBeanByPk(lab,false);
		 }*/
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnLabApply.class),labList);
		 //2、合并容器|标本，并记费
		 List<BlPubParamVo> temp = new ArrayList<BlPubParamVo>();
		 List<BlPubParamVo> colCgList = new ArrayList<BlPubParamVo>();
		 //2.1 合并容器，获得其容器List
		 List<LabColVo> contList = getDiffLabList(dtList,true);//合并容器
		 List<String> pkCgList = new ArrayList<>();
		 for(LabColVo lab : contList){
			 pkCgList.add(lab.getDtTubetype());
		 }
		 if(pkCgList.size()>0){
			 if(Application.isSqlServer()){
				 temp = orderExecListMapper.qryLabContCgVoSsr(pkCgList);
			 }else{
				 temp = orderExecListMapper.qryLabContCgVo(pkCgList);
			 }
		 }
		//循环的目的是为了当编码重复时再次过滤出项目
		for (LabColVo risColVo : contList) {
			if(null != temp && temp.size() > 0){
				for (BlPubParamVo bldt : temp) {
					if(risColVo.getDtTubetype().equals(bldt.getCodeDefdoc())){
						bldt = setPatiCgVo(bldt,risColVo,u,"容器收费");
						colCgList.add(bldt);
					}
				}
			}
		}
		 /*for (LabColVo risColVo : contList) {
			 temp = new ArrayList<BlPubParamVo>();
			 if(Application.isSqlServer()){
				 //temp = orderExecListMapper.qryLabContCgVoSsr(risColVo);
			 }else{
				 //temp = orderExecListMapper.qryLabContCgVo(risColVo);
			 }
			 
			 if(null != temp && temp.size() > 0){
				 for (BlPubParamVo bldt : temp) {
					 bldt = setPatiCgVo(bldt,risColVo,u,"容器收费");
					 colCgList.add(bldt);
				 }
			 }
		 }*/
		 //2.2  合并标本，获得其标本List,并查询其对应收费项目
		 List<LabColVo> specList = getDiffLabList(dtList,false);//合并标本
		 List<String> pkSpecList = new ArrayList<>();
		 for(LabColVo lab : specList){
			 pkSpecList.add(lab.getDtSamptype());
		 }
		 List<BlPubParamVo> specCgList = new ArrayList<BlPubParamVo>();
		if(pkCgList.size()>0){
			temp = new ArrayList<BlPubParamVo>();
			if(Application.isSqlServer()){
				temp = orderExecListMapper.qryLabSpecCgVoSsr(pkSpecList);
			}else{
				temp = orderExecListMapper.qryLabSpecCgVo(pkSpecList);
			}
		}
		//循环的目的是为了当编码重复时再次过滤出项目
		for (LabColVo risColVo : specList) {
			if(null != temp && temp.size() > 0){
				for (BlPubParamVo bldt : temp) {
					if(risColVo.getDtSamptype().equals(bldt.getCodeDefdoc())){
						specCgList.add(setPatiCgVo(bldt,risColVo,u,"标本收费"));
					}
				}
			}
		}
		/* for (LabColVo risColVo : specList) {
			 temp = new ArrayList<BlPubParamVo>();
			 if(Application.isSqlServer()){
				 //temp = orderExecListMapper.qryLabSpecCgVoSsr(risColVo);
			 }else{
				 //temp = orderExecListMapper.qryLabSpecCgVo(risColVo);
			 }
			 
			 if(null != temp && temp.size() > 0){
				 for (BlPubParamVo bldt : temp) {
					 specCgList.add(setPatiCgVo(bldt,risColVo,u,"标本收费"));
				 }
			 }
		 }*/
		 //合并收费项目，取最大执行数量
		 specCgList = getDiffItemList(specCgList);//合并标本费用
		 if(specCgList.size() > 0){
			 colCgList.addAll(specCgList);
		 }
		 //2.3 采集标本|容器记费
		 if(colCgList.size() > 0)
			 ipCgPubService.chargeIpBatch(colCgList,false);
		 
		 if("1".equals(ApplicationUtils.getSysparam("EX0033", false)))//EX0033 护士站检验标本采集时是否收费
		 {
			 if(null != dtList && dtList.size() > 0){
				 //3、检验记费,组装数据、走医技执行计费
				 ipRisEx(dtList,u,User);
				 
				 //4、接口发送，组装同步的入参 - pk_cnord [多条]
				 List<String> pkCnords = new ArrayList<String>();
				 for (LabColVo lab : dtList) {
					 pkCnords.add(lab.getPkCnord());
				 }
				 ExtSystemProcessUtils.processExtMethod("LIS", "saveLisApply", pkCnords);
			 }
		 }


		//发送检查检验记费信息至平台pkDeptEx
		Map<String,Object> paramMap = new HashMap<String,Object>();
		String pkCnords="";
		//paramMap.put("dtlist", params);
		for (int i = 0; i < dtList.size(); i++) {

			if(i==dtList.size()-1){
				pkCnords+="'"+dtList.get(i).getPkCnord()+"'";
			}else{
				pkCnords+="'"+dtList.get(i).getPkCnord()+"',";
			}
		}
		List<String> pkCnordsList = new ArrayList<String>();
		 for (LabColVo lab : dtList) {
			 pkCnordsList.add(lab.getPkCnord());
		 }
		paramMap.put("pkCnordS", pkCnords);
		paramMap.put("type", "I");
		paramMap.put("Control", "OK");
		paramMap.put("SDNoSend", "");//深大不发送消息标志
		paramMap.put("pkCnords",pkCnordsList);
		PlatFormSendUtils.sendBlMedApplyMsg(paramMap);		
		paramMap = null;
	 }
	
	public String test(String param,IUser user){

		String sql=" select * from test ";

		List<Map<String,Object>> dtList=DataBaseHelper.queryForList(sql);
		List<String > a=new ArrayList<>(30);

		String pkCnords="";
		//paramMap.put("dtlist", params);
		for (int i = 0; i < dtList.size(); i++) {
			if(i%10==0){
				try {
					Thread.sleep(9000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println("我错了");
				}
			}
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkCnordS", "'"+dtList.get(i).get("pkCnord")+"'");
			paramMap.put("type", "I");
			paramMap.put("Control", "OK");
			paramMap.put("SDNoSend", "");//深大不发送消息标志			
			PlatFormSendUtils.sendBlMedApplyMsg(paramMap);

		}

		return "cao";
	}
	
	/**
	 * 获取合并后的数据
	 * @param dtList 合并前数据
	 * @param isCont 容器|标本
	 * @return
	 */
	private List<LabColVo> getDiffLabList(List<LabColVo> dtList, boolean isCont){
		//EX0061 护士站检验记费时是否启用并管规则
		if("1".equals(ApplicationUtils.getSysparam("EX0061", false)))
		{
			//获取检验并管处理service，对应配置文件lab.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("lab.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				ILabMergeService handler = (ILabMergeService) bean;
				handler.dealLabMergeMethod(dtList, isCont);
			}
		}
		return dtList;
	}

	/**
	 * 获取合并后的收费记录
	 * 合并规则:同一个人，同一个执行时间，同一个收费项目，收费项目数量取最大
	 * @param dtList 合并前数据
	 * @return
	 */
	private List<BlPubParamVo> getDiffItemList(List<BlPubParamVo> dtList){
		List<BlPubParamVo> newDtList = new ArrayList<BlPubParamVo>();
		int cnt = -1;
		for(int i = 0 ; i < dtList.size() ; i++){
			if(newDtList.size() == 0){
				newDtList.add(dtList.get(i));
				continue;
			}
			cnt = -1;
			for(int j = 0 ; j < newDtList.size(); j++){
				if(newDtList.get(j).getPkItem().equals(dtList.get(i).getPkItem())
						&& newDtList.get(j).getPkPv().equals(dtList.get(i).getPkPv())
						&& newDtList.get(j).getDateExpire().equals(dtList.get(i).getDateExpire())){
					cnt = j;
					break;
				}
			}
			if(-1 != cnt){
				if(newDtList.get(cnt).getQuanCg() < dtList.get(i).getQuanCg())
					newDtList.get(cnt).setQuanCg(dtList.get(i).getQuanCg());
			}else
				newDtList.add(dtList.get(i));
		}
		return newDtList;
	}
	
	/**
	 * 校验是否合并容器
	 * 合并规则：同个人，同个校验科室，同个标本，同个容器，同个加急标志，同个执行时间
	 * @param list
	 * @param curVo
	 * @return
	 */
	private boolean isSameCont(List<LabColVo> list,LabColVo curVo){
		for (LabColVo proVo : list) {
			
			if(curVo.getPkPv().equals(proVo.getPkPv())
					&& curVo.getPkDeptOcc().equals(proVo.getPkDeptOcc())
					&& curVo.getDtSamptype().equals(proVo.getDtSamptype())
					&& curVo.getDtTubetype().equals(proVo.getDtTubetype())
					&& curVo.getFlagEmer().equals(proVo.getFlagEmer())
					&& curVo.getDatePlan().equals(proVo.getDatePlan()))
				return true;
		}
		return false;
	}

	/**
	 * 校验是否合并标本
	 * 合并规则：同个人，同个标本，取最大的数
	 * @param list
	 * @param curVo
	 * @return
	 */
	private boolean isSameSpec(List<LabColVo> list,LabColVo curVo){
		for (LabColVo proVo : list) {
			if(curVo.getPkPv().equals(proVo.getPkPv()) 
					&& curVo.getDtSamptype().equals(proVo.getDtSamptype())
					&& curVo.getDatePlan().equals(proVo.getDatePlan()))
				return true;
		}
		return false;
	}
	
	/**
	 * 组装记费VO
	 * @param blPubVo 校验标本VO
	 * @param u 当前操作人
	 * @return
	 */
	private BlPubParamVo setPatiCgVo(BlPubParamVo blPubVo,LabColVo labVo,User u,String spec){
		BlPubParamVo blVo = new BlPubParamVo();
		ApplicationUtils.copyProperties(blVo, blPubVo);
		blVo.setPkPv(labVo.getPkPv());
		blVo.setPkPi(labVo.getPkPi());
		blVo.setPkCnord(labVo.getPkCnord());//设置医嘱主键，用于绑定相关记费项目[无需绑定] 2019-11-05中二需求:检验项目退费时需要将材料费一起退，放开绑定
		blVo.setSpec(spec);//设置规格，注明是标本 | 容器费用
		blVo.setDateExpire(labVo.getDatePlan());//标本合费时借该字段用于缓存计划执行时间
		blVo.setPkOrg(u.getPkOrg());
		blVo.setEuPvType("3");
		blVo.setFlagPd("0");
		blVo.setFlagPv("0");
		blVo.setEuBltype("0");
		blVo.setPkOrgApp(u.getPkOrg());
		blVo.setPkDeptApp(labVo.getPkDeptApp());
		blVo.setPkOrgEx(u.getPkOrg());
		blVo.setPkDeptEx(u.getPkDept());//标本|容器的记费科室为当前执行科室
		blVo.setDateHap(new Date());//费用发生时间
		blVo.setPkDeptCg(u.getPkDept());
		blVo.setPkEmpCg(u.getPkEmp());
		blVo.setNameEmpCg(u.getNameEmp());
		return blVo;
	}
	
	/**
	 * 组装医技执行VO，并记费
	 * @return
	 */
	private void ipRisEx(List<LabColVo> dtList,User u,IUser User){
		List<MedExeIpParam> cgList = new ArrayList<MedExeIpParam>();
		String sql = "select count(1) from ex_order_occ occ where occ.pk_cnord = ?  and occ.pk_exocc = ? and occ.eu_status = 1 and occ.pk_cg is not null";
		List<String> cgTxtList = new ArrayList<String>();
		Integer cnt = null;		
		LabColVo ris = null;
		ExOrderOcc occ = null;
		MedExeIpParam medEx = null;
		for (int i = 0 ; i < dtList.size() ; i++ ) {
			ris = dtList.get(i);
			//校验该执行项目是否已经记费
			cnt = DataBaseHelper.queryForScalar(sql,Integer.class,new Object[]{ris.getPkCnord(),ris.getPkExocc()});
			if(cnt >0) continue;
			occ = new ExOrderOcc();
			medEx = new MedExeIpParam();
			ApplicationUtils.copyProperties(occ, ris);
			ApplicationUtils.copyProperties(medEx, ris);
			medEx.setExOrdOcc(occ);
			medEx.setQuanCg(ris.getQuanOcc());
			medEx.setPkOrdexdt(ris.getPkExocc());
			medEx.setPkOrg(u.getPkOrg());
			medEx.setEuPvType("3");
			medEx.setFlagPd("0");
			medEx.setFlagPv("0");
			//？？？记费人：相关信息待确认如何传值，目前处理为当前操作人，当前科室
			medEx.setPkOrgEx(ris.getPkOrgOcc());
			medEx.setPkOrgApp(u.getPkOrg());
			medEx.setPkDeptEx(ris.getPkDeptOcc());
			medEx.setDateOcc(new Date());
			medEx.setDateHap(ris.getDatePlan());//费用发生时间 - 计划执行时间
			medEx.setPkDeptCg(u.getPkDept());
			medEx.setPkEmpCg(u.getPkEmp());
			medEx.setNameEmpCg(u.getNameEmp());
			medEx.setEuBltype("0");
			if(dtList.size() == 1){
				cgList.add(medEx);
				cgTxtList.add(ApplicationUtils.objectToJson(cgList));
				break;
			}
			if(null == cgList || cgList.size() < 1){
				cgList.add(medEx);
			}
			else{
				if(null !=ris.getFormApp() && ris.getFormApp().equals(dtList.get(i-1).getFormApp())){
					cgList.add(medEx);
				}
				else{
					cgTxtList.add(ApplicationUtils.objectToJson(cgList));
					cgList = new ArrayList<MedExeIpParam>();
					cgList.add(medEx);
				}
			}
			if(i == dtList.size() - 1)
				cgTxtList.add(ApplicationUtils.objectToJson(cgList));
		}
		//根据拼装的同组执行单记费一次
		if(cgTxtList.size() <1) return;
		for (String cgTxt : cgTxtList) {
			ipMedicalExeService.ipExe(cgTxt, User);
		}		
	}
	 
	 /**
	  * 设置试敏医嘱开始时间
	  * @param param
	  * @param user
	  * @return
	  */
	public void setStDateBegin(String param,IUser user){
		 ExStOccVo stvo = JsonUtil.readValue(param, ExStOccVo.class);
		 if(null == stvo)
			 throw new BusException("未获取到待开始的试敏医嘱！");
		 if(CommonUtils.isEmptyString(stvo.getPkStocc())){
			 ExStOcc stOcc = new ExStOcc();
			 ApplicationUtils.copyProperties(stOcc, stvo);
			 DataBaseHelper.insertBean(stOcc);
		 }else{
			 String dateTxt = DateUtils.getDate("yyyyMMddHHmmss");
			 String sql = "update ex_st_occ set date_begin_st = ?, DUARTION = ?,"
					 + " ts = to_date('"+dateTxt+"','YYYYMMDDHH24MISS'), modifier = ? , "
					 + " modity_time =  to_date('"+dateTxt+"','YYYYMMDDHH24MISS') where pk_stocc = ? ";
			 int cnt = DataBaseHelper.execute(sql, new Object[]{stvo.getDateBeginSt(),stvo.getDuartion(),
					 ((User)user).getPkEmp(), stvo.getPkStocc()});
			 if( 1 != cnt)
				 throw new BusException("开始时间设置失败！");
		 }
	 }
	
	
	/**
	 * 根据医嘱写入CA记录
	 * @param cnOrds
	 */
	private void caRecrdByOrd(List<ExlistPubVo> cnOrds) {
		if(cnOrds!=null && cnOrds.size()>0){
			if(cnOrds.get(0).getCnSignCa()!=null){
				 List<CnSignCa> cnSignCa = new ArrayList<CnSignCa>();
				 Map<Integer, String> ordsnM = new HashMap<Integer,String>();
				 for(ExlistPubVo order : cnOrds){
					 CnSignCa signCa = order.getCnSignCa();
					 signCa.setPkSignca(NHISUUID.getKeyId());
					 signCa.setEuButype("0");//医嘱
					 signCa.setPkBu(StringUtils.isBlank(order.getPkCnord()) ? ordsnM.get(order.getOrdsn()) :order.getPkCnord());
					 signCa.setCreator(UserContext.getUser().getPkEmp());
					 signCa.setTs(new Date());
					 signCa.setDelFlag("0");
					 signCa.setCreateTime(signCa.getTs());
					 signCa.setPkOrg(UserContext.getUser().getPkOrg());
					 cnSignCa.add(signCa);
				 }
				 if(cnSignCa.size()>0){
					 DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnSignCa.class), cnSignCa);
				 } 
			 }
		}
	}


    /**
     * 封装选患者级别护理医嘱
     * @param param
     * @param user
     * @return
     */
    public ExlistPubVoRet getAdtForPv(String param, IUser user){
    	//获取选中患者的级别护理医嘱执行单
        List<ExlistPubVo> exlistPubVoList = JsonUtil.readValue(param,new TypeReference<List<ExlistPubVo>>(){});
        //返回参数（包含应取消的执行单，跟继续记费的执行单）
        ExlistPubVoRet epvr = new ExlistPubVoRet();
        //应取消的执行单
        List<ExlistPubVo> delList = new ArrayList<>();
        //继续记费的执行单
        List<ExlistPubVo> addList = new ArrayList<>();
        //查询参数封装
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateBegin = sdf.format(new Date())+"000000";
        String dateEnd = sdf.format(new Date())+"235959";
        //自动取消执行单的患者主键集合
        List<String> pkDelList = new ArrayList<>();
        //继续执行记费的患者主键集合
        List<String> pkAddList = new ArrayList<>();
        //选中患者的就诊主键字符串
		String pkPvS = "";
		if(exlistPubVoList.size() > 0){
            for(ExlistPubVo esPubVo : exlistPubVoList){
                pkPvS += "'"+esPubVo.getPkPv()+"',";
            }
            pkPvS = pkPvS.substring(0,pkPvS.length()-1);
        }else{
            pkPvS = "''";
        }
		map.put("dateBegin",dateBegin);
		map.put("dateEnd",dateEnd);
		map.put("pkPv",pkPvS);
		//判断选中患者是否在当天有转科记录
		List<Map<String,Object>> adtList =  orderExecListMapper.queryAdtByPkpv(map);
		//获取当天存在转科的患者转科前的就诊病区
		String oldAdtDept = "";
		if(adtList.size() > 0){
            for(Map<String,Object> adt:adtList){
                boolean haveKey = adt.containsKey("pkEmpEnd");
                if(haveKey){
                    oldAdtDept +="'"+adt.get("pkDeptNs")+"',";
                }
            }
            oldAdtDept = oldAdtDept.substring(0,oldAdtDept.length()-1);
        }else{
            oldAdtDept = "''";
        }
		map.put("pkDeptOcc",oldAdtDept);
		//查询该患者当天选中患者的级别护理医嘱执行记录
		List<ExOrderOcc> exOrderList = orderExecListMapper.queryOrderOcc(map);
		for(ExlistPubVo esPubVo : exlistPubVoList){
			//根据该执行单的患者主键判断本执行单是否应该继续记费
			boolean addFlag = pkAddList.contains(esPubVo.getPkPv());
			if(!addFlag){
				boolean delFlag = pkDelList.contains(esPubVo.getPkPv());
				//根据该执行单的患者主键判断本执行单是否应该自动取消
				if(!delFlag){
					//当前患者的转科记录
					List<Map<String,Object>> adtByPvList = new ArrayList<>();
					for(Map<String,Object> adt : adtList){
						if(adt.get("pkPv").toString().equals(esPubVo.getPkPv())){
							adtByPvList.add(adt);
						}
					}
					//当前患者的级别护理医嘱执行记录
					List<ExOrderOcc> exOrdByPv = new ArrayList<>();
					for(ExOrderOcc ex : exOrderList){
					    if(ex.getPkPv().equals(esPubVo.getPkPv())){
                            exOrdByPv.add(ex);
                        }
                    }
					if(adtByPvList.size() > 0){
                        Map<String,Object> nowAdt = new HashMap<>();
                        Map<String,Object> oldAdt = new HashMap<>();
						for(Map<String,Object> adt : adtByPvList){
                            boolean haveKey = adt.containsKey("pkEmpEnd");
							if(adt.get("pkPv").toString().equals(esPubVo.getPkPv()) && haveKey){
                                oldAdt = adt;
							}else if(adt.get("pkPv").toString().equals(esPubVo.getPkPv())){
                                nowAdt = adt;
                            }
						}
                        if(nowAdt.size() > 0 && oldAdt.size() > 0 && !"21".equals(nowAdt.get("dtMedicaltype")) && !"21".equals(oldAdt.get("dtMedicaltype"))){//普通科室转普通科室
                            //判断当天在转科前的科室是否有级别护理医嘱执行记录
                            if(exOrdByPv.size() > 0){
                                delList.add(esPubVo);
                                pkDelList.add(esPubVo.getPkPv());
                            }else{
                                pkAddList.add(esPubVo.getPkPv());
                                addList.add(esPubVo);
                            }
                        }else if(nowAdt.size() > 0 && oldAdt.size() > 0 && !"21".equals(nowAdt.get("dtMedicaltype")) && "21".equals(oldAdt.get("dtMedicaltype"))){//ICU科室转普通科室
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date());
                            calendar.set(Calendar.HOUR_OF_DAY, 12);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            Date zero = calendar.getTime();
                            String adtDate = oldAdt.get("dateEnd").toString();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            int i = DateUtil.compare(adtDate,format.format(zero),"yyyy-MM-dd HH:mm:ss");
                            //比较出科时间是否大于当天中午12点，且当日有级别护理医嘱执行记录
                            if(i > 0 && exOrdByPv.size() > 0){
                                pkDelList.add(esPubVo.getPkPv());
                                delList.add(esPubVo);
                            }else if(nowAdt.size() > 0 && oldAdt.size() > 0){//普通科室转ICU科室
                                //正常记费
                                pkAddList.add(esPubVo.getPkPv());
                                addList.add(esPubVo);
                            }
                        }else{
                            pkAddList.add(esPubVo.getPkPv());
                            addList.add(esPubVo);
                        }
					}else{
                        pkAddList.add(esPubVo.getPkPv());
                        addList.add(esPubVo);
					}
				}else{
					delList.add(esPubVo);
				}
			}else{
				addList.add(esPubVo);
			}
		}

//        for(ExlistPubVo esPubVo : exlistPubVoList){
//            map.put("dateBegin",dateBegin);
//            map.put("dateEnd",dateEnd);
//            map.put("pkPv",esPubVo.getPkPv());
//            boolean addFlag = pkAddList.contains(esPubVo.getPkPv());
//            //根据该执行单的患者主键判断本执行单是否应该继续记费
//            if(!addFlag){
//                boolean delFlag = pkDelList.contains(esPubVo.getPkPv());
//				//根据该执行单的患者主键判断本执行单是否应该自动取消
//                if(!delFlag){
//                    //判断该患者是否在当天有转科记录
//                    List<Map<String,Object>> adtList =  orderExecListMapper.queryAdtByPkpv(map);
//                    if(adtList.size() > 1){
//                        Map<String,Object> nowAdt = new HashMap<>();
//                        Map<String,Object> oldAdt = new HashMap<>();
//                        for(Map<String,Object> adt : adtList){
//                            boolean haveKey = adt.containsKey("pkEmpEnd");
//                            if(haveKey){
//                                oldAdt = adt;
//                            }else{
//                                nowAdt = adt;
//                            }
//                        }
//                        //查询患者转科前后的科室
//                        String querySql = "select * from bd_ou_dept where pk_dept = ?";
//                        BdOuDept nowDept = DataBaseHelper.queryForBean(querySql,BdOuDept.class,nowAdt.get("pkDept").toString());
//                        BdOuDept oldDept = DataBaseHelper.queryForBean(querySql,BdOuDept.class,oldAdt.get("pkDept").toString());
//                        //查询患者转科前的护理单元
//						BdOuDept oldDeptNs = DataBaseHelper.queryForBean(querySql,BdOuDept.class,oldAdt.get("pkDeptNs").toString());
//                        //查询该患者当天是否有级别护理医嘱执行记录
//						map.put("pkDeptOcc",oldDeptNs.getPkDept());
//						List<ExOrderOcc> exOrderOccList = orderExecListMapper.queryOrderOcc(map);
//						if(!"21".equals(nowDept.getDtMedicaltype()) && !"21".equals(oldDept.getDtMedicaltype())){//普通科室转普通科室
//							//判断当天在转科前的科室是否有级别护理医嘱执行记录
//                            if(exOrderOccList.size() > 0){
//                                delList.add(esPubVo);
//                                pkDelList.add(esPubVo.getPkPv());
//                            }else{
//                                pkAddList.add(esPubVo.getPkPv());
//                                addList.add(esPubVo);
//                            }
//                        }else if(!"21".equals(nowDept.getDtMedicaltype()) && "21".equals(oldDept.getDtMedicaltype())){//ICU科室转普通科室
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.setTime(new Date());
//                            calendar.set(Calendar.HOUR_OF_DAY, 12);
//                            calendar.set(Calendar.MINUTE, 0);
//                            calendar.set(Calendar.SECOND, 0);
//                            Date zero = calendar.getTime();
//                            String adtDate = oldAdt.get("dateEnd").toString();
//                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            int i = DateUtil.compare(adtDate,format.format(zero),"yyyy-MM-dd HH:mm:ss");
//                            //比较出科时间是否大于当天中午12点，且当日有级别护理医嘱执行记录
//                            if(i > 0 && exOrderOccList.size() > 0){
//                                pkDelList.add(esPubVo.getPkPv());
//                                delList.add(esPubVo);
//                            }else{//普通科室转ICU科室
//                            	//正常记费
//                                pkAddList.add(esPubVo.getPkPv());
//                                addList.add(esPubVo);
//                            }
//                        }else{
//                            pkAddList.add(esPubVo.getPkPv());
//                            addList.add(esPubVo);
//                        }
//                    }else{
//                        pkAddList.add(esPubVo.getPkPv());
//                        addList.add(esPubVo);
//                    }
//                }else{
//                    delList.add(esPubVo);
//                }
//            }else{
//				addList.add(esPubVo);
//			}
//        }
        epvr.setAddList(addList);
        epvr.setDelList(delList);
        return epvr;
    }
    
    
    /**
	  * 查询代理医嘱执行单列表
	  * @param param{dateBegin,dateEnd,euStatus,pkPvs,pkDeptNs,pkDeptEx}
	  * @param user
	  * @return
	  */
	@SuppressWarnings("unchecked")
	public List<ExlistPubVo> queryAgentExlist(String param,IUser user){
		 Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		 
		 String  dateBegin = CommonUtils.getString(map.get("dateBegin"));
		 if(dateBegin == null){
			 map.put("dateBegin", DateUtils.getDateStr(new Date())+"000000");
		 }else{
			 map.put("dateBegin", dateBegin.substring(0, 8)+"000000");
		 }
		 
		 String  dateEnd = CommonUtils.getString(map.get("dateEnd"));
		 if(dateEnd == null){
			 map.put("dateEnd", DateUtils.getDateStr(new Date())+"235959");
		 }else{
			 map.put("dateEnd", dateEnd.substring(0, 8)+"235959");
		 }
		 
		 List<ExlistPubVo> result = new ArrayList<ExlistPubVo>();
		 result =  orderExecListMapper.queryAgentExecList(map);
		 new ExListSortByOrdUtil().ordGroup(result);
		 return result;
	 }

	
 }
