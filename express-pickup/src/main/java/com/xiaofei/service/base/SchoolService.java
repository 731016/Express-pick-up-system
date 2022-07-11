package com.xiaofei.service.base;

import com.xiaofei.entity.base.SchoolEntity;

import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/15 23:05
 */
public interface SchoolService {
    /**
     * 查询所有学校
     */
    List<SchoolEntity> selectAll();
    /**
     * 根据provinceid查询学校
     * @param provinceId
     */
    List<SchoolEntity> selectList(Integer provinceId);
    /**
     * 根据id，查询单个学校信息
     */
    SchoolEntity selectOne(Integer id);
}
