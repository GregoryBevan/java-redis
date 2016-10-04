package com.elgregos.java.redis.conf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdInputStream;

public class CustomRedisSerializer implements RedisSerializer<Object> {

	private static final byte[] EMPTY_ARRAY = new byte[0];

	private final ObjectMapper objectMapper;

	public CustomRedisSerializer() {
		this.objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
		objectMapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		objectMapper.registerModule(new JodaModule());
		final Hibernate5Module hibernate5Module = new Hibernate5Module();
		hibernate5Module.disable(Feature.FORCE_LAZY_LOADING);
		objectMapper.registerModule(hibernate5Module);
	}

	@Override
	public Object deserialize(byte[] source) throws SerializationException {
		if (source == null || source.length == 0) {
			return null;
		}
		try {

			return objectMapper.readValue(decompress(source), Object.class);
		} catch (final Exception ex) {
			throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}

	@Override
	public byte[] serialize(Object source) throws SerializationException {
		if (source == null) {
			return EMPTY_ARRAY;
		}
		try {
			return Zstd.compress(objectMapper.writeValueAsBytes(source));
		} catch (final JsonProcessingException e) {
			throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
		}
	}

	private byte[] decompress(byte[] source) {
		try (final ByteArrayInputStream inStream = new ByteArrayInputStream(source);
				final ZstdInputStream zstdInputStream = new ZstdInputStream(inStream);
				final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			final byte[] buffer = new byte[4096];
			int bytesRead = 0;

			;
			while ((bytesRead = zstdInputStream.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}

			final byte[] uncompressedData = output.toByteArray();
			return uncompressedData;
		} catch (final Exception e) {
			throw new RuntimeException();
		}
	}

}
