package com.pika.gstore.order.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.constant.MqConstant;
import com.pika.gstore.common.constant.OrderConstant;
import com.pika.gstore.common.enums.OrderStatusEnum;
import com.pika.gstore.common.enums.PayType;
import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.exception.NoStockException;
import com.pika.gstore.common.to.MemberInfoTo;
import com.pika.gstore.common.to.OrderTo;
import com.pika.gstore.common.to.SkuHasStockVo;
import com.pika.gstore.common.to.SkuInfoTo;
import com.pika.gstore.common.to.es.SeckillOrderTo;
import com.pika.gstore.common.to.pay.PayVo;
import com.pika.gstore.common.to.pay.UnionPayVo;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.order.dao.OrderDao;
import com.pika.gstore.order.entity.OrderEntity;
import com.pika.gstore.order.entity.OrderItemEntity;
import com.pika.gstore.order.exception.OrderException;
import com.pika.gstore.order.feign.CartFeignService;
import com.pika.gstore.order.feign.MemberFeignService;
import com.pika.gstore.order.feign.ProductFeignService;
import com.pika.gstore.order.feign.WareFeignService;
import com.pika.gstore.order.interceptor.LoginInterceptor;
import com.pika.gstore.order.service.OrderItemService;
import com.pika.gstore.order.service.OrderService;
import com.pika.gstore.order.to.OrderCreateTo;
import com.pika.gstore.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


/**
 * @author pi'ka'chu
 */
@Service("orderService")
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Resource
    private MemberFeignService memberFeignService;
    @Resource
    private CartFeignService cartFeignService;
    @Resource
    private WareFeignService wareFeignService;
    @Resource
    private ThreadPoolExecutor executor;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ProductFeignService productFeignService;
    @Resource
    private OrderItemService orderItemService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        return new PageUtils(page(new Query<OrderEntity>().getPage(params), new QueryWrapper<>()
        ));
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        MemberInfoTo memberInfoTo = LoginInterceptor.threadLocal.get();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            //为异步任务线程设置request
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //1. 获取收获地址列表
            List<MemberAddressVo> address = memberFeignService.getMemberReceiveAddress(memberInfoTo.getId());
            Assert.notNull(address, "收货地址为空");
            confirmVo.setAddress(address);
        }, executor);

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //2.获取购物车(订单项)
            List<OrderItemVo> itemVos = cartFeignService.getCurrUserCart();
            Assert.notNull(itemVos, "订单项为空");
            confirmVo.setItems(itemVos);
        }, executor).thenRunAsync(() -> {
            //查询库存
            List<OrderItemVo> items = confirmVo.getItems();
            List<Long> list = items.stream().map(OrderItemVo::getSkuId).collect(Collectors.toList());
            R r = wareFeignService.getSkuHasStock(list);
            List<SkuHasStockVo> data = r.getData(new TypeReference<List<SkuHasStockVo>>() {
            });
            Assert.notEmpty(data, "库存查询为空");
            Map<Long, Boolean> map = data.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
            for (OrderItemVo item : items) {
                item.setHasStock(map.get(item.getSkuId()));
            }
        }, executor);

        CompletableFuture.allOf(future1, future2).get();

        //3. 优惠信息(用户积分)
        confirmVo.setIntegration(memberInfoTo.getIntegration() == null ? 0 : memberInfoTo.getIntegration());

        //4. 幂等性保证：防止重复令牌 + 数据库 OrderSn 唯一索引
        String token = IdUtil.fastSimpleUUID();
        confirmVo.setOrderToken(token);
        stringRedisTemplate.opsForValue().set(OrderConstant.ORDER_NUMBER_KEY + memberInfoTo.getId().toString(), token,
                Duration.ofMinutes(OrderConstant.ORDER_NUMBER_KEY_EXPIRE));
        return confirmVo;
    }

    @Override
//    @GlobalTransactional(name = "submitOrder",rollbackFor = Exception.class)
    @Transactional
    public OrderSubmitRepVo orderSubmit(OrderSubmitVo vo) {
        MemberInfoTo memberInfoTo = LoginInterceptor.threadLocal.get();
        OrderSubmitRepVo repVo = new OrderSubmitRepVo();
        repVo.setCode(0);
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(OrderConstant.ORDER_NUMBER_KEY + memberInfoTo.getId()),
                vo.getOrderToken());
        //令牌校验
        if (result != null && result == 1) {
            //1.创建订单
            OrderCreateTo order = createOrder(vo);
            //2.验价
            BigDecimal price = vo.getPayPrice();
            BigDecimal payAmount = order.getOrder().getPayAmount();
            if (Math.abs(price.subtract(payAmount).intValueExact()) < 0.01) {
                //验价通过
                saveOrder(order);
                WareSkuLockVo wareSkuLockVo = new WareSkuLockVo();
                wareSkuLockVo.setOrderSn(order.getOrder().getOrderSn());
                wareSkuLockVo.setLocks(order.getItems().stream().map(item -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setPrice(item.getSkuPrice());
                    orderItemVo.setTitle(item.getSkuName());
                    orderItemVo.setCount(item.getSkuQuantity());
                    orderItemVo.setSkuId(item.getSkuId());
                    return orderItemVo;
                }).collect(Collectors.toList()));
                //3.库存锁定
                R r = wareFeignService.lockStock(wareSkuLockVo);
                if (r.getCode() == 0) {
                    //锁定成功
                    repVo.setOrder(order.getOrder());
                    //4.扣减积分(模拟)
//                    int x = 10 / 0;
                    //5. 订单创建成功,发送消息
                    OrderTo to = new OrderTo();
                    BeanUtils.copyProperties(order.getOrder(), to);
                    rabbitTemplate.convertAndSend(MqConstant.ORDER_EVENT_EXCHANGE, MqConstant.ORDER_CREATE_KEY, to);
                } else {
                    throw new NoStockException(BaseException.WARE_STOCK_ERROR.getCode() + BaseException.WARE_STOCK_ERROR.getMsg());
                }
            } else {
                throw new RuntimeException(BaseException.ORDER_COMPUTE_PRICE_EXCEPTION.getCode() + BaseException.ORDER_COMPUTE_PRICE_EXCEPTION.getMsg());
            }
        } else {
            throw new RuntimeException(BaseException.ORDER_DEL_TOKEN_EXCEPTION.getCode() + BaseException.ORDER_DEL_TOKEN_EXCEPTION.getMsg());
        }
        return repVo;
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        return getOne(new LambdaQueryWrapper<OrderEntity>().eq(OrderEntity::getOrderSn, orderSn).last("limit 1"));
    }

    @Override
    public boolean closeOrder(OrderTo order) {
        OrderEntity orderDb = getById(order.getId());
        if (orderDb == null) {
            return true;
        } else if (orderDb.getStatus().equals(OrderStatusEnum.CREATE_NEW.getCode())) {
            order.setStatus(OrderStatusEnum.CANCELED.getCode());
            OrderEntity entity = new OrderEntity();
            entity.setId(order.getId());
            entity.setStatus(OrderStatusEnum.CANCELED.getCode());
            //发送解锁库存消息
            OrderTo to = new OrderTo();
            BeanUtils.copyProperties(order, to);
            rabbitTemplate.convertAndSend(MqConstant.ORDER_EVENT_EXCHANGE, MqConstant.ORDER_RELEASE_KEY, to);
            return updateById(entity);
        }
        return false;
    }

    @Override
    public Object getPayVo(String orderSn, Integer payType) {
        // TODO: 2023/3/29 返回通用payVo对象
        OrderEntity order = getOrderByOrderSn(orderSn);
        Integer status = order.getStatus();
        if (status.equals(OrderStatusEnum.CANCELED.getCode())) {
            throw new OrderException(BaseException.ORDER_STATUS_CANCEL_EXCEPTION.getCode(), BaseException.ORDER_STATUS_CANCEL_EXCEPTION.getMsg());
        } else if (status.equals(OrderStatusEnum.CREATE_NEW.getCode())) {
            OrderItemEntity one = orderItemService.getOne(new LambdaQueryWrapper<OrderItemEntity>()
                    .eq(OrderItemEntity::getOrderSn, orderSn).last("limit 1"));
            Object payVo = one;
            if (payType.equals(PayType.ALI_WEB.ordinal())) {
                payVo = new PayVo("", orderSn, one.getSkuName(), order.getPayAmount().setScale(2, RoundingMode.UP).toString(), "");
            } else if (payType.equals(PayType.UNIONPAY_WEB.ordinal())) {
                payVo = new UnionPayVo(orderSn, null,
                        String.valueOf(order.getPayAmount().multiply(BigDecimal.valueOf(100)).longValueExact()),
                        one.getSkuName(), OrderConstant.ORDER_NUMBER_KEY_EXPIRE
                );
            }

            System.out.println("payVo = " + payVo);
            return payVo;
        } else {
            throw new OrderException(BaseException.ORDER_STATUS_PAID_EXCEPTION.getCode(), BaseException.ORDER_STATUS_PAID_EXCEPTION.getMsg());
        }
    }

    @Override
    public PageUtils currUserOrderItemList(Map<String, Object> params) {
        MemberInfoTo memberInfoTo = LoginInterceptor.threadLocal.get();
        IPage<OrderEntity> page = page(new Query<OrderEntity>().getPage(params),
                new LambdaQueryWrapper<OrderEntity>().eq(OrderEntity::getMemberId, memberInfoTo.getId()).orderByDesc(OrderEntity::getId)
        );
        List<OrderEntity> collect = page.getRecords().stream().peek(item -> item.setItems(orderItemService.list(
                new LambdaQueryWrapper<OrderItemEntity>()
                        .eq(OrderItemEntity::getOrderSn, item.getOrderSn())
                        .orderByDesc(OrderItemEntity::getId)
        ))).collect(Collectors.toList());
        page.setRecords(collect);
        return new PageUtils(page);
    }

    @Override
    public void updateOrderStatus(String orderSn, Integer status) {
        update(new LambdaUpdateWrapper<OrderEntity>()
                .set(OrderEntity::getStatus, status)
                .eq(OrderEntity::getOrderSn, orderSn)
        );
    }

    /**
     * Desc: 保存订单
     *
     * @param order
     * @return
     */
    private void saveOrder(OrderCreateTo order) {
        OrderEntity orderEntity = order.getOrder();
        orderEntity.setModifyTime(new Date());
        save(orderEntity);
        orderItemService.saveBatch(order.getItems());
    }

    private OrderCreateTo createOrder(OrderSubmitVo vo) {
        OrderCreateTo createTo = new OrderCreateTo();
        FareVo fare = wareFeignService.getFare(vo.getAddrId());
        //1.订单号
        String orderSn = IdWorker.getTimeId();
        OrderEntity order = buildOrder(fare);
        order.setOrderSn(orderSn);
        createTo.setOrder(order);
        //运费
        createTo.setFare(fare.getFare());
        //2.获取所有订单项
        List<OrderItemEntity> orderItems = buildOrderItems(orderSn);
        createTo.setItems(orderItems);
        //3.验价
        computePrice(order, orderItems);
        return createTo;
    }

    /**
     * Desc:计算价格,积分信息
     *
     * @param order
     * @param orderItems
     * @return
     */
    private void computePrice(OrderEntity order, List<OrderItemEntity> orderItems) {
        //总价
        BigDecimal total = BigDecimal.ZERO;
        //优惠
        BigDecimal promotion = BigDecimal.ZERO, integration = BigDecimal.ZERO, coupon = BigDecimal.ZERO;
        BigDecimal growth = BigDecimal.ZERO, inte = BigDecimal.ZERO;
        for (OrderItemEntity orderItem : orderItems) {
            total = total.add(orderItem.getRealAmount());
            promotion = promotion.add(orderItem.getPromotionAmount());
            integration = integration.add(orderItem.getIntegrationAmount());
            coupon = coupon.add(orderItem.getCouponAmount());
            growth = growth.add(new BigDecimal(orderItem.getGiftGrowth()));
            inte = inte.add(new BigDecimal(orderItem.getGiftIntegration()));
        }
        order.setTotalAmount(total);
        order.setPayAmount(total.add(order.getFreightAmount()));
        order.setPromotionAmount(promotion);
        order.setIntegrationAmount(integration);
        order.setCouponAmount(coupon);
        //积分
        order.setIntegration(inte.intValue());
        order.setGrowth(growth.intValue());

        order.setDeleteStatus(0);
    }

    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        List<OrderItemVo> itemVos = cartFeignService.getCurrUserCart();
        Assert.notEmpty(itemVos, "购物车项为空");
        return itemVos.stream().map(item -> {
            OrderItemEntity orderItem = buildOrderItem(item);
            orderItem.setOrderSn(orderSn);
            return orderItem;
        }).collect(Collectors.toList());
    }

    private OrderEntity buildOrder(FareVo fare) {
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        order.setAutoConfirmDay(7);
        MemberAddressVo addressVo = fare.getMemberAddressVo();
        order.setMemberId(LoginInterceptor.threadLocal.get().getId());
        order.setReceiverCity(addressVo.getCity());
        order.setReceiverName(addressVo.getName());
        order.setReceiverPhone(addressVo.getPhone());
        order.setReceiverDetailAddress(addressVo.getDetailAddress());
        order.setReceiverPostCode(addressVo.getPostCode());
        order.setReceiverRegion(addressVo.getRegion());
        order.setReceiverProvince(addressVo.getProvince());
        //设置运费
        order.setFreightAmount(fare.getFare());
        return order;
    }

    private OrderItemEntity buildOrderItem(OrderItemVo item) {
        OrderItemEntity orderItem = new OrderItemEntity();
        //订单号pass
        //spu
        buildSpuToOrderItem(orderItem, item.getSkuId());

        //sku
        orderItem.setSkuId(item.getSkuId());
        orderItem.setSkuName(item.getTitle());
        orderItem.setSkuPic(item.getImage());
        orderItem.setSkuPrice(item.getPrice());
        orderItem.setSkuAttrsVals(StringUtils.collectionToDelimitedString(item.getSkuAttr(), ";"));
        orderItem.setSkuQuantity(item.getCount());
        //优惠pass
        //积分信息
        orderItem.setGiftGrowth(item.getTotalPrice().intValueExact());
        orderItem.setGiftIntegration(item.getTotalPrice().intValueExact());
        //价格
        orderItem.setPromotionAmount(BigDecimal.ZERO);
        orderItem.setIntegrationAmount(BigDecimal.ZERO);
        orderItem.setCouponAmount(BigDecimal.ZERO);
        orderItem.setRealAmount(item.getTotalPrice()
                .subtract(orderItem.getCouponAmount())
                .subtract(orderItem.getPromotionAmount())
                .subtract(orderItem.getIntegrationAmount())
        );
        return orderItem;
    }

    @Override
    public Boolean createSeckillOrder(SeckillOrderTo order) {
        // TODO: 2023/4/7 进一步完善秒杀订单信息
        //1. 订单
        OrderEntity entity = new OrderEntity();
        entity.setOrderSn(order.getOrderSn());
        entity.setMemberId(order.getMemberId());
        BigDecimal multiply = order.getSeckillPrice().multiply(BigDecimal.valueOf(order.getNum()));
        entity.setTotalAmount(multiply);
        entity.setPayAmount(multiply);
        entity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        Date date = new Date();
        entity.setModifyTime(date);
        entity.setCreateTime(date);
        entity.setDeleteStatus(0);
        save(entity);
        //2. 订单项
        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setRealAmount(multiply);
        orderItem.setOrderSn(order.getOrderSn());
        orderItem.setSkuQuantity(order.getNum());
        orderItem.setSkuPrice(order.getSeckillPrice());
        orderItem.setSkuId(order.getSkuId());
        // 进一步完善订单项信息
        try {
            List<String> saleAttrs = productFeignService.getSaleAttrs(orderItem.getSkuId());
            String skuAttrsVals = StringUtils.collectionToDelimitedString(saleAttrs, ";");
            orderItem.setSkuAttrsVals(skuAttrsVals);
            buildSpuToOrderItem(orderItem, order.getSkuId());
            buildSkuToOrderItem(orderItem, order.getSkuId());
        } catch (Exception ignored) {

        }
        orderItemService.save(orderItem);
        return Boolean.FALSE;
    }

    private void buildSkuToOrderItem(OrderItemEntity orderItem, Long skuId) {
        R r = productFeignService.getSkuById(skuId);
        if (r.getCode() == 0) {
            SkuInfoTo skuInfoTo = r.getData("skuInfo", new TypeReference<SkuInfoTo>() {
            });
            orderItem.setSkuPic(skuInfoTo.getSkuDefaultImg());
            orderItem.setSkuName(skuInfoTo.getSkuName());

        }
    }


    private void buildSpuToOrderItem(OrderItemEntity orderItem, Long skuId) {
        R r = productFeignService.getSpuBySkuId(skuId);
        if (r.getCode() == 0) {
            SpuInfoVo data = r.getData(new TypeReference<SpuInfoVo>() {
            });
            orderItem.setSpuId(data.getId());
            orderItem.setSpuBrand(data.getBrandId().toString());
            orderItem.setSpuName(data.getSpuName());
            orderItem.setCategoryId(data.getCatalogId());
        }
    }

    @Override
    public boolean delOrderByOrderSn(String orderSn) {
        return remove(new LambdaQueryWrapper<OrderEntity>().eq(OrderEntity::getOrderSn, orderSn)
                .and(item -> item.in(OrderEntity::getStatus, OrderStatusEnum.CREATE_NEW.getCode(), OrderStatusEnum.CANCELED.getCode())));
    }
}
