package com.zebone.nhis.ex.nis.ns.support.pdap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.vo.GeneratePdApplyBufferVo;
import com.zebone.nhis.ex.nis.ns.vo.PdApplyVo;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.platform.modules.exception.BusException;

/**
 * 请领单生成情况记录
 * @author yangxue
 *
 */
public class GeneratePdApContext {

	private static GeneratePdApContext instance;
	private Map<String,GeneratePdApplyBufferVo> app_map;//病区请领单执行情况记录
	
	private GeneratePdApContext(){
		app_map = new HashMap<String,GeneratePdApplyBufferVo> ();
	}
	
	public static GeneratePdApContext getInstance(){
		if(null == instance){
			instance = new GeneratePdApContext();
		}
		return instance;
	}
	
	/**
	 * 记录开始生成信息，
	 * @param pk_dept
	 * @param vo
	 * @return 如果正在生成，返回false，否则生成true
	 */
	public boolean doExecute(String pk_dept,GeneratePdApplyBufferVo vo){

		//如果没有当前病区的记录，未生成，添加记录；
		if(null == app_map.get(pk_dept)){
			synchronized(app_map){//加锁当前病区
				if(null == app_map.get(pk_dept)){
					app_map.put(pk_dept, vo);
					return true;
				}
			}
		}
		//如果当前病区请领单已经生成完成；开始下一次生成，完成标志设成false，记录执行时间
		GeneratePdApplyBufferVo appVO = app_map.get(pk_dept);
		if(appVO.isFinish()){
			synchronized(appVO){
				if(appVO.isFinish()){
					initAppVO(vo, appVO);
					return true;
				}
			}
		}
		//如果当前病区请领单没有生成完，但是生成执行时间大于30分钟，判断已经终止执行操作，记录新的执行时间
		int space = CommonUtils.getInt(ExSysParamUtil.getUnlockTime());//默认30分钟，可调
		Date time = vo.getExceTime();
		if(space < DateUtils.getMinsBetween(appVO.getExceTime(), time)){
			synchronized(appVO){
				if(appVO.isFinish()){
					initAppVO(vo, appVO);
					return true;
				}
				if(space < DateUtils.getMinsBetween(appVO.getExceTime(), time)){
					initAppVO(vo, appVO);
					return true;
				}
				
			}
		}
		return false;
	}

	/**
	 * 开始执行，初始化appVO
	 * @param vo
	 * @param appVO
	 */
	private void initAppVO(GeneratePdApplyBufferVo vo, GeneratePdApplyBufferVo appVO) {
		appVO.setFinish(false);
		appVO.setExceTime(vo.getExceTime());
		clearBuffer(appVO);
	}

	/**
	 * 清空缓存
	 * @param appVO
	 */
	private void clearBuffer(GeneratePdApplyBufferVo appVO) {
		appVO.setApList(null);
		appVO.setDtList(null);
		appVO.setUpdateList(null);
		appVO.setShowList(null);
		appVO.setErr("");
	}
	
	/**
	 * 设置当前科室的请领单生成完成
	 * @param pk_dept
	 * @throws BusException
	 */
	public void finish(String pk_dept)throws BusException{
		GeneratePdApplyBufferVo vo = app_map.get(pk_dept);
		if(vo == null){
			return;
		}
		clearBuffer(vo);
		vo.setFinish(true);
	}
	
	/**
	 * 为当前科室请领设置缓存
	 * @param pk_dept
	 * @param list
	 * @throws BusException
	 */
	public void setApBuffer(String pk_dept,List<ExPdApply> list)throws BusException{
		GeneratePdApplyBufferVo vo = getSetBuffer(pk_dept);
		vo.setApList(list);
	}
	
	/**
	 * 为当前科室请领设置缓存
	 * @param pk_dept
	 * @param list
	 * @throws BusException
	 */
	public void setDtBuffer(String pk_dept,List<ExPdApplyDetail> list)throws BusException{
		GeneratePdApplyBufferVo vo = getSetBuffer(pk_dept);
		vo.setDtList(list);
	}
	
	/**
	 * 为当前科室请领设置缓存
	 * @param pk_dept
	 * @param list
	 * @throws BusException
	 */
	public void setShowBuffer(String pk_dept,List<PdApplyVo> list)throws BusException{
		GeneratePdApplyBufferVo vo = getSetBuffer(pk_dept);
		vo.setShowList(list);
	}
	
	/**
	 * 为当前科室请领设置缓存
	 * @param pk_dept
	 * @param list
	 * @throws BusException
	 */
	public void setUpBuffer(String pk_dept,List<SynExListInfoHandler> list)throws BusException{
		GeneratePdApplyBufferVo vo = getSetBuffer(pk_dept);
		vo.setUpdateList(list);
	}
	
	/**
	 * 为当前科室增加显示缓存
	 * @param pk_dept
	 * @param list
	 * @throws BusException
	 */
	public void addShowBuffer(String pk_dept,PdApplyVo showVO)throws BusException{
		GeneratePdApplyBufferVo vo = getSetBuffer(pk_dept);
		List<PdApplyVo> list = vo.getShowList();
		if(null == list){
			list = new ArrayList<PdApplyVo>();
			list.add(showVO);
			vo.setShowList(list);
		}else{
			list.add(showVO);
		}
	}

	/**
	 * 设置缓存时校验
	 * @param pk_dept
	 * @return
	 * @throws BusException
	 */
	private GeneratePdApplyBufferVo getSetBuffer(String pk_dept)
			throws BusException {
		GeneratePdApplyBufferVo vo = app_map.get(pk_dept);
		if(null == vo){
			throw new  BusException("请领单没有开始生成，请检查！");
		}
		if(vo.isFinish())
			throw new  BusException("请领单已经生成结束，请检查！");
		return vo;
	}
	
	/**
	 * 清空当前科室缓存
	 * @param pk_dept
	 * @param list
	 * @throws BusException
	 */
	public void clearBuffer(String pk_dept)throws BusException{
		app_map.put(pk_dept, null);
	}
	
	/**
	 * 判断当前科室的请领单是否结束
	 * @param pk_dept
	 * @param list
	 * @throws BusException
	 */
	public boolean isExecuteCreate(String pk_dept)throws BusException{
		GeneratePdApplyBufferVo vo = app_map.get(pk_dept);
		if(null == vo){
			return false;
		}
		if(app_map.get(pk_dept).isFinish())
			return false;
		return true;
	}
	
	/**
	 * 获取当前科室的显示缓存
	 * @param pk_dept
	 * @return
	 * @throws BusException
	 */
	public List<PdApplyVo> getShowList(String pk_dept)throws BusException {
		GeneratePdApplyBufferVo vo = app_map.get(pk_dept);
		return vo == null ? null : vo.getShowList();
	}
	
	/**
	 * 获取当前科室的请领明细缓存
	 * @param pk_dept
	 * @return
	 * @throws BusException
	 */
	public List<ExPdApplyDetail> getApDtList(String pk_dept)throws BusException {
		GeneratePdApplyBufferVo vo = app_map.get(pk_dept);
		return vo == null ? null : vo.getDtList();
	}
	
	/**
	 * 获取当前科室的请领缓存
	 * @param pk_dept
	 * @return
	 * @throws BusException
	 */
	public List<SynExListInfoHandler> getUpdateList(String pk_dept)throws BusException {
		GeneratePdApplyBufferVo vo = app_map.get(pk_dept);
		return vo == null ? null : vo.getUpdateList();
	}
	
	/**
	 * 获取当前科室的请领缓存
	 * @param pk_dept
	 * @return
	 * @throws BusException
	 */
	public List<ExPdApply> getApList(String pk_dept)throws BusException {
		GeneratePdApplyBufferVo vo = app_map.get(pk_dept);
		return vo == null ? null : vo.getApList();
	}
	
	/**
	 * 获取当前科室的错误信息缓存
	 * @param pk_dept
	 * @return
	 * @throws BusException
	 */
	public String getErrMessage(String pk_dept)throws BusException {
		GeneratePdApplyBufferVo vo = app_map.get(pk_dept);
		return vo == null ? null : vo.getErr();
	}
	
	public void update(String pk_dept,String pk_pdapdt,String flag_base)throws BusException{
		if(null == pk_pdapdt){
			throw new BusException("请确定要修改的请领！");
		}
		GeneratePdApplyBufferVo vo = this.getSetBuffer(pk_dept);
		List<SynExListInfoHandler> up_list = vo.getUpdateList();
		for (SynExListInfoHandler info : up_list){
			if(info.getPk_pdapdt().equals(pk_pdapdt)){
				//info.setFlag_base(flag_base);
				break;
			}
		}
		List<ExPdApplyDetail> dt_list = vo.getDtList();
		for (ExPdApplyDetail dt : dt_list){
			if(dt.getPkPdapdt().equals(pk_pdapdt)){
				//dt.set(flag_base);待确定
				//dt.set(CreatePdAppUtil.getDispmodeByBase(flag_base)); 待添加
				break;
			}
		}
	}
}
