package com.zebone.nhis.ma.pub.platform.receive.yh.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.mq.msg.service.EwellMqHelper;
import com.zebone.mq.msg.service.MqMsgService;
import com.zebone.mq.msg.support.SDKMutiImpl;
import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.AdtRegister.AdtRegisterESBEntry;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.AdtRegister.AdtRegisterInfo;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.CancleSettle.CancleSettleESBEntry;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.CancleSettle.CancleSettleInfo;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.PatiCg.PatiCgESBEntry;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.PatiCg.PatiCgInfo;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.PrePay.PrePayESBEntry;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.PrePay.PrePayInfo;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.StrikeSettle.StrikeSettleESBEntry;
import com.zebone.nhis.ma.pub.platform.receive.yh.vo.StrikeSettle.StrikeSettleInfo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class CnIPReceiveMsgService {
	private Logger logger = LoggerFactory.getLogger(CnIPReceiveMsgService.class);
	@Resource
	private EwellMqHelper ewellMqHelper;
	@Resource
	private IPHandleService iPHandleService;
	@Resource
	private MqMsgService mqMsgService;
	
	@Value("#{applicationProperties['workspaceIsJm']}")
    private String workspaceIsJm;

//    @Value("#{defaultProperties['workspaceIsJm.waittime']}")
//    private int waittime;
	
	@PostConstruct
    public void saveEwellMessage() {
        if(Boolean.parseBoolean(workspaceIsJm)){
            //??????????????????
            this.saveIpAdtRegInfo();
            
            //????????????
            this.ipStrikeSettle();
            
            //????????????
            this.cancleSettle();

            //???????????????
            this.savePrePayInfo();
            
            //???????????????
            this.savePatiCgInfo();


            
        }

    }
	
	/**
	 * ????????????????????????
	 */
	@Transactional
    public void saveIpAdtRegInfo() {
		final User user = new User();
		final String fid = "PS10036_1";
		BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select pk_org from BD_OU_ORG where CODE_ORG='0101'", BdOuOrg.class, new Object[]{});
		user.setPkOrg(bdOuOrg.getPkOrg());
		UserContext userContext = new UserContext();
		userContext.setUser(user);
    	SDKMutiImpl sdkMutiImpl = new SDKMutiImpl("QMGR.P40_1",fid) {
			@Override
			public Object OnMessage(String msg, String msgid) {
				MDC.put("responseXml", msg);
				try {
					AdtRegisterESBEntry adtEsbEntry = ewellMqHelper.convertToJavaBean(msg, AdtRegisterESBEntry.class);
					//????????????
					mqMsgService.saveSysMsgResp(msg, "PS10036", null, "");
					//???????????????????????????
					System.out.println(msg);
					//??????????????????????????????????????????
					AdtRegisterInfo adtInfo = adtEsbEntry.getMsgInfo().getMsg();
					//????????????????????????
					iPHandleService.saveIpAdtRegInfo(adtInfo,user);
				} catch (Exception e) {

                    mqMsgService.saveSysMsgResp(msg, "PS10036", null, "????????????:"+getExceptionMsg(e));
					e.printStackTrace();
				}
                return null;
			}
			@Override
			public Object OnException(Exception e) {
				Date now=new Date();
                //MessageLogUtil.erroeLog(fid,"",now,"EWELL","NEMS",e,MDC.get("responseXml"),MDC.get("notes"));
                logger.error("???????????????", e);
                return null;
			}
		};
		sdkMutiImpl.getMessage();
    }

	/**
	 * ?????????????????????
	 */
	@Transactional
    public void savePrePayInfo() {
		final User user = new User();
		final String fid = "PS15009_1";
		BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select pk_org from BD_OU_ORG where CODE_ORG='0101'", BdOuOrg.class, new Object[]{});
		user.setPkOrg(bdOuOrg.getPkOrg());
		UserContext userContext = new UserContext();
		userContext.setUser(user);
    	SDKMutiImpl sdkMutiImpl = new SDKMutiImpl("QMGR.P40_1",fid) {
			@Override
			public Object OnMessage(String msg, String msgid) {
				try {
					mqMsgService.saveSysMsgResp(msg, "PS15009", null, "");
					PrePayESBEntry pESBEntry = ewellMqHelper.convertToJavaBean(msg, PrePayESBEntry.class);
					//???????????????????????????
					System.out.println(msg);
					//??????????????????????????????????????????
					PrePayInfo preInfo = pESBEntry.getMsgInfo().getMsg();
					//?????????????????????
					iPHandleService.savePrePayInfo(preInfo,user);
				} catch (Exception e) {
					e.printStackTrace();
					mqMsgService.saveSysMsgResp(msg, "PS15009", null, "????????????:"+getExceptionMsg(e));
				}
                return null;
			}
			
			@Override
			public Object OnException(Exception arg0) {
				return null;
			}
		};
		sdkMutiImpl.getMessage();
    }
	
	/**
	 * ??????????????? 
	 */
	@Transactional
    public void savePatiCgInfo() {
		final User user = new User();
		BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select pk_org from BD_OU_ORG where CODE_ORG='0101'", BdOuOrg.class, new Object[]{});
		user.setPkOrg(bdOuOrg.getPkOrg());
		UserContext userContext = new UserContext();
		userContext.setUser(user);
    	SDKMutiImpl sdkMutiImpl = new SDKMutiImpl("QMGR.P40_1","PS15010_1") {
			@Override
			public Object OnMessage(String msg, String msgid) {
				try {
					mqMsgService.saveSysMsgResp(msg, "PS15010", null, "");
					PatiCgESBEntry patiEsbEntry = ewellMqHelper.convertToJavaBean(msg, PatiCgESBEntry.class);
					//???????????????????????????????????????
					List<PatiCgInfo> cgInfoList = patiEsbEntry.getMsgInfo().getMsg();
					//???????????????????????????
					iPHandleService.savePatiCgInfo(cgInfoList,user);
				} catch (Exception e) {
					e.printStackTrace();
					mqMsgService.saveSysMsgResp(msg, "PS15010", null, "????????????:"+getExceptionMsg(e));
				}
                return null;
			}
			
			@Override
			public Object OnException(Exception arg0) {
				return null;
			}
		};
		sdkMutiImpl.getMessage();
    }
	
	/**
	 * ????????????
	 */
	@Transactional
    public  void ipStrikeSettle() {
		final User user = new User();
		BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select pk_org from BD_OU_ORG where CODE_ORG='0101'", BdOuOrg.class, new Object[]{});
		user.setPkOrg(bdOuOrg.getPkOrg());
		UserContext userContext = new UserContext();
		userContext.setUser(user);
    	SDKMutiImpl sdkMutiImpl = new SDKMutiImpl("QMGR.P40_1","PS15011_1") {
			@Override
			public Object OnMessage(String msg, String msgid) {
				try {
					mqMsgService.saveSysMsgResp(msg, "PS15011", null, "");
					StrikeSettleESBEntry sESBEntry = ewellMqHelper.convertToJavaBean(msg, StrikeSettleESBEntry.class);
					//???????????????????????????
					System.out.println(msg);
					//??????????????????????????????????????????
					StrikeSettleInfo strikeSettleInfo = sESBEntry.getMsgInfo().getMsg();
					//????????????????????????
					iPHandleService.strike(strikeSettleInfo,user);
				} catch (Exception e) {
					e.printStackTrace();
					mqMsgService.saveSysMsgResp(msg, "PS15011", null, "????????????:"+getExceptionMsg(e));
				}
                return null;
			}
			
			@Override
			public Object OnException(Exception arg0) {
				return null;
			}
		};
		sdkMutiImpl.getMessage();
    }
	/**
	 * ????????????
	 */
	private void cancleSettle(){
		final User user = new User();
		BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select pk_org from BD_OU_ORG where CODE_ORG='0101'", BdOuOrg.class, new Object[]{});
		user.setPkOrg(bdOuOrg.getPkOrg());
		UserContext userContext = new UserContext();
		userContext.setUser(user);
    	SDKMutiImpl sdkMutiImpl = new SDKMutiImpl("QMGR.P40_1","PS15012_1") {
			@Override
			public Object OnMessage(String msg, String msgid) {
				try {
					//????????????
					mqMsgService.saveSysMsgResp(msg, "PS15012", null, "");
					CancleSettleESBEntry csESBEntry = ewellMqHelper.convertToJavaBean(msg, CancleSettleESBEntry.class);
					//???????????????????????????
					System.out.println(msg);
					//??????????????????????????????????????????
					CancleSettleInfo csInfo = csESBEntry.getMsgInfo().getMsg();
					//????????????
					iPHandleService.cancelSettle(csInfo,user);
				} catch (Exception e) {
					e.printStackTrace();
					//????????????
					mqMsgService.saveSysMsgResp(msg, "PS15012", null, "????????????:"+getExceptionMsg(e));
				}
                return null;
			}
			@Override
			public Object OnException(Exception arg0) {
				return null;
			}
		};
		sdkMutiImpl.getMessage();
	}
	
	/**
	 * ????????????
	 * @param param
	 * @param user
	 */
	public void testMsg(String param, IUser user){

		List<BdOrd> strings = DataBaseHelper.queryForList("select PK_ORD,name from bd_ord where CODE_ORDTYPE='02' and FLAG_IP='1' and FLAG_OP='0'and DT_ORDCATE is null", BdOrd.class, new Object() {
		});
		//BdOrdAlias bdOrdAlias=new BdOrdAlias();
		//bdOrdAlias.
		for (BdOrd str : strings) {
			System.out.println(str);
			DataBaseHelper.update("insert into BD_ORD_EMR(PK_ORDEMR, PK_ORD, CODE_EMRTEMP, NAME_EMRTEMP, FLAG_ACTIVE, EU_PVTYPE, PK_EMRTEMP, pk_org,\n" +
					"                       creator, create_time, ts, del_flag)\n" +
					"values (LOWER(REPLACE(NEWID(), '-', '')), ?, '2725', '???????????????????????????', '1', '3',\n" +
					"        '0984e929302244beb5be166cad8c444b', '~                              ', '8105a3537c114bdab18523559c60cd9e',\n" +
					"        '2019-04-18 10:22:14.163', '2019-04-18 10:22:14.163', 0)",str.getPkOrd());

			DataBaseHelper.update("insert into BD_ORD_RIS(PK_ORDRIS, PK_ORD, pk_org, creator, create_time, ts, del_flag)\n" +
					"values (LOWER(REPLACE(NEWID(), '-', '')), ?, '~                               ',\n" +
					"        '8105a3537c114bdab18523559c60cd9e', '2019-04-18 09:38:26.963', '2019-04-18 09:38:26.963', 0)",str.getPkOrd());
			DataBaseHelper.update("insert into BD_ORD_ALIAS(PK_ORDALIA, PK_ORD, ALIAS, SPCODE, D_CODE, pk_org, creator, create_time, ts, del_flag)\n" +
					"values (LOWER(REPLACE(NEWID(), '-', '')), ?, ?, 'QBXXJSJT',\n" +
					"        'UNXXYTSW', '~                              ', 'nhisemp0000000000000000000000000', '2019-01-24 16:57:13.897',\n" +
					"        '2019-01-24 16:57:13.897', 0)",str.getPkOrd(),str.getName());

		}
		//?????????????????????
		/*String msg = "<ESBEntry>\n" +
			    "    <AccessControl>\n" +
			    "        <UserName>HIS</UserName>\n" +
			    "        <Password>123456</Password>\n" +
			    "        <SysFlag>1</SysFlag>\n" +
			    "        <Fid>PS10036</Fid>\n" +
			    "    </AccessControl>\n" +
			    "    <MessageHeader>\n" +
			    "        <Fid>PS10036</Fid>\n" +
			    "        <SourceSysCode>S01</SourceSysCode>\n" +
			    "        <ReturnFlag>-1</ReturnFlag>\n" + 
			    "        <TargetSysCode>S40</TargetSysCode>\n" +
			    "        <MsgDate>2017-05-25 14:00:0</MsgDate>\n" +
			    "    </MessageHeader>\n" +
			    "    <MsgInfo>\n" +
			    "        <Msg action=\"insert\">\n" +
			    "	<CHARGE_ITEM_INDEX_NO>100059</CHARGE_ITEM_INDEX_NO>	" +
			    "	<INHOSP_INDEX_NO>523341_1</INHOSP_INDEX_NO>	" +
			    "	<DEPT_INDEX_NO_APP>3020401</DEPT_INDEX_NO_APP>	" +
			    "	<DRUG_INDEX_NO>109019_01</DRUG_INDEX_NO>	" +
			    "	<DRUG_SPEC>2g x5???/???</DRUG_SPEC>	" +
			    "	<PAT_INDEX_NO>000247677100</PAT_INDEX_NO>	" +
			    "	<DEPT_INDEX_NO_EX>3020401</DEPT_INDEX_NO_EX>	" +
			    "	<DEPT_INDEX_NO_CG>3020401</DEPT_INDEX_NO_CG>	" +
			    "	<DRUG_FLAG>0</DRUG_FLAG>	" +
			    "	<TOTAL_MONEY>50</TOTAL_MONEY>	" +
			    "	<DRUG_AMOUNT>1</DRUG_AMOUNT>	" +
			    "	<CHARGE_INDEX_NO>AA1212121212</CHARGE_INDEX_NO>	" +
			    "	<CHARGE_FLAG>1</CHARGE_FLAG>	" +
			    "	<OPERATOR_NO>01350</OPERATOR_NO>	" +
			    "	<ORDER_NO>01350</ORDER_NO>	" +
			    "	<PAYMENT_TIME>2019-01-21 15:49:40</PAYMENT_TIME>	" +
			    "        </Msg>\n" +
			    "    </MsgInfo>\n" +
			    "</ESBEntry>  ";		
		String message = null;
		PatiCgESBEntry patiEsbEntry = null;
		try {
			patiEsbEntry = ewellMqHelper.convertToJavaBean(msg, PatiCgESBEntry.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		User use = new User();
		BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select pk_org from BD_OU_ORG where CODE_ORG='0101'", BdOuOrg.class, new Object[]{});
		use.setPkOrg(bdOuOrg.getPkOrg());
		UserContext userContext = new UserContext();
		userContext.setUser(use);
		//???????????????????????????
		System.out.println(msg);
		mqMsgService.saveSysMsgResp(msg, "PS10036", null, "");
		//??????????????????????????????????????????
		List<PatiCgInfo> cgInfoList = patiEsbEntry.getMsgInfo().getMsg();
		//???????????????????????????
		iPHandleService.savePatiCgInfo(cgInfoList,use);*/
		
		//??????????????????
		/*String msg = "<ESBEntry>\n" +
			    "    <AccessControl>\n" +
			    "        <UserName>HIS</UserName>\n" +
			    "        <Password>123456</Password>\n" +
			    "        <SysFlag>1</SysFlag>\n" +
			    "        <Fid>PS15011</Fid>\n" +
			    "    </AccessControl>\n" +
			    "    <MessageHeader>\n" +
			    "        <Fid>PS15011</Fid>\n" +
			    "        <SourceSysCode>S01</SourceSysCode>\n" +
			    "        <ReturnFlag>-1</ReturnFlag>\n" + 
			    "        <TargetSysCode>S40</TargetSysCode>\n" +
			    "        <MsgDate>2019-03-11 11:12:12</MsgDate>\n" +
			    "    </MessageHeader>\n" +
			    "    <MsgInfo>\n" +
			    "        <Msg action=\"insert\">\n" +
			    "	<PAT_INDEX_NO>0002495813001</PAT_INDEX_NO>" +
			    "	<MFS_TYPE_CODE>10</MFS_TYPE_CODE>	" +
			    "	<MFS_RESULT_CODE>0</MFS_RESULT_CODE>" +
			    "	<DEPT_INDEX_NO_APP>1070000</DEPT_INDEX_NO_APP>" +
			    "	<PREPAID_MONEY>1000</PREPAID_MONEY>" +
			    "	<SELF_TOTAL_PAYMENT>0</SELF_TOTAL_PAYMENT>" +
			    "	<SELF_PAYMENT_FEE>12313</SELF_PAYMENT_FEE>" +
			    "	<INHOSP_INDEX_NO>523341_1</INHOSP_INDEX_NO>" +
			    "	<MFS_METHOD_CODE>1</MFS_METHOD_CODE>" +
			    "	<RECEIPT_TYPE>1234123</RECEIPT_TYPE>" +
			    "	<RECEIPT_INDEX_NO>pk12341234</RECEIPT_INDEX_NO>" +
			    "	<INVOICE_NO>444444444</INVOICE_NO>" +
			    "	<SELF_INDEX_NO>00024958130013</SELF_INDEX_NO>" +
			    "	<MEDICARE_PAYMENT>500</MEDICARE_PAYMENT>" +
			    "	<OPERATOR_NO>1111111</OPERATOR_NO>" +
			    "	<OPERAT_TIME>2019-03-11 11:12:12</OPERAT_TIME>" +
			    "        </Msg>\n" +
			    "    </MsgInfo>\n" +
			    "</ESBEntry>  ";		
		String message = null;
		StrikeSettleESBEntry sESBEntry = null;
		try {
			sESBEntry = ewellMqHelper.convertToJavaBean(msg, StrikeSettleESBEntry.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		User use = new User();
		BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select pk_org from BD_OU_ORG where CODE_ORG='0101'", BdOuOrg.class, new Object[]{});
		use.setPkOrg(bdOuOrg.getPkOrg());
		UserContext userContext = new UserContext();
		userContext.setUser(use);
		//???????????????????????????
		System.out.println(msg);
		mqMsgService.saveSysMsgResp(msg, "PS15010", null, "");
		//??????????????????????????????????????????
		StrikeSettleInfo strikeSettleInfo = sESBEntry.getMsgInfo().getMsg();
		//????????????????????????
		iPHandleService.strike(strikeSettleInfo,use);*/
		
		
		
		/*char[] cs = new char[5];		
		String pool="";		
		for ( short i= '0';i<='9';i++){			
			pool=pool+(char)i;			
		}		
		for(short i='A';i<='Z';i++){			
			pool=pool+(char)i;		
		}		
		for(short i='a';i<='z';i++){			
			pool=pool+(char)i;		
		}		
		System.out.println(pool);				
		for(int h=0;h<cs.length;h++){			
			int index=(int)(Math.random()*pool.length());			
			cs[h]=pool.charAt(index);		
		}		
		String str = new String(cs);		
		System.out.println(str);*/
	
	}
	private static String getExceptionMsg(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		e.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}
}
