package com.zebone.nhis.pro.zsba.scm.service;

import com.zebone.nhis.base.pub.vo.CgdivItemVo;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyDiseaseOrd;
import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItem;
import com.zebone.nhis.common.module.base.bd.srv.BdItemHp;
import com.zebone.nhis.common.module.scm.pub.*;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.scm.pub.vo.PdAndAllParam;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Classname BaScmPubService
 * @Description 博爱药品个性化
 * @Date 2020-07-10 9:17
 * @Created by wuqiang
 */

@Service
public class BaScmPubService {

    /**
     * 添加状态
     **/
    public static final String AddState = "_ADD";

    /**
     * 更新状态
     */
    public static final String UpdateState = "_UPDATE";

    /**
     * 删除状态
     */
    public static final String DelState = "_DELETE";
    /**
     * 保存药品信息
     * 1、药品信息保存；2、包装信息保存；3、药品别名保存
     * 4、属性保存；5、分配保存
     *
     * @param param
     * @param user
     */
    public BdPd saveBdPdAndAll(String param, IUser user) {
        PdAndAllParam pdAndAll = JsonUtil.readValue(param, PdAndAllParam.class);
        BdPd pd = pdAndAll.getPd();
        List<BdPdConvert> pdConvertList = pdAndAll.getPdConvertList();
        List<BdPdAs> pdAsList = pdAndAll.getPdAsList();
        List<BdPdAtt> pdAttList = pdAndAll.getPdAttList();
        List<BdPdStore> pdStoreList = pdAndAll.getPdStoreList();
        List<CgdivItemVo> pdCgdivList = pdAndAll.getItemCgDivs(); //记费策略
        List<BdItemHp> pdItemHpList = pdAndAll.getPdItemHpList();//医保类型
        List<InsGzgyDiseaseOrd> insGzgyDiseaseOrdList = pdAndAll.getInsGzgyDiseaseOrdList();//门慢和门特
        boolean ifNewPd = true;//是否新增药品标志
        String pk = null;//用于存储新增后的主键，调平台时传入
        /**
         * 药品信息的保存
         */
        if (pd.getPkPd() == null) {
            String codeSql = "select count(1) from bd_pd where del_flag='0' and code = ?";
            String barCodeSql = "select count(1) from bd_pd where del_flag='0' and barcode = ?";
            String nameSpec = "select count(1) from bd_pd where del_flag='0' and name = ? and spec = ? and pk_factory = ? and Price = ?";
            //String codeHpSql="select count(1) from bd_pd where del_flag='0' and  code_hp=?";
            if (Application.isSqlServer()) {
                codeSql = codeSql.replace("count(1)", "top 1 count(1)");
                barCodeSql = barCodeSql.replace("count(1)", "top 1 count(1)");
                nameSpec = nameSpec.replace("count(1)", "top 1 count(1)");
                //codeHpSql=codeHpSql.replace("count(1)", "top 1 count(1)");
            } else {
                codeSql += " and rownum=1";
                barCodeSql += " and rownum=1";
                //codeHpSql+=" and rownum=1";
                nameSpec += " and rownum=1";
            }
            int code_count = DataBaseHelper.queryForScalar(codeSql, Integer.class, pd.getCode());
            if (code_count != 0) {
                throw new BusException("药品编码全局不唯一！");
            }
            int barCode_count = DataBaseHelper.queryForScalar(barCodeSql, Integer.class, pd.getBarcode());
            if (barCode_count != 0) {
                throw new BusException("药品条形码全局不唯一！");
            }
            int nameSpec_count = DataBaseHelper.queryForScalar(nameSpec, Integer.class, pd.getName(), pd.getSpec(), pd.getPkFactory(), pd.getPrice());
            if (nameSpec_count != 0) {
                throw new BusException("药品名称+规格+生产厂家全局不唯一！");
            }
//			int codeHp_count=DataBaseHelper.queryForScalar( codeHpSql, Integer.class, pd.getCodeHp());
//			if(codeHp_count!=0){
//				throw new BusException("医保上传编码全局不唯一！");
//			}
            if (null != pd.getPkPdgn()) {
                String dtCodeSql = "SELECT CODE from BD_PD_GN t where t.pk_pdgn=?";
                String dtCode = DataBaseHelper.queryForScalar(dtCodeSql, String.class, pd.getPkPdgn());
                String maxCodeSql = "select max(substring(pd.CODE,len(CODE) -2,len(CODE)) ) from BD_PD pd where  substring(pd.CODE,1,len(CODE) -3) =  ?";
                String dosageCode = pd.getDtDosage();
                String maxCode = DataBaseHelper.queryForScalar(maxCodeSql, String.class, dtCode + "-" + dosageCode + "-");
                String pdgnSql = "SELECT code from BD_PD_GN where PK_PDGN = ?";
                String pdgnCode = DataBaseHelper.queryForScalar(pdgnSql, String.class, pd.getPkPdgn());
                if (null != maxCode && maxCode.length() >= 3) {
                    Integer streamVal = Integer.valueOf(maxCode.substring(maxCode.length() - 3, maxCode.length())) + 1;
                    String streamCode = String.valueOf(streamVal);
                    if (streamVal.intValue() < 100) {
                        streamCode = String.format("%03d", streamVal);
                    }
                    //药品编码=通用编码+剂型编码+流水号
                    pd.setCode(pdgnCode + "-" + dosageCode + "-" + streamCode);
                } else {
                    pd.setCode(pdgnCode + "-" + dosageCode + "-" + "001");
                }
            }
            if (code_count == 0 && nameSpec_count == 0 && barCode_count == 0) {
                DataBaseHelper.insertBean(pd);
                pk = pd.getPkPd();
            }
        } else {
            ifNewPd = false;//修改药品信息
            String codeSql = "select count(1) from bd_pd where del_flag='0' and code = ? and pk_pd != ?";
            String barCodeSql = "select count(1) from bd_pd where del_flag='0' and barcode = ? and pk_pd != ?";
            String nameSpec = "select count(1) from bd_pd where del_flag='0' and name = ? and spec = ? and pk_pd != ? and pk_factory = ?";
            //String codeHpSql="select count(1) from bd_pd where del_flag='0' and  code_hp=? and pk_pd!=?";
            if (Application.isSqlServer()) {
                codeSql = codeSql.replace("count(1)", "top 1 count(1)");
                barCodeSql = barCodeSql.replace("count(1)", "top 1 count(1)");
                nameSpec = nameSpec.replace("count(1)", "top 1 count(1)");
                //codeHpSql=codeHpSql.replace("count(1)", "top 1 count(1)");
            } else {
                codeSql += " and rownum=1";
                barCodeSql += " and rownum=1";
                //codeHpSql+=" and rownum=1";
                nameSpec += " and rownum=1";
            }
            int code_count = DataBaseHelper.queryForScalar(codeSql, Integer.class, pd.getCode(), pd.getPkPd());
            if (code_count != 0) {
                throw new BusException("药品编码全局不唯一！");
            }
            int barCode_count = DataBaseHelper.queryForScalar(barCodeSql, Integer.class, pd.getBarcode(), pd.getPkPd());
            if (barCode_count != 0) {
                throw new BusException("药品条形码全局不唯一！");
            }
            int nameSpec_count = DataBaseHelper.queryForScalar(nameSpec, Integer.class, pd.getName(), pd.getSpec(), pd.getPkPd(), pd.getPkFactory());
            if (nameSpec_count != 0) {
                throw new BusException("药品名称+规格+生产厂家全局不唯一！");
            }
//			int codeHp_count=DataBaseHelper.queryForScalar(codeHpSql, Integer.class, pd.getCodeHp(), pd.getPkPd());
//			if(codeHp_count!=0){
//				throw new BusException("医保上传编码全局不唯一！");
//			}
            if (code_count == 0 && nameSpec_count == 0 && barCode_count == 0) {
                DataBaseHelper.updateBeanByPk(pd, false);
            }
        }
        String pkPd = pd.getPkPd();
        /**
         * 判断保存还是修改记费策略
         */
        if (ifNewPd) {
            if (pdCgdivList != null && pdCgdivList.size() != 0) {
                String pkitem = pd.getPkPd();
                for (CgdivItemVo cgdivItemVo : pdCgdivList) {
                    BdHpCgdivItem cgdiv = new BdHpCgdivItem();
                    ApplicationUtils.copyProperties(cgdiv, cgdivItemVo);
                    cgdiv.setPkItem(pkitem);
                    cgdiv.setFlagPd("1");
                    if (cgdiv.getPkHpcgdivitem() != null) {
                        DataBaseHelper.updateBeanByPk(cgdiv, false);
                    } else {
                        DataBaseHelper.insertBean(cgdiv);
                    }
                }
            }
        } else {
            String pkitem = pd.getPkPd();
            for (CgdivItemVo cgdivItemVo : pdCgdivList) {
                BdHpCgdivItem cgdiv = new BdHpCgdivItem();
                ApplicationUtils.copyProperties(cgdiv, cgdivItemVo);
                cgdiv.setPkItem(pkitem);
                cgdiv.setFlagPd("1");
                if (cgdiv.getPkHpcgdivitem() != null) {
                    DataBaseHelper.updateBeanByPk(cgdiv, false);
                } else {
                    DataBaseHelper.insertBean(cgdiv);
                }
            }
        }
        /**
         * 包装信息的保存
         */
        if (ifNewPd) {    //新增药品信息的状态下
            if (pdConvertList != null && pdConvertList.size() != 0) {
                if (checkOpIpSpecNum(pdConvertList)) {
                    //校验：pk_unit是否互相重复
                    Map<String, String> unitMap = new HashMap<String, String>();
                    int len = pdConvertList.size();
                    for (int i = 0; i < len; i++) {
                        String pkUnit = pdConvertList.get(i).getPkUnit();
                        if (unitMap.containsKey(pkUnit)) {
                            throw new BusException("包装单位重复！");
                        }
                        unitMap.put(pkUnit, pdConvertList.get(i).getPkPdconvert());
                    }
                    for (BdPdConvert convert : pdConvertList) {
                        convert.setPkPd(pkPd);
                        DataBaseHelper.insertBean(convert);
                    }
                } else {
                    throw new BusException("包装页签下请至少包含一条门诊默认=true和住院默认=true的记录！");
                }
            } else {
                throw new BusException("请插入至少一条包装规格记录！");
            }
        } else {          //修改药品信息的状态下(如果前台未点击包装Tab标签查询包装信息并对包装信息做修改，前台所传pdConvertList==null)
            if (pdConvertList != null) {
                if (pdConvertList.size() != 0) {
                    if (checkOpIpSpecNum(pdConvertList)) {
                        //校验：pk_unit是否互相重复
                        Map<String, String> unitMap = new HashMap<String, String>();
                        int len = pdConvertList.size();
                        for (int i = 0; i < len; i++) {
                            String pkUnit = pdConvertList.get(i).getPkUnit();
                            if (unitMap.containsKey(pkUnit)) {
                                throw new BusException("包装单位重复！");
                            }
                            unitMap.put(pkUnit, pdConvertList.get(i).getPkPdconvert());
                        }
                        //软删除，先删除后恢复
                        DataBaseHelper.update("update bd_pd_convert set del_flag = '1' where pk_pd = ?", new Object[]{pkPd});
                        for (BdPdConvert convert : pdConvertList) {
                            if (convert.getPkPdconvert() == null) {
                                convert.setPkPd(pkPd);
                                DataBaseHelper.insertBean(convert);
                            } else {
                                convert.setPkPd(pkPd);
                                DataBaseHelper.updateBeanByPk(convert, false);
                            }
                        }
                    } else {
                        throw new BusException("包装页签下请至少包含一条门诊默认=true和住院默认=true的记录！");
                    }
                } else {
                    throw new BusException("包装页签下请至少包含一条包装记录！");
                }
            }
        }
        /**
         * 药品别名保存
         */
        if (ifNewPd) {
            if (pdAsList != null && pdAsList.size() != 0) {
                for (BdPdAs alia : pdAsList) {
                    alia.setPkPd(pkPd);
                    DataBaseHelper.insertBean(alia);
                }
            } else {
                throw new BusException("请插入至少一条别名记录！");
            }
        } else {
            if (pdAsList != null) {
                if (pdAsList.size() != 0) {
                    //软删除，先删除后恢复
                    DataBaseHelper.update("update bd_pd_as set del_flag = '1' where pk_pd = ?", new Object[]{pkPd});
                    for (BdPdAs alia : pdAsList) {
                        if (alia.getPkPdas() == null) {
                            alia.setPkPd(pkPd);
                            DataBaseHelper.insertBean(alia);
                        } else {
                            alia.setPkPd(pkPd);
                            DataBaseHelper.updateBeanByPk(alia, false);
                        }
                    }
                } else {
                    throw new BusException("别名页签下请至少包含一条别名记录！");
                }
            }
        }
        /** 4、属性保存 */
        savePdAttList(pdAttList, pkPd);
        /**
         * 分配保存
         */
        if (ifNewPd) {
            if (pdStoreList != null && pdStoreList.size() != 0) {
                for (BdPdStore pdstore : pdStoreList) {
                    pdstore.setPkPd(pkPd);
                    Map<String, Object> convMap = choosePkPdconvertByStore(pdstore.getPkStore(), pkPd);
                    pdstore.setPkPdconvert(convMap.get("pkPdconvert").toString());
                    pdstore.setFlagStop("0");//默认启用
                    pdstore.setPackSize(Long.parseLong(convMap.get("packSize").toString()));
                    pdstore.setPkUnit(convMap.get("pkUnit").toString());
                    DataBaseHelper.insertBean(pdstore);
                    //保存处方包装记录
                    BdPdStorePack pdStorePack = new BdPdStorePack();
                    pdStorePack.setPkPdstorepack(NHISUUID.getKeyId());
                    pdStorePack.setPkPdstore(pdstore.getPkPdstore());
                    pdStorePack.setPkPdconvert(convMap.get("pkPdconvert").toString());
                    pdStorePack.setFlagDef("1");
                    pdStorePack.setDelFlag("0");
                    User userBean = (User) user;
                    pdStorePack.setPkOrg(userBean.getPkOrg());
                    pdStorePack.setCreator(userBean.getPkEmp());
                    pdStorePack.setCreateTime(new Date());
                    pdStorePack.setTs(new Date());
                    pdStorePack.setPackSize(Long.parseLong(convMap.get("packSize").toString()));
                    pdStorePack.setPkUnit(convMap.get("pkUnit").toString());
                    DataBaseHelper.insertBean(pdStorePack);
                }
            }
        } else {
            if (pdStoreList != null) {
                Set<String> pkStores = new HashSet<String>();
                if (pdStoreList.size() != 0) {
                    for (BdPdStore pdstore : pdStoreList) {
                        pdstore.setPkPd(pkPd);
                        Map<String, Object> convMap = choosePkPdconvertByStore(pdstore.getPkStore(), pkPd);
                        pdstore.setPkPdconvert(convMap.get("pkPdconvert").toString());
                        pdstore.setFlagStop("0");//默认启用
                        pdstore.setPackSize(Long.parseLong(convMap.get("packSize").toString()));
                        String pkUnit = convMap.get("pkUnit").toString();
                        pdstore.setPkUnit(pkUnit);
                        BdPdStorePack pdStorePack = new BdPdStorePack();
                        pdStorePack.setPkPdstorepack(NHISUUID.getKeyId());
                        pdStorePack.setPkPdstore(pdstore.getPkPdstore());
                        pdStorePack.setPkPdconvert(convMap.get("pkPdconvert").toString());
                        pdStorePack.setFlagDef("1");
                        pdStorePack.setDelFlag("0");
                        User userBean = (User) user;
                        pdStorePack.setPkOrg(userBean.getPkOrg());
                        pdStorePack.setCreator(userBean.getPkEmp());
                        pdStorePack.setCreateTime(new Date());
                        pdStorePack.setTs(new Date());
                        pdStorePack.setPackSize(Long.parseLong(convMap.get("packSize").toString()));
                        pdStorePack.setPkUnit(pkUnit);
                        String chkPdStoreSql = "select count(1) from bd_pd_store where pk_store=? and pk_pd=?";
                        Integer count = DataBaseHelper.queryForScalar(chkPdStoreSql, Integer.class, new Object[]{pdstore.getPkStore(), pkPd});
                        if (!CommonUtils.isEmptyString(pdstore.getPkPdstore())) {//修改分配页签已经分配的仓库信息时；
                            //单位相同，将数据更新，不同优先保留仓库维护好的单位数据
                            DataBaseHelper.updateBeanByWhere(pdstore, " pk_unit='" + pkUnit + "' and pk_pdstore='" + pdstore.getPkPdstore() + "'", false);
                            DataBaseHelper.updateBeanByWhere(pdStorePack, " pk_unit='" + pkUnit + "' and pk_pdstore='" + pdstore.getPkPdstore() + "'", false);
                        } else {
                            if (count == null || count == 0) {
                                DataBaseHelper.insertBean(pdstore);
                                pdStorePack.setPkPdstore(pdstore.getPkPdstore());
                                DataBaseHelper.insertBean(pdStorePack);
                            }
                        }
                        pkStores.add(pdstore.getPkStore());
                        String chkPack = "update bd_pd_store set pack_size=(select pack_size from bd_pd_convert where bd_pd_convert.pk_pdconvert=bd_pd_store.pk_pdconvert) where pk_pdstore=?";
                        DataBaseHelper.execute(chkPack, new Object[]{pdstore.getPkPdstore()});
                    }
                } else {
                    DataBaseHelper.execute("delete from bd_pd_store where pk_pd = ?", new Object[]{pkPd});
                }
                String delpdStore = "delete from bd_pd_store where pk_pd='" + pkPd + "' and pk_store not in (" + CommonUtils.convertSetToSqlInPart(pkStores, "pk_store") + ") ";
                DataBaseHelper.execute(delpdStore, new Object[]{});
            }
        }
        /** 新增或跟新医保类型 **///2018-10-09孙逸仙纪念医院需求
        if (pdItemHpList != null && pdItemHpList.size() != 0) {
            DataBaseHelper.update("update bd_item_hp set del_flag = '1' where pk_item = ?", new Object[]{pkPd});
            for (BdItemHp hp : pdItemHpList) {
                if (hp.getPkItemhp() != null) {
                    hp.setDelFlag("0");// 恢复
                    hp.setPkItem(pkPd);
                    hp.setEuItemType("1");
                    DataBaseHelper.updateBeanByPk(hp, false);
                } else {
                    hp.setPkItem(pkPd);
                    hp.setEuItemType("1");
                    DataBaseHelper.insertBean(hp);
                }
            }
        } else {
            DataBaseHelper.update("update bd_item_hp set del_flag = '1' where pk_item = ?", new Object[]{pkPd});
        }
        //门慢和门特
        DataBaseHelper.update("update ins_gzgy_disease_ord set del_flag = '1' where pk_ord = ?",
                new Object[]{pkPd});
        if (insGzgyDiseaseOrdList != null && insGzgyDiseaseOrdList.size() > 0) {
            for (InsGzgyDiseaseOrd ord : insGzgyDiseaseOrdList) {
                ord.setPkOrd(pkPd);
                ord.setCodeOrd(pd.getCode());
                ord.setNameOrd(pd.getName());
                if (StringUtils.isBlank(ord.getPkDiseaseord())) {
                    DataBaseHelper.insertBean(ord);
                } else {
                    ord.setDelFlag("0");
                    DataBaseHelper.updateBeanByPk(ord, false);
                }
            }
        }
        //发送消息至平台
        Map<String, Object> msgParam = new HashMap<String, Object>();
        msgParam.put("pd", JsonUtil.readValue(param, BdPd.class));
        msgParam.put("pkPd", pd.getPkPd());
        msgParam.put("STATUS", ifNewPd ? this.AddState : this.UpdateState);
        PlatFormSendUtils.sendBdPdMsg(msgParam);
        return pd;
    }


    /**
     * 检测“包装”页签下有且仅包含一条门诊默认=true和一条住院默认=true的记录
     *
     * @param pdConvertList
     * @return
     */
    private boolean checkOpIpSpecNum(List<BdPdConvert> pdConvertList) {
        int mz = 0;
        int zy = 0;
        for (BdPdConvert convert : pdConvertList) {
            if ("1".equals(convert.getFlagOp())) {
                mz++;
            }
            if ("1".equals(convert.getFlagIp())) {
                zy++;
            }
        }
        if (mz == 1 && zy == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 4、保存 药品属性
     *
     * @param pdAttList
     * @param pkPd
     */
    private void savePdAttList(List<BdPdAtt> pdAttList, String pkPd) {
        if (pdAttList != null && pdAttList.size() != 0) {
            for (BdPdAtt att : pdAttList) {
                if (att.getPkPdatt() != null) {
                    att.setPkPd(pkPd);
                    DataBaseHelper.updateBeanByPk(att, false);
                } else {
                    att.setPkPd(pkPd);
                    DataBaseHelper.insertBean(att);
                }
            }
        }
    }

    /**
     * 5、保存 药品 分配信息 ： 新增(插入全部),更新(删除全部，若传入不为空则插入)
     *
     * @param pdStoreList
     * @param ifNewPd
     * @param pkPd
     */
    private void savePdStoreList(List<BdPdStore> pdStoreList, boolean ifNewPd, String pkPd) {
        if (ifNewPd) {
            if (pdStoreList != null && pdStoreList.size() != 0) {
                for (BdPdStore pdstore : pdStoreList) {
                    pdstore.setPkPd(pkPd);
                    Map<String, Object> convMap = choosePkPdconvertByStore(pdstore.getPkStore(), pkPd);
                    pdstore.setPkPdconvert(convMap.get("pkPdconvert").toString());
                    pdstore.setFlagStop("0");// 默认启用
                    DataBaseHelper.insertBean(pdstore);
                }
            }
        } else {
            if (pdStoreList != null) {
                DataBaseHelper.execute("delete from bd_pd_store where pk_pd = ?", new Object[]{pkPd});
                if (pdStoreList.size() != 0) {
                    for (BdPdStore pdstore : pdStoreList) {
                        pdstore.setPkPd(pkPd);
                        Map<String, Object> convMap = choosePkPdconvertByStore(pdstore.getPkStore(), pkPd);
                        pdstore.setPkPdconvert(convMap.get("pkPdconvert").toString());
                        pdstore.setFlagStop("0");// 默认启用
                        DataBaseHelper.insertBean(pdstore);
                    }
                }
            }
        }
    }


    /**
     * 根据仓库默认包装分类 返回 仓库物品分配中的包装主键
     *
     * @param pkStore 仓库主键
     * @param pkPd    当前药品主键
     * @return pkPdconvert 包装主键
     */
    private Map<String, Object> choosePkPdconvertByStore(String pkStore, String pkPd) {
        Map<String, Object> map = DataBaseHelper.queryForMap("select eu_packtype from bd_store where del_flag='0' and pk_store = ?", pkStore);
        String packType = "";
        if (map == null) {
            throw new BusException("仓库不存在！");
        } else {
            packType = map.get("euPacktype").toString();
        }
        Map<String, Object> convertMap = null;
        if ("0".equals(packType)) {//门诊
            convertMap = DataBaseHelper.queryForMap("select * from bd_pd_convert where del_flag='0' and flag_op = '1' and pk_pd = ?", pkPd);
            if (convertMap == null) {
                Map<String, Object> pd = DataBaseHelper.queryForMap("select name from bd_pd where pk_pd = ?", pkPd);
                throw new BusException("药品" + pd.get("name").toString() + "没有指定默认门诊包装！");
            }
        } else if ("1".equals(packType)) {//住院
            convertMap = DataBaseHelper.queryForMap("select * from bd_pd_convert where del_flag='0' and flag_ip = '1' and pk_pd = ?", pkPd);
            if (convertMap == null) {
                Map<String, Object> pd = DataBaseHelper.queryForMap("select name from bd_pd where pk_pd = ?", pkPd);
                throw new BusException("药品" + pd.get("name").toString() + "没有指定默认住院包装！");
            }
        }
        return convertMap;
    }
}
