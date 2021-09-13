package com.zebone.nhis.ex.nis.pi.service;

import com.zebone.nhis.base.pub.service.BdResPubService;
import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.labor.nis.PvLabor;
import com.zebone.nhis.common.module.pv.PvAdt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.pi.dao.PvAdtMapper;
import com.zebone.nhis.ex.nis.pi.dao.PvInfantMapper;
import com.zebone.nhis.ex.nis.pi.vo.PvIpVo;
import com.zebone.nhis.ex.pub.service.AdtPubService;
import com.zebone.nhis.ex.pub.service.PatiDeptInAndOutPubService;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.ex.pub.vo.PvEncounterVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.dao.SyxPlatFormSendAdtMapper;
import com.zebone.nhis.pv.pub.service.PvAdtPubService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 入科与转科服务
 *
 * @author yangxue
 *
 */
@Service
public class PvAdtService {
	private Logger logger = LoggerFactory.getLogger("com.zebone");

	@Resource
	private PvAdtMapper pvAdtMapper;

    @Resource
    private PatiDeptInAndOutPubService deptInAndOutPubService;

    @Resource
	private CancelDeptInService cancelInService;

    @Resource
    private AdtPubService adtPubService;

    @Resource
	private PvInfantMapper pvInfantMapper;

    @Resource
    private PvAdtPubService pvAdtPubService;

    @Autowired
	private SyxPlatFormSendAdtMapper syxPlatFormSendAdtMapper;

    @Resource
	private BdResPubService bdResPubService;//处理婴儿床位公共类


	/**
	 * 根据参数查询待入科（含转科）患者列表
	 *
	 * @param param
	 *            (pk_dept_ns,pkPv,pk_dept)
	 * @param user
	 * @return [{code_pv,name_pi,dt_sex,age_pv,eu_status,pk_pv,date_reg,hpname,
	 *         adtype,dept_name,code_dept,dept_ns_name},....]
	 */
	public List<Map<String,Object>> queryPatisByIn(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		return pvAdtMapper.getPatiByIn(paramMap);
	}

	/**
	 * 根据参数查询待入科（含转科,新生儿）患者列表
	 *
	 * @param param
	 *            (pk_dept_ns,pkPv)
	 * @param user
	 * @return [{code_pv,name_pi,dt_sex,age_pv,eu_status,pk_pv,date_reg,hpname,
	 *         adtype,dept_name,code_dept,dept_ns_name},....]
	 */
	public List<Map<String,Object>> queryPatisByInAndNew(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> result =  pvAdtMapper.getPatiByIn(paramMap);
		List<Map<String,Object>> inflist = pvInfantMapper.getInfantNoBedList(paramMap);
		if(inflist!=null&&inflist.size()>0) result.addAll(inflist);
		return result;
	}

	/**
	 * 保存入科接收（医护人员、固定费用）信息,含新入院、新出生与转入病人
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public void savePatisPvIn(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        //添加实现代码
		if(paramMap == null) return;
		if(paramMap.get("pkPv")==null||paramMap.get("pkPv").toString().equals("")) {
			throw new BusException("就诊主键为空，无法完成入科操作！");
		}
		//1、判断所选床位是否可用[2019-07-30]
		String codeBed = CommonUtils.getString(paramMap.get("codeIpBed"));
		String pkBed = CommonUtils.getString(paramMap.get("pkIpBed"));
		if(CommonUtils.isEmptyString(pkBed))
			throw new BusException("当前选择床位主键为空，无法完成入科操作！");
		if(CommonUtils.isEmptyString(codeBed))
			throw new BusException("当前选择床位编码为空，无法完成入科操作！");
		List<BdResBed> beds = DataBaseHelper.queryForList("select * from bd_res_bed where pk_bed = ? and code = ? "
				, BdResBed.class, new Object[]{pkBed,codeBed});
		if(null == beds || beds.size() < 1)
			throw new BusException("当前选择床位不存在或已删除，无法完成入科操作！床位主键：" + pkBed + ",编码：" + codeBed);
		else if(!"1".equals(beds.get(0).getFlagActive()))
			throw new BusException("当前选择床位未启用，无法完成入科操作！床位主键：" + pkBed + ",编码：" + codeBed);
		else if(!CommonUtils.getString(paramMap.get("pkDeptNs")).equals(beds.get(0).getPkWard()))
			throw new BusException("当前选择床位不是当前护理单元的，无法完成入科操作！床位主键：" + pkBed + ",编码：" + codeBed);

		//2、判断透析、血透【euPvmode = 41,42】仅入住透析床位【dt_bedtype = 0301】[2019-07-30]
		String euPvmode = CommonUtils.getString(paramMap.get("euPvmode"));
		if(!CommonUtils.isEmptyString(euPvmode)){
			if(("41".equals(euPvmode) ||"42".equals(euPvmode)) && !"0301".equals(beds.get(0).getDtBedtype()))
				throw new BusException("就诊模式为【透析、血透】仅能入住【透析】类型床位，无法完成入科操作！");
			else if(!"41".equals(euPvmode) && !"42".equals(euPvmode) && "0301".equals(beds.get(0).getDtBedtype()))
				throw new BusException("就诊模式不是【透析、血透】不能入住【透析】类型床位，无法完成入科操作！");
		}

		//遍历paramMap，将对应的人员信息及费用插入相应的表
		if(paramMap.get("adtType")!=null&&(paramMap.get("adtType").equals("入院")||paramMap.get("adtType").equals("新出生"))){
			List<Map<String,Object>> pvList = DataBaseHelper.queryForList("select pv.pk_pv,pv.pk_dept_ns,dept.name_dept from pv_encounter pv left join bd_ou_dept dept " +
					" on pv.pk_dept_ns = dept.pk_dept where pv.pk_pi = ? and pv.eu_status='1' and pv.eu_pvtype = '3' "
					, new Object[]{paramMap.get("pkPi")});
			if(pvList != null && pvList.size() > 0){
				throw new BusException("当前患者在'"+pvList.get(0).get("nameDept")+"'存未出院记录，不允许重复入院！");
			}
			deptInAndOutPubService.saveDeptInInfo(paramMap, user);
		}else if(paramMap.get("adtType")!=null&& (paramMap.get("adtType").equals("转科") || paramMap.get("adtType").equals("婴儿转科")) ){
			if(paramMap.get("pkAdt")==null||paramMap.get("pkAdt").toString().equals("")){
				throw new BusException("转科记录主键为空，无法完成入科操作！");
			}
            List<Map<String,Object>> infList =(List<Map<String,Object>>)paramMap.get("infList");
			String pkPvM = "";
			String pkPvInf = "";
			if(infList != null && infList.size() > 0){
				Map<String,Object> inf = infList.get(0);
				pkPvM = paramMap.get("pkPv").toString();
				pkPvInf = inf.get("pkPv").toString();
			}
			if((paramMap.get("adtType").equals("婴儿转科") && !pkPvM.equals(pkPvInf)) || paramMap.get("adtType").equals("转科")){
				updataChangeInfo(paramMap);
				deptInAndOutPubService.saveDeptOutInfo(paramMap, user);
            }
			saveDeptChgInf(user, paramMap);//2019-07-05 保存同时转科的婴儿
		}
			  
		//发消息平台
		Map<String,Object> msgMap = JsonUtil.readValue(param, Map.class);
		msgMap.put("pkPv", msgMap.get("pkPv"));
		msgMap.put("pkEmp", UserContext.getUser().getPkEmp());
		msgMap.put("nameEmp", UserContext.getUser().getNameEmp());
		msgMap.put("codeEmp", UserContext.getUser().getCodeEmp());
		PlatFormSendUtils.sendDeptInMsg(msgMap);
	}

	/**
	 * 处理转科带医嘱的逻辑
	 * @param paramMap
	 */
	private void updataChangeInfo(Map<String,Object> paramMap){
		Map<String,Object> map = new HashMap();
		map.put("pkDept",MapUtils.getString(paramMap,"pkDept"));
		map.put("pkDeptNew",MapUtils.getString(paramMap,"pkDeptOld"));
		Integer cnt = pvAdtMapper.chkNoStopOrdByChg(map);//校验是否为转科不停嘱的专业组
        if(cnt==0){return;}
		//判断是否转往的科室是icu
		int count = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept where dt_medicaltype like '21%' and pk_dept=?", Integer.class, new Object[]{MapUtils.getString(paramMap,"pkDept")});
		if(count>0){
			//如果是转入ICU科室，则记录原医疗组，原转科处理逻辑基础上，增加处理就诊记录上的医疗组逻辑
			DataBaseHelper.update("update pv_encounter set pk_wg_org=pk_wg,pk_wg=? where pk_pv = ? ", new Object[]{MapUtils.getString(paramMap,"pkIpWg"),MapUtils.getString(paramMap,"pkPv")});
		}
		DataBaseHelper.update("update cn_order set pk_dept_exec=?  Where PK_PV=? And eu_always='0' and eu_status_ord='3' and pk_dept_exec=? ", new Object[]{MapUtils.getString(paramMap,"pkDeptNs"),MapUtils.getString(paramMap,"pkPv"),MapUtils.getString(paramMap,"pkDeptNsOld")});
		DataBaseHelper.update("update ex_order_occ set pk_dept_occ=? where PK_PV=? and eu_status='0' and pk_dept_occ=? ", new Object[]{MapUtils.getString(paramMap,"pkDeptNs"),MapUtils.getString(paramMap,"pkPv"),MapUtils.getString(paramMap,"pkDeptNsOld")});

	}
	/**
	 * 接收婴儿相关信息
	 * @param user
	 * @param paramMap
	 */
	private void saveDeptChgInf(IUser user, Map<String, Object> paramMap) {
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
				  String codeFa = CommonUtils.getString(map.get("codeFa"));
				  //转科是否留在原床位 -- 在这里清空原床位
				  String stayBeforeBed = ApplicationUtils.getSysparam("PV0024",false);
				  if("1".equals(stayBeforeBed)){
					  //查询当前转入科室及病区，更新至就诊记录表
					  PvAdt adt = DataBaseHelper.queryForBean("select * from pv_adt where pk_adt = ?", PvAdt.class, map.get("pkAdt").toString());
					  //针对博爱医院，将清空原科室床位信息移至此处执行--针对中二还原为转科不占床
					  //查询原转出科室及病区，更新至就诊记录表
					  PvAdt adtSource = DataBaseHelper.queryForBean("select * from pv_adt where pk_adt = ?", PvAdt.class, adt.getPkAdtSource());
					  String pk_pi = map.get("pkPi").toString();
					  String pk_pv = map.get("pkPv").toString();
					  //查询原科室就诊床位
					  Map<String,Object> bedMap = DataBaseHelper.queryForMap("select bedno,pk_pvbed from pv_bed where pk_dept = ? and pk_dept_ns = ? and pk_pv = ? and pk_bed_ward = ?  and date_end is null ", new Object[]{adtSource.getPkDept(),adtSource.getPkDeptNs(),pk_pv,adtSource.getPkDeptNs()});
                      //删除原科室自动生成的床位
					  String bed_update = "update bd_res_bed set del_flag = '1',pk_pi = null where code = ?  and pk_ward= ?  and pk_org = ?";
					  //删除原科室自动生成的床位
					  if(bedMap != null){
						  DataBaseHelper.update(bed_update, new Object[]{bedMap.get("bedno"),adtSource.getPkDeptNs(),u.getPkOrg()});
					  }else{
						  DataBaseHelper.update(bed_update, new Object[]{bedNoMa + bedSpc + sortNo,bedMa.getPkWard(),u.getPkOrg()});
					  }
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
				  int cntBed = DataBaseHelper.update("update bd_res_bed set flag_active = '1', del_flag = '0',code_fa = ? "
						  + "where pk_bed = ? and del_flag = '1' and pk_pi is null", new Object[]{codeFa,pkBedInf});
				  if(cntBed < 1)
					  throw new BusException("当前婴儿床位已被占用，无法保存婴儿信息！");

				  //添加对应的婴儿床位费
				  if(bedList.size() > 0 ) {
					  //2020-05-26 获取婴儿床位费编码
					  String BD0010 = ApplicationUtils.getDeptSysparam("BD0010", bedMa.getPkWard());
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
				  deptInAndOutPubService.saveDeptOutInfo(paramMap, user);
			}
		  }
	}

	/**
	 * 查询待出院患者列表
	 * @param param{pk_dept_ns}
	 * @param user
	 * @return {pv_code,name_pi,dt_sex}
	 */
	public List<Map<String,Object>> queryPatisByOut(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//return pvAdtMapper.getPatiByOut(paramMap);
		return pvAdtMapper.getPatiByStayOut(paramMap);
	}

	/**
	 * 查询取消出院患者列表
	 * @param param{pkDeptNs}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPatiCancelOut(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		return pvAdtMapper.queryCancelPatiOut(paramMap);
	}

	/**
	 * 取消入科校验信息查询--查询患者费用，有效医嘱，新增病历数据
	 * @param param{pkPv,pkDeptNs}
	 * @param user
	 */
	public Map<String,Object> queryCancelDeptInData(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap.get("pkPv") == null ||"".equals(paramMap.get("pkPv").toString())){
			throw new BusException("pkPv为空，请确认是否选择了需要取消的患者！");
		}
		if(paramMap.get("pkDeptNs") == null ||"".equals(paramMap.get("pkDeptNs").toString())){
			throw new BusException("pkDeptNs为空，请确认！");
		}

		String pk_pv = paramMap.get("pkPv").toString();
		String pk_dept_ns = paramMap.get("pkDeptNs").toString();

		return cancelInService.getCancelDataInfo(pk_pv, pk_dept_ns);
	}

	/**
	 * 确认取消入科（包含转科取消转科）
	 * @param param{pkPv,pkDeptNs}
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void cancelDeptIn(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		//查询床位号（深大项目需要）
		String sql ="select p.bed_no from pv_encounter p where p.pk_pv=?";
		Map<String, Object> queryForMap = DataBaseHelper.queryForMap(sql,paramMap.get("pkPv").toString());
		
		List<Map<String, Object>> resList = null;
		if (paramMap.get("pkPv") == null
				|| "".equals(paramMap.get("pkPv").toString())) {
			throw new BusException("pkPv为空，请确认是否选择了需要取消的患者！");
		}
		if (paramMap.get("pkDeptNs") == null
				|| "".equals(paramMap.get("pkDeptNs").toString())) {
			throw new BusException("pkDeptNs为空，请确认！");
		}
        //2020-9-14，护士转取消入科进行数据效验-lb_bug[29750]
		Map<String, Object> result = adtPubService.getDeptOutVerfyData(paramMap,user);
		if (result != null) {
			if (CommonUtils.isNotNull(result.get("ordData"))
					&& ((List) result.get("ordData")).size() > 0) {
				throw new BusException("存在未停未作废医嘱，不允许取消入科！");
			}
			if (CommonUtils.isNotNull(result.get("risData"))
					&& ((List) result.get("risData")).size() > 0) {
				throw new BusException("存在未执行执行单，不允许取消入科！");
			}
			if (CommonUtils.isNotNull(result.get("apData"))
					&& ((List) result.get("apData")).size() > 0) {
				throw new BusException("存在未完成请领单，不允许取消入科！");
			}
			if (CommonUtils.isNotNull(result.get("packBedData"))
					&& ((List) result.get("packBedData")).size() > 0) {
				throw new BusException("存在包床记录，不允许取消入科！");
			}
			if (CommonUtils.isNotNull(result.get("exListData"))
					&& ((List) result.get("exListData")).size() > 0) {
				throw new BusException("存在未执行执行单，不允许取消入科！");
			}
			if (CommonUtils.isNotNull(result.get("opData"))
					&& ((List) result.get("exListData")).size() > 0) {
				throw new BusException("存在未完成的手术申请单，不允许取消入科！");
			}
			if (CommonUtils.isNotNull(result.get("cpData"))
					&& ((List) result.get("exListData")).size() > 0) {
				throw new BusException("存在未完成的在径医嘱，不允许取消入科！");
			}
		}
		try {
			paramMap.put("pkPv", paramMap.get("pkPv"));
			paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
			paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
			paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
			resList = syxPlatFormSendAdtMapper.qryPvAdtInfo(paramMap);// 更新前先查询转科信息
			cancelInService.updateDeptInData(paramMap.get("pkPv").toString(),
					paramMap.get("pkDeptNs").toString(), user);
			//发送取消入科平台消息
			if(queryForMap!=null){
				//获取取消入科前的床号
				paramMap.put("bedNum",SDMsgUtils.getPropValueStr(queryForMap, "bedNo"));
			}
			PlatFormSendUtils.sendCancelDeptInMsg(paramMap);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

    /**
     * 转科患者校验数据查询
     * @param param{pkPv}
	 * @param user
     */
	@SuppressWarnings("unchecked")
	public Map<String,Object> deptChangeVerfy(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if((null == paramMap.get("pkPv") ||CommonUtils.isEmptyString(paramMap.get("pkPv").toString()))
				&&(null == paramMap.get("pkPvMa") || CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString()))){
			throw new BusException("pkPv为空，请确认是否选择了需要转科的患者！");
		}
		if(null != paramMap.get("pkPvMa") && !CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString())){
			List<String> pkPvs = getPkpvOfMaAndInfByPkpv(paramMap);
			paramMap.put("pkPvMa", pkPvs);
		}
		return adtPubService.getDeptOutVerfyData(paramMap, user);
	}

    /**
     * 转科患者校验数据查询
     * 2018-09-06 中山二院：添加出院日不可收费项目核查
     * @param param{pkPv}
	 * @param user
     */
	@SuppressWarnings("unchecked")
	public Map<String,Object> deptChangeVerfyBySYX(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if((null == paramMap.get("pkPv") ||CommonUtils.isEmptyString(paramMap.get("pkPv").toString()))
				&&(null == paramMap.get("pkPvMa") || CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString()))){
			throw new BusException("pkPv为空，请确认是否选择了需要转科的患者！");
		}
		if(null != paramMap.get("pkPvMa") && !CommonUtils.isEmptyString(paramMap.get("pkPvMa").toString())){
			List<String> pkPvs = getPkpvOfMaAndInfByPkpv(paramMap);
			paramMap.put("pkPvMa", pkPvs);
		}
		return adtPubService.getDeptOutVerfyDataBySyx(paramMap, user);
	}

	/**
	 * 确认转科操作
	 * @param param{pkPv,pkDeptNs,pkDeptNew,pkDeptNsNew,dateCh,dataStop}
	 * @param user
	 */
	public void confirmDeptChange(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap.get("pkPv") == null ||"".equals(paramMap.get("pkPv").toString())){
			throw new BusException("pkPv为空，请确认是否选择了需要转科的患者！");
		}
		String pkPv = paramMap.get("pkPv").toString();

		//2019-06-26 急诊转科 时，添加是否录入出院诊断校验
		if(null != paramMap.get("euPvmode") && "11".equals(paramMap.get("euPvmode").toString())){
	        int cnt = DataBaseHelper.queryForScalar("select count(1) from pv_diag where pk_pv=? and dt_diagtype='0109' and del_flag='0'"
	        		, Integer.class, new Object[]{pkPv});
	        if(cnt < 1)
	        	throw new BusException("该患者没有出院诊断，请联系主管医生！！！");
		}

		//增加是否允许未停医嘱转科校验
		if(paramMap.get("dataStop")!=null&&((List<Map<String,Object>>)paramMap.get("dataStop")).size()>0){
		 	Integer flag = DataBaseHelper.queryForScalar(" select count(1) from bd_adt_config where pk_dept = '"+paramMap.get("pkDeptNew")+"' and pk_dept_rel = '"+paramMap.get("pkDeptNsNew")+"' and dt_adtconftype = '01'", Integer.class, new Object[]{});
		    if(flag<1)
		    	throw new BusException("您要转入的科室与病区，不允许长期医嘱未停转科，请先处理完医嘱后再转入！");
		}

		//转科校验
        String flagCheck = CommonUtils.getString(paramMap.get("flagCheck"));
        if ("1".equals(flagCheck)) {
            String error = this.VerfyChkData(paramMap, user);//转科校验
            if (!CommonUtils.isEmptyString(error))
                throw new BusException(error);
        }

		String pkDeptNs = CommonUtils.getString(paramMap.get("pkDeptNs"));
		deptInAndOutPubService.updateDeptChangeData(paramMap, user);

		//2019-07-04 处理婴儿转科，flagExistOrds == 1则为大科内转科，判断是否存在婴儿待转
		if(null != paramMap.get("flagExistOrds") && "1".equals(paramMap.get("flagExistOrds").toString()))
		{
			List<PvEncounter> pvs = getPvInfoOfInfByPkpvMa(pkPv,((User)user).getPkDept());
			if(null == pvs || pvs.size() < 1)return;
			for (PvEncounter pv : pvs) {
				paramMap.put("pkPv", pv.getPkPv());
				paramMap.put("pkDeptNs",pkDeptNs);
				deptInAndOutPubService.updateDeptChangeData(paramMap, user);//循环处理婴儿转科
			}
		}
		//发消息平台
		Map<String,Object> msgMap = JsonUtil.readValue(param, Map.class);
		msgMap.put("pkPv", msgMap.get("pkPv"));
		msgMap.put("pkEmp", UserContext.getUser().getPkEmp());
		msgMap.put("nameEmp", UserContext.getUser().getNameEmp());
		msgMap.put("codeEmp", UserContext.getUser().getCodeEmp());
		PlatFormSendUtils.sendDeptChangeMsg(msgMap);
	}

	 /**
     * 转科校验
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("rawtypes")
    private String VerfyChkData(Map<String,Object> paramMap,IUser user) {
    	User u = (User)user ;
		boolean flagExistOrds = "1".equals(CommonUtils.getString(paramMap.get("flagExistOrds"))) ? true : false;
    	Map<String,Object> chkMap = new  HashMap<String, Object>();
    	chkMap.put("isDeptChg", "1");
    	if(flagExistOrds){
    		chkMap.put("pkPvMa", paramMap.get("pkPv"));
    		chkMap.put("pkDeptNs", paramMap.get("pkDeptNs"));
    		List<String> pkPvs = getPkpvOfMaAndInfByPkpv(chkMap);
			chkMap.put("pkPvMa", pkPvs);
		}else{
			chkMap.put("pkPv", paramMap.get("pkPv"));
		}
        String isNoStop = paramMap.containsKey("isNoStop") ? paramMap.get("isNoStop") != null ? paramMap.get("isNoStop").toString() :"0" : "0";
        chkMap.put("isNoStop", isNoStop);
        Map<String, Object> result = new HashMap<String, Object>();
        String itemIndex = CommonUtils.getString(paramMap.get("itemIndex"));
        if(!CommonUtils.isEmptyString(itemIndex)  && "1".endsWith(itemIndex)){
        	chkMap.put("dateEnd", paramMap.get("dateCh").toString().substring(0,8)+"000000");
        	result = adtPubService.getDeptOutVerfyDataBySyx(chkMap, user);
        }else{
        	result = adtPubService.getDeptOutVerfyData(chkMap, user);
        }
        if (result != null) {
        	int count = 0;
            if (CommonUtils.isNotNull(result.get("ordData")) && ((List) result.get("ordData")).size() > 0) {
            	if(flagExistOrds){
            		List<Map<String,Object>> ordData = (List) result.get("ordData");
            		count = 0;
                	for (Map<String,Object> ord : ordData) {
                		if("0".equals(CommonUtils.getString(ord.get("euAlways")))){
                			count += 1;
                		}
    				}
            		if(((List) result.get("ordData")).size() != count){
            			 return "该患者有" + (((List) result.get("ordData")).size() - count) + "条未处理完的 【临时医嘱】，不能转科！";
            		}
            	}   
            }
            
            if (CommonUtils.isNotNull(result.get("exListData")) && ((List) result.get("exListData")).size() > 0) {
            	List<ExlistPubVo> exListData = (List) result.get("exListData");
            	count = 0;
            	for (ExlistPubVo exData : exListData) {
            		String euStatus = CommonUtils.getString(exData.getEuStatus());
            		boolean isdate = belongCalendar(exData.getDatePlan(),new Date());
            		String euAlways = CommonUtils.getString(exData.getEuAlways());
            		if("0".equals(euStatus) && (!flagExistOrds || !(flagExistOrds && isdate && "0".equals(euAlways)))){
            			count += 1;
            		}
				}
            	if(count > 0){
            		return "该患者有" + count + "条未处理完的 【执行单】，不能转科！";
            	}
            }
            if (CommonUtils.isNotNull(result.get("risData")) && ((List) result.get("risData")).size() > 0 && isNoStop.equals("0")) {
            	List<Map<String,Object>> risData = (List) result.get("risData");
            	count = 0;
            	for (Map<String,Object> ris : risData) {
            		String euStatus = CommonUtils.getString(ris.get("euStatus"));
            		String pkDeptNs = CommonUtils.getString(ris.get("pkDeptNs"));
            		String statusLab = CommonUtils.getString(ris.get("statusLab"));
            		String statusRis = CommonUtils.getString(ris.get("statusRis"));
            		String codeApply = CommonUtils.getString(ris.get("codeApply"));
            		if("0".equals(euStatus) && u.getPkDept().equals(pkDeptNs) && ("0".equals(statusLab) || "1".equals(statusLab) || "0".equals(statusRis)) || CommonUtils.isEmptyString(codeApply) ){
            			count += 1;
            		}
				}
            	if(count > 0){
            		return "该患者有" + count + "条未处理完的 【医技】，不能转科！";
            	}
            }
//            if (CommonUtils.isNotNull(result.get("apData")) && ((List) result.get("apData")).size() > 0) {
//                return "【药品请退领单】存在" + ((List) result.get("apData")).size() + "条数据，不能转科！";
//            }
            if (CommonUtils.isNotNull(result.get("opData")) && ((List) result.get("opData")).size() > 0) {
            	List<Map<String,Object>> opData = (List) result.get("opData");
            	count = 0;
            	for (Map<String,Object> op : opData) {
            		String euStatus = CommonUtils.getString(op.get("euStatus"));
            		String pkDeptNs = CommonUtils.getString(op.get("pkDeptNs"));
            		if("0".equals(euStatus) && u.getPkDept().equals(pkDeptNs)){
            			count += 1;
            		}
				}
            	if(count > 0){
            		return "该患者有" + count + "条未处理完的 【医技】，不能转科！";
            	}            	
            }
            if (CommonUtils.isNotNull(result.get("cpData")) && ((List) result.get("cpData")).size() > 0) {
                return "该患者在径，不能转科！";
            }
            /*if (CommonUtils.isNotNull(result.get("infData")) && ((List) result.get("infData")).size() > 0) {
                return "存在同病区在诊婴儿，不允许转科！";
            }*/
            if (CommonUtils.isNotNull(result.get("packBedData")) && ((List) result.get("packBedData")).size() > 0) {
                return "存在包床记录，不允许转科！";
            }
            count = 0;
            if (CommonUtils.isNotNull(result.get("hpCgChkData")) && ((List) result.get("hpCgChkData")).size() > 0) {
            	count += ((List) result.get("hpCgChkData")).size();
            }
            if (CommonUtils.isNotNull(result.get("groupCgChkData")) && ((List) result.get("groupCgChkData")).size() > 0) {
            	count += ((List) result.get("groupCgChkData")).size();
            }    
            if(count > 0 ){
            	return "费用核查页签存在" + count + "条数据未处理！！！"; 
            }
        }
        return null;
    }
    
    /**
    * 判断时间是否在时间段内 开始时间大于等于结束时间返回true
    * @param beginTime
    * @param endTime
    * @return true
    * 如果业务数据存在相等的时候，而且相等时也需要做相应的业务判断或处理时，请注意。
    */
    public static boolean belongCalendar(Date beginTime, Date endTime) {
	    Calendar begin = Calendar.getInstance();
	    begin.setTime(beginTime);
	
	    Calendar end = Calendar.getInstance();
	    end.setTime(endTime);
	    if (begin.compareTo(end) >= 0) {
	    	return true;
	    } else {
	    	return false;
	    }
    }

	/**
	 * 病区入产房
	 * @param param{pkPv,pkDeptNs,pkDeptNew,pkDeptNsNew,dateCh}
	 * @param user
	 */
	public void transToLabor(String param,IUser user){
		PvLabor pvLabor = JsonUtil.readValue(param, PvLabor.class);
		if(pvLabor == null ){
			throw new BusException("请确认是否选择了需要入产房的患者！");
		}
		pvLabor.setEuStatus("0");
		DataBaseHelper.insertBean(pvLabor);
	}

	/**
	 * 根据参数查询 待 取消转科 的患者列表
	 *
	 * @param param
	 *            (pk_dept_ns,pkPv)
	 * @param user
	 * @return [{code_pv,name_pi,dt_sex,age_pv,eu_status,pk_pv,date_reg,hpname,
	 *         adtype,dept_name,code_dept,dept_ns_name},....]
	 */
	public List<Map<String,Object>> queryPatiByDeptOut(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		return pvAdtMapper.queryCancelPatiDeptOut(paramMap);
	}

	/**
	 * 取消转科操作
	 * @param param{pkPv,pkDeptNs,pkDeptOld,pkDeptNsOld,dateCh,dataStop}
	 * @param user
	 */
	public void cancelDeptChange(String param,IUser user){
		//多人版 - 取消转科
		List<Map<String,Object>> paramList = JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>(){});
		if(null == paramList || paramList.size() < 1)
			throw new BusException("未获取到待取消转科的相关入参！");
		for (Map<String, Object> map : paramList) {
			if(map.get("pkPv") == null ||"".equals(map.get("pkPv").toString()))
				throw new BusException("pkPv为空，请确认是否选择了需要转科的患者！");

			//1、校验当前是否已经入科完成
			int cnt = DataBaseHelper.queryForScalar("select count(1) from pv_encounter pv "
					+ " inner join bd_res_bed bed on bed.code = pv.bed_no and bed.pk_pi = pv.pk_pi"
					+ "	  and bed.pk_ward = pv.pk_dept_ns and bed.del_flag = '0' "
					+ " where pk_pv = ? and pv.bed_no not in (?) ", Integer.class, new Object[]{map.get("pkPv"),map.get("bedNo")});
			if(cnt > 0)
				throw new BusException("当前患者已经被转往科室接收！");
			deptInAndOutPubService.updateDeptChangeRtnData(map, user);
		}

		//发消息平台
		Map<String,Object> msgMap = new HashMap<>();
		msgMap.put("pkPvs", paramList.stream().map(vo -> MapUtils.getString(vo,"pkPv")).collect(Collectors.toList()));
		msgMap.put("pkEmp", UserContext.getUser().getPkEmp());
		msgMap.put("nameEmp", UserContext.getUser().getNameEmp());
		msgMap.put("codeEmp", UserContext.getUser().getCodeEmp());
		PlatFormSendUtils.execute(msgMap,"sendCancelDeptChangeMsg");
//		//单人版 - 取消转科
//		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
//		if(paramMap.get("pkPv") == null ||"".equals(paramMap.get("pkPv").toString()))
//			throw new BusException("pkPv为空，请确认是否选择了需要转科的患者！");
//		
//		//1、校验当前是否已经入科完成
//		int cnt = DataBaseHelper.queryForScalar("select count(1) from pv_encounter pv "
//				+ " inner join bd_res_bed bed on bed.code = pv.bed_no and bed.pk_pi = pv.pk_pi"
//				+ "	  and bed.pk_ward = pv.pk_dept_ns and bed.del_flag = '0' "
//				+ " where pk_pv = ? ", Integer.class, new Object[]{paramMap.get("pkPv")});
//		if(cnt > 0){
//			throw new BusException("当前患者已经被转往科室接收！");
//		}
//		deptInAndOutPubService.updateDeptChangeRtnData(paramMap, user);
	}

	//查询患者是否为转科操作
	private boolean isTransfer(String pkPv, boolean isCancel) {
		Integer num = pvAdtMapper.countPvAdt(pkPv);
		if(isCancel&&num !=null && num>=1)
			return true;
		if(num !=null && num>1)
			return true;
		return false;
	}

	/**
	 * 2019-05-28 检验当前选中科室是否可以转科不停嘱
	 * @param param
	 * @param user
	 */
	public Integer chkNoStopOrdByChg(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		Integer cnt = pvAdtMapper.chkNoStopOrdByChg(paramMap);//校验是否为转科不停嘱的专业组
		return cnt;
	}

	/**
	 * 留观转住院 - 保存入科接收（医护人员、固定费用）信息,含新入院、新出生与转入病人
	 * 1、上次就诊办理出院
	 * 2、新建本次住院就诊记录
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveErPatisPvIn(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null) return;
		if(paramMap.get("pkPv") == null || CommonUtils.isEmptyString(paramMap.get("pkPv").toString())) {
			throw new BusException("就诊主键为空，无法完成入科操作！");
		}
		User u = (User)user;
		Date ts = new Date();

		//1、办理出院
		String pkPvOld = paramMap.get("pkPv").toString();
		Map<String,Object> outMap = JsonUtil.readValue(param, Map.class);
		outMap.put("dateEnd", paramMap.get("dateAdmit"));
		outMap.put("bedNo", paramMap.get("bedNoOld"));
		outMap.put("pkDeptNs", outMap.get("pkDeptNsOld"));
		outMap.put("dtOutcomes", "04");
		outMap.put("dtOuttype", "05");
		outMap.put("dtSttypeIns", "");
		pvAdtPubService.outHospital(ApplicationUtils.objectToJson(outMap), user);
		int cnt = DataBaseHelper.update("update pv_encounter set flag_settle='1',eu_status='3' where pk_pv =? "
				+ "and date_end is not null and flag_settle='0' and eu_status='2' ", new Object[]{pkPvOld});
		if(cnt < 1)
			throw new BusException("办理出院失败，无法完成【留观转住院】操作！");

		//2、办理入院
		PvEncounter newPv = pvAdtPubService.saveErReg(u, ts, pkPvOld,paramMap);
		if(null == newPv)
			throw new BusException("办理入院失败，无法完成【留观转住院】操作");

		//3、转结费用【原留观费用、预交金 全部转为当前就诊记录】
		Map<String,Object> upBlMap = new HashMap<String,Object>();
		upBlMap.put("pkPv", newPv.getPkPv());
		upBlMap.put("pkPvOld", pkPvOld);
		upBlMap.put("ts", ts);
		upBlMap.put("pkEmp", UserContext.getUser().getPkEmp());
		int cntBl = DataBaseHelper.update("update bl_ip_dt set pk_pv =:pkPv,ts =:ts,modifier=:pkEmp where pk_pv =:pkPvOld and flag_settle='0' and del_flag='0'", upBlMap);
		int cntPre = DataBaseHelper.update("update bl_deposit set pk_pv=:pkPv,ts =:ts,modifier=:pkEmp where pk_pv=:pkPvOld and flag_settle='0' and del_flag='0'", upBlMap);
		logger.info("留观转入院，结转了"+cntBl+"条费用，结转了"+cntPre+"条预交金");

		//4、处理留观的转科记录
		int adtCnt = DataBaseHelper.update("update pv_adt set eu_status = '1',flag_admit = '1' where pk_adt =:pkAdt and eu_status='0'", paramMap);
		if(adtCnt < 1)
			throw new BusException("办理入科失败，无法完成【留观转住院】的入科 操作");

		//4、保存入科
		paramMap.put("adtType", "入院");
		paramMap.put("dateBegin",DateUtils.getDateTimeStr(newPv.getDateBegin()));
		paramMap.put("pkPv", newPv.getPkPv());
		savePatisPvIn(ApplicationUtils.objectToJson(paramMap) ,user);
	}

	/**
	 * 通过母亲主键，获取同病区婴儿就诊主键
	 * @param map
	 * @return
	 */
	private List<String> getPkpvOfMaAndInfByPkpv(Map<String,Object> map){
		List<String> list = new ArrayList<String>() ;
		list.add(map.get("pkPvMa").toString());
		List<PvEncounter> listInf =DataBaseHelper.queryForList("select inf.pk_pv_infant pk_pv from pv_infant inf "
				+ "inner join pv_encounter pvi on pvi.pk_pv = inf.pk_pv_infant "
				+ "where pvi.flag_in = '1' and inf.pk_pv =:pkPvMa and pvi.pk_dept_ns =:pkDeptNs "
				, PvEncounter.class, map);
		if(null != listInf && listInf.size() > 0){
			for (PvEncounter pv : listInf) {
				list.add(pv.getPkPv());
			}
		}
		return list;
	}

	/**
	 * 通过母亲主键，获取同病区婴儿就诊记录
	 * @param pkPvMa
	 * @return
	 */
	private List<PvEncounter> getPvInfoOfInfByPkpvMa(String pkPvMa,String pkDeptNs){
		List<PvEncounter> list =DataBaseHelper.queryForList("select pvi.* from pv_infant inf "
				+ " inner join pv_encounter pvi on pvi.pk_pv = inf.pk_pv_infant  "
				+ " where pvi.flag_in = '1' and pvi.bed_no is not null and inf.pk_pv = ? and pvi.pk_dept_ns = ? "
				, PvEncounter.class, new Object[]{pkPvMa,pkDeptNs});
		return list;
	}

	/**
	 * 取消手术申请
	 * 005002001061
	 * @param param
	 * @param user
	 */
	public void cancelOp(String param, IUser user) {
        Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
        String pkCnord = paramMap.get("pkCnord");
        String pkPv = paramMap.get("pkpv");
        Double amount = DataBaseHelper.queryForScalar("select sum(amount) from bl_ip_dt where pk_cnord=?", Double.class, pkCnord);
		Double amountrl = DataBaseHelper.queryForScalar("select sum(amount) from bl_ip_dt where pk_cnord_rl=? and pk_pv=?" ,Double.class,  pkCnord,pkPv);
		if (amount != null && amount > 0) {
			throw new BusException("该手术已收费，不可取消！");
		}

		if (amountrl != null && amountrl > 0) {
			throw new BusException("该手术已收费，不可取消！");
		}

        CnOpApply cnOpApply = DataBaseHelper.queryForBean("select * from cn_op_apply where pk_cnord = ? ", CnOpApply.class, pkCnord);
        ExOrderOcc exOrderOcc = DataBaseHelper.queryForBean("select * from ex_order_occ where pk_cnord = ? ", ExOrderOcc.class, pkCnord);
//        if (cnOpApply.getDatePlan().compareTo(new Date()) > 0) {
//            throw new BusException("手术计划日期大于当前出院日期，不可取消！");
//        }
        if (exOrderOcc == null) {
            throw new BusException("该手术未核对，可直接作废！");
        }
        if ("1".equals(exOrderOcc.getFlagCanc()) && "0".equals(cnOpApply.getEuStatus())) {
            throw new BusException("该手术申请已取消！");
        }
		DataBaseHelper.execute("update cn_op_apply set eu_status='0' where pk_cnord=? and pk_ordop = ?", new Object[]{pkCnord, cnOpApply.getPkOrdop()});
		DataBaseHelper.execute("update ex_order_occ set eu_status='9', flag_canc='1',pk_dept_canc = ?,pk_emp_canc=?,name_emp_canc=?,date_canc=? where pk_cnord=? ",
				new Object[]{UserContext.getUser().getPkDept(), UserContext.getUser().getPkEmp(), UserContext.getUser().getNameEmp(), new Date(), pkCnord});
    }


	/**
	 * 婴儿单独转科时验证婴儿母亲是否在本科室（如果在不允许婴儿单独转科）
	 * @param param
	 * @param user
	 * @return
	 */
    public String checkMomIsDept(String param, IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		String isDeptFlag = "0";
		String queSql = " select pv.*" +
						" from pv_encounter pv" +
						" inner join PV_INFANT inf on inf.PK_PV_INFANT = pv.pk_pv or inf.pk_pv = pv.pk_pv" +
						" where inf.PK_PV_INFANT = ?";
		List<PvEncounterVo> pvEncounterVos = DataBaseHelper.queryForList(queSql,PvEncounterVo.class,pkPv);
		PvEncounterVo pvMom = new PvEncounterVo();
		PvEncounterVo pvChd = new PvEncounterVo();
		if(pvEncounterVos.size() > 0){
			for(PvEncounterVo pvEncounterVo : pvEncounterVos){
				if(pvEncounterVo.getPkPv().equals(pkPv)){
					pvChd = pvEncounterVo;
				}else{
					pvMom = pvEncounterVo;
				}
			}
		}
		if(pvMom != null && pvChd != null && pvMom.getPkDept() != null && pvChd.getPkDept() != null && pvMom.getPkDept().equals(pvChd.getPkDept())){
			isDeptFlag = "1";
		}
		return isDeptFlag;
	}

	/**
	 * 查询待转科患者列表
	 * @param param{pk_dept_ns}
	 * @param user
	 */
	public List<Map<String,Object>> queryWaitDeptChange(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		return pvAdtMapper.getWaitDeptChange(paramMap);
	}
}
