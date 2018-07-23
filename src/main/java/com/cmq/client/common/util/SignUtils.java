package com.cmq.client.common.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.cmq.client.common.Constants;
import com.cmq.client.exception.CMQException;

public final class SignUtils {
	private static final char[] b64c = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	private static String base64Encode(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length * 4);
		int len = data.length;
		int i = 0;
		int b1, b2, b3;
		while (i < len) {
			b1 = data[i++] & 0xff;
			if (i == len) {
				sb.append(b64c[b1 >>> 2]);
				sb.append(b64c[(b1 & 0x3) << 4]);
				sb.append('=');
				sb.append('=');
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len) {
				sb.append(b64c[b1 >>> 2]);
				sb.append(b64c[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
				sb.append(b64c[(b2 & 0x0f) << 2]);
				sb.append('=');
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(b64c[b1 >>> 2]);
			sb.append(b64c[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
			sb.append(b64c[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
			sb.append(b64c[b3 & 0x3f]);
		}
		return sb.toString();
	}

	public static String sign(String source, String secretKey, String signMethod) {
		Assert.notEmpty(source, "source == null");
		Assert.notEmpty(secretKey, "secretKey == null");
		Assert.notEmpty(signMethod, "signMethod == null");
		try {
			Mac mac = Mac.getInstance(signMethod);
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(Constants.DEFAULT_CHARSET), mac.getAlgorithm());
			mac.init(secretKeySpec);
			byte[] digest = mac.doFinal(source.getBytes(Constants.DEFAULT_CHARSET));
			return base64Encode(digest);
		} catch (Exception e) {
			throw new CMQException(String.format("SignException:[source:%s,secretKey:%s,signMethod:%s]", source, secretKey, signMethod), e);
		}
	}

}
