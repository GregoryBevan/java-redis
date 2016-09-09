package com.elgregos.java.redis.populate;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = { "com.elgregos.java.redis.entities", "com.elgregos.java.redis.populate" })
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(basePackages = "com.elgregos.java.redis.entities")
public class Populate {

	@Autowired
	private org.springframework.core.env.Environment env;

	@Autowired
	private SimpleEntityPopulate simpleEntityPopulate;

	@Autowired
	private CompositeKeyEntityPopulate compositeKeyEntityPopulate;

	public static void main(String... args) {
		final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Populate.class);
		final Populate populate = ctx.getBean(Populate.class);
		populate.simpleEntityPopulate.populate();
		populate.compositeKeyEntityPopulate.populate();
		ctx.close();
	}

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
}
