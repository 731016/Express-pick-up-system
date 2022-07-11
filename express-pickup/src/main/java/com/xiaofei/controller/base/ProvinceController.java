package com.xiaofei.controller.base;

import com.xiaofei.common.CommonResponse;
import com.xiaofei.common.ResultUtils;
import com.xiaofei.entity.base.ProvinceEntity;
import com.xiaofei.entity.base.SchoolEntity;
import com.xiaofei.service.base.ProvinceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/16 13:06
 */
@Api(tags = "省份信息接口")
@RestController
@RequestMapping("/province")
public class ProvinceController {
    @Resource
    private ProvinceService provinceService;

    @ApiOperation("查询所有省份信息")
    @PostMapping("/selectAllProvince")
    public CommonResponse<List<ProvinceEntity>> selectAllProvince() {
        List<ProvinceEntity> entities = provinceService.selectAll();
        return ResultUtils.success(entities);
    }

    @ApiOperation(value = "查询单个省份信息")
    @PostMapping("/selectOneProvince")
    public CommonResponse<ProvinceEntity> selectOneProvince(@RequestBody ProvinceEntity provinceEntity) {
        ProvinceEntity entity = provinceService.selectOne(provinceEntity.getId());
        return ResultUtils.success(entity);
    }
}
