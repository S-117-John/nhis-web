package com.zebone.nhis.pro.zsba.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.sd.vo.EnoteResDataVo;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlInvoice;
import com.zebone.nhis.pro.zsba.mz.pub.support.EBillHttpUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

/**
 * 定时针对有电子票据冲红状态但是没有其他冲红信息的发票调用冲红接口回写补全信息(待测试-暂未启用)
 * @author cavancao
 */
@Service
public class ZsbaEBillService {

	private final static Logger logger = LoggerFactory.getLogger("nhis.quartz");
	
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	@SuppressWarnings("unchecked")
	public void executeWriteOffEBill(QrtzJobCfg cfg){  
		try {
			logger.debug("电子票据冲红定时任务开始==>>"+DateUtils.getDateTime());
			StringBuffer qrySql = new StringBuffer();
			qrySql.append("select inv.PK_INVOICE as pkInvoice,inv.ebillbatchcode as ebillbatchcode,");
			qrySql.append("inv.billbatchcode as billbatchcode,");
			qrySql.append("inv.ebillno as billno,inv.PK_EMP_CANCEL as pkempcancel,inv.PK_EMP_INV pkempinv,");
			qrySql.append("inv.code_sn as codeinv,");
			qrySql.append("'退费' as reason,");
			qrySql.append("emp.code_emp as codeemp,");
			qrySql.append("case when cate.EU_TYPE is null then ");
			qrySql.append("(case when st.EU_PVTYPE != '3' then '0' else '1' end) ");
			qrySql.append("else cate.EU_TYPE end eu_type ");
			qrySql.append("from bl_invoice inv ");
			qrySql.append("inner join bl_st_inv si on inv.pk_invoice=si.pk_invoice ");
			qrySql.append("inner join bl_settle st on si.pk_settle=st.pk_settle ");
			qrySql.append("inner join bd_ou_employee emp on emp.pk_emp = inv.pk_emp_inv ");
			qrySql.append("left join BD_INVCATE cate on inv.PK_INVCATE = cate.PK_INVCATE ");
			qrySql.append("where inv.ebillno is not null ");
			qrySql.append("and (inv.FLAG_CANCEL='1' or inv.FLAG_CANCEL_EBILL='1') and (inv.EBILLNO_CANCEL is null or inv.EBILLNO_CANCEL='')");
			List<Map<String,Object>> invList = DataBaseHelper.queryForList(qrySql.toString(), new Object[]{});
			Date currDate = null;//当前日期时间
			for(Map<String,Object> inv : invList){
				currDate = new Date();
				//结算收费员信息(发票记录里存在取消发票人员则直接使用，否则用发票开立人员信息)
				String pkEmp = (inv.get("pkempcancel")!=null?CommonUtils.getString(inv.get("pkempcancel")):CommonUtils.getString(inv.get("pkempinv")));
				if(StringUtils.isNotEmpty(pkEmp)){
					BdOuEmployee u = DataBaseHelper.queryForBean("select * from BD_OU_EMPLOYEE where PK_EMP=?",BdOuEmployee.class,new Object[]{pkEmp});
					//调用电子票据接口
					Map<String,Object> reqMap = new HashMap<>(16);
					reqMap.put("billBatchCode", inv.get("ebillbatchcode"));//电子票据代码
					reqMap.put("billNo", inv.get("billno"));//电子票据号码
					reqMap.put("reason", inv.get("reason"));//冲红原因
					reqMap.put("operator", u.getNameEmp());//经办人
					reqMap.put("busDateTime", DateUtils.getDate("yyyyMMddHHmmssSSS"));//业务发生时间
					reqMap.put("placeCode", getPlaceCode(MapUtils.getString(inv,"euType"), u));//开票点编码
					String dataJson = JsonUtil.writeValueAsString(reqMap);//将Data内容转换为json格式再base64编码,编码字符集UTF-8
					EnoteResDataVo resVo = EBillHttpUtils.enoteRts(dataJson,"1.0","writeOffEBill");//发送请求
					Map<String,Object> dataVo = JsonUtil.readValue(resVo.getMessage(), Map.class);
					if(dataVo!=null && dataVo.size()>0){
						String findBlInvoiceSql = "select top 1 * from bl_invoice where pk_invoice=?";
						ZsbaBlInvoice blInvoice = DataBaseHelper.queryForBean(findBlInvoiceSql, ZsbaBlInvoice.class, new Object[]{MapUtils.getString(inv,"pkInvoice")});
						//赋值电子票据冲红信息
						if(blInvoice!=null){
							blInvoice.setEbillbatchcodeCancel(CommonUtils.getString(dataVo.get("eScarletBillBatchCode")));
							blInvoice.setEbillnoCancel(CommonUtils.getString(dataVo.get("eScarletBillNo")));
							blInvoice.setCheckcodeCancel(CommonUtils.getString(dataVo.get("eScarletRandom")));
							blInvoice.setDateEbillCancel(DateUtils.strToDate(CommonUtils.getString(dataVo.get("createTime")),"yyyyMMddHHmmssSSS"));
							blInvoice.setQrcodeEbillCancel(Base64.decode(CommonUtils.getString(dataVo.get("billQRCode"))));
							blInvoice.setUrlEbillCancel(CommonUtils.getString(dataVo.get("pictureUrl")));
							blInvoice.setUrlNetebillCancel(CommonUtils.getString(dataVo.get("pictureNetUrl")));
							blInvoice.setFlagCancelEbill("1");
							blInvoice.setPkEmpCancelEbill(u.getPkEmp());
							blInvoice.setNameEmpCancelEbill(u.getNameEmp());
							blInvoice.setFlagCcCancelEbill("0");
							blInvoice.setDateEbillCancel(currDate);
							blInvoice.setFlagCancel(EnumerateParameter.ONE);
							blInvoice.setDateCancel(currDate);
							blInvoice.setPkEmpCancel(u.getPkEmp());
							blInvoice.setNameEmpCancel(u.getNameEmp());
							blInvoice.setFlagBack("1");//退票标记
							DataBaseHelper.update(DataBaseHelper.getUpdateSql(ZsbaBlInvoice.class), blInvoice);
						}
					}
				}
			}
			logger.debug("电子票据冲红定时任务结束==>>"+DateUtils.getDateTime());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("定时针对有电子票据冲红状态但是没有其他冲红信息的发票调用冲红接口回写补全信息发生异常："+e.getMessage());
		}
	}
	
	//获取开票点代码
	private String getPlaceCode(String euType, BdOuEmployee u){
		if(StringUtils.isBlank(euType) || u == null){
			return u.getCodeEmp();
		}
		if(EnumerateParameter.ONE.equals(euType)){
			//住院
			return u.getCodeEmp();
		} else if(EnumerateParameter.ZERO.equals(euType)){
			//门诊
			return u.getCodeEmp();
		}
		return null;
	}
}
