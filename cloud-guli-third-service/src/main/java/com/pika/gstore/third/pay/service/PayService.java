package com.pika.gstore.third.pay.service;

import com.pika.gstore.common.to.pay.PayAsyncVo;
import com.pika.gstore.third.pay.service.impl.WebAliPayImpl;
import com.pika.gstore.third.pay.service.impl.WebUnionPayImpl;

/**
 * <p>聚合支付接口
 * <p>方法返回Object，可变形参Object... 调用方无法确定类型？因此需要doc文档.. </p>
 * <p>todo 方法签名类型改成具体一点更好?</p>
 *
 * @author pi'ka'chu
 */
public interface PayService {
    /**
     * Desc:支付前
     *
     * @param objects {@link Object}数组
     * @return {@link Object}数组
     */
    Object beforePay(Object... objects);


    /**
     * <div>
     *  Desc: 调用支付,传入特定参数,实现类进行具体参数处理(如:强制类型转换);<br>
     * 若未实现,调用时将抛出异常
     *
     * @param objects {@link Object}数组 <br>
     * @return {@link Object}数组   订单支付页面<br>
     * </div>
     * <br>
     * <li>实现类:{@link WebAliPayImpl}<br>
     * 参数: {@link java.util.Map}<br>
     * 返回: {@link String} 支付form表单,可直接返回进行跳转
     * </li>
     * <br>
     * <li>实现类:{@link WebUnionPayImpl}<br>
     * 参数: {@link java.util.Map}<br>
     * 返回: {@link String} 支付form表单,可直接返回进行跳转
     * </li>
     */
    Object doPay(Object... objects);

    /**
     * <div>
     *  Desc: 支付成功后后台回调
     *
     * @param objects {@link Object}数组 <br>
     * @return {@link Object}数组   <br>
     * </div>
     * <br>
     * <li>实现类:{@link WebAliPayImpl}<br>
     * 参数: <br>
     * -{@link javax.servlet.http.HttpServletRequest}<br>
     * -{@link PayAsyncVo}<br>
     * 返回: {@link String} 校验成功后在response中返回"success"并发送订单状态更新消，校验失败返回"failure"
     * </li>
     * <br>
     * <li>实现类:{@link WebUnionPayImpl}<br>
     * 参数: {@link javax.servlet.http.HttpServletRequest}<br>
     * 返回: {@link String} 校验成功后在response中返回"ok"并发送订单状态更新消息，返回给银联服务器http 200  状态码,校验失败返回"error"
     * </li>
     */
    Object afterPaidBackNotify(Object... objects);

    /**
     * <div>
     *  Desc: 支付成功后前台回调
     *
     * @param objects {@link Object}数组 <br>
     * @return {@link Object}数组   <br>
     * </div>
     *
     * <li>实现类:{@link WebUnionPayImpl}<br>
     * 参数: <br>
     * -{@link javax.servlet.http.HttpServletRequest}<br>
     * 返回: {@link Boolean} 校验是否成功,成功则发送订单状态更新消息
     * </li>
     */
    Object afterPaidFrontNotify(Object... objects);

    /**
     * Desc:检查支付状态
     *
     * @param objects {@link Object}数组
     * @return {@link Object}数组
     */
    Object checkPay(Object... objects);

    /**
     * Desc: 退款
     *
     * @param objects {@link Object}数组
     * @return {@link Object}数组
     */
    Object refund(Object... objects);
}
