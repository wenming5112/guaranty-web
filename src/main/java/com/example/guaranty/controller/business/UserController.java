package com.example.guaranty.controller.business;

import com.example.guaranty.annotation.RequireAuth;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.AddBankStaffDTO;
import com.example.guaranty.dto.business.BackUserDTO;
import com.example.guaranty.dto.business.UserBaseInfoDTO;
import com.example.guaranty.dto.business.UserRegistryDTO;
import com.example.guaranty.service.business.BackstageUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * 用户模块
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/17
 */
@RestController()
@RequestMapping("user")
@Api(value = "后台用户管理", tags = "后台用户管理")
@CrossOrigin
public class UserController {

    @Resource
    private BackstageUserService userService;

    //    @RequestLimit(count = 1, time = 60000,apiName = "user-registry")
    @PostMapping("registry")
    @ApiOperation(value = "注册", notes = "用户注册")
    public ApiResult registry(@ModelAttribute @Validated UserRegistryDTO registryDTO) throws Exception {
        return userService.registry(registryDTO);
    }

    @PostMapping("add-bank-staff")
    @ApiOperation(value = "新增银行业务员", notes = "新增银行业务员")
    @RequireAuth(roleName = {"bank-admin"})
    public ApiResult addBankStaff(@ModelAttribute @Validated AddBankStaffDTO addBankStaffDTO) throws Exception {
        return userService.addBankStaff(addBankStaffDTO);
    }

    @PostMapping("login")
    @ApiOperation(value = "登录", notes = "用户登录(支持用户名/手机号码/邮箱)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query", required = true, defaultValue = "esbug"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", required = true, defaultValue = "123456")
    })
    public ApiResult login(@RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String password) throws Exception {
        return userService.login(username, password);
    }

    @PostMapping("disable-or-enable-user")
    @ApiOperation(value = "禁用/启用用户", notes = "禁用/启用用户(管理员可操作0:禁用，1:启用)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "operation", value = "密码", dataType = "int", paramType = "query", required = true, defaultValue = "0,1")
    })
    @RequireAuth(roleName = {"admin"})
    public ApiResult disableOrEnableUser(@RequestParam(name = "userId") Integer userId,
                                         @RequestParam(name = "operation") Integer operation) throws Exception {
        return userService.disableOrEnableUser(userId, operation);
    }

    @PostMapping("disable-or-enable-bank-staff")
    @ApiOperation(value = "禁用/启用银行业务员", notes = "禁用/启用银行业务员(银行管理员可操作0:禁用，1:启用)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "operation", value = "密码", dataType = "int", paramType = "query", required = true, defaultValue = "0,1")
    })
    @RequireAuth(roleName = {"bank-admin"})
    public ApiResult disableOrEnableBankStaff(@RequestParam(name = "userId") Integer userId,
                                              @RequestParam(name = "operation") Integer operation) throws Exception {
        return userService.disableOrEnableBankStaff(userId, operation);
    }

    @GetMapping(value = "list-bank-staff")
    @ApiOperation(value = "查询银行业务员", notes = "查询银行业务员", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "string", paramType = "query", defaultValue = "10", allowableValues = "10,20,40"),
    })
    @RequireAuth(roleName = {"admin", "bank-admin"})
    public ApiResult getBankStaff(@RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Long current,
                                  @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Long size) throws Exception {
        return userService.getBankStaff(current, size);
    }

    //    @RequestLimit(count = 1, time = 60000)
    @RequireAuth()
    @GetMapping("real-name-auth")
    @ApiOperation(value = "实名认证", notes = "实名认证(已购买100次)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "idNo", value = "身份证号", dataType = "string", paramType = "query"),
    })
    public ApiResult realNameAuth(@RequestParam(name = "name") String name,
                                  @RequestParam(name = "idNo") String idNo) throws Exception {
        return userService.realNameAuth(name, idNo);
    }

    @PostMapping("logout")
    @RequireAuth(roleName = {"admin", "user", "bank-admin", "bank-staff"})
    @ApiOperation(value = "登出", notes = "用户登出")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult logout() throws Exception {
        return userService.logout();
    }

    @PostMapping("password")
    @ApiOperation(value = "根据旧密码修改登录密码", notes = "在已登录的情况下，修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "string", paramType = "query"),
    })
    public ApiResult updateLoginPasswordByOldPassword(@RequestParam(name = "oldPassword") String oldPassword,
                                                      @RequestParam(name = "newPassword") String newPassword) throws Exception {
        return userService.updateLoginPasswordByOldPassword(oldPassword, newPassword);
    }

    // TODO: 2020/12/1 用户贷款贷款申请

    // TODO: 2020/12/1 用户贷款申请审批

    @PutMapping("info")
    @ApiOperation(value = "修改用户信息", notes = "修改后台用户信息(仅限管理员)")
    @ApiIgnore
    @RequireAuth(roleName = {"admin", "bank-admin"})
    public ApiResult update(@RequestBody @Validated BackUserDTO userDTO) throws Exception {
        return userService.updateUser(userDTO);
    }

    @PostMapping("base-info")
    @ApiOperation(value = "用户基本信息修改", notes = "用户基本信息修改(仅普通用户)")
    @RequireAuth()
    public ApiResult updateBaseInfo(@ModelAttribute @Validated UserBaseInfoDTO baseInfoDTO) throws Exception {
        return userService.updateBaseInfo(baseInfoDTO);
    }

//    @PostMapping("/info")
//    @ApiOperation(value = "新增用户", notes = "新增用户")
//    @ApiIgnore
//    public ApiResult add(@ModelAttribute @Validated AddUserDTO addUserDTO) throws Exception {
//        return userService.addUser(addUserDTO);
//    }
//
//    @DeleteMapping("info/{id}")
//    @ApiOperation(value = "删除用户", notes = "删除用户")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "int", paramType = "path")
//    })
//    @ApiIgnore
//    public ApiResult delete(@PathVariable(name = "id") Integer id) throws Exception {
//        return userService.deleteUser(id);
//    }

    @GetMapping("list")
    @ApiOperation(value = "用户列表", notes = "后台用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页显示数", dataType = "int", paramType = "query", defaultValue = "10", example = "10", allowableValues = "10,20,40,60"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "telephone", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "string", paramType = "query"),
    })
    public ApiResult getUserList(@RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Integer current,
                                 @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Integer size,
                                 @RequestParam(name = "userName", required = false, defaultValue = "") String userName,
                                 @RequestParam(name = "telephone", required = false, defaultValue = "") String telephone,
                                 @RequestParam(name = "email", required = false, defaultValue = "") String email) {
        return userService.backUserList(current, size, userName, telephone, email);
    }

//    @PostMapping("password/modification/{userId}")
//    @ApiOperation(value = "修改密码(仅管理员可操作)", notes = "修改密码(仅管理员可操作)")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "string", paramType = "path"),
//            @ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "string", paramType = "query"),
//    })
//    @ApiIgnore
//    public ApiResult changePwd(@PathVariable(name = "userId") String userId,
//                               @RequestParam(name = "newPassword") String newPassword) throws Exception {
//        return userService.changePwd(userId, newPassword);
//    }

//    @RequiresPermissions("user:role:get")
//    @GetMapping("role/{userId}")
//    @ApiOperation(value = "获取用户的角色", notes = "获取用户的角色")
//    public ApiResult getRole(@PathVariable(name = "userId") Integer userId) {
//        return ApiResult.successOf(userService.getUserRole(userId));
//    }

//    @RequiresPermissions("user:role:update")
//    @PostMapping("role/{userId}")
//    @ApiOperation(value = "设置用户角色(不保留原来的角色)", notes = "设置用户角色(不保留原来的角色)")
//    @SysLog(value = "设置用户角色(不保留原来的角色)", type = 1)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "int", paramType = "path", required = true),
//            @ApiImplicitParam(name = "roles", value = "角色ids", dataType = "int", paramType = "query", required = true, allowMultiple = true)
//    })
//    @ApiIgnore
//    public ApiResult updateUserRole(@PathVariable(name = "userId") Integer userId,
//                                    @RequestParam(name = "roles") Integer[] roles) throws Exception {
//        return userService.updateUserRole(userId, roles);
//    }

//    @RequiresPermissions("user:password:forgot")
//    @SysLog(value = "忘记密码")
//    @PostMapping("password/{actionId}")
//    @ApiOperation(value = "根据手机验证码修改登录密码", notes = "可以在未登录的情况下操作")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "actionId", value = "行为ID", dataType = "int", paramType = "path", defaultValue = "1", allowableValues = "1,2,3,4,5,6,7"),
//            @ApiImplicitParam(name = "telephone", value = "手机号码", dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "code", value = "短信验证码", dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "password", value = "新密码", dataType = "string", paramType = "query"),
//    })
//    @ApiIgnore
//    public ApiResult updateLoginPasswordByTelephone(
//            @PathVariable(name = "actionId") Integer actionId,
//            @RequestParam(name = "telephone") String telephone,
//            @RequestParam(name = "code") String code,
//            @RequestParam(name = "password") String password) throws Exception {
//        return userService.updateLoginPasswordByTelephone(telephone, code, password, actionId);
//    }

}
