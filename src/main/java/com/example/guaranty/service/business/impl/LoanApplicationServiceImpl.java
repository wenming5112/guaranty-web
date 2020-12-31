package com.example.guaranty.service.business.impl;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.guaranty.common.constant.WebConstant;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.common.utils.FileHashUtils;
import com.example.guaranty.component.FabricClientManager;
import com.example.guaranty.config.upload.FileUploadProperties;
import com.example.guaranty.dao.business.FileCacheMapper;
import com.example.guaranty.dao.business.LoanApplicationMapper;
import com.example.guaranty.dto.business.LoanApplicationDTO;
import com.example.guaranty.entity.business.*;
import com.example.guaranty.service.business.*;
import com.example.guaranty.shiro.JwtUtil;
import com.example.guaranty.vo.business.MaterialUploadVO;
import com.example.guaranty.vo.business.UserInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;


/**
 * 贷款申请表 Impl
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-26 14:13:11
 **/
@Service
public class LoanApplicationServiceImpl extends ServiceImpl<LoanApplicationMapper, LoanApplication> implements LoanApplicationService {

    @Resource
    private LoanApplicationMapper loanApplicationMapper;

    @Resource
    private BankService bankService;

    @Resource
    private UserMortgageMaterialService mortgageMaterialService;

    @Resource
    private BackstageUserService userService;

    @Resource
    private LoanProductService loanProductService;

    @Resource
    private FileUploadProperties fileUploadProperties;

    @Resource
    private FileCacheMapper fileCacheMapper;

    @Resource
    private FabricClientManager fabricClientManager;

    @Override
    public ApiResult createLoanApplication(LoanApplicationDTO loanApplicationDTO) throws BusinessException {
        LoanApplication loanApplication = dto2Entity(loanApplicationDTO);
        loanApplication.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        loanApplication.setCreator(JwtUtil.getUserNameFromRedis());
        if (loanApplicationMapper.insert(loanApplication) > 0) {
            return ApiResult.successOf("正在申请，等待业务员审核!!");
        }

        return ApiResult.failOf("申请失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult uploadMaterialFile(Integer loanApplicationId, MultipartFile file) throws BusinessException {
        if (ObjectUtils.isEmpty(file)) {
            throw new BusinessException("不能上传空文件");
        }
        MaterialUploadVO materialUploadVO = new MaterialUploadVO();
        File newFile;
        try {
            String fileHash = FileHashUtils.getSha256ByStream(file.getInputStream());
            String path = fileUploadProperties.getPath();
            File folderPath = new File(path);
            if (!folderPath.exists() && !folderPath.isDirectory()) {
                if (folderPath.mkdirs()) {
                    log.warn(String.format("Path %s is not exists, it will create new directory!!", path));
                }
            }
            //获取文件名，带后缀
            String fileName = file.getOriginalFilename();
            String fileSuffix = "";
            if (Objects.requireNonNull(fileName).contains(WebConstant.FILE_POINT)) {
                fileSuffix = fileName.substring(fileName.lastIndexOf("."));
            }
            fileName = fileHash;
            newFile = new File(path + fileName + fileSuffix);
            String url = fileUploadProperties.getBaseUrl() + fileName + fileSuffix;

            //将上传的文件写到服务器上指定的文件路径
            file.transferTo(newFile);

            FileCache fileCache = fileCacheMapper.selectOne(new QueryWrapper<FileCache>().lambda()
                    .eq(FileCache::getTitle, fileName)
                    .and(wrapper -> wrapper.eq(FileCache::getValid, Boolean.TRUE)));
            if (ObjectUtils.isEmpty(fileCache)) {
                fileCache = new FileCache();
                fileCache.setFileSize(file.getSize());
                fileCache.setTitle(fileName);
                fileCache.setTitleSuffix(fileSuffix);
                fileCache.setUrl(url);
                fileCache.setCreator(JwtUtil.getUserNameFromRedis());
                fileCache.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
                if (fileCacheMapper.insert(fileCache) < 1) {
                    throw new BusinessException("文件上传失败");
                }
            }

            LoanApplication loanApplication = loanApplicationMapper.selectOne(new QueryWrapper<LoanApplication>().lambda()
                    .eq(LoanApplication::getId, loanApplicationId)
                    .and(wrapper -> wrapper.eq(LoanApplication::getStatus, 1))
                    .and(wrapper -> wrapper.eq(LoanApplication::getValid, Boolean.TRUE)));
            if (ObjectUtils.isEmpty(loanApplication)) {
                throw new BusinessException("贷款申请不存在或者该贷款申请未通过!!");
            }
            loanApplication.setMaterialUrl(url);
            loanApplication.setMaterialHash(fileHash);

            if (loanApplicationMapper.updateById(loanApplication) < 1) {
                // TODO: 2020/12/8 贷款申请数据上链
                BackstageUser user = userService.getById(loanApplication.getProposer());
                String func = "USER_ADDITIONAL_LOAN_APPLICATION_CONTACT";
                String[] args = {loanApplication.getId().toString(), loanApplication.getMaterialHash(), loanApplication.getMaterialUrl()};
                Map<String, Object> map = fabricClientManager.getChainCodeManager().invoke(fabricClientManager.caEnroll(user.getUserName(), user.getPassword()), func, args);
                if (map.get("code").equals(1)) {
                    throw new BusinessException("文件上传失败");
                }
            }
            materialUploadVO.setMaterialUrl(url);
            materialUploadVO.setMaterialHash(fileHash);

        } catch (Exception e) {
            throw new BusinessException("文件上传失败");
        }
        return ApiResult.successOf(materialUploadVO);
    }


    @Override
    public ApiResult loanApplicationApproval(Integer loanApplicationId, Integer operationCode, String reason) throws Exception {
        LoanApplication loanApplication = loanApplicationMapper.selectById(loanApplicationId);
        if (ObjectUtils.isEmpty(loanApplication)) {
            throw new BusinessException("申请单不存在!!");
        }

        loanApplication.setStatus(operationCode);
        switch (operationCode) {
            case 1:
                loanApplication.setModifier(JwtUtil.getUserNameFromRedis());
                loanApplication.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));

                if (loanApplicationMapper.updateById(loanApplication) > 0) {
                    // TODO: 2020/12/8
                    BackstageUser user = userService.getById(loanApplication.getProposer());
                    String func = "USER_CREATE_LOAN_APPLICATION";
                    String[] args = {loanApplication.getId().toString(), loanApplication.getProposer(),
                            loanApplication.getBankId().toString(), loanApplication.getLoanProductId().toString(),
                            loanApplication.getStatus().toString()};
                    Map<String, Object> map = fabricClientManager.getChainCodeManager().invoke(fabricClientManager.caEnroll(user.getUserName(), user.getPassword()), func, args);
                    if (map.get("code").equals(1)) {
                        return ApiResult.successOf("同意操作成功");
                    }
                }
                return ApiResult.failOf("同意操作失败!!");
            case 0:
                loanApplication.setRefuseReason(reason);
                loanApplication.setModifier(JwtUtil.getUserNameFromRedis());
                loanApplication.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
                if (loanApplicationMapper.updateById(loanApplication) > 0) {
                    return ApiResult.successOf("拒绝成功");
                }
                return ApiResult.failOf("拒绝操作失败!!");
            default:
                throw new BusinessException(String.format("Unknown operationCode %d", operationCode));
        }
    }

    @Override
    public ApiResult getLoanApplication(String username, String realName, String idNo, Integer status, Long current, Long size) throws BusinessException {
        UserInfoVO userInfoVO = JwtUtil.getUserFromRedis();
        Integer bankId = userInfoVO.getBankId();
        Bank bank = bankService.getById(bankId);
        if (ObjectUtils.isEmpty(bank)) {
            throw new BusinessException("银行信息不存在!!");
        }
        BackstageUser user;
        LambdaQueryWrapper<LoanApplication> lambdaQueryWrapper = new QueryWrapper<LoanApplication>().lambda();
        lambdaQueryWrapper.eq(LoanApplication::getBankId, bankId);
        if (!StringUtils.isEmpty(username)) {
            user = userService.getOne(new QueryWrapper<BackstageUser>().lambda()
                    .eq(BackstageUser::getUserName, username)
                    .and(wrapper -> wrapper.eq(BackstageUser::getValid, Boolean.TRUE)));
            lambdaQueryWrapper.and(wrapper -> wrapper.eq(LoanApplication::getProposer, user.getId()));
        } else if (!StringUtils.isEmpty(realName)) {
            user = userService.getOne(new QueryWrapper<BackstageUser>().lambda()
                    .eq(BackstageUser::getRealName, realName)
                    .and(wrapper -> wrapper.eq(BackstageUser::getValid, Boolean.TRUE)));
            lambdaQueryWrapper.and(wrapper -> wrapper.eq(LoanApplication::getProposer, user.getId()));
        } else if (!StringUtils.isEmpty(realName)) {
            user = userService.getOne(new QueryWrapper<BackstageUser>().lambda()
                    .eq(BackstageUser::getRealName, realName)
                    .and(wrapper -> wrapper.eq(BackstageUser::getValid, Boolean.TRUE)));
            lambdaQueryWrapper.and(wrapper -> wrapper.eq(LoanApplication::getProposer, user.getId()));
        }

        if (!ObjectUtils.isEmpty(status)) {
            lambdaQueryWrapper.and(wrapper -> wrapper.eq(LoanApplication::getStatus, status));
        }
        lambdaQueryWrapper.orderByDesc(LoanApplication::getCreateTime);

        lambdaQueryWrapper.orderByDesc(LoanApplication::getCreateTime);
        Page<LoanApplication> page = loanApplicationMapper.selectPage(new Page<>(current, size), lambdaQueryWrapper);
        for (LoanApplication loanApplication : page.getRecords()) {
            BackstageUser u = userService.getOne(new QueryWrapper<BackstageUser>().lambda()
                    .eq(BackstageUser::getId, loanApplication.getProposer())
                    .and(wrapper -> wrapper.eq(BackstageUser::getValid, Boolean.TRUE)));
            LoanProduct loanProduct = loanProductService.getById(loanApplication.getLoanProductId());

            loanApplication.setLoanProduct(loanProduct);
            loanApplication.setUser(u);
        }

        return ApiResult.successOf(page);
    }

    @Override
    public ApiResult getLoanMyApplication(String bankName, Integer status, Long current, Long size) throws BusinessException {
        Integer userId = JwtUtil.getUserFromRedis().getUserId();
        LambdaQueryWrapper<LoanApplication> lambdaQueryWrapper = new QueryWrapper<LoanApplication>().lambda();
        lambdaQueryWrapper.eq(LoanApplication::getProposer, userId);
        if (!StringUtils.isEmpty(bankName)) {
            Bank bank = bankService.getByName(bankName);
            if (ObjectUtils.isEmpty(bank)) {
                throw new BusinessException("银行信息不存在!!");
            }
            lambdaQueryWrapper.and(wrapper -> wrapper.eq(LoanApplication::getBankId, bank.getId()));
        }

        if (!ObjectUtils.isEmpty(status)) {
            lambdaQueryWrapper.and(wrapper -> wrapper.eq(LoanApplication::getStatus, status));
        }
        lambdaQueryWrapper.orderByDesc(LoanApplication::getCreateTime);

        Page<LoanApplication> page = loanApplicationMapper.selectPage(new Page<>(current, size), lambdaQueryWrapper);
        for (LoanApplication loanApplication : page.getRecords()) {
            BackstageUser u = userService.getOne(new QueryWrapper<BackstageUser>().lambda()
                    .eq(BackstageUser::getId, loanApplication.getProposer())
                    .and(wrapper -> wrapper.eq(BackstageUser::getValid, Boolean.TRUE)));
            LoanProduct loanProduct = loanProductService.getById(loanApplication.getLoanProductId());
            Bank bank = bankService.getById(loanApplication.getBankId());
            loanApplication.setBank(bank);
            loanApplication.setLoanProduct(loanProduct);
            loanApplication.setUser(u);
        }
        return ApiResult.successOf(page);
    }

    @Override
    public ApiResult getUserMortgageMaterial(Integer loanApplicationId) throws BusinessException {
        LoanApplication loanApplication = loanApplicationMapper.selectById(loanApplicationId);
        if (ObjectUtils.isEmpty(loanApplication)) {
            throw new BusinessException("申请单不存在!!");
        }
        BackstageUser user = userService.getOne(new QueryWrapper<BackstageUser>().lambda()
                .eq(BackstageUser::getId, loanApplication.getProposer())
                .and(wrapper -> wrapper.eq(BackstageUser::getValid, Boolean.TRUE)));
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("申请单的申请人不存在!!");
        }

        UserMortgageMaterial userMortgageMaterial = mortgageMaterialService.getOne(new QueryWrapper<UserMortgageMaterial>().lambda()
                .eq(UserMortgageMaterial::getUserId, user.getId())
                .and(wrapper -> wrapper.eq(UserMortgageMaterial::getValid, Boolean.TRUE)));

        if (ObjectUtils.isEmpty(userMortgageMaterial)) {
            throw new BusinessException("用户未添抵押资料");
        }

        return ApiResult.successOf(userMortgageMaterial);
    }

    private <T> LoanApplication dto2Entity(T dto) {
        LoanApplication loanApplication = new LoanApplication();
        if (dto instanceof LoanApplicationDTO) {
            LoanApplicationDTO loanApplicationDTO = (LoanApplicationDTO) dto;
            loanApplication.setBankId(loanApplicationDTO.getBankId())
                    .setLoanProductId(loanApplicationDTO.getLoanProductId())
                    .setStatus(-1)
                    .setProposer(loanApplicationDTO.getProposer());
        }
        return loanApplication;
    }
}
