package com.zebone.nhis.bl.pub.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.zebone.nhis.bl.pub.support.SettlePubUtils;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOccDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ex.pub.support.OrderFreqCalCountHandler;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.nhis.ex.pub.vo.OrderExecVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.nhis.scm.pub.support.OpDrugPubUtils;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 处理结算中与库存、处方请领单、医技执行单 相关的业务
 * @author Administrator
 */
@Service
public class SettlePdOutService {

	@Autowired
	private PdStOutPubService pdStOutPubService;
	@Autowired
	private ExAssistPubService exAssistPubService;
	
	/**
	 * 收费合并处方，以物品为单位设置库存预留量.
	 * 具体思路：先将处方中所有收费明细按执行科室分类，同一执行科室可以进行一次药品库存量处理。同时，同一执行科室下同一药品要进行预留量合并。
	 * @param mapPres
	 */
	public void mergePreSetPdPrepNum(Map<String, List<BlOpDt>> mapPres) {
		
		//不发药不扣预留量
		Map<String, String> dispPresMap = new HashMap<String, String>();
		//自备药（和不发药还不一样，自备是处方里面有可能有，有可能没有。相同点都是不预留库存）
		final List<String> selfPkOrders = Lists.newArrayList();
		Set<String> pkPress = new HashSet<String>();
		for (Map.Entry<String, List<BlOpDt>> entry : mapPres.entrySet()) {
			pkPress.add(entry.getKey());
		}
		if (pkPress.size() > 0) {
			String pkPresSql = CommonUtils.convertSetToSqlInPart(pkPress, "pk_pres");
			StringBuffer sql = new StringBuffer();
			sql.append("select ord.pk_pres,max(ord.ords) ords,max(ord.flag_disp) flag_disp from cn_order ord where ord.PK_PRES in (");
			sql.append(pkPresSql);
			sql.append(") group by ord.PK_PRES");
			List<Map<String, Object>> mapResult = DataBaseHelper.queryForList(sql.toString(), new Object[] {});
			for (Map<String, Object> map : mapResult) {
				String pkPres = CommonUtils.getPropValueStr(map, "pkPres");
				//不发药标志 1=不发药
				if(EnumerateParameter.ONE.equals(CommonUtils.getPropValueStr(map, "flagDisp"))) {
					dispPresMap.put(pkPres, pkPres);
				}
			}
			selfPkOrders.addAll(DataBaseHelper.getJdbcTemplate().queryForList("select PK_CNORD from cn_order where PK_PRES in(" + pkPresSql + ") and flag_self='1'",String.class));
		}
		
		//合并处方，以物品为单位设置库存预留量
		Map<String, List<PdOutParamVo>> pkStoreMap = new HashMap<String, List<PdOutParamVo>>();
		for (Entry<String, List<BlOpDt>> mapPre : mapPres.entrySet()) {
			String pkPres = mapPre.getKey();
			//不发药不扣预留量
			if(dispPresMap.containsKey(pkPres)) {
				continue;
			}
			List<BlOpDt> blOpDtlists = mapPre.getValue();
			for (BlOpDt blOpDt : blOpDtlists) {
				double quanMin = MathUtils.mul(blOpDt.getQuan(), CommonUtils.getDouble(blOpDt.getPackSize()));
				PdOutParamVo pdOutParamVo = new PdOutParamVo();
				pdOutParamVo.setPkPd(blOpDt.getPkPd()); //物品pk
				pdOutParamVo.setPkCnOrd(blOpDt.getPkCnord());
				if(pkStoreMap.get(blOpDt.getPkDeptEx()) == null){
					// 产生预留量=记费数量*包装量
					pdOutParamVo.setQuanMin(quanMin);
					List<PdOutParamVo> pdOutList = new ArrayList<>();
					pdOutList.add(pdOutParamVo);
					pkStoreMap.put(blOpDt.getPkDeptEx(), pdOutList);
				}else{
					//合并同一科室下的同一个物品数量
					List<PdOutParamVo> pdVolist = pkStoreMap.get(blOpDt.getPkDeptEx());
					for(int i = 0; i < pdVolist.size(); i++){
						//当不同门诊收费明细中，存在同一科室同一药品的情况下， 合并药品数量（以旧换新）；若非同一药品，正常放入科室的list中。
						if(pdVolist.get(i).getPkPd().equals(blOpDt.getPkPd())){
							quanMin += pdVolist.get(i).getQuanMin();
							pdOutParamVo.setQuanMin(quanMin);
							pdVolist.remove(i);
							break; //由于每次List<PdOutParamVo>中不可能存在相同的pkpd 可以跳出
						}else{
							pdOutParamVo.setQuanMin(quanMin);
						}
					}
					pkStoreMap.get(blOpDt.getPkDeptEx()).add(pdOutParamVo);
				}
			}
		}
		
		Set<Entry<String, List<PdOutParamVo>>> pkStoreEntrys = pkStoreMap.entrySet();
		for (Entry<String, List<PdOutParamVo>> pkStoreEntry : pkStoreEntrys) {
			String pkStore = pkStoreEntry.getKey();
			//校验执行科室的医疗类型是否为药房，只有药房走预留量设置
			String qrySql = "select dt_depttype as code from bd_ou_dept where PK_DEPT = ?";
			Map<String,Object> typeMap = DataBaseHelper.queryForMap(qrySql, pkStore);
			if(typeMap!=null && typeMap.size()>0 
					&& !CommonUtils.isEmptyString(typeMap.get("code").toString())
					&& "0402".equals(typeMap.get("code").toString())){
				//根据pk_store取对应的List<PdOutParamVo>作为一个批次进行库存量处理
				List<PdOutParamVo> outList = pkStoreMap.get(pkStore);
				//处理自备药不预占库存
				List<PdOutParamVo> slefOrders = Lists.newArrayList();
				if(selfPkOrders.size()>0 && CollectionUtils.isNotEmpty(outList)){
					slefOrders = outList.stream().filter(dt -> selfPkOrders.contains(dt.getPkCnOrd())).collect(Collectors.toList());
					outList.removeAll(slefOrders);
				}
				if(outList.size()>0) {
					pdStOutPubService.setPdPrepNum(outList, null, pkStore, "1");
				}
				outList.addAll(slefOrders);
			}
		}
	}
	
	/**
	 * 生成门诊药品请领单和明细
	 * @param mapPres
	 * @param partRtnPres 含部分退的处方主键集合
	 * @param partRtnDt 部分退的记费明细集合
	 * @return
	 */
	public void saveOpDrugApply(Map<String, List<BlOpDt>> mapPres,Set<String> partRtnPres,List<BlOpDt> partRtnDt) throws BusException {

		User user = UserContext.getUser();
		Set<String> pkPress = new HashSet<String>();

		Set<String> pkDeptExSet=new HashSet<String>();
		
		for (Map.Entry<String, List<BlOpDt>> entry : mapPres.entrySet()) {
			pkPress.add(entry.getKey());
		}
		
		if (pkPress.size() == 0)
			return;
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select pres.*,cnord.flag_disp,cnord.ords from cn_prescription pres ");
		sql.append(" left join (select ord.pk_pres,max(ord.ords) ords,max(ord.flag_disp) flag_disp from cn_order ord where ord.PK_PRES in (");
		sql.append(CommonUtils.convertSetToSqlInPart(pkPress, "pk_pres"));
		sql.append(") group by ord.PK_PRES) cnord on cnord.pk_pres = pres.pk_pres  where pres.pk_pres in (");
		sql.append(CommonUtils.convertSetToSqlInPart(pkPress, "pk_pres"));
		sql.append(") ");	
		List<Map<String, Object>> mapResult = DataBaseHelper.queryForList(sql.toString(), new Object[] {});


		// 获取系统的发药队列签到方式
		String paramQueueSign = ApplicationUtils.getSysparam("EX0019", false);// 发药队列签到方式
		if (StringUtils.isEmpty(paramQueueSign)) {
			throw new BusException("未在本机构维护系统参数【EX0019】");
		}
		/*String paramOpDeDrug = ApplicationUtils.getSysparam("EX0001", false);// 门诊发药模式
		if (StringUtils.isEmpty(paramOpDeDrug)) {
			throw new BusException("未在本机构维护系统参数【EX0001】");
		}*/
		
		List<ExPresOcc> epoDaos = new ArrayList<ExPresOcc>();
		List<ExPresOccDt> exPresOccDtDaos = new ArrayList<ExPresOccDt>();
		
		//获取处方的开立科室和执行科室
		Set<String> pkList = new HashSet<>();//记录记费主键
		for (Map<String, Object> mapPresTemp : mapResult) {
			String pkPres = mapPresTemp.get("pkPres").toString();
			List<BlOpDt> opList = mapPres.get(pkPres);
			//获取需要结算明细主键
			if(opList!=null && opList.size()>0){
				for(BlOpDt dt : opList){
					pkList.add(dt.getPkCgop());
					pkDeptExSet.add(dt.getPkDeptEx());
				}
			}
		}

		Map<String,Object> prepConfParamMap=getPrepConfParameter(pkDeptExSet);

		// 一个处方生成一个请领单
		for (Map<String, Object> mapPresTemp : mapResult) {
			String pkPres = mapPresTemp.get("pkPres").toString();
			boolean flagHas = false;
			if(partRtnPres!=null&&partRtnPres.size()>0){
				for(String partPkPres:partRtnPres){
					if(partPkPres.equals(pkPres)){
						flagHas = true;
						break;
					}
				}
			}
			String pkPresocc = null;
			if(flagHas)//部分退重收标志的记费项目，不再生成处方执行单及执行明细
				continue;
			ExPresOcc epo = new ExPresOcc();
			epo.setPkOrg(user.getPkOrg());
			epo.setPkPv(mapPresTemp.get("pkPv").toString());
			epo.setPkPi(mapPresTemp.get("pkPi").toString());
			epo.setDtPrestype(mapPresTemp.get("dtPrestype").toString());
			epo.setPkPres(pkPres);
			epo.setPresNo(mapPresTemp.get("presNo").toString());
			epo.setPkDiag(mapPresTemp.get("pkDiag") == null ? null : mapPresTemp.get("pkDiag").toString());
			epo.setPkOrgPres(mapPresTemp.get("pkOrg").toString());
			epo.setPkDeptPres(mapPresTemp.get("pkDept").toString());
			epo.setDatePres((Date) mapPresTemp.get("datePres"));
			epo.setPkEmpPres(mapPresTemp.get("pkEmpOrd") == null ? null : mapPresTemp.get("pkEmpOrd").toString());
			epo.setNameEmpPres(mapPresTemp.get("nameEmpOrd") == null ? null : mapPresTemp.get("nameEmpOrd").toString());
			// 每个处方的执行机构和执行科室只有一个
			BlOpDt blOpDtFirst = mapPres.get(pkPres).get(0);
			epo.setPkDeptEx(blOpDtFirst.getPkDeptEx());
			epo.setPkOrgEx(blOpDtFirst.getPkOrgEx());
			epo.setPkSettle(blOpDtFirst.getPkSettle());
			epo.setFlagSusp(EnumerateParameter.ZERO);// 挂起标志
			epo.setFlagPrep(EnumerateParameter.ZERO);// 配药标志
			if (EnumerateParameter.ZERO.equals(paramQueueSign)) {
				epo.setDateReg(new Date());
				Map<String, Object> mapWinno = null;
				String paramOpDeDrug="1";//默认配药发药
				if(prepConfParamMap!=null){
					paramOpDeDrug=CommonUtils.getString(prepConfParamMap.get(epo.getPkDeptEx()),"1");
				}
				if (EnumerateParameter.ONE.equals(paramOpDeDrug)) {
					epo.setEuStatus(EnumerateParameter.ZERO);//收费签到当为配药模式为生成状态
				} else if (EnumerateParameter.TWO.equals(paramOpDeDrug)) {
					epo.setEuStatus(EnumerateParameter.ONE);//收费签到当为发药模式为打印状态
					epo.setFlagPrep("1");//发药模式--默认已经配药
				}
				epo.setFlagReg("1");//收费签到--中山二院更改
			} else {
				epo.setEuStatus(EnumerateParameter.ZERO);
				epo.setFlagReg("0");//药房签到--中山二院更改
			}
			//中山人医任务-5306：当医嘱表中不发药标志设置为1（cn_order. flag_disp=’1’）时，产生的处方执行记录（ex_pres_occ. eu_status置为10）
			if(EnumerateParameter.ONE.equals(CommonUtils.getPropValueStr(mapPresTemp, "flagDisp"))) {
				//不发药-处方部分退的逻辑，目前为先退整个处方，然后医生在开立新的数量，对于新开的药品，不要走发药的流程
				epo.setEuStatus(EnumerateParameter.TEN);
			}
			epo.setFlagConf(EnumerateParameter.ZERO);
			epo.setFlagBo(EnumerateParameter.ZERO);
			epo.setFlagBode(EnumerateParameter.ZERO);
			epo.setFlagCg(EnumerateParameter.ONE);
			epo.setFlagChk(EnumerateParameter.ZERO);
			epo.setFlagCanc(EnumerateParameter.ZERO);
			if(mapPresTemp.get("euBoil")!=null && (Integer.parseInt(mapPresTemp.get("euBoil").toString())==2 ||Integer.parseInt(mapPresTemp.get("euBoil").toString())==3)){
				//当煎药方式大于1时，写处方执行单为外部发放处理：0 自煎，1 医院代煎，2 商家代煎，3 商家配送
				epo.setEuDetype("9");
			}else{
				epo.setEuDetype("0");
			}
			ApplicationUtils.setDefaultValue(epo, true);
			pkPresocc = epo.getPkPresocc();
			epoDaos.add(epo);
			for (BlOpDt bod : mapPres.get(pkPres)) {
				ExPresOccDt exPresOccDt = new ExPresOccDt();
				exPresOccDt.setPkOrg(user.getPkOrg());
				exPresOccDt.setPkPresocc(pkPresocc);
				exPresOccDt.setPkCnord(bod.getPkCnord());
				exPresOccDt.setPkPd(bod.getPkPd());
				exPresOccDt.setPkUnit(bod.getPkUnit());
				exPresOccDt.setPackSize(bod.getPackSize().intValue());
				exPresOccDt.setPrice(BigDecimal.valueOf(bod.getPrice()));
				exPresOccDt.setQuanCg(bod.getQuan());
				exPresOccDt.setOrdsCg(Double.valueOf(mapPresTemp.get("ords").toString()));
				exPresOccDt.setAmountCg(BigDecimal.valueOf(bod.getAmount()));
				exPresOccDt.setQuanDe(0D);
				exPresOccDt.setOrdsDe(0D);
				exPresOccDt.setAmountDe(new BigDecimal(0));
				exPresOccDt.setQuanBack(0D);
				exPresOccDt.setOrdsBack(0D);
				exPresOccDt.setAmountBack(new BigDecimal(0));
				exPresOccDt.setOrdsRet(0D);
				exPresOccDt.setQuanRet(0D);
				exPresOccDt.setAmountRet(new BigDecimal(0));
				ApplicationUtils.setDefaultValue(exPresOccDt, true);
				exPresOccDtDaos.add(exPresOccDt);
			}
		}
		// 一次性插入请领
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPresOcc.class), epoDaos);
		// 一次性插入请领明细
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPresOccDt.class), exPresOccDtDaos);

		//调用分窗规则
		SettlePubUtils.getDrugWinno(epoDaos,mapPres);

		Map<String,Object> paramMap=new HashMap<String,Object>();
		List<String> pkPresocces=new ArrayList<String>();
		String IsOpenHrSysparam=ApplicationUtils.getDeptSysparam("EX0055", user.getPkDept());
		for (ExPresOcc exPres : epoDaos) {
			pkPresocces.add(exPres.getPkPresocc());
		}
		if("1".equals(IsOpenHrSysparam) && "0".equals(paramQueueSign)){//是否开启门诊包药机接口，并且为收费签到
			paramMap.put("IsPackMachine", "1");
			ExtSystemProcessUtils.processExtMethod("PackMachineOp", "upLoadPresInfo",pkPresocces);
		}
		paramMap.put("pkPresocces", pkPresocces);
		paramMap.put("IsPtOpen", "1");//开启平台推送门诊结算处方请领数据
		PlatFormSendUtils.sendCnPresOpMsg(paramMap);
	}
	
	/***
	 * 根据执行科室获取对应执行科室的维护的EX0001配药发药系统参数值
	 * @param pkDepts
	 * @return
	 */
	private Map<String,Object> getPrepConfParameter(Set<String> pkDepts){
		Map<String,Object> resMap=new HashMap<>();
		for (String pkdept : pkDepts) {
			String paramOpDeDrug = ApplicationUtils.getDeptSysparam("EX0001", pkdept);// 门诊发药模式
			if (StringUtils.isEmpty(paramOpDeDrug)){
				resMap.put(pkdept, "1");//为空默认走直接走配药发药模式
			}else{
				resMap.put(pkdept, paramOpDeDrug);
			}
		}
		return resMap;
	}
	
	/**
	 * 根据结算明细的类型（处方或者项目）生成对应的表记录，处方请领单和医技执行单。同时处理库存预留量。
	 * @param blOpDts
	 */
	public void makeSdPresAssitRecords(List<BlOpDt> blOpDts) {
		// 主键是处方主键
		Map<String, List<BlOpDt>> mapPres = new HashMap<String, List<BlOpDt>>();
		// 主键是医嘱主键
		Map<String, List<BlOpDt>> mapAssit = new HashMap<String, List<BlOpDt>>();
		// 含部分退的处方集合--中二退费模式添加,仅针对药品记费与退费数量不一致情况
		Set<String> partRtnPres = new HashSet<String>();
		// 含部分退的记费明细集合--中二退费模式添加,仅针对药品记费与退费数量不一致情况
		List<BlOpDt> partRtnDt = new ArrayList<BlOpDt>();
		// 处方费用
		List<BlOpDt> blOpDtPress = null;
		// 医技费用
		List<BlOpDt> blOpDtMedicals = null;
		for (BlOpDt blOpDt : blOpDts) {
			if (BlcgUtil.converToTrueOrFalse(blOpDt.getFlagPd()) && 
					(CommonUtils.isEmptyString(blOpDt.getEuAdditem()) || "0".equals(blOpDt.getEuAdditem()))) {//校验是否是附加费用(2019-08-13加入逻辑)
				if (mapPres.get(blOpDt.getPkPres()) == null) {
					blOpDtPress = new ArrayList<BlOpDt>();
					blOpDtPress.add(blOpDt);
					mapPres.put(blOpDt.getPkPres(), blOpDtPress);
				} else {
					mapPres.get(blOpDt.getPkPres()).add(blOpDt);
				}
				if("1".equals(blOpDt.getFlagRecharge())){//部分退，退费重收标志
					partRtnPres.add(blOpDt.getPkPres());
					partRtnDt.add(blOpDt);
				}
			} else {
				//如果是挂号费，不做任何处理
				if("1".equals(blOpDt.getFlagPv()) ||
						(!CommonUtils.isEmptyString(blOpDt.getEuAdditem()) && "1".equals(blOpDt.getEuAdditem()))) //校验是否是附加费用(2019-08-13加入逻辑)
					continue;
				if (mapAssit.get(blOpDt.getPkCnord()) == null) {
					blOpDtMedicals = new ArrayList<BlOpDt>();
					blOpDtMedicals.add(blOpDt);
					mapAssit.put(blOpDt.getPkCnord(), blOpDtMedicals);
				} else {
					mapAssit.get(blOpDt.getPkCnord()).add(blOpDt);
				}
			}
		}

		/**
		 * 调用个性化外部接口（内容不限）
		 */
		Object obj=ExtSystemProcessUtils.processExtMethod("DrugWinnoRule", "getDrugDeptEx",mapPres);
		if(obj!=null){
			mapPres=(Map<String, List<BlOpDt>>) obj;
		}

		//合并处方，以物品为单位设置库存预留量，分科室处理药品预留量（同一科室同一药品合并）。
		mergePreSetPdPrepNum(mapPres);
		// 调用生成处方请领单服务
		saveOpDrugApply(mapPres,partRtnPres,partRtnDt);
		// 调用生成医技执行单
		exAssistPubService.saveExAssistOcc(mapAssit);
		
		//TODO 需要优化为非循环操作数据库模式--yangxue
//		String sqll = "select count(1) as ct from ex_assist_occ where pk_cnord=? and flag_refund = '0'";
//		for (Map.Entry<String, List<BlOpDt>> entry : mapAssit.entrySet()) {  
//		    List<BlOpDt> blOpDtsm= entry.getValue();
//			for(BlOpDt blOpDt : blOpDtsm){
//				int mapCount = DataBaseHelper.queryForScalar(sqll,Integer.class, blOpDt.getPkCnord());
//				if( mapCount ==0 ){
//					// 调用生成医技执行单
//					saveExAssistOcc(mapAssit);
//				}
//			} 
//		}
	}


	
}
