package com.elgregos.java.redis;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import com.elgregos.java.redis.cache.CacheLoader;
import com.elgregos.java.redis.populate.Populate;
import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
@ComponentScan(basePackages = "com.elgregos.java.redis", excludeFilters = {
		@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { Populate.class }) })
@EnableJpaRepositories(basePackages = "com.elgregos.java.redis.entities")
public class RedisApplication {

	@Autowired
	private CacheLoader cacheLoader;

	private final Logger logger = LoggerFactory.getLogger(RedisApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {

		final JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("localhost");
		factory.setPort(6379);
		factory.setUsePool(true);

		return factory;
	}

	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				logger.info("ServletContext destroyed");
			}

			@Override
			public void contextInitialized(ServletContextEvent sce) {
				cacheLoader.loadCache();
				logger.info("ServletContext initialized");
			}

		};
	}

	@Bean
	CacheManager cacheManager() {
		final RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
		cacheManager.setDefaultExpiration(86400);
		return cacheManager;
	}

	@Bean
	RedisTemplate<Object, Object> redisTemplate() {
		final RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}
}
