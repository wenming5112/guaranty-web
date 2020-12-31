package com.example.guaranty.controller.business;

import com.example.guaranty.annotation.RequestLimit;
import com.example.guaranty.annotation.RequireAuth;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.BankSettlementDTO;
import com.example.guaranty.service.business.BankSettlementService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 银行入驻申请 控制层
 *
 * @author ming
 * @version 1.0.0
 * @date 2020-11-26 17:21:02
 **/
@RestController
@RequestMapping("settlement")
@Api(tags = "银行入驻申请管理")
@CrossOrigin
public class BankSettlementController {

    @Resource
    private BankSettlementService bankSettlementService;

    @PostMapping(value = "create")
    @RequestLimit(count = 6, time = 60000)
    @ApiOperation(value = "提交入驻申请", notes = "提交入驻申请", produces = "application/json")
    @RequireAuth()
    public ApiResult createSettlementApplication(@ModelAttribute @Validated BankSettlementDTO bankSettlementDTO) throws BusinessException {
        return bankSettlementService.createSettlementApplication(bankSettlementDTO);
    }

    @PostMapping(value = "approval")
    @RequestLimit(count = 1)
    @ApiOperation(value = "入驻审批", notes = "入驻审批", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "settlementId", value = "入驻ID", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "operationCode", value = "操作码(1:同意，0:不同意)", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "reason", value = "原因(不通过的时候需要填写)", dataType = "string", paramType = "query"),
    })
    @RequireAuth(roleName = {"admin"})
    public ApiResult settlementApproval(@RequestParam(name = "settlementId") Integer settlementId,
                                        @RequestParam(name = "operationCode") Integer operationCode,
                                        @RequestParam(name = "reason") String reason) throws BusinessException {
        return bankSettlementService.settlementApproval(settlementId, operationCode, reason);
    }

    @GetMapping(value = "list")
    @ApiOperation(value = "查询入驻申请列表", notes = "入驻列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态(-1:未通过,0:待审批,1:通过", dataType = "string", paramType = "query", defaultValue = "0", allowableValues = "-1,0,1"),
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "string", paramType = "query", defaultValue = "10", allowableValues = "10,20,40"),
    })
    @RequireAuth(roleName = {"admin"})
    public ApiResult getList(@RequestParam(name = "status", required = false) Integer status,
                             @RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Long current,
                             @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Long size) throws BusinessException {
        return bankSettlementService.getList(status, current, size);
    }

    @GetMapping(value = "my")
    @ApiOperation(value = "查询我的入驻申请列表", notes = "我的入驻列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态(-1:未通过,0:待审批,1:通过)", dataType = "string", paramType = "query", defaultValue = "0", allowableValues = "-1,0,1"),
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "string", paramType = "query", defaultValue = "10", allowableValues = "10,20,40"),
    })
    @RequireAuth(roleName = {"bank-admin"})
    public ApiResult getMyList(@RequestParam(name = "status", required = false) Integer status,
                               @RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Long current,
                               @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Long size) throws BusinessException {
        return bankSettlementService.getMyList(status, current, size);
    }


}
