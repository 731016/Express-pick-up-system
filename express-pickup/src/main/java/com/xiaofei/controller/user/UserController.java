package com.xiaofei.controller.user;

import com.auth0.jwt.JWTVerifier;
import com.xiaofei.common.ActionStatus;
import com.xiaofei.common.CommonResponse;
import com.xiaofei.common.IdCardResponseStatus;
import com.xiaofei.entity.base.SchoolEntity;
import com.xiaofei.entity.user.UserInfoEntity;
import com.xiaofei.common.ResultUtils;
import com.xiaofei.service.base.ProvinceService;
import com.xiaofei.service.base.SchoolService;
import com.xiaofei.service.user.LoginPersistenceService;
import com.xiaofei.service.user.UserInfoService;
import com.xiaofei.service.user.UserRoleService;
import com.xiaofei.utils.CodecUtils;
import com.xiaofei.utils.DateUtils;
import com.xiaofei.utils.HttpUtils;
import com.xiaofei.utils.JwtUtils;
import com.xiaofei.vo.IdCardVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户注册
 *
 * @date 2022/3/31 22:24
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private LoginPersistenceService loginPersistenceService;
    @Resource
    private SchoolService schoolService;
    @Resource
    private ProvinceService provinceService;

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
            logger.info("获取用户注册时的盐值 -> " + salt);
            if (StringUtils.isEmpty(salt)) {
                logger.info("无法获取用户注册时加密的密钥，请检查login_persistence表中，用户id -> 【" + user.getUserId() + "】数据是否存在！");
                return ResultUtils.error(ActionStatus.USERSTATUSEXCEPTION.getCode(), ActionStatus.USERSTATUSEXCEPTION.getMsg(), null);
            }
            //加密后，验证
            String pwd = CodecUtils.loginEncrypt(passWord, salt);
            Boolean checkResult = userInfoService.checkUserNameAndPwd(userName, pwd);
            String token = null;
            if (checkResult) {
                //验证用户是否被禁用或处于冻结状态
                if (user.getDisable() == 0) {
                    return ResultUtils.error(ActionStatus.USERDISABLE.getCode(), ActionStatus.USERDISABLE.getMsg(), "");
                }
                Date time = user.getFreezeTime();
                if (time != null) {
                    if (System.currentTimeMillis() < time.getTime()) {
                        return ResultUtils.error(ActionStatus.USERFREEZE.getCode(), ActionStatus.USERFREEZE.getMsg() + "解封时间：" + DateUtils.format(user.getFreezeTime()), null);
                    }
                }
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
        //验证密码是否正确
        String oldPwd = CodecUtils.loginEncrypt(userInfoEntity.getOldPwd(), salt);
        //前台传递后来要修改的密码
        String pwd = CodecUtils.loginEncrypt(userInfoEntity.getPassWord(), salt);
        //数据库的密码
        String passWord = userInfo.getPassWord();
        //先判断原密码是否正确
        if (StringUtils.equals(oldPwd, passWord)) {
            //判断修改后的密码是否相同
            if (passWord.equals(pwd)) {
                return ResultUtils.error(ActionStatus.PWDEXIST.getCode(), ActionStatus.PWDEXIST.getMsg(), "");
            }
            userInfo.setPassWord(pwd);
            Boolean flag = userInfoService.update(userInfo);
            if (flag) {
                return ResultUtils.success("");
            }
        } else {
            return ResultUtils.error(ActionStatus.PWDERROR.getCode(), ActionStatus.PWDERROR.getMsg(), "");
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
        if (update) {
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
        if (update) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }

    @ApiOperation("个人中心，修改学校信息")
    @PostMapping("/updateSchoolInfo")
    public CommonResponse<UserInfoEntity> updateSchoolInfo(@RequestHeader(value = "Authorization") String token, @RequestBody UserInfoEntity userInfoEntity) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        //因为<el-select>value和label，导致提交的值位value的编号
        userInfo.setSchoolId(userInfoEntity.getSchoolName());
        //查询名称
        Integer schoolId = Integer.parseInt(userInfoEntity.getSchoolName());
        if (schoolId != null) {
            SchoolEntity entity = schoolService.selectOne(schoolId);
            userInfo.setSchoolName(entity.getSchoolName());
        }
        userInfo.setSchoolNumber(userInfoEntity.getSchoolNumber());
        Boolean update = userInfoService.update(userInfo);
        if (update) {
            return ResultUtils.success(userInfo);
        }
        return ResultUtils.error(null);
    }

    @ApiOperation("个人中心，修改实名信息")
    @PostMapping("/updateIdCard")
    public CommonResponse<UserInfoEntity> updateIdCard(@RequestHeader(value = "Authorization") String token, @RequestBody UserInfoEntity userInfoEntity) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        //收集所有的身份证号，查询是否已绑定
        Boolean flag = userInfoService.selectAllIdNumber(userInfoEntity.getActualName(), userInfoEntity.getIdNumber());
        if (flag) {
            return ResultUtils.error(ActionStatus.IDNUMBEREXIST.getCode(), ActionStatus.IDNUMBEREXIST.getMsg(), null);
        } else {
            //校验身份证是否可用发送ajax
            Map<String, String> params = new HashMap<>();
            params.put("idcard", userInfoEntity.getIdNumber());
            params.put("name", userInfoEntity.getActualName());
            try {
                IdCardVo idCardVo = HttpUtils.postForm(params);
                int code = idCardVo.getCode();
                //状态码“0”，请求成功
                Boolean success = IdCardResponseStatus.isSuccess(code);
                if (success) {
                    String res = idCardVo.getResult().getRes();
                    //核验结果“1”，身份证号一致
                    switch (res) {
                        case "1":
                            userInfo.setIdNumber(userInfoEntity.getIdNumber());
                            userInfo.setActualName(userInfoEntity.getActualName());
                            Boolean update = userInfoService.update(userInfo);
                            if (update) {
                                return ResultUtils.success(userInfo);
                            }
                            break;
                        //不一致
                        case "2":
                            //无记录
                            return ResultUtils.error("身份证" + idCardVo.getResult().getDescription(), null);
                        case "3":
                            return ResultUtils.error("身份证" + idCardVo.getResult().getDescription(), null);
                    }
                } else {
                    return ResultUtils.error(idCardVo.getMessage(), null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResultUtils.error(null);
        }
    }

    @ApiOperation("个人中心，切换用户角色为配送员")
    @PostMapping("/verifyPwdAndChange")
    public CommonResponse<String> verifyPwdAndChange(@RequestHeader(value = "Authorization") String token, @RequestBody UserInfoEntity userInfoEntity) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        String roleName = userRoleService.selectRoleNameByUserRoleId("B");
        String pwd = CodecUtils.encrypt(userInfoEntity.getPassWord());
        String passWord = userInfo.getPassWord();
        if (StringUtils.equals(pwd, passWord)) {
            UserInfoEntity entity = userInfoService.selectOneUserInfo(userInfo.getUserName());
            entity.setUserRoleId("B");
            entity.setUserRoleName(roleName);
            Boolean update = userInfoService.update(entity);
            if (update) {
                return ResultUtils.success(ActionStatus.USERROLECHANGESUCCESS.getMsg(), "");
            }
        }
        return ResultUtils.error(ActionStatus.PWDERROR.getCode(), ActionStatus.PWDERROR.getMsg(), "");
    }


    @ApiOperation("个人中心，切换用户角色为普通用户")
    @PostMapping("/exchangeRoleToGeneral")
    public CommonResponse<String> exchangeRoleToGeneral(@RequestHeader(value = "Authorization") String token, @RequestBody UserInfoEntity userInfoEntity) {
        String name = JwtUtils.getUserNameByToken(token);
        UserInfoEntity userInfo = userInfoService.selectOneUserInfo(name);
        String roleName = userRoleService.selectRoleNameByUserRoleId("C");
        String pwd = CodecUtils.encrypt(userInfoEntity.getPassWord());
        String passWord = userInfo.getPassWord();
        if (StringUtils.equals(pwd, passWord)) {
            UserInfoEntity entity = userInfoService.selectOneUserInfo(userInfo.getUserName());
            entity.setUserRoleId("C");
            entity.setUserRoleName(roleName);
            Boolean update = userInfoService.update(entity);
            if (update) {
                return ResultUtils.success(ActionStatus.USERROLECHANGESUCCESS.getMsg(), "");
            }
        }
        return ResultUtils.error(ActionStatus.PWDERROR.getCode(), ActionStatus.PWDERROR.getMsg(), "");
    }

    @ApiOperation("个人中心，禁用用户")
    @PostMapping("/disableUser")
    public CommonResponse<String> disableUser(@RequestBody List<String> userIds) {
        List<UserInfoEntity> entities = userInfoService.selectUserByIds(userIds);
        for (UserInfoEntity entity : entities) {
            entity.setDisable(1);
        }
        Boolean aBoolean = userInfoService.disableUser(entities);
        if (aBoolean) {
            return ResultUtils.success("");
        }
        return ResultUtils.error("");
    }
}
