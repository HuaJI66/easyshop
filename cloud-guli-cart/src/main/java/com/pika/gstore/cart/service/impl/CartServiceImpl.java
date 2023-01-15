package com.pika.gstore.cart.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.pika.gstore.cart.feign.ProductFeignService;
import com.pika.gstore.cart.interceptor.LoginInterceptor;
import com.pika.gstore.cart.service.CartService;
import com.pika.gstore.cart.to.UserInfoTo;
import com.pika.gstore.cart.vo.CartItemVo;
import com.pika.gstore.cart.vo.CartVo;
import com.pika.gstore.common.constant.CartConstant;
import com.pika.gstore.common.to.SkuInfoTo;
import com.pika.gstore.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 12:51
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ProductFeignService productFeignService;
    @Resource
    private ThreadPoolExecutor executor;

    @Override
    public CartItemVo addToCart(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String key = cartOps.getKey();
        log.warn("key:{}", key);
        String res = (String) cartOps.get(skuId.toString());
        //缓存购物车中已存在
        if (!StringUtils.isEmpty(res)) {
            CartItemVo cartItemVo = JSONUtil.toBean(res, new TypeReference<CartItemVo>() {
            }, false);
            cartItemVo.setCount(cartItemVo.getCount() + num);
            cartItemVo.setTotalPrice(cartItemVo.getTotalPrice());
            cartOps.put(skuId.toString(), JSONUtil.toJsonStr(cartItemVo));
            return cartItemVo;
        }
        CartItemVo cartItemVo = new CartItemVo();
        cartItemVo.setCount(num);
        cartItemVo.setSkuId(skuId);
        cartItemVo.setChecked(true);
        //1.获取skuInfo
        CompletableFuture<Void> async = CompletableFuture.runAsync(() -> {
            R r = productFeignService.info(skuId);
            SkuInfoTo skuInfoTo = r.getData("skuInfo", new TypeReference<SkuInfoTo>() {
            });
            cartItemVo.setTitle(skuInfoTo.getSkuTitle());
            cartItemVo.setImage(skuInfoTo.getSkuDefaultImg());
            cartItemVo.setPrice(skuInfoTo.getPrice());
        });

        //2.获取spu分类属性组合
        CompletableFuture<Void> async1 = CompletableFuture.runAsync(() -> {
            List<String> saleAttrs = productFeignService.getSaleAttrs(skuId);
            cartItemVo.setSkuAttr(saleAttrs);
        }, executor);

        //3.缓存数据
        try {
            CompletableFuture.allOf(async, async1).get();
            cartItemVo.setTotalPrice(cartItemVo.getTotalPrice());
            cartOps.put(cartItemVo.getSkuId().toString(), JSONUtil.toJsonStr(cartItemVo));
            cartOps.expire(1, TimeUnit.DAYS);
            return cartItemVo;
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public CartItemVo getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> ops = getCartOps();
        String res = (String) ops.get( skuId.toString());
        String key = ops.getKey();
        log.info("key:{}", key);
        return StringUtils.isEmpty(res) ? null : JSONUtil.toBean(res, new TypeReference<CartItemVo>() {
        }, false);
    }

    @Override
    public CartVo getCart() {
        UserInfoTo userInfoTo = LoginInterceptor.threadLocal.get();
        CartVo cartVo = new CartVo();
        if (userInfoTo.getUserId() != null) {
            //已登录==>合并未登录时的购物车(若存在)
            List<CartItemVo> cartItems = getCartItems(CartConstant.CACHE_CART_PREFIX + userInfoTo.getUserKey());
            if (cartItems != null && cartItems.size() > 0) {
                for (CartItemVo cartItem : cartItems) {
                    addToCart(cartItem.getSkuId(), cartItem.getCount());
                }
                //删除临时购物车
                clearCart(CartConstant.CACHE_CART_PREFIX + userInfoTo.getUserKey());
            }
            List<CartItemVo> list = getCartItems(CartConstant.CACHE_CART_PREFIX + userInfoTo.getUserId());
            cartVo.setCartItems(list);
        } else {
            //未登录
            List<CartItemVo> cartItems = getCartItems(CartConstant.CACHE_CART_PREFIX + userInfoTo.getUserKey());
            cartVo.setCartItems(cartItems);
        }
        return cartVo;
    }

    @Override
    public void clearCart(String cartId) {
        stringRedisTemplate.delete(cartId);
    }

    @Override
    public void changeState(Long skuId, Boolean checkedState) {
        BoundHashOperations<String, Object, Object> ops = getCartOps();
        String str = (String) ops.get(skuId.toString());
        CartItemVo cartItemVo = JSONUtil.toBean(str, new TypeReference<CartItemVo>() {
        }, false);
        cartItemVo.setChecked(checkedState==null?cartItemVo.getChecked():checkedState);
        ops.put(skuId.toString(), JSONUtil.toJsonStr(cartItemVo));
    }

    @Override
    public void changeNum(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> ops = getCartOps();
        String str = (String) ops.get(skuId.toString());
        CartItemVo cartItemVo = JSONUtil.toBean(str, new TypeReference<CartItemVo>() {
        }, false);
        cartItemVo.setCount(num);
        ops.put(skuId.toString(), JSONUtil.toJsonStr(cartItemVo));
    }

    @Override
    public void deleteCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> ops = getCartOps();
        ops.delete(skuId.toString());
    }

    private List<CartItemVo> getCartItems(String cartKey) {
        if (StringUtils.isEmpty(cartKey)) {
            return null;
        }
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = ops.values();
        if (values != null && values.size() > 0) {
            return values.stream().map(i -> JSONUtil.toBean((String) i, new TypeReference<CartItemVo>() {
            }, false)).collect(Collectors.toList());
        }
        return null;
    }

    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = LoginInterceptor.threadLocal.get();
        return userInfoTo.getUserId() == null
                ? stringRedisTemplate.boundHashOps(CartConstant.CACHE_CART_PREFIX + userInfoTo.getUserKey())
                : stringRedisTemplate.boundHashOps(CartConstant.CACHE_CART_PREFIX + userInfoTo.getUserId());
    }
}
