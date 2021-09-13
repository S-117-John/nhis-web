package com.zebone.nhis.cn.ipdw.service;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.cn.ipdw.dao.QcEhpMapper;
import com.zebone.nhis.cn.ipdw.vo.QcEhpVo;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.cn.ipdw.QcEhp;
import com.zebone.nhis.common.module.cn.ipdw.QcEhpDetail;
import com.zebone.nhis.common.module.cn.ipdw.QcEhpRec;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 病案质控
 * @author dell
 *
 */
@Service
public class QcEhpService {
	
	@Resource
	private QcEhpMapper qcEhpMapper;
	/**
	 * 查询病案质控列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public QcEhpVo queryQcEhpList(String param,IUser user){
		QcEhpVo qryparam = JsonUtil.readValue(param,QcEhpVo.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
		int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
		MyBatisPage.startPage(pageIndex, pageSize);
		List<Map<String,Object>> list=qcEhpMapper.queryQcEhpList(paramMap);
		if(null!=list && list.size()>0){
			for (Map<String, Object> map : list) {
				if(null!=map.get("cntDef")){
					if(null!=map.get("cntRep")){
						map.put("countNum",map.get("cntRep")+"/"+map.get("cntDef"));
					}else{
						map.put("countNum","0/"+map.get("cntDef"));
					}
				}
			}
		}
		Page<List<QcEhpVo>> page = MyBatisPage.getPage();
		QcEhpVo paramPage =new QcEhpVo();
		paramPage.setQcEhpList(list);
		paramPage.setTotalCount(page.getTotalCount());
		if(null!=list && list.size()>0){
			if(null!=list.get(0).get("pkQcehp")){
				Map<String,Object> paramS=new HashMap<String, Object>();
				paramS.put("pkQcehp", list.get(0).get("pkQcehp"));
				List<Map<String,Object>> listRec=qcEhpMapper.queryQcEhpRecList(paramS);
				paramPage.setQcEhpRecList(listRec);
				List<Map<String,Object>> listdetail=qcEhpMapper.queryQcEhpdetailList(paramS);
				paramPage.setQcEhpDetail(listdetail);
			}
		}
		return paramPage;
	}
	/**
	 * 查询质控记录和缺陷
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public QcEhpVo queryQcRecAndDetailList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		QcEhpVo qc=new QcEhpVo();
		List<Map<String,Object>> listRec=qcEhpMapper.queryQcEhpRecList(paramMap);
		qc.setQcEhpRecList(listRec);
		List<Map<String,Object>> listdetail=qcEhpMapper.queryQcEhpdetailList(paramMap);
		qc.setQcEhpDetail(listdetail);
		return qc;
	}
	/**
	 * 查询质控或评分界面数据
	 * @param param
	 * @param user
	 * euType：0 项目质控，1评分
	 * euQctype：0 科室质控，1 医务质控，2 医保质控，3 物价质控
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public QcEhpVo queryQcContro(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(null==paramMap.get("pkQcehp") &&  null==paramMap.get("pkPv") ){
			throw new BusException("请选择数据！");
		}
		String pkQcehp=null;
		if(null!=paramMap.get("pkQcehp")){
			pkQcehp=paramMap.get("pkQcehp").toString();
		}
		String pkPv=paramMap.get("pkPv").toString();
		if(null==paramMap.get("euType")){
			throw new BusException("质控类型不能为空！");
		}
		String euType=paramMap.get("euType").toString();
		//质控级别
		String euQctype=null;
		if(null!=paramMap.get("euQctype")){
			euQctype=paramMap.get("euQctype").toString();
		}
		if("0".equals(euType)){//病案质控
			return queryQcControData(pkQcehp, euQctype, euType);
		}else{//评分
			return queryQcScoreData(pkQcehp, euQctype, euType);
		}
	}
	/**
	 * 查询质控数据
	 * @param pkQcehp 质控主键
	 * @param euQctype 质控级别
	 * @param euType  质控类型
	 * @return
	 */
	private QcEhpVo queryQcControData(String pkQcehp,String euQctype,String euType){
		QcEhpRec qcEhpRec=new QcEhpRec();
		List<QcEhpDetail> listDetail=new ArrayList<QcEhpDetail>();
		QcEhpRec qcEhpRecTmp=null;

		if(!StringUtils.isEmpty(pkQcehp)){
			if(!StringUtils.isEmpty(euQctype)){
				qcEhpRecTmp=DataBaseHelper.queryForBean("select * from qc_ehp_rec where pk_qcehp=? and EU_QCTYPE=?  and eu_result is not null", QcEhpRec.class, new Object[]{pkQcehp,euQctype});
			}else{
				qcEhpRecTmp=DataBaseHelper.queryForBean("select * from qc_ehp_rec where pk_qcehp=? and eu_result is not null", QcEhpRec.class, new Object[]{pkQcehp});
			}
		}
		if(StringUtils.isEmpty(pkQcehp) || qcEhpRecTmp == null) {
			//质控级别为空时，质控所有
			String sql="select * from BD_DEFDOC  where CODE_DEFDOCLIST='060313' and DEL_FLAG='0'";
			if(!StringUtils.isEmpty(euQctype)){
				sql=sql+" and (VAL_ATTR like '%"+euQctype+"%'  or VAL_ATTR is null)";
			}
			List<Map<String,Object>> listDoc=DataBaseHelper.queryForList(sql.toString());
			QcEhpDetail qcEhpDetail=null;
			for (Map<String, Object> map : listDoc) {
				qcEhpDetail=new QcEhpDetail();
				qcEhpDetail.setEuType(euType);
				qcEhpDetail.setDtEhpitem(map.get("code").toString());
				qcEhpDetail.setNameItem(map.get("name").toString());
				listDetail.add(qcEhpDetail);
			}
		}else{
			ApplicationUtils.copyProperties(qcEhpRec,qcEhpRecTmp);
			String sql = "select eq.pk_qcdetail, eq.pk_org, eq.pk_qcehp, eq.pk_qcehprec,";
			sql+="case when eq.eu_type is null then '"+euType+"' else eq.eu_type end eu_type, case when eq.dt_ehpitem is null then doc.code else eq.dt_ehpitem end dt_ehpitem, eq.pk_qcitem,";
			sql+="case when eq.name_item is null then doc.NAME else eq.NAME_ITEM end NAME_ITEM, eq.desc_defect, eq.eu_status,";
			sql+="eq.score, eq.cnt_ded, eq.val ";
			sql+="from BD_DEFDOC doc left join QC_EHP_DETAIL  eq" +
					" on doc.CODE = eq.DT_EHPITEM and eq.PK_QCEHPREC ='"+qcEhpRec.getPkQcehprec()+"'" +
					" where doc.CODE_DEFDOCLIST='060313' and doc.DEL_FLAG='0'";
			if(!StringUtils.isEmpty(euQctype)){
				sql += " and (VAL_ATTR like '%"+euQctype+"%'  or VAL_ATTR is null)";
			}
			listDetail=DataBaseHelper.queryForList(sql, QcEhpDetail.class);
		}
		QcEhpVo qc=new QcEhpVo();
		if(StringUtils.isEmpty(qcEhpRec.getEuResult())){
			if(qcEhpRecTmp != null){
				qcEhpRec.setEuResult(qcEhpRecTmp.getEuResult());
				qcEhpRec.setNote(qcEhpRecTmp.getNote());
			}else{
				qcEhpRec.setEuResult("1");
			}

		}
		qc.setQcEhpRecVo(qcEhpRec);
		qc.setListDetail(listDetail);
		return qc;
	}
	/**
	 * 查询评分数据
	 * @param pkQcehp 质控主键
	 * @param euQctype 质控级别
	 * @param euType  质控类型
	 * @return
	 */
	private QcEhpVo queryQcScoreData(String pkQcehp,String euQctype,String euType){
		
		int cnt=0;
		if(!StringUtils.isEmpty(pkQcehp)){
			Map<String,Object> mapCnt=DataBaseHelper.queryForMap("select count(1) cnt from QC_EHP_DETAIL where PK_QCEHP=? and EU_TYPE='1'", pkQcehp);
			if(null!=mapCnt && null!=mapCnt.get("cnt")){
				Integer count=Integer.valueOf(mapCnt.get("cnt").toString());
				if(null!=count){
					cnt=count.intValue();
				}
			}
		}
		QcEhpRec qcEhpRec=new QcEhpRec();
		List<QcEhpDetail> listDetail=new ArrayList<QcEhpDetail>();
		//没有质控主键和评分明细说明没有评分过，直接创建评分初始数据
		if(StringUtils.isEmpty(pkQcehp) || cnt==0){
			qcEhpRec.setEuQctype(null);
			//查询评分项目
			List<Map<String, Object>> listScoreItem=qcEhpMapper.queryBdScoreItem(null);
			QcEhpDetail qcEhpDetail=null;
			for (Map<String, Object> map : listScoreItem) {
				qcEhpDetail=new QcEhpDetail();
				qcEhpDetail.setEuType(euType);
				qcEhpDetail.setDtEhpitem(map.get("dtEhpitemtype").toString());
				qcEhpDetail.setPkQcitem(map.get("pkQcitem").toString());
				qcEhpDetail.setNameItem(map.get("name").toString());
				qcEhpDetail.setItemCount(map.get("itemCount").toString());
				qcEhpDetail.setScore(map.get("val").toString());
				qcEhpDetail.setScoreItem(map.get("nameQc").toString());
				qcEhpDetail.setEuItemcate(map.get("euItemcate").toString());
				qcEhpDetail.setValMax(Double.parseDouble(map.get("valMax").toString()));
				listDetail.add(qcEhpDetail);
			}
		}else{//查询评分记录
			//待处理
			qcEhpRec=DataBaseHelper.queryForBean("select * from qc_ehp_rec where pk_qcehp=? and EU_QCTYPE is null and EU_RESULT is null", QcEhpRec.class, new Object[]{pkQcehp});
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("pkQcehp", pkQcehp);
			param.put("pkQcehprec", qcEhpRec.getPkQcehprec());
			listDetail=qcEhpMapper.queryScoreItem(param);
		}
		QcEhpVo qc=new QcEhpVo();
		qc.setQcEhpRecVo(qcEhpRec);
		qc.setListDetail(listDetail);
		return qc;
	}
	/**
	 * 保存质控结果
	 * @param param
	 * @param user
	 */
	public void saveEhpRec(String param,IUser user){
		QcEhpVo qcEhpVo = JsonUtil.readValue(param,QcEhpVo.class);
		if("0".equals(qcEhpVo.getQcEhpRecVo().getEuType())){//质控
			this.saveEhpRecControl(param, user);
		}else{//评分
			this.saveEhpRecScore(param, user);
		}
	}
	/**
	 * 保存质控数据
	 * @param param
	 * @param user
	 */
	private void saveEhpRecControl(String param,IUser user){
		QcEhpVo qcEhpVo = JsonUtil.readValue(param,QcEhpVo.class);
		if(null==qcEhpVo.getQcEhpRecVo().getPkPv()){
			throw new BusException("就诊主键不能为空！");
		}
		QcEhpRec qcEhpRec=qcEhpVo.getQcEhpRecVo();
		if(null==qcEhpRec){
			throw new BusException("质控结果不能为空！");
		}
		List<QcEhpDetail> listDetail=qcEhpVo.getListDetail();
//		if(null==listDetail || listDetail.size()==0){
//			throw new BusException("质控明细不能为空！");
//		}
		User users = UserContext.getUser();
		QcEhp qcEhp=new QcEhp();
		//处理质控主表
		if(StringUtils.isEmpty(qcEhpRec.getPkQcehp())){//新增
			//保存质控主表
			String pkPv=qcEhpVo.getQcEhpRecVo().getPkPv();
			
			qcEhp.setPkPv(pkPv);
			ApplicationUtils.setDefaultValue(qcEhp, true);
			qcEhp.setEuStatus("0");
			if(!StringUtils.isEmpty(qcEhpRec.getEuQctype())){
				if(qcEhpRec.getEuQctype().equals("0")){//0 科室质控，1 医务质控，2 医保质控，3 物价质控
					qcEhp.setEuQcDept(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("1")){
					qcEhp.setEuQcMm(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("2")){
					qcEhp.setEuQcIns(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("3")){
					qcEhp.setEuQcPce(qcEhpRec.getEuResult());
				}
			}
			DataBaseHelper.insertBean(qcEhp);
		}else{
			qcEhp=DataBaseHelper.queryForBean("select * from QC_EHP where PK_QCEHP=?", QcEhp.class, qcEhpRec.getPkQcehp());
			if(!StringUtils.isEmpty(qcEhpRec.getEuQctype())){
				if(qcEhpRec.getEuQctype().equals("0")){//0 科室质控，1 医务质控，2 医保质控，3 物价质控
					qcEhp.setEuQcDept(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("1")){
					qcEhp.setEuQcMm(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("2")){
					qcEhp.setEuQcIns(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("3")){
					qcEhp.setEuQcPce(qcEhpRec.getEuResult());
				}
			}
			qcEhp.setModifier(users.getPkEmp());
			//如果是修改质控为不通过，则原来完成的状态也需要改回未完成状态
			if("1".equals(qcEhp.getEuStatus()) && "0".equals(qcEhpVo.getQcEhpRecVo().getEuResult())) {
				qcEhp.setEuStatus("0");
			}
			DataBaseHelper.updateBeanByPk(qcEhp,false);
//			qcEhpRec.setModifier(users.getPkEmp());
//			DataBaseHelper.updateBeanByPk(qcEhpRec,false);
		}
		//处理质控表
		QcEhpRec qcEhpRecOld=null;
		if(!StringUtils.isEmpty(qcEhpRec.getEuQctype())){
			qcEhpRecOld=DataBaseHelper.queryForBean("select * from qc_ehp_rec where pk_qcehp=? and EU_RESULT is not null and EU_QCTYPE=?", QcEhpRec.class, new Object[]{qcEhp.getPkQcehp(),qcEhpRec.getEuQctype()});
		}else{
			qcEhpRecOld=DataBaseHelper.queryForBean("select * from qc_ehp_rec where pk_qcehp=? and EU_RESULT is not null  and EU_QCTYPE is null", QcEhpRec.class, new Object[]{qcEhp.getPkQcehp()});
		}
		if(null==qcEhpRecOld){
			//保存质控记录
			qcEhpRec.setPkQcehp(qcEhp.getPkQcehp());
			qcEhpRec.setDateQc(new Date());
			qcEhpRec.setPkDept(users.getPkDept());
			qcEhpRec.setPkEmpQc(users.getPkEmp());
			qcEhpRec.setNameEmpQc(users.getNameEmp());
			ApplicationUtils.setDefaultValue(qcEhpRec, true);
			DataBaseHelper.insertBean(qcEhpRec);
		}else{
			if(StringUtils.isEmpty(qcEhpRec.getPkQcehprec())) {
				qcEhpRec.setPkQcehprec(qcEhpRecOld.getPkQcehprec());
			}
			qcEhpRec.setDateQc(new Date());
			qcEhpRec.setModifier(users.getPkEmp());
			qcEhpRec.setEuResult(qcEhpVo.getQcEhpRecVo().getEuResult());
			DataBaseHelper.updateBeanByPk(qcEhpRec,false);
		}
		//保存质控明细
		if(listDetail !=null && listDetail.size() > 0)
		{
			DataBaseHelper.deleteBeanByWhere(new QcEhpDetail(), " PK_QCEHPREC='"+qcEhpRec.getPkQcehprec()+"'");
			for (QcEhpDetail qcEhpDetail : listDetail) {
				qcEhpDetail.setPkQcehp(qcEhp.getPkQcehp());
				qcEhpDetail.setPkQcehprec(qcEhpRec.getPkQcehprec());
				ApplicationUtils.setDefaultValue(qcEhpDetail, true);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(QcEhpDetail.class), listDetail);
		}

		/*
		
		if(StringUtils.isEmpty(qcEhpRec.getPkQcehp())){//新增
			//保存质控主表
			String pkPv=qcEhpVo.getQcEhpRecVo().getPkPv();
			QcEhp qcEhp=new QcEhp();
			qcEhp.setPkPv(pkPv);
			ApplicationUtils.setDefaultValue(qcEhp, true);
			qcEhp.setEuStatus("0");
			if(!StringUtils.isEmpty(qcEhpRec.getEuQctype())){
				if(qcEhpRec.getEuQctype().equals("0")){//0 科室质控，1 医务质控，2 医保质控，3 物价质控
					qcEhp.setEuQcDept(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("1")){
					qcEhp.setEuQcMm(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("2")){
					qcEhp.setEuQcIns(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("3")){
					qcEhp.setEuQcPce(qcEhpRec.getEuResult());
				}
			}
			DataBaseHelper.insertBean(qcEhp);
			//保存质控记录
			qcEhpRec.setPkQcehp(qcEhp.getPkQcehp());
			qcEhpRec.setDateQc(new Date());
			qcEhpRec.setPkDept(users.getPkDept());
			qcEhpRec.setPkEmpQc(users.getPkEmp());
			qcEhpRec.setNameEmpQc(users.getNameEmp());
			ApplicationUtils.setDefaultValue(qcEhpRec, true);
			DataBaseHelper.insertBean(qcEhpRec);
			//保存质控明细
			for (QcEhpDetail qcEhpDetail : listDetail) {
				qcEhpDetail.setPkQcehp(qcEhp.getPkQcehp());
				qcEhpDetail.setPkQcehprec(qcEhpRec.getPkQcehprec());
				ApplicationUtils.setDefaultValue(qcEhpDetail, true);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(QcEhpDetail.class), listDetail);
		}else{//修改
			QcEhp qcEhp=DataBaseHelper.queryForBean("select * from QC_EHP where PK_QCEHP=?", QcEhp.class, qcEhpRec.getPkQcehp());
			if(!StringUtils.isEmpty(qcEhpRec.getEuQctype())){
				if(qcEhpRec.getEuQctype().equals("0")){//0 科室质控，1 医务质控，2 医保质控，3 物价质控
					qcEhp.setEuQcDept(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("1")){
					qcEhp.setEuQcMm(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("2")){
					qcEhp.setEuQcIns(qcEhpRec.getEuResult());
				}else if(qcEhpRec.getEuQctype().equals("3")){
					qcEhp.setEuQcPce(qcEhpRec.getEuResult());
				}
			}
			qcEhp.setModifier(users.getPkEmp());
			DataBaseHelper.updateBeanByPk(qcEhp,false);
			qcEhpRec.setModifier(users.getPkEmp());
			DataBaseHelper.updateBeanByPk(qcEhpRec,false);
			DataBaseHelper.deleteBeanByWhere(new QcEhpDetail(), " PK_QCEHPREC='"+qcEhpRec.getPkQcehprec()+"'");
			//保存质控明细
			for (QcEhpDetail qcEhpDetail : listDetail) {
				ApplicationUtils.setDefaultValue(qcEhpDetail, true);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(QcEhpDetail.class), listDetail);
		}*/
	}
	/**
	 * 保存评分数据
	 * @param param
	 * @param user
	 */
	private void saveEhpRecScore(String param,IUser user){
		QcEhpVo qcEhpVo = JsonUtil.readValue(param,QcEhpVo.class);
		if(null==qcEhpVo.getQcEhpRecVo().getPkPv()){
			throw new BusException("就诊主键不能为空！");
		}
		QcEhpRec qcEhpRec=qcEhpVo.getQcEhpRecVo();
		User users = UserContext.getUser();
		List<QcEhpDetail> listDetail=qcEhpVo.getListDetail();
		//计算总分
		Double val=(double) 0;
		for (QcEhpDetail qcEhpDetail : listDetail) {
			if(null!=qcEhpDetail.getVal()){
				val=val+qcEhpDetail.getVal();
			}
		}
		Double sumVal=100-val;
		QcEhp qcEhp=new QcEhp();
		if(StringUtils.isEmpty(qcEhpRec.getPkQcehp())){//新增
			//保存质控主表
			String pkPv=qcEhpVo.getQcEhpRecVo().getPkPv();
			qcEhp.setPkPv(pkPv);
			qcEhp.setScore(sumVal);
			ApplicationUtils.setDefaultValue(qcEhp, true);
			qcEhp.setEuStatus("0");
			DataBaseHelper.insertBean(qcEhp);
		}else{//有得话修改
			qcEhp=DataBaseHelper.queryForBean("select * from QC_EHP where PK_QCEHP=?", QcEhp.class, qcEhpRec.getPkQcehp());
			qcEhp.setModifier(users.getPkEmp());
			qcEhp.setScore(sumVal);
			DataBaseHelper.updateBeanByPk(qcEhp,false);
		}
		//处理质控表
		QcEhpRec qcEhpRecOld=DataBaseHelper.queryForBean("select * from qc_ehp_rec where pk_qcehp=? and EU_QCTYPE is null and EU_RESULT is null", QcEhpRec.class, new Object[]{qcEhp.getPkQcehp()});
		if(null==qcEhpRecOld){
			//保存质控记录
			qcEhpRecOld=new QcEhpRec();
			qcEhpRecOld.setPkQcehp(qcEhp.getPkQcehp());
			qcEhpRecOld.setDateQc(new Date());
			qcEhpRecOld.setPkDept(users.getPkDept());
			qcEhpRecOld.setPkEmpQc(users.getPkEmp());
			qcEhpRecOld.setNameEmpQc(users.getNameEmp());
			qcEhpRecOld.setNote(qcEhpRec.getNote());
			ApplicationUtils.setDefaultValue(qcEhpRecOld, true);
			qcEhpRecOld.setEuResult(null);
			DataBaseHelper.insertBean(qcEhpRecOld);
		}else{
			qcEhpRecOld.setNote(qcEhpRec.getNote());
			DataBaseHelper.updateBeanByPk(qcEhpRecOld,false);
		}
		//处理明细表
		DataBaseHelper.deleteBeanByWhere(new QcEhpDetail(), " PK_QCEHPREC='"+qcEhpRecOld.getPkQcehprec()+"'");
		for (QcEhpDetail qcEhpDetail : listDetail) {
			qcEhpDetail.setPkQcehp(qcEhp.getPkQcehp());
			qcEhpDetail.setPkQcehprec(qcEhpRecOld.getPkQcehprec());
			ApplicationUtils.setDefaultValue(qcEhpDetail, true);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(QcEhpDetail.class), listDetail);
	}
	/**
	 * 完成质控
	 * @param param
	 * @param user
	 */
	public void finishData(String param,IUser user) {
		QcEhp qcEhpVo = JsonUtil.readValue(param,QcEhp.class);
		if(StringUtils.isEmpty(qcEhpVo.getPkQcehp())){
			throw new BusException("请选择数据!");
		}
		String finishLevel = qcEhpVo.getNote();
		StringBuffer sb = new StringBuffer("select COUNT(1) from qc_ehp_detail dt left join QC_EHP_REC qc on dt.PK_QCEHP = qc.PK_QCEHP and dt.PK_QCEHPREC = qc.PK_QCEHPREC where dt.eu_type='0' and dt.eu_status!='1' and dt.pk_qcehp in ("+qcEhpVo.getPkQcehp()+") ");
		if("0".equals(finishLevel)) {
			sb.append(" and qc.EU_QCTYPE='0' ");
		}else if("1".equals(finishLevel)){
			sb.append(" and (qc.EU_QCTYPE='0' or qc.EU_QCTYPE='1') ");
		}else if("2".equals(finishLevel)){
			sb.append(" and (qc.EU_QCTYPE='0' or qc.EU_QCTYPE='1' or qc.EU_QCTYPE='2') ");
		}else{
			sb.append(" and (qc.EU_QCTYPE='0' or qc.EU_QCTYPE='1' or qc.EU_QCTYPE='2' or qc.EU_QCTYPE='3') ");
		}
		int count=DataBaseHelper.queryForScalar(sb.toString(), Integer.class);
		String euGard = qcEhpVo.getEuGrade();
		if(count>0){
			if("1".equals(euGard)){
				throw new BusException("缺陷必须全部修复才能点击完成！");
			}else{
				return;
			}
		}
		sb = new StringBuffer("update qc_ehp set eu_status='1' where PK_QCEHP in ("+qcEhpVo.getPkQcehp()+") and eu_status='0'" );
		if("0".equals(finishLevel)) {
			sb.append(" and eu_qc_dept='1' ");
		}else if("1".equals(finishLevel)){
			sb.append(" and eu_qc_dept='1' and eu_qc_mm='1' ");
		}else if("2".equals(finishLevel)){
			sb.append(" and eu_qc_dept='1' and eu_qc_mm='1' and eu_qc_ins='1' ");
		}else{
			sb.append(" and eu_qc_dept='1' and eu_qc_mm='1' and eu_qc_ins='1' and eu_qc_pce='1' ");
		}
		DataBaseHelper.update(sb.toString());
	}
	/**
	 * 退回质控
	 * @param param
	 * @param user
	 */
	public void returnData(String param, IUser user) {
		QcEhp qcEhpVo = JsonUtil.readValue(param,QcEhp.class);
		if(StringUtils.isEmpty(qcEhpVo.getPkPv())){
			throw new BusException("请选择数据!");
		}
		String euQctype=null;
		if(null!=JSONObject.parseObject(param).get("euQctype")){
			euQctype=(String) JSONObject.parseObject(param).get("euQctype");
		}
//		if(!StringUtils.isEmpty(euQctype)){
//			int count=DataBaseHelper.queryForScalar("Select Count(1) From qc_ehp_rec rec Left Join qc_ehp ehp On rec.pk_qcehp=ehp.pk_qcehp Where ehp.pk_pv in ("+qcEhpVo.getPkPv()+") And rec.eu_qctype=?", Integer.class,euQctype);
//			if(count==0){
//				throw new BusException("当前没有质控记录不允许退回！");
//			}
//		}
		DataBaseHelper.update("update QC_EHP set eu_qc_dept='0' ,eu_qc_mm='0',eu_qc_ins='0',eu_qc_pce='0',note='',eu_status='-1' where pk_pv in ("+qcEhpVo.getPkPv()+") ");
		User users = UserContext.getUser();
		PvIp pvIp=new PvIp();
		pvIp.setDateCommitDoc(new Date());
		pvIp.setEuStatusDoc("-2");
		pvIp.setPkEmpReturn(users.getPkEmp());
		pvIp.setDateReturn(new Date());
		DataBaseHelper.updateBeanByWhere(pvIp," eu_status_doc='0' and pk_pv in ("+qcEhpVo.getPkPv()+")",false);
	}
	/**
	 * 自动质控
	 * @param param
	 * @param user
	 * @return
	 */
	public QcEhpVo autoConTrol(String param,IUser user){
		QcEhpVo qcEhpVo = JsonUtil.readValue(param,QcEhpVo.class);
		if(null==qcEhpVo.getQcEhpRecVo().getPkPv()){
			throw new BusException("就诊主键不能为空！");
		}
		List<Map<String, Object>> listHomePage=DataBaseHelper.queryForList("select * from EMR_HOME_PAGE where DEL_FLAG='0' and pk_pv=?",qcEhpVo.getQcEhpRecVo().getPkPv());
		if(null==listHomePage || listHomePage.size()==0){
			throw new BusException("未查询到当前患者得病案信息，请检查！");
		}
		Map<String, Object> homePage=listHomePage.get(0);
		QcEhpRec qcEhpRec=qcEhpVo.getQcEhpRecVo();
		List<QcEhpDetail> listDetail=qcEhpVo.getListDetail();
		int cnt=0;
		//循环处理质控
		for (QcEhpDetail qcEhpDetail : listDetail) {
			String dtEhpitem=qcEhpDetail.getDtEhpitem();//质控元素
			List<Map<String, Object>> listQcRule=DataBaseHelper.queryForList("select * from BD_EHP_QCRULE where DEL_FLAG='0' and FLAG_ACTIVE='1' and EU_TYPE='0' and DT_EHPITEM=?",qcEhpDetail.getDtEhpitem());
			String text=null;
			String age=homePage.get("ageTxt").toString();
			int day = -1;
			if(age.contains("D")){
				day=Integer.parseInt(age.split("D")[1]);
			}
			if("0001".equals(dtEhpitem)){//新生儿入院体重
				if(day>28 || day == -1){
					continue;
				}
				text=homePage.get("newbornInWeight").toString();
			}else if("0002".equals(dtEhpitem)){//新生儿出生体重
				if(day>28 || day == -1){
					continue;
				}
				text=homePage.get("newbornWeight").toString();
			}else if("0003".equals(dtEhpitem)){//病案号
				text=homePage.get("patNo").toString();
			}else if("0004".equals(dtEhpitem)){//性别
				text=homePage.get("dtSex").toString();
				BdDefdoc bdDefdoc=DataBaseHelper.queryForBean("select * from bd_defdoc where CODE_DEFDOCLIST='000000' and DEL_FLAG='0' and ba_code=?", BdDefdoc.class, text);
				text=bdDefdoc.getName();
			}else if("0005".equals(dtEhpitem)){//出生日期
				text=homePage.get("birthDate").toString();
			}else if("0006".equals(dtEhpitem)){//年龄
				text=homePage.get("ageTxt").toString();
			}else if("0007".equals(dtEhpitem)){//医疗付费方式
				text=homePage.get("medPayMode").toString();//不对
				BdDefdoc bdDefdoc=DataBaseHelper.queryForBean("select * from bd_defdoc where CODE_DEFDOCLIST='040009' and DEL_FLAG='0' and ba_code=?", BdDefdoc.class, text);
				text=bdDefdoc.getName();
			}
			int count=0;
			for (Map<String, Object> map : listQcRule) {
				String euRest=map.get("euRest").toString();
				String euOperator=map.get("euOperator").toString();//运算符号
				String val=map.get("val").toString();//约束值
				String valMax=null;//约束值上限
				if(null!=map.get("valMax")){
					valMax = map.get("valMax").toString();
				}
				boolean bool = true;
				if("0".equals(euRest)){//长度
					bool=checkLength(euOperator, text, val, valMax);
				}else{//内容
					bool=checkContent(euOperator, text,val, valMax);
				}
				if(bool==false){
					count++;
				}
			}
			if(count>0){
				qcEhpDetail.setEuStatus("0");
				cnt++;
			}
		}
		if(cnt>0){
			qcEhpRec.setEuResult("0");
		}else{
			qcEhpRec.setEuResult("1");
		}
		QcEhpVo qc=new QcEhpVo();
		qc.setQcEhpRecVo(qcEhpRec);
		qc.setListDetail(listDetail);
		return qc;
	}
	/**
	 * 质控时判断长短
	 * @param
	 * @param
	 * @param
	 * @param
	 * @return质控通过返回true,其他为flase
	 */
	private boolean checkLength(String euOperator,String text,String val,String valMax){
		boolean bool=true;
		if(StringUtils.isEmpty(text)){
			return bool;
		}
		int len=text.length();
		int checkLen=0;
		try {
			checkLen=Integer.parseInt(val);
		} catch (Exception e) {
			throw new BusException("质控规则维护有问题，请检查！");
		}
		if("1".equals(euOperator)){//包含
		}else if("2".equals(euOperator)){//不包含
		}else if("3".equals(euOperator)){//等于
			if(len!=checkLen){
				bool=false;
			}
		}else if("4".equals(euOperator)){//不等于
			if(len==checkLen){
				bool=false;
			}
		}else if("5".equals(euOperator)){//大于
			if(len<=checkLen){
				bool=false;
			}
		}else if("6".equals(euOperator)){//大于等于
			if(len<checkLen){
				bool=false;
			}
		}else if("7".equals(euOperator)){//小于
			if(len>=checkLen){
				bool=false;
			}
		}else if("8".equals(euOperator)){//小于等于
			if(len>checkLen){
				bool=false;
			}
		}else if("9".equals(euOperator)){//介于
			int checkLenMax=0;
			try {
				checkLenMax=Integer.parseInt(valMax);
			} catch (Exception e) {
				throw new BusException("质控规则维护有问题，请检查！");
			}
			if(len<checkLen || len>checkLenMax){
				bool=false;
			}
		}else{
		}
		return bool;
	}
	/**
	 * 质控时判断长短
	 * @parameuOperator运算符号
	 * @paramtext值
	 * @paramval约束值
	 * @paramvalMax约束值上限
	 * @return质控通过返回true,其他为flase
	 */
	private boolean checkContent(String euOperator,String text,String val,String valMax){
		boolean bool=true;
		if(StringUtils.isEmpty(text)){
			return bool;
		}
		if("1".equals(euOperator)){//包含
			if(!val.contains(text)){
				bool=false;
			}
		}else if("2".equals(euOperator)){//不包含
			if(val.contains(text)){
				bool=false;
			}
		}else if("3".equals(euOperator)){//等于
			if(!text.equals(val)){
				bool=false;
			}
		}else if("4".equals(euOperator)){//不等于
			if(text.equals(val)){
				bool=false;
			}
		}else if("5".equals(euOperator)){//大于
		}else if("6".equals(euOperator)){//大于等于
		}else if("7".equals(euOperator)){//小于
		}else if("8".equals(euOperator)){//小于等于
		}else if("9".equals(euOperator)){//介于
			if(text.compareTo(val)<0 || text.compareTo(valMax)>0){
				bool=false;
			}
		}else{
		}
		return bool;
	}
	/**
	 * 自动评分
	 * @param param
	 * @param user
	 * @return
	 */
	public QcEhpVo autoScore(String param,IUser user){
		QcEhpVo qcEhpVo = JsonUtil.readValue(param,QcEhpVo.class);
		if(null==qcEhpVo.getQcEhpRecVo().getPkPv()){
			throw new BusException("就诊主键不能为空！");
		}
		List<Map<String, Object>> listHomePage=DataBaseHelper.queryForList("select * from EMR_HOME_PAGE where DEL_FLAG='0' and pk_pv=?",qcEhpVo.getQcEhpRecVo().getPkPv());
		if(null==listHomePage || listHomePage.size()==0){
			throw new BusException("未查询到当前患者得病案信息，请检查！");
		}
		QcEhpRec qcEhpRec=qcEhpVo.getQcEhpRecVo();//评分主表
		List<QcEhpDetail> listDetail=qcEhpVo.getListDetail();//评分明细
		//评分前先查询要质控的数据
		QcEhpVo qcEhpControl=queryQcControData(null,null,"1");
		QcEhpRec qcEhpRecNew=qcEhpControl.getQcEhpRecVo();
		qcEhpRecNew.setPkPv(qcEhpRec.getPkPv());
		qcEhpControl.setQcEhpRecVo(qcEhpRecNew);
		//调用自动质控,返回质控结果和明细
		QcEhpVo qcEhpControlResult=autoConTrol(JsonUtil.writeValueAsString(qcEhpControl), user);
		//质控明细
		List<QcEhpDetail> listControlResult=qcEhpControlResult.getListDetail();
		
		//开始处理评分
		for (QcEhpDetail qcEhpDetail : listDetail) {
			//查询到质控项目主表
			List<Map<String, Object>> qcItemList=DataBaseHelper.queryForList("select a.*,b.*,aa.DT_EHPITEM from bd_ehp_qcitem a left join BD_EHP_QCITEM_rule b on b.PK_QCITEM=a.PK_QCITEM left join BD_EHP_qcrule aa on aa.code_rule=b.CODE_RULE where a.DEL_FLAG='0'  and a.PK_QCITEM=?",qcEhpDetail.getPkQcitem());
			if(null==qcItemList || qcItemList.size()==0){
				throw new BusException("没有查到对应的质控项目，请检查！");
			}
			Map<String, Object> qcItemA=qcItemList.get(0);
			//统计需要减分的项
			int cnt=0;
			for (Map<String, Object> map : qcItemList) {
				for (QcEhpDetail qcEhpDetailResult : listControlResult) {
					if("0".equals(qcEhpDetailResult.getEuStatus())){
						if(map.get("dtEhpitem").equals(qcEhpDetailResult.getDtEhpitem())){
							cnt++;
						}
					}
				}
			}
			//大于0表示要扣分
			if(cnt>0){
				qcEhpDetail.setCntDed(cnt);
				Double val=qcEhpDetail.getCntDed()*Double.valueOf(qcEhpDetail.getScore());
				Double valMax=Double.parseDouble(qcItemA.get("valMax").toString());
				if(0!=valMax && val>valMax){
					val=valMax;
				}
				qcEhpDetail.setVal(val);
			}
		}
		QcEhpVo qc=new QcEhpVo();
		qc.setQcEhpRecVo(qcEhpRec);
		qc.setListDetail(listDetail);
		return qc;
	}
}
