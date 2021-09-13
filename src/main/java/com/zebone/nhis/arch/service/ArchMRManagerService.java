package com.zebone.nhis.arch.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.arch.dao.ArchiveMapper;
import com.zebone.nhis.arch.support.ArchUtil;
import com.zebone.nhis.arch.vo.ArchOpDocInfo;
import com.zebone.nhis.arch.vo.ArchiveDto;
import com.zebone.nhis.arch.vo.PvArchPrt;
import com.zebone.nhis.arch.vo.PvArchPrtRec;
import com.zebone.nhis.arch.vo.PvArchiveVO;
import com.zebone.nhis.common.module.arch.ArchPv;
import com.zebone.nhis.common.module.arch.PvArchComt;
import com.zebone.nhis.common.module.arch.PvArchComtDt;
import com.zebone.nhis.common.module.arch.PvArchDoc;
import com.zebone.nhis.common.module.arch.PvArchSeal;
import com.zebone.nhis.common.module.arch.PvArchive;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病例管理服务类
 * 
 * @version 2017-5-23 11:04:51
 * @author gongxy
 * 
 */
@Service
public class ArchMRManagerService {

	@Autowired
	private ArchiveMapper archiveMapper;
	
	public static final String ArchCode="9901";
	private static Logger log = LoggerFactory.getLogger(ArchMRManagerService.class);
	
	/**
	 * 查询患者的归档记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryMRArchRecord(String param, IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		archiveDto.setPkOrg(((User) user).getPkOrg());
		if (archiveDto.getArchDateStart() != null)
			archiveDto.setArchDateStart(DateUtils.getDateMorning(archiveDto.getArchDateStart(), 0));
		if (archiveDto.getArchDateStart() != null)
			archiveDto.setArchDateEnd(DateUtils.getDateMorning(archiveDto.getArchDateEnd(), 1));
		if (archiveDto.getOutDateStart() != null)
			archiveDto.setOutDateStart(DateUtils.getDateMorning(archiveDto.getOutDateStart(), 0));
		if (archiveDto.getOutDateEnd() != null)
			archiveDto.setOutDateEnd(DateUtils.getDateMorning(archiveDto.getOutDateEnd(), 1));
		if (archiveDto.getInDateStart() != null)
			archiveDto.setOutDateStart(DateUtils.getDateMorning(archiveDto.getInDateStart(), 0));
		if (archiveDto.getInDateEnd() != null)
			archiveDto.setOutDateEnd(DateUtils.getDateMorning(archiveDto.getInDateEnd(), 1));
		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		if(archiveDto.getFlagArchSearch()!=null && archiveDto.getFlagArchSearch().equals("1")){
			result = archiveMapper.queryMRArchRecordFlagIn(archiveDto);
		}else{
			result = archiveMapper.queryMRArchRecord(archiveDto);
		}
		
		return result;
	}

	/**
	 * 生成病历封存记录
	 * @param param
	 * @param user
	 * @return
	 */
	public int insertPvArchSeal(String param, IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		User u = (User) user;
		PvArchSeal pas = new PvArchSeal();
		ApplicationUtils.setDefaultValue(pas, true);
		pas.setPkArchive(archiveDto.getPkArchive());
		pas.setEuOptype(archiveDto.getEuOptype());
		pas.setDateOp(new Date());
		pas.setPkEmpOp(u.getPkEmp());
		pas.setNameEmpOp(u.getNameEmp());
		pas.setNote(archiveDto.getOperateReason());
		return DataBaseHelper.insertBean(pas);
	}
	public void updateEmrSeal(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String sql="update emr_pat_rec set flag_seal=? where pk_pv=?";
		DataBaseHelper.update(sql, new Object[] {map.get("euStatus"),map.get("pkPv")});
	}

	/**
	 * 加入点评库
	 * @param param
	 * @param user
	 * @return
	 */
	public int insertPvArchComt(String param, IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		List<PvArchComt> pvArchComts = new ArrayList<PvArchComt>();
		for (PvArchiveVO pvArchive : archiveDto.getPvArchives()) {
			PvArchComt pvArchComt = new PvArchComt();
			ApplicationUtils.setDefaultValue(pvArchComt, true);
			pvArchComt.setPkArchive(pvArchive.getPkArchive());
			pvArchComt.setDateBegin(new Date());
			pvArchComt.setFlagFinish(EnumerateParameter.ZERO);
			pvArchComts.add(pvArchComt);
		}
		if (pvArchComts.size() > 0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvArchComt.class), pvArchComts);

		return pvArchComts.size();
	}

	/**
	 * 记录病历点评
	 * @param param
	 * @param user
	 * @return
	 */
	public int insertPvArchComtDt(String param, IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		PvArchComtDt pvArchComtDt = archiveDto.getPvArchComtDt();
		int count;
		if (pvArchComtDt.getPkComtdt() == null) {
			ApplicationUtils.setDefaultValue(pvArchComtDt, true);
			count = DataBaseHelper.insertBean(pvArchComtDt);
		} else {
			ApplicationUtils.setDefaultValue(pvArchComtDt, false);
			count = DataBaseHelper.updateBeanByPk(pvArchComtDt, false);
		}
		return count;
	}

	/**
	 * 完成点评
	 * @param param
	 * @param user
	 * @return
	 */
	public int updatePvArchComt(String param, IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		User u = (User) user;
		List<PvArchComt> pvArchComts = new ArrayList<PvArchComt>();
		for (PvArchiveVO pvArchive : archiveDto.getPvArchives()) {
			PvArchComt pvArchComt = new PvArchComt();
			pvArchComt.setPkComt(pvArchive.getPkComt());
			ApplicationUtils.setDefaultValue(pvArchComt, false);
			pvArchComt.setFlagFinish(EnumerateParameter.ONE);
			pvArchComt.setDateEnd(new Date());
			pvArchComt.setPkEmpComt(u.getPkEmp());
			pvArchComt.setNameEmpComt(u.getNameEmp());
			pvArchComts.add(pvArchComt);
		}
		if (pvArchComts.size() > 0) {
			DataBaseHelper
					.batchUpdate(
							"update pv_arch_comt set flag_finish=:flagFinish,date_end=:dateEnd,pk_emp_comt=:pkEmpComt,name_emp_comt=:nameEmpComt,ts=:ts where pk_comt=:pkComt",
							pvArchComts);
		}
		return pvArchComts.size();
	}

	/**
	 * 取消点评
	 * @param param
	 * @param user
	 * @return
	 */
	public int deletePvArchComt(String param, IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		Set<String> pkComts = new HashSet<String>();
		for (PvArchiveVO pvArchive : archiveDto.getPvArchives()) {
			pkComts.add(pvArchive.getPkComt());
		}
		if (pkComts.size() > 0) {
			String deleteWhere = " pk_comt in (" + ArchUtil.convertSetToSqlInPart(pkComts, "pk_comt") + ")";
			DataBaseHelper.deleteBeanByWhere(new PvArchComtDt(), deleteWhere);
			DataBaseHelper.deleteBeanByWhere(new PvArchComt(), deleteWhere);
		}
		return pkComts.size();
	}

	/**
	 * 根据条件查询病历点评文件
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryMRReview(String param, IUser user) {

		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		archiveDto.setDateBegin(DateUtils.getDateMorning(archiveDto.getDateBegin(), 0));
		archiveDto.setDateEnd(DateUtils.getDateMorning(archiveDto.getDateEnd(), 1));
		archiveDto.setPkOrg(((User) user).getPkOrg());
		List<Map<String, Object>> result = archiveMapper.queryMRReview(archiveDto);
		return result == null ? new ArrayList<Map<String, Object>>() : result;
	}
	
	public List<Map<String, Object>> qryPatientList(String param, IUser user) throws ParseException{
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String type=map.get("type").toString();
		if(map.containsKey("flagLabor"))
		{
			return archiveMapper.qryArchPatListByLabor(map);
		}else{
			if(type.equals("1")){
				return archiveMapper.qryArchPatList(map);
			}else if(type.equals("2")){
				if(map.containsKey("flagInvok")){
					map.put("dateBegin", null);
					map.put("dateEnd", null);
				}else{
					SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
					Date dateBegin=sf.parse(map.get("dateBegin").toString());
					Date dateEnd=sf.parse(map.get("dateEnd").toString());
					Calendar c = Calendar.getInstance(); 
					c.setTime(dateEnd);
					c.add(Calendar.DAY_OF_MONTH,1); 
					Date endDate=c.getTime();
					sf = new SimpleDateFormat("yyyy-MM-dd");
					sf.format(dateBegin);
					map.put("dateBegin", sf.format(dateBegin));
					map.put("dateEnd", sf.format(endDate));
				}
				return archiveMapper.qryArchLeavePatList(map);
			}else if(type.equals("3")){
				return archiveMapper.qryArchChangePatList(map);
			}else if(type.equals("4")){
				return archiveMapper.qryArchConsulPatList(map);
			}
		}
		return null;
	}
	public PvArchive queryPvArchiveByPkPv(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String sql="select * from pv_archive where pk_pv=? and FLAG_DEL='0'";
		String sql2="select pi.code_op from PI_MASTER pi inner join PV_ENCOUNTER pv on pi.PK_PI=pv.PK_PI where pv.PK_PV=?";
		PvArchive arch=DataBaseHelper.queryForBean(sql, PvArchive.class, map.get("pkPv"));
		if(arch!=null){
			arch.setCodeOp(DataBaseHelper.queryForScalar(sql2, String.class, map.get("pkPv")));
		}
		return arch;
	}
	
	public String queryCodeApply(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String type=map.get("type").toString();
		String CodeApply=map.get("codeApply").toString();
		String CodeRpt="";
		if(type.equals("Ris")){
			CodeRpt=archiveMapper.queryRisCodeRpt(CodeApply);
		}else{
			CodeRpt=archiveMapper.queryLisCodeRpt(CodeApply);
		}
		return CodeRpt;
	}
	/**
	 * 根据CodePv查询打印条码的数据
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException
	 */
	public Map<String,Object> queryBarCodeByCodePv(String param, IUser user) throws ParseException{
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		Map<String, Object> resultMap = JsonUtil.readValue(param, Map.class);
		String codePv=map.get("codePv").toString();
		Map<String,Object> barCodeMap=archiveMapper.queryBarCodeByCodePv(codePv);
		if(barCodeMap!=null && barCodeMap.get("codeArch")!=null && !barCodeMap.get("codeArch").equals(""))
		{
			String CodeArch=barCodeMap.get("codeArch").toString();
			if(CodeArch!=null && !CodeArch.equals("")){
				SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
				Date codeArchDate=sf.parse(CodeArch.substring(0,8));
				sf = new SimpleDateFormat("yyyy-MM-dd");
				resultMap.put("codeArchDate", sf.format(codeArchDate));
				resultMap.put("codeArchNum", CodeArch.substring(8));
				resultMap.put("codeIp", barCodeMap.get("codeIp"));
				resultMap.put("name", barCodeMap.get("name"));
				resultMap.put("ipTimes", barCodeMap.get("ipTimes"));
				resultMap.put("codePv", barCodeMap.get("codePv"));
				resultMap.put("deptName", barCodeMap.get("deptname"));
				return resultMap;
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}
	/**
	 * 扫描时更新患者的归档ID
	 * @param param
	 * @param user
	 * @throws ParseException 
	 */
	public void updArchiveCode(String param, IUser user) throws ParseException{
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		String pkPv=map.get("pkPv").toString();
		String codeIp=map.get("codeIp").toString();
		String sql="select * from pv_archive where pk_pv=?";
		PvArchive archive=DataBaseHelper.queryForBean(sql, PvArchive.class, pkPv);
		if(archive !=null && (archive.getCodeArch()==null ||archive.getCodeArch().equals(""))){
			String patSql="select * from PV_ENCOUNTER where pk_pv=? and  DEL_FLAG='0'";
			PvEncounter pv=DataBaseHelper.queryForBean(patSql, PvEncounter.class, pkPv);
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = sf.format(pv.getDateEnd());
			String time=dateString.substring(0,10).replace("-", "");
			String sortSql="select isnull(max(code_arch),0) from PV_ARCHIVE where CODE_ARCH is not null and CODE_ARCH like '"+time+"%'";
			String sort=DataBaseHelper.queryForScalar(sortSql, String.class, new Object[] {});
			String updSql="update PV_ARCHIVE set CODE_ARCH=?,flag_arch_paper='1' where PK_PV=?";
			String updBSql="update PV_ARCHIVE set flag_arch_paper='1' where PK_PV=?";
			if(codeIp.contains("B")){
				DataBaseHelper.update(updBSql,new Object[]{pkPv});
			}else{
				if(sort.equals("0")){
					DataBaseHelper.update(updSql, time+"001",pkPv);
				}else{
					Integer maxsort=Integer.valueOf(sort.substring(8));
					String max=("000"+(maxsort+1));
					DataBaseHelper.update(updSql, time+max.substring(max.length()-3),pkPv);
				}
			}
			
			
		}
		if(archive !=null && archive.getFlagArch().equals("1"))
		{
			if(!archive.getFlagArch().equals("8")){
				String updsql="update PV_ARCHIVE set EU_ARCHTYPE='0',EU_STATUS='8' where PK_ARCHIVE=?";
				DataBaseHelper.update(updsql, new Object[]{archive.getPkArchive()});
			}
		}
	}
	
	public List<Map<String, Object>> queryArchPatInfo(String param, IUser user) {
		ArchiveDto archiveDto = JsonUtil.readValue(param, ArchiveDto.class);
		List<Map<String, Object>> result = archiveMapper.queryArchPatInfo(archiveDto);
		return result;
	}
	/**
	 * 打印登记查询患者信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String , Object>> queryPrintNewPatInfo(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> result = archiveMapper.queryPrintNewPatInfo(map);
		return result;
	}
	/**
	 * 保存新增的打印记录
	 * @param param
	 * @param user
	 */
	public void SavePrintContent(String param , IUser user){
		PvArchPrtRec pvArchPrtRec=JsonUtil.readValue(param, PvArchPrtRec.class);
		List<PvArchPrt> list = pvArchPrtRec.getPvArchPrtList();
		String pkId=NHISUUID.getKeyId();
		pvArchPrtRec.setPkPrtRec(pkId);
		DataBaseHelper.insertBean(pvArchPrtRec);
		if(list != null && list.size() != 0){
			for (PvArchPrt pvArchPrt : list) {
				PvArchPrt pv=new PvArchPrt();
				ApplicationUtils.copyProperties(pv,pvArchPrt);
				pv.setPkPrtRec(pkId);
				pv.setPkPrt(NHISUUID.getKeyId());
				pv.setFlagDel("0");
				DataBaseHelper.insertBean(pv);
			}
		}
		String sql="update emr_pat_rec set flag_copy='1' where pk_pv=? and del_flag='0'";
		DataBaseHelper.update(sql, new Object[]{pvArchPrtRec.getPkPv()});
	}
	/**
	 * 查询打印登记的患者记录
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	public List<Map<String , Object>> queryPrintUpdPatInfo(String param, IUser user) throws ParseException{
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date dateBegin=sf.parse(map.get("tDateBegin").toString());
		Date dateEnd=sf.parse(map.get("tDateEnd").toString());
		Calendar c = Calendar.getInstance(); 
		c.setTime(dateEnd);
		c.add(Calendar.DAY_OF_MONTH,1); 
		Date endDate=c.getTime();
		sf = new SimpleDateFormat("yyyy-MM-dd");
		sf.format(dateBegin);
		map.put("dateBegin", sf.format(dateBegin));
		map.put("dateEnd", sf.format(endDate));
		List<Map<String, Object>> result = archiveMapper.queryPrintUpdPatInfo(map);
		return result;
	}
	/**
	 * 根据打印主键查询打印内容
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PvArchPrt> queryContentByPk(String param, IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		List<PvArchPrt> result = archiveMapper.queryContentByPk(map);
		return result;
	}
	/**
	 * 保存新增的打印记录
	 * @param param
	 * @param user
	 */
	public void SaveUpdPrintContent(String param , IUser user){
		PvArchPrtRec pvArchPrtRec=JsonUtil.readValue(param, PvArchPrtRec.class);
		List<PvArchPrt> list = pvArchPrtRec.getPvArchPrtList();
		String pkPrtRec=pvArchPrtRec.getPkPrtRec();
		DataBaseHelper.update("update pv_arch_prt set flag_del = '1' where pk_prt_rec = ?",new Object[]{pkPrtRec});
		if(list != null && list.size() != 0){
			//先全删再恢复的方式（软删除）
			for(PvArchPrt prt : list){
				if(prt.getPkPrt() != null){
					prt.setFlagDel("0");//恢复
					DataBaseHelper.updateBeanByPk(prt, false);
				}else{
					prt.setFlagDel("0");
					prt.setPkPrt(NHISUUID.getKeyId());
					DataBaseHelper.insertBean(prt);
				}
				
			}
		}
	}
	public void delArchScanDoc(String param, IUser user) throws ParseException {
		List<String> list = JsonUtil.readValue(param,new TypeReference<List<String>>() {});
		List<String> upSqls=new ArrayList<String>();
		for (String li : list) {
			//StringBuffer upSql=new StringBuffer("delete from pv_arch_doc where pk_doc= '");
			StringBuffer upSql=new StringBuffer("update pv_arch_doc set flag_del='1' where pk_doc= '");
			upSql.append(li+"'");
			upSqls.add(upSql.toString());
		}
		DataBaseHelper.batchUpdate(upSqls.toArray(new String[0]));
		
	}
	
	public List<ArchOpDocInfo> queryArchLogOpDoc(String param, IUser user) throws ParseException{
		Map<String,Object> map = JsonUtil.readValue(param, HashMap.class);
		List<ArchOpDocInfo> res = new ArrayList<ArchOpDocInfo>(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date tmpb = sdf.parse(map.get("dateBegin").toString());
		Date tmpe = sdf.parse(map.get("dateEnd").toString());
		Calendar c = Calendar.getInstance(); 
		c.setTime(tmpe);
		c.add(Calendar.DAY_OF_MONTH,1); 
		Date endDate=c.getTime();
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("rptBeginDate", sdf.format(tmpb));
		map.put("rptEndDate", sdf.format(endDate));
		res=archiveMapper.queryArchLogOpDoc(map);
		String sql="select top 1 code_pi from pi_master where code_op='"+map.get("patCode")+"'";
		PiMaster pi=DataBaseHelper.queryForBean(sql, PiMaster.class);
		map.put("codePi", pi==null?null:pi.getCodePi());
		if(map.get("codePi")!=null || map.get("patName")!=null) 
		{
			List<ArchOpDocInfo> oldres=archiveMapper.queryArchLogOpDocOld(map);
			if(oldres!=null && oldres.size()>0) {
				res.addAll(oldres);
			}
		}
		return res;
	}
	
	public List<Map<String,Object>> qryArchOldIpDocInfo(String param, IUser user) throws ParseException{
		Map<String,Object> map = JsonUtil.readValue(param, HashMap.class);
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date tmpb = sdf.parse(map.get("dateBegin").toString());
		Date tmpe = sdf.parse(map.get("dateEnd").toString());
		Calendar c = Calendar.getInstance(); 
		c.setTime(tmpe);
		c.add(Calendar.DAY_OF_MONTH,1); 
		Date endDate=c.getTime();
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("rptBeginDate", sdf.format(tmpb));
		map.put("rptEndDate", sdf.format(endDate));
		res=archiveMapper.qryArchOldIpDocInfo(map);
		return res;
	}
	
	public PvArchDoc qryDocInfoByPk(String param, IUser user){
		//log.info("qryDocInfoByPk");
		Map<String,Object> map = JsonUtil.readValue(param, HashMap.class);
		String sql="select * from pv_arch_doc where pk_doc=?";
		//log.info("qryDocInfoByPk:"+map.get("pkDoc"));
		PvArchDoc pv=DataBaseHelper.queryForBean(sql, PvArchDoc.class, map.get("pkDoc"));
		String hosCode=ApplicationUtils.getPropertyValue("emr.hos.code", "");
		//log.info("hosCode:"+hosCode);
		//log.info("getPosition:"+pv.getPosition());
		if(hosCode!=null&& !hosCode.equals("zsba")){
			if(pv!=null && pv.getPosition()!=null) {
				try {
					String separator = ApplicationUtils.getPropertyValue("arch.separator", "");
					String archPath = ApplicationUtils.getPropertyValue("arch.file.path", "");
					if(StringUtils.isBlank(archPath)) archPath="/data/archive";
					String archDir = ApplicationUtils.getPropertyValue("arch.file.dir", "");
					if(StringUtils.isBlank(archDir)) archDir="/NHISArchive";
					
					String path = pv.getPosition();
					String filePath = archPath+archDir+separator+path+separator+pv.getNameDoc();
					//log.info("filePath:"+filePath);
					pv.setDocData(getFileBase64StrByLocalFile(filePath));
					//log.info("docData.length:"+pv.getDocData().length);
				}catch(Exception e) {
					//log.info("qryDocInfoByPk error");
					e.printStackTrace();
					pv.setDocData(null);
				}
			}
		}
		return pv;
	}
	public static byte[] getFileBase64StrByLocalFile(String localFilePath){
        
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try 
        {
        	//log.info("localFilePath:"+localFilePath);
        	in = new FileInputStream(localFilePath);
        	
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } 
        catch (IOException e) 
        {
        	log.info("getFileBase64StrByLocalFile error:");
            e.printStackTrace();
        }
        //log.info("data:"+data.length);
        return data;
    }

	/**
	 * 查询第三方的检查视图
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<Map<String, Object>> queryArchPacsListBoai(String codeIp,String times) {
		List<Map<String, Object>> rtnList = archiveMapper.queryArchPacsListBoai(codeIp,times);
		return rtnList;
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<ArchPv> queryArchPv(Map map) {
		List<ArchPv> rtnList = archiveMapper.queryArchPv(map);
		return rtnList;
	}
	/**
	 * 处理归档条码号重复问题
	 * @param param
	 * @param user
	 * @throws ParseException
	 */
	public void SovleCodeArch(String param, IUser user) throws ParseException{
		Map<String,Object> map = JsonUtil.readValue(param, HashMap.class);
		//查询所有重复归档条码号
		List<String> list=archiveMapper.queryCodeArchByDate(map.get("codeArch").toString());
		for (String code : list) {
			String dCode=code.substring(0, 8);
			//查询重复归档条码号的信息
			String infoSql="select * from PV_ARCHIVE where CODE_ARCH='"+code+"'";
			List<PvArchive> archlist=DataBaseHelper.queryForList(infoSql, PvArchive.class,new Object[] {});
			for (int i = 0; i < archlist.size(); i++) {
				if(i!=0) {
					//查询当前最大的条码号
					String maxSql="select max(CODE_ARCH) from PV_ARCHIVE where CODE_ARCH like '"+dCode+"%'";
					String maxCodeArch=DataBaseHelper.queryForScalar(maxSql, String.class,new Object[] {});
					Integer maxsort=Integer.valueOf(maxCodeArch.substring(8));
					String max=("000"+(maxsort+1));
					//更新重复的条码号
					String updSql="update PV_ARCHIVE set CODE_ARCH='"+dCode+max.substring(max.length()-3)+"' where PK_ARCHIVE='"+archlist.get(i).getPkArchive()+"'";
					DataBaseHelper.update(updSql);
				}
			}
		}
	}
}
