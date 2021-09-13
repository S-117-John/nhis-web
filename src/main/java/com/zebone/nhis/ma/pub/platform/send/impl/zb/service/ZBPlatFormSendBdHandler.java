package com.zebone.nhis.ma.pub.platform.send.impl.zb.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ca.uhn.hl7v2.HL7Exception;
import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendBd;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 发送BD领域消息
 * @author IBM
 *
 */
@Service
public class ZBPlatFormSendBdHandler {
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
	private MsgSendBd msgSendBd;
	
	/**
	 * 发送公共字典
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
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
					msgSendBd.sendMfnMsg("BdDefdocType", listMap);
			}
   }
   
   /**
	 * 发送诊断消息
	 * @param param
 * @throws HL7Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
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
				msgSendBd.sendMfnMsg("ICD10", listMap);
				msgSendBd.sendMfnMsg("Operation", listMap);
		
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendBdTermFreqMsg(Map<String,Object> paramMap) throws Exception{
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
					//获取前台传入的频次字典信息
					if(null != paramMap.get("freq")){
						paramMap.putAll(objectToMap(paramMap.get("freq")));
					}	
					paramMap.put("rleCode", rleCode);
					listMap.add(paramMap);
				}else if(rleCode.equals("MDL")){
					//根据频次主键获取将要删除的频次字典信息
					Map<String,Object> param = DataBaseHelper.queryForMap("select * from bd_term_freq where pk_freq = ?", new Object[] { ((BdTermFreq)paramMap.get("freq")).getPkFreq()});
					param.put("rleCode", rleCode);
					listMap.add(param);
				}
				msgSendBd.sendMfnMsg("yz_frequency", listMap);
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendBdSupplyMsg(Map<String,Object> paramMap) throws Exception{
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
					if(null != paramMap.get("supply")){
						paramMap.putAll(objectToMap(paramMap.get("supply")));
					}
					paramMap.put("rleCode", rleCode);
					listMap.add(paramMap);
				}else if(rleCode.equals("MDL")){
					//根据主键获取医嘱用法信息
					Map<String,Object> param = DataBaseHelper.queryForMap("select * from bd_supply where pk_supply = ?", new Object[] { ((BdSupply)paramMap.get("supply")).getPkSupply() });
					param.put("rleCode", rleCode);
					listMap.add(param);
				}
				msgSendBd.sendMfnMsg("yz_supply", listMap);
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendBdResBedMsg(Map<String,Object> paramMap) throws Exception{
			//发送消息
				List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
				
				String codeEmp = paramMap.get("codeEmp")==null?"":paramMap.get("codeEmp").toString();//获取当前操作人关联人员的编码
				Object codeObject = codeEmp;
				//List<bean>z转json,json转list<Map<String,Object>>格式
				List<Map<String,Object>> resbedList = JsonUtil.readValue(JsonUtil.writeValueAsString(paramMap.get("resbedList"),"yyyy-MM-dd HH:mm:ss"), new TypeReference<List<Map<String,Object>>>() {});
				Map<String, Object> tmpMap = new HashMap<>();
				
				if(resbedList!=null && resbedList.size()>0){
					//获取集合信息判断信息是'添加'还是'修改'
					for (int i = 0; i < resbedList.size(); i++) {
						tmpMap = resbedList.get(i);
						String pkBed = tmpMap.get("pkBed")==null?"":tmpMap.get("pkBed").toString();
						String isUpdate = tmpMap.get("isUpdate")==null?"":tmpMap.get("isUpdate").toString();
						if((pkBed==null||pkBed.equals(""))){
							tmpMap.put("empCode", codeObject);
							tmpMap.put("state", AddState);	//对添加的信息增加'添加'状态
							listMap.add(tmpMap);
						} else{ 	
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
						msgSendBd.sendMfnMsg("Bed", listMap);
				}
			
     }
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendBdOrdMsg(Map<String,Object> paramMap) throws Exception{
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
					
					if(null != paramMap.get("bdOrds")){
						paramMap.putAll(objectToMap(paramMap.get("bdOrds")));
					}
					paramMap.put("rleCode",rleCode);
					listMap.add(paramMap);
				}else if(rleCode.equals("MDL")){
					String pkOrd = null;
					if(paramMap.get("pkOrd") != null){
						pkOrd = paramMap.get("pkOrd").toString();
					}
					//根据医嘱主键获取医嘱字典信息
					Map<String,Object> ordMap = DataBaseHelper.queryForMap("select * from BD_ORD where PK_ORD=?", pkOrd);
					//获取执行科室
					Map<String,Object> ordDepts = DataBaseHelper.queryForMap("select * from BD_ORD_DEPT where PK_ORDORG = (select BD_ORD_ORG.PK_ORDORG from BD_ORD_ORG where BD_ORD_ORG.PK_ORD = ? and BD_ORD_ORG.PK_ORG=?)",pkOrd,UserContext.getUser().getPkOrg());
					
					paramMap.putAll(ordMap);
					if(ordDepts!=null && ordDepts.size()>0) paramMap.put("ordDept",(ordDepts.get(0)));
					paramMap.put("rleCode",rleCode);
					listMap.add(paramMap);
				}
				 msgSendBd.sendMfnMsg("yz_order_item", listMap);
				
				//判断医嘱类型是否为检验医嘱
				if(("03").equals(paramMap.get("codeOrdtype").toString().substring(0, 2))){
					String sql ="select oi.pk_orditem,oi.pk_ord,oi.pk_item,oi.flag_pd,oi.flag_union, case when oi.flag_pd='0' then item.spec else pd.spec end spec, case when oi.flag_pd='0' then item.code else pd.code end code, case when oi.flag_pd='0' then item.name else pd.name end name, oi.quan, case when oi.flag_pd='0' then item.price else pd.price/pd.pack_size end price from bd_ord_item oi left outer join bd_item item on oi.pk_item=item.pk_item and oi.flag_pd='0' left outer join bd_pd pd on oi.pk_item=pd.pk_pd and oi.flag_pd='1' where oi.del_flag = '0' and oi.pk_ord = ?";
					List<Map<String, Object>> itemList = DataBaseHelper.queryForList(sql, paramMap.get("pkOrd").toString());
					Map<String, Object> itemMap = new HashMap<>();
					BigDecimal amtAcc = BigDecimal.ZERO;
					if(0 != itemList.size()){
						for (int i = 0; i < itemList.size(); i++) {
							Double pric = Double.parseDouble(itemList.get(i).get("price").toString());
							Double quan = Double.parseDouble(itemList.get(i).get("quan").toString());
							BigDecimal amt=BigDecimal.valueOf(pric*quan);
							amtAcc=amtAcc.add(amt);	
						}
						listMap.get(0).put("price", amtAcc);
						listMap.get(0).put("dtItemtype", "JY");
						listMap.get(0).put("state", paramMap.get("STATUS").toString());
						msgSendBd.sendMfnMsg("Undrug", listMap);
					}
				}
	}
	/**
	 * 发送收费项目信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendBdItemMsg(Map<String,Object> paramMap) throws HL7Exception{
		List<Map<String, Object>> sendList = new ArrayList<>();
			sendList.add((Map<String, Object>)paramMap.get("item"));
			msgSendBd.sendMfnMsg("Undrug", sendList);
	}
	/**
	 * 发送收费项目信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendBdItemSetMsg(Map<String,Object> paramMap) throws HL7Exception{
		List<Map<String, Object>> sendList = new ArrayList<>();
			sendList.addAll((List<Map<String, Object>>)paramMap.get("itemSets"));
			msgSendBd.sendMfnMsg("UndrugGroup", sendList);
	}
	/**
	 * 发送科室信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendBdOuDeptMsg(Map<String,Object> paramMap) throws Exception{
		if(paramMap.get("dept")==null)
			return;
		        //发送消息
				Map<String,Object> dept = objectToMap(paramMap.get("dept"));
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
				msgSendBd.sendMfnMsg("Department", listMap);
				//发送科室病区关系信息
				msgSendBd.sendMfnMsg("DeptStat", listMap);
	}
	/**
	 * 发送人员信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
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
				    paramMap.putAll((Map)(empJobs.get(i)));
					//操作员关联人员的编码
					empMap.put("empCode", paramMap.get("codeEmp"));
					empMap.put("rleCode", rleCode);
					//查询科室编码+科室名称
					Map<String,Object> deptMap = DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = ?", paramMap.get("pkDept"));
					String deptInfo = deptMap.get("codeDept")+"^"+deptMap.get("nameDept");
					empMap.put("deptInfo", deptInfo);
					empMap.put("CODEDEPTS", deptMap.get("codeDept"));
					listMap.add(empMap);
					msgSendBd.sendMfnMsg("Employee", listMap);
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
					paramMap.putAll((Map)(empJobs.get(0)));
					//查询科室编码+科室名称
					Map<String,Object> deptMap = DataBaseHelper.queryForMap("select code_dept,name_dept from bd_ou_dept where pk_dept = ?",paramMap.get("pkDept"));
					String deptInfo = deptMap.get("codeDept")+"^"+deptMap.get("nameDept");
					empMap.put("deptInfo", deptInfo);
					empMap.put("CODEDEPTS", deptMap.get("codeDept"));
					//获取当前机构主键
					empMap.put("nowPkOrg",paramMap.get("pkOrg"));
					empMap.put("rleCode", rleCode);
					listMap.add(empMap);
					msgSendBd.sendMfnMsg("Employee", listMap);
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
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendUserMsg(Map<String,Object> paramMap) throws HL7Exception{
        	List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        	String rleCode = paramMap.get("STATUS").toString();
 			if(rleCode.equals(AddState))
 				rleCode = "MAD";
 			if(rleCode.equals(UpdateState))
 				rleCode = "MUP";
 			if(rleCode.equals(DelState))
 				rleCode = "MDL";
 			List<Map<String, Object>> codeLisMap = DataBaseHelper.queryForList("SELECT CODE_DEPT FROM bd_ou_dept bd LEFT JOIN bd_ou_empjob empjob ON empjob.pk_dept = bd.pk_dept WHERE empjob.Pk_emp = ?", MsgUtils.getPropValueStr(paramMap,"pkEmp"));
 			for (int i = 0; i < codeLisMap.size(); i++) {
 				Map<String, Object> paramMapMap = new HashMap<>();
 				
 				if(rleCode.equals("MAD") || rleCode.equals("MUP")) {
 				    if(paramMap.get("pd")!=null){   	
 				    	if(null !=paramMap.get("pd") && (paramMap.get("pd")).equals("")){
 				    		 paramMap.putAll((Map)paramMap.get("pd"));
 				    	}
 				    }else if(paramMap.get("user")!=null){
 				    	paramMap.putAll((Map)paramMap.get("user"));
 				    	if(null !=paramMap.get("pd") && (paramMap.get("pd")).equals("")){
 				    		 paramMap.putAll((Map)paramMap.get("pd"));
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
             msgSendBd.sendMfnMsg("UserInfo", listMap);
	}
	
	 /**
	 * 发送药品信息
	 * @param paramMap
	 * @throws Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
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
		     //获取当前机构主键
		     paramMap.put("opeType", rleCode);
		     listMap.add(paramMap);
		}else if (rleCode.equals("MDL")) {
		     paramMap.put("opeType", rleCode);
		     listMap.add(paramMap);
		
		}
	    msgSendBd.sendMfnMsg("Drug", listMap);//发送药品信息
	
	}
	public static Map<String, Object> objectToMap(Object obj) throws Exception {    
        if(obj == null){    
            return null;    
        }   
  
        Map<String, Object> map = new HashMap<String, Object>();    
  
        Field[] declaredFields = obj.getClass().getDeclaredFields();    
        for (Field field : declaredFields) {    
            field.setAccessible(true);  
            map.put(field.getName(), field.get(obj));  
        }    
  
        return map;  
    } 
	
}
