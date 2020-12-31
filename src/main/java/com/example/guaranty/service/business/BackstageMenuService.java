package com.example.guaranty.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.BackMenuDTO;
import com.example.guaranty.entity.TreeNode;
import com.example.guaranty.entity.business.BackstageMenu;
import com.example.guaranty.vo.business.BackMenuVO;

import java.util.List;
import java.util.Set;

/**
 * @author ming
 * @date 2019:08:16 16:09
 */
public interface BackstageMenuService extends IService<BackstageMenu> {

    /**
     * 是否已存在权限文案
     *
     * @param permission 权限
     * @return true or false
     * @author wzh
     * @date 2019/9/18 0018 19:39
     */
    ApiResult isExistPermission(String permission);

    /**
     * 根据用户名查询其角色下的访问菜单列表
     *
     * @param username 用户名
     * @return list menu
     */
    ApiResult<List<BackstageMenu>> selectByUsername(String username);

    /**
     * 菜单列表
     *
     * @return list
     */
    ApiResult<List<TreeNode<BackMenuVO>>> menuList();

    /**
     * 获取菜单树
     *
     * @param list 菜单列表
     * @return 树形菜单
     */
    List<TreeNode<BackMenuVO>> getMenuTree(List<BackMenuVO> list);

    /**
     * 添加菜单
     *
     * @param backMenuDTO 菜单dto
     * @return json
     * @throws BusinessException e
     */
    ApiResult addMenu(BackMenuDTO backMenuDTO) throws BusinessException;

    /**
     * 修改菜单
     *
     * @param backMenuDTO 菜单dto
     * @return json
     * @throws BusinessException e
     */
    ApiResult updateMenu(BackMenuDTO backMenuDTO) throws BusinessException;

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return json
     * @throws BusinessException e
     */
    ApiResult deleteMenu(Integer menuId) throws BusinessException;

    /**
     * 获取用户的菜单
     *
     * @param userId 用户id
     * @return list
     */
    ApiResult<Set<BackMenuVO>> getUserMenu(Integer userId);
}
