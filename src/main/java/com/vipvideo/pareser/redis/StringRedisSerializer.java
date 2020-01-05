package com.vipvideo.pareser.redis;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;

/**
 * 使用StringRedisSerializer做key的序列化时，StringRedisSerializer的泛型指定的是String，
 * 传其他对象就会报类型转换错误，在使用@Cacheable注解是key属性就只能传String进来。把这个序列化方式重写了，将泛型改成Object
 */
public class StringRedisSerializer implements RedisSerializer<Object> {
	private final Charset charset;

	private final String target = "\"";

	private final String replacement = "";

	public StringRedisSerializer() {
		this(Charset.forName("UTF8"));
	}

	public StringRedisSerializer(Charset charset) {
		Assert.notNull(charset, "Charset must not be null!");
		this.charset = charset;
	}

	@Override
	public String deserialize(byte[] bytes) {
		return (bytes == null ? null : new String(bytes, charset));
	}

	@Override
	public byte[] serialize(Object object) {
		String string = JSON.toJSONString(object);
		if (string == null) {
			return null;
		}
		string = string.replace(target, replacement);
		return string.getBytes(charset);
	}

}
