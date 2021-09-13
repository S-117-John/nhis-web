package com.zebone.nhis.scm.opds.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.scm.pub.dao.SchOpdsPreMapper;
import com.zebone.nhis.scm.pub.service.PdStOutBatchPubService;
import com.zebone.nhis.scm.pub.service.PdStOutPubService;
import com.zebone.nhis.scm.pub.vo.ExPresOccDtVO;
import com.zebone.nhis.scm.pub.vo.PdOutParamVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 发药无事物接口（为了解决发药时还原收费时锁定的预留量问题：在同一个事物中两次更新同一条记录会造成死锁）
 * @author yangxue
 *
 */
@Service
public class ScmOpDispenseHandler {
	@Autowired
	private SchOpdsPreMapper schOpdsPreMapper;
	@Autowired
	private PdStOutBatchPubService pdStOutBatchPubService;
	@Autowired
	private PdStOutPubService pdStOutPubService;
	@Resource
	private ScmOpDispenseService scmOpDispenseService;
	/**
	 * 发药发药科室pk_dept
	发药仓库pk_store
	发药窗口号winno
	待发药处方信息
	 * @param param
	 * @param user
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void dispensing(String param, IUser user) throws Exception {
		//1.参数获取
		Map<String,Object> paraMap = JsonUtil.readValue(param, Map.class);
		String pkPresocc = (String)paraMap.get("pkPresocc");//处方执行主键
		String pkDept = (String)paraMap.get("pkDept");//当前科室
		String winno = (String)paraMap.get("winno");//当前窗口
		String pkStore = (String)paraMap.get("pkStore");//当前仓库
		
		//1.发药-供应链出库参数转换,合并物品
		List<PdOutParamVo> pdOutParams = new ArrayList<PdOutParamVo>(16);
		List<ExPresOccDtVO> presDts = schOpdsPreMapper.QryPresDt(pkPresocc);
		Map<String,ExPresOccDt> flagPresDts = new HashMap<String,ExPresOccDt>();
		List<PdOutParamVo> param4Out = new  ArrayList<PdOutParamVo>(16);
		Map<String,PdOutParamVo> res4Out = new HashMap<String,PdOutParamVo>();
		Map<String,List<PdOutParamVo>> Out4Upd = new HashMap<String,List<PdOutParamVo>>();
		for(ExPresOccDtVO vo : presDts){
			//1.1参数转换
			PdOutParamVo pdOutParam = new PdOutParamVo();
			pdOutParam.setPackSize(vo.getPackSize());
			pdOutParam.setPkCnOrd(vo.getPkCnord());
			pdOutParam.setPkPd(vo.getPkPd());
			pdOutParam.setPkPdapdt(vo.getPkPresoccdt());
			pdOutParam.setPkPv(vo.getPkPv());
			pdOutParam.setPkUnitPack(vo.getPkUnit());
			if(vo.getOrdsCg() == 0.0)vo.setOrdsCg(1.0);
			Double tempMin = MathUtils.mul(vo.getQuanCg(), vo.getPackSize()*1.0);
			//tempMin = MathUtils.mul(tempMin, vo.getOrds());
			pdOutParam.setQuanMin(tempMin);
			pdOutParam.setQuanPack(vo.getQuanCg());
			pdOutParams.add(pdOutParam);	
			//将收费时锁定的预留数量还原
			//1.2.加入标志哈希表
			ExPresOccDt occDt = new ExPresOccDt();
			ApplicationUtils.copyProperties(occDt, vo);
			flagPresDts.put(vo.getPkPresoccdt(), occDt);
			
			//1.3 将同一个药品进行合并
			
			String pkPd = pdOutParam.getPkPd();
			List<PdOutParamVo> voListTemp = Out4Upd.get(pkPd)!=null && Out4Upd.get(pkPd).size()>0?Out4Upd.get(pkPd):new ArrayList<PdOutParamVo>();
			Out4Upd.put(pkPd, voListTemp);
			if(res4Out.get(pkPd)!=null){
				PdOutParamVo voTemp = res4Out.get(pkPd);
				voTemp.setQuanMin(voTemp.getQuanMin()+pdOutParam.getQuanMin());
				voTemp.setQuanPack(voTemp.getQuanPack()+pdOutParam.getQuanPack());
				res4Out.put(pkPd, voTemp);
			}else{
					res4Out.put(pkPd, (PdOutParamVo)pdOutParam.clone());
			}
		}
		//2.将同一个药品进行合并 (建议1-2合并为同一个循环处理)

//		for(PdOutParamVo vo : pdOutParams){
//			String pkPd = vo.getPkPd();
//			List<PdOutParamVo> voListTemp = Out4Upd.get(pkPd)!=null && Out4Upd.get(pkPd).size()>0?Out4Upd.get(pkPd):new ArrayList<PdOutParamVo>();
//			Out4Upd.put(pkPd, voListTemp);
//			if(res4Out.get(pkPd)!=null){
//				PdOutParamVo voTemp = res4Out.get(pkPd);
//				voTemp.setQuanMin(voTemp.getQuanMin()+vo.getQuanMin());
//				voTemp.setQuanPack(voTemp.getQuanPack()+vo.getQuanPack());
//				res4Out.put(pkPd, voTemp);
//			}else{
//					res4Out.put(pkPd, vo);
//			}
//		}
		param4Out.addAll(res4Out.values());
		pdStOutPubService.setPdUnPrepNum(pdOutParams,pkStore,pkDept,"1");
		try{
			//门诊发药剩余业务处理方法，封装到一个方法中为了保证其业务在同一个事物内完成
			ExPresOcc presOcc = schOpdsPreMapper.QryPresByPk(pkPresocc);
			scmOpDispenseService.dispend(presOcc.getPkOrgPres(),presOcc.getPkDeptPres(),pkDept, pkStore, pkPresocc, flagPresDts, winno, pdStOutBatchPubService,Out4Upd, param4Out);
		}catch(Exception e){
			//发药失败时，回滚已经解除的预留量
			pdStOutPubService.setPdPrepNum(param4Out,pkStore,pkDept,"1");
			e.printStackTrace();
			throw new BusException(e.getMessage());
		}
		
	}
}
