package com.xiaofei.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户信息
 *
 * @date 2022/3/27 17:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_info")
public class UserInfoEntity {
    /**
     * '用户id'
     */
    @TableId(value = "userId", type = IdType.ASSIGN_UUID)
    private String userId;
    /**
     * '姓名'
     */
    private String userName;
    /**
     * '密码'
     */
    @TableField(exist = false)
    private String oldPwd;
    private String passWord;
    /**
     * '角色' [C,普通用户；B,配送用户；A,管理员]
     */
    private String userRoleId;
    /**
     * 角色名称，不存储数据库
     */
    @TableField(exist = false)
    private String userRoleName;
    /**
     * '性别'
     */
    private String sex;
    /**
     * '真实姓名'
     */
    private String actualName;
    /**
     * '身份证号'
     */
    private String idNumber;
    /**
     * 实名状态不存储数据库
     */
    @TableField(exist = false)
    private String realNameStatus;
    /**
     * '学校代码'
     */
    private String schoolId;
    /**
     * '学校名称'
     */
    private String schoolName;
    /**
     * 学号
     */
    private String schoolNumber;
    /**
     * '手机号'
     */
    private String phone;
    /**
     * '三方登陆类型'
     */
    private String signInWithType;
    /**
     * '三方登录id'
     */
    private String signInWithId;
    /**
     * 用户评分，不存储数据库
     */
    @TableField(exist = false)
    private Double rate;
    /**
     * '冻结时间'
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date freezeTime;
    /**
     * 注册时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerTime;
    /**
     * 是否禁用 【1：启用，0：禁用】
     */
    private Integer disable;
    /**
     * 启用状态
     */
    @TableField(exist = false)
    private String disableName;
}
