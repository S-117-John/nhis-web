package com.zebone.nhis.webservice.zhongshan.service;

import com.zebone.nhis.webservice.zhongshan.vo.MtsOperRec;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 第三方接口-医技系统-公共服务
 * @author
 *
 */
@Service
public class MtsService {

    /**
     * 更新医技系统操作记录
     * @param pk_pv：就诊id
     * @param code_pi：患者编码
     * @param code_ip:住院号
     * @param times:就诊次数
     * @param req_no:申请单号
     * @param rec_no:医技系统记录号
     * @param mts_type:医技类型(LIS:检验 PACS：检查 ECG:心电)
     * @param mts_name:医技名称
     * @param oper_type:操作类型(检查：P0:预约P1:登记P2:上机P4:编写报告P5:审核报告P7:取消预约P8:取消申请P9:取消报告
     * 							 检验：L0:条码打印L1:采集标本L2:发送标本L3:接收标本L4:编写报告L5:审核报告L6:取消采集L7:取消申请L8:取消报告
     * @param oper_name：操作名称
     * @param oper_time:操作时间
     * @param emp_code:人员编码
     * @param emp_name:人员名称
     */
	@SuppressWarnings("unused")
	public void updateMtsOperRec(Map<String, Object> param) {
		if(param.get("oper_time")==null) return;
		
		String operTimeStr = param.get("oper_time").toString();
    	Date operTime = new Date();
    	try {
    		operTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(operTimeStr);
    	} catch (ParseException e) {
    	   e.printStackTrace();
    	   throw new BusException(" 操作时间格式不对，插入操作日志失败！");
    	}
    	Date now = new Date();
       
		//查询是否有操作记录
		String sql=" select * from mts_oper_rec where pk_pv = ? and req_no=? and mts_type=? and oper_type=? and rec_no=?";	
        MtsOperRec rec = DataBaseHelper.queryForBean(sql, MtsOperRec.class, param.get("pk_pv"),param.get("req_no"),param.get("mts_type"),param.get("oper_type"),param.get("rec_no"));
        if (rec != null) {
        	sql = "update mts_oper_rec set oper_time = ?,emp_code=?,emp_name=?,ts=?  where pk_pv = ? and req_no=? and mts_type=? and oper_type=? and rec_no=?";
            DataBaseHelper.update(sql, operTime, param.get("emp_code"), param.get("emp_name"),now,param.get("pk_pv"),param.get("req_no"),param.get("mts_type"),param.get("oper_type"),param.get("rec_no"));
        } else {
        	rec = new MtsOperRec();
        	rec.setPkOrg(UserContext.getUser().getPkOrg());
        	rec.setPkPv(param.get("pk_pv")==null?"":param.get("pk_pv").toString());
        	rec.setCodePi(param.get("code_pi")==null?"":param.get("code_pi").toString());
        	rec.setCode(param.get("code")==null?"":param.get("code").toString());
        	rec.setTimes(StringUtils.isNotBlank(String.valueOf(param.get("times")))?Integer.parseInt(param.get("times").toString()):0);
        	rec.setPvType(param.get("pv_type")==null?"":param.get("pv_type").toString());
        	rec.setPvName(param.get("pv_name")==null?"":param.get("pv_name").toString());
        	rec.setMtsType(param.get("mts_type")==null?"":param.get("mts_type").toString());
        	rec.setMtsName(param.get("mts_name")==null?"":param.get("mts_name").toString());
        	rec.setOperType(param.get("oper_type")==null?"":param.get("oper_type").toString());
        	rec.setOperName(param.get("oper_name")==null?"":param.get("oper_name").toString());
        	rec.setReqNo(param.get("req_no")==null?"":param.get("req_no").toString());
        	rec.setRecNo(param.get("rec_no")==null?"":param.get("rec_no").toString());
        	rec.setOperTime(operTime);
        	rec.setEmpCode(param.get("emp_code")==null?"":param.get("emp_code").toString());
        	rec.setEmpName(param.get("emp_name")==null?"":param.get("emp_name").toString());
        	rec.setDelFlag("0");
        	rec.setCreator(UserContext.getUser().getPkEmp());
        	rec.setCreateTime(now);
        	rec.setTs(now);
            DataBaseHelper.insertBean(rec);
        }
	}
	
}
