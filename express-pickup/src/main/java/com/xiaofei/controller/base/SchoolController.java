package com.xiaofei.controller.base;

import com.xiaofei.common.CommonResponse;
import com.xiaofei.common.ResultUtils;
import com.xiaofei.entity.base.SchoolEntity;
import com.xiaofei.service.base.SchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO 类描述
 *
 * @date 2022/4/16 13:05
 */
@Api(tags = "学校信息接口")
@RestController
@RequestMapping("/school")
public class SchoolController {
    @Resource
    private SchoolService schoolService;

    @ApiOperation(value = "查询所有学校信息")
    @PostMapping("/selectAllSchool")
    public CommonResponse<List<SchoolEntity>> selectAllSchool() {
        List<SchoolEntity> entities = schoolService.selectAll();
        return ResultUtils.success(entities);
    }

    @ApiImplicitParam(name = "schoolEntity", value = "学校实体信息", required = true)
    @ApiOperation(value = "查询单个学校信息")
    @PostMapping("/selectOneSchool")
    public CommonResponse<List<SchoolEntity>> selectOneSchool(@RequestBody SchoolEntity schoolEntity) {
        List<SchoolEntity> entity = schoolService.selectList(schoolEntity.getProvinceId());
        return ResultUtils.success(entity);
    }
}
