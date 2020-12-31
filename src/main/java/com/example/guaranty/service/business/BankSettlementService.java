package com.example.guaranty.service.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.handle.ApiResult;
import com.example.guaranty.dto.business.BankSettlementDTO;
import com.example.guaranty.entity.business.BankSettlement;
import com.example.guaranty.vo.business.BankSettlementListVO;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 银行入驻申请 Service
 *
 * @author m
 * @version 1.0.0
 * @date 2020-11-26 17:21:02
 **/
public interface BankSettlementService extends IService<BankSettlement> {

    /**
     * 银行入驻申请
     *
     * @param bankSettlementDTO 申请信息
     * @return res
     * @throws BusinessException e
     */
    ApiResult<String> createSettlementApplication(BankSettlementDTO bankSettlementDTO) throws BusinessException;

    /**
     * 银行入驻申请审批
     *
     * @param settlementId  申请ID
     * @param operationCode 操作码
     * @param reason        原因
     * @return res
     * @throws BusinessException e
     */
    ApiResult<String> settlementApproval(Integer settlementId, Integer operationCode, String reason) throws BusinessException;

    /**
     * 查询入驻申请列表
     *
     * @param status  状态
     * @param current 当前页
     * @param size    页大小
     * @return BankSettlementListVO
     */
    ApiResult<Page<BankSettlementListVO>> getList(Integer status, Long current, Long size);

    ApiResult<Page<BankSettlementListVO>> getMyList(Integer status, Long current, Long size) throws BusinessException;
}
