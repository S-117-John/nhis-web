package com.zebone.nhis.emr.nis.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.nhis.common.module.emr.mgr.EmrNsAddrec;
import com.zebone.nhis.common.module.emr.mgr.EmrNsAddrecDesign;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;
import com.zebone.nhis.common.module.nd.record.NdRecord;
import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.common.module.nd.record.NdRecordRow;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.emr.nis.dao.NsRecMapper;
import com.zebone.nhis.emr.nis.vo.NdRecordDtAndRowVo;
import com.zebone.nhis.emr.nis.vo.NdRecordRowsVo;
import com.zebone.nhis.emr.nis.vo.RecordDtAndColTextVo;
import com.zebone.nhis.emr.nis.vo.RegisInfoParam;
import com.zebone.nhis.nd.pub.vo.NdRecordRowVo;
import com.zebone.nhis.nd.pub.vo.RecordRowsAndDelPksVo;
import com.zebone.nhis.nd.record.vo.NdRecordVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 护理记录管理
 * @author yuanxinan
 *
 */
@Service
public class NsRecService {
	@Resource
	private	NsRecMapper nsRecMapper;
	private int j=1;
	/**
	 * 保存新增护理记录
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveNsAddRec(String param , IUser user){
		EmrNsAddrec emrNsAddrec = JsonUtil.readValue(param,EmrNsAddrec.class);
		ApplicationUtils.setDefaultValue(emrNsAddrec, true);
		DataBaseHelper.insertBean(emrNsAddrec);
	}


	/**
	 * 保存新增护理记录配置
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveAddRecConfig(String param , IUser user){
		List<EmrNsAddrecDesign> emrNsAddrecDesigns = JSON.parseArray(param,
				EmrNsAddrecDesign.class);
		DataBaseHelper.batchUpdate("delete from EMR_NS_ADDREC_DESIGN where PK_ADDREC_CONFIG=:pkAddrecConfig",emrNsAddrecDesigns);
		for (EmrNsAddrecDesign emrNsAddrecDesign : emrNsAddrecDesigns) {
			if(emrNsAddrecDesign.getDelFlag()==null||!emrNsAddrecDesign.getDelFlag().equals("1")){
				ApplicationUtils.setDefaultValue(emrNsAddrecDesign, true);
				DataBaseHelper.insertBean(emrNsAddrecDesign);
			}
		}
		//DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(EmrNsAddrecDesign.class), emrNsAddrecDesigns);
	}

	/**
	 * 查询医嘱（护理记录）
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryCnOrderNs(String param , IUser user) {
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);

		if(map.get("pkPv").toString().length()>32) return nsRecMapper.qryCnPatiOrder(map.get("pkPv").toString().substring(0, 32));
		return nsRecMapper.qryCnOrder(map);
	}

	/**
	 * 计算出入量
	 * 006005001006
	 * @param param{pkRecord,dateTime,hours}
	 * @param user
	 * @return
	 */
	public Map<String,Object> calInAndOutRec(String param,IUser user){		
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		Map result=new HashMap();
		if(paramMap == null||CommonUtils.isNull(paramMap.get("dateTime")))
			throw new BusException("未传入正确的计算总量参数！");
		Date beginDate = DateUtils.strToDate(CommonUtils.getString(paramMap.get("dateTime")));
		Double hours = paramMap.get("hours") == null ? 0.00:CommonUtils.getDouble(paramMap.get("hours"));
		Double hours_new = hours * 60 * 60 * 1000;
		Date endDate = new Date(beginDate.getTime()-hours_new.longValue());//往前计算指定小时数
		paramMap.put("dateBegin", DateUtils.getDefaultDateFormat().format(endDate));
		paramMap.put("dateEnd", DateUtils.getDefaultDateFormat().format(beginDate));
		Double in = 0.0;
		Double total = 0.0;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try{
		if(Application.isSqlServer()){
			list = nsRecMapper.queryOutValSqlServer(paramMap);//出量 及出量名称
			Map<String,Object> map = nsRecMapper.queryInValSqlServer(paramMap);
			if(map!=null){
				in = CommonUtils.getDouble(map.get("inval"));
			}
		}else{
			list = nsRecMapper.queryOutValOracle(paramMap);//出量 及出量名称
			Map<String,Object> map=null;
			Map<String,Object> tomap =null;
				
			 map = nsRecMapper.queryInValOracle(paramMap);//查询入量总和
		     tomap = nsRecMapper.queryToValOracle(paramMap);//查询出量总和
			
			if(map!=null){
				in = CommonUtils.getDouble(map.get("inval"));				
			}
			if(tomap!=null){
				total = CommonUtils.getDouble(tomap.get("inval"));
			}		
		}	
		total = total==null?0.0:total;
		in = in==null?0.0:in;

		String note = hours+"小时计入量"+in+"ml";	

		result.put("inSum", in);
		String note_sub = ",其中";
		

	if(list!=null&&list.size()>0){
			for(Map<String,Object> map:list){
				/*String val = CommonUtils.getString(map.get("val"));
				if(CommonUtils.isEmptyString(val))
					continue;*/
		Double valout =0.0;
				if(map!=null){
					valout = CommonUtils.getDouble(map.get("outval"));
				}
				valout = valout == null ?0.0:valout;
				total = total + valout;//计算出量总和
				//组合描述字符串
				//note_sub = note_sub +val+""+valout+"ml,";
			}
			note = note +",出量"+total+"ml";

			if(note_sub!=null&&note_sub.length()>3)
				note = note + note_sub.substring(0,note_sub.length()-1);
		}
		result.put("outSum", total);
		
		return result;
		}catch(Exception excep){			
			throw new BusException("未传入正确的入量值或出量值！");
			}
	}

	/**
	 * 保存出入量内容行和表格内容记录
	 * 006005001007
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */

	public void saveNdRecord(String param , IUser user) throws ParseException {
		 NdRecordDtAndRowVo ndVo = (NdRecordDtAndRowVo)JsonUtil.readValue(param, NdRecordDtAndRowVo.class);
		    NdRecordRow ndRecordRow = ndVo.getNdRecordRow();
		    j++;        
	       BigDecimal bigDecimal = new BigDecimal(j);
		    ndRecordRow.setPageNo(bigDecimal);
		    ndRecordRow.setPkRecord(ndVo.getPkRecord());
		    
		    ApplicationUtils.setDefaultValue(ndRecordRow, true);
		    
		//保存护理文书-内容行记录
		DataBaseHelper.insertBean(ndRecordRow);

		List<Integer> colnoArray=ndVo.getColnoArray();
		List<String> valArray=ndVo.getValArray();
		List<String> colnameArray=ndVo.getColnameArray();
	   //for(int i = 0; i <=2; i++){	
	   //	valArray.add("");	
	  // }
		
		NdRecordDt ndRecordDt=new NdRecordDt();
		ndRecordDt.setPkRecord(ndVo.getPkRecord());
		ndRecordDt.setPkRecordrow(ndRecordRow.getPkRecordrow());
		//批量保存护理文书-文书内容
		for (int i = 0; i <=valArray.size()-1; i++) {
			ApplicationUtils.setDefaultValue(ndRecordDt, true);				
			if(StringUtils.isBlank(valArray.get(i))){
				ndRecordDt.setColno(colnoArray.get(i));	
				ndRecordDt.setColname(colnameArray.get(i));
				ndRecordDt.setVal2("#total");	
				if(colnameArray.get(i).equals("hsqm")){
					ndRecordDt.setVal(user.getUserName());					
				}else{
					ndRecordDt.setVal(null);
				}
				DataBaseHelper.insertBean(ndRecordDt);	
			}else{	
				ndRecordDt.setColno(colnoArray.get(i));	
				ndRecordDt.setColname(colnameArray.get(i));
				ndRecordDt.setVal(valArray.get(i));
				ndRecordDt.setVal2("#total");	
				DataBaseHelper.insertBean(ndRecordDt);	
			}
				
		}	

	}

	/**
	 * 更新护理记录单 审核状态字段
	 * @param param
	 * @param user
	 * @throws ParseException
	 */
	public void updateNdRecord(String param , IUser user) throws ParseException {
		NdRecordVo ndVo = (NdRecordVo) JsonUtil.readValue(param, NdRecordVo.class);

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("pkRecord", ndVo.getPkRecord());
		map.put("chkStatus", ndVo.getChkStatus());
		map.put("reviewNote",ndVo.getReviewNote());

		String sql="update ND_RECORD set CHK_STATUS = :chkStatus,REVIEW_NOTE = :reviewNote  where  PK_RECORD=:pkRecord";
		DataBaseHelper.update(sql,map);
	}

	/**
	 * 查询护理记录单所有行数据
	 * 
	 * @param param
	 * {pkRecord dateBegin dateEnd}
	 * @param user
	 * @return
	 */
	public List<NdRecordRowsVo> queryRecordAllDataByDate(String param, IUser user) {
		Map<String, String> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || CommonUtils.isNull(paramMap.get("pkRecord")))
			throw new BusException("未获取到要查询的护理记录主键！");

		paramMap.put("dateBegin", paramMap.get("dateBegin"));
		paramMap.put("dateEnd", paramMap.get("dateEnd"));
		List<NdRecordRowsVo> list = nsRecMapper.queryRecordRowList(paramMap);
		if (list == null || list.size() <= 0)
			return null;
		List<NdRecordDt> dtlist = new ArrayList<NdRecordDt>();
		List<NdRecordDt> dtlistNew = new ArrayList<NdRecordDt>();
//		for (NdRecordRowsVo row : list) {
//			dtlist = new ArrayList<NdRecordDt>();
//			Map<String, String> pMap = new HashMap<String, String>();
//			pMap.put("pkRecord", row.getPkRecord());
//			pMap.put("pkRecordRow", row.getPkRecordrow());
//			dtlist = nsRecMapper.queryRecordColDtList(pMap);
//			row.setDtlist(dtlist);
//		}
//		List<NdRecordDt> dtListAll = nsRecMapper.queryRecordColDtListNew(paramMap);
//		if(dtListAll!=null&&dtListAll.size()>0){
//			for (NdRecordRowsVo row : list) {
//				dtlist = new ArrayList<NdRecordDt>();
//				for (NdRecordDt dt : dtListAll) {
//					if(dt.getPkRecord().equals(row.getPkRecord())&&dt.getPkRecordrow().equals(row.getPkRecordrow())){
//						dtlist.add(dt);
//					}
//				}
//				row.setDtlist(dtlist);
//			}
//		}
		int rowSize=list.size();
		List<NdRecordDt> dtListAll = nsRecMapper.queryRecordColDtListNew(paramMap);
		if(dtListAll!=null&&dtListAll.size()>0){
			int dtSize=dtListAll.size();
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
							NdRecordRowsVo row=list.get(j);
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
				NdRecordRowsVo row=list.get(j);
				String pk=row.getPkRecordrow();
				if(pkRecordrow.equals(pk)){
					row.setDtlist(dtlist);
					rowCnt++;
					break;
				}
			}
		}
		
		return list;
	}

	/**
	 * 保存或更新护理记录单所有数据
	 * 006005001009
	 * @param param
	 * @param user
	 */
	public void saveRecordAllDataNoRowNo(String param, IUser user) {
		RecordRowsAndDelPksVo reAndDelPksVo=JsonUtil.readValue(param, RecordRowsAndDelPksVo.class);
		List<NdRecordRowVo> rowList = reAndDelPksVo.getNdRows();
		delRecordAllData(reAndDelPksVo.getPkRecordrows());
		String xm = null;
		if (rowList == null || rowList.size() <= 0)
			throw new BusException("未获取到要保存的内容");
		for (NdRecordRowVo row : rowList) {
		
			NdRecordRow vo = new NdRecordRow();
			ApplicationUtils.copyProperties(vo, row);
			List<NdRecordDt> dtList = row.getDtlist();								
			//if (CommonUtils.isEmptyString(vo.getPkRecordrow())) {// 新增
			String pkRow = NHISUUID.getKeyId();
			vo.setPkRecordrow(pkRow);
			vo.setFlagSign("1");
			vo.setDateSign(new Date());
			vo.setPkEmp(((User)user).getPkEmp());
			vo.setNameEmp(((User)user).getNameEmp());
			DataBaseHelper.insertBean(vo);		
			for (NdRecordDt dt : dtList) {
				dt.setPkRecordrow(pkRow);
				if(StringUtils.isBlank(dt.getVal())){
					if(dt.getColname()=="hsqm"||dt.getColname().equals("hsqm")){
						dt.setVal(xm);					
					}else {
						dt.setVal(null);
					}									
				}
				if(dt.getColname()=="hsqm"||dt.getColname().equals("hsqm")){
					xm=dt.getVal();
				}
				
				DataBaseHelper.insertBean(dt);
			}
			//} 
		}
	}

	/**
	 * 创建护理主记录
	 * 
	 * @param param
	 * @param user
	 */
	public Map<String,Object> saveNdRecordIaNull(String param, IUser user) {
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String,Object> result =new HashMap<String, Object>();
		String pkPv=paramMap.get("pkPv");
		String pkTemplate=paramMap.get("pkTemplate");
		NdRecord ndRecord=DataBaseHelper.queryForBean("select * from nd_record where pk_pv=? and pk_template=? and del_flag='0'", NdRecord.class, pkPv,pkTemplate);
		//判断是否已存在护理主记录
			if(ndRecord==null){
				ndRecord=new NdRecord();
				ndRecord.setPkPv(pkPv);
				ndRecord.setPkTemplate(pkTemplate);
				ndRecord.setDateBegin(new Date());
				ndRecord.setPkEmp(((User) user).getPkEmp());
				ndRecord.setNameEmp(((User) user).getNameEmp());
				ApplicationUtils.setDefaultValue(ndRecord, true);
				DataBaseHelper.insertBean(ndRecord);
				result.put("pkRecord",ndRecord.getPkRecord());

		}else{
			result.put("pkRecord",ndRecord.getPkRecord());
				EmrMedRec emrMedRec = DataBaseHelper.queryForBean("select * from emr_med_rec where  pk_pv=? and pk_tmp=? and del_flag='0'", EmrMedRec.class, pkPv,pkTemplate);
				if(emrMedRec!=null){
					EmrMedDoc emrMedDoc=nsRecMapper.queryEmrMedDoc(emrMedRec.getPkDoc());
				result.put("docData",emrMedDoc.getDocData());
				result.put("pkRec",emrMedRec.getPkRec());
			}

		}

		return result;
	}


	/**
	 * 批量删除护理文书表格信息
	 * 
	 * @param param
	 * @param user
	 */
	public void delRecordAllData(String param, IUser user) {
		List<String> pkRecordrows = JSON.parseArray(param, String.class);
		delRecordAllData(pkRecordrows);
	}

	public void delRecordAllData(List<String> pkRecordrows) {
		if(pkRecordrows!=null&&pkRecordrows.size()>0){
			for (String pkRecordrow : pkRecordrows) {
				DataBaseHelper.execute("delete from nd_record_row where pk_recordrow=?", new Object[]{pkRecordrow});

				DataBaseHelper.execute("delete from nd_record_dt where pk_recordrow=?", new Object[]{pkRecordrow});
			}
		}
	}
	/**
	 * 根据Pkpv查询挂号信息
	 * @param param
	 * @param user
	 */
	public List<RegisInfoParam> queryRegisInfo(String param, IUser user) {
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		List<RegisInfoParam> regis=nsRecMapper.queryRegisInfo(paramMap.get("pkPv"));
		if(StringUtils.isNotBlank(paramMap.get("pkPv"))){
			return regis;
		}
		return null;
	}

	/**
	 * 根据Pkpv护理记录单
	 * 006005001013
	 * @param param
	 * @param user
	 */
	public List<Map<Object,Object>> queryNdRecordInfo(String param, IUser user) {
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<Object,Object>> result=new ArrayList<Map<Object,Object>>();

		if(StringUtils.isNotBlank(paramMap.get("pkPv"))&&StringUtils.isNotBlank(paramMap.get("pkTemplate"))){
			String pkOrg = ((User) user).getPkOrg();
			//查询护理主记录
			NdRecord ndRecord= DataBaseHelper.queryForBean("select * from nd_record where pk_pv=? and pk_template=? and pk_org=? and del_flag='0'", NdRecord.class,paramMap.get("pkPv"),paramMap.get("pkTemplate"),pkOrg);
			String pkRecord =null;
			if(ndRecord!=null){
				pkRecord=ndRecord.getPkRecord();
			}
			if(StringUtils.isNotBlank(pkRecord)){
				//查询护理行记录,取第一行体征
				List<NdRecordRow> ndRecordRows = DataBaseHelper.queryForList("select * from nd_record_row where pk_record=? and del_flag='0' and rowno='1' order by date_entry", NdRecordRow.class,pkRecord);
				if(ndRecordRows!=null && ndRecordRows.size()>0){
					for (NdRecordRow ndRecordRow : ndRecordRows) {
						Map map=new HashMap();
						//查询文书内容
						List<RecordDtAndColTextVo> ndRecordDts = nsRecMapper.queryNdRecordAndColText(pkRecord,ndRecordRow.getPkRecordrow());


						DateFormat format = new SimpleDateFormat("yyyyMMddHHmm"); 
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						String dateEntry=format.format(ndRecordRow.getDateEntry());
						Date end=ndRecordRow.getDateEntry();
						Date start=ndRecordRow.getDateEntry();
						try {
							end = sdf.parse(dateEntry+"59");
							start=sdf.parse(dateEntry+"00");
						} catch (ParseException e) {
							throw new BusException("查询相同时间段出入量时间转化失败！");
						}

						//查询同一时段中的行记录
						List<NdRecordRow> oneTimeRows = DataBaseHelper.queryForList("select * from nd_record_row where date_Entry<=? and date_Entry>=? and pk_record=? and del_flag='0' order by date_entry", NdRecordRow.class,end,start,pkRecord);

						RecordDtAndColTextVo inDt=new RecordDtAndColTextVo();
						RecordDtAndColTextVo outDt=new RecordDtAndColTextVo();
						inDt.setContrastField("所有入量");
						outDt.setContrastField("所有出量");
						String inVal="";
						String outVal="";
						for (int x = 0; x < oneTimeRows.size(); x++) {
							NdRecordRow oneTimeRow =oneTimeRows.get(x);
							//入量相关信息
							List<RecordDtAndColTextVo> inDts = DataBaseHelper.queryForList("select * from nd_record_dt where colname in ('rldw','rlmc','rlz') and pk_recordrow=? and del_flag='0'", RecordDtAndColTextVo.class,oneTimeRow.getPkRecordrow());
							if(inDts!=null&&inDts.size()>0){

								String rlz="";
								String rldw="";
								String rlmc="";
								for (int i = 0; i < inDts.size(); i++) {
									RecordDtAndColTextVo ndRecordDt=inDts.get(i);
									if(ndRecordDt.getVal()!=null){
										if(ndRecordDt.getColname().equals("rlz")){
											rlz=ndRecordDt.getVal();
										}else if(ndRecordDt.getColname().equals("rldw")){
											BdUnit bdUnit=DataBaseHelper.queryForBean("select * from bd_unit where pk_unit=? and del_flag='0'", BdUnit.class, ndRecordDt.getVal());
											if(bdUnit!=null){
												rldw=bdUnit.getName();
											}
										}else if(ndRecordDt.getColname().equals("rlmc")){
											BdPd bdPd=DataBaseHelper.queryForBean("select * from bd_pd where pk_pd=? and del_flag='0'", BdPd.class, ndRecordDt.getVal());
											if(bdPd!=null){
												rlmc=bdPd.getName();
											}
										}
									}
								}
								if(StringUtils.isNotBlank(rlz+rldw+rlmc)){
									if(x==oneTimeRows.size()-1){
										inVal+=rlz+rldw+rlmc;
									}else{
										inVal+=rlz+rldw+rlmc+",";
									}
								}

							}

							//出量相关信息
							List<RecordDtAndColTextVo> outDts = DataBaseHelper.queryForList("select * from nd_record_dt where colname in ('clmc','clz','cldw') and pk_recordrow=? and del_flag='0'", RecordDtAndColTextVo.class,oneTimeRow.getPkRecordrow());

							if(outDts!=null&&outDts.size()>0){

								String clmc="";
								String clz="";
								String cldw="";
								List<BdDefdoc> bdDefdocs = DataBaseHelper.queryForList("select * from bd_defdoc where code in ('1','2','3') and code_defdoclist='000212' and del_flag='0' order by code", BdDefdoc.class);

								for (int i = 0; i < outDts.size(); i++) {
									NdRecordDt ndRecordDt=outDts.get(i);

									if(ndRecordDt.getVal()!=null){
										if(ndRecordDt.getColname().equals("clmc")){
											for (BdDefdoc bdDefdoc : bdDefdocs) {
												if(ndRecordDt.getVal().equals(bdDefdoc.getCode())){
													clmc=bdDefdoc.getName();
													break;
												}
											}
										}else if(ndRecordDt.getColname().equals("clz")){
											clz=ndRecordDt.getVal();
										}else if(ndRecordDt.getColname().equals("cldw")){
											BdUnit bdUnit=DataBaseHelper.queryForBean("select * from bd_unit where pk_unit=? and del_flag='0'", BdUnit.class, ndRecordDt.getVal());
											if(bdUnit!=null){
												cldw=bdUnit.getName();
											}
										}

									}

								}
								if(StringUtils.isNotBlank(clmc+clz+cldw)){
									if(x==oneTimeRows.size()-1){
										outVal+=clmc+clz+cldw;
									}else{
										outVal+=clmc+clz+cldw+",";
									}
								}

							}
						}
						inDt.setVal(inVal);
						outDt.setVal(outVal);
						ndRecordDts.add(inDt);
						ndRecordDts.add(outDt);
						map.put(ndRecordRow.getDateEntry(), ndRecordDts);
						result.add(map);
					}
				}


			}
		}

		return result;
	}

	/**
	 * 删除新增护理记录配置
	 * 
	 * @param param
	 * @param user
	 */
	public void delAddrecData(String param, IUser user) {
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		if(StringUtils.isNotBlank(paramMap.get("pkAddrec"))){
			DataBaseHelper.execute("delete from EMR_NS_ADDREC_DESIGN where PK_ADDREC=?", new Object[]{paramMap.get("pkAddrec")});

			DataBaseHelper.execute("delete from EMR_NS_ADDREC where PK_ADDREC=?", new Object[]{paramMap.get("pkAddrec")});
		}
	}

	/**
	 * 查询护理记录
	 * 
	 * @param param
	 * @param user
	 */
	public Map<String,Object> queryNdRecord(String param, IUser user) {
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String,Object> result =new HashMap<String, Object>();
		String pkPv=paramMap.get("pkPv");
		String pkTemplate=paramMap.get("pkTemplate");
		NdRecord ndRecord=DataBaseHelper.queryForBean("select * from nd_record where pk_pv=? and pk_template=? and del_flag='0'", NdRecord.class, pkPv,pkTemplate);
			EmrMedRec emrMedRec = DataBaseHelper.queryForBean("select * from emr_med_rec where  pk_pv=? and pk_tmp=? and del_flag='0'", EmrMedRec.class, pkPv,pkTemplate);
			if(emrMedRec!=null){
				EmrMedDoc emrMedDoc=nsRecMapper.queryEmrMedDoc(emrMedRec.getPkDoc());
				result.put("docData",emrMedDoc.getDocData());
				result.put("pkRec",emrMedRec.getPkRec());

		}

		return result;
	}
	
	/**
	 * 更新预检分诊体征信息
	 * 006005001017
	 * @param param
	 * @param user
	 */
	public void updateSignInfo(String param, IUser user) {
		RecordRowsAndDelPksVo reAndDelPksVo=JsonUtil.readValue(param, RecordRowsAndDelPksVo.class);
		List<NdRecordRowVo> rowList = reAndDelPksVo.getNdRows();

		delRecordAllData(reAndDelPksVo.getPkRecordrows());
		if (rowList == null || rowList.size() <= 0)
			throw new BusException("未获取到要保存的内容");

		NdRecordDt recordDt = DataBaseHelper.queryForBean("select * from nd_record_dt where val2='预检分诊' and del_flag='0' and pk_record=?",NdRecordDt.class,reAndDelPksVo.getNdRows().get(0).getPkRecord());
		
		for (NdRecordRowVo row : rowList) {//这里只有一行
			NdRecordRow vo = new NdRecordRow();
			ApplicationUtils.copyProperties(vo, row);
			List<NdRecordDt> dtList = row.getDtlist();
			if (recordDt==null) {// 新增
				String pkRow = NHISUUID.getKeyId();
				vo.setPkRecordrow(pkRow);
				vo.setFlagSign("1");
				vo.setDateSign(new Date());
				vo.setPkEmp(((User)user).getPkEmp());
				vo.setNameEmp(((User)user).getNameEmp());
				DataBaseHelper.insertBean(vo);
				for (NdRecordDt dt : dtList) {
					dt.setPkRecordrow(pkRow);
					if(StringUtils.isBlank(dt.getVal())){
						dt.setVal(null);
					}
	
					DataBaseHelper.insertBean(dt);
				}
			} else{
				DataBaseHelper.execute("delete from nd_record_dt where pk_recordrow=?", recordDt.getPkRecordrow());
				for (NdRecordDt dt : dtList) {
					dt.setPkRecordrow(recordDt.getPkRecordrow());
					if(StringUtils.isBlank(dt.getVal())){
						dt.setVal(null);
					}
					DataBaseHelper.insertBean(dt);
				}
			}
		}
	}
	
	/**
	 * 根据pkPV获取护理记录管路信息
	 * 006005001019
	 * @param param{pkRecord,dateTime,hours}
	 * @param user
	 * @return
	 */
	public String queryRecordGlInfo(String param , IUser user) {
		Map<String,String> map = JsonUtil.readValue(param, Map.class);

		String result=null;
		NdRecord ndRecord=DataBaseHelper.queryForBean("select * from ND_RECORD where pk_pv=? and del_flag='0'", NdRecord.class, map.get("pkPv"));
		if(ndRecord!=null){
			NdRecordRow ndRecordRow=DataBaseHelper.queryForBean("select * from ND_RECORD_ROW where pk_record=? and del_flag='0' order by create_time desc", NdRecordRow.class, ndRecord.getPkRecord());
			if(ndRecordRow!=null){
				NdRecordDt ndRecordDt=DataBaseHelper.queryForBean("select * from ND_RECORD_Dt where pk_recordrow=? and del_flag='0' and colname='gl'", NdRecordDt.class, ndRecordRow.getPkRecordrow());
				if(ndRecordDt!=null){
					result=ndRecordDt.getVal();
				}
			}
		}
		return result;
	}
	
	/**
	 * 交班小结
	 * 006005001020
	 * @param param
	 * @param user
	 */
     public	Map<String,Object> HandoverdutyRec(String param,IUser user){
    	 Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	 if(paramMap == null||CommonUtils.isNull(paramMap.get("begin")))
 			throw new BusException("未传入正确的计算总量参数！");
    		Date beginDate = DateUtils.strToDate(CommonUtils.getString(paramMap.get("begin")));
    		Date endDate = DateUtils.strToDate(CommonUtils.getString(paramMap.get("end")));
    	
    		paramMap.put("dateBegin", DateUtils.getDefaultDateFormat().format(beginDate));
    		paramMap.put("dateEnd", DateUtils.getDefaultDateFormat().format(endDate));
    		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    		String tw=null;
    		String hx=null;
    		String xl=null;
    		String spo=null;
    		String cvp=null;
    	  Map<String,Object> twmap = nsRecMapper.queryTwValOracle(paramMap);//查询体温
    	  Map<String,Object> hxmap = nsRecMapper.queryHxValOracle(paramMap);//查询呼吸
    	  Map<String,Object> xlmap = nsRecMapper.queryXlValOracle(paramMap);//查询心率
    	  Map<String,Object> spomap = nsRecMapper.querySpoValOracle(paramMap);//查询spo

    		Map result=new HashMap();
    		if(twmap!=null ||hxmap!=null||xlmap!=null ||spomap!=null){
				tw = CommonUtils.getString(twmap.get("tw"));
				hx = CommonUtils.getString(hxmap.get("hx"));
				xl = CommonUtils.getString(xlmap.get("xl"));
				spo = CommonUtils.getString(spomap.get("spo"));					
			}	
    		
    		//tw = tw==null?null:tw;
    		//hx = hx==null?null:hx;
    		//xl = xl==null?null:xl;
    		//spo = spo==null?null:spo;
    		
    		result.put("tw", tw);
    		
    		result.put("hx", hx);
    		
    		result.put("xl", xl);
    		
    		result.put("spo", spo);
    		
    		result.put("cvp", cvp );
		return result;}
     
     public List<EmrPatList> queryPatListByLaborDept(String param, IUser user) throws ParseException {
    	Map map = JsonUtil.readValue(param, Map.class);
    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
    	if(map.get("beginDateOut")!=null && map.get("endDateOut")!=null) {
    		Date dateBegin=sf.parse(map.get("beginDateOut").toString());
    		Date dateEnd=sf.parse(map.get("endDateOut").toString());
    		sf = new SimpleDateFormat("yyyy-MM-dd");
    		sf.format(dateBegin);
    		map.put("dateBegin", sf.format(dateBegin));
    		map.put("dateEnd", sf.format(dateEnd));
    	}
 		List<EmrPatList> rtnList = nsRecMapper.queryPatListByLaborDept(map);
 		return rtnList;
     }
     public Map<String, Object> queryBabyInfo(String param, IUser user){
    	 Map<String, Object> map = JsonUtil.readValue(param, Map.class);
    	 Map<String,Object> result = new HashMap<>();
    	 if(!map.containsKey("type") || !map.containsKey("pkPv")){return null;}
    	 if(map.get("type").equals("0"))//为0时查询分娩记录
    	 {
    		 result=nsRecMapper.queryPvLaborRec(map.get("pkPv").toString());
    	 }else{
    		 result= nsRecMapper.queryPvInfant(map);//查询新生儿记录
    		 if(result!=null){
    			//查询新生儿评分记录
        		 List<Map<String,Object>> gradelist=nsRecMapper.queryPvInfantGrade(map.get("pkInfant").toString());
        		 if(gradelist!=null && gradelist.size()>0)
        		 {
        			 for(int i=0;i<gradelist.size();i++){
        				 Map<String,Object> grade=gradelist.get(i);
        				 result.put("grade"+i+"1", grade.get("scoreOne"));
        				 result.put("grade"+i+"2", grade.get("scoreFive"));
        				 result.put("grade"+i+"3", grade.get("scoreTen"));
        			 }
        		 }
    		 }
    	 }
    	 return result;
     }
}

