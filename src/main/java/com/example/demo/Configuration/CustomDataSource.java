package com.example.demo.Configuration;

import com.example.demo.DataSource.EventuallyEmbeddedDataSourceProperties;
import com.example.demo.DataSource.ReadOnlyDataSource;
import com.example.demo.DataSource.TransactionRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.Map;

import static com.example.demo.DataSource.TransactionRoutingDataSource.DataSourceType.READ_ONLY;
import static com.example.demo.DataSource.TransactionRoutingDataSource.DataSourceType.READ_WRITE;

@Configuration
public class CustomDataSource {
    @Bean
    public BeanPostProcessor dialectProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof HibernateJpaVendorAdapter) {
                    ((HibernateJpaVendorAdapter) bean).getJpaDialect().setPrepareConnection(false);
                }
                return bean;
            }
        };
    }

    @Bean
    @ConfigurationProperties(prefix="product-readonly.datasource")
    public EventuallyEmbeddedDataSourceProperties metaDataReadOnlyProperties() {

        return new EventuallyEmbeddedDataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix="product-readwrite.datasource")
    public EventuallyEmbeddedDataSourceProperties metaDataReadWriteProperties() {
        return new EventuallyEmbeddedDataSourceProperties();
    }

    @Bean
    public ReadOnlyDataSource readOnlyDataSource(DataSourceProperties metaDataReadOnlyProperties,
                                                 @Value("${product-database.readonly.hikari.maximum-pool-size:1}") Integer maxPoolSize) {
        final var dataSource = metaDataReadOnlyProperties.initializeDataSourceBuilder()
                .type(ReadOnlyDataSource.class)
                .build();

        dataSource.setPoolName("readOnlyPool");
        dataSource.setAutoCommit(true);
        dataSource.setReadOnly(true);
        dataSource.setMaximumPoolSize(maxPoolSize);

        return dataSource;
    }

    @Bean
    public HikariDataSource readWriteDataSource(DataSourceProperties metaDataReadWriteProperties,
                                                @Value("${product-database.readwrite.hikari.maximum-pool-size:1}") Integer maxPoolSize) {
        final var dataSource = metaDataReadWriteProperties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();

        dataSource.setPoolName("readWritePool");
        dataSource.setMaximumPoolSize(maxPoolSize);


        return dataSource;
    }

    @Primary
    @Bean
    public TransactionRoutingDataSource transactionRoutingDataSource(ReadOnlyDataSource readOnlyDataSource,
                                                                     HikariDataSource readWriteDataSource) {
        final var transactionRoutingDataSource = new TransactionRoutingDataSource();
        transactionRoutingDataSource.setTargetDataSources(
                Map.of(READ_ONLY, readOnlyDataSource, READ_WRITE, readWriteDataSource)
        );
        return transactionRoutingDataSource;
    }


}

