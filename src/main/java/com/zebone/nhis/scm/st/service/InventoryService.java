package com.zebone.nhis.scm.st.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdInvDetail;
import com.zebone.nhis.common.module.scm.st.PdInventory;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdStockVo;
import com.zebone.nhis.scm.st.dao.InventoryMapper;
import com.zebone.nhis.scm.st.vo.InventoryDtVo;
import com.zebone.nhis.scm.st.vo.InventoryVo;
import com.zebone.nhis.scm.st.vo.PdInventoryVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 盘点处理
 * @author yangxue
 *
 */
@Service
public class InventoryService {
    @Resource
	private InventoryMapper inventoryMapper;
	/**
	 * 盘点单查询
	 * @param param{pkStore，codeInv，dateBegin，dateEnd，euStatus}
	 * @param user
	 * @return
	 */
    public List<InventoryVo> queryInventory(String param,IUser user){
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
    	return inventoryMapper.queryInvenList(map);
    }
	/**
	 * 查询盘点显示明细记录
	 * @param param --pk_inv
	 * @param user
	 * @return
	 */
    public List<InventoryDtVo> queryInvenDt(String param,IUser user){
    	String pk_inv = JsonUtil.readValue(param, String.class);
    	if(CommonUtils.isEmptyString(pk_inv))
    		throw new BusException("未获取到盘点单号");
    	return inventoryMapper.queryInvenDtList(pk_inv);
    }
    /**
	 * 查询盘点真实明细记录
	 * @param param --pk_inv
	 * @param user
	 * @return
	 */
    public List<InventoryDtVo>  queryInvenRealDt(String param,IUser user){
    	String pk_inv = JsonUtil.readValue(param, String.class);
    	if(CommonUtils.isEmptyString(pk_inv))
    		throw new BusException("未获取到盘点单号");
    	return inventoryMapper.queryInvenRealDtList(pk_inv);
    }
    /**
     * 查询当前仓库物品全部库存---盘点选择（全部物品）
     * @param param
     * @param user
     * @return
     */
    public List<InventoryDtVo> queryAllPdStock(String param,IUser user){
    	List<InventoryDtVo> resList=inventoryMapper.queryAllPdStockList(((User)user).getPkStore());
    	return resList;
    }
    /**
     * 盘点选择-按条件
     * @param param{pkStore,posis,drugtypes,dtpois,usecates,antis,flagPrecious,flagVacc,flagRm,flagReag,flagHis}
     * @param user
     * @return
     */
    public List<InventoryDtVo> queryPdStockByCon(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null){
    		map = new HashMap<String,Object>();
    	}
    	if(CommonUtils.isNotNull(map.get("posis"))){
    		map.put("posis", convertStrToList(map.get("posis").toString()));
    	}
    	if(CommonUtils.isNotNull(map.get("drugtypes"))){
    		map.put("drugtypes", convertStrToList(map.get("drugtypes").toString()));
    	}
    	if(CommonUtils.isNotNull(map.get("dtpois"))){
    		map.put("dtpois", convertStrToList(map.get("dtpois").toString()));
    	}
    	if(CommonUtils.isNotNull(map.get("usecates"))){
    		map.put("usecates", convertStrToList(map.get("usecates").toString()));
    	}
    	if(CommonUtils.isNotNull(map.get("antis"))){
    		map.put("antis", convertStrToList(map.get("antis").toString()));
    	}
    	map.put("pkStore", ((User)user).getPkStore());
    	StringBuilder sql = new StringBuilder("");
    	if(CommonUtils.isNotNull(map.get("flagPrecious"))){
    		if(!CommonUtils.isEmptyString(sql.toString())){
	    	 sql.append(" or  pd.flag_precious = '").append(map.get("flagPrecious")+"'");
	    	}else{
	    	 sql = new StringBuilder("  pd.flag_precious = '").append(map.get("flagPrecious")+"'");
	    	}
    	}
    	if(CommonUtils.isNotNull(map.get("flagVacc"))){
    		if(!CommonUtils.isEmptyString(sql.toString())){
	    		sql.append(" or  pd.flag_vacc = '").append(map.get("flagVacc")+"'");
	    	}else{
	    		sql = new StringBuilder("  pd.flag_vacc = '"+map.get("flagVacc")+"'");
	    	}
    	}
    	if(CommonUtils.isNotNull(map.get("flagRm"))){
	    	 if(!CommonUtils.isEmptyString(sql.toString())){
	    		sql.append(" or  pd.flag_rm = '").append(map.get("flagRm")+"'");
	    	}else{
	    		sql = new StringBuilder("  pd.flag_rm = '"+map.get("flagRm")+"'");
	    	}
    	}
    	if(CommonUtils.isNotNull(map.get("flagReag"))){
    		if(!CommonUtils.isEmptyString(sql.toString())){
    			sql.append(" or  pd.flag_reag = '"+map.get("flagReag")+"'");
    	    }else{
    		   sql = new StringBuilder("  pd.flag_reag = '"+map.get("flagReag")+"'");
    	    }
    	}
    	map.put("flagSql", sql.toString()); 
        map.put("flagHisSql", " pds.last_date is null or to_date("+DateUtils.getDefaultDateFormat().format(new Date())+",'YYYYMMDDHH24MISS') - pds.last_date >= pds.count_per  ");
    	return inventoryMapper.queryPdStockByConList(map);
    }
   
    /**
     * 账面取数
     * @param param{pkPds,pkStore}
     * @param user
     * @return
     */
    public List<PdStockVo> getAccNum(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null )
    		throw new BusException("未获取到物品主键！");
    	map.put("pkStore", ((User)user).getPkStore());
    	return inventoryMapper.queryAccNum(map);
    }
    
    /**
     * 查询物品账面数量
     * @param param
     * @param user
     * @return
     */
    public List<InventoryDtVo> queryPdAccNum(String param,IUser user){
    	String pkPd = JsonUtil.readValue(param, String.class);
    	if(pkPd == null||pkPd.equals("")) throw new BusException("未获取到物品主键！");
    	return inventoryMapper.queryPdAccNum(((User)user).getPkStore(),pkPd);
    }
    
    /**
     * 查询物品账面数量(多个物品)
     * @param param
     * @param user
     * @return
     */
    public List<InventoryDtVo> queryPdAccNumBymode(String param,IUser user){
    	List<String> codePds = JsonUtil.readValue(param, new TypeReference<List<String>>() {});
    	if(codePds == null||codePds.size()==0) throw new BusException("未获取到药品信息！");
    	Map<String,Object> paramMap=new HashMap<String,Object>();
    	paramMap.put("pkStore", ((User)user).getPkStore());
    	paramMap.put("codePdList", codePds);
    	return inventoryMapper.queryPdAccNumBymode(paramMap);
    	
    }
    
    /**
     * 查看盘盈盘亏明细
     * @param param--pk_inv
     * @param user
     * @return
     */
    public List<InventoryDtVo>  queryInvtDtByDiff(String param,IUser user){
    	Map<String,Object> map= JsonUtil.readValue(param, Map.class);
    	if(null==map)
    		throw new BusException("未获取到盘点单主键");
    	return inventoryMapper.queryInvtDtByDiff(map);
    }
    /**
     * 保存盘点单
     * @param param
     * @param user
     */
    public InventoryVo saveInv(String param,IUser user){
    	InventoryVo invo = JsonUtil.readValue(param, InventoryVo.class);
    	User doUser=(User)user;
    	if(invo == null)  throw new BusException("未获取到盘点单数据！");
    	List<InventoryDtVo> dtlist = invo.getDtlist();
    	List<InventoryDtVo> insertlist = new ArrayList<InventoryDtVo>();
    	List<InventoryDtVo> updatelist = new ArrayList<InventoryDtVo>();
    	Set<String> pkPds=new HashSet<String>();
    	PdInventory inv = new PdInventory();
    	ApplicationUtils.copyProperties(inv,invo);
    	//保存盘点单
    	if(CommonUtils.isEmptyString(inv.getPkPdinv())){//新增
    		DataBaseHelper.insertBean(inv);
    	}else{
    		DataBaseHelper.updateBeanByPk(inv,false);
    	}
    	
    	String pkinv = inv.getPkPdinv();
    	for(InventoryDtVo dt:dtlist){
    		if(CommonUtils.isEmptyString(dt.getPkPdinvdt())){
	    		ApplicationUtils.setBeanComProperty(dt, true);
	    		dt.setPkPdinv(pkinv);
	    		dt.setPkPdinvdt(NHISUUID.getKeyId());
	    		dt.setPkEmpInput(doUser.getPkEmp());
	    		dt.setNameEmpInput(doUser.getNameEmp());
	    		insertlist.add(dt);
	    		pkPds.add(dt.getPkPd());
    		}else{
    			updatelist.add(dt);
    		}
    	}
    	if(pkPds!=null && pkPds.size()>0){
    		//String delSql="delete from PD_INV_DETAIL where PK_PDINV ='"+pkinv+"' and PK_PD in ("+CommonUtils.convertSetToSqlInPart(pkPds,"pk_pd")+")";
    		
    		String upSql="update PD_INV_DETAIL set del_flag='1' ,modifier=pk_pdinv,pk_pdinv='"+invo.getCodeInv()+"' where PK_PDINV ='"+pkinv+"' and (PK_PD in ("+CommonUtils.convertSetToSqlInPart(pkPds,"pk_pd")+"))";
    		DataBaseHelper.execute(upSql, new Object[]{});
    	}
    	//保存明细
    	if(insertlist!=null&&insertlist.size()>0){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdInvDetail.class),insertlist);
    	}
    	if(updatelist!=null && updatelist.size()>0){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(PdInvDetail.class),updatelist);
    	}
    	invo.setPkPdinv(pkinv);
    	invo.setDtlist(dtlist);
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
    	
    	StringBuffer sqlDt=new StringBuffer();
    	sqlDt.append("delete from pd_inv_detail  where pd_inv_detail.pk_pdinv = ?");
    	sqlDt.append(" and exists (select 1 from pd_inventory  where pd_inv_detail.pk_pdinv=pd_inventory.pk_pdinv and pd_inventory.eu_status='0')");
    	
    	DataBaseHelper.execute(sqlDt.toString(), new Object[]{pk_inv});
    	
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
    	//1）盘盈物品生成盘盈入库单
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("flag", "1");
    	map.put("pkPdinv", pk_inv);
    	List<InventoryDtVo> indt = inventoryMapper.queryInvtDtByDiff(map);
    	if(indt!=null&&indt.size()>0){
    		PdSt st = this.createPdst(u, IDictCodeConst.DT_STTYPE_INVLOSSIN, "1");
    		insertSt(st,indt,u,"1");
    	}
    	//2）盘亏物品生成盘亏出库单；
    	map.put("flag", "2");
    	List<InventoryDtVo> outdt = inventoryMapper.queryInvtDtByDiff(map);
    	if(outdt!=null&&outdt.size()>0){
    		PdSt st = this.createPdst(u, IDictCodeConst.DT_STTYPE_INVLOSSOUT, "-1");
    		insertSt(st,outdt,u,"-1");
    	}
    	//更新盘点单状态
    	Map<String,Object> map1 = new HashMap<String,Object>();
    	map1.put("pkPdinv", pk_inv);
    	map1.put("pkEmp", u.getPkEmp());
    	map1.put("nameEmp", u.getNameEmp());
    	map1.put("dateChk", new Date());
    	String update = "update pd_inventory  set pk_emp_chk = :pkEmp,name_emp_chk = :nameEmp,"+
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
     * @param u
     */
    private void insertSt(PdSt st,List<InventoryDtVo> dtlist,User u,String eudirect){
    	DataBaseHelper.insertBean(st);
    	List<PdStDetail> insert_list = new ArrayList<PdStDetail>();
		int i = 1;
		for(InventoryDtVo dt:dtlist){
			insert_list.add(this.createPdstdt(dt, u, st.getPkPdst(), i,eudirect));
			i++;
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class),insert_list);
    }
    /**
	 * 构建出入库单
	 * @param stvo
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
	private PdStDetail createPdstdt(InventoryDtVo dt,User u,String pk_pdst,int i,String eudirect){
		PdStDetail pddt = new PdStDetail();
		pddt.setBatchNo(dt.getBatchNo());
		pddt.setDateExpire(dt.getDateExpire());
		pddt.setDateFac(dt.getDateFac());
		pddt.setDelFlag("0");
		pddt.setDisc(1.00);
		pddt.setFlagChkRpt("0");
		pddt.setFlagFinish("0");
		pddt.setFlagPay("0");
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
        if("1".equals(eudirect)){
        	pddt.setQuanOutstore(0.0);
        }
        pddt.setSortNo(i);
        pddt.setAmount(MathUtils.div(pddt.getQuanMin(), CommonUtils.getDouble(dt.getPackSizePd()))*pddt.getPrice());
		pddt.setAmountCost(MathUtils.div(pddt.getQuanMin(), CommonUtils.getDouble(dt.getPackSizePd()))*pddt.getPriceCost());
		ApplicationUtils.setBeanComProperty(pddt, true);
		return pddt;
	}
	/**
	 * 获取各类字典
	 * @param param{code_defdoclist}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getDefDocList(String param,IUser user){
		String code_defdoclist = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(code_defdoclist)) return null;
		String sql = "select code ,name from bd_defdoc where code_defdoclist= ? ";
		return DataBaseHelper.queryForList(sql, new Object[]{code_defdoclist});
	}
	
	/**
	 * 盘点新增方法
	 * @param param
	 * @param user
	 */
	public PdInventoryVo saveInvDetial(String param,IUser user){
		//1.接收参数初始化，并做校验
		PdInventoryVo pdInvVo=JsonUtil.readValue(param, PdInventoryVo.class);
		if(pdInvVo.getCodeInv()==null) throw new BusException("为获得盘点单信息！");
		
		//2.根据盘点单号查询盘点记录
		//String sql="select count(1) from pd_inventory where code_inv=? and del_flag='0'";
		//int count=DataBaseHelper.queryForScalar(sql, Integer.class, pdInvVo.getCodeInv());
		User newUser=(User)user;
		PdInventory pdInventory=new PdInventory();
		ApplicationUtils.copyProperties(pdInventory, pdInvVo);
		pdInventory.pkOrg=newUser.getPkOrg();
		if(pdInvVo.getPkPdinv()!=null){
			DataBaseHelper.updateBeanByPk(pdInventory, false);
		}else{
			DataBaseHelper.insertBean(pdInventory);
		}
		//3.保存明细记录
		PdInvDetail pdinvDt=pdInvVo.getInvPdvo();
		pdinvDt.setPkPdinvdt(NHISUUID.getKeyId());
		pdinvDt.setPkOrg(newUser.getPkOrg());
		pdinvDt.setDelFlag("0");;
		pdinvDt.setCreator(newUser.getPkEmp());
		pdinvDt.setPkPdinv(pdInventory.getPkPdinv());
		DataBaseHelper.insertBean(pdinvDt);
		ApplicationUtils.copyProperties(pdInvVo,pdInventory );
		return pdInvVo;
	}
	
	/**
	 * 查询药品批次
	 * @param param{"pkPdinv":"盘点单主键","pkPd":"药品主键"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryBatchNoByPkPd(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		return inventoryMapper.qryBatchNoByPkPd(paramMap);
	}
	
	/**
	 * 选择-全部（明细）
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryRealInvDtAll(String param,IUser user){
		List<Map<String,Object>> resMap=inventoryMapper.qryRealInvDtAll(((User)user).getPkStore());
		return resMap;
	}
	
	/**
	 * 选择部分-明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryRealInvDtKind(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null){
    		map = new HashMap<String,Object>();
    	}
    	if(CommonUtils.isNotNull(map.get("posis"))){
    		String posiStr=map.get("posis").toString();
			String [] str=posiStr.split(",");
			String posiSql="1=0";
			for (int i = 0; i < str.length; i++) {
				posiSql+=" or pds.posi_no like '%"+str[i]+"%'";
			}
			map.put("posiSql", posiSql);
    	}
    	if(CommonUtils.isNotNull(map.get("drugtypes"))){
    		List<String> list = convertStrToList(map.get("drugtypes").toString());
			map.put("drugtypes",list);
    	}
    	if(CommonUtils.isNotNull(map.get("dtpois"))){
    		map.put("dtpois", convertStrToList(map.get("dtpois").toString()));
    	}
    	if(CommonUtils.isNotNull(map.get("usecates"))){
    		map.put("usecates", convertStrToList(map.get("usecates").toString()));
    	}
    	if(CommonUtils.isNotNull(map.get("antis"))){
    		map.put("antis", convertStrToList(map.get("antis").toString()));
    	}
    	map.put("pkStore", ((User)user).getPkStore());
    	StringBuilder sql = new StringBuilder("");
    	if(CommonUtils.isNotNull(map.get("flagPrecious"))){
    		if(!CommonUtils.isEmptyString(sql.toString())){
	    	 sql.append(" or  pd.flag_precious = '").append(map.get("flagPrecious")+"'");
	    	}else{
	    	 sql = new StringBuilder("  pd.flag_precious = '").append(map.get("flagPrecious")+"'");
	    	}
    	}
    	if(CommonUtils.isNotNull(map.get("flagVacc"))){
    		if(!CommonUtils.isEmptyString(sql.toString())){
	    		sql.append(" or  pd.flag_vacc = '").append(map.get("flagVacc")+"'");
	    	}else{
	    		sql = new StringBuilder("  pd.flag_vacc = '"+map.get("flagVacc")+"'");
	    	}
    	}
    	if(CommonUtils.isNotNull(map.get("flagRm"))){
	    	 if(!CommonUtils.isEmptyString(sql.toString())){
	    		sql.append(" or  pd.flag_rm = '").append(map.get("flagRm")+"'");
	    	}else{
	    		sql = new StringBuilder("  pd.flag_rm = '"+map.get("flagRm")+"'");
	    	}
    	}
    	if(CommonUtils.isNotNull(map.get("flagReag"))){
    		if(!CommonUtils.isEmptyString(sql.toString())){
    			sql.append(" or  pd.flag_reag = '"+map.get("flagReag")+"'");
    	    }else{
    		   sql = new StringBuilder("  pd.flag_reag = '"+map.get("flagReag")+"'");
    	    }
    	}
    	map.put("flagSql", sql.toString()); 
        map.put("flagHisSql", " pds.last_date is null or to_date("+DateUtils.getDefaultDateFormat().format(new Date())+",'YYYYMMDDHH24MISS') - pds.last_date >= pds.count_per  ");
        return inventoryMapper.qryRealInvDtKind(map);
	}
}
