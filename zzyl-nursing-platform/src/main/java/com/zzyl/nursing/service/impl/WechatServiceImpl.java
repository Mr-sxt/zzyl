package com.zzyl.nursing.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zzyl.common.exception.base.BaseException;
import com.zzyl.nursing.service.WechatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatServiceImpl implements WechatService {
    // 登录
    private static final String REQUEST_URL = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code";

    // 获取token
    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    // 获取手机号
    private static final String PHONE_REQUEST_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=";

    @Value("${wechat.appid}")
    private String appId;
    @Value("${wechat.secret}")
    private String appSecret;
    /**
     * 微信登录
     * @param code 微信小程序端的临时登录凭证
     * @return
     */
    @Override
    public String getOpenId(String code) {
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appId);
        params.put("secret",appSecret);
        params.put("js_code",code);
        //判断接口响应是否出错
        String result = HttpUtil.get(REQUEST_URL,params);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if(ObjectUtil.isNotEmpty(jsonObject.getInt("errcode"))){
            throw new BaseException(jsonObject.getStr("errmsg"));
        }
        return jsonObject.getStr("openid");
    }

    /**
     * 获取微信手机号
     * @param phoneCode
     * @return
     */
    @Override
    public String getPhoneNumber(String phoneCode) {
        Map<String,Object> params = new HashMap<>();
        params.put("code",phoneCode);
        String result =  HttpUtil.post(PHONE_REQUEST_URL + getAccessToken(),JSONUtil.toJsonStr(params));
        JSONObject jsonObject = JSONUtil.parseObj(result);

        if (!ObjectUtil.equals(jsonObject.getInt("errcode"),0)){
            throw new BaseException(jsonObject.getStr("errmsg"));
        }
        return jsonObject.getJSONObject("phone_info").getStr("phoneNumber");
    }

    /**
     * 获取微信接口调用凭证
     * @return
     */
    private String getAccessToken() {
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appId);
        params.put("secret",appSecret);
        String result = HttpUtil.get(TOKEN_URL, params);
        //判断接口响应是否出错
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (ObjectUtil.isNotEmpty(jsonObject.getInt("errcode"))){
            throw new BaseException(jsonObject.getStr("errmsg"));
        }
        return jsonObject.getStr("access_token");
    }

    /**
     * 小程序登录
     **/


}
