package com.zebone.nhis.scm.material.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdRepriceHist;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.material.dao.MtlPdRepriceMapper;
import com.zebone.nhis.scm.pub.service.MtlPdRepricePubService;
import com.zebone.nhis.scm.pub.vo.MtlPdRepriceDtVo;
import com.zebone.nhis.scm.pub.vo.MtlPdRepriceHistVo;
import com.zebone.nhis.scm.pub.vo.MtlPdRepriceVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 调价处理
 * @author wj
 *
 */
@Service
public class MtlPdRepriceService {
	
    @Resource
	private MtlPdRepriceMapper mtlPdRepriceMapper;
    @Resource
    private MtlPdRepricePubService mtlPdRepricePubService;
    
	/**
	 * 查询调价单{pkOrg,dtReptype,codeRep,dateBeginE,dateEndE,dateBeginC,dateEndC,euStatus}
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MtlPdRepriceVo> queryRepriceList(String param,IUser user){
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
		return mtlPdRepriceMapper.queryRepriceList(map);
	}
	
	/**
	 * 查询调价明细
	 * @param param--pkPdrep
	 * @param user
	 * @return
	 */
	public List<MtlPdRepriceDtVo> queryRepriceDtList(String param,IUser user){
		String pkPdrep = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pkPdrep))
			throw new BusException("未获取到调价单主键");
		return mtlPdRepriceMapper.queryRepriceDtList(pkPdrep);
	}
	
	/**
	 * 保存调价单
	 * @param param{PdRepriceVo}
	 * @param user
	 */
	public MtlPdRepriceVo saveReprice(String param,IUser user){
		MtlPdRepriceVo vo = JsonUtil.readValue(param, MtlPdRepriceVo.class);
		if(vo == null)
			throw new BusException("未获取到调价单数据");
		mtlPdRepricePubService.saveReprice(param, (User)user, vo);
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
		String sql = "delete from pd_reprice_detail  where pd_reprice_detail.pk_pdrep = ? and "+
        " exists (select 1  from pd_reprice   where pd_reprice_detail.pk_pdrep=pd_reprice.pk_pdrep and pd_reprice.eu_status='0')";
		DataBaseHelper.execute(sql, new Object[]{pk_pdrep});
		String sql_d = "delete from pd_reprice  where pk_pdrep = ? and eu_status='0' ";
		DataBaseHelper.execute(sql_d, new Object[]{pk_pdrep});
	}
	
	/**
	 * 审核调价单--生成调价记录查询
	 * @param param
	 * @param user
	 */
	public List<MtlPdRepriceHistVo> generateReprice(String param,IUser user)throws BusException{
		String pk_pdrep = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pk_pdrep))
			throw new BusException("未获取到调价单主键");
		List<MtlPdRepriceHistVo> list  = null;
        if(Application.isSqlServer()){
        	list = mtlPdRepriceMapper.generateHistSqlServer(pk_pdrep);
        }else{
        	list = mtlPdRepriceMapper.generateHistOracle(pk_pdrep);
        }
		return list;
	}
	
	/**
	 * 审核调价单-确定审核
	 * @param param
	 * @param user
	 */
	public  void submitReprice(String param,IUser user){
		//获取提交的调价记录
		List<MtlPdRepriceHistVo> list =  JsonUtil.readValue(param,new TypeReference<List<MtlPdRepriceHistVo>>(){});
		if(list==null||list.size()<=0) 
			throw new BusException("未获取到调价明细记录！");
		String eu_repmode = list.get(0).getEuRepmode();
		String pk_pdrep = list.get(0).getPkPdrep();
		//保存调价记录
		saveRepriceHist(list);
		User u = (User)user;
		//更新调价单状态
		mtlPdRepricePubService.updateRepriceApp(eu_repmode,pk_pdrep,u);
		if("0".equals(eu_repmode)){//更新仓库物品价格
			mtlPdRepricePubService.execReprice(list);
		}
	}
	
	/**
	 * 保存调价记录
	 */
	public void saveRepriceHist(List<MtlPdRepriceHistVo> list){
		if(list == null ||list.size() <=0) return;
		List<PdRepriceHist> insert_list = new ArrayList<PdRepriceHist>();
		for(MtlPdRepriceHistVo vo :list){
			PdRepriceHist htvo = new PdRepriceHist();
			ApplicationUtils.copyProperties(htvo, vo);
			ApplicationUtils.setBeanComProperty(htvo, true);
			htvo.setQuanRep(MathUtils.mul(htvo.getQuanRep(),CommonUtils.getDouble(htvo.getPackSize())));
			htvo.setPkPdrephist(NHISUUID.getKeyId());
			insert_list.add(htvo);
		}
	   if(insert_list!=null&&insert_list.size()>0){
		   DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PdRepriceHist.class), insert_list);
	   }
	}
	
	/**
	 * 查询调价历史纪录
	 * @param param{codeRep,flagAdd,flagSub}
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MtlPdRepriceHistVo> queryHist(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(map == null ) throw new BusException("未获取到查询历史纪录的查询条件！");
		return mtlPdRepriceMapper.queryRepriceHistList(map);
	}
}
