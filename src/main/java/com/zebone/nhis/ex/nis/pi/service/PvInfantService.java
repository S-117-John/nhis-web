package com.zebone.nhis.ex.nis.pi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pv.PvInfant;
import com.zebone.nhis.common.module.pv.PvInfantGrade;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.pi.dao.PvInfantMapper;
import com.zebone.nhis.ex.nis.pi.vo.PvInfantGradeVo;
import com.zebone.nhis.ex.pub.service.PatiDeptInAndOutPubService;
import com.zebone.nhis.ex.pub.service.PvInfantPubService;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.PvInfantVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 婴儿信息业务处理类
 * @author yangxue
 *
 */
@Service
public class PvInfantService {
	@Resource
	private PatiDeptInAndOutPubService deptInAndOutPubService;
	   
	@Autowired   
	private PvInfantMapper pvInfantMapper;
	
	@Resource
	private PvInfantPubService pvInfantPubService;
    /**
     * 根据患者pkpv查询分娩记录
     * @param param{pvPv}
     * @param user
     * @return
     */
	public List<PvInfantVo> queryRecInfantByPv(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkPv = (String)paramMap.get("pkPv");
		if(CommonUtils.isEmptyString(pkPv))
			throw new BusException("未获取到母亲就诊主键");
		return pvInfantMapper.queryRecList(pkPv);
	}
	/**
	 * 根据婴儿主键或就诊主键获取分娩信息
	 * @param {pkInfant,pkPv,pkLaborrecdt,sortNo}
	 * @return
	 */
	public List<PvInfantVo> queryInfantListByRec(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		return pvInfantMapper.queryInfantListByRec(paramMap);
	}
	/**
	 * 根据婴儿主键或就诊主键获取婴儿信息
	 * @param {pkInfant,pkPv,pkLaborrecdt,sortNo}
	 * @return
	 */
	public List<PvInfantVo> queryInfantListByInfant(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<PvInfantVo> resList=pvInfantMapper.queryInfantListByInfant(paramMap);
		return resList;
	}
	
	/**
	 * 根据婴儿主键删除婴儿信息
	 * @param param--{pkInfant,pkPv,pk_pv_infant,pk_pi_infant}
	 * @param user
	 */
	public void deleteInfantByPk(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pk_infant = CommonUtils.getString(paramMap.get("pkInfant"));
		String pk_pv = CommonUtils.getString(paramMap.get("pkPv"));
		String pk_pv_infant = CommonUtils.getString(paramMap.get("pkPvInfant"));
		String pk_pi_infant = CommonUtils.getString(paramMap.get("pkPiInfant"));
		
		//发消息给平台  adtType=新出生   flag_infant
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select inf.code,pv.code_pv,inf.pk_pv,inf.pk_pv_infant pk_pv_bb from pv_infant inf left join pv_encounter pv on inf.pk_pv_infant=pv.pk_pv where pk_infant='"+pk_infant+"'";
		Map<String, Object> queryForMap = DataBaseHelper.queryForMap(sql);
		if(null!=queryForMap && queryForMap.size()>0){
			map.putAll(queryForMap);
		}
		map.put("adtType", "新出生");
		map.put("flagInf", "1");
		map.put("STATUS", "_DELETE");
		PlatFormSendUtils.sendCancelDeptInMsg(map);
		
		pvInfantPubService.deleteInfantByPk(pk_infant, pk_pv, pk_pv_infant,pk_pi_infant);
//		pvInfantPubService.deleteInfAndRecByPk(pk_infant, pk_pv, pk_pv_infant,
//		pk_pi_infant,CommonUtils.getString(paramMap.get("pkLaborRecDt")));//同时删除婴儿+产程
	}
	/**
	 * 更新婴儿信息
	 * @param para--vo
	 * @param user
	 */
	public void updatePvInfant(String param, IUser user){
		PvInfantVo vo = JsonUtil.readValue(param, PvInfantVo.class);
		pvInfantPubService.updateInfant(vo,(User)user);
		
		//发消息给平台  adtType=新出生
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("adtType", "新出生");
		map.put("codeIp", vo.getCode());
		map.put("STATUS", "_UPDATE");
		PlatFormSendUtils.sendPvInfoMsg(map); 
	}
	
	/**
	 * 保存婴儿信息
	 * @param param--vo
	 * @param user
	 * @return
	 */
	public PvInfant savePvInfant(String param, IUser user){
		PvInfantVo vo = JsonUtil.readValue(param, PvInfantVo.class);
		vo = pvInfantPubService.saveInfant(vo, (User)user);
		
		//发消息给平台  adtType=新出生
		//Map<String, Object> paramMap = JsonUtil.readValue(ApplicationUtils.beanToJson(vo), Map.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("adtType", "新出生");
		map.put("codeIp", vo.getCode());
		map.put("STATUS", "_ADD");
		map.put("pkPvBb", vo.getPkPvInfant());//婴儿虚拟就诊主键
		map.put("pkPv", vo.getPkPv());//母亲就诊主键
		PlatFormSendUtils.sendDeptInMsg(map);  
		
		return vo;
	}
	/**
	 * 根据母亲就诊主键获取婴儿列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getInfantList(String param,IUser user){
		String pk_pv = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pk_pv)){
			throw new BusException("未获取到母亲就诊信息！");
		}
		StringBuilder sql = new StringBuilder("select (infant.name ||' | '|| (case infant.dt_sex when '02' then ");
		sql.append(" '男'  when '03' then  '女' when '04' then '未知' end)) as desc_infant,");
        sql.append(" infant.name,infant.sort_no as code,infant.sort_no as py_code,infant.sort_no as d_code,");
        sql.append(" infant.pk_infant from pv_infant infant  where infant.del_flag = '0' and infant.pk_pv = '"+pk_pv+"'");
		List<Map<String,Object>> list = DataBaseHelper.queryForList(sql.toString(), new Object[]{});
		if(list!=null&&list.size()>0){
			for(Map<String,Object> map:list){
				map.put("descInfant", map.get("sortNo")+"|"+map.get("descInfant"));
			}
		}
		return list;
	}
	/**
	 * 查询待分配床位婴儿列表
	 * @param param{pkDeptNs,pkPv}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getNoBedList(String param,IUser user){
		Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
		if(paramMap == null ){
			throw new BusException("未获取到就诊病区信息！");
		}
		return pvInfantMapper.getInfantNoBedList(paramMap);
	}
	/**
	 * 分配婴儿床(此方法废弃)
	 * @param param[{pkPv,pkInfant,codeIpBed,pkIpBed,pkPi,pkDeptNs,pkDept},...]
	 * @param user
	 */
	public void asignBed(String param,IUser user){
		List<Map<String,Object>> paramList = JsonUtil.readValue(param, ArrayList.class);
		if(paramList == null || paramList.size()<0)
			throw new BusException("未获取到要保存的信息！");
		for(Map<String,Object> paramMap:paramList){
			String pk_infant = (String)paramMap.get("pkInfant");
			String bedNo = (String)paramMap.get("codeIpBed");//婴儿床号
			//更新婴儿信息对应的床号
			DataBaseHelper.update("update pv_infant set bed_no = ? where pk_infant = ? ", new Object[]{bedNo,pk_infant});
			//增加就诊床位记录
			//更新床位表患者编码
			//paramMap.put("pkPi", pk_infant);//此处将在床患者主键设置为婴儿主键，否则一个患者存在多个婴儿的情况无法进行退床操作
			//床位维护的位置的占用患者字典需要追加婴儿信息表，否则婴儿床位显示的患者姓名为主键信息
			deptInAndOutPubService.savePatiBedInfo(paramMap, user,"0");
		}
	}
	/**
	 * 保存婴儿评分记录
	 */
	public void saveGrade(String param,IUser user){
		List<PvInfantGradeVo> list = JsonUtil.readValue(param,new TypeReference<List<PvInfantGradeVo>>(){});
		if(list!=null&&list.size()>0){
			for(PvInfantGradeVo vo :list){
				PvInfantGrade grade = new PvInfantGrade();
				ApplicationUtils.copyProperties(grade, vo);
				if(CommonUtils.isEmptyString(grade.getPkInfantgrade())){//新增
					DataBaseHelper.insertBean(grade);
				}else{//更新
				   DataBaseHelper.updateBeanByPk(grade, false);
				}
			}
		}
	}
	
	/**
	 * 查询婴儿评分
	 * 新增时默认取评分字典内容加载至界面
	 * @param param{pkInfant}
	 * @param user
	 * @return
	 */
	public List<PvInfantGradeVo> queryGrade(String param,IUser user){
		String pkInfant = JsonUtil.readValue(param, String.class);
		List<PvInfantGradeVo> list = new ArrayList<PvInfantGradeVo>();
		StringBuilder sql = new StringBuilder(" select doc.pk_defdoc as pk_item, doc.name,doc.memo,");
		sql.append(" gra.pk_infant, gra.pk_infantgrade,gra.pk_pv,");
		sql.append(" gra.score_five,gra.score_one, gra.score_ten ");
	    sql.append(" from bd_defdoc doc left join pv_infant_grade gra on doc.pk_defdoc = gra.pk_item  ");
		Object[] paramObj = new Object[]{};
		if(!CommonUtils.isEmptyString(pkInfant)){//查询时增加婴儿过滤
			paramObj = new Object[]{pkInfant};
			sql.append(" and gra.pk_infant = ? ");
		}else{
			sql.append("and gra.pk_infant is null");
		}
		sql.append(" where doc.code_defdoclist = '140006' order by doc.code ");
		list = DataBaseHelper.queryForList(sql.toString(), PvInfantGradeVo.class, paramObj);
		if(list!=null&&list.size()>0){
			for(PvInfantGradeVo vo : list){
				String memo = vo.getMemo();
				if(!CommonUtils.isEmptyString(memo)){
					String[] arrstr = memo.split(",");
					if(arrstr!=null&&arrstr.length>=1)
					    vo.setDesc0(arrstr[0]);
					if(arrstr!=null&&arrstr.length>=2)
						vo.setDesc1(arrstr[1]);
					if(arrstr!=null&&arrstr.length>=3)
						vo.setDesc2(arrstr[2]);
				}
			}
		}
		return list;
	}
	
	/**
	 * 查询新增婴儿姓名
	 * 交易码：005002001065
	 * @param param
	 * @param user
	 * @return
	 */
	public String getInfantNamePi(String param,IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		String namePi = null;
		if(!CommonUtils.isEmptyString(pkPv)){
			namePi = ExSysParamUtil.getInfantNamePi(pkPv);
		}
		
		return namePi;
	}
	
	
}
