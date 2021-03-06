package com.zebone.nhis.arch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.arch.service.ArchOuterService;
import com.zebone.nhis.common.arch.service.ArchiveCollectService;
import com.zebone.nhis.common.arch.vo.ArchResultUp;
import com.zebone.nhis.common.arch.vo.ArchUploadParam;
import com.zebone.nhis.common.arch.vo.HISFileInfo4Upd;
import com.zebone.nhis.common.arch.vo.PatiInfo;
import com.zebone.nhis.common.module.arch.ArchPv;
import com.zebone.nhis.common.module.arch.BdArchDoctype;
import com.zebone.nhis.common.module.arch.PvArchDoc;
import com.zebone.nhis.common.module.arch.PvArchive;
import com.zebone.nhis.common.module.emr.mgr.EmrOperateLogs;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.support.ArchUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.FTPUtil;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ArchiveUploadHandler {
	

	
	private static final String SUFFIX = ".pdf";
	private static final String HIS = "00";
	private static final String LIS = "03";
	private static final String PACS = "2";
	private static final String OP = "3";
	private static final String ICU = "4";
	@Autowired
	private ArchiveCollectService service;
	@Autowired
	private LisPathService lisPathService;
	@Autowired
	private HisPathService hisPathService;

	@Autowired
	private ArchOuterService archOuterService;
	@Autowired
	private ArchMRManagerService archMRManagerService;
	/**
	 * ???????????????????????????
	 * 
	 * @param param
	 *            * @param user
	 */
	public void archiveUpload(String param, IUser user) {
		List<PatiInfo> params = JsonUtil.readValue(param,
				new TypeReference<List<PatiInfo>>() {
				});

		// 1.????????????????????????????????????????????????
		if (params != null && params.size() > 0) {
			for (PatiInfo vo : params) {
				List<String> systems = vo.getSysToUp();
				PvArchive record = service.geneArchRecrd(vo.getPkPv());
				//2.???????????????????????????
				uploadBySystem(vo, systems, record);
			}
		} else {
			throw new BusException(" ??????????????????????????????????????????");
		}

	}

	/**
	 * ????????????????????????????????????
	 * @param vo ????????????
	 * @param systems ???????????? HIS???LIS???PACS
	 * @param pv  ????????????
	 * @param record ????????????
	 * 
	 */
	private void uploadBySystem(PatiInfo vo, List<String> systems, PvArchive record) {
		
		if(systems!=null && systems.size()>0){
			
			for (String sys : systems) {			
				//1.?????????????????????????????????---??????????????????...
				List<BdArchDoctype> doctypes = DataBaseHelper.queryForList(" select doc.pk_doctype,doc.code_doctype,doc.eu_doctype,doc.name_doctype,doc.code_map "
										+ "from bd_arch_doctype doc where doc.flag_active='1' and doc.dt_systype=?",BdArchDoctype.class, sys);
				
				if(doctypes!=null && doctypes.size()>0){
				 //2.????????????????????????????????????
					for(BdArchDoctype doctype : doctypes){
						if (doctype == null|| CommonUtils.isEmptyString(doctype.getCodeDoctype())) {
							service.logger.debug("???????????????" + vo.getNamePi() + "/"+ sys + "/??????????????????????????????");
							continue;
						}
						if(!"0".equals(doctype.getEuDoctype())){//pdf??????
							continue;
						}
						
						int SN = 1;// ???????????????
						List<String> FilePath = new ArrayList<String>();//????????????
						String nameSys = "";
						switch (sys) {
						case HIS:
							
							
							FilePath = hisPathService.get_HISPathByDoctype(vo,doctype.getCodeDoctype());
							nameSys = "HIS";
							break;
						case LIS:
							String codePi = vo.getCodePi();
							String ipTimes = "0";
							PvIp pvip = DataBaseHelper.queryForBean("select * from pv_ip where pk_pv =?",PvIp.class, vo.getPkPv());
							if (pvip != null) {
								ipTimes = pvip.getIpTimes() + "";
							}
							nameSys = "LIS";
							DataSourceRoute.putAppId("archOld");
							FilePath = lisPathService.getLisPaths(sys, codePi,ipTimes);
							break;
						case PACS:
							// FilePath = pathHandler.get_PACSPath(pv,sysName);
							break;
						case OP:
							// FilePath = pathHandler.get_OPPath(pv,sysName);
							break;
						case ICU:
							// FilePath = pathHandler.get_ICUPath(pv,sysName);
							break;
						}
						try {
							
							for (String path : FilePath) {
								synchronized (this) {
									uploadFile(vo,record,nameSys,SN, doctype, path);
								}
								SN++;
							}
						} catch (Exception e) {
							service.logger.debug("???????????????" + vo.getNamePi() + "/"+ sys + "ftp??????????????????;" + e.getMessage());
							continue;
						}
					}
				}		
			}				
		}
	}

	private synchronized void uploadFile(PatiInfo vo, PvArchive record,
			String sys, int SN, BdArchDoctype doctype, String path)
			throws IOException {
		DataSourceRoute.putAppId(null);
		PvEncounter pv = DataBaseHelper.queryForBean(" 	select * from pv_encounter where pk_pv = ?", PvEncounter.class, vo.getPkPv());
		String dir = path.substring(0,
				path.lastIndexOf("/"));// ?????????????????????
		String fileName = path.substring(path
				.lastIndexOf("/")+1);
		String folder = service.getFolder(pv,vo.getCodePi());
		if (CommonUtils.isEmptyString(folder)) {
			throw new BusException("???????????????????????????");
		}
		String NewFileName = sys + "_"
				+ doctype.getCodeDoctype() + "_" + SN+SUFFIX;
		DataSourceRoute.putAppId(null);
		PvArchDoc doc = service.genArchDoc(NewFileName,
				doctype.getPkDoctype(), record.getPkOrg(), null, record,null,null);
		double size = FTPUtil.moveFile(fileName, "/"+dir,
				folder,NewFileName);

		// ?????????????????????????????????pv_arch_doc???
			service.updateArchDoc(size, null, doc, folder,
					0,null);		
		// ????????????????????????
		service.geneDocOperRecrd(doc.getPkDoc(), "1");
	}

	
	public void uploadByByte(String param, IUser user){
		List<HISFileInfo4Upd>  files =JsonUtil.readValue(param, new TypeReference<List<HISFileInfo4Upd>>(){});
		if(files!=null && files.size()>0){
			for(HISFileInfo4Upd file : files){
				boolean succ=true;
				//??????????????????
				PvArchive record =  service.geneArchRecrd(file.getPkPv());
				ArchUploadParam updParam =ArchUtils.genUpdParam(file);
				String fileStr=null;
				if(updParam!=null && updParam.getUploadBody()!=null){
					fileStr = updParam.getUploadBody().getFilecontent();
				}
				//??????
				Map<String,Object> res = archOuterService.uploadFile(updParam);
				//??????????????????????????????
				ArchResultUp rtn = (ArchResultUp) res.get("result");
				if(rtn.getResponse().getErrormsg()!=null && !rtn.getResponse().getErrormsg().equals("")){
					EmrOperateLogs emrOpeLogs = new EmrOperateLogs();
					emrOpeLogs.setCode("archive");
					emrOpeLogs.setName("????????????");
					emrOpeLogs.setDelFlag("0");
					emrOpeLogs.setEuStatus("0");
					emrOpeLogs.setPkOrg(UserContext.getUser().getPkOrg());
					emrOpeLogs.setPkPv(file.getPkPv());
					if(rtn.getResponse().getErrormsg().length()>1000){
						emrOpeLogs.setOperateTxt("????????????"+file.getFileName()+"???????????????: "+rtn.getResponse().getErrormsg().substring(0, 1000));
					}else{
						emrOpeLogs.setOperateTxt("????????????"+file.getFileName()+"???????????????: "+rtn.getResponse().getErrormsg());
					}
					service.saveEmrOperLogs(emrOpeLogs);
					succ=false;
					//return;
				}
				PvArchDoc doc = service.genArchDoc(updParam.getUploadHead().getDocname(),
				updParam.getUploadHead().getDoctype(), UserContext.getUser().getPkOrg(), null, record,null,null);
				//?????????????????????????????????pv_arch_doc??????????????????????????????????????????????????????????????????byte??????
				if(succ)
				{
					service.updateArchDoc(0, null, doc, res.get("filePath").toString(),0,null);	
				}else{
					try {
						byte[] fileBytes = Base64.getDecoder().decode(fileStr);
						service.updateArchDoc(0, null, doc, null,0,fileBytes);
					} catch (Exception e) {
						
					}
				}
				// ????????????????????????
				if(file.getEuOpType()!=null &&file.getEuOpType().equals("0")){
					service.geneDocOperRecrd(doc.getPkDoc(), "0");
				}else{
					service.geneDocOperRecrd(doc.getPkDoc(), "1");
				}
				
			}

		}
    
	}
	
	/**
	 * ????????????????????????????????????--??????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryArchPacsListBoai(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> archList=new ArrayList<Map<String, Object>>();
		List<ArchPv> pvList=archMRManagerService.queryArchPv(map);
		DataSourceRoute.putAppId("baPacs");
		try {
			for (ArchPv archPv : pvList) {
				if(archPv.getCodeIp()!=null && archPv.getIpTimes()!=null) {
					archList=archMRManagerService.queryArchPacsListBoai(archPv.getCodeIp(),archPv.getIpTimes());
					for (Map m : archList) {
						if(m.get("reportid")!=null) {
							if(!archPv.getCodeRpt().contains(m.get("reportid").toString())) {
								m.put("codeIp", archPv.getCodeIp());
								m.put("times", archPv.getIpTimes());
								m.put("dateBegin", archPv.getDateBegin());
								m.put("dateEnd", archPv.getDateEnd());
								result.add(m);
							}
						}
					}
				}
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return result;
		}finally{
			DataSourceRoute.putAppId("default");
		}
		return result;
	}
}
