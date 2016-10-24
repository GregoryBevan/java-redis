package com.elgregos.java.redis.cache.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.entities.key.DoubleKey;

@Component
public class CompositeKeyEntitySerialier implements RedisSerializer<DoubleKey> {

	private static final String KEY_SEPARATOR = "_";
	private final String KEY_PREFIX = "cke_";

	@Override
	public DoubleKey deserialize(byte[] bytes) throws SerializationException {
		final String[] keyParts = new String(bytes).split("_");
		return new DoubleKey(keyParts[1], keyParts[2]);
	}

	@Override
	public byte[] serialize(DoubleKey doubleKey) throws SerializationException {
		final StringBuilder serializedKey = new StringBuilder();
		serializedKey.append(KEY_PREFIX).append(doubleKey.getFirstCode()).append(KEY_SEPARATOR)
				.append(doubleKey.getSecondCode());
		return serializedKey.toString().getBytes();
	}

}
