package com.zebone.nhis.ex.pub.service;

import java.text.ParseException;
import java.util.*;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.wf.BdWfOrdArguDept;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.dao.ExListPubMapper;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.support.OrderCalPdQuanHandler;
import com.zebone.nhis.ex.pub.support.OrderSortUtil;
import com.zebone.nhis.ex.pub.support.QueryOrdWfService;
import com.zebone.nhis.ex.pub.vo.BdWfOrdArguDeptVo;
import com.zebone.nhis.ex.pub.vo.BdWfOrdArguVo;
import com.zebone.nhis.ex.pub.vo.GenerateExLisOrdVo;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 医嘱执行单服务
 * @author yangxue
 *
 */
@Service
public class CreateExecListService {
	
	
	private Logger logger = LoggerFactory.getLogger("com.zebone");

	@Resource
	private ExListPubMapper execListMapper;
	
	@Resource  
	private QueryOrdWfService queryOrdWfService;
	
	 /**
     * 根据核对的医嘱列表，生成执行单
     * @param param{pkOrg,pkDept,pkEmp,nameEmp,ordsnParents}
     * 说明{当前操作人组织，当前操作人部门，当前操作人pk，当前操作人姓名，父医嘱号字符串‘’，‘’形式}
	 * @throws ParseException 
	 * @throws BusException 
     */
    public String createExecList(Map<String,Object> param) throws BusException{
    	//获取系统参数中的生成执行单天数
    	String endDays = ExSysParamUtil.getExListDays();
    	//获取系统参数中的生成执行单截止时刻
    	String endTime = ExSysParamUtil.getExListEndTime();
    	
    	//重新拼成截止时间  
    	Date endDate = DateUtils.getSpecifiedDay(new Date(), CommonUtils.getInt(endDays));
    	//SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    	//SimpleDateFormat sft = new SimpleDateFormat("HHmmss");
		String endDateTime = DateUtils.getDateStr(endDate)+endTime;
		//logger.info("执行天数："+endDays+" 截止执行时刻："+endTime +" 计算后的截止时间："+endDateTime);
		param.put("end", endDateTime);
    	return exec(param);
    }
    /**
     * 根据医嘱列表，生成执行单
     * @param param{ordsnParents,end,pkOrds}
     * 说明{截止时间，父医嘱号字符串'',''形式，医嘱号字符串拼接形式}
	 * @throws ParseException 
	 * @throws BusException 
     */
    public String exec(Map<String,Object> param) throws BusException{
    	//param.put("pkDeptNs", "af2cb09122e941b0a473eb7bede69d16");
    	//根据父医嘱号列表或医嘱主键，查询需要生成执行单的非药品医嘱列表
    	
    	List<GenerateExLisOrdVo> ords = execListMapper.getGenExecPdOrdListByP(param);
    	if(ords == null||ords.size() == 0){
			return "没有得到可生成执行单的医嘱!";
		}
    	//获取系统参数中的物品绑定模式
    	String isExecband = ExSysParamUtil.getBindModeParam();
    	//创建医嘱流向缓存
    	List<BdWfOrdArguVo> wf_list = new ArrayList<BdWfOrdArguVo>();
    	//分组医嘱
    	Map<String,List<GenerateExLisOrdVo>> map = OrderSortUtil.groupOrdBySortnoP(ords);
    	
//    	发送执行单至平台，2020.3.20注释
//    	List<ExOrderOcc> allOccList = new ArrayList<ExOrderOcc>();
    	
        //根据同组医嘱生成执行记录
    	for(List<GenerateExLisOrdVo> ordList : map.values()){
    		List<ExOrderOcc> occlist = createBySameGroupOrd(param, ordList, wf_list,isExecband);
//    		if(occlist!=null&&occlist.size()>0){
//    			allOccList.addAll(occlist);
//    		}
    	}	
//    	if(allOccList!=null&&allOccList.size()>0&&"1".equals(ApplicationUtils.getPropertyValue("msg.send.switch", "0"))
//    			&&"1".equals(ApplicationUtils.getPropertyValue("msg.send.exlist", "0"))){
//    		//发送生成的执行单信息至平台
//    		Map<String,Object> msgMap = new HashMap<String,Object>();
//    		msgMap.put("occlist", allOccList);
//    		msgMap.put("IsSendSD", true);
//    		PlatFormSendUtils.sendAddExOrderOccMsg(msgMap);
//    	}
    	
    	return "";
    }
    
    /**
     * 根据指定的时间区间生成执行单
     * @param param{dateStart,end,flagFirst,flagStop}
     * @return
     */
    public List<ExOrderOcc> createExListBySpecialTime(Map<String,Object> param){
    	GenerateExLisOrdVo ordVO = execListMapper.getGenExecPdOrdListByTime(param);
    	if(ordVO == null) return null;
    	//获取系统参数中的物品绑定模式
    	String isExecband = ExSysParamUtil.getBindModeParam();
    	//创建医嘱流向缓存
    	List<BdWfOrdArguVo> wf_list = new ArrayList<BdWfOrdArguVo>();
		BdWfOrdArguDeptVo wfVO = null;
		if("1".equals(isExecband)){//执行绑定
			wfVO = this.getExecDeptInfoByWf(ordVO, wf_list);
			if(null == wfVO){
				throw new BusException("没有得到对应医嘱的正确执行科室!");
			}
		}else{//开立绑定
			wfVO = new BdWfOrdArguDeptVo();
			getExecDeptInfoByOrd(ordVO, wfVO);
		}
		//logger.info("医嘱名称："+ordVO.getNameOrd()+":"+ordVO.getPkCnord());
		//根据首次，末次标志处理医嘱的开始和停止时间
		if("1".equals(CommonUtils.getString(param.get("flagFirst")))){
			//将开始时间前移至当日凌晨
			ordVO.setDateStart(DateUtils.strToDate(CommonUtils.getString(param.get("dateStart"))));
			ordVO.setDateLastEx(null);//以截止时间为准
		}
		if("1".equals(CommonUtils.getString(param.get("flagStop")))){
			//将停止时间推后至当日235959
			ordVO.setDateStop(DateUtils.strToDate(CommonUtils.getString(param.get("end"))));
			ordVO.setDateLastEx(DateUtils.strToDate(CommonUtils.getString(param.get("dateStart"))));
		}
		//确定生成执行的时间区间
		Map<String,Date> interval = confirmTimeInterval(ordVO, null,CommonUtils.getString(param.get("end")));
		//根据执行开始时间，结束时间，计算执行数量（非剂量）
		OrderAppExecVo exceVO = new OrderCalPdQuanHandler().calOrdQuan(interval.get("beginDate"), interval.get("endDate"), ordVO);  //exception待处理
		List<ExOrderOcc>  list = createDeptExListVO(exceVO, ordVO, wfVO);
		return list;
    }
    
    /**
	 * 由医嘱获取执行科室信息
	 * @param ordVO
	 * @param wfVO
	 */
	private void getExecDeptInfoByOrd(GenerateExLisOrdVo ordVO,
			BdWfOrdArguDeptVo wfVO) {
		wfVO.setPkDept(ordVO.getPkDeptExec());
		wfVO.setPkOrgExec(ordVO.getPkOrgExec());
	}
	
	/**
	 * 由医嘱流向计算执行科室
	 * @param ordVO
	 * @param wf_list
	 * @return
	 * @throws BusinessException
	 */
	private BdWfOrdArguDeptVo getExecDeptInfoByWf(GenerateExLisOrdVo ordVO,List<BdWfOrdArguVo> wf_list) {
		BdWfOrdArguVo vo = setWfValue(ordVO);
		BdWfOrdArguDeptVo wfVO = getWfVO(vo, wf_list);
		return wfVO;
	}
	/**
	 * 配置医嘱流向参数
	 * @param ordVO
	 */
	private BdWfOrdArguVo setWfValue(GenerateExLisOrdVo ordVO) {
		BdWfOrdArguVo vo = new BdWfOrdArguVo();
		vo.setPkOrg(ordVO.getPkOrg());
		vo.setPkSupplycate(ordVO.getPkSupplycate());
		vo.setEuPvtype(ordVO.getEuPvtype());
		vo.setOrderType(ordVO.getCodeOrdtype());
		vo.setWeeknos(DateUtils.getDayNumOfWeek(new Date())+"");
		vo.setOrdrecur(ordVO.getEuAlways());
		vo.setPkDept(ordVO.getPkDept());//开立科室
		return vo;
	}
	/**
	 * 获取医嘱流向VO
	 * @param param
	 * @param list
	 * @return
	 */
	private BdWfOrdArguDeptVo getWfVO(BdWfOrdArguVo param,List<BdWfOrdArguVo> list) {
		for (BdWfOrdArguVo vo : list){
			if(compareBdWfOrdArgu(vo,param))
				return vo.getWfDeptVO();
		}
		//若缓存里不存在匹配的医嘱流向，则根据开立科室重新计算
		BdWfOrdArguDeptVo wfVOList = queryOrdWfService.exce(param);
		BdWfOrdArguDeptVo  wfvo= null;
		if(wfVOList!=null){
			wfvo = wfVOList;
		}
		param.setWfDeptVO(wfvo);
		list.add(param);
		return wfvo;
		
	}    
	
	
	
	/**
	 * 生成执行记录并保存
	 * @param para
	 * @param ords
	 * @param wf_list
	 * @param message
	 * @param isExecBind
	 * @throws ParseException 
	 */
	private List<ExOrderOcc> createBySameGroupOrd(Map<String, Object> para,
			List<GenerateExLisOrdVo> ords, List<BdWfOrdArguVo> wf_list,String isExecBind) throws BusException{
		List<ExOrderOcc> all_list = new ArrayList<ExOrderOcc>();
		Map<String,Date> timeMap = new HashMap<String,Date>();
		for(GenerateExLisOrdVo ordVO : ords){
			BdWfOrdArguDeptVo wfVO = null;
			if("1".equals(isExecBind)){//执行绑定
				wfVO = this.getExecDeptInfoByWf(ordVO, wf_list);
				if(null == wfVO){
					throw new BusException("没有得到对应医嘱的正确执行科室!");
				}
			}else{//开立绑定
				wfVO = new BdWfOrdArguDeptVo();
				getExecDeptInfoByOrd(ordVO, wfVO);
			}
			//logger.info("医嘱名称："+ordVO.getNameOrd()+":"+ordVO.getPkCnord());
			//确定生成执行的时间区间
			Map<String,Date> interval = confirmTimeInterval(ordVO, null,CommonUtils.getString(para.get("end")));
			//根据执行开始时间，结束时间，计算执行数量（非剂量）
			OrderAppExecVo exceVO = new OrderCalPdQuanHandler().calOrdQuan(interval.get("beginDate"), interval.get("endDate"), ordVO);  //exception待处理
			timeMap.put(ordVO.getPkCnord(),exceVO.getDateEnd());
			List<ExOrderOcc>  list = createDeptExListVO(exceVO, ordVO, wfVO);
			if(null != list)
				all_list.addAll(list);
		}
		//保存数据
		saveExListVO(all_list,timeMap);
		return all_list;
	}
	/**
	 * 保存执行单记录，同时更新医嘱状态。
	 * @param list
	 * @param timeMap
	 * @return
	 * @throws BusinessException
	 */
	public void saveExListVO(List<ExOrderOcc> list,Map<String,Date> timeMap) throws BusException {
		List<String> pk_ords = new ArrayList<String>();
		//更新执行日期
		String sql_update = "update cn_order set date_last_ex = :dateEx, eu_status_ord = '3',ts=:curTime where pk_cnord = :pkCnord ";
		for(ExOrderOcc vo : list){
			String pk_cnord = vo.getPkCnord();
			if(!pk_ords.contains(pk_cnord)){
				Date time = timeMap.get(pk_cnord);
				pk_ords.add(pk_cnord);
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("dateEx", time);
				param.put("pkCnord", pk_cnord);
				param.put("curTime", new Date());
				DataBaseHelper.update(sql_update, param);
			}
			if(vo.getDatePlan()==null){
				logger.error("生成执行单时，医嘱pkCnord为"+vo.getPkCnord()+"的医嘱生成的计划执行时间为空！");
			}
				
		}
		//更新执行状态
//		String sql_up_status = "update cn_order set eu_status_ord = 3 where  pk_ord in (?)";
//		DataBaseHelper.batchUpdate(sql_up_status,"");
		//保存执行单数据
		int[] count = DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExOrderOcc.class), list);
		logger.info("=============本次生成执行单数量:"+ Arrays.stream(count).sum()+"==============");

	}
	/**
	 * 生成执行数据
	 * @param exceVO
	 * @param ordVO
	 * @param wfVO
	 * @return
	 */
	private List<ExOrderOcc> createDeptExListVO (OrderAppExecVo execVO,GenerateExLisOrdVo ordVO,BdWfOrdArguDept wfVO){
		List<OrderExecVo> exList = execVO.getExceList();
		if(null == exList || exList.size() == 0)
			return null;
		List<ExOrderOcc> voList = new ArrayList<ExOrderOcc>();
		for(OrderExecVo exec : exList){
			ExOrderOcc vo = new ExOrderOcc();
			vo.setPkExocc(NHISUUID.getKeyId());
			vo.setPkOrg(ordVO.getPkOrg());
			vo.setPkPv(ordVO.getPkPv());
			vo.setPkPi(ordVO.getPkPi());
			vo.setPkCnord(ordVO.getPkCnord());
			vo.setEuStatus("0");
			vo.setDripSpeed(ordVO.getDripSpeed()==null?0L:ordVO.getDripSpeed());
			vo.setDatePlan(exec.getExceTime());
			vo.setDateOcc(null);
			vo.setQuanOcc(exec.getQuanCur());
			vo.setPkUnit(ordVO.getPkUnit());
			vo.setPackSize(ordVO.getPackSize());
			vo.setQuanCg(ordVO.getQuanCg());
			vo.setPkUnitCg(ordVO.getPkUnitCg());
			//基数药，自备药,执行机构
			vo.setFlagSelf(ordVO.getFlagSelf());
			vo.setFlagBase(ordVO.getFlagBase());
			vo.setPkDeptOcc(wfVO.getPkDept());
			vo.setPkOrgOcc(wfVO.getPkOrgExec());
			vo.setFlagModi("0");
			vo.setFlagCanc("0");
			vo.setFlagPivas("0");
			vo.setCreateTime(new Date());
			if(UserContext.getUser()!=null){
				vo.setCreator(UserContext.getUser().getPkEmp());
			}
			vo.setDelFlag("0");
			vo.setTs(new Date());
			voList.add(vo);
		}
		return voList;
	}
	/**
	 * 确定生成执行的区间
	 * @param ordVO
	 * @param begin
	 * @param end
	 * @throws ParseException 
	 */
	private Map<String,Date> confirmTimeInterval(GenerateExLisOrdVo ordVO,Date begin_date, String end_date) throws BusException{
		
		if(null == end_date )
			throw new BusException("生成执行单截止时间获取失败，请检查！");
		Date endDate = null;
		try {
			endDate = DateUtils.getDefaultDateFormat().parse(end_date);
		} catch (ParseException e) {
			throw new BusException("转换生成执行单截止时间获取失败，请检查！");
		}	
		Map<String,Date> result = new HashMap<String,Date>();
			
		Date date_exec = ordVO.getDateLastEx();

		if(null == date_exec){
			/*执行时间为空取医嘱开始时间当做执行时间*/
			result.put("beginDate", ordVO.getDateStart());
		}else{
			/*执行时间比开始时间早，取执行时间*/
			result.put("beginDate", date_exec);
		}
		/*医嘱结束时间比结束时间早，取医嘱结束时间*/
		Date ord_end = ordVO.getDateStop();
		if(ord_end != null && ord_end.before(endDate)){
			endDate = ord_end;
		}
		//如果最终计算的结束时间，比开始时间小，则将开始时间与结束时间置为一样的内容
		//if(endDate.before((Date)result.get("beginDate"))){
		//	result.put("beginDate", endDate);
		//}
		result.put("endDate", endDate);
		
		return result;
	}
	
	
	
	
	private  boolean  compareBdWfOrdArgu(BdWfOrdArguVo src,BdWfOrdArguVo des){
		if(src == null||des == null) return false;
        String pk_org = src.getPkOrg()==null?"":src.getPkOrg();
        String dt_sp = src.getPkSupplycate()==null?"":src.getPkSupplycate();
        String pvtype = src.getEuPvtype()==null?"":src.getEuPvtype();
        String ordtype = src.getOrderType()==null?"":src.getOrderType();
        String ordre = src.getOrdrecur()==null?"":src.getOrdrecur();
        String pk_dept = src.getPkDept()==null?"":src.getPkDept();
        String weeknos = src.getWeeknos()==null?"":src.getWeeknos();
		if(pk_org.equals(des.getPkOrg())&&
				dt_sp.equals(des.getPkSupplycate())&&
				weeknos.equals(des.getWeeknos())&&
				pvtype.equals(des.getEuPvtype())&&
				ordtype.equals(des.getOrderType())&&
				ordre.equals(des.getOrdrecur())&&
				pk_dept.equals(des.getPkDept())){
			return true;
		}
		return false;
	}

    
}
