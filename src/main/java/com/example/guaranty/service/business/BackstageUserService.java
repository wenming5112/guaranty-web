package com.example.guaranty.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.*;
import com.example.guaranty.entity.business.BackstageUser;
import com.example.guaranty.vo.business.BackRoleVO;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 用户模块 接口
 *
 * @author ming
 * @date 2019:08:16 16:08
 */
public interface BackstageUserService extends IService<BackstageUser> {

    /**
     * 用户注册
     *
     * @param registryDTO 注册信息
     * @return res
     * @throws BusinessException e
     */
    ApiResult registry(UserRegistryDTO registryDTO) throws BusinessException;

    /**
     * 用户登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return json
     * @throws IOException              e1
     * @throws NoSuchAlgorithmException e2
     * @throws BusinessException        e3
     */
    ApiResult login(String username, String password) throws IOException, NoSuchAlgorithmException, BusinessException;

    /**
     * 禁用用户
     *
     * @param userId    用户id
     * @param operation operation(开启或者关闭)
     * @return res
     * @throws BusinessException e
     */
    ApiResult disableOrEnableUser(Integer userId, Integer operation) throws BusinessException;

    /**
     * 实名认证接口
     *
     * @param name 真实姓名
     * @param idNo 身份证号
     * @return res
     * @throws Exception e
     */
    ApiResult realNameAuth(String name, String idNo) throws Exception;

    /**
     * 用户登出
     *
     * @return json
     * @throws BusinessException e
     */
    ApiResult logout() throws BusinessException;

    /**
     * 新增银行业务员
     *
     * @param addBankStaffDTO bank staff
     * @return res
     * @throws BusinessException e
     */
    ApiResult addBankStaff(AddBankStaffDTO addBankStaffDTO) throws BusinessException;

    /**
     * 启/禁银行业务员
     *
     * @param userId        用户id
     * @param operationCode 操作码
     * @return res
     * @throws BusinessException e
     */
    ApiResult disableOrEnableBankStaff(Integer userId, Integer operationCode) throws BusinessException;

    /**
     * 获取银行业务员
     *
     * @param current 当前页码
     * @param size    页大小
     * @return res
     * @throws BusinessException e
     */
    ApiResult getBankStaff(Long current, Long size) throws BusinessException;

    /**
     * 添加一个后台用户
     *
     * @param addUserDTO register param
     * @return json
     * @throws BusinessException e
     */
    ApiResult addUser(AddUserDTO addUserDTO) throws BusinessException;

    /**
     * 删除用户
     *
     * @param userId userId
     * @return ApiResult
     * @throws BusinessException e
     */
    ApiResult deleteUser(Integer userId) throws BusinessException;

    /**
     * 修改用户信息
     *
     * @param userDTO user dto
     * @return json
     * @throws BusinessException e
     */
    ApiResult updateUser(BackUserDTO userDTO) throws Exception;


    ApiResult updateBaseInfo(UserBaseInfoDTO baseInfoDTO) throws BusinessException;

    /**
     * 后台用户列表
     *
     * @param current   1
     * @param size      10
     * @param userName  用户名
     * @param telephone 电话号码
     * @param email     邮箱
     * @return json
     */
    ApiResult backUserList(Integer current, Integer size, String userName, String telephone, String email);

    /**
     * 获取用户角色
     *
     * @param userId userId
     * @return json
     */
    ApiResult<List<BackRoleVO>> getUserRole(Integer userId);

    /**
     * 修改用户角色
     *
     * @param userId userId
     * @param roles  roles
     * @return List<BackRoleVO>
     * @throws BusinessException e
     */
    ApiResult updateUserRole(Integer userId, Integer[] roles) throws BusinessException;

    /**
     * 检测用户是否存在
     *
     * @param username 用户名
     * @return true or false
     * @date 2019/9/7 0007 11:55
     */
    ApiResult checkUserIsExist(String username);

    /**
     * 邮箱是否已存在
     *
     * @param email 邮箱
     * @return true or false
     * @date 2019/9/18 0018 21:13
     */
    ApiResult checkEmailIsExist(String email);

    /**
     * 手机号是否已存在
     *
     * @param telephone 手机号码
     * @return true or false
     * @date 2019/9/18 0018 21:13
     */
    ApiResult checkTelephoneIsExist(String telephone);

//    /**
//     * 忘记登录密码，通过手机号码修改密码
//     *
//     * @param telephone   手机号码
//     * @param code        短信验证
//     * @param newPassword 新密码
//     * @param actionId    行为ID
//     * @return json
//     * @throws BusinessException e
//     */
//    ApiResult updateLoginPasswordByTelephone(String telephone, String code, String newPassword, Integer actionId) throws BusinessException;

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return ApiResult
     * @throws BusinessException e
     */
    ApiResult updateLoginPasswordByOldPassword(String oldPassword, String newPassword) throws Exception;

    /**
     * 修改密码(仅管理员可操作)
     *
     * @param userId   旧密码
     * @param password 新密码
     * @return ApiResult
     * @throws BusinessException e
     */
    ApiResult<String> changePwd(String userId, String password) throws BusinessException;

}
