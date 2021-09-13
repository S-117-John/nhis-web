package com.zebone.nhis.webservice.sd.service;

import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdtype;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.sd.vo.*;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmerService {
	private Logger logger = LoggerFactory.getLogger("com.zebone");
	private User u = new User();
	String prefix = "";
	@Resource
	private BdSnService bdSnService;

	public void saveOrders(OrderInfo info) {

		List<CnOrderVo> cnOrderList = new ArrayList<CnOrderVo>();
		cnOrderList = info.getCnOrderList();

		if (cnOrderList != null && cnOrderList.size() > 0) {
			// 患者信息查询
			String codePv = cnOrderList.get(0).getCodePv();
			PvEncounter pv = new PvEncounter();
			pv = DataBaseHelper.queryForBean(
					"select * from pv_encounter where code_pv=? and del_flag ='0' ",
					PvEncounter.class, codePv);

			if (pv == null) {
				throw new BusException("根据就诊主键未查询到患者本次就诊记录！");
			}
			
			setUserInfo(cnOrderList.get(0).getCodeModifier(),cnOrderList.get(0).getCodeDept());
			//保存诊断信息
			saveDiag(info,pv);
			//费用明细处理
			String codeCg = ApplicationUtils.getCode("0601");
			int sortno = 1;
			for (BlOpDtVo dt : info.getBlOpDtList()) {
				dt.setCodeCg(codeCg);
				dt.setSortno(sortno);
				sortno++;
			}

			// 循环保存每个医嘱对应的相关表
			for (CnOrderVo cnOrder : cnOrderList) {
				if ("R".equals(cnOrder.getRowStatus())) {// 删除医嘱
					removeOrderRelate(cnOrder);
				} else if ("N".equals(cnOrder.getRowStatus())) {// 保存医嘱
					cnOrder.setEuPvtype("2");
					cnOrder.setPkPv(pv.getPkPv());
					cnOrder.setPkPi(pv.getPkPi());
					cnOrder.setPkDept(u.getPkDept());
					cnOrder.setPkEmpOrd(u.getPkEmp());
					cnOrder.setNameEmpOrd(u.getNameEmp());
					cnOrder.setPkEmpInput(u.getPkEmp());
					cnOrder.setNameEmpInput(u.getNameEmp());
					cnOrder.setDelFlag("0");

					codeConverPk(cnOrder);
					saveOrderRelate(cnOrder, info);
				} else {
					throw new BusException("医嘱行状态（RowStatus）为空！");
				}
			}
		}

		List<CnOrderVo> savOrders = new ArrayList<CnOrderVo>();
		for (CnOrderVo ord : cnOrderList) {
			if ("N".equals(ord.getRowStatus()))
				savOrders.add(ord);
		}
		if (savOrders != null && savOrders.size() > 0) {
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(CnOrder.class), savOrders);
			logger.info("急诊调用保存医嘱阶段 ：医嘱数据保存 ");
		}

	}
	private void saveDiag(OrderInfo info,PvEncounter pv){
		PvDiagVo diagN=info.getPvDiag().get(0);
		if(pv!=null)
		{
			//校验就诊号是否一致
			if(!pv.getCodePv().equals(diagN.getCodePv()))
			{
				throw new BusException("医嘱就诊号"+pv.getCodePv()+"与诊断就诊号"+diagN.getCodePv()+"不一致，操作失败");
			}
			//查询患者诊断信息
			PvDiagVo pvdiag=DataBaseHelper.queryForBean("select * from pv_diag where flag_maj='1' and pk_pv= ? ",PvDiagVo.class,pv.getPkPv());
		
			if(pvdiag!=null&&pvdiag.getPkEmpDiag()!=null)//已存在主诊断信息,不做处理
			{
				return;
			}
					
			if("N".equals(diagN.getRowStatus()))//新增患者主诊断信息
			{				
				diagN.setFlagMaj("1");
				diagN.setPkOrg(u.getPkOrg());
				diagN.setPkPv(pv.getPkPv());
				ApplicationUtils.setDefaultValue(diagN, false);
				if(diagN.getCodeEmp()!=null)
				{
					BdOuEmployee emp = DataBaseHelper.queryForBean(
							"select * from bd_ou_employee where code_emp=? and del_flag='0' ",
							BdOuEmployee.class, diagN.getCodeEmp());
					if(emp==null)
					{
						throw new BusException("未获取到诊断开立医生信息："+diagN.getCodeEmp());
					}
					diagN.setPkEmpDiag(emp.getPkEmp());
					diagN.setNameEmpDiag(emp.getNameEmp());									
				}
				if(diagN.getCodeDiag()!=null)
				{
					BdTermDiag bdCndiag= DataBaseHelper.queryForBean(
							"select * from bd_term_diag where diagcode=? and del_flag='0' ",
							BdTermDiag.class, diagN.getCodeDiag());
					if(bdCndiag==null) 
					{
						throw new BusException("未获取到诊断信息："+diagN.getCodeDiag());
					}
					diagN.setNameDiag(bdCndiag.getDiagname());
					diagN.setCodeIcd(bdCndiag.getDiagcode());
				}
				PvDiag diag=new PvDiag();
				ApplicationUtils.copyProperties(diag, diagN);
				ApplicationUtils.setDefaultValue(diag, false);
				DataBaseHelper.insertBean(diag);
				
			}else if("R".equals(diagN.getRowStatus()))//移除患者主诊断信息，暂注释
			{
				//DataBaseHelper.execute("delete from CN_PRESCRIPTION where pk_pres=?",diagN.getPkPvdiag());
			}
		}
							
		
	}

	/**
	 * 保存明细、处方、申请单
	 * @param cnOrder
	 * @param info
	 */
	private void saveOrderRelate(CnOrder cnOrder, OrderInfo info) {
		String pkCnord = cnOrder.getPkCnord();

		List<CnRisApply> cnRisApplyList = info.getCnRisApplyList();
		List<CnLabApply> cnLabApplyList = info.getCnLabApplyList();
		List<CnPrescription> cnPrescriptionList = info.getCnPrescriptionList();
		List<BlOpDtVo> blOpDtList = info.getBlOpDtList();

		/** 费用明细 */
		List<BlOpDtVo> OpDtListThemp = new ArrayList<BlOpDtVo>();
		if (blOpDtList != null && blOpDtList.size() > 0) {
			for (BlOpDtVo blOpDt : blOpDtList) {
				if (pkCnord.equals(blOpDt.getPkCnord())) {
					OpDtListThemp.add(blOpDt);
				}
			}
		}
		if (OpDtListThemp != null && OpDtListThemp.size() > 0) {
			for (BlOpDtVo blOpDt : OpDtListThemp) {
				if (CommonUtils.isEmptyString(blOpDt.getCodeBill())) {
					throw new BusException("费用明细账单码为空！");
				}
				blOpDt.setPkPv(cnOrder.getPkPv());
				blOpDt.setPkPi(cnOrder.getPkPi());
				blOpDt.setPkDeptEx(cnOrder.getPkDeptExec());
				blOpDt.setPkDeptCg(cnOrder.getPkDept());
				blOpDt.setPkDeptApp(u.getPkDept());
				blOpDt.setPkEmpApp(u.getPkEmp());
				blOpDt.setNameEmpApp(u.getNameEmp());
				blOpDt.setPkEmpCg(u.getPkEmp());
				blOpDt.setNameEmpCg(u.getNameEmp());
				blOpDt.setPkOrg(u.getPkOrg());
				blOpDt.setPkOrgApp(u.getPkOrg());
				blOpDt.setPkOrgEx(u.getPkOrg());
				blOpDt.setPkDisc(getHpByCode(blOpDt.getCodeDisc()));
				blOpDt.setPkUnit(getPkUnitByCode(blOpDt.getCodeUnit()));
				if(blOpDt.getPkUnit()==null)
				{
					throw new BusException("未获取到收费明细的单位编码！");
				}
				blOpDt.setPkUnitPd(getPkUnitByCode(blOpDt.getCodeUnitPd()));

				ApplicationUtils.setDefaultValue(blOpDt, true);

				if (blOpDt.getItemCode() != null) {// 药品
					if ("1".equals(blOpDt.getFlagPd())) {
						BdPd pd = DataBaseHelper.queryForBean(
								"select * from bd_pd where code=? and del_flag='0' ",
								BdPd.class, blOpDt.getItemCode());
						blOpDt.setPkItem(pd.getPkPd());
						blOpDt.setPkPd(pd.getPkPd());
						blOpDt.setPkItemcate(pd.getPkItemcate());
						if (CommonUtils.isEmptyString(blOpDt.getPkDeptEx()))
							throw new BusException("未传入药品对应的发药科室！");

					} else {// 收费项目
						BdItem item = DataBaseHelper.queryForBean(
								"select * from bd_item where code=? and del_flag='0' ",
								BdItem.class, blOpDt.getItemCode());
						blOpDt.setPkItem(item.getPkItem());
						blOpDt.setPkItemcate(item.getPkItemcate());
					}
				} else {
					throw new BusException("未传入收费明细的账单码！");
				}
			}
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(BlOpDt.class), OpDtListThemp);
			logger.info("急诊调用保存医嘱阶段 ：费用明细保存 ");
		}

		/** 检查申请单 */
		List<CnRisApply> risApplyListThemp = new ArrayList<CnRisApply>();
		if (cnRisApplyList != null && cnRisApplyList.size() > 0) {
			for (CnRisApply cnRisApply : cnRisApplyList) {
				if (pkCnord.equals(cnRisApply.getPkCnord())) {
					risApplyListThemp.add(cnRisApply);
				}
			}
		}
		if (risApplyListThemp != null && risApplyListThemp.size() > 0) {
			for (CnRisApply risApply : risApplyListThemp) {
				risApply.setCodeApply(prefix + risApply.getCodeApply());
				if (CommonUtils.isEmptyString(risApply.getPkCnord())) {
					throw new BusException("检查申请单医嘱主键为空！");
				}
				risApply.setPkPv(cnOrder.getPkPv());
				risApply.setPkPi(cnOrder.getPkPi());
				risApply.setPkEmpAppo(u.getPkEmp());
				risApply.setPkEmpInput(u.getPkEmp());
				risApply.setPkDeptExec(cnOrder.getPkDeptExec());
				ApplicationUtils.setDefaultValue(risApply, true);
			}
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(CnRisApply.class),
					risApplyListThemp);
			logger.info("急诊调用保存医嘱阶段 ：检查单保存 ");
		}

		/** 检验申请单 */
		List<CnLabApply> labApplyListThemp = new ArrayList<CnLabApply>();
		if (cnLabApplyList != null && cnLabApplyList.size() > 0) {
			for (CnLabApply cnlabApply : cnLabApplyList) {
				if (pkCnord.equals(cnlabApply.getPkCnord())) {
					labApplyListThemp.add(cnlabApply);
				}
			}
		}
		if (labApplyListThemp.size() > 0) {
			for (CnLabApply labApply : labApplyListThemp) {
				labApply.setCodeApply(prefix + labApply.getCodeApply());
				if (CommonUtils.isEmptyString(labApply.getPkCnord())) {
					throw new BusException("检验申请单医嘱主键为空！");
				}
				labApply.setPkPv(cnOrder.getPkPv());
				labApply.setPkPi(cnOrder.getPkPi());
				labApply.setPkDeptExec(cnOrder.getPkDeptExec());
				labApply.setPkEmpInput(u.getPkEmp());
				ApplicationUtils.setDefaultValue(labApply, true);
			}
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(CnLabApply.class),
					labApplyListThemp);
			logger.info("急诊调用保存医嘱阶段 ：检验单保存 ");
		}

		/** 处方 */
		List<CnPrescription> prestionThemp = new ArrayList<CnPrescription>();
		if (cnPrescriptionList != null && cnPrescriptionList.size() > 0) {
			for (CnPrescription cnPres : cnPrescriptionList) {
				if (cnOrder.getPkPres() != null
						&& cnOrder.getPkPres().equals(cnPres.getPkPres())) {
					prestionThemp.add(cnPres);
				}
			}
		}
		if (prestionThemp != null && prestionThemp.size() > 0) {
			for (CnPrescription cnpres : prestionThemp) {

				DataBaseHelper.execute(
						"delete from CN_PRESCRIPTION where pk_pres=?",
						cnpres.getPkPres());
				cnpres.setPresNo(prefix + cnpres.getPresNo());
				cnpres.setPkPv(cnOrder.getPkPv());
				cnpres.setPkPi(cnOrder.getPkPi());
				cnpres.setPkDeptExec(cnOrder.getPkDeptExec());
				cnpres.setPkEmpOrd(u.getPkEmp());
				cnpres.setPkDept(u.getPkDept());
				cnpres.setNameEmpOrd(u.getNameEmp());
				cnpres.setCreateTime(new Date());
				// 急诊无草药处方，增加校验
				if (cnpres.getDtPrestype() != null
						&& cnpres.getDtPrestype() == "02") {
					throw new BusException("操作失败，草药保存失败！");
				}

			}
			DataBaseHelper.batchUpdate(
					DataBaseHelper.getInsertSql(CnPrescription.class),
					prestionThemp);
			logger.info("急诊调用保存医嘱阶段 ：处方单保存 ");
		}

		logger.info("急诊调用保存医嘱阶段 ：保存成功");
	}

	/**
	 * 移除医嘱相关信息
	 * @param cnOrder
	 */
	private void removeOrderRelate(CnOrder cnOrder) {

		int ordCnt = DataBaseHelper.queryForScalar(
				"select count(*) from cn_order where pk_cnord=?  ",
				Integer.class, cnOrder.getPkCnord());
		if (ordCnt == 0) {
			throw new BusException("操作失败，未检索到医嘱信息，请刷新！");
		}

		int dtCnt = DataBaseHelper.queryForScalar(
				"select count(1) from bl_op_dt where pk_cnord=? and flag_settle='1' and del_flag='0'",
				Integer.class, cnOrder.getPkCnord());
		if (dtCnt > 0) {
			throw new BusException("操作失败，项目已缴费！");
		}
		DataBaseHelper.execute("delete from bl_op_dt where pk_cnord=?",
				cnOrder.getPkCnord());
		DataBaseHelper.execute(
				"delete from cn_order where pk_cnord=? ",
				cnOrder.getPkCnord());
		DataBaseHelper.execute("delete from cn_ris_apply where pk_cnord=?",
				cnOrder.getPkCnord());
		DataBaseHelper.execute("delete from cn_lab_apply where pk_cnord=?",
				cnOrder.getPkCnord());
		DataBaseHelper.execute("delete from cn_prescription where pk_pres=?",
				cnOrder.getPkPres());

		logger.info("急诊调用 ：删除医嘱相关表 ");
	}

	// 获取医嘱序号
	private int getOsdn() {
		Integer id = bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1,
				UserContext.getUser());
		return id;
	}

	// 组装用户信息
	private void setUserInfo(String codeEmp, String codeDept) {
		if (codeDept != null) {
			BdOuDept dept = DataBaseHelper.queryForBean(
					"select * from bd_ou_dept where code_dept=? and del_flag='0' ",
					BdOuDept.class, codeDept);
			u.setPkDept(dept.getPkDept());
		}
		if (codeEmp != null) {
			BdOuEmployee emp = DataBaseHelper.queryForBean(
					"select * from bd_ou_employee where code_emp=? and del_flag='0' ",
					BdOuEmployee.class, codeEmp);
			u.setPkEmp(emp.getPkEmp());
			u.setNameEmp(emp.getNameEmp());
			u.setCodeEmp(emp.getCodeEmp());
			u.setPkOrg(emp.getPkOrg());
		}
		UserContext.setUser(u);
	}

	private String getPkUnitByCode(String codeUnit) {
		if (codeUnit != null) {
			String pkUnit = DataBaseHelper.queryForScalar(
					"select pk_unit from bd_unit where code=? ", String.class,
					codeUnit);
			return pkUnit;
		}
		return null;
	}

	private String getHpByCode(String codeHp) {
		if (codeHp != null) {
			String pkHp = DataBaseHelper.queryForScalar(
					"select pk_hp from bd_hp where code=? ", String.class,
					codeHp);
			return pkHp;
		}
		return null;
	}

	/**
	 * code转pk 单位、执行科室、医嘱项目
	 */
	private void codeConverPk(CnOrderVo cnOrder) {
		if (cnOrder == null) {
			return;
		}
		cnOrder.setPkUnit(getPkUnitByCode(cnOrder.getCodeUnit()));
		cnOrder.setPkUnitDos(getPkUnitByCode(cnOrder.getCodeUnitDos()));
		cnOrder.setPkUnitCg(getPkUnitByCode(cnOrder.getCodeUnitCg()));
		if (cnOrder.getCodeDeptExec() != null) {

			BdOuDept dept = DataBaseHelper.queryForBean(
					"select * from bd_ou_dept where code_dept=? and del_flag='0' ",
					BdOuDept.class, cnOrder.getCodeDeptExec());
			if (dept == null) {
				throw new BusException("未检索到执行科室：" + cnOrder.getCodeDeptExec());
			}
			cnOrder.setPkDeptExec(dept.getPkDept());
		}
		if ("1".equals(cnOrder.getFlagDurg())) {
			BdPd pd = DataBaseHelper.queryForBean(
					"select * from bd_pd where code=? and del_flag='0' ",
					BdPd.class, cnOrder.getCodeOrd());
			if (pd == null) {
				throw new BusException("未检索到医嘱项目：" + cnOrder.getCodeOrd());
			}
			cnOrder.setPkOrd(pd.getPkPd());
			
			BdOrdtype ordtype=DataBaseHelper.queryForBean(
					"select * from bd_ordtype where pk_Ordtype=? and del_flag='0' ",
					BdOrdtype.class, pd.getPkOrdtype());	
			if(ordtype!=null){
			cnOrder.setCodeOrdtype(ordtype.getCode());
			}

		} else {
			BdOrd ord = DataBaseHelper.queryForBean(
					"select * from bd_ord where code=? and del_flag='0' ",
					BdOrd.class, cnOrder.getCodeOrd());
			if (ord == null) {
				throw new BusException("未检索到医嘱项目：" + cnOrder.getCodeOrd());
			}
			cnOrder.setPkOrd(ord.getPkOrd());
			cnOrder.setCodeOrdtype(ord.getCodeOrdtype());
		}

	}
}
