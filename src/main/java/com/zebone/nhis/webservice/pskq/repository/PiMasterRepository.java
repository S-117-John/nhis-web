package com.zebone.nhis.webservice.pskq.repository;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class PiMasterRepository {

    public static PiMaster findByCodePi(String code){
        String sql = "select * from PI_MASTER where CODE_PI = ?";
        PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,new Object[]{code});
        return piMaster;
    }

    public static int updateMpi(String empId,String codePi){
        String sql = "update PI_MASTER set MPI = ? where CODE_PI = ?";
        JdbcTemplate jdbcTemplate = SpringContextHolder.getApplicationContext().getBean(JdbcTemplate.class);
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
        int result = 0;
        try{
            result = jdbcTemplate.update(sql,new Object[]{empId,codePi});
            dataSourceTransactionManager.commit(transStatus);
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transStatus);
        }
        return result;
    }
}
