package com.elgregos.java.redis.test;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.elgregos.java.redis.entities", "com.elgregos.java.redis.cache",
		"com.elgregos.java.redis.service" })
@EnableJpaRepositories("com.elgregos.java.redis.entities")
@PropertySource("classpath:application.properties")
@Profile("test")
public class TestConfig {

	@Autowired
	private org.springframework.core.env.Environment env;

	@Bean
	public HikariDataSource dataSource() {
		return new HikariDataSource(hikariConfig());
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.elgregos.java.redis.entities");
		factory.setDataSource(dataSource());
		factory.setJpaProperties(jpaProperties());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {

		final JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("localhost");
		factory.setPort(6379);
		factory.setUsePool(true);

		return factory;
	}

	public Properties jpaProperties() {
		final Properties jpaProperties = new Properties();
		jpaProperties.put(Environment.DIALECT, env.getProperty("spring.jpa.properties.hibernate.dialect"));
		jpaProperties.put(Environment.HBM2DDL_AUTO, env.getProperty("spring.jpa.properties.hibernate.ddl-auto"));
		jpaProperties.put(Environment.DEFAULT_SCHEMA, env.getProperty("spring.datasource.schema"));
		jpaProperties.put(Environment.SHOW_SQL, env.getProperty("spring.jpa.properties.show-sql"));
		jpaProperties.put(Environment.FORMAT_SQL, env.getProperty("spring.jpa.properties.format-sql"));
		jpaProperties.put(Environment.PHYSICAL_NAMING_STRATEGY,
				"org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");

		return jpaProperties;
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
		final JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(emf);
		return txManager;
	}

	@Bean
	CacheManager cacheManager() {
		final RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
		cacheManager.setDefaultExpiration(86400);
		return cacheManager;
	}

	@Bean
	HikariConfig hikariConfig() {
		final HikariConfig config = new HikariConfig();
		config.setJdbcUrl(env.getProperty("spring.datasource.url"));
		config.setUsername(env.getProperty("spring.datasource.username"));
		config.setPassword(env.getProperty("spring.datasource.password"));
		config.setDataSourceClassName(env.getProperty("spring.datasource.dataSourceClassName"));
		config.setPoolName(env.getProperty("spring.datasource.poolName"));
		config.setMaximumPoolSize(Integer.parseInt(env.getProperty("spring.datasource.maximumPoolSize")));
		config.setMinimumIdle(Integer.parseInt(env.getProperty("spring.datasource.minimumIdle")));
		config.setConnectionTimeout(Long.parseLong(env.getProperty("spring.datasource.connectionTimeout")));
		config.setIdleTimeout(Long.parseLong(env.getProperty("spring.datasource.idleTimeout")));
		config.setMaxLifetime(Long.parseLong(env.getProperty("spring.datasource.maxLifetime")));

		return config;
	}

	@Bean
	RedisTemplate<String, String> redisTemplate() {
		final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}
}
