# cmq-client
近期项目中使用到CMQ，鉴于官方提供SDK太过简陋且存在严重BUG，于是自己简单的实现了一个，虽只实现了routingKey相关部分，但也足够一般使用。此外虽然本项目实现了应用启动时自动创建Topic、Queue、Subscription相关功能，但由于实际场景复杂，仍建议通过CMQ控制台进行相关创建。时间仓促，细节之处多有瑕疵，复杂场景测试不全，故仅供参考。

```java
// 配置类
@EnableCMQ
@Configuration
public class CMQConfig {
	@Bean
	public CMQSpringConfigBean cmqSpringConfigBean() {
		CMQSpringConfigBean config = new CMQSpringConfigBean();
		config.setSecretId("xxx");
		config.setSecretKey("xxx");
		config.setUrlConfigBean(new URLConfigBean("sh", true));
		config.addProducerConfigBean(new ProducerConfigBean("topic1"));
		config.addProducerConfigBean(new ProducerConfigBean("topic2", true));
		config.addConsumerConfigBean(new ConsumerConfigBean("queue1", Consumer1.class));

		ConsumerConfigBean consumer2 = new ConsumerConfigBean("queue2", Consumer2.class);
		consumer2.setAutoCreateQueue(true);
		SubscriptionBean subscriptionBean2 = new SubscriptionBean("sub-topic1-queue3", "topic1", Arrays.asList("key2"), true, true);
		consumer2.setSubscriptionBeans(Arrays.asList(subscriptionBean2));
		config.addConsumerConfigBean(consumer2);
		return config;
	}
}

// 发送消息
@Autowired
private Producer producer;
producer.send("topicName", "routingKey", "message info");

// 消费消息
@Service
public class Consumer1 implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.err.println("Consumer1: " + message);
	}

}
```

