package com.zebone.nhis.pro.zsba.compay.ins.qgyb.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePage;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.pro.zsba.compay.ins.pub.service.InsPubSignInService;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.dao.InsQgybPvMapper;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.FileUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.YbFunUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2401Mdtrtinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input3201Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input3202Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input3260Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input3261Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input3261Detail;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input3262Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Iteminfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Oprninfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Payinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Setlinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4160Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input9101FsUploadIn;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input9102FsDownloadIn;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output3260Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output4160Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsQgQfEntity;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsRecDetailedQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsRecLedgerQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaClearbranchDetailedQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaClearbranchQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaPvQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaSignInQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaStListFz;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaStQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input3201;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input3202;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input3260;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input3261;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input3262;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input4101;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input4160;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input9101;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input9102;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData1101;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData3201;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData3202;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData3260;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData3261;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData3262;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData4101;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData4160;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData9101;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData9102;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;

/**
 * 全国医保对账、异地清分
 * @author Administrator
 *
 */
@Service
public class InsQgRecService {
	
	@Autowired
	private InsPubSignInService insPubSignInService;
	
	@Autowired
	private InsQgybPvMapper insQgybPvMapper;
	
	/**
	 * 对总账
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> reconciliation(String param,IUser user){
		//1.通过日期区间查询险种和结算经办机构
		//2.根据险种和结算经办机构循环统计数据调用医保接口进行对总账
		//3.记录返回结果并返回客户端
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String dateBegin = jo.getString("dateBegin");
		String dateEnd = jo.getString("dateEnd");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		
		
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			//通过日期区间查询险种和结算经办机构
			String sql = "select a.insutype, a.clr_optins from ins_st_qg a inner join INS_PV_QG b on a.PK_INSPVQG = b.PK_INSPVQG where a.setl_time>=?+' 00:00:00' and a.setl_time<=?+' 23:59:59' and a.clr_optins = '442000' and b.insuplc_admdvs = '442000' and a.del_flag = '0' group by a.insutype, a.clr_optins order by a.clr_optins";
			List<Map<String, Object>> flList = DataBaseHelper.queryForList(sql, dateBegin, dateEnd);
			List<Map<String, Object>> resList = new ArrayList<Map<String,Object>>();//存储对账结算
			//根据险种和结算经办机构循环统计数据
			for (Map<String, Object> map : flList) {
				Map<String, Object> resMap = new HashMap<String, Object>();
				StringBuffer sbSql = new StringBuffer("select sum(a.medfee_sumamt) medfee_sumamt, sum(a.fund_pay_sumamt) fund_pay_sumamt, sum(a.acct_pay) acct_pay, count(*) bs  ");
				sbSql.append("from ins_st_qg a inner join INS_PV_QG b on a.PK_INSPVQG = b.PK_INSPVQG where a.setl_time>=?+' 00:00:00' and a.setl_time<=?+' 23:59:59' ");
				sbSql.append("and a.insutype = ? and a.clr_optins = ? and a.del_flag = '0' and b.insuplc_admdvs= ? ");
				Map<String, Object> dscMap = DataBaseHelper.queryForMap(sbSql.toString(), dateBegin, dateEnd, map.get("insutype"), map.get("clrOptins"), map.get("clrOptins"));
				resMap.put("insutype", map.get("insutype").toString());
				resMap.put("clrOptins", map.get("clrOptins").toString());
				//调用3201对总账
				Input3201Data data = new Input3201Data();
				data.setInsutype(map.get("insutype").toString());
				data.setClr_type("21"); //21:住院
				data.setSetl_optins(map.get("clrOptins").toString());
				data.setStmt_begndate(dateBegin);
				data.setStmt_enddate(dateEnd);
				data.setMedfee_sumamt(dscMap.get("medfeeSumamt").toString());
				data.setFund_pay_sumamt(dscMap.get("fundPaySumamt").toString());
				//data.setAcct_pay(dscMap.get("acctPay").toString());
				data.setAcct_pay("0.00");
				data.setFixmedins_setl_cnt(dscMap.get("bs").toString());
				data.setREFD_SETL_FLAG("0");
				Input3201 input = new Input3201();
				input.setData(data);
				OutputData3201 out = YbFunUtils.fun3201(input, signIn.getSignNo());
				if(out.getInfcode()==null || !out.getInfcode().equals("0")){
					resMap.put("callResult", "功能调用失败");
					resMap.put("callDescription", out.getErr_msg()+out.getMessage());
					resMap.put("stmtRslt", "");
					resMap.put("stmtRsltDscr", "");
				}else{
					resMap.put("callResult", "功能调用成功");
					resMap.put("callDescription", "");
					resMap.put("stmtRslt", out.getOutput().getStmtinfo().getStmt_rslt());
					resMap.put("stmtRsltDscr", out.getOutput().getStmtinfo().getStmt_rslt_dscr());
				}
				resList.add(resMap);
			}
			returnMap.put("code", "0");
			returnMap.put("msg",  "");
			returnMap.put("resList",  resList);
		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg",  signIn.getMsg());
			returnMap.put("resList",  null);
		}
		return returnMap;
	}

	
	/**
	 * 全国医保费用结算明细组装并上传
	 * @param paramList
	 * @param user
	 * @return
	 */
	public Map<String, Object> uploadDetailedFile(List<Map<String, Object>> paramList, InsZsbaSignInQg signIn, IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(paramList.size()<=0){
			returnMap.put("code", "-1");
			returnMap.put("msg", "his没有结算记录，无需对账！");
		}
		try {
			String respon;
			String fileName = DateUtils.formatDate(new Date(),"yyyyMMddHHmmss");
			//在当前路径下配置文件夹
			File file = new File("");
	        String filePath = file.getCanonicalPath();
	        int idx = filePath.lastIndexOf("\\");
	        filePath = filePath.substring(0, idx)+"\\logs\\ybscdzlog\\";
	        String filePathParent = filePath+com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.DateUtils.getYear()+"/"+com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.DateUtils.getMonth();
	        filePath = filePathParent +"/"+fileName+".zip";
	        file = new File(filePath);
	        // 先得到文件的上级目录，判断并创建文件
	        if(!file.getParentFile().exists()){
				//file.mkdir();
				FileUtils.createDirectory(filePathParent);
			}
			
			//执行创建
			file.createNewFile();
			//输出流
			FileOutputStream fos = new FileOutputStream(file);
			//zip写入流
			ZipOutputStream zos = new ZipOutputStream(fos);
			//获取最终命名的规则(此处以.txt命名，也可以其它方式命名)
			String name = new String((fileName).getBytes("UTF-8"))+ ".txt";
			//创建ZIP实体，并添加进压缩包
			ZipEntry zipEntry = new ZipEntry(name);
			zos.putNextEntry(zipEntry);
			for(int i=0;i<paramList.size();i++){
				//读取待压缩的文件并写进压缩包里
				zos.write((MapUtils.getString(paramList.get(i),"setlId","") + "\t").getBytes("UTF-8"));
				zos.write((MapUtils.getString(paramList.get(i),"mdtrtId","") + "\t").getBytes("UTF-8"));
				zos.write((MapUtils.getString(paramList.get(i),"psnNo","") + "\t").getBytes("UTF-8"));
				zos.write((MapUtils.getString(paramList.get(i),"medfeeSumamt","") + "\t").getBytes("UTF-8"));
				zos.write((MapUtils.getString(paramList.get(i),"fundPaySumamt","") + "\t").getBytes("UTF-8"));
				zos.write((MapUtils.getString(paramList.get(i),"acctPay","") + "\t").getBytes("UTF-8"));
				if(i==(paramList.size()-1)){
					zos.write((MapUtils.getString(paramList.get(i),"refdSetlFlag","")).getBytes("UTF-8"));
				}else{
					zos.write((MapUtils.getString(paramList.get(i),"refdSetlFlag","")+"\n").getBytes("UTF-8"));
				}
			}
			//最终记得要关闭流
			zos.flush();
			zos.close();
			fos.close();
			FileInputStream fileInputStream = new FileInputStream(file);

			Input9101FsUploadIn data = new Input9101FsUploadIn();
			data.setIn(FileUtils.readFileToByteArray(file));
			data.setFilename(file.getName());
			data.setFixmedins_code("H44200100009");
			
			Input9101 input9101 = new Input9101();
			input9101.setFsUploadIn(data);
			//上传文件
			OutputData9101 out9101 = YbFunUtils.fun9101(input9101, signIn.getSignNo());
			if(out9101.getInfcode()!=null && out9101.getInfcode().equals("0")){
				returnMap.put("code", "0");
				returnMap.put("msg", "");
				returnMap.put("fileQuryNo", out9101.getOutput().getFile_qury_no());
			}else{
				returnMap.put("code", "-1");
				returnMap.put("msg", out9101.getErr_msg()+out9101.getMessage());
			}
		} catch (IOException e) {
			returnMap.put("code", "-1");
			returnMap.put("msg", e.getMessage());
		}
		return returnMap;
	}

	
	/**
	 * 对明细
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveDetailedCheck(String param,IUser user) throws ParseException{
		//1、对总账不平的情况下，调用【3202-医药机构费用结算对明细账】进行核对明细数；
		//2、调用[9101文件上传]，上传[3202对明细账]要求的对账明细文件，获取到上传成功的“文件查询号”;
		//3、调用[3202]把刚才的"文件查询号”填到3202入参里面，获取3202返回的“文件查询号”
		//4、调用9102文件下载，下载3202返回的"文件查询号”获取到对账明细文件;
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String dateBegin = jo.getString("dateBegin");
		String dateEnd = jo.getString("dateEnd");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		String insutype = jo.getString("insutype");
		String setlOptins = jo.getString("setlOptins");
		String insuType = jo.getString("insutype");
		
		//上传明细
		StringBuffer sql = new StringBuffer("select a.setl_id, a.mdtrt_id, a.psn_no, a.medfee_sumamt, a.fund_pay_sumamt, '0' as acct_pay,");
		sql.append(" case when a.MEDFEE_SUMAMT>0 then '0' else '1' end refd_setl_flag ");
		//sql.append(" from INS_ST_QG where SETL_TIME >= ?+' 00:00:00'  and SETL_TIME <=?+' 23:59:59' and insutype = ? and clr_optins = ?");
		sql.append(" from INS_ST_QG  a inner join INS_PV_QG b on a.PK_INSPVQG = b.PK_INSPVQG  where a.SETL_TIME >= ?+' 00:00:00'  and a.SETL_TIME <=?+' 23:59:59' and a.clr_optins = ? and a.del_flag ='0' and b.insuplc_admdvs= ? ");
		List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sql.toString(), dateBegin, dateEnd, setlOptins, setlOptins);
		if(mapList==null || mapList.size()==0){
			returnMap.put("code", "-1");
			returnMap.put("msg", "查不到明细数据");
		}else{
			InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
			if(signIn.getCode().equals("0")){
				Map<String, Object> map9101 = uploadDetailedFile(mapList, signIn, user);
				if(map9101.get("code").equals("0")){
					//获取3202的入参
					sql = new StringBuffer("select sum(a.medfee_sumamt) as medfee_sumamt, sum(a.fund_pay_sumamt) as fund_pay_sumamt, sum(a.psn_part_amt) as cash_payamt, count(*) as fixmedins_setl_cnt ");
					sql.append(" from INS_ST_QG a inner join INS_PV_QG b on a.PK_INSPVQG = b.PK_INSPVQG where a.SETL_TIME >= ?+' 00:00:00'  and a.SETL_TIME <=?+' 23:59:59' and a.insutype = ? and a.clr_optins = ? and a.del_flag ='0' and b.insuplc_admdvs= ? ");
					Map<String, Object> mapSum = DataBaseHelper.queryForMap(sql.toString(), dateBegin, dateEnd, insutype, setlOptins, setlOptins);
					//获取下载的文件号
					Input3202Data data3202 = new Input3202Data();
					data3202.setSetl_optins(setlOptins);
					data3202.setFile_qury_no(map9101.get("fileQuryNo").toString());
					data3202.setStmt_begndate(dateBegin);
					data3202.setStmt_enddate(dateEnd);
					data3202.setMedfee_sumamt(mapSum.get("medfeeSumamt")==null?"0.00":mapSum.get("medfeeSumamt").toString());
					data3202.setFund_pay_sumamt(mapSum.get("fundPaySumamt")==null?"0.00":mapSum.get("fundPaySumamt").toString());
					data3202.setCash_payamt(mapSum.get("cashPayamt")==null?"0.00":mapSum.get("cashPayamt").toString());
					data3202.setFixmedins_setl_cnt(mapSum.get("fixmedinsSetlCnt")==null?"0.00":mapSum.get("fixmedinsSetlCnt").toString());
					data3202.setREFD_SETL_FLAG("0");
					data3202.setClr_type("21");
					Input3202 input3202 = new Input3202();
					input3202.setData(data3202);
					OutputData3202 out3202 = YbFunUtils.fun3202(input3202, signIn.getSignNo());
					if(out3202.getInfcode()!=null && out3202.getInfcode().equals("0")){
						InsRecLedgerQg recDet = new InsRecLedgerQg();
						recDet.setSetlOptins(data3202.getSetl_optins());
						recDet.setFileQuryNo(data3202.getFile_qury_no());
						recDet.setStmtBegndate(data3202.getStmt_begndate());
						recDet.setStmtEnddate(data3202.getStmt_enddate());
						recDet.setMedfeeSumamt(data3202.getMedfee_sumamt());
						recDet.setFundPaySumamt(data3202.getFund_pay_sumamt());
						recDet.setCashPayamt(data3202.getCash_payamt());
						recDet.setFixmedinsSetlCnt(Integer.parseInt(data3202.getFixmedins_setl_cnt()));
						recDet.setFileQuryNoFileinfo(out3202.getOutput().getFileinfo().getFile_qury_no());
						recDet.setFilename(out3202.getOutput().getFileinfo().getFilename());
						recDet.setDldEndtime(out3202.getOutput().getFileinfo().getDld_endtime()==null?null:DateUtils.parseDate(out3202.getOutput().getFileinfo().getDld_endtime(), "yyyy-MM-dd HH:mm:ss"));
						recDet.setInsutype(insuType);
						DataBaseHelper.insertBean(recDet);
						returnMap.put("code", "0");
						returnMap.put("msg", "成功！");
						
						//上传和下载分开
						//下载文件
						/*Input9102FsDownloadIn data9102 = new Input9102FsDownloadIn();
						data9102.setFile_qury_no(out3202.getOutput().getFileinfo().getFile_qury_no());
						data9102.setFilename(out3202.getOutput().getFileinfo().getFilename());
						data9102.setFixmedins_code("H44200100009");
						Input9102 input9102 = new Input9102();
						input9102.setFsDownloadIn(data9102);
						OutputData9102 out9102 = YbFunUtils.fun9102(input9102, signIn.getSignNo());
						if(out9102.getInfcode()!=null && out9102.getInfcode().equals("0")){
							returnMap.put("code", "0");
							returnMap.put("msg", "成功！");
						}else{
							returnMap.put("code", "-1");
							returnMap.put("msg", out9102.getErr_msg()+out9102.getMessage());
						}*/
					}else{
						returnMap.put("code", "-1");
						returnMap.put("msg", out3202.getErr_msg()+out3202.getMessage());
					}
				}else{
					returnMap.put("code", "-1");
					returnMap.put("msg", map9101.get("msg").toString());
				}	
			}else{
				returnMap.put("code", signIn.getCode());
				returnMap.put("msg",  signIn.getMsg());
			}
		}
		
		return returnMap;
	}

	/**
	 * 下载明细
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException
	 */
	public Map<String, Object> saveDownloadDetailed(String param,IUser user) throws ParseException{
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String dateBegin = jo.getString("dateBegin");
		String dateEnd = jo.getString("dateEnd");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		String insutype = jo.getString("insutype");
		String setlOptins = jo.getString("setlOptins");
		String insuType = jo.getString("insutype");
		
		String sql = "select * from INS_REC_LEDGER_QG where STMT_BEGNDATE = ? and STMT_ENDDATE = ? and SETL_OPTINS = ? and INSUTYPE = ?";
		InsRecLedgerQg recDet = DataBaseHelper.queryForBean(sql, InsRecLedgerQg.class, dateBegin, dateEnd, setlOptins, insuType);
		if(recDet!=null){
			InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
			if(signIn.getCode().equals("0")){
				Input9102FsDownloadIn data9102 = new Input9102FsDownloadIn();
				data9102.setFile_qury_no(recDet.getFileQuryNoFileinfo());
				data9102.setFilename(recDet.getFilename());
				data9102.setFixmedins_code("H44200100009");
				Input9102 input9102 = new Input9102();
				input9102.setFsDownloadIn(data9102);
				OutputData9102 out9102 = YbFunUtils.fun9102(input9102, signIn.getSignNo());
				if(out9102.getInfcode()!=null && out9102.getInfcode().equals("0")){
					recDet.setFileAddress(out9102.getFileAddr());
					//将下载回来的文件解压，并读取txt的内容存到数据库中
					//解压
					FileUtils.unZipFiles(out9102.getFileAddr(), out9102.getFileAddr().substring(0, out9102.getFileAddr().length()-4), "UTF-8");
					//读取txt文件
					List<String> list = readFileContent(out9102.getFileAddr().substring(0, out9102.getFileAddr().length()-4)+"/result.txt");
					for (String str : list) {
						InsRecDetailedQg recData = new InsRecDetailedQg();
						String[] strArr = str.split("	");
						recData.setPkRecledgerqg(recDet.getPkRecledgerqg());
						if(!strArr[0].equals("null"))
							recData.setPsnNo(strArr[0]);
						if(!strArr[1].equals("null"))
							recData.setMdtrtId(strArr[1]);
						if(!strArr[2].equals("null"))
							recData.setSetlId(strArr[2]);
						if(!strArr[3].equals("null"))
							recData.setMsgId(strArr[3]);
						if(!strArr[4].equals("null"))
							recData.setStmtRslt(strArr[4]);
						if(!strArr[5].equals("null"))
							recData.setRefdSetlFlag(strArr[5]);
						if(!strArr[6].equals("null"))
							recData.setMemo(strArr[6]);
						if(!strArr[7].equals("null"))
							recData.setHicentMedfeeSumamt(strArr[7]);
						if(!strArr[8].equals("null"))
							recData.setHifPaySumamt(strArr[8]);
						if(!strArr[9].equals("null"))
							recData.setHicentAcctPay(strArr[9]);
						DataBaseHelper.insertBean(recData);
					}
					DataBaseHelper.updateBeanByPk(recDet);
					returnMap.put("code", "0");
					returnMap.put("msg", "成功！");
				}else{
					returnMap.put("code", "-1");
					returnMap.put("msg", out9102.getErr_msg()+out9102.getMessage());
				}
			}else{
				returnMap.put("code", signIn.getCode());
				returnMap.put("msg",  signIn.getMsg());
			}
		}else{
			returnMap.put("code", "-1");
			returnMap.put("msg", dateBegin+"到"+dateEnd+",经办机构为："+setlOptins+",险种类型为："+insuType+"的明细未上传对账，请先上传对账再下载！");
		}
		return returnMap;
	}
	
	
	public static void main(String[] args) {
		
		//FileUtils.unZipFiles("E:/Java/MyEclipse 2015/dz/2021/05/20210526143851.zip", "E:/Java/MyEclipse 2015/dz/2021/05/20210526143851", "UTF-8");
		List<String> list = readFileContent("E:/Java/MyEclipse 2015/dz/2021/05/20210526143851/result.txt");
		for (String string : list) {
			System.out.println(string);
			String[] strArr = string.split("	");
			for (String string2 : strArr) {
				System.out.println(string2);
			}
		}
	}
	
	public static List<String> readFileContent(String fileName) {
		List<String> list = new ArrayList<String>();
	    File file = new File(fileName);
	    BufferedReader reader = null;
	    try {
	    	InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");   
	        reader = new BufferedReader(isr); 
	        String tempStr;
	        while ((tempStr = reader.readLine()) != null) {
	        	list.add(tempStr);
	        }
	        reader.close();
	        return list;
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
	        }
	    }
	    return list;
	}
	
	/**
	 * 提取异地清分明细
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveData3260(String param,IUser user){
		//1.判断是否已下载
		//2.保存改月份的记录
		//3.下载明细并保存
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String dateStr = jo.getString("dateStr");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		String flagAgain = jo.getString("flagAgain"); //1是0否   1：下载  0：已下载的不再下载
		
		String[] sourceArray = dateStr.split("-");
		String year = sourceArray[0];
		String month = sourceArray[1];
		
		if(flagAgain.equals("1")){
			String sql = "select * from INS_CLEARBRANCH_QG where TRT_YEAR = ? and TRT_MONTH = ? and del_flag = '0'";
			InsZsbaClearbranchQg qf = DataBaseHelper.queryForBean(sql, InsZsbaClearbranchQg.class,  year, month);
			if(qf==null){
				returnMap = saveYdqfmx(ip, mac, year, month);
			}else{
				//先检查该月份是否已清分确认，已清分确认的不允许重新下载
				sql = "select count(*) as qrsl from INS_CLEARBRANCH_DETAILED_QG where PK_INSCLEARBRANCHQG = ? and cnfm_flag = '1'";
				Map<String, Object> qrqfMap = DataBaseHelper.queryForMap(sql, qf.getPkInsclearbranchqg());
				if(qrqfMap!=null&&qrqfMap.get("qrsl")!=null && Integer.parseInt(qrqfMap.get("qrsl").toString())>0){
					returnMap.put("code", "-1");
					returnMap.put("msg",  "该月清分明细已有确认清分数据，不允许重新下载！");
				}else{
					//没有清分确认的数据的话，先删除数据，在重新下载
					DataBaseHelper.execute("delete INS_CLEARBRANCH_DETAILED_QG where PK_INSCLEARBRANCHQG=? ", qf.getPkInsclearbranchqg());
					DataBaseHelper.execute("delete INS_CLEARBRANCH_QG where PK_INSCLEARBRANCHQG=? ", qf.getPkInsclearbranchqg());
					returnMap = saveYdqfmx(ip, mac, year, month);
				}
			}
		}else{
			String sql = "select * from INS_CLEARBRANCH_QG where TRT_YEAR = ? and TRT_MONTH = ? and del_flag = '0'";
			InsZsbaClearbranchQg qf = DataBaseHelper.queryForBean(sql, InsZsbaClearbranchQg.class,  year, month);
			if(qf==null){
				returnMap = saveYdqfmx(ip, mac, year, month);
			}else{
				returnMap.put("code", "-11");
				returnMap.put("msg",  "该月清分明细已下载！");
			}
		}
		
		return returnMap;
	}

	/**
	 * 保存异地清分明细
	 * @param ip
	 * @param mac
	 * @param year
	 * @param month
	 * @return
	 */
	public Map<String, Object> saveYdqfmx(String ip, String mac, String year, String month){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			List<Output3260Data> data3206 = new ArrayList<Output3260Data>();
			String error = null;
			for(int i=1; i<100000; i+=100){
				Input3260Data data = new Input3260Data();
				data.setTrt_year(year);
				data.setTrt_month(month);
				data.setStartrow(i+"");
				Input3260 input = new Input3260();
				input.setData(data);
				OutputData3260 out = YbFunUtils.fun3260(input, signIn.getSignNo());
				if(out.getInfcode()!=null && out.getInfcode().equals("0")){
					data3206.addAll(out.getOutput().getData());
					if(out.getOutput().getData().size()!=100){
						break;
					}
				}else{
					error = out.getErr_msg()+out.getMessage();
					break;
				}
			}
			if(error!=null){
				returnMap.put("code", "-1");
				returnMap.put("msg", error);
			}else{
				//保存数据
				InsZsbaClearbranchQg qf = new InsZsbaClearbranchQg();
				qf.setTrtYear(year);
				qf.setTrtMonth(month);
				DataBaseHelper.insertBean(qf);
				for (Output3260Data output3260Data : data3206) {
					InsZsbaClearbranchDetailedQg qfmx = new InsZsbaClearbranchDetailedQg();
					qfmx.setPkInsclearbranchqg(qf.getPkInsclearbranchqg());
					qfmx.setSeqno(output3260Data.getSeqno());
					qfmx.setMdtrtarea(output3260Data.getMdtrtarea());
					qfmx.setMedinsNo(output3260Data.getMedins_no());
					qfmx.setCertno(output3260Data.getCertno());
					qfmx.setMdtrtId(output3260Data.getMdtrt_id());
					qfmx.setMdtrtSetlTime(output3260Data.getMdtrt_setl_time());
					qfmx.setSetlSn(output3260Data.getSetl_sn());
					qfmx.setFulamtAdvpayFlag(output3260Data.getFulamt_advpay_flag());
					qfmx.setMedfeeSumamt(output3260Data.getMedfee_sumamt());
					qfmx.setOptinsPaySumamt(output3260Data.getOptins_pay_sumamt());
					DataBaseHelper.insertBean(qfmx);
				}
				returnMap.put("code", "0");
				returnMap.put("msg",  "提取异地清分明细成功！");
			}
		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg",  signIn.getMsg());
		}
		return returnMap;
	}
	
	/**
	 * 查询异地清分明细和本地明细
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getData3260(String param,IUser user){
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String dateStr = jo.getString("dateStr");
		
		String[] sourceArray = dateStr.split("-");
		String year = sourceArray[0];
		String month = sourceArray[1];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDate = cal.getTime();//最后一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDate = cal.getTime();//第一天
		String beginDate = DateUtils.formatDate(firstDate, "yyyy-MM-dd") + "0 0:00:00";
		String endDate = DateUtils.formatDate(lastDate, "yyyy-MM-dd") + " 23:59:59";
		
		String sql = "select * from INS_CLEARBRANCH_QG where TRT_YEAR = ? and TRT_MONTH = ? and del_flag = '0'";
		InsZsbaClearbranchQg qf = DataBaseHelper.queryForBean(sql, InsZsbaClearbranchQg.class,  year, month);
		if(qf!=null){
			StringBuffer sb = new StringBuffer("select a.pk_insstqg, a.setl_id, CONVERT(varchar(100), a.setl_time, 120) as setl_time, a.medfee_sumamt as medfee_sumamt_his, a.fund_pay_sumamt, ");
			sb.append(" case when b.cnfm_flag is null then '未清分' else '已清分' end as qfStatus, ");
			sb.append(" b.PK_INSCLEDETQG, b.mdtrtarea, b.medins_no, b.certno, b.mdtrt_id, CONVERT(varchar(100), b.mdtrt_setl_time, 120) as mdtrt_setl_time, b.setl_sn,  ");
			sb.append(" b.fulamt_advpay_flag,b.medfee_sumamt, b.optins_pay_sumamt, b.cnfm_flag ");
			sb.append(" from (select t1.* from ins_st_qg t1 inner join ins_pv_qg t2 on t1.pk_inspvqg = t2.pk_inspvqg");
			sb.append(" inner join bd_hp t3 on t3.pk_hp = t2.pk_insu");
			sb.append(" where t3.code in ('00058', '00059')");
			//sb.append(" and t1.MSGID3261 is null ");
			//sb.append(" and t1.setl_time >=?");
			sb.append(" and t1.setl_time <=?");
			sb.append(" and ((t1.FLAG_REC='1' and TRT_YEAR_MONTH = ?) OR (t1.FLAG_REC = '0' or t1.FLAG_REC is null))");
			sb.append(" and t1.del_flag = '0' and t2.del_flag = '0') a ");
			sb.append(" full join ");
			sb.append(" (select t2.* from INS_CLEARBRANCH_QG t1 inner join INS_CLEARBRANCH_DETAILED_QG t2 on t1.PK_INSCLEARBRANCHQG = t2.PK_INSCLEARBRANCHQG");
			sb.append(" where t1.TRT_YEAR = ? and t1.TRT_MONTH = ? and t1.del_flag = '0') b");
			sb.append(" on a.setl_id = b.setl_sn");
			sb.append(" order by b.mdtrt_setl_time, a.setl_time ");
			List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sb.toString(), endDate, year+"-"+month, year, month);
			
			for (Map<String, Object> map : mapList) {
				map.put("insType", "0");//全国异地
				map.put("status", "");
/*				if(map.get("cnfmFlag")==null){
					map.put("cnfmFlag", "1");
				}*/
				if(map.get("pkInsstqg")==null){
					if(map.get("mdtrtSetlTime").toString().subSequence(0, 7).equals("2020-12")){
						StringBuffer nsSql = new StringBuffer("select pk_insst as pk_insstqg, CONVERT(varchar(100), akc194, 120) as setl_time, ykc700, ykc618 as setl_id, ");
						nsSql.append(" akc264 as medfee_sumamt_his, akb068 as fund_pay_sumamt, cnfm_flag  ");
						nsSql.append(" from ins_st_snyb where ykc700 = ? and ykc618 = ? and del_flag = '0'");
						Map<String, Object> hisMap = DataBaseHelper.queryForMap(nsSql.toString(), map.get("mdtrtId"), map.get("setlSn"));
						if(hisMap == null){
							StringBuffer ksSql = new StringBuffer("select pk_insst as pk_insstqg, CONVERT(varchar(100), akc194, 120) as setl_time, ykc700, aaz216 as setl_id, ");
							ksSql.append(" akc264 as medfee_sumamt_his, ake149 as fund_pay_sumamt, ykc707 as cnfm_flag  ");
							ksSql.append(" from ins_st_ksyb where ykc700 = ? and aaz216 = ? and del_flag = '0'");
							hisMap = DataBaseHelper.queryForMap(ksSql.toString(), map.get("mdtrtId").toString(), map.get("setlSn").toString());
						}else{
							map.put("insType", "1");//旧医保省内异地
						}
						if(hisMap!=null){
							if(!map.get("insType").toString().equals("1")){
								map.put("insType", "2");//旧医保跨省异地
							}
							map.put("cnfmFlag", hisMap.get("cnfmFlag"));
							map.put("pkInsstqg", hisMap.get("pkInsstqg"));
							map.put("setlTime", hisMap.get("setlTime"));
							map.put("setlId", hisMap.get("setlId"));
							map.put("medfeeSumamtHis", hisMap.get("medfeeSumamtHis"));
							map.put("fundPaySumamt", hisMap.get("fundPaySumamt"));
							BigDecimal medfeeSumamt = map.get("medfeeSumamtHis")==null?new BigDecimal("0"):new BigDecimal(map.get("medfeeSumamtHis").toString());
							BigDecimal fundPaySumamt = map.get("fundPaySumamt")==null?new BigDecimal("0"):new BigDecimal(map.get("fundPaySumamt").toString());
							BigDecimal medfeeSumamtQf = map.get("medfeeSumamt")==null?new BigDecimal("0"):new BigDecimal(map.get("medfeeSumamt").toString());
							BigDecimal optinsPaySumamt = map.get("optinsPaySumamt")==null?new BigDecimal("0"):new BigDecimal(map.get("optinsPaySumamt").toString());

							if(map.get("cnfmFlag")==null){
								map.put("cnfmFlag", "1");
							}
							
							if(medfeeSumamt.compareTo(medfeeSumamtQf)!=0){
								if(map.get("cnfmFlag")==null){
									map.put("cnfmFlag", "0");
								}
								map.put("status", "总金额对不上");
							}
							if(fundPaySumamt.compareTo(optinsPaySumamt)!=0){
								if(map.get("cnfmFlag")==null){
									map.put("cnfmFlag", "0");
								}
								if(map.get("status")!=null && map.get("status").toString().trim().length()!=0){
									map.put("status", map.get("status")+",统筹对不上");
								}else{
									map.put("status", "统筹对不上");
								}
							}
							if(map.get("cnfmFlag")==null){
								map.put("cnfmFlag", "1");
							}
						}else{
							map.put("status", "医保单边账");
							if(map.get("cnfmFlag")==null){
								map.put("cnfmFlag", "0");
							}
						}
					}else{
						map.put("status", "医保单边账");
						if(map.get("cnfmFlag")==null){
							map.put("cnfmFlag", "0");
						}
					}
				}else if (map.get("pkInscledetqg")==null){
					map.put("status", "HIS单边账");
					map.put("cnfmFlag", "0");
				}else{
					BigDecimal medfeeSumamt = map.get("medfeeSumamtHis")==null?new BigDecimal("0"):new BigDecimal(map.get("medfeeSumamtHis").toString());
					BigDecimal fundPaySumamt = map.get("fundPaySumamt")==null?new BigDecimal("0"):new BigDecimal(map.get("fundPaySumamt").toString());
					BigDecimal medfeeSumamtQf = map.get("medfeeSumamt")==null?new BigDecimal("0"):new BigDecimal(map.get("medfeeSumamt").toString());
					BigDecimal optinsPaySumamt = map.get("optinsPaySumamt")==null?new BigDecimal("0"):new BigDecimal(map.get("optinsPaySumamt").toString());
					//BigDecimal medfeeSumamt = map.get("medfeeSumamtHis")==null?0:(new BigDecimal(map.get("medfeeSumamtHis").toString())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					//BigDecimal fundPaySumamt = map.get("fundPaySumamt")==null?0:(new BigDecimal(map.get("fundPaySumamt").toString())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					//BigDecimal medfeeSumamtQf = map.get("medfeeSumamt")==null?0:(new BigDecimal(map.get("medfeeSumamt").toString())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					//BigDecimal optinsPaySumamt = map.get("optinsPaySumamt")==null?0:(new BigDecimal(map.get("optinsPaySumamt").toString())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					
					if(medfeeSumamt.compareTo(medfeeSumamtQf)!=0){
						if(map.get("cnfmFlag")==null){
							map.put("cnfmFlag", "0");
						}
						map.put("status", "总金额对不上");
					}
					if(fundPaySumamt.compareTo(optinsPaySumamt)!=0){
						if(map.get("cnfmFlag")==null){
							map.put("cnfmFlag", "0");
						}
						if(map.get("status")!=null && map.get("status").toString().trim().length()!=0){
							map.put("status", map.get("status")+",统筹对不上");
						}else{
							map.put("status", "统筹对不上");
						}
					}
					if(map.get("cnfmFlag")==null){
						map.put("cnfmFlag", "1");
					}
				}
			}
			returnMap.put("code", "0");
			returnMap.put("msg",  "查询异地清分明细和本地明细成功！");
			returnMap.put("list", mapList);
		}else{
			returnMap.put("code", "-1");
			returnMap.put("msg",  "该月份没有清分明细，请先下载!");
			returnMap.put("list", null);
		}
		return returnMap;
	}

	/**
	 * 5.1.1.2异地清分结果确认
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveData3261(String param,IUser user){
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String dateStr = jo.getString("dateStr");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		String totalrow = jo.getString("totalrow");
		String DataTable = jo.getString("list");
		JSONArray jsonArray = JSONArray.fromObject(DataTable);
		List<Input3261Detail> detailList = new ArrayList<Input3261Detail>();
		List<InsQgQfEntity> qfList = jsonArray.toList(jsonArray, InsQgQfEntity.class);
		
		String[] sourceArray = dateStr.split("-");
		String year = sourceArray[0];
		String month = sourceArray[1];
		
		for (InsQgQfEntity qf : qfList) {
			Input3261Detail detail = new Input3261Detail();
			detail.setCertno(qf.getCertno());
			detail.setCnfm_flag(qf.getCnfmFlag());
			detail.setMdtrt_id(qf.getMdtrtId());
			detail.setMdtrt_setl_time(qf.getMdtrtSetlTime());
			detail.setMedfee_sumamt(qf.getMedfeeSumamt());
			detail.setOptins_pay_sumamt(qf.getOptinsPaySumamt());
			detail.setSetl_sn(qf.getSetlSn());
			detailList.add(detail);
		}
		
		Input3261Data data = new Input3261Data();
		data.setTrt_year(year);
		data.setTrt_month(month);
		data.setTotalrow(totalrow);
		
		Input3261 input = new Input3261();
		input.setData(data);
		input.setDetail(detailList);
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			OutputData3261 out = YbFunUtils.fun3261(input, signIn.getSignNo());
			if(out.getInfcode()!=null && out.getInfcode().equals("0")){
				for (InsQgQfEntity qfmx : qfList) {
					String sql = "update INS_CLEARBRANCH_DETAILED_QG set cnfm_flag = ? where  PK_INSCLEDETQG = ?";
					DataBaseHelper.execute(sql, qfmx.getCnfmFlag(), qfmx.getPkInscledetqg());
					//DataBaseHelper.updateBeanByPk(qfmx, false);
					if(qfmx.getInsType().equals("2")){
						sql = "update ins_st_ksyb set yzz060 = ?, yzz061 = ?, transid6521 = ?, ykc707 = ?, transid6522 = null where  pk_insst = ?";
						DataBaseHelper.execute(sql, year, month, out.getMsgid(), qfmx.getCnfmFlag(), qfmx.getPkInsstqg());
					}else if(qfmx.getInsType().equals("1")){
						sql = "update ins_st_snyb set yzz060 = ?, yzz061 = ?, TRANSID3261 = ?, CNFM_FLAG = ?, TRANSID3262 = null where  pk_insst = ?";
						DataBaseHelper.execute(sql, year, month, out.getMsgid(), qfmx.getCnfmFlag(), qfmx.getPkInsstqg());
					}else{
						sql = "update ins_st_qg set TRT_YEAR = ?, TRT_MONTH = ?, MSGID3261 = ?, CNFM_FLAG = ?, MSGID3262 = null where  PK_INSSTQG = ?";
						DataBaseHelper.execute(sql, year, month, out.getMsgid(), qfmx.getCnfmFlag(), qfmx.getPkInsstqg());
					}
				}
				
				//修改结算表记录
				if(qfList.size()>0){
					String sql = "select * from INS_CLEARBRANCH_QG where TRT_YEAR = ? and TRT_MONTH = ? and del_flag = '0'";
					InsZsbaClearbranchQg qf = DataBaseHelper.queryForBean(sql, InsZsbaClearbranchQg.class,  year, month);
					if(qf!=null && qf.getPkInsclearbranchqg()!=null){
						sql = "update INS_ST_QG set FLAG_REC = '1', TRT_YEAR_MONTH = ? where setl_id in ("
								+" select setl_sn from INS_CLEARBRANCH_DETAILED_QG where PK_INSCLEARBRANCHQG = ? and cnfm_flag = '1')";
						DataBaseHelper.execute(sql, year+ "-" +month, qf.getPkInsclearbranchqg());
					}
					
				}
					
				returnMap.put("code", "0");
				returnMap.put("msg",  "异地清分结果确认成功");
			}else{
				returnMap.put("code", "-1");
				returnMap.put("msg",  out.getErr_msg()+out.getMessage());
			}
		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg",  signIn.getMsg());
		}
		return returnMap;
	}

	/**
	 * 5.1.1.3异地清分结果确认回退
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> cancelData3262(String param,IUser user){
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String dateStr = jo.getString("dateStr");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		
		String[] sourceArray = dateStr.split("-");
		String year = sourceArray[0];
		String month = sourceArray[1];
		
		Input3262Data data = new Input3262Data();
		data.setTrt_year(year);
		data.setTrt_month(month);//传入‘0’时，回退医疗机构该结算周期的所有确认业务
		data.setOtransid("0");
		
		Input3262 input = new Input3262();
		input.setData(data);
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			OutputData3262 out = YbFunUtils.fun3262(input, signIn.getSignNo());
			if(out.getInfcode()!=null && out.getInfcode().equals("0")){
				String sql = "select * from INS_CLEARBRANCH_QG where TRT_YEAR = ? and TRT_MONTH = ? and del_flag = '0'";
				InsZsbaClearbranchQg qf = DataBaseHelper.queryForBean(sql, InsZsbaClearbranchQg.class,  year, month);
				sql = "update INS_CLEARBRANCH_DETAILED_QG set cnfm_flag = null where  PK_INSCLEARBRANCHQG = ?";
				DataBaseHelper.execute(sql,qf.getPkInsclearbranchqg());	
				
				sql = "update ins_st_ksyb set yzz060 = null, yzz061 = null, transid6521 = null, ykc707 = null, transid6522 = ? where  yzz060 = ? and yzz061 = ?";
				DataBaseHelper.execute(sql, out.getMsgid(), year, month);
				sql = "update ins_st_snyb set yzz060 = null, yzz061 = null, TRANSID3261 = null, CNFM_FLAG = null, TRANSID3262 = ? where  yzz060 = ? and yzz061 = ?";
				DataBaseHelper.execute(sql, out.getMsgid(), year, month);
				sql = "update ins_st_qg set TRT_YEAR = null, TRT_MONTH = null, MSGID3261 = null, CNFM_FLAG = null, MSGID3262 = ? where  TRT_YEAR = ? and TRT_MONTH = ?";
				DataBaseHelper.execute(sql, out.getMsgid(), year, month);
				returnMap.put("code", "0");
				returnMap.put("msg",  "异地清分结果确认成功");
			}else{
				returnMap.put("code", "-1");
				returnMap.put("msg",  out.getErr_msg()+out.getMessage());
			}
		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg",  signIn.getMsg());
		}
		return returnMap;
	}

	public void saveData4101(String param,IUser user){
		
	}
	
	/**
	 * 根据时间段、状态获取结算清单列表
	 * @param param
	 * @param user
	 */
	public List<Map<String, Object>> getSettlementList(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String beginDate = jo.getString("beginDate");
		String endDate = jo.getString("endDate");
		String status = jo.getString("status");
		
		StringBuffer sql = new StringBuffer(" select CONVERT(varchar(100), dcla_time, 20) as dcla_time_gsh, a.*, b.*, c.act_setl_sco from INS_ST_QG a inner join ins_pv_qg b on a.PK_INSPVQG = b.pk_inspvqg ");
		sql.append(" left join ins_qgyb_stlist_fz c on a.psn_no = c.psn_no and b.begntime = c.adm_time and b.endtime = c.dscg_time");
		sql.append(" where a.CLR_OPTINS = '442000' and b.insuplc_admdvs = '442000' and a.DEL_FLAG = '0'");
		sql.append(" and a.setl_time >= ? and a.setl_time <= ?");
		if(status.equals("02")){
			sql.append(" and a.setl_list_id is null");
		}else if(status.equals("03")){
			sql.append(" and a.setl_list_id is not null");
		}
		sql.append(" order by a.setl_time asc  ");
		List<Map<String, Object>> list = DataBaseHelper.queryForList(sql.toString(), beginDate+" 00:00:00", endDate+" 23:59:59");
		return list;
	}
	
	/**
	 * 结算清单上传成功后，修改结算表的相关数据
	 * @param param
	 * @param user
	 */
	public void updateStUploadStatus(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String setlListId = jo.getString("setlListId");
		String dclaTime = jo.getString("dclaTime");
		String medinsFillDept = jo.getString("medinsFillDept");
		String medinsFillPsn = jo.getString("medinsFillPsn");
		String pkInsstqg = jo.getString("pkInsstqg");
		DataBaseHelper.execute("update ins_st_qg set SETL_LIST_ID = ?, DCLA_TIME = ?, MEDINS_FILL_DEPT = ?, MEDINS_FILL_PSN = ? where pk_insstqg=? ", 
				setlListId, dclaTime, medinsFillDept, medinsFillPsn, pkInsstqg);
	}
	

	/**
	 * 获取结算清单打印数据
	 * @param param
	 * @param user
	 */
	public Map<String, Object> getSettlementListPrintData(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String pkInsstqg = jo.getString("pkInsstqg");
		//String ip = jo.getString("ip");
		//String mac = jo.getString("mac");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkInsstqg", pkInsstqg);
		
		InsZsbaStQg st = DataBaseHelper.queryForBean("select * from ins_st_qg where pk_insstqg = ?", InsZsbaStQg.class, pkInsstqg);
		paramMap.put("pkPv", st.getPkPv());
		
		Map<String, Object> setlinfoMap  = insQgybPvMapper.get4101SetlinfoPrint(paramMap);
		List<Map<String, Object>> payinfoMapList  = insQgybPvMapper.get4101Payinfo(paramMap);
		List<Map<String, Object>> diseinfoMapList  = insQgybPvMapper.get4101Diseinfo(paramMap);
		List<Map<String, Object>> iteminfo2301MapList  = insQgybPvMapper.get4101Iteminfo2301(paramMap);
		List<Map<String, Object>> iteminfo5204MapList  = insQgybPvMapper.get4101Iteminfo5204(paramMap);
		List<Map<String, Object>> oprninfoMapList  = insQgybPvMapper.get4101Oprninfo(paramMap);
		
		Map<String, Object> otherMap = new HashMap<String, Object>();
		
		//组装诊断数据
		List<Map<String, Object>> diseinfoZyMapList  = insQgybPvMapper.get4101DiseinfoZy(paramMap);
		List<Map<String, Object>> diseinfoXyMapList  = insQgybPvMapper.get4101DiseinfoXy(paramMap);
		
		List<Map<String, Object>> diseinfoPrintList = new ArrayList<Map<String,Object>>();
		if(diseinfoZyMapList.size()>=diseinfoXyMapList.size()){
			for (int i = 0; i < diseinfoZyMapList.size(); i++) {
				Map<String, Object> zyMap = diseinfoZyMapList.get(i);
				Map<String, Object> printMap = new HashMap<String, Object>();
				printMap.put("zd4", zyMap.get("diag_name").toString());
				printMap.put("zd5", zyMap.get("diag_code").toString());
				if(zyMap.get("adm_cond_type")!=null){
					if(zyMap.get("adm_cond_type").toString().equals("1")){
						printMap.put("zd6", "有");
					}else if(zyMap.get("adm_cond_type").toString().equals("2")){
						printMap.put("zd6", "临床未确定");
					}else if(zyMap.get("adm_cond_type").toString().equals("3")){
						printMap.put("zd6", "情况不明");
					}else if(zyMap.get("adm_cond_type").toString().equals("4")){
						printMap.put("zd6", "无");
					}
				}
				
				if(diseinfoXyMapList.size()>=(i+1)){
					Map<String, Object> xyMap = diseinfoXyMapList.get(i);
					printMap.put("zd1", xyMap.get("diag_name").toString());
					printMap.put("zd2", xyMap.get("diag_code").toString());
					if(xyMap.get("adm_cond_type")!=null){
						if(xyMap.get("adm_cond_type").toString().equals("1")){
							printMap.put("zd3", "有");
						}else if(xyMap.get("adm_cond_type").toString().equals("2")){
							printMap.put("zd3", "临床未确定");
						}else if(xyMap.get("adm_cond_type").toString().equals("3")){
							printMap.put("zd3", "情况不明");
						}else if(xyMap.get("adm_cond_type").toString().equals("4")){
							printMap.put("zd3", "无");
						}
					}
				}else{
					printMap.put("zd1", "");
					printMap.put("zd2", "");
					printMap.put("zd3", "");
				}
				diseinfoPrintList.add(printMap);
			}
		}else{
			for (int i = 0; i < diseinfoXyMapList.size(); i++) {
				Map<String, Object> xyMap = diseinfoXyMapList.get(i);
				Map<String, Object> printMap = new HashMap<String, Object>();
				printMap.put("zd1", xyMap.get("diag_name").toString());
				printMap.put("zd2", xyMap.get("diag_code").toString());
				if(xyMap.get("adm_cond_type")!=null){
					if(xyMap.get("adm_cond_type").toString().equals("1")){
						printMap.put("zd3", "有");
					}else if(xyMap.get("adm_cond_type").toString().equals("2")){
						printMap.put("zd3", "临床未确定");
					}else if(xyMap.get("adm_cond_type").toString().equals("3")){
						printMap.put("zd3", "情况不明");
					}else if(xyMap.get("adm_cond_type").toString().equals("4")){
						printMap.put("zd3", "无");
					}
				}
				if(diseinfoZyMapList.size()>=(i+1)){
					Map<String, Object> zyMap = diseinfoZyMapList.get(i);
					printMap.put("zd4", zyMap.get("diag_name").toString());
					printMap.put("zd5", zyMap.get("diag_code").toString());
					if(zyMap.get("adm_cond_type")!=null){
						if(zyMap.get("adm_cond_type").toString().equals("1")){
							printMap.put("zd6", "有");
						}else if(zyMap.get("adm_cond_type").toString().equals("2")){
							printMap.put("zd6", "临床未确定");
						}else if(zyMap.get("adm_cond_type").toString().equals("3")){
							printMap.put("zd6", "情况不明");
						}else if(zyMap.get("adm_cond_type").toString().equals("4")){
							printMap.put("zd6", "无");
						}
					}
				}else{
					printMap.put("zd4", "");
					printMap.put("zd5", "");
					printMap.put("zd6", "");
				}
				diseinfoPrintList.add(printMap);
			}
		}
		returnMap.put("diseinfo", diseinfoPrintList);
		
		Input4101 input = new Input4101();
		Input4101Setlinfo setlinfo = new Input4101Setlinfo();
		List<Input4101Payinfo> payinfoList = new ArrayList<Input4101Payinfo>();
		List<Input4101Iteminfo> iteminfoList = new ArrayList<Input4101Iteminfo>();
		List<Input4101Oprninfo> oprninfoList = new ArrayList<Input4101Oprninfo>();
		
		if(setlinfoMap!=null){
				if(diseinfoMapList!=null && diseinfoMapList.size()!=0)
				{
					if((iteminfo2301MapList!=null && iteminfo2301MapList.size()!=0)||(iteminfo5204MapList!=null && iteminfo5204MapList.size()!=0))
					{
						//InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
						//if(signIn.getCode().equals("0")){
						otherMap.put("curr_addr_prov", setlinfoMap.get("curr_addr_prov")==null?null:setlinfoMap.get("curr_addr_prov").toString());
						otherMap.put("curr_addr_city", setlinfoMap.get("curr_addr_city")==null?null:setlinfoMap.get("curr_addr_city").toString());
						otherMap.put("curr_addr_county", setlinfoMap.get("curr_addr_county")==null?null:setlinfoMap.get("curr_addr_county").toString());
						otherMap.put("hifp_pay", setlinfoMap.get("hifp_pay")==null?null:setlinfoMap.get("hifp_pay").toString());
						otherMap.put("oth_pay", setlinfoMap.get("oth_pay")==null?null:setlinfoMap.get("oth_pay").toString());
						otherMap.put("hifmi_pay", setlinfoMap.get("hifmi_pay")==null?null:setlinfoMap.get("hifmi_pay").toString());
						otherMap.put("maf_pay", setlinfoMap.get("maf_pay")==null?null:setlinfoMap.get("maf_pay").toString());
						otherMap.put("cvlserv_pay", setlinfoMap.get("cvlserv_pay")==null?null:setlinfoMap.get("cvlserv_pay").toString());
						otherMap.put("hifob_pay", setlinfoMap.get("hifob_pay")==null?null:setlinfoMap.get("hifob_pay").toString());
						otherMap.put("hifes_pay", setlinfoMap.get("hifes_pay")==null?null:setlinfoMap.get("hifes_pay").toString());
						
							setlinfo.setMdtrt_id(setlinfoMap.get("mdtrt_id")==null?null:setlinfoMap.get("mdtrt_id").toString());
							setlinfo.setSetl_id(setlinfoMap.get("setl_id")==null?null:setlinfoMap.get("setl_id").toString());
							setlinfo.setFixmedins_name(setlinfoMap.get("fixmedins_name")==null?null:setlinfoMap.get("fixmedins_name").toString());
							setlinfo.setFixmedins_code(setlinfoMap.get("fixmedins_code")==null?null:setlinfoMap.get("fixmedins_code").toString());
							setlinfo.setHi_setl_lv(setlinfoMap.get("hi_setl_lv")==null?null:setlinfoMap.get("hi_setl_lv").toString());
							setlinfo.setHi_no(setlinfoMap.get("hi_no")==null?null:setlinfoMap.get("hi_no").toString());
							setlinfo.setMedcasno(setlinfoMap.get("medcasno")==null?null:setlinfoMap.get("medcasno").toString());
							setlinfo.setDcla_time(setlinfoMap.get("dcla_time")==null?null:setlinfoMap.get("dcla_time").toString());
							setlinfo.setPsn_name(setlinfoMap.get("psn_name")==null?null:setlinfoMap.get("psn_name").toString());
							setlinfo.setGend(setlinfoMap.get("gend")==null?null:setlinfoMap.get("gend").toString());
							setlinfo.setBrdy(setlinfoMap.get("brdy")==null?null:setlinfoMap.get("brdy").toString());
							setlinfo.setAge(setlinfoMap.get("age")==null?null:setlinfoMap.get("age").toString());
							setlinfo.setNtly(setlinfoMap.get("ntly")==null?null:setlinfoMap.get("ntly").toString());
							setlinfo.setNwb_age(setlinfoMap.get("nwb_age")==null?null:Integer.parseInt(setlinfoMap.get("nwb_age").toString())>365?null:setlinfoMap.get("nwb_age").toString());
							setlinfo.setNaty(setlinfoMap.get("naty")==null?null:setlinfoMap.get("naty").toString());
							setlinfo.setPatn_cert_type(setlinfoMap.get("patn_cert_type")==null?null:setlinfoMap.get("patn_cert_type").toString());
							setlinfo.setCertno(setlinfoMap.get("certno")==null?null:setlinfoMap.get("certno").toString());
							setlinfo.setPrfs(setlinfoMap.get("prfs")==null?null:setlinfoMap.get("prfs").toString());
							setlinfo.setCurr_addr(setlinfoMap.get("curr_addr")==null?null:setlinfoMap.get("curr_addr").toString());
							setlinfo.setEmp_name(setlinfoMap.get("emp_name")==null?null:setlinfoMap.get("emp_name").toString());
							setlinfo.setEmp_addr(setlinfoMap.get("emp_addr")==null?null:setlinfoMap.get("emp_addr").toString());
							setlinfo.setEmp_tel(setlinfoMap.get("emp_tel")==null?null:setlinfoMap.get("emp_tel").toString());
							setlinfo.setPoscode(setlinfoMap.get("poscode")==null?null:setlinfoMap.get("poscode").toString());
							setlinfo.setConer_name(setlinfoMap.get("coner_name")==null?null:setlinfoMap.get("coner_name").toString());
							setlinfo.setPatn_rlts(setlinfoMap.get("patn_rlts")==null?null:setlinfoMap.get("patn_rlts").toString());
							setlinfo.setConer_addr(setlinfoMap.get("coner_addr")==null?null:setlinfoMap.get("coner_addr").toString());
							setlinfo.setConer_tel(setlinfoMap.get("coner_tel")==null?null:setlinfoMap.get("coner_tel").toString());
							setlinfo.setHi_type(setlinfoMap.get("hi_type")==null?null:setlinfoMap.get("hi_type").toString());
							setlinfo.setInsuplc(setlinfoMap.get("insuplc")==null?null:setlinfoMap.get("insuplc").toString());
							setlinfo.setSp_psn_type(setlinfoMap.get("sp_psn_type")==null?null:setlinfoMap.get("sp_psn_type").toString());
							setlinfo.setNwb_adm_type(setlinfoMap.get("nwb_adm_type")==null?null:setlinfoMap.get("nwb_adm_type").toString());
							setlinfo.setNwb_bir_wt(setlinfoMap.get("nwb_bir_wt")==null?null:setlinfoMap.get("nwb_bir_wt").toString());
							setlinfo.setNwb_adm_wt(setlinfoMap.get("nwb_adm_wt")==null?null:setlinfoMap.get("nwb_adm_wt").toString());
							setlinfo.setOpsp_diag_caty(setlinfoMap.get("opsp_diag_caty")==null?null:setlinfoMap.get("opsp_diag_caty").toString());
							setlinfo.setOpsp_mdtrt_date(setlinfoMap.get("opsp_mdtrt_date")==null?null:setlinfoMap.get("opsp_mdtrt_date").toString());
							setlinfo.setIpt_med_type(setlinfoMap.get("ipt_med_type")==null?null:setlinfoMap.get("ipt_med_type").toString());
							setlinfo.setAdm_way(setlinfoMap.get("adm_way")==null?null:setlinfoMap.get("adm_way").toString());
							setlinfo.setTrt_type(setlinfoMap.get("trt_type")==null?null:setlinfoMap.get("trt_type").toString());
							setlinfo.setAdm_time(setlinfoMap.get("adm_time")==null?null:setlinfoMap.get("adm_time").toString());
							setlinfo.setAdm_caty(setlinfoMap.get("adm_caty")==null?null:setlinfoMap.get("adm_caty").toString());
							//转科科别 数据库存的是名字，得单独查
							//setlinfo.setRefldept_dept(setlinfoMap.get("refldept_dept")==null?null:setlinfoMap.get("refldept_dept").toString());
							setlinfo.setDscg_time(setlinfoMap.get("dscg_time")==null?null:setlinfoMap.get("dscg_time").toString());
							setlinfo.setDscg_caty(setlinfoMap.get("dscg_caty")==null?null:setlinfoMap.get("dscg_caty").toString());
							setlinfo.setAct_ipt_days(setlinfoMap.get("act_ipt_days")==null?null:setlinfoMap.get("act_ipt_days").toString());
							setlinfo.setOtp_wm_dise(setlinfoMap.get("otp_wm_dise")==null?null:setlinfoMap.get("otp_wm_dise").toString());
							setlinfo.setWm_dise_code(setlinfoMap.get("wm_dise_code")==null?null:setlinfoMap.get("wm_dise_code").toString());
							setlinfo.setOtp_tcm_dise(setlinfoMap.get("otp_tcm_dise")==null?null:setlinfoMap.get("otp_tcm_dise").toString());
							setlinfo.setTcm_dise_code(setlinfoMap.get("tcm_dise_code")==null?null:setlinfoMap.get("tcm_dise_code").toString());
							//诊断代码计数  需要单独查
							setlinfo.setDiag_code_cnt(diseinfoMapList.size()+"");
							//手术操作代码计数
							setlinfo.setOprn_oprt_code_cnt(oprninfoMapList.size()+"");
							setlinfo.setVent_used_dura(setlinfoMap.get("vent_used_dura")==null?null:setlinfoMap.get("vent_used_dura").toString());
							String pwcry_bfadm_coma_dura = "";
							String pwcry_afadm_coma_dura = "";
							if(setlinfoMap.get("coma_day_bef")!=null){
								pwcry_bfadm_coma_dura += setlinfoMap.get("coma_day_bef").toString();
							}else{
								pwcry_bfadm_coma_dura += "0";
							}
							if(setlinfoMap.get("coma_hour_bef")!=null){
								pwcry_bfadm_coma_dura += "/"+setlinfoMap.get("coma_hour_bef").toString();
							}else{
								pwcry_bfadm_coma_dura += "/0";
							}
							if(setlinfoMap.get("coma_min_bef")!=null){
								pwcry_bfadm_coma_dura += "/"+setlinfoMap.get("coma_min_bef").toString();
							}else{
								pwcry_bfadm_coma_dura += "/0";
							}
							if(pwcry_bfadm_coma_dura.equals("0/0/0")){
								pwcry_bfadm_coma_dura = null;
							}
							if(setlinfoMap.get("coma_day_after")!=null){
								pwcry_afadm_coma_dura += setlinfoMap.get("coma_day_after").toString();
							}else{
								pwcry_afadm_coma_dura += "0";
							}
							if(setlinfoMap.get("coma_hour_after")!=null){
								pwcry_afadm_coma_dura += "/"+setlinfoMap.get("coma_hour_after").toString();
							}else{
								pwcry_afadm_coma_dura += "/0";
							}
							if(setlinfoMap.get("coma_min_after")!=null){
								pwcry_afadm_coma_dura += "/"+setlinfoMap.get("coma_min_after").toString();
							}else{
								pwcry_afadm_coma_dura += "/0";
							}
							if(pwcry_afadm_coma_dura.equals("0/0/0")){
								pwcry_afadm_coma_dura = null;
							}
							setlinfo.setPwcry_bfadm_coma_dura(pwcry_bfadm_coma_dura);
							setlinfo.setPwcry_afadm_coma_dura(pwcry_afadm_coma_dura);
							//setlinfo.setBld_cat(setlinfoMap.get("bld_cat")==null?null:setlinfoMap.get("bld_cat").toString());
							//setlinfo.setBld_amt(setlinfoMap.get("bld_amt")==null?null:setlinfoMap.get("bld_amt").toString());
							//setlinfo.setBld_unt(setlinfoMap.get("bld_unt")==null?null:setlinfoMap.get("bld_unt").toString());
							//setlinfo.setSpga_nurscare_days(setlinfoMap.get("spga_nurscare_days")==null?null:setlinfoMap.get("spga_nurscare_days").toString());
							//setlinfo.setLv1_nurscare_days(setlinfoMap.get("lv1_nurscare_days")==null?null:setlinfoMap.get("lv1_nurscare_days").toString());
							//setlinfo.setScd_nurscare_days(setlinfoMap.get("scd_nurscare_days")==null?null:setlinfoMap.get("scd_nurscare_days").toString());
							//setlinfo.setLv3_nurscare_days(setlinfoMap.get("lv3_nurscare_days")==null?null:setlinfoMap.get("lv3_nurscare_days").toString());
							setlinfo.setDscg_way(setlinfoMap.get("dscg_way")==null?null:setlinfoMap.get("dscg_way").toString());
							setlinfo.setAcp_medins_name(setlinfoMap.get("acp_medins_name")==null?null:setlinfoMap.get("acp_medins_name").toString());
							setlinfo.setAcp_optins_code(setlinfoMap.get("acp_optins_code")==null?null:setlinfoMap.get("acp_optins_code").toString());
							setlinfo.setBill_code(setlinfoMap.get("bill_code")==null?null:setlinfoMap.get("bill_code").toString());
							setlinfo.setBill_no(setlinfoMap.get("bill_no")==null?null:setlinfoMap.get("bill_no").toString());
							setlinfo.setBiz_sn(setlinfoMap.get("biz_sn")==null?null:setlinfoMap.get("biz_sn").toString());
							//setlinfo.setDays_rinp_flag_31(setlinfoMap.get("days_rinp_flag_31")==null?null:setlinfoMap.get("days_rinp_flag_31").toString());
							//setlinfo.setDays_rinp_pup_31(setlinfoMap.get("days_rinp_pup_31")==null?null:setlinfoMap.get("days_rinp_pup_31").toString());
							setlinfo.setChfpdr_name(setlinfoMap.get("chfpdr_name")==null?null:setlinfoMap.get("chfpdr_name").toString());
							setlinfo.setChfpdr_code(setlinfoMap.get("chfpdr_code")==null?null:setlinfoMap.get("chfpdr_code").toString());
							setlinfo.setSetl_begn_date(setlinfoMap.get("setl_begn_date")==null?null:setlinfoMap.get("setl_begn_date").toString());
							setlinfo.setSetl_end_date(setlinfoMap.get("setl_end_date")==null?null:setlinfoMap.get("setl_end_date").toString());
							setlinfo.setPsn_selfpay(setlinfoMap.get("psn_selfpay")==null?null:setlinfoMap.get("psn_selfpay").toString());
							setlinfo.setPsn_ownpay(setlinfoMap.get("psn_ownpay")==null?null:setlinfoMap.get("psn_ownpay").toString());
							setlinfo.setAcct_pay(setlinfoMap.get("acct_pay")==null?null:setlinfoMap.get("acct_pay").toString());
							setlinfo.setPsn_cashpay(setlinfoMap.get("psn_cashpay")==null?null:setlinfoMap.get("psn_cashpay").toString());
							setlinfo.setHi_paymtd(setlinfoMap.get("hi_paymtd")==null?null:setlinfoMap.get("hi_paymtd").toString());
							setlinfo.setHsorg(setlinfoMap.get("hsorg")==null?null:setlinfoMap.get("hsorg").toString());
							setlinfo.setHsorg_opter(setlinfoMap.get("hsorg_opter")==null?null:setlinfoMap.get("hsorg_opter").toString());
							User currUser = UserContext.getUser();
							BdOuDept dept = DataBaseHelper.queryForBean(" select * from BD_OU_DEPT where pk_dept = ?", BdOuDept.class, currUser.getPkDept());
							setlinfo.setMedins_fill_dept(dept.getNameDept());
							setlinfo.setMedins_fill_psn(user.getUserName());
							input.setSetlinfo(setlinfo);
							
							//基金支付信息（节点标识：payinfo）
							for (Map<String, Object> map : payinfoMapList) {
								Input4101Payinfo payinfo = new Input4101Payinfo();
								payinfo.setFund_pay_type(map.get("fund_pay_type").toString());
								payinfo.setFund_payamt(map.get("fund_payamt").toString());
								payinfoList.add(payinfo);
							}
							input.setPayinfo(payinfoList);
							
							//门诊慢特病诊断信息（节点标识：opspdiseinfo）
							//住院的不用管
							
							//住院诊断信息（节点标识：diseinfo）

							
							// 收费项目信息（节点标识：iteminfo）
							if(iteminfo2301MapList!=null && iteminfo2301MapList.size()>0){
								for (Map<String, Object> map : iteminfo2301MapList) {
									Input4101Iteminfo iteminfo = new Input4101Iteminfo();
									iteminfo.setMed_chrgitm(map.get("name") == null?"0":map.get("name").toString());
									iteminfo.setAmt(map.get("det_item_fee_sumamt").toString());
									iteminfo.setClaa_sumfee(map.get("det_item_fee_sumamt1").toString());
									iteminfo.setClab_amt(map.get("det_item_fee_sumamt2").toString());
									iteminfo.setFulamt_ownpay_amt(map.get("det_item_fee_sumamt3").toString());
									iteminfo.setOth_amt("0.00");
									iteminfoList.add(iteminfo);
								}
							}else{
								for (Map<String, Object> map : iteminfo5204MapList) {
									Input4101Iteminfo iteminfo = new Input4101Iteminfo();
									iteminfo.setMed_chrgitm(map.get("name") == null?"0":map.get("name").toString());
									iteminfo.setAmt(map.get("det_item_fee_sumamt").toString());
									iteminfo.setClaa_sumfee(map.get("det_item_fee_sumamt1").toString());
									iteminfo.setClab_amt(map.get("det_item_fee_sumamt2").toString());
									iteminfo.setFulamt_ownpay_amt(map.get("det_item_fee_sumamt3").toString());
									iteminfo.setOth_amt("0.00");
									iteminfoList.add(iteminfo);
								}
							}
							input.setIteminfo(iteminfoList);
							
							//手术操作信息（节点标识：oprninfo）
							//病案首页的labor_op_upload等于1的时候，上传给医保的主诊治方式不能取第一条，要取第二条
							Map<String, Object> emrHomePageMap  = insQgybPvMapper.getEmrHomePage(paramMap);
							if(emrHomePageMap.get("labor_op_upload")!=null && emrHomePageMap.get("labor_op_upload").toString().equals("1")){
								Map<String, Object> zzdMap = oprninfoMapList.get(1);
								Input4101Oprninfo zzdOprninfo = new Input4101Oprninfo();
								zzdOprninfo.setOprn_oprt_type("1");  //1	主要手术及操作	2	其他手术及操作
								zzdOprninfo.setOprn_oprt_code(zzdMap.get("oprn_oprt_code").toString());
								zzdOprninfo.setOprn_oprt_name(zzdMap.get("oprn_oprt_name").toString());
								zzdOprninfo.setOprn_oprt_date(zzdMap.get("oprn_oprt_date").toString());
								zzdOprninfo.setAnst_way(zzdMap.get("anst_way")==null?null:zzdMap.get("anst_way").toString());
								zzdOprninfo.setOper_dr_code(zzdMap.get("oper_dr_code").toString());
								zzdOprninfo.setOper_dr_name(zzdMap.get("oper_dr_name").toString());
								zzdOprninfo.setAnst_dr_code(zzdMap.get("anst_dr_code")==null?null:zzdMap.get("anst_dr_code").toString());
								zzdOprninfo.setAnst_dr_name(zzdMap.get("anst_dr_name")==null?null:zzdMap.get("anst_dr_name").toString());
								oprninfoList.add(zzdOprninfo);
								for(int i=0; i<oprninfoMapList.size(); i++){
									if(i!=1){
										Map<String, Object> map = oprninfoMapList.get(i);
										Input4101Oprninfo oprninfo = new Input4101Oprninfo();
										oprninfo.setOprn_oprt_type("2");  //1	主要手术及操作	2	其他手术及操作
										oprninfo.setOprn_oprt_code(map.get("oprn_oprt_code").toString());
										oprninfo.setOprn_oprt_name(map.get("oprn_oprt_name").toString());
										oprninfo.setOprn_oprt_date(map.get("oprn_oprt_date").toString());
										oprninfo.setAnst_way(map.get("anes_type_name")==null?null:map.get("anes_type_name").toString());
										oprninfo.setOper_dr_code(map.get("oper_dr_code").toString());
										oprninfo.setOper_dr_name(map.get("oper_dr_name").toString());
										oprninfo.setAnst_dr_code(map.get("anst_dr_code")==null?null:map.get("anst_dr_code").toString());
										oprninfo.setAnst_dr_name(map.get("anst_dr_name")==null?null:map.get("anst_dr_name").toString());
										oprninfoList.add(oprninfo);
									}
								}
							}else{
								for(int i=0; i<oprninfoMapList.size(); i++){
									Map<String, Object> map = oprninfoMapList.get(i);
									Input4101Oprninfo oprninfo = new Input4101Oprninfo();
									if(i==0){
										oprninfo.setOprn_oprt_type("1");  //1	主要手术及操作	2	其他手术及操作
									}else{
										oprninfo.setOprn_oprt_type("2");  //1	主要手术及操作	2	其他手术及操作
									}
									oprninfo.setOprn_oprt_code(map.get("oprn_oprt_code").toString());
									oprninfo.setOprn_oprt_name(map.get("oprn_oprt_name").toString());
									oprninfo.setOprn_oprt_date(map.get("oprn_oprt_date").toString());
									oprninfo.setAnst_way(map.get("anes_type_name")==null?null:map.get("anes_type_name").toString());
									oprninfo.setOper_dr_code(map.get("oper_dr_code").toString());
									oprninfo.setOper_dr_name(map.get("oper_dr_name").toString());
									oprninfo.setAnst_dr_code(map.get("anst_dr_code")==null?null:map.get("anst_dr_code").toString());
									oprninfo.setAnst_dr_name(map.get("anst_dr_name")==null?null:map.get("anst_dr_name").toString());
									oprninfoList.add(oprninfo);
								}
							}
							input.setOprninfo(oprninfoList);
							
							//重症监护信息（节点标识：icuinfo）
							//先不传了，后面再补上
							
							returnMap.put("code", "0");
							returnMap.put("msg",  "");
							returnMap.put("input", input);
							returnMap.put("other", otherMap);
						/*}else{
							returnMap.put("code", signIn.getCode());
							returnMap.put("msg",  signIn.getMsg());
						}*/
					}else{
						returnMap.put("code", "-1");
						returnMap.put("msg", "没有查询到收费项目信息！");
					}
				}else{
					returnMap.put("code", "-1");
					returnMap.put("msg", "没有查询到住院诊断信息！");
				}
		}else{
			returnMap.put("code", "-1");
			returnMap.put("msg", "没有查询到结算清单信息！");
		}
		return returnMap;
	}

	/**
	 * 下载结算清单分值
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> downloadStListFz(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String beginDate = jo.getString("beginDate");
		String endDate = jo.getString("endDate");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			
			List<Output4160Data> data4160 = new ArrayList<Output4160Data>();
			String error = null;
			for(int i=1; i<100000; i+=100){
				Input4160Data data = new Input4160Data();
				data.setPoolarea_no("442000");
				data.setFixmedins_code("H44200100009");
				data.setFixmedins_name("中山市博爱医院");
				data.setSetl_end_date(beginDate+" 00:00:00");
				data.setEnd_setl_end_date(endDate +" 23:59:59");
				data.setPage_num("1");
				data.setPage_size("300");
				Input4160 input = new Input4160();
				input.setData(data);
				OutputData4160 out4160 = YbFunUtils.fun4160(input,  signIn.getSignNo());
				if(out4160.getInfcode()==null || !out4160.getInfcode().equals("0")){
					error = out4160.getErr_msg()+out4160.getMessage();
					break;
				}else{
					data4160.addAll(out4160.getOutput().getData());
					if(out4160.getOutput().getData().size()!=300){
						break;
					}
				}
			}
			
			if(error!=null){
				returnMap.put("code", "-1");
				returnMap.put("msg", error);
			}else{
				//删除查询区间内的值
				DataBaseHelper.execute("delete INS_QGYB_STLIST_FZ  where setl_end_date >=? and setl_end_date <=? ", beginDate+" 00:00:00", endDate +" 23:59:59");
				
				
				//保存数据
				for (Output4160Data outData : data4160) {
					DataBaseHelper.insertBean(YbToInsStListFz(outData));
				}
				
				returnMap.put("code", "0");
				returnMap.put("msg",  "提取结算清单分值成功！");
			}
		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg",  signIn.getMsg());
		}
		return returnMap;
	}
	
	private InsZsbaStListFz YbToInsStListFz(Output4160Data  data){
		InsZsbaStListFz fz = new InsZsbaStListFz();
		fz.setDiagCode(data.getDiag_code());
		fz.setDiagName(data.getDiag_name());
		fz.setPoolareaNo(data.getPoolarea_no());
		fz.setDiseSco(new BigDecimal(data.getDise_sco()==null?"0":data.getDise_sco()));
		fz.setActDiseSco(new BigDecimal(data.getAct_dise_sco()==null?"0":data.getAct_dise_sco()));
		fz.setActSetlSco(new BigDecimal(data.getAct_setl_sco()==null?"0":data.getAct_setl_sco()));
		fz.setTrtWay(data.getTrt_way());
		fz.setTrtWayName(data.getTrt_way_name());
		fz.setFixmedinsCode(data.getFixmedins_code());
		fz.setFixmedinsName(data.getFixmedins_name());
		fz.setPsnNo(data.getPsn_no());
		fz.setPsnName(data.getPsn_name());
		fz.setInsuAdmdvs(data.getInsu_admdvs());
		fz.setInsutype(data.getInsutype());
		fz.setActIptDays(Integer.parseInt(data.getAct_ipt_days()==null?"0":data.getAct_ipt_days()));
		fz.setFundPayamt(new BigDecimal(data.getFund_payamt()==null?"0":data.getFund_payamt()));
		fz.setPsnSelfpayAmt(new BigDecimal(data.getPsn_selfpay_amt()==null?"0":data.getPsn_selfpay_amt()));
		fz.setTotalPayamt(new BigDecimal(data.getTotal_payamt()==null?"0":data.getTotal_payamt()));
		fz.setInscpAmt(new BigDecimal(data.getInscp_amt()==null?"0":data.getInscp_amt()));
		fz.setFundPaySumamt(new BigDecimal(data.getFund_pay_sumamt()));
		try {
			fz.setAdmTime(DateUtils.parseDate(data.getAdm_time(), "yyyy-MM-dd HH:mm:ss"));
			fz.setDscgTime(DateUtils.parseDate(data.getDscg_time(), "yyyy-MM-dd HH:mm:ss"));
			fz.setSetlEndDate(DateUtils.parseDate(data.getSetl_end_date()+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fz.setNormFlag(data.getNorm_flag());
		fz.setPaybSetlAmt(new BigDecimal(data.getPayb_setl_amt()==null?"0":data.getPayb_setl_amt()));
		fz.setActSetlAmt(new BigDecimal(data.getAct_setl_amt()==null?"0":data.getAct_setl_amt()));
		fz.setDiagGrp(data.getDiag_grp());
		fz.setRemark(data.getRemark());
		return fz;
	}

	
}
