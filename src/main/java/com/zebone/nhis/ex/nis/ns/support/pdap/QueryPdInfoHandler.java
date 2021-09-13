package com.zebone.nhis.ex.nis.ns.support.pdap;

import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ex.nis.ns.vo.OrderExListVo;
import com.zebone.nhis.ex.nis.ns.vo.PdHerbInfoVo;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 设置执行数量
 * @author yangxue
 *
 */
public class QueryPdInfoHandler {
	private List<GeneratePdApExListVo> ordList;
	private boolean isExBind;
    private boolean isHerb;
	private String paramSecond;
    private boolean isSplit; //= "1".equals(ApplicationUtils.getSysparam("CN0030", false));//是否分散模式生成草药请领
	public QueryPdInfoHandler(List<GeneratePdApExListVo> ordList,boolean isSplit,String paramSecond){
		this.ordList = ordList;
		this.isSplit = isSplit;
		this.paramSecond = paramSecond;
		if(null != ordList && ordList.size() >0){
			isExBind = "1".equals(ExSysParamUtil.getBindModeParam());//1 执行绑定
			isHerb = ordList.get(0).getCodeOrdtype().equals(IOrdTypeCodeConst.DT_ORDTYPE_DRUG_HERB);
		}
	}
	
	public List<OrderExListVo> getOrdExList() throws BusException {
		//空值校验
		if(null == ordList && ordList.size() == 0){
			return null;
		}
		//生成执行记录VO
		GeneratePdApExListVo ordVO = ordList.get(0);
		//根据执行单中的静配标志分别生成请领明细
		List<GeneratePdApExListVo> pivasOrdList = new ArrayList<GeneratePdApExListVo>();//静配标志为1的执行单列表
		List<GeneratePdApExListVo> noPivasOrdList = new ArrayList<GeneratePdApExListVo>();//静配标志为0的执行单列表
		
		for(GeneratePdApExListVo execOrdVO :ordList){
			
			if("1".equals(execOrdVO.getFlagPivas())){
				pivasOrdList.add(execOrdVO);
			}else {
				noPivasOrdList.add(execOrdVO);
			}
		}
		List<OrderExListVo> list = new ArrayList<OrderExListVo>();
		String paramDay = ApplicationUtils.getSysparam("EX0059", false);//是否按天拆单
		if("1".equals(paramDay)){
			//根据执行单中的静配标志分别生成请领明细
			List<List<GeneratePdApExListVo>> pivasOrdListDay = new ArrayList<>();//静配标志为1的执行单列表
			List<List<GeneratePdApExListVo>> noPivasOrdListDay = new ArrayList<>();//静配标志为0的执行单列表
			//将执行单通过计划计划执行时间按天分组
			List<String> exPlanDate = new ArrayList<>();//存放所有的执行单日期
			for(GeneratePdApExListVo vo : ordList){
				String datePlan = DateUtils.getDateStr(vo.getDatePlan());
				if(!exPlanDate.contains(datePlan)){
					exPlanDate.add(datePlan);
				}
			}
			for(String st :exPlanDate){
				//存放按时间分组的执行单
				List<GeneratePdApExListVo> pivasOrd = new ArrayList<GeneratePdApExListVo>();//静配标志为1的执行单列表
				List<GeneratePdApExListVo> noPivasOrd = new ArrayList<GeneratePdApExListVo>();//静配标志为0的执行单列表
				for(GeneratePdApExListVo exVo : ordList){
					if(st.equals(DateUtils.getDateStr(exVo.getDatePlan()))){
						if("1".equals(exVo.getFlagPivas())){
							pivasOrd.add(exVo);
						}else {
							noPivasOrd.add(exVo);
						}
					}
				}
				if(pivasOrd.size()>0){
					pivasOrdListDay.add(pivasOrd);
				}
				if(noPivasOrd.size()>0){
					noPivasOrdListDay.add(noPivasOrd);
				}
			}
			if("1".equals(paramSecond)){
				//静配按顿生成
				for(GeneratePdApExListVo vo :pivasOrdList){
					OrderExListVo exceVO = creatExecVO(ordVO, Arrays.asList(vo));
					exceVO.setPackSize(ordVO.getPackSize());//用量单位对应的包装量
					exceVO.setPackSizeStore(ordVO.getPackSizeStore());//仓库包装量
					exceVO.setPkUnit(ordVO.getPkUnit());//用量单位
					exceVO.setPkUnitStore(ordVO.getPkUnitStore());//仓库单位
					exceVO.setFlagPivas("1");
					exceVO.setParamDay(paramDay);
					exceVO.setDatePlan(vo.getDatePlan());
					list.add(exceVO);
				}
			}else{
				//按天生成
				for(List<GeneratePdApExListVo> listDay :pivasOrdListDay){
					if(listDay!=null&&listDay.size()>0){
						OrderExListVo exceVO = creatExecVO(ordVO,listDay);
						exceVO.setPackSize(ordVO.getPackSize());//用量单位对应的包装量
						exceVO.setPackSizeStore(ordVO.getPackSizeStore());//仓库包装量
						exceVO.setPkUnit(ordVO.getPkUnit());//用量单位
						exceVO.setPkUnitStore(ordVO.getPkUnitStore());//仓库单位
						exceVO.setFlagPivas("1");
						exceVO.setParamDay(paramDay);
						exceVO.setDatePlan(listDay.get(listDay.size()-1).getDatePlan());
						list.add(exceVO);
					}
				}
			}
			for(List<GeneratePdApExListVo> voDay :noPivasOrdListDay){
				if(voDay!=null&&voDay.size()>0){
					OrderExListVo exceVO = creatExecVO(ordVO,voDay);
					exceVO.setPackSize(ordVO.getPackSize());//用量单位对应的包装量
					exceVO.setPackSizeStore(ordVO.getPackSizeStore());//仓库包装量
					exceVO.setPkUnit(ordVO.getPkUnit());//用量单位
					exceVO.setPkUnitStore(ordVO.getPkUnitStore());//仓库单位
					exceVO.setFlagPivas("0");
					exceVO.setParamDay(paramDay);
					exceVO.setDatePlan(voDay.get(voDay.size()-1).getDatePlan());
					if(isHerb&&!isSplit){
						return getHerbPd(ordVO, exceVO);
					}else{
						list.add(exceVO);
					}
				}
			}
		}else{
			if("1".equals(paramSecond)){
				//静配按顿生成
				for(GeneratePdApExListVo vo :pivasOrdList){
					OrderExListVo exceVO = creatExecVO(ordVO, Arrays.asList(vo));
					exceVO.setPackSize(ordVO.getPackSize());//用量单位对应的包装量
					exceVO.setPackSizeStore(ordVO.getPackSizeStore());//仓库包装量
					exceVO.setPkUnit(ordVO.getPkUnit());//用量单位
					exceVO.setPkUnitStore(ordVO.getPkUnitStore());//仓库单位
					exceVO.setFlagPivas("1");
					exceVO.setParamDay(paramDay);
					exceVO.setDatePlan(vo.getDatePlan());
					list.add(exceVO);
				}
			}else{
				if(pivasOrdList!=null&&pivasOrdList.size()>0){
					OrderExListVo exceVO = creatExecVO(ordVO,pivasOrdList);
					exceVO.setPackSize(ordVO.getPackSize());//用量单位对应的包装量
					exceVO.setPackSizeStore(ordVO.getPackSizeStore());//仓库包装量
					exceVO.setPkUnit(ordVO.getPkUnit());//用量单位
					exceVO.setPkUnitStore(ordVO.getPkUnitStore());//仓库单位
					exceVO.setFlagPivas("1");
					exceVO.setDatePlan(pivasOrdList.get(pivasOrdList.size()-1).getDatePlan());
					list.add(exceVO);
				}
			}
			if(noPivasOrdList!=null&&noPivasOrdList.size()>0){
				OrderExListVo exceVO = creatExecVO(ordVO,noPivasOrdList);
				exceVO.setPackSize(ordVO.getPackSize());//用量单位对应的包装量
				exceVO.setPackSizeStore(ordVO.getPackSizeStore());//仓库包装量
				exceVO.setPkUnit(ordVO.getPkUnit());//用量单位
				exceVO.setPkUnitStore(ordVO.getPkUnitStore());//仓库单位
				exceVO.setFlagPivas("0");
				exceVO.setDatePlan(noPivasOrdList.get(noPivasOrdList.size()-1).getDatePlan());
				if(isHerb&&!isSplit){
					return getHerbPd(ordVO, exceVO);
				}else{
					list.add(exceVO);
				}
			}
		}
		return list;
	}
	
	

	/**
	 * 草药获取物品信息（不用换算数量）
	 * @param ordVO
	 * @param exceVO
	 * @return
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	private List<OrderExListVo> getHerbPd(GeneratePdApExListVo ordVO, OrderExListVo exceVO) throws BusException{
		String pk_cnord = ordVO.getPkCnord();
		String sql = "select herb.pk_pd,herb.pk_unit,unit.name name_unit,pd.pack_size pack_size_p,pd.price,pd.spec,pd.name pdname,herb.quan,store.pk_unit as pk_unit_store,store.pack_size "+
				" from cn_ord_herb herb "+
				" inner join  ex_order_occ exlist on herb.pk_cnord = exlist.pk_cnord "+ 
				" inner join bd_pd pd on pd.pk_pd = herb.pk_pd "+
				" left join bd_pd_store store "+
				" on herb.pk_pd = store.pk_pd and store.pk_dept = exlist.pk_dept_occ "+ 
				//" left join bd_pd_convert convt "+
				//" on convt.pk_pdconvert = store.pk_pdconvert "+ 
				" left join bd_unit unit on unit.pk_unit = store.pk_unit where herb.pk_cnord = ? "; 
        List<Map<String,Object>> herblist = DataBaseHelper.queryForList(sql, new Object[]{pk_cnord});
		List<OrderExListVo> result = new ArrayList<OrderExListVo>();
		for(Map<String,Object> herb : herblist){
			//获取执行信息
			OrderExListVo exec =  new OrderExListVo();
			exec.setCode_ordtype(exceVO.getCode_ordtype());
			exec.setCount(exceVO.getCount());
			exec.setDate_end(exceVO.getDate_end());
			exec.setExce_list(exceVO.getExce_list());
			exec.setFreq(exceVO.isFreq());
			exec.setHerb(isHerb);
			exec.setPres(exceVO.isPres());
			//exec.setQuan_med(exceVO.getQuan_med());
			//exec.setQuan_total(exceVO.getQuan_total());
			PdHerbInfoVo herbvo = new PdHerbInfoVo();
			herbvo.setPkPd(CommonUtils.getString(herb.get("pkPd")));
			if(herb.get("pkUnitStore") == null)
				throw new BusException("未获取到执行药房下使用的"+herb.get("pdname")+"的单位");
			herbvo.setPkUnit(CommonUtils.getString(herb.get("pkUnitStore")));
			herbvo.setPackSize(CommonUtils.getInteger(herb.get("packSize")));
			herbvo.setPackSizeP(CommonUtils.getDouble(herb.get("packSizeP")));
			herbvo.setPrice(CommonUtils.getDouble(herb.get("price")));
			herbvo.setPdname(CommonUtils.getString(herb.get("pdname")));
			herbvo.setNameUnit(CommonUtils.getString(herb.get("nameUnit")));
			herbvo.setSpec(CommonUtils.getString(herb.get("spec")));
			exec.setHerbvo(herbvo);
			//List<OrderExecVo> exList = exceVO.getExce_list();
			double count = 0.00;
			//草药用量
			//for(OrderExecVo vo : exList){
				count =  MathUtils.mul(CommonUtils.getDouble(herb.get("quan")),CommonUtils.getDouble(ordVO.getOrds()));
			//}
			exec.setPkexoccs(exceVO.getPkexoccs());
			exec.setQuan_total(count);
			exec.setQuan_med(count);
			exec.setPkDeptOcc(exceVO.getPkDeptOcc());
			exec.setNameDeptOcc(exceVO.getNameDeptOcc());
			exec.setDatePlan(exceVO.getDatePlan());
			exec.setParamDay(exceVO.getParamDay());
			result.add(exec);
		}
		return result;
	}
	
	/**
	 * 生成执行记录VO
	 * @param ordVO
	 * @return
	 */
	private OrderExListVo creatExecVO(GeneratePdApExListVo ordVO,List<GeneratePdApExListVo> ordGroupList) {
		OrderExListVo execVO = new OrderExListVo();
		//判断是否是非变动医嘱
		String dt_frequnit = ordVO.getEuCycle();
		if("05".equals(dt_frequnit) || "06".equals(dt_frequnit)){//变动医嘱
			execVO.setFreq(false);
		}else{
			execVO.setFreq(true);
		}
		//设置执行次数
		execVO.setCount(Double.parseDouble(ordGroupList.size()+""));
		//设置单次执行信息
		List<OrderExecVo> list = new ArrayList<OrderExecVo>();
		List<String> pkexoccs = new ArrayList<>();
		//基本单位下的总数量
		Double quan_total = new Double(0);
		for(GeneratePdApExListVo ord : ordGroupList){
			pkexoccs.add(ord.getPkExocc());
			OrderExecVo vo = new OrderExecVo();
			vo.setExceTime(ord.getDateEx());
			//单次用量,转换为基本单位下的单次用量（由于处方存在开立的数量不是基本单位对应数量的情况，因此需要进行数量转换）
			if(ord.getFlagSelf().equals("1")){
				vo.setQuanCur(ord.getQuanOcc());
			}else{
				vo.setQuanCur(MathUtils.mul(ord.getQuanOcc(), ord.getPackSize()));
			}
			quan_total = MathUtils.add(quan_total, vo.getQuanCur());
			list.add(vo);
			execVO.setPkDeptOcc(ord.getPkDeptOcc());
			execVO.setNameDeptOcc(ord.getNameDeptOcc());
		}
		execVO.setPkexoccs(pkexoccs);
		execVO.setPres(!CommonUtils.isEmptyString(ordVO.getPkPres()));
		execVO.setQuan_total(quan_total);
		execVO.setHerb(isHerb);
		execVO.setExce_list(list);
		execVO.setCode_ordtype(ordVO.getCodeOrdtype());
		return execVO;
	}
}
