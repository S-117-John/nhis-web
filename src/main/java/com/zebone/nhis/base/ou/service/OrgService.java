package com.zebone.nhis.base.ou.service;

import java.util.*;

import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.ou.vo.BdOuOrgArea;
import com.zebone.nhis.common.module.base.bd.code.BdSysparam;
import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 机构服务
 * @author Xulj
 *
 */
@Service
public class OrgService {

	/**
	 * 机构的保存和更新
	 * @param param
	 * @param user
	 */
	public BdOuOrg saveOrg(String param , IUser user){
		User u = UserContext.getUser();
		
		BdOuOrg org = JsonUtil.readValue(param,BdOuOrg.class);
		
		//保存院区信息
		List<BdOuOrgArea> booa = org.getOrgAreas();
		for (BdOuOrgArea bdOuOrgArea : booa) {
			bdOuOrgArea.setPkOrg(org.getPkOrg());
			if(bdOuOrgArea.getPkOrgarea() == null){
				List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
				if(StringUtils.isBlank(bdOuOrgArea.getPkOrgarea())){
					list = DataBaseHelper.queryForList("select area.pk_orgarea,area.pk_org,area.code_area,area.name_area,area.note "      
										+"from bd_ou_org_area area where area.pk_org=? and area.del_flag='0' ",new Object[]{org.getPkOrg()});
				}
				for(Map<String,Object> m : list){
					if(bdOuOrgArea.getCodeArea().equals(m.get("codeArea"))){
						throw new BusException("您的院区编码重复!");
					}
					if(bdOuOrgArea.getNameArea().equals(m.get("nameArea"))){
						throw new BusException("您的院区名称重复!");
					}
				}
			}
			
			
		}
		if(org.getPkOrg() == null){
			int count_org = DataBaseHelper.queryForScalar("select count(1) from bd_ou_org "
					+ "where code_org=? and DEL_FLAG = '0'",Integer.class,org.getCodeOrg());
			int count_name = DataBaseHelper.queryForScalar("select count(1) from bd_ou_org "
					+ "where name_org=? and DEL_FLAG = '0'",Integer.class,org.getNameOrg());
			
			if(count_org == 0 && count_name == 0){
				if(CommonUtils.isEmptyString(org.getPkFather()))
					org.setPkOrg("~");
				DataBaseHelper.insertBean(org);
				//初始化系统参数
				String pkOrg = org.getPkOrg();
				String sql = "select bst.* from bd_sysparam_temp bst where bst.eu_range = '1' and bst.del_flag = '0' and not exists(select 1 from bd_sysparam bs where bs.pk_paramtemp = bst.pk_paramtemp and bs.pk_org = ?)";
				List<Map<String,Object>> sysparamTempList = DataBaseHelper.queryForList(sql, new Object[]{pkOrg});
				List<BdSysparam> sysparamList = new ArrayList<BdSysparam>();
				Date timeNow = new Date();
				for(Map<String,Object> map : sysparamTempList){
					BdSysparam sp = new BdSysparam();
					sp.setPkSysparam(NHISUUID.getKeyId());
					sp.setPkOrg(pkOrg);
					sp.setPkParamtemp(map.get("pkParamtemp").toString());
					sp.setCode(map.get("code").toString());
					sp.setName(map.get("name").toString());
					// 处理oracle库中null的问题
					if (null == map.get("valDef"))
					{
						sp.setVal("");
					}else{
						sp.setVal(map.get("valDef").toString());
					}
					if(map.get("descParam") != null){
						sp.setDescParam(map.get("descParam").toString());
					}
					if(map.get("note") != null){
						sp.setNote(map.get("note").toString());
					}
					sp.setCreator(u.getPkEmp());
					sp.setCreateTime(timeNow);
					sp.setModifier(u.getPkEmp());
					sp.setDelFlag("0");
					sp.setTs(timeNow);
					sysparamList.add(sp);
				}
				if(CollectionUtils.isNotEmpty(sysparamList)){
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdSysparam.class), sysparamList);
				}
			}if(count_org != 0){
				throw new BusException("机构编码重复！");
			}if(count_name != 0){
				throw new BusException("机构名称重复！");
			}
		}else{
			int count_org = DataBaseHelper.queryForScalar("select count(1) from bd_ou_org "
					+ "where code_org=? and DEL_FLAG = '0' and pk_org not like ?||'%'",Integer.class,org.getCodeOrg(),org.getPkOrg());
			if(count_org == 0){
				DataBaseHelper.updateBeanByPk(org,false);
			}else{
				throw new BusException("机构编码重复！");
			}
		}
		//保存院区信息
		for (BdOuOrgArea bdOuOrgArea : booa) {
			bdOuOrgArea.setPkOrg(org.getPkOrg());
			if(bdOuOrgArea.getPkOrgarea() == null){
				DataBaseHelper.insertBean(bdOuOrgArea);
			}
			else{
				if(!StringUtils.isBlank(bdOuOrgArea.getPkOrgarea())){
					DataBaseHelper.execute("delete from bd_ou_org_area where pk_orgarea =?", new Object[]{bdOuOrgArea.getPkOrgarea()});
					}
				DataBaseHelper.insertBean(bdOuOrgArea);
				
			}
			
		}
		//集成平台消息推送
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("bdOuOrg", org);
		BdOuOrg orgs = JsonUtil.readValue(param,BdOuOrg.class);
		if(StringUtils.isEmpty(orgs.getPkOrg())){
			paramMap.put("STATUS", "ADD");//状态
		}else{
			paramMap.put("STATUS", "UPDATE");//状态
		}
		PlatFormSendUtils.sendBdOuOrgMsg(paramMap);

		return org;
	}
	
	/**
	 * 机构的删除
	 * @param param
	 * @param user
	 */
	public void deleteOrg(String param , IUser user){
		
		BdOuOrg org = JsonUtil.readValue(param,BdOuOrg.class);

		int count1 = DataBaseHelper.queryForScalar("select count(1) from bd_ou_org "
				+ "where pk_father like ?||'%' and DEL_FLAG = '0'",Integer.class,org.getPkOrg());
		int count2 = DataBaseHelper.queryForScalar("select count(1) from bd_ou_dept "
				+ "where pk_org like ?||'%' and DEL_FLAG = '0'",Integer.class,org.getPkOrg());
		int count_org = count1 + count2;
		
		if(count_org == 0){
			DataBaseHelper.update("update bd_ou_org set DEL_FLAG = '1' where pk_org like ?||'%'", new Object[]{org.getPkOrg()});
		}else{
			throw new BusException("机构存在被引用，无法进行删除操作！");
		}

		//集成平台消息推送
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("bdOuOrg", org);
		BdOuOrg orgs = JsonUtil.readValue(param,BdOuOrg.class);
		paramMap.put("STATUS", "DELETE");//状态
		PlatFormSendUtils.sendBdOuOrgMsg(paramMap);
	}
	
	/**
	 * 查询当前所选机构下的院区信息
	 * 交易号：001001001008
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdOuOrgArea> qryOrgArea(String param , IUser user){
		BdOuOrg boo = JsonUtil.readValue(param, BdOuOrg.class);
		String pkOrg = boo.getPkOrg();
		List<BdOuOrgArea> booa = DataBaseHelper.queryForList("select area.pk_orgarea,area.pk_org,area.code_area,area.name_area,area.note "      
								+"from bd_ou_org_area area where area.pk_org=? and area.del_flag='0'", BdOuOrgArea.class, new Object[]{pkOrg});
		return booa;
	}
	
	/**
	 * 删除院区信息
	 * 交易号：001001001009
	 * @param param
	 * @param user
	 */
	public void delByPkOrgares(String param , IUser user){
		Map<Object,String> map = JsonUtil.readValue(param, Map.class);
		//通过主键删除
		String pkOrgares = map.get("pkOrgares");
		DataBaseHelper.execute("update bd_ou_org_area set Del_flag = '1' where pk_orgarea = ?", new Object[]{pkOrgares});
	}

}
