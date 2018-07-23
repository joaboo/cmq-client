package com.cmq.client.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import com.cmq.client.common.util.ObjectUtils;
import com.cmq.client.config.ClientConfig;
import com.cmq.client.config.ClientConfig.ConsumerConfig;
import com.cmq.client.consumer.Consumer;
import com.cmq.client.consumer.DefaultConsumer;
import com.cmq.client.consumer.MessageListener;
import com.cmq.client.core.CMQClient;
import com.cmq.client.exception.CMQException;
import com.cmq.client.producer.DefaultProducer;
import com.cmq.client.producer.Producer;

public class CMQContainerInitializer implements BeanFactoryPostProcessor, ApplicationListener<ContextRefreshedEvent>, DisposableBean, Ordered {

	private ClientConfig clientConfig;
	private CMQClient cmqClient;
	private Producer producer;
	private List<Consumer> consumers;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;

		Map<String, CMQSpringConfigBean> cmqSpringConfigBeans = defaultListableBeanFactory.getBeansOfType(CMQSpringConfigBean.class);
		if (ObjectUtils.isEmpty(cmqSpringConfigBeans)) {
			throw new CMQException("CMQ configuration not found in spring context");
		} else if (cmqSpringConfigBeans.size() > 1) {
			throw new CMQException("More than one CMQ configuration found in spring context");
		}

		CMQSpringConfigBean cmqSpringConfigBean = cmqSpringConfigBeans.values().iterator().next();
		// 配置初始化
		initConfig(cmqSpringConfigBean.getClientConfig());
		// 生产者初始化
		initProducer();
		if (producer != null) {
			// 将producer注入到spring context
			registerBeanDefinition(defaultListableBeanFactory, Producer.class, producer);
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 消费者初始化
		initConsumer(event.getApplicationContext());
	}

	private void initConfig(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
		this.cmqClient = new CMQClient(clientConfig);
	}

	private void initProducer() {
		if (ObjectUtils.isNotEmpty(clientConfig.getProducerConfigs())) {
			producer = new DefaultProducer(cmqClient);
			producer.start();
		}
	}

	private void initConsumer(ApplicationContext applicationContext) {
		List<ConsumerConfig> consumerConfigs = clientConfig.getConsumerConfigs();
		if (ObjectUtils.isEmpty(consumerConfigs)) {
			return;
		}

		consumers = new ArrayList<>(consumerConfigs.size());
		for (ConsumerConfig consumerConfig : consumerConfigs) {
			Consumer consumer = new DefaultConsumer(consumerConfig, cmqClient);
			MessageListener messageListener = applicationContext.getBean(consumerConfig.getMessageListenerClass());
			if (messageListener == null) {
				throw new CMQException(String.format("MessageListener not found -> messageListenerClass:%s", consumerConfig.getMessageListenerClass()));
			}
			consumer.registerMessageListener(messageListener);
			consumer.start();
			consumers.add(consumer);
		}
	}

	private void registerBeanDefinition(DefaultListableBeanFactory defaultListableBeanFactory, Class<?> beanClass, Object bean) {
		String beanClassName = beanClass.getName();
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClassName);
		AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
		ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
		constructorArgumentValues.addGenericArgumentValue(beanClass);
		constructorArgumentValues.addGenericArgumentValue(bean);
		beanDefinition.setBeanClass(ProxyFactoryBean.class);
		beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		defaultListableBeanFactory.registerBeanDefinition(beanClassName, beanDefinition);
	}

	@Override
	public void destroy() throws Exception {
		if (producer != null) {
			producer.shutdown();
		}
		if (consumers != null) {
			consumers.forEach(Consumer::shutdown);
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}