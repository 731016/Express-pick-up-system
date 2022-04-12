package com.xiaofei.service.base;

import com.xiaofei.entity.base.TrackCompanyEntity;

import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/10 13:38
 */
public interface TrackCompanyService {
    /**
     * 批量查询，根据id
     */
    List<TrackCompanyEntity> batchSelectAll(List<String> ids);
}
