package com.xiaofei.expresspickup.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * TODO 类描述
 *
 * @date 2022/3/27 17:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * '用户id'
     */
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    /**
     * '姓名'
     */
    private String userName;
    /**
     * '密码'
     */
    private String passWord;
    /**
     * '角色' [100,普通用户；101,配送用户；102,管理员]
     */
    private String userRoleId;
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
     * '学校代码'
     */
    private String schoolNumber;
    /**
     * '学校名称'
     */
    private String schoolName;
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
     * '冻结时间'
     */
    private Date freezeTime;
    /**
     * 注册时间
     */
    private Date registerTime;
    /**
     * 是否禁用 【1：启用，0：禁用】
     */
    private Integer disable;
}
