package com.zebone.nhis.pro.zsba.adt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.pub.vo.CnLabApplyVo;
import com.zebone.nhis.ex.pub.dao.AdtPubMapper;
import com.zebone.nhis.pi.pub.service.PiCodeFactory;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.nhis.pro.zsba.adt.vo.CnRisApplyBaVo;
import com.zebone.nhis.pro.zsba.adt.vo.LabAndRisTripartiteSystemVo;
import com.zebone.nhis.pro.zsba.adt.vo.PiMasterBa;
import com.zebone.nhis.pro.zsba.adt.vo.SaveNhisDeptMapVo;
import com.zebone.nhis.pro.zsba.adt.vo.TPatientWxMapper;
import com.zebone.nhis.pro.zsba.adt.vo.ZsbaHisAdtPv;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ZsbaAdtHandler {

	@Resource
	private ZsbaAdtService zsbaAdtService;
	private Logger logger = LoggerFactory.getLogger("com.zebone");
	@Autowired
	private AdtPubMapper adtPubMapper;

    @Autowired
    private PiCodeFactory piCodeFactory;

	/**
	 * 1.获取新住院号（code_ip）
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public String getCodeIpFromHis(String param, IUser user) {
		DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
		String codeIp = "";
		codeIp = zsbaAdtService.getCodeIpFromHis();//获取住院号
		DataSourceRoute.putAppId("default");//切换到 NHIS
		return codeIp;
	}
	
	/**
	 * 1.获取新患者编码（code_pi）
	 * @param param
	 * @param user
	 * @return
	 */
	public String getCodePiFromHis(String param, IUser user) {
		DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
		String codePi = "";
		codePi = zsbaAdtService.getCodePiFromHis();//获取患者编码
		DataSourceRoute.putAppId("default");//切换到 NHIS
		return codePi;
	}
	
	/**
	 * 2.获取相似患者登记记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ZsbaHisAdtPv> queryPvsFromHis(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(null == paramMap.get("namePi") || CommonUtils.isEmptyString(paramMap.get("namePi").toString())) 
			throw new BusException("未获取到患者姓名！");

		//1、切换到 HIS 获取历史就诊记录
		DataSourceRoute.putAppId("HIS_bayy");
		List<ZsbaHisAdtPv> listHis = zsbaAdtService.getHisPvList(paramMap);
		
		//2、切换到 病案 获取历史就诊记录
		DataSourceRoute.putAppId("BAGL_bayy");
		List<ZsbaHisAdtPv> listBagl = zsbaAdtService.getBaglPvList(paramMap);
		
		//3、切换到 NHIS 获取历史就诊记录
		DataSourceRoute.putAppId("default");
		List<ZsbaHisAdtPv> listNhis = zsbaAdtService.getNhisPvList(paramMap);
		
		//4.1、抽取 his 和  病案中患者最大就诊次数
		List<ZsbaHisAdtPv> listHisAndBagl = filterPvInfo(listHis,listBagl,true);
		//4.2、抽取 Nhis 和 旧系统中患者最大就诊次数
		List<ZsbaHisAdtPv> listTotal = filterPvInfo(listNhis,listHisAndBagl,false);
		
		return listTotal;
	}
	
	/**
	 * 从两个系统中根据【住院号】合并历史就诊信息
	 * ，以获取最大的就诊次数及相关记录
	 * @param listPro
	 * @param listAfter
	 * @param flagHis ： true = his+bagl,false = nhis+his+bagl
	 * @return
	 */
	private List<ZsbaHisAdtPv> filterPvInfo(List<ZsbaHisAdtPv> listPro,List<ZsbaHisAdtPv> listAfter,boolean flagHis){
		List<ZsbaHisAdtPv> list = new ArrayList<ZsbaHisAdtPv>();
		if(null == listPro || listPro.size() < 1) return listAfter;//前面无，则返回后面
		if(null == listAfter || listAfter.size() < 1) return listPro;//后面无，则直接返回前
		ZsbaHisAdtPv pro = null;
		ZsbaHisAdtPv aft = null;
		int cnt = 0;
		for(int i = 0  ; i < listPro.size(); i++){
			pro = listPro.get(i);
			cnt = 0;
			for (int j = 0; j <listAfter.size(); j++) {
				aft = listAfter.get(j);
				if(((flagHis && !aft.isFlagTransHisAndBaGl())//his + 病案
						|| (!flagHis && !aft.isFlagTranNhisAndHis())) // NHIS + his + 病案
						&& pro.getInpatientNo().equals(aft.getInpatientNo())
						&& pro.getName().equals(aft.getName())) {
					if(pro.getAdmissTimes() < aft.getAdmissTimes())
					{
						pro.setAdmissTimes(aft.getAdmissTimes());
						pro.setAdmissDate(aft.getAdmissDate()); 
						pro.setAdmissDiagStr(aft.getAdmissDiagStr());
						pro.setDeptName(aft.getDeptName());
						pro.setWardName(aft.getWardName());
					}
					list.add(pro);
					if(flagHis)
						aft.setFlagTransHisAndBaGl(true);//his + 病案
					else
						aft.setFlagTranNhisAndHis(true);// NHIS + his + 病案
					break;
				}else{
					cnt ++;
				}
			}
			if(cnt == listAfter.size())
				list.add(pro);
		}
		for (ZsbaHisAdtPv after : listAfter) {
			if( ((flagHis && !after.isFlagTransHisAndBaGl()) || (!flagHis && !after.isFlagTranNhisAndHis())))
				list.add(after);
		}
		return list;
	}
	
	/**
	 * 3. 从 旧his系统获取门诊待缴费清单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getOpFeeList(String param, IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

		//patientId 、 times 不为空则走旧系统获取门诊待缴费清单
		if( null !=map.get("patientId") && !CommonUtils.isEmptyString(map.get("patientId").toString())){
			//切换到 HIS 获取门诊待缴费清单
			DataSourceRoute.putAppId("HIS_bayy");
			List<Map<String,Object>> hisList = zsbaAdtService.getOpFeeListFromHis(map);
			if(null != hisList && hisList.size() > 0)
				list.addAll(hisList);
			//切换到 NHIS 
			DataSourceRoute.putAppId("default");
		}
		else if(null !=map.get("pkPvOp") && !CommonUtils.isEmptyString(map.get("pkPvOp").toString())){
			//根据 pkPvOp 从到 nhis 获取门诊待缴费清单
			List<Map<String,Object>> nhisList = zsbaAdtService.getOpFeeListFromNhis(map);
			if(null != nhisList && nhisList.size() > 0)
				list.addAll(nhisList);
		}
		return list;
	}

	/**
	 * 4.获取旧系统科室记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryHisDeptList(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);

		//1、切换到 HIS 获取历史就诊记录
		DataSourceRoute.putAppId("HIS_bayy");
		List<Map<String,Object>> listHis = zsbaAdtService.getHisDeptList(paramMap);
		DataSourceRoute.putAppId("default");
		
		return listHis;
	}
	
	/**
	 * 5.获取Nhis系统已对照科室记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryNhisDeptMapList(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);

		//1、从 NHIS 获取科室对照记录
		List<Map<String,Object>> listHis = zsbaAdtService.getNhisDeptMapList(paramMap);
		
		return listHis;
	}

	/**
	 * 6.往 Nhis系统 保存对照科室记录
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveNhisDeptMapList(String param, IUser user){
		
		//1、获取前台返回待插入、更新、删除的记录【存在多个list，用VO接收】
		SaveNhisDeptMapVo nhisDept = JsonUtil.readValue(param, SaveNhisDeptMapVo.class);
		if(null == nhisDept)
			throw new BusException("未获取到待保存的科室对照记录！");
		
		//2、往 NHIS 中保存科室对照记录
		zsbaAdtService.saveNhisDeptMapList(nhisDept.getAddList(),nhisDept.getUpList(),nhisDept.getDelList());
	}

	/**
	 * 7.从 his 系统中同步微信列表至 nhis系统
	 * @param param
	 * @param user
	 * @return
	 */
	public void updateWxListByPatient(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
		Map<String, Object> paramO = new HashMap<String, Object>();
		paramO.put("patientId", paramMap.get("patientId"));
		List<TPatientWxMapper> listFromHis = zsbaAdtService.getPatientWxList(paramO);//获取his系统中的微信list
		DataSourceRoute.putAppId("default");//切换到 NHIS
		if (null == listFromHis || listFromHis.size() < 1) return;
		zsbaAdtService.updatePatientWxList(listFromHis, paramMap);
	}

	/**
	 * @return void
	 * @Description 根据第三方信息更新检验检查状态
	 * @auther wuqiang
	 * @Date 2020-09-25
	 * @Param [param, user]
	 */
	public void updateRisAndLabStatusByTripartiteSystem(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if ((null == paramMap.get("pkPv") || CommonUtils.isEmptyString(paramMap.get("pkPv").toString()))
				&& (null == paramMap.get("pkPvMa") || CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString()))) {
			throw new BusException("pkPv为空，请确认是否选择了需要出院的患者！");
		}
		User user1 = (User) user;
		paramMap.put("pkDeptOcc", user1.getPkDept());
		List<LabAndRisTripartiteSystemVo> labTripartiteSystemVos = null;
		List<LabAndRisTripartiteSystemVo> risTripartiteSystemVos = null;
		//处理检验
		List<CnLabApplyVo> cnlabApplyVoNotDone = zsbaAdtService.getCnlabApplyVoNotDone(paramMap);
		if (!CollectionUtils.isEmpty(cnlabApplyVoNotDone)) {
			DataSourceRoute.putAppId("LIS_bayy");
			try {
				labTripartiteSystemVos = adtPubMapper.getLabTripartiteSystemVos(cnlabApplyVoNotDone);
			} catch (Exception e) {
				logger.info("出院查询三方检验报错" + e.getMessage(), cnlabApplyVoNotDone);
			} finally {
				DataSourceRoute.putAppId("default");
			}
		}
		//处理检查
		List<CnRisApplyBaVo> cnRisApplyVoNotDone = zsbaAdtService.getCnRisApplyVoNotDone(paramMap);
		if (!CollectionUtils.isEmpty(cnRisApplyVoNotDone)) {
			DataSourceRoute.putAppId("baPacs");
			try {
				risTripartiteSystemVos = adtPubMapper.getRisTripartiteSystemVos(cnRisApplyVoNotDone);
			} catch (Exception e) {
				logger.info("出院查询三方检查报错" + e.getMessage(), cnRisApplyVoNotDone);
			} finally {
				DataSourceRoute.putAppId("default");
			}
		}
		zsbaAdtService.updateEustus(labTripartiteSystemVos, cnlabApplyVoNotDone, risTripartiteSystemVos, cnRisApplyVoNotDone,user1);
	}

	/**
	 * 7.从 his 系统中同步 患者编码 至 nhis系统
	 * @param param
	 * @param user
	 * @return
	 */
	public void updateCodePiFromHis(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if ((null == paramMap.get("pkPi") || CommonUtils.isEmptyString(paramMap.get("pkPi").toString()))) {
			throw new BusException("pkPi为空，请确认是否选择了待同步的患者！");
		}
		if ((null == paramMap.get("codeIp") || CommonUtils.isEmptyString(paramMap.get("codeIp").toString()))) {
			throw new BusException("codeIp为空，请确认是否选择了待同步的患者！");
		}
		DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
		Map<String, Object> paramO = new HashMap<String, Object>();
		paramO.put("codeIp", paramMap.get("codeIp"));
		List<Map<String,Object>> listFromHis = zsbaAdtService.getHisInPatientInfo(paramO);//获取his系统中的微信list
		if(null == listFromHis || listFromHis.size() < 1)
			throw new BusException("未获取到旧系统患者主索引信息！");
		else if(listFromHis.size() > 1)
			throw new BusException("住院号："+paramMap.get("codeIp")+" 在旧系统中存在"+listFromHis.size()+"条患者主索引信息！");
		DataSourceRoute.putAppId("default");//切换到 NHIS
		paramMap.put("codePi", listFromHis.get(0).get("patientId"));
		zsbaAdtService.updatePiCodePiFromHis(paramMap);
	}

	/**
	 * 8.从 nhis 系统中同步 患者姓名 至 his系统
	 * @param param
	 * @param user
	 * @return
	 */
	public void updateNamePiFromNhis(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if ((null == paramMap.get("namePi") || CommonUtils.isEmptyString(paramMap.get("namePi").toString()))) {
			throw new BusException("namePi为空，请确认是否选择了待同步的患者！");
		}
		if ((null == paramMap.get("codeIp") || CommonUtils.isEmptyString(paramMap.get("codeIp").toString()))) {
			throw new BusException("codeIp为空，请确认是否选择了待同步的患者！");
		}
		DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
		Map<String, Object> paramO = new HashMap<String, Object>();
		zsbaAdtService.updatePiNamePiFromNhis(paramMap);
	}

	/**
	 * 获取 新/旧 his系统患者主索引数据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPiListByCon(String param, IUser user){
		List<Map<String, Object>> list = getPiListByConDetail(param);
		return list;
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getPiListByConDetail(String param) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		//查询新系统-患者索引信息
		DataSourceRoute.putAppId("default");//切换到 HIS
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list = zsbaAdtService.getPiListFromNhis(paramMap);
		if(null == list || list.size() < 1)
			list = new ArrayList<Map<String,Object>>();
		
		//查询旧系统-患者索引信息
		DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
		List<Map<String,Object>> nlist = zsbaAdtService.getPiListFromHis(paramMap);
		if(null != nlist && nlist.size() > 0)
			list.addAll(nlist);
		return list;
	}
	
	/**
	 * 保存患者基本信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> savePiInfo(String param, IUser user){
		Map<String,Object> piParam = JsonUtil.readValue(param, Map.class);
		PiMasterBa pi = JsonUtil.readValue(param, PiMasterBa.class);
		PiMaster pim = JsonUtil.readValue(param, PiMaster.class);
		return savePiinfoDetail(piParam,pi,pim,true);
	}

	/**
	 * 保存患者实际操作
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> savePiinfoDetail(Map<String,Object> piParam,PiMasterBa pi,PiMaster pim,boolean flagSys) {
		if(null == pi)
			throw new BusException("未获取待保存的患者信息！");
		
		boolean isNew = false;
		//1、新增 - 保存时获取患者编码、患者门诊号
		if(CommonUtils.isEmptyString(pi.getPkPi()))
		{
			if(CommonUtils.isEmptyString(pi.getCodeOp()))
			{
				String codeOp = piCodeFactory.getHandler().genCodePi(new PiMaster());
				pi.setCodeOp(codeOp);
				pi.setCodePi(pi.getCodeOp());
				isNew = true;
			}
			else{
				//2、切换到his，查询患者的住院号信息
				DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
				Map<String,Object> qryCodePa = new HashMap<String,Object>();
				qryCodePa.put("codeOp", pi.getCodeOp());
				Map<String,Object> pa = zsbaAdtService.getHisCodePiAndIp(qryCodePa);
				if(null != pa && !CommonUtils.isEmptyString(CommonUtils.getString(pa.get("inpatientNo"), ""))){
					pi.setCodeIp(CommonUtils.getString(pa.get("inpatientNo"), ""));
				}
				if(null != pa && !CommonUtils.isEmptyString(CommonUtils.getString(pa.get("patientId"), ""))){
					pi.setCodePi(CommonUtils.getString(pa.get("patientId"), ""));
				}
				if(CommonUtils.isEmptyString(pi.getCodePi())){
					pi.setCodePi(pi.getCodeOp());
				}
			}
			
			if(CommonUtils.isEmptyString(pi.getCodePi())){
				pi.setCodePi(pi.getCodeOp());
			}
		}
		
		//2、保存患者基本信息
		DataSourceRoute.putAppId("default");//切换到 HIS
		zsbaAdtService.savePiInfo(pi , pim , piParam);
		
		Map<String,Object> par = new HashMap<String,Object>();
		if(flagSys){
			if(!isNew){
				//3.1、回写旧住院系统导入标识
				DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
				par.put("codeOp", pi.getCodeOp());
				par.put("name", pi.getNamePi());
				par.put("dtSex", "02".equals(pi.getDtSex()) ? "1" : ("03".equals(pi.getDtSex()) ? "2" : "9"));
				par.put("birthday", pi.getBirthDate());
				zsbaAdtService.updateHisInsertFlag(par);
			}else{
				//3.2、回写旧住院系统 - 患者主索引信息
				DataSourceRoute.putAppId("HIS_bayy");//切换到 HIS
				zsbaAdtService.savePiInfoToHis(pi);
			}
		}
		
		//4、查询返回患者基本信息
		par = new HashMap<String,Object>();
		if("01".equals(pi.getDtIdtype()))
			par.put("idNo", pi.getIdNo());
		else
			par.put("codePi", pi.getCodePi());
		String partxt = JSONUtils.toJSONString(par);
		
		List<Map<String,Object>> list = getPiListByConDetail(partxt);
		return list;
	}
	
	/**
	 * 调用电子健康卡查询干部级别
	 * 交易号: 022003021008
	 * @param param
	 * @param user
	 * @return
	 */
	public String geteHealthCadreLevel(String param, IUser user) {
		PiMaster piMaster = JsonUtil.readValue(param, PiMaster.class);
		Map<String, String> hicInfo = zsbaAdtService.geteHealthCadreLevel(piMaster);
		return MapUtils.getString(hicInfo, "cadrelevel","");
	} 
	
}
