package com.example.guaranty.controller.business;

import com.example.guaranty.annotation.RequestLimit;
import com.example.guaranty.annotation.RequireAuth;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.LoanProductDTO;
import com.example.guaranty.service.business.LoanProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 贷款产品表 控制层
 *
 * @author ming
 * @version 1.0.0
 * @date 2020-11-29 12:11:50
 **/
@RestController
@RequestMapping("loan-product")
@Api(tags = "贷款产品管理")
@CrossOrigin
public class LoanProductController {

    @Resource
    private LoanProductService loanProductService;

    @PostMapping(value = "add")
    @RequestLimit(count = 1, time = 5000)
    @ApiOperation(value = "新增贷款产品", notes = "新增贷款产品(bank-admin可操作)", produces = "application/json")
    @RequireAuth(roleName = {"bank-admin"})
    public ApiResult add(@ModelAttribute @Validated LoanProductDTO loanProductDTO) throws BusinessException {
        return loanProductService.insertLoanProduct(loanProductDTO);
    }

    @PostMapping(value = "update/{productId}")
    @RequestLimit(count = 1, time = 5000)
    @ApiOperation(value = "修改贷款产品", notes = "修改贷款产品(bank-admin可操作)", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品ID", dataType = "int", paramType = "path", defaultValue = "1"),
    })
    @RequireAuth(roleName = {"bank-admin"})
    public ApiResult add(@PathVariable Integer productId, @ModelAttribute @Validated LoanProductDTO loanProductDTO) throws BusinessException {
        return loanProductService.updateLoanProduct(productId, loanProductDTO);
    }

    @GetMapping(value = "list-my")
    @ApiOperation(value = "查询贷款产品", notes = "查询贷款产品(银行用，仅能查询当前银行的)", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "string", paramType = "query", defaultValue = "10", allowableValues = "10,20,40"),
    })
    @RequireAuth(roleName = {"bank-admin", "bank-staff"})
    public ApiResult getMyList(@RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Long current,
                               @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Long size) throws BusinessException {
        return loanProductService.getLoanProduct(current, size);
    }

    @GetMapping(value = "list")
    @ApiOperation(value = "查询贷款产品", notes = "查询贷款产品", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "string", paramType = "query", defaultValue = "10", allowableValues = "10,20,40"),
    })
    @RequireAuth()
    public ApiResult getList(@RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Long current,
                             @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Long size) throws BusinessException {
        return loanProductService.getAllLoanProduct(current, size);
    }

}
