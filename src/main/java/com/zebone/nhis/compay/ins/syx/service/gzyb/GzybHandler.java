package com.zebone.nhis.compay.ins.syx.service.gzyb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHis;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisCfxmJksybz;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisFyjs;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisCfxm;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisMz;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisMzdj;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisMzjs;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisMzxm;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisZydj;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class GzybHandler {
	@Resource
	private GzybService gzybService;

	/**
	 * 015001007008 通过通过中间库、身份证号码查询医保中间表-住院登记表信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<MiddleHisZydj> queryInfoList(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		List<MiddleHisZydj> list = new ArrayList<MiddleHisZydj>();
		String gmsfhm = JsonUtil.getFieldValue(param, "gmsfhm");
		String zyh = JsonUtil.getFieldValue(param, "zyh");
		String xm = JsonUtil.getFieldValue(param, "xm");
		String xb = JsonUtil.getFieldValue(param, "xb");
		list = gzybService.queryInfoList(gmsfhm, zyh, xm, xb);
		DataSourceRoute.putAppId("default");
		return list;
	}

	/**
	 * 015001007009 通过中间库、就诊登记号、医保编、身份证号码修改医保中间表-住院登记表读入标识
	 * 
	 * @param param
	 * @param user
	 */
	public void updatedrbz(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		gzybService.updatedrbz(param, user);
		DataSourceRoute.putAppId("default");
	}

	/**
	 * 015001007015将结算数据插入中间库HIS_CFXM、HIS_CFXM_JKSYBZ表
	 * 
	 * @param param
	 * @param user
	 */
	public void addMiddleHisCfxm(String param, IUser user) {
		MiddleHis middleHis = JsonUtil.readValue(param, MiddleHis.class);
		String datasourcename = middleHis.getDatasourcename();
		List<MiddleHisCfxm> cfxmList = middleHis.getMiddleHisCfxmlist();
		List<MiddleHisCfxmJksybz> jksybzlist = middleHis.getJksybzlist();
		DataSourceRoute.putAppId(datasourcename);
		if (cfxmList != null && cfxmList.size() > 0) {
			gzybService.addMiddleHisCfxm(cfxmList);
		}
		if (jksybzlist != null && jksybzlist.size() > 0) {
			gzybService.addMiddleHisCfxmJksybz(jksybzlist);
		}
		DataSourceRoute.putAppId("default");
	}

	/**
	 * 015001007019根据就医登记号更新中间库HIS_FYJS的DRBZ状态
	 * 
	 * @param param
	 * @param user
	 */
	public void updateCostlist(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		gzybService.updateCostlist(param, user);
		DataSourceRoute.putAppId("default");
	}

	/**
	 * 015001007021通过就医登记号获取中间库-费用结算表HIS_FYJS的数据
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public MiddleHisFyjs queryCostInfo(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		MiddleHisFyjs info = gzybService.queryCostInfo(jydjh);
		DataSourceRoute.putAppId("default");
		return info;
	}

	/**
	 * 015001007023通过就医登记号删除中间库-费用明细表HIS_CFXM的数据
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void deleteMiddleHisCfxm(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		gzybService.deleteMiddleHisCfxm(param, user);
		DataSourceRoute.putAppId("default");
	}

	/**
	 * 获取项目序号最大值
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public int getCfxmMaxXh(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		int count = gzybService.getCfxmMaxXh(jydjh);
		DataSourceRoute.putAppId("default");
		return count;
	}

	/** 门诊 */
	/**
	 * 通过通过中间库、身份证号码查询医保中间表-门诊登记表信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<MiddleHisMzdj> queryMZInfoList(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		List<MiddleHisMzdj> list = new ArrayList<MiddleHisMzdj>();
		String gmsfhm = JsonUtil.getFieldValue(param, "gmsfhm");
		String zyh = JsonUtil.getFieldValue(param, "zyh");
		String xm = JsonUtil.getFieldValue(param, "xm");
		String xb = JsonUtil.getFieldValue(param, "xb");
		list = gzybService.queryMZInfoList(gmsfhm, zyh, xm, xb);
		DataSourceRoute.putAppId("default");
		return list;
	}

	/**
	 * 通过中间库、就诊登记号、医保编、身份证号码修改医保中间表-门诊登记表读入标识
	 * 
	 * @param param
	 * @param user
	 */
	public void updateMZdrbz(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		gzybService.updateMZdrbz(param, user);
		DataSourceRoute.putAppId("default");
	}

	/**
	 * 获取项目序号最大值
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public int getMZxmMaxXh(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		int count = gzybService.getMZxmMaxXh(jydjh);
		DataSourceRoute.putAppId("default");
		return count;
	}

	/**
	 * 将结算数据插入中间库HIS_MZXM、HIS_CFXM_JKSYBZ表
	 * 
	 * @param param
	 * @param user
	 */
	public void addMiddleHisMZxm(String param, IUser user) {
		MiddleHisMz middleHisMz = JsonUtil.readValue(param, MiddleHisMz.class);
		String datasourcename = middleHisMz.getDatasourcename();
		List<MiddleHisMzxm> mzxmList = middleHisMz.getMiddleHisMzxmlist();
		List<MiddleHisCfxmJksybz> jksybzlist = middleHisMz.getJksybzlist();
		DataSourceRoute.putAppId(datasourcename);
		if (mzxmList != null && mzxmList.size() > 0) {
			gzybService.addMiddleHisMZxm(mzxmList);
		}
		if (jksybzlist != null && jksybzlist.size() > 0) {
			gzybService.addMiddleHisCfxmJksybz(jksybzlist);
		}
		DataSourceRoute.putAppId("default");
	}

	/**
	 * 通过就医登记号删除中间库-费用明细表HIS_MZXM的数据
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void deleteMiddleHisMZxm(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		gzybService.deleteMiddleHisMZxm(param, user);
		DataSourceRoute.putAppId("default");
	}

	/**
	 * 通过就医登记号获取中间库-门诊费用结算表HIS_MZJS的数据
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public MiddleHisMzjs queryMZCostInfo(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		MiddleHisMzjs info = gzybService.queryMZCostInfo(jydjh);
		DataSourceRoute.putAppId("default");
		return info;
	}

	/**
	 * 根据就医登记号更新中间库HIS_MZJS的DRBZ状态
	 * 
	 * @param param
	 * @param user
	 */
	public void updateMZCostlist(String param, IUser user) {
		String datasourcename = JsonUtil.getFieldValue(param, "datasourcename");
		DataSourceRoute.putAppId(datasourcename);
		gzybService.updateMZCostlist(param, user);
		DataSourceRoute.putAppId("default");
	}

}
