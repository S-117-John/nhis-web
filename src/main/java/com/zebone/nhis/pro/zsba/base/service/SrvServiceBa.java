package com.zebone.nhis.pro.zsba.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.base.bd.srv.BdOrdExclu;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdExcluDt;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.base.dao.SrvBaMapper;
import com.zebone.nhis.pro.zsba.base.vo.BdOrdExcluVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class SrvServiceBa {

	@Autowired
	private SrvBaMapper srvBaMapper;
	
	/**
	 * 项目互斥及服务项目的保存和更新
	 * @param param
	 * @param user
	 */
	@Transactional
	public BdOrdExcluVo saveBdOrdExclu(String param, IUser user) {
		BdOrdExcluVo bdOrdExcluParam = JsonUtil.readValue(param, BdOrdExcluVo.class);
		BdOrdExclu bdOrdExclu = bdOrdExcluParam.getBdOrdExclu();
		List<BdOrdExcluDt> bdOrdExcluDtList = bdOrdExcluParam .getBdOrdExcluDtList();
		bdOrdExclu.setPkOrg("~                               ");//默认为集团级数据
		
		/*1、判断主表  编码、名称 是否重复,*/
		int count_code = DataBaseHelper.queryForScalar("select count(1) from bd_ord_exclu where code = ? ", Integer.class, bdOrdExclu.getCode());
		if (count_code > 1 || (CommonUtils.isEmptyString(bdOrdExclu.getPkExclu()) && count_code > 0)) {
			throw new BusException("机构内项目互斥编码【"+bdOrdExclu.getCode()+"】重复！");
		}
		int count_name = DataBaseHelper.queryForScalar( "select count(1) from bd_ord_exclu"
				+ " where del_flag = '0' and name = ? ", Integer.class, bdOrdExclu.getName());
		if (count_name > 1 || (CommonUtils.isEmptyString(bdOrdExclu.getPkExclu()) && count_name > 0)) {
			throw new BusException("机构内项目互斥名称【"+bdOrdExclu.getName()+"】重复！");
		}
		if (CommonUtils.isEmptyString(bdOrdExclu.getPkExclu())) {
			DataBaseHelper.insertBean(bdOrdExclu);
		} else {
			int cnt_up = DataBaseHelper.updateBeanByPk(bdOrdExclu, true);	
			if(cnt_up != 1)
				throw new BusException("机构内项目互斥【"+bdOrdExclu.getCode()+"-"+bdOrdExclu.getName()+"】保存失败！");
		}

		/** 新增或跟新项目互斥明细 **/
		if (bdOrdExcluDtList != null && bdOrdExcluDtList.size() != 0) {
			/** 校验项目互斥明细唯一性 */
			Map<String, String> nameOrdMap = new HashMap<String, String>();
			Map<String, String> codeOrdMap = new HashMap<String, String>();
			int len = bdOrdExcluDtList.size();
			for (int i = 0; i < len; i++) {
				String nameOrd = bdOrdExcluDtList.get(i).getNameOrd();
				String codeOrd = bdOrdExcluDtList.get(i).getCodeOrd();
				if (nameOrdMap.containsKey(nameOrd)) {
					throw new BusException("互斥明细名称重复！");
				}
				if (codeOrdMap.containsKey(codeOrd)) {
					throw new BusException("互斥明细编码重复！");
				}
				nameOrdMap.put(nameOrd, bdOrdExcluDtList.get(i).getPkExcludt());
				codeOrdMap.put(codeOrd, bdOrdExcluDtList.get(i).getPkExcludt());
			}

			// 先全删再恢复的方式（软删除）
			String pkexclu = bdOrdExclu.getPkExclu();
			DataBaseHelper.update("update bd_ord_exclu_dt set del_flag = '1' where pk_exclu = ?",new Object[] { pkexclu });
			for (BdOrdExcluDt excdt : bdOrdExcluDtList) {
				if (excdt.getPkExcludt() != null) {
					excdt.setDelFlag("0");// 恢复
					excdt.setPkExclu(pkexclu);
					DataBaseHelper.updateBeanByPk(excdt, false);
				} else {
					excdt.setPkExclu(pkexclu);
					DataBaseHelper.insertBean(excdt);
				}
				Map<String, String> map=new HashMap<>();
				if("0".equals(bdOrdExclu.getEuExcType())){//全排
					map.put("euExclude","1");
				}else if("1".equals(bdOrdExclu.getEuExcType())){//组排
					map.put("euExclude","2");
				}
				map.put("pkOrd", excdt.getPkOrd());
				srvBaMapper.updateOrdBypk(map);
			}
		} else {
			String pkexclu = bdOrdExclu.getPkExclu();
			DataBaseHelper.update("update bd_ord_exclu_dt set del_flag = '1' where pk_exclu = ?",new Object[] { pkexclu });
		}

		return bdOrdExcluParam;
	}

	/**
	 * 项目互斥的删除/恢复
	 * @param param
	 * @param user
	 */
	@Transactional
	public void delOrRtnBdOrdExclu(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(null == paramMap.get("pkExclu") || CommonUtils.isEmptyString(paramMap.get("pkExclu").toString()))
			throw new BusException("请选择待处理的项目互斥组！");
//		DataBaseHelper.update("update BD_ORD_EXCLU_DT set del_flag=:delFlag where PK_EXCLU =:pkExclu", paramMap);
		DataBaseHelper.update("update BD_ORD_EXCLU set del_flag=:delFlag where PK_EXCLU =:pkExclu", paramMap);
	}
}
