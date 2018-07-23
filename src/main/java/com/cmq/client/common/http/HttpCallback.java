package com.cmq.client.common.http;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.cmq.client.exception.CMQException;
import com.cmq.client.exception.RemotingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class HttpCallback implements Callback {

	@Override
	public void onResponse(Call call, Response response) throws IOException {
		try {
			int statusCode = response.code();
			if (statusCode != HttpURLConnection.HTTP_OK) {
				throw new RemotingException(String.format("Request status not ok -> httpRequest:{},status:{}", call.request(), statusCode));
			}
			String content = response.body().string();
			response(new HttpResponse(content));
		} catch (Exception e) {
			CMQException exception;
			if (e instanceof CMQException) {
				exception = (CMQException) e;
			} else {
				exception = new RemotingException(String.format("Execute fail -> httpRequest:{}", call.request()), e);
			}
			response(new HttpResponse(exception));
		}
	}

	@Override
	public void onFailure(Call call, IOException e) {
		RemotingException exception = new RemotingException(String.format("Execute fail -> httpRequest:{}", call.request()), e);
		response(new HttpResponse(exception));
	}

	public abstract void response(final HttpResponse httpResponse);
}
