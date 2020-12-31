package com.example.guaranty.controller.business;

import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.BackRoleDTO;
import com.example.guaranty.service.business.BackstageRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * 后台角色管理模块
 *
 * @author ming
 * @date 2020/11/11 11:11
 */
@RestController
@RequestMapping("role")
@Api(value = "后台角色管理", tags = "后台角色管理")
@CrossOrigin
public class RoleController {

    @Resource
    private BackstageRoleService roleService;

//    @GetMapping()
//    @ApiOperation(value = "角色列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "int", paramType = "query", defaultValue = "1"),
//            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "int", paramType = "query", defaultValue = "10", example = "10", allowableValues = "10,20,40,60")
//    })
//    @ApiIgnore
//    public ApiResult get(@RequestParam(name = "current", required = false, defaultValue = WebConstant.PAGE_NO) Integer current,
//                         @RequestParam(name = "size", required = false, defaultValue = WebConstant.PAGE_SIZE) Integer size) {
//        return roleService.roleList(current, size);
//    }
//
//    @PostMapping()
//    @ApiOperation(value = "添加角色")
//    @ApiIgnore
//    public ApiResult add(@ModelAttribute @Validated BackRoleDTO backRoleDTO) throws Exception {
//        return roleService.addRole(backRoleDTO);
//    }
//
//    @DeleteMapping("/{roleId}")
//    @ApiOperation(value = "删除角色")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "int", paramType = "query")
//    })
//    @ApiIgnore
//    public ApiResult delete(@PathVariable("roleId") Integer roleId) throws Exception {
//        return roleService.deleteRole(roleId);
//    }
//
//    @PutMapping()
//    @ApiOperation(value = "修改角色")
//    @ApiIgnore
//    public ApiResult update(@ModelAttribute @Validated BackRoleDTO backRoleDTO) throws Exception {
//        return roleService.updateRole(backRoleDTO);
//    }
//
//    @GetMapping("/menu/{roleId}")
//    @ApiOperation(value = "获取角色权限", notes = "获取角色的菜单")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "int", paramType = "path", required = true)
//    })
//    @ApiIgnore
//    public ApiResult getRoleMenu(@PathVariable("roleId") Integer roleId) {
//        return roleService.getRoleMenu(roleId);
//    }
//
//    @PutMapping("/menu/{roleId}")
//    @ApiOperation(value = "修改角色权限", notes = "修改角色权限")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "int", paramType = "path", required = true),
//            @ApiImplicitParam(name = "menus", value = "菜单ids", dataType = "int", allowMultiple = true, paramType = "query", required = true)
//    })
//    @ApiIgnore
//    public ApiResult addRoleMenu(@PathVariable("roleId") Integer roleId,
//                                 @RequestParam(name = "menus") Integer[] menus) throws Exception {
//        return roleService.setRoleMenu(roleId, menus).getData() ? ApiResult.successOf("设置菜单成功") : ApiResult.failOf("设置菜单失败");
//    }

}
