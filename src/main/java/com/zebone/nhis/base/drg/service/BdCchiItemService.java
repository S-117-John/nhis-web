package com.zebone.nhis.base.drg.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.drg.dao.BdCchiItemMapper;
import com.zebone.nhis.base.drg.vo.BdCchiItemVo;
import com.zebone.nhis.common.module.base.bd.drg.BdCchiItem;
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
 * CCHI项目关联维护
 * @author dell
 *
 */
@Service
public class BdCchiItemService {
	
	@Resource
	public BdCchiItemMapper bdCchiItemMapper;
	/**
	 * 保存CCHI项目关联维护
	 * @param param
	 * @param user
	 */
	public void saveCchiItem(String param, IUser user){
		BdCchiItem bdCchiItem = JsonUtil.readValue(param,BdCchiItem.class);
		if(StringUtils.isEmpty(bdCchiItem.getPkCchiitem())){
			int count=DataBaseHelper.queryForScalar("select count(*) from BD_CCHI_ITEM where PK_CCHI=? and PK_ITEM=? and FLAG_DEL='0'", Integer.class, bdCchiItem.getPkCchi(),bdCchiItem.getPkItem());
			if(count>0){
				throw new BusException("CCHI项目+收费项目必须唯一，请检查！");
			}
			ApplicationUtils.setDefaultValue(bdCchiItem, true);
			bdCchiItem.setFlagDel("0");
			bdCchiItem.setPkOrg("~                               ");
			DataBaseHelper.insertBean(bdCchiItem);
		}else{
			int count=DataBaseHelper.queryForScalar("select count(*) from BD_CCHI_ITEM where PK_CCHIITEM!=? and PK_CCHI=? and PK_ITEM=? and FLAG_DEL='0'", Integer.class, bdCchiItem.getPkCchiitem(), bdCchiItem.getPkCchi(),bdCchiItem.getPkItem());
			if(count>0){
				throw new BusException("CCHI项目+收费项目必须唯一，请检查！");
			}
			bdCchiItem.setPkOrg("~                               ");
			DataBaseHelper.updateBeanByPk(bdCchiItem);
		}
	}
	/**
	 * 删除CCHI项目关联维护
	 * @param param
	 * @param user
	 */
	public void delCchiItem(String param, IUser user) {
		BdCchiItem bdCchiItem = JsonUtil.readValue(param,BdCchiItem.class);
		if(StringUtils.isEmpty(bdCchiItem.getPkCchiitem())){
			throw new BusException("CCHI项目关联主键为空，请检查参数!");
		}
		/*int count=DataBaseHelper.queryForScalar("select count(1) from pv_diag where DEL_FLAG='0' and pk_dcdt=?;", Integer.class, bdCchiItem.getPkDcdt());
		if(count>0){
			throw new BusException("该记录被患者引用，不能删除!");
		}*/
		DataBaseHelper.update("update BD_CCHI_ITEM set FLAG_DEL = '1' where PK_CCHIITEM  in  ("+bdCchiItem.getPkCchiitem()+")");
	}
	/**
	 * 查询CCHI项目关联维护列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BdCchiItemVo queryCchiItemList(String param,IUser user){
		BdCchiItemVo qryparam = JsonUtil.readValue(param,BdCchiItemVo.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String,Object>> list=bdCchiItemMapper.queryCchiItemList(paramMap);
		Page<List<BdCchiItemVo>> page = MyBatisPage.getPage();
		BdCchiItemVo paramPage =new BdCchiItemVo();
		paramPage.setCchiItemList(list);
		paramPage.setTotalCount(page.getTotalCount());
		return paramPage;
	}
}
