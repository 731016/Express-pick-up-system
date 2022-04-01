package com.xiaofei.expresspickup.controller.user;

import com.xiaofei.expresspickup.entity.common.CommonResponse;
import com.xiaofei.expresspickup.entity.User;
import com.xiaofei.expresspickup.entity.common.ResultUtils;
import com.xiaofei.expresspickup.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户注册
 *
 * @date 2022/3/31 22:24
 */
@Api(tags = "用户注册接口")
@RestController
@RequestMapping("/user")
public class RegisterController {
    @Resource
    private UserService userService;

    @ApiOperation("注册方法")
    @PostMapping("/register")
    private CommonResponse<String> register(@RequestBody User user) {
        Boolean insertFlag = userService.insertUser(user);
        if (insertFlag) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }
}
