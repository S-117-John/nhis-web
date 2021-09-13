package com.zebone.nhis.sch.pub.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.arch.support.ArchUtil;
import com.zebone.nhis.common.module.sch.pub.*;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.sch.pub.dao.SchBaseMapper;
import com.zebone.nhis.sch.pub.vo.SchResourceVo;
import com.zebone.nhis.sch.pub.vo.SchSrvSaveParam;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.build.BuildSql;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * 基础设置
 * @author
 *
 */
@Service
public class SchBaseService {

	@Autowired
	private SchBaseMapper schBaseMapper;
	
	public List<SchTicketrules> saveSchTicketrules(String param , IUser user){
		User u = (User) user;
		List<SchTicketrules> schTicketrulesList = JsonUtil.readValue(param, new TypeReference<List<SchTicketrules>>(){});
		/**校验*/
		//1、code name 唯一性
		Map<String,String> codeMap = new HashMap<String,String>();
		Map<String,String> nameMap = new HashMap<String,String>();
		int defCount = 0;
		for (SchTicketrules schTicketrules : schTicketrulesList) {
			codeMap.put(schTicketrules.getCode(), "code");
			nameMap.put(schTicketrules.getName(), "name");
			if("1".equals(schTicketrules.getFlagDef())) 
				defCount ++;
		}
		if(codeMap.size() < schTicketrulesList.size()) throw new BusException("规则编码重复！");
		if(nameMap.size() < schTicketrulesList.size()) throw new BusException("规则名称重复！");
		//2、默认列只能有一个选中
		if(defCount > 1) throw new BusException("默认列只能有一列");
		
		/**写入表*/
		DataBaseHelper.execute("delete from SCH_TICKETRULES where pk_org = '"+u.getPkOrg() + "'");
		for (SchTicketrules schTicketrules : schTicketrulesList) {
			DataBaseHelper.insertBean(schTicketrules);
		}
		return schTicketrulesList;
	}
	
	public SchSrvSaveParam saveSchSrv(String param, IUser user){
		User u = (User) user;
		SchSrvSaveParam schSrvSaveParam = JsonUtil.readValue(param, SchSrvSaveParam.class);
		SchSrv schSrv = schSrvSaveParam.getSchSrv();
		List<SchSrvOrd> list = schSrvSaveParam.getSchSrvOrds();
		//平台发消息 state 新增：_ADD 更新：_UPDATE 删除：_DELETE	
		String state = "";
		
		String pkSchsrv = schSrv.getPkSchsrv();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pkSchsrv", pkSchsrv);
		params.put("pkOrg", u.getPkOrg());
		params.put("euSchclass", schSrv.getEuSchclass());
		int countCode = 0;
		int countName = 0;
		//检查code
		params.put("code", schSrv.getCode());
		countCode = schBaseMapper.SchSrvCheckExist(params);
		if(countCode > 0) throw new BusException("服务编码重复！");
		//检查name
		params.remove("code");
		params.put("name",schSrv.getName());
		countName = schBaseMapper.SchSrvCheckExist(params);
		if(countName > 0) throw new BusException("服务名称重复！");
		
		//校验服务与医技项目为多对一的关系 2018-11-15
//		Set<String> pkOrds = Sets.newHashSet(); // 临床服务主键
//		if(list!=null&&list.size()>0){
//			for (SchSrvOrd temp : list) {
//				if (StringUtils.isEmpty(temp.getPkSchsrvord()))
//					pkOrds.add(temp.getPkOrd());
//			}
//		}
//杨雪注释		
//		if(pkOrds != null && pkOrds.size() > 0){
//			params.put("pkOrds", pkOrds);
//			List<Map<String, Object>> repeatItem = schBaseMapper.checkRepeatPk(params); // 查找已录入的临床服务
//			if (repeatItem != null && repeatItem.size() > 0) {
//				StringBuffer msgSb = new StringBuffer();
//				for(Map<String, Object> temp : repeatItem){
//					//msgSb.append("项目【"+temp.get("ordName")+"】"+"已经维护在"+"【"+temp.get("srvName")+"】下!");
//					msgSb.append("项目【").append(temp.get("ordName")).append("】").append("已经维护在").append("【").append(temp.get("srvName")).append("】服务下!").append("\n");
//				}
//				throw new BusException(msgSb.toString());
//			}
//		}
		
		if(pkSchsrv == null){
			DataBaseHelper.insertBean(schSrv);
			pkSchsrv = schSrv.getPkSchsrv();
			state = "_ADD";//平台发消息
		}else{
			SchSrv srv = DataBaseHelper.queryForBean("select * from sch_srv where pk_schsrv=?", SchSrv.class, new Object[]{schSrv.getPkSchsrv()});
			BeanUtils.copyProperties(schSrv, srv);
			DataBaseHelper.update(BuildSql.buildUpdateSqlWithClass(SchSrv.class), srv);
			state = "_UPDATE";//平台发消息
		}
		DataBaseHelper.execute("delete from sch_srv_ord where pk_schSrv = ?", new Object[]{schSrv.getPkSchsrv()});
		if(list != null && list.size() > 0){
			for (SchSrvOrd schSrvOrd : list) {
				schSrvOrd.setPkSchsrv(pkSchsrv);
				ApplicationUtils.setDefaultValue(schSrvOrd, true);
			}
			DataBaseHelper.batchUpdate(BuildSql.buildInsertSqlWithClass(SchSrvOrd.class), list);
		}
		
		//发送消息(深圳项目)
		params.putAll((Map<String, Object>) ApplicationUtils.beanToMap(schSrv));
		params.put("state", state);
		PlatFormSendUtils.sendRegLevelMsg(params);
		
		return schSrvSaveParam;
	}
	
	public void deleteSchSrv(String param , IUser user){
		String pkSchSrv = JSON.parseObject(param).getString("pkSchSrv");
		int count1 = DataBaseHelper.queryForScalar("select count(1) from sch_sch "
				+ "where pk_schsrv = ? and DEL_FLAG = '0'",Integer.class,pkSchSrv);
		if(count1 > 0){
			throw new BusException("排班正在引用该服务，不能删除");
		}
		int count2 = DataBaseHelper.queryForScalar("select count(1) from sch_plan "
				+ "where pk_schsrv = ? and DEL_FLAG = '0'",Integer.class,pkSchSrv);
		if(count2 > 0){
			throw new BusException("排班计划正在引用该服务，不能删除");
		}
		//发送消息(深圳项目)
		String sql = "select * from sch_srv where pk_schsrv=?";
		Map<String,Object> params = DataBaseHelper.queryForMap(sql, pkSchSrv);
		params.put("state", "_DELETE");
		PlatFormSendUtils.sendRegLevelMsg(params);
		
		DataBaseHelper.execute("delete from sch_srv where pk_schSrv = ?", new Object[]{pkSchSrv});
		DataBaseHelper.execute("delete from sch_srv_ord where pk_schSrv = ?", new Object[]{pkSchSrv});
	}
	
	public void deleteSchTicketrules(String param , IUser user){
		String pkTicketrules = JSON.parseObject(param).getString("pkTicketrules");
		int count = DataBaseHelper.queryForScalar("select count(1) from sch_plan "
				+ "where pk_ticketrules = ? and DEL_FLAG = '0'",Integer.class,pkTicketrules);
		if(count > 0){
			throw new BusException("排班计划正在引用该规则，不能删除");
		}
		DataBaseHelper.execute("delete from SCH_TICKETRULES where PK_TICKETRULES =  ?", new Object[]{pkTicketrules});
	}
	
	/**
	 * 获取临床医嘱信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> searchSchSrvOrd(String param , IUser user){
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		params.put("pkOrg", ((User)user).getPkOrg());
		List<Map<String, Object>> schSrvOrdMap =  schBaseMapper.searchSchSrvOrdToMap(params);
		
		// 相关的附加描述性字段--价格
		//TODO 这里的查询有毒，之前为何这么弄呢？
		Map<String, Map<String, Object>> priceMap = DataBaseHelper
				.queryListToMap("select o.pk_ord as key_, sum(i.price) price from bd_ord_item o "
						+ "join bd_item i on i.pk_item = o.pk_item where o.del_flag = '0' group by o.pk_ord");

		for (Map<String, Object> schSrvOrd : schSrvOrdMap) {
			if(priceMap.get(schSrvOrd.get("pkOrd")) != null){
				schSrvOrd.putAll(priceMap.get(schSrvOrd.get("pkOrd")));
			}
		}
		return schSrvOrdMap;
	}
	
	/**
	 * 获取临床医嘱信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> searchSchSrvOrdSimple(String param , IUser user){
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		params.put("pkOrg", ((User)user).getPkOrg());
		List<Map<String, Object>> schSrvOrdMap =  schBaseMapper.searchSchSrvOrdToMap(params);
		return schSrvOrdMap;
	}
	/**
	 * 保存资源信息
	 * @param param
	 * @param user
	 */
	public Map<String,Object> saveSchResources(String param , IUser user){
		Map<String,Object> rsMap = new HashMap<>();
		List<SchResourceVo> reslist = JsonUtil.readValue(param, new TypeReference<List<SchResourceVo>>() {
		});
		String pkOrg = ((User) user).getPkOrg();
		if(reslist != null && reslist.size()>0){
			List<String> updateSchResSql = new ArrayList<String>();
			for(SchResourceVo vo : reslist){
				SchResource res = new SchResource();
				ApplicationUtils.copyProperties(res, vo);
				if(res.getPkSchres() == null){
					int count_code = DataBaseHelper.queryForScalar("select count(1) from sch_resource "
							+ "where pk_org = '"+pkOrg+"' and del_flag = '0' and code = ?", Integer.class, res.getCode());
					int count_name = DataBaseHelper.queryForScalar("select count(1) from sch_resource "
							+ "where pk_org = '"+pkOrg+"' and del_flag = '0' and name = ?", Integer.class, res.getName());
					if(count_code == 0 && count_name == 0){
						DataBaseHelper.insertBean(res);
						vo.setPkSchres(res.getPkSchres());
						saveResouceDts(vo,pkOrg);
					}else{
						if(count_code > 0 && count_name == 0){
							throw new BusException("资源信息编码重复！");
						}
						if(count_code == 0 && count_name > 0){
							throw new BusException("资源信息名称重复！");
						}
						if(count_code > 0 && count_name > 0){
							throw new BusException("资源信息编码和名称都重复！");
						}
					}
				}else{
					int count_code = DataBaseHelper.queryForScalar("select count(1) from sch_resource "
							+ "where pk_org = '"+pkOrg+"' and del_flag = '0' and code = ? and pk_schres != ?", Integer.class, res.getCode(),res.getPkSchres());
					int count_name = DataBaseHelper.queryForScalar("select count(1) from sch_resource "
							+ "where pk_org = '"+pkOrg+"' and del_flag = '0' and name = ? and pk_schres != ?", Integer.class, res.getName(),res.getPkSchres());
					if(count_code == 0 && count_name == 0){
						DataBaseHelper.updateBeanByPk(res,false);
						saveResouceDts(vo,pkOrg);
						
					}else{
						if(count_code > 0 && count_name == 0){
							throw new BusException("资源信息编码重复！");
						}
						if(count_code == 0 && count_name > 0){
							throw new BusException("资源信息名称重复！");
						}
						if(count_code > 0 && count_name > 0){
							throw new BusException("资源信息编码和名称都重复！");
						}
					}
				}
				//如果是科室资源，并且关联诊区不为空，同步更新人员资源的关联诊区
				if(EnumerateParameter.ZERO.equals(res.getEuRestype()) && CommonUtils.isNull(res.getPkDept()) && CommonUtils.isNotNull(res.getPkDeptArea())) {
					updateSchResSql.add("update sch_resource set pk_dept_area = '" + res.getPkDeptArea() + "' where del_flag = '0' and pk_dept = '" + res.getPkSchres() + "'");
				}
				rsMap.put(res.getCode(), res.getPkSchres());
			}
			
			if(updateSchResSql.size() > 0) {
				DataBaseHelper.batchUpdate(updateSchResSql.toArray(new String[0]));
			}
		}
		return rsMap;
	}

	/**
	 * 保存资源详细信息
	 * @param resource
	 */
	public void saveResouceDts(SchResourceVo resource,String pkOrg){
		
		List<SchResourceDt> resourceDtList = resource.getDtlist();
		if (resourceDtList!=null) {
			String pkSchres = resource.getPkSchres();
			//先删除，后插入
			DataBaseHelper.execute("delete from sch_resource_dt where pk_schres = ?", new Object[]{pkSchres});
			for (SchResourceDt schResourceDt : resourceDtList) {
				schResourceDt.setPkSchres(pkSchres);
				schResourceDt.setPkSchresdt(NHISUUID.getKeyId());
				DataBaseHelper.insertBean(schResourceDt);
			}
		}
	}
	/**
	 * 查询资源详细信息--杨雪废弃
	 * @param pkSchres
	 * @return
	 */
//	public List<SchResourceDt> getResouceDts(String param,IUser user){
//		Map<String,String> params = JsonUtil.readValue(param, Map.class);
//		String pkSchres = params.get("pkSchres");
//		List<SchResourceDt> resourcedtList = DataBaseHelper.queryForList("SELECT srdt.*,bcd.NAME_DATESLOT,bcd.CODE_DATESLOT FROM SCH_RESOURCE_DT srdt LEFT JOIN bd_code_dateslot bcd ON srdt.PK_DATESLOT = bcd.PK_DATESLOT WHERE srdt.PK_SCHRES = ? AND srdt.DEL_FLAG = '0'", SchResourceDt.class, pkSchres);
//		return resourcedtList;
//	}
	
	/**
	 * 删除资源信息
	 * @param param
	 * @param user
	 */
	public void delSchResource(String param , IUser user){
		String pkSchres = schResourceDelCheck(param);
		
		DataBaseHelper.execute("delete from sch_resource where pk_schres = ?", new Object[]{pkSchres});
		DataBaseHelper.execute("delete from sch_resource_dt where pk_schres = ?", new Object[]{pkSchres});
	}

	private String schResourceDelCheck(String param) {
		String pkSchres = JsonUtil.getFieldValue(param, "pkSchres");
		/**校验是否被 排班计划和排班 引用**/
		int count1 = DataBaseHelper.queryForScalar("select count(1) from sch_plan "
				+ "where del_flag = '0' and pk_schres = ?", Integer.class, pkSchres);
		if(count1 > 0){
			throw new BusException("【sch_plan】排班计划正在引用该资源，不能删除");
		}
		int count2 = DataBaseHelper.queryForScalar("select count(1) from sch_sch "
				+ "where pk_schres = ? and DEL_FLAG = '0'",Integer.class,pkSchres);
		if(count2 > 0){
			throw new BusException("【sch_sch】排班正在引用该资源，不能删除");
		}
		int count3 = DataBaseHelper.queryForScalar("select count(1) from sch_appt "
				+ "where pk_schres = ? and DEL_FLAG = '0'",Integer.class,pkSchres);
		if(count3 > 0){
			throw new BusException("【sch_appt】预约信息正在引用该资源，不能删除");
		}
		int count4 = DataBaseHelper.queryForScalar("select count(1) from pv_op "
				+ "where pk_res = ? and DEL_FLAG = '0'",Integer.class,pkSchres);
		if(count4 > 0){
			throw new BusException("【pv_op】门诊属性正在引用该资源，不能删除");
		}
		int count5 = DataBaseHelper.queryForScalar("select count(1) from pv_er "
				+ "where pk_res = ? and DEL_FLAG = '0'",Integer.class,pkSchres);
		if(count5 > 0){
			throw new BusException("【pv_er】急诊属性正在引用该资源，不能删除");
		}
		
		return pkSchres;
	}
	
	public void delSchResourceZs(String param, IUser user) {
		String pkSchres = schResourceDelCheck(param);
		Object objs[] = {pkSchres,pkSchres};
		DataBaseHelper.execute("delete from sch_resource_dt where pk_schres"
				+ " in(select a.pk_schres from sch_resource a where (a.pk_dept=? or a.pk_schres=?))", objs);
		DataBaseHelper.execute("delete from sch_resource where (pk_dept=? or pk_schres=?)", objs);
	}
	/**
	 * 查询资源信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SchResourceVo> querySchResourceList(String param,IUser user){
		Map<String,String> params = JsonUtil.readValue(param,Map.class);
		User u = (User) user;
		//当菜单参数=1，才根据SCH0018权限过滤
		String flagRole = params.get("flagRole");
		//读取参数【SCH0018】诊疗资源维护和诊疗排班计划是否控制权限
		String schAuthority = ApplicationUtils.getSysparam("SCH0018", false);
		if(EnumerateParameter.ONE.equals(flagRole) && CommonUtils.isNotNull(schAuthority) && !EnumerateParameter.ZERO.equals(schAuthority)) {		
			params.put("ftByUser", schAuthority);
			params.put("pkEmp", u.getPkEmp());
			params.put("pkUser", u.getPkUser());
			params.put("pkDept", u.getPkDept());
		}
		List<SchResourceVo> schResourceList = schBaseMapper.querySchResourceList(params);
		for (SchResourceVo schResource : schResourceList) {
			params.put("pkSchres", schResource.getPkSchres());
			List<SchResourceDt> resouceDts = schBaseMapper.getResouceDts(params);
			schResource.setDtlist(resouceDts);
		}
		return schResourceList;
	}
	
	/**
	 * 查询资源日期分组信息
	 * @param pkSchres
	 * @return
	 */
	public List<SchResourceDt> getGridConParams(String param,IUser user){
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		if(params == null)
			return null;
		//存在诊疗排班主键，先查询明细表
		List<SchResourceDt> resourcedtList = null;
		if(params.get("pkSchres")!=null&&!"".equals(params.get("pkSchres"))){
			resourcedtList =schBaseMapper.getResouceDts(params);
		}
		//未获取到诊疗资源明细，则根据分组编码查询分组内容
		if((resourcedtList==null||resourcedtList.size()<=0)&&params.get("dtDateslottype")!=null&&!"".equals(params.get("dtDateslottype"))){
			resourcedtList = schBaseMapper.getDateSlot(params);
		}
		return resourcedtList;
	}
	
	/**
	 * 查询资源信息-无资源日期分组信息
	 * 交易号：009001001024
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SchResourceVo> qrySchResourceList(String param,IUser user){
		Map<String,String> params = JsonUtil.readValue(param,Map.class);
		User u = (User) user;
		//当菜单参数=1，才根据SCH0018权限过滤
		String flagRole = params.get("flagRole");
		//读取参数【SCH0018】诊疗资源维护和诊疗排班计划是否控制权限
		String schAuthority = ApplicationUtils.getSysparam("SCH0018", false);
		if(EnumerateParameter.ONE.equals(flagRole) && CommonUtils.isNotNull(schAuthority) && !EnumerateParameter.ZERO.equals(schAuthority)) {		
			params.put("ftByUser", schAuthority);
			params.put("pkEmp", u.getPkEmp());
			params.put("pkUser", u.getPkUser());
			params.put("pkDept", u.getPkDept());
		}
		List<SchResourceVo> schResourceList = schBaseMapper.querySchResourceList(params);
		return schResourceList;
	}
	
	/**
	 * 只查询资源主信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SchResource> getSimpleSchResource(String param,IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>(){});
		User u = (User) user;
		String pkOrg = u.getPkOrg();
		String flagRole = CommonUtils.getPropValueStr(paramMap,"flagRole");
		List<SchResource> schResList = new ArrayList<SchResource>();
		//读取参数【SCH0018】诊疗资源维护和诊疗排班计划是否控制权限
		String schAuthority = ApplicationUtils.getSysparam("SCH0018", false);
		//当菜单参数=1，才根据SCH0018权限过滤
		if(EnumerateParameter.ONE.equals(flagRole) && CommonUtils.isNotNull(schAuthority) && !EnumerateParameter.ZERO.equals(schAuthority)) {
			paramMap.put("ftByUser", schAuthority);
			paramMap.put("pkEmp", u.getPkEmp());
			paramMap.put("pkUser", u.getPkUser());
			paramMap.put("pkDept", u.getPkDept());
			paramMap.put("pkOrg", pkOrg);
			//查询子节点
			List<SchResource> simpleSchResource = schBaseMapper.getSimpleSchResource(paramMap);
			schResList.addAll(simpleSchResource);
			Set<String> pkFathers = new HashSet<String>();
			for (SchResource schRes : simpleSchResource) {
				pkFathers.add(schRes.getPkFather());
			}
			paramMap.put("pkFathers", ArchUtil.convertSetToSqlInPart(pkFathers, "pk_schres"));
			List<String> pkFatherList = new ArrayList<String>(pkFathers);
			paramMap.put("pkFatherList", pkFatherList);
			//根据子节点查询父节点
			List<SchResource> simpleSchResourceFather = schBaseMapper.getSimpleSchResourceFather(paramMap);
			schResList.addAll(simpleSchResourceFather);
			//排序
//			schResList.stream().sorted(Comparator.comparing(SchResource::getPkFather).thenComparing(SchResource::getSortno));
		}else {
			StringBuilder sql = new StringBuilder("select * from sch_resource where del_flag='0' and EU_SCHCLASS='0' and pk_org = ? ");
			if(StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkDeptNull"))) {
				sql.append(" and pk_dept is null ");
			}
			schResList.addAll(DataBaseHelper.queryForList(sql.toString(), SchResource.class, new Object[]{pkOrg}));
		}
		
		return schResList;
	}
	
	/**
	 * 根据排班主键查询排班服务信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> getSchSrvByPkSch(String param,IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>(){});
		String pkOrg = ((User) user).getPkOrg();
		StringBuilder sql = new StringBuilder("select sch.pk_schsrv,srv.eu_srvtype,dept.code_dept,dept.PK_DEPT from SCH_SCH sch  inner join sch_srv srv on srv.pk_schsrv = sch.pk_schsrv ");
		sql.append(" inner join SCH_RESOURCE res on res.pk_schres = sch.pk_schres left join BD_OU_DEPT dept on dept.pk_dept = res.pk_dept_belong  ");
		sql.append("  where  srv.del_flag='0' and sch.pk_org = ? and sch.pk_sch = ? ");
		List<Map<String, Object>> queryForList = DataBaseHelper.queryForList(sql.toString(), new Object[]{pkOrg,MapUtils.getString(paramMap, "pkSch")});
		return queryForList != null ? queryForList.get(0) : null;
	}
}
