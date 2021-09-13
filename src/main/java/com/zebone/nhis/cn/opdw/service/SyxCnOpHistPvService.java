package com.zebone.nhis.cn.opdw.service;

import com.zebone.nhis.cn.opdw.dao.PvDocMapper;
import com.zebone.nhis.cn.opdw.dao.SyxCnOpHistPvMapper;
import com.zebone.nhis.cn.opdw.vo.SyxPvDoc;
import com.zebone.nhis.common.module.cn.opdw.PvDoc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.nhis.emr.common.EmrSaveUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.platform.Application;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SyxCnOpHistPvService {

    @Autowired
    public SyxCnOpHistPvMapper histDao;

    @Resource
    private PvDocMapper recMapper;

    public List<Map<String, Object>> qryHistoryOrders(String param, IUser user) {
        //pkPv
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPv = paramMap.get("pkPv") != null ? paramMap.get("pkPv").toString() : "";
        if (StringUtils.isBlank(pkPv)) throw new BusException("传参pkPv为空！");
        boolean flagEx = "1".equals(MapUtils.getString(paramMap, "flagEx"));
        List<Map<String, Object>> histOrderlist = new ArrayList<Map<String, Object>>();
        if (flagEx) {
            histOrderlist = histDao.qryHistoryOrdersEx(paramMap);
        } else {
            histOrderlist = histDao.qryHistoryOrders(paramMap);
        }
        return histOrderlist;
    }

    public List<SyxPvDoc> qryHistoryDocs(String param, IUser user) throws UnsupportedEncodingException {
        //pkPv
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        String pkPi = paramMap.get("pkPi") != null ? paramMap.get("pkPi").toString() : "";
        if (StringUtils.isBlank(pkPi)) throw new BusException("传参pkPi为空！");
        String dateDocBegin = paramMap.get("dateDocBegin") != null ? paramMap.get("dateDocBegin").toString() : "";
        if (StringUtils.isBlank(dateDocBegin)) throw new BusException("传参dateDocBegin为空！");
        paramMap.put("dateDocBegin", CommonUtils.getString(paramMap.get("dateDocBegin")).substring(0, 8) + "000000");
        String dateDocEnd = paramMap.get("dateDocEnd") != null ? paramMap.get("dateDocEnd").toString() : "";
        if (StringUtils.isBlank(dateDocEnd)) throw new BusException("传参dateDocEnd为空！");
        paramMap.put("dateDocEnd", CommonUtils.getString(paramMap.get("dateDocEnd")).substring(0, 8) + "235959");
        List<SyxPvDoc> histDocslist = new ArrayList<SyxPvDoc>();
        String noneDocData = paramMap.get("noneDocData") != null ? paramMap.get("noneDocData").toString() : "";
        if (noneDocData.equals("1")) {
            histDocslist = histDao.qryHistoryDocsNoneDocData(paramMap);
        } else {
            histDocslist = histDao.qryHistoryDocs(paramMap);
        }
        String saveDataMode = EmrSaveUtils.getSaveDataMode();
        if (saveDataMode.equals("1")) {
            //独立存储
            String dbName = EmrSaveUtils.getDbName();
            List<String> pkPvdocs = new ArrayList<>();
            if (histDocslist != null && histDocslist.size() > 0) {
                int i, j;
                for (i = 0; i < histDocslist.size(); i++) {
                    SyxPvDoc doc = histDocslist.get(i);
                    if (doc.getDataDoc() != null) continue;

                    pkPvdocs.add(doc.getPkPvdoc());
                }
                if (pkPvdocs != null && pkPvdocs.size() > 0) {
                    List<PvDoc> docs = recMapper.queryDocListEmrByPks(dbName, pkPvdocs);
                    if (docs != null && docs.size() > 0) {
                        for (i = 0; i < docs.size(); i++) {
                            PvDoc pvDoc = docs.get(i);
                            if (pvDoc != null && pvDoc.getDataDoc() != null) {
                                for (j = 0; j < histDocslist.size(); j++) {
                                    SyxPvDoc docVo = histDocslist.get(j);
                                    if (docVo.getPkPvdoc() == null) continue;
                                    if (pvDoc.getPkPvdoc() != null && pvDoc.getPkPvdoc().equals(docVo.getPkPvdoc()) && pvDoc.getDataDoc() != null) {
                                        docVo.setDataDoc(pvDoc.getDataDoc());
                                        break;
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

        String hosCode = ApplicationUtils.getPropertyValue("emr.hos.code", "");
        if (hosCode.equals("zsba")) {
            for (SyxPvDoc syxPvDoc : histDocslist) {
                if (syxPvDoc.getTempName() != null && syxPvDoc.getTempName().contains("_")) {
                    String[] name = syxPvDoc.getTempName().split("_");
                    syxPvDoc.setTempName(name[2] + "_" + name[1] + "_" + name[0]);
                }
            }
            String intfSwtch = ApplicationUtils.getPropertyValue("emr.shield.tpi.intf", "");
            if (StringUtils.isEmpty(intfSwtch)) intfSwtch = "0";
            if (!intfSwtch.equals("1")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日HH时mm分");
                List<SyxPvDoc> oldList = histDao.qryHistoryDocsOldBoai(paramMap);
                if (oldList != null && oldList.size() > 0) {
                    for (SyxPvDoc syxPvDoc : oldList) {
                        syxPvDoc.setTempName(sdf.format(syxPvDoc.getCreateTime()) + "_" + syxPvDoc.getModifier() + "_" + syxPvDoc.getTempName());
                    }
                    histDocslist.addAll(oldList);
                }
            }

        }
        return histDocslist;
    }

    public List<Map<String, Object>> qryCommonDiag(String param, IUser user) {
        Map<String, Object> params = JsonUtil.readValue(param, Map.class);
        if (Application.isSqlServer()) {
            params.put("dbType", "sqlserver");
        } else {
            params.put("dbType", "oralce");
        }
        String pkDept = MapUtils.getString(params, "pkDept");
        Map<String, String> stringMap = JsonUtil.readValue(String.valueOf(RedisUtils.getCacheHashObj("cnop:commondiag:" , pkDept)),Map.class);

        if (MapUtils.isEmpty(stringMap)) {
            return getMaps(params, pkDept);
        }
        Date rediesDate = DateUtils.strToDate(MapUtils.getString(stringMap, "date"), "yyyy-MM-dd HH:mm:ss");
        if (DateUtils.isSameDay(rediesDate,new Date())){
            return JsonUtil.readValue(MapUtils.getString(stringMap, "diags"), new TypeReference<List<Map<String, Object>>>() {
            });
        }
        return getMaps(params, pkDept);
    }

    private List<Map<String, Object>> getMaps(Map<String, Object> params, String pkDept) {
        Map<String, String> stringMap;
        List<Map<String, Object>> diag = histDao.qryCommonDiag(params);
        stringMap=new HashMap<>(2);
        stringMap.put("date",DateUtils.getDateTime());
        stringMap.put("diags",JsonUtil.writeValueAsString(diag));
        RedisUtils.setCacheHashObj("cnop:commondiag:" , pkDept, stringMap);
        return diag;
    }
}
