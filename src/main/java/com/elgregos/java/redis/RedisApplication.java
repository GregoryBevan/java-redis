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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.elgregos.java.redis.cache.CacheLoader;
import com.elgregos.java.redis.populate.Populate;
import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
@ComponentScan(basePackages = "com.elgregos.java.redis", excludeFilters = {
		@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { Populate.class }) })
@EnableJpaRepositories(basePackages = "com.elgregos.java.redis.entities")
@EnableAspectJAutoProxy
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
}
