package com.zebone.nhis.cn.ipdw.service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.PatiInfoListMapper;
import com.zebone.nhis.cn.ipdw.vo.ExVtsOccVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.service.EmrPubService;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.Application;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class PatiInfoListService {
	@Autowired
	private PatiInfoListMapper patiInfoListMapper;
	@Autowired
	private EmrPubService emrPubService;
	
	/**
	 * 获取病人医嘱列表
	 * @param pkpv
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<CnOrder> getOrderList(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		//String pkpv = JsonUtil.getFieldValue(param, "pkPv"); 
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkpv=String.valueOf(paramMap.get("pkPv"));
		String ifHistory=String.valueOf(paramMap.get("ifHistory"));
		if(StringUtils.isBlank(pkpv)) throw new BusException("前台传的患者编码(pkpv)为空!");
		Date now = new Date();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String sd = sdf.format(now);
		
		 
		 Map<String,Object> m = new HashMap<String,Object>();
		 m.put("beginDate", sd);
		 String pvPrefix = pkpv.substring(0, 1);
		 if(",".equals(pvPrefix)){
			m.put("EncHistoryOrd", "EncHistoryOrd");
			pkpv = pkpv.substring(1);
		 }
		 m.put("pkPv", pkpv);
		 List<Map<String,Object>> ret = new ArrayList<Map<String,Object>>();
		if(!StringUtils.isEmpty(ifHistory) && "1".equals(ifHistory)){//查询带转存的资料
			ret = patiInfoListMapper.qryCnOrderAndDump(m);
		}else{
			ret = patiInfoListMapper.qryCnOrder(m);
		}
		List<CnOrder> cos = new ArrayList<CnOrder>();
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		for(Map map : ret){
			CnOrder co = new CnOrder();
			BeanUtils.copyProperties(co, map);
			
			co.setCreator((String)map.get("deptPhyName")); //开立科室
			co.setModifier((String)map.get("nameDeptExec")); //执行科室
	
			cos.add(co);
		}
		return cos;
	}
	
	/**
	 * 获取病人诊断列表
	 * @param pkpv
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<PvDiag> getDiagList(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		String pkpv = JsonUtil.getFieldValue(param, "pkPv");                              
		if(StringUtils.isBlank(pkpv)) throw new BusException("前台传的患者编码(pkpv)为空!");
				 
		List<Map<String,Object>> ret = patiInfoListMapper.qryCnDiag(pkpv);
		List<PvDiag> pds = new ArrayList<PvDiag>();
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		for(Map map : ret){
			PvDiag pd = new PvDiag();
			BeanUtils.copyProperties(pd, map);
			
			pd.setCreator((String)map.get("diagtypeName"));
			pds.add(pd);
		}
		return pds;
	}
	
	/**
	 * 获取病人体征列表
	 * @param pkpv
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<Map<String,Object>> getOccList(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		String pkpv = JsonUtil.getFieldValue(param, "pkPv"); 
		if(StringUtils.isBlank(pkpv)) throw new BusException("前台传的患者编码(pkpv)为空!");
		List<Map<String,Object>> ret = new ArrayList<Map<String,Object>>();
		if(Application.isSqlServer()){
			 ret= patiInfoListMapper.qryCnOcc(pkpv);
		}else{
			ret= qryCnOccByOrcl(pkpv);
		}
	
//		List<ExVtsOccVo> evs = new ArrayList<ExVtsOccVo>();
//		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
//		
//		for(Map map : ret){
//			ExVtsOccVo evt = new ExVtsOccVo();
//			BeanUtils.copyProperties(evt, map);
//			
//			if(null != evt.getDateVts()) evt.pkOrg = DateUtils.dateToStr("yyyy-MM-dd",evt.getDateVts());
//			evs.add(evt);
//		}
		return ret;
	}
	
	private List<Map<String, Object>> qryCnOccByOrcl(String pkpv) {
		String sql = "select '体重' as val_st,occ.val_weight,occ.date_vts,'KG' as val_al  from ex_vts_occ occ where occ.pk_pv=?"+ 
	                " union all select '收缩压' as val_st,to_char(occ.val_sbp) as val_weight ,occ.date_vts,'mmHG' as val_al  from ex_vts_occ occ where occ.pk_pv=?"+
	                " union all select '舒张压' as val_st,to_char(occ.val_dbp) as val_weight ,occ.date_vts,'mmHG' as val_al  from ex_vts_occ occ where occ.pk_pv=?  order by date_vts desc";
		List<Map<String,Object>> rtn = DataBaseHelper.queryForList(sql, new Object[]{pkpv,pkpv,pkpv}); 
		return rtn;
	}

	/**
	 * 获取病人检查列表
	 * @param pkpv
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<ExRisOcc> getOccRisList(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		String pkpv = JsonUtil.getFieldValue(param, "pkPv"); 
		if(StringUtils.isBlank(pkpv)) throw new BusException("前台传的患者编码(pkpv)为空!");
				 
		List<Map<String,Object>> ret = patiInfoListMapper.qryCnRisOcc(pkpv);
		List<ExRisOcc> ers = new ArrayList<ExRisOcc>();
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		for(Map map : ret){
			ExRisOcc ero = new ExRisOcc();
			BeanUtils.copyProperties(ero, map);
			ers.add(ero);
		}
		return ers;
	}
	
	/**
	 * 获取病人检验列表
	 * @param pkpv
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<ExLabOcc> getOccLabList(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		String pkpv = JsonUtil.getFieldValue(param, "pkPv"); 
		if(StringUtils.isBlank(pkpv)) throw new BusException("前台传的患者编码(pkpv)为空!");
				 
		List<Map<String,Object>> ret = patiInfoListMapper.qryCnLabOcc(pkpv);
		List<ExLabOcc> els = new ArrayList<ExLabOcc>();
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		for(Map map : ret){
			ExLabOcc elo = new ExLabOcc();
			BeanUtils.copyProperties(elo, map);
			
			String val = elo.getVal();
			if(StringUtils.isNoneBlank(elo.getVal1())){
				if(StringUtils.isEmpty(val)) val = elo.getVal1();
				else val += ","+elo.getVal1();
			}
			
			if(StringUtils.isNoneBlank(elo.getVal2())){
				if(StringUtils.isEmpty(val)) val = elo.getVal2();
				else val += ","+elo.getVal2();
			}
			
			if(StringUtils.isNoneBlank(elo.getVal3())){
				if(StringUtils.isEmpty(val)) val = elo.getVal3();
				else val += ","+elo.getVal3();
			}
			
			if(StringUtils.isNoneBlank(elo.getVal4())){
				if(StringUtils.isEmpty(val)) val = elo.getVal4();
				else val += ","+elo.getVal4();
			}
			
			elo.setVal(val);
			els.add(elo);
		}
		return els;
	}	
	
	/**
	 * 获取病人病程
	 * @param pkpv
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public String getPatiEmr(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		String pkpv = JsonUtil.getFieldValue(param, "pkPv"); 
		if(StringUtils.isBlank(pkpv)) throw new BusException("前台传的患者编码(pkpv)为空!");		
				 
		return emrPubService.getPatEmrParaText(pkpv, "1000", null);
	}

	public List<Map<String,Object>> qryInfor(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		String pkDept=JsonUtil.getFieldValue(param, "pkDept");
		if(StringUtils.isBlank(pkDept)) throw new BusException("前台传的科室编码(pkDept)为空!");

		return patiInfoListMapper.qryInfor(pkDept);
	}



}
