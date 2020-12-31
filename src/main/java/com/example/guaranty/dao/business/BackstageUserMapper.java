package com.example.guaranty.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.guaranty.entity.business.BackstageUser;
import com.example.guaranty.entity.business.BackstageUser;
import com.example.guaranty.vo.business.BackMenuVO;
import com.example.guaranty.vo.business.BackRoleVO;
import com.example.guaranty.vo.business.UserInfoVO;
import com.example.guaranty.vo.business.UserListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 mapper
 *
 * @author ming
 * @date 2020/04/16
 */

@Mapper
public interface BackstageUserMapper extends BaseMapper<BackstageUser> {

    /**
     * 设置用户角色
     *
     * @param userId 用户id
     * @param roles  角色id列表
     * @return int
     */
    int setUserRoles(@Param("userId") Integer userId, @Param("roles") Integer[] roles);

    /**
     * 获取用户角色列表
     *
     * @param userId 用户id
     * @return list
     */
    List<BackRoleVO> getUserRole(Integer userId);

    /**
     * 添加后台用户
     *
     * @param user user entity
     * @return entity
     */
    int addBackUser(BackstageUser user);

    /**
     * 获取用户的角色id列表
     *
     * @param userId 用户id
     * @return int
     */
    List<Integer> getUserRoleIds(Integer userId);

    /**
     * 查询用户所有角色以及权限
     *
     * @param userId user ID
     * @return lsi
     */
    List<BackRoleVO> selectUserAllRoles(@Param("userId") Integer userId);

    /**
     * 查询角色菜单
     *
     * @param id 角色 ID
     * @return BackMenuVO
     */
    BackMenuVO getRolesMenus(Integer id);

    /**
     * 查询用户信息
     *
     * @param userId 用户ID
     * @return UserInfoVO
     */
    UserInfoVO selectMyUserInfo(Integer userId);

    /**
     * 根据角色查询用户
     *
     * @param page   page
     * @param roleId 角色
     * @param bankId 银行ID
     * @return BackstageUser
     */
    Page<BackstageUser> getUserByRole(Page page, @Param("roleId") Integer roleId, @Param("bankId") Integer bankId);

    /**
     * 查询所有的用户
     *
     * @param page      page
     * @param userName  userName
     * @param telephone telephone
     * @param email     email
     * @return list
     */
    Page<UserListVO> selectAllUsers(Page page, @Param("userName") String userName, @Param("telephone") String telephone, @Param("email") String email);
}
