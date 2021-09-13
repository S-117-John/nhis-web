package com.zebone.nhis.pro.lb.service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.lb.dao.LbPiPubMapper;
import com.zebone.nhis.pro.lb.vo.BaseSendtaskCategory;
import com.zebone.nhis.pro.lb.vo.BaseSendtaskList;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Classname LbTaskServcie
 * 灵璧定时任务服务类
 * @Description TODO
 * @Date 2019-12-20 18:59
 * @Created by wuqiang
 */

@Service
public class LbTaskServcie {

    @Autowired
    private LbPiPubMapper lbPiPubMapper;

     public List<BaseSendtaskList> getTthirdMessageVo(List<BaseSendtaskCategory> list) {

        List<BaseSendtaskList> thirdMessageVos = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return null;
        }
         Date dateYes = null;//昨天
         Date dateMonLast = null;//上月时间
         Calendar calendar = Calendar.getInstance();//可以对每个时间域单独修改
         int year = calendar.get(Calendar.YEAR);
         int month = calendar.get(Calendar.MONTH);
         int date = calendar.get(Calendar.DATE);
         if (date == 1) {
             calendar.set(Calendar.MONTH, month - 1);
             calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
             dateMonLast = calendar.getTime();
         }
         calendar.set(year, month, date - 1);
         dateYes = calendar.getTime();
        String dataPar = DateUtils.dateToStr("yyyy-MM-dd", dateYes);
        String dateMess = DateUtils.dateToStr("yyyy年MM月dd日", dateYes);
        thirdMessageVos.addAll(process(list, dataPar, dateMess));
        if (dateMonLast != null) {
            dataPar = DateUtils.dateToStr("yyyy-MM", dateMonLast);
            dateMess = DateUtils.dateToStr("yyyy年MM月", dateMonLast);
            thirdMessageVos.addAll(process(list, dataPar, dateMess));
        }
        return thirdMessageVos;
    }

    /**
     * @return java.util.List<com.zebone.nhis.pro.lb.vo.BaseSendtaskList>
     * @Description
     * @auther wuqiang
     * @Date 2019-12-21
     * @Param [list, dataPar查询日期, dateMess消息中日期填写]
     */
    private List<BaseSendtaskList> process(List<BaseSendtaskCategory> list, String dataPar, String dateMess) {
        List<BaseSendtaskList> thirdMessageVos = new ArrayList<>();
        String sql = "";
        for (BaseSendtaskCategory baseSendtaskCategory1 : list) {
            List<Map<String, Object>> mapList = new ArrayList<>(50);
            switch (String.valueOf(baseSendtaskCategory1.getId())) {
                case "60":
                    sql = "select * from MSG_DEPT ";
                    mapList = DataBaseHelper.queryForList(sql);
                    Map<String, Object> map1 = queryMessage(mapList, dataPar);
                    BaseSendtaskList thirdMessageVo1 = createMessage(map1, dateMess, baseSendtaskCategory1);
                    thirdMessageVos.add(thirdMessageVo1);
                    break;
                case "79":
                    sql = "select * from MSG_DEPT ";
                    mapList = DataBaseHelper.queryForList(sql);
                    Map<String, Object> map79 = queryMessage(mapList, dataPar);
                    BaseSendtaskList thirdMessageVo79= createMessage(map79, dateMess, baseSendtaskCategory1);
                    thirdMessageVos.add(thirdMessageVo79);
                    break;
                case "80":
                    sql = "select * from MSG_DEPT ";
                    mapList = DataBaseHelper.queryForList(sql);
                    Map<String, Object> map80 = queryMessage(mapList, dataPar);
                    BaseSendtaskList thirdMessageVo80= createMessage(map80, dateMess, baseSendtaskCategory1);
                    thirdMessageVos.add(thirdMessageVo80);
                    break;
                default:
                    sql = "select * from MSG_DEPT where id= ?";
                    mapList = DataBaseHelper.queryForList(sql, new Object[]{baseSendtaskCategory1.getId()});
                    Map<String, Object> map = queryMessage(mapList, dataPar);
                    BaseSendtaskList thirdMessageVo = createMessage(map, dateMess, baseSendtaskCategory1);
                    thirdMessageVos.add(thirdMessageVo);
                    break;
            }
        }
        return thirdMessageVos;
    }

    private Map<String, Object> queryMessage(List<Map<String, Object>> mapList, String date) {
        List<String> op = new ArrayList<>();// 门诊
        List<String> ip = new ArrayList<>();//住院
        for (Map<String, Object> map : mapList) {
            op.add(String.valueOf(map.get("opCode")));
            if (map.get("ipDept") != null) {
                ip.add(String.valueOf(map.get("ipDept")));
            }
        }
        Map<String, Object> map = new HashMap<>(10);
        map = sumOpMessage(op,date);
        map.putAll(sumIpMessage(ip, date));
        BigDecimal opSetleAmount = BigDecimal.ZERO;
        BigDecimal unInComoe = BigDecimal.ZERO;
        if (map.get("opsetleamount") != null) {
            opSetleAmount = BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("opsetleamount"))));
        }
        if (map.get("unincomoe") != null) {
            unInComoe = BigDecimal.valueOf(Double.valueOf(String.valueOf(map.get("unincomoe"))));
        }
        String totalAmount = opSetleAmount.add(unInComoe).toString();
        map.put("totalamount", totalAmount);
        return map;
    }

    /**
     * @return com.zebone.nhis.pro.lb.vo.ThirdMessageVo
     * @Description 组装消息内容
     * @auther wuqiang
     * @Date 2019-12-19
     * @Param [map 数据, date 时间, baseSendtaskCategory  模板]
     */
    private BaseSendtaskList createMessage(Map<String, Object> map, String date, BaseSendtaskCategory baseSendtaskCategory) {
        BaseSendtaskList thirdMessageVo = new BaseSendtaskList();
        StringBuffer  note = new StringBuffer();
        thirdMessageVo.setModname(baseSendtaskCategory.getTypename());
        thirdMessageVo.setTimetosend(DateUtils.strToDate(DateUtils.getDate() + " " + baseSendtaskCategory.getTimetosend(), "yyyy-MM-dd HH:mm:ss"));
        String message = date + "," + baseSendtaskCategory.getTemp();
        for (EnumMessage s : EnumMessage.values()) {
            String regec = "\\$\\{" + s.name() + "\\}";
            message = message.replaceAll(regec, String.valueOf(map.get(s.getDescription())));
            note.append(map.get(s.getDescription())).append(",");
        }
        thirdMessageVo.setNote(note.toString());
        thirdMessageVo.setOperdate(new Date());
        thirdMessageVo.setSendaddress(baseSendtaskCategory.getMobiles());
        thirdMessageVo.setCheckstatus(baseSendtaskCategory.getAutocheck());
        thirdMessageVo.setSendcontent(message);
        thirdMessageVo.setCompanyid("1005");
        thirdMessageVo.setReqid( Long.valueOf("-1"));
        thirdMessageVo.setSysname("yxt");
        thirdMessageVo.setTasktype("sms");
        return thirdMessageVo;
    }


    /***
     * @Description
     * 计算科室门诊总收入opSetleAmount，门诊总人次opSumNum
     * @auther wuqiang
     * @Date 2019-12-18
     * @Param [list]
     * @return java.util.Map<java.lang.String, java.lang.String>
     *
     */
    private Map<String, Object> sumOpMessage(List<String> list, String date) {
        return lbPiPubMapper.querySumOpMessage(list, date);
    }

    /**
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @Description 查询住院发生收入unInComoe，住院结算收入 setleAmount，新入院人数 newAdmNum，
     * 出院病人人数leaveNum，在院病人人数inAum
     * @auther wuqiang
     * @Date 2019-12-18
     * @Param [list] 科室编码
     */
    private Map<String, Object> sumIpMessage(List<String> list, String date) {
        return lbPiPubMapper.querySumIpMessage(list, date);
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

    private void getDate(Date dateYes, Date dateMonLast) {

    }

    /***
     * @Description
     * 短信模板参数对应枚举类
     * @auther wuqiang
     * @Date 2019-12-19
     * @Param
     * @param null
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

/***
 * @Description
 * 插入第三方数据库，有事务
 * @auther wuqiang
 * @Date 2019-12-22
 * @Param [thirdMessageVo]
 * @return void
 */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
  public  void updataBae( List<BaseSendtaskList> thirdMessageVo){
      DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BaseSendtaskList.class),thirdMessageVo);
  }


}

