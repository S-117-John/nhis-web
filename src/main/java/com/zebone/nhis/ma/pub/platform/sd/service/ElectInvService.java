package com.zebone.nhis.ma.pub.platform.sd.service;

import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.mybatis.ReflectHelper;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.springframework.stereotype.Component;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 电子票据
 * @author bat
 * @date 2020/10/21
 */
@Component
public class ElectInvService {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 挂号电子发票生成
     * @param pkPv
     * @param user
     * @param pkSettle
     */
    public Map<String,Object> registrationElectInv(String pkPv, User user, String pkSettle) throws Exception {
        //返回结果集
        Map<String, Object> result = null;
        Class<?> c = Class.forName("com.zebone.nhis.bl.pub.support.InvSettltService");
        //Method method= c.getMethod("repEBillRegistration",String.class, IUser.class,String.class);
        //Map<String, Object> resInv = (Map<String, Object>) method.invoke(c.newInstance(),pkPv,user,pkSettle);
        //获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
        //String eBillFlag = invSettltService.getBL0031ByNameMachine(invoInfos.get(0).getNameMachine());
        Method method= c.getMethod("getBL0031ByNameMachine",String.class);
        String eBillFlag = (String) method.invoke(c.newInstance(),user.getCodeEmp());
        if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
            // 调用开立票据接口生成票据信息
            Map<String,Object> paramMap = new HashMap<>(16);
            paramMap.put("pkPv", pkPv);
            paramMap.put("pkSettle", pkSettle);
            //是否打印纸质票据
            paramMap.put("flagPrint", "0");
            paramMap.put("invoInfos", null);
            paramMap.put("dateBegin", null);
            paramMap.put("dateEnd", null);
            paramMap.put("billList", null);
            paramMap.put("flagUpdate", "0");
            result = (Map<String, Object>)ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillRegistration", new Object[]{paramMap,user});
        }
        //返回结果集
        if(result==null){
            result = new HashMap<>(16);
            if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
                result.put("result","生成电子发票失败！method mzOutElectInv message:eBillMzOutpatient response is null");
            }else{
                result.put("result","电子参数状态为未启用！");
            }
        }else{
            //生成发票记录 挂号不需要保存
            //String resultStr = saveBlInvoice(resInv);
            result.put("result","获取电子票据成功");
        }
        return result;
    }

    /**
     * 结算电子发票生成
     * @param pkPv
     * @param user
     * @param pkSettle
     */
    public Map<String,Object> mzOutElectInv(String pkPv, User user, String pkSettle) throws Exception {
        //返回结果集
        Map<String, Object> result = null;
        Class<?> c = Class.forName("com.zebone.nhis.bl.pub.support.InvSettltService");
        //Method method= c.getMethod("repEBillMzOutpatient",String.class,IUser.class,String.class);
        //Map<String, Object> resInv = (Map<String, Object>) method.invoke(c.newInstance(),pkPv,user,pkSettle);
        //获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
        //String eBillFlag = invSettltService.getBL0031ByNameMachine(invoInfos.get(0).getNameMachine());
        Method method= c.getMethod("getBL0031ByNameMachine",String.class);
        String eBillFlag = (String) method.invoke(c.newInstance(),user.getCodeEmp());
        if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
            // 调用开立票据接口生成票据信息
            Map<String,Object> paramMap = new HashMap<>(16);
            paramMap.put("pkPv", pkPv);
            paramMap.put("pkSettle", pkSettle);
            //是否打印纸质票据
            paramMap.put("flagPrint", "0");
            paramMap.put("invoInfos", null);
            paramMap.put("dateBegin", null);
            paramMap.put("dateEnd", null);
            paramMap.put("billList", null);
            result = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillMzOutpatient", new Object[]{paramMap,user});
            //如果发票数据返回为空
            if(result != null ){
                //生成发票记录
                String resultStr = saveBlInvoice(result,pkSettle);
                result.put("result",resultStr);
            }
        }
        if(result==null){
            result = new HashMap<>(16);
            if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
                result.put("result","生成电子发票失败！method mzOutElectInv:eBillMzOutpatient response is null or eBillFlag is close");
            }else{
                result.put("result","电子参数状态为未启用！");
            }
        }
        return result;
    }

    /**
     *  结算写入发票记录
     * @param resInv
     * @return
     */
    private String saveBlInvoice(Map<String, Object> resInv,String pkSettle){
        String result = "";
        if(resInv!=null && resInv.size()>0 && resInv.containsKey("inv")){
            //主表数据  "inv", invs
            List<BlInvoice> invList = (List<BlInvoice>) resInv.get("inv");
            //从表数据 "invDt", invDtList
            List<BlInvoiceDt> invDtList = (List<BlInvoiceDt>) resInv.get("invDt");
            //发票与结算关联
            List<BlStInv> stinvList = new ArrayList<>();
            //组织电子票据信息
            if(invList!=null && invList.size()>0){
                //发票主表
                int[] ints = DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), invList);
                //发票细表
                int[] intsdt = DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), invDtList);
                //发票与明细关联
                for(BlInvoice invo : invList){
                    BlStInv vo = new BlStInv();
                    vo.setPkInvoice(invo.getPkInvoice());
                    vo.setPkOrg(invo.getPkOrg());
                    vo.setPkSettle(pkSettle);
                    vo.setPkStinv(NHISUUID.getKeyId());
                    setDefaultValue(vo, true);
                    stinvList.add(vo);
                }
                int[] intsStinv = DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), stinvList);
                //发票和结算关联表
                result = (ints==null||ints.length<1||intsdt==null||intsdt.length<1||intsStinv==null||intsStinv.length<1)?"获取电子发票失败，写入数据失败！":"获取电子发票成功！";
            }else{
                result = "获取电子发票失败，发票信息为空！";
            }
        }else{
            result = "获取电子发票失败：BlInvoice is null ";
        }
        return result;
    }

    /**
     * 获取电子票据数据转换为消息数据
     * 不论转换成功与否，程序都往下走
     * @param map
     * @return
     */
    public static Map<String, Object> getBlInvoiceMsg(Map<String, Object> map){
        Map<String, Object> result = new HashMap<>(16);
        try{
            //自助机挂号
            if(map.containsKey("inv")){
                List<BlInvoice> invoiceList = (List<BlInvoice>) map.get("inv");
                //电子发票数据
                //EbillCode	电子票据代码 ebillbatchcode 电子票据代码
                result.put("ebillCode",invoiceList.get(0).getEbillbatchcode());
                //EbillNo	电子票据号码 ebillno 电子票据号码
                result.put("ebillNo",invoiceList.get(0).getEbillno());
                //CheckCode	电子校验码 checkcode 电子校验码
                result.put("checkCode",invoiceList.get(0).getCheckcode());
                //EbillDate	电子票据生成时间 dateEbill 电子票据生成时间
                result.put("ebillDate",sdf.format(invoiceList.get(0).getDateEbill()));
                //EbillQRCode	电子票据二维码效验数据 qrcode_ebill 电子票据二维码效验数据(byte[])
                result.put("ebillQRCode",Base64.getEncoder().encodeToString(invoiceList.get(0).getQrcodeEbill()).replaceAll("[\\s*\t\n\r]",""));
                //生成图片测试
                //pictureTest(invoiceList,result);
            }else {
                //result.put("result","电子发票数据转换为消息数据失败！");
                result.put("error","");
            }
        }catch (Exception e){
            e.printStackTrace();
            result.put("result","电子发票数据转换为消息数据异常！"+e.getClass());
            result.put("error","");
        }finally {
            return result;
        }
    }

    /**
     * 默认参数赋值
     * @param obj
     * @param flag
     */
    public static void setDefaultValue(Object obj, boolean flag) {

        User user = UserContext.getUser();

        Map<String,Object> default_v = new HashMap<String,Object>(16);
        if(flag){
            // 如果新增
            default_v.put("pkOrg", user.getPkOrg());
            default_v.put("creator", user.getPkEmp());
            default_v.put("createTime",new Date());
            default_v.put("delFlag", "0");
        }

        default_v.put("ts", new Date());
        default_v.put("modifier",  user.getPkEmp());

        Set<String> keys = default_v.keySet();

        for(String key : keys){
            Field field = ReflectHelper.getTargetField(obj.getClass(), key);
            if (field != null) {
                ReflectHelper.setFieldValue(obj, key, default_v.get(key));
            }
        }

    }

    /**
     * 生成二维码图片
     * @param invoiceList
     * @param result
     */
    private static void pictureTest(List<BlInvoice> invoiceList,Map<String, Object> result){
        //测试
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File("c:\\hl7\\"+invoiceList.get(0).getEbillno()+".jpg"));
            imageOutput.write(invoiceList.get(0).getQrcodeEbill(), 0, invoiceList.get(0).getQrcodeEbill().length);
            imageOutput.close();

            byte[] b = Base64.getDecoder().decode(SDMsgUtils.getPropValueStr(result,"ebillQRCode"));
            //处理数据
            for (int i = 0;i<b.length;++i){
                if(b[i]<0){
                    b[i]+=256;
                }
            }
            FileOutputStream outputStream1 = new FileOutputStream("c:\\hl7\\"+invoiceList.get(0).getEbillno()+"(1).jpg");
            outputStream1.write(b);
            outputStream1.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
