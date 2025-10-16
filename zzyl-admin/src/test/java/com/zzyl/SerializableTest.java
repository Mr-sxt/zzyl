package com.zzyl;

import com.zzyl.nursing.domain.NursingProject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;

@SpringBootTest
public class SerializableTest {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Test
    public void test() {
        NursingProject nursingProject = new NursingProject();
        nursingProject.setId(1L);
        nursingProject.setName("测试");
        nursingProject.setOrderNo(1);
        nursingProject.setUnit("个");
        nursingProject.setPrice(new BigDecimal(1));
        nursingProject.setImage("1");
        nursingProject.setNursingRequirement("1");
        nursingProject.setStatus(1);

        redisTemplate.opsForValue().set("nursingProject",nursingProject);
        System.out.println(redisTemplate.opsForValue().get("nursingProject"));

    }
}
