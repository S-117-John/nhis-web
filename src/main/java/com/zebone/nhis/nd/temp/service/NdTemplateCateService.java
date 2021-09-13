package com.zebone.nhis.nd.temp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jasig.cas.client.util.CommonUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.nd.temp.NdTemplateCate;
import com.zebone.nhis.nd.temp.dao.NdTemplateCateMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 护理记录单模板分类维护
 * @author yangxue
 *
 */
@Service
public class NdTemplateCateService {
	@Resource
	private NdTemplateCateMapper ndTemplateCateMapper;
	/**
	 * 保存护理记录单模板分类
	 * @param param{模板分类实体属性}
	 * @param user
	 */
    public void saveTemplateCate(String param,IUser user){
    	NdTemplateCate tmpCate = JsonUtil.readValue(param, NdTemplateCate.class);
    	if(tmpCate == null)
    		throw new BusException("未获取到分类信息，无法保存！");
    	if(CommonUtils.isEmpty(tmpCate.getPkTemplatecate())){//新增
    		DataBaseHelper.insertBean(tmpCate);
    	}else{//修改
    	    DataBaseHelper.updateBeanByPk(tmpCate, false);
    	}
    }
    /**
     * 查询护理记录单模板分类
     * @param param{pkOrg,pkFather,pkTemplatecate}
     * @param user
     * @return
     */
    public List<NdTemplateCate> queryTemplateCate(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	if(paramMap == null){
    		paramMap = new HashMap<String,Object>();
    		paramMap.put("pkOrg", ((User)user).getPkOrg());
    	}
    	return ndTemplateCateMapper.queryTemplateCate(paramMap);
    }
    /**
     * 删除护理记录模板分类
     * @param param{pkFather,pkTemplatecate}
     * @param user
     */
    public void deltemplateCate(String param,IUser user){
    	Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
    	if(paramMap == null)
    		throw new BusException("未获取到删除条件，无法删除！");
    	String sql = "delete from nd_template_cate where pk_org = ? ";
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(((User)user).getPkOrg());
    	if(!CommonUtils.isEmpty(paramMap.get("pkFather"))){
    		sql = sql +" and pk_father = ? ";
    		paramList.add(paramMap.get("pkFather"));
    	}
    	if(!CommonUtils.isEmpty(paramMap.get("pkTemplatecate"))){
    		sql = sql +" and pk_templatecate = ? ";
    		paramList.add(paramMap.get("pkTemplatecate"));
    	}
    	DataBaseHelper.execute(sql, paramList.toArray());
    	
    }
    
    
    
}
