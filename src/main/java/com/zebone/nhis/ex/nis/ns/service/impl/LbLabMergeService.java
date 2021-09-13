package com.zebone.nhis.ex.nis.ns.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.service.ILabMergeService;
import com.zebone.nhis.ex.nis.ns.vo.LabColVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 * 灵璧医院处理检验项目并管服务
 */
@Service("lbLabMergeService")
public class LbLabMergeService implements ILabMergeService {
	/**
	 * 获取合并后的数据
	 * @param dtList 合并前数据
	 * @param isCont 容器|标本
	 * @return
	 */
	@Override
	public List<LabColVo> dealLabMergeMethod(List<LabColVo> dtList, boolean isCont) {
		// TODO Auto-generated method stub
		List<LabColVo> newDtList = new ArrayList<LabColVo>();
		List<LabColVo> upDtList = new ArrayList<LabColVo>();
		String codeApp = "";
		for(int i = 0 ; i < dtList.size() ; i++){
			if(isCont)//容器合并,按容器并单
			{
				dtList.get(i).setFormApp(dtList.get(i).getCodeApply());
				if(newDtList.size() == 0){
					if(null != dtList.get(i).getDtTubetype())
						newDtList.add(dtList.get(i));
					upDtList.add(dtList.get(i));
					continue;
				}
				if(!isSameCont(newDtList, dtList.get(i))){
					if(null != dtList.get(i).getDtTubetype())
						newDtList.add(dtList.get(i));
					upDtList.add(dtList.get(i));
				}
				else{
					if(i > 0){
						//相同时将上条记录的申请单设置到当前行
						codeApp = dtList.get(i-1).getFormApp();
						if(CommonUtils.isEmptyString(codeApp))
							codeApp = dtList.get(i-1).getCodeApply();
						dtList.get(i).setFormApp(codeApp);
						upDtList.add(dtList.get(i));
					}
				}
			} else {//标本合并：
				if(null == dtList.get(i).getDtSamptype())
					continue;
				if(newDtList.size() == 0){
					newDtList.add(dtList.get(i));
					continue;
				}
				if(!isSameSpec(newDtList, dtList.get(i)))//校验标本
					newDtList.add(dtList.get(i));
			}		
		}
		if(upDtList.size() > 0){
			DataBaseHelper.batchUpdate("update cn_lab_apply set form_app =:formApp where pk_cnord =:pkCnord ", upDtList);
		}
		return newDtList;
	}
	
	/**
	 * 校验是否合并容器
	 * 合并规则：同个人，同个校验科室，同个标本，同个容器，同个加急标志，同个执行时间,同个检验分组
	 * @param proVo
	 * @param curVo
	 * @return
	 */
	private boolean isSameCont(List<LabColVo> list,LabColVo curVo){
		for (LabColVo proVo : list) {
			if(StringUtils.isNotBlank(proVo.getPkDeptOcc())&&StringUtils.isNotBlank(proVo.getDtSamptype())
			   &&StringUtils.isNotBlank(proVo.getDtTubetype())&&StringUtils.isNotBlank(proVo.getFlagEmer())
			   && null != proVo.getDatePlan()&&StringUtils.isNotBlank(proVo.getDtLisgroup())){
				if(StringUtils.isNotBlank(curVo.getPkDeptOcc())&&StringUtils.isNotBlank(curVo.getDtSamptype())
						   &&StringUtils.isNotBlank(curVo.getDtTubetype())&&StringUtils.isNotBlank(curVo.getFlagEmer())
						   && null != curVo.getDatePlan()&&StringUtils.isNotBlank(curVo.getDtLisgroup())){
					if(curVo.getPkPv().equals(proVo.getPkPv())
							&& curVo.getPkDeptOcc().equals(proVo.getPkDeptOcc())
							&& curVo.getDtSamptype().equals(proVo.getDtSamptype())
							&& curVo.getDtTubetype().equals(proVo.getDtTubetype())
							&& curVo.getFlagEmer().equals(proVo.getFlagEmer())
							&& curVo.getDatePlan().equals(proVo.getDatePlan())
							&& curVo.getDtLisgroup().equals(proVo.getDtLisgroup()))
						return true;
				}else{
					 throw new BusException("请核对'"+curVo.getNameOrd()+"'医嘱检验属性");
				}
			}else{
				 throw new BusException("请核对'"+proVo.getNameOrd()+"'医嘱检验属性");
			}
			
		}
		return false;
	}

	
	/**
	 * 校验是否合并标本
	 * 合并规则：同个人，同个标本，取最大的数
	 * @param proVo
	 * @param curVo
	 * @return
	 */
	private boolean isSameSpec(List<LabColVo> list,LabColVo curVo){
		for (LabColVo proVo : list) {
			if(curVo.getPkPv().equals(proVo.getPkPv()) 
					&& curVo.getDtSamptype().equals(proVo.getDtSamptype())
					&& curVo.getDatePlan().equals(proVo.getDatePlan()))
				return true;
		}
		return false;
	}
}
