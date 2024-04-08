package com.company.app.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.company.app.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.company.app.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.company.app.domain.Authority.class.getName());
            createCache(cm, com.company.app.domain.Venta.class.getName());
            createCache(cm, com.company.app.domain.Coche.class.getName());
            createCache(cm, com.company.app.domain.Empleado.class.getName());
            createCache(cm, com.company.app.domain.Empleado.class.getName() + ".ventas");
            createCache(cm, com.company.app.domain.Cliente.class.getName());
            createCache(cm, com.company.app.domain.Cliente.class.getName() + ".ventas");
            createCache(cm, com.company.app.domain.Marca.class.getName());
            createCache(cm, com.company.app.domain.Marca.class.getName() + ".fkMarcaModelos");
            createCache(cm, com.company.app.domain.Modelo.class.getName());
            createCache(cm, com.company.app.domain.Marca.class.getName() + ".fkMarcaCoches");
            createCache(cm, com.company.app.domain.Marca.class.getName() + ".marcas");
            createCache(cm, com.company.app.domain.Marca.class.getName() + ".modelos");
            createCache(cm, com.company.app.domain.Modelo.class.getName() + ".coches");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
