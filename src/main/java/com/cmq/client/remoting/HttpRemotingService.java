package com.cmq.client.remoting;

import java.util.Map;

import com.cmq.client.common.http.HttpCallback;
import com.cmq.client.common.http.HttpClient;
import com.cmq.client.common.http.HttpRequest;
import com.cmq.client.common.http.HttpResponse;
import com.cmq.client.common.util.Assert;
import com.cmq.client.common.util.JsonUtils;
import com.cmq.client.exception.CMQException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRemotingService implements RemotingService {

	private final HttpClient httpClient;

	public HttpRemotingService(int poolSize, long timeoutInMillis) {
		this.httpClient = new HttpClient(poolSize, timeoutInMillis);
	}

	@Override
	public RemotingResponse call(RemotingRequest request) throws CMQException {
		final HttpRequest httpRequest = buildHttpRequest(request);
		final HttpResponse httpResponse = httpClient.syncRequest(httpRequest);
		return handleHttpResponse(httpResponse, request.getResponseType());
	}

	@Override
	public void asyncCall(RemotingRequest request, RemotingCallback callback) {
		Assert.notNull(callback, "RemotingCallback == null");
		final HttpRequest httpRequest = buildHttpRequest(request);
		httpClient.asyncRequest(httpRequest, new HttpCallback() {
			@Override
			public void response(HttpResponse httpResponse) {
				try {
					RemotingResponse response = handleHttpResponse(httpResponse, request.getResponseType());
					callback.onResponse(response);
				} catch (Throwable e) {
					callback.onFailure(e);
				}
			}
		});
	}

	private HttpRequest buildHttpRequest(RemotingRequest request) {
		Assert.notNull(request, "RemotingRequest == null");
		final String url = request.getUrl();
		final Map<String, String> params = request.getParameters();
		log.debug("Remoting call -> url:{},params:{}", url, params);
		final HttpRequest httpRequest = new HttpRequest.Builder()
				.url(url)
				.params(params)
				.build();
		return httpRequest;
	}

	private RemotingResponse handleHttpResponse(HttpResponse httpResponse, Class<? extends RemotingResponse> responseType) throws CMQException {
		if (httpResponse.getException() != null) {
			throw httpResponse.getException();
		} else {
			RemotingResponse response = JsonUtils.toBean(httpResponse.getContent(), responseType);
			response.resolveErrorCode();
			return response;
		}
	}
}
