package com.zebone.nhis.pro.zsba.mz.pub.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.dao.CgQryMaintainMapper;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BillItemVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdHpItemdiv;
import com.zebone.nhis.common.module.base.bd.price.BdPayer;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.acc.PiAccShare;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class ZsbaCgQryMaintainService {

	@Resource
	private CgQryMaintainMapper cgQryMaintainMapper;

	/**
	 * 根据医嘱主键查询收费项目信息及其数量
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryPkItemsByPkOrd(Map<String, Object> mapParam) throws BusException {

		String sql = "select item.*,orditem.quan from BD_ITEM item inner join BD_ORD_ITEM orditem on item.pk_item=orditem.pk_item where orditem.pk_ord=? and item.flag_active='1' and item.del_flag !='1' and orditem.del_flag!='1'";
		List<Map<String, Object>> bdItems = DataBaseHelper.queryForList(sql, new Object[] { mapParam.get("pkOrd") });
		return bdItems;
	}

	/**
	 * 根据药品主键pkpd查询药品相关信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryPdInfoByPkPd(Map<String, Object> mapParam) throws BusException {

		String sql = "select pd.pk_pd pk_item,1 as quan,pd.name,pd.spec from  bd_pd pd where pd.pk_pd=? ";
		List<Map<String, Object>> bdPds = DataBaseHelper.queryForList(sql, new Object[] { mapParam.get("pkOrd") });
		return bdPds;
	}

	/**
	 * 根据患者主键查询患者基本信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public PiMaster qryPiMasterByPk(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from pi_master where pk_pi=? ";
		PiMaster piInfo = DataBaseHelper.queryForBean(sql, PiMaster.class, new Object[] { mapParam.get("pkPi") });
		return piInfo;
	}

	/**
	 * 根据排班服务主键查询查询医嘱项目主键
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<String> qrySchSrvOrdsByPkSchsrv(Map<String, Object> mapParam) throws BusException {

		String sql = "select bdorditem.pk_item from sch_srv_ord schsrvord inner join bd_ord_item bdorditem on schsrvord.pk_ord=bdorditem.pk_ord where schsrvord.pk_schsrv=?  and bdorditem.del_flag='0' and schsrvord.del_flag='0'";
		List<Map<String, Object>> pkItemListMaps = DataBaseHelper.queryForList(sql, new Object[] { mapParam.get("pkSchsrv") });
		List<String> pkItems = new ArrayList<String>();
		for (Map<String, Object> map : pkItemListMaps) {
			if (map.get("pkItem") != null)
				pkItems.add(map.get("pkItem").toString());
		}
		return pkItems;
	}

	/**
	 * 根据患者主键查询患者账户信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public PiAcc qryPiAccByPkPi(String pkPi) throws BusException {
		String sql = "select * from pi_acc where pk_pi = ? and (del_flag = '0' or del_flag is null)";
		PiAcc piAcc = DataBaseHelper.queryForBean(sql, PiAcc.class, pkPi);
		return piAcc;
	}
	
	/**
	 * 根据患者主键查询患者父账户信息
	 * @param pkPi
	 * @return
	 * @throws BusException
	 */
	public PiAccShare qryParentPiAccByPkPi(String pkPi) throws BusException{
		
		// 查询患者存在的账户共享关系 --增加 and del_flag='0'查询表中存在的授权关系
		String sql = "select * from pi_acc_share where pk_pi_use = ? and (del_flag = '0' or del_flag is null)";
		PiAccShare piAccShare = DataBaseHelper.queryForBean(sql, PiAccShare.class, pkPi);
		return piAccShare;
	}
	

	/**
	 * 根据付款方类型查询付款方主键
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public BdPayer qryBdPayerByEuType(Map<String, Object> mapParam) throws BusException {

		//String sql = "select * from bd_payer where eu_type='0' and pk_org=?";
		String sql = "select * from bd_payer where eu_type='0' and code='01' and pk_org=?";//博爱默认全自费取付款方-个人
		BdPayer bdPayer = DataBaseHelper.queryForBean(sql, BdPayer.class, new Object[] { mapParam.get("pkOrg") });
		return bdPayer;
	}

	/**
	 * 根据排班服务主键查询排班服务信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qrySchSrvByPkSchsrv(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from sch_srv where pk_schsrv=? ";
		Map<String, Object> mapTemp = DataBaseHelper.queryForMap(sql, new Object[] { mapParam.get("pkSchsrv") });
		return mapTemp;
	}

	/**
	 * 根据pk_pv查询发票明细信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<BlInvoiceDt> qryInfoForBlInvoiceDt(Map<String, Object> mapParam) throws BusException {

		StringBuilder sql = new StringBuilder();
		Object pkBlOpDtInSql = mapParam.get("pkBlOpDtInSql");
		sql.append("select invitem.pk_invcateitem pk_bill,invitem.code code_bill,invitem.name name_bill, sum(bl.amount) amount from bd_invcate_itemcate invitemcate");
		sql.append(" inner join bd_invcate_item invitem on invitemcate.pk_invcateitem=invitem.pk_invcateitem inner join bd_invcate inv on invitem.pk_invcate=inv.pk_invcate");
		sql.append(" inner join bl_op_dt bl on invitemcate.pk_itemcate=bl.pk_itemcate where inv.pk_org = ?  and inv.eu_type = '0' and bl.pk_pv = ? ");
		if (pkBlOpDtInSql != null) {
			sql.append(" and bl.pk_cgop in (" + pkBlOpDtInSql.toString() + ")");
		} else {// 挂号
			sql.append(" and  bl.flag_pv = 1");
		}
		sql.append(" and invitemcate.del_flag='0' and invitem.del_flag='0' and inv.del_flag='0' and bl.del_flag='0'");
		sql.append(" group by invitem.pk_invcateitem, invitem.code, invitem.name");
		List<BlInvoiceDt> blInvoiceDts = DataBaseHelper.queryForList(sql.toString(), BlInvoiceDt.class,
				new Object[] { mapParam.get("pkOrg"), mapParam.get("pkPv") });
		return blInvoiceDts;
	}

	/**
	 * 根据患者的就诊pkPv查询所有的医保计划主键
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<PvInsurance> qryPkHpByPkPv(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from pv_insurance  where pk_pv=? order by SORT_NO ASC,FLAG_MAJ DESC ";
		List<PvInsurance> listTemp = DataBaseHelper.queryForList(sql, PvInsurance.class, new Object[] { mapParam.get("pkPv") });
		return listTemp;
	}

	/**
	 * 根据pk_pv查询患者就诊信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public PvEncounter qryPvEncounterInfo(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from pv_encounter where pk_pv=?  and (del_flag = '0' or del_flag is null) ";
		PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql, PvEncounter.class, new Object[] { mapParam.get("pkPv") });
		return pvEncounter;
	}

	/**
	 * 根据pk_pv查询患者优惠比率
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public PiCate qryPiCateInfo(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from pi_cate where pk_picate=?  and (del_flag = '0' or del_flag is null) ";
		PiCate piCate = DataBaseHelper.queryForBean(sql, PiCate.class, new Object[] { mapParam.get("pkPicate") });
		return piCate;
	}
	/**
	 * 根据pk_pv查询患者优惠比率
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public BdHp qryBdHpByPiCate(Map<String, Object> mapParam) throws BusException {
		return cgQryMaintainMapper.qryBdHpByPiCate(mapParam);
	}
	/**
	 * 根据医保计划主键查询医保信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public BdHp qryBdHpInfo(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from bd_hp where pk_hp=? ";
		BdHp bdHp = DataBaseHelper.queryForBean(sql, BdHp.class, new Object[] { mapParam.get("pkHp") });
		return bdHp;
	}
	/**
	 * 根据医保计划主键批量查询医保信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<BdHp> qryBdHpInfoList(List<String> pkHps,String euPvType) throws BusException {
        if(pkHps==null||pkHps.size()<=0) 
        	return null;
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("pkHps", pkHps);
        paramMap.put("euPvType", euPvType);
		return cgQryMaintainMapper.qryBdHpInfoList(paramMap);
	}
	/**
	 * 根据就诊信息医保信息
	 * @param mapParam{pkPv,euPvType}
	 * @return
	 * @throws BusException
	 */
	public List<BdHp> qryBdHpInfoList(Map<String,Object> paramMap) throws BusException {
		return cgQryMaintainMapper.qryBdHpInfoListByPv(paramMap);
	}

	/**
	 * 查询收费项目对应的子项目主键和数量
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryPkItemChild(Map<String, Object> mapParam) throws BusException {

		String sql = "select pk_item_child,quan from bd_item_set where pk_item=?  and (del_flag = '0' or del_flag is null) ";
		List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sql, new Object[] { mapParam.get("pkItem") });
		return mapList;
	}

	/**
	 * 根据bd_item的主键查询信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryBdItemByPk(Map<String, Object> mapParam) throws BusException {
		String sql = "select item.* from BD_ITEM item where pk_item=? ";
		if(CommonUtils.isNotNull(mapParam.get("flagActive")))
			sql = sql + "and item.flag_active='"+mapParam.get("flagActive")+"'";
		if(CommonUtils.isNotNull(mapParam.get("delFlag")))
			sql = sql + "and item.del_flag='"+mapParam.get("delFlag")+"'";
		Map<String, Object> bdItem = DataBaseHelper.queryForMap(sql, new Object[] {mapParam.get("pkItem")});
		return bdItem;
	}

	/**
	 * 根据pkPd的主键查询信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryBdPdyPk(Map<String, Object> mapParam) throws BusException {

		String sql = "select item.* from BD_PD item where pk_pd=? and item.del_flag!='1'";
		Map<String, Object> bdItem = DataBaseHelper.queryForMap(sql, new Object[] { mapParam.get("pkItem") });
		return bdItem;
	}

//	/**
//	 * 获取本服务定价下的价格
//	 * @param mapParam
//	 * @return
//	 * @throws BusException
//	 */
//	public Map<String, Object> qryThisServicePrice(Map<String, Object> mapParam) throws BusException {
//
//		StringBuffer sql = new StringBuffer();
//		sql.append(" select price.pk_item,item.eu_pricemode,(price.price*boi.quan ) as price,item.flag_set from bd_item item inner join bd_ord_item boi on item.pk_item = boi.pk_item  inner join bd_item_price price on item.pk_item=price.pk_item ");
//		sql.append(" and price.pk_org = ? inner join bd_pricetype_cfg cfg on price.eu_pricetype=cfg.eu_pricetype and cfg.pk_hp=? and cfg.pk_org=? where item.pk_item = ? and ");
//		sql.append(" price.date_begin <= ? and price.date_end > ? and  price.flag_stop = 0 and  nvl(item.del_flag,'0') = '0' and nvl(price.del_flag,'0') = '0' and nvl(cfg.del_flag,'0') = '0' ");
//
//		Map<String, Object> mapResult = DataBaseHelper.queryForMap(
//				sql.toString(),
//				new Object[] { mapParam.get("pkOrg"), mapParam.get("pkHp"), mapParam.get("pkOrg"), mapParam.get("pkItem"), mapParam.get("dateHap"),
//						mapParam.get("dateHap") });
//		return mapResult;
//	}
	
	
	/**
	 * 获取本服务定价下的价格
	 * 2018-02-24 修改本服务定价下面的价格，该方法用于记费项目计费，不需要关联医嘱-记费项目表查询相关价格，使用时请注意
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryThisServicePrice(Map<String, Object> mapParam) throws BusException {

		StringBuffer sql = new StringBuffer(); 
//		sql.append(" select price.pk_item,item.eu_pricemode,(price.price*boi.quan) as price,item.flag_set from bd_item item   inner join bd_item_price price on item.pk_item=price.pk_item ");
//		sql.append(" and price.pk_org = ? inner join bd_ord_item boi on item.pk_item = boi.pk_item  inner join bd_pricetype_cfg cfg on price.eu_pricetype=cfg.eu_pricetype and cfg.pk_hp=? and cfg.pk_org=? where item.pk_item = ? and ");
		sql.append(" select price.pk_item,item.eu_pricemode,price.price,item.flag_set from bd_item item   inner join bd_item_price price on item.pk_item=price.pk_item ");
		sql.append(" and price.pk_org = ? inner join bd_pricetype_cfg cfg on price.eu_pricetype=cfg.eu_pricetype and cfg.pk_hp=? and cfg.pk_org=? where item.pk_item = ? and ");
		sql.append(" price.date_begin <= ? and price.date_end > ? and  price.flag_stop = '0' and  item.del_flag!= '1' and price.del_flag!= '1' and  cfg.del_flag!= '1' ");

		Map<String, Object> mapResult = DataBaseHelper.queryForMap(
				sql.toString(),
				new Object[] { mapParam.get("pkOrg"), mapParam.get("pkHp"), mapParam.get("pkOrg"), mapParam.get("pkItem"), mapParam.get("dateHap"),
						mapParam.get("dateHap") });
		return mapResult;
	}

	/**
	 * 获取本服务定价组套下子项目价格
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Map<String, Object>> qryThisServiceChildPrice(Map<String, Object> mapParam) throws BusException {

		StringBuffer sql = new StringBuffer();

		sql.append("select price.pk_item key_,price.pk_item,item.eu_pricemode,price.price,item.flag_set from bd_item item inner join bd_item_price price on item.pk_item = price.pk_item ");
		sql.append(" and price.pk_org = ? inner join bd_pricetype_cfg cfg on price.eu_pricetype=cfg.eu_pricetype and cfg.pk_hp=? where exists( ");
		sql.append(" select itemset.pk_item from bd_item_set itemset where itemset.pk_item=? and itemset.pk_item_child = item.pk_item   and itemset.del_flag != '1'  and   ");
		sql.append(" price.date_begin <= to_date(?,'YYYYMMDDHH24MISS') and price.date_end > to_date(?,'YYYYMMDDHH24MISS')  and   price.flag_stop = 0  and cfg.del_flag != '1'  and price.del_flag!= '1' and item.del_flag!= '1')  ");
        String dateHap = DateUtils.getDefaultDateFormat().format(mapParam.get("dateHap"));
		Map<String, Map<String, Object>> mapResult = DataBaseHelper.queryListToMap(sql.toString(), mapParam.get("pkOrg"), mapParam.get("pkHp"),
				mapParam.get("pkItem"),dateHap , dateHap);
		return mapResult;
	}

	/**
	 * 查询患者的医保类型
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public String qryEuHpType(Map<String, Object> mapParam) throws BusException {

		// 1门诊，2急诊，3住院，4体检，5家庭病床
		StringBuffer sql = new StringBuffer("select eu_hptype from bd_hp where  pk_hp = ? and (del_flag = '0' or del_flag is null) ");
		String euPvtype = mapParam.get("euPvtype").toString();
		String exceptionMes = "";
		switch (euPvtype) {
		case "1":
			sql.append(" and flag_op='1'");
			exceptionMes = "门诊";
			break;
		case "2":
			sql.append(" and flag_er='1'");
			exceptionMes = "急诊";
			break;
		case "3":
			sql.append(" and flag_ip='1'");
			exceptionMes = "住院";
			break;
		case "4":
			sql.append(" and flag_pe='1'");
			exceptionMes = "体检";
			break;
		case "5":
			sql.append(" and flag_hm='1'");
			exceptionMes = "家庭病床";
			break;
		}
		
		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(), new Object[] { mapParam.get("pkHp") });
		if (mapResult == null) {
			throw new BusException("未查询到符合条件的医保信息，请核对医保属性是否设置正确！");
		}
		if (mapResult.get("euHptype") == null) {
			throw new BusException("就诊类型：" + exceptionMes + "对应的医保计划类型未维护");
		}
		return mapResult.get("euHptype").toString();
	}

	/**
	 * 查询内部医保的项目分摊定义
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryProjectShareDefine(Map<String, Object> mapParam) throws BusException {

		String sql = "select item.pk_item, item.eu_divide,item.rate, item.amount from bd_hp_itemdiv item where   "
				+ "item.pk_hp = ? and item.flag_pd = ? and  item.pk_item = ? and item.date_begin <= to_date(?,'YYYYMMDDHH24MISS') and item.date_end > to_date(?,'YYYYMMDDHH24MISS') and item.del_flag!= '1' ";
		String dateHap = DateUtils.getDefaultDateFormat().format(mapParam.get("dateHap"));
		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(),
				new Object[] { mapParam.get("pkHp"), mapParam.get("flagPd"), mapParam.get("pkItem"),dateHap,dateHap });
		return mapResult;
	}
	/**
	 * 查询内部医保的项目分摊定义
	 * @param pkHp 
	 * @return
	 * @throws BusException
	 */
	public List<BdHpItemdiv> qryHpItemDivDefine(String pkHp,String dateHap) throws BusException {
        if(pkHp == null||pkHp.equals("")) return null;
		return cgQryMaintainMapper.queryHpItemDivList(pkHp, dateHap);
	}

	/**
	 * 查询内部医保的项目分类分摊定义
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryProjectClassificationShareDefine(Map<String, Object> mapParam) throws BusException {

		StringBuffer sql = new StringBuffer("select cate.pk_itemcate, cate.rate from bd_hp_itemcatediv cate");
		// 药品
		if (BlcgUtil.converToTrueOrFalse(mapParam.get("flagPd").toString())) {
			sql.append(" inner join bd_pd pd on cate.pk_itemcate=pd.pk_itemcate ");
			sql.append(" where pd.pk_pd = '"+mapParam.get("pkPd")+"' and pd.del_flag='0'");
		}
		// 非药品
		else {
			sql.append(" inner join bd_item item on cate.pk_itemcate=item.pk_itemcate ");
			sql.append(" where item.pk_item = '"+mapParam.get("pkItem")+"' and item.del_flag = '0' ");
		}
		sql.append(" and cate.pk_hp = ?  and cate.date_begin <= to_date(?,'YYYYMMDDHH24MISS') and cate.date_end > to_date(?,'YYYYMMDDHH24MISS') and cate.del_flag='0' ");
		String dateHap = DateUtils.getDefaultDateFormat().format(mapParam.get("dateHap"));
		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(),new Object[] { mapParam.get("pkHp"),dateHap, dateHap});
		return mapResult;
	}

	/**
	 * 查询内部医保总额分摊定义信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryHealthTotalShareDefine(Map<String, Object> mapParam) throws BusException {

		String sql = "select total.eu_divide, total.rate, total.amount  from bd_hp_totaldiv total  where  total.pk_hp = ? and  total.date_begin <= to_date(?,'YYYYMMDDHH24MISS') and  total.date_end > to_date(?,'YYYYMMDDHH24MISS') and total.del_flag!= '1'";
		String dateCur = DateUtils.getDefaultDateFormat().format(mapParam.get("dateCur"));
		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(),
				new Object[] { mapParam.get("pkHp"), dateCur, dateCur});
		return mapResult;
	}

	/**
	 * 查询内部医保支付段分摊定义信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryPaymentPeriodShareDefine(Map<String, Object> mapParam) throws BusException {

		String sql = "select sec.amt_min,sec.amt_max,sec.eu_divide,sec.rate, sec.amount from bd_hp_secdiv sec where sec.pk_hp = ?  and nvl(sec.del_flag,'0') = '0' order by sec.amt_min,sec.amt_max ";
		List<Map<String, Object>> mapResultList = DataBaseHelper.queryForList(sql.toString(), new Object[] { mapParam.get("pkHp") });
		return mapResultList;
	}

	/**
	 * 根据收费项目主键查询账单码
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryBillCodeByPkItem(Map<String, Object> mapParam) throws BusException {

		StringBuffer sql = new StringBuffer();
		sql.append("select distinct invitem.code from bd_invcate_item invitem");
		sql.append(" inner join bd_invcate_itemcate cate on invitem.pk_invcateitem=cate.pk_invcateitem");
		sql.append(" inner join bd_invcate inv on invitem.pk_invcate=inv.pk_invcate ");
		if (BlcgUtil.converToTrueOrFalse(mapParam.get("flagPd").toString())) {
			sql.append(" inner join bd_pd item on cate.pk_itemcate=item.pk_itemcate");
			sql.append(" where item.pk_pd = '"+mapParam.get("pkItem")+"' ");
		} else {
			sql.append(" inner join bd_item item on cate.pk_itemcate=item.pk_itemcate");
			sql.append(" where item.pk_item = '"+mapParam.get("pkItem")+"' ");
		}
		sql.append(" and inv.eu_type = ? and invitem.del_flag ='0' and invitem.pk_org=? and cate.del_flag ='0' and inv.del_flag='0' and item.del_flag='0' ");
		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(),
				new Object[] {mapParam.get("euType"), mapParam.get("pkOrg") });
		return mapResult;
	}
	/**
	 * 根据多个收费项目主键查询账单码
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<BillItemVo> qryBillAndAccountCodeByPkItems(Map<String, Object> paramMap) throws BusException {
		List<BillItemVo> bills = cgQryMaintainMapper.qryBillCodeByPkItems(paramMap);
		return bills;
	}
	/**
	 * 根据收费项目主键查询账单码
	 * @param paramMap{pkItemcates(费用分类主键集合),euType,pkOrg}
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryBillCodeByItemCate(Map<String,Object> paramMap) throws BusException {
      return cgQryMaintainMapper.queryInvItemByItemCate(paramMap);
	}

	/**
	 * 根据收费项目主键查询核算码
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryAccountCodeByPkItem(Map<String, Object> mapParam) throws BusException {

		StringBuffer sql = new StringBuffer();
		sql.append("select distinct bdaudit.code  from bd_audit bdaudit");
		sql.append(" inner join bd_audit_itemcate auditcateitem on bdaudit.pk_audit = auditcateitem.pk_audit");
		sql.append(" inner join bd_itemcate itemcate on itemcate.pk_itemcate = auditcateitem.pk_itemcate ");
		if (BlcgUtil.converToTrueOrFalse(mapParam.get("flagPd").toString())) {
			sql.append(" inner join bd_pd item on itemcate.pk_itemcate=item.pk_itemcate");
			sql.append(" where item.pk_pd = ? ");
		} else {
			sql.append(" inner join bd_item item on itemcate.pk_itemcate=item.pk_itemcate");
			sql.append(" where item.pk_item = ? ");
		}
		sql.append(" and bdaudit.del_flag ='0'  and auditcateitem.del_flag ='0' and itemcate.del_flag ='0' and item.del_flag='0' ");
		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(), new Object[] { mapParam.get("pkItem") });
		//根据收费项目分类查询财务核算码为空时，根据收费项目或物品中维护的财务核算码查询
		if(mapResult==null)
			mapResult = this.qryAuditCodeByPkItem(mapParam);
		return mapResult;
	}
	/**
	 * 根据收费项目主键查询财务核算码,直接查询方式
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryAuditCodeByPkItem(Map<String, Object> mapParam) throws BusException {

		StringBuffer sql = new StringBuffer();
		sql.append("select distinct bdaudit.code  from bd_audit bdaudit");
		if (BlcgUtil.converToTrueOrFalse(mapParam.get("flagPd").toString())) {
			sql.append(" inner join bd_pd item on bdaudit.pk_audit = item.pk_audit");
			sql.append(" where item.pk_pd = ? ");
		} else {
			sql.append(" inner join bd_item item on bdaudit.pk_audit = item.pk_audit");
			sql.append(" where item.pk_item = ? ");
		}
		sql.append(" and bdaudit.del_flag ='0'  and item.del_flag='0' ");
		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(), new Object[] { mapParam.get("pkItem") });
		return mapResult;
	}
	/**
	 * 根据费用分类主键查询核算码
	 * @param paramMap{pkItemcates(费用分类主键集合),euType,pkOrg}
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryAuditCodeByItemcate(Map<String, Object> mapParam) throws BusException {
		return cgQryMaintainMapper.queryAuditItemByItemCate(mapParam);
	}
	/**
	 * 根据pkPv查询挂号费用信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<BlOpDt> qryRegCostInfoByPkpv(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from bl_op_dt where pk_pv=? and flag_pv='1'  and quan > 0";
		if(mapParam != null && mapParam.get("haveSettle")!=null) {
			sql += " and flag_settle='1' ";
		}
		List<BlOpDt> blOpDts = DataBaseHelper.queryForList(sql, BlOpDt.class, new Object[] { mapParam.get("pkPv") });
		return blOpDts;
	}

	/**
	 * 根据结算主键患者的交款记录信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<BlDeposit> qryRecordCashierByPkSettle(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from bl_deposit where pk_settle=?";
		List<BlDeposit> blDeposits = DataBaseHelper.queryForList(sql, BlDeposit.class, new Object[] { mapParam.get("pkSettle") });
		return blDeposits;
	}

	
	/**
	 * 根据用户和就诊主键查询交款记录
	 * @param mapParam
	 * @return
	 */
	public List<BlDeposit> qryBldepositByPk(Map<String, Object> mapParam){
		String sql = "select * from BL_DEPOSIT where PK_PI=? and PK_PV=? and EU_DIRECT=? and DT_PAYMODE=?";
		List<BlDeposit> blists = DataBaseHelper.queryForList(sql, BlDeposit.class, 
				new Object[]{mapParam.get("pkPi"),mapParam.get("pkPv"),mapParam.get("euDirect"),mapParam.get("dtPaymode")});
		return blists;
	}
	
	
	/**
	 * 根据pkPv查询患者的挂号结算信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public BlSettle qryBlSettleInfoByPkpv(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from bl_settle where pk_settle=? ";
		BlSettle blSettle = DataBaseHelper.queryForBean(sql, BlSettle.class, new Object[] { mapParam.get("pkSettle") });
		return blSettle;
	}

	/**
	 * 根据结算主键查询结算明细信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<BlSettleDetail> qryBlSettleDetailInfoByBlSettle(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from bl_settle_detail  where pk_settle=? ";
		List<BlSettleDetail> blSettleDetails = DataBaseHelper.queryForList(sql, BlSettleDetail.class, new Object[] { mapParam.get("pkSettle") });
		return blSettleDetails;
	}

	/**
	 * 根据结算主键查询结算对应的发票信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<BlInvoice> qryBlInvoiceInfoByBlSettle(Map<String, Object> mapParam) throws BusException {

		String sql = "select bi.* from bl_invoice bi inner join bl_st_inv bsi on bsi.pk_invoice=bi.pk_invoice where bsi.pk_settle=? ";
		List<BlInvoice> blInvoice = DataBaseHelper.queryForList(sql, BlInvoice.class, new Object[] { mapParam.get("pkSettle") });
		return blInvoice;
	}

	/**
	 * 查询住院费用明细
	 * @param paramMap
	 *            {euPvtype，codepi，namepi，pkDeptCg，pkEmpCg，dateBegin，dateEnd}
	 * @return
	 */
	public List<Map<String, Object>> queryBlCgIpDetails(Map<String, Object> paramMap) {

		return cgQryMaintainMapper.queryBlCgIpDetails(paramMap);
	}
	

}
