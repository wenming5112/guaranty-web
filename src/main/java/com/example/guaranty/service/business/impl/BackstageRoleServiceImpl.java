package com.example.guaranty.service.business.impl;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.common.utils.TreeUtil;
import com.example.guaranty.dao.business.BackstageRoleMapper;
import com.example.guaranty.dao.business.BackstageRoleMenuMapper;
import com.example.guaranty.dto.business.BackRoleDTO;
import com.example.guaranty.entity.TreeNode;
import com.example.guaranty.entity.business.BackstageRole;
import com.example.guaranty.entity.business.BackstageRoleMenu;
import com.example.guaranty.service.business.BackstageRoleService;
import com.example.guaranty.shiro.JwtUtil;
import com.example.guaranty.vo.business.BackMenuVO;
import com.example.guaranty.vo.business.BackRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ming
 * @date 2019:08:16 16:20
 */
@Service
public class BackstageRoleServiceImpl extends ServiceImpl<BackstageRoleMapper, BackstageRole> implements BackstageRoleService {

    @Resource
    private BackstageRoleMapper roleMapper;

    @Resource
    private BackstageRoleMenuMapper backstageRoleMenuMapper;

    @Override
    public ApiResult<List<BackstageRole>> selectByUsername(String username) {
        return ApiResult.successOf(roleMapper.selectByUsername(username));
    }

    @Override
    public ApiResult<List<BackstageRole>> queryRolesByMenu(Integer menuId) {
        return ApiResult.successOf(roleMapper.queryRolesByMenu(menuId));
    }

    /**
     * 获取角色菜单
     *
     * @param roleId 角色id
     * @return ApiResult
     */
    @Override
    public ApiResult getRoleMenu(Integer roleId) {
        List<BackMenuVO> list = roleMapper.getRoleMenu(roleId);
        List<TreeNode<BackMenuVO>> treeNodeList = new ArrayList<>();
        list.forEach(item -> {
            TreeNode<BackMenuVO> treeNode = new TreeNode<>();
            treeNode.setMenuId(item.getMenuId());
            treeNode.setUrl(item.getUrl());
            treeNode.setTitle(item.getTitle());
            treeNode.setComponent(item.getComponent());
            treeNode.setPath(item.getPath());
            treeNode.setType(item.getType());
            treeNode.setPid(item.getPid());
            treeNode.setPermission(item.getPermission());
            treeNode.setDescription(item.getDescription());
            treeNode.setCreateTime(item.getCreateTime());
            treeNodeList.add(treeNode);
        });
        List<TreeNode<BackMenuVO>> resultList = TreeUtil.build(treeNodeList);
        return ApiResult.successOf(resultList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> setRoleMenu(Integer roleId, Integer[] menus) throws BusinessException {
        try {
            BackstageRole role = roleMapper.selectById(roleId);
            if (ObjectUtils.isEmpty(role)) {
                throw new BusinessException("角色不存在!!");
            }
            // 删除所有的菜单
            BackstageRoleMenu backstageRoleMenu = new BackstageRoleMenu();
            backstageRoleMenu.setBackRoleId(roleId);
            backstageRoleMenu.setValid(true);
            int i = backstageRoleMenuMapper.delete(new QueryWrapper<>(backstageRoleMenu));
            if (menus.length == 0) {
                return ApiResult.successOf(i > 0);
            }
            // 在添加
            List<Integer> newList = new ArrayList<>(Arrays.asList(menus));
//            // db
//            Integer[] menuIds = roleMapper.getRoleMenuIds(roleId);
//            List<Integer> newList = IntegerUtil.listToRepeat(Arrays.asList(menuIds), Arrays.asList(menus));
//            // 帅选完之后 如果集合为空  则不添加 直接返回成功
//            if (newList.size() == 0) {
//                return true;
//            }
//            String operator = UserUtil.getUsername();
//            FabricAssert.judge(roleMapper.selectById(roleId) != null, "角色不存在");
            return ApiResult.successOf(roleMapper.setRoleMenu(roleId, newList, "测试用户") > 0);
        } catch (Exception e) {
            throw new BusinessException(e);
        }

    }

    @Override
    public ApiResult roleList(Integer current, Integer size) {
        Page<BackstageRole> page = baseMapper.selectPage(new Page<>(), new QueryWrapper<BackstageRole>().lambda().eq(BackstageRole::getValid, true));
        List<BackRoleVO> backRoleVoList = new ArrayList<>();
        page.getRecords().forEach(item -> {
            BackRoleVO backRoleVO = new BackRoleVO();
            backRoleVO.setRoleId(item.getId());
            backRoleVO.setRoleName(item.getRoleName());
            backRoleVO.setRoleDesc(item.getRoleDesc());
            backRoleVO.setCreateTime(item.getCreateTime());
            backRoleVO.setModifyTime(item.getModifyTime());
            backRoleVoList.add(backRoleVO);
        });

        Page<BackRoleVO> page1 = new Page<>();
        page1.setRecords(backRoleVoList);
        page1.setTotal(page.getTotal());
        page1.setCurrent(page.getCurrent());
        page1.setOrders(page.getOrders());
        page1.setPages(page.getPages());
        page1.setSize(page.getSize());

        return ApiResult.successOf(page1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addRole(BackRoleDTO backRoleDTO) throws BusinessException {
        BackstageRole role = dtoToEntity(backRoleDTO);
        role.setCreator(JwtUtil.getUserNameFromRedis());
        role.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        return this.save(role) ? ApiResult.successOf("添加成功") : ApiResult.failOf("添加失败");
    }

    @Override
    public ApiResult deleteRole(Integer roleId) throws BusinessException {
        BackstageRole role = new BackstageRole();
        role.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        role.setModifier(JwtUtil.getUserNameFromRedis());
        role.setId(roleId);
        role.setValid(false);
        return this.updateById(role) ? ApiResult.successOf("删除成功") : ApiResult.failOf("删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateRole(BackRoleDTO backRoleDTO) throws BusinessException {
        BackstageRole role = dtoToEntity(backRoleDTO);
        role.setModifier(JwtUtil.getUserNameFromRedis());
        role.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        return this.updateById(role) ? ApiResult.successOf("修改成功") : ApiResult.failOf("修改失败");
    }

    private BackstageRole dtoToEntity(BackRoleDTO dto) {
        BackstageRole role = new BackstageRole();
        role.setRoleName(dto.getRoleName());
        role.setId(dto.getRoleId());
        role.setRoleDesc(dto.getRoleDesc());
        return role;
    }

}
