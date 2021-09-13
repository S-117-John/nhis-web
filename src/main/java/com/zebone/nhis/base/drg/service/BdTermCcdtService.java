package com.zebone.nhis.base.drg.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.base.drg.dao.BdTermCcdtMapper;
import com.zebone.nhis.base.drg.vo.BdTermCcdtVo;
import com.zebone.nhis.common.module.base.bd.drg.BdTermCcdt;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pv.pub.vo.PageQryPvParam;
import com.zebone.nhis.pv.pub.vo.PvEncounterListVo;
import com.zebone.nhis.sch.pub.vo.SchPlanAndEmpsWeeksParam;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CCDT
 * @author ds
 *
 */
@Service
public class BdTermCcdtService {
	@Resource
	private BdTermCcdtMapper bdTermCcdtMapper;
	/**
	 * 保存ccdt字典
	 * @param param
	 * @param user
	 */
	public void saveTremCcdt(String param, IUser user){
		BdTermCcdt bdTermCcdt = JsonUtil.readValue(param,BdTermCcdt.class);
		if(StringUtils.isEmpty(bdTermCcdt.getPkCcdt())){
			int count=DataBaseHelper.queryForScalar("select count(*) from BD_TERM_CCDT where CODE_CCDT=? and FLAG_DEL='0'", Integer.class, bdTermCcdt.getCodeCcdt());
			if(count>0){
				throw new BusException("CCDT编码重复，请检查！");
			}
			int countName=DataBaseHelper.queryForScalar("select count(*) from BD_TERM_CCDT where name_CCDT=? and FLAG_DEL='0'", Integer.class, bdTermCcdt.getNameCcdt());
			if(countName>0){
				throw new BusException("CCDT名称重复，请检查！");
			}
			ApplicationUtils.setDefaultValue(bdTermCcdt, true);
			bdTermCcdt.setFlagDel("0");
			bdTermCcdt.setPkOrg("~                               ");
			DataBaseHelper.insertBean(bdTermCcdt);
		}else{
			int count=DataBaseHelper.queryForScalar("select count(*) from BD_TERM_CCDT where PK_CCDT!=? and CODE_CCDT=? and FLAG_DEL='0'", Integer.class, bdTermCcdt.getPkCcdt(), bdTermCcdt.getCodeCcdt());
			if(count>0){
				throw new BusException("CCDT编码重复，请检查！");
			}
			int countName=DataBaseHelper.queryForScalar("select count(*) from BD_TERM_CCDT where PK_CCDT!=? and name_CCDT=? and FLAG_DEL='0'", Integer.class, bdTermCcdt.getPkCcdt(), bdTermCcdt.getNameCcdt());
			if(countName>0){
				throw new BusException("CCDT名称重复，请检查！");
			}
			bdTermCcdt.setPkOrg("~                               ");
			DataBaseHelper.updateBeanByPk(bdTermCcdt);
		}
	}
	/**
	 * 删除ccdt字典
	 * @param param
	 * @param user
	 */
	public void delTremCcdt(String param, IUser user) {
		BdTermCcdt bdTermCcdt = JsonUtil.readValue(param,BdTermCcdt.class);
		if(StringUtils.isEmpty(bdTermCcdt.getPkCcdt())){
			throw new BusException("CCDT主键为空，请检查参数!");
		}
		int count=DataBaseHelper.queryForScalar("select count(1) from pv_diag where DEL_FLAG='0' and pk_ccdt in ("+bdTermCcdt.getPkCcdt()+")", Integer.class);
		if(count>0){
			throw new BusException("该记录被患者引用，不能删除!");
		}
		int count1=DataBaseHelper.queryForScalar("select count(1) from bd_term_dcdt where pk_ccdt in ("+bdTermCcdt.getPkCcdt()+")", Integer.class);
		if(count1>0){
			throw new BusException("该记录被地区引用，不能删除!");
		}
		DataBaseHelper.update("update BD_TERM_CCDT set FLAG_DEL = '1' where PK_CCDT in ("+bdTermCcdt.getPkCcdt()+")");
	}
	/**
	 * 查询ccdt列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BdTermCcdtVo queryTremCcdtList(String param,IUser user){
		BdTermCcdtVo qryparam = JsonUtil.readValue(param,BdTermCcdtVo.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String,Object>> list=bdTermCcdtMapper.queryTremCcdtList(paramMap);
		Page<List<BdTermCcdtVo>> page = MyBatisPage.getPage();
		BdTermCcdtVo paramPage =new BdTermCcdtVo();
		paramPage.setTermCcdtList(list);
		paramPage.setTotalCount(page.getTotalCount());
		return paramPage;
	}
	
}
