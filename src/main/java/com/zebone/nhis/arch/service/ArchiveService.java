package com.zebone.nhis.arch.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.arch.dao.ArchiveMapper;
import com.zebone.nhis.arch.support.ArchUtil;
import com.zebone.nhis.arch.vo.ArchDocSys;
import com.zebone.nhis.arch.vo.ArchiveDto;
import com.zebone.nhis.arch.vo.FileVo;
import com.zebone.nhis.arch.vo.PvArchiveVO;
import com.zebone.nhis.arch.vo.WSArchiveManageIntfParamVo;
import com.zebone.nhis.arch.vo.WSArchiveManageIntfReturnVo;
import com.zebone.nhis.common.arch.service.ArchOuterService;
import com.zebone.nhis.common.arch.vo.ArchQryParam;
import com.zebone.nhis.common.arch.vo.ArchQryParamBody;
import com.zebone.nhis.common.arch.vo.ArchQueryResult;
import com.zebone.nhis.common.arch.vo.Fileinfo;
import com.zebone.nhis.common.module.arch.BdArchDoctype;
import com.zebone.nhis.common.module.arch.PvArchDoc;
import com.zebone.nhis.common.module.arch.PvArchive;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.ArchUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.webservice.client.zhongshan.op.DocareTrunDataService;
import com.zebone.nhis.webservice.client.zhongshan.pacs.MathService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病例归档服务类
 * 
 * @author gongxy
 * 
 */
@Service
public class ArchiveService {

	final static Map<String, String> mapWebServiceUri = new HashMap<String, String>();

	@Autowired
	private ArchiveMapper archiveMapper;
	
	@Autowired
	private ArchOuterService archOuterService;
	
	static final   String HIS = "00";
	static final   String EMR = "01";
	static final   String LIS = "02";
	static final   String PACS = "03";
	static final   String OP = "04";
	
	private static Map<String,BdArchDoctype> typeInfo = new HashMap<String,BdArchDoctype>();
	

	/**
	 * 根据条件查询患者信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryArchManagerPatiInfo(String param, IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		if(archiveDto.getDateBegin()!=null)archiveDto.setDateBegin(DateUtils.getDateMorning(archiveDto.getDateBegin(), 0));
		if(archiveDto.getDateEnd()!=null)archiveDto.setDateEnd(DateUtils.getDateMorning(archiveDto.getDateEnd(), 1));
		archiveDto.setPkOrg(((User) user).getPkOrg());
		/*if(archiveDto.getScanEuStatus()!=null && archiveDto.getScanEuStatus().equals("0"))
		{
			archiveDto.setScanSql("exists(select * from PV_ARCH_DOC doc where doc.PK_ARCHIVE=arch.PK_ARCHIVE and doc.NAME_DOC like 'arh%')");
		}else if(archiveDto.getScanEuStatus()!=null && archiveDto.getScanEuStatus().equals("1")){
			archiveDto.setScanSql("not exists(select * from PV_ARCH_DOC doc where doc.PK_ARCHIVE=arch.PK_ARCHIVE and doc.NAME_DOC like 'arh%')");
		}*/
		List<Map<String, Object>> result = archiveMapper.queryArchManagerPatiInfo(archiveDto);
		return result == null ? new ArrayList<Map<String, Object>>() : result;
	}

	 /**
	 * 查询患者的归档情况
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryPatiArchSituation(String param,
			IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);

		List<Map<String, Object>> mapListResult = new ArrayList<Map<String, Object>>();

		Set<String> codeIps = new HashSet<String>();
		Set<String> pkArchives = new HashSet<String>();
		Map<String, String> mapContainer = new HashMap<String, String>();
		for (PvArchiveVO pvArchive : archiveDto.getPvArchives()) {
			codeIps.add(pvArchive.getCodePv());
			pkArchives.add(pvArchive.getPkArchive());
			mapContainer.put(pvArchive.getCodePv(), pvArchive.getPkArchive());
		}
		String sql0 = "select pk_archive key_,eu_status from pv_archive where pk_archive in ("
				+ ArchUtil.convertSetToSqlInPart(pkArchives, "pk_archive")
				+ ")";
		Map<String, Map<String, Object>> mapPvArchive = DataBaseHelper
				.queryListToMap(sql0, new Object[] {});
		// 根据就诊号查询病人ID
		String sql1 = "select pi.code_ip,pi.code_pi,pi.name_pi,pv.code_pv,pv.pk_dept,pv.date_end,pv.pk_pv from pi_master pi inner join pv_encounter pv on pi.pk_pi=pv.pk_pi   where pv.code_pv in ("
				+ ArchUtil.convertSetToSqlInPart(codeIps, "code_pv") + ")";
		List<Map<String, Object>> mapListCodepi = DataBaseHelper.queryForList(
				sql1, new Object[] {});
		// 根据就诊号查询住院次数
		String sql2 = "select pv.code_pv key_, ip.ip_times from pv_ip ip inner join pv_encounter pv on ip.pk_pv=pv.pk_pv where pv.code_pv in ("
				+ ArchUtil.convertSetToSqlInPart(codeIps, "code_pv") + ")";
		Map<String, Map<String, Object>> mapListIpTimes = DataBaseHelper
				.queryListToMap(sql2, new Object[] {});
		/**
		 * 因为webService接口规定了，一次访问只能一个病人查询归档情况，所以只能循环
		 */
		initWebServiceUrl();//加载webService地址
		String isFitArch="1";//声明是否符合归档属性，1表示符合归档，0表示不符合归档
		for (Map<String, Object> map : mapListCodepi) {
			Map<String, Object> mapResult = new HashMap<String, Object>();
			//校验HIS系统文件数量
			String hisResult=getHisCount(mapContainer.get(map.get("codePv")),map.get("pkPv").toString(),map.get("codePv").toString());
			String [] hisResultSplit=hisResult.split("_");
			mapResult.put("00",hisResultSplit[0]);//his的文件数量
			if(isFitArch.equals("1"))
			{
				isFitArch=hisResultSplit[1];
			}
			
			
			//校验Emr系统文件数量
			String emrResult=getEmrCount(mapContainer.get(map.get("codePv")),map.get("pkPv").toString(),map.get("codePv").toString());
			String [] emrResultSplit=emrResult.split("_");
			mapResult.put("01",emrResultSplit[0]);//emr的文件数量
			if(isFitArch.equals("1"))
			{
				isFitArch=emrResultSplit[1];
				
			}
			//校验检查应该产生的归档文件数
			String risApplyResult=getRisApplyCount(map.get("pkPv").toString());
			String [] risApplySplit=risApplyResult.split("_");
			mapResult.put("10",risApplySplit[0]);
			if(isFitArch.equals("1"))
			{
				isFitArch=risApplySplit[1];
				
			}
			//校验检查已经产生的归档文件数
			String risOccResult=getRisOccCount(map.get("pkPv").toString());
			String [] risOccSplit=risOccResult.split("_");
			mapResult.put("11",risOccSplit[0]);
			if(isFitArch.equals("1"))
			{
				isFitArch=risOccSplit[1];
				
			}
			//校验检验应该产生的归档文件数
			String labApplyResult=getLabApplyCount(map.get("pkPv").toString());
			String [] labApplySplit=labApplyResult.split("_");
			mapResult.put("12",labApplySplit[0]);
			if(isFitArch.equals("1"))
			{
				isFitArch=labApplySplit[1];
				
			}
			//校验检验已经产生的归档文件数
			String labOccResult=getLabOccCount(map.get("pkPv").toString());
			String [] labOccSplit=labOccResult.split("_");
			mapResult.put("13",labOccSplit[0]);
			if(isFitArch.equals("1"))
			{
				isFitArch=labOccSplit[1];
				
			}
			// 一次获取一个病人的归档文件数     调用第三方的系统查询pacs、lis、op
			List<WSArchiveManageIntfReturnVo> rtnVos = getInfoFromWebService(new WSArchiveManageIntfParamVo(
					map.get("codePi").toString(), mapListIpTimes
							.get(map.get("codePv")).get("ipTimes").toString(),
					"ZY"),"all");
			//下面循环是查询pacs、lis、op的数量
			//List<WSArchiveManageIntfReturnVo> rtnVos = getInfoFromWebService(new WSArchiveManageIntfParamVo("000763596000", "1","ZY"),"all");
			for (WSArchiveManageIntfReturnVo wsArchiveManageIntfReturnVo : rtnVos) {
				String sql3 = "select count(1) count from pv_arch_doc doc inner join bd_arch_doctype doctype on doc.pk_doctype=doctype.code_doctype  where doc.pk_archive=? and  doctype.dt_systype=? and doc.flag_upload='1' ";
				Map<String, Object> countDoc = DataBaseHelper.queryForMap(sql3,
						mapContainer.get(map.get("codePv")),
						wsArchiveManageIntfReturnVo.getCodeSys());
				int archFileCount=0;
				if(!wsArchiveManageIntfReturnVo.getCodeSys().equals("04")){
					for (FileVo fileVo : wsArchiveManageIntfReturnVo.getFiles()) {
						if(fileVo.getArcYN().equals("Y")){archFileCount=archFileCount+1;}
					}
				}else{
					if(archFileCount==0)
					{
						String opSql="select count(*) from PV_ARCH_DOC doc inner join PV_ARCHIVE arch on doc.PK_ARCHIVE=arch.PK_ARCHIVE "+
							     "where arch.PK_PV=? and doc.PK_DOCTYPE='0012'";
						Integer ooFiles=DataBaseHelper.queryForScalar(opSql, Integer.class, map.get("pkPv"));
						archFileCount=ooFiles;
					}
				}
				
				// 各个系统的归档文件数量浏览
				mapResult.put(
						wsArchiveManageIntfReturnVo.getCodeSys(),
						archFileCount
								+ "/"
								+ wsArchiveManageIntfReturnVo
										.getFileTotalCount());
				if(isFitArch.equals("1"))
				{
					if(archFileCount!=Integer.parseInt(wsArchiveManageIntfReturnVo.getFileTotalCount())){
						isFitArch="0";
					}
				}
				
				Integer cnt = Integer
						.parseInt(countDoc.get("count").toString());
				Integer total = Integer.parseInt(wsArchiveManageIntfReturnVo
						.getFileTotalCount());
				if (cnt.compareTo(total) != 0) {
					mapResult.put("flagForce", "1");
				} else {
					mapResult.put("flagForce", "0");
				}
			}
			// 查询扫描文件个数
			boolean IsScan=false;
			String sql4="select isnull(FLAG_ARCH_PAPER,0) from PV_ARCHIVE where PK_PV=?";
			String countSM = DataBaseHelper.queryForScalar(sql4,
					String.class, map.get("pkPv"));
			if(countSM.equals("1")){
				IsScan=true;
			}
			mapResult.put("sca", countSM.toString());
			mapResult.put("codePv", map.get("codePv"));
			mapResult.put("namePi", map.get("namePi"));
			mapResult.put("codePi", map.get("codePi"));
			mapResult.put("pkDept", map.get("pkDept"));
			mapResult.put("pkPv", map.get("pkPv"));
			mapResult.put("codeIp", map.get("codeIp"));
			mapResult.put("pkArchive", mapContainer.get(map.get("codePv")));
			// 住院次数
			mapResult.put("ipTimes",
					mapListIpTimes.get(map.get("codePv")).get("ipTimes")
							.toString());
			// 归档状态（改为只是电子归档，只更新电子归档标志）
			if(isFitArch.equals("1")){
				String pkArch=mapContainer.get(map.get("codePv"));
				updateArchiveNormal(pkArch,false);//只是电子归档第二个参数传false
				mapResult.put("euStatus","已归档");
			}else{
				mapResult.put("euStatus","未归档");
			}
			// 归档状态(电子归档标志有，纸质归档也有，那就更新eu_stutas为'8'，变为总归档的状态)
			if(isFitArch.equals("1") && IsScan){
				String pkArch=mapContainer.get(map.get("codePv"));
				updateArchiveNormal(pkArch,true);//总归档第二个参数传true
			}
			mapListResult.add(mapResult);
			isFitArch="1";
		}
		
		return mapListResult;
	}
	/**
	 * 查询患者的归档情况
	 * @param param
	 * @param user
	 * @return
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	/*public List<Map<String, Object>> queryPatiArchSituation(String param, IUser user) throws IOException, DocumentException {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);

		List<Map<String, Object>> mapListResult = new ArrayList<Map<String, Object>>();

		Map<String, String> mapContainer = new HashMap<String, String>();
		for (PvArchive pvArchive : archiveDto.getPvArchives()) {
			mapContainer.put(pvArchive.getCodePv(), pvArchive.getPkArchive());
		}
		List<Map<String, Object>> mapList = archiveMapper.queryArchManagerCheck(archiveDto);

//		for (PvArchive pvArchive : archiveDto.getPvArchives()) {
//			Map<String, Object> mapResult = new HashMap<String, Object>();
//			for (Map<String, Object> map : mapList) {
//				if (pvArchive.getCodePv().equals(map.get("codePv"))) {
//					mapResult.put("pkPv", map.get("pkPv"));
//					mapResult.put("codePv", map.get("codePv"));
//					mapResult.put("ipTimes", map.get("ipTimes"));
//					mapResult.put("namePi", map.get("namePi"));
//					mapResult.put("sca", map.get("sca"));
//					mapResult.put("euStatus", map.get("euStatus") == null ? null : map.get("euStatus").toString().equals("1") ? "已归档" : "未归档");
//					mapResult.put("pkArchive", pvArchive.getPkArchive());
//					if (map.get("dtSystype") != null) {
//						mapResult.put(map.get("dtSystype").toString(), map.get("cnt") + "/" + map.get("total"));
//						Integer cnt = Integer.parseInt(map.get("cnt").toString());
//						Integer total = Integer.parseInt(map.get("total").toString());
//						// 正常归档条件
//						if (cnt.compareTo(total) == 0 && total != 0) {
//							if (!"0".equals(map.get("sca").toString())) {
//								mapResult.put("flagForce", "0");
//							}
//						}
//					}
//				}
//			}
//			if (mapResult.get("flagForce") == null) {
//				mapResult.put("flagForce", "1");
//			}
//			mapListResult.add(mapResult);
//		}
		for (PvArchive pvArchive : archiveDto.getPvArchives()) {
			
			mapListResult.addAll(getFileInfoByPk(pvArchive.getPkArchive()));
		}
		
		return mapListResult;
	}*/

	
	public List<Map<String,Object>> getFileInfoByPk(String pkArchive) throws IOException, DocumentException{
	
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		//1）调用归档平台接口，查询患者本次就诊的全部文件；
		PvEncounter pv = DataBaseHelper.queryForBean(" select pi.code_pi,pv.* from pv_encounter pv inner join pv_archive pa on pa.pk_pv = pv.pk_pv inner join pi_master pi on pi.pk_pi = pv.pk_pi where pa.pk_archive = ? ", PvEncounter.class, pkArchive);
		Integer ipTimes = DataBaseHelper.queryForScalar(" select ip_times from pv_ip where pk_pv = ? ", Integer.class, pv.getPkPv());
		List<BdArchDoctype> types = DataBaseHelper.queryForList("select * from bd_arch_doctype ", BdArchDoctype.class); 
        for(BdArchDoctype type : types){
        	typeInfo.put(type.getCodeDoctype(), type);
	   }
		ArchQryParamBody body = new ArchQryParamBody();
		body.setVisitcode(pv.getCodePv()+"_1");
		body.setPaticode(ArchUtils.patidHandle(pv.getCodePi()));
		body.setPatiname(pv.getNamePi());
		ArchQryParam param = new ArchQryParam();
		param.setArchQryParamBody(body);
		ArchQueryResult res = archOuterService.queryFile(param);
		List<Fileinfo> files = null;
		if(res!=null && res.getArchResultBody() != null){
			 files = res.getArchResultBody().getFileinfos();
		}
		
		//2）遍历xml文件，读取docname、doctype和path节点，为归档文件建立数据库索引。
		//此处改为如果文件索引中存在相应的记录，则不在做新增操作
         List<PvArchDoc> docs = DataBaseHelper.queryForList(" select doc.PK_DOC,doc.pk_org,doc.PK_ARCHIVE,doc.PK_DOCTYPE,doc.NAME_DOC,doc.POSITION,doc.PAGES,doc.SIZE_DOC,doc.FLAG_UPLOAD,doc.DATE_UPLOAD,"+
        		 				"doc.PK_EMP_UPLOAD,doc.NAME_EMP_UPLOAD,doc.FLAG_VALID,doc.creator,doc.CREATE_TIME,doc.MODIFY_TIME,doc.FLAG_DEL,doc.TS,"+
        		 				"doc.code_rpt,doc.name_rpt,doc.sort_no from pv_arch_doc where pk_archive = ?", PvArchDoc.class, pkArchive);
		 List<String> fileNames = new ArrayList<String>();
		 for(PvArchDoc doc : docs){
			 fileNames.add(doc.getNameDoc());
		 }
		 List<PvArchDoc> docForIns = new ArrayList<PvArchDoc>();
		 if(null != files){
			 for(Fileinfo file : files){
				 if(!fileNames.contains(file.getFilename())){
					 PvArchDoc doc = new PvArchDoc();
					 doc.setPkOrg(UserContext.getUser().getPkOrg());
					 doc.setPkArchive(pkArchive);
					 doc.setNameDoc(file.getFilename());
					 BdArchDoctype type = typeInfo.get(file.getDoctype());
					 doc.setPkDoctype(type==null?"":type.getPkDoctype());
					 doc.setFlagUpload("1");
					 doc.setFlagValid("1");
					 doc.setPages(1);
					 doc.setPosition(file.getFilepath());
					 ApplicationUtils.setDefaultValue(doc, true);
					 docForIns.add(doc);
				 }
			 }
			 DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvArchDoc.class), docForIns);
		 }
		
		//2.
		Map<String,Object> pvMap = new HashMap<String,Object>();
		pvMap.put("pkPv", pv.getPkPv());
		pvMap.put("codePv", pv.getCodePv());
		pvMap.put("ipTimes", ipTimes);
		pvMap.put("namePi",pv.getNamePi());
		pvMap.put("euStatus", pv.getEuStatus() == null ? null : pv.getEuStatus().equals("1") ? "已归档" : "未归档");
		pvMap.put("pkArchive", pkArchive);
		
		//校验HIS病历(包括护理部分)，根据文件类型中设置的必须文档的数量判定；
		
		String hisSql = "	select count(1)  cnt_total, sum(case when doc.flag_upload='1' then 1 else 0 end) cnt_arch,sum(case when dtp.eu_doctype='1' then 1 else 0 end) sca"
						+" from bd_arch_doctype dtp left outer join pv_arch_doc doc on dtp.c_doctype=doc.pk_doctype"
						+" where dtp.dt_systype='00' and dtp.flag_nec='1' and doc.pk_archive=?";
		Map<String,Object> HISInfo = DataBaseHelper.queryForMap(hisSql, pkArchive);
		Integer cnt = HISInfo.get("cntArch")==null?0:Integer.parseInt(HISInfo.get("cntArch").toString());
		Integer total = HISInfo.get("cntTotal")==null?0:Integer.parseInt(HISInfo.get("cntTotal").toString());
		Integer sca = HISInfo.get("sca")==null?0:Integer.parseInt(HISInfo.get("sca").toString());
		Map<String,Object> HISMAP = new HashMap<String,Object>();
		HISMAP.putAll(pvMap);
		HISMAP.put("sca", sca);
		HISMAP.put("00", cnt+"/"+total);
		// 正常归档条件
		if (cnt.compareTo(total) == 0 && total != 0) {
			if (!"0".equals(HISMAP.get("sca").toString())) {
				HISMAP.put("flagForce", "0");
			}
		}else{
			HISMAP.put("flagForce", "1");
		}
		result.add(HISMAP);
		
		
	
		
		
		//校验HIS病历(包括护理部分)，根据文件类型中设置的必须文档的数量判定；
		
		String EMRSql = "	select count(1)  cnt_total, sum(case when doc.flag_upload='1' then 1 else 0 end) cnt_arch,sum(case when dtp.eu_doctype='1' then 1 else 0 end) sca"
						+" from bd_arch_doctype dtp left outer join pv_arch_doc doc on dtp.pk_doctype=doc.pk_doctype"
						+" where dtp.dt_systype='01' and dtp.flag_nec='1' and doc.pk_archive=?";
		Map<String,Object> EMRInfo = DataBaseHelper.queryForMap(EMRSql, pkArchive);
		Integer cntEMR = EMRInfo.get("cntArch")!=null?Integer.parseInt(EMRInfo.get("cntArch").toString()):0;
		Integer totalEMR = EMRInfo.get("cntTotal")!=null?Integer.parseInt(EMRInfo.get("cntTotal").toString()):0;
		Integer scaEMR = EMRInfo.get("sca")!=null?Integer.parseInt(EMRInfo.get("sca").toString()):0;
		Map<String,Object> EMRMAP = new HashMap<String,Object>();
		EMRMAP.putAll(pvMap);
		HISMAP.put("01", cntEMR+"/"+totalEMR);
		// 正常归档条件
		if (cntEMR.compareTo(totalEMR) == 0 ) {
			if (EMRMAP.get("sca")!=null&&!"0".equals(EMRMAP.get("sca").toString())) {
				HISMAP.put("flagForce", "0");
			}
		}else{
			HISMAP.put("flagForce", "1");
		}
		//result.add(EMRMAP);
		
		
		
//		3）	校验检查报告，根据有效的检查申请单的数量判定；
		 Integer totalPACS= DataBaseHelper.queryForScalar(" select count(1) from cn_order ord inner join cn_ris_apply ris  on ord.pk_cnord=ris.pk_cnord  where ord.flag_erase='0' and ord.pk_pv=?", Integer.class, pv.getPkPv());
		 Integer cntPACS = DataBaseHelper.queryForScalar(" select count(1)  from pv_arch_doc doc inner join bd_arch_doctype dtp on doc.pk_doctype=dtp.pk_doctype where dtp.dt_systype='03' and doc.pk_archive=?", Integer.class, pkArchive);
		  Map<String,Object> PACSMAP = new HashMap<String,Object>();
		  PACSMAP.putAll(pvMap);
		  HISMAP.put("02", cntPACS+"/"+totalPACS);
		  if (cntPACS.compareTo(totalPACS) == 0) {
			  HISMAP.put("flagForce", "0");
			}else{
				HISMAP.put("flagForce", "1");
			}
		//  result.add(PACSMAP);
		  
		
//		4）	校验检验报告，根据标本编号的数量判定；
		  
		  Integer totalLIS= DataBaseHelper.queryForScalar("select count(distinct lab.samp_no) from cn_order ord  inner join cn_lab_apply lab on ord.pk_cnord=lab.pk_cnord  where ord.flag_erase='0' and  ord.pk_pv=?", Integer.class, pv.getPkPv());
			 Integer cntLIS = DataBaseHelper.queryForScalar(" select count(1)  from pv_arch_doc doc inner join bd_arch_doctype dtp on doc.pk_doctype=dtp.pk_doctype  where dtp.dt_systype='02' and  doc.pk_archive=?", Integer.class, pkArchive);
			  Map<String,Object> LISMAP = new HashMap<String,Object>();
			  LISMAP.putAll(pvMap);
			  HISMAP.put("03", cntLIS+"/"+totalLIS);
			  if (cntLIS.compareTo(totalLIS) == 0 ) {
				  HISMAP.put("flagForce", "0");
				}else{
					HISMAP.put("flagForce", "1");
				}
			//  result.add(LISMAP);
		
		
//		5）	校验手术文档，根据手术申请的数量和必须的手术文档数量判定；
			  
			  Integer totalOP= DataBaseHelper.queryForScalar("select count(1) from cn_order ord inner join cn_op_apply opt on ord.pk_cnord=opt.pk_cnord cross join bd_arch_doctype dtp where ord.flag_erase='0' and  dtp.dt_systype='04' and   ord.pk_pv=?", Integer.class, pv.getPkPv());
				 Integer cntOP = DataBaseHelper.queryForScalar("select count(1)  from pv_arch_doc doc inner join bd_arch_doctype dtp on doc.pk_doctype=dtp.pk_doctype where dtp.dt_systype='04' and  doc.pk_archive=?", Integer.class, pkArchive);
				  Map<String,Object> OPMAP = new HashMap<String,Object>();
				  OPMAP.putAll(pvMap);
				  HISMAP.put("04", cntOP+"/"+totalOP);
				  if (cntOP.compareTo(totalOP) == 0 ) {
					  HISMAP.put("flagForce", "0");
					}else{
						HISMAP.put("flagForce", "1");
					}
				//  result.add(OPMAP);
		
//		6）	校验扫描文档，根据是否上传了扫描文档来判定；
//		--扫描文档
//		select count(1)
//		  from pv_arch_doc doc
//		       inner join bd_arch_doctype dtp on doc.pk_doctype=dtp.pk_doctype
//		 where dtp.eu_doctype='1' and
//		       doc.pk_archive=?
//		如果应归档的文件已全部建立索引，即可进行正常归档处理，否则不能归档，或必须强制归档。

		
		
		
		
		
	    return result;
		
	}
	
	/**
	 * 调用webService方法查询归档文件信息
	 * @param wSArchiveManageIntfParamVo
	 * @return
	 */
	private List<WSArchiveManageIntfReturnVo> getInfoFromWebService(WSArchiveManageIntfParamVo wSArchiveManageIntfParamVo,String type) {

		List<WSArchiveManageIntfReturnVo> vos = new ArrayList<WSArchiveManageIntfReturnVo>();
		try {
			//暂时屏蔽第三方检查检验，只留手麻
			/*if(type.equals("06") || type.equals("all")){
				MathService mathService = new MathService(new URL(mapWebServiceUri.get("02")));
				String resultPacs = mathService.getMathServiceSoap().pacsOrigFileList(wSArchiveManageIntfParamVo.getPatient_id(),
						wSArchiveManageIntfParamVo.getTimes(), wSArchiveManageIntfParamVo.getVisit_flag());
				WSArchiveManageIntfReturnVo voPacs = ArchUtil.parseToWSArchiveManageIntfReturnVo(resultPacs);
				voPacs.setCodeSys("06");
				vos.add(voPacs);
			}
			
			if(type.equals("02") || type.equals("all")){
				com.zebone.nhis.webservice.client.zhongshan.lis.Service s = new com.zebone.nhis.webservice.client.zhongshan.lis.Service(new URL(mapWebServiceUri.get("03")));
				String resultLis = s.getServiceSoap().lisOrigFileList(wSArchiveManageIntfParamVo.getPatient_id(), wSArchiveManageIntfParamVo.getTimes(),
						wSArchiveManageIntfParamVo.getVisit_flag());
				WSArchiveManageIntfReturnVo voLis = ArchUtil.parseToWSArchiveManageIntfReturnVo(resultLis);
				voLis.setCodeSys("02");
				vos.add(voLis);
			}*/
			
			if(type.equals("04") || type.equals("all")){
				DocareTrunDataService docareTrunDataService = new DocareTrunDataService(new URL(mapWebServiceUri.get("04")));
				String resultOp = docareTrunDataService.getDocareTrunDataServiceSoap().opOrigFileList(wSArchiveManageIntfParamVo.getPatient_id(),
						wSArchiveManageIntfParamVo.getTimes(), wSArchiveManageIntfParamVo.getVisit_flag());
				if(!CommonUtils.isEmptyString(resultOp))
				{
					WSArchiveManageIntfReturnVo voOp = ArchUtil.parseToWSArchiveManageIntfReturnVo(resultOp);
					voOp.setCodeSys("04");
					vos.add(voOp);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (BusException e) {
			throw new BusException(e.getMessage());
		}

		return vos;
	}

	/**
	 * 查询webService服务地址
	 */
	private void initWebServiceUrl() {

		String sql = "select code,url_srv from bd_arch_srvconf";
		List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sql, new Object[] {});
		for (Map<String, Object> map : mapList) {
			mapWebServiceUri.put(map.get("code").toString(), map.get("urlSrv").toString());
		}
	}

	/**
	 * 归档处理
	 * @param param
	 * @param user
	 * @return
	 */
	public int updateArchive(String param, IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		User u = (User) user;
		List<PvArchiveVO> pvArchives = archiveDto.getPvArchives();
		List<PvArchive> pvArchiveList = new ArrayList<PvArchive>();
		for (PvArchiveVO pvArchive : pvArchives) {
			pvArchive.setEuArchtype(pvArchive.getFlagForce());
			pvArchive.setFlagArch(EnumerateParameter.ONE);
			pvArchive.setDateArch(new Date());
			pvArchive.setPkEmpArch(u.getPkEmp());
			pvArchive.setNameEmpArch(u.getNameEmp());
			pvArchive.setEuStatus(EnumerateParameter.EIGHT);
			
			PvArchive archive = new PvArchive();
			ApplicationUtils.copyProperties(archive, pvArchive);
			ApplicationUtils.setDefaultValue(archive, false);
			pvArchiveList.add(archive);
		}
		String sql = "update pv_archive set eu_archtype=:euArchtype,flag_arch=:flagArch,date_arch=:dateArch,pk_emp_arch=:pkEmpArch,name_emp_arch=:nameEmpArch,eu_status=:euStatus,ts=:ts where pk_archive=:pkArchive";
		int[] result = DataBaseHelper.batchUpdate(sql, pvArchiveList);
		return result.length;
	}
	/**
	 * 归档处理---此方法为正常归档时改变归档状态的方法
	 * @param param
	 * @param user
	 * @return
	 */
	public void updateArchiveNormal(String pkArch,boolean IsAllArch) {
		User u = new User();
		String sql="";
		if(IsAllArch){
			sql="update PV_ARCHIVE set EU_ARCHTYPE='0',FLAG_ARCH='1',DATE_ARCH=?,PK_EMP_ARCH=?,NAME_EMP_ARCH=?,EU_STATUS='8' where PK_ARCHIVE=?";
		}else{
			sql="update PV_ARCHIVE set EU_ARCHTYPE='0',FLAG_ARCH='1',DATE_ARCH=?,PK_EMP_ARCH=?,NAME_EMP_ARCH=? where PK_ARCHIVE=?";
		}
		
		DataBaseHelper.update(sql, new Object[]{new Date(),u.getPkEmp(),u.getNameEmp(),pkArch});
	}
	/**
	 * 解除归档
	 * @param param
	 * @param user
	 * @return
	 */
	public int updateRemoveArchive(String param, IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		String pkArchive = archiveDto.getPkArchive();
		String sql = "update pv_archive set eu_archtype='0',flag_arch='0',date_arch=null,pk_emp_arch=null,name_emp_arch=null,eu_status='0',ts=? where pk_archive=?";

		return DataBaseHelper.execute(sql, new Date(), pkArchive);
	}

	/**
	 * 查询归档文件
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryArchManagerDoc(String param, IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, HashMap.class);
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>(); 
		res = archiveMapper.queryArchManagerDoc(map);
		if(map.containsKey("codeOp")){
			res.addAll(archiveMapper.queryArchManagerDocOp(map));
		}
		
		if(map.containsKey("isArchMgr")){
			res.clear();
			res = archiveMapper.queryArchMgrDoc(map);
		}
		return res;
	}
	
	
	public List<Map<String,Object>> qryFilesByPv(String param,IUser user){
		Map<String,Object> para = JsonUtil.readValue(param, HashMap.class);
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>(); 
		String pkPv= (String)para.get("pkPv");
		try{
			res = archOuterService.queryFileByPkPv(pkPv);
		}catch(Exception e){
			res =  null;
		}
		return res;
	}
	/**
	 * 查看归档的纸质分类文件
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PvArchDoc> qryPaperDocByPv(String param,IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		String pkPv=map.get("pkPv").toString();
		String pkDocType="";
		if(map.get("pkDocType")!=null){
			pkDocType=map.get("pkDocType").toString();
		}
		List<PvArchDoc> docList=archiveMapper.qryDocByPv(pkPv,pkDocType);
		return docList;
	}
	/**
	 * 根据系统类型查询所有的文件状态列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ArchDocSys> hisOrEmrList=new ArrayList<ArchDocSys>();
	public List<ArchDocSys> qryArchDocBySysType(String param,IUser user){
		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		List<ArchDocSys> fileList=new ArrayList<ArchDocSys>();
		if(archiveDto==null){return null;}
		if(archiveDto.getCodeSys().equals("00")){
			hisOrEmrList.clear();
			getHisCount(archiveDto.getPkArchive(),null,archiveDto.getCodePv());
			fileList=hisOrEmrList;
		}else if(archiveDto.getCodeSys().equals("01")){
			hisOrEmrList.clear();
			getEmrCount(archiveDto.getPkArchive(),null,archiveDto.getCodePv());
			fileList=hisOrEmrList;
		}else if(archiveDto.getCodeSys().equals("02")){
			List<WSArchiveManageIntfReturnVo> rtnVos = getInfoFromWebService(new WSArchiveManageIntfParamVo(
					archiveDto.getCodePi(), archiveDto.getIpTimes(),"ZY"),"02");
			fileList=queryfiles(rtnVos);
		}else if(archiveDto.getCodeSys().equals("04")){
			/*List<WSArchiveManageIntfReturnVo> rtnVos = getInfoFromWebService(new WSArchiveManageIntfParamVo(
					archiveDto.getCodePi(), archiveDto.getIpTimes(),"ZY"),"04");
			fileList=queryfiles(rtnVos);*/
			fileList=getOpOccDoc(archiveDto.getPkPv());
		}else if(archiveDto.getCodeSys().equals("06")){
			List<WSArchiveManageIntfReturnVo> rtnVos = getInfoFromWebService(new WSArchiveManageIntfParamVo(
					archiveDto.getCodePi(), archiveDto.getIpTimes(),"ZY"),"06");
			fileList=queryfiles(rtnVos);
		}else if(archiveDto.getCodeSys().equals("10")){
			List<ArchDocSys> risApplyList = getrisApplyDoc(archiveDto.getPkPv());
			fileList=risApplyList;
		}else if(archiveDto.getCodeSys().equals("11")){
			List<ArchDocSys> risOccList = getrisOccDoc(archiveDto.getPkPv());
			fileList=risOccList;
		}else if(archiveDto.getCodeSys().equals("12")){
			List<ArchDocSys> labApplyList = getlabApplyDoc(archiveDto.getPkPv());
			fileList=labApplyList;
		}else if(archiveDto.getCodeSys().equals("13")){
			List<ArchDocSys> labOccList = getlabOccDoc(archiveDto.getPkPv());
			fileList=labOccList;
		}
		return fileList;
	}
	/**
	 * 根据系统编号查询第三方接口的文件信息
	 * @param rtnVos
	 * @return
	 */
	public List<ArchDocSys> queryfiles(List<WSArchiveManageIntfReturnVo> rtnVos){
		List<ArchDocSys> fileList=new ArrayList<ArchDocSys>();
		if(rtnVos==null){return null;}
		for (WSArchiveManageIntfReturnVo wsArchiveManageIntfReturnVo : rtnVos) {
			if(wsArchiveManageIntfReturnVo.getFiles()!=null){
				for (FileVo fileVo : wsArchiveManageIntfReturnVo.getFiles()) {
					ArchDocSys archDoc=new ArchDocSys();
					archDoc.setNameDoctype(fileVo.getName());
					archDoc.setDocSpcode(wsArchiveManageIntfReturnVo.getPatId());
					archDoc.setNameDoc(fileVo.getFileName());
					archDoc.setStatus(fileVo.getArcYN().equals("Y")?"是":"否");
					fileList.add(archDoc);
				}
			}
		}
		return fileList;
	}
	
	public String getHisCount(String pkArchive,String pkPv,String codePv){
		int histotal=0;
		int hisDocCount=0;
		String recontent="";//声明返回的内容
		String isEustatus="0";
		List<String> hisDocList=archiveMapper.queryArchDocList("00", pkArchive);
		String pkSql="select PK_PV from PV_ENCOUNTER where CODE_PV=? and del_flag='0'";
		if(pkPv==null){
			pkPv=DataBaseHelper.queryForScalar(pkSql, String.class, codePv);
		}
		//校验HIS系统文件数量(1)查询长期医嘱数量
		String ordAlways="select count(1) from CN_ORDER where PK_PV=? and DEL_FLAG='0' and FLAG_DOCTOR='1' and EU_ALWAYS='1'";
		String countAlways=DataBaseHelper.queryForScalar(ordAlways,String.class, pkPv);
		if(Integer.parseInt(countAlways)>0){
			ArchDocSys archDoc=new ArchDocSys();
			archDoc.setNameDoctype("长期医嘱单");
			archDoc.setDocSpcode("ORD_L");
			archDoc.setNameDoc("HIS_ORD_L"+codePv);
			if(hisDocList.contains("0028")){
				hisDocCount=hisDocCount+1;
				archDoc.setStatus("是");
			}else{
				archDoc.setStatus("否");
			}
			histotal=histotal+1;
			hisOrEmrList.add(archDoc);
		}
		//校验HIS系统文件数量(2)查询临时医嘱数量
		String ordOnce="select count(1) from CN_ORDER where PK_PV=? and DEL_FLAG='0' and FLAG_DOCTOR='1' and EU_ALWAYS='0'";
		String countOnce=DataBaseHelper.queryForScalar(ordOnce,String.class, pkPv);
		if(Integer.parseInt(countOnce)>0){
			ArchDocSys archDoc=new ArchDocSys();
			archDoc.setNameDoctype("临时医嘱单");
			archDoc.setDocSpcode("ORD_T");
			archDoc.setNameDoc("HIS_ORD_T"+codePv);
			if(hisDocList.contains("0029")){
				hisDocCount=hisDocCount+1;
				archDoc.setStatus("是");
			}else{
				archDoc.setStatus("否");
			}
			histotal=histotal+1;
			hisOrEmrList.add(archDoc);
		}
		//校验HIS系统文件数量(3)查询声明体征数据
		String vts="select count(1) from ex_vts_occ where PK_PV=? and DEL_FLAG='0'";
		String countvts=DataBaseHelper.queryForScalar(vts,String.class, pkPv);
		if(Integer.parseInt(countvts)>0){
			ArchDocSys archDoc=new ArchDocSys();
			archDoc.setNameDoctype("体温单");
			archDoc.setDocSpcode("TWD");
			archDoc.setNameDoc("HIS_TWD"+codePv);
			if(hisDocList.contains("0030")){
				hisDocCount=hisDocCount+1;
				archDoc.setStatus("是");
			}else{
				archDoc.setStatus("否");
			}
			histotal=histotal+1;
			hisOrEmrList.add(archDoc);
		}
		//校验HIS系统文件数量(4)查询护理记录数据
		String nd="select count(1) from ND_RECORD where PK_PV=?  and isnull(DEL_FLAG,'0')='0'";
		String countnd=DataBaseHelper.queryForScalar(nd,String.class, pkPv);
		if(Integer.parseInt(countnd)>0){
			ArchDocSys archDoc=new ArchDocSys();
			archDoc.setNameDoctype("护理记录单");
			archDoc.setDocSpcode("HLJLD");
			archDoc.setNameDoc("HIS_HLJLD"+codePv);
			if(hisDocList.contains("0019")){
				hisDocCount=hisDocCount+1;
				archDoc.setStatus("是");
			}else{
				archDoc.setStatus("否");
			}
			histotal=histotal+1;
			hisOrEmrList.add(archDoc);
		}
		if(histotal!=0 && (hisDocCount==histotal || hisDocCount>histotal)){
			isEustatus="1";
		}
		recontent=hisDocCount+"/"+histotal+"_"+isEustatus;
		
		return recontent;
	}
	
	public String getEmrCount(String pkArchive,String pkPv,String codePv){

		//查询数据库文档类型
		int emrtotal=0;//总文件数
		int emrDocCount=0;//上传的文件数
		String recontent="";//声明返回的内容
		String isEustatus="0";
		String pkSql="select PK_PV from PV_ENCOUNTER where CODE_PV=? and del_flag='0'";
		if(pkPv==null){
			pkPv=DataBaseHelper.queryForScalar(pkSql, String.class, codePv);
		}
		String ArchDoctypeSql="select * from bd_arch_doctype where DT_SYSTYPE='01' and CODE_MAP is not null and FLAG_DEL='0' order by CODE_DOCTYPE";
		List<BdArchDoctype> archDocList=DataBaseHelper.queryForList(ArchDoctypeSql, BdArchDoctype.class);
		//查询已经书写的病历类型文件
		List<String> recTypeList=archiveMapper.queryRecTypeList(pkPv);
		//查询已经上传的文档类型
		List<String> emrDocList=archiveMapper.queryArchDocList("01", pkArchive);
		for (BdArchDoctype bdArchDoctype : archDocList) {
			if(bdArchDoctype.getCodeDoctype().equals("0001")){//住院首页
				ArchDocSys archDoc=new ArchDocSys();
				if(recTypeList.contains("080001")){
					archDoc.setNameDoctype("住院病历首页");
					archDoc.setDocSpcode("ZYBLSY");
					archDoc.setNameDoc("EMR_ZYBLSY"+codePv);
					emrtotal=emrtotal+1;
				}
				if(emrDocList.contains("0001")){
					emrDocCount=emrDocCount+1;
					archDoc.setStatus("是");
				}else{
					archDoc.setStatus("否");
				}
				if(archDoc.getNameDoctype()!=null){hisOrEmrList.add(archDoc);}
			}
			if(bdArchDoctype.getCodeDoctype().equals("0002")){//入院记录
				ArchDocSys archDoc=new ArchDocSys();
				if(recTypeList.contains("090001") || recTypeList.contains("090002")){
					archDoc.setNameDoctype("入院记录");
					archDoc.setDocSpcode("RYJL");
					archDoc.setNameDoc("EMR_RYJL"+codePv);
					emrtotal=emrtotal+1;
				}
				if(emrDocList.contains("0002")){
					emrDocCount=emrDocCount+1;
					archDoc.setStatus("是");
				}else{
					archDoc.setStatus("否");
				}
				if(archDoc.getNameDoctype()!=null){hisOrEmrList.add(archDoc);}
			}
			if(bdArchDoctype.getCodeDoctype().equals("0003")){//首次病程记录
				ArchDocSys archDoc=new ArchDocSys();
				if(recTypeList.contains("100001")){
					emrtotal=emrtotal+1;
					archDoc.setNameDoctype("病程记录");
					archDoc.setDocSpcode("BCJL");
					archDoc.setNameDoc("EMR_BCJL"+codePv);
				}
				if(emrDocList.contains("0003")){
					emrDocCount=emrDocCount+1;
					archDoc.setStatus("是");
				}else{
					archDoc.setStatus("否");
				}
				if(archDoc.getNameDoctype()!=null){hisOrEmrList.add(archDoc);}
			}
			if(bdArchDoctype.getCodeDoctype().equals("0004")){//出院记录
				ArchDocSys archDoc=new ArchDocSys();
				if(recTypeList.contains("120001")){
					emrtotal=emrtotal+1;
					archDoc.setNameDoctype("出院记录");
					archDoc.setDocSpcode("CYJL");
					archDoc.setNameDoc("EMR_CYJL"+codePv);
				}
				if(emrDocList.contains("0004")){
					emrDocCount=emrDocCount+1;
					archDoc.setStatus("是");
				}else{
					archDoc.setStatus("否");
				}
				if(archDoc.getNameDoctype()!=null){hisOrEmrList.add(archDoc);}
			}
			if(bdArchDoctype.getCodeDoctype().equals("0007")){//手术记录
				ArchDocSys archDoc=new ArchDocSys();
				if(recTypeList.contains("050102")){
					emrtotal=emrtotal+1;
					archDoc.setNameDoctype("手术记录");
					archDoc.setDocSpcode("SSJL");
					archDoc.setNameDoc("EMR_SSJL"+codePv);
				}
				if(emrDocList.contains("0007")){
					emrDocCount=emrDocCount+1;
					archDoc.setStatus("是");
				}else{
					archDoc.setStatus("否");
				}
				if(archDoc.getNameDoctype()!=null){hisOrEmrList.add(archDoc);}
			}
			if(bdArchDoctype.getCodeDoctype().equals("0010")){//会诊记录
				ArchDocSys archDoc=new ArchDocSys();
				if(recTypeList.contains("100009")){
					emrtotal=emrtotal+1;
					archDoc.setNameDoctype("会诊记录");
					archDoc.setDocSpcode("HZJL");
					archDoc.setNameDoc("EMR_HZJL"+codePv);
				}
				if(emrDocList.contains("0010")){
					emrDocCount=emrDocCount+1;
					archDoc.setStatus("是");
				}else{
					archDoc.setStatus("否");
				}
				if(archDoc.getNameDoctype()!=null){hisOrEmrList.add(archDoc);}
			}
		}
		if(emrtotal!=0 && (emrDocCount==emrtotal || emrDocCount>emrtotal)){
			isEustatus="1";
		}
		recontent=emrDocCount+"/"+emrtotal+"_"+isEustatus;
		return recontent;
	}
	
	public String getRisApplyCount(String pkPv){
		String sql="select isnull(sum(case when occ.code_rpt is null then 0 else 1 end),0) count,count(*) total from cn_order cnord "+
					"inner join cn_ris_apply ris on cnord.pk_cnord=ris.pk_cnord "+
					"inner join bd_ord bdord on cnord.pk_ord=bdord.pk_ord "+
					"left join ex_ris_occ occ on cnord.pk_cnord=occ.pk_cnord "+
					"where cnord.pk_pv=? "+
					"and cnord.eu_status_ord<>9 and cnord.eu_status_ord<>0 and cnord.del_flag='0' and bdord.eu_archtype='2'";// and bdord.eu_archtype='2'
		Map<String,Object> map =DataBaseHelper.queryForMap(sql, pkPv);
		Integer count =Integer.parseInt(map.get("count").toString());
		Integer total =Integer.parseInt(map.get("total").toString());
		String result="";
		if(count==total){
			result=count+"/"+total+"_1";
		}else{
			result=count+"/"+total+"_0";
		}
		
		return result;
	}
	public String getRisOccCount(String pkPv){
		/*String sql="select isnull(sum (case when doc.CODE_RPT is null then 0 else 1 end),0) count,count(*) total from cn_order cnord "+
					"inner join cn_ris_apply ris on cnord.pk_cnord=ris.pk_cnord "+
					"inner join BD_ORD bdord on cnord.PK_ORD=bdord.PK_ORD "+
					"inner join ex_ris_occ occ on cnord.pk_cnord=occ.pk_cnord "+
					"left join PV_ARCH_DOC doc on occ.CODE_RPT=doc.CODE_RPT "+
					"where cnord.PK_PV=? "+
					"and cnord.EU_STATUS_ORD<>9 and cnord.EU_STATUS_ORD<>0 and cnord.DEL_FLAG='0'  and bdord.eu_archtype='2'";// and  bdord.eu_archtype='2'*/		
		String sql="select (select count(distinct doc.CODE_RPT) from PV_ARCHIVE arch,PV_ARCH_DOC doc,ex_ris_occ occ2 where arch.PK_PV=pv.PK_PV and arch.PK_ARCHIVE=doc.PK_ARCHIVE and doc.CODE_RPT=occ2.code_rpt and occ2.PK_PV=pv.PK_PV) count, "+
				   "((select count(distinct occ.CODE_RPT)  from ex_ris_occ occ where occ.PK_PV=pv.PK_PV  )) total "+
				   "from PV_ENCOUNTER pv where pv.PK_PV=?";
		Map<String,Object> map =DataBaseHelper.queryForMap(sql, pkPv);
		Integer count =Integer.parseInt(map.get("count").toString());
		Integer total =Integer.parseInt(map.get("total").toString());
		String result="";
		if(count==total){
			result=count+"/"+total+"_1";
		}else{
			result=count+"/"+total+"_0";
		}
		
		return result;
	}
	public String getLabApplyCount(String pkPv){
		String sql="select isnull(sum(case when occ.code_rpt is null then 0 else 1 end),0) count,count(*) total from cn_order cnord "+
					"inner join cn_lab_apply lab on cnord.pk_cnord=lab.pk_cnord "+
					"inner join bd_ord bdord on cnord.pk_ord=bdord.pk_ord "+
					"left join ex_lab_occ occ on cnord.pk_cnord=occ.pk_cnord "+
					"where cnord.pk_pv=? "+
					"and cnord.eu_status_ord<>9 and cnord.eu_status_ord<>0 and cnord.del_flag='0'  and bdord.eu_archtype='2'";// and  bdord.eu_archtype='2'
		Map<String,Object> map =DataBaseHelper.queryForMap(sql, pkPv);
		Integer count =Integer.parseInt(map.get("count").toString());
		Integer total =Integer.parseInt(map.get("total").toString());
		String result="";
		if(count==total){
			result=count+"/"+total+"_1";
		}else{
			result=count+"/"+total+"_0";
		}
		
		return result;
	}
	public String getLabOccCount(String pkPv){
		/*String sql="select isnull(sum (case when doc.code_rpt is null then 0 else 1 end),0) count,count(*) total "
				+ "from cn_order cnord "+
					"inner join cn_lab_apply lab on cnord.pk_cnord=lab.pk_cnord "+
					"inner join bd_ord bdord on cnord.pk_ord=bdord.pk_ord "+
					"inner join ex_lab_occ occ on cnord.pk_cnord=occ.pk_cnord "+
					"left join pv_arch_doc doc on occ.code_rpt=doc.code_rpt "+
					"where cnord.pk_pv=? "+
					"and cnord.eu_status_ord<>9 and cnord.eu_status_ord<>0 and cnord.del_flag='0'  and bdord.eu_archtype='2'";// and  bdord.eu_archtype='2'*/		
		String sql="select (select count(distinct doc.CODE_RPT) from PV_ARCHIVE arch,PV_ARCH_DOC doc,ex_lab_occ occ2 where arch.PK_PV=pv.PK_PV and arch.PK_ARCHIVE=doc.PK_ARCHIVE and doc.CODE_RPT=occ2.code_rpt and occ2.PK_PV=pv.PK_PV) count, "+
				   "((select count(distinct occ.CODE_RPT)  from EX_LAB_OCC occ where occ.PK_PV=pv.PK_PV  )) total "+
				   "from PV_ENCOUNTER pv where pv.PK_PV=?";
		Map<String,Object> map =DataBaseHelper.queryForMap(sql, pkPv);
		Integer count =Integer.parseInt(map.get("count").toString());
		Integer total =Integer.parseInt(map.get("total").toString());
		String result="";
		if(count==total){
			result=count+"/"+total+"_1";
		}else{
			result=count+"/"+total+"_0";
		}
		
		return result;
	}
	public List<ArchDocSys> getrisApplyDoc(String pkPv){
		String sql="select occ.DATE_RPT DATE_UPLOAD,occ.CODE_RPT DocSpcode,cnord.NAME_ORD nameDoc,(case when occ.CODE_RPT is not null then '是' else '否' end ) status from cn_order cnord "+
					"inner join cn_ris_apply ris on cnord.pk_cnord=ris.pk_cnord "+
					"inner join bd_ord bdord on cnord.pk_ord=bdord.pk_ord "+
					"left join ex_ris_occ occ on cnord.pk_cnord=occ.pk_cnord "+
					"where cnord.pk_pv=? "+
					"and cnord.eu_status_ord<>9 and cnord.eu_status_ord<>0 and cnord.del_flag='0' and bdord.eu_archtype='2'";// and  bdord.eu_archtype='2'
		return DataBaseHelper.queryForList(sql,ArchDocSys.class, pkPv);
	}
	public List<ArchDocSys> getrisOccDoc(String pkPv){
		String sql="select doc.NAME_DOC nameDoctype,doc.CODE_RPT DocSpcode,doc.DATE_UPLOAD,cnord.NAME_ORD  nameDoc,(case when doc.CODE_RPT is not null then '是' else '否' end ) status from cn_order cnord "+
					"inner join cn_ris_apply ris on cnord.pk_cnord=ris.pk_cnord "+
					"inner join BD_ORD bdord on cnord.PK_ORD=bdord.PK_ORD "+
					"inner join ex_ris_occ occ on cnord.pk_cnord=occ.pk_cnord "+
					"left join PV_ARCH_DOC doc on occ.CODE_RPT=doc.CODE_RPT "+
					"where cnord.PK_PV=? "+
					"and cnord.EU_STATUS_ORD<>9 and cnord.EU_STATUS_ORD<>0 and cnord.DEL_FLAG='0' and bdord.eu_archtype='2'";// and  bdord.eu_archtype='2'
		return DataBaseHelper.queryForList(sql,ArchDocSys.class, pkPv);
	}
	public List<ArchDocSys> getlabApplyDoc(String pkPv){
		String sql="select occ.DATE_RPT DATE_UPLOAD,occ.CODE_RPT DocSpcode,cnord.NAME_ORD nameDoc,(case when occ.CODE_RPT is not null then '是' else '否' end ) status from cn_order cnord "+
					"inner join cn_lab_apply lab on cnord.pk_cnord=lab.pk_cnord "+
					"inner join bd_ord bdord on cnord.pk_ord=bdord.pk_ord "+
					"left join ex_lab_occ occ on cnord.pk_cnord=occ.pk_cnord "+
					"where cnord.pk_pv=? "+
					"and cnord.eu_status_ord<>9 and cnord.eu_status_ord<>0 and cnord.del_flag='0' and bdord.eu_archtype='2'";// and  bdord.eu_archtype='2' "+
		return DataBaseHelper.queryForList(sql,ArchDocSys.class, pkPv);
	}
	public List<ArchDocSys> getlabOccDoc(String pkPv){
		String sql="select doc.NAME_DOC nameDoctype,doc.CODE_RPT DocSpcode,doc.DATE_UPLOAD, cnord.NAME_ORD  nameDoc,(case when doc.CODE_RPT is not null then '是' else '否' end ) status from cn_order cnord "+
					"inner join cn_lab_apply lab on cnord.pk_cnord=lab.pk_cnord "+
					"inner join bd_ord bdord on cnord.pk_ord=bdord.pk_ord "+
					"inner join ex_lab_occ occ on cnord.pk_cnord=occ.pk_cnord "+
					"left join pv_arch_doc doc on occ.code_rpt=doc.code_rpt "+
					"where cnord.pk_pv=? "+
					"and cnord.eu_status_ord<>9 and cnord.eu_status_ord<>0 and cnord.del_flag='0' and bdord.eu_archtype='2'";//
		return DataBaseHelper.queryForList(sql,ArchDocSys.class, pkPv);
	}
	public List<ArchDocSys> getOpOccDoc(String pkPv){
		String sql="select doc.NAME_DOC nameDoctype,doc.CODE_RPT DocSpcode,doc.DATE_UPLOAD, doc.NAME_RPT  nameDoc,(case when doc.CODE_RPT is not null then '是' else '否' end ) status from pv_arch_doc doc "
				+ " inner join PV_ARCHIVE arch on doc.PK_ARCHIVE=arch.PK_ARCHIVE "+
				"where arch.PK_PV=? and doc.PK_DOCTYPE='0012'";
		return DataBaseHelper.queryForList(sql,ArchDocSys.class, pkPv);
	}
}
