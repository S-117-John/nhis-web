package com.zebone.nhis.webservice.cxf.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.cxf.INHISPskqDataUpWebService;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.vo.LbSHRequestVo;
import com.zebone.nhis.webservice.vo.LbSHResponseVo;
import com.zebone.nhis.webservice.vo.dataUp.DataUpRequestVo;
import com.zebone.nhis.webservice.vo.dataUp.DataUpResponseVo;
import com.zebone.nhis.webservice.vo.dataUp.RespItemsVo;
import com.zebone.nhis.webservice.vo.dataUp.RespWardVo;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * @Classname INHISPskqSchWebService
 * @Description  坪山口腔项目-数据上报接口
 * @Created by ds
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class NHISPskqDataUpWebServiceImpl implements INHISPskqDataUpWebService {
	
	private Logger logger = LoggerFactory.getLogger("nhis.lbWebServiceLog");
	/**
	 * 获取门诊实时情况
	 * @param param
	 * @return
	 */
	@Override
	public String getHospitalReport_Realtime_MZ(String param) {
		// TODO Auto-generated method stub
		logger.info("getHospitalReport_Realtime_MZ获取门诊实时情况："+param);
		DataUpResponseVo responseVo = new DataUpResponseVo();
		if (StringUtils.isBlank(param)) {
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("参数不能为空！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		DataUpRequestVo requ = (DataUpRequestVo) XmlUtil.XmlToBean(param,DataUpRequestVo.class);
		String itemId="";
		if(StringUtils.isNotBlank(requ.getItemId())){
			itemId=requ.getItemId();
		}
		String deptId="";
		if(StringUtils.isNotBlank(requ.getDeptId())){
			BdOuDept bdOuDept=getDeptByCode(requ.getDeptId());
			if(null==bdOuDept || StringUtils.isBlank(bdOuDept.getPkDept())){
				responseVo.setResultCode("-1");
				responseVo.setResultDesc("科室参数无法查到科室请检查！");
				return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
			}
			deptId=bdOuDept.getPkDept();
		}
		try {
			//mzrs:门诊人数
			if(StringUtils.isBlank(itemId) || itemId.contains("mzrs")){
				String sql="select COUNT(1) mzrs from PV_ENCOUNTER where TO_CHAR(DATE_BEGIN,'yyyy-mm-dd')=TO_CHAR(SYSDATE,'yyyy-mm-dd') and EU_PVTYPE='1' and EU_STATUS not in ('0','9')";
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql=sql+" and pk_dept=?";
					count=DataBaseHelper.queryForScalar(sql, Integer.class,deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql, Integer.class);
				}
				responseVo.setMzrs(String.valueOf(count));
			}
			//jzrs:急诊人数
			if(StringUtils.isBlank(itemId) || itemId.contains("jzrs")){
				responseVo.setJzrs("0");
			}
			//mzss:门诊手术
			if(StringUtils.isBlank(itemId) || itemId.contains("mzss")){
				responseVo.setMzss("0");
			}
			//cfsl:处方数
			if(StringUtils.isBlank(itemId) || itemId.contains("cfsl")){
				StringBuffer sql = new StringBuffer();
				sql.append("select COUNT(1) cfsl from CN_PRESCRIPTION c ");
				sql.append("INNER JOIN PV_ENCOUNTER pv on pv.PK_PV=c.PK_PV ");
				sql.append(" where TO_CHAR(c.CREATE_TIME,'yyyy-mm-dd')=TO_CHAR(SYSDATE,'yyyy-mm-dd') and pv.EU_PVTYPE='1' and pv.EU_STATUS in ('1','2') ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append(" and pv.pk_dept=?");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class);
				}
				responseVo.setCfsl(String.valueOf(count));
			}
			//dzrs:待诊人数
			if(StringUtils.isBlank(itemId) || itemId.contains("dzrs")){
				String sql="select COUNT(1) dzrs from PV_ENCOUNTER where TO_CHAR(DATE_BEGIN,'yyyy-mm-dd')=TO_CHAR(SYSDATE,'yyyy-mm-dd') and EU_PVTYPE='1' and EU_STATUS in ('0') ";
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql=sql+" and pk_dept=?";
					count=DataBaseHelper.queryForScalar(sql, Integer.class,deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql, Integer.class);
				}
				responseVo.setDzrs(String.valueOf(count));
			}
			//dqys:待取药
			if(StringUtils.isBlank(itemId) || itemId.contains("dqys")){
				StringBuffer sql = new StringBuffer();
				sql.append("select COUNT(1) dqys from  EX_PRES_OCC c ");
				sql.append(" INNER JOIN CN_PRESCRIPTION cp on cp.PK_PRES=c.PK_PRES ");
				sql.append(" INNER JOIN PV_ENCOUNTER pv on pv.PK_PV=cp.PK_PV ");
				sql.append("  where TO_CHAR(pv.DATE_BEGIN,'yyyy-mm-dd')=TO_CHAR(SYSDATE,'yyyy-mm-dd') and pv.EU_PVTYPE='1' and pv.EU_STATUS in ('0','1','2') and c.EU_STATUS not in ('3','9') and c.FLAG_REG='1' ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append(" and pv.pk_dept=?");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class);
				}
				responseVo.setDqys(String.valueOf(count));
			}
			//zj:专家
			if(StringUtils.isBlank(itemId) || itemId.contains("zj")){
				responseVo.setZj("0");
				StringBuffer sql = new StringBuffer();
				sql.append("select COUNT(1) zj from CN_PRESCRIPTION c ");
				sql.append("INNER JOIN PV_ENCOUNTER pv on pv.PK_PV=c.PK_PV ");
				sql.append("left join BL_SETTLE bs on bs.PK_PV=pv.PK_PV and bs.EU_PVTYPE='1' and bs.DT_STTYPE='00' ");
				sql.append("left join BL_OP_DT bod on bod.PK_SETTLE=bs.PK_SETTLE ");
				sql.append(" where TO_CHAR(c.CREATE_TIME,'yyyy-mm-dd')=TO_CHAR(SYSDATE,'yyyy-mm-dd') and pv.EU_PVTYPE='1' and pv.EU_STATUS in ('1','2') ");
				sql.append(" and bod.PK_ITEM in (SELECT pk_item from BD_ITEM where DEL_FLAG='0' and FLAG_ACTIVE='1' and (name like '%主任%' or name like '%名专家%')) ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append(" and pv.pk_dept=?");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class);
				}
				responseVo.setZj(String.valueOf(count));
			}
			//pt:普通
			if(StringUtils.isBlank(itemId) || itemId.contains("pt")){
				responseVo.setPt("0");
				StringBuffer sql = new StringBuffer();
				sql.append("select COUNT(1) pt from CN_PRESCRIPTION c ");
				sql.append("INNER JOIN PV_ENCOUNTER pv on pv.PK_PV=c.PK_PV ");
				sql.append("left join BL_SETTLE bs on bs.PK_PV=pv.PK_PV and bs.EU_PVTYPE='1' and bs.DT_STTYPE='00' ");
				sql.append("left join BL_OP_DT bod on bod.PK_SETTLE=bs.PK_SETTLE ");
				sql.append(" where TO_CHAR(c.CREATE_TIME,'yyyy-mm-dd')=TO_CHAR(SYSDATE,'yyyy-mm-dd') and pv.EU_PVTYPE='1' and pv.EU_STATUS in ('1','2') ");
				sql.append(" and bod.PK_ITEM in (SELECT pk_item from BD_ITEM where DEL_FLAG='0' and FLAG_ACTIVE='1' and name like '%普通门诊诊查费%' ) ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append(" and pv.pk_dept=?");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class);
				}
				responseVo.setPt(String.valueOf(count));
			}
			//zlds:治疗单数
			if(StringUtils.isBlank(itemId) || itemId.contains("zlds")){
				String sql="select COUNT(1) zlds from CN_ORDER cn INNER JOIN PV_ENCOUNTER pv on pv.PK_PV=cn.PK_PV "
						+ "where cn.CODE_ORDTYPE='05' and TO_CHAR(cn.DATE_ENTER,'yyyy-mm-dd')=TO_CHAR(SYSDATE,'yyyy-mm-dd') and cn.FLAG_BL='1' "
						+ " and cn.FLAG_ERASE='0' and pv.EU_PVTYPE='1' and pv.EU_STATUS in ('1','2') ";
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql=sql+" and pv.pk_dept=?";
					count=DataBaseHelper.queryForScalar(sql, Integer.class,deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql, Integer.class);
				}
				responseVo.setZlds(String.valueOf(count));
			}
			responseVo.setResultCode("0");
			responseVo.setResultDesc("成功");
		} catch (Exception e) {
			logger.info("获取门诊实时情况："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.UNUSUAL, "失败"), DataUpRequestVo.class, false);
		}	
		logger.info("获取门诊实时情况："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 获取就诊情况
	 * @param param
	 * @return
	 */
	@Override
	public String getHospitalReport_Recipe_MZ(String param) {
		// TODO Auto-generated method stub
		logger.info("getHospitalReport_Recipe_MZ获取就诊情况："+param);
		DataUpResponseVo responseVo = new DataUpResponseVo();
		if (StringUtils.isBlank(param)) {
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("参数不能为空！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		DataUpRequestVo requ = (DataUpRequestVo) XmlUtil.XmlToBean(param,DataUpRequestVo.class);
		if(StringUtils.isBlank(requ.getStartDate()) || StringUtils.isBlank(requ.getEndDate())){
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("查询日期不能为空请检查！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		String itemId="";
		if(StringUtils.isNotBlank(requ.getItemId())){
			itemId=requ.getItemId();
		}
		String deptId="";
		if(StringUtils.isNotBlank(requ.getDeptId())){
			BdOuDept bdOuDept=getDeptByCode(requ.getDeptId());
			if(null==bdOuDept ||StringUtils.isBlank(bdOuDept.getPkDept())){
				responseVo.setResultCode("-1");
				responseVo.setResultDesc("科室参数无法查到科室请检查！");
				return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
			}
			deptId=bdOuDept.getPkDept();
		}
		String deptSql = "select * from BD_OU_DEPT where CODE_DEPT = ?";
		BdOuDept bdOuDept = DataBaseHelper.queryForBean(deptSql,BdOuDept.class,deptId);
		deptId = bdOuDept!=null?bdOuDept.getPkDept():deptId;
		try {
			//mzrs:门诊人数
			if(StringUtils.isBlank(itemId) || itemId.contains("mzrs")){//TO_CHAR(DATE_BEGIN,'yyyy-mm-dd')=TO_CHAR(SYSDATE,'yyyy-mm-dd') and 
				String sql="select COUNT(1) mzrs from PV_ENCOUNTER where EU_PVTYPE='1'  and EU_STATUS not in ('0','9') and TO_CHAR(DATE_BEGIN,'yyyy-mm-dd')>=? and TO_CHAR(DATE_BEGIN,'yyyy-mm-dd')<=? ";
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql=sql+" and pk_dept=?";
					count=DataBaseHelper.queryForScalar(sql, Integer.class,requ.getStartDate(),requ.getEndDate(),deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql, Integer.class,requ.getStartDate(),requ.getEndDate());
				}
				responseVo.setMzrs(String.valueOf(count));
			}
			//jzcf:急诊处方
			if(StringUtils.isBlank(itemId) || itemId.contains("jzrs")){
				responseVo.setJzcf("0");
			}
			//mzss:门诊手术
			if(StringUtils.isBlank(itemId) || itemId.contains("mzss")){
				responseVo.setMzss("0");
			}
			//cfsl:处方数
			if(StringUtils.isBlank(itemId) || itemId.contains("cfsl")){
				StringBuffer sql = new StringBuffer();
				sql.append("select COUNT(1) cfsl from CN_PRESCRIPTION c ");
				sql.append("INNER JOIN PV_ENCOUNTER pv on pv.PK_PV=c.PK_PV ");
				sql.append(" where  pv.EU_PVTYPE='1' and pv.EU_STATUS in ('1','2')  and TO_CHAR(c.CREATE_TIME,'yyyy-mm-dd')>=? and TO_CHAR(pv.DATE_BEGIN,'yyyy-mm-dd')<=? ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append(" and pv.pk_dept=?");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getStartDate(),requ.getEndDate(),deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getStartDate(),requ.getEndDate());
				}
				responseVo.setCfsl(String.valueOf(count));
			}
			//cxrs:抽血人次
			if(StringUtils.isBlank(itemId) || itemId.contains("cxrs")){
				responseVo.setCxrs("0");
			}
			//syrs:输液人次
			if(StringUtils.isBlank(itemId) || itemId.contains("syrs")){
				responseVo.setSyrs("0");
			}
			//zsrs:注射人次
			if(StringUtils.isBlank(itemId) || itemId.contains("zsrs")){
				responseVo.setZsrs("0");
			}
			//yyrs:预约人次
			if(StringUtils.isBlank(itemId) || itemId.contains("yyrs")){//TO_CHAR(DATE_BEGIN,'yyyy-mm-dd')=TO_CHAR(SYSDATE,'yyyy-mm-dd') and 
				String sql="select COUNT(1) yyrs from SCH_APPT where EU_STATUS in ('0','1') and TO_CHAR(BEGIN_TIME,'yyyy-mm-dd')>=? and TO_CHAR(BEGIN_TIME,'yyyy-mm-dd')<=? ";
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql=sql+" and PK_DEPT_EX=?";
					count=DataBaseHelper.queryForScalar(sql, Integer.class,requ.getStartDate(),requ.getEndDate(),deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql, Integer.class,requ.getStartDate(),requ.getEndDate());
				}
				responseVo.setYyrs(String.valueOf(count));
			}
			//zj:专家
			if(StringUtils.isBlank(itemId) || itemId.contains("zj")){
				responseVo.setZj(String.valueOf("0"));
				StringBuffer sql = new StringBuffer();
				sql.append("select COUNT(1) zj from CN_PRESCRIPTION c ");
				sql.append("INNER JOIN PV_ENCOUNTER pv on pv.PK_PV=c.PK_PV ");
				sql.append("left join BL_SETTLE bs on bs.PK_PV=pv.PK_PV and bs.EU_PVTYPE='1' and bs.DT_STTYPE='00' ");
				sql.append("left join BL_OP_DT bod on bod.PK_SETTLE=bs.PK_SETTLE ");
				sql.append(" where TO_CHAR(c.CREATE_TIME,'yyyy-mm-dd')>=? and TO_CHAR(c.CREATE_TIME,'yyyy-mm-dd')<=? and pv.EU_PVTYPE='1' and pv.EU_STATUS in ('1','2') ");
				sql.append(" and bod.PK_ITEM in (SELECT pk_item from BD_ITEM where DEL_FLAG='0' and FLAG_ACTIVE='1' and (name like '%主任%' or name like '%名专家%')) ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append(" and pv.pk_dept=?");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getStartDate(),requ.getEndDate(),deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getStartDate(),requ.getEndDate());
				}
				responseVo.setZj(String.valueOf(count));
			}
			//pt:普通
			if(StringUtils.isBlank(itemId) || itemId.contains("pt")){
				responseVo.setPt("0");
				StringBuffer sql = new StringBuffer();
				sql.append("select COUNT(1) pt from CN_PRESCRIPTION c ");
				sql.append("INNER JOIN PV_ENCOUNTER pv on pv.PK_PV=c.PK_PV ");
				sql.append("left join BL_SETTLE bs on bs.PK_PV=pv.PK_PV and bs.EU_PVTYPE='1' and bs.DT_STTYPE='00' ");
				sql.append("left join BL_OP_DT bod on bod.PK_SETTLE=bs.PK_SETTLE ");
				sql.append(" where TO_CHAR(c.CREATE_TIME,'yyyy-mm-dd')>=? and TO_CHAR(c.CREATE_TIME,'yyyy-mm-dd')<=? and pv.EU_PVTYPE='1' and pv.EU_STATUS in ('1','2') ");
				sql.append(" and bod.PK_ITEM in (SELECT pk_item from BD_ITEM where DEL_FLAG='0' and FLAG_ACTIVE='1' and name like '%普通门诊诊查费%') ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append(" and pv.pk_dept=?");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getStartDate(),requ.getEndDate(),deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getStartDate(),requ.getEndDate());
				}
				responseVo.setPt(String.valueOf(count));
			}
			responseVo.setResultCode("0");
			responseVo.setResultDesc("成功");
		} catch (Exception e) {
			logger.info("获取就诊情况："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.UNUSUAL, "失败"), DataUpResponseVo.class, false);
		}	
		logger.info("获取就诊情况："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 获取门诊费用月报
	 * @param param
	 * @return
	 */
	@Override
	public String getHospitalReport_Income_MZ(String param) {
		// TODO Auto-generated method stub
		logger.info("getHospitalReport_Income_MZ获取门诊费用月报："+param);
		DataUpRequestVo requ = (DataUpRequestVo) XmlUtil.XmlToBean(param,DataUpRequestVo.class);
		DataUpResponseVo responseVo = new DataUpResponseVo();
		String deptId = requ.getWardId();
		String deptSql = "select * from BD_OU_DEPT where CODE_DEPT = ?";
		BdOuDept bdOuDept = DataBaseHelper.queryForBean(deptSql,BdOuDept.class,deptId);
		deptId = bdOuDept!=null?bdOuDept.getPkDept():deptId;

		if (StringUtils.isBlank(param)) {
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("参数不能为空！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		if(StringUtils.isBlank(requ.getStartDate()) || StringUtils.isBlank(requ.getEndDate())){
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("查询日期不能为空请检查！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ( ");
			sql.append("select bic.CODE,bic.name label,a.AMOUNT value from ( ");
			sql.append("select SUBSTR(bic.code,0,2) code,sum(cg.AMOUNT) amount ");
			sql.append("from BL_OP_DT cg ");
			sql.append("INNER JOIN BD_ITEM bi on bi.pk_item=cg.pk_item  INNER JOIN BD_ITEMcate bic on bic.PK_ITEMCATE=bi.PK_ITEMCATE ");
			sql.append("INNER JOIN BL_SETTLE bs on bs.PK_SETTLE=cg.PK_SETTLE ");
			sql.append("where TO_CHAR(bs.DATE_st,'yyyy-mm-dd')>=? and TO_CHAR(bs.DATE_st,'yyyy-mm-dd')<=? and cg.FLAG_SETTLE='1' ");
			if(StringUtils.isNotBlank(deptId)){
				sql.append("  and cg.PK_DEPT_APP= '"+deptId+"'  ");

			}
			sql.append("group by SUBSTR(bic.code,0,2) ");
			sql.append(" ) a  left join BD_ITEMcate bic on bic.CODE=a.CODE ");
			sql.append(" UNION all ");
			sql.append(" select '99' code,'药费' label,sum(cg.AMOUNT) value ");
			sql.append(" from BL_OP_DT cg INNER JOIN BD_PD bi on bi.pk_pd=cg.pk_pd  ");
			sql.append("INNER JOIN BL_SETTLE bs on bs.PK_SETTLE=cg.PK_SETTLE ");
			sql.append(" where TO_CHAR(bs.DATE_st,'yyyy-mm-dd')>=? and TO_CHAR(bs.DATE_st,'yyyy-mm-dd')<=? and cg.FLAG_SETTLE='1' ");
			if(StringUtils.isNotBlank(deptId)){
				sql.append("  and cg.PK_DEPT_APP= '"+deptId+"'  ");

			}
			sql.append(" ) suma ORDER BY suma.code");
			List<RespItemsVo> list=DataBaseHelper.queryForList(sql.toString(), RespItemsVo.class,requ.getStartDate(),requ.getEndDate(),requ.getStartDate(),requ.getEndDate());
			responseVo.setItems(list);
			responseVo.setResultCode("0");
			responseVo.setResultDesc("成功");
		} catch (Exception e) {
			logger.info("获取门诊费用月报："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.UNUSUAL, "失败"), DataUpResponseVo.class, false);
		}	
		logger.info("获取门诊费用月报："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 病区信息查询接口
	 * @param param
	 * @return
	 */
	@Override
	public String getWardInfo(String param) {
		// TODO Auto-generated method stub
		logger.info("getWardInfo病区信息查询接口："+param);
		DataUpRequestVo requ = (DataUpRequestVo) XmlUtil.XmlToBean(param,DataUpRequestVo.class);
		DataUpResponseVo responseVo = new DataUpResponseVo();
		if (StringUtils.isBlank(param)) {
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("参数不能为空！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT bo.code_dept wardId, bo.name_dept wardName,bod.code_dept deptId,bod.name_dept deptName FROM BD_OU_DEPT bo ");
			sql.append("left join bd_dept_bus bds on bds.pk_dept=bo.pk_dept and bds.DT_DEPTTYPE='02' and bds.DEL_FLAG='0' ");
			sql.append("INNER JOIN bd_dept_bu bdu on bdu.PK_DEPTBU=bds.PK_DEPTBU and bdu.DT_BUTYPE='01' and bdu.DEL_FLAG='0' ");
			sql.append("left join bd_dept_bus bds1 on bds1.PK_DEPTBU=bdu.PK_DEPTBU and bds1.DT_DEPTTYPE='01' and bds1.DEL_FLAG='0' ");
			sql.append("left join BD_OU_DEPT bod on bod.pk_dept=bds1.pk_dept and bod.DEL_FLAG='0' ");
			sql.append("WHERE	bo.FLAG_ACTIVE = '1' 	AND bo.DEL_FLAG = '0' 	AND bo.DT_DEPTTYPE = '02' ");
			List<RespWardVo> wardInfo=new ArrayList<RespWardVo>();
			if(StringUtils.isNotBlank(requ.getWardId())){
				sql.append(" AND bo.code_dept = ? ");
				wardInfo=DataBaseHelper.queryForList(sql.toString(), RespWardVo.class,requ.getWardId());
			}else{
				wardInfo=DataBaseHelper.queryForList(sql.toString(), RespWardVo.class);
			}
			responseVo.setWardInfo(wardInfo);
			responseVo.setResultCode("0");
			responseVo.setResultDesc("成功");
		} catch (Exception e) {
			logger.info("getWardInfo病区信息查询接口："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.UNUSUAL, "失败"), DataUpResponseVo.class, false);
		}	
		logger.info("getWardInfo病区信息查询接口："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 获取住院日报表
	 * @param param
	 * @return
	 */
	@Override
	public String getHospitalReport_Overview_ZY(String param) {
		// TODO Auto-generated method stub
		logger.info("getHospitalReport_Overview_ZY获取住院日报表："+param);
		DataUpResponseVo responseVo = new DataUpResponseVo();
		if (StringUtils.isBlank(param)) {
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("参数不能为空！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		DataUpRequestVo requ = (DataUpRequestVo) XmlUtil.XmlToBean(param,DataUpRequestVo.class);
		String itemId="";
		if(StringUtils.isNotBlank(requ.getItemId())){
			itemId=requ.getItemId();
		}
		if(StringUtils.isBlank(requ.getDateStr())){
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("查询日期不能为空！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		String deptId="";
		if(StringUtils.isNotBlank(requ.getWardId())){
			BdOuDept bdOuDept=getDeptByCode(requ.getWardId());
			if(null==bdOuDept || StringUtils.isBlank(bdOuDept.getPkDept())){
				responseVo.setResultCode("-1");
				responseVo.setResultDesc("科室参数无法查到科室请检查！");
				return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
			}
			deptId=bdOuDept.getPkDept();
		}
		try {
			//zyrs:在院人次--没带时间
			if(StringUtils.isBlank(itemId) || itemId.contains("zyrs")){
				StringBuffer sql = new StringBuffer();
				sql.append("select count (distinct(pv.pk_pv)) as count_total from pv_encounter pv ");
				sql.append("inner join bd_res_bed bed on pv.pk_pi=bed.pk_pi and pv.bed_no=bed.code ");
				sql.append("inner join pi_master pi on pv.pk_pi = pi.pk_pi and pi.del_flag = '0' ");
				sql.append("inner join pv_ip ip on pv.pk_pv = ip.pk_pv and ip.del_flag = '0' ");
				sql.append("left join pv_infant infant on pv.pk_pv=infant.pk_pv_infant and infant.del_flag='0' ");
				sql.append("where pv.flag_cancel != '1' and pv.eu_pvtype = '3' and pv.eu_status > 0 and pv.del_flag='0' and pv.flag_in ='1' and infant.pk_pv is null ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append("and pv.PK_DEPT_NS =? ");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class);
				}
				responseVo.setZyrs(String.valueOf(count));
			}
			//ryrs:入院人次
			if(StringUtils.isBlank(itemId) || itemId.contains("ryrs")){
				StringBuffer sql = new StringBuffer();
				sql.append("select count(pk_pv) as count_in from pv_encounter ");
				sql.append("where eu_pvtype = '3' and flag_in = '1' and flag_cancel = '0' and del_flag='0' ");
				sql.append(" and TO_CHAR(date_admit,'yyyy-mm-dd')=?");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append("and PK_DEPT_NS =? ");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getDateStr(),deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getDateStr());
				}
				responseVo.setRyrs(String.valueOf(count));
			}
			//cyrs:出院人数
			if(StringUtils.isBlank(itemId) || itemId.contains("cyrs")){
				StringBuffer sql = new StringBuffer();
				sql.append("select count(pk_pv) as count_leave from pv_encounter ");
				sql.append("where TO_CHAR(date_end,'yyyy-mm-dd')=? and eu_status > 0 and flag_in = '0' and eu_pvtype = '3' and del_flag='0'  ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append("and PK_DEPT_NS =? ");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getDateStr(),deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getDateStr());
				}
				responseVo.setCyrs(String.valueOf(count));
			}
			//wzrs:危重人数--没带时间查询
			if(StringUtils.isBlank(itemId) || itemId.contains("wzrs")){
				StringBuffer sql = new StringBuffer();
				sql.append("select count(pv.pk_pv) as count_disease from pv_encounter pv ");
				sql.append("inner join pv_ip ip on ip.pk_pv = pv.pk_pv  and ip.del_flag='0' ");
				sql.append("where pv.eu_status > 0 and pv.eu_pvtype = '3'  and pv.flag_cancel != '1' and pv.bed_no is not null and pv.flag_in = '1' and ip.dt_level_dise in ('02' ,'03') and pv.del_flag='0'  ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append("and pv.PK_DEPT_NS =? ");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class);
				}
				responseVo.setWzrs(String.valueOf(count));
			}
			//ssrs:手术人数
			if(StringUtils.isBlank(itemId) || itemId.contains("ssrs")){
				StringBuffer sql = new StringBuffer();
				sql.append("select count(distinct pv.pk_pv) from pv_encounter pv ");
				sql.append("inner join cn_order ord on pv.pk_pv = ord.pk_pv ");
				sql.append("inner join cn_op_apply apply on ord.pk_cnord = apply.pk_cnord ");
				sql.append("where pv.eu_status in ('1','2','3') and ord.eu_status_ord >'1' and ord.eu_status_ord <'9' and TO_CHAR(apply.date_plan,'yyyy-mm-dd')=? ");
				Integer count=null;
				if(StringUtils.isNotBlank(deptId)){
					sql.append("and pv.PK_DEPT_NS =? ");
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getDateStr(),deptId);
				}else{
					count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class,requ.getDateStr());
				}
				responseVo.setSsrs(String.valueOf(count));
			}
			//cwsyl:床位使用率(百分比%)
			StringBuffer sql = new StringBuffer();
			sql.append("select ROUND(((select COUNT(1) from BD_RES_BED br INNER JOIN PV_ENCOUNTER pv on pv.pk_pi=br.PK_PI and pv.EU_PVTYPE=3 where br.FLAG_ACTIVE='1' and br.EU_STATUS='02' and br.DEL_FLAG='0' and TO_CHAR(pv.DATE_ADMIT,'yyyy-mm-dd')=? ");
			String str=null;
			if(StringUtils.isNotBlank(deptId)){
				sql.append("and pv.PK_DEPT_NS =? ");
				sql.append(")/(select COUNT(1) from BD_RES_BED where FLAG_ACTIVE='1' and DEL_FLAG='0')),4)*100 || '%' from dual ");
				str=DataBaseHelper.queryForScalar(sql.toString(), String.class,requ.getDateStr(),deptId);
			}else{
				sql.append(")/(select COUNT(1) from BD_RES_BED where FLAG_ACTIVE='1' and DEL_FLAG='0')),4)*100 || '%' from dual ");
				str=DataBaseHelper.queryForScalar(sql.toString(), String.class,requ.getDateStr());
			}
			responseVo.setCwsyl(str);
			
			//pjzyr:平均住院日
			
			
			responseVo.setResultCode("0");
			responseVo.setResultDesc("成功");
		} catch (Exception e) {
			logger.info("获取住院日报表："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.UNUSUAL, "失败"), DataUpRequestVo.class, false);
		}	
		logger.info("获取住院日报表："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * 获取住院费用月报
	 * @param param
	 * @return
	 */
	@Override
	public String getHospitalReport_Income_ZY(String param) {
		// TODO Auto-generated method stub
		logger.info("getHospitalReport_Income_ZY获取住院费用月报："+param);
		DataUpRequestVo requ = (DataUpRequestVo) XmlUtil.XmlToBean(param,DataUpRequestVo.class);
		DataUpResponseVo responseVo = new DataUpResponseVo();
		if (StringUtils.isBlank(param)) {
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("参数不能为空！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		if(StringUtils.isBlank(requ.getStartDate()) || StringUtils.isBlank(requ.getEndDate())){
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("查询日期不能为空请检查！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ( ");
			sql.append("select bic.CODE,bic.name label,a.AMOUNT value from ( ");
			sql.append("select SUBSTR(bic.code,0,2) code,sum(cg.AMOUNT) amount ");
			sql.append("from BL_IP_DT cg ");
			sql.append("INNER JOIN BD_ITEM bi on bi.pk_item=cg.pk_item  INNER JOIN BD_ITEMcate bic on bic.PK_ITEMCATE=bi.PK_ITEMCATE ");
			sql.append("INNER JOIN BL_SETTLE bs on bs.PK_SETTLE=cg.PK_SETTLE ");
			sql.append("where TO_CHAR(bs.DATE_st,'yyyy-mm-dd')>=? and TO_CHAR(bs.DATE_st,'yyyy-mm-dd')<=? and cg.FLAG_SETTLE='1' ");
			sql.append("group by SUBSTR(bic.code,0,2) ");
			sql.append(" ) a  left join BD_ITEMcate bic on bic.CODE=a.CODE ");
			sql.append(" UNION all ");
			sql.append(" select '99' code,'药费' label,sum(cg.AMOUNT) value ");
			sql.append(" from BL_IP_DT cg INNER JOIN BD_PD bi on bi.pk_pd=cg.pk_pd  ");
			sql.append("INNER JOIN BL_SETTLE bs on bs.PK_SETTLE=cg.PK_SETTLE ");
			sql.append(" where TO_CHAR(bs.DATE_st,'yyyy-mm-dd')>=? and TO_CHAR(bs.DATE_st,'yyyy-mm-dd')<=? and cg.FLAG_SETTLE='1' ");
			sql.append(" ) suma ORDER BY suma.code");
			List<RespItemsVo> list=DataBaseHelper.queryForList(sql.toString(), RespItemsVo.class,requ.getStartDate(),requ.getEndDate(),requ.getStartDate(),requ.getEndDate());
			responseVo.setItems(list);
			responseVo.setResultCode("0");
			responseVo.setResultDesc("成功");
		} catch (Exception e) {
			logger.info("获取住院费用月报："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.UNUSUAL, "失败"), DataUpResponseVo.class, false);
		}	
		logger.info("获取住院费用月报："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	public BdOuDept getDeptByCode(String deptCode){
		BdOuDept codeDept = DataBaseHelper.queryForBean("select * from BD_OU_DEPT where CODE_DEPT=?", BdOuDept.class,deptCode);
		return codeDept;
	}
	/**
	 * 1.1.1.9获取全院费用月报
	 * @param param
	 * @return
	 */
	@Override
	public String getHospitalReport_Income_ALL(String param) {
		// TODO Auto-generated method stub
		logger.info("getHospitalReport_Income_ALL获取全院费用月报："+param);
		DataUpRequestVo requ = (DataUpRequestVo) XmlUtil.XmlToBean(param,DataUpRequestVo.class);
		DataUpResponseVo responseVo = new DataUpResponseVo();
		if (StringUtils.isBlank(param)) {
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("参数不能为空！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		if(StringUtils.isBlank(requ.getStartDate()) || StringUtils.isBlank(requ.getEndDate())){
			responseVo.setResultCode("-1");
			responseVo.setResultDesc("查询日期不能为空请检查！");
			return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		}
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select suma.CODE,suma.label,SUM(suma.value) value from ( ");
			sql.append("select bic.CODE,bic.name label,a.AMOUNT value from ( ");
			sql.append("select SUBSTR(bic.code,0,2) code,sum(cg.AMOUNT) amount ");
			sql.append("from BL_OP_DT cg ");
			sql.append("INNER JOIN BD_ITEM bi on bi.pk_item=cg.pk_item  INNER JOIN BD_ITEMcate bic on bic.PK_ITEMCATE=bi.PK_ITEMCATE ");
			sql.append("INNER JOIN BL_SETTLE bs on bs.PK_SETTLE=cg.PK_SETTLE ");
			sql.append("where TO_CHAR(bs.DATE_st,'yyyy-mm-dd')>=? and TO_CHAR(bs.DATE_st,'yyyy-mm-dd')<=? and cg.FLAG_SETTLE='1' ");
			sql.append("group by SUBSTR(bic.code,0,2) ");
			sql.append(" ) a  left join BD_ITEMcate bic on bic.CODE=a.CODE ");
			sql.append(" UNION all ");
			sql.append(" select '99' code,'药费' label,sum(cg.AMOUNT) value ");
			sql.append(" from BL_OP_DT cg INNER JOIN BD_PD bi on bi.pk_pd=cg.pk_pd  ");
			sql.append("INNER JOIN BL_SETTLE bs on bs.PK_SETTLE=cg.PK_SETTLE ");
			sql.append(" where TO_CHAR(bs.DATE_st,'yyyy-mm-dd')>=? and TO_CHAR(bs.DATE_st,'yyyy-mm-dd')<=? and cg.FLAG_SETTLE='1' ");
			
			sql.append("UNION all select bic.CODE,bic.name label,a.AMOUNT value from ( ");
			sql.append("select SUBSTR(bic.code,0,2) code,sum(cg.AMOUNT) amount ");
			sql.append("from BL_iP_DT cg ");
			sql.append("INNER JOIN BD_ITEM bi on bi.pk_item=cg.pk_item  INNER JOIN BD_ITEMcate bic on bic.PK_ITEMCATE=bi.PK_ITEMCATE ");
			sql.append("INNER JOIN BL_SETTLE bs on bs.PK_SETTLE=cg.PK_SETTLE ");
			sql.append("where TO_CHAR(bs.DATE_st,'yyyy-mm-dd')>=? and TO_CHAR(bs.DATE_st,'yyyy-mm-dd')<=? and cg.FLAG_SETTLE='1' ");
			sql.append("group by SUBSTR(bic.code,0,2) ");
			sql.append(" ) a  left join BD_ITEMcate bic on bic.CODE=a.CODE ");
			sql.append(" UNION all ");
			sql.append(" select '99' code,'药费' label,sum(cg.AMOUNT) value ");
			sql.append(" from BL_iP_DT cg INNER JOIN BD_PD bi on bi.pk_pd=cg.pk_pd  ");
			sql.append("INNER JOIN BL_SETTLE bs on bs.PK_SETTLE=cg.PK_SETTLE ");
			sql.append(" where TO_CHAR(bs.DATE_st,'yyyy-mm-dd')>=? and TO_CHAR(bs.DATE_st,'yyyy-mm-dd')<=? and cg.FLAG_SETTLE='1' ");
			sql.append(" ) suma group by suma.CODE,suma.label ORDER BY suma.code");
			List<RespItemsVo> list=DataBaseHelper.queryForList(sql.toString(), RespItemsVo.class,requ.getStartDate(),requ.getEndDate(),requ.getStartDate(),requ.getEndDate(),requ.getStartDate(),requ.getEndDate(),requ.getStartDate(),requ.getEndDate());
			responseVo.setItems(list);
			responseVo.setResultCode("0");
			responseVo.setResultDesc("成功");
		} catch (Exception e) {
			logger.info("getHospitalReport_Income_ALL获取全院费用月报："+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.UNUSUAL, "失败"), DataUpResponseVo.class, false);
		}	
		logger.info("getHospitalReport_Income_ALL获取全院费用月报："+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
}
