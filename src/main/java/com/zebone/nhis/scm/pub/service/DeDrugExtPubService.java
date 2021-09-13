package com.zebone.nhis.scm.pub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdDe;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.dao.DeDrugExtPubMapper;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.nhis.scm.support.ScmExtProcessUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 外接第三方系统发药服务
 * @author yangxue
 *
 */
@Service
public class DeDrugExtPubService {
	@Resource
	private DeDrugExtPubMapper deDrugExtPubMapper;
	@Autowired
	private IpCgPubService ipCgPubService;
	
   /**
    * 住院外部发药接口发药
    * @param exPdAppDetails
    * @param flagPivas
    * @param flagCg
    * @param codeDe
    * @param dateDe
    * @return
    */
	public List<PdDeDrugVo> ipDeDrug(List<ExPdApplyDetail> exPdAppDetails,String pkStore, String codeDe,Date dateDe,User user,String param){
	  if(exPdAppDetails==null||exPdAppDetails.size()<=0)
		  return null;
	  if(CommonUtils.isNull(pkStore))
		  throw new BusException("未指定发药仓库！");
	  Set<String> pdList = new HashSet<String>();
	  List<String> ordList = new ArrayList<String>();
	  for (ExPdApplyDetail exPdApplyDetail : exPdAppDetails) {
		pdList.add(exPdApplyDetail.getPkPd());
		ordList.add(exPdApplyDetail.getPkCnord());
	  }
	  List<String> pkList = new ArrayList<String>();
	  pkList.addAll(pdList);
	  List<ExPdDe> exPdDes = new ArrayList<ExPdDe>();
	  //本次发药药品价格信息
	  List<PdOutDtParamVo> pricelist = this.getPdStorePrice(pkList, pkStore);
	  //本次发药对应医嘱信息
	  List<CnOrder> ordlist = deDrugExtPubMapper.getOrderInfo(ordList);
	  //本次发药需要记费信息
	  List<BlPubParamVo> blPubParamVos = new ArrayList<BlPubParamVo>();
	  //发药后返回信息
	  List<PdDeDrugVo> deDrugVos = new ArrayList<PdDeDrugVo>();
	  //无价格信息药品集合
	  List<String> noPriceList = new ArrayList<String>();
	  //需要发药的请领明细主键集合
	  Set<String> deDtList = new HashSet<String>();
	  //请领单主键集合
	  Set<String> pkPdApSet = new HashSet<String>();
	  for(ExPdApplyDetail exPdApplyDetail:exPdAppDetails){
		  boolean hasFlag = false;
			for (PdOutDtParamVo pdOutDtParamVo : pricelist) {
				if (exPdApplyDetail.getPkPd().equals(pdOutDtParamVo.getPkPd())) {
					hasFlag = true;
					// 构建发药明细
					ExPdDe exPdDe = this.buildExPdDe(exPdApplyDetail,pdOutDtParamVo,user,codeDe,dateDe);
					exPdDes.add(exPdDe);
					// 构建记费参数VO
					BlPubParamVo blPubParamVo = buildBPPV(exPdApplyDetail, pdOutDtParamVo, ordlist, user, exPdDe);
					blPubParamVos.add(blPubParamVo);
					// 构建发药返回参数
					PdDeDrugVo deDrugVo = new PdDeDrugVo();
					deDrugVo.setCodeDe(codeDe);
					deDrugVo.setPkPdapdt(exPdApplyDetail.getPkPdapdt());
					deDrugVo.setPkPdde(exPdDe.getPkPdde());
					deDrugVos.add(deDrugVo);
					break;
				} 
				
			}
			if(!hasFlag){
				noPriceList.add(exPdApplyDetail.getPkPd());
			}
			deDtList.add(exPdApplyDetail.getPkPdapdt());
			pkPdApSet.add(exPdApplyDetail.getPkPdap());
		}
	   
	    //若存在未取到价格的药品，则不允许发放
	    if(noPriceList!=null&&noPriceList.size()>0){
	    	StringBuilder msg = new StringBuilder();
	    	List<BdPd> pds = deDrugExtPubMapper.getPdInfo(noPriceList);
	    	if(pds!=null&&pds.size()>0){
	    		for(BdPd pd:pds){
	    			msg.append(pd.getName()).append(",");
	    		}
	    	}
	    	throw new BusException(msg.append("以上药品在库存中无价格无法完成发药，请先维护库存价格信息！").toString());
	    }
		// 批量更新请领明细发放状态
		if(deDtList!=null&&deDtList.size()>0){
			int updateCount =DataBaseHelper.execute("update ex_pd_apply_detail set flag_de='1',flag_finish='1',ts=? where pk_pdapdt in (" + CommonUtils.convertSetToSqlInPart(deDtList, "pk_pdapdt") + ") and flag_de='0' and flag_stop='0' and nvl(flag_canc,'0') <> '1'", new Date());
			if(updateCount<deDtList.size())
				throw new BusException("您本次提交的药品发放明细中已有被其他人发放完成的记录，请刷新请领单后重新发放！");
			deDtList = null;//使用后清空
		}
		// 批量更新表ex_pd_apply
		String sqlAppFlagFinish = "update ex_pd_apply set flag_finish='1',ts=? where pk_pdap in("
				+ CommonUtils.convertSetToSqlInPart(pkPdApSet, "pk_pdap")
				+ ") and not exists (select 1  from ex_pd_apply_detail detail  where ex_pd_apply.pk_pdap = detail.pk_pdap and nvl(detail.flag_finish, '0') = '0' and nvl(detail.flag_stop, '0') <> '1' )";
		DataBaseHelper.execute(sqlAppFlagFinish, new Date());
		// 批量插入发药记录表
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdDe.class), exPdDes);

		// 只要请领单有一条明细发放，那么请领单的状态就是发放状态
		String sqlAppDetailStatus = "update ex_pd_apply set eu_status='1',ts=? where pk_pdap in("
				+ CommonUtils.convertSetToSqlInPart(pkPdApSet, "pk_pdap")
				+ ") and  exists (select 1  from ex_pd_apply_detail detail  where ex_pd_apply.pk_pdap = detail.pk_pdap and detail.flag_de = '1') and ex_pd_apply.eu_status ='0'";
		DataBaseHelper.execute(sqlAppDetailStatus, new Date());

		blPubParamVos.addAll(ScmPubUtils.buildBoilBl(exPdAppDetails, user));		
		if (blPubParamVos.size() > 0)
			ipCgPubService.chargeIpBatch(blPubParamVos,false);// 批量计费
		
		//执行发药外接服务
		if("1".equals(ApplicationUtils.getDeptSysparam("EX0053", UserContext.getUser().getPkDept()))){
			ScmExtProcessUtils.processExtIpDe(exPdAppDetails,param);
		}
		
		return deDrugVos;
	}
	
	/**
	 * 获取药品虚库存价格(只取价格，不确认批次及库存量)
	 * @param pdList 物品主键集合
	 * @param pkStore 仓库主键
	 * @return
	 */
	 public List<PdOutDtParamVo> getPdStorePrice(List<String> pdList,String pkStore){
		 if(pdList==null||pdList.size()<=0)
			 return null;
		 if(CommonUtils.isNull(pkStore))
			 return null;
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 paramMap.put("pdlist", pdList);
		 paramMap.put("pkStore", pkStore);
		 return deDrugExtPubMapper.getPdStorePrice(paramMap);
	 }
	 /**
	 * 构建计费参数vo
	 * @param exPdApplyDetail
	 * @param pdOutDtParamVo
	 * @param flagPivas
	 * @param userOp
	 */
	private BlPubParamVo buildBPPV(ExPdApplyDetail exPdApplyDetail, PdOutDtParamVo pdOutDtParamVo, List<CnOrder> ordlist,
			User userOp, ExPdDe exPdDe) {

		String pkOrg = userOp.getPkOrg();
		String pkDept = userOp.getPkDept();
		String pkEmp = userOp.getPkEmp();
		String nameEmp = userOp.getNameEmp();

		BlPubParamVo blPubParamVo = new BlPubParamVo();
		blPubParamVo.setPkOrg(pkOrg);
		blPubParamVo.setEuPvType("3");// 住院
		blPubParamVo.setPkPv(exPdApplyDetail.getPkPv());
		if(ordlist!=null&&ordlist.size()>0){
			for(CnOrder ord:ordlist){
				if(ord.getPkCnord().equals(exPdApplyDetail.getPkCnord())){
					blPubParamVo.setPkPi(ord.getPkPi());
					blPubParamVo.setPkOrgApp(ord.getPkOrg());// 开立机构
					blPubParamVo.setPkDeptApp(ord.getPkDept());// 开立科室
					blPubParamVo.setPkEmpApp(ord.getPkEmpOrd());// 开立医生
					blPubParamVo.setNameEmpApp(ord.getNameEmpOrd());// 开立医生姓名
					blPubParamVo.setInfantNo(ord.getInfantNo()==null?"":ord.getInfantNo().toString());
					blPubParamVo.setPkDeptNsApp(ord.getPkDeptNs());
					break;
				}
			}
		}
		blPubParamVo.setPkOrd(exPdApplyDetail.getPkPd());
		blPubParamVo.setPkItem(exPdApplyDetail.getPkPd());// 药品的话此处传pkPd
		blPubParamVo.setQuanCg(exPdApplyDetail.getQuanPack());// 实际发放数量
		blPubParamVo.setPkOrgEx(pkOrg);// 执行机构
		blPubParamVo.setPkPres(exPdApplyDetail.getPkPres());//处方主键
		blPubParamVo.setPkCnord(exPdApplyDetail.getPkCnord());// 对应的医嘱主键
		blPubParamVo.setPkDeptEx(pkDept);// 执行科室
		blPubParamVo.setFlagPd("1");
		blPubParamVo.setFlagPv("0");
		blPubParamVo.setDateHap(new Date());
		blPubParamVo.setPkDeptCg(pkDept);
		blPubParamVo.setPkEmpCg(pkEmp);
		blPubParamVo.setNameEmpApp(nameEmp);
		double price = MathUtils.mul(MathUtils.div(pdOutDtParamVo.getPrice(), pdOutDtParamVo.getPackSizePd().doubleValue()), exPdApplyDetail.getPackSize()
				.doubleValue());
		blPubParamVo.setPrice(price);
		blPubParamVo.setBatchNo(pdOutDtParamVo.getBatchNo());
		blPubParamVo.setDateExpire(pdOutDtParamVo.getDateExpire());
		blPubParamVo.setPkUnitPd(exPdApplyDetail.getPkUnit());
		blPubParamVo.setPackSize(exPdApplyDetail.getPackSize());
		double priceCost = MathUtils.mul(MathUtils.div(pdOutDtParamVo.getPriceCost(), pdOutDtParamVo.getPackSizePd().doubleValue()), exPdApplyDetail.getPackSize()
				.doubleValue());
		blPubParamVo.setPriceCost(priceCost);
		blPubParamVo.setPkOrdexdt(exPdDe.getPkPdde());
		blPubParamVo.setEuBltype("1");
		return blPubParamVo;
	}
   /**
	 * 构建发药明细vo
	 * @param exPdApplyDetail
	 * @param pdOutDtParamVo
	 * @param flagPivas
	 * @param userOp
	 */
	private ExPdDe buildExPdDe(ExPdApplyDetail exPdApplyDetail, PdOutDtParamVo pdOutDtParamVo, User userOp, String codeDe,Date dateDe) {

		String pkOrg = userOp.getPkOrg();
		String pkDept = userOp.getPkDept();
		String pkEmp = userOp.getPkEmp();
		String nameEmp = userOp.getNameEmp();
		String pkStock = userOp.getPkStore();
		ExPdDe exPdDe = new ExPdDe();
		// 设置默认值
		ApplicationUtils.setDefaultValue(exPdDe, true);
		// 写发药表ex_pd_de
		exPdDe.setPkPdapdt(exPdApplyDetail.getPkPdapdt());
		exPdDe.setPkOrg(pkOrg);
		exPdDe.setCodeDe(codeDe);// 调用编码生成规则获取发药编码
		exPdDe.setEuDirect("1");
		exPdDe.setPkDeptAp(exPdApplyDetail.getPkDeptAp());
		exPdDe.setPkOrgAp(exPdApplyDetail.getPkOrgAp());
		exPdDe.setPkPdstdt(null);//无出库单
		exPdDe.setPkPv(exPdApplyDetail.getPkPv());
		exPdDe.setPkCnord(exPdApplyDetail.getPkCnord());
		exPdDe.setPkPd(exPdApplyDetail.getPkPd());
		exPdDe.setPkUnit(exPdApplyDetail.getPkUnit());
		exPdDe.setPackSize(exPdApplyDetail.getPackSize());
		exPdDe.setBatchNo(pdOutDtParamVo.getBatchNo());
		exPdDe.setEuStatus("0");
		exPdDe.setQuanPack(exPdApplyDetail.getQuanPack());
		exPdDe.setQuanMin(exPdApplyDetail.getQuanMin());
		exPdDe.setPriceCost(MathUtils.mul(MathUtils.div(pdOutDtParamVo.getPriceCost(), pdOutDtParamVo.getPackSizePd().doubleValue()), exPdApplyDetail.getPackSize()
				.doubleValue()));
		double price = MathUtils.mul(MathUtils.div(pdOutDtParamVo.getPrice(), pdOutDtParamVo.getPackSizePd().doubleValue()), exPdApplyDetail.getPackSize()
				.doubleValue());
		exPdDe.setPrice(price);
		exPdDe.setAmount(exPdApplyDetail.getQuanPack() * price);// 包装数量乘以单价
		exPdDe.setPkDeptDe(pkDept);
		exPdDe.setPkStoreDe(pkStock);
		exPdDe.setDateDe(dateDe);
		exPdDe.setPkEmpDe(pkEmp);
		exPdDe.setNameEmpDe(nameEmp);
		exPdDe.setFlagPrt("0");
		exPdDe.setFlagPivas("0");
		// 目前静配标志和瓶签标志同步
		exPdDe.setFlagBarcode("0");
		exPdDe.setNote(null);
	    //设置发放分类
		exPdDe.setPkPddecate(exPdApplyDetail.getPkPddecate());
		exPdDe.setNameDecate(exPdApplyDetail.getNameDecate());
		//如果处方，设置为处方名称
		if(!CommonUtils.isEmptyString(exPdApplyDetail.getPresName())){
			exPdDe.setNameDecate(exPdApplyDetail.getPresName());
			exPdDe.setPkPddecate(exPdApplyDetail.getPkPres());
		}
		return exPdDe;
	}

}
