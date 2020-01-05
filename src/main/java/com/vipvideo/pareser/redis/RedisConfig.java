package com.vipvideo.pareser.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.parser.ParserConfig;

/**
 * 重写Redis序列化方式，使用Json方式:
 * 当我们的数据存储到Redis的时候，我们的键（key）和值（value）都是通过Spring提供的Serializer序列化到数据库的。
 * RedisTemplate默认使用的是JdkSerializationRedisSerializer，
 * StringRedisTemplate默认使用的是StringRedisSerializer。 Spring Data
 * JPA为我们提供了下面的Serializer：
 * GenericToStringSerializer、Jackson2JsonRedisSerializer、
 * JacksonJsonRedisSerializer、JdkSerializationRedisSerializer、OxmSerializer、
 * StringRedisSerializer。 在此我们将自己配置RedisTemplate并定义Serializer。
 */
@Configuration
public class RedisConfig {
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
		// fastjson在2017年3月爆出了在1.2.24以及之前版本存在远程代码执行高危安全漏洞。
		// 所以要使用ParserConfig.getGlobalInstance().addAccept("com.xiaolyuh.")包的名字;指定序列化白名单
		// ParserConfig.getGlobalInstance().addAccept("danwozhineng");
		// 或者全局设定
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

		// 设置值（value）的序列化采用FastJsonRedisSerializer。
		redisTemplate.setValueSerializer(fastJsonRedisSerializer);
		redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
		// 设置键（key）的序列化采用StringRedisSerializer。
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());

		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
