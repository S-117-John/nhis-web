package com.zebone.nhis.scm.st.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.service.PdStOutHandler;
import com.zebone.nhis.scm.pub.service.PdStPubService;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.nhis.scm.pub.vo.PuOrderDtVo;
import com.zebone.nhis.scm.pub.vo.PuOrderVo;
import com.zebone.nhis.scm.st.dao.PuInStoreMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 采购入库
 * @author yangxue
 *
 */
@Service
public class PuInStoreService {
	@Resource
    private PuInStoreMapper puInStoreMapper;
	@Resource
	private PdStPubService pdStPubService;
	@Resource
	private PdStOutHandler stOutHandler;
	
	/**
	 *查询采购订单
	 * @param param{}
	 * @param user
	 * @return
	 */
	public List<PuOrderVo> queryOrderList(String param,IUser user){
		Map<String,Object> map = new HashMap();
		map.put("pkOrg", ((User)user).getPkOrg());
		map.put("pkDept", ((User)user).getPkDept());
		map.put("pkStore", ((User)user).getPkStore());
		return puInStoreMapper.queryOrderList(map);
	}
	/**
	 * 查询采购订单明细
	 * @param param{pkPdpu}
	 * @param user
	 * @return
	 */
	public List<PuOrderDtVo> queryOrderDtList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		map.put("pkStore", ((User)user).getPkStore());
		map.put("curDate", DateUtils.getDefaultDateFormat().format(new Date()));
		map.put("pkOrg",((User)user).getPkOrg());
		List<PuOrderDtVo> list =  puInStoreMapper.queryOrderDtList(map);
		return list;
	}
	/**
	 * 查询采购入库单(只查询direct=1的)
	 * @param param{pkStoreSt,codeSt,pkSupplyer,dateBegin,dateEnd,pkEmpOp,euStatus}
	 * @param user
	 * @return
	 */
	public List<PdStVo> queryPdStoreList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(CommonUtils.isNotNull(map.get("dateBegin"))){
			map.put("dateBegin", CommonUtils.getString(map.get("dateBegin")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
		return puInStoreMapper.queryPuPdStList(map);
	}
	/**
	 * 查询采购入库单明细
	 * @param param{pkStore,pkPdst,flagRtn}
	 * @param user
	 * @return
	 */
	public List<PdStDtVo> queryPdStDetailList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		return puInStoreMapper.queryPuPdStDetailList(map);
	}
	/**
	 * 保存采购入库信息
	 * @param param
	 * @param user
	 */
	public PdStVo  savePdSt(String param,IUser user){
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		if(CommonUtils.isNotNull(stvo.getDtlist())) {
			stvo.getDtlist().stream().forEach(m ->
			{
				Double price=MathUtils.mul(MathUtils.div(m.getPrice(), CommonUtils.getDouble(m.getPackSize())), CommonUtils.getDouble(m.getPackSizePd()));
				Double priceCost=MathUtils.mul(MathUtils.div(m.getPriceCost(), CommonUtils.getDouble(m.getPackSize())), CommonUtils.getDouble(m.getPackSizePd()));

				m.setPrice(new BigDecimal(price).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
				m.setPriceCost(new BigDecimal(priceCost).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			});
		}else{
			throw new BusException("未传入明细记录！");
		}
		return pdStPubService.savePdSt(stvo,user,IDictCodeConst.DT_STTYPE_BUY);
	}
	/**
	 * 提交-审核采购入库单
	 */
	public void submitPdst(String param,IUser user){
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		User u =(User)user;
		if(stvo == null) return;
		String pk_pdst = stvo.getPkPdst();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkPdst", pk_pdst);
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
		paramMap.put("dateChk", new Date());
		boolean flag = (stvo.getDtlist()!=null&&stvo.getDtlist().size()>0)?false:true;
		if(flag) return;
		if(!CommonUtils.isEmptyString(stvo.getPkPdpu())){//采购订单生成入库单先保存
			pk_pdst = savePdSt(param,user).getPkPdst();
			//更新采购订单明细
				for(PdStDtVo stdt:stvo.getDtlist()){
					StringBuffer updateSqlDt=new StringBuffer();
					updateSqlDt.append("update pd_purchase_dt set quan_in_min = nvl(quan_in_min,0)+:quanMin,");
					updateSqlDt.append("  lastdate_in =:dateChk, pk_emp_acc = :pkEmp, name_emp_acc = :nameEmp ,ts=:dateChk ");
					if(stdt.getQuanPuMin()>=(stdt.getQuanMin()+stdt.getQuanInMin())){
						updateSqlDt.append(",flag_acc='1' ");
					}
					updateSqlDt.append(" where pk_pdpudt = :pkPdPudt");
					paramMap.put("quanMin",stdt.getQuanMin());
					paramMap.put("pkPdPudt", stdt.getPkPdpudt());
					DataBaseHelper.update(updateSqlDt.toString(), paramMap);
				}
				//更新采购订单状态
				StringBuffer updatePu=new StringBuffer();
				updatePu.append("update pd_purchase set eu_status='2' ");
				updatePu.append(" ,ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') ");
				updatePu.append(" where  (select count(1) from pd_purchase_dt where flag_acc='0' and  pk_pdpu = ? )<=0  and  pk_pdpu = ? ");
				
				DataBaseHelper.update(updatePu.toString(), new Object[]{stvo.getPkPdpu(),stvo.getPkPdpu()});
		}
		if(DataBaseHelper.queryForScalar("select count(1) from pd_st where  pk_pdst = ? and eu_status=1",Integer.class,new Object[]{pk_pdst}) ==0){
			pdStPubService.updatePdSt(paramMap);
		}
		//如果选择了出向仓库或者出向部门，新增出库记录，忽略库存处理
		boolean deptFlag = !CommonUtils.isEmptyString(stvo.getPkDeptLk());
		boolean storeFlag = !CommonUtils.isEmptyString(stvo.getPkStoreLk());
		if(deptFlag||storeFlag){
			PdSt stLk =  new PdSt();
		    ApplicationUtils.copyProperties(stLk, stvo);
		    stLk.setPkPdst(null);
		    stLk.setCodeSt(ScmPubUtils.getOutStoreCode());
		    stLk.setEuDirect("-1");
		    stLk.setEuStatus("1");
		    stLk.setDateChk(new Date());
		    stLk.setFlagChk("1");
		    stLk.setNameEmpChk(u.getNameEmp());
		    stLk.setPkEmpChk(u.getPkEmp());
			if(deptFlag){
				stLk.setDtSttype(IDictCodeConst.DT_STTYPE_DETPOUT);
			}
			if(storeFlag){
				stLk.setDtSttype(IDictCodeConst.DT_STTYPE_ALLOOUT);//以仓库为基准
			}
			stLk.setPkOrgLk(u.getPkOrg());
			ApplicationUtils.setBeanComProperty(stLk, true);
			DataBaseHelper.insertBean(stLk);
			String pk_st_lk = stLk.getPkPdst();
			List<PdStDetail> list_lk = new ArrayList<PdStDetail>();
			//List<String> updateList = new ArrayList<String>();
			for(PdStDtVo stdtvo:stvo.getDtlist()){
				//updateList.add("update pd_st_detail set quan_outstore = quan_min ,flag_finish='1' where pk_pdstdt = '"+stdtvo.getPkPdstdt()+"'");
				stdtvo.setPkPdst(pk_st_lk);
				stdtvo.setPkPdstdt(NHISUUID.getKeyId());
				ApplicationUtils.setBeanComProperty(stdtvo, true);
				if(!stdtvo.getPackSize().equals(stdtvo.getPackSizePd())){
					Double price = MathUtils.mul(MathUtils.div(stdtvo.getPrice(), CommonUtils.getDouble(stdtvo.getPackSize())), CommonUtils.getDouble(stdtvo.getPackSizePd()));
					Double priceCost = MathUtils.mul(MathUtils.div(stdtvo.getPriceCost(), CommonUtils.getDouble(stdtvo.getPackSize())), CommonUtils.getDouble(stdtvo.getPackSizePd()));
					stdtvo.setPrice(new BigDecimal(price).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
					stdtvo.setPriceCost(new BigDecimal(priceCost).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				list_lk.add(stdtvo);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), list_lk);
			/*if(updateList!=null&&updateList.size()>0){
				DataBaseHelper.batchUpdate(updateList.toArray(new String[0]));
			}*/
			String upStdtSql="update pd_st_detail set quan_outstore = quan_min ,flag_finish='1' where pk_pdst=?";
			DataBaseHelper.execute(upStdtSql,new Object[]{pk_pdst});
		}else{
			//更新库存
			pdStPubService.updateInStore(stvo.getDtlist(),u.getPkStore(),u.getPkDept());
		}
		
		
	}
	
	/**
	 * 删除采购入库单
	 * @param param{pkPdst}
	 * @param user
	 */
	public void deletePdst(String param,IUser user){
//		String pkPdst = JsonUtil.readValue(param, String.class);
//		if(CommonUtils.isEmptyString(pkPdst)) return;
		pdStPubService.deletePdst(param, user);
 	}
	/**
	 * 退回采购入库单
	 * @param param
	 * @param user
	 */
	public String rtnPdst(String param,IUser user){
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		if(stvo == null) throw new BusException("未获取到需要退回的单据！");
		PdSt st = stOutHandler.createPdst(null,null,stvo,(User)user,IDictCodeConst.DT_STTYPE_RTN,"-1");
		DataBaseHelper.insertBean(st);
		//生成明细
		List<PdStDtVo> dtlist = stvo.getDtlist();
		if(dtlist == null ||dtlist.size() <= 0) return null;
		List<PdStDetail> insert_list = new ArrayList<PdStDetail>();
		int i = 1;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		for(PdStDtVo dt:dtlist){
			PdStDetail stdt = this.createPdstdt(null,dt,(User)user,st.getPkPdst(),i);
			i++;
			stdt.setPkSupplyer(stvo.getPkSupplyer());
			insert_list.add(stdt);
			//重新设置价格为转换过的零售单位对应的价格，否则取不到库存
			dt.setPrice(new BigDecimal(stdt.getPrice()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			dt.setPriceCost(new BigDecimal(stdt.getPriceCost()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			//更新库存表
			pdStPubService.updateStock(dt,st.getPkStoreSt(),dt.getQuanMin());
			//更新原入库单的库存数量
			paramMap.put("quanMin", dt.getQuanMin());
			paramMap.put("pdStdt", dt.getPkPdstdt());
			DataBaseHelper.update("update pd_st_detail set quan_outstore = nvl(quan_outstore,0)+:quanMin,flag_finish = (case when nvl(quan_outstore,0)+:quanMin=quan_min then '1' else '0' end) where pk_pdstdt = :pdStdt", paramMap);
			
		}
		if(insert_list!=null&&insert_list.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), insert_list);
		}
		return st.getCodeSt();
		
	}
	/**
	 * 交易号【008006001009】 通过科室主键查询仓库主键
	 * @param param
	 * @param user
	 * @return
	 */
	public String qryPkStoreByPkDept(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null || paramMap.size()<=0) return null;
		return puInStoreMapper.qryPkStoreByPkdept(paramMap);
	}
	
	/**
	 * 构建出库明细
	 * @param dt
	 * @param u
	 * @return
	 */
	public PdStDetail createPdstdt(PdOutDtParamVo dt,PdStDtVo stdt,User u,String pk_pdst,int i){
		PdStDetail pddt = new PdStDetail();
		pddt.setDelFlag("0");
		pddt.setDisc(1.00);
		pddt.setFlagChkRpt("0");
		pddt.setFlagFinish("0");
		pddt.setFlagPay("0");
		pddt.setPkOrg(u.getPkOrg());
		pddt.setPkPdstdt(NHISUUID.getKeyId());
		pddt.setPkPdst(pk_pdst);
        pddt.setSortNo(i);
		ApplicationUtils.setBeanComProperty(pddt, true);
		pddt.setBatchNo(stdt.getBatchNo());
		pddt.setDateExpire(stdt.getDateExpire());
		pddt.setDateFac(stdt.getDateFac());
		pddt.setPackSize(stdt.getPackSize());
		pddt.setPkPd(stdt.getPkPd());
		pddt.setPkUnitPack(stdt.getPkUnitPack());
		pddt.setPrice(MathUtils.mul(MathUtils.div(stdt.getPrice(),CommonUtils.getDouble(stdt.getPackSize())),CommonUtils.getDouble(stdt.getPackSizePd())));
		pddt.setPriceCost(MathUtils.mul(MathUtils.div(stdt.getPriceCost(),CommonUtils.getDouble(stdt.getPackSize())),CommonUtils.getDouble(stdt.getPackSizePd())));
		pddt.setQuanPack(stdt.getQuanPack());
        pddt.setQuanMin(stdt.getQuanMin());
		pddt.setAmount(stdt.getAmount());
		pddt.setAmountCost(stdt.getAmountCost());
		pddt.setReceiptNo(stdt.getReceiptNo());
		return pddt;
	}

	/**
	 * 校验当前仓库是否初始化
	 * @param param
	 * @param user
	 * @return
	 */
	public Integer verfyPdIsStRecord(String param, IUser user){
		//如果当前仓库已经发生了出入库业务，就不判断
		Integer st = DataBaseHelper.queryForScalar("select count(*) from pd_st where PK_DEPT_ST=?", Integer.class, new Object[]{((User) user).getPkStore()});
		if(st>0){
			return 1;
		}
		Integer isInit = pdStPubService.verfyPdIsStRecord(((User)user).getPkDept(),((User)user).getPkStore());
		return isInit;
	}
	
	/**
	 * 008006001011
	 * 采购入库获取药品单价
	 * @param param
	 * @param user
	 * @return
	 */
	public Double getPuPdPrice(String param,IUser user){
	    Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
	    if(paramMap==null)return 0D;
	    paramMap.put("pkStore", UserContext.getUser().getPkStore());
	    Double priceCost=puInStoreMapper.getPdPuPrice(paramMap);
		return priceCost;
		
	}
}
