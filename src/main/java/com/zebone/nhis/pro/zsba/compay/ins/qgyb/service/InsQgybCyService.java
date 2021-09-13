package com.zebone.nhis.pro.zsba.compay.ins.qgyb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.pro.zsba.compay.ins.pub.service.InsPubSignInService;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.YbFunUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2302Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2404Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaFeedetailQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2302;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2403;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2404;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2302;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2404;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * 医保处理工具  
 * @author Administrator
 *
 */
@Service
public class InsQgybCyService {

	@Autowired
	private InsPubSignInService insPubSignInService;
	
	/**
	 * 取消医保登记
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> Fun2404(String param,IUser user){
		Map<String, Object> retuanMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String mdtrtId = jo.getString("mdtrtId");
		String psnNo = jo.getString("psnNo");
		String insuplcAdmdvs = jo.getString("insuplcAdmdvs");//参保地编码
		Input2404Data data = new Input2404Data();
		data.setMdtrt_id(mdtrtId);
		data.setPsn_no(psnNo);
		Input2404 input = new Input2404();
		input.setData(data);
		//OutputData2404 out = YbFunUtils.fun2404(input, insuplcAdmdvs);
/*		if(out.getInfcode()==0){
			retuanMap.put("code",  0);
			retuanMap.put("msg", "取消医保登记成功");
		}else{
			retuanMap.put("code",  out.getInfcode());
			retuanMap.put("msg", "取消医保登记失败");
		}*/
		return retuanMap;
	}

	/**
	 * 费用明细撤销
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> Fun2302(String param,IUser user){
		Map<String, Object> retuanMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String feedetlSn = jo.getString("feedetlSn");//费用明细流水号 传入“0000”时删除全部
		String mdtrtId = jo.getString("mdtrtId");
		String psnNo = jo.getString("psnNo");
		String insuplcAdmdvs = jo.getString("insuplcAdmdvs");//参保地编码
		Input2302Data data = new Input2302Data();
		data.setMdtrt_id(mdtrtId);
		data.setPsn_no(psnNo);
		data.setFeedetl_sn(feedetlSn);
		Input2302 input = new Input2302();
		List<Input2302Data> dataList = new ArrayList<Input2302Data>();
		dataList.add(data);
		//input.setData(dataList);
		//OutputData2302 out = YbFunUtils.fun2302(input, insuplcAdmdvs);
/*		if(out.getInfcode()==0){
			retuanMap.put("code",  0);
			retuanMap.put("msg", "费用明细撤销成功");
		}else{
			retuanMap.put("code",  out.getInfcode());
			retuanMap.put("msg", "费用明细撤销失败");
		}*/
		return retuanMap;
	}
	
	/**
	 * 根据就诊id或者医保就诊记录号，获取医保费用明细
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InsZsbaFeedetailQg> getFeedetailList(String param,IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String mdtrtId = jo.getString("mdtrtId");
		String pkPv = jo.getString("pkPv");
		List<InsZsbaFeedetailQg> list = new ArrayList<InsZsbaFeedetailQg>();
		if(mdtrtId!=null&&mdtrtId.trim().length()!=0 && !mdtrtId.equals("null")){
			list = DataBaseHelper.queryForList("select * from INS_FEEDETAIL_QG where mdtrt_id = ? and del_flag = '0'", InsZsbaFeedetailQg.class, mdtrtId);
		}
		if(pkPv!=null&&pkPv.trim().length()!=0){
			list = DataBaseHelper.queryForList("select * from INS_FEEDETAIL_QG where pk_pv = ? and del_flag = '0'", InsZsbaFeedetailQg.class, pkPv);
		}
		if(list==null || list.size()==0){
			if(mdtrtId!=null&&mdtrtId.trim().length()!=0){
				list = DataBaseHelper.queryForList("select * from INS_FEEDETAIL2301_QG where mdtrt_id = ? and del_flag = '0'", InsZsbaFeedetailQg.class, mdtrtId);
			}
			if(pkPv!=null&&pkPv.trim().length()!=0){
				list = DataBaseHelper.queryForList("select * from INS_FEEDETAIL2301_QG where pk_pv = ? and del_flag = '0'", InsZsbaFeedetailQg.class, pkPv);
			}
		}
		return list;
	}

}
