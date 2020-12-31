package com.example.guaranty.service.business.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.common.utils.TreeUtil;
import com.example.guaranty.dao.business.BackstageMenuMapper;
import com.example.guaranty.dto.business.BackMenuDTO;
import com.example.guaranty.entity.TreeNode;
import com.example.guaranty.entity.business.BackstageMenu;
import com.example.guaranty.service.business.BackstageMenuService;
import com.example.guaranty.shiro.JwtUtil;
import com.example.guaranty.vo.business.BackMenuVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author ming
 * @date 2019:08:16 16:21
 */
@Service
public class BackstageMenuServiceImpl extends ServiceImpl<BackstageMenuMapper, BackstageMenu> implements BackstageMenuService {

    @Resource
    private BackstageMenuMapper menuMapper;

    @Override
    public ApiResult<Boolean> isExistPermission(String permission) {
        if (StringUtils.isNotBlank(permission)) {
            BackstageMenu menu = new BackstageMenu();
            menu.setPermission(permission);
            menu.setValid(true);
            return ApiResult.successOf(menuMapper.selectCount(new QueryWrapper<>(menu)) > 0);
        }
        return ApiResult.successOf(false);
    }

    @Override
    public ApiResult<List<BackstageMenu>> selectByUsername(String username) {
        return ApiResult.successOf(menuMapper.selectByUsername(username));
    }

    @Override
    public ApiResult<List<TreeNode<BackMenuVO>>> menuList() {
        BackstageMenu menu = new BackstageMenu();
        menu.setValid(true);
        List<BackstageMenu> list = menuMapper.selectList(new QueryWrapper<>(menu));
        return ApiResult.successOf(getMenuTree(entity2Vo(list)));
    }

    private List<BackMenuVO> entity2Vo(List<BackstageMenu> list) {
        List<BackMenuVO> vos = new ArrayList<>();
        list.forEach(item -> {
            BackMenuVO menuVO = new BackMenuVO();
            menuVO.setComponent(item.getComponent())
                    .setMenuId(item.getId())
                    .setIcon(item.getIcon())
                    .setPath(item.getPath())
                    .setDescription(item.getDescription())
                    .setPermission(item.getPermission())
                    .setPid(item.getParentId())
                    .setTitle(item.getTitle())
                    .setType(item.getType())
            ;
            vos.add(menuVO);
        });
        return vos;
    }

//    @Override
//    public List<TreeNode<BackMenuVO>> getMenuTree(List<BackstageMenu> list) {
//        List<TreeNode<BackMenuVO>> treeNodeList = new ArrayList<>();
//        list.forEach(item -> {
//            TreeNode<BackMenuVO> treeNode = new TreeNode<>();
//            treeNode.setMenuId(item.getId());
//            treeNode.setUrl(item.getUrl());
//            treeNode.setIcon(item.getIcon());
//            treeNode.setTitle(item.getTitle());
//            treeNode.setComponent(item.getComponent());
//            treeNode.setPath(item.getPath());
//            treeNode.setType(item.getType());
//            treeNode.setPid(item.getParentId());
//            treeNode.setPermission(item.getPermission());
//            treeNode.setDescription(item.getDescription());
//            treeNode.setCreateTime(item.getCreateTime());
//            treeNodeList.add(treeNode);
//        });
//        return TreeUtil.build(treeNodeList);
//    }

    @Override
    public List<TreeNode<BackMenuVO>> getMenuTree(List<BackMenuVO> list) {
        List<TreeNode<BackMenuVO>> treeNodeList = new ArrayList<>();
        list.forEach(item -> {
            TreeNode<BackMenuVO> treeNode = new TreeNode<>();
            treeNode.setMenuId(item.getMenuId());
            treeNode.setUrl(item.getUrl());
            treeNode.setIcon(item.getIcon());
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
        return TreeUtil.build(treeNodeList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addMenu(BackMenuDTO backMenuDTO) throws BusinessException {
        BackstageMenu menu = dtoToEntity(backMenuDTO);
        Assert.isTrue(!ObjectUtils.isEmpty(menu), "参数为空");
        try {
            if (backMenuDTO.getType() == 0) {
                // 如果是菜单 ，权限可以为空
                if (StringUtils.isBlank(backMenuDTO.getPermission())) {
                    throw new BusinessException("按钮权限不能为空!!");
                }
            }
            // 防止添加重复的权限
            if (this.isExistPermission(Objects.requireNonNull(menu).getPermission()).getData()) {
                return ApiResult.failOf(menu.getPermission() + "权限permission已存在");
            }
            menu.setCreator(JwtUtil.getUserNameFromRedis());
            menu.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            return this.save(menu) ? ApiResult.successOf("添加成功") : ApiResult.failOf("添加失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateMenu(BackMenuDTO backMenuDTO) throws BusinessException {
        Assert.isTrue(!ObjectUtils.isEmpty(backMenuDTO), "参数为空");
        Assert.isTrue(backMenuDTO.getMenuId() != null, "菜单id不能为空");
        BackstageMenu dbMenu = menuMapper.selectById(backMenuDTO.getMenuId());
        Assert.isTrue(!ObjectUtils.isEmpty(dbMenu), "要修改的菜单不存在");
        dbMenu.setType(backMenuDTO.getType());
        dbMenu.setIcon(backMenuDTO.getIcon());
        dbMenu.setComponent(backMenuDTO.getComponent());
        dbMenu.setPath(backMenuDTO.getPath());
        dbMenu.setType(backMenuDTO.getType());
        dbMenu.setTitle(backMenuDTO.getTitle());
        dbMenu.setParentId(backMenuDTO.getPid());
        dbMenu.setPermission(backMenuDTO.getPermission());
        dbMenu.setDescription(backMenuDTO.getDescription());
        dbMenu.setModifier(JwtUtil.getUserNameFromRedis());
        dbMenu.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        return this.updateById(dbMenu) ? ApiResult.successOf("修改成功") : ApiResult.failOf("修改失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteMenu(Integer menuId) throws BusinessException {
        BackstageMenu menu = menuMapper.selectById(menuId);
        Assert.isTrue(!ObjectUtils.isEmpty(menu), "找不到菜单");
        List<BackstageMenu> childrenList = menuMapper.selectMenuByPid(menu.getId());
        Assert.isTrue(childrenList.size() == 0, "该菜单还有子节点,不能删除菜单");
        menu.setValid(false);
        menu.setId(menuId);
        menu.setModifier(JwtUtil.getUserNameFromRedis());
        menu.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        return this.updateById(menu) ? ApiResult.successOf("删除成功") : ApiResult.failOf("删除失败");
    }

    @Override
    public ApiResult<Set<BackMenuVO>> getUserMenu(Integer userId) {
        Set<BackMenuVO> list = menuMapper.getUserMenu(userId);
        return ApiResult.successOf(list);
    }

    private <T> BackstageMenu dtoToEntity(T dto) {
        BackstageMenu backstageMenu = new BackstageMenu();
        if (dto instanceof BackMenuDTO) {
            BackMenuDTO menuDTO = (BackMenuDTO) dto;
            backstageMenu.setUrl(menuDTO.getUrl());
            backstageMenu.setTitle(menuDTO.getTitle());
            backstageMenu.setComponent(menuDTO.getComponent());
            backstageMenu.setPath(menuDTO.getPath());
            backstageMenu.setIcon(menuDTO.getIcon());
            backstageMenu.setType(menuDTO.getType());
            backstageMenu.setPermission(menuDTO.getPermission());
            backstageMenu.setParentId(menuDTO.getPid());
            return backstageMenu;
        } else {
            return null;
        }
    }
}
