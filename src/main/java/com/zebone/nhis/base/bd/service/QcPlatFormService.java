package com.zebone.nhis.base.bd.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.base.bd.dao.QcPlatFormMapper;
import com.zebone.nhis.base.bd.vo.BdQcPlatformVo;
import com.zebone.nhis.common.module.base.bd.srv.BdQcPerm;
import com.zebone.nhis.common.module.base.bd.srv.BdQcPlatform;
import com.zebone.nhis.common.module.base.bd.srv.BdQcQue;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 分诊台管理
 *
 * @author haohan
 *
 */
@Service
public class QcPlatFormService {

	@Autowired
	private QcPlatFormMapper platFormMapper;

	/**
	 * 查询分诊台信息 003003001004
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdQcPlatform> qryQcPlatFormInfo(String param, IUser user) {
		String pkOrgarea = JsonUtil.getFieldValue(param, "pkOrgarea");
		List<BdQcPlatform> bdQcPlatforms = platFormMapper.qryQcPlatFormInfo(pkOrgarea);
		return bdQcPlatforms;
	}

	/**
	 * 查询分诊台队列 003003001005
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryQcPlatFormQue(String param, IUser user) {
		String pkQcplatform = JsonUtil.getFieldValue(param, "pkQcplatform");
		List<Map<String, Object>> bdQcQues = platFormMapper.qryQcPlatFormQue(pkQcplatform);
		return bdQcQues;
	}

	/**
	 * 查询院区下的科室信息 003003001013
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOuDept> qryDeptInfo(String param, IUser user) {
		String pkOrgarea = JsonUtil.getFieldValue(param, "pkOrgarea");
		List<BdOuDept> bdOuDepts = platFormMapper.qryDeptInfo(pkOrgarea);
		return bdOuDepts;
	}

	/**
	 * 保存分诊台信息 003003001006
	 *
	 * @param param
	 * @param user
	 */
	public BdQcPlatform saveQcPlatFormInfo(String param, IUser user) {
		BdQcPlatform bdQcPlatform = JsonUtil.readValue(param, BdQcPlatform.class);

		Integer count = -1;
		if (bdQcPlatform.getPkQcplatform() != null && !"".equals(bdQcPlatform.getPkQcplatform())) {// 修改
			count = DataBaseHelper.queryForScalar("select count(1) from bd_qc_platform where pk_qcplatform <> ? and (code=? or name=?) and PK_ORGAREA=?", Integer.class, new Object[] { bdQcPlatform.getPkQcplatform(), bdQcPlatform.getCode(), bdQcPlatform.getName().trim(),bdQcPlatform.getPkOrgarea() });
			DataBaseHelper.updateBeanByPk(bdQcPlatform, false);
		} else {// 新增
			count = DataBaseHelper.queryForScalar("select count(1) from bd_qc_platform where (code=? or name=? )  and PK_ORGAREA=?", Integer.class, new Object[] { bdQcPlatform.getCode(), bdQcPlatform.getName().trim() ,bdQcPlatform.getPkOrgarea()});
			DataBaseHelper.insertBean(bdQcPlatform);
		}
		if (count > 0) {
			throw new BusException("编码或名称重复！");
		}

		return bdQcPlatform;
	}

	/**
	 * 保存分诊台队列 003003001012
	 *
	 * @param param
	 * @param user
	 */
	public List<BdQcQue> saveBdQcQue(String param, IUser user) {
		List<BdQcQue> bdQcQues = JsonUtil.readValue(param, new TypeReference<List<BdQcQue>>() {
		});
		if (bdQcQues == null || bdQcQues.size() <= 0) {
			throw new BusException("没有需要保存的队列信息！");
		}
		//校验同一科室分诊台唯一
		if (bdQcQues.size() == 1) {
			String sql;
            Integer count = 0;
			if(!StringUtils.isEmpty(bdQcQues.get(0).getPkQcque())){
                sql = "select count(1) from bd_qc_que where pk_dept=? and pk_qcplatform <> ?";
                count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{bdQcQues.get(0).getPkDept(), bdQcQues.get(0).getPkQcplatform()});
            }else {
                sql = "select count(1) from bd_qc_que where pk_dept=?";
                count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{bdQcQues.get(0).getPkDept()});
            }
			if(count>0){
				//List<BdQcPlatform> bdQcPlatform = JsonUtil.readValue(param, new TypeReference<List<BdQcPlatform>>(){});
				//sql="select name from bd_qc_platform where pk_qcplatform=?";
				sql="select a.name from bd_qc_platform a inner join bd_qc_que  b on a.pk_qcplatform =b.pk_qcplatform where b.pk_dept = ?";
				BdQcPlatform scalar = DataBaseHelper.queryForBean(sql, BdQcPlatform.class, new Object[]{bdQcQues.get(0).getPkDept()});
				throw new BusException("该科室已维护在"+scalar.getName()+"下，不可将同一科室维护多个分诊台");
			}
		}
		// 校验序号唯一
		List<Integer> sortNos = Lists.newArrayList();
		for (BdQcQue temp : bdQcQues) {
			sortNos.add(temp.getSortno());
		}
		Map<String, Object> qryParam = Maps.newHashMap();
		if (sortNos.size() > 0) {
			qryParam.put("sortNos", sortNos);
			qryParam.put("pkQcplatform", bdQcQues.get(0).getPkQcplatform());
			qryParam.put("pkQcque", bdQcQues.get(0).getPkQcque());// 如果为空则说明是新增，不为空则为修改
			Integer count = platFormMapper.isRepeatNo(qryParam);
			if (count > 0) {
				throw new BusException("序号重复，请修改序号重新保存！");
			}
		}
		// 只可批量新增，不可批量修改，修改只能单条修改，只判断第一条主键是否有值就行
		if (bdQcQues.get(0).getPkQcque() == null || "".equals(bdQcQues.get(0).getPkQcque())) {
			for (BdQcQue item : bdQcQues) {
				item.setPkQcque(NHISUUID.getKeyId());
				item.setTs(new Date());
				item.setCreator(UserContext.getUser().getPkEmp());
				item.setCreateTime(item.getTs());
				item.setPkOrg(UserContext.getUser().getPkOrg());
				item.setDtQctype("01");
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdQcQue.class), bdQcQues);
		} else {
			DataBaseHelper.updateBeanByPk(bdQcQues.get(0), false);
		}
		return bdQcQues;
	}

	/**
	 * 删除分诊台信息 003003001007
	 *
	 * @param param
	 * @param user
	 */
	public void delQcPlatFormInfo(String param, IUser user) {
		String pkQcplatform = JsonUtil.getFieldValue(param, "pkQcplatform");
		Integer count = DataBaseHelper.queryForScalar("select count(1) as count from bd_qc_que where pk_qcplatform=?", Integer.class, pkQcplatform);
		if (count != null && count > 0) {
			throw new BusException("该条数据已被引用，请先取消引用再做删除！");
		}
		DataBaseHelper.execute("delete from bd_qc_platform where pk_qcplatform=?", pkQcplatform);
	}

	/**
	 * 删除分诊台队列 003003001011
	 *
	 * @param param
	 * @param user
	 */
	public void delQcQue(String param, IUser user) {
		List<String> pkQcques = JsonUtil.readValue(param, new TypeReference<List<String>>() {
		});
		Set<String> pkQcqueSet = new HashSet<String>(pkQcques);
		Integer count = DataBaseHelper.queryForScalar("select count(1) from pv_que pvq inner join bd_qc_que que on que.pk_qcque=pvq.pk_qcque where pvq.pk_qcque in (" + CommonUtils.convertSetToSqlInPart(pkQcqueSet, "pk_qcque") + ")", Integer.class);
		if (count > 0) {
			throw new BusException("所删除的队列存在引用,无法删除，请先取消相关引用再做删除操作！");
		}

		DataBaseHelper.execute("delete from bd_qc_que where pk_qcque in (" + CommonUtils.convertSetToSqlInPart(pkQcqueSet, "pk_qcque") + ")");
	}

	/**
	 * 查询可导入队列 003003001008
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryCanImpPlatForm(String param, IUser user) {
		String pkOrgarea = JsonUtil.getFieldValue(param, "pkOrgarea");
		List<Map<String, Object>> bdOuDepts = platFormMapper.qryCanImpPlatForm(pkOrgarea);
		return bdOuDepts;
	}

	/**
	 * 查询分诊权限信息 003003001009
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryAuthPlatFormInfo(String param, IUser user) {
		String pkUser = JsonUtil.getFieldValue(param, "pkUser");
		String pkOrgarea = JsonUtil.getFieldValue(param, "pkOrgarea");
		List<Map<String, Object>> maps = platFormMapper.qryAuthPlatFormInfo(pkUser, pkOrgarea);
		return maps;
	}

	/**
	 * 保存分诊权限信息 003003001010
	 *
	 * @param param
	 * @param user
	 */
	public void saveAuthPlatFormInfo(String param, IUser user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> insertPams = JsonUtil.readValue(param, Map.class);	
		DataBaseHelper.execute("delete from bd_qc_perm where PK_QCPLATFORM in("
				+ " select pe.PK_QCPLATFORM from bd_qc_perm pe inner join bd_qc_platform pl on "
				+ "pl.pk_qcplatform= pe.pk_qcplatform where pe.pk_user=? and pl.pk_orgarea=? ) ", insertPams.get("pkUser"),insertPams.get("pkOrgarea"));
		@SuppressWarnings("unchecked")
		List<Map<String, String>> perms = (List<Map<String, String>>) insertPams.get("perms");
		List<BdQcPerm> bdQcPerms = Lists.newArrayList();
		if (perms != null && perms.size() > 0) {
			for (Map<String, String> item : perms) {
				BdQcPerm bdQcPerm = new BdQcPerm();
				bdQcPerm.setPkQcperm(NHISUUID.getKeyId());
				bdQcPerm.setDelFlag("0");
				bdQcPerm.setPkQcplatform(item.get("pkQcplatform"));
				bdQcPerm.setPkUser(insertPams.get("pkUser").toString());
				bdQcPerm.setFlagDef(item.get("flagDef"));
				bdQcPerm.setTs(new Date());
				bdQcPerm.setCreateTime(bdQcPerm.getTs());
				bdQcPerm.setCreator(UserContext.getUser().getPkEmp());
				bdQcPerm.setPkOrg(UserContext.getUser().getPkOrg());
				bdQcPerms.add(bdQcPerm);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdQcPerm.class), bdQcPerms);
		}
	}

	/**
	 * 查询机构下是否有权限的用户 003003001016
	 *
	 * @param param
	 * @param user
	 */
	public List<Map<String, Object>> qryUserByArea(String param, IUser user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> qryParam = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> bdOuUsers = platFormMapper.qryUserByArea(qryParam);
		return bdOuUsers;
	}

	/**
	 * 查询当前院区用户有权限的的诊台和科室
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdQcPlatformVo> getPlatFormAndDept(String param, IUser user){
		Map<String, Object> insertPams = JsonUtil.readValue(param, Map.class);
		if(StringUtils.isBlank(MapUtils.getString(insertPams, "orgArea"))){
			throw new BusException("未传入当前院区字段！”");
		}
		User u = (User)user;
		String sql = "select pf.*,dept.pk_dept,dept.code_dept,dept.name_dept from bd_qc_platform pf inner join bd_qc_que que on pf.pk_qcplatform=que.pk_qcplatform inner join bd_ou_dept dept on que.pk_dept=dept.pk_dept where pf.pk_orgarea=?"
				+" AND exists(SELECT 1 from BD_QC_PERM perm  where pf.PK_QCPLATFORM=perm.PK_QCPLATFORM and perm.PK_USER=?)";
		return DataBaseHelper.queryForList(sql, BdQcPlatformVo.class, new Object[]{MapUtils.getString(insertPams, "orgArea"),u.getPkUser()});
	}
}
