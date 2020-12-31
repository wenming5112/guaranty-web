package com.example.guaranty.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.guaranty.entity.business.BackstageMenu;
import com.example.guaranty.entity.business.BackstageMenu;
import com.example.guaranty.vo.business.BackMenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * 菜单 mapper
 *
 * @author ming
 * @date 2020/04/16
 */

@Mapper
public interface BackstageMenuMapper extends BaseMapper<BackstageMenu> {

    /**
     * 根据用户名查询用户菜单列表
     *
     * @param username 用户名
     * @return list
     */
    List<BackstageMenu> selectByUsername(String username);

    /**
     * 获取用户菜单列表
     *
     * @param userId 用户id
     * @return ist
     */
    Set<BackMenuVO> getUserMenu(Integer userId);

    /**
     * 根据父级ID查询菜单
     *
     * @param pid 父级ID
     * @return list
     */
    List<BackstageMenu> selectMenuByPid(Integer pid);

}
