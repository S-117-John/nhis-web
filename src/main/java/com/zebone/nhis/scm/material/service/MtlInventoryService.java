package com.zebone.nhis.scm.material.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.platform.common.support.UserContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdInvDetail;
import com.zebone.nhis.common.module.scm.st.PdInvSingle;
import com.zebone.nhis.common.module.scm.st.PdInventory;
import com.zebone.nhis.common.module.scm.st.PdSingle;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.material.dao.MtlInventoryMapper;
import com.zebone.nhis.scm.material.vo.MtlInventoryDtVo;
import com.zebone.nhis.scm.material.vo.MtlInventoryVo;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdStockVo;
import com.zebone.nhis.scm.st.vo.InventoryDtVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.util.StringUtils;

/**
 * 盘点处理
 * @author wj
 *
 */
@Service
public class MtlInventoryService {
    @Resource
	private MtlInventoryMapper mtlInventoryMapper;
	
    /**
	 * 盘点单查询
	 * @param param{pkStore，codeInv，dateBegin，dateEnd，euStatus}
	 * @param user
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<MtlInventoryVo> queryInventory(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null){
    		map = new HashMap<String,Object>();
    	}
    	if(CommonUtils.isNotNull(map.get("dateBegin"))){
			map.put("dateBegin", CommonUtils.getString(map.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
    	map.put("pkStore", ((User)user).getPkStore());
    	return mtlInventoryMapper.queryInvenList(map);
    }
	/**
	 * 查询盘点显示明细记录
	 * @param param --pk_inv
	 * @param user
	 * @return
	 */
    public List<MtlInventoryDtVo> queryInvenDt(String param,IUser user){
    	String pk_inv = JsonUtil.readValue(param, String.class);
    	if(CommonUtils.isEmptyString(pk_inv))
    		throw new BusException("未获取到盘点单号");
    	return mtlInventoryMapper.queryInvenDtList(pk_inv);
    }
    /**
	 * 查询盘点真实明细记录
	 * @param param --pk_inv
	 * @param user
	 * @return
	 */
    public List<MtlInventoryDtVo>  queryInvenRealDt(String param,IUser user){
    	String pk_inv = JsonUtil.readValue(param, String.class);
    	if(CommonUtils.isEmptyString(pk_inv))
    		throw new BusException("未获取到盘点单号");
    	return mtlInventoryMapper.queryInvenRealDtList(pk_inv);
    }
   
    /**
     * 盘点选择-全部 | 按条件
     * @param param{flagAll,pkStore,posi,dtPdtype,pkPdcate,flagPrecious,flagSingle,flagHis}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<MtlInventoryDtVo> queryPdStockByCon(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null)
    		throw new BusException("未获取到盘点选择的参数！");
    	map.put("pkStore", ((User)user).getPkStore());
    	
    	//非全部时，添加各种条件
    	if(CommonUtils.isNull(map.get("flagAll"))){
    		if(CommonUtils.isNotNull(map.get("posi"))){
    			map.put("posis", convertStrToList(map.get("posi").toString()));
    		}
    		if(CommonUtils.isNotNull(map.get("pkPdcate"))){
    			map.put("pkPdcate", convertStrToList(map.get("pkPdcate").toString()));
    		}
    		if(CommonUtils.isNotNull(map.get("dtPdtype"))){
    			map.put("dtPdtype", convertStrToList(map.get("dtPdtype").toString()));
    		}
    		
    		StringBuilder sql = new StringBuilder("");
    		if(CommonUtils.isNotNull(map.get("flagPrecious"))){
    			if(!CommonUtils.isEmptyString(sql.toString())){
    				sql.append(" or  pd.flag_precious = '"+ map.get("flagPrecious")+"'");
    			}else{
    				sql = new StringBuilder("  pd.flag_precious = '"+ map.get("flagPrecious")+"'");
    			}
    		}
    		if(CommonUtils.isNotNull(map.get("flagSingle"))){
    			if(!CommonUtils.isEmptyString(sql.toString())){
    				sql.append(" or  pd.flag_single = '"+ map.get("flagSingle")+"'");
    			}else{
    				sql = new StringBuilder("  pd.flag_single = '"+map.get("flagSingle")+"'");
    			}
    		}
    		if(CommonUtils.isNotNull(sql)&& !CommonUtils.isEmptyString(sql.toString()))
    			map.put("flagSql", sql.toString()); 
    		
    		if(CommonUtils.isNotNull(map.get("flagHis"))){
    			map.put("flagHisSql", " pds.last_date is null or "
    					+ "to_date("+DateUtils.getDate("yyyyMMddHHmmss")+",'YYYYMMDDHH24MISS') - pds.last_date >= pds.count_per  ");
    		}
    	}

    	List<MtlInventoryDtVo> list = mtlInventoryMapper.queryPdStockByConList(map);
    	return list;
    }
   
    /**
     * 账面取数
     * @param param{pkPds,pkStore}
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<PdStockVo> getAccNum(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null )
    		throw new BusException("未获取到物品主键！");
    	map.put("pkStore", ((User)user).getPkStore());
    	return mtlInventoryMapper.queryAccNum(map);
    }
    
    /**
     * 查询物品账面数量
     * @param param
     * @param user
     * @return
     */
    public List<MtlInventoryDtVo> queryPdAccNum(String param,IUser user){
    	String pkPd = JsonUtil.readValue(param, String.class);
    	if(pkPd == null||pkPd.equals("")) throw new BusException("未获取到物品主键！");
    	return mtlInventoryMapper.queryPdAccNum(((User)user).getPkStore(),pkPd);
    }
    /**
     * 查看盘盈盘亏明细
     * @param param--pk_inv
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<MtlInventoryDtVo>  queryInvtDtByDiff(String param,IUser user){
    	Map<String,Object> map= JsonUtil.readValue(param, Map.class);
    	if(null==map)
    		throw new BusException("未获取到盘点单主键");
    	return mtlInventoryMapper.queryInvtDtByDiff(map);
    }
    
    /**
     * 保存盘点单
     * @param param
     * @param user
     */
    public MtlInventoryVo saveInv(String param,IUser user){
    	MtlInventoryVo invo = JsonUtil.readValue(param, MtlInventoryVo.class);
    	if(invo == null)  
    		throw new BusException("未获取到盘点单数据！");
    	
    	List<MtlInventoryDtVo> dtlist = invo.getDtlist();
    	PdInventory inv = new PdInventory();
    	ApplicationUtils.copyProperties(inv,invo);
    	//保存盘点单
    	if(CommonUtils.isEmptyString(inv.getPkPdinv())){
    		DataBaseHelper.insertBean(inv);
    	}else{
    		DataBaseHelper.updateBeanByPk(inv,false);
    	}
    	
    	//被删除的物品列表
    	if(invo.getDelDtList() !=null && invo.getDelDtList().size() > 0){
    		for(String pk_pd : invo.getDelDtList()){
    			//删除盘点的物品明细
    			DataBaseHelper.execute("delete from pd_inv_detail where pk_pd = ? and pk_pdinv = ? ", new Object[]{pk_pd,invo.getPkPdinv()});
    			//删除盘点单品记录
    			DataBaseHelper.execute("delete from pd_inv_single where exists (select 1 from pd_inv_detail dt "
    					+ " inner join pd_inventory inv on dt.pk_pdinv=inv.pk_pdinv "
    					+ " where pd_inv_single.pk_pdinvdt = dt.pk_pdinvdt "
    					+ " and dt.pk_pdinv = ? and dt.pk_pd = ? and inv.eu_status='0')", new Object[]{invo.getPkPdinv(),pk_pd});
    			for(int i=dtlist.size()-1;i>=0;i--){
    				MtlInventoryDtVo dt=dtlist.get(i);
    				if(dt.getPkPd().equals(pk_pd)){
    					dtlist.remove(dt);
    				}
    			}
    		}
    	}
    	
    	String pkinv = inv.getPkPdinv();
    	List<MtlInventoryDtVo> insertlist = new ArrayList<MtlInventoryDtVo>();
    	List<PdInvSingle> sinList = null;
    	for(MtlInventoryDtVo dt : dtlist){
    		//先删掉所有同一个物品的记录，然后重新保存（删除单品 +明细）
    		DataBaseHelper.execute("delete from pd_inv_single where exists (select 1 from pd_inv_detail dt "
    				+ " inner join pd_inventory inv on dt.pk_pdinv=inv.pk_pdinv "
    				+ " where pd_inv_single.pk_pdinvdt = dt.pk_pdinvdt "
    				+ " and dt.pk_pdinv = ? and dt.pk_pd = ? and inv.eu_status='0')", new Object[]{invo.getPkPdinv(),dt.getPkPd()});
    		DataBaseHelper.execute("delete from pd_inv_detail where pk_pd = ? and pk_pdinv = ? ", new Object[]{dt.getPkPd(),pkinv});
    		
    		ApplicationUtils.setBeanComProperty(dt, true);
    		dt.setPkPdinv(pkinv);
    		dt.setPkPdinvdt(NHISUUID.getKeyId());
    		insertlist.add(dt);
    		//设置单品记录
    		sinList = dt.getSinList();
    		if(null != sinList && sinList.size() > 0){
    			for (PdInvSingle pdInvSin : sinList) {
    				pdInvSin.setPkInvsingle(NHISUUID.getKeyId());
    				pdInvSin.setPkPdinvdt(dt.getPkPdinvdt());
    				pdInvSin.setPkOrg(inv.getPkOrg());
    				//DataBaseHelper.insertBean(pdInvSin);
    			}
    		}
    	}
    	//保存明细
    	if(insertlist!=null&&insertlist.size()>0){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdInvDetail.class),insertlist);
    	}
    	//保存单品记录
    	if(sinList != null && sinList.size() > 0){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdInvSingle.class),sinList);
    	}
    	invo.setPkPdinv(pkinv);
    	return invo;
    }
    
    /**
     * 删除盘点单
     * @param param
     * @param user
     */
    public void deleteInv(String param,IUser user){
    	String pk_inv = JsonUtil.readValue(param,String.class);
    	if(CommonUtils.isEmptyString(pk_inv))
    		throw new BusException("未获取到盘点单主键");
    	//删除盘点单品记录
    	String sql_sin = "delete from pd_inv_single where exists (select 1 from pd_inv_detail dt "
    			+ " inner join pd_inventory inv on dt.pk_pdinv=inv.pk_pdinv "
    			+ " where pd_inv_single.pk_pdinvdt = dt.pk_pdinvdt and dt.pk_pdinv = ? and inv.eu_status='0')";
    	DataBaseHelper.execute(sql_sin, new Object[]{pk_inv});
    	//删除盘点明细
    	String sql_dt = "delete from pd_inv_detail  where pd_inv_detail.pk_pdinv = ? and exists (select 1 "+
    			" from pd_inventory  where pd_inv_detail.pk_pdinv = pd_inventory.pk_pdinv and pd_inventory.eu_status='0')";
    	DataBaseHelper.execute(sql_dt, new Object[]{pk_inv});
    	//删除盘点单
    	String sql = "delete from pd_inventory  where pk_pdinv = ? and eu_status='0'";
    	DataBaseHelper.execute(sql, new Object[]{pk_inv});
    }
    
    /**
     * 盘点审核
     * @param param
     * @param user
     */
    public void submitInv(String param,IUser user){
    	String pk_inv = JsonUtil.readValue(param, String.class);
    	if(CommonUtils.isEmptyString(pk_inv))
    		throw new BusException("未获取到盘点单号");
    	User u = (User)user;
    	//1） 盘盈物品生成盘盈入库单
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("flag", "1");
    	map.put("pkPdinv", pk_inv);
    	List<MtlInventoryDtVo> indt = mtlInventoryMapper.queryInvtDtByDiff(map);
    	if(indt != null && indt.size() > 0){
    		PdSt st = this.createPdst(u, IDictCodeConst.DT_STTYPE_INVLOSSIN, "1");
    		insertSt(st,indt,u);
    	}
    	//2） 盘亏物品生成盘亏出库单；
    	map.put("flag", "2");
    	List<MtlInventoryDtVo> outdt = mtlInventoryMapper.queryInvtDtByDiff(map);
    	if(outdt != null && outdt.size() >0){
    		PdSt st = this.createPdst(u, IDictCodeConst.DT_STTYPE_INVLOSSOUT, "-1");
    		insertSt(st,outdt,u);
    	}
    	//更新盘点单状态
    	Map<String,Object> map1 = new HashMap<String,Object>();
    	map1.put("pkPdinv", pk_inv);
    	map1.put("pkEmp", u.getPkEmp());
    	map1.put("nameEmp", u.getNameEmp());
    	map1.put("dateChk", new Date());
    	String update = "update pd_inventory set pk_emp_chk = :pkEmp,name_emp_chk = :nameEmp,"+
    			" date_chk = :dateChk,eu_status='1',ts=:dateChk  where pk_pdinv = :pkPdinv ";
    	DataBaseHelper.update(update, map1);
    	map1.put("pkStore", ((User)user).getPkStore());
    	//更新仓库物品的最后盘点时间
    	String update_pd = " update bd_pd_store set last_date = :dateChk where pk_store = :pkStore  and pk_pd in (select pk_pd from pd_inv_detail where pk_pdinv = :pkPdinv )";
    	DataBaseHelper.update(update_pd, map1);
    }

    //字符串转数组
    private List<String> convertStrToList(String param){
    	if(CommonUtils.isEmptyString(param)) return null;
    	String[] arr = param.split(",");
    	if(arr!=null&&arr.length>0){
    		return  Arrays.asList(arr);
    	}
    	return null;
    }
    
    /**
     * 插入出入库单及明细
     * @param st
     * @param dtlist
     * 
     * @param u
     */
    private void insertSt(PdSt st,List<MtlInventoryDtVo> dtlist,User u){
    	DataBaseHelper.insertBean(st);
    	List<PdStDetail> insert_list = new ArrayList<PdStDetail>();
		int i = 1;
		
		for(MtlInventoryDtVo dt:dtlist){
			insert_list.add(this.createPdstdt(dt, u, st.getPkPdst(), i));
			i++;
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class),insert_list);
    }
    
    /**
	 * 构建出入库单
	 * @param
	 * @param u
	 * @return
	 */
	private PdSt createPdst(User u,String dttype,String eudirect){
		PdSt st = new PdSt();
		st.setCodeSt(ScmPubUtils.getInStoreCode());
		st.setDateChk(new Date());
		st.setDateSt(new Date());
		st.setDelFlag("0");
		st.setDtSttype(dttype);
		st.setEuDirect(eudirect);
		st.setEuStatus("0");
		st.setFlagChk("0");
		st.setFlagPay("0");
		st.setFlagPu("0");
		st.setNameEmpChk(u.getNameEmp());
		st.setNameEmpOp(u.getNameEmp());
		st.setNameSt(u.getNameEmp());
		st.setPkDeptSt(u.getPkDept());
		st.setPkEmpChk(u.getPkEmp());
		st.setPkEmpOp(u.getPkEmp());
		st.setPkOrg(u.getPkOrg());
		st.setPkStoreSt(u.getPkStore());
		return st;
	}
	
	/**
	 * 构建出入库明细
	 * @param dt
	 * @param u
	 * @return
	 */
	private PdStDetail createPdstdt(MtlInventoryDtVo dt,User u,String pk_pdst,int i){
		PdStDetail pddt = new PdStDetail();
		pddt.setBatchNo(dt.getBatchNo());
		pddt.setDateExpire(dt.getDateExpire());
		pddt.setDateFac(dt.getDateFac());
		pddt.setDelFlag("0");
		pddt.setDisc(1.00);
		pddt.setFlagChkRpt("0");
		pddt.setFlagFinish("0");
		pddt.setFlagPay("0");
		pddt.setQuanOutstore(0.00);
		pddt.setPackSize(dt.getPackSize());
		pddt.setPkOrg(u.getPkOrg());
		pddt.setPkPd(dt.getPkPd());
		pddt.setPkPdstdt(NHISUUID.getKeyId());
		pddt.setPkPdst(pk_pdst);
		pddt.setPkUnitPack(dt.getPkUnitPack());
		pddt.setPrice(dt.getPrice());
		pddt.setPriceCost(dt.getPriceCost());
		pddt.setQuanPack(MathUtils.abs(dt.getQuanDiff()));
        pddt.setQuanMin(MathUtils.mul(pddt.getQuanPack(), CommonUtils.getDouble(dt.getPackSize())));
        pddt.setSortNo(i);
        pddt.setAmount(MathUtils.div(pddt.getQuanMin(), CommonUtils.getDouble(dt.getPackSizePd()))*pddt.getPrice());
		pddt.setAmountCost(MathUtils.div(pddt.getQuanMin(), CommonUtils.getDouble(dt.getPackSizePd()))*pddt.getPriceCost());
		ApplicationUtils.setBeanComProperty(pddt, true);
		updatePdSingle(dt,u,pddt.getPkPdstdt());//更新|插入物品单品表
		return pddt;
	}
	
	/**
	 * 更新单品记录表
	 * 1、盘盈 ： 插入记录，pk_pdstdt_in = 入库单明细主键
	 * 2、盘亏：更新 pk_pdstdt_out = 出库单明细主键
	 * @param dt
	 * @param u
	 * @param pk_pdstdt
	 */
	private void updatePdSingle(MtlInventoryDtVo dt,User u,String pk_pdstdt){
		List<PdInvSingle> sinList = DataBaseHelper.queryForList("select * from pd_inv_single "
				+ "where pk_pd = ? and pk_pdinvdt = ? ", PdInvSingle.class, new Object[]{dt.getPkPd(),dt.getPkPdinvdt()});
		if(null == sinList || sinList.size() < 1) return;
		PdSingle pdSin = null;
		String upOut = "update pd_single set pk_pdstdt_out = ? where pk_pd = ? and barcode = ? and eu_status = '0' ";
		for (PdInvSingle pdS : sinList) {
			if("1".equals(pdS.getEuStatus())){
				//盘盈插入单品记录
				pdSin = new PdSingle();
				pdSin.setPkOrg(u.getPkOrg());
				pdSin.setPkDept(u.getPkDept());
				pdSin.setPkStore(u.getPkStore());
				pdSin.setPkPd(pdS.getPkPd());
				pdSin.setBarcode(pdS.getBarcode());
				pdSin.setEuStatus("0");//盘盈做登记操作
				pdSin.setPkPdstdtIn(pk_pdstdt);//设置入库单明细主键
				DataBaseHelper.insertBean(pdSin);
			}
			else if("2".equals(pdS.getEuStatus()))//盘亏更新出库明细主键
				DataBaseHelper.update(upOut, new Object[]{pk_pdstdt,dt.getPkPd(),pdS.getBarcode()});
		}
	}

	
	/**
	 * 查询单品记录
	 * @param param --pk_pd,pk_store
	 * @param user
	 * @return
	 */
    public List<Map<String, Object>> queryPdSin(String param,IUser user){
    	//兼容处理
		Map<String,Object> map = null;
		try {
			map = JsonUtil.readValue(param,Map.class);
		} catch (Exception e){
			map = new HashMap<>();
			String pkPd = JsonUtil.readValue(param, String.class);
			map.put("pkPd", pkPd);
		}
		if(StringUtils.isEmpty(MapUtils.getString(map,"pkStore"))){
			map.put("pkStore", ((User)user).getPkStore());
		}
    	List<Map<String, Object>> list = mtlInventoryMapper.queryPdSin(map);
    	return list;
    }
}
