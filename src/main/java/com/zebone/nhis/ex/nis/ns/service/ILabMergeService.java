package com.zebone.nhis.ex.nis.ns.service;

import java.util.List;

import com.zebone.nhis.bl.pub.vo.SettleInfo;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.ex.nis.ns.vo.LabColVo;

/**
 * 检验并管接口，各项目不同检验并管方式请编写自己的具体业务实现类
 * @author tongjiaqi
 *
 */
public interface ILabMergeService {
	/**
	 * 处理检验并管方法
	 * @param settlevo
	 */
      public List<LabColVo> dealLabMergeMethod(List<LabColVo> dtList, boolean isCont);
}
