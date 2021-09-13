package com.zebone.nhis.nd.temp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.nd.temp.NdDyncRange;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.nd.temp.dao.NdDyncMapper;
import com.zebone.nhis.nd.temp.vo.NdDyncRangeVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 病例文书动态列服务
 * @author zhangninglin
 *
 */
@Service
public class NdDyncRangeService {
	@Resource
	private NdDyncMapper ndDyncMapper;
	
	/**
	 * 保存病例文书动态列
	 * @param param {List<NdTemplateVo>}
	 * @param user
	 */
	public void saveNdDyncRanges(String param,IUser user){
		List<NdDyncRange> bdNdRangs = JsonUtil.readValue(param, new TypeReference<List<NdDyncRange>>() {});
		if(bdNdRangs == null||bdNdRangs.size()<=0)
			throw new BusException("未获取到要保存的病例文书列信息！");
		for(NdDyncRange temprange:bdNdRangs){
			temprange.setSpcode(CommonUtils.getPycode(temprange.getName()));
        	if(CommonUtils.isEmptyString(temprange.getPkDyncrange())){//新增
        		DataBaseHelper.insertBean(temprange);
        	}else{//修改
        	    DataBaseHelper.updateBeanByPk(temprange, false);
        	}
        }
	}
	
	/**
	 * 删除病例文书动态列
	 * @param param-pkTemplate
	 * @param user
	 */
	public void delDyncranges(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null)
    		throw new BusException("未获取到删除条件，无法删除！");
		DataBaseHelper.execute("delete from nd_dync_range where pk_dyncrange = ? ", new Object[]{paramMap.get("pkDyncrange")});
	}
	/**
	 * 查询动态列
	 * @param param
	 * @param user
	 * @return
	 */
	public List<NdDyncRangeVo> queryDync(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null) paramMap = new HashMap<String,Object>();
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		List<NdDyncRangeVo> list =  ndDyncMapper.queryRange(paramMap);
		return list;
	}
}
