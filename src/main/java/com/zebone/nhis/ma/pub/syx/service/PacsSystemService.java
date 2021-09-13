package com.zebone.nhis.ma.pub.syx.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.zebone.nhis.ma.pub.syx.dao.PacsSystemMapper;
import com.zebone.nhis.ma.pub.syx.vo.Tfunctionitemlistforpacs;
import com.zebone.nhis.ma.pub.syx.vo.Tfunctionrequestforpacs;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * pacs系统业务处理服务(无事物)
 * 
 * @author yangxue
 * 
 */
@Service
public class PacsSystemService {

	@Autowired
	private PacsSystemMapper pacsSystemMapper;

	/**
	 * 保存数据至检查系统
	 * 
	 * @param tfunctionitemlistforpacs
	 * @param tfunctionrequestforpacsList
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<Tfunctionitemlistforpacs> savaPacs(List<Tfunctionitemlistforpacs> tfunctionitemlistforpacs, List<Tfunctionrequestforpacs> tfunctionrequestforpacsList) {
		// 中间库已存在跳过插入
		List<String> codeApplys = null;
		List<String> itemCodeApplys = null;
		if (tfunctionrequestforpacsList != null && tfunctionrequestforpacsList.size() > 0) {
			codeApplys = pacsSystemMapper.qryRepeatApply(tfunctionrequestforpacsList);
			itemCodeApplys = pacsSystemMapper.qryItemRepeatApply(tfunctionrequestforpacsList);
			//主表去掉已存在的
			if (codeApplys != null && codeApplys.size() > 0) {
				for(String temp : codeApplys){
					for (int i = tfunctionrequestforpacsList.size() - 1; i >= 0; i--) {
						if (tfunctionrequestforpacsList.get(i).getFunctionrequestid().equals(temp))
							tfunctionrequestforpacsList.remove(i);
					}
				}
			}
			//子表去掉已存在的
			if (itemCodeApplys != null && itemCodeApplys.size() > 0) {
				for (String temp : itemCodeApplys) {
					for (int i = tfunctionitemlistforpacs.size() - 1; i >= 0; i--) {
						if (tfunctionitemlistforpacs.get(i).getFunctionrequestid().equals(temp))
							tfunctionitemlistforpacs.remove(i);
					}
				}
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(Tfunctionrequestforpacs.class), tfunctionrequestforpacsList);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(Tfunctionitemlistforpacs.class), tfunctionitemlistforpacs);
		}
		return tfunctionitemlistforpacs;
	}

	/**
	 * 检查申请单上传成功，更新表cn_ris_apply
	 * 
	 * @param savaPacs
	 */
	public void updateRisApply(List<Tfunctionitemlistforpacs> savaPacsList) {
		List<String> codeApplys = Lists.newArrayList();
		for (Tfunctionitemlistforpacs temp : savaPacsList) {
			codeApplys.add(temp.getFunctionrequestid());
		}
		if (codeApplys.size() > 0) {
			List<String> pkCnords = pacsSystemMapper.qryPkCnord(codeApplys);
			pacsSystemMapper.updateRisApply(pkCnords);
		}
	}

}
