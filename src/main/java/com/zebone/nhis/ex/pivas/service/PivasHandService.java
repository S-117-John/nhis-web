package com.zebone.nhis.ex.pivas.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdPivas;
import com.zebone.nhis.ex.pivas.dao.PivasHandMapper;
import com.zebone.nhis.ex.pivas.vo.CnOrderCheckVo;
import com.zebone.nhis.ex.pivas.vo.PivasAndApplyParam;
import com.zebone.nhis.scm.pub.service.IpDeDrugPubService;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/***
 * 打包处理、入舱处理、配液处理 service
 * 
 * @author wangpeng
 * @date 2016年12月15日
 *
 */
@Service
public class PivasHandService {
	
	@Resource
	private PivasHandMapper pivasHandMapper;
	
	@Resource
	private IpDeDrugPubService ipDeDrugPubService;
	
	@Resource
	private IpCgPubService ipCgPubService;
	
	/***
	 * 交易号：005003003003<br>
	 * 配液处理
	 *  
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年12月16日
	 */
	public void updateExPdPivasPy(String param, IUser user){
		User u = UserContext.getUser();
		PivasAndApplyParam pivasParam = JsonUtil.readValue(param, PivasAndApplyParam.class);
		String[] pkPdpivass = pivasParam.getPkPdpivass(); //静配记录明细主键
		
		//处理静配记录
		int pivasLen = pkPdpivass.length;
		if(pivasLen > 0){
			List<ExPdPivas> pivasList = new ArrayList<ExPdPivas>();
			for(int i=0;i<pivasLen;i++){
				ExPdPivas pivas = new ExPdPivas();
				pivas.setPkPdpivas(pkPdpivass[i]);
				pivas.setEuStatus("3");
				pivas.setFlagAdmix("1");
				pivas.setDateAdmix(new Date());
				pivas.setPkEmpAdmix(u.getPkEmp());
				pivas.setNameEmpAdmix(u.getNameEmp());
				pivas.setModifier(u.getPkEmp());
				pivas.setTs(new Date());
				pivasList.add(pivas);
			}
			String sql = "update ex_pd_pivas "
					+ "set eu_status =:euStatus, "
					+ "flag_admix =:flagAdmix, "
					+ "date_admix =:dateAdmix, "
					+ "pk_emp_admix =:pkEmpAdmix, "
					+ "name_emp_admix =:nameEmpAdmix, "
					+ "modifier =:modifier, "
					+ "ts =:ts "
					+ "where pk_pdpivas =:pkPdpivas";
			DataBaseHelper.batchUpdate(sql, pivasList);
		}		
	}
	
	/***
	 * 交易号：005003003004<br>
	 * 医嘱(包含打包、入舱)停发
	 * 
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年12月16日
	 */
	public void updateExPdApplyAndPivas(String param, IUser user){
		User u = UserContext.getUser();
		PivasAndApplyParam pivasParam = JsonUtil.readValue(param, PivasAndApplyParam.class);
		String[] pkPdapdts = pivasParam.getPkPdapdts(); //领药明细主键
		String[] pkPdpivass = pivasParam.getPkPdpivass(); //静配记录明细主键
		String reasonStop = pivasParam.getReasonStop();
		
		int apdtLen = pkPdapdts.length;
		int pivasLen = pkPdpivass.length;
		//处理发药单明细
		if(apdtLen > 0){			
			//校验
			List<CnOrderCheckVo> checkList = pivasHandMapper.getCnOrderCheckVoList(pkPdpivass);	
			for(CnOrderCheckVo checkVo : checkList){
				if(("1".equals(checkVo.getFlagStop()) && (checkVo.getDateStop() != null && checkVo.getTimePlan() != null && checkVo.getDateStop().before(checkVo.getTimePlan()))) || ("1".equals(checkVo.getFlagErase()))){
					//医嘱为停止且停止时间小于执行时间  或者 医嘱状态为作废 ，可以停发
				}else{
					throw new BusException("只有医嘱状态为停止并且停止时间小于执行时间，或者医嘱状态为作废的医嘱可以停发！");
				}
			}
			
			List<ExPdApplyDetail> apdtList = new ArrayList<ExPdApplyDetail>();
			for(int i=0;i<apdtLen;i++){
				ExPdApplyDetail apdt = new ExPdApplyDetail();
				apdt.setPkPdapdt(pkPdapdts[i]);
				apdt.setFlagStop("1");
				apdt.setReasonStop(reasonStop);
				apdt.setPkEmpStop(u.getPkEmp());
				apdt.setNameEmpStop(u.getNameEmp());
				apdt.setModifier(u.getPkEmp());
				apdt.setTs(new Date());
				apdtList.add(apdt);
			}
			String sql = "update ex_pd_apply_detail "
					+ "set flag_stop =:flagStop, "
					+ "reason_stop =:reasonStop, "
					+ "pk_emp_stop =:pkEmpStop, "
					+ "name_emp_stop =:nameEmpStop, "
					+ "modifier =:modifier, "
					+ "ts =:ts "
					+ "where pk_pdapdt =:pkPdapdt";
			DataBaseHelper.batchUpdate(sql, apdtList);					
		}
		//处理静配记录
		if(pivasLen > 0){
			List<ExPdPivas> pivasList = new ArrayList<ExPdPivas>();
			for(int i=0;i<pivasLen;i++){
				ExPdPivas pivas = new ExPdPivas();
				pivas.setPkPdpivas(pkPdpivass[i]);
				pivas.setFlagStop("1");
				pivas.setModifier(u.getPkEmp());
				pivas.setTs(new Date());
				pivasList.add(pivas);
			}
			String sql = "update ex_pd_pivas "
					+ "set flag_stop =:flagStop, "
					+ "modifier =:modifier, "
					+ "ts =:ts "
					+ "where pk_pdpivas =:pkPdpivas";
			DataBaseHelper.batchUpdate(sql, pivasList);
		}
	}
	
	/***
	 * 交易号：005003003005<br>
	 * 打包处理
	 *  
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年12月16日
	 */
	public void updateExPdPivasDb(String param, IUser user){
		User u = UserContext.getUser();
		PivasAndApplyParam pivasParam = JsonUtil.readValue(param, PivasAndApplyParam.class);
		String[] pkPdapdts = pivasParam.getPkPdapdts(); //领药明细主键
		String[] pkPdpivass = pivasParam.getPkPdpivass(); //静配记录明细主键
		
		int apdtLen = pkPdapdts.length;
		if(apdtLen > 0){
			//校验
			List<CnOrderCheckVo> checkList = pivasHandMapper.getCnOrderCheckVoList(pkPdpivass);	
			for(CnOrderCheckVo checkVo : checkList){
				if("1".equals(checkVo.getFlagErase())){ // 作废标志
					throw new BusException("医嘱已作废，不能入舱！");
				}
				if(checkVo.getDateStop() != null && checkVo.getTimePlan() != null && checkVo.getDateStop().before(checkVo.getTimePlan())){
					throw new BusException("停止时间小于执行时间，不能入舱！");
				}
			}
			
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("pkPdapdts", pkPdapdts);
			mapParam.put("pkPdpivass", pkPdpivass);
			List<ExPdApplyDetail> apdtList = pivasHandMapper.getExPdApplyDetailByPkPdapdts(mapParam);
			for(ExPdApplyDetail d : apdtList){
				d.setQuanPack(d.getPivasQuanPack());
				d.setQuanMin(d.getPivasQuanMin());
			}
			//TODO 调用发药接口,需要返回发药主键ex_pd_de.pk_pdde 
			List<PdDeDrugVo> pList = ipDeDrugPubService.mergeIpDeDrug(apdtList,"1","1",null,new Date());
			Map<String, String> apdtMap = new HashMap<String, String>(); //pkPdapdt：pkPdde
			for(PdDeDrugVo vo : pList){
				apdtMap.put(vo.getPkPdapdt(), vo.getPkPdde());
			}
			
			//处理静配记录
			int pivasLen = pkPdpivass.length;
			if(pivasLen > 0){
				List<ExPdPivas> pivasList = pivasHandMapper.getExPdPivasListByPkPdpivas(pkPdpivass);
				for(ExPdPivas pivas : pivasList){
					pivas.setEuStatus("5");
					pivas.setFlagPack("1");
					pivas.setDatePack(new Date());
					pivas.setPkEmpPack(u.getPkEmp());
					pivas.setNameEmpPack(u.getNameEmp());
					pivas.setPkPdde(apdtMap.get(pivas.getPkPdapdt())); //发药主键
					pivas.setModifier(u.getPkEmp());
					pivas.setTs(new Date());
				}
				String sql = "update ex_pd_pivas "
						+ "set eu_status =:euStatus, "
						+ "flag_pack =:flagPack, "
						+ "date_pack =:datePack, "
						+ "pk_emp_pack =:pkEmpPack, "
						+ "name_emp_pack =:nameEmpPack, "
						+ "pk_pdde =:pkPdde, "
						+ "modifier =:modifier, "
						+ "ts =:ts "
						+ "where pk_pdpivas =:pkPdpivas";
				DataBaseHelper.batchUpdate(sql, pivasList);
			}			
		}		
	}
	
	/***
	 * 交易号：005003003006<br>
	 * 出舱处理
	 *  
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年12月16日
	 */
	public void updateExPdPivasCc(String param, IUser user){
		User u = UserContext.getUser();
		PivasAndApplyParam pivasParam = JsonUtil.readValue(param, PivasAndApplyParam.class);
		String[] pkPdpivass = pivasParam.getPkPdpivass(); //静配记录明细主键		
		
		//处理静配记录
		int pivasLen = pkPdpivass.length;
		if(pivasLen > 0){
			//校验 如果eustatus<3就提示还没有配液
			List<CnOrderCheckVo> checkList = pivasHandMapper.getCnOrderCheckVoList(pkPdpivass);
			for(CnOrderCheckVo checkVo : checkList){
				if(checkVo.getEuStatus() != null && "3".compareTo(checkVo.getEuStatus()) > 0){
					throw new BusException("没有配液，不能出舱！");
				}
			}
			
			List<ExPdPivas> pivasList = new ArrayList<ExPdPivas>();
			for(int i=0;i<pivasLen;i++){
				ExPdPivas pivas = new ExPdPivas();
				pivas.setPkPdpivas(pkPdpivass[i]);
				pivas.setEuStatus("4");
				pivas.setFlagOut("1");
				pivas.setDateOut(new Date());
				pivas.setPkEmpOut(u.getPkEmp());
				pivas.setNameEmpOut(u.getNameEmp());
				pivas.setModifier(u.getPkEmp());
				pivas.setTs(new Date());
				pivasList.add(pivas);
			}
			String sql = "update ex_pd_pivas "
					+ "set eu_status =:euStatus, "
					+ "flag_out =:flagOut, "
					+ "date_out =:dateOut, "
					+ "pk_emp_out =:pkEmpOut, "
					+ "name_emp_out =:nameEmpOut, "
					+ "modifier =:modifier, "
					+ "ts =:ts "
					+ "where pk_pdpivas =:pkPdpivas";
			DataBaseHelper.batchUpdate(sql, pivasList);
		}		
	}
	
	/***
	 * 交易号：005003003008<br>
	 * 入舱处理<br>
	 * <pre>
	 * 1、校验：
	 * 1.1、停止时间小于执行时间时，不能入舱；
	 * 1.2、医嘱已作废的，不能入舱；
	 * </pre>
	 *  
	 * @param  param
	 * @param  user
	 * @return void
	 * @throws 
	 *
	 * @author wangpeng
	 * @date 2016年12月16日
	 */
	public void updateExPdPivasRc(String param, IUser user){
		User u = UserContext.getUser();
		PivasAndApplyParam pivasParam = JsonUtil.readValue(param, PivasAndApplyParam.class);
		String[] pkPdapdts = pivasParam.getPkPdapdts(); //领药明细主键
		String[] pkPdpivass = pivasParam.getPkPdpivass(); //静配记录明细主键
		
		int apdtLen = pkPdapdts.length;
		int pivasLen = pkPdpivass.length;
		if(apdtLen > 0){
			//校验
			List<CnOrderCheckVo> checkList = pivasHandMapper.getCnOrderCheckVoList(pkPdpivass);	
			for(CnOrderCheckVo checkVo : checkList){
				if("1".equals(checkVo.getFlagErase())){ // 作废标志
					throw new BusException("医嘱已作废，不能入舱！");
				}
				if(checkVo.getDateStop() != null && checkVo.getTimePlan() != null && checkVo.getDateStop().before(checkVo.getTimePlan())){
					throw new BusException("停止时间小于执行时间，不能入舱！");
				}
			}
			Map<String, Object> mapP = new HashMap<String, Object>();
			mapP.put("pkPdapdts", pkPdapdts);
			mapP.put("pkPdpivass", pkPdpivass);
			List<ExPdApplyDetail> apdtList = pivasHandMapper.getExPdApplyDetailByPkPdapdts(mapP);
			for(ExPdApplyDetail d : apdtList){
				d.setQuanPack(d.getPivasQuanPack());
				d.setQuanMin(d.getPivasQuanMin());
			}
			//调用发药接口,需要返回发药主键ex_pd_de.pk_pdde 
			List<PdDeDrugVo> pList = ipDeDrugPubService.mergeIpDeDrug(apdtList,"1","1",null,new Date());
			Map<String, String> apdtMap = new HashMap<String, String>(); //pkPdapdt：pkPdde
			for(PdDeDrugVo vo : pList){
				apdtMap.put(vo.getPkPdapdt(), vo.getPkPdde());
			}
			
			//处理静配记录
			if(pivasLen > 0){
				List<ExPdPivas> pivasList = pivasHandMapper.getExPdPivasListByPkPdpivas(pkPdpivass);
				for(ExPdPivas pivas : pivasList){
					pivas.setEuStatus("2");
					pivas.setFlagIn("1");
					pivas.setDateIn(new Date());
					pivas.setPkEmpIn(u.getPkEmp());
					pivas.setNameEmpIn(u.getNameEmp());
					pivas.setPkPdde(apdtMap.get(pivas.getPkPdapdt())); //发药主键
					pivas.setModifier(u.getPkEmp());
					pivas.setTs(new Date());
				}
				String sql = "update ex_pd_pivas "
						+ "set eu_status =:euStatus, "
						+ "flag_in =:flagIn, "
						+ "date_in =:dateIn, "
						+ "pk_emp_in =:pkEmpIn, "
						+ "name_emp_in =:nameEmpIn, "
						+ "pk_pdde =:pkPdde, "
						+ "modifier =:modifier, "
						+ "ts =:ts "
						+ "where pk_pdpivas =:pkPdpivas";
				DataBaseHelper.batchUpdate(sql, pivasList);
				
				//TODO 根据静配记录所属的静配分类获取其静配收费项目   如果返回值不为空，调用患者记费接口，按组执行记费
				//ipCgService.chargeInBatch
				Map<String, Object> mapParam = new HashMap<String, Object>();
				mapParam.put("pkOrg", u.getPkOrg());
				mapParam.put("pkPdpivass", pkPdpivass);
				List<BlPubParamVo> blList = pivasHandMapper.getBlPubParamVoList(mapParam);
				for(BlPubParamVo vo : blList){
					vo.setPkOrg(u.getPkOrg());
					vo.setPkOrgEx(u.getPkOrg()); //执行机构
					vo.setPkDeptEx(u.getPkDept()); //执行科室
					vo.setPkDeptCg(u.getPkDept()); //记费科室
					vo.setPkEmpCg(u.getPkEmp()); //计费人
					vo.setNameEmpCg(u.getNameEmp());
					vo.setFlagPd("0"); //非药品
					vo.setQuanCg(1.0); //数量
					vo.setDateHap(new Date());
				}
				if(CollectionUtils.isNotEmpty(blList)){
					ipCgPubService.chargeIpBatch(blList,false);
				}
			}
		}		
	}

}
