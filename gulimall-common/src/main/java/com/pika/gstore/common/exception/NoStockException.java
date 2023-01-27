package com.pika.gstore.common.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/26 16:52
 */
@Data
@NoArgsConstructor
public class NoStockException extends RuntimeException{
    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品id: "+skuId+" 库存不足");
    }

    public NoStockException(String message) {
        super(message);
    }
}
