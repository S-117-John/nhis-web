package com.zebone.nhis.cn.ipdw.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
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
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnIpPressAuthMapper;
import com.zebone.nhis.cn.ipdw.dao.CnIpPressMapper;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.CnIpPressVO;
import com.zebone.nhis.cn.ipdw.vo.CnOrderVO;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.ex.pub.support.CalOrderPdQuanService;
import com.zebone.nhis.ex.pub.support.OrderFreqCalCountHandler;
import com.zebone.nhis.ex.pub.vo.GenerateExLisOrdVo;
import com.zebone.nhis.ex.pub.vo.OrderAppExecVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnIpPressService {
	
	@Autowired
	private CnIpPressMapper cnIpPressMapper;
	
	@Autowired      
	private BdSnService bdSnService; 
	
	@Autowired
	private CalOrderPdQuanService calOrderPdQuanService;
	
	@Autowired
	private CnIpPressAuthMapper cnIpPressAuthMapper;
	
	@Autowired
	private CnPubService cnPubService;
	
	public List<CnOrderVO> queryOrders(String param,IUser user) throws IllegalAccessException, InvocationTargetException, ParseException{
		String 	pressId = JsonUtil.getFieldValue(param, "pressId");			
		if(StringUtils.isBlank(pressId)) throw new BusException("前台传的患者编码(pressId)为空!");	
				
		return queryOrdersExec(pressId,param,user);		
	}
	
	/**
	 * 
	 * @param pid press id
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	private List<CnOrderVO> queryOrdersExec(String pid,String param,IUser user) throws IllegalAccessException, InvocationTargetException, ParseException{
		if(StringUtils.isBlank(pid)) throw new BusException("前台传的患者编码(pressId)为空!");
		
		String sql = "select o.*, case when p.VOL IS null then p.WEIGHT else p.vol end as vol,p.pk_unit_pack, stk.quan_min, "
				   + " case when o.ordsn = o.ordsn_parent then o.date_start else null end as date_start1, supply.flag_pivas  from cn_order o inner join bd_pd p on o.PK_ORD=p.PK_PD "
				   +" left outer join (select sum(quan_min - quan_prep) quan_min, pk_pd "
                   +"  from pd_stock "
                   +"  where del_flag = '0' "
                   +"  group by pk_pd) stk on o.pk_ord=stk.pk_pd  inner join bd_supply supply on supply.code = o.code_supply and supply.del_flag='0'  "
				   + " where o.pk_pres = '"+pid+"' "
				   + " and (o.del_flag is null or o.del_flag <> '1')"
                   +" order by o.ordsn";
		
		List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		List<CnOrderVO> ret = new ArrayList<CnOrderVO>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		User u = (User)user;
				
		for(Map m : ps){
			CnOrderVO co = new CnOrderVO();
			BeanUtils.copyProperties(co, m);
			
			String store = getCurrentStore(param,user);
			
			Map mm = new HashMap();
			mm.put("pkOrd", co.getPkOrd());
			mm.put("pkStore", store);
			
			/*Integer quanMin = cnIpPressMapper.qryStoreQuanMin(mm);
			co.setQuanMin(quanMin);	*/
			
			String da = m.get("dateStart1") == null ? null : m.get("dateStart1").toString();
			if(!StringUtils.isEmpty(da)){
				co.setDateStart(sdf.parse(da));
			}else co.setDateStart(null);			
			
			ret.add(co);
		}
		
		return ret;		
	}	
	
	public List<CnIpPressVO> queryPress(String param,IUser user) throws IllegalAccessException, InvocationTargetException, ParseException{
		String pv = JsonUtil.getFieldValue(param, "pv");	
		
		if(StringUtils.isBlank(pv)) throw new BusException("前台传的患者编码(pkpv)为空!");
		
		String sql = "select distinct p.* from cn_prescription p  inner join cn_order o"
                     +" on o.pk_pres = p.pk_pres where p.pk_pv ='"+pv+"' and (p.del_flag is null or p.del_flag <> '1')"
                     +" and o.code_ordtype <> '0103'"
                     +" order by date_pres desc"; 
		List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		List<CnIpPressVO> ret = new ArrayList<CnIpPressVO>();
		
		for(Map m : ps){
			CnIpPressVO cp = new CnIpPressVO();
			BeanUtils.copyProperties(cp, m);
			
			cp.setOrds(queryOrdersExec(cp.getPkPres(),param,user));
			ret.add(cp);
		}
		
		return ret;		
	}
	
	/**
	 * 查询当前内容
	 * 004004009028
	 * @param param
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException
	 */
	public List<CnIpPressVO> queryPressSing(String param,IUser user) throws IllegalAccessException, InvocationTargetException, ParseException{
		String pres = JsonUtil.getFieldValue(param, "pres");	
		
		if(StringUtils.isBlank(pres)) throw new BusException("前台传的患者编码(pres)为空!");
		
		String sql = "select distinct p.* from cn_prescription p  inner join cn_order o"
                     +" on o.pk_pres = p.pk_pres where p.PK_PRES ='"+pres+"' and (p.del_flag is null or p.del_flag <> '1')"
                     +" and o.code_ordtype <> '0103'"
                     +" order by date_pres desc"; 
		List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		List<CnIpPressVO> ret = new ArrayList<CnIpPressVO>();
		
		for(Map m : ps){
			CnIpPressVO cp = new CnIpPressVO();
			BeanUtils.copyProperties(cp, m);
			
			cp.setOrds(queryOrdersExec(cp.getPkPres(),param,user));
			ret.add(cp);
		}
		
		return ret;		
	}
	
	
	private void fillCnOrder(CnOrder co,IUser user){
		co.setEuAlways("1");
		if(StringUtils.isEmpty(co.getEuStatusOrd())) co.setEuStatusOrd("0");
		
		
		User u = (User)user;
		String deptExec = null;
		String pkOrgExec = null;
		
		Map para = new HashMap();
		para.put("code", "02");
		//para.put("pkDept", "72eb098f8b1e4e68b88c43fcdc37a694"); //TODO 测试硬编码
		para.put("pkDept", u.getPkDept());
        List<Map<String,Object>> depts = cnIpPressMapper.qryDeptExec(para);
		
		if(null != depts && depts.size() > 0){
			Map<String,Object> m = depts.get(0);
			deptExec = (String)m.get("pkDept");
			pkOrgExec = (String)m.get("pkOrg");
		}
		
		co.setPkDept(u.getPkDept());
		if(StringUtils.isBlank(co.getPkCnord())){
			co.setPkEmpInput(u.getPkEmp());
			co.setNameEmpInput(u.getNameEmp());
		}
		co.setEuPvtype("3");
		co.setFlagDurg("1");
		if(null == co.getOrdsn() || co.getOrdsn().intValue() == 0){
			co.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, u));
			co.setOrdsnParent(co.getOrdsn());
		}
		if(null == co.getDateEnter()) co.setDateEnter(new Date());
		if(null == co.getDateStart()) co.setDateStart(new Date());
		if(null == co.getDateEffe())  co.setDateEffe(new Date());
		
//		co.setPkEmpOrd(u.getPkEmp());
		co.setDescOrd(co.getNameOrd());
		co.setPkDeptExec(deptExec);
		co.setPkOrgExec(pkOrgExec);
		co.setFlagDoctor("1");
		co.setPkUnitCg(co.getPkUnit());		
		
//		co.setNameEmpOrd(u.getUserName()); //签署时写入
//		co.setPkEmpOrd(u.getPkEmp());
		
		co.setFlagBase("0");
		co.setFlagBl("1");
		co.setFlagCp("0");
		co.setFlagDoctor("1");
		co.setFlagEmer("0");
		co.setFlagFirst("0");
		co.setFlagFit("0");
		co.setFlagNote("1");
		co.setFlagPrev("0");
		co.setFlagPrint("0");
		co.setFlagSelf("0");
		co.setFlagStop("0");
		co.setFlagThera("0");	
		co.setFlagErase("0");
		co.setFlagStopChk("0");
		co.setFlagEraseChk("0");		
	}
	
	public void saveOrUpdatePress(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnIpPressVO> preses = JsonUtil.readValue(param,new TypeReference<List<CnIpPressVO>>() {});
		User u = (User)user;
		
		if(null == preses || preses.size() <= 0) return;
		vaildOrdts(preses);
		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		Date outHosOrdDate = getPressDate(preses);
		for(CnIpPressVO cip : preses){
			CnPrescription pres = new CnPrescription();
			BeanUtils.copyProperties(pres, cip);
			
			if(StringUtils.isEmpty(pres.delFlag)) pres.setDelFlag("0");
			Date dPress = getCnOrdDateStart(cip.getDatePres(),outHosOrdDate);//补充:医嘱的开始日期同处方日期，判断是否存在出院类医嘱，如果存在，判断处方日期不能大于出院类医嘱开立时间，如果大于出院医嘱时间，将时间修改为出院医嘱时间，否则取当前时间；
			pres.setDatePres(dPress);
			if(StringUtils.isBlank(cip.getPkPres())){
				pres.setPkDept(u.getPkDept());
				pres.setNameEmpOrd(u.getUserName());
				pres.setPkEmpOrd(u.getPkDept());
				DataBaseHelper.insertBean(pres);
			}
			else DataBaseHelper.updateBeanByPk(pres,false);
			
			List<CnOrderVO> ords = cip.getOrds();
			
			if(null == ords || ords.size() <= 0) continue;
			
			for(CnOrderVO co : ords){
				CnOrder cco = new CnOrder();
				
				BeanUtils.copyProperties(cco, co);
				//cco.setDateStart(new Date());
				
				if(!StringUtils.isEmpty(co.getFlagSign()) && "1".equals(co.getFlagSign())) continue;
				if(!StringUtils.isEmpty(co.getEuStatusOrd()) && !"0".equals(co.getEuStatusOrd())) continue;
					
				cco.setPkPres(pres.getPkPres());
				double qcg = cco.getQuan();
				cco.setQuanCg(qcg);				//把总量赋值给quan_cg字段
				fillCnOrder(cco,user);
				cco.setTs(new Date());
				cco.setDateStart(pres.getDatePres());//处方下的医嘱时间同处方日期一致
				if(StringUtils.isBlank(cco.getPkCnord())) DataBaseHelper.insertBean(cco);
				else DataBaseHelper.updateBeanByPk(cco,false);
			}			
		}
	}

	private Date getPressDate(List<CnIpPressVO> preses) {
		List<Map<String,Object>> outHosOrdDateL =  DataBaseHelper.queryForList("select date_start from cn_order where pk_pv = ? and code_ordtype='1102' and del_flag='0' and flag_erase='0'  order by date_enter desc ",new Object[]{preses.get(0).getOrds().get(0).getPkPv()});
		Date outHosOrdDate = null;
		if(outHosOrdDateL!=null && outHosOrdDateL.size()>0) outHosOrdDate = (Date) outHosOrdDateL.get(0).get("dateStart");
		return outHosOrdDate;
	}
	
	private Date getCnOrdDateStart(Date datePres, Date outHosOrdDate) {
		 Date presDate = datePres==null?new Date():datePres;
		 if(outHosOrdDate!=null){
			 if(presDate.after(outHosOrdDate)){
				 presDate = outHosOrdDate;
			 }
		 }
		 return presDate;
	}

	private void vaildOrdts(List<CnIpPressVO> preses) {
		List<CnOrder> list = new ArrayList<CnOrder>();
		for(CnIpPressVO cip : preses){		
			List<CnOrderVO> ords = cip.getOrds();
			for(CnOrderVO co : ords){
				if(!StringUtils.isEmpty(co.getFlagSign()) && "1".equals(co.getFlagSign())) continue;
				if(!StringUtils.isEmpty(co.getEuStatusOrd()) && !"0".equals(co.getEuStatusOrd())) continue;
				if(!StringUtils.isBlank(co.getPkCnord())){
					list.add(co);
				}
			}
		}
		if(list.size()>0) cnPubService.vaildUpdateCnOrdts(list);
		
	}

	/**
	 * 获取当前科室对应的仓库
	 * @param param
	 * @param user
	 * @return
	 */
	public String getCurrentStore(String param,IUser user){
		User u = (User)user;		
		String curr_dept = u.getPkDept();		
		if(StringUtils.isEmpty(curr_dept)) return null;
		
		String store = null;
		String dept = null;
		List<Map<String,Object>> ret = cnIpPressMapper.qryDrugStore(curr_dept);
		
		if(null == ret || ret.size()!=1) {
			Map<String,Object> deptMap =DataBaseHelper.queryForMap("select name_dept from bd_ou_dept where pk_dept=?", ((User)user).getPkDept());
			throw new BusException("请维护好临床科室["+deptMap.get("nameDept")+"]与药房的业务线！");
		}
		
		store  = (String)ret.get(0).get("pkStore");	
		dept = (String)ret.get(0).get("pkDept");
		
		if(StringUtils.isEmpty(store) || StringUtils.isEmpty(dept)) return null;
		
		
		return store+"&"+dept;
	} 
	
	/**
	 * 删除处方
	 * @param param
	 * @param user
	 */
	public void delPress(String param,IUser user){
		String press = JsonUtil.getFieldValue(param, "pressId");
	    int count =  DataBaseHelper.queryForScalar("select count(1) from cn_order where pk_pres=? and flag_sign='1' ", Integer.class, new Object[]{press});
		if(count>1) throw new BusException("处方已签署,请刷新！");
		CnPrescription cpv = new CnPrescription();
		cpv.setPkPres(press);
		
		DataBaseHelper.execute("DELETE FROM CN_ORDER WHERE ORDSN_PARENT IN (SELECT ORDSN_PARENT FROM CN_ORDER WHERE PK_PRES = ? )", new Object[]{press});//删除护瞩
		DataBaseHelper.deleteBeanByPk(cpv);
		DataBaseHelper.deleteBeanByWhere(new CnOrder(), "pk_pres='"+cpv.getPkPres()+"'");	
	}
	
	/**
	 * 签署处方
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void signPress(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnIpPressVO> preses = JsonUtil.readValue(param,new TypeReference<List<CnIpPressVO>>() {});
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		User u = (User)user;
				
		if(null == preses || preses.size() <= 0) return;
		vaildOrdts(preses);
		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		List<CnOrder> cpOrdList = new ArrayList<CnOrder>();
		List<CnOrder> messangOrds = new ArrayList<CnOrder>();
		Date outHosOrdDate = getPressDate(preses);
		for(CnIpPressVO cip : preses){
			CnPrescription pres = new CnPrescription();
			BeanUtils.copyProperties(pres, cip);
			
			if(StringUtils.isEmpty(pres.delFlag)) pres.setDelFlag("0");
			Date dPress = getCnOrdDateStart(cip.getDatePres(),outHosOrdDate);//补充:医嘱的开始日期同处方日期，判断是否存在出院类医嘱，如果存在，判断处方日期不能大于出院类医嘱开立时间，如果大于出院医嘱时间，将时间修改为出院医嘱时间，否则取当前时间；
			pres.setDatePres(dPress);
			if(StringUtils.isBlank(cip.getPkPres())){
				pres.setPkDept(u.getPkDept());
				pres.setNameEmpOrd(u.getUserName());
				pres.setPkEmpOrd(u.getPkDept());
				DataBaseHelper.insertBean(pres);
			}		
			
			List<CnOrderVO> ords = cip.getOrds();
			
			if(null == ords || ords.size() <= 0) continue;
			
			for(CnOrderVO co : ords){
				CnOrder cco = new CnOrder();
				
				BeanUtils.copyProperties(cco, co);
				
				if(!StringUtils.isEmpty(co.getFlagSign()) && "1".equals(co.getFlagSign())) continue;
				if(!StringUtils.isEmpty(co.getEuStatusOrd()) && !"0".equals(co.getEuStatusOrd())) continue;
			    
				if(StringUtils.isNoneEmpty(cip.getPkCpexp())){
					cco.setPkCpexp(cip.getPkCpexp());
					cco.setPkCprec(cip.getPkCprec());
					cco.setExpNote(cip.getExpNote());
					cpOrdList.add(cco);
				}
				fillCnOrder(cco,user);				
				if(null == co.getDateSign()) co.setDateSign(new Date());
				
				cco.setPkPres(pres.getPkPres());
				cco.setFlagSign("1");
				cco.setDateSign(new Date());
				cco.setPkEmpOrd(u.getPkEmp());
				cco.setNameEmpOrd(u.getNameEmp());
				if(StringUtils.isEmpty(co.getEuStatusOrd()) || "0".equals(co.getEuStatusOrd())) cco.setEuStatusOrd("1");
				double qcg = cco.getQuan();
				cco.setQuanCg(qcg);				//把总量赋值给quan_cg字段
				cco.setTs(new Date());
				cco.setDateStart(pres.getDatePres());//处方下的医嘱时间同处方日期一致
				if(StringUtils.isBlank(cco.getPkCnord())){					
					DataBaseHelper.insertBean(cco);
				}
				else DataBaseHelper.updateBeanByPk(cco,false);
				
				messangOrds.add(cco);
			}			
		}
		if(cpOrdList.size()>0) cnPubService.recExpOrder(false, cpOrdList, u);
		cnPubService.sendMessage("新医嘱",messangOrds,false);
	}
	
	/**
	 * 取消签署
	 * 交易码004004009027
	 * @param param
	 * @param user
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void UpdatePressSta(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnIpPressVO> preses = JsonUtil.readValue(param,new TypeReference<List<CnIpPressVO>>() {});
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		User u = (User)user;
				
		if(null == preses || preses.size() <= 0) return;
		vaildOrdts(preses);
		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		List<CnOrder> cpOrdList = new ArrayList<CnOrder>();
		List<CnOrder> messangOrds = new ArrayList<CnOrder>();
		Date outHosOrdDate = getPressDate(preses);
		for(CnIpPressVO cip : preses){
			CnPrescription pres = new CnPrescription();
			BeanUtils.copyProperties(pres, cip);
			
			if(StringUtils.isEmpty(pres.delFlag)) pres.setDelFlag("0");
			Date dPress = getCnOrdDateStart(cip.getDatePres(),outHosOrdDate);//补充:医嘱的开始日期同处方日期，判断是否存在出院类医嘱，如果存在，判断处方日期不能大于出院类医嘱开立时间，如果大于出院医嘱时间，将时间修改为出院医嘱时间，否则取当前时间；
			pres.setDatePres(dPress);
			if(StringUtils.isBlank(cip.getPkPres())){
				pres.setPkDept(u.getPkDept());
				pres.setNameEmpOrd(u.getUserName());
				pres.setPkEmpOrd(u.getPkDept());
				DataBaseHelper.insertBean(pres);
			}		
			
			List<CnOrderVO> ords = cip.getOrds();
			
			if(null == ords || ords.size() <= 0) continue;
			
			for(CnOrderVO co : ords){
				CnOrder cco = new CnOrder();
				
				BeanUtils.copyProperties(cco, co);
				
				if(!StringUtils.isEmpty(co.getFlagSign()) && "0".equals(co.getFlagSign())) continue;
				if(!StringUtils.isEmpty(co.getEuStatusOrd()) && !"1".equals(co.getEuStatusOrd())) continue;
			    
				if(StringUtils.isNoneEmpty(cip.getPkCpexp())){
					cco.setPkCpexp(cip.getPkCpexp());
					cco.setPkCprec(cip.getPkCprec());
					cco.setExpNote(cip.getExpNote());
					cpOrdList.add(cco);
				}
				fillCnOrder(cco,user);				
				if(null == co.getDateSign()) co.setDateSign(new Date());
				
				cco.setPkPres(pres.getPkPres());
				cco.setFlagSign("0");
				cco.setDateSign(new Date());
				cco.setPkEmpOrd(u.getPkEmp());
				cco.setNameEmpOrd(u.getNameEmp());
				if(StringUtils.isEmpty(co.getEuStatusOrd()) || "1".equals(co.getEuStatusOrd())) cco.setEuStatusOrd("0");
				
				cco.setTs(new Date());
				cco.setDateStart(pres.getDatePres());//处方下的医嘱时间同处方日期一致
				if(StringUtils.isBlank(cco.getPkCnord())){					
					DataBaseHelper.insertBean(cco);
				}
				else DataBaseHelper.updateBeanByPk(cco,false);
				if(cco.getOrdsnParent() == null) return;
				DataBaseHelper.update("update cn_order set eu_status_ord = '0' , flag_sign = '0' ,ts=?   where ordsn_parent= ? and  eu_status_ord ='1' and flag_doctor='0'  and ordsn!=ordsn_parent ",cco.getTs(), cco.getOrdsnParent());
				messangOrds.add(cco);
			}			
		}
		if(cpOrdList.size()>0) cnPubService.recExpOrder(false, cpOrdList, u);
		cnPubService.sendMessage("新医嘱",messangOrds,false);
	}
	
	
	/**
	 * 删除医嘱
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void delOrder(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<String> oids = JsonUtil.readValue(param, new TypeReference<List<String>>() {});
		if(oids==null || oids.size()==0) return;
	    List<String> pkCns = cnPubService.splitPkTsValidOrd(oids);
		List<CnOrder> list = new ArrayList<CnOrder>();
		for(String pk : pkCns){
			CnOrder co = new CnOrder();
			co.setPkCnord(pk);
			list.add(co);
		}
		DataBaseHelper.batchUpdate("delete from cn_order where pk_cnord =:pkCnord", list);
//		for(String oid : oids) execDelOrd(oid);		
	}
	
	/**
	 * 具体执行删除医嘱的方法
	 * @param oid
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private String execDelOrd(String oid) throws IllegalAccessException, InvocationTargetException{
		CnOrder co = new CnOrder();
		co.setPkCnord(oid);
		
		String sql = "select * from cn_order o where o.pk_cnord='"+oid+"'";
		List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);		
		
		if(null == ps || ps.size() <= 0){
			DataBaseHelper.deleteBeanByPk(co);				
			return null;
		}
		
		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		BeanUtils.copyProperties(co, ps.get(0));
				
		if(co.getOrdsnParent().intValue() == co.getOrdsnParent().intValue()){
			DataBaseHelper.deleteBeanByWhere(co, "ordsn_parent='"+co.getOrdsnParent()+"'");
		}else DataBaseHelper.deleteBeanByPk(co);	
		
		return co.getPkPres();
		
	}
	
	/**
	 * 计算医嘱执行次数
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public int countOrderTimes(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		CnOrder co = JsonUtil.readValue(param,CnOrder.class);
		if(null == co) throw new BusException("没有获取到医嘱信息！");
		if(co.getQuanCg() < 0) return 0;
		
		/*GenerateExLisOrdVo eco = new GenerateExLisOrdVo();
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		BeanUtils.copyProperties(eco, co);	
		
		Date start = new Date();
		if(null == co.getDays()) throw new BusException("没有获取到医嘱天数！");
		eco.setDateStart(new Date());
		Date endd = new Date(start.getTime() + co.getDays() * 1000*60*60*24);
		
		eco.setDateStop(endd);		
		
		
		OrderAppExecVo exceVO = calOrderPdQuanService.calOrdQuan(start, endd, eco);
		return new Double(exceVO.getCount()).intValue();*/
		
		GenerateExLisOrdVo eco = new GenerateExLisOrdVo();
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		BeanUtils.copyProperties(eco, co);	
		
		Date start = null;
		if(null == co.getDateStart()) start = new Date();
		else start = co.getDateStart();
		
		if(null == co.getDays()) throw new BusException("没有获取到医嘱天数！");
		eco.setDateStart(new Date());
		Date endd = new Date(start.getTime() + co.getDays() * 1000*60*60*24);
		
		eco.setDateStop(endd);					
		OrderAppExecVo exceVO = new OrderFreqCalCountHandler().calCount(co.getCodeOrdtype(),co.getCodeFreq(),start,endd,co.getDosage()/co.getQuanCg(),false);	
		
		return new Double(exceVO.getCount()).intValue();
	}
	
	/**
	 * 作废处方
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public int wastePress(String param,IUser user) throws IllegalAccessException, InvocationTargetException {

		String pkPress = JsonUtil.getFieldValue(param, "pressId");
		String sql = "select  * from cn_order o where o.pk_pres='"+pkPress + "'";
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		List<Map<String,Object>> l = DataBaseHelper.queryForList(sql);
		if(null == l || l.size() <= 0) return 0;
		
		List<CnOrder> ords = new ArrayList<CnOrder>();
		User u = (User)user;
		
		for(Map<String,Object> m : l){
			CnOrder co = new CnOrder();
			BeanUtils.copyProperties(co, m);
			
			if("9".equals(co.getEuStatusOrd())) continue;
			
			co.setFlagErase("1");
			co.setPkEmpErase(u.getPkEmp());
			co.setNameEmpErase(user.getUserName());
			co.setDateErase(new Date());
			co.setEuStatusOrd("9");
			ords.add(co);
		}				 
		
		BloodService.cancelOrder(ords, user);	
		cnPubService.sendMessage("作废医嘱", ords,false);
		return 1;		
	}
	
	/**
	 * 获取当个处方用于打印
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	public CnIpPressVO getSinglePress(String param,IUser user) throws IllegalAccessException, InvocationTargetException, ParseException{
		String pkPress = JsonUtil.getFieldValue(param, "pressId");
		if(StringUtils.isEmpty(pkPress)) throw new BusException("没有获取到处方信息！");
		
		String sql = "select * from cn_prescription p where p.pk_pres='"+pkPress+"'";
		List<Map<String,Object>> l = DataBaseHelper.queryForList(sql);
		
		if(null == l || l.size() <= 0) return null;
		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		CnIpPressVO pres = new CnIpPressVO();
		BeanUtils.copyProperties(pres, l.get(0));
		
		pres.setOrds(queryOrdersExec(pres.getPkPres(),param,user));
		
		return pres;		
	}
	
	/**
	 * 获取医生的医疗项目权限，用于抗菌药
	 * @return
	 */
	public String getAntiCode(String param,IUser user){
		String pkEmp = JsonUtil.getFieldValue(param, "empId");
		String sql = "select emp.dt_empsrvtype,dt_anti from bd_ou_employee emp where emp.pk_emp='"+pkEmp+"' and emp.flag_active=1";
		
		List<Map<String,Object>> l = DataBaseHelper.queryForList(sql);		
		if(null == l || l.size() <= 0)  return "'01','00'";
		
		String code = (String)l.get(0).get("dtEmpsrvtype");
		if(StringUtils.isEmpty(code)) return  "'01','00'";
		
		//if("01".equals(code) || "02".equals(code)) return "'00','01','02','03'";
		
		//if("03".equals(code)) return "'00','01','02'";
		
		//if("05".equals(code)){ //住院医生//}
		String antiCode = (String)l.get(0).get("dtAnti");				
	    if("02".equals(antiCode)){
			return "'00','01','02'";
		}
		else if("03".equals(antiCode)){
			return "'00','01','02','03'";				
		}else if("01".equals(antiCode)){
			return "'01'";
		}		
	    return "00";
		//return "'01','00'";		
	}
	
	/**
	 * 获取医生的医疗项目权限，用于毒麻处方
	 * @return
	 */
	public String getAnsiCode(String param,IUser user){
		String pkEmp = JsonUtil.getFieldValue(param, "empId");
		String sql = "select  emp.flag_anes, emp.flag_spir_one, emp.flag_spir_sec,emp.flag_poi from bd_ou_employee emp where emp.pk_emp='"+pkEmp+"' and emp.flag_active=1";
		
		String ret = "'00',";
		
		List<Map<String,Object>> l = DataBaseHelper.queryForList(sql);		
		if(null == l || l.size() <= 0)  return ret;
		
		String code = (String)l.get(0).get("flagAnes");
		if(!StringUtils.isEmpty(code) && "1".equals(code)) ret +=  "'01',";
		
		code = (String)l.get(0).get("flagSpirOne");
		if(!StringUtils.isEmpty(code) && "1".equals(code)) ret +=  "'02',";
		
		code = (String)l.get(0).get("flagSpirSec");
		if(!StringUtils.isEmpty(code) && "1".equals(code)) ret +=  "'03',";
		
		code = (String)l.get(0).get("flagPoi");
		if(!StringUtils.isEmpty(code) && "1".equals(code)) ret +=  "'04','07'";
		
		if(ret.endsWith(",")) ret = ret.substring(0,ret.length()-1);
		
		return ret;		
	}
	
	/**
	 * 查询医生的所有权限，毒麻，抗菌等，用于处方的模板
	 */
	public Map<String,Object> getAuthByEmployee(String param,IUser user){
		List<Map<String,Object>> rets = cnIpPressAuthMapper.qryPressAuth(user.getId());
		
		if(null == rets || rets.size() <= 0) throw new BusException("请维护当前医生处方权限！");
		
		Map m = rets.get(0);
		if(null != m.get("dtEmpsrvtype")) m.put("AntiCode", m.get("dtEmpsrvtype"));
		
		return m;		
	}
}
