package com.zebone.nhis.task.bd.dao;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.task.bd.vo.InvCheckVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdTaskDao {
	
	/**查询是否有票据项目没有设置费用分类*/
	public List<InvCheckVo> checkInvCateInfo();
	
	/**查询收费项目字典数据完整性*/
	public List<BdItem> checkBdItemInfo();
	
	/**查询医嘱项目字典数据完整性*/
	public List<BdOrd> checkBdOrdInfo();
	
	/**查询药品字典数据完整性*/
	public List<BdPd> checkBdPdInfo();
	
}
