package com.zebone.nhis.arch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.arch.dao.ArchPrtCateSettingMapper;
import com.zebone.nhis.arch.vo.ArchDoctypeVo;
import com.zebone.nhis.arch.vo.ArchPrtcateForSave;
import com.zebone.nhis.common.module.arch.BdArchPrtcate;
import com.zebone.nhis.common.module.arch.BdArchPrtcateDoctype;
import com.zebone.nhis.common.module.arch.PvArchPrt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 打印分类设置
 * 
 * @author Roger
 * 
 */
@Service
public class ArchPrtCateSettingService {

	@Autowired
	private ArchPrtCateSettingMapper mapper;

	/**
	 * 查询打印分类
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdArchPrtcate> qryArchPrtCate(String param, IUser user) {
		return DataBaseHelper .queryForList( " select prtcate.pk_prtcate,prtcate.code_prtcate, "
				+ "prtcate.name_prtcate,  prtcate.flag_def "
				+ " from bd_arch_prtcate prtcate "
				+ "where prtcate.pk_org=? ", BdArchPrtcate.class, UserContext.getUser().getPkOrg());
	}

	/**
	 * 查询打印分类下的文件类型
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ArchDoctypeVo> qryArchDocType(String param, IUser user) {
		Map<String, String> params = JsonUtil.readValue(param, Map.class);
		List<ArchDoctypeVo> res = new ArrayList<ArchDoctypeVo>();
		String pkPrtcate = params.get("pkPrtcate");

		if (!CommonUtils.isEmptyString(pkPrtcate)) {
			List<ArchDoctypeVo> chosen = mapper .qryArchDocType_chosen(pkPrtcate);
			if (chosen != null && chosen.size() > 0) {
				for (ArchDoctypeVo vo : chosen) {
					vo.setChoiceType(ArchDoctypeVo.CHOSEN);
				}
			}
			res.addAll(chosen);
		}

		List<ArchDoctypeVo> alter = mapper.qryArchDocType_alter(UserContext.getUser().getPkOrg());
		if (alter != null && alter.size() > 0) {
			for (ArchDoctypeVo vo : alter) {
				vo.setChoiceType(ArchDoctypeVo.ALTER);
			}
			res.addAll(alter);
		}
		return res;
	}

	/**
	 * 保存打印分类
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void savePrtCate(String param, IUser user) {
		ArchPrtcateForSave vo = JsonUtil.readValue(param, ArchPrtcateForSave.class);
		if ( null == vo || null == vo.getPrtcate())
			throw new BusException("未获取到待保存的打印分类相关数据！");
		
		//1. 保存打印分类
		String pkPrtcate = null;
		BdArchPrtcate prtcate = vo.getPrtcate();
		if (CommonUtils.isEmptyString(prtcate.getPkPrtcate())) {
			//保存前对编码名称的校验
			afterUpdateCheckDate(prtcate);
			ApplicationUtils.setDefaultValue(prtcate, true);
			DataBaseHelper.insertBean(prtcate);
		} else {
			//更新前检查编码名称是否唯一
			afterUpdateCheckDate(prtcate);
			ApplicationUtils.setDefaultValue(prtcate, false);
			DataBaseHelper.updateBeanByPk(prtcate, false);
		}
		
		pkPrtcate = prtcate.getPkPrtcate();
		if(null == pkPrtcate || CommonUtils.isEmptyString(pkPrtcate))
			throw new BusException("保存出错！");
		
		//2. 保证默认唯一性
		if ("1".equals(prtcate.getFlagDef())) {
			DataBaseHelper .execute( "update BD_ARCH_PRTCATE set flag_def = 0 where pk_prtcate <>  ? ", pkPrtcate);
		}
		
		//3. 保存打印分类明细，先删除再保存
		DataBaseHelper .execute( "delete from bd_arch_prtcate_doctype  where pk_prtcate=?", pkPrtcate);
		List<BdArchPrtcateDoctype> types = vo.getTypes();
		if (types != null && types.size() > 0) {
			for (BdArchPrtcateDoctype type : types) {
				type.setPkPrtcate(pkPrtcate);
				ApplicationUtils.setDefaultValue(type, true);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper .getInsertSql(BdArchPrtcateDoctype.class), types);
		}
	}

	/**
	 * 保存前对编码名称的校验
	 * @param prtcate
	 */
	private void afterUpdateCheckDate(BdArchPrtcate prtcate) {
		// 1.1校验当前机构下编码唯一性
		String codeSql = "select count(1) cnt from bd_arch_prtcate where pk_org =? and code_prtcate = ?";
		//更新时添加主键过滤
		codeSql += CommonUtils.isEmptyString(prtcate.getPkPrtcate()) ? "" : " and pk_prtcate != ?";
		int cnt_code = DataBaseHelper.queryForScalar(codeSql,
				Integer.class, UserContext.getUser().getPkOrg(),
				prtcate.getCodePrtcate(), prtcate.getPkPrtcate());
		if (cnt_code > 0) {
			throw new BusException("已存在该编码，请修改！");
		}
		// 1.2校验当前机构下名称的唯一性
		String nameSql = "select count(1) cnt from bd_arch_prtcate where pk_org =? and name_prtcate = ? ";
		//更新时添加主键过滤
		nameSql += CommonUtils.isEmptyString(prtcate.getPkPrtcate()) ? "" : "and pk_prtcate != ?";
		int cnt_name = DataBaseHelper.queryForScalar(nameSql,
				Integer.class, UserContext.getUser().getPkOrg(),
				prtcate.getNamePrtcate(), prtcate.getPkPrtcate());
		if (cnt_name > 0) {
			throw new BusException("已存在该名称，请修改！");
		}
	}

	/**
	 * 删除打印分类
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void delPrtCate(String param, IUser user) {

		Map<String, String> params = JsonUtil.readValue(param, Map.class);
		String pkPrtcate = params.get("pkPrtcate");
		if (!CommonUtils.isEmptyString(pkPrtcate)) {
			DataBaseHelper.execute( "delete from bd_arch_prtcate_doctype  where pk_prtcate=?", pkPrtcate);
			DataBaseHelper.execute( "delete from bd_arch_prtcate  where pk_prtcate= ? ", pkPrtcate);
		}
	}

	/**
	 * 更新打印表
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void insetPrt(String param, IUser user) {

		PvArchPrt params = JsonUtil.readValue(param, PvArchPrt.class);
		if (params != null) {
			ApplicationUtils.setDefaultValue(params, true);
			params.setPkOrg(UserContext.getUser().getPkOrg());
			DataBaseHelper.insertBean(params);
		}
	}

}
