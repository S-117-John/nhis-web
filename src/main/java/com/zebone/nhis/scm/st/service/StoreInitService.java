package com.zebone.nhis.scm.st.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.zebone.platform.Application;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdCc;
import com.zebone.nhis.common.module.scm.st.PdCcDetail;
import com.zebone.nhis.common.module.scm.st.PdInvInit;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.service.PdStPubService;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.st.dao.StoreInitMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.nhis.scm.st.vo.PdInvInitVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 初始化仓库库存服务
 * @author yangxue
 *
 */


@Service
public class StoreInitService {
	@Resource
	private StoreInitMapper storeInitMapper;
	@Resource
	private PdStPubService pdStPubService;
    /**
     * 查询初始化信息
     * @param param
     * @param user
     * @return
     */
	public List<Map<String,Object>> queryInvInit(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		
		//pkDep 当前科室  pkStore 当前仓库
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkStore"))||CommonUtils.isNull(paramMap.get("pkDept")))
			throw new BusException("未获取到要查询的科室及对应仓库");			
		//助记码（拼音码或编码）
		String spcodeOrcode = "";
		if(paramMap.get("spcodeOrcode")!=null){
			spcodeOrcode = paramMap.get("spcodeOrcode").toString().toUpperCase();
		}
		paramMap.put("spcodeOrcode",spcodeOrcode);
		paramMap.put("pdType", MapUtils.getString(paramMap,"pdType","0"));
		//可从后台直接获取科室和仓库
		List<Map<String,Object>> InvInitList = storeInitMapper.queryInvInit(paramMap);		
		return InvInitList;
	}
	/**
	 * 保存初始化信息
	 * @param param
	 * @param user
	 */
	public void saveInvInit(String param,IUser user){
		PdInvInit pdInv=JsonUtil.readValue(param, PdInvInit.class);
		//判断是否有新增参数
		if (pdInv == null)
			throw new BusException("未获取到需要保存的初始数据");
		
		//主键为空则新增，不为空则根据主键修改数据
		if((pdInv.getPkPdinvinit()==null || "".equals(CommonUtils.getString(pdInv.getPkPdinvinit())))){
			//非用户传递参数
			pdInv.setPkPdinvinit(NHISUUID.getKeyId());
			pdInv.setPkDept(((User)user).getPkDept());
			pdInv.setPkOrg(((User)user).getPkOrg());
			pdInv.setPkStore(((User)user).getPkStore());
			pdInv.setPkEmp(((User)user).getPkEmp());
			pdInv.setNameEmp(((User)user).getNameEmp());
			pdInv.setDateInput(new Date());
			pdInv.setFlagCc("0");		
			DataBaseHelper.insertBean(pdInv);
		}else{
			//将传递进来的参数转成数据库相对应的类型
			String modityTimeSource = DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", new Date());
			Date modityTime = DateUtils.strToDate(modityTimeSource, "yyyy-MM-dd HH:mm:ss");
			pdInv.setModityTime(modityTime);			
			DataBaseHelper.update("update pd_inv_init set pk_pd=:pkPd ,batch_no=:batchNo ,date_expire=:dateExpire ,price_cost=:priceCost ,price=:price ,quan_min=:quanMin ,amount_cost=:amountCost ,amount=:amount ,modity_time=:modityTime  where pk_pdinvinit=:pkPdinvinit", pdInv);
		}
		
	}
	/**
	 * 转账处理
	 * @param param{pkDept,pkStore}
	 * @param user
	 */
	public void transStock(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkStore"))||CommonUtils.isNull(paramMap.get("pkDept")))
			throw new BusException("未获取到要转账的科室及对应仓库");
		//查询未转账的初始库存数据
		int pdType = MapUtils.getIntValue(paramMap,"pdType",0);
		paramMap.put("pdType", pdType);
		List<PdInvInitVo> initList = storeInitMapper.getAllInvInitByCC(paramMap);
		Date dateNow=new Date();
		//结账
		String pkPd = null;
		if(initList==null||initList.size()<=0) {
			Map<String,Object> map = DataBaseHelper.queryForMap("select min(CREATE_TIME) tm from pd_st where PK_STORE_ST=? and EU_STATUS=1"
					,new Object[]{paramMap.get("pkStore")});
			if(MapUtils.isNotEmpty(map)){
				initList = Lists.newArrayList();
				dateNow = (Date) MapUtils.getObject(map,"tm");
				//随意获取一个当前仓库存在的物品
				if(Application.isSqlServer()){
					pkPd = MapUtils.getString(DataBaseHelper.queryForMap("select top 1 sto.pk_pd from bd_pd_store sto inner join bd_pd pd on sto.PK_PD=pd.PK_PD where pk_store = ? and pd.dt_pdtype like '1%'",new Object[]{paramMap.get("pkStore")}),"pkPd");
				} else {
					pkPd = MapUtils.getString(DataBaseHelper.queryForMap("select * from (select rownum as n,sto.pk_pd from bd_pd_store sto inner join bd_pd pd on sto.PK_PD=pd.PK_PD where pk_store = ? and pd.dt_pdtype like '1%') tp where tp.n=1",new Object[]{paramMap.get("pkStore")}),"pkPd");
				}
			}
		}

		String pkPdcc = this.processPdcc(initList,(User)user,CommonUtils.getString(paramMap.get("pkStore")),CommonUtils.getString(paramMap.get("pkDept")),dateNow);
		if(CollectionUtils.isNotEmpty(initList)) {
			//入库
			this.updateStock(initList,CommonUtils.getString(paramMap.get("pkStore")),CommonUtils.getString(paramMap.get("pkDept")));
			//生成初始化入库的入库单
			createPdStinfo(initList,dateNow);
			//更新结账状态
			paramMap.put("pkPdcc", pkPdcc);
			DataBaseHelper.update("update pd_inv_init set flag_cc='1',pk_pdcc=:pkPdcc where pk_store=:pkStore and pk_dept=:pkDept and flag_cc='0'", paramMap);
		} else{
			User u = (User)user;
			PdInvInit pdInv = new PdInvInit();
			pdInv.setPkDept(u.getPkDept());
			pdInv.setPkStore(u.getPkStore());
			pdInv.setPkEmp(u.getPkEmp());
			pdInv.setNameEmp(u.getNameEmp());
			pdInv.setDateInput(new Date());
			pdInv.setFlagCc("1");
			pdInv.setPkPdcc(pkPdcc);
			pdInv.setPkPd(pkPd);
			ApplicationUtils.setDefaultValue(pdInv,true);
			DataBaseHelper.insertBean(pdInv);
		}
	}
	
	/**
	 * 生成初始化入库的入库单
	 * @param initList
	 */
	private void createPdStinfo(List<PdInvInitVo> initList,Date curDate){
		PdSt pdst=new PdSt();
		User user=UserContext.getUser();
		pdst.setPkPdst(NHISUUID.getKeyId());
		pdst.setPkOrg(user.getPkOrg());
		pdst.setPkDeptSt(user.getPkDept());
		pdst.setPkStoreSt(user.getPkStore());
		pdst.setDtSttype("0110");
		pdst.setCodeSt(ApplicationUtils.getCode("0702"));//调用入库单号编码接口
		pdst.setEuDirect("1");
		pdst.setDateSt(curDate);
		pdst.setEuStatus("1");
		pdst.setFlagPay("0");
		pdst.setPkEmpOp(user.getPkEmp());
		pdst.setNameEmpOp(user.getNameEmp());
		pdst.setFlagChk("1");
		pdst.setPkEmpChk(user.getPkEmp());
		pdst.setNameEmpChk(user.getNameEmp());
		pdst.setDateChk(curDate);
		pdst.setNote("初始建账");
		pdst.setFlagPu("0");
		List<PdStDetail> stDts=new ArrayList<PdStDetail>();
		
		int i=1;
		for (PdInvInitVo initVo : initList) {
			PdStDetail pdstDt=new PdStDetail();
			pdstDt.setPkPdstdt(NHISUUID.getKeyId());
			pdstDt.setPkOrg(user.getPkOrg());
			pdstDt.setPkPdst(pdst.getPkPdst());
			pdstDt.setSortNo(i);
			pdstDt.setPkPd(initVo.getPkPd());
			pdstDt.setPkUnitPack(initVo.getPkUnit());
			pdstDt.setPackSize(initVo.getPackSize());
			double quanPack=MathUtils.div(initVo.getQuanMin(), new Double(initVo.getPackSize()));
			pdstDt.setQuanPack(quanPack);
			pdstDt.setQuanMin(initVo.getQuanMin());
			pdstDt.setQuanOutstore(new Double(0));
			pdstDt.setPriceCost(initVo.getPriceCost());
			pdstDt.setAmountCost(initVo.getAmountCost());
			pdstDt.setPrice(initVo.getPrice());
			pdstDt.setAmount(initVo.getAmount());
			pdstDt.setDisc(new Double(1));
			pdstDt.setBatchNo(initVo.getBatchNo());
			pdstDt.setDateExpire(initVo.getDateExpire());
			pdstDt.setFlagChkRpt("0");
			pdstDt.setFlagPay("0");
			pdstDt.setFlagFinish("0");
			stDts.add(pdstDt);
			i++;
		}
		DataBaseHelper.insertBean(pdst);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), stDts);
		
	}
	
	/**
	 * @param list
	 * @return pkPdcc --结账主键
	 */
	private String processPdcc(List<PdInvInitVo> list,User u,String pkStore,String pkDept,Date curDate){
		PdCc cc = new PdCc();
		cc.setDateBegin(curDate);
		cc.setDateEnd(curDate);
		cc.setPkDept(pkDept);
		cc.setPkStore(pkStore);
		cc.setPkEmpCc(u.getPkEmp());
		cc.setNameEmpCc(u.getNameEmp());
		cc.setDateCc(curDate);
		String strYear = DateUtils.getCurrYear();
		String strMonth = DateUtils.getCurrMonth();
		//当前月份-1
		if(Integer.parseInt(strMonth)==1){
			strYear = Integer.toString(Integer.parseInt(strYear)-1); 
			strMonth = Integer.toString(12);
		}else{
			strMonth = Integer.toString(Integer.parseInt(strMonth)-1);
		}
		
		//月份不够两位数，前面补0
		if(strMonth.length()<2)
			strMonth = "0"+strMonth;
		cc.setMonthFin(Integer.parseInt(strYear+strMonth));
		ApplicationUtils.setDefaultValue(cc, true);
		DataBaseHelper.insertBean(cc);
		//插入月结明细
		for(PdInvInitVo init:list){
			PdCcDetail dt = new PdCcDetail();
			dt.setPkPd(init.getPkPd());
			dt.setPkUnit(init.getPkUnit());
			dt.setAmount(init.getAmount());
			dt.setAmountCost(init.getAmountCost());
			dt.setPackSize(init.getPackSize());
			dt.setPkFactory(init.getPkFactory());
			dt.setPkPdcc(cc.getPkPdcc());
			//dt.setPrice(init.getPrice());
			//dt.setPriceCost(init.getPriceCost());
			dt.setQuanMin(init.getQuanMin());
			dt.setSpec(init.getSpec());
			ApplicationUtils.setDefaultValue(dt,true);
			DataBaseHelper.insertBean(dt);
		}
		return cc.getPkPdcc();
	}
	/**
	 * 合并库存
	 * @param list
	 */
	private void updateStock(List<PdInvInitVo> list,String pkStore,String pkDept){
		List<PdStDtVo> stdt = new ArrayList<PdStDtVo>();
		for(PdInvInitVo init:list){
			PdStDtVo dt = new PdStDtVo();
			dt.setPkUnitPack(init.getPkUnit());
			dt.setPackSize(init.getPackSize());
			dt.setQuanMin(init.getQuanMin());
			dt.setPackSizePd(init.getPackSizePd());
			dt.setPkPd(init.getPkPd());
			dt.setAmount(init.getAmount());
	    	dt.setAmountCost(init.getAmountCost());
	    	dt.setBatchNo(init.getBatchNo());
	    	dt.setDateExpire(init.getDateExpire());	    	
	    	dt.setPkOrg(init.getPkOrg());
	    	//调用入库库存接口时，传入的价格为当前仓库包装的价格，所以要将零售包装的价格进行转换
	    	double price=MathUtils.mul(init.getPrice(), new Double(init.getPackSize()));
	    	price=MathUtils.div(price, new Double(init.getPackSizePd()));
	    	dt.setPrice(price);
	    	double priceCost=MathUtils.mul(init.getPriceCost(),new Double(init.getPackSize()));
	    	priceCost=MathUtils.div(priceCost,  new Double(init.getPackSizePd()));
	    	dt.setPriceCost(priceCost);
	    	dt.setQuanMin(init.getQuanMin());
	    	stdt.add(dt);
		}
		pdStPubService.updateInStore(stdt,pkStore,pkDept);
	}
	
	/**
	 * 删除数据
	 * @param param
	 * @param user 
	 */
	public void deleteInvInit(String param,IUser user){
		PdInvInit pdInv=JsonUtil.readValue(param, PdInvInit.class);
		//判断是否有新增参数
		if (pdInv == null)
			throw new BusException("未获取到需要删除的初始数据");
		
		if(	pdInv.getFlagCc().equals("0") && pdInv.getPkPdinvinit() !=null &&  !CommonUtils.getString(pdInv.getPkPdinvinit()).equals("")){
			DataBaseHelper.deleteBeanByPk(pdInv);
		}else{
			throw new BusException("删除异常");
		}	
	}

	/**
	 * 判断当前仓库是否已经结账
	 * @param param
	 * @param user
	 * @return
	 */
	public int getAccFlag(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(MapUtils.isEmpty(paramMap)){
			throw new BusException("请传入参数");
		}
		int pdType = MapUtils.getIntValue(paramMap,"pdType");
		return DataBaseHelper.queryForScalar("select count(*) from pd_inv_init inv inner join bd_pd pd on inv.PK_PD=pd.PK_PD where inv.pk_store=? and inv.flag_cc=1 and pd.dt_pdtype like " + (pdType==0?"'0%'":"'1%'")
				,Integer.class,new Object[]{MapUtils.getString(paramMap,"pkStore")});
	}

}
