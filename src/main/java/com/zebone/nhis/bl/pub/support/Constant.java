package com.zebone.nhis.bl.pub.support;

public class Constant {
    
    //价格策略-院内票据分类-bd_invcate
    public static final String OPINV = "0";//门诊发票 
    public static final String IPINV = "1";//住院发票 
    public static final String REGCERT= "3";//挂号凭条 
    public static final String PATIPREPAY= "4";//患者预交金收据 
    public static final String HOSPREPAY = "5";//住院预交金收据 
    public static final String COMPAYINV = "6";//单位发票 
    public static final String OTHERINV = "9";//其他
    
    //患者就诊状态 
    public static final String REGISTER = "0";//0 登记 
    public static final String VISIT  = "1";//1 就诊 
    public static final String END= "3";//2 结束 
    public static final String SETTLE= "4";//3 结算
    public static final String BACK = "5";//9 退诊 
    
    //患者就诊类型
    public static final String OP = "0";//1门诊
    public static final String EMERGENCY  = "1";//2急诊
    public static final String IP= "3";//3住院
    public static final String  MEDICALCHECK= "4";//4体检
    public static final String  HOMEBED= "5";//5家庭病床 
    

}
