package com.xiaofei.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.entity.base.ProvinceEntity;
import com.xiaofei.mapper.base.ProvinceMapper;
import com.xiaofei.service.base.ProvinceService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/15 23:08
 */
@Service
public class ProvinceServiceImpl implements ProvinceService {
    @Resource
    private ProvinceMapper provinceMapper;

    /**
     * 查询所有省份信息
     */
    @Override
    public List<ProvinceEntity> selectAll() {
        List<ProvinceEntity> entities = provinceMapper.selectList(null);
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities;
    }

    /**
     * 查询单个省份信息
     *
     * @param id
     */
    @Override
    public ProvinceEntity selectOne(Integer id) {
        QueryWrapper<ProvinceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        ProvinceEntity entity = provinceMapper.selectOne(wrapper);
        return entity;
    }
}
