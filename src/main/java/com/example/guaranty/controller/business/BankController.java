package com.example.guaranty.controller.business;

import com.example.guaranty.annotation.RequireAuth;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.service.business.BankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * 银行信息表 控制层
 *
 * @author ming
 * @version 1.0.0
 * @date 2020-11-26 14:13:11
 **/
@RestController
@RequestMapping("bank")
@Api(tags = "银行信息表管理")
@CrossOrigin
public class BankController {

    @Resource
    private BankService bankService;

    @GetMapping(value = "list")
    @ApiOperation(value = "查询银行信息列表", notes = "银行信息(管理员)", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "string", paramType = "query", defaultValue = "10", allowableValues = "10,20,40"),
    })
    @RequireAuth(roleName = {"admin"})
    public ApiResult getList(@RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Long current,
                             @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Long size) throws BusinessException {
        return bankService.getList(current, size);
    }

    @GetMapping(value = "all")
    @ApiOperation(value = "查询所有银行", notes = "所有银行", produces = "application/json")
    @RequireAuth(roleName = {"user", "admin", "bank-admin", "bank-staff"})
    @ApiIgnore
    public ApiResult getAll() throws BusinessException {
        return bankService.getAll();
    }

}
