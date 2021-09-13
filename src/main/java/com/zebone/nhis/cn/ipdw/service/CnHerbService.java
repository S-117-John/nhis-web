package com.zebone.nhis.cn.ipdw.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.aop.OrderVoid;
import com.zebone.nhis.cn.ipdw.dao.CnOrderMapper;
import com.zebone.nhis.cn.ipdw.vo.OrderVo;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.CnNoticeService;
import com.zebone.platform.Application;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnOrdHerbMapper;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.CnSignCaParam;
import com.zebone.nhis.cn.ipdw.vo.HerbOrder;
import com.zebone.nhis.cn.ipdw.vo.SaveHerMoudParam;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.cn.cp.CpRecExp;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSetDt;
import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.module.cn.ipdw.CpRecExpParam;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnHerbService {
	static org.apache.logging.log4j.Logger log = LogManager.getLogger(CnHerbService.class);
	 
	//private static final String FREQ_TMP = "once";
	//private static final String HERB_ORDER = "2cbd46a530cd44a083b999abae68738e";		//草药医嘱pk，后期参数化
	private static final String HERB_NAME = "草药医嘱";
	private static final String HERB_CODE = "DEF99999";
	private static SimpleDateFormat dateformat =  new SimpleDateFormat("yyyyMMddHHmmss");

	private static final String CODE_HERB = "0407";

	@Autowired      
	private CnOrdHerbMapper cnOrdHerbMapper;  
	
	@Autowired      
	private BdSnService bdSnService;

	@Autowired      
	private CnPubService cnPubService;  

	@Autowired
	private CnNoticeService cnNoticeService;


	@Autowired
	private CnOrderMapper cnOrderMapper;
	/**
	 * 获取患者的所有草药医嘱
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getHerbOrders(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		List<Map<String,Object>> ret = cnOrdHerbMapper.getHerbOrders(paramMap.get("pkPv"), HERB_CODE);
		return ret;
	}
	
	/**
	 * 根据草药药品主键查询药品单位
	 * 交易码004004009035
	 */
	public List<BdUnit> getUnitVol(String param, IUser user){
		Map<String, String> paramVol = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		List<BdUnit> vol = cnOrdHerbMapper.getUnitVol(paramVol.get("pkPd"));
		return vol;
		
	}
	
	
	/**
	 * 获取草药医嘱对应的明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>>  getHerbItems(String param, IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> ret = null;
		if (Application.isSqlServer()) {
			ret = cnOrdHerbMapper.getHerbItemsSqlServer(paramMap);
		}else{
			ret = cnOrdHerbMapper.getHerbItems(paramMap);
		}
		return ret;
	}
	
	/**
	 * 保存草药处方
	 * @param param
	 * @param user
	 */
	public HerbOrder saveHerbOrder(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		//log.info("---->param==="+param);
		HerbOrder rtn = new HerbOrder();
		HerbOrder herbOrder = JsonUtil.readValue(param, new TypeReference<HerbOrder>(){});
		User u = (User)user;
		CnOrder order = herbOrder.getOrder();
		//vaildOrdTs(order);
		CnPrescription presVo = null;
		Date dt = new Date();
		Date dd = cnPubService.getOutOrdDate(herbOrder.getOrder().getPkPv());
		if (dd != null && dd.compareTo(dt) < 0) {
			dt = dd;
		}
		if( order.getPkCnord() == null || "".equals(order.getPkCnord())){
			presVo = new CnPrescription();
			presVo.setPkPres(NHISUUID.getKeyId());
			presVo.setPkOrg(u.getPkOrg());
			presVo.setPkPv(order.getPkPv());
			presVo.setPkPi(order.getPkPi());
			presVo.setDtPrestype("02");//02 草药处方
			presVo.setPresNo(order.getPresNo());
			presVo.setDatePres(dt);
			presVo.setPkDept(u.getPkDept());
			presVo.setPkDeptNs(order.getPkDeptNs());
			presVo.setPkEmpOrd(u.getPkEmp());  
			presVo.setNameEmpOrd(u.getNameEmp());
			presVo.setFlagPrt(Constants.FALSE);	
			presVo.setEuBoil(order.getEuBoil());
			presVo.setFriedNum(order.getFriedNum());
			presVo.setUsageCount(order.getUsageCount());
			presVo.setDtProperties(order.getDtProperties());
			presVo.setDosagePack(order.getDosagePack());	
			presVo.setDtBoiltype(order.getDtBoiltype());
			presVo.setTs(dt);
			order.setTs(dt);
			DataBaseHelper.insertBean(presVo);
			
			order.setPkCnord(NHISUUID.getKeyId());
			order.setCodeApply(null);
			order.setPkPres(presVo.getPkPres());
			//pkPv, pkOrg, nameOrd ：前台设置
			order.setCodeOrdtype("0103");					//草药类型，bd_ordtype
			order.setEuAlways("1");						//临时
			//order.setPkOrd(HERB_ORDER);						
			order.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, user));					
			order.setDescOrd(order.getNameOrd());	
			if (StringUtils.isBlank(order.getCodeFreq())) {
				order.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));
			}
			order.setQuan(1.0);
			order.setQuanCg(1.0);
			order.setEuStatusOrd("0");		//签署
			order.setDateEnter(dt);			//暂用服务器时间，db时间后期处理
			order.setDateStart(dt);
			order.setPkDept(u.getPkDept());
			order.setPkDeptNs(order.getPkDeptNs());		
			order.setPkEmpInput(u.getPkEmp());
			order.setNameEmpInput(u.getUserName());
//			order.setDateSign(dt);
//			order.setPkEmpOrd(u.getPkEmp());  //签署时写入
//			order.setNameEmpOrd(u.getUserName());
			order.setEuIntern(order.getEuIntern());
			order.setEuPvtype("3");			//住院患者
			order.setOrdsnParent(order.getOrdsn());
			order.setNameOrd(order.getNameOrd()==null?HERB_NAME:order.getNameOrd());
			order.setDescOrd(order.getNameOrd()==null?HERB_NAME:order.getNameOrd());
			order.setCodeOrd(HERB_CODE);
			order.setFlagDoctor("1");
			order.setFlagDurg("1");
			DataBaseHelper.insertBean(order);
		}else {
			order.setTs(dt);
			DataBaseHelper.updateBeanByPk(order,false);
			presVo = DataBaseHelper.queryForBean("select * from cn_prescription where pk_pres=?", CnPrescription.class, new Object[]{order.getPkPres()});
			presVo.setEuBoil(order.getEuBoil());
			presVo.setDtBoiltype(order.getDtBoiltype());
			presVo.setFriedNum(order.getFriedNum());
			presVo.setUsageCount(order.getUsageCount());
			presVo.setDtProperties(order.getDtProperties());
			presVo.setDosagePack(order.getDosagePack());	
			DataBaseHelper.updateBeanByPk(presVo, false);
			
		}	
		for(CnOrdHerb herb : herbOrder.getHerbs()){
			log.info("--------herb.pkpd=="+herb.getPkPd()+",  cnord=="+order.getPkCnord());
			if(herb.getPkPd()!=null && !"".equals(herb.getPkPd()) ){
				log.info("------------insert-------");
				herb.setPkCnord(order.getPkCnord());
				herb.setDelFlag(Constants.FALSE);
				herb.setTs(dt);
				DataBaseHelper.insertBean(herb);
			}else{
				if(herb.getPkOrdherb()!=null && !"".equals(herb.getPkOrdherb())) {
					DataBaseHelper.deleteBeanByPk(herb);
				}
			}
		}
		
		List<CnOrdHerb> rtnCnOrdHerb= DataBaseHelper.queryForList("select * from cn_ord_herb where del_flag='0' and pk_cnord = ?", CnOrdHerb.class, new Object[] { order.getPkCnord() });
		BeanUtils.copyProperties(rtn, herbOrder);
		rtn.setOrder(order);
		rtn.setHerbs(rtnCnOrdHerb);
		
		if(herbOrder.getCnSignCaList()!=null && herbOrder.getCnSignCaList().size()>0 ){
			for(CnSignCa cnSignCa :herbOrder.getCnSignCaList()){
				cnSignCa.setPkBu(order.getPkCnord() );
			}
			rtn.setCnSignCaList(herbOrder.getCnSignCaList());
		}
		
		if(herbOrder.getCpRecExp()!=null && herbOrder.getCpRecExp().size()>0 ){
			for(CpRecExp cpRecExpParam :herbOrder.getCpRecExp()){
				cpRecExpParam.setPkCnord(order.getPkCnord() );
			}
			rtn.setCpRecExp(herbOrder.getCpRecExp());
		}
		
		return rtn;
	}
	
	
	
	
	
	private void vaildOrdTs(CnOrder order) {
		List<CnOrder> ords = new ArrayList<CnOrder>();
		if(!( order.getPkCnord() == null || "".equals(order.getPkCnord()))){
			CnOrder ord = new CnOrder();
			ord.setPkCnord(order.getPkCnord());
			ord.setTs(order.getTs());
			ords.add(ord);
		}
	    if(ords.size()>0)cnPubService.vaildUpdateCnOrdts(ords);	
	}

	/**
	 * 签署草药处方
	 * @param param
	 * @param user
	 */
	public void signHerbOrder(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		//CnSignCaParam cnSignCaParam = JsonUtil.readValue(param, CnSignCaParam.class);
		//签署前先保存
		HerbOrder herbOrder = saveHerbOrder(param,user);
		
		List<String> pkCnordTsList = new ArrayList<String>();
		pkCnordTsList.add(herbOrder.getOrder().getPkCnord()+","+dateformat.format(herbOrder.getOrder().getTs()));
		if(pkCnordTsList==null||pkCnordTsList.size()<=0) return;
		List<String> pkCnordList = cnPubService.splitPkTsValidOrd(pkCnordTsList);
		
		List<CnOrder> ordList = new ArrayList<CnOrder>();
		User u = (User)user;
		for(String pkCnord : pkCnordList){
			CnOrder ord = new CnOrder();
			ord.setPkEmpOrd(u.getPkEmp());
			ord.setNameEmpOrd(u.getNameEmp());
			ord.setPkCnord(pkCnord);
			ord.setEuStatusOrd("1");
			ord.setFlagSign(Constants.TRUE);
			ord.setDateSign(new Date());
			ord.setTs(ord.getDateSign());
			String str = pkCnordTsList.toString();
			ord.setPkPv(str.substring(str.length()-33,str.length()-1));
			ordList.add(ord);
		}
		DataBaseHelper.batchUpdate("update cn_order set pk_emp_ord=:pkEmpOrd,name_emp_ord=:nameEmpOrd ,eu_status_ord = :euStatusOrd , flag_sign = :flagSign , date_sign = :dateSign ,ts=:ts  where pk_cnord = :pkCnord and eu_status_ord ='0'", ordList);
		cnPubService.updateDateStart(ordList);
		
		if(herbOrder.getCnSignCaList()!=null && herbOrder.getCnSignCaList().size()>0){
			cnPubService.caRecord(herbOrder.getCnSignCaList());
		}
		//cnPubService.recExpOrder(herbOrder.getCpRecExp());
		cnPubService.sendMessage(pkCnordList,"新医嘱");
		
	}
	
	public String getPdStore(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		List<String> rs = cnOrdHerbMapper.getPdStore(paramMap.get("pkDept"));
		String ret = null;
		if(rs.size()>0)
			ret = rs.get(0);
		return ret;
	}

	/**
	 * 删除未签署的草药
	 * @param param
	 * @param user
	 */
	public void removeHerbOrder(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
	    String pkCnordTs=paramMap.get("pkCnOrd");
	    String pkCnord = cnPubService.splitPkTsValidOrd(pkCnordTs);
	    
	    //bug34863 【草药开立】删处方提示删除成功后，申请单还存在
	    //医嘱状态: 0 开立；1 签署；2 核对；3 执行；4 停止；9 作废
	    //说明: 产品的住院医生站草药医嘱不需要签署(医嘱状态为0)，人民医院需要签署(医嘱状态为1)
	    String euStatusOrd = "0";
	    Map<String,Object>  stateMap = DataBaseHelper.queryForMap("select eu_status_ord from cn_order where pk_cnord = ? and del_flag = '0'", pkCnord);
	    String statusOrd = stateMap.get("euStatusOrd") == null?null:stateMap.get("euStatusOrd").toString();
	    if(StringUtils.isNotBlank(statusOrd) && statusOrd.equals("1")) {
	    	euStatusOrd = "1";
	    }
	    
	    //cnOrdHerbMapper.removeHerbOrder(pkCnord);
	    DataBaseHelper.execute("delete from cn_prescription where pk_pres = (select pk_pres from cn_order where PK_CNORD = ? and eu_status_ord = '"+euStatusOrd+"') ", new Object[]{pkCnord});
	    DataBaseHelper.execute("delete from cn_ord_herb where PK_CNORD = (select PK_CNORD from cn_order where PK_CNORD = ? and eu_status_ord = '"+euStatusOrd+"')", new Object[]{pkCnord}); 
	    //DataBaseHelper.execute("delete from cn_order where PK_CNORD = ? and eu_status_ord = '0'", new Object[]{pkCnord}); 
	    DataBaseHelper.execute("delete from cn_order where eu_status_ord = '"+euStatusOrd+"' and ORDSN_PARENT IN (SELECT ORDSN_PARENT FROM CN_ORDER WHERE PK_CNORD = ? )", new Object[]{pkCnord}); //删除护瞩
		
	}

	/**
	 * 撤销草药处方
	 * @param param
	 * @param user
	 */
	@OrderVoid(param = "param")
	public void cancelHerbOrder(String param, IUser user){
		//List<String> pkHerbList = JsonUtil.readValue(param, new TypeReference<List<String>>(){});
		CnSignCaParam cnSignCaParam = JsonUtil.readValue(param, CnSignCaParam.class);
		List<String> pkOpList = cnSignCaParam.getPkCnList();
		List<CnSignCa> cnSignCaList = cnSignCaParam.getCnSignCaList();
		this.cancleOrder(pkOpList, user);
		cnPubService.caRecord(cnSignCaList);
		cnPubService.sendMessage(pkOpList,"作废医嘱");
		//发送平台消息
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		PlatFormSendUtils.sendCancelHerbOrderMsg(paramMap);
		
	}
	
	private void cancleOrder(List<String> pkHerbList,  IUser user) {
		if(pkHerbList.size()<=0) return ;
		User u = (User)user;
		//作废医嘱是否需要护士核对(只处理作废标志、作废人、作废时间)
		String ifNeedNurseChk = ApplicationUtils.getSysparam("CN0028", false);
		if(ifNeedNurseChk==null) ifNeedNurseChk="0";
		if(ifNeedNurseChk.equals("1")){
			List<CnOrder> updateList=new ArrayList<CnOrder>();
			for(String pk_cnord : pkHerbList){
				CnOrder order = new CnOrder();
	
				order.setPkCnord(pk_cnord);
				order.setEuStatusOrd("9");
				order.setFlagErase(Constants.TRUE);
				order.setDateErase(new Date());
				order.setPkEmpErase(u.getPkEmp());
				order.setNameEmpErase(u.getNameEmp());
				
				updateList.add(order);
			}
			DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord",updateList);
			//更新标记
			DataBaseHelper.batchUpdate("update cn_order set eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  "
                                      + "where pk_cnord =:pkCnord and eu_status_ord in ('0','1') ", updateList);
		    //作废医嘱
			DataBaseHelper.batchUpdate("update cn_order set flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  "
					                  + "where pk_cnord =:pkCnord and eu_status_ord not in ('0','1') ", updateList);
			return;
		}
		
		Map<String,Object> pkOrdMap = new HashMap<String,Object>();
		pkOrdMap.put("pk_cnord", pkHerbList);
	    //作废医嘱，删除未执行的医嘱执行单
	     //2、作废药品医嘱时，如果已生成医嘱执行单并且未请领，删除执行单；
	    String delPdExList = "delete  from ex_order_occ  "+
				 "where exists (select 1 "+
				          "from cn_order ord "+
				         "where ex_order_occ.pk_cnord = ord.pk_cnord "+
				           "and ex_order_occ.pk_pdapdt is null "+
				           "and ord.pk_cnord in (:pk_cnord) "+
				           "and ord.flag_durg = '1')";
	    DataBaseHelper.update(delPdExList, pkOrdMap);
	    //3、判断是否有未完成的有效请领单
	    String pdApplyList = "select ap.name_emp_ap, ap.date_ap, apdt.pk_pdapdt "+
					  "from ex_pd_apply ap "+
					 "inner join ex_pd_apply_detail apdt on ap.pk_pdap = apdt.pk_pdap "+
					 "where ap.flag_cancel = '0' and apdt.flag_stop = '0' and apdt.flag_finish = '0' and APDT.FLAG_CANC='0' and apdt.pk_cnord in (:pk_cnord)";
		List<Map<String, Object>> pdAplistNoFin = DataBaseHelper.queryForList(pdApplyList, pkOrdMap);
		if(pdAplistNoFin!=null&&pdAplistNoFin.size()>0){
			String str="";
			for(Map<String, Object> pdap : pdAplistNoFin){
				String date_ap = pdap.get("dateAp")==null ? "" : DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss" , (java.sql.Timestamp)pdap.get("dateAp"));
				if(StringUtils.isEmpty(str)){
					str = date_ap;
				}else if(str.indexOf(date_ap)<0){
					str += "和"+ date_ap;
				}
			}
			throw new BusException("该医嘱有未完成请领单，不能作废！请告知护士检查 申请日期为"+str+"的请领单!");
		}
		//4.判断发药数量是否为0
		String pdDeListSql = "select (case when sum(pdde.eu_direct * abs(pdde.quan_pack)) is null then 0 else sum(pdde.eu_direct * abs(pdde.quan_pack)) end) as sum_quan_de "+
                             "from ex_pd_de pdde where pdde.pk_cnord in (:pk_cnord)";
		List<Map<String, Object>> pdDelistQuan = DataBaseHelper.queryForList(pdDeListSql, pkOrdMap);
		BigDecimal sum_quan_de =  (BigDecimal) pdDelistQuan.get(0).get("sumQuanDe");
		if(sum_quan_de.compareTo(BigDecimal.ZERO)>0){
			throw new BusException("该医嘱未退药，不能作废！ 请告知护士  完成该医嘱的退药流程后  再作废!");
		}
		List<CnOrder> ordList = new ArrayList<CnOrder>();
		for(String pkCnord : pkHerbList){
			CnOrder ord = new CnOrder();
			ord.setPkCnord(pkCnord);
			ord.setEuStatusOrd("9");
			ord.setFlagErase(Constants.TRUE);
			ord.setDateErase(new Date());
			ord.setPkEmpErase(u.getPkEmp());
			ord.setNameEmpErase(u.getNameEmp());
			ord.setTs(ord.getDateErase());
			ordList.add(ord);
		}
	    //作废医嘱
		DataBaseHelper.batchUpdate("update cn_order set flag_erase_chk='1', eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase, "+
						" pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  where pk_cnord =:pkCnord ", ordList);
		String sql = "SELECT * FROM CN_ORDER WHERE PK_CNORD = ? AND DEL_FLAG = '0'";
		List<Map<String, Object>> nohsu =DataBaseHelper.queryForList(sql, ordList.get(0).getPkCnord());
		DataBaseHelper.batchUpdate("update cn_order set flag_erase_chk='1', eu_status_ord = :euStatusOrd , flag_erase= :flagErase , date_erase = :dateErase , pk_emp_erase= :pkEmpErase , name_emp_erase= :nameEmpErase  where ordsn_parent in ( select ordsn_parent from cn_order where pk_cnord= :pkCnord) and flag_doctor='0' ", nohsu);
		DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord",ordList);
//		User userEmp = (User)user;
//		cnOrdHerbMapper.cancelHerbOrder(pkHerbList, userEmp.getPkEmp(), userEmp.getNameEmp());
	}
	
	/**
	 * 取消签署
	 * 交易码004004009025
	 */
	public void updateSign(String param,IUser user){
		
		//List<String> pkOpList = JsonUtil.readValue(param, new TypeReference<List<String>>(){});
		CnSignCaParam cnSignCaParam = JsonUtil.readValue(param, CnSignCaParam.class);
		List<String> pkOpLists = cnSignCaParam.getPkCnList();
		if(pkOpLists==null||pkOpLists.size()<=0) return;
		List<String> pkOpList = cnPubService.splitPkTsValidOrd(pkOpLists);
		List<CnSignCa> cnSignCaList = cnSignCaParam.getCnSignCaList();
		List<CnOrder> ordList = new ArrayList<CnOrder>();
		List<CpRecExpParam> cpRecExp = cnSignCaParam.getCpRecExp();
		String pkCnords = null;
		User u = (User)user;
		for(String pkCnord : pkOpList){
			CnOrder ord = new CnOrder();
			ord.setPkEmpOrd(u.getPkEmp());
			ord.setNameEmpOrd(u.getNameEmp());
			ord.setPkCnord(pkCnord);
			pkCnords = pkCnord;
			ord.setEuStatusOrd("0");
			ord.setFlagSign(Constants.FALSE);
			ord.setDateSign(new Date());
			ord.setTs(ord.getDateSign());
			ordList.add(ord);
		}
		DataBaseHelper.batchUpdate("update cn_order set pk_emp_ord=:pkEmpOrd,name_emp_ord=:nameEmpOrd ,eu_status_ord = :euStatusOrd , flag_sign = :flagSign , date_sign = :dateSign ,ts=:ts  where pk_cnord = :pkCnord and eu_status_ord ='1'", ordList);
		String sql = "SELECT * FROM CN_ORDER WHERE PK_CNORD = ? AND DEL_FLAG = '0'";
		List<Map<String, Object>> nohs =DataBaseHelper.queryForList(sql, pkCnords);
		DataBaseHelper.batchUpdate("update cn_order set eu_status_ord = '0' , flag_sign = '0' ,ts=:ts   where ordsn_parent=:ordsnParent and  eu_status_ord ='1' and flag_doctor='0'  and ordsn!=ordsn_parent ", nohs);
		//cnOrdHerbMapper.signHerbOrder(pkOpList);
		cnPubService.caRecord(cnSignCaList);
		cnPubService.recExpOrder(cpRecExp);
		cnPubService.sendMessage(pkOpList,"新医嘱");
		//发送平台消息
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ordlist", nohs);
		PlatFormSendUtils.sendUpdateSignMsg(paramMap);
			
	}
	
	/**
	 * 草药模板明细保存方法
	 * 004004009031
	 * @param param
	 * @param user
	 * @throws Exception
	 */
	public void saveHerbMoudleDt(String param, IUser user)throws Exception{
		SaveHerMoudParam sHMParam = JsonUtil.readValue(param, SaveHerMoudParam.class);
		String pkOrdset = sHMParam.getPkOrdset();
		if(StringUtils.isEmpty(pkOrdset)) return;
		List<BdOrdSetDt> list = sHMParam.getHerMouDtList();
		DataBaseHelper.execute("DELETE FROM BD_ORD_SET_DT WHERE PK_ORDSET = ?", new Object[]{pkOrdset});
		for (BdOrdSetDt bdOrdSetDt : list) {
			bdOrdSetDt.setPkOrdsetdt(NHISUUID.getKeyId());
			bdOrdSetDt.setPkOrdset(pkOrdset);
			bdOrdSetDt.setFlagPd("1");
			bdOrdSetDt.setDelFlag("0");
			DataBaseHelper.insertBean(bdOrdSetDt);
		}
	}
	
	/**
	 * 查询草药模板列表
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> queryHerbTempList(String param, IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		return cnOrdHerbMapper.queryHerbTempList(paramMap);
	}
	
	
	/**
	 * 保存草药处方(重写)
	 * @param param
	 * @param user
	 */
	public HerbOrder saveHerbOrder2(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		HerbOrder rtn = new HerbOrder();
		HerbOrder herbOrder = JsonUtil.readValue(param, new TypeReference<HerbOrder>(){});

		User u = (User)user;
		OrderVo orderVo = herbOrder.getOrderVo();
		String pkWgOrg=null;
		String pkWg=null;
		if(StringUtils.isNotEmpty(orderVo.getPkPv())){
			//判断是否符合开立条件
			if(checkPvInfo(orderVo.getPkPv())){
				throw  new BusException("前患者就诊的科室为日间病房,住院超过允许开立医嘱的天数！不允许开立新医嘱！");
			}

			PvEncounter pvInfo = DataBaseHelper.queryForBean("Select * From PV_ENCOUNTER Where pk_pv=? ",PvEncounter.class, new Object[]{orderVo.getPkPv()});
			pkWgOrg=pvInfo.getPkWgOrg();
			pkWg=pvInfo.getPkWg();
		}

		CnOrder order = new CnOrder();
		BeanUtils.copyProperties(order, orderVo);
		order.setPkWgOrg(pkWgOrg);
		order.setPkWg(pkWg);
		herbOrder.setOrder(order);


		CnPrescription presVo = null;
		Date dt = new Date();
		//数据一致性验证
		if(herbOrder.getOrder().getTs()!=null){
			List<String> pkCnordTsList = new ArrayList<String>();
			pkCnordTsList.add(herbOrder.getOrder().getPkCnord()+","+dateformat.format(herbOrder.getOrder().getTs()));
			if(pkCnordTsList==null||pkCnordTsList.size()<=0) return null;
			cnPubService.splitPkTsValidOrd(pkCnordTsList);
		}
		
		
		Date dd = cnPubService.getOutOrdDate(herbOrder.getOrder().getPkPv());
		if (dd != null && dd.compareTo(dt) < 0) {
			dt = dd;
		}
		
		//操作主数据：cn_order、CN_PRESCRIPTION
		if( StringUtils.isBlank(order.getPkCnord())){
			presVo = new CnPrescription();
			presVo.setPkPres(NHISUUID.getKeyId());
			presVo.setPkOrg(u.getPkOrg());
			presVo.setPkPv(order.getPkPv());
			presVo.setPkPi(order.getPkPi());
			presVo.setDtPrestype("02");//02 草药处方
			presVo.setPresNo(order.getPresNo());
			presVo.setDatePres(dt);
			presVo.setPkDept(u.getPkDept());
			presVo.setPkDeptNs(order.getPkDeptNs());
			presVo.setPkEmpOrd(u.getPkEmp());  
			presVo.setNameEmpOrd(u.getNameEmp());
			presVo.setFlagPrt(Constants.FALSE);	
			presVo.setEuBoil(order.getEuBoil());
			presVo.setFriedNum(order.getFriedNum());
			presVo.setUsageCount(order.getUsageCount());
			presVo.setDtProperties(order.getDtProperties());
			presVo.setDosagePack(order.getDosagePack());	
			presVo.setDtBoiltype(order.getDtBoiltype());
			presVo.setTs(dt);
			presVo.setNameDiag(order.getNameDiag());
			presVo.setDateSend(order.getDateSend());
			presVo.setNameSymp(herbOrder.getNameSymp());
			presVo.setNamePres(orderVo.getNamePres());
			DataBaseHelper.insertBean(presVo);
			
			order.setPkCnord(NHISUUID.getKeyId());
			order.setCodeApply(null);
			order.setPkPres(presVo.getPkPres());
			order.setCodeOrdtype("0103");	//草药类型，bd_ordtype
			order.setEuAlways("1");	        //临时
			order.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, user));					
			order.setDescOrd(order.getNameOrd());
			order.setTs(dt);
			if (StringUtils.isBlank(order.getCodeFreq())) {
				order.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));
			}
			order.setQuan(1.0);
			order.setQuanCg(1.0);
			order.setEuStatusOrd("0");		//签署
			order.setDateEnter(dt);			//暂用服务器时间，db时间后期处理
			order.setDateStart(dt);
			//order.setPkDept(order.getPkDept());
			//order.setPkDeptNs(order.getPkDeptNs());
			order.setPkEmpInput(u.getPkEmp());
			order.setNameEmpInput(u.getUserName());
//			order.setDateSign(dt);
//			order.setPkEmpOrd(u.getPkEmp());  //签署时写入
//			order.setNameEmpOrd(u.getUserName());
			order.setEuIntern(order.getEuIntern());
			order.setEuPvtype("3");			//住院患者
			order.setOrdsnParent(order.getOrdsn());
			order.setNameOrd(order.getNameOrd()==null?HERB_NAME:order.getNameOrd());
			order.setDescOrd(order.getNameOrd()==null?HERB_NAME:order.getNameOrd());
			order.setCodeOrd(HERB_CODE);
			order.setFlagDoctor("1");
			order.setFlagDurg("1");
			order.setFlagSign("1");
			order.setPkEmpChk(null);
			order.setNameEmpChk(null);
			order.setDateChk(null);
			order.setDateLastEx(null);
			order.setDateErase(null);
			order.setPkEmpErase(null);
			order.setNameEmpErase(null);
			order.setFlagErase("0");
			order.setFlagEraseChk("0");
			order.setDosage(1.00);
			
			DataBaseHelper.insertBean(order);
		}else {
			order.setTs(dt);
			DataBaseHelper.updateBeanByPk(order,false);
			
			presVo = DataBaseHelper.queryForBean("select * from cn_prescription where pk_pres=?", CnPrescription.class, new Object[]{order.getPkPres()});
			presVo.setEuBoil(order.getEuBoil());
			presVo.setDtBoiltype(order.getDtBoiltype());
			presVo.setFriedNum(order.getFriedNum());
			presVo.setUsageCount(order.getUsageCount());
			presVo.setDtProperties(order.getDtProperties());
			presVo.setDosagePack(order.getDosagePack());	
			presVo.setTs(dt);
			presVo.setNameDiag(order.getNameDiag());
			presVo.setDateSend(order.getDateSend());
			presVo.setNameSymp(herbOrder.getNameSymp());
			presVo.setNamePres(orderVo.getNamePres());
			DataBaseHelper.updateBeanByPk(presVo, false);
		}	
		
		//处理草药明细：cn_ord_herb
		//增加、修改
		List<CnOrdHerb> addHerbItemList = new ArrayList<CnOrdHerb>();
		List<CnOrdHerb> updateHerbItemList  = new ArrayList<CnOrdHerb>();
		if(herbOrder.getHerbs() != null)
		{
			for(CnOrdHerb herbVo: herbOrder.getHerbs() ){
				if(StringUtils.isBlank(herbVo.getPkOrdherb())){
					String pkOrdherb = NHISUUID.getKeyId();
					herbVo.setPkOrdherb(pkOrdherb);
					herbVo.setPkCnord(order.getPkCnord());
					herbVo.setDelFlag("0");
					herbVo.setTs(dt);
					herbVo.setCreateTime(dt);
					herbVo.setCreator(u.getPkEmp());
					addHerbItemList.add(herbVo);
				}else{
					herbVo.setTs(dt);
					updateHerbItemList.add(herbVo);
				}
			}
		}
		
		if (addHerbItemList != null && addHerbItemList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrdHerb.class), addHerbItemList);
		}
		if (updateHerbItemList != null && updateHerbItemList.size() > 0) {
			//DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOrdHerb.class), updateHerbItemList);
			for(CnOrdHerb herbVo: updateHerbItemList ){
				DataBaseHelper.updateBeanByPk(herbVo, false);
			}
		}
		
		//删除明细
		List<CnOrdHerb> delHerbItemList= herbOrder.getDelHerbs();
		if (delHerbItemList != null && delHerbItemList.size() > 0) {
			DataBaseHelper.batchUpdate("delete from cn_ord_herb where pk_ordherb = :pkOrdherb ", delHerbItemList);
		}
		
		
		List<CnOrdHerb> rtnCnOrdHerb= DataBaseHelper.queryForList("select * from cn_ord_herb where del_flag='0' and pk_cnord = ?", CnOrdHerb.class, new Object[] { order.getPkCnord() });
		BeanUtils.copyProperties(rtn, herbOrder);
		rtn.setOrder(order);
		rtn.setHerbs(rtnCnOrdHerb);
		
		if(herbOrder.getCnSignCaList()!=null && herbOrder.getCnSignCaList().size()>0 ){
			for(CnSignCa cnSignCa :herbOrder.getCnSignCaList()){
				cnSignCa.setPkBu(order.getPkCnord() );
			}
			rtn.setCnSignCaList(herbOrder.getCnSignCaList());
		}
		
		if(herbOrder.getCpRecExp()!=null && herbOrder.getCpRecExp().size()>0 ){
			for(CpRecExp cpRecExpParam :herbOrder.getCpRecExp()){
				cpRecExpParam.setPkCnord(order.getPkCnord() );
			}
			rtn.setCpRecExp(herbOrder.getCpRecExp());
		}
		
		return rtn;
	}
	
	/**
	 * 签署草药处方(重写)
	 * @param param
	 * @param user
	 */
	public void signHerbOrder2(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		User userInfo = (User)user;
		//签署前先保存
		HerbOrder herbOrder = saveHerbOrder2(param,user);
		
		List<String> pkCnordList = new ArrayList<String>();
		pkCnordList.add(herbOrder.getOrder().getPkCnord());
		if(pkCnordList==null||pkCnordList.size()<=0) return;
		
		List<CnOrder> ordList = new ArrayList<CnOrder>();
		User u = (User)user;
		for(String pkCnord : pkCnordList){
			CnOrder ord = new CnOrder();
			ord.setPkEmpOrd(u.getPkEmp());
			ord.setNameEmpOrd(u.getNameEmp());
			ord.setPkCnord(pkCnord);
			ord.setEuStatusOrd("1");
			ord.setFlagSign(Constants.TRUE);
			ord.setDateSign(new Date());
			ord.setTs(ord.getDateSign());
//			String str = pkCnordTsList.toString();
//			ord.setPkPv(str.substring(str.length()-33,str.length()-1));
			ordList.add(ord);
		}
		DataBaseHelper.batchUpdate("update cn_order set pk_emp_ord=:pkEmpOrd,name_emp_ord=:nameEmpOrd ,eu_status_ord = :euStatusOrd , flag_sign = :flagSign , date_sign = :dateSign ,ts=:ts  where pk_cnord = :pkCnord and eu_status_ord ='0'", ordList);
		cnPubService.updateDateStart(ordList);
		
		if(herbOrder.getCnSignCaList()!=null && herbOrder.getCnSignCaList().size()>0){
			cnPubService.caRecord(herbOrder.getCnSignCaList());
		}
		cnPubService.DealOutCpOrdExp(herbOrder.getCpRecExp(),userInfo);
		cnPubService.sendMessage(pkCnordList,"新医嘱");
		//保存临床消息提醒
		List<CnOrder> noticeList = new ArrayList<>();
		noticeList.add(herbOrder.getOrder());
		cnNoticeService.saveCnNotice(noticeList);
		//发送平台消息
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("ordList", ordList);
		PlatFormSendUtils.sendSignHerbOrder2Msg(paramMap);
	}

	/**
	 * 复制草药明细
	 * CnHerbService.copyHerbsDetail
	 * 004004009053
	 * @param param
	 * @param user
	 */
	public void copyHerbsDetail(String param, IUser user){
		User u = (User)user;
		//获取医嘱集合
		List<CnOrder> orderList = JsonUtil.readValue(param, new TypeReference<List<CnOrder>>() {});
		//筛选出草药医嘱，CodeOrdtype==0103
		for (int i = orderList.size()-1; i >=0 ; i--) {
			if(orderList.get(i)!=null){
				if(!("0103".equals(orderList.get(i).getCodeOrdtype()))){
					orderList.remove(i);
				}
			}

		}
		//设置处方属性
		setPrescription(orderList,user);
	}



	/**
	 * 复制草药明细-设置处方属性
	 * @param orderList
	 */
	private void setPrescription(List<CnOrder> orderList,IUser user) {
		User u = (User)user;
		for (int i = 0; i < orderList.size(); i++) {
			if(orderList.get(i)!=null){
				Date dt = new Date();
				CnOrder order = orderList.get(i);
				Map<String,String> map = new HashMap<>();
				map.put("pkCnord",order.getPkCnord());
				List<CnOrder> newOrderList = cnOrderMapper.getOrdInfo(map);
				CnOrder newOrder = new CnOrder();
				if(newOrderList!=null&&newOrderList.size()>0){
					newOrder = newOrderList.get(0);
				}


				//设置处方编号
				order.setPresNo(ApplicationUtils.getCode(CODE_HERB));
				//设置处方属性
				CnPrescription presVo = new CnPrescription();
				presVo.setPkPres(NHISUUID.getKeyId());
				presVo.setPkOrg(u.getPkOrg());
				presVo.setPkPv(orderList.get(i).getPkPv());
				presVo.setPkPi(orderList.get(i).getPkPi());
				presVo.setDtPrestype("02");//02 草药处方
				presVo.setPresNo(order.getPresNo());
				presVo.setDatePres(dt);
				presVo.setPkDept(u.getPkDept());
				presVo.setPkDeptNs(orderList.get(i).getPkDeptNs());
				presVo.setPkEmpOrd(u.getPkEmp());
				presVo.setNameEmpOrd(u.getNameEmp());
				presVo.setFlagPrt(Constants.FALSE);
				presVo.setEuBoil(newOrder.getEuBoil());
				presVo.setFriedNum(newOrder.getFriedNum());
				presVo.setUsageCount(newOrder.getUsageCount());
				presVo.setDtProperties(newOrder.getDtProperties());
				presVo.setDosagePack(newOrder.getDosagePack());
				presVo.setDtBoiltype(newOrder.getDtBoiltype());
				presVo.setTs(dt);
				DataBaseHelper.insertBean(presVo);

				//查询草药明细
				List<CnOrdHerb> ordHerbList = cnOrdHerbMapper.findByPkCnOrd(order.getPkCnord());
				if(orderList.size()>0){
					for (CnOrdHerb cnOrdHerb: ordHerbList) {
						cnOrdHerb.setPkOrdherb(NHISUUID.getKeyId());
						cnOrdHerb.setDelFlag("0");
						cnOrdHerb.setTs(dt);
						cnOrdHerb.setCreateTime(dt);
						cnOrdHerb.setCreator(u.getPkEmp());
					}
				}

				//设置医嘱属性
				order.setPkCnord(NHISUUID.getKeyId());
				//煎法
				order.setDtBoiltype(newOrder.getDtBoiltype());
				//处方性质
				order.setDtProperties(newOrder.getDtProperties());
				//煎药方式
				order.setEuBoil(newOrder.getEuBoil());
				//用法
				order.setCodeApply(newOrder.getCodeApply());
				order.setPkPres(presVo.getPkPres());
				order.setCodeOrdtype("0103");	//草药类型，bd_ordtype
				order.setEuAlways("1");	        //临时
				order.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, user));
				order.setDescOrd(order.getNameOrd());
				order.setTs(dt);
				if (StringUtils.isBlank(order.getCodeFreq())) {
					order.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));
				}
				order.setQuan(1.0);
				order.setQuanCg(1.0);
				order.setEuStatusOrd("0");		//签署
				order.setDateEnter(dt);			//暂用服务器时间，db时间后期处理
				order.setDateStart(dt);
				order.setPkDept(u.getPkDept());
				order.setPkDeptNs(order.getPkDeptNs());
				order.setPkEmpInput(u.getPkEmp());
				order.setNameEmpInput(u.getUserName());
				order.setEuIntern(order.getEuIntern());
				order.setEuPvtype("3");			//住院患者
				order.setOrdsnParent(order.getOrdsn());
				order.setNameOrd(order.getNameOrd()==null?HERB_NAME:order.getNameOrd());
				order.setDescOrd(order.getNameOrd()==null?HERB_NAME:order.getNameOrd());
				order.setCodeOrd(HERB_CODE);
				order.setFlagDoctor("1");
				order.setFlagDurg("1");
				order.setFlagSign("1");
				order.setPkEmpChk(null);
				order.setNameEmpChk(null);
				order.setDateChk(null);
				order.setDateLastEx(null);
				order.setDateErase(null);
				order.setPkEmpErase(null);
				order.setNameEmpErase(null);
				order.setFlagErase("0");
				order.setFlagEraseChk("0");
				order.setDosage(1.00);
				order.setEuStatusOrd("0");
				order.setFlagSign("0");
				order.setPkEmpOrd(null);
				order.setNameEmpOrd(null);
				DataBaseHelper.insertBean(order);

				if(ordHerbList.size()>0){
					for (CnOrdHerb cnOrdHerb: ordHerbList) {
						cnOrdHerb.setPkCnord(order.getPkCnord());
					}
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrdHerb.class), ordHerbList);
				}
			}

		}
	}

	/*************************************医嘱属性 前端不需要处理 start**************************************/
	//处理日间病房医嘱开立期限的校验
	private boolean checkPvInfo(String pkPv){
		boolean ok=false;//默认通过校验
		String day=ApplicationUtils.getSysparam("CN0107", false);
		String sql="Select PV.* From pv_encounter pv \n" +
				"Inner Join bd_dictattr dict On pv.pk_dept=dict.pk_dict \n" +
				"Where  pv.pk_pv=? And dict.code_attr='0605' And dict.val_attr='1'";
		PvEncounter pvEncounter=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{pkPv});
		int dayIp= DateUtils.getDateSpace(pvEncounter.getDateBegin(), new Date());
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(day) && pvEncounter!=null){
			int dayI= Integer.parseInt(day);
			//判断
			if(dayI>0 && dayI<dayIp) ok=true;
		}
		return ok;
	}
	/*************************************医嘱属性 前端不需要处理 end**************************************/

}

