package com.zebone.nhis.base.drg.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.drg.dao.BdTermDcdtMapper;
import com.zebone.nhis.base.drg.vo.BdTermDcdtVo;
import com.zebone.nhis.common.module.base.bd.drg.BdTermDcdt;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 地区诊断
 * @author dell
 *
 */
@Service
public class BdTermDcdtService {
	@Resource
	public BdTermDcdtMapper bdTermDcdtMapper;
	/**
	 * 保存地区诊断
	 * @param param
	 * @param user
	 */
	public void saveTermDcdt(String param, IUser user){
		BdTermDcdt bdTermDcdt = JsonUtil.readValue(param,BdTermDcdt.class);
		if(StringUtils.isEmpty(bdTermDcdt.getPkDcdt())){
			int count=DataBaseHelper.queryForScalar("select count(*) from BD_TERM_DCDT where CODE_DCDT=? and FLAG_DEL='0'", Integer.class, bdTermDcdt.getCodeDcdt());
			if(count>0){
				throw new BusException("地区诊断编码重复，请检查！");
			}
			int countName=DataBaseHelper.queryForScalar("select count(*) from BD_TERM_DCDT where Name_DCDT=? and FLAG_DEL='0'", Integer.class, bdTermDcdt.getNameDcdt());
			if(countName>0){
				throw new BusException("地区诊断名称重复，请检查！");
			}
			ApplicationUtils.setDefaultValue(bdTermDcdt, true);
			bdTermDcdt.setFlagDel("0");
			bdTermDcdt.setPkOrg("~                               ");
			DataBaseHelper.insertBean(bdTermDcdt);
		}else{
			int count=DataBaseHelper.queryForScalar("select count(*) from BD_TERM_DCDT where PK_DCDT!=? and CODE_DCDT=? and FLAG_DEL='0'", Integer.class, bdTermDcdt.getPkDcdt(), bdTermDcdt.getCodeDcdt());
			if(count>0){
				throw new BusException("地区诊断编码重复，请检查！");
			}
			int countName=DataBaseHelper.queryForScalar("select count(*) from BD_TERM_DCDT where PK_DCDT!=? and Name_DCDT=? and FLAG_DEL='0'", Integer.class, bdTermDcdt.getPkDcdt(), bdTermDcdt.getNameDcdt());
			if(countName>0){
				throw new BusException("地区诊断名称重复，请检查！");
			}
			bdTermDcdt.setPkOrg("~                               ");
			DataBaseHelper.updateBeanByPk(bdTermDcdt);
		}
	}
	/**
	 * 删除地区诊断
	 * @param param
	 * @param user
	 */
	public void delTermDcdt(String param, IUser user) {
		BdTermDcdt bdTermDcdt = JsonUtil.readValue(param,BdTermDcdt.class);
		if(StringUtils.isEmpty(bdTermDcdt.getPkDcdt())){
			throw new BusException("地区诊断主键为空，请检查参数!");
		}
		int count=DataBaseHelper.queryForScalar("select count(1) from pv_diag where DEL_FLAG='0' and pk_dcdt in ("+bdTermDcdt.getPkDcdt()+")", Integer.class);
		if(count>0){
			throw new BusException("该记录被患者引用，不能删除!");
		}
		DataBaseHelper.update("update BD_TERM_DCDT set FLAG_DEL = '1' where  pk_dcdt in ("+bdTermDcdt.getPkDcdt()+")");
	}
	/**
	 * 查询地区诊断列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BdTermDcdtVo queryTermDcdtList(String param,IUser user){
		BdTermDcdtVo qryparam = JsonUtil.readValue(param,BdTermDcdtVo.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String,Object>> list=bdTermDcdtMapper.queryTermDcdtList(paramMap);
		Page<List<BdTermDcdtVo>> page = MyBatisPage.getPage();
		BdTermDcdtVo paramPage =new BdTermDcdtVo();
		paramPage.setTermDcdtList(list);
		paramPage.setTotalCount(page.getTotalCount());
		return paramPage;
	}
}
