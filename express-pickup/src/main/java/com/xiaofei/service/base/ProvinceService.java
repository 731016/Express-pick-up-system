package com.xiaofei.service.base;

import com.xiaofei.entity.base.ProvinceEntity;

import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/15 23:07
 */
public interface ProvinceService {
    /**
     * 查询所有省份信息
     */
    List<ProvinceEntity> selectAll();
    /**
     * 查询单个省份信息
     */
    ProvinceEntity selectOne(Integer id);
}
