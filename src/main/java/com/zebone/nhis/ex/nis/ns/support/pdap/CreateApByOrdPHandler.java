package com.zebone.nhis.ex.nis.ns.support.pdap;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.scm.pub.BdPdStore;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.nis.ns.vo.OrderExListVo;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyDtVo;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyVo;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

import java.util.*;



/**
 * 生成一个医嘱的请领单
 * @author yangxue
 *
 */
public class CreateApByOrdPHandler extends CreateApHandler{

	@Override
	protected String getKeyValue(GeneratePdApExListVo vo) {
		return vo.getPkCnord();
	}

	@Override
	public List<PdApplyDtVo> createAP(Map<String,Object> param,
			List<GeneratePdApExListVo> exList, List<ExPdApply> apList,boolean isSplit) throws BusException {
		if(exList == null || exList.size() == 0){
			return null;
		}
		//本条医嘱被拆分的请领单集合
		Map<String,List<GeneratePdApExListVo>> apListMap = new HashMap<String,List<GeneratePdApExListVo>>();
		for(GeneratePdApExListVo ordInfo : exList){
			//创建请领单
			ExPdApply vo = this.getExecVO(ordInfo.getPkOrgOcc(), ordInfo.getPkDeptOcc(), apList,null,false);
			if(null == vo){
				vo = this.createPdAp(ordInfo, param);
				apList.add(vo);
			}
			List<GeneratePdApExListVo>  tmplist = new ArrayList<GeneratePdApExListVo>();
			tmplist.add(ordInfo);
			if(apListMap.size()<=0){
		    	apListMap.put(vo.getPkPdap(), tmplist);
			}else{
				boolean flagHas = false;
				for(Map.Entry<String,List<GeneratePdApExListVo>> ap : apListMap.entrySet()) {
				    if(vo.getPkPdap().equals(ap.getKey())){
				    	flagHas = true;
				    	ap.getValue().add(ordInfo);
				    	break;
				    }else{
				    	flagHas = false;
				    }
				}
				if(!flagHas){
					apListMap.put(vo.getPkPdap(), tmplist);
				}
			}
			tmplist = null;
			
		}
//		GeneratePdApExListVo ordInfo = exList.get(0);
//		//创建请领单
//		ExPdApply vo = this.getExecVO(ordInfo.getPkOrgOcc(), ordInfo.getPkDeptOcc(), apList);
//		if(null == vo){
//			vo = this.createPdAp(ordInfo, param);
//			apList.add(vo);
//		}
		List<PdApplyDtVo> result = new ArrayList<PdApplyDtVo>();
		for(Map.Entry<String,List<GeneratePdApExListVo>> ap : apListMap.entrySet()) {
			result.addAll(this.createPdApDt(ap.getValue(),getExecVO(null,null,apList,ap.getKey(),true),isSplit));
		}
		return result;
	}
	
	/**
	 * 获取请领主记录
	 * @param pk_org
	 * @param pk_dept
	 * @param apList
	 * @return
	 */
	private ExPdApply getExecVO(String pk_org,String pk_dept,List<ExPdApply> apList,String pkPdAp,boolean isPk){
		for(ExPdApply vo : apList){
			if(isPk){
				if(vo.getPkPdap().equals(pkPdAp))
					return vo;
			}else{
				if(vo.getPkDeptDe().equals(pk_dept)
						&&vo.getPkOrgDe().equals(pk_org)){
					return vo;
				}
			}
			
		}
		return null;
	}
	
	/**
	 * 
	 * @param ordVO
	 * @param param
	 * @return
	 * @throws BusException
	 */
	private ExPdApply createPdAp(GeneratePdApExListVo ordVO,Map<String,Object> param) throws BusException {
		ExPdApply vo = new ExPdApply();
		String pk_org = ordVO.getPkOrg();
		vo.setPkOrg(pk_org);
		String pk_pdap = NHISUUID.getKeyId();
		vo.setPkPdap(pk_pdap);
		vo.setEuDirect("1");
		String code = ExSysParamUtil.getAppCode();
		vo.setCodeApply(code);
		vo.setEuAptype("0");//病区领药
		String pk_emp = CommonUtils.getString(param.get("pkEmp"));
		vo.setPkDeptAp(CommonUtils.getString(param.get("pkDept")));
		vo.setPkEmpAp(CommonUtils.getString(pk_emp));
		vo.setNameEmpAp(CommonUtils.getString(param.get("nameEmp")));
		vo.setDateAp(new Date());
		vo.setPkOrgDe(ordVO.getPkOrgOcc());
		vo.setPkDeptDe(ordVO.getPkDeptOcc());
		vo.setFlagCancel("0");
		vo.setFlagFinish("0");
		vo.setEuStatus("0");
		vo.setCreateTime(new Date());
		vo.setCreator(pk_emp);
		vo.setTs(new Date());
		vo.setDelFlag("0");
		return vo;
	}
	
	
	
	/**
	 * 生成请领单明细
	 * @param ordVO
	 * @param pk_dept
	 * @param appVO
	 * @return
	 * @throws BusException
	 */
	private List<PdApplyDtVo> createPdApDt(List<GeneratePdApExListVo> ordList,ExPdApply appVO ,boolean isSplit) throws BusException {
		//校验医嘱是否合法；
		validate(ordList);

		String paramSecond = ApplicationUtils.getSysparam("EX0084", false);//是否静配按顿生成请领（一个执行单生成一个请领明细）
		//合并执行单，并计算使用物品；
		List<OrderExListVo> ex_list = new QueryPdInfoHandler(ordList,isSplit,paramSecond).getOrdExList();
		
		List<PdApplyDtVo> result = new ArrayList<PdApplyDtVo>();
		for(OrderExListVo execVO : ex_list){
			result.add(createDtByExec(ordList, appVO, execVO,isSplit));
		}
		
		return result;
	}
	
	/**
	 * 根据物品执行信息生成请领明细
	 * @param ordList
	 * @param appVO
	 * @param exceVO
	 * @return
	 * @throws BusException
	 */
	private PdApplyDtVo createDtByExec(List<GeneratePdApExListVo> ordList,
			ExPdApply appVO, OrderExListVo execVO,boolean isSplit) throws BusException {
		GeneratePdApExListVo ordVO = ordList.get(0);
		String dt_frequnit = ordVO.getEuCycle();
		//boolean isSplit = "1".equals(ApplicationUtils.getSysparam("CN0030", false));//是否分散模式生成草药请领
		boolean isHerbVerfy = execVO.isHerb()&&!isSplit;//草药模式校验
		PdApplyDtVo dt = new PdApplyDtVo();
		if(execVO.getCount().intValue() <1 || null == execVO.getExce_list()){
			return null;
		}
		if(!isHerbVerfy&&(CommonUtils.isNull(ordVO.getPackSizeP())||ordVO.getPackSizeP()==0)){
			dt.setErrMsg("【"+ordVO.getBedNo()+"】床【"+ordVO.getNamePi()+"】患者,\n【"+ordVO.getPdname()+"】在【"+execVO.getNameDeptOcc()+"】不存在,请检查！\n");
			return dt;
			//throw new BusException("【"+ordVO.getBedNo()+"】床【"+ordVO.getNamePi()+"】患者,\n【"+ordVO.getPdname()+"】在【"+execVO.getNameDeptOcc()+"】不存在,请检查！\n 如若继续请领，请先去除该患者！");
		}
		//自备药不计算数量，因此不校验包装量
		if(!isHerbVerfy&&ordVO.getFlagSelf().equals("0")&&(CommonUtils.isNull(ordVO.getPackSizeStore())||ordVO.getPackSizeStore()==0)){
			dt.setErrMsg("【"+ordVO.getBedNo()+"】床【"+ordVO.getNamePi()+"】患者,\n【"+ordVO.getPdname()+"】在【"+execVO.getNameDeptOcc()+"】不存在,请检查！\n");
			return dt;
			//throw new BusException("【"+ordVO.getBedNo()+"】床【"+ordVO.getNamePi()+"】患者,\n【"+ordVO.getPdname()+"】在【"+execVO.getNameDeptOcc()+"】不存在,请检查！\n 如若继续请领，请先去除该患者！");
		}
		if(!isHerbVerfy&&(CommonUtils.isNull(ordVO.getPrice()))){
			dt.setErrMsg("【"+ordVO.getBedNo()+"】床【"+ordVO.getNamePi()+"】患者,\n【"+ordVO.getPdname()+"】在【"+execVO.getNameDeptOcc()+"】中的价格,\n无法完成金额计算，请检查！\n");
			return dt;
			//throw new BusException("【"+ordVO.getBedNo()+"】床【"+ordVO.getNamePi()+"】患者,\n【"+ordVO.getPdname()+"】在【"+execVO.getNameDeptOcc()+"】中的价格,\n无法完成金额计算，请检查！\n如若继续请领，请先去除该患者！");
		}
		if(!isHerbVerfy&&(ordVO.getPkPd() == null || "".equals(ordVO.getPkPd()))){
			dt.setErrMsg("【"+ordVO.getBedNo()+"】床【"+ordVO.getNamePi()+"】患者,\n【"+ordVO.getPdname()+"】未能从药房物品表中获取到物品主键，\n无法生成请领单，请作废医嘱或确认药房是否存在此药品！\n");
		    return dt;
		}
			
		if(isHerbVerfy&&(execVO.getHerbvo().getPkPd()==null||"".equals(execVO.getHerbvo().getPkPd()))){
			dt.setErrMsg("【"+ordVO.getBedNo()+"】床【"+ordVO.getNamePi()+"】患者,\n【"+ordVO.getPdname()+"】未能从药房物品表中获取到物品主键，\n无法生成请领单，请作废医嘱或确认药房是否存在此药品！\n");
	        return dt;
			//throw new BusException("未能从药房物品表中获取到物品主键，无法生成请领单，请作废医嘱或确认药房是否存在此药品！！");
		}
		//请领明细表
		ExPdApplyDetail vo = new ExPdApplyDetail();
		String pk_pdapdt = NHISUUID.getKeyId();
		vo.setCreateTime(new Date());
		vo.setCreator(UserContext.getUser().getPkEmp());
		vo.setTs(new Date());
		vo.setDelFlag("0");
		vo.setEuDirect("1");
		vo.setPkCnord(ordVO.getPkCnord());
		vo.setPkOrg(appVO.getPkOrg());
		vo.setPkPdap(appVO.getPkPdap());
		vo.setPkPdapdt(pk_pdapdt);
		vo.setPkPv(ordVO.getPkPv());
		vo.setPkPres(ordVO.getPkPres());
		vo.setOrds(ordVO.getOrds());
		vo.setFlagBase(ordVO.getFlagBase());
		vo.setFlagSelf(ordVO.getFlagSelf());
		vo.setFlagPivas(execVO.getFlagPivas());
		vo.setFlagDe("0");
		vo.setFlagFinish("0");
		vo.setFlagCanc("0");
		
		vo.setFlagStop("0");
		if(execVO.isHerb()&&!isSplit){
			vo.setPkPd(execVO.getHerbvo().getPkPd()); 
			vo.setPkUnit(execVO.getHerbvo().getPkUnit());
			vo.setPackSize(execVO.getHerbvo().getPackSize());
			//单价 = 零售价格/零售单位包装量*仓库单位包装量
			vo.setPrice(MathUtils.mul(MathUtils.div(execVO.getHerbvo().getPrice(), CommonUtils.getDouble(execVO.getHerbvo().getPackSizeP())), CommonUtils.getDouble(execVO.getHerbvo().getPackSize())));
			ordVO.setPackSizeP(execVO.getHerbvo().getPackSizeP());
			ordVO.setPackSizeStore(vo.getPackSize());
			ordVO.setPdname(execVO.getHerbvo().getPdname());
			ordVO.setSpec(execVO.getHerbvo().getSpec());
			ordVO.setNameUnitStore(execVO.getHerbvo().getNameUnit());
		}else{
			vo.setPkPd(ordVO.getPkPd());
			if("1".equals(ordVO.getFlagSelf())){
				vo.setPkUnit(ordVO.getPkUnit());
				vo.setPackSize(ordVO.getPackSize().intValue());
				vo.setPrice(MathUtils.mul(MathUtils.div(ordVO.getPrice(), CommonUtils.getDouble(ordVO.getPackSizeP())),CommonUtils.getDouble(ordVO.getPackSize())));
			}else{
				vo.setPkUnit(ordVO.getPkUnitStore());
				vo.setPackSize(ordVO.getPackSizeStore());
				vo.setPrice(MathUtils.mul(MathUtils.div(ordVO.getPrice(), CommonUtils.getDouble(ordVO.getPackSizeP())),CommonUtils.getDouble(ordVO.getPackSizeStore())));
			}
		}
		if(CommonUtils.isNull(vo.getPrice())){
			dt.setErrMsg("【"+ordVO.getBedNo()+"】床【"+ordVO.getNamePi()+"】患者,\n【"+ordVO.getPdname()+"】生成请领单时未获取到价格,无法计算金额，\n请作废医嘱或确认药房是否存在此药品！\n");
		    return dt;
		}
		//自备药不计算数量，因此不校验包装量
		if("0".equals(vo.getFlagSelf())&&(CommonUtils.isNull(vo.getPackSize())||vo.getPackSize()==0)){
			dt.setErrMsg("【"+ordVO.getBedNo()+"】床【"+ordVO.getNamePi()+"】患者,\n【"+ordVO.getPdname()+"】生成请领单时未获取在仓库中使用的计量单位,\n请确认执行发药的药房是否存在此物品！\n");
		    return dt;
		}
		
		//分情况处理基本单位请领数量	
		//取整模式
		String eu_muputype = ordVO.getEuMuputype();
		String flag_medout = ordVO.getFlagMedout();//出院带药
		vo.setFlagMedout(flag_medout);
		if(execVO.isPres()){//处方,开立时已经计算为包装单位下的数量，因此不需要取整
			vo.setQuanPack(getQuanPack(execVO.getQuan_total(),vo.getPackSize()));
			//vo.setQuanPack(execVO.getQuan_total());
		}if(execVO.isHerb()){//草药
			vo.setQuanPack(getQuanPack(execVO.getQuan_total(),vo.getPackSize()));
		}else if(null != flag_medout && flag_medout.equals("1")){
			//出院带药，总量取证
			vo.setQuanPack(getQuanPack(MathUtils.upRound(execVO.getQuan_total()),vo.getPackSize()));
		}else if("05".equals(dt_frequnit)){
			//临时医嘱,规则：单次取整
			vo.setQuanPack(getQuanPack(calTotalQuanR(execVO),vo.getPackSize()));
		}else if(eu_muputype == null ){
			//取整模式为空,规则：单次取整
			vo.setQuanPack(getQuanPack(calTotalQuanR(execVO),vo.getPackSize()));
		}else if( eu_muputype.equals("0")){
			//0：单次取证
			//vo.setQuanPack(getQuanPack(calTotalQuanR(execVO),vo.getPackSize()));
			vo.setQuanPack(calTotalQuanPack(execVO,vo.getPackSize()));
		}else if(eu_muputype.equals("1"))
			//1：按批取整;
		     vo.setQuanPack(getQuanPack(Math.ceil(execVO.getQuan_total()),vo.getPackSize()));
		else if(eu_muputype.equals("2")){
			if(ordVO.getQuanBed() == null){
				throw new BusException("使用余量法发药，医嘱的床边量不能为null！");
			}
			//2:余量法，床边量
			vo.setQuanPack(getQuanPack(MathUtils.sub(execVO.getQuan_total(), ordVO.getQuanBed()),vo.getPackSize()));
		}else{
			//其他：不取整
			vo.setQuanPack(getQuanPack(execVO.getQuan_total(),vo.getPackSize()));
		}
		 vo.setQuanMin(MathUtils.mul(vo.getQuanPack(),CommonUtils.getDouble(vo.getPackSize())));
		 vo.setAmount(MathUtils.mul(vo.getQuanPack(),vo.getPrice()));
		
		//发放分类
		vo.setEuDetype(getEudetype(ordVO,vo.getQuanMin()));
		//缓存模式
		//SynExListInfoHandler info = new SynExListInfoHandler(vo,ordList, execVO);
		//PdApplyDtVo dt = new PdApplyDtVo();
		//dt.setInfo(info);
		//dt.setShowVO(createShowVO(ordVO, vo));
		//dt.setDtvo(vo);
		//非缓存模式
		
		dt.setShowVO(createShowVO(ordVO,vo,ordList,appVO,execVO));
		dt.setDtvo(vo);
		return dt;
	}
	/**
	 * 根据医嘱确定发放分类 0 药房发，1 病区发，2 不发,9外部接口
	 * @return
	 */
	public String getEudetype(GeneratePdApExListVo ordVO,Double quanmin){
		if("1".equals(ordVO.getFlagBase())) return "1";
		if("1".equals(ordVO.getFlagSelf())) return "2";
		if("2".equals(ordVO.getEuBoil())||"3".equals(ordVO.getEuBoil())) return "9";
		if(quanmin<=0) return "2";
		return "0";
	}
	/**
	 * 计算仓库单位下的发放数量
	 * @param quan_min -- 基本单位下的数量
	 * @param pack_size--仓库单位下的包装量
	 * @return
	 */
	private double getQuanPack(double quan_min,int pack_size){
		double num = MathUtils.upRound(MathUtils.div(quan_min, CommonUtils.getDouble(pack_size)));
		return num;
	}

	/**
	 * 生成界面显示的VO
	 * @param ordVO
	 * @param vo
	 * @param pdVO
	 * 后面两个参数是为了兼容非缓存模式时使用的
	 * @return PdApplyVo
	 */
	private PdApplyVo createShowVO(GeneratePdApExListVo ordVO,
			ExPdApplyDetail vo,List<GeneratePdApExListVo> ordList,ExPdApply appVO,OrderExListVo execVo) {
		PdApplyVo showVO = new PdApplyVo();
		showVO.setPkHp(ordVO.getPkInsu());
		showVO.setPkPdapdt(vo.getPkPdapdt());
		showVO.setBedNo(ordVO.getBedNo());
		showVO.setNamePi(ordVO.getNamePi());
		showVO.setOrdsn(ordVO.getOrdsn());
		showVO.setOrdsnParent(ordVO.getOrdsnParent());
		showVO.setEuAlways(ordVO.getEuAlways());
		showVO.setMedName(ordVO.getPdname());
		showVO.setPdcode(ordVO.getPdcode());
		showVO.setSpec(ordVO.getSpec());
		showVO.setQuan(vo.getQuanPack());
		showVO.setNameUnitBase(ordVO.getNameUnitStore());
		showVO.setPackSize(vo.getPackSize());
		showVO.setNamefreq(ordVO.getFreqname());
		showVO.setNamesupply(ordVO.getNamesupply());
		showVO.setPkCnord(ordVO.getPkCnord());
		showVO.setFlagEmer(ordVO.getFlagEmer());
		showVO.setFlagBase(ordVO.getFlagBase());
		showVO.setFlagPivas(execVo.getFlagPivas());
		showVO.setFlagSelf(ordVO.getFlagSelf());
		showVO.setFlagMedout(ordVO.getFlagMedout());
		showVO.setDtDispmode(vo.getEuDetype());
		showVO.setOrdsnParent(ordVO.getOrdsnParent());
		showVO.setPkDeptOcc(execVo.getPkDeptOcc());
		showVO.setNameDeptOcc(execVo.getNameDeptOcc());
		showVO.setPkPd(vo.getPkPd());
		showVO.setNameDeptExOrd(ordVO.getNameDeptExOrd());
		showVO.setPkDeptExOrd(ordVO.getPkDeptExOrd());
		showVO.setPkDeptOrd(ordVO.getPkDept());
		showVO.setFlagStop(ordVO.getFlagStop());
		showVO.setFlagOrdStop(ordVO.getFlagOrdStop());
		showVO.setFlagStopChk(ordVO.getFlagStopChk());
		showVO.setDateStop(ordVO.getDateStop());
		showVO.setEuSt(ordVO.getEuSt());
		showVO.setDatePlan(execVo.getDatePlan());
		showVO.setNameUnitOrd(ordVO.getNameUnitOrd());
		showVO.setPackSizeOrd(ordVO.getPackSizeOrd());
		//showVO.setDatePlan(ordVO.getDateEx());
		//设置仓库停用标志
		if(!"1".equals(showVO.getFlagPivas())){
			showVO.setFlagStoreStop(ordVO.getFlagStoreStop());
		}else{//按新执行科室获取仓库物品是否停用标志
			BdPdStore store = DataBaseHelper.queryForBean("select flag_stop from bd_pd_store where pk_pd = ? and pk_dept = ? ", BdPdStore.class, new Object[]{vo.getPkPd(),execVo.getPkDeptOcc()});
		    showVO.setFlagStoreStop(store==null?"":store.getFlagStop());
		}
		
		
		//非缓存模式，设置要更新的执行单主键
		List<String> pk_exLists = new ArrayList<String>();
		if("1".equals(execVo.getParamDay())){
			pk_exLists = execVo.getPkexoccs();
		}else{
			for(GeneratePdApExListVo ex : ordList){
				pk_exLists.add(ex.getPkExocc());
			}
		}

		showVO.setPkExLists(pk_exLists);
		
		showVO.setApdt(vo);
		showVO.setAp(appVO);
		return showVO;
	}
	
	/**
	 * 计算合计
	 * @param vo
	 * @return
	 */
	private Double calTotalQuanR(OrderExListVo vo){
		List<OrderExecVo> list = vo.getExce_list();
		Double quan_min = new Double(0);
		for(OrderExecVo ex:list){
			quan_min = MathUtils.add(quan_min, MathUtils.upRound(ex.getQuanCur())) ;
		}
		return quan_min;
	}
	/**
	 * 计算合计
	 * @param vo
	 * @return
	 */
	private Double calTotalQuanPack(OrderExListVo vo,int pack_size){
		List<OrderExecVo> list = vo.getExce_list();
		Double quan_pack = new Double(0);
		for(OrderExecVo ex:list){
			quan_pack = MathUtils.add(quan_pack, MathUtils.upRound(MathUtils.div(ex.getQuanCur(), CommonUtils.getDouble(pack_size)))) ;
		}
		return quan_pack;
	}
	/**
	 * 校验医嘱是否合理
	 * @param ordVO
	 * @return
	 * @throws BusException 
	 */
	private void validate(List<GeneratePdApExListVo> list) throws BusException{
		for(GeneratePdApExListVo ordVO : list){
			if(null == ordVO.getQuanOcc()){
				throw new BusException(ordVO.getPdname()+"医嘱定义有误，数量未定义！");
			}
			
			if(CommonUtils.isEmptyString(ordVO.getCodeOrdtype())){
				throw new BusException(ordVO.getPdname()+"医嘱定义有误，医嘱类型未定义！");
			}
		}
	}
	
	/**
	 * 记录错误信息
	 * @param vo
	 * @return
	 */
	@Override
	public String getErrorMsg(GeneratePdApExListVo vo) {
		return vo.getNamePi() + vo.getPdname() +"医嘱生成有误，请查看！";
	}
}
