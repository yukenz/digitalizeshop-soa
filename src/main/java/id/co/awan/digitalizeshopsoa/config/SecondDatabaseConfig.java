package id.co.awan.digitalizeshopsoa.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "secondEntityManagerFactory",
//        transactionManagerRef = "secondTransactionManager",
//        //Repository
//        basePackages = {"id.co.awan.digitalizeshopsoa.database.second.repo"}
//)
public class SecondDatabaseConfig {

    private static final String DOMAIN_PATH = "id.co.awan.digitalizeshopsoa.database.second.domain";


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-second")
    public DataSourceProperties secondDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource secondDatasource(@Qualifier("secondDatasourceProperties") DataSourceProperties secondDatasourceProperties) {
        return secondDatasourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(@Qualifier("secondDatasource") DataSource secondDatasource, EntityManagerFactoryBuilder builder) {
        return builder.dataSource(secondDatasource)
                //Model
                .packages(DOMAIN_PATH)
                .persistenceUnit("two")
                .build();
    }

    @Bean
    public PlatformTransactionManager secondTransactionManager(@Qualifier("secondEntityManagerFactory") EntityManagerFactory secondEntityManagerFactory) {
        return new JpaTransactionManager(secondEntityManagerFactory);
    }

}
