package com.zebone.nhis.arch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.arch.BdArchDoctype;
import com.zebone.nhis.common.module.arch.BdArchSrvconf;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病例归档基础服务
 * 
 * @author gongxy
 * 
 */
@Service
public class ArchBaseBuildService {

	/**
	 * 保存接口配置
	 * 
	 * @param param
	 * @param user
	 */
	public List<BdArchSrvconf> saveBdArchSrvconf(String param, IUser user) {
		List<BdArchSrvconf> bdArchSrvconfs = JsonUtil.readValue(param,
				new TypeReference<List<BdArchSrvconf>>() {
				});
		// 分开保存
		List<BdArchSrvconf> bdArchSrvconfInsert = new ArrayList<BdArchSrvconf>();
		List<BdArchSrvconf> bdArchSrvconfUpdate = new ArrayList<BdArchSrvconf>();
		if (bdArchSrvconfs != null && bdArchSrvconfs.size() > 0) {
			for (BdArchSrvconf bdArchSrvconf : bdArchSrvconfs) {
				if (bdArchSrvconf.getPkSrvconf() != null) {
					ApplicationUtils.setDefaultValue(bdArchSrvconf, false);
					bdArchSrvconfUpdate.add(bdArchSrvconf);
				} else {
					ApplicationUtils.setDefaultValue(bdArchSrvconf, true);
					bdArchSrvconfInsert.add(bdArchSrvconf);
				}
			}
			if (bdArchSrvconfInsert.size() > 0) {
				DataBaseHelper.batchUpdate(
						DataBaseHelper.getInsertSql(BdArchSrvconf.class),
						bdArchSrvconfInsert);
			}
			if (bdArchSrvconfUpdate.size() > 0) {
				DataBaseHelper.batchUpdate(
						DataBaseHelper.getUpdateSql(BdArchSrvconf.class),
						bdArchSrvconfUpdate);
			}
		}
		return bdArchSrvconfs;
	}

	/**
	 * 用于数据库是oracle的系统,因为使用交易码ClOB类型字段无法显示
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdArchSrvconf> searchBdArchSrvconf(String param, IUser user) {

		List<BdArchSrvconf> bdArchSrvconfs = new ArrayList<BdArchSrvconf>();
		
		bdArchSrvconfs = DataBaseHelper.queryForList(
				"SELECT * FROM BD_ARCH_SRVCONF WHERE 1=1 AND PK_ORG = ?",
				BdArchSrvconf.class, UserContext.getUser().getPkOrg());

		return bdArchSrvconfs;
	}

	/**
	 * 保存文档分类设置
	 * 
	 * @param param
	 * @param user
	 */
	public BdArchDoctype saveBdArchDoctype(String param, IUser user) {

		BdArchDoctype bdArchDoctype = JsonUtil.readValue(param, BdArchDoctype.class);
		if (bdArchDoctype == null) 
			throw new BusException("未获取到待保存的文档分类设置数据！");
		
		if (!CommonUtils.isEmptyString(bdArchDoctype.getPkDoctype())) {
			Integer codeCount = DataBaseHelper.queryForScalar(
					"select count(1) from bd_arch_doctype where flag_del = '0' and pk_doctype!=? and code_doctype=? ",
						Integer.class, bdArchDoctype.getPkDoctype(), bdArchDoctype.getCodeDoctype());
			Integer nameCount = DataBaseHelper.queryForScalar(
					"select count(1) from bd_arch_doctype where flag_del = '0' and pk_doctype!=? and name_doctype=? ",
						Integer.class, bdArchDoctype.getPkDoctype(), bdArchDoctype.getNameDoctype());
			Integer noCount = DataBaseHelper.queryForScalar(
					"select count(1) from bd_arch_doctype where flag_del = '0' and pk_doctype!=? and sortno=? ",
						Integer.class, bdArchDoctype.getPkDoctype(), bdArchDoctype.getSortno());
			
			if (nameCount > 0) {
				throw new BusException("名字重复,请重新录入！");
			} else if (codeCount > 0) {
				throw new BusException("编码重复,请重新录入！");
			} else if (noCount > 0) {
				throw new BusException("序号重复,请重新录入！");
			}
			ApplicationUtils.setDefaultValue(bdArchDoctype, false);
			DataBaseHelper.update( DataBaseHelper.getUpdateSql(BdArchDoctype.class), bdArchDoctype);
		} else {
			Integer codeCount = DataBaseHelper.queryForScalar(
				"select count(1) from bd_arch_doctype where flag_del = '0' and code_doctype=? ", 
					Integer.class, bdArchDoctype.getCodeDoctype());
			Integer nameCount = DataBaseHelper.queryForScalar(
				"select count(1) from bd_arch_doctype where flag_del = '0' and name_doctype=? ", 
					Integer.class, bdArchDoctype.getNameDoctype());
			Integer noCount = DataBaseHelper.queryForScalar(
				"select count(1) from bd_arch_doctype where flag_del = '0' and sortno=? ", 
					Integer.class, bdArchDoctype.getSortno());
			
			if (nameCount > 0) {
				throw new BusException("名字重复,请重新录入！");
			} else if (codeCount > 0) {
				throw new BusException("编码重复,请重新录入！");
			} else if (noCount > 0) {
				throw new BusException("序号重复,请重新录入！");
			}
			ApplicationUtils.setDefaultValue(bdArchDoctype, true);
			DataBaseHelper.insertBean(bdArchDoctype);
		}

		return bdArchDoctype;
	}

	/**
	 * 删除文档分类设置
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void delBdArchDoctype(String param, IUser user) {

		Map<String, String> params = JsonUtil.readValue(param, Map.class);
		String pkDoctype = params.get("pkDoctype");
		if (!CommonUtils.isEmptyString(pkDoctype)) {
			Integer count = DataBaseHelper.queryForScalar(
					"select count(1) from bd_arch_prtcate_doctype where flag_del = '0' and pk_doctype = ? ", 
					Integer.class, pkDoctype);
			if (count > 0)
				throw new BusException("该文档分类已使用，不可删除！");
			
			DataBaseHelper.execute( "delete from bd_arch_doctype  where pk_doctype= ?", pkDoctype);
		}
	}
	
}
