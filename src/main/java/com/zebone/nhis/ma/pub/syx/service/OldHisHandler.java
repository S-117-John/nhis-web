package com.zebone.nhis.ma.pub.syx.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.cn.ipdw.ExOpSch;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.syx.dao.OldHisMapper;
import com.zebone.nhis.ma.pub.syx.vo.ExOpSchVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.dao.jdbc.LoadDataSource;
import com.zebone.platform.modules.dao.jdbc.entity.DataBaseEntity;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.FileUtils;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 中山二院旧HIS系统服务(无事务)
 * @author huanghaisheng
 *
 */
@Service
public class OldHisHandler {
	@Autowired
	private OldHisService oldHisService;
	
	@Autowired
	private OldHisMapper oldHisMapper;
	
	public Object invokeMethod(String methodName, Object... args) {
		Object result = null;
		switch (methodName) {
		case "qryOpApplys":
			//result=this.qryOpApplys(args);
			break;
		}
		return result;
	}
	
	/**
	 * 获取手术申请
	 * 此方法是提供给直接通过交易码使用(因为切换了数据源,无事务)
	 */
	public Map<String,Object>  qryOpApplys(String param,IUser user) throws Exception{
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		Map<String,Object> rtn=new HashMap<String,Object>();

		List<String> listName = new ArrayList();
		try {
			String path = LoadDataSource.class.getClassLoader().getResource("config/datasource.xml").getFile().toString();
			File file = new File(path.replace("%20", " "));
			Document doc = FileUtils.getDocument(file);
			Element rootElement = doc.getRootElement();

			if(rootElement != null){
				List<Element> list = rootElement.elements("datasource");

				for(int i=0;i<list.size();i++){
					DataBaseEntity dataBaseEntity = new DataBaseEntity();
					Element datasources = list.get(i);
					if(datasources != null){
						String dsName = datasources.attributeValue("name");
						listName.add(dsName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (paramMap != null) {
			List<ExOpSchVo> opApplyList= new ArrayList<>();
			List<ExOpSchVo> opArrangeList=null;
			
				try {
					if (listName.contains("syxip")){
						//切换数据源
						DataSourceRoute.putAppId("syxip");
						// 调用Service类查询老HIS系统数据
						opApplyList=oldHisService.qryOpApplys(paramMap);
						DataSourceRoute.putAppId("default");
					}else {
						opApplyList=oldHisService.qryOpApplysHIS(paramMap);
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new BusException("从老HIS系统获取手术申请记录失败\n"+e.getMessage());
				} finally {
					DataSourceRoute.putAppId("default");
				}
			
			try {
				String dateType=paramMap.get("dateType").toString();  
				String sDateBegin=paramMap.get("dateBegin").toString();  
				String sDateEnd=paramMap.get("dateEnd").toString();  
			    Date dateBegin=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDateBegin);  
			    Date dateEnd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDateEnd);  
			    
			    String sqlStr="select cndiag.name_cd,bd_defdoc.name dt_nae_name,cnop.pk_emp_anae,cnop.pk_emp_circul,cnop.pk_emp_scrub,ord.PK_DEPT_EXEC,op.* from ex_op_sch op INNER JOIN CN_OP_APPLY cnop on cnop.PK_CNORD=op.PK_CNORD INNER JOIN BD_CNDIAG cndiag on cndiag.pk_cndiag= cnop.pk_diag_pre INNER JOIN CN_ORDER ord ON ord.PK_CNORD = op.PK_CNORD INNER JOIN bd_defdoc on bd_defdoc.code=cnop.dt_anae and code_defdoclist='030300' where op.date_apply >= to_date(?, 'yyyymmddhh24miss') and op.date_apply <= to_date(?, 'yyyymmddhh24miss')";
			    if("plane".equals(dateType)){
			    	 sqlStr="select cndiag.name_cd,bd_defdoc.name dt_nae_name,cnop.pk_emp_anae,cnop.pk_emp_circul,cnop.pk_emp_scrub,ord.PK_DEPT_EXEC,op.* from ex_op_sch op INNER JOIN CN_OP_APPLY cnop on cnop.PK_CNORD=op.PK_CNORD INNER JOIN BD_CNDIAG cndiag on cndiag.pk_cndiag= cnop.pk_diag_pre INNER JOIN CN_ORDER ord ON ord.PK_CNORD = op.PK_CNORD INNER JOIN bd_defdoc on bd_defdoc.code=cnop.dt_anae and code_defdoclist='030300' where op.date_plan >= to_date(?, 'yyyymmddhh24miss') and op.date_plan <= to_date(?, 'yyyymmddhh24miss')";
			    }

				if (paramMap.get("codeIp") != null && !"".equals(paramMap.get("codeIp").toString())) {
					sqlStr += " and op.code_ip=? ";
					opArrangeList = DataBaseHelper.queryForList(sqlStr, ExOpSchVo.class, 
							new Object[] { DateUtils.dateToStr("yyyyMMddHHmmss",dateBegin),DateUtils.dateToStr("yyyyMMddHHmmss",dateEnd),paramMap.get("codeIp").toString()});
				}else{
					opArrangeList = DataBaseHelper.queryForList(sqlStr, ExOpSchVo.class, 
							new Object[] { DateUtils.dateToStr("yyyyMMddHHmmss",dateBegin),DateUtils.dateToStr("yyyyMMddHHmmss",dateEnd)});
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("查询手术安排记录失败");
			} finally {
				DataSourceRoute.putAppId("default");
			}
			rtn.put("opApplyList", opApplyList);
			rtn.put("opArrangeList", opArrangeList);
		}
		return rtn;
	}
	
	/**
	 * 切换数据库，查询检验中间表tExamineRequestForIP.InfectedFlag
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int qryInfectedFlag(String param,IUser user) throws Exception{
		
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);	
		String time = "2019-07-01";
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date utilDate = sdf.parse(time);
	    paramMap.put("date", utilDate);
		List<String> listName = new ArrayList();
		try {
			String path = LoadDataSource.class.getClassLoader().getResource("config/datasource.xml").getFile().toString();
			File file = new File(path.replace("%20", " "));
			Document doc = FileUtils.getDocument(file);
			Element rootElement = doc.getRootElement();

			if(rootElement != null){
				List<Element> list = rootElement.elements("datasource");

				for(int i=0;i<list.size();i++){
					DataBaseEntity dataBaseEntity = new DataBaseEntity();
					Element datasources = list.get(i);
					if(datasources != null){
						String dsName = datasources.attributeValue("name");
						listName.add(dsName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		int count = 0;
		if (paramMap != null) {
				try {
					if (listName.contains("syxlisreq")){
						//切换数据源
						DataSourceRoute.putAppId("syxlisreq");
						// 调用Service类查询老HIS系统数据
						count=oldHisMapper.qryInfectedFlag(
								paramMap);
						DataSourceRoute.putAppId("default");					
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new BusException("从老HIS系统获取中间表tExamineRequestForIP感染标志失败\n"+e.getMessage());
				} finally {
					DataSourceRoute.putAppId("default");
				}
		
		}		
		if(count>0){	
			oldHisService.updateInfectedFlag(paramMap);
		}			
		return 	count;
					
	}
	
	
}
