package com.zebone.nhis.ex.nis.ns.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.nis.ns.dao.DeptPdRtnMapper;
import com.zebone.nhis.ex.nis.ns.vo.PdRtnDtVo;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 退药申请
 * @author yangxue
 *
 */
@Service
public class DeptPdRtnService {
	
	@Resource
	private DeptPdRtnMapper deptPdRtnMapper;
	/**
	 * 查询可退药医嘱
	 * @param param{pkPv,name}
	 * @param user
	 * @return
	 */
	public List<PdRtnDtVo> queryCgListByPv(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String pk_dept_cur = ((User)user).getPkDept();
		map.put("pkDeptNs", pk_dept_cur);
		//获取允许长期医嘱使用记费退药--0：不允许，1：允许
		String flagEuAlways=ApplicationUtils.getSysparam("EX0056", false);
		if("1".equals(flagEuAlways)){
			//map.put("euAlways", "0");
		}else{
			map.put("euAlways", "1");
		}
		return deptPdRtnMapper.queryCgListByPv(map);
	}
	
	/**
	 * 查询医嘱对应执行记录
	 * @param param{pkCnords}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryExListByOrd(String param,IUser user){
		List<String> list = JsonUtil.readValue(param, ArrayList.class);
		if(list == null || list.size()<= 0) throw new BusException("未获取到医嘱主键！");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pkCnords", list);
		return deptPdRtnMapper.queryExListByOrd(map);
	}
	/**
	 * 生成退请领(支持多个医嘱)
	 * @param param{cglist}
	 * @param user
	 */
	public void generateRtnApply(String param,IUser user){
		List<PdRtnDtVo> cgslist = JsonUtil.readValue(param, new TypeReference<List<PdRtnDtVo>>(){});
		if(cgslist == null ||cgslist.size()<=0)
			throw new BusException("未获取到退药明细！");
		User u = (User)user;
		
		
		//生成请领单及请领明细，同一条医嘱的计费明细记录生成请领时不能合并
		List<ExPdApplyDetail> details = new ArrayList<ExPdApplyDetail>();
		StringBuffer msg = new StringBuffer();
		String pk_org_de = "";
		String pk_dept_de = "";
		ExPdApply app = null;
		for(PdRtnDtVo rtnvo:cgslist){
			if(CommonUtils.isEmptyString(rtnvo.getPkCnord())){
				throw new BusException("医嘱数据不全请联系管理员！");
			}
			if(CommonUtils.isEmptyString(rtnvo.getPkCgip())){
				throw new BusException("记费数据不全请联系管理员！");
			}
			if(rtnvo.getRtnum()<=0){
				throw new BusException("退药数量数据为空，或者不大于0！");
			}
			
			if(!rtnvo.getPkOrgDe().equals(pk_org_de)&&!rtnvo.getPkDeptDe().equals(pk_dept_de)){
				 app = createAppVo(u,rtnvo);
				 DataBaseHelper.insertBean(app);
				 pk_org_de = rtnvo.getPkOrgDe();
				 pk_dept_de = rtnvo.getPkDeptDe();
			}
			if("1".equals(rtnvo.getFlagSelf())){
				msg.append("医嘱："+rtnvo.getNameCg()+"是自备药，不能退药！");
				continue;
			}
			if("1".equals(rtnvo.getFlagBase())){
				msg.append("医嘱："+rtnvo.getNameCg()+"是基数药，不能在此退药！");
				continue;
			}
			ExPdApplyDetail dtvo = this.createAppDtVo(rtnvo, u, app);
			details.add(dtvo);
			List<String> exlist = rtnvo.getExlistPks();
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("dateCanc", new Date());
			paramMap.put("pkocc", exlist);
			paramMap.put("pkEmp", u.getPkEmp());
			paramMap.put("nameEmp", u.getNameEmp());
			paramMap.put("pkDept", u.getPkDept());
			paramMap.put("pkCnord", rtnvo.getPkCnord());
			paramMap.put("pkBackDt", dtvo.getPkPdapdt());
			
			if(exlist != null && exlist.size() > 0){
				//更新执行单为作废状态
				StringBuilder sql = new StringBuilder("update ex_order_occ set flag_canc='1',eu_status='9',pk_dept_canc=:pkDept,");
				sql.append(" pk_emp_canc=:pkEmp,name_emp_canc=:nameEmp,date_canc = :dateCanc ,pk_pdback=:pkBackDt ");
				sql.append(" where pk_exocc in (:pkocc) and pk_cnord = :pkCnord");
				DataBaseHelper.update(sql.toString(), paramMap);
			}
			
			//如果处方，更新该处方的所有执行记录
			if(!CommonUtils.isEmptyString(rtnvo.getPkPres())){
				//更新执行单为作废状态
				StringBuilder sql = new StringBuilder("update ex_order_occ set ex_order_occ.flag_canc='1',ex_order_occ.eu_status='9',ex_order_occ.pk_dept_canc=:pkDept,");
				sql.append(" ex_order_occ.pk_emp_canc=:pkEmp,ex_order_occ.name_emp_canc=:nameEmp,ex_order_occ.date_canc = :dateCanc ,ex_order_occ.pk_pdback=:pkBackDt ");
				sql.append(" where exists (select 1 from cn_order where cn_order.pk_cnord = ex_order_occ.pk_cnord and ");
				sql.append("(cn_order.pk_pres is not null or cn_order.pk_pres !='')) and ex_order_occ.pk_cnord = :pkCnord ");
			    if(exlist != null && exlist.size() > 0){
			    	sql.append(" and ex_order_occ.pk_exocc not in (:pkocc) ");
			    }
				DataBaseHelper.update(sql.toString(), paramMap);
			}
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdApplyDetail.class), details);
	}
	
	/**
	 * 创建请领单
	 * @param u
	 * @return
	 */
	private ExPdApply createAppVo(User u,PdRtnDtVo rtnvo){
		ExPdApply app = new ExPdApply();
		app.setPkPdap(NHISUUID.getKeyId());
		app.setDateAp(new Date());
		app.setEuAptype("0");
		app.setEuDirect("-1");
		app.setEuStatus("0");
		app.setFlagCancel("0");
		app.setFlagFinish("0");
		app.setNameEmpAp(u.getNameEmp());
		app.setPkEmpAp(u.getPkEmp());
		app.setPkDeptAp(u.getPkDept());
		app.setCodeApply(ExSysParamUtil.getAppCode());//调用编码规则
		app.setPkOrg(u.getPkOrg());
		app.setCreateTime(new Date());
		app.setCreator(u.getPkEmp());
		app.setPkDeptDe(rtnvo.getPkDeptDe());
		app.setPkOrgDe(rtnvo.getPkOrgDe());
		app.setDelFlag("0");
		return app;
	}
	/**
	 * 创建请领明细
	 * @param rtnvo
	 * @param u
	 * @param pk_app
	 * @return
	 */
	private ExPdApplyDetail createAppDtVo(PdRtnDtVo rtnvo,User u,ExPdApply app){
		ExPdApplyDetail dt = new ExPdApplyDetail();	
		dt.setBatchNo(rtnvo.getBatchNo());
		dt.setEuDetype("0");
		dt.setEuDirect("-1");
		dt.setFlagBase(rtnvo.getFlagBase());
		dt.setFlagMedout(rtnvo.getFlagMedout());
		dt.setFlagSelf(rtnvo.getFlagSelf());
		dt.setOrds(rtnvo.getOrds());
		dt.setPackSize(rtnvo.getPackSize());
		dt.setPkCgip(rtnvo.getPkCgip());
		dt.setPkCnord(rtnvo.getPkCnord());
		dt.setPkPd(rtnvo.getPkPd());
		dt.setPkPdap(app.getPkPdap());
		dt.setPkPdapdt(NHISUUID.getKeyId());
		dt.setPkOrg(u.getPkOrg());
		dt.setCreateTime(new Date());
		dt.setCreator(u.getPkEmp());
		dt.setDelFlag("0");
		dt.setFlagDe("0");
		dt.setFlagFinish("0");
		dt.setFlagStop("0");
		dt.setFlagCanc("0");
		dt.setPkPres(rtnvo.getPkPres());
		dt.setPkPv(rtnvo.getPkPv());
		dt.setPkUnit(rtnvo.getPkUnit());
		dt.setPkCnord(rtnvo.getPkCnord());
		dt.setPrice(rtnvo.getPrice());
		dt.setPriceCost(rtnvo.getPriceCost());
		dt.setDateExpire(rtnvo.getDateExpire());
		dt.setBatchNo(rtnvo.getBatchNo());
		double quanPack = rtnvo.getRtnum();
		dt.setQuanPack(quanPack);
		dt.setQuanMin(MathUtils.mul(quanPack,CommonUtils.getDouble(rtnvo.getPackSize())));
		dt.setAmount(MathUtils.mul(rtnvo.getPrice(),quanPack));
		dt.setTs(new Date());
		return dt;
	}
   
}
