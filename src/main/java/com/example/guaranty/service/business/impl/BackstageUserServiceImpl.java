package com.example.guaranty.service.business.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.guaranty.common.constant.RegexConstant;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.enumeration.ResponseResultEnum;
import com.example.guaranty.common.enumeration.RoleEnum;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.common.handle.RequestHolder;
import com.example.guaranty.common.utils.CommonUtil;
import com.example.guaranty.common.utils.IpUtil;
import com.example.guaranty.common.utils.security.DigestAlgorithmUtils;
import com.example.guaranty.component.FabricClientManager;
import com.example.guaranty.config.RealNameAuthProperties;
import com.example.guaranty.config.fabric.SdkProperties;
import com.example.guaranty.config.redis.RedisCache;
import com.example.guaranty.dao.business.BackstageRoleMapper;
import com.example.guaranty.dao.business.BackstageUserMapper;
import com.example.guaranty.dao.business.BackstageUserRoleMapper;
import com.example.guaranty.dto.business.*;
import com.example.guaranty.entity.BaseEntity;
import com.example.guaranty.entity.business.BackstageRole;
import com.example.guaranty.entity.business.BackstageUser;
import com.example.guaranty.entity.business.BackstageUserRole;
import com.example.guaranty.service.business.BackstageMenuService;
import com.example.guaranty.service.business.BackstageUserService;
import com.example.guaranty.service.business.CommonService;
import com.example.guaranty.shiro.JwtUtil;
import com.example.guaranty.vo.business.BackRoleVO;
import com.example.guaranty.vo.business.UserInfoVO;
import com.example.guaranty.vo.business.UserListVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * 后台用户
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/17
 */

@Service
public class BackstageUserServiceImpl extends ServiceImpl<BackstageUserMapper, BackstageUser> implements BackstageUserService {

    @Resource
    private BackstageUserMapper userMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private BackstageUserRoleMapper userRoleMapper;

    @Resource
    private BackstageMenuService menuService;

    @Resource
    private BackstageRoleMapper roleMapper;

    @Resource
    private WebConstant webConstant;

    @Resource
    private RealNameAuthProperties authProperties;

    @Resource
    private CommonService commonService;

    @Resource
    private FabricClientManager fabricClientManager;

    @Resource
    private SdkProperties sdkProperties;

    @Resource
    private PlatformTransactionManager platformTransactionManager;

    @Resource
    private TransactionDefinition transactionDefinition;

    private Lock lock = new ReentrantLock();

    /**
     * 这里jwt生成的token是可以解析用户信息的
     * 但jwt不自带刷新token. 所以这里采用redis 和 jwt生成的token 做有效刷新存储
     *
     * @param username 用户名
     * @param password 密码
     * @return json
     * @throws BusinessException e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult login(String username, String password) throws BusinessException {
        try {
            BackstageUser backstageUser = judgeLoginType(username);
            String redisToken;
            String jwtToken;
            // 验证密码
            if (!ObjectUtils.isEmpty(backstageUser) && DigestAlgorithmUtils.md5Verify(password, backstageUser.getPassword())) {
                // 更新登录信息
                backstageUser.setLoginIp(IpUtil.getIp(new RequestHolder().getRequest()));
                backstageUser.setLoginAddress(IpUtil.getCityInfo(backstageUser.getLoginIp()));
                backstageUser.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
                String token = (String) redisCache.get(backstageUser.getUserName());
                if (!StringUtils.isEmpty(token)) {
                    UserInfoVO user = (UserInfoVO) redisCache.get(token);
                    if (!ObjectUtils.isEmpty(user)) {
                        user.setLoginIp(backstageUser.getLoginIp());
                        user.setLoginAddress(backstageUser.getLoginAddress());
                        // 更新jwt token
                        jwtToken = JwtUtil.sign(backstageUser.getUserName(), backstageUser.getPassword(), webConstant.getExpireTime());
                        user.setJwtToken(jwtToken);
                        redisCache.set(token, user, webConstant.getExpireTime());
                        redisCache.set(backstageUser.getUserName(), token, webConstant.getExpireTime());
                        userMapper.update(backstageUser, new UpdateWrapper<>(backstageUser));
                        return ApiResult.successOf(user);
                    }
                }
                // 生成签名
                jwtToken = JwtUtil.sign(backstageUser.getUserName(), backstageUser.getPassword(), webConstant.getExpireTime());
                // 缓存token
                redisToken = IdUtil.simpleUUID();
                userMapper.update(backstageUser, new UpdateWrapper<>(backstageUser));
            } else {
                return ApiResult.failOf(ResponseResultEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());
            }
            // 验证通过返回用户部分信息 以及 拥有的角色和菜单
            UserInfoVO userVO = userMapper.selectMyUserInfo(backstageUser.getId());
            userVO.setMyMenus(userVO.getRoles());
            userVO.setUserRoutes(menuService.getMenuTree(userVO.getMenus()));
            userVO.setToken(redisToken);
            userVO.setJwtToken(jwtToken);
            redisCache.set(backstageUser.getUserName(), redisToken, webConstant.getExpireTime());
            redisCache.set(redisToken, userVO, webConstant.getExpireTime());
            //fabricClientManager.caEnroll(backstageUser.getUserName(), backstageUser.getPassword());
            if (backstageUser.getUserName().equals("admin")) {
                fabricClientManager.caEnroll(sdkProperties.getCaAdminName(), sdkProperties.getCaAdminPass());
            }
            fabricClientManager.caEnroll(backstageUser.getUserName(), backstageUser.getPassword());
            return ApiResult.successOf(userVO);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 禁用用户
     *
     * @param userId 用户id
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult disableOrEnableUser(Integer userId, Integer operation) throws BusinessException {
        BackstageUser user = userMapper.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("User is not exists!!");
        }
        user.setModifier(JwtUtil.getUserNameFromRedis());
        user.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        user.setStatus(operation);
        switch (operation) {
            case 1:
                if (userMapper.updateById(user) > 0) {
                    return ApiResult.successOf("Enable success!!");
                }
                return ApiResult.successOf("Enable failed!!");
            case 0:
                if (userMapper.updateById(user) > 0) {
                    if (redisCache.hasKey(user.getUserName())) {
                        String token = (String) redisCache.get(user.getUserName());
                        redisCache.delete(token, user.getUserName());
                    }

                    return ApiResult.successOf("Disable success!!");
                }
                return ApiResult.successOf("Disable failed!!");
            default:
                throw new BusinessException(String.format("Unknown operation %d !!", operation));
        }
    }

    /**
     * 用户注册
     *
     * @param registryDTO 注册信息
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult registry(UserRegistryDTO registryDTO) throws BusinessException {

        log.debug(String.format("--> %s 用户正在注册!!", registryDTO.getUsername()));
        if (!commonService.emailVerify(registryDTO.getEmail(), registryDTO.getVerifyCode())) {
            throw new BusinessException("验证码错误!!");
        }

        BackstageUser user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getUserName, registryDTO.getUsername())
                .and(wrapper -> wrapper.eq(BackstageUser::getValid, true)));
        if (!ObjectUtils.isEmpty(user)) {
            throw new BusinessException("用户名已存在!!");
        }

        user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getEmail, registryDTO.getUsername())
                .and(wrapper -> wrapper.eq(BaseEntity::getValid, Boolean.TRUE)));
        if (!ObjectUtils.isEmpty(user)) {
            throw new BusinessException("邮箱已被绑定!! 您可以使用邮箱登陆!!");
        }

        user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getTelephone, registryDTO.getTelephone())
                .and(wrapper -> wrapper.eq(BaseEntity::getValid, Boolean.TRUE)));
        if (!ObjectUtils.isEmpty(user)) {
            throw new BusinessException("手机号已被绑定!! 您可以使用手机号登陆!!");
        }

        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        user = dto2Entity(registryDTO);
        if (userMapper.insert(user) > 0) {
            // 设置一个默认的角色普通用户
            Integer[] tmpRole = {RoleEnum.USER.getRoleId()};
            rolesCheck(tmpRole);
            user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                    .eq(BackstageUser::getUserName, registryDTO.getUsername())
                    .and(wrapper -> wrapper.eq(BackstageUser::getValid, true)));
            if (userMapper.setUserRoles(user.getId(), tmpRole) > 0) {
                lock.lock();
                try {
                    // TODO: 2020/11/26 向ca注册，用户数据上链
                    if (!fabricClientManager.caRegister(user.getUserName(), user.getPassword())) {
                        throw new BusinessException("注册失败!!");
                    }
                    String func = "USER_CREATE";
                    String[] args = {user.getId().toString(), user.getUserName(), "", user.getEmail(), user.getTelephone()};
                    Map<String, Object> map = fabricClientManager.getChainCodeManager().invoke(fabricClientManager.caEnroll(user.getUserName(), user.getPassword()), func, args);
                    if (map.get("code").equals(0)) {
                        throw new BusinessException("注册失败!!");
                    }
                } catch (Exception e) {
                    platformTransactionManager.rollback(transaction);
                    throw new BusinessException("注册失败!!");
                } finally {
                    lock.unlock();
                }
                platformTransactionManager.commit(transaction);
                return ApiResult.successOf("注册成功!!");
            }
        }
        throw new BusinessException("注册失败!!");
    }

    /**
     * 实名认证接口
     *
     * @param name 真实姓名
     * @param idNo 身份证号
     * @return res
     * @throws Exception e
     */
    @Override
    public ApiResult realNameAuth(String name, String idNo) throws Exception {
        UserInfoVO userInfoVO = JwtUtil.getUserFromRedis();
        if (!ObjectUtils.isEmpty(userInfoVO.getRealNameAuthentication()) && userInfoVO.getRealNameAuthentication()) {
            return ApiResult.successOf("您已完成实名");
        }
        String host = authProperties.getHost();
        String path = authProperties.getPath();
        String appCode = authProperties.getAppCode();
        Map<String, String> headers = new HashMap<>(1);
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> body = new HashMap<>(2);
        body.put("idNo", idNo);
        body.put("name", name);
        //HttpResponse response = HttpUtils.doPost(host, path, headers, Collections.emptyMap(), body);

        //RealNameAuthEntity authEntity = JSONObject.toJavaObject(JSON.parseObject(EntityUtils.toString(response.getEntity())), RealNameAuthEntity.class);
//        if (!ObjectUtils.isEmpty(authEntity) && authEntity.getName().equals(name) && authEntity.getIdNo().equals(idNo)) {
        // 修改数据库实名认证状态

        //log.debug(authEntity.toString());
        String username = JwtUtil.getUserNameFromRedis();
        BackstageUser user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getUserName, username)
                .and(wrapper -> wrapper.eq(BackstageUser::getValid, Boolean.TRUE)));
        user.setRealNameAuthentication(Boolean.TRUE);
        user.setRealName(name);
        user.setIdCard(idNo);
        user.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        user.setModifier(username);
        userInfoVO.setRealNameAuthentication(Boolean.TRUE);
        if (!ObjectUtils.isEmpty(user) && userMapper.updateById(user) > 0 && redisCache.set(userInfoVO.getToken(), userInfoVO)) {
            // TODO: 2020/12/8 begin
            String func = "USER_REAL_NAME_AUTH";
            String[] args = {user.getId().toString(), user.getRealName(), user.getIdCard()};
            Map<String, Object> map = fabricClientManager.getChainCodeManager().invoke(fabricClientManager.caEnroll(user.getUserName(), user.getPassword()), func, args);
            if (map.get("code").equals(0)) {
                throw new BusinessException("实名认证失败");
            }
            // TODO: 2020/12/8 end
            return ApiResult.successOf("实名认证成功");
        }
//        }
        return ApiResult.failOf("实名认证失败");
    }

    @Override
    public ApiResult logout() throws BusinessException {
        String token = new RequestHolder().getRequest().getHeader(WebConstant.TOKEN_HEADER);
        String userName = JwtUtil.getUserNameFromRedis();
        log.debug(String.format("%s 用户正在登出!!", userName));
        if (redisCache.hasKey(token) || redisCache.hasKey(userName)) {
            redisCache.delete(token);
            redisCache.delete(userName);
        }
        return ApiResult.successOf("退出成功");
    }

    /**
     * 新增银行业务员
     *
     * @param addBankStaffDTO bank staff
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult addBankStaff(AddBankStaffDTO addBankStaffDTO) throws BusinessException {
        log.debug(String.format("--> 正在新增银行业务员 %s!!", addBankStaffDTO.getUsername()));

        BackstageUser user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getUserName, addBankStaffDTO.getUsername())
                .and(wrapper -> wrapper.eq(BackstageUser::getValid, true)));
        if (!ObjectUtils.isEmpty(user)) {
            throw new BusinessException("用户名已存在!!");
        }

        user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getEmail, addBankStaffDTO.getUsername())
                .and(wrapper -> wrapper.eq(BaseEntity::getValid, Boolean.TRUE)));
        if (!ObjectUtils.isEmpty(user)) {
            throw new BusinessException("邮箱已被绑定!! 您可以使用邮箱登陆!!");
        }

        user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getTelephone, addBankStaffDTO.getTelephone())
                .and(wrapper -> wrapper.eq(BaseEntity::getValid, Boolean.TRUE)));
        if (!ObjectUtils.isEmpty(user)) {
            throw new BusinessException("手机号已被绑定!! 您可以使用手机号登陆!!");
        }
        String initSecret = "Aa" + CommonUtil.getRandomNumCode(6);
        user = dto2Entity(addBankStaffDTO);
        user.setPassword(DigestAlgorithmUtils.md5Sign(initSecret));
        // 为银行业务员设置bankID
        UserInfoVO bankAdmin = JwtUtil.getUserFromRedis();
        if (StringUtils.isEmpty(bankAdmin.getBankId())) {
            throw new BusinessException("请先完成银行入驻申请。");
        }
        user.setBankId(bankAdmin.getBankId());
        if (userMapper.insert(user) > 0) {
            // 设置一个默认的角色普通用户
            Integer[] tmpRole = {RoleEnum.BANK_STAFF.getRoleId()};
            rolesCheck(tmpRole);
            user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                    .eq(BackstageUser::getUserName, addBankStaffDTO.getUsername())
                    .and(wrapper -> wrapper.eq(BackstageUser::getValid, true)));
            if (userMapper.setUserRoles(user.getId(), tmpRole) > 0) {

                if (commonService.sendEmailSecret(user.getEmail(), initSecret)) {
                    log.debug("初始密码邮件发送成功!!");
                } else {
                    throw new BusinessException("初始密码邮件发送失败!!");
                }

                log.debug("新增银行业务员成功!!");
                return ApiResult.successOf("新增银行业务员成功!!");
            }
        }
        return ApiResult.failOf("新增银行业务员失败!!");
    }

    /**
     * 启/禁银行业务员
     *
     * @param userId        用户id
     * @param operationCode 操作码
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult disableOrEnableBankStaff(Integer userId, Integer operationCode) throws BusinessException {
        BackstageUser user = userMapper.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("用户不存在!!");
        }
        List<BackRoleVO> roleList = getUserRole(userId).getData();
        Boolean flag = Boolean.TRUE;
        for (BackRoleVO role : roleList) {
            if (RoleEnum.BANK_STAFF.getRoleId().equals(role.getRoleId())) {
                flag = Boolean.FALSE;
            }
        }
        if (flag) {
            throw new BusinessException("您无法操作非银行业务员的用户");
        }
        user.setModifier(JwtUtil.getUserNameFromRedis());
        user.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        user.setStatus(operationCode);
        switch (operationCode) {
            case 1:
                if (userMapper.updateById(user) > 0) {
                    return ApiResult.successOf("启用成功");
                }
                return ApiResult.successOf("启用失败");
            case 0:
                if (userMapper.updateById(user) > 0) {
                    if (redisCache.hasKey(user.getUserName())) {
                        String token = (String) redisCache.get(user.getUserName());
                        redisCache.delete(token, user.getUserName());
                    }
                    return ApiResult.successOf("禁用成功");
                }
                return ApiResult.successOf("禁用失败");
            default:
                throw new BusinessException(String.format("Unknown operationCode %d", operationCode));
        }
    }

    /**
     * 获取银行业务员
     *
     * @param current 当前页码
     * @param size    页大小
     * @return res
     * @throws BusinessException e
     */
    @Override
    public ApiResult getBankStaff(Long current, Long size) throws BusinessException {
        Integer bankId = JwtUtil.getUserFromRedis().getBankId();
        Page<BackstageUser> page = userMapper.getUserByRole(new Page(current, size), RoleEnum.BANK_STAFF.getRoleId(), bankId);
        return ApiResult.successOf(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addUser(AddUserDTO addUserDTO) throws BusinessException {
        BackstageUser user = new BackstageUser();
        user.setUserName(addUserDTO.getUsername());
        if (userMapper.selectOne(new QueryWrapper<>(user)) != null) {
            throw new BusinessException(ResponseResultEnum.USER_ALREADY_EXIST.getMsg());
        }
        BackstageUser backstageUser = dto2Entity(addUserDTO);
        backstageUser.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        backstageUser.setCreator(JwtUtil.getUserNameFromRedis());
        int i = userMapper.addBackUser(backstageUser);
        Assert.isTrue(i > 0, "新增用户失败");
        // 如果没有设置角色，则设置一个默认的角色
        if (ObjectUtils.isEmpty(addUserDTO.getRoles())) {
            Integer[] tmpRole = {2};
            addUserDTO.setRoles(tmpRole);
        } else {
            // 角色检查
            rolesCheck(addUserDTO.getRoles());
            Set<Integer> roleSet = new HashSet<>(Arrays.asList(addUserDTO.getRoles()));
            addUserDTO.setRoles(roleSet.toArray(new Integer[roleSet.size()]));
        }
        // 再设置用户角色
        int n = userMapper.setUserRoles(backstageUser.getId(), addUserDTO.getRoles());
        return n > 0 ? ApiResult.successOf("新增用户成功") : ApiResult.failOf("新增用户失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteUser(Integer userId) throws BusinessException {
        BackstageUser backstageUser = this.getById(userId);
        if (ObjectUtils.isEmpty(backstageUser)) {
            return ApiResult.failOf("用户不存在，删除失败");
        }
        backstageUser.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        backstageUser.setValid(false);
        backstageUser.setModifier(JwtUtil.getRedisToken());
        return this.updateById(backstageUser) ? ApiResult.successOf("删除成功") : ApiResult.failOf("删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateUser(BackUserDTO userDTO) throws Exception {
        BackstageUser backstageUser = userMapper.selectById(userDTO.getUserId());
        if (ObjectUtils.isEmpty(backstageUser)) {
            throw new BusinessException("");
        }
        Assert.isTrue(!checkEmailIsExist(userDTO.getEmail()).getData(), "邮箱已存在");
        Assert.isTrue(!checkTelephoneIsExist(userDTO.getTelephone()).getData(), "手机号码已存在");
        backstageUser.setEmail(userDTO.getEmail());
        backstageUser.setTelephone(userDTO.getTelephone());
        backstageUser.setModifier(JwtUtil.getUserNameFromRedis());
        backstageUser.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        boolean i = this.updateById(backstageUser);

        // TODO: 2020/12/8 begin
        String func = "USER_UPDATE_CONTACT_INFO";

        String[] args = {backstageUser.getId().toString(), "1", backstageUser.getEmail()};
        Map<String, Object> map = fabricClientManager.getChainCodeManager().invoke(fabricClientManager.caEnroll(backstageUser.getUserName(), backstageUser.getPassword()), func, args);
        if (map.get("code").equals(0)) {
            throw new BusinessException("修改失败");
        }
        String[] args1 = {backstageUser.getId().toString(), "2", backstageUser.getTelephone()};
        map = fabricClientManager.getChainCodeManager().invoke(fabricClientManager.caEnroll(backstageUser.getUserName(), backstageUser.getPassword()), func, args1);
        if (map.get("code").equals(0)) {
            throw new BusinessException("修改失败");
        }
        // TODO: 2020/12/8 end

        // 角色检查
        rolesCheck(userDTO.getRoles());
        Set<Integer> roleSet = new HashSet<>(Arrays.asList(userDTO.getRoles()));
        Integer[] rolesX = roleSet.toArray(new Integer[roleSet.size()]);
        // 删除用户旧的角色
        BackstageUserRole userRole = new BackstageUserRole();
        userRole.setBackUserId(backstageUser.getId());
        userRole.setValid(true);
        userRoleMapper.delete(new QueryWrapper<>(userRole));
        // 设置用户角色
        int o = userMapper.setUserRoles(backstageUser.getId(), rolesX);
        return (i && o > 0) ? ApiResult.successOf("修改成功") : ApiResult.failOf("修改失败");
    }

    @Override
    public ApiResult updateBaseInfo(UserBaseInfoDTO baseInfoDTO) throws BusinessException {
        // TODO: 2020/12/1 判断用户是否实名
        UserInfoVO userInfoVO = JwtUtil.getUserFromRedis();
        if (!ObjectUtils.isEmpty(userInfoVO.getRealNameAuthentication()) && !userInfoVO.getRealNameAuthentication()) {
            throw new BusinessException("请先进行实名!!");
        }
        BackstageUser user = userMapper.selectById(userInfoVO.getUserId());

        if (user.getRealName().equals(baseInfoDTO.getRealName()) && user.getIdCard().equals(baseInfoDTO.getIdNo())) {
            user.setSex(baseInfoDTO.getSex());
            user.setAge(baseInfoDTO.getAge());
            if (!userInfoVO.getTelephone().equals(baseInfoDTO.getTel())) {
                throw new BusinessException("您填写的手机号与您注册时填写的手机不一致!!");
            }
            user.setTelephone(baseInfoDTO.getTel());
            if (userMapper.updateById(user) > 0) {
                // TODO: 2020/12/1 这里的数据是否需要上链

                return ApiResult.successOf("保存基础信息成功");
            }
        } else {
            throw new BusinessException("实名信息不一致");
        }
        return ApiResult.failOf("保存基础信息失败");
    }

    @Override
    public ApiResult backUserList(Integer current, Integer size, String userName, String telephone, String email) {
        Page<UserListVO> users = userMapper.selectAllUsers(new Page<>(current, size), userName, telephone, email);
        return ApiResult.successOf(users);
    }

    @Override
    public ApiResult<Boolean> checkUserIsExist(String username) {
        BackstageUser backstageUser = new BackstageUser();
        backstageUser.setUserName(username);
        backstageUser.setValid(true);
        return ApiResult.successOf(userMapper.selectCount(new QueryWrapper<>(backstageUser)) > 0);
    }

    @Override
    public ApiResult<Boolean> checkEmailIsExist(String email) {
        BackstageUser user = new BackstageUser();
        user.setEmail(email);
        user.setValid(true);
        return ApiResult.successOf(userMapper.selectCount(new QueryWrapper<>(user)) > 0);
    }

    @Override
    public ApiResult<Boolean> checkTelephoneIsExist(String telephone) {
        BackstageUser user = new BackstageUser();
        user.setTelephone(telephone);
        user.setValid(true);
        return ApiResult.successOf(userMapper.selectCount(new QueryWrapper<>(user)) > 0);
    }

    @Override
    public ApiResult<List<BackRoleVO>> getUserRole(Integer userId) {
        return ApiResult.successOf(userMapper.getUserRole(userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateUserRole(Integer userId, Integer[] roles) throws BusinessException {
        Assert.isTrue(!ObjectUtils.isEmpty(this.getById(userId)), ResponseResultEnum.USER_NOT_EXIST.getMsg());
        // 角色检查
        rolesCheck(roles);
        Set<Integer> roleSet = new HashSet<>(Arrays.asList(roles));
        Integer[] rolesX = roleSet.toArray(new Integer[roleSet.size()]);
        // 删除用户旧的角色
        BackstageUserRole userRole = new BackstageUserRole();
        userRole.setBackUserId(userId);
        userRole.setValid(true);
        userRoleMapper.delete(new QueryWrapper<>(userRole));
        // 设置用户角色
        int i = userMapper.setUserRoles(userId, rolesX);
        return i > 0 ? ApiResult.successOf("角色设置成功") : ApiResult.failOf("角色设置失败");
    }

    private void rolesCheck(Integer[] roles) throws BusinessException {
        if (ObjectUtils.isEmpty(roles)) {
            throw new BusinessException("角色列表为空");
        }
        // 角色去重
        Set<Integer> roleSet = new HashSet<>(Arrays.asList(roles));
        List<Integer> roles2 = new ArrayList<>(roleSet);
        // 先检查角色是否存在
        BackstageRole role = new BackstageRole();
        role.setValid(true);
        List<BackstageRole> roleList = roleMapper.selectList(new QueryWrapper<>(role));
        List<Integer> roles1 = new ArrayList<>();
        for (BackstageRole aRoleList : roleList) {
            roles1.add(aRoleList.getId());
        }
        List<Integer> reduce = roles2.stream().filter(item -> !roles1.contains(item)).collect(toList());
        log.debug("---差集 reduce (list2 - list1)---");
        if (reduce.size() > 0) {
            StringBuilder re = new StringBuilder();
            re.append("角色ID为： ");
            for (Integer id : reduce) {
                re.append(id).append(", ");
            }
            re.append("角色不存在");
            throw new BusinessException(re.toString());
        }
    }

//    /**
//     * 忘记登录密码，通过手机号码修改密码
//     *
//     * @param telephone   手机号码
//     * @param code        短信验证
//     * @param newPassword 新密码
//     * @return json
//     */
//    @Override
//    public ApiResult updateLoginPasswordByTelephone(String telephone, String code, String newPassword, Integer actionId) throws BusinessException {
//        try {
//            Assert.isTrue((Boolean) commonService.verifySmsVerificationCode(telephone, code, actionId).getData(),
//                    "短信验证码不正确");
//            BackstageUser backstageUser = new BackstageUser();
//            backstageUser.setTelephone(telephone);
//            backstageUser = userMapper.selectOne(new QueryWrapper<>(backstageUser));
//            Assert.isTrue(!ObjectUtils.isEmpty(backstageUser), "密码修改失败");
//            backstageUser.setPassword(DigestAlgorithmUtils.md5Sign(newPassword));
//            backstageUser.setModifier(backstageUser.getUserName());
//            backstageUser.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
//            if (userMapper.updateById(backstageUser) > 0) {
//                return ApiResult.successOf("密码修改成功");
//            } else {
//                return ApiResult.successOf("密码修改失败");
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new BusinessException(e);
//        }
//    }

    @Override
    public ApiResult updateLoginPasswordByOldPassword(String oldPassword, String newPassword) throws Exception {
        UserInfoVO userInfo = JwtUtil.getUserFromRedis();
        BackstageUser backstageUser = userMapper.selectById(userInfo.getUserId());
        Assert.isTrue(!ObjectUtils.isEmpty(backstageUser), ResponseResultEnum.USER_TOKEN_INVALID.getMsg());
        if (DigestAlgorithmUtils.md5Verify(oldPassword, backstageUser.getPassword())) {
            backstageUser.setPassword(DigestAlgorithmUtils.md5Sign(newPassword));
            backstageUser.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            backstageUser.setModifier(userInfo.getUserName());
            if (userMapper.updateById(backstageUser) > 0) {
                // TODO: 2020/11/26 修改ca用户密码
                fabricClientManager.getCaManager().update(fabricClientManager.caEnroll(backstageUser.getUserName(), backstageUser.getPassword()), DigestAlgorithmUtils.md5Sign(newPassword));
                return ApiResult.successOf("密码修改成功");
            }
        } else {
            return ApiResult.failOf("密码错误");
        }
        return ApiResult.failOf("密码修改失败,稍后再试");
    }

    /**
     * 修改密码(仅管理员可操作)
     *
     * @param userId   旧密码
     * @param password 新密码
     * @return ApiResult
     * @throws BusinessException e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<String> changePwd(String userId, String password) throws BusinessException {
        BackstageUser user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda().eq(BackstageUser::getValid, true)
                .and(wrapper -> wrapper.eq(BackstageUser::getId, userId)));
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(DigestAlgorithmUtils.md5Sign(password));
        int i = userMapper.updateById(user);
        return i > 0 ? ApiResult.successOf("修改密码成功") : ApiResult.failOf("修改密码失败");
    }

    /**
     * dto 转实体
     *
     * @param dto dto
     * @param <T> 泛型
     * @return 实体
     */
    private <T> BackstageUser dto2Entity(T dto) {
        BackstageUser user = new BackstageUser();
        if (dto instanceof BackUserDTO) {
            BackUserDTO backUserDTO = (BackUserDTO) dto;
            user.setId(backUserDTO.getUserId());
            user.setEmail(backUserDTO.getEmail());
            user.setTelephone(backUserDTO.getTelephone());
        } else if (dto instanceof AddUserDTO) {
            AddUserDTO addUserDTO = (AddUserDTO) dto;
            user.setUserName(addUserDTO.getUsername());
            user.setPassword(DigestAlgorithmUtils.md5Sign(addUserDTO.getPassword()));
            user.setEmail(addUserDTO.getEmail());
            user.setTelephone(addUserDTO.getTelephone());
        } else if (dto instanceof UserRegistryDTO) {
            UserRegistryDTO registryDTO = (UserRegistryDTO) dto;
            user.setUserName(registryDTO.getUsername())
                    .setPassword(DigestAlgorithmUtils.md5Sign(registryDTO.getPassword()))
                    .setTelephone(registryDTO.getTelephone())
                    .setEmail(registryDTO.getEmail())
                    .setCreator(registryDTO.getUsername())
                    .setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        } else if (dto instanceof AddBankStaffDTO) {
            AddBankStaffDTO bankStaffDTO = (AddBankStaffDTO) dto;
            user.setUserName(bankStaffDTO.getUsername())
                    .setEmail(bankStaffDTO.getEmail())
                    .setTelephone(bankStaffDTO.getTelephone());
        }
        return user;
    }

    private BackstageUser judgeLoginType(String username) throws BusinessException {
        if (Pattern.compile(RegexConstant.TELEPHONE_REG).matcher(username).find()) {
            return getUserByTel(username);
        } else if (Pattern.compile(RegexConstant.EMAIL_REG).matcher(username).find()) {
            return getUserByEmail(username);
        } else if (Pattern.compile(RegexConstant.USERNAME_REG).matcher(username).find()) {
            return getUserByUsername(username);
        } else {
            throw new BusinessException("Invalid username!!");
        }
    }

    private BackstageUser getUserByUsername(String username) throws BusinessException {
        BackstageUser user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getUserName, username)
                .and(wrapper -> wrapper.eq(BaseEntity::getValid, Boolean.TRUE)));
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("User is not exists!!");
        }
        return user;
    }

    private BackstageUser getUserByEmail(String email) throws BusinessException {
        BackstageUser user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getEmail, email)
                .and(wrapper -> wrapper.eq(BaseEntity::getValid, Boolean.TRUE)));
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("User is not exists!!");
        }
        return user;
    }

    private BackstageUser getUserByTel(String tel) throws BusinessException {
        BackstageUser user = userMapper.selectOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getTelephone, tel)
                .and(wrapper -> wrapper.eq(BaseEntity::getValid, Boolean.TRUE)));
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("User is not exists!!");
        }
        return user;
    }

}
