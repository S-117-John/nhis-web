package com.zebone.nhis.emr.rec.tmp.service;


import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTemplate;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTmpOrg;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTmpSet;
import com.zebone.nhis.common.service.MessageService;
import com.zebone.nhis.emr.comm.service.EmrCommService;
import com.zebone.nhis.emr.common.EmrConstants;
import com.zebone.nhis.emr.rec.tmp.dao.EmrTmpMapper;
import com.zebone.nhis.emr.rec.tmp.vo.BdPvDocTempVo;
import com.zebone.nhis.emr.rec.tmp.vo.BdPvdocTempOrgVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 病历书写-模板编辑
 * @author chengjia
 *
 */
@Service
public class TmpMgrService {

	@Resource
	private EmrTmpMapper tmpMapper;
	@Resource
	private MessageService msgService;
	
	@Resource
	private EmrCommService emrCommService;
	
	
	/**
	 * 查询病历模板列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<EmrTemplate> queryTmpList(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
//		String pkDept=null;
//		String flagOpen=null;
//		if(map.get("pkDept")!=null) pkDept=map.get("pkDept").toString();
//		if(map.get("flagOpen")!=null) flagOpen=map.get("flagOpen").toString();
		//map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		List<EmrTemplate> rtnList=tmpMapper.queryTypeTmpList(map);
		return rtnList;
	}
	
	
	/**
	 * 查询科室病历模板列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<EmrTemplate> queryTmpListDept(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		map.put("pkEmp", UserContext.getUser().getPkEmp());
		List<EmrTemplate> rtnList=tmpMapper.queryTypeTmpListDept(map);
		return rtnList;
	}
	
	/**
	 * 查询病历模板列表（根据机构）
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<EmrTemplate> queryTmpListOrg(String param , IUser user){
		Map map = JsonUtil.readValue(param,Map.class);

		List<EmrTemplate> rtnList=tmpMapper.queryTmpListOrg(map);
		return rtnList;
	}
	
	
	/**
	 * 查询所有病历模板记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrTemplate> queryAllTmpList(String param , IUser user){

		List<EmrTemplate> rtnList=tmpMapper.queryAllTmpList();
		return rtnList;
	}	

	/**
	 * 保存病历模板
	 * @param param
	 * @param user
	 * @return
	 */
	public int saveEmrTemplate(String param , IUser user){
		EmrTemplate template=JsonUtil.readValue(param, EmrTemplate.class);
		String status="";
		
		if(StringUtils.isNoneEmpty(template.getStatus())) status= template.getStatus();
		try {
			if(status.equals(EmrConstants.STATUS_NEW)){
				return tmpMapper.saveEmrTemplate(template);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				return tmpMapper.updateEmrTemplate(template);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				return tmpMapper.deleteEmrTemplate(template.getPkTmp());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
		
		return 0;
	}
	
	/**
	 * 根据主键查询病历模板数据
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrTemplate queryTmpDataById(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		String pkTmp=map.get("pkTmp").toString();
		
		EmrTemplate tmp=tmpMapper.queryTmpDataById(pkTmp);
		
		return tmp;
	}	
	
	/**
	 * 根据模板类型、科室、code查询科室会诊病历模板
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrTemplate queryConsultTmpByCode(String param,IUser user){
		EmrTemplate rtn=new EmrTemplate();
		Map paramMap = JsonUtil.readValue(param,Map.class);
		paramMap.put("euTmpLevel", "1");
		
		List<EmrTemplate> tmpList=tmpMapper.queryConsultTmpByCode(paramMap);
		if(tmpList!=null && tmpList.size()>0){
			rtn=tmpList.get(0);
		}else{
			paramMap.put("euTmpLevel", "0");
			paramMap.put("pkDept", "");
			List<EmrTemplate> fullTmpList=tmpMapper.queryConsultTmpByCode(paramMap);
			if(fullTmpList!=null && fullTmpList.size()>0){
				rtn=fullTmpList.get(0);
			}else{
				throw new BusException("未查询到会诊病历模板!");
			}
		}
		
		return rtn;
	}	

	 /**
	  *根据主键查询病历模板（含分类）
	 * @param param
	 * @param user
	 * @return
	 */
	public EmrTemplate queryTmpById(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		String pkTmp=map.get("pkTmp").toString();
		String noData="0";
		if(map.get("noData")!=null){
			noData=map.get("noData").toString();
		}
		EmrTemplate tmp=null;
		if(noData!=null&&noData.equals("1")){
			tmp=tmpMapper.queryTmpByIdNoData(pkTmp);
		}else{
			tmp=tmpMapper.queryTmpById(pkTmp);
		}
		
		return tmp;
	}	
	
	 /**
	  *根据编码查询病历模板（含分类）
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EmrTemplate queryTmpByCode(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		EmrTemplate tmp=tmpMapper.queryTmpByCode(map);
		
		return tmp;
	}	
	
	/**
	 * 根据条件查询病历模板
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrTemplate> queryTmpByConds(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		List<EmrTemplate> rtnList=tmpMapper.queryTmpByConds(map);
		
		return rtnList;
	}
	
	/**
	 * 查询科室用模板
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrTemplate> queryDeptTmpList(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		map.put("pkOrg", UserContext.getUser().getPkOrg().trim()+"%");
		List<EmrTemplate> rtnList=tmpMapper.queryDeptTmpList(map);
		
		return rtnList;
	}
	
	/**
	 * 根据条件查询病历元素设置表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrTmpSet> queryTmpSetByCode(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		List<EmrTmpSet> list=tmpMapper.queryTmpSetByCode(map.get("pkTmp").toString(), map.get("code").toString());
		
		return list;
	}	
	
	/**
	 * 保存病历模板设置
	 * @param param
	 * @param user
	 * @return
	 */
	public int saveEmrTmpSet(String param , IUser user){
		EmrTmpSet emrTmpSet=JsonUtil.readValue(param, EmrTmpSet.class);
		String status="";
		if(StringUtils.isNotEmpty(emrTmpSet.getStatus())){
			status=emrTmpSet.getStatus();
		}
		if(status.equals(EmrConstants.STATUS_NEW)){
			return tmpMapper.saveEmrTmpSet(emrTmpSet);
		}else if(status.equals(EmrConstants.STATUS_UPD)){
			return tmpMapper.updateEmrTmpSet(emrTmpSet);
		}else if(status.equals(EmrConstants.STATUS_DEL)){
			return tmpMapper.deleteEmrTmpSet(emrTmpSet.getPkSet());
		}
		return 0;
	}	
	
	/**
	 * 保存病历模板（导入）
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveEmrTemplateImport(String param , IUser user){
		List<EmrTemplate> tmpList=JsonUtil.readValue(param, new TypeReference<List<EmrTemplate>>(){});
		if(tmpList==null||tmpList.size()==0) return;
		
		for (EmrTemplate emrTemplate : tmpList) {
			String pkTmp=emrTemplate.getPkTmp();
			String pkTmpOld=emrTemplate.getPkTmpOld();
			if(StringUtils.isEmpty(pkTmpOld)) continue; 
			
			EmrTemplate tmpOld=tmpMapper.queryTmpById(pkTmpOld);
			emrTemplate.setDocData(tmpOld.getDocData());
			emrTemplate.setDocXml(tmpOld.getDocXml());
			emrTemplate.setAssociationRules(tmpOld.getAssociationRules());
			
			tmpMapper.saveEmrTemplate(emrTemplate);
			
			List<EmrTmpSet> setList=tmpMapper.queryTmpSetByTmp(pkTmpOld);
			if(setList!=null&&setList.size()>0){
				for (EmrTmpSet emrTmpSet : setList) {
					emrTmpSet.setPkSet(NHISUUID.getKeyId());
					emrTmpSet.setPkTmp(pkTmp);
					emrTmpSet.setPkOrg(emrTemplate.getPkOrg());
					tmpMapper.saveEmrTmpSet(emrTmpSet);
				}
			}
		}
	}	
	
	/**
	 * 保存病历模板（另存）
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveEmrTemplateAs(String param , IUser user){
		EmrTemplate emrTemplate=JsonUtil.readValue(param, EmrTemplate.class);
		if(emrTemplate==null) return;
		
		String pkTmp=emrTemplate.getPkTmp();
		String pkTmpOld=emrTemplate.getPkTmpOld();
		if(StringUtils.isEmpty(pkTmpOld)) return; 
		int no = emrCommService.getSerialNo("emr_template", "tmp_id", 1, user);
		if(no>0){
			emrTemplate.setCode(Integer.toString(no));
		}
		EmrTemplate tmpOld=tmpMapper.queryTmpById(pkTmpOld);
		if(emrTemplate.getDocData()==null)
			emrTemplate.setDocData(tmpOld.getDocData());
		if(emrTemplate.getDocXml()==null||emrTemplate.getDocXml().equals(""))
			emrTemplate.setDocXml(tmpOld.getDocXml());
		if(emrTemplate.getAssociationRules()==null||emrTemplate.getAssociationRules().equals(""))
			emrTemplate.setAssociationRules(tmpOld.getAssociationRules());
		
		tmpMapper.saveEmrTemplate(emrTemplate);
		
		List<EmrTmpSet> setList=tmpMapper.queryTmpSetByTmp(pkTmpOld);
		if(setList!=null&&setList.size()>0){
			for (EmrTmpSet emrTmpSet : setList) {
				emrTmpSet.setPkSet(NHISUUID.getKeyId());
				emrTmpSet.setPkTmp(pkTmp);
				emrTmpSet.setPkOrg(emrTemplate.getPkOrg());
				tmpMapper.saveEmrTmpSet(emrTmpSet);
			}
		}
		
		List<EmrTmpOrg> orgTmpist=tmpMapper.queryTmpOrgByTmpId(pkTmpOld);
		if(orgTmpist!=null&&orgTmpist.size()>0){
			EmrTmpOrg org = orgTmpist.get(0);
			org.setPkTmporg(NHISUUID.getKeyId());
			org.setPkTmp(pkTmp);
			org.setPkOrg(UserContext.getUser().getPkOrg());
			tmpMapper.saveEmrTmpOrg(org);
		}
		
	}
	
	/**
	 * 保存病历模板机构科室
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public void saveEmrTmpOrgs(String param , IUser user){
		List<EmrTmpOrg> tmpOrgList=JsonUtil.readValue(param, new TypeReference<List<EmrTmpOrg>>(){});
		if(tmpOrgList==null||tmpOrgList.size()==0) return;
		
		for (EmrTmpOrg  tmpOrg : tmpOrgList) {
			
			String status="";
			if(StringUtils.isNotEmpty(tmpOrg.getStatus())){
				status=tmpOrg.getStatus();
			}
			if(status.equals(EmrConstants.STATUS_NEW)){
				//查询是否已经存在
				Map<String, String> map = new HashMap<String,String>();
				map.put("pkTmp", tmpOrg.getPkTmp());
				map.put("pkOrg", tmpOrg.getPkOrg());
				map.put("pkDept", tmpOrg.getPkDept());
				List<EmrTmpOrg> list = tmpMapper.queryTmpOrgByConds(map);
				if(list==null||list.size()==0){
					tmpMapper.saveEmrTmpOrg(tmpOrg);
				}else{
					String pk=tmpOrg.getPkTmporg();
					String pkTmp=tmpOrg.getPkTmp();
					String pkOrg=tmpOrg.getPkOrg();
					String pkDept=tmpOrg.getPkDept();
					pkDept=pkDept==null?"":pkDept;
					for (EmrTmpOrg  item : tmpOrgList) {
						String pkL=item.getPkTmporg();
						String pkTmpL=item.getPkTmp();
						String pkOrgL=item.getPkOrg();
						String pkDeptL=item.getPkDept();
						pkDeptL=pkDeptL==null?"":pkDeptL;
						String statusL=item.getStatus()==null?"":item.getStatus();
						if(!pk.equals(pkL)&&pkTmp.equals(pkTmpL)&&pkOrg.equals(pkOrgL)&&pkDept.equals(pkDeptL)&&statusL.equals("del")){
							tmpMapper.saveEmrTmpOrg(tmpOrg);
							break;
						}
					}
				}
				//tmpMapper.saveEmrTmpOrg(tmpOrg);
			}else if(status.equals(EmrConstants.STATUS_UPD)){
				tmpMapper.updateEmrTmpOrg(tmpOrg);
			}else if(status.equals(EmrConstants.STATUS_DEL)){
				tmpMapper.deleteEmrTmpOrg(tmpOrg.getPkTmporg());
			}			
		}
	}	
	/**
	 * 查询病历模板机构科室
	 * @param param
	 * @param user
	 * @return
	 */
	public List<EmrTmpOrg> queryEmrTmpOrgs(String param,IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		
		List<EmrTmpOrg> list=tmpMapper.queryTmpOrgByTmpId(map.get("pkTmp").toString());
		
		return list;
	}	
	/**
	 * 根据住院模板同步到门诊表里
	 * @param param
	 * @param user
	 * @return
	 */
	public int savePvdocTemp(String param,IUser user){
		//BdOuDept bdDept=JsonUtil.readValue(JsonUtil.getJsonNode(param, "pkDept"), BdOuDept.class);
		List<EmrTemplate> tempList=JsonUtil.readValue(param, new TypeReference<List<EmrTemplate>>(){});
		//List<EmrTemplate> tempList = JsonUtil.readValue(JsonUtil.getJsonNode(param, "list"), new TypeReference<List<EmrTemplate>>(){});
		int result=0;
		String sql="select DOC_DATA from EMR_TEMPLATE where PK_TMP=?";
		String sqlPvDpc="select count(1) from BD_PVDOC_TEMP where code=? ";
		byte[] docData= null;
		for (EmrTemplate emrTemplate : tempList) {
			docData=DataBaseHelper.queryForScalar(sql,byte[].class,emrTemplate.getPkTmp());
			Integer count=DataBaseHelper.queryForScalar(sqlPvDpc,Integer.class,new Object[]{emrTemplate.getCode()});
			if(docData!=null){
				BdPvDocTempVo bdPvDocTemp=new BdPvDocTempVo();
				bdPvDocTemp.setPkPvdoctemp(emrTemplate.getPkTmp());
				bdPvDocTemp.setPkOrg(UserContext.getUser().getPkOrg());
				bdPvDocTemp.setCode(emrTemplate.getCode());
				bdPvDocTemp.setName(emrTemplate.getName());
				bdPvDocTemp.setDataDoc(docData);
				bdPvDocTemp.setFlagActive("1");
				bdPvDocTemp.setFlagEmr(emrTemplate.getEuType());
				bdPvDocTemp.setFlagDef(emrTemplate.getFlagDefault());
				if(count>0){
					result=DataBaseHelper.updateBeanByPk(bdPvDocTemp,false);
				}else{
					result=DataBaseHelper.insertBean(bdPvDocTemp);
				}
				/*BdPvdocTempOrgVo bdPvdocTempOrg=new BdPvdocTempOrgVo();
				bdPvdocTempOrg.setPkPvdoctemp(bdPvDocTemp.getPkPvdoctemp());
				bdPvdocTempOrg.setPkOrgUse(bdDept.getPkOrg());
				bdPvdocTempOrg.setPkDept(bdDept.getPkDept());
				result=DataBaseHelper.insertBean(bdPvdocTempOrg);*/
			}
		}
		if(result>0){
			return 1;
		}else{
			return 0;
		}
	}
	/**
	 * 门诊表里模板添加引用关系
	 * @param param
	 * @param user
	 * @return
	 */
	public int inPvdocTemp(String param,IUser user){
		//List<BdPvDocTempVo> tempList=JsonUtil.readValue(param, new TypeReference<List<BdPvDocTempVo>>(){});
		BdOuDept bdDept=JsonUtil.readValue(JsonUtil.getJsonNode(param, "dept"), BdOuDept.class);
		List<BdPvDocTempVo> tempList = JsonUtil.readValue(JsonUtil.getJsonNode(param, "list"), new TypeReference<List<BdPvDocTempVo>>(){});
		int result=0;
		for (BdPvDocTempVo pvDocTemp : tempList) {
			BdPvdocTempOrgVo bdPvdocTempOrg=new BdPvdocTempOrgVo();
			bdPvdocTempOrg.setPkPvdoctemp(pvDocTemp.getPkPvdoctemp());
			bdPvdocTempOrg.setPkOrgUse(bdDept.getPkOrg());
			bdPvdocTempOrg.setPkDept(bdDept.getPkDept());
			bdPvdocTempOrg.setFlagDefault(pvDocTemp.getFlagDef());
			bdPvdocTempOrg.setDelFlag("0");
			result=DataBaseHelper.insertBean(bdPvdocTempOrg);
		}
		if(result>0){
			return 1;
		}else{
			return 0;
		}
	}	

	/**
	 * 门诊病例删除模板引用表
	 * @param param
	 * @param user
	 * @return
	 */
	public int delinPvdocTemp(String param,IUser user){
		BdPvdocTempOrgVo param1=JsonUtil.readValue(param,BdPvdocTempOrgVo.class);
		int result = 0;
		result=DataBaseHelper.deleteBeanByPk(param1);
		if(result>0){
			return 1;
		}else{
			return 0;
		}
	}


		/**
		 * 根据住院模板同步到门诊表里
		 * @param param
		 * @param user
		 * @return
		 */
	public int updatePvdocTemp(String param, IUser user) {
		List<BdPvDocTempVo> bpdtv = JsonUtil.readValue(param,
				new TypeReference<List<BdPvDocTempVo>>() {
				});
		int result = 0;
		String sql = "select data_doc from BD_PVDOC_TEMP where pk_pvdoctemp=?";
		String sqlPvDpc = "select count(1) from BD_PVDOC_TEMP where pk_pvdoctemp=?";
		byte[] docData = null;
		for (BdPvDocTempVo bdpvdoctempVo : bpdtv) {
			docData = DataBaseHelper.queryForScalar(sql, byte[].class,
					bdpvdoctempVo.getPkPvdoctemp());
			Integer count = DataBaseHelper.queryForScalar(sqlPvDpc,
					Integer.class,
					new Object[] { bdpvdoctempVo.getPkPvdoctemp() });
			if (docData != null) {
				BdPvDocTempVo bdPvDocTemp = new BdPvDocTempVo();
				bdPvDocTemp.setPkPvdoctemp(bdpvdoctempVo.getPkPvdoctemp());
				bdPvDocTemp.setPkOrg(UserContext.getUser().getPkOrg());
				bdPvDocTemp.setCode(bdpvdoctempVo.getCode());
				bdPvDocTemp.setName(bdpvdoctempVo.getName());
				bdPvDocTemp.setDataDoc(docData);
				bdPvDocTemp.setFlagActive(bdpvdoctempVo.getFlagActive());
				bdPvDocTemp.setFlagEmr(bdpvdoctempVo.getFlagEmr());
				bdPvDocTemp.setFlagDef(bdpvdoctempVo.getFlagDef());
				if (count > 0) {
					result = DataBaseHelper.updateBeanByPk(bdPvDocTemp, false);
				} 
			}
		}
		if (result > 0) {
			return 1;
		} else {
			return 0;
		}
	}		
	
}
