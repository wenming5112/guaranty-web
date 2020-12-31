package com.example.guaranty.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.config.redis.RedisCache;
import com.example.guaranty.entity.business.BackstageUser;
import com.example.guaranty.service.business.BackstageUserService;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.config.redis.RedisCache;
import com.example.guaranty.entity.business.BackstageUser;
import com.example.guaranty.service.business.BackstageUserService;
import com.example.guaranty.vo.business.BackMenuVO;
import com.example.guaranty.vo.business.BackRoleVO;
import com.example.guaranty.vo.business.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ming
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private BackstageUserService userService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WebConstant webConstant;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserInfoVO userInfo;
        try {
            // 从缓存中获取用户信息
            userInfo = JwtUtil.getUserByRedisToken(principals.toString());
        } catch (BusinessException e) {
            return new SimpleAuthorizationInfo();
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 角色
        List<BackRoleVO> roleList = userInfo.getRoles();
        Set<String> roles = roleList.stream().map(BackRoleVO::getRoleName).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roles);
        // 权限
        List<BackMenuVO> resourceList = userInfo.getMenus();
        Set<String> resources = resourceList.stream().map(BackMenuVO::getPermission).collect(Collectors.toSet());
        simpleAuthorizationInfo.setStringPermissions(resources);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String redisToken = (String) auth.getCredentials();
        UserInfoVO userInfo;
        try {
            userInfo = JwtUtil.getUserByRedisToken(redisToken);
        } catch (BusinessException e) {
            throw new AuthenticationException("用户状态异常");
        }
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new AuthenticationException("Token已失效");
        }
        BackstageUser user = new BackstageUser();
        user.setUserName(userInfo.getUserName());
        user.setValid(true);
        user = userService.getOne(new QueryWrapper<>(user));
        if (ObjectUtils.isEmpty(user)) {
            throw new AuthenticationException("用户不存在");
        }
        if (!JwtUtil.verify(userInfo.getJwtToken(), user.getUserName(), user.getPassword())) {
            throw new AuthenticationException("账户名或者密码错误");
        }
        // Refresh redis token
        String newJwtToken = JwtUtil.sign(user.getUserName(), user.getPassword(), webConstant.getExpireTime());
        userInfo.setJwtToken(newJwtToken);
        redisCache.set(redisToken, userInfo, webConstant.getExpireTime());
        redisCache.set(user.getUserName(), redisToken);
        return new SimpleAuthenticationInfo(redisToken, redisToken, getName());
    }
}
