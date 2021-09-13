package com.zebone.nhis.ma.pub.syx.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.ma.pub.syx.vo.AtfYpPageNo;
import com.zebone.nhis.ma.pub.syx.vo.AtfYpxx;
import com.zebone.nhis.ma.pub.syx.vo.AtfYpxxNy;
import com.zebone.nhis.ma.pub.syx.vo.AtfYpxxSyx;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class SendDrugsToMachineService {

	/**
	 * 保存包药机数据
	 * @param pageNo
	 * @param atfYpxxsSyx
	 * @param codeArea
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveDrugToMachine(AtfYpPageNo pageNo, List<AtfYpxxSyx> atfYpxxsSyx, String codeArea) {
		//坑爹的包药机，同一家医院，不同院区用的表还不一样，字段都一样。。。
		if("001".equals(codeArea)){//北院
			DataBaseHelper.insertBean(pageNo);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(AtfYpxx.class),atfYpxxsSyx);
		}else if("002".equals(codeArea)){//南院
			DataBaseHelper.insertBean(pageNo);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(AtfYpxxNy.class),atfYpxxsSyx);
		}else{
			throw new BusException("请维护好发药科室所属院区！");
		}
	}

	/**
	 * 完成发药，更新发药表数据
	 * @param param
	 */
	public void updateFlagSend(Map<String, Object> param) {
		DataBaseHelper.execute("update ex_pd_de set flag_sendtofa='1' where pk_dept_de=? and code_de=? and pk_pddecate=? and flag_sendtofa='0'", new Object[] { param.get("pkDeptDe"), param.get("codeDe"), param.get("pkPddecate") });
	}

}
