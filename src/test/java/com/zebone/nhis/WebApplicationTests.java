package com.zebone.nhis;
import com.zebone.nhis.common.dao.BaseCodeMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.security.shiro.AjaxFormAuthenticationFilter;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@SpringBootTest
@ActiveProfiles(profiles = "dev")
class WebApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(WebApplicationTests.class);

    @Resource
    private BaseCodeMapper baseCodeMapper;

    @Test
    public void log(){
        logger.info("你好：{}","中国");
    }
    @Test
    public void testMapper(){
        baseCodeMapper.listByDtDateslottype("xx");
    }

    @Test
    public void restTest(){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        User user = new User();
        user.setId("x");
        user.setNameEmp("猫猫");
        restTemplate.postForEntity("http://127.0.0.1:8080/",user,String.class);
    }
}
