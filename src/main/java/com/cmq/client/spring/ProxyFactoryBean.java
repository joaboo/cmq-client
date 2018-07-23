package com.cmq.client.spring;

import org.springframework.beans.factory.FactoryBean;

public class ProxyFactoryBean implements FactoryBean<Object> {

	private final Class<Object> objectType;
	private final Object object;

	public ProxyFactoryBean(Class<Object> objectType, Object object) {
		this.objectType = objectType;
		this.object = object;
	}

	@Override
	public Object getObject() throws Exception {
		return object;
	}

	@Override
	public Class<Object> getObjectType() {
		return objectType;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
