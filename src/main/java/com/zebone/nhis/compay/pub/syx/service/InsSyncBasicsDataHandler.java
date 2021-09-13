package com.zebone.nhis.compay.pub.syx.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.compay.pub.syx.vo.ViewItem;
import com.zebone.nhis.compay.pub.syx.vo.ViewItemOut;
import com.zebone.nhis.compay.pub.syx.vo.ViewMatch;
import com.zebone.nhis.compay.pub.syx.vo.ViewMatchOut;
import com.zebone.nhis.compay.pub.syx.vo.ViewMedi;
import com.zebone.nhis.compay.pub.syx.vo.ViewMediOut;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsSyncBasicsDataHandler {

	@Resource
	private InsSyncBasicsDataService InsSyncBasicsData;

	/** 015001007025获取中间库诊疗项目及服务设施目录-市医保 */
	public List<ViewItem> queryViewItemList(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		String codeHp = JsonUtil.getFieldValue(param, "codeHp");
		DataSourceRoute.putAppId(datasourcename);
		List<ViewItem> list = InsSyncBasicsData.queryViewItemList(codeHp);
		DataSourceRoute.putAppId("default");
		return list;
	}

	/** 015001007026获取中间库药品目录 -市医保 */
	public List<ViewMedi> queryViewMediList(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		String codeHp = JsonUtil.getFieldValue(param, "codeHp");
		DataSourceRoute.putAppId(datasourcename);
		List<ViewMedi> list = InsSyncBasicsData.queryViewMediList(codeHp);
		DataSourceRoute.putAppId("default");
		return list;
	}

	/** 015001007028获取中间库目录对应表-市医保 */
	public List<ViewMatch> queryViewMatchList(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		String codeHp = JsonUtil.getFieldValue(param, "codeHp");
		DataSourceRoute.putAppId(datasourcename);
		List<ViewMatch> list = InsSyncBasicsData.queryViewMatchList(codeHp);
		DataSourceRoute.putAppId("default");
		return list;
	}

	/** 015001007066获取中间库诊疗项目及服务设施目录-省内异地 */
	public List<ViewItemOut> queryViewItemListOut(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		String codeHp = JsonUtil.getFieldValue(param, "codeHp");
		DataSourceRoute.putAppId(datasourcename);
		List<ViewItemOut> list = InsSyncBasicsData.queryViewItemListOut(codeHp);
		DataSourceRoute.putAppId("default");
		return list;
	}

	/** 015001007067获取中间库药品目录 -省内异地 */
	public List<ViewMediOut> queryViewMediListOut(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		String codeHp = JsonUtil.getFieldValue(param, "codeHp");
		DataSourceRoute.putAppId(datasourcename);
		List<ViewMediOut> list = InsSyncBasicsData.queryViewMediListOut(codeHp);
		DataSourceRoute.putAppId("default");
		return list;
	}

	/** 015001007068获取中间库目录对应表-省内异地 */
	public List<ViewMatchOut> queryViewMatchListOut(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		String codeHp = JsonUtil.getFieldValue(param, "codeHp");
		DataSourceRoute.putAppId(datasourcename);
		List<ViewMatchOut> list = InsSyncBasicsData
				.queryViewMatchListOut(codeHp);
		DataSourceRoute.putAppId("default");
		return list;
	}

	/**
	 * 修改同步单条或多条中间库诊疗项目及服务设施目录数据
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String queryViewItems(String param, IUser user) {
		 String datasourcename = JsonUtil.getFieldValue(param,
		 "datasourcename");
		 String euHpDictType = JsonUtil.getFieldValue(param,
		 "ecbEuHpdicttype");
		 String codelist = JsonUtil.getFieldValue(param, "codelist");
		 String flagFit = JsonUtil.getFieldValue(param, "flagFit");
		String itemmessage = "";
		String medimessage = "";
		String matchmessage = "";
		String indmessage = "";
		return itemmessage + medimessage + matchmessage + indmessage;
	}
}
