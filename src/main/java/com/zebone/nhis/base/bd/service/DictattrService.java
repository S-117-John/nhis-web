package com.zebone.nhis.base.bd.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zebone.nhis.base.bd.dao.DictattrMapper;
import com.zebone.nhis.base.bd.vo.DictattrVo;
import com.zebone.nhis.base.bd.vo.DictattrsqlVo;
import com.zebone.nhis.base.bd.vo.ReDictattrVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class DictattrService {

	@Autowired
	private DictattrMapper dictattrMapper;

	public List<Map<String, Object>> qryDictattrTemp(String param, IUser user) {
		String dicttype = JsonUtil.getFieldValue(param, "dicttype");
		return dictattrMapper.qirDictattrTemp(dicttype);
	}

	public List<Map<String, Object>> qryDictattr(String param, IUser user) {
		String dictAttr = JsonUtil.getFieldValue(param, "dictAttr");
		return dictattrMapper.qirDictattr(dictAttr);
	}

	public void saveDictattr(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
		ReDictattrVo paramMap = JsonUtil.readValue(param, ReDictattrVo.class);
		List<DictattrVo> updataDictattr = paramMap.getUpdateDictattr();
		List<DictattrVo> delDictattr = paramMap.getDelDictattr();
		List<DictattrVo> newDictattr = paramMap.getNewDictattr();
		User userInfo = (User) user;
		Date nowDate = new Date();
		// 修改数据处理
		if (updataDictattr != null) {
			for (DictattrVo dictattrVo : updataDictattr) {
				dictattrVo.setPkOrg(userInfo.getPkOrg());
				dictattrVo.setModifier(userInfo.getPkUser());
				dictattrVo.setModityTime(nowDate);
				dictattrVo.setTs(nowDate);
				dictattrMapper.updateDictattr(dictattrVo);
			}
		}
		if (newDictattr != null) { // 新增数据处理
			for (DictattrVo dictattrVo : newDictattr) {
				DictattrsqlVo dictattrsqlVo = new DictattrsqlVo(); 
//				String keyid = NHISUUID.getKeyId();
//				dictattrVo.setPkDictattr(keyid);
				if(userInfo.getPkOrg()!=null) dictattrVo.setPkOrg(userInfo.getPkOrg());
				if(userInfo.getPkOrg()==null) dictattrVo.setPkOrg("~");
				dictattrVo.setCreator(userInfo.getPkUser());
				dictattrVo.setCreateTime(nowDate);
				dictattrVo.setTs(nowDate);
				dictattrVo.setDelFlag("0");
				BeanUtils.copyProperties(dictattrsqlVo, dictattrVo);
				DataBaseHelper.insertBean(dictattrsqlVo);
				//dictattrMapper.insertDictattr(dictattrVo);
			}
		}
		if (delDictattr != null) {
			for (DictattrVo dictattrVo : delDictattr) {
				dictattrMapper.delDictattr(dictattrVo);
			}
		}

	}
	
	public List<Map<String, Object>> qryLeadDictattr(String param, IUser user) {
		DictattrVo dictattrVo = JsonUtil.readValue(param, DictattrVo.class);
		return dictattrMapper.qryLeadDictattr(dictattrVo);
	} 
}
