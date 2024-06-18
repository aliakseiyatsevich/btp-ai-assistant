package com.github.marshallbaby.assistant.config.hibernate;

import com.github.marshallbaby.assistant.Application;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class HibernateConfig {

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                       CurrentTenantIdentifierResolver tenantIdentifierResolver) {
        final LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource);
        entityManager.setPackagesToScan(Application.class.getPackageName());

        entityManager.setJpaVendorAdapter(jpaVendorAdapter());

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        jpaProperties.put(Environment.FORMAT_SQL, true);

        entityManager.setJpaPropertyMap(jpaProperties);
        return entityManager;
    }

}
