package com.zebone.nhis.task.drg.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.base.drg.vo.PvIpVo;
import com.zebone.nhis.common.module.base.bd.drg.QcEhpIndrg;
import com.zebone.nhis.common.module.sch.plan.SchPlanWeekDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.task.drg.dao.DrgMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.build.BuildSql;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * drg分组结果定时任务
 * @author ds
 *
 */
@Service
public class DrgGroupResultTaskService {
	private static Logger log = LoggerFactory.getLogger("nhis.SdDrgPlatFormLog");
	@Resource
	private	DrgMapper drgMapper;
	
	/**
	 * 定时上传DRG数据
	 * @param cfg
	 * @return
	 */
	@SuppressWarnings({ "rawtypes","null" })
	public void executeTask(QrtzJobCfg cfg){
		log.info("****************定时任务开始执行DRG结果获取*****************");
		String pkOrg = cfg.getJgs();
		if(CommonUtils.isEmptyString(pkOrg)){
			throw new BusException("未获取到机构信息！");
		}
		String [] orgArr=pkOrg.split(",");
		if(orgArr!=null&&orgArr.length>0){
			User u = new User();  
			u.setPkOrg(pkOrg);
			UserContext.setUser(u);
			Object result=ExtSystemProcessUtils.processExtMethod("DrgBusinessInfo", "GroupQueryTask", null);
			JSONObject json =JSONObject.parseObject(result.toString());
			Integer stuCode= (Integer)json.get("CODE");
			if(200==stuCode.intValue()){
				List<Map> list =new ArrayList<Map>();;
				if(null!=json.get("DATA")){
					list =JSONObject.parseArray(json.get("DATA").toString(),  Map.class);
					List<QcEhpIndrg> listGroup=new ArrayList<QcEhpIndrg>();
					QcEhpIndrg qcEhpIndrg=new QcEhpIndrg();
					for (Map map : list) {
						qcEhpIndrg=new QcEhpIndrg();
						qcEhpIndrg.setPkOrg(pkOrg);
						qcEhpIndrg.setOrgCode(String.valueOf(map.get("ORG_CODE")));
						qcEhpIndrg.setYblsh(String.valueOf(map.get("YBLSH")));
						qcEhpIndrg.setZycs((Integer)map.get("ZYCS"));
						qcEhpIndrg.setBah(String.valueOf(map.get("BAH")));
						qcEhpIndrg.setGenerateDate(DateUtils.strToDate(String.valueOf(map.get("GENERATE_DATE")),"yyyy-MM-dd HH:mm:ss"));
						qcEhpIndrg.setRemarks1(String.valueOf(map.get("REMARKS1")));
						qcEhpIndrg.setRemarks2(String.valueOf(map.get("REMARKS2")));
						qcEhpIndrg.setRemarks3(String.valueOf(map.get("REMARKS3")));
						qcEhpIndrg.setInDrg(String.valueOf(map.get("IN_DRG")));
						ApplicationUtils.setDefaultValue(qcEhpIndrg, true);
						listGroup.add(qcEhpIndrg);
					}
					if(list.size()>0){
						DataBaseHelper.batchUpdate(BuildSql.buildInsertSqlWithClass(QcEhpIndrg.class), listGroup);
					}
				}
			}
		}
	}
}
