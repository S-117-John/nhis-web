package com.zebone.nhis.task.bl.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.bl.pub.service.OpblccService;
import com.zebone.nhis.bl.pub.vo.OpBlCcVo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.task.bl.vo.OthStaMonthUnstRec;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.collect.Maps;
import com.zebone.nhis.bl.pub.service.IpBlCcService;
import com.zebone.nhis.bl.pub.vo.BlCcDt;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.task.bl.dao.BlCcTaskMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * 日结自动日结任务<br>
 * 不做业务处理，调用原始服务，这里只是跑批
 * @author Alvin
 *
 */
@Service
public class BlCcTaskService {

	private final static Logger logger = LoggerFactory.getLogger("nhis.quartz");
	
	/** 取拓展属性【0702】住院自动日结账人员属性值为1的人员，执行自动日结逻辑；*/
	private final String IP_CC_ATTR_CODE_ZY = "0702";
	private final String IP_CC_ATTR_CODE_MZ = "0701";

	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	@Resource
	BlCcTaskMapper blCcTaskMapper;
	
	@Autowired
	IpBlCcService ipBlCcService;

	@Autowired
	OpblccService opblccService;
	//没有对获取到的操作员分页处理，假设没有很多操作员、、;也没有并行处理，假设此任务每天执行一次，不会太久..

	/**
	 * 住院日结任务
	 * @param cfg
	 */
	public Map<String,Object> execIpBlCc(QrtzJobCfg cfg) {
		return executeProxy(IP_CC_ATTR_CODE_ZY, cfg,null, new BlCcTaskExecutor() {
			@Override
			public void closeCounter(String param, IUser user) {
				BlCcDt dt = ipBlCcService.CloseCounter(param, user);
				if(dt != null && dt.getBlCc() !=null && dt.getBlCc().getPkCc() != null) {
					//将创建人打个标记
					saveMark(dt.getBlCc().getPkCc());
				}
			}
		});
	}

	/**
	 * 门诊日结任务
	 * @param cfg
	 * @return
	 */
	public Map<String,Object> execOpBlCc(QrtzJobCfg cfg) {
		return execOpCc(EnumerateParameter.ZERO, cfg);
	}

	/**
	 * 挂号日结任务
	 * @param cfg
	 * @return
	 */
	public Map<String,Object> execOpRegBlCc(QrtzJobCfg cfg) {
		return execOpCc(EnumerateParameter.ONE, cfg);
	}

	/**
	 * 诊间日结任务(门诊收费日结）
	 * @param cfg
	 * @return
	 */
	public Map<String,Object> execOpChargeBlCc(QrtzJobCfg cfg) {
		return execOpCc(EnumerateParameter.TWO, cfg);
	}

	private Map<String,Object> execOpCc(String euCctype,QrtzJobCfg cfg){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("euCctype", euCctype);
		return executeProxy(IP_CC_ATTR_CODE_MZ, cfg,paramMap, new BlCcTaskExecutor() {
			@Override
			public void closeCounter(String param, IUser user) {
				OpBlCcVo dt = opblccService.saveBlCc(param, user);
				if(dt != null && dt.getBlCc() !=null && dt.getBlCc().getPkCc() != null) {
					saveMark(dt.getBlCc().getPkCc());
				}
			}
		});
	}

	private void saveMark(String pkCc){
		DataBaseHelper.update("update bl_cc set creator = 'sys' where pk_cc = ?", new Object[]{pkCc});
	}
	/**
	 * 查询需要自动日结的操作员列表
	 * @param codeAttr
	 * @param pkOrg
	 * @return
	 */
	private List<BdOuEmployee> getPkEmpOpera(String codeAttr,String pkOrg){
        
        List<String> pkOrgs=new ArrayList<>();
		if(pkOrg!=null){
			//pkOrg = pkOrg.replace(CommonUtils.getGlobalOrg(),"").replace(",","");
			pkOrg = pkOrg.replace(CommonUtils.getGlobalOrg(), "");
			if(!pkOrg.contains(",")){
                pkOrgs.add(pkOrg);
            }else {
                List<String> org_temp= Arrays.asList(pkOrg.split(","));
                if(org_temp!=null && org_temp.size()>0){
                    pkOrgs.addAll(org_temp); 
                }
            }
		} else if(CommonUtils.getGlobalOrg().equals(pkOrg)){
			throw new BusException("请将任务授权给具体机构！");
		}
		Map<String,Object> param = new HashMap<>();
		param.put("codeAttr", codeAttr);
		param.put("pkOrgs", pkOrgs);
		return blCcTaskMapper.queryEmployees(param);
	}
	
	private IUser initUser(BdOuEmployee employee){
		User user = new User();
		user.setPkEmp(employee.getPkEmp());
		user.setNameEmp(employee.getNameEmp());
		user.setCodeEmp(employee.getCodeEmp());
		user.setPkOrg(employee.getPkOrg());
		user.setPkDept(MapUtils.getString(DataBaseHelper.queryForMap("select pk_dept from BD_OU_EMPJOB where pk_emp=? and del_flag='0'",
				new Object[]{employee.getPkEmp()}), "pkDept"));
		UserContext.setUser(user);
		return user;
	}
	
	private Map<String,Object> executeProxy(String attrCode,QrtzJobCfg cfg,Map<String,Object> initParam, BlCcTaskExecutor executor){
		Map<String,Object> rs = Maps.newHashMap();
		String msg = IP_CC_ATTR_CODE_ZY.equals(attrCode)?"住院":"门诊";
		logger.info("执行{}日结任务开始...",msg);
		List<BdOuEmployee> listEmp = getPkEmpOpera(attrCode, cfg.getJgs());
		logger.info("查到符合条件的操作员：" + JsonUtil.writeValueAsString(listEmp));
		if(CollectionUtils.isEmpty(listEmp)){
			return rs;
		}
		StringBuilder sblError = new StringBuilder();
		Date dateEnd = new Date();
		if(StringUtils.isNotBlank(cfg.getJobparam())) {
			if(NumberUtils.isNumber(cfg.getJobparam())){
				dateEnd = DateUtils.addDays(dateEnd, -Math.abs(NumberUtils.toInt(cfg.getJobparam())));
			} else if(StringUtils.trim(cfg.getJobparam()).length()==8){
				Date date = setFmtTime(dateEnd,StringUtils.trim(cfg.getJobparam()));
				if(date != null && date.compareTo(dateEnd)<=0){
					dateEnd = date;
				}
			}
		}
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		for(int i=0;i<listEmp.size();i++) {
			TransactionStatus status = platformTransactionManager.getTransaction(def);
			try {
				Map<String,Object> param = Maps.newHashMap();
				if(MapUtils.isNotEmpty(initParam)){
					param.putAll(initParam);
				}
				param.put("dateEnd", dateEnd);
				executor.closeCounter(JsonUtil.writeValueAsString(param), initUser(listEmp.get(i)));
				platformTransactionManager.commit(status);
			} catch (Exception e) {
				sblError.append(String.format("执行%s日结异常,日结人员：%s,异常信息：%s;",msg, UserContext.getUser().getNameEmp(),e.getMessage()));
				logger.error("执行{}日结第{}条异常...",msg,i+1,e);
				platformTransactionManager.rollback(status);
			}
			logger.info("执行{}日结第{}条完成...",msg,i+1);
		}
		rs.put("tips", sblError.toString());
		logger.info("执行日结任务结束...",msg);
		return rs;
	}

	private Date setFmtTime(Date dateEnd,String time){
		try {
			String str = FastDateFormat.getInstance("yyyy-MM-dd").format(dateEnd) + " "+StringUtils.trim(time);
			return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(str);
		}  catch (ParseException e) {
			logger.info("自动日结任务参数配置异常,正确格式HH:mm:ss...",e);
		}
		return null;
	}

	/**
	 * 欠费患者短信平台推送定时任务
	 * zsrm-任务-4317
	 */
	public Map sendArrearsShortMessageTask(QrtzJobCfg cfg) {
		//门诊欠费患者短信推送平台接口
		PlatFormSendUtils.sendOpFeeReminderMsg(null);
		return null;
	}

	/**
	 * 向微信推送当天未缴费信息模板内容
	 */
	public void sendWeiXinForNotPayCost(QrtzJobCfg cfg) {
		Map<String, Object> map = JSONObject.parseObject(cfg.getJobparam(), Map.class);
		PlatFormSendUtils.sendWeiXinForNotPayCost(map);
	}

	/**
	 * 新增月度定时任务，记录月底未结账患者费用信息
	 */
	public void insertThStaMonthUnstRec(QrtzJobCfg cfg) {
		List<OthStaMonthUnstRec> othStaMonthUnstRecs = blCcTaskMapper.queryOthStaMonthUnstRec();
		for (OthStaMonthUnstRec othStaMonthUnstRec : othStaMonthUnstRecs) {
			ApplicationUtils.setDefaultValue(othStaMonthUnstRec, true);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(OthStaMonthUnstRec.class), othStaMonthUnstRecs);
	}
}
