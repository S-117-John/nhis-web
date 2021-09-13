package com.zebone.nhis.webservice.zsrm.service;

import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.zsrm.support.ZsrmMsgUtils;
import com.zebone.nhis.ma.pub.zsrm.vo.DrugQryVo;
import com.zebone.nhis.ma.pub.zsrm.vo.MachDrug;
import com.zebone.nhis.ma.pub.zsrm.vo.MachDrug.MachDrugHeader;
import com.zebone.nhis.webservice.zsrm.dao.ZsrmDrugPackMapper;
import com.zebone.nhis.webservice.zsrm.utils.InputParseTool;
import com.zebone.nhis.webservice.zsrm.vo.pack.MachInParam;
import com.zebone.nhis.webservice.zsrm.vo.pack.MachItem;
import com.zebone.nhis.webservice.zsrm.vo.pack.MachItem.MachItemHeader;
import com.zebone.nhis.webservice.zsrm.vo.pack.MachOrder;
import com.zebone.nhis.webservice.zsrm.vo.pack.MachOrder.MachOrderHeader;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZsrmWesDrugPackService {
    private static Logger log = LoggerFactory.getLogger("nhis.drug.pack");
    //上屏状态码
    private static final List<String> UP_SCREEN_STATUS = Arrays.asList("700","750");
    //下屏状态码
    private static final List<String> DOWN_SCREEN_STATUS = Arrays.asList("800","801","550");
    @Resource
    private ZsrmDrugPackMapper zsrmDrugPackMapper;

    /**
     * 依据“订单号”查询处方信息
     * @param orderId
     * @return
     */
    public String getOrders(String orderId){
        Map<String, Object> map = getPkSettle(orderId);
        if(map==null)return "-1";
        List<MachOrder> orderList = zsrmDrugPackMapper.getPresInfo(map);
        MachOrderHeader header = new MachOrderHeader();
        header.setOrders(orderList);

        return XmlUtil.beanToXml(header,MachOrderHeader.class, InputParseTool.getCustomerProperties());
    }

    /**
     * 依据“订单号”查询处方 明细
     * @param orderId
     * @return
     */
    public String getItems(String orderId) {
        Map<String, Object> map = getPkSettle(orderId);
        if(map==null)return "-1";
        List<MachItem> presItems = zsrmDrugPackMapper.getPresItems(map);
        for (MachItem presItem : presItems) {
            String[] splitQuan = String.valueOf(presItem.getQuanCg().doubleValue()).split("\\.");
            boolean big = "01".equals(presItem.getSpc());
            boolean dec = splitQuan.length>1 && Integer.valueOf(splitQuan[1])>0;
            if(dec && big){
                presItem.setAmount(MathUtils.mul(presItem.getQuanCg(),Double.valueOf(presItem.getPackSize())).intValue());
                presItem.setOrderItemPrice(MathUtils.mul(presItem.getOrderItemPrice(),Double.valueOf(presItem.getPackSize())));
                presItem.setYpId(new StringBuilder(presItem.getYpId()).replace(6,8,"00").toString());
            }
            BigDecimal dosageEnd=getNumberForZero(presItem.getDosage());
            presItem.setDosage(dosageEnd);
        }
        MachItemHeader header = new MachItemHeader();
        header.setItems(presItems);
        return XmlUtil.beanToXml(header,MachItemHeader.class, InputParseTool.getCustomerProperties());
    }

    /**
     * 用量舍零转化
     * @param dosage
     * @return
     */
    private BigDecimal getNumberForZero(BigDecimal dosage){
        String resTxt="";
        if(dosage!=null ){
            String bigtxt=dosage.setScale(4,BigDecimal.ROUND_HALF_UP).toString();
            if(bigtxt.contains(".")){
                for (int i = bigtxt.length()-1; i>=0; i--) {
                    char txt=bigtxt.charAt(i);
                    if(!"0".equals(String.valueOf(txt))){
                        resTxt=bigtxt.substring(0, i+1);
                        break;
                    }
                }
                if(resTxt.lastIndexOf(".")==resTxt.length()-1){

                    resTxt=resTxt.replace(".", "");
                }
            }else{
                resTxt=bigtxt;
            }
        }

        return new BigDecimal(resTxt);
    }

    public String saveSendDrugStatus(MachInParam inputVo) {
        if(inputVo==null){
            log.info("接受摆药机上/下屏消息，未解析到入参对象");
            return "-1";
        }
        if(StringUtils.isAnyBlank(inputVo.getOrderId(),inputVo.getUserId(),inputVo.getStatus())){
            log.info("接受摆药机上/下屏消息，OrderId,Status,UserId都不能为空");
            return "-1";
        }
        if(StringUtils.length(inputVo.getOrderId())!=16){
            log.info("接受摆药机上/下屏消息，OrderId长度错误");
            return "-1";
        }
        if(!UP_SCREEN_STATUS.contains(inputVo.getStatus()) && !DOWN_SCREEN_STATUS.contains(inputVo.getStatus())){
            log.info("接受摆药机上/下屏消息，Status取值错误");
            return "-1";
        }
        User user = DataBaseHelper.queryForBean("select emp.PK_ORG,emp.NAME_EMP,emp.PK_EMP,emp.CODE_EMP from BD_OU_EMPLOYEE emp inner join BD_OU_USER u on emp.PK_EMP=u.PK_EMP where u.CODE_USER = ?", User.class, inputVo.getUserId());
        if(user == null){
            log.info("接受摆药机上/下屏消息，依据UserId未获取到用户信息");
            return "-1";
        }
        boolean confirm = UP_SCREEN_STATUS.contains(inputVo.getStatus());
        String stCode = inputVo.getOrderId().substring(0,13);
        String euPvtype = inputVo.getOrderId().substring(13, 14);
        Integer   wino = Integer.parseInt(inputVo.getOrderId().substring(14,16));

        String paramSql = confirm?" and occ.eu_status in ('1','8','0') and occ.flag_prep='0' and occ.flag_canc='0'"
                :" and occ.flag_reg='1' and occ.flag_prep='1' and nvl(occ.flag_susp,'0')='0'";
        List<Map<String, Object>> list = DataBaseHelper.queryForList("select occ.PK_PRESOCC,occ.PK_DEPT_EX,store.PK_STORE from BL_SETTLE st" +
                " inner join EX_PRES_OCC occ on st.PK_SETTLE=occ.PK_SETTLE and st.PK_PV=occ.PK_PV" +
                " inner join BD_STORE store on occ.PK_DEPT_EX=store.PK_DEPT" +
                " where lpad(CODE_ST,13,'0')=?  and EU_PVTYPE=? " +
                " and occ.winno_conf=? and nvl(st.FLAG_CANC,'0')='0' and nvl(flag_conf,'0')='0'"+paramSql, new Object[]{stCode,euPvtype,wino.toString()});
        if(CollectionUtils.isEmpty(list)) {
            log.info("接受摆药机上/下屏消息，未获取到有效执行数据");
            return "-1";
        }
        log.info("{}构造出的配药参数：{}",confirm?"上屏-配药":"下屏-发药", JsonUtil.writeValueAsString(list));
        UserContext.setUser(user);
        ApplicationUtils apputil = new ApplicationUtils();
        if(confirm) {
            //上屏，触发HIS配药
            Set<String> pks = list.parallelStream().map(x-> MapUtils.getString(x,"pkPresocc")).collect(Collectors.toSet());
            Map<String,Object> paramReg = new HashMap<>();
            paramReg.put("datePrep", DateUtils.getDateTimeStr(new Date()));
            paramReg.put("pkEmp", user.getPkEmp());
            paramReg.put("nameEmp", user.getNameEmp());
            paramReg.put("pkPresoccs", pks);
            ResponseJson rs = apputil.execService("Scm", "ScmOpPreDispense2Service","saveConfirmDosageInfo", paramReg, user);
            log.info("上屏-配药执行结果：{}", JsonUtil.writeValueAsString(rs));
            if(0!=rs.getStatus()){
               log.info((StringUtils.isBlank(rs.getErrorMessage())?"":" "+rs.getErrorMessage()) + (StringUtils.isBlank(rs.getDesc())?"":" "+rs.getDesc()));
                return "-1";
            }
        } else {
            //下屏，触发HIS发药
            for (Map<String,Object> map:list) {
                Map<String,Object> paramReg = new HashMap<>();
                paramReg.put("pkPresocc",MapUtils.getString(map,"pkPresocc"));
                paramReg.put("pkDept", MapUtils.getString(map,"pkDeptEx"));
                paramReg.put("winno", wino);
                paramReg.put("pkStore",MapUtils.getString(map,"pkStore"));
                paramReg.put("codeEmp", user.getCodeEmp());
                ResponseJson rs = apputil.execService("Scm", "ScmOpDispense2Service","updateSendPd", paramReg, user);
                log.info("下屏-发药执行结果：{},{}",MapUtils.getString(map,"pkPresocc") , JsonUtil.writeValueAsString(rs));
                if(0!=rs.getStatus()){
                    log.info((StringUtils.isBlank(rs.getErrorMessage())?"":" "+rs.getErrorMessage()) + (StringUtils.isBlank(rs.getDesc())?"":" "+rs.getDesc()));
                    return "-1";
                }
            }
        }

        return EnumerateParameter.ZERO;
    }

    public String getBdInfoList() {
        List<DrugQryVo.WesDrugConfVo> config = ZsrmMsgUtils.getConfig();
        if(CollectionUtils.isNotEmpty(config)){
            DrugQryVo drugQryVo = new DrugQryVo();
            drugQryVo.setConfigs(config);
            List<MachDrug> drugList = zsrmDrugPackMapper.getBdPdStoreInfo(drugQryVo);
            drugList.stream().forEach(m->{
                if(CommonUtils.isNotNull(m.getSpec())){
                    m.setSpec(m.getSpec().replace("*","x"));
                }
            });
            drugList.sort(Comparator.comparing(MachDrug::getCode));
            MachDrugHeader header = new MachDrugHeader();
            header.setItems(drugList);
            return XmlUtil.beanToXml(header, MachDrugHeader.class, InputParseTool.getCustomerProperties());
        }
        return EnumerateParameter.ZERO;
    }



    private Map<String,Object> getPkSettle(String orderId){
        if(StringUtils.isNotBlank(orderId) && orderId.length()==16) {
            String code = orderId.substring(0, 13);
            String euPvtype = orderId.substring(13, 14);
            Map<String, Object> map = DataBaseHelper.queryForMap("select PK_SETTLE from BL_SETTLE where lpad(CODE_ST,13,'0')=? and nvl(FLAG_CANC,'0')='0' and EU_PVTYPE=?"
                            , new Object[]{code, euPvtype});
            if(MapUtils.isEmpty(map)){
                log.info("依据orderId解析后未查询到结算单"+orderId);
                return null;
            }
            return map;
        }
        log.info("orderId为空或者长度异常："+orderId);
        return null;
    }


}
