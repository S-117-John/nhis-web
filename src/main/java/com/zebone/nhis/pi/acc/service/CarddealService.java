package com.zebone.nhis.pi.acc.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlPi;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.acc.*;
import com.zebone.nhis.common.module.pv.BlExtPayBankVo;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pi.acc.dao.CardDealMapper;
import com.zebone.nhis.pi.acc.vo.ChangeCardParam;
import com.zebone.nhis.pi.acc.vo.PagePiaccVo;
import com.zebone.nhis.pi.acc.vo.PageQueryPiccParam;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;


/**
 * 患者卡信息处理，院内就诊卡需要收费并生成相应的账户费用信息 患者账户在发卡之前就已经有其账户存在
 *
 * @author L
 */
@Service
public class CarddealService {

    @Autowired
    private CardDealMapper cardDealMapper;
    @Autowired
    BalAccoutService jsservice;

    /***
     * @Description 002004001003, 获取患者账户信息列表
     * @auther wuqiang
     * @Date 2019-11-06
     * @Param [param, user]
     * @return com.zebone.nhis.pi.acc.vo.PagePiaccVo
     */
    public PagePiaccVo queryPiAccs(String param, IUser user) {
        PagePiaccVo pagePiaccVo = new PagePiaccVo();
        PageQueryPiccParam pageQueryPiccParam = JsonUtil.readValue(param, PageQueryPiccParam.class);
        if (pageQueryPiccParam == null)
            throw new BusException("查询参数有问题！");
        int pageIndex = CommonUtils.getInteger(pageQueryPiccParam.getPageIndex());
        int pageSize = CommonUtils.getInteger(pageQueryPiccParam.getPageSize());
        // 分页操作
        String pkOrg = UserContext.getUser().getPkOrg();
        pageQueryPiccParam.setPkOrg(pkOrg);
        MyBatisPage.startPage(pageIndex, pageSize);
        List<Map<String, Object>> list = cardDealMapper.queryPiAccs(pageQueryPiccParam);
        Page<List<Map<String, Object>>> page = MyBatisPage.getPage();
        pagePiaccVo.setPiccPiList(list);
        pagePiaccVo.setTotalCount(page.getTotalCount());
        return pagePiaccVo;
    }


    /**
     * 获取患者账户信息 要求：如果被查询患者有授权人信息，显示授权人信息，如果没有，显示自己的账户信息
     *
     * @param param
     * @param user
     * @return
     */
    public List<PiAcc> getPiAcc(String param, IUser user) {
        List<PiAcc> retuO = new ArrayList<PiAcc>();
        String pkPi = JSON.parseObject(param).getString("pkPi");
        PiAcc pkc = DataBaseHelper.queryForBean(
                "select * from pi_acc where pk_pi=?", PiAcc.class, pkPi);
        retuO.add(pkc);
        return retuO;
    }


    /**
     * 交易接口号：002004001006 保存患者授权信息 要求：被授权人不能够授权给其他人，能被授权（授权人唯一）,不能授权给自己
     *
     * @param param
     * @param zl
     */
    public void saveSharedAcc(String param, IUser user) {
        Date time = new Date();
        PiAccShare piShare = JsonUtil.readValue(param, PiAccShare.class);
        // 授权账户，如果该账户已被其他账户授权，则不允许授权给其他账户
        String pkPi = piShare.getPkPi();
        // 被授权账户，如果该账户已被授权或者已授权给其他账户，则不允许再次被授权
        String pkPiUse = piShare.getPkPiUse();
        String getPkpi = "select * from pi_acc where pk_pi = ? ";
        String getSharLimt = "select * from pi_acc_share where pk_pi_use=? and del_flag='0'";
        List<PiAccShare> i = DataBaseHelper.queryForList(getSharLimt,
                PiAccShare.class, pkPi);
        String countUseSql = "select count(*) from pi_acc_share where (pk_pi_use = ? or pk_pi = ?) and del_flag='0'";
        Integer countUse = DataBaseHelper.queryForScalar(countUseSql,
                Integer.class, new Object[]{pkPiUse, pkPiUse});
        if (i.size() > 0) {
            throw new BusException("授权账户已被授权给其他账户，请重新选择。");
        } else if (piShare.getPkPi().equals(piShare.getPkPiUse())) {
            throw new BusException("授权人不能授权给自己！");
        } else if (countUse != null && countUse > 0) {
            throw new BusException("被授权账户已被授权或者已授权过其他账户，请重新选择。");
        }
        PiAcc piPkacc = DataBaseHelper.queryForBean(getPkpi, PiAcc.class,
                piShare.getPkPi());
        piShare.setPkPiacc(piPkacc.getPkPiacc());
        piShare.setDateOpera(time);
        piShare.setPkEmpOpera(((User) user).getPkEmp());
        piShare.setNameEmpOpera(((User) user).getNameEmp());
        piShare.setModifier(((User) user).getPkEmp());
        piShare.setModityTime(time);
        DataBaseHelper.insertBean(piShare);
    }

    /**
     * 交易接口号：002004001008 发卡数据处理 另外，如果是发卡类型为01 ，医院就诊卡的时候，需要交纳押金，做处理 冻结账户不可操作
     * 查询病人的卡，设置卡的序号
     * <p>
     * 校验卡号： 1.对于本账户信息，要求非作废的卡号不可重复，作废状态的卡号可重复 2.不是本账户的人，要求不可重复卡号
     *
     * @param param
     * @author zl
     */
    public Map<String, Object> saveCardInfos(String param, IUser user) {
        String pkpi = JSON.parseObject(param).getString("pkPi");
        String dtPaymode = JSON.parseObject(param).getString("dtPaymode");
        String payInfo = JSON.parseObject(param).getString("payInfo");
        PiCard picard = JsonUtil.readValue(param, PiCard.class);

        Calendar instance = Calendar.getInstance();
        Date time = instance.getTime();

        Map<String, Object> pamMap = new HashMap<>();

     /* 灵璧项目改造，允许个人拥有多张可用卡，
      // 查询该患者是否有在用状态的卡
      String cardStatus = "select * from pi_card where eu_status = '0' and del_flag = '0' "
                + " and date_end > ? and pk_pi = ? ";
        List<PiCard> piCards = DataBaseHelper.queryForList(cardStatus,
                PiCard.class, time, pkpi);
        if (piCards != null && piCards.size() != 0) {
            throw new BusException("该患者存在在用卡信息,请先进行注销或退卡");
        }*/


        // 先不将卡类型加入查询条件，发的卡类型有局限，如身份证
        String ckCardNOInScop = "select * from pi_card_iss where flag_use=1 and "
                + "flag_active=1 and eu_status=1 and pk_org=?";
        List<PiCardIss> piCardIss = DataBaseHelper.queryForList(ckCardNOInScop,
                PiCardIss.class, ((User) user).getPkOrg());
        if (IDictCodeConst.DT_CARDTYPE_HOS.equals(picard.getDtCardtype())
                && EnumerateParameter.ONE.equals(ApplicationUtils.getSysparam(
                "PI0006", false))) {
            if (piCardIss.size() != 1) {
                throw new BusException("领卡表维护错误，请修改。");
            } else {// 唯一一条
                if (piCardIss.get(0).getCntUse() <= 0) {
                    throw new BusException("剩余卡不足。");
                }
                // 添加参数控制按照领卡表/读卡接口发卡
                // 使用领卡表发卡时校验是否在范围内
                if (piCardIss.get(0).getBeginNo() == null) {
                    piCardIss.get(0).setBeginNo(0l);
                }
                if (!(Integer.valueOf(picard.getCardNo()) >= piCardIss.get(
                        0).getBeginNo() && Integer.valueOf(picard
                        .getCardNo()) <= piCardIss.get(0).getEndNo())) {
                    throw new BusException("不在卡号范围内，无法发卡。");
                }

            }
        }

        String yz = "select * from pi_card where FLAG_ACTIVE in ('1','0' )and EU_STATUS in ('0','1','2' ) and card_no=? ";// 检查全局范围内是否存在该卡号
        List<PiCard> yzLst = DataBaseHelper.queryForList(yz, PiCard.class,
                picard.getCardNo());
        if (yzLst.size() > 0) {// 数据库中存在卡号，校验，不存在的话可执行发卡操作
            boolean accountCon = true;
            List<String> lStats = new ArrayList<String>();
            String onez = yzLst.get(0).getPkPi();
            for (int i = 0; i < yzLst.size(); i++) {
                if (!onez.equals(yzLst.get(i).getPkPi())) {
                    accountCon = false;// 数据库存在不同账户
                    // 数据库已存在不同账户不执行发卡
                } else {
                    lStats.add(yzLst.get(i).getEuStatus());
                }
            }
            if (accountCon) {// 同一账户,
                if (!pkpi.equals(yzLst.get(0).getPkPi())) {// 校验当前账户是否相同
                    throw new BusException("已有其他账户拥有该卡！");// 否相同
                } else {// 相同，校验作废卡号（非作废不可重复）
                    if (lStats.contains(EnumerateParameter.ZERO)
                            || lStats.contains(EnumerateParameter.ONE)
                            || lStats.contains(EnumerateParameter.TWO)) {
                        throw new BusException("已存在有效的该卡信息，不可再次添加！");
                    }
                }
            } else {
                throw new BusException("已有多个账户拥有该卡，数据错误！");
            }
        }
        // 以上都以抛出异常信息为不执行一下操作，若要求不抛出异常信息，需要增加变量甲乙判断
        String getXh = "select * from pi_card where pk_pi=? and pk_org=?";
        List<PiCard> xhLst = DataBaseHelper.queryForList(getXh, PiCard.class,
                pkpi, ((User) user).getPkOrg());
        int num = DataBaseHelper
                .queryForScalar(
                        "select count(1) from pi_card  where pk_pi= ? and eu_status<'2' ",
                        Integer.class, pkpi);
        int maxNo = 0;
        for (PiCard no : xhLst) {
            if (no.getSortNo() >= maxNo) {
                maxNo = no.getSortNo();
            }
        }
        maxNo++;
        picard.setSortNo(maxNo);
        picard.setFlagActive(EnumerateParameter.ONE);
        picard.setEuStatus(EnumerateParameter.ZERO);
        DataBaseHelper.insertBean(picard);
        /**
         * 灵璧项目使用多张卡，和第三方交互，作为查询，故使其他卡片的flag_active='0',只保留当前卡片
         * */

        PiCardDetail piCardDetail = new PiCardDetail();
        piCardDetail.setPkEmpOpera(((User) user).getPkEmp());
        piCardDetail.setNameEmpOpera(((User) user).getNameEmp());
        piCardDetail.setDateHap(new Date());
        piCardDetail.setModifier(((User) user).getPkEmp());
        piCardDetail.setPkPicard(picard.getPkPicard());
        piCardDetail.setPkPi(picard.getPkPi());
        piCardDetail.setCardNo(picard.getCardNo());
        piCardDetail.setEuOptype(EnumerateParameter.ZERO);
        DataBaseHelper.insertBean(piCardDetail);
        // 医院就诊卡，插入交款记录信息
        if (IDictCodeConst.DT_CARDTYPE_HOS.equals(picard.getDtCardtype())) {
            // 获取押金金额
            String jeVal = ApplicationUtils.getSysparam("PI0002", false);
            // 获取制卡费
            String fee = ApplicationUtils.getSysparam("PI0004", false);
            String piAccInfo = "select * from pi_acc where pk_pi=? ";
            PiAcc pia = DataBaseHelper.queryForBean(piAccInfo, PiAcc.class,
                    pkpi);
            String aa = ApplicationUtils.getSysparam("PI0005", false);
            if (EnumerateParameter.ONE.equals(pia.getEuStatus())) {
                BigDecimal ye = pia.getAmtAcc();// 发卡时账户未加入押金
                pia.setAmtAcc(ye);
                DataBaseHelper.updateBeanByPk(pia, false);
                // 收取制卡费模式为1或2,插入交付款记录
                if (EnumerateParameter.ONE.equals(ApplicationUtils.getSysparam(
                        "PI0005", false))
                        || EnumerateParameter.TWO.equals(ApplicationUtils
                        .getSysparam("PI0005", false))
                        || EnumerateParameter.THREE.equals(ApplicationUtils
                        .getSysparam("PI0005", false))) {
                    BlDepositPi jkjl = new BlDepositPi();
                    BlPi blPi = new BlPi();
                    if (EnumerateParameter.THREE.equals(ApplicationUtils
                            .getSysparam("PI0005", false))) {
                        pia.setAmtAcc(new BigDecimal(jeVal).add(ye));
                        DataBaseHelper.updateBeanByPk(pia, false);
                        picard.setDeposit(new BigDecimal(jeVal));
                        DataBaseHelper.updateBeanByPk(picard, false);
                        jkjl.setAmount(new BigDecimal(jeVal));// 押金
                        jkjl.setEuDirect(EnumerateParameter.ONE);
                        jkjl.setPkPi(pkpi);
                        jkjl.setDatePay(new Date());
                        jkjl.setPkDept(((User) user).getPkDept());
                        jkjl.setPkEmpPay(((User) user).getPkEmp());
                        jkjl.setNameEmpPay(((User) user).getNameEmp());
                        jkjl.setDtPaymode(dtPaymode);
                        jkjl.setPayInfo(payInfo);
                        jkjl.setNote("押金");
                        DataBaseHelper.insertBean(jkjl);
                        updateThirdPay(jkjl);
                        piAccDetailVal(pia, jkjl, picard);
                    } else if ((num > 0 && EnumerateParameter.TWO.equals(ApplicationUtils.getSysparam("PI0005", false)))
                            || EnumerateParameter.ONE.equals(ApplicationUtils.getSysparam("PI0005", false))) {
                        // 采用补卡费模式1，收取卡费，如果num是0,则患者以前未发就诊卡，如果是1，则患者有在用或者挂失状态的就诊卡
                        jkjl.setAmount(new BigDecimal(fee));// 工本费
                        jkjl.setEuDirect(EnumerateParameter.ONE);
                        jkjl.setPkPi(pkpi);
                        jkjl.setDatePay(new Date());
                        jkjl.setPkDept(((User) user).getPkDept());
                        jkjl.setPkEmpPay(((User) user).getPkEmp());
                        jkjl.setNameEmpPay(((User) user).getNameEmp());
                        jkjl.setDtPaymode(EnumerateParameter.ONE);
                        //DataBaseHelper.insertBean(jkjl); //患者账户交退款记录
                        piAccDetailVal(pia, jkjl, picard);
                        // 非就诊计费明细
                        blPi.setPkPi(pkpi);
                        blPi.setNameBl("患者就诊卡制卡费");
                        blPi.setPrice(Double.valueOf(fee));
                        blPi.setQuan(Double.valueOf(EnumerateParameter.ONE));
                        blPi.setAmount(Double.valueOf(fee)
                                * Double.valueOf(EnumerateParameter.ONE));
                        blPi.setFlagPd(EnumerateParameter.ZERO); //
                        //制卡费暂不理会
                        blPi.setDtPaymode(EnumerateParameter.ONE);
                        blPi.setEuButype(EnumerateParameter.ZERO);
                        blPi.setPkBu(piCardDetail.getPkPicarddt());
                        blPi.setFlagCc(EnumerateParameter.ZERO);
                        nonTreatment(blPi, user);
                    }
                    //Lb-自助机webService需要BL_DEPOSIT_PI的主键
                    pamMap.put("pkDepopi", jkjl.getPkDepopi());
                }
                /*
                 * if(!"0".equals(jeVal)){ BlDepositPi jkjl=new BlDepositPi();
                 * jkjl.setEuDirect(EnumerateParameter.ONE); jkjl.setPkPi(pkpi);
                 * jkjl.setAmount(Double.valueOf(jeVal)); jkjl.setDatePay(new
                 * Date()); jkjl.setPkDept(((User)user).getPkDept());
                 * jkjl.setPkEmpPay(((User)user).getPkEmp());
                 * jkjl.setNameEmpPay(((User)user).getNameEmp());
                 * jkjl.setDtPaymode(EnumerateParameter.ONE);
                 * DataBaseHelper.insertBean(jkjl); piAccDetailVal(pia,jkjl); }
                 */
            } else if (EnumerateParameter.TWO.equals(pia.getEuStatus())) {
                throw new BusException("该账户已被冻结，院内就诊卡不可发放！");
            } else {
                throw new BusException("账户数据错误");
            }
        }
        if (IDictCodeConst.DT_CARDTYPE_HOS.equals(picard.getDtCardtype())
                && EnumerateParameter.ONE.equals(ApplicationUtils.getSysparam(
                "PI0006", false))
                && piCardIss.size() > 0) {
            // 更新领卡信息表
            piCardIss.get(0).setCntUse(piCardIss.get(0).getCntUse() - 1);
            if (piCardIss.get(0).getCntUse() == 0) {
                piCardIss.get(0).setFlagUse(EnumerateParameter.ZERO);
                piCardIss.get(0).setFlagActive(EnumerateParameter.ZERO);
                piCardIss.get(0).setEuStatus(EnumerateParameter.TWO);
            } else {
                // 添加参数控制按照领卡表/读卡接口发卡
                // 参数为1时使用领卡表发卡,更新当前卡号
                piCardIss.get(0).setCurNo(
                        Long.valueOf(picard.getCardNo()) + 1);
            }
            DataBaseHelper.updateBeanByPk(piCardIss.get(0), false);
        }

        /**
         * 灵璧项目使用多张卡，和第三方交互，作为查询，故使其他卡片的flag_active='0',只保留当前卡片
         * */

        String sql = " update PI_CARD set FLAG_ACTIVE='0' where DT_CARDTYPE='01' and pk_pi=? and CARD_NO !=?";
        DataBaseHelper.execute(sql, new Object[]{picard.getPkPi(), picard.getCardNo()});

        //推送发卡消息
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        PlatFormSendUtils.sendDstributeCardMsg(paramMap);
        return pamMap;
    }

    private void updateThirdPay(BlDepositPi jkjl){
        if (jkjl!=null&&!CommonUtils.isEmptyString(jkjl.getPayInfo())){
            String sql="update  BL_EXT_PAY set PK_DEPOPI=?,pk_pi=? where  SERIAL_NO=? ";
            DataBaseHelper.update(sql, new Object[]{ jkjl.getPkDepopi(),jkjl.getPkPi(),jkjl.getPayInfo()});
        }

    }

    /***
     * @Description
     * 校验卡号是否有效
     * @auther wuqiang
     * @Date 2019-11-14
     * @Param [param, user]
     * @return void
     */
    public void checkCardVil(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String pkPi = String.valueOf(map.get("pkPi"));
        String cardNo = String.valueOf(map.get("cardNo"));
        if (CommonUtils.isEmptyString(cardNo)) {
            return;
        }
        String yz = "select * from pi_card where FLAG_ACTIVE in ('1','0' )and EU_STATUS in ('0','1','2' ) and card_no=? ";// 检查全局范围内是否存在该卡号
        List<PiCard> yzLst = DataBaseHelper.queryForList(yz, PiCard.class,
                cardNo);
        if (yzLst.size() > 0) {// 数据库中存在卡号，校验，不存在的话可执行发卡操作
            boolean accountCon = true;
            List<String> lStats = new ArrayList<String>();
            String onez = yzLst.get(0).getPkPi();
            if (CommonUtils.isEmptyString(pkPi)) {
                throw new BusException("已存在有效的该卡信息，不可再次添加！");
            }
            for (int i = 0; i < yzLst.size(); i++) {
                if (!onez.equals(yzLst.get(i).getPkPi())) {
                    accountCon = false;// 数据库存在不同账户
                    // 数据库已存在不同账户不执行发卡
                } else {
                    lStats.add(yzLst.get(i).getEuStatus());
                }
            }
            if (accountCon) {// 同一账户,
                if (!pkPi.equals(yzLst.get(0).getPkPi())) {// 校验当前账户是否相同
                    throw new BusException("已有其他账户拥有该卡！");// 否相同
                } else {// 相同，校验作废卡号（非作废不可重复）
                    if (lStats.contains(EnumerateParameter.ZERO)
                            || lStats.contains(EnumerateParameter.ONE)
                            || lStats.contains(EnumerateParameter.TWO)) {
                        throw new BusException("已存在有效的该卡信息，不可再次添加！");
                    }
                }
            } else {
                throw new BusException("已有多个账户拥有该卡，数据错误！");
            }
        }

    }


    /**
     * 交易号：002004001011 患者退卡 1.只有卡片状态不是"作废"状态的才可以退卡 2.将卡片状态改成退卡 3.如果卡片类型 为"01"
     * 院内就诊卡时,需要退给他押金,押金金额取PI0002参数 4.冻结账户不可操作 注：如果发卡后一段时间后改了押金金额，再次退卡的时候金额就不一样了
     */
    public List<BlDepositPi> backCard(String param, IUser user) {
        User uu = (User) user;
        List<BlDepositPi> blDepositPiList = new ArrayList<>(3);
        ChangeCardParam changeCardParam=JsonUtil.readValue(param,ChangeCardParam.class);
        String pkpiCard=changeCardParam.getPkPiCard();
        List<BlDepositPi> blDepositPis =changeCardParam.getBlDepositPiList();

        String sqlForcard = "select * from pi_card where pk_picard=? and pk_org=?";
        PiCard pic = DataBaseHelper.queryForBean(sqlForcard, PiCard.class,
                pkpiCard, uu.getPkOrg());
        if (pic != null && !EnumerateParameter.EIGHT.equals(pic.getEuStatus())
                && !EnumerateParameter.NINE.equals(pic.getEuStatus())) {
            pic.setEuStatus(EnumerateParameter.EIGHT);// 退卡
            pic.setFlagActive("0");// 退卡
            BigDecimal loanAmount = new BigDecimal("0.00");
            pic.setDeposit(loanAmount);//把押金重新定义0
            if (IDictCodeConst.DT_CARDTYPE_HOS.equals(pic.getDtCardtype())) {
                    if ( blDepositPis !=null&& blDepositPis.size()>0) {

                        for (int i = 0; i < blDepositPis.size(); i++) {
                            Map<String, Object> tMap = new HashMap<>();
                            tMap.put("euOptype", "1");
                            tMap.put("dtPaymode", blDepositPis.get(i).getDtPaymode());
                            tMap.put("euDirect", EnumerateParameter.NEGA);
                            tMap.put("pkPi", pic.getPkPi());
                            tMap.put("amount", blDepositPis.get(i).getAmount());
                            tMap.put("datePay", new Date());
                            tMap.put("pkDept", ((User) user).getPkDept());
                            tMap.put("pkEmpPay", ((User) user).getPkEmp());
                            tMap.put("nameEmpPay", ((User) user).getNameEmp());
                            //调用退费/充值接口
                            String strJson = JsonUtil.writeValueAsString(tMap);
                            BlDepositPi bl = this.saveMonOperation(strJson, user);
                            bl.setBlPayTrdVO(blDepositPis.get(i).getBlPayTrdVO());
                            blDepositPiList.add(bl);
                        }
                    }
                } else {
                    throw new BusException("该病人账户已冻结，无法退卡！");
                }

            DataBaseHelper.updateBeanByPk(pic, false);
            PiCardDetail pcd = new PiCardDetail();
            pcd.setPkPicard(pic.getPkPicard());
            pcd.setPkPi(pic.getPkPi());
            pcd.setCardNo(pic.getCardNo());
            pcd.setEuOptype(EnumerateParameter.EIGHT);
            pcd.setPkEmpOpera(uu.getPkEmp());
            pcd.setNameEmpOpera(uu.getNameEmp());
            pcd.setDateHap(new Date());
            DataBaseHelper.insertBean(pcd);
        }
        return blDepositPiList;
    }

    public  List<BlDepositPi> queryRefundAmount( String param,IUser user ) {
        User uu = (User) user;
        String pkpiCard = JSON.parseObject(param).getString("pkPiCard");
        String status = JSON.parseObject(param).getString("status");
        String sqlForcard = "select * from pi_card where pk_picard=? and pk_org=?";
        PiCard pic = DataBaseHelper.queryForBean(sqlForcard, PiCard.class,
                pkpiCard, uu.getPkOrg());
        String piAccInfo = "select * from pi_acc where pk_pi=? ";
        PiAcc pia = DataBaseHelper.queryForBean(piAccInfo, PiAcc.class,
                pic.getPkPi());
        List<BlDepositPi> blDepositPis = new ArrayList<>(3);
        BlDepositPi blDepositPi=new BlDepositPi();
        blDepositPi.setPkPi(pia.getPkPi());
        BigDecimal tAmt=BigDecimal.ZERO;
        //本次退款金额
        //销卡
        if(EnumerateParameter.NINE.equals(status)){
            String balance = "";
            //销卡不退押金，故查出所有的押金
            Map<String, Object> yjMap = DataBaseHelper.queryForMap(
                    "select nvl(sum(pc.deposit)，0) depo_amt from pi_card pc where pc.pk_pi=? and FLAG_ACTIVE in ('0','1') and EU_STATUS in ('0','1','2')"
                    , new Object[]{pic.getPkPi()});
            if (yjMap != null && yjMap.size() > 0) {
                balance = CommonUtils.getString(yjMap.get("depoAmt"));
            }
            if (CommonUtils.isEmptyString(balance)) {
                balance= "0";
            }
             tAmt = pia.getAmtAcc().subtract(BigDecimal.valueOf(Double.valueOf(balance)));


        }
        //退卡
        if (EnumerateParameter.EIGHT.equals(status)){
            //查询押金
            String yj = "";
            //只退一张卡的押金，查询其他押金金额
            Map<String, Object> yjMap = DataBaseHelper.queryForMap(
                    "select sum(pc.deposit) depo_amt from pi_card pc where pc.pk_pi=? and pc. pk_picard!=? and pc.eu_status！='8'"
                    , new Object[]{pic.getPkPi(), pic.getPkPicard()});
            if (yjMap != null && yjMap.size() > 0) {
                yj = CommonUtils.getString(yjMap.get("depoAmt"));
            }
            if (CommonUtils.isEmptyString(yj))
                yj = "0";

            if (pia.getEuStatus() == null
                    || EnumerateParameter.ONE.equals(pia.getEuStatus())) {
                tAmt = pia.getAmtAcc().subtract(BigDecimal.valueOf(Double.valueOf(yj)));}
        }
        if ( BigDecimal.ZERO.compareTo(tAmt)==0){
            return blDepositPis;
        }
        blDepositPi.setAmount(tAmt.multiply(BigDecimal.valueOf(-1D)));
        //计算出退款方式，优先退款现金，优先退还满足金额
        BigDecimal refuamount = blDepositPi.getAmount();
        BlDepositPi blDepositPi1;
        List<Map<String, Object>> userPayRecord = queryPaymodeAndAmount(blDepositPi);
        Map<String, Double> modeAmountMap = new HashMap<>();
        // 得到所有账户中存在的付款方式及金额
        for (Map<String, Object> record : userPayRecord) {
            String paymode = CommonUtils.getString(record.get("paymode"));
            double payModeAmount = CommonUtils.getDouble(record.get("amount"));
            if (payModeAmount > 0) {
                blDepositPi1 = new BlDepositPi();
                blDepositPi1.setPkPi(blDepositPi.getPkPi());
                blDepositPi1.setDtPaymode(paymode);
                int i = refuamount.add(new BigDecimal(payModeAmount)).compareTo(BigDecimal.ZERO);
                //
                if (i == 0 || i == 1) {
                    blDepositPi1.setAmount(refuamount);
                    if (paymode.equals(IDictCodeConst.ALI) || paymode.equals(IDictCodeConst.WECHAT)) {
                        //组装第三方订单
                        blDepositPi1 = thirdPaymode(blDepositPi1);
                        blDepositPis.add(blDepositPi1);
                        break;
                    } else {
                        blDepositPis.add(blDepositPi1);
                        break;
                    }
                } else {
                    //不够支付
                    blDepositPi1.setAmount(new BigDecimal(payModeAmount).negate());
                    refuamount = new BigDecimal(payModeAmount).add(refuamount);
                    if (paymode.equals(IDictCodeConst.ALI) || paymode.equals(IDictCodeConst.WECHAT)) {
                        //组装第三方订单
                        blDepositPi1 = thirdPaymode(blDepositPi1);
                        blDepositPis.add(blDepositPi1);
                    } else {
                        blDepositPis.add(blDepositPi1);

                    }
                }
            }
        }

        return blDepositPis;


    }

    /**
     * 交易号：002004001012 1.注意判断当前卡状态 2.卡操作流水
     *
     * @param  ，status
     * @param user
     */
    public List<BlDepositPi> changeCardStatus(String param, IUser user) {
        User uu = (User) user;
        ChangeCardParam changeCardParam=JsonUtil.readValue(param,ChangeCardParam.class);
        String pkpiCard=changeCardParam.getPkPiCard();
        String status=changeCardParam.getStatus();
        List<BlDepositPi> blDepositPis =changeCardParam.getBlDepositPiList();
        List<BlDepositPi> blDepositPiList = new ArrayList<>(3);
        // 查询当前卡状态
        String cardsql = "select * from pi_card where pk_picard=? and pk_org=?";
        PiCard card = DataBaseHelper.queryForBean(cardsql, PiCard.class,
                pkpiCard, uu.getPkOrg());
        BigDecimal cardYj=card.getDeposit();
        if (EnumerateParameter.ZERO.equals(card.getEuStatus())) {
            if (EnumerateParameter.NINE.equals(status)
                    || EnumerateParameter.ONE.equals(status)
                    || EnumerateParameter.TWO.equals(status)
                    || EnumerateParameter.EIGHT.equals(status)) {
                card.setEuStatus(status);
                card.setFlagActive("0");
                BigDecimal loanAmount = new BigDecimal("0.00");
                if (!EnumerateParameter.ONE.equals(status))//挂失时预交金不需要清0
                    card.setDeposit(loanAmount);
            }
            if (EnumerateParameter.NINE.equals(status)) {
                /**销卡时退余额*/
                if (blDepositPis !=null&& blDepositPis.size()>0) {
                    for (int i = 0; i < blDepositPis.size(); i++) {
                        Map<String, Object> tMap = new HashMap<>();
                        tMap.put("euOptype", "1");
                        tMap.put("dtPaymode",  blDepositPis.get(i).getDtPaymode());
                        tMap.put("euDirect", EnumerateParameter.NEGA);
                        tMap.put("pkPi", card.getPkPi());
                        tMap.put("amount",  blDepositPis.get(i).getAmount());
                        tMap.put("datePay", new Date());
                        tMap.put("pkDept", ((User) user).getPkDept());
                        tMap.put("pkEmpPay", ((User) user).getPkEmp());
                        tMap.put("nameEmpPay", ((User) user).getNameEmp());
                        //调用退费/充值接口
                        String strJson = JsonUtil.writeValueAsString(tMap);
                        BlDepositPi bl = saveMonOperation(strJson, user);//第一次退款 余额-押金 需要退给患者的钱
                        bl.setBlPayTrdVO(blDepositPis.get(i).getBlPayTrdVO());
                        blDepositPiList.add(bl);
                    }
                }
                if (BigDecimal.ZERO.compareTo(cardYj)<0){
                    BigDecimal tyj =cardYj.multiply(BigDecimal.valueOf(-1D));
                    if (tyj.compareTo(BigDecimal.valueOf(0D)) != 0) {
                        Map<String, Object> tsMap = new HashMap<>();
                        tsMap.put("euOptype", "1");
                        tsMap.put("dtPaymode", "1");
                        tsMap.put("euDirect", EnumerateParameter.NEGA);
                        tsMap.put("pkPi", card.getPkPi());
                        tsMap.put("amount", tyj);//押金
                        tsMap.put("datePay", new Date());
                        tsMap.put("pkDept", ((User) user).getPkDept());
                        tsMap.put("pkEmpPay", ((User) user).getPkEmp());
                        tsMap.put("nameEmpPay", ((User) user).getNameEmp());
                        tsMap.put("dtPaymode", EnumerateParameter.ONE);
                        String strtJson = JsonUtil.writeValueAsString(tsMap);
                        this.saveMonOperation(strtJson, user);//第二次退款 押金 实际并没有退给患者的钱，但是表里写记录
                        //调用非就诊记费接口
                        BlPi blPi = new BlPi();
                        blPi.setPkPi(card.getPkPi());
                        blPi.setNameBl("被销卡片押金");
                        blPi.setPrice(Double.valueOf(String.valueOf(card.getDeposit())));
                        blPi.setQuan(Double.valueOf(EnumerateParameter.ONE));
                        blPi.setAmount(Double.valueOf(String.valueOf(card.getDeposit()))
                                * Double.valueOf(EnumerateParameter.ONE));
                        blPi.setFlagPd(EnumerateParameter.ZERO); //
                        blPi.setDtPaymode(EnumerateParameter.ONE);
                        blPi.setEuButype(EnumerateParameter.ZERO);
                        blPi.setFlagCc(EnumerateParameter.ZERO);
                        nonTreatment(blPi, user);
                }

                 }

            }
        } else if (EnumerateParameter.ONE.equals(card.getEuStatus())) {
            if (EnumerateParameter.ZERO.equals(status)) {
                card.setEuStatus(status);
                card.setFlagActive("1");
            } else if (EnumerateParameter.NINE.equals(status) || EnumerateParameter.TWO.equals(status)) {
                card.setEuStatus(status);
                card.setFlagActive("0");
                if (blDepositPis !=null&& blDepositPis.size()>0) {
                    for (int i = 0; i < blDepositPis.size(); i++) {
                        Map<String, Object> tMap = new HashMap<>();
                        tMap.put("euOptype", "1");
                        tMap.put("dtPaymode",  blDepositPis.get(i).getDtPaymode());
                        tMap.put("euDirect", EnumerateParameter.NEGA);
                        tMap.put("pkPi", card.getPkPi());
                        tMap.put("amount", blDepositPis.get(i).getAmount());
                        tMap.put("datePay", new Date());
                        tMap.put("pkDept", ((User) user).getPkDept());
                        tMap.put("pkEmpPay", ((User) user).getPkEmp());
                        tMap.put("nameEmpPay", ((User) user).getNameEmp());

                        //调用退费/充值接口
                        String strJson = JsonUtil.writeValueAsString(tMap);
                        BlDepositPi bl = saveMonOperation(strJson, user);
                        bl.setBlPayTrdVO(blDepositPis.get(i).getBlPayTrdVO());
                        blDepositPiList.add(bl);
                    }
                }


                if (BigDecimal.ZERO.compareTo(cardYj)<0){
                BigDecimal tyj =cardYj.multiply(BigDecimal.valueOf(-1D));
                 if (tyj.compareTo(BigDecimal.valueOf(0D)) != 0) {
                 Map<String, Object> tsMap = new HashMap<>();
                 tsMap.put("euOptype", "1");
                 tsMap.put("dtPaymode", "1");
                 tsMap.put("euDirect", EnumerateParameter.NEGA);
                 tsMap.put("pkPi", card.getPkPi());
                 tsMap.put("amount", tyj);//押金
                 tsMap.put("datePay", new Date());
                 tsMap.put("pkDept", ((User) user).getPkDept());
                 tsMap.put("pkEmpPay", ((User) user).getPkEmp());
                 tsMap.put("nameEmpPay", ((User) user).getNameEmp());
                 tsMap.put("dtPaymode", EnumerateParameter.ONE);
                 String strtJson = JsonUtil.writeValueAsString(tsMap);
                 this.saveMonOperation(strtJson, user);//第二次退款 押金 实际并没有退给患者的钱，但是表里写记录

                 //调用非就诊记费接口
                 BlPi blPi = new BlPi();
                 blPi.setPkPi(card.getPkPi());
                 blPi.setNameBl("被销卡片押金");
                 blPi.setPrice(Double.valueOf(String.valueOf(cardYj)));
                 blPi.setQuan(Double.valueOf(EnumerateParameter.ONE));
                 blPi.setAmount(Double.valueOf(String.valueOf(cardYj))
                 * Double.valueOf(EnumerateParameter.ONE));
                 blPi.setFlagPd(EnumerateParameter.ZERO); //
                 blPi.setDtPaymode(EnumerateParameter.ONE);
                 blPi.setEuButype(EnumerateParameter.ZERO);
                 blPi.setFlagCc(EnumerateParameter.ZERO);
                 nonTreatment(blPi, user);}
                 }

            } else {
                throw new BusException("该卡处于挂失状态，不可退卡！");
            }
        } else if (EnumerateParameter.TWO.equals(card.getEuStatus())) {
            if (EnumerateParameter.EIGHT.equals(status)
                    || EnumerateParameter.NINE.equals(status)) {
                card.setEuStatus(status);
            } else if (EnumerateParameter.ZERO.equals(card.getEuStatus())) {
                throw new BusException("该卡已到期，不可使用该卡！");
            } else {
                throw new BusException("该卡已到期，不可挂失！");
            }
        }
        DataBaseHelper.updateBeanByPk(card, false);
        // DataBaseHelper.update("update pi_card_detail set eu_optype='"+status+"' where pk_picard=?",
        // new Object[]{card.getPkPicard()});
        PiCardDetail pcd = new PiCardDetail();
        pcd.setPkPicard(card.getPkPicard());
        pcd.setPkPi(card.getPkPi());
        pcd.setCardNo(card.getCardNo());
        // 如果参数 status 为0,说明是启用状态（该处不存在发卡状态）
        if (PiCardDetail.CARD_EU_OPTYPE_0.equals(status)) {
            status = PiCardDetail.CARD_EU_OPTYPE_2;
        }
        pcd.setEuOptype(status);
        pcd.setPkEmpOpera(uu.getPkEmp());
        pcd.setNameEmpOpera(uu.getNameEmp());
        pcd.setDateHap(new Date());
        DataBaseHelper.insertBean(pcd);
        return blDepositPiList;
    }

    /**
     * 交易号：002004001013 获取当前收据号
     *
     * @param param
     * @param user
     * @return
     */
    public String getNowSjh(String param, IUser user) {
        return ApplicationUtils.getCode("0603");// 收据号
    }

    /**
     * 交易号：002004001014 新增信用额度 冻结账户不可操作
     *
     * @param param
     * @param user
     */
    public void saveNeCredit(String param, IUser user) {
        PiAccCredit piAccd = JsonUtil.readValue(param, PiAccCredit.class);
        // 获取账户id
        String getaccId = "select * from pi_acc where pk_pi=?";
        PiAcc pk_acc = DataBaseHelper.queryForBean(getaccId, PiAcc.class,
                piAccd.getPkPi());
        if (pk_acc.getEuStatus() == null
                || EnumerateParameter.ONE.equals(pk_acc.getEuStatus())) {
            BigDecimal leftamt = BigDecimal.ZERO;
            if (pk_acc.getCreditAcc() == null
                    || "".equals(pk_acc.getCreditAcc())) {
                pk_acc.setCreditAcc(BigDecimal.ZERO);
            }
            leftamt = pk_acc.getCreditAcc().add(piAccd.getAmtCredit());
            if (leftamt.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusException("信用额度小于0！");
            } else {
                pk_acc.setCreditAcc(leftamt);
            }
            DataBaseHelper.updateBeanByPk(pk_acc, false);
            piAccd.setPkPiacc(pk_acc.getPkPiacc().toString());
            piAccd.setPkEmpOpera(((User) user).getPkEmp());
            piAccd.setNameEmpOpera(((User) user).getNameEmp());
            piAccd.setDateOpera(new Date());
            DataBaseHelper.insertBean(piAccd);
        } else {
            throw new BusException("该账户已冻结，不可新增信用");
        }
    }

    /**
     * 交易接口号：002004001015 新增收退款 冻结账户不可操作
     * 往pi_acc,pi_acc_detail,bl_deposit_pi表里面插入数据
     *
     * @param param
     * @param user
     */
    public BlDepositPi saveMonOperation(String param, IUser user) {
        String euopType = JsonUtil.getFieldValue(param, "euOptype");
        String dtPaymode = JsonUtil.getFieldValue(param, "dtPaymode");
        BlDepositPi dp = JsonUtil.readValue(param, BlDepositPi.class);
        List<BlExtPayBankVo> blPayTrdVO = dp.getBlPayTrdVO();
        if (dp == null || dtPaymode == null) {
            throw new BusException("必传参数为空");
        }

        /**
         * 数据校验: 1）金额必须大于0； 2）如果收付款方式为“银行卡”或“支票”，必须输入银行和银行卡号；
         * 3）退款时的收付款方式处理（为规避套现行为，要求退款时的支付方式尽量和收款时的支付方式一致）；
         */
        if (dp.getAmount().abs().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusException("金额必须大于0");
        }
        boolean euDirect_in = dp.getEuDirect().equals(EnumerateParameter.ONE);
        // 收款
        if (euDirect_in) {
            // 假设收款方式 现金1 支票2 银行卡3 账户4
            boolean isBankOrCheck = dp.getDtPaymode().equals(
                    EnumerateParameter.TWO)
                    || dp.getDtPaymode().equals(EnumerateParameter.THREE);
            if (isBankOrCheck) {
                String dtBank = dp.getDtBank(); // 银行档案
                String bankNo = dp.getBankNo(); // 银行卡号
                boolean bankIsNull = StringUtils.isEmpty(dtBank)
                        || StringUtils.isEmpty(bankNo);
                if (bankIsNull) {
                    throw new BusException("必须输入银行和银行卡号");
                }
            }
        }


        return jsservice.saveMonOperation(dp, user, null, euopType, dtPaymode);
    }

    /**
     * 交易号：002004001017 患者账户冻结和解冻 如果eu_status字段为空，默认设置为1 ： 有效
     *
     * @param param
     * @param user
     */
    public void changeAccStatus(String param, IUser user) {
        PiAcc piac = JsonUtil.readValue(param, PiAcc.class);
        String etAcc = "select * from pi_acc where pk_pi=?";
        PiAcc dbAcc = DataBaseHelper.queryForBean(etAcc, PiAcc.class,
                piac.getPkPi());
        if (dbAcc.getEuStatus() == null || "".equals(dbAcc.getEuStatus()))
            dbAcc.setEuStatus(EnumerateParameter.ONE);
        if (EnumerateParameter.NINE.equals(dbAcc.getEuStatus())) {// 作废
            throw new BusException("该账户已作废，不可做解冻，冻结操作！");
        } else if (dbAcc.getEuStatus().equals(piac.getEuStatus())) {
            if (EnumerateParameter.ONE.equals(dbAcc.getEuStatus())) {
                throw new BusException("该账户已为有效状态，无需解冻！");
            } else if (EnumerateParameter.TWO.equals(dbAcc.getEuStatus())) {
                throw new BusException("该账户已为冻结状态，无需再次冻结！");
            }
        }
        dbAcc.setEuStatus(piac.getEuStatus());
        DataBaseHelper.updateBeanByPk(dbAcc, false);
    }

    /**
     * 插入卡详细信息押金值
     *
     * @param pia
     * @param jkjl
     */
    public static void piAccDetailVal(PiAcc pia, BlDepositPi jkjl, PiCard piCard) {
        PiAccDetail pad = new PiAccDetail();
        pad.setPkPiacc(pia.getPkPiacc());
        pad.setPkPi(pia.getPkPi());
        pad.setDateHap(new Date());
        pad.setEuOptype(EnumerateParameter.NINE);
        pad.setEuDirect(jkjl.getEuDirect());
        pad.setAmount(jkjl.getAmount());
        pad.setPkDepopi(jkjl.getPkDepopi());
        pad.setAmtBalance(pia.getAmtAcc());
        pad.setPkEmpOpera(jkjl.getPkEmpPay());
        pad.setNameEmpOpera(jkjl.getNameEmpPay());
        //针对灵璧项目，押金第三方退款，不保存押金号，无法确定押金交款方式，咨询成哥后，故备注使用填写押金号
        pad.setNote(piCard == null ? null : piCard.getCardNo());
        DataBaseHelper.insertBean(pad);
    }

    /**
     * 查询可用的领卡信息
     */
    public List<PiCardIss> getPiCardIss(String param, IUser user) {
        PiCardIss cardiss = JsonUtil.readValue(param, PiCardIss.class);
        cardiss.setPkOrg(UserContext.getUser().getPkOrg());
        List<PiCardIss> list = cardDealMapper.getPiCardIss(cardiss);
        return list;
    }

    /**
     * 校验退款金额是否合法
     * 交易号：002004001025
     *
     * @param param
     * @param user
     */


    public void checkAmt(String param, IUser user) {
        BlDepositPi dp = JsonUtil.readValue(param, BlDepositPi.class);
        if (dp != null && dp.getAmount().compareTo(BigDecimal.valueOf(0D)) != 0) {
            //除了退卡外其他押金均不可以退还-查询卡押金
            PiCard piCard = DataBaseHelper.queryForBean(
                    "select NVL(sum(DEPOSIT),0) as DEPOSIT  from PI_CARD where EU_STATUS in('0','1','2','9') and pk_pi = ?",
                    PiCard.class, new Object[]{dp.getPkPi()});
            //账户余额
            PiAcc pkc = DataBaseHelper.queryForBean(
                    "select * from pi_acc where pk_pi=?", PiAcc.class, dp.getPkPi());
            //计算账户可退余额
            BigDecimal amt = pkc.getAmtAcc().subtract(piCard.getDeposit());
            if (amt.add(dp.getAmount()).compareTo(BigDecimal.valueOf(0D)) < 0) {
                throw new BusException("退款金额大于账户余额，请重新输入(押金" + piCard.getDeposit() + "元不可退)！");
            }
        }
    }

    /**
     * 002004001019 退款方式预结算接口
     *
     * @return 账户中余额的组成方式：现金和第三方支付
     */
    public BlDepositPi getDtPaymodeAmount(String param, IUser user) {
        BlDepositPi dp = JsonUtil.readValue(param, BlDepositPi.class);
        List<Map<String, Object>> userPayRecord = queryPaymodeAndAmount(dp);
        BlDepositPi blDepositPi = new BlDepositPi();
        Map<String, Double> modeAmountMap = new HashMap<>();
        // 得到所有账户中存在的付款方式及金额
        for (Map<String, Object> record : userPayRecord) {
            String paymode = CommonUtils.getString(record.get("paymode"));
            String name = switchNameUseCode(paymode);
            double payModeAmount = CommonUtils.getDouble(record.get("amount"));

            if (payModeAmount > 0) {
                modeAmountMap.put(name, payModeAmount);
            }
        }
        // 校验退款的金额 > 此退款方式下充值金额 -- 抛出异常信息
        validateAmountIsEnough(dp, userPayRecord, modeAmountMap);

        switch (dp.getDtPaymode()) {
            case IDictCodeConst.CASH:
                // 现金方式 1
                blDepositPi = new BlDepositPi();
                blDepositPi.setDtPaymode(IDictCodeConst.CASH);
                blDepositPi.setAmount(dp.getAmount());
                return blDepositPi;
            case IDictCodeConst.BANKCARD:
            case IDictCodeConst.WECHAT:
            case IDictCodeConst.ALI:

            default:
                // 支付宝、银行卡、微信
                blDepositPi = thirdPaymode(dp);

                break;
        }


        return blDepositPi;
    }


    /*
     * 校验退款的金额 > 此退款方式下充值金额 -- 抛出异常信息
     */
    private void validateAmountIsEnough(BlDepositPi dp,
                                        List<Map<String, Object>> userPayRecord,
                                        Map<String, Double> modeAmountMap) {
        for (Map<String, Object> record : userPayRecord) {
            String paymode = CommonUtils.getString(record.get("paymode"));
            BigDecimal payModeAmount = new BigDecimal(
                    record.get("amount") == null ? "0" : record.get("amount")
                            .toString());
            if (paymode.equals(dp.getDtPaymode())) {
                // 检查支付方式下 当前退款金额与充值金额
                checkDtPayModeAmount(dp, payModeAmount, modeAmountMap);
            }
        }
    }

    /*
     * 检查收款支付方式与退款金额是否
     */
    private void checkDtPayModeAmount(BlDepositPi dp, BigDecimal payAmount,
                                      Map<String, Double> modeAmountMap) {
        // 用计算结果比对当前支付方式下的退款金额，如果之前收款支付方式下的金额小于当前退款支付方式下的金额，提示用户修改支付方式。
        if (dp.getAmount().abs().compareTo(payAmount) > 0) {
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("当前支付方式下可退余额不足，请修改退款方式！");
            sBuffer.append("\r\n");
            sBuffer.append("\r\n");
            Set<Entry<String, Double>> entrySet = modeAmountMap.entrySet();
            for (Entry<String, Double> entry : entrySet) {
                sBuffer.append(
                        " " + entry.getKey() + "可退余额总数 : " + entry.getValue()
                                + " 元").append("\r\n");
                sBuffer.append("\r");
            }
            throw new BusException(sBuffer.toString());
        }
    }


    /*
     * 账户充值为第三方支付
     */
    private BlDepositPi thirdPaymode(BlDepositPi dp) {

        List<BlExtPayBankVo> blExtPayBankVos = new ArrayList<>();//第三方退款信息
        BigDecimal remainingRefund = dp.getAmount();//剩余退款金额，用作比较
        String sql = " select * from BL_EXT_PAY where PK_DEPOPI is not null and EU_PAYTYPE=? and  pk_pi=?  and amount > 0 order by  TS DESC";
        List<BlExtPay> blExtPayList = DataBaseHelper.queryForList(sql, BlExtPay.class, dp.getDtPaymode(), dp.getPkPi());
        if (CollectionUtils.isEmpty(blExtPayList)) {
            throw new BusException(switchNameUseCode(dp.getDtPaymode())
                    + "充值记录为空");
        }
        for (BlExtPay blExtPay : blExtPayList) {

            String tradeNoSql = "select sum(AMOUNT) as AMOUNT, SERIAL_NO  from BL_EXT_PAY where  FLAG_PAY='1' and SERIAL_NO=? group by SERIAL_NO ";
            BlExtPay refBl = DataBaseHelper.queryForBean(tradeNoSql, BlExtPay.class, blExtPay.getTradeNo());
            //不存在退款订单
            if (refBl == null) {
                int i = blExtPay.getAmount().add(remainingRefund).compareTo(BigDecimal.ZERO);
                BlExtPayBankVo blExtPayBankVo = new BlExtPayBankVo();
                if (i == 1 || i == 0) {
                    //足够退款，不进行运算
                    blExtPayBankVo.setAmount(remainingRefund.abs());
                    blExtPayBankVo.setOldBankCode(blExtPay.getPkExtpay());
                    blExtPayBankVos.add(blExtPayBankVo);
                    break;
                } else {
                    //
                    blExtPayBankVo.setAmount(blExtPay.getAmount().abs());
                    blExtPayBankVo.setOldBankCode(blExtPay.getPkExtpay());
                    blExtPayBankVos.add(blExtPayBankVo);
                    remainingRefund = blExtPay.getAmount().add(remainingRefund);
                }

            } else {//存在退款订单

                int i = blExtPay.getAmount().add(remainingRefund).add(refBl.getAmount()).compareTo(BigDecimal.ZERO);
                BlExtPayBankVo blExtPayBankVo = new BlExtPayBankVo();
                if (i == 1 || i == 0) {
                    //足够退款，不进行运算
                    blExtPayBankVo.setAmount(remainingRefund.abs());
                    blExtPayBankVo.setOldBankCode(blExtPay.getPkExtpay());
                    blExtPayBankVos.add(blExtPayBankVo);
                    break;
                } else {
                    //
                    blExtPayBankVo.setAmount(blExtPay.getAmount().add(refBl.getAmount()).abs());
                    blExtPayBankVo.setOldBankCode(blExtPay.getPkExtpay());
                    blExtPayBankVos.add(blExtPayBankVo);
                    remainingRefund = blExtPay.getAmount().add(refBl.getAmount()).add(remainingRefund);
                }

            }


        }

        dp.setBlPayTrdVO(blExtPayBankVos);
        return dp;
    }

    /*
     * 返回订单支付时的金额、剩余可退金额、最新refund_id、订单号 blTemps: 目前所有大于0的支付记录 blExtPays：
     * 目前所有大于0的支付记录中存在的已经部分退费的订单记录
     */
    private List<BlDepositPi> partTradeNoCreateBlDeposit(
            List<BlDepositPi> blTemps, List<BlExtPay> blExtPays) {
        List<BlDepositPi> blDepositPis = new ArrayList<>();
        for (BlDepositPi blTemp : blTemps) {
            String payInfo = blTemp.getPayInfo();
            BlExtPayBankVo payTrdVo = blTemp.getBlPayTrdVO().get(0);
            BigDecimal amount = blTemp.getAmount();
            String serialNo = payInfo; // 默认是商品订单号,当发生退款时变更为退款id;当账单多笔部分退，为最近一次退款id

            // 遍历部分退款的账单：取账单剩余金额和最近一个退款账单的返回refund_id
            for (int i = 0; i < blExtPays.size(); i++) {
                BlExtPay blExtPay = blExtPays.get(i);
                if (payInfo.equals(blExtPay.getOutTradeNo())) {
                    amount = amount.add(blExtPay.getAmount()); // 负值，累减得到当前可退的总金额
                    serialNo = getLastRefundId(blExtPay);
                }
                blTemp.setSerialNo(blExtPay.getSerialNo());
                blTemp.setPayResult(blExtPay.getPayResult());
                blTemp.setAmountThird(blExtPay.getAmount());
                blTemp.setOutTradeNo(blExtPay.getOutTradeNo());
            }

            payTrdVo.setAmount(amount); // 剩余的可退金额
            payTrdVo.setOldBankCode(payInfo); // 支付时的商品订单号
            payTrdVo.setNewBankCode(serialNo); // 最近一笔退费单号
            blDepositPis.add(blTemp);
        }
        return blDepositPis;
    }

    /*
     * 获取最近一次的退款id
     */
    private String getLastRefundId(BlExtPay blExtPay) {
        Date returnDate = new Date();
        String serialNo = "";
        if (blExtPay.getDatePay().before(returnDate)) {
            returnDate = blExtPay.getDatePay();
            // 取到最近一笔退款的id
            serialNo = blExtPay.getSerialNo();
        }
        return serialNo;
    }

    /*
     * 根据账户支付方式构造BlDepositPi
     */
    private List<BlDepositPi> createBlDepositPi(
            List<BlDepositPi> trdBlDeposits, BlDepositPi dp) {
        List<BlDepositPi> blDePiList = new ArrayList<>();
        BigDecimal blAmount = BigDecimal.ZERO;
        String payInfoTemp = trdBlDeposits.get(0).getPayInfo();
        BlExtPay blExtPay = DataBaseHelper.queryForBean(
                "select * from bl_ext_pay where SERIAL_NO = ?", BlExtPay.class,
                payInfoTemp);
        outer:
        for (BlDepositPi trdBlDeposit : trdBlDeposits) {
            blAmount = blAmount.add(trdBlDeposit.getAmount());
            do {
                // 第三方支付方式
                BlDepositPi blDepositPi = new BlDepositPi();

                List<BlExtPayBankVo> blTrdVos = createBlExtPayTrdVo(dp,
                        trdBlDeposit, blDepositPi);
                blDepositPi.setBlPayTrdVO(blTrdVos);

                blDepositPi.setAmount(trdBlDeposit.getAmount());
                blDepositPi.setPayInfo(trdBlDeposit.getPayInfo());
                blDepositPi.setDtPaymode(dp.getDtPaymode());
                blDepositPi.setSerialNo(blExtPay.getSerialNo());
                blDepositPi.setPayResult(blExtPay.getPayResult());
                blDepositPi.setAmountThird(blExtPay.getAmount());
                blDepositPi.setOutTradeNo(blExtPay.getOutTradeNo());
                blDePiList.add(blDepositPi);
                // 下面代码是为了取出刚好大于退款金额的充值记录 比如最近两次充值：最后一次50 上一次100
                // 退款金额80，要把这两条记录都拿到
                if (dp.getAmount().abs().compareTo(blAmount) <= 0) {
                    break outer;
                }
                continue outer;
            } while (true);
        }
        return blDePiList;
    }

    /*
     * 创建第三方支付时新旧交易码及金额（银行卡支付还需要银行交易时间）
     */
    private List<BlExtPayBankVo> createBlExtPayTrdVo(BlDepositPi dp,
                                                     BlDepositPi trdBlDeposit, BlDepositPi blDepositPi) {
        List<BlExtPayBankVo> blTrdVos = new ArrayList<>();
        BlExtPayBankVo blTrdVo = new BlExtPayBankVo();
        blTrdVo.setAmount(trdBlDeposit.getAmount());
        blTrdVo.setNewBankCode(trdBlDeposit.getPayInfo());
        blTrdVo.setOldBankCode(trdBlDeposit.getPayInfo());
        if (dp.getDtPaymode().equals(IDictCodeConst.BANKCARD)) {
            blDepositPi.setBankTime(trdBlDeposit.getDatePay());
            blTrdVo.setBankTime(trdBlDeposit.getDatePay());
        }
        blTrdVos.add(blTrdVo);
        return blTrdVos;
    }

    /**
     * 查询最近的账户余额为0的充值组成
     *
     * @param dp
     * @return
     */
    private List<Map<String, Object>> queryPaymodeAndAmount(BlDepositPi dp) {
        Map<String, Object> datePayMap = DataBaseHelper
                .queryForMap(
                        "select max(date_hap) as datePay from pi_acc_detail piacc where piacc.pk_pi= ? and piacc.amt_balance = 0",
                        new Object[]{dp.getPkPi()});
        List<Map<String, Object>> userPayRecord = null;
        String datePay = CommonUtils.getString(datePayMap.get("datePay"));
        // 数据库中存在当账户为0的情况 ，取时间
        if (datePay != null) {
            Date lastZeroDate = DateUtils.strToDate(datePay,
                    "yyyy-MM-dd hh:mm:ss");
            userPayRecord = DataBaseHelper
                    .queryForList(
                            "select depo.dt_paymode as paymode,sum(depo.amount) amount from bl_deposit_pi depo "
                                    + " where depo.pk_pi = ? and depo.date_pay > ? group by dt_paymode",
                            new Object[]{dp.getPkPi(), lastZeroDate});
        } else {
            // 数据库中存在当账户不为0的情况 ，不过滤时间
            userPayRecord = DataBaseHelper
                    .queryForList(
                            "select depo.dt_paymode as paymode,sum(depo.amount) amount from bl_deposit_pi depo "
                                    + " where depo.pk_pi = ? group by dt_paymode",
                            new Object[]{dp.getPkPi()});
        }
        return userPayRecord;
    }

    /**
     * 做支付方式和支付名称的对应字典
     *
     * @param num
     * @return
     */
    private String switchNameUseCode(String num) {
        String name = "";
        switch (num) {
            case "1":
                name = "现金";
                break;
            case "2":
                name = "支票";
                break;
            case "3":
                name = "银行卡";
                break;
            case "5":
                name = "内部转账";
                break;
            case "6":
                name = "单位记账";
                break;
            case "7":
                name = "微信";
                break;
            case "8":
                name = "支付宝";
                break;
            default:
                name = "其他";
                break;
        }
        return name;
    }

    /**
     * @param user
     * @param blPi 非就诊计费接口
     */
    public void nonTreatment(BlPi blPi, IUser user) {
        blPi.setCodeBl(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
        blPi.setDateBl(new Date());
        blPi.setPkDeptBl(((User) user).getPkDept());
        blPi.setPkEmpBl(((User) user).getPkEmp());
        blPi.setNameEmpBl(((User) user).getNameEmp());
        ApplicationUtils.setDefaultValue(blPi, true);
        DataBaseHelper.insertBean(blPi);
    }

    /**
     * 查询卡操作记录
     * 交易号：002004001024
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryCardActionRec(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("pkOrg", UserContext.getUser().getPkOrg());

        List<Map<String, Object>> list = cardDealMapper.qryCardActionRec(paramMap);

        return list;
    }

    /**
     * 领卡记录中的当前卡号
     * @param param
     * @param user
     * @return
     */
    public String  qryIssuingCard(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
        String curNo = "";
        Map<String, Object> list = cardDealMapper.getCardNo(paramMap);
        if (list != null){
            curNo = list.get("curNo").toString();
        }
        return curNo;
    }
}