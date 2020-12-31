package com.example.guaranty.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/26 17:50
 **/
@Getter
@AllArgsConstructor
public enum BankSettlementEnum {
    // 银行入驻状态
    AGREE(1),
    WITHOUT_APPROVAL(0),
    DISAGREE(-1);

    private Integer code;
}
