package com.zebone.nhis.pro.zsrm.bl.service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.compay.ins.qgyb.MedicalCharges;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.pro.zsrm.bl.dao.ZsrmThirdPartyMapper;
import com.zebone.nhis.pro.zsrm.bl.vo.*;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.modules.utils.RedisUtils;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZsrmThirdPartyService {
    @Resource
    private ZsrmThirdPartyMapper thirdPartyMapper;

    /**
     * 交易号： 022006006001
     * 查询His第三方支付信息
     * @return
     */
    public PaymentCheckListVo qryHisTripartitePayment(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String type = MapUtils.getString(paramMap,"euOrdertype","");
        switch (type){
            case "UnionPay":    	
                return unionPayDataHandle(paramMap);//调用银联厂商对账接口获取数据
            case "Sande":            
                return sandeDataHandle(paramMap);//调用杉德厂商对账接口获取对账信息
            case "GenerationPOS":  
                return generationPOSDataHandle(paramMap);//调用一代POS获取对账信息
            case "Summary":
                return summaryHandle(paramMap);
            case "Seabird":
            	return seabirdDataHandle(paramMap);//海鹚对账信息
            case "QualityPay":
                return getQualityPayRecord(paramMap);//支付平台对账信息
            default:
                return null;
        }
    }

    /**
     * 交易号： 022006006001
     * 查询His第三方支付信息
     * @return
     */
    public List<PaymentCheckVo> qryHisTripartitePayments(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        Date datePay = DateUtils.strToDate(MapUtils.getString(paramMap,"datePay"), "yyyyMMdd");
        String dateBegin = DateUtils.addDate(datePay, -1, 4, "yyyyMMddHH");
        String dateEnd = DateUtils.addDate(datePay, 23, 4, "yyyyMMddHH");
        paramMap.put("dateExt", datePay);
        paramMap.put("dateBegin", dateBegin);
        paramMap.put("dateEnd", dateEnd);
        return thirdPartyMapper.getHisTripartite(paramMap);
    }

    /**
     * 交易号： 022006006012
     * zsrm-清除对账缓存
     * @param param
     * @param user
     */
    public void zsrmPaymentClearCache(String param, IUser user){
        Set<String> keys = RedisUtils.getRedisTemplate().keys("his:ChecksPayment:*");
        for (String key : keys) {
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(key)) {
                if (key.contains("ChecksPayment")) {
                    com.zebone.nhis.common.support.RedisUtils.getRedisTemplate().delete(key);
                }
            }
        }
    }

    /**
     * 银联
     * zsrm-支付对账银联相关数据业务处理
     * @param paramMap
     * @return
     */
    public PaymentCheckListVo unionPayDataHandle(Map<String, Object> paramMap){
        List<PaymentCheckVo> paymentList = new ArrayList<>();
        //1.从redis获取数据
        //1.1组装redis的Key
        StringBuffer rdeisKeyName = new StringBuffer("");
        rdeisKeyName.append(String.format("ChecksPayment:%s", MapUtils.getString(paramMap,"euOrdertype")));
        rdeisKeyName.append(String.format(":%s", MapUtils.getString(paramMap,"datePay")));
        Object redisParam= RedisUtils.getCacheHashObj(rdeisKeyName.toString(),MapUtils.getString(paramMap,"matchingType"));
        if(null != redisParam){
            paymentList = JsonUtil.readValue(redisParam.toString(), new TypeReference<List<PaymentCheckVo>>(){});;
        }else{
            //判断redis是否存在当前名称
            Set<String> keyss = RedisUtils.getRedisTemplate().keys("his:"+rdeisKeyName.toString());
            if(keyss.size()>0){
                //存在证明数据已匹配,无需再次查询匹配
                return null;
            }
        }
        //2.判断
        if(paymentList.size()<=0){
            //2.1掉接口查询
            //银联HIS数据查询
            List<PaymentCheckVo> hisPaymenList = thirdPartyMapper.getHisTripartite(paramMap);
            //银联对账信息查询
            List<PartyPaymentVo> unionPayList = getUnionPayList(paramMap);
            String datePay = MapUtils.getString(paramMap,"datePay");
            Date date = DateUtils.strToDate(datePay, "yyyyMMdd");
            String datePays = DateUtils.addDate(date, 1, 3, "yyyyMMdd");
            paramMap.put("datePay",datePays);
            //unionPayList.addAll(getUnionPayList(paramMap));
            paramMap.put("datePay",datePay);
            //银联与HIS数据匹配合并并保存缓存中
            Map<String, List<PaymentCheckVo>> matchingMap = matchingMerge(hisPaymenList, unionPayList, paramMap);
            //获取指定数据，HIS单边、第三方单边、匹配数据
            paymentList = matchingMap.get(MapUtils.getString(paramMap,"matchingType"));
        }
        //3.获取指定数据分页响应
        return unionPayRedisDataRetrieval(paymentList,paramMap);
    }

    /**
     * 杉德
     * zsrm-支付对账杉德相关数据业务处理
     * @param paramMap
     * @return
     */
    public PaymentCheckListVo sandeDataHandle(Map<String, Object> paramMap){
        List<PaymentCheckVo> paymentList = new ArrayList<>();
        //1.从redis获取数据
        //1.1组装redis的Key
        StringBuffer rdeisKeyName = new StringBuffer("");
        rdeisKeyName.append(String.format("ChecksPayment:%s", MapUtils.getString(paramMap,"euOrdertype")));
        rdeisKeyName.append(String.format(":%s", MapUtils.getString(paramMap,"datePay")));
        Object redisParam= RedisUtils.getCacheHashObj(rdeisKeyName.toString(),MapUtils.getString(paramMap,"matchingType"));
        if(null != redisParam){
            paymentList = JsonUtil.readValue(redisParam.toString(), new TypeReference<List<PaymentCheckVo>>(){});;
        }else{
            //判断redis是否存在当前名称
            Set<String> keyss = RedisUtils.getRedisTemplate().keys("his:"+rdeisKeyName.toString());
            if(keyss.size()>0){
                //存在证明数据已匹配,无需再次查询匹配
                return null;
            }
        }
        //2.判断
        if(paymentList.size()<=0){
            //2.1掉接口查询
            //杉德HIS数据查询
            List<PaymentCheckVo> hisPaymenList = thirdPartyMapper.getHisTripartite(paramMap);
            //杉德对账信息查询
            List<PartyPaymentVo> unionPayList = getSandeList(paramMap);
            //杉德与HIS数据匹配合并并保存缓存中
            Map<String, List<PaymentCheckVo>> matchingMap = matchingMerge(hisPaymenList, unionPayList,paramMap);
            //获取指定数据，HIS单边、第三方单边、匹配数据
            paymentList = matchingMap.get(MapUtils.getString(paramMap,"matchingType"));
        }
        //3.获取指定数据分页响应
        return unionPayRedisDataRetrieval(paymentList,paramMap);
    }

    /**
     * 中山人医-银联支付对账查询
     * @param paramMap
     * @return
     */
    public List<PartyPaymentVo> getUnionPayList(Map<String, Object> paramMap){
        //创建银联对账查询条件
        UnionPayRequestVo requestVo = new UnionPayRequestVo();
        requestVo.setGroupId(ApplicationUtils.getPropertyValue("msg.zsrm.UnionPay.groupId", "COMM00000002666"));
        requestVo.setSettDate(MapUtils.getString(paramMap,"datePay"));
        //requestVo.setPageSize(CommonUtils.getInteger(MapUtils.getString(paramMap,"pageSize")));
        requestVo.setPageSize("-1");
        requestVo.setPageIndex(MapUtils.getString(paramMap,"pageIndex"));
        requestVo.setWithTotalCount("Y");
        requestVo.setDateMode("00");
        requestVo.setSettTermNo(MapUtils.getString(paramMap,"txtSysname"));//终端编号
        requestVo.setRefNo(MapUtils.getString(paramMap,"orderNo"));//检索参考号

        //组织sign参数
        StringBuffer signSbf = new StringBuffer("");
        signSbf.append(String.format("app_id=%s", ApplicationUtils.getPropertyValue("msg.zsrm.UnionPay.appId", "5ff6767484b3aa9f459530714551777b")));
        signSbf.append(String.format("&timestamp=%s", DateUtils.getDateTime()));
        signSbf.append(String.format("&v=%s", "1.0.1"));
        signSbf.append(String.format("&sign_alg=%s", "1"));
        signSbf.append(String.format("&method=%s", "gnete.msc.itfs.biz.ReconManager.getDetails"));
        signSbf.append(String.format("&biz_content=%s", JsonUtil.writeValueAsString(requestVo)));

        //获取证书地址
        String Path = ApplicationUtils.getPropertyValue("msg.zsrm.UnionPay.filePath", "F:\\A0125810_I-0104.p12");
        //获取密码
        String passw = ApplicationUtils.getPropertyValue("msg.zsrm.UnionPay.passw", "zsrm$123.");
        try{
            PrivateKey privateKey = RSAUtil.ReadP12Private(Path,passw);
            signSbf.append(String.format("&sign=%s", RSAUtil.signToString(privateKey,signSbf.toString())));
        }catch (Exception e){
            throw new BusException(String.format("银联p12签名：%s", e.getMessage()));
        }
        String resJson = HttpClientUtil.sendHttpPostJson(ApplicationUtils.getPropertyValue("msg.zsrm.UnionPay.url", "https://api.gnete.com/routejson"), signSbf.toString());
        List<PartyPaymentVo> UnionPayList = new ArrayList<>();
        if(StringUtils.isNotEmpty(resJson)){
            JSONObject jsonObject = JSONObject.parseObject(resJson);
            if ("00000".equals(jsonObject.getString("code"))) {
                if("00".equals(jsonObject.getJSONObject("response").getString("returnCode"))){
                    UnionPayList = JsonUtil.readValue(jsonObject.getJSONObject("response").getString("detailList"), new TypeReference<List<PartyPaymentVo>>() {});
                }else{
                    throw new BusException(String.format("银联对账接口响应：%s", jsonObject.getJSONObject("response").getString("returnMessage")));
                }
            }else{
                throw new BusException(String.format("银联对账接口响应：%s", jsonObject.getString("msg")));
            }
        }
        return UnionPayList;
    }

    /**
     * 中山人医-杉德支付对账查询
     * @param paramMap
     * @return
     */
    public List<PartyPaymentVo> getSandeList(Map<String, Object> paramMap){
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> bizmap  = new HashMap<String, String>();
        String datePay =DateUtils.dateToStr("yyyy-MM-dd",DateUtils.strToDate(MapUtils.getString(paramMap,"datePay"), "yyyyMMdd"));
        bizmap .put("userName", "sandTest");
        bizmap .put("txnDate", datePay);
        //bizmap .put("acountDate", "2020-01-05");
        //bizmap .put("resType", "1");//服务端响应返回形式(默认0：json   1：文本),后期端口升级客户端做兼容
        map.put("bizcontent", JsonUtil.writeValueAsString(bizmap));
        map.put("sign_type", "RSA2");
        map.put("nonce", String.valueOf(System.currentTimeMillis()));
        map.put("app_id", "202012221645561");
        String content = RSAUtil.getSignCheckContentV2(map);
        try{
            String contents = ArchFileUtils.writeInFiles("F:\\test.txt");
            map.put("sign", RSAUtil.signUtfToBase64(RSAUtil.getPrivateKey(contents), content));
        }catch (Exception e){
            throw new BusException(String.format("杉德签名：%s", e.getMessage()));
        }

        String resJson = HttpClientUtil.sendHttpPostJson("http://testbillapi.ebankpos.com.cn:8691/InterfaceSys/sandTxnQuery", JsonUtil.writeValueAsString(map));
        List<PartyPaymentVo> UnionPayList = new ArrayList<>();
        if(StringUtils.isNotEmpty(resJson)){
            JSONObject jsonObject = JSONObject.parseObject(resJson);
            if ("200".equals(jsonObject.getString("code"))) {
                UnionPayList = JsonUtil.readValue(jsonObject.getJSONObject("data").getString("transDetailList"), new TypeReference<List<PartyPaymentVo>>() {});
            }else{
                throw new BusException(String.format("杉德对账接口响应：%s", jsonObject.getString("msg")));
            }
        }
        return UnionPayList;
    }

    /**
     * 一代POS
     * zsrm-支付对账一代POS相关数据业务处理
     * @param paramMap
     * @return
     */
    public PaymentCheckListVo generationPOSDataHandle(Map<String, Object> paramMap){
        PaymentCheckListVo payment = new PaymentCheckListVo();
        // 分页操作
        int pageIndex = CommonUtils.getInteger(MapUtils.getString(paramMap,"pageIndex"));
        int pageSize = CommonUtils.getInteger(MapUtils.getString(paramMap,"pageSize"));
        MyBatisPage.startPage(pageIndex,pageSize);
        payment.setPaymentList(thirdPartyMapper.getHisTripartite(paramMap));
        Page<List<MedicalCharges>> page = MyBatisPage.getPage();
        payment.setTotalCount(page.getTotalCount());
        return payment;
    }

    /**查询汇总交易信息**/
    public PaymentCheckListVo summaryHandle(Map<String, Object> paramMap){
        Map<String, Object> oneInsuMap = thirdPartyMapper.queryOneInsu(paramMap);
        Map<String, Object> threInsuMap = thirdPartyMapper.queryThreInsu(paramMap);
        Map<String, Object> totalAmtMap = thirdPartyMapper.querytotalAmt(paramMap);

        PaymentCheckListVo payment = new PaymentCheckListVo();
        payment.setOneInsuSum(MapUtils.getString(oneInsuMap,"toles",""));
        payment.setOneInsuAmt(MapUtils.getString(oneInsuMap,"amount",""));
        payment.setThreInsuSum(MapUtils.getString(threInsuMap,"toles",""));
        payment.setThreInsuAmt(MapUtils.getString(threInsuMap,"amount",""));
        payment.setTotalSum(MapUtils.getString(totalAmtMap,"toles",""));
        payment.setTotalAmt(MapUtils.getString(totalAmtMap,"amount",""));

        return payment;
    }
    /**
     * 数据集合匹配
     * @param hisPayList
     * @param ThirdList
     * @param paramMap
     * @return
     */
    public Map<String, List<PaymentCheckVo>> matchingMerge(List<PaymentCheckVo> hisPayList, List<PartyPaymentVo> ThirdList, Map<String, Object> paramMap){
        List<PaymentCheckVo> hisLs = hisPayList;
        List<PaymentCheckVo> hisThreeList = new ArrayList<>();
        List<PaymentCheckVo> threeList = new ArrayList<>();
        List<PartyPaymentVo> threePayList = new ArrayList<>();
        List<PartyPaymentVo> threePayLists = new ArrayList<>();

        ThirdList.remove(null);
        //1.1数据匹配
        for(PartyPaymentVo Third:ThirdList){
            boolean noExist = true;
            for(int i=0;i< hisLs.size();i++){
                //订单号、交易流水号作为条件进行匹配
                //备注：第一不使用订单号和金额做匹配是防止金额不一致问题
                if(Third.getTraceNo().equals(hisLs.get(i).getTradeNo()) && Third.getRefNo().equals(hisLs.get(i).getOrderNo())){
                    noExist = false;
                    hisLs.get(i).setPartyPay(Third);
                    hisThreeList.add(hisLs.get(i));
                    hisLs.remove(i);
                    break;
                }
            }
            if(noExist){
                //未匹配的数据因为流水号不一致进行二次匹配
                threePayList.add(Third);
            }
        }
        //1.2数据二次匹配
        for(PartyPaymentVo Thirds:threePayList){
            boolean noExist = true;
            for(int i=0;i< hisLs.size();i++){
                //金额、订单号匹配
                //银行卡、三代社保卡支付流水号不一致
                if(Thirds.getPayAmount().equals(hisLs.get(i).getAmount()) && Thirds.getRefNo().equals(hisLs.get(i).getOrderNo())){
                    noExist = false;
                    hisLs.get(i).setPartyPay(Thirds);
                    hisThreeList.add(hisLs.get(i));
                    hisLs.remove(i);
                    break;
                }
            }
            if(noExist){
                threePayLists.add(Thirds);
            }
        }
        //1.3数据三次匹配
        for(PartyPaymentVo Thirds:threePayLists){
            boolean noExist = true;
            for(int i=0;i< hisLs.size();i++){
                //订单号匹配
                if(Thirds.getRefNo().equals(hisLs.get(i).getOrderNo())){
                    noExist = false;
                    hisLs.get(i).setPartyPay(Thirds);
                    hisThreeList.add(hisLs.get(i));
                    hisLs.remove(i);
                    break;
                }
            }
            if(noExist){
                PaymentCheckVo check = new PaymentCheckVo();
                check.setPartyPay(Thirds);
                threeList.add(check);
            }
        }

        //2.保存缓存中
        StringBuffer rdeisKeyName = new StringBuffer("");
        rdeisKeyName.append(String.format("ChecksPayment:%s", MapUtils.getString(paramMap,"euOrdertype")));
        rdeisKeyName.append(String.format(":%s", MapUtils.getString(paramMap,"datePay")));

        if(hisLs.size()>0){//HIS单边保存缓存中
            RedisUtils.setCacheHashObj(rdeisKeyName.toString(),"HisList", hisLs);
        }
        if(threeList.size()>0){//第三方单边保存缓存
            RedisUtils.setCacheHashObj(rdeisKeyName.toString(),"ThreeList",threeList);
        }
        if(hisThreeList.size()>0){//匹配数据保存缓存
            RedisUtils.setCacheHashObj(rdeisKeyName.toString(),"HisThreeList",hisThreeList);
        }
        List<PaymentCheckVo> AllList = hisThreeList;
        AllList.addAll(hisLs);
        AllList.addAll(threeList);
        RedisUtils.setCacheHashObj(rdeisKeyName.toString(),"AllLis",AllList);//所有数据保存缓存中

        //组装数据响应
        Map<String, List<PaymentCheckVo>> matchingMap = new HashMap<>();
        matchingMap.put("HisThreeList",hisThreeList);
        matchingMap.put("HisList",hisLs);
        matchingMap.put("ThreeList",threeList);
        return matchingMap;
    }
    /**
     * 数据集合匹配
     * @param hisPayList
     * @param ThirdList
     * @param paramMap
     * @return
     */
    public Map<String, List<PaymentCheckVo>> matchingMerges(List<PaymentCheckVo> hisPayList, List<PayRecordResponseVo> ThirdList, Map<String, Object> paramMap){
        List<PaymentCheckVo> hisLs = hisPayList;
        List<PaymentCheckVo> hisThreeList = new ArrayList<>();
        List<PaymentCheckVo> threeList = new ArrayList<>();
        List<PayRecordResponseVo> threePayList = new ArrayList<>();
        List<PayRecordResponseVo> threePayLists = new ArrayList<>();

        ThirdList.remove(null);
        //1.1数据匹配
        for(PayRecordResponseVo Third:ThirdList){
            boolean noExist = true;
            for(int i=0;i< hisLs.size();i++){
                //订单号、交易流水号作为条件进行匹配
                //备注：第一不使用订单号和金额做匹配是防止金额不一致问题
                if(Third.getTradeNo().equals(hisLs.get(i).getTradeNo()) && Third.getMerOrderNo().equals(hisLs.get(i).getOrderNo())){
                    noExist = false;
                    hisLs.get(i).setPayRecordResponseVo(Third);
                    hisThreeList.add(hisLs.get(i));
                    hisLs.remove(i);
                    break;
                }
            }
            if(noExist){
                //未匹配的数据因为流水号不一致进行二次匹配
                threePayList.add(Third);
            }
        }
        //1.2数据二次匹配
        for(PayRecordResponseVo Thirds:threePayList){
            boolean noExist = true;
            for(int i=0;i< hisLs.size();i++){
                //金额、订单号匹配
                //银行卡、三代社保卡支付流水号不一致
                if(Thirds.getPayAmount().equals(hisLs.get(i).getAmount()) && Thirds.getMerOrderNo().equals(hisLs.get(i).getOrderNo())){
                    noExist = false;
                    hisLs.get(i).setPayRecordResponseVo(Thirds);
                    hisThreeList.add(hisLs.get(i));
                    hisLs.remove(i);
                    break;
                }
            }
            if(noExist){
                threePayLists.add(Thirds);
            }
        }
        //1.3数据三次匹配
        for(PayRecordResponseVo Thirds:threePayLists){
            boolean noExist = true;
            for(int i=0;i< hisLs.size();i++){
                //订单号匹配
                if(Thirds.getMerOrderNo().equals(hisLs.get(i).getOrderNo())){
                    noExist = false;
                    hisLs.get(i).setPayRecordResponseVo(Thirds);
                    hisThreeList.add(hisLs.get(i));
                    hisLs.remove(i);
                    break;
                }
            }
            if(noExist){
                PaymentCheckVo check = new PaymentCheckVo();
                check.setPayRecordResponseVo(Thirds);
                threeList.add(check);
            }
        }

        //2.保存缓存中
        StringBuffer rdeisKeyName = new StringBuffer("");
        rdeisKeyName.append(String.format("ChecksPayment:%s", MapUtils.getString(paramMap,"euOrdertype")));
        rdeisKeyName.append(String.format(":%s", MapUtils.getString(paramMap,"datePay")));

        if(hisLs.size()>0){//HIS单边保存缓存中
            RedisUtils.setCacheHashObj(rdeisKeyName.toString(),"HisList", hisLs);
        }
        if(threeList.size()>0){//第三方单边保存缓存
            RedisUtils.setCacheHashObj(rdeisKeyName.toString(),"ThreeList",threeList);
        }
        if(hisThreeList.size()>0){//匹配数据保存缓存
            RedisUtils.setCacheHashObj(rdeisKeyName.toString(),"HisThreeList",hisThreeList);
        }
        List<PaymentCheckVo> AllList = hisThreeList;
        AllList.addAll(hisLs);
        AllList.addAll(threeList);
        RedisUtils.setCacheHashObj(rdeisKeyName.toString(),"AllLis",AllList);//所有数据保存缓存中

        //组装数据响应
        Map<String, List<PaymentCheckVo>> matchingMap = new HashMap<>();
        matchingMap.put("HisThreeList",hisThreeList);
        matchingMap.put("HisList",hisLs);
        matchingMap.put("ThreeList",threeList);
        return matchingMap;
    }

    /**
     * 检索后数据进行分页
     * @param redisList
     * @param paramMap
     * @return
     */
    public PaymentCheckListVo unionPayRedisDataRetrieval(List<PaymentCheckVo> redisList,Map<String, Object> paramMap){
        String codeOp = MapUtils.getString(paramMap,"codeOp");//门诊号
        String payType = MapUtils.getString(paramMap,"payType");//支付方式
        String dtBank = MapUtils.getString(paramMap,"dtBank");//扫码支付方式
        String orderNo = MapUtils.getString(paramMap,"orderNo");//订单号
        String txtSysname = MapUtils.getString(paramMap,"txtSysname");//终端号
        String tradeNo = MapUtils.getString(paramMap,"tradeNo");//交易流水号
        String nameEmp = MapUtils.getString(paramMap,"txtNameEmp");//操作员姓名

        List<PaymentCheckVo> returnList = redisList;

        //使用lambda表达式对list数据检索
        //门诊号(id号)
        if(StringUtils.isNotEmpty(codeOp)){
            returnList = returnList.stream().filter(item->BeanUtils.isNotNull(item.getCodeOp()) && item.getCodeOp().equals(codeOp)).collect(Collectors.toList());
        }
        //支付方式
        if(StringUtils.isNotEmpty(payType)){
            returnList = returnList.stream().filter(item->BeanUtils.isNotNull(item.getPayType()) && item.getPayType().equals(payType)).collect(Collectors.toList());
        }
        //扫码支付方式
        if(StringUtils.isNotEmpty(dtBank)){
            returnList = returnList.stream().filter(item->BeanUtils.isNotNull(item.getDtBank()) && item.getDtBank().equals(dtBank)).collect(Collectors.toList());
        }
        //orderNo 订单号
        if(StringUtils.isNotEmpty(orderNo)){
            returnList = returnList.stream().filter(item->(BeanUtils.isNotNull(item.getOrderNo()) && item.getOrderNo().equals(orderNo)) || (BeanUtils.isNotNull(item.getPartyPay()) && item.getPartyPay().getRefNo().equals(orderNo)) ).collect(Collectors.toList());
        }
        //txtSysname 终端号
        if(StringUtils.isNotEmpty(txtSysname)){
            returnList = returnList.stream().filter(item->(BeanUtils.isNotNull(item.getSysname()) && item.getSysname().equals(txtSysname)) || (BeanUtils.isNotNull(item.getPartyPay()) && item.getPartyPay().getSettTermNo().equals(txtSysname)) ).collect(Collectors.toList());
        }
        //tradeNo 交易流水号
        if(StringUtils.isNotEmpty(tradeNo)){
            returnList = returnList.stream().filter(item->(BeanUtils.isNotNull(item.getTradeNo()) && item.getTradeNo().equals(tradeNo)) || (BeanUtils.isNotNull(item.getPartyPay()) && item.getPartyPay().getTraceNo().equals(tradeNo)) ).collect(Collectors.toList());
        }
        //操作员姓名
        if(StringUtils.isNotEmpty(nameEmp)){
            returnList = returnList.stream().filter(item->BeanUtils.isNotNull(item.getNameEmp()) && item.getNameEmp().equals(nameEmp)).collect(Collectors.toList());
        }
        //操作员姓名
        if(StringUtils.isNotEmpty(nameEmp)){
            returnList = returnList.stream().filter(item->BeanUtils.isNotNull(item.getNameEmp()) && item.getNameEmp().equals(nameEmp)).collect(Collectors.toList());
        }
        if(StringUtils.isNotEmpty(MapUtils.getString(paramMap,"varyPay"))){
            returnList = returnList.stream().filter(item-> item.getAmount().equals(item.getPartyPay().getTrxAmount())).collect(Collectors.toList());
        }

        //调用List分页
        return pagingHandleDate(returnList,paramMap);
    }

    /**
     * 分页List集合
     * @param paymentList
     * @param paramMap
     * @return
     */
    public PaymentCheckListVo pagingHandleDate(List<PaymentCheckVo> paymentList, Map<String, Object> paramMap){
        if(paymentList.size()<=0){
            return null;
        }            
        Integer pageNum = CommonUtils.getInteger(MapUtils.getString(paramMap,"pageIndex"));
        Integer pageSize = CommonUtils.getInteger(MapUtils.getString(paramMap,"pageSize"));
        PaymentCheckListVo payment = new PaymentCheckListVo();
        if(paramMap.get("euOrdertype").equals("Seabird")){
            String pageSizes=ApplicationUtils.getPropertyValue("seabird.pageSizes", "");
            String pageIndexs=ApplicationUtils.getPropertyValue("seabird.pageIndexs", "");
            payment.setPaymentList(CommonUtils.startPage(paymentList,Integer.parseInt(pageIndexs),Integer.parseInt(pageSizes)));
        }else {
            payment.setPaymentList(CommonUtils.startPage(paymentList, pageNum, pageSize));
        }
        payment.setTotalCount(paymentList.size());
        return payment;
    }

    /**
     * @param 
     * @return
     */
    public PaymentCheckListVo seabirdDataHandle(Map<String, Object> paramMap){
    	List<PaymentCheckVo> paymentList = new ArrayList<>();
    	
    	//HIS支付信息
        List<PaymentCheckVo> hisPaymenList = thirdPartyMapper.getHisTripartite(paramMap);
        //银联对账信息查询
        List<PartyPaymentVo> unionPayList = getSeabirdPayList(paramMap);
        String datePay = MapUtils.getString(paramMap,"datePay");
        Date date = DateUtils.strToDate(datePay, "yyyyMMdd");
        String datePays = DateUtils.addDate(date, 1, 3, "yyyyMMdd");

        //订单匹配
        Map<String, List<PaymentCheckVo>> matchingMap = matchingMerge(hisPaymenList, unionPayList, paramMap);
        //获取指定数据，HIS单边、第三方单边、匹配数据
        paymentList = matchingMap.get(MapUtils.getString(paramMap,"matchingType"));
        
        return unionPayRedisDataRetrieval(paymentList,paramMap);
        
    }
    /**
     *
     *支付平台交易记录
     */
     public PaymentCheckListVo getQualityPayRecord(Map<String, Object> paramMap){
         List<PaymentCheckVo> paymentList = new ArrayList<>();
         //HIS支付信息
         List<PaymentCheckVo> hisPaymenList = thirdPartyMapper.getHisTripartite(paramMap);
         //请求支付平台
         List<PayRecordResponseVo> PayRecordList = getPayRecord(paramMap);
         //订单匹配
         Map<String, List<PaymentCheckVo>> matchingMap = matchingMerges(hisPaymenList, PayRecordList, paramMap);
         //获取指定数据，HIS单边、第三方单边、匹配数据
         paymentList = matchingMap.get(MapUtils.getString(paramMap,"matchingType"));

         return unionPayRedisDataRetrieval(paymentList,paramMap);

     }
     /**
      * 请求支付平台
      */
     public List<PayRecordResponseVo> getPayRecord(Map<String, Object> paramMap){
         //获取url
         String url=ApplicationUtils.getPropertyValue("quality.pay.getPayRecord.url", "");
         //初始化参数
         PayRecordParamVo payRecordParamVo=new PayRecordParamVo();
         if(!StringUtils.isBlank((String) paramMap.get("outTradeNo"))){
             payRecordParamVo.setOutTradeNo((String) paramMap.get("outTradeNo"));
         }
         if(!StringUtils.isBlank((String)paramMap.get("payMode"))){
             payRecordParamVo.setPayMode((String)paramMap.get("payMode"));
         }
         if(!StringUtils.isBlank((String)paramMap.get("datePay"))){
             payRecordParamVo.setTimeStamp((String)paramMap.get("datePay"));
         }
         if(!StringUtils.isBlank((String)paramMap.get("tradeNo"))){
             payRecordParamVo.setTradeNo((String)paramMap.get("tradeNo"));
         }
         //调用支付平台发送请求获取响应数据
         String result = HttpClientUtil.sendHttpPostJson(
                 url,
                 JSONObject.toJSONString(payRecordParamVo));
         Map<String,Object> map = JSONObject.parseObject(result, Map.class);
         if (map==null){
             throw  new BusException("请求异常，返回null");
         }
         if(map.get("state").toString().equals("error")){
             throw  new BusException(map.get("msg").toString());
         }
         //请求成功 获取数据并返回
         List<PayRecordResponseVo> payRecordResponseVoList= (List<PayRecordResponseVo>) map.get("data");
         return payRecordResponseVoList;
     }

    /**
     *获取海鹚支付账单
     * @return
     */
    public List<PartyPaymentVo> getSeabirdPayList(Map<String, Object> paramMap){ 
    	
        String seabirdUrl=ApplicationUtils.getPropertyValue("seabird.seabirdUrl", "");
        String partnerId=ApplicationUtils.getPropertyValue("seabird.partnerId", "");
        String password=ApplicationUtils.getPropertyValue("seabird.password", "");
        String pageSizes=ApplicationUtils.getPropertyValue("seabird.pageSizes", "");
        String pageIndexs=ApplicationUtils.getPropertyValue("seabird.pageIndexs", "");

        UnionPayRequestVo req = new UnionPayRequestVo();
        req.setServiceCode("QueryOrderInfo");
        req.setPartnerId(partnerId);
        req.setOrderDate(paramMap.get("datePay").toString());
        req.setTimeStamp(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        req.setPageNo(pageIndexs);
        req.setPageNumber(pageSizes);

        //组织sign参数
        StringBuffer signSbf = new StringBuffer("");
        signSbf.append(String.format("orderDate=%s", req.getOrderDate()));
        signSbf.append(String.format("&pageNo=%s", req.getPageNo()));
        signSbf.append(String.format("&pageNumber=%s", req.getPageNumber()));
        signSbf.append(String.format("&partnerId=%s", req.getPartnerId()));
        signSbf.append(String.format("&serviceCode=%s", req.getServiceCode()));
        signSbf.append(String.format("&timeStamp=%s", req.getTimeStamp()));
        signSbf.append("&"+password);//密码 配置
        String signMd5 = new SimpleHash("md5",signSbf.toString()).toHex().toUpperCase();
        req.setPassword(signMd5);

        //System.out.println(XmlUtil.beanToXml(req, req.getClass(), false));
        String resXml = HttpClientUtil.sendHttpPostXml(seabirdUrl, XmlUtil.beanToXml(req, req.getClass(), false));
        //System.out.println(resXml);

        SeabirdRetVo retVo = (SeabirdRetVo)XmlUtil.XmlToBean(resXml,SeabirdRetVo.class );
        List<PartyPaymentVo> orderPayList=new ArrayList<>();
        if(retVo.getList()==null){
            throw new BusException(retVo.getResultDesc());
        }
         
		for (SeabirdOrder seabirdOrder : retVo.getList()) {
			PartyPaymentVo PartyPaymentVo = new PartyPaymentVo();
			
			PartyPaymentVo.setTrxAmount(seabirdOrder.getPayAmt()/100);
			PartyPaymentVo.setRefNo(seabirdOrder.getAgtOrdNum());//唯一订单号
			PartyPaymentVo.setTrxTime(seabirdOrder.getPayTime());//交易时间
			PartyPaymentVo.setOrderType(seabirdOrder.getOrderType());
			PartyPaymentVo.setPayMode(seabirdOrder.getPayMode());
			PartyPaymentVo.setPatName(seabirdOrder.getPatName());

			PartyPaymentVo.setPayAmount(seabirdOrder.getPayAmt());
			PartyPaymentVo.setAmount(seabirdOrder.getPayAmt());
			PartyPaymentVo.setTradeNo(seabirdOrder.getAgtOrdNum());
			PartyPaymentVo.setTraceNo(seabirdOrder.getPsOrdNum());
			for(int i=0;i<retVo.getList().size();i++){
                if(seabirdOrder.getAgtOrdNum().equals(retVo.getList().get(i).getOldAgtOrdNum())){
                    PartyPaymentVo.setOrderStatus("已退费");
                    break;
                }
            }
			orderPayList.add(PartyPaymentVo);
		}
		return orderPayList;
   }
    
    /**022006006032
     * 海鹚退款接口
     * @param param
     * @param user
     */
    public SeabirdCancelResponse refundSeabirdOrder(String param, IUser user){

    	Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
    	//查询订单信息
        BlSettle blsettle = new BlSettle();
        BlExtPay blextpay = new BlExtPay();
        if(!CommonUtils.isEmptyString(paramMap.get("codeSt").toString())){
            blsettle = DataBaseHelper.queryForBean("select * from bl_settle where code_st = ?", BlSettle.class, paramMap.get("codeSt"));
            paramMap.put("pkSettle",blsettle.getPkSettle());
            blextpay = thirdPartyMapper.queryThirdOrder(paramMap);
            if (blextpay == null) {
                throw new BusException("未查询到有效的订单信息");
            }
        }else if (!CommonUtils.isEmptyString(paramMap.get("serialNo").toString())){
            blextpay = thirdPartyMapper.queryThirdOrders(paramMap);
            if (blextpay == null) {
                throw new BusException("未查询到有效的订单信息");
            }
            if(paramMap.get("amountSt") != "0.00" && CommonUtils.isEmptyString(paramMap.get("amountSt").toString())){
                if(blextpay.getAmount().equals(paramMap.get("amountSt"))){
                    throw new BusException("金额不一致，请检查金额是否正确");
                }
            }
            blsettle = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle = ?", BlSettle.class, blextpay.getPkSettle());
        }else {
            blextpay = thirdPartyMapper.queryThirdOrder(paramMap);
            if (blextpay == null) {
                throw new BusException("未查询到有效的订单信息");
            }
            blsettle = DataBaseHelper.queryForBean("select * from bl_settle where pk_settle = ?", BlSettle.class, blextpay.getPkSettle());
        }
        String seabirdUrl=ApplicationUtils.getPropertyValue("seabird.seabirdUrl", "");
        String partnerId=ApplicationUtils.getPropertyValue("seabird.partnerId", "");
        String password=ApplicationUtils.getPropertyValue("seabird.password", "");    
        
    	SeabirdCancelRequest req = new SeabirdCancelRequest();
    	req.setServiceCode("RefundNotice");
    	req.setPartnerId(partnerId);
    	req.setTimeStamp(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
    	req.setPassword(password);
    	req.setHisRefundOrdNum(blextpay.getTradeNo());
    	req.setAgtOrdNum(blextpay.getSerialNo());
    	if(paramMap.get("amountSt") != "0.00" && CommonUtils.isEmptyString(paramMap.get("amountSt").toString())) {
            BigDecimal amountst = (BigDecimal) paramMap.get("amountSt");
    	    req.setRefundAmout(amountst.multiply(new BigDecimal(100)).intValue());
        }else{
            req.setRefundAmout(blsettle.getAmountSt().multiply(new BigDecimal(100)).intValue());
        }
    	req.setRefundDesc("门诊退费");
    	req.setCheckOutNo(blextpay.getPkSettle());

        //组织sign参数
        StringBuffer signSbf = new StringBuffer("");
        signSbf.append(String.format("agtOrdNum=%s", req.getAgtOrdNum()));
        signSbf.append(String.format("&checkOutNo=%s", req.getCheckOutNo()));
        signSbf.append(String.format("&hisRefundOrdNum=%s", req.getHisRefundOrdNum()));
        signSbf.append(String.format("&partnerId=%s", req.getPartnerId()));
        signSbf.append(String.format("&refundAmout=%s", req.getRefundAmout()));
        signSbf.append(String.format("&refundDesc=%s", req.getRefundDesc()));
        signSbf.append(String.format("&serviceCode=%s", req.getServiceCode()));
        signSbf.append(String.format("&timeStamp=%s", req.getTimeStamp()));

        signSbf.append("&"+password);//密码 配置
        String signMd5 = new SimpleHash("md5",signSbf.toString()).toHex().toUpperCase();
        req.setPassword(signMd5);

        System.out.println(XmlUtil.beanToXml(req, req.getClass(), false));
        String resXml = HttpClientUtil.sendHttpPostXml(seabirdUrl,XmlUtil.beanToXml(req, req.getClass(),false));
        System.out.println(resXml);

        SeabirdCancelResponse retVo = (SeabirdCancelResponse)XmlUtil.XmlToBean(resXml,SeabirdCancelResponse.class );
        if(retVo != null && "0".equals(retVo.getRefundStatus())){
            return retVo;
        }else{
        	throw new BusException("线上(微信)退款失败："+retVo.getResultDesc());
        }
    	
    }
	/**
	 * 022006006033
	 * @param param
	 * @param user
	 * @return
	 */
	public Integer  checkCancelAllSettle(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null){
			throw new BusException("未传入有效参数信息！");
		}

        String pkSettle=paramMap.get("pkSettle").toString();
        String sql="select count(1) from bl_ext_pay where pk_settle=? and sysname='MZWX' and flag_pay = '1'";
        Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pkSettle});
		return count;
	}
    
}
