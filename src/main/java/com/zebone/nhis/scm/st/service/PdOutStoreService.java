package com.zebone.nhis.scm.st.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.service.PdStOutHandler;
import com.zebone.nhis.scm.pub.service.PdStPubService;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.nhis.scm.pub.vo.PdPlanDtVo;
import com.zebone.nhis.scm.pub.vo.PdPlanVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.nhis.scm.st.dao.PdOutStoreMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 出库处理
 * @author yangxue
 *
 */
@Service
public class PdOutStoreService {
    @Resource 
	private PdOutStoreMapper pdOutStoreMapper;
    @Resource
	private PdStPubService pdStPubService;
    @Resource
    private PdStOutHandler stOutHandler;
    /**
     * 查询待出库列表
     * @param param{codePlan,dateBegin,dateEnd,pkOrg,pkDept,pkStore}
     * @param user
     * @return
     */
    public List<PdPlanVo> queryToOutList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null ){
    		map = new HashMap<String,Object>();
    	}
    	map.put("pkStoreSrv", ((User)user).getPkStore());
    	return pdOutStoreMapper.queryToOutPdStList(map);
    }
    /**
     * 根据调拨申请单构造出库明细{pkPdplan,pkStore}
     * @return
     */
    public List<PdStDtVo> createOutDtList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) throw new BusException("未获取到待出库单主键！");
    	String pk_store = CommonUtils.getString(map.get("pkStore"));
    	map.put("pkStoreSrv",pk_store);
    	List<PdPlanDtVo> plandtlist = pdOutStoreMapper.queryToOutPdStDtList(map);
    	if(plandtlist == null ||plandtlist.size()<=0)
    		throw new BusException("未获取到待出库单明细，请核对申请物品是否正确设置了包装单位及生产厂商!");
    	List<PdStDtVo> dtlist = new ArrayList<PdStDtVo>();
    	List<String> pkPds = new ArrayList<String>();
		for(int i = 0 , len = plandtlist.size() ; i < len ; i++){
			pkPds.add(plandtlist.get(i).getPkPd());
		}
    	//加锁物品
		stOutHandler.lockConfirmPdSt(pkPds, pk_store);
    	//确认出库批次
    	for(PdPlanDtVo plan : plandtlist){
			String pk_pd = plan.getPkPd();
			List<PdStDtVo> stlist = genStDt(pk_pd,pk_store,plan.getQuanMin()-(plan.getQuanDe()==null?0:plan.getQuanDe()),plan.getCodePd(),plan.getNamePd(),plan.getSpec(),
					plan.getSpcode(),plan.getFactoryName(),plan.getUnitName(),plan.getPkPdplandt(),plan.getUnitPd());
			for (PdStDtVo dtvo : stlist) {
				dtvo.setQuanPackQ(plan.getQuanPack());
			}
			dtlist.addAll(stlist);
    	}
		return dtlist;
    }
  
    /**
     * 根据选择物品确认出库明细
     * @param param{PdStDtVo}
     * @param user
     * @return
     */
    public List<PdStDtVo> confirmStoreInfoByPd(String param,IUser user){
    	PdStDtVo vo = JsonUtil.readValue(param, PdStDtVo.class);
    	if(vo == null)throw new BusException("未获取到物品信息！");
    	String pk_pd = vo.getPkPd();
    	String pk_store = ((User)user).getPkStore();
		List<PdStDtVo> stlist = genStDt(pk_pd,pk_store,vo.getQuanMin(),vo.getPdcode(),vo.getPdname(),vo.getSpec(),
				vo.getSpcode(),vo.getFactory(),vo.getUnit(),null,vo.getUnitPd());
    	return stlist;
    }
    /**
     * 删除出库单
     * @param param{pkPdst值}
     * @param user
     */
    public void deletePdSt(String param,IUser user){
    	pdStPubService.deletePdst(param, user);
    }
    /**
     * 查询出库单
     * @param param{dtSttype,pkOrgLk,pkDeptLk,pkStoreLk,codeSt,dateBegin,dateEnd,pkEmpOp,euStatus}
     * @param user
     * @return
     */
    public List<PdStVo> queryPdStOutList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) {
    		map = new HashMap<String,Object>();
    	}
    	map.put("pkStore", ((User)user).getPkStore());
    	return pdOutStoreMapper.queryPdStOutList(map);
    }
    /**
     * 查询出库明细
     * @param param{pkPdst}
     * @param user
     * @return
     */
    public List<PdStDtVo> queryPdStDtOutList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(map == null) throw new BusException("未获取到出库单主键！");
    	map.put("pkStore", ((User)user).getPkStore());
    	List<PdStDtVo> list =  pdOutStoreMapper.queryPdStDtOutList(map);
    	return list;
    }
    
    /**
	 * 保存出库信息
	 * @param param{PdStVo}
	 * @param user
	 */
	public PdStVo savePdSt(String param,IUser user){
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
		return pdStPubService.savePdSt(stvo,user,"");
	}
	/**
	 * 审核出库单
	 * @param param
	 * @param user
	 */
	public void submitPdSt(String param,IUser user){
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		User u =(User)user;
		if(stvo == null ||stvo.getDtlist() == null||stvo.getDtlist().size()<=0) 
			throw new BusException("未获取到出库单信息！");
		String pk_store = stvo.getPkStoreSt();
		String eu_outtype = stOutHandler.queryEuOutType(pk_store);
		//手工录入的出库明细，先删除出库明细，重新生成出库明细单并插入
		//如果录入了申领仓库的，需要校验 对方仓库是否包含此物品，若不包含，则不允许出库。
		List<PdStDtVo> dtlist_new  = new ArrayList<PdStDtVo>();
		List<String> pkPds = new ArrayList<String>();
		List<PdStDtVo> qryUnFinishedPd = new ArrayList<>();
		for(int i = 0 , len = stvo.getDtlist().size() ; i < len ; i++){
			PdStDtVo vo = stvo.getDtlist().get(i);
			pkPds.add(vo.getPkPd());
			if(StringUtils.isNotBlank(vo.getPkPdPlandt())){
				qryUnFinishedPd.add(vo);
			}
		}
		if(!CommonUtils.isEmptyString(stvo.getPkStoreLk())){
			List<Map<String,Object>> pdlist = pdStPubService.verfyPdIsInStore(stvo.getPkStoreLk(), stvo.getPkDeptLk(), pkPds);
	    	//传入的调拨明细与仓库中获取到的物品数量不等，则说明存在当前仓库不存在的物品，需要提示
	    	if(pdlist==null||pdlist.size()!=pkPds.size()){
	    		StringBuilder pdName = new StringBuilder("");
	    		for(PdStDtVo dt:stvo.getDtlist()){
	    			boolean hasFlag = false;
	    			for(Map<String,Object> pdMap:pdlist){
	    				if(dt.getPkPd().equals(CommonUtils.getString(pdMap.get("pkPd")))){
	        				hasFlag = true;
	        				break;
	        			}
	    			}
	    			if(!hasFlag){
	    				pdName.append("【").append(dt.getPdname()).append("】\n");
	    			}
	    		}
	    		if(pdName.toString().length()>4)
	    			throw new BusException(""+pdName.toString()+"在申领仓库物品字典中不存在，\n需先添加后方可进行出库！");
	    	}
		}
		  //加锁物品
		  stOutHandler.lockConfirmPdSt(pkPds,pk_store);
		  StringBuilder errpd = new StringBuilder();
		  int i = 1;
		  for(PdStDtVo vo:stvo.getDtlist()){
				double price = MathUtils.mul(MathUtils.div(vo.getPrice(),vo.getPackSize().doubleValue()), vo.getPackSizePd().doubleValue());
				price=BigDecimal.valueOf(price).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
				double priceCost = MathUtils.mul(MathUtils.div(vo.getPriceCost(), vo.getPackSize().doubleValue()),vo.getPackSizePd().doubleValue());
				priceCost=BigDecimal.valueOf(priceCost).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
			     Map<String,Object> confirmMap = stOutHandler.confirmPdSt(vo.getPkPd(), null, pk_store,vo.getQuanMin(), eu_outtype, price,priceCost, vo.getBatchNo(),vo.getDateExpire());
			     List<PdOutDtParamVo> confirmlist = new ArrayList<PdOutDtParamVo>();

			     //无法匹配到出库批次的药品改为批量提示
			     if(confirmMap!=null){
					 if(confirmMap.get("dtlist")!=null) {
						 confirmlist = (List<PdOutDtParamVo>) confirmMap.get("dtlist");
					 }else if(confirmMap.get("errpd")!=null){
						 errpd.append(confirmMap.get("errpd").toString()).append(",");
					 }
				 }
				if (confirmlist != null && confirmlist.size() > 0) {
					for (PdOutDtParamVo paramVo : confirmlist) {
						PdStDtVo newdt = new PdStDtVo();
						newdt.setPkDtin(paramVo.getPkPdstdt());
						newdt.setPkPdstdt(NHISUUID.getKeyId());
						newdt.setPkPd(paramVo.getPkPd());
						newdt.setBatchNo(paramVo.getBatchNo());
						newdt.setDateExpire(paramVo.getDateExpire());
						newdt.setDateFac(paramVo.getDateFac());
						newdt.setDisc(vo.getDisc());
						newdt.setFlagFinish(vo.getFlagFinish());
						newdt.setFlagPay(vo.getFlagPay());
						newdt.setNote(vo.getNote());
						newdt.setPackSize(paramVo.getPackSize());
						newdt.setQuanPack(paramVo.getQuanOutPack());
						newdt.setQuanMin(paramVo.getQuanOutMin());
						newdt.setPkPdst(stvo.getPkPdst());
						newdt.setSortNo(i);
						newdt.setPkUnitPack(paramVo.getPkUnitPack());
						newdt.setPrice(paramVo.getPrice());
						newdt.setPriceCost(paramVo.getPriceCost());
						newdt.setPackSizePd(paramVo.getPackSizePd());
						double pack_size = CommonUtils.getDouble(paramVo.getPackSizePd());
						newdt.setAmount(MathUtils.mul(newdt.getQuanMin(),MathUtils.div(newdt.getPrice(), pack_size)));
						newdt.setAmountCost(MathUtils.mul(newdt.getQuanMin(), MathUtils.div(newdt.getPriceCost(), pack_size)));
						newdt.setTs(new Date());
						newdt.setCreator(u.getPkEmp());
						newdt.setCreateTime(new Date());
						newdt.setPkPdPlandt(vo.getPkPdPlandt());
						newdt.setPkPdstdtRl(paramVo.getPkPdstdt());
						newdt.setPriceOrg(paramVo.getPrice());
						newdt.setPriceCostOrg(paramVo.getPriceCost());
						i++;
						dtlist_new.add(newdt);
					}
				}
			}
		  if(errpd.toString()!=null&&errpd.toString().length()>1){
			  throw new BusException("未获取到与物品"+errpd.toString()+"相匹配的可用入库记录，无法完成出库操作！");
		  }
		//}
		if(dtlist_new!=null&&dtlist_new.size()>0){
			//删除原来的记录
			DataBaseHelper.execute("delete from pd_st_detail where pk_pdst = ? ", new Object[]{stvo.getPkPdst()});
			//插入新的明细记录
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdStDetail.class), dtlist_new);
			stvo.setDtlist(dtlist_new);
		}else{
			throw new BusException("未获取到符合条件的入库明细记录，无法完成出库操作！");
		}
		if(!CommonUtils.isEmptyString(stvo.getPkPdplan())){//通过调拨申请
			stvo.setEuStatus("0");
			stvo.setFlagChk("0");
			pdStPubService.savePdSt(stvo,user,"");
		}
		//更新库存量，并更新入库明细
		pdStPubService.updateOutStore(stvo.getDtlist(),((User)user).getPkStore());

		String pk_pdst = stvo.getPkPdst();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkPdst", pk_pdst);
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
		paramMap.put("dateChk", new Date());
		//更新出库信息
		pdStPubService.updatePdSt(paramMap);
		if(!CommonUtils.isEmptyString(stvo.getPkPdplan())){
			//更新调拨申请单明细对应的状态
			updatePlanDt(stvo.getDtlist(),u);
		}
		ExtSystemProcessUtils.processExtMethod("HRPSERVICE", "sendReturnBackDrug",pk_pdst);
	}

	/**
	 * 生成缺药计划
	 * @param param{List<PdPlanDtVo>}
	 * @param user
	 */
	public void genDrugPlan(String param,IUser user){
		List<PdPlanDtVo> dtlist = JsonUtil.readValue(param, ArrayList.class);
	    if(dtlist == null || dtlist.size()<=0)
	    	throw new BusException("未获取到缺药物品明细!");
	    User u = (User)user;
	    //生成计划
	    PdPlan plan = this.createPlan(u);
	    DataBaseHelper.insertBean(plan);
	    String pk_plan = plan.getPkPdplan();
	    //生成计划明细
	    List<PdPlanDetail> insert_list = new ArrayList<PdPlanDetail>();
	    int i = 1;
	    for(PdPlanDtVo dt:dtlist){
	    	ApplicationUtils.setBeanComProperty(dt, true);
	    	dt.setPkPdplandt(NHISUUID.getKeyId());
	    	dt.setPkPdplan(pk_plan);
	    	dt.setFlagFinish("0");
	    	dt.setQuanDe(0.00);
	    	dt.setDateNeed(new Date());
	    	dt.setSortNo(i);
			i++;
			insert_list.add(dt);
	    }
	    if(insert_list!=null&&insert_list.size()>0){
	    	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdPlanDetail.class), insert_list);
	    }
	}
	
	/**
	 * 构建采购计划单
	 * @param u
	 * @return
	 */
	private PdPlan createPlan(User u){
		PdPlan plvo = new PdPlan();
		plvo.setCodePlan(ApplicationUtils.getCode("采购计划单"));//未注册
		plvo.setDatePlan(new Date());
		plvo.setDtPlantype(IDictCodeConst.DT_PLANTYPE_BUY);
		plvo.setEuStatus("0");
		plvo.setFlagAcc("0");
		plvo.setNameEmpMak(u.getNameEmp());
		plvo.setNamePlan(DateUtils.getDateStr(new Date())+"缺药计划");
		plvo.setPkDept(u.getPkDept());
		plvo.setPkEmpMak(u.getPkEmp());
		plvo.setPkOrg(u.getPkOrg());
		plvo.setPkStore(u.getPkStore());
		return plvo;
	}
	/**
	 * 构造出库明细记录
	 * @param pk_pd
	 * @param pk_store
	 * @param quan_min
	 * @param namepd
	 * @param codepd
	 * @param spec
	 * @param spcode
	 * @param factory
	 * @param unitname
	 * @param pk_pdplandt
	 * @return
	 */
	private  List<PdStDtVo> genStDt(String pk_pd,String pk_store,double quan_min,
			String codepd,String namepd,String spec,String spcode,String factory,String unitname,String pk_pdplandt,String unitPd){
		String eu_outtype = stOutHandler.queryEuOutType(pk_store);
    	List<PdStDtVo> dtlist = new ArrayList<PdStDtVo>();
    	int i = 1;
		List<PdOutDtParamVo> list = stOutHandler.confirmPdSt(pk_pd,pk_store,quan_min,eu_outtype);
	    if(list!=null&&list.size()>0){
	    	for(PdOutDtParamVo vo:list){
	    		PdStDtVo dtvo = new PdStDtVo();
	    		dtvo.setSortNo(i);
	    		dtvo.setPkPdPlandt(pk_pdplandt);
	    		i++;
	    		dtvo.setPkPd(pk_pd);
	    		dtvo.setPdcode(codepd);
	    		dtvo.setPdname(namepd);
	    		dtvo.setSpec(spec);
	    		dtvo.setSpcode(spcode);
	    		dtvo.setFactory(factory);
	    		dtvo.setUnit(unitname);
	    		dtvo.setUnitPd(unitPd);
	    		dtvo.setPkUnitPack(vo.getPkUnitPack());
	    		dtvo.setPackSize(vo.getPackSize());
	    		dtvo.setPosiNo(vo.getPosiNo());
	    		
	    		dtvo.setQuanMin(vo.getQuanOutMin());
	    		dtvo.setQuanPack(vo.getQuanOutPack());
	    		dtvo.setBatchNo(vo.getBatchNo());
	    		dtvo.setDateExpire(vo.getDateExpire());
	    		dtvo.setDateFac(vo.getDateFac());
	    		dtvo.setPrice(vo.getPrice());//零售单位下的单价--为了求库存量使用
	    		dtvo.setPriceCost(vo.getPriceCost());//零售单位下的单价--为了求库存量使用
	    		dtvo.setPriceOrg(vo.getPrice());//为了审核时确认批次使用
	    		dtvo.setPriceCostOrg(vo.getPriceCost());//为了审核时确认批次使用
	    		//求对应库存量的库量
	    		Map<String,Object> quanMap = pdStPubService.verfiyExistPD(dtvo,pk_store,"0");
	    		if(quanMap!=null){
		    		double quan = CommonUtils.getDouble(quanMap.get("quanMin"));
		    		double pre = CommonUtils.getDouble(quanMap.get("quanPrep"));
		    		dtvo.setQuanStk(MathUtils.div(MathUtils.sub(quan, pre), CommonUtils.getDouble(vo.getPackSize())));
		    		//double pack_size = CommonUtils.getDouble(stOutHandler.getPackSize(pk_pd));
		    		//dt.price/pd.pack_size*store.pack_size
		    		//重新设置单价为当前仓库对应单位的单价
	    		}else{
	    			dtvo.setQuanStk(0D);
	    		}
	    		dtvo.setPrice(MathUtils.mul(MathUtils.div(vo.getPrice(),CommonUtils.getDouble(vo.getPackSizePd())),CommonUtils.getDouble(vo.getPackSize())));
	    		dtvo.setPriceCost(MathUtils.mul(MathUtils.div(vo.getPriceCost(),CommonUtils.getDouble(vo.getPackSizePd())),CommonUtils.getDouble(vo.getPackSize())));
	    		dtvo.setPackSizeMax(MathUtils.div(MathUtils.mul(CommonUtils.getDouble(vo.getPackSizeMax()),CommonUtils.getDouble(vo.getPackSizePd())),CommonUtils.getDouble(vo.getPackSize())).intValue());
	    		dtvo.setPackSizePd(vo.getPackSizePd());
	    		dtvo.setAmount(MathUtils.mul(MathUtils.div(vo.getPrice(),CommonUtils.getDouble(vo.getPackSizePd())), dtvo.getQuanMin()));
	    		dtvo.setAmountCost(MathUtils.mul(MathUtils.div(vo.getPriceCost(),CommonUtils.getDouble(vo.getPackSizePd())),dtvo.getQuanMin()));
	    		dtvo.setPkDtin(vo.getPkPdstdt());//对应入库明细
	    		dtlist.add(dtvo);
	    	}
	    }
	    return dtlist;
	}
	/**
	 * 转换加锁对象
	 * @param stlist
	 * @param planlist
	 * @return
	 */
	private List<PdOutParamVo> cvtPdList(List<PdStDtVo> stlist,List<PdPlanDtVo> planlist){
		List<PdOutParamVo> list = new ArrayList<PdOutParamVo>();
		if(stlist!=null&&stlist.size()>0){
			for(PdStDtVo vo :stlist){
				PdOutParamVo param = new PdOutParamVo();
				param.setPkPd(vo.getPkPd());
				list.add(param);
			}
		}else{
            for(PdPlanDtVo vo :planlist){
            	PdOutParamVo param = new PdOutParamVo();
				param.setPkPd(vo.getPkPd());
				list.add(param);
			}
		}
		return list;
	}
	/**
	 * 更新调拨明细
	 * @param stlist
	 */
	private void updatePlanDt(List<PdStDtVo> stlist,User u){
		if(stlist == null ||stlist.size()<0) return;
		String pk_plandt = "";
		//相同的pk_plandt做累加，只执行一次
		Map<String,Double> mapTotal = new HashMap<>();
		List<Map<String,Object>> updateParams = new ArrayList<>();
		Set<String> planSet = new HashSet<String>();
		for(PdStDtVo dtvo : stlist){
			pk_plandt = dtvo.getPkPdPlandt();
			if(CommonUtils.isNull(pk_plandt)) {
				continue;
			}
			if(mapTotal.put(pk_plandt, MathUtils.add(MapUtils.getDoubleValue(mapTotal,pk_plandt), dtvo.getQuanMin())) == null){
				Map<String,Object> paramMap = new HashMap<>();
				paramMap.put("quanDe",MapUtils.getDoubleValue(mapTotal,pk_plandt));
				paramMap.put("pkEmp",u.getPkEmp());
				paramMap.put("nameEmp",u.getNameEmp());
				paramMap.put("current",DateUtils.getDefaultDateFormat().format(new Date()));
				paramMap.put("pkPdplandt",pk_plandt);
				updateParams.add(paramMap);

				PdPlanDetail dt =  DataBaseHelper.queryForBean("select pk_pdplan from pd_plan_detail where pk_pdplandt = ?", PdPlanDetail.class, new Object[]{pk_plandt});
				if(CommonUtils.isNotNull(dt.getPkPdplan())) {
					planSet.add(dt.getPkPdplan());
				}
			} else {
				//多条记录时，重新给数量赋值,将最终累加的数量赋值给quanDe
				String finalpkdt = pk_plandt;
				updateParams.stream().filter(map -> StringUtils.equals(MapUtils.getString(map,"pkPdplandt"), finalpkdt)).findFirst().get().put("quanDe",MapUtils.getDoubleValue(mapTotal,pk_plandt));
			}
		}
		if(updateParams.size()>0){
			String update_sql = " update pd_plan_detail set quan_de = nvl(QUAN_DE,0)+:quanDe,pk_emp_de =:pkEmp,flag_finish = (case when nvl(QUAN_DE,0)+:quanDe>=QUAN_MIN then '1' else flag_finish end)"
					+",name_emp_de = :nameEmp,date_de = to_date(:current,'YYYYMMDDHH24MISS') ,ts = to_date(:current,'YYYYMMDDHH24MISS') where pk_pdplandt =:pkPdplandt";
			DataBaseHelper.batchUpdate(update_sql,updateParams);
		}
		if(planSet!=null&&planSet.size()>0){
			//submit方法中有锁且，加了数量判断，这里加上状态为2的没问题
			String sqlPlan="update pd_plan set eu_status='2',flag_acc='1'  where (pk_pdplan in ("+CommonUtils.convertSetToSqlInPart(planSet,"pk_pdplan")+") )and eu_status in('1','2')";
			int countPlan=DataBaseHelper.execute(sqlPlan,new Object[]{});
			if(countPlan!=planSet.size()){
				throw new BusException("您所提交的数据已发生改变，请刷新数据，或返回初始列表");
			}
		}
	}
}
