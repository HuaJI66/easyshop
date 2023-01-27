package com.pika.gstore.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/24 20:19
 */
@Data
public class FareVo {
    private MemberAddressVo memberAddressVo;
    private BigDecimal fare;
}
