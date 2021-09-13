package com.zebone.nhis.scm.dict.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.pub.BdPdMens;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.scm.pub.vo.PdAndAllParam;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 
 * @author JonorHuang
 *
 */

@Service

public class PdMensService {
	
	/**
	 * 保存药品溶媒关联关系
	 * @param param
	 * @param user
	 * @return
	 */
	
	public void savePdMens(String param, IUser user){
		PdAndAllParam pdAndAll = JsonUtil.readValue(param, PdAndAllParam.class);
//		BdPd pd = pdAndAll.getPd();
		List<BdPdMens> bdPdMensList = pdAndAll.getPdMensList();
		List<BdPdMens> delBdPdMensList = pdAndAll.getDelPdMensList();
		List<BdPdMens> cancelDefPdMensList = pdAndAll.getCancelDefPdMensList();
		
		/**验证溶媒编码是否重复
		 * 至少2个，才验重*/
//		if(bdPdMensList != null && bdPdMensList.size() > 1){
//			
//			Map<String, String> codeMap = new HashMap<String, String>();
//			int len = bdPdMensList.size();
//			for(int i=0; i<len; i++){
//				String code = bdPdMensList.get(i).getPkPdMens();
//				if(codeMap.containsKey(code)){
//					throw new BusException("溶媒编码重复！");
//				}
//				codeMap.put(code, bdPdMensList.get(i).getPkPdmens());
//			}
//			
//		}	
		
		
		
		/**保存或更新药品溶媒关系*/
		if(bdPdMensList != null && bdPdMensList.size() != 0){
			for(BdPdMens pdMens : bdPdMensList){
				if(pdMens.getPkPdmens() != null){
					DataBaseHelper.updateBeanByPk(pdMens, false);
				}else{
					ApplicationUtils.setDefaultValue(pdMens, true);
					DataBaseHelper.insertBean(pdMens);
				}
			}
		}
		
		/**删除药品溶媒关系*/
		if(delBdPdMensList !=null && delBdPdMensList.size() !=0){
			for(BdPdMens pdMens : delBdPdMensList){
				if(pdMens.getPkPdmens() != null){
					DataBaseHelper.execute("delete from bd_pd_mens where pk_pdmens=?",new Object[]{pdMens.getPkPdmens() });
				}
			}
		}
		
		/**取消默认标志*/
		if(cancelDefPdMensList !=null && cancelDefPdMensList.size() !=0){
			for(BdPdMens pdMens : cancelDefPdMensList){
				if(pdMens.getPkPdMens() != null && pdMens.getPkPd() !=null ){
					DataBaseHelper.execute("update bd_pd_mens set flag_def='0' where flag_def='1' and pk_pd=? and pk_pd_mens=?",new Object[]{pdMens.getPkPd() ,pdMens.getPkPdMens() });
				}
			}
		}
		
	}

}
