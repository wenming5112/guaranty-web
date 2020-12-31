package com.example.guaranty.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.guaranty.entity.business.BackstageRole;
import com.example.guaranty.entity.business.BackstageRole;
import com.example.guaranty.vo.business.BackMenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色 mapper
 *
 * @author ming
 * @date 2020/04/16
 */

@Mapper
public interface BackstageRoleMapper extends BaseMapper<BackstageRole> {

    /**
     * 根据用户名查询菜单列表
     *
     * @param username 用户名
     * @return list
     */
    List<BackstageRole> selectByUsername(String username);

    /**
     * 根据菜单查询可访问的角色
     *
     * @param menuId 菜单id
     * @return 角色列表
     */
    List<BackstageRole> queryRolesByMenu(Integer menuId);


    /**
     * 查询角色菜单
     *
     * @param roleId 角色id
     * @return list
     */
    List<BackMenuVO> getRoleMenu(Integer roleId);

    /**
     * 设置角色菜单
     *
     * @param roleId   角色id
     * @param menus    菜单id列表
     * @param operator 操作人
     * @return int
     */
    int setRoleMenu(Integer roleId, List<Integer> menus, String operator);

    /**
     * 获取角色已拥有的菜单
     *
     * @param roleId 角色id
     * @return list
     */
    Integer[] getRoleMenuIds(Integer roleId);

}
