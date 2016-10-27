package com.elgregos.java.redis.cache.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

@Component
public class HierarchyValueKeySerialier implements RedisSerializer<Long> {

	private final String KEY_PREFIX = "hv_";

	@Override
	public Long deserialize(byte[] bytes) throws SerializationException {
		return Long.valueOf(new String(bytes).replace(KEY_PREFIX, ""));
	}

	@Override
	public byte[] serialize(Long id) throws SerializationException {
		return KEY_PREFIX.concat(id.toString()).getBytes();
	}

}
