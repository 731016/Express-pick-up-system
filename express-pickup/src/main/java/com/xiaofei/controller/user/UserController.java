package com.xiaofei.controller.user;

import com.auth0.jwt.JWTVerifier;
import com.xiaofei.common.ActionStatus;
import com.xiaofei.common.CommonResponse;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.common.ResultUtils;
import com.xiaofei.service.user.LoginPersistenceService;
import com.xiaofei.service.user.UserInfoService;
import com.xiaofei.service.user.UserRoleService;
import com.xiaofei.utils.CodecUtils;
import com.xiaofei.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户注册
 *
 * @date 2022/3/31 22:24
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private LoginPersistenceService loginPersistenceService;

    @ApiOperation("用户注册方法")
    @PostMapping("/register")
    public CommonResponse<String> register(@RequestBody UserInfoEntity userInfoEntity) {
        String userName = userInfoEntity.getUserName();
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(userName);
        if (userInfo != null) {
            return ResultUtils.error(ActionStatus.USERNAMEEXIST.getCode(), ActionStatus.USERNAMEEXIST.getMsg(), "");
        }
        Boolean insertFlag = userInfoService.insertUserInfo(userInfoEntity);
        String userId = userInfoService.selectOneUserInfo(userInfoEntity.getUserName()).getUserId();
        Boolean addFlag = loginPersistenceService.add(userId, userInfoEntity.getUserName(), CodecUtils.getSalt());
        if (insertFlag || addFlag) {
            return ResultUtils.success("");
        }
        return ResultUtils.error(null);
    }

    @ApiOperation("登录方法，生成token")
    @PostMapping("/login")
    public CommonResponse<String> login(@RequestBody UserInfoEntity userInfoEntity) {
        String userName = userInfoEntity.getUserName();
        String passWord = userInfoEntity.getPassWord();
        //查询用户是否存在
        UserInfoEntity user = userInfoService.selectOneUserInfo(userName);
        if (user != null) {
            //获取注册时的盐值
            String salt = loginPersistenceService.querySalt(user.getUserId());
            //加密后，验证
            String pwd = CodecUtils.loginEncrypt(passWord, salt);
            Boolean checkResult = userInfoService.checkUserNameAndPwd(userName, pwd);
            String token = null;
            if (checkResult) {
                token = JwtUtils.sign(userName, pwd);
                //设置token
                Boolean setTokenFlag = loginPersistenceService.updateToken(user.getUserId(), token);
                if (token != null && setTokenFlag) {
                    return ResultUtils.success(token);
                }
            }
        }
        return ResultUtils.error(ActionStatus.LOGINERROR.getCode(), ActionStatus.LOGINERROR.getMsg(), null);
    }

    @ApiOperation("进入菜单，获取用户权限")
    @PostMapping("/selectOneUserInfo")
    public CommonResponse<UserInfoEntity> selectOneUserInfo(@RequestHeader(value = "Authorization") String token) {
        //根据token获取用户名称
        String userName = JwtUtils.getUserNameByToken(token);
        //查询用户信息
        UserInfoEntity user = userInfoService.selectOneUserInfo(userName);
        //重新设置用户角色名称
        String userRoleId = user.getUserRoleId();
        String roleName = userRoleService.selectRoleNameByUserRoleId(userRoleId);
        user.setUserRoleName(roleName);
        if (user == null) {
            return ResultUtils.error(ActionStatus.MENUPULLERROR.getCode(), ActionStatus.MENUPULLERROR.getMsg(), null);
        } else {
            return ResultUtils.success(user);
        }
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public CommonResponse<String> logout(@RequestBody UserInfoEntity userInfoEntity) {
        Boolean delFlag = loginPersistenceService.delToken(userInfoEntity.getUserId());
        if (delFlag) {
            return ResultUtils.success("用户登录信息删除成功", null);
        }
        return ResultUtils.error("");
    }

    @ApiOperation("个人中心，查询用户信息")
    @PostMapping("/queryUserInfo")
    public CommonResponse<UserInfoEntity> queryUserInfo(@RequestHeader(value = "Authorization") String token) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        String roleName = userRoleService.selectRoleNameByUserRoleId(userInfo.getUserRoleId());
        userInfo.setUserRoleName(roleName);
        return ResultUtils.success(userInfo);
    }

    @ApiOperation("个人中心，修改密码")
    @PostMapping("/updatePwd")
    public CommonResponse<String> updatePwd(@RequestHeader(value = "Authorization") String token, @RequestBody UserInfoEntity userInfoEntity) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        //获取注册时的盐值
        String salt = loginPersistenceService.querySalt(userInfo.getUserId());
        //加密后，验证
        String pwd = CodecUtils.loginEncrypt(userInfoEntity.getPassWord(), salt);
        //判断修改后的密码是否相同
        String passWord = userInfo.getPassWord();
        if (passWord.equals(pwd)){
            return ResultUtils.error(ActionStatus.PWDEXIST.getCode(),ActionStatus.PWDEXIST.getMsg(),"");
        }
        userInfo.setPassWord(pwd);
        Boolean flag = userInfoService.update(userInfo);
        if (flag) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("个人中心，修改手机号")
    @PostMapping("/updatePhoneAjax")
    public CommonResponse<String> updatePhoneAjax(@RequestHeader(value = "Authorization") String token, @RequestBody UserInfoEntity userInfoEntity) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        userInfo.setPhone(userInfoEntity.getPhone());
        Boolean update = userInfoService.update(userInfo);
        if (update){
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("个人中心，修改性别")
    @PostMapping("/updateSexAjax")
    public CommonResponse<String> updateSexAjax(@RequestHeader(value = "Authorization") String token, @RequestBody UserInfoEntity userInfoEntity) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        userInfo.setSex(userInfoEntity.getSex());
        Boolean update = userInfoService.update(userInfo);
        if (update){
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }
}
