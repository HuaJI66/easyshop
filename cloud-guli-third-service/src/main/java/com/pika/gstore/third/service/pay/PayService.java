package com.pika.gstore.third.service.pay;

/**
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
     *
     * <div>实现类:{@link com.pika.gstore.third.service.pay.impl.WebAliPayServiceImpl}<br>
     * 参数: {@link com.pika.gstore.third.vo.PayVo}<br>
     * 返回: {@link String} 支付form表单,可直接返回进行跳转
     * </div>
     */
    Object doPay(Object... objects);

    /**
     * <div>
     *  Desc: 支付成功后回调
     *
     * @param objects {@link Object}数组 <br>
     * @return {@link Object}数组   <br>
     * </div>
     *
     * <div>实现类:{@link com.pika.gstore.third.service.pay.impl.WebAliPayServiceImpl}<br>
     * 参数: <br>
     * -{@link javax.servlet.http.HttpServletRequest}<br>
     * -{@link com.pika.gstore.third.vo.PayAsyncVo}<br>
     * 返回: {@link String} 校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
     * </div>
     */
    Object afterPaidNotify(Object... objects);

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
