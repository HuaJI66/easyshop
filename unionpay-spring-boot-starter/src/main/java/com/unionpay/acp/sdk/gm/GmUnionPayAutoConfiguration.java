/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 *
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 *
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       MPI基本参数工具类
 * =============================================================================
 */
package com.unionpay.acp.sdk.gm;

import com.pika.config.UnionPayAutoConfiguration;
import com.pika.properties.SDKConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
//@ConfigurationProperties(prefix = "acpsdk")
public class GmUnionPayAutoConfiguration extends UnionPayAutoConfiguration {

	private final static Logger logger = LoggerFactory.getLogger(GmUnionPayAutoConfiguration.class);

	/** 签名证书路径. */
	private String signCertPath;
	/** 签名证书密码. */
	private String signCertPwd;
	/** 签名证书类型. */
	private String signCertType;
	/** 加密公钥证书路径. */
	private String encryptCertPath;
	/** 加密公钥证书路径. */
	private String pinEncryptCertPath;
	/** 验证签名公钥证书目录. */
	private String validateCertDir;
	/** 按照商户代码读取指定签名证书目录. */
	private String signCertDir;
	/** 中级证书路径  */
	private String middleCertPath;
	/** 根证书路径  */
	private String rootCertPath;
	/** 是否验证验签证书CN，除了false都验  */
	private boolean ifValidateCNName = true;

	/** 配置文件中签名证书路径常量. */
	public static final String SDK_SIGNCERT_PATH = "acpsdk.sm2.signCert.path";
	/** 配置文件中签名证书密码常量. */
	public static final String SDK_SIGNCERT_PWD = "acpsdk.sm2.signCert.pwd";
	/** 配置文件中签名证书类型常量. */
	public static final String SDK_SIGNCERT_TYPE = "acpsdk.sm2.signCert.type";
	/** 配置文件中密码加密证书路径常量. */
	public static final String SDK_ENCRYPTCERT_PATH = "acpsdk.sm2.encryptCert.path";
	/** 配置文件中6.0.0统一支付产品加密pin证书路径常量. */
	public static final String SDK_PINENCRYPTCERT_PATH = "acpsdk.sm2.pinEncryptCert.path";
	/** 配置文件中磁道加密证书路径常量. */
	public static final String SDK_ENCRYPTTRACKCERT_PATH = "acpsdk.sm2.encryptTrackCert.path";
	/** 配置文件中磁道加密公钥模数常量. */
	public static final String SDK_ENCRYPTTRACKKEY_MODULUS = "acpsdk.sm2.encryptTrackKey.modulus";
	/** 配置文件中磁道加密公钥指数常量. */
	public static final String SDK_ENCRYPTTRACKKEY_EXPONENT = "acpsdk.sm2.encryptTrackKey.exponent";
	/** 配置文件中验证签名证书目录常量. */
	public static final String SDK_VALIDATECERT_DIR = "acpsdk.sm2.validateCert.dir";
	/** 配置文件中安全密钥 */
	public static final String SDK_SECURITYKEY = "acpsdk.sm2.secureKey";
	/** 配置文件中根证书路径常量  */
	public static final String SDK_ROOTCERT_PATH = "acpsdk.sm2.rootCert.path";
	/** 配置文件中根证书路径常量  */
	public static final String SDK_MIDDLECERT_PATH = "acpsdk.sm2.middleCert.path";
	/** 配置是否需要验证验签证书CN，除了false之外的值都当true处理 */
	public static final String SDK_IF_VALIDATE_CN_NAME = "acpsdk.ifValidateCNName";


	/** 操作对象. */
	private static GmUnionPayAutoConfiguration config = null;


	/**
	 * 获取config对象.
	 * @return
	 */
	public static SDKConfigProperties getConfig() {
		return UnionPayAutoConfiguration.getConfig();
	}

	public GmUnionPayAutoConfiguration(SDKConfigProperties sdkConfigProperties) {
		super(sdkConfigProperties);
	}
}
