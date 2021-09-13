package com.zebone.nhis.ex.nis.ns.service;

import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.pv.PvAdt;
import com.zebone.nhis.common.service.CnNoticeService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.nhis.ex.nis.pub.service.NsPubService;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
/**
 * 无事物的医嘱执行单相关的组合服务
 * @author yangxue
 *
 */

@Service
public class OrderExecListHandler{
	@Resource
	private OrderCheckService orderCheckService;
	@Resource
	private OrderExecListService orderExecListService;
	@Resource
	private OrderNsService orderNsService;
	@Resource
	private NsPubService nsPubService;
	@Autowired
	private BdSnService snService ;
	
	@Resource
	private OrderCheckPdApplyService applyService;
	
	@Resource 
	private OrderAutoCgService orderAutoCgService;//自动记费
	
	@Resource 
	private CnNoticeService cnNoticeService;//临床消息提醒
	private Logger logger = LoggerFactory.getLogger("com.zebone");
	/**
	 * 医嘱核对
	 * @param param{list}
	 * @param user
	 * @param 是否自动创建执行单
	 * @param 是否自动计费
	 * @return
	 */
    public String checkOrd(String param, IUser user){
    	//核对医嘱,是否自动生成执行单，是否自动计费
    	List<OrderCheckVo> checkList = JsonUtil.readValue(param,new TypeReference<List<OrderCheckVo>>(){});
    	if(checkList == null||checkList.size()<=0) throw new BusException("未获取到核对数据！");
    	List<OrderCheckVo> check_list= new ArrayList<OrderCheckVo>();
    	//List<String> stop_list= new ArrayList<String>();
    	//List<String> erase_list= new ArrayList<String>();
    	List<String> check_pklist= new ArrayList<String>();
    	List<String> stop_pklist= new ArrayList<String>();
    	List<String> erase_pklist= new ArrayList<String>();
    	List<OrderCheckVo> stop_list = new ArrayList<OrderCheckVo>();
    	List<OrderCheckVo> erase_list = new ArrayList<OrderCheckVo>();
    	Set<String> dietPkPvs = new HashSet<String>();;
    	//根据医嘱状态，区分医嘱是待核对，还是停止核对
    	for(OrderCheckVo vo:checkList){
    		//String pno = vo.getOrdsnParent()+"";
    		String pk_cnord = vo.getPkCnord();
    		//饮食医嘱取患者饮食医嘱等级写入到pv_ip.dt_dietary
    		if("13".equals(vo.getCodeOrdtype())){
    			dietPkPvs.add(vo.getPkPv());
    		}
    		if("1".equals(vo.getEuStatusOrd())){
    			check_list.add(vo);
    			check_pklist.add(pk_cnord);
    			
    		}else{
    			if("1".equals(vo.getFlagStop())&&"0".equals(vo.getFlagStopChk())){//停止待核对
    				//stop_list.add(pno);
    				stop_pklist.add(pk_cnord);
    				stop_list.add(vo);
    			}else if("1".equals(vo.getFlagErase())&&"0".equals(vo.getFlagEraseChk())){
    				erase_list.add(vo);
    				erase_pklist.add(pk_cnord);
    			}
    		}
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkDept", vo.getPkDeptNsCur());
			paramMap.put("pkCnorder", pk_cnord);
			//核对后更新临床消息提醒数量
			cnNoticeService.updateChkCnNotice(paramMap);
    	}
    	User u = (User)user;
    	Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkOrg", u.getPkOrg());
		paramMap.put("pkDept", u.getPkDept());
		paramMap.put("pkDeptNs",u.getPkDept());
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());

    	// orderCheckService.updateOrdState(check_pklist,stop_pklist,erase_pklist,u,checkList.size());
    	orderCheckService.updateOrdStateByTs(check_list,check_pklist,stop_list,stop_pklist,erase_list,u,checkList.size(),dietPkPvs);

		String	msg = "";
		
		try{
		    nsPubService.updatePvIpInfo(check_pklist, stop_pklist, erase_pklist);
		}catch(BusException e){
			msg = msg +" 核对时更新病情等级、护理等级、营养等级异常，请联系管理员检查！";
		}
		try{
			msg = msg + orderExecListService.processExecList(check_list,check_pklist,stop_pklist,stop_list,erase_pklist,paramMap,true);
		}catch(BusException e){
			msg  = msg + e.getMessage();
			msg =  msg + " 核对时生成执行单失败，请根据提示信息修正后到医嘱执行计划中重新生成！";
		} catch (ParseException e) {
			msg  = msg + e.getMessage();
			msg =  msg + " 核对时追加首日执行单转换日期失败，请根据提示信息修正后到医嘱执行计划中重新生成！";
		}
		//执行自动记费
		orderAutoCgService.autoCgOrder(check_list, (User)user);
		
		//CA认证信息保存  核对医嘱
		if(check_list!=null && check_list.size()>0){
			orderCheckService.caRecrdByOrd(check_list);
		}
		
		//CA认证信息保存  核对停嘱
		if(stop_list!=null && stop_list.size()>0){
			orderCheckService.caRecrdByOrd(stop_list);
		}
		//核对后临时医嘱自动请领
		try{
			if("1".equals(ApplicationUtils.getSysparam("EX0063", false))){
				msg = msg + applyService.executeApply(checkList, (User)user);
			}
		}catch(BusException e){
			msg  = msg +"核对时自动请领发生错误："+ e.getMessage();
			logger.error("====核对时自动请领发生错误："+e.getMessage());
		}
		//发送医嘱核对信息至平台
		paramMap = new HashMap<String,Object>();
		//传map格式
		paramMap.put("ordlist", JsonUtil.readValue(param,new TypeReference<List<Map<String,Object>>>(){}));
		//传vo格式
		List<OrderCheckVo> ordlistvo = JsonUtil.readValue(param,new TypeReference<List<OrderCheckVo>>(){});
		for (OrderCheckVo orderCheckVo : ordlistvo) {
			orderCheckVo.setPkOrg(u.getPkOrg());
		}
		paramMap.put("ordlistvo", ordlistvo);
		paramMap.put("control", "NW");//操作类型
		paramMap.put("ordStatus", "3");//医嘱状态
		paramMap.put("checkList", checkList);//深大需要数据
		paramMap.put("checkOrd", "");//深大发消息标志
		if (!"SyxPlatFormSendService".equals(ApplicationUtils.getPropertyValue("msg.send.switch", "0")))//孙逸仙改为医嘱签署时发送消息
		PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
		paramMap = null;
		
		//5.记费将单子发送至三方系统
		if(null != check_list && check_list.size() > 0){
			List<String> pkCnords = new ArrayList<String>();
			for (OrderCheckVo ex : check_list) {
				pkCnords.add(ex.getPkCnord());
			}
			ExtSystemProcessUtils.processExtMethod("PACS", "savePacsEx", null,pkCnords);
		}

		return msg;
    }
      /**
     * 停止核对（单独处理）
     * @param param
     * @param user
     * @return
     */
    public String stopCheckOrd(String param,IUser user){
    	//核对停止的医嘱
    	List<OrderCheckVo> checkList = JsonUtil.readValue(param,new TypeReference<List<OrderCheckVo>>(){});
    	List<String> stop_pklist= new ArrayList<String>();
    	
    	for(OrderCheckVo vo:checkList){
    		String pk_cnord = vo.getPkCnord();
    		stop_pklist.add(pk_cnord);
    	}
    	User u = (User)user;
    	Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkOrg", u.getPkOrg());
		paramMap.put("pkDept", u.getPkDept());
		paramMap.put("pkDeptNs",u.getPkDept());
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
		//停止核对的医嘱，为避免核对停止时间不在当天的情况，定时任务漏生成未生成的执行单，先对停止核对的医嘱执行生成执行单业务
    	if(stop_pklist!=null&&stop_pklist.size()>0){
    		
    		//CA认证信息保存
    		orderCheckService.caRecrdByOrd(checkList);
    		
    		try {
        		orderExecListService.createCheckStopExlist(stop_pklist,paramMap);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
    	}
    	String msg = "";
    	msg = orderExecListService.processStopOrd(checkList,paramMap,u);

    	
		try{
		    nsPubService.updatePvIpInfo(null,stop_pklist, null);
		}catch(BusException e){
			msg = msg +" 核对时更新病情等级、护理等级、营养等级异常，请联系管理员检查！";
		}

		for(OrderCheckVo vo:checkList){
			String pk_cnord = vo.getPkCnord();
			Map<String,Object> paramMs = new HashMap<String,Object>();
			paramMs.put("pkDept", vo.getPkDeptNsCur());
			paramMs.put("pkCnorder", pk_cnord);
			//核对后更新临床消息提醒数量
			cnNoticeService.updateChkCnNotice(paramMs);
		}

		//HL7SendOMPO09Msg(param);
		//HL7SendORGO20_ORLO22Msg(param);
		ExtSystemProcessUtils.processExtMethod("PIVAS", "updatePivasOutByCancelChk", param);
		
		//发送医嘱核对信息至平台
		paramMap = new HashMap<String,Object>();
		List<OrderCheckVo> ordlistvo = JsonUtil.readValue(param,new TypeReference<List<OrderCheckVo>>(){});
		for (OrderCheckVo orderCheckVo : ordlistvo) {
			orderCheckVo.setPkOrg(u.getPkOrg());
		}
		paramMap.put("ordlistvo", ordlistvo);
		List<Map<String,Object>> ordlist = JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>(){});
		paramMap.put("ordlist", ordlist);
		paramMap.put("control", "NW");//操作类型
		paramMap.put("ordStatus", "5");//医嘱状态
		PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
		
		return msg;
    }
    
    /**
     * 停止护嘱信息
     * @param param
     * @param user
     */
    public String  stopOrderNs(String param,IUser user){
    	//核对停止的医嘱
    	List<OrderCheckVo> checkList = JsonUtil.readValue(param,new TypeReference<List<OrderCheckVo>>(){});
    	List<String> stop_pklist= new ArrayList<String>();
    	User u = (User)user;
    	Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkOrg", u.getPkOrg());
		paramMap.put("pkDept", u.getPkDept());
		paramMap.put("pkDeptNs",u.getPkDept());
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
    	for(OrderCheckVo vo:checkList){
    		String pk_cnord = vo.getPkCnord();
    		stop_pklist.add(pk_cnord);
    		//先更新医嘱为停止状态
    		orderCheckService.updateOrdNsStop(pk_cnord,vo.getDateStop(),paramMap);
    	}
	
    	String msg = "";
    	msg = orderExecListService.processStopOrd(checkList,paramMap,u);
    	
    	//发送医嘱核对信息至平台
		paramMap = new HashMap<String,Object>();
		paramMap.put("ordlistvo", JsonUtil.readValue(param,new TypeReference<List<Map<String,Object>>>(){}));
		paramMap.put("control", "NW");//申请控制（深圳项目需要）
		paramMap.put("ordStatus", "5");//医嘱状态
		paramMap.put("ns","");//护嘱标志
		paramMap.put("pkCnords", stop_pklist);
		if (!"SyxPlatFormSendService".equals(ApplicationUtils.getPropertyValue("msg.send.switch", "0")))//孙逸仙改为医嘱签署时发送消息
		PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
		paramMap = null;
		
		return msg;
    }
    
    /**
     * 核对护嘱信息
     * @param param{List<String> pkOrds, operType=1}
     * @param user
     */
    public String checkOrderNs(String param,IUser user){
    	List<OrderCheckVo> checkList = JsonUtil.readValue(param,new TypeReference<List<OrderCheckVo>>(){});
    	if(checkList==null||checkList.size()<0) return "";
    	if("0".equals(checkList.get(0).getFlagFunDept())){
			List<PvAdt> pvAdts = DataBaseHelper.queryForList("select * from pv_adt where pk_pv=? and (date_end is null or flag_dis='1')", PvAdt.class, new Object[]{checkList.get(0).getPkPv()});
			// 由于病区转产房不算转科，所以产房需要单独判断
			Map<String, Object> map = DataBaseHelper.queryForMap("select dt_depttype from bd_ou_dept where pk_dept =?", UserContext.getUser().getPkDept());
			if (pvAdts != null && pvAdts.size() > 0) {
				if ("1".equals(pvAdts.get(0).getFlagDis())) {
					throw new BusException("该患者已批出院，无法核对护嘱！请确认患者状态！");
				} else if (!map.get("dtDepttype").equals("0310") && !UserContext.getUser().getPkDept().equals(pvAdts.get(0).getPkDeptNs())) {
					throw new BusException("该患者已批转科，无法核对护嘱！请确认患者状态！");
				}
			}
		}

    	User u = (User)user;
    	List<String> pkList = new ArrayList<String>();
    	for(OrderCheckVo chkvo : checkList){
    		if(CommonUtils.isEmptyString(chkvo.getPkCnord())){
    			chkvo.setPkCnord((orderNsService.saveCheckOrdNsVo(chkvo,u)).getPkCnord());
    		}
    		pkList.add(chkvo.getPkCnord());
    	}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkOrds", pkList);
		paramMap.put("operType", "1");
	   	orderNsService.updateOrdNsByPk(paramMap, u);
	   	paramMap.put("pkOrg", u.getPkOrg());
		paramMap.put("pkDept", u.getPkDept());
		paramMap.put("pkDeptNs", u.getPkDept());
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
		String msg = "";
		//生成执行单
		List<String> pkOrdsList = (List<String>)paramMap.get("pkOrds");
		try{
		   msg = orderExecListService.processExecList(checkList,pkOrdsList, null,null, null, paramMap, true);
	    }catch(BusException e){
			msg = msg + "核对时生成执行单失败，请到医嘱执行计划中重新生成！";
		} catch (ParseException e) {
			msg = msg + "核对时追加首日执行单转换日期失败，请到医嘱执行计划中重新生成！";
		}
		
		//发送医嘱核对信息至平台
		paramMap = new HashMap<String,Object>();
		paramMap.put("ordlistvo", JsonUtil.readValue(param,new TypeReference<List<Map<String,Object>>>(){}));
		paramMap.put("control", "NW");//申请控制（深圳项目需要）
		paramMap.put("ordStatus", "3");//医嘱状态
		paramMap.put("ns","");//护嘱标志
		paramMap.put("pkCnords", pkList);
		if (!"SyxPlatFormSendService".equals(ApplicationUtils.getPropertyValue("msg.send.switch", "0")))//孙逸仙改为医嘱签署时发送消息
		PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
		paramMap = null;
		
		return msg;
    }
    
    /**
     * 作废未执行或已取消执行护嘱信息
     * @param param{List<String> pkOrds, operType=3}
     * @param user
     * @return {pk_cnord,num,nameOrd}
     */
    public List<Map<String,Object>> eraseOrderNs(String param,IUser user){
    	//对未执行或取消的执行单执行作废操作
    	Map<String,Object> verfyResult = queryOrderNsExecList(param,user);
    	List<String> pkOrdList = (List<String>)verfyResult.get("pkOrds");//未执行列表
    	List<Map<String,Object>> exList = (List<Map<String,Object>>)verfyResult.get("exList");//已执行的列表，前台去拼提示信息
    	//无未执行的记录情况
    	if(pkOrdList == null||pkOrdList.size()<=0) return exList;
    	//对未执行更新作废状态
    	orderNsService.updateOrdNsByPk(verfyResult,(User)user);
    	//作废执行单
    	String msg="";
    	  orderExecListService.cancelExListByPkOrd(verfyResult,"", user);
    	 //发送消息到平台
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("control", "NW");//申请控制（深圳项目需要）
  		paramMap.put("ordStatus", "6");//医嘱状态
  		paramMap.put("ns","");//护嘱标志
  		paramMap.put("pkCnords", pkOrdList);
  		PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
    	return exList; 	
    }
    /**
     * 作废已执行护嘱信息----此方法废弃
     * @param param{List<String> pkOrds, operType=3}
     * @param user
     * @return 
     */
    public void eraseExOrderNs(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	List<String> pkOrdsList = (List<String>)paramMap.get("pkOrds");
    	if(pkOrdsList==null||pkOrdsList.size()<=0) return ;
    	orderNsService.updateOrdNsByPk(paramMap,(User)user);
    	//调用退费接口
    	//cgPubService.cancelExAndRtnCg(exList, (User)user);
    	//取消执行单
    	//ordExListServcie.cancelExListByPkOrd(verfyResult, "");
    }
    /**
     * 作废护嘱时，查询护嘱是否被执行，返回已被执行的执行单数目
     * @return 提示信息（医嘱+条数）,exPkOrds已执行的医嘱主键集合,pkOrds未执行的医嘱主键集合
     */
    private Map<String,Object> queryOrderNsExecList(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	List<String> pkOrdsList = (List<String>)paramMap.get("pkOrds");
    	if(pkOrdsList==null||pkOrdsList.size()<=0) return null;
    	String euAlways = CommonUtils.getString(paramMap.get("euAlways")); 
    	//如果传入0 表示长期医嘱不验证是否已经执行
    	if(!CommonUtils.isEmptyString(euAlways) && "0".equals(euAlways)){
    		paramMap.put("exList", new ArrayList<Map<String,Object>>());
    		return paramMap;	
    	} 
    	List<Map<String,Object>> list =  orderExecListService.queryExListNumByPkOrd(paramMap);
    	if(list!=null&&list.size()>0){
    		for(Map<String,Object> map:list){
    			String pk_cnord = CommonUtils.getString(map.get("pkCnord"));
     			if(pkOrdsList.contains(pk_cnord))
    				pkOrdsList.remove(pk_cnord);
    		}
    	}
    	paramMap.put("pkOrds", pkOrdsList);//未执行的医嘱主键
    	paramMap.put("exList", list);
    	return paramMap;
    }
   
   
   
   
}
