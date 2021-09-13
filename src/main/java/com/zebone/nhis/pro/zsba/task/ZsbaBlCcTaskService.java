package com.zebone.nhis.pro.zsba.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.dao.ZsBlIpCcMapper;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.service.ZsIpBlCcService;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlCcDt;
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
public class ZsbaBlCcTaskService {

	private final static Logger logger = LoggerFactory.getLogger("nhis.quartz");
	
	/** 取拓展属性【0702】住院自动日结账人员属性值为1的人员，执行自动日结逻辑；*/
	private final String IP_CC_ATTR_CODE = "0702";
	
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	@Resource
	ZsBlIpCcMapper zsBlIpCcMapper;
	
	@Autowired
	ZsIpBlCcService zsIpBlCcService;
	
	//没有对获取到的操作员分页处理，假设没有很多操作员、、;也没有并行处理，假设此任务每天执行一次，不会太久..
	
	/**
	 * 住院日结任务
	 * @param cfg
	 */
	public Map<String,Object> execIpBlCc(QrtzJobCfg cfg) {
		return executeProxy(IP_CC_ATTR_CODE, cfg, new ZsbaBlCcTaskExecutor() {
			@Override
			public void closeCounter(String param, IUser user) {
				ZsBlCcDt dt = zsIpBlCcService.CloseCounter(param, user);
				if(dt != null && dt.getBlCc() !=null && dt.getBlCc().getPkCc() != null) {
					//将创建人打个标记
					DataBaseHelper.update("update bl_cc set creator = 'sys' where pk_cc = ?", new Object[]{dt.getBlCc().getPkCc()});
				}
			}
		});
	}
	
	/**
	 * 查询需要自动日结的操作员列表
	 * @param codeAttr
	 * @param pkOrg
	 * @return
	 */
	private List<BdOuEmployee> getPkEmpOpera(String codeAttr,String pkOrg){
		Map<String,Object> param = new HashMap<>();
		param.put("codeAttr", codeAttr);
		param.put("pkOrg", pkOrg);
		return zsBlIpCcMapper.queryEmployees(param);
	}
	
	/**
	 * 查询需要自动日结的操作员列表(不根据扩展属性，收款表、结算表有记录就日结)
	 * @param codeAttr
	 * @param pkOrg
	 * @return
	 */
	private List<BdOuEmployee> getPkEmpOperaRj(String pkOrg){
		Map<String,Object> param = new HashMap<>();
		//获取昨天的日期
		Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		param.put("date", date);
		param.put("pkOrg", pkOrg);
		return zsBlIpCcMapper.queryEmployeesRj(param);
	}
	
	private IUser inittUser(BdOuEmployee employee){
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
	
	private Map<String,Object> executeProxy(String attrCode,QrtzJobCfg cfg, ZsbaBlCcTaskExecutor executor){
		Map<String,Object> rs = Maps.newHashMap();
		String msg = IP_CC_ATTR_CODE.equals(attrCode)?"住院":"门诊";
		logger.info("执行{}日结任务开始...",msg);
		//List<BdOuEmployee> listEmp = getPkEmpOpera(attrCode, cfg.getJgs());
		List<BdOuEmployee> listEmp = getPkEmpOperaRj(cfg.getJgs());
		logger.info("查到符合条件的操作员：" + JsonUtil.writeValueAsString(listEmp));
		StringBuilder sblError = new StringBuilder();
		//Date dateEnd = new Date();
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		//获取前几天的都可以，对-1进行改变即可
		//获取后一天的时间也可以，把-1改为1即可
		//后几天和前几天同理
		calendar.add(Calendar.DAY_OF_MONTH,-1);
		Date dateEnd = calendar.getTime();
		if(StringUtils.isNotBlank(cfg.getJobparam()) && NumberUtils.isNumber(cfg.getJobparam())) {
			dateEnd = DateUtils.addDays(dateEnd, NumberUtils.toInt(cfg.getJobparam()));
		}
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		for(int i=0;i<listEmp.size();i++) {
			TransactionStatus status = platformTransactionManager.getTransaction(def);
			try {
				Map<String,Object> param = Maps.newHashMap();
				param.put("dateEnd", dateEnd);
				executor.closeCounter(JsonUtil.writeValueAsString(param), inittUser(listEmp.get(i)));
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
	
}
