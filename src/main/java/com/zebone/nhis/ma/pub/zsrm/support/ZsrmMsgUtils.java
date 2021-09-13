package com.zebone.nhis.ma.pub.zsrm.support;


import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.zsrm.vo.DrugQryVo;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class ZsrmMsgUtils {

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 对字符串进行MD5编码
     */
    public static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                // 创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                // 将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 轮换字节数组为十六进制字符串
     *
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 哪些药房？窗口发送给摆药机，不配置，不发送(配置形式：科室code:窗口号,科室code2:窗口号)，比如：<br>
     * c1:01,c1:02,c2:01
     * @return
     */
    public static List<DrugQryVo.WesDrugConfVo> getConfig() {
        String config = "1104010030200:6,1104010030200:7,1104010030200:8,1104010030200:9";
        List<DrugQryVo.WesDrugConfVo> confVoList = new ArrayList<>();
        if (StringUtils.isNotBlank(config)) {
            String strings[] = config.split(",");
            for (String str : strings) {
                if (StringUtils.isBlank(str)) {
                    continue;
                }
                String kv[] = str.split(":");
                if (kv != null && kv.length == 2) {
                    DrugQryVo.WesDrugConfVo confVo = new DrugQryVo.WesDrugConfVo();
                    confVo.setCodeDept(kv[0]);
                    confVo.setWino(kv[1]);
                    confVoList.add(confVo);
                }
            }
        }
        return confVoList;
    }
}
