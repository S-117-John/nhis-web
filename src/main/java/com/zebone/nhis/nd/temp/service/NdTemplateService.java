package com.zebone.nhis.nd.temp.service;

import java.util.List;

import javax.annotation.Resource;

import org.jasig.cas.client.util.CommonUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.nd.temp.NdTemplate;
import com.zebone.nhis.common.module.nd.temp.NdTemplateDept;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.nd.temp.dao.NdTemplateMapper;
import com.zebone.nhis.nd.temp.vo.NdTemplateVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 护理记录模板操作业务类
 * @author yangxue
 *
 */
@Service
public class NdTemplateService {
	@Resource
	private NdTemplateMapper ndTemplateMapper;
    /**
     * 查询护理记录模板
     * @param param-pkTemplatecate
     * @param user
     * @return
     */
	public List<NdTemplateVo> queryTemplate(String param,IUser user){
		String pkTemplatecate = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmpty(pkTemplatecate))
			throw new BusException("未获取到模板所属分类，无法进行查询!");
		List<NdTemplateVo> tempList = ndTemplateMapper.queryTemplate(pkTemplatecate);
		if(tempList == null || tempList.size()<=0)
			return null;
		for(NdTemplateVo vo:tempList){
			List<NdTemplateDept> tempDeptList = ndTemplateMapper.queryTemplateDept(vo.getPkTemplate());
			if(tempDeptList!=null)
				vo.setTempDeptList(tempDeptList);
		}
		 return tempList;
	}
	
	/**
	 * 保存护理记录模板
	 * @param param{NdTemplateVo}
	 * @param user
	 */
	public void saveTemplate(String param,IUser user){
		NdTemplateVo vo = JsonUtil.readValue(param, NdTemplateVo.class);
		if(vo == null)
			throw new BusException("未获取到要保存的模板数据！");
		NdTemplate temp = new NdTemplate();
		ApplicationUtils.copyProperties(temp, vo);
		if(CommonUtils.isEmpty(vo.getPkTemplate())){//新增
			DataBaseHelper.insertBean(temp);
			//插入使用部门
			if(vo.getTempDeptList()!=null&&vo.getTempDeptList().size()>0)
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(NdTemplateDept.class), vo.getTempDeptList());
		}else{//修改
			DataBaseHelper.updateBeanByPk(temp, false);
			if(vo.getTempDeptList()!=null&&vo.getTempDeptList().size()>0){
				DataBaseHelper.execute("delete from nd_template_dept where pk_template = ? ", new Object[]{temp.getPkTemplate()});
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(NdTemplateDept.class), vo.getTempDeptList());
			}	
		}
	}
	/**
	 * 删除模板
	 * @param param-pkTemplate
	 * @param user
	 */
	public void delTemplate(String param,IUser user){
		String pkTemplate = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmpty(pkTemplate))
			throw new BusException("未获取到要删除的模板主键！");
		DataBaseHelper.execute("delete from nd_template_dept where pk_template = ? ", new Object[]{pkTemplate});
		DataBaseHelper.execute("delete from nd_template where pk_template = ? ", new Object[]{pkTemplate});
	}
	
	
}
