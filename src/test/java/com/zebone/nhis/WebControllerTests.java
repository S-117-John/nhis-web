package com.zebone.nhis;

import com.zebone.WebApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes={WebApplication.class})
@AutoConfigureMockMvc
public class WebControllerTests {
    private static final Logger logger = LoggerFactory.getLogger(WebControllerTests.class);

    @Autowired
    private MockMvc mvc;
    private MockHttpSession session;

    @BeforeEach
    public void setupMockMvc(){
        session = new MockHttpSession();
    }

    @Test
    void login() throws Exception{
        //login流程：输入用户名密码跳转到FramewrokController>/login(注意是POST)，经过Shiro认证，回转到/,成功后跳转到index>index.html
        //已经有登录状态时，访问直接进入index
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/login")
                        .accept(MediaType.ALL)
                        .session(session)
                        //这里要特别注意和content传参数的不同，具体看你接口接受的是哪种
                        .param("username", "alv").param("password", "0")
                //传json参数,最后传的形式是 Body = {"password":"admin","userName":"admin"}
                //.content(JSON.toJSON(info).toString().getBytes())
        ).andExpect(MockMvcResultMatchers.status().isFound())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        logger.info(content);

        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.post("/sessionout")).andReturn();
        logger.info("退出返回：{}",mvcResult1.getResponse().getContentAsString());
    }
}
