package com.pika.gstore.ware.vo;

import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/5 22:35
 */
@Data
public class PurchaseDoneItemVo {
    private Long itemId;
    private int status;
    private String reason;
}
