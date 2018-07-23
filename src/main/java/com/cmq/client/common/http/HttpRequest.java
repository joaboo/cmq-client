package com.cmq.client.common.http;

import java.util.Map;

import com.cmq.client.common.util.Assert;

import lombok.Getter;
import lombok.ToString;

@ToString
public class HttpRequest {

	@Getter
	private String url;
	@Getter
	private Map<String, String> params;

	public HttpRequest(Builder builder) {
		this.url = builder.url;
		this.params = builder.params;
	}

	public static class Builder {
		String url;
		Map<String, String> params;

		public Builder url(String url) {
			Assert.notNull(url, "url == null");
			this.url = url;
			return this;
		}

		public Builder params(Map<String, String> params) {
			Assert.notEmpty(params, "params == null");
			this.params = params;
			return this;
		}

		public HttpRequest build() {
			return new HttpRequest(this);
		}
	}
}
