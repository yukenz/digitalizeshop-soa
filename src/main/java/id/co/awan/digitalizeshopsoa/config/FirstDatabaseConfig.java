package id.co.awan.digitalizeshopsoa.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "firstEntityManagerFactory",
        transactionManagerRef = "firstTransactionManager",
        //Repository
        basePackages = {"id.co.awan.digitalizeshopsoa.database.first.repo"}
)

public class FirstDatabaseConfig {

    private static final String DOMAIN_PATH = "id.co.awan.digitalizeshopsoa.database.first.domain";

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-first")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource firstDataSource(@Qualifier("firstDataSourceProperties") DataSourceProperties firstDataSourceProperties) {
        return firstDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory(@Qualifier("firstDataSource") DataSource firstDataSource, EntityManagerFactoryBuilder builder) {
        return builder.dataSource(firstDataSource)
                //Model
                .packages(DOMAIN_PATH)
                .persistenceUnit("one")
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager firstTransactionManager(@Qualifier("firstEntityManagerFactory") EntityManagerFactory firstEntityManagerFactory) {
        return new JpaTransactionManager(firstEntityManagerFactory);
    }

}
