package com.zebone.nhis.pro.zsba.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.common.support.DateUtil;
import com.zebone.nhis.pro.zsba.common.support.HttpUtil;
import com.zebone.nhis.pro.zsba.common.utils.PayConfig;
import com.zebone.nhis.pro.zsba.nm.dao.NmCiStDetailsMapper;
import com.zebone.nhis.pro.zsba.nm.dao.NmCiStMapper;
import com.zebone.nhis.pro.zsba.nm.vo.NmChargeItem;
import com.zebone.nhis.pro.zsba.nm.vo.NmCiSt;
import com.zebone.nhis.pro.zsba.nm.vo.NmCiStDetails;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * 非医疗收费
 * 1.自动生成上一日的生活用品费用；
 * 2.自动从护理记录单中获取奶粉用量，并关联收费项目，生成计费记录；
 * 3.每日凌晨1点执行
 * @author lipz
 *
 */
@Service
public class ZsbaNmTaskService {
	
	private final static Logger logger = LoggerFactory.getLogger(ZsbaNmTaskService.class);

	@Autowired private NmCiStMapper stMapper;
	@Autowired private NmCiStDetailsMapper stDetailsMapper;
	
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;

	/**
	 * 生成计费数据
	 * @param cfg
	 */
	public void executeJf(QrtzJobCfg cfg){ 
		// 获取自动计费的科室
		String deptSql = "SELECT CONVERT(VARCHAR(2000), STUFF((SELECT ',' + pk_dept from nm_tast_dept  FOR XML PATH('')), 1, 1, '')) as pk_dept";
		Map<String, Object> deptMap = DataBaseHelper.queryForMap(deptSql, new Object[]{});
		logger.info("获取【自动计费科室】数量：{}", deptMap.size());
		
		if(deptMap!=null && deptMap.size()>0){
			String deptPks = deptMap.get("pkDept").toString();
			deptPks = "'" + deptPks.replaceAll(",", "','") + "'";
			
			String patSql = "select * from view_emr_nr_pat_list where pk_dept in ("+deptPks+")";
			List<Map<String, Object>> patList = DataBaseHelper.queryForList(patSql, new Object[]{});
			logger.info("获取【在院患者信息】数量：{}", patList.size());
			
			if(!patList.isEmpty()){

				// 生活用品
				createShyp(patList);
				
				// 奶粉费
				createNff(patList);
				
			}
		}
	}
	
	/**
	 * 推送付款通知
	 * @param cfg
	 */
	public void executePush(QrtzJobCfg cfg){ 
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		try {
			StringBuffer stSql = new StringBuffer();
		    stSql.append(" SELECT pk_org, pk_dept, input_dept, pv_type, pk_pv, name_pi, code_pv, times, sum(total) amount ");
		    stSql.append(" FROM nm_ci_st_details ");
		    stSql.append(" WHERE is_sett='0' and pv_type='2' and date_annal=? ");
		    stSql.append(" GROUP BY pk_org, pk_dept, input_dept, pv_type, pk_pv, name_pi, code_pv, times ");
		    List<Map<String, Object>> stList = DataBaseHelper.queryForList(stSql.toString(), new Object[] { DateUtils.getDate() });
		    logger.info("非医疗费用[任务调度-生成结算]待生成总数：" + stList.size());

		    int upNum = 0;
		    String updateStdSql = "update nm_ci_st_details set is_sett='1', pk_ci_st=? where is_sett='0' and pv_type='2' and date_annal=? and pk_pv=?";
		    for(Map<String, Object> stMap : stList){
		        NmCiSt sett = new NmCiSt();
		        sett.setPkCiSt(NHISUUID.getKeyId());
		        sett.setPkOrg(stMap.get("pkOrg").toString());
		        sett.setPkDept(stMap.get("pkDept").toString());
		        sett.setInputDept(stMap.get("inputDept").toString());
		        sett.setPkPv(stMap.get("pkPv").toString());
		        sett.setPvType(stMap.get("pvType").toString());
		        sett.setNamePi(stMap.get("namePi").toString());
		        sett.setCodePv(stMap.get("codePv").toString());
		        sett.setTimes(Integer.valueOf(Integer.parseInt(stMap.get("times").toString())));
		        sett.setAmount(new BigDecimal(stMap.get("amount").toString()));
		        sett.setIsPush("0");
		        sett.setIsPay("0");
		        sett.setSettCode("00000");
		        sett.setSettName("自动计费");
		        sett.setCreator(sett.getSettCode());
		        sett.setCreateTime(new Date());
		        sett.setModifier(sett.getCreator());
		        sett.setModityTime(sett.getCreateTime());
		        sett.setDelFlag("0");
		        sett.setTs(sett.getCreateTime());
		        this.stMapper.saveCiSt(sett);

		        upNum += DataBaseHelper.execute(updateStdSql, new Object[] { sett.getPkCiSt(), DateUtils.getDate(), stMap.get("pkPv").toString() });
		    }
		    logger.info("非医疗费用[任务调度-生成结算]更新结算标记数：" + upNum);
			
			platformTransactionManager.commit(status);
		} catch (Exception e) {
		    logger.error("非医疗费用[任务调度-生成结算]处理异常：" + e.getMessage());
			platformTransactionManager.rollback(status);
		}finally{
			
		}
		
		
		DefaultTransactionDefinition def1 = new DefaultTransactionDefinition();
		def1.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status1 = platformTransactionManager.getTransaction(def1);
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT pi.code_op,st.name_pi, st.code_pv, st.times,st.amount,st.pk_ci_st,pwm.WX_OPEN_ID ");
			sql.append(" FROM nm_ci_st st ");
			sql.append(" LEFT JOIN PI_MASTER pi ON pi.CODE_IP=st.code_pv ");
			sql.append(" LEFT JOIN t_patient_wx_mapper pwm ON pwm.PK_PI=pi.PK_PI  ");
			sql.append(" WHERE st.pv_type='2' AND st.is_pay='0' AND st.is_push='0' and st.create_time>'2021-03-20' ");
			List<Map<String, Object>> dataList = DataBaseHelper.queryForList(sql.toString());
			logger.info("非医疗费用[任务调度-微信通知]待推送总数：" + dataList.size());
			
			int num = 0;
			String updateSql = " update nm_ci_st set is_push='1' where pk_ci_st=? ";
			for(Map<String, Object> data : dataList){
				String title = "新住院生活用品计费";
				String content = data.get("namePi").toString()+"["+data.get("codePv").toString()+"]["+data.get("times").toString()+"],\n就诊期间的【生活用品】费用共计应缴："+data.get("amount").toString()+"元。";
				String remarks = "点击进入查看明细 和 缴纳费用。";
				String detailUriParams = "/lift/nmIndex?settId="+data.get("pkCiSt").toString();//公众号控制器的方法路径及参数
				if(data.get("codeOp")!=null) {
					if(sendPushWx(title, content, remarks, detailUriParams, data.get("codeOp").toString())){//调用推送中转接口
						// 更新推送标记
						num += DataBaseHelper.execute(updateSql, data.get("pkCiSt").toString());
					}else{
						logger.info("非医疗费用[任务调度-微信通知]返回结果:{}, pkCiSt:{}, codeOp:{}", "推送失败", data.get("pkCiSt").toString(), data.get("codeOp").toString());
					}
				}else {
					logger.info("非医疗费用[任务调度-微信通知]返回结果:{}, pkCiSt:{}, codeOp:{}", "推送失败", data.get("pkCiSt").toString(), "null");
				}
			}
		    logger.info("非医疗费用[任务调度-微信通知]推送并更新标记成功总数：" + num);
			platformTransactionManager.commit(status1);
		} catch (Exception e) {
		    logger.error("非医疗费用[任务调度-微信通知]处理异常：" + e.getMessage());
			platformTransactionManager.rollback(status1);
		}finally{
			
		}
		
	}
	
	/*
	 * 生活用品
	 * 1.获取【生活用品】收费项目信息
	 * 2.遍历关联患者基本信息
	 * 3.生成计费明细，数量为1、单价从收费项目信息中取得，小计为 数量*单价
	 * @param queryDate
	 */
	private void createShyp(List<Map<String, Object>> patList){
		logger.info("自动生成【生活用品】开始");
		
		String ciSql = "select * from nm_charge_item where spec='-' and del_flag=? and auto_annal=?";
		List<NmChargeItem> ciList = DataBaseHelper.queryForList(ciSql, NmChargeItem.class, new Object[]{"0", "0"});
		logger.info("获取【生活用品】收费项目信息：{}", ciList.size());
		if(!ciList.isEmpty()){
			try {
				String currDate = DateUtils.getDate()+" 00:00:00";
				for(NmChargeItem ci : ciList) {
					for(Map<String, Object> pat : patList){
						// 当天之前入院的才生成
						String dateBegin = pat.get("dateBegin").toString().substring(0, 19);
						if(DateUtil.compare(currDate, dateBegin, "yyyy-MM-dd HH:mm:ss") > 0){
							createStd(pat, ci.getPkOrg(), ci.getPkCi(), ci.getPrice());
						}
					}
				}
			} catch (Exception e) {
				logger.error("自动生成【生活用品】费用时，发生异常：{}", e.getMessage());
			}
		}
		logger.info("自动生成【生活用品】结束");
	}
	
	/*
	 * 奶粉费
	 * 1.遍历患者获取上一天的计费数据
	 * 2.遍历计费数据获取收费项目，并遍历匹配获取单价
	 * 3.生成对应的计费数据
	 * @param queryDate
	 */
	private void createNff(List<Map<String, Object>> patList){
		logger.info("自动生成【奶粉费】开始");
		
		String dateTime = DateUtils.addDate(new Date(), -1, 3, "yyyy-MM-dd");
		try {
			// 获取在用及自动计费的收费项目
			String ciSql = "select pk_org, pk_ci, name_item, spec, price from nm_charge_item where spec<>'-' and del_flag=? and auto_annal=?";
			List<Map<String, Object>> ciList = DataBaseHelper.queryForList(ciSql, new Object[]{"0", "0"});
			for(Map<String, Object> pat : patList){
				// 取患者对应日期的奶费记录
				String intakeSql = "select * from view_emr_nr_pat_intake where pk_pv=? and date_annal>=? and date_annal<=?";
				List<Map<String, Object>> intakeList = DataBaseHelper.queryForList(intakeSql, new Object[]{pat.get("pkPv").toString(), dateTime+" 00:00:00", dateTime+" 23:59:59"});
				logger.info("自动生成患者：{},【奶粉费】数量：{}", pat.get("pkPv").toString(), intakeList.size());
				
				for(Map<String, Object> intake : intakeList){
					try {
						String ordName = intake.get("content").toString();
						Integer ordNum = Integer.parseInt(intake.get("num").toString());
						
						logger.info("护理记录名称：{}, 数量：{}", ordName, ordNum);
						
						String pkOrg = "", pkCi="";
						BigDecimal price = new BigDecimal(0);
						
						// 根据奶费名称匹配收费项目信息
						for(Map<String, Object> ci : ciList){
							String nameItem = ci.get("nameItem").toString();
							if(nameItem.equals(ordName)){
								String spec = ci.get("spec").toString();
								Integer specNum = getSpecNum(spec);
								
								/*
								 * 当计费数量大于当前项目规格数量时，要判断当前项目规格前缀是否为大于
								 */
								if(ordNum > specNum){
									if(spec.contains("＞") || spec.contains(">")){
										pkOrg = ci.get("pkOrg").toString();
										pkCi = ci.get("pkCi").toString();
										price = new BigDecimal(ci.get("price").toString());
										logger.info("配对成功，pkCi：{}, price：{}", pkCi, price);
										break;
									}
								}else{
									/*
									 * 当计费数量小于等于当前项目规格数量时，要判断当前项目规格前缀是否为小于等于
									 */
									if(spec.contains("≤") || spec.contains("<=")){
										pkOrg = ci.get("pkOrg").toString();
										pkCi = ci.get("pkCi").toString();
										price = new BigDecimal(ci.get("price").toString());
										logger.info("配对成功，pkCi：{}, price：{}", pkCi, price);
										break;
									}
								}
							}
						}
						
						/*
						 * 当获取到对应的收费项目信息后，生成计费明细
						 */
						if(StringUtils.isNotEmpty(pkOrg) && StringUtils.isNotEmpty(pkCi) && (price.compareTo(BigDecimal.ZERO) == 1)){
							createStd(pat, pkOrg, pkCi, price);
						}
					} catch (Exception e) {
						logger.error("遍历【奶粉费】费用时，发生异常，患者pkPV：{}，记录名称：{}， 记录数量：{}。异常信息：", 
								pat.get("pkPv").toString(), intake.get("content"), intake.get("num"), e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			logger.error("自动生成【奶粉费】费用时，发生异常：{}", e.getMessage());
			e.printStackTrace();
		}
		logger.info("自动生成【奶粉费】结束");
	}
	
	/*
	 * 生成患者计费数据
	 * @param pat	在院患者信息
	 * @param pkOrg	所属机构
	 * @param pkCi	收费项目主键
	 * @param price	收费项目单价
	 */
	private void createStd(Map<String, Object> pat, String pkOrg, String pkCi, BigDecimal price){
		NmCiStDetails std = new NmCiStDetails();
		std.setPkCiStd(NHISUUID.getKeyId());
		std.setPkOrg(pkOrg);
		std.setPkDept(pat.get("pkDept").toString());
		std.setInputDept(pat.get("pkDept").toString());
		std.setPkPv(pat.get("pkPv").toString());
		std.setNamePi(pat.get("namePi").toString());
		std.setPvType("2");//住院
		std.setCodePv(pat.get("codeIp").toString());
		std.setTimes(Integer.parseInt(pat.get("times").toString()));
		std.setDateAnnal(DateUtils.getDate());
		std.setNumOrd(new BigDecimal(1));
		std.setPkCi(pkCi);
		std.setCiPrice(price);
		std.setTotal(price);
		std.setIsSett("0");
		std.setIsPay("0");
		std.setAnnalCode("00000");
		std.setAnnalName("自动计费");
		std.setCreator(std.getAnnalCode());
		std.setCreateTime(new Date());
		std.setModifier(std.getAnnalCode());
		std.setModityTime(std.getCreateTime());
		std.setDelFlag("0");
		std.setTs(std.getCreateTime());
		
		stDetailsMapper.saveCiStd(std);
	}
	
	/*
	 * 取规格中的数值
	 * @param spec	项目规格
	 * @return		数值
	 */
	private int getSpecNum(String spec){
		String regEx="[^0-9]";  
		Pattern p = Pattern.compile(regEx);  
		Matcher m = p.matcher(spec); 
		return Integer.parseInt(m.replaceAll("").trim());
	}
	
	
	
	/**
	 * 推送消息
	 * @param title
	 * @param content
	 * @param remarks
	 * @param detailUriParams
	 * @param patientId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Boolean sendPushWx(String title, String content, String remarks, String detailUriParams, String patientId){
		// 调用NHIS的接口服务查询
		SortedMap<Object, Object> params = new TreeMap<Object, Object>();
		params.put("title", title);
		params.put("content", content);
		params.put("remarks", remarks);
		params.put("detailUriParams", detailUriParams);
		params.put("patientId", patientId);
		String jsonParams = JsonUtil.writeValueAsString(params);
		
		//发送http请求通过中转服务器请求微信推送接口
		String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.wx_push_pay_url;
		String result = HttpUtil.httpPost(queryUrl, jsonParams);
		logger.info("非医疗费用[任务调度-微信通知]返回结果:" + result);
		
		Boolean flag =false;
		Map<String, Object> map = JsonUtil.readValue(result, Map.class);
		if(map!=null && map.containsKey("common_return")){
			flag = (Boolean)map.get("common_return");
		}
		return flag;
	}
}
