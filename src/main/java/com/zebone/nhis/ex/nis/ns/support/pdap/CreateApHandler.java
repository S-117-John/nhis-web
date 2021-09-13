package com.zebone.nhis.ex.nis.ns.support.pdap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyDtVo;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.platform.modules.exception.BusException;

/**
 * 按pv分组生成请领处理
 * @author yangxue
 *
 */
public class CreateApHandler {
	
	protected CreateApHandler next;

	/**
	 * 获取下级，按同一个患者生成
	 * @return
	 */
	protected CreateApHandler getNext(){
		if(null == next){
			next = new CreateApByPVHandler();
		}
		return next;
	}

	/**
	 * 生成方法
	 * @param param
	 * @param exList
	 * @param apList
	 * @return
	 * @throws BusException
	 */
	public List<PdApplyDtVo> createAP(Map<String, Object> param,
			 List<GeneratePdApExListVo> exList ,List<ExPdApply> apList,boolean isSplit) throws BusException {
		if(exList == null || exList.size() == 0){
			return null;
		}
		//按pv分组排序
		List<List<GeneratePdApExListVo>> pvExList = getNext().groupExList(exList);
		List<List<PdApplyDtVo>> new_list = new ArrayList<List<PdApplyDtVo>>();
		//循环获取生成记录
		for(List<GeneratePdApExListVo> list : pvExList){
			List<PdApplyDtVo> re_list = getNext().createAP(param,list,apList,isSplit);
			new_list.add(re_list);
		}
		List<PdApplyDtVo> result = new ArrayList<PdApplyDtVo>();
		for(List<PdApplyDtVo> rl : new_list){
			result.addAll(rl);
		}
		return result;
	}
	
	/**
	 * 根据key分组执行记录
	 * @param exList
	 * @return
	 */
	public List<List<GeneratePdApExListVo>> groupExList(List<GeneratePdApExListVo> exList){
		List<List<GeneratePdApExListVo>> result = new ArrayList<List<GeneratePdApExListVo>>();
		String pk = "";
		List<GeneratePdApExListVo> list = null;
		for(int i = 0 ; i < exList.size() ; i++){
			GeneratePdApExListVo vo = exList.get(i);
			String key = getKeyValue(vo);
			if(pk.equals(key)){
				list.add(vo);
			}else{
				List<GeneratePdApExListVo> listPv = new ArrayList<GeneratePdApExListVo>();
				listPv.add(vo);
				result.add(listPv);
				list = listPv;
				pk = key;
			}
		}
		return result;
	}
	
	/**
	 * 获取分组字段值
	 * @param vo
	 * @return
	 */
	protected String getKeyValue(GeneratePdApExListVo vo){
		return vo.getPkPv();
	}
	
	/**
	 * 返回错误信息
	 * @param vo
	 * @return
	 */
	public String getErrorMsg(GeneratePdApExListVo vo){
		return "生成请领单报错，请查看！";
	}
}
