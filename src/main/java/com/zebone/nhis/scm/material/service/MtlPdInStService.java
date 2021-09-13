package com.zebone.nhis.scm.material.service;

import java.util.*;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.*;
import com.zebone.platform.common.support.NHISUUID;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.zebone.nhis.common.module.scm.st.PdDeptusing;
import com.zebone.nhis.scm.material.dao.MtlPdStMapper;
import com.zebone.nhis.scm.pub.service.MtlPdStPubService;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 入库处理
 * 
 * @author wj
 * 
 */
@Service
public class MtlPdInStService {

	@Resource
	private MtlPdStMapper pdStMapper;
	@Autowired
	private MtlPdStPubService mtlPdStPubService;
	private static Logger log = org.slf4j.LoggerFactory.getLogger(MtlPdInStService.class);

	/**
	 * 查询待入库列表
	 * 
	 * @param{codeSt,pkOrg,dateBegin,dateEnd,pkDeptSt,pkStoreSt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryToInList(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		map.put("pkStore", ((User) user).getPkStore());
		List<Map<String, Object>> list = pdStMapper.queryToInPdStList(map);
		return list;
	}

	/**
	 * 查询入库明细
	 * 
	 * @param param
	 *            {pkPdst}
	 * @param user
	 * @return
	 */
	public List<PdStDtVo> queryPdStDetail(String param, IUser user) {
		return mtlPdStPubService.queryPdStDtList(param, user);
	}

	/**
	 * 查询入库列表
	 * 
	 * @param{codeSt,dateBegin,dateEnd,pkDeptLk,pkStoreLk,pkOrgLk,dtSttype,pkEmpOp,euStatus
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInList(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		map.put("pkStoreSt", ((User) user).getPkStore());
		map.put("flagPu", "0");
		map.put("euDirect", "1");
		List<Map<String, Object>> list = pdStMapper.queryPdStByCon(map);
		return list;
	}

	/**
	 * 保存入库信息
	 * 
	 * @param param
	 *            {PdStVo}
	 * @param user
	 */
	public PdStVo savePdSt(String param, IUser user) {
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		if(CommonUtils.isEmptyString(stvo.getPkPdst()))
		{
			//查询一下通业务类型下单号是否重复
			int isCodeDup = DataBaseHelper.queryForScalar("select count(1) from pd_st where dt_sttype=? and  code_st=?",Integer.class,new Object[]{stvo.getDtSttype(),stvo.getCodeSt()});
			if(isCodeDup != 0)
			{
				stvo.setCodeSt(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_RKD));
			}
		}
		return mtlPdStPubService.savePdSt(stvo, user, stvo.getDtSttype(), EnumerateParameter.ONE);

	}

	/**
	 * 删除入库信息
	 * 
	 * @param param
	 *            {pkPdst}
	 * @param user
	 */
	public void deletePdSt(String param, IUser user) {
		mtlPdStPubService.deletePdst(param, user);
	}

	/**
	 * 审核入库单
	 * 
	 * @param param
	 * @param user
	 */
	public void submitPdSt(String param, IUser user) {
		PdStVo stvo = JsonUtil.readValue(param, PdStVo.class);
		User u = (User) user;
		if (stvo == null)
			return;
		String pk_pdst = stvo.getPkPdst();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkPdst", pk_pdst);
		paramMap.put("pkEmp", u.getPkEmp());
		paramMap.put("nameEmp", u.getNameEmp());
		paramMap.put("dateChk", new Date());
		paramMap.put("euStatus", EnumerateParameter.ONE);
		if (!CommonUtils.isEmptyString(stvo.getPkPdstSr())) {// 如果是待入库进来的，需要保存
			//解决一个单据多次入库问题
			Map<String, Object> pkMap = DataBaseHelper.queryForMap("SELECT t.pk_pdst_sr from pd_st t where t.pk_pdst_sr=?", stvo.getPkPdstSr());
			if(null != pkMap && pkMap.size() > 0){
				log.debug("=======该单据已经审核入库=========");
				throw new BusException("该单据已经审核入库，请确认！");
			}
			List<PdStDtVo> stDtVoList = stvo.getDtlist();
			for(int i=0;i<stDtVoList.size();i++){
				PdStDtVo dt = stDtVoList.get(i);
				if(DataBaseHelper.queryForScalar("select count(*) from BD_PD_STORE where PK_STORE=? and PK_PD=?",Integer.class,new Object[]{stvo.getPkStoreSt(),dt.getPkPd()}) ==0){
					throw new BusException("明细第"+(i+1)+"行，没有维护仓库物品信息！");
				}
			}
			PdStVo stVo = mtlPdStPubService.savePdSt(stvo, user, stvo.getDtSttype(), EnumerateParameter.ONE);
			paramMap.put("pkPdst", stVo.getPkPdst());
		}
		mtlPdStPubService.updatePdSt(paramMap);
		//更新单品状态
		mtlPdStPubService.updatePdSingleStatus(EnumerateParameter.ONE,pk_pdst,EnumerateParameter.ONE,true);
		//界面是当前仓库，数据库是零售，这里转为零售去库里查找---待入库过来的调用保存时已经处理
		if(CommonUtils.isEmptyString(stvo.getPkPdstSr()) && !CollectionUtils.isEmpty(stvo.getDtlist())){
			for (PdStDtVo dt : stvo.getDtlist()) {
				dt.setPrice(MathUtils.mul(MathUtils.div(dt.getPrice(), (double)dt.getPackSize()), (double)dt.getPackSizePd()));
				dt.setPriceCost(MathUtils.mul(MathUtils.div(dt.getPriceCost(), (double)dt.getPackSize()), (double)dt.getPackSizePd()));
			}
		}

		// 更新库存
		mtlPdStPubService.updateInStore(stvo.getDtlist(), u.getPkStore(),
				u.getPkDept());
		//业务类型为科室退库且物品具有再用属性时，处理科室物品入库统计
		updateCommonPdDt(stvo,user);
	}

	/**
	 * 入库退回
	 * 
	 * @param param
	 * @param user
	 */
	public void rtnPdSt(String param, IUser user) {
		mtlPdStPubService.rtnPdst(param, user, IDictCodeConst.DT_STTYPE_INRTN);
	}

	
	/*
	 * 业务类型为科室退库且物品具有再用属性
	 */
	private void updateCommonPdDt(PdStVo stvo,IUser user){
		if (!StringUtils.isEmpty(stvo.getDtSttype())
				&& stvo.getDtSttype().equals("0106")) // 科室退库
		{
			if (stvo.getDtlist() != null && stvo.getDtlist().size() > 0) {
				for (PdStDtVo pdStDtVo : stvo.getDtlist()) {
					
					if(CommonUtils.isEmptyString(pdStDtVo.getPkPd()))
					{
						throw new BusException(pdStDtVo.getPdname() + "不存在!");
						//continue;
					}
					if (!CommonUtils.isEmptyString(pdStDtVo.getFlagUse()) && pdStDtVo.getFlagUse().equals("1")) { //再用属性
						PdDeptusing pdDeptusing = DataBaseHelper.queryForBean("select * from pd_deptusing where pk_pd = ?", PdDeptusing.class,
								new Object[]{pdStDtVo.getPkPd()});
						if(pdDeptusing == null || CommonUtils.isEmptyString(pdDeptusing.getPkDeptusing()))
						{
							//新增科室物品统计	
							pdDeptusing = new PdDeptusing();
							pdDeptusing.setPkDeptusing(UUID.randomUUID().toString().replaceAll("-", ""));//主键生成
							BeanUtils.copyProperties(pdStDtVo,pdDeptusing); 
							//给pdDeptusing赋值
							pdDeptusing.setDelFlag("0");
							pdDeptusing.setPkOrgUse(stvo.getPkOrgLk()); //使用组织
							pdDeptusing.setPkDeptUse(stvo.getPkDeptLk()); //使用部门
							pdDeptusing.setPkPd(pdStDtVo.getPkPd()); //物品主键
							pdDeptusing.setQuan(pdStDtVo.getQuanPack()); //数量
							pdDeptusing.setPkPdstdtOut(pdStDtVo.getPkPdstdt()); //出库明细主键
							pdDeptusing.setDateBeign(new Date());
							DataBaseHelper.insertBean(pdDeptusing);
						}
						else
						{
							//计算剩余数量 ，计算剩余在用数量：在用数量-入库数量；
							Double Retnum = pdDeptusing.getQuan() - pdStDtVo.getQuanPack();	
							
							if(Retnum > 0)
							{
								//pdDeptusing
								pdDeptusing.setQuan(Retnum);
								pdDeptusing.setAmount(Retnum*(pdDeptusing.getPrice() == null ? 0 : pdDeptusing.getPrice()));
								pdDeptusing.setAmountCost(Retnum*(pdDeptusing.getPriceCost() == null ? 0 : pdDeptusing.getPriceCost()));
								DataBaseHelper.updateBeanByPk(pdDeptusing);
							}
							else if(Retnum == 0){									
								DataBaseHelper.deleteBeanByPk(pdDeptusing);
							}
							else{
								throw new BusException("科室退回数量超出在用数量，请修改!");
							}
						}
					} 
				}
			}
		}
		
	}
	
}
