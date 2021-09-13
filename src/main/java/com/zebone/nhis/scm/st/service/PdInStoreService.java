package com.zebone.nhis.scm.st.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.scm.pub.service.PdStPubService;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.nhis.scm.st.dao.PdInStoreMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 入库处理
 * @author yangxue
 *
 */
@Service
public class PdInStoreService {
    @Resource
	private  PdInStoreMapper pdInStoreMapper;
    @Resource
	private PdStPubService pdStPubService;
    /**
     * 查询待入库列表
     * @param{codeSt,pkOrg,dateBegin,dateEnd,pkDeptSt,pkStoreSt}
     * @return
     */
    public List<PdStVo> queryToInList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
		map.put("pkStore", ((User)user).getPkStore());
		return pdInStoreMapper.queryToInPdStList(map);
    }
	/**
	 * 查询入库明细(含待入库记录)
	 * @param param{pkPdst,isToInStore}
	 * @param user
	 * @return
	 */
    public List<PdStDtVo> queryPdStDetail(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	//如果是待入库记录查询，需要转换数量，单位，价格为当前仓库相对应的内容
        if(map!=null&&CommonUtils.isNotNull(map.get("isToInStore"))&&"1".equals(map.get("isToInStore").toString())){
        	map.put("pkStore", ((User)user).getPkStore());
        	return pdInStoreMapper.queryToInPdStDtList(map);
        }else{
        	return pdStPubService.queryPdStDtList(param, user);
        }
    	
    }
    
    /**
     * 查询入库列表
     * @param{codeSt,dateBegin,dateEnd,pkDeptLk,pkStoreLk,pkOrgLk,dtSttype,pkEmpOp,euStatus}
     * @return
     */
    public List<PdStVo> queryInList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(CommonUtils.isNotNull(map.get("dateEnd"))){
			map.put("dateEnd", CommonUtils.getString(map.get("dateEnd")).substring(0, 8)+"235959");
		}
		map.put("pkStore", ((User)user).getPkStore());
		return pdInStoreMapper.queryInPdStList(map);
    }
    
    /**
	 * 保存入库信息
	 * @param param{PdStVo}
	 * @param user
	 */
	public PdStVo savePdSt(String param,IUser user){
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		if(CommonUtils.isNotNull(stvo.getDtlist())) {
			stvo.getDtlist().stream().forEach(m ->
			{
				Double price=MathUtils.mul(MathUtils.div(m.getPrice(), CommonUtils.getDouble(m.getPackSize())), CommonUtils.getDouble(m.getPackSizePd()));
				Double priceCost=MathUtils.mul(MathUtils.div(m.getPriceCost(), CommonUtils.getDouble(m.getPackSize())), CommonUtils.getDouble(m.getPackSizePd()));

				m.setPrice(new BigDecimal(price).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
				m.setPriceCost(new BigDecimal(priceCost).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			});
		}else{
			throw new BusException("未传入明细记录！");
		}
		return pdStPubService.savePdSt(stvo,user,"");
	}
	/**
	 * 删除入库信息
	 * @param param{pkPdst}
	 * @param user
	 */
	public void deletePdSt(String param,IUser user){
		pdStPubService.deletePdst(param, user);
	}
    /**
     * 审核入库单
     * @param param
     * @param user
     * @throws CloneNotSupportedException 
     */
	public void submitPdSt(String param,IUser user) {
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		User u =(User)user;
		if(stvo == null ) return;
		String pk_pdst = stvo.getPkPdst();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkPdst", pk_pdst);
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
		paramMap.put("dateChk", new Date());
		List<PdStDtVo> dtlist = stvo.getDtlist();
		if(!CommonUtils.isEmptyString(stvo.getPkPdstSr())) {//如果是待入库进来的，需要保存
			dtlist = new ArrayList<PdStDtVo>();
			for (PdStDtVo dt : stvo.getDtlist()) {
				dtlist.add(dt.clone());
			}
			stvo.getDtlist().stream().forEach(m ->
			{
				Double price = MathUtils.mul(MathUtils.div(m.getPrice(), CommonUtils.getDouble(m.getPackSize())), CommonUtils.getDouble(m.getPackSizePd()));
				Double priceCost = MathUtils.mul(MathUtils.div(m.getPriceCost(), CommonUtils.getDouble(m.getPackSize())), CommonUtils.getDouble(m.getPackSizePd()));

				m.setPrice(new BigDecimal(price).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
				m.setPriceCost(new BigDecimal(priceCost).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			});
			pdStPubService.savePdSt(stvo, user, "");
		}else{
			//更新入库单
			pdStPubService.updatePdSt(paramMap);
		}
		//更新库存
		pdStPubService.updateInStore(dtlist,u.getPkStore(),u.getPkDept());
	}
	/**
	 * 入库退回
	 * @param param
	 * @param user
	 */
	public void rtnPdSt(String param,IUser user){
		pdStPubService.rtnPdst(param, user, IDictCodeConst.DT_STTYPE_INRTN,"1");
	}
	
}
