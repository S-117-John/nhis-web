package com.zebone.nhis.ex.nis.ns.service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ex.nis.ns.dao.DeptPdApplyMapper;
import com.zebone.nhis.ex.nis.ns.support.GenerateExListSortByOrdUtil;
import com.zebone.nhis.ex.nis.ns.support.PdApplySortByOrdUtil;
import com.zebone.nhis.ex.nis.ns.support.pdap.GeneratePdApContext;
import com.zebone.nhis.ex.nis.ns.support.pdap.SynExListInfoHandler;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyParamVo;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyVo;
import com.zebone.nhis.ex.pub.service.QueryRemainFeeService;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 药品请领业务处理类
 * @author yangxue
 *
 */
@Service
public class DeptPdApplyService {
     @Resource
     private DeptPdApplyMapper pdApplyMapper;
     @Resource
     private GeneratePdApplyService genPdApplyService;
     @Resource
 	 private QueryRemainFeeService queryFeeServcie;
     @Resource
     private PdStOutPubService pdStOutPubService;
     @Resource
     private PvInfoPubService pvInfoPubService;
     
     private static String APPLYID = "ex:nis:ns:applyid";
     
     private String pre="ex:nis:ns:";
     
     private Logger logger = LoggerFactory.getLogger("com.zebone");
     /**
      * 查询生成请领单医嘱列表---缓存模式
      * @param param{pkPvs,endDate,pkDeptNs}
      * @param user
      * @return
      */
     public Map<String,Object> queryApplyPdListBuffer(String param,IUser user){
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 List<String> pv_list = (List<String>)paramMap.get("pkPvs");
    	 if(pv_list==null||pv_list.size()<=0)
    		 throw new BusException("未获取到需要生成请领单的患者信息！");
    	 String name = "";
    	 String msg = "";
    	 
    	 if (QueryRemainFeeService.isControlFee()) {
    		 for(String pk_pv:pv_list){
    			 if(!queryFeeServcie.isArrearage(pk_pv,"",BigDecimal.ZERO)){//已欠费
    				 PvEncounter pv = DataBaseHelper.queryForBean("select pv.name_pi,pi.code_ip from pv_encounter pv inner join pi_master pi on pi.pk_pi = pv.pk_pi  where pk_pv = ? ", PvEncounter.class, new Object[]{pk_pv});
    				 name += pv.getNamePi()+"("+pv.getCodeIp()+"),";
    			 }
    		 }
    		 
    		 if(!"".equals(name)){
    			 msg = msg +"患者"+name+"已经欠费，请催缴预交金或者提供担保！";
    		 }
    	 }
    	 User u = (User)user;   	 
    	 String pk_dept = CommonUtils.getString(paramMap.get("pkDeptNs"));
    	 if(pk_dept == null)
    	       pk_dept = u.getPkDept();//当前操作的病区
    	 
    	 //处理并发
    	 String appIdenNo = RedisUtils.getCacheObj(APPLYID,String.class);//取某一作用域的唯一标识 
    	 if(null == appIdenNo || "".equals(appIdenNo)){
    		 appIdenNo = NHISUUID.getKeyId();//使用uuid作为唯一标识
 		}else{
 			String pk_dept_old = RedisUtils.getCacheObj(pre+appIdenNo,String.class);
 			//当前用户生成请领直接清除缓存，清除，否则不处理
 			if(null != pk_dept_old && pk_dept_old.equals(pk_dept)){
 				finishExec(appIdenNo, pk_dept);
 			}
 		}
    	paramMap.put("pkDept", pk_dept);
    	paramMap.put("pkOrg", u.getPkOrg());
    	paramMap.put("pkEmp", u.getPkEmp());
    	paramMap.put("nameEmp", u.getNameEmp());
    	//生成请领单,并将生成的信息存放在缓存中
 		msg = msg +  genPdApplyService.generate(paramMap);
 		int lockTime = CommonUtils.getInt(ExSysParamUtil.getUnlockTime());
 		RedisUtils.setCacheObj(pre+appIdenNo,pk_dept,lockTime*60);
 		RedisUtils.setCacheObj(APPLYID,appIdenNo,lockTime*60);
 		
 		List<PdApplyVo> show_list = GeneratePdApContext.getInstance().getShowList(pk_dept);
 		if(null == show_list || show_list.size() == 0){
 			finishExec(appIdenNo, pk_dept);
 			return null;
 		}
 		
 		new PdApplySortByOrdUtil().ordGroup(show_list);
 		
 		Map<String,Object> map = new HashMap<String,Object>();
 		map.put("msg", msg);
 		map.put("list", show_list);
    	 return map;
     }
     /**
      * 查询生成请领单医嘱列表---非缓存模式
      * @param param{pkPvs,endDate,pkDeptNs}
      * @param user
      * @return
      */
     public Map<String,Object> queryApplyPdList(String param,IUser user){
    	 PdApplyParamVo paramVo = JsonUtil.readValue(param, PdApplyParamVo.class);
    	 Date cur = new Date();
    	 long beginTime = cur.getTime();
    	 logger.info("======================执行药品请领方法开始，开始时间："+cur.getTime()+"=========================");
    	 //List<String> pv_list = (List<String>)paramMap.get("pkPvs");
    	 if(paramVo==null||paramVo.getPkPvs()==null||paramVo.getPkPvs().size()<=0)
    		 throw new BusException("未获取到需要生成请领单的患者信息！");
    	 //String name = "";
    	 //String msg = "";
    	 User u = (User)user;
    	 String pk_dept = CommonUtils.getString(paramVo.getPkDeptNs());
    	 if(pk_dept == null)
    	       pk_dept = u.getPkDept();//当前操作的病区 
    	//此处校验患者欠费及刷静配规则内容移到刷新静配规则的业务方法中
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("pkDept", pk_dept);
    	paramMap.put("pkOrg", u.getPkOrg());
    	paramMap.put("pkEmp", u.getPkEmp());
    	paramMap.put("nameEmp", u.getNameEmp());
    	paramMap.put("pkPvs", paramVo.getPkPvs());
    	paramMap.put("endDate", paramVo.getEndDate());
    	paramMap.put("pkDeptNs", paramVo.getPkDeptNs());
    	//生成请领单
		 List<PdApplyVo> showList = new ArrayList<>();
 		List<PdApplyVo> show_list = genPdApplyService.generateApplyDt(paramMap);
 		 String paramDay = ApplicationUtils.getSysparam("EX0059", false);//是否按天拆单
		 String paramSecond = ApplicationUtils.getSysparam("EX0084", false);//是否静配按顿生成请领（一个执行单生成一个请领明细）
		 if("1".equals(paramDay)||"1".equals(paramSecond)){
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
 		Map<String,Object> map = new HashMap<String,Object>();
 		//map.put("msg", msg);
 		//map.put("updateMsg", updatemsg);//更新静配规则提示信息，不影响正常使用
 		map.put("list", showList);
 		cur = new Date();
 		logger.info("======================执行药品请领方法结束，耗时："+(cur.getTime()-beginTime)+"=========================");
    	 return map;
     }
     /**
      *  保存生成的请领单--非缓存模式
      * @param param
      * @param user
      */
     public void saveAppInfo(String param,IUser user){
    	 List<PdApplyVo> dtList = JsonUtil.readValue(param, new TypeReference<List<PdApplyVo>>(){});
    	 if(dtList == null) throw new BusException("未获取到要提交的内容!");
    	//获取需要保存数据
  		List<ExPdApply> pd_ap = new ArrayList<ExPdApply>();					//请领
  		List<ExPdApplyDetail> pd_apdt = new ArrayList<ExPdApplyDetail>();			//请领明细
  		List<String> update_sql = new ArrayList<String>();
  		Set<String> pkList = new HashSet<String>();
  		List<String> apList = new ArrayList<String>();//用来判断是否含有相同的请领单
		 String paramDay = ApplicationUtils.getSysparam("EX0059", false);
		 String paramSecond = ApplicationUtils.getSysparam("EX0084", false);//是否静配按顿生成请领（一个执行单生成一个请领明细）
  		for(PdApplyVo dt : dtList){
  			if("1".equals(paramDay)||"1".equals(paramSecond)){
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
     }
     
     /**
      * 保存生成的请领单--缓存模式
      * @param param{pkDeptNs,pkDts(list)}
      * @param user
      */
     public void saveAppInfoBuffer(String param,IUser user){
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 String pkDeptNs = CommonUtils.getString(paramMap.get("pkDeptNs"));
    	 if(pkDeptNs == null)
    		 pkDeptNs = ((User)user).getPkDept();//当前操作的病区
    	 List<String> pkDts = (List)paramMap.get("pkDts");
    	 
    	//获取缓存中数据
 		List<ExPdApply> aps = GeneratePdApContext.getInstance().getApList(pkDeptNs);
 		List<ExPdApplyDetail> dts = GeneratePdApContext.getInstance().getApDtList(pkDeptNs);
 		List<SynExListInfoHandler> ups = GeneratePdApContext.getInstance().getUpdateList(pkDeptNs);
 		//获取需要保存数据
 		List<ExPdApply> pd_ap = new ArrayList<ExPdApply>();					//请领
 		List<ExPdApplyDetail> pd_apdt = new ArrayList<ExPdApplyDetail>();			//请领明细
 		List<String> pk_aps = new ArrayList<String>();							//请领主键记录
 		//更新执行单信息和获取要保存的请领单明细信息
 		for(String pk_dt : pkDts){
 			for(ExPdApplyDetail dt : dts){
 				if(pk_dt.equals(dt.getPkPdapdt())&& !dt.getFlagBase().equals("1")){
 					pd_apdt.add(dt);
 					if(!pk_aps.contains(dt.getPkPdap())){
 						pk_aps.add(dt.getPkPdap());
 					}
 					break;
 				}
 			}
 			for(SynExListInfoHandler up : ups){
 				if(pk_dt.equals(up.getPk_pdapdt())){
 					DataBaseHelper.update(up.getUpdateSql(),new Object());
 				}
 			}
 		}
 		//获取要保存的请领单信息
 		for(String pk_ap : pk_aps){
 			for(ExPdApply ap : aps){
 				if(pk_ap.equals(ap.getPkPdap())){
 					pd_ap.add(ap);
 					break;
 				}
 			}
 		}
        //保存请领单及请领明细
 		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdApply.class), pd_ap);
 		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdApplyDetail.class), pd_apdt);
 		//清除缓存
 		GeneratePdApContext.getInstance().finish(pkDeptNs);
     }
     
     /**
 	 * 清除当前pk_dept缓存，清除缓存
 	 * @param uuid
 	 * @param pk_dept
 	 * @param 
 	 * @throws BusException
 	 */
 	private void finishExec(String uuid, String pk_dept)
 			throws BusException {
 		GeneratePdApContext.getInstance().finish(pk_dept);
 		RedisUtils.delCache(pre+uuid);//移除唯一标识
 		RedisUtils.delCache(APPLYID);
 	}
 	/**
 	 * 强制结束请领进程
 	 * @param param{当前病区主键}
 	 * @param user
 	 */
 	public void forceEnd(String param,IUser user){
   	 String appIdenNo = CommonUtils.getString(RedisUtils.getCacheObj(APPLYID));//取某一作用域的唯一标识 
   	 if(null != appIdenNo && !"".equals(appIdenNo)){
   		String pk_dept = JsonUtil.readValue(param, String.class);
   		finishExec(appIdenNo, pk_dept);
   	 }
 	}
 	/**
 	 * 根据执行科室，物品信息获取物品库存量
 	 * @param param{pkDeptOcc,pkPd,packSize}
 	 * @param user
 	 * @return
 	 */
 	public Double getPdStoreNum(String param,IUser user){
 		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
 		if(paramMap == null||paramMap.get("pkDeptOcc")==null||paramMap.get("pkPd")==null)
 			throw new BusException("未获取到需要计算库存量的物品或发药科室信息！");
 		boolean flagSelf = "1".equals(CommonUtils.getString(paramMap.get("flagSelf")));
 		if(!flagSelf&&(paramMap.get("packSize")==null||CommonUtils.getDouble(paramMap.get("packSize"))==0)){
 			throw new BusException("包装量为空或者为0，无法计算库存量！");
 		}
 		Map<String,Object> storeMap = DataBaseHelper.queryForMap("select pk_store from bd_store where pk_dept = ? and del_flag=0", new Object[]{paramMap.get("pkDeptOcc")});
 		if(!flagSelf&&storeMap == null)
 			throw new BusException("根据发药科室未能获取到对应的仓库！");
 		if(!flagSelf){
 			double num = pdStOutPubService.getStoreNum(CommonUtils.getString(paramMap.get("pkPd")), CommonUtils.getString(storeMap.get("pkStore")));
 	 	    return MathUtils.div(num,CommonUtils.getDouble(paramMap.get("packSize")));
 		}else{
 			return 0.00;
 		}
 	}
 	/**
 	 * 增加刷新静配规则对外访问方法
 	 * @param param
 	 * @param user
 	 * @return
 	 */
 	public Map<String,Object> updateExOrderOccByPivas(String param,IUser user){
 		 PdApplyParamVo paramVo = JsonUtil.readValue(param, PdApplyParamVo.class);
 		 Date cur = new Date();
 		 long timeBegin = cur.getTime();
 		 logger.info("======================执行静配方法开始，开始时间："+timeBegin+"=========================");
    	 //List<String> pv_list = (List<String>)paramMap.get("pkPvs");
    	 if(paramVo==null||paramVo.getPkPvs()==null||paramVo.getPkPvs().size()<=0)
    		 throw new BusException("未获取到需要生成请领单的患者信息！");
    	 String name = "";
    	 String msg = "";
    	 User u = (User)user;
    	 String pk_dept = CommonUtils.getString(paramVo.getPkDeptNs());
    	 if(pk_dept == null)
    	       pk_dept = u.getPkDept();//当前操作的病区 
 		 String updatemsg = null;
    	 //判断是否需要刷静配规则
    	 if(paramVo.getOrdsPivas()==null||paramVo.getOrdsPivas().size()<=0){
    	 //根据静配规则更新执行单
    	  cur = new Date();
    	 logger.info("======================执行更新静配开始，开始时间："+cur.getTime()+"=========================");	
    	 updatemsg =  updateExOrderOccByPivas(pk_dept,u.getPkOrg(),paramVo.getPkPvs(),paramVo.getEndDate());
    	 logger.info("======================执行更新静配结束，耗时："+(cur.getTime()-timeBegin)+"ms=========================");
    	 String paramChkFee = ApplicationUtils.getDeptSysparam("BL0005", UserContext.getUser().getPkDept());// 除医生站外是否控制欠费,11-21修改为科室参数
    	 if ("1".equals(paramChkFee)) {
    		 cur = new Date();
    		 long time = cur.getTime();
    		 logger.info("======================执行更新静配后获取欠费信息开始，开始时间："+time+"=========================");
    		 for(String pk_pv:paramVo.getPkPvs()){
    			 //若当前患者主医保为农合，且患者类型为贫困患者，则不控费
    			 if(pvInfoPubService.isLimiteFee(pk_pv)){
    				 if(!queryFeeServcie.isArrearage(pk_pv,"",BigDecimal.ZERO)){//已欠费
    					 PvEncounter pv = DataBaseHelper.queryForBean("select pv.name_pi,pi.code_ip,pv.bed_no from pv_encounter pv inner join pi_master pi on pi.pk_pi = pv.pk_pi  where pk_pv = ? ", PvEncounter.class, new Object[]{pk_pv});
    					 name += "(" +pv.getBedNo()+"床)"+ pv.getNamePi() +"("+pv.getCodeIp()+"),";
    				 }
    			 }
    		 }
    		 cur = new Date();
    		 logger.info("======================执行更新静配后获取欠费信息结束，耗时："+(cur.getTime()-time)+"ms=========================");
    		 if(!"".equals(name)){
    			 msg = msg +"患者："+name+"已经欠费，请催缴预交金或者提供担保！";
    		 }
    	  }
    	 }else{//不刷新静配规则，刷新非静配规则
    		 this.updateExlistToNoPivas(paramVo);
    	 }
    	Map<String,Object> map = new HashMap<String,Object>();
  		map.put("msg", msg);
  		map.put("updateMsg", updatemsg);//更新静配规则提示信息，不影响正常使用
  		//map.put("list", show_list);
  		cur = new Date();
  		logger.info("======================执行静配方法结束，耗时："+(cur.getTime()-timeBegin)+"ms=========================");
  		logger.info("======================执行静配方法结束，结束时间："+cur.getTime()+"=========================");
     	return map;
 	}
 	/**
 	 * 根据静配规则更新执行单，规则内容取自系统参数的备注描述
 	 */
 	private String updateExOrderOccByPivas(String pkDept,String pkOrg,List<String> pkPvs,String endDate){
 		String val = ApplicationUtils.getDeptSysparam("EX0039", pkDept);
 		if(!"1".equals(val))
 			return null;
 		Map<String,Object> paramMap = new HashMap<String,Object>();
 		paramMap.put("pkDept", pkDept);
 		Map<String,Object> sqlMap =  pdApplyMapper.getPivasRule(paramMap);
 		if(sqlMap == null||"".equals(sqlMap.get("wheresql").toString()))
 			return null;
 		paramMap.put("whereSql", sqlMap.get("wheresql").toString());
 		paramMap.put("pkPvs", pkPvs);
 		paramMap.put("endDate", endDate);
 		paramMap.put("pkDeptNs", UserContext.getUser().getPkDept());

 		//2019-06-25 新增科室级别参数，控制是否走优化后的sql-with as规则
 		String useWithAs = ApplicationUtils.getDeptSysparam("EX0054", pkDept);
 		List<Map<String,Object>> exlist = null;
 		if("1".equals(useWithAs))
 			exlist = pdApplyMapper.queryUpdateExByWithAs(paramMap);
 		else
 			exlist = pdApplyMapper.queryUpdateEx(paramMap);
 		if(exlist==null||exlist.size()<=0) return null;
 		Set<String> updateSet = new HashSet<String>();
 		StringBuilder str = null;
 		String now = DateUtils.getDateTimeStr(new Date());
 		StringBuilder msg = new StringBuilder("");
 		User user = UserContext.getUser();
 		Set<String> ordsnSet = new HashSet<String>();
 		Map<String,Set<String>> updateMap = new HashMap<String,Set<String>>();
 		String pkDeptStore = null;
 		for(Map<String,Object> ex:exlist){
 			//判断同组是否在库存中已维护，存在未维护的则整组不静配
 			String ordsnParent = ex.get("ordsnParent").toString();
 			//药房主键不存在或包装量为空、为0均认为物品未维护
 			if(CommonUtils.isNull(ex.get("pkDeptStore"))||CommonUtils.isNull(ex.get("packSize"))||CommonUtils.getInt(ex.get("packSize"))==0){
 				ordsnSet.add(ordsnParent);
 				if(msg.toString().indexOf(ex.get("name").toString()+";"+ex.get("spec")==null?"":ex.get("spec").toString())>=0)
 					continue;
 				else{
 					msg.append(ex.get("name")+";"+ex.get("spec")+",");
 					continue;
 				}
 			}
 			if(CommonUtils.isEmptyString(pkDeptStore))
 				pkDeptStore = CommonUtils.getString(ex.get("pkDeptStore"));
 			
 			boolean hasFlag = false;
 			for (String ordsnP : updateMap.keySet()) {
				//存在同组医嘱的，合并执行单结合
				if(ordsnP.equals(ordsnParent)){
					Set<String> updateSqlList = updateMap.get(ordsnP);
					if(updateSqlList==null)
						updateSqlList = new HashSet<String>();
					updateSqlList.add(ex.get("pkExocc").toString());
					updateMap.put(ordsnParent,updateSqlList);
					hasFlag = true;
					break;
				}
			}
			if(!hasFlag){
				Set<String> updateListTemp = new HashSet<String>();
				updateListTemp.add(ex.get("pkExocc").toString());
				updateMap.put(ordsnParent, updateListTemp);
			}
 			//updateList.add(str.toString());
 		}
 		
 		for(String ordsnP : updateMap.keySet()){
 			//遍历含仓库中不存在药品的执行单集合
 			boolean hasFlag = false;
 			for(String ordSnTemp:ordsnSet){
 				if(ordsnP.equals(ordSnTemp)){
 					hasFlag = true;
 					break;
 				}
 			}
 			if(!hasFlag)
 				updateSet.addAll(updateMap.get(ordsnP));
 		}
 		//逐条更新，变更为in模式批量更新
 		if(null != updateSet && updateSet.size() > 0){
 			str = new StringBuilder("update ex_order_occ set flag_base='0',flag_pivas='1',flag_modi='1',pk_dept_occ =");
 			str.append("'");
 			str.append(pkDeptStore);
 			str.append("'");
 			str.append(",ts=to_date('");
  			str.append(now);
  			str.append("','YYYYMMDDHH24MISS'),modifier='pivas-");
  	      	str.append(user.getNameEmp());
  	      	str.append("' ");
  	        str.append(",modity_time=to_date('");
			str.append(now);
			str.append("','YYYYMMDDHH24MISS')");
 			str.append(" where pk_exocc in ( ");
 			str.append(CommonUtils.convertSetToSqlInPart(updateSet, "pk_exocc"));
 			str.append(") and eu_status='0' and flag_modi='0' ");
 			DataBaseHelper.update(str.toString(), new Object[]{});
 		}
 			
 		
 		if(msg!=null&&!"".equals(msg.toString())){
 			return "药品【"+msg.toString().substring(0, msg.toString().length()-1)+"】\n未在仓库物品字典中维护，无法发送静配科室！\n";
 		}
 		return null;
 	}
 	
 	/**
 	 * 查询待领药执行记录
 	 * @param param
 	 * @param user
 	 * @return
 	 */
 	public List<GeneratePdApExListVo> queryExlistToApPd(String param,IUser user){
 		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
 		paramMap.put("orderFlag", "1");//自定义排序
 		//List<GeneratePdApExListVo> list =  pdApplyMapper.getGenPdApOrdList(paramMap);
		String endDate = CommonUtils.getString(paramMap.get("endDate"));
		paramMap.put("endDate", endDate);
		List<GeneratePdApExListVo> list = new ArrayList<>();
		List<GeneratePdApExListVo> resultOth= pdApplyMapper.getGenPdApOrdList(paramMap);
		if(resultOth != null && resultOth.size()>0){
			list.addAll(resultOth);
		}
		//获取长期口服的执行单
		String endTime = ApplicationUtils.getSysparam("EX0007", false);//口服请领截止时间
		if(!CommonUtils.isEmptyString(endTime)){
			String OranlEndDate = endDate.substring(0, 8)+endTime.replace(":", "");
			paramMap.put("endDate", OranlEndDate);//如果ex0007不为空，截止时刻取ex0007
		}
		List<GeneratePdApExListVo> resultOral= pdApplyMapper.getGenPdApOrdOralList(paramMap);
		if(resultOral != null && resultOral.size()>0){
			list.addAll(resultOral);
		}
 		new GenerateExListSortByOrdUtil().ordGroup(list);
 		return list;
 	}
 	/**
 	 * 更新静配标志的执行单为非静配(只支持静配标志改为非静配的情况)
 	 * @param param{ordsPivas,pkDeptNs,endDate}
 	 * @param user
 	 */
 	private void updateExlistToNoPivas(PdApplyParamVo upDeptVo){
    	 //将执行单更新为非静配标志，执行科室更新为医嘱开立科室，基数药标志更新为原医嘱基数药标志
 		if(null == upDeptVo)
 			throw new BusException("未获取到要修改静配标志的入参！");
 		List<PdApplyVo> ordlist = upDeptVo.getOrdsPivas();
 		if(ordlist==null||ordlist.size()<=0)
 			throw new BusException("未获取到要修改静配标志的医嘱集合！");
    	StringBuilder str = null;
    	StringBuilder ordsql = null;
    	List<String> updateList = new ArrayList<String>();
    	//List<String> pkCnOrds = new ArrayList<String>();
    	String now = DateUtils.getDateTimeStr(new Date());
    	User user = UserContext.getUser();
    	Set<String> updateOrdSet = new HashSet<String>();
    	 for(PdApplyVo appvo:ordlist){
    		if(!"1".equals(upDeptVo.getFlagOnce())){//永久不静配，需要还原所有执行单
    			str = new StringBuilder("update ex_order_occ  set ex_order_occ.flag_base=(select cn_order.flag_base from cn_order   where cn_order.pk_cnord = ex_order_occ.pk_cnord),ex_order_occ.flag_pivas='0',ex_order_occ.pk_dept_occ =");
      			str.append("'");
      			str.append(appvo.getPkDeptExOrd());
      			str.append("'");
      			str.append(",ex_order_occ.ts=to_date('");
      			str.append(now);
      			str.append("','YYYYMMDDHH24MISS'),ex_order_occ.modifier='cancpivas-");
      			str.append(user.getNameEmp());
      			str.append("' ");
      			str.append(",ex_order_occ.modity_time=to_date('");
      			str.append(now);
      			str.append("','YYYYMMDDHH24MISS')");
      			str.append(" where (ex_order_occ.pk_pdapdt is null or ex_order_occ.pk_pdapdt='')  and ex_order_occ.pk_cnord ='");
      			str.append(appvo.getPkCnord());
      			str.append("'");
      			ordsql = new StringBuilder("update cn_order set flag_pivas='1' where pk_cnord='");
      			ordsql.append(appvo.getPkCnord());
      			ordsql.append("'");
      			updateOrdSet.add(ordsql.toString());
    		}else{
	  			str = new StringBuilder("update ex_order_occ  set ex_order_occ.flag_base=(select cn_order.flag_base from cn_order   where cn_order.pk_cnord = ex_order_occ.pk_cnord),ex_order_occ.flag_pivas='0',ex_order_occ.pk_dept_occ =");
	  			str.append("'");
	  			str.append(appvo.getPkDeptExOrd());
	  			str.append("'");
	  			//str.append(",ex_order_occ.pk_pdapdt = case (select cn_order.flag_base from cn_order   where cn_order.pk_cnord = ex_order_occ.pk_cnord)  when '1' then 'base_once_canc_pivas' else null end ");
	  			str.append(",ex_order_occ.ts=to_date('");
      			str.append(now);
      			str.append("','YYYYMMDDHH24MISS'),ex_order_occ.modifier='cancpivas-");
      			str.append(user.getNameEmp());
      			str.append("' ");
      			str.append(",ex_order_occ.modity_time=to_date('");
      			str.append(now);
      			str.append("','YYYYMMDDHH24MISS')");
	  			str.append(" where ex_order_occ.pk_exocc  in (");
	  			str.append(CommonUtils.convertSetToSqlInPart(new HashSet(appvo.getPkExLists()), "ex_order_occ.pk_exocc"));
	  			str.append(")");
    		}
  			updateList.add(str.toString());
  			//pkCnOrds.add(appvo.getPkCnord());
  		}
  		if(null != updateList && updateList.size() > 0)
  			DataBaseHelper.batchUpdate(updateList.toArray(new String[0]));
  		
  		if(null !=updateOrdSet && updateOrdSet.size()>0&&!"1".equals(upDeptVo.getFlagOnce()))
  			DataBaseHelper.batchUpdate(updateOrdSet.toArray(new String[0]));
 	}
}
