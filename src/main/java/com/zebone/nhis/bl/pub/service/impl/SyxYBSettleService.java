package com.zebone.nhis.bl.pub.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.IYBSettleService;
import com.zebone.nhis.bl.pub.syx.service.IpSettleGzgyService;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.SettleInfo;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.support.ApplicationUtils;
/**
 * 孙逸仙医院处理外部医保服务
 * @author IBM
 *
 */
@Service("syxYBSettleService")
public class SyxYBSettleService implements IYBSettleService{
	@Resource
	private IpSettleGzgyService ipSettleGzgyService;
    /**
     * 处理各医保个性化业务需求
     */
	@Override
	public void dealYBSettleMethod(SettleInfo settlevo, BlSettle stVo) {
		
		/**广州公医统一结算策略*/
		//判断是否启用广州公医
		if("1".equals(ApplicationUtils.getSysparam("BL0023", false))){
			
			if(BlcgUtil.converToTrueOrFalse(settlevo.getFlagSpItemCg())){//特殊项目结算
				ipSettleGzgyService.gzgySpItemIpSettle(settlevo, stVo);
			}else{	//正常结算
				ipSettleGzgyService.gzgyIpSettle(settlevo, stVo);
			}
			
		}
		
	}

}
