package com.zebone.nhis.cn.opdw.service;

import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.OpPressVo;
import com.zebone.nhis.cn.opdw.dao.CnOperationMapper;
import com.zebone.nhis.cn.opdw.vo.CnIpOpApplyVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.cp.CpRecExp;
import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpInfect;
import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CnOperationService {

    @Autowired
    private CnOperationMapper cnOpMapper;
    @Autowired
    private CnPubService cnPubService;
    @Autowired
    private BdSnService bdSnService;

    private static final String CODE_OP = "DEF99999";		//手术医嘱的code,约定
    private static SimpleDateFormat dateformat =  new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * 保存手术申请及附加手术（重写）
     *
     * @param param
     * @param user
     */
    public OpPressVo saveOpApply(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        OpPressVo rtn = new OpPressVo();
        CnOpApply rtnOp = new CnOpApply();
        CnIpOpApplyVo cnOpApplyMore = JsonUtil.readValue(param, CnIpOpApplyVo.class);
        OpPressVo cnInfectMore = new OpPressVo();
        rtnOp = saveOpApply(cnOpApplyMore, user, param);

        BeanUtils.copyProperties(cnInfectMore, rtnOp);
        rtn = saveInfect(cnInfectMore, user, param);
        //发送平台消息
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("saveOpApply", "");//方法名标志
        PlatFormSendUtils.sendOpApplyMsg(paramMap);
        return rtn;
    }

    /**
     * 同上
     *
     * @param cnOpApplyMore
     * @param user
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public CnOpApply saveOpApply(CnIpOpApplyVo cnOpApplyMore, IUser user, String param) throws IllegalAccessException, InvocationTargetException {
        CnOpApply rtn = new CnOpApply();
        List<CnOpSubjoin> subOpList = cnOpApplyMore.getSubOpList();
        List<CnOpSubjoin> subOpListForDel = cnOpApplyMore.getSubOpListForDel();
        Date dateNow = new Date();
        User userInfo = (User) user;

        //数据一致性验证
        if (cnOpApplyMore.getTs() != null) {
            List<String> pkCnordTsList = new ArrayList<String>();
            pkCnordTsList.add(cnOpApplyMore.getPkCnord() + "," + dateformat.format(cnOpApplyMore.getTs()));
            if (pkCnordTsList == null || pkCnordTsList.size() <= 0) return null;
            cnPubService.splitPkTsValidOrd(pkCnordTsList);
        }
        CnOpApply cnop= new CnOpApply();
        Date dd = cnPubService.getOutOrdDate(cnOpApplyMore.getPkPv());
        if (dd != null && dd.compareTo(dateNow) < 0) {
            dateNow = dd;
        }

        CnOrder order = null;
        if (cnOpApplyMore.getPkCnord() != null) {
            order = cnOpMapper.selectOrder(cnOpApplyMore.getPkCnord());
        } else {
            order = new CnOrder();
        }


        if (StringUtils.isBlank(cnOpApplyMore.getPkOrdop())) {
            order.setPkCnord(NHISUUID.getKeyId());
            order.setCodeOrdtype("04");                    //手术类型
            order.setEuAlways("1");                        //临时
            order.setCodeOrd(CODE_OP);
            order.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, user));
            order.setOrdsnParent(order.getOrdsn());
            order.setDescOrd(order.getNameOrd());
            order.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));
            order.setQuanCg(1.0);
            order.setQuan(1.0);            //医嘱的数量
            order.setDosage(1.0);
            order.setEuStatusOrd(cnOpApplyMore.getEuStatusOrd());  //医嘱状态
            order.setEuPvtype(cnOpApplyMore.getEuPvtype());  //患者就诊类型
            order.setFlagDoctor("1");
            order.setFlagErase("0");
            order.setFlagStopChk("0");
            order.setFlagStop("0");
            order.setFlagEraseChk("0");
            order.setFlagFirst(Constants.FALSE);
            order.setFlagDurg(Constants.FALSE);
            order.setFlagSelf(Constants.FALSE);
            order.setFlagNote(Constants.FALSE);
            order.setFlagBase(Constants.FALSE);
            order.setFlagBl(Constants.FALSE);
            order.setFlagStop(Constants.FALSE);
            order.setFlagPrint(Constants.FALSE);
            order.setFlagMedout(Constants.FALSE);
            order.setFlagEmer(cnOpApplyMore.getFlagEmer());
            order.setFlagItc(cnOpApplyMore.getFlagItc());
            order.setFlagThera(Constants.FALSE);
            order.setFlagPrev(Constants.FALSE);
            order.setFlagFit(Constants.FALSE);
            order.setFlagCp(StringUtils.isBlank(cnOpApplyMore.getFlagCp()) ? Constants.FALSE : cnOpApplyMore.getFlagCp());
            order.setTs(dateNow);
            order.setDateEnter(dateNow);    //暂用服务器时间，db时间后期处理
            order.setDateStart(dateNow);
            order.setDateSign(dateNow);
            order.setPkDept(userInfo.getPkDept());
            order.setPkEmpInput(userInfo.getPkEmp());
            order.setPkEmpOrd(userInfo.getPkEmp());
            order.setNameEmpInput(userInfo.getUserName());

            order.setPkDept(cnOpApplyMore.getPkDept());
            order.setEuIntern(cnOpApplyMore.getEuIntern());
            order.setNoteOrd(cnOpApplyMore.getNote());
            order.setPkOrgExec(cnOpApplyMore.getPkOrgExec());
            order.setPkDeptExec(cnOpApplyMore.getPkDeptExec());
            order.setPkDeptNs(cnOpApplyMore.getPkDeptNs());
            order.setCodeApply(cnOpApplyMore.getCodeApply());
            order.setNameOrd(cnOpApplyMore.getOpName());                //手术医嘱名称带格式
            order.setPkPv(cnOpApplyMore.getPkPv());
            order.setPkPi(cnOpApplyMore.getPkPi());
            order.setPkOrg(cnOpApplyMore.getPkOrg());
            order.setDtHpprop(cnOpApplyMore.getDtHpprop());
            order.setDateEffe(getDateEffe());
            order.setDays(1L);

            cnOpApplyMore.setPkOrdop(NHISUUID.getKeyId());
            cnOpApplyMore.setPkCnord(order.getPkCnord());
            cnOpApplyMore.setTs(dateNow);
            cnOpApplyMore.setDelFlag(Constants.FALSE);
            cnOpApplyMore.setEuStatus(StringUtils.isNotBlank(cnOpApplyMore.getEuStatus())?cnOpApplyMore.getEuStatus():"0");
            cnOpApplyMore.setPkOrg(userInfo.getPkOrg());
            cnOpApplyMore.setDateApply(dateNow);


            BeanUtils.copyProperties(cnop, cnOpApplyMore);
            DataBaseHelper.insertBean(cnop);
            DataBaseHelper.insertBean(order);

        } else {
            order.setNameOrd(cnOpApplyMore.getOpName());
            order.setDescOrd(order.getNameOrd());
            order.setTs(dateNow);
            order.setNoteOrd(cnOpApplyMore.getNote());
            order.setPkOrgExec(cnOpApplyMore.getPkOrgExec());
            order.setPkDeptExec(cnOpApplyMore.getPkDeptExec());

            cnOpApplyMore.setTs(dateNow);
            //cnOpApplyMore.setEuStatus("0");
            BeanUtils.copyProperties(cnop, cnOpApplyMore);
            DataBaseHelper.updateBeanByPk(cnop, false);
            DataBaseHelper.updateBeanByPk(order, false);

            if (cnOpApplyMore.getPkDeptAnae() == null && cnOpApplyMore.getPkEmpAnae() == null) {
                DataBaseHelper.update("update cn_op_apply set PK_DEPT_ANAE=:pkDeptAnae,PK_EMP_ANAE=:pkEmpAnae  where pk_cnord=:pkCnord ", cnOpApplyMore);
            }
        }

        List<CnOpSubjoin> addCnOpSubjoin = new ArrayList<CnOpSubjoin>();
        List<CnOpSubjoin> updateCnOpSubjoin = new ArrayList<CnOpSubjoin>();
        if (subOpList != null) {
            for (CnOpSubjoin cnOpSub : subOpList) {
                cnOpSub.setPkOrdop(cnOpApplyMore.getPkOrdop());
                CnOpSubjoin subOp = new CnOpSubjoin();
                BeanUtils.copyProperties(subOp, cnOpSub);
                if (StringUtils.isBlank(subOp.getPkCnopjoin())) {
                    subOp.setPkCnopjoin(NHISUUID.getKeyId());
                    subOp.setTs(dateNow);
                    subOp.setCreateTime(dateNow);
                    subOp.setCreator(userInfo.getPkEmp());
                    addCnOpSubjoin.add(subOp);
                } else {
                    updateCnOpSubjoin.add(subOp);
                }
            }
        }
        if (subOpListForDel != null && subOpListForDel.size() > 0) {
            DataBaseHelper.batchUpdate("delete from cn_op_subjoin where pk_cnopjoin = :pkCnopjoin ", subOpListForDel);
        }
        if (addCnOpSubjoin != null && addCnOpSubjoin.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOpSubjoin.class), addCnOpSubjoin);
        }
        if (updateCnOpSubjoin != null && updateCnOpSubjoin.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOpSubjoin.class), updateCnOpSubjoin);
        }

        List<CnOpSubjoin> rtnCnOpSubjoinList = DataBaseHelper.queryForList("select * from cn_op_subjoin where del_flag='0' and pk_ordop = ?", CnOpSubjoin.class, new Object[]{cnOpApplyMore.getPkOrdop()});
        BeanUtils.copyProperties(rtn, cnOpApplyMore);

        //路径外医嘱变异记录
        if (cnOpApplyMore.getCpRecExpList() != null && cnOpApplyMore.getCpRecExpList().size() > 0) {
            for (CpRecExp cpRecExpParam : cnOpApplyMore.getCpRecExpList()) {
                cpRecExpParam.setPkCnord(order.getPkCnord());
            }
            rtn.setCpRecExpList(cnOpApplyMore.getCpRecExpList());
        }
        if (cnOpApplyMore.getCnSignCa() != null) {
            cnOpApplyMore.getCnSignCa().setPkBu(order.getPkCnord());
            rtn.setCnSignCa(cnOpApplyMore.getCnSignCa());
        }


        rtn.setSubOpList(rtnCnOpSubjoinList);
        rtn.setCnOrder(order);

        return rtn;
    }

    public OpPressVo saveInfect(OpPressVo cnOpApplyMore, IUser user, String param) throws IllegalAccessException, InvocationTargetException {
        OpPressVo rtn = new OpPressVo();
        OpPressVo cnInfectMore = JsonUtil.readValue(param, OpPressVo.class);
        List<CnOpInfect> infectList = cnInfectMore.getInfectOpList();
        Date dateNow = new Date();
        User userInfo = (User) user;

        /*******     感染类型     start       ********/
        List<CnOpInfect> addInfect = new ArrayList<CnOpInfect>();
        List<CnOpInfect> updateInfect = new ArrayList<CnOpInfect>();
        if (infectList != null) {
            for (CnOpInfect cninfect : infectList) {
                cninfect.setPkCnord(cnOpApplyMore.getPkCnord());
                CnOpInfect subIn = new CnOpInfect();
                BeanUtils.copyProperties(subIn, cninfect);
                if (StringUtils.isBlank(subIn.getPkOpinfect())) {
                    subIn.setPkOpinfect(NHISUUID.getKeyId());
                    subIn.setTs(dateNow);
                    subIn.setCreateTime(dateNow);
                    subIn.setCreator(userInfo.getPkEmp());
                    addInfect.add(subIn);
                } else {
                    updateInfect.add(cninfect);
                }
            }
        }
        if (addInfect != null && addInfect.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOpInfect.class), addInfect);
        }
        if (updateInfect != null && updateInfect.size() > 0) {
            DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnOpInfect.class), updateInfect);
        }

        List<CnOpInfect> rtnInfectList = DataBaseHelper.queryForList("select * from cn_op_infect where del_flag='0' and pk_cnord = ?", CnOpInfect.class, new Object[]{cnOpApplyMore.getPkCnord()});

        /*******     感染类型     end       ********/

        BeanUtils.copyProperties(rtn, cnOpApplyMore);

        rtn.setInfectOpList(rtnInfectList);
        return rtn;
    }

    /**
     * 删除手术申请
     * @param param
     */
    public void removeOpApply(String param, IUser user){
        Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
        String pkOrdop = paramMap.get("pkOrdop");
        String pkCnordTs = paramMap.get("pkCnord");
        String pkCnords = paramMap.get("pkCnords");
        //校验手术是计费
        Map<String, Object> rtnInfectList = cnOpMapper.queryOpOrdAmount(pkCnords);
        if(rtnInfectList!=null){
            String amtS=getPropValueStr(rtnInfectList,"amt");
            Double amt= amtS==null? 0.0: Double.parseDouble(amtS);
            if(amt>0)
                throw new BusException("当前医嘱已计费，不可删除！");
        }
        String pkCnord = cnPubService.splitPkTsValidOrd(pkCnordTs);
        //平台消息数据
        Map<String, Object> map = cnOpMapper.queryOperation(pkCnord);
        cnOpMapper.removeOpApply(pkOrdop);
        cnOpMapper.removeChildApply(pkOrdop);
        cnOpMapper.removeOpOrder(pkCnord);
        cnOpMapper.removeOpInfect(pkCnord);
        cnOpMapper.removeOpCg(pkCnord);
       //发送平台消息
        map.put("removeopapply", "");//方法名标志
       PlatFormSendUtils.sendOpApplyMsg(map);
    }

    public static Date getDateEffe() {
        int IntEffeDate = Integer.parseInt(ApplicationUtils.getSysparam("CN0004", false,"3"));
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE, now.get(Calendar.DATE) + IntEffeDate);
        return now.getTime();
    }

    /**
     * 取文本内容
     * @param map
     * @return
     */
    public static String getPropValueStr(Map<String, Object> map,String key) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        String value="" ;
        if(map.containsKey(key)){
            Object obj=map.get(key);
            value=obj==null?"":obj.toString();
        }
        return value;
    }


}
