package id.co.awan.digitalizeshopsoa.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class CachingConfig {

    // Cache for BackendEntity
    @Bean
    CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(new ConcurrentMapCache("banckendEntity")));
        return cacheManager;
    }

    // Refresh Cache Every 10 Minutes
    @CacheEvict(allEntries = true, value = {"banckendEntity"})
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 500)
    public void backendEntityCacheEvict() {
        log.info("Flush Cache backendEntity");
    }

}