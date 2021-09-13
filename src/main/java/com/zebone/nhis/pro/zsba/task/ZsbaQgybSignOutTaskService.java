package com.zebone.nhis.pro.zsba.task;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.google.common.collect.Maps;
import com.zebone.nhis.pro.zsba.compay.ins.pub.service.InsPubSignInService;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * 全国医保自动签退任务
 * @author Administrator
 *
 */
@Service
public class ZsbaQgybSignOutTaskService {
	private final static Logger logger = LoggerFactory.getLogger("nhis.quartz");
	
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	@Autowired
	private InsPubSignInService insPubSignInService;
	
	/**
	 * 自动签退任务
	 * @param cfg
	 */
	public void execIpAotuSignOut(QrtzJobCfg cfg) {
/*		Map<String,Object> rs = Maps.newHashMap();
		try {*/
			insPubSignInService.saveSignOut();
/*		} catch (Exception e) {
			e.printStackTrace();
			rs.put("tips", String.format("执行%s日结异常,日结人员：%s,异常信息：%s;", e.getMessage(), UserContext.getUser().getNameEmp(),e.getMessage()));
		}finally{
			return rs;
		}*/
	}
}
