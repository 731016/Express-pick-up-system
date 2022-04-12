package com.xiaofei.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaofei.entity.base.TrackCompanyEntity;
import com.xiaofei.mapper.base.TrackCompanyMapper;
import com.xiaofei.service.base.TrackCompanyService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/10 13:39
 */
@Service
public class TrackCompanyServiceImpl implements TrackCompanyService {
    @Resource
    private TrackCompanyMapper trackCompanyMapper;

    /**
     * 批量查询，根据id
     *
     * @param ids
     */
    @Override
    public List<TrackCompanyEntity> batchSelectAll(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        QueryWrapper<TrackCompanyEntity> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        List<TrackCompanyEntity> list = trackCompanyMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }
}
