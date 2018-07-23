package com.cmq.client.common.http;

import java.net.HttpURLConnection;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cmq.client.common.Constants;
import com.cmq.client.exception.CMQException;
import com.cmq.client.exception.RemotingException;

import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

public class HttpClient {

	private final OkHttpClient httpClient;

	public HttpClient(int poolSize, long timeoutInMillis) {
		final ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
		httpClient = new OkHttpClient.Builder()
				.connectTimeout(timeoutInMillis, TimeUnit.MILLISECONDS)
				.readTimeout(timeoutInMillis, TimeUnit.MILLISECONDS)
				.writeTimeout(timeoutInMillis, TimeUnit.MILLISECONDS)
				.connectionPool(new ConnectionPool(poolSize, 5, TimeUnit.MINUTES))
				.dispatcher(new Dispatcher(executorService))
				.build();
	}

	public HttpResponse syncRequest(final HttpRequest httpRequest) {
		try {
			final Request request = buildRequest(httpRequest);
			final Call call = httpClient.newCall(request);
			final Response response = call.execute();
			int statusCode = response.code();
			if (statusCode != HttpURLConnection.HTTP_OK) {
				throw new RemotingException(String.format("Request status not ok -> httpRequest:{},status:{}", httpRequest, statusCode));
			}
			String content = response.body().string();
			return new HttpResponse(content);
		} catch (Exception e) {
			CMQException exception;
			if (e instanceof CMQException) {
				exception = (CMQException) e;
			} else {
				exception = new RemotingException(String.format("Execute fail -> httpRequest:{}", httpRequest), e);
			}
			return new HttpResponse(exception);
		}
	}

	public void asyncRequest(final HttpRequest httpRequest, final HttpCallback callback) {
		final Request request = buildRequest(httpRequest);
		final Call call = httpClient.newCall(request);
		call.enqueue(callback);
	}

	private Request buildRequest(final HttpRequest httpRequest) {
		final Builder bodyBuilder = new FormBody.Builder(Constants.DEFAULT_CHARSET);
		httpRequest.getParams().forEach((key, val) -> {
			bodyBuilder.add(key, val);
		});
		return new Request.Builder()
				.url(httpRequest.getUrl())
				.post(bodyBuilder.build())
				.build();
	}
}
