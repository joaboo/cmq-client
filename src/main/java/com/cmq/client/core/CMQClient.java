package com.cmq.client.core;

import java.util.List;

import com.cmq.client.common.Constants;
import com.cmq.client.config.ClientConfig;
import com.cmq.client.core.bean.CreateQueueRequest;
import com.cmq.client.core.bean.CreateQueueResponse;
import com.cmq.client.core.bean.CreateTopicRequest;
import com.cmq.client.core.bean.CreateTopicResponse;
import com.cmq.client.core.bean.DeleteMessageRequest;
import com.cmq.client.core.bean.DeleteMessageResponse;
import com.cmq.client.core.bean.GetQueueAttributesRequest;
import com.cmq.client.core.bean.GetQueueAttributesResponse;
import com.cmq.client.core.bean.GetSubscriptionAttributesRequest;
import com.cmq.client.core.bean.GetSubscriptionAttributesResponse;
import com.cmq.client.core.bean.GetTopicAttributesRequest;
import com.cmq.client.core.bean.GetTopicAttributesResponse;
import com.cmq.client.core.bean.PublishMessageRequest;
import com.cmq.client.core.bean.PublishMessageResponse;
import com.cmq.client.core.bean.ReceiveMessageRequest;
import com.cmq.client.core.bean.ReceiveMessageResponse;
import com.cmq.client.core.bean.SubscribeRequest;
import com.cmq.client.core.bean.SubscribeResponse;
import com.cmq.client.exception.CMQException;
import com.cmq.client.exception.ClientException;
import com.cmq.client.remoting.HttpRemotingService;
import com.cmq.client.remoting.RemotingService;

import lombok.Getter;

public class CMQClient {

	@Getter
	private final ClientConfig clientConfig;
	private final RemotingService remotingService;

	public CMQClient(final ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
		this.remotingService = new HttpRemotingService(clientConfig.getHttpPoolSize(), clientConfig.getHttpTimeoutInMillis());
	}

	public CreateQueueResponse createQueue(final String queueName) {
		try {
			CreateQueueRequest request = new CreateQueueRequest(clientConfig);
			request.setResponseType(CreateQueueResponse.class);
			request.setAction(Constants.ACTION_CREATEQUEUE);
			request.setQueueName(queueName);
			return (CreateQueueResponse) remotingService.call(request);
		} catch (CMQException e) {
			throw new ClientException("createQueue error", e);
		}
	}

	public GetQueueAttributesResponse getQueueAttributes(final String queueName) {
		try {
			GetQueueAttributesRequest request = new GetQueueAttributesRequest(clientConfig);
			request.setResponseType(GetQueueAttributesResponse.class);
			request.setAction(Constants.ACTION_GETQUEUEATTRIBUTES);
			request.setQueueName(queueName);
			return (GetQueueAttributesResponse) remotingService.call(request);
		} catch (CMQException e) {
			throw new ClientException("getQueueAttributes error", e);
		}
	}

	public void batchReceiveMessage(final String queueName, final int numOfMsg, final int pollingWaitSeconds, final ClientCallback callback) {
		try {
			ReceiveMessageRequest request = new ReceiveMessageRequest(clientConfig);
			request.setResponseType(ReceiveMessageResponse.class);
			request.setAction(Constants.ACTION_BATCHRECEIVEMESSAGE);
			request.setQueueName(queueName);
			request.setNumOfMsg(numOfMsg);
			request.setPollingWaitSeconds(pollingWaitSeconds);
			remotingService.asyncCall(request, callback);
		} catch (Exception e) {
			throw new ClientException("batchReceiveMessage error", e);
		}
	}

	public void deleteMessage(final String queueName, final String receiptHandle, final ClientCallback callback) {
		try {
			DeleteMessageRequest request = new DeleteMessageRequest(clientConfig);
			request.setResponseType(DeleteMessageResponse.class);
			request.setAction(Constants.ACTION_DELETEMESSAGE);
			request.setQueueName(queueName);
			request.setReceiptHandle(receiptHandle);
			remotingService.asyncCall(request, callback);
		} catch (Exception e) {
			throw new ClientException("deleteMessage error", e);
		}
	}

	public CreateTopicResponse createTopic(final String topicName) {
		try {
			CreateTopicRequest request = new CreateTopicRequest(clientConfig);
			request.setResponseType(CreateTopicResponse.class);
			request.setAction(Constants.ACTION_CREATETOPIC);
			request.setTopicName(topicName);
			return (CreateTopicResponse) remotingService.call(request);
		} catch (CMQException e) {
			throw new ClientException("createTopic error", e);
		}
	}

	public GetTopicAttributesResponse getTopicAttributes(final String topicName) {
		try {
			GetTopicAttributesRequest request = new GetTopicAttributesRequest(clientConfig);
			request.setResponseType(GetTopicAttributesResponse.class);
			request.setAction(Constants.ACTION_GETTOPICATTRIBUTES);
			request.setTopicName(topicName);
			return (GetTopicAttributesResponse) remotingService.call(request);
		} catch (CMQException e) {
			throw new ClientException("getTopicAttributes error", e);
		}
	}

	public PublishMessageResponse publishMessage(final String topicName, final String routingKey, final String message) {
		try {
			PublishMessageRequest request = new PublishMessageRequest(clientConfig);
			request.setResponseType(PublishMessageResponse.class);
			request.setAction(Constants.ACTION_PUBLISHMESSAGE);
			request.setTopicName(topicName);
			request.setRoutingKey(routingKey);
			request.setMsgBody(message);
			return (PublishMessageResponse) remotingService.call(request);
		} catch (Exception e) {
			throw new ClientException("publishMessage error", e);
		}
	}

	public SubscribeResponse subscribe(final String subscriptionName, final String queueName, final String topicName, final List<String> bindingKeys) {
		try {
			SubscribeRequest request = new SubscribeRequest(clientConfig);
			request.setResponseType(SubscribeResponse.class);
			request.setAction(Constants.ACTION_SUBSCRIBE);
			request.setTopicName(topicName);
			request.setSubscriptionName(subscriptionName);
			request.setEndpoint(queueName);
			request.setBindingKeys(bindingKeys);
			return (SubscribeResponse) remotingService.call(request);
		} catch (CMQException e) {
			throw new ClientException("subscribe error", e);
		}
	}

	public GetSubscriptionAttributesResponse getSubscriptionAttributes(final String topicName, final String subscriptionName) {
		try {
			GetSubscriptionAttributesRequest request = new GetSubscriptionAttributesRequest(clientConfig);
			request.setResponseType(GetSubscriptionAttributesResponse.class);
			request.setAction(Constants.ACTION_GETSUBSCRIPTIONATTRIBUTES);
			request.setTopicName(topicName);
			request.setSubscriptionName(subscriptionName);
			return (GetSubscriptionAttributesResponse) remotingService.call(request);
		} catch (CMQException e) {
			throw new ClientException("getSubscriptionAttributes error", e);
		}
	}
}
