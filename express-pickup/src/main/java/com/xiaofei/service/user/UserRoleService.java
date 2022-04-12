package com.xiaofei.service.user;

/**
 * 用户角色信息，service
 *
 * @date 2022/4/4 12:29
 */
public interface UserRoleService {
    /**
     * 根据用户角色id，查询用户角色名称
     */
    String selectRoleNameByUserRoleId(String userRoleId);
}
