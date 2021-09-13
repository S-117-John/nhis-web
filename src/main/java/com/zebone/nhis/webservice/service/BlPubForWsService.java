package com.zebone.nhis.webservice.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import org.apache.commons.collections.CollectionUtils;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdPayer;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.webservice.dao.BlPubForWsMapper;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.support.OtherRespJson;
import com.zebone.nhis.webservice.vo.BlPubParamVo;
import com.zebone.nhis.webservice.vo.LbSHRequestVo;
import com.zebone.nhis.webservice.vo.OpCgTransforVo;
import com.zebone.nhis.webservice.vo.tmisvo.ResponseHISFeeVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

/**
 * 收费域webservcie专用公共服务
 *
 * @author yangxue
 */
@Service
public class BlPubForWsService {
    @Resource
    private BlPubForWsMapper blPubForWsMapper;

    /**
     * 查询患者待缴费信息(调用HIS原服务)
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getUnpaidFeeByOp(Map<String, Object> paramMap) {
        ApplicationUtils apputil = new ApplicationUtils();
        User u = new User();
        u.setPkOrg(CommonUtils.getString(paramMap.get("pkOrg")));
        UserContext.setUser(u);
        ResponseJson rs =  apputil.execService("BL", "OpCgSettlementService", "queryPatiCgInfoNotSettle", paramMap,u);
        return (List<Map<String, Object>>)rs.getData();
    }

    /**
     * 查询患者已缴费信息
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> getPaidFeeByOp(Map<String, Object> paramMap) {
        return blPubForWsMapper.getPaidFeeByOp(paramMap);
    }

    /**
     * 查询患者已缴费信息（第三方缴费记录）
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> getThirdPaidFeeByOp(Map<String, Object> paramMap) {
        return blPubForWsMapper.getThirdPaidFeeByOp(paramMap);
    }
    
    /**
     * 查询门诊缴费明细
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> getPayDetailByOp(Map<String, Object> paramMap) {
        String pkSettle = paramMap.get("pkSettle").toString();
        return blPubForWsMapper.getPayDetailByOp(pkSettle);
    }

    public Map<String, Object> cardRecharge(Map<String, Object> paramMap) {
        //TODO
        return null;
    }

    public Map<String, Object> prePayRecharge(Map<String, Object> paramMap) {
    	 //TODO
        return null;
    }

    /**
     * 功能描述：查询住院预交金充值记录
     *
     * @param paramMap<pkPi:患者唯一标识>
     * @return java.util.List<java.util.Map               <               java.lang.String               ,               java.lang.Object>>
     * @author wuqiang
     * @date 2018/9/11
     */
    public List<Map<String, Object>> getPrePayDetail(Map<String, Object> paramMap) {
        return blPubForWsMapper.getPrePayDetail(paramMap);
    }

    /**
     * 功能描述：查询患者当前住院信息费用清单
     *
     * @param paramMap<pkPi:患者唯一标识 ,pkPv:患者就诊标识>
     * @return java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     * @author wuqiang
     * @date 2018/9/11
     */
    public List<Map<String, Object>> getIpCgDetail(Map<String, Object> paramMap) {
        return  blPubForWsMapper.getIpCgDetail(paramMap);
    }
    
    /**
     * 功能描述：查询患者当前住院信息费用清单
     *
     * @param paramMap<pkPi:患者唯一标识 ,dateCg:费用日期,catecode:费用类别>
     * @return java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     * @author wuqiang
     * @date 2018/9/11
     */
    public List<Map<String, Object>> getIpCgDayDetail(Map<String, Object> paramMap) {
        String pkPi = CommonUtils.getString(paramMap.get("pkPi"));
        String dateCg = paramMap.get("dateCg").toString();
        String catecode = CommonUtils.getString(paramMap.get("catecode"));
        Map<String ,Object> map=new HashMap<>();
        //时间处理
        Date date = DateUtils.strToDate(dateCg);
        String dateBegin = DateUtils.dateToStr("yyyyMMddHHmmss",DateUtils.getDateMorning(date, 0));
        String dateEnd = DateUtils.dateToStr("yyyyMMddHHmmss",DateUtils.getDateMorning(date,1));
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        map.put("pkPi",pkPi);
        if(!CommonUtils.isEmptyString(catecode)){
        	map.put("catecode", catecode);
        }
        return  blPubForWsMapper.getIpCgDayDetail(map);
    }
    /**
     * 功能描述：查询患者交易明细
     *
     * @param ParamMap<pkPi：患者唯一标识>
     * @return java.util.List<java.util.Map               <               java.lang.String       ,       java.lang.Object>>
     * @author wuqiang
     * @date 2018/9/11
     */
    public List<Map<String, Object>> getDepositInfo(Map<String, Object> paramMap) {
        String pkPi = paramMap.get("pkPi").toString();
        return blPubForWsMapper.getDepositInfo(pkPi);
    }
    
    /**
     * 灵璧自助机支付数据保存
     * @param requ
     * @return
     * @throws ParseException 
     */
    public void  LbPayment(BlDeposit vo,LbSHRequestVo requ ,Map<String, Object> pvMap,User user,String pkDepopi) throws ParseException{
    	BlExtPay extPay = new BlExtPay();        
        	// 写外部交易接口支付bl_ext_pay
        	extPay.setPkExtpay(NHISUUID.getKeyId());
        	extPay.setPkOrg(user.getPkOrg());
        	//extPay.setAmount(BigDecimal.valueOf(CommonUtils.getDouble(MsgUtils.getPropValueStr(pvMap,"payAmt"))));//金额
        	extPay.setAmount(BigDecimal.valueOf(Double.valueOf(requ.getPayAmt())));
        	extPay.setDtBank("");
        	extPay.setNameBank(""); 
        	extPay.setEuPaytype(requ.getPayType());//支付方式7：微信，8：支付宝
        	extPay.setFlagPay("1");
        	extPay.setSysname("");
        	extPay.setSerialNo(requ.getQrCodeInfoVo().get(0).getOrderno());//订单号
        	extPay.setTradeNo(requ.getQrCodeInfoVo().get(0).getFlowno());//交易流水号
        	extPay.setPkPi(LbSelfUtil.getPropValueStr(pvMap,"pkPi"));
        	extPay.setPkPv(LbSelfUtil.getPropValueStr(pvMap,"pkPv"));
        	extPay.setResultPay("");
        	extPay.setRefundNo("");
        	extPay.setDateRefund(null);
        	extPay.setEuBill("0");
        	extPay.setPkBill("");
        	extPay.setDateBill(null);
        	extPay.setDescPay("自助机金额缴纳"+requ.getQrCodeInfoVo().get(0).getPaymethod());
        	extPay.setDatePay(new Date());
        	if(null != vo){
        		extPay.setPkDepo(vo.getPkDepo());//BL_DEPOSIT主键
        	}
        	if(StringUtils.isNotBlank(pkDepopi)){
        		extPay.setPkDepopi(pkDepopi);//BL_DEPOSIT_PI主键-发卡时使用
        	}
        	extPay.setCreator(user.getPkEmp());
        	extPay.setCreateTime(new Date());
        	extPay.setDelFlag("0");
        	extPay.setTs(new Date());
        	extPay.setModifier(LbSelfUtil.getPropValueStr(pvMap,"pkEmp"));
        	DataBaseHelper.insertBean(extPay);
    	extPay = null;
    }

	/**插入BlExtPay**/
	public void  inserBlExtPay(BlExtPay extPay){
		DataBaseHelper.insertBean(extPay);
	}
	/**
     * 坪山口腔电子健康码支付数据保存
     * @param requ
     * @return
     * @throws ParseException
     */
    public void  PskqHealthPayment(BlDeposit vo,Map<String, Object> paramMap ,Map<String, Object> pvMap,User user,String pkDepopi) throws ParseException{
    	BlExtPay extPay = new BlExtPay();
        	// 写外部交易接口支付bl_ext_pay
        	extPay.setPkExtpay(NHISUUID.getKeyId());
        	extPay.setPkOrg(user.getPkOrg());
        	//extPay.setAmount(BigDecimal.valueOf(CommonUtils.getDouble(MsgUtils.getPropValueStr(pvMap,"payAmt"))));//金额
        	extPay.setAmount(BigDecimal.valueOf(Double.valueOf(CommonUtils.getPropValueStr(paramMap, "payAmt"))));
        	extPay.setDtBank("");
        	extPay.setNameBank("");

        	extPay.setEuPaytype(CommonUtils.getPropValueStr(paramMap, "payType"));
        	extPay.setFlagPay("1");
        	extPay.setSysname(CommonUtils.getPropValueStr(paramMap, "sysname"));
        	extPay.setSerialNo(CommonUtils.getPropValueStr(paramMap, "orderno"));//订单号
        	extPay.setTradeNo(CommonUtils.getPropValueStr(paramMap, "flowno"));//交易流水号
        	extPay.setPkPi(CommonUtils.getPropValueStr(pvMap,"pkPi"));
        	extPay.setPkPv(CommonUtils.getPropValueStr(pvMap,"pkPv"));
        	extPay.setResultPay("");
        	extPay.setRefundNo("");
        	extPay.setDateRefund(null);
        	extPay.setEuBill("0");
        	extPay.setPkBill("");
        	extPay.setDateBill(null);
        	extPay.setDescPay(CommonUtils.getPropValueStr(paramMap, "descPay"));
        	extPay.setDatePay(DateUtils.strToDate(paramMap.get("paytime").toString(), "yyyy-MM-dd HH:mm:ss"));
        	if(null != vo){
        		extPay.setPkDepo(vo.getPkDepo());//BL_DEPOSIT主键
        	}
        	if(StringUtils.isNotBlank(pkDepopi)){
        		extPay.setPkDepopi(pkDepopi);//BL_DEPOSIT_PI主键-发卡时使用
        	}
        	extPay.setCreator(user.getPkEmp());
        	extPay.setCreateTime(new Date());
        	extPay.setDelFlag("0");
        	extPay.setTs(new Date());
        	extPay.setModifier(CommonUtils.getPropValueStr(pvMap,"pkEmp"));
        	DataBaseHelper.insertBean(extPay);
    	extPay = null;
    }
    
    public void lbUpdateBlExtPay(Map<String, Object> param){
    	blPubForWsMapper.updateBlExtPayFlagpay(param);
    }
    
    //微信使用，后期修改优化
    public void LbUpdateBlExtPay(LbSHRequestVo requ){
        if(BeanUtils.isNotNull(requ.getQrCodeInfoVo())){
     	   Map<String, Object> param =new HashMap<String, Object>();
     	   param.put("tradeNo", requ.getQrCodeInfoVo().get(0).getFlowno());
     	   param.put("serialNo", requ.getQrCodeInfoVo().get(0).getOrderno());
     	   blPubForWsMapper.updateBlExtPayFlagpay(param);
     	}
     }

    /**
	 * 根据医保计划主键查询医保信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public BdHp qryBdHpInfo(Map<String, Object> mapParam) throws BusException {
		String sql = "select * from bd_hp where pk_hp=?  and (del_flag = '0' or del_flag is null) ";
		BdHp bdHp = DataBaseHelper.queryForBean(sql, BdHp.class, new Object[] { mapParam.get("pkHp") });
		return bdHp;
	}
    
    public BlDeposit LbInsertBlDeposit(String eudt, BlDeposit vo,Map<String, Object> queryForMap,LbSHRequestVo requ,Map<String, Object> PatMap,User user) throws ParseException{
 
    		vo.setDtPaymode(requ.getPayType());
    		vo.setEuDptype(eudt);//收付款类型9:住院预交金
    		vo.setPkPi(LbSelfUtil.getPropValueStr(PatMap,"pkPi"));
    		vo.setPkPv(LbSelfUtil.getPropValueStr(PatMap,"pkPv"));
    		vo.setEuDirect("1");//收退方向
    		vo.setAmount(BigDecimal.valueOf(Double.valueOf(requ.getPayAmt())));
    		if(("7").equals(requ.getPayType()) || ("8").equals(requ.getPayType())){
    		  if(StringUtils.isNotBlank(requ.getQrCodeInfoVo().get(0).getPaytime())){
    			vo.setPayInfo(requ.getQrCodeInfoVo().get(0).getOrderno());
    		  }
    		}
    		vo.setDatePay(new Date());
    		if(null !=queryForMap){
    			vo.setPkEmpinvoice(LbSelfUtil.getPropValueStr(queryForMap,"pkEmpinv"));
        		vo.setReptNo(LbSelfUtil.getPropValueStr(queryForMap,"curCodeInv"));
    		}
    		vo.setPkEmpPay(user.getPkEmp());
    		vo.setNameEmpPay("自助机:"+requ.getDeviceid());
    		
    		//查询患者账户主键
    		Map<String, Object> piAccMap = DataBaseHelper.queryForMap("select * from pi_acc where pk_pi= ?", LbSelfUtil.getPropValueStr(PatMap,"pkPi"));
    		
    		vo.setPkAcc(LbSelfUtil.getPropValueStr(piAccMap,"pkPiacc"));//患者账户主键
    		vo.setFlagSettle("0");//结算标志
    		vo.setFlagAcc("0");//账户支付标志
    		vo.setNote("住院预交金");
    		vo.setEuPvtype(LbSelfUtil.getPropValueStr(PatMap,"euPvtype"));
            
    		vo.setPkDept(user.getPkDept());
    		vo.setPkOrg(user.getPkOrg());
    		vo.setCreator(user.getPkEmp());
    		vo.setCreateTime(new Date());
    	    vo.setDelFlag("0");
    	    vo.setTs(new Date());
    	    vo.setModifier(user.getPkEmp());
    	return vo;
    } 
    
    /**
     * 坪山口腔医院电子健康码住院预交金缴费
     * @param eudt
     * @param vo
     * @param queryForMap
     * @param paramMap
     * @param PatMap
     * @param user
     * @return
     * @throws ParseException
     */
    public BlDeposit PskqHealthInsertBlDeposit(String eudt, BlDeposit vo,Map<String, Object> queryForMap,Map<String, Object> paramMap,Map<String, Object> PatMap,User user) throws ParseException{
    	 
		vo.setDtPaymode(CommonUtils.getPropValueStr(paramMap,"payType"));
		vo.setEuDptype(eudt);//收付款类型9:住院预交金
		vo.setPkPi(CommonUtils.getPropValueStr(PatMap,"pkPi"));
		vo.setPkPv(CommonUtils.getPropValueStr(PatMap,"pkPv"));
		vo.setEuDirect("1");//收退方向
		vo.setAmount(BigDecimal.valueOf(Double.valueOf(CommonUtils.getPropValueStr(paramMap, "payAmt"))));
		vo.setPayInfo(CommonUtils.getPropValueStr(paramMap, "orderno"));
		vo.setDatePay(DateUtils.strToDate(paramMap.get("paytime").toString(), "yyyy-MM-dd HH:mm:ss"));
		if(null !=queryForMap){
			vo.setPkEmpinvoice(CommonUtils.getPropValueStr(queryForMap,"pkEmpinv"));
    		vo.setReptNo(CommonUtils.getPropValueStr(queryForMap,"curCodeInv"));
		}
		vo.setPkEmpPay(user.getPkEmp());
		vo.setNameEmpPay(user.getNameEmp());
		
		//查询患者账户主键
		Map<String, Object> piAccMap = DataBaseHelper.queryForMap("select * from pi_acc where pk_pi= ?", CommonUtils.getPropValueStr(PatMap,"pkPi"));
		
		vo.setPkAcc(CommonUtils.getPropValueStr(piAccMap,"pkPiacc"));//患者账户主键
		vo.setFlagSettle("0");//结算标志
		vo.setFlagAcc("0");//账户支付标志
		vo.setNote("住院预交金");
		vo.setEuPvtype(CommonUtils.getPropValueStr(PatMap,"euPvtype"));
        
		vo.setPkDept(user.getPkDept());
		vo.setPkOrg(user.getPkOrg());
		vo.setCreator(user.getPkEmp());
		vo.setCreateTime(new Date());
	    vo.setDelFlag("0");
	    vo.setTs(new Date());
	    vo.setModifier(user.getPkEmp());
	return vo;
} 
	
	/**
	 * 根据付款方类型查询付款方主键
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public BdPayer qryBdPayerByEuType(Map<String, Object> mapParam) throws BusException {

		String sql = "select * from bd_payer where eu_type='0' and pk_org=?";
		BdPayer bdPayer = DataBaseHelper.queryForBean(sql, BdPayer.class, new Object[] { mapParam.get("pkOrg") });
		return bdPayer;
	}
	
	/**
	 * 根据pk_pv查询发票明细信息
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public List<BlInvoiceDt> qryInfoForBlInvoiceDt(Map<String, Object> mapParam) throws BusException {

		StringBuilder sql = new StringBuilder();
		Object pkBlOpDtInSql = mapParam.get("pkBlOpDtInSql");
		sql.append("select invitem.pk_invcateitem pk_bill,invitem.code code_bill,invitem.name name_bill, sum(bl.amount) amount from bd_invcate_itemcate invitemcate");
		sql.append(" inner join bd_invcate_item invitem on invitemcate.pk_invcateitem=invitem.pk_invcateitem inner join bd_invcate inv on invitem.pk_invcate=inv.pk_invcate");
		sql.append(" inner join bl_op_dt bl on invitemcate.pk_itemcate=bl.pk_itemcate where inv.pk_org = ?  and inv.eu_type = '0' and bl.pk_pv = ? ");
		if (pkBlOpDtInSql != null) {
			sql.append(" and bl.pk_cgop in (" + pkBlOpDtInSql.toString() + ")");
		} else {// 挂号
			sql.append(" and  bl.flag_pv = 1");
		}
		sql.append(" and invitemcate.del_flag='0' and invitem.del_flag='0' and inv.del_flag='0' and bl.del_flag='0'");
		sql.append(" group by invitem.pk_invcateitem, invitem.code, invitem.name");
		List<BlInvoiceDt> blInvoiceDts = DataBaseHelper.queryForList(sql.toString(), BlInvoiceDt.class,
				new Object[] { mapParam.get("pkOrg"), mapParam.get("pkPv") });
		return blInvoiceDts;
	}

	/**
	 * 根据收费项目主键查询账单码
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryBillCodeByPkItem(Map<String, Object> mapParam) throws BusException {

		StringBuffer sql = new StringBuffer();
		sql.append("select distinct invitem.code from bd_invcate_item invitem");
		sql.append(" inner join bd_invcate_itemcate cate on invitem.pk_invcateitem=cate.pk_invcateitem");
		sql.append(" inner join bd_invcate inv on invitem.pk_invcate=inv.pk_invcate ");
		if (LbSelfUtil.converToTrueOrFalse(mapParam.get("flagPd").toString())) {
			sql.append(" inner join bd_pd item on cate.pk_itemcate=item.pk_itemcate");
			sql.append(" where item.pk_pd = '"+mapParam.get("pkItem")+"' ");
		} else {
			sql.append(" inner join bd_item item on cate.pk_itemcate=item.pk_itemcate");
			sql.append(" where item.pk_item = '"+mapParam.get("pkItem")+"' ");
		}
		sql.append(" and inv.eu_type = ? and invitem.del_flag ='0' and invitem.pk_org=? and cate.del_flag ='0' and inv.del_flag='0' and item.del_flag='0' ");
		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(),
				new Object[] {mapParam.get("euType"), mapParam.get("pkOrg") });
		return mapResult;
	}
	
	/**
	 * 根据收费项目主键查询核算码
	 * @param mapParam
	 * @return
	 * @throws BusException
	 */
	public Map<String, Object> qryAccountCodeByPkItem(Map<String, Object> mapParam) throws BusException {

		StringBuffer sql = new StringBuffer();
		sql.append("select distinct bdaudit.code  from bd_audit bdaudit");
		sql.append(" inner join bd_audit_itemcate auditcateitem on bdaudit.pk_audit = auditcateitem.pk_audit");
		sql.append(" inner join bd_itemcate itemcate on itemcate.pk_itemcate = auditcateitem.pk_itemcate ");
		if (LbSelfUtil.converToTrueOrFalse(mapParam.get("flagPd").toString())) {
			sql.append(" inner join bd_pd item on itemcate.pk_itemcate=item.pk_itemcate");
			sql.append(" where item.pk_pd = ? ");
		} else {
			sql.append(" inner join bd_item item on itemcate.pk_itemcate=item.pk_itemcate");
			sql.append(" where item.pk_item = ? ");
		}
		sql.append(" and bdaudit.del_flag !='1' and bdaudit.pk_org=? and auditcateitem.del_flag !='1' and itemcate.del_flag!='1' and item.del_flag!='1' ");
		Map<String, Object> mapResult = DataBaseHelper.queryForMap(sql.toString(), new Object[] { mapParam.get("pkItem"), mapParam.get("pkOrg") });
		return mapResult;
	}	
	
	public List<Map<String, Object>> getDeptSchInfo(Map<String, Object> paramMap) {
    	Map<String ,Object> map=new HashMap();
    	if (paramMap.get("pkDept")!=null&&!StringUtils.isEmpty(paramMap.get("pkDept").toString())) {
    		map.put("pkDept",paramMap.get("pkDept").toString());
		}
    	if (paramMap.get("pkOrg")!=null&&!StringUtils.isEmpty(paramMap.get("pkOrg").toString())) {
    		map.put("pkOrg",paramMap.get("pkOrg").toString());
    	}
    	if (paramMap.get("pkOrgarea")!=null&&!StringUtils.isEmpty(paramMap.get("pkOrgarea").toString())) {
    		map.put("pkOrgarea",paramMap.get("pkOrgarea").toString());
    	}
        String dateBegin=null;
        String dateEnd=null;
        String  euSrvtype=null;
      if(paramMap.get("dateBegin")!=null){
            dateBegin=paramMap.get("dateBegin").toString();
        }
        if(paramMap.get("dateEnd")!=null){
            dateEnd=paramMap.get("dateEnd").toString();
        }
        if(paramMap.get("euSrvtype")!=null){
        	euSrvtype=paramMap.get("euSrvtype").toString();
        }
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        map.put("euSrvtype",euSrvtype);
        return blPubForWsMapper.getDeptSchInfo(map);
    }
	
	/**
	 * 根据患者查询有效就诊记录
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getEffectPvnfo(Map<String,Object> paramMap){
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkPi")))
			throw new BusException("未获取到患者主键信息！");
		paramMap.put("curDate", DateUtils.getDateStr(new Date())+"000000");//当天23点
		return blPubForWsMapper.queryPvList(paramMap);
	}
	
	/**
	 * 微信服务订单查询
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryOrderCenterInfo(Map<String,Object> paramMap){
		 List<Map<String,Object>> payInfoList = blPubForWsMapper.queryOrderCenterInfo(paramMap);
		 return payInfoList;
		 
	} 
	
	/**
	 * 微信服务查询挂号信息
	 * @param pkPv
	 * @return
	 */
	public List<Map<String,Object>> queryReg(String pkPv){
		List<Map<String,Object>> regList = blPubForWsMapper.queryReg(pkPv);
		return regList;
	}
	
	/**
	 * 微信服务查询结算信息
	 * @param pkPv
	 * @return
	 */
	public List<Map<String,Object>> querySettle(String pkPv){
		List<Map<String,Object>> settleList = blPubForWsMapper.querySettle(pkPv);
		return settleList;
	}
	
	/**
	 * 微信服务查询出院结算信息
	 * @param pkPv
	 * @return
	 */
    public List<Map<String,Object>> queryInSettle(String pkPv,String payOrderSn){
    	List<Map<String,Object>> inSettleList = blPubForWsMapper.queryInSettle(pkPv,payOrderSn);
    	return inSettleList;
    }
    
	/**
	 * 微信服务查询住院预缴金信息
	 * @param pkPv
	 * @return
	 */
	public List<Map<String,Object>> queryIpFee(String pkPv,String payOrderSn){
		List<Map<String,Object>> ipFeeList = blPubForWsMapper.queryIpFee(pkPv,payOrderSn);
		return ipFeeList;
	}
	
    /**
     * 根据有效就诊记录查询门诊待缴费用
     * @param pkPv
     */
    public List<Map<String,Object>>  getPvToChargeInfo(Map<String,Object> paramMap){
    	 return blPubForWsMapper.queryBlOpDtsToPay(paramMap);
    }
    
	/**
	 * 查询传入的记费明细总和
	 * @param pkCgops
	 * @return
	 */
	public BigDecimal getBlOpDtAmountSum(List<String> pkCgops){
		return  blPubForWsMapper.getBlOpDtAmountSum(pkCgops);
	}
	
    /**
     * 门诊收费结算
     * @param OpCgTransforVo param
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String,Object> chargeOpSettle(OpCgTransforVo param){
    	if(param==null) 
    	    throw new BusException("未传入需要记费的数据！");
		List<BlOpDt> blOpDts = param.getBlOpDts();
		if (CollectionUtils.isEmpty(blOpDts))
			throw new BusException("未传入需要记费的数据！");
		if(CommonUtils.isEmptyString(param.getPkPi()))
			throw new BusException("结算时请传入患者主键！");
		if(CommonUtils.isEmptyString(param.getPkPv()))
			throw new BusException("结算时请传入就诊主键！");
		if(CommonUtils.isEmptyString(param.getPkOrgSt()))
			throw new BusException("结算时请传入结算机构主键！");
    	ApplicationUtils apputil = new ApplicationUtils();
    	User u = new User();
		u.setPkOrg(param.getPkOrgSt());
		u.setPkDept(param.getPkDeptSt());
		UserContext.setUser(u);
		
		ResponseJson  rs =  apputil.execService("BL", "ClinicPubService", "blOpSettle", param,u);
		if(null==rs.getData() && null!=rs.getDesc()){
			throw new BusException(rs.getDesc());
		}
		return (Map<String, Object>) rs.getData();
    }
    
    /**
     * 预缴费用
     */
    public List<Map<String,Object>>  queryBlOpDtsToPrePay(List<String> pkCgops){
    	return blPubForWsMapper.queryBlOpDtsToPrePay(pkCgops);
    }

    /**
     * 查询患者输血明细费用（灵璧输血）
     * @author ds
     */
    public List<ResponseHISFeeVo>  queryBloodCost(Map<String,Object> param){
    	return blPubForWsMapper.queryBloodCost(param);
    }

	/**
	 * 费用补录
	 * @param requestParam
	 * @return
	 */
	public  String savePatiCgInfo(List<Map<String, Object>> requestParam){
		ApplicationUtils aUtils = new ApplicationUtils();
		OtherRespJson respJson = new OtherRespJson();
		respJson.setData(JsonUtil.writeValueAsString(requestParam,"yyyy-MM-dd HH:mm:ss"));
		
		User user = new User();
		List<BlPubParamVo> voList = new ArrayList<BlPubParamVo>();
		for (Map<String, Object> map : requestParam) {
			String pkOrdexdt = LbSelfUtil.getPropValueStr(map, "PkOrdexdt");//获取记费主键，调用方传递，防止重复记费
			String codeOrg = LbSelfUtil.getPropValueStr(map, "CodeOrg");
            String euPvType = LbSelfUtil.getPropValueStr(map, "EuPvType");
            String pkPv = LbSelfUtil.getPropValueStr(map, "PkPv");
            String codePv = LbSelfUtil.getPropValueStr(map, "CodePv");
           
            if(StringUtils.isEmpty(pkPv)){
            	if(StringUtils.isNotEmpty(codePv)){
            		Map<String, Object> pvMap = DataBaseHelper.queryForMap("select pk_pv,eu_pvtype from pv_encounter where eu_pvtype= '3' and eu_status = '1' and code_pv = ?",codePv);
            		pkPv=LbSelfUtil.getPropValueStr(pvMap, "pkPv");
            		euPvType=LbSelfUtil.getPropValueStr(pvMap, "euPvtype");
            	}else{
            		respJson.setStatus(Constant.UNUSUAL);
        			respJson.setDesc("未获取到有效患者信息");
    				return respJson.toString();
            	}
            }
            
            if(StringUtils.isNotEmpty(pkOrdexdt)){
            	Map<String, Object> bdItemMaps = DataBaseHelper.queryForMap("select amount,pk_dept_ex,pk_cgip from bl_ip_dt where pk_ordexdt=? and pk_pv=?",new Object[]{pkOrdexdt,pkPv});
    			if(MapUtils.isNotEmpty(bdItemMaps)){
    				respJson.setStatus(Constant.UNUSUAL);
        			respJson.setDesc("已记费项目不能重复记费");
    				return respJson.toString();
    			}
            }else{
            	respJson.setStatus(Constant.UNUSUAL);
    			respJson.setDesc("未获取到记费主键");
				return respJson.toString();
            }
            
			
			String pkPi = LbSelfUtil.getPropValueStr(map, "PkPi");
			String pkOrd = LbSelfUtil.getPropValueStr(map, "PkOrd");
            String pkCnord = LbSelfUtil.getPropValueStr(map, "Pkcnord");//医嘱项目编码
            String codeItem = LbSelfUtil.getPropValueStr(map, "CodeItem");//收费项目编码
            if(StringUtils.isEmpty(pkOrd)){
            	if(StringUtils.isNotEmpty(codeItem)){
            		pkOrd=LbSelfUtil.getPropValueStr(DataBaseHelper.queryForMap("select pk_ord from bd_ord where code = ?",codeItem), "pkOrd");
            	}else if(StringUtils.isEmpty(pkOrd)){
            		respJson.setStatus(Constant.UNUSUAL);
        			respJson.setDesc("未获取到有效项目信息");
    				return respJson.toString();
            	}
            }
            
            String codeOrgEx = LbSelfUtil.getPropValueStr(map, "CodeOrgEx");	
            String codeOrgApp = LbSelfUtil.getPropValueStr(map, "CodeOrgApp");
			String codeDeptEx = LbSelfUtil.getPropValueStr(map, "CodeDeptEx");
			
			if(StringUtils.isEmpty(codeOrgApp)){
				codeOrgApp=codeOrg;
			}
			if(StringUtils.isEmpty(codeDeptEx)){
				codeDeptEx=codeOrg;
			}
			
			String codeDeptApp = LbSelfUtil.getPropValueStr(map, "CodeDeptApp");
			String codeEmpApp = LbSelfUtil.getPropValueStr(map, "CodeEmpApp");
			String flagPd = LbSelfUtil.getPropValueStr(map, "FlagPd");
			String flagPv = LbSelfUtil.getPropValueStr(map, "FlagPv");
			String dateHap = LbSelfUtil.getPropValueStr(map, "DateHap");
			String codeDeptCg = LbSelfUtil.getPropValueStr(map, "CodeDeptCg");
			String codeEmpCg = LbSelfUtil.getPropValueStr(map, "CodeEmpCg");
			String quanCg = LbSelfUtil.getPropValueStr(map, "QuanCg");
			String barCode = LbSelfUtil.getPropValueStr(map, "Barcode");
			if (StringUtils.isEmpty(codeOrg) || StringUtils.isEmpty(euPvType)
					|| StringUtils.isEmpty(pkPv)
					|| StringUtils.isEmpty(codeDeptEx)
					|| StringUtils.isEmpty(codeDeptApp)
					|| StringUtils.isEmpty(codeEmpApp)
					|| StringUtils.isEmpty(flagPd)
					|| StringUtils.isEmpty(flagPv)
					|| StringUtils.isEmpty(codeDeptCg)
					|| StringUtils.isEmpty(codeEmpCg)
					|| StringUtils.isEmpty(quanCg)) {
				respJson.setStatus(Constant.UNUSUAL);
    			respJson.setDesc("必传参数不全");
				return respJson.toString();
			}
			Map<String, Object> pkOrgMap = DataBaseHelper.queryForMap("select pk_org from bd_ou_org where code_org = ?",codeOrg);
			if(MapUtils.isEmpty(pkOrgMap)){
				respJson.setStatus(Constant.UNUSUAL);
    			respJson.setDesc("未查询到"+codeOrg+"相关机构信息");
				return respJson.toString();
			}
			String pkOrg = LbSelfUtil.getPropValueStr(pkOrgMap, "pkOrg");
			String pkPd = null;
			String pkItem = null;
			String pkUnit=null;
			BigDecimal price = BigDecimal.ZERO;
			
			if (CommonUtils.isEmptyString(pkPi))
				pkPi = LbSelfUtil.getPropValueStr(DataBaseHelper.queryForMap("select pk_pi from pv_encounter where pk_pv = ?",pkPv), "pkPi");
			if (flagPd.equals("1")) {
				Map<String, Object> bdPdMap = DataBaseHelper.queryForMap("select pk_unit_min as pk_unit,pk_pd,price,pack_size from bd_pd where flag_stop!='1' and code = ?", codeItem);
				if(MapUtils.isNotEmpty(bdPdMap)){
					pkPd = LbSelfUtil.getPropValueStr(bdPdMap,"pkPd");
					String bdPdprice = LbSelfUtil.getPropValueStr(bdPdMap,"price");//零售价格
					String packSize = LbSelfUtil.getPropValueStr(bdPdMap,"packSize");//包装量
					pkUnit=LbSelfUtil.getPropValueStr(bdPdMap,"pkUnit");
					BigDecimal bdPdPackSize = BigDecimal.ZERO;
					if(StringUtils.isNotEmpty(bdPdprice)){
						price=BigDecimal.valueOf(Double.valueOf(bdPdprice));
					}
					if(StringUtils.isNotEmpty(packSize)){
						bdPdPackSize=BigDecimal.valueOf(Double.valueOf(packSize));
					}
					//药品零售价除以单位包装量
					price=price.divide(bdPdPackSize);
				}else{
					respJson.setStatus(Constant.UNUSUAL);
	    			respJson.setDesc("未查询到该药品信息,或改药品信息已停用");
					return respJson.toString();
				}
			} else if (flagPd.equals("0")) {
				Map<String, Object> bdItemMap =DataBaseHelper.queryForMap("select pk_item from bd_item where flag_active='1' and code = ?",codeItem);
				if(MapUtils.isEmpty(bdItemMap)){
					respJson.setStatus(Constant.UNUSUAL);
	    			respJson.setDesc("未查询到该收费项目信息,或收费项目已停用");
					return respJson.toString();	
				}
				pkItem = LbSelfUtil.getPropValueStr(bdItemMap, "pkItem");
				
			} else {
				respJson.setStatus(Constant.UNUSUAL);
    			respJson.setDesc("flagPd不合法");
				return respJson.toString();
			}
			
			
			BlPubParamVo vo = new BlPubParamVo();
			vo.setEuBltype("9");//记费类型：9：其他记账
			vo.setPkOrdexdt(pkOrdexdt);
			vo.setPkOrg(pkOrg);
			vo.setEuPvType(euPvType);
			vo.setPkPv(pkPv);
			vo.setPkPi(pkPi);
			vo.setPkOrd(pkOrd);
			vo.setPkCnord(pkCnord);
			vo.setPkItem(pkItem);
			vo.setPkOrd(pkPd);
			//执行机构判断。无数据默认pk_org
            if(StringUtils.isEmpty(codeOrgEx)){
        	    vo.setPkOrgEx(pkOrg);
            }else{
        	    String pkOrgEx = LbSelfUtil.getPropValueStr(DataBaseHelper.queryForMap("select pk_org from bd_ou_org where code_org = ?",codeOrgEx), "pkOrg");
        	    vo.setPkOrgEx(pkOrgEx);
		   }
        
			String pkDeptEx = LbSelfUtil.getPropValueStr(DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ? and pk_org =?",codeDeptEx, pkOrg), "pkDept");
			if(StringUtils.isNotEmpty(pkDeptEx)){
				vo.setPkDeptEx(pkDeptEx);
			}else{
				respJson.setStatus(Constant.UNUSUAL);
    			respJson.setDesc("未查询到"+codeDeptEx+"相关执行科室信息");
				return respJson.toString();
			}
			Map<String, Object> bdOuDeptMap = DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ? and pk_org =?",codeDeptApp, pkOrg);
			if(MapUtils.isEmpty(bdOuDeptMap)){
				respJson.setStatus(Constant.UNUSUAL);
    			respJson.setDesc("未查询到"+codeDeptApp+"相关开立科室 信息");
				return respJson.toString();
			}
			String pkDeptApp = LbSelfUtil.getPropValueStr(bdOuDeptMap, "pkDept");
			vo.setPkDeptApp(pkDeptApp);
			vo.setPkOrgApp(pkOrg);
			//
			Map<String, Object> EmpAppMap = DataBaseHelper.queryForMap("select pk_emp,name_emp from bd_ou_employee where code_emp = ?",codeEmpApp);
			if(MapUtils.isEmpty(EmpAppMap)){
				respJson.setStatus(Constant.UNUSUAL);
    			respJson.setDesc("未查询到"+codeEmpApp+"相关开立医生信息");
				return respJson.toString();
			}
			vo.setPkEmpApp(LbSelfUtil.getPropValueStr(EmpAppMap, "pkEmp"));
			vo.setNameEmpApp(LbSelfUtil.getPropValueStr(EmpAppMap, "nameEmp"));
			vo.setFlagPd(flagPd);
			vo.setFlagPv(flagPv);
			if(StringUtils.isNotEmpty(dateHap)){
				try {
					vo.setDateHap(DateUtils.parseDate(dateHap));
				} catch (ParseException e) {
					respJson.setStatus(Constant.UNUSUAL);
	    			respJson.setDesc("dateHap格式错误");
					return respJson.toString();
				};
			}else{
				vo.setDateHap(new Date());
			}
			Map<String, Object> deptMap = DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ?",codeDeptCg);
			if(MapUtils.isNotEmpty(deptMap)){
				String pkDeptCg = LbSelfUtil.getPropValueStr(deptMap, "pkDept");
				vo.setPkDeptCg(pkDeptCg);
			}else{
				respJson.setStatus(Constant.UNUSUAL);
    			respJson.setDesc("未查询到"+codeDeptCg+"相关记费科室信息");
				return respJson.toString();
			}
			
			Map<String, Object> empMap = DataBaseHelper.queryForMap("select pk_emp,name_emp from bd_ou_employee where code_emp = ?",codeEmpCg);
			if(MapUtils.isEmpty(empMap)){
				respJson.setStatus(Constant.UNUSUAL);
    			respJson.setDesc("未查询到"+codeEmpCg+"相关人员信息");
				return respJson.toString();
			}
			vo.setPkEmpCg(LbSelfUtil.getPropValueStr(empMap, "pkEmp"));
			vo.setNameEmpCg(LbSelfUtil.getPropValueStr(empMap, "nameEmp"));
			vo.setQuanCg(Double.parseDouble(quanCg));
			vo.setBarcode(barCode);
			vo.setPrice(price.doubleValue());
			//收费项目为药品时批次号
			if (flagPd.equals("1")) {
				vo.setBatchNo("~");//批次号
				vo.setPriceCost(price.doubleValue());//药品成本单价
				vo.setPkUnitPd(pkUnit);//零售单位
				vo.setPackSize(Integer.valueOf("1"));//药品包装量
			}
			voList.add(vo);
			user.setPkOrg(pkOrg);
			user.setCodeOrg(codeOrg);
		}
		UserContext.setUser(user);
		ResponseJson ret = aUtils.execService("bl", "IpCgPubService", "savePatiCgInfo",voList, user);
		if(0 == ret.getStatus()){
			respJson.setStatus(Constant.RESFAIL);
			respJson.setDesc("成功");
		}else{
			respJson.setStatus(Constant.UNUSUAL);
			respJson.setDesc(ret.getDesc());
		}
		System.out.println(respJson.toString());
    	return respJson.toString();
	}
	/**
     * 口腔医院自助机支付数据保存
     * @param requ
     * @return
     * @throws ParseException 
     */
    public void  pskqPayment(BlDeposit vo,LbSHRequestVo requ ,Map<String, Object> pvMap,User user,String pkDepopi) throws ParseException{
    	BlExtPay extPay = new BlExtPay();        
        	// 写外部交易接口支付bl_ext_pay
        	extPay.setPkExtpay(NHISUUID.getKeyId());
        	extPay.setPkOrg(user.getPkOrg());
        	//extPay.setAmount(BigDecimal.valueOf(CommonUtils.getDouble(MsgUtils.getPropValueStr(pvMap,"payAmt"))));//金额
        	extPay.setAmount(BigDecimal.valueOf(Double.valueOf(requ.getPayAmt())));
        	extPay.setDtBank("");
        	extPay.setNameBank(""); 
        	extPay.setEuPaytype(requ.getPayType());//支付方式7：微信，8：支付宝
        	extPay.setFlagPay("1");
        	extPay.setSysname("");
        	extPay.setSerialNo(requ.getQrCodeInfoVo().get(0).getOrderno());//订单号
        	extPay.setTradeNo(requ.getQrCodeInfoVo().get(0).getFlowno());//交易流水号
        	extPay.setPkPi(LbSelfUtil.getPropValueStr(pvMap,"pkPi"));
        	extPay.setPkPv(LbSelfUtil.getPropValueStr(pvMap,"pkPv"));
        	extPay.setResultPay("");
        	extPay.setRefundNo("");
        	extPay.setDateRefund(null);
        	extPay.setEuBill("0");
        	extPay.setPkBill("");
        	extPay.setDateBill(null);
        	extPay.setDescPay("自助机金额缴纳"+requ.getQrCodeInfoVo().get(0).getPaymethod());
        	extPay.setDatePay(new Date());
        	if(null != vo){
        		extPay.setPkDepo(vo.getPkDepo());//BL_DEPOSIT主键
        	}
        	if(StringUtils.isNotBlank(pkDepopi)){
        		extPay.setPkDepopi(pkDepopi);//BL_DEPOSIT_PI主键-发卡时使用
        	}
        	extPay.setCreator(user.getPkEmp());
        	extPay.setCreateTime(new Date());
        	extPay.setDelFlag("0");
        	extPay.setTs(new Date());
        	extPay.setModifier(LbSelfUtil.getPropValueStr(pvMap,"pkEmp"));
        	DataBaseHelper.insertBean(extPay);
    	extPay = null;
    }
}
