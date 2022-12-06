package com.pika.gstore.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/5 22:29
 */
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id;
    @NotNull
    private List<PurchaseDoneItemVo> items;
}
