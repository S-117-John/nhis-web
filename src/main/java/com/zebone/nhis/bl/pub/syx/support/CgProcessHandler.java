package com.zebone.nhis.bl.pub.syx.support;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.support.Constant;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.syx.vo.OrdNumVo;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.BillItemVo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.dao.BlIpPubMapper;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyBl;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.service.CnPiPubService;
import com.zebone.nhis.common.support.*;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 处理记费服务工具类
 *
 * @author yangxue
 */
@Service
public class CgProcessHandler {
    @Resource
    private CgQryMaintainService cgQryMaintainService;
    @Resource
	private InvSettltService invSettltService;
    @Resource
    private BlIpPubMapper blIpPubMapper;

    /**
     * 构造门诊记费明细
     *
     * @param items 收费项目集合
     * @return
     */
    public List<BlOpDt> constructBlOpDt(List<ItemPriceVo> items, String codeCg, int cgSort, PvEncounter pv,String sysParamBd0016) {
        List<BlOpDt> bods = new ArrayList<BlOpDt>();
        if (items == null || items.size() <= 0) return null;
        BigDecimal age = CnPiPubService.getPvAge(pv.getPkPi());//计算患者年龄
        //查询发票及账单码
        Set<String> itemSet = new HashSet<String>();
        Set<String> pdSet = new HashSet<String>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pkOrg", UserContext.getUser().getPkOrg());
        param.put("euType", Constant.OPINV);
        for (ItemPriceVo item : items) {
            if ("1".equals(item.getFlagPd())) {
                pdSet.add(item.getPkItem());
            } else {
                itemSet.add(item.getPkItem());
            }
        }
        param.put("itemList", CommonUtils.convertSetToSqlInPart(itemSet, "pk_item"));
        param.put("pdList", CommonUtils.convertSetToSqlInPart(pdSet, "pk_item"));
        List<BillItemVo> bills = cgQryMaintainService.qryBillAndAccountCodeByPkItems(param);
        if (bills == null || bills.size() <= 0)
            throw new BusException("调用门诊记费时，未获取到任何可用的账单码，请检查！");
        Set<String> itemPks = getPkItemByAttr(itemSet);
        User user = UserContext.getUser();
        for (ItemPriceVo pricevo : items) {
            BlOpDt bod = new BlOpDt();
            //相同属性进行拷贝
            BeanUtils.copyPropertiesIgnoreNull(pricevo,bod);
            if ("1".equals(bod.getFlagPd())) {
                bod.setPkPd(pricevo.getPkItem());
                bod.setPkItem(bod.getPkPd());
            }
            bod.setFlagSettle(EnumerateParameter.ZERO);
            bod.setFlagAcc(EnumerateParameter.ZERO);
            bod.setFlagInsu(EnumerateParameter.ZERO);
            bod.setFlagRecharge(EnumerateParameter.ZERO);
            bod.setCodeCg(codeCg);// 记费编码
            bod.setDateCg(new Date());// 记费日期
            bod.setSpec(pricevo.getSpec());
            //如果序号为空，从1开始递增
            if (pricevo.getSortno() == 0) {
                bod.setSortno(cgSort);
                cgSort++;
            } else {
                bod.setSortno(pricevo.getSortno());
            }
            //获取当前时间yyyy-MM-dd
            Date dateHap = null;
            if (pricevo.getDateHap() != null)
                dateHap = DateUtils.strToDate(DateUtils.formatDate(pricevo.getDateHap(), "yyyy-MM-dd"), "yyyy-MM-dd");
            else
                dateHap = DateUtils.strToDate(DateUtils.getDateTime(), "yyyy-MM-dd");
            bod.setDateHap(dateHap);
            bod.setPriceOrg(pricevo.getPriceOrg());
            bod.setPkDisc(pricevo.getPkDisc());
            bod.setRatioDisc(pricevo.getRatioDisc() == null ? 1D : pricevo.getRatioDisc());
            bod.setRatioSelf(pricevo.getRatioSelf() == null ? 1D : pricevo.getRatioSelf());
            bod.setRatioPock(pricevo.getRatioPock() == null ? 1D : pricevo.getRatioPock());
            bod.setAmountPock(pricevo.getAmountPock() == null ? 0D : pricevo.getAmountPock());
            bod.setRatioAdd(0D);
            bod.setAmountAdd(0D);
            Double priceOld = bod.getPriceOrg();

            //判断年龄加收和儿童加收是否同时生效，0特诊加收不再叠加年龄加收策略，1年龄加收基础上叠加特诊加收策略
            if(CommonUtils.isEmptyString(sysParamBd0016) || !EnumerateParameter.ONE.equals(sysParamBd0016)){
                //ratioAdd赋值  特诊加收优先，儿童加收随后
                if (BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())) {
                    //特诊加收模式  0:比例   1:金额
                    if (EnumerateParameter.ZERO.equals(pricevo.getEuSpmode())) {
                        bod.setRatioAdd(pricevo.getRatioSpec() != null ? pricevo.getRatioSpec() : 0D);    //特诊加收比例
                        //计算项目原始单价
                        priceOld = MathUtils.div(bod.getPriceOrg(), MathUtils.add(1D, bod.getRatioAdd()), 6);
                        //特诊加收金额 price_org*quan*ratio_add
                        bod.setAmountAdd(MathUtils.mul(MathUtils.mul(priceOld, bod.getQuan()), bod.getRatioAdd()));
                    } else if (EnumerateParameter.ONE.equals(pricevo.getEuSpmode())) {
                        bod.setRatioAdd(pricevo.getAmountSpec() != null ? pricevo.getAmountSpec() : 0D);    //特诊加收金额
                        //计算项目原始单价
                        priceOld = MathUtils.sub(bod.getPriceOrg(), bod.getRatioAdd());
                        //特诊加收金额 quan*ratio_add
                        bod.setAmountAdd(MathUtils.mul(bod.getRatioAdd(), bod.getQuan()));
                    }
                } else if (new BigDecimal(6).compareTo(age) > 0) {
                    //儿童加收模式  0:比例   1:金额
                    if (EnumerateParameter.ZERO.equals(pricevo.getEuCdmode())) {
                        bod.setRatioAdd(pricevo.getRatioChildren() != null ? pricevo.getRatioChildren() : 0D);    //儿童加收比例
                        //计算项目原始单价
                        priceOld = MathUtils.div(bod.getPriceOrg(), MathUtils.add(1D, bod.getRatioAdd()), 6);
                        //儿童加收金额 price_org*quan*ratio_add
                        bod.setAmountAdd(MathUtils.mul(MathUtils.mul(priceOld, bod.getQuan()), bod.getRatioAdd()));
                    } else if (EnumerateParameter.ONE.equals(pricevo.getEuCdmode())) {
                        bod.setRatioAdd(pricevo.getAmountChildren() != null ? pricevo.getAmountChildren() : 0D);    //儿童加收金额
                        //计算项目原始单价
                        priceOld = MathUtils.sub(bod.getPriceOrg(), bod.getRatioAdd());
                        //特诊加收金额 quan*ratio_add
                        bod.setAmountAdd(MathUtils.mul(bod.getRatioAdd(), bod.getQuan()));
                    }
                }
            }else{
                priceOld = bod.getPriceOrg();

                if (BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())) {
                    //特诊加收模式  0:比例   1:金额
                    if (EnumerateParameter.ZERO.equals(pricevo.getEuSpmode())) {
                        bod.setRatioAdd(pricevo.getRatioSpec() != null ? pricevo.getRatioSpec() : 0D);    //特诊加收比例
                        //计算项目原始单价
                        priceOld = MathUtils.div(bod.getPriceOrg(), MathUtils.add(1D, bod.getRatioAdd()), 6);
                        //特诊加收金额 price_org*quan*ratio_add
                        bod.setAmountAdd(MathUtils.mul(MathUtils.mul(priceOld, bod.getQuan()), bod.getRatioAdd()));
                    } else if (EnumerateParameter.ONE.equals(pricevo.getEuSpmode())) {
                        bod.setRatioAdd(pricevo.getAmountSpec() != null ? pricevo.getAmountSpec() : 0D);    //特诊加收金额
                        //计算项目原始单价
                        priceOld = MathUtils.sub(bod.getPriceOrg(), bod.getRatioAdd());
                        //特诊加收金额 quan*ratio_add
                        bod.setAmountAdd(MathUtils.mul(bod.getRatioAdd(), bod.getQuan()));
                    }
                }

                if (new BigDecimal(6).compareTo(age) > 0) {
                    Double tmpPrice = priceOld;
                    //计算原始单价
                    if(pricevo.getRatioPock()!=null){
                        if(pricevo.getRatioPock().compareTo(Double.valueOf(0d))==0){
                            tmpPrice = pricevo.getAmountPock();
                        }else{
                            tmpPrice = MathUtils.div(tmpPrice, pricevo.getRatioPock(), 6);
                        }
                    }

                    //儿童加收模式  0:比例   1:金额
                    if (EnumerateParameter.ZERO.equals(pricevo.getEuCdmode())) {
                        bod.setRatioAdd(pricevo.getRatioChildren() != null ? pricevo.getRatioChildren() : 0D);    //儿童加收比例
                        //计算项目原始单价
                        tmpPrice = MathUtils.div(tmpPrice, MathUtils.add(1D, bod.getRatioAdd()), 6);
                        //儿童加收金额 price_org*quan*ratio_add
                        bod.setAmountAdd(MathUtils.mul(MathUtils.mul(tmpPrice, bod.getQuan()), bod.getRatioAdd()));
                    } else if (EnumerateParameter.ONE.equals(pricevo.getEuCdmode())) {
                        bod.setRatioAdd(pricevo.getAmountChildren() != null ? pricevo.getAmountChildren() : 0D);    //儿童加收金额
                        //计算项目原始单价
                        tmpPrice = MathUtils.sub(tmpPrice, bod.getRatioAdd());
                        //特诊加收金额 quan*ratio_add
                        bod.setAmountAdd(MathUtils.mul(bod.getRatioAdd(), bod.getQuan()));
                    }
                }

                if(pricevo.getRatioPock()!=null){
                    bod.setAmountPock(MathUtils.mul(pricevo.getAmountPock(),bod.getQuan()));
                }
            }

            //amount，金额，price_org*quan+amount_add
            bod.setAmount(MathUtils.mul(bod.getQuan(), bod.getPriceOrg()));
            //amount_hppi，患者支付的医保金额，price*quan*ratio_self；
            if ("1".equals(pricevo.getFlagHppi())) {
                bod.setAmountHppi(pricevo.getAmtHppi().doubleValue());
            } else {
                bod.setAmountHppi(MathUtils.mul(MathUtils.mul(bod.getPrice(), bod.getQuan()), bod.getRatioSelf()));
            }
            //amount_pi，amount_hppi-[price_org*(1-ratio_disc)*quan]+amount_add，计算结果小于0时为0；
            //Double amt = MathUtils.add(MathUtils.sub(vo.getAmountHppi(), MathUtils.mul(MathUtils.mul(vo.getPriceOrg(), MathUtils.sub(1D, vo.getRatioDisc())), vo.getQuan())), vo.getAmountAdd());
            Double amt = BlcgUtil.getCgOpCustomerService().calcAmountPi(pricevo, bod);
            //因静配自动记费可能会传数量<0的计费项目，所以判断amt_pi字段赋值时增加校验条件。
            if (MathUtils.compareTo(amt, 0D) < 0 && bod.getQuan() > 0) {
                bod.setAmountPi(0D);
            } else {
                bod.setAmountPi(amt);
            }
            //price_org重新赋值(保存为未加收的原始单价)
            bod.setPriceOrg(priceOld);
            //校验ratio_disc和ratio_self为1时，amount和amount_pi和amount_hppi是否相等，不相等时以amount_pi的值为准
            if (MathUtils.compareTo(bod.getRatioDisc(), 1D) == 0 &&
                    MathUtils.compareTo(bod.getRatioSelf(), 1D) == 0 &&
                    MathUtils.compareTo(bod.getAmount(), bod.getAmountPi()) != 0) {
                bod.setAmount(bod.getAmountPi());
                bod.setAmountHppi(bod.getAmountPi());
            }
            for (BillItemVo bill : bills) {
                if ("bill".equals(bill.getBillType()) && bill.getFlagPd().equals(bod.getFlagPd()) && bill.getPkItem().equals(bod.getPkItem())) {
                    bod.setCodeBill(bill.getCodeBill());
                }
                if ("acc".equals(bill.getBillType()) && bill.getFlagPd().equals(bod.getFlagPd()) && bill.getPkItem().equals(bod.getPkItem())) {
                    bod.setCodeAudit(bill.getCodeBill());
                }
            }
            if (CommonUtils.isEmptyString(bod.getCodeBill()))
                throw new BusException("调用门诊记费时，【" + bod.getNameCg() + "】未获取到对应账单码，请检查！");
            bod.setPkOrgApp(user.getPkOrg());//开立机构
            bod.setPkDeptApp(CommonUtils.isEmptyString(pricevo.getPkDeptApp()) ? pv.getPkDept() : pricevo.getPkDeptApp());
            bod.setPkEmpApp(CommonUtils.isEmptyString(pricevo.getPkEmpApp()) ? user.getPkEmp() : pricevo.getPkEmpApp());
            bod.setNameEmpApp(CommonUtils.isEmptyString(pricevo.getNameEmpApp()) ? user.getNameEmp() : pricevo.getNameEmpApp());//当前用户姓名；
            bod.setPkOrgEx(user.getPkOrg());
            if (!"1".equals(bod.getFlagPd()) && itemPks.contains(bod.getPkItem())) {
                bod.setPkDeptEx(CommonUtils.isEmptyString(pv.getPkDeptArea()) ? pv.getPkDept() : pv.getPkDeptArea());
            } else {
                bod.setPkDeptEx(CommonUtils.isEmptyString(pricevo.getPkDeptEx()) ? pv.getPkDept() : pricevo.getPkDeptEx());//当前用户姓名；
            }
            bod.setPkDeptCg(CommonUtils.isEmptyString(pricevo.getPkDeptCg()) ? pv.getPkDept() : pricevo.getPkDeptCg());
            bod.setPkEmpCg(CommonUtils.isEmptyString(pricevo.getPkEmpCg()) ? user.getPkEmp() : pricevo.getPkEmpCg());
            bod.setNameEmpCg(CommonUtils.isEmptyString(pricevo.getNameEmpCg()) ? user.getNameEmp() : pricevo.getNameEmpCg());//当前用户姓名；
            ApplicationUtils.setDefaultValue(bod, true);// 设置默认字段
            bods.add(bod);
        }
        return bods;
    }

    public Set<String> getPkItemByAttr(Set<String> itemSet) {
        //0113	门诊记费需要记录到诊区的项目
        List<String> list = new ArrayList<>(8);
        if (Application.isSqlServer()) {
            list = DataBaseHelper.getJdbcTemplate()
                    .queryForList("select item.PK_ITEM from BD_ITEM item inner join bd_dictattr att on item.PK_ITEM=att.pk_dict" +
                            " inner join bd_dictattr_temp tmp on att.pk_dictattrtemp=tmp.pk_dictattrtemp and tmp.dt_dicttype='01' and tmp.code_attr='0113' " +
                            " where isnull(att.VAL_ATTR,'0') ='1' and item.PK_ITEM in(" + CommonUtils.convertSetToSqlInPart(itemSet, "pk_item") + ")", String.class);
        } else {
            list = DataBaseHelper.getJdbcTemplate()
                    .queryForList("select item.PK_ITEM from BD_ITEM item inner join bd_dictattr att on item.PK_ITEM=att.pk_dict" +
                            " inner join bd_dictattr_temp tmp on att.pk_dictattrtemp=tmp.pk_dictattrtemp and tmp.dt_dicttype='01' and tmp.code_attr='0113' " +
                            " where nvl(att.VAL_ATTR,'0') ='1' and item.PK_ITEM in(" + CommonUtils.convertSetToSqlInPart(itemSet, "pk_item") + ")", String.class);
        }
        return new HashSet<>(list);
    }

    /**
     * webService通过ApplicationUtils.execService方法调用改方法，无交易号
     *
     * @param param
     * @param user
     * @return
     */
    public List<BlOpDt> constructBlOpDtWs(String param, IUser user) {
        PvEncounter pvInfo = JsonUtil.readValue(JsonUtil.getJsonNode(param, "pv"), PvEncounter.class);
        String codeCg = JsonUtil.getFieldValue(param, "codeCg");
        int cgSort = Integer.parseInt(JsonUtil.getFieldValue(param, "cgSort"));
        List<ItemPriceVo> items = JsonUtil.readValue(JsonUtil.getJsonNode(param, "items"), new TypeReference<List<ItemPriceVo>>() {
        });
        //判断年龄加收和儿童加收是否同时生效，0特诊加收不再叠加年龄加收策略，1年龄加收基础上叠加特诊加收策略
        String sysParamBd0016 = ApplicationUtils.getSysparam("BD0016", false);
        return constructBlOpDt(items, codeCg, cgSort, pvInfo,sysParamBd0016);
    }

    /**
     * 构建调用记费策略所需参数
     *
     * @param blCgPubParamVo
     * @return
     */
    public ItemPriceVo constructParam(BlPubParamVo blCgPubParamVo) {
        ItemPriceVo itemvo = new ItemPriceVo();
        itemvo.setPkWg(blCgPubParamVo.getPkWg());
        itemvo.setPkWgEx(blCgPubParamVo.getPkWgEx());
        itemvo.setPkWgOrg(blCgPubParamVo.getPkWgOrg());
        itemvo.setBatchNo(blCgPubParamVo.getBatchNo());
        itemvo.setDateExpire(blCgPubParamVo.getDateExpire());
        itemvo.setDateHap(blCgPubParamVo.getDateHap());
        itemvo.setEuAdditem(blCgPubParamVo.getEuAdditem());
        itemvo.setFlagPd(blCgPubParamVo.getFlagPd());
        itemvo.setFlagPv(blCgPubParamVo.getFlagPv());
        itemvo.setPackSize(blCgPubParamVo.getPackSize());
        itemvo.setPkCnord(blCgPubParamVo.getPkCnord());
        itemvo.setDateStart(blCgPubParamVo.getDateStart());
        itemvo.setCodeOrdtype(blCgPubParamVo.getCodeOrdtype());
        itemvo.setFlagFit(blCgPubParamVo.getFlagFit());
        itemvo.setDescFit(blCgPubParamVo.getDescFit());
        itemvo.setOrdsn(blCgPubParamVo.getOrdsn());
        itemvo.setOrdsnParent(blCgPubParamVo.getOrdsnParent());
        itemvo.setFlagSign(blCgPubParamVo.getFlagSign());
        itemvo.setDtSamptype(blCgPubParamVo.getDtSamptype());
        itemvo.setDateCg(blCgPubParamVo.getDateCg());
        itemvo.setSortno(blCgPubParamVo.getSortno());
        itemvo.setDosage(blCgPubParamVo.getDosage());
        itemvo.setUnitDos(blCgPubParamVo.getUnitDos());
        itemvo.setNameSupply(blCgPubParamVo.getNameSupply());
        itemvo.setNameFreq(blCgPubParamVo.getNameFreq());
        itemvo.setPkDeptNsApp(blCgPubParamVo.getPkDeptNsApp());// 开立病区
        itemvo.setPkEmpApp(blCgPubParamVo.getPkEmpApp());
        itemvo.setNameEmpApp(blCgPubParamVo.getNameEmpApp());
        itemvo.setPkDeptEx(blCgPubParamVo.getPkDeptEx());
        itemvo.setPkDeptCg(blCgPubParamVo.getPkDeptCg());
        itemvo.setPkEmpCg(blCgPubParamVo.getPkEmpCg());
        itemvo.setNameEmpCg(blCgPubParamVo.getNameEmpCg());
        itemvo.setPkDeptJob(blCgPubParamVo.getPkDeptJob());
        itemvo.setPkDeptApp(blCgPubParamVo.getPkDeptApp());
        itemvo.setPkItemOld(blCgPubParamVo.getPkItem());
        itemvo.setPkOrdOld(blCgPubParamVo.getPkOrd());
        itemvo.setPkOrgApp(blCgPubParamVo.getPkOrgApp());
        itemvo.setPkOrgEx(blCgPubParamVo.getPkOrgEx());
        itemvo.setPkPi(blCgPubParamVo.getPkPi());
        itemvo.setPkPres(blCgPubParamVo.getPkPres());
        itemvo.setPkPv(blCgPubParamVo.getPkPv());
        itemvo.setPkUnitPd(blCgPubParamVo.getPkUnitPd());
        itemvo.setPrice(blCgPubParamVo.getPrice() != null ? blCgPubParamVo.getPrice() : new Double(0));
        itemvo.setQuanOld(blCgPubParamVo.getQuanCg());
        itemvo.setSpec(blCgPubParamVo.getSpec());
        itemvo.setPkOrdexdt(blCgPubParamVo.getPkOrdexdt());
        itemvo.setPkPdstdt(blCgPubParamVo.getPkPdstdt());
        itemvo.setBarcode(blCgPubParamVo.getBarcode());
        itemvo.setPkEmpEx(blCgPubParamVo.getPkEmpEx());
        itemvo.setNameEmpEx(blCgPubParamVo.getNameEmpEx());
        itemvo.setEuBltype(blCgPubParamVo.getEuBltype());
        itemvo.setPkCnordRl(blCgPubParamVo.getPkCnordRl());
        itemvo.setIsFixedCg(blCgPubParamVo.getIsFixedCg());
        if ("1".equals(blCgPubParamVo.getFlagPd())) {
            //将收费项目主键设置为物品主键
            itemvo.setPkItem(blCgPubParamVo.getPkOrd());
            itemvo.setPriceCs(blCgPubParamVo.getPrice());
            itemvo.setQuan(blCgPubParamVo.getQuanCg());
            //设置费用分类
            BdPd pd = blIpPubMapper.getPdInfoByPk(blCgPubParamVo.getPkOrd());
            itemvo.setPkItemcate(pd.getPkItemcate());
            itemvo.setSpec(pd.getSpec());
            itemvo.setNameCg(pd.getName());
            itemvo.setName(pd.getName());
            itemvo.setPkUnit(itemvo.getPkUnitPd());
        }
        //针对药品，原始单价设置为零售单价
        itemvo.setPriceCost(blCgPubParamVo.getPriceCost());
        itemvo.setPriceOrg(blCgPubParamVo.getPrice());
        itemvo.setPkDeptAreaapp(blCgPubParamVo.getPkDeptAreaapp());
        itemvo.setFlagSelf(blCgPubParamVo.getFlagSelf());
        if (blCgPubParamVo.getRatioDisc() != null)
            itemvo.setRatioDisc(blCgPubParamVo.getRatioDisc());//设置优惠比例-外部传入
        if (blCgPubParamVo.getRatioSelf() != null)
            itemvo.setRatioSelf(blCgPubParamVo.getRatioSelf());//设置自付比例-外部传入(对应覆盖bd_ord_item表中ratio_self)字段
        return itemvo;
    }

    /**
     * @param pricelist
     * @return
     */
    public Map<String, Object> constructBlIpDt(String codeCg, List<ItemPriceVo> pricelist, PvEncounter pv, int cgSortNo, BigDecimal age) {
        List<BlIpDt> dtlist = new ArrayList<BlIpDt>();
        BigDecimal totalAmt = BigDecimal.ZERO;
        //获取患者入院日期和出院日期
        Date dateBegin = DateUtils.strToDate(DateUtils.formatDate(pv.getDateBegin(), "yyyy-MM-dd"), "yyyy-MM-dd");
        Date dateEnd = DateUtils.strToDate(pv.getDateEnd() != null ? DateUtils.formatDate(pv.getDateEnd(), "yyyy-MM-dd") : DateUtils.getDateTime(), "yyyy-MM-dd");
        //查询发票及账单码
        Set<String> itemSet = new HashSet<String>();
        Set<String> pdSet = new HashSet<String>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pkOrg", UserContext.getUser().getPkOrg());
        param.put("euType", Constant.IPINV);
        for (ItemPriceVo item : pricelist) {
            if ("1".equals(item.getFlagPd())) {
                pdSet.add(item.getPkItem());
            } else {
                itemSet.add(item.getPkItem());
            }
        }
        param.put("itemList", CommonUtils.convertSetToSqlInPart(itemSet, "pk_item"));
        param.put("pdList", CommonUtils.convertSetToSqlInPart(pdSet, "pk_item"));
        List<BillItemVo> bills = cgQryMaintainService.qryBillAndAccountCodeByPkItems(param);
        if (bills == null || bills.size() <= 0)
            throw new BusException("调用住院记费时，未获取到任何可用的账单码，请检查！");
        
        //获取开立医生考勤科室字段
        String pkDeptJob = invSettltService.getPkDept();
        for (ItemPriceVo item : pricelist) {
            BlIpDt vo = new BlIpDt();
            String pkCgip = NHISUUID.getKeyId();
            vo.setPkCgip(pkCgip);
            vo.setPkOrdexdt(item.getPkOrdexdt());
            vo.setPkPdstdt(item.getPkPdstdt());
            vo.setSpec(item.getSpec());
            vo.setPkOrg(UserContext.getUser().getPkOrg());
            vo.setPkPv(item.getPkPv());
            vo.setPkPi(item.getPkPi());
            vo.setPkOrgApp(item.getPkOrgApp());
            vo.setPkDeptApp(CommonUtils.isEmptyString(item.getPkDeptApp()) ? pv.getPkDept() : item.getPkDeptApp());
            vo.setPkOrgEx(item.getPkOrgEx());
            vo.setDosage(item.getDosage());
            vo.setUnitDos(item.getUnitDos());
            vo.setNameSupply(item.getNameSupply());
            vo.setNameFreq(item.getNameFreq());
            vo.setPkWg(item.getPkWg());
            vo.setPkWgEx(item.getPkWgEx());
            vo.setPkWgOrg(item.getPkWgOrg());
            //如果序号为空，从1开始递增
            if (item.getSortno() == 0) {
                vo.setSortno(cgSortNo);
                cgSortNo++;
            } else {
                vo.setSortno(item.getSortno());
            }
            vo.setPkDeptNsApp(CommonUtils.isEmptyString(item.getPkDeptNsApp()) ? pv.getPkDeptNs() : item.getPkDeptNsApp());// 开立病区
            vo.setPkEmpApp(CommonUtils.isEmptyString(item.getPkEmpApp()) ? pv.getPkEmpPhy() : item.getPkEmpApp());
            vo.setNameEmpApp(CommonUtils.isEmptyString(item.getNameEmpApp()) ? pv.getNameEmpPhy() : item.getNameEmpApp());
            vo.setPkDeptEx(CommonUtils.isEmptyString(item.getPkDeptEx()) ? pv.getPkDept() : item.getPkDeptEx());
            //医嘱自动记费不写记费部门、记费人员、记费人员姓名字段。
            if (!BlcgUtil.converToTrueOrFalse(item.getFlagSign())) {
                vo.setPkDeptCg(CommonUtils.isEmptyString(item.getPkDeptCg()) ? pv.getPkDept() : item.getPkDeptCg());
                vo.setPkEmpCg(CommonUtils.isEmptyString(item.getPkEmpCg()) ? UserContext.getUser().getPkEmp() : item.getPkEmpCg());
                vo.setNameEmpCg(CommonUtils.isEmptyString(item.getNameEmpCg()) ? UserContext.getUser().getNameEmp() : item.getNameEmpCg());//当前用户姓名；
            }

            //校验是否是固定记费，固定记费可以使用传入的date_cg
            if (item.getIsFixedCg() != null && item.getIsFixedCg()) {
                //校验dateCg是否为null
                if (item.getDateCg() != null)
                    vo.setDateCg(item.getDateCg());
                else
                    vo.setDateCg(new Date());
            } else {
                //赋值当前时间
                vo.setDateCg(new Date());
            }
            //录入时间(同date_cg)
            vo.setDateEntry(vo.getDateCg());
            //获取当前时间yyyy-MM-dd
            Date dateHap = null;
            if (item.getDateHap() != null) {
                dateHap = DateUtils.strToDate(DateUtils.formatDate(item.getDateHap(), "yyyy-MM-dd"), "yyyy-MM-dd");
                //校验传入的dateHap是否小于患者入院日期或者大于患者出院时间
                if (dateBegin != null && dateHap.compareTo(dateBegin) < 0) {
                    dateHap = dateBegin;//入院日期
                } else if (dateEnd != null && dateHap.compareTo(dateEnd) > 0) {
                    dateHap = dateEnd;
                }
            } else {
                dateHap = DateUtils.strToDate(DateUtils.getDateTime(), "yyyy-MM-dd");
            }
            vo.setDateHap(dateHap);
            vo.setPkUnit(item.getPkUnit());
            vo.setPkUnitPd(item.getPkUnitPd());
            vo.setSpec(item.getSpec());
            vo.setPackSize(item.getPackSize() == null ? 1D : item.getPackSize().doubleValue());
            //设置药品相关属性
            if ("1".equals(item.getFlagPd())) {//药品
                vo.setPkPd(item.getPkItem());
                vo.setPkUnit(item.getPkUnitPd());
                vo.setNameCg(item.getName());
            }
            vo.setBatchNo(item.getBatchNo());
            vo.setPrice(item.getPrice());
            vo.setPriceCost(item.getPriceCost());
            vo.setDateExpire(item.getDateExpire());
            vo.setPkItem(item.getPkItem());
            vo.setNameCg(item.getNameCg());
            vo.setPkItemcate(item.getPkItemcate());
            vo.setBarcode(item.getBarcode());
            //执行医生如果为空则填入记费医生
            vo.setPkEmpEx(CommonUtils.isEmptyString(item.getPkEmpEx()) ? item.getPkEmpCg() : item.getPkEmpEx());
            vo.setNameEmpEx(CommonUtils.isEmptyString(item.getNameEmpEx()) ? item.getNameEmpCg() : item.getNameEmpEx());
            vo.setEuBltype(item.getEuBltype());
            vo.setPkCnordRl(item.getPkCnordRl());
            vo.setPkPayer(item.getPkPayer());
            vo.setFlagInsu("0");
            vo.setFlagPd(item.getFlagPd());
            vo.setPkPres(item.getPkPres());
            vo.setPkCnord(item.getPkCnord());
            vo.setPkCgipBack(null);
            vo.setFlagSettle("0");
            vo.setCodeCg(codeCg);
            vo.setInfantNo("0");
            //添加组套主键、名称
            if (!item.getPkItem().equals(item.getPkItemOld())) {
                vo.setPkItemset(item.getPkItemOld());
                vo.setNameItemset(item.getNameItemOld());
            }
            // 设置单价，数量，金额，比例等信息
            // price_org,price,quan,amount,pk_disc,ratio_disc,ratio_self,amount_pi
            vo.setQuan(item.getQuan());
            vo.setPriceOrg(item.getPriceOrg());
            vo.setPkDisc(item.getPkDisc());
            vo.setRatioDisc(item.getRatioDisc() == null ? 1D : item.getRatioDisc());
            vo.setRatioSelf(item.getRatioSelf() == null ? 1D : item.getRatioSelf());
            vo.setRatioAdd(0D);
            vo.setAmountAdd(0D);
            Double priceOld = vo.getPriceOrg();
            //ratioAdd赋值  特诊加收优先，儿童加收随后
            if (BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())) {
                //特诊加收模式  0:比例   1:金额
                if (EnumerateParameter.ZERO.equals(item.getEuSpmode())) {
                    vo.setRatioAdd(item.getRatioSpec() != null ? item.getRatioSpec() : 0D);    //特诊加收比例
                    //计算项目原始单价
                    priceOld = MathUtils.div(vo.getPriceOrg(), MathUtils.add(1D, vo.getRatioAdd()), 6);
                    //特诊加收金额 price_org*quan*ratio_add
                    vo.setAmountAdd(MathUtils.mul(MathUtils.mul(priceOld, vo.getQuan()), vo.getRatioAdd()));
                } else if (EnumerateParameter.ONE.equals(item.getEuSpmode())) {
                    vo.setRatioAdd(item.getAmountSpec() != null ? item.getAmountSpec() : 0D);    //特诊加收金额
                    //计算项目原始单价
                    priceOld = MathUtils.sub(vo.getPriceOrg(), vo.getRatioAdd());
                    //特诊加收金额 quan*ratio_add
                    vo.setAmountAdd(MathUtils.mul(vo.getRatioAdd(), vo.getQuan()));
                }
            } else if (new BigDecimal(6).compareTo(age) > 0) {
                //儿童加收模式  0:比例   1:金额
                if (EnumerateParameter.ZERO.equals(item.getEuCdmode())) {
                    vo.setRatioAdd(item.getRatioChildren() != null ? item.getRatioChildren() : 0D);    //儿童加收比例
                    //计算项目原始单价
                    priceOld = MathUtils.div(vo.getPriceOrg(), MathUtils.add(1D, vo.getRatioAdd()), 6);
                    //儿童加收金额 price_org*quan*ratio_add
                    vo.setAmountAdd(MathUtils.mul(MathUtils.mul(priceOld, vo.getQuan()), vo.getRatioAdd()));
                } else if (EnumerateParameter.ONE.equals(item.getEuCdmode())) {
                    vo.setRatioAdd(item.getAmountChildren() != null ? item.getAmountChildren() : 0D);    //儿童加收金额
                    //计算项目原始单价
                    priceOld = MathUtils.sub(vo.getPriceOrg(), vo.getRatioAdd());
                    //特诊加收金额 quan*ratio_add
                    vo.setAmountAdd(MathUtils.mul(vo.getRatioAdd(), vo.getQuan()));
                }
            }
//			vo.setRatioAdd(item.getRatioSpec()!=null && BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())?item.getRatioSpec():0D);	//特诊加收比例
//			//如果特诊加收比例为0，判断患者年龄是否小于6岁
//			if((vo.getRatioAdd()==null || vo.getRatioAdd()==0D) && new BigDecimal(6).compareTo(age)>0)
//				vo.setRatioAdd(item.getRatioChildren()!=null?item.getRatioChildren():0D);	//特诊加收比例
//			//计算项目原始单价
//			Double priceOld = MathUtils.div(vo.getPriceOrg(), MathUtils.add(1D, vo.getRatioAdd()), 6);
//			//特诊加收金额 price_org*quan*ratio_add
//			vo.setAmountAdd(MathUtils.mul(MathUtils.mul(priceOld,vo.getQuan()), vo.getRatioAdd()));
            //amount，金额，price_org*quan+amount_add
            vo.setAmount(MathUtils.mul(vo.getQuan(), vo.getPriceOrg()));
            //amount_hppi，患者支付的医保金额，price*quan*ratio_self；
            if ("1".equals(item.getFlagHppi())) {
                vo.setAmountHppi(item.getAmtHppi().doubleValue());
            } else {
                vo.setAmountHppi(MathUtils.mul(MathUtils.mul(vo.getPrice(), vo.getQuan()), vo.getRatioSelf()));
            }
            //amount_pi，amount_hppi-[price_org*(1-ratio_disc)*quan]+amount_add，计算结果小于0时为0；
            //Double amt = MathUtils.add(MathUtils.sub(vo.getAmountHppi(), MathUtils.mul(MathUtils.mul(vo.getPriceOrg(), MathUtils.sub(1D, vo.getRatioDisc())), vo.getQuan())), vo.getAmountAdd());
            Double amt = MathUtils.sub(vo.getAmountHppi(), MathUtils.mul(MathUtils.mul(vo.getPriceOrg(), MathUtils.sub(1D, vo.getRatioDisc())), vo.getQuan()));
            //因静配自动记费可能会传数量<0的计费项目，所以判断amt_pi字段赋值时增加校验条件。
            if (MathUtils.compareTo(amt, 0D) < 0 && vo.getQuan() > 0) {
                vo.setAmountPi(0D);
            } else {
                vo.setAmountPi(amt);
            }
            //price_org重新赋值(保存为未加收的原始单价)
            vo.setPriceOrg(priceOld);
            //校验ratio_disc和ratio_self为1时，amount和amount_pi和amount_hppi是否相等，不相等时以amount_pi的值为准
            if (MathUtils.compareTo(vo.getRatioDisc(), 1D) == 0 &&
                    MathUtils.compareTo(vo.getRatioSelf(), 1D) == 0 &&
                    MathUtils.compareTo(vo.getAmount(), vo.getAmountPi()) != 0) {
                vo.setAmount(vo.getAmountPi());
                vo.setAmountHppi(vo.getAmountPi());
            }
            for (BillItemVo bill : bills) {
                if ("bill".equals(bill.getBillType()) && bill.getFlagPd().equals(vo.getFlagPd()) && bill.getPkItem().equals(vo.getPkItem())) {
                    vo.setCodeBill(bill.getCodeBill());
                }
                if ("acc".equals(bill.getBillType()) && bill.getFlagPd().equals(vo.getFlagPd()) && bill.getPkItem().equals(vo.getPkItem())) {
                    vo.setCodeAudit(bill.getCodeBill());
                }
            }
            if (CommonUtils.isEmptyString(vo.getCodeBill()))
                throw new BusException("调用住院记费时，【" + vo.getNameCg() + "】未获取到对应账单码，请检查！");
            ApplicationUtils.setDefaultValue(vo, true);
            //组装最终集合之前过滤amount=0的记费信息，保存amount=0的信息在结算时上传记费明细到医保端可能会出问题，所以记费时过滤费用为0的项目。
            if (!MathUtils.equ(vo.getAmount(), 0.0)
                    && !MathUtils.equ(vo.getQuan(), 0.0)) {
            	vo.setPkDeptJob(pkDeptJob);
                dtlist.add(vo);
                totalAmt = totalAmt.add(new BigDecimal(vo.getAmountPi()));
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (dtlist != null && dtlist.size() > 0)
            map.put("dtlist", dtlist);
        map.put("curAmt", totalAmt);
        return map;
    }

    public List<InsGzgyBl> constructInsGzgyBl(List<BlIpDt> blIpList, PvEncounter pv) {
        List<InsGzgyBl> gyBlList = new ArrayList<>();
        for (BlIpDt dt : blIpList) {
            InsGzgyBl gyBl = new InsGzgyBl();
            gyBl.setPkGzgybl(NHISUUID.getKeyId());
            gyBl.setPkCnord(dt.getPkCnord());         //关联医嘱主键
            gyBl.setPkCg(dt.getPkCgip());    //关联的记费(bl_ip_dt)主键
            gyBl.setPkPv(dt.getPkPv());    //关联的患者就诊
            gyBl.setEuItemtype(dt.getFlagPd());         //0非药品，1药品
            gyBl.setPkItem(dt.getPkItem());    //关联的收费项目或药品项目主键
            gyBl.setPkItemcate(dt.getPkItemcate());  //关联的收费项目分类
            gyBl.setNameCg(dt.getNameCg());    //收费名称
            gyBl.setSpec(dt.getSpec());    //规格
            gyBl.setPrice(dt.getPrice());    //单价
            gyBl.setQuan(dt.getQuan());    //数量
            gyBl.setAmount(dt.getAmount()); //总金额
            gyBl.setRatio(dt.getRatioSelf());        //患者自付比例
            gyBl.setAmountPi(dt.getAmountHppi());    //患者自付金额
            gyBl.setAmountHp(MathUtils.sub(gyBl.getAmount(), gyBl.getAmountPi()));        //医保支付金额
            gyBl.setAmountUnit(new Double(0));        //单位支付金额
            gyBl.setDateHap(dt.getDateHap());        //费用发生日期
            gyBl.setDateCg(dt.getDateCg());    //记费日期
            gyBlList.add(gyBl);
        }
        return gyBlList;
    }

    public List<InsGzgyBl> constructInsGzgyBlByOp(List<BlOpDt> blIpList, PvEncounter pv) {
        List<InsGzgyBl> gyBlList = new ArrayList<>();
        for (BlOpDt dt : blIpList) {
            InsGzgyBl gyBl = new InsGzgyBl();
            gyBl.setPkGzgybl(NHISUUID.getKeyId());
            gyBl.setPkCnord(dt.getPkCnord());         //关联医嘱主键
            gyBl.setPkCg(dt.getPkCgop());    //关联的记费(bl_op_dt)主键
            gyBl.setPkPv(dt.getPkPv());    //关联的患者就诊
            gyBl.setEuItemtype(dt.getFlagPd());         //0非药品，1药品
            gyBl.setPkItem(dt.getPkItem());    //关联的收费项目或药品项目主键
            gyBl.setPkItemcate(dt.getPkItemcate());  //关联的收费项目分类
            gyBl.setNameCg(dt.getNameCg());    //收费名称
            gyBl.setSpec(dt.getSpec());    //规格
            gyBl.setPrice(dt.getPrice());    //单价
            gyBl.setQuan(dt.getQuan());    //数量
            gyBl.setAmount(dt.getAmount()); //总金额
            gyBl.setRatio(dt.getRatioSelf());        //患者自付比例
            gyBl.setAmountPi(dt.getAmountHppi());    //患者自付金额
            gyBl.setAmountHp(MathUtils.sub(gyBl.getAmount(), gyBl.getAmountPi()));        //医保支付金额
            gyBl.setAmountUnit(new Double(0));        //单位支付金额
            gyBl.setDateHap(dt.getDateHap());        //费用发生日期
            gyBl.setDateCg(dt.getDateCg());    //记费日期
            gyBlList.add(gyBl);
        }
        return gyBlList;
    }

    /**
     * 根据医嘱项目合并代码合并项目
     *
     * @param itemList 原始记费入参
     * @param sampList 标本容器费用入参
     * @param combList 合并代码集合 key:pk_cnord、code_comb、pk_ord
     */
    public void hbOrdInfoBycodeComb(List<BlPubParamVo> itemList, List<BlPubParamVo> sampList, List<Map<String, Object>> combList) {
        if (combList != null && combList.size() > 0) {
            //过滤相同检验项目,相同检验项目的试管不合并
//			for(int i =combList.size() - 1; i >= 0; i--){
//				for(int j =combList.size() - 1; j >= 0; j--){
//					if(!CommonUtils.getPropValueStr(combList.get(i),"pkCnord").equals(CommonUtils.getPropValueStr(combList.get(j),"pkCnord"))
//					&& CommonUtils.getPropValueStr(combList.get(i),"pkOrd").equals(CommonUtils.getPropValueStr(combList.get(j),"pkOrd"))){
//						combList.remove(i);
//						break;
//					}
//				}
//			}
            if (combList != null && combList.size() > 0) {
                //itemList删除所有试管附加费用
                for (BlPubParamVo sampVo : sampList) {
                    for (int i = itemList.size() - 1; i >= 0; i--) {
                        if (itemList.get(i).getSampTube() != null
                                && !CommonUtils.isEmptyString(itemList.get(i).getPkItem())
                                && itemList.get(i).getSampTube()
                                && itemList.get(i).getPkCnord().equals(sampVo.getPkCnord())
                                && sampVo.getPkItem().equals(itemList.get(i).getPkItem())) {
                            itemList.remove(i);
                            break;
                        }
                    }
                }
                //codeComb信息赋值到sampList集合
                for (Map<String, Object> combMap : combList) {
                    for (BlPubParamVo sampVo : sampList) {
                        if (CommonUtils.getPropValueStr(combMap, "pkCnord").equals(sampVo.getPkCnord())) {
                            sampVo.setCodeComb(CommonUtils.getPropValueStr(combMap, "codeComb"));
                        }
                    }
                }
                //标本费用合并，不需要校验合并代码
                //容器费用合并，需要校验合并代码
                //合并符合条件的项目
                for (int i = sampList.size() - 1; i >= 0; i--) {
                    for (int j = sampList.size() - 1; j >= 0; j--) {
                        if (!sampList.get(i).getPkCnord().equals(sampList.get(j).getPkCnord()) //非相同医嘱
                                && !sampList.get(i).getPkOrdChk().equals(sampList.get(j).getPkOrdChk())
                                && sampList.get(i).getPkItem().equals(sampList.get(j).getPkItem())    //收费项目一样
                                && sampList.get(i).getOrdCntChk() != null && sampList.get(j).getOrdCntChk() != null) {
                            //此if判断是否为标本收费
                            if ("标本收费".equals(sampList.get(i).getSpec())
                                    && "标本收费".equals(sampList.get(j).getSpec())) {
                                //该if判断此次收费的医嘱项目数量，删除医嘱数量少的容器费
                                if (sampList.get(i).getOrdCntChk().compareTo(sampList.get(j).getOrdCntChk()) >= 0) {
                                    //该if判断收费项目数量，删除收费项目数量少的容器费
                                    if (sampList.get(i).getQuanCg().compareTo(sampList.get(j).getQuanCg()) >= 0) {
                                        sampList.remove(j);
                                    } else {
                                        sampList.remove(i);
                                    }
                                } else {
                                    //该if判断收费项目数量，删除收费项目数量少的容器费
                                    if (sampList.get(i).getQuanCg().compareTo(sampList.get(j).getQuanCg()) > 0) {
                                        sampList.remove(j);
                                    } else {
                                        sampList.remove(i);
                                    }
                                }
                                break;
                            } else {
                                //容器收费，合并代码一样参与合并
                                if (!CommonUtils.isEmptyString(sampList.get(i).getCodeComb())
                                        && !CommonUtils.isEmptyString(sampList.get(j).getCodeComb())
                                        && sampList.get(i).getCodeComb().equals(sampList.get(j).getCodeComb())) {
                                    //该if判断此次收费的医嘱项目数量，删除医嘱数量少的容器费
                                    if (sampList.get(i).getOrdCntChk().compareTo(sampList.get(j).getOrdCntChk()) >= 0) {
                                        //该if判断收费项目数量，删除收费项目数量少的容器费
                                        if (sampList.get(i).getQuanCg().compareTo(sampList.get(j).getQuanCg()) >= 0) {
                                            sampList.remove(j);
                                        } else {
                                            sampList.remove(i);
                                        }
                                    } else {
                                        //该if判断收费项目数量，删除收费项目数量少的容器费
                                        if (sampList.get(i).getQuanCg().compareTo(sampList.get(j).getQuanCg()) > 0) {
                                            sampList.remove(j);
                                        } else {
                                            sampList.remove(i);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                //合并符合条件的项目
                for (int i = sampList.size() - 1; i >= 0; i--) {
                    for (int j = sampList.size() - 1; j >= 0; j--) {
                        if (!CommonUtils.isEmptyString(sampList.get(i).getCodeComb())
                                && !CommonUtils.isEmptyString(sampList.get(j).getCodeComb())
                                && !sampList.get(i).getPkCnord().equals(sampList.get(j).getPkCnord()) //非相同医嘱
                                && !sampList.get(i).getPkOrdChk().equals(sampList.get(j).getPkOrdChk())
                                && sampList.get(i).getPkItem().equals(sampList.get(j).getPkItem())    //收费项目一样
                                && sampList.get(i).getCodeComb().equals(sampList.get(j).getCodeComb())
                                && sampList.get(i).getOrdCntChk() != null && sampList.get(j).getOrdCntChk() != null) {//合并代码一样
                            //保留数量最大的项目
                            if (sampList.get(i).getOrdCntChk().compareTo(sampList.get(j).getOrdCntChk()) > 0) {
                                sampList.remove(j);
                            } else {
                                sampList.remove(i);
                            }
                            break;
                        }
                    }
                }
                //重新组织入参
                itemList.addAll(sampList);
            }
        }
    }

    /**
     * 合并检验医嘱下相同的收费项目
     *
     * @param jyVo      检验医嘱1
     * @param itemVo    检验医嘱2
     * @param priceList 价格集合
     * @param ordNumMap 检验医嘱收费项目数量集合
     */
    public void hBOrdItem(ItemPriceVo jyVo, ItemPriceVo itemVo, List<ItemPriceVo> priceList, List<OrdNumVo> ordNumList) {
        for (int i = priceList.size() - 1; i >= 0; i--) {
            //校验是否是jyVo下的收费项目
            if (CommonUtils.isEmptyString(priceList.get(i).getPkOrdOld())
                    || !jyVo.getPkOrdOld().equals(priceList.get(i).getPkOrdOld())
                    || !jyVo.getPkCnord().equals(priceList.get(i).getPkCnord()))
                continue;
            for (int j = priceList.size() - 1; j >= 0; j--) {
                //校验是否是itemVo下的收费项目
                if (CommonUtils.isEmptyString(priceList.get(j).getPkOrdOld())
                        || !itemVo.getPkOrdOld().equals(priceList.get(j).getPkOrdOld())
                        || !itemVo.getPkCnord().equals(priceList.get(j).getPkCnord()))
                    continue;
                //比较jyVo下的收费项目和itemVo下的收费项目主键是否相同
                if (priceList.get(i).getPkItem().equals(priceList.get(j).getPkItem())) {
                    //比较两组检验医嘱的收费项目数量，哪组数量多，删哪组医嘱下的收费项目。
                    Double jyVoNum = getOrdItemNum(ordNumList, jyVo.getPkOrdOld());
                    Double itemVoNum = getOrdItemNum(ordNumList, itemVo.getPkOrdOld());
                    if (jyVoNum > itemVoNum) {
                        priceList.remove(priceList.get(i));
                        break;
                    } else {
                        priceList.remove(priceList.get(j));
                        break;
                    }
                }
            }
        }
    }

    /**
     * 获取医嘱下的收费项目数量
     *
     * @param pkOrd
     * @return
     */
    public Double getOrdItemNum(List<OrdNumVo> ordNumList, String pkOrd) {
        Double itemNum = 0D;
        for (OrdNumVo vo : ordNumList) {
            if (pkOrd.equals(vo.getPkOrd())) {
                itemNum = vo.getOrdItemNum();
                break;
            }
        }
        return itemNum;
    }

    /**
     * 重新计算收费项目价格
     * 如果医嘱项目下收费项目数量>8,则此医嘱下的收费项目单价打8折
     *
     * @param priceList
     * @param itemList
     * @return
     */
    public List<ItemPriceVo> jsOrdItemRrice(List<ItemPriceVo> priceList, List<ItemPriceVo> itemList) {
        for (ItemPriceVo itemVo : itemList) {
            //存放医嘱下收费项目
            List<ItemPriceVo> tempList = new ArrayList<>();
            for (int i = priceList.size() - 1; i >= 0; i--) {
                if (!CommonUtils.isEmptyString(priceList.get(i).getPkOrdOld())
                        && priceList.get(i).getPkOrdOld().equals(itemVo.getPkOrdOld())
                        && itemVo.getPkCnord().equals(priceList.get(i).getPkCnord())) {
                    tempList.add(priceList.get(i));
                    priceList.remove(priceList.get(i));
                }
            }
            if (tempList != null && tempList.size() > 8) {
                //所有收费项目数量打8折
                for (ItemPriceVo vo : tempList) {
                    vo.setPriceOrg(MathUtils.mul(vo.getPriceCs(), 0.8D));
                    vo.setPrice(MathUtils.mul(vo.getPriceCs(), 0.8D));
                    vo.setPriceCs(MathUtils.mul(vo.getPriceCs(), 0.8D));
                }
                //临时集合里的数据重新存到priceList下
                priceList.addAll(tempList);
            } else {
                //临时集合里的数据重新存到priceList下
                priceList.addAll(tempList);
            }
        }
        return priceList;
    }

    /**
     * @return void
     * @Description 根据医嘱项目合并代码合并项目
     * 添加对采集方法的处理
     * @auther wuqiang
     * @Date 2021-05-31
     * @Param [itemList, sampList, combList]
     */
    public void hbOrdInfoBycodeCombAndFlagAddcol(List<BlPubParamVo> itemList, List<BlPubParamVo> sampList, List<Map<String, Object>> combList) {
        boolean existAddFlag = false;//存在至少一个采集方法附加费加收的项目
    	if (combList != null && combList.size() > 0) {
            if (combList != null && combList.size() > 0) {
                //itemList删除所有试管附加费用
                for (BlPubParamVo sampVo : sampList) {
                    for (int i = itemList.size() - 1; i >= 0; i--) {
                        if (itemList.get(i).getSampTube() != null
                                && !CommonUtils.isEmptyString(itemList.get(i).getPkItem())
                                && itemList.get(i).getSampTube()
                                && itemList.get(i).getPkCnord().equals(sampVo.getPkCnord())
                                && sampVo.getPkItem().equals(itemList.get(i).getPkItem())) {
                            itemList.remove(i);
                            break;
                        }
                    }
                }
                //codeComb信息赋值到sampList集合
                for (Map<String, Object> combMap : combList) {
                    for (BlPubParamVo sampVo : sampList) {
                        if (CommonUtils.getPropValueStr(combMap, "pkCnord").equals(sampVo.getPkCnord())) {
                            sampVo.setCodeComb(CommonUtils.getPropValueStr(combMap, "codeComb"));
                            sampVo.setFlagAddcol(MapUtils.getInteger(combMap, "flagAddcol"));
                        }
                        if(!canDelItem(sampVo)) {
                    		existAddFlag = true;
                    	}
                    }
                }
                //标本费用合并，不需要校验合并代码
                //容器费用合并，需要校验合并代码
                //合并符合条件的项目
                for (int i = sampList.size() - 1; i >= 0; i--) {
                    for (int j = sampList.size() - 1; j >= 0; j--) {
                        if (!sampList.get(i).getPkCnord().equals(sampList.get(j).getPkCnord()) //非相同医嘱
                                && !sampList.get(i).getPkOrdChk().equals(sampList.get(j).getPkOrdChk())
                                && sampList.get(i).getPkItem().equals(sampList.get(j).getPkItem())    //收费项目一样
                                && sampList.get(i).getOrdCntChk() != null && sampList.get(j).getOrdCntChk() != null) {
                            //此if判断是否为标本收费
                            if ("标本收费".equals(sampList.get(i).getSpec())
                                    && "标本收费".equals(sampList.get(j).getSpec())) {
                                //该if判断此次收费的医嘱项目数量，删除医嘱数量少的容器费
                                if (sampList.get(i).getOrdCntChk().compareTo(sampList.get(j).getOrdCntChk()) >= 0) {
                                    //该if判断收费项目数量，删除收费项目数量少的容器费
                                    if (sampList.get(i).getQuanCg().compareTo(sampList.get(j).getQuanCg()) >= 0) {
                                        sampList.remove(j);
                                    } else {
                                        sampList.remove(i);
                                    }
                                } else {
                                    //该if判断收费项目数量，删除收费项目数量少的容器费
                                    if (sampList.get(i).getQuanCg().compareTo(sampList.get(j).getQuanCg()) > 0) {
                                        sampList.remove(j);
                                    } else {
                                        sampList.remove(i);
                                    }
                                }
                                break;
                            }
                            if ("容器收费".equals(sampList.get(i).getSpec())
                                    && "容器收费".equals(sampList.get(j).getSpec())) {
                                //容器收费，合并代码一样参与合并
                                if (!CommonUtils.isEmptyString(sampList.get(i).getCodeComb())
                                        && !CommonUtils.isEmptyString(sampList.get(j).getCodeComb())
                                        && sampList.get(i).getCodeComb().equals(sampList.get(j).getCodeComb())) {
                                    //该if判断此次收费的医嘱项目数量，删除医嘱数量少的容器费
                                    if (sampList.get(i).getOrdCntChk().compareTo(sampList.get(j).getOrdCntChk()) >= 0) {
                                        //该if判断收费项目数量，删除收费项目数量少的容器费
                                        if (sampList.get(i).getQuanCg().compareTo(sampList.get(j).getQuanCg()) >= 0) {
                                            sampList.remove(j);
                                        } else {
                                            sampList.remove(i);
                                        }
                                    } else {
                                        //该if判断收费项目数量，删除收费项目数量少的容器费
                                        if (sampList.get(i).getQuanCg().compareTo(sampList.get(j).getQuanCg()) > 0) {
                                            sampList.remove(j);
                                        } else {
                                            sampList.remove(i);
                                        }
                                    }
                                    break;
                                }
                            }
                            if ("采集方法收费".equals(sampList.get(i).getSpec())
                                    && "采集方法收费".equals(sampList.get(j).getSpec())) {
                                sampList.get(i).setEuAdditem("3");
                                boolean isMerge = sampList.get(i).getFlagAddcol() != null &&
                                        sampList.get(j).getFlagAddcol() != null &&
                                        sampList.get(i).getFlagAddcol() == 1
                                        && sampList.get(j).getFlagAddcol() == 1;
                                //加收
                                if (isMerge) {
                                    break;
                                } else {
                                    //该if判断此次收费的医嘱项目数量，删除医嘱数量少的容器费
                                    if (sampList.get(i).getOrdCntChk().compareTo(sampList.get(j).getOrdCntChk()) >= 0) {
                                        //该if判断收费项目数量，删除收费项目数量少的容器费
                                        if (sampList.get(i).getQuanCg().compareTo(sampList.get(j).getQuanCg()) >= 0) {
                                        	if(canDelItem(sampList.get(j))) {
                                        		 sampList.remove(j);
                                        	}
                                        } else {
                                        	if(canDelItem(sampList.get(i))) {
                                        		sampList.remove(i);
                                        	}
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                //合并符合条件的项目
                for (int i = sampList.size() - 1; i >= 0; i--) {
                    for (int j = sampList.size() - 1; j >= 0; j--) {
                        if (!CommonUtils.isEmptyString(sampList.get(i).getCodeComb())
                                && !CommonUtils.isEmptyString(sampList.get(j).getCodeComb())
                                && !sampList.get(i).getPkCnord().equals(sampList.get(j).getPkCnord()) //非相同医嘱
                                && !sampList.get(i).getPkOrdChk().equals(sampList.get(j).getPkOrdChk())
                                && sampList.get(i).getPkItem().equals(sampList.get(j).getPkItem())    //收费项目一样
                                && sampList.get(i).getCodeComb().equals(sampList.get(j).getCodeComb())
                                && sampList.get(i).getOrdCntChk() != null && sampList.get(j).getOrdCntChk() != null) {//合并代码一样
                            //保留数量最大的项目
                            if (sampList.get(i).getOrdCntChk().compareTo(sampList.get(j).getOrdCntChk()) > 0) {
                            	if(canDelItem(sampList.get(j))) {
                            		 sampList.remove(j);
                            	}
                            } else {
                            	if(canDelItem(sampList.get(i))) {
                                    sampList.remove(i);
                            	}
                            }
                            break;
                        }
                    }
                }
                
                //处理采集方法附加费的加收与不加收情况
                if(null != sampList && sampList.size() > 0 ) {
                	int count = 0;
                	for(int v=sampList.size()-1; v>=0; v--) {
                		String dtColltype = sampList.get(v).getSpec();
                     	Integer FlagAddcol = sampList.get(v).getFlagAddcol();
                     	if(StringUtils.isNotBlank(dtColltype) && dtColltype.equals("采集方法收费") 
                           	   && (null == FlagAddcol ||  FlagAddcol != 1) ) {
                     		count++;
                     		//同时开立“非加收”和“加收”采集方法附加费的检验项目，采集方法附加费为：“加收”医嘱项目数量 个附加费
                         	if(existAddFlag) {
                         		sampList.remove(v);
                         	} else if (count > 1) {//否则不包含“加收”采集方法附加费的项目同一次结算只生成一次采集方法附加费
                     			sampList.remove(v);
                     		}
                     	}
                    }
                }
                //重新组织入参
                itemList.addAll(sampList);
            }
        }
    }
    
    /**  
     *<p>Desc:判断是否可以移除相同的采集方法收费项目</p>  
     * bug35392 加收采集附加费 少收问题  多个医嘱项目的附加费都勾选收取多次采集方法费用  不删除  返回 false
     * @author : wangpengyong  
     * @date   : 2021年7月12日  
     */  
     public boolean canDelItem(BlPubParamVo vo) {
     	boolean flag = true;
     	String dtColltype = vo.getSpec();
     	Integer FlagAddcol = vo.getFlagAddcol();
     	if(StringUtils.isNotBlank(dtColltype) && dtColltype.equals("采集方法收费") 
           	   && null != FlagAddcol && FlagAddcol == 1) {
     		flag = false;
     	}
     	return flag;
     }

    
}
