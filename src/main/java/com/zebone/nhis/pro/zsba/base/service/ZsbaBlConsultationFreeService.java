package com.zebone.nhis.pro.zsba.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.pro.zsba.base.dao.ZsbaBlConsultationFreeMapper;
import com.zebone.nhis.pro.zsba.cn.opdw.vo.BlConsultationFree;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 免收费用服务（博爱版）
 */
@Service
public class ZsbaBlConsultationFreeService {

	@Resource
    private ZsbaBlConsultationFreeMapper freeMappre;
	
	/**
	 * 查询免收费用信息集合
	 * 交易码：022003015013
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryBlConFree(String param, IUser user) {
		Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
    	// 查询免收费用信息
    	return freeMappre.qryBlConFreeApplyList(paramMap);  
    }
	
	/**
	 * 保存免收费用信息
	 * 交易号：022003015014
	 * @param param
	 * @param user
	 */
	public void saveBlConFree(String param, IUser user) {
		List<BlConsultationFree> paramList = JsonUtil.readValue(param, new TypeReference<List<BlConsultationFree>>(){});
		List<BlConsultationFree> insertBlConFree = new ArrayList<>();
		List<BlConsultationFree> updateBlConFree = new ArrayList<>();

		//循环处理数据为更新集合或插入集合
		paramList.forEach(blFree ->{
			if(StringUtils.isNotEmpty(blFree.getPkConfree())) {
				updateBlConFree.add(blFree);
			}else {
				ApplicationUtils.setDefaultValue(blFree, true);
				insertBlConFree.add(blFree);
			}
		});
		
		//批量插入更新
		if(insertBlConFree.size()>0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlConsultationFree.class), insertBlConFree);
		}
		if(updateBlConFree.size()>0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlConsultationFree.class), updateBlConFree);
		}
	}
	
	/**
	 * 删除免收费用信息（物理删除）
	 * 交易码：022003015015
	 * @param param
	 * @param user
	 */
	public void deleteBlConFree(String param, IUser user) {
		Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
		if(StringUtils.isNotBlank(MapUtils.getString(paramMap, "pkConfree"))) {
			DataBaseHelper.execute("delete from bl_consultation_free where pk_confree = ? ", MapUtils.getString(paramMap, "pkConfree"));
		}	
	}
}
