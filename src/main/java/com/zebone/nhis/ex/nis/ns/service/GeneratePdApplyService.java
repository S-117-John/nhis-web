package com.zebone.nhis.ex.nis.ns.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.ApplicationUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.dao.DeptPdApplyMapper;
import com.zebone.nhis.ex.nis.ns.support.pdap.CreateFacade;
import com.zebone.nhis.ex.nis.ns.support.pdap.GeneratePdApContext;
import com.zebone.nhis.ex.nis.ns.support.pdap.SynExListInfoHandler;
import com.zebone.nhis.ex.nis.ns.vo.GeneratePdApplyBufferVo;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyDtVo;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyVo;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.platform.modules.exception.BusException;
/**
 * 生成请领单处理类
 * @author yangxue
 *
 */
@Service
public class GeneratePdApplyService {
	@Resource
	private DeptPdApplyMapper pdApplyMapper;
	
	/**
	 * 设置生成请领单的各类缓存数据
	 * @param param(pkPvs,pkDept,pkEmp,nameEmp,pkOrg,endDate)
	 * @return
	 * @throws BusException 
	 */
	public String generate(Map<String,Object> param)throws BusException{
		
		String pk_Dept = CommonUtils.getString(param.get("pkDept"));
		GeneratePdApplyBufferVo creatInf = new GeneratePdApplyBufferVo();
		creatInf.setPkOrg(CommonUtils.getString(param.get("pkOrg")));
		creatInf.setExceTime(new Date());
		creatInf.setFinish(false);
		
		if(!GeneratePdApContext.getInstance().doExecute(pk_Dept, creatInf))
			return "病区的请领正在生成，请稍后再试！";
		
		//获取要生成请领的医嘱执行单
		List<GeneratePdApExListVo> exList = getExList(param);
		
		if(null == exList || exList.size() == 0){
			GeneratePdApContext.getInstance().finish(pk_Dept);
			return "没有需要生成请领的数据，请重新选择！";
		}
		
		//生成请领记录
		List<ExPdApply> apList = new ArrayList<ExPdApply>();
		List<PdApplyDtVo> dtList = new ArrayList<PdApplyDtVo>();
		boolean isSplit = "1".equals(ApplicationUtils.getSysparam("CN0030", false));//是否分散模式生成草药请领
		String msg = new CreateFacade().createAP(param, exList, apList,dtList,isSplit);
		
		//没有生成请领明细返回
		if(null == dtList || dtList.size() == 0){
			GeneratePdApContext.getInstance().finish(pk_Dept);
			return msg;
		}
		
		//设置缓存
		GeneratePdApContext.getInstance().setApBuffer(pk_Dept, apList);
		List<ExPdApplyDetail> apdt = new ArrayList<ExPdApplyDetail>();
		List<PdApplyVo> show = new ArrayList<PdApplyVo>();
		List<SynExListInfoHandler> info = new ArrayList<SynExListInfoHandler>();
		for(PdApplyDtVo dt : dtList){
			PdApplyVo showVO = dt.getShowVO();
			//showVO.setFlagArr(f);//设置是否欠费
			apdt.add(dt.getDtvo());//设置请领明细
			show.add(showVO);//设置请领单
			info.add(dt.getInfo());//设置更新数据
		}
		GeneratePdApContext.getInstance().setDtBuffer(pk_Dept, apdt);
		GeneratePdApContext.getInstance().setShowBuffer(pk_Dept, show);
		GeneratePdApContext.getInstance().setUpBuffer(pk_Dept, info);
		
		return msg;
	}
	/**
	 * 获取需要生成医嘱的执行单
	 * @param param(pkPvs,pkDept,pkEmp,nameEmp,pkOrg，endDate,pkCnOrdsPivas)
	 * @return
	 * @throws BusException
	 */
	@SuppressWarnings("unchecked")
	private List<GeneratePdApExListVo> getExList(Map<String,Object> param) throws BusException{
		
		String endDate = CommonUtils.getString(param.get("endDate"));
		//String now = DateUtils.defaultDateFormat.format(new Date()).substring(0, 8);
		//String end = CommonUtils.isEmptyString(endDate)?now+"235959":endDate.substring(0, 8)+"235959";
		param.put("endDate", endDate);
		List<GeneratePdApExListVo> result = new ArrayList<>();
		List<GeneratePdApExListVo> resultOth= pdApplyMapper.getGenPdApOrdList(param);
		if(resultOth != null && resultOth.size()>0){
			result.addAll(resultOth);
		}
		//获取长期口服的执行单
		String endTime = ApplicationUtils.getSysparam("EX0007", false);//口服请领截止时间
		if(!CommonUtils.isEmptyString(endTime)){
			String OranlEndDate = endDate.substring(0, 8)+endTime.replace(":", "");
			param.put("endDate", OranlEndDate);//如果ex0007不为空，截止时刻取ex0007
		}
		List<GeneratePdApExListVo> resultOral= pdApplyMapper.getGenPdApOrdOralList(param);
		if(resultOral != null && resultOral.size()>0){
			result.addAll(resultOral);
		}
		return result;
	}
	/**
	 * 生成请领单，无缓存模式
	 * @return
	 */
	public List<PdApplyVo>  generateApplyDt(Map<String,Object> param){
		//String pk_Dept = CommonUtils.getString(param.get("pkDept"));
		//获取要生成请领的医嘱执行单
		List<GeneratePdApExListVo> exList = getExList(param);
		
		if(null == exList || exList.size() == 0){
			return null;
		}
		
		//生成请领记录
		List<ExPdApply> apList = new ArrayList<ExPdApply>();
		List<PdApplyDtVo> dtList = new ArrayList<PdApplyDtVo>();
		boolean isSplit = "1".equals(ApplicationUtils.getSysparam("CN0030", false));//是否分散模式生成草药请领
		new CreateFacade().createAP(param, exList, apList,dtList,isSplit);
		
		//没有生成请领明细返回
		if(null == dtList || dtList.size() == 0){
			return null;
		}
		
		//设置缓存
		//GeneratePdApContext.getInstance().setApBuffer(pk_Dept, apList);
		List<PdApplyVo> show = new ArrayList<PdApplyVo>();
		StringBuilder errMsg = new StringBuilder("");
		for(PdApplyDtVo dt : dtList){
			if(CommonUtils.isNotNull(dt.getErrMsg())){//生成请领时的错误信息
				errMsg.append(dt.getErrMsg());
				continue;
			}
			PdApplyVo showVO = dt.getShowVO();
			showVO.setApdt(dt.getDtvo());//设置请领明细
			show.add(showVO);//设置请领单
		}
		if(CommonUtils.isNotNull(errMsg))
			throw new BusException(errMsg.toString());
		return show;
	}
	
}
