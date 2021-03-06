package com.zebone.nhis.webservice.pskq.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOccDt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.VitalSigns;
import com.zebone.nhis.webservice.pskq.model.message.*;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("webVitalSignsService")
public class VitalSignsService {

    public String save(String param){
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        try {

            List<DataElement> dataElementList = (List<DataElement>) requestBody.getMessage().get("VITAL_SIGNS");
            VitalSigns vitalSigns = (VitalSigns) MessageFactory.deserialization(dataElementList, new VitalSigns());


            if (vitalSigns == null) {
                throw new Exception("????????????????????????");
            }

            if (StringUtils.isEmpty(vitalSigns.getEnterDateTime())) {
                throw new Exception("enterDateTime is empty");
            }

            if (StringUtils.isEmpty(vitalSigns.getMeasureDateTime())) {
                throw new Exception("????????????????????????????????????");
            }
            if (StringUtils.isEmpty(vitalSigns.getEncounterId()) ||
                    vitalSigns.getEncounterId().split("_").length < 4 ||
                    StringUtils.isEmpty(vitalSigns.getEncounterId().split("_")[3])) {
                throw new Exception("??????id????????????");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateVts = simpleDateFormat.parse(vitalSigns.getMeasureDateTime());
            String sql = "select * from PV_ENCOUNTER where CODE_PV = ?";
            PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql, PvEncounter.class, vitalSigns.getEncounterId().split("_")[3]);

            if (pvEncounter == null) {
                throw new Exception("PvEncounter is null");
            }
            if (StringUtils.isEmpty(vitalSigns.getPkPatient()) ||
                    vitalSigns.getPkPatient().split("_").length < 3 ||
                    StringUtils.isEmpty(vitalSigns.getPkPatient().split("_")[2])) {
                throw new Exception("pkPi is empty");
            }
            sql = "select * from PI_MASTER where CODE_PI = ?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql, PiMaster.class, vitalSigns.getPkPatient().split("_")[2]);
            if (piMaster == null) {
                throw new Exception("PiMaster is null");
            }

            if (StringUtils.isEmpty(vitalSigns.getDeptId())) {
                throw new Exception("deptId is empty");
            }

            sql = "select * from BD_OU_DEPT where CODE_DEPT = ?";
            BdOuDept dept = DataBaseHelper.queryForBean(sql, BdOuDept.class, vitalSigns.getDeptId());
            if (dept == null) {
                throw new Exception("dept is null");
            }

            if (StringUtils.isEmpty(vitalSigns.getMeasureOperaId())) {
                throw new Exception("operaId is empty");
            }
            sql = "select * from BD_OU_EMPLOYEE where CODE_EMP = ?";
            BdOuEmployee employee = DataBaseHelper.queryForBean(sql, BdOuEmployee.class, vitalSigns.getMeasureOperaId());
            if (employee == null) {
                throw new Exception("employee is null");
            }


            sql = "select * from ex_vts_occ where pk_pv = ? and del_flag = '0' order by CREATE_TIME desc";
            List<ExVtsOcc> exVtsOccList = DataBaseHelper.queryForList(sql, ExVtsOcc.class, pvEncounter.getPkPv());
            boolean isExVtsOcc = false;
            for (ExVtsOcc a : exVtsOccList) {
                if (DateUtils.isSameDay(dateVts, a.getDateVts())) {
                    //update
                    System.out.println("update");
                    ExVtsOcc exVtsOcc = new ExVtsOcc();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateVts);
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            0, 0, 0);
                    Date dateVts1 = calendar.getTime();

                    exVtsOcc.setDateVts(dateVts1);
                    exVtsOcc.setPkDeptInput(dept.getPkDept());
                    exVtsOcc.setDateInput(new Date());
                    exVtsOcc.setPkEmpInput(employee.getPkEmp());
                    exVtsOcc.setNameEmpInput(employee.getNameEmp());
                    exVtsOcc.setCreator(employee.getPkEmp());
                    exVtsOcc.setModifier(employee.getPkEmp());
                    exVtsOcc.setDelFlag("0");
                    exVtsOcc.setEuStooltype("0");
                    exVtsOcc.setFlagColo("0");
                    exVtsOcc.setInfantNo("0");
                    exVtsOcc.setModityTime(new Date());
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setEuStooltype(vitalSigns.getMeasureValue());
                        }

                    }
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValStool(Long.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("?????????????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValStoolColo(Long.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("???????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValUrine(vitalSigns.getMeasureValue());
                        }

                    }
                    if ("????????????????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setHourUrine(Integer.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("???????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValGast(Long.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("???????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValGall(Long.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("???????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValDrainage(Long.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValInput(vitalSigns.getMeasureValue());
                        }

                    }
                    if ("?????????????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setHourInput(Integer.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("??????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValWeight(vitalSigns.getMeasureValue());
                        }

                    }
                    if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValSbp(Long.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValDbp(Long.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValSbpAdd(Long.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValDbpAdd(Long.valueOf(vitalSigns.getMeasureValue()));
                        }

                    }
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValAl(vitalSigns.getMeasureValue());
                        }

                    }
                    if ("??????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if (!StringUtils.isEmpty(vitalSigns.getMeasureValue())) {
                            exVtsOcc.setValSt(vitalSigns.getMeasureValue());
                        }

                    }
                    if ("?????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        if ("1".equals(vitalSigns.getMeasureValue())) {

                        }
                    }

                    if ("?????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        exVtsOcc.setValOutputTotal(vitalSigns.getMeasureValue());
                    }

                    if ("???????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        exVtsOcc.setHourOutputTotal(Integer.valueOf(vitalSigns.getMeasureValue()));
                    }

                    if ("??????????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        exVtsOcc.setDtOutputtype(vitalSigns.getMeasureValue());
                    }

                    if ("????????????ml".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        exVtsOcc.setValOutput(vitalSigns.getMeasureValue());
                    }

                    if ("??????????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        exVtsOcc.setHourOutput(Integer.valueOf(vitalSigns.getMeasureValue()));
                    }

                    if ("??????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        exVtsOcc.setValOth(vitalSigns.getMeasureValue());
                    }

                    if ("????????????".equals(vitalSigns.getSignName())) {
                        isExVtsOcc = true;
                        exVtsOcc.setEuStooltype(vitalSigns.getMeasureValue());
                    }

                    if (isExVtsOcc) {
                        com.zebone.nhis.common.support.BeanUtils.copyPropertiesIgnoreNull(exVtsOcc, a);
                        if ("????????????".equals(vitalSigns.getSignName())) {

                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setEuStooltype(vitalSigns.getMeasureValue());
                            }

                        }
                        if ("????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValStool(null);
                            }

                        }
                        if ("?????????????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValStoolColo(null);
                            }

                        }
                        if ("???????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValUrine(null);
                            }


                        }
                        if ("????????????????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setHourUrine(null);
                            }


                        }
                        if ("???????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValGast(null);
                            }


                        }
                        if ("???????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValGall(null);
                            }


                        }
                        if ("???????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValDrainage(null);
                            }

                        }
                        if ("????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValInput(null);
                            }


                        }
                        if ("?????????????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setHourInput(null);
                            }


                        }
                        if ("??????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValWeight(null);
                            }


                        }
                        if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValSbp(null);
                            }


                        }
                        if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValDbp(null);
                            }


                        }
                        if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValSbpAdd(null);
                            }


                        }
                        if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValDbpAdd(null);
                            }


                        }
                        if ("????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValAl(null);
                            }


                        }
                        if ("??????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValSt(null);
                            }


                        }
                        if ("?????????".equals(vitalSigns.getSignName())) {
                            isExVtsOcc = true;
                            if ("1".equals(vitalSigns.getMeasureValue())) {

                            }
                        }

                        if ("?????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValOutputTotal(null);
                            }

                        }

                        if ("???????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setHourOutputTotal(null);
                            }

                        }

                        if ("??????????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setDtOutputtype(null);
                            }


                        }

                        if ("????????????ml".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValOutput(null);
                            }

                        }

                        if ("??????????????????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setHourOutput(null);
                            }

                        }

                        if ("??????".equals(vitalSigns.getSignName())) {
                            if ("???".equals(vitalSigns.getMeasureValue())) {
                                a.setValOth(null);
                            }

                        }
//                        Thread.sleep(3000);
                        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getBean(DataSourceTransactionManager.class);

                        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // ??????????????????????????????????????????????????????????????????
                        TransactionStatus status = dataSourceTransactionManager.getTransaction(def); // ??????????????????
                        DataBaseHelper.update(DataBaseHelper.getUpdateSql(ExVtsOcc.class), a);
                        dataSourceTransactionManager.commit(status);

                    } else {

                        String sql1 = "select * from EX_VTS_OCC_DT where PK_VTSOCC = ?";
                        List<ExVtsOccDt> exVtsOccDtList = DataBaseHelper.queryForList(sql1, ExVtsOccDt.class, a.getPkVtsocc());
                        String pkOccDt = "";
                        for (ExVtsOccDt exVtsOccDt : exVtsOccDtList) {
                            if (DateUtils.isSameInstant(exVtsOccDt.getDateVts(), dateVts)) {
                                //??????
                                pkOccDt = exVtsOccDt.getPkVtsoccdt();
                                break;
                            }
                        }

                        if (StringUtils.isEmpty(pkOccDt)) {
                            //insert
                            ExVtsOccDt bean = new ExVtsOccDt();
                            bean.setPkOrg(piMaster.getPkOrg());
                            bean.setPkVtsocc(a.getPkVtsocc());

                            Date date = null;
                            try {
                                date = simpleDateFormat.parse(vitalSigns.getEnterDateTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTime(date);
                            System.out.println(calendar1.get(Calendar.HOUR_OF_DAY));
                            if (calendar1.get(Calendar.HOUR_OF_DAY) > 12) {
                                bean.setEuDateslot("1");
                            }
                            bean.setHourVts(calendar1.get(Calendar.HOUR));
                            bean.setEuTemptype("1");
                            bean.setEuHrtype("0");
                            bean.setEuBrtype("0");
                            bean.setModifier(employee.getPkEmp());
                            bean.setDelFlag("0");
                            bean.setDtVtscolltype("00");
                            bean.setDateVts(date);
                            bean.setFlagAdd("");
                            bean.setModityTime(new Date());
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setValTemp(new BigDecimal(vitalSigns.getMeasureValue()));
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setEuTemptype(vitalSigns.getMeasureValue());
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setValDrop(new BigDecimal(vitalSigns.getMeasureValue()));
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setValPulse(new BigDecimal(vitalSigns.getMeasureValue()));
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setEuHrtype(vitalSigns.getMeasureValue());
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setValHr(new BigDecimal(vitalSigns.getMeasureValue()));
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setEuBrtype(vitalSigns.getMeasureValue());
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setValBre(new BigDecimal(vitalSigns.getMeasureValue()));
                            }
                            if ("??????".equals(vitalSigns.getSignName())) {
                                bean.setNote(vitalSigns.getMeasureValue());
                            }

//                            Thread.sleep(3000);

                            DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getBean(DataSourceTransactionManager.class);

                            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // ??????????????????????????????????????????????????????????????????
                            TransactionStatus status = dataSourceTransactionManager.getTransaction(def); // ??????????????????
                            DataBaseHelper.insertBean(bean);
                            dataSourceTransactionManager.commit(status);
                        } else {
                            //update
                            String sql2 = "select * from EX_VTS_OCC_DT where PK_VTSOCCDT = ?";
                            ExVtsOccDt target = DataBaseHelper.queryForBean(sql2, ExVtsOccDt.class, pkOccDt);
                            ExVtsOccDt bean = new ExVtsOccDt();
                            Date date = null;
                            try {
                                date = simpleDateFormat.parse(vitalSigns.getEnterDateTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTime(date);
                            System.out.println(calendar1.get(Calendar.HOUR_OF_DAY));
                            if (calendar1.get(Calendar.HOUR_OF_DAY) > 12) {
                                bean.setEuDateslot("1");
                            }
                            bean.setHourVts(calendar1.get(Calendar.HOUR));
                            bean.setEuTemptype("1");
                            bean.setEuHrtype("0");
                            bean.setEuBrtype("0");
                            bean.setModifier(employee.getPkEmp());
                            bean.setDelFlag("0");
                            bean.setDtVtscolltype("00");
                            bean.setDateVts(date);
                            bean.setFlagAdd("");
                            bean.setModityTime(new Date());
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setValTemp(new BigDecimal(vitalSigns.getMeasureValue()));
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setEuTemptype(vitalSigns.getMeasureValue());
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setValDrop(new BigDecimal(vitalSigns.getMeasureValue()));
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setValPulse(new BigDecimal(vitalSigns.getMeasureValue()));
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setEuHrtype(vitalSigns.getMeasureValue());
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setValHr(new BigDecimal(vitalSigns.getMeasureValue()));
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setEuBrtype(vitalSigns.getMeasureValue());
                            }
                            if ("????????????".equals(vitalSigns.getSignName())) {
                                bean.setValBre(new BigDecimal(vitalSigns.getMeasureValue()));
                            }
                            if ("??????".equals(vitalSigns.getSignName())) {
                                bean.setNote(vitalSigns.getMeasureValue());
                            }


                            com.zebone.nhis.common.support.BeanUtils.copyPropertiesIgnoreNull(bean, target);
//                            Thread.sleep(3000);
                            DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getBean(DataSourceTransactionManager.class);

                            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // ??????????????????????????????????????????????????????????????????
                            TransactionStatus status = dataSourceTransactionManager.getTransaction(def); // ??????????????????
                            DataBaseHelper.update(DataBaseHelper.getUpdateSql(ExVtsOccDt.class), target);
                            dataSourceTransactionManager.commit(status);
                        }
                    }


                }
            }

            if (!exVtsOccList.stream().filter(a -> DateUtils.isSameDay(dateVts, a.getDateVts())).findAny().isPresent()) {
                //insert
                ExVtsOcc exVtsOcc = new ExVtsOcc();
                exVtsOcc.setPkOrg(pvEncounter.getPkOrg());
                exVtsOcc.setPkPi(piMaster.getPkPi());
                exVtsOcc.setPkPv(pvEncounter.getPkPv());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateVts);
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        0, 0, 0);
                Date dateVts1 = calendar.getTime();

                exVtsOcc.setDateVts(dateVts1);
                exVtsOcc.setPkDeptInput(dept.getPkDept());
                exVtsOcc.setDateInput(new Date());
                exVtsOcc.setPkEmpInput(employee.getPkEmp());
                exVtsOcc.setNameEmpInput(employee.getNameEmp());
                exVtsOcc.setCreator(employee.getPkEmp());
                exVtsOcc.setModifier(employee.getPkEmp());
                exVtsOcc.setDelFlag("0");
                exVtsOcc.setEuStooltype("0");
                exVtsOcc.setFlagColo("0");
                exVtsOcc.setInfantNo("0");
                exVtsOcc.setModityTime(new Date());
                if ("????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setEuStooltype(vitalSigns.getMeasureValue());
                }
                if ("????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValStool(Long.valueOf(vitalSigns.getMeasureValue()));
                }
                if ("?????????????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValStoolColo(Long.valueOf(vitalSigns.getMeasureValue()));
                }
                if ("???????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValUrine(vitalSigns.getMeasureValue());
                }
                if ("????????????????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setHourUrine(Integer.valueOf(vitalSigns.getMeasureValue()));
                }
                if ("???????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValGast(Long.valueOf(vitalSigns.getMeasureValue()));
                }
                if ("???????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValGall(Long.valueOf(vitalSigns.getMeasureValue()));
                }
                if ("???????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValDrainage(Long.valueOf(vitalSigns.getMeasureValue()));

                }
                if ("????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValInput(vitalSigns.getMeasureValue());
                }
                if ("?????????????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setHourInput(Integer.valueOf(vitalSigns.getMeasureValue()));
                }
                if ("??????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValWeight(vitalSigns.getMeasureValue());
                }
                if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValSbp(Long.valueOf(vitalSigns.getMeasureValue()));
                }
                if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValDbp(Long.valueOf(vitalSigns.getMeasureValue()));
                }
                if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValSbpAdd(Long.valueOf(vitalSigns.getMeasureValue()));
                }
                if ("?????????(??????)".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValDbpAdd(Long.valueOf(vitalSigns.getMeasureValue()));
                }
                if ("????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValAl(vitalSigns.getMeasureValue());
                }
                if ("??????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValSt(vitalSigns.getMeasureValue());
                }
                if ("?????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                }

                if ("?????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValOutputTotal(vitalSigns.getMeasureValue());
                }

                if ("???????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setHourOutputTotal(Integer.valueOf(vitalSigns.getMeasureValue()));
                }

                if ("??????????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setDtOutputtype(vitalSigns.getMeasureValue());
                }

                if ("????????????ml".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValOutput(vitalSigns.getMeasureValue());
                }

                if ("??????????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setHourOutput(Integer.valueOf(vitalSigns.getMeasureValue()));
                }

                if ("??????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setValOth(vitalSigns.getMeasureValue());
                }

                if ("????????????".equals(vitalSigns.getSignName())) {
                    isExVtsOcc = true;
                    exVtsOcc.setEuStooltype(vitalSigns.getMeasureValue());
                }

                if (isExVtsOcc) {
                    DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getBean(DataSourceTransactionManager.class);

                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // ??????????????????????????????????????????????????????????????????
                    TransactionStatus status = dataSourceTransactionManager.getTransaction(def); // ??????????????????
                    DataBaseHelper.insertBean(exVtsOcc);
                    dataSourceTransactionManager.commit(status);
                } else {
                    ExVtsOccDt bean = new ExVtsOccDt();
                    bean.setPkOrg(piMaster.getPkOrg());
                    bean.setPkVtsocc(exVtsOcc.getPkVtsocc());

                    Date date = simpleDateFormat.parse(vitalSigns.getEnterDateTime());
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(date);
                    System.out.println(calendar1.get(Calendar.HOUR_OF_DAY));
                    if (calendar1.get(Calendar.HOUR_OF_DAY) > 12) {
                        bean.setEuDateslot("1");
                    }
                    bean.setHourVts(calendar1.get(Calendar.HOUR));
                    bean.setEuTemptype("1");
                    bean.setEuHrtype("0");
                    bean.setEuBrtype("0");
                    bean.setModifier(employee.getPkEmp());
                    bean.setDelFlag("0");
                    bean.setDtVtscolltype("00");
                    bean.setDateVts(date);
                    bean.setFlagAdd("");
                    bean.setModityTime(new Date());
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        bean.setValTemp(new BigDecimal(vitalSigns.getMeasureValue()));
                    }
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        bean.setEuTemptype(vitalSigns.getMeasureValue());
                    }
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        bean.setValDrop(new BigDecimal(vitalSigns.getMeasureValue()));
                    }
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        bean.setValPulse(new BigDecimal(vitalSigns.getMeasureValue()));
                    }
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        bean.setEuHrtype(vitalSigns.getMeasureValue());
                    }
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        bean.setValHr(new BigDecimal(vitalSigns.getMeasureValue()));
                    }
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        bean.setEuBrtype(vitalSigns.getMeasureValue());
                    }
                    if ("????????????".equals(vitalSigns.getSignName())) {
                        bean.setValBre(new BigDecimal(vitalSigns.getMeasureValue()));
                    }


//                    Thread.sleep(3000);

                    DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getBean(DataSourceTransactionManager.class);

                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // ??????????????????????????????????????????????????????????????????
                    TransactionStatus status = dataSourceTransactionManager.getTransaction(def); // ??????????????????
                    DataBaseHelper.insertBean(bean);
                    dataSourceTransactionManager.commit(status);
                }


            }


        } catch (Exception e) {
            ack.put("ackCode", "AE");
            ack.put("ackDetail", e.getMessage());
            responseBody.setAck(ack);
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String result = JSON.toJSONString(responseBody);
            return result;
        }

        ack.put("ackCode", "AC");
        ack.put("ackDetail", "????????????");
        responseBody.setAck(ack);
        return JSON.toJSONString(responseBody);
    }
}
