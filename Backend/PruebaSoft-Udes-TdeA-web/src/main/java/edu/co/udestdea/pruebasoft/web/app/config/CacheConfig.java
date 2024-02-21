package edu.co.udestdea.pruebasoft.web.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class CacheConfig {
	
	@Bean
	RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
	    		.computePrefixWith(cacheName -> "PRUEBABABAB" + "::" +  cacheName + "::");
	    
	   return RedisCacheManager.builder(connectionFactory)
	            .cacheDefaults(config) 
	            .build();
	}
	
	

}
