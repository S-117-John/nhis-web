package com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input1201Medinsinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input1901Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input1101Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2301Feedetail;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2302Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2401Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2401Mdtrtinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2402Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2402Dscginfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2403Adminfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2403Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2404Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2405Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input3301Catalogcompin;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input5201Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input9001SignIn;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.*;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.utils.JsonUtil;


public class YbFunUtils {

	public static void main(String[] args) {
		test1201();
	}
	
	/**
	 * 测试 9001签到入参
	 */
	public static void test9001(){
		OutputData9001 paramOut= fun9001(); 
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 测试 3301上传目录
	 */
	public static void test3301(){
		Input3301 input  = new Input3301();
		Input3301Catalogcompin data = new Input3301Catalogcompin();
		data.setFixmedins_hilist_id("");
		data.setFixmedins_hilist_name("");
		data.setList_type("");
		data.setMed_list_codg("");
		data.setBegndate("");
		data.setEnddate("");
		List<Input3301Catalogcompin> dataList = new ArrayList<Input3301Catalogcompin>();
		dataList.add(data);
		input.setCatalogcompin(dataList);
		OutputData3301 paramOut = fun3301(input, null);
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 测试 1101人员基本信息获取
	 */
	public static void test1101(){
		Input1101 input  = new Input1101();
		Input1101Data data = new Input1101Data();
		data.setMdtrt_cert_type("02");
		data.setMdtrt_cert_no("441521197906130121");
		input.setData(data);
		OutputData1101 paramOut = fun1101(input, null, null);
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 测试 5.1.2.1【1201】医药机构信息获取
	 */
	public static void test1201(){
		Input1201 input  = new Input1201();
		Input1201Medinsinfo data = new Input1201Medinsinfo();
		data.setFixmedins_type("1");
		//data.setFixmedins_name("人");
		input.setMedinsinfo(data);
		OutputData1201 paramOut = fun1201(input, null);
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 测试 1901字典表查询
	 */
	public static void test1901(){
		Input1901 input  = new Input1901();
		Input1901Data data = new Input1901Data();
		data.setType("matn_type");
		data.setDate(DateUtils.getDateTime());
		data.setAdmdvs("442000");
		data.setValiFlag("1");
		input.setData(data);
		OutputData1901 paramOut = fun1901(input, null);
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 测试 5.2.4.1【2301】住院费用明细上传
	 */
	public static void test2301(){
		Input2301 input  = new Input2301();
		List<Input2301Feedetail> feedetailList = new ArrayList<Input2301Feedetail>();
		Input2301Feedetail feedetail = new Input2301Feedetail();
		feedetail.setFeedetl_sn("001");
		feedetail.setMdtrt_id("");
		feedetail.setPsn_no("GD_SW00000006792509");
		feedetail.setMed_type("21");
		feedetail.setFee_ocur_time("2020-10-24 15:00:00");
		feedetail.setMed_list_codg("XA01ABD075A002010100594");
		feedetail.setMedins_list_codg("XA01ABD075A002010100594");
		feedetail.setDet_item_fee_sumamt("1.00");
		feedetail.setCnt("1");
		feedetail.setPric("1.00");
		feedetail.setBilg_dept_codg("A02");
		feedetail.setBilg_dept_name("实施科");
		feedetail.setBilg_dr_codg("ys001");
		feedetail.setBilg_dr_name("测试医生");
		feedetailList.add(feedetail);
		input.setFeedetail(feedetailList);
		OutputData2301 paramOut = fun2301(input, null, null);
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 5.2.4.2【2302】住院费用明细撤销
	 */
	public static void test2302(){
		Input2302 input  = new Input2302();
		//List<Input2302Data> dataList = new ArrayList<Input2302Data>();
		Input2302Data data= new Input2302Data();
		data.setFeedetl_sn("0000");
		data.setFeedetl_sn("");
		data.setPsn_no("");
		//dataList.add(data);
		input.setData(data);
		OutputData2302 paramOut = fun2302(input, null, null);
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	
	
	//1664063
	/**
	 * 测试 【2401】入院办理
	 */
	public static void test2401(){
		Input2401 input  = new Input2401();
		Input2401Mdtrtinfo  mdtrtinfo = new Input2401Mdtrtinfo();
		mdtrtinfo.setPsn_no("44150000000000051364");
		mdtrtinfo.setInsutype("310");
		mdtrtinfo.setBegntime("2020-10-24 08:58:35");
		mdtrtinfo.setMdtrt_cert_type("02");
		mdtrtinfo.setMdtrt_cert_no("441521197906130121");
		mdtrtinfo.setMed_type("21");
		mdtrtinfo.setIpt_no("ba001");
		mdtrtinfo.setAtddr_no("ys001");
		mdtrtinfo.setChfpdr_name("测试医生");
		mdtrtinfo.setAdm_diag_dscr("肚子疼");
		mdtrtinfo.setAdm_dept_codg("A02");
		mdtrtinfo.setAdm_dept_name("实施科");
		mdtrtinfo.setAdm_bed("01");
		mdtrtinfo.setDscg_maindiag_code("A08.000");
		mdtrtinfo.setDscg_maindiag_name("轮状病毒性肠炎");
		
		//诊断
		List<Input2401Diseinfo> diseinfoList = new ArrayList<Input2401Diseinfo>();
		Input2401Diseinfo diseinfo = new Input2401Diseinfo();
		diseinfo.setPsn_no("44150000000000051364");
		diseinfo.setDiag_type("3");
		diseinfo.setMaindiag_flag("1");
		diseinfo.setDiag_srt_no("1");
		diseinfo.setDiag_code("A08.000");
		diseinfo.setDiag_name("轮状病毒性肠炎");
		diseinfo.setDiag_dept("A01");
		diseinfo.setDise_dor_no("ys001");
		diseinfo.setDise_dor_name("测试医生");
		diseinfo.setDiag_time("2020-10-24 08:58:35");
		diseinfoList.add(diseinfo);
		input.setMdtrtinfo(mdtrtinfo);
		input.setDiseinfo(diseinfoList);
		OutputData2401 paramOut = fun2401(input, "441500", null);
		
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 测试 【2402】出院办理
	 */
	public static void test2402(){
		Input2402 input  = new Input2402();
		Input2402Dscginfo  mdtrtinfo = new Input2402Dscginfo();
		mdtrtinfo.setMdtrt_id("1647001");
		mdtrtinfo.setPsn_no("GD_SW00000006792509");
		mdtrtinfo.setInsutype("310");
		mdtrtinfo.setEndtime("2020-10-24 17:58:35");
		mdtrtinfo.setDscg_dept_codg("A02");
		mdtrtinfo.setDscg_dept_name("实施科");
		mdtrtinfo.setDscg_way("9");
		//诊断
		List<Input2402Diseinfo> diseinfoList = new ArrayList<Input2402Diseinfo>();
		Input2402Diseinfo diseinfo = new Input2402Diseinfo();
		diseinfo.setPsn_no("GD_SW00000006792509");
		diseinfo.setDiag_type("3");
		diseinfo.setMaindiag_flag("1");
		diseinfo.setDiag_srt_no("1");
		diseinfo.setDiag_code("A08.000");
		diseinfo.setDiag_name("轮状病毒性肠炎");
		diseinfo.setDiag_dept("A01");
		diseinfo.setDise_dor_no("ys001");
		diseinfo.setDise_dor_name("测试医生");
		diseinfo.setDiag_time("2020-10-24 08:58:35");
		diseinfoList.add(diseinfo);
		input.setDscginfo(mdtrtinfo);
		input.setDiseinfo(diseinfoList);
		OutputData2402 paramOut = fun2402(input, "441500", null);
		
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 测试 5.2.5.3【2403】住院信息变更
	 */
	public static void test2403(){
		Input2403 input  = new Input2403();
		Input2403Adminfo  mdtrtinfo = new Input2403Adminfo();
		mdtrtinfo.setMdtrt_id("1647001");
		mdtrtinfo.setPsn_no("GD_SW00000006792509");
		mdtrtinfo.setBegntime("2020-10-24 08:58:35");
		mdtrtinfo.setMdtrt_cert_type("02");
		mdtrtinfo.setMed_type("21");
		mdtrtinfo.setIpt_otp_no("ba001");
		mdtrtinfo.setAtddr_no("ys001");
		mdtrtinfo.setChfpdr_name("测试医生");
		mdtrtinfo.setAdm_diag_dscr("肚子疼");
		mdtrtinfo.setAdm_dept_codg("A02");
		mdtrtinfo.setAdm_dept_name("实施科");
		mdtrtinfo.setAdm_bed("01");
		mdtrtinfo.setDscg_maindiag_code("A08.000");
		mdtrtinfo.setDscg_maindiag_name("轮状病毒性肠炎");
		//诊断
		List<Input2403Diseinfo> diseinfoList = new ArrayList<Input2403Diseinfo>();
		Input2403Diseinfo diseinfo = new Input2403Diseinfo();
		diseinfo.setMdtrt_id("1647001");
		diseinfo.setPsn_no("GD_SW00000006792509");
		diseinfo.setDiag_type("3");
		diseinfo.setMaindiag_flag("1");
		diseinfo.setDiag_srt_no("1");
		diseinfo.setDiag_code("A08.000");
		diseinfo.setDiag_name("轮状病毒性肠炎");
		diseinfo.setDiag_dept("A01");
		diseinfo.setDise_dor_no("ys001");
		diseinfo.setDise_dor_name("测试医生");
		diseinfo.setDiag_time("2020-10-24 08:58:35");
		diseinfoList.add(diseinfo);
		input.setAdminfo(mdtrtinfo);
		input.setDiseinfo(diseinfoList);
		OutputData2403 paramOut = fun2403(input, "441500", null);
		
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 测试 5.2.5.4【2404】入院撤销
	 */
	public static void test2404(){
		Input2404 input  = new Input2404();
		Input2404Data data = new Input2404Data();
		data.setMdtrt_id("1664155");
		data.setPsn_no("44150000000000151866");
		input.setData(data);
		OutputData2404 paramOut = fun2404(input, "441500", null);
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 测试 5.2.5.5【2405】出院撤销
	 */
	public static void test2405(){
		Input2405 input  = new Input2405();
		Input2405Data data = new Input2405Data();
		data.setMdtrt_id("1647001");
		data.setPsn_no("GD_SW00000006792509");
		input.setData(data);
		OutputData2405 paramOut = fun2405(input, "441500", null);
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	/**
	 * 测试 5.5.2.1【5201】就诊信息查询
	 */
	public static void test5201(){
		Input5201 input  = new Input5201();
		Input5201Data data = new Input5201Data();
		data.setPsn_no("GD_SW00000006792509");
		data.setBegntime("2020-10-24 00:00:00");
		data.setEndtime("2020-10-25 00:00:00");
		data.setMed_type("21");
		data.setMdtrt_id("");
		
		input.setData(data);
		OutputData5201 paramOut = fun5201(input, "441500", null);
		if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
			System.out.println(paramOut.getErr_msg());
		}else{
			System.out.println(paramOut.getErr_msg());
		}
	}
	
	
	/**
	 * 9001签到入参 使用服务器的ip和mac进行签到
	 * @param input
	 * @return
	 */
	public static OutputData9001 fun9001(){
		InputData9001 param9001 = new InputData9001();
		//param9001.Assignment("9001", null, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param9001.Assignment("9001", null, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), null);
		Input9001SignIn signIn = new Input9001SignIn();
		//input9001.setIp(UserContext.getUser().getDbIp());
		try {
			InetAddress ia = InetAddress.getLocalHost();
			String ip=ia.toString().split("/")[1];
			System.out.println(ia);
			System.out.println("IP:"+ip);
			signIn.setIp(ip);
			try {
				signIn.setMac(getLocalMac(ia));
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		signIn.setOpter_no("001");
		Input9001 input9001  = new Input9001();
		input9001.setSignIn(signIn);
		param9001.setInput(input9001);
		Gson gson=new Gson();
		String jsonStr = YbHttpClass.HttpRequestYbQg("9001",gson.toJson(param9001));
		OutputData9001 outputData9001 = JsonUtil.readValue(jsonStr, OutputData9001.class);
		return outputData9001;
	}
	
	/**
	 * 9001签到 使用前端传过来的ip和mac进行签到
	 * @return
	 */
	public static OutputData9001 fun9001Client(Input9001 input){
		InputData9001 param = new InputData9001();
		param.Assignment("9001", null, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), null);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("9001",json.toString());
		OutputData9001 outputData = JsonUtil.readValue(jsonStr, OutputData9001.class);
		return outputData;
	}
	
	/**
	 * 9002签退 定时任务用的
	 * @return
	 */
	public static OutputData9002 fun9002(Input9002 input){
		InputData9002 param = new InputData9002();
		//param.Assignment("9002", null, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.Assignment("9002", null, "1.0", "1", "system", "定时任务", null);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("9002",json.toString());
		OutputData9002 outputData = JsonUtil.readValue(jsonStr, OutputData9002.class);
		return outputData;
	}
	
	
	/**
	 * 1101人员基本信息获取
	 * @param input
	 * @return
	 */
	public static OutputData1101 fun1101(Input1101 input, String insuplcAdmdvs, String signNo){
		InputData1101 param = new InputData1101();
		param.Assignment("1101", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("1101", json.toString());
		OutputData1101 outputData = JsonUtil.readValue(jsonStr, OutputData1101.class);
		return outputData;
	}
	
	/**
	 * 5.1.2.1【1201】医药机构信息获取
	 * @param input
	 * @return
	 */
	public static OutputData1201 fun1201(Input1201 input, String signNo){
		InputData1201 param = new InputData1201();
		//param.Assignment("1201", null, "1.0", "1", "system", "定时任务", null);
		param.Assignment("1201", null, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("1201", json.toString());
		OutputData1201 outputData = JsonUtil.readValue(jsonStr, OutputData1201.class);
		return outputData;
	}
	
	/**
	 * 5.1.4.1【1901】字典表查询
	 * @param input
	 * @return
	 */
	public static OutputData1901 fun1901(Input1901 input, String signNo){
		InputData1901 param = new InputData1901();
		param.Assignment("1901", null, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("1901", json.toString());
		OutputData1901 outputData = JsonUtil.readValue(jsonStr, OutputData1901.class);
		return outputData;
	}
	
	/**
	 * 6.2.1.1【2001】人员待遇享受检查
	 * @param input
	 * @return
	 */
	public static OutputData2001 fun2001(Input2001 input, String insuplcAdmdvs, String signNo){
		InputData2001 param = new InputData2001();
		param.Assignment("2001", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2001", json.toString());
		OutputData2001 outputData = JsonUtil.readValue(jsonStr, OutputData2001.class);
		return outputData;
	}
	
	/**
	 * 【2301】住院费用明细上传
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2301 fun2301(Input2301 input, String insuplcAdmdvs, String signNo){
		InputData2301 param = new InputData2301();
		param.Assignment("2301", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2301", json.toString());
		OutputData2301 outputData = JsonUtil.readValue(jsonStr, OutputData2301.class);
		return outputData;
	}
	
	/**
	 * 5.2.4.2【2302】住院费用明细撤销
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2302 fun2302(Input2302 input, String insuplcAdmdvs, String signNo){
		InputData2302 param = new InputData2302();
		param.Assignment("2302", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2302", json.toString());
		OutputData2302 outputData = JsonUtil.readValue(jsonStr, OutputData2302.class);
		return outputData;
	}
	
	/**
	 * 5.2.4.2【2302】住院费用明细撤销 入参实体，回参json字符串
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static String fun2302Json(Input2302 input, String insuplcAdmdvs, String signNo){
		InputData2302 param = new InputData2302();
		param.Assignment("2302", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2302", json.toString());
		return jsonStr;
	}
	
	/**
	 * 5.2.4.3【2303】住院预结算
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2303 fun2303(Input2303 input, String insuplcAdmdvs, String signNo){
		InputData2303 param = new InputData2303();
		param.Assignment("2303", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2303", json.toString());
		OutputData2303 outputData = JsonUtil.readValue(jsonStr, OutputData2303.class);
		return outputData;
	}
	
	/**
	 * 5.2.4.4【2304】住院结算
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2304 fun2304(Input2304 input, String insuplcAdmdvs, String signNo){
		InputData2304 param = new InputData2304();
		param.Assignment("2304", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2304", json.toString());
		OutputData2304 outputData = JsonUtil.readValue(jsonStr, OutputData2304.class);
		outputData.setMsgid(param.getMsgid());
		return outputData;
	}
	
	/**
	 * 【2305】住院结算撤销
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2305 fun2305(Input2305 input, String insuplcAdmdvs, String signNo){
		InputData2305 param = new InputData2305();
		param.Assignment("2305", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2305", json.toString());
		OutputData2305 outputData = JsonUtil.readValue(jsonStr, OutputData2305.class);
		return outputData;
	}
	
	/**
	 * 5.2.5.1【2401】入院办理
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2401 fun2401(Input2401 input, String insuplcAdmdvs, String signNo){
		InputData2401 param = new InputData2401();
		param.Assignment("2401", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2401", json.toString());
		OutputData2401 outputData = JsonUtil.readValue(jsonStr, OutputData2401.class);
		outputData.setMsgid(param.getMsgid());
		return outputData;
	}
	
	/**
	 * 5.2.5.2【2402】出院办理
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2402 fun2402(Input2402 input, String insuplcAdmdvs, String signNo){
		InputData2402 param = new InputData2402();
		param.Assignment("2402", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2402", json.toString());
		OutputData2402 outputData = JsonUtil.readValue(jsonStr, OutputData2402.class);
		outputData.setMsgid(param.getMsgid());
		return outputData;
	}
	
	/**
	 * 5.2.5.3【2403】住院信息变更
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2403 fun2403(Input2403 input, String insuplcAdmdvs, String signNo){
		InputData2403 param = new InputData2403();
		param.Assignment("2403", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2403", json.toString());
		OutputData2403 outputData = JsonUtil.readValue(jsonStr, OutputData2403.class);
		return outputData;
	}
	
	/**
	 * 【2404】入院撤销
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2404 fun2404(Input2404 input, String insuplcAdmdvs, String signNo){
		InputData2404 param = new InputData2404();
		param.Assignment("2404", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2404", json.toString());
		OutputData2404 outputData = JsonUtil.readValue(jsonStr, OutputData2404.class);
		return outputData;
	}
	
	/**
	 * 5.2.5.5【2405】出院撤销
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2405 fun2405(Input2405 input, String insuplcAdmdvs, String signNo){
		InputData2405 param = new InputData2405();
		param.Assignment("2405", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2405", json.toString());
		OutputData2405 outputData = JsonUtil.readValue(jsonStr, OutputData2405.class);
		return outputData;
	}
	
	/**
	 * 5.2.5.5【2405】出院撤销 josn回参
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static String fun2405Json(Input2405 input, String insuplcAdmdvs, String signNo){
		InputData2405 param = new InputData2405();
		param.Assignment("2405", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2405", json.toString());
		return jsonStr;
	}
	
	/**
	 * 5.2.6.1【2501】转院备案 (异地用)
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2501 fun2501(Input2501 input, String insuplcAdmdvs, String signNo){
		InputData2501 param = new InputData2501();
		param.Assignment("2501", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2501", json.toString());
		OutputData2501 outputData = JsonUtil.readValue(jsonStr, OutputData2501.class);
		return outputData;
	}
	
	/**
	 * 5.2.6.1【2501A】转院备案 （中山用）
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2501A fun2501A(Input2501A input, String insuplcAdmdvs, String signNo){
		InputData2501A param = new InputData2501A();
		param.Assignment("2501A", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2501A", json.toString());
		OutputData2501A outputData = JsonUtil.readValue(jsonStr, OutputData2501A.class);
		return outputData;
	}
	
	/**
	 * 5.2.6.1【2501】转院备案
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData2502 fun2502(Input2502 input, String insuplcAdmdvs, String signNo){
		InputData2502 param = new InputData2502();
		param.Assignment("2502", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("2502", json.toString());
		OutputData2502 outputData = JsonUtil.readValue(jsonStr, OutputData2502.class);
		return outputData;
	}
	
	/**
	 * 6.3.2.1【3201】医药机构费用结算对总账
	 * @param input
	 * @return
	 */
	public static OutputData3201 fun3201(Input3201 input, String signNo){
		InputData3201 param = new InputData3201();
		param.Assignment("3201", "442000", "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("3201", json.toString());
		OutputData3201 outputData = JsonUtil.readValue(jsonStr, OutputData3201.class);
		return outputData;
	}
	
	/**
	 * 医药机构费用结算对明细账
	 * @param input
	 * @return
	 */
	public static OutputData3202 fun3202(Input3202 input, String signNo){
		InputData3202 param = new InputData3202();
		param.Assignment("3202", "442000", "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("3202", json.toString());
		OutputData3202 outputData = JsonUtil.readValue(jsonStr, OutputData3202.class);
		return outputData;
	}
	
	/**
	 * 5.1.1.1提取异地清分明细
	 * 就医地使用此交易提取省内异地外来就医月度结算清分明细,供医疗机构进行确认处理。
	 * 查询操作每次返回不超过100条。
	 * @param input
	 * @return
	 */
	public static OutputData3260 fun3260(Input3260 input, String signNo){
		InputData3260 param = new InputData3260();
		param.Assignment("3260", "442000", "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("3260", json.toString());
		OutputData3260 outputData = JsonUtil.readValue(jsonStr, OutputData3260.class);
		return outputData;
	}
	
	/**
	 * 5.1.1.2异地清分结果确认
	 * 就医地使用此交易提交省内异地外来就医月度结算清分确认结果。
	 * 每批次上传明细数量不超过100条，超过100条明细需要分批多次上传。
	 * @param input
	 * @return
	 */
	public static OutputData3261 fun3261(Input3261 input, String signNo){
		InputData3261 param = new InputData3261();
		param.Assignment("3261", "442000", "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("3261", json.toString());
		OutputData3261 outputData = JsonUtil.readValue(jsonStr, OutputData3261.class);
		outputData.setMsgid(param.getMsgid());
		return outputData;
	}
	
	/**
	 * 5.1.1.3异地清分结果确认回退
	 * @param input
	 * @return
	 */
	public static OutputData3262 fun3262(Input3262 input, String signNo){
		InputData3262 param = new InputData3262();
		param.Assignment("3262", "442000", "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("3262", json.toString());
		OutputData3262 outputData = JsonUtil.readValue(jsonStr, OutputData3262.class);
		outputData.setMsgid(param.getMsgid());
		return outputData;
	}
	
	/**
	 * 5.2.6.1【2501】转院备案
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData3301 fun3301(Input3301 input, String signNo){
		InputData3301 param = new InputData3301();
		param.Assignment("3301", null, "1.0", "1", "001", "测试", signNo);
		//param.Assignment("3301", null, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQgCs("3301", json.toString());
		OutputData3301 outputData = JsonUtil.readValue(jsonStr, OutputData3301.class);
		return outputData;
	}
	
	/**
	 * 6.4.1.1医疗保障基金结算清单信息上传
	 * @param input
	 * @param signNo
	 * @return
	 */
	public static OutputData4101 fun4101(Input4101 input, String signNo){
		InputData4101 param = new InputData4101();
		param.Assignment("4101", null, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("4101", json.toString());
		OutputData4101 outputData = JsonUtil.readValue(jsonStr, OutputData4101.class);
		return outputData;
	}
	
	/**
	 * 5.1.1.1结算清单信息查询
	 * @param input
	 * @param signNo
	 * @return
	 */
	public static OutputData4160 fun4160(Input4160 input, String signNo){
		InputData4160 param = new InputData4160();
		param.Assignment("4160", null, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("4160", json.toString());
		OutputData4160 outputData = JsonUtil.readValue(jsonStr, OutputData4160.class);
		return outputData;
	}
	
	/**
	 * 5.5.2.1【5201】就诊信息查询
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData5201 fun5201(Input5201 input, String insuplcAdmdvs, String signNo){
		InputData5201 param = new InputData5201();
		param.Assignment("5201", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("5201", json.toString());
		OutputData5201 outputData = JsonUtil.readValue(jsonStr, OutputData5201.class);
		return outputData;
	}
		
	/**
	 * 6.5.2.3结算信息查询
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData5203 fun5203(Input5203 input, String insuplcAdmdvs, String signNo){
		InputData5203 param = new InputData5203();
		param.Assignment("5203", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("5203", json.toString());
		OutputData5203 outputData = JsonUtil.readValue(jsonStr, OutputData5203.class);
		return outputData;
	}
	
	/**
	 * 6.5.2.4费用明细查询
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData5204 fun5204(Input5204 input, String insuplcAdmdvs, String signNo){
		InputData5204 param = new InputData5204();
		param.Assignment("5204", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("5204", json.toString());
		OutputData5204 outputData = JsonUtil.readValue(jsonStr, OutputData5204.class);
		return outputData;
	}
	
	/**
	 * 费用明细查询
	 * @param input
	 * @param insuplcAdmdvs 参保地区划代码
	 * @return
	 */
	public static OutputData5204 fun5203(Input5204 input, String insuplcAdmdvs, String signNo){
		InputData5204 param = new InputData5204();
		param.Assignment("5204", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("5204", json.toString());
		OutputData5204 outputData = JsonUtil.readValue(jsonStr, OutputData5204.class);
		return outputData;
	}
	
	/**
	 * 6.7.2.1文件上传
	 * @param input
	 * @return
	 */
	public static OutputData9101 fun9101(Input9101 input, String signNo){
		InputData9101 param = new InputData9101();
		param.Assignment("9101", "442000", "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("9101", json.toString());
		OutputData9101 outputData = JsonUtil.readValue(jsonStr, OutputData9101.class);
		return outputData;
	}
	
	/**
	 * 6.7.2.2【9102】文件下载
	 * @param input
	 * @return
	 */
	public static OutputData9102 fun9102(Input9102 input, String signNo){
		InputData9102 param = new InputData9102();
		param.Assignment("9102", "442000", "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQg("9102", json.toString());
		OutputData9102 outputData = JsonUtil.readValue(jsonStr, OutputData9102.class);
		return outputData;
	}
	
	/**
	 * 1.16缴费查询接口【90100】
	 * 接口功能号【90100-缴费查询】可以根据传入的人员编号查询出近1年的参保缴费记录，我们可以查询到的缴费记录后，可以根据险种类型、到账类型、费款所属期和到账时间判断参保人的待遇。
通用规则：参保人在医院办理门诊或住院结算时，系统会根据就诊时间判断上月是否有正常的缴费记录，上月缴费正常即可享受待遇。
	 * @param input
	 * @return
	 */
	public static OutputData90100 fun90100(Input90100 input, String insuplcAdmdvs, String signNo){
		InputData90100 param = new InputData90100();
		param.Assignment("90100", insuplcAdmdvs, "1.0", "1", UserContext.getUser().getCodeEmp(), UserContext.getUser().getNameEmp(), signNo);
		param.setInput(input);
		JSONObject json = JSONObject.fromObject(param);
		String jsonStr = YbHttpClass.HttpRequestYbQgJson("90100", json.toString());
		OutputData90100 outputData = JsonUtil.readValue(jsonStr, OutputData90100.class);
		return outputData;
	}
	
	public static Map<String,Object> getPutParamMap(String infno, Boolean flag1101, Map<String,Object> input){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("infno", infno);//	交易编号
		map.put("msgid", "H44020300006"+DateUtils.getDateTimeStr(new Date())+YbHttpClass.getRandomString(4));//	发送方报文ID
		map.put("mdtrtarea_admvs", "441500");//	就医地医保区划
		if(flag1101){
			//调用1101获取参保地
			map.put("insuplc_admdvs", "441500");//	参保地医保区划
		}else{
			map.put("insuplc_admdvs", "");//	参保地医保区划
		}
		map.put("recer_sys_code", "01");//	接收方系统代码
		map.put("dev_no", "");//	设备编号
		map.put("dev_safe_info", "");//	设备安全信息
		map.put("cainfo", "");//	数字签名信息
		map.put("signtype", "");//	签名类型
		map.put("infver", "1.0");//	接口版本号
		map.put("opter_type", "1");//	经办人类别
		map.put("opter", UserContext.getUser().getCodeEmp());//	经办人
		map.put("opter_name", UserContext.getUser().getNameEmp());//	经办人姓名
		map.put("inf_time", DateUtils.getDateTime());//	交易时间
		map.put("fixmedins_code", "GD_SW1097");//	定点医药机构编号
		map.put("fixmedins_name", "广东汕尾红海湾经济开发区东洲街道社区卫生服务中心");//	定点医药机构名称
		map.put("sign_no", "");//	交易签到流水号
		map.put("input", input);//	交易输入
		return map;
	}
	
    private static String getLocalMac(InetAddress ia) throws SocketException {
    	//获取网卡，获取地址
	    byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
	     //System.out.println("mac数组长度："+mac.length);
	    StringBuffer sb = new StringBuffer("");
	    for(int i=0; i<mac.length; i++) {
	    	if(i!=0) {
	    		sb.append("-");
	    	}
	    	//字节转换为整数
	    	int temp = mac[i]&0xff;
	    	String str = Integer.toHexString(temp);
	    	//System.out.println("每8位:"+str);
	    	if(str.length()==1) {
	    		sb.append("0"+str);
	    	}else {
	    		sb.append(str);
	    	}
	   }
	    System.out.println("本机MAC地址:"+sb.toString().toUpperCase());
	    return sb.toString().toUpperCase();
	}
}
