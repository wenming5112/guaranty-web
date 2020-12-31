package com.example.guaranty.controller.business;

import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.BackMenuDTO;
import com.example.guaranty.service.business.BackstageMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * 后台菜单管理模块
 *
 * @author ming
 * @date 2019:08:16 17:31
 */
@Api(value = "后台菜单管理", tags = "后台菜单管理")
@RestController
@RequestMapping("menu")
@CrossOrigin
public class MenuController {

    @Resource
    private BackstageMenuService menuService;
//
//    @GetMapping()
//    @ApiOperation(value = "菜单列表")
//    @ApiIgnore
//    public ApiResult menuList() {
//        return menuService.menuList();
//    }
//
//    @ApiOperation(value = "添加菜单")
//    @PostMapping()
//    @ApiIgnore
//    public ApiResult addMenu(@ModelAttribute @Validated BackMenuDTO backMenuDTO) throws Exception {
//        return menuService.addMenu(backMenuDTO);
//    }
//
//    @ApiOperation(value = "修改菜单")
//    @PutMapping()
//    @ApiIgnore
//    public ApiResult updateMenu(@ModelAttribute @Validated BackMenuDTO backMenuDTO) throws Exception {
//        return menuService.updateMenu(backMenuDTO);
//    }
//
//    @DeleteMapping("/{menuId}")
//    @ApiOperation(value = "删除菜单")
//    @ApiIgnore
//    public ApiResult deleteMenu(@PathVariable(value = "menuId") Integer menuId) throws Exception {
//        return menuService.deleteMenu(menuId);
//    }
}
