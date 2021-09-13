package com.zebone.nhis.pro.zsba.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

@Service
public class ZsbaAdtSysMzToZyHandler {
	
	@Autowired
	private ZsbaAdtSysMzToZyService zsbaAdtSysMzToZyService;
	
	public void sysMzToZyAdminFlag(QrtzJobCfg cfg){
		 String pkOrg = cfg.getJgs();
	        if (CommonUtils.isEmptyString(pkOrg))
	            throw new BusException("未获取到机构信息！");
	        
		String upToHis = ApplicationUtils.getPropertyValue("ext.system.sysMzToZy", "");
		if(!"1".equals(upToHis)) return;
		
		//切换到nhis库查询待同步的数据
		DataSourceRoute.putAppId("default");//切换到 NHIS
		List<PvIpNotice> list = zsbaAdtSysMzToZyService.getHaveAdmitList();
		List<String> listPk = new ArrayList<String>();
		List<String> listId = new ArrayList<String>();
		if(null != list && list.size() > 0){
			for (PvIpNotice pv : list) {
				listPk.add(pv.getPkInNotice());
				listId.add(pv.getPkPvOp().trim());
			}
		}
		
		//切换到his库同步接收标志
		DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
		int cnt_o = zsbaAdtSysMzToZyService.upDateConfirmFlag(listId);
		
		//切换回原库处理已同步标志
		DataSourceRoute.putAppId("default");//切换到 NHIS
		int cnt_n = zsbaAdtSysMzToZyService.updatePvIpNotice(listPk);
	}
}
