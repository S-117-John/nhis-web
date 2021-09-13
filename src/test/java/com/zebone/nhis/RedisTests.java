package com.zebone.nhis;

import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.platform.modules.utils.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@ActiveProfiles(profiles = "pro")
public class RedisTests {

    @Test
    void test(){
        RedisUtils.setCacheHashObj("a","a","溜溜球");
        RedisUtils.setCacheHashObj("a","b",new Date());
        Map jjk = RedisUtils.getRedisTemplate().opsForHash().entries(RedisUtils.getRedisPreName()+"a1");
        System.out.println(JsonUtil.writeValueAsString(jjk));

        RedisUtils.delCacheHashObj("a","a");

        System.out.println(RedisUtils.getCacheHashObj("a","a"));

        RedisUtils.setCacheObj("a1","bb",1000);
        RedisUtils.setCacheObj("b","bb",1000);
        RedisUtils.setCacheObj("c","cc",1000);
        RedisUtils.setCacheObj("d","cc",1000);
        Set<String> keys = RedisUtils.getRedisTemplate().keys("*");
        System.out.println(keys.toString());

        RedisUtils.getRedisTemplate().delete(RedisUtils.getRedisTemplate().keys("a*"));
        System.out.println("~~~~~~~~~~~~~~~~");
        keys = RedisUtils.getRedisTemplate().keys("*");
        System.out.println(keys.toString());
    }
}
