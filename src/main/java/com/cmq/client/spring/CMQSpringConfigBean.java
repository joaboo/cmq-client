package com.cmq.client.spring;

import org.springframework.beans.factory.InitializingBean;

public class CMQSpringConfigBean extends CMQConfigBean implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		super.valid();
	}

}
