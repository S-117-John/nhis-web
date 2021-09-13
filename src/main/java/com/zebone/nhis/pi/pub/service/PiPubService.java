package com.zebone.nhis.pi.pub.service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.pi.*;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.constant.IsAdd;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pi.pub.dao.PiPubMapper;
import com.zebone.nhis.pi.pub.support.ClientUtils;
import com.zebone.nhis.pi.pub.vo.CommonParam;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.nhis.pi.pub.vo.PibaseVo;
import com.zebone.nhis.pi.pub.vo.PvDiagVo;
import com.zebone.nhis.pv.pub.dao.RegPubMapper;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PiPubService {

    @Resource
    private PiPubMapper piPubMapper;

    @Autowired
    private RegPubMapper regPubMapper;

    @Autowired
    private CgQryMaintainService cgQryMaintainService;

    @Autowired
    private OpCgPubService opCgPubService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private PiCodeFactory piCodeFactory;
    // 用来控制手动事物
    @Resource(name = "transactionManager")
    private PlatformTransactionManager platformTransactionManager;

    private Logger logger = LoggerFactory.getLogger("com.zebone");


    /**
     * 插入卡详细信息押金值 有操作pk_pv的费用记录需要将pk_pv传入，否则null EuOptype类型 1充值 2账户消费 3信用消费 9退费
     * 信用额度的增加和减少从pi_acc_credit中获取 退费时金额不能超过账户余额，并且不能超过同类型累计充值总额 需要在外部做校验
     *
     * @param pia
     * @param jkjl
     */
    public void piAccDetailVal(PiAcc pia, BlDepositPi jkjl, String pkPv,
                               String euOptype) {
        DataBaseHelper.updateBeanByPk(pia, false);
        PiAccDetail pad = new PiAccDetail();
        pad.setPkPiacc(pia.getPkPiacc());
        pad.setPkPi(pia.getPkPi());
        pad.setDateHap(new Date());
        // pad.setEuOptype(EnumerateParameter.ONE);
        if (pkPv != null)
            pad.setPkPv(pkPv);
        if (euOptype != null) {
            if (EnumerateParameter.NINE.equals(euOptype)) {// 退费
                if (pia.getAmtAcc().compareTo(jkjl.getAmount()) < 0) {
                    throw new BusException("退款金额不可超过账户余额。");
                }
            }
            pad.setEuOptype(euOptype);
        }
        pad.setEuDirect(jkjl.getEuDirect());
        pad.setAmount(jkjl.getAmount());
        pad.setPkDepopi(jkjl.getPkDepopi());
        pad.setAmtBalance(pia.getAmtAcc());
        pad.setPkEmpOpera(jkjl.getPkEmpPay());
        pad.setNameEmpOpera(jkjl.getNameEmpPay());
        DataBaseHelper.insertBean(pad);
    }

    /**
     * 保存患者信息<br>
     * 交易号：<br>
     *
     * <pre>
     * 1 保存患者基本信息;
     * 2 保存患者医保计划，缺省标志为1的保存主医保，其他保存辅助医保;
     * 3 保存患者家庭关系;
     * 4 保存患者地址;
     * 5 保存患者过敏史;
     * 6 保存患者疾病史;
     * 7 保存患者账户信息
     * 8保存卡信息列表,cardList为null 时不操作对应数据;
     * </pre>
     *
     * @param param
     * @return user
     * @throws
     * @author wangpeng
     * @date 2016年9月14日
     */
    public PiMaster savePiMasterParam(String param, IUser user) {
        PiMasterParam mParam = JsonUtil.readValue(param, PiMasterParam.class);
        Map<String, Object> msgParam = new HashMap<String, Object>();
        if (mParam.getMaster() != null && StringUtils.isEmpty(mParam.getMaster().getPkPi())) {
            msgParam.put("isAdd", IsAdd.ADD);
        } else {
            msgParam.put("isAdd", IsAdd.UPDATE);
        }

        PiMaster master = savePiMasterParam(mParam);

        //发送患者信息至主索引系统

        master.setPiList((List<Map<String, Object>>) ClientUtils.addEmpi(master));

        //发送患者信息
        msgParam.put("pkPi", master.getPkPi());
        msgParam.put("pkEmp", UserContext.getUser().getPkEmp());
        msgParam.put("nameEmp", UserContext.getUser().getNameEmp());
        msgParam.put("codeEmp", UserContext.getUser().getCodeEmp());
        msgParam.put("pi", master.getPkPi());
        PlatFormSendUtils.sendPiMasterMsg(msgParam);


        return master;
    }


    // 无名氏保存患者
    public PiMaster savePiMaster(String param, IUser user) {
        PiMaster piMaster = JsonUtil.readValue(param, PiMaster.class);
        return savePiMaster(piMaster);
    }

    // 无名氏保存患者
    public PiMaster savePiMaster(PiMaster piMaster) {
        PiMasterParam mParam = new PiMasterParam();
        mParam.setMaster(piMaster);
        if (mParam.getMaster() == null) {
            throw new BusException("参数错误,无法保存患者！");
        }
        if (StringUtils.isNotEmpty(mParam.getMaster().getNamePi())
                && mParam.getMaster().getNamePi().startsWith("无名氏")) {
            int count = DataBaseHelper.queryForScalar("select count(1) from pv_encounter "
                    + " where del_flag = '0' and pk_pi = ? and (eu_status = '0' or eu_status = '1')", Integer.class, mParam.getMaster().getPkPi());
            int countPv = DataBaseHelper.queryForScalar("select count(1) from pv_encounter "
                    + " where del_flag = '0' and pk_pi = ?", Integer.class, mParam.getMaster().getPkPi());
            if (count == 0 && StringUtils.isNotBlank(mParam.getMaster().getPkPi()) && countPv > 0) {
                throw new BusException("患者就诊结束不允许修改！");
            } else {
                return savePiMasterParam(mParam, "isAnonymous");
            }
        } else {
            return savePiMasterParam(mParam);
        }
    }

    public PiMaster savePiMasterParam(PiMasterParam mParam, String... isAnonymous) {
        // 关闭事务自动提交
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(def);
        PiMaster master = mParam.getMaster();
        try {
            if (master == null) {
                throw new BusException("参数错误,无法保存患者！");
            } else {

                //健康卡是否需要读取外部接口
                String extHealth = ApplicationUtils.getSysparam("PI0019", false);
                if ("1".equals(extHealth)) {
                    //如果不存在健康码进行健康码注册
                    if (CommonUtils.isEmptyString(master.getHicNo())) {
                        Map<String, Object> ehealthMap = new HashMap<>(16);
                        ehealthMap.put("piMaster", master);
                        //电子健康码注册
                        Map<String, String> hicNo = (Map<String, String>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHC01", new Object[]{ehealthMap});
                        if (hicNo != null) {
                            master.setHicNo(hicNo.get("hicNo"));
                            master.setNote(master.getNote() + hicNo.get("note"));
                        }
                    }
                }

                if (StringUtils.isEmpty(master.getPkPicate())) {
                    List<PiCate> picates = DataBaseHelper.queryForList("select * from pi_cate "
                            + "where flag_def='1' and del_flag='0'", PiCate.class);
                    master.setPkPicate(picates.get(0).getPkPicate());
                }

                if (StringUtils.isEmpty(master.getCodePi())) {
                    master.setCodePi(piCodeFactory.getHandler().genCodePi(master));
                }
                //判断PV0040-门诊建档生成住院号,0不生成，1生成
                if (("1").equals(ApplicationUtils.getSysparam("PV0040", false))) {
                    if (StringUtils.isEmpty(master.getCodeIp())) {//住院号
                        master.setCodeIp(piCodeFactory.getHandler().genCodeIp(master));
                        logger.info("==============【" + master.getNamePi() + "】获取住院号:" + master.getCodeIp() + "============");
                    }
                }
                if (StringUtils.isEmpty(master.getCodeOp())) {//门诊号
                    master.setCodeOp(piCodeFactory.getHandler().genCodeOp(master));
                }
            }

            int count_code = 0;//患者编码的数量
            int count_op = 0;//住院号
            int count_ip = 0;//门诊号
            int count_card = 0;//证件号码

            //证件号码字母转大写
            if (CommonUtils.isNotNull(master.getIdNo())) {
                master.setIdNo(master.getIdNo().toUpperCase());
            }

            //校验证件号码，证件类型
            List<BdDefdoc> identityTypeMap = DataBaseHelper.queryForList("select code,name from bd_defdoc" +
                    " where code_defdoclist = '000007' and  code = ? and del_flag = '0' order by  code ", BdDefdoc.class, master.getDtIdtype());
            StringBuilder sbSqlStr = new StringBuilder("");
            sbSqlStr.append("select count(1) from pi_master" +
                    " where del_flag = '0' and dt_idtype !='99' and dt_idtype = '" + master.getDtIdtype() + "'  and id_no = '" + master.getIdNo()+"'");
            if (!StringUtils.isEmpty(master.getPkPi())) {
                sbSqlStr.append(" and pk_pi != '" + master.getPkPi()+"'");
            }
            count_card = DataBaseHelper.queryForScalar(sbSqlStr.toString(),Integer.class);
            if (StringUtils.isNotBlank(master.getIdNo())) {
                if (count_card != 0) {
                    String tipMsg = identityTypeMap.get(0).getName();
                    throw new BusException(tipMsg + "号重复，无法保存！");
                }
            }

            // 患者编码、门诊号、住院号、身份证不能重复
            if (StringUtils.isEmpty(master.getPkPi())) // 新增保存
            {
                //1、校验患者编码
                count_code = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and code_pi = ?", Integer.class, master.getCodePi());
                if (count_code != 0) {
                    throw new BusException("患者编码重复,无法保存！");
                }

                //患者建档医保卡号唯一校验-ZSRM_任务[4779]_20201230_tjq
                if (StringUtils.isNotEmpty(master.getInsurNo())) {
                    int countInsurNo = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and insur_no = ?", Integer.class, master.getInsurNo());
                    if (countInsurNo != 0) {
                        throw new BusException("医保卡号重复,无法保存！");
                    }
                }

                // 判断是否为无名氏保存患者
                if (isAnonymous.length == 0) // 普通患者保存
                {
                    //2、校验门诊号
                    count_op = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and code_op = ?", Integer.class, master.getCodeOp());
                    if (count_op != 0) {
                        throw new BusException("门诊号重复,无法保存！");
                    }

                    //3、校验住院号
//					count_ip = DataBaseHelper.queryForScalar( "select count(1) from pi_master "
//									+ "where del_flag = '0' and code_ip = ?", Integer.class, master.getCodeIp());
//					if (count_ip != 0) throw new BusException("住院号重复,无法保存！");

                    //4、校验证件号码，证件类型 = 身份证时
					/*if ("01".equals(master.getDtIdtype())) {
						count_card = DataBaseHelper .queryForScalar( "select count(1) from pi_master "
								+ "where del_flag = '0' and dt_idtype = '01' and id_no = ?", Integer.class, master.getIdNo());
					}
					if(StringUtils.isNotBlank(master.getIdNo())){
						if (count_card != 0){
							throw new BusException("身份证号重复,无法保存！");
						}
					}*/

                    master.setPkPi(NHISUUID.getKeyId());

                    DataBaseHelper.insertBean(master);
                    savePiAccByNew(master);//新增时，插入一条PiAcc记录

                } else {// 无名氏患者保存
                    String name = master.getNamePi();
                    // 查询无名氏是否存在
                    int count_name = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and name_pi = ?", Integer.class, name);
                    if (count_name == 0) {
                        DataBaseHelper.insertBean(master);
                    } else {
                        name = getAnonymous();
                        master.setNamePi(name);
                        DataBaseHelper.insertBean(master);
                    }
                    savePiAccByNew(master);//新增时，插入一条PiAcc记录
                }
            } else {
                //1、校验编码是否重复
                count_code = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and code_pi = ? and pk_pi != ? ", Integer.class, master.getCodePi(), master.getPkPi());
                if (count_code != 0) {
                    throw new BusException("患者编码重复，无法更新！");
                }

                //患者建档医保卡号唯一校验-ZSRM_任务[4779]_20201230_tjq
                if (StringUtils.isNotEmpty(master.getInsurNo())) {
                    int countInsurNo = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and insur_no = ? and pk_pi != ? ", Integer.class, master.getInsurNo(), master.getPkPi());
                    if (countInsurNo != 0) {
                        throw new BusException("医保卡号重复,无法更新！");
                    }
                }

                if (isAnonymous.length == 0) // 判断是否为无名氏保存患者
                {
                    // 普通患者保存
                    //2、检验门诊号是否重复
                    if (StringUtils.isNotBlank(master.getCodeOp())) {
                        count_op = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and code_op = ? and pk_pi != ? ", Integer.class, master.getCodeOp(), master.getPkPi());
                    }
                    if (count_op != 0) {
                        throw new BusException("门诊号重复，无法更新！");
                    }
                    //3、校验住院号是否重复
                    if (StringUtils.isNotBlank(master.getCodeIp())) {
                        count_ip = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and code_ip = ? and pk_pi != ? ", Integer.class, master.getCodeIp(), master.getPkPi());
                    }
                    if (count_ip != 0) {
                        throw new BusException("住院号重复，无法更新！");
                    }
					/*//4、校验身份证是否重复
					if(StringUtils.isNotBlank(master.getIdNo())){
						count_card = DataBaseHelper.queryForScalar("select count(1) from pi_master "
								+ "where del_flag = '0' and dt_idtype = '01' and id_no = ? and pk_pi != ? ",Integer.class, master.getIdNo(),master.getPkPi());
					}
					if ("01".equals(master.getDtIdtype()) && count_card != 0){
						throw new BusException("身份证号重复，无法更新！");
					}*/

                    //5、更新患者信息
                    DataBaseHelper.updateBeanByPk(master, false);
                    //6、更新账户表账户编码(CODE_ACC = code_ip)
                    DataBaseHelper.update("update pi_acc set code_acc = ? where pk_pi = ?",
                            new Object[]{master.getCodeIp(), master.getPkPi()});

                    //7、更新最近一次就诊记录的相关
                    if (master.getBirthDate() != null) {
                        // 修改患者就诊表冗余字段，包括年龄，但只改当前就诊记录，不改历史就诊记录 2017-06-14
                        String age = DateUtils.getAgeByBirthday(master.getBirthDate(), new Date());
                        try {
                            Map<String, Object> param = objectToMap(master);
                            String birthDate = DateUtils.getDateTimeStr(master.getBirthDate());
                            StringBuffer str = new StringBuffer();
                            str.append("update pv_encounter set pk_picate =:pkPicate, name_pi  =:namePi, "
                                    + "dt_sex =:dtSex, address =:address, dt_marry =:dtMarry, ");
                            if (Application.isSqlServer()) {
                                str.append("age_pv = dbo.GetPatAge (to_date(" + birthDate + ",'YYYYMMDDHH24MISS'),date_begin),");
                            } else {
                                str.append("age_pv = GetPatAge (to_date(" + birthDate + ",'YYYYMMDDHH24MISS'),date_begin),");
                            }
                            str.append("unit_work =:unitWork, tel_work =:telWork, postcode_work =:postcodeWork,"
                                    + "name_rel =:nameRel, tel_rel =:telRel, dt_ralation =:dtRalation, addr_rel =:addrRel,"
                                    + "addrcode_regi =:addrcodeRegi, addr_regi =:addrRegi, addr_regi_dt =:addrRegiDt, postcode_regi =:postcodeRegi,"
                                    + "addrcode_cur =:addrcodeCur, addr_cur =:addrCur, addr_cur_dt =:addrCurDt, postcode_cur =:postcodeCur"
                                    + " where 1=1 and pk_pi =:pkPi"
                                    + " and (eu_status = '0' or eu_status = '1')");
                            DataBaseHelper.update(str.toString(), param);


                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new BusException("查询当前有效就诊记录失败，更新患者信息失败！");
                        }
                    }
                } else {
                    String name = master.getNamePi();
                    // 查询无名氏是否存在
                    int count_name = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and name_pi = ? and pk_pi != ?", Integer.class, name, master.getPkPi());
                    if (count_name == 0) {
                        DataBaseHelper.updateBeanByPk(master, false);
                    } else {
                        name = getAnonymous();
                        master.setNamePi(name);
                        DataBaseHelper.updateBeanByPk(master, false);
                    }
                }
            }

            String pkPi = master.getPkPi();
            master = DataBaseHelper.queryForBean("select * from pi_master where del_flag = '0' and pk_pi = ?", PiMaster.class, pkPi);

            // 保存医保计划,为null表示没有操作，数量为0表示全部删除了
            List<PiInsurance> insulist = mParam.getInsuranceList();
            if (insulist != null) {
                savePiInsuranceList(insulist, pkPi);
            }
            // 保存家庭关系
            List<PiFamily> familyList = mParam.getFamilyList();
            if (familyList != null) {
                savePiFamilyList(familyList, pkPi);
            }
            // 保存患者地址
            List<PiAddress> addressList = mParam.getAddressList();
            if (addressList != null) {
                savePiAddressList(addressList, pkPi);
            }
            // 保存患者过敏史
            List<PiAllergic> allergicList = mParam.getAllergicList();
            if (allergicList != null) {
                savePiAllergicList(allergicList, pkPi);
            }
            // 保存患者疾病史
            List<PiDise> diseList = mParam.getDiseList();
            if (diseList != null) {
                savePiDiseList(diseList, pkPi);
            }
            // 保存患者卡信息
            List<PiCard> cardLst = mParam.getCardList();
            if (cardLst != null) {
                savePiCardList(cardLst, pkPi);
            }
            platformTransactionManager.commit(status); // 提交事务
        } catch (Exception e) {
            platformTransactionManager.rollback(status); // 添加失败 回滚事务；
            if (e instanceof BusException) {
                throw e;
            }
            e.printStackTrace();//打印非bus异常堆栈
            throw new BusException("保存患者信息失败：" + e.getMessage());
        }
        return master;
    }

    /**
     * 保存患者信息，自动提交事物
     *
     * @param mParam
     * @param isAnonymous
     * @return
     */
    public PiMaster savePiMasterParamAutoCommint(PiMasterParam mParam, String... isAnonymous) {

        PiMaster master = mParam.getMaster();
        if (master == null) {
            throw new BusException("参数错误,无法保存患者！");
        } else {
            //健康卡是否需要读取外部接口
            String extHealth = ApplicationUtils.getSysparam("PI0019", false);
            if ("1".equals(extHealth)) {
                //如果不存在健康码进行健康码注册
                if (CommonUtils.isEmptyString(master.getHicNo())) {
                    Map<String, Object> ehealthMap = new HashMap<>(16);
                    ehealthMap.put("piMaster", master);
                    //电子健康码注册
                    Map<String, String> hicNo = (Map<String, String>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHC01", new Object[]{ehealthMap});
                    if (hicNo != null) {
                        master.setHicNo(hicNo.get("hicNo"));
                        master.setNote(master.getNote() + hicNo.get("note"));
                    }
                }
            }
            if (StringUtils.isEmpty(master.getPkPicate())) {
                List<PiCate> picates = DataBaseHelper.queryForList("select * from pi_cate "
                        + "where flag_def='1' and del_flag='0'", PiCate.class);
                master.setPkPicate(picates.get(0).getPkPicate());
            }

            if (StringUtils.isEmpty(master.getCodePi())) {
                String codePi = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ);
                master.setCodePi(codePi);

            }
            if (StringUtils.isEmpty(master.getCodeIp())) {//住院号
                master.setCodeIp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL));
                logger.info("==============【" + master.getNamePi() + "】PiPubService.savePiMasterParamAutoCommint获取住院号:" + master.getCodeIp() + "============");

            }
            if (StringUtils.isEmpty(master.getCodeOp())) {//门诊号
                master.setCodeOp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZBL));
            }
        }

        int count_code = 0;//患者编码的数量
        int count_op = 0;//住院号
        int count_ip = 0;//门诊号
        int count_card = 0;//证件号码

        //证件号码字母转大写
        if (CommonUtils.isNotNull(master.getIdNo())) {
            master.setIdNo(master.getIdNo().toUpperCase());
        }

        // 患者编码、门诊号、住院号、身份证不能重复
        if (StringUtils.isEmpty(master.getPkPi())) // 新增保存
        {
            //1、校验患者编码
            count_code = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                    + "where del_flag = '0' and code_pi = ?", Integer.class, master.getCodePi());
            if (count_code != 0) {
                throw new BusException("患者编码重复,无法保存！");
            }

            // 判断是否为无名氏保存患者
            if (isAnonymous.length == 0) // 普通患者保存
            {
                //2、校验门诊号
                count_op = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and code_op = ?", Integer.class, master.getCodeOp());
                if (count_op != 0) {
                    throw new BusException("门诊号重复,无法保存！");
                }

                //3、校验住院号
                count_ip = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and code_ip = ?", Integer.class, master.getCodeIp());
                if (count_ip != 0) {
                    throw new BusException("住院号重复,无法保存！");
                }

                //4、校验证件号码，证件类型 = 身份证时
                if ("01".equals(master.getDtIdtype())) {
                    count_card = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and dt_idtype = '01' and id_no = ?", Integer.class, master.getIdNo());
                }
                if (StringUtils.isNotBlank(master.getIdNo())) {
                    if (count_card != 0) {
                        throw new BusException("身份证号重复,无法保存！");
                    }
                }

                master.setPkPi(NHISUUID.getKeyId());

                DataBaseHelper.insertBean(master);
                savePiAccByNew(master);//新增时，插入一条PiAcc记录

            } else {// 无名氏患者保存
                String name = master.getNamePi();
                // 查询无名氏是否存在
                int count_name = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and name_pi = ?", Integer.class, name);
                if (count_name == 0) {
                    DataBaseHelper.insertBean(master);
                } else {
                    name = getAnonymous();
                    master.setNamePi(name);
                    DataBaseHelper.insertBean(master);
                }
                savePiAccByNew(master);//新增时，插入一条PiAcc记录
            }
        } else {
            //1、校验编码是否重复
            count_code = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                    + "where del_flag = '0' and code_pi = ? and pk_pi != ? ", Integer.class, master.getCodePi(), master.getPkPi());
            if (count_code != 0) {
                throw new BusException("患者编码重复，无法更新！");
            }

            if (isAnonymous.length == 0) // 判断是否为无名氏保存患者
            {
                // 普通患者保存
                //2、检验门诊号是否重复
                if (StringUtils.isNotBlank(master.getCodeOp())) {
                    count_op = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and code_op = ? and pk_pi != ? ", Integer.class, master.getCodeOp(), master.getPkPi());
                }
                if (count_op != 0) {
                    throw new BusException("门诊号重复，无法更新！");
                }
                //3、校验住院号是否重复
                if (StringUtils.isNotBlank(master.getCodeIp())) {
                    count_ip = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and code_ip = ? and pk_pi != ? ", Integer.class, master.getCodeIp(), master.getPkPi());
                }
                if (count_ip != 0) {
                    throw new BusException("住院号重复，无法更新！");
                }
                //4、校验身份证是否重复
                if (StringUtils.isNotBlank(master.getIdNo())) {
                    count_card = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and dt_idtype = '01' and id_no = ? and pk_pi != ? ", Integer.class, master.getIdNo(), master.getPkPi());
                }
                if ("01".equals(master.getDtIdtype()) && count_card != 0) {
                    throw new BusException("身份证号重复，无法更新！");
                }

                //5、更新患者信息
                DataBaseHelper.updateBeanByPk(master, false);
                //6、更新账户表账户编码(CODE_ACC = code_ip)
                DataBaseHelper.update("update pi_acc set code_acc = ? where pk_pi = ?",
                        new Object[]{master.getCodeIp(), master.getPkPi()});

                //7、更新最近一次就诊记录的相关
                if (master.getBirthDate() != null) {
                    // 修改患者就诊表冗余字段，包括年龄，但只改当前就诊记录，不改历史就诊记录 2017-06-14
                    String age = DateUtils.getAgeByBirthday(master.getBirthDate(), new Date());
                    try {
                        Map<String, Object> param = objectToMap(master);
                        String birthDate = DateUtils.getDateTimeStr(master.getBirthDate());
                        StringBuffer str = new StringBuffer();
                        str.append("update pv_encounter set pk_picate =:pkPicate, name_pi  =:namePi, "
                                + "dt_sex =:dtSex, address =:address, dt_marry =:dtMarry, ");
                        if (Application.isSqlServer()) {
                            str.append("age_pv = dbo.GetPatAge (to_date(" + birthDate + ",'YYYYMMDDHH24MISS'),date_begin),");
                        } else {
                            str.append("age_pv = GetPatAge (to_date(" + birthDate + ",'YYYYMMDDHH24MISS'),date_begin),");
                        }
                        str.append("unit_work =:unitWork, tel_work =:telWork, postcode_work =:postcodeWork,"
                                + "name_rel =:nameRel, tel_rel =:telRel, dt_ralation =:dtRalation, addr_rel =:addrRel,"
                                + "addrcode_regi =:addrcodeRegi, addr_regi =:addrRegi, addr_regi_dt =:addrRegiDt, postcode_regi =:postcodeRegi,"
                                + "addrcode_cur =:addrcodeCur, addr_cur =:addrCur, addr_cur_dt =:addrCurDt, postcode_cur =:postcodeCur"
                                + " where 1=1 and pk_pi =:pkPi"
                                + " and (eu_status = '0' or eu_status = '1')");
                        DataBaseHelper.update(str.toString(), param);


                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new BusException("查询当前有效就诊记录失败，更新患者信息失败！");
                    }
                }
            } else {
                String name = master.getNamePi();
                // 查询无名氏是否存在
                int count_name = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and name_pi = ? and pk_pi != ?", Integer.class, name, master.getPkPi());
                if (count_name == 0) {
                    DataBaseHelper.updateBeanByPk(master, false);
                } else {
                    name = getAnonymous();
                    master.setNamePi(name);
                    DataBaseHelper.updateBeanByPk(master, false);
                }
            }
        }

        String pkPi = master.getPkPi();
        master = DataBaseHelper.queryForBean("select * from pi_master where del_flag = '0' and pk_pi = ?", PiMaster.class, pkPi);

        // 保存医保计划,为null表示没有操作，数量为0表示全部删除了
        List<PiInsurance> insulist = mParam.getInsuranceList();
        if (insulist != null) {
            savePiInsuranceList(insulist, pkPi);
        }
        // 保存家庭关系
        List<PiFamily> familyList = mParam.getFamilyList();
        if (familyList != null) {
            savePiFamilyList(familyList, pkPi);
        }
        // 保存患者地址
        List<PiAddress> addressList = mParam.getAddressList();
        if (addressList != null) {
            savePiAddressList(addressList, pkPi);
        }
        // 保存患者过敏史
        List<PiAllergic> allergicList = mParam.getAllergicList();
        if (allergicList != null) {
            savePiAllergicList(allergicList, pkPi);
        }
        // 保存患者疾病史
        List<PiDise> diseList = mParam.getDiseList();
        if (diseList != null) {
            savePiDiseList(diseList, pkPi);
        }
        // 保存患者卡信息
        List<PiCard> cardLst = mParam.getCardList();
        if (cardLst != null) {
            savePiCardList(cardLst, pkPi);
        }

        return master;
    }

    /**
     * java 实体类转 Map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }


    /**
     * 生成无名氏
     *
     * @param param
     * @param user
     * @return
     */
    public String getAnonymous(String param, IUser user) {
        return getAnonymous();
    }

    public String getAnonymous() {
        Integer maxNum = 0;
        if (Application.isSqlServer()) {
            maxNum = piPubMapper.getAnonymousMaxNumberSqlServer();
        } else {
            maxNum = piPubMapper.getAnonymousMaxNumberOracle();
        }
        String num = "";
        if (maxNum == null) {
            num = "001";
        } else {
            int number = maxNum + 1;
            num = String.valueOf(number);
            if (num.length() < 3) {
                while (num.length() < 3) {
                    num = "0" + num;
                }
            }
        }
        String name = "无名氏" + num;
        return name;
    }

    /**
     * 新增的时候保存一条账户信息
     */
    public void savePiAccByNew(PiMaster master) {
        PiAcc acc = new PiAcc();
        acc.setPkPi(master.getPkPi());
        acc.setCodeAcc(master.getCodeIp());
        acc.setAmtAcc(BigDecimal.ZERO);
        acc.setCreditAcc(BigDecimal.ZERO);
        acc.setEuStatus(PiConstant.ACC_EU_STATUS_1);
        DataBaseHelper.insertBean(acc);
    }

    /**
     * 保存医保计划
     */
    public void savePiInsuranceList(List<PiInsurance> list, String pkPi) {
        // 全部删除后重新添加
        DataBaseHelper
                .update("update pi_insurance set del_flag = '1',flag_def = '0' where pk_pi = ?",
                        new Object[]{pkPi});
        for (PiInsurance insu : list) {
            if (StringUtils.isEmpty(insu.getPkInsurance())) { // 保存
                insu.setPkPi(pkPi);
                insu.setDelFlag("0");
                insu.setCreateTime(new Date());
                DataBaseHelper.insertBean(insu);
            } else {
                insu.setPkPi(pkPi);
                insu.setDelFlag("0");
                insu.setCreateTime(new Date());
                DataBaseHelper.updateBeanByPk(insu, false);
            }
        }
    }

    /**
     * 保存家庭关系
     */
    public void savePiFamilyList(List<PiFamily> list, String pkPi) {
        DataBaseHelper.update(
                "update pi_family set del_flag = '1' where pk_pi = ?",
                new Object[]{pkPi});
        for (PiFamily family : list) {
            if (StringUtils.isEmpty(family.getPkFamily())) {
                family.setPkPi(pkPi);
                family.setDelFlag("0");
                DataBaseHelper.insertBean(family);
            } else {
                family.setPkPi(pkPi);
                family.setDelFlag("0");
                DataBaseHelper.updateBeanByPk(family, false);
            }
        }
    }

    /**
     * 保存患者地址
     */
    public void savePiAddressList(List<PiAddress> list, String pkPi) {
        // 全部删除后重新添加
        DataBaseHelper.update(
                "update pi_address set del_flag = '1' where pk_pi = ?",
                new Object[]{pkPi});
        for (PiAddress address : list) {
            if (StringUtils.isEmpty(address.getPkAddr())) { // 保存
                address.setPkPi(pkPi);
                address.setDelFlag("0");
                DataBaseHelper.insertBean(address);
            } else {
                address.setPkPi(pkPi);
                address.setDelFlag("0");
                DataBaseHelper.updateBeanByPk(address, false);
            }
        }
    }

    /**
     * 保存患者过敏史
     */
    public void savePiAllergicList(List<PiAllergic> list, String pkPi) {
        // 全部删除后重新添加
        DataBaseHelper.update(
                "update pi_allergic set del_flag = '1' where pk_pi = ?",
                new Object[]{pkPi});
        for (PiAllergic allergic : list) {
            if (StringUtils.isEmpty(allergic.getPkPial())) { // 保存
                allergic.setPkPi(pkPi);
                allergic.setDelFlag("0");
                DataBaseHelper.insertBean(allergic);
            } else {
                allergic.setPkPi(pkPi);
                allergic.setDelFlag("0");
                DataBaseHelper.updateBeanByPk(allergic, false);
            }
        }
    }

    /**
     * 保存患者疾病史
     */
    public void savePiDiseList(List<PiDise> list, String pkPi) {
        // 全部删除后重新添加
        DataBaseHelper.update(
                "update pi_dise set del_flag = '1' where pk_pi = ?",
                new Object[]{pkPi});
        for (PiDise dise : list) {
            if (StringUtils.isEmpty(dise.getPkPiDise())) { // 保存
                dise.setPkPi(pkPi);
                dise.setDelFlag("0");
                DataBaseHelper.insertBean(dise);
            } else {
                dise.setPkPi(pkPi);
                dise.setDelFlag("0");
                DataBaseHelper.updateBeanByPk(dise, false);
            }
        }
    }

    /**
     * 保存账户信息
     */
    public void savePiCardList(List<PiCard> list, String pkPi) {
        DataBaseHelper.update("update pi_card set del_flag='1' where pk_pi=?",
                new Object[]{pkPi});
        for (PiCard card : list) {
            if (StringUtils.isEmpty(card.getPkPicard())) {
                card.setPkPi(pkPi);
                card.setDelFlag("0");
                DataBaseHelper.insertBean(card);
            } else {
                card.setPkPi(pkPi);
                card.setDelFlag("0");
                DataBaseHelper.updateBeanByPk(card, false);
            }
        }
    }

    /**
     * 精确查询住院患者就诊信息(包含预交金以及费用总额)<br>
     *
     * <pre>
     * 1.从患者基本表pi_master中查询基本信息;
     * 2.从就诊记录表pv_encounter查询就诊信息,查询就诊状态为0、1、3的记录;
     * 3.从过敏史表pi_allergic查询过敏信息，多笔过敏信息之间以逗号分隔;
     * 4.从临床综合诊断表pv_diag中获取诊断名称，多个诊断之间使用逗号分隔;
     * 5.从医保计划表pi_insurance查询医保名称;
     * 6.从收费结算-交款记录表bl_deposit中统计eu_dptype=9的交款金额作为预交金金额;
     * 7.从收费结算-住院收费明细包bl_ip_dt表中统计金额字段做为费用总额;
     * 8.pi_allergic、pv_diag、bl_deposit、bl_ip_dt表使用pk_pv字段与pv_encounter表关联;
     * 9.参数就诊状态为数组，为空时查询就诊状态为1的记录;
     * 10.查询字段包括：患者编码、就诊卡号、证件号码、医保卡号、就诊号码、当前床位、就诊主键
     * </pre>
     *
     * @param param
     * @return user
     * @throws
     * @author wangpeng
     * @date 2016年9月10日
     */
    public PibaseVo getPibaseAndAmountVo(String param, IUser user) {
        CommonParam cparam = JsonUtil.readValue(param, CommonParam.class);
        PibaseVo vo = getPibaseAndAmountVo(cparam);
        return vo;
    }

    public PibaseVo getPibaseAndAmountVo(CommonParam cparam) {
        String fieldName = cparam.getFieldName();
        String fieldValue = cparam.getFieldValue();

        PibaseVo vo = new PibaseVo();
        PiMaster master = new PiMaster();
        PvEncounter encounter = new PvEncounter();
        // 参数在不同的表中，分开查表
        boolean flag = false; // 默认false 查患者基本表 pi_master
        if ("code_pi".equals(fieldName)) { // 患者编码
            master.setCodePi(fieldValue);
        } else if ("code_ip".equals(fieldName)) { // 住院号
            master.setCodeIp(fieldValue);
            encounter.setEuPvtype("3");//如果按照住院号查询，默认查询住院患者，否则会查出门诊就诊信息
        } else if ("id_no".equals(fieldName)) {// 证件号码
            master.setIdNo(fieldValue);
        } else if ("insur_no".equals(fieldName)) {// 医保卡号
            master.setInsurNo(fieldValue);
        } else if ("code_pv".equals(fieldName)) { // 就诊号码
            encounter.setCodePv(fieldValue);
            flag = true;
        } else if ("bed_no".equals(fieldName)) { // 当前床位
            //新增查询条件按，查询本科室下的患者
            encounter.setPkDeptNs(UserContext.getUser().getPkDept());
            encounter.setBedNo(fieldValue);
            flag = true;
        } else if ("pk_pv".equals(fieldName)) { // 就诊主键
            encounter.setPkPv(fieldValue);
            flag = true;
        } else if ("card_no".equals(fieldName)) { // 就诊卡号
            PiCard card = piPubMapper.getPiCardByCardNo(fieldValue);
            if (card != null) {
                master.setPkPi(card.getPkPi());
            } else {
                return null;
            }
        } else if ("code_st".equals(fieldName)) { // 就诊卡号
            BlSettle blSettle = piPubMapper.qryBlSettleByCodeSt(fieldValue);
            if (blSettle != null) {
                master.setPkPi(blSettle.getPkPi());
                encounter.setPkPi(blSettle.getPkPi());
                encounter.setPkPv(blSettle.getPkPv());
            } else {
                return null;
            }
        } else {
            throw new BusException("参数错误！");
        }

        // 参数flagStatus（默认1）：0-不判断就诊状态；1-判断就诊状态（如果参数euStatuss为空，默认查询状态为1的记录）
        if ("1".equals(cparam.getFlagStatus())) {
            // 查询就诊记录表pv_encounter，默认查就诊状态为1的记录
            String[] euStatuss = cparam.getEuStatuss();
            if (euStatuss == null) {
                euStatuss = new String[]{"1"};
            }
            encounter.setEuStatuss(euStatuss);
        }
        if (flag) {
            List<PvEncounter> list = piPubMapper.getPvEncounterList(encounter);
            if (CollectionUtils.isNotEmpty(list)) {
                encounter = list.get(0);
                master.setPkPi(encounter.getPkPi());
                List<PiMaster> listP = piPubMapper.getPiMaster(master);
                if (CollectionUtils.isNotEmpty(listP)) {
                    master = listP.get(0);
                }
            } else {
                return null;
            }
        } else {
            List<PiMaster> listP = piPubMapper.getPiMaster(master);
            if (CollectionUtils.isNotEmpty(listP)) {
                master = listP.get(0);
            }
            if (master == null) {
                throw new BusException("无法获取患者信息！");
            } else if (CommonUtils.isEmptyString(master.getPkPi())) {
                return null;
            }

            encounter.setPkPi(master.getPkPi());
            List<PvEncounter> list = piPubMapper.getPvEncounterList(encounter);
            if (CollectionUtils.isNotEmpty(list)) {
                encounter = list.get(0);
            } else {
                return null;
            }
        }

        if (encounter.getPkPv() != null && !"".equals(encounter.getPkPv())) {
            String queSql = "select * from pv_ip where pk_pv = ?";
            PvIp pvIp = DataBaseHelper.queryForBean(queSql, PvIp.class, encounter.getPkPv());
            if (pvIp != null) {
                vo.setIpTimes(pvIp.getIpTimes().toString());
            }
        }


        vo.setPkPi(master.getPkPi());
        vo.setPkPv(encounter.getPkPv());
        vo.setPkInsu(encounter.getPkInsu());//医保主计划
        vo.setCodePi(master.getCodePi());
        vo.setCodeIp(master.getCodeIp());//添加住院号
        vo.setCodeOp(master.getCodeOp());//添加门诊号
        vo.setNamePi(master.getNamePi());
        vo.setDtSex(master.getDtSex());
        vo.setBirthDate(master.getBirthDate());
        vo.setDtIdtype(master.getDtIdtype());
        vo.setIdNo(master.getIdNo());
        vo.setMobile(master.getMobile());
        vo.setInsurNo(master.getInsurNo());
        vo.setNamePicate(master.getNamePicate());
        vo.setNameRel(master.getNameRel());//联系人
        vo.setFlagIn(encounter.getFlagIn());
        vo.setCodePv(encounter.getCodePv());
        vo.setPkEmpPhy(encounter.getPkEmpPhy());
        vo.setNameEmpPhy(encounter.getNameEmpPhy());
        vo.setPkEmpNs(encounter.getPkEmpNs());
        vo.setNameEmpNs(encounter.getNameEmpNs());
        vo.setBedNo(encounter.getBedNo());
        vo.setPkDept(encounter.getPkDept());
        vo.setPkDeptNs(encounter.getPkDeptNs());
        vo.setEuStatus(encounter.getEuStatus());
        vo.setDateBegin(encounter.getDateBegin());
        vo.setDateAdmit(encounter.getDateAdmit());
        vo.setDateEnd(encounter.getDateEnd());
        vo.setDateClinic(encounter.getDateClinic());
        vo.setEuPvtype(encounter.getEuPvtype());
        vo.setPkPicate(encounter.getPkPicate());
        vo.setAddress(master.getAddrCur() + master.getAddrCurDt());
        vo.setFlagSpec(encounter.getFlagSpec());
        vo.setNote(encounter.getNote());
        vo.setFlagMi(encounter.getFlagMi());
        vo.setNameSpouse(encounter.getNameSpouse());
        vo.setIdnoSpouse(encounter.getIdnoSpouse());
        vo.setMcno(master.getMcno());
        vo.setEuPvmode(encounter.getEuPvmode());
        vo.setAgeFormat(ApplicationUtils.getAgeFormat(master.getBirthDate(), encounter.getDateBegin()));
        // 住院天数 -- 出院则用出院时间计算，未出院用当前日期
        if (encounter.getDateBegin() != null && encounter.getDateEnd() != null) {
            vo.setIpDays(DateUtils.getDateSpace(encounter.getDateBegin(), encounter.getDateEnd()) + 1);
        } else if (encounter.getDateBegin() != null) {
            vo.setIpDays(DateUtils.getDateSpace(encounter.getDateBegin(), new Date()));
        } else {
            vo.setIpDays(0);
        }

        //婴儿标志--pv_infant存在 pk_pv_infant = pk_pv，则表示为婴儿
        int cnt = DataBaseHelper.queryForScalar("select count(1) from pv_infant where pk_pv_infant = ? and del_flag='0'"
                , Integer.class, new Object[]{encounter.getPkPv()});
        if (cnt > 0)
            vo.setFlagInfant("1");

        // 过敏信息
        String nameAl = "";
        List<PiAllergic> allergicLsit = piPubMapper.getPiAllergicListByPkPi(master.getPkPi());
        if (CollectionUtils.isNotEmpty(allergicLsit)) {
            int i = 0;
            for (PiAllergic allergic : allergicLsit) {
                if (i == 0) {
                    nameAl += allergic.getNameAl();
                } else {
                    nameAl += "," + allergic.getNameAl();
                    i++;
                }
            }
        }
        vo.setNameAl(nameAl);

        // 诊断信息
        String nameDiag = "";
        String pkDiag = "";
        List<PvDiagVo> diagList = piPubMapper.getPvDiagListByPkPv(encounter.getPkPv());
        if (CollectionUtils.isNotEmpty(diagList)) {
            int i = 0;
            for (PvDiagVo diag : diagList) {
                if (i == 0) {
                    nameDiag += diag.getDiagText();
                    pkDiag += diag.getPkDiag();
                } else {
                    nameDiag += "," + diag.getDiagText();
                    pkDiag += "," + diag.getPkDiag();
                    i++;
                }
            }
        }
        vo.setPkDiag(pkDiag);
        vo.setNameDiag(nameDiag);

        // 保险 2018-12-13 左连接拓展属性，获取该医保是否为广州医保
        Map<String, Object> mapInsu = DataBaseHelper.queryForMap("select hp.bedquota,hp.name,attr.val_attr flag_gz_hp from bd_hp hp"
                + " left join bd_dictattr attr on attr.pk_dict = hp.pk_hp and attr.del_flag = '0' and attr.code_attr = '0307'"
                + " left join bd_dictattr_temp attrtemp on attrtemp.pk_dictattrtemp = attr.pk_dictattrtemp and attrtemp.del_flag = '0'"
                + " where hp.del_flag = '0' and hp.pk_hp = ?", encounter.getPkInsu());
        if (mapInsu != null) {
            vo.setNameInsu(mapInsu.get("name").toString());
            vo.setBedquota(CommonUtils.getDoubleObject(mapInsu.get("bedquota")));
            if (null != mapInsu.get("flagGzHp") && !CommonUtils.isEmptyString(mapInsu.get("flagGzHp").toString()))
                vo.setFlagGzHp(mapInsu.get("flagGzHp").toString());//是否为广州医保
        } else {
            vo.setNameInsu("");
            vo.setFlagGzHp("0");
        }

        // 预交金金额
        vo.setPrepayAmount(piPubMapper.getTotalPrepayAmountByPkPv(encounter.getPkPv()));

        // 费用总额
        vo.setTotalAmount(piPubMapper.getTotalAmountByPkPv(encounter.getPkPv()));

        // 自费总额
        vo.setAmountPi(piPubMapper.getTotalAmountPiByPkPv(encounter.getPkPv()));

        return vo;
    }

    /**
     * 交易号：002003001040
     * 保存患者就诊医保计划
     *
     * @param param
     * @param user
     * @return
     */
    public String savePvInsuInfo(String param, IUser user) {
        String pkInsu = null;
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");
        if (CommonUtils.isEmptyString(pkPv))
            return pkInsu;

        //查询pi_insurance医保计划集合
        List<PiInsurance> insuList = DataBaseHelper.queryForList(
                "select * from pi_insurance insu inner join PV_ENCOUNTER pv on pv.pk_pi = insu.pk_pi where pv.pk_pv = ? and insu.del_flag = '0'",
                PiInsurance.class, new Object[]{pkPv});

        if (insuList != null && insuList.size() > 0) {
            //取flag_def=‘1’的医保计划
            PiInsurance insuInfo = null;
            for (PiInsurance insu : insuList) {
                if ("1".equals(insu.getFlagDef())) {
                    insuInfo = new PiInsurance();
                    insuInfo = insu;
                    break;
                }
            }
            //如果没有flag_def=‘1’的医保计划，则取集合的第一条数据
            if (insuInfo == null || CommonUtils.isEmptyString(insuInfo.getPkHp())) {
                insuInfo = new PiInsurance();
                insuInfo = insuList.get(0);
            }

            if (insuInfo != null && !CommonUtils.isEmptyString(insuInfo.getPkHp())) {
                //更新患者就诊医保计划
                DataBaseHelper.execute(
                        "update PV_ENCOUNTER set pk_insu = ? where pk_pv = ?",
                        new Object[]{insuInfo.getPkHp(), pkPv});
                pkInsu = insuInfo.getPkHp();
            }
        }

        return pkInsu;
    }

    /**
     * 交易号：002003001041
     * 查询患者是否有待结算明细数据
     *
     * @param param List<String> pkPvList
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryPvCgInfo(String param, IUser user) {
        List<String> pkPvList = JsonUtil.readValue(
                param,
                new TypeReference<List<String>>() {
                });

        List<Map<String, Object>> retList = new ArrayList<>();

        if (pkPvList != null && pkPvList.size() > 0) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("pkList", pkPvList);

            if (Application.isSqlServer()) {
                retList = piPubMapper.qryPvCgInfoByMssql(paramMap);
            } else {
                retList = piPubMapper.qryPvCgInfo(paramMap);
            }
        }
        return retList;
    }


    /**
     * 保存患者信息，自动提交事物
     *
     * @param mParam
     * @param isAnonymous
     * @return
     */
    public PiMaster savePiMasterParamAutoCommint(PiMasterParam mParam, boolean isAddCodeIp, String... isAnonymous) {

        PiMaster master = mParam.getMaster();
        if (master == null) {
            throw new BusException("参数错误,无法保存患者！");
        } else {
            //健康卡是否需要读取外部接口
            String extHealth = ApplicationUtils.getSysparam("PI0019", false);
            if ("1".equals(extHealth)) {
                //如果不存在健康码进行健康码注册
                if (CommonUtils.isEmptyString(master.getHicNo())) {
                    Map<String, Object> ehealthMap = new HashMap<>(16);
                    ehealthMap.put("piMaster", master);
                    //电子健康码注册
                    Map<String, String> hicNo = (Map<String, String>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHC01", new Object[]{ehealthMap});
                    if (hicNo != null) {
                        master.setHicNo(hicNo.get("hicNo"));
                        master.setNote(master.getNote() + hicNo.get("note"));
                    }
                }
            }
            if (StringUtils.isEmpty(master.getPkPicate())) {
                List<PiCate> picates = DataBaseHelper.queryForList("select * from pi_cate "
                        + "where flag_def='1' and del_flag='0'", PiCate.class);
                master.setPkPicate(picates.get(0).getPkPicate());
            }

            if (StringUtils.isEmpty(master.getCodePi())) {
                String codePi = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ);
                master.setCodePi(codePi);

            }
            if (StringUtils.isEmpty(master.getCodeIp()) && isAddCodeIp == true) {//住院号
                master.setCodeIp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL));
                logger.info("==============【" + master.getNamePi() + "】PiPubService.savePiMasterParamAutoCommint获取住院号:" + master.getCodeIp() + "============");

            }
            if (StringUtils.isEmpty(master.getCodeOp())) {//门诊号
                master.setCodeOp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZBL));
            }
        }

        int count_code = 0;//患者编码的数量
        int count_op = 0;//住院号
        int count_ip = 0;//门诊号
        int count_card = 0;//证件号码

        // 患者编码、门诊号、住院号、身份证不能重复
        if (StringUtils.isEmpty(master.getPkPi())) // 新增保存
        {
            //1、校验患者编码
            count_code = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                    + "where del_flag = '0' and code_pi = ?", Integer.class, master.getCodePi());
            if (count_code != 0) throw new BusException("患者编码重复,无法保存！");

            // 判断是否为无名氏保存患者
            if (isAnonymous.length == 0) // 普通患者保存
            {
                //2、校验门诊号
                count_op = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and code_op = ?", Integer.class, master.getCodeOp());
                if (count_op != 0) throw new BusException("门诊号重复,无法保存！");

                if (isAddCodeIp == true) {
                    //3、校验住院号
                    count_ip = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and code_ip = ?", Integer.class, master.getCodeIp());
                    if (count_ip != 0) throw new BusException("住院号重复,无法保存！");
                }
                //4、校验证件号码，证件类型 = 身份证时
                if ("01".equals(master.getDtIdtype())) {
                    count_card = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and dt_idtype = '01' and id_no = ?", Integer.class, master.getIdNo());
                }
                if (StringUtils.isNotBlank(master.getIdNo())) {
                    if (count_card != 0) throw new BusException("身份证号重复,无法保存！");
                }

                master.setPkPi(NHISUUID.getKeyId());

                DataBaseHelper.insertBean(master);
                savePiAccByNew(master);//新增时，插入一条PiAcc记录

            } else {// 无名氏患者保存
                String name = master.getNamePi();
                // 查询无名氏是否存在
                int count_name = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and name_pi = ?", Integer.class, name);
                if (count_name == 0) {
                    DataBaseHelper.insertBean(master);
                } else {
                    name = getAnonymous();
                    master.setNamePi(name);
                    DataBaseHelper.insertBean(master);
                }
                savePiAccByNew(master);//新增时，插入一条PiAcc记录
            }
        } else {
            //1、校验编码是否重复
            count_code = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                    + "where del_flag = '0' and code_pi = ? and pk_pi != ? ", Integer.class, master.getCodePi(), master.getPkPi());
            if (count_code != 0) throw new BusException("患者编码重复，无法更新！");

            if (isAnonymous.length == 0) // 判断是否为无名氏保存患者
            {
                // 普通患者保存
                //2、检验门诊号是否重复
                if (StringUtils.isNotBlank(master.getCodeOp())) {
                    count_op = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and code_op = ? and pk_pi != ? ", Integer.class, master.getCodeOp(), master.getPkPi());
                }
                if (count_op != 0) throw new BusException("门诊号重复，无法更新！");
                if (isAddCodeIp == true) {
                    //3、校验住院号是否重复
                    if (StringUtils.isNotBlank(master.getCodeIp())) {
                        count_ip = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                                + "where del_flag = '0' and code_ip = ? and pk_pi != ? ", Integer.class, master.getCodeIp(), master.getPkPi());
                    }
                    if (count_ip != 0) throw new BusException("住院号重复，无法更新！");
                }
                //4、校验身份证是否重复
                if (StringUtils.isNotBlank(master.getIdNo())) {
                    count_card = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                            + "where del_flag = '0' and dt_idtype = '01' and id_no = ? and pk_pi != ? ", Integer.class, master.getIdNo(), master.getPkPi());
                }
                if ("01".equals(master.getDtIdtype()) && count_card != 0) throw new BusException("身份证号重复，无法更新！");

                //5、更新患者信息
                DataBaseHelper.updateBeanByPk(master, false);
                //6、更新账户表账户编码(CODE_ACC = code_ip)
                DataBaseHelper.update("update pi_acc set code_acc = ? where pk_pi = ?",
                        new Object[]{master.getCodeIp(), master.getPkPi()});

                //7、更新最近一次就诊记录的相关
                if (master.getBirthDate() != null) {
                    // 修改患者就诊表冗余字段，包括年龄，但只改当前就诊记录，不改历史就诊记录 2017-06-14
                    String age = DateUtils.getAgeByBirthday(master.getBirthDate(), new Date());
                    try {
                        Map<String, Object> param = objectToMap(master);
                        String birthDate = DateUtils.getDateTimeStr(master.getBirthDate());
                        StringBuffer str = new StringBuffer();
                        str.append("update pv_encounter set pk_picate =:pkPicate, name_pi  =:namePi, "
                                + "dt_sex =:dtSex, address =:address, dt_marry =:dtMarry, ");
                        if (Application.isSqlServer()) {
                            str.append("age_pv = dbo.GetPatAge (to_date(" + birthDate + ",'YYYYMMDDHH24MISS'),date_begin),");
                        } else {
                            str.append("age_pv = GetPatAge (to_date(" + birthDate + ",'YYYYMMDDHH24MISS'),date_begin),");
                        }
                        str.append("unit_work =:unitWork, tel_work =:telWork, postcode_work =:postcodeWork,"
                                + "name_rel =:nameRel, tel_rel =:telRel, dt_ralation =:dtRalation, addr_rel =:addrRel,"
                                + "addrcode_regi =:addrcodeRegi, addr_regi =:addrRegi, addr_regi_dt =:addrRegiDt, postcode_regi =:postcodeRegi,"
                                + "addrcode_cur =:addrcodeCur, addr_cur =:addrCur, addr_cur_dt =:addrCurDt, postcode_cur =:postcodeCur"
                                + " where 1=1 and pk_pi =:pkPi"
                                + " and (eu_status = '0' or eu_status = '1')");
                        DataBaseHelper.update(str.toString(), param);


                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new BusException("查询当前有效就诊记录失败，更新患者信息失败！");
                    }
                }
            } else {
                String name = master.getNamePi();
                // 查询无名氏是否存在
                int count_name = DataBaseHelper.queryForScalar("select count(1) from pi_master "
                        + "where del_flag = '0' and name_pi = ? and pk_pi != ?", Integer.class, name, master.getPkPi());
                if (count_name == 0) {
                    DataBaseHelper.updateBeanByPk(master, false);
                } else {
                    name = getAnonymous();
                    master.setNamePi(name);
                    DataBaseHelper.updateBeanByPk(master, false);
                }
            }
        }

        String pkPi = master.getPkPi();
        master = DataBaseHelper.queryForBean("select * from pi_master where del_flag = '0' and pk_pi = ?", PiMaster.class, pkPi);

        // 保存医保计划,为null表示没有操作，数量为0表示全部删除了
        List<PiInsurance> insulist = mParam.getInsuranceList();
        if (insulist != null) {
            savePiInsuranceList(insulist, pkPi);
        }
        // 保存家庭关系
        List<PiFamily> familyList = mParam.getFamilyList();
        if (familyList != null) {
            savePiFamilyList(familyList, pkPi);
        }
        // 保存患者地址
        List<PiAddress> addressList = mParam.getAddressList();
        if (addressList != null) {
            savePiAddressList(addressList, pkPi);
        }
        // 保存患者过敏史
        List<PiAllergic> allergicList = mParam.getAllergicList();
        if (allergicList != null) {
            savePiAllergicList(allergicList, pkPi);
        }
        // 保存患者疾病史
        List<PiDise> diseList = mParam.getDiseList();
        if (diseList != null) {
            savePiDiseList(diseList, pkPi);
        }
        // 保存患者卡信息
        List<PiCard> cardLst = mParam.getCardList();
        if (cardLst != null) {
            savePiCardList(cardLst, pkPi);
        }

        return master;
    }

    /**
     * 修改人员手机号
     *
     * @param param
     * @param user
     * @return
     */
    public void savePiMasterMobileParam(String param, IUser user) {
        Map<String, Object> mapParam = JsonUtil.readValue(param, Map.class);
        if (mapParam == null) throw new BusException("未传入有效参数");
        if (mapParam.get("pkPi") == null) {
            throw new BusException("pkPi参数为空！");
        }
        if (mapParam.get("mobile") == null) {
            throw new BusException("患者手机号为空！");
        }
        DataBaseHelper.update("update pi_master set mobile=:mobile where pk_pi = :pkPi ", mapParam);
        Map<String, Object> msgParam = new HashMap<String, Object>();
        msgParam.put("isAdd", IsAdd.UPDATE);
        //发送患者信息
        msgParam.put("pkPi", mapParam.get("pkPi"));
        msgParam.put("pkEmp", UserContext.getUser().getPkEmp());
        msgParam.put("nameEmp", UserContext.getUser().getNameEmp());
        msgParam.put("codeEmp", UserContext.getUser().getCodeEmp());
        msgParam.put("pi", mapParam.get("pkPi"));
        PlatFormSendUtils.sendPiMasterMsg(msgParam);
    }

    public List<Map<String, Object>> qryPvCgInfos(String param, IUser user) {
        JSONObject jsonObject = JSONObject.parseObject(param);
        List<String> pkPvList = JsonUtil.readValue(
                jsonObject.get("pkPvList").toString(),
        new TypeReference<List<String>>() {
        });

        List<Map<String, Object>> retList = new ArrayList<>();

        if (pkPvList != null && pkPvList.size() > 0) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("pkList", pkPvList);
            paramMap.put("termOfValidity", jsonObject.get("termOfValidity"));
            if (Application.isSqlServer()) {
                retList = piPubMapper.qryPvCgInfoByMssql(paramMap);
            } else {
                retList = piPubMapper.qryPvCgInfos(paramMap);
            }
        }
        return retList;
    }

}
