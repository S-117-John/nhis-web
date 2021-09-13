package com.zebone.nhis.base.price.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.base.price.dao.BdHpDiagDivMapper;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdHpDiagdiv;
import com.zebone.nhis.common.module.base.bd.price.BdHpDiagdivItemcate;
import com.zebone.nhis.common.module.base.bd.srv.BdItemcate;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class BdHpDiagDivService {

	@Resource
	private BdHpDiagDivMapper bdHpDiagDivMapper;

	/**
	 * 根据诊断和患者信息查询是否有符合的单病种规则（初始显示全部，默认详细显示第一个）
	 * 
	 * @param param
	 *            前台传递的{pk_hp：医保计划 ；
	 * @param user
	 *            当前用户
	 * @return
	 */
	public List<Map<String, Object>> queryBdHpDiagdiv(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> outList = null;
		if (paramMap.get("pkHp") == null || "".equals(paramMap.get("pkHp"))) {
			return null;
		} else {
			outList = bdHpDiagDivMapper.queryBdHpDiagDiv(paramMap);// 获取单病种
		}
		return outList;
	}

	/**
	 * 根据医保计划和疾病编码查出单病种限价定义和单病种费用控制
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getBdHpDiagdiv(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>();
		if (paramMap.get("pkHp") == null || "".equals(paramMap.get("pkHp"))) {
			throw new BusException("医保计划未获得！");
		} else if (paramMap.get("pkDiag") == null
				|| "".equals(paramMap.get("pkDiag"))) {
			return null;
		} else {
			outList = bdHpDiagDivMapper.getBdHpDiagdiv(paramMap);
		}
		return outList;
	}
	
	
	/**
	 * 查询医保计划下的单病种列表（新）
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdHpDiagdiv> queryHpDiagDivList(String param, IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		//BdHp hp = JsonUtil.readValue(param, BdHp.class);
		List<BdHpDiagdiv>  diagList = this.bdHpDiagDivMapper.queryHpDiagDivList(paramMap.get("pkHp").toString());
		return diagList;
		
	}
	
	
	/**
	 * 查询单病种下的  费用分类列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdHpDiagdivItemcate> queryHpDiagdivItemcate(String param, IUser user){

		BdHpDiagdiv diag = JsonUtil.readValue(param, BdHpDiagdiv.class);
		List<BdHpDiagdivItemcate>  diagItemCateList = this.bdHpDiagDivMapper.queryHpDiagdivItemcate(diag.getPkTotaldiv());
		return diagItemCateList;
		
	}
	
	
	

	/**
	 * 保存/跟新单病种定义
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public BdHpDiagdiv saveHpDiagdiv(String param, IUser user) {
		BdHpDiagdiv bdHpDiagdiv = JsonUtil.readValue(param, BdHpDiagdiv.class);
		if (bdHpDiagdiv == null) {
			return null;
		}
		List<BdHpDiagdivItemcate> hpDiagdivItemcates = bdHpDiagdiv
				.getDiagitemcates();
		if (bdHpDiagdiv.getPkTotaldiv() == null
				|| "".equals(bdHpDiagdiv.getPkTotaldiv())) {
			// 主键不存在进行新增
			DataBaseHelper.insertBean(bdHpDiagdiv);
		} else {// 主键存在进行修改
			DataBaseHelper.updateBeanByPk(bdHpDiagdiv, false);
		}
		if (hpDiagdivItemcates != null && hpDiagdivItemcates.size() > 0) {
			User newUser = (User) user;
			for (BdHpDiagdivItemcate itemcate : hpDiagdivItemcates) {
				itemcate.setPkOrg(newUser.getPkOrg());
				itemcate.setPkTotaldiv(bdHpDiagdiv.getPkTotaldiv());
				if (itemcate.getPkDiagitemcate() == null
						|| "".equals(itemcate.getPkDiagitemcate())) {
					DataBaseHelper.insertBean(itemcate);
				} else {
					DataBaseHelper.updateBeanByPk(itemcate, false);
				}
			}
		}
		return bdHpDiagdiv;
	}

	/**
	 * 保存单病种费用信息
	 * 
	 * @param param
	 * @param user
	 */
	public BdHpDiagdivItemcate saveHpDiagIteamcate(String param, IUser user) {
		BdHpDiagdivItemcate bdHpDiagdivItemcate = JsonUtil.readValue(param,
				BdHpDiagdivItemcate.class);
		if (bdHpDiagdivItemcate == null) {
			return null;
		}
		DataBaseHelper.insertBean(bdHpDiagdivItemcate);
		return bdHpDiagdivItemcate;
	}

	/**
	 * 根据单病种费用主键删除
	 * 
	 * @param param
	 * @param user
	 */
	public void deleteHpDiagIteamcate(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap.get("pkDiagitemcate") != null
				&& !"".equals(paramMap.get("pkDiagitemcate"))) {
			bdHpDiagDivMapper.deleteDiagItemcate(paramMap);
		}
	}

	/**
	 * 根据单病种定义主键删除{删除前判断费用控制，是否存在相应数据}
	 * 
	 * @param param
	 * @param user
	 */
	public void deleteHpDiagDiv(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap.get("pkTotaldiv") != null
				&& !"".equals(paramMap.get("pkTotaldiv"))) {
			bdHpDiagDivMapper.deleteDiagItemcate(paramMap);
			bdHpDiagDivMapper.deleteHpDiagDiv(paramMap);
		}
	}
}
