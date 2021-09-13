package com.zebone.nhis.cn.opdw.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.opdw.dao.CnOpPvDocMapper;
import com.zebone.nhis.cn.opdw.dao.PvDocMapper;
import com.zebone.nhis.cn.opdw.vo.BdPvDocTempAndOrgVo;
import com.zebone.nhis.cn.opdw.vo.CnOpPvDocVo;
import com.zebone.nhis.common.module.cn.opdw.BdOpEmrTempCate;
import com.zebone.nhis.common.module.cn.opdw.EmrDeptSet;
import com.zebone.nhis.common.module.cn.opdw.PvDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrTxtData;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.emr.common.EmrFtpUtils;
import com.zebone.nhis.emr.common.EmrSaveUtils;
import com.zebone.nhis.emr.common.EmrUtils;
import com.zebone.nhis.emr.rec.rec.dao.EmrRecMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnOpPvDocService {
	
	@Autowired
	private CnOpPvDocMapper cnOpPvDocMapper; 
	
	@Resource
	private PvDocMapper recMapper;
	
	/**
	 * 查询病人的所有病历
	 * @param param
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	 public List<CnOpPvDocVo> qryAllPvDoc(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		String saveDataMode = EmrSaveUtils.getSaveDataMode();
		String dbName = EmrSaveUtils.getDbName();
		String sql = "select case when doc.name is null then temp.name else doc.name end as temp_name,"
				+ " doc.*,temp.name,pk_dept as pkDept "
				+ " from pv_doc doc inner join pv_encounter pv on pv.pk_pv= doc.pk_pv "
				+ "  left join bd_pvdoc_temp temp on doc.pk_pvdoctemp=temp.pk_pvdoctemp"
			     + "  where doc.pk_pv='"+pkPv+"'";
		 String hosCode = ApplicationUtils.getPropertyValue("emr.hos.code", "");
		 //if (hosCode != null && hosCode.equals("szkq")) {
		 	sql+=" and doc.del_flag !='1'";
		 //}
		 List<CnOpPvDocVo> cnOpPvDocVos = DataBaseHelper.queryForList(sql, CnOpPvDocVo.class);;
		 if(saveDataMode.equals("0")){
			//本地存储
		 }else if(saveDataMode.equals("1")){
			 //独立存储
			 int i=0;
			 int j=0;
			 CnOpPvDocVo doc=null;
			 List<String> pkPvdocs=new ArrayList<>();
			 if(cnOpPvDocVos!=null&&cnOpPvDocVos.size()>0){
				 for(i=0;i<cnOpPvDocVos.size();i++){
					 doc=cnOpPvDocVos.get(i);
					 if(doc.getDataDoc()!=null) continue;
					 
					 pkPvdocs.add(doc.getPkPvdoc());
				}
				if(pkPvdocs!=null&&pkPvdocs.size()>0){
					List<PvDoc> docs = recMapper.queryDocListEmrByPks(dbName, pkPvdocs);
					if(docs!=null&&docs.size()>0){
						for(i=0;i<docs.size();i++){
							PvDoc pvDoc=docs.get(i);
							if(pvDoc!=null&&pvDoc.getDataDoc()!=null){
								for(j=0;j<cnOpPvDocVos.size();j++){
									CnOpPvDocVo docVo=cnOpPvDocVos.get(j);
									if(docVo.getPkPvdoc()==null) continue;
									if(pvDoc.getPkPvdoc()!=null&&pvDoc.getPkPvdoc().equals(docVo.getPkPvdoc())&&pvDoc.getDataDoc()!=null){
										docVo.setDataDoc(pvDoc.getDataDoc());
										break;
									}
								}
								
							}
						}
					}
				}
			}
		 }
		 
		 return cnOpPvDocVos;
	 }
	 
	 /**
	  * 查询当前可用模板
	  * @param param
	  * @param user
	  * @return
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	 */
	 public List<BdPvDocTempAndOrgVo> qryAllPvDocForUse(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
			 String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");
			 String pkDept = JsonUtil.getFieldValue(param, "pkDept");
			 String sql = "select temp.*,tporg.pk_temporg,tporg.pk_org_use,tporg.pk_dept,tporg.flag_default,tmp.eu_tmp_level  "
			 		+ "    from bd_pvdoc_temp temp "
					    +" inner join bd_pvdoc_temp_org tporg on temp.pk_pvdoctemp=tporg.pk_pvdoctemp "
					    +" inner join emr_template tmp on temp.pk_pvdoctemp=tmp.pk_tmp "
					    +"  where tporg.pk_org_use='"+pkOrg+"' and temp.flag_active=1 and temp.del_flag='0'";
			 if(pkDept!=null){
				 sql+=" and (tporg.pk_dept='"+pkDept+"' or tporg.pk_dept='*')";
			 }
			 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);	
			 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
			 
			 List<BdPvDocTempAndOrgVo> ret = new ArrayList<BdPvDocTempAndOrgVo>();
			 
			 for(Map<String,Object> map : ps){
				 BdPvDocTempAndOrgVo bpv = new BdPvDocTempAndOrgVo();
				 BeanUtils.copyProperties(bpv, map);	
				 
				 ret.add(bpv);
			 }
			 
			 return ret;
	}
	 
	 
	 /**
	  * 查询门诊当前可用模板
	  * @param param
	  * @param user
	  * @return
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	 */
		 public List<BdPvDocTempAndOrgVo> qryAllPvDocForUseToMZ(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
			 String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");
			 //String pkDept = JsonUtil.getFieldValue(param, "pkDept");
			 String sql = "select temp.* from bd_pvdoc_temp temp "
					    //+" left join bd_pvdoc_temp_org tporg on temp.pk_pvdoctemp=tporg.pk_pvdoctemp "
					   // +"  where tporg.pk_org_use='"+pkOrg+"' and temp.flag_active=1 and temp.del_flag='0'";
					 +"where temp.pk_org='"+pkOrg+"' and temp.flag_active=1 and temp.del_flag!='1'";
//			 if(pkDept!=null){
//				 sql+=" and pk_dept='"+pkDept+"'";
//			 }
			 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);	
			 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
			 
			 List<BdPvDocTempAndOrgVo> ret = new ArrayList<BdPvDocTempAndOrgVo>();
			 
			 for(Map<String,Object> map : ps){
				 BdPvDocTempAndOrgVo bpv = new BdPvDocTempAndOrgVo();
				 BeanUtils.copyProperties(bpv, map);	
				 
				 ret.add(bpv);
			 }
			 
			 return ret;
	}
	 /**
	  * 
	  * @param param
	  * @param user
	  * @return 保存就诊文书
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public PvDoc savePvDoc(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 PvDoc pvd = JsonUtil.readValue(param,PvDoc.class);
		 User u = (User)user;
		 String saveDataMode = EmrSaveUtils.getSaveDataMode();
		 String dbName = EmrSaveUtils.getDbName();

		 if(saveDataMode.equals("1")){//独立存储
			 pvd.setDbName(dbName);
		 }
		 if(StringUtils.isEmpty(pvd.getPkPvdoc())){
			 pvd.setCreator(u.getPkEmp());
			 pvd.setCreateTime(new Date()); 
			 pvd.setDateDoc(new Date());
			 pvd.setNameEmpDoc(user.getUserName());
			 pvd.setPkEmpDoc(u.getPkEmp());
		 }else{
			 pvd.setModifier(u.getPkEmp());
			 pvd.setModityTime(new Date());
		 }

		 pvd.setDelFlag("0");
		 if(StringUtils.isEmpty(pvd.getPkPvdoc())){
			//DataBaseHelper.insertBean(pvd);
			 //新增
			if(saveDataMode.equals("0")){//本地存储
				DataBaseHelper.insertBean(pvd);
			}else if(saveDataMode.equals("1")){//独立存储
				String pkPvdoc = NHISUUID.getKeyId();
				pvd.setPkPvdoc(pkPvdoc);
				recMapper.savePvDocEmr(pvd);
				pvd.setDataDoc(null);
				pvd.setDocExpData(null);
				pvd.setDbName(null);
				//recMapper.savePvDocEmr(pvd);
				DataBaseHelper.insertBean(pvd);
			}
		  }
		 else { 
		 	//修改
			 //DataBaseHelper.updateBeanByPk(pvd, false);
			 if(saveDataMode.equals("0")){//本地存储
				 DataBaseHelper.updateBeanByPk(pvd, false);
			 }else if(saveDataMode.equals("1")){//独立存储
				int row = recMapper.updatePvDocEmr(pvd);
				if(row<=0){
					//兼容并行期/存储库无记录则插入
					recMapper.savePvDocEmr(pvd);
				}
				pvd.setDataDoc(null);
				pvd.setDocExpData(null);
				pvd.setDbName(null);
				DataBaseHelper.updateBeanByPk(pvd, false);
			}
		 }
		 
		 //保存emr_txt_data
		 saveEmrExpData(pvd);
		 
		 //保存图片
		 saveEmrImageData(pvd);
		 
		 return pvd;
	 }
	 
		/**
		 * 保存病历文本数据
		 * @param medRec
		 * @param medDoc
		 */
		private void saveEmrExpData(PvDoc pvDoc) {
			if(pvDoc!=null&&pvDoc.getDocXml()!=null&&!pvDoc.getDocXml().equals("")){
				String docXml = pvDoc.getDocXml();
				Date now = new Date();
				//先删除、再插入
				DataBaseHelper.execute("delete from emr_txt_data where pk_pv = ? and  pk_rec = ? ",new Object[] {pvDoc.getPkPv(),pvDoc.getPkPvdoc()});
				
				String docTxt=EmrUtils.getDocFullTxt(docXml);
				
				//存储整份病历文本
				saveEmrTxtData(pvDoc, now, docTxt,null);
				
			}
		}
		
		
		private void saveEmrTxtData(PvDoc pvDoc, Date now, String docTxt,String remark) {
			if(docTxt==null||docTxt.equals("")) return;
			
			EmrTxtData txt = new EmrTxtData();
			
			txt.setPkPv(pvDoc.getPkPv());
			txt.setPkDoc(pvDoc.getPkPvdoc());
			txt.setPkRec(pvDoc.getPkPvdoc());
			txt.setTypeCode("020001");//门(急)诊病历
			txt.setParaCode("00");//全文
			txt.setDocTxt(docTxt);
			txt.setRemark(remark);
			txt.setDelFlag("0");
			txt.setCreator(pvDoc.getCreator());
			txt.setCreateTime(now);
			txt.setTs(now);
			
			DataBaseHelper.insertBean(txt);
		}

		
	 /**
	 * 保存病历图片
	 * @param status
	 * @param medDoc
	 * @return
	 */
	 private void saveEmrImageData(PvDoc pvDoc) {
		 String path="";
		 String saveExportData = ApplicationUtils.getSysparam("SaveExportData", true);
		 if(StringUtils.isNotEmpty("")||!saveExportData.equals("1")) return;
		 
		 String fileSaveMode = ApplicationUtils.getPropertyValue("emr.fileSaveMode", "loc");
		 //loc/ftp
		 if(fileSaveMode==null||fileSaveMode.equals("")) return;
		 // 新增和修改文档保存pdf
		 try {
			 byte[] expData = pvDoc.getDocExpData();
			 if (expData != null && expData.length > 0) {
				 String filePath = pvDoc.getFilePath();
				 String rootStr = ApplicationUtils.getPropertyValue("emr.filePath", "");
				 if (rootStr != null && !rootStr.equals("")) {
					if(fileSaveMode.equals("loc")){
						if(rootStr.indexOf("/")>=0) {
							path = rootStr + "//" + filePath;
						}else {
							path = rootStr + "\\" + filePath;
						}
						// 判断文件夹是否存在/不存在创建
						EmrUtils.checkDirsExists(path);
						//生成图片（覆盖）
						EmrUtils.byte2image(expData, path);
					}else if (fileSaveMode.equals("ftp")){
						String fileName=pvDoc.getPkPvdoc()+".pdf";
						path=filePath.replace(fileName, "");
						EmrFtpUtils.fileUpload(path, fileName, expData);
					}
				}
			}
			//DataBaseHelper.update("update emr_med_doc set file_path=? where pk_doc=?", new Object[]{pathSave,medDoc.getPkDoc()});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
 
	 /**
	  * 
	  * @param param
	  * @param user
	  * @return 获取就诊文书的二进制数据
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public PvDoc getDocData(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 String pktemp = JsonUtil.getFieldValue(param, "pktemp");
		 List<PvDoc> bps = null; 
		 try{
			 bps = cnOpPvDocMapper.getPvDocData(pktemp);
		 }catch(Exception e){
			 bps = null;
		 }
		 
		 
		 if(null == bps || bps.size() <= 0) return new PvDoc();
		 return bps.get(0);
	 }
	 
	 /**
	  * 
	  * @param param
	  * @param user
	  * @return s删除就诊病历
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public void delPvDoc(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
	 	String hosCode= ApplicationUtils.getPropertyValue("emr.hos.code", "");
//	 	if(hosCode!=null&&hosCode.equals("szkq")){
			String pktemp = JsonUtil.getFieldValue(param, "pkPvdoc");
			PvDoc pvdoc = new PvDoc();
			pvdoc.setPkPvdoc(pktemp);
			pvdoc.setDelFlag("1");
			DataBaseHelper.updateBeanByPk(pvdoc, false);
//		}else {
//			String pktemp = JsonUtil.getFieldValue(param, "pkPvdoc");
//			DataBaseHelper.deleteBeanByWhere(new PvDoc(), "pk_pvdoc='"+pktemp+"'");
//		}
	 }
	 
	 /**
	  * 查询编码和名称是否唯一
	  * 004003001012
	  * @param param
	  * @param user
	  * @return
	  */
	 public int queryOnlyCodeOrName(String param,IUser user){
		 BdOpEmrTempCate bdOpEmrTempCate = JsonUtil.readValue(param, BdOpEmrTempCate.class);
		 HashMap<String, String> params = new HashMap<String,String>();
		 params.put("code", bdOpEmrTempCate.getCode());
		 params.put("name", bdOpEmrTempCate.getName());
		 params.put("pkEmp", bdOpEmrTempCate.getPkEmp());
		 int count = 0;
		 count = cnOpPvDocMapper.getOnleCodeOrName(params);
		return count;
		 
	 }

	 public int saveEmrDeptSet(String param, IUser user) throws Exception {
		 List<EmrDeptSet> emrDeptSetList = JsonUtil.readValue(param, new TypeReference<List<EmrDeptSet>>() {
		 });
		 int emrDeptSetSum = -1;
		 if (emrDeptSetList==null||emrDeptSetList.size()<=0){
		 	throw new Exception("未获取到保存数据！");
		 }
		 for (EmrDeptSet emrDeptSet:emrDeptSetList){
		 	if (StringUtils.isBlank(emrDeptSet.getPkDeptSet())){
				emrDeptSetSum=DataBaseHelper.insertBean(emrDeptSet);
			}else{
				emrDeptSetSum=DataBaseHelper.updateBeanByPk(emrDeptSet, false);
			}
		 }
		 return emrDeptSetSum;
	 }

	 public List<EmrDeptSet> queryEmrDeptSet(String param, IUser user){
		 EmrDeptSet emrDeptSet = JsonUtil.readValue(param, EmrDeptSet.class);
		 String sql = "select * from EMR_DEPT_SET t where t.DEL_FLAG!='1'";
		 if (emrDeptSet!=null&&!StringUtils.isBlank(emrDeptSet.getPkDeptSet())){
		 	sql+=" and t.PK_DEPT_SET = '"+emrDeptSet.getPkDeptSet()+"'";
		 }
		 if (emrDeptSet!=null&&!StringUtils.isBlank(emrDeptSet.getSetPk())){
			 sql+=" and t.set_pk = '"+emrDeptSet.getSetPk()+"'";
		 }
		 if (emrDeptSet!=null&&!StringUtils.isBlank(emrDeptSet.getPkDept())){
			 sql+=" and t.PK_DEPT = '"+emrDeptSet.getPkDept()+"'";
		 }
		 if (emrDeptSet!=null&&!StringUtils.isBlank(emrDeptSet.getPkOrg())){
			 sql+=" and t.PK_ORG = '"+emrDeptSet.getPkOrg()+"'";
		 }
		 if (emrDeptSet!=null&&!StringUtils.isBlank(emrDeptSet.getCode())){
			 sql+=" and t.CODE = '"+emrDeptSet.getCode()+"'";
		 }
		 if (emrDeptSet!=null&&!StringUtils.isBlank(emrDeptSet.getName())){
			 sql+=" and t.NAME = '"+emrDeptSet.getName()+"'";
		 }
		 if (emrDeptSet!=null&&!StringUtils.isBlank(emrDeptSet.getSetCode())){
			 sql+=" and t.SET_CODE = '"+emrDeptSet.getSetCode()+"'";
		 }
		 if (emrDeptSet!=null&&!StringUtils.isBlank(emrDeptSet.getSetName())){
			 sql+=" and t.SET_NAME = '"+emrDeptSet.getSetName()+"'";
		 }
		 List<EmrDeptSet> emrDeptSetList = DataBaseHelper.queryForList(sql, EmrDeptSet.class);
		 return emrDeptSetList;
	 }
	 
		/**
		 *  查询所有病人的病历
		 * @param param
		 * @param user
		 * @return
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 */
		 public List<CnOpPvDocVo> qryAllPatPvDoc(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
			String pkPv = JsonUtil.getFieldValue(param, "pkPv");
			String dateBegin = JsonUtil.getFieldValue(param, "dateBegin");
			String dateEnd = JsonUtil.getFieldValue(param, "dateEnd");
			
			String sql = "select case when doc.NAME is null then temp.name else doc.NAME end as temp_name,"
					+ "          doc.pk_pvdoc,"
					+ "          doc.pk_pi,"
					+ "			 doc.pk_pv,"
					+ "          doc.date_doc,"
					+ "          doc.pk_emp_doc,"
					+ "			 doc.name_emp_doc,"
					+ "			 doc.pk_pvdoctemp,"
					+ "			 doc.del_flag,"
					+ "			 doc.flag_emr,"
					+ "			 doc.name,"
					+ "			 doc.flag_secret,"
					+ "			 doc.flag_print,"
					+ "			 doc.file_path,"
					+ "			 doc.eu_secret,"
					+ "			 doc.pk_secret,"
					+ "			 doc.remark,"
					+ "          pk_dept as pkDept "
					+ "  from pv_doc doc "
					+ " inner join pv_encounter pv on pv.pk_pv= doc.pk_pv "
					+ "  left join bd_pvdoc_temp temp on doc.pk_pvdoctemp=temp.pk_pvdoctemp"
				     + "  where doc.del_flag='0'"
				     + "    and doc.flag_emr = '1' "
				     + "    and doc.doc_xml is null ";
			 if (pkPv != null ) {
			 	sql+= "  and doc.pk_pv='"+pkPv+"'";
			 }
			 if (dateBegin != null ) {
				 	sql+= "  and doc.date_doc >= to_date('" + dateBegin + "','yyyyMMddhh24miss')";
			 }
			 if (dateEnd != null ) {
				 sql+= "  and doc.date_doc <= to_date('" + dateEnd + "','yyyyMMddhh24miss')";
			 }
			 
			 List<CnOpPvDocVo> cnOpPvDocVos = DataBaseHelper.queryForList(sql, CnOpPvDocVo.class);
			 
			 return cnOpPvDocVos;
		 }
		 
		 /**
			 *  查询单份病历内容
			 * @param param
			 * @param user
			 * @return
			 * @throws IllegalAccessException
			 * @throws InvocationTargetException
			 */
			 public List<CnOpPvDocVo> qryPatPvDocData(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
				String pkPv = JsonUtil.getFieldValue(param, "pkPv");
				String pkPvdoc = JsonUtil.getFieldValue(param, "pkPvdoc");
				
				String sql = "select doc.* "
						+ "  from pv_doc doc "
					    + " where doc.pk_pvdoc='"+pkPvdoc+"'";
	

			  	List<CnOpPvDocVo> cnOpPvDocVos = DataBaseHelper.queryForList(sql, CnOpPvDocVo.class);
				 
			  	return cnOpPvDocVos;
			 }
			 

			 /**
			  * 
			  * @param param
			  * @param user
			  * @return 保存就诊文书xml
			  * @throws IllegalAccessException
			  * @throws InvocationTargetException
			  */
			 public PvDoc savePvDocXml(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
				 PvDoc pvd = JsonUtil.readValue(param,PvDoc.class);
				 User u = (User)user;
				 
			 	DataBaseHelper.update("update pv_doc set doc_xml=?,remark='update xml' where pk_pvdoc=?", new Object[]{pvd.getDocXml(),pvd.getPkPvdoc()});
				 
				 //保存emr_txt_data
				 saveEmrExpData(pvd);
				 
				 return pvd;
			 }			 
			 
			 
			 
}
