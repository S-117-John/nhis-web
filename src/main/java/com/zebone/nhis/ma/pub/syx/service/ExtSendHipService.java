package com.zebone.nhis.ma.pub.syx.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.kangMei.vo.OrderInfoReq;
import com.zebone.nhis.ma.pub.platform.syx.support.MsgUtils;
import com.zebone.nhis.ma.pub.support.PrivateHIPMessageServerUtils;
import com.zebone.nhis.ma.pub.syx.dao.ExtSendHipMapper;
import com.zebone.nhis.ma.pub.syx.support.ExtUtils;
import com.zebone.nhis.ma.pub.syx.vo.Address;
import com.zebone.nhis.ma.pub.syx.vo.BdItem;
import com.zebone.nhis.ma.pub.syx.vo.BdOrd;
import com.zebone.nhis.ma.pub.syx.vo.Data;
import com.zebone.nhis.ma.pub.syx.vo.Head;
import com.zebone.nhis.ma.pub.syx.vo.OrderInfo;
import com.zebone.nhis.ma.pub.syx.vo.Pdetail;
import com.zebone.nhis.ma.pub.syx.vo.Request;
import com.zebone.nhis.ma.pub.syx.vo.Response;
import com.zebone.nhis.ma.pub.syx.vo.Subject;
import com.zebone.nhis.ma.pub.syx.vo.Xq;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;


@Service
public class ExtSendHipService {
	
	private PrivateHIPMessageServerUtils hipUtils = new PrivateHIPMessageServerUtils();
	
	@Autowired
	private ExtSendHipMapper extSendHipMapper;
	
	/**
	 * 业务处理方法转换器
	 * @param methodName
	 * @param args
	 */

    public Object invokeMethod(String methodName,Map<String,Object> paramMap){
    	switch(methodName){
    	case "HospitalBusiness":this.hospitalBusiness(paramMap);
    		break;
    	case "CancellationOfOrderMsg":
			this.cancellationOfOrderMsg(paramMap);
			break;
    	case "InpatientSendRecipeAdd":
    		this.inpatientSendRecipeAddMsg(paramMap);
    		break;
    	case "InpatientNoSendRecipeQuery":
    		this.inpatientNoSendRecipeQuery(paramMap);
    		break;
    	case "ChargeDictAddAndUpdate":
    		this.chargeDictAddAndUpdate(paramMap);
    		break;
    	case "OrderItemsAddAndUpdate":
    		this.orderItemsAddAndUpdate(paramMap);
    		break;
//    	case "OrderItemsQuery":
//    		this.orderItemsQuery(paramMap);
//    		break;
    	}
    	return null;
    }
    
    
    /**
     * 住院业务处理服务
     * @param paramMap
     */
    private void hospitalBusiness(Map<String,Object> paramMap){
    	Request request = new Request();
    	request = ExtUtils.jointhospHeadXml(request,"InpatientBusinessProcessing","住院业务处理服务");
    	jointhospBusXml(request,paramMap);
    	
    	//获取返回状态码
//		String resultCode = StringUtils.substringBetween(strResponse, "<resultCode>", "</resultCode>");
//		
//		//保存返回数据
//		if("0".equals(resultCode)){
//			ExPdExtRet exPdExtRet = new ExPdExtRet();
//			exPdExtRet.setPkPv(dataReq.getPkPv());
//			exPdExtRet.setRegNum(StringUtils.substringBetween(strResponse, "<reg_num>", "</reg_num>"));
//			exPdExtRet.setOrderid(StringUtils.substringBetween(strResponse, "<orderid>", "</orderid>"));
//			exPdExtRet.setPresNo(msgParam.get("presNo").toString());
//			exPdExtRet.setOrdsn(dataReq.getOrdsn());
//			exPdExtRet.setDescInfo(StringUtils.substringBetween(strResponse, "<description>", "</description>"));
//			exPdExtRet.setEuStatus(resultCode);
//			DataBaseHelper.insertBean(exPdExtRet);
//			
//			//更新 ex_pd_apply 医疗执行-物品请领（退）单      ex_pd_apply_detail 医疗执行-物品请领(退)明细
//			
//			//写ex_pd_de 医疗执行_部门_物品发退
//		}
    }
    
    private void jointhospBusXml(Request request,Map<String,Object> paramMap){
    	String KangMeiSendSwitch = ApplicationUtils.getPropertyValue("KangMeiSendSwitch","0");
    	String param = ExtUtils.getPropValueStr(paramMap, "param");
    	List<ExPdApplyDetail> exPdAppDetails = null;
    	Object object = paramMap.get("exList");
    	if(object != null)
    		exPdAppDetails = (List<ExPdApplyDetail>)object;
		if("1".equals(KangMeiSendSwitch)){
			//前台传过来的发药明细
			List<Map<String,Object>> exPdAppInfos = JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>() {
			});
			
			//查询该科室所属院区
			Map<String,Object> codeArea = DataBaseHelper.queryForMap("select code_area from bd_ou_org_area where pk_orgarea in (select pk_orgarea from bd_ou_dept where pk_dept=?)", new Object[]{UserContext.getUser().getPkDept()});
			if (codeArea == null)
				return ;

			//根据处方单号发药,获取处方单号
			Set<String> applyDtSet = Sets.newHashSet();
			for (ExPdApplyDetail temp : exPdAppDetails) {
				applyDtSet.add(temp.getPkPres());
			}
			String sql="select pres_no from CN_PRESCRIPTION where pk_pres in ("+CommonUtils.convertSetToSqlInPart(applyDtSet, "pk_pres")+")";
			List<Map<String, Object>> presNoMap = DataBaseHelper.queryForList(sql, new Object[]{});
			Set<String> presNoSet = Sets.newHashSet();
			for(Map<String, Object> presNo : presNoMap){//处方单号去重
				presNoSet.add(presNo.get("presNo").toString());
			}
			Map<String,Object> msgParam;
			for(String presNo : presNoSet){
				//遍历集合传入患者地址
				msgParam = Maps.newHashMap();
				for(Map<String,Object> temp : exPdAppInfos){
					if(temp.containsValue(presNo)) msgParam.put("piAddress",temp.get("piAddress"));
				}
				msgParam.put("presNo", presNo);
				msgParam.put("codeArea", codeArea.get("codeArea"));
				try {
					sendPresData(request, msgParam);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
    }
    

    
    /**
	 * 保存订单  发药订单
	 * @param msgParam
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	public void sendPresData(Request req, Map<String, Object> msgParam) throws Exception {
		
		String key=new Date().getTime()+"";
		
		String psw = null;
		String companyNum = null;
		if("001".equals(msgParam.get("codeArea"))){//北院区
			psw = ApplicationUtils.getPropertyValue("KangMei.psw.NorthDistrict","0");
			//康美分配给医院或机构的编号
			companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.NorthDistrict","0");
		}else if("002".equals(msgParam.get("codeArea"))){//南院区
			psw = ApplicationUtils.getPropertyValue("KangMei.psw.SouthDistrict","0");
			//康美分配给医院或机构的编号
			companyNum = ApplicationUtils.getPropertyValue("KangMei.companyNum.SouthDistrict","0");
		}

		//认证签名  Md5(接口名+时间+psw)
		String sign = "saveOrderInfo"+key+psw;
		String presNo = msgParam.get("presNo").toString();
		// 得到XML对象 <data>
		List<Map<String, Object>> qrySendDispMap = extSendHipMapper.qrySendDispMap(presNo);
		Data dataReq = ApplicationUtils.mapToBean(qrySendDispMap.get(0), Data.class);
		Pdetail pdetailReq=ApplicationUtils.mapToBean(qrySendDispMap.get(0), Pdetail.class); //= sendPrescriptionMapper.qrySendDispPdetail(msgParam.get("presNo").toString()); // <pdetail>
		List<Xq> xqreq =Lists.newArrayList(); //sendPrescriptionMapper.qrySendDispXq(msgParam.get("presNo").toString()); // <xq>
		for(Map<String, Object> temp:qrySendDispMap){
			xqreq.add((Xq) ApplicationUtils.mapToBean(temp, Xq.class));
		}
		pdetailReq.getMedici_xq().setXqs(xqreq);
		List<Pdetail> pde = new ArrayList<Pdetail>();
		pde.add(pdetailReq);
		dataReq.getPrescript().setPdetails(pde);
		
		//设置婴儿年龄
		if(StringUtils.endsWith(dataReq.getAgePv(), "月")){
			String ageByBirthday = DateUtils.getAgeByBirthday(dataReq.getBirthDate(),new Date());
			pdetailReq.setAge("0");
			pdetailReq.setPrescript_remark(ageByBirthday);
		}
		
		//设置患者发药地址  ApplicationUtils.mapToBean(qrySendDispMap.get(0), DataReq.class);
		Map<String,Object> piAddress = (Map<String,Object>)msgParam.get("piAddress");
		if("hospital".equals(piAddress.get("addr"))){//送医院
			dataReq.setIs_hos_addr("1");
			if("001".equals(msgParam.get("codeArea"))){
				dataReq.setAddr_str("广东省,广州市,荔湾区,中山大学孙逸仙纪念医院（北院区）");
			}else if("002".equals(msgParam.get("codeArea"))){
				dataReq.setAddr_str("广东省,广州市,海珠区,中山大学孙逸仙纪念医院（南院区）");
			}
		} else {//送个人
			dataReq.setAddr_str(piAddress.get("nameProv")+","+piAddress.get("nameCity")+","+piAddress.get("nameDist")+","+piAddress.get("addr"));
			dataReq.setIs_hos_addr("2");
			dataReq.setConsignee(piAddress.get("nameRel").toString());
			dataReq.setCon_tel(piAddress.get("tel").toString());
		}
		if(piAddress.get("sendDrugsTime")!=null){//设置送药时间
			dataReq.setSend_goods_time(DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.strToDate(piAddress.get("sendDrugsTime").toString())));
		}
		
		//<head>
		Head headReq=new Head();
		headReq.setKey(key);   //set KEY
		headReq.setSign(ExtUtils.encodeByMD5(sign)); //set 签名
		headReq.setCompany_num(companyNum);  
		
		//<orderInfo>
		OrderInfo orderInfoReq =new OrderInfo();
		orderInfoReq.setData(dataReq);
		orderInfoReq.setHead(headReq);
		
		req.getSubject().setOrderInfo(orderInfoReq);
		qryPiAdderss(piAddress);
		Response sendHIPMsg = hipUtils.sendHIPMsg(req, "InpatientBusinessProcessing", false,true);
	}

    /**
	 * 取消订单服务规范发送xml到平台
	 * @param dataReq
	 * @param msgParam
	 */
	private void cancellationOfOrderMsg(Map<String, Object> msgParam){
		if (msgParam == null) {
			throw new BusException("未查询到数据");
		}
		Request request = createOrderMsg(msgParam);
    	hipUtils.sendHIPMsg(request, "CancellationOfOrder", false, true);
	}
	/**
	 * 创建取消订单报文信息
	 * @param request
	 * @param msgParam
	 * @return
	 */
	private Request createOrderMsg( Map<String, Object> msgParam) {
		OrderInfoReq infoReq = new OrderInfoReq();
		infoReq = (OrderInfoReq) msgParam.get("infoReq");
		Request request = new Request();
		Request jointhospHeadXml = ExtUtils.jointhospHeadXml(request, "CancellationOfOrder", "取消订单服务");
		// 请求方信息 
		jointhospHeadXml.getSender();
	
		Subject subject = request.getSubject();
		OrderInfo orderInfo = subject.getOrderInfo();
		Head head = orderInfo.getHead();
		head.setCompany_num(infoReq.getHeadReq().getCompanyNum());
		head.setKey(infoReq.getHeadReq().getKey());
		head.setSign(infoReq.getHeadReq().getSign());
		
		Data data = orderInfo.getData();
		data.setTreat_card(infoReq.getDataReq().getTreatCard());//患者编码
		data.setReg_num(infoReq.getDataReq().getRegNum());//挂单号
		data.setOper_name(infoReq.getDataReq().getOperName());//操作人
		data.setReason(infoReq.getDataReq().getReason());;//取消原因
		data.setOrder_id(infoReq.getDataReq().getOrderId());//订单号
		return request;

	}
    
	private void qryPiAdderss(Map<String, Object> paramMap) {
		Request req = ExtUtils.jointhospHeadXml(new Request(), "UserAdderssInfoQuery", "患者常用地址查询服务");
		
		Address address = req.getSubject().getAddress();
		Map<String, Object> addrMap = extSendHipMapper.qryPiAdderss(ExtUtils.getPropValueStr(paramMap, "pkAddr"));
		address.setPk_addr(ExtUtils.getPropValueStr(addrMap, "pkAddr"));
		address.setAddrtype(ExtUtils.getPropValueStr(addrMap, "dtAddrtype"));
		address.setAddtypeName(ExtUtils.getPropValueStr(addrMap, "adtype"));
		address.setDt_region_prov(ExtUtils.getPropValueStr(addrMap, "dtRegionProv"));
		address.setName_prov(ExtUtils.getPropValueStr(addrMap, "nameProv"));
		address.setDt_region_city(ExtUtils.getPropValueStr(addrMap, "dtRegionCity"));
		address.setName_city(ExtUtils.getPropValueStr(addrMap, "nameCity"));
		address.setDt_region_dist(ExtUtils.getPropValueStr(addrMap, "dtRegionDist"));
		address.setName_dist(ExtUtils.getPropValueStr(addrMap, "nameDist"));
		address.setAddr(ExtUtils.getPropValueStr(addrMap, "addr"));
		address.setName_rel(ExtUtils.getPropValueStr(addrMap, "nameRel"));
		address.setTel(ExtUtils.getPropValueStr(addrMap, "tel"));
		address.setSort_no(ExtUtils.getPropValueStr(addrMap, "sortNo"));
		address.setFlag_use(ExtUtils.getPropValueStr(addrMap, "flagUse"));
		address.setAmt_fee(ExtUtils.getPropValueStr(addrMap, "amtFee"));
		hipUtils.sendHIPMsg(req, "UserAdderssInfoQuery", false, true);

	}

	  /**
	   * 发药记录发送服务 ，
	   * 发送xml报文到平台
	   * @param paramMap
	   */
    private void inpatientSendRecipeAddMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
    	if (paramMap == null) {
			throw new BusException("未查询到数据");
		}
		Request request = createInpatientRecipeAddMsg(paramMap);
		hipUtils.sendHIPMsg(request, "InpatientSendRecipeAdd", false, true);
	}

    /**
     * 创建发药记录发送服务报文数据
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
	private Request createInpatientRecipeAddMsg(Map<String, Object> paramMap) {
    	Request request = new Request();
		Request jointhospHeadXml = ExtUtils.jointhospHeadXml(request, "InpatientSendRecipeAdd", "发药记录发送服务");
		// 请求方信息 
		jointhospHeadXml.getSender();
    	List<PdDeDrugVo> drugVo = (List<PdDeDrugVo>) paramMap.get("drugVo");
    	PdDeDrugVo pdDeDrugVo = drugVo.get(0);
    	String codeDe = pdDeDrugVo.getCodeDe();      // 发退单号
    	Map<String,Object> map = new HashMap<>();
    	map.put("codeDe", codeDe);
    	List<OrderInfo> orderInfoList = jointhospHeadXml.getSubject().getOrderInfoList();
    	List<Map<String, Object>> resList = extSendHipMapper.selectNotDispensingMap(map);
    	for (Map<String, Object> resMap : resList) {
    		OrderInfo orderInfo = new OrderInfo();
    		orderInfo.setWard_sn(MsgUtils.getPropValueStr(resMap, "wardSn"));//申请科室
    		orderInfo.setPage_no(codeDe);//发药单号
    		orderInfo.setAtf_no(MsgUtils.getPropValueStr(resMap, "flagSendtofa"));//当前药房对应的包药机设备编号
    		orderInfo.setFlag("0");
    		orderInfo.setBylx(MsgUtils.getPropValueStr(resMap,"nameDecate"));//物品分发类型名称
    		orderInfo.setInpatient_no(MsgUtils.getPropValueStr(resMap, "codePv"));//就诊号
    		orderInfo.setP_id(MsgUtils.getPropValueStr(resMap, "codePi"));//code_pi患者编码
    		orderInfo.setName(MsgUtils.getPropValueStr(resMap, "namePi"));//患者姓名
    		orderInfo.setAge(MsgUtils.getPropValueStr(resMap, "agePv"));//年龄
    		orderInfo.setWard_sn(MsgUtils.getPropValueStr(resMap, "wardSn"));//领药病区编码
    		orderInfo.setWard_name(MsgUtils.getPropValueStr(resMap, "nameDept"));//领药病区名称
    		orderInfo.setDoctor(MsgUtils.getPropValueStr(resMap, "nameEmpOrd"));//医生姓名
    		orderInfo.setBed_no(MsgUtils.getPropValueStr(resMap, "bedNo"));//床位号
    		orderInfo.setComment(MsgUtils.getPropValueStr(resMap, "supName"));//用药途径
    		orderInfo.setDrug_code(MsgUtils.getPropValueStr(resMap, "pdCode"));//药品编码
    		orderInfo.setDrugname(MsgUtils.getPropValueStr(resMap, "pdName"));//药品名称
    		orderInfo.setSpecification(MsgUtils.getPropValueStr(resMap, "pdSpec"));//药品规格
    		orderInfo.setDosage(MsgUtils.getPropValueStr(resMap, "dosage"));//用药剂量
    		orderInfo.setDos_unit(MsgUtils.getPropValueStr(resMap, "unitName"));//剂量单位
    		orderInfo.setAmount(MsgUtils.getPropValueStr(resMap, "ordQuan")); //用药数量
    		orderInfo.setTotal(MsgUtils.getPropValueStr(resMap, "quanMin"));//用药数量合计
    		String datePlan = MsgUtils.dateFormatString("yyyyMMddHHmmss", MsgUtils.getPropValueDate(resMap, "datePlan"));
    		orderInfo.setOcc_time(datePlan);//执行时间
    		String ordsn = MsgUtils.getPropValueStr(resMap, "ordsn");//医嘱序号
    		String orsdnCode = ordsn+codeDe;
    		orderInfo.setDetail_sn(orsdnCode);//医嘱序号+发药单号 ordsn
    		orderInfo.setExecdalistid(MsgUtils.getPropValueStr(resMap, "ordsn"));//医嘱序号
    		orderInfoList.add(orderInfo);
		}
		return request;
	}
    /**
     *查询未发送包药机记录服务
     * @param paramMap
     */
    private void inpatientNoSendRecipeQuery(Map<String, Object> paramMap) {
    	List<Map<String, Object>> resList = extSendHipMapper.selectNotDispensingMap(paramMap);
    	Request request = new Request();
		Request jointhospHeadXml = ExtUtils.jointhospHeadXml(request, "InpatientNoSendRecipeQuery", "查询未发送包药机记录服务");
		jointhospHeadXml.getSender();
		Subject subject = jointhospHeadXml.getSubject();
		Map<String, Object> reqMap = resList.get(0);
		subject.setPk_dept_de(MsgUtils.getPropValueStr(reqMap,"pkDeptDe"));//发退部门
		subject.setCode_de(MsgUtils.getPropValueStr(reqMap, "codeDe"));//发退单号
		subject.setPk_pddecate(MsgUtils.getPropValueStr(reqMap, "pkPddecate"));//发放分类
		hipUtils.sendHIPMsg(request, "InpatientNoSendRecipeQuery", false, true);
	}
    
    
    /**
     *  收费项目注册更新服务，组装报文，并发到平台syx
     * @param paramMap
     */
    private void chargeDictAddAndUpdate(Map<String, Object> paramMap) {
    	Request request = new Request();
		Request jointhospHeadXml = ExtUtils.jointhospHeadXml(request, "ChargeDictAddAndUpdate", "收费项目注册更新服务");
		jointhospHeadXml.getSender();
		Subject subject = jointhospHeadXml.getSubject();
		setState(paramMap, subject);
		createBdItemMsg(paramMap, subject);
		hipUtils.sendHIPMsg(request, "ChargeDictAddAndUpdate", false, true);
	}
    	/**
    	 * 设置操作标志
    	 * @param paramMap
    	 * @param subject
    	 */
	private void setState(Map<String, Object> paramMap, Subject subject) {
		// <!-- 操作标志；0，注册；1，更新 -->
		String state = (String) paramMap.get("STATUS");
		switch (state) {
		case "_ADD":
			subject.setAction("0");
			break;
		case "_UPDATE":
			subject.setAction("1");
			break;
		default:
			break;
		}
	}

	/**
     * 创建BdItem 报文
     * @param paramMap
     * @param subject
     */
	private void createBdItemMsg(Map<String, Object> paramMap ,Subject subject) {
		@SuppressWarnings("unchecked")
		Map<String, Object> reqMap = (Map<String, Object>) paramMap.get("item");
		BdItem bd_item = subject.getBd_item();
		bd_item.setPk_item(MsgUtils.getPropValueStr(reqMap, "pkItem"));//收费项目主键
		bd_item.setCode(MsgUtils.getPropValueStr(reqMap, "code"));//收费编码
		bd_item.setName(MsgUtils.getPropValueStr(reqMap, "name"));//收费名称
		bd_item.setName_prt(MsgUtils.getPropValueStr(reqMap, "namePrt"));//打印名称
		bd_item.setSpcode(MsgUtils.getPropValueStr(reqMap, "spcode")); //拼音编码
		bd_item.setD_code(MsgUtils.getPropValueStr(reqMap, "dCode"));//自定义编码
		bd_item.setPk_unit(MsgUtils.getPropValueStr(reqMap, "pkUnit"));//单位
		bd_item.setSpec(MsgUtils.getPropValueStr(reqMap, "spec"));//规格
		bd_item.setPrice(MsgUtils.getPropValueStr(reqMap, "price"));//价格
		bd_item.setFlag_set(MsgUtils.getPropValueStr(reqMap, "flagSet"));// 组套标志 
		bd_item.setFlag_pd(MsgUtils.getPropValueStr(reqMap, "flagPd"));//物品标志 
		bd_item.setFlag_active(MsgUtils.getPropValueStr(reqMap, "flagPd"));//启用标志
		bd_item.setEu_pricemode(MsgUtils.getPropValueStr(reqMap, "euPricemode"));//定价模式
		bd_item.setPk_itemcate(MsgUtils.getPropValueStr(reqMap, "pkItemcate"));//服务分类
		bd_item.setDt_chcate(MsgUtils.getPropValueStr(reqMap, "dtChcate"));//病案分类
		bd_item.setNote(MsgUtils.getPropValueStr(reqMap, "note")); // 医疗项目说明
		bd_item.setCreator(MsgUtils.getPropValueStr(reqMap, "creator"));//创建人
		bd_item.setCreate_time(MsgUtils.getPropValueStr(reqMap, "createTime"));//创建时间
		bd_item.setModifier(MsgUtils.getPropValueStr(reqMap, "modifier"));//修改人
		bd_item.setModity_time(MsgUtils.getPropValueStr(reqMap, "modityTime"));//修改时间 (his 不保存修改时间？)
		bd_item.setDel_flag(MsgUtils.getPropValueStr(reqMap, "delFlag"));//删除标志 
		bd_item.setTs(MsgUtils.getPropValueDate(reqMap, "ts"));//时间戳
		bd_item.setDesc_item(MsgUtils.getPropValueStr(reqMap, "descItem"));//项目内涵
		bd_item.setExcept_item(MsgUtils.getPropValueStr(reqMap, "exceptItem"));//除外内容
		bd_item.setCode_hp(MsgUtils.getPropValueStr(reqMap, "codeHp"));//医保上传编码
		bd_item.setCode_std(MsgUtils.getPropValueStr(reqMap, "codeStd"));//标准编码
		bd_item.setDt_itemtype(MsgUtils.getPropValueStr(reqMap, "dtItemtype"));//项目类型
	}
		
	 /**
	  * 医嘱项目注册更新服务 发送消息到平台syx
	  * @param paramMap
	  */
	 private void orderItemsAddAndUpdate(Map<String, Object> paramMap) {
		Request request = new Request();
		Request jointhospHeadXml = ExtUtils.jointhospHeadXml(request, "OrderItemsAddAndUpdate", "医嘱项目注册更新服务");
		jointhospHeadXml.getSender();
		Subject subject = jointhospHeadXml.getSubject();
		setState(paramMap, subject);
		createBdOrdMsg(paramMap, subject);
		hipUtils.sendHIPMsg(request, "OrderItemsAddAndUpdate", false, true);
		}

	 /**
	  * 创建BdOrd报文信息
	  * @param subject
	  */
	private void createBdOrdMsg(Map<String, Object> paramMap, Subject subject) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		Map<String, Object> reqMap  = (Map<String, Object>) paramMap.get("bdOrd");
		BdOrd bd_ord = subject.getBd_ord();
		bd_ord.setPk_ord(MsgUtils.getPropValueStr(reqMap, "pkOrd"));//医嘱项目主键
		bd_ord.setPk_ordtype(MsgUtils.getPropValueStr(reqMap, "pkOrdtype"));//医嘱项目主键类型
		bd_ord.setCode_ordtype(MsgUtils.getPropValueStr(reqMap, "codeOrdtype"));// 医嘱类型编码-
		bd_ord.setCode(MsgUtils.getPropValueStr(reqMap, "code"));//医嘱编码
		bd_ord.setName(MsgUtils.getPropValueStr(reqMap, "name"));//医嘱名称
		bd_ord.setName_prt(MsgUtils.getPropValueStr(reqMap, "namePrt"));//打印名称
		bd_ord.setSpec(MsgUtils.getPropValueStr(reqMap, "spec"));//显示规格
		bd_ord.setSpcode(MsgUtils.getPropValueStr(reqMap, "spcode"));//拼音码
		bd_ord.setD_code(MsgUtils.getPropValueStr(reqMap, "dCode"));//自定义码
		bd_ord.setEu_exclude(MsgUtils.getPropValueStr(reqMap, "euExclude"));//互斥类型
		bd_ord.setFlag_ns(MsgUtils.getPropValueStr(reqMap, "flagNs"));//护士使用标志
		bd_ord.setFlag_dr(MsgUtils.getPropValueStr(reqMap, "flagDr"));//医生使用标志
		bd_ord.setCode_freq(MsgUtils.getPropValueStr(reqMap, "codeFreq"));// 默认频次
		bd_ord.setQuan_def(MsgUtils.getPropValueStr(reqMap, "quanDef"));//默认用量
		bd_ord.setFlag_ip(MsgUtils.getPropValueStr(reqMap, "flagIp"));// 住院可用标志 
		bd_ord.setFlag_op(MsgUtils.getPropValueStr(reqMap, "flagOp"));//门诊可用标志
		bd_ord.setFlag_er(MsgUtils.getPropValueStr(reqMap, "flagEr"));//急诊可用标志
		bd_ord.setFlag_hm(MsgUtils.getPropValueStr(reqMap, "flagHm"));//家庭病房可用标志
		bd_ord.setFlag_pe(MsgUtils.getPropValueStr(reqMap, "flagPe"));//体检可用标志
		bd_ord.setFlag_emr(MsgUtils.getPropValueStr(reqMap, "flagEmr"));//医疗记录联动标志 
		bd_ord.setFlag_cg(MsgUtils.getPropValueStr(reqMap, "flagCg"));//记费标志
		bd_ord.setFlag_pd(MsgUtils.getPropValueStr(reqMap, "flagPd"));//物品标志
		bd_ord.setFlag_active(MsgUtils.getPropValueStr(reqMap, "flagActive"));//启用标志
		bd_ord.setNote(MsgUtils.getPropValueStr(reqMap, "note"));//备注
		bd_ord.setCreator(MsgUtils.getPropValueStr(reqMap, "creator"));//创建人
		bd_ord.setCreate_time(MsgUtils.getPropValueStr(reqMap, "createTime"));//创建时间 
		bd_ord.setModifier(MsgUtils.getPropValueStr(reqMap, "modifier"));//修改人
		bd_ord.setDel_flag(MsgUtils.getPropValueStr(reqMap, "delFlag"));//删除标志 
		bd_ord.setTs(MsgUtils.getPropValueStr(reqMap, "ts"));//时间戳
		bd_ord.setFlag_unit(MsgUtils.getPropValueStr(reqMap, "flagUnit"));//单位标志
		bd_ord.setPk_unit(MsgUtils.getPropValueStr(reqMap, "pkUnit"));//单位
		bd_ord.setEu_sex(MsgUtils.getPropValueStr(reqMap, "euSex"));//可用性别 
		bd_ord.setFlag_ped(MsgUtils.getPropValueStr(reqMap, "flagPed"));//儿科使用标志
		bd_ord.setDt_ordcate(MsgUtils.getPropValueStr(reqMap, "dtOrdcate"));//医嘱分类
		bd_ord.setAge_min(MsgUtils.getPropValueStr(reqMap, "ageMin"));//适用开始年龄
		bd_ord.setAge_max(MsgUtils.getPropValueStr(reqMap, "ageMax"));//适用结束年龄
		bd_ord.setDesc_ord(MsgUtils.getPropValueStr(reqMap, "descOrd"));//项目内涵
		bd_ord.setExcept_ord(MsgUtils.getPropValueStr(reqMap, "exceptOrd"));// 除外内容
		bd_ord.setFlag_noc(MsgUtils.getPropValueStr(reqMap, "flagNoc"));//禁止修改扩展属性
		
	}
//	/**
//	 * 医嘱项目查询服务 发送消息到平台
//	 * @param paramMap
//	 */
//	private void orderItemsQuery(Map<String, Object> paramMap) {
//		Request request = new Request();
//		Request jointhospHeadXml = ExtUtils.jointhospHeadXml(request, "OrderItemsQuery", "医嘱项目查询服务");
//		jointhospHeadXml.getSender();
//		Subject subject = jointhospHeadXml.getSubject();
//		BdOrd bd_ord = subject.getBd_ord();
//		bd_ord.setPk_ord(MsgUtils.getPropValueStr(paramMap, "pkOrd"));//<!-- 医嘱类型主键-->
//		bd_ord.setPk_ordtype(MsgUtils.getPropValueStr(paramMap, "pkOrdtype"));//医嘱类型主键
//		bd_ord.setCode_ordtype(MsgUtils.getPropValueStr(paramMap, "codeOrdtype"));//医嘱类型编码
//		bd_ord.setCode(MsgUtils.getPropValueStr(paramMap, "code"));//医嘱编码
//		bd_ord.setName(MsgUtils.getPropValueStr(paramMap, "name"));//医嘱名称 
//		bd_ord.setName_prt(MsgUtils.getPropValueStr(paramMap, "namePrt"));//打印名称
//		hipUtils.sendHIPMsg(request, "OrderItemsQuery", false, true);
//	}
}
