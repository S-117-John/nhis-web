package com.zebone.nhis.task.compay.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItem;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.module.compay.ins.syx.InsGzybItem;
import com.zebone.nhis.common.module.compay.ins.syx.InsGzybItemmap;
import com.zebone.nhis.common.module.scm.pub.BdPdInd;
import com.zebone.nhis.common.module.scm.pub.BdPdIndpd;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.pub.syx.vo.ViewItem;
import com.zebone.nhis.compay.pub.syx.vo.ViewItemOut;
import com.zebone.nhis.compay.pub.syx.vo.ViewMatch;
import com.zebone.nhis.compay.pub.syx.vo.ViewMatchOut;
import com.zebone.nhis.compay.pub.syx.vo.ViewMedi;
import com.zebone.nhis.compay.pub.syx.vo.ViewMediOut;
import com.zebone.nhis.task.compay.dao.SyxHpDataCopyMapper;
import com.zebone.nhis.task.compay.vo.HpViewItemOutVo;
import com.zebone.nhis.task.compay.vo.HpViewItemVo;
import com.zebone.nhis.task.compay.vo.HpViewMatchOutVo;
import com.zebone.nhis.task.compay.vo.HpViewMatchVo;
import com.zebone.nhis.task.compay.vo.HpViewMediOutVo;
import com.zebone.nhis.task.compay.vo.HpViewMediVo;
import com.zebone.nhis.task.compay.vo.SyxHpDataCopyVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import com.zebone.nhis.compay.pub.syx.service.InsSyncBasicsDataHandler;
import com.zebone.nhis.compay.pub.syx.service.InsSyncBasicsDataService;

/**
 * 中山二院医保同步定时任务
 * 
 * @author
 * 
 */
@Service
public class InsSyxTaskService {
	@Resource
	private SyxHpDataCopyMapper syxHpDataCopyMapper;

	private InsSyncBasicsDataHandler insSyncBasicsDataHandler;
	private InsSyncBasicsDataService insSyncBasicsDataService;
	private final String PK_ORG = "~                               ";

	private final String DEF_FLAG = "0";

	private final BigDecimal NUM = new BigDecimal("1");

	private final String ADD_NOTE = "自动同步";

	private final String UP_NOTE = "数据同步修改";

	private final String ONE_TXT = "1";

	private final String TWO_TXT = "2";

	private final double ZERO_NUM = 0;

	private final double ONE_NUM = 1;

	private int codeAdd = 1000;

	public void savaHpDataCopy() {
		// 1.查询需要同步的医保记费策略共有多少种
		String sql = "select  dt_hpdicttype euHpdicttype, pk_hpcgdiv pkHpcgdiv, name_div nameDiv from bd_hp_cgdiv where dt_hpdicttype is not null";
		List<Map<String, Object>> hpCgdivList = DataBaseHelper.queryForList(
				sql, new Object[] {});
		if (hpCgdivList == null || hpCgdivList.size() <= 0)
			return;

		// 2.删除医保目录中去除的费用
		deleteCgDic(hpCgdivList);

		// 3.获取需要保存的医保数据
		qryNeedCopyHpData(hpCgdivList);
		// 4.处理保存任务:插入bd_pd_ind,bd_pd_indpd,bd_item_hp
		List<SyxHpDataCopyVo> dataCopyVos = syxHpDataCopyMapper
				.qryNeedHpDataForOther();
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
	private void deleteCgDic(List<Map<String, Object>> hpCgdivList) {
		List<String> upSqls = new ArrayList<String>();
		for (Map<String, Object> hpCgdiv : hpCgdivList) {
			// 1.去除不再医保数据中的记费策略
			StringBuffer upSql = new StringBuffer(
					"update bd_hp_cgdiv_item set del_flag='1' where pk_hpcgdiv = '");
			upSql.append(hpCgdiv.get("pkhpcgdiv"));
			upSql.append("'  and del_flag='0' and  eu_divide ='2' and (not exists(select 1    from ins_gzyb_item item ,ins_gzyb_itemmap map");
			upSql.append("  where item.code_item =map.code_center and item.eu_hpdicttype ='");
			upSql.append(hpCgdiv.get("euhpdicttype"));
			upSql.append("' and bd_hp_cgdiv_item.code =map.code_hosp))");
			upSqls.add(upSql.toString());

		}
		// 2.删除关联的医保收费项目
		String delItemSql = "delete from  bd_item_hp where  exists (select 1 from bd_defdoc where code_defdoclist='040010' and del_flag ='0' and bd_defdoc.code=bd_item_hp.dt_hpdicttype)";

		// 3.删除医保关联的适应症用药信息
		String delPdindSql = "delete from bd_pd_indpd where pk_pdind in (select pk_pdind from bd_pd_ind where exists (select 1 from bd_defdoc where code_defdoclist='040010' and del_flag ='0' and bd_defdoc.code=bd_pd_ind.code_indtype))";
		String delPdSql = "delete from bd_pd_ind where exists (select 1 from bd_defdoc where code_defdoclist='040010' and del_flag ='0' and bd_defdoc.code=bd_pd_ind.code_indtype)";
		DataBaseHelper.batchUpdate(upSqls.toArray(new String[0]));
		DataBaseHelper.execute(delItemSql, new Object[] {});
		DataBaseHelper.execute(delPdindSql, new Object[] {});
		DataBaseHelper.execute(delPdSql, new Object[] {});
	}

	/**
	 * 查询医保目录
	 * 
	 * @param hpCgdivList
	 * @return
	 */
	private void qryNeedCopyHpData(List<Map<String, Object>> hpCgdivList) {
		for (Map<String, Object> map : hpCgdivList) {
			// 更新操作：pk_item和code匹配一致：并且eu_divide=2时，当eu_divide!=2时不更新不插入
			map.put("status", "UPDATE");
			List<SyxHpDataCopyVo> updateDatas = syxHpDataCopyMapper
					.qryNeedCopyHpData(map);
			updateHpcgdivItem(updateDatas);

			// 插入操作：pk_item 和code同时不存在时
			map.put("status", "ADD");
			List<SyxHpDataCopyVo> insertDatas = syxHpDataCopyMapper
					.qryNeedCopyHpData(map);
			insertHpcgdivItem(insertDatas);

		}
	}

	/**
	 * 插入医保记费策略数据
	 * 
	 * @param hpDataCopyVos
	 */
	private void insertHpcgdivItem(List<SyxHpDataCopyVo> hpDataCopyVos) {
		List<BdHpCgdivItem> hpcgdivItems = new ArrayList<BdHpCgdivItem>();
		for (SyxHpDataCopyVo data : hpDataCopyVos) {
			// 1.构建BdHpcgdivItem保存数据
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
			// DataBaseHelper.insertBean(cgItem);
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
	private void updateHpcgdivItem(List<SyxHpDataCopyVo> hpDataCopyVos) {
		List<BdHpCgdivItem> hpcgdivItems = new ArrayList<BdHpCgdivItem>();
		for (SyxHpDataCopyVo data : hpDataCopyVos) {
			// 1.构建BdHpcgdivItem保存数据
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
	 * @param pkEmp
	 */
	private void savaHpcgdiv(List<SyxHpDataCopyVo> hpDataCopyVos) {
		List<BdItemHp> itemHpList = new ArrayList<BdItemHp>();
		List<BdPdInd> pdIndList = new ArrayList<BdPdInd>();
		List<BdPdIndpd> pdindPdList = new ArrayList<BdPdIndpd>();
		for (SyxHpDataCopyVo data : hpDataCopyVos) {
			// 2.构建bd_item_hp保存数据
			BdItemHp itemHp = new BdItemHp();
			itemHp.setPkItemhp(NHISUUID.getKeyId());
			itemHp.setPkOrg(PK_ORG);
			itemHp.setEuItemType(data.getFlagPd());
			itemHp.setPkItem(data.getPkItem());
			// itemHp.setPkHp(data.getPkHp());
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

			if (!"1".equals(data.getFlagRest()))
				continue;
			// 3.构建bd_pd_ind保存数据
			BdPdInd pdInd = new BdPdInd();
			pdInd.setPkPdind(NHISUUID.getKeyId());
			pdInd.setPkHp(data.getPkHp());
			pdInd.setPkOrg(PK_ORG);
			// 通过医生站的公共方法生成不同表的序列号
			// Integer codeInd=bdSnService.getSerialNo("bd_pd_ind", "code_ind",
			// 8,null);
			String codeInd = String.valueOf(codeAdd++);
			pdInd.setCodeInd(codeInd);// 通过序列号来获取
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

			// 4.构建bd_pd_indpd保存数据
			BdPdIndpd pdindpd = new BdPdIndpd();
			pdindpd.setPkPdindpd(NHISUUID.getKeyId());
			pdindpd.setPkPdind(pdInd.getPkPdind());
			pdindpd.setPkOrg(PK_ORG);
			pdindpd.setPkPd(data.getPkItem());
			pdindpd.setCreateTime(new Date());
			pdindpd.setTs(new Date());
			pdindpd.setDelFlag(DEF_FLAG);
			pdindPdList.add(pdindpd);
			// DataBaseHelper.insertBean(itemHp);
			// DataBaseHelper.insertBean(pdInd);
			// DataBaseHelper.insertBean(pdindpd);

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

	/**
	 * 启用定时任务放入
	 * 
	 * @param cfg
	 */
	public void hpCgDivDataCopyTask(QrtzJobCfg cfg) {
		try {
			savaHpDataCopy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 定时同步医保目录
	 * @param cfg
	 */
	public void hpDataCopyTask(QrtzJobCfg cfg) {
		try {
			savagzHpData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DataSourceRoute.putAppId("default");
	}

	/**
	 * 定时同步医保目录数据
	 */
	public void savagzHpData() {
		DataSourceRoute.putAppId("10yy004002");
		/* 市医保 */
		List<HpViewItemVo> gzItemlist = syxHpDataCopyMapper.queryViewItemList();
		List<HpViewMediVo> gzMedilist = syxHpDataCopyMapper.queryViewMediList();
		List<HpViewMatchVo> gzMatchlist = syxHpDataCopyMapper
				.queryViewMatchList();
		DataSourceRoute.putAppId("default");
		addViewItemList(gzItemlist, "10yy004002", "1");
		addViewMediList(gzMedilist, "10yy004002", "1");
		addViewMatchList(gzMatchlist, "10yy004002", "1");
		
		/* 异地医保 */
		DataSourceRoute.putAppId("10yy004002");
		List<HpViewItemOutVo> ydItemlist = syxHpDataCopyMapper
				.queryViewItemListOut();
		List<HpViewMediOutVo> ydMedilist = syxHpDataCopyMapper
				.queryViewMediListOut();
		List<HpViewMatchOutVo> ydMatchlist = syxHpDataCopyMapper
				.queryViewMatchListOut();
		DataSourceRoute.putAppId("default");
		addViewItemListOut(ydItemlist, "10yy004002", "2");
		addViewMediListOut(ydMedilist, "10yy004002", "2");
		addViewMatchListOut(ydMatchlist, "10yy004002", "2");
		/* 花都公医 */
		DataSourceRoute.putAppId("10yy004002_hdgy");
		List<HpViewItemVo> hdItemlist = syxHpDataCopyMapper.queryViewItemList();
		List<HpViewMediVo> hdMedilist = syxHpDataCopyMapper.queryViewMediList();
		List<HpViewMatchVo> hdMatchlist = syxHpDataCopyMapper
				.queryViewMatchList();
		DataSourceRoute.putAppId("default");
		addViewItemList(hdItemlist, "10yy004002_hdgy", "3");
		addViewMediList(hdMedilist, "10yy004002_hdgy", "3");
		addViewMatchList(hdMatchlist, "10yy004002_hdgy", "3");
	}

	/**
	 * 同步诊疗项目及服务数据 -省内异地
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void addViewItemListOut(List<HpViewItemOutVo> viewItemlist,
			String datasourcename, String euHpDictType) {
		String sql = "DELETE FROM INS_GZYB_ITEM  WHERE EU_ITEMTYPE !='0' and EU_HPDICTTYPE=? and CODE_ITEM=?";
		for (HpViewItemOutVo viewitem : viewItemlist) {
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
		}
	}

	/**
	 * 同步药品数据-省内异地
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void addViewMediListOut(List<HpViewMediOutVo> viewMedilist,
			String datasourcename, String euHpDictType) {
		String sql = "DELETE FROM INS_GZYB_ITEM  WHERE EU_ITEMTYPE ='0' and EU_HPDICTTYPE=? and CODE_ITEM=?";
		for (HpViewMediOutVo viewMedi : viewMedilist) {
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
		}
	}

	/**
	 * 同步目录对照-省内异地
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void addViewMatchListOut(List<HpViewMatchOutVo> viewMatchlist,
			String datasourcename, String euHpDictType) {
		String sql = "DELETE FROM INS_GZYB_ITEMMAP WHERE EU_HPDICTTYPE=? AND CODE_HOSP=?";
		for (HpViewMatchOutVo itemInfo : viewMatchlist) {
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
		}
	}

	/**
	 * 保存诊疗项目及服务数据-市医保/花都公医
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void addViewItemList(List<HpViewItemVo> viewItemlist,
			String datasourcename, String euHpDictType) {
		String sql = "DELETE FROM INS_GZYB_ITEM WHERE EU_ITEMTYPE !='0' and EU_HPDICTTYPE=? and CODE_ITEM=?";
		for (HpViewItemVo viewitem : viewItemlist) {
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
		}
	}

	/**
	 * 保存药品数据-市医保/花都公医
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void addViewMediList(List<HpViewMediVo> viewMediList,
			String datasourcename, String euHpDictType) {
		String sql = "DELETE FROM INS_GZYB_ITEM WHERE EU_ITEMTYPE ='0' and EU_HPDICTTYPE=? and CODE_ITEM=?";
		for (HpViewMediVo viewMedi : viewMediList) {
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
		}
	}

	/**
	 * 保存目录对照-市医保/花都公医
	 * 
	 * @param param
	 * @param user
	 * @return
	 */

	public void addViewMatchList(List<HpViewMatchVo> viewMatchlist,
			String datasourcename, String euHpDictType) {
		String sql = "DELETE FROM INS_GZYB_ITEMMAP WHERE EU_HPDICTTYPE=? AND CODE_HOSP=?";
		for (HpViewMatchVo itemInfo : viewMatchlist) {
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
		}
	}
}
