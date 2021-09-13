package com.zebone.nhis.scm.ipdedrug.service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.scm.ipdedrug.dao.IpDeDrugMapper;
import com.zebone.nhis.scm.ipdedrug.vo.DrugApDeptVo;
import com.zebone.nhis.scm.ipdedrug.vo.IpDeDrugDto;
import com.zebone.nhis.scm.pub.service.IpDeDrugPubService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * 医嘱发退药服务（合并发退药）
 * @author yangxue
 *
 */
@Service
public class IpPdDeAndRtnDrugService {
	@Autowired
	private IpDeDrugMapper ipDeDrugMapper;
	
	@Autowired
	private IpDeDrugPubService ipDeDrugPubService;
	
	@Autowired
	private IpPdReBackDrugService ipPdReBackDrugService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> queryApDrugList(String param, IUser user) {
		IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
		User userCur = (User) user;
		StringBuffer sql = new StringBuffer();
		sql.append("select ap.pk_pdap, ap.date_ap,ap.eu_direct,ap.pk_dept_ap,dept.name_dept from bd_ou_dept dept");
		sql.append(" inner join ex_pd_apply ap on dept.pk_dept=ap.pk_dept_ap");
		sql.append(" where ap.pk_dept_de=? ");
		sql.append("  and ap.date_ap>=? and ap.date_ap<=? and ap.flag_finish='0' and ap.flag_cancel='0' ");
		List args = new ArrayList();
		args.add(userCur.getPkDept());
		args.add(DateUtils.getDateMorning(ipDeDrugDto.getDateStart(), 0));
		args.add(DateUtils.getDateMorning(ipDeDrugDto.getDateEnd(), 1));
		if (ipDeDrugDto.getPkAppDeptNs() != null && ipDeDrugDto.getPkAppDeptNs().trim().length() > 0) {
			sql.append(" and ap.pk_dept_ap=? ");
			args.add(ipDeDrugDto.getPkAppDeptNs());
		}
		sql.append("and ap.eu_aptype= '0'");
		sql.append(" and exists (select 1 from ex_pd_apply_detail dt where ap.pk_pdap=dt.pk_pdap and dt.flag_finish='0' and (((dt.pk_pres is null or dt.pk_pres='') and ap.eu_direct=1) or (ap.eu_direct=-1 ))  ");
		if(CommonUtils.isNotNull(ipDeDrugDto.getDeCateWhereSql())){
			if(!"#".equals(ipDeDrugDto.getDeCateWhereSql().substring(0, 1))){//涓轰簡鍏煎澶氶�
				ipDeDrugDto.setDeCateWhereSql("#"+ipDeDrugDto.getDeCateWhereSql());
			}
			String wheresql[] = ipDeDrugDto.getDeCateWhereSql().substring(1, ipDeDrugDto.getDeCateWhereSql().length()).split("#");
			if(wheresql!=null&&wheresql.length>0){
				sql.append(" and ( ");
				for(int i=0;i<wheresql.length;i++){
					if(i==0){
						sql.append("(").append(wheresql[i]).append(")");
					}else{
						sql.append(" or ").append("(").append(wheresql[i]).append(")");
					}
				}
				sql.append(")");
			}
		}
		sql.append(")");
		sql.append(" order by ap.pk_dept_ap,ap.date_ap,ap.eu_direct");
		List<Map<String, Object>> mapResult = DataBaseHelper.queryForList(sql.toString(), args.toArray());
		return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
	}
	
	/**
	 * 008004003004
	 * 查询医嘱发退药申请单-按科室汇总
	 * @param param
	 * @param user
	 * @return
	 */
	public List<DrugApDeptVo> queryApDrugListSumDept(String param, IUser user) {
		IpDeDrugDto ipDeDrugDto = JsonUtil.readValue(param, IpDeDrugDto.class);
		User userCur = (User) user;
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("pkDeptDe", userCur.getPkDept());
		if(ipDeDrugDto.getDateStart()!=null){
			paramMap.put("dateStart", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss",DateUtils.getDateMorning(ipDeDrugDto.getDateStart(), 0)));
		}
		if(ipDeDrugDto.getDateEnd()!=null)
		{
			paramMap.put("dateEnd", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.getDateMorning(ipDeDrugDto.getDateEnd(), 1)));
		}	
		paramMap.put("pkDeptAp", ipDeDrugDto.getPkAppDeptNs());
		paramMap.put("flagDept", ipDeDrugDto.getFlagDept());
		paramMap.put("isPres", ipDeDrugDto.getIsPres());
		paramMap.put("deCateWhereSql", ipDeDrugDto.getDeCateWhereSql());
		List<DrugApDeptVo> mapResult =ipDeDrugMapper.queryAllApplyDrugList(paramMap);
		Map<String,DrugApDeptVo> creatMap=new HashMap<>();
		for (DrugApDeptVo resvo : mapResult) {
			if(creatMap.containsKey(resvo.getPkDeptAp())){
				DrugApDeptVo tempvo=creatMap.get(resvo.getPkDeptAp());
				if(tempvo.getDateAp().getTime()<resvo.getDateAp().getTime()){
					creatMap.put(resvo.getPkDeptAp(), resvo);
				}
			}else
			{
				creatMap.put(resvo.getPkDeptAp(), resvo);
			}
		}
		List<DrugApDeptVo> resList=new ArrayList<>();
		for (DrugApDeptVo tempvo : creatMap.values()) {
			resList.add(tempvo);
		}
		return  resList;
	}
	/**
	 * 查询请退领明细
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> queryApDrugDetail (String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null) throw new BusException("未获取到请领单主键");
		List<Map<String, Object>> mapResult = new ArrayList<Map<String, Object>>();
		User userCur = (User) user;
		if(!CommonUtils.isNull(paramMap.get("apObj"))){
			Map<String,Object> apMap = (Map<String,Object>)paramMap.get("apObj");
			if(apMap.get("pkPdaps")!=null&&((ArrayList)apMap.get("pkPdaps")).size()>0){
			IpDeDrugDto apDto = new IpDeDrugDto();
			apDto.setPkPdaps((String[])((ArrayList)apMap.get("pkPdaps")).toArray(new String[0]));
			apDto.setIsPres("0");
			apDto.setPkPdStock(userCur.getPkStore());
			apDto.setPkOrg(userCur.getPkOrg());
			//支持多选,但不建议，循环查询会影响效率
			String wheresql = (String)apMap.get("deCateWhereSql");
			if(!CommonUtils.isEmptyString(wheresql)){
				delAndRtnMany(apDto,apMap,mapResult,true);
			}else{
				apDto.setDeCateWhereSql((String)apMap.get("deCateWhereSql"));
				apDto.setPkPddecate((String)apMap.get("pkPddecate"));
				apDto.setNameDecate((String)apMap.get("nameDecate"));
				apDto.setCodeDecate((String)apMap.get("codeDecate"));
				//查询发药明细
				List<Map<String, Object>> dt = ipDeDrugMapper.qryAppListDetail(apDto);
				if(dt!=null&&dt.size()>0) mapResult.addAll(dt);
				}
			}
		}
		if(!CommonUtils.isNull(paramMap.get("rtnObj"))){
			Map<String,Object> rtnMap = (Map<String,Object>)paramMap.get("rtnObj");
			if(rtnMap.get("pkPdaps")!=null&&((ArrayList)rtnMap.get("pkPdaps")).size()>0){
			IpDeDrugDto rtnDto = new IpDeDrugDto();
			rtnDto.setPkPdaps((String[])((ArrayList)rtnMap.get("pkPdaps")).toArray(new String[0]));
			rtnDto.setIsPres("0");
			rtnDto.setPkPdStock(userCur.getPkStore());
			rtnDto.setPkOrg(userCur.getPkOrg());
			
			String[] pkarr = rtnMap.get("pkPddecate").toString().split("#");
			String[] namearr = rtnMap.get("nameDecate").toString().split("#");
			String[] codearr = rtnMap.get("codeDecate").toString().split("#");
			if(pkarr!=null&&pkarr.length>0){
				rtnDto.setPkPddecate(pkarr[0]);
			}
			if(namearr!=null&&namearr.length>0){
				rtnDto.setNameDecate(namearr[0]);
			}	
			if(codearr!=null&&codearr.length>0){
				rtnDto.setCodeDecate(codearr[0]);
			}
				//查询退药明细
				List<Map<String, Object>> rtndt = ipDeDrugMapper.qryIpReBackDrugDetailByCDT(rtnDto);
				if(rtndt!=null&&rtndt.size()>0) mapResult.addAll(rtndt);
			}
			
		}
		return mapResult == null ? new ArrayList<Map<String, Object>>() : mapResult;
	}
	
	/**
	 * 008004003005
	 * 查询请退领明细
	 * @return
	 */
	public List<Map<String,Object>> queryApDrugDetailAll (String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null)return null;
		User userCur = (User) user;
		paramMap.put("pkPdStock", userCur.getPkStore());
		paramMap.put("pkOrg", userCur.getPkOrg());
		paramMap.put("pkDept", userCur.getPkDept());
		List<Map<String,Object>> resList=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> drugList=ipDeDrugMapper.queryApDtDrugList(paramMap);//发药
		List<Map<String,Object>> rtnDrugList=ipDeDrugMapper.queryApDtretDrugList(paramMap);//退药
		if(drugList!=null && drugList.size()>0){
			resList.addAll(drugList);
		}
		if(rtnDrugList!=null && rtnDrugList.size()>0){
			resList.addAll(rtnDrugList);
		}
		return resList;
	}
	
	/**
	 * 住院发退药处理
	 * @param param
	 * @param user
	 */
	public String deliAndReturn(String param, IUser user) {
		// 前台传过来是勾选的请领明细
		List<ExPdApplyDetail> exPdAppDetails = JsonUtil.readValue(param, new TypeReference<List<ExPdApplyDetail>>() {
		});
		if(exPdAppDetails==null||exPdAppDetails.size()<=0)
			throw new BusException("未获取到需要发退药的明细信息！");
		String codeDe= ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_IPDS);
		Date dateDe = new Date();
		//发药明细
		List<ExPdApplyDetail> aplist = new ArrayList<ExPdApplyDetail>();
		//退药明细
		List<ExPdApplyDetail> delist = new ArrayList<ExPdApplyDetail>();
		for(ExPdApplyDetail dt:exPdAppDetails){
			if(dt.getQuanPack()>=0){
				aplist.add(dt);
			}else if(dt.getQuanPack()<0){
				delist.add(dt);
			}
		}
		
		if(aplist!=null&&aplist.size()>0){
			 ipDeDrugPubService.mergeIpDeDrug(aplist, "0", "1",codeDe,dateDe);
		}
		if(delist!=null&&delist.size()>0){
			ipPdReBackDrugService.mergeIpReBackDrug(delist, codeDe,dateDe,(User)user);
		}
		return codeDe;
	}
	
	/**
	 * 多类型同时发放
	 * @return
	 */
	private void delAndRtnMany(IpDeDrugDto dto,Map<String,Object> paramMap,List<Map<String, Object>> mapResult,boolean ap){
		String wheresql = (String)paramMap.get("deCateWhereSql");
		String[] sqlarr = wheresql.split("#");
		String[] pkarr = paramMap.get("pkPddecate").toString().split("#");
		String[] namearr = paramMap.get("nameDecate").toString().split("#");
		String[] codearr = paramMap.get("codeDecate")==null?null:paramMap.get("codeDecate").toString().split("#");
		if(sqlarr!=null&&sqlarr.length>0){
			for(int i=0;i<sqlarr.length;i++){
				dto.setDeCateWhereSql(sqlarr[i]);
				dto.setPkPddecate(pkarr[i]);
				dto.setNameDecate(namearr[i]);
				if(codearr!=null&&codearr.length>=sqlarr.length){
					dto.setCodeDecate(codearr[i]);
				}
				List<Map<String, Object>> dt = new ArrayList<Map<String,Object>>();
				if(ap){
					dt = ipDeDrugMapper.qryAppListDetail(dto);
				}else{
					dt = ipDeDrugMapper.qryIpReBackDrugDetailByCDT(dto);
				}
				if(dt!=null&&dt.size()>0) mapResult.addAll(dt);
			}
		}
	}
}
 