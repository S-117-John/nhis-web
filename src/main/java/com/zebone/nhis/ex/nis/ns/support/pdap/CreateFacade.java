package com.zebone.nhis.ex.nis.ns.support.pdap;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyDtVo;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.platform.modules.exception.BusException;
/**
 * 生成请领对外开放类
 * @author yangxue
 *
 */
public class CreateFacade {

	/**
	 * 生成请领方法
	 * @param param
	 * @param exList
	 * @param apList
	 * @param dtList
	 * @return
	 */
	public String createAP(Map<String,Object> param,
			List<GeneratePdApExListVo> exList, List<ExPdApply> apList,
			List<PdApplyDtVo> dtList,boolean isSplit){
		String msg = "";
		CreateApHandler handler = new CreateApByPVHandler();
		//对查询结果按pv进行分组
		List<List<GeneratePdApExListVo>> group_list = handler.groupExList(exList);
		for(List<GeneratePdApExListVo> list : group_list){
			//对分组结果进行生成请领
		//	try {
				List<PdApplyDtVo> re_list = handler.createAP(param,list,apList,isSplit);
				dtList.addAll(re_list);
//			} catch (BusException e) {
//				msg += handler.getErrorMsg(list.get(0))+e.getMessage()+";";
//				e.getStackTrace();
//			}
		}
		return msg;
	}
}