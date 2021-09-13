package com.zebone.nhis.ma.lb.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.gson.JsonObject;
import com.zebone.nhis.common.module.ma.sms.SmsSend;
import com.zebone.nhis.common.module.ma.sms.SmsSendResult;
import com.zebone.nhis.common.module.ma.sms.SmsTemp;
import com.zebone.nhis.common.module.ma.sms.SmsTempSt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.lb.dao.SmsMapper;
import com.zebone.nhis.ma.lb.vo.SmsTempVo;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SmsService {

    @Autowired
    private SmsMapper smsMapper;

    private final static Logger logger = LoggerFactory.getLogger(SmsService.class);

    /**
     * 查询短信模板
     *
     * @param param
     * @param user
     * @return
     */
    public List<SmsTempVo> qrySmsTemp(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<SmsTempVo> smsTempList = smsMapper.QrySmsTemp(paramMap);
        List<SmsTempSt> listSt = DataBaseHelper.queryForList("select PK_SMSTEMPST,PK_SMSTEMP,DT_TYPE,VAL from SMS_TEMP_ST", SmsTempSt.class);
        if (CollectionUtils.isNotEmpty(smsTempList) && CollectionUtils.isNotEmpty(listSt)) {
            ImmutableMap<String, Collection<SmsTempSt>> immutableMap = Multimaps.index(listSt, new Function<SmsTempSt, String>() {
                @Override
                public String apply(SmsTempSt smsTempSt) {
                    return smsTempSt.getPkSmstemp();
                }
            }).asMap();
            for (SmsTempVo vo : smsTempList) {
                Collection<SmsTempSt> collection = immutableMap.get(vo.getPkSmstemp());
                if (collection != null && collection.size() > 0) {
                    vo.setStList(new ArrayList<SmsTempSt>(collection));
                }
            }
        }
        return smsTempList;
    }


    /**
     * 删除短信模板
     *
     * @param param
     * @param user
     * @return
     */
    public void delSmsTemp(String param, IUser user) {
        SmsTemp smsTemp = JsonUtil.readValue(param, SmsTemp.class);
        if (smsTemp != null && "0".equals(smsTemp.getDelFlag())) {
            DataBaseHelper.update("update sms_temp set del_flag='1' where pk_smstemp=? and del_flag='0' ", new Object[]{smsTemp.getPkSmstemp()});
        } else if (smsTemp != null && "1".equals(smsTemp.getDelFlag())) {
            DataBaseHelper.update("update sms_temp set del_flag='0' where pk_smstemp=? and del_flag='1' ", new Object[]{smsTemp.getPkSmstemp()});
        }
    }

    /**
     * 保存短信模板
     *
     * @param param
     * @param user
     * @return
     */
    public void saveSmsTemp(String param, IUser user) {
        SmsTempVo smsTempVo = JsonUtil.readValue(param, SmsTempVo.class);
        if (smsTempVo != null) {
            if (StringUtils.isBlank(smsTempVo.getCodeTemp())) {
                throw new BusException("模板编码不能为空");
            }
            boolean isMod = StringUtils.isNotBlank(smsTempVo.getPkSmstemp());
            int count = DataBaseHelper.queryForScalar("select count(*) from sms_temp where code_temp=?" + (isMod ? " and pk_smstemp!=?" : "")
                    , Integer.class, (isMod ? new Object[]{smsTempVo.getCodeTemp(), smsTempVo.getPkSmstemp()} : new Object[]{smsTempVo.getCodeTemp()}));
            if (count > 0) {
                throw new BusException("模板编码不能重复");
            }
            SmsTemp smsTemp = new SmsTemp();
            BeanUtils.copyProperties(smsTempVo, smsTemp);
            if (!isMod) {
                DataBaseHelper.insertBean(smsTemp);
                smsTempVo.setPkSmstemp(smsTemp.getPkSmstemp());
                saveTempSt(smsTempVo);
                //新增时，立即生成一次待发送数据
                saveAndGetSmsSend(Lists.newArrayList(smsTemp));
                //TODO 我们的定时器每隔一段时间拿取固定时间段之内的数据，但若此时任务已经执行（数据已经加入列表）
                // 然后新建了一个此时间段内的模板，那这个就不会被发送...
            } else {
                saveTempSt(smsTempVo);
                DataBaseHelper.updateBeanByPk(smsTemp);
            }
        }
    }

    private void saveTempSt(SmsTempVo smsTempVo) {
        //保存setting信息
        List<SmsTempSt> stList;
        if (StringUtils.isNotBlank(smsTempVo.getPkSmstemp())) {
            DataBaseHelper.execute("delete from SMS_TEMP_ST where PK_SMSTEMP=?", new Object[]{smsTempVo.getPkSmstemp()});
        }
        if (CollectionUtils.isNotEmpty(stList = smsTempVo.getStList())) {
            for (SmsTempSt st : stList) {
                st.setPkSmstemp(smsTempVo.getPkSmstemp());
                ApplicationUtils.setDefaultValue(st, true);
            }
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SmsTempSt.class), stList);
        }
    }


    /**
     * 查询人员及手机号
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> searchMobile(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> MobileList = smsMapper.SearchMobile(paramMap);
        return MobileList;
    }

    /**
     * 查询患者及手机号
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> searchMaster(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> MasterList = smsMapper.SearchMaster(paramMap);
        return MasterList;
    }

    /**
     * 查询短信记录
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> searchSmsSend(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String, Object>> MasterList = smsMapper.SearchSmsSend(paramMap);
        return MasterList;
    }

    /**
     * 审核短信发送
     *
     * @param param
     * @param user
     * @return
     */
    public void statusSmsSend(String param, IUser user) {
        List<SmsSend> smsSendList = JsonUtil.readValue(param, new TypeReference<List<SmsSend>>() {
        });
        for (SmsSend smsSend : smsSendList) {
            if (smsSend != null && "0".equals(smsSend.getEuStatusChk())) {
                DataBaseHelper.update("update sms_send  set eu_status_chk='1' where pk_smssend in (?) and eu_status_chk='0' ", new Object[]{smsSend.getPkSmssend()});
            }
        }
    }

    /**
     * 保存短信发送
     *
     * @param param
     * @param user
     * @return
     */
    public void smsEditSave(String param, IUser user) {
        SmsSend smsSend = JsonUtil.readValue(param, SmsSend.class);
        if (smsSend != null) {
            if (smsSend.getPkSmssend() == null) {
                DataBaseHelper.insertBean(smsSend);
            } else {
                if ("1".equals(smsSend.getDelFlag())) {
                    DataBaseHelper.execute("delete from sms_send where Pk_Smssend = ?", new Object[]{smsSend.getPkSmssend()});
                } else {
                    DataBaseHelper.updateBeanByPk(smsSend);
                }
            }
        }
    }


    /**
     * 发送短信
     *
     * @param param
     * @param user
     */
    public void smsSend(String param, IUser user) {
        List<SmsSend> smsSendList = JsonUtil.readValue(param, new TypeReference<List<SmsSend>>() {
        });
        if (CollectionUtils.isEmpty(smsSendList) || user == null) {
            throw new BusException("待发送列表为空，或者当前用户信息为空！");
        }
        for (SmsSend smsSend : smsSendList) {
            if (smsSend == null) {
                continue;
            }
            if (smsSend.getEuStatusChk() != null && "1".equals(smsSend.getEuStatusChk())) {
                Pattern pattern = Pattern.compile("<(.*?)>");
                Matcher matcher = pattern.matcher(smsSend.getMobile());
                List<String> mobileList = new ArrayList<>();
                while (matcher.find()) {
                    String str = matcher.group(1);
                    if (StringUtils.isNotBlank(str)) {
                        mobileList.add(str);
                    }
                }
                if (mobileList.size() == 0) {
                    throw new BusException("未匹配到手机号列表");
                }
                smsSend(smsSend.getPkSmssend(), smsSend.getContent(), mobileList);
            }
        }
    }

    public void smsSend(String pkSmssend, String content, List<String> mobileList) {
        if (StringUtils.isAnyBlank(pkSmssend, content) || CollectionUtils.isEmpty(mobileList)) {
            throw new BusException("发送短信时入参异常");
        }
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(doPost(StringUtils.join(mobileList, ","), content));
        } catch (Exception e) {
            throw new BusException("调用短信接口异常：" + e.getMessage());
        }
        if (json == null) {
            throw new BusException("调用短信返回为空");
        }
        //调用完发送接口，保存结果，回调后修改
        saveResult(mobileList, json.getString("msgGroup"), pkSmssend);
        DataBaseHelper.update("update sms_send set flag_finish=1,cnt_send=nvl(cnt_send,0)+1,cnt_failure=nvl(cnt_failure,0)+? where pk_smssend=?", new Object[]{json.getBoolean("success") ? 0 : 1, pkSmssend});
    }

    /**
     * 保存一个smsSend对应的结果列表
     *
     * @param mobileList
     * @param msgGroup
     * @param pkSmssend
     */
    public void saveResult(List<String> mobileList, String msgGroup, String pkSmssend) {
        List<SmsSendResult> sendResultList = new ArrayList<>(mobileList.size());
        for (String mobile : mobileList) {
            SmsSendResult smsSendResult = new SmsSendResult();
            smsSendResult.setMsgGroup(msgGroup);
            smsSendResult.setMobile(mobile);
            smsSendResult.setPkSmssend(pkSmssend);
            ApplicationUtils.setDefaultValue(smsSendResult, true);
            sendResultList.add(smsSendResult);
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SmsSendResult.class), sendResultList);
    }

    /**
     * @param mobiles 多个用,号分开，不超过5000个
     * @param content
     * @return
     */
    public String doPost(String mobiles, String content) {
        //请求地址
        String url = ApplicationUtils.getPropertyValue("msg.sms.send", "http://i180843m68.iask.in/mas/wechatInterface/sendSms");
        JsonObject object = new JsonObject();
        object.addProperty("mobiles", mobiles);
        object.addProperty("content", content);
        String resultString = HttpClientUtil.sendHttpPostJson(url, object.toString());
        logger.info("短信发送接口调用完成：{}", resultString);
        return resultString;
    }

    public List<SmsSend> saveAndGetSmsSend(List<SmsTemp> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<SmsSend> smsSendVos = new ArrayList<>();
        //可遍历SmsSend模板，判断按日按月，取时间。
        for (SmsTemp smsTemp : list) {
            Date date;
            if ("1".equals(smsTemp.getEuType())) {
                date = DateUtils.addMonths(new Date(), -1);//上月时间
            } else {
                date = DateUtils.addDays(new Date(), -1);//昨天
            }
            if (StringUtils.isNotBlank(smsTemp.getMobile())) {
                smsSendVos.add(process(smsTemp, DateFormatUtils.format(date, EnumerateParameter.ONE.equals(smsTemp.getEuType()) ? "yyyy-MM" : "yyyy-MM-dd"), DateFormatUtils.format(date, EnumerateParameter.ONE.equals(smsTemp.getEuType()) ? "yyyy年MM月" : "yyyy年MM月dd日")));
            }
        }
        return smsSendVos;
    }

    /**
     * 生成一次模板对应的待发送记录。
     * 按月：每月不管那一条只发一次、按日：每天只发一次
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void autoSave() {
        User user = UserContext.getUser();
        Date date = new Date();
        List<SmsTemp> tempList = DataBaseHelper.queryForList("select * from sms_temp st where st.del_flag=0 and st.pk_org=?" +
                        " and not exists(select 1 from sms_send sd where sd.code_temp=st.code_temp " +
                        " and ((st.EU_TYPE=1 and to_char(sd.create_time,'yyyyMM')=?) or (st.EU_TYPE=0 and to_char(sd.create_time,'yyyyMMdd')=?))) order by st.sortno"
                , SmsTemp.class, new Object[]{user.getPkOrg(), DateFormatUtils.format(date, "yyyyMM"), DateFormatUtils.format(date, "yyyyMMdd")});
        logger.info("查到符合条件的sms模板数量{}", tempList.size());
        saveAndGetSmsSend(tempList);
    }

    /**
     * @return java.util.List<com.zebone.nhis.pro.lb.vo.BaseSendtaskList>
     * @Description
     * @auther wuqiang
     * @Date 2019-12-21
     * @Param [list, dataPar查询日期, dateMess消息中日期填写]
     */
    private SmsSend process(SmsTemp smsTemp1, String datePar, String dateMess) {
        Map<String, Object> map = new HashMap<>();
        initSettingParam(smsTemp1.getPkSmstemp(), map);
        map.put("euType", smsTemp1.getEuType());
        map.put("date", datePar);
        return createMessage(queryMessage(map), dateMess, smsTemp1);
    }

    /**
     * 构造模板中其他多选参数
     *
     * @param pkSmstemp
     * @param map
     */
    private void initSettingParam(String pkSmstemp, Map<String, Object> map) {
        List<SmsTempSt> stList = DataBaseHelper.queryForList("select dt_type,val from sms_temp_st where pk_smstemp=?", SmsTempSt.class, new Object[]{pkSmstemp});
        if (CollectionUtils.isNotEmpty(stList)) {
            List<String> listOpDept = Lists.newArrayList();
            List<String> listIpDept = Lists.newArrayList();
            List<String> listDeptNs = Lists.newArrayList();
            for (SmsTempSt st : stList) {
                if (StringUtils.isBlank(st.getVal()) || StringUtils.isBlank(st.getDtType())) {
                    continue;
                }
                switch (st.getDtType()) {
                    case EnumerateParameter.ONE:
                        listOpDept.add(st.getVal());
                        break;
                    case EnumerateParameter.TWO:
                        listIpDept.add(st.getVal());
                        break;
                    case EnumerateParameter.THREE:
                        listDeptNs.add(st.getVal());
                        break;
                    default:
                }
            }
            map.put("opDepts", listOpDept);
            map.put("ipDepts", listIpDept);
            map.put("pkDeptNs", listDeptNs);
        }
    }

    private Map<String, Object> queryMessage(Map<String, Object> mapParam) {
        Map<String, Object> map = smsMapper.querySumOpMessage(mapParam);
        if (map == null) {
            map = new HashMap<>();
        }
        map.putAll(smsMapper.querySumIpMessage(mapParam));
        BigDecimal opSetleAmount = BigDecimal.valueOf(MapUtils.getDoubleValue(map, "opsetleamount"));
        BigDecimal unInComoe = BigDecimal.valueOf(MapUtils.getDoubleValue(map, "unincomoe"));
        ;
        map.put("totalamount", opSetleAmount.add(unInComoe));
        return map;
    }

    /**
     * @return com.zebone.nhis.pro.lb.vo.ThirdMessageVo
     * @Description 组装消息内容
     * @auther wuqiang
     * @Date 2019-12-19
     * @Param [map 数据, date 时间, baseSendtaskCategory  模板]
     */
    private SmsSend createMessage(Map<String, Object> map, String date, SmsTemp smsTemp) {
        String message = date + "," + smsTemp.getContent();
        for (EnumMessage s : EnumMessage.values()) {
            String regec = "\\$\\{" + s.name() + "\\}";
            message = message.replaceAll(regec, String.valueOf(map.get(s.getDescription())));
        }
        SmsSend smsSend = new SmsSend();
        smsSend.setMobile(smsTemp.getMobile());
        smsSend.setCodeTemp(smsTemp.getCodeTemp());
        smsSend.setNameTemp(smsTemp.getNameTemp());
        if ("0".equals(smsTemp.getEuChktype())) {
            smsSend.setEuStatusChk("1");
        } else {
            smsSend.setEuStatusChk("0");
        }
        smsSend.setContent(replaceTitle(message, smsTemp));
        smsSend.setReqid(Long.valueOf("-1").toString());
        smsSend.setFlagFinish(EnumerateParameter.ZERO);
        smsSend.setDateSend(smsTemp.getTimeSend() == null ? null : getDateSend(smsTemp.getTimeSend()));
        ApplicationUtils.setDefaultValue(smsSend, true);
        DataBaseHelper.insertBean(smsSend);
        return smsSend;
    }

    private Date getDateSend(String date) {
        try {
            return DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd") + " " + StringUtils.trim(date), "yyyy-MM-dd hh:mm:ss");
        } catch (ParseException e) {
            throw new BusException("日期转换异常,短信模板中发送时间格式有误");
        }
    }

    private String replaceTitle(String message, SmsTemp smsTemp) {
        final String titleMk = "[title]";
        if (message.indexOf(titleMk) < 0) {
            return message;
        }
        return message.replace("[title]", smsTemp.getNameTemp());
    }


    /***
     * @Description
     * 根据模板参数，获得对应数据所在位置，以及数据
     * @auther wuqiang
     * @Date 2019-12-19
     * @Param [en]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    private Map<String, Object> getEnumMessage(String en) {
        Map<String, Object> map = new HashMap<>(2);
        for (EnumMessage s : EnumMessage.values()) {
            if (s.name().equals(en)) {
                map.put("number", s.getCode());
                map.put("description", s.getDescription());
                return map;
            }
        }
        return map;
    }

    /***
     * @Description
     * 短信模板参数对应枚举类
     * @auther wuqiang
     * @Date 2019-12-19
     * @Param
     * @param
     * @return
     */
    private enum EnumMessage {
        //总金额=门诊收入+住院结算收入
        VAL1(0, "totalamount"),
        //门诊收入
        VAL2(1, "opsetleamount"),
        //住院发生收入
        VAL3(2, "unincomoe"),
        //住院结算收入
        VAL4(3, "setleamount"),
        //门诊总人数
        VAL5(4, "opsumnum"),
        //新入院人数
        VAL6(5, "newadmnum"),
        //出院病人人数
        VAL7(6, "leavenum"),
        //在原病人人数
        VAL8(7, "inaum");

        EnumMessage(int number, String description) {
            this.code = number;
            this.description = description;
        }

        private int code;
        private String description;

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
