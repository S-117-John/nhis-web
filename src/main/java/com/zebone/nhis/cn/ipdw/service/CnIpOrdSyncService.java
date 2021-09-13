package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.support.json.JSONUtils;
import com.zebone.nhis.cn.ipdw.dao.CnOrderSyncMapper;
import com.zebone.nhis.cn.ipdw.vo.CnBlIpDtVo;
import com.zebone.nhis.cn.ipdw.vo.PvDiagVo;
import com.zebone.nhis.cn.ipdw.vo.ReqTmpItemVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnOrderSync;
import com.zebone.nhis.common.module.ma.tpi.ems.JgPacsReq;
import com.zebone.nhis.common.module.ma.tpi.ems.YjSq01;
import com.zebone.nhis.common.module.ma.tpi.ems.YjSq02;
import com.zebone.nhis.common.module.ma.tpi.ems.YsZyJbzd;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.PinyinUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

/**
 * 住院医嘱同步存储服务
 * @author chengjia
 *
 */
@Service
public class CnIpOrdSyncService {
	@Autowired
	private CnOrderSyncMapper cnOrderSyncMapper ;
	
	/**同步操作中间库
	 * @param list
	 * @param u
	 * @param optType
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public void saveOrderSync(List<CnOrderSync> list,User u,String optType) {
		if(list==null||list.size()==0) return;
		
		if(optType.equals("D")){
			//删除未处理的数据
			DataBaseHelper.batchUpdate("delete from CN_ORDER_SYNC where pk_cnord=:pkCnord and eu_status_proc='0'",list);
		}
		
		//if(optType.equals("N")||optType.equals("C")||optType.equals("S")||optType.equals("O")){
		Date now=new Date();
		for (CnOrderSync ord : list) {
			ord.setDateOper(now);
			ord.setCodeEmp(u.getCodeEmp());
			ord.setNameEmp(u.getNameEmp());
			ord.setEuStatusProc("0");
			ord.setOptType(optType);
			ord.setPkCnordSync(NHISUUID.getKeyId());
			//ord.setOrderNo(ord.getOrdsn());
			//ord.setOrderGroupNo(ord.getOrdsnParent());
		}
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrderSync.class), list);
		
		Map<String,Object> map = new HashMap<String,Object>();
		String codePv=list.get(0).getCodePv();
		map.put("codePv", codePv);
		map.put("optType", optType);
		//调用同步存储过程
		cnOrderSyncMapper.syncProcOrders(map);
		String rtnCode=map.get("rtnCode")==null?"0":map.get("rtnCode").toString();
		if(!rtnCode.equals("")&&!rtnCode.equals("0")){
			String rtnMsg=map.get("rtnMsg")==null?"":map.get("rtnMsg").toString();
			throw new BusException(rtnMsg);  
		}
		//System.out.println("save------saveOrderSync----end");
	}

	//判断是否可以取消签署
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public String checkCancelSign(List<CnBlIpDtVo> cnOrds, User u,String codePv) {
		String rtnStr="";
//		//判断是否可以取消签署开关（目前只有急诊项目在用）
//		String checkCancelStopSwitch=ApplicationUtils.getPropertyValue("sync.check.cancelStop.switch", "0");
//		if(checkCancelStopSwitch!=null&&checkCancelStopSwitch.equals("1")){
			List<Integer> ordsns = new ArrayList<>();
			for (CnBlIpDtVo order : cnOrds) {
				ordsns.add(order.getOrderNo());
			}
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("zyh", codePv);
			paramMap.put("ordsns", ordsns);
	
			List<CnOrder> list=cnOrderSyncMapper.querySyncOrderStatus(paramMap);
			if(list!=null&&list.size()>0){
				String str="";
				for (CnOrder ord : list) {
					if(str.equals("")){
						str=ord.getNameOrd();
					}else{
						str=str+"/r/n"+ord.getNameOrd();
					}
				}
				rtnStr="医嘱状态改变，以下医嘱不能取消签署:\r\n"+str;
			}
		//}
		
		return rtnStr;
	}
	
	//判断是否可以取消停止
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public String checkCancelStop(List<CnBlIpDtVo> cnOrds, User u,String codePv) {
		String rtnStr="";
		List<Integer> ordsns = new ArrayList<>();
		for (CnBlIpDtVo order : cnOrds) {
			ordsns.add(order.getOrderNo());
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("zyh", codePv);
		paramMap.put("ordsns", ordsns);

		List<CnOrder> list=cnOrderSyncMapper.querySyncOrderStops(paramMap);
		if(list!=null&&list.size()>0){
			String str="";
			for (CnOrder ord : list) {
				if(str.equals("")){
					str=ord.getNameOrd();
				}else{
					str=str+"/r/n"+ord.getNameOrd();
				}
			}
			rtnStr="医嘱状态改变，以下医嘱不能取消停止:\r\n"+str;
		}
		//rtnStr="医嘱状态改变，以下医嘱不能取消停止！！！";
		return rtnStr;
	}
	
	/**同步诊断信息
	 * @param diagList
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public void saveDiagInfoSync(List<PvDiagVo> diagList,User u) {
		if(diagList==null||diagList.size()==0) return;
		String codePv=diagList.get(0).getCodePv();
		String codeIp=diagList.get(0).getCodeIp();
		List<YsZyJbzd> list = new ArrayList<YsZyJbzd>();
		String diagAdmit="";
		//删除未处理的数据
		for (PvDiagVo pvDiag : diagList) {
			//his.dt_getmax_tran('YS_ZY_JBZD',l_jlbh)
			//Map<String,Object> map = new HashMap<String,Object>();
			//map.put("code", "YS_ZY_JBZD");
			
			//cnOrderSyncMapper.createSyncHisSn(map);
			//Object sn=map.get("sn");
			//if(sn==null) continue;
			if(pvDiag.getDtDiagtype()!=null&&pvDiag.getDtDiagtype().equals("0100")){
				if(diagAdmit.equals("")||pvDiag.getFlagMaj()!=null&&pvDiag.getFlagMaj().equals("1")){
					diagAdmit=pvDiag.getNameDiag()==null?pvDiag.getDescDiag():pvDiag.getNameDiag();
				}
			}
			YsZyJbzd hisDiag=new YsZyJbzd();
//			String no = createSyncHisSn("YS_ZY_JBZD");
//			System.out.println("no:"+no);
			hisDiag.setJlbh(pvDiag.getSn().toString());
			hisDiag.setJzhm(codePv);
			hisDiag.setBrbh(codeIp);//病案号码
			hisDiag.setZdlb(pvDiag.getDiagtypeName());//诊断分类
			//0100	入院诊断 	0101	初步诊断		0102	确定诊断 	0103	补充诊断 	0104	修正诊断 	0109	出院诊断
			//his	入院诊断  出院诊断 	初步诊断 	修正诊断 	确认诊断
			hisDiag.setJbzh("0");//疾病组号@todo
			hisDiag.setJbdm(pvDiag.getDiagcode());//疾病代码
			hisDiag.setJbmc(pvDiag.getNameDiag()==null?pvDiag.getDescDiag():pvDiag.getNameDiag());//疾病名称
			hisDiag.setMszd(pvDiag.getDescDiag());//描述诊断
			hisDiag.setZgqk("");//转归情况
			hisDiag.setZdys(u.getCodeEmp());//@todopvDiag.getEmpCode()==null?"0":pvDiag.getEmpCode());//诊断医生
			hisDiag.setZdsj(pvDiag.getDateDiag());//诊断时间
			hisDiag.setZfbz(0);//作废标志
			hisDiag.setTjbz(0);//提交标志
			//System.out.println("pk:"+hisDiag.getJlbh());
			list.add(hisDiag);
		}
		
		DataBaseHelper.batchUpdate("delete from his.ys_zy_jbzd where jzhm=:jzhm and tjbz='0'",list);  //and zdys=:zdys 
		
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(YsZyJbzd.class), list);
		//System.out.println("diagAdmit"+diagAdmit);
		//System.out.println("codePv"+codePv);
		DataBaseHelper.update("update his.ys_zy_jzjl set ryzd = ? where jzhm = ?",new Object[] { diagAdmit,codePv});
	}
	
	/**同步医技申请信息
	 * @param ordList
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<YjSq01> saveReqInfoSync(List<CnOrderSync> ordList,User u,String optType,String reqType,List<YjSq02> itemList) {
		if(ordList==null||ordList.size()==0) return null;
		CnOrderSync ordSync=ordList.get(0);
		String pkPv=ordSync.getPkPv();
		String codePv=ordSync.getCodePv();
		String codeIp=ordSync.getCodeIp();
		String name=ordSync.getName();
		String dtSex=ordSync.getDtSex()==null?"":ordSync.getDtSex();
		String sex="未知";
		if(dtSex.equals("02")){
			sex="男";
		}else if(dtSex.equals("03")){
			sex="女";
		}
		if(optType.equals("N")){
			Integer age = getAgeByBirth(ordSync.getBirthDate(),ordSync.getDateStart());
			List<YjSq01> list = new ArrayList<YjSq01>();
			String diagAdmit="";
			//删除未处理的数据
			for (CnOrderSync vo : ordList) {
				//Integer itemSn = createSyncHisSn("YJ_SQ02");
				YjSq01 req=new YjSq01();
				req.setYjxh(Integer.parseInt(vo.getCodeApply()));//医技序号vo.getReqSn()
//				String reqTabNo=getDictCodeMap(reqMapPropName,vo.getCodeOrdtype());
//				if(reqTabNo.equals("")){
//					if(reqType.equals("LIS")){
//						reqTabNo="40";
//					}else{
//						reqTabNo="42";
//					}
//				}
				req.setMbxh(vo.getReqTabNo());
				req.setMzzy(2);//门诊住院2，住院
				req.setTjhm("0");//提交号码
				req.setBrhm(codeIp);//病人号码
				req.setZyh(Integer.parseInt(codePv));//HIS住院号
				req.setBrxm(name);//病人姓名
				req.setBrxb(sex);//病人性别
				req.setBrnl(age);//病人年龄
				req.setSjys(vo.getCodeEmp());//实际医生
				req.setSjks(Integer.parseInt(vo.getCodeDept()));//实际科室
				req.setJcys(null);//检查医生
				req.setJcks(Integer.parseInt(vo.getCodeDeptExec())); //检查科室
				req.setJcrq(vo.getDateStart());//检查日期
				String xmxh=vo.getCodeOrd();
				//System.out.println("xmxh1:"+xmxh);
				//System.out.println("vo.getFlagSet():"+vo.getFlagSet());
				if(vo.getFlagSet()!=null&&vo.getFlagSet().equals("1")) xmxh=vo.getCodeOrdSeq();//xmxh=vo.getSetCode();
				//System.out.println("xmxh2:"+xmxh);
				//System.out.println("vo.getSetCode():"+vo.getSetCode());
				req.setXmxh(Integer.parseInt(xmxh));//项目序号(子项对应第一条记录xmxh)
				req.setXmmc(vo.getDescOrd());//项目名称
				req.setHjje(vo.getPrice());//合计金额
				req.setZddm(null);//诊断代码
				req.setYzxh(Integer.parseInt(Long.toString(vo.getOrderNo())));//医嘱序号
				
				list.add(req);
			}
			if(list!=null&&list.size()>0)
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(YjSq01.class), list);
			if(itemList!=null&&itemList.size()>0)
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(YjSq02.class), itemList);
			return list;
		}else if(optType.equals("D")||optType.equals("C")){
			//删除或撤销
			List<Long> ordsns = new ArrayList<>();
			List<Long> yzxhs = new ArrayList<>();
			
			for (CnOrderSync order : ordList) {
				ordsns.add(order.getOrdsn());
				yzxhs.add(order.getOrderNo());
			}
			Map<String,Object> paramMap = new HashMap<String,Object>();
//			paramMap.put("pkPv", pkPv);
//			paramMap.put("ordsns", ordsns);
			paramMap.put("zyh", codePv);
			paramMap.put("yzxhs", yzxhs);
			List<YjSq01> reqList=cnOrderSyncMapper.queryReqRecs(paramMap);
			
			List<Integer> yjxhs = new ArrayList<>();
			for (YjSq01 req : reqList) {
				yjxhs.add(req.getYjxh());
			}
			Map<String,Object> itemMap = new HashMap<String,Object>();
			itemMap.put("yjxhs", yjxhs);
			List<YjSq02> reqItemList=cnOrderSyncMapper.queryReqItems(itemMap);
			if(reqItemList!=null&&reqItemList.size()>0){
				DataBaseHelper.batchUpdate("delete from his.yj_sq02 where yjxh=:yjxh ",reqItemList);
			}
			if(reqList!=null&&reqList.size()>0){
				DataBaseHelper.batchUpdate("delete from his.yj_sq01 where yjxh=:yjxh ",reqList);
			}
			return reqList;
		}
		return null;
	}
	
	/**同步检查申请信息
	 * @param ordList
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public void savePacsReqInfoSync(List<CnOrderSync> ordList,User u,String optType,Map<String,Object> patMap,PvDiag diag,List<YjSq01> reqItemDel) {
		if(ordList==null||ordList.size()==0) return;
		CnOrderSync ordSync=ordList.get(0);
		String pkPv=ordSync.getPkPv();
		String codePv=ordSync.getCodePv();
		String codeIp=ordSync.getCodeIp();
		String name=ordSync.getName();
		String dtSex=ordSync.getDtSex()==null?"":ordSync.getDtSex();
		String sex="O";
		if(dtSex.equals("02")){
			sex="F";
		}else if(dtSex.equals("03")){
			sex="M";
		}
		if(optType.equals("N")){
			Integer age = getAgeByBirth(ordSync.getBirthDate(),ordSync.getDateStart());
			List<JgPacsReq> list = new ArrayList<JgPacsReq>();
			String diagAdmit="";
			//删除未处理的数据
			for (CnOrderSync vo : ordList) {
				JgPacsReq req=new JgPacsReq();
				
				req.setPatientID("Z02"+vo.getCodeIp());

				req.setName(vo.getName());
				req.setNamePY(PinyinUtils.getPinYinHeadChar(vo.getName()).toUpperCase());
				req.setSex(sex);
				String birthday="";
				if(vo.getBirthDate()!=null){
					birthday=DateUtils.getDateStr(vo.getBirthDate());
				}
				req.setBirthday(birthday);
				req.setCertificateID(getPropValueStr(patMap,"idNo"));//身份证号
				req.setAddress(getPropValueStr(patMap,"address"));//地址
				req.setTelephone(getPropValueStr(patMap,"telNo"));//电话
				req.setHISSheetID("Z02"+vo.getCodeApply());//医技序号对应yj_sq01vo.getReqSn()
				req.setFeeTypeID(getPropValueStr(patMap,"insuName"));//医保名称
				req.setExamTypeID(vo.getReqTmpNo());//检查分类名称(US/CT等）
				String codeOrd=vo.getCodeOrd();
				if(codeOrd.contains("ZT")){
					codeOrd = "Z02"+codeOrd.substring(2);
				}
				req.setProjectID(codeOrd);//编码
				req.setProjectName(vo.getNameOrd());//名称
				String descBody=vo.getDescBody();
				if(descBody==null||descBody.equals("")){
					descBody=vo.getNameOrd();
				}else{
					descBody=vo.getNameOrd()+" "+descBody;
				}
				req.setExamPart(descBody);
				req.setDepartmentName(getPropValueStr(patMap,"nameDept"));//科室
				String codeDeptExec=vo.getCodeDeptExec();
				if(codeDeptExec.contains("XN")){
					codeDeptExec = codeDeptExec.substring(2);
				}
				req.setExamDepartmentID(codeDeptExec);
				
				req.setPatientSource("3");//住院
				req.setEmergency(vo.getFlagEmer());//加急标志
				req.setConfidentiality("0");
				req.setIsPhysicalCheck("0");
				req.setReportReturnToDepartmentName(getPropValueStr(patMap,"nameDept"));
				req.setDoctorName(vo.getNameEmp());//科室名称
				String diagStr=vo.getNameDiagRis();
				if(diagStr==null||diagStr.equals("")){
					if(diag!=null){
						diagStr=diag.getDescDiag();
					}
				}
				req.setClinicDiagnose(diagStr);//诊断
				req.setDoctorDescription(vo.getNoteDise());//病情描述
				req.setExamReason(vo.getPurposeRis());//原因
				req.setReqDateTime(DateUtils.getDateTimeStr(vo.getDateStart()));
				req.setHosArea(getPropValueStr(patMap,"nameDeptNs"));//病区
				req.setBedNum(getPropValueStr(patMap,"bedNo"));
				req.setIsBaby(getPropValueStr(patMap,"flagInfant"));
				req.setNewRequest("1");
				req.setInOrOutNum(vo.getCodeIp());
				if(vo.getPrice()!=null) req.setFee(vo.getPrice());

				list.add(req);
			}
			if(list!=null&&list.size()>0)
				DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(JgPacsReq.class), list);
		}else if(optType.equals("D")||optType.equals("C")){
			//删除或撤销
			//List<String> ids = new ArrayList<>();
			if(reqItemDel==null||reqItemDel.size()==0) return;
			
			for (YjSq01 req : reqItemDel) {
				//ids.add("Z02"+req.getYjxh());
				req.setId("Z02"+req.getYjxh());
			}
			

			if(reqItemDel!=null&&reqItemDel.size()>0){
				DataBaseHelper.batchUpdate("delete from Request where HISSheetID=:id ",reqItemDel);
			}
		}
		
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public Integer createSyncHisSn(String code) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", code);
		
		cnOrderSyncMapper.createSyncHisSn(map);
		
		Object sn=map.get("sn");
		if(sn==null) return 0;
		
		return Integer.parseInt(sn.toString());
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public Integer createSyncHisSnYs(String code) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", code);
		
		cnOrderSyncMapper.createSyncHisSnYs(map);
		
		Object sn=map.get("sn");
		if(sn==null) return 0;
		
		return Integer.parseInt(sn.toString());

	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public Integer createSyncHisSnMax(String code) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", code);
		
		cnOrderSyncMapper.createSyncHisSnMax(map);
		
		Object sn=map.get("sn");
		if(sn==null) return 0;
		
		return Integer.parseInt(sn.toString());

	}
	
	private static int getAgeByBirth(Date birthday,Date admitDate) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            //now.setTime(new Date());// 当前时间
            if(admitDate==null) admitDate=new Date();
            now.setTime(admitDate);// 当前时间
            
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {
           return 0;
        }
	}
	
	 /**获取申请单模板编号
	 * @param map
	 * @return
	*/
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public List<ReqTmpItemVo> queryReqTmpItems(Map<String,Object> map){
		return cnOrderSyncMapper.queryReqTmpItems(map);
	}
	
	 /**获取申请单模板编号
	 * @param map
	 * @return
	*/
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public int queryReqTabNo(String code){
		int no = DataBaseHelper.queryForScalar("select max(COALESCE(jcsq,0)) from his.gv_ylsf where fyxh=? ", Integer.class, code);
		
		return no;
		
	}
	
	 /**获取申请单模板编码
	 * @param map
	 * @return
	*/
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public String queryReqTmpNo(int no){
		//int no = DataBaseHelper.queryForScalar("select max(jcsq) from his.gv_ylsf where fyxh=? ", Integer.class, code);
		//return no;
//		Object[] objs=new Object[2];
//		String sql = "select a.jcsq,b.mbzt from his.gv_ylsf a,his.yj_bgmb01 b"
//				   + " where a.jcsq=b.mbxh and a.fyxh=? ";
//
//		List<Map<String, Object>> mapList = DataBaseHelper.queryForList(sql, new Object[] {code});
//		if(mapList!=null&&mapList.size()>0){
//			objs[0]=mapList.get(0).get("jcsq");
//			objs[1]=mapList.get(0).get("mbzt");
//		}
		String str = DataBaseHelper.queryForScalar("select COALESCE(mbzt,'') from his.yj_bgmb01 where mbxh=? ", String.class, no);
		if(str==null||str.equals("")) str="XR";
		return str;
	}
	
	/**查询患者信息
	 * @param map
	 * @return
	*/
	public List<Map<String,Object>> queryPatList(Map<String,Object> map){
		return cnOrderSyncMapper.queryPatListSync(map);
	}
	
	/**
	 * 取文本内容
	 * @param map
	 * @return
	 */
	public static String getPropValueStr(Map<String, Object> map,String key) {
		String value="" ;
		if(map.containsKey(key)){
			Object obj=map.get(key);
			value=obj==null?"":obj.toString();
		}
		return value;
	}
	
	
}
