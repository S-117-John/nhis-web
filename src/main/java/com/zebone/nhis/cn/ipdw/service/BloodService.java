package com.zebone.nhis.cn.ipdw.service;

import com.sun.xml.bind.v2.TODO;
import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.cn.ipdw.dao.CnBloodMapper;
import com.zebone.nhis.cn.ipdw.dao.CnIpPressMapper;
import com.zebone.nhis.cn.ipdw.vo.BlAppSaveVo;
import com.zebone.nhis.cn.ipdw.vo.BloodApplyVO;
import com.zebone.nhis.cn.ipdw.vo.BloodPatientVO;
import com.zebone.nhis.cn.ipdw.vo.CnTransApprovalVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnTransApply;
import com.zebone.nhis.common.module.cn.ipdw.CnTransApproval;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.CnNoticeService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.support.QueryOrdWfService;
import com.zebone.nhis.ex.pub.vo.BdWfOrdArguDeptVo;
import com.zebone.nhis.ex.pub.vo.BdWfOrdArguVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.webservice.vo.ticketvo.Data;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * ???????????????
 * @author gejianwen
 *
 */
@Service
public class BloodService {
	
	@Autowired      
	private BdSnService bdSnService;
	
	@Autowired
	private CnIpPressMapper cnIpPressMapper;
	
	@Autowired
	private QueryOrdWfService queryOrdWfService;
	
	@Autowired
	private CnPubService cnPubService;

	@Autowired
	private CnBloodMapper bloodDao;

	@Autowired
	private CnNoticeService cnNoticeService;
	@Autowired
    private IpCgPubService ipCgPubService;
	/**
	 * ?????????????????????????????????
	 * @param
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<BloodApplyVO> getPatientReport(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<BloodApplyVO> ret = new ArrayList<BloodApplyVO>();
		
		String 	pv = JsonUtil.getFieldValue(param, "pv");		
		String sql = "select o.*,c.BARCODE_BP,c.BT_CONTENT,c.DATE_BP,c.DATE_PLAN,c.DESC_DIAG,c.DT_BT_ABO,c.DT_BT_RH,c.DT_BTTYPE,c.EU_STATUS,c.FLAG_AL,c.FLAG_BP,c.FLAG_BTHIS,c.FLAG_LAB,c.FLAG_PREG,c.NAME_EMP_BP,c.PK_DIAG,c.PK_EMP_BP,c.PK_ORDBT,c.PK_UNIT_BT,c.QUAN_BP,c.QUAN_BT,c.cnt_preg,c.cnt_labor,c.desc_bthis,c.code_apply as code_apply_lab ,c.eu_pre_aborh,c.note  from cn_order o,cn_trans_apply c where o.pk_pv = '"+pv+"' and o.pk_cnord=c.pk_cnord and (c.del_flag is null or c.del_flag <> '1')";
		sql += " order by o.date_start desc";
		List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		for(Map<String,Object> map : ps){
			BloodApplyVO bv = new BloodApplyVO();
			BeanUtils.copyProperties(bv, map);	
			
			if(null == bv.getBloodPatientVO()){
				//CnTransApply ctp = new CnTransApply();
				BloodPatientVO ctp = new BloodPatientVO();
				BeanUtils.copyProperties(ctp, map);
				ctp.setCodeApply(bv.getCodeApplyLab());
				//????????????????????????
				String sqlLab="select * from ex_lab_occ where code_apply=? ";
				List<ExLabOcc> lab = DataBaseHelper.queryForList(sqlLab,ExLabOcc.class,ctp.getCodeApply());
				ctp.setExOcc(lab);
				bv.setBloodPatientVO(ctp);
			}
			ret.add(bv);
		}
		
		return ret;	
	}
	
	/**
	 * ????????????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BloodApplyVO getSingleTrans(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		BloodApplyVO bv = JsonUtil.readValue(param,BloodApplyVO.class);
		if(null == bv) throw new BusException("????????????????????????(pkcnord)??????!");
		
		String sql = "select c.*,o.*,dept.NAME_DEPT as dept_name,d.NAME as dt_bt_abo_name,dd.NAME as dt_bt_rh_name,u.NAME as pk_unit_bt_name, bo.name "
                   +" from cn_order o inner join cn_trans_apply c on o.PK_CNORD=c.PK_CNORD "
                   +" inner join bd_ou_dept dept on dept.pk_dept=o.pk_dept "
                   +" inner join bd_defdoc d on d.code=c.DT_BT_ABO and d.code_defdoclist='000004' "
                   +" inner join bd_defdoc dd on dd.code=c.DT_BT_RH and dd.code_defdoclist='000005' "
                   +" inner join BD_UNIT u on c.PK_UNIT_BT=u.PK_UNIT "
                   +" inner join bd_ord bo on bo.CODE=o.CODE_ORD and bo.code_ordtype='12' where o.pk_cnord = '"+bv.getPkCnord()+"'";
		List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		if(ps.size() > 0){
			bv = new BloodApplyVO();
			BeanUtils.copyProperties(bv, ps.get(0));	
			
			/*CnTransApply ctp = new CnTransApply();
			BeanUtils.copyProperties(ctp, ps.get(0));	
			bv.setBloodPatientVO(ctp);	*/		
			
			return bv;
		}else return null;		
	}
	
	public List<CnOrder> saveOrUpdateBloodApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<BloodApplyVO> bloods = JsonUtil.readValue(param,new TypeReference<List<BloodApplyVO>>() {});	
		
		return saveOrUpdateBloodApply(bloods,user);
	}
	/**
	 * ???????????????????????????(????????????)
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<CnOrder> saveOrUpdateBloodApply(List<BloodApplyVO> bloods,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<CnOrder> cnOrderList=new ArrayList();
		if(bloods != null  && bloods.size() > 0) {
	    	vaildOrdts(bloods);
			ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
			Date dt = new Date();
			Date dd = cnPubService.getOutOrdDate(bloods.get(0).getPkPv());
			if (dd != null && dd.compareTo(dt) < 0) {
				dt = dd;
			}
			for(BloodApplyVO bav : bloods){
				CnOrder co = new CnOrder();
				CnTransApply cnt = new CnTransApply();
				BloodPatientVO cntVo = bav.getBloodPatientVO();
				BeanUtils.copyProperties(co, bav);
				BeanUtils.copyProperties(cnt, cntVo);
				if (StringUtils.isEmpty(co.getFlagSign()) || co.getFlagSign() == null) {
					co.setFlagSign("0");
				}
				if(!StringUtils.isEmpty(co.getFlagSign()) && "1".equals(co.getFlagSign())) continue;
				if(!StringUtils.isEmpty(co.getEuStatusOrd()) && !"0".equals(co.getEuStatusOrd())) continue;


				co.setFlagEmer(bav.getFlagEmer());
				co.setCodeOrdtype("12");
				co.setDosage(cnt.getQuanBt());
				co.setPkUnitDos(cnt.getPkUnitBt());
				//co.setDateStart(dateStart);
				co.setDateStart(dt);
				fillCnOrder(co,user);


				cnt.setPkUnitBt(cnt.getPkUnitBt().trim());
				co.setTs(new Date());
				//??????
				if(StringUtils.isBlank(co.getPkCnord())){
					co.setPkCnord(NHISUUID.getKeyId());
					DataBaseHelper.insertBean(co);
					cnt.setPkCnord(co.getPkCnord());
					cnt.setEuStatus("0");
					cnt.setCodeApply(co.getCodeApply());
					if(StringUtils.isBlank(cnt.getPkOrdbt())) DataBaseHelper.insertBean(cnt);
					else {
						DataBaseHelper.updateBeanByPk(cnt,false);

						//??????????????????????????????????????????
						String sqlNu=" update CN_TRANS_APPLY set CNT_PREG=:cntPreg,CNT_LABOR=:cntLabor,DESC_BTHIS=:descBthis,CODE_APPLY=:codeApply where PK_ORDBT=:pkOrdbt ";
						DataBaseHelper.update(sqlNu,cnt);
					}
					CnOrder cnorder=DataBaseHelper.queryForBean(
							"select * from cn_order where pk_cnord = ? and del_flag = '0'",
							CnOrder.class, co.getPkCnord());
					cnOrderList.add(cnorder);

					//?????????????????????--??????
					if(cntVo.getExOcc()!=null && cntVo.getExOcc().size()>0){
						String ordSql="delete from ex_lab_occ where code_apply=?";
						DataBaseHelper.execute(ordSql, co.getCodeApply());
						for (ExLabOcc occ : cntVo.getExOcc()){
							occ.setPkLabocc(NHISUUID.getKeyId());
							occ.setCodeApply(co.getCodeApply());//????????????
							occ.setPkPi(co.getPkPi());
							occ.setPkPv(co.getPkPv());
							occ.setPkOrg(co.getPkOrg());
							occ.setPkOrgOcc(co.getPkOrg());
							occ.setEuType("0");
							occ.setDelFlag("0");
							DataBaseHelper.insertBean(occ);
						}
					}
					continue;
				}

				DataBaseHelper.updateBeanByPk(co,false);
				cnt.setPkCnord(co.getPkCnord());
				cnt.setEuStatus("0");
				cnt.setCodeApply(co.getCodeApply());
				DataBaseHelper.updateBeanByPk(cnt,false);
				//??????????????????????????????????????????
				String sqlNu=" update CN_TRANS_APPLY set CNT_PREG=:cntPreg,CNT_LABOR=:cntLabor,DESC_BTHIS=:descBthis,CODE_APPLY=:codeApply where PK_ORDBT=:pkOrdbt ";
				DataBaseHelper.update(sqlNu,cnt);

				CnOrder cnorder=DataBaseHelper.queryForBean(
						"select * from cn_order where pk_cnord = ? and del_flag = '0'",
						CnOrder.class, co.getPkCnord());
				cnOrderList.add(cnorder);

				//?????????????????????--??????
				if(cntVo.getExOcc()!=null && cntVo.getExOcc().size()>0){
					String ordSql="delete from ex_lab_occ where code_apply=?";
					DataBaseHelper.execute(ordSql, co.getCodeApply());
					for (ExLabOcc occ : cntVo.getExOcc()){
						occ.setPkLabocc(NHISUUID.getKeyId());
						occ.setCodeApply(co.getCodeApply());//????????????
						occ.setPkPi(co.getPkPi());
						occ.setPkPv(co.getPkPv());
						occ.setPkOrg(co.getPkOrg());
						occ.setPkOrgOcc(co.getPkOrg());
						occ.setEuType("0");
						occ.setDelFlag("0");
						DataBaseHelper.insertBean(occ);
					}
				}
			}
		
		}
		return cnOrderList;
	}
	private void vaildOrdts(List<BloodApplyVO> bloods) {
		List<CnOrder> list = new ArrayList<CnOrder>();
		for(BloodApplyVO co : bloods){		
			if(!StringUtils.isEmpty(co.getFlagSign()) && "1".equals(co.getFlagSign())) continue;
			if(!StringUtils.isEmpty(co.getEuStatusOrd()) && !"0".equals(co.getEuStatusOrd())) continue;			
			if(!StringUtils.isBlank(co.getPkCnord())){
				CnOrder ord = new CnOrder();
				ord.setPkCnord(co.getPkCnord());
				ord.setTs(co.getTs());
				list.add(ord);
			}
		}
		if(list.size()>0) cnPubService.vaildUpdateCnOrdts(list);
	}

	/**
	 * ???????????????
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void delApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		BloodApplyVO bav = JsonUtil.readValue(param,BloodApplyVO.class);
		delApply(bav);
	}
	/**
	 * ???????????????(????????????)
	 * @param param
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void delApply(BloodApplyVO param) throws IllegalAccessException, InvocationTargetException{
		BloodApplyVO bav = param;
		if(null == bav) return;
		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		CnOrder co = new CnOrder();  		
		BeanUtils.copyProperties(co, bav);
		BloodPatientVO cnt1 = bav.getBloodPatientVO();
		CnTransApply cnt = new CnTransApply();
		BeanUtils.copyProperties(cnt, cnt1);

		List<CnOrder> list = new ArrayList<CnOrder>();
		list.add(co);
		cnPubService.vaildUpdateCnOrdts(list);

		cnt.setPkCnord(co.getPkCnord());
		
		co.setDelFlag("1");
		cnt.setDelFlag("1");
		
		DataBaseHelper.updateBeanByPk(cnt, false);
		//DataBaseHelper.deleteBeanByPk(co);
		String ordSql="delete from cn_order where ordsn_parent in (select ordsn_parent from cn_order where pk_cnord = ? )  and flag_sign='0'";
		DataBaseHelper.execute(ordSql, co.getPkCnord());

		//?????????????????????--??????
		String occSql="delete from ex_lab_occ where code_apply=?";
		DataBaseHelper.execute(occSql,co.getCodeApply());

	}
	
	
	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void signApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		BlAppSaveVo saveBlood = JsonUtil.readValue(param,new TypeReference<BlAppSaveVo>() {});
		List<BloodApplyVO> bloods = saveBlood.getBloods();
		User u = (User)user;
		if(null == bloods || bloods.size() <= 0) return;
		String pkPv=null;
		String pkWgOrg=null;
		String pkWg=null;
		if(StringUtils.isNotEmpty(bloods.get(0).getPkPv())){
			//??????????????????????????????
			if(checkPvInfo(pkPv)){
				throw  new BusException("???????????????????????????????????????,?????????????????????????????????????????????????????????????????????");
			}

			pkPv=bloods.get(0).getPkPv();
			PvEncounter pvInfo = DataBaseHelper.queryForBean("Select * From PV_ENCOUNTER Where pk_pv=? ",PvEncounter.class, new Object[]{pkPv});
			pkWgOrg=pvInfo.getPkWgOrg();
			pkWg=pvInfo.getPkWg();
		}


		vaildOrdts(bloods);	
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
 		List<CnOrder> cpOrderList = new ArrayList<CnOrder>();
 		List<CnOrder> messageOrds = new ArrayList<CnOrder>();
 		List<CnOrder> parentOrdList= new ArrayList<CnOrder>();
		for(BloodApplyVO bav : bloods){
			CnOrder co = new CnOrder();  		
			BeanUtils.copyProperties(co, bav);
			co.setPkWgOrg(pkWgOrg);
			co.setPkWg(pkWg);
			CnTransApply cnt1 = bav.getBloodPatientVO();
			CnTransApply cnt=new CnTransApply();
			BeanUtils.copyProperties(cnt, cnt1);
			//???????????????
			BloodPatientVO cntVo = bav.getBloodPatientVO();
		  
			if(!StringUtils.isEmpty(co.getFlagSign()) && "1".equals(co.getFlagSign())) continue;
			if(!StringUtils.isEmpty(co.getEuStatusOrd()) && !"0".equals(co.getEuStatusOrd())) continue;
			if(StringUtils.isNotEmpty((bav.getPkCpexp()))){
				co.setPkCpexp(bav.getPkCpexp());
				co.setPkCprec(bav.getPkCprec());
				co.setExpNote(bav.getExpNote());
				co.setPkCpphase(bav.getPkCpphase());
				co.setNameExp(bav.getNameExp());
				cpOrderList.add(co);				
			}

			cnt.setEuStatus("0");
			co.setFlagEmer(bav.getFlagEmer());
			
			co.setFlagSign("1");
			co.setPkEmpOrd(u.getPkEmp());
			co.setNameEmpOrd(u.getNameEmp());
			if(StringUtils.isEmpty(co.getEuStatusOrd()) || "0".equals(co.getEuStatusOrd())) co.setEuStatusOrd("1");
			if(null == co.getDateSign()) co.setDateSign(new Date());						
			fillCnOrder(co,user);					
			messageOrds.add(co);
			co.setTs(new Date());
			if(StringUtils.isBlank(co.getPkCnord())){
				//co.setDateStart(new Date());
				co.setCodeOrdtype("12");	
				DataBaseHelper.insertBean(co);
				
				cnt.setPkCnord(co.getPkCnord());
				cnt.setPkUnitBt(cnt.getPkUnitBt().trim()); 
				if(StringUtils.isBlank(cnt.getPkOrdbt())) DataBaseHelper.insertBean(cnt);
				else DataBaseHelper.updateBeanByPk(cnt,false);

				//?????????????????????--??????
				if(cntVo.getExOcc()!=null && cntVo.getExOcc().size()>0){
					String ordSql="delete from ex_lab_occ where code_apply=?";
					DataBaseHelper.execute(ordSql, co.getCodeApply());
					for (ExLabOcc occ : cntVo.getExOcc()){
						occ.setPkLabocc(NHISUUID.getKeyId());
						occ.setCodeApply(co.getCodeApply());//????????????
						occ.setPkPi(co.getPkPi());
						occ.setPkPv(co.getPkPv());
						occ.setPkOrg(co.getPkOrg());
						occ.setPkOrgOcc(co.getPkOrg());
						occ.setEuType("0");
						occ.setDelFlag("0");
						DataBaseHelper.insertBean(occ);
					}
				}

				continue;
			}			
			
			DataBaseHelper.updateBeanByPk(co,false);	
			DataBaseHelper.updateBeanByPk(cnt,false);

			//?????????????????????--??????
			if(cntVo.getExOcc()!=null && cntVo.getExOcc().size()>0){
				String ordSql="delete from ex_lab_occ where code_apply=?";
				DataBaseHelper.execute(ordSql, co.getCodeApply());
				for (ExLabOcc occ : cntVo.getExOcc()){
					occ.setPkLabocc(NHISUUID.getKeyId());
					occ.setCodeApply(co.getCodeApply());//????????????
					occ.setPkPi(co.getPkPi());
					occ.setPkPv(co.getPkPv());
					occ.setPkOrg(co.getPkOrg());
					occ.setPkOrgOcc(co.getPkOrg());
					occ.setEuType("0");
					occ.setDelFlag("0");
					DataBaseHelper.insertBean(occ);
				}
			}

			if(co.getOrdsn().compareTo(co.getOrdsnParent())==0){
				parentOrdList.add(co);
			}
		}
		//???????????????????????????????????????????????????????????????????????????
		if(parentOrdList.size()>0){
			DataBaseHelper.batchUpdate("update cn_order set eu_status_ord=:euStatusOrd ,flag_sign=:flagSign ,date_sign=:dateSign,pk_emp_ord=:pkEmpOrd,name_emp_ord=:nameEmpOrd where ordsn_parent =:ordsnParent  and flag_sign='0'", parentOrdList);
	    }

		if("1".equals(saveBlood.getIfMaxApp())){ //??????????????????
			saveMaxApp(saveBlood.getMaxList(),(User)user);
		}
		
		if(cpOrderList.size()>0) cnPubService.recExpOrder(false, cpOrderList, (User)user);
		cnPubService.sendMessage("?????????", messageOrds,false);

		//????????????????????????
		cnNoticeService.saveCnNotice(messageOrds);
	}
	
	/**
	 * ????????????????????????
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void cancelSignApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<BloodApplyVO> bloods = JsonUtil.readValue(param,new TypeReference<List<BloodApplyVO>>() {});	
		User u = (User)user;
		if(null == bloods || bloods.size() <= 0) return;
		vaildOrdts(bloods);	
		List<CnOrder> cnOrds=new ArrayList<CnOrder>();
		for(BloodApplyVO blood : bloods){
			CnOrder ord = new CnOrder();  		
			BeanUtils.copyProperties(ord, blood);
			ord.setPkEmpOrd(null);
			ord.setNameEmpOrd(null);
			ord.setDateSign(null);
			if(StringUtils.isNotBlank(ord.getPkOrd()))
				cnOrds.add(ord);
			
		}
		
		if(cnOrds.size()>0){
			DataBaseHelper.batchUpdate("update cn_order set eu_status_ord = '0', flag_sign='0' , pk_emp_ord=:pkEmpOrd,name_emp_ord=:nameEmpOrd,date_sign =:dateSign  where eu_status_ord = '1' and ordsn_parent  in (:ordsnParent ) ",cnOrds);
		}
		
	}
	
	
	private void fillCnOrder(CnOrder co,IUser user){
		User u = (User)user;
		co.setEuAlways("1");
		if(StringUtils.isEmpty(co.getEuStatusOrd())) co.setEuStatusOrd("0");
				
		//co.setPkDeptExec(co.getPkDept()); //TODO ?????????????????????????????????????????????
		co.setPkDept(u.getPkDept());
		if(StringUtils.isBlank(co.getPkCnord())){
			co.setPkEmpInput(u.getId());
			co.setNameEmpInput(u.getUserName());
		}
		co.setPkOrg(u.getPkOrg());
		co.setEuPvtype("3");
		co.setFlagDurg("0");
		
		
		if(null == co.getDateEnter()) co.setDateEnter(new Date());
		
		if(null == co.getOrdsn() || co.getOrdsn().intValue() == 0){
			co.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, u));
			co.setOrdsnParent(co.getOrdsn());
		}
		
		co.setFlagDoctor("1");				
		BdWfOrdArguVo bwo = new BdWfOrdArguVo();
		bwo.setPkOrg(u.getPkOrg());
		bwo.setEuPvtype(co.getEuPvtype());
		bwo.setOrdrecur(co.getEuAlways());
		bwo.setOrderType(co.getCodeOrdtype());	
		if(StringUtils.isEmpty(bwo.getPkOrg()) || bwo.getPkOrg().indexOf("~") >= 0 )bwo.setPkOrg(co.getPkOrg());
		
		BdWfOrdArguDeptVo bwd = queryOrdWfService.exce(bwo);
		
		if(null != bwd){
			co.setPkDeptExec(bwd.getPkDept());
			co.setPkOrgExec(bwd.getPkOrgExec());
		}
		
		co.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));		
//		co.setNameEmpOrd(u.getUserName()); //???????????????
//		co.setPkEmpOrd(u.getPkEmp());
		co.setDescOrd(co.getNameOrd());
		
		co.setQuan(new Double(1));
		
		co.setFlagBase("0");
		//co.setFlagBl("1");????????????
		co.setFlagCp("0");
		co.setFlagDoctor("1");
		if(StringUtils.isEmpty(co.getFlagEmer())) co.setFlagEmer("0");
		co.setFlagFirst("0");
		//co.setFlagFit("0");
		co.setFlagMedout("0");
		//co.setFlagNote("1");????????????
		co.setFlagPrev("0");
		co.setFlagPrint("0");
		co.setFlagSelf("0");
		co.setFlagStop("0");
		co.setFlagThera("0");
		co.setFlagErase("0");
		co.setFlagStopChk("0");
		co.setFlagEraseChk("0");	
		
		if(null == co.getDateStart()) co.setDateStart(new Date());
		if(null == co.getDateEnter()) co.setDateEnter(new Date());
		if(null == co.getDateEffe())  co.setDateEffe(new Date());
		
	}
	
	public static void cancelOrder(List<CnOrder> cnOrds,IUser user){
		if(null == cnOrds || cnOrds.size() <= 0) return ;		
		User u = (User)user;
		List<String> pkOrds = new ArrayList<String>();	
		
		for(CnOrder o : cnOrds) pkOrds.add(o.getPkCnord());
		
		//????????????????????????????????????(????????????????????????????????????????????????)
		String ifNeedNurseChk = ApplicationUtils.getSysparam("CN0028", false);
		if(ifNeedNurseChk==null) ifNeedNurseChk="0";
		if(ifNeedNurseChk.equals("1")){
			List<CnOrder> revokeList=new ArrayList<CnOrder>();
			List<CnOrder> updateList=new ArrayList<CnOrder>();
			Date d = new Date();
			for(CnOrder order: cnOrds){
				if(!order.getEuStatusOrd().equals("0")&&!order.getEuStatusOrd().equals("1")){
					order.setEuStatusOrd("9");
					revokeList.add(order);
				}else{
					updateList.add(order);
				}
				order.setFlagErase("1");
				order.setDateErase(d);
				order.setPkEmpErase(u.getPkEmp());
				order.setNameEmpErase(u.getNameEmp());
				order.setTs(order.getDateErase());
			}
			if(updateList.size()>0){
				//????????????
				DataBaseHelper.batchUpdate("update cn_order set flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  "
	                                      + "where pk_cnord =:pkCnord and eu_status_ord in ('0','1') ", updateList);
			}
			if(revokeList.size()>0){
			    //????????????
				DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord",revokeList);
				DataBaseHelper.batchUpdate("update cn_order set eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  "
						                  + "where pk_cnord =:pkCnord and eu_status_ord not in ('0','1') ", revokeList);
			}
			return;
		}
		
		Map<String,Object> pkOrdMap = new HashMap<String,Object>();
		pkOrdMap.put("pk_cnord", pkOrds);
		String exListSql = "select ord.name_ord,ex.pk_exocc,ex.pk_cnord,ex.eu_status,dept.name_dept "+
						   "from ex_order_occ ex "+
						        "inner join cn_order ord on ex.pk_cnord = ord.pk_cnord  and ord.flag_durg='0' and ord.flag_bl='1' "+
						        "inner join bd_ou_dept dept on ex.pk_dept_occ = dept.pk_dept and dept.del_flag='0' "+
						   "where ex.eu_status=1 and ex.flag_canc='0' and ex.pk_cnord in(:pk_cnord)";
		//???????????????/?????????/??????????????????/??????????????????
		List<Map<String, Object>> exList  = DataBaseHelper.queryForList(exListSql,pkOrdMap);
	    String throwStr="";
	    for(Map<String,Object> map :exList){
	    	throwStr+="["+map.get("nameOrd")+"]??????["+map.get("nameDept")+"]???????????????????????????!";
	    }
	    if(StringUtils.isNotEmpty(throwStr))throw new BusException(throwStr);
	    
	    Map<String,Object> updateMap = new HashMap<String,Object>();
	    //updateMap.put("pk_cnord", pkOrds);
	    updateMap.put("date_canc", new Date());
	    updateMap.put("pk_emp_canc", u.getPkEmp());
	    updateMap.put("name_emp_canc", u.getNameEmp());
	    Set<String> set = new HashSet<>(pkOrds);
	    //????????????????????????????????????????????????
	       //1????????????????????????????????????????????????????????????????????????????????????????????????
	    /*String delNoPdExList = "delete  from ex_order_occ  "+
                "where exists (select 1 "+
                            "from cn_order ord "+
                         "where ex_order_occ.pk_cnord = ord.pk_cnord "+
                           "and ex_order_occ.eu_status = 0 "+
                           "and ord.pk_cnord in (:pk_cnord) "+
                        "and ord.flag_durg='0')";*/
	    String delNoPdExList = "update ex_order_occ set eu_status = '9',"
	    		+ "flag_canc = '1',date_canc = :date_canc,"
	    		+ "pk_emp_canc = :pk_emp_canc,name_emp_canc = :name_emp_canc "
	    		+ "where exists (select 1 from cn_order ord "
	    		+ "where ex_order_occ.pk_cnord = ord.pk_cnord "
	    		+ "and ex_order_occ.eu_status = 0 "
	    		+ "and ord.pk_cnord in ("+CommonUtils.convertSetToSqlInPart(set, "pk_cnord")+") "
	    		+ "and ord.flag_durg='0')";
	    
	    DataBaseHelper.update(delNoPdExList, updateMap);
	     //2?????????????????????????????????????????????????????????????????????????????????????????????
	    /*String delPdExList = "delete  from ex_order_occ  "+
				 "where exists (select 1 "+
				          "from cn_order ord "+
				         "where ex_order_occ.pk_cnord = ord.pk_cnord "+
				           "and ex_order_occ.pk_pdapdt is null "+
				           "and ord.pk_cnord in (:pk_cnord) "+
				           "and ord.flag_durg = '1')";*/
	    String delPdExList = "update ex_order_occ set eu_status = '9',"
	    		+ "flag_canc = '1',date_canc = :date_canc,"
	    		+ "pk_emp_canc = :pk_emp_canc,name_emp_canc = :name_emp_canc "
	    		+ "where exists (select 1 from cn_order ord "
	    		+ "where ex_order_occ.pk_cnord = ord.pk_cnord "
	    		+ "and ex_order_occ.pk_pdapdt is null "
	    		+ "and ord.pk_cnord in ("+CommonUtils.convertSetToSqlInPart(set, "pk_cnord")+") "
	    		+ "and ord.flag_durg = '1')";
	    DataBaseHelper.update(delPdExList, updateMap);
	    
	    //3?????????????????????????????????????????????
	    String pdApplyList = "select ap.name_emp_ap, ap.date_ap, apdt.pk_pdapdt "+
					  "from ex_pd_apply ap "+
					 "inner join ex_pd_apply_detail apdt on ap.pk_pdap = apdt.pk_pdap "+
					 "where ap.flag_cancel = '0' and apdt.flag_stop = '0' and apdt.flag_finish = '0' and apdt.pk_cnord in (:pk_cnord)";
		List<Map<String, Object>> pdAplistNoFin = DataBaseHelper.queryForList(pdApplyList, pkOrdMap);
		if(pdAplistNoFin!=null&&pdAplistNoFin.size()>0){
			String str="";
			for(Map<String, Object> pdap : pdAplistNoFin){
				String date_ap = pdap.get("dateAp").toString();
				if(date_ap.length() > 19) date_ap = date_ap.substring(0,19);
				if(StringUtils.isEmpty(str)){
					str = date_ap;
				}else if(str.indexOf(date_ap)<0){
					str += "???"+ date_ap;
				}
			}
			throw new BusException("????????????????????????????????????????????????????????????????????? ???????????????"+str+"????????????!");
		}
		//4.???????????????????????????0
		String pdDeListSql = "select sum(pdde.eu_direct * abs(pdde.quan_pack))  as sum_quan_de "+
                             "from ex_pd_de pdde where pdde.pk_cnord in (:pk_cnord)";
		List<Map<String, Object>> pdDelistQuan = DataBaseHelper.queryForList(pdDeListSql, pkOrdMap);
		String quan = pdDelistQuan.get(0).get("sumQuanDe")==null ?"":pdDelistQuan.get(0).get("sumQuanDe").toString();
		Double sum_quan_de =  0d;
		if(StringUtils.isNotEmpty((quan))){
			try{
				sum_quan_de = Double.parseDouble(quan);				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(sum_quan_de.doubleValue() > 0){
			throw new BusException("???????????????????????????????????? ???????????????  ?????????????????????????????????  ?????????!");
		}
		
		//??????????????????
		
	    //????????????
		DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord",cnOrds);
		DataBaseHelper.batchUpdate("update cn_order set flag_erase_chk='1', eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  where pk_cnord =:pkCnord ", cnOrds);
		DataBaseHelper.batchUpdate("update cn_order set flag_erase_chk='1', eu_status_ord = :euStatusOrd , flag_erase= :flagErase , date_erase = :dateErase , pk_emp_erase= :pkEmpErase , name_emp_erase= :nameEmpErase  where ordsn_parent in ( select ordsn_parent from cn_order where pk_cnord= :pkCnord) and flag_doctor='0' ", cnOrds);
	}
	
	/**
	 * ????????????????????????????????????????????????
	 * @param cnOrds
	 * @param user
	 */
	public static void cancelApply(List<CnOrder> cnOrds,IUser user){
		if(null == cnOrds || cnOrds.size() <= 0) return ;		
		User u = (User)user;
		List<String> pkOrds = new ArrayList<String>();	
		
		//????????????????????????????????????(????????????????????????????????????????????????)
		String ifNeedNurseChk = ApplicationUtils.getSysparam("CN0028", false);
		if(ifNeedNurseChk==null) ifNeedNurseChk="0";
		if(ifNeedNurseChk.equals("1")){
			List<CnOrder> revokeList=new ArrayList<CnOrder>();
			List<CnOrder> updateList=new ArrayList<CnOrder>();
			Date d = new Date();
			for(CnOrder order: cnOrds){
				if(!order.getEuStatusOrd().equals("0")&&!order.getEuStatusOrd().equals("1")){
					order.setEuStatusOrd("9");
					revokeList.add(order);
				}else{
					updateList.add(order);
				}
				order.setFlagErase("1");
				order.setDateErase(d);
				order.setPkEmpErase(u.getPkEmp());
				order.setNameEmpErase(u.getNameEmp());
				order.setTs(order.getDateErase());
			}
			if(updateList.size()>0){
				//????????????
				DataBaseHelper.batchUpdate("update cn_order set flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  "
	                                      + "where pk_cnord =:pkCnord and eu_status_ord in ('0','1') ", updateList);
			}
			if(revokeList.size()>0){
			    //????????????
				DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord",revokeList);
				DataBaseHelper.batchUpdate("update cn_order set eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  "
						                  + "where pk_cnord =:pkCnord and eu_status_ord not in ('0','1') ", revokeList);
			}
			return;
		}
		
		for(CnOrder o : cnOrds) pkOrds.add(o.getPkCnord());
		
		Map<String,Object> pkOrdMap = new HashMap<String,Object>();
		pkOrdMap.put("pk_cnord", pkOrds);
		String exListSql = "select ord.name_ord,ex.pk_exocc,ex.pk_cnord,ex.eu_status,dept.name_dept "+
						   "from ex_order_occ ex "+
						        "inner join cn_order ord on ex.pk_cnord = ord.pk_cnord "+
						        "inner join bd_ou_dept dept on ex.pk_dept_occ = dept.pk_dept and dept.del_flag='0' "+
						   "where ex.eu_status=1 and ex.flag_canc='0' and ex.pk_cnord in(:pk_cnord)";
		//???????????????/?????????/??????????????????/??????????????????
		List<Map<String, Object>> exList  = DataBaseHelper.queryForList(exListSql,pkOrdMap);
	    String throwStr="";
	    for(Map<String,Object> map :exList){
	    	throwStr+="["+map.get("nameOrd")+"]??????["+map.get("nameDept")+"]???????????????????????????!";
	    }
	    if(StringUtils.isNotEmpty(throwStr))throw new BusException(throwStr);
	    //????????????????????????????????????????????????
	       //1????????????????????????????????????????????????????????????????????????????????????????????????
	    String delNoPdExList = "delete  from ex_order_occ  "+
                "where exists (select 1 "+
                            "from cn_order ord "+
                         "where ex_order_occ.pk_cnord = ord.pk_cnord "+
                           "and ex_order_occ.eu_status = 0 "+
                           "and ord.pk_cnord in (:pk_cnord) "+
                        "and ord.flag_durg='0')";
	    DataBaseHelper.update(delNoPdExList, pkOrdMap);
	     //2?????????????????????????????????????????????????????????????????????????????????????????????
	    String delPdExList = "delete  from ex_order_occ  "+
				 "where exists (select 1 "+
				          "from cn_order ord "+
				         "where ex_order_occ.pk_cnord = ord.pk_cnord "+
				           "and ex_order_occ.pk_pdapdt is null "+
				           "and ord.pk_cnord in (:pk_cnord) )";
	    DataBaseHelper.update(delPdExList, pkOrdMap);
	   		
	    //????????????
	    DataBaseHelper.batchUpdate("delete from CP_REC_EXP where PK_CNORD =:pkCnord",cnOrds);
		DataBaseHelper.batchUpdate("update cn_order set flag_erase_chk='1', eu_status_ord = :euStatusOrd,flag_erase=:flagErase,date_erase=:dateErase,pk_emp_erase=:pkEmpErase,name_emp_erase=:nameEmpErase,ts=:ts  where pk_cnord =:pkCnord ", cnOrds);
		DataBaseHelper.batchUpdate("update cn_order set flag_erase_chk='1', eu_status_ord = :euStatusOrd , flag_erase= :flagErase , date_erase = :dateErase , pk_emp_erase= :pkEmpErase , name_emp_erase= :nameEmpErase  where ordsn_parent in ( select ordsn_parent from cn_order where pk_cnord= :pkCnord) and flag_doctor='0' ", cnOrds);
	}
	
	
	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void wasteApply(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<BloodApplyVO> bloods = JsonUtil.readValue(param,new TypeReference<List<BloodApplyVO>>() {});
		
		if(null == bloods || bloods.size() <= 0) return;
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		List<CnOrder> ords = new ArrayList<CnOrder>();
		User u = (User)user;
		
		for(BloodApplyVO bav : bloods){
			CnOrder co = new CnOrder();  		
			BeanUtils.copyProperties(co, bav);
			
			if("9".equals(co.getEuStatusOrd())) continue;
			
			co.setFlagErase("1");
			co.setPkEmpErase(u.getPkEmp());
			co.setNameEmpErase(user.getUserName());
			co.setDateErase(new Date());
			co.setEuStatusOrd("9");
			
			//DataBaseHelper.updateBeanByPk(co,false);
			ords.add(co);
		}
		if(ords.size() > 0){
		Map<String,Object> pkOrdMap = new HashMap<String,Object>();
		pkOrdMap.put("pk_cnord", bloods.get(0).getPkCnord());
		String bloodSql = "SELECT ord.NAME_ORD,dept.NAME_DEPT, trans.EU_STATUS AS trans_status "+
						  " FROM CN_ORDER ord "+
						  "INNER JOIN BD_OU_DEPT dept ON dept.PK_DEPT = ord.PK_DEPT_NS AND dept.DEL_FLAG = '0' "+
						  "LEFT JOIN CN_TRANS_APPLY trans ON trans.PK_CNORD = ord.PK_CNORD AND trans.DEL_FLAG = '0' "+
						  "WHERE ord.PK_CNORD IN (:pk_cnord) and ord.ordsn_parent in ( select ordsn_parent from cn_order where pk_cnord in (:pk_cnord) ) AND ord.DEL_FLAG = '0' "+
						  "AND ord.FLAG_ERASE = '0'";
		List<Map<String,Object>> traList = DataBaseHelper.queryForList(bloodSql,pkOrdMap);
		String throwStr="";
		for(Map<String,Object> map :traList){
			if(map.get("transStatus") != null && "1".compareTo(map.get("transStatus").toString()) <= 0)
			throwStr+="["+map.get("nameOrd")+"]??????["+map.get("nameDept")+"]???????????????????????????!\n";
		}
		if(StringUtils.isNotEmpty(throwStr))throw new BusException(throwStr);
		cancelOrder(ords,user);	
		cnPubService.sendMessage("????????????", ords,false);
		//????????????????????????????????????????????????-???????????????
			try {
				if("SyxPlatFormSendService".equals(ApplicationUtils.getPropertyValue("msg.processClass", ""))){
					Map<String, Object> paramMap = new HashMap<String,Object>();
					paramMap.put("ordlistvo",ords);
					PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * ???????????????????????????????????????
	 * 004004004008
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CnTransApply> queryBlood(String param,IUser user){
		Map<String, Object> params = JsonUtil.readValue(param, HashMap.class);
		List<CnTransApply> cnTransApply = cnIpPressMapper.getApplies(params);
		return cnTransApply;
		
	}
	/**
	 * ??????????????????
	 * @param param
	 * @param user
	 */
	public Map<String, Object> saveBlood(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		System.out.println("saveBlood:???????????????????????? param="+JsonUtil.writeValueAsString(param));
		Map<String, Object> rest=new HashMap<String, Object>();
		BloodApplyVO bav = JsonUtil.readValue(param,new TypeReference<BloodApplyVO>() {});
		
		if(bav != null) {
			if(bav.getBloodPatientVO()==null)
				throw new BusException("??????????????????????????????!"); 
			CnTransApply cnt = bav.getBloodPatientVO();
			if(cnt.getDtBttype()==null)
				throw new BusException("?????????????????????!");
			if(cnt.getDtBtAbo()==null)
				throw new BusException("?????????ABO??????!");
			if(cnt.getDtBtRh()==null)
				throw new BusException("?????????RH??????!");
			if(cnt.getBtContent()==null)
				throw new BusException("?????????????????????!");
			if(cnt.getDatePlan()==null)
				throw new BusException("?????????????????????!");
			//????????????			
			if(bav.getDateEffe()==null) bav.setDateEffe(new Date());  //??????????????????
			if(bav.getEuAlways()==null) bav.setEuAlways("1");//????????????
			if(bav.getEuPvtype()==null) bav.setEuPvtype("3");//????????????
			if(bav.getCodeFreq()==null) bav.setCodeFreq("once");//????????????
			if(bav.getFlagFirst()==null) bav.setFlagFirst("0");//????????????
			if(bav.getEuStatusOrd()==null) bav.setEuStatusOrd("1");//?????????????????????1
			if(bav.getDateEnter()==null) bav.setDateEnter(new Date());//???????????????????????????
			if(bav.getDateStart()==null) bav.setDateStart(new Date());//????????????
			if(bav.getFlagFit()==null) bav.setFlagFit("0");//???????????????
			if(bav.getFlagMedout()==null) bav.setFlagMedout("0");//??????????????????
			if(bav.getFlagPrev()==null) bav.setFlagPrev("0");//????????????
			if(bav.getFlagPrint()==null) bav.setFlagPrint("0");//??????????????????
			if(bav.getFlagSelf()==null) bav.setFlagSelf("0");//???????????????
			if(bav.getFlagStop()==null) bav.setFlagStop("0");//????????????
			if(bav.getFlagThera()==null) bav.setFlagThera("0");//????????????
			if(bav.getFlagErase()==null) bav.setFlagErase("0");//????????????
			if(bav.getFlagStopChk()==null) bav.setFlagStopChk("0");//??????????????????
			if(bav.getFlagEraseChk()==null) bav.setFlagEraseChk("0");//??????????????????
			if(bav.getFlagSign()==null) bav.setFlagSign("1");//????????????,????????????
			if(bav.getFlagDurg()==null) bav.setFlagDurg("0");//????????????
			if(bav.getFlagBase()==null) bav.setFlagBase("0");//???????????????
			if(bav.getFlagBl()==null) bav.setFlagBl("0");//????????????
			if(bav.getFlagNote()==null) bav.setFlagNote("0");//????????????
			if(bav.getFlagCp()==null) bav.setFlagCp("0");//????????????????????????
			if(bav.getEuIntern()==null) bav.setEuIntern("0");//??????????????????,???????????????
			if(bav.getFlagEmer()==null) bav.setFlagEmer("0");//????????????
						
			if(cnt.getFlagBthis()==null) cnt.setFlagBthis("0");//??????????????????????????????
			if(cnt.getFlagLab()==null) cnt.setFlagLab("0");//??????????????????????????????
			if(cnt.getFlagAl()==null) cnt.setFlagAl("0");//????????????
			if(cnt.getFlagPreg()==null) cnt.setFlagPreg("0");//????????????
			if(cnt.getEuStatus()==null) cnt.setEuStatus("0");//???????????????
			if(cnt.getQuanBt()==null) cnt.setQuanBt(bav.getQuan().doubleValue());//??????????????????????????????
			
			if(bav.getCodeApply()!=null){ //???????????????????????????
				cnt.setNote(bav.getCodeApply());
			}
			//????????????????????????
			ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
			Date dt = new Date();
			Date dd = cnPubService.getOutOrdDate(bav.getPkPv());
			if (dd != null && dd.compareTo(dt) < 0) {
				dt = dd;
			}			
			//????????????
			CnOrder co = new CnOrder();  
			BeanUtils.copyProperties(co, bav);			  			
			co.setTs(new Date());			
			//if(StringUtils.isBlank(co.getPkCnord())){
				//co.setPkCnord(NHISUUID.getKeyId());
				//String codeApply=ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_SXSQD,((User)user).getPkOrg());
				co.setCodeApply(bav.getCodeApply());
				//co.setCreator(bav.getPkEmpOrd());
				System.out.println("saveBlood:??????????????????");
				DataBaseHelper.insertBean(co);//????????????
				cnt.setPkCnord(co.getPkCnord());
				cnt.setPkUnitBt(co.getPkUnitDos());//??????
				//cnt.setCreator(bav.getPkEmpOrd());
				//?????????????????????????????????????????????
				System.out.println("saveBlood:?????????????????????");
				DataBaseHelper.insertBean(cnt);
				/*if(StringUtils.isBlank(cnt.getPkOrdbt())) DataBaseHelper.insertBean(cnt);
				else DataBaseHelper.updateBeanByPk(cnt,false);	*/			
			//}
			
			//????????????			
			rest.put("pkCnord", co.getPkCnord());
			rest.put("ordsn",co.getOrdsn());
			rest.put("pkOrdbt", cnt.getPkOrdbt());
			rest.put("codeApply", co.getCodeApply());
		}
		return rest;
	}
	/**
	 * ??????????????????--??????
	 * @param dtAllList
	 * @return
	 */
	public BlPubReturnVo chargeIpBatch(List<BlPubParamVo> dtAllList){
		return ipCgPubService.chargeIpBatch(dtAllList, false);
		
	}

	/**
	 * ???????????????????????????????????????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>>  qryMaxBlood (String param, IUser user){
		Map<String, Object> params = JsonUtil.readValue(param, HashMap.class);
		if(params.get("pkPv")==null || StringUtils.isBlank(params.get("pkPv").toString())){
			throw new BusException("?????????????????????");
		}
		List<Map<String,Object>> maxBl=bloodDao.qryMaxBl(params);
		List<CnTransApprovalVo> appList=bloodDao.qryBlApp(params);

		if(maxBl==null || maxBl.size()==0) return null; //?????????????????????null
		if(appList==null || appList.size()==0) return maxBl;//??????????????????????????????

		List<Map<String,Object>> retMap=new ArrayList<Map<String,Object>>();
		boolean ifApp=true;//??????????????????
		for (Map<String,Object> map : maxBl){
			ifApp=true;
			if(map.get("pkOrd")==null || StringUtils.isBlank(map.get("pkOrd").toString())) continue;
			for (CnTransApprovalVo vo : appList){
				if(map.get("pkOrd").toString().equals(vo.getPkOrd())){
					ifApp=false;
					break;
				}
			}
			if(ifApp){
				retMap.add(map);
			}
		}

		return retMap;
	}

	/**
	 * ??????????????????????????????????????????
	 * @param param
	 * @param user
	 * @return
	 */
	public List<CnTransApprovalVo>  qryBlApp (String param, IUser user){
		Map<String, Object> params = JsonUtil.readValue(param, HashMap.class);
		List<CnTransApprovalVo> appList=bloodDao.qryBlApp(params);
		return appList;
	}

	/**
	 * ?????????????????????
	 * @param param
	 * @param user
	 */
	public void saveBlApp (String param, IUser user) {
		List<CnTransApproval> approvals = JsonUtil.readValue(param, new TypeReference<List<CnTransApproval>>(){});
		if(approvals == null || approvals.size()==0){
			throw new BusException("??????????????????????????????????????????");
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnTransApproval.class),approvals);
	}


	public void saveMaxApp(List<Map<String, Object>> maxList,User user){
		if(maxList==null || maxList.size()==0) return;
		for (Map<String, Object> vo : maxList){
			//?????????????????????
			List<Map<String,Object>> ordMapList=bloodDao.qryMaxOrd(vo);
			if(ordMapList==null || ordMapList.size()==0) continue;
			Map<String,Object> ord=ordMapList.get(0);
			CnTransApproval approval = new CnTransApproval();
			approval.setPkOrg(user.getPkOrg());
			approval.setPkPv(vo.get("pkPv").toString());
			approval.setPkOrd(vo.get("pkOrd").toString());
			approval.setQuanBt(Double.parseDouble(vo.get("quanBt").toString()));
			approval.setPkUnitBt(ord.get("pkUnitBt").toString());
			approval.setDateApply(new Date());
			approval.setPkDept(user.getPkDept());
			if(CommonUtils.isEmptyString(approval.getFlagChkHead()))
			{
				approval.setFlagChkHead("0");
			}
			if(CommonUtils.isEmptyString(approval.getFlagChkMm()))
			{
				approval.setFlagChkMm("0");
			}
			if(CommonUtils.isEmptyString(approval.getFlagChkTrans()))
			{
				approval.setFlagChkTrans("0");
			}
			DataBaseHelper.insertBean(approval);
		}
	}

	/*************************************???????????? ????????????????????? start**************************************/
	//?????????????????????????????????????????????
	private boolean checkPvInfo(String pkPv){
		boolean ok=false;//??????????????????
		String day=ApplicationUtils.getSysparam("CN0107", false);
		String sql="Select PV.* From pv_encounter pv \n" +
				"Inner Join bd_dictattr dict On pv.pk_dept=dict.pk_dict \n" +
				"Where  pv.pk_pv=? And dict.code_attr='0605' And dict.val_attr='1'";
		PvEncounter pvEncounter=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{pkPv});
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(day) && pvEncounter!=null){
			int dayIp= DateUtils.getDateSpace(pvEncounter.getDateBegin(), new Date());
			int dayI= Integer.parseInt(day);
			//??????
			if(dayI>0 && dayI<dayIp) ok=true;
		}
		return ok;
	}
	/*************************************???????????? ????????????????????? end**************************************/
}
