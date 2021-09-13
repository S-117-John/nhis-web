package com.zebone.nhis.pro.lb.arch.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.arch.ArchDoc;
import com.zebone.nhis.common.module.arch.ArchPrint;
import com.zebone.nhis.common.module.arch.ArchPv;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.ArchUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.FTPUtil;
import com.zebone.nhis.pro.lb.arch.dao.ArchiveLbMapper;
import com.zebone.nhis.pro.lb.arch.vo.ArchDocUpVo;
import com.zebone.nhis.pro.lb.arch.vo.ArchDocVo;
import com.zebone.nhis.pro.lb.arch.vo.PatientArchVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病例归档服务类 lb
 * 
 * @author Alvin
 * 
 */
@Service
public class ArchiveLbService {

	private final static Logger logger = LogManager.getLogger(ArchiveLbService.class);
	@Resource
	private ArchiveLbMapper archiveLbMapper;
	
	/**
	 * 查询患者归档信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Page<PatientArchVo> queryPatientOfArch(String param, IUser user) {
		@SuppressWarnings("unchecked")
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		MyBatisPage.startPage(MapUtils.getIntValue(paramMap, "pageIndex"), MapUtils.getIntValue(paramMap, "pageSize"));
		List<PatientArchVo> list = archiveLbMapper.queryPatientOfArch(paramMap);
		@SuppressWarnings("unchecked")
		Page<PatientArchVo> page = MyBatisPage.getPage();
		page.setRows(list);		
		return page;
	}
	
	/**
	 * 查询归档文件信息包含打印信息<br>
	 * 注意不返回大字段content，预览重新请求预览接口即可
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ArchDocVo> queryArchDocAndPrint(String param, IUser user) {
		return archiveLbMapper.queryArchDocAndPrint(JsonUtil.getFieldValue(param, "pkPv"));
	}
	
	/**
	 * 查询归档文件信息<br>
	 * 注意不返回大字段content
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ArchDocVo> queryArchDoc(String param, IUser user) {
		return archiveLbMapper.queryArchDoc(JsonUtil.getFieldValue(param, "pkPv"));
	}
	
	public ArchDoc getArchDoc(String param, IUser user){
		String pkArchdoc = JsonUtil.getFieldValue(param, "pkArchdoc");
		if (StringUtils.isBlank(pkArchdoc))
			throw new BusException("pkArchdoc字段不能为空！");
		ArchDoc docContent = DataBaseHelper.queryForBean("select CONTENT from ARCH_DOC where PK_ARCHDOC = ?", ArchDoc.class,
				new Object[] { pkArchdoc });
		ArchDoc doc = DataBaseHelper.queryForBean("select PK_ARCHDOC,PK_ORG,PK_ARCHPV,SORTNO,DT_MEDDOCTYPE,NAME_DOC,PATH,DATE_UPLOAD,PK_EMP_UPLOAD,NAME_EMP_UPLOAD,CNT_PRINT,FLAG_CANCEL,DATE_CANCEL,PK_EMP_CANCEL,NAME_EMP_CANCEL,NOTE,CREATOR,CREATE_TIME,MODIFIER,DEL_FLAG,TS,FLAG_LOOK" +
				" from ARCH_DOC where PK_ARCHDOC = ?", ArchDoc.class, new Object[]{pkArchdoc});
		if(doc!=null){
			doc.setContent(docContent.getContent());
		}
		if (doc != null && doc.getContent() != null && StringUtils.isNotBlank(doc.getNameDoc())) {
			Map<String, Object> codeMap = DataBaseHelper.queryForMap(
					"select pi.CODE_IP from pi_master pi inner join pv_encounter pv on pi.pk_pi=pv.pk_pi inner join ARCH_PV ap on pv.PK_PV=ap.PK_PV inner join ARCH_DOC ad on ap.PK_ARCHPV=ad.PK_ARCHPV where PK_ARCHDOC = ?",
					new Object[] { pkArchdoc });
			if (MapUtils.isEmpty(codeMap)) {
				throw new BusException("依据PK_ARCHDOC没有获取到患者信息！");
			}
			uploadFile(doc.getContent(), MapUtils.getString(codeMap, "codeIp"), doc.getNameDoc());
			doc.setContent(null);
		}
		return doc;
	}
	
	
	public void saveArchDoc(String param, IUser user) {
		ArchDocUpVo vo = JsonUtil.readValue(param, ArchDocUpVo.class);
		if(vo == null) {
			throw new BusException("文档对象不能为空！");
		}
		if(StringUtils.isBlank(vo.getDtMeddoctype())) {
			throw new BusException("dtMeddoctype字段不能为空！");
		}
		if(StringUtils.isBlank(vo.getNameDoc())) {
			throw new BusException("nameDoc字段不能为空！");
		}
		User u = (User)user;
		if(StringUtils.isBlank(vo.getPkPv())) {
			throw new BusException("pkPv字段不能为空！");
		}
		PiMaster piMaster = DataBaseHelper.queryForBean("select pi.CODE_IP,pi.NAME_PI,pi.BIRTH_DATE,pi.DT_SEX from pi_master pi "
				+ " inner join pv_encounter pv on pi.pk_pi=pv.pk_pi where pv.pk_pv=?", PiMaster.class, new Object[]{vo.getPkPv()});
		if(piMaster == null) {
			throw new BusException("依据pkPv没有获取到患者信息！");
		}
		if(StringUtils.isBlank(vo.getPkArchpv())) {
			ArchPv originalArchPv = DataBaseHelper.queryForBean("select PK_ARCHPV from ARCH_PV where pk_pv=?",
					ArchPv.class, new Object[]{vo.getPkPv()});
			if(originalArchPv == null){
				ArchPv archPv = new ArchPv();
				ApplicationUtils.setDefaultValue(archPv, true);
				vo.setPkArchpv(archPv.getPkArchpv());
				archPv.setPkPv(vo.getPkPv());
				archPv.setBirthDate(piMaster.getBirthDate());
				archPv.setCodeIp(piMaster.getCodeIp());
				archPv.setEuSex(piMaster.getDtSex());
				archPv.setNamePi(piMaster.getNamePi());
				archPv.setCntArch(0);
				DataBaseHelper.insertBean(archPv);
			} else {
				vo.setPkArchpv(originalArchPv.getPkArchpv());
			}
		}
		
		String originalPkArchdoc = vo.getPkArchdoc();
		ArchDoc archDoc = new ArchDoc();
		ApplicationUtils.setDefaultValue(archDoc, true);
		archDoc.setContent(getContentTwice(piMaster.getCodeIp(),vo.getNameDoc()));
		archDoc.setDateUpload(new Date());
		archDoc.setDtMeddoctype(vo.getDtMeddoctype());
		archDoc.setFlagCancel(EnumerateParameter.ZERO);
		archDoc.setNameDoc(vo.getNameDoc());
		archDoc.setPkEmpUpload(u.getPkEmp());
		archDoc.setNameEmpUpload(u.getNameEmp());
		archDoc.setNote(null);
		archDoc.setPath(null);
		archDoc.setPkArchpv(vo.getPkArchpv());
		archDoc.setSortno(vo.getSortno());
		if(StringUtils.isNotBlank(originalPkArchdoc)) {
			ArchDoc archCancel = new ArchDoc();
			archCancel.setPkArchdoc(originalPkArchdoc);
			archCancel.setFlagCancel(EnumerateParameter.ONE);
			archCancel.setDateCancel(new Date());
			archCancel.setPkEmpCancel(u.getPkEmp());
			archCancel.setNameEmpCancel(u.getNameEmp());
			archCancel.setNote("重新上传,新记录："+archDoc.getPkArchdoc());
			DataBaseHelper.updateBeanByPk(archCancel,false);
		}
		DataBaseHelper.insertBean(archDoc);
		DataBaseHelper.update("update arch_pv set CNT_ARCH = (select count(1) from arch_doc where pk_archpv=? and flag_cancel='0') where PK_ARCHPV=?",
				new Object[]{vo.getPkArchpv(), vo.getPkArchpv()});
	}

	public byte[] getContentTwice(String codeIp,String nameDoc){
		byte[] cont = null;
		try {
			cont = getFileContent(codeIp,nameDoc);
		} catch (Exception e){
			cont = getFileContent(codeIp,nameDoc);
		}
		return cont;
	}
	
	public void savePrintArchDoc(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String pkArchdoc = MapUtils.getString(map, "pkArchdoc");
		if(StringUtils.isBlank(pkArchdoc)) {
			throw new BusException("pkArchdoc不能为空！");
		}
		User u = (User)user;
		ArchPrint archPrint = new ArchPrint();
		ApplicationUtils.setDefaultValue(archPrint, true);
		archPrint.setPkArchdoc(pkArchdoc);
		archPrint.setDatePrint(new Date());
		archPrint.setNameEmpPrint(u.getNameEmp());
		archPrint.setPkEmpPrint(u.getPkEmp());
		DataBaseHelper.insertBean(archPrint);
		DataBaseHelper.update("update ARCH_DOC set CNT_PRINT = nvl(CNT_PRINT,0) +1 where PK_ARCHDOC = ?", new Object[]{pkArchdoc});
	}
	
	private byte[] getFileContent(String codeIp,String fileName) {
		if(StringUtils.isBlank(fileName)){
			throw new BusException("fileName不能为空！");
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		InputStream in = null;
		byte[] content = null;
		try {
			FTPUtil.connect();
			ftpSet();
			in = FTPUtil.ftpClient.retrieveFileStream(getFileName(codeIp,fileName));
			if (in == null) {
				throw new BusException("文件下载失败,文件名:" + fileName);
			}
			byte[] bytes = new byte[1024];
			int c;
			try {
				while ((c = in.read(bytes)) != -1) {
					os.write(bytes, 0, c);
				}
			} catch (IOException e) {
				throw new BusException(e.getMessage());
			}
			content = os.toByteArray();
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(os);
			logger.info("上载执行结果："+completeFtpCommand());
		} catch (BusException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw new BusException("获取文件内容失败："+e.getMessage());
		} finally {
			FTPUtil.disconnect();
		}
		return content;
	}

	private void ftpSet() throws IOException {
		//如果报错 451 No mapping for the unicode character exists，到IIS》FTP站点》高级设置》允许UTF8改为false即可
		FTPUtil.ftpClient.enterLocalPassiveMode();
		FTPUtil.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

		String local_charset = "gbk";
		if (FTPReply.isPositiveCompletion(FTPUtil.ftpClient.getReplyCode())) {
			if (FTPUtil.ftpClient.login(FTPUtil.FTP_USER, FTPUtil.FTP_PWD)) {
				if (FTPReply.isPositiveCompletion(FTPUtil.ftpClient.sendCommand(
						"OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
					local_charset = "UTF-8";
				}
			}
		}
		FTPUtil.ftpClient.setControlEncoding(local_charset);
		FTPUtil.ftpClient.enterLocalPassiveMode();// 设置被动模式
	}
	
	private void uploadFile(byte[] content, String codeIp,String docName){
		OutputStream out = null;
		try {
			String filePath = getFileName(codeIp,docName);
			FTPUtil.connect();
			if(FTPUtil.ftpClient.listFiles(filePath).length == 0){
				ftpSet();
				FTPUtil.ftpClient.makeDirectory(filePath.substring(0, filePath.lastIndexOf("/")));
				out = FTPUtil.ftpClient.appendFileStream(filePath);
				out.write(content, 0, content.length);
				IOUtils.closeQuietly(out);
				logger.info("上载执行结果："+completeFtpCommand());
			}
		} catch(Exception e){
			logger.error(e);
			e.printStackTrace();
			throw new BusException("文件上载异常，"+e.getMessage());
		} finally{
			FTPUtil.disconnect();
		}
	}
	
	private String getFileName(String codeIp,String docName) {
		return ArchUtils.BASE_DIR+ "/" + codeIp +"/"+docName;
	}
	
	private boolean completeFtpCommand() {
		try {
			return FTPUtil.ftpClient.completePendingCommand();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}
}
