package com.xiaofei.filter;

import com.xiaofei.common.ActionStatus;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.service.user.UserInfoService;
import com.xiaofei.utils.JwtUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证token是否生效
 *
 * @date 2022/3/31 20:51
 */
public class AllRequestInterceptor implements HandlerInterceptor {
    @Resource
    private UserInfoService userInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token != null) {
            String userName = JwtUtils.getUserNameByToken(token);
            UserInfoEntity userInfoEntity = userInfoService.selectOneUserInfo(userName);
            //可能token解析不出name
            if (userInfoEntity != null) {
                String passWord = userInfoEntity.getPassWord();
                boolean verifyResult = JwtUtils.verify(token, userName, passWord);
                if (verifyResult) {
                    return true;
                }
            }
            response.setStatus(ActionStatus.TOKENINVALID.getCode());
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
