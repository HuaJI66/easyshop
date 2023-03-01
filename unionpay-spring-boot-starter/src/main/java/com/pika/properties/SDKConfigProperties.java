package com.pika.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/3/1 9:22
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "union-pay.acpsdk")
public class SDKConfigProperties {
    /**
     * 是否启用自动注入UnionPayTemplate
     */
    private boolean enable;
    private String encoding = "UTF-8";

    /**
     * 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
     */
    private String merId;
    /**
     * 接入类型，0：直连商户
     */
    private String accessType = "0";


    /**
     * 前台请求URL.
     */
    private String frontRequestUrl;

    /**
     * 后台请求URL.
     */
    private String backRequestUrl;
    /**
     * 二维码统一下单请求URL.
     */
    private String orderRequestUrl;
    /**
     * 单笔查询
     */
    private String singleQueryUrl;
    /**
     * 批量查询
     */
    private String batchQueryUrl;
    /**
     * 批量交易
     */
    private String batchTransUrl;
    /**
     * 文件传输
     */
    private String fileTransUrl;
    /**
     * 签名证书路径.
     */
    private String signCertPath;
    /**
     * 签名证书密码.
     */
    private String signCertPwd;
    /**
     * 签名证书类型.
     */
    private String signCertType;
    /**
     * 加密公钥证书路径.
     */
    private String encryptCertPath;
    /**
     * 验证签名公钥证书目录.
     */
    private String validateCertDir;
    /**
     * 按照商户代码读取指定签名证书目录.
     */
    private String signCertDir;
    /**
     * 磁道加密证书路径
     */
    private String encryptTrackCertPath;
    /**
     * 磁道加密公钥模数.
     */
    private String encryptTrackKeyModulus;
    /**
     * 磁道加密公钥指数.
     */
    private String encryptTrackKeyExponent;
    /**
     * 6.0.0统一支付产品加密pin公钥证书路径.
     */
    private String pinEncryptCertPath;
    /**
     * 有卡交易.
     */
    private String cardRequestUrl;
    /**
     * app交易
     */
    private String appRequestUrl;
    /**
     * 证书使用模式(单证书/多证书)
     */
    private String singleMode;
    /**
     * 安全密钥(SHA256和SM3计算时使用)
     */
    private String secureKey;
    /**
     * 中级证书路径
     */
    private String middleCertPath;
    /**
     * 根证书路径
     */
    private String rootCertPath;
    /**
     * 是否验证验签证书CN，除了false都验
     */
    private boolean ifValidateCNName = true;
    /**
     * 是否验证https证书，默认都不验
     */
    private boolean ifValidateRemoteCert = false;
    /**
     * signMethod，没配按01吧
     */
    private String signMethod = "01";
    /**
     * version，没配按5.0.0
     */
    private String version = "5.0.0";
    /**
     * 前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
     * 如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
     * 异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交
     */
    private String frontUrl;
    /**
     * 后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
     * 后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知<br>
     * 注意:<br>
     * 1.需设置为外网能访问，否则收不到通知<br>
     * 2.http https均可<br>
     * 3.收单后台通知后需要10秒内返回http200或302状态码<br>
     * 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。<br>
     * 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败<br>
     */
    private String backUrl;
    /**
     * frontFailUrl
     */
    private String frontFailUrl;

    /*缴费相关地址*/
    private String jfFrontRequestUrl;
    private String jfBackRequestUrl;
    private String jfSingleQueryUrl;
    private String jfCardRequestUrl;
    private String jfAppRequestUrl;

    //二维码
    private String qrcBackTransUrl;
    private String qrcB2cIssBackTransUrl;
    private String qrcB2cMerBackTransUrl;
    private String qrcB2cMerBackSynTransUrl;

    //综合认证
    private String zhrzFrontRequestUrl;
    private String zhrzBackRequestUrl;
    private String zhrzSingleQueryUrl;
    private String zhrzCardRequestUrl;
    private String zhrzAppRequestUrl;
    private String zhrzFaceRequestUrl;

    /**
     * acp6
     */
    private String transUrl;


    /**
     * 配置文件中的前台URL常量.
     */
    private String frontTransUrl;
    public static final String SDK_FRONT_URL = "union-pay.acpsdk.frontTransUrl";
    /**
     * 配置文件中的后台URL常量.
     */
    private String backTransUrl;
    public static final String SDK_BACK_URL = "union-pay.acpsdk.backTransUrl";
    /**
     * 配置文件中的统一下单URL常量.
     */
    private String orderTransUrl;
    public static final String SDK_ORDER_URL = "union-pay.acpsdk.orderTransUrl";
    /**
     * 配置文件中的单笔交易查询URL常量.
     */
    public static final String SDK_SIGNQ_URL = "union-pay.acpsdk.singleQueryUrl";
    /**
     * 配置文件中的批量交易查询URL常量.
     */
    public static final String SDK_BATQ_URL = "union-pay.acpsdk.batchQueryUrl";
    /**
     * 配置文件中的批量交易URL常量.
     */
    public static final String SDK_BATTRANS_URL = "union-pay.acpsdk.batchTransUrl";
    /**
     * 配置文件中的文件类交易URL常量.
     */
    public static final String SDK_FILETRANS_URL = "union-pay.acpsdk.fileTransUrl";
    /**
     * 配置文件中的有卡交易URL常量.
     */
    private String cardTransUrl;
    public static final String SDK_CARD_URL = "union-pay.acpsdk.cardTransUrl";
    /**
     * 配置文件中的app交易URL常量.
     */
    private String appTransUrl;
    public static final String SDK_APP_URL = "union-pay.acpsdk.appTransUrl";
    //以下缴费产品使用，其余产品用不到，无视即可
    /**
     * 前台请求地址
     */
    public static final String JF_SDK_FRONT_TRANS_URL = "union-pay.acpsdk.jfFrontTransUrl";
    /**
     * 后台请求地址
     */
    public static final String JF_SDK_BACK_TRANS_URL = "union-pay.acpsdk.jfBackTransUrl";
    /**
     * 单笔查询请求地址
     */
    public static final String JF_SDK_SINGLE_QUERY_URL = "union-pay.acpsdk.jfSingleQueryUrl";
    /**
     * 有卡交易地址
     */
    public static final String JF_SDK_CARD_TRANS_URL = "union-pay.acpsdk.jfCardTransUrl";
    /**
     * App交易地址
     */
    public static final String JF_SDK_APP_TRANS_URL = "union-pay.acpsdk.jfAppTransUrl";
    /**
     * 人到人
     */
    public static final String QRC_BACK_TRANS_URL = "union-pay.acpsdk.qrcBackTransUrl";
    /**
     * 人到人
     */
    public static final String QRC_B2C_ISS_BACK_TRANS_URL = "union-pay.acpsdk.qrcB2cIssBackTransUrl";
    /**
     * 人到人
     */
    public static final String QRC_B2C_MER_BACK_TRANS_URL = "union-pay.acpsdk.qrcB2cMerBackTransUrl";

    public static final String QRC_B2C_MER_BACK_SYN_TRANS_URL = "union-pay.acpsdk.qrcB2cMerBackSynTransUrl";

    //以下综合认证产品使用，其余产品用不到，无视即可
    /**
     * 前台请求地址
     */
    public static final String ZHRZ_SDK_FRONT_TRANS_URL = "union-pay.acpsdk.zhrzFrontTransUrl";

    /**
     * 后台请求地址
     */
    public static final String ZHRZ_SDK_BACK_TRANS_URL = "union-pay.acpsdk.zhrzBackTransUrl";
    /**
     * 单笔查询请求地址
     */
    public static final String ZHRZ_SDK_SINGLE_QUERY_URL = "union-pay.acpsdk.zhrzSingleQueryUrl";
    /**
     * 有卡交易地址
     */
    public static final String ZHRZ_SDK_CARD_TRANS_URL = "union-pay.acpsdk.zhrzCardTransUrl";
    /**
     * App交易地址
     */
    public static final String ZHRZ_SDK_APP_TRANS_URL = "union-pay.acpsdk.zhrzAppTransUrl";
    /**
     * 图片识别交易地址
     */
    public static final String ZHRZ_SDK_FACE_TRANS_URL = "union-pay.acpsdk.zhrzFaceTransUrl";

    /**
     * acp6
     */
    public static final String TRANS_URL = "union-pay.acpsdk.transUrl";

    /**
     * 配置文件中签名证书路径常量.
     */
    public static final String SDK_SIGNCERT_PATH = "union-pay.acpsdk.signCert.path";
    /**
     * 配置文件中签名证书密码常量.
     */
    public static final String SDK_SIGNCERT_PWD = "union-pay.acpsdk.signCert.pwd";
    /**
     * 配置文件中签名证书类型常量.
     */
    public static final String SDK_SIGNCERT_TYPE = "union-pay.acpsdk.signCert.type";
    /**
     * 配置文件中加密证书路径常量.
     */
    public static final String SDK_ENCRYPTCERT_PATH = "union-pay.acpsdk.encryptCert.path";
//	/** 配置文件中磁道加密证书路径常量. */
//	public static final String SDK_ENCRYPTTRACKCERT_PATH = "union-pay.acpsdk.encryptTrackCert.path";
    /**
     * 配置文件中5.0.0有卡产品磁道加密公钥模数常量.
     */
    public static final String SDK_ENCRYPTTRACKKEY_MODULUS = "union-pay.acpsdk.encryptTrackKey.modulus";
    /**
     * 配置文件中5.0.0有卡产品磁道加密公钥指数常量.
     */
    public static final String SDK_ENCRYPTTRACKKEY_EXPONENT = "union-pay.acpsdk.encryptTrackKey.exponent";
    /**
     * 配置文件中验证签名证书目录常量.
     */
    public static final String SDK_VALIDATECERT_DIR = "union-pay.acpsdk.validateCert.dir";
    /**
     * 配置文件中6.0.0统一支付产品加密pin证书路径常量.
     */
    public static final String SDK_PINENCRYPTCERT_PATH = "union-pay.acpsdk.pinEncryptCert.path";

    /**
     * 配置文件中是否加密cvn2常量.
     */
    public static final String SDK_CVN_ENC = "union-pay.acpsdk.cvn2.enc";
    /**
     * 配置文件中是否加密cvn2有效期常量.
     */
    public static final String SDK_DATE_ENC = "union-pay.acpsdk.date.enc";
    /**
     * 配置文件中是否加密卡号常量.
     */
    public static final String SDK_PAN_ENC = "union-pay.acpsdk.pan.enc";
    /**
     * 配置文件中证书使用模式
     */
    public static final String SDK_SINGLEMODE = "union-pay.acpsdk.singleMode";
    /**
     * 配置文件中安全密钥
     */
    public static final String SDK_SECURITYKEY = "union-pay.acpsdk.secureKey";
    /**
     * 配置文件中根证书路径常量
     */
    public static final String SDK_ROOTCERT_PATH = "union-pay.acpsdk.rootCert.path";
    /**
     * 配置文件中根证书路径常量
     */
    public static final String SDK_MIDDLECERT_PATH = "union-pay.acpsdk.middleCert.path";
    /**
     * 配置是否需要验证验签证书CN，除了false之外的值都当true处理
     */
    public static final String SDK_IF_VALIDATE_CN_NAME = "union-pay.acpsdk.ifValidateCNName";
    /**
     * 配置是否需要验证https证书，除了true之外的值都当false处理
     */
    public static final String SDK_IF_VALIDATE_REMOTE_CERT = "union-pay.acpsdk.ifValidateRemoteCert";
    /**
     * signmethod
     */
    public static final String SDK_SIGN_METHOD = "union-pay.acpsdk.signMethod";
    /**
     * version
     */
    public static final String SDK_VERSION = "union-pay.acpsdk.version";
    /**
     * 后台通知地址
     */
    public static final String SDK_BACKURL = "union-pay.acpsdk.backUrl";
    /**
     * 前台通知地址
     */
    public static final String SDK_FRONTURL = "union-pay.acpsdk.frontUrl";
    /**
     * 前台失败通知地址
     */
    public static final String SDK_FRONT_FAIL_URL = "union-pay.acpsdk.frontFailUrl";

    public String getFrontRequestUrl() {
        return frontTransUrl;
    }
}
