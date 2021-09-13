package com.zebone.nhis.bl.pub.service;


import com.zebone.nhis.bl.pub.vo.SettleInfo;
import com.zebone.nhis.common.module.bl.BlSettle;

/**
 * 医保结算公共接口，各项目不同医保请编写自己的具体业务实现类
 * @author yangxue
 *
 */
public interface IYBSettleService {
	/**
	 * 处理医保相关业务方法
	 * @param settlevo
	 */
      public void dealYBSettleMethod(SettleInfo settlevo,BlSettle stVo);
}
