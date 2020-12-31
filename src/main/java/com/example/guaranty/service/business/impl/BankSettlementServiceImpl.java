package com.example.guaranty.service.business.impl;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.guaranty.common.enumeration.BankSettlementEnum;
import com.example.guaranty.common.enumeration.RoleEnum;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dao.business.BankSettlementMapper;
import com.example.guaranty.dto.business.BankSettlementDTO;
import com.example.guaranty.entity.business.BackstageUser;
import com.example.guaranty.entity.business.Bank;
import com.example.guaranty.entity.business.BankSettlement;
import com.example.guaranty.service.business.BackstageUserService;
import com.example.guaranty.service.business.BankService;
import com.example.guaranty.service.business.BankSettlementService;
import com.example.guaranty.shiro.JwtUtil;
import com.example.guaranty.vo.business.BankSettlementListVO;
import com.example.guaranty.vo.business.UserInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 银行入驻申请 Impl
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-26 17:21:02
 **/
@Service
public class BankSettlementServiceImpl extends ServiceImpl<BankSettlementMapper, BankSettlement> implements BankSettlementService {

    @Resource
    private BankSettlementMapper bankSettlementMapper;

    @Resource
    private BankService bankService;

    @Resource
    private BackstageUserService userService;

    /**
     * 银行入驻申请
     *
     * @param bankSettlementDTO 申请信息
     * @return res
     * @throws BusinessException e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<String> createSettlementApplication(BankSettlementDTO bankSettlementDTO) throws BusinessException {
        // 状态 (-1:未通过, 0:待审批, 1:通过)

        BankSettlement bankSettlement = bankSettlementMapper.selectOne(new QueryWrapper<BankSettlement>().lambda().eq(BankSettlement::getBankName, bankSettlementDTO.getBankName()));
        if (!ObjectUtils.isEmpty(bankSettlement)) {
            if (BankSettlementEnum.AGREE.getCode().equals(bankSettlement.getStatus())) {
                throw new BusinessException("银行入驻申请已存在且已通过，请勿重复提交!!");
            } else if (BankSettlementEnum.DISAGREE.getCode().equals(bankSettlement.getStatus())) {
                throw new BusinessException("您的入驻申请未通过，请仔细审查未通过原因，请勿重复提交!!");
            } else if (BankSettlementEnum.WITHOUT_APPROVAL.getCode().equals(bankSettlement.getStatus())) {
                throw new BusinessException("您的入驻申请正在审批中，请勿重复提交!!");
            } else {
                throw new BusinessException("入驻申请状态错误!!");
            }
        }
        if (bankSettlementMapper.insert(dto2Entity(bankSettlementDTO)) > 0) {
            // TODO: 2020/11/28 数据上链

            return ApiResult.successOf("申请提交成功!!");
        }

        return ApiResult.failOf("申请提交失败!!");
    }

    /**
     * 银行入驻申请审批
     *
     * @param settlementId 申请ID
     * @param reason       原因
     * @return res
     * @throws BusinessException e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<String> settlementApproval(Integer settlementId, Integer operationCode, String reason) throws BusinessException {
        BankSettlement bankSettlement = bankSettlementMapper.selectById(settlementId);
        if (ObjectUtils.isEmpty(bankSettlement)) {
            throw new BusinessException(String.format("ID为 %d 的申请单不存在!!", settlementId));
        }
        bankSettlement.setModifier(JwtUtil.getUserNameFromRedis());
        bankSettlement.setModifyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        switch (operationCode) {
            case 0:
                if (StringUtils.isEmpty(reason)) {
                    throw new BusinessException("When disagree, The reason must not be null!!");
                }
                bankSettlement.setStatus(BankSettlementEnum.DISAGREE.getCode());
                bankSettlement.setDisagreeReason(reason);
                if (bankSettlementMapper.updateById(bankSettlement) > 0) {
                    return ApiResult.successOf("拒绝处理成功");
                }
                return ApiResult.failOf("拒绝处理失败");
            case 1:
                bankSettlement.setStatus(BankSettlementEnum.AGREE.getCode());
                bankSettlement.setDisagreeReason("");
                break;
            default:
                throw new BusinessException(String.format("Unknown operationCode %d !!", operationCode));
        }

        // 审批通过才做一下流程
        if (bankSettlementMapper.updateById(bankSettlement) > 0 && bankSettlement.getStatus().equals(1)) {
            log.debug("银行入驻申请审批成功!!");
            Bank bank = bankService.getByName(bankSettlement.getBankName());
            if (!ObjectUtils.isEmpty(bank)) {
                bankService.updateById(bank);
            }
            bank = new Bank();
            bank.setBankAddr(bankSettlement.getBankAddress())
                    .setBankName(bankSettlement.getBankName())
                    .setBankTel(bankSettlement.getBankTel())
                    .setCreator(JwtUtil.getUserNameFromRedis())
                    .setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            if (bankService.save(bank)) {

                log.debug("银行信息添加成功!!");

                bank = bankService.getByName(bankSettlement.getBankName());
                if (ObjectUtils.isEmpty(bank)) {
                    throw new BusinessException("获取银行信息失败");
                }

                String proposer = bankSettlement.getProposer();
                BackstageUser user = userService.getOne(new QueryWrapper<BackstageUser>().lambda()
                        .eq(BackstageUser::getUserName, proposer)
                        .and(wrapper -> wrapper.eq(BackstageUser::getValid, Boolean.TRUE))
                        .and(wrapper -> wrapper.eq(BackstageUser::getStatus, 1)));
                user.setBankId(bank.getId());

                userService.updateById(user);
                Integer[] roles = {RoleEnum.BANK_ADMIN.getRoleId(), RoleEnum.USER.getRoleId()};
                userService.updateUserRole(user.getId(), roles);

                // TODO: 2020/11/28 银行数据上链 ，修改链上入驻申请状态

            }

            return ApiResult.successOf("同意操作成功");
        }
        return ApiResult.failOf("同意操作失败");
    }

    /**
     * 查询入驻申请列表
     *
     * @param status  状态
     * @param current 当前页
     * @param size    页大小
     * @return BankSettlementListVO
     */
    @Override
    public ApiResult<Page<BankSettlementListVO>> getList(Integer status, Long current, Long size) {
        LambdaQueryWrapper<BankSettlement> queryWrapper = new QueryWrapper<BankSettlement>().lambda().eq(BankSettlement::getValid, Boolean.TRUE);
        if (!ObjectUtils.isEmpty(status)) {
            queryWrapper.and(wrapper -> wrapper.eq(BankSettlement::getStatus, status));
        }
        Page<BankSettlement> page = bankSettlementMapper.selectPage(new Page<>(current, size), queryWrapper);

        Page<BankSettlementListVO> blacklistPages = new Page<>(current, size);
        blacklistPages.setOrders(page.getOrders());
        blacklistPages.setTotal(page.getTotal());
        blacklistPages.setOrders(page.getOrders());

        if (CollectionUtils.isEmpty(page.getRecords())) {
            blacklistPages.setRecords(Collections.emptyList());
        }

        List<BankSettlementListVO> list = page.getRecords().stream().map((entity) -> {
            BankSettlementListVO settlementListVO = new BankSettlementListVO();
            settlementListVO.setBankName(entity.getBankName())
                    .setBankAddress(entity.getBankAddress())
                    .setBankLicense(entity.getBankLicense())
                    .setBankTel(entity.getBankTel())
                    .setId(entity.getId())
                    .setCreateTime(entity.getCreateTime())
                    .setStatus(entity.getStatus())
                    .setLegalPersonName(entity.getLegalPersonName())
                    .setLegalPersonIdCard(entity.getLegalPersonIdCard())
                    .setLegalPersonTel(entity.getLegalPersonTel())
                    .setDisagreeReason(entity.getDisagreeReason())
                    .setProposer(entity.getProposer());
            return settlementListVO;
        }).collect(Collectors.toList());
        blacklistPages.setRecords(list);
        return ApiResult.successOf(blacklistPages);
    }

    @Override
    public ApiResult<Page<BankSettlementListVO>> getMyList(Integer status, Long current, Long size) throws BusinessException {
        String username = JwtUtil.getUserNameFromRedis();
        LambdaQueryWrapper<BankSettlement> queryWrapper = new QueryWrapper<BankSettlement>().lambda()
                .eq(BankSettlement::getProposer, username)
                .eq(BankSettlement::getValid, Boolean.TRUE);
        if (!ObjectUtils.isEmpty(status)) {
            queryWrapper.and(wrapper -> wrapper.eq(BankSettlement::getStatus, status));
        }
        Page<BankSettlement> page = bankSettlementMapper.selectPage(new Page<>(current, size), queryWrapper);

        Page<BankSettlementListVO> blacklistPages = new Page<>(current, size);
        blacklistPages.setOrders(page.getOrders());
        blacklistPages.setTotal(page.getTotal());
        blacklistPages.setOrders(page.getOrders());

        if (CollectionUtils.isEmpty(page.getRecords())) {
            blacklistPages.setRecords(Collections.emptyList());
        }

        List<BankSettlementListVO> list = page.getRecords().stream().map((entity) -> {
            BankSettlementListVO settlementListVO = new BankSettlementListVO();
            settlementListVO.setBankName(entity.getBankName())
                    .setBankAddress(entity.getBankAddress())
                    .setBankLicense(entity.getBankLicense())
                    .setBankTel(entity.getBankTel())
                    .setId(entity.getId())
                    .setCreateTime(entity.getCreateTime())
                    .setStatus(entity.getStatus())
                    .setLegalPersonName(entity.getLegalPersonName())
                    .setLegalPersonIdCard(entity.getLegalPersonIdCard())
                    .setLegalPersonTel(entity.getLegalPersonTel())
                    .setDisagreeReason(entity.getDisagreeReason())
                    .setProposerIdCard(entity.getProposerIdCard())
                    .setProposerTel(entity.getProposerTel())
                    .setProposer(entity.getProposer());
            return settlementListVO;
        }).collect(Collectors.toList());
        blacklistPages.setRecords(list);
        return ApiResult.successOf(blacklistPages);
    }

    private BankSettlement dto2Entity(BankSettlementDTO apply) throws BusinessException {
        BankSettlement bankSettlement = new BankSettlement();
        UserInfoVO userInfoVO = JwtUtil.getUserFromRedis();
        if (!userInfoVO.getRealNameAuthentication()) {
            throw new BusinessException("请先实名之后再申请入驻");
        }
        if (!apply.getProposer().equals(userInfoVO.getUserName())) {
            throw new BusinessException("申请人与当前登录的用户不一致!!");
        }
        bankSettlement.setBankName(apply.getBankName())
                .setBankAddress(apply.getBankAddress())
                .setBankLicense(apply.getBankLicense())
                .setBankTel(apply.getBankTel())
                .setProposer(apply.getProposer())
                .setProposerIdCard(apply.getProposerIdCard())
                .setProposerTel(apply.getProposerTel())
                .setLegalPersonName(apply.getLegalPersonName())
                .setLegalPersonIdCard(apply.getLegalPersonIdCard())
                .setLegalPersonTel(apply.getLegalPersonTel())
                .setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        return bankSettlement;
    }
}
