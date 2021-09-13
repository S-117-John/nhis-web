package com.zebone.nhis.pro.zsba.mz.bl.service;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.ArchFileUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.common.support.RSAUtil;
import com.zebone.nhis.common.module.compay.ins.qgyb.MedicalCharges;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.pro.zsba.mz.bl.dao.ZsBaThirdPartyMapper;
import com.zebone.nhis.pro.zsba.mz.bl.vo.PartyPaymentVo;
import com.zebone.nhis.pro.zsba.mz.bl.vo.PaymentCheckListVo;
import com.zebone.nhis.pro.zsba.mz.bl.vo.PaymentCheckVo;
import com.zebone.nhis.pro.zsba.mz.bl.vo.UnionPayRequestVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.modules.utils.RedisUtils;

@Service
public class ZsBaThirdPartyService {
    @Resource
    private ZsBaThirdPartyMapper thirdPartyMapper;

    /**
     * 交易号： 022006006001->022003027007
     * 第三方支付信息查询
     * @return
     */
    public PaymentCheckListVo qryHisTripartitePayment(String param, IUser user){
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        //判断厂商类型
        if("UnionPay".equals(MapUtils.getString(paramMap,"euOrdertype"))){
            //调用银联厂商对账接口获取数据
            return UnionPayDataHandle(paramMap);
        }else if("Sande".equals(MapUtils.getString(paramMap,"euOrdertype"))){
            //调用杉德厂商对账接口获取对账信息
            return SandeDataHandle(paramMap);
        } else if("GenerationPOS".equals(MapUtils.getString(paramMap,"euOrdertype"))){
            //调用一代POS获取对账信息
            return GenerationPOSDataHandle(paramMap);
        }
        return null;
    }

    /**
     * 交易号： 022006006013->022003027066
     * 支付费用查询
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
     * 交易号： 022006006012->022003027008
     * 支付对账缓存清理
     * @param param
     * @param user
     */
    public void zsbaPaymentClearCache(String param, IUser user){
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
     * zsba-支付对账银联相关数据业务处理
     * @param paramMap
     * @return
     */
    public PaymentCheckListVo UnionPayDataHandle(Map<String, Object> paramMap){
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
            unionPayList.addAll(getUnionPayList(paramMap));
            paramMap.put("datePay",datePay);
            //银联与HIS数据匹配合并并保存缓存中
            Map<String, List<PaymentCheckVo>> matchingMap = matchingMerge(hisPaymenList, unionPayList, paramMap);
            //获取指定数据，HIS单边、第三方单边、匹配数据
            paymentList = matchingMap.get(MapUtils.getString(paramMap,"matchingType"));
        }
        //3.获取指定数据分页响应
        return UnionPayRedisDataRetrieval(paymentList,paramMap);
    }

    /**
     * 杉德
     * zsba-支付对账杉德相关数据业务处理
     * @param paramMap
     * @return
     */
    public PaymentCheckListVo SandeDataHandle(Map<String, Object> paramMap){
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
        return UnionPayRedisDataRetrieval(paymentList,paramMap);
    }

    /**
     * 中山博爱-银联支付对账查询
     * @param paramMap
     * @return
     */
    public List<PartyPaymentVo> getUnionPayList(Map<String, Object> paramMap){
        //创建银联对账查询条件
        UnionPayRequestVo requestVo = new UnionPayRequestVo();
        requestVo.setGroupId(ApplicationUtils.getPropertyValue("msg.zsrm.UnionPay.groupId", "COMM00000002666"));
        requestVo.setSettDate(MapUtils.getString(paramMap,"datePay"));
        //requestVo.setPageSize(CommonUtils.getInteger(MapUtils.getString(paramMap,"pageSize")));
        requestVo.setPageSize(-1);
        requestVo.setPageIndex(CommonUtils.getInteger(MapUtils.getString(paramMap,"pageIndex")));
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
     * 中山博爱-杉德支付对账查询
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
     * zsba-支付对账一代POS相关数据业务处理
     * @param paramMap
     * @return
     */
    public PaymentCheckListVo GenerationPOSDataHandle(Map<String, Object> paramMap){
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
    public PaymentCheckListVo UnionPayRedisDataRetrieval(List<PaymentCheckVo> redisList,Map<String, Object> paramMap){
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
        payment.setPaymentList(CommonUtils.startPage(paymentList,pageNum,pageSize));
        payment.setTotalCount(paymentList.size());
        return payment;
    }

}
