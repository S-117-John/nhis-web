package com.zebone.nhis.scm.opds.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.arch.support.ArchUtil;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccPddt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.scm.opds.dao.ScmOpDispense2Mapper;
import com.zebone.nhis.scm.pub.dao.SchOpdsPreMapper;
import com.zebone.nhis.scm.pub.service.PdStOutBatchPubService;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.nhis.scm.pub.vo.ExPresOccDtVO;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.nhis.scm.support.ScmExtProcessUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ScmOpDispense2Service {
	@Resource
	private ScmOpDispense2Mapper scmOpDispense2Mapper;
	
	@Resource
	private SchOpdsPreMapper schOpdsPreMapper;
	
	@Resource
	private PdStOutPubService pdStOutPubService;
	
	@Resource
	private PdStOutBatchPubService pdStOutBatchPubService;
	
	/**008003002010
	 * 查询本机对应的发药窗口
	 * @param param{"namePc":"计算机名称（地址）"}
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryLocalPdUpForm(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		User toUser=(User)user;
		paramMap.put("pkOrg", toUser.getPkOrg());
		paramMap.put("pkDept", toUser.getPkDept());
		return scmOpDispense2Mapper.qryLocalPdUpForm(paramMap);
	}
	
	/**008003002011
	 * 查询可选择的发药窗口
	 * @param param {"pkDept":"当前药房","pkDeptunit":"当前窗口主键"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> selectPdUpForm(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		return scmOpDispense2Mapper.selectPdUpForm(paramMap);
	}
	
	/**008003002012
	 * 查询患者信息
	 * @param param{"pkDept":"发药药房","winno":"药房窗口","type":"0未完成，1已完成，2暂挂"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPiInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		List<Map<String,Object>> piInfoList=new ArrayList<Map<String,Object>>(10);
		Integer type=Integer.parseInt(paramMap.get("type").toString());
		
		if(!"1".equals(paramMap.get("change"))){//前台根据是否选择有效期
			String evDay=ApplicationUtils.getSysparam("CN0004", false);
			if(!CommonUtils.isEmptyString(evDay)){
				Date dateUp=DateUtils.getDateMorning(new Date(), 0);
				int numDay=1-CommonUtils.getInt(evDay);
				String datePres=DateUtils.addDate(dateUp, numDay, 3, "yyyy-MM-dd HH:mm:ss");
				paramMap.put("datePres", datePres);
			}
		}
		switch (type) {
		case 0://未完成
			piInfoList=scmOpDispense2Mapper.qryUnFinishedPiInfo(paramMap);
			break;
		case 1://已完成
			String date=DateUtils.getDate("yyyyMMdd");
			String dateStart=date+"000000";
			String DateEnd=date+"235959";
			paramMap.put("dateStart", dateStart);
			paramMap.put("dateEnd",  DateEnd);
			piInfoList=scmOpDispense2Mapper.qryFinishedPiInfo(paramMap);
			break;
		case 2://暂挂
			piInfoList=scmOpDispense2Mapper.qryPendingPiInfo(paramMap);	
			break;
		default:
			break;
		}
        // 结果返回list
        List<Map<String, Object>> result = new ArrayList<>();
		// 中间过滤需要的临时容器
        List<Map<String, Object>> distinctMaps =new ArrayList<>();
		if (piInfoList.size()>0){

            // 拿到包含互联网信息的元数据
            List<Map<String, Object>> containIHRPMaps = piInfoList.stream().filter(p -> p.get("presNo").toString().contains("IHRP")).collect(Collectors.toList());

            // 移除presNo字段
            if (CollectionUtils.isNotEmpty(containIHRPMaps)){
                for (Map<String, Object> map : containIHRPMaps) {
                    map.remove("presNo");
                }

                distinctMaps=  containIHRPMaps.stream().distinct().collect(Collectors.toList());

            }

            // 元数据移除presNo
            for (Map<String, Object> map : piInfoList) {
                map.remove("presNo");
            }
            // 去重后的结果
            result = piInfoList.stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(distinctMaps))return result;
            // 假设有互联网的处方，打上标记，供前端使用
            for (Map<String, Object> map : result) {
                if (distinctMaps.contains(map)){
                    map.put("presNo","IHRP");}
                else {
                    map.put("presNo","1");
                }
            }
        }

        return result;
	}
	
	/**008003002013
	 * 查询处方信息
     *
	 * @param param {"pkPv":"就诊主键","pkDept":"发药药房","winno":"药房窗口","type":"0未完成，1已完成，2暂挂"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPresInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		List<Map<String,Object>> presInfoList=new ArrayList<Map<String,Object>>();
		Integer type=Integer.parseInt(paramMap.get("type").toString());
		if (Application.isSqlServer()) {
			paramMap.put("dbType", "sqlserver");
		}else{
			paramMap.put("dbType", "oralce");
		}
		switch (type) {
		case 0://未完成
			presInfoList=scmOpDispense2Mapper.qryUnFinishedPresInfo(paramMap);
			break;
		case 1://已完成
			presInfoList=scmOpDispense2Mapper.qryFinishedPresInfo(paramMap);
			break;
		case 2://暂挂
			presInfoList=scmOpDispense2Mapper.qryPendingPresInfo(paramMap);	
			break;
		}
		return presInfoList;
	}
	
	/**008003002014
	 * 查询处方明细
	 * @param param{"pkPresocc":"处方执行主键"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPresDetailInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return null;
		return scmOpDispense2Mapper.qryPresDetialInfo(paramMap);
	}
	
	/**008003002015
	 * 发药信息暂挂处理
	 * @param param{"pkPresocc":"处方","winno":"发药窗口"}
	 * @param user
	 */
	public void updatePendingPd(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return ;
		scmOpDispense2Mapper.updatePendingPd(paramMap);
	}
	
	/**008003002016
	 * 开始/停止发药
	 * @param param{"pkDept":"当前药房","code":"当前窗口","type":"0(开始)/1（停止）"}
	 * @param user
	 */
	public void updatePdFormStatus(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null) return;
		if(paramMap.get("type")!=null && "0".equals(paramMap.get("type"))){//开始
			scmOpDispense2Mapper.startPdForm(paramMap);
		}else if(paramMap.get("type")!=null && "1".equals(paramMap.get("type"))){//停止
			scmOpDispense2Mapper.stopPdForm(paramMap);
		}
	}
	
	/**
	 * 008003002017
	 * 门诊发药
	 * @param param
	 * @param user
	 */
	public void updateSendPd(String param,IUser user){
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPresocc = paraMap.get("pkPresocc").toString();//处方执行主键
		String pkDept = paraMap.get("pkDept").toString();//当前科室
		String winno = MapUtils.getString(paraMap, "winno","");//当前窗口
		String pkStore = paraMap.get("pkStore").toString();//当前仓库
		String codeEmp=paraMap.get("codeEmp").toString();//发药人工号
		String empSql="select pk_emp,name_emp from bd_ou_employee where code_emp=? ";
		List<Map<String,Object>> codeEmpList=DataBaseHelper.queryForList(empSql,new Object[]{codeEmp});

		User us=UserContext.getUser();
		us.setPkEmp(MapUtils.getString(codeEmpList.get(0),"pkEmp"));
		us.setNameEmp((MapUtils.getString(codeEmpList.get(0),"nameEmp")));
				
		//1.发药-供应链出库参数转换,合并物品
		List<PdOutParamVo> pdOutParams = new ArrayList<PdOutParamVo>();
		List<ExPresOccDtVO> presDts = schOpdsPreMapper.QryPresDt(pkPresocc);
		
		//重复发药校验 2020-12-03 --深大上线为止至今天每天都有重复发药的数据，触发点暂未找到，先强制加限制
		Set<String> pkPresoccdts = new HashSet<String>();
		for (ExPresOccDtVO presDt : presDts) {
			pkPresoccdts.add(presDt.getPkPresoccdt());
		}
		int count = DataBaseHelper.queryForScalar("select count(*) from ex_pres_occ_pddt where pk_presoccdt in (" 
					+ ArchUtil.convertSetToSqlInPart(pkPresoccdts, "pk_presoccdt") + ") and del_flag = '0' ", Integer.class);
		if(count > 0) {
			throw new BusException("药品已发放，请勿重复发放！");
		}
		
		Map<String,ExPresOccDt> flagPresDts = new HashMap<String,ExPresOccDt>();
		List<PdOutParamVo> param4Out = new  ArrayList<PdOutParamVo>();
		Map<String,PdOutParamVo> res4Out = new HashMap<String,PdOutParamVo>();
		Map<String,List<PdOutParamVo>> Out4Upd = new HashMap<String,List<PdOutParamVo>>();
		for(ExPresOccDtVO vo : presDts) {
			//1.1参数转换
			PdOutParamVo pdOutParam = new PdOutParamVo();
			pdOutParam.setPackSize(vo.getPackSize());
			pdOutParam.setPkCnOrd(vo.getPkCnord());
			pdOutParam.setPkPd(vo.getPkPd());
			pdOutParam.setPkPdapdt(vo.getPkPresoccdt());
			pdOutParam.setPkPv(vo.getPkPv());
			pdOutParam.setPkUnitPack(vo.getPkUnit());
			if (vo.getOrdsCg().doubleValue() == 0.0) vo.setOrdsCg(1.0);
			Double tempMin = MathUtils.mul(vo.getQuanCg(), vo.getPackSize() * 1.0);
			//tempMin = MathUtils.mul(tempMin, vo.getOrds());
			pdOutParam.setQuanMin(tempMin);
			pdOutParam.setQuanPack(vo.getQuanCg());
			pdOutParams.add(pdOutParam);
			//将收费时锁定的预留数量还原
			//1.2.加入标志哈希表
			ExPresOccDt occDt = new ExPresOccDt();
			ApplicationUtils.copyProperties(occDt, vo);
			flagPresDts.put(vo.getPkPresoccdt(), occDt);

			//1.3 将同一个药品进行合并
			String pkPd = pdOutParam.getPkPd();
			List<PdOutParamVo> voListTemp = Out4Upd.get(pkPd) != null && Out4Upd.get(pkPd).size() > 0 ? Out4Upd.get(pkPd) : new ArrayList<PdOutParamVo>();
			voListTemp.add(pdOutParam.clone());
			Out4Upd.put(pkPd, voListTemp);
			if (res4Out.get(pkPd) != null) {
				PdOutParamVo voTemp = res4Out.get(pkPd);
				voTemp.setQuanMin(voTemp.getQuanMin() + pdOutParam.getQuanMin());
				voTemp.setQuanPack(voTemp.getQuanPack() + pdOutParam.getQuanPack());
				res4Out.put(pkPd, voTemp);
			} else {
				res4Out.put(pkPd, (PdOutParamVo) pdOutParam.clone());
			}
		}
			param4Out.addAll(res4Out.values());
			pdStOutPubService.setPdUnPrepNum(pdOutParams,pkStore,pkDept,"1");
			try{
				//门诊发药剩余业务处理方法，封装到一个方法中为了保证其业务在同一个事物内完成
				ExPresOcc presOcc = schOpdsPreMapper.QryPresByPk(pkPresocc);
				dispend(presOcc.getPkOrgPres(),
						presOcc.getPkDeptPres(),
						pkDept,
						pkStore,
						pkPresocc,
						flagPresDts,
						winno,
						pdStOutBatchPubService,
						Out4Upd,
						param4Out,
						presOcc.getPresNo(),
						presOcc.getNameEmpPrep());

			}catch(Exception e){
				//发药失败时，回滚已经解除的预留量
				e.printStackTrace();
				pdStOutPubService.setPdPrepNum(param4Out,pkStore,pkDept,"1");
				throw new BusException(e.getMessage());
			}
			Map<String,Object> paramMap=new HashMap<String,Object>(); 
			String isOpenHrSysParam=ApplicationUtils.getDeptSysparam("EX0055", pkDept);
			paramMap.put("IsFinishDrug", "1");
			paramMap.put("winno", winno);
			paramMap.put("pkPresocc",pkPresocc);
			if("1".equals(isOpenHrSysParam)){
				PlatFormSendUtils.sendCnPresOpMsg(paramMap);
			}
		List<String> pkPresoccs = new ArrayList<>();
		pkPresoccs.add(pkPresocc);
		paramMap.put("opType","1");
		paramMap.put("pkPresoccs",pkPresoccs);
		PlatFormSendUtils.execute(paramMap,"sendConfirmDosage");
		ExtSystemProcessUtils.processExtMethod("PackMachineOp", "confirmPres",paramMap);
	}
	
	/**
	  * 门诊发药 （内部方法，不允许直接被交易码调用）
	   * @param pkorgAp 请领机构
	 * @param pkDeptAp 请领科室
	 * @param pkDept 当前科室
	 * @param pkStore 当前仓库
	 * @param pkPresocc 处方执行单主键
	 * @param flagPresDts
	 * @param winno 发药窗口
	 * @param pdStOutPubService
	 * @param pdOutParams
	 */
	private void dispend(String pkOrgAp, String pkDeptAp, String pkDept, String pkStore, String pkPresocc, Map<String, ExPresOccDt> flagPresDts,
						 String winno, PdStOutBatchPubService pdStOutBatchPubService, Map<String, List<PdOutParamVo>> Out4Upd, List<PdOutParamVo> param4Out, String presNo, String nameEmpPrep) {
		// 新流程，自备药按照正常逻辑发药，但是不出库
		List<String> listPkcnord = param4Out.stream().map(PdOutParamVo::getPkCnOrd).filter(StringUtils::isNotBlank).collect(Collectors.toList());
		List<String> listCnord = DataBaseHelper.getJdbcTemplate().queryForList("SELECT PK_CNORD FROM CN_ORDER WHERE (PK_CNORD in(" + CommonUtils.convertListToSqlInPart(listPkcnord) + ")) and FLAG_SELF='1'", String.class);
		List<PdOutParamVo> filterMap = param4Out.stream().filter(vo -> listCnord.contains(vo.getPkCnOrd())).collect(Collectors.toList());
		param4Out.removeAll(filterMap);
		//1.调用供应链接口
		List<PdOutDtParamVo> pdOutRes = pdStOutBatchPubService.execStOut(param4Out, pkOrgAp, pkDeptAp, pkDept, pkStore, null, false);
		param4Out.addAll(filterMap);
		//2.拆分药品
		List<PdOutDtParamVo> pdoutResUpdate = new ArrayList<>();
	 		for(PdOutDtParamVo vo : pdOutRes){
	 			PdOutDtParamVo tempvo=(PdOutDtParamVo)vo.clone();
	 			if(Out4Upd.get(tempvo.getPkPd()).size()>1){
	 				for(int i = 0;i<Out4Upd.get(vo.getPkPd()).size();i++){
	 					PdOutParamVo paraTemp = Out4Upd.get(vo.getPkPd()).get(i);
	 					PdOutDtParamVo voTemp = (PdOutDtParamVo) vo.clone();
	 					voTemp.setQuanOutMin(paraTemp.getQuanMin());
	 					voTemp.setQuanOutPack(paraTemp.getQuanPack());
	 					voTemp.setPkPdapdt(paraTemp.getPkPdapdt());
						pdoutResUpdate.add(voTemp);
						tempvo.setQuanOutMin(tempvo.getQuanOutMin() - voTemp.getQuanOutMin());
						tempvo.setQuanOutPack(tempvo.getQuanOutPack() - voTemp.getQuanOutPack());
					}
				} else {
					pdoutResUpdate.add(tempvo);
				}
			}

		//3.更新处方执行单ex_pres_occ；
		Map<String, Object> updParam = new LinkedHashMap<>();
		updParam.put("flagConf", "1");
		updParam.put("dateConf", new Date());
		updParam.put("pkEmpConf", UserContext.getUser().getPkEmp());
		updParam.put("nameEmpConf", UserContext.getUser().getNameEmp());
		updParam.put("euStatus", "3");
		if (StringUtils.isBlank(nameEmpPrep)) {
			updParam.put("pkEmpPrep", UserContext.getUser().getPkEmp());
			updParam.put("nameEmpPrep", UserContext.getUser().getNameEmp());
		}
		updParam.put("pkPresocc", pkPresocc);
		schOpdsPreMapper.updExPresOcc(updParam);

		//4.更新处方明细ex_pres_occ_dt；
		List<ExPresOccDt> presDtForUpd = new ArrayList<ExPresOccDt>();
		Map<String, List<PdOutDtParamVo>> flagOutRes = new HashMap<String, List<PdOutDtParamVo>>(16);
		for (PdOutDtParamVo vo : pdoutResUpdate) {
			List<PdOutDtParamVo> temps = flagOutRes.get(vo.getPkPdapdt()) != null ? flagOutRes.get(vo.getPkPdapdt()) : new ArrayList<PdOutDtParamVo>(16);
			temps.add(vo);
			flagOutRes.put(vo.getPkPdapdt(), temps);
		}
	 		
	        for(Entry<String,List<PdOutDtParamVo>> e:flagOutRes.entrySet()) {
				ExPresOccDt presDt = flagPresDts.get(e.getKey());
				List<PdOutDtParamVo> resOut = e.getValue();
				double quanDe = 0.0;
				double amountDe = 0.0;
				double quanMin = 0.0;
				for (PdOutDtParamVo vo : resOut) {
					quanDe = MathUtils.add(quanDe, vo.getQuanOutPack());
					Double priceTemp = MathUtils.mul(MathUtils.div(vo.getPrice(), vo.getPackSizePd().doubleValue()), vo.getPackSize().doubleValue());
					Double temp = MathUtils.mul(presDt.getQuanCg().doubleValue() == 0 ? 1 : presDt.getQuanCg(), presDt.getPrice().doubleValue());

					quanMin = MathUtils.add(quanMin, vo.getQuanOutMin());
					amountDe = MathUtils.add(amountDe, temp);
				}
				presDt.setQuanDe(quanDe);
				presDt.setQuanMinDe(quanMin);
				presDt.setOrdsDe(presDt.getOrdsCg().doubleValue() == 0.0 ? 1.0 : presDt.getOrdsCg());
				BigDecimal amt = new BigDecimal(amountDe).setScale(2, BigDecimal.ROUND_HALF_UP);
				presDt.setAmountDe(amt);
				ApplicationUtils.setDefaultValue(presDt, false);
				presDtForUpd.add(presDt);
			}
	 		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExPresOccDt.class), presDtForUpd);

	 		//5.更新处方明细操作流水ex_pres_occ_pddt；
	 		List<ExPresOccPddt> presPddt = new ArrayList<ExPresOccPddt>();
	 		for(PdOutDtParamVo vo : pdoutResUpdate){
	 			ExPresOccPddt pddt = new ExPresOccPddt();
	 			pddt.setPkOrg(UserContext.getUser().getPkOrg());
	 			pddt.setPkPresoccdt(vo.getPkPdapdt());
	 			pddt.setDateDe(new Date());
	 			pddt.setEuDirect("1");
	 			pddt.setPkPd(vo.getPkPd());
	 			pddt.setBatchNo(vo.getBatchNo());
	 			pddt.setPkDeptDe(UserContext.getUser().getPkDept());
	 			pddt.setPkStore(pkStore);
	 			pddt.setPkUnit(vo.getPkUnitPack());
	 			pddt.setPackSize(vo.getPackSize());
	 			pddt.setQuanPack(vo.getQuanOutPack());
	 			pddt.setQuanMin(vo.getQuanOutMin());
	 			double price = MathUtils.mul(MathUtils.div(vo.getPrice(), vo.getPackSizePd().doubleValue()), vo.getPackSize()
	 					.doubleValue());
	 			double priceCost = MathUtils.mul(MathUtils.div(vo.getPriceCost(), vo.getPackSizePd().doubleValue()), vo.getPackSize()
	 					.doubleValue());
	 			pddt.setPriceCost(priceCost);
	 			pddt.setDateExpire(vo.getDateExpire());
	 			pddt.setPrice(price);
	 			ExPresOccDt dt = flagPresDts.get(vo.getPkPdapdt());
	 			pddt.setAmountCost(MathUtils.mul(pddt.getQuanPack(), priceCost));
	 			pddt.setAmount(MathUtils.mul(pddt.getQuanPack(), price));
	 			pddt.setPkPdstdt(vo.getPkPdstdt());
	 			pddt.setNote(null);
	 			ApplicationUtils.setDefaultValue(pddt, true);
	 			pddt.setPresNo(presNo);
	 			presPddt.add(pddt);
	 			
	 		}
	 		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPresOccPddt.class), presPddt);
	 		//6）更新窗口工作量（减少处方数），见3.9.6.6。
	 		//OpDrugPubUtils.updWinVol(1,pkDept,winno,-1);
		//throw new BusException("");
   }
	
	/**
	 * 008003002018
	 * 通过处方执行记录中ts数据来控制并发
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> checkPresTime(String param,IUser user){
		Set<String> pkPreses=JsonUtil.readValue(param, new TypeReference<HashSet<String>>() {});
		if(pkPreses==null || pkPreses.size()<=0)return null;
		String sql="select pk_presocc,to_char(ts,'yyyy-MM-dd HH24:mm:ss') ts from ex_pres_occ where pk_presocc in ("+ CommonUtils.convertSetToSqlInPart(pkPreses, "pk_presocc")+")";
		List<Map<String,Object>> resList=DataBaseHelper.queryForListFj(sql, new Object[]{});
		return resList;
		
	
	}

	/**
	 * 门诊发药外部接口处理逻辑
	 * 外部发药接口，不确定批次，不解预留，不扣库存，不写出入库消耗记录
	 * 1.更新ex_pres_occ状态信息，
	 * 2.更新ex_pres_occ_dt信息，发药数量（付数），金额为收费的数量（付数），金额
	 * 3.写表ex_pres_occ_pddt
	 * 4.调用外部接口，传输处方数据进行发送
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void opSendPres(String param,IUser user){
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPresocc = paraMap.get("pkPresocc").toString();//处方执行主键
		String pkStore = paraMap.get("pkStore").toString();//当前仓库
		
		List<ExPresOccDtVO> presDts = schOpdsPreMapper.QryPresDt(pkPresocc);
		ExPresOcc presOcc = schOpdsPreMapper.QryPresByPk(pkPresocc);
		//更新ex_pres_occ表数据
		Map<String,Object> updParam = new HashMap<String,Object>();
 		updParam.put("flagConf", "1");
 		updParam.put("dateConf", new Date());
 		updParam.put("pkEmpConf", UserContext.getUser().getPkEmp());
 		updParam.put("nameEmpConf",UserContext.getUser().getNameEmp());
 		updParam.put("euStatus", "3");
 		updParam.put("pkPresocc", pkPresocc);
 		schOpdsPreMapper.updExPresOcc(updParam);
 		
 		//更新ex_pres_occ_dt表数据，因调用外部接口数据，即发药数量（付数），发药金额，可为收费数量（付数），金额
 		StringBuilder dtSql=new StringBuilder();
 		dtSql.append("update ex_pres_occ_dt set quan_de=quan_cg ,");
 		dtSql.append(" ords_de=ords_cg,amount_de=amount_cg ");
 		dtSql.append(" where pk_presocc=?");
 		DataBaseHelper.update(dtSql.toString(), new Object[]{pkPresocc});
 		
 		//更新处方明细操作流水ex_pres_occ_pddt
 		List<ExPresOccPddt> presPddt = new ArrayList<ExPresOccPddt>();
 		for(ExPresOccDtVO vo : presDts){
 			ExPresOccPddt pddt = new ExPresOccPddt();
 			pddt.setPkOrg(UserContext.getUser().getPkOrg());
 			pddt.setPkPresoccdt(vo.getPkPresoccdt());
 			pddt.setDateDe(new Date());
 			pddt.setEuDirect("1");
 			pddt.setPkPd(vo.getPkPd());
 			pddt.setPkDeptDe(UserContext.getUser().getPkDept());
 			pddt.setPkStore(pkStore);
 			pddt.setPkUnit(vo.getPkUnit());
 			pddt.setPackSize(vo.getPackSize());
 			pddt.setQuanPack(vo.getQuanCg());
 			pddt.setQuanMin(MathUtils.mul(vo.getQuanCg(), vo.getPackSize().doubleValue()));
 			pddt.setPriceCost(vo.getPrice().doubleValue());
 			pddt.setPrice(vo.getPrice().doubleValue());
 			pddt.setAmountCost(MathUtils.mul(vo.getPrice().doubleValue(),vo.getOrdsCg()));
 			pddt.setAmount(MathUtils.mul(vo.getPrice().doubleValue(),vo.getOrdsCg()));
 			pddt.setNote("外部草药配送流水");
 			ApplicationUtils.setDefaultValue(pddt, true);
 			presPddt.add(pddt);
 			
 		}
 		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPresOccPddt.class), presPddt);
 		
 		//执行发药外接服务
 		if("1".equals(ApplicationUtils.getDeptSysparam("EX0053", UserContext.getUser().getPkDept()))){
 			ScmExtProcessUtils.processExtOpDe(presOcc, paraMap);
 		}
	}
	
	/**
	 * 008003002020
	 * 查询已完成外部发药的草药订单
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryHerbOrder(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		paramMap.put("pkDept", UserContext.getUser().getPkDept());
		List<Map<String,Object>> resList=scmOpDispense2Mapper.qryHerbOrder(paramMap);
		return resList;
	}
	/**
	 * 008003002021
	 * 更新门诊药房处方打印次数
	 * @param param
	 * @param user
	 */
	public void updatePresPrintCnt(String param,IUser user){
		List<String> pkPresoccs=JsonUtil.readValue(param, new TypeReference<List<String>>() {	});
		if(pkPresoccs==null || pkPresoccs.size()<0)return;
		Set<String> pkPresoccSets=new  HashSet<String>(pkPresoccs);
		StringBuffer sql=new StringBuffer();
		sql.append("update EX_PRES_OCC set CNT_PRINT=nvl(CNT_PRINT,0)+1 ");
		sql.append(" where PK_PRESOCC in ("+ CommonUtils.convertSetToSqlInPart(pkPresoccSets, "pk_presocc") +")");
		DataBaseHelper.update(sql.toString(), new Object[]{});
		
	}
	
	/**
	 * 008003002022
	 * 根据患者信息获取患者门诊号
	 * @param param
	 * @param user
	 * @return
	 */
	public List<String> getCodeOpByPiInfo(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		List<String> codeList=scmOpDispense2Mapper.getCodeOpByPiInfo(paramMap);
		return codeList;
	}
	
	/**
	 * 开始发药
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void doStartDrug(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return;
		ExtSystemProcessUtils.processExtMethod("PackMachineOp", "startDoDrug",paramMap);
	}
}
