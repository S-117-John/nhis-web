package com.zebone.nhis.ma.pub.sd.service;

import com.ciyun.rptgw.encrypt.CyCipher;
import com.ciyun.rptgw.encrypt.Encryption;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.sd.dao.FollowUpMapper;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.ma.pub.sd.vo.FollowUpAdviceDoctorInfoVo;
import com.zebone.nhis.ma.pub.sd.vo.FollowUpClinicAdviceItemVo;
import com.zebone.nhis.ma.pub.sd.vo.FollowUpPiInfoVo;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 随访系统上传全量上传门诊医嘱单
 */
@Service
public class FollowUpService {

    @Autowired
    private FollowUpMapper upMapper;

    private static String auth = ApplicationUtils.getPropertyValue("ext.auth", "");
    private static String med_id = ApplicationUtils.getPropertyValue("ext.med_id", "");
    private static String ciyun_id = ApplicationUtils.getPropertyValue("ext.ciyun_id", "");
    private static String url = ApplicationUtils.getPropertyValue("ext.system.address", "");
    private static String public_key = ApplicationUtils.getPropertyValue("ext.public_key", "");

    private Logger logger = LoggerFactory.getLogger("com.zebone");

    // 用来控制手动事物
    @Resource(name = "transactionManager")
    private PlatformTransactionManager platformTransactionManager;

    /**
     * 业务处理方法转换器
     *
     * @param methodName
     * @param args
     */
    public Object invokeMethod(String methodName, Object... args) {

        Object result = null;
        if (StringUtils.isEmpty(url)) return result;
        switch (methodName) {
            //上传全部医嘱
            case "uploadOrder":
                uploadOrder(args);
                break;
            default:
                break;
        }

        return result;
    }

    /**
     * 上传医嘱，诊毕时调用
     *
     * @param args
     */
    public void uploadOrder(Object[] args) {

        //修改为手动事物 , 关闭事务自动提交
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);


        try {
            String pkPv = "";
            if (args != null) {
                pkPv = (String) args[0];
            } else {
                return;
            }

            List<FollowUpPiInfoVo> pvInfos = upMapper.queryPvInfo(pkPv);//查询就诊信息
            if (pvInfos.size() <= 0) {
                return;
            }
            List<FollowUpClinicAdviceItemVo> ordInfos = upMapper.queryOrdInfo(pkPv);//查询医嘱信息
            List<Map<String, Object>> diagInfos = upMapper.queryDiagInfo(pkPv);//查询诊断信息
            List<FollowUpAdviceDoctorInfoVo> docs = upMapper.queryDocinfo(pkPv);//查询医生信息

            if (ordInfos.size() <= 0) return;

            pvInfos.get(0).setClinicAdviceItem(ordInfos);
            pvInfos.get(0).setAdviceDoctorInfo(docs);
            pvInfos.get(0).setDiagnosis(diagInfos);

            String body = JsonUtil.writeValueAsString(pvInfos);

            CyCipher cc = new CyCipher();
            Encryption encrpt = cc.encrpt(public_key, body, CyCipher.KeyEnum.publicKey);
            Map<String, Object> sendMsg = Maps.newHashMap();
            sendMsg.put("auth", auth);
            sendMsg.put("med_id", med_id);
            sendMsg.put("ciyun_id", ciyun_id);
            sendMsg.put("key", encrpt.getKey());
            sendMsg.put("body", encrpt.getEncryptContent());
            String s = HttpClientUtil.sendHttpPostJson(url, JsonUtil.writeValueAsString(sendMsg));
            logger.info("==============================================="+ body);
            logger.info("==============================================="+ s);
            //提交事务
            platformTransactionManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            //出现错误，事务回滚，记录日志
            platformTransactionManager.rollback(status);
            //消息返回错误信息
//            throw e;
        }

    }
}
