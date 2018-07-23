package com.cmq.client.common.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
	private static final AtomicInteger poolNumber = new AtomicInteger(1);

	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;
	private final boolean daemon;

	public NamedThreadFactory() {
		this("pool-" + poolNumber.getAndIncrement());
	}

	public NamedThreadFactory(String prefix) {
		this(prefix, false);
	}

	public NamedThreadFactory(String prefix, boolean daemon) {
		this.namePrefix = prefix + "-thread-";
		this.daemon = daemon;
		SecurityManager s = System.getSecurityManager();
		this.group = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
	}

	@Override
	public Thread newThread(Runnable runnable) {
		String name = namePrefix + threadNumber.getAndIncrement();
		Thread thread = new Thread(group, runnable, name, 0);
		thread.setDaemon(daemon);
		if (thread.getPriority() != Thread.NORM_PRIORITY) {
			thread.setPriority(Thread.NORM_PRIORITY);
		}
		return thread;
	}

}
