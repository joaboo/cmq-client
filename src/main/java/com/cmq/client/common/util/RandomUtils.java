package com.cmq.client.common.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

	public static int randomInt() {
		return Math.abs(ThreadLocalRandom.current().nextInt());
	}
}
