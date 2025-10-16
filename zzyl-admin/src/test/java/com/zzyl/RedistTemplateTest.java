package com.zzyl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedistTemplateTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void testString() {
        //保存字符串
        redisTemplate.opsForValue().set("name","zzyl");
        //获取字符串
        String name = redisTemplate.opsForValue().get("name");
        System.out.println(name);

        //设置过期字符串
        redisTemplate.opsForValue().set("name1","李四",10, TimeUnit.SECONDS);
    }
}
