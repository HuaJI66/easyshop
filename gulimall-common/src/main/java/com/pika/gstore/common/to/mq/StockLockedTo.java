package com.pika.gstore.common.to.mq;

import lombok.Data;

import java.io.Serializable;


/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/29 21:06
 */
@Data
public class StockLockedTo implements Serializable {
    private Long taskId;
    private WareOrderTaskDetailTo detail;
}
