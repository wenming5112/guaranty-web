package com.example.guaranty.controller.business;

import com.example.guaranty.annotation.RequestLimit;
import com.example.guaranty.annotation.RequireAuth;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.LoanApplicationDTO;
import com.example.guaranty.service.business.LoanApplicationService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 贷款申请表 控制层
 *
 * @author ming
 * @version 1.0.0
 * @date 2020-11-26 14:13:11
 **/
@RestController
@RequestMapping("loanApplication")
@Api(tags = "贷款申请接口")
public class LoanApplicationController {

    @Resource
    private LoanApplicationService loanApplicationService;

    @PostMapping(value = "add")
    @RequestLimit(count = 1, time = 5000)
    @ApiOperation(value = "新增贷款申请", notes = "新增贷款申请(user可操作)", produces = "application/json")
    @RequireAuth()
    public ApiResult add(@ModelAttribute @Validated LoanApplicationDTO loanApplicationDTO) throws BusinessException {
        return loanApplicationService.createLoanApplication(loanApplicationDTO);
    }

    @PostMapping(value = "approval")
    @ApiOperation(value = "审批贷款申请", notes = "审批贷款申请(bank-staff可操作)", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loanApplicationId", value = "贷款申请ID", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "operationCode", value = "操作码(1:同意，2,:拒绝)", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "reason", value = "分页大小", dataType = "string", paramType = "query")
    })
    @RequireAuth(roleName = {"bank-staff"})
    public ApiResult approval(@RequestParam(name = "loanApplicationId") Integer loanApplicationId,
                              @RequestParam(name = "operationCode") Integer operationCode,
                              @RequestParam(name = "reason", required = false) String reason) throws Exception {
        return loanApplicationService.loanApplicationApproval(loanApplicationId, operationCode, reason);
    }

    @PostMapping(value = "upload", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "上传贷款合同", notes = "上传贷款合同(bank-staff使用)", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loanApplicationId", value = "贷款申请ID", dataType = "int", paramType = "query", required = true)

    })
    @RequireAuth(roleName = {"bank-staff"})
    @ResponseStatus(code = HttpStatus.CREATED)
    public ApiResult upload(@RequestParam(name = "loanApplicationId") Integer loanApplicationId,
                            @RequestParam(name = "file") @ApiParam(value = "文件", name = "file", type = "file", required = true) MultipartFile file) throws Exception {
        return loanApplicationService.uploadMaterialFile(loanApplicationId, file);
    }

    @GetMapping(value = "list")
    @ApiOperation(value = "查询贷款申请", notes = "查询贷款申请(bank-staff可操作)", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "申请人用户名(用户名,后面改成真实姓名)", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "realName", value = "申请人姓名(真实姓名)", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "idNo", value = "申请人身份证", dataType = "idNo", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "操作码(-1:待审批，1:同意，0:拒绝)", dataType = "int", paramType = "query", defaultValue = "-1", allowableValues = "-1,1,0"),
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "string", paramType = "query", defaultValue = "10", allowableValues = "10,20,40")
    })
    @RequireAuth(roleName = {"bank-staff"})
    public ApiResult get(@RequestParam(name = "username", required = false) String username,
                         @RequestParam(name = "realName", required = false) String realName,
                         @RequestParam(name = "idNo", required = false) String idNo,
                         @RequestParam(name = "status", required = false) Integer status,
                         @RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Long current,
                         @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Long size) throws BusinessException {
        return loanApplicationService.getLoanApplication(username, realName, idNo, status, current, size);
    }

    @GetMapping(value = "list-my")
    @ApiOperation(value = "查询贷款申请", notes = "查询贷款申请(user可操作)", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankName", value = "银行名称(全称)", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "操作码(-1:待审批，1:同意，0:拒绝)", dataType = "int", paramType = "query", defaultValue = "-1", allowableValues = "-1,1,0"),
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "string", paramType = "query", defaultValue = "10", allowableValues = "10,20,40")
    })
    @RequireAuth()
    public ApiResult getMy(@RequestParam(name = "bankName", required = false) String bankName,
                           @RequestParam(name = "status", required = false) Integer status,
                           @RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Long current,
                           @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Long size) throws BusinessException {
        return loanApplicationService.getLoanMyApplication(bankName, status, current, size);
    }

    @GetMapping(value = "get-user-material")
    @ApiOperation(value = "查询用户抵押资料", notes = "查询用户抵押资料(user可操作)", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loanApplicationId", value = "贷款申请ID", dataType = "int", paramType = "query", required = true)
    })
    @RequireAuth(roleName = {"bank-staff"})
    public ApiResult getUserMortgageMaterial(@RequestParam(name = "loanApplicationId") Integer loanApplicationId) throws BusinessException {
        return loanApplicationService.getUserMortgageMaterial(loanApplicationId);
    }

}
