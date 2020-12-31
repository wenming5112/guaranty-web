package com.example.guaranty.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.BackRoleDTO;
import com.example.guaranty.entity.business.BackstageRole;

import java.util.List;

/**
 * @author ming
 * @date 2019:08:16 16:09
 */
public interface BackstageRoleService extends IService<BackstageRole> {


    /**
     * 根据用户名查询用户的角色列表
     *
     * @param username 用户名
     * @return list role
     */
    ApiResult<List<BackstageRole>> selectByUsername(String username);


    /**
     * 根据菜单查询可访问的角色
     *
     * @param menuId 菜单id
     * @return list
     */
    ApiResult<List<BackstageRole>> queryRolesByMenu(Integer menuId);

    /**
     * 获取角色菜单
     *
     * @param roleId 角色id
     * @return json
     */
    ApiResult getRoleMenu(Integer roleId);

    /**
     * 为角色设置访问菜单
     *
     * @param roleId 角色id
     * @param menus  菜单id列表
     * @return json
     * @throws BusinessException e
     */
    ApiResult<Boolean> setRoleMenu(Integer roleId, Integer[] menus) throws BusinessException;

    /**
     * 添加角色
     *
     * @param backRoleDTO 后台角色dto
     * @return json
     * @throws BusinessException e
     */
    ApiResult addRole(BackRoleDTO backRoleDTO) throws BusinessException;


    /**
     * 删除角色
     *
     * @param roleId 角色id
     * @return json
     * @throws BusinessException e
     */
    ApiResult deleteRole(Integer roleId) throws BusinessException;

    /**
     * 修改角色
     *
     * @param backRoleDTO 角色dto
     * @return json
     * @throws BusinessException e
     */
    ApiResult updateRole(BackRoleDTO backRoleDTO) throws BusinessException;

    /**
     * 角色列表
     *
     * @param current 当前页码
     * @param size    分页大小
     * @return json
     */
    ApiResult roleList(Integer current, Integer size);
}
