package com.zebone.nhis.ex.nis.ns.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.support.PdApplySortByOrdUtil;
import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyParamVo;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 医嘱核对自动请领药品服务
 * 
 * @author yangxue
 * 
 */
@Service
public class OrderCheckPdApplyService {

	@Resource
	private GeneratePdApplyService genPdApplyService;

	private Logger logger = LoggerFactory.getLogger("com.zebone");

	@Resource
	private DeptPdApplyService deptPdApplyService;

	/**
	 * 执行请领
	 * 
	 * @param checkList
	 * @return
	 */
	public String executeApply(List<OrderCheckVo> checkList, User user) {
		String msg = "";
		if(checkList==null||checkList.size()<=0)
			return "";
		
//		try {
			Set<String> pkPvs = new HashSet<String>();
			Set<String> pkCnords = new HashSet<String>();
			for(OrderCheckVo ord:checkList){
				if("1".equals(ord.getFlagDurg())){
					pkPvs.add(ord.getPkPv());
					pkCnords.add(ord.getPkCnord());
				}
			}
			if(pkCnords==null||pkCnords.size()<=0)
				return "";
			PdApplyParamVo  paramVo = new PdApplyParamVo();
			String endTime = ApplicationUtils.getSysparam("EX0009", false);//药品请领截止时刻
			if(CommonUtils.isNull(endTime)){
				endTime = "235959";
			}else{
				endTime = endTime.replaceAll(":", "");
			}
			String end = DateUtils.getDateStr(DateUtils.getNextDay())+endTime;
			paramVo.setEndDate(end);
			paramVo.setPkCnords(new ArrayList<String>(pkCnords));
			paramVo.setPkPvs(new ArrayList<String>(pkPvs));
			paramVo.setPkDeptNs(user.getPkDept());
			String paramVos = JSON.toJSONString(paramVo);
			//刷新静配规则
		    Map<String,Object> info= deptPdApplyService.updateExOrderOccByPivas(paramVos,user);
		    //生成请领明细
			List<PdApplyVo> showList = queryApplyPdList(paramVo,user);
			if(showList==null||showList.size()<=0)
				return "";
			msg = saveAppInfo(showList);
//		} catch (Exception e) {
//			e.printStackTrace();
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//			logger.error("================医嘱核对自动生成请领有误，原因如下："+e.getMessage());
//		}
		return msg;
	}

	/**
	 * 查询生成请领单医嘱列表
	 * 
	 * @param param
	 *            {pkPvs,endDate,pkDeptNs}
	 * @param user
	 * @return
	 */
	public List<PdApplyVo> queryApplyPdList(PdApplyParamVo paramVo, User u) {
		if (paramVo == null || paramVo.getPkPvs() == null|| paramVo.getPkPvs().size() <= 0){
			return null;
		}
		String pk_dept = CommonUtils.getString(paramVo.getPkDeptNs());
		if (pk_dept == null)
			pk_dept = u.getPkDept();// 当前操作的病区
		// 此处校验患者欠费及刷静配规则内容移到刷新静配规则的业务方法中
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkDept", pk_dept);
		paramMap.put("pkOrg", u.getPkOrg());
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
		paramMap.put("pkPvs", paramVo.getPkPvs());
		paramMap.put("endDate", paramVo.getEndDate());
		paramMap.put("pkDeptNs", paramVo.getPkDeptNs());
		paramMap.put("pkCnOrdsPivas", paramVo.getPkCnords());//复用静配字段
		
    	//生成请领单
		List<PdApplyVo> showList = new ArrayList<>();
 		List<PdApplyVo> show_list = genPdApplyService.generateApplyDt(paramMap);
 		String paramDay = ApplicationUtils.getSysparam("EX0059", false);//是否按天拆单
 		if("1".equals(paramDay)){
			List<String> pdApplyVoList = new ArrayList<>();
			//返回到前端的
			List<PdApplyVo> endList = new ArrayList<>();
			if(show_list != null && show_list.size()>0){
				for (PdApplyVo bill : show_list) {
					String a = bill.getOrdsn()+","+bill.getPkDeptOcc()+","+bill.getMedName();//加药品名称解决草药
					if(!pdApplyVoList.contains(a)){
						pdApplyVoList.add(a);
					}
				}
				for(String str : pdApplyVoList){
					PdApplyVo pdApplyVo = new PdApplyVo();
					List<ExPdApplyDetail> exPdApplyDetail =  new ArrayList<>();
					Map<String,List<String>> pkMap = new HashMap<>();
					int i = 0;
					for(PdApplyVo vo : show_list){
						if(str.equals(vo.getOrdsn()+","+vo.getPkDeptOcc()+","+vo.getMedName())){
							if(i==0){
								i++;
								ApplicationUtils.copyProperties(pdApplyVo, vo);
								pdApplyVo.setDatePlan(vo.getDatePlan());
								String datePlan = DateUtils.formatDate(vo.getDatePlan())+ " 00:00:00";
								try {
									if (!StringUtils.isEmpty(datePlan)) {
										vo.getApdt().setDateOcc(DateUtils.parseDate(datePlan));
									}
								} catch (ParseException e) {
									throw new BusException("失败|时间格式错误！");
								}
								exPdApplyDetail.add(vo.getApdt());
								pkMap.put(vo.getApdt().getPkPdapdt(),vo.getPkExLists());
							}else{
								BigDecimal b1 = new BigDecimal(Double.toString(vo.getQuan()));
								BigDecimal b2 = new BigDecimal(Double.toString(pdApplyVo.getQuan()));
								pdApplyVo.setQuan(b1.add(b2).doubleValue());
								pdApplyVo.setDatePlan(vo.getDatePlan());
								String datePlan = DateUtils.formatDate(vo.getDatePlan())+ " 00:00:00";
								try {
									if (!StringUtils.isEmpty(datePlan)) {
										vo.getApdt().setDateOcc(DateUtils.parseDate(datePlan));
									}
								} catch (ParseException e) {
									throw new BusException("失败|时间格式错误！");
								}
								exPdApplyDetail.add(vo.getApdt());
								pkMap.put(vo.getApdt().getPkPdapdt(),vo.getPkExLists());
							}
						}
					}
					if(pdApplyVo != null){
						pdApplyVo.setPkExMap(pkMap);
						pdApplyVo.setApdtList(exPdApplyDetail);
						endList.add(pdApplyVo);
					}
				}

			}
			showList = endList;
		}else{
			showList = show_list;
		}

 		new PdApplySortByOrdUtil().ordGroup(showList);
 	
		return showList;
	}

	/**
	 * 保存生成的请领单
	 * 
	 * @param param
	 * @param user
	 */
	public String saveAppInfo(List<PdApplyVo> showList) {
		List<PdApplyVo> dtList = showList;
		if (dtList == null)
			return "未获取到要提交请领的医嘱内容!";
    	//获取需要保存数据
  		List<ExPdApply> pd_ap = new ArrayList<ExPdApply>();					//请领
  		List<ExPdApplyDetail> pd_apdt = new ArrayList<ExPdApplyDetail>();			//请领明细
  		List<String> update_sql = new ArrayList<String>();
  		Set<String> pkList = new HashSet<String>();
  		List<String> apList = new ArrayList<String>();//用来判断是否含有相同的请领单
		String paramDay = ApplicationUtils.getSysparam("EX0059", false);
  		for(PdApplyVo dt : dtList){
  			if("1".equals(paramDay)){
				//不含基数药
				if(dt.getApdtList() != null){
					List<ExPdApplyDetail> exPdApplyDetails = dt.getApdtList();
					for(ExPdApplyDetail detail : exPdApplyDetails){
						detail.setFlagEmer(dt.getFlagEmer());
						//生成请领单时基数药的请领数量默认为0
						if("1".equals(detail.getFlagBase())){
							detail.setQuanMin(0.00);
							detail.setQuanPack(0.00);
							detail.setFlagFinish("1");//直接设置发放完成标志为已完成
							detail.setFlagDe("1");
						}
						detail.setFlagCanc("0");
						//dt.getApdt().setFlagCanc("0");
						pd_apdt.add(detail);
					}
				}
				if(dt.getPkExMap()!=null){
					for(Map.Entry<String, List<String>> entry : dt.getPkExMap().entrySet()){
						String pkPdapdt = entry.getKey();
						List<String> pkExLists = entry.getValue();
						if(pkExLists !=null &&pkExLists.size()>0){
							pkList.addAll(pkExLists);
							for(String pkExOcc : pkExLists){
								StringBuilder sql = new StringBuilder("update ex_order_occ set  pk_pdapdt = '");
								sql.append(pkPdapdt);
								sql.append("'  where pk_exocc = '");
								sql.append(pkExOcc);
								sql.append("' and (pk_pdapdt is null or pk_pdapdt = '') ");
								update_sql.add(sql.toString());
							}
						}
					}
				}
			}else{
				//不含基数药
				if(dt.getApdt()!=null){
					dt.getApdt().setFlagEmer(dt.getFlagEmer());
					//生成请领单时基数药的请领数量默认为0
					if("1".equals(dt.getApdt().getFlagBase())){
						dt.getApdt().setQuanMin(0.00);
						dt.getApdt().setQuanPack(0.00);
						dt.getApdt().setFlagFinish("1");//直接设置发放完成标志为已完成
						dt.getApdt().setFlagDe("1");
					}
					dt.getApdt().setFlagCanc("0");
					pd_apdt.add(dt.getApdt());
				}
				if(dt.getPkExLists()!=null&&dt.getPkExLists().size()>0){
					pkList.addAll(dt.getPkExLists());
					//String pks = OrderSortUtil.convertPkListToStr(dt.getPkExLists());
					for(String pkExOcc : dt.getPkExLists()){
						StringBuilder sql = new StringBuilder("update ex_order_occ set  pk_pdapdt = '");
						sql.append(dt.getPkPdapdt());
						sql.append("'  where pk_exocc = '");
						sql.append(pkExOcc);
						sql.append("' and (pk_pdapdt is null or pk_pdapdt = '') ");
						update_sql.add(sql.toString());
					}

				}
			}
  			if(dt.getAp()!=null&&!apList.contains(dt.getAp().getPkPdap())){
  				pd_ap.add(dt.getAp());
  				apList.add(dt.getAp().getPkPdap());
  			}

  		}
  	    //更新执行单
  		if(update_sql!=null&&update_sql.size()>0){
  			int count[] = DataBaseHelper.batchUpdate(update_sql.toArray(new String[0]));
  	 		if(count!=null&&count.length>0){
  	 			int rows = 0;
  	 			for(int row:count){
  	 				rows = rows+row;
  	 			}
  	 			if(pkList!=null&&pkList.size() !=rows)
  	 				throw new BusException("本次提交内容可能已被他人请领，请重新生成！");
  	 		}
  		}
 		
  	    //保存请领单及请领明细
 		if(pd_ap!=null&&pd_ap.size()>0){
 			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdApply.class), pd_ap);
 		}
 		if(pd_apdt!=null&&pd_apdt.size()>0){
 			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdApplyDetail.class), pd_apdt);
 		}
		return "";
	}
	

}
