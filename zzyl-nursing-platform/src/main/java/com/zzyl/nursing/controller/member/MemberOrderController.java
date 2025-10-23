package com.zzyl.nursing.controller.member;

import com.zzyl.common.core.controller.BaseController;
import com.zzyl.common.core.domain.R;
import com.zzyl.common.core.page.TableDataInfo;
import com.zzyl.nursing.domain.NursingProject;
import com.zzyl.nursing.service.INursingProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member/orders")
public class MemberOrderController extends BaseController {
    @Autowired
    private INursingProjectService nursingProjectService;

    @GetMapping("/project/page")
    @ApiOperation("分页查询护理项目列表")
    public TableDataInfo<NursingProject> page(Integer pageNum, Integer pageSize,String name,Integer status){
        return nursingProjectService.pageByNameAndStaus(pageNum,pageSize,name,status);
    }

    @GetMapping("/project/{id}")
    @ApiOperation("查询护理项目详情")
    public R<NursingProject> getInfo(@ApiParam("护理项目ID") @PathVariable Long id){
        NursingProject nursingProject = nursingProjectService.selectNursingProjectById(id);
        return R.ok(nursingProject);
    }
}
