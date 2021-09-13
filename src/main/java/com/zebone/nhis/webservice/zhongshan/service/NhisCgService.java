package com.zebone.nhis.webservice.zhongshan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class NhisCgService {

	@Resource
	private IpCgPubService ipCgPubService;//住院记费公共类
	
	@Resource
	private OpCgPubService opCgPubService;//门诊记费公共类
	
	@Resource
	private BdSnService bdSnService;//获取医嘱序号  
	
	/**
	 * 1.1 记费
	 * @param param
	 * @param pv 患者就诊
	 * @param item 记费项目
	 * @param empOper 操作人
	 * @param deptEx 执行科室
	 * @param empApp 开立人
	 * @param deptApp 开立科室
	 * @param deptNsApp 开立病区
	 * @param deptCg 记费科室
	 * @return
	 */
	public String BlCg(Map<String, Object> param,  PvEncounter pv, BdItem item, 
			BdOuEmployee empOper, BdOuDept deptEx, BdOuEmployee empApp, BdOuDept deptApp, 
			BdOuDept deptNsApp, BdOuDept deptCg , CnOrder ord2) {
		
		List<BlPubParamVo> cgParams = new ArrayList<BlPubParamVo>();
		BlPubParamVo cgParam = new BlPubParamVo();
		cgParam.setPkOrg(pv.getPkOrg());
		cgParam.setPkItem(item.getPkItem());//记费项目
		cgParam.setPkPi(pv.getPkPi());//患者主键
		cgParam.setPkPv(pv.getPkPv());//患者就诊主键
		cgParam.setEuPvType(pv.getEuPvtype());//就诊类型
		cgParam.setFlagPd("0");
		cgParam.setFlagPv("0");
		cgParam.setInfantNo("0");
		cgParam.setDateHap(null != ord2 ? ord2.getDateStart() : new Date());
		cgParam.setPkOrgApp(deptApp == null ? pv.getPkOrg() : deptApp.getPkOrg());
		cgParam.setPkDeptApp(deptApp == null ? pv.getPkDept() : deptApp.getPkDept());
		cgParam.setPkDeptNsApp(deptNsApp == null ? pv.getPkDeptNs() : deptNsApp.getPkDept());
		cgParam.setPkEmpApp(empApp.getPkEmp());
		cgParam.setNameEmpApp(empApp.getNameEmp());
		cgParam.setPkOrgEx(deptEx.getPkOrg());
		cgParam.setPkDeptEx(deptEx.getPkDept());
		cgParam.setPkEmpCg(empOper.getPkEmp());
		cgParam.setNameEmpCg(empOper.getNameEmp());
		cgParam.setPkDeptCg(deptCg == null ? deptEx.getPkDept() : deptCg.getPkDept());
		String price = param.get("price") == null || CommonUtils.isEmptyString(param.get("price").toString()) ? "0.00" :param.get("price").toString(); 
		cgParam.setPrice(Double.parseDouble(price));
		String quanCg = param.get("quan_cg") == null || CommonUtils.isEmptyString(param.get("quan_cg").toString())? "0" :param.get("quan_cg").toString(); 
		cgParam.setQuanCg(Double.parseDouble(quanCg));
		cgParam.setBarcode(param.get("bar_code")==null?"":param.get("bar_code").toString());
		cgParams.add(cgParam);
		
		BlPubReturnVo cgRtns =  new BlPubReturnVo();
		//6.执行记费(单条记费)
		if("3".equals(pv.getEuPvtype()))//住院
			cgRtns = ipCgPubService.chargeIpBatch(cgParams,false);
		else //门诊
			cgRtns = opCgPubService.blOpCg(cgParams);
		
		String pkCg = "";
		if(cgRtns==null){
			throw new BusException("记费失败！");
		}else if("3".equals(pv.getEuPvtype()) && (cgRtns.getBids()==null || cgRtns.getBids().size()<1)){
			throw new BusException("住院记费失败！");
		}else if("1".equals(pv.getEuPvtype()) && (cgRtns.getBods()==null || cgRtns.getBods().size()<1)){
			throw new BusException("门诊记费失败！");
		}else{
			if("3".equals(pv.getEuPvtype()))//住院
				pkCg = cgRtns.getBids().get(0).getPkCgip();
			else //门诊
				pkCg = cgRtns.getBods().get(0).getPkCgop();
		}
		
		String pkExOcc = "";
		//8.开立说明医嘱，并更新记费记录中的医嘱主键
		if(param.get("name_pd") != null && !CommonUtils.isEmptyString(param.get("name_pd").toString()))
		{
			CnOrder ord = AddDescOrd(param, pv, deptEx, deptApp, deptNsApp,empApp,ord2);//插入医嘱
			pkExOcc = AddExOcc(param, pv, deptEx, deptApp, deptNsApp,pkCg,ord);//插入执行单
			DataBaseHelper.update("update bl_ip_dt set pk_cnord = ? , pk_ordexdt = ? where pk_cgip = ? ",ord.getPkCnord(), pkExOcc, pkCg);
		}
		String res = "{\"pk_cg\":\"" + pkCg + "\"";
		if(!CommonUtils.isEmptyString(pkExOcc))
			res+=",\"pk_exocc\":\"" + pkExOcc + "\"" ;
		return res + "}";
	}

	/**
	 * 1.3 插入执行单
	 * @param param
	 * @param pv 患者就诊
	 * @param deptEx 执行科室
	 * @param deptApp 开立科室
	 * @param deptNsApp 开立病区
	 */
	public String AddExOcc(Map<String, Object> param, PvEncounter pv,
			BdOuDept deptEx, BdOuDept deptApp, BdOuDept deptNsApp ,String pkCg,CnOrder ord) {
		
		//2.插入执行单
		ExOrderOcc occ = new ExOrderOcc();
		occ.setPkCnord(ord.getPkCnord());
		occ.setEuStatus("1");
		occ.setDateOcc(ord.getDateStart());
		occ.setDatePlan(ord.getDateStart());
		occ.setDelFlag("0");
		occ.setFlagBase("0");
		occ.setFlagCanc("0");
		occ.setFlagSelf("0");
		occ.setPkEmpOcc(ord.getPkEmpChk());
		occ.setNameEmpOcc(ord.getNameEmpChk());
		occ.setPkDeptOcc(ord.getPkDeptExec());
		occ.setPkOrgOcc(ord.getPkOrdExc());
		occ.setPkPi(ord.getPkPi());
		occ.setPkPv(ord.getPkPv());
		occ.setQuanCg(ord.getQuanCg());
		occ.setQuanOcc(ord.getQuanCg());
		occ.setPkCg(pkCg);
		DataBaseHelper.insertBean(occ);
		if(CommonUtils.isEmptyString(occ.getPkExocc()))
			throw new BusException("记费时，未生成相关执行记录，记费失败！");
		return occ.getPkExocc();
	}

	/**
	 * //1.2插入说明医嘱
	 * @param param
	 * @param pv
	 * @param deptEx
	 * @param deptApp
	 * @param deptNsApp
	 * @return
	 */
	public CnOrder AddDescOrd(Map<String, Object> param, PvEncounter pv,
			BdOuDept deptEx, BdOuDept deptApp, BdOuDept deptNsApp,BdOuEmployee empApp , CnOrder ord2) {
		CnOrder ord = new CnOrder();
		Date sysDate = new Date();
		ord.setPkOrg(pv.getPkOrg());
		ord.setEuPvtype(pv.getEuPvtype());
		ord.setPkPv(pv.getPkPv());
		ord.setPkPi(pv.getPkPi());
		String codeOrd="DEF99999";
		if(param.get("code_ord")!=null&&!param.get("code_ord").toString().equals("")){
			codeOrd=param.get("code_ord").toString();
		}
		ord.setCodeOrd(codeOrd);
		String codeOrdtype="08";
		if(param.get("code_ordtype")!=null&&!param.get("code_ordtype").toString().equals("")){
			codeOrdtype=param.get("code_ordtype").toString();
		}
		ord.setCodeOrdtype(codeOrdtype);
		ord.setEuAlways("1");
		if(null == ord.getOrdsn() || ord.getOrdsn().intValue() == 0){
			ord.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, UserContext.getUser()));
			ord.setOrdsnParent(ord.getOrdsn());
		}
		ord.setNameOrd(param.get("name_pd").toString());//医嘱名称
		ord.setSpec(param.get("spec").toString());//规格
		String noteOrd=param.get("note")==null?"":param.get("note").toString();
		String barcode = param.get("bar_code")==null?"":param.get("bar_code").toString();
		if(!barcode.equals("")) noteOrd=noteOrd+" " + barcode;
		ord.setNoteOrd(noteOrd);//备注
		ord.setCodeFreq("once");//医嘱频次
		ord.setDosage(1.00);//剂量
		ord.setQuan(1.00);//数量
		ord.setEuStatusOrd("3");//执行状态
		ord.setFlagNote("1");//说明医嘱
		ord.setFlagSign("1");//签署
		ord.setFlagDoctor("1");//医嘱标志
		ord.setDateEnter(sysDate);
		ord.setDateStart(null != ord2 ? ord2.getDateStart() : sysDate);
		ord.setDateEffe(sysDate);
		ord.setDays(1L);
		ord.setPkOrgExec(deptEx.getPkOrg());//执行机构
		ord.setPkDeptExec(deptEx.getPkDept());//执行科室
		ord.setPkEmpInput(UserContext.getUser().getPkEmp());
		ord.setNameEmpInput(UserContext.getUser().getNameEmp());
		ord.setPkDept(deptApp == null ? pv.getPkDept() : deptApp.getPkDept());
		ord.setPkDeptNs(deptNsApp == null ? pv.getPkDeptNs() : deptNsApp.getPkDept());
		ord.setDateSign(sysDate);//签署时间
		ord.setDateChk(sysDate);//核对时间
		ord.setDateLastEx(sysDate);//执行时间
		//开立医生【2020-03-19 开立医生由当前登录人修改为开立医生】
		ord.setPkEmpOrd(empApp.getPkEmp());
		ord.setNameEmpOrd(empApp.getNameEmp());
		//核对人 = 开立医生【2020-03-19 核对人 由当前登录人 修改为 开立医生】
		ord.setPkEmpChk(empApp.getPkEmp());
		ord.setNameEmpChk(empApp.getNameEmp());
		ord.setFlagFirst("0");//首次标志
		ord.setFlagDurg("0");//非药品
		ord.setFlagSelf("0");//非自备
		ord.setFlagBase("0");//非基数药
		ord.setFlagBl("0");//不记费医嘱
		ord.setFlagStop("0");//停止
		ord.setFlagStopChk("0");//停止核对
		ord.setFlagErase("0");//作废
		ord.setFlagEraseChk("0");//作废核对
		ord.setFlagCp("0");//临床路径医嘱
		ord.setFlagPrint("0");//打印标志
		ord.setFlagMedout("0");//出院带药标志
		ord.setFlagEmer("0");//加急标志
		ord.setFlagThera("0");//治疗标志
		ord.setFlagPrev("0");//预防标志
		ord.setFlagFit("0");//适应症标志
		ord.setDelFlag("0");//删除标志
		ord.setInfantNo(0);//婴儿序号
		ord.setQuanCg(0.00);//记费数量
		ord.setQuanBed(0.00);//床边量
		DataBaseHelper.insertBean(ord);
		return ord;
	}
	
	/**
	 * 2.退费
	 * @param blIp
	 * @param blOp
	 * @return
	 */
	public String BlCgRtn(BlIpDt blIp ,BlOpDt blOp , String pkExocc){
		String res = "";
		if(blIp != null && !CommonUtils.isEmptyString(blIp.getPkCgip()))//住院退费
		{
			RefundVo refVo =  new RefundVo();
			refVo.setPkCgip(blIp.getPkCgip());
			refVo.setQuanRe(blIp.getQuan());
			List<RefundVo> list = new ArrayList<RefundVo>();
			list.add(refVo);
			BlPubReturnVo rs = ipCgPubService.refundInBatch(list);//调用退费相关方法
			if(rs == null || rs.getBids() == null || rs.getBids().size() < 1)
				throw new BusException("住院退费失败！");
			else{
				//更新退费记录中的医嘱主键
				if(!CommonUtils.isEmptyString(blIp.getPkCnord()))
					DataBaseHelper.update("update bl_ip_dt set pk_cnord = ? ,pk_ordexdt = ? where pk_cgip = ? ",blIp.getPkCnord(),pkExocc, rs.getBids().get(0).getPkCgip());
				
				Date now = new Date();
				String pkEmp=UserContext.getUser().getPkEmp();
				String empName=UserContext.getUser().getNameEmp();

				//更新执行单记录中的退费主键
				if(!CommonUtils.isEmptyString(pkExocc))
					DataBaseHelper.update("update ex_order_occ set pk_cg_cancel = ? , eu_status = '9',pk_emp_canc = ?,name_emp_canc = ?,date_canc = ? where pk_exocc = ? ",
							rs.getBids().get(0).getPkCgip(), pkEmp,empName,now,pkExocc);
				//作废医嘱
				if(rs.getBids().get(0)!=null&&!CommonUtils.isEmptyString(rs.getBids().get(0).getPkCnord()))
					DataBaseHelper.update("update cn_order set eu_status_ord = '9',flag_erase = '1',date_erase =?,pk_emp_erase=?,name_emp_erase=?,ts=?,flag_erase_chk='1',date_erase_chk=?,pk_emp_erase_chk=?,name_erase_chk=? "
							+ "	 where pk_cnord = ? ",now,pkEmp,empName,now,now,pkEmp,empName, rs.getBids().get(0).getPkCnord());

				return rs.getBids().get(0).getPkCgip();
			}
		}
		
		if(blOp != null && !CommonUtils.isEmptyString(blOp.getPkCgop()))//门诊退费
		{
			//暂时不作处理
		}
		return res;
	}

}
