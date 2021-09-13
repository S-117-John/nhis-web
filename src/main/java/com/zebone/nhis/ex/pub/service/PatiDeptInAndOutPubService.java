package com.zebone.nhis.ex.pub.service;

import com.zebone.nhis.base.pub.service.BdResPubService;
import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pv.*;
import com.zebone.nhis.common.service.EmrPubService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.ex.nis.pi.vo.PvIpVo;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
/**
 * 转科、入科公共服务
 * @author yangxue
 *
 */
@Service
public class PatiDeptInAndOutPubService {

	@Resource
	private ExPubService exPubService;

	@Resource
	private EmrPubService emrPubService;

	@Resource
	private BdResPubService bdResPubService;//处理婴儿床位公共类

	/**
	 * 保存入科信息
	 * @param param
	 * @param user
	 */
	public void saveDeptInInfo(Map<String,Object> param,IUser user){
		param.put("flagAdmit", "1");//设置入院标志
		this.saveAdtInfo(param,user);
		//2020-02-25 手动分床模式不删除床位
		String BD0013 = ApplicationUtils.getSysparam("BD0013",false);
		String adtType = param.containsKey("adtType") ? param.get("adtType") != null ? param.get("adtType").toString() :"" : "";
		if("1".equals(BD0013) && adtType.equals("新出生")){
			param = this.saveChgInf(user,param);
		}
		this.savePatiBedInfo(param, user,"1");
		//2019-07-04 婴儿不需要处理：固定费用、医疗组、医护人员
		if(null == param.get("flagInfant") || !"1".equals(param.get("flagInfant"))){
			this.saveDayCgInfo(param, user);
			this.saveClincStaffInfo(param, user);
			this.saveClincGrpInfo(param, user);
		}
//			this.setPatiCredit(param, user);//设置患者担保记录
		String date = param.get("dateAdmit").toString();
		if(!CommonUtils.isEmptyString(date)){
			Date dateAdmit = DateUtils.strToDate(date);
			param.put("dateAdmit", dateAdmit);
		}else{
			param.put("dateAdmit", new Date());
		}

		StringBuilder sql = new StringBuilder(" update pv_encounter set eu_status = '1' ,flag_in = '1',date_admit=:dateAdmit, ");
		if(null != param.get("pkIpWg") && !CommonUtils.isEmptyString(param.get("pkIpWg").toString()))
			sql.append(" pk_wg=:pkIpWg,");
		sql.append("bed_no=:codeIpBed, pk_emp_phy=:pkIpPsnMain, name_emp_phy=:nameIpPsnMain,");

		//针对中二改造，处理入院时间
		if(null != param.get("dateBegin") && !CommonUtils.isEmptyString(param.get("dateBegin").toString())){
			Date dateBegin = DateUtils.strToDate(param.get("dateBegin").toString());
			param.put("dateBegin", dateBegin);
			sql.append(" date_begin=:dateBegin,");
		}
		//2019-03-02 针对中二改造，入科接收时，处理患者分类【更新为传入值】
		if(null != param.get("pkPicateNew") && !CommonUtils.isEmptyString(param.get("pkPicateNew").toString())
				&& !param.get("pkPicateNew").equals(param.get("pkPicate"))){
			sql.append(" pk_picate=:pkPicateNew, flag_spec=(select flag_spec from pi_cate where pk_picate=:pkPicateNew and del_flag='0'),");
		}

		if(CommonUtils.isNotNull(param.get("pkDept"))){
			sql.append(" pk_dept=:pkDept,");
		}
		if(CommonUtils.isNotNull(param.get("pkDeptNs"))){
			sql.append(" pk_dept_ns=:pkDeptNs,");
		}

		sql.append(" pk_emp_ns=:pkIpPsnNs, name_emp_ns=:nameIpPsnNs  where pk_pv=:pkPv and eu_status='0' and (bed_no='' or bed_no is null) ");
		int count = DataBaseHelper.update(sql.toString(), param);
		if(count<1)
			throw new BusException("接收失败！该患者可能已被他人接收，请刷新后重试！");
	}

	/**
	 * 婴儿分配床位相关信息
	 * @param user
	 * @param paramMap
	 */
	private Map<String, Object> saveChgInf(IUser user, Map<String, Object> paramMap) {
		List<Map<String,Object>> infList =(List<Map<String,Object>>)paramMap.get("infList");
		User u = (User)user ;
		if(null != infList && infList.size() > 0){
			String pkDeptNs = CommonUtils.getString(paramMap.get("pkDeptNs"));
			String bedNoMa = CommonUtils.getString(paramMap.get("codeIpBed"));
			//获取母亲所在床位
			BdResBed bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where pk_pi = ? and pk_ward = ? and code = ? "
					, BdResBed.class, new Object[]{CommonUtils.getString(paramMap.get("pkPi")),
							CommonUtils.getString(paramMap.get("pkDeptNs")),bedNoMa});

			if(bedMa == null){
				bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where pk_ward = ? and code = ? "
						, BdResBed.class, new Object[]{CommonUtils.getString(paramMap.get("pkDeptNs")),bedNoMa});
			}
			String bedSpc = ExSysParamUtil.getSpcOfCodeBed();//获取婴儿床位分隔符
			if(CommonUtils.isEmptyString(bedSpc))
				throw new BusException("请维护系统参数【BD0007】-婴儿床位编码分隔符！");
			String sortNo = "";
			for (Map<String, Object> map : infList) {
				sortNo = CommonUtils.getString(map.get("sortNo"));
				//转科是否留在原床位 -- 在这里清空原床位
				String stayBeforeBed = ApplicationUtils.getSysparam("PV0024",false);
				if("1".equals(stayBeforeBed)){
					String pk_pv = map.get("pkPv").toString();
					//婴儿单独转科时先查询本病区存在同一个母亲的其他婴儿的陪护床位
					if(pk_pv.equals(CommonUtils.getString(paramMap.get("pkPv")))){
						String queSql = " select ip.pk_bed_an,bed.pk_ward" +
								" from pv_ip ip" +
								" inner join PV_INFANT inf on inf.PK_PV_INFANT = ip.pk_pv" +
								" inner join bd_res_bed bed on bed.pk_bed = ip.pk_bed_an" +
								" inner join PV_BED pb on pb.pk_bed_ward = bed.pk_ward and pb.bedno = bed.code" +
								" inner join (select * from PV_INFANT where PK_PV_INFANT = ?) oth on oth.pk_pv = inf.pk_pv" +
								" where inf.PK_PV_INFANT != ?";
						String queSqlM = " select bed.* " +
								" from bd_res_bed bed " +
								" inner join PV_BED pv on pv.PK_BED_WARD = bed.pk_ward and pv.bedno = bed.code " +
								" inner join PV_INFANT inf on inf.pk_pv = pv.pk_pv " +
								" where inf.PK_PV_INFANT = ? and bed.pk_ward = ? and pv.date_end is null ";
						BdResBed bdResBed = DataBaseHelper.queryForBean(queSqlM,BdResBed.class,pk_pv,CommonUtils.getString(paramMap.get("pkDeptNs")));
						List<PvIpVo> pkIpList = DataBaseHelper.queryForList(queSql,PvIpVo.class,pk_pv,pk_pv);
						if(pkIpList.size() > 0){//存在同个母亲其他婴儿的陪护则用同一个陪护床位
							PvIpVo pvip = pkIpList.get(0);
							if(StringUtils.isNotBlank(pvip.getPkWard()) && StringUtils.isNotBlank(pvip.getPkBedAn()) && paramMap.get("pkDeptNs").equals(pvip.getPkWard())){
								String updSql = "update pv_ip set pk_bed_an = ? where pk_pv = ?";
								DataBaseHelper.update(updSql,new Object[]{pvip.getPkBedAn(),pk_pv});
							}
						}else if(bdResBed != null){
							String updSql = "update pv_ip set pk_bed_an = null where pk_pv = ?";
							DataBaseHelper.update(updSql,new Object[]{pk_pv});
						}else{//不存在则设置选中的大人床为陪护床
							String updSql = "update pv_ip set pk_bed_an = ? where pk_pv = ?";
							DataBaseHelper.update(updSql,new Object[]{bedMa.getPkBed(),pk_pv});
						}
					}
				}

				List<BdResBed> bedList = bdResPubService.isHaveBedAndAdd(pkDeptNs, bedMa, bedNoMa + bedSpc + sortNo, null, (User)user);
				if(null == bedList || bedList.size() < 1)
					throw new BusException("维护婴儿床位失败，无法保存婴儿信息！");

				String pkBedInf = bedList.get(0).getPkBed();
				int cntBed = DataBaseHelper.update("update bd_res_bed set flag_active = '1', del_flag = '0' "
						+ "where pk_bed = ? and del_flag = '1' and pk_pi is null", new Object[]{pkBedInf});
				if(cntBed < 1)
					throw new BusException("当前婴儿床位已被占用，无法保存婴儿信息！");

				//添加对应的婴儿床位费
				if(bedList.size() > 0 ) {
					//2020-05-26 获取婴儿床位费编码
					String BD0010 = ApplicationUtils.getDeptSysparam("BD0010", CommonUtils.getString(paramMap.get("pkDeptNs")));
					String queSql = "select * from BD_ITEM where code = ?";
					BdItem bdItem = DataBaseHelper.queryForBean(queSql, BdItem.class, BD0010);
					//添加对应的婴儿床位费
					String updSql = "update BD_RES_BED set PK_ITEM = ? where PK_BED = ? ";
					if (bdItem != null) {
						DataBaseHelper.update(updSql, new Object[]{bdItem.getPkItem(), pkBedInf});
					}
				}
				paramMap.put("pkPv", map.get("pkPv"));
				paramMap.put("pkPi", map.get("pkPi"));
				paramMap.put("pkAdt", map.get("pkAdt"));
				paramMap.put("flagInfant", map.get("flagInfant"));
				paramMap.put("codeIpBed", bedNoMa + bedSpc + sortNo);
				paramMap.put("pkIpBed", pkBedInf);
			}
		}
		return paramMap;
	}


	/**
	 * 保存转科患者的入科信息
	 * @param param
	 * @param user
	 */
	public void saveDeptOutInfo(Map<String,Object> param ,IUser user){
		this.savePatiBedInfo(param, user,"1");
		this.saveClincStaffInfo(param, user);
		this.saveClincGrpInfo(param, user);
		this.saveDayCgInfo(param, user);
		User u = (User)user;
		String pk_adt = param.get("pkAdt").toString();
		Date dateCh = new Date();
		String date = CommonUtils.getString(param.get("dateAdmit"));
		if(!CommonUtils.isEmptyString(date)){
			dateCh = DateUtils.strToDate(date);
		}
		//更新转科记录信息
		String adtsql = "update pv_adt set eu_status = '1',date_begin = :beginDate,pk_emp_begin = :pkEmp,name_emp_begin = :nameEmp where pk_adt = :pkAdt  and eu_status='0'";
		Map<String,Object> upMap = new HashMap<String,Object>();
		upMap.put("beginDate", dateCh);
		upMap.put("pkEmp", u.getPkEmp());
		upMap.put("nameEmp", u.getNameEmp());
		upMap.put("pkAdt", pk_adt);
		int count = DataBaseHelper.update(adtsql, upMap);
		if(count<1)
			throw new BusException("接收失败！该患者可能已被他人接收，请刷新后重试！");
		//查询当前转入科室及病区，更新至就诊记录表
		PvAdt adt = DataBaseHelper.queryForBean("select * from pv_adt where pk_adt = ?", PvAdt.class, pk_adt);
		//更新就诊记录信息
		StringBuilder sql = new StringBuilder(" update pv_encounter set pk_wg = :pkIpWg,bed_no=:codeIpBed,pk_emp_phy=:pkIpPsnMain,name_emp_phy=:nameIpPsnMain, ");

		//2019-03-02 针对中二改造，转科时处理患者分类【录入则直接更新，否则赋默认值】
		if(null != param.get("pkPicateNew") && !CommonUtils.isEmptyString(param.get("pkPicateNew").toString())
				&& !param.get("pkPicateNew").equals(param.get("pkPicate"))){
			sql.append(" pk_picate=:pkPicateNew, flag_spec=(select flag_spec from pi_cate where pk_picate=:pkPicateNew and del_flag='0'),");
		}else{
			//转科接收时，从特诊转入时设置患者类型默认值
			if(null != param.get("flagSpec") && "1".equals(param.get("flagSpec").toString())){
				List<PiCate> picates = DataBaseHelper.queryForList("select * from PI_CATE where del_flag = '0' and flag_spec = '0' order by flag_def desc ", PiCate.class, new Object[]{});
				if(null != picates && picates.size() > 0){
					param.put("pkPicateNew", picates.get(0).getPkPicate());
					param.put("flagSpec", picates.get(0).getFlagSpec());
					sql.append(" pk_picate=:pkPicateNew, flag_spec=:flagSpec,");
				}
			}
		}

		sql.append(" pk_emp_ns=:pkIpPsnNs,name_emp_ns=:nameIpPsnNs,pk_dept=:pvAdtpkDept,pk_dept_ns=:pvAdtpkDeptNs  where pk_pv = :pkPv ");
		param.put("pvAdtpkDept", adt.getPkDept());
		param.put("pvAdtpkDeptNs", adt.getPkDeptNs());
		DataBaseHelper.update(sql.toString(), param);

		//2019-05-28 针对转科不停嘱处理
		Map<String,Object> ordUpdateMap = new HashMap<String,Object>();
		ordUpdateMap.put("pkPv", param.get("pkPv"));
		ordUpdateMap.put("pkDeptNs", param.get("pkDeptNs"));
		ordUpdateMap.put("pkDeptNsOld", param.get("pkDeptNsOld"));
		ordUpdateMap.put("dtDepttype", param.get("dtDepttype"));
		ordUpdateMap.put("dtDepttypeOld", param.get("dtDepttypeOld"));
        ordUpdateMap.put("pkDept", param.get("pkDept"));
        ordUpdateMap.put("pkDeptOld", param.get("pkDeptOld"));
        ordUpdateMap.put("flagNoStop",param.get("flagNoStop"));
		updateOrdByNoStop(ordUpdateMap);

		//2019-05-22 针对病历主管医生
		Map<String,Object> emrUpdateMap = new HashMap<String,Object>();
		emrUpdateMap.put("pkPv", param.get("pkPv"));//同步病历医护人员：pkPv(必填)
		emrUpdateMap.put("pkEmpRefer", param.get("pkIpPsnMain"));//组装同步病历医护人员：pkEmpRefer主管医生 => 住院医师
		emrUpdateMap.put("pkEmpNsCharge", param.get("pkIpPsnNs"));//组装同步病历医护人员：pkEmpNsCharge主管护士
		if(!CommonUtils.isEmptyString("pkDoctUpper"))
			emrUpdateMap.put("pkEmpConsult", param.get("pkDoctUpper"));//组装同步病历医护人员：pkEmpConsult上级医师
		if(!CommonUtils.isEmptyString("pkIpPschief"))
			emrUpdateMap.put("pkEmpDirector", param.get("pkIpPschief"));//组装同步病历医护人员：pkEmpDirector主任医师
		if(!CommonUtils.isEmptyString("pkSuperNs"))
			emrUpdateMap.put("pkEmpNsHead", param.get("pkSuperNs"));//组装同步病历医护人员：pkEmpNsCharge护士长
		emrPubService.updateEmrPatRec(emrUpdateMap);//同步病历医护人员

		//转科是否留在原床位 -- 在这里清空原床位
		String stayBeforeBed = ApplicationUtils.getSysparam("PV0024",false);
		if("1".equals(stayBeforeBed)){
			//针对博爱医院，将清空原科室床位信息移至此处执行--针对中二还原为转科不占床
			//查询原转出科室及病区，更新至就诊记录表
			PvAdt adtSource = DataBaseHelper.queryForBean("select * from pv_adt where pk_adt = ?", PvAdt.class, adt.getPkAdtSource());
			String pk_pi = param.get("pkPi").toString();
			//查询原科室就诊床位
			Map<String,Object> bedMap = DataBaseHelper.queryForMap("select bedno,pk_pvbed from pv_bed where pk_dept = ? and pk_dept_ns = ? and pk_pv = ? and pk_bed_ward = ?  and date_end is null ", new Object[]{adtSource.getPkDept(),adtSource.getPkDeptNs(),param.get("pkPv"),adtSource.getPkDeptNs()});
			bedMap.put("dateCh", adtSource.getDateEnd());
			bedMap.put("pkEmp", adtSource.getPkEmpEnd());
			bedMap.put("nameEmp", adtSource.getNameEmpEnd());

			//更新就诊床位记录
			DataBaseHelper.update("update pv_bed set date_end = :dateCh,pk_emp_end=:pkEmp,name_emp_end=:nameEmp where pk_pvbed = :pkPvbed ", bedMap);
			String bed_update = "update bd_res_bed set pk_pi = null,flag_ocupy='0',pk_dept_used = null ,eu_status = '01' where code = ? and pk_pi = ? and pk_ward= ?  and pk_org = ?";
			DataBaseHelper.update(bed_update, new Object[]{bedMap.get("bedno"),pk_pi,adtSource.getPkDeptNs(),u.getPkOrg()});
		}
	}

	/**
	 * 保存接收记录
	 * @param param
	 * @param user
	 */
	public PvAdt saveAdtInfo(Map<String,Object> param,IUser user){
		User u = (User)user;
		Date dateCh = new Date();
		if(param.get("adtType")!=null&&(param.get("adtType").equals("入院")||param.get("adtType").equals("新出生"))){
			String date = CommonUtils.getString(param.get("dateAdmit"));
			if(!CommonUtils.isEmptyString(date)){
				dateCh = DateUtils.strToDate(date);
			}
		}else{
			if(CommonUtils.isNotNull(param.get("dateCh"))) {
				try {
					dateCh = DateUtils.getDefaultDateFormat().parse(CommonUtils.getString(param.get("dateCh")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		String flag_admit = CommonUtils.getString(param.get("flagAdmit"));
		PvAdt adt = new PvAdt();
		adt.setPkPv(param.get("pkPv").toString());
		adt.setDateBegin(dateCh);//入科时间
		adt.setFlagAdmit(flag_admit);
		adt.setPkDept(param.get("pkDept").toString());
		adt.setPkDeptNs(param.get("pkDeptNs").toString());
		adt.setPkEmpBegin(u.getPkEmp());
		adt.setPkOrg(u.getPkOrg());
		adt.setNameEmpBegin(u.getNameEmp());
		adt.setEuStatus(CommonUtils.getString(param.get("state")));//转科的插入
		if(!"".equals(CommonUtils.getString(param.get("pkAdt")))){
			adt.setPkAdtSource(CommonUtils.getString(param.get("pkAdt")));//转科的插入
		}
		if ("1".equals(CommonUtils.getString(param.get("flagNone")))) {
			adt.setFlagNone("1");
		}
		DataBaseHelper.insertBean(adt);
		return adt;
	}

	/**
	 * 保存固定费用信息
	 * @param param
	 * @param user
	 */
	public void saveDayCgInfo(Map<String,Object> param,IUser user){
		//2019-07-02 针对婴儿录入时无需添加固定记费，作此判断
		if(null == param.get("euDaycgmode") && null == param.get("daycgList")) return;
		String eu_cgmode = param.get("euDaycgmode").toString();
		String pk_pv = param.get("pkPv").toString();
		String pk_org = param.get("pkOrg")==null?"":param.get("pkOrg").toString();
		param.put("dateNow", new Date());
		//插入之前先查询，如果有固定费用，更新，否则插入
		PvDaycg cgvo = DataBaseHelper.queryForBean("select * from pv_daycg where pk_pv = ?", PvDaycg.class, new Object[]{pk_pv});
		String pk_daycg = "";
		if(cgvo == null){
			//插入固定费用
			PvDaycg daycg = new PvDaycg();
			daycg.setEuDaycgmode(eu_cgmode);
			daycg.setPkPv(pk_pv);
			daycg.setPkOrg(pk_org);
			DataBaseHelper.insertBean(daycg);
			pk_daycg = daycg.getPkDaycg();
		}else{//更新
			pk_daycg = cgvo.getPkDaycg();
			DataBaseHelper.update("update pv_daycg set eu_daycgmode = :euDaycgmode,ts=:dateNow where pk_pv = :pkPv", param);
			//明细删掉，全部重新插入
			DataBaseHelper.execute("delete from pv_daycg_detail where pk_daycg = ?", new Object[]{pk_daycg});
		}
		//插入固定费用明细
		List<Map<String,Object>> cgList = (List<Map<String,Object>>)param.get("daycgList");
		if(cgList == null||cgList.size()<=0) return;
		List<PvDaycgDetail> dayCgDetailList = new ArrayList<PvDaycgDetail>();
		for(Map<String,Object> cgMap:cgList){
			if(cgMap == null) continue;
			PvDaycgDetail daycgDetail = new PvDaycgDetail();
			daycgDetail.setPkDaycgdt(NHISUUID.getKeyId());
			daycgDetail.setPkDaycg(pk_daycg);
			daycgDetail.setBeginDay(CommonUtils.getLongObject(cgMap.get("dayBegin")));
			daycgDetail.setBeginMonth(CommonUtils.getLongObject(cgMap.get("monthBegin")));
			daycgDetail.setEndDay(CommonUtils.getLongObject(cgMap.get("dayEnd")));
			daycgDetail.setEndMonth(CommonUtils.getLongObject(cgMap.get("monthEnd")));
			daycgDetail.setEuCgmode(CommonUtils.getString(cgMap.get("euCgmode")));
			daycgDetail.setFlagActive("1");
			daycgDetail.setPkItem(CommonUtils.getString(cgMap.get("pkItem"), ""));
			daycgDetail.setPkOrg(pk_org);
			dayCgDetailList.add(daycgDetail);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvDaycgDetail.class), dayCgDetailList);
	}

	/**
	 * 保存患者床位记录信息，插入pv_bed，同时更新bd_res_bed表的pk_pi
	 * @param param
	 * @param user
	 */
	public void savePatiBedInfo(Map<String,Object> param,IUser user,String flagMaj){
		if(param.get("codeIpBed")==null||param.get("codeIpBed").toString().equals(""))
			return;
		User u = (User)user;
		String pk_bed = param.get("pkIpBed").toString();
		String pk_pi = param.get("pkPi").toString();
		//入科时间
		Date dateCh = new Date();
		if(param.get("adtType")!=null&&(param.get("adtType").equals("入院")||param.get("adtType").equals("新出生"))){
			String date = CommonUtils.getString(param.get("dateAdmit"));
			if(!CommonUtils.isEmptyString(date)) {
				dateCh = DateUtils.strToDate(date);
			}
		}
		//床位信息实体
		PvBed bed = new PvBed();
		bed.setBedno(param.get("codeIpBed").toString());
		bed.setDateBegin(dateCh);
		bed.setNameEmpBegin(u.getNameEmp());
		bed.setPkEmpBegin(u.getPkEmp());
		bed.setPkDeptNs(param.get("pkDeptNs").toString());
		BdResBed bedvo = exPubService.getBedInfoByPk(pk_bed, user);
		if(bedvo!=null){
			if(!bedvo.getPkWard().equals(bed.getPkDeptNs()))
				throw new BusException("当前接收床位【"+param.get("codeIpBed").toString()+"】非当前病区床位，请重新选择！");
			bed.setPkBedWard(bedvo.getPkWard());
		}else{
			throw new BusException("当前接收床位【"+param.get("codeIpBed").toString()+"】传入主键不存在，请重新选择！");
		}
		bed.setPkDept(param.get("pkDept").toString());
		bed.setFlagMaj(flagMaj);//主床位标志
		bed.setPkPv(param.get("pkPv").toString());
		bed.setEuHold("0");//正常
		//保存就诊床位信息
		DataBaseHelper.insertBean(bed);
		Object obj[] = new Object[]{pk_pi,pk_bed};
		//更新bd_res_bed的使用患者pk_pi和占用状态
		int cnt = DataBaseHelper.update("update bd_res_bed set pk_pi = ?,eu_status='02',flag_ocupy='1' where pk_bed = ? and flag_ocupy='0' and flag_active = '1' ", obj);
		if(cnt<=0)
			throw new BusException("床位【"+param.get("codeIpBed").toString()+"】已经被占用或未启用，请刷新后重试！");
		//如果该患者在本病区（产科）存在婴儿占用的陪护床位，患者入科时婴儿的PV_IP表中的占用床位主键置空
		String pkPv = param.get("pkPv").toString();
		String pkDept = param.get("pkDept").toString();
		String pkDeptNs = param.get("pkDeptNs").toString();
		String pkDeptOld = param.containsKey("pkDeptOld") ? param.get("pkDeptOld") != null ? param.get("pkDeptOld").toString() :"" : "";
		String pkDeptNsOld = param.containsKey("pkDeptNsOld") ? param.get("pkDeptNsOld") != null ? param.get("pkDeptNsOld").toString() : "" : "";
		String queSql = " select pi.* " +
						" from  pv_ip pi " +
						" inner join PV_INFANT inf on pi.PK_PV = inf.PK_PV_INFANT " +
						" where inf.pk_pv = ?";
		List<PvIp> pkIpList = new ArrayList<>();
		if(StringUtils.isNotBlank(pkPv)&& StringUtils.isNotBlank(pkDeptNs)){
			pkIpList = DataBaseHelper.queryForList(queSql,PvIp.class,pkPv);
			if(pkIpList.size() > 0){
				for(PvIp pvIp:pkIpList){
					if(pk_bed.equals(pvIp.getPkBedAn())){
						DataBaseHelper.update("update pv_ip set pk_bed_an = null where pk_pvip = ?",new Object[]{pvIp.getPkPvip()});
					}
				}
			}
		}

		//患者从产科转往非产科时，判断在产科是否存在婴儿，如果存在则设置婴儿的陪护床位为患者原来的床位
		String getLaborCode = ApplicationUtils.getSysparam("CN0002",false);
		if(getLaborCode != null){
			List<Map<String, Object>> deptList = DataBaseHelper.queryForListFj("select * from BD_OU_DEPT where pk_dept = ? or pk_dept = ?",pkDept,pkDeptOld);
			if(deptList.size() > 0){
				Map<String, Object> newDept = new HashMap<>();
				Map<String, Object> oldDept = new HashMap<>();
				for(Map<String, Object> dept : deptList){
					if(dept.get("pkDept").toString().equals(pkDept)){
						newDept = dept;
					}else if(dept.get("pkDept").toString().equals(pkDeptOld)){
						oldDept = dept;
					}
				}
				String newDeptCode = newDept.size() > 0 ? newDept.get("codeDept").toString() : "";
				String oldDeptCode = oldDept.size() > 0 ? oldDept.get("codeDept").toString() : "";
				Map<String, Object> data = new HashMap<>();
				data.put("pkDeptOld",pkDeptOld);
				data.put("pkPv",pkPv);
				data.put("pkDeptNsOld",pkDeptNsOld);
				BdResBed bdResBed = bdResPubService.queMomBed(data);
				if(getLaborCode.indexOf(oldDeptCode) >= 0 && getLaborCode.indexOf(newDeptCode) < 0){
					if(pkIpList.size() > 0 && bdResBed != null){
						for(PvIp pvIp:pkIpList){
							if(StringUtils.isNotBlank(bdResBed.getPkBed()) && StringUtils.isNotBlank(pvIp.getPkPvip())){
								DataBaseHelper.update("update pv_ip set pk_bed_an = ? where pk_pvip = ?",new Object[]{bdResBed.getPkBed(),pvIp.getPkPvip()});
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 保存患者医疗组信息
	 * @param param
	 * @param user
	 */
	public void saveClincGrpInfo(Map<String,Object> param,IUser user){
		if(param.get("pkIpWg")==null||param.get("pkIpWg").toString().equals("")){
			return;
		}
		//入科时间
		Date dateCh = new Date();
		if(param.get("adtType")!=null&&(param.get("adtType").equals("入院")||param.get("adtType").equals("新出生"))){
			String date = CommonUtils.getString(param.get("dateAdmit"));
			if(!CommonUtils.isEmptyString(date)){
				dateCh = DateUtils.strToDate(date);
			}
		}
		//new 医疗组信息实体
		PvClinicGroup wg = new PvClinicGroup();
		wg.setDateBegin(dateCh);
		wg.setEuStatus("1");
		wg.setPkDept(param.get("pkDept").toString());
		wg.setPkDeptNs(param.get("pkDeptNs").toString());
		wg.setPkPv(param.get("pkPv").toString());
		wg.setPkWg(param.get("pkIpWg").toString());
		DataBaseHelper.insertBean(wg);
	}

	/**
	 * 保存患者医护人员信息
	 * @param param
	 * @param user
	 */
	public void saveClincStaffInfo(Map<String,Object> param,IUser user){
		List<PvStaff> staffList = new ArrayList<PvStaff>();
		String pk_pv = param.get("pkPv").toString();
		String pk_dept_ns = param.get("pkDeptNs").toString();
		String pk_dept = param.get("pkDept").toString();
		String pk_org = ((User)user).getPkOrg();
		//入科时间
		Date dateCh = new Date();
		if(param.get("adtType")!=null&&(param.get("adtType").equals("入院")||param.get("adtType").equals("新出生"))){
			String date = CommonUtils.getString(param.get("dateAdmit"));
			if(!CommonUtils.isEmptyString(date)) {
				dateCh = DateUtils.strToDate(date);
			}
		}
		//质控医生0005
		if(param.get("pkDoctQa")!=null&&!param.get("pkDoctQa").toString().equals("")){
			PvStaff qa = new PvStaff();
			qa.setPkStaff(NHISUUID.getKeyId());
			qa.setDateBegin(dateCh);
			qa.setDtRole(IDictCodeConst.DT_EMPROLE_QADOCT);
			qa.setNameEmp(param.get("nameDoctQa").toString());
			qa.setPkEmp(param.get("pkDoctQa").toString());
			qa.setPkDeptNs(pk_dept_ns);
			qa.setPkDept(pk_dept);
			qa.setPkPv(pk_pv);
			qa.setTs(new Date());
			qa.setPkOrg(pk_org);
			staffList.add(qa);
		}
		//上级医生0006
		if(param.get("pkDoctUpper")!=null&&!param.get("pkDoctUpper").toString().equals("")){
			PvStaff qa = new PvStaff();
			qa.setPkStaff(NHISUUID.getKeyId());
			qa.setDateBegin(dateCh);
			qa.setDtRole(IDictCodeConst.DT_EMPROLE_UPPERDOCT);
			qa.setNameEmp(param.get("nameDoctUpper").toString());
			qa.setPkEmp(param.get("pkDoctUpper").toString());
			qa.setPkDeptNs(pk_dept_ns);
			qa.setPkDept(pk_dept);
			qa.setPkPv(pk_pv);
			qa.setTs(new Date());
			qa.setPkOrg(pk_org);
			staffList.add(qa);
		}
		//主任医生0000
		if(param.get("pkIpPschief")!=null&&!param.get("pkIpPschief").toString().equals("")){
			PvStaff qa = new PvStaff();
			qa.setPkStaff(NHISUUID.getKeyId());
			qa.setDateBegin(dateCh);
			qa.setDtRole(IDictCodeConst.DT_EMPROLE_HEADDOCT);
			qa.setNameEmp(param.get("nameIpPschief").toString());
			qa.setPkEmp(param.get("pkIpPschief").toString());
			qa.setPkDeptNs(pk_dept_ns);
			qa.setPkDept(pk_dept);
			qa.setPkPv(pk_pv);
			qa.setTs(new Date());
			qa.setPkOrg(pk_org);
			staffList.add(qa);
		}
		//实习医生0004
		if(param.get("pkIpPsnIntern")!=null&&!param.get("pkIpPsnIntern").toString().equals("")){
			PvStaff qa = new PvStaff();
			qa.setPkStaff(NHISUUID.getKeyId());
			qa.setDateBegin(dateCh);
			qa.setDtRole(IDictCodeConst.DT_EMPROLE_REFDOCT);
			qa.setNameEmp(param.get("nameIpPsnIntern").toString());
			qa.setPkEmp(param.get("pkIpPsnIntern").toString());
			qa.setPkDeptNs(pk_dept_ns);
			qa.setPkDept(pk_dept);
			qa.setPkPv(pk_pv);
			qa.setTs(new Date());
			qa.setPkOrg(pk_org);
			staffList.add(qa);
		}
		//管床医生0001
		if(param.get("pkIpPsnMain")!=null&&!param.get("pkIpPsnMain").toString().equals("")){
			PvStaff qa = new PvStaff();
			qa.setPkStaff(NHISUUID.getKeyId());
			qa.setDateBegin(dateCh);
			qa.setDtRole(IDictCodeConst.DT_EMPROLE_MAINDOCT);
			qa.setNameEmp(param.get("nameIpPsnMain").toString());
			qa.setPkEmp(param.get("pkIpPsnMain").toString());
			qa.setPkDeptNs(pk_dept_ns);
			qa.setPkDept(pk_dept);
			qa.setPkPv(pk_pv);
			qa.setTs(new Date());
			qa.setPkOrg(pk_org);
			staffList.add(qa);
		}
		//责任护士0100
		if(param.get("pkIpPsnNs")!=null&&!param.get("pkIpPsnNs").toString().equals("")){
			PvStaff qa = new PvStaff();
			qa.setPkStaff(NHISUUID.getKeyId());
			qa.setDateBegin(dateCh);
			qa.setDtRole(IDictCodeConst.DT_EMPROLE_MAINNS);
			qa.setNameEmp(param.get("nameIpPsnNs").toString());
			qa.setPkEmp(param.get("pkIpPsnNs").toString());
			qa.setPkDeptNs(pk_dept_ns);
			qa.setPkDept(pk_dept);
			qa.setPkPv(pk_pv);
			qa.setTs(new Date());
			qa.setPkOrg(pk_org);
			staffList.add(qa);
		}
		//护士长0101
		if(param.get("pkSuperNs")!=null&&!param.get("pkSuperNs").toString().equals("")){
			PvStaff qa = new PvStaff();
			qa.setPkStaff(NHISUUID.getKeyId());
			qa.setDateBegin(dateCh);
			qa.setDtRole(IDictCodeConst.DT_EMPROLE_HEADNS);
			qa.setNameEmp(param.get("nameSuperNs").toString());
			qa.setPkEmp(param.get("pkSuperNs").toString());
			qa.setPkDeptNs(pk_dept_ns);
			qa.setPkDept(pk_dept);
			qa.setPkPv(pk_pv);
			qa.setTs(new Date());
			qa.setPkOrg(pk_org);
			staffList.add(qa);
		}
		//进修医生0003
		if(param.get("pkIpPsnRefresher")!=null&&!param.get("pkIpPsnRefresher").toString().equals("")){
			PvStaff qa = new PvStaff();
			qa.setPkStaff(NHISUUID.getKeyId());
			qa.setDateBegin(dateCh);
			qa.setDtRole(IDictCodeConst.DT_EMPROLE_INTERDOCT);
			qa.setNameEmp(param.get("nameIpPsnRefresher").toString());
			qa.setPkEmp(param.get("pkIpPsnRefresher").toString());
			qa.setPkDeptNs(pk_dept_ns);
			qa.setPkDept(pk_dept);
			qa.setPkPv(pk_pv);
			qa.setTs(new Date());
			qa.setPkOrg(pk_org);
			staffList.add(qa);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvStaff.class), staffList);
	}

	/**
	 * 更新转科后的相关业务数据
	 * @param {pkPv,pkDeptNs,pkDeptNew,pkDeptNsNew,dateCh,dataStop}
	 * @param user
	 */
	public void updateDeptChangeData(Map<String,Object> param,IUser user){

		String pk_pv = CommonUtils.getString(param.get("pkPv"));
		//先取当前床位，当前就诊科室，为了后面更新用
		Map<String,Object> result = DataBaseHelper.queryForMap("select bed_no,pk_dept,pk_dept_ns,pk_org from pv_encounter where pk_pv = ? ", pk_pv);
		if(result == null) return;
		String bedno = result.get("bedNo").toString();//当前床位
		String pk_dept = result.get("pkDept").toString();//当前就诊科室
		String pk_dept_ns = CommonUtils.getString(param.get("pkDeptNs"));
		String pk_dept_new = CommonUtils.getString(param.get("pkDeptNew"));
		String pk_dept_ns_new = CommonUtils.getString(param.get("pkDeptNsNew"));
		Date dateCh = new Date();
		try {
			dateCh = DateUtils.getDefaultDateFormat().parse(CommonUtils.getString(param.get("dateCh")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//2020-06-19 转科时先判断患者知否已转科，已转科给出提示；（针对博爱转科对方科室未接收时继续转科）
		String queSql = "select count(1) from pv_adt where pk_adt_source = (select pk_adt from pv_adt where pk_pv = ? and pk_dept = ? and pk_dept_ns = ? and del_flag = '0' and eu_status='0' and date_end is not null)";
		int count = DataBaseHelper.queryForScalar(queSql,Integer.class,pk_pv,pk_dept,pk_dept_ns);
		if(count > 0){
			throw new BusException("该患者已转科成功，待对方科室接收！");
		}
		//查询原入科记录
		Map<String,Object> adtsource = DataBaseHelper.queryForMap("select pk_adt,pk_adt_source from  pv_adt  where pk_pv = ? and pk_dept = ? and pk_dept_ns = ? and date_end is null ", new Object[]{pk_pv,pk_dept,pk_dept_ns});
		String pk_adt = "";
		if(adtsource!=null){
			pk_adt = CommonUtils.getString(adtsource.get("pkAdt"));
			//pk_adt_source = CommonUtils.getString(adtsource.get("pkAdtSource"));
		}
		param.put("pkAdt", pk_adt);
		param.put("flagAdmit", "0");
		param.put("pkDept", pk_dept_new);
		param.put("pkDeptNs", pk_dept_ns_new);
		param.put("state", "0");

		//插入转科数据
		PvAdt adtChk = this.saveAdtInfo(param, user);
		//2019-09-04  无法重现，添加校验 中二BUG ：19289 【转科】转科时pv_adt表生成的新记录pk_adt_source有时是空的，取消时没有及时删除
		if(adtChk.getPkAdtSource()==null || "".equals(adtChk.getPkAdtSource())){
            throw new BusException("转科失败！\n未获取到源科室信息。");
        }

		Map<String,Object> paramMap = new HashMap<String,Object>();
		User u = (User)user;
		String pk_emp = u.getPkEmp();
		String name_emp = u.getNameEmp();
		paramMap.put("pkPv", pk_pv);
		paramMap.put("pkEmp", pk_emp);
		paramMap.put("nameEmp", name_emp);
		paramMap.put("dateCh", dateCh);
		paramMap.put("pkDept", pk_dept);
		paramMap.put("pkDeptNs", pk_dept_ns);
		StringBuilder adt_update = new StringBuilder(" update pv_adt set pk_emp_end = :pkEmp ,name_emp_end=:nameEmp,date_end = :dateCh");
		if ("1".equals(CommonUtils.getString(param.get("flagNone")))) {
			adt_update.append(",flag_none = '1'");
		}
		if(!CommonUtils.isEmptyString(pk_adt)){
			paramMap.put("pkAdt", pk_adt);
			adt_update.append(" where pk_adt = :pkAdt");
		}else{
			paramMap.put("pkPv", pk_pv);
			adt_update.append(" where pk_pv = :pkPv  and pk_dept = :pkDept and pk_dept_ns = :pkDeptNs");
		}

		int cntAdt = DataBaseHelper.update(adt_update.toString(), paramMap);
		System.out.println(result.get("bedNo") + " 转科时，处理" +cntAdt+ "条ADT记录！");
		
		//更新就诊医疗小组
		int cntPvWg = DataBaseHelper.update("update  pv_clinic_group set date_end = :dateCh "
				+ " where pk_pv = :pkPv and pk_dept = :pkDept and pk_dept_ns = :pkDeptNs and date_end is null", paramMap);
		System.out.println(result.get("bedNo") + " 转科时，处理" +cntPvWg+ "条 PV_WG记录！");

		//更新就诊医护人员
		int cntPvStf = DataBaseHelper.update("update pv_staff set date_end = :dateCh "
				+ " where pk_pv = :pkPv and pk_dept = :pkDept and pk_dept_ns = :pkDeptNs and date_end is null",paramMap);
		System.out.println(result.get("bedNo") + " 转科时，处理" +cntPvStf+ "条 PV_STAFF记录！");

		paramMap.put("bedno", bedno);
		//更新就诊记录--不清空床位信息2019.4.24杨雪更改
		//StringBuilder pv_update = new StringBuilder("update pv_encounter set ");
		//pv_update.append(" pk_dept = '").append(pk_dept_new).append("',pk_dept_ns = '").append(pk_dept_ns_new).append("'  where pk_pv = ? ");
		//DataBaseHelper.update(pv_update.toString(), new Object[]{pk_pv});
		
		//转科是否留在原床位  0-不保留 1-保留
		String stayBeforeBed = ApplicationUtils.getSysparam("PV0024",false);
		if(!"1".equals(stayBeforeBed)){
			//更新就诊床位记录
			int cntPvBed = DataBaseHelper.update("update  pv_bed  set date_end = :dateCh,pk_emp_end=:pkEmp,name_emp_end=:nameEmp "
					+ " where pk_pv = :pkPv and pk_dept = :pkDept and pk_dept_ns = :pkDeptNs  and bedno = :bedno and date_end is null", paramMap);
			System.out.println(result.get("bedNo") + " 转科时，处理" +cntPvBed+ "条 PV_BED记录！");
			
			//更新床位信息(针对博爱医院注释掉)--针对中二还原
			//2020-02-25 手动分床模式不删除床位
			String BD0013 = ApplicationUtils.getSysparam("BD0013",false);
			int cntBed = 0;
	    	if("0".equals(BD0013)){
	    		cntBed = cntBed = DataBaseHelper.update("update bd_res_bed set pk_pi = null,flag_ocupy='0',pk_dept_used = null"
						+ " ,eu_status = '01' where code = ?  and pk_org = ? and pk_ward = ?"
				, new Object[]{bedno,u.getPkOrg(),paramMap.get("pkDeptNs")});
	    		System.out.println(result.get("bedNo") + " 转科时，处理" +cntBed+ "条 BD_RES_BED记录！");
	    	}else{
	    		cntBed = DataBaseHelper.update("update bd_res_bed set pk_pi = null,flag_ocupy='0',pk_dept_used = null"
						+ " ,flag_active = (case when '09' = dt_bedtype or '06' = dt_bedtype then '0' else flag_active end)"
						+ " ,del_flag = (case when '09' = dt_bedtype then '1' else del_flag end)"
						+ " ,eu_status = '01' where code = ?  and pk_org = ? and pk_ward = ?"
				, new Object[]{bedno,u.getPkOrg(),paramMap.get("pkDeptNs")});
	    		System.out.println(result.get("bedNo") + " 转科时，处理" +cntBed+ "条 BD_RES_BED记录！");
	    	}
		}

		//若存在未停止的医嘱，则更新开立病区及执行病区
		List<Map<String,Object>> ordlist = (List<Map<String,Object>>)param.get("dataStop");
		if(ordlist!=null&&ordlist.size()>0){
			for(Map<String,Object> ord:ordlist){
				ord.put("pkDeptNs", pk_dept_ns);
				ord.put("pkDeptNsNew", pk_dept_ns_new);
				String sql = "";
				//执行科室为当前科室的，更新为新的病区
				if(CommonUtils.getString(ord.get("pkDeptExec")).equals(pk_dept_ns)){
					sql = "update cn_order set pk_dept_ns = :pkDeptNsNew,pk_dept_exec=:pkDeptNsNew where pk_cnord = :pkCnord and pk_dept_exec = :pkDeptNs";
				}else{//执行科室不是当前科室的，只更新开立病区
					sql = "update cn_order set pk_dept_ns = :pkDeptNsNew where pk_cnord = :pkCnord ";
				}
				DataBaseHelper.update(sql, ord);
			}
		}
	}

	/**
	 * 入科时，给患者添加信用担保记录
	 * @param param
	 * @param user
	 */
	public void setPatiCredit(Map<String,Object> param ,IUser user){
		String pk_pv = CommonUtils.getString(param.get("pkPv"));
		String pk_dept = CommonUtils.getString(param.get("pkDept"));
		StringBuilder sql = new StringBuilder("select fac.amt_cred,fac.pk_hp from bd_hp_factor fac ");
		sql.append(" inner join pv_encounter pv on pv.pk_dept = fac.pk_dept ");
		sql.append(" where fac.del_flag = '0' and fac.eu_pvtype='3' ");
		sql.append(" and fac.pk_dept= ? and pv.pk_pv = ? and ( fac.pk_hp is null or fac.pk_hp = pv.pk_insu) order by fac.pk_hp desc");
		List<Map<String, Object>> map = DataBaseHelper.queryForList(sql.toString(), new Object[]{pk_dept,pk_pv});
		if(map == null || map.size() < 1) return;

		Map<String, Object> hpFactorMap = null;
		for (Map<String, Object> facMap : map) {
			if(facMap.get("pkInsu") != null && !CommonUtils.isEmptyString(facMap.get("pkInsu").toString())){
				hpFactorMap = facMap;
				break;
			}else{
				hpFactorMap = facMap;
			}
		}

		BigDecimal amt_cred = (BigDecimal)hpFactorMap.get("amtCred");
		String pk_pi = CommonUtils.getString(param.get("pkPi"));
		PvIpAcc acc = new PvIpAcc();
		acc.setAmtCredit(amt_cred);
		acc.setPkPi(pk_pi);
		acc.setPkPv(pk_pv);
		acc.setFlagCanc("0");
		acc.setDateWar(new Date());
		acc.setPkEmpWar(((User) user).getPkEmp());
		acc.setNameEmpWar(((User) user).getNameEmp());
		acc.setCreator(((User) user).getNameEmp());
		acc.setNote("入科接收时，系统控费");
		DataBaseHelper.insertBean(acc);
	}

	/**
	 * 更新取消转科后的相关业务数据
	 * @param {pkPv,pkDeptNs,pkDeptNew,pkDeptNsNew,dateCh,dataStop}
	 * @param user
	 */
	public void updateDeptChangeRtnData(Map<String,Object> param,IUser user){
		User u = (User)user;
		param.put("modifier", u.getPkEmp());
		param.put("ts", new Date());
		
		//2020-03-13 手动分床模式查询分床记录
    	String BD0013 = ApplicationUtils.getSysparam("BD0013",false);
    	
		//相关表只能恢复最后一条记录
		//更新就诊记录表pv_encounter:还原原科室、病区、床号
		DataBaseHelper.update("update pv_encounter set "
//				+ " pk_dept=:pkDeptOld,pk_dept_ns =:pkDeptNsOld,"
				+ " bed_no =:" +( (null == param.get("bedNoNew") || CommonUtils.isEmptyString(param.get("bedNoNew").toString())) ? "bedNo" : "bedNoNew")
				+ " , modifier =:modifier, ts =:ts where pk_pv =:pkPv", param);

		//更新医护人员表pv_staff: 置空date_end
		DataBaseHelper.update("update pv_staff set date_end = null, modifier =:modifier, ts =:ts "
				+ "where pk_pv =:pkPv and date_end = (select max(date_end) from pv_staff where pk_pv =:pkPv)", param);

		//更新医疗组pv_clinic_group: 置空date_end
		DataBaseHelper.update("update pv_clinic_group set date_end = null, modifier =:modifier, ts =:ts "
				+ "where pk_pv =:pkPv and date_end = (select max(date_end) from pv_clinic_group where pk_pv =:pkPv)", param);

		//更新转科表pv_adt: 置空date_end  pk_emp_end  name_emp_end  flag_dis,删除新增的转科记录
		DataBaseHelper.update("delete from pv_adt where pk_pv =:pkPv and pk_adt_source =:pkAdtSource and flag_admit = '0' ", param);
		DataBaseHelper.update("update pv_adt set date_end = null, pk_emp_end = null, name_emp_end = null, flag_dis = '0', "
				+ "modifier =:modifier, ts =:ts where pk_pv =:pkPv and pk_adt =:pkAdtSource", param);

		//还是原来的床位，置为取消转科前状态；如果是新的床位，需要插入新的就诊床位记录
		if(null == param.get("bedNoNew") || CommonUtils.isEmptyString(param.get("bedNoNew").toString())){
			//更新就诊床位记录表pv_bed: 置空date_end pk_emp_end name_emp_end
			int update = DataBaseHelper.update("update pv_bed set date_end = null, pk_emp_end = null, name_emp_end = null, "
					+ "modifier =:modifier, ts =:ts where pk_pv =:pkPv "
					//+ "and date_end = (select max(date_end) from pv_bed where pk_pv =:pkPv)", param);
					+ "and pk_pvbed =:pkPvbed", param);
			
			//处理床位表bd_res_bed
			StringBuffer sql = new StringBuffer("update bd_res_bed set eu_status = '02', flag_ocupy = '1', pk_pi =:pkPi");
			
	    	if(!"0".equals(BD0013)){
	    		sql.append(" ,flag_active = (case when '09' = dt_bedtype then '1' else flag_active end)");
	    		sql.append(" ,del_flag = (case when '09' = dt_bedtype then '0' else del_flag end)");
	    	}
	    	sql.append(" , modifier =:modifier, ts =:ts where pk_ward =:pkDeptNsOld and code =:bedNo and (pk_pi is null or pk_pi =:pkPi)");
	    	
			int cnt = DataBaseHelper.update(sql.toString(), param);
			if(cnt < 1){
				throw new BusException("原床位【"+param.get("bedNo")+"】已经被占用了！");
			}
				
		}else{
			PvBed bed = new PvBed();
			bed.setPkPv(param.get("pkPv").toString());
			bed.setPkDept(param.get("pkDeptOld").toString());
			bed.setPkDeptNs(param.get("pkDeptNsOld").toString());
			bed.setBedno(param.get("bedNoNew").toString());
			bed.setDateBegin(new Date());
			bed.setPkBedWard(param.get("pkDeptNsOld").toString());
			bed.setEuHold("0");
			bed.setPkEmpBegin(u.getPkEmp());
			bed.setNameEmpBegin(u.getNameEmp());
			bed.setFlagMaj("1"); //主床位
			DataBaseHelper.insertBean(bed);

			if(!CommonUtils.isEmptyString(CommonUtils.getString(param.get("bedNoMa")))){
				//婴儿时，先判断当前床位是否存在，无则自动添加
				String bedNoMa = CommonUtils.getString(param.get("bedNoMa"));
				BdResBed bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where code=? "
						+ "and pk_ward =? and del_flag = '0' and flag_active = '1'", BdResBed.class, new Object[]{param.get("bedNoMa"),param.get("pkDeptNsOld")});
				if(null == bedMa)
					throw new BusException("未获取到 当前婴儿母亲的床位【"+bedNoMa+"】相关信息！");
				List<BdResBed> bedList = bdResPubService.isHaveBedAndAdd( CommonUtils.getString(param.get("pkDeptNsOld"))
						, bedMa, CommonUtils.getString(param.get("bedNoNew")), null, u);
				if(null == bedList || bedList.size() < 1)
					throw new BusException("维护婴儿床位失败，无法保存婴儿信息！");
			}

			//处理床位表bd_res_bed
			StringBuffer sql = new StringBuffer("update bd_res_bed set eu_status = '02', flag_ocupy = '1', pk_pi =:pkPi");
	    	if(!"0".equals(BD0013)){
	    		sql.append(" ,flag_active = (case when '09' = dt_bedtype then '1' else flag_active end)");
	    		sql.append(" ,del_flag = (case when '09' = dt_bedtype then '0' else del_flag end)");
	    	}
	    	sql.append(" , modifier =:modifier, ts =:ts where pk_ward =:pkDeptNsOld and code =:bedNoNew and pk_pi is null");
			int cnt = DataBaseHelper.update(sql.toString(), param);
			if(cnt < 1){
				throw new BusException("当前选中床位【"+param.get("bedNoNew")+"】已经被占用了！");
			}

		}

		//还原转科更新的长期未停止的医嘱、相关执行单
		Map<String,Object> upOrdParam = new HashMap<String,Object>();
		upOrdParam.put("pkPv", param.get("pkPv"));
		upOrdParam.put("dtDepttype", param.get("dtDepttypeOld"));
		upOrdParam.put("dtDepttypeOld", param.get("dtDepttype"));
		upOrdParam.put("pkDeptNs", param.get("pkDeptNsOld"));
		upOrdParam.put("pkDeptNsOld", param.get("pkDeptNs"));
		updateOrdByNoStop(upOrdParam);//更新转科时未处理的长期医嘱
	}

	/**
	 * 更新转科时未处理的长期医嘱
	 * 2019-05-28 针对中二转科新增逻辑
	 * @param 就诊主键 	pkPv
	 * @param 新科室类型 dtDepttype
	 * @param 原科室类型 dtDepttypeOld
	 * @param 新科室主键 pkDeptNs
	 * @param 原科室主键 pkDeptNsOld
	 */
	private void updateOrdByNoStop(Map<String, Object> param) {
		//透析转科不作任何处理
		if("0311".equals(param.get("dtDepttype")) || "0311".equals(param.get("dtDepttypeOld"))) return;
		String pkDept = param.containsKey("pkDept") ? param.get("pkDept") != null ? param.get("pkDept").toString() :"" : "";
		String pkDeptNs = param.containsKey("pkDeptNs") ? param.get("pkDeptNs") != null ? param.get("pkDeptNs").toString() :"" : "";
		String flagNoStop = param.containsKey("flagNoStop") ? param.get("flagNoStop") != null ? param.get("flagNoStop").toString() :"" : "";
		//非透析，若存在未停止的医嘱，则更新开立病区及执行病区
		List<CnOrder> ordlist = new ArrayList<>();
		if(flagNoStop != null && "0".equals(flagNoStop)){
            ordlist = DataBaseHelper.queryForList(" select * from cn_order where pk_pv =:pkPv and eu_always = '0' and flag_stop = '0' " +
                      " and eu_status_ord < '4' and del_flag = '0' and pk_dept_ns =:pkDeptNsOld ", CnOrder.class,param);
        }else{//转科未接收时旧科室停止医嘱后，新科室可进行停嘱核对操作（博爱需求）
            ordlist = DataBaseHelper.queryForList(" select * from cn_order where pk_pv =:pkPv and eu_always = '0' and del_flag = '0' " +
                    " and ((eu_status_ord < '4' and eu_status_ord > '1' and flag_stop = '0') " +
					"		or (eu_status_ord < '4' and eu_status_ord > '1' and flag_stop = '1' and flag_stop_chk = '0')) " +
                    " and pk_dept_ns =:pkDeptNsOld", CnOrder.class,param);
        }

		if(ordlist != null && ordlist.size() > 0){
			String sql = "";
			for(CnOrder ord : ordlist){
				if(CommonUtils.getString(ord.getPkDeptExec()).equals(ord.getPkDeptNs())){
					//执行科室为当前病区的，更新为新的病区
					sql = "update cn_order set pk_dept_ns ='"+pkDeptNs+"',pk_dept_exec='"+pkDeptNs+"' where pk_cnord = :pkCnord and pk_dept_exec = :pkDeptExec";
				}else{
					//执行科室不是当前病区的，只更新开立病区
					sql = "update cn_order set pk_dept_ns ='"+pkDeptNs+"' where pk_cnord = :pkCnord ";
				}
				DataBaseHelper.update(sql, ord);
			}
		}

        //开立医嘱未核对时对该患者进行转科，新科室可进行核对操作（博爱需求）
        if(flagNoStop != null && "1".equals(flagNoStop)){
            ordlist = DataBaseHelper.queryForList(" select * from cn_order where pk_pv =:pkPv and eu_status_ord < '2' and del_flag = '0' " +
                    " and pk_dept_ns =:pkDeptNsOld", CnOrder.class,param);
            if(ordlist != null && ordlist.size() > 0){
                for(CnOrder ord : ordlist){
					//新开未核对的医嘱转科患者入科接收时将开立科室、开立病区修改为新的就诊科室、就诊病区
					if(CommonUtils.getString(ord.getPkDeptExec()).equals(ord.getPkDeptNs())){
						DataBaseHelper.update("update cn_order set pk_dept_ns ='"+pkDeptNs+"',pk_dept_exec='"+pkDeptNs+"',pk_dept = '"+pkDept+"' where pk_cnord = :pkCnord ", ord);
					}else{
						DataBaseHelper.update("update cn_order set pk_dept_ns ='"+pkDeptNs+"',pk_dept = '"+pkDept+"' where pk_cnord = :pkCnord ", ord);
					}

                }
            }
        }

		//非透析，未执行的执行科室为护理单元的，将执行单更新为新的护理单元
//			String dateStopChk = param.get("dateStopChk").toString();
		List<ExOrderOcc> occList = DataBaseHelper.queryForList("select * from ex_order_occ where pk_pv =:pkPv "
				+ "and eu_status = '0' and flag_canc = '0' and del_flag = '0' "
//					+ "and date_plan >= to_date('"+dateStopChk+"','YYYYMMDDHH24MISS')"// > 今天的未执行、未取消的执行单
				+ "and pk_dept_occ =:pkDeptNsOld ", ExOrderOcc.class, param);
		if(occList != null && occList.size() > 0){
			for (ExOrderOcc occ : occList) {
				occ.setPkDeptOcc(pkDeptNs);
			}
			int[] cnt = DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(ExOrderOcc.class), occList);
			if(cnt.length != occList.size())
				throw new BusException("执行单未更新成功！");
		}
	}

}
