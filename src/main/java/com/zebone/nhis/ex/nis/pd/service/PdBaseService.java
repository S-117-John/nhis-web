package com.zebone.nhis.ex.nis.pd.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.pd.BdPdBase;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.pd.dao.PdBaseMapper;
import com.zebone.nhis.ex.nis.pd.vo.PdBaseVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 基数药品维护
 * @author yangxue
 *
 */
@Service
public class PdBaseService {
	@Resource
	private PdBaseMapper pdBaseMapper; 
	
	/**
	 * 查询基数药品信息
	 * @param param{pkDeptNs,pdname}
	 * @param user
	 * @return
	 */
	public List<PdBaseVo> queryPdBaseList(String param,IUser user){
		Map map = JsonUtil.readValue(param, Map.class);
		if(map==null||map.get("pkDeptNs")==null||map.get("pkDeptNs").equals("")) 
			throw new BusException("未获取到所查基数药品所属部门！");
		return pdBaseMapper.queryPdBaseByCon(map);
	}
	/**
	 * 保存基数药
	 * @param param
	 * @param user
	 */
	public void savePdBase(String param,IUser user){
		List<BdPdBase> list = JsonUtil.readValue(param,new TypeReference<List<BdPdBase>>(){});
		User u = (User)user;
		if(list!=null&&list.size()>0){
			for(BdPdBase vo:list){
				vo.setCreateTime(new Date());
				vo.setCreator(u.getPkEmp());
				vo.setDelFlag("0");
				vo.setTs(new Date());
				vo.setPkPdbase(NHISUUID.getKeyId());
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdPdBase.class), list);
		}
	}
	/**
	 * 删除基数药
	 * @param param{vo}
	 * @param user
	 */
	public void deletePdBase(String param,IUser user){
		BdPdBase vo = JsonUtil.readValue(param, BdPdBase.class);
		if(vo!=null){
			DataBaseHelper.deleteBeanByPk(vo);
		}
	}
	
}
