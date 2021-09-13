package com.zebone.nhis.base.pub.service;

import com.zebone.nhis.base.pub.dao.BdDumpMapper;
import com.zebone.nhis.base.pub.vo.DumpPubSearchVo;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.Application;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.*;

/**
 * 历史资料转存
 * @author ds
 *
 */
@Service
public class DumpPubService {
	private static Logger log = LoggerFactory.getLogger("com.zebone");
	
	@Resource
	private BdDumpMapper bdDumpMapper;
	/**
	 * 转存方向--转存
	 */
	public static final String SAVE = "0";
	/**
	 * 转存方向--还原
	 */
	public static final String REDUCTION = "1";
	
	/**
	 * 就诊类型--门诊
	 */
	public static final String EUTYPE_OP = "1";
	/** 就诊类型--住院
	 */
	public static final String EUTYPE_IP = "3";
	
	public static final int LIST_SIZE = 200;
	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	
	/**
	 * 查询要转存得患者信息列表（前端界面用）
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DumpPubSearchVo queryPiMasterData(String param,IUser user){
		Map<String,Object> qryparam = JsonUtil.readValue(param,Map.class);
		//qryparam.setPkDept(UserContext.getUser().getPkDept());
		if (null==qryparam)
			throw new BusException("查询参数不能为空！");
		if(null==qryparam.get("direction") || "".equals(qryparam.get("direction"))){
			throw new BusException("转存方向不能为空，请检查！");
		}
		if(null==qryparam.get("euPvtype") || "".equals(qryparam.get("euPvtype"))){
			throw new BusException("就诊类型不能为空，请检查！");
		}
		String direction=qryparam.get("direction").toString();
		if(SAVE.equals(direction)){//查询原表患者信息
			return queryPiMaster(qryparam);
		}else{//查询转存表患者信息
			return queryPiMasterByDump(qryparam);
		}
	}
	/**
	 * 查询可以转存的患者信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DumpPubSearchVo queryPiMaster(Map<String,Object> param){
		String defaultDays="60";
		if(null==param || null==param.get("days") || "".equals(param.get("days"))){
			defaultDays=ApplicationUtils.getSysparam("PUB0004", false);
		}
		if(null!=param && null!=param.get("days") && !"".equals(param.get("days"))){
			defaultDays=param.get("days").toString();
		}
		if(StringUtils.isEmpty(defaultDays)){
			log.info("****************默认天数为空，转存失败*****************");
			throw new BusException("默认天数为空，转存失败！");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		
		map.put("euPvtype", param.get("euPvtype"));
		map.put("codeIp", param.get("codeIp"));
		map.put("namePi", param.get("namePi"));
		map.put("endDate", DateUtils.getSpecifiedDateStr2(new Date(), -Integer.valueOf(defaultDays))+" 00:00:00");
		int pageIndex = CommonUtils.getInteger(param.get("pageIndex"));
		int pageSize = CommonUtils.getInteger(param.get("pageSize"));
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String,Object>> list=bdDumpMapper.queryPiMaster(map);
		Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
		DumpPubSearchVo dumpPubSearchVo=new DumpPubSearchVo();
		dumpPubSearchVo.setDataList(list);
		dumpPubSearchVo.setTotalCount(page.getTotalCount());
		return dumpPubSearchVo;
	}
	/**
	 * 查询转存表可以恢复的患者信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DumpPubSearchVo queryPiMasterByDump(Map<String,Object> param){
		String defaultDays="60";
		if(null==param || null==param.get("days") || "".equals(param.get("days"))){
			defaultDays=ApplicationUtils.getSysparam("PUB0004", false);
		}
		if(null!=param && null!=param.get("days") && !"".equals(param.get("days"))){
			defaultDays=param.get("days").toString();
		}
		if(StringUtils.isEmpty(defaultDays)){
			log.info("****************默认天数为空，转存失败*****************");
			throw new BusException("默认天数为空，转存失败！");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("euPvtype", param.get("euPvtype"));
		map.put("codeIp", param.get("codeIp"));
		map.put("namePi", param.get("namePi"));
		map.put("endDate", DateUtils.getSpecifiedDateStr2(new Date(), -Integer.valueOf(defaultDays))+" 00:00:00");
		int pageIndex = CommonUtils.getInteger(param.get("pageIndex"));
		int pageSize = CommonUtils.getInteger(param.get("pageSize"));
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String,Object>> list=bdDumpMapper.queryPiMasterByDump(map);
		Page<List<Map<String,Object>>> page = MyBatisPage.getPage();
		DumpPubSearchVo dumpPubSearchVo=new DumpPubSearchVo();
		dumpPubSearchVo.setDataList(list);
		dumpPubSearchVo.setTotalCount(page.getTotalCount());
		return dumpPubSearchVo;
	}
	/**
	 * 转存或者恢复（前端界面用）
	 * @param param pkpv列表   direction转存方向
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String dataOrDump(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		if (null==paramMap)
			throw new BusException("查询参数不能为空！");
		if(null==paramMap.get("direction") || "".equals(paramMap.get("direction"))){
			throw new BusException("转存方向不能为空，请检查！");
		}
		if(null==paramMap.get("euPvtype") || "".equals(paramMap.get("euPvtype"))){
			throw new BusException("就诊类型不能为空，请检查！");
		}
		String direction=paramMap.get("direction").toString();
		String euPvtype=paramMap.get("euPvtype").toString();
		List<String> pkPvList=Arrays.asList(String.valueOf(paramMap.get("pkPvs")).split(","));
		System.out.println("---------------开始时间"+new Date());
		try {
			if(SAVE.equals(direction)){//转存
				batDumpData(pkPvList,euPvtype);
			}else{//恢复
				recoveryData(pkPvList,euPvtype);
			}
		} catch (Exception e) {
			log.info("****************数据转存出现异常，转存失败："+e.getMessage()+"*****************");
			e.printStackTrace();
			return "数据转存出现异常，转存失败："+e.getMessage();
		}
		String str="";
		String pkPvs=CommonUtils.convertListToSqlInPart(pkPvList);
		List<PvEncounter> list=DataBaseHelper.queryForList("select * from PV_ENCOUNTER where pk_pv in ("+pkPvs+")",PvEncounter.class);
		if(SAVE.equals(direction)){//转存
			for (PvEncounter pvEncounter : list) {
				str=str+"患者 "+pvEncounter.getNamePi()+"：在线转历史->数据转储成功! \r\n";
			}
		}else{//恢复
			for (PvEncounter pvEncounter : list) {
				str=str+"患者 "+pvEncounter.getNamePi()+"：历史转在线->数据转储成功! \r\n";
			}
		}
		System.out.println("---------------完成时间"+new Date());
		return str;
	}
	
	
	/**
	 * 取消结算恢复数据
	 * @param pkPvList
	 * @param euPvtype
	 */
	public void recoveryDataByPkpv(List<String> pkPvList,String euPvtype){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("euPvtype", euPvtype);
		map.put("direction", REDUCTION);
		map.put("pkPv",pkPvList.get(0));
	    List<Map<String,Object>> list=bdDumpMapper.queryPiMasterByDump(map);
	    if(null!=list && list.size()>0){
	    	recoveryData(pkPvList,euPvtype);
	    }
	}
	/**
	 * 批量转存数据
	 * @param pkPvList 
	 * @param euPvtype 就诊类型
	 */
	public void batDumpData(List<String> pkPvList,String euPvtype){
		Map<Integer,List<String>> itemMap = new BatchListUtil<String>().batchList(pkPvList, LIST_SIZE);//拆分LIST,每个最大200个
		for(List<String> list : itemMap.values()){
			String pkPvs=CommonUtils.convertListToSqlInPart(list);
			// 关闭事务自动提交
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus status = platformTransactionManager.getTransaction(def);
			try{
				//批量转存数据
				batInsDataToDump(pkPvs,euPvtype);
				//删除数据
				batDelDataTobusiness(pkPvs,euPvtype);
				platformTransactionManager.commit(status); // 提交事务
			}catch(Exception e){
				platformTransactionManager.rollback(status); // 添加失败 回滚事务；
				log.info("****************数据转存出现异常，转存失败："+e.getMessage()+"*****************");
				e.printStackTrace();
				throw new BusException(e.getMessage());
			}
		}
	}
	/**
	 * 批量恢复数据
	 * @param pkPvList 
	 */
	public void recoveryData(List<String> pkPvList,String euPvtype){
		String pkPvs=CommonUtils.convertListToSqlInPart(pkPvList);
		// 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		try{
			//批量转存数据
			batInsertDataToBusiness(pkPvs,euPvtype);
			//删除数据
			batDelDataToDump(pkPvs,euPvtype);
			platformTransactionManager.commit(status); // 提交事务
		}catch(Exception e){
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			log.info("****************数据恢复出现异常，恢复失败："+e.getMessage()+"*****************");
			e.printStackTrace();
			throw new BusException("数据恢复出现异常，恢复失败："+e.getMessage());
		}
	}
	
	
	/**
	 * 转存时-批量转存数据
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsDataToDump(String pkPvs,String euPvtype){
		//转存医嘱
		batInsertCnOrderByPkPv(pkPvs);
		//检查申请
		batInsertRisApplyByPkPv(pkPvs);
		//检验申请
		batInsertLabApplyByPkPv(pkPvs);
		//转存医嘱-处方
		batInsertPrescriptionByPkPv(pkPvs);
		//住院
		if(EUTYPE_IP.equals(euPvtype)){
			//医嘱-打印
			batInsertOrderPrintByPkPv(pkPvs);
			//转存医嘱-输血申请
			batInsertTransApplyByPkPv(pkPvs);
			//转存医嘱-会诊申请
			batInsertConsultApplyByPkPv(pkPvs);
			//转存医嘱-会诊应答
			batInsertConsultResponseByPkPv(pkPvs);
			//转存医嘱-手术申请
			batInsertOpApplyByPkPv(pkPvs);
			//转存执行单
			batInsertOrderOccByPkPv(pkPvs);
			//转存执行单打印
			batInsertOrderOccPrtByPkPv(pkPvs);
			//转存领药申请
			batInsertPdApplyByPkPv(pkPvs);
			//转存发药记录
			batInsertPdDeByPkPv(pkPvs);
			//转存记费明细
			batInsertIpDtByPkPv(pkPvs);
		}else{//门诊
			//转存记费明细
			batInsertOpDtByPkPv(pkPvs);
			//转存预约登记信息
			batInsertSchApptByPkpv(pkPvs);
			//转存门诊医技执行记录
			batInsertExAssistOcc(pkPvs);
			//转储处方执行
			batInsertExPresOcc(pkPvs);
		}
		return false;
	}
	/**
	 * 转存时批量删除数据
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelDataTobusiness(String pkPvs,String euPvtype){
		//检查申请
		batDelRisApplyByPkPv(pkPvs);
		//检验申请
		batDelLabApplyByPkPv(pkPvs);
		//转存医嘱-处方
		batDelPrescriptionByPkPv(pkPvs);
		//住院
		if(EUTYPE_IP.equals(euPvtype)){
			//医嘱-打印
			batDelOrderPrintByPkPv(pkPvs);
			//医嘱-输血申请
			batDelTransApplyByPkPv(pkPvs);
			//医嘱-会诊应答
			batDelConsultResponseByPkPv(pkPvs);
			//医嘱-会诊申请
			batDelConsultApplyByPkPv(pkPvs);
			//医嘱-手术申请
			batDelOpApplyByPkPv(pkPvs);
			//执行单打印
			batDelOrderOccPrtByPkPv(pkPvs);
			//执行单
			batDelOrderOccByPkPv(pkPvs);
			//领药申请
			batDelPdApplyByPkPv(pkPvs);
			//发药记录
			batDelPdDeByPkPv(pkPvs);
			//记费明细
			batDelIpDtByPkPv(pkPvs);
		}else{//门诊
			//记费明细
			batDelOpDtByPkPv(pkPvs);
			//预约记录
			batDelSchApptByPkPv(pkPvs);
			//删除门诊医技执行记录
			batDelExAssistOcc(pkPvs);
			//删除处方执行
			batDelExPresOcc(pkPvs);
		}
		//医嘱
		batDelCnOrderByPkPv(pkPvs);
		return false;
	}
	
	
	/************************************************************以下为转存代码****************************************************************/
	/**
	 * 根据pkpv批量转存医嘱
	 * @param depts
	 * @return
	 */
	private boolean batInsertCnOrderByPkPv(String pkPvs){
		int result=DataBaseHelper.execute("insert into cn_order_b select * from cn_order where pk_pv in ("+pkPvs+")");
		//int result=bdDumpMapper.batInsertCnOrderByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条医嘱到cn_order_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量转存检查申请
	 * @param depts
	 * @return
	 */
	private boolean batInsertRisApplyByPkPv(String pkPvs){
		String sql="insert into cn_ris_apply_b"
  				  +" select a.* from cn_ris_apply a"
                  +" inner join cn_order b on a.pk_cnord=b.pk_cnord where b.pk_pv in ("+pkPvs+")";
        int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batInsertRisApplyByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条检查申请到cn_ris_apply_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量转存检验申请
	 * @param depts
	 * @return
	 */
	private boolean batInsertLabApplyByPkPv(String pkPvs){
		String sql=" insert into cn_lab_apply_b"
  				+" select a.* from cn_lab_apply a"
                +" inner join cn_order b on a.pk_cnord=b.pk_cnord where b.pk_pv in  ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batInsertRisApplyByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条检验申请到cn_lab_apply_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量转存医嘱-处方
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertPrescriptionByPkPv(String pkPvs){
		 
		 int result=DataBaseHelper.execute("insert into cn_prescription_b select * from cn_prescription where pk_pv in ("+pkPvs+")");
		//int result=bdDumpMapper.batInsertRisApplyByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条医嘱-处方到cn_prescription_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量转存医嘱-打印
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertOrderPrintByPkPv(String pkPvs){
		 int result=DataBaseHelper.execute("insert into cn_order_print_b select * from cn_order_print where pk_pv in ("+pkPvs+")");
		log.info("****************成功转存"+result+"条医嘱-打印到cn_order_print_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量转存医嘱-输血申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertTransApplyByPkPv(String pkPvs){
		String sql=" insert into cn_trans_apply_b"
  			+" select a.* from cn_trans_apply a"
  			+" inner join cn_order b on a.pk_cnord=b.pk_cnord where b.pk_pv in  ("+pkPvs+")";
		 int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batInsertTransApplyByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条医嘱-输血申请到cn_trans_apply_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量转存医嘱-会诊申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertConsultApplyByPkPv(String pkPvs){
		String sql="insert into cn_consult_apply_b"
	  			+" select a.* from cn_consult_apply a"
	  			+" inner join cn_order b on a.pk_cnord=b.pk_cnord where b.pk_pv in  ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batInsertConsultApplyByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条医嘱-会诊申请到cn_consult_apply_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量转存医嘱-会诊应答
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertConsultResponseByPkPv(String pkPvs){
		String sql="insert into cn_consult_response_b"
	  			+" select a.* from cn_consult_response a"
	  			+" inner join cn_consult_apply b on a.pk_cons=b.pk_cons"
	  			+" inner join cn_order c on b.pk_cnord=c.pk_cnord where c.pk_pv in  ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batInsertConsultResponseByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条医嘱-会诊应答到cn_consult_response_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量转存医嘱-手术申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertOpApplyByPkPv(String pkPvs){
		String sql="insert into cn_op_apply_b"
	  			+" select a.* from cn_op_apply a"
	  			+" inner join cn_order b on a.pk_cnord=b.pk_cnord where b.pk_pv in  ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batInsertOpApplyByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条医嘱-手术申请到cn_op_apply_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量转存执行单
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertOrderOccByPkPv(String pkPvs){
		int result=DataBaseHelper.execute("insert into ex_order_occ_b select * from ex_order_occ where pk_pv in  ("+pkPvs+")");
		//int result=bdDumpMapper.batInsertOrderOccByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条执行单到ex_order_occ_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量转存执行单打印
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertOrderOccPrtByPkPv(String pkPvs){
		String sql="insert into ex_order_occ_prt_b"
	  			+" select a.* from ex_order_occ_prt a"
	  			+" inner join ex_order_occ b on a.pk_exocc=b.pk_exocc where b.pk_pv in  ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batInsertOrderOccPrtByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条执行单打印到ex_order_occ_prt_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量转存领药申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertPdApplyByPkPv(String pkPvs){
		int result=DataBaseHelper.execute("insert into ex_pd_apply_detail_b select * from ex_pd_apply_detail where pk_pv in ("+pkPvs+")");
		//int result=bdDumpMapper.batInsertPdApplyByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条领药申请到ex_pd_apply_detail_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量转存发药记录
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertPdDeByPkPv(String pkPvs){
		int result=DataBaseHelper.execute("insert into ex_pd_de_b select * from ex_pd_de where pk_pv in  ("+pkPvs+")");
		//int result=bdDumpMapper.batInsertPdDeByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条发药记录到ex_pd_de_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量转存记费明细
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertIpDtByPkPv(String pkPvs){
		int result=DataBaseHelper.execute("insert into bl_ip_dt_b select * from bl_ip_dt where pk_pv in  ("+pkPvs+")");
		//int result=bdDumpMapper.batInsertIpDtByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条记费明细记录到bl_ip_dt_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量转存记费明细-门诊
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertOpDtByPkPv(String pkPvs){
		int result=DataBaseHelper.execute("insert into BL_OP_DT_B select * from BL_OP_DT where pk_pv in  ("+pkPvs+")");
		//int result=bdDumpMapper.batInsertOpDtByPkPv(pkPvList);
		log.info("****************成功转存"+result+"条记费明细记录到bl_op_dt_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  批量转存排班预约-排班————门诊
	 * @param pkPvList
	 * @return
	 */
	public boolean batInsertSchSchByDate(String endDate){
		int result=0;
		if (Application.isSqlServer()) {
			result=DataBaseHelper.execute("insert into SCH_SCH_B select * from SCH_SCH where DATE_WORK<=convert(datetime,'"+endDate+"',17) ");
	    } else {
	    	result=DataBaseHelper.execute("insert into SCH_SCH_B select * from SCH_SCH where DATE_WORK<=to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss')");
	    }
		//int result=bdDumpMapper.batInsertSchSchByDate(endDate);
		log.info("****************成功转存"+result+"条排班记录到sch_schb表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  批量删除排班预约-排班————门诊
	 * @param pkPvList
	 * @return
	 */
	public boolean batdelSchSchByDate(String endDate){
		int result=0;
		int result2=0;
		if (Application.isSqlServer()) {
			result2=DataBaseHelper.execute("delete from SCH_TICKET  where exists(select 1 from SCH_SCH sch where sch.DATE_WORK<convert(datetime,'"+endDate+"',17) and sch.PK_SCH=SCH_TICKET.PK_SCH)");
			result=DataBaseHelper.execute("delete from SCH_SCH where DATE_WORK<convert(datetime,'"+endDate+"',17) ");
		} else {
			result2=DataBaseHelper.execute("delete from SCH_TICKET  where exists(select 1 from SCH_SCH sch where sch.DATE_WORK<to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss') and sch.PK_SCH=SCH_TICKET.PK_SCH)");
			result=DataBaseHelper.execute("delete from SCH_SCH where DATE_WORK<to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss')");
		}
		//int result=bdDumpMapper.batInsertSchSchByDate(endDate);
		log.info("****************成功删除"+result+"条排班记录到sch_sch表*****************");
		log.info("****************成功删除"+result2+"条号表记录到sch_ticket表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  批量删除排班明细————门诊
	 * @param pkPvList
	 * @return
	 */
	public boolean batInsertSchTicketByDate(String endDate){
		int result=0;
		if (Application.isSqlServer()) {
			result=DataBaseHelper.execute(" delete sch_ticket  where END_TIME<=convert(datetime,'"+endDate+"',17) ");
	    } else {
	    	result=DataBaseHelper.execute(" delete sch_ticket  where END_TIME<=to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss')");
	    }
		
		//int result=bdDumpMapper.batInsertSchSchByDate(endDate);
		log.info("****************成功删除"+result+"条号表明细*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  批量删除门诊排队记录————门诊
	 * @param pkPvList
	 * @return
	 */
	public boolean batInsertPvQueByDate(String endDate){
		int result=0;
		if (Application.isSqlServer()) {
			result=DataBaseHelper.execute(" delete pv_que  where DATE_PV<=convert(datetime,'"+endDate+"',17) ");
	    } else {
	    	result=DataBaseHelper.execute(" delete pv_que  where DATE_PV<=to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss')");
	    }
		//int result=bdDumpMapper.batInsertSchSchByDate(endDate);
		log.info("****************成功删除"+result+"条门诊排队记录*****************");
		if(result>0){
			return true;
		}
		return false;
	}

	/**
	 * 批量转存预约表数据
	 * @param endDate
	 */
	public boolean batInsertSchApptByPkpv(String pkPvs) {
		int result=0;
		int res=0;

			result=DataBaseHelper.execute("insert into SCH_APPT_B select * from SCH_APPT appt " +
					"where exists(select * from SCH_APPT_PV ap where ap.PK_PV in ("+pkPvs+") and ap.PK_SCHAPPT = appt.PK_SCHAPPT)");
			res=DataBaseHelper.execute("insert into SCH_APPT_PV_B select * from SCH_APPT_PV where PK_PV in ("+pkPvs+")");
		log.info("****************成功转存"+result+"条预约记录到shc_appt_b表*****************");
		log.info("****************成功转存"+res+"条预约就诊记录到shc_appt_pv_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/*************************************************下面为删除数据*************************************************/
	
	/**
	 * 根据pkpv批量删除已转存检查申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelRisApplyByPkPv(String pkPvs){
		String sql="delete from cn_ris_apply "
		           +" where exists (select * from cn_order b where cn_ris_apply.pk_cnord=b.pk_cnord and b.pk_pv in  ("+pkPvs+") )";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelIpDtByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条检查申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已转存检验申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelLabApplyByPkPv(String pkPvs){
		String sql="delete from cn_lab_apply "
		           +" where exists (select * from cn_order b where cn_lab_apply.pk_cnord=b.pk_cnord and b.pk_pv in   ("+pkPvs+"))";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelIpDtByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条检验申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已转存医嘱-处方
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelPrescriptionByPkPv(String pkPvs){
		String sql=" delete from cn_prescription where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelIpDtByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条医嘱-处方*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已转存医嘱-打印
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelOrderPrintByPkPv(String pkPvs){
		String sql="delete from cn_order_print where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelOrderPrintByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条医嘱-打印*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已转存医嘱-输血申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelTransApplyByPkPv(String pkPvs){
		String sql=" delete from cn_trans_apply "
				+ " where exists (select * from cn_order b where cn_trans_apply.pk_cnord=b.pk_cnord and b.pk_pv in ("+pkPvs+"))";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelTransApplyByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条医嘱-输血申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已转存医嘱-会诊应答
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelConsultResponseByPkPv(String pkPvs){
		String sql="delete from cn_consult_response "
				+ " where exists (select * from cn_consult_apply b"
				+ "  inner join cn_order c on b.pk_cnord=c.pk_cnord"
				+ " where cn_consult_response.pk_cons=b.pk_cons and c.pk_pv in ("+pkPvs+"))";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelConsultResponseByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条医嘱-会诊应答*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已转存医嘱-会诊申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelConsultApplyByPkPv(String pkPvs){
		String sql="delete from cn_consult_apply "
				+ " where exists (select * from cn_order b where cn_consult_apply.pk_cnord=b.pk_cnord and b.pk_pv in ("+pkPvs+"))";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelConsultApplyByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条医嘱-会诊申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 根据pkpv批量删除已转存医嘱-手术申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelOpApplyByPkPv(String pkPvs){
		String sql="delete from cn_op_apply "
				+ " where exists (select * from cn_order b where cn_op_apply.pk_cnord=b.pk_cnord and b.pk_pv in ("+pkPvs+"))";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelOpApplyByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条医嘱-手术申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已转存执行单打印
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelOrderOccPrtByPkPv(String pkPvs){
		String sql=" delete from ex_order_occ_prt "
				+ " where exists (select * from ex_order_occ b"
				+ "  inner join cn_order c on b.pk_cnord=c.pk_cnord"
				+ " where ex_order_occ_prt.pk_exocc=b.pk_exocc and c.pk_pv in  ("+pkPvs+"))";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelOrderOccPrtByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条执行单打印*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已转存执行单
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelOrderOccByPkPv(String pkPvs){
		String sql="  delete from ex_order_occ where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelOrderOccByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条执行单*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已转存领药申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelPdApplyByPkPv(String pkPvs){
		String sql="delete from ex_pd_apply_detail where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelPdApplyByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条领药申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已转存发药记录
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelPdDeByPkPv(String pkPvs){
		String sql="delete from ex_pd_de where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelPdDeByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条发药记录*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已转存记费明细
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelIpDtByPkPv(String pkPvs){
		String sql="delete from bl_ip_dt where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelIpDtByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条记费明细*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已转存预约记录--门诊
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelSchApptByPkPv(String pkPvs){
		String sql2="delete from SCH_APPT  where exists(select 1 from SCH_APPT_PV pv where SCH_APPT.PK_SCHAPPT=pv.PK_SCHAPPT and pv.PK_PV in ("+pkPvs+"))";
		String sql="delete from sch_appt_pv where pk_pv in ("+pkPvs+")";
		int result2=DataBaseHelper.execute(sql2);
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条预约记录(sch_appt_pv)--门诊*****************");
		log.info("****************成功删除"+result2+"条预约记录(sch_appt)--门诊*****************");
		if(result>0){
			return true;
		}
		return false;
	}

	/**
	 * 根据pkpv批量删除已转存预约记录-门诊
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelOpDtByPkPv(String pkPvs){
		//int result=bdDumpMapper.batDelOpDtByPkPv(pkPvList);
		String sql="delete from BL_OP_DT where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条记费明细--门诊*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已转存医嘱
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelCnOrderByPkPv(String pkPvs){
		String sql=" delete from cn_order where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batDelCnOrderByPkPv(pkPvList);
		log.info("****************成功删除"+result+"条医嘱*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/*************************************************删除数据结束***************************************************/
	/************************************************************以上为转存代码****************************************************************/
	/************************************************************以下为恢复时代码**************************************************************/
	
	/**
	 * 恢复时-批量恢复数据
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertDataToBusiness(String pkPvs,String euPvtype){
		//恢复医嘱
		batInsertCnOrderByPkPvR(pkPvs);
		//检查申请
		batInsertRisApplyByPkPvR(pkPvs);
		//检验申请
		batInsertLabApplyByPkPvR(pkPvs);
		//恢复医嘱-处方
		batInsertPrescriptionByPkPvR(pkPvs);
		//住院
		if(EUTYPE_IP.equals(euPvtype)){
			//医嘱-打印
			batInsertOrderPrintByPkPvR(pkPvs);
			//恢复医嘱-输血申请
			batInsertTransApplyByPkPvR(pkPvs);
			//恢复医嘱-会诊申请
			batInsertConsultApplyByPkPvR(pkPvs);
			//恢复医嘱-会诊应答
			batInsertConsultResponseByPkPvR(pkPvs);
			//恢复医嘱-手术申请
			batInsertOpApplyByPkPvR(pkPvs);
			//恢复执行单
			batInsertOrderOccByPkPvR(pkPvs);
			//恢复执行单打印
			batInsertOrderOccPrtByPkPvR(pkPvs);
			//恢复领药申请
			batInsertPdApplyByPkPvR(pkPvs);
			//恢复发药记录
			batInsertPdDeByPkPvR(pkPvs);
			//恢复记费明细
			batInsertIpDtByPkPvR(pkPvs);
		}else{//门诊
			//恢复记费明细
			batInsertOpDtByPkPvR(pkPvs);
			//恢复预约记录
			batInsertSchApptByPkPvR(pkPvs);
			//恢复门诊医技执行记录
			batInsertExAssistOccD(pkPvs);
			//恢复处方执行单
			batInsertExPresOccD(pkPvs);
		}
		return false;
	}
	/**
	 * 恢复时批量删除数据
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelDataToDump(String pkPvs,String euPvtype){
		//检查申请
		batDelRisApplyByPkPvR(pkPvs);
		//检验申请
		batDelLabApplyByPkPvR(pkPvs);
		//恢复医嘱-处方
		batDelPrescriptionByPkPvR(pkPvs);
		//住院
		if(EUTYPE_IP.equals(euPvtype)){
			//恢复医嘱-打印
			batDelOrderPrintByPkPvR(pkPvs);
			//恢复医嘱-输血申请
			batDelTransApplyByPkPvR(pkPvs);
			//恢复医嘱-会诊应答
			batDelConsultResponseByPkPvR(pkPvs);
			//恢复医嘱-会诊申请
			batDelConsultApplyByPkPvR(pkPvs);
			//恢复医嘱-手术申请
			batDelOpApplyByPkPvR(pkPvs);
			//恢复执行单打印
			batDelOrderOccPrtByPkPvR(pkPvs);
			//恢复执行单
			batDelOrderOccByPkPvR(pkPvs);
			//恢复领药申请
			batDelPdApplyByPkPvR(pkPvs);
			//恢复发药记录
			batDelPdDeByPkPvR(pkPvs);
			//恢复记费明细
			batDelIpDtByPkPvR(pkPvs);
		}else{//门诊
			//记费明细
			batDelOpDtByPkPvR(pkPvs);
			//删除B表的预约数据
			batDelSchApptByPkPvR(pkPvs);
			//删除b表的门诊医技执行记录
			batDelExAssistOccD(pkPvs);
			//删除b表处方执行单
			batDelExPresOccD(pkPvs);
		}
		////恢复医嘱
		batDelCnOrderByPkPvR(pkPvs);
		return false;
	}
	/**
	 * 根据pkpv批量恢复医嘱
	 * @param depts
	 * @return
	 */
	private boolean batInsertCnOrderByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertCnOrderByPkPvR(pkPvList);
		int result=DataBaseHelper.execute("insert into cn_order select * from cn_order_b where pk_pv in ("+pkPvs+")");
		log.info("****************成功恢复"+result+"条医嘱到cn_order表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量恢复检查申请
	 * @param depts
	 * @return
	 */
	private boolean batInsertRisApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertRisApplyByPkPvR(pkPvList);
		String sql="insert into cn_ris_apply"
				  +" select a.* from cn_ris_apply_b a"
                +" inner join cn_order_b b on a.pk_cnord=b.pk_cnord where b.pk_pv in ("+pkPvs+")";
        int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条检查申请到cn_ris_apply表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量恢复检验申请
	 * @param depts
	 * @return
	 */
	private boolean batInsertLabApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertRisApplyByPkPvR(pkPvList);
		String sql=" insert into cn_lab_apply"
  				+" select a.* from cn_lab_apply_b a"
                +" inner join cn_order_b b on a.pk_cnord=b.pk_cnord where b.pk_pv in  ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条检验申请到cn_lab_apply表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量恢复医嘱-处方
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertPrescriptionByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertRisApplyByPkPvR(pkPvList);
		 int result=DataBaseHelper.execute("insert into cn_prescription select * from cn_prescription_b where pk_pv in ("+pkPvs+")");
		log.info("****************成功恢复"+result+"条医嘱-处方到cn_prescription表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量恢复医嘱-打印
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertOrderPrintByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertOrderPrintByPkPvR(pkPvList);
		int result=DataBaseHelper.execute("insert into cn_order_print select * from cn_order_print_b where pk_pv in ("+pkPvs+")");
		log.info("****************成功恢复"+result+"条医嘱-打印到cn_order_print表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量恢复医嘱-输血申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertTransApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertTransApplyByPkPvR(pkPvList);
		String sql=" insert into cn_trans_apply"
	  			+" select a.* from cn_trans_apply_b a"
	  			+" inner join cn_order_b b on a.pk_cnord=b.pk_cnord where b.pk_pv in  ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条医嘱-输血申请到cn_trans_apply表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量恢复医嘱-会诊申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertConsultApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertConsultApplyByPkPvR(pkPvList);
		String sql="insert into cn_consult_apply"
	  			+" select a.* from cn_consult_apply_b a"
	  			+" inner join cn_order_b b on a.pk_cnord=b.pk_cnord where b.pk_pv in  ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条医嘱-会诊申请到cn_consult_apply表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量恢复医嘱-会诊应答
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertConsultResponseByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertConsultResponseByPkPvR(pkPvList);
		String sql="insert into cn_consult_response"
	  			+" select a.* from cn_consult_response_b a"
	  			+" inner join cn_consult_apply_b b on a.pk_cons=b.pk_cons"
	  			+" inner join cn_order_b c on b.pk_cnord=c.pk_cnord where c.pk_pv in  ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条医嘱-会诊应答到cn_consult_response表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量恢复医嘱-手术申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertOpApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertOpApplyByPkPvR(pkPvList);
		String sql="insert into cn_op_apply"
	  			+" select a.* from cn_op_apply_b a"
	  			+" inner join cn_order_b b on a.pk_cnord=b.pk_cnord where b.pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条医嘱-手术申请到cn_op_apply表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量恢复执行单
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertOrderOccByPkPvR(String pkPvs){
		String sql="insert into ex_order_occ select * from ex_order_occ_b where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		//int result=bdDumpMapper.batInsertOrderOccByPkPvR(pkPvList);
		log.info("****************成功恢复"+result+"条执行单到ex_order_occ表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量恢复执行单打印
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertOrderOccPrtByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertOrderOccPrtByPkPvR(pkPvList);
		String sql="insert into ex_order_occ_prt"
	  			+" select a.* from ex_order_occ_prt_b a"
	  			+" inner join ex_order_occ_b b on a.pk_exocc=b.pk_exocc where b.pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条执行单打印到ex_order_occ_prt表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量恢复领药申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertPdApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertPdApplyByPkPvR(pkPvList);
		String sql="insert into ex_pd_apply_detail select * from ex_pd_apply_detail_b where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条领药申请到ex_pd_apply_detail表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量恢复发药记录
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertPdDeByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertPdDeByPkPvR(pkPvList);
		String sql="insert into ex_pd_de select * from ex_pd_de_b where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条发药记录到ex_pd_de表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量恢复记费明细
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertIpDtByPkPvR(String pkPvs){
		String sql="insert into bl_ip_dt select * from bl_ip_dt_b where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条发药记录到bl_ip_dt_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 *  根据pkpv批量恢复记费明细-门诊
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertOpDtByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batInsertOpDtByPkPvR(pkPvList);
		String sql="insert into BL_OP_DT select * from BL_OP_DT_B where pk_pv in ("+pkPvs+")";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功恢复"+result+"条计费明细到BL_OP_DT表*****************");
		if(result>0){
			return true;
		}
		return false;
	}

	/**
	 *  根据pkpv批量恢复记费明细-门诊
	 * @param pkPvList
	 * @return
	 */
	private boolean batInsertSchApptByPkPvR(String pkPvs){
		String sql="insert into SCH_APPT_PV select * from SCH_APPT_PV_B where PK_PV in ("+pkPvs+")";
		String sql1="insert into SCH_APPT select * from SCH_APPT_B apptb where exists(select 1 from SCH_APPT_PV_B pvb where PK_PV in ("+pkPvs+") and pvb.PK_SCHAPPT=apptb.PK_SCHAPPT)";
		int result=DataBaseHelper.execute(sql);
		int result2=DataBaseHelper.execute(sql1);
		log.info("****************成功恢复"+result2+"条计费明细到sch_appt表*****************");
		log.info("****************成功恢复"+result+"条计费明细到sch_appt_pv表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	
	/*************************************************下面为删除数据*************************************************/
	
	/**
	 * 根据pkpv批量删除已恢复检查申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelRisApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelRisApplyByPkPvR(pkPvList);
		String sql="delete from cn_ris_apply_b a"
				+ " where exists (select * from cn_order_b b where a.pk_cnord=b.pk_cnord and b.pk_pv in ("+pkPvs+") )";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条检查申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已恢复检验申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelLabApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelLabApplyByPkPvR(pkPvList);
		String sql="delete from cn_lab_apply_b a"
				+ " where exists (select * from cn_order_b b where a.pk_cnord=b.pk_cnord and b.pk_pv in ("+pkPvs+") )";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条检验申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已恢复医嘱-处方
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelPrescriptionByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelPrescriptionByPkPvR(pkPvList);
		String sql="delete from cn_prescription_b where pk_pv in ("+pkPvs+") ";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条医嘱-处方*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已恢复医嘱-打印
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelOrderPrintByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelOrderPrintByPkPvR(pkPvList);
		String sql="delete from cn_order_print_b where pk_pv in ("+pkPvs+") ";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条医嘱-打印*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已恢复医嘱-输血申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelTransApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelTransApplyByPkPvR(pkPvList);
		String sql=" delete from cn_trans_apply_b a"
				+ " where exists (select * from cn_order_b b where a.pk_cnord=b.pk_cnord and b.pk_pv in ("+pkPvs+") )";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条医嘱-输血申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已恢复医嘱-会诊应答
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelConsultResponseByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelConsultResponseByPkPvR(pkPvList);
		String sql=" delete from cn_consult_response_b a"
				+ " where exists (select * from cn_consult_apply_b b"
				+ " inner join cn_order_b c on b.pk_cnord=c.pk_cnord"
				+ " where a.pk_cons=b.pk_cons and c.pk_pv in ("+pkPvs+") )";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条医嘱-会诊应答*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已恢复医嘱-会诊申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelConsultApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelConsultApplyByPkPvR(pkPvList);
		String sql=" delete from cn_consult_apply_b a"
				+ " where exists (select * from cn_order_b b where a.pk_cnord=b.pk_cnord and b.pk_pv in ("+pkPvs+") )";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条医嘱-会诊申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 根据pkpv批量删除已恢复医嘱-手术申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelOpApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelOpApplyByPkPvR(pkPvList);
		String sql=" delete from cn_op_apply_b a"
				+ " where exists (select * from cn_order_b b where a.pk_cnord=b.pk_cnord and b.pk_pv in ("+pkPvs+") )";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条医嘱-手术申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已恢复执行单打印
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelOrderOccPrtByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelOrderOccPrtByPkPvR(pkPvList);
		String sql=" delete from ex_order_occ_prt_b a"
				+ " where exists (select * from ex_order_occ_b b"
				+ " inner join cn_order_b c on b.pk_cnord=c.pk_cnord"
				+ " where a.pk_exocc=b.pk_exocc and c.pk_pv in ("+pkPvs+") )";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条执行单打印*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已恢复执行单
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelOrderOccByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelOrderOccByPkPvR(pkPvList);
		String sql="delete from ex_order_occ_b where pk_pv in  ("+pkPvs+") ";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条执行单*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已恢复领药申请
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelPdApplyByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelPdApplyByPkPvR(pkPvList);
		String sql="delete from ex_pd_apply_detail_b where pk_pv in  ("+pkPvs+") ";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条领药申请*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据pkpv批量删除已恢复发药记录
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelPdDeByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelPdDeByPkPvR(pkPvList);
		String sql="delete from ex_pd_de_b where pk_pv in ("+pkPvs+") ";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条发药记录*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已恢复记费明细
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelIpDtByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelIpDtByPkPvR(pkPvList);
		String sql="delete from bl_ip_dt_b where pk_pv in ("+pkPvs+") ";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条记费明细*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已恢复记费明细--门诊
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelOpDtByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelOpDtByPkPvR(pkPvList);
		String sql="delete from BL_OP_DT_b where pk_pv in ("+pkPvs+") ";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条计费明细--门诊*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已恢复预约记录--门诊
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelSchApptByPkPvR(String pkPvs){
		String sql1="delete from SCH_APPT_B aptb where exists(select 1 from SCH_APPT_PV_B pvb where pvb.PK_SCHAPPT=aptb.PK_SCHAPPT and pvb.PK_PV in ("+pkPvs+"))";
		String sql="delete from sch_appt_pv_b where pk_pv in ("+pkPvs+") ";
		int result1=DataBaseHelper.execute(sql1);
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条预约记录（sch_appt_pv_b）--门诊*****************");
		log.info("****************成功删除"+result1+"条预约记录（SCH_APPT_B）--门诊*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 根据pkpv批量删除已恢复医嘱
	 * @param pkPvList
	 * @return
	 */
	private boolean batDelCnOrderByPkPvR(String pkPvs){
		//int result=bdDumpMapper.batDelCnOrderByPkPvR(pkPvList);
		String sql="delete from cn_order_b where pk_pv in ("+pkPvs+") ";
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条医嘱*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	/************************************************************以上为恢复时代码**************************************************************/
	
	/**
	 * 查询病区或科室列表
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> queryDeptList(Map<String,Object> param){
		String defaultDays="60";
		if(null==param || null==param.get("days") || "".equals(param.get("days"))){
			defaultDays=ApplicationUtils.getSysparam("PUB0004", false);
		}
		if(StringUtils.isEmpty(defaultDays)){
			log.info("****************默认天数为空，转存失败*****************");
			throw new BusException("默认天数为空，转存失败！");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("euPvtype", param.get("euPvtype"));
		map.put("endDate", DateUtils.getSpecifiedDateStr2(new Date(), -Integer.valueOf(defaultDays))+" 00:00:00");
		return bdDumpMapper.queryDeptList(map);
	}
	/**
	 * 按科室查询可以转存的患者信息（定时器用）
	 * @return
	 */
	public List<Map<String,Object>> queryPiMasterByDept(Map<String,Object> param){
		String defaultDays="60";
		if(null==param || null==param.get("days") || "".equals(param.get("days"))){
			defaultDays=ApplicationUtils.getSysparam("PUB0004", false);
		}
		if(StringUtils.isEmpty(defaultDays)){
			log.info("****************默认天数为空，转存失败*****************");
			throw new BusException("默认天数为空，转存失败！");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		
		map.put("euPvtype", param.get("euPvtype"));
		map.put("codeIp", param.get("codeIp"));
		map.put("namePi", param.get("namePi"));
		map.put("pkDept", param.get("pkDept"));
		map.put("endDate", DateUtils.getSpecifiedDateStr2(new Date(), -Integer.valueOf(defaultDays))+" 00:00:00");
		return bdDumpMapper.queryPiMaster(map);
	}
	private boolean batInsertExAssistOcc(String pkPvs){
		int result=0;
		int res=0;

		result=DataBaseHelper.execute("insert into ex_assist_occ_b select * from ex_assist_occ where PK_PV in ("+pkPvs+")");
		res=DataBaseHelper.execute("insert into ex_assist_occ_dt_b select * from ex_assist_occ_dt where PK_ASSOCC in (select PK_ASSOCC from ex_assist_occ where PK_PV in ("+pkPvs+"))");
		log.info("****************成功转存"+result+"条门诊医技执行记录到ex_assist_occ_b表*****************");
		log.info("****************成功转存"+res+"条医技执行明细到ex_assist_occ_dt_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	private boolean batInsertExAssistOccD(String pkPvs){
		int result=0;
		int res=0;

		result=DataBaseHelper.execute("insert into ex_assist_occ select * from ex_assist_occ_b where PK_PV in ("+pkPvs+")");
		res=DataBaseHelper.execute("insert into ex_assist_occ_dt select * from ex_assist_occ_dt_b where PK_ASSOCC in (select PK_ASSOCC from ex_assist_occ_b where PK_PV in ("+pkPvs+"))");
		log.info("****************成功恢复"+result+"条门诊医技执行记录到ex_assist_occ表*****************");
		log.info("****************成功恢复"+res+"条医技执行明细到ex_assist_occ_dt表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	private boolean batDelExAssistOcc(String pkPvs){
		String sql2="delete from ex_assist_occ_dt  where PK_ASSOCC in (select PK_ASSOCC from ex_assist_occ where PK_PV in ("+pkPvs+"))";
		String sql="delete from ex_assist_occ where pk_pv in ("+pkPvs+")";
		int result2=DataBaseHelper.execute(sql2);
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条门诊医技执行记录(ex_assist_occ)--门诊*****************");
		log.info("****************成功删除"+result2+"条医技执行明细(ex_assist_occ_dt)--门诊*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	private boolean batDelExAssistOccD(String pkPvs){
		String sql2="delete from ex_assist_occ_dt_b  where PK_ASSOCC in (select PK_ASSOCC from ex_assist_occ_b where PK_PV in ("+pkPvs+"))";
		String sql="delete from ex_assist_occ_b where pk_pv in ("+pkPvs+")";
		int result2=DataBaseHelper.execute(sql2);
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条门诊医技执行记录(ex_assist_occ_b)--门诊*****************");
		log.info("****************成功删除"+result2+"条医技执行明细(ex_assist_occ_dt_b)--门诊*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	private boolean batInsertExPresOcc(String pkPvs){
		int result=0;
		int res=0;
		int ret=0;
		result=DataBaseHelper.execute("insert into ex_pres_occ_b select * from ex_pres_occ where PK_PV in ("+pkPvs+")");
		res=DataBaseHelper.execute("insert into ex_pres_occ_dt_b select * from ex_pres_occ_dt where pk_presocc in (select pk_presocc from ex_pres_occ where PK_PV in ("+pkPvs+"))");
		ret=DataBaseHelper.execute("insert into ex_pres_occ_pddt_b select * from ex_pres_occ_pddt where pk_presoccdt in (select pk_presoccdt from ex_pres_occ_dt where pk_presocc in (select pk_presocc from ex_pres_occ where PK_PV in ("+pkPvs+")))");
		log.info("****************成功转存"+result+"条处方执行单到ex_pres_occ_b表*****************");
		log.info("****************成功转存"+res+"条处方执行明细到ex_pres_occ_dt_b表*****************");
		log.info("****************成功转存"+ret+"条处方发退药明细到ex_pres_occ_pddt_b表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	private boolean batInsertExPresOccD(String pkPvs){
		int result=0;
		int res=0;
		int ret=0;
		result=DataBaseHelper.execute("insert into ex_pres_occ select * from ex_pres_occ_b where PK_PV in ("+pkPvs+")");
		res=DataBaseHelper.execute("insert into ex_pres_occ_dt select * from ex_pres_occ_dt_b where pk_presocc in (select pk_presocc from ex_pres_occ_b where PK_PV in ("+pkPvs+"))");
		ret=DataBaseHelper.execute("insert into ex_pres_occ_pddt select * from ex_pres_occ_pddt_b where pk_presoccdt in (select pk_presoccdt from ex_pres_occ_dt_b where pk_presocc in (select pk_presocc from ex_pres_occ_b where PK_PV in ("+pkPvs+")))");
		log.info("****************成功恢复"+result+"条处方执行单到ex_pres_occ表*****************");
		log.info("****************成功恢复"+res+"条处方执行明细到ex_pres_occ_dt表*****************");
		log.info("****************成功恢复"+ret+"条处方发退药明细到ex_pres_occ_pddt表*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	private boolean batDelExPresOcc(String pkPvs){
		String sql2="delete from ex_pres_occ_pddt  where pk_presoccdt in (select pk_presoccdt from ex_pres_occ_dt where pk_presocc in (select pk_presocc from ex_pres_occ where PK_PV in ("+pkPvs+")))";
		String sql3="delete from ex_pres_occ_dt  where pk_presocc in (select pk_presocc from ex_pres_occ where PK_PV in ("+pkPvs+"))";
		String sql="delete from ex_pres_occ where pk_pv in ("+pkPvs+")";
		int result3=DataBaseHelper.execute(sql2);
		int result2=DataBaseHelper.execute(sql3);
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条处方执行单(ex_pres_occ)--门诊*****************");
		log.info("****************成功删除"+result2+"条处方执行明细(ex_pres_occ_dt)--门诊*****************");
		log.info("****************成功删除"+result3+"条处方发退药明细(ex_pres_occ_pddt)--门诊*****************");
		if(result>0){
			return true;
		}
		return false;
	}
	private boolean batDelExPresOccD(String pkPvs){
		String sql2="delete from ex_pres_occ_pddt_b  where pk_presoccdt in (select pk_presoccdt from ex_pres_occ_dt_b where pk_presocc in (select pk_presocc from ex_pres_occ_b where PK_PV in ("+pkPvs+")))";
		String sql3="delete from ex_pres_occ_dt_b  where pk_presocc in (select pk_presocc from ex_pres_occ_b where PK_PV in ("+pkPvs+"))";
		String sql="delete from ex_pres_occ_b where pk_pv in ("+pkPvs+")";
		int result3=DataBaseHelper.execute(sql2);
		int result2=DataBaseHelper.execute(sql3);
		int result=DataBaseHelper.execute(sql);
		log.info("****************成功删除"+result+"条处方执行单(ex_pres_occ_b)--门诊*****************");
		log.info("****************成功删除"+result2+"条处方执行明细(ex_pres_occ_dt_b)--门诊*****************");
		log.info("****************成功删除"+result3+"条处方发退药明细(ex_pres_occ_pddt_b)--门诊*****************");
		if(result>0){
			return true;
		}
		return false;
	}
}
class BatchListUtil<E> {
	/**
     * 把list分成多个批次
     * @param list 集合
     * @param batchSize 批次大小
     * @return Map<Integer,List<E>>
     */
    public Map<Integer,List<E>> batchList(List<E> list, int batchSize){
        Map<Integer,List<E>> itemMap = new HashMap<>();
        itemMap.put(1, new ArrayList<E>());
        for(E e : list){
            List<E> batchList= itemMap.get(itemMap.size());
            if(batchList.size() == batchSize){//当list满足批次数量，新建一个list存放后面的数据
                batchList = new ArrayList<E>();
                itemMap.put(itemMap.size()+1, batchList);
            }
            batchList.add(e);
        }
        return itemMap;
    }

}