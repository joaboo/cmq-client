package com.cmq.client.core.bean;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.cmq.client.common.Constants.MQModel;
import com.cmq.client.common.util.Assert;
import com.cmq.client.common.util.DateUtils;
import com.cmq.client.common.util.ObjectUtils;
import com.cmq.client.common.util.RandomUtils;
import com.cmq.client.common.util.SignUtils;
import com.cmq.client.config.ClientConfig;
import com.cmq.client.config.ClientConfig.UrlMeta;
import com.cmq.client.remoting.RemotingRequest;
import com.cmq.client.remoting.RemotingResponse;

import lombok.Data;

@Data
public abstract class Request implements RemotingRequest {

	// 具体操作的指令接口名称
	private String action;
	// 地域参数
	private String region;
	// 发起 API 请求的时间
	private Long timestamp;
	// 随机正整数,用于防止重放攻击
	private Integer nonce;
	// 密钥
	private String secretId;
	// 请求签名
	private String signature;
	// 签名方式(HmacSHA256,HmacSHA1)
	private String signatureMethod;
	// 临时证书所用的 Token(长期密钥不需要 Token)
	private String token;

	// 请求终端信息
	private UrlMeta urlMeta;
	// 签名KEY
	private String secretKey;
	// 返回类型
	private Class<? extends RemotingResponse> responseType;

	protected Request(ClientConfig clientConfig, MQModel mqModel) {
		this.urlMeta = clientConfig.getUrl(mqModel);
		Assert.notNull(urlMeta, "urlMeta == null");
		this.signatureMethod = clientConfig.getSignatureMethod();
		this.secretId = clientConfig.getSecretId();
		this.secretKey = clientConfig.getSecretKey();
		this.timestamp = DateUtils.unixTimestamp(System.currentTimeMillis());
		this.nonce = RandomUtils.randomInt();
	}

	@Override
	public String getUrl() {
		return urlMeta.getUrl();
	}

	@Override
	public Map<String, String> getParameters() {
		baseValid();
		valid();
		final TreeMap<String, String> params = baseParams();
		addParams(params);
		addSign(params);
		return params;
	}

	protected abstract void valid();

	private void baseValid() {
		Assert.notEmpty(action, "action == null");
		Assert.notNull(timestamp, "timestamp == null");
		Assert.notNull(nonce, "nonce == null");
		Assert.notEmpty(signatureMethod, "signatureMethod == null");
		Assert.notEmpty(secretId, "secretId == null");
		Assert.notEmpty(secretKey, "secretKey == null");
	}

	protected abstract void addParams(final TreeMap<String, String> params);

	private TreeMap<String, String> baseParams() {
		final TreeMap<String, String> params = new TreeMap<>();
		// put(params, "Signature", signature);
		put(params, "SignatureMethod", signatureMethod);
		put(params, "Token", token);
		put(params, "Action", action);
		put(params, "Region", region);
		put(params, "Timestamp", timestamp);
		put(params, "Nonce", nonce);
		put(params, "SecretId", secretId);
		return params;
	}

	private void addSign(final TreeMap<String, String> params) {
		StringBuilder source = new StringBuilder(128);
		source.append(urlMeta.getHttpMethod()).append(urlMeta.getHost()).append(urlMeta.getPath()).append('?');
		for (Iterator<Entry<String, String>> it = params.entrySet().iterator(); it.hasNext();) {
			Entry<String, String> entry = it.next();
			source.append(entry.getKey()).append('=').append(entry.getValue());
			if (it.hasNext()) {
				source.append('&');
			}
		}
		signature = SignUtils.sign(source.toString(), secretKey, signatureMethod);
		put(params, "Signature", signature);
	}

	protected void put(final TreeMap<String, String> params, final String key, final Object val) {
		if (val == null) {
			return;
		}
		if (ObjectUtils.isEmpty(key)) {
			return;
		}
		if (params == null) {
			return;
		}
		String valStr = ObjectUtils.toString(val);
		if (ObjectUtils.isNotEmpty(valStr)) {
			params.put(key, valStr);
		}
	}
}
