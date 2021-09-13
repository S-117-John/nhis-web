package com.zebone.nhis.compay.pub.syx.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItem;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.module.compay.ins.syx.InsGzybItem;
import com.zebone.nhis.common.module.compay.ins.syx.InsGzybItemmap;
import com.zebone.nhis.common.module.scm.pub.BdPdInd;
import com.zebone.nhis.common.module.scm.pub.BdPdIndpd;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.pub.syx.dao.InsSyncBasicsDataMapper;
import com.zebone.nhis.compay.pub.syx.vo.InsSyncBasicsDataVo;
import com.zebone.nhis.compay.pub.syx.vo.InsSyncVo;
import com.zebone.nhis.compay.pub.syx.vo.SyxHpDataVo;
import com.zebone.nhis.compay.pub.syx.vo.ViewItem;
import com.zebone.nhis.compay.pub.syx.vo.ViewItemOut;
import com.zebone.nhis.compay.pub.syx.vo.ViewMatch;
import com.zebone.nhis.compay.pub.syx.vo.ViewMatchOut;
import com.zebone.nhis.compay.pub.syx.vo.ViewMedi;
import com.zebone.nhis.compay.pub.syx.vo.ViewMediOut;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsSyncBasicsDataService {

	@Autowired
	private InsSyncBasicsDataMapper syncBasicsDataMapper;

	private final String PK_ORG = "~                               ";

	private final String DEF_FLAG = "0";

	private final BigDecimal NUM = new BigDecimal("1");

	private final String ADD_NOTE = "单个目录数据插入时自动同步";

	private final String UP_NOTE = "数据同步修改";

	private final String ONE_TXT = "1";

	private final String TWO_TXT = "2";

	private final double ZERO_NUM = 0;

	private final double ONE_NUM = 1;

	private int codeAdd = 1000;

	/**
	 * 诊疗项目及服务数据 -市医保/花都公医
	 * 
	 * @param codeHp
	 * @return
	 */
	public List<ViewItem> queryViewItemList(String codeHp) {
		List<ViewItem> list = syncBasicsDataMapper.queryViewItemList(codeHp);
		return list;
	}

	/**
	 * 药品数据 -市医保/花都公医
	 * 
	 * @param codeHp
	 * @return
	 */
	public List<ViewMedi> queryViewMediList(String codeHp) {
		List<ViewMedi> list = syncBasicsDataMapper.queryViewMediList(codeHp);
		return list;
	}

	/**
	 * 目录对照-市医保/花都公医
	 * 
	 * @param codeHp
	 * @return
	 */
	public List<ViewMatch> queryViewMatchList(String codeHp) {
		List<ViewMatch> list = syncBasicsDataMapper.queryViewMatchList(codeHp);
		return list;
	}

	/**
	 * 诊疗项目及服务数据 -省内异地
	 * 
	 * @param codeHp
	 * @return
	 */
	public List<ViewItemOut> queryViewItemListOut(String codeHp) {
		List<ViewItemOut> list = syncBasicsDataMapper
				.queryViewItemListOut(codeHp);
		return list;
	}

	/**
	 * 药品数据 -省内异地
	 * 
	 * @param codeHp
	 * @return
	 */
	public List<ViewMediOut> queryViewMediListOut(String codeHp) {
		List<ViewMediOut> list = syncBasicsDataMapper
				.queryViewMediListOut(codeHp);
		return list;
	}

	/**
	 * 目录对照-省内异地
	 * 
	 * @param codeHp
	 * @return
	 */
	public List<ViewMatchOut> queryViewMatchListOut(String codeHp) {
		List<ViewMatchOut> list = syncBasicsDataMapper
				.queryViewMatchListOut(codeHp);
		return list;
	}

	/**
	 * 015001007075省内异地目录同步后若是限制用药则更新审核状态
	 * 
	 * @param param
	 * @param user
	 */
	public void updateItemMap(String param, IUser user) {
		String euHpDictType = JsonUtil.getFieldValue(param, "euHpDictType");
		String sql = "UPDATE INS_GZYB_ITEMMAP SET FLAG_AUDIT='1' WHERE PK_ITEMMAP IN (SELECT itemmap.PK_ITEMMAP FROM INS_GZYB_ITEMMAP itemmap INNER JOIN INS_GZYB_ITEM item ON itemmap.CODE_CENTER=item.CODE_ITEM WHERE itemmap.EU_HPDICTTYPE=? AND item.EU_HPDICTTYPE=? AND itemmap.DEL_FLAG='0' AND item.DEL_FLAG='0' AND item.RANGE_REST IS NOT NULL AND item.RANGE_REST!=' ')";
		DataBaseHelper
				.execute(sql, new Object[] { euHpDictType, euHpDictType });
	}

	/* 单独新增 */
	/**
	 * 015001007069修改同步单条或多条诊疗项目及服务数据 -省内异地
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String addViewItemListOut(String param, IUser user) {
		InsSyncVo allData = JsonUtil.readValue(param, InsSyncVo.class);
		List<ViewItemOut> addItemlist = allData.getAddViewItemListOut();
		String datasourcename = allData.getDatasourcename();
		String euHpDictType = allData.getEuHpDictType();
		String message = "";
		int i = 0;
		String sql = "DELETE FROM INS_GZYB_ITEM  WHERE EU_ITEMTYPE !='0' and EU_HPDICTTYPE=? and CODE_ITEM=?";
		for (ViewItemOut viewitem : addItemlist) {
			if (viewitem == null) {
				continue;
			}
			InsGzybItem item = new InsGzybItem();
			item.setPkInsitem(NHISUUID.getKeyId());
			item.setEuHpDictType(euHpDictType);/* 医保目录类别 */
			item.setEuItemtype("1");/* 项目类型0药品，1诊疗项目，2服务设施 */
			item.setDtStattype(viewitem.getStatType());/* 统计类别 */
			item.setEuDrugtype(null);/* 药品类型0非药品，1 西药，2 中成药，3 中草药 */
			item.setSerialItem(CommonUtils.getInteger(viewitem
					.getSerialItemOut()));/* 诊疗项目序列号 */
			item.setCodeItem(viewitem.getItemCodeOuts());/* 项目编码 */
			item.setNameItem(viewitem.getItemNameOuts());/* 项目名称 */
			item.setNameEng(null);/* 英文名称 */
			item.setSpcode(null);/* 拼音码 */
			item.setdCode(null);/* 五笔码 */
			item.setUnit(viewitem.getUnit());/* 标准单位 */
			item.setModel(null);/* 剂型 */
			item.setEuMedtype(viewitem.getMtFlag());/* 医疗目录 */
			item.setEuStaple(null);/* 甲乙标志0甲类,1乙类,2全自费 */
			item.setFlagOtc(null);/* 处方用药标志 */
			item.setFlagWl(viewitem.getWlFlag());/* 工伤补充目录 */
			item.setFlagBo(viewitem.getBoFlag());/* 生育补充目录 */
			item.setFlagValid(viewitem.getValidFlag());/* 有效标志 */
			item.setDateEffect(viewitem.getEffectDate());/* 生效日期 */
			item.setDateExpire(viewitem.getExpireDate());/* 失效日期 */
			item.setRatio(viewitem.getSelfScale());/* 自负比例 */
			item.setRangeRest(null);/* 限制用药范围 */
			item.setFlagRest(null);/* 限制用药标志 */
			item.setDescConn(datasourcename);/* 数据库连接串 */
			item.setCreateTime(new Date());
			item.setCreator(null);
			item.setDelFlag("0");
			DataBaseHelper.execute(sql,
					new Object[] { euHpDictType, viewitem.getItemCodeOuts() });
			DataBaseHelper.insertBean(item);
			i++;
		}
		message = "本次同步诊疗项目及服务数据" + i + "条；";
		return message;
	}

	/**
	 * 015001007070修改同步单条或多条药品数据-省内异地
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String addViewMediListOut(String param, IUser user) {
		InsSyncVo allData = JsonUtil.readValue(param, InsSyncVo.class);
		List<ViewMediOut> viewMedilist = allData.getAddViewMediListOut();
		String datasourcename = allData.getDatasourcename();
		String euHpDictType = allData.getEuHpDictType();
		String message = "";
		int i = 0;
		String sql = "DELETE FROM INS_GZYB_ITEM  WHERE EU_ITEMTYPE ='0' and EU_HPDICTTYPE=? and CODE_ITEM=?";
		for (ViewMediOut viewMedi : viewMedilist) {
			if (viewMedi == null) {
				continue;
			}
			String euStale = "";
			if ("1".equals(viewMedi.getStapleFlag())) {
				euStale = "0";
			}
			if ("2".equals(viewMedi.getStapleFlag())) {
				euStale = "1";
			}
			if ("9".equals(viewMedi.getStapleFlag())) {
				euStale = "2";
			}
			InsGzybItem item = new InsGzybItem();
			item.setPkInsitem(NHISUUID.getKeyId());
			item.setEuHpDictType(euHpDictType);/* 医保目录类别 */
			item.setEuItemtype("0");/* 项目类型0药品，1诊疗项目，2服务设施 */
			item.setDtStattype(viewMedi.getStatType());/* 统计类别 */
			item.setEuDrugtype(viewMedi.getMediItemType());/* 药品类型0非药品1西药2中成药3中草药 */
			item.setSerialItem(0); /* 诊疗项目序列号 */
			item.setCodeItem(viewMedi.getMediCodeOuts());/* 项目编码 */
			item.setNameItem(viewMedi.getMediNameOuts());/* 项目名称 */
			item.setNameEng(viewMedi.getEnglishName());/* 英文名称 */
			item.setSpcode(viewMedi.getCodePy()); /* 拼音码 */
			item.setdCode(viewMedi.getCodeWb()); /* 五笔码 */
			item.setUnit(null); /* 标准单位 */
			item.setModel(viewMedi.getModel()); /* 剂型 */
			item.setEuMedtype(viewMedi.getMtFlag()); /* 医疗目录 */
			item.setEuStaple(euStale); /* 甲乙标志0甲类,1乙类,2全自费 */
			item.setFlagOtc(viewMedi.getOtc()); /* 处方用药标志 */
			item.setFlagWl(viewMedi.getWlFlag()); /* 工伤补充目录 */
			item.setFlagBo(viewMedi.getBoFlag()); /* 生育补充目录 */
			item.setFlagValid(viewMedi.getValidFlag()); /* 有效标志 */
			item.setDateEffect(viewMedi.getEffectDate()); /* 生效日期 */
			item.setDateExpire(viewMedi.getExpireDate());/* 失效日期 */
			item.setRatio(viewMedi.getSelfScale()); /* 自负比例 */
			item.setFlagRest(viewMedi.getRangeFlag());/* 限制用药标志 */
			item.setRangeRest(viewMedi.getRange());/* 限制用药范围 */
			item.setDescConn(datasourcename);/* 数据库连接串 */

			item.setCreateTime(new Date());
			item.setCreator(null);
			item.setDelFlag("0");
			DataBaseHelper.execute(sql,
					new Object[] { euHpDictType, viewMedi.getMediCodeOuts() });
			DataBaseHelper.insertBean(item);
			i++;
		}
		message = "本次同步药品字典数据" + i + "条；";
		return message;
	}

	/**
	 * 015001007071修改同步单条或多条目录对照-省内异地
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String addViewMatchListOut(String param, IUser user) {
		InsSyncVo allData = JsonUtil.readValue(param, InsSyncVo.class);
		List<ViewMatchOut> viewMedilist = allData.getAddViewMatchListOut();
		String datasourcename = allData.getDatasourcename();
		String euHpDictType = allData.getEuHpDictType();
		String message = "";
		int i = 0;
		String sql = "DELETE FROM INS_GZYB_ITEMMAP WHERE EU_HPDICTTYPE=? AND CODE_HOSP=?";
		for (ViewMatchOut itemInfo : viewMedilist) {
			if (itemInfo == null) {
				continue;
			}
			InsGzybItemmap itemmap = new InsGzybItemmap();
			itemmap.setPkItemmap(NHISUUID.getKeyId());
			itemmap.setPkOrg(null);/* 所属机构 */
			itemmap.setEuHpDictType(euHpDictType);/* 医保目录类别 */
			itemmap.setEuItemtype(itemInfo.getMatchType());/* 项目类型 */
			itemmap.setCodeCenter(itemInfo.getItemCodeOut());/* 中心目录编码 */
			itemmap.setCodeHosp(itemInfo.getHospCode());/* 医院目录编码 */
			itemmap.setFlagAudit(null);/* 审核标志 */
			itemmap.setDescConn(datasourcename);/* 数据库连接串 */
			itemmap.setCreateTime(new Date());
			itemmap.setCreator(null);
			itemmap.setDelFlag("0");
			DataBaseHelper.execute(sql,
					new Object[] { euHpDictType, itemInfo.getHospCode() });
			DataBaseHelper.insertBean(itemmap);
			i++;
		}
		message = "本次同步目录对照数据" + i + "条；";
		return message;
	}

	/**
	 * 015001007072保存诊疗项目及服务数据-市医保/花都公医
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String addViewItemList(String param, IUser user) {
		InsSyncVo allData = JsonUtil.readValue(param, InsSyncVo.class);
		List<ViewItem> addItemlist = allData.getAddViewItemList();
		String datasourcename = allData.getDatasourcename();
		String euHpDictType = allData.getEuHpDictType();
		String message = "";
		int i = 0;
		String sql = "DELETE FROM INS_GZYB_ITEM WHERE EU_ITEMTYPE !='0' and EU_HPDICTTYPE=? and CODE_ITEM=?";
		for (ViewItem viewitem : addItemlist) {
			if (viewitem == null) {
				continue;
			}
			String euStale = "";
			if ("1".equals(viewitem.getStapleFlag())) {
				euStale = "0";
			}
			if ("2".equals(viewitem.getStapleFlag())) {
				euStale = "1";
			}
			if ("9".equals(viewitem.getStapleFlag())) {
				euStale = "2";
			}
			InsGzybItem item = new InsGzybItem();
			item.setPkInsitem(NHISUUID.getKeyId());
			item.setEuHpDictType(euHpDictType);/* 医保目录类别 */
			item.setEuItemtype("1");/* 项目类型0药品，1诊疗项目，2服务设施 */
			item.setDtStattype(viewitem.getStatType());/* 统计类别 */
			item.setEuDrugtype(null);/* 药品类型0非药品，1 西药，2 中成药，3 中草药 */
			item.setSerialItem(CommonUtils.getInteger(viewitem.getSerialItem()));/* 诊疗项目序列号 */
			item.setCodeItem(viewitem.getItemCode());/* 项目编码 */
			item.setNameItem(viewitem.getItemName());/* 项目名称 */
			item.setNameEng(null);/* 英文名称 */
			item.setSpcode(null);/* 拼音码 */
			item.setdCode(null);/* 五笔码 */
			item.setUnit(viewitem.getUnit());/* 标准单位 */
			item.setModel(null);/* 剂型 */
			item.setEuMedtype(viewitem.getMtFlag());/* 医疗目录 */
			item.setEuStaple(euStale);/* 甲乙标志0甲类,1乙类,2全自费 */
			item.setFlagOtc(null);/* 处方用药标志 */
			item.setFlagWl(viewitem.getWlFlag());/* 工伤补充目录 */
			item.setFlagBo(viewitem.getBoFlag());/* 生育补充目录 */
			item.setFlagValid(viewitem.getValidFlag());/* 有效标志 */
			item.setDateEffect(viewitem.getEffectDate());/* 生效日期 */
			item.setDateExpire(viewitem.getExpireDate());/* 失效日期 */
			item.setRatio(viewitem.getSelfScale());/* 自负比例 */
			item.setFlagRest(null);/* 限制用药标志 */
			item.setRangeRest(null);/* 限制用药范围 */
			item.setDescConn(datasourcename);/* 数据库连接串 */
			item.setCreateTime(new Date());
			item.setCreator(null);
			item.setDelFlag("0");
			DataBaseHelper.execute(sql,
					new Object[] { euHpDictType, viewitem.getItemCode() });
			DataBaseHelper.insertBean(item);
			i++;
		}
		message = "本次同步诊疗项目及服务数据" + i + "条；";

		return message;
	}

	/**
	 * 015001007073保存药品数据-市医保/花都公医
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String addViewMediList(String param, IUser user) {
		InsSyncVo allData = JsonUtil.readValue(param, InsSyncVo.class);
		List<ViewMedi> addViewMediList = allData.getAddViewMediList();
		String datasourcename = allData.getDatasourcename();
		String euHpDictType = allData.getEuHpDictType();
		String message = "";
		int i = 0;
		String sql = "DELETE FROM INS_GZYB_ITEM WHERE EU_ITEMTYPE ='0' and EU_HPDICTTYPE=? and CODE_ITEM=?";
		for (ViewMedi viewMedi : addViewMediList) {
			if (viewMedi == null) {
				continue;
			}
			String euStale = "";
			if ("1".equals(viewMedi.getStapleFlag())) {
				euStale = "0";
			}
			if ("2".equals(viewMedi.getStapleFlag())) {
				euStale = "1";
			}
			if ("9".equals(viewMedi.getStapleFlag())) {
				euStale = "2";
			}
			InsGzybItem item = new InsGzybItem();
			item.setPkInsitem(NHISUUID.getKeyId());
			item.setEuHpDictType(euHpDictType);/* 医保目录类别 */
			item.setEuItemtype("0");/* 项目类型0药品，1诊疗项目，2服务设施 */
			item.setDtStattype(viewMedi.getStatType());/* 统计类别 */
			item.setEuDrugtype(viewMedi.getMediItemType());/* 药品类型0非药品1西药2中成药3中草药 */
			item.setSerialItem(0); /* 诊疗项目序列号 */
			item.setCodeItem(viewMedi.getMediCode());/* 项目编码 */
			item.setNameItem(viewMedi.getMediName());/* 项目名称 */
			item.setNameEng(viewMedi.getEnglishName());/* 英文名称 */
			item.setSpcode(viewMedi.getCodePy()); /* 拼音码 */
			item.setdCode(viewMedi.getCodeWb()); /* 五笔码 */
			item.setUnit(null); /* 标准单位 */
			item.setModel(viewMedi.getModel()); /* 剂型 */
			item.setEuMedtype(viewMedi.getMtFlag()); /* 医疗目录 */
			item.setEuStaple(euStale); /* 甲乙标志0甲类,1乙类,2全自费 */
			item.setFlagOtc(viewMedi.getOtc()); /* 处方用药标志 */
			item.setFlagWl(viewMedi.getWlFlag()); /* 工伤补充目录 */
			item.setFlagBo(viewMedi.getBoFlag()); /* 生育补充目录 */
			item.setFlagValid(viewMedi.getValidFlag()); /* 有效标志 */
			item.setDateEffect(viewMedi.getEffectDate()); /* 生效日期 */
			item.setDateExpire(viewMedi.getExpireDate());/* 失效日期 */
			item.setRatio(viewMedi.getSelfScale()); /* 自负比例 */
			item.setFlagRest(viewMedi.getRangeFlag());/* 限制用药标志 */
			item.setRangeRest(viewMedi.getRange());/* 限制用药范围 */
			item.setDescConn(datasourcename);/* 数据库连接串 */
			item.setCreateTime(new Date());
			item.setCreator(null);
			item.setDelFlag("0");
			DataBaseHelper.execute(sql,
					new Object[] { euHpDictType, viewMedi.getMediCode() });
			DataBaseHelper.insertBean(item);
			i++;
		}
		message = "本次同步药品字典数据" + i + "条；";
		return message;
	}

	/**
	 * 015001007074保存目录对照-市医保/花都公医
	 * 
	 * @param param
	 * @param user
	 * @return
	 */

	public String addViewMatchList(String param, IUser user) {
		InsSyncVo allData = JsonUtil.readValue(param, InsSyncVo.class);
		List<ViewMatch> list = allData.getAddViewMatchList();
		String datasourcename = allData.getDatasourcename();
		String euHpDictType = allData.getEuHpDictType();
		String message = "";
		int i = 0;
		String sql = "DELETE FROM INS_GZYB_ITEMMAP WHERE EU_HPDICTTYPE=? AND CODE_HOSP=?";

		for (ViewMatch itemInfo : list) {
			if (itemInfo == null) {
				continue;
			}
			InsGzybItemmap itemmap = new InsGzybItemmap();
			itemmap.setPkItemmap(NHISUUID.getKeyId());
			itemmap.setPkOrg(null);/* 所属机构 */
			itemmap.setEuHpDictType(euHpDictType);/* 医保目录类别 */
			itemmap.setEuItemtype(itemInfo.getMatchType());/* 项目类型 */
			itemmap.setCodeCenter(itemInfo.getItemCode());/* 中心目录编码 */
			itemmap.setCodeHosp(itemInfo.getHospCode());/* 医院目录编码 */
			itemmap.setFlagAudit(itemInfo.getAuditFlag());/* 审核标志 */
			itemmap.setDescConn(datasourcename);/* 数据库连接串 */
			itemmap.setCreateTime(new Date());
			itemmap.setCreator(null);
			itemmap.setDelFlag("0");
			DataBaseHelper.execute(sql,
					new Object[] { euHpDictType, itemInfo.getHospCode() });
			DataBaseHelper.insertBean(itemmap);
			i++;
		}
		message = "本次同步目录对照数据" + i + "条；";
		return message;
	}

	/* 015001007035查询显示同步字典数据 */
	public List<InsSyncBasicsDataVo> seachInsSyncBasicsData(String param,
			IUser user) {
		String euhphicttype = JsonUtil.getFieldValue(param, "euHpDictType");
		String euitemtype = JsonUtil.getFieldValue(param, "euItemtype");
		String keywords = JsonUtil.getFieldValue(param, "keyword");
		String keyword = keywords.toUpperCase();
		List<InsSyncBasicsDataVo> list = new ArrayList<InsSyncBasicsDataVo>();
		list = syncBasicsDataMapper.seachInsSyncBasicsData(euhphicttype,
				euitemtype, keyword);
		return list;
	}

	/**
	 * 同步医保记费策略及限制用药表等
	 * @param euhphicttype
	 * @param codehosp
	 */
	public void savaHpDataCopy(String euhphicttype, String codehosp) {
		/* 1.查询需要同步的医保记费策略 */
		String sql = "select  dt_hpdicttype euHpdicttype, pk_hpcgdiv pkHpcgdiv, name_div nameDiv from bd_hp_cgdiv where dt_hpdicttype=?";
		List<Map<String, Object>> hpCgdivList = DataBaseHelper.queryForList(
				sql, new Object[] { euhphicttype });
		if (hpCgdivList == null || hpCgdivList.size() <= 0)
			return;

		/* 2.删除医保目录中去除的费用 */
		deleteCgDic(hpCgdivList, codehosp);

		/* 3.获取需要保存的医保数据 */
		qryNeedCopyHpData(hpCgdivList, codehosp);
		/* 4.处理保存任务:插入bd_pd_ind,bd_pd_indpd,bd_item_hp */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("euhpdicttype", euhphicttype);
		map.put("codehosp", codehosp);
		List<SyxHpDataVo> dataCopyVos = syncBasicsDataMapper
				.qryNeedHpDataForOther(map);
		String codeSql = "select max(code_ind) from bd_pd_ind where code_ind is not null";
		Integer codeInd = DataBaseHelper.queryForScalar(codeSql, Integer.class,
				new Object[] {});
		if (codeInd != null) {
			this.codeAdd = codeInd;
		}
		savaHpcgdiv(dataCopyVos);
	}

	/**
	 * 删除医保目录中去除的费用
	 * 
	 * @param hpCgdivList
	 */
	private void deleteCgDic(List<Map<String, Object>> hpCgdivList,
			String codehosp) {
		List<String> upSqls = new ArrayList<String>();
		for (Map<String, Object> hpCgdiv : hpCgdivList) {
			/* 去除不再医保数据中的记费策略 */
			StringBuffer upSql = new StringBuffer(
					"update bd_hp_cgdiv_item set del_flag='1' where pk_hpcgdiv = '");
			upSql.append(hpCgdiv.get("pkhpcgdiv"));
			upSql.append("'  and del_flag='0' and  eu_divide ='2'");
			upSql.append("  and bd_hp_cgdiv_item.code ='");
			upSql.append(codehosp);
			upSql.append("'");
			upSqls.add(upSql.toString());

		}
		DataBaseHelper.batchUpdate(upSqls.toArray(new String[0]));
	}

	/**
	 * 
	 * @param hpCgdivList
	 * @param codehosp
	 */
	private void qryNeedCopyHpData(List<Map<String, Object>> hpCgdivList,
			String codehosp) {
		for (Map<String, Object> map : hpCgdivList) {
			/* 更新操作：pk_item和code匹配一致：并且eu_divide=2时，当eu_divide!=2时不更新不插入 */
			map.put("codehosp", codehosp);
			map.put("status", "UPDATE");
			List<SyxHpDataVo> updateDatas = syncBasicsDataMapper
					.qryNeedCopyHpData(map);
			updateHpcgdivItem(updateDatas);

			/* 插入操作：pk_item 和code同时不存在时 */
			map.put("status", "ADD");
			List<SyxHpDataVo> insertDatas = syncBasicsDataMapper
					.qryNeedCopyHpData(map);
			insertHpcgdivItem(insertDatas);

		}
	}

	/**
	 * 插入医保记费策略数据
	 * 
	 * @param hpDataCopyVos
	 */
	private void insertHpcgdivItem(List<SyxHpDataVo> hpDataCopyVos) {
		List<BdHpCgdivItem> hpcgdivItems = new ArrayList<BdHpCgdivItem>();
		for (SyxHpDataVo data : hpDataCopyVos) {
			/* 构建BdHpcgdivItem保存数据 */
			BdHpCgdivItem cgItem = new BdHpCgdivItem();
			ApplicationUtils.copyProperties(cgItem, data);
			cgItem.setPkOrg(PK_ORG);
			cgItem.setDelFlag(DEF_FLAG);
			if (TWO_TXT.equals(cgItem.getDtHptype())) {
				cgItem.setEuDivide(ONE_TXT);
				cgItem.setRate(ONE_NUM);
			} else {
				cgItem.setEuDivide(TWO_TXT);
				cgItem.setRate(ZERO_NUM);
			}
			cgItem.setPkHpcgdivitem(NHISUUID.getKeyId());
			cgItem.setNote(ADD_NOTE);
			cgItem.setCreateTime(new Date());
			hpcgdivItems.add(cgItem);
		}
		if (hpcgdivItems.size() > 0) {
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(BdHpCgdivItem.class),
					hpcgdivItems);
		}
	}

	/**
	 * 更新医保记费策略数据
	 * 
	 * @param hpDataCopyVos
	 */
	private void updateHpcgdivItem(List<SyxHpDataVo> hpDataCopyVos) {
		List<BdHpCgdivItem> hpcgdivItems = new ArrayList<BdHpCgdivItem>();
		for (SyxHpDataVo data : hpDataCopyVos) {
			/* 构建BdHpcgdivItem保存数据 */
			BdHpCgdivItem cgItem = new BdHpCgdivItem();
			ApplicationUtils.copyProperties(cgItem, data);
			cgItem.setPkOrg(PK_ORG);
			cgItem.setDelFlag(DEF_FLAG);
			if (TWO_TXT.equals(cgItem.getDtHptype())) {
				cgItem.setEuDivide(ONE_TXT);
				cgItem.setRate(ONE_NUM);
			} else {
				cgItem.setEuDivide(TWO_TXT);
				cgItem.setRate(ZERO_NUM);
			}
			cgItem.setModityTime(new Date());
			cgItem.setTs(new Date());
			cgItem.setNote(UP_NOTE);
			hpcgdivItems.add(cgItem);
		}
		if (hpcgdivItems.size() > 0) {
			StringBuffer upSql = new StringBuffer(
					"update bd_hp_cgdiv_item  set pk_org=:pkOrg, FLAG_PD=:flagPd,dt_hptype=:dtHptype,eu_divide=:euDivide,");
			upSql.append(" RATE=:rate,MODIFIER=:modifier,MODITY_TIME=:modityTime,ts=:ts,del_flag='0'");
			upSql.append(" where pk_item=:pkItem and code=:code and pk_hpcgdiv=:pkHpcgdiv and eu_divide <> '2' ");
			DataBaseHelper.batchUpdate(upSql.toString(), hpcgdivItems);
		}
	}

	/**
	 * 插入bd_pd_ind,bd_pd_indpd,bd_item_hp
	 * 
	 * @param hpDataCopyVos
	 */
	private void savaHpcgdiv(List<SyxHpDataVo> hpDataCopyVos) {
		List<BdItemHp> itemHpList = new ArrayList<BdItemHp>();
		List<BdPdInd> pdIndList = new ArrayList<BdPdInd>();
		List<BdPdIndpd> pdindPdList = new ArrayList<BdPdIndpd>();
		/* 删除关联的医保收费项目 */
		String delItemSql = "delete from bd_item_hp where  PK_ITEM=?";
		/* 删除医保关联的适应症用药信息 */
		String delPdindSql = "delete from bd_pd_indpd where PK_PD=?";

		for (SyxHpDataVo data : hpDataCopyVos) {
			/* 构建bd_item_hp保存数据 */
			BdItemHp itemHp = new BdItemHp();
			itemHp.setPkItemhp(NHISUUID.getKeyId());
			itemHp.setPkOrg(PK_ORG);
			itemHp.setEuItemType(data.getFlagPd());
			itemHp.setPkItem(data.getPkItem());
			itemHp.setEuLevel(data.getDtHptype());
			itemHp.setCodeHp(data.getCode());
			String ratioSelf = data.getRatio() == null ? "" : data.getRatio()
					.toString();
			itemHp.setRatioSelf(ratioSelf);
			itemHp.setNote(data.getRangeRest());
			itemHp.setCreateTime(new Date());
			itemHp.setDtHpdicttype(data.getEuHpdicttype());
			itemHp.setTs(new Date());
			itemHp.setDelFlag(DEF_FLAG);
			itemHpList.add(itemHp);
			DataBaseHelper.execute(delItemSql,
					new Object[] { data.getPkItem() });
			if (!"1".equals(data.getFlagRest()))
				continue;
			/* 构建bd_pd_ind保存数据 */
			BdPdInd pdInd = new BdPdInd();
			pdInd.setPkPdind(NHISUUID.getKeyId());
			pdInd.setPkHp(data.getPkHp());
			pdInd.setPkOrg(PK_ORG);
			String codeInd = String.valueOf(codeAdd++);
			pdInd.setCodeInd(codeInd);
			pdInd.setNameInd(data.getRangeRest());
			pdInd.setRatioBase(NUM);
			pdInd.setRatioBear(NUM);
			pdInd.setRatioComm(NUM);
			pdInd.setRatioInj(NUM);
			pdInd.setRatioRec(NUM);
			pdInd.setRatioRet(NUM);
			pdInd.setRatioSpec(NUM);
			pdInd.setCodeIndtype(data.getEuHpdicttype());
			pdInd.setDescInd(data.getRangeRest());
			pdInd.setCreateTime(new Date());
			pdInd.setTs(new Date());
			pdInd.setDelFlag(DEF_FLAG);
			pdIndList.add(pdInd);

			/* 构建bd_pd_indpd保存数据 */
			BdPdIndpd pdindpd = new BdPdIndpd();
			pdindpd.setPkPdindpd(NHISUUID.getKeyId());
			pdindpd.setPkPdind(pdInd.getPkPdind());
			pdindpd.setPkOrg(PK_ORG);
			pdindpd.setPkPd(data.getPkItem());
			pdindpd.setCreateTime(new Date());
			pdindpd.setTs(new Date());
			pdindpd.setDelFlag(DEF_FLAG);
			pdindPdList.add(pdindpd);
			DataBaseHelper.execute(delPdindSql,
					new Object[] { data.getPkItem() });
		}
		if (itemHpList.size() > 0) {
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(BdItemHp.class), itemHpList);
		}
		if (pdIndList.size() > 0) {
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(BdPdInd.class), pdIndList);
		}
		if (pdindPdList.size() > 0) {
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(BdPdIndpd.class), pdindPdList);
		}
	}

}
