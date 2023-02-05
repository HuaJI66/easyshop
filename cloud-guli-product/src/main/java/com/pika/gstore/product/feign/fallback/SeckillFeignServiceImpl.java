package com.pika.gstore.product.feign.fallback;

import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.product.feign.SeckillFeignService;
import org.springframework.stereotype.Service;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/5 19:25
 */
@Service
public class SeckillFeignServiceImpl implements SeckillFeignService {
    @Override
    public R getSkuSeckillInfo(String skuId) {
        return R.error(BaseException.TOO_MANY_REQUESTS.getCode(), BaseException.TOO_MANY_REQUESTS.getMsg());
    }
}
