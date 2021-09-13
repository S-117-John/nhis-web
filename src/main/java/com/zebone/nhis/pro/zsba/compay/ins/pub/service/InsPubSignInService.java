package com.zebone.nhis.pro.zsba.compay.ins.pub.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.YbFunUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input9002SignOut;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input9001SignIn;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaSignInQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input9001;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input9002;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData9001;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData9002;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class InsPubSignInService {

	/**
	 * 医保签到
	 * @param ip
	 * @param mac
	 * @return
	 */
	public InsZsbaSignInQg saveSignIn(String ip, String mac){
		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("opterNo", UserContext.getUser().getCodeEmp());
		param_h.put("ip",ip);
		param_h.put("mac", mac);
		param_h.put("beginDate", DateUtils.getDate()+" 00:00:00");
		param_h.put("endDate", DateUtils.getDate()+" 23:59:59");
		StringBuffer sql = new StringBuffer("select top 1 * from ins_sign_in_qg");
		sql.append(" where opter_no = ?");
		sql.append(" and  ip= ?");
		sql.append(" and  mac= ?");
		sql.append(" and sign_time>=to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
		sql.append(" and sign_time<=to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
		sql.append(" and status = '1'");
		sql.append(" order by sign_time desc");
		
		InsZsbaSignInQg signIn = DataBaseHelper.queryForBean(sql.toString(), InsZsbaSignInQg.class, 
				new Object[]{UserContext.getUser().getCodeEmp(), ip, mac, DateUtils.getDate()+" 00:00:00", DateUtils.getDate()+" 23:59:59"});
		if(signIn==null){
			signIn = new InsZsbaSignInQg();
			Input9001SignIn dSignIn = new Input9001SignIn();
			dSignIn.setIp(ip);
			dSignIn.setMac(mac);
			dSignIn.setOpter_no(UserContext.getUser().getCodeEmp());
			Input9001 input  = new Input9001();
			input.setSignIn(dSignIn);
			OutputData9001 paramOut = YbFunUtils.fun9001Client(input);
			if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
				signIn.setOpterNo(dSignIn.getOpter_no());
				signIn.setIp(dSignIn.getIp());
				signIn.setMac(dSignIn.getMac());
				try {
					signIn.setSignTime(DateUtils.parseDate(paramOut.getOutput().getSigninoutb().getSign_time()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				signIn.setSignNo(paramOut.getOutput().getSigninoutb().getSign_no());
				signIn.setStatus("1");
				DataBaseHelper.insertBean(signIn);
				signIn.setCode("0");
				signIn.setMsg("签到成功！");
			}else{
				signIn.setCode("-1");
				signIn.setMsg(paramOut.getErr_msg()+paramOut.getMessage());
			}
		}else{
			signIn.setCode("0");
			signIn.setMsg("已经签到过");
		}
		return signIn;
	}
	
	/**
	 * 医保签退
	 * @param ip
	 * @param mac
	 * @return
	 */
	public void saveSignOut(){
		
		//获取昨天的日期
		Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		
        StringBuffer sql = new StringBuffer("select * from INS_SIGN_IN_QG ");
		sql.append(" where status = '1'");
		sql.append(" and sign_time>=to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
		sql.append(" and sign_time<=to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
		
		List<InsZsbaSignInQg> list = DataBaseHelper.queryForList(sql.toString(), InsZsbaSignInQg.class, 
				new Object[]{date+" 00:00:00", date+" 23:59:59"});
		/*List<InsZsbaSignInQg> list = DataBaseHelper.queryForList(sql.toString(), InsZsbaSignInQg.class, 
				new Object[]{DateUtils.getDate()+" 00:00:00", DateUtils.getDate()+" 23:59:59"});*/
		for (InsZsbaSignInQg signIn : list) {
			Input9002SignOut signOut = new Input9002SignOut();
			signOut.setSign_no(signIn.getSignNo());
			signOut.setOpter_no(signIn.getOpterNo());
			Input9002 input  = new Input9002();
			input.setSignOut(signOut);
			OutputData9002 paramOut = YbFunUtils.fun9002(input);
			if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
				signIn.setStatus("2");
				signIn.setSignOutTime(paramOut.getOutput().getSignoutoutb().getSign_time());
				DataBaseHelper.updateBeanByPk(signIn);
			}else{
				signIn.setStatus("3");
				signIn.setMsg(paramOut.getErr_msg()+paramOut.getMessage());
				DataBaseHelper.updateBeanByPk(signIn);
			}
		}
	}
	
}
