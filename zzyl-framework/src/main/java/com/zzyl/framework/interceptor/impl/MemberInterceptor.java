package com.zzyl.framework.interceptor.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zzyl.common.exception.base.BaseException;
import com.zzyl.common.utils.StringUtils;
import com.zzyl.common.utils.UserThreadLocal;
import com.zzyl.framework.web.service.TokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MemberInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是controller 方法，则不进行拦截
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //获取header中的token
        String token = request.getHeader("authorization");
        //判断 token，为空直接返回401
        if(StringUtils.isEmpty(token)){
            response.setStatus(401);
            throw new BaseException("认证失败！");
        }
        // 解析token
        Claims claims = tokenService.parseToken(token);
        if (ObjectUtil.isEmpty(claims)) {
            response.setStatus(401);
            throw new BaseException("认证失败");
        }

        //解析token中的的数据，保存到ThreadLocal中
        Long userId = MapUtil.getLong(claims, "userId");
        if(ObjectUtil.isEmpty(userId)){
            response.setStatus(401);
            throw new BaseException("认证失败！");
        }

        //保存用户id到线程
        UserThreadLocal.set(userId);

        //放行
        return true;
    }

    /**
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户id
        UserThreadLocal.remove();
    }
}
