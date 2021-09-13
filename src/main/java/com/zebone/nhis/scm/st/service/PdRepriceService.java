package com.zebone.nhis.scm.st.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdRepriceHist;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.service.PdRepricePubService;
import com.zebone.nhis.scm.pub.vo.PdRepriceDtVo;
import com.zebone.nhis.scm.pub.vo.PdRepriceHistVo;
import com.zebone.nhis.scm.pub.vo.PdRepriceVo;
import com.zebone.nhis.scm.st.dao.PdRepriceMapper;
import com.zebone.nhis.scm.st.vo.PdRepriceSubVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 调价处理
 * @author yangxue
 *
 */
@Service
public class PdRepriceService {
    @Resource
	private PdRepriceMapper pdRepriceMapper;
    @Resource
    private PdRepricePubService pdRepricePubService;
	/**
	 * 查询调价单{pkOrg,dtReptype,codeRep,dateBeginE,dateEndE,dateBeginC,dateEndC,euStatus}
	 * @param param
	 * @param user
	 * @return
	 */
	public List<PdRepriceVo> queryRepriceList(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null){
			map = new HashMap<String,Object>();
		}
		if(CommonUtils.isNotNull(map.get("dateBeginE"))){
			map.put("dateBeginE", CommonUtils.getString(map.get("dateBeginE")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEndE"))){
			map.put("dateEndE", CommonUtils.getString(map.get("dateEndE")).substring(0, 8)+"235959");
		}
		if(CommonUtils.isNotNull(map.get("dateBeginC"))){
			map.put("dateBeginC", CommonUtils.getString(map.get("dateBeginC")).substring(0, 8)+"000000");
		}
		if(CommonUtils.isNotNull(map.get("dateEndC"))){
			map.put("dateEndC", CommonUtils.getString(map.get("dateEndC")).substring(0, 8)+"235959");
		}
		map.put("pkOrg", ((User)user).getPkOrg());
		return pdRepriceMapper.queryRepriceList(map);
	}
	/**
	 * 查询调价明细
	 * @param param--pkPdrep
	 * @param user
	 * @return
	 */
	public List<PdRepriceDtVo> queryRepriceDtList(String param,IUser user){
		String pkPdrep = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pkPdrep))
			throw new BusException("未获取到调价单主键");
		return pdRepriceMapper.queryRepriceDtList(pkPdrep);
	}
	/**
	 * 保存调价单
	 * @param param{PdRepriceVo}
	 * @param user
	 */
	public PdRepriceVo saveReprice(String param,IUser user){
		PdRepriceVo vo = JsonUtil.readValue(param, PdRepriceVo.class);
		if(vo == null)
			throw new BusException("未获取到调价单数据");
		pdRepricePubService.saveReprice(param, (User)user, vo);
		return vo;
	}
	/**
	 * 取消调价单
	 * @param param --调价单主键
	 * @param user
	 */
	public void cancelReprice(String param,IUser user){
		String pk_pdrep = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pk_pdrep))
			throw new BusException("未获取到调价单主键");
		String sql = "update pd_reprice  set eu_status='9',flag_canc='1',pk_emp_canc=:pkEmp,"+
				" name_emp_canc=:nameEmp,date_canc=:dateChk,ts=:dateChk where pk_pdrep=:pkPdrep and "+
				" eu_repmode='1' and eu_status='1' and flag_canc='0' ";
		Map<String,Object> map = new HashMap<String,Object>();
		User u = (User)user;
		map.put("dateChk", new Date());
		map.put("pkEmp", u.getPkEmp());
		map.put("nameEmp", u.getNameEmp());
		map.put("pkPdrep", pk_pdrep);
		DataBaseHelper.update(sql, map);
	}
	/**
	 * 删除调价单
	 * @param param--调价单主键
	 * @param user
	 */
	public void deleteReprice(String param,IUser user){
		String pk_pdrep = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pk_pdrep))
			throw new BusException("未获取到调价单主键");
		StringBuffer sql=new StringBuffer();
		sql.append("delete from pd_reprice_detail  where pd_reprice_detail.pk_pdrep = ? and");
		sql.append("  exists (select 1  from pd_reprice   where pd_reprice_detail.pk_pdrep=pd_reprice.pk_pdrep and pd_reprice.eu_status='0')");
		DataBaseHelper.execute(sql.toString(), new Object[]{pk_pdrep});
		String sql_d = "delete from pd_reprice  where pk_pdrep = ? and eu_status='0' ";
		DataBaseHelper.execute(sql_d, new Object[]{pk_pdrep});
	}
	/**
	 * 审核调价单--生成调价记录查询
	 * @param param
	 * @param user
	 */
	public List<PdRepriceHistVo> generateReprice(String param,IUser user)throws BusException{
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null)
			throw new BusException("查询调价记录时未获到任何参数");
		String pk_pdrep = CommonUtils.getString(paramMap.get("pkPdrep"));
		Map<String,Object> priceDt = (Map<String, Object>) paramMap.get("priceDt");
		List<PdRepriceHistVo> list  = null;
		//药品主键不为空，查询单条药品调价记录
		if(CommonUtils.isEmptyString(pk_pdrep)&&priceDt!=null&&!CommonUtils.isEmptyString(CommonUtils.getString(priceDt.get("pkPd")))){
				list = pdRepriceMapper.generateHistByPd(priceDt);
		}
		//调价单主键不为空，查询单条药品调价记录
		if((priceDt==null||CommonUtils.isEmptyString(CommonUtils.getString(priceDt.get("pkPd"))))&&!CommonUtils.isEmptyString(pk_pdrep)){
			if(Application.isSqlServer()){
				list = pdRepriceMapper.generateHistSqlServer(pk_pdrep);
			}else{
				list = pdRepriceMapper.generateHistOracle(pk_pdrep);
			}
		}

		return list;
	}
	/**
	 * 审核调价单-确定审核
	 * @param param
	 * @param user
	 */
	public  void submitReprice(String param,IUser user){
		PdRepriceSubVo subVo=JsonUtil.readValue(param, PdRepriceSubVo.class);
		if(subVo==null)throw new BusException("为获取到调价数据");
		//获取提交的调价记录
		List<PdRepriceHistVo> list =  subVo.getHistList();
		
		String eu_repmode = subVo.getEuRepmode();
		String pk_pdrep = subVo.getPkPdrep();
		User u = (User)user;
		//更新调价单状态
		pdRepricePubService.updateRepriceApp(eu_repmode,pk_pdrep,u);
		if("0".equals(eu_repmode)){//更新仓库物品价格
			pdRepricePubService.execReprice(list,pk_pdrep);
		}
		//保存历史调价记录
		saveRepriceHist(list);
	}
	
	/**
	 * 保存调价记录
	 */
	public void saveRepriceHist(List<PdRepriceHistVo> list){
		if(list == null ||list.size() <=0) return;
		List<PdRepriceHist> insertList = new ArrayList<PdRepriceHist>();
		for(PdRepriceHistVo vo :list){
			PdRepriceHist htvo = new PdRepriceHist();
			ApplicationUtils.copyProperties(htvo, vo);
			ApplicationUtils.setBeanComProperty(htvo, true);
			htvo.setQuanRep(MathUtils.mul(htvo.getQuanRep(),CommonUtils.getDouble(htvo.getPackSize())));
			htvo.setPkPdrephist(NHISUUID.getKeyId());
			htvo.setPackSize(vo.getPackSizeCvt());
			htvo.setPkUnitPack(vo.getPkUnitPackCvt());
			Double price=MathUtils.mul(MathUtils.div(htvo.getPrice(), (double)vo.getPackSizePd(),4),(double)vo.getPackSizeCvt());
			Double priceOrg=MathUtils.mul(MathUtils.div(htvo.getPriceOrg(), (double)vo.getPackSizePd(),4),(double)vo.getPackSizeCvt());
			htvo.setPrice(price);
			htvo.setPriceOrg(priceOrg);
			insertList.add(htvo);
		}
	   if(insertList!=null&&insertList.size()>0){
		   DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdRepriceHist.class), insertList);
	   }
	}
	/**
	 * 查询调价历史纪录
	 * @param param{codeRep,flagAdd,flagSub}
	 * @param user
	 * @return
	 */
	public List<PdRepriceHistVo> queryHist(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null ) throw new BusException("未获取到查询历史纪录的查询条件！");
		return pdRepriceMapper.queryRepriceHistList(map);
	}
}
