package com.zebone.nhis.pro.zsrm.sch.service;

import com.google.common.base.Function;
import com.google.common.collect.*;
import com.zebone.nhis.arch.support.ArchUtil;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.pro.zsrm.sch.dao.ZsrmSchApptMapper;
import com.zebone.nhis.pro.zsrm.sch.vo.SchApptCntVo;
import com.zebone.nhis.pro.zsrm.sch.vo.SchInfoVo;
import com.zebone.nhis.pro.zsrm.sch.vo.SchSchVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 排班预约
 * 
 * @author
 *
 */
@Service
public class ZsrmSchApptService {

	@Autowired
	private ZsrmSchApptMapper zsrmSchApptMapper;

	/**
	 * 获取排班信息
	 * 
	 * @return
	 */
	public Page<SchInfoVo> getSchInfo(String param, IUser user) {
		User u = (User) user;
		String pkOrg = u.getPkOrg();
		Map<String, Object> params = JsonUtil.readValue(param, Map.class);
		params.put("pkOrg", pkOrg);
		params.put("flagStop","0");
		params.put("euStatus","8");
		params.put("isSqlServer",String.valueOf(Application.isSqlServer()));
		
		//由于有分页，先按照下面分组条件查询分页结果》》再次依据主键查询结果集，保证分页正确
		MyBatisPage.startPage(MapUtils.getIntValue(params, "pageIndex"), MapUtils.getIntValue(params, "pageSize"));
		
		//传递过来的是诊区,按诊区查询（只有菜单参数flagRole=1 SCH0018才起作用）
		String flagRole = CommonUtils.getPropValueStr(params,"flagRole");
		String schAuthority = ApplicationUtils.getSysparam("SCH0018", false);
		params.put("ftByUser", schAuthority);
		params.put("pkDeptbus",u.getPkDept());
		if(!EnumerateParameter.ONE.equals(flagRole) && !EnumerateParameter.THREE.equals(schAuthority)) {
			params.remove("pkDeptArea");
		}
		List<String> listGroup = zsrmSchApptMapper.getSchOfApptGroup(params);

		Page<SchInfoVo> page = MyBatisPage.getPage();
		List<SchInfoVo> infoList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(listGroup)) {
			Set<String> pkSchs = new HashSet<>();
			listGroup.parallelStream().map(x -> Arrays.asList(x.split(","))).collect(Collectors.toList()).forEach(v->pkSchs.addAll(v));
			listGroup.clear();
			params.clear();
			params.put("pkSchs", pkSchs);
			List<SchSchVo> list = zsrmSchApptMapper.getSchOfApptInfo(params);

			/** list分组 */
			// 根据pkSchres:schresName:pkSchsrv:schsrvName 对list进行分组
			ImmutableList<SchSchVo> digits = ImmutableList.copyOf(list);
			// 分组方法，pkSchres:schresName:pkSchsrv:schsrvName一致的为一组
			Function<SchSchVo, String> group = schSchVo -> schSchVo.getPkSchres() + ":" + schSchVo.getPkSchsrv() + ":" + schSchVo.getPkEmp();
			// 执行分组方法
			ImmutableListMultimap<String, SchSchVo> groupMap = Multimaps.index(digits, group);

			//查询日期分组
			List<BdCodeDateslot> dateslotList = DataBaseHelper.queryForList("select * from BD_CODE_DATESLOT where flag_active='0'", BdCodeDateslot.class);
			Map<String, String> pkTotype = dateslotList.parallelStream().collect(Collectors.toMap(BdCodeDateslot::getPkDateslot, BdCodeDateslot::getDtDateslottype));
			Map<String, List<BdCodeDateslot>> typeToMap = dateslotList.parallelStream().collect(Collectors.groupingBy(BdCodeDateslot::getDtDateslottype));

			//查询实时可预约号数量
			Map<String, SchApptCntVo> apptCount = getCntAppt(pkSchs);
			for (String key : groupMap.keySet()) {
				String[] arr = key.split(":");
				SchInfoVo infoVo = new SchInfoVo(arr[0], arr[1]);
				List<SchSchVo> schVos = groupMap.get(key);
				Set<String> pkDateslots = Sets.newHashSet();
				Set<String> soltTypes = new HashSet<String>();
				List<BdCodeDateslot> bdCodeDateslotList = new ArrayList<BdCodeDateslot>();
				for (SchSchVo schSchVo : schVos) {
					SchApptCntVo cntVo = apptCount.get(schSchVo.getPkSch());
					schSchVo.setFlagTicket(cntVo!=null ? "1" : "0");
					schSchVo.setCntAppt(cntVo!=null ? cntVo.getCntAppt():0);
					schSchVo.setApptType(cntVo!=null ? cntVo.getCntApptOut():0);
					schSchVo.setCntApptIn(cntVo!=null ? cntVo.getCntApptIn():0);
					String soltType = pkTotype.get(schSchVo.getPkDateslot());
					if(!soltTypes.contains(soltType)) {
						bdCodeDateslotList.addAll(soltType==null?null:typeToMap.get(soltType));
						soltTypes.add(soltType);
					}
				}
				SchSchVo schVoFirst = schVos.get(0);
				infoVo.setSchresName(schVoFirst.getSchresName());
				infoVo.setSchsrvName(schVoFirst.getSchsrvName());
				infoVo.setDoctorName(schVoFirst.getDoctorName());
				//String soltType = pkTotype.get(schVoFirst.getPkDateslot());
				//infoVo.setBdCodeDateslots(soltType==null?null:typeToMap.get(soltType));
				infoVo.setBdCodeDateslots(bdCodeDateslotList);
				infoVo.setSchschs(schVos);
				infoVo.setPkDateslots(pkDateslots);
				infoList.add(infoVo);
			}
		}
		page.setRows(infoList);
		return page;
	}

	/**
	 * 获取排班信息（表格版本）
	 * 022006007003
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SchSchVo> getSchInfoToTable(String param, IUser user) {

		User u = (User) user;
		String pkOrg = u.getPkOrg();
		Map<String, Object> params = JsonUtil.readValue(param, Map.class);
		params.put("pkOrg", pkOrg);
		params.put("pkDeptbus", u.getPkDept());

		//传递过来的是诊区,按诊区查询（只有菜单参数flagRole=1 SCH0018才起作用）
		String flagRole = CommonUtils.getPropValueStr(params, "flagRole");
		String schAuthority = ApplicationUtils.getSysparam("SCH0018", false);
		params.put("ftByUser", schAuthority);
		params.put("pkDeptbus", u.getPkDept());
		if (!EnumerateParameter.ONE.equals(flagRole) && !EnumerateParameter.THREE.equals(schAuthority)) {
			params.remove("pkDeptArea");
		}
		//新需求：5601 预约挂号直接显示诊区所有当日排班医生
		if (EnumerateParameter.ONE.equals(flagRole) && MapUtils.getString(params, "pkDept","").equals("_RootDept_")) {
			params.put("pkDept", "");
			params.put("pkDeptArea", "_RootDept_");
		}

		List<SchSchVo> schSchVos = zsrmSchApptMapper.getSchInfoData(params);

		Set<String> pkSchSet = Sets.newHashSet();
		schSchVos.stream().forEach(m -> {
			pkSchSet.add(m.getPkSch());
		});

		if (CollectionUtils.isNotEmpty(schSchVos)) {

			List<Map<String, Object>> tkListMap = zsrmSchApptMapper.getTicketInfo(pkSchSet);
			List<BdCodeDateslot> bdCodeDateslots = DataBaseHelper.queryForList(
					"select lot.* from bd_code_dateslot lot inner join sch_sch sch on sch.pk_dateslot=lot.pk_dateslot where pk_sch in (" +
							CommonUtils.convertSetToSqlInPart(pkSchSet, "pk_sch") + ")", BdCodeDateslot.class);
			for (SchSchVo sch : schSchVos) {

				tkListMap.stream().forEach(m -> {
					if (sch.getPkSch().equals(MapUtils.getString(m, "pkSch"))) {
						sch.setCntApptIn(MapUtils.getInteger(m, "cntApptIn"));
						sch.setCntApptOut(MapUtils.getInteger(m, "cntApptOut"));
						sch.setCntApptCliniced(MapUtils.getInteger(m, "cntApptCliniced"));
						sch.setCntApptLocaled(MapUtils.getInteger(m, "cntApptLocaled"));
						sch.setCntApptOuted(MapUtils.getInteger(m, "cntApptOuted"));
					}
				});
				bdCodeDateslots.stream().forEach(n -> {
					if (n.getPkDateslot().equals(sch.getPkDateslot())) {
						sch.setBdCodeDateslot(n);
					}
				});
			}
		}
		return schSchVos;
	}

	public Map<String, SchApptCntVo> getCntAppt(Set<String> pkSchs){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("date", DateUtils.getDateTime());
		paramMap.put("pkSchs", pkSchs);
		List<SchApptCntVo> apptCntVoList = zsrmSchApptMapper.getApptStatCount(paramMap);
		return apptCntVoList.stream().collect(Collectors.toMap(SchApptCntVo::getPkSch, vo -> vo));
	}

	
    /**
     * 交易号 ：009003002012
     * 查询科室树
     * @param param
     * @param user
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getDeptTree(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		User u = (User) user;
		paramMap.put("pkOrg", u.getPkOrg());		
		List<Map<String,Object>>  deptTreeList = new ArrayList<Map<String,Object>>();
		//增加菜单参数，菜单参数为0时，诊区列表默认加载所有诊区，菜单参数为1时，根据登陆人员所属诊区显示；
		String flagRole = CommonUtils.getPropValueStr(paramMap, "flagRole");
		//读取参数【SCH0018】诊疗资源维护和诊疗排班计划是否控制权限
		String schAuthority = ApplicationUtils.getSysparam("SCH0018", false);
		if(EnumerateParameter.ONE.equals(flagRole) && CommonUtils.isNotNull(schAuthority) && !EnumerateParameter.ZERO.equals(schAuthority)) {
			//paramMap.put("pkDept", u.getPkDept());	
			//deptTreeList = schApptMapper.getAreaDeptTree(paramMap);
			paramMap.put("ftByUser", schAuthority);
			paramMap.put("pkEmp", u.getPkEmp());
			paramMap.put("pkUser", u.getPkUser());
			paramMap.put("pkDept", u.getPkDept());
			paramMap.put("pkOrg", u.getPkOrg());
			//查询子节点
			List<Map<String,Object>> simpleSchResource = zsrmSchApptMapper.getSimpleSchResource(paramMap);
			deptTreeList.addAll(simpleSchResource);
			Set<String> pkFathers = new HashSet<String>();
			for (Map<String,Object> schRes : simpleSchResource) {
				pkFathers.add(schRes.get("fatherid").toString());
			}
			paramMap.put("pkFathers", ArchUtil.convertSetToSqlInPart(pkFathers, "pk_schres"));
			List<String> pkFatherList = new ArrayList<String>(pkFathers);
			paramMap.put("pkFatherList", pkFatherList);
			//根据子节点查询父节点
			List<Map<String,Object>> simpleSchResourceFather = zsrmSchApptMapper.getSimpleSchResourceFather(paramMap);
			deptTreeList.addAll(simpleSchResourceFather);
		}else {
			deptTreeList = zsrmSchApptMapper.getDeptTree(paramMap);
		}
		return deptTreeList;
	}
    
}
