package com.zebone.nhis.pro.zsba.mz.ins.zsba.service.qgyb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.dao.ZsbaQGMapper;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.HisItem;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.HisItemInfo;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybDict;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybItemMap;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.MedicalCharges;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.PagingVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import net.sf.json.JSONObject;

@SuppressWarnings({ "unchecked", "unused" })
@Service
public class ZsbaQgBaseService {
	
	private Logger logger = LoggerFactory.getLogger("nhis.ZsbaQGLog");
	
	@Value("#{applicationProperties['qgyb.url']}")
	private String HSA_URL;
	@Value("#{applicationProperties['qgyb.accountCode']}")
	private String HSA_ACCOUNT_CODE;
	@Value("#{applicationProperties['qgyb.paasid']}")
	private String HSA_PAASID;
	@Value("#{applicationProperties['qgyb.secretKey']}")
	private String SECRETKEY;
	@Value("#{applicationProperties['qgyb.fixmedins_name']}")
	private String FIXMEDINS_NAME;
	@Value("#{applicationProperties['qgyb.fixmedins_code']}")
	private String FIXMEDINS_CODE;
	@Value("#{applicationProperties['qgyb.version']}")
	private String VERSION;
	@Value("#{applicationProperties['qgyb.uploadFile.path']}")
	private String UploadFilePath;
	
	@Resource private ZsbaQGMapper zsbaQGMapper;
	

	/**
	 * 查询HIS药品字典对照信息
	 * 交易号：015001013014->022003027028
	 * @param param
	 */
	public PagingVo getHisPd(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// 分页操作
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		List<MedicalCharges> itemList = zsbaQGMapper.qryHisPdIns(paraMap);
		pagingVo.setItemList(zsbaQGMapper.qryHisPdIns(paraMap));
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

	/**
	 * 查询HIS收费项目字典对照信息
	 * 交易号：015001013015->022003027029
	 * @param param
	 */
	public PagingVo getHisItem(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// 分页操作
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		pagingVo.setItemList(zsbaQGMapper.qryHisItemIns(paraMap));
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

    /**
     * 查询医保目录信息
	 * 交易号：015001013016->022003027030
     * @param param
     */
	public PagingVo getCatalog(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// 分页操作
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		pagingVo.setInsur(zsbaQGMapper.qryInsQgybItem(paraMap));
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

	/**
	 * 获取对照信息
	 * 交易号：015001013017->022003027031
	 * @param param
	 */
	public PagingVo getcontrast(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		PagingVo pagingVo = new PagingVo();
		// 分页操作
		int pageIndex = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageIndex"));
		int pageSize = CommonUtils.getInteger(MapUtils.getString(paraMap,"pageSize"));
		MyBatisPage.startPage(pageIndex,pageSize);
		pagingVo.setItemList(zsbaQGMapper.qryInsQgybItemmap(paraMap));
		Page<List<MedicalCharges>> page = MyBatisPage.getPage();
		pagingVo.setTotalCount(page.getTotalCount());
		return pagingVo;
	}

	/**
	 * 保存对照信息
	 * 交易号：015001013018->022003027032
	 * @param param
	 */
	public void saveContrast(String param, IUser user){
		List<InsQgybItemMap> insItemlist = JsonUtil.readValue(param,new TypeReference<List<InsQgybItemMap>>(){});
		for(InsQgybItemMap insItemmap:insItemlist){
			insItemmap.setPkItemmap(NHISUUID.getKeyId());
			//目录类别(list_type):101:西药;102:中成药;103:中药饮片;104:自制剂;201:服务项目;301:医用材料;105:民族药
			if("Medical".equals(insItemmap.getHisType())){//物品标志1：物品；0：药品
				insItemmap.setFlagPd("0");
				switch (insItemmap.getListType()) {
					case "2":
						insItemmap.setListType("102");
						break;
					case "3":
						insItemmap.setListType("103");
						break;
					default:
						insItemmap.setListType("101");
						break;
				}
			}else{
				insItemmap.setFlagPd("1");
				if("07".equals(insItemmap.getListType().substring(1,2))){
					insItemmap.setListType("301");
				}else{
					insItemmap.setListType("201");
				}
			}
			insItemmap.setState("0");
			ApplicationUtils.setDefaultValue(insItemmap, true);
			DataBaseHelper.insertBean(insItemmap);
		}
	}

	/**
	 * 删除对照信息
	 * 交易号：015001013019->022003027033
	 * @param param
	 * @param user
	 */
	public void deleteContrast(String param, IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		if(StringUtils.isEmpty(MapUtils.getString(paraMap,"pkItemmap"))){
			throw new BusException("对照主键不能为空");
		}
		zsbaQGMapper.delInsQgybItemmap(paraMap);
	}

	/**
	 * 上传全国医保目录对照
	 *  交易号：015001013020->022003027034
	 * @param param
	 * @param user
	 */
	public void uploadMatchCatalog(String param, IUser user){
		HisItemInfo hisItemInfo = JsonUtil.readValue(param, HisItemInfo.class);
		getHealthInsuranceEntry((Map<String, Object>) ApplicationUtils.beanToMap(hisItemInfo),user);
		for(String  PkItemmap :hisItemInfo.getPkItemmapList()){
			DataBaseHelper.update("UPDATE INS_QGYB_ITEMMAP SET STATE = '1', UPLOAD_DATE = ? WHERE PK_ITEMMAP = ?", new Object[]{new Date(),PkItemmap});
		}
	}

	/**
	 * 撤销医保目录对照
	 * 交易号：015001013021->022003027035
	 * @param param
	 * @param user
	 */
	public void CancelMatchCatalog(String param, IUser user){
		HisItemInfo hisItemInfo = JsonUtil.readValue(param, HisItemInfo.class);
		for(HisItem hisItem:hisItemInfo.getParams()){
			hisItem.setFixmedinsCode(FIXMEDINS_CODE);
		}
		getHealthInsuranceEntry((Map<String, Object>) ApplicationUtils.beanToMap(hisItemInfo),user);
		for(String  PkItemmap :hisItemInfo.getPkItemmapList()){
			DataBaseHelper.update("UPDATE INS_QGYB_ITEMMAP SET STATE = '3' WHERE PK_ITEMMAP = ?",new Object[]{PkItemmap});
		}
	}
	
	
	/**
	 * 医疗目录与医保目录匹配信息查询
	 * 交易号：015001013022->022003027036
	 * @param user
	 * @throws ParseException 
	 */
	public void ybMatchCatalog(String param, IUser user){
		HisItemInfo hisItemInfo = JsonUtil.readValue(param, HisItemInfo.class);
		if(hisItemInfo.getPkItemmapList().size()>0){
			for(String pkItemMap:hisItemInfo.getPkItemmapList()){
				Map<String, Object> paraMap = zsbaQGMapper.qryItemMap(pkItemMap);
				if(MapUtils.isNotEmpty(paraMap)){
					paraMap.put("port", "1317");
					paraMap.put("page_num", String.valueOf(hisItemInfo.getPageIndex()));
					paraMap.put("page_size", String.valueOf(hisItemInfo.getPageSize()));
					paraMap.put("fixmedins_code", FIXMEDINS_CODE);
					String result = getHealthInsuranceEntry(paraMap, user);
					if(StringUtils.isNotEmpty(result)){
						com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
						List<Map<String, Object>> dataList = JsonUtil.readValue(jsonObject.getJSONObject("output").getString("data"), new TypeReference<List<Map<String,Object>>>() {});
						if(dataList.size()>0){
							if(("1").equals(MapUtils.getString(dataList.get(0),"vali_flag")) && MapUtils.getString(paraMap,"HIS_ITEM_CODE").equals(MapUtils.getString(dataList.get(0),"medins_list_codg")) &&  MapUtils.getString(paraMap,"HILIST_CODE").equals(MapUtils.getString(dataList.get(0),"med_list_codg"))){
								DataBaseHelper.update("UPDATE INS_QGYB_ITEMMAP SET STATE = '2' WHERE PK_ITEMMAP = ?",new Object[]{pkItemMap});
							}else{
								DataBaseHelper.update("UPDATE INS_QGYB_ITEMMAP SET STATE = '9' WHERE PK_ITEMMAP = ?",new Object[]{pkItemMap});
							}
						}
					}
				}
			}
		}
	}

    /**
     * 全国医保费用结算总账对账查询
     * 015001013026->022003027064
     * @param param
     * @param user
     */
    public List<Map<String, Object>> getCheckInsuranceSum(String param, IUser user){
        Map<String, Object> mapParam = JsonUtil.readValue(param,Map.class);
        List<Map<String, Object>> insuraList = zsbaQGMapper.qryQGYBMedicalInsuranceSum(mapParam);
        if(insuraList.size() >0){
			for(Map<String, Object> map:insuraList){
				Map<String, Object> insurMap = new HashMap<>();
				insurMap.put("port","3201");//医药机构费用结算对总账接口编码
				insurMap.put("insutype",MapUtils.getString(map,"insutype"));//险种
				insurMap.put("clr_type",MapUtils.getString(map,"clrType"));//清算类别
				insurMap.put("setl_optins","442000");//结算经办机构
				insurMap.put("stmt_begndate",MapUtils.getString(mapParam,"deBegn"));//对账开始日期
				insurMap.put("stmt_enddate",MapUtils.getString(mapParam,"deEnd"));//对账结束日期
				insurMap.put("medfee_sumamt",MapUtils.getString(map,"amountSt"));//医疗费总额
				insurMap.put("fund_pay_sumamt",MapUtils.getString(map,"amountInsu"));//基金支付总额
				insurMap.put("acct_pay",MapUtils.getString(map,"amountPi"));//个人账户支付金额
				insurMap.put("fixmedins_setl_cnt",MapUtils.getString(map,"dataNum"));//定点医药机构结算笔数
				String respon = getHealthInsuranceEntry(insurMap, user);
				if(StringUtils.isNotEmpty(respon)){
					com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respon);
					if ("0".equals(jsonObject.getString("infcode"))) {
						Map<String, Object> reqsMap = JsonUtil.readValue(jsonObject.getJSONObject("output").getString("stmtinfo"), new TypeReference<Map<String,Object>>() {});
                        map.put("stmtRsltDscr",MapUtils.getString(reqsMap,"stmt_rslt_dscr"));
					}else{
						map.put("stmtRsltDscr","医保反馈：" + jsonObject.getString("err_msg"));
					}
				}
			}
		}
        return insuraList;
    }

	/**
	 * 医保费用明细上传对账
	 * 015001013027->022003027065
	 * @param param
	 * @param user
	 */
	public void uploadCostDetailed(String param, IUser user){
		Map<String, Object> mapParam = JsonUtil.readValue(param,Map.class);
		//根据客户端传递开始时间、结束时间、险种查询相关费用明细
		List<Map<String, Object>> insuraDetailedList = zsbaQGMapper.qryQGYBMedicalInsuranceDetailed(mapParam);
		//费用明细上次文件号
		String uploadfileQuryNo;
		//调用全国医保明细上传方法
		String respUpl = uploadDetailedFile(insuraDetailedList,user);
		if(StringUtils.isNotEmpty(respUpl)){
			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respUpl);
			if ("0".equals(jsonObject.getString("infcode"))) {
				Map<String, Object> reqsMap = JsonUtil.readValue(jsonObject.getString("output"), new TypeReference<Map<String,Object>>() {});
				uploadfileQuryNo = MapUtils.getString(reqsMap,"file_qury_no");
			}else{
				throw new BusException("医保反馈：" + jsonObject.getString("err_msg"));
			}
		}else{
			throw new BusException("全国医保结算费用明细上传响应为空!!!");
		}

        if(StringUtils.isNotEmpty(uploadfileQuryNo)){
        	////根据客户端传递开始时间、结束时间、险种查询相关总费用
			List<Map<String, Object>> insuraSumList = zsbaQGMapper.qryQGYBMedicalInsuranceSum(mapParam);
			if(insuraSumList.size()>0){
				Map<String, Object> insurMap = new HashMap<>();
				insurMap.put("port","3202");//医药机构费用结算对总账接口编码
				insurMap.put("setl_optins","442000");//结算经办机构
				insurMap.put("file_qury_no",uploadfileQuryNo);//文件查询号
				insurMap.put("stmt_begndate",MapUtils.getString(mapParam,"deBegn"));//对账开始日期
				insurMap.put("stmt_enddate",MapUtils.getString(mapParam,"deEnd"));//对账结束日期
				insurMap.put("medfee_sumamt",MapUtils.getString(insuraSumList.get(0),"amountSt"));//医疗费总额
				insurMap.put("fund_pay_sumamt",MapUtils.getString(insuraSumList.get(0),"amountInsu"));//基金支付总额
				insurMap.put("cash_payamt",MapUtils.getString(insuraSumList.get(0),"amountPi"));//现金支付金额
				insurMap.put("fixmedins_setl_cnt",MapUtils.getString(insuraSumList.get(0),"dataNum"));//定点医药机构结算笔数
				String respon = getHealthInsuranceEntry(insurMap, user);
				if(StringUtils.isNotEmpty(respon)){
					com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respon);
					if ("0".equals(jsonObject.getString("infcode"))) {
						Map<String, Object> reqsMap = JsonUtil.readValue(jsonObject.getJSONObject("output").getString("fileinfo"), new TypeReference<Map<String,Object>>() {});
					}else{
					}
				}
			}else{
				throw new BusException("未查询到该险种总费用信息");
			}

		}else{
			throw new BusException("未获取的上传明细后反馈的文件号");
		}
	}

	/**
	 * 全国医保费用结算明细组装并上传
	 * @param paramList
	 * @param user
	 * @return
	 */
	public String uploadDetailedFile(List<Map<String, Object>> paramList,IUser user){
		if(paramList.size()<=0){
			return null;
		}
		String respon;
		String fileName = DateUtils.formatDate(new Date(),"yyyyMMddHHmmss");
		//在当前路径下配置文件夹
		File file = new File(UploadFilePath+fileName+".zip");
		// 先得到文件的上级目录，判断并创建文件
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdir();
		}
		try {
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

			Map<String, Object> insurMap = new HashMap<>();
			insurMap.put("port","9101");//文件上传
			//insurMap.put("in",file);
			insurMap.put("in", FileUtils.readFileToByteArray(file));
			insurMap.put("filename",file.getName());
			insurMap.put("fixmedins_code",FIXMEDINS_CODE);
			respon = getHealthInsuranceEntry(insurMap, user);
		} catch (IOException e) {
			throw new BusException(e.getMessage());
		}
		return respon;
	}



	/**
	 * 预留接口，字典下载
	 * @param param
	 * @param user
	 */
	public void downLoadInsDict(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		Map<String, Object> retMap = new HashMap<>();
		paramMap.put("port", "1901");
		paramMap.put("type", "");
		String ret = getHealthInsuranceEntry(paramMap, user);
		retMap = JsonUtil.readValue(ret, Map.class);

		if ("0".equals(CommonUtils.getString(retMap.get("infcode")))) {
			retMap = (Map<String, Object>) retMap.get("output");
			List<InsQgybDict> dictList = JsonUtil.readValue(JsonUtil.writeValueAsString(retMap.get("list")),new TypeReference<List<InsQgybDict>>() {});
			for (InsQgybDict dict : dictList) {
				ApplicationUtils.setDefaultValue(dict, true);
				dict.setCodeType(dict.getCodeType().toLowerCase());
				dict.setCreator("yl20210106");
				dict.setEuHpdicttype("1");
				dict.setFlagDef("0");
				dict.setStopFlag("0");
				dict.setFlagChd("1");

			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsQgybDict.class), dictList);
		}
	}
	

	/**
	 * 请求数据处理，发起请求，返回结果
	 * @param paramMap
	 * @param user
	 * @return
	 */
	public String getHealthInsuranceEntry(Map<String, Object> paramMap, IUser user) {
		String port = paramMap.get("port").toString();
		Map<String, String> map = this.getHeaderElement();
		String jsonStr = "";

		if(paramMap.containsKey("pk_pi")){
			paramMap.remove("pk_pi");
		}
		if(paramMap.containsKey("pk_pv")){
			paramMap.remove("pk_pv");
		}
		paramMap.remove("port");
		
		switch (port) {
			/* 获取人员信息 */
			case "1101":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 获取字典 */
			case "1901":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 签到 */
			case "9001":
				jsonStr = getJsonStr(paramMap, "signIn", port, user);
				break;
			/* 签退 */
			case "9002":
				jsonStr = getJsonStr(paramMap, "signOut", port, user);
				break;
			/* 门诊挂号 */
			case "2201":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 门诊挂号撤销 */
			case "2202":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 门诊就诊信息上传 */
			case "2203A":
				JSONObject jsonObject = JSONObject.fromObject(paramMap);
				jsonStr = getJsonStr(jsonObject, port, user);
				break;
			/* 门诊费用明细信息上传 */
			case "2204":
				jsonStr = getJsonStr(paramMap.get("params"), "feedetail", port, user);
				break;
			/* 门诊费用明细信息撤销 */
			case "2205":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 门诊预结算 */
			case "2206":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 门诊结算 */
			case "2207":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 门诊结算撤销 */
			case "2208":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 待遇检查 */
			case "2001":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 人员累计信息查询 */
			case "5206":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 科室信息上传 */
			case "3401":
				jsonStr = getJsonStr(paramMap, "deptinfo", port, user);
				break;
			/* 科室信息变更 */
			case "3402":
				jsonStr = getJsonStr(paramMap, "deptinfo", port, user);
				break;
			/* 科室信息撤销 */
			case "3403":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 核对总账 */
			case "3201":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 医药机构费用结算对明细账 */
			case "3202":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 目录对照上传 */
			case "3301":
				jsonStr = getJsonStr(paramMap.get("params"), "data", port, user);
				break;
			/* 目录对照撤销 */
			case "3302":
				jsonStr = getJsonStr(paramMap, "catalogcompin", port, user);
				break;
			/* 冲正交易 */
			case "2601":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 医疗目录与医保目录匹配信息查询 */
			case "1317":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			case "9101":
				jsonStr = getJsonStr(paramMap, "fsUploadIn", port, user);
				break;
			/* 慢特病备案 */	
			case "2503":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;	
			/* 慢特病备案撤销 */	
			case "2504":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 慢特病备案查询 */
			case "5301":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			/* 人员定点信息查询 */
			case "5302":
				jsonStr = getJsonStr(paramMap, "data", port, user);
				break;
			default:
				break;
		}
		String rs = "";
		try {
			logger.info(port + "入参参数：" + jsonStr);
			rs = HttpClientUtil.sendHttpPost(HSA_URL + port, jsonStr, map);
			logger.info(port + "出参参数：" + rs);
			JSONObject obj = JSONObject.fromObject(rs);
			obj.put("msgid", JSONObject.fromObject(jsonStr).getString("msgid"));
			rs = obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("医保接口调用失败，接口port " + port);
			throw new BusException("医保返回失败" + port + e.getMessage());
		}
		return rs;
	}
	/*
	 * 获取请求头数据
	 * @return
	 */
	private Map<String, String> getHeaderElement() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("x-tif-paasid", HSA_PAASID);
		map.put("x-tif-nonce", RandomStringUtils.random(6, true, true));
		map.put("x-tif-timestamp", Long.toString(ZsbaQGUtils.getCurrentUnixSeconds()));
		map.put("x-tif-signature", ZsbaQGUtils.getSHA256Str(map.get("x-tif-timestamp") + SECRETKEY + map.get("x-tif-nonce") + map.get("x-tif-timestamp")));
		return map;
	}
	

	/*
	 * 将Map转json格式字符串
	 * @param paramMap
	 * @param key
	 * @param interfaceNO
	 * @param user
	 * @return
	 */
	private String getJsonStr(Map<String, Object> paramMap, String key, String interfaceNO, IUser user) {
		User userInfo = (User) user;

		JSONObject jsonObject = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONObject params = new JSONObject();

		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			params.put(entry.getKey(), entry.getValue());
		}

		obj.put(key, params);
		jsonObject.put("recer_sys_code", "01");
		jsonObject.put("msgid", FIXMEDINS_CODE + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + getSNcode());
		jsonObject.put("dev_no", "");
		jsonObject.put("dev_safe_info", "");
		jsonObject.put("signtype", "SM3");
		jsonObject.put("cainfo", "");
		jsonObject.put("inf_time", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()) );
		jsonObject.put("input", obj);
		/* 经办人类别 1-经办人；2-自助终端；3-移动终端 */
		jsonObject.put("opter_type", userInfo.getCodeEmp().startsWith("999")?"2":"1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", "442000");//参保地医保区划
		jsonObject.put("mdtrtarea_admvs", "442000");//就医地医保区划
		jsonObject.put("recer_admvs", "442000");/* 接收方医保区划代码 */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}

	/*
	 * 将JSONObject转json格式字符串
	 * @param paramObj
	 * @param interfaceNO
	 * @param user
	 * @return
	 */
	private String getJsonStr(JSONObject paramObj, String interfaceNO, IUser user) {
		User userInfo = (User) user;

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("recer_sys_code", "01");
		jsonObject.put("msgid", FIXMEDINS_CODE + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + getSNcode());
		jsonObject.put("dev_no", "");
		jsonObject.put("dev_safe_info", "");
		jsonObject.put("signtype", "SM3");
		jsonObject.put("cainfo", "");
		jsonObject.put("inf_time", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()) );
		jsonObject.put("input", paramObj);
		/* 经办人类别 1-经办人；2-自助终端；3-移动终端 */
		jsonObject.put("opter_type", userInfo.getCodeEmp().startsWith("999")?"2":"1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", "442000");
		jsonObject.put("mdtrtarea_admvs", "442000");
		jsonObject.put("recer_admvs", "442000");/* 接收方医保区划代码 */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}

	/*
	 * 将Object转json格式字符串
	 * @param param
	 * @param key
	 * @param interfaceNO
	 * @param user
	 * @return
	 */
	private String getJsonStr(Object param, String key, String interfaceNO, IUser user) {
		User userInfo = (User) user;
		
		JSONObject jsonObject = new JSONObject();
		Map<String,Object> objMap = new HashMap<>(16);
		objMap.put(key,param);

		jsonObject.put("recer_sys_code", "01");
		jsonObject.put("msgid", FIXMEDINS_CODE + DateUtils.dateToStr("yyyyMMddHHmmss", new Date()) + getSNcode());
		jsonObject.put("dev_no", "");
		jsonObject.put("dev_safe_info", "");
		jsonObject.put("signtype", "SM3");
		jsonObject.put("cainfo", "");
		jsonObject.put("inf_time", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date()) );
		jsonObject.put("input", objMap);
		/* 经办人类别 1-经办人；2-自助终端；3-移动终端 */
		jsonObject.put("opter_type", userInfo.getCodeEmp().startsWith("999")?"2":"1");
		jsonObject.put("opter_name", userInfo.getNameEmp());
		jsonObject.put("opter", userInfo.getCodeEmp());
		jsonObject.put("insuplc_admdvs", "442000");
		jsonObject.put("mdtrtarea_admvs", "442000");
		jsonObject.put("recer_admvs", "442000");/* 接收方医保区划代码 */
		jsonObject.put("infver", VERSION);
		jsonObject.put("infno", interfaceNO);
		jsonObject.put("sign_no", "");
		jsonObject.put("fixmedins_name", FIXMEDINS_NAME);
		jsonObject.put("fixmedins_code", FIXMEDINS_CODE);

		return jsonObject.toString();
	}
	
	/*
	 * 截取0607医保交互顺序号后四位
	 * @return
	 */
	private String getSNcode() {
		String SNcode = ApplicationUtils.getCode("0607");
		if (SNcode != null && SNcode.length() >= 4) {
			SNcode = SNcode.substring(SNcode.length() - 4, SNcode.length());
			return SNcode;
		} else {
			throw new BusException("根据编码规则【0607】未获取的有效的医保顺序号SNcode");
		}
	}
	
}
