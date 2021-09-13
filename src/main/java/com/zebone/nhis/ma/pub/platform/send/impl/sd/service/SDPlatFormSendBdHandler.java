package com.zebone.nhis.ma.pub.platform.send.impl.sd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ca.uhn.hl7v2.HL7Exception;

import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendBd;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.sd.dao.SDBdSendMapper;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 * 发送BD领域消息
 * @author IBM
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDPlatFormSendBdHandler {
	
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	/**
	 * 添加状态
	 **/
	public static final String AddState = "_ADD";

	/**
	 * 更新状态
	 */
	public static final String UpdateState = "_UPDATE";

	/**
	 * 删除状态
	 */
	public static final String DelState = "_DELETE";
	
	@Resource
	private SDMsgSendBd sDMsgSendBd;
	@Resource
	private SDBdSendMapper sDBdSendMapper;
	
	/**
	 * 发送挂号级别信息字典（深大项目）
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public void sendRegLevelMsg(Map<String,Object> paramMap) throws HL7Exception{
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		listMap.add(paramMap);
		sDMsgSendBd.sendMfnMsg("RegLevel", listMap);
	}
	
	/**
	 * 发送合同单位信息（医保信息）（深大项目）
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public void sendPactMsg(Map<String, Object> paramMap) throws HL7Exception {
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		listMap.add(paramMap);
		sDMsgSendBd.sendMfnMsg("Pact", listMap);
	}
	
	/**
	 * 发送公共字典
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	
   public void sendBdDefDocMsg(Map<String,Object> paramMap) throws HL7Exception{
		//发送消息
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			String codeEmp = paramMap.get("codeEmp")==null?"":paramMap.get("codeEmp").toString();
			//获取公共字典集合信息
			List<Map<String,Object>> tempList = (List<Map<String,Object>>)paramMap.get("bdDefdoc");
			if(tempList!=null && tempList.size()>0){
				//获取集合信息判断信息是'添加'还是'修改'
				for(Map<String,Object> tmpMap:tempList){
					String pkDefdoc = tmpMap.get("pkDefdoc")==null?"":tmpMap.get("pkDefdoc").toString();
					String isUpdate = tmpMap.get("isUpdate")==null?"":tmpMap.get("isUpdate").toString();
					if((pkDefdoc==null||pkDefdoc.equals(""))){
						tmpMap.put("empCode", codeEmp);
						tmpMap.put("state", AddState);	//对添加的信息增加'添加'状态
						listMap.add(tmpMap);
					} else if(pkDefdoc!=null && isUpdate.equals("1")){//如果前台对数据做了修改则IsUpdate为'1'
						tmpMap.put("empCode", codeEmp);
						tmpMap.put("state", UpdateState);	//对修改的信息添加'修改'状态
						listMap.add(tmpMap);
					}
				}
			}
			
			//获取删除的集合信息
			List<String> pkDefdocs = (List<String>)paramMap.get("delPkDefdocs");
			if(pkDefdocs!=null && pkDefdocs.size()>0){
				//拼接sql语句
				StringBuffer sbfSql = new StringBuffer();
				sbfSql.append("select * from BD_DEFDOC where pk_defdoc in ( ");
				for(int i=0; i<pkDefdocs.size(); i++){
					if(i==pkDefdocs.size()-1)
						sbfSql.append("'"+pkDefdocs.get(i)+"'");
					else
						sbfSql.append("'"+pkDefdocs.get(i)+"'"+",");
				}
				sbfSql.append(" )");
				//List<BdDefdoc> bdDefdocItem = DataBaseHelper.queryForList(sbfSql.toString(), BdDefdoc.class);
				List<Map<String,Object>> bdDefdocItem = DataBaseHelper.queryForList(sbfSql.toString());
				if(bdDefdocItem!=null && bdDefdocItem.size()>0){
					for(Map<String,Object> bdDefdoc : bdDefdocItem){
						//删除的信息
						bdDefdoc.put("empCode", codeEmp);
						bdDefdoc.put("state", DelState);
						listMap.add(bdDefdoc);
					}
				}
			}
			/**判断有没有做新增/修改/删除操作,只有当数据发生改变后才发送消息**/
			if(listMap!=null && listMap.size()>0){
					sDMsgSendBd.sendMfnMsg("BdDefdocType", listMap);
			}
			//发送公共字典消息
			//sDMsgSendBd.sendMfnMsg("BdDefdocType", listMap);
   }
   
   /**
	 * 发送诊断消息
	 * @param param
 * @throws HL7Exception 
	 */
	
	public void sendBdTermDiagMsg(Map<String,Object> paramMap) throws HL7Exception{
				String rleCode = paramMap.get("STATUS").toString();
				String codeEmp = paramMap.get("codeEmp").toString();
				if(rleCode.equals(AddState))
					rleCode = "MAD";
				if(rleCode.equals(UpdateState))
					rleCode = "MUP";
				if(rleCode.equals(DelState))
					rleCode = "MDL";
				List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
				if(rleCode.equals("MAD")||rleCode.equals("MUP")){
					//获取前台传入的ICD10/手术编码字典信息
					paramMap.put("empCode", codeEmp);
					paramMap.put("rleCode", rleCode);
					listMap.add(paramMap);
				}else if(rleCode.equals("MDL")){
					//根据诊断编码主键获取将要删除标准诊断编码字典信息
					Map<String,Object> param = DataBaseHelper.queryForMap("select * from bd_term_diag where pk_diag = ?", new Object[] { ((BdTermDiag)paramMap.get("diag")).getPkDiag()});
					param.put("empCode", codeEmp);
					param.put("rleCode", rleCode);
					listMap.add(param);
				}
				sDMsgSendBd.sendMfnMsg("ICD10", listMap);
				sDMsgSendBd.sendMfnMsg("Operation", listMap);
		
	}
	/**
	 * 频次字典
	 * @param paramMap
	 * @throws Exception
	 */
	
	public void sendBdTermFreqMsg(Map<String,Object> paramMap) throws Exception{
		String rleCode = paramMap.get("STATUS").toString();
		//String codeEmp = paramMap.get("codeEmp").toString();
		if(rleCode.equals(AddState))
			rleCode = "MAD";
		if(rleCode.equals(UpdateState))
			rleCode = "MUP";
		if(rleCode.equals(DelState))
			rleCode = "MDL";
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		if(rleCode.equals("MAD")||rleCode.equals("MUP")){
			//获取前台传入的频次字典信息
			if(null != paramMap.get("freq")){
				paramMap.putAll(SDMsgUtils.objectToMap(paramMap.get("freq")));
			}	
			paramMap.put("rleCode", rleCode);
			listMap.add(paramMap);
		}else if(rleCode.equals("MDL")){
			//根据频次主键获取将要删除的频次字典信息
			Map<String,Object> param = DataBaseHelper.queryForMap("select * from bd_term_freq where pk_freq = ?", new Object[] { ((BdTermFreq)paramMap.get("freq")).getPkFreq()});
			param.put("rleCode", rleCode);
			listMap.add(param);
		}
		sDMsgSendBd.sendMfnMsg("yz_frequency", listMap);
	}
	/**
	 * 医嘱用法字典//医嘱用法
	 * @param paramMap
	 * @throws Exception
	 */
	
	public void sendBdSupplyMsg(Map<String,Object> paramMap) throws Exception{
		String rleCode = paramMap.get("STATUS").toString();
		//String codeEmp = paramMap.get("codeEmp").toString();
		if(rleCode.equals(AddState))
			rleCode = "MAD";
		if(rleCode.equals(UpdateState))
			rleCode = "MUP";
		if(rleCode.equals(DelState))
			rleCode = "MDL";
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		if(rleCode.equals("MAD")||rleCode.equals("MUP")){
			if(null != paramMap.get("supply")){
				paramMap.putAll(SDMsgUtils.objectToMap(paramMap.get("supply")));
			}
			paramMap.put("rleCode", rleCode);
			listMap.add(paramMap);
		}else if(rleCode.equals("MDL")){
			//根据主键获取医嘱用法信息
			Map<String,Object> param = DataBaseHelper.queryForMap("select * from bd_supply where pk_supply = ?", new Object[] { ((BdSupply)paramMap.get("supply")).getPkSupply() });
			param.put("rleCode", rleCode);
			listMap.add(param);
		}
		sDMsgSendBd.sendMfnMsg("yz_supply", listMap);
	}
	/**
	 * 床位字典
	 * @param paramMap
	 * @throws Exception
	 */
	
	public void sendBdResBedMsg(Map<String,Object> paramMap) throws Exception{
		//发送消息
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		
		String codeEmp = paramMap.get("codeEmp")==null?"":paramMap.get("codeEmp").toString();//获取当前操作人关联人员的编码
		Object codeObject = codeEmp;
		
		List<Map<String,Object>> resbedList = (List<Map<String,Object>>)paramMap.get("resbedList");
		Map<String, Object> tmpMap = new HashMap<>();
		
		if(resbedList!=null && resbedList.size()>0){
			//获取集合信息判断信息是'添加'还是'修改'
			for (int i = 0; i < resbedList.size(); i++) {
				tmpMap = SDMsgUtils.objectToMap(resbedList.get(i));
				String pkBed = tmpMap.get("pkBed")==null?"":tmpMap.get("pkBed").toString();
				//String isUpdate = tmpMap.get("isUpdate")==null?"":tmpMap.get("isUpdate").toString();
				if((pkBed==null||pkBed.equals(""))){
					tmpMap.put("empCode", codeObject);
					tmpMap.put("state", AddState);	//对添加的信息增加'添加'状态
					listMap.add(tmpMap);
				} else{ 	//if(pkBed!=null && isUpdate.equals("1")){//如果前台对数据做了修改则IsUpdate为'1'
					tmpMap.put("empCode", codeObject);
					tmpMap.put("state", UpdateState);	//对修改的信息添加'修改'状态
					listMap.add(tmpMap);
				}
				tmpMap = null;
			}
		}
			
		if(paramMap.get("delBed")!=null) {	
			Map<String,Object> delBed = (Map<String,Object>)paramMap.get("delBed");
			delBed.put("empCode", codeEmp);
			delBed.put("state", DelState);	//对添加的信息增加'添加'状态
			listMap.add(delBed);
		}
		/**判断有没有做新增/修改/删除操作,只有当数据发生改变后才发送消息**/
		if(listMap!=null && listMap.size()>0){
				sDMsgSendBd.sendMfnMsg("Bed", listMap);
		}				
			
     }
	/**
	 * 医嘱字典（Z04组套）     
	 * 原来是ZA1消息：sDMsgSendBd.sendMfnMsg("yz_order_item", listMap);
	 * 现在未Z04 通过字段判断是否组套：通过单位标识（z04-16） 区分组套和收费项目
	 * @param paramMap （pkOrd）
	 * @throws Exception
	 */	
	public void sendBdOrdMsg(Map<String,Object> paramMap) throws Exception{
		//查询map
		Map<String, Object> queryMap = new HashMap<String,Object>();
		//操作类型
		String rleCode = SDMsgUtils.getPropValueStr(paramMap, "STATUS");
		if(rleCode.equals(AddState)){
			rleCode = "MAD";
			queryMap = (Map<String, Object>)paramMap.get("bdOrd");
		}
		if(rleCode.equals(UpdateState)){
			rleCode = "MUP";
			queryMap = (Map<String, Object>)paramMap.get("bdOrd");
		}
		if(rleCode.equals(DelState)){
			rleCode = "MDL";
			queryMap = paramMap;
		}
		//查询医嘱项目数据
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		Map<String, Object> queryBdOrd = sDBdSendMapper.queryBdOrd(queryMap);
		queryBdOrd.put("state", SDMsgUtils.getPropValueStr(paramMap, "STATUS"));
		queryBdOrd.put("rleCode", rleCode);
		queryBdOrd.put("group", "");//组套标志
		listMap.add(queryBdOrd);
		sDMsgSendBd.sendMfnMsg("Undrug", listMap);
	}
	/**
	 * 发送收费项目信息(Z04) 非组套
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public void sendBdItemMsg(Map<String,Object> paramMap) throws HL7Exception{
		List<Map<String, Object>> sendList = new ArrayList<>();
		Map<String, Object> param = (Map<String, Object>)paramMap.get("item");
		//查询总价格
		List<Map<String, Object>> queryBdItemPrice = sDBdSendMapper.queryBdItemPrice(param);
		for(Map<String, Object> price : queryBdItemPrice){
			param.putAll(price);
			sendList.add(param);
			sDMsgSendBd.sendMfnMsg("Undrug", sendList);
		}
	}
	
	
	/**
	 * 发送收费项目信息（包含组套）
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public void sendBdItemSetMsg(Map<String,Object> paramMap) throws HL7Exception{
		List<Map<String, Object>> sendList = new ArrayList<>();
			sendList.addAll((List<Map<String, Object>>)paramMap.get("itemSets"));
			sDMsgSendBd.sendMfnMsg("UndrugGroup", sendList);
	}
	/**
	 * 发送科室信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	
	public void sendBdOuDeptMsg(Map<String,Object> paramMap) throws Exception{
		if(paramMap.get("dept")==null)
			return;
        //发送消息
		Map<String,Object> dept = SDMsgUtils.objectToMap(paramMap.get("dept"));
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		String rleCode = paramMap.get("STATUS").toString();
		if(rleCode.equals(AddState))
			rleCode = "MAD";
		if(rleCode.equals(UpdateState))
			rleCode = "MUP";
		if(rleCode.equals(DelState))
			rleCode = "MDL";
		dept.put("rleCode", rleCode);
		dept.put("empCode", paramMap.get("codeEmp"));
		listMap.add(dept);
		//发送科室信息
		sDMsgSendBd.sendMfnMsg("Department", listMap);
		//发送科室病区关系信息
		//sDMsgSendBd.sendMfnMsg("DeptStat", listMap);
		/*//Map<String,Object> fatherInfo = DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = ?", MsgUtils.getPropValueStr(dept,"pkFather"));
		String sqlString = "SELECT D.code_dept AS code,D.NAME_DEPT AS name FROM (((bd_dept_bus A INNER JOIN bd_dept_bus b ON A.PK_DEPTBU = b.PK_DEPTBU)" +
		        "INNER JOIN BD_OU_DEPT c ON c.PK_DEPT = A.PK_DEPT) INNER JOIN BD_OU_DEPT D ON D.PK_DEPT = b.PK_DEPT)" +
		        "WHERE A.DT_DEPTTYPE = '02' AND A.DEL_FLAG = '0' AND b.DT_DEPTTYPE = '01' AND b.DEL_FLAG = '0'" +
		        "AND A.PK_ORG = ? AND b.PK_ORG = ? AND A.pk_dept = ?";
				
				String org = ((User)UserContext.getUser()).getPkOrg();
				String PkDept = MsgUtils.getPropValueStr(listMap.get(0),"pkDept");
				List<Map<String,Object>> fatherIn = DataBaseHelper.queryForList(sqlString,org ,org,PkDept);
				listMap.clear();
				if(0 != fatherIn.size()){
					for (int i = 0; i < fatherIn.size(); i++) {
						Map<String,Object> deprcodr = new HashMap<>();
						deprcodr.putAll(dept);
						deprcodr.putAll(fatherIn.get(i));
						listMap.add(deprcodr);
						deprcodr = null;
					}
				}
				//发送科室病区关系信息
				sDMsgSendBd.sendMfnMsg("DeptStat", listMap);
				listMap=null;*/
	}
	
	/**
	 * 科室与病区关系
	 * @param paramMap
	 * @throws Exception
	 */
	
	public void sendRelationshipMsg(Map<String,Object> paramMap) throws Exception{
		if(paramMap==null || paramMap.size()<=0){
			loger.info("科室与病区关系字典；未获取到前台数据！");
			throw new BusException("科室与病区关系字典；未获取到前台数据！");
		}
		//业务线数据
		Map<String,Object> deptbuMap = (Map<String, Object>) paramMap.get("deptbu");
		//部门关系集合
		//List<Map<String,Object>> deptbusList = (List<Map<String, Object>>) paramMap.get("deptbusList");
		if(deptbuMap==null || deptbuMap.size()<=0 || !deptbuMap.containsKey("pkDeptbu")){
			loger.info("科室与病区关系字典；未获取到前台数据！");
			throw new BusException("科室与病区关系字典；未获取到前台数据！");
		}
		//操作标志
		deptbuMap.put("control", SDMsgUtils.getPropValueStr(paramMap, "control"));
		//消息数据集合 //查询所需数据
		List<Map<String,Object>> paramList = sDBdSendMapper.queryBdDeptBus(deptbuMap);
		//发送新增
		sDMsgSendBd.sendMfnMsg("DeptStat", paramList);
		
		
	}
	/**
	 * 发送人员信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	
	public void sendEmpMsg(Map<String,Object> paramMap) throws HL7Exception{
			String rleCode = paramMap.get("STATUS").toString();
			if(rleCode.equals(AddState))
				rleCode = "MAD";
			if(rleCode.equals(UpdateState))
				rleCode = "MUP";
			if(rleCode.equals(DelState))
				rleCode = "MDL";
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			
			if(rleCode.equals("MAD")||rleCode.equals("MUP")){
				List<Map<String,Object>> empJobs = (List<Map<String,Object>>)paramMap.get("empJobs");
				for (int i = 0; i < empJobs.size(); i++) {
					Map<String,Object> empMap = new HashMap<String,Object>();
					empMap.putAll((Map<String,Object>)paramMap.get("emp"));
				    paramMap.putAll((empJobs.get(i)));
					//操作员关联人员的编码
					empMap.put("empCode", paramMap.get("codeEmp"));
					empMap.put("rleCode", rleCode);
					//查询科室编码+科室名称
					Map<String,Object> deptMap = DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = ?", paramMap.get("pkDept"));
					String deptInfo = deptMap.get("codeDept")+"^"+deptMap.get("nameDept");
					empMap.put("deptInfo", deptInfo);
					empMap.put("CODEDEPTS", deptMap.get("codeDept"));
					listMap.add(empMap);
					sDMsgSendBd.sendMfnMsg("Employee", listMap);
					listMap.clear();
					deptInfo = null;
					empMap = null;
				}
				
			}else if(rleCode.equals("MDL")){
				String pkEmp = ((Map<String,Object>)paramMap.get("emp")).get("pkEmp").toString();
				
				//获取人员所属关系信息
				List<Map<String,Object>> empJobs = DataBaseHelper.queryForList("select * from BD_OU_EMPJOB where PK_EMP=?", pkEmp);
				for (int i = 0; i < empJobs.size(); i++) {
					Map<String,Object> empMap = new HashMap<String,Object>();
					empMap = DataBaseHelper.queryForMap("select * from bd_ou_employee where PK_EMP = ?", pkEmp);
					paramMap.putAll((empJobs.get(0)));
					//查询科室编码+科室名称
					Map<String,Object> deptMap = DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = ?",paramMap.get("pkDept"));
					String deptInfo = deptMap.get("codeDept")+"^"+deptMap.get("nameDept");
					empMap.put("deptInfo", deptInfo);
					empMap.put("CODEDEPTS", deptMap.get("codeDept"));
					//获取当前机构主键
					empMap.put("nowPkOrg",paramMap.get("pkOrg"));
					empMap.put("rleCode", rleCode);
					listMap.add(empMap);
					sDMsgSendBd.sendMfnMsg("Employee", listMap);
					listMap.clear();
					empMap.clear();
					deptInfo = null;
				}
			}
	}
	/**
	 * 发送用户信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	
	public void sendUserMsg(Map<String,Object> paramMap) throws HL7Exception{
        	List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        	String rleCode = paramMap.get("STATUS").toString();
 			if(rleCode.equals(AddState))
 				rleCode = "MAD";
 			if(rleCode.equals(UpdateState))
 				rleCode = "MUP";
 			if(rleCode.equals(DelState))
 				rleCode = "MDL";
 			List<Map<String, Object>> codeLisMap = DataBaseHelper.queryForList("SELECT CODE_DEPT FROM bd_ou_dept bd LEFT JOIN bd_ou_empjob empjob ON empjob.pk_dept = bd.pk_dept WHERE empjob.Pk_emp = ?", SDMsgUtils.getPropValueStr(paramMap,"pkEmp"));
 			for (int i = 0; i < codeLisMap.size(); i++) {
 				Map<String, Object> paramMapMap = new HashMap<>();
 				
 				if(rleCode.equals("MAD") || rleCode.equals("MUP")) {
 				    if(paramMap.get("pd")!=null){   	
 				    	if(null !=paramMap.get("pd") && (paramMap.get("pd")).equals("")){
 				    		 paramMap.putAll((Map<String, Object>)paramMap.get("pd"));
 				    	}
 				    }else if(paramMap.get("user")!=null){
 				    	paramMap.putAll((Map<String, Object>)paramMap.get("user"));
 				    	if(null !=paramMap.get("pd") && (paramMap.get("pd")).equals("")){
 				    		 paramMap.putAll((Map<String, Object>)paramMap.get("pd"));
 				    	}
 				    }
 				     //获取当前机构主键
 				     paramMap.put("nowPkOrg", UserContext.getUser().getPkOrg());
 				     paramMap.put("rleCode", rleCode);
 				     paramMapMap.putAll(paramMap);
 				     paramMapMap.put("codeDept", codeLisMap.get(i).get("codeDept"));
 				     listMap.add(paramMapMap);
 				}else if (rleCode.equals("MDL")) {
 				     String pkUser = ((Map<String, Object>)paramMap.get("user")).get("pkUser").toString();
 				     //查询将要删除的用户信息作为发消息时使用
 				     paramMap = DataBaseHelper.queryForMap("select * from bd_ou_user where pk_user = ?", pkUser);
 				     //获取当前机构主键
 				     paramMap.put("nowPkOrg", UserContext.getUser().getPkOrg());
 				     paramMap.put("rleCode", rleCode);
 				     paramMapMap.putAll(paramMap);
				     paramMapMap.put("codeDept", codeLisMap.get(i).get("codeDept"));
				     listMap.add(paramMapMap);
 				}
 				paramMapMap = null;
			}
             sDMsgSendBd.sendMfnMsg("UserInfo", listMap);
	}
	
	 /**
	 * 发送药品信息
	 * @param paramMap
	 * @throws Exception 
	 */
	
	public void sendBdPdMsg(Map<String,Object> paramMap) throws Exception{
		if(paramMap==null||paramMap.get("pd")==null)
			return;
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
    	String rleCode = paramMap.get("STATUS").toString();
			if(rleCode.equals(AddState))
				rleCode = "MAD";
			if(rleCode.equals(UpdateState))
				rleCode = "MUP";
			if(rleCode.equals(DelState))
				rleCode = "MDL";
		if(rleCode.equals("MAD") || rleCode.equals("MUP")) {
			//System.out.println(((BdPd)paramMap.get("pd")).getPkPd());
		     //paramMap.putAll(objectToMap((BdPd)paramMap.get("pd")));
		     //获取当前机构主键
		     paramMap.put("opeType", rleCode);
		     listMap.add(paramMap);
		}else if (rleCode.equals("MDL")) {
		    // paramMap.put("pkPd", ((BdPd)paramMap.get("pd")).getPkPd());	
		     paramMap.put("opeType", rleCode);
		     listMap.add(paramMap);
		
		}
	    sDMsgSendBd.sendMfnMsg("Drug", listMap);//发送药品信息
	
	}
}
