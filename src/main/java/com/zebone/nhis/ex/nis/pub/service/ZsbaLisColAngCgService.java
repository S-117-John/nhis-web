package com.zebone.nhis.ex.nis.pub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.pub.dao.ZsSmExMapper;
import com.zebone.nhis.ex.nis.pub.vo.BdItemDefdocVo;
import com.zebone.nhis.ex.nis.pub.vo.CnLabApplyVo;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class ZsbaLisColAngCgService {

	@Resource
	private	ZsSmExMapper zsSmExMapper;
	
	@Resource
	public IpCgPubService ipCgPubService;

	/**
	 * 根据医嘱号更新检验申请单的采集人相关信息
	 * 2018-03-23 扫码执行调试后添加
	 * 试管费用，直接插入费用，barCode 记录条码号，作为退费依据【2020-03-24】
	 * 将试管对应医嘱号，拼接 存入 pk_cnord 用于记录条码对应医嘱
	 * @param ordsn
	 */
	public int updateNhisRisApp(String sampNo,User u) {
		if(CommonUtils.isEmptyString(sampNo)) 
			return 0;
		
		//2020-04-13 1、获取该条码号对应的申请单是否可采集，2、获取试管类型，3、拼装待记费的医嘱序号
		String queStr = "select ord.ordsn ordsn_no, ord.code_apply code_app,ord.ordsn_parent ordsn_parent_no, ord.eu_status_ord,ord.name_ord ord_name,"
				+ " lis.* from cn_lab_apply lis inner join cn_order ord on ord.pk_cnord = lis.pk_cnord  where lis.samp_no = ? and ord.del_flag = '0' order by ord.ts ";
		List<CnLabApplyVo> cnLabApply = DataBaseHelper.queryForList(queStr,CnLabApplyVo.class,sampNo);
		if(null == cnLabApply || cnLabApply.size() < 1)
			throw new BusException("未获取到条码号："+sampNo+" 对应的医嘱信息，不允许采集！");
		String errTxt = "";
		String ordTxt = "";
		for (CnLabApplyVo app : cnLabApply) {
			ordTxt += app.getOrdsnNo() + ",";
			if("9".equals(app.getEuStatusOrd())){
				errTxt += "\n 申请单：" + app.getCodeApp() +"-"+app.getOrdName()+" - 对应医嘱已作废";
			}
			else if("0".equals(app.getEuStatus())){
				errTxt += "\n 申请单：" + app.getCodeApp() +"-"+app.getOrdName()+" - 申请状态";
			}
//			else if("2".equals(app.getEuStatus())){
//				errTxt += "\n 申请单：" + app.getCodeApp() +"-"+app.getOrdName()+" - 采集状态";
//			}
			else if("3".equals(app.getEuStatus())){
				errTxt += "\n 申请单：" + app.getCodeApp() +"-"+app.getOrdName()+" - 签收状态";
			}
			else if("4".equals(app.getEuStatus())){
				errTxt += "\n 申请单：" + app.getCodeApp() +"-"+app.getOrdName()+" - 报告状态";
			}
		}
		//1、获取该条码号对应的申请单是否可采集，
		if(errTxt.length() > 0)
			throw new BusException("条码【"+sampNo+"】对应医嘱的状态不允许采集！" + errTxt);
		
		Date date = new Date();
		Map<String, Object> upMap = new HashMap<String, Object>();
		upMap.put("dateCol", date);
		upMap.put("pkDeptCol", u.getPkDept());
		upMap.put("pkEmpCol", u.getPkEmp());
		upMap.put("nameEmpCol", u.getNameEmp());
		upMap.put("sampNo", sampNo);
		int count = zsSmExMapper.updateCxApp(upMap);
		
		//2020-04-13 增加开关，处理检验采集是否收费
		if("1".equals(ApplicationUtils.getPropertyValue("lis02.colAndCg", ""))){
			//3、拼装待记费的医嘱序号
			ordTxt = ordTxt.substring(0,ordTxt.length() - 1);
			setConCg(sampNo,ordTxt, u, cnLabApply.get(0), date); //2020-03-24 增加试管费用加收	
		}
		return count;
	}

	/**
	 * 试管费用加收
	 * 2020-03-24 增加试管费用加收
	 * @param sampNo
	 * @param u
	 * @param cnLabApply
	 * @param date
	 */
	private void setConCg(String sampNo,String appList, User u, CnLabApply cnLabApply, Date date) {
		
		//1、判断当前条码号是否已经收费,若已存在收费记录，则不在记费
		int cnt = DataBaseHelper.queryForScalar("select count(*) from BL_IP_DT dt where  dt.BARCODE = ? "
				+ " and not exists(select 1 from BL_IP_DT dtBack where dtBack.PK_CGIP_BACK = dt.PK_CGIP)"//未退费
				+ " and (dt.PK_CGIP_BACK is null  or dt.PK_CGIP_BACK like '% %')", Integer.class, new Object[]{sampNo});//记费记录
		if(cnt > 0) 
			return;
		
		//获取试管费用明细信息
		String queItem = "select itd.PK_ITEM,itd.QUAN,it.spec from  BD_ITEM_DEFDOC itd "
				+ "inner join bd_item it on it.pk_item = itd.pk_item and it.del_flag = '0' and it.flag_active = '1' "
				+ "where itd.CODE_DEFDOCLIST ='030203' and itd.CODE_DEFDOC= ? and itd.eu_pvtype='3' ";
		List<BdItemDefdocVo> bdItemDefdocs = DataBaseHelper.queryForList(queItem,BdItemDefdocVo.class,cnLabApply.getDtTubetype());
		//存在待记费项目，则进行记费处理
		if(null != bdItemDefdocs && bdItemDefdocs.size() > 0){
			//获取医嘱信息
			String queCnord = "select * from cn_order where PK_CNORD = ?";
			CnOrder cnOrder = DataBaseHelper.queryForBean(queCnord,CnOrder.class,cnLabApply.getPkCnord());
			//获取执行单信息
			String orderOcc = "select * from ex_order_occ where pk_cnord = ?";
			ExOrderOcc exOrderOcc  = DataBaseHelper.queryForBean(orderOcc,ExOrderOcc.class,cnLabApply.getPkCnord());
			List<BlPubParamVo> blCgPubParamVos = new ArrayList<>();
			BlPubParamVo blCgPubParamVo = null;
			for (BdItemDefdocVo item : bdItemDefdocs) {
				blCgPubParamVo = new BlPubParamVo();
				blCgPubParamVo.setPkOrg(cnOrder.getPkOrg());
				blCgPubParamVo.setPkPres(cnOrder.getPkPres());
				blCgPubParamVo.setEuPvType(cnOrder.getEuPvtype());
				blCgPubParamVo.setPkPv(cnOrder.getPkPv());
				blCgPubParamVo.setPkPi(cnOrder.getPkPi());
				blCgPubParamVo.setPkOrg(cnOrder.getPkOrg());
				blCgPubParamVo.setPkCnord(null);
				blCgPubParamVo.setNameSupply(appList);
				blCgPubParamVo.setSpec(item.getSpec());//耗材规格
				blCgPubParamVo.setPkItem(item.getPkItem());//费用主键
				blCgPubParamVo.setQuanCg(item.getQuan());//费用数量
				blCgPubParamVo.setBarcode(sampNo);//将条码号记录
				blCgPubParamVo.setPkOrgEx(cnOrder.getPkOrgExec());
				blCgPubParamVo.setPkOrgApp(cnOrder.getPkOrd());
				blCgPubParamVo.setPkDeptApp(cnOrder.getPkDept());
				blCgPubParamVo.setPkDeptNsApp(cnOrder.getPkDeptNs());
				blCgPubParamVo.setPkEmpApp(cnOrder.getPkEmpOrd());
				blCgPubParamVo.setNameEmpApp(cnOrder.getNameEmpOrd());
				blCgPubParamVo.setFlagPd("0");
				String txt = DateUtils.getDateStr(exOrderOcc.getDatePlan());
				blCgPubParamVo.setDateHap( DateUtils.strToDate(txt+ "000000", "yyyyMMddHHmmss") );
				blCgPubParamVo.setPkDeptCg(cnOrder.getPkDeptNs());
				blCgPubParamVo.setPkEmpCg(u.getPkEmp());
				blCgPubParamVo.setNameEmpCg(u.getNameEmp());
				blCgPubParamVo.setPkOrdexdt(null);
				blCgPubParamVo.setDateStart(cnOrder.getDateStart());
				blCgPubParamVo.setCodeOrdtype(cnOrder.getCodeOrdtype());
				blCgPubParamVo.setOrdsn(null);
				blCgPubParamVo.setOrdsnParent(null);
				blCgPubParamVo.setDateCg(date);
				blCgPubParamVo.setSortno(1);
				blCgPubParamVo.setPkDeptEx(cnOrder.getPkDeptNs());
				blCgPubParamVos.add(blCgPubParamVo);
			}
			//调用公共计费方法
			ipCgPubService.chargeIpBatch(blCgPubParamVos,true);
		}
	}

	/**
	 * 取消采集
	 * 根据条码号，获取待退费记录进行退费
	 * @param paramMap
	 * @param u
	 * @return
	 */
	public int CancelCollection(String sampNo, User u) {
		//批量判断申请单状态是否可取消采集
		String queStr = "select ord.ordsn ordsn_no, ord.code_apply code_app,ord.ordsn_parent ordsn_parent_no, ord.eu_status_ord,ord.name_ord ord_name,"
				+ " lis.* from cn_lab_apply lis inner join cn_order ord on ord.pk_cnord = lis.pk_cnord  where lis.samp_no = ? and ord.del_flag = '0' order by ord.ts ";
		List<CnLabApplyVo> cnLabApplys = DataBaseHelper.queryForList(queStr,CnLabApplyVo.class,sampNo);
		String euStatusName;
		if(null == cnLabApplys || cnLabApplys.size() < 1)
			throw new BusException("未获取到条码号："+sampNo+" 对应的申请单信息，不允许取消采集！");
		String errorTxt = "";//错误描述信息
		for (CnLabApplyVo labApp : cnLabApplys) {
			if(labApp != null && !"2".equals(labApp.getEuStatus())){
				if("0".equals(labApp.getEuStatus())){
					euStatusName = "申请";
				}else if("1".equals(labApp.getEuStatus())){
					euStatusName = "提交";
				}else if("3".equals(labApp.getEuStatus())){
					euStatusName = "核收";
				}else{
					euStatusName = "报告";
				}
				errorTxt += "\n 申请单【"+labApp.getCodeApp()+"】- "+labApp.getOrdName()+" - "+ euStatusName+" 状态";
			}
		}
		if(!CommonUtils.isEmptyString(errorTxt))
			throw new BusException("该条码号："+sampNo+" 对应的部分申请单状态不允许取消采集！" + errorTxt);
		
		//取消执行医嘱
		String updSql = "update CN_LAB_APPLY set eu_status = '1',date_col=null,pk_emp_col=null,NAME_EMP_COL=null where SAMP_NO=? and eu_status='2'";
		int count = DataBaseHelper.update(updSql, new Object[]{sampNo});

		//2020-04-13 增加开关，处理取消采集是否退费
		if("1".equals(ApplicationUtils.getPropertyValue("lis02.cancColAndRtnCg", ""))){
			rtnColCg(u, sampNo);//根据条码号退费
		}
		
		return count;
	}

	/**
	 * 获取试管对应的计费明细[针对条码号，进行费用作废]
	 * @param u
	 * @param sampNo
	 */
	private void rtnColCg(User u, String sampNo) {
		String qreBIDStr = "select * from BL_IP_DT dt where dt.BARCODE =? "
				+ " and not exists(select 1 from BL_IP_DT dtBack where dtBack.PK_CGIP_BACK = dt.PK_CGIP) "//未退费
				+ " and (dt.PK_CGIP_BACK is null  or dt.PK_CGIP_BACK like '% %')";//记费记录
		List<BlIpDt> blIpDts = DataBaseHelper.queryForList(qreBIDStr,BlIpDt.class,new Object[]{sampNo});
		if(null != blIpDts && blIpDts.size() > 0){
			List<RefundVo> refundVos = new ArrayList<>();
			RefundVo refundVo = null;
			for (BlIpDt dt : blIpDts) {
				refundVo = new RefundVo();
				refundVo.setPkOrg(u.getPkOrg());
				refundVo.setPkDept(u.getPkDept());
				refundVo.setPkCgip(dt.getPkCgip());
				refundVo.setPkEmp(u.getPkEmp());
				refundVo.setNameEmp(u.getNameEmp());
				refundVo.setQuanRe(dt.getQuan());
				refundVos.add(refundVo);
			}
			ipCgPubService.refundInBatch(refundVos);
			ExtSystemProcessUtils.processExtMethod("CONSUMABLE", "savaReturnConsumable", refundVos);
		}
	}
}
