package com.zebone.nhis.webservice.pskq.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybSt;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybVisit;
import org.apache.commons.collections.MapUtils;

import org.apache.commons.lang3.StringUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybSt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStCity;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStCitydt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybVisit;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybVisitCity;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiLock;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pv.pub.service.TicketPubService;
import com.zebone.nhis.pv.reg.service.RegSyxService;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.nhis.sch.pub.service.SchExtPubService;
import com.zebone.nhis.webservice.dao.BdPubForWsMapper;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.ReserveOutpatient;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.service.PskqInitService;
import com.zebone.nhis.webservice.pskq.service.ReserveOutpatientService;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.webservice.pskq.utils.PskqMesUtils;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.vo.dataUp.RespItemsVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

/**
 * 门诊预约服务
 */
public class ReserveOutpatientServiceImpl implements ReserveOutpatientService {

    public static ReserveOutpatientService newInstance() {
        return new ReserveOutpatientServiceImpl();
    }
    

    /**
     * 预约挂号
     * @param requestBody
     * @param listener
     */

    @Override
    public void save(RequestBody requestBody, ResultListener listener) {
    	
        try {
            DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
            transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
            DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
            TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);

            PiMasterRegVo regvo = new PiMasterRegVo();
            Map<String, Object> message = requestBody.getMessage();
            if (!message.containsKey("RESERVE_OUTPATIENT") && message.get("RESERVE_OUTPATIENT") == null) {
                listener.error("未获取到挂号信息");
                return;
            }
            //发送方工号登录
            User user = PskqMesUtils.getUserExt(requestBody.getSender().getId());
    		if(user == null) {
                listener.error("发送方：【"+requestBody.getSender().getSoftwareName().getName()+ "】；未在his注册工号，请先联系his注册工号！");
                return;
    		}
    		UserContext.setUser(user);
            List<DataElement> dataElement = (List<DataElement>) message.get("RESERVE_OUTPATIENT");
            ReserveOutpatient reserveOutpatient = (ReserveOutpatient) MessageFactory.deserialization(dataElement, new ReserveOutpatient());
            
            String reserveDateTime = reserveOutpatient.getReserveDateTime();
            if(CommonUtils.isEmptyString(reserveDateTime)) {
            	listener.error("发送方：预约日期不能为空！");
            	return;
            }
            //自助机当天挂号走锁号策略
            String regDate = DateUtils.dateToStr("yyyyMMdd",DateUtils.parseDate(reserveDateTime,"yyyy-MM-dd HH:mm:ss"));
            String type = "1";
            String dateToStr = DateUtils.dateToStr("yyyyMMdd",new Date());
            if(dateToStr.equals(regDate) && "zzj".equals(user.getCodeEmp())) {
            	type = "0";
            } 
            String codePi = reserveOutpatient.getPkPatient().split("_")[2];
            //查询患者对象，赋值给regvo
            PiMaster piMaster = DataBaseHelper.queryForBean("select * from PI_MASTER where CODE_PI = ? AND DEL_FLAG='0' ",
                    PiMaster.class, codePi);
            if (piMaster == null) {
                listener.error("HIS中没有CODE_PI为" + codePi + "的患者信息,请检查传输患者主键3号位置");
                return;
            } 
            List<PiLock> listLock=DataBaseHelper.queryForList("select * from pi_lock where pk_pi=?", PiLock.class,piMaster.getPkPi());
            if(null!=listLock && listLock.size()>0){
            	if("3".equals(listLock.get(0).getEuLocktype())){
        			String pkPv=DataBaseHelper.queryForScalar("SELECT pk_Pv pkPv from (SELECT PK_PV from BL_OP_DT WHERE FLAG_SETTLE='0' and DEL_FLAG='0' and PK_PI=?  GROUP BY PK_PV) a WHERE ROWNUM=1", String.class, piMaster.getPkPi());
        			if(!CommonUtils.isEmptyString(pkPv)){
        				String amount=DataBaseHelper.queryForScalar("SELECT SUM(AMOUNT) amount from BL_OP_DT where PK_PV=? and FLAG_SETTLE='0' and DEL_FLAG='0'", String.class, pkPv);
        				String datestr=DataBaseHelper.queryForScalar("SELECT TO_CHAR(DATE_CLINIC,'yyyy-mm-dd') datestr from PV_ENCOUNTER WHERE PK_PV=?", String.class, pkPv);
        				listener.error("您于"+datestr+"就诊的费用尚未结清，欠款"+amount+"元，请您先到医院窗口缴清相关费用后再行挂号就诊。");
        			}
            	}else{
            		listener.error("您已经爽约两次，请到窗口挂号就诊！");
            	}
                return;
            }
            ApplicationUtils.copyProperties(regvo, piMaster);
            regvo.setTs(new Date());
            //排班表主键
            String pkSch = reserveOutpatient.getScheduleId();
            SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
                    SchSch.class, pkSch);
            //1.判断是否还有可约号
            if (schSch == null || "1".equals(schSch.getFlagStop())) {
                listener.error("您所选排班不存在或已停用");
                return;
            }

            if (schSch.getCntTotal().intValue() <= schSch.getCntUsed().intValue()) {
                listener.error("您所选排班不存在或已停用");
                return;
            }
            //2.判断是否重复挂号
            List<Map<String, Object>> schAppList =DataBaseHelper.queryForList("SELECT * FROM sch_appt WHERE eu_status < '9' and flag_cancel ='0' AND pk_sch=? and pk_pi=?", new Object[]{schSch.getPkSch(),piMaster.getPkPi()});
			if(schAppList.size()>0){
				listener.error("您已经在当前日期当前科室登记，请勿重复登记！");
				return;
			}
			
            regvo.setPkSch(pkSch);//排班主键
			regvo.setPkDateslotsec(reserveOutpatient.getReserveSourceId());//时段主键
            regvo.setEuSchclass("0");//排班类型
			regvo.setPkSchplan(schSch.getPkSchplan());
			regvo.setPkSchsrv(schSch.getPkSchsrv());
			SchSrv schSrv = DataBaseHelper.queryForBean("select * from sch_srv where eu_schclass = '0' and pk_schsrv = ?",
					SchSrv.class, schSch.getPkSchsrv());
			if(schSrv != null && "9".equals(schSrv.getEuSrvtype())){
			   regvo.setEuPvtype("2");//就诊类型
			}else{
			   regvo.setEuPvtype("1");//就诊类型
			}
			regvo.setPkSchres(schSch.getPkSchres());
			regvo.setPkDateslot(schSch.getPkDateslot());
			regvo.setDateAppt(DateUtils.parseDate(DateUtils.dateToStr("YYYY-MM-DD", schSch.getDateWork()), "YYYY-MM-DD"));//预约日期
			//预约渠道：2500-自助机；  2510-微信 ; 2520-健康160 ；维护系统字典：020100
			regvo.setOrderSource(reserveOutpatient.getReserveCahannelCode());
            //修改原判断预约号源逻辑，查询-后期待优化
			List<SchTicket> ticketlist =new ArrayList<SchTicket>();
			String sql="select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0' and flag_stop='0' and FLAG_USED = '0' ";
			if("0".equals(type)){
				ticketlist = DataBaseHelper.queryForList(sql,SchTicket.class, pkSch);
				type="1";
			}else{
				sql=sql+" and FLAG_APPT = ?";
				ticketlist = DataBaseHelper.queryForList(sql,SchTicket.class, pkSch,type);
			}
            
            if (ticketlist.size() <= 0) {
                listener.error("您所选排班已无可预约号");
                return;
            }
            SchTicket ticket = new SchTicket();
            boolean haveTicketNo = DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where pk_sch = ?",
                    Integer.class, new Object[]{schSch.getPkSch()}) > 0;
            //2.占用号源
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            TicketPubService ticketPubService = applicationContext.getBean("ticketPubService", TicketPubService.class);
            RegSyxService regSyxService = applicationContext.getBean("regSyxService", RegSyxService.class);
            if (haveTicketNo) {
                // 处理号表
                ticket = ticketPubService.getUnusedAppTicketExt(pkSch, regvo.getPkDateslotsec(),type);
            } else {
                ticket = ticketPubService.getUnusedAppTicketFromSchExt(pkSch,type);
            }
            //3.保存预约登记信息（含保存患者信息）
            try {
                regvo = regSyxService.saveApptSchRegInfo(regvo, schSch, ticket, haveTicketNo, user, false);
                reserveOutpatient.setReserveOutpatientId(regvo.getApptCode());
                reserveOutpatient.setOutpatientNo(piMaster.getCodeOp());
                List<DataElement> dataElementList = MessageBodyUtil.dataElementsReturnFactory(reserveOutpatient);
                dataSourceTransactionManager.commit(transStatus);
                listener.success(dataElementList);

                return;
            } catch (Exception e) {
                //还原占用的预约号
                if (haveTicketNo) {
                    ticketPubService.setTicketUnused(ticket);
                } else {
                    ticketPubService.setTicketUnusedFromSch(ticket);
                }
                dataSourceTransactionManager.rollback(transStatus);
                listener.error(e.getMessage());
                return;
            }

        } catch (Exception exception) {
            listener.exception(exception.getMessage());
            return;
        }

    }

    @Override
    public void edit(RequestBody requestBody, ResultListener listener) {

        try {
        	//工号登录
        	User user = PskqMesUtils.getUserExt(requestBody.getSender().getId());
    		if(user == null) {
    			listener.error("发送方：【"+requestBody.getSender().getSoftwareName().getName()+ "】；未在his注册工号，请先联系his注册工号！");
    			return;
    		}
    		UserContext.setUser(user);
            List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("RESERVE_OUTPATIENT");
            List<Map<String,Object>> ybcsList = (List<Map<String,Object>>) requestBody.getMessage().get("ybcs");
            //预约信息
            Map<String,Object> ybcs = new HashMap<String,Object>();
            if(ybcsList != null && ybcsList.size() > 0) {
            	ybcs = ybcsList.get(0);
            }
            ReserveOutpatient reserveOutpatient = (ReserveOutpatient) MessageFactory.deserialization(list, new ReserveOutpatient());
            String type = reserveOutpatient.getReserveState()+reserveOutpatient.getPayState();         
            reserveOutpatient = StrategyFactory.creator(type).doOperate(reserveOutpatient,ybcs);
            List<DataElement> dataElementList = MessageBodyUtil.dataElementsReturnFactory(reserveOutpatient);
            listener.success(dataElementList);
            return;
        } catch (Exception exception) {
            listener.exception(exception.getMessage());
            return;
        }

    }

    public interface Strategy {

        /**
         * 此接口方法根据具体业务传递定义
         */
        ReserveOutpatient doOperate(ReserveOutpatient reserveOutpatient,Map<String,Object> ybcs) throws Exception;
    }

    /**
     * 未缴费退号策略
     */
    public static class CancelStrategy implements Strategy {


        @Override
        public ReserveOutpatient doOperate(ReserveOutpatient reserveOutpatient,Map<String,Object> ybcs) throws Exception {
            //1、验证预约登记编码
        	if(CommonUtils.isEmptyString(CommonUtils.getString(reserveOutpatient.getReserveOutpatientId()))) {
        		 throw new BusException("预约单号不能为空");
        	}
            //2、使用订单编码查询SchAppt信息
            SchAppt schAppt = DataBaseHelper.queryForBean("select * from sch_appt where del_flag = '0' and code = ?",
                    SchAppt.class, CommonUtils.getString(reserveOutpatient.getReserveOutpatientId()));
            //3、验证schAppt
            if(schAppt == null) {
            	 throw new BusException("未找到预约登记信息，请检查预约单号是否正确：" + reserveOutpatient.getReserveOutpatientId());
            }
            if("9".equals(schAppt.getEuStatus())) {
            	throw new BusException("已取消预约登记，请勿重复发起撤销，预约单号：" + reserveOutpatient.getReserveOutpatientId());
            }
            if("1".equals(schAppt.getEuStatus())) {
            	throw new BusException("撤销登记失败，订单已挂号，请做退号业务，预约单号：" + reserveOutpatient.getReserveOutpatientId());
            }
            //4、取消预约
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            SchExtPubService schExtPubService = applicationContext.getBean("schExtPubService", SchExtPubService.class);
            //5、获取pkSchappt
            String pkSchappt = schAppt.getPkSchappt();
            Map<String,Object> map = new HashMap<>(16);
            map.put("pkSchappt",pkSchappt);
            Map<String,Object> result = schExtPubService.cancelApplyRegister(JSON.toJSONString(map),UserContext.getUser());
            if(MapUtils.isEmpty(result)){
                throw new BusException("取消预约失败");
            }
            if(!result.containsKey("result")){
                throw new BusException("取消预约失败");
            }

            if(!"true".equals(result.get("result"))){
                throw new BusException("取消预约失败");
            }
            return reserveOutpatient;
        }
    }

    /**
     * 取消已缴费预约
     */
    public static class CancelPayStrategy implements Strategy {


        @Override
        public ReserveOutpatient doOperate(ReserveOutpatient reserveOutpatient,Map<String,Object> ybcs) throws Exception {
            //1、验证预约登记编码
        	if(CommonUtils.isEmptyString(CommonUtils.getString(reserveOutpatient.getReserveOutpatientId()))) {
        		 throw new BusException("预约单号不能为空");
        	}
            //2、使用订单编码查询SchAppt信息
            SchAppt schAppt = DataBaseHelper.queryForBean("select * from sch_appt where del_flag = '0' and code = ?",
                    SchAppt.class, CommonUtils.getString(reserveOutpatient.getReserveOutpatientId()));
            //3、验证schAppt
            if(schAppt == null) {
            	 throw new BusException("未找到预约挂号信息，请检查预约单号是否正确：" + reserveOutpatient.getReserveOutpatientId());
            }
            if("9".equals(schAppt.getEuStatus())) {
            	throw new BusException("已退号，请勿重复发起退号，预约单号：" + reserveOutpatient.getReserveOutpatientId());
            }
            if("0".equals(schAppt.getEuStatus())) {
            	throw new BusException("退号失败，该订单未挂号缴费，预约单号：" + reserveOutpatient.getReserveOutpatientId());
            }
            //4.1获取排班主键ID
            String pkSch = reserveOutpatient.getScheduleId();
            //4.2获取患者codePi
            String codePi = reserveOutpatient.getPkPatient().split("_")[2];
            //4.3查询挂号对应服务类型、效验是否是急诊类型
            Map<String, Object> euSrvtypeMap = DataBaseHelper.queryForMap("select srv.eu_srvtype from sch_srv srv LEFT JOIN  sch_sch sch ON sch.PK_SCHSRV = srv.PK_SCHSRV where sch.pk_sch=?", new Object[]{pkSch});
            if(MapUtils.isEmpty(euSrvtypeMap)){
                throw new BusException("调用退号接口失败！失败原因：根据排班主键："+pkSch+"未查询到相关排班信息");
            }
            StringBuffer sql = new StringBuffer("select pv.*,pi.id_no,pi.CODE_OP,pi.DT_IDTYPE,pvOpEr.*,st.code_inv ");
            sql.append(" from pv_encounter pv inner join pi_master pi on pi.pk_pi = pv.pk_pi ");
            //4.4根据查询到号源服务类型判断是否关联急诊表查询相关信息
            if("9".equals(euSrvtypeMap.get("euSrvtype"))){
                sql.append(" left join pv_er pvOpEr on pv.pk_pv=pvOpEr.pk_pv ");
            }else{
                sql.append(" left join pv_op pvOpEr on pv.pk_pv=pvOpEr.pk_pv ");
            }
            sql.append(" left join ( select st.pk_pv,inv.code_inv,inv.flag_cancel from bl_settle st ");
            sql.append(" inner join bl_st_inv stinv on stinv.pk_settle = st.pk_settle ");
            sql.append(" inner join bl_invoice inv on inv.pk_invoice = stinv.pk_invoice ");
            sql.append(" where st.dt_sttype='00' and st.FLAG_CANC!='1' and inv.flag_cancel='0' ");
            sql.append(" ) st on st.pk_pv = pv.pk_pv and st.flag_cancel='0' ");
            sql.append(" where pv.eu_pvtype in('1','2','4') and nvl(pv.flag_cancel,'0')='0' ");
            sql.append(" and pi.code_pi = ? and pvOpEr.pk_sch= ? ");
            
            //4.5查询相关数据并创建his业务退号接口调用入参
            PiMasterRegVo piMasterRegVo = DataBaseHelper.queryForBean(sql.toString(), PiMasterRegVo.class, new Object[]{codePi,pkSch});
            if(!BeanUtils.isNotNull(piMasterRegVo)){
                throw new BusException("调用退号接口失败！失败原因：根据患者信息："+codePi+"和排班主键："+pkSch+"未查询到相关信息");
            }
            //5.退号操作
            piMasterRegVo.setPkAppt(schAppt.getPkSchappt());
            User user = UserContext.getUser();
            if(user != null && !CommonUtils.isEmptyString(user.getCodeEmp())) {
            	//红冲电子票使用此参数
            	piMasterRegVo.setNameMachine(user.getCodeEmp().toUpperCase());
            }
           
            ApplicationUtils apputil = new ApplicationUtils();
            ResponseJson  NowSjh =  apputil.execService("PV", "RefundSyxService", "cancelReg",piMasterRegVo,UserContext.getUser());
            if(NowSjh.getStatus() != Constant.SUC){
                throw new BusException("调用退号接口失败！失败原因："+NowSjh.getDesc());
            }
            //6.响应---待确认
            return reserveOutpatient;
        }
    }

    /**
     * 挂号缴费策略
     * 预约挂号缴费后，HIS返回中包含导诊信息。并且HIS发出挂号消息到平台，平台转发到其他订阅系统
     */
    public static class PayStrategy implements Strategy{

        @Override
        public ReserveOutpatient doOperate(ReserveOutpatient reserveOutpatient,Map<String,Object> ybcs) throws Exception {
            if (null == reserveOutpatient) {
                throw new BusException("未查询到预约信息");
            }

            if (CommonUtils.isEmptyString(reserveOutpatient.getHisOrderNo())) {
                throw new BusException("预约单号不能为空");
            }
            if (CommonUtils.isEmptyString(reserveOutpatient.getPayState())) {
                throw new BusException("支付状态不能为空");
            }
            if(!reserveOutpatient.getPayState().equals("1")){
            	throw new BusException("支付状态不正确");
            }
            if(CommonUtils.isEmptyString(reserveOutpatient.getPayMethodCode())){
            	throw new BusException("支付方式代码不能为空");
            }

            if(CommonUtils.isEmptyString(reserveOutpatient.getTotalPayment())){
                throw new BusException("支付总费用不能为空");
            }
            //医保支付金额
            if(CommonUtils.isEmptyString(reserveOutpatient.getMedicalInsuranceExpenses())){
            	reserveOutpatient.setMedicalInsuranceExpenses("0");
            }
            //自费金额
            if(CommonUtils.isEmptyString(reserveOutpatient.getPersonalExpenses())){
            	reserveOutpatient.setPersonalExpenses("0");
            }

            if(CommonUtils.isEmptyString(reserveOutpatient.getPaymentOrderNo())){
                throw new BusException("预约支付订单号不能为空");
            }
            
            //是否医保
            if(CommonUtils.isEmptyString(reserveOutpatient.getIsInsurance())){
                reserveOutpatient.setIsInsurance("0");
            }
            String isInsurance = reserveOutpatient.getIsInsurance();
            
            //医保参数验证
            if("1".equals(isInsurance)) {
            	if(MapUtils.isEmpty(ybcs)) {
            		throw new BusException("医保患者挂号，医保参数体不能为空！");
            	}
            	if(CommonUtils.isEmptyString(CommonUtils.getPropValueStr(ybcs, "serialNumber"))) {
            		throw new BusException("交易流水号:serialNumber，不能为空！");
            	}
            }
            
            //源系统代码
            String reserveCahannelCode = reserveOutpatient.getReserveCahannelCode();
			if ("".equals(reserveCahannelCode) || reserveCahannelCode == null) {
                throw new BusException("预约渠道代码不能为空");
            }

            SchAppt apptMap = DataBaseHelper.queryForBean("select * from sch_appt where code = ? ",SchAppt.class, reserveOutpatient.getReserveOutpatientId());
            if(apptMap == null||StringUtils.isEmpty(apptMap.getPkSchappt())){
            	throw new BusException("未找到您的登记信息，请确认预约单号是否正确，预约订单号：" + reserveOutpatient.getReserveOutpatientId());
            }
            if("1".equals(apptMap.getEuStatus())){
            	throw new BusException("您已成功支付，请勿重复支付，预约订单号：" + reserveOutpatient.getReserveOutpatientId());
            }
            if("9".equals(apptMap.getEuStatus())){
            	throw new BusException("此订单已被取消，请重新预约登记，预约订单号：" + reserveOutpatient.getReserveOutpatientId());
            }

            PiMaster pimaster = DataBaseHelper.queryForBean("select * from pi_master where pk_pi = ? ",PiMaster.class, apptMap.getPkPi());
            if(null==pimaster){
            	throw new BusException("未获取到患者信息");
            }        
    
            User user = UserContext.getUser();
            //修改为手动事物 , 关闭事务自动提交
            DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
            transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
            DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
            TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
            try{
	            SchSrv schSrv = DataBaseHelper.queryForBean("select * from SCH_SRV where PK_SCHSRV = ? ",SchSrv.class, apptMap.getPkSchsrv());
	            Map<String,Object> regvo=new HashMap<String, Object>();
	            regvo.put("pkAppt", apptMap.getPkSchappt());
	            regvo.put("ticketNo", apptMap.getTicketNo());
	            regvo.put("pkSchres", apptMap.getPkSchres());
	            regvo.put("pkDept", apptMap.getPkDeptEx());
	            regvo.put("euPvtype", (schSrv.getEuSrvtype().equals("9"))?"2":"1");
	            regvo.put("pkPi", pimaster.getPkPi());
	            regvo.put("idNo", pimaster.getIdNo());
	            regvo.put("dtIdtype", pimaster.getDtIdtype());
	            regvo.put("namePi", pimaster.getNamePi());
	            regvo.put("dtSex", pimaster.getDtSex());
	            regvo.put("birthDate", pimaster.getBirthDate());
	            regvo.put("address", pimaster.getAddress());
	            regvo.put("dtMarry", pimaster.getDtMarry());
	            regvo.put("pkOrg", pimaster.getPkOrg());                 
	            //医保/自费
				Map<String, Object> bdhpMap = new HashMap<String, Object>();
				if("1".equals(isInsurance)){
					//通过bcc334 缴费人员类别 获取医保计划
					bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp,name FROM bd_hp WHERE code = ? and del_flag='0' ", CommonUtils.getPropValueStr(ybcs,"bcc334"));
					if(bdhpMap == null){
						throw new BusException("未查询到该【bcc334】:【"+CommonUtils.getPropValueStr(ybcs,"bcc334")+"】医保编号所对应的医保主建信息");
					}
					//第三方医保支付金额
					regvo.put("amtInsuThird", new BigDecimal(CommonUtils.isEmptyString(reserveOutpatient.getMedicalInsuranceExpenses())?0:Double.valueOf(reserveOutpatient.getMedicalInsuranceExpenses())));
				}else{
					//获取自费医保类型
					bdhpMap = DataBaseHelper.queryForMap("SELECT pk_hp,name FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
				}
				regvo.put("pkHp", bdhpMap.get("pkHp"));
				
				regvo.put("addrOrigin",pimaster.getAddrOrigin());
	            regvo.put("pkPicate",pimaster.getPkPicate());
	            regvo.put("dateReg",apptMap.getBeginTime());
	            regvo.put("pkDateslot",apptMap.getPkDateslot());
	            regvo.put("dtSource",pimaster.getDtSource());
	            regvo.put("nameRel",pimaster.getNameRel());
	            regvo.put("idnoRel",pimaster.getIdnoRel());
	            regvo.put("telRel",pimaster.getTelRel());
	            regvo.put("pkSrv",apptMap.getPkSchsrv());
	            regvo.put("pkSchsrv",apptMap.getPkSchsrv());
	            regvo.put("mcno",pimaster.getMcno());
	            regvo.put("dtSpecunit",pimaster.getDtSpecunit());
	            regvo.put("pkSch",apptMap.getPkSch());
	            regvo.put("dtApptype",apptMap.getDtApptype());
	            regvo.put("euSrvtype",schSrv.getEuSrvtype());
	            regvo.put("agePv",ApplicationUtils.getAgeFormat(pimaster.getBirthDate(),null));
	            //获取收费项目
	            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
	            Map<String, Object> insurParam = new HashMap<>();
	    		insurParam.put("dateBirth", DateUtils.dateToStr("YYYY-MM-DD", pimaster.getBirthDate()));
	    		insurParam.put("flagSpec", (schSrv.getEuSrvtype().equals("2"))?"1":"0");
	    		insurParam.put("pkPicate", pimaster.getPkPicate());
	    		insurParam.put("pkSchsrv", schSrv.getPkSchsrv());
	    		insurParam.put("pkInsu", bdhpMap.get("pkHp"));
	    		insurParam.put("nameInsu",bdhpMap.get("name"));
	    		insurParam.put("euPvType",(schSrv.getEuSrvtype().equals("9"))?"2":"1");
	    		insurParam.put("pkEmp",pimaster.getPkPi());
	    		String param = JsonUtil.writeValueAsString(insurParam);
	            RegSyxService regSyxService = applicationContext.getBean("regSyxService", RegSyxService.class);   
	            List<ItemPriceVo> listItem = regSyxService.getItemBySrv(param, user);
	            
	            regvo.put("itemList", listItem);
	            BigDecimal amt = BigDecimal.ZERO;
				BlDeposit dep =new BlDeposit();
				//Double pric = Double.parseDouble(reserveOutpatient.getTotalPayment());
				Double pric = Double.parseDouble(reserveOutpatient.getPersonalExpenses());
				amt=BigDecimal.valueOf(pric);
				String payType="";
				switch (reserveOutpatient.getPayMethodCode()) {
				    //院内预交金余额支付
				case "0":
					payType="4";break;
					//银行支付
				case "1":
					payType="3";break;
					//微信支付
				case "2":
					payType="15";break;
					//支付宝支付
				case "3":
					payType="16";break;
					//医保支付
				case "4":
					payType="9";break;
					//其他第三方支付
				default:
					payType="99";break;
				}
				reserveOutpatient.setPayMethodCode(payType);
				if(!("1").equals(payType) && !("4").equals(payType)){
					 dep.setPayInfo(reserveOutpatient.getTransactionSerialNo());
				}
				regvo.put("invStatus", "-2");
				dep.setAmount(amt);
				dep.setDtPaymode(payType);//支付方式
				List<BlDeposit> depList =new ArrayList<>();
				depList.add(dep);
				regvo.put("depositList",depList);
				
				String paramReg=JsonUtil.writeValueAsString(regvo);     
				//调用预约确认
				PiMasterRegVo piMasterRegVo=regSyxService.confirmApptRegInfo(paramReg, user);
				
				 
    		    //生成医保记录  ins_szyb_st  ins_szyb_st_city  ins_szyb_st_citydt  ins_szyb_visit  ins_szyb_visit_city
    			if("1".equals(isInsurance)){
    			    //之前保存深圳医保数据不要
    				saveInsSzybInfo(piMasterRegVo,ybcs,user);
    			}
    		    
				//保存第三方支付表
				if(!StringUtils.isEmpty(dep.getPayInfo())){
					BlExtPay extPay = new BlExtPay();        
		        	// 写外部交易接口支付bl_ext_pay
		        	extPay.setPkOrg(user.getPkOrg());
		        	extPay.setAmount(amt);
		        	extPay.setEuPaytype(payType);//支付方式7：微信，8：支付宝
		        	extPay.setFlagPay("1");
		        	extPay.setSerialNo(reserveOutpatient.getTransactionSerialNo());//订单号
		        	extPay.setTradeNo(reserveOutpatient.getPaymentOrderNo());//交易流水号
		        	extPay.setPkPi(apptMap.getPkPi());
		        	extPay.setPkPv(piMasterRegVo.getPkPv());
		        	extPay.setSysname(reserveCahannelCode);//预约渠道代码，退号用
		        	extPay.setRefundNo("");
		        	extPay.setDateRefund(null);
		        	extPay.setEuBill("0");
		        	extPay.setPkBill("");
		        	extPay.setDateBill(null);
		        	extPay.setDatePay(new Date());
		        	if(null != piMasterRegVo.getDepositList()){
		        		extPay.setPkDepo(piMasterRegVo.getDepositList().get(0).getPkDepo());//BL_DEPOSIT主键
		        	}
		        	extPay.setPkSettle(piMasterRegVo.getPkSettle());//bl_settle主键
		        	extPay.setCreator(user.getPkEmp());
		        	extPay.setCreateTime(new Date());
		        	extPay.setDelFlag("0");
		        	extPay.setTs(new Date());
		        	extPay.setModifier(user.getPkEmp());
		        	DataBaseHelper.insertBean(extPay);    
				}

				//调用电子票据服务 --- 博思未给工号对照，暂时注释
    		    if("1".equals(ApplicationUtils.getSysparam("BL0002", false))){//门诊挂号使用发票
    		    	//获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
    		    	String sql = "select argu.arguval from bd_res_pc pc "
    						+" left join bd_res_pc_argu argu on pc.PK_PC = argu.pk_pc and argu.flag_stop = '0' and argu.DEL_FLAG = '0'"
    						+" where pc.flag_active = '1' and pc.del_flag = '0' and pc.eu_addrtype = '0' and argu.code_argu = 'BL0031' and pc.addr = ?";
    			
    				Map<String,Object> qryMap = DataBaseHelper.queryForMap(sql, user.getCodeEmp().toUpperCase());
    				
    				String eBillFlag = null;
    				
    				if(qryMap!=null && qryMap.size()>0){
    					eBillFlag = CommonUtils.getString(qryMap.get("arguval"));
    				}
    					
    				if(CommonUtils.isEmptyString(eBillFlag)){
    					eBillFlag = "0";
    				}
    				if("1".equals(eBillFlag)){
						//调用开立票据接口生成票据信息
						Map<String,Object> paramMap = new HashMap<>(16);
						paramMap.put("pkPv", piMasterRegVo.getPkPv());
						paramMap.put("pkSettle", piMasterRegVo.getPkSettle());
						paramMap.put("flagPrint", "0");//不打印纸质票据
						//调用开立票据接口生成票据信息
						Map<String, Object> invMap = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillRegistration", new Object[]{paramMap,user});	
						List<BlInvoice> blInvoiceList =  (List<BlInvoice>)invMap.get("invs");
						if(blInvoiceList != null && blInvoiceList.size() > 0) {
                    		reserveOutpatient.setRegisteredInvoiceId(blInvoiceList.get(0).getPkInvoice());
                    		reserveOutpatient.setRegisteredInvoicePdfUrl(blInvoiceList.get(0).getUrlEbill());
                    	}
    				}
    		    }  
    		   
				dataSourceTransactionManager.commit(transStatus); // 添加失败 回滚事务；
				
				//发送消息到平台
				Map<String,Object> msgParam =  new HashMap<String,Object>();
				msgParam.put("pkEmp", user.getPkEmp());
				msgParam.put("nameEmp", user.getNameEmp());
				msgParam.put("codeEmp", user.getCodeEmp());
				msgParam.put("pkPv", piMasterRegVo.getPkPv());
				msgParam.put("isAdd", "0");
				msgParam.put("timePosition", "0");//0：预约     1：当天
				msgParam.put("addrPosition", "0");//0：现场 1：自助机 2：电话 3：微信  4：支付宝
				PlatFormSendUtils.sendPvOpRegMsg(msgParam);
				
            }catch (Exception e){
                dataSourceTransactionManager.rollback(transStatus);
                throw new BusException(e.getMessage());
            }
            return reserveOutpatient;
        }
    }
   
    /**
     * 取号策略
     */
    public static class TakeNoStrategy implements Strategy{

        @Override
        public ReserveOutpatient doOperate(ReserveOutpatient reserveOutpatient,Map<String,Object> ybcs) throws Exception {

            return reserveOutpatient;
        }
    }

    public static class StrategyFactory {

        //已预约
        public static final String RESERVE_STATE_DONE = "1";
        //取消预约-未支付
        public static final String RESERVE_STATE_CANCEL = "20";
        //取消预约-已支付
        public static final String RESERVE_STATE_CANCEL_PAY = "22";
        //已取号
        public static final String RESERVE_STATE_TAKE = "3";

        //支付
        public static final String PAY_STATE = "11";

        private static Map<String, Strategy> strategyMap = new HashMap<>();

        static {
            strategyMap.put(RESERVE_STATE_CANCEL, new CancelStrategy());
            strategyMap.put(RESERVE_STATE_CANCEL_PAY, new CancelPayStrategy());
            strategyMap.put(PAY_STATE, new PayStrategy());
        }

        public static Strategy creator(String event) {
            return strategyMap.get(event);
        }
    }
    
    /**
     * 深圳医保
     * 生成医保记录
     * ins_szyb_st
     * ins_szyb_st_city
     * ins_szyb_st_citydt
     * ins_szyb_visit
     * ins_szyb_visit_city
     * @param regvo
     * @param map
     */
    private static void saveInsSzybInfo(PiMasterRegVo regvo, Map<String,Object> map,User u){
		String pkOpDoctor = u.getPkEmp();// 当前用户主键
		String codeOrg = ApplicationUtils.getPropertyValue("qgyb.codeOrg", "");
		String nameOrg = ApplicationUtils.getPropertyValue("qgyb.nameOrg", "");
		
//    	InsSzybVisit insSzybVisit = new InsSzybVisit();
//    	String pkVisit = NHISUUID.getKeyId();
//    	insSzybVisit.setPkVisit(pkVisit);
//    	insSzybVisit.setPkOrg(regvo.getPkOrg());
//    	insSzybVisit.setPkHp(regvo.getPkHp());
//    	insSzybVisit.setPkPv(regvo.getPkPv());
//    	insSzybVisit.setPkPi(regvo.getPkPi());
//    	insSzybVisit.setCodeCenter("");
//    	insSzybVisit.setCodeOrg(codeOrg);//医院编码
//    	insSzybVisit.setNameOrg(nameOrg);//医院名称
//    	insSzybVisit.setTransid(CommonUtils.getPropValueStr(map,"serialNumber"));//交易流水号
//    	//zpr-4
//    	insSzybVisit.setCodeInsst(CommonUtils.getPropValueStr(map,"bke384"));//机构结算业务号
//    	//zpr-3  医保登记号
//    	insSzybVisit.setPvcodeIns(CommonUtils.getPropValueStr(map,"akc190"));//就医登记号
//    	insSzybVisit.setNamePi(regvo.getNamePi());
//    	insSzybVisit.setPersontype(CommonUtils.getPropValueStr(map,"aka130"));//医疗类别(默认普通门诊)  CommonUtils.getPropValueStr(map,"code")
//    	insSzybVisit.setEuResctype("");//救助对象类型	0 非医疗救助类别；1；低保对象；2 特困供养人员；3 孤儿；4 建档立卡贫困人员；5 低收入家庭的老年人；6 低收入家庭的未成年人；7 低收入家庭的重度残疾人；8 低收入家庭的重病患者；9 其他
//    	insSzybVisit.setBirthDate(regvo.getBirthDate());
//    	insSzybVisit.setIdno(regvo.getIdNo());
//    	insSzybVisit.setCodeAreayd("440300");//深圳市行政区划代码
//    	insSzybVisit.setDtExthp("03");//深圳市医保
//    	insSzybVisit.setDateReg(new Date());
//    	insSzybVisit.setDtSttypeIns("");//结算类型
//    	insSzybVisit.setEuStatusSt("0");//门诊：0结算中（挂号登记开始） 1结算完成（挂号登记完成）
//    	insSzybVisit.setCodeMedino(CommonUtils.getPropValueStr(map,"ylzh"));//医疗证号加密串
//    	insSzybVisit.setCreator(pkOpDoctor);
//    	insSzybVisit.setCreateTime(new Date());
//    	insSzybVisit.setTs(new Date());
//    	insSzybVisit.setDelFlag("0");
//    	DataBaseHelper.insertBean(insSzybVisit);


        //查询医保信息
        String sql = "select * from INS_QGYB_VISIT where MDTRT_ID = ?";
        String mdtrtId = MapUtils.getString(map,"mdtrtId");
        String setlId = MapUtils.getString(map,"setl_id");
        if(!org.springframework.util.StringUtils.isEmpty(mdtrtId)){
            InsQgybVisit insQgybVisit = DataBaseHelper.queryForBean(sql,InsQgybVisit.class,mdtrtId);
            insQgybVisit.setPkPv(regvo.getPkPv());
            insQgybVisit.setPkPi(regvo.getPkPi());
            insQgybVisit.setNamePi(regvo.getNamePi());
            insQgybVisit.setIdno(regvo.getIdNo());
            DataBaseHelper.update(DataBaseHelper.getUpdateSql(InsQgybVisit.class),insQgybVisit);

            InsQgybSt insQgybSt = new InsQgybSt();
            String pkInsst = NHISUUID.getKeyId();
            insQgybSt.setMdtrtId(mdtrtId);
            insQgybSt.setSetlId(setlId);
            insQgybSt.setPkInsst(pkInsst);
            insQgybSt.setPsnNo(insQgybVisit.getPsnNo());
            insQgybSt.setPkVisit(insQgybVisit.getPkVisit());
            insQgybSt.setPkOrg(insQgybVisit.getPkOrg());
            insQgybSt.setPkHp(insQgybVisit.getPkHp());
            insQgybSt.setPkPv(insQgybVisit.getPkPv());
            insQgybSt.setPkPi(insQgybVisit.getPkPi());
            insQgybSt.setPkSettle(regvo.getPkSettle());
            insQgybSt.setDateSt(new Date());
            String money = MapUtils.getString(map,"fundPayamt","0.0");
            String inscpScpamt = MapUtils.getString(map,"inscpScpamt","0.0");
            insQgybSt.setAmount(Double.valueOf(money));//结算金额
            insQgybSt.setFundPaySumamt(Double.valueOf(money));
            insQgybSt.setInscpScpAmt(Double.valueOf(inscpScpamt));
            insQgybSt.setCreator(pkOpDoctor);
            insQgybSt.setCreateTime(new Date());
            insQgybSt.setTs(new Date());
            insQgybSt.setDelFlag("0");
            DataBaseHelper.insertBean(insQgybSt);


        }








//    	InsSzybSt insSzybSt = new InsSzybSt();
//    	String pkInsst = NHISUUID.getKeyId();
//    	insSzybSt.setPkInsst(pkInsst);
//    	insSzybSt.setPkVisit(pkVisit);
//    	insSzybSt.setPkOrg(regvo.getPkOrg());
//    	insSzybSt.setCodeHpst(CommonUtils.getPropValueStr(map,"ckc618"));//医保业务号
//    	insSzybSt.setPkHp(regvo.getPkHp());
//    	insSzybSt.setPkPv(regvo.getPkPv());
//    	insSzybSt.setPkPi(regvo.getPkPi());
//    	insSzybSt.setPkSettle(regvo.getPkSettle());
//    	insSzybSt.setPvcodeIns(CommonUtils.getPropValueStr(map,"akc190"));//就医登记号
//    	insSzybSt.setDateSt(new Date());
//    	insSzybSt.setCodeSerialno(CommonUtils.getPropValueStr(map, "bke384"));//机构结算业务号
//    	insSzybSt.setAmount(0.0);//结算金额
//    	insSzybSt.setCodeCenter("");
//    	insSzybVisit.setCodeOrg(codeOrg);//医院编码
//    	insSzybVisit.setNameOrg(nameOrg);//医院名称
//    	insSzybSt.setCodeSerialno(CommonUtils.getPropValueStr(map,"bke384"));//机构结算业务号
//    	insSzybSt.setCreator(pkOpDoctor);
//    	insSzybSt.setCreateTime(new Date());
//    	insSzybSt.setTs(new Date());
//    	insSzybSt.setDelFlag("0");
//    	DataBaseHelper.insertBean(insSzybSt);







//    	InsSzybStCity insSzybStCity = new InsSzybStCity();
//    	String pkInsstCity = NHISUUID.getKeyId();
//    	insSzybStCity.setPkInsstcity(pkInsstCity);
//    	insSzybStCity.setPkInsst(pkInsst);
//    	insSzybStCity.setDtMedicate(CommonUtils.getPropValueStr(map,"aka130"));//医疗类别
//    	insSzybStCity.setAmtJjzf(0.0);//基金支付金额
//    	insSzybStCity.setAmtGrzhzf(0.0);//个人帐户支付金额
//    	insSzybStCity.setAmtGrzf(0.0);//个人支付金额
//    	//insSzybStCity.setAmtGrzh(0.0);//个人账户金额
//    	insSzybStCity.setCreator(pkOpDoctor);
//    	insSzybStCity.setCreateTime(new Date());
//    	insSzybStCity.setTs(new Date());
//    	insSzybStCity.setDelFlag("0");
//    	DataBaseHelper.insertBean(insSzybStCity);


    	List<Map<String, Object>> outputlist = (List<Map<String, Object>>)map.get("outputlist");
		List<Map<String, Object>> outputlist2 = (List<Map<String, Object>>)map.get("outputlist2");

        if(outputlist!=null){
            outputlist = new ArrayList<>();
        }

        if(outputlist2!=null){
            outputlist2 = new ArrayList<>();
        }

    	List<InsSzybStCitydt> InsSzybStCitydtList = new ArrayList<InsSzybStCitydt>();
    	for (Map<String, Object> output : outputlist) {
//			InsSzybStCitydt citydt = new InsSzybStCitydt();
//			citydt.setTypeOutput(CommonUtils.getPropValueStr(output, "aka111"));//大类代码
//			citydt.setCategory(CommonUtils.getPropValueStr(output, "aka111"));//大类代码
//			String amt=CommonUtils.getPropValueStr(output, "bka058");//费用金额
//			amt = "".equals(amt)?"0":amt;
//			citydt.setAmtFee(0.0);//费用金额
//			citydt.setPkInsstcitydt(NHISUUID.getKeyId());
//			citydt.setPkInsstcity(insSzybStCity.getPkInsstcity());
//			citydt.setCreator(u.getPkEmp());
//			citydt.setCreateTime(new Date());
//			citydt.setDelFlag("0");
//			citydt.setTs(new Date());
//			InsSzybStCitydtList.add(citydt);
		}

    	for (Map<String, Object> output2 : outputlist2) {
//			InsSzybStCitydt citydt = new InsSzybStCitydt();
//			citydt.setTypeOutput(CommonUtils.getPropValueStr(output2, "aaa036"));//支付项目代码
//			citydt.setCategory(CommonUtils.getPropValueStr(output2, "aaa036"));//支付项目代码
//			String amt=CommonUtils.getPropValueStr(output2, "aae019");//金额
//			amt = "".equals(amt)?"0":amt;
//			citydt.setAmtFee(Double.valueOf(amt));//费用金额
//			citydt.setPkInsstcitydt(NHISUUID.getKeyId());
//			citydt.setPkInsstcity(insSzybStCity.getPkInsstcity());
//			citydt.setCreator(u.getPkEmp());
//			citydt.setCreateTime(new Date());
//			citydt.setDelFlag("0");
//			citydt.setTs(new Date());
//			InsSzybStCitydtList.add(citydt);
		}


    	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybStCitydt.class),InsSzybStCitydtList);

//    	InsSzybVisitCity  insSzybVisitCity = new InsSzybVisitCity();
//    	String pkVisitcity = NHISUUID.getKeyId();
//    	insSzybVisitCity.setPkVisitcity(pkVisitcity);
//    	insSzybVisitCity.setPkOrg(regvo.getPkOrg());
//    	insSzybVisitCity.setPkVisit(pkVisit);
//    	insSzybVisitCity.setPkPv(regvo.getPkPv());
//    	insSzybVisitCity.setAaz515(CommonUtils.getPropValueStr(map,"aaz515"));//社保卡状态
//    	insSzybVisitCity.setAaz500(CommonUtils.getPropValueStr(map,"aaz500"));//社保卡号
//    	insSzybVisitCity.setAac001(1L);//人员ID
//    	insSzybVisitCity.setAac999(CommonUtils.getPropValueStr(map,"aac999"));//人员电脑号
//    	insSzybVisitCity.setCka303(CommonUtils.getPropValueStr(map,"cka303"));//大病类别
//    	String idType = regvo.getDtIdtype();
//    	String aac058 = "";
//    	switch (idType) {
//			case "01":aac058 = "01";break;
//			case "02":aac058 = "02";break;
//			case "03":aac058 = "04";break;
//			case "06":aac058 = "04";break;
//			case "07":aac058 = "05";break;
//			case "08":aac058 = "06";break;
//			case "09":aac058 = "07";break;
//			case "10": aac058 = "08";break;
//			case "99":aac058 = "99";break;
//			default:break;
//		}
//    	insSzybVisitCity.setAac058(aac058);
//    	insSzybVisitCity.setAac147(regvo.getIdNo());
//    	insSzybVisitCity.setAac002(regvo.getIdNo());//社会保障号码
//    	insSzybVisitCity.setAac003(regvo.getNamePi());
//    	String dtSex = regvo.getDtSex();
//    	String aac004 = "";
//    	switch (dtSex) {
//			case "02":aac004 = "1";break;
//			case "03":aac004 = "2";break;
//			default:aac004 = "9";break;
//		}
//    	insSzybVisitCity.setAac004(aac004);
//    	if(regvo.getBirthDate()!=null) {
//            insSzybVisitCity.setAac006(0);
//        }
//    	//年纪处理
//    	String agePv = regvo.getAgePv();
//    	if(agePv!=null){
//    		insSzybVisitCity.setBae093(0);
//    	}else{
//    		throw new BusException("年龄数据错误！"+agePv);
//    	}
//    	insSzybVisitCity.setCac215(regvo.getAddrOrigin());
//    	insSzybVisitCity.setCreator(pkOpDoctor);
//    	insSzybVisitCity.setCreateTime(new Date());
//    	insSzybVisitCity.setTs(new Date());
//    	insSzybVisitCity.setDelFlag("0");
//    	DataBaseHelper.insertBean(insSzybVisitCity);
//    	//更新医保卡号到患者信息表
//    	if(!StringUtils.isEmpty(CommonUtils.getPropValueStr(map,"aaz500"))){
//	    	PiMaster pi=new PiMaster();
//	    	pi.setPkPi(regvo.getPkPi());
//	    	pi.setInsurNo(CommonUtils.getPropValueStr(map,"aaz500"));
//	    	DataBaseHelper.updateBeanByPk(pi,false);
//    	}

    }





}
