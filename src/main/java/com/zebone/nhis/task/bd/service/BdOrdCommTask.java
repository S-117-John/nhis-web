package com.zebone.nhis.task.bd.service;

import com.zebone.nhis.common.module.cn.ipdw.BdOrdComm;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BdOrdCommTask {

    private final static Logger logger = LoggerFactory.getLogger("nhis.quartz");



    public void personalData(QrtzJobCfg cfg){

        DefaultTransactionDefinition transDefinition1 = new DefaultTransactionDefinition();
        transDefinition1.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager1 = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus1 = dataSourceTransactionManager1.getTransaction(transDefinition1);

        //数据全删
        DataBaseHelper.execute("delete from BD_ORD_COMM");
        dataSourceTransactionManager1.commit(transStatus1);
        System.out.println("删除数据完成！");


        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);

        //将CN_ORDER数据导入BD_ORD_COMM
        String param = cfg.getJobparam();
        Map<String,Integer> map = JsonUtil.readValue(param, Map.class);
        Integer day = MapUtils.getInteger(map,"day",60);
        Integer startDay = MapUtils.getInteger(map,"startDay",0);//中山人医要求排除最近24小时数据
        Date now = new Date();
        Date start = DateUtils.addDays(now,-day);
        now = DateUtils.addDays(now,-startDay);
        String s= null;

        if (Application.isSqlServer()){
            s= "substring(CODE_ORDTYPE ,1,2) in ('02','03','04','05','06','07','08','10','11','12','13','14','99') and\n";
        }else{
            s="SUBSTR (CODE_ORDTYPE, 0, 2) in ('02','03','04','05','06','07','08','10','11','12','13','14','99') and\n";
        }
        String sql = "SELECT PK_ORD,PK_ORG,PK_EMP_INPUT PK_EMP, CODE_ORDTYPE, PK_DEPT,count(PK_ORD) as times \n" +
                "FROM CN_ORDER\n" +
                "WHERE\n" + s+
                "EU_PVTYPE in ('1','2')\n" +
                "and\n" +
                "CREATE_TIME BETWEEN ? AND ?" + 
                " group by PK_ORD,PK_ORG,PK_EMP_INPUT,CODE_ORDTYPE,PK_DEPT ";
        	
        List<BdOrdComm> bdOrdCommList = DataBaseHelper.queryForList(sql,BdOrdComm.class,new Object[]{start,now});
        
        if (bdOrdCommList.size() > 0) {
        	Date date = new Date();
        	for (BdOrdComm bdOrdComm : bdOrdCommList) {
        		bdOrdComm.setPkOrdcomm(NHISUUID.getKeyId());
        		bdOrdComm.setCreator("admin");
        		bdOrdComm.setCreateTime(date);
        		bdOrdComm.setDelFlag("0");
        		bdOrdComm.setTs(date);
			}
			DataBaseHelper.batchUpdate( DataBaseHelper.getInsertSql(BdOrdComm.class),bdOrdCommList);
		}
        /*
        List<BdOrdCommVO> bdOrdCommList = DataBaseHelper.queryForList(sql,BdOrdCommVO.class,new Object[]{start,now});
        Map<String, Long> collect = bdOrdCommList.stream().map(BdOrdCommVO::getKey).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        //key去重
        List<BdOrdCommVO> distinctList = bdOrdCommList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(
                                ()->new TreeSet<>(Comparator.comparing(BdOrdCommVO::getKey))
                        ),ArrayList::new
                )
        );

        collect.forEach((k,v)->{
            distinctList.stream()
                    .filter(u->u.getKey().equals(k))
                    .findAny()
                    .ifPresent(u->{
                        u.setTimes(v);
                        BdOrdComm bdOrdComm = new BdOrdComm();
                        BeanUtils.copyProperties(u,bdOrdComm);
                        DataBaseHelper.insertBean(bdOrdComm);

                    });
        });
        */
        dataSourceTransactionManager.commit(transStatus);
        System.out.println(param);
    }


}
