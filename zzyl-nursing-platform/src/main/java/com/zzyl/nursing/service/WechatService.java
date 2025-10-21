package com.zzyl.nursing.service;

public interface WechatService {
    /**
     * 微信登录
     * @param code 微信小程序端的临时登录凭证
     * @return
     */
    String getOpenId(String code);
    /**
     * 获取手机号
     * @param code 临时登录凭证
     * @return
     */
    String getPhoneNumber(String code);

}
