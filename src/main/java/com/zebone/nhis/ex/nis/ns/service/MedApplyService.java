package com.zebone.nhis.ex.nis.ns.service;

import java.util.*;

import javax.annotation.Resource;

import com.zebone.nhis.common.module.base.message.SysMessage;
import com.zebone.nhis.common.service.CnNoticeService;
import com.zebone.nhis.common.service.MessageService;
import com.zebone.nhis.common.support.MessageUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.dao.MedApplyMapper;
import com.zebone.nhis.ex.nis.ns.vo.MedApplyVo;
import com.zebone.nhis.common.module.base.bd.wf.BdOrdAutoexec;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class MedApplyService {
    
	@Resource
	public MedApplyMapper medApplyMapper;
    
    @Resource
    public IpCgPubService ipCgPubService;
    
    @Resource
    public OrderAutoCgService orderAutoCgService;

	@Autowired
	private CnNoticeService cnNoticeService;
    
    /**
     * 查询医技申请单
     * @param param{pkPvs,apptype,codeApply}
     * @param user
     * @return
     */
    public List<MedApplyVo> queryMedApplist(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	List<String> pvlist = (List)map.get("pkPvs");
   	    if(pvlist == null || pvlist.size()<=0)
   		 throw new BusException("未获取到患者就诊信息！");
   	    String pk_dept_cur = ((User)user).getPkDept();
   	    map.put("pkDeptNs", pk_dept_cur);
   	    List<MedApplyVo> list = null;
   	   // 由于病区转产房不算转科，所以产房需要单独判断  
   	   Map<String, Object> mapParam = DataBaseHelper.queryForMap("select dt_depttype from bd_ou_dept where pk_dept =?", UserContext.getUser().getPkDept());
   	   if(mapParam.get("dtDepttype").equals("0310")){
   		  list = medApplyMapper.queryLabAppList(map);  	 
   	   }else{
   		  list = medApplyMapper.queryAppList(map);  		   
   	   }
   	    
   	    //2019-07-23 查询并设置当前记录是否为自动计费医嘱
   	    if(null != list && list.size() > 0){
   	    	Map<String,Object> qryMap = new HashMap<String,Object>();
   	    	qryMap.put("pkOrg", ((User)user).getPkOrg());
   	    	List<BdOrdAutoexec> autoList = orderAutoCgService.queryAutoOrdCg(qryMap, (User)user);//查询该机构下自动计费的相关记录
   	    	if(null != autoList && autoList.size() > 0){
   	    		for (MedApplyVo app : list) {
   	    			for (BdOrdAutoexec auto : autoList) {
   	    				if(app.getPkDeptExec().equals(auto.getPkDept())){
   	    					if("0".equals(auto.getEuType()))/**0 全部*/
   	    						app.setFlagAutoCg("1");
   	    					else if("1".equals(auto.getEuType()) && app.getApptype().equals(auto.getCodeOrdtype()))/**1医嘱类型*/
   	    						app.setFlagAutoCg("1");
   	    					else if("2".equals(auto.getEuType()) && app.getPkOrd().equals(auto.getPkOrd()))/**2医嘱项目*/
   	    						app.setFlagAutoCg("1");
   	    					break;
   	    				}
   	    			}
   	    		}
   	    	}
   	    }
   	    
    	return list;
    }
    
    /**
     * 提交申请单
     * @param param{pkOrds}
     * @param user
     */
    public void submitApply(String param,IUser user){
    	List<String> pkOrds = JsonUtil.readValue(param, List.class);
    	if(pkOrds!=null&&pkOrds.size()>0){
    		Map<String,Object> paramMap = new HashMap<String,Object>();
    		paramMap.put("pkOrds", pkOrds);
    		//校验是否有已经执行的
    		String flag = medApplyMapper.queryAppExList(paramMap);
    		if(flag!=null&&flag.equals("1")){
    			throw new BusException("您选择的内容存在已执行的申请单，请刷新后重新选择！");
    		}
    		DataBaseHelper.update("update cn_lab_apply set eu_status = '1' where pk_cnord in (:pkOrds) and eu_status = '0'", paramMap);
    		DataBaseHelper.update("update cn_ris_apply set eu_status = '1' where pk_cnord in (:pkOrds) and eu_status = '0'", paramMap);
    		DataBaseHelper.update("update cn_pa_apply set eu_status = '1' where pk_cnord in (:pkOrds) and eu_status = '0'", paramMap);
    		DataBaseHelper.update("update cn_op_apply set eu_status = '1' where pk_cnord in (:pkOrds) and eu_status = '0'", paramMap);
    		DataBaseHelper.update("update cn_trans_apply  set eu_status = '1' where pk_cnord in (:pkOrds) and eu_status = '0'", paramMap);
    	}
    }
    
    /**
     * 取消提交申请单
     * @param param{pkOrds}
     * @param user
     */
    public void cancelSubmitApply(String param,IUser user){
    	List<String> pkOrds = JsonUtil.readValue(param, List.class);
    	if(pkOrds!=null&&pkOrds.size()>0){
    		Map<String,Object> paramMap = new HashMap<String,Object>();
    		paramMap.put("pkOrds", pkOrds);
    		//校验是否有已经执行的
    		String flag = medApplyMapper.queryAppExList(paramMap);
    		if(flag != null && !flag.equals("0")){
    			throw new BusException("您选择的医嘱里存在医嘱执行单状态为已执行,请先到[执行确认]--[非本科室医嘱]界面选择已执行进行恢复,即可取消提交。");
    		}
    		DataBaseHelper.update("update cn_lab_apply set eu_status = '0' where pk_cnord in (:pkOrds) and eu_status = '1'", paramMap);
    		DataBaseHelper.update("update cn_ris_apply set eu_status = '0' where pk_cnord in (:pkOrds) and eu_status = '1'", paramMap);
    		DataBaseHelper.update("update cn_pa_apply set eu_status = '0' where pk_cnord in (:pkOrds) and eu_status = '1'", paramMap);
    		DataBaseHelper.update("update cn_op_apply set eu_status = '0' where pk_cnord in (:pkOrds) and eu_status = '1'", paramMap);
    		DataBaseHelper.update("update cn_trans_apply  set eu_status = '0' where pk_cnord in (:pkOrds) and eu_status = '1'", paramMap);
    	}
    }
    
    /**
     * 手术完成
     * @param param
     * @param user
     */
    public void operationFinish(String param,IUser user){
    	List<MedApplyVo> list = JsonUtil.readValue(param,new TypeReference<List<MedApplyVo>>(){});
    	if(list == null || list.size() < 1)
    		throw new BusException("未获取到待完成的手术申请单！");
    	List<String> pkOrdOps = new ArrayList<String>();
    	List<String> pkCnords = new ArrayList<String>();
    	for (MedApplyVo app : list) {
    		pkOrdOps.add(app.getPkOrdop());
    		pkCnords.add(app.getPkCnord());
		}
		if(pkOrdOps.size() < 1)
			throw new BusException("未获取到待完成的手术申请单主键！");
		
		if(pkCnords.size() < 1)
			throw new BusException("未获取到待完成的手术医嘱的执行单主键！");
		
		//校验是否有已经执行的
		Map<String,Object> chkparam = new HashMap<String,Object>();
		chkparam.put("pkOrds", pkCnords);
		String flag = medApplyMapper.queryAppExList(chkparam);
		if(flag!=null&&flag.equals("1")){
			throw new BusException("您选择的内容存在已执行的申请单，请刷新后重新选择！");
		}
		
		//1、更新手术申请单状态
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkOrds", pkOrdOps);
		DataBaseHelper.update("update cn_op_apply set eu_status = '5' where pk_ordop in (:pkOrds)", paramMap);
		
		//2、更新相关执行单的状态
		paramMap.put("pkCnords", pkCnords);
		paramMap.put("dateOcc", new Date());
		paramMap.put("pkEmpOcc", UserContext.getUser().getPkEmp());
		paramMap.put("pkOrgOcc", UserContext.getUser().getPkOrg());
		paramMap.put("pkDeptOcc", UserContext.getUser().getPkDept());
		paramMap.put("nameEmpOcc", UserContext.getUser().getNameEmp());
		StringBuilder sql_exocc = new StringBuilder("update ex_order_occ set eu_status = '1' , pk_org_occ =:pkOrgOcc , pk_dept_occ =:pkDeptOcc,");
				sql_exocc.append(" pk_emp_occ =:pkEmpOcc , name_emp_occ =:nameEmpOcc , date_occ =:dateOcc where pk_cnord in (:pkCnords)  ");
		DataBaseHelper.update(sql_exocc.toString(), paramMap);
    }
    
    /**
     * 同步检查|检验申请单至三方接口
     * @param param{pkOrds}
     * @param user
     */
    public void sysLisOrRisApply(String param,IUser user){
    	if(CommonUtils.isEmptyString(param))
    		throw new BusException("同步申请单入参为空！");
    	List<MedApplyVo> list = JsonUtil.readValue(param,new TypeReference<List<MedApplyVo>>(){});
    	if(null == list || list.size() < 1)
    		throw new BusException("未获取到待同步的申请单！");
    	List<String> lisPks = new ArrayList<String>();
    	List<String> risPks = new ArrayList<String>();
    	for (MedApplyVo app : list) {
    		if(0 == app.getCodeApptype().indexOf("03"))//检验传入医嘱主键
    			lisPks.add(app.getPkCnord());
    		if(0 == app.getCodeApptype().indexOf("02"))//检查传入医嘱主键
    			risPks.add(app.getPkCnord());
    	}
    	
    	if(null != lisPks && lisPks.size() > 0){
    		ExtSystemProcessUtils.processExtMethod("LIS", "saveLisApply", lisPks);
    	}
    	if(null != risPks && risPks.size() > 0){
    		ExtSystemProcessUtils.processExtMethod("PACS", "savePacsEx", null,risPks);
    	}
    	
    	//LB-添加重发送医技申请单至平台
 	    Map<String,Object> paramMap = new HashMap<String,Object>();
 	    paramMap.put("pkPv", list.get(0).getPkPv());
    	if(null != lisPks && lisPks.size() > 0){
    		paramMap.put("type", "lis");
    		paramMap.put("lisList", list);
    	}
    	if(null != risPks && risPks.size() > 0){
    		paramMap.put("type", "ris");
     		paramMap.put("risList", list);
    	}
    	paramMap.put("Control", "NW");
    	PlatFormSendUtils.sendCnMedApplyMsg(paramMap);
 		paramMap = null;	
    }
    
    /**
     * 检验退费【删除lis申请单及明细 + 退费 + 更新申请单 + 更新执行单】
     * @param param
     * @param user
     */
    public void delLisApplyFromLis(String param,IUser user){
    	if(CommonUtils.isEmptyString(param))
    		throw new BusException("检验退费入参为空！");
    	List<MedApplyVo> list = JsonUtil.readValue(param,new TypeReference<List<MedApplyVo>>(){});
    	if(null == list || list.size() < 1)
    		throw new BusException("未获取到待退费的检验申请单！");
    	List<String> lisPks = new ArrayList<String>();
    	List<String> lisPkExocc = new ArrayList<String>();
    	for (MedApplyVo app : list) {
    		if(0 == app.getCodeApptype().indexOf("03"))//检验传入医嘱主键
    		{
    			lisPks.add(app.getPkCnord());
    			lisPkExocc.add(app.getPkExocc());
    		}
    	}
    	try {
    		if(null != lisPks && lisPks.size() > 0)
    			ExtSystemProcessUtils.processExtMethod("LIS", "delLisApply", lisPks);
		} catch (Exception e) {
			throw new BusException("退费失败，失败原因：" + e.getMessage());
		}
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("pkOrds", lisPks);
    	map.put("exlist", lisPkExocc);
    	List<RefundVo> refList = medApplyMapper.queryAppBackCgList(map);
    	User u = (User)user;
    	//存在待退费则退费
    	if(null != refList && refList.size() > 0) {
    		for (RefundVo ref : refList) {
    			ref.setPkDept(u.getPkDept());
    			ref.setPkEmp(u.getPkEmp());
    			ref.setNameEmp(u.getNameEmp());
    			ref.setPkOrg(u.getPkOrg());
    		}
    		BlPubReturnVo blRtn = ipCgPubService.refundInBatch(refList);
    		if(null == blRtn || null == blRtn.getBids())
    			throw new BusException("退费失败！");
    		if(blRtn.getBids().size() != refList.size())
    			throw new BusException("退费失败！退费记录 != 记费数量");
    	}
    	
    	Map<String,Object> upmap = new HashMap<String,Object> ();
    	upmap.put("statusAp", "1".equals(list.get(0).getFlagCanc()) ? "0" : "1");
    	upmap.put("statusEx", "1".equals(list.get(0).getFlagCanc()) ? "9" : "0");
    	upmap.put("pkDeptNs", u.getPkDept());
    	upmap.put("pkEmp", u.getPkEmp());
    	upmap.put("nameEmp", u.getNameEmp());
    	upmap.put("pkOrds", lisPks);
    	upmap.put("exlist", lisPkExocc);
    	upmap.put("dateCanc", DateUtils.getDateTimeStr(new Date()));
    	medApplyMapper.updateLabApplyToBackCg(upmap);//更新检验申请单状态
    	if("1".equals(list.get(0).getFlagCanc()))
    		medApplyMapper.cancExStatus(upmap);//取消执行单状态
    	else
    	{
    		medApplyMapper.updateExStatus(upmap);//还原执行单状态
    		medApplyMapper.updateOrdExInfo(upmap);//清空医嘱临时执行人相关信息
    	}
    	
    	//发送检查检验记费信息至平台
		Map<String,Object> paramListMap = new HashMap<String,Object>();
		paramListMap.put("dtlist", JsonUtil.readValue(param,new TypeReference<List<Map<String, Object>>>(){}));
		paramListMap.put("type", "I");
		paramListMap.put("Control", "CR");
		PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
		paramListMap = null;
    }
    
    /**
     * 取消执行【针对 非检查、非检验、非会诊、非手术、执行科室非当前病区的执行单 ，进行取消执行单的操作】
     * @param param
     * @param user
     */
    public void cancExByPk(String param,IUser user){
    	if(CommonUtils.isEmptyString(param))
    		throw new BusException("取消执行-入参为空！");
    	List<MedApplyVo> list = JsonUtil.readValue(param,new TypeReference<List<MedApplyVo>>(){});
    	if(null == list || list.size() < 1)
    		throw new BusException("未获取到待取消执行的记录！");
    	List<String> lisPks = new ArrayList<String>();
		Set<String> pkCnords = new HashSet<>();
		for (MedApplyVo app : list) {
			pkCnords.add(app.getPkCnord());
			lisPks.add(app.getPkExocc());//检验传入医嘱主键
    	}
		User u = (User)user;
    	Map<String,Object> upmap = new HashMap<String,Object> ();
    	upmap.put("exlist", lisPks);
    	upmap.put("statusEx", "9");
    	upmap.put("pkDeptNs", u.getPkDept());
    	upmap.put("pkEmp", list.get(0).getPkEmpCanc());
    	upmap.put("nameEmp", list.get(0).getNameEmpCanc());
    	upmap.put("dateCanc", DateUtils.getDateTimeStr(new Date()));
    	int cnt = medApplyMapper.cancExStatus(upmap);//更新执行单状态
    	if(cnt != list.size())
    		throw new BusException("取消执行的记录数 != 待取消的记录数，取消失败！");
    	//发送系统消息
		cnNoticeService.saveCnNoticeToBe(pkCnords,user);
	}



}
