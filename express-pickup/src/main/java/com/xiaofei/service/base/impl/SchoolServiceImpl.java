package com.xiaofei.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.entity.base.SchoolEntity;
import com.xiaofei.mapper.base.SchoolMapper;
import com.xiaofei.service.base.SchoolService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/15 23:06
 */
@Service
public class SchoolServiceImpl implements SchoolService {
    @Resource
    private SchoolMapper schoolMapper;
    /**
     * 查询所有学校
     */
    @Override
    public List<SchoolEntity> selectAll() {
        List<SchoolEntity> entities = schoolMapper.selectList(null);
        if (CollectionUtils.isEmpty(entities)){
            return new ArrayList<>();
        }
        return entities;
    }

    /**
     * 根据id查询学校
     *
     * @param province
     */
    @Override
    public List<SchoolEntity> selectList(Integer province) {
        QueryWrapper<SchoolEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("provinceId", province);
        List<SchoolEntity> entity = schoolMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entity)){
            return new ArrayList<>();
        }
        return entity;
    }

    /**
     * 根据id，查询单个学校信息
     *
     * @param id
     */
    @Override
    public SchoolEntity selectOne(Integer id) {
        QueryWrapper<SchoolEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        SchoolEntity entity = schoolMapper.selectOne(wrapper);
        return entity;
    }
}
