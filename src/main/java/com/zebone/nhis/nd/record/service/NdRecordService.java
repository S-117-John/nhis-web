package com.zebone.nhis.nd.record.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.cn.ipdw.CnDiagDt;
import com.zebone.nhis.common.module.nd.record.NdRecord;
import com.zebone.nhis.common.module.nd.record.NdRecordDc;
import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.common.module.nd.record.NdRecordFd;
import com.zebone.nhis.common.module.nd.record.NdRecordRow;
import com.zebone.nhis.common.module.nd.record.NdRecordTitle;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.emr.nis.vo.NdRecordRowsVo;
import com.zebone.nhis.nd.pub.dao.NdPubMapper;
import com.zebone.nhis.nd.pub.vo.NdRecordRowVo;
import com.zebone.nhis.nd.pub.vo.NdRecordTitleVo;
import com.zebone.nhis.nd.record.dao.NdRecordMapper;
import com.zebone.nhis.nd.record.vo.NdRecordVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 护理文书业务操作类
 * 
 * @author yangxue
 * 
 */
@Service
public class NdRecordService {
	@Resource
	private NdRecordMapper ndRecordMapper;
	
	@Resource
	private NdPubMapper ndPubMapper;

	/**
	 * 查询患者病历列表
	 * 
	 * @param param
	 *            --pkPv
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> queryRecordList(String param, IUser user) {
		String pkPv = JsonUtil.readValue(param, String.class);
		if (CommonUtils.isEmptyString(pkPv))
			throw new BusException("未获取到患者就诊主键！");
		return ndPubMapper.queryRecordList(pkPv);
	}

	/**
	 * 查询病历格式
	 * 
	 * @param param
	 *            -- pkRecord
	 * @param user
	 * @return
	 */
	public NdRecordVo queryRecordContent(String param, IUser user) {
		String pkRecord;
		String qryDocData="1";
		try {
			Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
			pkRecord=paramMap.get("pkRecord").toString();
			
			if(!CommonUtils.isNull(paramMap.get("docData"))){
				qryDocData=paramMap.get("docData").toString();
			}
		} catch (Exception e) {
			pkRecord = JsonUtil.readValue(param, String.class);
		}

		NdRecordVo vo = null;
		if(qryDocData.equals("1")){
			vo = ndRecordMapper.queryRecordContent(pkRecord);
		}else{
			vo = ndRecordMapper.queryRecordContentNoDocData(pkRecord);
		}
			
		Integer rowNum = DataBaseHelper
				.queryForScalar(
						"select count(1) as rowCoun from nd_record_row where pk_record = ?  ",
						Integer.class, new Object[] { pkRecord });
		if (rowNum == null || rowNum < 0){
			rowNum = 0;
		}
		if(vo==null){
			return null;
		}
		vo.setRowNum(rowNum);
		return vo;
	}

	/**
	 * 查询病历记录表头数据
	 * 
	 * @param param
	 *            -- pkRecord
	 * @param user
	 * @return
	 */
	public List<NdRecordTitle> queryRecordTitleData(String param, IUser user) {
		String pkRecord = JsonUtil.readValue(param, String.class);
		if (CommonUtils.isEmptyString(pkRecord))
			throw new BusException("未获取到要查询的护理记录主键！");
		return ndRecordMapper.queryRecordTitleData(pkRecord);
	}

	/**
	 * 查询病历行记录内容列表
	 * 
	 * @param param
	 *            {pkRecord,pageNo,pageSize}
	 * @param user
	 * @return
	 */
	public List<NdRecordRowVo> queryRecordRowList(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || CommonUtils.isNull(paramMap.get("pkRecord")))
			throw new BusException("未获取到要查询的护理记录主键！");
		Integer pageNo = CommonUtils.getInteger(paramMap.get("pageNo"));
		Integer pageSize = CommonUtils.getInteger(paramMap.get("pageSize"));
		if(pageNo<=0||pageSize<=0)
			throw new BusException("您传入的页面大小或页码小于0，无法进行获取数据！");
		paramMap.put("pageBegin", (pageNo-1)*pageSize);
		if(Application.isSqlServer()){
			 return ndRecordMapper.queryRecordRowListSqlServer(paramMap);
		}else{
			paramMap.put("pageEnd", pageNo*pageSize);
			return ndRecordMapper.queryRecordRowListOracle(paramMap);
		}
		
	}

	/**
	 * 查询病历列记录内容列表
	 * 
	 * @param param
	 *            -- {pkRecord,pkRecordrow}
	 * @param user
	 * @return
	 */
	public List<NdRecordDt> queryRecordColDtList(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null
				|| CommonUtils.isEmptyString(CommonUtils.getString(paramMap
						.get("pkRecord"))))
			throw new BusException("未获取到要查询的护理记录主键！");
		return ndPubMapper.queryRecordColDtList(paramMap);
	}

	/**
	 * 根据页码查询护理记录单所有行数据
	 * 
	 * @param param
	 *            {pkRecord,pageNo,pageSize}
	 * @param user
	 * @return
	 */
	public List<NdRecordRowVo> queryRecordAllDataByPage(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || CommonUtils.isNull(paramMap.get("pkRecord")))
			throw new BusException("未获取到要查询的护理记录主键！");
		Integer pageNo = CommonUtils.getInteger(paramMap.get("pageNo"));
		Integer pageSize = CommonUtils.getInteger(paramMap.get("pageSize"));
		if(pageNo<=0||pageSize<=0)
			throw new BusException("您传入的页面大小或页码小于0，无法进行获取数据！");
		paramMap.put("pageBegin", (pageNo-1)*pageSize);
		List<NdRecordRowVo> list = new ArrayList<NdRecordRowVo>();
		if(Application.isSqlServer()){
			list = ndRecordMapper.queryRecordRowListSqlServer(paramMap);
		}else{
			paramMap.put("pageEnd", pageNo*pageSize);
			list = ndRecordMapper.queryRecordRowListOracle(paramMap);
		}
		if (list == null || list.size() <= 0)
			return null;
		List<NdRecordDt> dtlist = new ArrayList<NdRecordDt>();
		for (NdRecordRowVo row : list) {
			dtlist = new ArrayList<NdRecordDt>();
			Map<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("pkRecord", row.getPkRecord());
			pMap.put("pkRecordRow", row.getPkRecordrow());
			dtlist = ndPubMapper.queryRecordColDtList(pMap);
			row.setDtlist(dtlist);
		}
		return list;
	}
	/**
	 * 根据页码查询护理记录单所有行数据
	 * 
	 * @param param
	 *            {pkRecord,pageNo}
	 * @param user
	 * @return
	 */
	public List<NdRecordRowVo> queryRecordAllDataByPageNew(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || CommonUtils.isNull(paramMap.get("pkRecord")))
			throw new BusException("未获取到要查询的护理记录主键！");
		Integer pageNo = CommonUtils.getInteger(paramMap.get("pageNo"));
		if(pageNo<=0){
			String maxSql="select max(page_no) from nd_record_row where pk_record =?";
			pageNo=DataBaseHelper.queryForScalar(maxSql, Integer.class, paramMap.get("pkRecord"));
		}
		List<NdRecordRowVo> list = new ArrayList<NdRecordRowVo>();
		String rowListSql="select * from nd_record_row where PAGE_NO=? and pk_record =?";
		list = DataBaseHelper.queryForList(rowListSql, NdRecordRowVo.class, pageNo,paramMap.get("pkRecord"));
		if (list == null || list.size() <= 0)
			return null;
		List<NdRecordDt> dtlist = new ArrayList<NdRecordDt>();
		for (NdRecordRowVo row : list) {
			dtlist = new ArrayList<NdRecordDt>();
			Map<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("pkRecord", row.getPkRecord());
			pMap.put("pkRecordRow", row.getPkRecordrow());
			dtlist = ndPubMapper.queryRecordColDtList(pMap);
			row.setDtlist(dtlist);
		}
		return list;
	}
	/**
	 * 查询护理记录单所有行数据
	 * 
	 * @param param
	 *            {pkRecord}
	 * @param user
	 * @return
	 */
	public List<NdRecordRowVo> queryRecordAllData(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || CommonUtils.isNull(paramMap.get("pkRecord")))
			throw new BusException("未获取到要查询的护理记录主键！");
		List<NdRecordRowVo> list = ndPubMapper.queryRecordRowList(paramMap);
		if (list == null || list.size() <= 0)
			return null;
		List<NdRecordDt> dtlist = new ArrayList<NdRecordDt>();
		List<NdRecordDt> dtlistNew = new ArrayList<NdRecordDt>();
		/*for (NdRecordRowVo row : list) {
			dtlist = new ArrayList<NdRecordDt>();
			Map<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("pkRecord", row.getPkRecord());
			pMap.put("pkRecordRow", row.getPkRecordrow());
			dtlist = ndPubMapper.queryRecordColDtList(pMap);
			row.setDtlist(dtlist);
		}*/
		int rowSize=list.size();
		System.out.println("-------begin-------:"+new Date());
		List<NdRecordDt> dtListAll = ndPubMapper.queryRecordColDtList(paramMap);
		if(dtListAll!=null&&dtListAll.size()>0){
			int dtSize=dtListAll.size();
			int i=0;
			int j=0;
			for(i=0;i<list.size();i++){
				for(j=0;j<dtListAll.size();j++){
					if(dtListAll.get(j).getPkRecordrow().equals(list.get(i).getPkRecordrow())){
						dtlist.add(dtListAll.get(j));
					}
				}
				list.get(i).setDtlist(dtlist);
				dtlist = new ArrayList<NdRecordDt>();
			}
			System.out.println("-------end-------:"+new Date());
			/*int dtSize=dtListAll.size();
			int i=0;
			int j=0;
			int rowCnt=0;
			String pkRecordrow="";
			dtlist = new ArrayList<NdRecordDt>();
			for(i=0;i<dtSize;i++){
				NdRecordDt dt=dtListAll.get(i);
				String pkRowLoop=dt.getPkRecordrow();
				if(i==0){
					pkRecordrow = pkRowLoop;
					dtlist.add(dt);
				}else{
					if(pkRecordrow.equals(pkRowLoop)){
						//dtlist.add(dt);
					}else{
						//遇到不同row
						for(j=rowCnt;j<rowSize;j++){
							NdRecordRowVo row=list.get(j);
							String pk=row.getPkRecordrow();
							if(pkRecordrow.equals(pk)){
								dtlistNew=new ArrayList<NdRecordDt>();
								dtlistNew.addAll(dtlist);
								row.setDtlist(dtlistNew);
								rowCnt++;
								break;
							}
						}
						dtlist = new ArrayList<NdRecordDt>();
						pkRecordrow=dt.getPkRecordrow();
					}
					dtlist.add(dt);
				}
			}
			//遇到不同row
			for(j=rowCnt;j<rowSize;j++){
				NdRecordRowVo row=list.get(j);
				String pk=row.getPkRecordrow();
				if(pkRecordrow.equals(pk)){
					row.setDtlist(dtlist);
					rowCnt++;
					break;
				}
			}*/
		}
		
		return list;
	}

	/**
	 * 查询护理文书流文件
	 * 
	 * @param param
	 *            --pkRecord
	 * @param user
	 * @return
	 */
	public List<NdRecordFd> queryRecordFdList(String param, IUser user) {
		String pkRecord = JsonUtil.readValue(param, String.class);
		if (CommonUtils.isEmptyString(pkRecord))
			throw new BusException("未获取到要查询的护理记录主键！");
		return ndRecordMapper.queryRecordFdList(pkRecord);
	}
	/**
	 * 删除护理记录单行记录
	 * @param param{pkRecordrow,pkEmp}
	 * @param user
	 */
	public void delRecordRow(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null
				|| CommonUtils.isEmptyString(CommonUtils.getString(paramMap
						.get("pkRecordrow"))))
			throw new BusException("未获取到要删除的行主键！");
		String pk_row = CommonUtils.getString(paramMap.get("pkRecordrow"));
		if(!CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkEmp")))){
			//校验要删除的行是否是自己签署的
			String sql = "select count(1) from nd_record_row where pk_recordrow  = ? and pk_emp = ? ";
			Integer num = DataBaseHelper.queryForScalar(sql, Integer.class,new Object[] {pk_row,paramMap.get("pkEmp")});
			if (num <= 0)
				throw new BusException("该行记录不是当前操作人编写，无法删除！");
		}
		DataBaseHelper.execute("delete from nd_record_row where pk_recordrow = ? ", new Object[]{pk_row});
		DataBaseHelper.execute("delete from nd_record_dt where pk_recordrow = ? ", new Object[]{pk_row});
	}

	/**
	 * 删除护理文书及内容
	 * 
	 * @param param
	 *            {pkRecord,pkEmp}
	 * @param user
	 */
	public void delRecordData(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null
				|| CommonUtils.isEmptyString(CommonUtils.getString(paramMap
						.get("pkRecord"))))
			throw new BusException("未获取到要删除的护理记录主键！");
		if(!CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkEmp")))){
			// 校验是否存在他人编写的护理文书内容
			String sql = "select count(1) from nd_record_row  where pk_record=?  and   not pk_emp = ?";
			Integer num = DataBaseHelper.queryForScalar(sql, Integer.class,
					new Object[] { paramMap.get("pkRecord"),paramMap.get("pkEmp") });
			if (num > 0)
				throw new BusException("存在他人编写的护理记录，无法进行删除操作！");
		}
		String pkRecord = CommonUtils.getString(paramMap.get("pkRecord"));
		// 删除护理文书记录
		DataBaseHelper.execute("delete from nd_record_dt  where pk_record = ?",
				new Object[] { pkRecord });
		DataBaseHelper.execute(
				"delete from nd_record_title  where pk_record = ?",
				new Object[] { pkRecord });
		DataBaseHelper.execute("delete from nd_record_fd  where pk_record = ?",
				new Object[] { pkRecord });
		DataBaseHelper.execute(
				"delete from nd_record_row  where pk_record = ?",
				new Object[] { pkRecord });
		DataBaseHelper.execute(
				"delete from nd_record  where pk_record = ? ",
				new Object[] { pkRecord });
	}

	/**
	 * 保存或更新护理文书主体
	 * 
	 * @param param
	 *            {NdRecord}
	 * @param user
	 */
	public String saveRecord(String param, IUser user) {
		NdRecord record = JsonUtil.readValue(param, NdRecord.class);
		if (record == null) {
			throw new BusException("未获取到要保存的内容！");
		}
		// 新增
		if (CommonUtils.isEmptyString(record.getPkRecord())) {
			String pk = NHISUUID.getKeyId();
			record.setPkRecord(pk);
			ndRecordMapper.saveRecord(record);
			//DataBaseHelper.insertBean(record);
			Map<String,Object> paramMap = new HashMap<>(16);
			paramMap.put("pkRecord",record.getPkRecord());
			paramMap.put("pkPv",record.getPkPv());
			paramMap.put("status","add");
			PlatFormSendUtils.sendNursingRecordSheet(paramMap);
			return pk;
			// 更新
		} else {
			ndRecordMapper.updateRecord(record);
			Map<String,Object> paramMap = new HashMap<>(16);
			paramMap.put("pkRecord",record.getPkRecord());
			paramMap.put("pkPv",record.getPkPv());
			paramMap.put("status","update");
			PlatFormSendUtils.sendNursingRecordSheet(paramMap);
			return record.getPkRecord();
		}


	}

	/**
	 * 保存或更新护理文书表头内容
	 * 
	 * @param param
	 * @param user
	 */
	public void saveRecordTitle(String param, IUser user) {
		List<NdRecordTitle> titleList = JsonUtil.readValue(param,
				new TypeReference<List<NdRecordTitle>>() {
				});
		if (titleList == null || titleList.size() <= 0)
			throw new BusException("未获取到要保存的内容！");
		for (NdRecordTitle title : titleList) {
			if (CommonUtils.isEmptyString(title.getPkRecordTitle())) {// 新增
				DataBaseHelper.insertBean(title);
			} else {// 更新
				DataBaseHelper.updateBeanByPk(title, false);
			}
		}
	}

	/**
	 * 保存护理文书行内容
	 * 
	 * @param param
	 * @param user
	 */
	public void saveRecordRow(String param, IUser user) {
		List<NdRecordRow> rowList = JsonUtil.readValue(param,
				new TypeReference<List<NdRecordRow>>() {
				});
		if (rowList == null || rowList.size() <= 0)
			throw new BusException("未获取到要保存的内容！");
		for (NdRecordRow row : rowList) {
			if (CommonUtils.isEmptyString(row.getPkRecordrow())) {// 新增
				DataBaseHelper.insertBean(row);
			} else {// 更新
				DataBaseHelper.updateBeanByPk(row, false);
			}
		}

	}

	/**
	 * 保存护理文书列内容
	 * 
	 * @param param
	 * @param user
	 */
	public void saveRecordColDt(String param, IUser user) {
		List<NdRecordDt> dtList = JsonUtil.readValue(param,
				new TypeReference<List<NdRecordDt>>() {
				});
		if (dtList == null || dtList.size() <= 0)
			throw new BusException("未获取到要保存的内容！");
		for (NdRecordDt dt : dtList) {
			if (CommonUtils.isEmptyString(dt.getPkRecorddt())) {// 新增
				DataBaseHelper.insertBean(dt);
			} else {// 更新
				DataBaseHelper.updateBeanByPk(dt, false);
			}
		}

	}

	/**
	 * 保存或更新护理记录单所有数据
	 * 
	 * @param param
	 * @param user
	 */
	public List<NdRecordRowVo> saveRecordAllData(String param, IUser user) {
		List<NdRecordRowVo> rowList = JsonUtil.readValue(param,
				new TypeReference<List<NdRecordRowVo>>() {
				});
		if (rowList == null || rowList.size() <= 0)
			throw new BusException("未获取到要保存的内容");
		
		List<NdRecordRowVo> rowListNew = new ArrayList<NdRecordRowVo>();
		for (NdRecordRowVo row : rowList) {
			NdRecordRow vo = new NdRecordRow();
			ApplicationUtils.copyProperties(vo, row);
			NdRecordRowVo rowVo = null;
			
			List<NdRecordDt> dtListNew = new ArrayList<NdRecordDt>();
			
			List<NdRecordDt> dtList = row.getDtlist();
			if (CommonUtils.isEmptyString(vo.getPkRecordrow())) {// 新增
				String pkRow = NHISUUID.getKeyId();
				vo.setPkRecordrow(pkRow);
				vo.setFlagSign("1");
				vo.setDateSign(new Date());
				vo.setPkEmp(((User)user).getPkEmp());
				vo.setNameEmp(((User)user).getNameEmp());
				DataBaseHelper.insertBean(vo);
				for (NdRecordDt dt : dtList) {
					dt.setPkRecordrow(pkRow);
					DataBaseHelper.insertBean(dt);
					
					dtListNew.add(dt);
				}
				rowVo = new NdRecordRowVo();
				
			} else {// 修改
				DataBaseHelper.updateBeanByPk(vo, false);
				for (NdRecordDt dt : dtList) {
					if (CommonUtils.isEmptyString(dt.getPkRecorddt())) {// 新增
						dt.setPkRecordrow(row.getPkRecordrow());
						DataBaseHelper.insertBean(dt);
						
						if(rowVo==null) rowVo = new NdRecordRowVo();
						
						dtListNew.add(dt);

					} else {// 更新
						DataBaseHelper.updateBeanByPk(dt, false);
					}
					
				}
			}
			
			if(rowVo!=null){
				ApplicationUtils.copyProperties(rowVo,vo);
				rowVo.setDtlist(dtListNew);
				
				rowListNew.add(rowVo);
				
			}
		}
		
		return rowListNew;
	}

	public void saveRecordTitleData(String param, IUser user) {
		List<NdRecordTitle> rowList = JsonUtil.readValue(param,
				new TypeReference<List<NdRecordTitle>>() {
		});
		if (rowList == null || rowList.size() <= 0)
			throw new BusException("未获取到要保存的内容");
		//DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(NdRecordTitle.class),rowList);
		for (NdRecordTitle row : rowList) {
			if (CommonUtils.isEmptyString(row.getPkRecordTitle())) {// 新增
				String pk = NHISUUID.getKeyId();
				row.setPkRecordTitle(pk);
				DataBaseHelper.insertBean(row);
			} else {// 更新
				DataBaseHelper.updateBeanByPk(row,false);
			}
		}
	}

	/**
	 * 保存护理文书附件内容
	 * 
	 * @param param
	 * @param user
	 */
	public void saveRecordFd(String param, IUser user) {
		List<NdRecordFd> fdList = JsonUtil.readValue(param,
				new TypeReference<List<NdRecordFd>>() {
				});
		if (fdList == null || fdList.size() <= 0)
			throw new BusException("未获取到要保存的内容！");
		for (NdRecordFd fd : fdList) {
			if (CommonUtils.isEmptyString(fd.getPkRecordfd())) {// 新增
				fd.setPkRecordfd(NHISUUID.getKeyId());
				ndRecordMapper.saveRecordFd(fd);
			} else {// 更新
				ndRecordMapper.updateRecordFd(fd);
			}
		}
	}
	/**
	 * 计算出入量
	 * @param param{pkRecord,dateTime,hours}
	 * @param user
	 * @return
	 */
	public NdRecordRow calInAndOut(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null||CommonUtils.isNull(paramMap.get("dateTime")))
			throw new BusException("未传入正确的计算总量参数！");
		Date beginDate = DateUtils.strToDate(CommonUtils.getString(paramMap.get("dateTime")));
		//Date beginDate = new Date();
		Double hours = paramMap.get("hours") == null ? 0.00:CommonUtils.getDouble(paramMap.get("hours"));
		Double hours_new = hours * 60 * 60 * 1000;
		Date endDate = new Date(beginDate.getTime()-hours_new.longValue());//往前计算指定小时数
		paramMap.put("dateBegin", DateUtils.getDefaultDateFormat().format(endDate));
		paramMap.put("dateEnd", DateUtils.getDefaultDateFormat().format(beginDate));
		
		paramMap.put("dateBeginDate", endDate);
		paramMap.put("dateEndDate", beginDate);
		Double in = 0.0;
		Double out = 0.0;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String OutType="";
		if(Application.isSqlServer()){
			List<Map<String,Object>> mapOut = ndRecordMapper.queryOutValSqlServer(paramMap);//出量 及出量名称分
			for (Map<String, Object> map : mapOut) {
				OutType=OutType+map.get("val")+map.get("sumCnt")+"ml,";
				out =out+CommonUtils.getDouble(map.get("sumCnt"));
			}
			Map<String,Object> map = ndRecordMapper.queryInValSqlServer(paramMap);
			if(map!=null){
				in = CommonUtils.getDouble(map.get("inval"));
			}
			if(mapOut!=null){
				//out = CommonUtils.getDouble(mapOut.get("outval"));
			}
		}else{
			Map<String,Object> mapOut = ndRecordMapper.queryOutValOracle(paramMap);//出量 及出量名称
			Map<String,Object> map = ndRecordMapper.queryInValOracle(paramMap);
			if(map!=null){
				in = CommonUtils.getDouble(map.get("inval"));
			}
			if(mapOut!=null){
				out = CommonUtils.getDouble(mapOut.get("outval"));
			}
		}
		in = in==null?0.0:in;
		out = out==null?0.0:out;
		String note = hours+"小时计入量"+in+"ml"+",出量"+out+"ml";
		if(!CommonUtils.isEmptyString(OutType)){
			note=note+"其中"+OutType;
			note=note.substring(0,note.length()-1);
		}
		//String note_sub = ",其中";
		

		//if(list!=null&&list.size()>0){
			//for(Map<String,Object> map:list){
				//if(map==null)continue;
				//if(!map.containsKey("val"))continue;
				//String val = CommonUtils.getString(map.get("val"));
				//if(CommonUtils.isEmptyString(val))
				//	continue;
				//Double valout = CommonUtils.getDouble(map.get("outval"));
				//valout = valout == null ?0.0:valout;
				//total = total + valout;//计算出量总和
				//组合描述字符串
				//note_sub = note_sub +val+""+valout+"ml,";
			//}
			//note = note +",出量"+total+"ml";
			
			//if(note_sub!=null&&note_sub.length()>3)
			  // note = note + note_sub.substring(0,note_sub.length()-1);
		//}
		//插入行数据
		return insertSumRow(note,(User)user,beginDate,paramMap);
	}
	/**
	 * 计算出入量 --新,不插入数据只返回入量出量
	 * @param param{pkRecord,dateTime,hours}
	 * @param user
	 * @return
	 */
	public Map<String,Object> calInAndOutNew(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String,Object> returnMap = new HashMap<String,Object>();
		if(paramMap == null||CommonUtils.isNull(paramMap.get("dateTime")))
			throw new BusException("未传入正确的计算总量参数！");
		Date beginDate = DateUtils.strToDate(CommonUtils.getString(paramMap.get("dateTime")));
		//Date beginDate = new Date();
		Double hours = paramMap.get("hours") == null ? 0.00:CommonUtils.getDouble(paramMap.get("hours"));
		Double hours_new = hours * 60 * 60 * 1000;
		Date endDate = new Date(beginDate.getTime()-hours_new.longValue());//往前计算指定小时数
		paramMap.put("dateBegin", DateUtils.getDefaultDateFormat().format(endDate));
		paramMap.put("dateEnd", DateUtils.getDefaultDateFormat().format(beginDate));
		Double in = 0.0;
		Double out = 0.0;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String OutType="";
		if(Application.isSqlServer()){
			List<Map<String,Object>> mapOut = ndRecordMapper.queryOutValSqlServer(paramMap);//出量 及出量名称分
			for (Map<String, Object> map : mapOut) {
				OutType=OutType+map.get("val")+map.get("sumCnt")+"ml,";
				out =out+CommonUtils.getDouble(map.get("sumCnt"));
			}
			Map<String,Object> map = ndRecordMapper.queryInValSqlServer(paramMap);
			if(map!=null){
				in = CommonUtils.getDouble(map.get("inval"));
			}
			if(mapOut!=null){
				//out = CommonUtils.getDouble(mapOut.get("outval"));
			}
		}else{
			Map<String,Object> mapOut = ndRecordMapper.queryOutValOracle(paramMap);//出量 及出量名称
			Map<String,Object> map = ndRecordMapper.queryInValOracle(paramMap);
			if(map!=null){
				in = CommonUtils.getDouble(map.get("inval"));
			}
			if(mapOut!=null){
				out = CommonUtils.getDouble(mapOut.get("outval"));
			}
		}
		in = in==null?0.0:in;
		out = out==null?0.0:out;
		returnMap.put("inSum", in);
		returnMap.put("outSum", out);
		return returnMap;
	}
	/**
	 * 插入合计行
	 * @param note
	 * @param user
	 * @param beginDate
	 * @param paramMap
	 */
	private NdRecordRow insertSumRow(String note,User user,Date beginDate,Map<String,Object> paramMap){
		   //将合计行插入数据库
			NdRecordRow row = new NdRecordRow();
			String pk_row = NHISUUID.getKeyId();
			row.setPkRecordrow(pk_row);
			row.setPkRecord(CommonUtils.getString(paramMap.get("pkRecord")));
			row.setDateChk(new Date());
			row.setDateEntry(beginDate);
			row.setFlagChk("1");
			row.setFlagNote("1");
			row.setFlagPrt("0");
			row.setFlagSign("1");
			row.setNameEmp(user.getNameEmp());
			row.setNameEmpChk(user.getNameEmp());
			row.setNote(note);
			row.setPkEmp(user.getPkEmp());
			row.setPkEmpChk(user.getPkEmp());
			row.setRowno(0);
			row.setRownoParent(null);
			int cnt = DataBaseHelper.insertBean(row);
			String sql="select * from ND_RECORD_ROW where PK_RECORDROW=?";
			row=cnt>0?DataBaseHelper.queryForBean(sql, NdRecordRow.class, pk_row):null;
			return row;
	}
	
	  /**
	 * 保存护理单列头设置信息
	 * @param param{pkRecord,dateTime,hours}
	 * @param user
	 * @return
	 */
	public ArrayList<String> saveNdRecordDc(String param, IUser user)
	{
		ArrayList<String> pklst=new ArrayList<String>();
		List<NdRecordDc> ndRecordDcs = JsonUtil.readValue(param, new TypeReference<List<NdRecordDc>>() {});
		if(ndRecordDcs != null&&ndRecordDcs.size()>0)
		{
        for(int index=0;index<ndRecordDcs.size();index++){
        	NdRecordDc recorddc=ndRecordDcs.get(index);
        	if(org.jasig.cas.client.util.CommonUtils.isEmpty(recorddc.getPkRecorddc())){//新增
        		DataBaseHelper.insertBean(recorddc);        		
        	}else{//修改
        	    DataBaseHelper.updateBeanByPk(recorddc, false);
        	}
        	pklst.add(recorddc.getPkRecorddc());
        }
		}
		return pklst;
	}
	
	public void updDtVal(String param, IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		String updSql="update ND_RECORD_DT set val=null where PK_RECORD=? and COLNAME=?";
		DataBaseHelper.update(updSql, map.get("pkRecord"),map.get("colname"));
	}
	
	public void synAllergyInfoboai(String param, IUser user){
		Map map = JsonUtil.readValue(param,Map.class);
		String sql="SELECT CAST(doc.doc_xml AS xml).value ( '(//DocObjContent/NewCtrl[@de_name="+"\""+"食物"+"\""+"]/Content_Text)[1]', 'varchar(max)' ) 食物, "+
				"CAST(doc.doc_xml AS xml).value ( '(//DocObjContent/NewCtrl[@de_name="+"\""+"药物"+"\""+"]/Content_Text)[1]', 'varchar(max)' ) 药物, "+
				"CAST(doc.doc_xml AS xml).value ( '(//DocObjContent/NewCtrl[@de_name="+"\""+"其他"+"\""+"]/Content_Text)[1]', 'varchar(max)' ) 其他 "+
				"FROM EMR_MED_DOC doc where doc.PK_DOC=?";
		Map<String,Object> resultMap=DataBaseHelper.queryForMap(sql, map.get("pkDoc"));
		String allergicSql="select * from PI_ALLERGIC where pk_pi=?";
		List<PiAllergic> all=DataBaseHelper.queryForList(allergicSql, PiAllergic.class, map.get("pkPi"));
		PiAllergic pkAllergic=null;
		for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
			boolean ishas=false;
			for(PiAllergic piAllergic : all){
				if(piAllergic.getNameAl().contains("("+entry.getKey()+")") && piAllergic.getPkBu().equals(map.get("pkDoc"))){
					pkAllergic=piAllergic;
					ishas=true;
				}
			}
			if(ishas) {
				if(pkAllergic!=null && pkAllergic.getPkPial()!=null) {
					if(entry.getValue()!= null &&StringUtils.isNoneEmpty(entry.getValue().toString())) {
						pkAllergic.setNameAl("("+entry.getKey()+")"+entry.getValue());
						pkAllergic.setTs(new Date());
						DataBaseHelper.updateBeanByPk(pkAllergic);
					}else {
						DataBaseHelper.deleteBeanByPk(pkAllergic);
					}
					
				}
			}else{
				if(entry.getValue() !=null && StringUtils.isNotEmpty(entry.getValue().toString())){
					PiAllergic allergic=new PiAllergic();
					allergic.setPkPial(NHISUUID.getKeyId());
					allergic.setPkPi(map.get("pkPi").toString());
					allergic.setEuAltype("9");
					allergic.setNameAl("("+entry.getKey()+")"+entry.getValue());
					allergic.setPkBu(map.get("pkDoc").toString());
					allergic.setDateRec(new Date());
					allergic.setDateFind(new Date());
					allergic.setNote("入院评估单自动生成");
					allergic.setEuMcsrc("0");
					allergic.setDelFlag("0");
					allergic.setCreateTime(new Date());
					allergic.setTs(new Date());
					DataBaseHelper.insertBean(allergic);
				}
			}
			ishas=false;
		}
	}
	/**
	 * 更新护理记录行选中标识（生成产程图使用）
	 * @param param--{List<String> pkRowList,pkRecord}
	 * @param user
	 */
//	public void updateFlagCheck(String param,IUser user){
//		Map<String,Object> paramMap  =  JsonUtil.readValue(param, Map.class);
//		if(paramMap == null) return;
//		List<String> pkRowList = (List<String>)paramMap.get("pkRowList");
//		if(pkRowList == null || pkRowList.size()<=0) return;
//		String pkStr = "";
//		for(String pk_row :pkRowList){
//			pkStr = pkStr +"'"+pk_row+"',";
//		}
//		if(CommonUtils.isEmptyString(pkStr)||pkStr.length()<1)
//			return;
//		//Map<String,Object> paramMap = new HashMap<String,Object>();
//		paramMap.put("dateNow", new Date());
//		//paramMap.put("pkRecord", pkRecord);
//		DataBaseHelper.execute("update nd_record_row set flag_check='1',ts = :dateNow  where pk_recordrow in ("+pkStr.substring(0, pkStr.length()-1)+")",paramMap);
//		DataBaseHelper.execute("update nd_record_row set flag_check='0',ts = :dateNow  where pk_record = :pkRecord and pk_recordrow not in ("+pkStr.substring(0, pkStr.length()-1)+")",paramMap);
//	}
	/**
	 * 查询护理记录单生成产程图的行数据
	 * 
	 * @param param
	 *            {pkRecord}
	 * @param user
	 * @return
	 */
//	public List<NdRecordRowVo> queryRecordCheckData(String param, IUser user) {
//		String pkRecord = JsonUtil.readValue(param, String.class);
//		if (CommonUtils.isEmptyString(pkRecord))
//			throw new BusException("未获取到要查询的护理记录主键！");
//		
//		List<NdRecordRowVo> list = new ArrayList<NdRecordRowVo>();
//		list = ndRecordMapper.queryCheckRecordRowList(pkRecord);
//		if (list == null || list.size() <= 0)
//			return null;
//		List<NdRecordDt> dtlist = new ArrayList<NdRecordDt>();
//		for (NdRecordRowVo row : list) {
//			dtlist = new ArrayList<NdRecordDt>();
//			Map<String, Object> pMap = new HashMap<String, Object>();
//			pMap.put("pkRecord", row.getPkRecord());
//			pMap.put("pkRecordRow", row.getPkRecordrow());
//			dtlist = ndRecordMapper.queryRecordColDtList(pMap);
//			row.setDtlist(dtlist);
//		}
//		return list;
//	}
}
