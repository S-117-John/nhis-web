package com.zebone.nhis.labor.nis.service;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExStOcc;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.vo.ExStOccVo;
import com.zebone.nhis.ex.nis.pub.service.NsPubService;
import com.zebone.nhis.ex.pub.service.BlCgExPubService;
import com.zebone.nhis.ex.pub.support.ExListSortByOrdUtil;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.labor.nis.dao.PvLaborExListMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 产房医嘱执行确认
 * @author yangxue
 *
 */
@Service
public class PvLaborExListConfirmService {
     
	 @Resource
	 private PvLaborExListMapper pvLaborExListMapper;
	 @Resource
	 private BlCgExPubService blCgExPubService;
	 @Resource
	 private NsPubService nsPubService;
	 /**
	  * 查询医嘱执行单列表
	  * @param param{dateBegin,dateEnd,confirmFlag,ordtype,euStatus,pkPvs,pkDeptNs}
	  * @param user
	  * @return
	  */
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
		 List<ExlistPubVo> result =  pvLaborExListMapper.queryExecListByCon(map);
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
	  * 执行确认
	  * @param param{List<ExlistPubVo>}
	  * @param user
	  */
	 public void confirmEx(String param,IUser user){
		 List<ExlistPubVo> exList = JsonUtil.readValue(param,new TypeReference<List<ExlistPubVo>>(){});
		 if(exList == null) return;
		 //默认将药品医嘱设置为基数药，确认时记费
		 for(ExlistPubVo vo:exList){
			 if("1".equals(vo.getFlagDurg())){
				 vo.setFlagBase("1");
				 vo.setFlagLabor(true);
			 }
		 }
		 //如果是计费医嘱，调用计费接口
		 User u = (User)user;
		 //调用公共执行计费服务
		 blCgExPubService.execAndCg(exList, u);
	 }
	 
	 /**
	  * 保存试敏结果
	  * @param param
	  * @param user
	  */
	 public void saveStResult(String param,IUser user){
		 ExStOccVo stvo = JsonUtil.readValue(param, ExStOccVo.class);		 		 
		 if(stvo!=null){
			 ExStOcc vo = new ExStOcc();
			 ApplicationUtils.copyProperties(vo, stvo);
			 if(CommonUtils.isEmptyString(vo.getPkStocc()))
				 DataBaseHelper.insertBean(vo);
			 else
				 DataBaseHelper.updateBeanByPk(vo,false);
		 }		 		 		 
		 String pk_cnord = stvo.getPkCnord();
		 ExlistPubVo paramVO = new ExlistPubVo();
		
		//先根据 pk_cnord查询出对应的医嘱
		 CnOrder ordVO=(CnOrder) DataBaseHelper.queryForBean("select * from cn_order where pk_cnord = ? ", CnOrder.class,pk_cnord);
		 List<ExlistPubVo> exListMaps=new ArrayList<ExlistPubVo>();
		 paramVO.setIsskt("Y");
		 paramVO.setPkCnord(pk_cnord);
		 paramVO.setPkExocc(stvo.getPkExocc());
		 paramVO.setFlagSt("st");
		 paramVO.setPackSize(stvo.getPackSize());
		 paramVO.setCodeSupply(ordVO.getCodeSupply());
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
		 User u = (User)user;
		 paramVO.setPkDeptNs(u.getPkDept());
		 paramVO.setPkDeptExec(u.getPkDept());
		 paramVO.setQuanOcc(stvo.getQuanOcc());
		 paramVO.setPkPi(ordVO.getPkPi());
		 paramVO.setPkUnit(stvo.getPkUnit());
		 paramVO.setPkOrgOcc(stvo.getPkOrgOcc());
		 paramVO.setInfantNo(ordVO.getInfantNo()+"");
	     exListMaps.add(paramVO);
	     //更新执行单并计费 ---记皮试附加费
	     //blCgExPubService.execAndCg(exListMaps, u);
	     //利用基数药的逻辑记药品费用
	     ExlistPubVo newParamVO = new ExlistPubVo();
	     ApplicationUtils.copyProperties(newParamVO, paramVO);
	     newParamVO.setFlagBase("1");
	     newParamVO.setFlagSt(null);
	     newParamVO.setIsskt(null);
	     newParamVO.setCodeSupply("");
	     newParamVO.setPkDeptExec(ordVO.getPkDeptExec());
	     newParamVO.setFlagDurg(ordVO.getFlagDurg());
	     //exListMaps.clear();
	     //exListMaps.add(newParamVO);
	     blCgExPubService.execAndCg(exListMaps, u);
		 //保存皮试结果变更日志
		 nsPubService.saveSysApplogInfo(stvo,u,ordVO.getEuSt());
		//将皮试结果写入医嘱表cn_order的name_ord
		//String sql="update cn_order  set name_ord='"+ordVO.getNameOrd()+"("+stvo.getResult()+")'  where pk_cnord='"+pk_cnord+"'";
		//DataBaseHelper.update(sql, new Object());
	     setPiAllergic(stvo,(User)user);
	 }

	 /**
	  * 保存或更新试敏结果时，更新过敏史及医嘱名称
	  * @param stvo
	  * @param user
	  */
	private void setPiAllergic(ExStOccVo stvo,User user){
		//如果是阳性，则插入患者过敏史
		 if(!"-".equals(stvo.getResult())){
			 StringBuilder sql = new StringBuilder(" select pd.name,pi.dt_pharm,pd.dt_pharm as ph from cn_order ord   ");
			 sql.append(" inner join bd_pd pd on pd.pk_pd=ord.pk_ord and ord.flag_durg='1'  ");
			 sql.append(" left join pi_allergic pi on pi.pk_pi = ord.pk_pi and  pd.dt_pharm = pi.dt_pharm ");
			 sql.append(" where ord.pk_cnord = ? ");
			 List<Map<String,Object>> list = DataBaseHelper.queryForList(sql.toString(),new Object[]{stvo.getPkCnord()});
		     if(list !=null&&list.size()>0){
		    	 String dt_pharm = CommonUtils.getString(list.get(0).get("dtPharm"));
		    	 if(CommonUtils.isEmptyString(dt_pharm)){//插入过敏史
		    		 PiAllergic al = new PiAllergic();
			    	 al.setPkPi(stvo.getPkPi());
			    	 al.setEuMcsrc("0");
			    	 al.setDateFind(new Date());
			    	 al.setNameEmpRec(user.getNameEmp());
			    	 al.setPkEmpRec(user.getPkEmp());
			    	 al.setDateRec(new Date());
			    	// al.setDtPharm(CommonUtils.getString(list.get(0).get("ph")));
			    	 al.setNameAl(CommonUtils.getString(list.get(0).get("name")));
			    	 al.setDelFlag("0");
			    	 DataBaseHelper.insertBean(al);
		    	 }
		     }
		 }
		 String eu_st = "-".equals(stvo.getResult())?"2":"3";
		//将皮试结果写入医嘱表cn_order的name_ord
	 	String sql_t="update cn_order  set eu_st='"+eu_st+"'  where pk_cnord='"+stvo.getPkCnord()+"'";
	 	DataBaseHelper.update(sql_t, new Object());
	 	//更新关联的医嘱列表标志
	 	if(stvo.getOrdList()!=null&&stvo.getOrdList().size()>0){
	 		Map<String,Object> paramMap = new HashMap<String,Object>();
	 		paramMap.put("ordList", stvo.getOrdList());
	 		paramMap.put("result", stvo.getResult());
	 		paramMap.put("euSt", eu_st);
	 		pvLaborExListMapper.updateStOrd(paramMap);
	 	}
	}


}
