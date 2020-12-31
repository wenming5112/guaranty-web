package com.example.guaranty.controller.business;

import com.example.guaranty.annotation.RequireAuth;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.MortgageMaterialDTO;
import com.example.guaranty.service.business.UserMortgageMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 用户抵押资料 控制层
 *
 * @author ming
 * @version 1.0.0
 * @date 2020-11-30 17:48:20
 **/
@RestController
@RequestMapping("userMortgageMaterial")
@Api(tags = "用户抵押资料管理")
@CrossOrigin
public class UserMortgageMaterialController {

    @Resource
    private UserMortgageMaterialService userMortgageMaterialService;

    @PostMapping(value = "upload", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "抵押资料文件上传", notes = "抵押资料文件上传", produces = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    @RequireAuth
    public ApiResult upload(@RequestParam(name = "file") @ApiParam(value = "文件", name = "file", type = "file", required = true) MultipartFile file) throws Exception {
        return userMortgageMaterialService.upload(file);
    }

    @PostMapping(value = "add")
    @ApiOperation(value = "添加抵押资料", notes = "添加抵押资料", produces = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    @RequireAuth
    public ApiResult addMortgageMaterial(@ModelAttribute @Validated MortgageMaterialDTO mortgageMaterialDTO) throws Exception {
        return userMortgageMaterialService.addMortgageMaterial(mortgageMaterialDTO);
    }

    @GetMapping(value = "list-my")
    @ApiOperation(value = "查询我的抵押资料", notes = "查询我的抵押资料", produces = "application/json")
    @RequireAuth()
    public ApiResult getList() throws BusinessException {
        return userMortgageMaterialService.getMortgageMaterial();
    }

}
