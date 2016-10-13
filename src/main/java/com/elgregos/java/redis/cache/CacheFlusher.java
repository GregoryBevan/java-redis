package com.elgregos.java.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class CacheFlusher {

	@Autowired
	private RedisConnectionFactory jedisConnectionFactory;

	public void flushCache() {
		jedisConnectionFactory.getConnection().flushAll();
	}

}
