package com.zebone.nhis.scm.pub.service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdDe;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * 住院发药公共服务 本服务至关重要，涉及到多种场景，关于内部实现逻辑，欢迎咨询
 * 
 * 
 * @author gongxy
 * 
 */
@Service
public class IpDeDrugPubService {

	Logger logger = LogManager.getLogger("nhis.scm");

	@Autowired
	private IpCgPubService ipCgPubService;

	@Autowired
	private PdStOutPubService pdStOutPubService;
	
	@Autowired
	private PdStOutBatchPubService pdStOutNoInPubService;

	/**
	 * 住院(医嘱，处方)发药处理（此方法非常重要、所以要考虑性能）
	 * 
	 * exPdAppDetails 请领明细
	 * 
	 * flagPivas 是否静配标志
	 * 
	 * flagCg 是否计费标志IpPdDeDrugService.mergeIpDeDrug
	 * 
	 * @return
	 */
	public List<PdDeDrugVo> mergeIpDeDrug(List<ExPdApplyDetail> exPdAppDetails, String flagPivas, String flagCg,String codeDe,Date dateDe) {
		List<PdDeDrugVo> deDrugVos = new ArrayList<PdDeDrugVo>();
		/*
		 * 1、调用供应链出库服务 2、调用住院计费服务 3、写发药表 4、更新请领明细
		 */
		// 获取本次发药的发药单号
		if(CommonUtils.isEmptyString(codeDe)){
			codeDe = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_IPDS);
		}
		//获取静配分类编码系统参数
		String codeDecatePivas = ApplicationUtils.getSysparam("EX0003", false);
		if (exPdAppDetails == null || exPdAppDetails.size() <= 0)
			throw new BusException("传入的发药明细数据错误");
		User userCur = UserContext.getUser();
		String pkDept = userCur.getPkDept();
		String pkStock = userCur.getPkStore();
		// 利用set集合的不可重复性，存储本次发药请领单的主键
		Set<String> pkPdApSet = new HashSet<>();
		// 利用set集合的不可重复性，存储本次发药医嘱主键
		Set<String> pkCnordSet = new HashSet<>();
		// 利用set集合的不可重复性，存储本次发药请领明细主键
		Set<String> pkAppDtSet = new HashSet<>();
		// 通过静配分类发放的请领明细集合
		Set<String> pkAppDtPivas = new HashSet<>();
		// 将前台勾选的出库明细转化成出库服务需要的vo
		List<PdOutParamVo> popvList = new ArrayList<PdOutParamVo>();
		
		String pk_dept_ap = "";//请领科室
		String pk_org_ap = "";//请领机构

		Map<String, PdOutParamVo> mapGroup = new HashMap<String, PdOutParamVo>();
		List<CnOrder> cnOrders = new ArrayList<CnOrder>();
		// 存放明细的主键和请领信息
		Map<String, ExPdApplyDetail> mapPkForDt = new HashMap<String, ExPdApplyDetail>();
		for (ExPdApplyDetail exPdApplyDetail : exPdAppDetails) {
			//添加通过静配分类发放的请领明细集合
			//if(!CommonUtils.isEmptyString(codeDecatePivas)&&codeDecatePivas.equals(exPdApplyDetail.getCodeDecate())){
			if("1".equals(exPdApplyDetail.getFlagPivas())){//根据静配标志来判
				pkAppDtPivas.add(exPdApplyDetail.getPkPdapdt());
			}
			pk_dept_ap = exPdApplyDetail.getPkDeptAp();//发药是以请领科室为单位发药，因此一次发药对应的请领科室与机构唯一
			pk_org_ap = exPdApplyDetail.getPkOrgAp();
			// 床边量
			if ("2".equals(exPdApplyDetail.getEuMuputype())) {
				CnOrder orderPd = new CnOrder();
				orderPd.setPkCnord(exPdApplyDetail.getPkCnord());
				//更新医嘱床边量时需要去除执行的数量，当请领数量小于执行数量时可能出现负的值
				orderPd.setQuanBed(MathUtils.sub(exPdApplyDetail.getQuanMin(),getExQuanMin(exPdApplyDetail.getPkPdapdt())));
				ApplicationUtils.setDefaultValue(orderPd, false);
				cnOrders.add(orderPd);
			}
			ExPdApplyDetail exPdApplyDetailNew = new ExPdApplyDetail();
			ApplicationUtils.copyProperties(exPdApplyDetailNew, exPdApplyDetail);
			mapPkForDt.put(exPdApplyDetail.getPkPdapdt(), exPdApplyDetailNew);
			PdOutParamVo popv = new PdOutParamVo();
			popv.setPkPd(exPdApplyDetail.getPkPd());
			popv.setPkPv(exPdApplyDetail.getPkPv());
			popv.setPkCnOrd(exPdApplyDetail.getPkCnord());
			popv.setPkPdapdt(exPdApplyDetail.getPkPdapdt());
			popv.setPackSize(exPdApplyDetail.getPackSize());
			popv.setPkUnitPack(exPdApplyDetail.getPkUnit());
			popv.setQuanPack(exPdApplyDetail.getActualQuanPack() == null ? exPdApplyDetail.getQuanPack() : exPdApplyDetail.getActualQuanPack());// 如果是医嘱取实际请领数量，否则取包装数量
			popv.setQuanMin(exPdApplyDetail.getQuanMin());
			
			// 存放医嘱主键
			pkCnordSet.add(exPdApplyDetail.getPkCnord());
			// 存放请领单主键
			pkPdApSet.add(exPdApplyDetail.getPkPdap());
			pkAppDtSet.add(exPdApplyDetail.getPkPdapdt());
			
			if("1".equals(exPdApplyDetail.getFlagSelf()))continue;//排除自备药
			
			// 将同一个药品进行合并
			if (mapGroup.get(exPdApplyDetail.getPkPd()) == null) {
				mapGroup.put(exPdApplyDetail.getPkPd(), popv);
			} else {
				PdOutParamVo temp = mapGroup.get(exPdApplyDetail.getPkPd());
				temp.setQuanPack(MathUtils.add(temp.getQuanPack(), popv.getQuanPack()));
				temp.setQuanMin(MathUtils.add(temp.getQuanMin(), popv.getQuanMin()));
			}
		}

		// 获取map的所有Value
		for (String pkPd : mapGroup.keySet()) {
			popvList.add(mapGroup.get(pkPd));
		}
		String sql = "select ord.pk_cnord key_,ord.* from cn_order ord where ord.pk_cnord in (" + CommonUtils.convertSetToSqlInPart(pkCnordSet, "ord.pk_cnord")
				+ ")";
		Map<String, Map<String, Object>> mapCnOrderInfo = DataBaseHelper.queryListToMap(sql, new Object[] {});
		List<PdOutDtParamVo> dtParamVos=new ArrayList<PdOutDtParamVo>();
		if(popvList.size()!=0){
			// 调用出库服务，注意一个物品可能对应多个批次
			dtParamVos = pdStOutNoInPubService.execStOut(popvList,pk_org_ap,pk_dept_ap,pkDept, pkStock, null,true);
		}
		//logger.debug("本次发药出库用时：" + (time2 - time1) / 1000 + "s");
		List<BlPubParamVo> blPubParamVos = new ArrayList<BlPubParamVo>();
		List<ExPdDe> exPdDes = new ArrayList<ExPdDe>();
		for (ExPdApplyDetail exPdApplyDetail : exPdAppDetails) {
			if("1".equals(exPdApplyDetail.getFlagSelf()))continue;//排除自备药：中二需求
			twoFor: for (PdOutDtParamVo pdOutDtParamVo : dtParamVos) {
				if(pdOutDtParamVo.getQuanOutMin().doubleValue()<=0)
					continue;
				// 当一个物品对应多个批次时，判断请领数量是否小于出库数量
				// 如果小于或等于说明一条出库对应多条出库
				// 如果大于或等于说明一条明细对应多条出库
				if (exPdApplyDetail.getPkPd().equals(pdOutDtParamVo.getPkPd())) {
					if (exPdApplyDetail.getQuanMin().doubleValue() <= pdOutDtParamVo.getQuanOutMin().doubleValue()) {
						// 构建发药明细
						ExPdDe exPdDe = buildExPdDe(exPdApplyDetail, pdOutDtParamVo, flagPivas, userCur, codeDe,dateDe);
						exPdDes.add(exPdDe);
						// 构建记费参数VO
						BlPubParamVo blPubParamVo = new BlPubParamVo();
						if ("1".equals(flagCg)){
							blPubParamVo = buildBPPV(exPdApplyDetail, pdOutDtParamVo, mapCnOrderInfo, userCur, exPdDe);
							blPubParamVos.add(blPubParamVo);
						}
						// 构建发药返回参数
						PdDeDrugVo deDrugVo = new PdDeDrugVo();
						deDrugVo.setCodeDe(codeDe);
						deDrugVo.setPkPdapdt(exPdApplyDetail.getPkPdapdt());
						deDrugVo.setPkPdde(exPdDe.getPkPdde());
						deDrugVos.add(deDrugVo);
						// 修改该物品的出库剩余数量
						double pdOutQuanMin = pdOutDtParamVo.getQuanOutMin().doubleValue() - exPdApplyDetail.getQuanMin().doubleValue();
						double pdOutQuanPack = pdOutDtParamVo.getQuanOutPack().doubleValue() - exPdApplyDetail.getQuanPack().doubleValue();
						pdOutDtParamVo.setQuanOutMin(pdOutQuanMin);
						pdOutDtParamVo.setQuanOutPack(pdOutQuanPack);
						//if (pdOutQuanMin == 0)
						//	break twoFor;
						break twoFor;
					} else if (exPdApplyDetail.getQuanMin().doubleValue() > pdOutDtParamVo.getQuanOutMin().doubleValue()) {
						// 修改本次请领明细的数量（因为本次一条请领对应了两次出库）
						// 修改之前需要记录本次请领还需要请领多少
						double quanMinRemain = exPdApplyDetail.getQuanMin().doubleValue() - pdOutDtParamVo.getQuanOutMin().doubleValue();
						double quanPackRemain = exPdApplyDetail.getQuanPack().doubleValue() - pdOutDtParamVo.getQuanOutPack().doubleValue();
						exPdApplyDetail.setQuanMin(pdOutDtParamVo.getQuanOutMin());
						exPdApplyDetail.setQuanPack(pdOutDtParamVo.getQuanOutPack());
						ExPdDe exPdDe = buildExPdDe(exPdApplyDetail, pdOutDtParamVo, flagPivas, userCur, codeDe,dateDe);
						exPdDes.add(exPdDe);
						// 构建记费参数VO
						BlPubParamVo blPubParamVo =new BlPubParamVo();
						if ("1".equals(flagCg)){
							blPubParamVo= buildBPPV(exPdApplyDetail, pdOutDtParamVo, mapCnOrderInfo, userCur, exPdDe);
							blPubParamVos.add(blPubParamVo);
						}
						// 当本批次发完后，继续修改请领数量(还需要请领多少)
						exPdApplyDetail.setQuanMin(quanMinRemain);
						exPdApplyDetail.setQuanPack(quanPackRemain);
						pdOutDtParamVo.setQuanOutMin(new Double(0));
						pdOutDtParamVo.setQuanOutPack(new Double(0));
						// 构建发药返回参数
						PdDeDrugVo deDrugVo = new PdDeDrugVo();
						deDrugVo.setCodeDe(codeDe);
						deDrugVo.setPkPdapdt(exPdApplyDetail.getPkPdapdt());
						deDrugVo.setPkPdde(exPdDe.getPkPdde());
						deDrugVos.add(deDrugVo);
					}
				}

			}

		}
		blPubParamVos.addAll(ScmPubUtils.buildBoilBl(exPdAppDetails,userCur));
		/**
		 * 所有数据库操作全部放在循环外边做
		 * 
		 * v1:初始版本 v2:调用出库之前合并请领明细、出库后根据请领数量和出库数量比较来确定具体是哪个批次（不是很固定，随机批次）
		 * v3:静配室那边要支持部分发药--->注意点 判断本次请领的与已经发放的数量之和，和原本请领的数量之间的大小比较
		 * 
		 * 由于静配那边要支持部分发药故做第三次大改
		 * 
		 * 
		 */
		String pkInsertApDt = CommonUtils.convertSetToSqlInPart(pkAppDtSet, "pk_pdapdt");
		List<ExPdApplyDetail> applyDetails = DataBaseHelper.queryForList("select * from ex_pd_apply_detail where pk_pdapdt in (" + pkInsertApDt + ")",
				ExPdApplyDetail.class, new Object[] {});
		String sqlQueryDeQuan = "select pk_pdapdt key_,sum(quan_pack) quan_pack_sum,sum(quan_min) quan_min_sum from ex_pd_de  where pk_pdapdt in("
				+ pkInsertApDt + ") group by pk_pdapdt";
		Map<String, Map<String, Object>> mapPkDtAndDeQuan = DataBaseHelper.queryListToMap(sqlQueryDeQuan, new Object[] {});
		//修改请领明细发放状态的请明细主键集合
		Set<String> deDtList = new HashSet<String>();
		//修改请领明细完成状态的请领明细主键集合
		Set<String> finishDtList = new HashSet<String>();
		for (ExPdApplyDetail epad : applyDetails) {
			double curApQuan = mapPkForDt.get(epad.getPkPdapdt()).getQuanMin().doubleValue();
			double hadDeQuan = 0;
			if (mapPkDtAndDeQuan.get(epad.getPkPdapdt()) != null) {// 已经发过药品
				hadDeQuan = Double.parseDouble(mapPkDtAndDeQuan.get(epad.getPkPdapdt()).get("quanMinSum").toString());
			}
			double tempSum = curApQuan + hadDeQuan;
			if (tempSum < epad.getQuanMin().doubleValue()) {// 部分发药
				   deDtList.add(epad.getPkPdapdt());
				} else {
				   finishDtList.add(epad.getPkPdapdt());
				}
		}


		/**
		 * 当前代码块只为校验请领数量和对应请领明细总发药量是否相等
		 * 如果不相等程序存在问题，目前以限制一次发药操作不会进行部分发放
		 * 另：特别注意静配药品，自备药品无法进行判断，目前静配发药补充此限制需单独处理
		 *    自备药品不生成发药单，不生成费用
		 */
		//checkDeQuanMin(exPdAppDetails, exPdDes);

		int apcnt = 0;
		// 批量更新请领明细发放状态
		if(deDtList!=null&&deDtList.size()>0){
			//不支持同一条明细只发一部分的情况
			StringBuilder pdapdtDeSql=new StringBuilder();
			pdapdtDeSql.append("update ex_pd_apply_detail set flag_de='1',ts=? where (pk_pdapdt in (");
			pdapdtDeSql.append(CommonUtils.convertSetToSqlInPart(deDtList, "pk_pdapdt"));
			pdapdtDeSql.append(")) and flag_de='0' and flag_stop='0' and nvl(flag_canc,'0') <> '1'");
			int updateCount = DataBaseHelper.execute(pdapdtDeSql.toString(), new Date());
			if(updateCount<deDtList.size())
				throw new BusException("您本次提交的药品发放明细中已有被其他人发放完成的记录，请刷新请领单后重新发放！");
			deDtList = null;//使用后清空
			apcnt = updateCount;
		}
		// 批量更新请领明细发放状态及完成标志
		if(finishDtList!=null&&finishDtList.size()>0){
			StringBuilder pdapdtFinSql=new StringBuilder();
			pdapdtFinSql.append("update ex_pd_apply_detail set flag_de='1',flag_finish='1',ts=? where (pk_pdapdt in (");
			pdapdtFinSql.append(CommonUtils.convertSetToSqlInPart(finishDtList, "pk_pdapdt"));
			pdapdtFinSql.append(")) and flag_de='0' and flag_stop='0' and nvl(flag_canc,'0') = '0'");
			int updateCount=DataBaseHelper.execute(pdapdtFinSql.toString(), new Date());
			if(updateCount<finishDtList.size())
				throw new BusException("您本次提交的药品发放明细中已有被其他人发放完成的记录，请刷新请领单后重新发放！");
			finishDtList = null;//使用后清空
		}

		// 批量更新表ex_pd_apply
		StringBuilder pdapSql=new StringBuilder();
		pdapSql.append("update ex_pd_apply set flag_finish='1',ts=? where (pk_pdap in(");
		pdapSql.append(CommonUtils.convertSetToSqlInPart(pkPdApSet, "pk_pdap"));
		pdapSql.append(")) and not exists (select 1  from ex_pd_apply_detail detail  where ex_pd_apply.pk_pdap = detail.pk_pdap");
		pdapSql.append(" and nvl(detail.flag_finish, '0') = '0' and detail.eu_result in ('0','1') and nvl(detail.flag_canc,'0') = '0')");
		DataBaseHelper.execute(pdapSql.toString(), new Date());

		// 批量插入发药记录表
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdDe.class), exPdDes);
		
		// 只要请领单有一条明细发放，那么请领单的状态就是发放状态
		StringBuilder pdapStatusSql=new StringBuilder();
		pdapStatusSql.append("update ex_pd_apply set eu_status='1',ts=? where (pk_pdap in(");
		pdapStatusSql.append(CommonUtils.convertSetToSqlInPart(pkPdApSet, "pk_pdap"));
		pdapStatusSql.append(")) and  exists (select 1  from ex_pd_apply_detail detail  where ex_pd_apply.pk_pdap = detail.pk_pdap and detail.flag_de = '1')");
		pdapStatusSql.append(" and ex_pd_apply.eu_status ='0'");
		DataBaseHelper.execute(pdapStatusSql.toString(), new Date());

		if (cnOrders.size() > 0) {
			String sqlCnOrder = "update cn_order set quan_bed=quan_bed+:quanBed,ts=:ts where pk_cnord=:pkCnord";
			DataBaseHelper.batchUpdate(sqlCnOrder, cnOrders);
		}
		int cgcnt = 0;
		if (blPubParamVos.size() > 0){
			BlPubReturnVo cgrtn =  ipCgPubService.chargeIpBatch(blPubParamVos,false);// 批量计费
			if(cgrtn==null||cgrtn.getBids()==null||cgrtn.getBids().size()<=0){
				logger.info("================发药单【"+codeDe+"】在记费时未产生记费明细================");
			}else{
				cgcnt = cgrtn.getBids().size();
			}
		}
		logger.info("================发药单【"+codeDe+"】对应"+exPdAppDetails.size()+"条请领明细，共生成"+exPdDes.size()+"条发药记录,"+cgcnt+"条记费明细================");
		//更新静配标志
		if(pkAppDtPivas!=null&&pkAppDtPivas.size()>0){
			String sqlExOcc = "update ex_order_occ set flag_pivas='1' where pk_pdapdt in ("+CommonUtils.convertSetToSqlInPart(pkAppDtPivas, "pk_pdapdt")+")";
			DataBaseHelper.execute(sqlExOcc);
		}
		/**
		 * 发送住院发药信息
		 */
		Map<String, Object> map = new HashMap<>();
		map.put("exPdAppDetails",exPdAppDetails);//请领明细
		map.put("exPdDes", exPdDes);//发药明细
		map.put("orc", "OK");//新增
		map.put("IsSendSDEx", true);//深大是否发送医嘱执行消息
		map.put("codeDe", codeDe);//发药单号
		PlatFormSendUtils.sendScmIpDeDrug(map);
		return deDrugVos;
	}

	/**
	 * 校验发药数量是否小于请领数量
	 * @param exPdAppDetails
	 * @param exPdDes
	 */
	private void checkDeQuanMin(List<ExPdApplyDetail> exPdAppDetails, List<ExPdDe> exPdDes) {
		for (ExPdApplyDetail exapdt:exPdAppDetails) {
			if("1".equals(exapdt.getFlagPivas()) ||"1".equals(exapdt.getFlagSelf())) continue;
			List<ExPdDe> temp_delist=exPdDes.stream().filter(m->m.getPkPdapdt().equals(exapdt.getPkPdapdt())).collect(Collectors.toList());
			if(temp_delist==null || temp_delist.size()==0){
				StringBuilder deChkSql=new StringBuilder();
				deChkSql.append("select pv.name_pi,pd.name name_pd,pv.bed_no from pv_encounter pv ");
				deChkSql.append(" inner join ex_pd_apply_detail dt on dt.pk_pv=pv.pk_pv ");
				deChkSql.append(" inner join bd_pd pd on pd.pk_pd=dt.pk_pd ");
				deChkSql.append(" where dt.pk_pdapdt=?");
				Map<String,Object> chkResMap= DataBaseHelper.queryForMap(deChkSql.toString(),exapdt.getPkPdapdt());
				throw new BusException("您本次提交发药数据错误,未生成发药单，" +
						"床号["+ MapUtils.getString(chkResMap,"bedNo") +"]，" +
						"姓名["+MapUtils.getString(chkResMap,"namePi")+"]," +
						"药品["+MapUtils.getString(chkResMap,"namePd")+"]!");
			}
			DoubleStream dequanMin= temp_delist.stream().mapToDouble(m->m.getQuanMin());
			Double deQuanSum=dequanMin.sum();
			if(exapdt.getQuanMin().doubleValue()>deQuanSum.doubleValue()){
				StringBuilder deChkSql=new StringBuilder();
				deChkSql.append("select pv.name_pi,pd.name name_pd,pv.bed_no from pv_encounter pv ");
				deChkSql.append(" inner join ex_pd_apply_detail dt on dt.pk_pv=pv.pk_pv ");
				deChkSql.append(" inner join bd_pd pd on pd.pk_pd=dt.pk_pd ");
				deChkSql.append(" where dt.pk_pdapdt=?");
				Map<String,Object> chkResMap=DataBaseHelper.queryForMap(deChkSql.toString(),exapdt.getPkPdapdt());
				throw new BusException("您本次提交发药数据错误，发药数量小于请领数量，" +
						"床号["+ MapUtils.getString(chkResMap,"bedNo") +"]，" +
						"姓名["+MapUtils.getString(chkResMap,"namePi")+"]," +
						"药品["+MapUtils.getString(chkResMap,"namePd")+"]!");
			}
		}
	}

	/**
	 * 构建发药明细vo
	 * @param exPdApplyDetail
	 * @param pdOutDtParamVo
	 * @param flagPivas
	 * @param userOp
	 */
	private ExPdDe buildExPdDe(ExPdApplyDetail exPdApplyDetail, PdOutDtParamVo pdOutDtParamVo, String flagPivas, User userOp, String codeDe,Date dateDe) {
		if(!"1".equals(exPdApplyDetail.getFlagSelf())&&exPdApplyDetail.getQuanPack().doubleValue()<=0){
			throw new BusException("所选记录包含数量小于等于0的,请核对数据后处理！");
		}
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
		exPdDe.setPkPdstdt(pdOutDtParamVo.getPkPdstdt());
		exPdDe.setPkPv(exPdApplyDetail.getPkPv());
		exPdDe.setPkCnord(exPdApplyDetail.getPkCnord());
		exPdDe.setPkPd(exPdApplyDetail.getPkPd());
		exPdDe.setDateExpire(pdOutDtParamVo.getDateExpire());
		exPdDe.setPkUnit(pdOutDtParamVo.getPkUnitPack());
		exPdDe.setPackSize(pdOutDtParamVo.getPackSize());
		exPdDe.setBatchNo(pdOutDtParamVo.getBatchNo());
		exPdDe.setEuStatus("0");
		exPdDe.setQuanPack(exPdApplyDetail.getQuanPack());
		exPdDe.setQuanMin(exPdApplyDetail.getQuanMin());
		double priceCost=MathUtils.mul(MathUtils.div(pdOutDtParamVo.getPriceCost(), exPdApplyDetail.getPdPackSize().doubleValue()), pdOutDtParamVo.getPackSize()
				.doubleValue());
		priceCost=BigDecimal.valueOf(priceCost).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();//保留6位小数计算，防止结算金额差异0.01

		exPdDe.setPriceCost(priceCost);
		double price = MathUtils.mul(MathUtils.div(pdOutDtParamVo.getPrice(), exPdApplyDetail.getPdPackSize().doubleValue()), pdOutDtParamVo.getPackSize()
				.doubleValue());
		price=BigDecimal.valueOf(price).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();//保留6位小数计算，防止结算金额差异0.01
		exPdDe.setPrice(price);
		exPdDe.setAmount(MathUtils.mul(exPdApplyDetail.getQuanPack().doubleValue() , price));// 包装数量乘以单价
		exPdDe.setPkDeptDe(pkDept);
		exPdDe.setPkStoreDe(pkStock);
		exPdDe.setDateDe(dateDe);
		exPdDe.setPkEmpDe(pkEmp);
		exPdDe.setNameEmpDe(nameEmp);
		exPdDe.setFlagPrt("0");
		exPdDe.setFlagPivas(exPdApplyDetail.getFlagPivas());//取请领明细设置的静配标志
		// 目前静配标志和瓶签标志同步
		exPdDe.setFlagBarcode(flagPivas);
		exPdDe.setNote(null);
	    //设置发放分类
		exPdDe.setPkPddecate(exPdApplyDetail.getPkPddecate());
		exPdDe.setNameDecate(exPdApplyDetail.getNameDecate());
		//如果处方，设置为处方名称
		if(!CommonUtils.isEmptyString(exPdApplyDetail.getPresName())){
			exPdDe.setNameDecate(exPdApplyDetail.getPresName());
			exPdDe.setPkPddecate(exPdApplyDetail.getPkPres());
		}
		exPdDe.setFlagSendtofa("0");//发送至包药机标志:包药机接口使用
		return exPdDe;
	}
	
	/**
	 * 构建计费参数vo
	 * @param exPdApplyDetail
	 * @param pdOutDtParamVo
	 * @param flagPivas
	 * @param userOp
	 */
	 public BlPubParamVo buildBPPV(ExPdApplyDetail exPdApplyDetail, PdOutDtParamVo pdOutDtParamVo, Map<String, Map<String, Object>> mapCnOrderInfo,
			User userOp, ExPdDe exPdDe) {

		String pkOrg = userOp.getPkOrg();
		String pkDept = userOp.getPkDept();
		String pkEmp = userOp.getPkEmp();
		String nameEmp = userOp.getNameEmp();

		BlPubParamVo blPubParamVo = new BlPubParamVo();
		blPubParamVo.setPkOrg(pkOrg);
		blPubParamVo.setEuPvType("3");// 住院
		blPubParamVo.setPkPv(exPdApplyDetail.getPkPv());
		blPubParamVo.setPkPi(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkPi").toString());
		blPubParamVo.setPkOrd(exPdApplyDetail.getPkPd());
		blPubParamVo.setPkItem(exPdApplyDetail.getPkPd());// 药品的话此处传pkPd
		blPubParamVo.setQuanCg(exPdApplyDetail.getQuanPack());// 实际发放数量
		blPubParamVo.setPkOrgEx(pkOrg);// 执行机构
		blPubParamVo.setPkPres(exPdApplyDetail.getPkPres());//处方主键
		blPubParamVo.setPkCnord(exPdApplyDetail.getPkCnord());// 对应的医嘱主键
		if(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkWgOrg")!=null){
			blPubParamVo.setPkWgOrg(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkWgOrg").toString());// 原医疗组
		}
		if(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkWg")!=null){
			blPubParamVo.setPkWgEx(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkWg").toString());// 执行医疗组
		}
		if(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkWg")!=null){
			blPubParamVo.setPkWg(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkWg").toString());// 开立医疗组
		}
		blPubParamVo.setPkOrgApp(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkOrg").toString());// 开立机构
		blPubParamVo.setPkDeptEx(pkDept);// 执行科室
		blPubParamVo.setPkDeptApp(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkDept").toString());// 开立科室
		blPubParamVo.setPkEmpApp(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkEmpOrd").toString());// 开立医生
		blPubParamVo.setNameEmpApp(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("nameEmpOrd").toString());// 开立医生姓名
		blPubParamVo.setFlagFit(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("flagFit").toString());//适应症用药标志
		blPubParamVo.setDescFit(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("descFit")==null?null:mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("descFit").toString());//适应症用药描述
		blPubParamVo.setFlagPd("1");
		blPubParamVo.setFlagPv("0");
		blPubParamVo.setDateHap(new Date());
		blPubParamVo.setPkDeptCg(pkDept);
		blPubParamVo.setPkEmpCg(pkEmp);
		blPubParamVo.setNameEmpCg(nameEmp);
		
		double price = MathUtils.mul(MathUtils.div(pdOutDtParamVo.getPrice(), exPdApplyDetail.getPdPackSize().doubleValue()), pdOutDtParamVo.getPackSize()
				.doubleValue());
		price=BigDecimal.valueOf(price).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();//保留6位小数计算，防止结算金额差异0.01
		blPubParamVo.setPrice(price);
		blPubParamVo.setBatchNo(pdOutDtParamVo.getBatchNo());
		blPubParamVo.setDateExpire(pdOutDtParamVo.getDateExpire());
		blPubParamVo.setPkUnitPd(pdOutDtParamVo.getPkUnitPack());
		blPubParamVo.setPackSize(pdOutDtParamVo.getPackSize());
		double priceCost = MathUtils.mul(MathUtils.div(pdOutDtParamVo.getPriceCost(), exPdApplyDetail.getPdPackSize().doubleValue()), pdOutDtParamVo.getPackSize()
				.doubleValue());
		priceCost=BigDecimal.valueOf(priceCost).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();//保留6位小数计算，防止结算金额差异0.01
		blPubParamVo.setPriceCost(priceCost);
		blPubParamVo.setPkOrdexdt(exPdDe.getPkPdde());
		blPubParamVo.setPkPdstdt(exPdDe.getPkPdstdt());
		blPubParamVo.setInfantNo(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("infantNo").toString());
		blPubParamVo.setPkDeptNsApp(mapCnOrderInfo.get(exPdApplyDetail.getPkCnord()).get("pkDeptNs").toString());
		blPubParamVo.setEuBltype("1");//计费类型
		return blPubParamVo;
	}
	/**
	 * 根据请领明细，查询执行数量
	 * @param pk_pdapdt
	 * @return
	 */
	private Double getExQuanMin(String pk_pdapdt){
		String sql = " select sum(quan_occ) as quan_occ from ex_order_occ where pk_pdapdt = ?";
		return  DataBaseHelper.queryForScalar(sql,Double.class, new Object[]{pk_pdapdt});
	}
	
}
