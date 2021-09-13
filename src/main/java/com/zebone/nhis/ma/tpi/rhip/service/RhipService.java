package com.zebone.nhis.ma.tpi.rhip.service;


import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.zebone.nhis.ma.tpi.rhip.vo.*;
import com.zebone.nhis.scm.st.vo.PdStDetailVo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;
import com.zebone.nhis.common.module.cn.opdw.CnOpEmrRecord;
import com.zebone.nhis.common.module.emr.mgr.EmrOperateLogs;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePage;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageBr;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageCharges;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOps;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOr;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOrDt;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.common.module.scm.st.PdStock;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.tpi.rhip.dao.RhipMapper;
import com.zebone.nhis.ma.tpi.rhip.support.RpDataUtils;
import com.zebone.nhis.ma.tpi.rhip.support.RpWsUtils;
import com.zebone.nhis.ma.tpi.rhip.support.XmlGenUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 第三方接口-区域卫生平台
 * @author chengjia
 *
 */
@Service
public class RhipService {

	@Resource
	private	RhipMapper rhipMapper;

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(RhipService.class);
	/**
	 * 区域平台数据上传
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public List<Object> rpDataTrans(String param , IUser user,PatListVo pat) throws Exception{
		Map map = JsonUtil.readValue(param,Map.class);
		String pkPv = (String) map.get("pkPv");
		List<Object> rtnList=new ArrayList<>();
		rtnList.add("");
		rtnList.add(pat);

		List<PatListVo> list=rhipMapper.queryPatList(map);
		//根据pkpv查询诊断编码和诊断名称
		System.out.println("诊断开始时间"+new Date());
		List<Map<String,String>> diagInfo=rhipMapper.queryDiagInfo(pkPv);
		System.out.println("诊断结束时间"+new Date());
		String diagCode = "";
		String diagName = "";
		String adCode = "";
		String adName = "";
		for (Map<String,String> diagInfoMap: diagInfo) {
			if ("1".equals(diagInfoMap.get("flag")) ) {// 获取主要诊断
				diagCode = diagInfoMap.get("diagcode");
				diagName = diagInfoMap.get("diagname");

			}
			if ("0100".equals(diagInfoMap.get("diagtype")) && "".equals(adCode)) {//获取入院诊断
				adCode = diagInfoMap.get("diagcode");
				adName = diagInfoMap.get("diagname");
			}
			if (StringUtils.isNotBlank(diagCode)&& StringUtils.isNotBlank(adCode)) {
				break;
			}
		}
		if(list==null||list.size()==0){
			rtnList.set(0, "未能找到患者信息");
			return rtnList;
		}
		String xml="";
		String rtnStr="";
		pat=list.get(0);
		pat.setDiagCode(diagCode);
		pat.setDiagName(diagName);
		pat.setAdmissionDiagnosisCode(adCode);
		pat.setAdmissionDiagnosisName(adName);
		//@todo

		//1/住院就诊记录表
		IptRecord record=RpDataUtils.createIptRecord(user, pat);
		xml=XmlGenUtils.create(user, pat, record);
		logger.info("住院就诊记录表");
		rtnStr=RpWsUtils.invokeWS(xml);
		if(!CommonUtils.isEmptyString(rtnStr)){
			rtnList.set(0, "住院就诊记录表:"+rtnStr);
			return rtnList;
		}
		//2/住院入院记录(Ipt_AdmissionNote)
		MedRecVo medRec=getEmrRecByType(pkPv, "0900");
		if(medRec!=null){
			IptAdmissionNote note=RpDataUtils.createIptAdmissionNote(user, pat, medRec);
			xml=XmlGenUtils.create(user, pat, note);

			logger.info("住院入院记录");

			rtnStr=RpWsUtils.invokeWS(xml);
			if(!CommonUtils.isEmptyString(rtnStr)){
				rtnList.set(0, "住院入院记录:"+rtnStr);
				return rtnList;

			}
		}

		//3/住院医嘱明细表(Ipt_AdviceDetail)
		List<TOrderVo> listOrd=rhipMapper.queryCnOrder(map);
		logger.info("住院医嘱明细表 b");
		if(listOrd!=null&&listOrd.size()>0){
			for (TOrderVo cnOrder : listOrd) {
				IptAdviceDetail detail=RpDataUtils.createIptAdviceDetail(user, pat,cnOrder);
				xml=XmlGenUtils.create(user, pat, detail);
				//System.out.println(xml);

 				rtnStr=RpWsUtils.invokeWS(xml);
  				if(!CommonUtils.isEmptyString(rtnStr)){
					rtnList.set(0, "住院医嘱明细表:"+rtnStr);
					return rtnList;
				}
			}
		}
		logger.info("住院体温单 3");
		//4/住院体温单(Ipt_SignsRecord)
		List<Map<String,Object>> listVts=rhipMapper.queryVtsList(map);
		if(listVts!=null&&listVts.size()>0){
			for (Map<String,Object> vtMap : listVts) {
				RhipExVtsOccDtVo dt=new RhipExVtsOccDtVo();
				ExVtsOcc occ=new ExVtsOcc();
				BeanUtils.populate(dt,vtMap);
				BeanUtils.populate(occ,vtMap);
				dt.setOcc(occ);
				IptSignsRecord rec=RpDataUtils.createIptSignsRecord(user, pat,dt);
				xml=XmlGenUtils.create(user, pat, rec);
				rtnStr=RpWsUtils.invokeWS(xml);
				if(!CommonUtils.isEmptyString(rtnStr)){
					//return "住院体温单:"+rtnStr;
					rtnList.set(0, "住院体温单:"+rtnStr);
					return rtnList;
				}
			}
		}
		logger.info("住院体温单 e");
		//5/住院出院记录(Ipt_LeaveRecord)
		medRec=getEmrRecByType(pkPv, "1200");
		if(medRec!=null){
			IptLeaveRecord lRec=RpDataUtils.createIptLeaveRecord(user, pat, medRec);
			xml=XmlGenUtils.create(user, pat, lRec);

			rtnStr=RpWsUtils.invokeWS(xml);
			if(!CommonUtils.isEmptyString(rtnStr)){
				//return "住院出院记录:"+rtnStr;
				rtnList.set(0, "住院出院记录:"+rtnStr);
				return rtnList;
			}
		}
		logger.info("住院出院记录 e");
		//6/住院病案首页(Ipt_MedicalRecordPage)
		EmrHomePage page =rhipMapper.queryHomePageByPv(map);
		if(page!=null){
			String pkPage=page.getPkPage();
			map.put("pkPage", pkPage);
			List<EmrHomePageDiags> diags=rhipMapper.queryHomePageDiags(map);
			page.setDiags(diags);
			List<EmrHomePageOps> ops=rhipMapper.queryHomePageOps(map);
			page.setOps(ops);
			List<EmrHomePageCharges> charges=rhipMapper.queryHomePageCharges(map);
			page.setCharges(charges);

			List<EmrHomePageBr> brs=rhipMapper.queryEmrHomePageBrsByPage(pkPage);
			page.setBrs(brs);
			EmrHomePageOr or=rhipMapper.queryEmrHomePageOrByPage(pkPage);
			page.setOr(or);
			List<EmrHomePageOrDt> ordts=rhipMapper.queryEmrHomePageOrDtsByPage(pkPage);
			page.setOrdts(ordts);

			IptMedicalRecordPage medRecPage=RpDataUtils.createIptMedicalRecordPage(user, pat, page);
			xml=XmlGenUtils.create(user, pat, medRecPage);

			rtnStr=RpWsUtils.invokeWS(xml);
			if(!CommonUtils.isEmptyString(rtnStr)){
				//return "住院病案首页:"+rtnStr;
				rtnList.set(0, "住院病案首页:"+rtnStr);
				return rtnList;
			}

		}
		logger.info("住院病案首页 e");
		//7/手术记录(Pt_Operation)
		List<RhipCnOpApply> listOp=getOpApplyList(param , user);
		if(listOp!=null&&listOp.size()>0){
			for (RhipCnOpApply op : listOp) {
				PtOperation opOp=RpDataUtils.createPtOperation(user, pat, op);
				xml=XmlGenUtils.create(user, pat, opOp);
				rtnStr=RpWsUtils.invokeWS(xml);
				if(!CommonUtils.isEmptyString(rtnStr)){
					//return "住院病案首页:"+rtnStr;
					rtnList.set(0, "手术记录:"+rtnStr);
					return rtnList;
				}
			}
		}

		logger.info("手术记录 e");
		//8/在/出院结算表(Ipt_Fee)
		//map.put("pkPv", "d721a89312fe4515a0c6b5de2a768d83");
		List<Map<String,Object>> listFee=rhipMapper.querySettleList(map);
		List<IptFeeCostVo> iptFeeList = rhipMapper.queryIptFeeCostList(map);
		if(listFee!=null&&listFee.size()>0){
			List<RhipBlSettleVo> listBs=new ArrayList<RhipBlSettleVo>();
			List<RhipBlSettleItemVo> items=new ArrayList<RhipBlSettleItemVo>();
			RhipBlSettleVo bsVo=new RhipBlSettleVo();
			String pkSettle="";

			for (Map<String,Object> invItem : listFee) {
				RhipBlSettleVo vo=new RhipBlSettleVo();
				RhipBlSettleItemVo item=new RhipBlSettleItemVo();
				BeanUtils.populate(vo,invItem);
				BeanUtils.populate(item,invItem);

				if(!pkSettle.equals(vo.getPkSettle())){
					pkSettle=vo.getPkSettle();
					bsVo=new RhipBlSettleVo();
					BeanUtils.copyProperties(bsVo, vo);
					items=new ArrayList<RhipBlSettleItemVo>();
					bsVo.setItems(items);
					listBs.add(bsVo);
				}
				items.add(item);
			}

			if(listBs.size()>0){
				for (RhipBlSettleVo rbsVo : listBs) {
					IptFee iptFee=RpDataUtils.createIptFee(user, pat, rbsVo,iptFeeList);
					xml=XmlGenUtils.create(user, pat, iptFee);
					//System.out.println(xml);

					rtnStr=RpWsUtils.invokeWS(xml);
					if(!CommonUtils.isEmptyString(rtnStr)){
						rtnList.set(0, "出院结算表:"+rtnStr);
						return rtnList;
					}
				}
			}

		}
		logger.info("出院结算表 e");
		//9/住院费用明细表(Ipt_FeeDetail)
		//map.put("pkPv", "ee2bcd2c54c6402c908710008a6c736b");
		List<Map<String,Object>> listDts=rhipMapper.queryBlIpDtList(map);
		List<RhipBlIpDtVo> dts=new ArrayList<RhipBlIpDtVo>();
		for (Map<String,Object> mapDt : listDts) {
			RhipBlIpDtVo dt=new RhipBlIpDtVo();
			BeanUtils.populate(dt,mapDt);
			dts.add(dt);
		}

		if(listDts.size()>0){
			for (RhipBlIpDtVo dt : dts) {
				IptFeeDetail iptFeeDetail=RpDataUtils.createIptFeeDetail(user, pat, dt);
				xml=XmlGenUtils.create(user, pat, iptFeeDetail);
				//System.out.println(xml);

				rtnStr=RpWsUtils.invokeWS(xml);
				if(!CommonUtils.isEmptyString(rtnStr)){
					rtnList.set(0, "住院费用明细表:"+rtnStr);
					return rtnList;
				}
			}
		}


		logger.info("出院结算表 e");
		//门诊数据集
		//1/挂号表(Opt_Register)
		//pkPv="cb1d116406f74d278940fd77e7c72ee6";
		/* 2020-04-24 屏蔽门诊数据集
		pkPv="842cd0148eea4ebab2fbac80ce1317a6";
		map.put("pkPv", pkPv);
		List<PatListVo> listO=rhipMapper.queryPatListOp(map);
		if(listO!=null&&listO.size()>0){
			pat=listO.get(0);
			//pat=rhipMapper.queryPatListOp(map);
			EncVo enc=getPatEncInfo(pkPv,"op");
			if(enc!=null){
				//pat=new PatListVo();
				//BeanUtils.copyProperties(pat, enc.getPv());
				//BeanUtils.copyProperties(pat, enc.getPi());
				if(enc!=null){
					OptRegister reg=RpDataUtils.createOptRegister(user, enc,pat);
					xml=XmlGenUtils.create(user, pat, reg);
					//System.out.println(xml);

					rtnStr=RpWsUtils.invokeWS(xml);
					if(!CommonUtils.isEmptyString(rtnStr)){
						rtnList.set(0, "门诊挂号表:"+rtnStr);
						return rtnList;
					}
				}
				//2/门诊就诊记录表(Opt_Record)
				if(enc!=null){
					CnOpEmrRecord opRec=getOpEmrRec(pkPv);
					OptRecord optRec=RpDataUtils.createOptRecord(user, enc,opRec,pat);
					xml=XmlGenUtils.create(user, pat, optRec);
					//System.out.println(xml);
					rtnStr=RpWsUtils.invokeWS(xml);
					if(!CommonUtils.isEmptyString(rtnStr)){
						rtnList.set(0, "门诊就诊记录表:"+rtnStr);
						return rtnList;
					}
				}
				//3/门诊处方记录表(Opt_Recipe)
				if(enc!=null){
					List<CnPresVo> listPres=getPatPres(pkPv);
					if(listPres!=null&&listPres.size()>0){
						for (CnPresVo pres : listPres) {
							OptRecipe recipe=RpDataUtils.createOptRecipe(user, enc, pres,pat);
							xml=XmlGenUtils.create(user, pat, recipe);
							//System.out.println(xml);

							rtnStr=RpWsUtils.invokeWS(xml);
							if(!CommonUtils.isEmptyString(rtnStr)){
								rtnList.set(0, "门诊处方记录表:"+rtnStr);
								return rtnList;
							}

						}
					}
				}
				//4/门诊收费表(Opt_Fee)
				map.put("pkPv", pkPv);
				listFee=rhipMapper.queryOpSettleList(map);
				if(listFee!=null&&listFee.size()>0){
					List<RhipBlSettleVo> listBs=new ArrayList<RhipBlSettleVo>();
					List<RhipBlSettleItemVo> items=new ArrayList<RhipBlSettleItemVo>();
					RhipBlSettleVo bsVo=new RhipBlSettleVo();
					String pkSettle="";

					for (Map<String,Object> invItem : listFee) {
						RhipBlSettleVo vo=new RhipBlSettleVo();
						RhipBlSettleItemVo item=new RhipBlSettleItemVo();
						BeanUtils.populate(vo,invItem);
						BeanUtils.populate(item,invItem);

						if(!pkSettle.equals(vo.getPkSettle())){
							pkSettle=vo.getPkSettle();
							bsVo=new RhipBlSettleVo();
							BeanUtils.copyProperties(bsVo, vo);
							items=new ArrayList<RhipBlSettleItemVo>();
							bsVo.setItems(items);
							listBs.add(bsVo);

						}
						items.add(item);
					}

					if(listBs.size()>0){
						for (RhipBlSettleVo rbsVo : listBs) {
							OptFee optFee=RpDataUtils.createOptFee(user, pat, rbsVo);
							xml=XmlGenUtils.create(user, pat, optFee);
							//System.out.println(xml);

							rtnStr=RpWsUtils.invokeWS(xml);
							if(!CommonUtils.isEmptyString(rtnStr)){
								rtnList.set(0, "门诊收费表:"+rtnStr);
								return rtnList;
							}
						}
					}

				}


				//5/门诊费用明细表(Opt_FeeDetail)
				List<Map<String,Object>> listFeeDts=rhipMapper.queryBlOpDtList(map);
				List<RhipBlOpDtVo> feeDts=new ArrayList<RhipBlOpDtVo>();
				for (Map<String,Object> mapDt : listFeeDts) {
					RhipBlOpDtVo dt=new RhipBlOpDtVo();
					BeanUtils.populate(dt,mapDt);
					feeDts.add(dt);
				}

				if(feeDts.size()>0){
					for (RhipBlOpDtVo dt : feeDts) {
						OptFeeDetail optFeeDetail=RpDataUtils.createOptFeeDetail(user, pat, dt);
						xml=XmlGenUtils.create(user, pat, optFeeDetail);
						//System.out.println(xml);

						rtnStr=RpWsUtils.invokeWS(xml);
						if(!CommonUtils.isEmptyString(rtnStr)){
							rtnList.set(0, "门诊费用明细表:"+rtnStr);
							return rtnList;
						}
					}
				}
			}

		}
		*/

		//6/诊断明细报告(Pt_Diagnosis)
		List<PvDiagVo> listDiags = rhipMapper.queryPvDiagVos(map);
		if(listDiags!=null&&listDiags.size()>0){
			for (PvDiagVo diag : listDiags) {
				PtDiagnosis ptDiag=RpDataUtils.createPtDiagnosis(user, pat, diag);
				xml=XmlGenUtils.create(user, pat, ptDiag);
				//System.out.println(xml);

				rtnStr=RpWsUtils.invokeWS(xml);
				if(!CommonUtils.isEmptyString(rtnStr)){
					rtnList.set(0, "诊断明细报告:"+rtnStr);
					return rtnList;
				}

			}
		}

		rtnList.set(0, "");
		rtnList.set(1, pat);
		return rtnList;
	}

	/**
	 * 区域平台数据上传--门诊
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public List<Object> rpDataTransOp(String param, IUser user,PatListVo pat) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkPv", param);
		String pkPv = (String) map.get("pkPv");
		List<Object> rtnList=new ArrayList<>();
		rtnList.add("");
		rtnList.add(pat);

		List<PatListVo> list=rhipMapper.queryPatListOp(map);
		//根据pkpv查询诊断编码和诊断名称
		List<Map<String,String>> diagInfo=rhipMapper.queryDiagInfo(pkPv);
		String diagCode = "";
		String diagName = "";
		String adCode = "";
		String adName = "";
		for (Map<String,String> diagInfoMap: diagInfo) {
			if ("1".equals(diagInfoMap.get("flag")) ) {// 获取主要诊断
				diagCode = diagInfoMap.get("diagcode");
				diagName = diagInfoMap.get("diagname");

			}
			if ("0100".equals(diagInfoMap.get("diagtype")) && "".equals(adCode)) {//获取入院诊断
				adCode = diagInfoMap.get("diagcode");
				adName = diagInfoMap.get("diagname");
			}
			if (StringUtils.isNotBlank(diagCode)&& StringUtils.isNotBlank(adCode)) {
				break;
			}
		}
		if(list==null||list.size()==0){
			rtnList.set(0, "未能找到患者信息");
			return rtnList;
		}
		String xml="";
		String rtnStr="";
		pat=list.get(0);
		pat.setDiagCode(diagCode);
		pat.setDiagName(diagName);
		pat.setAdmissionDiagnosisCode(adCode);
		pat.setAdmissionDiagnosisName(adName);
		//@todo

		//1.病人基本信息(Bs_Patient)
		List<CardInfo> card=new ArrayList<CardInfo>();
		List<AddressInfo> addr=new ArrayList<AddressInfo>();
		if(pat!=null) {
			//card=rhipMapper.queryCardInfo(pat.getPkPi());
			card=zhCard(pat);
			addr=zhAddr(pat);
		}
		BsPatient patient=RpDataUtils.createBsPatient(user, pat,card,addr);
		xml=XmlGenUtils.create(user, pat, patient);
		logger.info("病人基本信息");
		rtnStr=RpWsUtils.invokeWS(xml);
		if(!CommonUtils.isEmptyString(rtnStr)){
			rtnList.set(0, "病人基本信息:"+rtnStr);
			return rtnList;
		}
		
		EncVo enc=getPatEncInfo(pkPv,"op");
		if(enc!=null){
			//pat=new PatListVo();
			//BeanUtils.copyProperties(pat, enc.getPv());
			//BeanUtils.copyProperties(pat, enc.getPi());
			if(enc!=null){
				OptRegister reg=RpDataUtils.createOptRegister(user, enc,pat);
				xml=XmlGenUtils.create(user, pat, reg);
				//System.out.println(xml);

				rtnStr=RpWsUtils.invokeWS(xml);
				if(!CommonUtils.isEmptyString(rtnStr)){
					rtnList.set(0, "门诊挂号表:"+rtnStr);
					return rtnList;
				}
			}
			//2/门诊就诊记录表(Opt_Record)
			if(enc!=null){
				CnOpEmrRecord opRec=getOpEmrRec(pkPv);
				OptRecord optRec=RpDataUtils.createOptRecord(user, enc,opRec,pat);
				xml=XmlGenUtils.create(user, pat, optRec);
				//System.out.println(xml);
				rtnStr=RpWsUtils.invokeWS(xml);
				if(!CommonUtils.isEmptyString(rtnStr)){
					rtnList.set(0, "门诊就诊记录表:"+rtnStr);
					return rtnList;
				}
			}
			//3/门诊处方记录表(Opt_Recipe)
			if(enc!=null){
				List<CnPresVo> listPres=getPatPres(pkPv);
				if(listPres!=null&&listPres.size()>0){
					for (CnPresVo pres : listPres) {
						OptRecipe recipe=RpDataUtils.createOptRecipe(user, enc, pres,pat);
						xml=XmlGenUtils.create(user, pat, recipe);
						//System.out.println(xml);

						rtnStr=RpWsUtils.invokeWS(xml);
						if(!CommonUtils.isEmptyString(rtnStr)){
							rtnList.set(0, "门诊处方记录表:"+rtnStr);
							return rtnList;
						}

					}
				}
			}
			//4/门诊收费表(Opt_Fee)
			//map.put("pkPv", pkPv);
			List<Map<String,Object>> listFee=rhipMapper.queryOpSettleList(map);
			if(listFee!=null&&listFee.size()>0){
				List<RhipBlSettleVo> listBs=new ArrayList<RhipBlSettleVo>();
				List<RhipBlSettleItemVo> items=new ArrayList<RhipBlSettleItemVo>();
				RhipBlSettleVo bsVo=new RhipBlSettleVo();
				String pkSettle="";

				for (Map<String,Object> invItem : listFee) {
					RhipBlSettleVo vo=new RhipBlSettleVo();
					RhipBlSettleItemVo item=new RhipBlSettleItemVo();
					BeanUtils.populate(vo,invItem);
					BeanUtils.populate(item,invItem);

					if(!pkSettle.equals(vo.getPkSettle())){
						pkSettle=vo.getPkSettle();
						bsVo=new RhipBlSettleVo();
						BeanUtils.copyProperties(bsVo, vo);
						items=new ArrayList<RhipBlSettleItemVo>();
						bsVo.setItems(items);
						listBs.add(bsVo);

					}
					items.add(item);
				}

				if(listBs.size()>0){
					for (RhipBlSettleVo rbsVo : listBs) {
						OptFee optFee=RpDataUtils.createOptFee(user, pat, rbsVo);
						xml=XmlGenUtils.create(user, pat, optFee);
						//System.out.println(xml);

						rtnStr=RpWsUtils.invokeWS(xml);
						if(!CommonUtils.isEmptyString(rtnStr)){
							rtnList.set(0, "门诊收费表:"+rtnStr);
							return rtnList;
						}
					}
				}

			}


			//5/门诊费用明细表(Opt_FeeDetail)
			List<Map<String,Object>> listFeeDts=rhipMapper.queryBlOpDtList(map);
			List<RhipBlOpDtVo> feeDts=new ArrayList<RhipBlOpDtVo>();
			for (Map<String,Object> mapDt : listFeeDts) {
				RhipBlOpDtVo dt=new RhipBlOpDtVo();
				BeanUtils.populate(dt,mapDt);
				feeDts.add(dt);
			}

			if(feeDts.size()>0){
				for (RhipBlOpDtVo dt : feeDts) {
					OptFeeDetail optFeeDetail=RpDataUtils.createOptFeeDetail(user, pat, dt);
					xml=XmlGenUtils.create(user, pat, optFeeDetail);
					//System.out.println(xml);

					rtnStr=RpWsUtils.invokeWS(xml);
					if(!CommonUtils.isEmptyString(rtnStr)){
						rtnList.set(0, "门诊费用明细表:"+rtnStr);
						return rtnList;
					}
				}
			}
			
			//6.门诊病历
			if(enc!=null){
				CnOpEmrRecord opRec=getOpEmrRec(pkPv);
				OptBL optBl=RpDataUtils.createOptBL(user,pat,opRec,null);
				xml=XmlGenUtils.create(user, pat, optBl);
				//System.out.println(xml);
				rtnStr=RpWsUtils.invokeWS(xml);
				if(!CommonUtils.isEmptyString(rtnStr)){
					rtnList.set(0, "门诊病历表:"+rtnStr);
					return rtnList;
				}
			}
		}
		rtnList.set(0, "");
		rtnList.set(1, pat);
		return rtnList;
	}
	
	public List<String> queryPvList(){
		List<String> pkPvs=rhipMapper.queryPvList();
		return pkPvs;
	}
	/**
	 * 区域平台数据上传--门诊药房药库
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public List<Object> rpDataTransOpPMC(String param , IUser user) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		List<Object> rtnList=new ArrayList<>();
		rtnList.add("");
		String xml="";
		String rtnStr="";
		//查询入出库单
		map.put("euDirect", "-1");
		List<PdstDetail> store=rhipMapper.queryPdstList(map);
		//1. 药房出库(PMC_DrugStore)
		for (PdstDetail pdstDetail : store) {
			PMCDrugStore drugStore=RpDataUtils.createPMCDrugStore(user,pdstDetail);
			xml=XmlGenUtils.create(user, null, drugStore);
			rtnStr=RpWsUtils.invokeWS(xml);
			if(!CommonUtils.isEmptyString(rtnStr)){
				rtnList.set(0, "药房出库:"+rtnStr);
				return rtnList;
			}
		}
		//2. 药房入库(PMC_DrugEntry)
		/*PMCDrugEntry drugEntry=RpDataUtils.createPMCDrugEntry(user,pdstDetail);
		xml=XmlGenUtils.create(user, null, drugEntry);
		rtnStr=RpWsUtils.invokeWS(xml);
		if(!CommonUtils.isEmptyString(rtnStr)){
			rtnList.set(0, "药房入库:"+rtnStr);
			return rtnList;
		}
				
		//3.药房库存(PMC_DrugStock)
		PdStock pdstock=new PdStock();
		PMCDrugStock drugStock=RpDataUtils.createPMCDrugStock(user,pdstock);
		xml=XmlGenUtils.create(user, null, drugStock);
		rtnStr=RpWsUtils.invokeWS(xml);
		if(!CommonUtils.isEmptyString(rtnStr)){
			rtnList.set(0, "药房库存:"+rtnStr);
			return rtnList;
		}
				
		//4.药库入库(PMC_PharmacyEntry)
		PMCPharmacyEntry pharmacyEntry=RpDataUtils.createPMCPharmacyEntry(user,pdstDetail);
		xml=XmlGenUtils.create(user, null, pharmacyEntry);
		rtnStr=RpWsUtils.invokeWS(xml);
		if(!CommonUtils.isEmptyString(rtnStr)){
			rtnList.set(0, "药库入库:"+rtnStr);
			return rtnList;
		}
				
		//5.药库库存(PMC_PharmacyStock)
		PMCPharmacyStock pharmacyStock=RpDataUtils.createPMCPharmacyStock(user,pdstDetail);
		xml=XmlGenUtils.create(user, null, pharmacyStock);
		rtnStr=RpWsUtils.invokeWS(xml);
		if(!CommonUtils.isEmptyString(rtnStr)){
			rtnList.set(0, "药库库存:"+rtnStr);
			return rtnList;
		}*/
		rtnList.set(0, "");
		return rtnList;
	}
//    //调用webservice接口
//	private String invokeWS(String xml) throws BusException {
//		String rtnStr=RhipWSInvoke.execute(xml);
//		if(rtnStr.isEmpty()){
//			//throw new BusException("调用接口返回值为空！");
//			return "调用接口返回值为空！";
//		}
//		if(rtnStr.indexOf("您没有接入权限，请和管理员联系")>=0){
//			//throw new BusException("调用接口失败："+rtnStr);
//			return "调用接口失败："+rtnStr;
//		}
//		//System.out.println("rtn:"+rtnStr);
//		Response resp=XmlGenUtils.resolveResp(rtnStr);
//		if(resp==null){
//			//throw new BusException("取应答为空！");
//			return "取应答为空！";
//		}
//		String code=resp.getCode();
//		if(!code.isEmpty()&&code.equals("200")){
//			//成功
//			return "";
//		}else{
//			String msg="";
//			if(resp.getMessage()!=null){
//				msg="Describe："+resp.getMessage().getDescribe();
//				msg+="\r\n";
//				msg+="EventId："+resp.getMessage().getEventId();
//			}
//			//System.out.println("err:"+msg);
//			Log.info("xml:"+xml);
//			//throw new BusException("调用接口失败："+msg);
//			return "调用接口失败："+msg;
//		}
//	}


	/**
	 * 查询病历文档段落内容
	 * 说明：1、病程记录：typeCode[1000]paraCode[null] 2、主诉：typeCode[0900]paraCode[01]...
	 * @param pkPv就诊主键
	 * @param typeCode文档分类编码
	 * @param paraCode文档段落编码
	 * @return
	 */
	public MedRecVo getEmrRecByType(String pkPv , String typeCode){
		MedRecVo rec=null;
		String text="";
		Map<String,String> map=new HashMap<String,String>();
		map.put("pkPv", pkPv);
		map.put("typeCode", typeCode+"%");
		String orderBy=" order by rec.rec_date desc,rec.seq_no desc,rec.pk_rec";
		map.put("orderBy", orderBy);

		List<MedRecVo> rtnList=rhipMapper.queryPatMedRecDoc(map);
		if(rtnList!=null&&rtnList.size()>0){
			rec=rtnList.get(0);
			EmrMedDoc doc =rhipMapper.getEmrMedDocById(rec.getPkDoc());
			rec.setMedDoc(doc);
		}

		return rec;
	}

	/*	*//**
	 * 查询病历文档段落内容
	 * 说明：1、病程记录：typeCode[1000]paraCode[null] 2、主诉：typeCode[090001]paraCode[01]...
	 * @param pkPv就诊主键
	 * @param typeCode文档分类编码
	 * @param paraCode文档段落编码
	 * @return
	 *//*
	public String getPatEmrParaText(String pkPv , String typeCode,String paraCode){
		String text="";
		Map<String,String> map=new HashMap<String,String>();
		map.put("pkPv", pkPv);
		map.put("typeCode", typeCode+"%");
		String orderBy=" order by rec.rec_date desc,rec.seq_no desc,rec.pk_rec";
		map.put("orderBy", orderBy);

		List<EmrMedRec> rtnList=rhipMapper.queryPatMedRecDoc(map);
		Iterator<Element> it = null;
		Iterator<Element> itC = null;
		Element node=null;
		Element nodeC=null;
		String nodeName="";
		String paraCodeElement="";
		String paraCodeElementC="";
		Element nodeText=null;
		String recDateStr="";
		Date recDate=null;
		String pkRec="";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		if(rtnList!=null&&rtnList.size()>0){
			EmrMedRec rec=rtnList.get(0);
			String flagCourseStr=rec.getDocType().getFlagCourse();

			Boolean flagCourse=flagCourseStr==null?false:(flagCourseStr.equals("1")?true:false);
			if(rec.getPkDoc()!=null){
				EmrMedDoc doc =recMapper.getEmrMedDocById(rec.getPkDoc());
				if(doc==null) return "";
				String docXml=doc.getDocXml();
				if(docXml!=null&&!docXml.equals("")){
					try {
						//docXml=docXml.replace("\\<DocObjContent[^>]*\\", "");
						Pattern pattern = Pattern.compile("<DocObjContent(.*?)>");
						Matcher matcher = pattern.matcher(docXml);
						if(matcher.find()){
							String str=matcher.group(1);
							docXml=docXml.replace(str,"");
						}

						SAXReader reader = new SAXReader();
						Document document = reader.read(new java.io.ByteArrayInputStream(docXml.getBytes("utf-8")));
						Element root = document.getRootElement();
						it = root.elementIterator();
						while (it.hasNext()) {
				            // 获取某个子节点对象
				            node = it.next();
				            nodeName=node.getName();
				            if(nodeName==null||!nodeName.equals("Region")) continue;
							//System.out.println(node.getName());
//							List<Attribute> list = node.attributes();
//							for (Attribute attr : list) {
//					            System.out.println(attr.getText() + "-----" + attr.getName()
//					                    + "---" + attr.getValue());
//					        }
				            if(flagCourse==false){
				            	//非病程
								if(node.attribute("para_code")==null) continue;
								paraCodeElement=node.attribute("para_code").getText();
								//if(node.attribute("para_name")==null) continue;
								//String paraNameElement=node.attribute("para_name").getText();
								if(paraCodeElement!=null){
									if(paraCodeElement.equals(paraCode)||paraCode==null||paraCode.equals("")){
										nodeText= node.element("Content_Text");
										if(nodeText!=null){
											text=nodeText.getText();
										}
										break;
									}
								}

				            }else{
				            	//病程
								if(node.attribute("pk_rec")==null) continue;
								if(node.attribute("rec_date")==null) continue;
								recDateStr=node.attribute("rec_date").getText();
								pkRec=node.attribute("pk_rec").getText();
								if(recDate==null||recDate.compareTo(format.parse(recDateStr))<0){
									recDate=format.parse(recDateStr);
									nodeText= node.element("Content_Text");
									if(nodeText!=null){
										text = nodeText.getText();
									}

//									itC = node.elementIterator();
//									while (itC.hasNext()) {
//							            nodeC = itC.next();
//							            if(nodeC.attribute("para_code")==null) continue;
//							            paraCodeElementC=nodeC.attribute("para_code").getText();
//										if(paraCodeElementC!=null){
//											if(paraCodeElementC.equals(pkRec)){
//												nodeText= nodeC.element("Content_Text");
//												if(nodeText!=null){
//													text=nodeText.getText();
//												}
//												break;
//											}
//										}
//									}

								}

				            }
				        }
						document.toString();


					} catch (BusException e) {
						// TODO: handle BusException
						e.printStackTrace();
						return "";
					}

				}
			}

		}
		if(text!=null){
			text=text.trim();
			//text=text.replaceAll("[\\t]", "");//text.replaceAll("[\\t\\n\\r]", ""); \t为制表符 \n为换行 \r为回车
			text= text.replaceAll("(\r?\n()+)", "\r\n");
			text=text.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");
		}
		//System.out.println("text:"+text);
		return text;
	}    */

	/**
	 * 获取指定患者的手术申请
	 * @param param
	 * @param user
	 * @return
	 */
	public List<RhipCnOpApply> getOpApplyList(String param , IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		User u = (User)user;
		List<RhipCnOpApply> ret = rhipMapper.getOpApplyList(paramMap.get("pkPv"), u.getPkOrg());
		List<String> pkOpList = new ArrayList<String>();
		HashMap<String, CnOpApply> hmApply = new HashMap<String, CnOpApply>();
		for(CnOpApply apply : ret){
			pkOpList.add(apply.getPkOrdop());
			hmApply.put(apply.getPkOrdop(), apply);
		}
		if(pkOpList.size()>0){
			List<CnOpSubjoin> childList = rhipMapper.getChildApplyList(pkOpList);
			for(CnOpSubjoin child : childList){
				CnOpApply apply = hmApply.get(child.getPkOrdop());
				if(apply!=null)
					apply.getSubOpList().add(child);
			}
		}
		return ret;
	}


	//8/在/出院结算表(Ipt_Fee)
//	map.put("pkPv", "ee2bcd2c54c6402c908710008a6c736b");
//	List<Map<String,Object>> listFee=rhipMapper.querySettleInvList(map);
//	if(listFee!=null&&listFee.size()>0){
//		List<RhipBlInvoice> listBi=new ArrayList<RhipBlInvoice>();
//		List<BlInvoiceDt> items=new ArrayList<BlInvoiceDt>();
//		RhipBlInvoice bi=new RhipBlInvoice();
//		String codeInv="";
//		for (Map<String,Object> invItem : listFee) {
//			BlSettle bs=new BlSettle();
//			BlInvoice inv=new BlInvoice();
//			BlInvoiceDt dt=new BlInvoiceDt();
//
//			BeanUtils.populate(bs,invItem);
//			BeanUtils.populate(inv,invItem);
//			BeanUtils.populate(dt,invItem);
//			if(codeInv!=inv.getCodeInv()){
//				bi=new RhipBlInvoice();
//				bi.setBs(bs);
//				items=new ArrayList<BlInvoiceDt>();
//				bi.setItems(items);
//				bi.setInv(inv);
//				listBi.add(bi);
//
//				codeInv=inv.getCodeInv();
//			}
//			items.add(dt);
//		}
//		if(listBi.size()>0){
//			for (RhipBlInvoice blInvoice : listBi) {
//				IptFee iptFee=RpDataUtils.createIptFee(user, pat, blInvoice);
//				xml=XmlGenUtils.create(user, pat, iptFee);
//				System.out.println(xml);
//				invokeWS(xml);
//			}
//		}
//
//	}

	/**
	 * 查询就诊记录
	 * @param pkPv就诊主键
	 * @param encType ip:住院 op:门诊
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetBusException
	 * @throws IllegalAccessBusException
	 */
	public EncVo getPatEncInfo(String pkPv,String encType) throws IllegalAccessException, InvocationTargetException{
		EncVo enc=null;
		Map<String,String> paramMap=new HashMap<String,String>();
		paramMap.put("pkPv", pkPv);
		List<Map<String,Object>> list=null;
		if(encType!=null&&encType.equals("op")){
			list=rhipMapper.queryPatEncInfoOp(paramMap);
		}else{
			list=rhipMapper.queryPatEncInfo(paramMap);
		}

		if(list!=null&&list.size()>0){
			enc=new EncVo();
			Map<String,Object> map =list.get(0);
			BeanUtils.populate(enc,map);

			PvEncounter pv=new PvEncounter();
			PiMaster pi=new PiMaster();

			if(encType!=null&&encType.equals("op")){
				PvOp op=new PvOp();
				BeanUtils.populate(op,map);
				enc.setPvOp(op);

			}else{
				PvIp ip=new PvIp();
				BeanUtils.populate(ip,map);
				enc.setPvIp(ip);
			}

			BeanUtils.populate(pv,map);
			BeanUtils.populate(pi,map);

			enc.setPv(pv);
			enc.setPi(pi);
		}

		return enc;
	}

	/**
	 * 获取指定患者的门诊病历记录
	 * @param param
	 * @param user
	 * @return
	 */
	public CnOpEmrRecord getOpEmrRec(String pkPv)  {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("pkPv", pkPv);
		CnOpEmrRecord ret = rhipMapper.queryPatEmrOp(paramMap);

		return ret;
	}

	public List<CnPresVo> getPatPres(String pkPv) throws IllegalAccessException, InvocationTargetException  {
		List<Map<String,Object>> list = rhipMapper.queryPatPresList(pkPv);
		List<CnPresVo> rtnList=new ArrayList<>();
		List<TOrderVo> ords=new ArrayList<>();
		CnPresVo pres=new CnPresVo();
		TOrderVo ord=new TOrderVo();
		String pkPres="";
		if(list!=null&&list.size()>0){
			for (Map<String,Object> map : list) {
				if(map.get("pkPres")==null) continue;

				String pkPresTmp=map.get("pkPres").toString();

				if(!pkPres.equals(pkPresTmp)){
					pres=new CnPresVo();
					BeanUtils.populate(pres,map);
					ords=new ArrayList<>();

					pres.setOrders(ords);

					pkPres=pres.getPkPres();

					rtnList.add(pres);
				}
				ord=new TOrderVo();
				BeanUtils.populate(ord,map);
				ords.add(ord);
			}
		}

		return rtnList;
	}

	/**
	 * 根据条件查询出院患者列表
	 * @param map
	 * @return
	 * @throws ParseException
	 * @throws BusException
	 */
	public List<OutPvPatiVo> getOutPatiList(Map<String, Object> map) throws ParseException {
		List<OutPvPatiVo> outPatis = rhipMapper.queryOutPatiList(map);
		List<OutPvPatiVo> returnPatis=new ArrayList<OutPvPatiVo>();
		String isAutoStart=map.get("isAutoStart")==null?"":map.get("isAutoStart").toString();
		if(isAutoStart.equals("true")||isAutoStart.equals("1")){
			int intervalDay=1;
			String days=ApplicationUtils.getPropertyValue("rhip_interval_days", "");
			if(days!=null){
				intervalDay=Integer.parseInt(days.trim());
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//声明接受日期格式
			Calendar cal = Calendar.getInstance();
			Date beginTime=null;//声明终末质控时间
			Date endTime=null;//声明上一次就诊的结束时间
			Date now=new Date();
			for (OutPvPatiVo outPvPatiVo : outPatis) {
				if(outPvPatiVo.getFinalQcDate()==null) continue;
				String begin=sf.format(outPvPatiVo.getFinalQcDate());
				String end=sf.format(now);
				beginTime=sf.parse(begin);
				endTime=sf.parse(end);
				cal.setTime(beginTime);
				long time1 = cal.getTimeInMillis();
				cal.setTime(endTime);
				long time2 = cal.getTimeInMillis();
				//算两次时间相差的天数
				long betweenDays=(time2-time1)/(1000*3600*24);
				Integer daysBetween=Integer.parseInt(String.valueOf(betweenDays));
				if(daysBetween>=intervalDay){
					returnPatis.add(outPvPatiVo);
				}
			}
			return returnPatis;
		}
		return outPatis;
	}

	/**
	 * 更新出院患者上传状态
	 * @param pkPv
	 * @return
	 */
	public int updateRhipStatusFromEmr(String pkPv,String pkEmp){
		
		int count = DataBaseHelper.update("update emr_pat_rec set eu_status_rhip ='1',pk_emp_rhip=?,rhip_date=? where pk_pv = ? ", 
				new Object[]{pkEmp,new Date(),pkPv});
		return count;
	}
	/**
	 * 门诊区域更新上传状态
	 * @param pkPv
	 * @param pkEmp
	 * @return
	 */
	public int insertRhipStatusFromEmr(String pkPv,String pkEmp,String objName){
		RpDataUpload rpDataUpload=new RpDataUpload();
		rpDataUpload.setPkRpData(NHISUUID.getKeyId());
		rpDataUpload.setObjName(objName);
		rpDataUpload.setPkPv(pkPv);
		rpDataUpload.setFlagUpload("1");
		rpDataUpload.setCreator(pkEmp);
		rpDataUpload.setDelFlag("0");
		int count = DataBaseHelper.insertBean(rpDataUpload);
		return count;
	}
	public void insertErrLogs(String pkPv, String errTxt) {
		EmrOperateLogs emrOpeLogs = new EmrOperateLogs();
		emrOpeLogs.setCode("rhip_upload");
		emrOpeLogs.setDelFlag("0");
		emrOpeLogs.setEuStatus("0");
		emrOpeLogs.setPkOrg(UserContext.getUser().getPkOrg());
		emrOpeLogs.setPkPv(pkPv);
		emrOpeLogs.setOperateTxt(errTxt);
		emrOpeLogs.setPkLog(UUID.randomUUID().toString().replace("-",""));
		DataBaseHelper.insertBean(emrOpeLogs);
	}
	public List<CardInfo> zhCard(PatListVo pat){
		List<CardInfo> cardList=new ArrayList<CardInfo>();
		if(pat.getInsurNo()!=null) {
			CardInfo card=new CardInfo();
			card.setCardtype("01");
			card.setCardno(pat.getInsurNo());
			cardList.add(card);
		}
		
		return cardList;
	}
	public List<AddressInfo> zhAddr(PatListVo pat){
		List<AddressInfo> addrList=new ArrayList<AddressInfo>();
		if(pat.getAddress()!=null) {
			AddressInfo addr=new AddressInfo();
			addr.setAddresstype("09");
			addr.setAddress(pat.getAddress());
			addrList.add(addr);
		}
		if(pat.getUnitWork()!=null) {
			AddressInfo addr=new AddressInfo();
			addr.setAddresstype("02");
			addr.setAddress(pat.getUnitWork());
			addrList.add(addr);
		}
		return addrList;
	}
}
