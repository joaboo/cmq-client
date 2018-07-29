package com.cmq.client.common;

import java.nio.charset.Charset;

public class Constants {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public static final String PROTOCOL_HTTP = "http";
	public static final String PROTOCOL_HTTPS = "https";

	public static final String ENDPOINT_QUEUE_OUTER = "cmq-queue-%s.api.qcloud.com";
	public static final String ENDPOINT_QUEUE_INNER = "cmq-queue-%s.api.tencentyun.com";
	public static final String ENDPOINT_TOPIC_OUTER = "cmq-topic-%s.api.qcloud.com";
	public static final String ENDPOINT_TOPIC_INNER = "cmq-topic-%s.api.tencentyun.com";
	public static final String ENDPOINT_PATH = "/v2/index.php";

	public static final String HTTP_METHOD_POST = "POST";
	public static final String HTTP_METHOD_GET = "GET";

	public static final String SIGN_METHOD_SHA1 = "HmacSHA1";
	public static final String SIGN_METHOD_SHA256 = "HmacSHA256";

	public static final long DEFAULT_HTTP_TIMEOUT = 3000L;
	public static final int DEFAULT_HTTP_POOL_SIZE = 5;

	public static final int DEFAULT_CONSUMER_THREADS = 1;
	public static final int DEFAULT_CONSUMER_QUEUES = 100;

	public static final long DEFAULT_PULL_INTERVAL = 1000L;
	public static final int DEFAULT_RECEIVE_NUM_OF_MSG = 16; // 消费的消息数量 1-16
	public static final int DEFAULT_POLLING_WAIT_SECONDS = 0; // 请求的长轮询等待时间0-30

	/** 队列模型 **/
	/** 队列相关接口 **/
	public static final String ACTION_CREATEQUEUE = "CreateQueue"; // 创建队列
	public static final String ACTION_LISTQUEUE = "ListQueue"; // 获取队列列表
	public static final String ACTION_GETQUEUEATTRIBUTES = "GetQueueAttributes"; // 获取队列属性
	public static final String ACTION_SETQUEUEATTRIBUTES = "SetQueueAttributes"; // 修改队列属性
	public static final String ACTION_DELETEQUEUE = "DeleteQueue"; // 删除队列
	/** 消息相关接口 **/
	public static final String ACTION_SENDMESSAGE = "SendMessage"; // 发送消息
	public static final String ACTION_BATCHSENDMESSAGE = "BatchSendMessage"; // 批量发送消息
	public static final String ACTION_RECEIVEMESSAGE = "ReceiveMessage"; // 消费消息
	public static final String ACTION_BATCHRECEIVEMESSAGE = "BatchReceiveMessage"; // 批量消费消息
	public static final String ACTION_DELETEMESSAGE = "DeleteMessage"; // 删除消息
	public static final String ACTION_BATCHDELETEMESSAGE = "BatchDeleteMessage"; // 批量删除消息

	/** 主题模型 **/
	/** 主题相关接口 **/
	public static final String ACTION_CREATETOPIC = "CreateTopic"; // 创建主题
	public static final String ACTION_SETTOPICATTRIBUTES = "SetTopicAttributes"; // 修改主题属性
	public static final String ACTION_LISTTOPIC = "ListTopic"; // 获取主题列表
	public static final String ACTION_GETTOPICATTRIBUTES = "GetTopicAttributes"; // 获取主题属性
	public static final String ACTION_DELETETOPIC = "DeleteTopic"; // 删除主题
	/** 消息相关接口 **/
	public static final String ACTION_PUBLISHMESSAGE = "PublishMessage"; // 发布消息
	public static final String ACTION_BATCHPUBLISHMESSAGE = "BatchPublishMessage"; // 批量发布消息
	/** 订阅相关接口 **/
	public static final String ACTION_SUBSCRIBE = "Subscribe"; // 创建订阅
	public static final String ACTION_LISTSUBSCRIPTIONBYTOPIC = "ListSubscriptionByTopic"; // 获取订阅列表
	public static final String ACTION_SETSUBSCRIPTIONATTRIBUTES = "SetSubscriptionAttributes"; // 修改订阅属性
	public static final String ACTION_GETSUBSCRIPTIONATTRIBUTES = "GetSubscriptionAttributes"; // 获取订阅属性
	public static final String ACTION_UNSUBSCRIBE = "Unsubscribe"; // 删除订阅

	// 属性
	public static final String PROPERTY_PROTOCOL_QUEUE = "queue";
	public static final String PROPERTY_NOTIFYSTRATEGY = "BACKOFF_RETRY";
	public static final String PROPERTY_NOTIFYCONTENTFORMAT = "SIMPLIFIED";
	public static final String PROPERTY_BINDINGKEY_FORMAT = "bindingKey.%d";
	public static final int PROPERTY_FILTERTYPE_BINDINGKEY = 2; // 表示用户使用bindingKey过滤

	// 消息模型
	public static enum MQModel {
		QUEUE, // 队列模型
		TOPIC; // 主题模型
	}

}
