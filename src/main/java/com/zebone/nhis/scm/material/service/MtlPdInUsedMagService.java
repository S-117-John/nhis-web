package com.zebone.nhis.scm.material.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.*;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.scm.st.PdDeptusing;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.nhis.common.module.scm.st.PdStDetail;
import com.zebone.nhis.scm.material.dao.MtlInUsedMapper;
import com.zebone.nhis.scm.material.vo.MtlPdDeptUsingVo;
import com.zebone.nhis.scm.pub.support.ScmPubUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 在用管理
 * @author wj
 */
@Service
public class MtlPdInUsedMagService {
	
    @Resource
	private MtlInUsedMapper mtlInUsedMapper;
	
    /**
	 * 在用查询
	 * @param param{pkStore，pkPd}
	 * @param user
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryPdUsedList(String param,IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param, Map.class);
    	if(null == map){
    		map = new HashMap<String,Object>();
    	}
    	map.put("pkStore", UserContext.getUser().getPkStore());
    	List<Map<String, Object>> list = mtlInUsedMapper.queryPdUsedList(map);
    	return list;
    }
    
    /**
     * 在用物品转移
     * @param param
     * @param user
     */
    public void transUsingPd(String param,IUser user){
    	MtlPdDeptUsingVo pdVo = JsonUtil.readValue(param, MtlPdDeptUsingVo.class);
    	if(pdVo == null)
    		throw new BusException("未获取到待转移的物品");
    	User u = (User)user;
    	
    	//1） 从在用科室将物品退回仓库，仓库做入库处理，业务类型为0106“科室退回”
    	this.insertSt(pdVo,u, IDictCodeConst.DT_STTYPE_DEPTRTN, "1");
    	
    	//2） 从仓库将物品出库至目标科室，仓库做出库处理，业务类型为0202“科室领用”
    	this.insertSt(pdVo,u, IDictCodeConst.DT_STTYPE_DETPOUT, "-1");
    		
    	//3）更新科室在用记录
    	//更新原在用科室数量和金额，如果转移后数量为0，删除原科室在用记录；
    	double quanRemain = MathUtils.sub(pdVo.getQuan(), pdVo.getQuanBack());
    	double quanAmountCost = MathUtils.mul(quanRemain , pdVo.getPriceCost());
    	double quanAmount = MathUtils.mul(quanRemain,pdVo.getPrice());
    	if(quanRemain == 0)
    		DataBaseHelper.execute("delete from  pd_deptusing  where pk_deptusing = ?", 
    				pdVo.getPkDeptusing());
    	else
    		DataBaseHelper.update("update pd_deptusing set quan = ? ,del_flag = '0',amount = ?,amount_cost = ? "
    				+ "where  pk_deptusing = ?",quanRemain,quanAmount,quanAmountCost,pdVo.getPkDeptusing());
    	
    	//写目标科室在用记录。
    	PdDeptusing pdDeptU = null;
    	List<PdDeptusing> list = DataBaseHelper.queryForList("select * from pd_deptusing "
    			+ "where pk_pd = ? and batch_no = ? and pk_dept_use = ? and del_flag = '0'",
    			PdDeptusing.class , new Object[]{pdVo.getPkPd(),pdVo.getBatchNo(),pdVo.getPkDeptNew()});
    	if(null == list || list.size() < 1){
    		pdDeptU = new PdDeptusing();
    		ApplicationUtils.copyProperties(pdDeptU, pdVo);
    		pdDeptU.setPkDeptusing(NHISUUID.getKeyId());
    		pdDeptU.setDelFlag("0");
    		pdDeptU.setPkOrg(u.getPkOrg());
    		pdDeptU.setPkDept(u.getPkDept());
    		pdDeptU.setPkStore(u.getPkStore());
    		pdDeptU.setPkOrgUse(u.getPkOrg());
    		pdDeptU.setPkDeptUse(pdVo.getPkDeptNew());
    		pdDeptU.setDateBeign(new Date());
    		pdDeptU.setQuan(pdVo.getQuanBack());
    		//TODO 此处包装量未做转换
    		pdDeptU.setAmount(pdVo.getPrice() * pdVo.getQuanBack());
    		pdDeptU.setAmountCost(pdVo.getPriceCost() * pdVo.getQuanBack());
    		DataBaseHelper.insertBean(pdDeptU);
    	}else{
    		pdDeptU = list.get(0);
    		double quanNew = pdDeptU.getQuan() + pdVo.getQuanBack();
    		pdDeptU.setQuan(quanNew);
    		pdDeptU.setAmount(pdVo.getPrice() * quanNew );
    		pdDeptU.setAmountCost(pdVo.getPriceCost() * quanNew);
    		DataBaseHelper.updateBeanByPk(pdDeptU,false);
    	}
    }
    
    /**
     * 在用物品报废
     * @param param
     * @param user
     */
    public void scrapUsingPd(String param,IUser user){
    	MtlPdDeptUsingVo pdVo = JsonUtil.readValue(param, MtlPdDeptUsingVo.class);
    	if(pdVo == null)
    		throw new BusException("未获取到待报废的物品");
    	User u = (User)user;
    	
    	//1） 从在用科室将物品退回仓库，仓库做入库处理，业务类型为0106“科室退回”
    	this.insertSt(pdVo,u, IDictCodeConst.DT_STTYPE_DEPTRTN, "1");

    	//2） 仓库做物品出库处理，业务类型为0207“报损出库”
    	this.insertSt(pdVo,u,IDictCodeConst.DT_STTYPE_DISOUT, "-1");
    	
    	//3）更新科室在用记录
    	//更新原在用科室数量和金额，如果报损后数量为0，删除原科室在用记录。
    	double quanRemain = MathUtils.sub(pdVo.getQuan(), pdVo.getQuanBack());
    	if(quanRemain == 0)
    		DataBaseHelper.execute("delete from  pd_deptusing where pk_deptusing = ?", 
    				pdVo.getPkDeptusing());
    	else
    		DataBaseHelper.update("update pd_deptusing set quan = ? ,del_flag = '0' "
    				+ "where  pk_deptusing = ?",quanRemain, pdVo.getPkDeptusing());
    }

	/**
	 * 插入出入库单及明细
	 * @param pd
	 * @param u
	 * @param dttype
	 * @param eudirect
	 */
    private void insertSt(MtlPdDeptUsingVo pd,User u,String dttype,String eudirect){
    	PdSt st = this.createPdst(u, dttype, eudirect,pd);
    	DataBaseHelper.insertBean(st);
    	PdStDetail dt = this.createPdstdt(pd, u, st.getPkPdst(),eudirect);
		DataBaseHelper.insertBean(dt);
    }

	/**
	 * 构建出入库单
	 * @param u
	 * @param dttype
	 * @param eudirect
	 * @return
	 */
	private PdSt createPdst(User u,String dttype,String eudirect,MtlPdDeptUsingVo pd){
		PdSt st = new PdSt();
		if("1".equals(eudirect)) {
			st.setCodeSt(ScmPubUtils.getInStoreCode());
			//入库对应的出库机构，出库部门（在用部门）
			st.setPkOrgLk(pd.getPkOrgUse());
			st.setPkDeptLk(pd.getPkDeptUse());
		}
		if("-1".equals(eudirect)){
			st.setCodeSt(ScmPubUtils.getOutStoreCode());
			//出库对应申请机构，申请部门对应出库部门
			st.setPkOrgLk(pd.getPkOrg());
			st.setPkDeptLk(pd.getPkDeptNew());
		}
		st.setEuStatus("1");
		st.setDateSt(new Date());
		st.setDelFlag("0");
		st.setDtSttype(dttype);
		st.setEuDirect(eudirect);
		st.setFlagChk("0");
		st.setFlagPay("0");
		st.setFlagPu("0");
		st.setNameEmpOp(u.getNameEmp());
		st.setNameSt(u.getNameEmp());
		st.setPkDeptSt(u.getPkDept());
		st.setPkEmpOp(u.getPkEmp());
		st.setPkOrg(u.getPkOrg());
		st.setPkStoreSt(u.getPkStore());
		st.setDateChk(new Date());
		st.setFlagChk(EnumerateParameter.ONE);
		st.setNameEmpChk(u.getNameEmp());
		st.setPkEmpChk(u.getPkEmp());
		return st;
	}
	
	/**
	 * 构建出入库明细
	 * @param dt
	 * @param u
	 * @return
	 */
	private PdStDetail createPdstdt(MtlPdDeptUsingVo dt,User u,String pk_pdst,String eudirect){
		PdStDetail pddt = new PdStDetail();
		pddt.setPkPdstdt(NHISUUID.getKeyId());
		pddt.setPkOrg(u.getPkOrg());
		pddt.setPkPdst(pk_pdst);
		pddt.setSortNo(1);
		pddt.setPkPd(dt.getPkPd());
		pddt.setPackSize(dt.getPackSize());
		pddt.setPkUnitPack(dt.getPkUnitPack());
		//pddt.setDateFac(dt.getDateFac());
		pddt.setBatchNo(dt.getBatchNo());
		pddt.setDateExpire(dt.getDateExpire());
		pddt.setPrice(dt.getPrice());
		pddt.setPriceCost(dt.getPriceCost());
		pddt.setDisc(1.00);
		pddt.setFlagChkRpt("0");
		pddt.setFlagFinish("1");
		pddt.setFlagPay("0");
		pddt.setQuanPack(MathUtils.abs(dt.getQuanBack()));
        pddt.setQuanMin(MathUtils.mul(pddt.getQuanPack(), CommonUtils.getDouble(dt.getPackSize())));
        pddt.setAmount(MathUtils.div(pddt.getQuanMin(), CommonUtils.getDouble(dt.getPackSize()))*pddt.getPrice());
		pddt.setAmountCost(MathUtils.div(pddt.getQuanMin(), CommonUtils.getDouble(dt.getPackSize()))*pddt.getPriceCost());
		pddt.setQuanOutstore(EnumerateParameter.ONE.equals(eudirect)?pddt.getQuanMin():0);
		pddt.setDelFlag("0");
		ApplicationUtils.setBeanComProperty(pddt, true);
		return pddt;
	}
	
}
