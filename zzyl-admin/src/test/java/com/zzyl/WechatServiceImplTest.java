package com.zzyl;

import com.zzyl.nursing.service.WechatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WechatServiceImplTest {
    @Autowired
    private WechatService wechatService;

    @Test
    public void testGetOpenId() {
        String openId = wechatService.getOpenId("0f1JFy000NdhaV1Wyu200R6XyE4JFy0G");
        System.out.println(openId);
    }

    @Test
    public void testGetPhoneNumber() {
        String phoneNumber = wechatService.getPhoneNumber("57b06dd0d0b72b551ad4173683d5d5e9c8715e63b9c73898dcff609c31dbc403");
        System.out.println(phoneNumber);
    }
}
